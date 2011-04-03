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

package com.globalmentor.text.xml.stylesheets.css;

import java.io.*;

import java.net.URI;
import static com.globalmentor.text.xml.stylesheets.css.XMLCSS.*;

import com.globalmentor.io.*;
import com.globalmentor.text.xml.*;

import org.w3c.dom.*;
import org.w3c.dom.stylesheets.StyleSheet;
import org.w3c.dom.css.*;

/**Class which processes an XML document and parses and applies stylesheets.
@author Garret Wilson
@deprecated
*/
public class XMLCSSProcessor implements URIInputStreamable
{

	/**The interface to use to locate external files. This can be this class or another
		class, depending on which constructor is used.
	@see XMLCSSProcessor#XMLCSSProcessor
	*/
	private URIInputStreamable uriInputStreamable=null;

		/**@return The interface to use to locate external files. This can be this class
			or another class, depending on which constructor has been used.
		@see XMLCSSProcessor#XMLCSSProcessor
		*/
		public URIInputStreamable getURIInputStreamable() {return uriInputStreamable;}

		/**Sets the interface to use to locate external files. This can be this class
			or another class, depending on which constructor has been used.
		@param newURIInputStreamable A class implementing the interface that can find files
			and return an <code>InputStream</code> to them.
		@see XMLCSSProcessor#XMLCSSProcessor
		*/
		protected void setURIInputStreamable(final URIInputStreamable newURIInputStreamable) {uriInputStreamable=newURIInputStreamable;}

	/**Default constructor. Sets the input stream locator to this class so that we can find
		our own files.*/
	public XMLCSSProcessor()	//TODO probably make a separate XMLStyleSheetProcessor or something
	{
		setURIInputStreamable(this);	//show that we'll locate our own files
	}

	/**Constructor that sets the interface to use to locate external files.
	@param newURIInputStreamable A class implementing the interface that can find files and
		return an <code>InputStream</code> to them.
	*/
	public XMLCSSProcessor(final URIInputStreamable newURIInputStreamable)
	{
		setURIInputStreamable(newURIInputStreamable);	//show which file locator we'll use
	}

	/**Skips all CSS whitespace characters, and returns the number of characters skipped.
	Resets peeking.
	@param reader The reader from which to retrieve characters.
	@except IOException Thrown when an i/o error occurs.
	@except ParseEOFException Thrown when the end of the input stream is reached unexpectedly.
	@return The number of characters skipped.
	*/
	public static long skipWhitespaceCharacters(final ParseReader reader) throws IOException, ParseEOFException
	{
		return reader.skipChars(WHITESPACE_CHARS);	//skip any whitespace characters and return the number of characters skipped
	}

	/**Skips all CSS whitespace characters, and returns the number of characters skipped.
	No exception is thrown if the end of the file is reached.
	Resets peeking.
	@param reader The reader from which to retrieve characters.
	@except IOException Thrown when an i/o error occurs.
	@return The number of characters skipped.
	*/
	public static long skipWhitespaceCharactersEOF(final ParseReader reader) throws IOException
	{
		return reader.skipCharsEOF(WHITESPACE_CHARS);	//skip any whitespace characters and return the number of characters skipped without throwing an exception if we find the end of the file
	}

	/**Skips all CSS whitespace characters and XML comment starts/stops,
		and returns the number of characters skipped.
	Resets peeking.
	@param reader The reader from which to retrieve characters.
	@except IOException Thrown when an i/o error occurs.
	@except ParseEOFException Thrown when the end of the input stream is reached unexpectedly.
	@return The number of characters skipped.
	*/
/*TODO del if we don't need
	public long skipWhitespaceCharacters(final ParseReader reader) throws IOException, ParseEOFException
	{
		return reader.skipChars(WHITESPACE_CHARS);	//skip any whitespace characters and return the number of characters skipped
	}
*/

