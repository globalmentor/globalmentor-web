package com.garretwilson.text.xml;

import org.w3c.dom.Element;
import org.w3c.dom.DOMException;
import org.w3c.dom.TypeInfo;

/**An attribute of a tag (and therefore of an element).
@see XMLTag
@see XMLElement
@see org.w3c.dom.Attr
*/
public class XMLAttribute extends XMLNode implements org.w3c.dom.Attr
{

	/**Constructor for an attribute taking a name.
	@param ownerDocument The document which owns this node.
	@param newName The new name of the attribute.
//G***fix for DOM	@exception XMLInvalidNameException Thrown if the specified name is invalid.
	*/
	public XMLAttribute(final XMLDocument ownerDocument, final String newName) //G***fix for DOM throws XMLInvalidNameException
	{
		super(XMLNode.ATTRIBUTE_NODE, ownerDocument);	//construct the parent class
		setNodeName(newName);	//set the name
	}

	/**Constructor that uses a specified name and namespace URI. The attributes's
		names and values will be constructed in the following fashion:
		<table>
		<tr><th>Attribute</th><th>Value</th></tr>
		<tr><td><code>Node.nodeName</code></td><td>qualifiedName</td></tr>
		<tr><td><code>Node.namespaceURI</code></td><td><code>namespaceURI</code></td></tr>
		<tr><td><code>Node.prefix</code></td><td>prefix, extracted from <code>qualifiedName</code>, or <code>null</code> if there is no prefix</td></tr>
		<tr><td><code>Node.localName</code></td><td>local name, extracted from <code>qualifiedName</code></td></tr>
		<tr><td><code>Attr.name</code></td><td><code>qualifiedName</code></td></tr>
		<tr><td><code>Node.nodeValue</code></td><td>the empty string</td></tr>
		</table>
	@param ownerDocument The document which owns this node.
	@param namespaceURI The namespace URI of the attribute.
	@param qualifiedName The qualified name of the attribute.
	@param newValue The new value of the attribute.
	@exception DOMException
	<ul>
		<li>NAMESPACE_ERR: Raised if the specified <code>prefix</code> is
			malformed, if the <code>namespaceURI</code> of is
			<code>null</code>, if the specified prefix is "xml" and the
			<code>namespaceURI</code> of this node is different from
			"http://www.w3.org/XML/1998/namespace", if this node is an attribute
			and the specified prefix is "xmlns" and the <code>namespaceURI</code> of
			this node is different from "http://www.w3.org/2000/xmlns/",
			or if this node is an attribute and the <code>qualifiedName</code> of this
			node is "xmlns".</li>
	</ul>
	*/
	public XMLAttribute(final XMLDocument ownerDocument, final String namespaceURI, final String qualifiedName, final String newValue)
	{
		super(XMLNode.ATTRIBUTE_NODE, ownerDocument);	//construct the parent class
		setNamespaceURI(namespaceURI);  //set the namespace URI
		setNodeNameNS(qualifiedName);	//set the node name, correctly extracting namespace information
		setNodeValue(newValue);	//set the value G***do we want to have an XMLNode constructor that accepts a value as well?
	}

	/**Constructor for an attribute taking both a name and a value.
	@param ownerDocument The document which owns this node.
	@param newName The new name of the attribute.
	@param newValue The new value of the attribute.
//G***fix for DOM	@exception XMLInvalidNameException Thrown if the specified name is invalid.
	*/
	public XMLAttribute(final XMLDocument ownerDocument, final String newName, final String newValue) //G***fix for DOM throws XMLInvalidNameException
	{
		this(ownerDocument, newName);	//do the default constructing
		setNodeValue(newValue);	//set the value G***do we want to have an XMLNode constructor that accepts a value as well?
	}

	/**Constructor for an attribute taking a name.
	@param ownerDocument The document which owns this node.
	@param newName The new name of the attribute.
	@param newLineIndex The new line index.
	@param newCharIndex The new character index.
	//G***fix for DOM	@exception XMLInvalidNameException Thrown if the specified name is invalid.
	*/
	public XMLAttribute(final XMLDocument ownerDocument, final String newName, final long newLineIndex, final long newCharIndex) //G***fix for DOM throws XMLInvalidNameException
	{
		this(ownerDocument, newName);	//do the default constructing
		setLineIndex(newLineIndex);	//set the line index
		setCharIndex(newCharIndex);	//set the character index
	}

