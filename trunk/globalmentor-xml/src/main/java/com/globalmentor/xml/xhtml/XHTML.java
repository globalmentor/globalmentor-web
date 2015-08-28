/*
 * Copyright © 1996-2012 GlobalMentor, Inc. <http://www.globalmentor.com/>
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

package com.globalmentor.xml.xhtml;

import java.io.*;
import java.net.URI;
import java.util.*;

import javax.xml.parsers.*;

import org.w3c.dom.*;

import com.globalmentor.config.ConfigurationException;
import com.globalmentor.io.*;

import com.globalmentor.java.Arrays;
import com.globalmentor.java.Objects;
import com.globalmentor.log.Log;
import com.globalmentor.net.ContentType;
import com.globalmentor.xml.XML;

import static com.globalmentor.text.ASCII.*;
import static com.globalmentor.w3c.spec.HTML.*;
import static com.globalmentor.xml.XML.*;
import static com.globalmentor.xml.xpath.XPath.*;

/**
 * Utilities for working with XHTML DOM documents.
 * @author Garret Wilson
 * @see <a href="http://www.pibil.org/technology/writing/xhtml-media-type.html">XHTML: An XML Application</a>
 * @see <a href="http://www.w3.org/TR/xhtml-media-types/">XHTML Media Types</a>
 */
public class XHTML {

	/**
	 * Creates a new unformatted default XHTML document with the required minimal structure, {@code <html><head><title></title<head><body></html>}, with no
	 * document type.
	 * @param title The title of the document.
	 * @param includeDocumentType Whether a document type should be added to the document.
	 * @param publicID The external subset public identifier, or <code>null</code> if there is no such identifier.
	 * @param systemID The external subset system identifier, or <code>null</code> if there is no such identifier.
	 * @return A newly created default generic XHTML document with the required minimal structure.
	 * @throws NullPointerException if the given title is <code>null</code>.
	 * @throws ConfigurationException if a document builder cannot be created which satisfies the configuration requested.
	 * @throws DOMException if there is an error creating the XHTML document.
	 */
	public static Document createXHTMLDocument(final String title) throws DOMException {
		return createXHTMLDocument(title, false); //create an unformatted XHTML document with no document type
	}

	/**
	 * Creates a new default XHTML document with the required minimal structure, {@code <html><head><title></title<head><body></html>}, with no document type.
	 * @param title The title of the document.
	 * @param includeDocumentType Whether a document type should be added to the document.
	 * @param publicID The external subset public identifier, or <code>null</code> if there is no such identifier.
	 * @param systemID The external subset system identifier, or <code>null</code> if there is no such identifier.
	 * @param formatted <code>true</code> if the sections of the document should be formatted.
	 * @return A newly created default generic XHTML document with the required minimal structure.
	 * @throws NullPointerException if the given title is <code>null</code>.
	 * @throws ConfigurationException if a document builder cannot be created which satisfies the configuration requested.
	 * @throws DOMException if there is an error creating the XHTML document.
	 */
	public static Document createXHTMLDocument(final String title, final boolean formatted) throws DOMException {
		return createXHTMLDocument(title, false, null, null, formatted); //create an XHTML document with no document type 
	}

	/**
	 * Creates a new unformatted default XHTML document with the required minimal structure, {@code <html><head><title></title<head><body></html>}, with an
	 * optional document type but no public ID or system ID.
	 * @param title The title of the document.
	 * @param includeDocumentType Whether a document type should be added to the document.
	 * @param formatted <code>true</code> if the sections of the document should be formatted.
	 * @return A newly created default generic XHTML document with the required minimal structure.
	 * @throws NullPointerException if the given title is <code>null</code>.
	 * @throws ConfigurationException if a document builder cannot be created which satisfies the configuration requested.
	 * @throws DOMException if there is an error creating the XHTML document.
	 */
	public static Document createXHTMLDocument(final String title, final boolean includeDocumentType, final boolean formatted) throws DOMException {
		return createXHTMLDocument(title, includeDocumentType, null, null, formatted); //create an XHTML document with optional doctype		
	}

