package com.garretwilson.text.xml;

import static com.garretwilson.text.CharacterEncoding.*;

/**XML declaration in an XML document.
@see XMLNode
*/
public class XMLDeclaration extends XMLNode
{

	/**The XML version of this document.*/
	private String Version="";

		/**@return The XML version.*/
		public String getVersion() {return Version;}

		/**Sets the XML version.
		@param newVersion The new XML version.*/
		public void setVersion(String newVersion) {Version=newVersion;}

	/**The encoding of this document, which defaults to UTF-8.*/
	private String Encoding=UTF_8;

		/**@return The encoding.*/
		public String getEncoding() {return Encoding;}

		/**Sets the encoding.
		@param newEncoding The new encoding.*/
		public void setEncoding(String newEncoding) {Encoding=newEncoding;}

	/**Whether the document is standalone. Defaults to <code>false</code>.*/
	private boolean Standalone=false;

		/**@return Whether the document is standalone..*/
		public boolean isStandalone() {return Standalone;}

		/**Sets the whether the document is standalone.
		@param newStandalone Whether the document should be standalone.*/
		public void setStandalone(boolean newStandalone) {Standalone=newStandalone;}

	/*Constructor which requires an owner document to be specified.
	@param ownerDocument The document which owns this node.
	*/
	public XMLDeclaration(final XMLDocument ownerDocument)
	{
		super(XMLNode.XMLDECL_NODE, ownerDocument);	//construct the parent class
//G***what about specifying a constant name, here?
//G***del		setNodeType(XMLNode.XMLDECL_NODE);	//show that this is an XML declaration
	}

	/**Creates and returns a duplicate copy of this node. The clone has no parent.
	This function creates a "shallow" clone which does not contain clones of all
	child nodes. For the DOM version, see cloneNode().
	@return A duplicate copy of this node.
	@see XMLNode#cloneXMLNode
	@see XMLNode#cloneNode
	@see XMLNode#getParentXMLNode
	*/
/*G*** fix cloning
	public Object clone()
	{
		return new XMLCDATA(getOwnerDocument(), getNodeValue());	//create a new node with the same owner document and the same value
	}
*/


//G***fix all the DOM stuff

	/**The character data of the comment.*/
//G***del	private String Value="";

		/**DOM: Returns the value of the comment.
		@return The value of the comment.
		*/
//G***del		public String getNodeValue() throws DOMException {return Value;}

		/**DOM: Sets the value of the comment.
		@param nodeValue The new value for the comment.*/
//G***del		public void setNodeValue(String nodeValue) throws DOMException {Value=nodeValue;}


}
