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

package com.globalmentor.xml.dom.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.w3c.dom.*;
import org.w3c.dom.events.*;

import com.globalmentor.java.Objects;
import com.globalmentor.xml.XMLNamedObject;
import com.globalmentor.xml.dom.impl.events.*;
import com.globalmentor.xml.spec.XML;

/**
 * The class which forms a basis for all other XML document classes.
 * @author Garret Wilson
 * @see XMLNodeList
 * @deprecated
 */
public abstract class XMLNode extends XMLNamedObject implements Node, EventTarget, Cloneable {

	/** The type of node before it is defined. This value is not defined in DOM. */
	public static final short UNDEFINED_NODE = 0;
	/** A tag node, which is a pseudo node created temporarily. This value is not defined in DOM. */
	public static final short TAG_NODE = -1;
	/** An XML declaration node. This value is not defined in DOM. */
	public static final short XMLDECL_NODE = -2;

	/**
	 * Returns whether events are enabled. If this node belongs to a document, the document is queried to determined if events should be enabled. If this node
	 * does not belong to a document, <code>true</code> is returned.
	 * @return Whether events are enabled for this node.
	 * @see XMLDocument#isEventsEnabled
	 * @see XMLDocument#setEventsEnabled
	 */
	public boolean isEventsEnabled() {
		return getOwnerDocument() != null ? ((XMLDocument)getOwnerDocument()).isEventsEnabled() : true;
	}

	/**
	 * The map of lists of event listeners. Each element in the map is keyed to the name of a type of event, and the value contains a <code>List</code> of
	 * listeners for that type of event.
	 */
	private Map eventListenerListMap = null;

	/** @return The map of lists of event listeners, creating one if necessary. */
	private Map getEventListenerListMap() {
		if(eventListenerListMap == null) //if there is no event listener list map
			eventListenerListMap = new HashMap(); //create a map
		return eventListenerListMap; //return the map
	}

	/**
	 * The map of lists of capture event listeners which have specified that they wish to capture events sent to any descendants of this node. Each element in the
	 * map is keyed to the name of a type of event, and the value contains a <code>List</code> of listeners for that type of event.
	 */
	private Map captureEventListenerListMap = null;

	/** @return The map of lists of capture event listeners, creating one if necessary. */
	private Map getCaptureEventListenerListMap() {
		if(captureEventListenerListMap == null) //if there is no capture event listener list map
			captureEventListenerListMap = new HashMap(); //create a map
		return captureEventListenerListMap; //return the map
	}

	/**
	 * Constructor which requires an owner document to be specified. Certain derived objects, such as <code>XMLDocument</code>, may set this to <code>null</code>.
	 * @param nodeType The type of node this is.
	 * @param ownerDocument The document which owns this node.
	 * @see XMLDocument
	 */
	public XMLNode(final short nodeType, final XMLDocument ownerDocument) {
		super(null, null); //construct an XML named object with no namespace or qualified name
		NodeType = nodeType; //set the type of node this is
		OwnerDocument = ownerDocument; //set the owner document
	}

	/** The type of XML node this is. */
	//TODO probably delete the default value
	private short NodeType = UNDEFINED_NODE;

	@Override
	public short getNodeType() {
		return NodeType;
	}

	@Override
	public String getNodeName() {
		return getQName();
	}

	/**
	 * Sets the name of this this node. This is <em>not</em> a DOM function, but is provided as protected access so that specific nodes can set their name.
	 * @param newName The new name of the node.
	 * @see #setNodeName
	 * @see #getNodeType
	 */
	//TODO we now set the qname, which has side effects of updating the prefix and local name---make sure this doesn't cause problems
	protected void setNodeName(final String newName) {
		if(newName != null) { //TODO fix to make sure the qname is null if newName is null
			setQName(newName);
		}
	}

	/**
	 * Sets the prefix and local name of this this node, updating the node name as the correct combination of the two. This function can only be called by other
	 * classes in this package, such as <code>XMLNamespaceProcessor</code>.
	 * @param newPrefix The namespace prefix of the node, or <code>null</code> for no prefix.
	 * @param newLocalName The node's local name.
	 * @throws DOMException
	 *           <ul>
	 *           <li>NAMESPACE_ERR: Raised if the specified <code>prefix</code> is malformed, if the <code>namespaceURI</code> of is <code>null</code>, if the
	 *           specified prefix is "xml" and the <code>namespaceURI</code> of this node is different from "http://www.w3.org/XML/1998/namespace", if this node
	 *           is an attribute and the specified prefix is "xmlns" and the <code>namespaceURI</code> of this node is different from
	 *           "http://www.w3.org/2000/xmlns/", or if this node is an attribute and the <code>qualifiedName</code> of this node is "xmlns".</li>
	 *           </ul>
	 * @see #setNodeName
	 * @see #getNodeType
	 * @see #setNodeNameNS
	 */
	protected void setNodeName(final String newPrefix, final String newLocalName) {
		//TODO fix to throw DOM exceptions if we need to
		setName(newPrefix, newLocalName); //set the name and prefix
	}

	/**
	 * Sets the name of this this node, correctly extracting namespace information. This is <em>not</em> a DOM function, but is provided as protected access so
	 * that specific nodes can set their name. This function parses the name and sets name properties as follows:
	 * <table>
	 * <caption>Determination of note attributes from parameter values.</caption>
	 * <tr>
	 * <th>Attribute</th>
	 * <th>Value</th>
	 * </tr>
	 * <tr>
	 * <td><code>Node.nodeName</code></td>
	 * <td><code>qualifiedName</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>Node.prefix</code></td>
	 * <td>prefix, extracted from <code>qualifiedName</code>, or <code>null</code> if there is no prefix</td>
	 * </tr>
	 * <tr>
	 * <td><code>Node.localName</code></td>
	 * <td>local name, extracted from <code>qualifiedName</code></td>
	 * </tr>
	 * </table>
	 * @param qualifiedName The qualified name of the node.
	 * @throws DOMException
	 *           <ul>
	 *           <li>NAMESPACE_ERR: Raised if the specified <code>prefix</code> is malformed, if the <code>namespaceURI</code> of is <code>null</code>, if the
	 *           specified prefix is "xml" and the <code>namespaceURI</code> of this node is different from "http://www.w3.org/XML/1998/namespace", if this node
	 *           is an attribute and the specified prefix is "xmlns" and the <code>namespaceURI</code> of this node is different from
	 *           "http://www.w3.org/2000/xmlns/", or if this node is an attribute and the <code>qualifiedName</code> of this node is "xmlns".</li>
	 *           </ul>
	 * @see #setNodeName
	 * @see #getNodeType
	 */
	protected void setNodeNameNS(final String qualifiedName) {
		//TODO fix to throw DOM exceptions if we need to
		setQName(qualifiedName); //set the qualified name of the node
	}

	@Override
	public String getNodeValue() throws DOMException {
		return null;
	}

	@Override
	public void setNodeValue(String nodeValue) throws DOMException {
	}

	/** The parent of this node. */
	private XMLNode ParentNode = null;

