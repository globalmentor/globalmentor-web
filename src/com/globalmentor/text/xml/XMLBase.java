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

package com.globalmentor.text.xml;

import java.net.URI;
import java.net.URISyntaxException;

import static com.globalmentor.text.xml.XMLUtilities.*;

import org.w3c.dom.*;

/**Class that allows discovery of a base URI according to XML Base,
<a href="http://www.w3.org/TR/xmlbase/">http://www.w3.org/TR/xmlbase/</a>.
@author Garret Wilson
*/
public class XMLBase
{
	/**The local name of the <code>xml:base</code> attribute.*/
	public final static String ATTRIBUTE_BASE="base";

	/**Retrieves the value of the <code>xml:base</code> attribute, if present.
	@param element The element for which the <code>xml:base</code> attribute value
		should be returned.
	@return The string value of the <code>xml:base</code> attribute, or
		<code>null</code> if the element has no such attribute.
	 */
	public static String getXMLBaseAttributeValue(final Element element)
	{
		return getDefinedAttributeNS(element, XML_NAMESPACE_URI.toString(), ATTRIBUTE_BASE);  //return the xml:base attribute value, if it exists
	}

	/**Retrieves the base URI of the given element by traversing up the element
		heirarchy to resolve the first specified <code>xml:base</code>. If no
		base URI is specified, this method returns <code>null</code>.
	@param element The element for which a base URI should be determined.
	@return The resolved base URI for the element according to XML Base, or
		<code>null</code> if a base URI is not specified.
	@exception URISyntaxException Thrown if the constructed URI is invalid.
	*/
	public static URI getBaseURI(final Element element) throws URISyntaxException
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
	@exception URISyntaxException Thrown if the constructed URI is invalid.
	*/
	public static URI getBaseURI(final Element element, final URI documentBaseURI) throws URISyntaxException
	{
		final String xmlBaseValue=getXMLBaseAttributeValue(element);  //get the element's xml:base attribute value
		final URI parentBaseURI; //determine the base URI of the parent, so that we can resolve relative URIs, if we need to
		final Node parentNode=element.getParentNode();  //get the element's parent node
		if(parentNode!=null && parentNode.getNodeType()==Node.ELEMENT_NODE) //if there is a parent element
		{
			parentBaseURI=getBaseURI((Element)parentNode, documentBaseURI); //get the base URI of the parent element
		}
		else  //if there is no parent node
			parentBaseURI=documentBaseURI;  //use the provided document base URI
		if(xmlBaseValue!=null)  //if the element has an xml:base attribute value
		{
			return parentBaseURI!=null ? parentBaseURI.resolve(xmlBaseValue) : new URI(xmlBaseValue);	//create a URI relative to the parent's base URI	
		}
		else  //if there is no xml:base attribute value
		{
			return parentBaseURI; //return the base URI of the parent, if we could find one
		}
	}

	/**Creates a URI by resolving the given URI relative to the base URI of the
		provided element.
	@param uri The URI to resolve.
	@param element The element for which a base URI should be determined.
	@return A URI resolved to the base URI of the document for the given element.
	@exception URISyntaxException Thrown if the constructed URI is invalid.
	*/
	public URI resolveURI(final URI uri, final Element element) throws URISyntaxException
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
	@exception URISyntaxException Thrown if the constructed URI is invalid.
	*/
	public static URI resolveURI(final URI uri, final Element element, final URI documentBaseURI) throws URISyntaxException
	{
		final URI baseURI=getBaseURI(element, documentBaseURI);  //get the base URI of the element
		  //resolve the given URI to the base URI we determine
		return baseURI!=null ? baseURI.resolve(uri) : uri;
	}

	/**Creates a URI by resolving the given string relative to the base URI of the
		provided element.
	@param string The string to resolve.
	@param element The element for which a base URI should be determined.
	@return A URI resolved to the base URI of the document for the given element.
	@exception URISyntaxException Thrown if the constructed URI is invalid.
	*/
	public URI resolveURI(final String string, final Element element) throws URISyntaxException
	{
		return resolveURI(string, element, null);  //resolve the URI without specifying a document base URI
	}

	/**Creates a URI by resolving the given string relative to the base URI of the
		provided element, taking into account the given base URI of the document.
	@param string The string to resolve.
	@param element The element for which a base URI should be determined.
	@param documentBaseURI The base URI of the document itself, or
		<code>null</code> if the base URI of the document is not known.
	@return A URI resolved to the base URI of the document for the given element.
	@exception URISyntaxException Thrown if the constructed URI is invalid.
	*/
	public static URI resolveURI(final String string, final Element element, final URI documentBaseURI) throws URISyntaxException
	{
		final URI baseURI=getBaseURI(element, documentBaseURI);  //get the base URI of the element
			//resolve the given URI to the base URI we determine
		return baseURI!=null ? baseURI.resolve(string) : new URI(string);
	}

}