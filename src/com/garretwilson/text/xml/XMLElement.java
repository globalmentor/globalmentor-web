package com.garretwilson.text.xml;

import java.io.*;
import java.util.*;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.DOMException;
import org.w3c.dom.TypeInfo;
//G***del import org.w3c.dom.css.CSSValue;
//G***del import com.garretwilson.lang.StringUtilities;	//G***see if we really need this or not
//G***del import com.garretwilson.text.xml.stylesheets.css.XMLCSSStyleDeclaration;
import com.garretwilson.util.Debug;

/**An element in an XML document.
@see XMLNode
@see org.w3c.dom.Element
*/
public class XMLElement extends XMLNode implements org.w3c.dom.Element
{
	/*Constructor which requires an owner document to be specified.
	@param ownerDocument The document which owns this node.
	*/
	public XMLElement(final XMLDocument ownerDocument)
	{
		super(XMLNode.ELEMENT_NODE, ownerDocument);	//construct the parent class
//G***del		setNodeType(XMLNode.ELEMENT_NODE);	//show that this is an XML element
	}

	/**Constructor that uses a specified name. Namespace-related names will be set
		to <code>null</code>.
	@param ownerDocument The document which owns this node.
	@param newName The new name for the XML element, or "" to specify no name.
//G***fix	@exception XMLInvalidNameException Thrown if the specified name is invalid.
	*/
	public XMLElement(final XMLDocument ownerDocument, final String newName)//G***fix throws XMLInvalidNameException
	{
		this(ownerDocument);	//do the default constructing
		setNodeName(newName);	//set the node name
	}

	/**Constructor that creates an element from an existing tag. All attributes
		from the tag will be used.
	@param ownerDocument The document which owns this node.
	@param elementTag The tag which begins the element.
//G***fix	@exception XMLInvalidNameException Thrown if the specified name is invalid.
	*/
	public XMLElement(final XMLDocument ownerDocument, final XMLTag elementTag)//G***fix throws XMLInvalidNameException
	{
		this(ownerDocument);	//do the default constructing
		setNodeName(elementTag.getNodeName());	//set the node name from the tag
		setAttributeXMLNamedNodeMap(elementTag.getAttributeXMLNamedNodeMap());	//give the element the same list of attributes that the tag formed from the file
	}

	/**Constructor that uses a specified name and namespace URI. The element's
		names will be constructed in the following fashion:
		<table>
		<tr><th>Attribute</th><th>Value</th></tr>
		<tr><td><code>Node.nodeName</code></td><td><code>qualifiedName</code></td></tr>
		<tr><td><code>Node.namespaceURI</code></td><td><code>namespaceURI</code></td></tr>
		<tr><td><code>Node.prefix</code></td><td>prefix, extracted from <code>qualifiedName</code>, or <code>null</code> if there is no prefix</td></tr>
		<tr><td><code>Node.localName</code></td><td>local name, extracted from <code>qualifiedName</code></td></tr>
		<tr><td><code>Element.tagName</code></td><td><code>qualifiedName</code></td></tr>
		</table>
	@param ownerDocument The document which owns this node.
	@param namespaceURI The namespace URI of the element.
	@param qualifiedName The qualified name of the element.
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
	public XMLElement(final XMLDocument ownerDocument, final String namespaceURI, final String qualifiedName)
	{
		this(ownerDocument);	//do the default constructing
		setNamespaceURI(namespaceURI);  //set the namespace URI
		setNodeNameNS(qualifiedName);	//set the node name, correctly extracting namespace information
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
		final XMLElement clone; //we'll assign a clone to this variable
		final String localName=getLocalName();  //get the local name
		if(localName!=null) //if we have a local name, this element recognizes namespace
			clone=new XMLElement(getOwnerXMLDocument(), getNamespaceURI(), getNodeName());  //create a new element with the same owner document, namespace, and name
		else  //if we don't have a local name, this is not a namespace-aware element
			clone=new XMLElement(getOwnerXMLDocument(), getNodeName());	//create a new element with the same owner document and the same name
		clone.setAttributeXMLNamedNodeMap(getAttributeXMLNamedNodeMap().cloneDeep());	//clone the attributes and put them in the clone
		return clone;	//return our cloned node
	}

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
		private XMLNamedNodeMap getAttributeXMLNamedNodeMap() {return AttributeMap;}  //G***probably remove this in favor of the DOM version

		/**Returns a <code>NamedNodeMap</code> containing the attributes
		of this element.
		@return A map of attributes of this element node.
		@see Attribute
		@see NamedNodeMap
		@see XMLNode#getAttributes
		@version DOM Level 1
		*/
		public NamedNodeMap getAttributes() {return getAttributeXMLNamedNodeMap();}