	/**
	 * Returns the parent of this node, or <code>null</code> if this type of node does not have a parent, or if the node has not been added to the node tree or
	 * has been removed from the node tree. For the DOM version, see <code>getParentNode</code>.
	 * @return The parent of this node, or <code>null</code> if there is no parent.
	 * @see XMLNode#getParentNode
	 */
	public XMLNode getParentXMLNode() {
		return ParentNode;
	}

	@Override
	public Node getParentNode() {
		return getParentXMLNode();
	}

	/**
	 * Sets the parent of this node.
	 * @param parentNode The new parent of this node, or <code>null</code> if this node does not have a parent, or if the node has not been added to the node tree
	 *          or has been removed from the node tree.
	 * @see XMLNode#getParentNode
	 * @see XMLNode#getParentXMLNode
	 */
	protected void setParentXMLNode(final XMLNode parentNode) {
		ParentNode = parentNode;
	}

	/**
	 * The list of child nodes in this node.
	 * @see XMLNode
	 * @see XMLNodeList
	 */
	private XMLNodeList ChildNodeList = new XMLNodeList();

	/**
	 * Returns the <code>XMLNodeList</code> that contains all children of this node. If there are no children, an <code>XNodeList</code> containing no nodes will
	 * be returned. For the DOM version, see <code>getXMLNodeList</code>.
	 * @return The list of child nodes in this node.
	 * @see XMLNode
	 * @see XMLNodeList
	 * @see org.w3c.dom.NodeList
	 * @see XMLNode#getChildNodes
	 */
	public XMLNodeList getChildXMLNodeList() {
		return ChildNodeList;
	}

	/**
	 * Sets the children of this node. Iterates through the list of nodes and ensures each parent property is properly set to this node.
	 * @param childNodeList The node list containing the children for this node.
	 */
	private void setChildXMLNodeList(final XMLNodeList childNodeList) {
		ChildNodeList = childNodeList; //assign the list of child nodes to ourselves
		final int childNodeCount = childNodeList.getLength(); //find out how many nodes are in the list
		for(int i = 0; i < childNodeCount; ++i)
			//look at each node
			((XMLNode)childNodeList.item(i)).setParentXMLNode(this); //ensure this child has the correct parent
	}

	@Override
	public NodeList getChildNodes() {
		return getChildXMLNodeList();
	}

	/**
	 * Specifies whether or not a particular node can be added as a child of this node. This allows all node functionality to be included in XMLNode, while
	 * allowing derived node types to simply override this function to specify if a particular type of node can be added. Nodes that do not support child nodes
	 * can keep this default implementation, which returns <code>false</code>.
	 * @param node The node to be verified.
	 * @return Whether or not the specified node can be added to the list of children.
	 * @see XMLNode#checkNodeBeforeAdding
	 */
	protected boolean isNodeTypeAllowed(final Node node) {
		return false;
	}

	/**
	 * Checks the specified node to ensure it can be added as a child node.
	 * @param node The node to be verified.
	 * @throws DOMException
	 *           <ul>
	 *           <li>HIERARCHY_REQUEST_ERR: Raised if this node is of a type that does not allow children of the type of the <code>newChild</code> node, or if the
	 *           node to append is one of this node's ancestors.</li>
	 *           <li>WRONG_DOCUMENT_ERR: Raised if <code>newChild</code> was created from a different document than the one that created this node.</li>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly.</li>
	 *           </ul>
	 * @see XMLNode#isNodeTypeAllowed
	 */
	protected void checkNodeBeforeAdding(final Node node) throws DOMException {
		if(!isNodeTypeAllowed(node)) //if we can't add this type of node
			throw new XMLDOMException(DOMException.HIERARCHY_REQUEST_ERR, new Object[] {node.getNodeName()}); //show that this node type isn't allowed
		//TODO see if the node is one of this node's ancestors
		//TODO check the document type
		//TODO check readonly status
	}

	/**
	 * Returns the first child of this node, or <code>null</code> if there are no children. For the DOM version, see getFirstChild().
	 * @return The first child of this node, or <code>null</code> if there are no children.
	 * @see XMLNode#getLastChildXMLNode
	 */
	public XMLNode getFirstChildXMLNode() {
		return hasChildNodes() ? (XMLNode)getChildXMLNodeList().get(0) : null; //if there are children, return the first one, else return null
	}

	@Override
	public Node getFirstChild() {
		return getFirstChildXMLNode(); //return the first child node
	}

	/**
	 * Returns the last child of this node, or <code>null</code> if there are no children. For the DOM version, see getLastChild().
	 * @return The last child of this node, or <code>null</code> if there are no children.
	 * @see XMLNode#getFirstChildXMLNode
	 */
	public XMLNode getLastChildXMLNode() {
		return hasChildNodes() ? (XMLNode)getChildXMLNodeList().get(getChildXMLNodeList().size() - 1) : null; //if there are children, return the last one, else return null
	}

	@Override
	public Node getLastChild() {
		return getLastChildXMLNode(); //return the last child node
	}

	/**
	 * Returns the sibling node relative to this one, or <code>null</code> if there is no such node.
	 * @param relativeIndex The position of the requested node in the parent node list relative to this node.
	 * @return The sibling node relative to this node, or <code>null</code> if there is no such node.
	 * @see XMLNode#getPreviousSibling
	 * @see XMLNode#getNextSibling
	 */
	public XMLNode getRelativeSiblingXMLNode(final int relativeIndex) {
		XMLNode relativeSibling = null; //assume at the start that we have no such sibling
		if(getParentNode() != null) { //if we have a parent node
			int thisIndex = getParentXMLNode().getChildXMLNodeList().indexOf(this); //find out our index in our parent's node list
			relativeSibling = (XMLNode)getParentXMLNode().getChildNodes().item(thisIndex + relativeIndex); //get the node relative to this one; this will correctly be set to null if our index is invalid
		}
		return relativeSibling; //return the previous sibling, if we found one
	}

	@Override
	public Node getPreviousSibling() {
		return getRelativeSiblingXMLNode(-1); //find and return the node before this one
	}

	public Node getNextSibling() {
		return getRelativeSiblingXMLNode(1); //find and return the node after this one
	}

	@Override
	public NamedNodeMap getAttributes() {
		return null;
	}

	/** The document which owns this node. */
	private XMLDocument OwnerDocument = null;

	/**
	 * Returns the document associated with this node, which was also in most circumstances the document which created this node. This will be <code>null</code>
	 * for documents or for document types which are not used with any document, yet. For the DOM version, see get <code>OwnerDocument</code>.
	 * @return The document which owns this node, or <code>null</code> if there is no owner.
	 * @see XMLDocument
	 * @see XMLNode#getOwnerDocument
	 */
	public XMLDocument getOwnerXMLDocument() {
		return OwnerDocument;
	}

