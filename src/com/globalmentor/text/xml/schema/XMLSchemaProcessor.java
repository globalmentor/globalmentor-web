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

package com.globalmentor.text.xml.schema;

import java.util.*;

import com.globalmentor.collections.iterators.Iterators;
import com.globalmentor.log.Log;
import com.globalmentor.text.xml.XMLSerializer;
import static com.globalmentor.text.xml.schema.XMLSchema.*;
import com.globalmentor.util.*;

import org.w3c.dom.*;

/**Class to process an XML schema XML document and return an XML schema object.
Each schema processor instance keeps a map of other schemas keyed to their
	target namespaces. A schema can be looked up by namespace with a call to
	<code>getSchema()</code>. This function will, if necessary, automatically
	call the static function <code>getDefaultSchema()</code>, which retrieves one
	of the predefined schemas, and adds that schema to the map of schemas for
	cached retrieval.
@author Garret Wilson
@deprecated
*/
public class XMLSchemaProcessor
{

	/**The serializer used to serialize annotations, shared across schema
		processors.
//TODO do we want to have a lazy creation here?
	*/
	protected final static XMLSerializer xmlSerializer=new XMLSerializer();

	/**The map of schemas, keyed by target namespace.*/
	private Map schemaMap=new HashMap();

	/**Stores a schema keyed to a particular namespace for later retrieval and
		use by the schema processor.
	@param targetNamespace The namespace the schema is for. TODO perhaps get this directly from the schema
	@param schema The schema to store in the map.
	*/
	public void putSchema(final String targetNamespace, final XMLSchema schema)
	{
		schemaMap.put(targetNamespace, schema); //store the schema in the map
	}

	/**Retrieves a schema for the specified namespace. This schema may have been
		stored earlier in the map. If the schema is not already in the map and is
		a recognized namespace for a default schema, that default schema is added to
		the map and returned.
	@param targetNamspace The target namespace for which a relevant schema should
		be returned.
	@return A schema for the specified namespace, or <code>null</code> if no
		relevant schema could be found.
	*/
	public XMLSchema getSchema(final String targetNamespace)
	{
		XMLSchema schema=(XMLSchema)schemaMap.get(targetNamespace);  //get a schema for the namespace
		if(schema==null)  //if there is no schema stored in the map
		{
		  //TODO process a default schema and add it to the map
		}
		return schema;  //return the schema we found, or null if one could not be found
	}

	/**Default constructor.*/
	public XMLSchemaProcessor() {}

	/**Processes a schema found in a particular element hierarchy, and returns
		a schema object which represents the schema.
	@param element The XML element representing the top of the schema element tree.
	@return A schema object derived from the XML schema elements.
	*/
	public static XMLSchema processSchema(final Element element)
	{
		//TODO make sure this is a schema object in the schema namespace
		final XMLSchema schema=new XMLSchema(); //create a new schema
		schema.setTargetNamespace(element.hasAttributeNS(null, ATTRIBUTE_TARGET_NAMESPACE) ?
		    element.getAttributeNS(null, ATTRIBUTE_TARGET_NAMESPACE):
			  null); //get the target namespace attribute, or null if there is no target namespace attribute (one isn't required)
		final Iterator schemaComponentIterator=processSchemaComponents(schema, element).iterator();  //get a list of child schema components, and get an iterator to look through them
			//get the next element, or null if there is no next element
	  XMLSchemaComponent schemaComponent=(XMLSchemaComponent)Iterators.getNext(schemaComponentIterator);
			//check for an annotation TODO also check for an include, import, or a redefine
		while(schemaComponent!=null && schemaComponent.getComponentType()==schemaComponent.ANNOTATION_COMPONENT) //if there is a schema component, and it's an annotation TODO change other instances to reflect the new ComponentType property
		{
			//TODO process the schema component
			schemaComponent=(XMLSchemaComponent)Iterators.getNext(schemaComponentIterator);  //get the next schema component, or null if there is no other one
		}
		  //check for body components
		while(schemaComponent!=null)  //while there are schema components left
		{
//TOD del if needed			final short componentType=schemaComponent.getComponentType(); //see which type of component this is
			switch(schemaComponent.getComponentType())  //see which type of component this is
			{
				case XMLSchemaComponent.ATTRIBUTE_COMPONENT:
				case XMLSchemaComponent.ATTRIBUTE_GROUP_COMPONENT:
				case XMLSchemaComponent.COMPLEX_TYPE_COMPONENT:
				case XMLSchemaComponent.ELEMENT_COMPONENT:
				case XMLSchemaComponent.MODEL_GROUP_COMPONENT:
				case XMLSchemaComponent.NOTATION_COMPONENT:
				case XMLSchemaComponent.SIMPLE_TYPE_COMPONENT:
				  schema.addComponent(schemaComponent); //add this component to the schema
					break;
				case XMLSchemaComponent.ANNOTATION_COMPONENT:
					break;  //annotations can come at any time after normal components, but they will be the last components of the schema
				default:
					Log.error("Unrecognized component: "+schemaComponent);  //TODO fix
				  break;
			}
			if(schemaComponent!=null && schemaComponent.getComponentType()==schemaComponent.ANNOTATION_COMPONENT) //if this is an annotation
				break;  //we're finished with the body components
		  schemaComponent=(XMLSchemaComponent)Iterators.getNext(schemaComponentIterator);  //get the next schema component, or null if there is no other one
		}
		//TODO read the rest of the annotations
		return schema;  //return the schema we created
	}