	/**Skips all CSS whitespace characters and XML comment starts/stops,
		and returns the number of characters skipped.
	No exception is thrown if the end of the file is reached.
	Resets peeking.
	@param reader The reader from which to retrieve characters.
	@except IOException Thrown when an i/o error occurs.
	@return The number of characters skipped.
	*/
/*TODO del if we don't need
	public long skipWhitespaceCharactersEOF(final ParseReader reader) throws IOException
	{
		return reader.skipCharsEOF(WHITESPACE_CHARS);	//skip any whitespace characters and return the number of characters skipped without throwing an exception if we find the end of the file
	}
*/

	/**Returns an input stream from given URI. This function is used when we're
		acting as our own <code>URIInputStreamable</code>.
	@param uri A complete URI to a file.
	@return An input stream to the contents of the file represented by the given URI.
	@exception IOException Thrown if an I/O error occurred.
	@see #getFileLocator
	*/
	public InputStream getInputStream(final URI uri) throws IOException
	{
		return uri.toURL().openConnection().getInputStream();	//since we don't know any better (they didn't specify their own file locator), try to open a connection to the URI (converted to a URL) directly and return an input stream to that connection
	}

	/**Parses an input stream that supposedly begins with a CSS value. The entire
		value is read and parsed up to whitespace, the declaration separator (;),
		or the end-of-rule-group separator (}). No end-of-file exception is thrown.
		The returned string value will not have been verified; it is assumed that
		this value will itself be parsed later when assigned to a style declaration.
	@param reader The reader from which to retrieve characters.
	@param propertyName The name of the property for which the value is being parsed
//TODO fix all the exception stuff
	@except IOException Thrown when an i/o error occurs.
//TODO fix	@except XMLSyntaxException Thrown when there is a syntax error in the XML file.
//TODO fix	@except XMLWellFormednessException Thrown when there is a well-formedness error in the XML file.
	@except ParseUnexpectedDataException Thrown when an unexpected character is found.
	@except ParseEOFException Thrown when the end of the input stream is reached unexpectedly.
//TODO fix	@except XMLUndefinedEntityReferenceException Thrown if a named entity reference has not been defined.
	@return The value of the CSS property, in string form.
	*/
//TODO throw DOM syntax error exception, maybe
	//TODO the EOF exception is now no longer returned; take away that exception declaration
	protected static String parseValue(final ParseReader reader, final String propertyName) throws IOException,/*TODO fix, XMLSyntaxException, XMLWellFormednessException, */ParseUnexpectedDataException, ParseEOFException//TODO fix, XMLUndefinedEntityReferenceException
	{
//TODO fix		String valueString=reader.readStringUntilCharEOF(WHITESPACE_CHARS+DECLARATION_SEPARATOR_CHAR+RULE_GROUP_END_CHAR);	//get the value of the property, which is followed by whitespace, a divider character, or the end of the group
		String valueString=reader.readStringUntilCharEOF(""+DECLARATION_SEPARATOR_CHAR+RULE_GROUP_END_CHAR);	//get the value of the property, which is followed by a divider character or the end of the group
		final int peekCharEOF=reader.peek();  //look at the next character
		if(peekCharEOF!=-1 && (char)peekCharEOF==DECLARATION_SEPARATOR_CHAR)	//if the next character is the declaration separator
			reader.readExpectedChar(DECLARATION_SEPARATOR_CHAR);	//read the character to get it out of the way
		return valueString; //return the value in string form
//TODO del when works	  return XMLCSSStyleDeclaration.parseValue(propertyName, valueString); //parse the value and return the value object
/*TODO del when works
		XMLCSSValue value=null;	//we'll assign a value to this variable based upon which type of CSS value should be created TODO is this the right way to do this? make sure the identical code in XMLCSSSTyleDeclaration is in sync
		if(propertyName.equals(CSS_PROP_TEXT_DECORATION)	//if this is the text decoration property TODO fix for other types
		   || propertyName.equals(CSS_PROP_FONT_FAMILY))    //if this is a font-family property
		{
			value=XMLCSSValueList.createValueList(valueString);	//create a new list of values
		}
		else
			value=XMLCSSPrimitiveValue.createPrimitiveValue(propertyName, valueString);	//create a new primitive value
		if(value!=null)	//if we were able to construct a valid value object
			return value;	//return the value
		else	//if there was an error processing the value string
return value;	//TODO throw the correct error here
*/
	}

