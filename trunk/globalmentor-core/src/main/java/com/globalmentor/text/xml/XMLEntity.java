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

import org.w3c.dom.Node;

/**An entity value, either parsed or unparsed, in an XML document.
This does <em>not</em> model the entity declaration itself, only the entity value.
@author Garret Wilson
@see XMLNode
@see XMLEntityReference
@see org.w3c.dom.Entity
@deprecated
*/
public class XMLEntity extends XMLNode implements org.w3c.dom.Entity
{
	/*Constructor which requires an owner document to be specified.
	@param ownerDocument The document which owns this node.
	*/
	public XMLEntity(final XMLDocument ownerDocument)
	{
		super(XMLNode.ENTITY_NODE, ownerDocument);	//construct the parent class
	}

	/**Constructor that specifies an entity name.
	@param ownerDocument The document which owns this node.
	@param newName The name of the XML entity.
//TODO fix	@exception XMLInvalidNameException Thrown if the specified name is invalid.
	*/
	public XMLEntity(final XMLDocument ownerDocument, final String newName)	//TODO fix throws XMLInvalidNameException
	{
		this(ownerDocument);	//do the default constructing
		setNodeName(newName);	//set the name
	}

	/**Constructor that specifies an entity name, along with character content.
	@param ownerDocument The document which owns this node.
	@param newName The name of the XML entity.
	@param value The value for this entity.
//TODO fix	@exception XMLInvalidNameException Thrown if the specified name is invalid.
	*/
	public XMLEntity(final XMLDocument ownerDocument, final String newName, final String value)	//TODO fix throws XMLInvalidNameException
	{
		this(ownerDocument, newName);	//do the default constructing
			//TODO should we check for a DOMException and throw something based on XMLException here?
		appendText(value);	//create a text node from the character content and add it to our children
	}

	/**Creates and returns a duplicate copy of this node. The clone has no parent.
	This function creates a "shallow" clone which does not contain clones of all
	child nodes. For the DOM version, see cloneNode().
	@return A duplicate copy of this node.
	@see XMLNode#cloneXMLNode
	@see XMLNode#cloneNode
	@see XMLNode#getParentXMLNode
	*/
	public Object clone()
	{
		final XMLEntity clone=new XMLEntity(getOwnerXMLDocument(), getNodeName());	//create a new entity with the same owner document and the same name
		clone.setPublicID(getPublicID());	//give the clone the same public ID
		clone.setSystemID(getSystemID());	//give the clone the same system ID
		clone.setNotationName(getNotationName());	//give the clone the same notation name
		return clone;	//return our cloned node
	}

	/**Function override to specify the types of nodes that can be child nodes.
	@return Whether or not the specified node can be added to the list of children.
	@see XMLNode#isNodeTypeAllowed
	*/
	protected boolean isNodeTypeAllowed(final Node node)	//TODO maybe rename this to isChildNodeTypeAllowed
	{
		return true;	//TODO fix this to actually check the type of node
	}

	/**Whether or not this entity is a parameter entity. Defaults to <code>false</code>.*/
	private boolean ParameterEntity=false;


	/**Returns whether or not this entity is a parameter entity.
	@return <code>true</code> if this is a parameterEntity, or <code>false</code>
	if this is a general entity.
	*/
	public boolean isParameterEntity() {return ParameterEntity;}

	/**Specifies if this entity is a parameter entity.
	@param parameterEntity <code>true</code> if this is a parameter entity, or
	<code>false</code> if this is a general entity.
	*/
	void setParameterEntity(final boolean parameterEntity) {ParameterEntity=parameterEntity;}

	/**Returns whether or not this is an external entity by checking to see if
	getSystemID() is not <code>null</code>.
	@return <code>true</code> if this is an external entity, else <code>false</code>.
	@see XMLEntity#getSystemID
	*/
	public boolean isExternalEntity() {return getSystemID()!=null;}

	/**Returns whether or not this is a parsed entity by checking to see if
	getNotationName() is <code>null</code>.
	@return <code>true</code> if this is a  parsed entity, else <code>false</code>.
	@see XMLEntity#getNotationName
	*/
	public boolean isParsedEntity() {return getNotationName()==null;}

	/**The public identifier of this entity, or <code>null</code> if there is none.*/
	private String PublicID=null;

	/**@return The public identifier, or <code>null</code> if there is none.
	For the DOM version, see getPublicId().
	@see XMLDocumentType#getPublicId
	@see XMLDocumentType#getSystemID
	*/
	public String getPublicID() {return PublicID;}

