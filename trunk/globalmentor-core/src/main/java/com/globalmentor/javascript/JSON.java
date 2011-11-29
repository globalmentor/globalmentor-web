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

package com.globalmentor.javascript;

import static java.lang.reflect.Array.*;
import java.util.*;
import java.util.regex.Pattern;


import static com.globalmentor.java.Arrays.*;
import static com.globalmentor.java.CharSequences.*;
import static com.globalmentor.java.Objects.*;
import static com.globalmentor.java.StringBuilders.*;

import com.globalmentor.model.ObjectHolder;
import com.globalmentor.text.ArgumentSyntaxException;
import com.globalmentor.text.W3CDateFormat;

/**Utilities for encoding and decoding JavaScript Object Notation (JSON).
In addition to standard JSON, any {@link Date} object will be formatted as a string value according to the W3C Note, 
	"Date and Time Formats", <a href="http://www.w3.org/TR/NOTE-datetime">http://www.w3.org/TR/NOTE-datetime</a>,
	a profile of ISO 8601.
@author Garret Wilson
@see <a href="http://www.ietf.org/rfc/rfc4627.txt">RFC 4627: The application/json Media Type for JavaScript Object Notation (JSON)</a>
@see <a href="http://www.json.org/">Introducing JSON</a>
@see <a href="http://www.w3.org/TR/NOTE-datetime">Date and Time Formats</a>
@see W3CDateFormat.Style#DATE_TIME
*/
public class JSON
{

	public final static char BEGIN_ARRAY=0x5B;	//[ left square bracket
	public final static char BEGIN_OBJECT=0x7B;	//[ left square bracket
	public final static char END_ARRAY=0x5D;	//] right square bracket
	public final static char END_OBJECT=0x7D;	//} right curly bracket
	public final static char NAME_SEPARATOR=0x3A;	//: colon
	public final static char VALUE_SEPARATOR=0x2C;	//, comma
	public final static char ESCAPE=0x5C;	//\
	public final static char QUOTATION_MARK=0x22;	//"
	public final static char MINUS=0x2D;	//-
  public final static char PLUS=0x2B;	//+
  public final static char DECIMAL_POINT=0x2E;	//.

		//whitespace characters
	public final static char SPACE=0x20;	//Space
	public final static char HORIZONTAL_TAB=0x09;	//Horizontal tab
	public final static char LINE_FEED=0x0A;	//Line feed or New line
	public final static char CARRIAGE_RETURN=0x0D;	//Carriage return
		//escaped forms of characters
	public final static char ESCAPED_QUOTATION_MARK=0x22;	//" quotation mark  U+0022
	public final static char ESCAPED_REVERSE_SOLIDUS=0x5C;	//\ reverse solidus U+005C
	public final static char REVERSE_SOLIDUS=0x5C;	//\ reverse solidus U+005C
	public final static char ESCAPED_SOLIDUS=0x2F;	/// solidus U+002F
	public final static char SOLIDUS=0x2F;	/// solidus U+002F
	public final static char ESCAPED_BACKSPACE=0x62;	//b backspace U+0008
	public final static char BACKSPACE=0x08;	//b backspace U+0008
	public final static char ESCAPED_FORM_FEED=0x66;	//f form feed U+000C
	public final static char FORM_FEED=0x0C;	//f form feed U+000C
	public final static char ESCAPED_LINE_FEED=0x6E;	//n line feed U+000A
	public final static char ESCAPED_CARRIAGE_RETURN=0x72;	//r carriage return U+000D
	public final static char ESCAPED_TAB=0x74;	//t tab U+0009	

	/**The character introducing an escaped Unicode code point.*/
	public final static char ESCAPED_UNICODE='u';	

	/**Sign characters.*/
	public final static char[] SIGN=new char[]{MINUS, PLUS};

	/**Digit characters.*/
	public final static char[] DIGITS=new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

	/**Whitespace characters.*/
	public final static char[] WHITESPACE=new char[]{SPACE, HORIZONTAL_TAB, LINE_FEED, CARRIAGE_RETURN};

	/**The null identifier.*/
	public final static String NULL="null";
	/**The boolean true identifier.*/
	public final static String TRUE="true";
	/**The boolean false identifier.*/
	public final static String FALSE="false";
	//TODO fix (?<!\\\\)
	public final static Pattern JSON_PATTERN=Pattern.compile("[{}\\[\\]:,]|(?:\"[^((?<!\\\\)\")]*\")|(?:null)|(?:true)(?:false)");