	/**Parses a value list from a parse reader.
	@param reader The reader from which to retrieve characters.
//G**del if not needed	@propertyName The name of the property to which the value will be assigned, or
//TODO del if not needed		<code>null</code> if the property name is not known.
	@valueListString The string which contains the parsable CSS value list.
	@return The new primitive value, or <code>null</code> if the value string was not parsable.
	*/
//TODO after figuring out the difference between ident and string, do we really need the property name passed here?
/*TODO fix
	public static XMLCSSValueList parseValueList(final ParseReader reader)
	{
		final XMLCSSValueList valueList=new XMLCSSValueList();	//create a new list to hold values
		valueListString=valueListString.trim();	//trim the value string of whitespace
			//TODO fix: this next line will *not* work with quotes and commas, such as font-family: Arial, "Times New Roman"
		final StringTokenizer valueTokenizer=new StringTokenizer(valueListString, " ,");	//create a tokenizer to get the items from the list TODO use constants here, and decide whether commas should be present, for example
		while(valueTokenizer.hasMoreTokens())	//while there are more tokens
		{
			final String valueString=valueTokenizer.nextToken();	//get the next token
//TODO fix; perhaps parse the characters in the list and then have a separate routine to check the values, based upon the property name			if(valueString.equals(CSS_LIST_NONE))	//if this
			final XMLCSSPrimitiveValue value=XMLCSSPrimitiveValue.createPrimitiveValue(valueString);	//create a new primitive value with the contents of this value string
			valueList.valueList.add(value);	//add this value to the list
		}
		return valueList;	//return the list of values we created
	}
	*/

	/**Parses an input stream that supposedly begins with a CSS ruleset.
	@param reader The reader from which to retrieve characters.
	@param parentStyleSheet The stylesheet which will hold the selectors and properties encountered.
//TODO fix all the exception stuff
	@except IOException Thrown when an i/o error occurs.
//TODO fix	@except XMLSyntaxException Thrown when there is a syntax error in the XML file.
//TODO fix	@except XMLWellFormednessException Thrown when there is a well-formedness error in the XML file.
	@except ParseUnexpectedDataException Thrown when an unexpected character is found.
	@except ParseEOFException Thrown when the end of the input stream is reached unexpectedly.
//TODO fix	@except XMLUndefinedEntityReferenceException Thrown if a named entity reference has not been defined.
	@return A new XML tag constructed from the reader.
	*/
	protected static XMLCSSStyleRule parseRuleSet(final ParseReader reader, final XMLCSSStyleSheet parentStyleSheet) throws IOException,/*TODO fix, XMLSyntaxException, XMLWellFormednessException, */ParseUnexpectedDataException, ParseEOFException//TODO fix, XMLUndefinedEntityReferenceException
	{
		final XMLCSSStyleRule styleRule=new XMLCSSStyleRule(parentStyleSheet);	//create a new style rule for this stylesheet
		while(true)	//we'll keep reading selectors until we find the block of rules, which, when finished, will return from this function
		{
			skipWhitespaceCharacters(reader);	//skip any whitespace characters
			final String selectorText=reader.readStringUntilChar(""+SELECTOR_SEPARATOR_CHAR+RULE_GROUP_START_CHAR);	//the end of each selector will either be a comma or the start of the block of rules
			styleRule.addSelector(selectorText);	//add this selector to the style, which will do its own parsing TODO check for an exception being thrown here, and return our own exception
			if(reader.peekChar()==RULE_GROUP_START_CHAR)	//if the next character to be read is the start of the group of rules
			{
				reader.readExpectedChar(RULE_GROUP_START_CHAR);	//read the start of this group
				  //parse this group of rules; if we hit the end of the stream, reading the end-of-style-rule character will throw the correct error
				parseRuleSet(reader, (XMLCSSStyleDeclaration)styleRule.getStyle()); //parse the group of rules and add the rules to our style
/*TODO del when works
				do	//keep reading declarations until there aren't any more
				{
					skipWhitespaceCharacters(reader);	//skip any whitespace characters
					final String propertyName=reader.readStringUntilChar(WHITESPACE_CHARS+PROPERTY_DIVIDER_CHAR);	//get the name of the property, which is followed by whitespace or a divider character

					skipWhitespaceCharacters(reader);	//skip any whitespace characters
					reader.readExpectedChar(PROPERTY_DIVIDER_CHAR);	//reade the divider between the property name and its value
					skipWhitespaceCharacters(reader);	//skip any whitespace characters

					final XMLCSSValue propertyValue=parseValue(reader, propertyName);	//parse the value, showing which property the value is for
					skipWhitespaceCharacters(reader);	//skip any whitespace characters
					//TODO parse the priority somewhere here
					((XMLCSSStyleDeclaration)styleRule.getStyle()).setPropertyCSSValue(propertyName, propertyValue);	//TODO set this property
					skipWhitespaceCharacters(reader);	//skip any whitespace characters
				}
				while(reader.peekChar()!=RULE_GROUP_END_CHAR);	//keep reading rules until we've reached the end of the group
*/
				reader.readExpectedChar(RULE_GROUP_END_CHAR);	//read the end of the group
				return styleRule;	//return the style rule we constructed
			}
			else	//if this isn't the start of a group of rules, so this must have just been the separator between selectors
				reader.readExpectedChar(SELECTOR_SEPARATOR_CHAR);	//read the separator character and go on reading selectors
		}
	}

