package com.globalmentor.text.xml;

import org.w3c.dom.*;

import static com.globalmentor.text.xml.XML.*;

/**A tag in an XML document. G***make comment clarifying whether this is part of the DOM
@see XMLNode
*/
class XMLTag extends XMLNode
{
	/**An undefined tag.*/
	private final static int UNDEFINED_TAG=-1;
	/**A starting tag.*/
	public final static int START_TAG=0;
	/**An ending tag.*/
	public final static int END_TAG=1;
	/**An empty-element tag.*/
	public final static int EMPTY_ELEMENT_TAG=2;

	/**The type of tag this is.*/
	private int TagType=UNDEFINED_TAG;	//default to an undefined tag type

		/**@return The type of this tag.*/
		public int getTagType() {return TagType;}

		/**Sets the type of the tag.
		@param newTagType The new type for the tag.*/
		public void setTagType(int newTagType) {TagType=newTagType;}

	/**The name of the XML tag.*/
//G***del DOM	private String Name="";

		/**@return The name of the XML tag.*/
//del DOM		public String getName() {return Name;}

//G***fix
		/**Sets the name of the XML tag.
		@param newName The new name for the XML tag, or "" to specify no name.
		@exception XMLInvalidNameException Thrown if the specified name is invalid.
		*/
/*G***fix and reconcile with DOM
		public void setName(String newName) throws XMLInvalidNameException
		{
			if(newName.length()!=0)	//don't check empty strings, because empty strings really aren't valid XML names, and we want to allow clearing the name
				XMLProcessor.checkValidName(newName);	//make sure the name is valid
			Name=newName;
		}
*/


	/**The map of attributes.*/
//G***del	private Map AttributeList=new HashMap();

		/**@return The list of attributes, which may contain duplicates.*/
//G***del		Map getAttributeMap() {return AttributeMap;}

	/**The map of attributes in this element.
	@see XMLAttribute
	@see XMLNamedNodeMap
	*/
	private XMLNamedNodeMap AttributeMap=new XMLNamedNodeMap();

		/**Returns an <code>XMLNamedNodeMap</code> containing the attributes
		of this element. For the DOM version, see <code>getAttributes</code>.
		@return A map of attributes of this element node.
		@see XMLNode#getAttributes
		*/
		public XMLNamedNodeMap getAttributeXMLNamedNodeMap() {return AttributeMap;}	//G***should we just have the DOM version of getAttributes() or whatever it is? tidy up these comments, too

		/**Returns a <code>NamedNodeMap</code> containing the attributes of this element.
		@return A map of attributes of this element node.
		@see Attribute
		@see NamedNodeMap
		@see XMLNode#getAttributes
		@version DOM Level 1
		*/
		public NamedNodeMap getAttributes() {return getAttributeXMLNamedNodeMap();}

		/**Sets the attributes for this element. This has package access so helper
		  classes such as <code>XMLProcessor</code> can access this method.
		@param attributeMap The <code>XMLNamedNodeMap</code> with the attributes for this element.
		@see XMLProcessor
		*/
		void setAttributeXMLNamedNodeMap(final XMLNamedNodeMap attributeMap) {AttributeMap=attributeMap;}

	/*Constructor which requires an owner document to be specified.
	@param ownerDocument The document which owns this node.
	*/
	public XMLTag(final XMLDocument ownerDocument)
	{
		super(XMLNode.TAG_NODE, ownerDocument);	//construct the parent class
//G***del		setNodeType(XMLNode.TAG_NODE);	//show that this is an XML tag
	}

	/**@return A string representation of the tag as it should appear in an XML document.*/
	public String toXMLString()
	{
		final StringBuffer stringBuffer=new StringBuffer(); //create a string buffer to hold the characters
		stringBuffer.append(TAG_START); //append the starting character for a tag
		if(getTagType()==END_TAG) //if this is an ending tag
			stringBuffer.append(END_TAG_IDENTIFIER_CHAR); //add the character for identifying the tag as an end tag
		stringBuffer.append(getNodeName()); //append the name of the tag
		if(getTagType()==EMPTY_ELEMENT_TAG) //if this is an empty element tag
			stringBuffer.append(END_TAG_IDENTIFIER_CHAR); //add the character for identifying the tag as an empty element tag
//G***should we	add a space above for HTML compatibility for empty element tags?
		stringBuffer.append(TAG_END); //append the ending character for a tag
		return stringBuffer.toString(); //convert the string buffer to a string and return it
	}

	/**@return A string representation of the tag.*/
/*G***del when not needed
	public String toString()
	{
		final StringBuffer stringBuffer=new StringBuffer(super.toString()); //create a string buffer to hold the characters, starting with the default text
		stringBuffer.append(TAG_START); //append the starting character for a tag
		if(getTagType()==END_TAG) //if this is an ending tag
			stringBuffer.append(END_TAG_IDENTIFIER_CHAR); //add the character for identifying the tag as an end tag
		stringBuffer.append(getNodeName()); //append the name of the tag
		if(getTagType()==EMPTY_ELEMENT_TAG) //if this is an empty element tag
			stringBuffer.append(END_TAG_IDENTIFIER_CHAR); //add the character for identifying the tag as an empty element tag
//G***should we	add a space above for HTML compatibility for empty element tags?
		stringBuffer.append(TAG_END); //append the ending character for a tag
		return stringBuffer.toString(); //convert the string buffer to a string and return it
	}
*/
//G***do we want or need a clone() function here?

}
