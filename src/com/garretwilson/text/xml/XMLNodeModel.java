package com.garretwilson.text.xml;

import java.net.URI;
import com.garretwilson.io.URIInputStreamable;
import com.garretwilson.lang.JavaConstants;
import com.garretwilson.model.URIAccessibleModel;
import org.w3c.dom.*;

/**An abstract model of an XML node.
<p>Bound properties:</p>
<dl>
	<dt><code>XML_PROPERTY</code> (<code>Node</code>)</dt>
	<dd>Indicates that the XML node property has been changed.</dd>
</dl>
@author Garret Wilson
@see #XML_PROPERTY
*/
public abstract class XMLNodeModel extends URIAccessibleModel
{

	/**The XML node property.*/
	public final String XML_PROPERTY=XMLNodeModel.class.getName()+JavaConstants.PACKAGE_SEPARATOR+"xml";

	/**The XML information being modeled, or <code>null</code> if there is no XML information.*/
	private Node xml=null;

		/**@return The XML information being modeled, or <code>null</code> if there is no XML information.*/
		public Node getXML() {return xml;}

		/**Sets the XML information being modeled.
		This is a bound property.
		@param newXML The XML information being modeled, or <code>null</code> if
			there is no XML information.
		*/
		public void setXML(final Node newXML)
		{
			final Node oldXML=xml; //get the old value
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
		this((Node)null);
	}
	
	/**XML constructor.
	@param xml The XML informatoin being modeled, or <code>null</code> if there is
		no XML information.
	*/
	public XMLNodeModel(final Node xml)
	{
		this(xml, (URI)null);
	}
	
	/**Base URI constructor.
	@param baseURI The base URI of the model, or <code>null</code> if unknown.
	*/
	public XMLNodeModel(final URI baseURI)
	{
		this((Node)null, baseURI);
	}
	
	/**XML and base URI constructor.
	@param xml The XML information being modeled, or <code>null</code> if there is
		no XML information.
	@param baseURI The base URI of the model, or <code>null</code> if unknown.
	*/
	public XMLNodeModel(final Node xml, final URI baseURI)
	{
		this(xml, baseURI, null);
	}
	
	/**URI input stream locator constructor.
	@param uriInputStreamable The implementation to use for accessing a URI for
		input, or <code>null</code> if the default implementation should be used.
	*/
	public XMLNodeModel(final URIInputStreamable uriInputStreamable)
	{
		this((Node)null, uriInputStreamable);
	}
	
	/**XML and URI input stream locator constructor.
	@param xml The XML information being modeled, or <code>null</code> if there is
		no XML information.
	@param uriInputStreamable The implementation to use for accessing a URI for
		input, or <code>null</code> if the default implementation should be used.
	*/
	public XMLNodeModel(final Node xml, final URIInputStreamable uriInputStreamable)
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
	public XMLNodeModel(final Node xml, final URI baseURI, final URIInputStreamable uriInputStreamable)
	{
		super(baseURI, uriInputStreamable);	//construct the parent class
		this.xml=xml;	//save the XML
	}

}
