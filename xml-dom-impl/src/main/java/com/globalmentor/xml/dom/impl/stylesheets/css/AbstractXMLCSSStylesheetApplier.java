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

package com.globalmentor.xml.dom.impl.stylesheets.css;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import static com.globalmentor.html.spec.HTML.*;
import static com.globalmentor.xml.spec.XML.*;
import static com.globalmentor.xml.spec.XMLStyleSheets.*;

import com.globalmentor.html.spec.HTML;
import com.globalmentor.io.*;
import com.globalmentor.model.NameValuePair;
import com.globalmentor.net.ContentType;
import com.globalmentor.net.URIs;
import com.globalmentor.xml.XmlDom;
import com.globalmentor.xml.dom.impl.stylesheets.XMLStyleSheetDescriptor;

import io.clogr.Clogged;

import org.w3c.dom.css.*;

/**
 * Applies styles to XML elements.
 * @param <D> The document type.
 * @param <E> The element type.
 * @author Garret Wilson
 * @deprecated
 */
public abstract class AbstractXMLCSSStylesheetApplier<D, E> implements URIInputStreamable, Clogged {

	/**
	 * Determeins the base URI of the given document.
	 * @param document The document for which a base URI should be returned.
	 * @return The base URI of the document, or <code>null</code> if the base URI of the document is not known.
	 */
	//TODO del if not needed	protected abstract URI getDocumentBaseURI(final Object document);

	/**
	 * Returns the object that represents the root element of the given document.
	 * @param document The object representing the XML document.
	 * @return The object representing the root element of the XML document.
	 */
	protected abstract E getDocumentElement(final D document);

	/**
	 * Retrieves processing instructions from the given document.
	 * @param document The document that might contain XML processing instructions.
	 * @return A non-<code>null</code> array of name-value pairs representing processing instructions.
	 */
	protected abstract NameValuePair[] getDocumentProcessingInstructions(final D document);

	/**
	 * Retrieves the namespace URI of the given element.
	 * @param element The element for which the namespace URI should be returned.
	 * @return The namespace URI of the given element.
	 */
	protected abstract String getElementNamespaceURI(final E element);

	/**
	 * Retrieves the local name of the given element.
	 * @param element The element for which the local name should be returned.
	 * @return The local name of the given element.
	 */
	protected abstract String getElementLocalName(final E element);

	/**
	 * Retrieves the value of one of the element's attributes.
	 * @param element The element owner of the attributes.
	 * @param attributeNamespaceURI The namespace of the attribute to find.
	 * @param attributeLocalName The local name of the attribute to find.
	 * @return The value of the specified attribute, or <code>null</code> if there is no such attribute.
	 */
	protected abstract String getElementAttributeValue(final E element, final String attributeNamespaceURI, final String attributeLocalName);

	/**
	 * Retrieves the parent element for the given element.
	 * @param element The element for which a parent should be found.
	 * @return The element's parent, or <code>null</code> if no parent could be found.
	 */
	protected abstract E getParentElement(final E element);

	/**
	 * Determines the number of child nodes the given element has.
	 * @param element The parent element.
	 * @return The number of child nodes this element has.
	 */
	protected abstract int getChildCount(final E element);

	/**
	 * Determines if the given indexed child of an element is an element.
	 * @param element The parent element.
	 * @param index The zero-based index of the child.
	 * @return <code>true</code> if the the child of the element at the given index is an element.
	 */
	protected abstract boolean isChildElement(final E element, final int index);

	/**
	 * Retrieves the given indexed child of an element.
	 * @param element The parent element.
	 * @param index The zero-based index of the child.
	 * @return The child of the element at the given index.
	 */
	protected abstract E getChildElement(final E element, final int index);

	/**
	 * Retrieves all child text of the given element.
	 * @param element The element for which text should be returned.
	 * @return The text content of the element.
	 */
	protected abstract String getElementText(final E element);

