package com.garretwilson.text.xml.stylesheets.css;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import javax.mail.internet.ContentType;
import com.garretwilson.io.*;
import com.garretwilson.net.URIUtilities;
import com.garretwilson.text.xml.XMLNamespaceProcessor;
import com.garretwilson.text.xml.XMLUtilities;
import com.garretwilson.text.xml.stylesheets.XMLStyleSheetConstants;
import com.garretwilson.text.xml.stylesheets.XMLStyleSheetDescriptor;
import com.garretwilson.text.xml.oeb.OEBConstants;
import com.garretwilson.text.xml.xhtml.XHTMLConstants;
//G***del import com.garretwilson.text.xml.xhtml.XHTMLUtilities;  //G***del
import com.garretwilson.util.Debug;
import com.garretwilson.util.NameValuePair;
import org.w3c.dom.css.*;

/**Applies styles to XML elements.
@author Garret Wilson
*/
public abstract class AbstractXMLCSSStylesheetApplier implements XMLStyleSheetConstants, URIInputStreamable
{

	/**Determeins the base URI of the given document.
	@param document The document for which a base URI should be returned.
	@return The base URI of the document, or <code>null</code> if the base URI
		of the document is not known.
	*/
//G***del if not needed	protected abstract URI getDocumentBaseURI(final Object document);

	/**Returns the object that represents the root element of the given document.
	@param The object representing the XML document.
	@return The object representing the root element of the XML document.
	*/
	protected abstract Object getDocumentElement(final Object document);

	/**Retrieves processing instructions from the given document.
	@param document The document that might contain XML processing instructions.
	@return A non-<code>null</code> array of name-value pairs representing
		processing instructions.
	*/
	protected abstract NameValuePair[] getDocumentProcessingInstructions(final Object document);

	/**Retrieves the namespace URI of the given element.
	@param element The element for which the namespace URI should be returned.
	@return The namespace URI of the given element.
	*/
	protected abstract String getElementNamespaceURI(final Object element);

	/**Retrieves the local name of the given element.
	@param element The element for which the local name should be returned.
	@return The local name of the given element.
	*/
	protected abstract String getElementLocalName(final Object element);

	/**Retrieves the value of one of the element's attributes.
	@param element The element owner of the attributes.
	@param attributeNamespaceURI The namespace of the attribute to find.
	@param attributeLocalName The local name of the attribute to find.
	@return The value of the specified attribute, or <code>null</code> if there
		is no such attribute.
	*/
	protected abstract String getElementAttributeValue(final Object element, final String attributeNamespaceURI, final String attributeLocalName);

	/**Retrieves the parent element for the given element.
	@param element The element for which a parent should be found.
	@return The element's parent, or <code>null</code> if no parent could be found.
	 */
	protected abstract Object getParentElement(final Object element);

	/**Determines the number of child elements the given element has.
	@param element The parent element.
	@return The number of child elements this element has.
	*/
	protected abstract int getChildElementCount(final Object element);
	
	/**Retrieves the given indexed child of an element.
	@param element The parent element.
	@param index The zero-based index of the child.
	@return The child of the element at the given index.
	*/
	protected abstract Object getChildElement(final Object element, final int index);

	/**Retrieves all child text of the given element.
	@param element The element for which text should be returned.
	@return The text content of the element.
	*/
	protected abstract String getElementText(final Object element);

	/**Imports style information into that already gathered for the given element.
	@param element The element for which style information should be imported
	@param cssStyle The style information to import.	
	*/
	protected abstract void importCSSStyle(final Object element, final CSSStyleDeclaration cssStyle);

