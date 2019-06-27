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

package com.globalmentor.xml.dom.impl.schema;

import java.util.*;

import com.globalmentor.xml.*;

import io.clogr.Clogr;

import static com.globalmentor.html.spec.HTML.*;

import org.w3c.dom.*;

//TODO we really want this separate class? do we really want it in com.globalmentor.text.xml.schema?

/**
 * Class which generates XHTML documentation for an XML schema.
 * @author Garret Wilson
 * @deprecated
 */
public class XMLSchemaDocumenter {

	/**
	 * Documents an XML schema by creating an XHTML cross-referenced document.
	 * @param schema The schema for which to create documentation.
	 * @return An XHTML document representing the documentation of the XML schema. //TODO this should probably go inside a
	 *         com.globalmentor.text.xml.schema.XMLSchemaDocumentor or something
	 */
	public static Document generateDocumentation(final XMLSchema schema) {
		final DOMImplementation domImplementation = XmlDom.createDocumentBuilder(true).getDOMImplementation();
		final DocumentType documentType = domImplementation.createDocumentType(ELEMENT_HTML, XHTML_1_0_STRICT_PUBLIC_ID, XHTML_1_0_STRICT_SYSTEM_ID); //create an XHTML document type TODO perhaps put this in an OEB class
		final Document document = domImplementation.createDocument(XHTML_NAMESPACE_URI_STRING, ELEMENT_HTML, documentType); //create an XHTML document TODO perhaps put this in an OEB class
		//TODO check about whether we need to add a <head> and <title>
		final Element htmlElement = document.getDocumentElement(); //get the html element
		XmlDom.appendText(htmlElement, "\n"); //append a newline to start the content of the html element
		document.appendChild(htmlElement); //add the html element to the document
		final Element bodyElement = document.createElementNS(XHTML_NAMESPACE_URI_STRING, ELEMENT_BODY); //create the body element
		XmlDom.appendText(bodyElement, "\n"); //append a newline to separate the information in the body
		generateAttributeGroupDocumentation(schema, bodyElement); //generate the attribute group documentation
		/*TODO fix
				Element paragraphElement=parseParagraph(document, bufferedReader);	//parse the first paragraph
				while(paragraphElement!=null) {	//keep reading until we run out of paragraphs
					XMLUtilities.appendText(document, bodyElement, "\n");	//append a newline to separate the paragraphs
					bodyElement.appendChild(paragraphElement);	//add the paragraph element to our document
					paragraphElement=parseParagraph(document, bufferedReader);	//parse the next paragraph
				}
		*/
		XmlDom.appendText(bodyElement, "\n"); //append a newline to end the content of the body element
		XmlDom.appendText(htmlElement, "\n"); //append a newline to end the content of the html element
		return document; //return the document we created
	}

	/**
	 * Creates a paragraph of documentation based upon the annotation of the given named schema component, if annotation information is available. The annotation
	 * information will be documented by creating a paragraph for each user information element present in the annotation.
	 * @param bodyElement The HTML body element of the documentation XML document.
	 * @param namedComponent The component for which annotation information should be documented, if annotation information is available.
	 * @see XMLSchemaNamedComponent#getAnnotation
	 */
	protected static void generateAnnotationDocumentation(final Element bodyElement, final XMLSchemaNamedComponent namedComponent) {
		Clogr.getLogger(XMLSchemaDocumenter.class).trace("ready to generate annotation doc");
		final XMLSchemaAnnotation annotation = namedComponent.getAnnotation(); //get the annotation for this component
		if(annotation != null) { //if there is annotation information
			Clogr.getLogger(XMLSchemaDocumenter.class).trace("found annotation");
			final Iterator userInfoIterator = annotation.getUserInformation().iterator(); //get an iterator for the user information from the annotation
			while(userInfoIterator.hasNext()) { //while there is more user information
				//get the next user information
				final XMLSchemaAnnotation.UserInformation userInfo = (XMLSchemaAnnotation.UserInformation)userInfoIterator.next();
				Clogr.getLogger(XMLSchemaDocumenter.class).trace("found user info: {}", userInfo.toString());
				final Element userInfoParagraphElement = XmlDom.appendElementNS(bodyElement, XHTML_NAMESPACE_URI_STRING, ELEMENT_P, null); //create the annotation documentation paragraph, but do not add any content
				//TODO do something besides creating a new XML parser from scratch
				//TODO see if we can do something besides casting the document to an XMLDocument
				try {
					//parse the element content from the user info string, preserving the entire XML sub-tree
					//TODO fix					new XMLProcessor().parseElementContent((XMLElement)userInfoParagraphElement, userInfo.toString());
				} catch(Exception e) { //TODO fix these exceptions; let them bubble up
					Clogr.getLogger(XMLSchemaDocumenter.class).error(e.getMessage(), e);
				}
				XmlDom.appendText(bodyElement, "\n"); //append a newline to separate the information in the body
			}
		}
	}

