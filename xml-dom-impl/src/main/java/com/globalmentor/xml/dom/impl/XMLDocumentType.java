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

import org.w3c.dom.*;

/**
 * An interface to the entities defined for an XML document along with other items.
 * @author Garret Wilson
 * @see XMLNode
 * @see org.w3c.dom.DocumentType
 * @deprecated
 */
@Deprecated
public class XMLDocumentType extends XMLNode implements DocumentType {

	/**
	 * Constructor which requires an owner document to be specified.
	 * @param ownerDocument The document which owns this node.
	 */
	public XMLDocumentType(final XMLDocument ownerDocument) {
		super(XMLNode.DOCUMENT_TYPE_NODE, ownerDocument); //construct the parent class
	}

	/**
	 * Constructor that specifies a document type name.
	 * @param ownerDocument The document which owns this node.
	 * @param newName The new name for the XML document type.
	 */
	public XMLDocumentType(final XMLDocument ownerDocument, final String newName) { //TODO fix throws XMLInvalidNameException
		this(ownerDocument); //do the default constructing
		setNodeName(newName); //set the name
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
		final XMLDocumentType clone = new XMLDocumentType(getOwnerXMLDocument(), getNodeName()); //create a new document type with the same owner document and the same name
		clone.setEntityXMLNamedNodeMap(getEntityXMLNamedNodeMap().cloneDeep()); //clone the entities and put them in the clone
		//TODO clone the notations
		clone.setPublicID(getPublicID()); //give the clone the same public ID
		clone.setSystemID(getSystemID()); //give the clone the same system ID
		return clone; //return our cloned node
	}

	@Override
	public String getName() {
		return getNodeName();
	}

	/** The named node map of the parameter entities in this XML document. */
	private XMLNamedNodeMap ParameterEntities = new XMLNamedNodeMap();

	/**
	 * Returns an <code>XMLNamedNodeMap</code> containing the parameter entities, both external and internal, declared in the DTD. Duplicates are discarded. Every
	 * entry in the map is an <code>XMLEntity</code>.
	 * @see XMLEntity
	 * @see XMLDocumentType#getEntities
	 * @see XMLDocumentType#getParameterEntities
	 */
	XMLNamedNodeMap getParameterEntityXMLNamedNodeMap() {
		return ParameterEntities;
	}

	/**
	 * Sets the <code>XMLNamedNodeMap</code> containing the parameter entities.
	 * @param entities The new map of parameter entities.
	 */
	void setParameterEntityXMLNamedNodeMap(final XMLNamedNodeMap parameterEntities) {
		ParameterEntities = parameterEntities;
	}

	/** The named node map of the general entities in this XML document. */
	private XMLNamedNodeMap Entities = new XMLNamedNodeMap();

	/**
	 * @return An <code>XMLNamedNodeMap</code> containing the general entities, both external and internal, declared in the DTD. Duplicates are discarded. Every
	 *         entry in the map is an <code>XMLEntity</code>. For the DOM version, see getEntities(). The DOM Level 2 does not support editing entities, so the
	 *         entities cannot be altered in any way.
	 * @see XMLEntity
	 * @see XMLDocumentType#getEntities
	 */
	public XMLNamedNodeMap getEntityXMLNamedNodeMap() {
		return Entities;
	}

	/**
	 * Sets the <code>XMLNamedNodeMap</code> containing the general entities.
	 * @param entities The new map of general entities.
	 */
	private void setEntityXMLNamedNodeMap(final XMLNamedNodeMap entities) {
		Entities = entities;
	}

	/**
	 * Returns a <code>NamedNodeMap</code> containing the general entities, both external and internal, declared in the DTD. Duplicates are discarded. Every entry
	 * in the map emplements <code>Entity</code>. The DOM Level 2 does not support editing entities, so the entities cannot be altered in any way.
	 * @see Entity
	 */
	public NamedNodeMap getEntities() {
		return getEntityXMLNamedNodeMap();
	}

	/**
	 * A <code>NamedNodeMap</code> containing the notations declared in the DTD. Duplicates are discarded. Every node in this map also implements the
	 * <code>Notation</code> interface.
	 * <p>
	 * The DOM Level 2 does not support editing notations, therefore <code>notations</code> cannot be altered in any way.
	 * </p>
	 */
	public NamedNodeMap getNotations() {
		return null;
	} //TODO fix

	/** The public identifier of this document type, or <code>null</code> if there is none. */
	private String PublicID = null;

	/**
	 * @return The public identifier, or <code>null</code> if there is none.
	 * @see XMLDocumentType#getSystemID
	 */
	public String getPublicID() {
		return PublicID;
	}

	@Override
	public String getPublicId() {
		return getPublicID();
	} //TODO fix, comment

	/**
	 * Sets the public identifier.
	 * @param publicID The new public ID.
	 * @see XMLDocumentType#setSystemID
	 */
	public void setPublicID(final String publicID) {
		PublicID = publicID;
	} //TODO this shouldn't be public; for now it's used by XHTMLTidier

	/** The system identifier of this document type, or <code>null</code> if there is none. */
	private String SystemID = null;

	/**
	 * @return The public identifier, or <code>null</code> if there is none.
	 * @see XMLDocumentType#getPublicID
	 */
	public String getSystemID() {
		return SystemID;
	}

	@Override
	public String getSystemId() {
		return getSystemID();
	} //TODO fix, comment

	/**
	 * Sets the system identifier.
	 * @param systemID The new system ID.
	 * @see XMLDocumentType#setPublicID
	 */
	public void setSystemID(final String systemID) {
		SystemID = systemID;
	} //TODO this shouldn't be public; for now it's used by XHTMLTidier

	/**
	 * Sets the public identifier and system identifier. Only for use within this package.
	 * @param externalID The external ID object, which can contain both a public and system identifier.
	 * @see XMLDocumentType#setPublicID
	 * @see XMLDocumentType#setSystemID
	 */
	void setExternalID(final XMLExternalID externalID) {
		setPublicID(externalID.getPublicID()); //sets the public identifier
		setSystemID(externalID.getSystemID()); //sets the system identifier
	}

	@Override
	public String getInternalSubset() {
		return "";
	} //TODO fix

}
