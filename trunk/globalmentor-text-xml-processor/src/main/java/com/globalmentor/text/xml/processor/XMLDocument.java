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

import java.util.*;

import com.globalmentor.text.xml.processor.events.*;
import com.globalmentor.text.xml.processor.traversal.*;

import org.w3c.dom.*;
import org.w3c.dom.events.*;
import org.w3c.dom.traversal.*;

/**
 * The entire XML document. Each node has an owner document.
 * @author Garret Wilson
 * @see XMLNode
 * @deprecated
 */
public class XMLDocument extends XMLNode implements Document, DocumentTraversal, DocumentEvent {

	//TODO should we override getNodeName() to return the correct name from the document type?

	/** Whether events are enabled. If set to <code>false</code>, no events will be generated. */
	private boolean eventsEnabled = true;

	/** @return Whether events are enabled. The default is <code>true</code>. */
	public boolean isEventsEnabled() {
		return eventsEnabled;
	}

	/**
	 * Sets whether events are enabled.
	 * @param newEventsEnabled Whether events should be enbabled. If set to <code>false</code>, no events will be generated.
	 */
	public void setEventsEnabled(final boolean newEventsEnabled) {
		eventsEnabled = newEventsEnabled;
	}

	/**
	 * Default constructor for a document. Since a document has no owner document, its owner document will be set to <code>null</code>.
	 */
	public XMLDocument() {
		super(XMLNode.DOCUMENT_NODE, null); //show that we have no owner document
		setXMLDeclaration(new XMLDeclaration(this)); //have a default XML declaration
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
		return new XMLDocument(); //create a new document TODO do we clone the document type and stuff as well?
	}

	/**
	 * Function override to specify the types of nodes that can be child nodes.
	 * @return Whether or not the specified node can be added to the list of children.
	 * @see XMLNode#isNodeTypeAllowed
	 */
	protected boolean isNodeTypeAllowed(final Node node) {
		switch(node.getNodeType()) { //see which type of node this is
			case ELEMENT_NODE: //if we accept one of these types of child nodes
			case PROCESSING_INSTRUCTION_NODE:
			case COMMENT_NODE:
			case DOCUMENT_TYPE_NODE:
				return true; //show that we accept it
			default: //if this is another type of node
				return false; //show that we don't accept it
		}
	}

	/**
	 * The list of stylesheets for this document, in order encountered.
	 * @see org.w3c.dom.stylesheets.StyleSheet
	 * @see com.globalmentor.text.xml.processor.stylesheets.XMLStyleSheet
	 * @see com.globalmentor.text.xml.processor.stylesheets.css.XMLCSSStyleSheet
	 */
	private List StyleSheetList = new ArrayList(); //TODO remove this proprietary stuff when we can

	/**
	 * @return The list of stylesheets for this document in order encountered.
	 * @see org.w3c.dom.stylesheets.StyleSheet
	 * @see com.globalmentor.text.xml.processor.stylesheets.XMLStyleSheet
	 * @see com.globalmentor.text.xml.processor.stylesheets.css.XMLCSSStyleSheet
	 */
	public List getStyleSheetList() {
		return StyleSheetList;
	} //TODO remove this proprietary stuff when we can

	/** The XML declaration of this XML document. */
	private XMLDeclaration XMLDecl;

	/** @return The XML declaration of this XML document. */
	public XMLDeclaration getXMLDeclaration() {
		return XMLDecl;
	}

	/**
	 * Sets the XML declaration of this XML document.
	 * @param xmlDecl The new XML declaration.
	 */
	public void setXMLDeclaration(final XMLDeclaration xmlDecl) {
		XMLDecl = xmlDecl;
	}

	/**
	 * Returns the root element of the document, or <code>null</code> if there is no document element. For the DOM version, see <code>getDocumentElement</code>.
	 * @return The root element of the document.
	 * @see XMLDocument#getDocumentElement
	 */
	//TODO there might be a more efficient way to implement this
	public XMLElement getDocumentXMLElement() {
		for(int i = 0; i < getChildXMLNodeList().size(); ++i) { //look at each of the child nodes
			final XMLNode node = (XMLNode)getChildXMLNodeList().get(i); //get a reference to this node
			if(node.getNodeType() == XMLNode.ELEMENT_NODE) //if this is an element node (there should only be one)
				return (XMLElement)node; //this node is the document element, so return it
		}
		return null; //we couldn't find a document element, so return null
	}

	/**
	 * Returns the child node that is the root element of this document, or <code>null</code> if there is no document element.
	 * @return The child node that is the root element of this document.
	 * @see XMLDocument#getDocumentXMLElement
	 * @version DOM Level 1
	 */
	public Element getDocumentElement() {
		return getDocumentXMLElement();
	}

	/**
	 * The Document Type Declaration associated with this XML document.
	 * @see XMLDocument#getXMLDocumentType
	 * @see XMLDocument#getDoctype
	 */
	private XMLDocumentType DocumentType = null;

	/**
	 * Returns the Document Type Declaration associated with this XML document. For the DOM version, see getDoctype().
	 * @return The Document Type Declaration information for this XML document, or <code>null</code> if there is no associated DTD.
	 * @see XMLDocument#getDoctype
	 */
	public XMLDocumentType getXMLDocumentType() {
		return DocumentType;
	} //TODO see if the document type should be a child, just like the document element

	/**
	 * Sets the document type for this XML document.
	 * @param documentType The new document type for this XML document, or <code>null</code> to specify no document type.
	 * @see XMLDocument#getXMLDocumentType
	 * @see XMLDocument#getDoctype
	 */
	public void setXMLDocumentType(final XMLDocumentType documentType) { //TODO this shouldn't be public; for now it's used by XHTMLTidier
		DocumentType = documentType; //set the document type
		if(documentType != null) //if they are actually setting a document type
			getXMLDocumentType().setOwnerXMLDocument(this); //show the document type which document owns it
	}

	/**
	 * Returns the Document Type Declaration associated with this XML document. For HTML documents as well as XML documents without a document type declaration
	 * this returns <code>null</code>. The DOM Level 2 does not support editing the Document Type Declaration, therefore docType cannot be altered in any way,
	 * including through the use of methods, such as insertNode or removeNode()/, inherited from <code>Node</code>.
	 * @return The Document Type Declaration information for this XML document, or <code>null</code> if there is no associated DTD.
	 * @see XMLDocument#getXMLDocumentType
	 */
	public DocumentType getDoctype() {
		return getXMLDocumentType();
	}

	/**
	 * The <code>DOMImplementation</code> object that handles this document. A DOM application may use objects from multiple implementations.
	 */
	public DOMImplementation getImplementation() {
		return new XMLDOMImplementation(); //return the XML DOM implementation these classes use TODO do we really want to create a new one each time? probably create one and use it until its weak reference is garbage collected
	}