	/**
	 * Generates documentation for any globally defined attribute groups, appending the content to an existing XML document.
	 * @param schema The schema for which to create documentation.
	 * @param bodyElement The HTML body element of the documentation XML document.
	 */
	protected static void generateAttributeGroupDocumentation(final XMLSchema schema, final Element bodyElement) {
		//get a list of all attribute groups
		final Collection attributeGroupCollection = schema.getComponents(schema.getTargetNamespace(), XMLSchemaSymbolTable.ATTRIBUTE_GROUP_SYMBOL_SPACE_NAME);
		if(attributeGroupCollection.size() > 0) { //if there are global attribute groups
			//<h2>Global Attribute Groups</h2>
			final Element headingElement = XmlDom.appendElementNS(bodyElement, XHTML_NAMESPACE_URI_STRING, ELEMENT_H2, "Global Attribute Groups"); //create the heading TODO i18n
			XmlDom.appendText(bodyElement, "\n"); //append a newline to separate the information in the body
			final Iterator attributeGroupIterator = attributeGroupCollection.iterator(); //get an iterator to look through the attribute groups
			while(attributeGroupIterator.hasNext()) { //while there are more attribute groups
				final XMLSchemaAttributeGroup attributeGroup = (XMLSchemaAttributeGroup)attributeGroupIterator.next(); //get the next attribute group
				generateDocumentation(schema, bodyElement, attributeGroup); //generate the documentation for this attribute group
			}
		}
	}

	/**
	 * Documents an attribute group in a schema.
	 * @param schema The schema for which to create documentation. TODO del if not needed
	 * @param bodyElement The HTML body element of the documentation XML document.
	 * @param attributeGroup The attribute group to document.
	 */
	public static void generateDocumentation(final XMLSchema schema, final Element bodyElement, final XMLSchemaAttributeGroup attributeGroup) {
		//<h3>Attribute Group Name</h3>
		final Element attributeGroupHeadingElement = XmlDom.appendElementNS(bodyElement, XHTML_NAMESPACE_URI_STRING, ELEMENT_H3, attributeGroup.getName()); //create the heading for the attribute group
		//set the "id" attribute to identify this attribute
		attributeGroupHeadingElement.setAttributeNS(null, ATTRIBUTE_ID, "attributeGroup_" + attributeGroup.getName());
		XmlDom.appendText(bodyElement, "\n"); //append a newline to separate the information in the body
		generateAnnotationDocumentation(bodyElement, attributeGroup); //generate documentation for any annotation this component may have
		/*TODO fix
				//get an iterator to look at the contents of this attribute group
				final Iterator attributeGroupChildIterator=attributeGroup.getChildComponentList().iterator();


				//<h4>Contents</h4>
				final Element attributeGroupHeadingElement=XMLUtilities.appendElement(bodyElement, XHTML_NAMESPACE_URI,
						ELEMENT_H4, "Contents"); //create the heading for the attribute group contents TODO i18n
				final Element contentListElement=XMLUtilities.appendElement(bodyElement, XHTML_NAMESPACE_URI,
						ELEMENT_UL, "Contents"); //create the heading for the attribute group contents TODO i18n
		*/
	}

}
