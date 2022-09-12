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

import java.util.Iterator;
import java.util.HashMap;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.DOMException;

/**
 * A map of nodes in an XML document in no particular order.
 * @author Garret Wilson
 * @see XMLNode
 * @see com.globalmentor.xml.dom.impl.traversal.XMLNodeIterator
 * @see org.w3c.dom.Node
 * @see org.w3c.dom.NamedNodeMap
 * @deprecated
 */
@Deprecated
public class XMLNamedNodeMap extends HashMap implements NamedNodeMap {

	/**
	 * The delimiter used for forming combined namespaceURI+localName keys for the namespace map.
	 */
	protected static final char NAMESPACE_DELIMITER = '$';

	/**
	 * Specifies which XML document owns these nodes. Can only be accessed from within this package. Used to set all document owners in one batch.
	 * @param ownerDocument The document which owns the nodes.
	 * @see XMLNode#setOwnerXMLDocument
	 */
	void setOwnerXMLDocument(final XMLDocument ownerDocument) { //TODO add this method to XMLNodeList, too
		final Iterator childIterator = values().iterator(); //get an iterator of all the values
		while(childIterator.hasNext()) { //while there are children
			final XMLNode childXMLNode = (XMLNode)childIterator.next(); //get the next node
			childXMLNode.setOwnerXMLDocument(ownerDocument); //set this node's owner document
		}
	}

	//TODO there's a method somewhere that we'll probably use which adds the contents of a list to the map; see if we need to override it and make sure all the parents or whatever are updated

	/**
	 * Creates and returns a duplicate copy of this named node map with no values.
	 * @return A duplicate "shallow clone" copy of this named node map.
	 * @see XMLNode#cloneXMLNode
	 * @see XMLNode#cloneNode
	 * @see XMLNamedNodeMap#cloneDeep
	 * @see Object#clone
	 */
	public Object clone() {
		return new XMLNamedNodeMap(); //create a new named node map and return it
		//TODO this doesn't look right; even a shallow clone should have children, just not cloned children
	}

	/**
	 * Creates and returns a duplicate copy of this named node map containing clones of all its children.
	 * @return A duplicate "deep clone" copy of this named node map.
	 * @see XMLNode#cloneXMLNode
	 * @see XMLNode#cloneNode
	 * @see XMLNamedNodeMap#clone
	 */
	public XMLNamedNodeMap cloneDeep() {
		//TODO important: fix for namespaces
		final XMLNamedNodeMap clone = (XMLNamedNodeMap)clone(); //create a new named node map
		//create an iterator to look through the keys
		//we'll look up the values from the keys instead of just looking through the
		//values, because in setting them in the node we don't know if they have been
		//set with namespace awareness or nott
		final Iterator keyIterator = keySet().iterator();
		while(keyIterator.hasNext()) { //while we still have keys
			final Object key = keyIterator.next(); //get the key
			final Node node = (Node)get(key); //get the value that goes with the key
			clone.put(key, node.cloneNode(true)); //perform a deep clone of the node and add it to our named node map
		}
		/*TODO del when works with namespaces
				final Iterator nodeIterator=values().iterator();	//get an interator of our values
				while(nodeIterator.hasNext())	//while we still have nodes
					clone.setNamedItem(((XMLNode)nodeIterator.next()).cloneXMLNode(true));	//deep clone the next node and store it in our named node map clone
		*/
		return clone; //return our cloned named node map
	}

	/**
	 * Returns whether a node exists keyed to a particular namespace URI and a local name.
	 * @param namespaceURI The URI of the namespace for a particular node.
	 * @param localName The node's local name.
	 * @return <code>true</code> if a node exists in the map keyed to the given namespace URI and local name.
	 * @see #NAMESPACE_DELIMITER
	 */
	public boolean containsKeyNS(final String namespaceURI, final String localName) {
		return containsKey(getNamespaceKey(namespaceURI, localName)); //see if there is a key based on the namespace URI and local name
	}