	/**
	 * Imports style information into that already gathered for the given element.
	 * @param element The element for which style information should be imported
	 * @param cssStyle The style information to import.
	 */
	protected abstract void importCSSStyle(final E element, final CSSStyleDeclaration cssStyle);

	/**
	 * Makes copies of the properties in the second style declaration and adds them to the first style, replacing any properties with the same name that may
	 * already exist.
	 * @param styleDeclaration The style declaration to receive the new style information.
	 * @param importStyleDeclaration The style to import.
	 */
	public static void importStyle(final CSSStyleDeclaration styleDeclaration, final CSSStyleDeclaration importStyleDeclaration) {
		for(int i = 0; i < importStyleDeclaration.getLength(); ++i) { //look at each of the properties in the style to import
			final String propertyName = importStyleDeclaration.item(i); //get the name of this property
			/*TODO fix to use generic CSS DOM routines, rather than custom classes
						final CSSValue cssValue=importStyleDeclaration.getPropertyCSSValue(proprtyName);	//get the value for this property
						styleDeclaration.setProperty(propertyName, cssValue.getCssText(), );			
			*/
			((XMLCSSStyleDeclaration)styleDeclaration).setPropertyCSSValue(propertyName, importStyleDeclaration.getPropertyCSSValue(propertyName)); //get this property value from the style declaration, and set it in our style declaration, replacing any value(s) already set TODO do a clone() here before we set the value
		}
	}

	/**
	 * Finds and loads all stylesheets referenced in the document. The external stylesheets are:
	 * <ul>
	 * <li>External stylesheets referenced from XML processing instructions.</li>
	 * <li>External stylesheets referenced from HTML/OEB link elements.</li>
	 * <li>Internal stylesheets in the HTML/OEB style element.</li>
	 * </ul>
	 * @param document The document that references the stylesheets.
	 * @param baseURI The base URI of the document, or <code>null</code> if the base URI is not known.
	 * @param mediaType The media type of the document, or <code>null</code>. if the media type is unknown.
	 * @return An array of stylesheets.
	 */
	public CSSStyleSheet[] getStylesheets(final D document, final URI baseURI,
			final ContentType mediaType/*TODO convert to something general, final RDFResource description*/) {
		final List<CSSStyleSheet> styleSheetList = new ArrayList<CSSStyleSheet>(); //create a new list to hold the stylesheets
		//get all default stylesheets
		final String[] namespaceURIArray = getNamespaceURIs(document, mediaType/*TODO fix, description*/); //get all namespaces used in this document
		for(int i = 0; i < namespaceURIArray.length; ++i) { //look at each namespace
			final String namespaceURI = namespaceURIArray[i]; //get this namespace
			final CSSStyleSheet cssStyleSheet = XMLCSS.getDefaultStyleSheet(namespaceURI); //get the default stylesheet for the given namespace
			if(cssStyleSheet != null) { //if we know about a default stylesheet for this namespace
				styleSheetList.add(cssStyleSheet); //add the default stylesheet to the list
			}
		}
		//gather all stylesheet links
		final XMLCSSProcessor cssProcessor = new XMLCSSProcessor(this); //create a new CSS processor for parsing all the stylesheets, showing that we'll worry about getting the needed input streams
		//gather all the references to external stylesheets
		final XMLStyleSheetDescriptor[] styleSheetDescriptorArray = getStylesheetDescriptors(document/*TODO convert to something general, description*/);
		if(styleSheetDescriptorArray.length > 0) { //if there are stylesheet descriptors
			for(int i = 0; i < styleSheetDescriptorArray.length; ++i) { //look at each stylesheet descriptor
				final XMLStyleSheetDescriptor styleSheetDescriptor = styleSheetDescriptorArray[i]; //get the next descriptor
				assert styleSheetDescriptor.getHRef() != null : "Stylesheet processing instruction has null href"; //TODO fix
				//TODO do whatever we need to do for the media type, title, etc.
				try {
					//create a URI from the original URI of the XML document and the stylesheet href
					final URI styleSheetURI = URIs.createURI(baseURI, styleSheetDescriptor.getHRef());
					//buffer the input stream so that the BOMInputStreamReader will be able to use mark/reset
					final Reader styleSheetReader = new BOMInputStreamReader(new BufferedInputStream(getInputStream(styleSheetURI))); //get an input stream to the stylesheet TODO do a preread check for the @charset "" CSS keyword 
					try {
						final CSSStyleSheet cssStyleSheet = cssProcessor.parseStyleSheet(styleSheetReader, styleSheetURI); //parse the stylesheet
						styleSheetList.add(cssStyleSheet); //add this stylesheet to our list
					} finally {
						styleSheetReader.close(); //always close the stylesheet reader
					}
				} catch(IOException ioException) { //if there are any I/O exceptions
					getLogger().warn(ioException.getMessage(), ioException); //TODO fix better
				} catch(URISyntaxException uriSyntaxException) { //if there are any URI syntax errors
					getLogger().warn(uriSyntaxException.getMessage(), uriSyntaxException); //TODO fix better
				}
			}
		}
		//process and gather any internal stylesheets TODO right now, this is very HTML/OEB specific; fix to check the namespace or something
		final E documentElement = getDocumentElement(document); //get the document element
		final int childCount = getChildCount(documentElement); //find out how many direct children are in the document
		for(int childIndex = 0; childIndex < childCount; ++childIndex) { //look at the direct children of the document element
			if(isChildElement(documentElement, childIndex)) { //if this child is an element
				final E childElement = getChildElement(documentElement, childIndex); //get a reference to the child element
				final String childElementLocalName = getElementLocalName(childElement); //get the child element local name
				//if this element is <head> and it's an HTML <head>
				if(ELEMENT_HEAD.equals(childElementLocalName)) { //TODO fix checking to see if this is HTML					&& XHTMLSwingTextUtilities.isHTMLElement(childAttributeSet, documentAttributeSet))
					getInternalStylesheets(childElement, cssProcessor, styleSheetList); //get the internal stylesheets from the HTML <head> element
				}
			}
		}
		return (CSSStyleSheet[])styleSheetList.toArray(new CSSStyleSheet[styleSheetList.size()]); //return an array of stylesheets we collected
	}

