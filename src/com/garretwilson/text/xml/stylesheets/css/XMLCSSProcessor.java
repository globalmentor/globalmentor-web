package com.garretwilson.text.xml.stylesheets.css;

import java.io.*;

/*G***bring back as needed
import java.util.Vector;
import java.util.Map;
import java.util.HashMap;
import java.net.MalformedURLException;
import java.net.URL;
import org.w3c.dom.DOMException;
*/
import java.net.URL;
import com.garretwilson.io.InputStreamLocator;
import com.garretwilson.io.ParseReader;
import com.garretwilson.io.ParseEOFException;	//G***go through and catch all these and throw CSS exceptions
import com.garretwilson.io.ParseUnexpectedDataException;
//G***del import com.garretwilson.lang.StringManipulator;
import com.garretwilson.net.URLUtilities;
import com.garretwilson.text.xml.*;
import com.garretwilson.text.xml.stylesheets.XMLStyleSheetConstants;
import com.garretwilson.util.Debug;
import org.w3c.dom.*;
import org.w3c.dom.stylesheets.StyleSheet;
import org.w3c.dom.css.*;
import org.w3c.dom.traversal.*;

/**Class which processes an XML document and parses and applies stylesheets.
@author Garret Wilson
@see com.garretwilson.text.xml.XMLProcessor
*/
public class XMLCSSProcessor implements XMLStyleSheetConstants, XMLCSSConstants, InputStreamLocator
{

	/**The interface to use to locate external files. This can be this class or another
		class, depending on which constructor is used.
	@see XMLCSSProcessor#XMLCSSProcessor
	*/
	private InputStreamLocator inputStreamLocator=null;

		/**@return The interface to use to locate external files. This can be this class
			or another class, depending on which constructor has been used.
		@see XMLCSSProcessor#XMLCSSProcessor
		*/
		public InputStreamLocator getInputStreamLocator() {return inputStreamLocator;}

		/**Sets the interface to use to locate external files. This can be this class
			or another class, depending on which constructor has been used.
		@param newInputStreamLocator A class implementing the interface that can find files
			and return an <code>InputStream</code> to them.
		@see XMLCSSProcessor#XMLCSSProcessor
		*/
		protected void setInputStreamLocator(final InputStreamLocator newInputStreamLocator) {inputStreamLocator=newInputStreamLocator;}

	/**Default constructor. Sets the input stream locator to this class so that we can find
		our own files.*/
	public XMLCSSProcessor()	//G***probably make a separate XMLStyleSheetProcessor or something
	{
		setInputStreamLocator(this);	//show that we'll locate our own files
	}

	/**Constructor that sets the interface to use to locate external files.
	@param newInputStreamLocator A class implementing the interface that can find files and
		return an <code>InputStream</code> to them.
	*/
	public XMLCSSProcessor(final InputStreamLocator newInputStreamLocator)
	{
		setInputStreamLocator(newInputStreamLocator);	//show which file locator we'll use
	}

