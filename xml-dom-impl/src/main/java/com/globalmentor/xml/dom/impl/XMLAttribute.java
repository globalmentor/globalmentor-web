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

import org.w3c.dom.Element;
import org.w3c.dom.DOMException;
import org.w3c.dom.TypeInfo;

/**
 * An attribute of a tag (and therefore of an element).
 * @author Garret Wilson
 * @see XMLTag
 * @see XMLElement
 * @see org.w3c.dom.Attr
 * @deprecated
 */
@Deprecated
public class XMLAttribute extends XMLNode implements org.w3c.dom.Attr {

	/**
	 * Constructor for an attribute taking a name.
	 * @param ownerDocument The document which owns this node.
	 * @param newName The new name of the attribute. //TODO fix for DOM @throws XMLInvalidNameException Thrown if the specified name is invalid.
	 */
	public XMLAttribute(final XMLDocument ownerDocument, final String newName) { //TODO fix for DOM throws XMLInvalidNameException
		super(XMLNode.ATTRIBUTE_NODE, ownerDocument); //construct the parent class
		setNodeName(newName); //set the name
	}

	/**
	 * Constructor that uses a specified name and namespace URI. The attributes's names and values will be constructed in the following fashion:
	 * <table>
	 * <caption>Determination of note attributes from parameter values.</caption>
	 * <tr>
	 * <th>Attribute</th>
	 * <th>Value</th>
	 * </tr>
	 * <tr>
	 * <td><code>Node.nodeName</code></td>
	 * <td>qualifiedName</td>
	 * </tr>
	 * <tr>
	 * <td><code>Node.namespaceURI</code></td>
	 * <td><code>namespaceURI</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>Node.prefix</code></td>
	 * <td>prefix, extracted from <code>qualifiedName</code>, or <code>null</code> if there is no prefix</td>
	 * </tr>
	 * <tr>
	 * <td><code>Node.localName</code></td>
	 * <td>local name, extracted from <code>qualifiedName</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>Attr.name</code></td>
	 * <td><code>qualifiedName</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>Node.nodeValue</code></td>
	 * <td>the empty string</td>
	 * </tr>
	 * </table>
	 * @param ownerDocument The document which owns this node.
	 * @param namespaceURI The namespace URI of the attribute.
	 * @param qualifiedName The qualified name of the attribute.
	 * @param newValue The new value of the attribute.
	 * @throws DOMException
	 *           <ul>
	 *           <li>NAMESPACE_ERR: Raised if the specified <code>prefix</code> is malformed, if the <code>namespaceURI</code> of is <code>null</code>, if the
	 *           specified prefix is "xml" and the <code>namespaceURI</code> of this node is different from "http://www.w3.org/XML/1998/namespace", if this node
	 *           is an attribute and the specified prefix is "xmlns" and the <code>namespaceURI</code> of this node is different from
	 *           "http://www.w3.org/2000/xmlns/", or if this node is an attribute and the <code>qualifiedName</code> of this node is "xmlns".</li>
	 *           </ul>
	 */
	public XMLAttribute(final XMLDocument ownerDocument, final String namespaceURI, final String qualifiedName, final String newValue) {
		super(XMLNode.ATTRIBUTE_NODE, ownerDocument); //construct the parent class
		setNamespaceURI(namespaceURI); //set the namespace URI
		setNodeNameNS(qualifiedName); //set the node name, correctly extracting namespace information
		setNodeValue(newValue); //set the value TODO do we want to have an XMLNode constructor that accepts a value as well?
	}

	/**
	 * Constructor for an attribute taking both a name and a value.
	 * @param ownerDocument The document which owns this node.
	 * @param newName The new name of the attribute.
	 * @param newValue The new value of the attribute. //TODO fix for DOM @throws XMLInvalidNameException Thrown if the specified name is invalid.
	 */
	public XMLAttribute(final XMLDocument ownerDocument, final String newName, final String newValue) { //TODO fix for DOM throws XMLInvalidNameException
		this(ownerDocument, newName); //do the default constructing
		setNodeValue(newValue); //set the value TODO do we want to have an XMLNode constructor that accepts a value as well?
	}

