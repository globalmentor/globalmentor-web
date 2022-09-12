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

import org.w3c.dom.*;

/**
 * Methods for performing operations that are independent of any particular instance of the document object model.
 * @author Garret Wilson
 * @see org.w3c.dom.DOMImplementation
 * @deprecated
 */
@Deprecated
class XMLDOMImplementation implements DOMImplementation {

	/**
	 * Tests to see if the DOM implementation implements a specific feature.
	 * @param feature The name of the feature to test (case-insensitive). The name must be an XML name.
	 * @param version The version number of the feature to test. In Level 2, this is the string "2.0". If the version is not specified, supporting any version of
	 *          the feature causes the method to return <code>true</code>.
	 * @return <code>true</code> if the feature is implemented in the specified version, <code>false</code> otherwise.
	 */
	public boolean hasFeature(String feature, String version) {
		return true;
	} //TODO fix

	@Override
	public Object getFeature(String feature, String version) {
		throw new UnsupportedOperationException();
	} //TODO fix for DOM 3

	@Override
	public DocumentType createDocumentType(String qualifiedName, String publicId, String systemId) throws DOMException {
		final XMLDocumentType documentType = new XMLDocumentType(null, qualifiedName); //create a document type with the correctd name but no owner document
		documentType.setPublicID(publicId); //set the public ID
		documentType.setSystemID(systemId); //set the system ID
		return documentType; //return the document type we created
	}

	@Override
	public Document createDocument(String namespaceURI, String qualifiedName, DocumentType doctype) throws DOMException {
		//TODO fix for namespaces
		//TODO check to see if the doctype.getNodeName() is not null, and if so throw a WRONG_DOCUMENT_ERR
		//TODO check all the other conditions that might cause errors
		final XMLDocument document = new XMLDocument(); //create a new document
		document.setNodeName(qualifiedName); //set the name of the document
		//TODO make sure that doctype is really an XMLDocumentType, or throw a WRONG_DOCUMENT_ERR if not
		//TODO probably add the document type as a child, just like the document element
		document.setXMLDocumentType((XMLDocumentType)doctype); //set the document type
		//TODO check the namespace somewhere here; maybe use document.checkNamespace()
		//TODO do whatever we need to do with the namespace
		//create the document element
		final XMLElement documentElement = (XMLElement)document.createElementNS(namespaceURI, qualifiedName);
		document.getChildXMLNodeList().add(documentElement); //add this child the document
		//TODO set the document, set the parent, etc.
		documentElement.setParentXMLNode(document); //set the parent of the added child
		/*TODO check to see if we need to fire events when the document element is added
				if(isEventsEnabled()) {	//if events are enabled
					//create a new event for the inserted node
					final Event nodeInsertedEvent=XMLMutationEvent.createDOMNodeInsertedEvent(xmlNewChild, this);
					xmlNewChild.dispatchEvent(nodeInsertedEvent); //dispatch the event
					//TODO dispatch the document events
				}
		*/
		return document; //return the document we created
	}

}