	/**Makes copies of the properties in the second style declaration and adds
		them to the first style, replacing any properties with the same name that
		may already exist.
	@param styleDeclaration The style declaration to receive the new style
		information.
	@param importStyleDeclaration The style to import.
	*/
	public static void importStyle(final CSSStyleDeclaration styleDeclaration, final CSSStyleDeclaration importStyleDeclaration)
	{
		for(int i=0; i<importStyleDeclaration.getLength(); ++i)	//look at each of the properties in the style to import
		{
			final String propertyName=importStyleDeclaration.item(i);	//get the name of this property
/*TODO fix to use generic CSS DOM routines, rather than custom classes
			final CSSValue cssValue=importStyleDeclaration.getPropertyCSSValue(proprtyName);	//get the value for this property
			styleDeclaration.setProperty(propertyName, cssValue.getCssText(), );			
*/
			((XMLCSSStyleDeclaration)styleDeclaration).setPropertyCSSValue(propertyName, importStyleDeclaration.getPropertyCSSValue(propertyName));	//get this property value from the style declaration, and set it in our style declaration, replacing any value(s) already set G***do a clone() here before we set the value
		}
	}

	/**Finds and loads all stylesheets referenced in the document. The
		external stylesheets are:
		<ul>
			<li>External stylesheets referenced from XML processing instructions.</li>
			<li>External stylesheets referenced from HTML/OEB link elements.</li>
			<li>Internal stylesheets in the HTML/OEB style element.</li>
		</ul>
	@param document The document that references the stylesheets.
	@param baseURI The base URI of the document, or <code>null</code> if the base
		URI is not known.
	@param mediaType The media type of the document, or <code>null</code>. if the
		media type is unknown.
	@return An array of stylesheets.
	*/
	public CSSStyleSheet[] getStylesheets(final Object document, final URI baseURI, final ContentType mediaType)
	{
		final List styleSheetList=new ArrayList(); //create a new list to hold the stylesheets
			//get all default stylesheets
		final String[] namespaceURIArray=getNamespaceURIs(document, mediaType);  //get all namespaces used in this document
		for(int i=0; i<namespaceURIArray.length; ++i) //look at each namespace
		{
			final String namespaceURI=namespaceURIArray[i]; //get this namespace
Debug.trace("getting default stylesheet for namespace: ", namespaceURI);  //G***del
			final CSSStyleSheet cssStyleSheet=XMLCSSUtilities.getDefaultStyleSheet(namespaceURI);  //get the default stylesheet for the given namespace
			if(cssStyleSheet!=null) //if we know about a default stylesheet for this namespace
			{
Debug.trace("Found default stylesheet for namespace: ", namespaceURI);  //G***del
//G***del Debug.trace("Default stylesheet: ", cssStyleSheet);  //G***testing; del
				styleSheetList.add(cssStyleSheet);  //add the default stylesheet to the list
			}
		}
			//create a new CSS processor for parsing all the stylesheets, showing that we'll worry about getting the needed input streams
		final XMLCSSProcessor cssProcessor=new XMLCSSProcessor(this);
			//gather all the references to external stylesheets
		final XMLStyleSheetDescriptor[] styleSheetDescriptorArray=getStylesheetDescriptors(document);
		if(styleSheetDescriptorArray.length>0)  //if there are stylesheet descriptors
		{
			for(int i=0; i<styleSheetDescriptorArray.length; ++i) //look at each stylesheet descriptor
			{
				final XMLStyleSheetDescriptor styleSheetDescriptor=styleSheetDescriptorArray[i];  //get the next descriptor
				assert styleSheetDescriptor.getHRef()!=null : "Stylesheet processing instruction has null href";  //G***fix
				//G***do whatever we need to do for the media type, title, etc.
//		G***del Debug.trace("Looking at stylesheet descriptor: ", styleSheetDescriptor.getHRef());  //G***del
				try
				{
						//create a URI from the original URI of the XML document and the stylesheet href
					final URI styleSheetURI=URIUtilities.createURI(baseURI, styleSheetDescriptor.getHRef());
					final Reader styleSheetReader=new InputStreamReader(getInputStream(styleSheetURI));	//get an input stream to the stylesheet G***use the document's encoding here
					try
					{
						final CSSStyleSheet cssStyleSheet=cssProcessor.parseStyleSheet(styleSheetReader, styleSheetURI); //parse the stylesheet
//		G***del Debug.trace("parsed stylesheet: ", cssStyleSheet);  //G***testing; del
						styleSheetList.add(cssStyleSheet);  //add this stylesheet to our list
					}
					finally
					{
						styleSheetReader.close(); //always close the stylesheet reader
					}
				}
				catch(IOException ioException)  //if there are any I/O exceptions
				{
					Debug.warn(ioException);  //G***fix better
				}
				catch(URISyntaxException uriSyntaxException)  //if there are any URI syntax errors
				{
					Debug.warn(uriSyntaxException);  //G***fix better
				}
			}
		}
			//process and gather any internal stylesheets G***right now, this is very HTML/OEB specific; fix to check the namespace or something
		final Object documentElement=getDocumentElement(document);	//get the document element
		final int childElementCount=getChildElementCount(documentElement);  //find out how many direct children are in the document
		for(int childIndex=0; childIndex<childElementCount; ++childIndex)  //look at the direct children of the document element
		{
			final Object childElement=getChildElement(documentElement, childIndex); //get a reference to the child element
			final String childElementLocalName=getElementLocalName(childElement);  //get the child element local name
					//if this element is <head> and it's an HTML <head>
			if(XHTMLConstants.ELEMENT_HEAD.equals(childElementLocalName))
//TODO fix checking to see if this is HTML					&& XHTMLSwingTextUtilities.isHTMLElement(childAttributeSet, documentAttributeSet))
			{
				getInternalStylesheets(childElement, cssProcessor, styleSheetList); //get the internal stylesheets from the HTML <head> element
			}
		}
		return (CSSStyleSheet[])styleSheetList.toArray(new CSSStyleSheet[styleSheetList.size()]);  //return an array of stylesheets we collected
	}