	/**Appends an object value.
	Supported value types are:
	<ul>
		<li>{@link CharSequence} (string)</li>
		<li>{@link Boolean} (boolean)</li>
		<li>{@link Number} (number)</li>
		<li>{@link List} (array)</li>
		<li>{@link Map} (associative array)</li>
		<li>[] (array)</li>
		<li>{@link Date} (W3C date/time)</li>
		<li>{@link Object} (string)</li>
		<li><code>null</code></li>
	</ul>
	@param stringBuilder The string builder to accept the string.
	@param object The object value to be appended, or <code>null</code>.
	@return The string builder.
	*/
	public static StringBuilder appendValue(final StringBuilder stringBuilder, final Object value)
	{
		if(value!=null)	//if the value is not null
		{
			if(value instanceof CharSequence)	//string
			{
				appendStringValue(stringBuilder, (CharSequence)value);	
			}
			else if(value instanceof Boolean)	//boolean
			{
				appendBooleanValue(stringBuilder, (Boolean)value);
			}
			else if(value instanceof Number)	//number
			{
				appendNumberValue(stringBuilder, (Number)value);
			}
			else if(value instanceof List)	//if the value is a list
			{
				appendArrayValue(stringBuilder, ((List<?>)value).toArray());	//append the list as an array
			}
			else if(value instanceof Map)	//if the value is a map
			{
				appendAssociativeArrayValue(stringBuilder, (Map<?, ?>)value);	//append the map as an associative array
			}
			else if(value.getClass().isArray())	//if the value is an array (we can't use instanceof Object[], because this may be an array of something besides Object)
			{
				appendArrayValue(stringBuilder, value);	//append the array
			}
			else if(value instanceof Date)	//date
			{
				appendStringValue(stringBuilder, W3CDateFormat.format((Date)value, W3CDateFormat.Style.DATE_HOURS_MINUTES_SECONDS));
			}
			else	//if we can't determine the type of object
			{
				appendStringValue(stringBuilder, value.toString());	//append a string form of the value	
			}
		}
		else	//if the value is null
		{
			stringBuilder.append(NULL);	//append null
		}
		return stringBuilder;	//return the string builder
	}


	/**Appends a string value in the form <code>"<var>string</var>"</code>.
	The character sequence will first be encoded as necessary.
	@param stringBuilder The string builder to accept the string.
	@param charSequence The string characters to be appended.
	@return The string builder.
	@exception NullPointerException if the given character sequence is <code>null</code>.
	@see #encodeStringValue(StringBuilder)
	*/
	public static StringBuilder appendStringValue(final StringBuilder stringBuilder, final CharSequence charSequence)
	{
		return stringBuilder.append(QUOTATION_MARK).append(encodeStringValue(charSequence)).append(QUOTATION_MARK);	//append and return "encodedString"
	}

	/**Appends a boolean value.
	@param stringBuilder The string builder to accept the value.
	@param bool The boolean value to append.
	@return The string builder.
	@exception NullPointerException if the given boolean value is <code>null</code>.
	*/
	public static StringBuilder appendBooleanValue(final StringBuilder stringBuilder, final Boolean bool)
	{
		return stringBuilder.append(checkInstance(bool, "Boolean value cannot be null."));	//append and return boolean
	}

	/**Appends a number value.
	@param stringBuilder The string builder to accept the value.
	@param number The number to append.
	@return The string builder.
	@exception NullPointerException if the given number is <code>null</code>.
	*/
	public static StringBuilder appendNumberValue(final StringBuilder stringBuilder, final Number number)
	{
		return stringBuilder.append(checkInstance(number, "Number value cannot be null."));	//append and return number
	}

	/**Appends an array value.
	@param stringBuilder The string builder to accept the value.
	@param array The array to append.
	@return The string builder.
	@exception NullPointerException if the given array is <code>null</code>.
	@exception IllegalArgumentException if the given object is not an array.
	*/
	public static StringBuilder appendArrayValue(final StringBuilder stringBuilder, final Object array)
	{
		stringBuilder.append(BEGIN_ARRAY);	//[
		final int arrayLength=getLength(array);	//see how long the array is
		if(arrayLength==0)	//if the array is empty
		{
			stringBuilder.append(END_ARRAY);	//]
		}
		else	//if the array isn't empty
		{
			for(int i=0; i<arrayLength; ++i)	//for each array element
			{
				appendValue(stringBuilder, get(array, i));	//append this element value
				stringBuilder.append(VALUE_SEPARATOR);	//,
			}
			final int lastIndex=stringBuilder.length()-1;	//get the index of the last character, which is the delimiter
			stringBuilder.replace(lastIndex, lastIndex+1, String.valueOf(END_ARRAY));	//replace the last delimiter with ']'
		}
		return stringBuilder;	//return the string builder
	}

