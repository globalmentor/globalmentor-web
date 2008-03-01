package com.garretwilson.text.xml.stylesheets.css;

import java.net.URI;
import java.io.*;
import java.util.*;
import com.garretwilson.io.*;
import com.garretwilson.text.xml.XMLUtilities;
import com.globalmentor.util.NameValuePair;

import org.w3c.dom.*;
import org.w3c.dom.css.*;
import org.w3c.dom.stylesheets.*;

/**Applies styles to XML elements.
@author Garret Wilson
*/
public class XMLCSSStylesheetApplier extends AbstractXMLCSSStylesheetApplier<Document, Element> implements CSSStyleManager //G***do we really need this style manager interaface 
{
	
		//TODO maybe move all the map storage stuff to the abstract version

	/**The map of styles, each keyed to an element. The weak map allows the styles
		to be garbage-collected once the corresponding element is no longer used.
	*/
	protected final Map<Element, CSSStyleDeclaration> cssStyleMap=new WeakHashMap<Element, CSSStyleDeclaration>();  //G***fix entire pattern to be much more general and robust

		/**@return The map of styles, each keyed to an element.*/
		protected Map<Element, CSSStyleDeclaration> getCSSStyleMap() {return cssStyleMap;}

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
			return cssStyleMap.get(element);  //return the style, if any, stored keyed to the element
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
	
		/**Clears all the cached element styles.*/
		public void clearStyles()
		{
			cssStyleMap.clear();	//clear the map of styles
		}

	/**The source of input streams.*/
	private final URIInputStreamable uriInputStreamable;

		/**Returns an input stream for the given URI.
		<p>The calling class has the responsibility for closing the input stream.</p>
		<p>This implementation delegates to the object provided at construction.</p>
		@param uri A URI to a resource.
		@return An input stream to the contents of the resource represented by the given URI.
		@exception IOException Thrown if an I/O error occurred.
		*/
		public InputStream getInputStream(final URI uri) throws IOException
		{
			return uriInputStreamable.getInputStream(uri);	//delegate to the provided URIInputStreamable
		}

	/**Constructs a stylesheet applier for DOM XML documents.
	@param uriInputStreamable The source of input streams.
	*/
	public XMLCSSStylesheetApplier(final URIInputStreamable uriInputStreamable)
	{
		this.uriInputStreamable=uriInputStreamable;	//save the URIInputStreamable
	}

	/**Returns the object that represents the root element of the given document.
	@param document The object representing the XML document.
	@return The object representing the root element of the XML document.
	*/
	protected Element getDocumentElement(final Document document)
	{
		return(document.getDocumentElement());	//return the root element of the document
	}

	/**Retrieves processing instructions from the given document.
	@param document The document that might contain XML processing instructions.
	@return A non-<code>null</code> array of name-value pairs representing
		processing instructions.
	*/
	protected NameValuePair[] getDocumentProcessingInstructions(final Document document)
	{
		final List processingInstructionList=XMLUtilities.getNodesByName(document, Node.PROCESSING_INSTRUCTION_NODE, "*", false);	//get a list of all the processing instructions in the document TODO use a constant for the wildcard
		final NameValuePair[] processingInstructions=new NameValuePair[processingInstructionList.size()];	//create an array large enough to hold all the processing instructions
		for(int i=0; i<processingInstructionList.size(); ++i)	//look at each of the nodes representing a style sheet link
		{
			final ProcessingInstruction processingInstruction=(ProcessingInstruction)processingInstructionList.get(i);	//get a reference to this processing instruction
			processingInstructions[i]=new NameValuePair(processingInstruction.getNodeName(), processingInstruction.getNodeValue());	//creaet a name-value pair with the name and value of the processing instruction
		}
		return processingInstructions;	//return the processing instructions we found
	}

	/**Retrieves the namespace URI of the given element.
	@param element The element for which the namespace URI should be returned.
	@return The namespace URI of the given element.
	*/
	protected String getElementNamespaceURI(final Element element)
	{
		return element instanceof Element ? element.getNamespaceURI() : null;	//return the element's namespace URI
	}

	/**Retrieves the local name of the given element.
	@param element The element for which the local name should be returned.
	@return The local name of the given element.
	*/
	protected String getElementLocalName(final Element element)
	{
			//TODO fix all this---we don't properly distinguish between elements and nodes
		return element instanceof Element ? element.getLocalName() : null;	//return the element's local name	//G***fix
	}
	
	/**Retrieves the value of one of the element's attributes.
	@param element The element owner of the attributes.
	@param attributeNamespaceURI The namespace of the attribute to find.
	@param attributeLocalName The local name of the attribute to find.
	@return The value of the specified attribute, or <code>null</code> if there
		is no such attribute.
	*/
	protected String getElementAttributeValue(final Element element, final String attributeNamespaceURI, final String attributeLocalName)
	{
		return element instanceof Element ? XMLUtilities.getDefinedAttributeNS(element, attributeNamespaceURI, attributeLocalName) : null;	//return the attribute value only if it is defined
	}
	
	/**Retrieves the parent element for the given element.
	@param element The element for which a parent should be found.
	@return The element's parent, or <code>null</code> if no parent could be found.
	 */
	protected Element getParentElement(final Element element)
	{
		final Node parentNode=element.getParentNode();	//get this element's parent node
		if(parentNode!=null)	//if there is a parent element
		{
			if(parentNode.getNodeType()==Node.ELEMENT_NODE)	//if the parent is an element
			{
				return (Element)parentNode;	//return the parent element
			}
		}
		return null;	//either we didn't find a parent node, or it wasn't an element
	}

	/**Determines the number of child nodes the given element has.
	@param element The parent element.
	@return The number of child elements this element has.
	*/
	protected int getChildCount(final Element element)
	{
		return element.getChildNodes().getLength();	//return the number of child nodes
	}

	/**Determines if the given indexed child of an element is an element.
	@param element The parent element.
	@param index The zero-based index of the child.
	@return <code>true</code> if the the child of the element at the given index is an element.
	*/
	protected boolean isChildElement(final Element element, final int index)
	{
		return element.getChildNodes().item(index).getNodeType()==Node.ELEMENT_NODE;	//see if this is an element node
	}

	/**Retrieves the given indexed child of an element.
	@param element The parent element.
	@param index The zero-based index of the child.
	@return The child of the element at the given index.
	*/
	protected Element getChildElement(final Element element, final int index)
	{
		return (Element)element.getChildNodes().item(index);	//return the child element at the given index
	}

	/**Retrieves all child text of the given element.
	@param element The element for which text should be returned.
	@return The text content of the element.
	*/
	protected String getElementText(final Element element)
	{
		return XMLUtilities.getText(element, true);	//return all the child text of the element
	}
	
	/**Imports style information into that already gathered for the given element.
	@param element The element for which style information should be imported
	@param cssStyle The style information to import.	
	*/
	protected void importCSSStyle(final Element element, final CSSStyleDeclaration cssStyle)
	{
		CSSStyleDeclaration elementStyle=getStyle(element);	//get this element's style
		if(elementStyle==null) //if there is no existing style
		{
			elementStyle=new XMLCSSStyleDeclaration();  //create an empty default style TODO use standard DOM classes if we can
			setStyle(element, elementStyle); //set the element style to the new one we created
		}
//G***del					Debug.trace("style rule is of type: ", cssStyleRule.getClass().getName());  //G***del
		importStyle(elementStyle, cssStyle);	//import the style
	}
}