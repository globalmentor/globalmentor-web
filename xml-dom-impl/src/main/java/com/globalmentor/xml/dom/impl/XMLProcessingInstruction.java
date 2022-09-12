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

import java.util.*;

import static com.globalmentor.xml.spec.XML.*;

import org.w3c.dom.DOMException;

/**
 * A processing instruction in an XML document.
 * @author Garret Wilson
 * @see org.w3c.dom.ProcessingInstruction
 * @deprecated
 */
@Deprecated
public class XMLProcessingInstruction extends XMLNode implements org.w3c.dom.ProcessingInstruction {

	/**
	 * A map of pseudo attributes and values. These are "pseudo" in the sense that XML does not define attributes for processing instructions as it does for
	 * elements, but many processing instructions may have attribute-like parts.
	 */
	private Map pseudoAttributeMap = new HashMap();

	/**
	 * Returns the value of any pseudo attribute in the form name="value" or name='value' which may be part of the processing instruction data.
	 * @param pseudoAttributeName used to achieve its own pseudo attribute value.
	 * @return The value of the given pseudo attribute, or <code>null</code> if that attribute isn't present in the data of the processing instruction.
	 * @see #getData
	 * @see #setData
	 * @see #getNodeValue
	 * @see #setNodeValue
	 */
	public String getPseudoAttributeValue(final String pseudoAttributeName) {
		return (String)pseudoAttributeMap.get(pseudoAttributeName); //return the attribute value, if its name is present in the map
	}

	/**
	 * Constructor which requires an owner document to be specified.
	 * @param ownerDocument The document which owns this node.
	 */
	public XMLProcessingInstruction(final XMLDocument ownerDocument) {
		super(XMLNode.PROCESSING_INSTRUCTION_NODE, ownerDocument); //construct the parent class
	}

	/**
	 * Constructor that specifies the target of the processing instruction.
	 * @param ownerDocument The document which owns this node.
	 * @param target The target for the processing instruction.
	 */
	public XMLProcessingInstruction(final XMLDocument ownerDocument, final String target) {
		this(ownerDocument); //do the default constructing
		setNodeName(target); //set the name, which holds the target
	}

	/**
	 * Constructor that specifies the content of the comment.
	 * @param ownerDocument The document which owns this node.
	 * @param target The target for the processing instruction.
	 * @param data The data for the processing instruction.
	 */
	public XMLProcessingInstruction(final XMLDocument ownerDocument, final String target, final String data) {
		this(ownerDocument, target); //do the default constructing
		setNodeValue(data); //set the data
	}

	/**
	 * Creates and returns a duplicate copy of this node. The clone has no parent. This function creates a "shallow" clone which does not contain clones of all
	 * child nodes. For the DOM version, see cloneNode().
	 * @return A duplicate copy of this node.
	 * @see XMLNode#cloneXMLNode
	 * @see XMLNode#cloneNode
	 * @see XMLNode#getParentXMLNode
	 */
	@Override
	public Object clone() {
		return new XMLProcessingInstruction(getOwnerXMLDocument(), getTarget(), getData()); //create a new node with the same owner document, target, and value
	}

	@Override
	public String getTarget() {
		return getNodeName();
	}

	/** The character data of this node. */
	private String Value = "";

	@Override
	public String getNodeValue() throws DOMException {
		return Value;
	}

	@Override
	public void setNodeValue(String nodeValue) throws DOMException { //TODO change to use the new getProcessingInstructionPseudoAttributeValues()
		Value = nodeValue; //set the value
		//TODO this stuff may be eventually placed back into XMLProcessor
		pseudoAttributeMap.clear(); //clear all of the keys and values from the map
		final StringTokenizer tokenizer = new StringTokenizer(nodeValue, WHITESPACE_CHARS); //create a tokenizer that separates tokens based on XML whitespace
		while(tokenizer.hasMoreTokens()) { //while there are more tokens
			final String token = tokenizer.nextToken(); //get the next token
			final int equalsIndex = token.indexOf(EQUAL_CHAR); //get the index of the equal sign, if there is one
			if(equalsIndex != -1 && equalsIndex > 0 && token.length() >= equalsIndex + 2) { //if there is an equal sign, it's not the first character, and there's enough room for at least the quotes
				final char quoteChar = token.charAt(equalsIndex + 1); //get the character after the equals sign
				//if the character after the equals sign is one of the quote characters, and it matches the last character in the token
				if((quoteChar == SINGLE_QUOTE_CHAR || quoteChar == DOUBLE_QUOTE_CHAR) && token.charAt(token.length() - 1) == quoteChar) {
					final String pseudoAttributeName = token.substring(0, equalsIndex); //get the name of the attribute
					//TODO this does not correctly interpret embedded entities; does XML require it?
					final String pseudoAttributeValue = token.substring(equalsIndex + 2, token.length() - 1); //get the value of the attribute
					pseudoAttributeMap.put(pseudoAttributeName, pseudoAttributeValue); //put the name and value in the map
				}
			}
		}
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

}
