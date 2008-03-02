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
import com.garretwilson.model.URIAccessibleModel;
import com.globalmentor.io.URIInputStreamable;
import com.globalmentor.java.Java;

import org.w3c.dom.*;

/**An abstract model of an XML node.
<p>Bound properties:</p>
<dl>
	<dt><code>XML_PROPERTY</code> (<code>Node</code>)</dt>
	<dd>Indicates that the XML node property has been changed.</dd>
</dl>
@author Garret Wilson
@see #XML_PROPERTY
@deprecated
*/
public class XMLNodeModel<N extends Node> extends URIAccessibleModel
{

	/**The XML node property.*/
	public final String XML_PROPERTY=XMLNodeModel.class.getName()+Java.PACKAGE_SEPARATOR+"xml";

	/**The XML information being modeled, or <code>null</code> if there is no XML information.*/
	private N xml=null;

		/**@return The XML information being modeled, or <code>null</code> if there is no XML information.*/
		public N getXML() {return xml;}

		/**Sets the XML information being modeled.
		This is a bound property.
		@param newXML The XML information being modeled, or <code>null</code> if
			there is no XML information.
		*/
		public void setXML(final N newXML)
		{
			final N oldXML=xml; //get the old value
			if(oldXML!=newXML)  //if the value is really changing
			{
				xml=newXML; //update the value
					//show that the property has changed
				firePropertyChange(XML_PROPERTY, oldXML, newXML);
			}
		}


	/**Default constructor.*/
	public XMLNodeModel()
	{
		this((N)null);
	}
	
	/**XML constructor.
	@param xml The XML informatoin being modeled, or <code>null</code> if there is
		no XML information.
	*/
	public XMLNodeModel(final N xml)
	{
		this(xml, (URI)null);
	}
	
	/**Base URI constructor.
	@param baseURI The base URI of the model, or <code>null</code> if unknown.
	*/
	public XMLNodeModel(final URI baseURI)
	{
		this((N)null, baseURI);
	}
	
	/**XML and base URI constructor.
	@param xml The XML information being modeled, or <code>null</code> if there is
		no XML information.
	@param baseURI The base URI of the model, or <code>null</code> if unknown.
	*/
	public XMLNodeModel(final N xml, final URI baseURI)
	{
		this(xml, baseURI, null);
	}
	
	/**URI input stream locator constructor.
	@param uriInputStreamable The implementation to use for accessing a URI for
		input, or <code>null</code> if the default implementation should be used.
	*/
	public XMLNodeModel(final URIInputStreamable uriInputStreamable)
	{
		this((N)null, uriInputStreamable);
	}
	
	/**XML and URI input stream locator constructor.
	@param xml The XML information being modeled, or <code>null</code> if there is
		no XML information.
	@param uriInputStreamable The implementation to use for accessing a URI for
		input, or <code>null</code> if the default implementation should be used.
	*/
	public XMLNodeModel(final N xml, final URIInputStreamable uriInputStreamable)
	{
		this(xml, null, uriInputStreamable);
	}
	
	/**Base URI and input stream locator constructor.
	@param xml The XML information being modeled, or <code>null</code> if there is
		no XML information.
	@param baseURI The base URI of the model, or <code>null</code> if unknown.
	@param uriInputStreamable The implementation to use for accessing a URI for
		input, or <code>null</code> if the default implementation should be used.
	*/
	public XMLNodeModel(final URI baseURI, final URIInputStreamable uriInputStreamable)
	{
		this(null, baseURI, uriInputStreamable);
	}
	
	/**Full constructor.
	@param xml The XML information being modeled, or <code>null</code> if there is
		no XML information.
	@param baseURI The base URI of the model, or <code>null</code> if unknown.
	@param uriInputStreamable The implementation to use for accessing a URI for
		input, or <code>null</code> if the default implementation should be used.
	*/
	public XMLNodeModel(final N xml, final URI baseURI, final URIInputStreamable uriInputStreamable)
	{
		super(baseURI, uriInputStreamable);	//construct the parent class
		this.xml=xml;	//save the XML
	}

}