	/**Appends an object (associative array) value in the form <code>{"<var>key</var>":<var>value</var>,...}</code>.
	The provided map must not have any <code>null</code> keys.
	@param <K> The type of keys stored in the map.
	@param <V> The type of values stored in the map.
	@param stringBuilder The string builder to accept the associative array.
	@param map The map containing the associative array values.
	@return The string builder.
	@exception NullPointerException if one of the keys of the given map is is <code>null</code>.
	@see #appendValue(StringBuilder, Object)
	*/
	public static <K, V> StringBuilder appendAssociativeArrayValue(final StringBuilder stringBuilder, final Map<K, V> map)
	{
		stringBuilder.append(BEGIN_OBJECT);	//{
		final Set<Map.Entry<K, V>> mapEntrySet=map.entrySet();	//get the set of map entries
		if(mapEntrySet.isEmpty())	//if the map is empty
		{
			stringBuilder.append(END_OBJECT);	//}
		}
		else	//if the map isn't empty
		{
			for(final Map.Entry<K, V> mapEntry:mapEntrySet)	//for each map entry
			{
				appendStringValue(stringBuilder, mapEntry.getKey().toString());	//key
				stringBuilder.append(NAME_SEPARATOR);	//:
				appendValue(stringBuilder, mapEntry.getValue());	//value
				stringBuilder.append(VALUE_SEPARATOR);	//,
			}
			final int lastIndex=stringBuilder.length()-1;	//get the index of the last character, which is the delimiter
			stringBuilder.replace(lastIndex, lastIndex+1, String.valueOf(END_OBJECT));	//replace the last delimiter with '}'
		}
		return stringBuilder;	//return the string builder
	}

	/**Encodes a string value.
	The characters {@value JavaScript#STRING_ENCODE_CHARS} will be replaced with {@value JavaScript#STRING_ENCODE_REPLACEMENT_STRINGS}, respectively. 
	@param charSequence The characters to encode.
	@return A string containing encoded characters.
	@exception NullPointerException if the given character sequence is <code>null</code>.
	*/
	public static String encodeStringValue(final CharSequence charSequence)
	{
		final StringBuilder stringBuilder=new StringBuilder(checkInstance(charSequence, "Character sequence cannot be null."));	//create a new string builder with the contents of the character sequence
		replace(stringBuilder, JavaScript.STRING_ENCODE_CHARS, JavaScript.STRING_ENCODE_REPLACEMENT_STRINGS);	//replace the encode characters with their encoded replacements
		return stringBuilder.toString();	//return the encoded string
	}

	/**Decodes a string value.
	Every instance of {@value JavaScript#ESCAPE} will be removed if followed by another character and the subsequent character will be ignored. 
	@param charSequence The characters to encode.
	@return A string containing encoded characters.
	@exception NullPointerException if the given character sequence is <code>null</code>.
	@exception IllegalArgumentException if the character sequence ends with the given escape character.
	*/
	public static String decodeStringValue(final CharSequence charSequence)
	{
		return unescape(new StringBuilder(checkInstance(charSequence, "Character sequence cannot be null.")), ESCAPE).toString();	//unescape the string
	}

	/**Serializes the given object in JSON.
	@param object The objectd to serialize.
	@return A string serialization of the given object.
	*/
	public static String serialize(final Object object)
	{
		return appendValue(new StringBuilder(), object).toString();	//serialize the given object and return the resulting string
	}

	/**Checks that the current character matches a specific character and advances to the next character.
	@param charSequence The character sequence to be parsed.
	@param index The current parse index in the character sequence.
	@param c The character against which the current character should be checked.
	@return The new index at which to continue parsing.
	@exception NullPointerException if the given character sequence is <code>null</code>.
	@exception ArrayIndexOutOfBoundsException if the character sequence has insufficient characters at the given index.
	@exception ArgumentSyntaxException if the current character in the sequence does not match the specified character.
	*/
	protected static int check(final CharSequence charSequence, final int index, final char c) throws ArgumentSyntaxException
	{
		if(charSequence.charAt(index)!=c)	//if this character does not match what we expected
		{
			throw new ArgumentSyntaxException("Expected "+(char)c+".", charSequence.toString(), index);
		}
		return index+1;	//return the subsequent index
	}

