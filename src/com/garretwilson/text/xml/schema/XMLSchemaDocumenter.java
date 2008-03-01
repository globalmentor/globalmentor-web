package com.garretwilson.text.xml.schema;

import java.io.*;
import java.util.Collection;
import java.util.List;
import java.util.Iterator;
import com.garretwilson.text.xml.*;
import static com.garretwilson.text.xml.xhtml.XHTML.*;

import com.globalmentor.util.Debug;

import org.w3c.dom.*;

//G***do we really want this separate class? do we really want it in com.garretwilson.text.xml.schema?

/**Class which generates XHTML documentation for an XML schema.
@author Garret Wilson
*/
public class XMLSchemaDocumenter
{

	/**Documents an XML schema by creating an XHTML cross-referenced document.
	@param schema The schema for which to create documentation.
	@return An XHTML document representing the documentation of the XML schema.
//G***this should probably go inside a com.garretwilson.text.xml.schema.XMLSchemaDocumentor or something
	*/
	public static Document generateDocumentation(final XMLSchema schema)
	{
		final XMLDOMImplementation domImplementation=new XMLDOMImplementation();	//create a new DOM implementation
		final DocumentType documentType=domImplementation.createDocumentType(ELEMENT_HTML, XHTML_1_0_STRICT_PUBLIC_ID, XHTML_1_0_STRICT_SYSTEM_ID);	//create an XHTML document type G***perhaps put this in an OEB class
		final Document document=domImplementation.createDocument(XHTML_NAMESPACE_URI.toString(), ELEMENT_HTML, documentType);	//create an XHTML document G***perhaps put this in an OEB class
			//G***check about whether we need to add a <head> and <title>
		final Element htmlElement=document.getDocumentElement();	//get the html element
		XMLUtilities.appendText(/*G***del when works document, */htmlElement, "\n");	//append a newline to start the content of the html element
		document.appendChild(htmlElement);	//add the html element to the document
		final Element bodyElement=document.createElementNS(XHTML_NAMESPACE_URI.toString(), ELEMENT_BODY);	//create the body element
		XMLUtilities.appendText(bodyElement, "\n");	//append a newline to separate the information in the body
		generateAttributeGroupDocumentation(schema, bodyElement); //generate the attribute group documentation
/*G***fix
		Element paragraphElement=parseParagraph(document, bufferedReader);	//parse the first paragraph
		while(paragraphElement!=null)	//keep reading until we run out of paragraphs
		{
			XMLUtilities.appendText(document, bodyElement, "\n");	//append a newline to separate the paragraphs
			bodyElement.appendChild(paragraphElement);	//add the paragraph element to our document
			paragraphElement=parseParagraph(document, bufferedReader);	//parse the next paragraph
		}
*/
		XMLUtilities.appendText(/*G***del when works document, */bodyElement, "\n");	//append a newline to end the content of the body element
		XMLUtilities.appendText(/*G***del when works document, */htmlElement, "\n");	//append a newline to end the content of the html element
		return document;	//return the document we created
	}

	/**Creates a paragraph of documentation based upon the annotation of the given
		named schema component, if annotation information is available. The
		annotation information will be documented by creating a paragraph for each
		user information element present in the annotation.
	@param bodyElement The HTML body element of the documentation XML document.
	@param namedComponent The component for which annotation information should
		be documented, if annotation information is available.
	@see XMLSchemaNamedComponent#getAnnotation
	*/
	protected static void generateAnnotationDocumentation(final Element bodyElement, final XMLSchemaNamedComponent namedComponent)
	{
Debug.trace("ready to generate annotation doc");
		final XMLSchemaAnnotation annotation=namedComponent.getAnnotation();  //get the annotation for this component
		if(annotation!=null)  //if there is annotation information
		{
Debug.trace("found annotation");
		  final Iterator userInfoIterator=annotation.getUserInformation().iterator(); //get an iterator for the user information from the annotation
		  while(userInfoIterator.hasNext()) //while there is more user information
			{
					//get the next user information
				final XMLSchemaAnnotation.UserInformation userInfo=(XMLSchemaAnnotation.UserInformation)userInfoIterator.next();
Debug.trace("found user info: ", userInfo.toString());
				final Element userInfoParagraphElement=XMLUtilities.appendElementNS(bodyElement, XHTML_NAMESPACE_URI.toString(),
						ELEMENT_P, null); //create the annotation documentation paragraph, but do not add any content
				  //G***do something besides creating a new XML parser from scratch
					//G***see if we can do something besides casting the document to an XMLDocument
				try
				{
					//parse the element content from the user info string, preserving the entire XML sub-tree
					new XMLProcessor().parseElementContent((XMLElement)userInfoParagraphElement, userInfo.toString());
				}
				catch(Exception e)  //G***fix these exceptions; let them bubble up
				{
					Debug.error(e);
				}
				XMLUtilities.appendText(bodyElement, "\n");	//append a newline to separate the information in the body
			}
		}
	}