	/**Finds and parses any internal stylesheets contained in the given element or
		its children. All stylesheets found in the document will be added to the
		list in the order encoundered in document order.
	@param element The XML element to check.
	@param cssProcessor The processor that processes CSS stylesheets.
	@param styleSheetList The list to which stylesheets should be added.
	*/
	protected void getInternalStylesheets(final Object element, final XMLCSSProcessor cssProcessor, final List styleSheetList)
	{
		final String elementLocalName=getElementLocalName(element);  //get the element's local name
			//if this is an HTML <style>
		if(XHTMLConstants.ELEMENT_STYLE.equals(elementLocalName))
//TODO fix checking to see if this is HTML				&& XHTMLSwingTextUtilities.isHTMLElement(attributeSet, swingDocumentElement.getAttributes()))
		{
			try
			{
				final String styleText=getElementText(element);  //get the text of the style element
				final String descriptionString="Internal Style Sheet"; //create a string to describe the internal style sheet G***i18n
				final CSSStyleSheet stylesheet=cssProcessor.parseStyleSheet(styleText, descriptionString); //parse the stylesheet
				styleSheetList.add(stylesheet); //add this stylesheet to the list
			}
			catch(IOException ioException)  //if there are any I/O exceptions
			{
				Debug.warn(ioException);  //G***fix better
			}
		}
		else  //if this is not a style element
		{
			final int childElementCount=getChildElementCount(element); //find out how many child elements there are
			for(int childElementIndex=0; childElementIndex<childElementCount; ++childElementIndex) //look at each child element
			{
				final Object childElement=getChildElement(element, childElementIndex);  //get this child element
				getInternalStylesheets(childElement, cssProcessor, styleSheetList);  //get internal stylesheets contained in this element
			}
		}
	}