		/**Sets the attributes for this element. This has package access so helper
		  classes such as <code>XMLNamespaceProcessor</code> can access this method.
		@param attributeMap The <code>XMLNamedNodeMap</code> with the attributes for this element.
		@see XMLNamespaceProcessor
		*/
		void setAttributeXMLNamedNodeMap(final XMLNamedNodeMap attributeMap) {AttributeMap=attributeMap;}

		/**Gets an attribute object with a certain name.
		@param attributeName The name of the desired attribute.
		@return A reference to the attribute object with the specified name, or null if no attribute with that name exists.
		*/
/*G***fix
		public XMLAttribute getAttribute(final String attributeName)
		{
			return (XMLAttribute)getAttributeXMLNamedNodeMap().get(attributeName);	//return the attribute with the given name
		}
*/

		/**Gets the value of an attribute with a certain name.
		@param attributeName The name of the desired attribute.
		@return The value of the attribute with the specified name, or "" if no such named attribute exists.
		*/
/*G***fix
		public String getAttributeValue(final String attributeName)
		{
			final XMLAttribute attribute=getAttribute(attributeName);	//find the attribute with the given name
			if(attribute!=null)	//if an attribute exists with that name
				return attribute.getNodeValue();	//return the name of that attribute
			else	//if no attribute has the specified name
				return "";	//return an empty string
		}
*/

		/**Gets the integer value of an attribute with a certain name.
		@param attributeName The name of the desired attribute.
		@return The integer value of the attribute with the specified name, or 0 if no such named attribute exists or there was an invalid integer.
		*/
/*G***fix
		public int getAttributeIntegerValue(final String attributeName)
		{
			final String attributeValue=getAttributeValue(attributeName);	//get the string value of the attribute
			try
			{
				return Integer.parseInt(attributeValue);	//convert the value to an integer and return it
			}
			catch(NumberFormatException e)	//if this was an invalid number
			{
				return 0;	//return zero as the default G***maybe later make a function that specifies the default
			}
		}
*/

		/**Gets the boolean value of an attribute with a certain name.
		@param attributeName The name of the desired attribute.
		@return The boolean value of the attribute with the specified name, or false if no such named attribute exists or there was an invalid boolean value.
		*/
/*G***fix
		public boolean getAttributeBooleanValue(final String attributeName)
		{
			final String attributeValue=getAttributeValue(attributeName);	//get the string value of the attribute
			try
			{
				return Boolean.valueOf(attributeValue).booleanValue();	//convert the value to a boolean and return it
			}
			catch(NumberFormatException e)	//if this was an invalid number
			{
				return false;	//return false as the default G***maybe later make a function that specifies the default
			}
		}
*/

	/**The local CSS style, if any, of this element.*/
//G***del	private XMLCSSStyleDeclaration LocalStyle=null;

	/**@return The local CSS style of this element (e.g. style from a "style"
		attribute), or <code>null</code> if there is no local CSS style.
	@see XMLCSSStyleDeclaration
	@see XMLElement#getCSSStyle
	*/
//G***del	public XMLCSSStyleDeclaration getLocalCSSStyle() {return LocalStyle;}

	/**Sets the local CSS style of this element.
	@param style The new local CSS style of this element.
	@see XMLCSSStyleDeclaration
	@see XMLElement#setCSSStyle
	*/
//G***del	public void setLocalCSSStyle(final XMLCSSStyleDeclaration style) {LocalStyle=style;}

	/**The resulting CSS style of this element, after processing stylesheets and local styles.*/
//G***del	private XMLCSSStyleDeclaration Style=new XMLCSSStyleDeclaration();

	/**@return The resulting CSS style of this element, after processing
		stylesheets and local styles.
	@see XMLCSSStyleDeclaration
	@see XMLElement#getLocalCSSStyle
	*/
//G***del	public XMLCSSStyleDeclaration getCSSStyle() {return Style;}

	/**Sets the resulting style of this element, after processing stylesheets and
		local styles.
	@param style The new resulting style of this element.
	@see XMLCSSStyleDeclaration
	@see XMLElement#setLocalCSSStyle
	*/
//G***del	public void setCSSStyle(final XMLCSSStyleDeclaration style) {Style=style;}


	/**Retrieves the object representation of the value of this element's CSS
		style property if it has been set. If it has not been set, this element's
		parent node will be searched, and so on up the hierarchy.
	@param propertyName  The name of the CSS property. See the CSS property index.
	@return  Returns the value of the property if it has been set somewhere up the
		hierarchy, or <code>null</code> if the property has not been set.
	@see XMLCSSStyleDeclaration#getPropertyCSSValue
	@see XMLCSSStyleDeclaration#getPropertyValue
	@see XMLCSSStyleDeclaration#setProperty
	*/
/*G***fix or delete
	public CSSValue getPropertyCSSValue(String propertyName)
	{
		final CSSValue cssValue=getCSSStyle().getPropertyCSSValue();	//get the



		return (CSSValue)get(propertyName);	//get the item with the specified property name
	}
*/