	/**
	 * Finds and parses any internal stylesheets contained in the given element or its children. All stylesheets found in the document will be added to the list
	 * in the order encoundered in document order.
	 * @param element The XML element to check.
	 * @param cssProcessor The processor that processes CSS stylesheets.
	 * @param styleSheetList The list to which stylesheets should be added.
	 */
	protected void getInternalStylesheets(final E element, final XMLCSSProcessor cssProcessor, final List<CSSStyleSheet> styleSheetList) {
		final String elementLocalName = getElementLocalName(element); //get the element's local name
		//if this is an HTML <style>
		if(ELEMENT_STYLE.equals(elementLocalName)) { //TODO fix checking to see if this is HTML				&& XHTMLSwingTextUtilities.isHTMLElement(attributeSet, swingDocumentElement.getAttributes()))
			try {
				final String styleText = getElementText(element); //get the text of the style element
				final String descriptionString = "Internal Style Sheet"; //create a string to describe the internal style sheet TODO i18n
				final CSSStyleSheet stylesheet = cssProcessor.parseStyleSheet(styleText, descriptionString); //parse the stylesheet
				styleSheetList.add(stylesheet); //add this stylesheet to the list
			} catch(IOException ioException) { //if there are any I/O exceptions
				getLogger().warn(ioException.getMessage(), ioException); //TODO fix better
			}
		} else { //if this is not a style element
			final int childCount = getChildCount(element); //find out how many child elements there are
			for(int childIndex = 0; childIndex < childCount; ++childIndex) { //look at each child element
				if(isChildElement(element, childIndex)) { //if this child is an element
					final E childElement = getChildElement(element, childIndex); //get this child element
					getInternalStylesheets(childElement, cssProcessor, styleSheetList); //get internal stylesheets contained in this element
				}
			}
		}
	}

