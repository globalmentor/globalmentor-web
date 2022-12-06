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

import java.io.*;
import java.util.*;

import org.w3c.dom.Attr;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.DOMException;
import org.w3c.dom.TypeInfo;

import com.globalmentor.xml.def.XML;

/**
 * An element in an XML document.
 * @author Garret Wilson
 * @see XMLNode
 * @see org.w3c.dom.Element
 * @deprecated
 */
@Deprecated
public class XMLElement extends XMLNode implements org.w3c.dom.Element {

	/**
	 * Constructor which requires an owner document to be specified.
	 * @param ownerDocument The document which owns this node.
	 */
	public XMLElement(final XMLDocument ownerDocument) {
		super(XMLNode.ELEMENT_NODE, ownerDocument); //construct the parent class
	}

	/**
	 * Constructor that uses a specified name. Namespace-related names will be set to <code>null</code>.
	 * @param ownerDocument The document which owns this node.
	 * @param newName The new name for the XML element, or "" to specify no name. //TODO fix @throws XMLInvalidNameException Thrown if the specified name is
	 *          invalid.
	 */
	public XMLElement(final XMLDocument ownerDocument, final String newName) { //TODO fix throws XMLInvalidNameException
		this(ownerDocument); //do the default constructing
		setNodeName(newName); //set the node name
	}

	/**
	 * Constructor that creates an element from an existing tag. All attributes from the tag will be used.
	 * @param ownerDocument The document which owns this node.
	 * @param elementTag The tag which begins the element. //TODO fix @throws XMLInvalidNameException Thrown if the specified name is invalid.
	 */
	public XMLElement(final XMLDocument ownerDocument, final XMLTag elementTag) { //TODO fix throws XMLInvalidNameException
		this(ownerDocument); //do the default constructing
		setNodeName(elementTag.getNodeName()); //set the node name from the tag
		setAttributeXMLNamedNodeMap(elementTag.getAttributeXMLNamedNodeMap()); //give the element the same list of attributes that the tag formed from the file
	}

	/**
	 * Constructor that uses a specified name and namespace URI. The element's names will be constructed in the following fashion:
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
	 * <td><code>Element.tagName</code></td>
	 * <td><code>qualifiedName</code></td>
	 * </tr>
	 * </table>
	 * @param ownerDocument The document which owns this node.
	 * @param namespaceURI The namespace URI of the element.
	 * @param qualifiedName The qualified name of the element.
	 * @throws DOMException
	 *           <ul>
	 *           <li>NAMESPACE_ERR: Raised if the specified <code>prefix</code> is malformed, if the <code>namespaceURI</code> of is <code>null</code>, if the
	 *           specified prefix is "xml" and the <code>namespaceURI</code> of this node is different from "http://www.w3.org/XML/1998/namespace", if this node
	 *           is an attribute and the specified prefix is "xmlns" and the <code>namespaceURI</code> of this node is different from
	 *           "http://www.w3.org/2000/xmlns/", or if this node is an attribute and the <code>qualifiedName</code> of this node is "xmlns".</li>
	 *           </ul>
	 */
	public XMLElement(final XMLDocument ownerDocument, final String namespaceURI, final String qualifiedName) {
		this(ownerDocument); //do the default constructing
		setNamespaceURI(namespaceURI); //set the namespace URI
		setNodeNameNS(qualifiedName); //set the node name, correctly extracting namespace information
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
		final XMLElement clone; //we'll assign a clone to this variable
		final String localName = getLocalName(); //get the local name
		if(localName != null) //if we have a local name, this element recognizes namespace
			clone = new XMLElement(getOwnerXMLDocument(), getNamespaceURI(), getNodeName()); //create a new element with the same owner document, namespace, and name
		else
			//if we don't have a local name, this is not a namespace-aware element
			clone = new XMLElement(getOwnerXMLDocument(), getNodeName()); //create a new element with the same owner document and the same name
		clone.setAttributeXMLNamedNodeMap(getAttributeXMLNamedNodeMap().cloneDeep()); //clone the attributes and put them in the clone
		return clone; //return our cloned node
	}

	/**
	 * The map of attributes in this element.
	 * @see XMLAttribute
	 * @see XMLNamedNodeMap
	 */
	private XMLNamedNodeMap AttributeMap = new XMLNamedNodeMap();

	/**
	 * Returns an <code>XMLNamedNodeMap</code> containing the attributes of this element. For the DOM version, see <code>getAttributes</code>.
	 * @return A map of attributes of this element node.
	 * @see XMLNode#getAttributes
	 */
	private XMLNamedNodeMap getAttributeXMLNamedNodeMap() {
		return AttributeMap;
	} //TODO probably remove this in favor of the DOM version

	@Override
	public NamedNodeMap getAttributes() {
		return getAttributeXMLNamedNodeMap();
	}

