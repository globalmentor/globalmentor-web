package com.garretwilson.text.xml.stylesheets.css;

import java.util.*;
import com.garretwilson.io.*;
import com.garretwilson.text.xml.XMLUtilities;
import com.garretwilson.text.xml.stylesheets.XMLStyleSheetDescriptor;
import com.garretwilson.text.xml.stylesheets.XMLStyleSheetList;
import com.garretwilson.text.xml.stylesheets.css.CSSStyleManager;
import com.garretwilson.text.xml.oeb.OEBConstants;
import com.garretwilson.text.xml.xhtml.XHTMLConstants;
//G***del import com.garretwilson.text.xml.xhtml.XHTMLUtilities;  //G***del
import com.garretwilson.util.Debug;
import org.w3c.dom.*;
import org.w3c.dom.css.*;
import org.w3c.dom.stylesheets.*;

/**Applies styles to XML elements.
@author Garret Wilson
*/
public class XMLCSSStylesheetApplier implements CSSStyleManager
{

	/**The map of styles, each keyed to an element. The weak map allows the styles
		to be garbage-collected once the corresponding elent is no longer used.
	*/
	protected final Map cssStyleMap=new WeakHashMap();  //G***fix entire pattern to be much more general and robust

		/**@return The map of styles, each keyed to an element.*/
//G***del		public Map getCSSStyleMap() {return cssStyleMap;}

//G***maybe delete this comment
		/**Retrieves the style of the given element.
		  The returned style object is guaranteed to be non-<code>null</code>&mdash;
			if the requested element has no style in the map, a default empty style is
			created and added to the map, keyed to the element.
		@param element The element for which a style should be returned.
		@return A non-<code>null</code> style for the given element.
		*/

		/**Retrieves the style of the given element.
		@param element The element for which a style should be returned.
		@return A style for the given element, or <code>null</code> if the element
			has no style assigned to it.
		*/
		public CSSStyleDeclaration getStyle(final Element element)
		{
			return (CSSStyleDeclaration)cssStyleMap.get(element);  //return the style, if any, stored keyed to the element
		}

		/**Sets the style of an element, replacing any existing style stored for the
		  element.
		@param element The element for which a style is being specified.
		@param style The element style.
		*/
		public void setStyle(final Element element, final CSSStyleDeclaration style)
		{
		  cssStyleMap.put(element, style);  //store the style in the map, keyed to the element
		}

	/**Default constructor.*/
	public XMLCSSStylesheetApplier()
	{
	}

	/**Discovers any referenced styles to this document, loads the stylesheets,
		and applies the styles to the Swing element attributes.
	@param document The XML document tree.
	@param mediaType The media type of the document, or <code>null</code. if the
		media type is unknown.
	*/
	public void applyStyles(final Document document, final MediaType mediaType)
	{
Debug.trace("Ready to apply styles");  //G***fix
//G***del				final URL documentURL=XMLStyleConstants.getBaseURL(swingDocumentElement.getAttributes());  //get the URL of this document
		final StyleSheetList styleSheetList=getStylesheets(document, mediaType); //G***testing
		//apply the stylesheets
		final int styleSheetCount=styleSheetList.getLength(); //find out how many stylsheets there are
		for(int i=0; i<styleSheetCount; ++i) //look at each stylesheet
		{
Debug.trace("applying stylesheet: ", i);  //G***del
			//prepare a progress message: "Applying stylesheet X to XXXXX.html"
//G***fix				final String progressMessage=MessageFormat.format("Applying stylesheet {0} to {1}", new Object[]{new Integer(i+1), documentURL!=null ? documentURL.toString() : "unknown"}); //G***i18n; fix documentURL if null
//G***fixDebug.trace(progressMessage); //G***del
//G***fix				fireMadeProgress(new ProgressEvent(this, APPLY_STYLESHEET_TASK, progressMessage, swingDocumentElementIndex, swingDocumentElementCount));	//fire a progress message saying that we're applying a stylesheet
			final CSSStyleSheet cssStyleSheet=(CSSStyleSheet)styleSheetList.item(i);  //get a reference to this stylesheet, assuming that it's a CSS stylesheet (that's all that's currently supported)
		  applyStyleSheet(cssStyleSheet, document);  //apply the stylesheet to the document
		}
		//G***fire a progress event about applying local styles
//G***fix			applyLocalStyles(swingDocumentElement); //go through and apply local styles in from the "style" attributes G***this is HTML-specific; fix
	}