	/**
	 * Determines the default XML namespace for the given MIME content type.
	 * @param mediaType The media type for which a default namespace should be found, or <code>null</code> if the media type is not known.
	 * @return The default XML namespace URI used by resources of the given content type, or <code>null</code> if there is no default namespace URI or the default
	 *         namespace URI is not known.
	 */
	public static URI getDefaultNamespaceURI(final ContentType mediaType) {
		if(mediaType != null) { //if we were given a valid media type
			if(HTML.isHTML(mediaType)) //if this is one of the HTML media types
				return HTML.XHTML_NAMESPACE_URI; //return the XHTML media type
			/*TODO fix for OEB; create some sort of registration facility
			else if(mediaType.match(ContentType.TEXT_PRIMARY_TYPE, OEB.X_OEB1_DOCUMENT_SUBTYPE)) //if this is an OEB 1.x document
				return OEB.OEB1_DOCUMENT_NAMESPACE_URI; //return the OEB 1.x document namespace
			*/
		}
		return null; //show that we can't find a default namespace URI
	}

	/**
	 * Finds the namespaces used by the document.
	 * <p>
	 * This implementation gathers namespaces from the following locations:
	 * </p>
	 * <ul>
	 * <li>The namespace used by the document element, if any.</li>
	 * <li>The default namespace for the given content type, if any. For example, a document with a content type of <code>text/html</code> will include the
	 * namespace <code>http://www.w3.org/1999/xhtml</code>.</li>
	 * <li>The XPackage <code>x:namespace</code> namespaces listed in the description, if any.</li>
	 * </ul>
	 * @param document The document that contains the namespaces.
	 * @param mediaType The media type of the document, or <code>null</code> if the media type is unknown.
	 * @return A non-<code>null</code> array of namespace URIs.
	 */
	protected String[] getNamespaceURIs(final D document, final ContentType mediaType/*TODO convert to something general, final RDFResource description*/) { //TODO fix to actually look through all the namespaces, maybe, but that could be intensive -- on the other hand, a subclass could get that information from the OEB package, overriding the intensive part
		final Set<String> namespaceURISet = new HashSet<String>(); //create a set of namespaces
		final E documentElement = getDocumentElement(document); //get the root element of the document
		final String documentElementNamespaceURI = getElementNamespaceURI(documentElement); //get the document element namespace URI
		if(documentElementNamespaceURI != null) { //if the document element has a namespace
			namespaceURISet.add(documentElementNamespaceURI); //add the document element namespace to the namespace set
		}
		final URI mediaTypeNamespaceURI = getDefaultNamespaceURI(mediaType); //see if we can find a default namespace for the media type
		if(mediaTypeNamespaceURI != null) { //if we found a namespace for the media type
			namespaceURISet.add(mediaTypeNamespaceURI.toString()); //add the content type namespace to the namespace set
		}
		return namespaceURISet.toArray(new String[namespaceURISet.size()]); //return an array of the namespace URIs in the set we filled
	}