	/**Checks that the current character matches a character in a range and advances to the next character.
	@param charSequence The character sequence to be parsed.
	@param index The current parse index in the character sequence.
	@param lowerBound The lowest character in the range.
	@param upperBound The highest character in the range.	
	@return The new index at which to continue parsing.
	@exception NullPointerException if the given character sequence is <code>null</code>.
	@exception ArrayIndexOutOfBoundsException if the character sequence has insufficient characters at the given index.
	@exception ArgumentSyntaxException if the current character in the sequence does not fall within the given range.
	*/
	protected static int check(final CharSequence charSequence, int index, final char lowerBound, final char upperBound)
	{
		final char c=charSequence.charAt(index);	//get the current character
		if(c<lowerBound || c>upperBound)	//if this character is not in the range
		{
			throw new ArgumentSyntaxException("Expected character from "+(char)lowerBound+" to "+(char)upperBound+".", charSequence.toString(), index);
		}
		return index+1;	//return the subsequent index
	}
	
	/**Checks that the current characters matches a given set of characters and advances to the next character.
	@param charSequence The character sequence to be parsed.
	@param index The current parse index in the character sequence.
	@param characters The characters to accept.
	@return The new index at which to continue parsing.
	@exception NullPointerException if the given character sequence and/or the given characters is <code>null</code>.
	@exception ArrayIndexOutOfBoundsException if the character sequence has insufficient characters at the given index.
	@exception ArgumentSyntaxException if the current character in the sequence does not match one of the specified characters.
	*/
	protected static int check(final CharSequence charSequence, int index, final char[] characters)
	{
		if(indexOf(characters, charSequence.charAt(index))<0)	//if this character does not match one of the expected characters
		{
			throw new ArgumentSyntaxException("Expected one of "+java.util.Arrays.toString(characters)+".", charSequence.toString(), index);
		}
		return index+1;	//return the subsequent index
	}

	/**Checks that the current and subsequent characters matches a specified character sequence.
	@param charSequence The character sequence to be parsed.
	@param index The current parse index in the character sequence.
	@param match The character sequence with which the current characters should be checked.
	@return The new index at which to continue parsing.
	@exception NullPointerException if the given character sequence and/or match character sequence is <code>null</code>.
	@exception ArrayIndexOutOfBoundsException if the character sequence has insufficient characters at the given index.
	@exception ArgumentSyntaxException if the current character in the sequence does not match the specified character sequence.
	*/
	protected static int check(final CharSequence charSequence, int index, final CharSequence match) throws ArgumentSyntaxException
	{
		final int matchLength=match.length();	//get the length to match
		for(int i=0; i<matchLength; ++i)	//for each match index
		{
			index=check(charSequence, index, match.charAt(i));	//compare the current character with the match character
		}
		return index;	//return the index, which is already at the subsequent character
	}

	/**Skips over characters in a character sequence that appear within a given array.
	@param charSequence The character sequence to be parsed.
	@param index The current parse index in the character sequence.
	@param characters The characters to skip.
	@return The new index at which to continue parsing; either the first character not in the array, or the length of the character sequence.
	@exception NullPointerException if the given character sequence and/or the given characters is <code>null</code>.
	*/
	protected static int skip(final CharSequence charSequence, int index, final char[] characters)
	{
		char lowerBound=Character.MAX_VALUE;	//we'll determine the lower bound of the range
		char upperBound=0;	//we'll determine the lower bound of the range
		for(int i=characters.length-1; i>=0; --i)	//look at each characters to skip
		{
			final char c=characters[i];	//get this character
			if(c<lowerBound)	//if this is a lower character than the one we already have for the lower bound
			{
				lowerBound=c;	//update the lower bound
			}
			if(c>upperBound)	//if this is a higher character than the one we already have for the upper bound
			{
				upperBound=c;	//update the upper bound
			}
		}
		final int length=charSequence.length();	//get the length of the character sequence
		for(; index<length; ++index)	//keep looking until we run out of characters
		{
			final char c=charSequence.charAt(index);	//get the current character
			if(c<lowerBound || c>upperBound)	//if this character is not in the range of the characters
			{
				break;	//stop searching
			}
			else	//if the character is within the range of characters, make sure it's one of the characters
			{
				boolean skip=false;	//we'll see if there's a match
				for(int i=characters.length-1; i>=0 && !skip; --i)	//look at each characters to skip
				{
					if(c==characters[i])	//if we found a character to skip
					{
						skip=true;	//indicate that we should skip this character
					}
				}
				if(!skip)	//if we shouldn't skip this characters
				{
					break;	//stop advancing
				}
			}
		}
		return index;	//return the next index
	}

	/**Skips over characters in a character sequence that lie within a given range.
	@param charSequence The character sequence to be parsed.
	@param index The current parse index in the character sequence.
	@param lowerBound The lowest character in the range.
	@param upperBound The highest character in the range.	
	@return The new index at which to continue parsing; either the first character not in the range, or the length of the character sequence.
	@exception NullPointerException if the given character sequence is <code>null</code>.
	*/
	protected static int skip(final CharSequence charSequence, int index, final char lowerBound, final char upperBound)
	{
		final int length=charSequence.length();	//get the length of the character sequence
		for(; index<length; ++index)	//keep looking until we run out of characters
		{
			final char c=charSequence.charAt(index);	//get the current character
			if(c<lowerBound || c>upperBound)	//if this character is not in the range
			{
				break;	//stop searching
			}
		}
		return index;	//return the next index
	}
	
