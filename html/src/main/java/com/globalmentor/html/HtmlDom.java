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

package com.globalmentor.html;

import static java.util.Objects.*;
import static java.util.function.Predicate.*;

import java.io.*;
import java.net.URI;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import javax.annotation.*;
import javax.xml.parsers.*;

import org.w3c.dom.*;

import com.globalmentor.io.*;

import com.globalmentor.java.Arrays;
import com.globalmentor.java.Objects;
import com.globalmentor.model.ConfigurationException;
import com.globalmentor.net.ContentType;
import com.globalmentor.text.ASCII;
import com.globalmentor.html.spec.HTML;
import com.globalmentor.xml.XmlDom;

import io.clogr.Clogr;

import static com.globalmentor.text.ASCII.*;
import static com.globalmentor.html.spec.HTML.*;
import static com.globalmentor.java.Conditions.*;
import static com.globalmentor.xml.XmlDom.*;

/**
 * Utilities for working with XHTML DOM documents.
 * @implSpec The methods in this class assume that HTML elements are the in {@value HTML#XHTML_NAMESPACE_URI_STRING} namespace, which at least on the web should
 *           occur <a href="https://www.w3.org/TR/html52/infrastructure.html#xml">even for HTML documents</a>.
 * @author Garret Wilson
 * @see <a href="http://www.pibil.org/technology/writing/xhtml-media-type.html">XHTML: An XML Application</a>
 * @see <a href="http://www.w3.org/TR/xhtml-media-types/">XHTML Media Types</a>
 */
public class HtmlDom {

	/**
	 * Creates a new unformatted default XHTML document with the required minimal structure, {@code <html><head><title></title<head><body></html>}, with no
	 * document type.
	 * @param title The title of the document.
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
	 * Finds the root {@code <html>} element in the XHTML namespace.
	 * @param document The XHTML document tree.
	 * @return The root element only if it is the XHTML {@code <html>} element.
	 * @see HTML#XHTML_NAMESPACE_URI
	 * @see HTML#ELEMENT_HTML
	 */
	public static Optional<Element> findHtmlElement(@Nonnull final Document document) {
		return Optional.ofNullable(document.getDocumentElement())
				.filter(documentElement -> XHTML_NAMESPACE_URI_STRING.equals(documentElement.getNamespaceURI()) && documentElement.getLocalName().equals(ELEMENT_HTML));
	}

	/**
	 * Finds the {@code <html><body>} element in the XHTML namespace.
	 * @param document The XHTML document tree.
	 * @return A reference to the {@code <html><body>} element if it exists in the tree.
	 * @see HTML#XHTML_NAMESPACE_URI
	 * @see HTML#ELEMENT_BODY
	 */
	public static Optional<Element> findHtmlBodyElement(@Nonnull final Document document) {
		return findHtmlElement(document).flatMap(htmlElement -> findFirstChildElementByNameNS(htmlElement, XHTML_NAMESPACE_URI_STRING, ELEMENT_BODY));
	}

	/**
	 * Finds the {@code <html><head>} element in the XHTML namespace.
	 * @param document The XHTML document tree.
	 * @return A reference to the {@code <html><head>} element if it exists in the tree.
	 * @see HTML#XHTML_NAMESPACE_URI
	 * @see HTML#ELEMENT_HEAD
	 */
	public static Optional<Element> findHtmlHeadElement(@Nonnull final Document document) {
		return findHtmlElement(document).flatMap(htmlElement -> findFirstChildElementByNameNS(htmlElement, XHTML_NAMESPACE_URI_STRING, ELEMENT_HEAD));
	}

	/**
	 * Finds the {@code <html><head><title>} element in the XHTML namespace.
	 * @param document The XHTML document tree.
	 * @return A reference to the {@code <html><head><title>} element if it exists in the tree.
	 * @see HTML#XHTML_NAMESPACE_URI
	 * @see HTML#ELEMENT_TITLE
	 */
	public static Optional<Element> findHtmlHeadTitleElement(@Nonnull final Document document) {
		return findHtmlHeadElement(document).flatMap(htmlElement -> findFirstChildElementByNameNS(htmlElement, XHTML_NAMESPACE_URI_STRING, ELEMENT_TITLE));
	}