	/**
	 * Constructor for an attribute taking a name.
	 * @param ownerDocument The document which owns this node.
	 * @param newName The new name of the attribute.
	 * @param newLineIndex The new line index.
	 * @param newCharIndex The new character index. //TODO fix for DOM @throws XMLInvalidNameException Thrown if the specified name is invalid.
	 */
	public XMLAttribute(final XMLDocument ownerDocument, final String newName, final long newLineIndex, final long newCharIndex) { //TODO fix for DOM throws XMLInvalidNameException
		this(ownerDocument, newName); //do the default constructing
		setLineIndex(newLineIndex); //set the line index
		setCharIndex(newCharIndex); //set the character index
	}

	/**
	 * Constructor for an attribute taking both a name and a value.
	 * @param ownerDocument The document which owns this node.
	 * @param newName The new name of the attribute.
	 * @param newValue The new value of the attribute.
	 * @param newLineIndex The new line index.
	 * @param newCharIndex The new character index. //TODO fix for DOM @throws XMLInvalidNameException Thrown if the specified name is invalid.
	 */
	public XMLAttribute(final XMLDocument ownerDocument, final String newName, final String newValue, final long newLineIndex, final long newCharIndex) { //TODO fix for DOM throws XMLInvalidNameException
		this(ownerDocument, newName, newValue); //do the default constructing
		setLineIndex(newLineIndex); //set the line index
		setCharIndex(newCharIndex); //set the character index
	}

	//TODO maybe make this have package level access or something
	/**
	 * Sets the name of the attribute.
	 * @param newName The new name for the attribute, or "" to specify no name. //TODO fix for DOM @throws XMLInvalidNameException Thrown if the specified name is
	 *          invalid.
	 */
	/*TODO fix exceptions and stuff and reconcile with DOM
			public void setName(String newName) {	//TODO fix for DOM throws XMLInvalidNameException
	//TODO fix for DOM			if(newName.length()!=0)	//don't check empty strings, because empty strings really aren't valid XML names, and we want to allow clearing the name
	//TODO fix for DOM				XMLProcessor.checkValidName(newName);	//make sure the name is valid
				Name=newName;
			}
	*/

	/** The value of the attribute. */
	private String Value = "";

	@Override
	public String getNodeValue() throws DOMException {
		return Value;
	}

	@Override
	public void setNodeValue(String nodeValue) throws DOMException {
		Value = nodeValue; //set the value
		setSpecified(true); //show that this value has been specified
	}

	//TODO fix these different value types, and maybe move them to node or something
	/**
	 * Gets the integer value of the attribute.
	 * @throws NumberFormatException Thrown when the value being read is not a valid integer.
	 * @return The integer value of the attribute.
	 */
	public int getIntegerValue() throws NumberFormatException {
		return Integer.parseInt(getNodeValue()); //convert the value to an integer and return it
	}

	/**
	 * Gets the boolean value of the attribute.
	 * @param attributeName The name of the attribute.
	 * @throws NumberFormatException Thrown when the value being read is not a valid boolean value.
	 * @return The boolean value of the attribute with the specified name.
	 */
	public boolean getAttributeBooleanValue(final String attributeName) throws NumberFormatException {
		return Boolean.valueOf(getNodeValue()).booleanValue(); //convert the value to a boolean and return it
	}

	/** The owner of this attribute, or null if there is no owner. */
	private XMLElement OwnerElement = null;

	/**
	 * Returns the element node to which this attribute is attached, or <code>null</code> if this attribute is not in use. For the DOM version, see
	 * getOwnerElement().
	 * @return The owner of the attribute, or <code>null</code> if this attribute is not in use.
	 * @see XMLAttribute#getOwnerElement
	 * @see XMLElement
	 */
	public XMLElement getXMLOwnerElement() {
		return OwnerElement;
	}

	/**
	 * Sets the owner of this attribute.
	 * @param newOwnerElement The new owner of the attribute.
	 * @see XMLElement
	 */
	public void setOwnerXMLElement(XMLElement newOwnerElement) {
		OwnerElement = newOwnerElement;
	}

	@Override
	public Element getOwnerElement() {
		return getXMLOwnerElement();
	}