	/**Skips over JSON whitespace characters in a character sequence.
	@param charSequence The character sequence to be parsed.
	@param index The current parse index in the character sequence.
	@return The new index at which to continue parsing; either the first non-whitespace character, or the length of the character sequence.
	@exception NullPointerException if the given character sequence is <code>null</code>.
	*/
	protected static int skipWhitespace(final CharSequence charSequence, int index)
	{
		final int length=charSequence.length();	//get the length of the character sequence
		for(; index<length; ++index)	//keep looking until we run out of characters
		{
			switch(charSequence.charAt(index))
			{
				case SPACE:	//whitespace
				case HORIZONTAL_TAB:
				case LINE_FEED:
				case CARRIAGE_RETURN:
					continue;	//skip whitespace
				default:
					return index;	//stop advancing and return the index
			}
		}
		return index;	//return the index of the non-whitespace character
	}	
	
	/**Parses a value encoded in a JSON character sequence.
	@param charSequence The character sequence to be parsed.
	@return A new {@link String}, {@link Boolean}, {@link Number}, {@link List}, {@link Map}, or <code>null</code> representing the value represented by the character sequence.
	@exception NullPointerException if the given character sequence is <code>null</code>.
	@exception ArgumentSyntaxException if the given character sequence does not represent a valid JSON object.
	*/
	public static Object parseValue(final CharSequence charSequence) throws ArgumentSyntaxException
	{
		final ObjectHolder<Object> objectHolder=new ObjectHolder<Object>();	//create a new objecdt holder to hold the value
		try
		{
			parseValue(charSequence, 0, objectHolder);	//parse the value
			return objectHolder.getObject();	//return the object
		}
		catch(final IndexOutOfBoundsException indexOutOfBoundsException)	//if we ran out of characters
		{
			throw new ArgumentSyntaxException(indexOutOfBoundsException, charSequence.toString());
		}
		catch(final NumberFormatException numberFormatException)	//if a number wasn't formatted correctly
		{
			throw new ArgumentSyntaxException(numberFormatException, charSequence.toString());
		}
	}

	/**Parses a value encoded in a JSON character sequence.
	@param charSequence The character sequence to be parsed.
	@param index The current parse index in the character sequence.
	@param objectHolder The object that will hold the parsed object: {@link String}, {@link Boolean}, {@link Number}, {@link List}, {@link Map}, or <code>null</code> representing the value represented by the character sequence..
	@return The new index at which to continue parsing.
	@exception NullPointerException if the given character sequence is <code>null</code>.
	@exception ArgumentSyntaxException if the given character sequence does not represent a valid JSON object.
	@exception ArrayIndexOutOfBoundsException if the character sequence has insufficient characters at the given index.
	*/
	protected static int parseValue(final CharSequence charSequence, int index, final ObjectHolder<Object> objectHolder) throws ArgumentSyntaxException
	{
		final Object object;	//we'll set the object after parsing it
		char c=charSequence.charAt(index);	//get the current character
		switch(c)	//check the current character
		{
			case BEGIN_ARRAY:	//array
				{
					final List<Object> list=new ArrayList<Object>();	//create a new list to populate
					index=parseArray(charSequence, index, list);	//parse the array
					object=list;	//save the list we parsed
				}
				break;
			case BEGIN_OBJECT:	//object
				{
					final Map<String, Object> map=new HashMap<String, Object>();	//create a new map to populate
					index=parseObject(charSequence, index, map);	//parse the array
					object=map;	//save the map we parsed
				}
				break;
			case QUOTATION_MARK:	//string
				{
					final ObjectHolder<String> stringHolder=new ObjectHolder<String>();	//create a new string holder to populate
					index=parseString(charSequence, index, stringHolder);	//parse the string
					object=stringHolder.getObject();	//save the string we parsed
				}
				break;
			case MINUS:	//minus
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
				{
					final ObjectHolder<Number> numberHolder=new ObjectHolder<Number>();	//create a new object holder to populate
					index=parseNumber(charSequence, index, numberHolder);	//parse the number
					object=numberHolder.getObject();	//save the number we parsed
				}
				break;
			case 'f':
				index=check(charSequence, index, FALSE);	//make sure this is "false"
				object=Boolean.FALSE;	//the value is false
				break;
			case 'n':
				index=check(charSequence, index, NULL);	//make sure this is "null"
				object=null;	//the value is null
				break;
			case 't':
				index=check(charSequence, index, TRUE);	//make sure this is "true"
				object=Boolean.TRUE;	//the value is true
				break;
			default:
				throw new ArgumentSyntaxException("Illegal value character.", charSequence.toString(), index);
		}
		objectHolder.setObject(object);	//set the object we parsed
		return index;	//return the new index
	}

