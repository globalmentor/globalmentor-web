package com.garretwilson.rdf;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import com.garretwilson.net.URIConstants;
import com.garretwilson.rdf.rdfs.RDFSUtilities;
import com.garretwilson.rdf.xmlschema.XMLSchemaTypedLiteralFactory;
import com.garretwilson.text.xml.schema.XMLSchemaConstants;
import com.garretwilson.util.*;

/**An RDF data model.
<p>The data model should be used to create resources, as it keeps a list of
	registered resource factories based upon resource type namespaces.</p>
<p>The RDF data model itself is a resource factory for the default RDF resources
	such as bags, but registering a resource factory for the RDF namespace will
	override this default behavior.</p>
<p>The RDF data model itself is a typed literal factory for the default RDF
	datatypes (currently XMLLiteral), but registering a resource factory for the
	RDF namespace will override this default behavior.</p>
<p>The RDF data model by default registers a factories that handle the following
	datatype namespaces:</p>
<dl>
	<dt>XML Schema</dt> <dd>http://www.w3.org/2001/XMLSchema</dd>
</dl>
@author Garret Wilson
*/
public class RDF implements RDFConstants	//TODO special-case rdf:nil list resources so that they are not located, but a different instance is created for each one, to make RDFListResource convenience methods work correctly 
{

	/**The next ID to use in an anonymous resource reference URI.*/
//G***del when works	private int nextAnonymousID=1;

		/**@return A new reference URI for an anonymous resource.
		@exception URISyntaxException Thrown if an anonymous URI cannot be created.
		*/
/*G***del when works
		URI createAnonymousReferenceURI()
		{
			final int anonymousID=nextAnonymousID++;  //get the next anonymous ID and increment the counter
			try
			{
				return new URI("anonymous", Integer.toString(anonymousID), null);	//return an anonymous reference URI using the ID G***use a constant here
			}
			catch (URISyntaxException e)
			{
				Debug.error("could not create anonymous URI");	//G***fix better
				return null;	//G***fix better
			}	
		}
*/

	/**The map of prefixes, keyed by namespace URIs, used for serialization,
		lazily constructed.
	*/
//G***del if not needed	private Map namespacePrefixMap;

		/**@return The map of prefixes, keyed by namespace URI; one will be created
		  if it does not already exist.
		*/
/*G***del if not needed
		public Map getNamespacePrefixMap()
		{
			if(namespacePrefixMap==null)  //if we don't yet have a namespace prefix map
			{
				namespacePrefixMap=XMLSerializer.createNamespacePrefixMap();  //create a map of default XML namespace prefixes
			}
			return namespacePrefixMap;
		}
*/

	/**The base URI of the RDF data model, or <code>null</code> if unknown.*/
	private final URI baseURI;
	
		/**@return The base URI of the RDF data model, or <code>null</code> if unknown.*/
		public URI getBaseURI() {return baseURI;}

	/**A map of resource factories, keyed to namespace URI.*/
	private final Map resourceFactoryMap=new HashMap();

		/**Registers a resource factory to be used to create resources with a type
		  from the specified namespace. If a resource factory is already registered
			for this namespace, it will be replaced.
		@param typeNamespaceURI The namespace of the resource type for which this
			factory should be used to create objects.
		@param factory The resource factory that will be used to create resources
			of types from this namespace.
		*/
		public void registerResourceFactory(final URI typeNamespaceURI, final RDFResourceFactory factory)
		{
			resourceFactoryMap.put(typeNamespaceURI, factory);
		}

		/**Removes the resource factory being used to create resources with a type
			from the specified namespace. If there is no resource factory registered
			for this namespace, no action will be taken.
		@param typeNamespaceURI The namespace of the resource type for which this
			factory should be used to create objects.
		*/
		public void unregisterResourceFactory(final URI typeNamespaceURI)
		{
			resourceFactoryMap.remove(typeNamespaceURI);
		}