	/**
	 * Retrieves an array of all referenced stylesheets in the document by href. The stylesheet descriptors gathered reference:
	 * <ul>
	 * <li>External stylesheets referenced from XML processing instructions.</li>
	 * <li>External stylesheets referenced from HTML/OEB link elements.</li>
	 * </ul>
	 * @param document The document in which the descriptors exist.
	 * @return An array of style sheet descriptors, each referencing a stylesheet.
	 */
	protected XMLStyleSheetDescriptor[] getStylesheetDescriptors(final D document/*TODO convert to something general, final RDFResource description*/) {
		final List<XMLStyleSheetDescriptor> styleSheetDescriptorList = new ArrayList<XMLStyleSheetDescriptor>(); //create a list to hold stylesheet descriptors
		//gather all XML processing instructions stylesheet links
		//find stylesheet references from processing instructions
		final NameValuePair[] processingInstructions = getDocumentProcessingInstructions(document); //get the processing instructions, if any (this will never return null)
		for(int processingInstructionIndex = 0; processingInstructionIndex < processingInstructions.length; ++processingInstructionIndex) { //look at each processing instruction
			final NameValuePair processingInstruction = processingInstructions[processingInstructionIndex]; //get this processing instruction's values
			//if this is a stylesheet processing instruction
			if(XML_STYLESHEET_PROCESSING_INSTRUCTION.equals(processingInstruction.getName())) {
				final String processingInstructionData = (String)processingInstruction.getValue(); //get the processing instruction's data, assuming it's a string
				//TODO check the media type, etc. here
				//get the href pseudo-attribute, if it is defined
				final String href = getProcessingInstructionPseudoAttributeValue(processingInstructionData, HREF_ATTRIBUTE);
				if(href != null) { //if there is an href
					final XMLStyleSheetDescriptor styleSheetDescriptor = new XMLStyleSheetDescriptor(href); //create a new descriptor for this stylesheet TODO fix for media type, title, etc.
					styleSheetDescriptorList.add(styleSheetDescriptor); //add the stylesheet descriptor to our list
				}
			}
		}
		//get all linked stylesheets in an HTML document
		final E documentElement = getDocumentElement(document); //get the document element
		for(int childIndex = getChildCount(documentElement) - 1; childIndex >= 0; --childIndex) { //look at each child, starting from the last to the first because order doesn't matter
			if(isChildElement(documentElement, childIndex)) { //if this child is an element
				final E childElement = getChildElement(documentElement, childIndex); //get this child
				if(ELEMENT_HEAD.equals(getElementLocalName(childElement))) { //if this is the HTML <head> element TODO ensure this is HTML
					for(int headIndex = getChildCount(childElement) - 1; headIndex >= 0; --headIndex) { //look at each child, starting from the last to the first because order doesn't matter
						if(isChildElement(childElement, headIndex)) { //if this head child is an element
							final E headElement = getChildElement(childElement, headIndex); //get this head child
							if(ELEMENT_LINK.equals(getElementLocalName(headElement))) { //if this is the HTML <head><link> element TODO ensure this is HTML
								if(LINK_REL_STYLESHEET.equals(getElementAttributeValue(headElement, null, ELEMENT_LINK_ATTRIBUTE_REL))) { //if this is link rel="stylesheet"
									//TODO check the media type, etc. here
									//get the href attribute, if it is defined
									final String href = getElementAttributeValue(headElement, null, ELEMENT_LINK_ATTRIBUTE_HREF);
									if(href != null) { //if there is an href
										final XMLStyleSheetDescriptor styleSheetDescriptor = new XMLStyleSheetDescriptor(href); //create a new descriptor for this stylesheet TODO fix for media type, title, etc.
										styleSheetDescriptorList.add(styleSheetDescriptor); //add the stylesheet descriptor to our list
									}
								}
							}
						}
					}
				}
			}
		}
		return (XMLStyleSheetDescriptor[])styleSheetDescriptorList.toArray(new XMLStyleSheetDescriptor[styleSheetDescriptorList.size()]); //convert the stylesheet descriptors to an array and return them
	}