	/**Function override to specify the types of nodes that can be child nodes.
	@return Whether or not the specified node can be added to the list of children.
	@see XMLNode#isNodeTypeAllowed
	*/
	protected boolean isNodeTypeAllowed(final Node node)
	{
		switch(node.getNodeType())	//see which type of node this is
		{
			case ELEMENT_NODE:		//if we accept one of these types of child nodes
			case TEXT_NODE:
			case COMMENT_NODE:
			case PROCESSING_INSTRUCTION_NODE:
			case CDATA_SECTION_NODE:
			case ENTITY_REFERENCE_NODE:
				return true;	//show that we accept it
			default:	//if this is another type of node
				return false;	//show that we don't accept it
		}
	}

		/**Sets the name of the XML element.
		@param newName The new name for the XML element, or "" to specify no name.
		@exception XMLInvalidNameException Thrown if the specified name is invalid.
		*/
		//G***fix; either change name or make it have package access or something
/*G***del and check about valid names
		public void setName(String newName) throws XMLInvalidNameException
		{
			if(newName.length()!=0)	//don't check empty strings, because empty strings really aren't valid XML names, and we want to allow clearing the name
				XMLProcessor.checkValidName(newName);	//make sure the name is valid
			setNodeName(newName);	//set the name
		}
*/

//G***it would probably be best to find a way not to have to typecast all of the getChildNodes() calls below

	/**@return An iterator of only child elements.
	@see XMLElement
	@see XMLNodeIterator
	*/
/*G***del if we don't need
	public final synchronized Iterator elements()
	{
		return new XMLNodeIterator((XMLNodeList)getChildNodes(), XMLNode.ELEMENT_NODE);	//construct an XMLNodeIterator that looks at our children and only returns elements
	}
*/

//G***maybe make an entities() function here

	/**@return An iterator of only comments.
	@see XMLComment
	@see XMLNodeIterator
	*/
/*G***del if we don't need
	public final synchronized Iterator comments()
	{
		return new XMLNodeIterator((XMLNodeList)getChildNodes(), XMLNode.COMMENT_NODE);	//construct an XMLNodeIterator that looks at our children and only returns comments
	}
	*/

	/**@return An iterator of only processing instructions.
	@see XMLProcessingInstructions
	@see XMLNodeIterator
	*/
/*G***del if we don't need
	public final synchronized Iterator processingInstructions()
	{
		return new XMLNodeIterator((XMLNodeList)getChildNodes(), XMLNode.PROCESSING_INSTRUCTION_NODE);	//construct an XMLNodeIterator that looks at our children and only returns processing instructions
	}
*/

	/**@return An iterator of only CDATA objects.
	@see XMLCDATA
	@see XMLNodeIterator
	*/
/*G***del if we don't need
	public final synchronized Iterator CDATAs()
	{
		return new XMLNodeIterator((XMLNodeList)getChildNodes(), XMLNode.CDATA_SECTION_NODE);	//construct an XMLNodeIterator that looks at our children and only returns CDATA object
	}
*/

	/**@return The complete character data of the element without markup.
	@see XMLEntity#getCharacterData
	*/
/*G***del if we don't need
	public String getCharacterData()
	{
		String characterData="";	//we'll collect the character data here
*/
/*G***fix this to work with the DOM
		int charIndex=0;	//this will be our index into this element's character content
		for(int childIndex=0; childIndex<getChildNodes().getLength(); childIndex++)	//look at each child node
		{
			Node node=getChildNodes().item(childIndex);	//look at this node
			String charactersToAdd="";	//this will be the characters to add
			switch(node.getNodeType())	//see which type of object this is
			{
				case XMLNode.ELEMENT_NODE:	//if this is an element
					charactersToAdd=((XMLElement)node).getCharacterData();	//get the characters from the element
					break;
				case XMLNode.CDATA_SECTION_NODE:	//if this is a CDATA object
					charactersToAdd+=((XMLCDATA)node).getNodeValue();	//get the characters from the CDATA object
					break;
				//G***add code for comment sections and such
			}
			if(charactersToAdd.length()!=0)	//if we have characters to add (which means that this is an object whose characters should be added
			{
				int objectCharacterIndex=node.getCharacterIndex();	//get the character index of the object
				if(objectCharacterIndex>getCharacterContent().length())	//if for some reason the object's character index is above the end of our character content (which would make substring() throw an error)
					objectCharacterIndex=getCharacterContent().length();		//bring the object's character index back into range; put it one past the end of the character content, which menas we're defaulting to putting the object after our character content
				if(objectCharacterIndex>charIndex)	//if this object comes later in the character content
				{
					characterData+=getCharacterContent().substring(charIndex, objectCharacterIndex);	//append all the character content up to this point
					charIndex=objectCharacterIndex;	//show how much character content we've added to the character data
				}
				characterData+=charactersToAdd;	//add the character data from this object
			}
		}
		if(charIndex<getCharacterContent().length())	//if we still have character content we haven't added, yet
			characterData+=getCharacterContent().substring(charIndex);	//append the remaining characters
*/
/*G***del if we don't need
		return characterData;	//return the character data we constructed
	}
*/

