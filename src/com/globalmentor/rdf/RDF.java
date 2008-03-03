/*
 * Copyright © 1996-2008 GlobalMentor, Inc. <http://www.globalmentor.com/>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.globalmentor.rdf;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import static java.util.Collections.*;

import com.globalmentor.net.URIs;
import static com.globalmentor.rdf.RDFUtilities.*;
import com.globalmentor.rdf.rdfs.RDFSUtilities;
import com.globalmentor.rdf.xmlschema.XMLSchemaTypedLiteralFactory;
import com.globalmentor.text.xml.schema.XMLSchema;
import com.globalmentor.util.*;

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
public class RDF	//TODO special-case rdf:nil list resources so that they are not located, but a different instance is created for each one, to make RDFListResource convenience methods work correctly 
{

	/**The recommended prefix to the RDF namespace.*/
	public final static String RDF_NAMESPACE_PREFIX="rdf";

	/**The URI to the RDF namespace.*/
	public final static URI RDF_NAMESPACE_URI=URI.create("http://www.w3.org/1999/02/22-rdf-syntax-ns#");

	
	  //RDF property names
	/**The pseudo-property name of a member container used only for serialization. The local name of rdf:li.*/
	public final static String LI_PROPERTY_NAME="li";
	/**The first element of a list. The local name of rdf:first.*/
	public final static String FIRST_PROPERTY_NAME="first";
	/**The rest of the elements of a list. The local name of rdf:rest.*/
	public final static String REST_PROPERTY_NAME="rest";
	/**The type of an RDF resource. The local name of rdf:type.*/
	public final static String TYPE_PROPERTY_NAME="type";
	/**The value of an RDF resource. The local name of rdf:value. (Defined in RDFS.)*/
	public final static String VALUE_PROPERTY_NAME="value";

	  //RDF class names
  /**The local name of rdf:Alt.*/
  public final static String ALT_CLASS_NAME="Alt";
  /**The local name of rdf:Bag.*/
  public final static String BAG_CLASS_NAME="Bag";
  /**The local name of rdf:List.*/
  public final static String LIST_CLASS_NAME="List";
  /**The local name of rdf:Seq.*/
  public final static String SEQ_CLASS_NAME="Seq";

		//RDF datatypes
	/**The local name of the rdf:XMLLiteral.*/
	public final static String XML_LITERAL_DATATYPE_NAME="XMLLiteral";
	/**The URI of the <code>rdf:XMLLiteral</code> datatype.*/
	public final static URI XML_LITERAL_DATATYPE_URI=RDFUtilities.createReferenceURI(RDF_NAMESPACE_URI, XML_LITERAL_DATATYPE_NAME);

	/**The prefix to be used when generating property names for each member of
		a container, originally represented by <code>&lt;li&gt;</code> in the
		serialization.
	*/
	public final static String CONTAINER_MEMBER_PREFIX="_";

		//RDF predefined reference URIs
	/**The name of the <code>rdf:nil</code> resource.*/
	public final static String NIL_RESOURCE_NAME="nil";
	/**The URI of the <code>rdf:nil</code> resource.*/
	public final static URI NIL_RESOURCE_URI=RDFUtilities.createReferenceURI(RDF_NAMESPACE_URI, NIL_RESOURCE_NAME);

		//RDF XML predefined class reference URIs
	/**The reference URI of the rdf:alt property.*/ 
	public final static URI ALT_PROPERTY_REFERENCE_URI=RDFUtilities.createReferenceURI(RDF_NAMESPACE_URI, ALT_CLASS_NAME);
	/**The reference URI of the rdf:bag property.*/ 
	public final static URI BAG_PROPERTY_REFERENCE_URI=RDFUtilities.createReferenceURI(RDF_NAMESPACE_URI, BAG_CLASS_NAME);
	/**The reference URI of the rdf:list property.*/ 
	public final static URI LIST_PROPERTY_REFERENCE_URI=RDFUtilities.createReferenceURI(RDF_NAMESPACE_URI, LIST_CLASS_NAME);
	/**The reference URI of the rdf:seq property.*/ 
	public final static URI SEQ_PROPERTY_REFERENCE_URI=RDFUtilities.createReferenceURI(RDF_NAMESPACE_URI, SEQ_CLASS_NAME);

		//RDF XML predefined property reference URIs
	/**The reference URI of the rdf:li property.*/ 
	public final static URI LI_PROPERTY_REFERENCE_URI=RDFUtilities.createReferenceURI(RDF_NAMESPACE_URI, LI_PROPERTY_NAME);
	/**The reference URI of the rdf:type property.*/ 
	public final static URI TYPE_PROPERTY_REFERENCE_URI=RDFUtilities.createReferenceURI(RDF_NAMESPACE_URI, TYPE_PROPERTY_NAME);

	/**A map of resource factories, keyed to namespace URI.*/
	private final Map<URI, RDFResourceFactory> resourceFactoryMap=new HashMap<URI, RDFResourceFactory>();

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
			return resourceFactoryMap.get(typeNamespaceURI);  //return any factory registered for this namespace
		}

	/**A map of typed literal factories, keyed to datatype namespace URI.*/
	private final Map<URI, RDFTypedLiteralFactory> typedLiteralFactoryMap=new HashMap<URI, RDFTypedLiteralFactory>();

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
			datatype from the specified namespace. If there is no typed literal
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
			RDFTypedLiteralFactory typedLiteralFactory=typedLiteralFactoryMap.get(datatypeNamespaceURI);  //get any factory registered for this namespace
			if(typedLiteralFactory==null && datatypeNamespaceURI!=null)	//if we didn't find a factory, try the namespaceURI without its ending # (RDF has different URI generation rules than, for example, XML Schema, resulting in different namespace representations)
			{
				final String datatypeNamespaceURIString=datatypeNamespaceURI.toString();	//get the string form of the datatype namespace URI 
					//if this URI ends with '#' and has data before that character
				if(datatypeNamespaceURIString.length()>1 && datatypeNamespaceURIString.charAt(datatypeNamespaceURIString.length()-1)==URIs.FRAGMENT_SEPARATOR)
				{
					try	//see if we recognize the URI without the ending '#'
					{
						typedLiteralFactory=typedLiteralFactoryMap.get(new URI(datatypeNamespaceURIString.substring(0, datatypeNamespaceURIString.length()-1)));
					}
					catch(final URISyntaxException URISyntaxException)	//if we can't create a valid URI by remove the fragment separator character, don't do anything---there wouldn't be a factory mapped to that value, anyway 
					{
					}
				}			
			}
			return typedLiteralFactory;	//return whatever typed literal we found, if any
		}

	/**The set of all resources, named and unnamed, using identity rather than equality for equivalence.*/
	private final IdentityHashSet<RDFResource> resourceSet=new IdentityHashSet<RDFResource>();

	/**The map of all named resources, keyed to resource reference URI.*/
	private final Map<URI, RDFResource> resourceMap=new HashMap<URI, RDFResource>();

	/**Adds a resource to the data model.
	If the resource is already in the model, no action occurs. 
	@param resource The resource to add.
	*/
	public void addResource(final RDFResource resource)
	{
		resourceSet.add(resource);	//add the resource to our set
		if(resource.getURI()!=null)	//if this is not a blank node
			resourceMap.put(resource.getURI(), resource);  //store the resource in the map
	}

	/**Retrieves a named resource from the data model using its reference URI.
	@param resourceURI The reference URI of the resource to retrieve.
	@return The resource, or <code>null</code> if no matching resource was found.
	*/
	public RDFResource getResource(final URI resourceURI)
	{
		return resourceMap.get(resourceURI); //retrieve the resource
	}

	/**@return The number of resources in this data model.*/
	public int getResourceCount()
	{
		return resourceSet.size();  //return the size of the resource set
	}

	/**@return A read-only iterable of resources.*/
	public Iterable<RDFResource> getResources()
	{
		return unmodifiableCollection(resourceSet); //return an unmodifiable iterable to the set of all resources
	}

	/**@return A read-only iterable of resources appropriate for appearing at the
		root of a hierarchy, such as an RDF tree or an RDF+XML serialization.
	*/
	public Iterable<RDFResource> getRootResources()
	{
		return getRootResources(null);	//return an unsorted iterable to the root resources 
	}

	/**Returns a read-only iterable of resources appropriate for appearing at the
		root of a hierarchy, such as an RDF tree or an RDF+XML serialization. The
		resources are sorted using the optional comparator.
	@param comparator The object that determines how the resources will be sorted,
		or <code>null</code> if the resources should not be sorted.
	@return A read-only iterable of root resources sorted by the optional
		comparator.
	*/
	public Iterable<RDFResource> getRootResources(final Comparator comparator)
	{
			//create a set in which to place the root resources, making the set sorted if we have a comparator
///TODO fix		final Set<RDFResource> rootResourceSet=comparator!=null ? (Set<RDFResource>)new TreeSet<RDFResource>(comparator) : (Set<RDFResource>)new HashSet<RDFResource>();	 		
		final Set<RDFResource> rootResourceSet=new HashSet<RDFResource>();	//TODO fix comparing once we decide what type of comparator to use---should it include just resources, or all RDF objects?
		for(final RDFResource resource:getResources())	//look at all resouces
		{
			if(isRootResource(resource))	//if this is a root resource
			{
				rootResourceSet.add(resource);	//add the resource to the set of root resources
			}
		}
		return unmodifiableCollection(rootResourceSet); //return an unmodifiable set of root resources
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
		final URI referenceURI=resource.getURI(); //get the resource reference URI
		final RDFLiteral label=RDFSUtilities.getLabel(resource);	//see if this resource has a label
//TODO eventually we'll probably have to determine if something is actually a property---i.e. this doesn't work: if(resource.getReferenceURI()!=null || resource.getPropertyCount()>0)	//only show resources that have reference URIs or have properties, thereby not showing property resources and literals at the root
			//if this is not a blank node and this resource actually has properties (even properties such as type identifiers are resources, but they don't have properties)
		return (referenceURI!=null && resource.getPropertyCount()>0)
					|| label!=null;	//if a resource is labeled, it's probably important enough to show at the top of the hierarchy as well 
		
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
		return locateResource(createReferenceURI(namespaceURI, localName));	//locate a resource, constructing a reference URI from the given namespace URI and local name
	}
	
	/**Retreives a resource from the data model based upon a URI. If no such
		resource exists, one will be created and added to the data model.
	@param referenceURI The reference URI of the resource to retrieve.
	@return A resource with the given reference URI.
	*/
	public RDFResource locateResource(final URI referenceURI)
	{
		return locateTypedResource(referenceURI, null);	//locate a resource without knowing its type
	}

	/**Retrieves a resource from the data model based upon the reference URI of
		the resource. If no such resource exists, one will be created and added to
		the data model. The given URI will be used to locate a resource factory to
		create the resource, and that type URI will be added as a type property.
		If the resource already exists, no checks are performed to ensure that the
		existing resource is of the requested type.
	@param referenceURI The reference URI of the resource to retrieve.
	@param typeURI The URI of the type, or <code>null</code> if the type is not known.
	@return A resource with the given reference URI.
	*/
	public RDFResource locateTypedResource(final URI referenceURI, final URI typeURI)
	{
		return locateTypedResource(referenceURI, typeURI!=null ? getNamespaceURI(typeURI) : null, typeURI!=null ? getLocalName(typeURI) : null);	//locate a resource after splitting out the type URI components
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
	public RDFResource locateTypedResource(final URI referenceURI, final URI typeNamespaceURI, final String typeLocalName)
	{
		RDFResource resource=getResource(referenceURI);  //retrieve a resource from the data model
		if(resource==null)  //if no such resource exists
		{
			resource=createTypedResource(referenceURI, typeNamespaceURI, typeLocalName);  //create a new resource from the given reference URI and store the resource in the data model
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
		return createResource(createReferenceURI(namespaceURI, localName));	//create and return a resource with a reference URI constructed from the namespace URI and local name
	}
	
	/**Creates a general resource with the specified reference URI and stores it in
		this RDF data model.
	@param referenceURI The reference URI of the resource to create, or
		<code>null</code> if the resource created should be represented by a blank node.
	@return A resource with the given reference URI.
	*/
	public RDFResource createResource(final URI referenceURI)
	{
		return createTypedResource(referenceURI, null);	//create and return a resource without a type
	}

	/**Creates a resource with the provided reference URI and type URI.
		The given type type URI will be used to attempt to locate a resource factory to create the resource.
		A type property with the given type URI will be added to the resource.
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
	@param typeURI The URI of the type, or <code>null</code> if the type is not known.
	@return The resource created with this reference URI, with the given type added if a type was given.
	*/
	public RDFResource createTypedResource(final URI referenceURI, final URI typeURI)
	{
		return createTypedResource(referenceURI, typeURI!=null ? getNamespaceURI(typeURI) : null, typeURI!=null ? getLocalName(typeURI) : null);	//create a typed resource after breaking out the namespace URI and the local name of the type
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
	protected RDFResource createTypedResource(final URI referenceURI, final URI typeNamespaceURI, final String typeLocalName)
	{
		RDFResource resource=createTypedResourceFromFactory(referenceURI, typeNamespaceURI, typeLocalName);	//see if we can create the resource from a resource factory
		if(resource==null)  //if we didn't created a resource from a factory
		{
		  resource=new DefaultRDFResource(this, referenceURI);  //create a new resource from the given reference URI, showing which data model created the resource
			addResource(resource);  //store the resource in the data model
		}
		if(typeNamespaceURI!=null && typeLocalName!=null)	//if we were given a type
		{
			RDFUtilities.addType(resource, typeNamespaceURI, typeLocalName); //add the type property
		}
		return resource;  //return the resource we created
	}

	/**Attempts to create a resource with the provided reference URI
	The given type URI will be used to attempt to locate a resource factory to create the resource.
	<p>The created resource, if any, will be added to this RDF data model, but
		no type will be added to the resource.</p>
	<p>This method knows how to create the following RDF-defined resources:</p>
	<ul>
		<li>Reference URI <code>rdf:nil</code> (<code>RDFListResource</code>)</li>
		<li>Type <code>rdf:Alt</code>  (<code>RDFAltResource</code>)</li>
		<li>Type <code>rdf:Bag</code>  (<code>RDFBagResource</code>)</li>
		<li>Type <code>rdf:Seq</code>  (<code>RDFSequenceResource</code>)</li>
	</ul>
	@param referenceURI The reference URI of the resource to create, or
		<code>null</code> if the resource created should be represented by a blank node.
	@param typeURI The URI of the type, or <code>null</code> if the type is not known.
	@return The resource created with this reference URI, or <code>null</code>
		if the resource could not be created from a resource factory or a suitable
		resource factory could not be found.
	*/
	public RDFResource createTypedResourceFromFactory(final URI referenceURI, final URI typeURI)
	{
		return createTypedResourceFromFactory(referenceURI, typeURI!=null ? getNamespaceURI(typeURI) : null, typeURI!=null ? getLocalName(typeURI) : null);	//create a typed resource after breaking out the namespace URI and the local name of the type
	}

	/**Attempts to create a resource with the provided reference URI
		The given type namespace URI and type local name will be used to
		attempt to locate a resource factory to create the resource.
	<p>The created resource, if any, will be added to this RDF data model, but
		no type will be added to the resource.</p>
	<p>This method knows how to create the following RDF-defined resources:</p>
	<ul>
		<li>Reference URI <code>rdf:nil</code> (<code>RDFListResource</code>)</li>
		<li>Type <code>rdf:Alt</code>  (<code>RDFAltResource</code>)</li>
		<li>Type <code>rdf:Bag</code>  (<code>RDFBagResource</code>)</li>
		<li>Type <code>rdf:Seq</code>  (<code>RDFSequenceResource</code>)</li>
	</ul>
	@param referenceURI The reference URI of the resource to create, or
		<code>null</code> if the resource created should be represented by a blank node.
	@param typeNamespaceURI The XML namespace used in the serialization of the
		type URI, or <code>null</code> if the type is not known.
	@param typeLocalName The XML local name used in the serialization of the type
		URI, or <code>null</code> if the type is not known.
	@return The resource created with this reference URI, or <code>null</code>
		if the resource could not be created from a resource factory or a suitable
		resource factory could not be found.
	*/
	public RDFResource createTypedResourceFromFactory(final URI referenceURI, final URI typeNamespaceURI, final String typeLocalName)
	{
		RDFResource resource=null; //start by assuming that no factory is registered for this type namespace, or the registered factory can't create a resource
/*TODO fix if needed
		final Class<? extends RDFResource> resourceClass=getTypeClass(createReferenceURI(typeNamespaceURI, typeLocalName));	//see if we have a class specifically for this type
		if(resourceClass!=null)	//if we have a class specifically for this resource
		{
			final Constructor<? extends RDFResource> constructor=resourceClass.getConstructor(URI.class);
		}
*/
		final RDFResourceFactory resourceFactory=getResourceFactory(typeNamespaceURI); //get a resource factory for this namespace
		if(resourceFactory!=null) //if we have a factory
		{
			resource=resourceFactory.createResource(referenceURI, typeNamespaceURI, typeLocalName); //try to create a resource from this factory
		}
		if(resource==null)  //if we haven't created a resource, see if this is an RDF resource
		{
			if(RDF.NIL_RESOURCE_URI.equals(referenceURI))	//if we are creating the nil resource
			{
				resource=new RDFListResource(this, RDF.NIL_RESOURCE_URI);	//create the nil resource with the special RDF nil URI
			}
			else if(RDF.RDF_NAMESPACE_URI.equals(typeNamespaceURI)) //if this resource is an RDF resource
			{
				if(RDF.ALT_CLASS_NAME.equals(typeLocalName)) //<rdf:Alt>
				{
					//TODO fix for alt
				}
				else if(RDF.BAG_CLASS_NAME.equals(typeLocalName))  //<rdf:Bag>
				{
					resource=new RDFBagResource(this, referenceURI);  //create a bag resource
				}
				else if(RDF.SEQ_CLASS_NAME.equals(typeLocalName))  //<rdf:Seq>
				{
					resource=new RDFSequenceResource(this, referenceURI);  //create a sequence resource
				}
				else if(RDF.LIST_CLASS_NAME.equals(typeLocalName))  //<rdf:Seq>
				{
					resource=new RDFListResource(this, referenceURI);  //create a list resource
				}
			}
		}
		if(resource!=null)  //if we found a resource
		{
			if(resource.getRDF()!=this)	//if a resource was created that isn't associated with this data model
			{
				resource.setRDF(this);	//associate the resource with this data model TODO create an import() method that will recursively set the data models of the resource and all properties and property values
			}
			addResource(resource);  //store the resource in the data model
		}
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
	public RDFTypedLiteral<?> createTypedLiteral(final String lexicalForm, final URI datatypeURI)
	{
		if(datatypeURI==null)	//if no datatype is given
		{
			throw new IllegalArgumentException("A datatype must be given to create a typed literal.");	//TODO fix
		}
			//try to get the namespace of the datatype
		final URI datatypeNamespaceURI=RDFUtilities.getNamespaceURI(datatypeURI);
		RDFTypedLiteral<?> typedLiteral=null; //start by assuming that no factory is registered for this datatype namespace, or the registered factory can't create a typed literal
		final RDFTypedLiteralFactory typedLiteralFactory=getTypedLiteralFactory(datatypeNamespaceURI); //get a typed literal factory for this namespace
		if(typedLiteralFactory!=null) //if we have a factory
		{
			typedLiteral=typedLiteralFactory.createTypedLiteral(lexicalForm, datatypeURI); //try to create a typed literal from this factory
		}
		if(typedLiteral==null)  //if we haven't created a typed literal, see if this is an RDF datatype
		{
			if(RDF.XML_LITERAL_DATATYPE_URI.equals(datatypeURI))	//if we are creating an XML typed literal
			{
				typedLiteral=new RDFXMLLiteral(lexicalForm);	//create a new XML typed literal containing the lexical form
			}
		}
		if(typedLiteral==null)  //if we still haven't created a typed literal
		{
			typedLiteral=new RDFTypedLiteral<String>(lexicalForm, datatypeURI);  //create a new typed literal from the lexical form, specifying the correct datatype
		}
		return typedLiteral;  //return the resource we created
	}

	/**Default constructor.*/
	public RDF()
	{
		//register typed literal factories for certain default namespaces
		registerTypedLiteralFactory(XMLSchema.XML_SCHEMA_NAMESPACE_URI, new XMLSchemaTypedLiteralFactory());	//XML Schema
	}

	/**Looks at all the resources in the RDF data model and recursively gathers
		which resources reference which other resources.
	<p>Circular references are correctly handled.</p>
	@return A map that associates, for each resource, a set of all resources that
		reference the that resource. Both the map and the associated set use
		identity rather than equality to store resources, as some resources may
		be anonymous.
	*/
	public Map<RDFResource, Set<RDFResource>> getReferences()
	{
		final Map<RDFResource, Set<RDFResource>> referenceMap=new IdentityHashMap<RDFResource, Set<RDFResource>>();	//create a new map in which to store reference sets
		return getReferences(referenceMap);	//gather all reference sets, place them in the reference map, and return the map
		
	}

	/**Looks at all the resources in the RDF data model and recursively gathers
		which resources reference which other resources.
	<p>Circular references are correctly handled.</p>
	@param referenceMap A map that associates, for each resource, a set of all
		resources that reference the that resource.
	@return The map of resources and associated referring resources. The
		associated set will use identity rather than equality to store resources,
		as some resources may be anonymous.
	*/
	public Map<RDFResource, Set<RDFResource>> getReferences(final Map<RDFResource, Set<RDFResource>> referenceMap)
	{
		final Set<RDFResource> referringResourceSet=new IdentityHashSet<RDFResource>();	//create a set of referring resources to prevent endless following of circular references
		for(final RDFResource resource:getResources())	//for each resource in this data model
		{
			getReferences(resource, referenceMap, referringResourceSet);	//gather all references to this resource
		}
		return referenceMap;	//return the map we populated
	}

	/**Looks at the resources and all its properties and recursively gathers
		which resources reference which other resources.
	<p>Circular references are correctly handled.</p>
	@param resource The resource for which references should be gathered for the
		resource and all resources that are property values of this resource's
		properties, and so on.
	@return A map that associates, for each resource, a set of all resources that
		reference the that resource. Both the map and the associated set use
		identity rather than equality to store resources, as some resources may
		be anonymous.
	*/
	public static Map<RDFResource, Set<RDFResource>> getReferences(final RDFResource resource)
	{
		return getReferences(resource, new IdentityHashMap<RDFResource, Set<RDFResource>>());	//create a new identity hash map and use it to retrieve references to the given resources
	}

	/**Looks at the resources and all its properties and recursively gathers
		which resources reference which other resources.
	<p>Circular references are correctly handled.</p>
	@param resource The resource for which references should be gathered for the
		resource and all resources that are property values of this resource's
		properties, and so on.
	@param referenceMap A map that associates, for each resource, a set of all
		resources that reference the that resource.
	@return The map of resources and associated referring resources. The
		associated set will use identity rather than equality to store resources,
		as some resources may be anonymous.
	*/
	public static Map<RDFResource, Set<RDFResource>> getReferences(final RDFResource resource, final Map<RDFResource, Set<RDFResource>> referenceMap)
	{
		return getReferences(resource, referenceMap, new IdentityHashSet<RDFResource>());	//gather references, showing that we haven't looked at any referring resources, yet
	}

	/**Looks at the resources and all its properties and recursively gathers
		which resources reference which other resources.
	<p>Circular references are correctly handled.</p>
	@param resource The resource for which references should be gathered for the
		resource and all resources that are property values of this resource's
		properties, and so on.
	@param referenceMap A map that associates, for each resource, a set of all
		resources that reference the that resource.
	@param referrerResourceSet The set of referrers the properties of which have
		been traversed, the checking of which prevents circular reference problems.
	@return The map of resources and associated referring resources. The
		associated set will use identity rather than equality to store resources,
		as some resources may be anonymous.
	*/
	protected static Map<RDFResource, Set<RDFResource>> getReferences(final RDFResource resource, final Map<RDFResource, Set<RDFResource>> referenceMap, final Set<RDFResource> referrerResourceSet)
	{
		if(!referrerResourceSet.contains(resource))	//if we haven't checked this resource before
		{
			referrerResourceSet.add(resource);	//show that we've now checked this resource (in case one of the resource's own properties or subproperties reference this resource)
			final Iterator propertyIterator=resource.getPropertyIterator();	//get an iterator to this resource's properties
			while(propertyIterator.hasNext())	//while there are more properties
			{
				final RDFPropertyValuePair property=(RDFPropertyValuePair)propertyIterator.next();	//get the next property
				final RDFObject valueObject=property.getPropertyValue();	//get the value of the property
				if(valueObject instanceof RDFResource)	//if the value is a resource
				{
					final RDFResource valueResource=(RDFResource)valueObject;	//cast the object value to a resource
					Set<RDFResource> referenceSet=referenceMap.get(valueResource);	//get the set of references to the object resource
					if(referenceSet==null)	//if this is the first reference we've gathered for the object resource
					{
						referenceSet=new IdentityHashSet<RDFResource>();	//create a new set to keep track of references to the object resource
						referenceMap.put(valueResource, referenceSet);	//store the set in the map, keyed to the object resource
					}
					referenceSet.add(resource);	//show that this resource is another referrer to the object resource of this property
					getReferences(valueResource, referenceMap, referrerResourceSet);	//gather resources to the object resource
				}
			}
		}
		return referenceMap;	//return the map that was provided, which now holds sets of references to resources
	}

}