package com.garretwilson.text.xml;

import java.net.URI;

import com.garretwilson.io.URIInputStreamable;
import com.garretwilson.lang.JavaConstants;
import com.garretwilson.model.URIAccessibleModel;
import org.w3c.dom.*;

/**A model of an XML document.
<p>Bound properties:</p>
<dl>
	<dt><code>XML_PROPERTY</code> (<code>Resource</code>)</dt>
	<dd>Indicates that the XML document property has been changed.</dd>
</dl>
@author Garret Wilson
@see #XML_PROPERTY
*/
public class XMLModel extends URIAccessibleModel
{
	/**The XML document property.*/
	public final String XML_PROPERTY=XMLModel.class.getName()+JavaConstants.PACKAGE_SEPARATOR+"xml";

	/**The XML document being modeled, or <code>null</code> if there is no XML document.*/
	private Document xml=null;

		/**@return The XML document being modeled, or <code>null</code> if there is no XML document.*/
		public Document getXML() {return xml;}

		/**Sets the XML document being modeled.
		This is a bound property.
		@param newXML The XML document being modeled, or <code>null</code> if
			there is no XML document.
		*/
		public void setXML(final Document newXML)
		{
			final Document oldXML=xml; //get the old value
			if(oldXML!=newXML)  //if the value is really changing
			{
				xml=newXML; //update the value
					//show that the property has changed
				firePropertyChange(XML_PROPERTY, oldXML, newXML);
			}
		}


	/**Default constructor.*/
	public XMLModel()
	{
		this((Document)null);
	}
	
	/**XML constructor.
	@param xml The XML document being modeled, or <code>null</code> if there is
		no XML document.
	*/
	public XMLModel(final Document xml)
	{
		this(xml, (URI)null);
	}
	
	/**Base URI constructor.
	@param baseURI The base URI of the model, or <code>null</code> if unknown.
	*/
	public XMLModel(final URI baseURI)
	{
		this((Document)null, baseURI);
	}
	
	/**XML and base URI constructor.
	@param xml The XML document being modeled, or <code>null</code> if there is
		no XML document.
	@param baseURI The base URI of the model, or <code>null</code> if unknown.
	*/
	public XMLModel(final Document xml, final URI baseURI)
	{
		this(xml, baseURI, null);
	}
	
	/**URI input stream locator constructor.
	@param uriInputStreamable The implementation to use for accessing a URI for
		input, or <code>null</code> if the default implementation should be used.
	*/
	public XMLModel(final URIInputStreamable uriInputStreamable)
	{
		this((Document)null, uriInputStreamable);
	}
	
	/**XML and URI input stream locator constructor.
	@param xml The XML document being modeled, or <code>null</code> if there is
		no XML document.
	@param uriInputStreamable The implementation to use for accessing a URI for
		input, or <code>null</code> if the default implementation should be used.
	*/
	public XMLModel(final Document xml, final URIInputStreamable uriInputStreamable)
	{
		this(xml, null, uriInputStreamable);
	}
	
	/**Base URI and input stream locator constructor.
	@param xml The XML document being modeled, or <code>null</code> if there is
		no XML document.
	@param baseURI The base URI of the model, or <code>null</code> if unknown.
	@param uriInputStreamable The implementation to use for accessing a URI for
		input, or <code>null</code> if the default implementation should be used.
	*/
	public XMLModel(final URI baseURI, final URIInputStreamable uriInputStreamable)
	{
		this(null, baseURI, uriInputStreamable);
	}
	
	/**Full constructor.
	@param xml The XML document being modeled, or <code>null</code> if there is
		no XML document.
	@param baseURI The base URI of the model, or <code>null</code> if unknown.
	@param uriInputStreamable The implementation to use for accessing a URI for
		input, or <code>null</code> if the default implementation should be used.
	*/
	public XMLModel(final Document xml, final URI baseURI, final URIInputStreamable uriInputStreamable)
	{
		super(baseURI, uriInputStreamable);	//construct the parent class
		this.xml=xml;	//save the XML
	}

}