	/**Parses an array encoded in a JSON character sequence.
	@param charSequence The character sequence to be parsed.
	@param index The current parse index in the character sequence.
	@param list The list in which the parsed array contents will be placed.
	@return The new index at which to continue parsing after the array.
	@exception NullPointerException if the given character sequence is <code>null</code>.
	@exception ArgumentSyntaxException if the given character sequence does not represent a valid JSON string.
	@exception ArrayIndexOutOfBoundsException if the character sequence has insufficient characters at the given index.
	*/
	protected static int parseArray(final CharSequence charSequence, int index, final List<Object> list) throws ArgumentSyntaxException
	{
		index=check(charSequence, index, BEGIN_ARRAY);	//make sure this is the start of an array
		index=parseArrayContents(charSequence, index, list);	//parse the contents of the array
		return check(charSequence, index, END_ARRAY);	//make sure this is the end of a array
	}

	/**Parses the contents of an array encoded in a JSON character sequence.
	@param charSequence The character sequence to be parsed.
	@param index The current parse index in the character sequence.
	@param list The list in which the parsed array contents will be placed.
	@return The new index at which to continue parsing after the array contents (usually the character {@value #END_ARRAY}).
	@exception NullPointerException if the given character sequence is <code>null</code>.
	@exception ArgumentSyntaxException if the given character sequence does not represent a valid JSON object.
	@exception ArrayIndexOutOfBoundsException if the character sequence has insufficient characters at the given index.
	*/
	protected static int parseArrayContents(final CharSequence charSequence, int index, final List<Object> list) throws ArgumentSyntaxException
	{
		index=skipWhitespace(charSequence, index);	//skip whitespace
		if(charSequence.charAt(index)==END_ARRAY)	//if we've reached the end of the array
		{
			return index;	//return the new starting index
		}
		while(true)
		{
			final ObjectHolder<Object> objectHolder=new ObjectHolder<Object>();	//create a new object holder
			index=parseValue(charSequence, index, objectHolder);	//parse this value
			list.add(objectHolder.getObject());	//add the object to the list
			index=skipWhitespace(charSequence, index);	//skip whitespace
			final char c=charSequence.charAt(index);	//get the next character
			if(c==VALUE_SEPARATOR)	//if there are more values
			{
				index=skipWhitespace(charSequence, index+1);	//skip whitespace after the separator value and keep processing the other values
			}
			else if(c==END_ARRAY)	//if we've reached the end of the array
			{
				return index;	//return the new starting index
			}
			else	//if we don't have more values but we haven't reached the end of the array
			{
				throw new ArgumentSyntaxException("Expected "+VALUE_SEPARATOR+" or "+END_ARRAY+".", charSequence.toString(), index);
			}
		}
	}

	/**Parses an object encoded in a JSON character sequence.
	@param charSequence The character sequence to be parsed.
	@param index The current parse index in the character sequence.
	@param list The list in which the parsed array contents will be placed.
	@return The new index at which to continue parsing after the object.
	@exception NullPointerException if the given character sequence is <code>null</code>.
	@exception ArgumentSyntaxException if the given character sequence does not represent a valid JSON string.
	@exception ArrayIndexOutOfBoundsException if the character sequence has insufficient characters at the given index.
	*/
	protected static int parseObject(final CharSequence charSequence, int index, final Map<String, Object> map) throws ArgumentSyntaxException
	{
		index=check(charSequence, index, BEGIN_OBJECT);	//make sure this is the start of an object
		index=parseObjectContents(charSequence, index, map);	//parse the contents of the array
		return check(charSequence, index, END_OBJECT);	//make sure this is the end of a object
	}