	/**Constroctor that uses an existing <code>XMLProcessor</code> to find out
		where files are located.
	@param xmlProcessor An XML processor which will be used to locate files.
	*/
	public XMLCSSProcessor(final XMLProcessor xmlProcessor)
	{
		this(xmlProcessor.getInputStreamLocator());	//use the XML processor's input stream locator for locating files
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
/*G***del if we don't need
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
/*G***del if we don't need
	public long skipWhitespaceCharactersEOF(final ParseReader reader) throws IOException
	{
		return reader.skipCharsEOF(WHITESPACE_CHARS);	//skip any whitespace characters and return the number of characters skipped without throwing an exception if we find the end of the file
	}
*/

	/**Returns an input stream from given URL. This function is used when we're
		acting as our own <code>InputStreamLocator</code>.
	@param url A complete URL to a file.
	@return An input stream to the contents of the file represented by the given URL.
	@exception IOException Thrown if an I/O error occurred.
	@see #getFileLocator
	*/
	public InputStream getInputStream(final URL url) throws IOException
	{
		return url.openConnection().getInputStream();	//since we don't know any better (they didn't specify their own file locator), try to open a connection to the URL directly and return an input stream to that connection
	}

	/**Parses an input stream that supposedly begins with a CSS value. The entire
		value is read and parsed up to whitespace, the declaration separator (;),
		or the end-of-rule-group separator (}). No end-of-file exception is thrown.
		The returned string value will not have been verified; it is assumed that
		this value will itself be parsed later when assigned to a style declaration.
	@param reader The reader from which to retrieve characters.
	@param propertyName The name of the property for which the value is being parsed
//G***fix all the exception stuff
	@except IOException Thrown when an i/o error occurs.
//G***fix	@except XMLSyntaxException Thrown when there is a syntax error in the XML file.
//G***fix	@except XMLWellFormednessException Thrown when there is a well-formedness error in the XML file.
	@except ParseUnexpectedDataException Thrown when an unexpected character is found.
	@except ParseEOFException Thrown when the end of the input stream is reached unexpectedly.
//G***fix	@except XMLUndefinedEntityReferenceException Thrown if a named entity reference has not been defined.
	@return The value of the CSS property, in string form.
	*/
//G***throw DOM syntax error exception, maybe
	//G***the EOF exception is now no longer returned; take away that exception declaration
	protected static String parseValue(final ParseReader reader, final String propertyName) throws IOException,/*G***fix, XMLSyntaxException, XMLWellFormednessException, */ParseUnexpectedDataException, ParseEOFException//G***fix, XMLUndefinedEntityReferenceException
	{
Debug.trace("Starting to parse value.");	//G***del
//G***fix		String valueString=reader.readStringUntilCharEOF(WHITESPACE_CHARS+DECLARATION_SEPARATOR_CHAR+RULE_GROUP_END_CHAR);	//get the value of the property, which is followed by whitespace, a divider character, or the end of the group
		String valueString=reader.readStringUntilCharEOF(""+DECLARATION_SEPARATOR_CHAR+RULE_GROUP_END_CHAR);	//get the value of the property, which is followed by a divider character or the end of the group
Debug.trace("Value string: ", valueString);	//G***del
		final int peekCharEOF=reader.peek();  //look at the next character
		if(peekCharEOF!=-1 && (char)peekCharEOF==DECLARATION_SEPARATOR_CHAR)	//if the next character is the declaration separator
			reader.readExpectedChar(DECLARATION_SEPARATOR_CHAR);	//read the character to get it out of the way
		return valueString; //return the value in string form
//G***del when works	  return XMLCSSStyleDeclaration.parseValue(propertyName, valueString); //parse the value and return the value object
/*G***del when works
		XMLCSSValue value=null;	//we'll assign a value to this variable based upon which type of CSS value should be created G***is this the right way to do this? make sure the identical code in XMLCSSSTyleDeclaration is in sync
		if(propertyName.equals(CSS_PROP_TEXT_DECORATION)	//if this is the text decoration property G***fix for other types
		   || propertyName.equals(CSS_PROP_FONT_FAMILY))    //if this is a font-family property
		{
			value=XMLCSSValueList.createValueList(valueString);	//create a new list of values
		}
		else
			value=XMLCSSPrimitiveValue.createPrimitiveValue(propertyName, valueString);	//create a new primitive value
		if(value!=null)	//if we were able to construct a valid value object
			return value;	//return the value
		else	//if there was an error processing the value string
return value;	//G***throw the correct error here
*/
	}

	/**Parses a CSS value in the form of a string and converts it to a CSS value
		object.
	@param propertyName The name of the CSS property. See the CSS property index.
	@param valueString The new value of the property.
//G***fix	@param priority The new priority of the property (e.g. "important").
//G***fix, decide which to use	@exception DOMException
	<ul>
		<li>SYNTAX_ERR: Raised if the specified value has a syntax error and is unparsable.</li>
//G***del		<li>NO_MODIFICATION_ALLOWED_ERR: Raised if this declaration is readonly.</li>
	</ul>
	*/
/*G**del
	public static XMLCSSValue parseValue(final String propertyName, final String valueString) //G***fix throws DOMException
	{
		XMLCSSValue value=null;	//we'll assign a value to this variable based upon which type of CSS value should be created G***is this the right way to do this? make sure the identical code in XMLCSSSTyleDeclaration is in sync
		if(propertyName.equals(CSS_PROP_TEXT_DECORATION)	//if this is the text decoration property G***fix for other types
		   || propertyName.equals(CSS_PROP_FONT_FAMILY))    //if this is a font-family property
		{
			value=XMLCSSValueList.createValueList(valueString);	//create a new list of values
		}
		else
			value=XMLCSSPrimitiveValue.createPrimitiveValue(propertyName, valueString);	//create a new primitive value
		if(value!=null)	//if we were able to construct a valid value object
			return value;	//return the value
		else	//if there was an error processing the value string
return value;	//G***throw the correct error here
	}
*/

	/**Parses a value list from a parse reader.
	@param reader The reader from which to retrieve characters.
//G**del if not needed	@propertyName The name of the property to which the value will be assigned, or
//G***del if not needed		<code>null</code> if the property name is not known.
	@valueListString The string which contains the parsable CSS value list.
	@return The new primitive value, or <code>null</code> if the value string was not parsable.
	*/
//G***after figuring out the difference between ident and string, do we really need the property name passed here?
/*G***fix
	public static XMLCSSValueList parseValueList(final ParseReader reader)
	{
		final XMLCSSValueList valueList=new XMLCSSValueList();	//create a new list to hold values
		valueListString=valueListString.trim();	//trim the value string of whitespace
			//G***fix: this next line will *not* work with quotes and commas, such as font-family: Arial, "Times New Roman"
		final StringTokenizer valueTokenizer=new StringTokenizer(valueListString, " ,");	//create a tokenizer to get the items from the list G***use constants here, and decide whether commas should be present, for example
		while(valueTokenizer.hasMoreTokens())	//while there are more tokens
		{
			final String valueString=valueTokenizer.nextToken();	//get the next token
//G***fix; perhaps parse the characters in the list and then have a separate routine to check the values, based upon the property name			if(valueString.equals(CSS_LIST_NONE))	//if this
			final XMLCSSPrimitiveValue value=XMLCSSPrimitiveValue.createPrimitiveValue(valueString);	//create a new primitive value with the contents of this value string
//G***del Debug.assert(value!=null, "A null value when creating a CSSValueList.");	//G***fix
			valueList.valueList.add(value);	//add this value to the list
//G***del Debug.notify("XMLCSSValueList.createValueList() just added a value: "+value.getCssText());	//G***del
		}
		return valueList;	//return the list of values we created
	}
	*/


	/**Parses an input stream that supposedly begins with a CSS ruleset.
	@param reader The reader from which to retrieve characters.
	@param parentStyleSheet The stylesheet which will hold the selectors and properties encountered.
//G***fix all the exception stuff
	@except IOException Thrown when an i/o error occurs.
//G***fix	@except XMLSyntaxException Thrown when there is a syntax error in the XML file.
//G***fix	@except XMLWellFormednessException Thrown when there is a well-formedness error in the XML file.
	@except ParseUnexpectedDataException Thrown when an unexpected character is found.
	@except ParseEOFException Thrown when the end of the input stream is reached unexpectedly.
//G***fix	@except XMLUndefinedEntityReferenceException Thrown if a named entity reference has not been defined.
	@return A new XML tag constructed from the reader.
	*/
	protected static XMLCSSStyleRule parseRuleSet(final ParseReader reader, final XMLCSSStyleSheet parentStyleSheet) throws IOException,/*G***fix, XMLSyntaxException, XMLWellFormednessException, */ParseUnexpectedDataException, ParseEOFException//G***fix, XMLUndefinedEntityReferenceException
	{
		final XMLCSSStyleRule styleRule=new XMLCSSStyleRule(parentStyleSheet);	//create a new style rule for this stylesheet
		while(true)	//we'll keep reading selectors until we find the block of rules, which, when finished, will return from this function
		{
			skipWhitespaceCharacters(reader);	//skip any whitespace characters
			final String selectorText=reader.readStringUntilChar(""+SELECTOR_SEPARATOR_CHAR+RULE_GROUP_START_CHAR);	//the end of each selector will either be a comma or the start of the block of rules
			styleRule.addSelector(selectorText);	//add this selector to the style, which will do its own parsing G***check for an exception being thrown here, and return our own exception
			if(reader.peekChar()==RULE_GROUP_START_CHAR)	//if the next character to be read is the start of the group of rules
			{
				reader.readExpectedChar(RULE_GROUP_START_CHAR);	//read the start of this group
				  //parse this group of rules; if we hit the end of the stream, reading the end-of-style-rule character will throw the correct error
				parseRuleSet(reader, (XMLCSSStyleDeclaration)styleRule.getStyle()); //parse the group of rules and add the rules to our style
//G***del					throw new XMLWellFormednessException(XMLWellFormednessException.EOF, new Object[]{ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME).getString(RESOURCE_PREFIX+DOCUMENT_TYPE_RESOURCE), documentType.getName()}, reader.getLineIndex(), reader.getCharIndex(), reader.getName());	//show that we couldn't find the end of the document type

/*G***del when works
				do	//keep reading declarations until there aren't any more
				{
//G***del Debug.trace("Starting to parse a group of rules.");	//G***del
					skipWhitespaceCharacters(reader);	//skip any whitespace characters
					final String propertyName=reader.readStringUntilChar(WHITESPACE_CHARS+PROPERTY_DIVIDER_CHAR);	//get the name of the property, which is followed by whitespace or a divider character
//G***del Debug.trace("Property name: "+propertyName);	//G***del

					skipWhitespaceCharacters(reader);	//skip any whitespace characters
					reader.readExpectedChar(PROPERTY_DIVIDER_CHAR);	//reade the divider between the property name and its value
					skipWhitespaceCharacters(reader);	//skip any whitespace characters

					final XMLCSSValue propertyValue=parseValue(reader, propertyName);	//parse the value, showing which property the value is for
					skipWhitespaceCharacters(reader);	//skip any whitespace characters
					//G***parse the priority somewhere here
					((XMLCSSStyleDeclaration)styleRule.getStyle()).setPropertyCSSValue(propertyName, propertyValue);	//G***set this property
					skipWhitespaceCharacters(reader);	//skip any whitespace characters
				}
				while(reader.peekChar()!=RULE_GROUP_END_CHAR);	//keep reading rules until we've reached the end of the group
*/
				reader.readExpectedChar(RULE_GROUP_END_CHAR);	//read the end of the group
//G***del System.out.println("Finished with rule group.");	//G***del
				return styleRule;	//return the style rule we constructed
			}
			else	//if this isn't the start of a group of rules, so this must have just been the separator between selectors
				reader.readExpectedChar(SELECTOR_SEPARATOR_CHAR);	//read the separator character and go on reading selectors
		}
	}

	/**Parses an input stream that supposedly begins with a CSS style,
		a group of rules with attribute names and values.
	@param reader The reader from which to retrieve characters.
	@param styleDeclaration The style declaration which will hold the style declaration elements.
	@param endChar
//G***fix all the exception stuff
	@except IOException Thrown when an i/o error occurs.
//G***fix	@except XMLSyntaxException Thrown when there is a syntax error in the XML file.
//G***fix	@except XMLWellFormednessException Thrown when there is a well-formedness error in the XML file.
	@except ParseUnexpectedDataException Thrown when an unexpected character is found.
	@except ParseEOFException Thrown when the end of the input stream is reached unexpectedly.
//G***fix	@except XMLUndefinedEntityReferenceException Thrown if a named entity reference has not been defined.
	@return <code>true</code> if the end-of-rule-group character was found,
		<code>false</code> if the end of the stream was reached.
	*/
	public static boolean parseRuleSet(final ParseReader reader, final XMLCSSStyleDeclaration styleDeclaration) throws IOException,/*G***fix, XMLSyntaxException, XMLWellFormednessException, */ParseUnexpectedDataException, ParseEOFException//G***fix, XMLUndefinedEntityReferenceException
	{
//G***del Debug.trace();
		do	//keep reading declarations until there aren't any more
		{
			skipWhitespaceCharactersEOF(reader);	//skip over any whitespace characters, but do not throw an error for the end of the stream
			if(reader.isEOF())	//if we've hit the end of the stream of characters
				return false;	//show that we hit the end of the stream without finding the end-of-rule-group character
//G***del Debug.trace("Starting to parse a group of rules.");	//G***del
			skipWhitespaceCharacters(reader);	//skip any whitespace characters
			if((char)reader.peek()==RULE_GROUP_END_CHAR)  //if we're at the end of the rule group (which will happen if the rule group is empty, for instance) G***what if this is just an empty string being passed, from an XHTML style="" attribute?
				return true;  //show that we hit the end of the rule group G***this check is done at the end of this loop, too -- is there a place we can combine both checks?
			final String propertyName=reader.readStringUntilChar(WHITESPACE_CHARS+PROPERTY_DIVIDER_CHAR+RULE_GROUP_END_CHAR);	//get the name of the property, which is followed by whitespace or a divider character
//G***del Debug.trace("Property name: "+propertyName);	//G***del

			skipWhitespaceCharacters(reader);	//skip any whitespace characters
			reader.readExpectedChar(PROPERTY_DIVIDER_CHAR);	//reade the divider between the property name and its value
			skipWhitespaceCharacters(reader);	//skip any whitespace characters



//G***del Debug.trace("Ready to parse ruleset value for property: ", propertyName);  //G***del
		  final String propertyValueString=parseValue(reader, propertyName); //parse the value, showing which property the value is for
//G***del when works			final XMLCSSValue propertyValue=parseValue(reader, propertyName);	//parse the value, showing which property the value is for
			skipWhitespaceCharactersEOF(reader);	//skip over any whitespace characters, but do not throw an error for the end of the stream
			//G***parse the priority somewhere here
//G***del Debug.trace("ready to set property with value string: ", propertyValueString);  //G***fix
		  if(propertyValueString.trim().length()>0) //if there actually is a property value
			{
//G***del Debug.trace("This value checked out");  //G***del
				styleDeclaration.setProperty(propertyName, propertyValueString);  //set this property G***check about the DOM exception it throws
			}

//G***del when works			styleDeclaration.setPropertyCSSValue(propertyName, propertyValue);	//G***set this property
			skipWhitespaceCharactersEOF(reader);	//skip over any whitespace characters, but do not throw an error for the end of the stream
		}
		while((char)reader.peek()!=RULE_GROUP_END_CHAR);	//keep reading rules until we've reached the end of the group G***should this be at the beginning? what if it's an empty style block {}?
		return true;  //show that we found the end-of-rule-group character
	}

	/**Parses an input stream that contains stylesheet information.
	@param reader The reader from which to retrieve characters.
	@param parentStyleSheet The stylesheet which will hold the nodes encountered.

//G***fix all this exception stuff
	@except IOException Thrown when an i/o error occurs.
	@except XMLSyntaxException Thrown when there is a syntax error in the XML file.
	@except XMLWellFormednessException Thrown when there is a well-formedness error in the XML file.
	@except XMLValidityException Thrown when there is a validity error in the XML file.
	@except XMLUndefinedEntityReferenceException Thrown if a named entity reference has not been defined.
	@except ParseUnexpectedDataException Thrown when an unexpected character is found.
	@except ParseEOFException Thrown when the end of the input stream is reached unexpectedly.
//G***fix	@return A new XML object constructed from the markup.
	*/
	protected void parseStyleSheetContent(final ParseReader reader, final XMLCSSStyleSheet parentStyleSheet) throws IOException, /*G***fix XMLSyntaxException, XMLWellFormednessException, XMLValidityException, XMLUndefinedEntityReferenceException,*/ ParseUnexpectedDataException, ParseEOFException
	{
		//the stylesheet strings we expect; make sure we put the AT_RULE_START after the other at-rule constants, because it represents an unknown at-rule
		final String[] EXPECTED_STYLESHEET_STRINGS={MEDIA_RULE_SYMBOL, PAGE_RULE_SYMBOL, FONT_FACE_RULE_SYMBOL, AT_RULE_START, CDO, CDC, COMMENT_START, ""};
		//the indexes of the stylesheet strings in our array
		final int MEDIA_RULE=0, PAGE_RULE=1, FONT_FACE_RULE=2, UNKNOWN_AT_RULE=3, XML_COMMENT_START=4, XML_COMMENT_END=5, COMMENT=6;
		while(true)	//we'll keep processing rulesets and such until we run out of characters looking for whitespace
		{
			skipWhitespaceCharactersEOF(reader);	//skip over any whitespace
			if(reader.isEOF())	//if we've hit the end of the stream of characters
				return;	//we're finished processing the stylesheet
			reader.resetPeek();	//reset peeking so that the next character peeked will reflect the next character to be read

			//G***add CSS comment parsing as well
Debug.trace("looking for next stylesheet thing");
			switch(reader.peekExpectedStrings(EXPECTED_STYLESHEET_STRINGS))	//see what we have next in the stylesheet
			{
				case MEDIA_RULE:	//if this is a media rule
Debug.trace("found media rule");
				  reader.readStringUntilChar('}');  //G***fix parsing media rules
					reader.readExpectedChar('}'); //G***fix parsing media rules
					break;	//G***fix
				case PAGE_RULE:	//if this is a page rule
Debug.trace("Found page rule"); //G***del
				  reader.readStringUntilChar('}');  //G***fix parsing page rules
					reader.readExpectedChar('}'); //G***fix parsing page rules
					break;	//G***fix
				case FONT_FACE_RULE:	//if this is a font face rule
Debug.trace("found font face");
				  reader.readStringUntilChar('}');  //G***fix parsing font-face rules
					reader.readExpectedChar('}'); //G***fix parsing font-face rules
					break;	//G***fix
				case UNKNOWN_AT_RULE:	//if this is an at-rule we don't know about
Debug.trace("found unknown at rule");
				  reader.readStringUntilChar('}');  //G***fix parsing at rules
					reader.readExpectedChar('}'); //G***fix parsing at rules
					break;	//G***fix
				case COMMENT:	//if this is a CSS comment
Debug.trace("comment");
					while(!reader.isEOF())  //G***fix parsing comments; this is a hack
					{
						reader.readStringUntilChar('*');  //G***fix parsing comments; this is a hack
						reader.readExpectedChar('*'); //G***fix parsing comments; this is a hack
						if(reader.peekChar()=='/')  //G***fix parsing comments; this is a hack
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
Debug.trace("Found a ruleset.");	//G***del
					((XMLCSSRuleList)parentStyleSheet.getCssRules()).add(parseRuleSet(reader, parentStyleSheet));	//parse this ruleset and add it to our list of rules
					break;
			}
//G***del System.out.println("Outside of switch statment.");	//G***del

		}
	}

	/**Parses any internal stylesheets contained in an XML document.
	@param document The XML document that that contains stylesheet information.
//G***fix all this exception stuff
	@except IOException Thrown when an i/o error occurs.
	@except ParseUnexpectedDataException Thrown when an unexpected character is found.
	@except ParseEOFException Thrown when the end of the input stream is reached unexpectedly.
	*/
	public void parseInternalStyleSheets(final XMLDocument document) throws IOException, ParseUnexpectedDataException, ParseEOFException
	{
Debug.trace("ready to parse internal stylesheets"); //G***del
/*G***fix
		final XMLNodeList styleElementList=XPath.evaluateLocationPath(document, )
(XMLNodeList)document.getElementsByTagName("style");	//get a list of all the style elements in the document G***use a constant here
*/
		//G***we should probably eventually make sure the styles come at the right place, but an OEB version of this should have been validated against a DTD at some point which ensures this anyway
		final XMLNodeList styleElementList=(XMLNodeList)document.getElementsByTagName("style");	//get a list of all the style elements in the document G***use a constant here
Debug.trace("Number of style tags found: ", styleElementList.size());	//G***del

		for(int styleIndex=0; styleIndex<styleElementList.size(); ++styleIndex)	//look at each of the elements representing a style
		{
			//G***we should probably check to make sure this is "text/css" or "text/css-oeb" or whatever
			final XMLElement styleElement=(XMLElement)styleElementList.get(styleIndex);	//get the element at this index

//G***del System.out.println("Text from getText() method: "+styleElement.getText());	//G***del

			String styleText="";	//we'll eventually have all the text of this style in this string
			for(int styleChildIndex=0; styleChildIndex<((XMLNodeList)styleElement.getChildNodes()).size(); ++styleChildIndex)	//in case they wrapped the style in a comment, we'll get normal text and comment text
			{
				XMLNode styleChildNode=(XMLNode)styleElement.getChildNodes().item(styleChildIndex);	//get a reference to this child node

//G***del Debug.trace("Looking at style child node of type "+styleChildNode.getNodeType()+" with text: "+styleChildNode.getText());	//G***del

				if(styleChildNode.getNodeType()==XMLNode.TEXT_NODE)	//if this is a text node
					styleText+=styleChildNode.getText();	//get the text of the node
				else if(styleChildNode.getNodeType()==XMLNode.COMMENT_NODE)	//if this is a comment node
					styleText+=((XMLComment)styleChildNode).getData();	//get the comment data
			}

//G***del Debug.trace("Style text found: ", styleText);	//G***del

			//G***probably have a parseStyleSheetString function or something that does this
			final XMLCSSStyleSheet styleSheet=new XMLCSSStyleSheet(styleElement);	//create a new stylesheet owned by this style element node
			final ParseReader styleSheetReader=new ParseReader(styleText, "Internal Style Sheet");	//create a string reader from the text of this style G***Int
//G***del when works			final ParseReader styleSheetReader=new ParseReader(new StringReader(styleText), "Internal Style Sheet");	//create a string reader from the text of this style G***Int
//G***fix			entityReader.setCurrentLineIndex(entity.getLineIndex());	//pretend we're reading where the entity was located in that file, so any errors will show the correct information
//G***fix			entityReader.setCurrentCharIndex(entity.getCharIndex());	//pretend we're reading where the entity was located in that file, so any errors will show the correct information
			parseStyleSheetContent(styleSheetReader, styleSheet);	//parse the stylesheet content
			document.getStyleSheetList().add(styleSheet);	//add the stylesheet we created to the document's list of stylesheets
		}
Debug.trace("parsed internal stylesheets"); //G***del
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
	@param contextURL The source of the
	@except IOException Thrown when an i/o error occurs.
	@except ParseUnexpectedDataException Thrown when an unexpected character is found.
	@except ParseEOFException Thrown when the end of the input stream is reached unexpectedly.
	*/
	protected void parse(final XMLCSSStyleSheet styleSheet, final Reader reader, final Object sourceObject) throws IOException, ParseUnexpectedDataException, ParseEOFException
	{
		final ParseReader styleSheetReader=new ParseReader(reader, sourceObject);	//create a parse reader reader to use to read the stylesheet
//G***fix			entityReader.setCurrentLineIndex(entity.getLineIndex());	//pretend we're reading where the entity was located in that file, so any errors will show the correct information
//G***fix			entityReader.setCurrentCharIndex(entity.getCharIndex());	//pretend we're reading where the entity was located in that file, so any errors will show the correct information
		parseStyleSheetContent(styleSheetReader, styleSheet);	//parse the stylesheet content
	}



	/**Parses any external stylesheets contained in an XML document.
	@param document The XML document that that contains stylesheet information.
	@param sourceObject The source of the XML document (e.g. a String, File, or URL).
//G***fix all this exception stuff
	@except IOException Thrown when an i/o error occurs.
	@except ParseUnexpectedDataException Thrown when an unexpected character is found.
	@except ParseEOFException Thrown when the end of the input stream is reached unexpectedly.
	*/
//G***actually, these parseXXXStyleSheets() should probably be inside an XMLStyleSheetProcessor which calls the correct type of processor based upon the style sheet type
/*G***del when not needed
	public void parseExternalStyleSheets(final XMLDocument document, final Object sourceObject) throws IOException, ParseUnexpectedDataException, ParseEOFException
	{
			//check for xml-stylesheet processing instructions
		//G***we should probably eventually make sure the directives come at the right place, but an OEB version of this should have been validated against a DTD at some point which ensures this anyway
		final XMLNodeList styleSheetProcessingInstructionList=(XMLNodeList)document.getNodesByName(XMLNode.PROCESSING_INSTRUCTION_NODE, XML_STYLESHEET_PROCESSING_INSTRUCTION, true);	//get a list of all the style processing instructions in the document G***use a constant here
		for(int styleSheetIndex=0; styleSheetIndex<styleSheetProcessingInstructionList.size(); ++styleSheetIndex)	//look at each of the nodes representing a style sheet link
		{
			final XMLProcessingInstruction styleSheetLink=(XMLProcessingInstruction)styleSheetProcessingInstructionList.item(styleSheetIndex);	//get a reference to this child node
				//G***check the media type, etc. here
			final XMLCSSStyleSheet styleSheet=new XMLCSSStyleSheet(styleSheetLink);	//create a new stylesheet owned by this style processing instruction
			final String href=styleSheetLink.getPseudoAttributeValue(HREF_ATTRIBUTE);	//get the value of the href attribute, if it is present
			final URL styleSheetURL=URLUtilities.createURL(sourceObject, href);	//create a URL from the original URL of the XML document and the href
//G***del Debug.notify(styleSheetURL.toString());	//G***del
			final InputStreamReader inputStreamReader=new InputStreamReader(getInputStreamLocator().getInputStream(styleSheetURL));	//get an input stream to the external stylesheet G***use the document's encoding here
			final ParseReader styleSheetReader=new ParseReader(inputStreamReader, styleSheetURL);	//create a parse reader reader to use to read the external stylesheet
			try
			{
//G***fix			entityReader.setCurrentLineIndex(entity.getLineIndex());	//pretend we're reading where the entity was located in that file, so any errors will show the correct information
//G***fix			entityReader.setCurrentCharIndex(entity.getCharIndex());	//pretend we're reading where the entity was located in that file, so any errors will show the correct information
				parseStyleSheetContent(styleSheetReader, styleSheet);	//parse the stylesheet content
				document.getStyleSheetList().add(styleSheet);	//add the stylesheet we created to the document's list of stylesheets
			}
			finally
			{
				styleSheetReader.close();	//always close the stylesheet reader
			}
		}
	}
*/

	/**Parses any local styles contained in an XML document as a "style" attribute. G***this shouldn't go in the CSS processor itself
	@param document The XML document that that contains style information.
//G***fix all this exception stuff
	@except IOException Thrown when an i/o error occurs.
	@except ParseUnexpectedDataException Thrown when an unexpected character is found.
	@except ParseEOFException Thrown when the end of the input stream is reached unexpectedly.
	*/
//G***actually, these parseXXXStyleSheets() should probably be inside an XMLStyleSheetProcessor which calls the correct type of processor based upon the style sheet type
//G***this can probably be static
/*G***move
	public void parseLocalStyles(final XMLDocument document) throws IOException, ParseUnexpectedDataException, ParseEOFException
	{
		NodeIterator nodeIterator=((DocumentTraversal)document).createNodeIterator(document.getDocumentElement(), NodeFilter.SHOW_ELEMENT, null, false); //create a node walker to traverse over every node
		Node node;
		while((node=nodeIterator.nextNode())!=null)  //while we haven't reached the last node
		{
//G***del Debug.trace("Node: "+node.getNodeName()+" namespace URI: "+node.getNamespaceURI()); //G***testing
		  final Element element=(Element)node;  //cast the node to an element; elements are all we asked for
		  final String elementName=element.getNodeName(); //get the name of the element G***fix for namespaces
		  if(element.hasAttributeNS(null, "style")) //if this element has the style attribute G***use a constant, fix for a general attribute name case
			{
			  final String styleValue=element.getAttributeNS(null, "style");  //get the value of the style attribute G***use a constant, fix for a general attribute name case
				if(styleValue.length()!=0)  //if there is a style value
				{
//G***del Debug.trace("Found local style value: ", styleValue); //G***del
					final XMLCSSStyleDeclaration style=new XMLCSSStyleDeclaration(); //create a new style declaration
					final ParseReader localStyleReader=new ParseReader(styleValue, "Element "+elementName+" Local Style");	//create a string reader from the value of this local style attribute G***i18n
					parseRuleSet(localStyleReader, style); //read the style into our style declaration
					((XMLElement)element).setLocalCSSStyle(style);  //set the element's style to whatever we constructed G***eventually use a separate style tree instead of the element itself
				}
			}
		}
	}
*/

	/**Parses any stylesheets contained in or referenced from an XML document, as
		well as local style declarations in "style" attributes.
	@param document The XML document that that contains stylesheet information.
	@param sourceObject The source of the XML document (e.g. a String, File, or URL).
//G***fix all this exception stuff
	@except IOException Thrown when an i/o error occurs.
	@except ParseUnexpectedDataException Thrown when an unexpected character is found.
	@except ParseEOFException Thrown when the end of the input stream is reached unexpectedly.
	*/
/*G***move
	public void parseStyles(final XMLDocument document, final Object sourceObject) throws IOException, ParseUnexpectedDataException, ParseEOFException
	{
		parseExternalStyleSheets(document, sourceObject);	//parse any external stylesheets
		parseInternalStyleSheets(document);	//parse any internal stylesheets
		parseLocalStyles(document); //parse any local styles
	}
*/

	//G***couldn't a lot of the functions above be static?

	/**Determines the style of each element in the document based on the document's
		stylesheets as well as local element style attributes.
	*/
/*G***move
	public static void applyStyles(final XMLDocument document)
	{
			//apply all the styles in each stylesheet
		for(int styleSheetIndex=0; styleSheetIndex<document.getStyleSheetList().size(); ++styleSheetIndex)	//look at each of this document's stylesheets
		{
			if(document.getStyleSheetList().get(styleSheetIndex) instanceof XMLCSSStyleSheet)	//if this stylesheet is a CSS stylesheet (we only support CSS stylesheets in this implementation) G***change this comment if we move this function somewhere else where it doesn't apply
			{
				final XMLCSSStyleSheet cssStyleSheet=(XMLCSSStyleSheet)document.getStyleSheetList().get(styleSheetIndex);	//get a reference to this CSS stylesheet
				applyStyleSheet(document, cssStyleSheet);	//apply this stylesheet to the document
			}
		}
			//apply all the local styles G***fix to make more efficient
		final XMLNodeList elementList=(XMLNodeList)document.getElementsByTagName("*");	//get a list of all elements in this document G***maybe use a constant here
		for(int elementIndex=0; elementIndex<elementList.size(); ++elementIndex)	//look at each of the elements
		{
			final XMLElement element=(XMLElement)elementList.get(elementIndex);	//get the element at this index
		  final XMLCSSStyleDeclaration elementStyle=(XMLCSSStyleDeclaration)element.getCSSStyle();  //get a reference to this element's style G***eventually put the style tree somewhere else
		  final XMLCSSStyleDeclaration elementLocalStyle=(XMLCSSStyleDeclaration)element.getLocalCSSStyle();  //get a reference to this element's local style, if available G***eventually put the style tree somewhere else
		  if(elementLocalStyle!=null) //if this element has a local style
				elementStyle.importStyle(elementLocalStyle);	//import this element's local style the element's overall style
		}
	}
*/

	/**Determines the style of each element in the document based on the document's
		stylesheets as well as local element style attributes.
	*/
	//G***maybe put this in XMLCSSStyleSheet
/*G***move
	public static void applyStyleSheet(final XMLDocument document, final XMLCSSStyleSheet styleSheet)
	{
		  //G***this looks expensive -- the tree has to be walked first to gather the elements, then again to apply the styles
		final XMLNodeList elementList=(XMLNodeList)document.getElementsByTagName("*");	//get a list of all elements in this document G***maybe use a constant here
//G***del System.out.println("Looking at this many elements: "+elementList.size());
		for(int elementIndex=0; elementIndex<elementList.size(); ++elementIndex)	//look at each of the elements
		{
			final XMLElement element=(XMLElement)elementList.get(elementIndex);	//get the element at this index
			for(int ruleIndex=0; ruleIndex<styleSheet.getCssRules().getLength(); ++ruleIndex)	//look at each of our rules
			{
				if(styleSheet.getCssRules().item(ruleIndex).getType()==XMLCSSRule.STYLE_RULE)	//if this is a style rule G***fix for other rule types
				{
					final XMLCSSStyleRule styleRule=(XMLCSSStyleRule)styleSheet.getCssRules().item(ruleIndex);	//get a reference to this style rule in the stylesheet
					if(styleRule.appliesTo(element))	//if this style rule applies to this element
						((XMLCSSStyleDeclaration)element.getCSSStyle()).importStyle((XMLCSSStyleDeclaration)styleRule.getStyle());	//import this style's properties into the style of this element
				}
			}
		}
	}
*/
}