	/**Constructor for an attribute taking both a name and a value.
	@param ownerDocument The document which owns this node.
	@param newName The new name of the attribute.
	@param newValue The new value of the attribute.
	@param newLineIndex The new line index.
	@param newCharIndex The new character index.
	//G***fix for DOM	@exception XMLInvalidNameException Thrown if the specified name is invalid.
	*/
	public XMLAttribute(final XMLDocument ownerDocument, final String newName, final String newValue, final long newLineIndex, final long newCharIndex) //G***fix for DOM throws XMLInvalidNameException
	{
		this(ownerDocument, newName, newValue);	//do the default constructing
		setLineIndex(newLineIndex);	//set the line index
		setCharIndex(newCharIndex);	//set the character index
	}

//G***maybe make this have package level access or something
		/**Sets the name of the attribute.
		@param newName The new name for the attribute, or "" to specify no name.
//G***fix for DOM		@exception XMLInvalidNameException Thrown if the specified name is invalid.
		*/
/*G***fix exceptions and stuff and reconcile with DOM
		public void setName(String newName) //G***fix for DOM throws XMLInvalidNameException
		{
//G***fix for DOM			if(newName.length()!=0)	//don't check empty strings, because empty strings really aren't valid XML names, and we want to allow clearing the name
//G***fix for DOM				XMLProcessor.checkValidName(newName);	//make sure the name is valid
			Name=newName;
		}
*/

	/**The value of the attribute.*/
	private String Value="";

		/**Returns the value of this attribute.
		@return The value of this attribute.
		@version DOM Level 1
		*/
		public String getNodeValue() throws DOMException {return Value;}

		/**Sets the value of the attribute, and makes a note that the attribute has
		now been explicitely specified.
		@param nodeValue The new value for the attribute.
		@see XMLAttribute#getSpecified
		@version DOM Level 1
		*/
		public void setNodeValue(String nodeValue) throws DOMException
		{
			Value=nodeValue;	//set the value
			setSpecified(true);	//show that this value has been specified
		}

//G***fix these different value types, and maybe move them to node or something
		/**Gets the integer value of the attribute.
		@throw NumberFormatException Thrown when the value being read is not a valid integer.
		@return The integer value of the attribute.
		*/
		public int getIntegerValue() throws NumberFormatException
		{
			return Integer.parseInt(getNodeValue());	//convert the value to an integer and return it
		}

		/**Gets the boolean value of the attribute.
		@throw NumberFormatException Thrown when the value being read is not a valid boolean value.
		@return The boolean value of the attribute with the specified name.
		*/
		public boolean getAttributeBooleanValue(final String attributeName) throws NumberFormatException
		{
			return Boolean.valueOf(getNodeValue()).booleanValue();	//convert the value to a boolean and return it
		}

	/**The owner of this attribute, or null if there is no owner.*/
	private XMLElement OwnerElement=null;


		/**Returns the element node to which this attribute is attached, or
		<code>null</code> if this attribute is not in use.
		For the DOM version, see getOwnerElement().
		@return The owner of the attribute, or <code>null</code> if this attribute is not in use.
		@seeXMLAattribute#getOwnerElement
		@see XMLElement
		@version DOM Level 2
		@since DOM Level 2
		*/
		public XMLElement getXMLOwnerElement() {return OwnerElement;}

		/**Sets the owner of this attribute.
		@param newOwnerElement The new owner of the attribute.
		@see XMLElement
		*/
		public void setOwnerXMLElement(XMLElement newOwnerElement) {OwnerElement=newOwnerElement;}

		/**Returns the element node to which this attribute is attached, or
		<code>null</code> if this attribute is not in use.
		@return The owner of the attribute, or <code>null</code> if this attribute is not in use.
		@see XMLElement
		@version DOM Level 2
		@since DOM Level 2
		*/
		public Element getOwnerElement() {return getXMLOwnerElement();}