	/**Parses a CSS style from a string.
	@param styleValue The set of rules to parse.
	//TODO fix all the exception stuff
	@except IOException Thrown when an i/o error occurs.
	//TODO fix	@except XMLSyntaxException Thrown when there is a syntax error in the XML file.
	//TODO fix	@except XMLWellFormednessException Thrown when there is a well-formedness error in the XML file.
	@except ParseUnexpectedDataException Thrown when an unexpected character is found.
	@except ParseEOFException Thrown when the end of the input stream is reached unexpectedly.
	//TODO fix	@except XMLUndefinedEntityReferenceException Thrown if a named entity reference has not been defined.
	@return <code>true</code> if the end-of-rule-group character was found,
		<code>false</code> if the end of the stream was reached.
	*/
	public static CSSStyleDeclaration parseRuleSet(final String styleValue) throws IOException,/*TODO fix, XMLSyntaxException, XMLWellFormednessException, */ParseUnexpectedDataException, ParseEOFException//TODO fix, XMLUndefinedEntityReferenceException
	{
		final XMLCSSStyleDeclaration style=new XMLCSSStyleDeclaration(); //create a new style declaration
		final ParseReader styleReader=new ParseReader(styleValue);	//create a string reader from the value of this style information
		parseRuleSet(styleReader, style); //read the style into our style declaration
		return style;	//return the parsed style			
	}	
	
