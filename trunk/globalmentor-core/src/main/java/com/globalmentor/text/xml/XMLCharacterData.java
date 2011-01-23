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

import org.w3c.dom.DOMException;

/**An abstract class representing a node which contain character data.
@author Garret Wilson
@see XMLNode
@see XMLText
@see XMLComment
@see XMLCDATASection
@see org.w3c.dom.CharacterData
@deprecated
*/
public abstract class XMLCharacterData extends XMLNode implements org.w3c.dom.CharacterData
{
	/*Constructor which requires an owner document to be specified.
	@param nodeType The type of node this is.
	@param ownerDocument The document which owns this node.
	*/
	public XMLCharacterData(final short nodeType, final XMLDocument ownerDocument)
	{
		super(nodeType, ownerDocument);	//construct the parent class
	}

	/**Constructor that specifies the character content.
	@param nodeType The type of node this is.
	@param ownerDocument The document which owns this node.
	@param characterData The character data for the node.
	*/
	XMLCharacterData(final short nodeType, final XMLDocument ownerDocument, final String characterData)
	{
		this(nodeType, ownerDocument);	//do the default constructing
		setData(characterData);	//set the character data
	}

	/**The character data of this node.*/
	private String Value="";

		/**Returns the character data of this node.
		@return The character data of this node.
		@version DOM Level 1
		*/
		public String getNodeValue() throws DOMException {return Value;}

		/**Sets the character data of the attribute.
		@param nodeValue The new character data for the attribute.
		@version DOM Level 1
		*/
		public void setNodeValue(String nodeValue) throws DOMException {Value=nodeValue;}

	/**Returns the character data of this node.
	@exception DOMException
	<ul>
		<li>NO_MODIFICATION_ALLOWED_ERR: Raised when the node is readonly.<li>
		<li>DOMSTRING_SIZE_ERR: Raised when the number of characters returned would
			not fit in the returned string. Not used in this implementation.</li>
	</ul>
	@return The character data of this node.
	@see XMLNode#getNodeValue
	@version DOM Level 1
	*/
	public String getData() throws DOMException
	{
		return getNodeValue();	//return the character data
	}

	/**Sets the character data of this node.
	@param data The new character data for this node.
	<ul>
		<li>NO_MODIFICATION_ALLOWED_ERR: Raised when the node is readonly.<li>
		<li>DOMSTRING_SIZE_ERR: Raised when the number of characters returned would
			not fit in the returned string. Not used in this implementation.</li>
	</ul>
	@see XMLNode#setNodeValue
	@version DOM Level 1
	*/
	public void setData(String data) throws DOMException
	{
		//TODO check about this readonly stuff
		setNodeValue(data);
	}

	/**Returns the number of 16-bit units that are available (the length of the data).
	@return The number of characters in the character data of this node.
	@version DOM Level 1
	*/
	public int getLength()
	{
		return getData().length();	//return the size of our data string
	}

	/**Extracts a range of data from the node.
	@param offset Start offset of substring to extract.
	@param count The number of 16-bit units to extract.
	@return The specified substring. If the sum of <code>offset</code> and
	<code>count</code> exceeds the <code>length</code>, then all 16-bit
	units to the end of the data are returned.
	@exception DOMException
	<ul>
		<li>INDEX_SIZE_ERR: Raised if the specified <code>offset</code> is
			negative or greater than the number of 16-bit units in this node's
			character data, or if the specified <code>count</code> is negative.</li>
		<li>DOMSTRING_SIZE_ERR: Raised if the specified range of text does not
			fit into a <code>DOMString</code> (not used in this implementation).</li>
	</ul>
	@version DOM Level 1
	*/
	public String substringData(int offset, int count) throws DOMException
	{
		if(offset<0 || offset>getLength() || count<0)	//if the offset is out of bounds or the count is negative
			throw new XMLDOMException(DOMException.INDEX_SIZE_ERR, new Object[]{new Integer(offset)});	//throw an exception
		final int endIndex=Math.min(offset+count, getLength());	//don't let the ending index be too high (this is for the DOM, which wants us to automatically compensate instead of throwing an exception, as Java does)
		return getData().substring(offset, endIndex);	//return the substring section
	}

