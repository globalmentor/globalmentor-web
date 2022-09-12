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

import org.w3c.dom.DOMException;

/**
 * An abstract class representing a node which contain character data.
 * @author Garret Wilson
 * @see XMLNode
 * @see XMLText
 * @see XMLComment
 * @see XMLCDATASection
 * @see org.w3c.dom.CharacterData
 * @deprecated
 */
@Deprecated
public abstract class XMLCharacterData extends XMLNode implements org.w3c.dom.CharacterData {

	/**
	 * Constructor which requires an owner document to be specified.
	 * @param nodeType The type of node this is.
	 * @param ownerDocument The document which owns this node.
	 */
	public XMLCharacterData(final short nodeType, final XMLDocument ownerDocument) {
		super(nodeType, ownerDocument); //construct the parent class
	}

	/**
	 * Constructor that specifies the character content.
	 * @param nodeType The type of node this is.
	 * @param ownerDocument The document which owns this node.
	 * @param characterData The character data for the node.
	 */
	XMLCharacterData(final short nodeType, final XMLDocument ownerDocument, final String characterData) {
		this(nodeType, ownerDocument); //do the default constructing
		setData(characterData); //set the character data
	}

	/** The character data of this node. */
	private String Value = "";

	@Override
	public String getNodeValue() throws DOMException {
		return Value;
	}

	@Override
	public void setNodeValue(String nodeValue) throws DOMException {
		Value = nodeValue;
	}

	@Override
	public String getData() throws DOMException {
		return getNodeValue(); //return the character data
	}

	@Override
	public void setData(String data) throws DOMException {
		//TODO check about this readonly stuff
		setNodeValue(data);
	}

	@Override
	public int getLength() {
		return getData().length(); //return the size of our data string
	}

	@Override
	public String substringData(int offset, int count) throws DOMException {
		if(offset < 0 || offset > getLength() || count < 0) //if the offset is out of bounds or the count is negative
			throw new XMLDOMException(DOMException.INDEX_SIZE_ERR, new Object[] {Integer.valueOf(offset)}); //throw an exception
		final int endIndex = Math.min(offset + count, getLength()); //don't let the ending index be too high (this is for the DOM, which wants us to automatically compensate instead of throwing an exception, as Java does)
		return getData().substring(offset, endIndex); //return the substring section
	}

	@Override
	public void appendData(String arg) throws DOMException {
		setData(getData() + arg); //add the specified character data to our character data, and change it (this will check for readonly status)
	}

	@Override
	public void insertData(int offset, String arg) throws DOMException {
		if(offset < 0 || offset > getLength()) //if the offset is out of bounds
			throw new XMLDOMException(DOMException.INDEX_SIZE_ERR, new Object[] {Integer.valueOf(offset)}); //throw an exception
		setData(substringData(0, offset) + arg + substringData(offset, getLength() - offset)); //construct and set the new data, which will check for readonly status
	}

	@Override
	public void deleteData(int offset, int count) throws DOMException {
		if(offset < 0 || offset > getLength() || count < 0) //if the offset is out of bounds or the count is negative
			throw new XMLDOMException(DOMException.INDEX_SIZE_ERR, new Object[] {Integer.valueOf(offset)}); //throw an exception
		final int secondIndex = Math.min(offset + count, getLength()); //find the start of the second substring, claiming the entire rest of the string if count is too high
		setData(substringData(0, offset) + substringData(secondIndex, getLength() - secondIndex)); //construct the new data without the substring, and set the new data, which will check for readonly status
	}

	@Override
	public void replaceData(int offset, int count, String arg) throws DOMException {
		if(offset < 0 || offset > getLength() || count < 0) //if the offset is out of bounds or the count is negative
			throw new XMLDOMException(DOMException.INDEX_SIZE_ERR, new Object[] {Integer.valueOf(offset)}); //throw an exception
		final int secondIndex = Math.min(offset + count, getLength()); //find the start of the second substring, claiming the entire rest of the string if count is too high
		setData(substringData(0, offset) + arg + substringData(secondIndex, getLength() - secondIndex)); //construct and set the new data, which will check for readonly status
	}

}