	/**Finds and loads all stylesheets referenced in the document. The
		external stylesheets are:
		<ul>
			<li>External stylesheets referenced from XML processing instructions.</li>
			<li>External stylesheets referenced from HTML/OEB link elements.</li>
			<li>Internal stylesheets in the HTML/OEB style element.</li>
		</ul>
	@param document The XML document tree.
	@param mediaType The media type of the document, or <code>null</code>. if the
		media type is unknown.
	@return A list of stylesheets.
	*/
	protected StyleSheetList getStylesheets(final Document document, final MediaType mediaType)
	{
		final XMLStyleSheetList styleSheetList=new XMLStyleSheetList(); //create a new list to hold the stylesheets
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
/*G***fix
		  //gather all the references to stylesheets
		final XMLStyleSheetDescriptor[] styleSheetDescriptorArray=getStylesheetDescriptors(swingDocumentElement);
		if(styleSheetDescriptorArray.length>0)  //if there are stylesheet descriptors
		{
			final AttributeSet attributeSet=swingDocumentElement.getAttributes();  //get the element's attribute set G***do we know this isn't null?
			URL baseURL=XMLStyleConstants.getBaseURL(attributeSet); //get the base URL of the XML document
			if(baseURL==null) //if we couldn't found a base URL in the attributes
				baseURL=getBaseURL();	//get the base URL from the document
				//create a new CSS processor for parsing all the stylesheets, showing that we'll worry about getting the needed input streams (this allows our subclasses to access zip files, for instance)
			final XMLCSSProcessor cssProcessor=new XMLCSSProcessor(this);
//G***del when not needed			final Iterator styleSheetDescriptorIterator=styleSheetDescriptorList.iterator();  //get an iterator to all stylesheet descriptors we've gathered so far
//G***del when not needed			while(styleSheetDescriptorIterator.hasNext()) //while there are more stylesheet descriptors
		  for(int i=0; i<styleSheetDescriptorArray.length; ++i) //look at each stylesheet descriptor
			{
//G***del when not needed				final XMLStyleSheetDescriptor styleSheetDescriptor=(XMLStyleSheetDescriptor)styleSheetDescriptorIterator.next();  //get the next descriptor
				final XMLStyleSheetDescriptor styleSheetDescriptor=styleSheetDescriptorArray[i];  //get the next descriptor
				Debug.assert(styleSheetDescriptor.getHRef()!=null, "Stylesheet processing instruction has null href");  //G***fix
				//G***do whatever we need to do for the media type, title, etc.

//G***del Debug.trace("Looking at stylesheet descriptor: ", styleSheetDescriptor.getHRef());  //G***del
				try
				{
					final URL styleSheetURL=URLUtilities.createURL(baseURL, styleSheetDescriptor.getHRef());	//create a URL from the original URL of the XML document and the stylesheet href
					final InputStreamReader styleSheetReader=new InputStreamReader(getResourceAsInputStream(styleSheetURL));	//get an input stream to the stylesheet G***use the document's encoding here
					try
					{
						final CSSStyleSheet cssStyleSheet=cssProcessor.parseStyleSheet(styleSheetReader, styleSheetURL); //parse the stylesheet
//G***del Debug.trace("parsed stylesheet: ", cssStyleSheet);  //G***testing; del
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
			}
		}
*/
		return styleSheetList;  //return the list of stylesheets we collected
	}