	/**Parses an input stream that supposedly begins with a CSS style,
		a group of rules with attribute names and values.
	@param reader The reader from which to retrieve characters.
	@param styleDeclaration The style declaration which will hold the style declaration elements.
	@param endChar
//TODO fix all the exception stuff
	@except IOException Thrown when an i/o error occurs.
//TODO fix	@except XMLSyntaxException Thrown when there is a syntax error in the XML file.
//TODO fix	@except XMLWellFormednessException Thrown when there is a well-formedness error in the XML file.
	@except ParseUnexpectedDataException Thrown when an unexpected character is found.
	@except ParseEOFException Thrown when the end of the input stream is reached unexpectedly.
//TODO fix	@except XMLUndefinedEntityReferenceException Thrown if a named entity reference has not been defined.
	@return <code>true</code> if the end-of-rule-group character was found,
		<code>false</code> if the end of the stream was reached.
	*/
	public static boolean parseRuleSet(final ParseReader reader, final XMLCSSStyleDeclaration styleDeclaration) throws IOException,/*TODO fix, XMLSyntaxException, XMLWellFormednessException, */ParseUnexpectedDataException, ParseEOFException//TODO fix, XMLUndefinedEntityReferenceException
	{
		do	//keep reading declarations until there aren't any more
		{
			skipWhitespaceCharactersEOF(reader);	//skip over any whitespace characters, but do not throw an error for the end of the stream
			if(reader.isEnd())	//if we've hit the end of the stream of characters
				return false;	//show that we hit the end of the stream without finding the end-of-rule-group character
			skipWhitespaceCharacters(reader);	//skip any whitespace characters
			if((char)reader.peek()==RULE_GROUP_END_CHAR)  //if we're at the end of the rule group (which will happen if the rule group is empty, for instance) TODO what if this is just an empty string being passed, from an XHTML style="" attribute?
				return true;  //show that we hit the end of the rule group TODO this check is done at the end of this loop, too -- is there a place we can combine both checks?
			
					//TODO fix CSS comment hack
			final String propertyName=reader.readStringUntilChar(WHITESPACE_CHARS+PROPERTY_DIVIDER_CHAR+RULE_GROUP_END_CHAR+'/');	//get the name of the property, which is followed by whitespace or a divider character

			skipWhitespaceCharacters(reader);	//skip any whitespace characters
			
			if((char)reader.peek()=='/')
			{
				reader.readExpectedString(COMMENT_START);
				while(!reader.isEnd())  //TODO fix parsing comments; this is a hack
				{
					reader.readStringUntilChar('*');  //TODO fix parsing comments; this is a hack
					reader.readExpectedChar('*'); //TODO fix parsing comments; this is a hack
					if(reader.peekChar()=='/')  //TODO fix parsing comments; this is a hack
					{
						reader.readExpectedChar('/');
						break;
					}
				}
			}
			else
			{			
			
				reader.readExpectedChar(PROPERTY_DIVIDER_CHAR);	//reade the divider between the property name and its value
				skipWhitespaceCharacters(reader);	//skip any whitespace characters
	
			  final String propertyValueString=parseValue(reader, propertyName); //parse the value, showing which property the value is for
				skipWhitespaceCharactersEOF(reader);	//skip over any whitespace characters, but do not throw an error for the end of the stream
				//TODO parse the priority somewhere here
			  if(propertyValueString.trim().length()>0) //if there actually is a property value
				{
					styleDeclaration.setProperty(propertyName, propertyValueString);  //set this property TODO check about the DOM exception it throws
				}
	
				skipWhitespaceCharactersEOF(reader);	//skip over any whitespace characters, but do not throw an error for the end of the stream
			}
		}
		while((char)reader.peek()!=RULE_GROUP_END_CHAR);	//keep reading rules until we've reached the end of the group TODO should this be at the beginning? what if it's an empty style block {}?
		return true;  //show that we found the end-of-rule-group character
	}