	/**
	 * Sets the namespace prefix of this node. This overridden function ensures that an attribute can never have the name "xmlns".
	 * <p>
	 * Note that setting this attribute, when permitted, changes the <code>nodeName</code> attribute, which holds the qualified name, as well as the
	 * <code>tagName</code> and <code>name</code> attributes of the <code>Element</code> and <code>Attr</code> interfaces, when applicable.
	 * </p>
	 * <p>
	 * Note also that changing the prefix of an attribute that is known to have a default value, does not make a new attribute with the default value and the
	 * original prefix appear, since the <code>namespaceURI</code> and <code>localName</code> do not change.
	 * </p>
	 * @throws DOMException
	 *           <ul>
	 *           <li>INVALID_CHARACTER_ERR: Raised if the specified prefix contains an illegal character.</li>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly.</li>
	 *           <li>NAMESPACE_ERR: Raised if the specified <code>prefix</code> is malformed, if the <code>namespaceURI</code> of this node is <code>null</code>,
	 *           if the specified prefix is "xml" and the <code>namespaceURI</code> of this node is different from "http://www.w3.org/XML/1998/namespace", if this
	 *           node is an attribute and the specified prefix is "xmlns" and the <code>namespaceURI</code> of this node is different from
	 *           "http://www.w3.org/2000/xmlns/", or if this node is an attribute and the <code>qualifiedName</code> of this node is "xmlns".</li>
	 *           </ul>
	 */
	/*TODO fix; this doesn't check the namespace URI, and it doesn't even update the prefix by calling the parent version
		public void setPrefix(String prefix) throws DOMException
		{
			if(prefix!=null && prefix.equals(XMLConstants.XMLNS_NAMESPACE_PREFIX))  //if this is the "xmlns" prefix
			  throw new XMLDOMException(DOMException.NAMESPACE_ERR, new Object[]{prefix});	//show that this namespace isn't allowed
		}
	*/

	/**
	 * Creates and returns a duplicate copy of this node. The clone has no parent. This function creates a "shallow" clone which does not contain clones of all
	 * child nodes. For the DOM version, see cloneNode().
	 * @return A duplicate copy of this node.
	 * @see XMLNode#cloneXMLNode
	 * @see XMLNode#cloneNode
	 * @see XMLNode#getParentXMLNode
	 */
	public Object clone() {
		return new XMLAttribute(getOwnerXMLDocument(), getNamespaceURI(), getNodeName(), getNodeValue()); //create a duplicate attribute TODO eventually put all line and char index information elsewhere, like in the parser
	}

	/**
	 * The index of the line on which this attribute begins, or -1 if unknown
	 * @see CharIndex
	 */
	private long LineIndex = -1;

	/**
	 * @return The index of the line on which this attribute begins, or -1 if unknown
	 * @see getCharIndex
	 */
	public long getLineIndex() {
		return LineIndex;
	}

	/**
	 * Sets the new line index of this attribute.
	 * @param newLineIndex The new line index.
	 * @see setCharIndex
	 */
	public void setLineIndex(final long newLineIndex) {
		LineIndex = newLineIndex;
	}

	/**
	 * The index of the character at which this attribute begins, or -1 if unknown.
	 * @see LineIndex
	 */
	private long CharIndex = -1;

	/**
	 * @return The index of the character at which this attribute begins, or -1 if unknown.
	 * @see getLineIndex
	 */
	public long getCharIndex() {
		return CharIndex;
	}

	/**
	 * Sets the new character index of this attribute.
	 * @param newCharIndex The new character index.
	 * @see setLineIndex
	 */
	public void setCharIndex(final long newCharIndex) {
		CharIndex = newCharIndex;
	}

	@Override
	public String getName() {
		return getNodeName();
	}

	/**
	 * Whether or not this attribute was specified by the document or the user. Defaults to <code>true</code>.
	 */
	private boolean Specified = true;

	@Override
	public boolean getSpecified() {
		return Specified;
	}

	/**
	 * Set whether this attribute was was explicitly given a value in the original document or by the user.
	 * @param specified The new specified value.
	 * @see XMLAttribute#setValue
	 */
	protected void setSpecified(final boolean specified) {
		Specified = specified;
	}

	@Override
	public String getValue() {
		return getNodeValue();
	}

	@Override
	public void setValue(String value) throws DOMException {
		setNodeValue(value);
	}

	@Override
	public TypeInfo getSchemaTypeInfo() {
		throw new UnsupportedOperationException();
	} //TODO fix for DOM 3

	@Override
	public boolean isId() {
		throw new UnsupportedOperationException();
	} //TODO fix for DOM 3

}
