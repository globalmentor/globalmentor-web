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

import static com.globalmentor.text.CharacterEncoding.*;

/**
 * XML declaration in an XML document.
 * @author Garret Wilson
 * @see XMLNode
 * @deprecated
 */
public class XMLDeclaration extends XMLNode {

	/** The XML version of this document. */
	private String Version = "";

	/** @return The XML version. */
	public String getVersion() {
		return Version;
	}

	/**
	 * Sets the XML version.
	 * @param newVersion The new XML version.
	 */
	public void setVersion(String newVersion) {
		Version = newVersion;
	}

	/** The encoding of this document, which defaults to UTF-8. */
	private String Encoding = UTF_8;

	/** @return The encoding. */
	public String getEncoding() {
		return Encoding;
	}

	/**
	 * Sets the encoding.
	 * @param newEncoding The new encoding.
	 */
	public void setEncoding(String newEncoding) {
		Encoding = newEncoding;
	}

	/** Whether the document is standalone. Defaults to <code>false</code>. */
	private boolean Standalone = false;

	/** @return Whether the document is standalone.. */
	public boolean isStandalone() {
		return Standalone;
	}

	/**
	 * Sets the whether the document is standalone.
	 * @param newStandalone Whether the document should be standalone.
	 */
	public void setStandalone(boolean newStandalone) {
		Standalone = newStandalone;
	}

	/*Constructor which requires an owner document to be specified.
	@param ownerDocument The document which owns this node.
	*/
	public XMLDeclaration(final XMLDocument ownerDocument) {
		super(XMLNode.XMLDECL_NODE, ownerDocument); //construct the parent class
		//TODO what about specifying a constant name, here?
	}

	/**
	 * Creates and returns a duplicate copy of this node. The clone has no parent. This function creates a "shallow" clone which does not contain clones of all
	 * child nodes. For the DOM version, see cloneNode().
	 * @return A duplicate copy of this node.
	 * @see XMLNode#cloneXMLNode
	 * @see XMLNode#cloneNode
	 * @see XMLNode#getParentXMLNode
	 */
	/*TODO fix cloning
		public Object clone()
		{
			return new XMLCDATA(getOwnerDocument(), getNodeValue());	//create a new node with the same owner document and the same value
		}
	*/

	//TODO fix all the DOM stuff

}