	/**
	 * Creates an element of the type specified. If the document type has known attributes with default values, attribute nodes representing them are
	 * automatically created and attached to the element.<br />
	 * To create an element with a qualified name and namespace URI, use the createElementNS() method.
	 * @param tagName The name of the element type to instantiate. For XML, this is case-sensitive. For HTML, the <code>tagName</code> parameter may be provided
	 *          in any case, but it must be mapped to the canonical uppercase form by the DOM implementation.
	 * @return A new <code>Element</code> object.
	 * @throws DOMException <ul>
	 *           </li>INVALID_CHARACTER_ERR: Raised if the specified name contains an invalid character.</li>
	 *           </ul>
	 * @see XMLAttribute
	 * @see XMLElement
	 * @see XMLDocumentType
	 * @see XMLDocument#createElementNS
	 * @version DOM Level 1
	 */
	public Element createElement(String tagName) throws DOMException {
		final XMLElement element = new XMLElement(this, tagName); //create a new XML element with the specified name
		//TODO create default attributes if needed
		return element; //return the new element we constructed
	}

	/**
	 * Creates an empty <code>DocumentFragment</code> object.
	 * @return A new <code>DocumentFragment</code>.
	 */
	public DocumentFragment createDocumentFragment() {
		return new XMLDocumentFragment(); //create a new document fragment and return it
	}

	/**
	 * Creates a text node given the specified string.
	 * @param data The data for the node.
	 * @return The new text node object.
	 * @see XMLTextNode
	 * @version DOM Level 1
	 */
	public Text createTextNode(String data) {
		return new XMLText(this, data); //create a text node with the given data and return it
	}

	/**
	 * Creates a comment node given the specified string.
	 * @param data The data for the node.
	 * @return The new comment node object.
	 * @see XMLComment
	 * @version DOM Level 1
	 */
	public Comment createComment(String data) {
		return new XMLComment(this, data); //create a comment node with the given data and return it
	}

	/**
	 * Creates a CDATA section node whose value is the specified string.
	 * @param data The data for the CDATA section contents.
	 * @return The new CDATA section node object.
	 * @throws DOMException <ul>
	 *           <li>NOT_SUPPORTED_ERR: Raised if this document is an HTML document.</li>
	 *           </ul>
	 * @version DOM Level 1
	 */
	public CDATASection createCDATASection(String data) throws DOMException {
		//TODO check to see if this is an HTML document, and if so throw an exception
		return new XMLCDATASection(this, data); //create a CDATA section node with the given data and return it
	}

	/**
	 * Creates a <code>ProcessingInstruction</code> node given the specified name and data strings.
	 * @param target The target part of the processing instruction.
	 * @param data The data for the node.
	 * @return The new <code>ProcessingInstruction</code> object.
	 * @throws DOMException <ul>
	 *           <li>INVALID_CHARACTER_ERR: Raised if an invalid character is specified.</li>
	 *           <li>NOT_SUPPORTED_ERR: Raised if this document is an HTML document.</li>
	 */
	public ProcessingInstruction createProcessingInstruction(String target, String data) throws DOMException {
		//TODO check for an invalid character
		//create a new processing instruction with the specified target and data
		final XMLProcessingInstruction processingInstruction = new XMLProcessingInstruction(this, target, data);
		return processingInstruction; //return the new processing instruction we constructed
	}

	/**
	 * Creates an attribute with the given name. Note that the attribute instance can then be set on an element node setAttribute() method.<br />
	 * To create an attribute with a qualified name and namespace URI, use the createAttributeNS() method.
	 * @param name The name of the attribute.
	 * @return A new attribute node object.
	 * @throws DOMException <ul>
	 *           <li>INVALID_CHARACTER_ERR: Raised if the specified name contains an invalid character.</li>
	 *           </ul>
	 * @see XMLElement
	 * @see XMLElement#setAttribute
	 * @see XMLDocument#createAttributeNS
	 * @version DOM Level 1
	 */
	public Attr createAttribute(String name) throws DOMException {
		//TODO check the attribute name here and throw an exception if needed
		return new XMLAttribute(this, name); //create a new attribute with the given name and return it
	}

	/**
	 * Creates an EntityReference object.
	 * @param name The name of the entity to reference.
	 * @return The new <code>EntityReference</code> object.
	 * @throws DOMException INVALID_CHARACTER_ERR: Raised if the specified name contains an invalid character. <br>
	 *           NOT_SUPPORTED_ERR: Raised if this document is an HTML document.
	 */
	public EntityReference createEntityReference(String name) throws DOMException {
		return null;
	} //TODO fix

	/**
	 * Returns a NodeList of first-level descendant elements with a given tag name. If deep is set to <code>true</code>, returns a NodeList of all descendant
	 * elements with a given tag name, in the order in which they would be encountered in a preorder traversal of the element tree.
	 * @param name The name of the tag to match on. The special value "*" matches all tags.
	 * @param deep Whether or not matching child elements of each matching child element, etc. should be included.
	 * @return A new NodeList object containing all the matching element nodes.
	 * @see XMLElement
	 * @see XMLDocument
	 * @see XMLDocument#getElementsByTagName
	 * @see XMLNodeList
	 */
	/*TODO decide if we need or not
		public NodeList getElementsByTagName(String name, boolean deep)
		{
			final XMLNodeList nodeList=new XMLNodeList();	//create a new node list to return
			if((getDocumentElement().getNodeName().equals("*") || getDocumentElement().getNodeName().equals(name)))	//if the root element has the correct name (or they passed us the wildcard character) TODO use a constant here
				nodeList.add(getDocumentElement());	//add this node to the list
			if(deep)	//if each of the children should check for matching tags as well
				nodeList.addAll((XMLNodeList)(((XMLElement)getDocumentElement())).getElementsByTagName(name, deep));	//get the root element's elements by name and add them to our list
			return nodeList;	//return the list we created and filled
		}
	*/

	/**
	 * Returns a NodeList of all the elements with a given tag name in the order in which they would be encountered in a preorder traversal of the document tree.
	 * @param tagname The name of the tag to match on. The special value "*" matches all tags.
	 * @return A new NodeList object containing all the matched elements.
	 * @see XMLElement
	 * @see XMLELement#getElementsByTagName
	 * @see XMLDocument
	 * @see XMLDocument#getDocumentElement
	 * @see XMLNodeList
	 * @version DOM Level 1
	 */
	//TODO why is this needed? why not use the XMLNOde version?
	public NodeList getElementsByTagName(String tagname) {
		final XMLNodeList nodeList = new XMLNodeList(); //create a new node list to return
		if((tagname.equals("*") || getDocumentElement().getNodeName().equals(tagname))) //if the root element has the correct name (or they passed us the wildcard character) TODO use a constant here
			nodeList.add(getDocumentElement()); //add this node to the list
		nodeList.addAll((XMLNodeList)(((XMLElement)getDocumentElement()).getNodesByName(ELEMENT_NODE, tagname, true))); //get the root element's elements by name and add them to our list
		return nodeList; //return the list we created and filled
	}