	/**Processes the child schema components of an XML element, returning a list
		of objects which represent those schema components.
	@param ownerSchema The XML schema which will own the processed components.
	@param element The XML element the children of which represent schema component.
	@return A list of objects which correspond to schema components.
	*/
	protected static List processSchemaComponents(final XMLSchema ownerSchema, final Element element)
	{
		final List schemaComponentList=new ArrayList(); //create a list in which to store child schema components
		final NodeList childNodeList=element.getChildNodes(); //get a list of the direct children
		for(int childIndex=0; childIndex<childNodeList.getLength(); ++childIndex) //look at each schema child node TODO eventually replace this with an iterator
		{
			final Node node=childNodeList.item(childIndex); //get a reference to this child node
			if(node.getNodeType()==node.ELEMENT_NODE)  //if this is an element
			{
				final Element childElement=(Element)node; //cast the node to an element
				final String childNamespaceURI=childElement.getNamespaceURI();  //find out which namespace this element is in
					//if this element is in the XML schema namespace
				if(childNamespaceURI!=null && childNamespaceURI.equals(XML_SCHEMA_NAMESPACE_URI.toString()))
				{
						//process the element which contains the schema component
				  final XMLSchemaComponent schemaComponent=processSchemaComponent(ownerSchema, childElement);
					if(schemaComponent!=null) //if we received a valid schema component
					  schemaComponentList.add(schemaComponent); //add this schema component to the list
					else  //TODO fix
						Log.trace("Did not recognize element: ", childElement.getLocalName()); //TODO fix
				}
			}
 		}
		return schemaComponentList; //return the list of schema components we created
	}

	/**Processes an XML element in a schema that represents a component in a schema,
		returning an object which represents that schema component.
	@param ownerSchema The XML schema which will own the processed components.
	@param element The XML element representing the schema component.
	@return An object which corresponds to the schema component, or null if the
		element is not recognized.
	*/
	protected static XMLSchemaComponent processSchemaComponent(final XMLSchema ownerSchema, final Element element)
	{
		//TODO make sure this is a schema object in the schema namespace
		final String localName=element.getLocalName();  //get the element's local name
		if(localName.equals(ELEMENT_ANNOTATION)) //<annotation>
			return processAnnotation(ownerSchema, element);  //process the schema component
		else if(localName.equals(ELEMENT_ATTRIBUTE_GROUP)) //<attributeGroup>
			return processAttributeGroup(ownerSchema, element);  //process the schema component
		else if(localName.equals(ELEMENT_ELEMENT)) //<element>
			return processElement(ownerSchema, element);  //process the schema component
		return null;  //show that we don't recognize the element
	}

