/*
 * Copyright © 1996-2008 GlobalMentor, Inc. <https://www.globalmentor.com/>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.globalmentor.xml.dom.impl;

import static com.globalmentor.xml.def.XML.*;

import org.w3c.dom.*;

/**
 * Character data in an XML document residing in an element or attribute. XMLText contains no markup. Immediately after parsing, an element with no markup will
 * contain a single XMLText child with all character content. Other XMLText children may be created but if these contain no markup they will be normalized into
 * one XMLText child before writing. It is recommended that normalize() be called to merge adjacent XMLText children before employing operations that depend on
 * a particular document structure, such as navigation with <code>XPointers</code>.
 * @author Garret Wilson
 * @see XMLCharacterData
 * @see XMLElement#normalize
 * @see org.w3c.dom.Text
 * @deprecated
 */
@Deprecated
public class XMLText extends XMLCharacterData implements org.w3c.dom.Text {

	/**
	 * Constructor which requires an owner document to be specified.
	 * @param ownerDocument The document which owns this node.
	 */
	public XMLText(final XMLDocument ownerDocument) {
		super(XMLNode.TEXT_NODE, ownerDocument); //construct the parent class
		setNodeName(TEXT_NODE_NAME); //set the appropriate name for this type of character data node
	}

	/**
	 * Constructor which requires a type to be specified. Used by descendant classes such as XMLCDATASection.
	 * @param nodeType The type of node this is.
	 * @param ownerDocument The document which owns this node.
	 */
	protected XMLText(final short nodeType, final XMLDocument ownerDocument) {
		super(nodeType, ownerDocument); //construct the parent class
		setNodeName(TEXT_NODE_NAME); //set the appropriate name for this type of character data node
	}

	/**
	 * Constructor that specifies the character content.
	 * @param ownerDocument The document which owns this node.
	 * @param characterData The character data for the node.
	 */
	XMLText(final XMLDocument ownerDocument, final String characterData) {
		super(XMLNode.TEXT_NODE, ownerDocument, characterData); //do the default constructing
		setNodeName(TEXT_NODE_NAME); //set the appropriate name for this type of character data node
	}

	@Override
	public String getText() {
		return getNodeValue(); //return the text of this node, which is the node value of this text node
	}

	@Override
	public Text splitText(int offset) throws DOMException {
		final String secondCharacterData = substringData(offset, getLength() - offset); //determine the characters to be used for the second text node (this will check to make sure offset is valid)
		deleteData(offset, getLength() - offset); //delete the text that we found from our node; this will make sure modifications are allowed, and throw the appropriate exception if not
		final XMLText secondTextNode = new XMLText(getOwnerXMLDocument(), secondCharacterData); //create the second text node
		final XMLNodeList parentNodeList = getParentXMLNode().getChildXMLNodeList(); //get the list of child nodes
		final int thisIndex = parentNodeList.indexOf(this); //find our index in our list of siblings
		parentNodeList.add(thisIndex + 1, secondTextNode); //add the second text node to our list of siblings, making sure it comes right after us
		secondTextNode.setParentXMLNode(getParentXMLNode()); //set the parent of the added child
		/*TODO fix
				if(isEventsEnabled()) {	//if events are enabled TODO should this be here?
					//create a new event for the inserted node
					final Event nodeInsertedEvent=XMLMutationEvent.createDOMNodeInsertedEvent(xmlNewChild, this);
					xmlNewChild.dispatchEvent(nodeInsertedEvent); //dispatch the event
					//TODO dispatch the document events
				}
		*/
		return secondTextNode; //return the second text node which contains the split data
	}

	@Override
	public boolean isElementContentWhitespace() {
		throw new UnsupportedOperationException();
	} //TODO fix for DOM 3

	@Override
	public String getWholeText() {
		throw new UnsupportedOperationException();
	} //TODO fix for DOM 3

	@Override
	public Text replaceWholeText(String content) throws DOMException {
		throw new UnsupportedOperationException();
	} //TODO fix for DOM 3

	/**
	 * Creates and returns a duplicate copy of this node. The clone has no parent. This function creates a "shallow" clone which does not contain clones of all
	 * child nodes. For the DOM version, see cloneNode().
	 * @return A duplicate copy of this node.
	 * @see XMLNode#cloneXMLNode
	 * @see XMLNode#cloneNode
	 * @see XMLNode#getParentXMLNode
	 */
	@Override
	public Object clone() {
		return new XMLText(getOwnerXMLDocument(), getNodeValue()); //create a new node with the same owner document and the same value
	}

}