	/**Returns the name of the element. For example, in:
	<pre>
		&lt;elementExample id="demo"&gt;
						 ...
		&lt;/elementExample&gt; ,
	</pre>
		the tag name has the value "elementExample". Note that this is case-preserving
		in XML, as are all of the operations of the DOM. The HTML DOM returns
		the <code>tagName</code> of an HTML element in the canonical uppercase
		form, regardless of the case in the  source HTML document.
	@version DOM Level 1
	*/
	public String getTagName() {return getNodeName();}	//G***check to see if this is an HTML document and canonicize the names if so

	/**Retrieves an attribute value by name.
	@param name The name of the attribute to retrieve.
	@return The attribute value as a string, or the empty string if that attribute
	does not have a specified or default value.
	@see XMLAttribute
	@see XMLElement#getAttributeNode
	@version DOM Level 1
	*/
	public String getAttribute(String name)
	{
		final XMLAttribute attribute=(XMLAttribute)getAttributeNode(name);	//see if we can get a matching attribute node
		return attribute!=null ? attribute.getNodeValue() : "";	//return the value if we found an attribute, otherwise return an empty string
	}

	/**Adds a new attribute. If an attribute with that name is already present
		in the element, its value is changed to be that of the value parameter.
		This value will appropriately escape any markup characters before this value
		is written out, so in order to assign an attribute value that contains
		entity references, create an attribute node plus any text and entity
		references nodes and use setAttributeNode() to assign that tree as the value
		of that attribute.
	@param name The name of the attribute to create or alter.
	@param value Value to set in string form.
	@except DOMException
	<ul>
		<li>INVALID_CHARACTER_ERR: Raised if the specified name contains an
			invalid character.</li>
		<li>NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly.</li>
	</ul>
	@see XMLAttribute
	@see XMLText
	@see XMLEntityReference
	@see XMLElement#setAttributeNode
	@version DOM Level 1
	*/
	public void setAttribute(String name, String value) throws DOMException
	{
		//G***check for valid name characters here
		//G***check for read-only status here
		XMLAttribute attribute;	//we'll use this to either find an existing attribute or create a new one
		if(getAttributeXMLNamedNodeMap().containsKey(name))	//if an attribute with this name already exists
			attribute=(XMLAttribute)getAttributeXMLNamedNodeMap().getNamedItem(name);	//get a reference to the attribute
		else	//if this attribute doesn't exist
		{
			attribute=new XMLAttribute(getOwnerXMLDocument(), name);	//create an attribute with the specified name
			getAttributeXMLNamedNodeMap().setNamedItem(attribute);	//add this attribute to our map
		}
		attribute.setValue(value);	//set the value of the attribute G***check about setValue() setNodeValue() and such
	}

	/**Removes an attribute by name. If the removed attribute is known to have a
		default value, an attribute immediately appears containing the default value.
	@param name The name of the attribute to remove.
	@exception DOMException
	<ul>
		<li>NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly.</li>
	</ul>
	@version DOM Level 1
	*/
	public void removeAttribute(String name) throws DOMException
	{
		//G***check read-only status here
		if(hasAttribute(name))  //if the attribute exists (needed because this function in the DOM throws now not-found exception)
			getAttributeXMLNamedNodeMap().removeNamedItem(name);	//remove any attribute with this name, if there is one
		//G***check to see if there's a default value that we should add
	}

	/**Retrieves an attribute node by name.
	@param name The name of the attribute to retrieve.
	@return The attribute node with the specified attribute name or
	<code>null</code> if there is no such attribute.
	@see XMLAttribute
	@see XMLElement#getAttribute
	@version DOM Level 1
	*/
	public Attr getAttributeNode(String name)
	{
		if(hasAttribute(name))	//if an attribute with this name exists
		{
			return (XMLAttribute)getAttributeXMLNamedNodeMap().getNamedItem(name);	//return a reference to the attribute
		}
		else	//if this attribute doesn't exist
			return null;	//return null
	}