	/**Processes an XML element in a schema that represents a named schema
		component. This function sets general properties (such as name and
		target namespace) of a nmaed schema component, and is called from another,
		more specific <code>processXXX()</code> method.
		No checks are done to ensure this is indeed the correct type of element.
	@param ownerSchema The XML schema which will own the processed components.
	@param element The XML element representing the schema component.
	*/
	protected static void processSchemaNamedComponent(final XMLSchema ownerSchema, final Element element, final XMLSchemaNamedComponent namedComponent)
	{
		namedComponent.setName(element.hasAttributeNS(null, ATTRIBUTE_NAME) ?
		    element.getAttributeNS(null, ATTRIBUTE_NAME):
			  null); //get the name attribute, or null if there is no name attribute (one isn't required)
		namedComponent.setTargetNamespace(ownerSchema.getTargetNamespace());  //in XML schema documents, all components have the same target namespace as the schema itself
	}

	/**Processes an XML element in a schema that represents an attribute group. No
		checks are done to ensure this is indeed the correct type of element.
	@param ownerSchema The XML schema which will own the processed components.
	@param element The XML element representing the schema component.
	@return The attribute group object.
	*/
	protected static XMLSchemaAttributeGroup processAttributeGroup(final XMLSchema ownerSchema, final Element element)
	{
		final XMLSchemaAttributeGroup attributeGroup=new XMLSchemaAttributeGroup(); //create a new attribute group
		processSchemaNamedComponent(ownerSchema, element, attributeGroup); //do the default processing for the named component
		final Iterator schemaComponentIterator=processSchemaComponents(ownerSchema, element).iterator();  //get an iterator for the list of child schema components
		//check to see if the first component is an annotation, and if so assign it; get the next schema component
		XMLSchemaComponent schemaComponent=assignAnnotation(attributeGroup, schemaComponentIterator);
		while(schemaComponent!=null)  //while there are schema components left
		{
				//attribute groups can only hold attributes or other attribute groups
			if(schemaComponent instanceof XMLSchemaAttribute || schemaComponent instanceof XMLSchemaAttributeGroup)
			{
				attributeGroup.getChildComponentList().add(schemaComponent);  //add this schema component as a child element
			}
//TODO check to see if this is anyAttribute			else if
		  else  //if we don't recognize the component
				Log.error("Unrecognized component: "+schemaComponent);  //TODO fix, even for annotations out of order
		  schemaComponent=(XMLSchemaComponent)Iterators.getNext(schemaComponentIterator);  //get the next schema component, or null if there is no other one
		}
		return attributeGroup;  //return the schema component we created
	}







	/**Processes an XML element in a schema that represents a schema element. No
		checks are done to ensure this is indeed the correct type of element.
	@param ownerSchema The XML schema which will own the processed components.
	@param element The XML element representing the schema component.
	@return The schema element object.
	*/
	protected static XMLSchemaElementComponent processElement(final XMLSchema ownerSchema, final Element element)
	{
		final XMLSchemaElementComponent schemaElement=new XMLSchemaElementComponent(); //create a new schema element
		processSchemaNamedComponent(ownerSchema, element, schemaElement); //do the default processing for the named component
		final Iterator schemaComponentIterator=processSchemaComponents(ownerSchema, element).iterator();  //get an iterator for the list of child schema components
		//check to see if the first component is an annotation, and if so assign it; get the next schema component
		XMLSchemaComponent schemaComponent=assignAnnotation(schemaElement, schemaComponentIterator);
		if(schemaComponent!=null) //if there is a schema component, see if it is a simple or complex type
		{
		  if(schemaComponent instanceof XMLSchemaTypeComponent)  //if this is a component representing type
			{
				schemaElement.setType((XMLSchemaTypeComponent)schemaComponent); //set the type of the element
			  schemaComponent=(XMLSchemaComponent)Iterators.getNext(schemaComponentIterator);  //get the next schema component, or null if there is no other one
			}
		}

/*TODO fix
		while(schemaComponent!=null)  //while there are schema components left
		{

				//attribute groups can only hold attributes or other attribute groups
			if(schemaComponent instanceof XMLSchemaAttribute || schemaComponent instanceof XMLSchemaAttributeGroup)
			{
				attributeGroup.getChildComponentList().add(schemaComponent);  //add this schema component as a child element
			}
//TODO check to see if this is anyAttribute			else if
		  else  //if we don't recognize the component
				Log.error("Unrecognized component: "+schemaComponent);  //TODO fix, even for annotations out of order
		  schemaComponent=(XMLSchemaComponent)IteratorUtilities.getNext(schemaComponentIterator);  //get the next schema component, or null if there is no other one
		}
*/
		return schemaElement;  //return the schema component we created
	}