	/**
	 * Specifies which XML document owns this node. Can only be accessed from within this package. The owner document will also be recursively set for all
	 * children.
	 * @param ownerDocument The document which owns this node.
	 * @see XMLDocument
	 * @see XMLNode#getOwnerXMLDocument
	 */
	void setOwnerXMLDocument(final XMLDocument ownerDocument) {
		OwnerDocument = ownerDocument;
		final Iterator childIterator = getChildXMLNodeList().iterator(); //get an iterator of child nodes
		while(childIterator.hasNext()) { //while there are children
			final XMLNode childXMLNode = (XMLNode)childIterator.next(); //get the next node
			childXMLNode.setOwnerXMLDocument(ownerDocument); //set this node's owner document
		}
	}

	@Override
	public Document getOwnerDocument() {
		return OwnerDocument;
	}

	@Override
	public Node insertBefore(Node newChild, Node refChild) throws DOMException {
		final XMLNode xmlNewChild = (XMLNode)newChild; //cast the child to an XMLNode
		//TODO check the hierarchy
		//TODO check the document
		//TODO check read-only status here
		if(newChild.getNodeType() == DOCUMENT_FRAGMENT_NODE) { //if a document fragment is being added
			final NodeList childNodeList = newChild.getChildNodes(); //get a list of the element fragment's children
			final int childCount = childNodeList.getLength(); //see how many children there are
			for(int i = 0; i < childCount; ++i) { //look at each child
				insertBefore(childNodeList.item(i), refChild); //append this child to our node before the reference child
			}
		} else { //if anything else is being added
			checkNodeBeforeAdding(newChild); //make sure this node can be added to our list; if not, the appropriate exception will be thrown
			final int refChildIndex = getChildXMLNodeList().indexOf(refChild); //get the index of the reference child
			if(refChildIndex == -1) //if we can't find the reference child
				throw new XMLDOMException(DOMException.NOT_FOUND_ERR, new Object[] {refChild.getNodeName()}); //show that we couldn't find the reference node
			getChildXMLNodeList().add(refChildIndex, newChild); //add this child to our list at the appropriate index
			//TODO set the document, set the parent, etc.
			xmlNewChild.setParentXMLNode(this); //set the parent of the added child
			if(isEventsEnabled()) { //if events are enabled
				//create a new event for the inserted node
				final Event nodeInsertedEvent = XMLMutationEvent.createDOMNodeInsertedEvent(xmlNewChild, this);
				xmlNewChild.dispatchEvent(nodeInsertedEvent); //dispatch the event
				//TODO dispatch the document events
			}
		}
		return newChild; //return the inserted child
	}

	@Override
	public Node replaceChild(Node newChild, Node oldChild) throws DOMException {
		if(getChildXMLNodeList().indexOf(oldChild) == -1) //if the old child isn't in the list (do this first, even though this will be checked again in our call to removeChild(), so that errors will occur before modifications occur
			throw new XMLDOMException(DOMException.NOT_FOUND_ERR, new Object[] {oldChild.getNodeName()}); //show that we couldn't find the node to remove
		final int newChildIndex = getChildXMLNodeList().indexOf(newChild); //see if the new child is already in our list
		if(newChildIndex != -1) //if the new child is already in our list
			getChildXMLNodeList().remove(newChildIndex); //remove the new child from the list before we replace the old child with it
		//TODO probably replace this code with a simple replacement operations, taking care to fire the right events
		insertBefore(newChild, oldChild); //insert the new child before the old child
		removeChild(oldChild); //remove the old child
		return oldChild; //return the child we replaced
	}

	@Override
	public Node removeChild(Node oldChild) throws DOMException {
		final XMLNode xmlOldChild = (XMLNode)oldChild; //cast the child to an XMLNode
		//TODO check for read-only status
		if(isEventsEnabled()) { //if events are enabled
			//create a new event for the removed node
			final Event nodeRemovedEvent = XMLMutationEvent.createDOMNodeRemovedEvent(xmlOldChild, this);
			xmlOldChild.dispatchEvent(nodeRemovedEvent); //dispatch the event
			//TODO dispatch the document events
		}
		final int oldChildIndex = getChildXMLNodeList().indexOf(oldChild); //get the index of the child to remove
		if(oldChildIndex == -1) //if we can't find the child they want removed
			throw new XMLDOMException(DOMException.NOT_FOUND_ERR, new Object[] {oldChild.getNodeName()}); //show that we couldn't find the node to remove
		getChildXMLNodeList().remove(oldChildIndex); //remove the child
		((XMLNode)oldChild).setParentXMLNode(null); //show that the removed child no longer has a parent
		return oldChild; //return the child they removed
	}

	@Override
	public Node appendChild(Node newChild) throws DOMException {
		final XMLNode xmlNewChild = (XMLNode)newChild; //cast the child to an XMLNode
		//TODO check the hierarchy
		//TODO check the document
		//TODO check read-only status here
		if(newChild.getNodeType() == DOCUMENT_FRAGMENT_NODE) { //if a document fragment is being added
			final NodeList childNodeList = newChild.getChildNodes(); //get a list of the element fragment's children
			final int childCount = childNodeList.getLength(); //see how many children there are
			for(int i = 0; i < childCount; ++i) { //look at each child
				appendChild(childNodeList.item(i)); //append this child to our node
			}
		} else { //if this isn't a document fragment
			checkNodeBeforeAdding(newChild); //make sure this node can be added to our list; if not, the appropriate exception will be thrown
			getChildXMLNodeList().add(newChild); //add this child to our list
			//TODO set the document, set the parent, etc.
			xmlNewChild.setParentXMLNode(this); //set the parent of the added child
			if(isEventsEnabled()) { //if events are enabled
				//create a new event for the inserted node
				final Event nodeInsertedEvent = XMLMutationEvent.createDOMNodeInsertedEvent(xmlNewChild, this);
				xmlNewChild.dispatchEvent(nodeInsertedEvent); //dispatch the event
				//TODO dispatch the document events
			}
		}
		return newChild; //return the appended child
	}

	/**
	 * Appends the specified character content to this element's last node if that node is an <code>XMLText</code> node, otherwise creates an <code>XMLText</code>
	 * and adds it to the list of child nodes.
	 * @param textString The string content to be added.
	 * @throws DOMException
	 *           <ul>
	 *           <li>HIERARCHY_REQUEST_ERR: Raised if this node is of a type that does not allow children of the type of the <code>newChild</code> node, or if the
	 *           node to append is one of this node's ancestors.</li>
	 *           <li>WRONG_DOCUMENT_ERR: Raised if <code>newChild</code> was created from a different document than the one that created this node.</li>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly.</li>
	 *           </ul>
	 * @see XMLText
	 * @see XMLNode#getLastChildXMLNode
	 * @see XMLNode#appendChild
	 * @see XMLNode#isNodeTypeAllowed
	 */
	public void appendText(final String textString) throws DOMException { //TODO is this a DOM method? if not, it probably goes in XMLUtilities
		final XMLNode lastNode = getLastChildXMLNode(); //get our last child node
		if(lastNode != null && lastNode.getNodeType() == Node.TEXT_NODE) //if we have a last node, and it is a text node
			((XMLText)lastNode).appendData(textString); //append the specified character content to the node
		else { //if we don't have any child nodes, or the last node isn't a text node
			if(textString.length() > 0) { //if there is character content (we don't want to create an empty XMLText node, although that wouldn't really hurt anything)
				//TODO should we check for a DOMException and throw something based on XMLException here?
				appendChild(new XMLText(getOwnerXMLDocument(), textString)); //create a text node from the character content and add it to our children
			}
		}
	}