	/**
	 * Creates a copy of a node from another document and imports it to this document. The returned node has no parent. The source node is not altered or removed
	 * from the original document
	 * <p>
	 * Additional information is copied as appropriate to the <code>nodeType</code>, attempting to mirror the behavior expected if a fragment of XML or HTML
	 * source was copied from one document to another, recognizing that the two documents may have different DTDs in the XML case. The following list describes
	 * the specifics for every type of node.
	 * </p>
	 * <ul>
	 * <li>ELEMENT_NODE: Specified attribute nodes of the source element are imported, and the generated attribute nodes are attached to the generated element.
	 * Default attributes are not copied, though if the document being imported into defines default attributes for this element name, those are assigned. If the
	 * <code>deep</code> parameter was set to <code>true</code>, the descendants of the source element will be recursively imported and the resulting nodes
	 * reassembled to form the corresponding subtree.</li>
	 * <li>ATTRIBUTE_NODE: The <code>specified</code> flag is set to <code>true</code> on the generated <code>Attr</code>. The descendants of the the source
	 * attribute are recursively imported and the resulting nodes reassembled to form the corresponding subtree. Note that the <code>deep</code> parameter does
	 * not apply to attribute nodes; they always carry their children with them when imported.</li>
	 * <li>TEXT_NODE, CDATA_SECTION_NODE, COMMENT_NODE: These three types of nodes inheriting from <code>CharacterData</code> copy their data and length
	 * attributes from those of the source node.</li>
	 * <li>ENTITY_REFERENCE_NODE: Only the entity reference itself is copied, even if a <code>deep</code> import is requested, since the source and destination
	 * documents might have defined the entity differently. If the document being imported into provides a definition for this entity name, its value is assigned.
	 * </li>
	 * <li>ENTITY_NODE: Entity nodes can be imported, however in the current release of the DOM the document type is readonly. Ability to add these imported nodes
	 * to a document type will be considered for addition to a future release of the DOM. On import, the publicID, systemID, and notationName attributes are
	 * copied. If a deep import is requested, the descendants of the the source entity node is recursively imported and the resulting nodes reassembled to form
	 * the corresponding subtree.</li>
	 * <li>PROCESSING_INSTRUCTION_NODE: The imported node copies its target and data values from those of the source node.</li>
	 * <li>DOCUMENT_NODE: Document nodes nodes cannot be imported.</li>
	 * <li>DOCUMENT_TYPE_NODE: Document type nodes cannot be imported.</li>
	 * <li>DOCUMENT_FRAGMENT_NODE: If the <code>deep</code> option was set to <code>true</code>, the descendants of the source element will be recursively
	 * imported and the resulting nodes reassembled to form the corresponding subtree. Otherwise, this simply generates an empty DocumentFragment node.</li> TODO
	 * is that last sentence really true?
	 * <li>NOTATION_NODE: Notation nodes can be imported, however in the current release of the DOM the document type is readonly. Ability to add these imported
	 * nodes to a document type will be considered for addition to a future release of the DOM. On import, the publicID and systemID attributes are copied. Note
	 * that the <code>deep</code> parameter does not apply to notation nodes since they never have any children.</li>
	 * </ul>
	 * @param importedNode The node to import.
	 * @param deep If <code>true</code>, recursively imports the subtree under the specified node; if <code>false</code>, imports only the node itself, as
	 *          explained above. This does not apply to Attribute, EntityReference, and Notation nodes.
	 * @return The imported node that belongs to this document.
	 * @throws DOMException <ul>
	 *           <li>NOT_SUPPORTED_ERR: Raised if the type of node being imported is not supported.</li>
	 *           </ul>
	 * @see XMLNode#cloneNode
	 * @see XMLNode#getParentNode
	 * @version DOM Level 2
	 * @since DOM Level 2
	 */
	public Node importNode(Node importedNode, boolean deep) throws DOMException {
		//TODO check the type of node being imported
		final XMLNode xmlNodeClone = (XMLNode)importedNode.cloneNode(deep); //clone the imported node
		xmlNodeClone.setOwnerXMLDocument(this); //show that we own the cloned element
		return xmlNodeClone; //return the imported node TODO this all isn't quite correct; make sure it's complete
	}

	/**
	 * Inserts the node <code>newChild</code> before the existing child node <code>refChild</code>. If <code>refChild</code> is <code>null</code>, inserts
	 * <code>newChild</code> at the end of the list of children.
	 * <p>
	 * This version throws a <code>HIERARCHY_REQUEST_ERR</code> if an attempt is made to add an element as a child.
	 * </p>
	 * @param newChild The node to insert. If <code>newChild</code> is a <code>DocumentFragment</code> object, all of its children are inserted, in the same
	 *          order, before <code>refChild</code>. If the <code>newChild</code> is already in the tree, it is first removed.
	 * @param refChild The reference node, i.e., the node before which the new node must be inserted.
	 * @return The node being inserted.
	 * @throws DOMException <ul>
	 *           <li>HIERARCHY_REQUEST_ERR: Raised if this node is of a type that does not allow children of the type of the <code>newChild</code> node, or if the
	 *           node to insert is one of this node's ancestors.</li>
	 *           <li>WRONG_DOCUMENT_ERR: Raised if <code>newChild</code> was created from a different document than the one that created this node.</li>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly.</li>
	 *           <li>NOT_FOUND_ERR: Raised if <code>refChild</code> is not a child of this node.</li>
	 *           </ul>
	 * @version DOM Level 1
	 */
	public Node insertBefore(Node newChild, Node refChild) throws DOMException {
		if(newChild.getNodeType() == Node.ELEMENT_NODE) //if they are trying to add an element
			throw new XMLDOMException(DOMException.HIERARCHY_REQUEST_ERR, new Object[] { newChild.getNodeName() }); //show that there can only be one document element
		return super.insertBefore(newChild, refChild); //do the default functionality		
	}

