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

import java.util.*;

import org.w3c.dom.*;
import org.w3c.dom.events.*;
import org.w3c.dom.traversal.*;

import com.globalmentor.xml.dom.impl.events.*;
import com.globalmentor.xml.dom.impl.traversal.*;

/**
 * The entire XML document. Each node has an owner document.
 * @author Garret Wilson
 * @see XMLNode
 * @deprecated
 */
@Deprecated
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
	 * @see com.globalmentor.xml.dom.impl.stylesheets.XMLStyleSheet
	 * @see com.globalmentor.xml.dom.impl.stylesheets.css.XMLCSSStyleSheet
	 */
	private List StyleSheetList = new ArrayList(); //TODO remove this proprietary stuff when we can

	/**
	 * @return The list of stylesheets for this document in order encountered.
	 * @see org.w3c.dom.stylesheets.StyleSheet
	 * @see com.globalmentor.xml.dom.impl.stylesheets.XMLStyleSheet
	 * @see com.globalmentor.xml.dom.impl.stylesheets.css.XMLCSSStyleSheet
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

	@Override
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

	@Override
	public DOMImplementation getImplementation() {
		return new XMLDOMImplementation(); //return the XML DOM implementation these classes use TODO do we really want to create a new one each time? probably create one and use it until its weak reference is garbage collected
	}

	@Override
	public Element createElement(String tagName) throws DOMException {
		final XMLElement element = new XMLElement(this, tagName); //create a new XML element with the specified name
		//TODO create default attributes if needed
		return element; //return the new element we constructed
	}

	@Override
	public DocumentFragment createDocumentFragment() {
		return new XMLDocumentFragment(); //create a new document fragment and return it
	}

	@Override
	public Text createTextNode(String data) {
		return new XMLText(this, data); //create a text node with the given data and return it
	}

	@Override
	public Comment createComment(String data) {
		return new XMLComment(this, data); //create a comment node with the given data and return it
	}

	@Override
	public CDATASection createCDATASection(String data) throws DOMException {
		//TODO check to see if this is an HTML document, and if so throw an exception
		return new XMLCDATASection(this, data); //create a CDATA section node with the given data and return it
	}

	@Override
	public ProcessingInstruction createProcessingInstruction(String target, String data) throws DOMException {
		//TODO check for an invalid character
		//create a new processing instruction with the specified target and data
		final XMLProcessingInstruction processingInstruction = new XMLProcessingInstruction(this, target, data);
		return processingInstruction; //return the new processing instruction we constructed
	}

	@Override
	public Attr createAttribute(String name) throws DOMException {
		//TODO check the attribute name here and throw an exception if needed
		return new XMLAttribute(this, name); //create a new attribute with the given name and return it
	}

	@Override
	public EntityReference createEntityReference(String name) throws DOMException {
		return null;
	} //TODO fix

	//	/**
	//	 * Returns a NodeList of first-level descendant elements with a given tag name. If deep is set to <code>true</code>, returns a NodeList of all descendant
	//	 * elements with a given tag name, in the order in which they would be encountered in a preorder traversal of the element tree.
	//	 * @param name The name of the tag to match on. The special value "*" matches all tags.
	//	 * @param deep Whether or not matching child elements of each matching child element, etc. should be included.
	//	 * @return A new NodeList object containing all the matching element nodes.
	//	 * @see XMLElement
	//	 * @see XMLDocument
	//	 * @see XMLDocument#getElementsByTagName
	//	 * @see XMLNodeList
	//	 */
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

	@Override
	//TODO why is this needed? why not use the XMLNOde version?
	public NodeList getElementsByTagName(String tagname) {
		final XMLNodeList nodeList = new XMLNodeList(); //create a new node list to return
		if((tagname.equals("*") || getDocumentElement().getNodeName().equals(tagname))) //if the root element has the correct name (or they passed us the wildcard character) TODO use a constant here
			nodeList.add(getDocumentElement()); //add this node to the list
		nodeList.addAll((XMLNodeList)(((XMLElement)getDocumentElement()).getNodesByName(ELEMENT_NODE, tagname, true))); //get the root element's elements by name and add them to our list
		return nodeList; //return the list we created and filled
	}

	@Override
	public Node importNode(Node importedNode, boolean deep) throws DOMException {
		//TODO check the type of node being imported
		final XMLNode xmlNodeClone = (XMLNode)importedNode.cloneNode(deep); //clone the imported node
		xmlNodeClone.setOwnerXMLDocument(this); //show that we own the cloned element
		return xmlNodeClone; //return the imported node TODO this all isn't quite correct; make sure it's complete
	}

	@Override
	public Node insertBefore(Node newChild, Node refChild) throws DOMException {
		if(newChild.getNodeType() == Node.ELEMENT_NODE) //if they are trying to add an element
			throw new XMLDOMException(DOMException.HIERARCHY_REQUEST_ERR, new Object[] {newChild.getNodeName()}); //show that there can only be one document element
		return super.insertBefore(newChild, refChild); //do the default functionality		
	}

	@Override
	public Node replaceChild(Node newChild, Node oldChild) throws DOMException {
		//if an element is replacing a node and/or if an element is being replaced
		if((newChild.getNodeType() == Node.ELEMENT_NODE || oldChild.getNodeType() == Node.ELEMENT_NODE)) {
			if(newChild.getNodeType() != oldChild.getNodeType()) //if one is an element, both have to be an element
				throw new XMLDOMException(DOMException.HIERARCHY_REQUEST_ERR, new Object[] {newChild.getNodeName()}); //show that there must be at least one and only one document element
			final int index = getChildXMLNodeList().indexOf(oldChild); //get the index of the old child
			if(index < 0) //if the old child isn't in the list (do this first, even though this will be checked again in our call to removeChild(), so that errors will occur before modifications occur
				throw new XMLDOMException(DOMException.NOT_FOUND_ERR, new Object[] {oldChild.getNodeName()}); //show that we couldn't find the node to remove
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

	@Override
	public Node removeChild(Node oldChild) throws DOMException {
		if(oldChild.getNodeType() == Node.ELEMENT_NODE) //if they are trying to remove the document element
			throw new XMLDOMException(DOMException.HIERARCHY_REQUEST_ERR, new Object[] {oldChild.getNodeName()}); //show that there must be one document element
		return super.removeChild(oldChild); //do the default functionality
	}

	@Override
	public Node appendChild(Node newChild) throws DOMException {
		if(newChild.getNodeType() == Node.ELEMENT_NODE) //if they are trying to append an element
			throw new XMLDOMException(DOMException.HIERARCHY_REQUEST_ERR, new Object[] {newChild.getNodeName()}); //show that there can only be one document element
		return super.appendChild(newChild); //do the default functionality
	}

	@Override
	public Element createElementNS(String namespaceURI, String qualifiedName) throws DOMException {
		//TODO check for an invalid character here, and decide if this should be done here or in the element constructor
		final XMLElement element = new XMLElement(this, namespaceURI, qualifiedName); //create a new XML element with the specified names
		//TODO create default attributes if needed
		return element; //return the new element we constructed
	}

	@Override
	public Attr createAttributeNS(String namespaceURI, String qualifiedName) throws DOMException {
		//TODO check for an invalid character here, and decide if this should be done here or in the attribute constructor
		//TODO check the attribute name here and throw an exception if needed
		return new XMLAttribute(this, namespaceURI, qualifiedName, ""); //create a new attribute with the specified names and no value and return it
	}

	@Override
	public NodeList getElementsByTagNameNS(String namespaceURI, String localName) {
		return null;
	} //TODO fix namespace

	@Override
	public Element getElementById(String elementId) {
		return null;
	} //TODO fix

	//	/**
	//	 * The Document Type Declaration (see <code>DocumentType</code>) associated with this document. For XML documents without a document type declaration this
	//	 * returns <code>null</code>. For HTML documents, a <code>DocumentType</code> object may be returned, independently of the presence or absence of document
	//	 * type declaration in the HTML document.
	//	 * <p>
	//	 * This provides direct access to the <code>DocumentType</code> node, child node of this <code>Document</code>. This node can be set at document creation time
	//	 * and later changed through the use of child nodes manipulation methods, such as <code>Node.insertBefore</code>, or <code>Node.replaceChild</code>. Note,
	//	 * however, that while some implementations may instantiate different types of <code>Document</code> objects supporting additional features than the "Core",
	//	 * such as "HTML" [<a href='http://www.w3.org/TR/2003/REC-DOM-Level-2-HTML-20030109'>DOM Level 2 HTML</a>] , based on the <code>DocumentType</code> specified
	//	 * at creation time, changing it afterwards is very unlikely to result in a change of the features supported.
	//	 * <p>
	//	 */
	//TODO @Override
	//TODO fix for DOM 3    public DocumentType getDoctype() {throw new UnsupportedOperationException();}	//TODO fix for DOM 3

	@Override
	public String getInputEncoding() {
		throw new UnsupportedOperationException();
	} //TODO fix for DOM 3

	@Override
	public String getXmlEncoding() {
		throw new UnsupportedOperationException();
	} //TODO fix for DOM 3

	@Override
	public boolean getXmlStandalone() {
		throw new UnsupportedOperationException();
	} //TODO fix for DOM 3

	@Override
	public void setXmlStandalone(boolean xmlStandalone) throws DOMException {
		throw new UnsupportedOperationException();
	} //TODO fix for DOM 3

	@Override
	public String getXmlVersion() {
		throw new UnsupportedOperationException();
	} //TODO fix for DOM 3

	@Override
	public void setXmlVersion(String xmlVersion) throws DOMException {
		throw new UnsupportedOperationException();
	} //TODO fix for DOM 3

	@Override
	public boolean getStrictErrorChecking() {
		throw new UnsupportedOperationException();
	} //TODO fix for DOM 3

	@Override
	public void setStrictErrorChecking(boolean strictErrorChecking) {
		throw new UnsupportedOperationException();
	} //TODO fix for DOM 3

	@Override
	public String getDocumentURI() {
		throw new UnsupportedOperationException();
	} //TODO fix for DOM 3

	@Override
	public void setDocumentURI(String documentURI) {
		throw new UnsupportedOperationException();
	} //TODO fix for DOM 3

	@Override
	public Node adoptNode(Node source) throws DOMException {
		throw new UnsupportedOperationException();
	} //TODO fix for DOM 3

	@Override
	public DOMConfiguration getDomConfig() {
		throw new UnsupportedOperationException();
	} //TODO fix for DOM 3

	@Override
	public void normalizeDocument() {
		throw new UnsupportedOperationException();
	} //TODO fix for DOM 3

	@Override
	public Node renameNode(Node n, String namespaceURI, String qualifiedName) throws DOMException {
		throw new UnsupportedOperationException();
	} //TODO fix for DOM 3

	@Override
	public NodeIterator createNodeIterator(Node root, int whatToShow, NodeFilter filter, boolean entityReferenceExpansion) throws DOMException {
		if(root == null) //if an invalid root was given
			throw new XMLDOMException(DOMException.NOT_SUPPORTED_ERR, new Object[] {"null"}); //show that a null root is not allowed TODO use a constant here
		return new XMLNodeIterator(root, whatToShow, filter, entityReferenceExpansion); //create and return a new node iterator
	}

	@Override
	public TreeWalker createTreeWalker(Node root, int whatToShow, NodeFilter filter, boolean entityReferenceExpansion) throws DOMException {
		return null;
	} //TODO fix

	@Override
	public Event createEvent(String eventType) throws DOMException {
		if(eventType.equals(XMLMutationEvent.FEATURE_NAME)) //if they wish to create a mutation event
			return new XMLMutationEvent(); //create a new mutation event
		else
			//if this type of event was not recognized
			throw new XMLDOMException(DOMException.NOT_SUPPORTED_ERR, new Object[] {eventType}); //show that this event type isn't allowed
	}

}