	/**
	 * @return The text of this node, recursively searching all child nodes. Ultimately, this will return a concatenated string of all terminal XMLText nodes.
	 *         Therefore, this function will be overriden in XMLText.
	 * @see XMLText
	 * @see XMLText#getText
	 */
	public String getText() {
		String textString = ""; //we start out with no text TODO use a string builder
		for(int childIndex = 0; childIndex < getChildXMLNodeList().size(); childIndex++)
			//look at each child node
			textString += ((XMLNode)getChildXMLNodeList().get(childIndex)).getText(); //append the text of this child to the text we've collected so far
		return textString; //return the text we collect
	}

	@Override
	public boolean hasChildNodes() {
		return getChildNodes().getLength() > 0;
	}

	/**
	 * Creates and returns a duplicate copy of this node. The clone has no parent. For the DOM version, see cloneNode().
	 * @param deep If <code>true</code>, recursively clone the subtree under the specified node; if <code>false</code>, clone only the node itself (and its
	 *          attributes, if it is an <code>Element</code>).
	 * @return The duplicate node.
	 * @see XMLNode#clone
	 * @see XMLNode#cloneNode
	 * @see XMLNode#getParentXMLNode
	 */
	public XMLNode cloneXMLNode(boolean deep) {
		try {
			final XMLNode clone = (XMLNode)clone(); //create a clone of this node
			if(deep) //if they want a deep clone
				clone.setChildXMLNodeList(getChildXMLNodeList().cloneDeep()); //deep clone our list of child nodes and put that list in the clone (this operation updates the parent variable of each node)
			return clone; //return the clone we created
		} catch(CloneNotSupportedException e) {
			return null; //since the DOM doesn't support CloneNotSupportedException, we'll hope it never happens (it shouldn't, if we've implemented the terminal classes correctly)
		}
	}

	@Override
	public Node cloneNode(boolean deep) {
		return cloneXMLNode(deep);
	}

	@Override
	public void normalize() {
		normalize(this); //normalize all this node's child nodes
	}

	/**
	 * Normalizes the specified node, which will recursively call this same method for each of the child nodes.
	 * @see #normalize
	 */
	private static void normalize(final Node node) {
		Node childNode = node.getFirstChild(); //get the first child
		while(childNode != null) { //while we have a child node to examine
			Node nextNode = childNode.getNextSibling(); //get a reference to the next sibling so we'll have it when we need it
			if(childNode.getNodeType() == Node.TEXT_NODE) { //if the child node is a text node
				final Text currentTextNode = (Text)childNode; //cast the child node to a text node
				if(currentTextNode.getData().length() == 0) //if this is an empty text node
					node.removeChild(currentTextNode); //remove the empty text node
				else { //if this text node isn't empty, we'll combine as many following text nodes as we can
					while(nextNode != null && nextNode.getNodeType() == Node.TEXT_NODE) { //while the next child node is a text node
						final Text nextTextNode = (Text)nextNode; //cast the next child node to a text node
						final String nextData = nextTextNode.getData(); //get the data from the next text node
						if(nextData.length() != 0) { //if the next text node is not empty, we'll need to combine them
							currentTextNode.appendData(nextData); //append the next text node's data to that of the first
						}
						nextNode = nextTextNode.getNextSibling(); //we'll next look at the node *after* the next node (after the second text node)
						node.removeChild(nextTextNode); //the next text node will always be removed, whether or not it had data to add to this text node
					}
				}
			} else
				//if this isn't a text child node (don't normalize text nodes, since we know they don't have children; this is more efficient than normalizing every child node)
				normalize(childNode); //normalize the child node itself
			childNode = nextNode; //look at the next node
		}
	}

	@Override
	public boolean isSupported(String feature, String version) {
		return false;
	} //TODO fix DOM

	@Override
	public void setPrefix(final String prefix) throws DOMException {
		if(!Objects.equals(getPrefix(), prefix)) { //if the prefix is really changing
			//TODO check for an illegal character
			//TODO check for read-only status
			//TODO check to see if the prefix is malformed
			checkNamespace(getNamespaceURI(), prefix); //make sure the namespace URI and prefix are compatible
			final String localName = XML.getLocalName(getNodeName()); //get the current local name from the qualified node name (we can't use getLocalName(), because it may return null if this node was created with a DOM Level 1 method)
			setNodeName(prefix, localName); //set the prefix and the local name, and update the node name itself
		}
	}

	/**
	 * Checks a prefix against a namespace URI to ensure they are compatibile.
	 * @throws DOMException
	 *           <ul>
	 *           <li>NAMESPACE_ERR: Raised if the <code>qualifiedName</code> is malformed, if the <code>qualifiedName</code> has a prefix and the
	 *           <code>namespaceURI</code> is <code>null</code>, or if the <code>qualifiedName</code> has a prefix that is "xml" and the <code>namespaceURI</code>
	 *           is different from "http://www.w3.org/XML/1998/namespace".</li>
	 *           </ul>
	 */

	/**
	 * Checks a prefix against a namespace URI to ensure they are compatibile. A prefix may be <code>null</code> without causing an error.
	 * @param namespaceURI The URI of the namespace of nodes to return. The special value "*" matches all namespaces.
	 * @param prefix The prefix to look for. If this parameter is <code>null</code>, the method will return the default namespace URI if any.
	 * @throws DOMException
	 *           <ul>
	 *           <li>NAMESPACE_ERR: Raised if the specified <code>prefix</code> is malformed, if the <code>namespaceURI</code> of is <code>null</code>, if the
	 *           specified prefix is "xml" and the <code>namespaceURI</code> of this node is different from "http://www.w3.org/XML/1998/namespace", if this node
	 *           is an attribute and the specified prefix is "xmlns" and the <code>namespaceURI</code> of this node is different from
	 *           "http://www.w3.org/2000/xmlns/", or if this node is an attribute and the <code>qualifiedName</code> of this node is "xmlns".</li>
	 *           </ul>
	 */
	protected void checkNamespace(final String namespaceURI, final String prefix) {
		if(prefix != null) { //if there is a prefix, check the prefix; if not, don't do anything
			if(namespaceURI != null && prefix != null) { //if the namespace URI and prefix are not null
				boolean mismatchedPrefix = false; //we'll set this to true if the prefix doesn't go with the URI
				if(prefix.equals(XML.XML_NAMESPACE_PREFIX)) { //if this is the prefix, "xml"
					if(!namespaceURI.equals(XML.XML_NAMESPACE_URI)) //if the correct URI is not assigned
						mismatchedPrefix = true; //show that this namespace prefix doesn't go with the namespace URI
				} else if(getNodeType() == ATTRIBUTE_NODE && prefix.equals(XML.XMLNS_NAMESPACE_PREFIX)) { //if this is the prefix, "xmlns" for an attribute
					if(!namespaceURI.equals(XML.XMLNS_NAMESPACE_URI)) //if the correct URI is not assigned
						mismatchedPrefix = true; //show that this namespace prefix doesn't go with the namespace URI
				}
				if(!mismatchedPrefix) //if the prefix matches the URI
					return; //everything is OK, as far as we can tell
			}
			throw new XMLDOMException(DOMException.NAMESPACE_ERR, new Object[] {prefix + " (URI: " + namespaceURI + ")"}); //show that this namespace prefix isn't allowed with this specified URI
		}
	}

