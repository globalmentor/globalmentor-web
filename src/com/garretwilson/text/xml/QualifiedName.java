package com.garretwilson.text.xml;

import java.net.URI;

import com.garretwilson.model.DefaultResource;

/**Represents a qualified name (QName) of an XML element, including the
 	namespace URI, the prefix, and the local name.
The reference URI indicates the concatenation of the namespace URI and the local name.
@author Garret Wilson
*/
public class QualifiedName extends DefaultResource
{
	/**The namespace URI.*/
	private final URI namespaceURI;

		/**@return The namespace URI.*/
		public URI getNamespaceURI() {return namespaceURI;}

	/**The prefix string, or <code>null</code> if there is no prefix.*/
	private final String prefix;

		/**@return The prefix string, or <code>null</code> if there is no prefix.*/
		public String getPrefix() {return prefix;}

	/**The local name.*/
	private final String localName;

		/**@return The local name.*/
		public String getLocalName() {return localName;}

	/**QName constructor.
	@param namespaceURI The namespace URI.
	@param qname The combined <var>prefix</var>:<var>localName</var> qualified name.
	*/	
	public QualifiedName(final URI namespaceURI, final String qname)
	{
		this(namespaceURI, XMLUtilities.getPrefix(qname), XMLUtilities.getLocalName(qname));	//split out the prefix and local name and call the full constructor
	}
	
	/**Full constructor.
	@param namespaceURI The namespace URI.
	@param prefix The prefix string, or <code>null</code> if there is no prefix.
	@param localName The local name.
	*/	
	public QualifiedName(final URI namespaceURI, final String prefix, final String localName)
	{
		super(URI.create(namespaceURI.toString()+localName.toString()));	//concatenate the namespace and the local name to yield the reference URI
		this.namespaceURI=namespaceURI;
		this.prefix=prefix;
		this.localName=localName;
	}
}