	/**Finds the namespaces used by the document.
		<p>If no namespace is indicated, a default namespace is derived from the
		document media type. For example, a document with no namespace indicated
		but with a media type of <code>text/html</code> will include the namespace
		<code>http://www.w3.org/1999/xhtml</code>.</p>
		<p>Currently this method only examines the root XML element, so other
		vocabularies within the document will be ignored.</p>
	@param document The XML document tree.
	@param mediaType The media type of the document, or <code>null</code>. if the
		media type is unknown.
	@return A non-<code>null</code> array of namespace URIs.
	*/
	protected String[] getNamespaceURIs(final Document document, final MediaType mediaType)  //G***fix to actually look through all the namespaces, maybe, but that could be intensive -- on the other hand, a subclass could get that information from the OEB package, overriding the intensive part
	{
//G***fix when we support multiple namespaces		final List namespaceList=new ArrayList(); //create a list to store the namespaces
		String namespaceURI=document.getDocumentElement().getNamespaceURI();  //get the document element namespace URI
		if(namespaceURI==null)  //if there is no namespace defined
		{
			if(mediaType!=null) //if we have a media type G***change this so that these media types aren't hard-coded in
			{
Debug.trace("getting default namespace URI, found media type: ", mediaType);  //G***del
				if(mediaType.equals(mediaType.TEXT_HTML)) //if the media type is text/html
					namespaceURI=XHTMLConstants.XHTML_NAMESPACE_URI;  //use the XHTML namespace
				else if(mediaType.equals(mediaType.TEXT_X_OEB1_DOCUMENT)) //if the media type is for an OEB document
					namespaceURI=OEBConstants.OEB1_DOCUMENT_NAMESPACE_URI.toString();  //use the OEB document namespace
Debug.trace("namespace URI: ", namespaceURI);  //G***del
			}
		}
		if(namespaceURI!=null)  //if we found a namespace
			return new String[]{namespaceURI};  //return the namespace we found
		else  //if we didn't find a namespace
			return new String[]{};  //G***fix; make efficient
	}


	/**Determines the style of the given XML document based on the given stylesheet.
	@param styleSheet The stylesheet to apply to the element.
	@param document The XML document to which styles should be applied.
	*/
	protected void applyStyleSheet(final CSSStyleSheet styleSheet, final Document document)
	{
		applyStyleSheet(styleSheet, document.getDocumentElement()); //apply the stylesheet to the document element
	}

	/**Determines the style of the given element and its children based on the
		given stylesheet.
	@param styleSheet The stylesheet to apply to the element.
	@param element The element to which styles should be applied, along
		with its children.
	*/
	protected void applyStyleSheet(final CSSStyleSheet styleSheet, final Element element)
	{
		final String elementLocalName=element.getLocalName(); //get the element's local name for quick lookup
//G***del Debug.trace("applying stylesheet to element: ", elementLocalName);  //G***del
		final CSSRuleList cssRuleList=styleSheet.getCssRules(); //get the list of CSS rules
		for(int ruleIndex=0; ruleIndex<cssRuleList.getLength(); ++ruleIndex)	//look at each of our rules
		{
			if(cssRuleList.item(ruleIndex).getType()==CSSRule.STYLE_RULE)	//if this is a style rule G***fix for other rule types
			{
				final CSSStyleRule cssStyleRule=(CSSStyleRule)cssRuleList.item(ruleIndex);	//get a reference to this style rule in the stylesheet
				if(isApplicable(cssStyleRule, element, elementLocalName)) //if this style rule applies to this element
				{
//G***del when not needed		  		final AttributeSet attributeSet=swingElement.getAttributes();  //get the element's attribute set G***do we know this isn't null?

						//get this element's style G***fix to use the normal CSS DOM, putting the importStyle() method into a generic Swing utility class
				  XMLCSSStyleDeclaration elementStyle=(XMLCSSStyleDeclaration)getStyle(element);
				  if(elementStyle==null) //if there is no existing style
					{
						elementStyle=new XMLCSSStyleDeclaration();  //create an empty default style
						setStyle(element, elementStyle); //set the element style to the new one we created
					}
//G***del					Debug.trace("style rule is of type: ", cssStyleRule.getClass().getName());  //G***del
Debug.trace("Adding style to element: ", element);  //G***del
Debug.trace("Adding style: ", cssStyleRule.getStyle()); //G***del
					elementStyle.importStyle((XMLCSSStyleDeclaration)cssStyleRule.getStyle());  //import the style G***use generic DOM and move this to a utility class
//G***del Debug.trace("new, completed style for element: ", getStyle(element)); //G***del
				}
			}
		}
			//apply the stylesheet to the child elements
		for(int childIndex=element.getChildNodes().getLength()-1; childIndex>=0; --childIndex) //look at each child element, starting from the last to the first because order doesn't matter
		{
			final Node childNode=element.getChildNodes().item(childIndex);  //get this child node
			if(childNode.getNodeType()==childNode.ELEMENT_NODE) //if this is an element
			{
				applyStyleSheet(styleSheet, (Element)childNode); //apply this stylesheet to the child element
			}
		}
	}


























