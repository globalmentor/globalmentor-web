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

package com.globalmentor.text.xml.processor;

import static com.globalmentor.text.xml.XML.*;

import org.w3c.dom.*;

/**
 * A lightweight portion of an XML document.
 * @see XMLNode
 * @author Garret Wilson
 * @deprecated
 */
public class XMLDocumentFragment extends XMLNode implements DocumentFragment {

	/**
	 * Default constructor for a document fragment. Since a document fragment has no owner document, its owner document will be set to <code>null</code>.
	 */
	public XMLDocumentFragment() {
		super(DOCUMENT_FRAGMENT_NODE, null); //show that we have no owner document
		setNodeName(DOCUMENT_FRAGMENT_NODE_NAME); //set the appropriate name for a document fragment
	}

	/**
	 * Function override to specify the types of nodes that can be child nodes.
	 * @return Whether or not the specified node can be added to the list of children.
	 * @see XMLNode#isNodeTypeAllowed
	 */
	protected boolean isNodeTypeAllowed(final Node node) { //TODO make sure these are the right nodes for DocumentFragment to accept
		switch(node.getNodeType()) { //see which type of node this is
			case ELEMENT_NODE: //if we accept one of these types of child nodes
			case TEXT_NODE:
			case COMMENT_NODE:
			case PROCESSING_INSTRUCTION_NODE:
			case CDATA_SECTION_NODE:
			case ENTITY_REFERENCE_NODE:
				return true; //show that we accept it
			default: //if this is another type of node
				return false; //show that we don't accept it
		}
	}

	/**
	 * Creates and returns a duplicate copy of this node. The clone has no parent. This function creates a "shallow" clone which does not contain clones of all
	 * child nodes. For the DOM version, see cloneNode().
	 * @return A duplicate copy of this node.
	 * @see XMLNode#cloneXMLNode
	 * @see XMLNode#cloneNode
	 * @see XMLNode#getParentXMLNode
	 */
	public Object clone() {
		return new XMLDocumentFragment(); //create a new document fragment TODO this isn't right; we need to copy the child nodes, even if we don't clone them
	}

}
