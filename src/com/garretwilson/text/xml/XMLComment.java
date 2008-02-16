package com.garretwilson.text.xml;

import static com.garretwilson.text.xml.XML.*;

/**A comment in an XML document.
@see XMLCharacterData
@see org.w3c.dom.Comment
*/
public class XMLComment extends XMLCharacterData implements org.w3c.dom.Comment
{
	/*Constructor which requires an owner document to be specified.
	@param ownerDocument The document which owns this node.
	*/
	public XMLComment(final XMLDocument ownerDocument)
	{
		super(XMLNode.COMMENT_NODE, ownerDocument);	//construct the parent class
//G***del		setNodeType(XMLNode.COMMENT_NODE);	//show that this is an XML comment
		setNodeName(COMMENT_NODE_NAME);	//set the appropriate name for this type of character data node
	}

	/**Constructor that specifies the content of the comment.
	@param ownerDocument The document which owns this node.
	@param characterData The character data for the comment.
	*/
	public XMLComment(final XMLDocument ownerDocument, final String characterData)
	{
		this(ownerDocument);	//do the default constructing
		setNodeValue(characterData);	//set the character data
	}

	/**Creates and returns a duplicate copy of this node. The clone has no parent.
	This function creates a "shallow" clone which does not contain clones of all
	child nodes. For the DOM version, see cloneNode().
	@return A duplicate copy of this node.
	@see XMLNode#cloneXMLNode
	@see XMLNode#cloneNode
	@see XMLNode#getParentXMLNode
	*/
	public Object clone()
	{
		return new XMLComment(getOwnerXMLDocument(), getNodeValue());	//create a new node with the same owner document and the same value
	}

}