	/**
	 * Finds the {@code <html><head><meta>} elements in an HTML document.
	 * @param document The XHTML document tree.
	 * @return A stream of {@code <html><head><meta>} elements if they exist in the tree.
	 * @see HTML#XHTML_NAMESPACE_URI
	 * @see HTML#ELEMENT_META
	 */
	public static Stream<Element> htmlHeadMetaElements(@Nonnull final Document document) {
		return findHtmlHeadElement(document).map(headElement -> childElementsByNameNS(headElement, XHTML_NAMESPACE_URI_STRING, ELEMENT_META))
				.orElse(Stream.empty());
	}

	/**
	 * Determines the reference to the image file represented by the element, assuming the element (an "img" or "object" element) does in fact represent an image.
	 * For "img", this return the "src" attribute. For objects, the value of the "data" attribute is returned. The {@value HTML#XHTML_NAMESPACE_URI_STRING}
	 * namespace is used.
	 * @implSpec This method delegates to {@link #findImageElementHRef(Element, String)}.
	 * @param element The element which contains the image information.
	 * @return The reference to the image file.
	 * @throws NullPointerException if the given element is <code>null</code>.
	 */
	public static Optional<String> findImageElementHRef(@Nonnull final Element element) {
		return findImageElementHRef(element, XHTML_NAMESPACE_URI_STRING);
	}

	/**
	 * Determines the reference to the image file represented by the element, assuming the element (an "img" or "object" element) does in fact represent an image.
	 * For "img", this return the "src" attribute. For objects, the value of the "data" attribute is returned.
	 * @param element The element which contains the image information.
	 * @param htmlNamespaceURI The HTML namespace.
	 * @return The reference to the image file.
	 * @throws NullPointerException if the given element is <code>null</code>.
	 */
	public static Optional<String> findImageElementHRef(@Nonnull final Element element, @Nullable final String htmlNamespaceURI) {
		final String namespaceURI = element.getNamespaceURI(); //get the element's namespace URI
		//if this element is in the correct namespace
		if(Objects.equals(htmlNamespaceURI, namespaceURI)) {
			final String elementName = element.getLocalName(); //get the element's name
			//see if this is an <img> or <object> element
			if(elementName.equals(ELEMENT_IMG)) { //if the corresponding element is an img element
				//get the src attribute, representing the href of the image, or null if not present
				return findAttributeNS(element, null, ELEMENT_IMG_ATTRIBUTE_SRC);
			} else if(elementName.equals(ELEMENT_OBJECT)) { //if the corresponding element is an object element
				//get the data attribute, representing the href of the image, or null if not present
				return findAttributeNS(element, null, ELEMENT_OBJECT_ATTRIBUTE_DATA);
			}
		}
		return Optional.empty(); //show that we couldn't find an image reference
	}

	/**
	 * Determines if the specified element represents a link, and if so attempts to find the link element's reference. The
	 * {@value HTML#XHTML_NAMESPACE_URI_STRING} namespace is used.
	 * @implSpec This method delegates to {@link #findLinkElementHRef(Element, String)}.
	 * @param element The element which might represent a link.
	 * @return The reference of the link, which will not be present if the specified element does not represent a link or has no reference.
	 * @throws NullPointerException if the given element is <code>null</code>.
	 */
	public static Optional<String> findLinkElementHRef(@Nonnull final Element element) { //TODO fix to call XLink and see if this is an XLink link
		return findLinkElementHRef(element, XHTML_NAMESPACE_URI_STRING);
	}