	/**Parses an input stream that contains stylesheet information.
	@param reader The reader from which to retrieve characters.
	@param parentStyleSheet The stylesheet which will hold the nodes encountered.

//TODO fix all this exception stuff
	@except IOException Thrown when an i/o error occurs.
	@except XMLSyntaxException Thrown when there is a syntax error in the XML file.
	@except XMLWellFormednessException Thrown when there is a well-formedness error in the XML file.
	@except XMLValidityException Thrown when there is a validity error in the XML file.
	@except XMLUndefinedEntityReferenceException Thrown if a named entity reference has not been defined.
	@except ParseUnexpectedDataException Thrown when an unexpected character is found.
	@except ParseEOFException Thrown when the end of the input stream is reached unexpectedly.
//TODO fix	@return A new XML object constructed from the markup.
	*/
	protected void parseStyleSheetContent(final ParseReader reader, final XMLCSSStyleSheet parentStyleSheet) throws IOException, /*TODO fix XMLSyntaxException, XMLWellFormednessException, XMLValidityException, XMLUndefinedEntityReferenceException,*/ ParseUnexpectedDataException, ParseEOFException
	{
		//the stylesheet strings we expect; make sure we put the AT_RULE_START after the other at-rule constants, because it represents an unknown at-rule
		final String[] EXPECTED_STYLESHEET_STRINGS={MEDIA_RULE_SYMBOL, PAGE_RULE_SYMBOL, FONT_FACE_RULE_SYMBOL, AT_RULE_START, CDO, CDC, COMMENT_START, ""};
		//the indexes of the stylesheet strings in our array
		final int MEDIA_RULE=0, PAGE_RULE=1, FONT_FACE_RULE=2, UNKNOWN_AT_RULE=3, XML_COMMENT_START=4, XML_COMMENT_END=5, COMMENT=6;
		while(true)	//we'll keep processing rulesets and such until we run out of characters looking for whitespace
		{
			skipWhitespaceCharactersEOF(reader);	//skip over any whitespace
			if(reader.isEnd())	//if we've hit the end of the stream of characters
				return;	//we're finished processing the stylesheet
			reader.resetPeek();	//reset peeking so that the next character peeked will reflect the next character to be read

			//TODO add CSS comment parsing as well
			switch(reader.peekExpectedStrings(EXPECTED_STYLESHEET_STRINGS))	//see what we have next in the stylesheet
			{
				case MEDIA_RULE:	//if this is a media rule
				  reader.readStringUntilChar('}');  //TODO fix parsing media rules
					reader.readExpectedChar('}'); //TODO fix parsing media rules
					break;	//TODO fix
				case PAGE_RULE:	//if this is a page rule
				  reader.readStringUntilChar('}');  //TODO fix parsing page rules
					reader.readExpectedChar('}'); //TODO fix parsing page rules
					break;	//TODO fix
				case FONT_FACE_RULE:	//if this is a font face rule
				  reader.readStringUntilChar('}');  //TODO fix parsing font-face rules
					reader.readExpectedChar('}'); //TODO fix parsing font-face rules
					break;	//TODO fix
				case UNKNOWN_AT_RULE:	//if this is an at-rule we don't know about
				  reader.readStringUntilChar('}');  //TODO fix parsing at rules
					reader.readExpectedChar('}'); //TODO fix parsing at rules
					break;	//TODO fix
				case COMMENT:	//if this is a CSS comment
					while(!reader.isEnd())  //TODO fix parsing comments; this is a hack
					{
						reader.readStringUntilChar('*');  //TODO fix parsing comments; this is a hack
						reader.readExpectedChar('*'); //TODO fix parsing comments; this is a hack
						if(reader.peekChar()=='/')  //TODO fix parsing comments; this is a hack
						{
							reader.readExpectedChar('/');
							break;
						}
					}
					break;
				case XML_COMMENT_START:	//if this is the start of an XML-style comment
					reader.readExpectedString(CDO);	//ignore the start of the comment as CSS requires and process what's inside it
					break;
				case XML_COMMENT_END:	//if this is the end of an XML-style comment
					reader.readExpectedString(CDC);	//ignore the comment ending string
					break;
				default:	//if we didn't find any of the above, we'll assume this is a ruleset
					((XMLCSSRuleList)parentStyleSheet.getCssRules()).add(parseRuleSet(reader, parentStyleSheet));	//parse this ruleset and add it to our list of rules
					break;
			}
		}
	}