	/**
	 * Creates a new unformatted default XHTML document with the required minimal structure, {@code <html><head><title></title<head><body></html>}, with an
	 * optional document type.
	 * @param title The title of the document.
	 * @param includeDocumentType Whether a document type should be added to the document.
	 * @param publicID The external subset public identifier, or <code>null</code> if there is no such identifier.
	 * @param systemID The external subset system identifier, or <code>null</code> if there is no such identifier.
	 * @return A newly created default generic XHTML document with the required minimal structure.
	 * @throws NullPointerException if the given title is <code>null</code>.
	 * @throws ConfigurationException if a document builder cannot be created which satisfies the configuration requested.
	 * @throws DOMException if there is an error creating the XHTML document.
	 */
	public static Document createXHTMLDocument(final String title, final boolean includeDocumentType, final String publicID, final String systemID)
			throws DOMException {
		return createXHTMLDocument(title, includeDocumentType, publicID, systemID, false); //create an unformatted XHTML document
	}

	/**
	 * Creates a new default XHTML document with the required minimal structure, {@code <html><head><title></title<head><body></html>}, with an optional document
	 * type.
	 * @param title The title of the document.
	 * @param includeDocumentType Whether a document type should be added to the document.
	 * @param publicID The external subset public identifier, or <code>null</code> if there is no such identifier.
	 * @param systemID The external subset system identifier, or <code>null</code> if there is no such identifier.
	 * @param formatted <code>true</code> if the sections of the document should be formatted.
	 * @return A newly created default generic XHTML document with the required minimal structure.
	 * @throws NullPointerException if the given title is <code>null</code>.
	 * @throws ConfigurationException if a document builder cannot be created which satisfies the configuration requested.
	 * @throws DOMException if there is an error creating the XHTML document.
	 */
	public static Document createXHTMLDocument(final String title, final boolean includeDocumentType, final String publicID, final String systemID,
			final boolean formatted) throws DOMException {
		final DocumentBuilder documentBuilder = createDocumentBuilder(true); //create a namespace-aware document builder
		final DOMImplementation domImplementation = documentBuilder.getDOMImplementation(); //get the DOM implementation from the document builder
		final DocumentType documentType; //the document type, if any
		if(includeDocumentType) { //if we should add a document type
			documentType = domImplementation.createDocumentType(ELEMENT_HTML, publicID, systemID); //create an XHTML document, using the given identifiers, if any 
		} else { //if we should not add a document type
			documentType = null; //don't use a document type
		}
		final Document document = domImplementation.createDocument(XHTML_NAMESPACE_URI.toString(), ELEMENT_HTML, documentType); //create an XHTML document
		//TODO check about whether we need to add a <head> and <title>
		final Element htmlElement = document.getDocumentElement(); //get the html element
		if(formatted) { //if we should format the document
			appendText(htmlElement, "\n"); //append a newline to start the content of the html element
		}
		final Element headElement = document.createElementNS(XHTML_NAMESPACE_URI.toString(), ELEMENT_HEAD); //create the head element
		htmlElement.appendChild(headElement); //add the head element to the html element
		if(formatted) { //if we should format the document
			appendText(headElement, "\n"); //append a newline to start the content in the head
			appendText(htmlElement, "\n"); //append a newline to separate the content of the html element
		}
		final Element titleElement = document.createElementNS(XHTML_NAMESPACE_URI.toString(), ELEMENT_TITLE); //create the title element
		headElement.appendChild(titleElement); //add the title element to the head element
		appendText(titleElement, title); //append the title text to the title element 
		if(formatted) { //if we should format the document
			appendText(headElement, "\n"); //append a newline to separate the informtaion after the title
		}
		final Element bodyElement = document.createElementNS(XHTML_NAMESPACE_URI.toString(), ELEMENT_BODY); //create the body element
		htmlElement.appendChild(bodyElement); //add the body element to the html element
		if(formatted) { //if we should format the document
			appendText(bodyElement, "\n"); //append a newline to separate the information in the body
			appendText(htmlElement, "\n"); //append a newline to separate the information after the body
		}
		return document; //return the document we created
	}

	/**
	 * Finds the XHTML <code>&lt;body&gt;</code> element.
	 * @param document The XHTML document tree.
	 * @return A reference to the <code>&lt;body&gt;</code> element, or <code>null</code> if there is such element.
	 */
	public static Element getBodyElement(final Document document) {
		return (Element)getNode(document, LOCATION_STEP_SEPARATOR_CHAR + ELEMENT_BODY);
	}

	/**
	 * Finds the XHTML {@code <head>} element.
	 * @param document The XHTML document tree.
	 * @return A reference to the {@code <head>} element, or <code>null</code> if there is no such element.
	 */
	public static Element getHeadElement(final Document document) {
		return (Element)getNode(document, LOCATION_STEP_SEPARATOR_CHAR + ELEMENT_HEAD);
	}