		/**Retrieves a resource factory to be used for creating resources with a
		  type from the specified namespace URI.
		@param typeNamespaceURI The namespace of the type for which a resource
			factory should be returned.
		@return The factory registered for this type namespace, or <code>null</code>
			if there is no factory registered for this type namespace.
		*/
		protected RDFResourceFactory getResourceFactory(final URI typeNamespaceURI)
		{
			return (RDFResourceFactory)resourceFactoryMap.get(typeNamespaceURI);  //return any factory registered for this namespace
		}

	/**A map of typed literal factories, keyed to datatype namespace URI.*/
	private final Map typedLiteralFactoryMap=new HashMap();

		/**Registers a typed literal factory to be used to create literals with a
			datatype from the specified namespace. If a type literal factory is
			already registered for this namespace, it will be replaced.
		@param datatypeNamespaceURI The namespace of the datatype for which this
			factory should be used to create typed literals.
		@param factory The typed literal factory that will be used to create literals
			with datatypes from this namespace.
		*/
		public void registerTypedLiteralFactory(final URI datatypeNamespaceURI, final RDFTypedLiteralFactory factory)
		{
			typedLiteralFactoryMap.put(datatypeNamespaceURI, factory);
		}

		/**Removes the typed literal factory being used to create literals with a
			datatype from the specified namespace. If there is no typed litreal
			factory registered for this namespace, no action will be taken.
		@param datatypeNamespaceURI The namespace of the datatype for which this
			factory should be used to create typed literals.
		*/
		public void unregisterTypedLiteralFactory(final URI datatypeNamespaceURI)
		{
			typedLiteralFactoryMap.remove(datatypeNamespaceURI);
		}

		/**Retrieves a typed literal factory to be used for creating literals with
			a datatype from the specified namespace URI.
		<p>To accommodate vocabularies with different namespace URI mapping rules
			(such as RDF and and XML Schema) which result in different namespace
			representations, if the namespace URI ends in a fragment separator ('#')
			and no factory is found, an attempt is made to look up a factory using the
			namespace URI without that trailing fragment separator character.</p>
		@param datatypeNamespaceURI The namespace of the datatype for which a
			typed literal factory should be returned.
		@return The factory registered for this datatype namespace, or
			<code>null</code> if there is no factory registered for this datatype
			namespace.
		*/
		protected RDFTypedLiteralFactory getTypedLiteralFactory(final URI datatypeNamespaceURI)
		{
			RDFTypedLiteralFactory typedLiteralFactory=(RDFTypedLiteralFactory)typedLiteralFactoryMap.get(datatypeNamespaceURI);  //get any factory registered for this namespace
			if(typedLiteralFactory==null && datatypeNamespaceURI!=null)	//if we didn't find a factory, try the namespaceURI without its ending # (RDF has different URI generation rules than, for example, XML Schema, resulting in different namespace representations)
			{
				final String datatypeNamespaceURIString=datatypeNamespaceURI.toString();	//get the string form of the datatype namespace URI 
					//if this URI ends with '#' and has data before that character
				if(datatypeNamespaceURIString.length()>1 && datatypeNamespaceURIString.charAt(datatypeNamespaceURIString.length()-1)==URIConstants.FRAGMENT_SEPARATOR)
				{
					try	//see if we recognize the URI without the ending '#'
					{
						typedLiteralFactory=(RDFTypedLiteralFactory)typedLiteralFactoryMap.get(new URI(datatypeNamespaceURIString.substring(0, datatypeNamespaceURIString.length()-1)));
					}
					catch(final URISyntaxException URISyntaxException)	//if we can't create a valid URI by remove the fragment separator character, don't do anything---there wouldn't be a factory mapped to that value, anyway 
					{
					}
				}			
			}
			return typedLiteralFactory;	//return whatever typed literal we found, if any
		}

	/**The set of all resources, named and unnamed.*/
	private final Set resourceSet=new HashSet();