	/**
	 * Determines the style of each element in the given element and its children based on the given stylesheet.
	 * @param styleSheet The stylesheet to apply to the element.
	 * @param element The element to which styles should be applied, along with its children.
	 */
	public void applyStyleSheet(final CSSStyleSheet styleSheet, final E element) {
		final String elementLocalName = getElementLocalName(element); //get the element's local name for quick lookup
		final CSSRuleList cssRuleList = styleSheet.getCssRules(); //get the list of CSS rules
		for(int ruleIndex = 0; ruleIndex < cssRuleList.getLength(); ++ruleIndex) { //look at each of our rules
			if(cssRuleList.item(ruleIndex).getType() == CSSRule.STYLE_RULE) { //if this is a style rule TODO fix for other rule types
				final CSSStyleRule cssStyleRule = (CSSStyleRule)cssRuleList.item(ruleIndex); //get a reference to this style rule in the stylesheet
				if(isApplicable(cssStyleRule, element, elementLocalName)) { //if this style rule applies to this element
					importCSSStyle(element, cssStyleRule.getStyle()); //import this style into whatever style we've collected so far for this element, if any
				}
			}
		}
		//apply the stylesheet to the child elements
		for(int childIndex = getChildCount(element) - 1; childIndex >= 0; --childIndex) { //look at each child, starting from the last to the first because order doesn't matter
			if(isChildElement(element, childIndex)) { //if this child is an element
				final E childElement = getChildElement(element, childIndex); //get this child
				applyStyleSheet(styleSheet, childElement); //apply this stylesheet to the child element
				/*TODO fix for Swing stylesheet applier
							if(!AbstractDocument.ContentElementName.equals(childSwingElement.getName())) {	//if this isn't content (content never gets styled)
								applyStyleSheet(styleSheet, childSwingElement); //apply this stylesheet to the child element
							}
				*/
			}
		}
	}

	/**
	 * Checks the given element and all sub-elements and applies any locally-defined styles.
	 * @param element The element to which styles should be applied, along with its children.
	 */
	/*TODO del
		public void applyLocalStyles(final E element)
		{
			//apply local styles using a new CSS processor for parsing all the stylesheets, showing that we'll worry about getting the needed input streams
			applyLocalStyles(element, new XMLCSSProcessor(this));
		}
	*/

	/**
	 * Checks the given element and all sub-elements and applies any locally-defined styles.
	 * @param element The element to which styles should be applied, along with its children.
	 */
	public void applyLocalStyles(final E element) {
		final String styleValue = getElementAttributeValue(element, null, ATTRIBUTE_STYLE); //get the value of the style attribute TODO later check to make sure this element is in an XHTML namespace
		if(styleValue != null && styleValue.length() != 0) { //if there is a style value
			final XMLCSSStyleDeclaration style = new XMLCSSStyleDeclaration(); //create a new style declaration
			try {
				final ParseReader styleReader = new ParseReader(styleValue, "Element " + getElementLocalName(element) + " Local Style"); //create a string reader from the value of this local style attribute TODO i18n
				XMLCSSProcessor.parseRuleSet(styleReader, style); //read the style into our style declaration
				importCSSStyle(element, style); //import this style into whatever style we've collected so far for this element, if any
			} catch(final IOException ioException) { //if we have any errors reading the style
				getLogger().warn(ioException.getMessage(), ioException); //warn that we don't understand the style
			}
		}
		//apply local stylesheets to the child elements
		for(int childIndex = getChildCount(element) - 1; childIndex >= 0; --childIndex) { //look at each child, starting from the last to the first because order doesn't matter
			if(isChildElement(element, childIndex)) { //if this child is an element
				final E childElement = getChildElement(element, childIndex); //get this child
				applyLocalStyles(childElement); //apply local styles to the child element
			}
		}
	}