	/**Processes an XML element in a schema that represents an annotation. No
		checks are done to ensure this is indeed the correct type of element.
	@param ownerSchema The XML schema which will own the processed components.
	@param element The XML element representing the schema component.
	@return The annotation object.
	*/
	protected static XMLSchemaAnnotation processAnnotation(final XMLSchema ownerSchema, final Element element)
	{
		final XMLSchemaAnnotation annotation=new XMLSchemaAnnotation(); //create a new annotation
		final NodeList childNodeList=element.getChildNodes(); //get a list of the direct children
		for(int childIndex=0; childIndex<childNodeList.getLength(); ++childIndex) //look at each schema child node TODO eventually replace this with an iterator
		{
			final Node node=childNodeList.item(childIndex); //get a reference to this child node
			if(node.getNodeType()==node.ELEMENT_NODE)  //if this is an element
			{
				final Element childElement=(Element)node; //cast the node to an element
				final String childNamespaceURI=childElement.getNamespaceURI();  //find out which namespace this element is in
					//if this element is in the XML schema namespace
				if(childNamespaceURI!=null && childNamespaceURI.equals(XML_SCHEMA_NAMESPACE_URI.toString()))
				{
					final String childLocalName=childElement.getLocalName();  //get the element's local name
						//if this is <appinfo> or <documentation>
					if(childLocalName.equals(ELEMENT_APPINFO) || childLocalName.equals(ELEMENT_DOCUMENTATION))
				  {
						final String informationString=xmlSerializer.serializeContent(childElement); //serialize the content of the information element to a string
						if(childLocalName.equals(ELEMENT_APPINFO))  //if this is application information
						  annotation.addApplicationInformation(informationString);  //add the application information
						else if(childLocalName.equals(ELEMENT_DOCUMENTATION)) //if this is user information
						  annotation.addUserInformation(informationString); //add the user information
				  }
					else  //if this is not <appinfo> or <documentation>
						Log.error("Illegal annotation element: "+childLocalName); //TODO fix
				}
			}
		}
		return annotation;  //return the schema component we created
	}

	/**Checks the next schema component in the iterator, and if that component is
		an annotation, assigns the annotation to the named schema component.
	@param namedComponent The named schema component which can have an annotation.
	@param schemaComponentIterator The iterator to retrieve processed child components.
	@return The next schema component, either the one after the annotation (if the
		first component was an annotation), the first component (if the first
		component was not an annotation), or <code>null</code> if there are no more
		componnents.
	*/
	protected static XMLSchemaComponent assignAnnotation(final XMLSchemaNamedComponent namedComponent, final Iterator schemaComponentIterator)
	{
			//get the next element, or null if there is no next element
	  XMLSchemaComponent schemaComponent=(XMLSchemaComponent)Iterators.getNext(schemaComponentIterator);
			//check for an annotation
		if(schemaComponent!=null) //if there is a schema component
		{
		  if(schemaComponent instanceof XMLSchemaAnnotation)  //if this is an annotation
			{
				namedComponent.setAnnotation((XMLSchemaAnnotation)schemaComponent); //set this component's annotation
			  schemaComponent=(XMLSchemaComponent)Iterators.getNext(schemaComponentIterator);  //get the next schema component, or null if there is no other one
			}
		}
		return schemaComponent; //return the next schema component
	}


}