	/**The map of all named resources, keyed to resource reference URI.*/
	private final Map resourceMap=new HashMap();

	/**Adds a resource to the data model.
	If the resource is already in the model, no action occurs. 
	@param resource The resource to add.
	*/
	public void addResource(final RDFResource resource)
	{
//G***del Debug.trace("putting resource with URI: ", resource.getReferenceURI());
//G***del Debug.traceStack(); //G***del

		resourceSet.add(resource);	//add the resource to our set
		if(resource.getReferenceURI()!=null)	//if this is not a blank node
			resourceMap.put(resource.getReferenceURI(), resource);  //store the resource in the map
	}

	/**Retrieves a named resource from the data model using its reference URI.
	@param resourceURI The reference URI of the resource to retreive.
	@return The resource, or <code>null</code> if no matching resource was found.
	*/
	public RDFResource getResource(final URI resourceURI)
	{
//G***del Debug.trace("getting resource with URI: ", resourceURI);
//G***del Debug.traceStack(); //G***del
		return (RDFResource)resourceMap.get(resourceURI); //retrieve the resource
	}

	/**@return The number of resources in this data model.*/
	public int getResourceCount()
	{
		return resourceSet.size();  //return the size of the resource set
	}

	/**@return A read-only iterator of resources.*/
	public Iterator getResourceIterator()
	{
		return Collections.unmodifiableCollection(resourceSet).iterator(); //return an unmodifiable iterator to the set of all resources
	}

	/**@return A read-only iterator of resources appropriate for appearing at the
		root of a hierarchy, such as an RDF tree or an RDF+XML serialization.
	*/
	public Iterator getRootResourceIterator()
	{
		return getRootResourceIterator(null);	//return an unsorted iterator to the root resources 
	}

	/**Returns a read-only iterator of resources appropriate for appearing at the
		root of a hierarchy, such as an RDF tree or an RDF+XML serialization. The
		resources are sorted using the optional comparator.
	@param comparator The object that determines how the resources will be sorted,
		or <code>null</code> if the resources should not be sorted.
	@return A read-only iterator of root resources sorted by the optional
		comparator.
	*/
	public Iterator getRootResourceIterator(final Comparator comparator)
	{
			//create a set in which to place the root resources, making the set sorted if we have a comparator
		final Set rootResourceSet=comparator!=null ? (Set)new TreeSet(comparator) : (Set)new HashSet();	 		
		final Iterator resourceIterator=getResourceIterator();  //get an iterator to all the RDF resources
		while(resourceIterator.hasNext()) //while there are resources remaining
		{
			final RDFResource resource=(RDFResource)resourceIterator.next();  //get the next resource
			if(isRootResource(resource))	//if this is a root resource
			{
				rootResourceSet.add(resource);	//add the resource to the set of root resources
			}
		}
		return Collections.unmodifiableCollection(rootResourceSet).iterator(); //return an unmodifiable iterator to the set of root resources
	}

	/**Determines if the given resource is appropriate for appearing at the root
		of a hierarchy, such as an RDF tree or an RDF+XML serialization.
	<p>This should be determined, among other things, by whether the resource
		 in question is a property and whether or not there are references to the
		 resource. This implementation considers root resources to be those
		 that have a URI and have at least one property, along with those that
		 have labels.</p> 
	@param resource The resource which might be a root resource.
	@return <code>true</code> if this resource is one of the resources that
		should be presented at the root of a hierarchy.
	*/
	public boolean isRootResource(final RDFResource resource)
	{
		final URI referenceURI=resource.getReferenceURI(); //get the resource reference URI
		final RDFLiteral label=RDFSUtilities.getLabel(resource);	//see if this resource has a label
//TODO eventually we'll probably have to determine if something is actually a property---i.e. this doesn't work: if(resource.getReferenceURI()!=null || resource.getPropertyCount()>0)	//only show resources that have reference URIs or have properties, thereby not showing property resources and literals at the root
			//if this is not a blank node and this resource actually has properties (even properties such as type identifiers are resources, but they don't have properties)
		return (referenceURI!=null && resource.getPropertyCount()>0)
					|| label!=null;	//if a resource is labeled, it's probably important enough to show at the top of the hierarchy as well 
		
	}