	/**Parses any internal stylesheets contained in an XML document.
	@param document The XML document that that contains stylesheet information.
//TODO fix all this exception stuff
	@except IOException Thrown when an i/o error occurs.
	@except ParseUnexpectedDataException Thrown when an unexpected character is found.
	@except ParseEOFException Thrown when the end of the input stream is reached unexpectedly.
	*/
	public void parseInternalStyleSheets(final XMLDocument document) throws IOException, ParseUnexpectedDataException, ParseEOFException
	{
/*TODO fix
		final XMLNodeList styleElementList=XPath.evaluateLocationPath(document, )
(XMLNodeList)document.getElementsByTagName("style");	//get a list of all the style elements in the document TODO use a constant here
*/
		//TODO we should probably eventually make sure the styles come at the right place, but an OEB version of this should have been validated against a DTD at some point which ensures this anyway
		final XMLNodeList styleElementList=(XMLNodeList)document.getElementsByTagName("style");	//get a list of all the style elements in the document TODO use a constant here
		for(int styleIndex=0; styleIndex<styleElementList.size(); ++styleIndex)	//look at each of the elements representing a style
		{
			//TODO we should probably check to make sure this is "text/css" or "text/css-oeb" or whatever
			final XMLElement styleElement=(XMLElement)styleElementList.get(styleIndex);	//get the element at this index
			String styleText="";	//we'll eventually have all the text of this style in this string
			for(int styleChildIndex=0; styleChildIndex<((XMLNodeList)styleElement.getChildNodes()).size(); ++styleChildIndex)	//in case they wrapped the style in a comment, we'll get normal text and comment text
			{
				XMLNode styleChildNode=(XMLNode)styleElement.getChildNodes().item(styleChildIndex);	//get a reference to this child node
				if(styleChildNode.getNodeType()==XMLNode.TEXT_NODE)	//if this is a text node
					styleText+=styleChildNode.getText();	//get the text of the node
				else if(styleChildNode.getNodeType()==XMLNode.COMMENT_NODE)	//if this is a comment node
					styleText+=((XMLComment)styleChildNode).getData();	//get the comment data
			}
			//TODO probably have a parseStyleSheetString function or something that does this
			final XMLCSSStyleSheet styleSheet=new XMLCSSStyleSheet(styleElement);	//create a new stylesheet owned by this style element node
			final ParseReader styleSheetReader=new ParseReader(styleText, "Internal Style Sheet");	//create a string reader from the text of this style TODO i18n
//TODO del when works			final ParseReader styleSheetReader=new ParseReader(new StringReader(styleText), "Internal Style Sheet");	//create a string reader from the text of this style TODO i18n
//TODO fix			entityReader.setCurrentLineIndex(entity.getLineIndex());	//pretend we're reading where the entity was located in that file, so any errors will show the correct information
//TODO fix			entityReader.setCurrentCharIndex(entity.getCharIndex());	//pretend we're reading where the entity was located in that file, so any errors will show the correct information
			parseStyleSheetContent(styleSheetReader, styleSheet);	//parse the stylesheet content
			document.getStyleSheetList().add(styleSheet);	//add the stylesheet we created to the document's list of stylesheets
		}
	}

	/**Parses a stylesheet from the contents of a <code>Reader</code> with a given
		owner node.
	@param reader The source from which the information will be read.
	@param sourceObject The source of the data (e.g. a String, File, or URL).
	@param The node that associates this stylesheet with the document.
	@return A new stylesheet contructed from the reader.
	@except IOException Thrown when an i/o error occurs.
	@except ParseUnexpectedDataException Thrown when an unexpected character is found.
	@except ParseEOFException Thrown when the end of the input stream is reached unexpectedly.
	*/
	public CSSStyleSheet parseStyleSheet(final Reader reader, final Object sourceObject, final Node ownerNode) throws IOException, ParseUnexpectedDataException, ParseEOFException
	{
		final XMLCSSStyleSheet styleSheet=new XMLCSSStyleSheet(ownerNode);	//create a new stylesheet owned by the given node
		parse(styleSheet, reader, sourceObject);  //parse the stylesheet
		return styleSheet;  //return the stylesheet
	}