	/**Finds the namespaces used by the document.
	<p>If no namespace is indicated, a default namespace is derived from the
		document media type. For example, a document with no namespace indicated
		but with a media type of <code>text/html</code> will include the namespace
		<code>http://www.w3.org/1999/xhtml</code>.</p>
	<p>Currently this method only examines the root XML element, so other
		vocabularies within the document will be ignored.</p>
	@param document The document that contains the namespaces.
	@param mediaType The media type of the document, or <code>null</code> if the
		media type is unknown.
	@return A non-<code>null</code> array of namespace URIs.
	*/
	protected String[] getNamespaceURIs(final Object document, final ContentType mediaType)  //G***fix to actually look through all the namespaces, maybe, but that could be intensive -- on the other hand, a subclass could get that information from the OEB package, overriding the intensive part
	{
//G***fix when we support multiple namespaces		final List namespaceList=new ArrayList(); //create a list to store the namespaces
		final Object documentElement=getDocumentElement(document);	//get the root element of the document
		String namespaceURI=getElementNamespaceURI(documentElement);  //get the document element namespace URI
		if(namespaceURI==null)  //if there is no namespace defined
		{
			final URI mediaTypeNamespaceURI=XMLNamespaceProcessor.getDefaultNamespaceURI(mediaType);	//see if we can find a default namespace for the media type
			if(mediaTypeNamespaceURI!=null)	//if we found a namespace for the media type
			{
				namespaceURI=mediaTypeNamespaceURI.toString();	//use the namespace for the media type
			}
		}
		if(namespaceURI!=null)  //if we found a namespace
			return new String[]{namespaceURI};  //return the namespace we found
		else  //if we didn't find a namespace
			return new String[]{};  //G***fix; make efficient
	}


	/**Retrieves an array of all referenced stylesheets in the document by href.
		The stylesheet descriptors gathered reference:
		<ul>
			<li>External stylesheets referenced from XML processing instructions.</li>
			<li>External stylesheets referenced from HTML/OEB link elements.</li>
		</ul>
	@param document The document in which the descriptors exist.
	@return An array of style sheet descriptors, each referencing a stylesheet.
	*/
	protected XMLStyleSheetDescriptor[] getStylesheetDescriptors(final Object document)
	{
		final List styleSheetDescriptorList=new ArrayList();  //create a list to hold stylesheet descriptors
		final NameValuePair[] processingInstructions=getDocumentProcessingInstructions(document);  //get the processing instructions, if any (this will never return null)
			//find stylesheet references from processing instructions
		for(int processingInstructionIndex=0; processingInstructionIndex<processingInstructions.length; ++processingInstructionIndex) //look at each processing instruction
		{
			final NameValuePair processingInstruction=processingInstructions[processingInstructionIndex];  //get this processing instruction's values
				//if this is a stylesheet processing instruction
			if(XML_STYLESHEET_PROCESSING_INSTRUCTION.equals(processingInstruction.getName()))
			{
				final String processingInstructionData=(String)processingInstruction.getValue();  //get the processing instruction's data, assuming it's a string
				//G***check the media type, etc. here
					//get the href pseudo-attribute, if it is defined
				final String href=XMLUtilities.getProcessingInstructionPseudoAttributeValue(processingInstructionData, XMLStyleSheetConstants.HREF_ATTRIBUTE);
				final XMLStyleSheetDescriptor styleSheetDescriptor=new XMLStyleSheetDescriptor(href); //create a new descriptor for this stylesheet G***fix for media type, title, etc.
				styleSheetDescriptorList.add(styleSheetDescriptor); //add the stylesheet descriptor to our list
			}
		}
//TODO fix for stylesheet instructions in HTML/OEB link elements
		return (XMLStyleSheetDescriptor[])styleSheetDescriptorList.toArray(new XMLStyleSheetDescriptor[styleSheetDescriptorList.size()]);  //convert the stylesheet descriptors to an array and return them
	}