	/**
	 * Determines if the specified element represents a link, and if so attempts to find the link element's reference.
	 * @param element The element which might represent a link.
	 * @param htmlNamespaceURI The HTML namespace.
	 * @return The reference of the link, which will not be present if the specified element does not represent a link or has no reference.
	 * @throws NullPointerException if the given element is <code>null</code>.
	 */
	public static Optional<String> findLinkElementHRef(@Nonnull final Element element, @Nullable final String htmlNamespaceURI) { //TODO fix to call XLink and see if this is an XLink link
		final String namespaceURI = element.getNamespaceURI(); //get the element's namespace URI
		//if this element is in the correct namespace
		if(Objects.equals(htmlNamespaceURI, namespaceURI)) {
			final String elementName = element.getLocalName(); //get the element's name
			if(elementName.equals(ELEMENT_A)) { //if this is an <a> element
				return findAttributeNS(element, null, ELEMENT_A_ATTRIBUTE_HREF); //return the value of the href attribute, if there is one
			}
		}
		return Optional.empty(); //show that we couldn't find an href or this wasn't even a link
	}

	/**
	 * Determines the content type indicated by the given link element.
	 * @param element The element, presumably a link ({@code <a>}, {@code <area>}, or {@code <link>}).
	 * @return The content type specified by the link element, which will not be present if no content type was specified or the content type was not
	 *         syntactically correct.
	 * @see HTML#LINK_ATTRIBUTE_TYPE
	 * @see <a href="http://www.w3.org/TR/html5/links.html#attr-hyperlink-type">HTML5 Link MIME type</a>
	 */
	public static Optional<ContentType> findLinkContentType(@Nonnull final Element element) {
		return findAttributeNS(element, null, LINK_ATTRIBUTE_TYPE).flatMap(link -> { //if there is a type specified
			try {
				return Optional.of(ContentType.create(link)); //parse the content type and return it
			} catch(final IllegalArgumentException illegalArgumentException) { //if the content type isn't valid
				Clogr.getLogger(HtmlDom.class).debug(illegalArgumentException.getMessage(), illegalArgumentException);
				return Optional.empty();
			}
		});
	}

	/**
	 * Checks to see if the given link's {@value HTML#LINK_ATTRIBUTE_REL} attribute contains the given link type with ASCII case insensitivity.
	 * @param element The element, presumably a link ({@code <a>}, {@code <area>}, or {@code <link>}).
	 * @param linkType The type of link to look for.
	 * @return <code>true</code> if the given element has a {@value HTML#LINK_ATTRIBUTE_REL} attribute with one of the given link types.
	 * @see HTML#LINK_ATTRIBUTE_REL
	 * @see <a href="http://www.w3.org/TR/html5/links.html#linkTypes">HTML5 Link Types</a>
	 */
	public static boolean hasLinkType(@Nonnull final Element element, @Nonnull final LinkType linkType) {
		return hasLinkType(element, linkType.getID());
	}

	/**
	 * Checks to see if the given link's {@value HTML#LINK_ATTRIBUTE_REL} attribute contains the given link type with ASCII case insensitivity.
	 * @param element The element, presumably a link ({@code<a>}, {@code<area>}, or {@code<link>}).
	 * @param linkType The type of link to look for.
	 * @return <code>true</code> if the given element has a {@value HTML#LINK_ATTRIBUTE_REL} attribute with one of the given link types.
	 * @see HTML#LINK_ATTRIBUTE_REL
	 */
	public static boolean hasLinkType(@Nonnull final Element element, @Nonnull final String linkType) {
		return containsTokenIgnoreCase(element.getAttributeNS(null, LINK_ATTRIBUTE_REL), SPACE_CHARACTERS, linkType); //see if the given link type ID is one of the tokens in the "rel" attribute value
	}

	/**
	 * Determines if the specified element represents an image. Specifically, this returns <code>true</code> if the element's name is "img"; or if the element's
	 * name is "object" and the type attribute is an image or the data attribute references an image file. The {@value HTML#XHTML_NAMESPACE_URI_STRING} namespace
	 * is used.
	 * @implSpec This method delegates to {@link #isImageElement(Element, String)}.
	 * @param element The element which might represent an image.
	 * @return <code>true</code> if the specified element represents an image.
	 * @throws NullPointerException if the given element is <code>null</code>.
	 */
	public static boolean isImageElement(@Nonnull final Element element) {
		return isImageElement(element, XHTML_NAMESPACE_URI_STRING);
	}