	/**Generates documentation for any globally defined attribute groups, appending
		the content to an existing XML document.
	@param schema The schema for which to create documentation.
	@param bodyElement The HTML body element of the documentation XML document.
	*/
	protected static void generateAttributeGroupDocumentation(final XMLSchema schema, final Element bodyElement)
	{
Debug.trace("Getting list of attribute groups.");



		  //get a list of all attribute groups
		final Collection attributeGroupCollection=schema.getComponents(schema.getTargetNamespace(), XMLSchemaSymbolTable.ATTRIBUTE_GROUP_SYMBOL_SPACE_NAME);

//G***del when works		final List attributeGroupList=schema.getComponentList(XMLSchemaComponent.ATTRIBUTE_GROUP_COMPONENT);


Debug.trace("attribute groups: ", attributeGroupCollection.size());
		if(attributeGroupCollection.size()>0) //if there are global attribute groups
		{
			//<h2>Global Attribute Groups</h2>
		  final Element headingElement=XMLUtilities.appendElementNS(bodyElement, XHTML_NAMESPACE_URI.toString(),
				  ELEMENT_H2, "Global Attribute Groups"); //create the heading G***i18n
			XMLUtilities.appendText(bodyElement, "\n");	//append a newline to separate the information in the body
			final Iterator attributeGroupIterator=attributeGroupCollection.iterator();  //get an iterator to look through the attribute groups
			while(attributeGroupIterator.hasNext()) //while there are more attribute groups
			{
				final XMLSchemaAttributeGroup attributeGroup=(XMLSchemaAttributeGroup)attributeGroupIterator.next(); //get the next attribute group
				generateDocumentation(schema, bodyElement, attributeGroup); //generate the documentation for this attribute group
			}
		}
	}

	/**Documents an attribute group in a schema.
	@param schema The schema for which to create documentation. G***del if not needed
	@param bodyElement The HTML body element of the documentation XML document.
	@param attributeGroup The attribute group to document.
	*/
	public static void generateDocumentation(final XMLSchema schema, final Element bodyElement, final XMLSchemaAttributeGroup attributeGroup)
	{
		//<h3>Attribute Group Name</h3>
		final Element attributeGroupHeadingElement=XMLUtilities.appendElementNS(bodyElement, XHTML_NAMESPACE_URI.toString(),
				ELEMENT_H3, attributeGroup.getName()); //create the heading for the attribute group
			//set the "id" attribute to identify this attribute
		attributeGroupHeadingElement.setAttributeNS(null, ATTRIBUTE_ID, "attributeGroup_"+attributeGroup.getName());
		XMLUtilities.appendText(bodyElement, "\n");	//append a newline to separate the information in the body
		generateAnnotationDocumentation(bodyElement, attributeGroup); //generate documentation for any annotation this component may have
/*G***fix
		//get an iterator to look at the contents of this attribute group
		final Iterator attributeGroupChildIterator=attributeGroup.getChildComponentList().iterator();


		//<h4>Contents</h4>
		final Element attributeGroupHeadingElement=XMLUtilities.appendElement(bodyElement, XHTML_NAMESPACE_URI,
				ELEMENT_H4, "Contents"); //create the heading for the attribute group contents G***i18n
		final Element contentListElement=XMLUtilities.appendElement(bodyElement, XHTML_NAMESPACE_URI,
				ELEMENT_UL, "Contents"); //create the heading for the attribute group contents G***i18n
*/
	}

}
