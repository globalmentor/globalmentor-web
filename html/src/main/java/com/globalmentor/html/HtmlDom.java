/*
 * Copyright © 1996-2012 GlobalMentor, Inc. <https://www.globalmentor.com/>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.globalmentor.html;

import static java.util.Collections.*;
import static java.util.Objects.*;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import javax.annotation.*;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.*;

import org.w3c.dom.*;

import com.globalmentor.io.*;

import com.globalmentor.model.ConfiguredStateException;
import com.globalmentor.net.MediaType;
import com.globalmentor.text.ASCII;
import com.globalmentor.vocab.VocabularyRegistry;
import com.globalmentor.html.def.HTML;
import com.globalmentor.xml.VocabularyNamespaceContext;
import com.globalmentor.xml.XmlDom;

import io.clogr.Clogr;

import static com.globalmentor.text.ASCII.*;
import static com.globalmentor.html.def.HTML.*;
import static com.globalmentor.java.Characters.SPACE_CHAR;
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
	 * A namespace context suitable for use with XPath expressions with HTML, mapping the (X)HTML namespace to {@value HTML#HTML_NAMESPACE_PREFIX} (as Java's
	 * XPath implementation doesn't allow for a default namespace).
	 * @apiNote If the default mapping is insufficient and/or uses a different namespace prefix than desired, create a {@link VocabularyNamespaceContext} using a
	 *          custom configured {@link VocabularyRegistry}.
	 * @see javax.xml.xpath.XPath#setNamespaceContext(NamespaceContext)
	 * @see HTML#HTML_NAMESPACE_PREFIX
	 * @see HTML#XHTML_NAMESPACE_URI
	 */
	public static NamespaceContext DEFAULT_XPATH_NAMESPACE_CONTEXT = new VocabularyNamespaceContext(
			VocabularyRegistry.builder().registerPrefix(HTML_NAMESPACE_PREFIX, XHTML_NAMESPACE_URI).build());

	/**
	 * Creates a new unformatted default XHTML document with the required minimal structure, {@code <html><head><title></title<head><body></html>}, with no
	 * document type.
	 * @param title The title of the document.
	 * @return A newly created default generic XHTML document with the required minimal structure.
	 * @throws NullPointerException if the given title is <code>null</code>.
	 * @throws ConfiguredStateException if a document builder cannot be created which satisfies the configuration requested.
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
	 * @throws ConfiguredStateException if a document builder cannot be created which satisfies the configuration requested.
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
	 * @throws ConfiguredStateException if a document builder cannot be created which satisfies the configuration requested.
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
	 * @throws ConfiguredStateException if a document builder cannot be created which satisfies the configuration requested.
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
	 * @throws ConfiguredStateException if a document builder cannot be created which satisfies the configuration requested.
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
		final Document document = domImplementation.createDocument(XHTML_NAMESPACE_URI_STRING, ELEMENT_HTML, documentType); //create an XHTML document
		//TODO check about whether we need to add a <head> and <title>
		final Element htmlElement = document.getDocumentElement(); //get the html element
		if(formatted) { //if we should format the document
			appendText(htmlElement, "\n"); //append a newline to start the content of the html element
		}
		final Element headElement = document.createElementNS(XHTML_NAMESPACE_URI_STRING, ELEMENT_HEAD); //create the head element
		htmlElement.appendChild(headElement); //add the head element to the html element
		if(formatted) { //if we should format the document
			appendText(headElement, "\n"); //append a newline to start the content in the head
			appendText(htmlElement, "\n"); //append a newline to separate the content of the html element
		}
		final Element titleElement = document.createElementNS(XHTML_NAMESPACE_URI_STRING, ELEMENT_TITLE); //create the title element
		headElement.appendChild(titleElement); //add the title element to the head element
		appendText(titleElement, title); //append the title text to the title element 
		if(formatted) { //if we should format the document
			appendText(headElement, "\n"); //append a newline to separate the informtaion after the title
		}
		final Element bodyElement = document.createElementNS(XHTML_NAMESPACE_URI_STRING, ELEMENT_BODY); //create the body element
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
	 * @see HTML#XHTML_NAMESPACE_URI_STRING
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
	 * Determines the media type indicated by the given link element.
	 * @param element The element, presumably a link ({@code <a>}, {@code <area>}, or {@code <link>}).
	 * @return The content type specified by the link element, which will not be present if no type was specified or the media type was not syntactically correct.
	 * @see HTML#LINK_ATTRIBUTE_TYPE
	 * @see <a href="http://www.w3.org/TR/html5/links.html#attr-hyperlink-type">HTML5 Link MIME type</a>
	 */
	public static Optional<MediaType> findLinkType(@Nonnull final Element element) {
		return findAttributeNS(element, null, LINK_ATTRIBUTE_TYPE).flatMap(link -> { //if there is a type specified
			try {
				return Optional.of(MediaType.parse(link)); //parse the content type and return it
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
	 * @implNote This implementation considers only the most common image filename extensions.
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
					final MediaType mediaType = MediaType.parse(type); //create a media type from the given type
					//TODO catch possible exception here
					if(mediaType.getPrimaryType().equals(MediaType.IMAGE_PRIMARY_TYPE)) //if this is an image
						return true; //show that this is an image object
				}
				//see if there is a data attribute, since there is no type specified
				if(element.hasAttributeNS(null, ELEMENT_OBJECT_ATTRIBUTE_DATA)) { //if there is a data attribute
					final String data = element.getAttributeNS(null, ELEMENT_OBJECT_ATTRIBUTE_DATA); //get the data
					if(Filenames.findExtension(data).map(Images.MEDIA_TYPES_BY_FILENAME_EXTENSION::containsKey).orElse(false)) //if the filename has an image extension
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
	 * @param <N> The type the metadata name will be mapped to.
	 * @param <V> The type the metadata value will be mapped to.
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
	 * @see #setNamedMetadata(Document, String, String)
	 * @throws IllegalArgumentException if the given name-value pair has no key name.
	 */
	public static Element setNamedMetadata(@Nonnull Document document, @Nonnull Map.Entry<String, String> meta) {
		checkArgument(meta.getKey() != null, "No metadata name given.");
		return setNamedMetadata(document, meta.getKey(), meta.getValue());
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
	public static Element setNamedMetadata(@Nonnull Document document, @Nonnull final String metaName, @Nonnull final String metaContent) {
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
	 * Adds named metadata of an HTML document element.
	 * @param headElement The parent element on which to set metadata; normally the {@code <head>} element.
	 * @param metaName The name of the metadata to add.
	 * @param metaContent The metadata value to add.
	 * @return The {@code <meta>} element that was added.
	 * @see HTML#ELEMENT_META
	 * @see HTML#ELEMENT_META_ATTRIBUTE_NAME
	 * @see HTML#ELEMENT_META_ATTRIBUTE_CONTENT
	 */
	public static Element addNamedMetadata(@Nonnull Element headElement, @Nonnull final String metaName, @Nonnull final String metaContent) {
		requireNonNull(metaName);
		requireNonNull(metaContent);
		final Element metaElement = addLast(headElement, headElement.getOwnerDocument().createElementNS(XHTML_NAMESPACE_URI_STRING, ELEMENT_META));
		metaElement.setAttributeNS(null, ELEMENT_META_ATTRIBUTE_NAME, metaName);
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
	 * meta name is matched in an ASCII case insensitive manner. If a metadata element has a name but no content attribute, an empty string is returned as per the
	 * HTML specification.
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
		//getAttributeNS() is supposed to return an empty string for missing content, which matches the HTML default "" for no specified content
		return findHtmlHeadMetaElementByName(document, metaName).map(metaElement -> metaElement.getAttributeNS(null, ELEMENT_META_ATTRIBUTE_CONTENT));
	}

	//#attributes

	/**
	 * Merges the attributes of some element into the target element in an HTML-aware manner. If an attribute exists in the other element, its value will replace
	 * the value, if any, in the target element. Any target element attributes not present in the other element will remain. If the target element is an HTML
	 * element, the {@value HTML#ATTRIBUTE_CLASS} attribute, if any, will be merged by the individual class names, not by the entire attribute value.
	 * @apiNote This method functions similarly to {@link XmlDom#mergeAttributesNS(Element, Element)} except that the {@value HTML#ATTRIBUTE_CLASS} attribute is
	 *          treated specially.
	 * @implNote Any attribute value set or updated by this method will use the namespace prefix of the other element, which means that even if the target element
	 *           contains an attribute with the same value, its namespace prefix may change. Although the namespace URI is guaranteed to be correct, no checks are
	 *           performed to ensure that the target document has defined the new namespace prefix, if any.
	 * @param targetElement The element into which the attributes will be merged.
	 * @param element The element the attributes of which will be merged into the target element.
	 * @see Element#setAttributeNS(String, String, String)
	 * @see HTML#ATTRIBUTE_CLASS
	 */
	public static void mergeAttributes(@Nonnull final Element targetElement, @Nonnull final Element element) {
		final boolean isTargetHtml = XHTML_NAMESPACE_URI_STRING.equals(targetElement.getNamespaceURI());
		Stream<Attr> attributes = attributesOf(element);
		if(isTargetHtml) { //skip the class attribute if this is HTML, to treat it specially later
			attributes = attributes.filter(attr -> !attr.getLocalName().equals(ATTRIBUTE_CLASS) || attr.getNamespaceURI() != null);
		}
		mergeAttributesNS(targetElement, attributes);
		if(isTargetHtml) { //for HTML, merge the contents of the class attribute as individual class name tokens
			final Set<String> targetClasses = getClasses(targetElement);
			final Set<String> classes = getClasses(element);
			if(!targetClasses.isEmpty() || !classes.isEmpty()) { //if there are no classes on either side, there is nothing to merge
				final Set<String> mergedClasses = new LinkedHashSet<>(targetClasses); //maintain order for aesthetics
				mergedClasses.addAll(classes);
				targetElement.setAttributeNS(null, ATTRIBUTE_CLASS, String.join(String.valueOf(SPACE_CHAR), mergedClasses));
			}
		}
	}

	//##class

	/**
	 * Retrieves all class names, the tokens that appear in the <code>class</code> attribute. If there is no HTML <code>class</code> attribute (that is, no a
	 * <code>class</code> attribute appearing with no namespace), an empty set is returned.
	 * @apiNote The HTML specifications indicates that <code>class</code> attribute should only be interpreted as a set of space-separated tokens when specified
	 *          on an HTML element, but this method makes no checks of whether the given element is in the XHTML namespace or not.
	 * @implSpec The returned set will maintain the order of the class names.
	 * @param element The element on which a <code>class</code> attribute may be defined.
	 * @return The set of class name tokens in the <code>class</code> attribute, if any, of the given element.
	 * @see <a href="https://www.w3.org/TR/html52/dom.html#element-attrdef-global-class">HTML global class attribute</a>
	 * @see HTML#ATTRIBUTE_CLASS
	 * @see HTML#SPACE_CHARACTERS
	 */
	public static Set<String> getClasses(@Nonnull final Element element) {
		if(!element.hasAttributeNS(null, ATTRIBUTE_CLASS)) {
			return emptySet();
		}
		return new LinkedHashSet<>(SPACE_CHARACTERS.split(element.getAttributeNS(null, ATTRIBUTE_CLASS)));
	}

}