	@Override
	public boolean hasAttributes() {
		return false;
	} //TODO fix DOM

	/**
	 * Adds an event listener to the specified map.
	 * @param map The listenerListMap The map of lists of listeners.
	 * @param type The type of event.
	 * @param listener The listener to be added.
	 */
	private void addEventListener(final Map listenerListMap, final String type, final EventListener listener) {
		List eventListenerList = (List)listenerListMap.get(type); //get a list of listeners for this type of event
		if(eventListenerList == null) { //if there is no list of listeners for this event
			eventListenerList = new ArrayList(); //create a new list of listeners for this event
			listenerListMap.put(type, eventListenerList); //put the list into the map, keyed by this event type
		}
		if(!eventListenerList.contains(listener)) //if this listener isn't in our list
			eventListenerList.add(listener); //add this listener to our list
	}

	/**
	 * Removes an event listener to the specified map.
	 * @param map The listenerListMap The map of lists of listeners.
	 * @param type The type of event.
	 * @param listener The listener to be removed.
	 */
	private void removeEventListener(final Map listenerListMap, final String type, final EventListener listener) {
		final List eventListenerList = (List)listenerListMap.get(type); //get a list of listeners for this type of event
		if(eventListenerList != null) { //if there is a list of listeners for this event
			eventListenerList.remove(listener); //remove the message listener if it exists
			if(eventListenerList.size() == 0) //if there are no more listeners in this list
				listenerListMap.remove(eventListenerList); //remove the list itself from the map
			//TODO note that, if the map is now empty, it will not be removed; we may want to add this capability elsewhere
		}
	}

	/**
	 * <p>
	 * Allows the registration of event listeners on the event target. If an <code>EventListener</code> is added to an <code>EventTarget</code> while it is
	 * processing an event, it will not be triggered by the current actions but may be triggered during a later stage of event flow, such as the bubbling phase.
	 * </p>
	 * <p>
	 * If multiple identical <code>EventListener</code>s are registered on the same <code>EventTarget</code> with the same parameters the duplicate instances are
	 * discarded. They do not cause the <code>EventListener</code> to be called twice and since they are discarded they do not need to be removed with the
	 * <code>removeEventListener</code> method.
	 * </p>
	 * @param type The event type for which the user is registering.
	 * @param listener The <code>listener</code> parameter takes an interface implemented by the user which contains the methods to be called when the event
	 *          occurs.
	 * @param useCapture If <code>true</code>, <code>useCapture</code> indicates that the user wishes to initiate capture. After initiating capture, all events of
	 *          the specified type will be dispatched to the registered <code>EventListener</code> before being dispatched to any <code>EventTargets</code>
	 *          beneath them in the tree. Events which are bubbling upward through the tree will not trigger an <code>EventListener</code> designated to use
	 *          capture.
	 */
	public void addEventListener(String type, EventListener listener, boolean useCapture) {
		if(useCapture) { //if this event listener wants to capture events
			addEventListener(getCaptureEventListenerListMap(), type, listener); //add the listener to our list of capture event listeners
		} else { //if this is just a normal non-capturing event listener
			addEventListener(getEventListenerListMap(), type, listener); //add the listener to our list of event listeners
		}
	}

	/**
	 * <p>
	 * Allows the removal of event listeners from the event target. If an <code>EventListener</code> is removed from an <code>EventTarget</code> while it is
	 * processing an event, it will not be triggered by the current actions. <code>EventListener</code>s can never be invoked after being removed.
	 * </p>
	 * <p>
	 * Calling <code>removeEventListener</code> with arguments which do not identify any currently registered <code>EventListener</code> on the
	 * <code>EventTarget</code> has no effect.
	 * </p>
	 * @param type Specifies the event type of the <code>EventListener</code> being removed.
	 * @param listener Indicates the <code>EventListener </code> to be removed.
	 * @param useCapture Specifies whether the <code>EventListener</code> being removed was registered as a capturing listener or not. If a listener was
	 *          registered twice, one with capture and one without, each must be removed separately. Removal of a capturing listener does not affect a
	 *          non-capturing version of the same listener, and vice versa.
	 */
	public void removeEventListener(String type, EventListener listener, boolean useCapture) {
		if(useCapture) { //if this event listener was for capture events
			if(captureEventListenerListMap != null) //if we have a map for capture event listeners
				removeEventListener(getCaptureEventListenerListMap(), type, listener); //remove the listener from our list of capture event listeners
		} else { //if this was just a normal non-capturing event listener, and we have a map for event listeners
			if(eventListenerListMap != null) //if we have a map for event listeners
				removeEventListener(getEventListenerListMap(), type, listener); //remove the listener from our list of event listeners
		}
	}

