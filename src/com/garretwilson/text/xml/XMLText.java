package com.garretwilson.text.xml;

import java.io.*;
import java.util.*;
import org.w3c.dom.DOMException;
import org.w3c.dom.Text;
//G***del if not needed import com.garretwilson.lang.StringUtilities;
import com.garretwilson.util.Debug;

/**Character data in an XML document residing in an element or attribute.
XMLText contains no markup. Immediately after parsing, an element with no
markup will contain a single XMLText child with all character content. Other
XMLText children may be created but if these contain no markup they will be
normalized into one XMLText child before writing. It is recommended that
normalize() be called to merge adjacent XMLText children before employing
operations that depend on a particular document structure, such as navigation
with <code>XPointers</code>.

@see XMLCharacterData
@see XMLElement#normalize
@see org.w3c.dom.Text
*/
public class XMLText extends XMLCharacterData implements org.w3c.dom.Text
{
	/*Constructor which requires an owner document to be specified.
	@param ownerDocument The document which owns this node.
	*/
	public XMLText(final XMLDocument ownerDocument)
	{
		super(XMLNode.TEXT_NODE, ownerDocument);	//construct the parent class
//G***del		setNodeType(XMLNode.TEXT_NODE);	//show that this is a text node
		setNodeName(TEXT_NODE_NAME);	//set the appropriate name for this type of character data node
	}

	/*Constructor which requires a type to be specified. Used by descendant classes
	such as XMLCDATASection.
	@param nodeType The type of node this is.
	@param ownerDocument The document which owns this node.
	*/
	protected XMLText(final short nodeType, final XMLDocument ownerDocument)
	{
		super(nodeType, ownerDocument);	//construct the parent class
//G***del		setNodeType(XMLNode.TEXT_NODE);	//show that this is a text node
		setNodeName(TEXT_NODE_NAME);	//set the appropriate name for this type of character data node
	}


	/**Constructor that specifies the character content.
	@param ownerDocument The document which owns this node.
	@param characterData The character data for the node.
	*/
	XMLText(final XMLDocument ownerDocument, final String characterData)
	{
		super(XMLNode.TEXT_NODE, ownerDocument, characterData);	//do the default constructing
		setNodeName(TEXT_NODE_NAME);	//set the appropriate name for this type of character data node
//G***del		setNodeType(XMLNode.TEXT_NODE);	//show that this is a text node
//G***del		setData(characterData);	//set the character data
	}

	/**Returns the text of this node, which is the same as that returned by getNodeValue().
	This function overrides and is used by XMLNode.getText() to recursively return the text
	of all children of a node.
	@see Node#getText
	*/
	public String getText()
	{
		return getNodeValue();	//return the text of this node, which is the node value of this text node
	}

	/**Breaks this <code>XMLText</code> node into two text nodes at the specified
	offset, keeping both in the tree as siblings. This node then only contains all
	the content up to the <code>offset</code> point. A new <code>XMLText</code>
	node, which is inserted as the next sibling of this node, contains all the
	content at and after the <code>offset</code> point.
	@param offset The 16-bit unit offset at which to split, starting from 0.
	@return The new <code>XMLText</code> node.
	@exception DOMException
	<ul>
		<li>INDEX_SIZE_ERR: Raised if the specified offset is negative or greater
			than the number of 16-bit units in <code>data</code>.</li>
		<li>NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly.</li>
	</ul>
	@version DOM Level 1
	*/
	public Text splitText(int offset) throws DOMException
	{
		final String secondCharacterData=substringData(offset, getLength()-offset);	//determine the characters to be used for the second text node (this will check to make sure offset is valid)
		deleteData(offset, getLength()-offset);	//delete the text that we found from our node; this will make sure modifications are allowed, and throw the appropriate exception if not
		final XMLText secondTextNode=new XMLText(getOwnerXMLDocument(), secondCharacterData);	//create the second text node
		final XMLNodeList parentNodeList=getParentXMLNode().getChildXMLNodeList();	//get the list of child nodes
		final int thisIndex=parentNodeList.indexOf(this);	//find our index in our list of siblings
		parentNodeList.add(thisIndex+1, secondTextNode);	//add the second text node to our list of siblings, making sure it comes right after us
		secondTextNode.setParentXMLNode(getParentXMLNode());	//set the parent of the added child
/*G***fix
		if(isEventsEnabled()) //if events are enabled G***should this be here?
		{
			//create a new event for the inserted node
			final Event nodeInsertedEvent=XMLMutationEvent.createDOMNodeInsertedEvent(xmlNewChild, this);
			xmlNewChild.dispatchEvent(nodeInsertedEvent); //dispatch the event
			//G***dispatch the document events
		}
*/
		return secondTextNode;	//return the second text node which contains the split data
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
//G***del Debug.trace(this, "clone() text: "+getText()); //G***del
		return new XMLText(getOwnerXMLDocument(), getNodeValue());	//create a new node with the same owner document and the same value
	}


}