	/**
	 * Finds the XHTML {@code <head><title>} element.
	 * @param document The XHTML document tree.
	 * @return A reference to the {@code <head><title>} element, or <code>null</code> if there is no such element.
	 */
	public static Element getHeadTitleElement(final Document document) {
		return (Element)getNode(document, LOCATION_STEP_SEPARATOR_CHAR + ELEMENT_HEAD + LOCATION_STEP_SEPARATOR_CHAR + ELEMENT_TITLE);
	}

	/**
	 * Finds all XHTML {@code <head><meta>} elements.
	 * @param document The XHTML document tree.
	 * @return A list of all {@code <head><meta>} elements, if any.
	 */
	@SuppressWarnings("unchecked")
	public static List<Element> getHeadMetaElements(final Document document) {
		//		final Element headElement=getHeadElement(document);	//TODO improve XPath processor to extract elements

		return getElements((List<Node>)evaluatePathExpression(document.getDocumentElement(), ELEMENT_HEAD + LOCATION_STEP_SEPARATOR_CHAR + ELEMENT_META));
	}

	/**
	 * Finds the first XHTML {@code <head><meta>} element with the given name. The meta name is matched in a case insensitive manner.
	 * @param document The XHTML document tree.
	 * @param metaName The name of the meta element to return.
	 * @return The first {@code <head><meta>} element with the given name, case insensitive, or <code>null</code> if no such meta element can be found.
	 * @throws NullPointerException if the given document and/or name is <code>null</code>.
	 * @see #ELEMENT_META_ATTRIBUTE_NAME
	 */
	public static Element getHeadMetaElement(final Document document, final String metaName) {
		for(final Element element : getHeadMetaElements(document)) { //get all the meta elements
			if(metaName.equalsIgnoreCase(element.getAttribute(ELEMENT_META_ATTRIBUTE_NAME))) { //if this meta element has the correct name
				return element;
			}
		}
		return null; //we coudn't find such a named meta element
	}

	/**
	 * Finds the first XHTML {@code <head><meta>} element with the given name and returns its <code>content</code> attribute. The meta name is matched in a case
	 * insensitive manner.
	 * @param document The XHTML document tree.
	 * @param metaName The name of the meta element to return.
	 * @return The <code>content</code> attribute of the first {@code <head><meta>} element with the given name, case insensitive, or <code>null</code> if no such
	 *         meta element can be found.
	 * @throws NullPointerException if the given document and/or name is <code>null</code>.
	 * @see #ELEMENT_META_ATTRIBUTE_NAME
	 * @see #ELEMENT_META_ATTRIBUTE_CONTENT
	 */
	public static String getHeadMetaElementContent(final Document document, final String metaName) {
		final Element metaElement = getHeadMetaElement(document, metaName);
		return metaElement != null ? metaElement.getAttribute(ELEMENT_META_ATTRIBUTE_CONTENT) : null;
	}

	/**
	 * Determines the reference to the image file represented by the element, assuming the element (an "img" or "object" element) does in fact represent an image.
	 * For "img", this return the "src" attribute. For objects, the value of the "data" attribute is returned.
	 * @param xhtmlNamespaceURI The XHTML namespace.
	 * @param element The element which contains the image information.
	 * @return The reference to the image file, or <code>null</code> if no reference could be found.
	 */
	public static String getImageElementHRef(final String xhtmlNamespaceURI, final Element element) {
		if(element != null) { //if a valid element is passed
			final String namespaceURI = element.getNamespaceURI(); //get the element's namespace URI
			//if this element is in the correct namespace
			if((xhtmlNamespaceURI == null && namespaceURI == null) || namespaceURI.equals(xhtmlNamespaceURI)) {
				final String elementName = element.getLocalName(); //get the element's name
				//see if this is an <img> or <object> element
				if(elementName.equals(ELEMENT_IMG)) { //if the corresponding element is an img element
					//get the src attribute, representing the href of the image, or null if not present
					return getDefinedAttributeNS(element, null, ELEMENT_IMG_ATTRIBUTE_SRC);
				} else if(elementName.equals(ELEMENT_OBJECT)) { //if the corresponding element is an object element
					//get the data attribute, representing the href of the image, or null if not present
					return getDefinedAttributeNS(element, null, ELEMENT_OBJECT_ATTRIBUTE_DATA);
				}
			}
		}
		return null; //show that we couldn't find an image reference
	}