	/**Adds a new attribute. If an attribute with that name is already present
		in the element, it is replaced by the new one.
	@param newAttr The attribute node to add to the attribute list.
	@return If the new attribute replaces an existing attribute with the same
		name, the  previously existing attribute node is returned, otherwise
		<code>null</code> is returned.
	@exception DOMException
	<ul>
		<li>WRONG_DOCUMENT_ERR: Raised if the new attribute was created from a
			different document than the one that created the element.</li>
		<li>NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly.</li>
		<li>INUSE_ATTRIBUTE_ERR: Raised if the new attribute is already an
			attribute of another element node. The DOM user must explicitly clone
			attribute nodes to re-use them in other elements.</li>
	</ul>
	@version DOM Level 1
	*/
	public Attr setAttributeNode(Attr newAttr) throws DOMException
	{
		//G***make sure this is the correct document
		//G***check for read-only status
		//G***check to see if the attribute is already in use
		return (Attr)getAttributeXMLNamedNodeMap().setNamedItem(newAttr);	//add this attribute to our map
	}

	/**
	 * Removes the specified attribute.
	 * @param oldAttr The <code>Attr</code> node to remove from the attribute
	 *   list. If the removed <code>Attr</code> has a default value it is
	 *   immediately replaced.
	 * @return The <code>Attr</code> node that was removed.
	 * @exception DOMException
	 *   NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly.
	 *   <br>NOT_FOUND_ERR: Raised if <code>oldAttr</code> is not an attribute
	 *   of the element.
	 */
	public Attr               removeAttributeNode(Attr oldAttr)
																								throws DOMException {return null;}	//G***fix


	/**Returns a NodeList of first-level descendant elements with a given tag name.
	If deep is set to <code>true</code>, returns a NodeList of all descendant elements
		with a given tag name, in the	order	in which they would be encountered in a preorder
		traversal of the element tree.
	@param name The name of the tag to match on. The special value "*" matches all tags.
	@param deep Whether or not matching child elements of each matching child element, etc. should be included.
	@return A new NodeList object containing all the matching element nodes.
	@see XMLElement
	@see XMLDocument
	@see XMLDocument#getElementsByTagName
	@see XMLNodeList
	*/
/*G***del
	public NodeList getElementsByTagName(String name, boolean deep)
	{
		return getNodesByName(ELEMENT_NODE, name, deep);	//get all the element nodes with matching name
	}
*/

	/**Returns a NodeList of all descendant elements with a given tag name, in the
		order	in which they would be encountered in a preorder traversal of the element
		tree.
	@param name The name of the tag to match on. The special value "*" matches all tags.
	@return A new NodeList object containing all the matching element nodes.
	@see XMLElement
	@see XMLDocument
	@see XMLDocument#getElementsByTagName
	@see XMLNodeList
	@version DOM Level 1
	*/
	public NodeList getElementsByTagName(String name) //G***does this correctly return a *preorder* traversal?
	{
//G***we may want to just remove the function below and replace it with a direct call to getNodesByName()
		return getNodesByName(ELEMENT_NODE, name, true);	//get the elements, showing that we want *all* the child elements at whatever level
//G***del		return getElementsByTagName(name, true);	//get the elements, showing that we want *all* the child elements at whatever level
	}

	/**Returns a NodeList of all descendant elements with a given nested order of
		elements named in tagPathArray. At each level, the first matching element
		will be used if there is more than one match, but if that path comes to a
		dead end the next matching element will be searched, and so on up the hierarchy.
		If the path has been traversed up to its last element, the list of nodes will
		be returned even if it is emty.<br/>
		That is, for the xml structure
		<code>&lt;top&gt;&lt;middle&gt;&lt;tag1/&gt;&lt;tag2/&gt;&lt;tag3/&gt;&lt;/middle&gt;&lt;/top&gt;</code>,
		all the internal tags may be returned by passing a path of
		{"top", "middle", "*"}.
	@param tagPathArray An array of names representing a path of tags to follow in
		retrieving a list of elements. The special value "*" matches all tags.
	@return A new NodeList object containing all the matching element nodes at the
		end of the path, or <code>null</code> if the path could not be traversed up
		to its last tag name.
	@see XMLElement
	@see XMLDocument
	@see XMLNodeList
	@see XMLDocument#getElementsByTagPath
	@see #getElementsByTagName
	*/
/*G***del if not needed
	public NodeList getElementsByTagPath(final String[] tagPathArray)
	{
		if(tagPathArray.length>0)	//if a valid path was passed
			return getElementsByTagPath(tagPathArray, 0);	//start with this node and try to traverse the path starting at its first element
		else	//if there are no tag names in the path
			return null;	//show that no path was passed
	}
*/

