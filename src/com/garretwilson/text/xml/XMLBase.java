package com.garretwilson.text.xml;

import java.net.MalformedURLException;
import java.net.URL;
import com.garretwilson.net.URLUtilities;
import com.garretwilson.util.Debug;
import org.w3c.dom.*;

/**Class that allows discovery of a base URI according to XML Base,
<a href="http://www.w3.org/TR/xmlbase/">http://www.w3.org/TR/xmlbase/</a>.
Currently this class processes all URIs as URLs.
@author Garret Wilson
*/
public class XMLBase implements XMLBaseConstants
{

	/**Retrieves the value of the <code>xml:base</code> attribute, if present.
	@param element The element for which the <code>xml:base</code> attribute value
		should be returned.
	@return The string value of the <code>xml:base</code> attribute, or
		<code>null</code> if the element has no such attribute.
	 */
	public static String getXMLBaseAttributeValue(final Element element)
	{
		return XMLUtilities.getDefinedAttributeNS(element, XML_NAMESPACE_URI, ATTRIBUTE_BASE);  //return the xml:base attribute value, if it exists
	}

	/**Retrieves the base URI of the given element by traversing up the element
		heirarchy to resolve the first specified <code>xml:base</code>. If no
		base URI is specified, this method returns <code>null</code>.
	@param element The element for which a base URI should be determined.
	@return The resolved base URI for the element according to XML Base, or
		<code>null</code> if a base URI is not specified.
	@exception MalformedURLException Thrown if the constructed URI is invalid.
	*/
	public static String getBaseURI(final Element element) throws MalformedURLException
	{
		return getBaseURI(element, null); //get the base URI without specifying a base URI for the document
	}

	/**Retrieves the base URI of the given element by traversing up the element
		heirarchy to resolve the first specified <code>xml:base</code>. The given
		URI is considered to be the base URI of the document, and will be used to
		resolve fragment base URIs or will be returned if no base URI is specified.
	@param element The element for which a base URI should be determined.
	@param documentBaseURI The base URI of the document itself, or
		<code>null</code> if the base URI of the document is not known.
	@return The resolved base URI for the element according to XML Base, or
		the document base URI if a base URI is not specified.
	@exception MalformedURLException Thrown if the constructed URI is invalid.
	*/
	public static String getBaseURI(final Element element, final String documentBaseURI) throws MalformedURLException
	{
Debug.trace("Getting base URI for element: ", element.getNodeName()); //G***del
		final String xmlBaseValue=getXMLBaseAttributeValue(element);  //get the element's xml:base attribute value
Debug.trace("xml:base: ", xmlBaseValue); //G***del

		final String parentBaseURI; //determine the base URI of the parent, so that we can resolve relative URIs, if we need to
		final Node parentNode=element.getParentNode();  //get the element's parent node
		if(parentNode!=null && parentNode.getNodeType()==Node.ELEMENT_NODE) //if there is a parent element
		{
			parentBaseURI=getBaseURI((Element)parentNode, documentBaseURI); //get the base URI of the parent element
		}
		else  //if there is no parent node
			parentBaseURI=documentBaseURI;  //use the provided document base URI
		if(xmlBaseValue!=null)  //if the element has an xml:base attribute value
		{
Debug.trace("found xml base value, creating URL with parent: ", parentBaseURI); //G***del
			return URLUtilities.createURL(parentBaseURI!=null ? new URL(parentBaseURI) : null, xmlBaseValue).toString(); //create a URI relative to the base URI G***fix to use URIs instead of URLs
		}
		else  //if there is no xml:base attribute value
		{
			return parentBaseURI; //return the base URI of the parent, if we could find one
/*G***del
			final Node parentNode=element.getParentNode();  //get the element's parent node
			if(parentNode!=null && parentNode.getNodeType()==Node.ELEMENT_NODE) //if there is a parent element
			{
				return getBaseURI((Element)parentNode); //get the base URI of the parent element
			}
			else  //if there is no parent node
				return null;  //show that there's no XML base to be found
*/
		}
	}

	/**Creates a URI by resolving the given URI relative to the base URI of the
		provided element.
	@param uri The URI to resolve.
	@param element The element for which a base URI should be determined.
	@return A URI resolved to the base URI of the document for the given element.
	@exception MalformedURLException Thrown if the constructed URI is invalid.
	*/
	public static String resolveURI(final String uri, final Element element) throws MalformedURLException
	{
	  return resolveURI(uri, element, null);  //resolve the URI without specifying a document base URI
	}

	/**Creates a URI by resolving the given URI relative to the base URI of the
		provided element, taking into account the given base URI of the document.
	@param uri The URI to resolve.
	@param element The element for which a base URI should be determined.
	@param documentBaseURI The base URI of the document itself, or
		<code>null</code> if the base URI of the document is not known.
	@return A URI resolved to the base URI of the document for the given element.
	@exception MalformedURLException Thrown if the constructed URI is invalid.
	*/
	public static String resolveURI(final String uri, final Element element, final String documentBaseURI) throws MalformedURLException
	{
		final String baseURI=getBaseURI(element, documentBaseURI);  //get the base URI of the element
		  //resolve the given URI to the base URI we determine
		return URLUtilities.createURL(baseURI!=null ? new URL(baseURI) : null, uri).toString();
	}

}