	/**Determines the style of each element in the given element and its children
		based on the given stylesheet.
	@param styleSheet The stylesheet to apply to the element.
	@param element The element to which styles should be applied, along
		with its children.
	*/
	public void applyStyleSheet(final CSSStyleSheet styleSheet, final Object element)
	{
		final String elementLocalName=getElementLocalName(element);  //get the element's local name for quick lookup
//G***del Debug.trace("applying stylesheet to element: ", elementLocalName);  //G***del
		final CSSRuleList cssRuleList=styleSheet.getCssRules(); //get the list of CSS rules
		for(int ruleIndex=0; ruleIndex<cssRuleList.getLength(); ++ruleIndex)	//look at each of our rules
		{
			if(cssRuleList.item(ruleIndex).getType()==CSSRule.STYLE_RULE)	//if this is a style rule G***fix for other rule types
			{
				final CSSStyleRule cssStyleRule=(CSSStyleRule)cssRuleList.item(ruleIndex);	//get a reference to this style rule in the stylesheet
				if(isApplicable(cssStyleRule, element, elementLocalName)) //if this style rule applies to this element
				{
					importCSSStyle(element, cssStyleRule.getStyle());	//import this style into whatever style we've collected so far for this element, if any
				}
			}
		}
			//apply the stylesheet to the child elements
		for(int childIndex=getChildElementCount(element)-1; childIndex>=0; --childIndex) //look at each child element, starting from the last to the first because order doesn't matter
		{
			final Object childElement=getChildElement(element, childIndex);  //get this child
			applyStyleSheet(styleSheet, childElement); //apply this stylesheet to the child element
/*TODO fix for Swing stylesheet applier
			if(!AbstractDocument.ContentElementName.equals(childSwingElement.getName())) //if this isn't content (content never gets styled)
			{
				applyStyleSheet(styleSheet, childSwingElement); //apply this stylesheet to the child element
			}
*/
		}
	}

	/**Determines whether, based upon this style rule's selectors, the given style
		applies to the specified element.
	@param cssStyleRule The style rule to check against.
	@param element The element this style might apply to.
	@param elementLocalName The element's local name for quick lookup.
	*/
/*G***del if not needed
	protected boolean isApplicable(final CSSStyleRule cssStyleRule, final Object element)
	{
		return isApplicable(cssStyleRule, element, getElementLocalName(element));	//pass along the local name of the element for quick lookup
	}
*/

	/**Determines whether, based upon this style rule's selectors, the given style
		applies to the specified element.
	@param cssStyleRule The style rule to check against.
	@param element The element this style might apply to.
	@param elementLocalName The element's local name for quick lookup.
	*/
	protected boolean isApplicable(final CSSStyleRule cssStyleRule, final Object element, final String elementLocalName)
	{
//G***del Debug.trace("Checking to see if: "+getSelectorText()+" applies to: "+element.getNodeName());
		final XMLCSSStyleRule xmlCSSStyleRule=(XMLCSSStyleRule)cssStyleRule;  //G***fix; right now we use special features of our CSS DOM implementation -- fix to use just the normal CSS DOM
		for(int selectorIndex=0; selectorIndex<xmlCSSStyleRule.getSelectorArrayList().size(); ++selectorIndex)	//look at each selector array
		{
			final XMLCSSSelector[] contextArray=(XMLCSSSelector[])xmlCSSStyleRule.getSelectorArrayList().get(selectorIndex);	//get a reference to this array of selectors
/*G***del
Debug.trace("Checking against the following context, right-to-left:");
		for(int contextIndex=contextArray.length-1; contextIndex>=0; --contextIndex)	//G***del; testing
		{
			final XMLCSSSelector selectorContext=contextArray[contextIndex];	//
Debug.trace("Context "+contextIndex+": "+selectorContext.getTagName());	//G***del
		}
*/
			if(isApplicable(contextArray, element, elementLocalName))	//if this context array applies to the element
			{
//G***del Debug.trace("Context array applies to element "+element.getNodeName());	//G***del
				return true;	//we don't need to check the others; we've found a match
			}
		}
		return false;	//if none of our array of contextual selectors match, show that this style rule doesn't apply to this element
	}