	/**Helper function for <code>getElementsByTagPath()<code> which recursively
		finds the matching path, searching alternate paths if one comes to a dead end.
		Returns a NodeList of all descendant elements with a given nested order of
		elements named in tagPathArray. At each level, the first matching element
		will be used if there is more than one match, but if that path comes to a
		dead end the next matching element will be searched, and so on up the hierarchy.
		If the path has been traversed up to its last element, the list of nodes will
		be returned even if it is emty.<br/>
		That is, for the xml structure
		<code>&lt;top&gt;&lt;middle&gt;&lt;tag1/&gt;&lt;tag2/&gt;&lt;tag3/&gt;&lt;/middle&gt;&lt;/top&gt;</code>,
		all the internal tags may be returned by passing a path of
		{"top", "middle", "*"}.<br/>
	@param tagPathArray An array of names representing a path of tags to follow in
		retrieving a list of elements. The special value "*" matches all tags.
	@param pathIndex The current part of the path being searched.
	@return A new NodeList object containing all the matching element nodes at the
		end of the path, or <code>null</code> if the path could not be traversed up
		to its last tag name.
	@see XMLElement
	@see XMLDocument
	@see XMLNodeList
	@see #getElementsByTagName
	@see #getElementsByTagPath
	*/
/*G***del if not needed
	protected NodeList getElementsByTagPath(final String[] tagPathArray, final int pathIndex)
	{
		final NodeList elementsAvailableList=getElementsByTagName(tagPathArray[pathIndex], false);	//get the list of elements at this level in the path
		if(pathIndex==tagPathArray.length-1)	//if this is the last tag on the path
			return elementsAvailableList;	//return the list we constructed, even if it is empty
		else	//otherwise, if we're somewhere in the middle of the path
		{
			for(int elementIndex=0; elementIndex<elementsAvailableList.getLength(); ++elementIndex)	//look at each of our possible paths at this point
			{
				final Element element=(Element)elementsAvailableList.item(elementIndex);	//get a reference to this element
				final NodeList elementsFoundList=((XMLElement)element).getElementsByTagPath(tagPathArray, pathIndex+1);	//see if this path works
				if(elementsFoundList!=null)	//if this path retrieved a list of elements (even an empty list), this means we were able to follow the complete path
					return elementsFoundList;	//return the list we found
			}
			return null;	//show that none of our paths worked; they all failed somewhere in the middle
		}
	}
*/

	/**Retrieves an attribute value by local name and namespace URI.
	@param namespaceURI The namespace URI of the attribute to retrieve.
	@param localName The local name of the attribute to retrieve.
	@return The <code>Attr</code> value as a string, or the empty string
		if that attribute does not have a specified or default value.
	@since DOM Level 2
	*/
	public String getAttributeNS(String namespaceURI, String localName)
	{
		final XMLAttribute attribute=(XMLAttribute)getAttributeNodeNS(namespaceURI, localName);	//see if we can get a matching attribute node
		return attribute!=null ? attribute.getNodeValue() : "";	//return the value if we found an attribute, otherwise return an empty string
	}

	/**Adds a new attribute. If an attribute with the same local name and
		namespace URI is already present on the element, its prefix is
		changed to be the prefix part of the <code>qualifiedName</code>, and
		its value is changed to be the <code>value</code> parameter. This
		value is a simple string; it is not parsed as it is being set. So any
		markup (such as syntax to be recognized as an entity reference) is
		treated as literal text, and needs to be appropriately escaped by the
		implementation when it is written out. In order to assign an
		attribute value that contains entity references, the user must create
		an <code>Attr</code> node plus any <code>Text</code> and
		<code>EntityReference</code> nodes, build the appropriate subtree,
		and use <code>setAttributeNodeNS</code> or
		<code>setAttributeNode</code> to assign it as the value of an
		attribute.
	@param namespaceURI The namespace URI of the attribute to create or alter.
	@param qualifiedName The qualified name of the attribute to create or alter.
	@param valueThe value to set in string form.
	@exception DOMException
	<ul>
		<li>INVALID_CHARACTER_ERR: Raised if the specified qualified name
		  contains an illegal character.</li>
		<li>NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly.</li>
		<li>NAMESPACE_ERR: Raised if the <code>qualifiedName</code> is
		  malformed, if the <code>qualifiedName</code> has a prefix and the
		  <code>namespaceURI</code> is <code>null</code>, if the
		  <code>qualifiedName</code> has a prefix that is "xml" and the
		  <code>namespaceURI</code> is different from "http://www.w3.org/XML/1998/namespace",
			or if the <code>qualifiedName</code> is "xmlns" and the
		  <code>namespaceURI</code> is different from "http://www.w3.org/2000/xmlns/".</li>
	</ul>
	@since DOM Level 2
	*/
	public void setAttributeNS(String namespaceURI, String qualifiedName, String value) throws DOMException
	{
		//G***check for valid name characters here
		//G***check for read-only status here
		final String localName=XMLUtilities.getLocalName(qualifiedName); //get the local name of the qualified name
		XMLAttribute attribute;	//we'll use this to either find an existing attribute or create a new one
		if(getAttributeXMLNamedNodeMap().containsKeyNS(namespaceURI, localName))	//if an attribute with this name already exists
		{
			attribute=(XMLAttribute)getAttributeXMLNamedNodeMap().getNamedItemNS(namespaceURI, localName);  //get a reference to that attribute
		  final String prefix=XMLUtilities.getPrefix(qualifiedName); //get the prefix of the qualified name
		  attribute.setPrefix(prefix);  //update the prefix
		  attribute.setValue(value);  //update the attribute's value
		}
		else	//if this attribute doesn't exist
		{
//G***del Debug.trace("adding attribute with value: ", value);  //G***del
			attribute=new XMLAttribute(getOwnerXMLDocument(), namespaceURI, qualifiedName, value); //create an attribute with the specified name and value
			getAttributeXMLNamedNodeMap().setNamedItemNS(attribute);	//add this attribute to our map
		}
	}

