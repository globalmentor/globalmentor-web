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

import java.util.LinkedList;
import org.w3c.dom.Node;

/**
 * A list of nodes in an XML document.
 * @author Garret Wilson
 * @see XMLNode
 * @see com.globalmentor.xml.dom.impl.traversal.XMLNodeIterator
 * @see org.w3c.dom.Node
 * @see org.w3c.dom.NodeList
 * @deprecated
 */
@Deprecated
public class XMLNodeList extends LinkedList implements org.w3c.dom.NodeList {

	/**
	 * Creates and returns a duplicate copy of this node list with no values.
	 * @return A duplicate "shallow clone" copy of this node list.
	 * @see XMLNode#cloneXMLNode
	 * @see XMLNode#cloneNode
	 * @see XMLNodeList#cloneDeep
	 * @see Object#clone
	 */
	public Object clone() {
		return new XMLNodeList(); //create a new node list and return it
	}

	/**
	 * Creates and returns a duplicate copy of this node list containing clones of all its children.
	 * @return A duplicate "deep clone" copy of this node list.
	 * @see XMLNode#cloneXMLNode
	 * @see XMLNode#cloneNode
	 * @see XMLNodeList#clone
	 */
	public XMLNodeList cloneDeep() {
		final XMLNodeList clone = (XMLNodeList)clone(); //create a new node list
		for(int i = 0; i < size(); ++i)
			//look at each node in our list
			clone.add(((XMLNode)get(i)).cloneXMLNode(true)); //deep clone this node and store it in our node list clone
		return clone; //return our cloned node list
	}

	//	/**
	//	 * Appends the specified node to the end of this list, and sets the node's
	//	 * 
	//	 * getParentNode
	//	 * 
	//	 *
	//	 * @param o element to be appended to this list.
	//	 * @return <code>true</code> (as per the general contract of Collection.add).
	//	 */
	//TODO fix		public boolean add(Object o) {

	@Override
	public Node item(int index) {
		try {
			return (Node)get(index); //return the object at the index
		} catch(IndexOutOfBoundsException e) { //if they don't give a valid index
			return null; //return null instead of throwing an exception
		}
	}

	@Override
	public int getLength() {
		return size();
	}

}
