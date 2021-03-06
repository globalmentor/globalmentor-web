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

import static com.globalmentor.xml.spec.XML.*;

/**
 * A CDATA section in an XML document.
 * @author Garret Wilson
 * @see XMLText
 * @see org.w3c.dom.CDATASection
 * @deprecated
 */
public class XMLCDATASection extends XMLText implements org.w3c.dom.CDATASection {

	/*Constructor which requires an owner document to be specified.
	@param ownerDocument The document which owns this node.
	*/
	public XMLCDATASection(final XMLDocument ownerDocument) {
		super(XMLNode.CDATA_SECTION_NODE, ownerDocument); //construct the parent class
		setNodeName(CDATASECTION_NODE_NAME); //set the appropriate name for a CDATA section (changing the name from the one set by the XMLText constructor)
	}

	/**
	 * Constructor that specifies the content of the CDATA.
	 * @param ownerDocument The document which owns this node.
	 * @param characterData The character data for the node.
	 */
	XMLCDATASection(final XMLDocument ownerDocument, final String characterData) {
		this(ownerDocument); //do the default constructing
		setData(characterData); //set the character data
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
		return new XMLCDATASection(getOwnerXMLDocument(), getNodeValue()); //create a new node with the same owner document and the same value
	}

}