	/**Parses a stylesheet from the contents of a <code>Reader</code> with the
		given parent document.
	@param reader The source from which the information will be read.
	@param sourceObject The source of the data (e.g. a String, File, or URL).
	@param parentStyleSheet The stylesheet that included this stylesheet.
	@return A new stylesheet contructed from the reader.
	@except IOException Thrown when an i/o error occurs.
	@except ParseUnexpectedDataException Thrown when an unexpected character is found.
	@except ParseEOFException Thrown when the end of the input stream is reached unexpectedly.
	*/
	public CSSStyleSheet parseStyleSheet(final Reader reader, final Object sourceObject, final StyleSheet parentStyleSheet) throws IOException, ParseUnexpectedDataException, ParseEOFException
	{
		final XMLCSSStyleSheet styleSheet=new XMLCSSStyleSheet(parentStyleSheet);	//create a new stylesheet with the given parent
		parse(styleSheet, reader, sourceObject);  //parse the stylesheet
		return styleSheet;  //return the stylesheet
	}

	/**Parses a stylesheet from the contents of a <code>Reader</code> with no
		parent document or owner node.
	@param reader The source from which the information will be read.
	@param sourceObject The source of the data (e.g. a String, File, or URL).
	@return A new stylesheet contructed from the reader.
	@except IOException Thrown when an i/o error occurs.
	@except ParseUnexpectedDataException Thrown when an unexpected character is found.
	@except ParseEOFException Thrown when the end of the input stream is reached unexpectedly.
	*/
	public CSSStyleSheet parseStyleSheet(final Reader reader, final Object sourceObject) throws IOException, ParseUnexpectedDataException, ParseEOFException
	{
		final XMLCSSStyleSheet styleSheet=new XMLCSSStyleSheet((StyleSheet)null);	//create a new stylesheet with no parent
		parse(styleSheet, reader, sourceObject);  //parse the stylesheet
		return styleSheet;  //return the stylesheet
	}

	/**Parses a stylesheet from the contents of a string.
	@param styleText The string containing the style information.
	@param name The name of the style information (e.g. "Internal Style Sheet").
	@return A new stylesheet contructed from the string.
	@except IOException Thrown when an i/o error occurs.
	@except ParseUnexpectedDataException Thrown when an unexpected character is found.
	@except ParseEOFException Thrown when the end of the input stream is reached unexpectedly.
	*/
	public CSSStyleSheet parseStyleSheet(final String styleText, final String name) throws IOException, ParseUnexpectedDataException, ParseEOFException
	{
		final ParseReader styleSheetReader=new ParseReader(styleText, name);	//create a parse reader reader to use to read the stylesheet string
		final XMLCSSStyleSheet styleSheet=new XMLCSSStyleSheet((StyleSheet)null);	//create a new stylesheet with no parent
		parseStyleSheetContent(styleSheetReader, styleSheet);	//parse the stylesheet content
		return styleSheet;  //return the stylesheet
	}

	/**Parses a stylesheet from the information in the given <code>Reader</code>
		The reader is left open.
	@param styleSheet The stylesheet already constructed, ready for information.
	@param reader The source of the stylesheet information.
	@param sourceObject The source of the data (e.g. a String, File, or URL).
	@except IOException Thrown when an i/o error occurs.
	@except ParseUnexpectedDataException Thrown when an unexpected character is found.
	@except ParseEOFException Thrown when the end of the input stream is reached unexpectedly.
	*/
	protected void parse(final XMLCSSStyleSheet styleSheet, final Reader reader, final Object sourceObject) throws IOException, ParseUnexpectedDataException, ParseEOFException
	{
		final ParseReader styleSheetReader=new ParseReader(reader, sourceObject);	//create a parse reader reader to use to read the stylesheet
//TODO fix			entityReader.setCurrentLineIndex(entity.getLineIndex());	//pretend we're reading where the entity was located in that file, so any errors will show the correct information
//TODO fix			entityReader.setCurrentCharIndex(entity.getCharIndex());	//pretend we're reading where the entity was located in that file, so any errors will show the correct information
		parseStyleSheetContent(styleSheetReader, styleSheet);	//parse the stylesheet content
	}

}