	/**
	 * Replaces the child node <code>oldChild</code> with <code>newChild</code> in the list of children, and returns the <code>oldChild</code> node. If the
	 * <code>newChild</code> is already in the tree, it is first removed.
	 * <p>
	 * This version throws a <code>HIERARCHY_REQUEST_ERR</code> if an attempt is made to replace the document element with a non-element, or to replace a
	 * non-element child with an element.
	 * </p>
	 * @param newChild The new node to put in the child list.
	 * @param oldChild The node being replaced in the list.
	 * @return The node replaced.
	 * @throws DOMException <ul>
	 *           <li>HIERARCHY_REQUEST_ERR: Raised if this node is of a type that does not allow children of the type of the <code>newChild</code> node, or it the
	 *           node to put in is one of this node's ancestors.</li>
	 *           <li>WRONG_DOCUMENT_ERR: Raised if <code>newChild</code> was created from a different document than the one that created this node.</li>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly.</li>
	 *           <li>NOT_FOUND_ERR: Raised if <code>oldChild</code> is not a child of this node.</li>
	 *           </ul>
	 * @see XMLNode#removeChild
	 * @version DOM Level 1
	 */
	public Node replaceChild(Node newChild, Node oldChild) throws DOMException {
		//if an element is replacing a node and/or if an element is being replaced
		if((newChild.getNodeType() == Node.ELEMENT_NODE || oldChild.getNodeType() == Node.ELEMENT_NODE)) {
			if(newChild.getNodeType() != oldChild.getNodeType()) //if one is an element, both have to be an element
				throw new XMLDOMException(DOMException.HIERARCHY_REQUEST_ERR, new Object[] { newChild.getNodeName() }); //show that there must be at least one and only one document element
			final int index = getChildXMLNodeList().indexOf(oldChild); //get the index of the old child
			if(index < 0) //if the old child isn't in the list (do this first, even though this will be checked again in our call to removeChild(), so that errors will occur before modifications occur
				throw new XMLDOMException(DOMException.NOT_FOUND_ERR, new Object[] { oldChild.getNodeName() }); //show that we couldn't find the node to remove
			final XMLNode xmlNewChild = (XMLNode)newChild; //cast the child to an XMLNode
			//TODO make sure this replacement fires the correct events
			getChildXMLNodeList().set(index, xmlNewChild); //replace the document element
			//TODO set the document, set the parent, etc.
			xmlNewChild.setParentXMLNode(this); //set the parent of the added child
			return oldChild; //return the old replaced node
		} else { //if elements aren't involved
			return super.replaceChild(newChild, oldChild); //do the default functionality
		}
	}

	/**
	 * Removes the child node indicated by <code>oldChild</code> from the list of children, and returns it.
	 * <p>
	 * This version throws a <code>HIERARCHY_REQUEST_ERR</code> if an attempt is made to remove the document element.
	 * </p>
	 * @param oldChild The node being removed.
	 * @return The node removed.
	 * @throws DOMException <ul>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly.</li>
	 *           <li>NOT_FOUND_ERR: Raised if <code>oldChild</code> is not a child of this node.</li>
	 *           </ul>
	 * @version DOM Level 1
	 */
	public Node removeChild(Node oldChild) throws DOMException {
		if(oldChild.getNodeType() == Node.ELEMENT_NODE) //if they are trying to remove the document element
			throw new XMLDOMException(DOMException.HIERARCHY_REQUEST_ERR, new Object[] { oldChild.getNodeName() }); //show that there must be one document element
		return super.removeChild(oldChild); //do the default functionality
	}

	/**
	 * Adds the specified node to the end of the list of children. If the node already exists, it is first removed.
	 * <p>
	 * This version throws a <code>HIERARCHY_REQUEST_ERR</code> if an attempt is made to append an element as a child.
	 * </p>
	 * @param newChild The node to add. If it is a <code>DocumentFragment</code> object, the entire contents of the document fragment are moved into the child
	 *          list of this node
	 * @return The node added.
	 * @throws DOMException <ul>
	 *           <li>HIERARCHY_REQUEST_ERR: Raised if this node is of a type that does not allow children of the type of the <code>newChild</code> node, or if the
	 *           node to append is one of this node's ancestors.</li>
	 *           <li>WRONG_DOCUMENT_ERR: Raised if <code>newChild</code> was created from a different document than the one that created this node.</li>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly.</li>
	 *           </ul>
	 * @see XMLNode#isNodeTypeAllowed
	 * @see XMLNode#checkNodeBeforeAdding
	 * @version DOM Level 1
	 */
	public Node appendChild(Node newChild) throws DOMException {
		if(newChild.getNodeType() == Node.ELEMENT_NODE) //if they are trying to append an element
			throw new XMLDOMException(DOMException.HIERARCHY_REQUEST_ERR, new Object[] { newChild.getNodeName() }); //show that there can only be one document element
		return super.appendChild(newChild); //do the default functionality
	}

	/**
	 * Creates an element of the given qualified name and namespace URI.
	 * @param namespaceURI The namespace URI of the element to create.
	 * @param qualifiedName The qualified name of the element type to instantiate.
	 * @return A new <code>Element</code> object with the following attributes:
	 *         <table>
	 *         <tr>
	 *         <th>Attribute</th>
	 *         <th>Value</th>
	 *         </tr>
	 *         <tr>
	 *         <td><code>Node.nodeName</code></td>
	 *         <td><code>qualifiedName</code></td>
	 *         </tr>
	 *         <tr>
	 *         <td><code>Node.namespaceURI</code></td>
	 *         <td><code>namespaceURI</code></td>
	 *         </tr>
	 *         <tr>
	 *         <td><code>Node.prefix</code></td>
	 *         <td>prefix, extracted from <code>qualifiedName</code>, or <code>null</code> if there is no prefix</td>
	 *         </tr>
	 *         <tr>
	 *         <td><code>Node.localName</code></td>
	 *         <td>local name, extracted from <code>qualifiedName</code></td>
	 *         </tr>
	 *         <tr>
	 *         <td><code>Element.tagName</code></td>
	 *         <td><code>qualifiedName</code></td>
	 *         </tr>
	 *         </table>
	 * @throws DOMException <ul>
	 *           <li>INVALID_CHARACTER_ERR: Raised if the specified qualified name contains an illegal character.</li>
	 *           <li>NAMESPACE_ERR: Raised if the <code>qualifiedName</code> is malformed, if the <code>qualifiedName</code> has a prefix and the
	 *           <code>namespaceURI</code> is <code>null</code>, or if the <code>qualifiedName</code> has a prefix that is "xml" and the <code>namespaceURI</code>
	 *           is different from "http://www.w3.org/XML/1998/namespace".</li>
	 *           </ul>
	 * @since DOM Level 2
	 */
	public Element createElementNS(String namespaceURI, String qualifiedName) throws DOMException {
		//TODO check for an invalid character here, and decide if this should be done here or in the element constructor
		final XMLElement element = new XMLElement(this, namespaceURI, qualifiedName); //create a new XML element with the specified names
		//TODO create default attributes if needed
		return element; //return the new element we constructed
	}