	/**Retreives a resource from the data model based upon a URI. If no such
		resource exists, one will be created and added to the data model.
	@param referenceURI The reference URI of the resource to retrieve.
	@return A resource with the given reference URI.
	*/
	public RDFResource locateResource(final URI referenceURI)
	{
		return locateResource(referenceURI, null, null);	//locate a resource without knowing its type
	}

	/**Retreives a resource from the data model based upon an XML namespace URI
		and an XML local name. If no such resource exists, one will be created and
		added to the data model.
	@param namespaceURI The XML namespace URI used in the serialization.
	@param localName The XML local name used in the serialization.
	@return A resource with a reference URI corresponding to the given namespace
		URI and local name.
	*/
	public RDFResource locateResource(final URI namespaceURI, final String localName)
	{
		return locateResource(RDFUtilities.createReferenceURI(namespaceURI, localName), namespaceURI, localName);	//locate a resource, constructing a reference URI from the given namespace URI and local name
	}

	/**Retreives a resource from the data model based upon an XML namespace URI
		and an XML local name. If no such resource exists, one will be created and
		added to the data model.
	@param referenceURI The reference URI of the resource to retrieve.
	@param namespaceURI The XML namespace URI used in the serialization, or
		<code>null</code> if the namespace URI is not known.
	@param localName The XML local name used in the serialization, or
		<code>null</code> if the local name is not known.
	@return A resource with a reference URI corresponding to the given namespace
		URI and local name.
	*/
	protected RDFResource locateResource(final URI referenceURI, final URI namespaceURI, final String localName)
	{
		return locateTypedResource(referenceURI, namespaceURI, localName, null, null);	//locate a resource, constructing a reference URI from the given namespace URI and local name
	}

	/**Retrieves a resource from the data model based upon the reference URI of
		the resource. If no such resource exists, one will be created and added to
		the data model. The given type serialization XML namespace URI and local
		name will be used to locate a resource factory to create the resource, and
		the type URI derived from the namespace URI and local name will be added
		as a type property.
		If the resource already exists, no checks are performed to ensure that the
		existing resource is of the requested type.
	@param referenceURI The reference URI of the resource to retrieve.
	@param typeNamespaceURI The XML namespace used in the serialization of the
		type URI, or <code>null</code> if the type is not known.
	@param typeLocalName The XML local name used in the serialization of the type
		URI, or <code>null</code> if the type is not known.
	@return A resource with the given reference URI.
	*/
	public RDFResource locateTypedResource(final URI referenceURI, final URI typeNamespaceURI, final String typeLocalName)
	{
		return locateTypedResource(referenceURI, null, null, typeNamespaceURI, typeLocalName);	//locate a resource with no namespace URI or local name
	}

	/**Retrieves a resource from the data model based upon the reference URI of
		the resource. If no such resource exists, one will be created and added to
		the data model. The given type serialization XML namespace URI and local
		name will be used to locate a resource factory to create the resource, and
		the type URI derived from the namespace URI and local name will be added
		as a type property.
		If the resource already exists, no checks are performed to ensure that the
		existing resource is of the requested type.
	@param referenceURI The reference URI of the resource to retrieve.
	@param namespaceURI The XML namespace URI used in the serialization, or
		<code>null</code> if the namespace URI is not known.
	@param localName The XML local name used in the serialization, or
		<code>null</code> if the local name is not known.
	@param typeNamespaceURI The XML namespace used in the serialization of the
		type URI, or <code>null</code> if the type is not known.
	@param typeLocalName The XML local name used in the serialization of the type
		URI, or <code>null</code> if the type is not known.
	@return A resource with the given reference URI.
	*/
	RDFResource locateTypedResource(final URI referenceURI, final URI namespaceURI, final String localName, final URI typeNamespaceURI, final String typeLocalName)
	{
		RDFResource resource=getResource(referenceURI);  //retrieve a resource from the data model
		if(resource==null)  //if no such resource exists
		{
			resource=createTypedResource(referenceURI, namespaceURI, localName, typeNamespaceURI, typeLocalName);  //create a new resource from the given reference URI and store the resource in the data model
		}
		return resource;  //return the resource we either found or created
	}