	/**
	 * Allows the dispatch of events into the implementations event model. Events dispatched in this manner will have the same capturing and bubbling behavior as
	 * events dispatched directly by the implementation. The target of the event is the <code>EventTarget</code> on which <code>dispatchEvent</code> is called.
	 * @param event Specifies the event type, behavior, and contextual information to be used in processing the event.
	 * @return Whether any of the listeners which handled the event called <code>preventDefault</code>. If <code>preventDefault</code> was called the value is
	 *         <code>false</code>, else the value is <code>true</code>.
	 * @throws EventException
	 *           <ul>
	 *           <li>UNSPECIFIED_EVENT_TYPE_ERR: Raised if the <code>Event</code>'s type was not specified by initializing the event before
	 *           <code>dispatchEvent</code> was called. Specification of the <code>Event</code>'s type as <code>null</code> or an empty string will also trigger
	 *           this exception.</li>
	 *           </ul>
	 */
	public boolean dispatchEvent(Event event) throws EventException {
		if(event.getType() == null || event.getType().length() == 0) //if the event type has not been set
			throw new XMLEventException(XMLEventException.UNSPECIFIED_EVENT_TYPE_ERR, new Object[] {}); //show that the event type has not been set
		final XMLEvent xmlEvent = (XMLEvent)event; //cast the event to our implementation type
		if(xmlEvent.isDefaultActionOccurring()) { //if the event hasn't been cancelled
			//Create a list of ancestors, with the first node representing the
			//  immediate ancestor (the parent). The DOM requires any capturing and
			//  bubbling to occur based upon this initial state of the tree.
			final List ancestorList = new ArrayList(); //a list which we'll use to construct the initial state of the objects ancestor
			XMLNode xmlNode = (XMLNode)getParentNode(); //get our first parent
			while(xmlNode != null) { //while there are more parents in the hierarchy
				ancestorList.add(xmlNode); //add the node to our list of ancestors
				xmlNode = (XMLNode)xmlNode.getParentNode(); //get this node's parent
			}
			xmlEvent.setEventPhase(Event.CAPTURING_PHASE); //show that we're ready to send the event to any capture event listeners
			//dispatch captured events, starting at the top of our ancestor chain and working our way down,
			//stopping when propatation has been stopped
			for(int i = ancestorList.size() - 1; i >= 0 && xmlEvent.isPropagating(); --i) {
				final XMLNode ancestorXMLNode = (XMLNode)ancestorList.get(i); //get the current ancestor node
				ancestorXMLNode.dispatchCaptureEvent(xmlEvent); //dispatch the captured event, if there are any listeners
			}
			//dispatch normally
			if(xmlEvent.isDefaultActionOccurring()) { //if the event hasn't been cancelled
				xmlEvent.setEventPhase(Event.AT_TARGET); //show that we're ready to send the event to its target
				dispatchEvent(eventListenerListMap, xmlEvent); //dispatch the event to all event listeners
			}
			//bubble
			if(xmlEvent.getBubbles() && xmlEvent.isDefaultActionOccurring()) { //if this event can bubble, and the event hasn't been cancelled
				xmlEvent.setEventPhase(Event.BUBBLING_PHASE); //show that we're ready to send the event to any bubble event listeners
				//dispatch captured events, starting at the bottom of our ancestor chain and working our way to the top,
				//stopping when propatation has been stopped
				for(int i = 0; i < ancestorList.size() && xmlEvent.isPropagating(); ++i) {
					final XMLNode ancestorXMLNode = (XMLNode)ancestorList.get(i); //get the current ancestor node
					ancestorXMLNode.dispatchBubbleEvent(xmlEvent); //dispatch the captured event, if there are any listeners
				}
			}
		}
		return xmlEvent.isDefaultActionOccurring(); //return false if any of the listeners called event.preventDefault()
	}

	/**
	 * Dispatches the given event to the event listeners in the specified map.
	 * @param listerListMap The map which contains lists of listeners for events.
	 * @param xmlEvent The event to be dispatched.
	 * @return Whether any of the listeners which handled the event called <code>preventDefault</code>. If <code>preventDefault</code> was called the value is
	 *         <code>false</code>, else the value is <code>true</code>.
	 */
	private boolean dispatchEvent(final Map listerListMap, final XMLEvent xmlEvent) {
		if(listerListMap != null) { //if we might have event listeners
			final List eventListenerList = (List)listerListMap.get(xmlEvent.getType()); //get a list of event listeners
			if(eventListenerList != null) { //if we have a list of event listeners
				xmlEvent.setCurrentTarget(this); //show that we're currently processing event listeners for this node
				//look at each of the event listeners while the event has not been cancelled
				for(int i = 0; i < eventListenerList.size() && xmlEvent.isDefaultActionOccurring(); ++i) {
					final EventListener eventListener = (EventListener)eventListenerList.get(i); //get a reference to this event listener
					eventListener.handleEvent(xmlEvent); //let the event listener handle the event
				}
			}
		}
		return xmlEvent.isDefaultActionOccurring(); //return false if any of the listeners called event.preventDefault()
	}

	/**
	 * Dispatches the event to any capture event listeners. This is called from a child node's <code>dispatchEvent()</code> method.
	 * @param xmlEvent The captured event to dispatch to any capture event listeners.
	 * @return Whether any of the listeners which handled the event called <code>preventDefault</code>. If <code>preventDefault</code> was called the value is
	 *         <code>false</code>, else the value is <code>true</code>.
	 * @throws EventException
	 *           <ul>
	 *           <li>UNSPECIFIED_EVENT_TYPE_ERR: Raised if the <code>Event</code>'s type was not specified by initializing the event before
	 *           <code>dispatchEvent</code> was called. Specification of the <code>Event</code>'s type as <code>null</code> or an empty string will also trigger
	 *           this exception.</li>
	 *           </ul>
	 * @see #dispatchEvent
	 */
	protected boolean dispatchCaptureEvent(XMLEvent xmlEvent) throws EventException {
		if(xmlEvent.getType() == null || xmlEvent.getType().length() == 0) //if the event type has not been set
			throw new XMLEventException(XMLEventException.UNSPECIFIED_EVENT_TYPE_ERR, new Object[] {}); //show that the event type has not been set
		return dispatchEvent(captureEventListenerListMap, xmlEvent); //dispatch the event to all capture event listeners
	}

	/**
	 * Dispatches the event the event listeners for this current level. This is called from a child node's <code>dispatchEvent()</code> method to implement
	 * bubbling.
	 * @param xmlEvent The captured event to dispatch to any event listeners via bubbling.
	 * @return Whether any of the listeners which handled the event called <code>preventDefault</code>. If <code>preventDefault</code> was called the value is
	 *         <code>false</code>, else the value is <code>true</code>.
	 * @throws EventException
	 *           <ul>
	 *           <li>UNSPECIFIED_EVENT_TYPE_ERR: Raised if the <code>Event</code>'s type was not specified by initializing the event before
	 *           <code>dispatchEvent</code> was called. Specification of the <code>Event</code>'s type as <code>null</code> or an empty string will also trigger
	 *           this exception.</li>
	 *           </ul>
	 * @see #dispatchEvent
	 */
	protected boolean dispatchBubbleEvent(XMLEvent xmlEvent) throws EventException {
		if(xmlEvent.getType() == null || xmlEvent.getType().length() == 0) //if the event type has not been set
			throw new XMLEventException(XMLEventException.UNSPECIFIED_EVENT_TYPE_ERR, new Object[] {}); //show that the event type has not been set
		return dispatchEvent(eventListenerListMap, xmlEvent); //dispatch the event to all normal event listeners
	}

	/**
	 * Returns a NodeList of first-level descendant nodes with a given type and node name. The special wildcard name "*" returns nodes of all names. If
	 * <code>deep</code> is set to <code>true</code>, returns a NodeList of all descendant nodes with a given name, in the order in which they would be
	 * encountered in a preorder traversal of the node tree.
	 * @param nodeType The type of node this is.
	 * @param nodeName The name of the node to match on. The special value "*" matches all nodes.
	 * @param deep Whether or not matching child nodes of each matching child node, etc. should be included.
	 * @return A new NodeList object containing all the matching nodes.
	 * @see XMLNode
	 * @see XMLNodeList
	 */
	public NodeList getNodesByName(final int nodeType, final String nodeName, final boolean deep) {
		//TODO change this to use XMLUtilities.getNodesByNameNS
		final XMLNodeList nodeList = new XMLNodeList(); //create a new node list to return
		final boolean matchAllNodes = "*".equals(nodeName); //see if they passed us the wildcard character TODO use a constant here
		for(int childIndex = 0; childIndex < getChildXMLNodeList().size(); childIndex++) { //look at each child node
			final XMLNode node = ((XMLNode)getChildXMLNodeList().get(childIndex)); //get a reference to this node
			if(node.getNodeType() == nodeType) { //if this is a node of the correct type
				if((matchAllNodes || node.getNodeName().equals(nodeName))) //if node has the correct name (or they passed us the wildcard character)
					nodeList.add(node); //add this node to the list
				if(deep) //if each of the children should check for matching nodes as well
					nodeList.addAll((XMLNodeList)node.getNodesByName(nodeType, nodeName, deep)); //get this node's matching child nodes by name and add them to our list
			}
		}
		return nodeList; //return the list we created and filled
	}

