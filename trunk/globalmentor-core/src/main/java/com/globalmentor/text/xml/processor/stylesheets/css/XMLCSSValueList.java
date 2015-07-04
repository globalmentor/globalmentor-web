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

package com.globalmentor.text.xml.processor.stylesheets.css;

import java.io.*;
import java.util.*;

import com.globalmentor.log.Log;

import org.w3c.dom.css.*;

/**
 * An ordered collection of CSS vlaues. An empty list is the same as a list that contains the medium <code>"all"</code>.
 * @author Garret Wilson
 * @since DOM Level 2
 * @see org.w3c.dom.css.CSSValueList
 * @deprecated
 */
public class XMLCSSValueList extends XMLCSSValue implements CSSValueList {

	/** The list of CSS values. */
	private List valueList = new ArrayList();

	/**
	 * Default constructor.
	 * @see CSSValueList
	 */
	public XMLCSSValueList() {
		super(CSS_VALUE_LIST); //construct the parent, specifying that we hold a list of values
	}

	/**
	 * Returns the number of values in the list. The range of valid media list indices is 0 to <code>length-1</code> inclusive.
	 * @return The number of values in the list.
	 */
	public int getLength() {
		return valueList.size();
	}

	/**
	 * Returns the <code>index</code>th CSS rule in the list. The order in this collection represents the order of the values in the CSS style property.
	 * @param index Index into the list.
	 * @return The style rule at the <code>index</code>th position in the list, or <code>null</code> if that is not a valid index.
	 */
	public CSSValue item(int index) {
		try {
			return (CSSValue)valueList.get(index); //return the object at the index
		} catch(IndexOutOfBoundsException e) { //if they don't give a valid index
			return null; //return null instead of throwing an exception
		}
	}

	/**
	 * Returns the parsable textual representation of the current value.
	 * @return The parsable textual representation of the value.
	 * @see XMLValue#getCssText
	 * @version DOM Level 2
	 * @since DOM Level 2
	 */
	public String getCssText() {
		final StringBuilder cssTextStringBuilder = new StringBuilder(); //create a string builder to hold our values
		//TODO fix; for now, we don't know whether to put spaces or commas between the values
		for(int i = 0; i < getLength(); ++i) { //look at each value
			final CSSValue value = item(i); //get the value for this item in the list
			cssTextStringBuilder.append(value.getCssText()); //add the text of this value to the buffer
			if(i < getLength() - 1) //if we're not printing the last value
				cssTextStringBuilder.append(','); //put a space between the value TODO check
		}
		return cssTextStringBuilder.toString(); //return the string of the string buffer we constructed
	}

	//TODO this still does not work for quoted values with spaces -- the tokenizer will break up the quoted string; fix this
	/**
	 * Creates a value list from a value string. TODO decide if we want this is XMLCSSValueList or XMLCSSValue.createValue()
	 * @valueListString The string which contains the parsable CSS value list.
	 * @return The new primitive value, or <code>null</code> if the value string was not parsable.
	 */
	//TODO after figuring out the difference between ident and string, do we really need the property name passed here?
	public static XMLCSSValueList createValueList(String valueListString) {
		final XMLCSSValueList valueList = new XMLCSSValueList(); //create a new list to hold values
		valueListString = valueListString.trim(); //trim the value string of whitespace

		final StreamTokenizer valueTokenizer = new StreamTokenizer(new StringReader(valueListString)); //create a tokenizer to get the items from the list TODO update the delimiter values using constants
		/*TODO fix for numbers
				valueTokenizer.resetSyntax(); //TODO testing; comment
				valueTokenizer.wordChars('a', 'z'); //TODO fix using constants
				valueTokenizer.wordChars('A', 'Z');
				valueTokenizer.wordChars(128 + 32, 255);
				valueTokenizer.wordChars('0', '9');
				valueTokenizer.whitespaceChars(0, ' ');
				valueTokenizer.commentChar('/');
				valueTokenizer.quoteChar('"');
				valueTokenizer.quoteChar('\'');
		*/
		while(true) { //we'll break out of the loop when we reach the end of the file
			try {
				final int token = valueTokenizer.nextToken(); //get the next token
				String valueString = null; //we'll put a value here if it should be added to the list
				switch(token) { //see what type of token this is
					case StreamTokenizer.TT_WORD: //if this is a word
						valueString = valueTokenizer.sval; //store the word value
						break;
					case '"': //if this is a quoted value with a double quote
					case '\'': //if this is a quoted value with a single quote
						valueString = (char)token + valueTokenizer.sval + (char)token; //store the string value, adding the quotes back so that it can properly be stored as a string
						break;
					//TODO what about numbers?
					case StreamTokenizer.TT_EOF: //if this is the end of the file
						return valueList; //stop processing the list and return it
				}
				if(valueString != null) { //if we tokenized a value
					final XMLCSSPrimitiveValue value = XMLCSSPrimitiveValue.createPrimitiveValue(valueString); //create a new primitive value with the contents of this value string
					valueList.valueList.add(value); //add this value to the list
				}
			} catch(IOException ioException) { //if we have a problem reading from the string
				Log.warn(ioException); //warn than an error has occurred
				return valueList; //stop processing the list and return it
			}
		}

		//TODO fix: this next line will *not* work with quotes and commas, such as font-family: Arial, "Times New Roman"
		/*TODO fix
				final StringTokenizer valueTokenizer=new StringTokenizer(valueListString, WHITESPACE_CHARS+",");	//create a tokenizer to get the items from the list TODO use constants here, and decide whether commas should be present, for example
				while(valueTokenizer.hasMoreTokens()) {	//while there are more tokens
					final String valueString=valueTokenizer.nextToken();	//get the next token
		//TODO fix; perhaps parse the characters in the list and then have a separate routine to check the values, based upon the property name			if(valueString.equals(CSS_LIST_NONE))	//if this
					final XMLCSSPrimitiveValue value=XMLCSSPrimitiveValue.createPrimitiveValue(valueString);	//create a new primitive value with the contents of this value string
					valueList.valueList.add(value);	//add this value to the list
				}
		*/
	}

	//TODO all this will probably be replaced by lex-like parsing

	/**
	 * TODO comment; replace with better lex-like parsing
	 * @valueListString The string which contains the parsable CSS value list.
	 * @return The new primitive value, or <code>null</code> if the value string was not parsable.
	 */
	/*TODO fix
	//TODO after figuring out the difference between ident and string, do we really need the property name passed here?
		public static XMLCSSValueList createValueList(String valueListString)
		{
			final XMLCSSValueList valueList=new XMLCSSValueList();	//create a new list to hold values
			valueListString=valueListString.trim();	//trim the value string of whitespace
				//TODO fix: this next line will *not* work with quotes and commas, such as font-family: Arial, "Times New Roman"
			final StringTokenizer valueTokenizer=new StringTokenizer(valueListString, " ,");	//create a tokenizer to get the items from the list TODO use constants here, and decide whether commas should be present, for example
			while(valueTokenizer.hasMoreTokens()) {	//while there are more tokens
				final String valueString=valueTokenizer.nextToken();	//get the next token
	//TODO fix; perhaps parse the characters in the list and then have a separate routine to check the values, based upon the property name			if(valueString.equals(CSS_LIST_NONE))	//if this
				final XMLCSSPrimitiveValue value=XMLCSSPrimitiveValue.createPrimitiveValue(valueString);	//create a new primitive value with the contents of this value string
				valueList.valueList.add(value);	//add this value to the list
			}
			return valueList;	//return the list of values we created
		}
	*/

}