	/**Removes an attribute by local name and namespace URI. If the removed
		attribute has a default value it is immediately replaced. The
		replacing attribute has the same namespace URI and local name, as
		well as the original prefix.
	@param namespaceURI The namespace URI of the attribute to remove.
	@param localNameThe local name of the attribute to remove.
	@exception DOMException
	<ul>
		<li>NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly.</li>
	</ul>
	@since DOM Level 2
	*/
	public void removeAttributeNS(String namespaceURI, String localName) throws DOMException
	{
		//G***check read-only status here
		if(hasAttributeNS(namespaceURI, localName))  //if the attribute exists (needed because this function in the DOM throws now not-found exception)
			getAttributeXMLNamedNodeMap().removeNamedItemNS(namespaceURI, localName); //remove any attribute with this name, if there is one
		//G***check to see if there's a default value that we should add
	}

	/**Retrieves an <code>Attr</code> node by local name and namespace URI.
	@param namespaceURI The namespace URI of the attribute to retrieve.
	@param localName The local name of the attribute to retrieve.
	@return The <code>Attr</code> node with the specified attribute local
		name and namespace URI or <code>null</code> if there is no such attribute.
	@since DOM Level 2
	*/
	public Attr getAttributeNodeNS(String namespaceURI, String localName)
	{
		if(hasAttributeNS(namespaceURI, localName))	//if an attribute with this name exists
		{
			return (XMLAttribute)getAttributeXMLNamedNodeMap().getNamedItemNS(namespaceURI, localName);	//return a reference to the attribute
		}
		else	//if this attribute doesn't exist
			return null;	//return null
	}

	/**Adds a new attribute. If an attribute with that local name and that
		namespace URI is already present in the element, it is replaced by
		the new one.
	@param newAttr The <code>Attr</code> node to add to the attribute list.
	@return If the <code>newAttr</code> attribute replaces an existing
		attribute with the same local name and namespace URI, the replaced
		<code>Attr</code> node is returned, otherwise <code>null</code> is
		returned.
	@exception DOMException
	<ul>
		<li>WRONG_DOCUMENT_ERR: Raised if <code>newAttr</code> was created from a
		  different document than the one that created the element.</li>
		<li>NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly.</li>
		<li>INUSE_ATTRIBUTE_ERR: Raised if <code>newAttr</code> is already an
		  attribute of another <code>Element</code> object. The DOM user must
		  explicitly clone <code>Attr</code> nodes to re-use them in other
		  elements.</li>
	</ul>
	@since DOM Level 2
	*/
	public Attr setAttributeNodeNS(Attr newAttr) throws DOMException
	{
		//G***make sure this is the correct document
		//G***check for read-only status
		//G***check to see if the attribute is already in use
//G***del Debug.trace("inside setAttributeNodeNS()");
		return (Attr)getAttributeXMLNamedNodeMap().setNamedItemNS(newAttr);	//add this attribute to our map
	}

	/**Returns a <code>NodeList</code> of all the descendant
		<code>Elements</code> with a given local name and namespace URI in
		the order in which they are encountered in a preorder traversal of
		this <code>Element</code> tree.
	@param namespaceURI The namespace URI of the elements to match on. The
		special value "*" matches all namespaces.
	@param localName The local name of the elements to match on. The
		special value "*" matches all local names.
	@return A new <code>NodeList</code> object containing all the matched
		<code>Elements</code>.
	@since DOM Level 2
	*/
	public NodeList getElementsByTagNameNS(String namespaceURI, String localName)
	{
		return getNodesByNameNS(ELEMENT_NODE, namespaceURI, localName, true);	//get the elements that match, showing that we want *all* the child elements at whatever level
	}

