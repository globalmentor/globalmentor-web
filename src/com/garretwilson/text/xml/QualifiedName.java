package com.garretwilson.text.xml;

import java.net.URI;

import com.garretwilson.model.DefaultResource;

/**Represents a qualified name (QName) of an XML element, including the
 	namespace URI, the prefix, and the local name.
The reference URI indicates the concatenation of the namespace URI and the local name.
<p>This class does not currently use true URIs for namespaces, primarily because WebDAV uses a namespace ("DAV:") which is not a true URI.</p>
@author Garret Wilson
*/
public class QualifiedName extends DefaultResource
{

	/**The namespace URI.*/
	private final String namespaceURI;

		/**@return The namespace URI.*/
		public String getNamespaceURI() {return namespaceURI;}

	/**The namespace URI.*/
//TODO del or fix	private final URI namespaceURI;

		/**@return The namespace URI.*/
//	TODO del or fix		public URI getNamespaceURI() {return namespaceURI;}

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
	public QualifiedName(final String namespaceURI, final String qname)
	{
		this(namespaceURI, XMLUtilities.getPrefix(qname), XMLUtilities.getLocalName(qname));	//split out the prefix and local name and call the full constructor
	}
	
	/**Full constructor.
	@param namespaceURI The namespace URI.
	@param prefix The prefix string, or <code>null</code> if there is no prefix.
	@param localName The local name.
	*/	
	public QualifiedName(final String namespaceURI, final String prefix, final String localName)
	{
		super(createReferenceURI(namespaceURI, localName));	//create our reference URI by combining our namespace URI and our local name
		this.namespaceURI=namespaceURI;
		this.prefix=prefix;
		this.localName=localName;
	}

	/**Creates a reference URI from an XML namespace URI and a local name.
	@param namespaceURI The XML namespace URI used in the serialization.
	@param localName The XML local name used in the serialization.
	@return A reference URI constructed from the given namespace and local name.
	*/
	public static URI createReferenceURI(final URI namespaceURI, final String localName)
	{
		return createReferenceURI(namespaceURI.toString(), localName);	//concatenate the string version of the namespace URI and the local name
	}

	/**Creates a reference URI from an XML namespace and a local name.
	<p>This method was created specifically for WebDAV, which uses a namespace ("DAV:")
		which is not a true URI.
	@param namespace The XML namespace used in the serialization.
	@param localName The XML local name used in the serialization.
	@return A reference URI constructed from the given namespace and local name.
	*/
	public static URI createReferenceURI(final String namespace, final String localName)
	{
			//TODO beware local names that may be valid XML names but not valie URI characters---we should probably encode the local name
		return URI.create(namespace+localName);	//concatenate the namespace and the local name to yield the reference URI
	}

}