	/**Sets the namespace prefix of this node. This overridden function ensures
		that an attribute can never have the name "xmlns".
		<p>Note that setting this attribute, when permitted, changes the
		<code>nodeName</code> attribute, which holds the qualified name, as
		well as the <code>tagName</code> and <code>name</code> attributes of
		the <code>Element</code> and <code>Attr</code> interfaces, when
		applicable.</p>
		<p>Note also that changing the prefix of an attribute that is known to
		have a default value, does not make a new attribute with the default
		value and the original prefix appear, since the
		<code>namespaceURI</code> and <code>localName</code> do not change.</p>
	@exception DOMException
	<ul>
		<li>INVALID_CHARACTER_ERR: Raised if the specified prefix contains an
			illegal character.</li>
		<li>NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly.</li>
		<li>NAMESPACE_ERR: Raised if the specified <code>prefix</code> is
			malformed, if the <code>namespaceURI</code> of this node is
			<code>null</code>, if the specified prefix is "xml" and the
			<code>namespaceURI</code> of this node is different from
			"http://www.w3.org/XML/1998/namespace", if this node is an attribute
			and the specified prefix is "xmlns" and the <code>namespaceURI</code> of
			this node is different from "http://www.w3.org/2000/xmlns/",
			or if this node is an attribute and the <code>qualifiedName</code> of this
			node is "xmlns".</li>
	</ul>
	@since DOM Level 2
	*/
/*G***fix; this doesn't check the namespace URI, and it doesn't even update the prefix by calling the parent version
	public void setPrefix(String prefix) throws DOMException
	{
		if(prefix!=null && prefix.equals(XMLConstants.XMLNS_NAMESPACE_PREFIX))  //if this is the "xmlns" prefix
		  throw new XMLDOMException(DOMException.NAMESPACE_ERR, new Object[]{prefix});	//show that this namespace isn't allowed
	}
*/

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
		return new XMLAttribute(getOwnerXMLDocument(), getNamespaceURI(), getNodeName(), getNodeValue()); //create a duplicate attribute G***eventually put all line and char index information elsewhere, like in the parser
//G***del		return new XMLAttribute(getOwnerXMLDocument(), getNodeName(), getNodeValue(), getLineIndex(), getCharIndex());	//create a new node with the same owner document and the same name
	}

	/**The index of the line on which this attribute begins, or -1 if unknown
	@see CharIndex
	*/
	private long LineIndex=-1;

		/**@return The index of the line on which this attribute begins, or -1 if unknown
		@see getCharIndex
		*/
		public long getLineIndex() {return LineIndex;}

		/**Sets the new line index of this attribute.
		@param newLineIndex The new line index.
		@see setCharIndex
		*/
		public void setLineIndex(final long newLineIndex) {LineIndex=newLineIndex;}

	/**The index of the character at which this attribute begins, or -1 if unknown.
	@see LineIndex
	*/
	private long CharIndex=-1;

		/**@return The index of the character at which this attribute begins, or -1 if unknown.
		@see getLineIndex
		*/
		public long getCharIndex()	{return CharIndex;}

		/**Sets the new character index of this attribute.
		@param newCharIndex The new character index.
		@see setLineIndex
		*/
		public void setCharIndex(final long newCharIndex) {CharIndex=newCharIndex;}

	/**Returns the name of this attribute.
	@return The name of this attribute.
	@version DOM Level 1
	*/
	public String getName() {return getNodeName();}

	/**Whether or not this attribute was specified by the document or the user.
	Defaults to <code>true</code>.
	*/
	private boolean Specified=true;

	/**Specifies if this attribute was was explicitly given a value in the original document.
	If the user changes the value of the attribute, this will revert to <code>true</code>.
	@return <code>true</code> if this attribute was specifically given in the document or
	by the user.
	@see XMLAttribute#setValue
	@version DOM Level 1
	*/
	public boolean getSpecified() {return Specified;}

	/**Set whether this attribute was was explicitly given a value in the original
	document or by the user.
	@see XMLAttribute#setValue
	*/
	protected void setSpecified(final boolean specified) {Specified=specified;}

	/**Returns the value of the attribute as a string.
	Character and general entity references are replaced with their values.
	@return The value of the attribute as a string.
	@version DOM Level 1
	*/
	public String getValue() {return getNodeValue();}

	/**Sets the value of the attribute, and makes a note that the attribute has
	now been explicitely specified.
	G***fix On setting, this creates a <code>Text</code> node with the unparsed
	contents of the string.
	@param value The new value for the attribute.
	@exception DOMException
	<ul>
		<li>NO_MODIFICATION_ALLOWED_ERR: Raised when the node is readonly.</li>
	</ul>
	@see XMLAttribute#getNodeValue
	@see XMLAttribute#getSpecified
	@version DOM Level 1
	*/
	public void setValue(String value) throws DOMException {setNodeValue(value);}

    /**
     *  The type information associated with this attribute. While the type 
     * information contained in this attribute is guarantee to be correct 
     * after loading the document or invoking 
     * <code>Document.normalizeDocument()</code>, <code>schemaTypeInfo</code>
     *  may not be reliable if the node was moved. 
     * @since DOM Level 3
     */
    public TypeInfo getSchemaTypeInfo() {throw new UnsupportedOperationException();}	//TODO fix for DOM 3

    /**
     *  Returns whether this attribute is known to be of type ID (i.e. to 
     * contain an identifier for its owner element) or not. When it is and 
     * its value is unique, the <code>ownerElement</code> of this attribute 
     * can be retrieved using the method <code>Document.getElementById</code>
     * . The implementation could use several ways to determine if an 
     * attribute node is known to contain an identifier: 
     * <ul>
     * <li> If validation 
     * occurred using an XML Schema [<a href='http://www.w3.org/TR/2001/REC-xmlschema-1-20010502/'>XML Schema Part 1</a>]
     *  while loading the document or while invoking 
     * <code>Document.normalizeDocument()</code>, the post-schema-validation 
     * infoset contributions (PSVI contributions) values are used to 
     * determine if this attribute is a schema-determined ID attribute using 
     * the <a href='http://www.w3.org/TR/2003/REC-xptr-framework-20030325/#term-sdi'>
     * schema-determined ID</a> definition in [<a href='http://www.w3.org/TR/2003/REC-xptr-framework-20030325/'>XPointer</a>]
     * . 
     * </li>
     * <li> If validation occurred using a DTD while loading the document or 
     * while invoking <code>Document.normalizeDocument()</code>, the infoset <b>[type definition]</b> value is used to determine if this attribute is a DTD-determined ID 
     * attribute using the <a href='http://www.w3.org/TR/2003/REC-xptr-framework-20030325/#term-ddi'>
     * DTD-determined ID</a> definition in [<a href='http://www.w3.org/TR/2003/REC-xptr-framework-20030325/'>XPointer</a>]
     * . 
     * </li>
     * <li> from the use of the methods <code>Element.setIdAttribute()</code>, 
     * <code>Element.setIdAttributeNS()</code>, or 
     * <code>Element.setIdAttributeNode()</code>, i.e. it is an 
     * user-determined ID attribute; 
     * <p ><b>Note:</b>  XPointer framework (see section 3.2 in [<a href='http://www.w3.org/TR/2003/REC-xptr-framework-20030325/'>XPointer</a>]
     * ) consider the DOM user-determined ID attribute as being part of the 
     * XPointer externally-determined ID definition. 
     * </li>
     * <li> using mechanisms that 
     * are outside the scope of this specification, it is then an 
     * externally-determined ID attribute. This includes using schema 
     * languages different from XML schema and DTD. 
     * </li>
     * </ul>
     * <br> If validation occurred while invoking 
     * <code>Document.normalizeDocument()</code>, all user-determined ID 
     * attributes are reset and all attribute nodes ID information are then 
     * reevaluated in accordance to the schema used. As a consequence, if 
     * the <code>Attr.schemaTypeInfo</code> attribute contains an ID type, 
     * <code>isId</code> will always return true. 
     * @since DOM Level 3
     */
    public boolean isId() {throw new UnsupportedOperationException();}	//TODO fix for DOM 3


}