	/**
	 * Creates an attribute of the given qualified name and namespace URI.
	 * @param namespaceURI The namespace URI of the attribute to create.
	 * @param qualifiedName The qualified name of the attribute to instantiate.
	 * @return A new <code>Attr</code> object with the following attributes:
	 *         <table>
	 *         <tr>
	 *         <th>Attribute</th>
	 *         <th>Value</th>
	 *         </tr>
	 *         <tr>
	 *         <td><code>Node.nodeName</code></td>
	 *         <td>qualifiedName</td>
	 *         </tr>
	 *         <tr>
	 *         <td><code>Node.namespaceURI</code></td>
	 *         <td><code>namespaceURI</code></td>
	 *         </tr>
	 *         <tr>
	 *         <td><code>Node.prefix</code></td>
	 *         <td>prefix, extracted from <code>qualifiedName</code>, or <code>null</code> if there is no prefix</td>
	 *         </tr>
	 *         <tr>
	 *         <td><code>Node.localName</code></td>
	 *         <td>local name, extracted from <code>qualifiedName</code></td>
	 *         </tr>
	 *         <tr>
	 *         <td><code>Attr.name</code></td>
	 *         <td><code>qualifiedName</code></td>
	 *         </tr>
	 *         <tr>
	 *         <td><code>Node.nodeValue</code></td>
	 *         <td>the empty string</td>
	 *         </tr>
	 *         </table>
	 * @throws DOMException <ul>
	 *           <li>INVALID_CHARACTER_ERR: Raised if the specified qualified name contains an illegal character.</li>
	 *           <li>NAMESPACE_ERR: Raised if the <code>qualifiedName</code> is malformed, if the <code>qualifiedName</code> has a prefix and the
	 *           <code>namespaceURI</code> is <code>null</code>, if the <code>qualifiedName</code> has a prefix that is "xml" and the <code>namespaceURI</code> is
	 *           different from "http://www.w3.org/XML/1998/namespace", or if the <code>qualifiedName</code> is "xmlns" and the <code>namespaceURI</code> is
	 *           different from "http://www.w3.org/2000/xmlns/".</li>
	 *           </ul>
	 * @since DOM Level 2
	 */
	public Attr createAttributeNS(String namespaceURI, String qualifiedName) throws DOMException {
		//TODO check for an invalid character here, and decide if this should be done here or in the attribute constructor
		//TODO check the attribute name here and throw an exception if needed
		return new XMLAttribute(this, namespaceURI, qualifiedName, ""); //create a new attribute with the specified names and no value and return it
	}

	/**
	 * Returns a <code>NodeList</code> of all the <code>Elements</code> with a given local name and namespace URI in the order in which they are encountered in a
	 * preorder traversal of the <code>Document</code> tree.
	 * @param namespaceURIThe namespace URI of the elements to match on. The special value "*" matches all namespaces.
	 * @param localNameThe local name of the elements to match on. The special value "*" matches all local names.
	 * @return A new <code>NodeList</code> object containing all the matched <code>Elements</code>.
	 * @since DOM Level 2
	 */
	public NodeList getElementsByTagNameNS(String namespaceURI, String localName) {
		return null;
	} //TODO fix namespace

	/**
	 * Returns the <code>Element</code> whose <code>ID</code> is given by <code>elementId</code> . If no such element exists, returns <code>null</code> . Behavior
	 * is not defined if more than one element has this <code>ID</code> . The DOM implementation must have information that says which attributes are of type ID.
	 * Attributes with the name "ID" are not of type ID unless so defined. Implementations that do not know whether attributes are of type ID or not are expected
	 * to return <code>null</code> .
	 * @param elementId The unique <code>id</code> value for an element.
	 * @return The matching element.
	 * @since DOM Level 2
	 */
	public Element getElementById(String elementId) {
		return null;
	} //TODO fix

	/**
	 * The Document Type Declaration (see <code>DocumentType</code>) associated with this document. For XML documents without a document type declaration this
	 * returns <code>null</code>. For HTML documents, a <code>DocumentType</code> object may be returned, independently of the presence or absence of document
	 * type declaration in the HTML document. <br>
	 * This provides direct access to the <code>DocumentType</code> node, child node of this <code>Document</code>. This node can be set at document creation time
	 * and later changed through the use of child nodes manipulation methods, such as <code>Node.insertBefore</code>, or <code>Node.replaceChild</code>. Note,
	 * however, that while some implementations may instantiate different types of <code>Document</code> objects supporting additional features than the "Core",
	 * such as "HTML" [<a href='http://www.w3.org/TR/2003/REC-DOM-Level-2-HTML-20030109'>DOM Level 2 HTML</a>] , based on the <code>DocumentType</code> specified
	 * at creation time, changing it afterwards is very unlikely to result in a change of the features supported.
	 * @version DOM Level 3
	 */
	//TODO fix for DOM 3    public DocumentType getDoctype() {throw new UnsupportedOperationException();}	//TODO fix for DOM 3

	/**
	 * An attribute specifying the encoding used for this document at the time of the parsing. This is <code>null</code> when it is not known, such as when the
	 * <code>Document</code> was created in memory.
	 * @since DOM Level 3
	 */
	public String getInputEncoding() {
		throw new UnsupportedOperationException();
	} //TODO fix for DOM 3

	/**
	 * An attribute specifying, as part of the <a href='http://www.w3.org/TR/2004/REC-xml-20040204#NT-XMLDecl'>XML declaration</a>, the encoding of this document.
	 * This is <code>null</code> when unspecified or when it is not known, such as when the <code>Document</code> was created in memory.
	 * @since DOM Level 3
	 */
	public String getXmlEncoding() {
		throw new UnsupportedOperationException();
	} //TODO fix for DOM 3

	/**
	 * An attribute specifying, as part of the <a href='http://www.w3.org/TR/2004/REC-xml-20040204#NT-XMLDecl'>XML declaration</a>, whether this document is
	 * standalone. This is <code>false</code> when unspecified.
	 * <p >
	 * <b>Note:</b> No verification is done on the value when setting this attribute. Applications should use <code>Document.normalizeDocument()</code> with the
	 * "validate" parameter to verify if the value matches the <a href='http://www.w3.org/TR/2004/REC-xml-20040204#sec-rmd'>validity constraint for standalone
	 * document declaration</a> as defined in [<a href='http://www.w3.org/TR/2004/REC-xml-20040204'>XML 1.0</a>].
	 * @since DOM Level 3
	 */
	public boolean getXmlStandalone() {
		throw new UnsupportedOperationException();
	} //TODO fix for DOM 3