	/**
	 * Returns a NodeList of first-level descendant nodes with a given type, namespace URI, and local name. The special wildcard name "*" returns nodes of all
	 * local names. If <code>deep</code> is set to <code>true</code>, returns a NodeList of all descendant nodes with a given name, in the order in which they
	 * would be encountered in a preorder traversal of the node tree.
	 * @param nodeType The type of node this is.
	 * @param namespaceURI The URI of the namespace of nodes to return. The special value "*" matches all namespaces.
	 * @param localName The local name of the node to match on. The special value "*" matches all local names.
	 * @param deep Whether or not matching child nodes of each matching child node, etc. should be included.
	 * @return A new NodeList object containing all the matching nodes.
	 * @see XMLNode
	 * @see XMLNodeList
	 */
	public NodeList getNodesByNameNS(final int nodeType, final String namespaceURI, final String localName, final boolean deep) {
		//TODO change this to use XMLUtilities.getNodesByNameNS
		final XMLNodeList nodeList = new XMLNodeList(); //create a new node list to return
		final boolean matchAllNamespaces = "*".equals(namespaceURI); //see if they passed us the wildcard character for the namespace URI TODO use a constant here
		final boolean matchAllLocalNames = "*".equals(localName); //see if they passed us the wildcard character for the local name TODO use a constant here
		for(int childIndex = 0; childIndex < getChildXMLNodeList().size(); childIndex++) { //look at each child node
			final XMLNode node = ((XMLNode)getChildXMLNodeList().get(childIndex)); //get a reference to this node
			if(node.getNodeType() == nodeType) { //if this is a node of the correct type
				final String nodeNamespaceURI = node.getNamespaceURI(); //get the node's namespace URI
				final String nodeLocalName = node.getLocalName(); //get the node's local name
				if(matchAllNamespaces || //if we should match all namespaces
						((namespaceURI == null && nodeNamespaceURI == null) || //if both namespaces are null
								(namespaceURI != null && namespaceURI.equals(nodeNamespaceURI))) //if the namespace URI's match
				) { //if we should match all namespaces, or the namespaces match
					if(matchAllLocalNames || localName.equals(nodeLocalName)) { //if we should match all local names, or the local names match
						nodeList.add(node); //add this node to the list
					}
				}
				if(deep) //if each of the children should check for matching nodes as well
					nodeList.addAll((XMLNodeList)node.getNodesByNameNS(nodeType, namespaceURI, localName, deep)); //get this node's matching child nodes by name and add them to our list
			}
		}
		return nodeList; //return the list we created and filled
	}

	/**
	 * Inserts the node <code>newChild</code> before the existing child node <code>refChild</code>. If <code>refChild</code> is <code>null</code>, insert
	 * <code>newChild</code> at the end of the list of children.
	 * <p>
	 * If <code>newChild</code> is a <code>DocumentFragment</code> object, all of its children are inserted, in the same order, before <code>refChild</code>. If
	 * the <code>newChild</code> is already in the tree, it is first removed.
	 * </p>
	 * <p>
	 * <b>Note:</b> Inserting a node before itself is implementation dependent.
	 * </p>
	 * @param newChild The node to insert.
	 * @param refChild The reference node, i.e., the node before which the new node must be inserted.
	 * @return The node being inserted.
	 * @throws DOMException
	 *           <ul>
	 *           <li>HIERARCHY_REQUEST_ERR: Raised if this node is of a type that does not allow children of the type of the <code>newChild</code> node, or if the
	 *           node to insert is one of this node's ancestors or this node itself, or if this node is of type <code>Document</code> and the DOM application
	 *           attempts to insert a second <code>DocumentType</code> or <code>Element</code> node.</li>
	 *           <li>WRONG_DOCUMENT_ERR: Raised if <code>newChild</code> was created from a different document than the one that created this node.</li>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly or if the parent of the node being inserted is readonly.</li>
	 *           <li>NOT_FOUND_ERR: Raised if <code>refChild</code> is not a child of this node.</li>
	 *           <li>NOT_SUPPORTED_ERR: if this node is of type <code>Document</code>, this exception might be raised if the DOM implementation doesn't support
	 *           the insertion of a <code>DocumentType</code> or <code>Element</code> node.</li>
	 *           </ul>
	 */
	//TODO @Override
	//TODO fix for DOM 3    public Node insertBefore(Node newChild, Node refChild) throws DOMException {throw new UnsupportedOperationException();}	//TODO fix for DOM 3

	/**
	 * Replaces the child node <code>oldChild</code> with <code>newChild</code> in the list of children, and returns the <code>oldChild</code> node.
	 * <p>
	 * If <code>newChild</code> is a <code>DocumentFragment</code> object, <code>oldChild</code> is replaced by all of the <code>DocumentFragment</code> children,
	 * which are inserted in the same order. If the <code>newChild</code> is already in the tree, it is first removed.
	 * </p>
	 * <p >
	 * <b>Note:</b> Replacing a node with itself is implementation dependent.
	 * @param newChild The new node to put in the child list.
	 * @param oldChild The node being replaced in the list.
	 * @return The node replaced.
	 * @throws DOMException
	 *           <ul>
	 *           <li>HIERARCHY_REQUEST_ERR: Raised if this node is of a type that does not allow children of the type of the <code>newChild</code> node, or if the
	 *           node to put in is one of this node's ancestors or this node itself, or if this node is of type <code>Document</code> and the result of the
	 *           replacement operation would add a second <code>DocumentType</code> or <code>Element</code> on the <code>Document</code> node.</li>
	 *           <li>WRONG_DOCUMENT_ERR: Raised if <code>newChild</code> was created from a different document than the one that created this node.</li>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this node or the parent of the new node is readonly.</li>
	 *           <li>NOT_FOUND_ERR: Raised if <code>oldChild</code> is not a child of this node.</li>
	 *           <li>NOT_SUPPORTED_ERR: if this node is of type <code>Document</code>, this exception might be raised if the DOM implementation doesn't support
	 *           the replacement of the <code>DocumentType</code> child or <code>Element</code> child.</li>
	 *           </ul>
	 */
	//TODO @Override
	//TODO fix for DOM 3    public Node replaceChild(Node newChild, Node oldChild) throws DOMException {throw new UnsupportedOperationException();}	//TODO fix for DOM 3