	/**
	 * Sets the attributes for this element. This has package access so helper classes such as <code>XMLNamespaceProcessor</code> can access this method.
	 * @param attributeMap The <code>XMLNamedNodeMap</code> with the attributes for this element.
	 * @see XMLNamespaceProcessor
	 */
	void setAttributeXMLNamedNodeMap(final XMLNamedNodeMap attributeMap) {
		AttributeMap = attributeMap;
	}

	/**
	 * Gets an attribute object with a certain name.
	 * @param attributeName The name of the desired attribute.
	 * @return A reference to the attribute object with the specified name, or null if no attribute with that name exists.
	 */
	/*TODO fix
			public XMLAttribute getAttribute(final String attributeName)
			{
				return (XMLAttribute)getAttributeXMLNamedNodeMap().get(attributeName);	//return the attribute with the given name
			}
	*/

	/**
	 * Gets the value of an attribute with a certain name.
	 * @param attributeName The name of the desired attribute.
	 * @return The value of the attribute with the specified name, or "" if no such named attribute exists.
	 */
	/*TODO fix
			public String getAttributeValue(final String attributeName)
			{
				final XMLAttribute attribute=getAttribute(attributeName);	//find the attribute with the given name
				if(attribute!=null)	//if an attribute exists with that name
					return attribute.getNodeValue();	//return the name of that attribute
				else	//if no attribute has the specified name
					return "";	//return an empty string
			}
	*/

	/**
	 * Gets the integer value of an attribute with a certain name.
	 * @param attributeName The name of the desired attribute.
	 * @return The integer value of the attribute with the specified name, or 0 if no such named attribute exists or there was an invalid integer.
	 */
	/*TODO fix
			public int getAttributeIntegerValue(final String attributeName)
			{
				final String attributeValue=getAttributeValue(attributeName);	//get the string value of the attribute
				try
				{
					return Integer.parseInt(attributeValue);	//convert the value to an integer and return it
				}
				catch(NumberFormatException e) {	//if this was an invalid number
					return 0;	//return zero as the default TODO maybe later make a function that specifies the default
				}
			}
	*/

	/**
	 * Gets the boolean value of an attribute with a certain name.
	 * @param attributeName The name of the desired attribute.
	 * @return The boolean value of the attribute with the specified name, or false if no such named attribute exists or there was an invalid boolean value.
	 */
	/*TODO fix
			public boolean getAttributeBooleanValue(final String attributeName)
			{
				final String attributeValue=getAttributeValue(attributeName);	//get the string value of the attribute
				try
				{
					return Boolean.valueOf(attributeValue).booleanValue();	//convert the value to a boolean and return it
				}
				catch(NumberFormatException e) {	//if this was an invalid number
					return false;	//return false as the default TODO maybe later make a function that specifies the default
				}
			}
	*/