	/**Appends the specified string to the end of this node's character data.
	@param arg The character data to append.
	@exception DOMException
	<ul>
		<li>NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly.</li>
	</ul>
	@version DOM Level 1
	*/
	public void appendData(String arg) throws DOMException
	{
		setData(getData()+arg);	//add the specified character data to our character data, and change it (this will check for readonly status)
	}

	/**Inserts character data at the specified offset.
	@param offset The character offset at which to insert.
	@param arg The character data to insert.
	@exception DOMException
	<ul>
		<li>INDEX_SIZE_ERR: Raised if the specified <code>offset</code> is
			negative or greater than the number of 16-bit units in this node's
			character data.</li>
		<li>NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly.</li>
	</ul>
	@version DOM Level 1
	*/
	public void insertData(int offset, String arg) throws DOMException
	{
		if(offset<0 || offset>getLength())	//if the offset is out of bounds
			throw new XMLDOMException(DOMException.INDEX_SIZE_ERR, new Object[]{new Integer(offset)});	//throw an exception
		setData(substringData(0, offset)+arg+substringData(offset, getLength()-offset));	//construct and set the new data, which will check for readonly status
	}

	/**Removes a range of 16-bit units from the node.
	@param offset The offset from which to start removing.
	@param count The number of 16-bit units to delete. If the sum of
	<code>offset</code> and <code>count</code> exceeds <code>length</code>
	then all 16-bit units from <code>offset</code> to the end of the data
	are deleted.
	@exception DOMException
	<ul>
		<li>INDEX_SIZE_ERR: Raised if the specified <code>offset</code> is
			negative or greater than the number of 16-bit units in the node's
			character data, or if the specified <code>count</code> is negative.</li>
		<li>NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly.</li>
	</ul>
	@version DOM Level 1
	*/
	public void deleteData(int offset, int count) throws DOMException
	{
		if(offset<0 || offset>getLength() || count<0)	//if the offset is out of bounds or the count is negative
			throw new XMLDOMException(DOMException.INDEX_SIZE_ERR, new Object[]{new Integer(offset)});	//throw an exception
		final int secondIndex=Math.min(offset+count, getLength());	//find the start of the second substring, claiming the entire rest of the string if count is too high
		setData(substringData(0, offset)+substringData(secondIndex, getLength()-secondIndex));	//construct the new data without the substring, and set the new data, which will check for readonly status
	}

	/**Replaces the characters starting at the specified 16-bit unit offset with
	the specified string.
	@param offset The offset from which to start replacing.
	@param count The number of 16-bit units to replace. If the sum of
		<code>offset</code> and <code>count</code> exceeds the number of characters,
		then all 16-bit units to the end of the data are replaced (i.e., the
		effect is the same as a remove method call with the same
		range, followed by an append method invocation).
	@param arg The character data with which the range must be replaced.
	@exception DOMException
	<ul>
		<li>INDEX_SIZE_ERR: Raised if the specified <code>offset</code> is
			negative or greater than the number of 16-bit units in the node's
			character data, or if the specified <code>count</code> is negative.</li>
		<li>NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly.</li>
	</ul>
	@version DOM Level 1
	*/
	public void replaceData(int offset, int count, String arg) throws DOMException
	{
		if(offset<0 || offset>getLength() || count<0)	//if the offset is out of bounds or the count is negative
			throw new XMLDOMException(DOMException.INDEX_SIZE_ERR, new Object[]{new Integer(offset)});	//throw an exception
		final int secondIndex=Math.min(offset+count, getLength());	//find the start of the second substring, claiming the entire rest of the string if count is too high
		setData(substringData(0, offset)+arg+substringData(secondIndex, getLength()-secondIndex));	//construct and set the new data, which will check for readonly status
	}

}