	/**
	 * Determines if the specified element represents a link, and if so attempts to find the link element's reference.
	 * @param xhtmlNamespaceURI The XHTML namespace.
	 * @param element The element which might represent a link.
	 * @return The reference of the link, or <code>null</code> if the specified element does not represent a link or has no reference.
	 */
	public static String getLinkElementHRef(final String xhtmlNamespaceURI, final Element element) { //TODO fix to call XLink and see if this is an XLink link
		if(element != null) { //if a valid element was passed
			final String namespaceURI = element.getNamespaceURI(); //get the element's namespace URI
			//if this element is in the correct namespace
			if(Objects.equals(xhtmlNamespaceURI, namespaceURI)) {
				final String elementName = element.getLocalName(); //get the element's name
				if(elementName.equals(ELEMENT_A)) { //if this is an <a> element
					return getDefinedAttributeNS(element, null, ELEMENT_A_ATTRIBUTE_HREF); //return the value of the href attribute, if there is one
				}
			}
		}
		return null; //show that we couldn't find an href or this wasn't even a link
	}

	/**
	 * Determines the content type indicated by the given link element.
	 * @param element The element, presumably a link ({@code<a>}, {@code<area>}, or {@code<link>}).
	 * @return The content type specified by the link element, or <code>null</code> if no content type was specified or the content type was not syntactically
	 *         correct.
	 * @see #LINK_ATTRIBUTE_TYPE
	 * @see <a href="http://www.w3.org/TR/html5/links.html#attr-hyperlink-type">HTML5 Link MIME type</a>
	 */
	public static ContentType getLinkContentType(final Element element) {
		final String typeString = element.getAttributeNS(null, LINK_ATTRIBUTE_TYPE); //get the value of the type attribute
		if(!typeString.isEmpty()) { //if there is a type specified
			try {
				return ContentType.create(typeString); //parse the content type and return it
			} catch(final IllegalArgumentException illegalArgumentException) { //if the content type isn't valid
				Log.debug(illegalArgumentException);
			}
		}
		return null;
	}

	/**
	 * Checks to see if the given link's {@value #LINK_ATTRIBUTE_REL} attribute contains the given link type with ASCII case insensitivity.
	 * @param element The element, presumably a link ({@code<a>}, {@code<area>}, or {@code<link>}).
	 * @param linkType The type of link to look for.
	 * @return <code>true</code> if the given element has a {@value #LINK_ATTRIBUTE_REL} attribute with one of the given link types.
	 * @see #LINK_ATTRIBUTE_REL
	 * @see <a href="http://www.w3.org/TR/html5/links.html#linkTypes">HTML5 Link Types</a>
	 */
	public static boolean hasLinkType(final Element element, final LinkType linkType) {
		return hasLinkType(element, linkType.getID());
	}

	/**
	 * Checks to see if the given link's {@value #LINK_ATTRIBUTE_REL} attribute contains the given link type with ASCII case insensitivity.
	 * @param element The element, presumably a link ({@code<a>}, {@code<area>}, or {@code<link>}).
	 * @param linkType The type of link to look for.
	 * @return <code>true</code> if the given element has a {@value #LINK_ATTRIBUTE_REL} attribute with one of the given link types.
	 * @see #LINK_ATTRIBUTE_REL
	 */
	public static boolean hasLinkType(final Element element, final String linkType) {
		return containsTokenIgnoreCase(element.getAttributeNS(null, LINK_ATTRIBUTE_REL), SPACE_CHARACTERS, linkType); //see if the given link type ID is one of the tokens in the "rel" attribute value
	}