	/**
	 * An extension to the DOM that indicates whether a certain node is currently in this named node map.
	 * @param arg The node that is checked to see if its name exists in the named node map.
	 * @return <code>true</code> if a node name of the given node exists in the named node map, else <code>false</code>. Does not check to see if the value in the
	 *         named node map actually matches the given node.
	 * @see XMLNamedNodeMap#getNamedItem
	 * @see XMLNamedNodeMap#setNamedItem
	 * @see XMLNamedNodeMap#removeNamedItem
	 */
	public boolean containsNamedItem(Node arg) {
		return containsKey(arg.getNodeName()); //see if there's a node in our map with the same name as the specified node
	}

	@Override
	public Node getNamedItem(String name) {
		return (Node)get(name); //return the specified node, nor null if it doesn't exist; we'll not check for a ClassCastExption or a NullPointerException, which shouldn't occur in our implementation
	}

	@Override
	public Node setNamedItem(Node arg) throws DOMException {
		//TODO check to make sure the node has the same document
		//TODO check to see if we are readonly
		//TODO check the node to see if it is used by another attribute
		//TODO what's that "special" string name clash thing above?
		return (Node)put(arg.getNodeName(), arg); //add the node, using its node name as the key, and return the previously existing node, if any; we'll let runtime exceptions occur, but their shouldn't be any in our implementation
	}

	@Override
	public Node removeNamedItem(String name) throws DOMException {
		//TODO check read-only here, and elsewhere
		Node existingNode = (Node)remove(name); //remove the node with this name, and store the the node if it existed; we'll allow runtime errors to occur, which shouldn't happen in our implementation, anyway
		if(existingNode != null) //if a node exists
			return existingNode; //return that node
		else
			//if the node didn't exist to begin with
			throw new XMLDOMException(DOMException.NOT_FOUND_ERR, new Object[] {name}); //show that we couldn't find the existing node
		//TODO do whatever is needed for bringing default nodes in
	}

	@Override
	public Node item(int index) {
		if(index >= 0 && index < size()) //if the index is in range
			return (Node)values().toArray()[index]; //get a collection of the values, convert that to an array, and return the correct indexed node in the array
		else
			//if the index is out of range
			return null; //return null
	}

	@Override
	public int getLength() {
		return size();
	} //return our size

	/**
	 * Returns the key name to use based on a namespace URI and a local name.
	 * @param namespaceURI The URI of the namespace for a particular node.
	 * @param localName The node's local name.
	 * @return The namespace key name to use in the map.
	 * @see #NAMESPACE_DELIMITER
	 */
	protected static final String getNamespaceKey(final String namespaceURI, final String localName) {
		return namespaceURI + NAMESPACE_DELIMITER + localName; //combine the namespace URI and the local name using the delimiter
	}

	@Override
	public Node getNamedItemNS(String namespaceURI, String localName) {
		//TODO check to see if either namespaceURI or localName is null
		return getNamedItem(getNamespaceKey(namespaceURI, localName)); //get the named item from its namespace key
	}

	@Override
	public Node setNamedItemNS(Node arg) throws DOMException {
		//TODO check to make sure the node has the same document
		//TODO check to see if we are readonly
		//TODO check the node to see if it is used by another attribute
		//TODO what's that "special" string name clash thing above?
		return (Node)put(getNamespaceKey(arg.getNamespaceURI(), arg.getLocalName()), arg); //add the node, using its combined namespace URI and local name as the key, and return the previously existing node, if any; we'll let runtime exceptions occur, but there shouldn't be any in our implementation
	}

	@Override
	public Node removeNamedItemNS(String namespaceURI, String localName) throws DOMException {
		return removeNamedItem(getNamespaceKey(namespaceURI, localName)); //remove the item with a key name composed of the namespace URI and the local name
	}

}