	/**Parses the contents of an object encoded in a JSON character sequence.
	@param charSequence The character sequence to be parsed.
	@param index The current parse index in the character sequence.
	@param map The map in which the parsed object contents will be placed.
	@return The new index at which to continue parsing after the object contents (usually the character {@value #END_OBJECT}).
	@exception NullPointerException if the given character sequence is <code>null</code>.
	@exception ArgumentSyntaxException if the given character sequence does not represent a valid JSON object.
	@exception ArrayIndexOutOfBoundsException if the character sequence has insufficient characters at the given index.
	*/
	protected static int parseObjectContents(final CharSequence charSequence, int index, final Map<String, Object> map) throws ArgumentSyntaxException
	{
		index=skipWhitespace(charSequence, index);	//skip whitespace
		if(charSequence.charAt(index)==END_OBJECT)	//if we've reached the end of the object
		{
			return index;	//return the new starting index
		}
		while(true)
		{
			final ObjectHolder<String> nameHolder=new ObjectHolder<String>();	//create a new object holder for the name
			index=parseString(charSequence, index, nameHolder);	//parse the name
			index=skipWhitespace(charSequence, index);	//skip whitespace
			index=check(charSequence, index, NAME_SEPARATOR);	//parse the name separator
			index=skipWhitespace(charSequence, index);	//skip whitespace
			final ObjectHolder<Object> valueHolder=new ObjectHolder<Object>();	//create a new object holder for the value
			index=parseValue(charSequence, index, valueHolder);	//parse this value
			map.put(nameHolder.getObject(), valueHolder.getObject());	//store the value
			index=skipWhitespace(charSequence, index);	//skip whitespace
			final char c=charSequence.charAt(index);	//get the next character
			if(c==VALUE_SEPARATOR)	//if there are more values
			{
				index=skipWhitespace(charSequence, index+1);	//skip whitespace after the separator value and keep processing the other values
			}
			else if(c==END_OBJECT)	//if we've reached the end of the object
			{
				return index;	//return the new starting index
			}
			else	//if we don't have more values but we haven't reached the end of the object
			{
				throw new ArgumentSyntaxException("Expected "+VALUE_SEPARATOR+" or "+END_ARRAY+".", charSequence.toString(), index);
			}
		}
	}

	/**Parses a string encoded in a JSON character sequence.
	@param charSequence The character sequence to be parsed.
	@param index The current parse index in the character sequence.
	@param stringHolder The string holder in which the parsed string will be placed.
	@return The new index at which to continue parsing after the string.
	@exception NullPointerException if the given character sequence is <code>null</code>.
	@exception ArgumentSyntaxException if the given character sequence does not represent a valid JSON string.
	@exception ArrayIndexOutOfBoundsException if the character sequence has insufficient characters at the given index.
	*/
	protected static int parseString(final CharSequence charSequence, int index, final ObjectHolder<String> stringHolder) throws ArgumentSyntaxException
	{
		index=check(charSequence, index, QUOTATION_MARK);	//make sure this is the start of a string
		index=parseStringContents(charSequence, index, stringHolder);	//parse the contents of the string
		return check(charSequence, index, QUOTATION_MARK);	//make sure this is the end of a string
	}

	/**Parses the contents of a string encoded in a JSON character sequence.
	@param charSequence The character sequence to be parsed.
	@param index The current parse index in the character sequence.
	@param stringHolder The string holder in which the parsed string contents will be placed.
	@return The new index at which to continue parsing after the string contents (usually the character {@value #QUOTATION_MARK}).
	@exception NullPointerException if the given character sequence is <code>null</code>.
	@exception ArgumentSyntaxException if the given character sequence does not represent a valid JSON object.
	@exception ArrayIndexOutOfBoundsException if the character sequence has insufficient characters at the given index.
	*/
	protected static int parseStringContents(final CharSequence charSequence, int index, final ObjectHolder<String> stringHolder) throws ArgumentSyntaxException
	{
		final int endQuoteIndex=indexOf(charSequence, QUOTATION_MARK, index);	//the most common case is a simple quoted string with no escaped characters; try to handle the common case by getting the ending quotation mark, which will be much faster than appending characters one at a time
		final String commonCaseString=charSequence.subSequence(index, endQuoteIndex).toString();	//get the contents of the string (if there was no ending quotation mark, this will throw an exception, which we might as well throw now as any time)
		if(commonCaseString.indexOf(ESCAPE)<0)	//if the string has no escape characters, this is a simple string that we can simply return now
		{
			stringHolder.setObject(commonCaseString);	//the string will be the string we just parsed
			return endQuoteIndex;	//return the ending quote, which will be our next parse index
		}
		else	//if this string has escape characters, abandon the string (even the last quote could be escaped, after all) and construct the string from scratch
		{
			final StringBuilder stringBuilder=new StringBuilder();	//create a new string builder
			while(true)
			{
				char c=charSequence.charAt(index);	//get the current character
				if(c==QUOTATION_MARK)	//if we're reached the end of the string
				{
					stringHolder.setObject(stringBuilder.toString());	//set the string to be the contents of our string builder
					return index;	//return the index of the quotation mark
				}
				else if(c==ESCAPE)	//if this is the escape character
				{
					++index;	//we'll determine which character is being escaped
					c=charSequence.charAt(index);	//get the escaped version of the character
					switch(c)	//check the escaped version of the character
					{
						case ESCAPED_QUOTATION_MARK:	//" quotation mark  U+0022
							c=QUOTATION_MARK;
							break;
						case ESCAPED_REVERSE_SOLIDUS:	//\ reverse solidus U+005C
							c=REVERSE_SOLIDUS;
							break;
						case ESCAPED_SOLIDUS:	/// solidus U+002F
							c=SOLIDUS;
							break;
						case ESCAPED_BACKSPACE:	//b backspace U+0008
							c=BACKSPACE;
							break;
						case ESCAPED_FORM_FEED:	//f form feed U+000C
							c=FORM_FEED;
							break;
						case ESCAPED_LINE_FEED:	//n line feed U+000A
							c=LINE_FEED;
							break;
						case ESCAPED_CARRIAGE_RETURN:	//r carriage return U+000D
							c=CARRIAGE_RETURN;
							break;
						case ESCAPED_TAB:	//t tab U+0009	
							c=HORIZONTAL_TAB;
							break;
						case ESCAPED_UNICODE:	//if this is an escaped version of a Unicode code point
							{
								final int nextIndex=index+4;	//the hex characters should be four characters long
								c=(char)Integer.parseInt(charSequence.subSequence(index+1, nextIndex+1).toString(), 16);	//parse the hex characters
								index=nextIndex;	//skip the hex characters
							}
							break;
						default:	//if we didn't recognize the escaped character
							throw new ArgumentSyntaxException("Unrecognized escaped character "+c+".", charSequence.toString(), index);
					}
				}
				stringBuilder.append(c);	//append the character
				++index;	//look at the next index
			}
		}
	}