	/**Determines whether the given selector applies to the specified element.
		Currently this method only checks the selector's tag name and the class,
		in both instances ignoring the element's namespace.
	@param selectorContext The description of this stop in the selector context
		path.
	@param element The element whose name and class should match those
		contained in this selector.
	@return <code>true</code> if the given selector context applies to the given
		element.
	*/
	protected static boolean isApplicable(final XMLCSSSelector selectorContext, final Element element)  //G***this method may be modified or go away when we fully switch to the DOM
	{
//G***del Debug.trace("XMLCSSSelector checking to see if "+element.getNodeName()+" matches "+getCssText()); //G***del
		final String localName=element.getLocalName();  //get the element's local name
			//G***later, add the CSS ID checking
			//G**later make this more robust for CSS 2 and CSS 3
		if(selectorContext.getTagName().length()==0 || selectorContext.getTagName().equals(localName))  //if the tag names match, or we don't have a tag name to match with (which means we'll be matching class only)
		{
//G***del Debug.trace("Element "+element.getNodeName()+" matched, now checking to see if class: "+element.getAttributeNS(null, "class")+" equals the tag we expect: "+getTagClass());	//G***del
			if(selectorContext.getTagClass().length()==0 || selectorContext.getTagClass().equals(element.getAttributeNS(null, "class"))) //if the class names match as well (or there isn't a specified class in this selector) G***use a constant here
				return true;
		}
		return false;	//if we get to this point, this selector doesn't apply to this element
	}

