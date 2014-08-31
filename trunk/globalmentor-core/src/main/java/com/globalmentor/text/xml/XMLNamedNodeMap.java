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

import java.util.Iterator;
import java.util.HashMap;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.DOMException;

/**
 * A map of nodes in an XML document in no particular order.
 * @author Garret Wilson
 * @see XMLNode
 * @see XMLNodeIterator
 * @see org.w3c.dom.Node
 * @see org.w3c.dom.NamedNodeMap
 * @deprecated
 */
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

	/**
	 * Retrieves a node specified by name.
	 * @param name The name of the node to retrieve.
	 * @return A <code>Node</code> (of any type) with the specified name, or <code>null</code> if the specified name did not identify any node in the map.
	 * @version DOM Level 1
	 */
	public Node getNamedItem(String name) {
		return (Node)get(name); //return the specified node, nor null if it doesn't exist; we'll not check for a ClassCastExption or a NullPointerException, which shouldn't occur in our implementation
	}

	/**
	 * Adds a node using its <code>nodeName</code> attribute, which is the qualified name when applicable.<br/>
	 * As the <code>nodeName</code> attribute is used to derive the name which the node must be stored under, multiple nodes of certain types (those that have a
	 * "special" string value) cannot be stored as the names would clash. This is seen as preferable to allowing nodes to be aliased.
	 * @param arg A node to store in a named node map. The node will later be accessible using the value of the <code>nodeName</code> attribute of the node. If a
	 *          node with that name is already present in the map, it is replaced by the new one.
	 * @return If the new <code>Node</code> replaces an existing node with the same name the previously existing <code>Node</code> is returned, otherwise
	 *         <code>null</code> is returned.
	 * @throws DOMException <ul>
	 *           <li>WRONG_DOCUMENT_ERR: Raised if <code>arg</code> was created from a different document than the one that created the <code>NamedNodeMap</code>.
	 *           </li>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this <code>NamedNodeMap</code> is readonly.</li>
	 *           <li>INUSE_ATTRIBUTE_ERR: Raised if <code>arg</code> is an <code>Attr</code> that is already an attribute of another <code>Element</code> object.
	 *           The DOM user must explicitly clone <code>Attr</code> nodes to re-use them in other elements.</li>
	 *           </ul>
	 * @version DOM Level 1
	 */
	public Node setNamedItem(Node arg) throws DOMException {
		//TODO check to make sure the node has the same document
		//TODO check to see if we are readonly
		//TODO check the node to see if it is used by another attribute
		//TODO what's that "special" string name clash thing above?
		return (Node)put(arg.getNodeName(), arg); //add the node, using its node name as the key, and return the previously existing node, if any; we'll let runtime exceptions occur, but their shouldn't be any in our implementation
	}

	/**
	 * Removes a node specified by name.
	 * @param name The name of a node to remove. When this <code>NamedNodeMap</code> contains the attributes attached to an element, as returned by the attributes
	 *          attribute of the <code>Node</code> interface, if the removed attribute is known to have a default value, an attribute immediately appears
	 *          containing the default value.
	 * @return The node removed from the map if a node with such a name exists.
	 * @throws DOMException <ul>
	 *           <li>NOT_FOUND_ERR: Raised if there is no node named <code>name</code> in the map.</li>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this map is readonly.</li>
	 *           </ul>
	 * @version DOM Level 1
	 */
	public Node removeNamedItem(String name) throws DOMException {
		//TODO check read-only here, and elsewhere
		Node existingNode = (Node)remove(name); //remove the node with this name, and store the the node if it existed; we'll allow runtime errors to occur, which shouldn't happen in our implementation, anyway
		if(existingNode != null) //if a node exists
			return existingNode; //return that node
		else
			//if the node didn't exist to begin with
			throw new XMLDOMException(DOMException.NOT_FOUND_ERR, new Object[] { name }); //show that we couldn't find the existing node
		//TODO do whatever is needed for bringing default nodes in
	}

	/**
	 * Returns the <code>index</code>th item in the map. If <code>index</code> is less than zero or greater than or equal to the number of nodes in the map, this
	 * returns <code>null</code>.
	 * @param index Index into the map.
	 * @return The node at the <code>index</code>th position in the <code>NamedNodeMap</code>, or <code>null</code> if that is not a valid index.
	 * @version DOM Level 1
	 */
	public Node item(int index) {
		if(index >= 0 && index < size()) //if the index is in range
			return (Node)values().toArray()[index]; //get a collection of the values, convert that to an array, and return the correct indexed node in the array
		else
			//if the index is out of range
			return null; //return null
	}

	/**
	 * Returns the number of nodes in the map. The range of valid child node indices is 0 to <code>length-1</code> inclusive.
	 * @return The number of nodes in the map
	 * @version DOM Level 1
	 */
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

	/**
	 * Retrieves a node specified by local name and namespace URI.
	 * @param namespaceURI The namespace URI of the node to retrieve.
	 * @param localName The local name of the node to retrieve.
	 * @return A <code>Node</code> (of any type) with the specified local name and namespace URI, or <code>null</code> if they do not identify any node in this
	 *         map.
	 * @since DOM Level 2
	 */
	public Node getNamedItemNS(String namespaceURI, String localName) {
		//TODO check to see if either namespaceURI or localName is null
		return getNamedItem(getNamespaceKey(namespaceURI, localName)); //get the named item from its namespace key
	}

	/**
	 * Adds a node using its <code>namespaceURI</code> and <code>localName</code>. If a node with that namespace URI and that local name is already present in
	 * this map, it is replaced by the new one.
	 * @param arg A node to store in this map. The node will later be accessible using the value of its <code>namespaceURI</code> and <code>localName</code>
	 *          attributes.
	 * @return If the new <code>Node</code> replaces an existing node the replaced <code>Node</code> is returned, otherwise <code>null</code> is returned.
	 * @throws DOMException <ul>
	 *           <li>WRONG_DOCUMENT_ERR: Raised if <code>arg</code> was created from a different document than the one that created this map.</li>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this map is readonly.</li>
	 *           <li>INUSE_ATTRIBUTE_ERR: Raised if <code>arg</code> is an <code>Attr</code> that is already an attribute of another <code>Element</code> object.
	 *           The DOM user must explicitly clone <code>Attr</code> nodes to re-use them in other elements.</li>
	 *           </ul>
	 * @since DOM Level 2
	 */
	public Node setNamedItemNS(Node arg) throws DOMException {
		//TODO check to make sure the node has the same document
		//TODO check to see if we are readonly
		//TODO check the node to see if it is used by another attribute
		//TODO what's that "special" string name clash thing above?
		return (Node)put(getNamespaceKey(arg.getNamespaceURI(), arg.getLocalName()), arg); //add the node, using its combined namespace URI and local name as the key, and return the previously existing node, if any; we'll let runtime exceptions occur, but there shouldn't be any in our implementation
	}

	/**
	 * Removes a node specified by local name and namespace URI. A removed attribute may be known to have a default value when this map contains the attributes
	 * attached to an element, as returned by the attributes attribute of the <code>Node</code> interface. If so, an attribute immediately appears containing the
	 * default value as well as the corresponding namespace URI, local name, and prefix when applicable.
	 * @param namespaceURI The namespace URI of the node to remove.
	 * @param localNameThe local name of the node to remove.
	 * @return The node removed from this map if a node with such a local name and namespace URI exists.
	 * @throws DOMException <ul>
	 *           <li>NOT_FOUND_ERR: Raised if there is no node with the specified <code>namespaceURI</code> and <code>localName</code> in this map.</li>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this map is readonly.</li>
	 *           </ul>
	 * @since DOM Level 2
	 */
	public Node removeNamedItemNS(String namespaceURI, String localName) throws DOMException {
		return removeNamedItem(getNamespaceKey(namespaceURI, localName)); //remove the item with a key name composed of the namespace URI and the local name
	}

}
