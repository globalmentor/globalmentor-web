package com.garretwilson.rdf;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import com.garretwilson.util.*;

/**An RDF data model.
	The data model should be used to create resources, as it keeps a list of
	registered resource factories based upon resource type namespaces.
	The RDF data model itself is a resource factory for the default RDF resources
	such as bags, but registering a resource factory for the RDF namespace will
	override this default behavior.
@author Garret Wilson
*/
public class RDF implements RDFConstants
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
		@param datatypeNamespaceURI The namespace of the datatype for which a
			typed literal factory should be returned.
		@return The factory registered for this datatype namespace, or
			<code>null</code> if there is no factory registered for this datatype
			namespace.
		*/
		protected RDFTypedLiteralFactory getTypedLiteralFactory(final URI datatypeNamespaceURI)
		{
			return (RDFTypedLiteralFactory)typedLiteralFactoryMap.get(datatypeNamespaceURI);  //return any factory registered for this namespace
		}

	/**The map of all resources.*/
	private final Map resourceMap=new HashMap();

	/**Adds a resource to the data model.
	@param resource The resource to add.
	*/
	public void putResource(final RDFResource resource)
	{
//G***del Debug.trace("putting resource with URI: ", resource.getReferenceURI());
//G***del Debug.traceStack(); //G***del

//TODO fix to correctly work with new null URI blank node resources; we'll probably want to store them somewhere

		if(resource.getReferenceURI()!=null)	//if this is not a blank node
			resourceMap.put(resource.getReferenceURI(), resource);  //store the resource
	}

	/**Retrieves a resource from the data model using its reference URI.
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
		return resourceMap.size();  //return the size of the resource map
	}

	/**@return A read-only iterator of resources.*/
	public Iterator getResourceIterator()
	{
		return Collections.unmodifiableCollection(resourceMap.values()).iterator(); //return an unmodifiable iterator to the collection of values
	}

	/**Retreives a resource from the data model based upon a URI. If no such
		resource exists, one will be created and added to the data model.
	@param referenceURI The reference URI of the resource to retrieve.
	@return A resource with the given reference URI.
	*/
	public RDFResource locateResource(final URI referenceURI)
	{
		RDFResource resource=getResource(referenceURI);  //retrieve a resource from the data model
		if(resource==null)  //if no such resource exists
		{
			resource=createResource(referenceURI);  //create a new resource from the given reference URI and store the resource in the data model
		}
		return resource;  //return the resource we either found or created
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
		final URI referenceURI=RDFUtilities.createReferenceURI(namespaceURI, localName);  //create a reference URI from the given information
		RDFResource resource=getResource(referenceURI);  //retrieve a resource from the data model
		if(resource==null)  //if no such resource exists
		{
			resource=createResource(namespaceURI, localName);  //create a new resource from the given XML serialization information and store the resource in the data model
		}
		return resource;  //return the resource we either found or created
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
	public RDFResource locateResource(final URI referenceURI, final URI typeNamespaceURI, final String typeLocalName)
	{
//G***del Debug.trace("Trying to get resource with reference URI: ", referenceURI); //G***del
		RDFResource resource=getResource(referenceURI);  //retrieve a resource from the data model
		if(resource==null)  //if no such resource exists
		{
//G***del Debug.trace("Resource not found; ready to create it."); //G***del
			resource=createResource(referenceURI, typeNamespaceURI, typeLocalName);  //create a new resource from the given reference URI and store the resource in the data model
//G***del; now duplicated by createResource()			putResource(resource);  //store the resource in the data model
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
//G***del		return createResource(referenceURI, null, null);	//create a resource without knowing its type
			//create an anonymous reference URI if needed
//G***del		final URI resourceReferenceURI=referenceURI!=null ? referenceURI : createAnonymousReferenceURI();
		final RDFResource resource;
			//TODO remove this check eventually when we stop storing namespaces and local names, and move this to a common routine
		if(NIL_RESOURCE_URI.equals(referenceURI))	//if we are creating the nil resource
		{
			resource=new RDFListResource(NIL_RESOURCE_URI);	//create the nil resource with the special RDF nil URI
		}
		else	//if this isn't a special resource we know about
		{
			resource=new DefaultRDFResource(referenceURI);  //create a new resource from the given reference URI
			putResource(resource);  //store the resource in the data model
		}
		return resource;  //return the resource we either created
	}

	/**Creates a general resource with the a reference URI based upon an XML
		namespace URI and an XML local name, and stores the resource in this RDF
		data model.
	@param referenceNamespaceURI The XML namespace URI used in the reference URI serialization.
	@param referenceLocalName The XML local name used in the reference URI serialization.
	@return A resource with a reference URI corresponding to the given namespace
		URI and local name.
	*/
	public RDFResource createResource(final URI referenceNamespaceURI, final String referenceLocalName)
	{
		final RDFResource resource;
			//TODO remove this check eventually when we stop storing namespaces and local names, and move this to a common routine
		if(NIL_RESOURCE_URI.equals(RDFUtilities.createReferenceURI(referenceNamespaceURI, referenceLocalName)))	//if we are creating the nil resource
		{
			resource=new RDFListResource(NIL_RESOURCE_URI);	//create the nil resource with the special RDF nil URI
		}
		else	//if this isn't a special resource we know about
		{
			resource=new DefaultRDFResource(referenceNamespaceURI, referenceLocalName);  //create a new resource from the given reference URI
		}
		putResource(resource);  //store the resource in the data model
		return resource;  //return the resource we either created
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
		type URI. //G***del , or <code>null</code> if the type is not known.
	@param typeLocalName The XML local name used in the serialization of the type
		URI.  //G***del, or <code>null</code> if the type is not known.
	@return The resource created with this reference URI, with the given type
		added if a type was given.
	*/
	public RDFResource createResource(final URI referenceURI, final URI typeNamespaceURI, final String typeLocalName)
	{
		Debug.assert(typeNamespaceURI!=null, "type namespace is null");	//G***fix
			//create an anonymous reference URI if needed
//G***del when works		final URI resourceReferenceURI=referenceURI!=null ? referenceURI : createAnonymousReferenceURI();
//G***del Debug.trace("Ready to create resource with reference URI: ", referenceURI); //G***del
//G***del Debug.trace("Type namespace URI: ", typeNamespaceURI); //G***del
//G***del Debug.trace("Type local name: ", typeLocalName); //G***del
		RDFResource resource=null; //start by assuming that no factory is registered for this type namespace, or the registered factory can't create a resource
		final RDFResourceFactory resourceFactory=getResourceFactory(typeNamespaceURI); //get a resource factory for this namespace
//G***del		if(resourceFactory!=null && resourceFactory!=this) //if we have a factory (and it isn't this class; otherwise, this would cause recursion if one were to register the RDF data model as a factory)
		if(resourceFactory!=null) //if we have a factory
		{
//G***del Debug.trace("Found resource factory.");  //G***del
		  resource=resourceFactory.createResource(referenceURI, typeNamespaceURI, typeLocalName); //try to create a resource from this factory
		}
		if(resource==null)  //if we haven't created a resource, see if this is an RDF resource
		{
//G***del Debug.trace("Did not find resource factory.");  //G***del
			if(NIL_RESOURCE_URI.equals(referenceURI))	//if we are creating the nil resource
			{
				resource=new RDFListResource(NIL_RESOURCE_URI);	//create the nil resource with the special RDF nil URI
			}
			else if(RDF_NAMESPACE_URI.equals(typeNamespaceURI)) //if this resource is an RDF resource
			{
//G***del Debug.trace("Is an RDF type.");  //G***del
				if(ALT_TYPE_NAME.equals(typeLocalName)) //<rdf:Alt>
				{
					//G***fix for alt
				}
				else if(BAG_TYPE_NAME.equals(typeLocalName))  //<rdf:Bag>
				{
//G***del Debug.trace("creating an RDF bag"); //G***del
					resource=new RDFBagResource(referenceURI);  //create a bag resource
				}
				else if(SEQ_TYPE_NAME.equals(typeLocalName))  //<rdf:Seq>
				{
//G***del Debug.trace("creating an RDF sequence"); //G***del
					resource=new RDFSequenceResource(referenceURI);  //create a sequence resource
				}
				else if(LIST_TYPE_NAME.equals(typeLocalName))  //<rdf:Seq>
				{
					resource=new RDFListResource(referenceURI);  //create a list resource
				}
//G***should we check to see if this is the nil resource, and if so automatically set its type to rdf:List?
			}
		}
		if(resource==null)  //if we still haven't created a resource
		{
		  resource=new DefaultRDFResource(referenceURI);  //create a new resource from the given reference URI
//G***del when works			resource=createResource(resourceReferenceURI);  //create a new default resource from the given reference URI G***maybe use an RDF factory method for this, too
		}
		RDFUtilities.addType(this, resource, typeNamespaceURI, typeLocalName); //add the type property TODO make sure the type is added to the list of triples
/*G***del
		final RDFResource typeProperty=RDFUtilities.getTypeProperty(this); //get a rdf:type resource
		final RDFResource typeValue=locateResource(typeNamespaceURI, typeLocalName);  //get a resource for the value of the property
		resource.addProperty(typeProperty, typeValue);  //add the property to the resource
*/
		putResource(resource);  //store the resource in the data model
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
	}
}