	/**Creates an anonymous resource and stores it in this RDF data model.
	@return An anonymous resource with a generated anonymous reference URI.
	@see #createAnonymousReferenceURI
	*/
	public RDFResource createResource()
	{
		return createResource(null);  //create a resource with an anonymous reference URI
//G***del		return createResource(createAnonymousReferenceURI()); //create a resource with an anonymous reference URI
	}

	/**Creates a general resource with the specified reference URI and stores it in
		this RDF data model.
	@param referenceURI The reference URI of the resource to create, or
		<code>null</code> if the resource created should be represented by a blank node.
	@return A resource with the given reference URI.
	*/
	public RDFResource createResource(final URI referenceURI)
	{
		return createResource(referenceURI, null, null);	//create and return a resource without a namespace URI or local name
	}

	/**Creates a general resource with the a reference URI based upon an XML
		namespace URI and an XML local name, and stores the resource in this RDF
		data model.
	@param namespaceURI The XML namespace URI used in the reference URI serialization.
	@param localName The XML local name used in the reference URI serialization.
	@return A resource with a reference URI corresponding to the given namespace
		URI and local name.
	*/
	public RDFResource createResource(final URI namespaceURI, final String localName)
	{
		return createResource(RDFUtilities.createReferenceURI(namespaceURI, localName), namespaceURI, localName);	//create and return a resource with a reference URI constructed from the namespace URI and local name
	}

	/**Creates a general resource with the a reference URI based upon an XML
		namespace URI and an XML local name, and stores the resource in this RDF
		data model.
	@param referenceURI The reference URI of the resource to create, or
		<code>null</code> if the resource created should be represented by a blank node.
	@param namespaceURI The XML namespace URI used in the serialization, or
		<code>null</code> if the namespace URI is not known.
	@param localName The XML local name used in the serialization, or
		<code>null</code> if the local name is not known.
	@return A resource with a reference URI corresponding to the given namespace
		URI and local name.
	*/
	public RDFResource createResource(final URI referenceURI, final URI namespaceURI, final String localName)
	{
		return createTypedResource(referenceURI, namespaceURI, localName, null, null);	//create a resource without knowing its type
	}

	/**Creates a resource with the provided reference URI.
		The given type XML namespace URI and type XML local name will be used to
		attempt to locate a resource factory to create the resource.
		A type property derived from the specified type namespace URI and local name
		will be added to the resource.
	<p>This method knows how to create the following RDF-defined resources:</p>
	<ul>
		<li>Reference URI <code>rdf:nil</code> (<code>RDFListResource</code>)</li>
		<li>Type <code>rdf:Alt</code>  (<code>RDFAltResource</code>)</li>
		<li>Type <code>rdf:Bag</code>  (<code>RDFBagResource</code>)</li>
		<li>Type <code>rdf:Seq</code>  (<code>RDFSequenceResource</code>)</li>
	</ul>
	<p>The created resource will be stored in this RDF data model.</p>
	@param referenceURI The reference URI of the resource to create, or
		<code>null</code> if the resource created should be represented by a blank node.
	@param typeNamespaceURI The XML namespace used in the serialization of the
		type URI, or <code>null</code> if the type is not known.
	@param typeLocalName The XML local name used in the serialization of the type
		URI, or <code>null</code> if the type is not known.
	@return The resource created with this reference URI, with the given type
		added if a type was given.
	*/
	public RDFResource createTypedResource(final URI referenceURI, final URI typeNamespaceURI, final String typeLocalName)
	{
		return createTypedResource(referenceURI, null, null, typeNamespaceURI, typeLocalName);	//create a typed resource with no namespace URI or local name
	}