	/**
	 * An attribute specifying, as part of the <a href='http://www.w3.org/TR/2004/REC-xml-20040204#NT-XMLDecl'>XML declaration</a>, whether this document is
	 * standalone. This is <code>false</code> when unspecified.
	 * <p >
	 * <b>Note:</b> No verification is done on the value when setting this attribute. Applications should use <code>Document.normalizeDocument()</code> with the
	 * "validate" parameter to verify if the value matches the <a href='http://www.w3.org/TR/2004/REC-xml-20040204#sec-rmd'>validity constraint for standalone
	 * document declaration</a> as defined in [<a href='http://www.w3.org/TR/2004/REC-xml-20040204'>XML 1.0</a>].
	 * @throws DOMException NOT_SUPPORTED_ERR: Raised if this document does not support the "XML" feature.
	 * @since DOM Level 3
	 */
	public void setXmlStandalone(boolean xmlStandalone) throws DOMException {
		throw new UnsupportedOperationException();
	} //TODO fix for DOM 3

	/**
	 * An attribute specifying, as part of the <a href='http://www.w3.org/TR/2004/REC-xml-20040204#NT-XMLDecl'>XML declaration</a>, the version number of this
	 * document. If there is no declaration and if this document supports the "XML" feature, the value is <code>"1.0"</code>. If this document does not support
	 * the "XML" feature, the value is always <code>null</code>. Changing this attribute will affect methods that check for invalid characters in XML names.
	 * Application should invoke <code>Document.normalizeDocument()</code> in order to check for invalid characters in the <code>Node</code>s that are already
	 * part of this <code>Document</code>. <br>
	 * DOM applications may use the <code>DOMImplementation.hasFeature(feature, version)</code> method with parameter values "XMLVersion" and "1.0" (respectively)
	 * to determine if an implementation supports [<a href='http://www.w3.org/TR/2004/REC-xml-20040204'>XML 1.0</a>]. DOM applications may use the same method
	 * with parameter values "XMLVersion" and "1.1" (respectively) to determine if an implementation supports [<a
	 * href='http://www.w3.org/TR/2004/REC-xml11-20040204/'>XML 1.1</a>]. In both cases, in order to support XML, an implementation must also support the "XML"
	 * feature defined in this specification. <code>Document</code> objects supporting a version of the "XMLVersion" feature must not raise a
	 * <code>NOT_SUPPORTED_ERR</code> exception for the same version number when using <code>Document.xmlVersion</code>.
	 * @since DOM Level 3
	 */
	public String getXmlVersion() {
		throw new UnsupportedOperationException();
	} //TODO fix for DOM 3

	/**
	 * An attribute specifying, as part of the <a href='http://www.w3.org/TR/2004/REC-xml-20040204#NT-XMLDecl'>XML declaration</a>, the version number of this
	 * document. If there is no declaration and if this document supports the "XML" feature, the value is <code>"1.0"</code>. If this document does not support
	 * the "XML" feature, the value is always <code>null</code>. Changing this attribute will affect methods that check for invalid characters in XML names.
	 * Application should invoke <code>Document.normalizeDocument()</code> in order to check for invalid characters in the <code>Node</code>s that are already
	 * part of this <code>Document</code>. <br>
	 * DOM applications may use the <code>DOMImplementation.hasFeature(feature, version)</code> method with parameter values "XMLVersion" and "1.0" (respectively)
	 * to determine if an implementation supports [<a href='http://www.w3.org/TR/2004/REC-xml-20040204'>XML 1.0</a>]. DOM applications may use the same method
	 * with parameter values "XMLVersion" and "1.1" (respectively) to determine if an implementation supports [<a
	 * href='http://www.w3.org/TR/2004/REC-xml11-20040204/'>XML 1.1</a>]. In both cases, in order to support XML, an implementation must also support the "XML"
	 * feature defined in this specification. <code>Document</code> objects supporting a version of the "XMLVersion" feature must not raise a
	 * <code>NOT_SUPPORTED_ERR</code> exception for the same version number when using <code>Document.xmlVersion</code>.
	 * @throws DOMException NOT_SUPPORTED_ERR: Raised if the version is set to a value that is not supported by this <code>Document</code> or if this document
	 *           does not support the "XML" feature.
	 * @since DOM Level 3
	 */
	public void setXmlVersion(String xmlVersion) throws DOMException {
		throw new UnsupportedOperationException();
	} //TODO fix for DOM 3

	/**
	 * An attribute specifying whether error checking is enforced or not. When set to <code>false</code>, the implementation is free to not test every possible
	 * error case normally defined on DOM operations, and not raise any <code>DOMException</code> on DOM operations or report errors while using
	 * <code>Document.normalizeDocument()</code>. In case of error, the behavior is undefined. This attribute is <code>true</code> by default.
	 * @since DOM Level 3
	 */
	public boolean getStrictErrorChecking() {
		throw new UnsupportedOperationException();
	} //TODO fix for DOM 3

	/**
	 * An attribute specifying whether error checking is enforced or not. When set to <code>false</code>, the implementation is free to not test every possible
	 * error case normally defined on DOM operations, and not raise any <code>DOMException</code> on DOM operations or report errors while using
	 * <code>Document.normalizeDocument()</code>. In case of error, the behavior is undefined. This attribute is <code>true</code> by default.
	 * @since DOM Level 3
	 */
	public void setStrictErrorChecking(boolean strictErrorChecking) {
		throw new UnsupportedOperationException();
	} //TODO fix for DOM 3

	/**
	 * The location of the document or <code>null</code> if undefined or if the <code>Document</code> was created using
	 * <code>DOMImplementation.createDocument</code>. No lexical checking is performed when setting this attribute; this could result in a <code>null</code> value
	 * returned when using <code>Node.baseURI</code> . <br>
	 * Beware that when the <code>Document</code> supports the feature "HTML" [<a href='http://www.w3.org/TR/2003/REC-DOM-Level-2-HTML-20030109'>DOM Level 2
	 * HTML</a>] , the href attribute of the HTML BASE element takes precedence over this attribute when computing <code>Node.baseURI</code>.
	 * @since DOM Level 3
	 */
	public String getDocumentURI() {
		throw new UnsupportedOperationException();
	} //TODO fix for DOM 3

	/**
	 * The location of the document or <code>null</code> if undefined or if the <code>Document</code> was created using
	 * <code>DOMImplementation.createDocument</code>. No lexical checking is performed when setting this attribute; this could result in a <code>null</code> value
	 * returned when using <code>Node.baseURI</code> . <br>
	 * Beware that when the <code>Document</code> supports the feature "HTML" [<a href='http://www.w3.org/TR/2003/REC-DOM-Level-2-HTML-20030109'>DOM Level 2
	 * HTML</a>] , the href attribute of the HTML BASE element takes precedence over this attribute when computing <code>Node.baseURI</code>.
	 * @since DOM Level 3
	 */
	public void setDocumentURI(String documentURI) {
		throw new UnsupportedOperationException();
	} //TODO fix for DOM 3