	/**
	 * Determines if the given content type is one representing HTML in some form.
	 * <p>
	 * HTML content types include:
	 * </p>
	 * <ul>
	 * <li><code>text/html</code></li>
	 * <li><code>application/xhtml+xml</code></li>
	 * <li><code>application/xhtml+xml-external-parsed-entity</code> (not formally defined)</li>
	 * </ul>
	 * @param contentType The content type of a resource, or <code>null</code> for no content type.
	 * @return <code>true</code> if the given media type is one of several HTML media types.
	 */
	public static boolean isHTML(final ContentType contentType) { //TODO maybe move this to an HTMLUtilities
		if(contentType != null) { //if a media type is given
			final String primaryType = contentType.getPrimaryType(); //get the primary type
			final String subType = contentType.getSubType(); //get the sub type
			if(ContentType.TEXT_PRIMARY_TYPE.equals(primaryType)) { //if this is "text/?"
				if(HTML_SUBTYPE.equals(subType) //if this is "text/html"
				/*TODO fix for OEB || OEB.X_OEB1_DOCUMENT_SUBTYPE.equals(subType)*/) { //if this is "text/x-oeb1-document"
					return true; //show that this is HTML
				}
			}
			if(ContentType.APPLICATION_PRIMARY_TYPE.equals(primaryType)) { //if this is "application/?"
				//TODO probably add a parameter to specify whether we should allow fragments to qualify as XHTML---right now this behavior is not consistent with XMLUtilities.isXML(), which doesn't recognize fragments as XML
				if(XHTML_XML_SUBTYPE.equals(subType) || XHTML_XML_EXTERNAL_PARSED_ENTITY_SUBTYPE.equals(subType)) { //if this is "application/xhtml+xml" or "application/xhtml+xml-external-parsed-entity"
					return true; //show that this is HTML
				}
			}
		}
		return false; //this is not a media type we recognize as being HTML
	}

	/**
	 * Determines if the given URI represents one of the HTML XML namespaces.
	 * <p>
	 * HTML namespaces include:
	 * </p>
	 * <ul>
	 * <li><code>http://www.w3.org/1999/xhtml</code></li>
	 * </ul>
	 * @param namespaceURI A URI representing an XML namespace, or <code>null</code> for no namespace.
	 * @return <code>true</code> if the given URI represents one of the HTML XML namespaces.
	 */
	public static boolean isHTMLNamespaceURI(final URI namespaceURI) {
		if(namespaceURI != null) { //if the namespace URI is valid
			//if it's part of the XHTML namespace
			if(XHTML_NAMESPACE_URI.equals(namespaceURI) /*TODO fix for OEB || OEB.OEB1_DOCUMENT_NAMESPACE_URI.equals(namespaceURI)*/)
				return true; //show that this is an HTML namespace
		}
		return false; //show that we didn't recognize the namespace as HTML
	}

	/**
	 * Determines if the specified element represents an image. Specifically, this returns <code>true</code> if the element's name is "img"; or if the element's
	 * name is "object" and the type attribute is an image or the data attribute references an image file.
	 * @param xhtmlNamespaceURI The XHTML namespace.
	 * @param element The element which might represent an image.
	 * @return <code>true</code> if the specified element represents an image.
	 */
	public static boolean isImageElement(final String xhtmlNamespaceURI, final Element element) {
		if(element != null) { //if a valid element was passed
			final String namespaceURI = element.getNamespaceURI(); //get the element's namespace URI
			//if this element is in the correct namespace
			if(Objects.equals(xhtmlNamespaceURI, namespaceURI)) {
				//TODO fix				final String elementName=element.getLocalName();  //get the element's name
				final String elementName = element.getLocalName(); //get the element's name
				if(elementName.equals(ELEMENT_IMG)) //if this is an <img> element
					return true; //show that this is an image object
				else if(elementName.equals(ELEMENT_OBJECT)) { //if this is an <object> element
					//see if there is a type attribute
					if(element.hasAttributeNS(null, ELEMENT_OBJECT_ATTRIBUTE_TYPE)) { //if there is a type attribute
						final String type = element.getAttributeNS(null, ELEMENT_OBJECT_ATTRIBUTE_TYPE); //get the type
						final ContentType mediaType = ContentType.create(type); //create a media type from the given type
						if(mediaType.getPrimaryType().equals(ContentType.IMAGE_PRIMARY_TYPE)) //if this is an image
							return true; //show that this is an image object
					}
					//see if there is a data attribute, since there is no type specified
					if(element.hasAttributeNS(null, ELEMENT_OBJECT_ATTRIBUTE_DATA)) { //if there is a data attribute
						final String data = element.getAttributeNS(null, ELEMENT_OBJECT_ATTRIBUTE_DATA); //get the data
						final ContentType mediaType = Files.getContentType(new File(data)); //try to get a media type from the file
						if(mediaType != null && mediaType.getPrimaryType().equals(ContentType.IMAGE_PRIMARY_TYPE)) //if this is an image
							return true; //show that this is an image object
					}
				}
			}
		}
		return false; //this does not appear to be an image element
	}