	/**Determines whether this contextual selector (represented by an array of
		selectors) applies to the specified element.
	@param contextArray The array of nested contexts to compare to the element
		hierarchy.
	@param element The element this context array might apply to.
	@param elementLocalName The element's local name for quick lookup.
	@return <code>true</code> if all the style selectors apply to this element.
	*/
	protected static boolean isApplicable(final XMLCSSSelector[] contextArray, Element element, final String elementLocalName) //G***this method may be modified or go away when we fully switch to the DOM
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
			if(!isApplicable(selectorContext, element))	//if this selector context does not apply to this element
				return false;	//this entire contextual selector does not apply
//G***del Debug.trace("matches");
			if(contextIndex>0)	//if we're still working our way up the chain
			{
				final Node parentNode=element.getParentNode();  //get this element's parent
				if(parentNode!=null && parentNode.getNodeType()==parentNode.ELEMENT_NODE) //if there is a parent node, and the parent is an element
					element=(Element)parentNode;	//we'll check the earlier context with this element's parent node
				else	//if this element has no parent element
					return false;	//since we're not at the top of the context chain, this element can't match since it has no parents to compare
			}
		}
		return true;	//if we make it here, we've worked our way up the selector context chain and every element along the way has applied to the appropriate selector
	}

	/**Determines whether, based upon this style rule's selectors, the given style
		applies to the specified element.
	@param cssStyleRule The style rule to check against.
	@param element The element this style might apply to.
	@param elementLocalName The element's local name for quick lookup.
	@return <code>true</code> if the style applies to the element.
	*/
	protected static boolean isApplicable(final CSSStyleRule cssStyleRule, final Element element, final String elementLocalName)
	{
//G***del Debug.trace("Checking to see if: "+getSelectorText()+" applies to: "+element.getNodeName());
		final XMLCSSStyleRule xmlCSSStyleRule=(XMLCSSStyleRule)cssStyleRule;  //G***fix; right now we use special features of our CSS DOM implementation -- fix to use just the normal CSS DOM
		for(int selectorIndex=0; selectorIndex<xmlCSSStyleRule.getSelectorArrayList().size(); ++selectorIndex)	//look at each selector array
		{
			final XMLCSSSelector[] contextArray=(XMLCSSSelector[])xmlCSSStyleRule.getSelectorArrayList().get(selectorIndex);	//get a reference to this array of selectors
//G***del Debug.trace("Checking against the following context, right-to-left:");
//G***del 		for(int contextIndex=contextArray.length-1; contextIndex>=0; --contextIndex)	//G***del; testing
//G***del 		{
//G***del 			final XMLCSSSelector selectorContext=contextArray[contextIndex];	//
//G***del Debug.trace("Context "+contextIndex+": "+selectorContext.getTagName());	//G***del
//G***del 		}
			if(isApplicable(contextArray, element, elementLocalName))	//if this context array applies to the element
			{
//G***del Debug.trace("Context array applies to element "+element.getNodeName());	//G***del
				return true;	//we don't need to check the others; we've found a match
			}
		}
		return false;	//if none of our array of contextual selectors match, show that this style rule doesn't apply to this element
	}





















	/**Parses any external stylesheets contained in an XML document.
	@param document The XML document that that contains stylesheet information.
	@param sourceObject The source of the XML document (e.g. a String, File, or URL).
//G***fix all this exception stuff
	@except IOException Thrown when an i/o error occurs.
	@except ParseUnexpectedDataException Thrown when an unexpected character is found.
	@except ParseEOFException Thrown when the end of the input stream is reached unexpectedly.
	*/
//G***actually, these parseXXXStyleSheets() should probably be inside an XMLStyleSheetProcessor which calls the correct type of processor based upon the style sheet type
/*G***del when not needed
	public void parseExternalStyleSheets(final XMLDocument document, final Object sourceObject) throws IOException, ParseUnexpectedDataException, ParseEOFException
	{
			//check for xml-stylesheet processing instructions
		//G***we should probably eventually make sure the directives come at the right place, but an OEB version of this should have been validated against a DTD at some point which ensures this anyway
		final XMLNodeList styleSheetProcessingInstructionList=(XMLNodeList)document.getNodesByName(XMLNode.PROCESSING_INSTRUCTION_NODE, XML_STYLESHEET_PROCESSING_INSTRUCTION, true);	//get a list of all the style processing instructions in the document G***use a constant here
		for(int styleSheetIndex=0; styleSheetIndex<styleSheetProcessingInstructionList.size(); ++styleSheetIndex)	//look at each of the nodes representing a style sheet link
		{
			final XMLProcessingInstruction styleSheetLink=(XMLProcessingInstruction)styleSheetProcessingInstructionList.item(styleSheetIndex);	//get a reference to this child node
				//G***check the media type, etc. here
			final XMLCSSStyleSheet styleSheet=new XMLCSSStyleSheet(styleSheetLink);	//create a new stylesheet owned by this style processing instruction
			final String href=styleSheetLink.getPseudoAttributeValue(HREF_ATTRIBUTE);	//get the value of the href attribute, if it is present
			final URL styleSheetURL=URLUtilities.createURL(sourceObject, href);	//create a URL from the original URL of the XML document and the href
//G***del Debug.notify(styleSheetURL.toString());	//G***del
			final InputStreamReader inputStreamReader=new InputStreamReader(getInputStreamLocator().getInputStream(styleSheetURL));	//get an input stream to the external stylesheet G***use the document's encoding here
			final ParseReader styleSheetReader=new ParseReader(inputStreamReader, styleSheetURL);	//create a parse reader reader to use to read the external stylesheet
			try
			{
//G***fix			entityReader.setCurrentLineIndex(entity.getLineIndex());	//pretend we're reading where the entity was located in that file, so any errors will show the correct information
//G***fix			entityReader.setCurrentCharIndex(entity.getCharIndex());	//pretend we're reading where the entity was located in that file, so any errors will show the correct information
				parseStyleSheetContent(styleSheetReader, styleSheet);	//parse the stylesheet content
				document.getStyleSheetList().add(styleSheet);	//add the stylesheet we created to the document's list of stylesheets
			}
			finally
			{
				styleSheetReader.close();	//always close the stylesheet reader
			}
		}
	}
*/

	/**Parses any local styles contained in an XML document as a "style" attribute. G***this shouldn't go in the CSS processor itself
	@param document The XML document that that contains style information.
//G***fix all this exception stuff
	@except IOException Thrown when an i/o error occurs.
	@except ParseUnexpectedDataException Thrown when an unexpected character is found.
	@except ParseEOFException Thrown when the end of the input stream is reached unexpectedly.
	*/
