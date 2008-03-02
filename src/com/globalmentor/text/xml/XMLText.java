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

import static com.globalmentor.text.xml.XMLUtilities.*;

import org.w3c.dom.*;

/**Character data in an XML document residing in an element or attribute.
XMLText contains no markup. Immediately after parsing, an element with no
markup will contain a single XMLText child with all character content. Other
XMLText children may be created but if these contain no markup they will be
normalized into one XMLText child before writing. It is recommended that
normalize() be called to merge adjacent XMLText children before employing
operations that depend on a particular document structure, such as navigation
with <code>XPointers</code>.
@author Garret Wilson
@see XMLCharacterData
@see XMLElement#normalize
@see org.w3c.dom.Text
@deprecated
*/
public class XMLText extends XMLCharacterData implements org.w3c.dom.Text
{
	/*Constructor which requires an owner document to be specified.
	@param ownerDocument The document which owns this node.
	*/
	public XMLText(final XMLDocument ownerDocument)
	{
		super(XMLNode.TEXT_NODE, ownerDocument);	//construct the parent class
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
/*TODO fix
		if(isEventsEnabled()) //if events are enabled TODO should this be here?
		{
			//create a new event for the inserted node
			final Event nodeInsertedEvent=XMLMutationEvent.createDOMNodeInsertedEvent(xmlNewChild, this);
			xmlNewChild.dispatchEvent(nodeInsertedEvent); //dispatch the event
			//TODO dispatch the document events
		}
*/
		return secondTextNode;	//return the second text node which contains the split data
	}

    /**
     * Returns whether this text node contains <a href='http://www.w3.org/TR/2004/REC-xml-infoset-20040204#infoitem.character'>
     * element content whitespace</a>, often abusively called "ignorable whitespace". The text node is 
     * determined to contain whitespace in element content during the load 
     * of the document or if validation occurs while using 
     * <code>Document.normalizeDocument()</code>.
     * @since DOM Level 3
     */
    public boolean isElementContentWhitespace() {throw new UnsupportedOperationException();}	//TODO fix for DOM 3

    /**
     * Returns all text of <code>Text</code> nodes logically-adjacent text 
     * nodes to this node, concatenated in document order.
     * <br>For instance, in the example below <code>wholeText</code> on the 
     * <code>Text</code> node that contains "bar" returns "barfoo", while on 
     * the <code>Text</code> node that contains "foo" it returns "barfoo". 
     * @since DOM Level 3
     */
    public String getWholeText() {throw new UnsupportedOperationException();}	//TODO fix for DOM 3

    /**
     * Replaces the text of the current node and all logically-adjacent text 
     * nodes with the specified text. All logically-adjacent text nodes are 
     * removed including the current node unless it was the recipient of the 
     * replacement text.
     * <br>This method returns the node which received the replacement text. 
     * The returned node is: 
     * <ul>
     * <li><code>null</code>, when the replacement text is 
     * the empty string;
     * </li>
     * <li>the current node, except when the current node is 
     * read-only;
     * </li>
     * <li> a new <code>Text</code> node of the same type (
     * <code>Text</code> or <code>CDATASection</code>) as the current node 
     * inserted at the location of the replacement.
     * </li>
     * </ul>
     * <br>For instance, in the above example calling 
     * <code>replaceWholeText</code> on the <code>Text</code> node that 
     * contains "bar" with "yo" in argument results in the following: 
     * <br>Where the nodes to be removed are read-only descendants of an 
     * <code>EntityReference</code>, the <code>EntityReference</code> must 
     * be removed instead of the read-only nodes. If any 
     * <code>EntityReference</code> to be removed has descendants that are 
     * not <code>EntityReference</code>, <code>Text</code>, or 
     * <code>CDATASection</code> nodes, the <code>replaceWholeText</code> 
     * method must fail before performing any modification of the document, 
     * raising a <code>DOMException</code> with the code 
     * <code>NO_MODIFICATION_ALLOWED_ERR</code>.
     * <br>For instance, in the example below calling 
     * <code>replaceWholeText</code> on the <code>Text</code> node that 
     * contains "bar" fails, because the <code>EntityReference</code> node 
     * "ent" contains an <code>Element</code> node which cannot be removed.
     * @param content The content of the replacing <code>Text</code> node.
     * @return The <code>Text</code> node created with the specified content.
     * @exception DOMException
     *   NO_MODIFICATION_ALLOWED_ERR: Raised if one of the <code>Text</code> 
     *   nodes being replaced is readonly.
     * @since DOM Level 3
     */
    public Text replaceWholeText(String content)
                                 throws DOMException {throw new UnsupportedOperationException();}	//TODO fix for DOM 3


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
		return new XMLText(getOwnerXMLDocument(), getNodeValue());	//create a new node with the same owner document and the same value
	}


}