	/**Determines whether this contextual selector (represented by an array of
		selectors) applies to the specified element.
	@param contextArray The array of nested contexts to compare to the element
		hierarchy.
	@param element The element this context array might apply to.
	@param elementLocalName The element's local name for quick lookup.
	*/
	protected boolean isApplicable(final XMLCSSSelector[] contextArray, Object element, final String elementLocalName) //G***this method may be modified or go away when we fully switch to the DOM
	{
		//first see if we can do a quick comparison on the most common type of selector: name-based selectors
		if(contextArray.length==1)  //if there is only one context in the array
		{
			final XMLCSSSelector selectorContext=contextArray[0];	//get the only context of the selector
				//if this context only looks at the tag name
			if(selectorContext.getTagName().length()>0 && selectorContext.getTagClass().length()==0)  //G***fix for CSS2 and CSS3
			{
//G***del Debug.trace("we can do a quick CSS selection for element: ", elementLocalName);  //G***del
				return selectorContext.getTagName().equals(elementLocalName); //compare tag names here without going on to anything more complicated
			}
		}
//G***del Debug.trace("we *cannot* do a quick CSS selection for element: ", elementLocalName);  //G***del
		for(int contextIndex=contextArray.length-1; contextIndex>=0; --contextIndex)	//look at each context for this selector, working from the last one (applying to this element) to the first one (applying to an ancestor of this element)
		{
//G***del Debug.trace("Checking element: "+element.getNodeName());
			final XMLCSSSelector selectorContext=contextArray[contextIndex];	//get this context of the selector
//G***del Debug.trace("against: "+selectorContext.getCssText());
			if(!isApplicable(selectorContext, element, elementLocalName))	//if this selector context does not apply to this element
				return false;	//this entire contextual selector does not apply
//G***del Debug.trace("matches");
			if(contextIndex>0)	//if we're still working our way up the chain
			{
				element=getParentElement(element); //get this element's parent
				if(element==null)	//if this element does not have a parent
				{
					return false;	//since we're not at the top of the context chain, this element can't match since it has no parents to compare
				}
			}
		}
		return true;	//if we make it here, we've worked our way up the selector context chain and every element along the way has applied to the appropriate selector
	}

	/**Determines whether the given selector applies to the specified element.
		Currently this method only checks the selector's tag name and the class,
		in both instances ignoring the element's namespace.
	@param selectorContext The description of this stop in the selector context
		path.
	@param element The element whose name and class should match those
		contained in this selector.
	@param elementLocalName The element's local name for quick lookup.
	*/
	protected boolean isApplicable(final XMLCSSSelector selectorContext, final Object element, final String elementLocalName)  //G***this method may be modified or go away when we fully switch to the DOM
	{
//G***del Debug.trace("XMLCSSSelector checking to see if "+element.getNodeName()+" matches "+getCssText()); //G***del
			//G***later, add the CSS ID checking
			//G**later make this more robust for CSS 2 and CSS 3
		if(selectorContext.getTagName().length()==0 || selectorContext.getTagName().equals(elementLocalName))  //if the tag names match, or we don't have a tag name to match with (which means we'll be matching class only)
		{
//G***del Debug.trace("Element "+element.getNodeName()+" matched, now checking to see if class: "+element.getAttributeNS(null, "class")+" equals the tag we expect: "+getTagClass());	//G***del
					//G***right now this is XHTML-specific---make more generic if possible
			if(selectorContext.getTagClass().length()==0 || selectorContext.getTagClass().equals(getElementAttributeValue(element, null, XHTMLConstants.ATTRIBUTE_CLASS)))	//if the class names match as well (or there isn't a specified class in this selector)
				return true;
		}
		return false;	//if we get to this point, this selector doesn't apply to this element
	}

}