	/**
	 * Determines if the specified element represents a link. Specifically, this returns <code>true</code> if the element's name is "a".
	 * @param xhtmlNamespaceURI The XHTML namespace.
	 * @param element The element which might represent a link.
	 * @return <code>true</code> if the specified element represents a link.
	 */
	public static boolean isLinkElement(final String xhtmlNamespaceURI, final Element element) { //TODO fix to call XLink and see if this is an XLink link
		if(element != null) { //if a valid element was passed
			final String namespaceURI = element.getNamespaceURI(); //get the element's namespace URI
			//if this element is in the correct namespace
			if(Objects.equals(xhtmlNamespaceURI, namespaceURI)) {
				final String elementName = element.getLocalName(); //get the element's name
				if(elementName.equals(ELEMENT_A)) //if this is an <a> element
					return true; //show that this is a link element
			}
		}
		return false; //this does not appear to be a link element
	}

	/**
	 * Determines if the specified element represents an empty element&mdash;an element that might be declared as <code>EMPTY</code> in a DTD.
	 * @param namespaceURI The element namespace.
	 * @param localName The local name of the element.
	 * @return <code>true</code> if the specified element should be empty.
	 */
	public static boolean isEmptyElement(final URI namespaceURI, final String localName) {
		//TODO make this static---placed here because it was causing a compile error due to an Eclipse bug
		final String[] EMPTY_ELEMENT_LOCAL_NAMES = new String[] { ELEMENT_BASE, ELEMENT_META, ELEMENT_LINK, ELEMENT_HR, ELEMENT_BR, ELEMENT_PARAM, ELEMENT_IMG,
				ELEMENT_AREA, ELEMENT_INPUT, ELEMENT_COL }; //TODO probably put these in a better place, either in a hash set for faster lookup or put this constant in XHTMLConstants
		if(localName != null && (namespaceURI == null || isHTMLNamespaceURI(namespaceURI))) { //if this element has an HTML namespace or no namespace
			return Arrays.indexOf(EMPTY_ELEMENT_LOCAL_NAMES, localName) >= 0; //return whether the local name is one of the empty element local names
		}
		return false; //this does not appear to be an empty element
	}

	/**
	 * Determines if the specified element represents a link, and if so sets the link element's reference.
	 * @param xhtmlNamespaceURI The XHTML namespace.
	 * @param element The element which represents a link.
	 * @param href The new reference to add to the link.
	 */
	public static void setLinkElementHRef(final String xhtmlNamespaceURI, final Element element, final String href) { //TODO fix to call XLink and see if this is an XLink link
		if(element != null) { //if a valid element was passed
			final String namespaceURI = element.getNamespaceURI(); //get the element's namespace URI
			//if this element is in the correct namespace
			if(Objects.equals(xhtmlNamespaceURI, namespaceURI)) {
				final String elementName = element.getLocalName(); //get the element's name
				if(elementName.equals(ELEMENT_A)) { //if this is an <a> element
					element.setAttributeNS(null, ELEMENT_A_ATTRIBUTE_HREF, href); //set the href attribute TODO what if they were using an attribute prefix?
				}
			}
		}
	}

	/**
	 * Retrieves the text of the node contained in child nodes of type {@link Node#TEXT_NODE}, extracting text deeply.
	 * <p>
	 * This HTML-specific version adds whitespace to separate block elements.
	 * </p>
	 * @param node The node from which text will be retrieved.
	 * @return The data of all <code>Text</code> descendant nodes, which may be the empty string.
	 * @see XML#getText(Node, Set)
	 * @see #BLOCK_ELEMENTS
	 */
	public static String getText(final Node node) {
		return XML.getText(node, BLOCK_ELEMENTS);
	}

	/**
	 * Retrieves the text of the node contained in child nodes of type <code>Node.Text</code>. If <code>deep</code> is set to <code>true</code> the text of all
	 * descendant nodes in document (depth-first) order; otherwise, only text of direct children will be returned.
	 * <p>
	 * This HTML-specific version adds whitespace to separate block elements.
	 * </p>
	 * @param node The node from which text will be retrieved.
	 * @param deep Whether text of all descendants in document order will be returned.
	 * @param stringBuilder The buffer to which text will be added.
	 * @see XML#getText(Node, Set, boolean, StringBuilder)
	 * @see #BLOCK_ELEMENTS
	 */
	public static void getText(final Node node, final StringBuilder stringBuilder) {
		XML.getText(node, BLOCK_ELEMENTS, true, stringBuilder);
	}

}