//G***actually, these parseXXXStyleSheets() should probably be inside an XMLStyleSheetProcessor which calls the correct type of processor based upon the style sheet type
//G***this can probably be static
/*G***move
	public void parseLocalStyles(final XMLDocument document) throws IOException, ParseUnexpectedDataException, ParseEOFException
	{
		NodeIterator nodeIterator=((DocumentTraversal)document).createNodeIterator(document.getDocumentElement(), NodeFilter.SHOW_ELEMENT, null, false); //create a node walker to traverse over every node
		Node node;
		while((node=nodeIterator.nextNode())!=null)  //while we haven't reached the last node
		{
//G***del Debug.trace("Node: "+node.getNodeName()+" namespace URI: "+node.getNamespaceURI()); //G***testing
		  final Element element=(Element)node;  //cast the node to an element; elements are all we asked for
		  final String elementName=element.getNodeName(); //get the name of the element G***fix for namespaces
		  if(element.hasAttributeNS(null, "style")) //if this element has the style attribute G***use a constant, fix for a general attribute name case
			{
			  final String styleValue=element.getAttributeNS(null, "style");  //get the value of the style attribute G***use a constant, fix for a general attribute name case
				if(styleValue.length()!=0)  //if there is a style value
				{
//G***del Debug.trace("Found local style value: ", styleValue); //G***del
					final XMLCSSStyleDeclaration style=new XMLCSSStyleDeclaration(); //create a new style declaration
					final ParseReader localStyleReader=new ParseReader(styleValue, "Element "+elementName+" Local Style");	//create a string reader from the value of this local style attribute G***i18n
					parseRuleSet(localStyleReader, style); //read the style into our style declaration
					((XMLElement)element).setLocalCSSStyle(style);  //set the element's style to whatever we constructed G***eventually use a separate style tree instead of the element itself
				}
			}
		}
	}
*/

	/**Parses any stylesheets contained in or referenced from an XML document, as
		well as local style declarations in "style" attributes.
	@param document The XML document that that contains stylesheet information.
	@param sourceObject The source of the XML document (e.g. a String, File, or URL).
//G***fix all this exception stuff
	@except IOException Thrown when an i/o error occurs.
	@except ParseUnexpectedDataException Thrown when an unexpected character is found.
	@except ParseEOFException Thrown when the end of the input stream is reached unexpectedly.
	*/