	/**Parses a number encoded in a JSON character sequence.
	@param charSequence The character sequence to be parsed.
	@param index The current parse index in the character sequence.
	@param numberHolder The number holder in which the parsed number will be placed.
	@return The new index at which to continue parsing after the number.
	@exception NullPointerException if the given character sequence is <code>null</code>.
	@exception ArgumentSyntaxException if the given character sequence does not represent a valid JSON number.
	@exception NumberFormatException if the given character sequence does not contain a parsable JSON number.
	@exception ArrayIndexOutOfBoundsException if the character sequence has insufficient characters at the given index.
	*/
	protected static int parseNumber(final CharSequence charSequence, int index, final ObjectHolder<Number> numberHolder) throws ArgumentSyntaxException
	{
		final int length=charSequence.length();	//get the length of the character sequence
		final int start=index;	//make note of where we start
		if(charSequence.charAt(index)==MINUS)	//if the number starts with a minus sign
		{
			++index;	//skip the minus sign
		}
		index=skip(charSequence, index, '0', '9');	//skip all characters '0'-'9'
		if(index<length)	//if we're not at the end of the character sequence
		{
			boolean hasFraction=false;	//we don't have a fraction yet
			boolean hasExponent=false;	//we don't have an exponent yet
			if(charSequence.charAt(index)==DECIMAL_POINT)	//if this is a floating point number
			{
				hasFraction=true;	//we found a fraction
				index=check(charSequence, index+1, DIGITS);	//make sure at least one digit comes after the decimal point
				index=skip(charSequence, index, '0', '9');	//skip all fraction characters '0'-'9'				
			}
			if(index<length)	//if we're not at the end of the character sequence
			{
				final char possibleExponentIndex=charSequence.charAt(index);	//get the character that may be an exponent
				if(possibleExponentIndex=='e' || possibleExponentIndex=='E')	//if this is an exponent
				{
					hasExponent=true;	//we found an exponent
					final char firstExponentCharacter=charSequence.charAt(index);	//get the first exponent character
					if(firstExponentCharacter==MINUS || firstExponentCharacter==PLUS)	//if the exponent starts with a sign
					{
						++index;	//skip the sign
					}
					index=check(charSequence, index+1, DIGITS);	//make sure at least one digit comes after the exponent
					index=skip(charSequence, index, '0', '9');	//skip all exponent characters '0'-'9'				
				}
			}
			if(hasFraction || hasExponent)	//if there was a fraction or exponent
			{
				numberHolder.setObject(Double.valueOf(Double.parseDouble(charSequence.subSequence(start, index).toString())));	//parse a double and store it
				return index;	//return the new index
			}
		}
		numberHolder.setObject(Integer.valueOf(Integer.parseInt(charSequence.subSequence(start, index).toString())));	//parse an integer and store it 
		return index;	//return the new index
	}

}
