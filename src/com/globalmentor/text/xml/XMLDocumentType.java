package com.globalmentor.text.xml;

import org.w3c.dom.NamedNodeMap;

//G***why don't we import the document type

/**An interface to the entities defined for an XML document along with other items.
@see XMLNode
@see org.w3c.dom.DocumentType
*/
public class XMLDocumentType extends XMLNode implements org.w3c.dom.DocumentType
{
	/*Constructor which requires an owner document to be specified.
	@param ownerDocument The document which owns this node.
	*/
	public XMLDocumentType(final XMLDocument ownerDocument)
	{
		super(XMLNode.DOCUMENT_TYPE_NODE, ownerDocument);	//construct the parent class
//G***del		setNodeType(XMLNode.DOCUMENT_TYPE_NODE);	//show that this is an XML document type
	}

	/**Constructor that specifies a document type name.
	@param ownerDocument The document which owns this node.
	@param newName The new name for the XML document type.
	*/
	public XMLDocumentType(final XMLDocument ownerDocument, final String newName)//G***fix throws XMLInvalidNameException
	{
		this(ownerDocument);	//do the default constructing
		setNodeName(newName);	//set the name
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
		final XMLDocumentType clone=new XMLDocumentType(getOwnerXMLDocument(), getNodeName());	//create a new document type with the same owner document and the same name
		clone.setEntityXMLNamedNodeMap(getEntityXMLNamedNodeMap().cloneDeep());	//clone the entities and put them in the clone
		//G***clone the notations
		clone.setPublicID(getPublicID());	//give the clone the same public ID
		clone.setSystemID(getSystemID());	//give the clone the same system ID
		return clone;	//return our cloned node
	}

	/**Returns the name of the DTD (i.e. the name immediately following the
		DOCTYPE keyword).
	@return The name of the document type.
	@version DOM Level 1
	*/
	public String getName() {return getNodeName();}

	/**The named node map of the parameter entities in this XML document.*/
	private XMLNamedNodeMap ParameterEntities=new XMLNamedNodeMap();

	/**Returns an <code>XMLNamedNodeMap</code> containing the parameter entities, both
	external and internal, declared in the DTD. Duplicates are discarded.
	Every entry in the map is an <code>XMLEntity</code>.
	@see XMLEntity
	@see XMLDocumentType#getEntities
	@see XMLDocumentType#getParameterEntities
	*/
	XMLNamedNodeMap getParameterEntityXMLNamedNodeMap() {return ParameterEntities;}

	/**Sets the <code>XMLNamedNodeMap</code> containing the parameter entities.
	@param entities The new map of parameter entities.
	*/
	void setParameterEntityXMLNamedNodeMap(final XMLNamedNodeMap parameterEntities) {ParameterEntities=parameterEntities;}

	/**The named node map of the general entities in this XML document.*/
	private XMLNamedNodeMap Entities=new XMLNamedNodeMap();

	/**Returns an <code>XMLNamedNodeMap</code> containing the general entities, both
	external and internal, declared in the DTD. Duplicates are discarded.
	Every entry in the map is an <code>XMLEntity</code>. For the DOM version,
	see getEntities().
	The DOM Level 2 does not support editing entities, so the entities cannot be
	altered in any way.
	@see XMLEntity
	@see XMLDocumentType#getEntities
	*/
	public XMLNamedNodeMap getEntityXMLNamedNodeMap() {return Entities;}

	/**Sets the <code>XMLNamedNodeMap</code> containing the general entities.
	@param entities The new map of general entities.
	*/
	private void setEntityXMLNamedNodeMap(final XMLNamedNodeMap entities) {Entities=entities;}

	/**Returns a <code>NamedNodeMap</code> containing the general entities, both
	external and internal, declared in the DTD. Duplicates are discarded.
	Every entry in the map emplements <code>Entity</code>.
	The DOM Level 2 does not support editing entities, so the entities cannot be
	altered in any way.
	@see Entity
	*/
	public NamedNodeMap getEntities() {return getEntityXMLNamedNodeMap();}

	/**
	 * A <code>NamedNodeMap</code> containing  the notations declared in the
	 * DTD. Duplicates are discarded. Every node in this map also implements
	 * the <code>Notation</code> interface.
	 * <br>The DOM Level 2 does not support editing notations, therefore
	 * <code>notations</code> cannot be altered in any way.
	 */
	public NamedNodeMap       getNotations() {return null;}	//G***fix

	/**The public identifier of this document type, or <code>null</code> if there is none.*/
	private String PublicID=null;


	/**@return The public identifier, or <code>null</code> if there is none.
	@see XMLDocumentType#getSystemID
	@version DOM Level 2
	@since DOM Level 2
	*/
	public String getPublicID() {return PublicID;}

    /**
     *  The public identifier of the external subset.
     * @since DOM Level 2
     */
    public String getPublicId() {return getPublicID();}	//G***fix, comment


	/**Sets the public identifier.
	@see XMLDocumentType#setSystemID
	*/
	public void setPublicID(final String publicID) {PublicID=publicID;} //G***this shouldn't be public; for now it's used by XHTMLTidier

	/**The system identifier of this document type, or <code>null</code> if there is none.*/
	private String SystemID=null;

	/**@return The public identifier, or <code>null</code> if there is none.
	@see XMLDocumentType#getPublicID
	@version DOM Level 2
	@since DOM Level 2
	*/
	public String getSystemID() {return SystemID;}

    /**
     *  The system identifier of the external subset.
     * @since DOM Level 2
     */
    public String getSystemId() {return getSystemID();}	//G***fix, comment


	/**Sets the system identifier.
	@see XMLDocumentType#setPublicID
	*/
	public void setSystemID(final String systemID) {SystemID=systemID;} //G***this shouldn't be public; for now it's used by XHTMLTidier

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

    /**
     *  The internal subset as a string. The actual content returned depends
     * on how much information is available to the implementation. This may
     * vary depending on various parameters, including the XML processor used
     * to build the document.
     * @since DOM Level 2
     */
    public String getInternalSubset() {return "";}	//G***fix

}