/*G***move
	public void parseStyles(final XMLDocument document, final Object sourceObject) throws IOException, ParseUnexpectedDataException, ParseEOFException
	{
		parseExternalStyleSheets(document, sourceObject);	//parse any external stylesheets
		parseInternalStyleSheets(document);	//parse any internal stylesheets
		parseLocalStyles(document); //parse any local styles
	}
*/

	//G***couldn't a lot of the functions above be static?

	/**Determines the style of each element in the document based on the document's
		stylesheets as well as local element style attributes.
	*/
/*G***move
	public static void applyStyles(final XMLDocument document)
	{
			//apply all the styles in each stylesheet
		for(int styleSheetIndex=0; styleSheetIndex<document.getStyleSheetList().size(); ++styleSheetIndex)	//look at each of this document's stylesheets
		{
			if(document.getStyleSheetList().get(styleSheetIndex) instanceof XMLCSSStyleSheet)	//if this stylesheet is a CSS stylesheet (we only support CSS stylesheets in this implementation) G***change this comment if we move this function somewhere else where it doesn't apply
			{
				final XMLCSSStyleSheet cssStyleSheet=(XMLCSSStyleSheet)document.getStyleSheetList().get(styleSheetIndex);	//get a reference to this CSS stylesheet
				applyStyleSheet(document, cssStyleSheet);	//apply this stylesheet to the document
			}
		}
			//apply all the local styles G***fix to make more efficient
		final XMLNodeList elementList=(XMLNodeList)document.getElementsByTagName("*");	//get a list of all elements in this document G***maybe use a constant here
		for(int elementIndex=0; elementIndex<elementList.size(); ++elementIndex)	//look at each of the elements
		{
			final XMLElement element=(XMLElement)elementList.get(elementIndex);	//get the element at this index
		  final XMLCSSStyleDeclaration elementStyle=(XMLCSSStyleDeclaration)element.getCSSStyle();  //get a reference to this element's style G***eventually put the style tree somewhere else
		  final XMLCSSStyleDeclaration elementLocalStyle=(XMLCSSStyleDeclaration)element.getLocalCSSStyle();  //get a reference to this element's local style, if available G***eventually put the style tree somewhere else
		  if(elementLocalStyle!=null) //if this element has a local style
				elementStyle.importStyle(elementLocalStyle);	//import this element's local style the element's overall style
		}
	}
*/

	/**Determines the style of each element in the document based on the document's
		stylesheets as well as local element style attributes.
	*/
	//G***maybe put this in XMLCSSStyleSheet
/*G***move
	public static void applyStyleSheet(final XMLDocument document, final XMLCSSStyleSheet styleSheet)
	{
		  //G***this looks expensive -- the tree has to be walked first to gather the elements, then again to apply the styles
		final XMLNodeList elementList=(XMLNodeList)document.getElementsByTagName("*");	//get a list of all elements in this document G***maybe use a constant here
//G***del System.out.println("Looking at this many elements: "+elementList.size());
		for(int elementIndex=0; elementIndex<elementList.size(); ++elementIndex)	//look at each of the elements
		{
			final XMLElement element=(XMLElement)elementList.get(elementIndex);	//get the element at this index
			for(int ruleIndex=0; ruleIndex<styleSheet.getCssRules().getLength(); ++ruleIndex)	//look at each of our rules
			{
				if(styleSheet.getCssRules().item(ruleIndex).getType()==XMLCSSRule.STYLE_RULE)	//if this is a style rule G***fix for other rule types
				{
					final XMLCSSStyleRule styleRule=(XMLCSSStyleRule)styleSheet.getCssRules().item(ruleIndex);	//get a reference to this style rule in the stylesheet
					if(styleRule.appliesTo(element))	//if this style rule applies to this element
						((XMLCSSStyleDeclaration)element.getCSSStyle()).importStyle((XMLCSSStyleDeclaration)styleRule.getStyle());	//import this style's properties into the style of this element
				}
			}
		}
	}
*/

}