	/**Creates a resource with the provided reference URI.
		The given type XML namespace URI and type XML local name will be used to
		attempt to locate a resource factory to create the resource.
		A type property derived from the specified type namespace URI and local name
		will be added to the resource.
	<p>This method knows how to create the following RDF-defined resources:</p>
	<ul>
		<li>Reference URI <code>rdf:nil</code> (<code>RDFListResource</code>)</li>
		<li>Type <code>rdf:Alt</code>  (<code>RDFAltResource</code>)</li>
		<li>Type <code>rdf:Bag</code>  (<code>RDFBagResource</code>)</li>
		<li>Type <code>rdf:Seq</code>  (<code>RDFSequenceResource</code>)</li>
	</ul>
	<p>The created resource will be stored in this RDF data model.</p>
	@param referenceURI The reference URI of the resource to create, or
		<code>null</code> if the resource created should be represented by a blank node.
	@param namespaceURI The XML namespace URI used in the serialization, or
		<code>null</code> if the namespace URI is not known.
	@param localName The XML local name used in the serialization, or
		<code>null</code> if the local name is not known.
	@param typeNamespaceURI The XML namespace used in the serialization of the
		type URI, or <code>null</code> if the type is not known.
	@param typeLocalName The XML local name used in the serialization of the type
		URI, or <code>null</code> if the type is not known.
	@return The resource created with this reference URI, with the given type
		added if a type was given.
	*/
	protected RDFResource createTypedResource(final URI referenceURI, final URI namespaceURI, final String localName, final URI typeNamespaceURI, final String typeLocalName)
	{
//G***del when works		Debug.assert(typeNamespaceURI!=null, "type namespace is null");	//G***fix
		RDFResource resource=null; //start by assuming that no factory is registered for this type namespace, or the registered factory can't create a resource
		final RDFResourceFactory resourceFactory=getResourceFactory(typeNamespaceURI); //get a resource factory for this namespace
		if(resourceFactory!=null) //if we have a factory
		{
		  resource=resourceFactory.createResource(referenceURI, typeNamespaceURI, typeLocalName); //try to create a resource from this factory
		}
		if(resource==null)  //if we haven't created a resource, see if this is an RDF resource
		{
			if(NIL_RESOURCE_URI.equals(referenceURI))	//if we are creating the nil resource
			{
				resource=new RDFListResource(this, RDF_NAMESPACE_URI, NIL_RESOURCE_NAME);	//create the nil resource with the special RDF nil URI
			}
			else if(RDF_NAMESPACE_URI.equals(typeNamespaceURI)) //if this resource is an RDF resource
			{
				if(ALT_CLASS_NAME.equals(typeLocalName)) //<rdf:Alt>
				{
					//G***fix for alt
				}
				else if(BAG_CLASS_NAME.equals(typeLocalName))  //<rdf:Bag>
				{
					resource=new RDFBagResource(this, referenceURI, namespaceURI, localName);  //create a bag resource
				}
				else if(SEQ_CLASS_NAME.equals(typeLocalName))  //<rdf:Seq>
				{
					resource=new RDFSequenceResource(this, referenceURI, namespaceURI, localName);  //create a sequence resource
				}
				else if(LIST_CLASS_NAME.equals(typeLocalName))  //<rdf:Seq>
				{
					resource=new RDFListResource(this, referenceURI, namespaceURI, localName);  //create a list resource
				}
//G***should we check to see if this is the nil resource, and if so automatically set its type to rdf:List?
			}
		}
		if(resource==null)  //if we still haven't created a resource
		{
		  resource=new DefaultRDFResource(this, referenceURI, namespaceURI, localName);  //create a new resource from the given reference URI, showing which data model created the resource
		}
		if(resource!=null && resource.getRDF()!=this)	//if a resource was created that isn't associated with this data model
		{
			resource.setRDF(this);	//associate the resource with this data model TODO create an import() method that will recursively set the data models of the resource and all properties and property values
		}
		if(typeNamespaceURI!=null && typeLocalName!=null)	//if we were given a type
		{
			RDFUtilities.addType(resource, typeNamespaceURI, typeLocalName); //add the type property TODO make sure the type is added to the list of triples
		}
		addResource(resource);  //store the resource in the data model
		return resource;  //return the resource we created
	}