	/**
	 * Removes the child node indicated by <code>oldChild</code> from the list of children, and returns it.
	 * @param oldChild The node being removed.
	 * @return The node removed.
	 * @throws DOMException
	 *           <ul>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly.</li>
	 *           <li>NOT_FOUND_ERR: Raised if <code>oldChild</code> is not a child of this node.</li>
	 *           <li>NOT_SUPPORTED_ERR: if this node is of type <code>Document</code>, this exception might be raised if the DOM implementation doesn't support
	 *           the removal of the <code>DocumentType</code> child or the <code>Element</code> child.</li>
	 *           </ul>
	 */
	//TODO @Override
	//TODO fix for DOM 3        public Node removeChild(Node oldChild) throws DOMException {throw new UnsupportedOperationException();}	//TODO fix for DOM 3

	/**
	 * Adds the node <code>newChild</code> to the end of the list of children of this node. If the <code>newChild</code> is already in the tree, it is first
	 * removed.
	 * @param newChild The node to add.If it is a <code>DocumentFragment</code> object, the entire contents of the document fragment are moved into the child list
	 *          of this node
	 * @return The node added.
	 * @throws DOMException
	 *           <ul>
	 *           <li>HIERARCHY_REQUEST_ERR: Raised if this node is of a type that does not allow children of the type of the <code>newChild</code> node, or if the
	 *           node to append is one of this node's ancestors or this node itself, or if this node is of type <code>Document</code> and the DOM application
	 *           attempts to append a second <code>DocumentType</code> or <code>Element</code> node.</li>
	 *           <li>WRONG_DOCUMENT_ERR: Raised if <code>newChild</code> was created from a different document than the one that created this node.</li>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly or if the previous parent of the node being inserted is readonly.</li>
	 *           <li>NOT_SUPPORTED_ERR: if the <code>newChild</code> node is a child of the <code>Document</code> node, this exception might be raised if the DOM
	 *           implementation doesn't support the removal of the <code>DocumentType</code> child or <code>Element</code> child.</li>
	 *           </ul>
	 */
	//TODO @Override
	//TODO fix for DOM 3        public Node appendChild(Node newChild) throws DOMException {throw new UnsupportedOperationException();}	//TODO fix for DOM 3

	/**
	 * Puts all <code>Text</code> nodes in the full depth of the sub-tree underneath this <code>Node</code>, including attribute nodes, into a "normal" form where
	 * only structure (e.g., elements, comments, processing instructions, CDATA sections, and entity references) separates <code>Text</code> nodes, i.e., there
	 * are neither adjacent <code>Text</code> nodes nor empty <code>Text</code> nodes. This can be used to ensure that the DOM view of a document is the same as
	 * if it were saved and re-loaded, and is useful when operations (such as XPointer [
	 * <a href='http://www.w3.org/TR/2003/REC-xptr-framework-20030325/'>XPointer</a>] lookups) that depend on a particular document tree structure are to be used.
	 * If the parameter "normalize-characters" of the <code>DOMConfiguration</code> object attached to the <code>Node.ownerDocument</code> is <code>true</code>,
	 * this method will also fully normalize the characters of the <code>Text</code> nodes.
	 * <p >
	 * <b>Note:</b> In cases where the document contains <code>CDATASections</code>, the normalize operation alone may not be sufficient, since XPointers do not
	 * differentiate between <code>Text</code> nodes and <code>CDATASection</code> nodes.
	 */
	//TODO @Override
	//TODO fix for DOM 3        public void normalize() {throw new UnsupportedOperationException();}	//TODO fix for DOM 3

	@Override
	public String getBaseURI() {
		throw new UnsupportedOperationException();
	} //TODO fix for DOM 3

	@Override
	public short compareDocumentPosition(Node other) throws DOMException {
		throw new UnsupportedOperationException();
	} //TODO fix for DOM 3

	@Override
	public String getTextContent() throws DOMException {
		throw new UnsupportedOperationException();
	} //TODO fix for DOM 3

	@Override
	public void setTextContent(String textContent) throws DOMException {
		throw new UnsupportedOperationException();
	} //TODO fix for DOM 3

	@Override
	public boolean isSameNode(Node other) {
		throw new UnsupportedOperationException();
	} //TODO fix for DOM 3

	@Override
	public String lookupPrefix(String namespaceURI) {
		throw new UnsupportedOperationException();
	} //TODO fix for DOM 3

	@Override
	public boolean isDefaultNamespace(String namespaceURI) {
		throw new UnsupportedOperationException();
	} //TODO fix for DOM 3

	@Override
	public String lookupNamespaceURI(String prefix) {
		throw new UnsupportedOperationException();
	} //TODO fix for DOM 3

	@Override
	public boolean isEqualNode(Node arg) {
		throw new UnsupportedOperationException();
	} //TODO fix for DOM 3

	@Override
	public Object getFeature(String feature, String version) {
		throw new UnsupportedOperationException();
	} //TODO fix for DOM 3

	@Override
	public Object setUserData(String key, Object data, UserDataHandler handler) {
		throw new UnsupportedOperationException();
	} //TODO fix for DOM 3

	@Override
	public Object getUserData(String key) {
		throw new UnsupportedOperationException();
	} //TODO fix for DOM 3

	/** @return A string representation of the node. */
	public String toString() {
		final StringBuilder stringBuilder = new StringBuilder(); //create a string buffer to hold the characters
		stringBuilder.append('['); //append the node type
		switch(getNodeType()) {
			case ELEMENT_NODE:
				stringBuilder.append("ELEMENT_NODE");
				break;
			case ATTRIBUTE_NODE:
				stringBuilder.append("ATTRIBUTE_NODE");
				break;
			case TEXT_NODE:
				stringBuilder.append("TEXT_NODE");
				break;
			case CDATA_SECTION_NODE:
				stringBuilder.append("CDATA_SECTION_NODE");
				break;
			case ENTITY_REFERENCE_NODE:
				stringBuilder.append("ENTITY_REFERENCE_NODE");
				break;
			case ENTITY_NODE:
				stringBuilder.append("ENTITY_NODE");
				break;
			case PROCESSING_INSTRUCTION_NODE:
				stringBuilder.append("PROCESSING_INSTRUCTION_NODE");
				break;
			case COMMENT_NODE:
				stringBuilder.append("COMMENT_NODE");
				break;
			case DOCUMENT_NODE:
				stringBuilder.append("DOCUMENT_NODE");
				break;
			case DOCUMENT_TYPE_NODE:
				stringBuilder.append("DOCUMENT_TYPE_NODE");
				break;
			case DOCUMENT_FRAGMENT_NODE:
				stringBuilder.append("DOCUMENT_FRAGMENT_NODE");
				break;
			case NOTATION_NODE:
				stringBuilder.append("NOTATION_NODE");
				break;
			default:
				stringBuilder.append("UNKNOWN_NODE");
				break;
		}
		stringBuilder.append("] ");
		stringBuilder.append(getNodeName()); //append the node name
		return stringBuilder.toString(); //convert the string buffer to a string and return it
	}

}