	/**
	 * Function override to specify the types of nodes that can be child nodes.
	 * @return Whether or not the specified node can be added to the list of children.
	 * @see XMLNode#isNodeTypeAllowed
	 */
	protected boolean isNodeTypeAllowed(final Node node) {
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

	//TODO it would probably be best to find a way not to have to typecast all of the getChildNodes() calls below

	//TODO maybe make an entities() function here

	@Override
	public String getTagName() {
		return getNodeName();
	} //TODO check to see if this is an HTML document and canonicize the names if so

	@Override
	public String getAttribute(String name) {
		final XMLAttribute attribute = (XMLAttribute)getAttributeNodeNS(null, name); //see if we can get a matching attribute node
		//TODO bring back and fix		final XMLAttribute attribute=(XMLAttribute)getAttributeNode(name);	//see if we can get a matching attribute node
		return attribute != null ? attribute.getNodeValue() : ""; //return the value if we found an attribute, otherwise return an empty string
	}

	@Override
	public void setAttribute(String name, String value) throws DOMException {
		//TODO check for valid name characters here
		//TODO check for read-only status here
		XMLAttribute attribute; //we'll use this to either find an existing attribute or create a new one
		if(getAttributeXMLNamedNodeMap().containsKey(name)) //if an attribute with this name already exists
			attribute = (XMLAttribute)getAttributeXMLNamedNodeMap().getNamedItem(name); //get a reference to the attribute
		else { //if this attribute doesn't exist
			attribute = new XMLAttribute(getOwnerXMLDocument(), name); //create an attribute with the specified name
			getAttributeXMLNamedNodeMap().setNamedItem(attribute); //add this attribute to our map
		}
		attribute.setValue(value); //set the value of the attribute TODO check about setValue() setNodeValue() and such
	}

	@Override
	public void removeAttribute(String name) throws DOMException {
		//TODO check read-only status here
		if(hasAttribute(name)) //if the attribute exists (needed because this function in the DOM throws now not-found exception)
			getAttributeXMLNamedNodeMap().removeNamedItem(name); //remove any attribute with this name, if there is one
		//TODO check to see if there's a default value that we should add
	}

	@Override
	public Attr getAttributeNode(String name) {
		if(hasAttribute(name)) { //if an attribute with this name exists
			return (XMLAttribute)getAttributeXMLNamedNodeMap().getNamedItem(name); //return a reference to the attribute
		} else
			//if this attribute doesn't exist
			return null; //return null
	}

	@Override
	public Attr setAttributeNode(Attr newAttr) throws DOMException {
		//TODO make sure this is the correct document
		//TODO check for read-only status
		//TODO check to see if the attribute is already in use
		return (Attr)getAttributeXMLNamedNodeMap().setNamedItem(newAttr); //add this attribute to our map
	}

	@Override
	public Attr removeAttributeNode(Attr oldAttr) throws DOMException {
		return null;
	} //TODO fix

	@Override
	public NodeList getElementsByTagName(String name) { //TODO does this correctly return a *preorder* traversal?
		//TODO we may want to just remove the function below and replace it with a direct call to getNodesByName()
		return getNodesByName(ELEMENT_NODE, name, true); //get the elements, showing that we want *all* the child elements at whatever level
	}

	@Override
	public String getAttributeNS(String namespaceURI, String localName) {
		final XMLAttribute attribute = (XMLAttribute)getAttributeNodeNS(namespaceURI, localName); //see if we can get a matching attribute node
		return attribute != null ? attribute.getNodeValue() : ""; //return the value if we found an attribute, otherwise return an empty string
	}

	@Override
	public void setAttributeNS(String namespaceURI, String qualifiedName, String value) throws DOMException {
		//TODO check for valid name characters here
		//TODO check for read-only status here
		final String localName = XML.getLocalName(qualifiedName); //get the local name of the qualified name
		XMLAttribute attribute; //we'll use this to either find an existing attribute or create a new one
		if(getAttributeXMLNamedNodeMap().containsKeyNS(namespaceURI, localName)) { //if an attribute with this name already exists
			attribute = (XMLAttribute)getAttributeXMLNamedNodeMap().getNamedItemNS(namespaceURI, localName); //get a reference to that attribute
			final String prefix = XML.findPrefix(qualifiedName).orElse(null); //get the prefix of the qualified name
			attribute.setPrefix(prefix); //update the prefix
			attribute.setValue(value); //update the attribute's value
		} else { //if this attribute doesn't exist
			attribute = new XMLAttribute(getOwnerXMLDocument(), namespaceURI, qualifiedName, value); //create an attribute with the specified name and value
			getAttributeXMLNamedNodeMap().setNamedItemNS(attribute); //add this attribute to our map
		}
	}

	@Override
	public void removeAttributeNS(String namespaceURI, String localName) throws DOMException {
		//TODO check read-only status here
		if(hasAttributeNS(namespaceURI, localName)) //if the attribute exists (needed because this function in the DOM throws now not-found exception)
			getAttributeXMLNamedNodeMap().removeNamedItemNS(namespaceURI, localName); //remove any attribute with this name, if there is one
		//TODO check to see if there's a default value that we should add
	}

	@Override
	public Attr getAttributeNodeNS(String namespaceURI, String localName) {
		if(hasAttributeNS(namespaceURI, localName)) { //if an attribute with this name exists
			return (XMLAttribute)getAttributeXMLNamedNodeMap().getNamedItemNS(namespaceURI, localName); //return a reference to the attribute
		} else
			//if this attribute doesn't exist
			return null; //return null
	}

	@Override
	public Attr setAttributeNodeNS(Attr newAttr) throws DOMException {
		//TODO make sure this is the correct document
		//TODO check for read-only status
		//TODO check to see if the attribute is already in use
		return (Attr)getAttributeXMLNamedNodeMap().setNamedItemNS(newAttr); //add this attribute to our map
	}

	@Override
	public NodeList getElementsByTagNameNS(String namespaceURI, String localName) {
		return getNodesByNameNS(ELEMENT_NODE, namespaceURI, localName, true); //get the elements that match, showing that we want *all* the child elements at whatever level
	}

	@Override
	public boolean hasAttribute(String name) {
		return ((XMLNamedNodeMap)getAttributes()).containsKey(name); //return whether there's a node in our attribute map with the given name for a key
	}

	@Override
	public boolean hasAttributeNS(String namespaceURI, String localName) {
		return ((XMLNamedNodeMap)getAttributes()).containsKeyNS(namespaceURI, localName); //return whether there's a node in our attribute map with the given namespace URI and local name for a key
	}

	@Override
	public TypeInfo getSchemaTypeInfo() {
		throw new UnsupportedOperationException();
	} //TODO fix for DOM 3

	@Override
	public void setIdAttribute(String name, boolean isId) throws DOMException {
		throw new UnsupportedOperationException();
	} //TODO fix for DOM 3

	@Override
	public void setIdAttributeNS(String namespaceURI, String localName, boolean isId) throws DOMException {
		throw new UnsupportedOperationException();
	} //TODO fix for DOM 3

	@Override
	public void setIdAttributeNode(Attr idAttr, boolean isId) throws DOMException {
		throw new UnsupportedOperationException();
	} //TODO fix for DOM 3

	@Override
	public String getTextContent() throws DOMException { //TODO fix for DOM 3 in XMLNode
		return com.globalmentor.xml.XmlDom.getText(this, true);
	}

}