	/**Creates a typed literal from the provided lexical form and datatype.
	The determined datatype namespace URI from the given datatype URI
		will be used to attempt to locate a typed literal factory to create the
		typed literal.
	<p>If no typed literal resource factory can be found, a generic
		<code>RDFTypedLiteral</code> will be created using the lexical form as the
		object.</p>
	<p>This method knows how to create the following RDF-defined typed literals:</p>
	<ul>
		<li>Datatype <code>rdf:XMLLiteral</code>  (<code>RDFXMLLiteral</code>)</li>
	</ul>
	@param lexicalForm The lexical form of the resource.
	@param datatypeURI The datatype reference URI of the datatype.
	@return The typed literal with the given datatype with a value created by
		a lexical form to value mapping.
	*/
	public RDFTypedLiteral createTypedLiteral(final String lexicalForm, final URI datatypeURI)
	{
		Debug.assert(datatypeURI!=null, "datatype is null");	//G***fix
			//create an anonymous reference URI if needed
//G***del when works		final URI resourceReferenceURI=referenceURI!=null ? referenceURI : createAnonymousReferenceURI();
//G***del Debug.trace("Ready to create resource with reference URI: ", referenceURI); //G***del
//G***del Debug.trace("Type namespace URI: ", typeNamespaceURI); //G***del
//G***del Debug.trace("Type local name: ", typeLocalName); //G***del


			//try to get the namespace of the datatype
		final URI datatypeNamespaceURI=RDFUtilities.getNamespaceURI(datatypeURI);
		RDFTypedLiteral typedLiteral=null; //start by assuming that no factory is registered for this datatype namespace, or the registered factory can't create a typed literal
		final RDFTypedLiteralFactory typedLiteralFactory=getTypedLiteralFactory(datatypeNamespaceURI); //get a typed literal factory for this namespace
		if(typedLiteralFactory!=null) //if we have a factory
		{
			typedLiteral=typedLiteralFactory.createTypedLiteral(lexicalForm, datatypeURI); //try to create a typed literal from this factory
		}
		if(typedLiteral==null)  //if we haven't created a typed literal, see if this is an RDF datatype
		{
			if(XML_LITERAL_DATATYPE_URI.equals(datatypeURI))	//if we are creating an XML typed literal
			{
				typedLiteral=new RDFXMLLiteral(lexicalForm);	//create a new XML typed literal containing the lexical form
			}
		}
		if(typedLiteral==null)  //if we still haven't created a typed literal
		{
			typedLiteral=new RDFTypedLiteral(lexicalForm, datatypeURI);  //create a new typed literal from the lexical form, specifying the correct datatype
		}
		return typedLiteral;  //return the resource we created
	}

	/**Default constructor.*/
	public RDF()
	{
		this(null);	//construct the data model with no known base URI
	}

	/**Base URI constructor.
	@param baseURI The base URI of the RDF data model, or <code>null</code> if unknown.
	*/
	public RDF(final URI baseURI)
	{
		this.baseURI=baseURI;	//save the base URI
				//register typed literal factories for certain default namespaces
		registerTypedLiteralFactory(XMLSchemaConstants.XML_SCHEMA_NAMESPACE_URI, new XMLSchemaTypedLiteralFactory());	//XML Schema
	}
}