	/**@return The public identifier, or <code>null</code> if there is none.
	@see XMLDocumentType#getSystemID
	@see XMLDocumentType#getPublicID
	@version DOM Level 1
	*/
	public String getPublicId() {return getPublicID();}

	/**Sets the public identifier.
	@see XMLDocumentType#setSystemID
	*/
	void setPublicID(final String publicID) {PublicID=publicID;}

	/**The system identifier of this entity, or <code>null</code> if there is none.*/
	private String SystemID=null;

	/**@return The public identifier, or <code>null</code> if there is none.
	For the DOM version, see getSystemId().
	@see XMLDocumentType#getSystemId
	@see XMLDocumentType#getPublicID
	*/
	public String getSystemID() {return SystemID;}

	/**@return The public identifier, or <code>null</code> if there is none.
	@see XMLDocumentType#getSystemID
	@see XMLDocumentType#getPublicID
	@version DOM Level 1
	*/
	public String getSystemId() {return getSystemID();}

	/**Sets the system identifier.
	@see XMLDocumentType#setPublicID
	*/
	void setSystemID(final String systemID) {SystemID=systemID;}

	/**Sets the public identifier and system identifier. Only for use within this
	package.
	@param externalID The external ID object, which can contain both a public and system identifier.
	@see XMLDocumentType#setPublicID
	@see XMLDocumentType#setSystemID
	*/
	void setExternalID(final XMLExternalID externalID)
	{
		setPublicID(externalID.getPublicID());	//sets the public identifier
		setSystemID(externalID.getSystemID());	//sets the system identifier
	}

	/**For unparsed entities, the name of the notation for the entity. For
	parsed entities, this is <code>null</code>.
	*/
	private String NotationName=null;

	/**@return The notation name, if this is an unparsed entity, or
	<code>null</code> if this is a parsed entity.
	@version DOM Level 1
	*/
	public String getNotationName() {return NotationName;}

	/**Sets the system notation name.
	@see XMLDocumentType#setNotationName
	*/
	void setNotationName(final String notationName) {NotationName=notationName;}



	/**The source of this entity (such as a filename).*/

	private String SourceName="";


	/**@return The source of this entity (such as a filename).*/
	public String getSourceName() {return SourceName;}


	/*Sets the source of this entity (such as a filename).
	@param sourceName The source of this entity, such as a filename or the name of another entity.
	*/
	public void setSourceName(final String sourceName) {SourceName=sourceName;}

    /**
     * An attribute specifying the encoding used for this entity at the time 
     * of parsing, when it is an external parsed entity. This is 
     * <code>null</code> if it an entity from the internal subset or if it 
     * is not known.
     * @since DOM Level 3
     */
    public String getInputEncoding() {throw new UnsupportedOperationException();}	//TODO fix for DOM 3

    /**
     * An attribute specifying, as part of the text declaration, the encoding 
     * of this entity, when it is an external parsed entity. This is 
     * <code>null</code> otherwise.
     * @since DOM Level 3
     */
    public String getXmlEncoding() {throw new UnsupportedOperationException();}	//TODO fix for DOM 3

    /**
     * An attribute specifying, as part of the text declaration, the version 
     * number of this entity, when it is an external parsed entity. This is 
     * <code>null</code> otherwise.
     * @since DOM Level 3
     */
    public String getXmlVersion() {throw new UnsupportedOperationException();}	//TODO fix for DOM 3


	/**The index of the line on which this entity begins, or -1 if unknown.
	@see CharIndex
	*/
	private long LineIndex=-1;

		/**@return The index of the line on which this entity begins, or -XMLNode(). if unknown
		@see getCharIndex
		*/
		public long getLineIndex() {return LineIndex;}

		/**Sets the new line index of this entity.
		@param newLineIndex The new line index.
		@see setCharIndex
		*/
		public void setLineIndex(final long newLineIndex) {LineIndex=newLineIndex;}

	/**The index of the character at which this entitybegins, or -1 if unknown.
	@see LineIndex
	*/
	private long CharIndex=-1;

		/**@return The index of the character at which this entitybegins, or -1 if unknown.
		@see getLineIndex
		*/
		public long getCharIndex()	{return CharIndex;}

		/**Sets the new character index of this entity.
		@param newCharIndex The new character index.
		@see setLineIndex
		*/
		public void setCharIndex(final long newCharIndex) {CharIndex=newCharIndex;}

}