	/**Returns <code>true</code> when an attribute with a given name is
		specified on this element or has a default value, <code>false</code>
		otherwise.
	@param name The name of the attribute to look for.
	@return <code>true</code> if an attribute with the given name is
		specified on this element or has a default value, <code>false</code> otherwise.
	@since DOM Level 2
	*/
	public boolean hasAttribute(String name)
	{
		return ((XMLNamedNodeMap)getAttributes()).containsKey(name);  //return whether there's a node in our attribute map with the given name for a key
	}

	/**Returns <code>true</code> when an attribute with a given local name and
		namespace URI is specified on this element or has a default value,
		<code>false</code> otherwise.
	@param namespaceURI The namespace URI of the attribute to look for.
	@param localName The local name of the attribute to look for.
	@return <code>true</code> if an attribute with the given local name
		and namespace URI is specified or has a default value on this
		element, <code>false</code> otherwise.
	@since DOM Level 2
	*/
	public boolean hasAttributeNS(String namespaceURI, String localName)
	{
//G***del Debug.trace("--inside XMLElement.hasAttributeNS, looking for namespace URI: "+namespaceURI+" local name: "+localName);  //G***del
		return ((XMLNamedNodeMap)getAttributes()).containsKeyNS(namespaceURI, localName);  //return whether there's a node in our attribute map with the given namespace URI and local name for a key
	}

	    /**
     *  The type information associated with this element. 
     * @since DOM Level 3
     */
    public TypeInfo getSchemaTypeInfo() {throw new UnsupportedOperationException();}	//TODO fix for DOM 3

    /**
     *  If the parameter <code>isId</code> is <code>true</code>, this method 
     * declares the specified attribute to be a user-determined ID attribute
     * . This affects the value of <code>Attr.isId</code> and the behavior 
     * of <code>Document.getElementById</code>, but does not change any 
     * schema that may be in use, in particular this does not affect the 
     * <code>Attr.schemaTypeInfo</code> of the specified <code>Attr</code> 
     * node. Use the value <code>false</code> for the parameter 
     * <code>isId</code> to undeclare an attribute for being a 
     * user-determined ID attribute. 
     * <br> To specify an attribute by local name and namespace URI, use the 
     * <code>setIdAttributeNS</code> method. 
     * @param name The name of the attribute.
     * @param isId Whether the attribute is a of type ID.
     * @exception DOMException
     *   NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly.
     *   <br>NOT_FOUND_ERR: Raised if the specified node is not an attribute 
     *   of this element.
     * @since DOM Level 3
     */
    public void setIdAttribute(String name, 
                               boolean isId)
                               throws DOMException {throw new UnsupportedOperationException();}	//TODO fix for DOM 3

    /**
     *  If the parameter <code>isId</code> is <code>true</code>, this method 
     * declares the specified attribute to be a user-determined ID attribute
     * . This affects the value of <code>Attr.isId</code> and the behavior 
     * of <code>Document.getElementById</code>, but does not change any 
     * schema that may be in use, in particular this does not affect the 
     * <code>Attr.schemaTypeInfo</code> of the specified <code>Attr</code> 
     * node. Use the value <code>false</code> for the parameter 
     * <code>isId</code> to undeclare an attribute for being a 
     * user-determined ID attribute. 
     * @param namespaceURI The namespace URI of the attribute.
     * @param localName The local name of the attribute.
     * @param isId Whether the attribute is a of type ID.
     * @exception DOMException
     *   NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly.
     *   <br>NOT_FOUND_ERR: Raised if the specified node is not an attribute 
     *   of this element.
     * @since DOM Level 3
     */
    public void setIdAttributeNS(String namespaceURI, 
                                 String localName, 
                                 boolean isId)
                                 throws DOMException {throw new UnsupportedOperationException();}	//TODO fix for DOM 3

    /**
     *  If the parameter <code>isId</code> is <code>true</code>, this method 
     * declares the specified attribute to be a user-determined ID attribute
     * . This affects the value of <code>Attr.isId</code> and the behavior 
     * of <code>Document.getElementById</code>, but does not change any 
     * schema that may be in use, in particular this does not affect the 
     * <code>Attr.schemaTypeInfo</code> of the specified <code>Attr</code> 
     * node. Use the value <code>false</code> for the parameter 
     * <code>isId</code> to undeclare an attribute for being a 
     * user-determined ID attribute. 
     * @param idAttr The attribute node.
     * @param isId Whether the attribute is a of type ID.
     * @exception DOMException
     *   NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly.
     *   <br>NOT_FOUND_ERR: Raised if the specified node is not an attribute 
     *   of this element.
     * @since DOM Level 3
     */
    public void setIdAttributeNode(Attr idAttr, 
                                   boolean isId)
                                   throws DOMException {throw new UnsupportedOperationException();}	//TODO fix for DOM 3


}