	/**
	 * Attempts to adopt a node from another document to this document. If supported, it changes the <code>ownerDocument</code> of the source node, its children,
	 * as well as the attached attribute nodes if there are any. If the source node has a parent it is first removed from the child list of its parent. This
	 * effectively allows moving a subtree from one document to another (unlike <code>importNode()</code> which create a copy of the source node instead of moving
	 * it). When it fails, applications should use <code>Document.importNode()</code> instead. Note that if the adopted node is already part of this document
	 * (i.e. the source and target document are the same), this method still has the effect of removing the source node from the child list of its parent, if any.
	 * The following list describes the specifics for each type of node.
	 * <dl>
	 * <dt>ATTRIBUTE_NODE</dt>
	 * <dd>The <code>ownerElement</code> attribute is set to <code>null</code> and the <code>specified</code> flag is set to <code>true</code> on the adopted
	 * <code>Attr</code>. The descendants of the source <code>Attr</code> are recursively adopted.</dd>
	 * <dt>DOCUMENT_FRAGMENT_NODE</dt>
	 * <dd>The descendants of the source node are recursively adopted.</dd>
	 * <dt>DOCUMENT_NODE</dt>
	 * <dd>
	 * <code>Document</code> nodes cannot be adopted.</dd>
	 * <dt>DOCUMENT_TYPE_NODE</dt>
	 * <dd>
	 * <code>DocumentType</code> nodes cannot be adopted.</dd>
	 * <dt>ELEMENT_NODE</dt>
	 * <dd><em>Specified</em> attribute nodes of the source element are adopted. Default attributes are discarded, though if the document being adopted into
	 * defines default attributes for this element name, those are assigned. The descendants of the source element are recursively adopted.</dd>
	 * <dt>ENTITY_NODE</dt>
	 * <dd>
	 * <code>Entity</code> nodes cannot be adopted.</dd>
	 * <dt>ENTITY_REFERENCE_NODE</dt>
	 * <dd>Only the <code>EntityReference</code> node itself is adopted, the descendants are discarded, since the source and destination documents might have
	 * defined the entity differently. If the document being imported into provides a definition for this entity name, its value is assigned.</dd>
	 * <dt>NOTATION_NODE</dt>
	 * <dd><code>Notation</code> nodes cannot be adopted.</dd>
	 * <dt>PROCESSING_INSTRUCTION_NODE, TEXT_NODE, CDATA_SECTION_NODE, COMMENT_NODE</dt>
	 * <dd>These nodes can all be adopted. No specifics.</dd>
	 * </dl>
	 * <p >
	 * <b>Note:</b> Since it does not create new nodes unlike the <code>Document.importNode()</code> method, this method does not raise an
	 * <code>INVALID_CHARACTER_ERR</code> exception, and applications should use the <code>Document.normalizeDocument()</code> method to check if an imported name
	 * is not an XML name according to the XML version in use.
	 * @param source The node to move into this document.
	 * @return The adopted node, or <code>null</code> if this operation fails, such as when the source node comes from a different implementation.
	 * @throws DOMException NOT_SUPPORTED_ERR: Raised if the source node is of type <code>DOCUMENT</code>, <code>DOCUMENT_TYPE</code>. <br>
	 *           NO_MODIFICATION_ALLOWED_ERR: Raised when the source node is readonly.
	 * @since DOM Level 3
	 */
	public Node adoptNode(Node source) throws DOMException {
		throw new UnsupportedOperationException();
	} //TODO fix for DOM 3

	/**
	 * The configuration used when <code>Document.normalizeDocument()</code> is invoked.
	 * @since DOM Level 3
	 */
	public DOMConfiguration getDomConfig() {
		throw new UnsupportedOperationException();
	} //TODO fix for DOM 3

	/**
	 * This method acts as if the document was going through a save and load cycle, putting the document in a "normal" form. As a consequence, this method updates
	 * the replacement tree of <code>EntityReference</code> nodes and normalizes <code>Text</code> nodes, as defined in the method <code>Node.normalize()</code>. <br>
	 * Otherwise, the actual result depends on the features being set on the <code>Document.domConfig</code> object and governing what operations actually take
	 * place. Noticeably this method could also make the document namespace well-formed according to the algorithm described in , check the character
	 * normalization, remove the <code>CDATASection</code> nodes, etc. See <code>DOMConfiguration</code> for details.
	 * 
	 * <pre>
	 * // Keep in the document 
	 * the information defined // in the XML Information Set (Java example) 
	 * DOMConfiguration docConfig = myDocument.getDomConfig(); 
	 * docConfig.setParameter("infoset", Boolean.TRUE); 
	 * myDocument.normalizeDocument();
	 * </pre>
	 * 
	 * <br>
	 * Mutation events, when supported, are generated to reflect the changes occurring on the document. <br>
	 * If errors occur during the invocation of this method, such as an attempt to update a read-only node or a <code>Node.nodeName</code> contains an invalid
	 * character according to the XML version in use, errors or warnings (<code>DOMError.SEVERITY_ERROR</code> or <code>DOMError.SEVERITY_WARNING</code>) will be
	 * reported using the <code>DOMErrorHandler</code> object associated with the "error-handler " parameter. Note this method might also report fatal errors (
	 * <code>DOMError.SEVERITY_FATAL_ERROR</code>) if an implementation cannot recover from an error.
	 * @since DOM Level 3
	 */
	public void normalizeDocument() {
		throw new UnsupportedOperationException();
	} //TODO fix for DOM 3