	/**
	 * Determines whether, based upon this style rule's selectors, the given style applies to the specified element.
	 * @param cssStyleRule The style rule to check against.
	 * @param element The element this style might apply to.
	 * @param elementLocalName The element's local name for quick lookup.
	 * @return <code>true</code> if the given style applies to the specified element.
	 */
	protected boolean isApplicable(final CSSStyleRule cssStyleRule, final E element, final String elementLocalName) {
		final XMLCSSStyleRule xmlCSSStyleRule = (XMLCSSStyleRule)cssStyleRule; //TODO fix; right now we use special features of our CSS DOM implementation -- fix to use just the normal CSS DOM
		for(int selectorIndex = 0; selectorIndex < xmlCSSStyleRule.getSelectorArrayList().size(); ++selectorIndex) { //look at each selector array
			final XMLCSSSelector[] contextArray = (XMLCSSSelector[])xmlCSSStyleRule.getSelectorArrayList().get(selectorIndex); //get a reference to this array of selectors
			if(isApplicable(contextArray, element, elementLocalName)) { //if this context array applies to the element
				return true; //we don't need to check the others; we've found a match
			}
		}
		return false; //if none of our array of contextual selectors match, show that this style rule doesn't apply to this element
	}

	/**
	 * Determines whether this contextual selector (represented by an array of selectors) applies to the specified element.
	 * @param contextArray The array of nested contexts to compare to the element hierarchy.
	 * @param element The element this context array might apply to.
	 * @param elementLocalName The element's local name for quick lookup.
	 * @return <code>true</code> if the given styles applies to the specified element.
	 */
	protected boolean isApplicable(final XMLCSSSelector[] contextArray, E element, String elementLocalName) { //TODO this method may be modified or go away when we fully switch to the DOM
		//first see if we can do a quick comparison on the most common type of selector: name-based selectors
		if(contextArray.length == 1) { //if there is only one context in the array
			final XMLCSSSelector selectorContext = contextArray[0]; //get the only context of the selector
			//if this context only looks at the tag name
			if(selectorContext.getTagName().length() > 0 && selectorContext.getTagClass().length() == 0) { //TODO fix for CSS2 and CSS3
				return selectorContext.getTagName().equals(elementLocalName); //compare tag names here without going on to anything more complicated
			}
		}
		for(int contextIndex = contextArray.length - 1; contextIndex >= 0; --contextIndex) { //look at each context for this selector, working from the last one (applying to this element) to the first one (applying to an ancestor of this element)
			final XMLCSSSelector selectorContext = contextArray[contextIndex]; //get this context of the selector
			if(!isApplicable(selectorContext, element, elementLocalName)) //if this selector context does not apply to this element
				return false; //this entire contextual selector does not apply
			if(contextIndex > 0) { //if we're still working our way up the chain
				element = getParentElement(element); //get this element's parent
				if(element != null) { //if there is a parent
					elementLocalName = getElementLocalName(element); //update the local name of the element
				} else { //if this element does not have a parent
					return false; //since we're not at the top of the context chain, this element can't match since it has no parents to compare
				}
			}
		}
		return true; //if we make it here, we've worked our way up the selector context chain and every element along the way has applied to the appropriate selector
	}

	/**
	 * Determines whether the given selector applies to the specified element. Currently this method only checks the selector's tag name and the class, in both
	 * instances ignoring the element's namespace.
	 * @param selectorContext The description of this stop in the selector context path.
	 * @param element The element whose name and class should match those contained in this selector.
	 * @param elementLocalName The element's local name for quick lookup.
	 * @return <code>true</code> if the given style applies to the specified element.
	 */
	protected boolean isApplicable(final XMLCSSSelector selectorContext, final E element, final String elementLocalName) { //TODO this method may be modified or go away when we fully switch to the DOM
		//TODO later, add the CSS ID checking
		//TODO later make this more robust for CSS 2 and CSS 3
		if(selectorContext.getTagName().length() == 0 || selectorContext.getTagName().equals(elementLocalName)) { //if the tag names match, or we don't have a tag name to match with (which means we'll be matching class only)
			//TODO right now this is XHTML-specific---make more generic if possible
			if(selectorContext.getTagClass().length() == 0 || selectorContext.getTagClass().equals(getElementAttributeValue(element, null, ATTRIBUTE_CLASS))) //if the class names match as well (or there isn't a specified class in this selector)
				return true;
		}
		return false; //if we get to this point, this selector doesn't apply to this element
	}

}