	/**
	 * Determines if the specified element represents an image. Specifically, this returns <code>true</code> if the element's name is "img"; or if the element's
	 * name is "object" and the type attribute is an image or the data attribute references an image file.
	 * @param htmlNamespaceURI The XHTML namespace.
	 * @param element The element which might represent an image.
	 * @return <code>true</code> if the specified element represents an image.
	 * @throws NullPointerException if the given element is <code>null</code>.
	 */
	public static boolean isImageElement(@Nonnull final Element element, @Nullable final String htmlNamespaceURI) {
		final String namespaceURI = element.getNamespaceURI(); //get the element's namespace URI
		//if this element is in the correct namespace
		if(Objects.equals(htmlNamespaceURI, namespaceURI)) {
			//TODO fix				final String elementName=element.getLocalName();  //get the element's name
			final String elementName = element.getLocalName(); //get the element's name
			if(elementName.equals(ELEMENT_IMG)) //if this is an <img> element
				return true; //show that this is an image object
			else if(elementName.equals(ELEMENT_OBJECT)) { //if this is an <object> element
				//see if there is a type attribute
				if(element.hasAttributeNS(null, ELEMENT_OBJECT_ATTRIBUTE_TYPE)) { //if there is a type attribute
					final String type = element.getAttributeNS(null, ELEMENT_OBJECT_ATTRIBUTE_TYPE); //get the type
					final ContentType mediaType = ContentType.create(type); //create a media type from the given type
					//TODO catch possible exception here
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
		return false; //this does not appear to be an image element
	}

	/**
	 * Determines if the specified element represents a link. Specifically, this returns <code>true</code> if the element's name is "a". The
	 * {@value HTML#XHTML_NAMESPACE_URI_STRING} namespace is used.
	 * @implSpec This method delegates to {@link #isLinkElement(Element, String)}.
	 * @param element The element which might represent a link.
	 * @return <code>true</code> if the specified element represents a link.
	 * @throws NullPointerException if the given element is <code>null</code>.
	 */
	public static boolean isLinkElement(@Nonnull final Element element) { //TODO fix to call XLink and see if this is an XLink link
		return isLinkElement(element, XHTML_NAMESPACE_URI_STRING);
	}

	/**
	 * Determines if the specified element represents a link. Specifically, this returns <code>true</code> if the element's name is "a".
	 * @param htmlNamespaceURI The XHTML namespace.
	 * @param element The element which might represent a link.
	 * @return <code>true</code> if the specified element represents a link.
	 * @throws NullPointerException if the given element is <code>null</code>.
	 */
	public static boolean isLinkElement(@Nonnull final Element element, @Nullable final String htmlNamespaceURI) { //TODO fix to call XLink and see if this is an XLink link
		final String namespaceURI = element.getNamespaceURI(); //get the element's namespace URI
		//if this element is in the correct namespace
		if(Objects.equals(htmlNamespaceURI, namespaceURI)) {
			final String elementName = element.getLocalName(); //get the element's name
			if(elementName.equals(ELEMENT_A)) //if this is an <a> element
				return true; //show that this is a link element
		}
		return false; //this does not appear to be a link element
	}

	/**
	 * Determines if the specified element represents an empty element—an element that might be declared as <code>EMPTY</code> in a DTD.
	 * @param namespaceURI The element namespace.
	 * @param localName The local name of the element.
	 * @return <code>true</code> if the specified element should be empty.
	 */
	public static boolean isEmptyElement(@Nullable final URI namespaceURI, @Nonnull final String localName) {
		//TODO make this static---placed here because it was causing a compile error due to an Eclipse bug
		final String[] EMPTY_ELEMENT_LOCAL_NAMES = new String[] {ELEMENT_BASE, ELEMENT_META, ELEMENT_LINK, ELEMENT_HR, ELEMENT_BR, ELEMENT_PARAM, ELEMENT_IMG,
				ELEMENT_AREA, ELEMENT_INPUT, ELEMENT_COL}; //TODO probably put these in a better place, either in a hash set for faster lookup or put this constant in XHTMLConstants
		if(localName != null && (namespaceURI == null || isHTMLNamespaceURI(namespaceURI))) { //if this element has an HTML namespace or no namespace
			return Arrays.indexOf(EMPTY_ELEMENT_LOCAL_NAMES, localName) >= 0; //return whether the local name is one of the empty element local names
		}
		return false; //this does not appear to be an empty element
	}

	/**
	 * Determines if the specified element represents a link, and if so sets the link element's reference.
	 * @param element The element which represents a link.
	 * @param htmlNamespaceURI The XHTML namespace.
	 * @param href The new reference to add to the link.
	 * @throws NullPointerException if the given element is <code>null</code>.
	 * @deprecated This doesn't seem like a good approach in light of modern best practices.
	 */
	@Deprecated
	public static void setLinkElementHRef(@Nonnull final Element element, @Nullable final String htmlNamespaceURI, @Nonnull final String href) { //TODO fix to call XLink and see if this is an XLink link
		final String namespaceURI = element.getNamespaceURI(); //get the element's namespace URI
		//if this element is in the correct namespace
		if(Objects.equals(htmlNamespaceURI, namespaceURI)) {
			final String elementName = element.getLocalName(); //get the element's name
			if(elementName.equals(ELEMENT_A)) { //if this is an <a> element
				element.setAttributeNS(null, ELEMENT_A_ATTRIBUTE_HREF, href); //set the href attribute TODO what if they were using an attribute prefix?
			}
		}
	}

	//#metadata

	/**
	 * Finds the text contents of the {@code <html><head><title>} element in the XHTML namespace.
	 * @param document The XHTML document tree.
	 * @return The text content of the {@code <html><head><title>} element if it exists in the tree.
	 * @see HTML#XHTML_NAMESPACE_URI
	 * @see HTML#ELEMENT_TITLE
	 * @see Element#getTextContent()
	 */
	public static Optional<String> findTitle(@Nonnull final Document document) {
		return findHtmlHeadTitleElement(document).map(Element::getTextContent);
	}

	/**
	 * Finds the {@code <html><head><meta>} elements that have a <code>name</code>, returning the name and <code>content</code> as name-value pairs. Note that it
	 * is possible for a returned metadata name to be the empty string. Note also that while a named {@code <meta>} element is supposed to have a
	 * <code>content</code> attribute as per <a href="https://www.w3.org/TR/html52/document-metadata.html#the-meta-element"><cite>HTML 5.2 § 4.2.5. The meta
	 * element</cite></a>, because HTML documents may in practice not include a <code>content</code> attribute the returned name-value pairs may each have
	 * <code>null</code> as a value.
	 * <p>
	 * For convenience, because HTML metadata names are ASCII case-insensitive, the returned names are normalized to ASCII lowercase.
	 * </p>
	 * @implSpec This implementation delegates to {@link #namedMetadata(Document, BiFunction, BiFunction)}.
	 * @param document The XHTML document tree.
	 * @return A stream of name-value pairs representing the {@code <html><head><meta>} elements that contain <code>name</code> attributes.
	 * @see #htmlHeadMetaElements(Document)
	 */
	public static Stream<Map.Entry<String, String>> namedMetadata(@Nonnull final Document document) {
		return namedMetadata(document, (element, name) -> name, (element, value) -> value);
	}

	/**
	 * Finds the {@code <html><head><meta>} elements that have a <code>name</code>, returning the name and <code>content</code> as name-value pairs after applying
	 * some mapping. Note that it is possible for a provided metadata name to be the empty string. Note also that while a named {@code <meta>} element is supposed
	 * to have a <code>content</code> attribute as per <a href="https://www.w3.org/TR/html52/document-metadata.html#the-meta-element"><cite>HTML 5.2 § 4.2.5. The
	 * meta element</cite></a>, because HTML documents may in practice not include a <code>content</code> attribute the provided value may be <code>null</code>.
	 * @param document The XHTML document tree.
	 * @param nameMapper The function for mapping the string metadata names, in the context of some element, to metadata keys. The provided element will indicate
	 *          on which element the metadata was found. Because HTML metadata names are ASCII case-insensitive, the provided metadata name is normalized to ASCII
	 *          lowercase. It is possible for a metadata name to be the empty string, will never be <code>null</code>.
	 * @param valueMapper The function for mapping the string metadata values, in the context of some element, to metadata values. The provided element will
	 *          indicate on which element the metadata was found. The provided metadata value may be <code>null</code>.
	 * @return A stream of name-value pairs representing the {@code <html><head><meta>} elements that contain <code>name</code> attributes.
	 * @see #htmlHeadMetaElements(Document)
	 */
	public static <N, V> Stream<Map.Entry<N, V>> namedMetadata(@Nonnull final Document document, @Nonnull final BiFunction<Element, String, N> nameMapper,
			@Nonnull final BiFunction<Element, String, V> valueMapper) {
		return htmlHeadMetaElements(document).filter(metaElement -> metaElement.hasAttributeNS(null, ELEMENT_META_ATTRIBUTE_NAME))
				.map(metaElement -> new AbstractMap.SimpleImmutableEntry<>(
						nameMapper.apply(metaElement, ASCII.toLowerCase(metaElement.getAttributeNS(null, ELEMENT_META_ATTRIBUTE_NAME)).toString()), //normalize names
						valueMapper.apply(metaElement, findAttributeNS(metaElement, null, ELEMENT_META_ATTRIBUTE_CONTENT).orElse(null)))); //values can be null 
	}

	/**
	 * Sets named metadata of an HTML document based on the given map entry. If an existing {@code <html><head><meta>} element exist with the given ASCII
	 * case-insensitive <code>name</code>, its <code>content</code> attribute is updated to the given value (and its name is not changed); otherwise, a new
	 * metadata element is added with the given name and value.
	 * @param document The XHTML document tree.
	 * @param meta The name-value pair representing the metadata name and content to set.
	 * @return The {@code <html><head><meta>} element that was added or updated.
	 * @throws IllegalArgumentException if the given document has no {@code <html><head>} element.
	 * @see HTML#ELEMENT_META
	 * @see HTML#ELEMENT_META_ATTRIBUTE_NAME
	 * @see HTML#ELEMENT_META_ATTRIBUTE_CONTENT
	 * @see #setNamedMetata(Document, String, String)
	 * @throws IllegalArgumentException if the given name-value pair has no key name.
	 */
	public static Element setNamedMetata(@Nonnull Document document, @Nonnull Map.Entry<String, String> meta) {
		checkArgument(meta.getKey() != null, "No metadata name given.");
		return setNamedMetata(document, meta.getKey(), meta.getValue());
	}

	/**
	 * Sets named metadata of an HTML document. If an existing {@code <html><head><meta>} element exist with the given ASCII case-insensitive <code>name</code>,
	 * its <code>content</code> attribute is updated to the given value (and its name is not changed); otherwise, a new metadata element is added with the given
	 * name and value.
	 * @param document The XHTML document tree.
	 * @param metaName The name of the metadata to set.
	 * @param metaContent The metadata value to set.
	 * @return The {@code <html><head><meta>} element that was added or updated.
	 * @throws IllegalArgumentException if the given document has no {@code <html><head>} element.
	 * @see HTML#ELEMENT_META
	 * @see HTML#ELEMENT_META_ATTRIBUTE_NAME
	 * @see HTML#ELEMENT_META_ATTRIBUTE_CONTENT
	 */
	public static Element setNamedMetata(@Nonnull Document document, @Nonnull final String metaName, @Nonnull final String metaContent) {
		requireNonNull(metaName);
		requireNonNull(metaContent);
		final Element metaElement = findHtmlHeadMetaElementByName(document, metaName).orElseGet(() -> {
			final Element headElement = findHtmlHeadElement(document)
					.orElseThrow(() -> new IllegalArgumentException("Missing <html><head> element for adding child <meta> element."));
			final Element newMetaElement = addLast(headElement, document.createElementNS(XHTML_NAMESPACE_URI_STRING, ELEMENT_META)); //add a new <meta> element as the last child
			newMetaElement.setAttributeNS(null, ELEMENT_META_ATTRIBUTE_NAME, metaName); //initialize the name of the new <meta> element
			return newMetaElement;
		});
		metaElement.setAttributeNS(null, ELEMENT_META_ATTRIBUTE_CONTENT, metaContent);
		return metaElement;
	}

	/**
	 * Finds the first {@code <html><head><meta>} element with the given name. The meta name is matched in an ASCII case insensitive manner.
	 * @param document The XHTML document tree.
	 * @param metaName The name of the meta element to return.
	 * @return The first {@code <html><head><meta>} element with the given name, ASCII case insensitive, if found.
	 * @throws NullPointerException if the given document and/or name is <code>null</code>.
	 * @see HTML#ELEMENT_META_ATTRIBUTE_NAME
	 * @see ASCII#equalsIgnoreCase(CharSequence, CharSequence)
	 * @see <a href="https://www.w3.org/TR/html52/document-metadata.html#the-meta-element">HTML 5.2 § 4.2.5. The meta element</a>
	 */
	public static Optional<Element> findHtmlHeadMetaElementByName(@Nonnull final Document document, @Nonnull final String metaName) {
		requireNonNull(metaName);
		return htmlHeadMetaElements(document).filter(metaElement -> {
			final String metaElementName = metaElement.getAttributeNS(null, ELEMENT_META_ATTRIBUTE_NAME);
			return metaElementName != null && ASCII.equalsIgnoreCase(metaName, metaElementName);
		}).findFirst();
	}

	/**
	 * Finds the first XHTML {@code <html><head><meta>} element with the given name and returns its {@value HTML#ELEMENT_META_ATTRIBUTE_CONTENT} attribute. The
	 * meta name is matched in an ASCII case insensitive manner.
	 * @param document The XHTML document tree.
	 * @param metaName The name of the meta element to return.
	 * @return The {@value HTML#ELEMENT_META_ATTRIBUTE_CONTENT} attribute of the first {@code <html><head><meta>} element with the given name, ASCII case
	 *         insensitive.
	 * @throws NullPointerException if the given document and/or name is <code>null</code>.
	 * @see HTML#ELEMENT_META_ATTRIBUTE_NAME
	 * @see HTML#ELEMENT_META_ATTRIBUTE_CONTENT
	 * @see ASCII#equalsIgnoreCase(CharSequence, CharSequence)
	 * @see <a href="https://www.w3.org/TR/html52/document-metadata.html#the-meta-element">HTML 5.2 § 4.2.5. The meta element</a>
	 */
	public static Optional<String> findHtmlHeadMetaElementContent(@Nonnull final Document document, @Nonnull final String metaName) {
		return findHtmlHeadMetaElementByName(document, metaName).map(metaElement -> metaElement.getAttributeNS(null, ELEMENT_META_ATTRIBUTE_CONTENT))
				.filter(not(String::isEmpty)); //filter out the empty string, as getAttributeNS() is supposed to return an empty string for missing content
	}

	//#text

	/**
	 * Retrieves the text of the node contained in child nodes of type {@link Node#TEXT_NODE}, extracting text deeply.
	 * @apiNote This HTML-specific version adds whitespace to separate block elements.
	 * @param node The node from which text will be retrieved.
	 * @return The data of all <code>Text</code> descendant nodes, which may be the empty string.
	 * @see XmlDom#getText(Node, Set)
	 * @see HTML#BLOCK_ELEMENTS
	 */
	public static String getText(final Node node) { //TODO transfer to some higher-layer content manipulation class 
		return XmlDom.getText(node, BLOCK_ELEMENTS);
	}

	/**
	 * Retrieves the text of the node contained in child nodes of type <code>Node.Text</code>. If <code>deep</code> is set to <code>true</code> the text of all
	 * descendant nodes in document (depth-first) order; otherwise, only text of direct children will be returned.
	 * <p>
	 * This HTML-specific version adds whitespace to separate block elements.
	 * </p>
	 * @param node The node from which text will be retrieved.
	 * @param stringBuilder The buffer to which text will be added.
	 * @see XmlDom#getText(Node, Set, boolean, StringBuilder)
	 * @see HTML#BLOCK_ELEMENTS
	 */
	public static void getText(final Node node, final StringBuilder stringBuilder) { //TODO transfer to some higher-layer content manipulation class
		XmlDom.getText(node, BLOCK_ELEMENTS, true, stringBuilder);
	}

}