	/**
	 * Rename an existing node of type <code>ELEMENT_NODE</code> or <code>ATTRIBUTE_NODE</code>. <br>
	 * When possible this simply changes the name of the given node, otherwise this creates a new node with the specified name and replaces the existing node with
	 * the new node as described below. <br>
	 * If simply changing the name of the given node is not possible, the following operations are performed: a new node is created, any registered event listener
	 * is registered on the new node, any user data attached to the old node is removed from that node, the old node is removed from its parent if it has one, the
	 * children are moved to the new node, if the renamed node is an <code>Element</code> its attributes are moved to the new node, the new node is inserted at
	 * the position the old node used to have in its parent's child nodes list if it has one, the user data that was attached to the old node is attached to the
	 * new node. <br>
	 * When the node being renamed is an <code>Element</code> only the specified attributes are moved, default attributes originated from the DTD are updated
	 * according to the new element name. In addition, the implementation may update default attributes from other schemas. Applications should use
	 * <code>Document.normalizeDocument()</code> to guarantee these attributes are up-to-date. <br>
	 * When the node being renamed is an <code>Attr</code> that is attached to an <code>Element</code>, the node is first removed from the <code>Element</code>
	 * attributes map. Then, once renamed, either by modifying the existing node or creating a new one as described above, it is put back. <br>
	 * In addition,
	 * <ul>
	 * <li>a user data event <code>NODE_RENAMED</code> is fired,</li>
	 * <li>
	 * when the implementation supports the feature "MutationNameEvents", each mutation operation involved in this method fires the appropriate event, and in the
	 * end the event { <code>http://www.w3.org/2001/xml-events</code>, <code>DOMElementNameChanged</code> or { <code>http://www.w3.org/2001/xml-events</code>,
	 * <code>DOMAttributeNameChanged</code> is fired.</li>
	 * </ul>
	 * @param n The node to rename.
	 * @param namespaceURI The new namespace URI.
	 * @param qualifiedName The new qualified name.
	 * @return The renamed node. This is either the specified node or the new node that was created to replace the specified node.
	 * @throws DOMException NOT_SUPPORTED_ERR: Raised when the type of the specified node is neither <code>ELEMENT_NODE</code> nor <code>ATTRIBUTE_NODE</code>, or
	 *           if the implementation does not support the renaming of the document element. <br>
	 *           INVALID_CHARACTER_ERR: Raised if the new qualified name is not an XML name according to the XML version in use specified in the
	 *           <code>Document.xmlVersion</code> attribute. <br>
	 *           WRONG_DOCUMENT_ERR: Raised when the specified node was created from a different document than this document. <br>
	 *           NAMESPACE_ERR: Raised if the <code>qualifiedName</code> is a malformed qualified name, if the <code>qualifiedName</code> has a prefix and the
	 *           <code>namespaceURI</code> is <code>null</code>, or if the <code>qualifiedName</code> has a prefix that is "xml" and the <code>namespaceURI</code>
	 *           is different from "<a href='http://www.w3.org/XML/1998/namespace'> http://www.w3.org/XML/1998/namespace</a>" [<a
	 *           href='http://www.w3.org/TR/1999/REC-xml-names-19990114/'>XML Namespaces</a>] . Also raised, when the node being renamed is an attribute, if the
	 *           <code>qualifiedName</code>, or its prefix, is "xmlns" and the <code>namespaceURI</code> is different from
	 *           "<a href='http://www.w3.org/2000/xmlns/'>http://www.w3.org/2000/xmlns/</a>".
	 * @since DOM Level 3
	 */
	public Node renameNode(Node n, String namespaceURI, String qualifiedName) throws DOMException {
		throw new UnsupportedOperationException();
	} //TODO fix for DOM 3

	/**
	 * Creates a new <code>NodeIterator</code> over the subtree rooted at the specified node.
	 * @param rootThe node which will be iterated together with its children. The iterator is initially positioned just before this node. The
	 *          <code>whatToShow</code> flags and the filter, if any, are not considered when setting this position. The root must not be <code>null</code>.
	 * @param whatToShow Which node types may appear in the logical view of the tree presented by the iterator. See the description of <code>NodeFilter</code> for
	 *          the set of possible <code>SHOW_</code> values.These flags can be combined using boolean OR.
	 * @param filter The <code>NodeFilter</code> to be used with this <code>TreeWalker</code>, or <code>null</code> to indicate no filter.
	 * @param entityReferenceExpansion Whether entity reference nodes are expanded.
	 * @return The newly created <code>NodeIterator</code>.
	 * @throws DOMException <ul>
	 *           <li>NOT_SUPPORTED_ERR: Raised if the specified <code>root</code> is <code>null</code>.</li>
	 */
	public NodeIterator createNodeIterator(Node root, int whatToShow, NodeFilter filter, boolean entityReferenceExpansion) throws DOMException {
		if(root == null) //if an invalid root was given
			throw new XMLDOMException(DOMException.NOT_SUPPORTED_ERR, new Object[] { "null" }); //show that a null root is not allowed TODO use a constant here
		return new XMLNodeIterator(root, whatToShow, filter, entityReferenceExpansion); //create and return a new node iterator
	}

	/**
	 * Create a new <code>TreeWalker</code> over the subtree rooted at the specified node.
	 * @param rootThe node which will serve as the <code>root</code> for the <code>TreeWalker</code>. The <code>whatToShow</code> flags and the
	 *          <code>NodeFilter</code> are not considered when setting this value; any node type will be accepted as the <code>root</code>. The
	 *          <code>currentNode</code> of the <code>TreeWalker</code> is initialized to this node, whether or not it is visible. The <code>root</code> functions
	 *          as a stopping point for traversal methods that look upward in the document structure, such as <code>parentNode</code> and nextNode. The
	 *          <code>root</code> must not be <code>null</code>.
	 * @param whatToShowThis flag specifies which node types may appear in the logical view of the tree presented by the tree-walker. See the description of
	 *          <code>NodeFilter</code> for the set of possible SHOW_ values.These flags can be combined using <code>OR</code>.
	 * @param filterThe <code>NodeFilter</code> to be used with this <code>TreeWalker</code>, or <code>null</code> to indicate no filter.
	 * @param entityReferenceExpansionIf this flag is false, the contents of <code>EntityReference</code> nodes are not presented in the logical view.
	 * @return The newly created <code>TreeWalker</code>.
	 * @throws DOMException NOT_SUPPORTED_ERR: Raised if the specified <code>root</code> is <code>null</code>.
	 */
	public TreeWalker createTreeWalker(Node root, int whatToShow, NodeFilter filter, boolean entityReferenceExpansion) throws DOMException {
		return null;
	} //TODO fix

	/**
	 * Creates an event supported by this implementation.
	 * @param eventType The type of <code>Event</code> interface to be created. If the <code>Event</code> interface specified is supported by the implementation
	 *          this method will return a new <code>Event</code> of the interface type requested. If the <code>Event</code> is to be dispatched via the
	 *          <code>dispatchEvent</code> method the appropriate event init method must be called after creation in order to initialize the <code>Event</code>'s
	 *          values.
	 * @return The newly created <code>Event</code>.
	 * @throws DOMException <ul>
	 *           <li>NOT_SUPPORTED_ERR: Raised if the implementation does not support the type of <code>Event</code> interface requested.</li>
	 *           </ul>
	 */
	public Event createEvent(String eventType) throws DOMException {
		if(eventType.equals(XMLMutationEvent.FEATURE_NAME)) //if they wish to create a mutation event
			return new XMLMutationEvent(); //create a new mutation event
		else
			//if this type of event was not recognized
			throw new XMLDOMException(DOMException.NOT_SUPPORTED_ERR, new Object[] { eventType }); //show that this event type isn't allowed
	}

}
