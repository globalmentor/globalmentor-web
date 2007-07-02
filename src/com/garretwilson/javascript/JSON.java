package com.garretwilson.javascript;

import static java.lang.reflect.Array.*;
import java.util.*;

import static com.garretwilson.javascript.JavaScriptConstants.*;
import static com.garretwilson.lang.CharSequenceUtilities.*;
import static com.garretwilson.lang.StringBuilderUtilities.*;
import static com.garretwilson.lang.ObjectUtilities.*;
import static com.garretwilson.text.CharacterConstants.*;

import com.garretwilson.text.W3CDateFormat;

/**Utilities for encoding and decoding JavaScript Object Notation (JSON).
In addition to standard JSON, any {@link Date} object will be formatted as a string value according to the W3C Note, 
	"Date and Time Formats", <a href="http://www.w3.org/TR/NOTE-datetime">http://www.w3.org/TR/NOTE-datetime</a>,
	a profile of ISO 8601.
@author Garret Wilson
@see <a href="http://www.ietf.org/rfc/rfc4627.txt">The application/json Media Type for JavaScript Object Notation (JSON)</a>
@see <a href="http://www.json.org/">Introducing JSON</a>
@see <a href="http://www.w3.org/TR/NOTE-datetime">Date and Time Formats</a>
@see W3CDateFormat.Style#DATE_TIME
*/
public class JSON
{

	/**Appends an object value.
	Supported value types are:
	<ul>
		<li>{@link CharSequence} (string)</li>
		<li>{@link Boolean} (boolean)</li>
		<li>{@link Number} (number)</li>
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
		return stringBuilder.append(QUOTATION_MARK_CHAR).append(encodeStringValue(charSequence)).append(QUOTATION_MARK_CHAR);	//append and return "encodedString"
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
		stringBuilder.append(ARRAY_BEGIN_CHAR);	//[
		final int arrayLength=getLength(array);	//see how long the array is
		if(arrayLength==0)	//if the array is empty
		{
			stringBuilder.append(ARRAY_END_CHAR);	//]
		}
		else	//if the array isn't empty
		{
			for(int i=0; i<arrayLength; ++i)	//for each array element
			{
				appendValue(stringBuilder, get(array, i));	//append this element value
				stringBuilder.append(ARRAY_DELIMITER);	//,
			}
			final int lastIndex=stringBuilder.length()-1;	//get the index of the last character, which is the delimiter
			stringBuilder.replace(lastIndex, lastIndex+1, String.valueOf(ARRAY_END_CHAR));	//replace the last delimiter with ']'
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
		stringBuilder.append(ASSOCIATIVE_ARRAY_BEGIN_CHAR);	//{
		final Set<Map.Entry<K, V>> mapEntrySet=map.entrySet();	//get the set of map entries
		if(mapEntrySet.isEmpty())	//if the map is empty
		{
			stringBuilder.append(ASSOCIATIVE_ARRAY_END_CHAR);	//}
		}
		else	//if the map isn't empty
		{
			for(final Map.Entry<K, V> mapEntry:mapEntrySet)	//for each map entry
			{
				appendStringValue(stringBuilder, mapEntry.getKey().toString());	//key
				stringBuilder.append(ASSOCIATIVE_ARRAY_KEY_VALUE_DELIMITER);	//:
				appendValue(stringBuilder, mapEntry.getValue());	//value
				stringBuilder.append(ASSOCIATIVE_ARRAY_DELIMITER);	//,
			}
			final int lastIndex=stringBuilder.length()-1;	//get the index of the last character, which is the delimiter
			stringBuilder.replace(lastIndex, lastIndex+1, String.valueOf(ASSOCIATIVE_ARRAY_END_CHAR));	//replace the last delimiter with '}'
		}
		return stringBuilder;	//return the string builder
	}

	/**Encodes a string value.
	The characters {@value JavaScriptConstants#STRING_ENCODE_CHARS} will be replaced with {@value JavaScriptConstants#STRING_ENCODE_REPLACEMENT_STRINGS}, respectively. 
	@param charSequence The characters to encode.
	@return A string containing encoded characters.
	@exception NullPointerException if the given character sequence is <code>null</code>.
	*/
	public static String encodeStringValue(final CharSequence charSequence)
	{
		final StringBuilder stringBuilder=new StringBuilder(checkInstance(charSequence, "Character sequence cannot be null."));	//create a new string builder with the contents of the character sequence
		replace(stringBuilder, STRING_ENCODE_CHARS, STRING_ENCODE_REPLACEMENT_STRINGS);	//replace the encode characters with their encoded replacements
		return stringBuilder.toString();	//return the encoded string
	}

	/**Decodes a string value.
	Every instance of {@value JavaScriptConstants#ESCAPE_CHAR} will be removed if followed by another character and the subsequent character will be ignored. 
	@param charSequence The characters to encode.
	@return A string containing encoded characters.
	@exception NullPointerException if the given character sequence is <code>null</code>.
	@exception IllegalArgumentException if the character sequence ends with the given escape character.
	*/
	public static String decodeStringValue(final CharSequence charSequence)
	{
		return unescape(new StringBuilder(checkInstance(charSequence, "Character sequence cannot be null.")), ESCAPE_CHAR).toString();	//unescape the string
	}

	/**Serializes the given object in JSON.
	@param object The objectd to serialize.
	@return A string serialization of the given object.
	*/
	public static String serialize(final Object object)
	{
		return appendValue(new StringBuilder(), object).toString();	//serialize the given object and return the resulting string
	}

	/**Parses a value encoded in a JSON string.
	@param string The JSON string to be parsed.
	@return A new {@link String}, {@link Boolean}, {@link Number}, array, {@link Map}, or <code>null</code> representing the value represented by the string.
	@exception NullPointerException if the given string is <code>null</code>.
	@exception IllegalArgumentException if the given string does not represent a valid JSON object.
	*/
	public static Object parseValue(final String string)
	{
		if(string.length()==0)	//if the string is empty
		{
			throw new IllegalArgumentException("A JSON string cannot be empty.");
		}
		switch(string.charAt(0))	//see what the first character is
		{
			case ASSOCIATIVE_ARRAY_BEGIN_CHAR:
				throw new UnsupportedOperationException("Parsing JSON associative arrays not yet available.");
			case ARRAY_BEGIN_CHAR:
				return parseArray(string);
			case QUOTATION_MARK_CHAR:
				return parseString(string);
			default:
				if(TRUE.equals(string))	//if this is boolean true
				{
					return Boolean.TRUE;
				}
				else if(FALSE.equals(string))	//if this is boolean false
				{
					return Boolean.FALSE;
				}
				else if(NULL.equals(string))	//if this is null
				{
					return null;
				}
				else	//the only syntactic option left is a number
				{
					return parseNumber(string);
				}
		}
	}

	/**Parses an associative array encoded in a JSON string and delimited by {}.
	The current brute-force implementation only supports a single-level associative array with string values containing no semicolons or escaped characters.
	@param string The string to be parsed as an associative array.
	@return A new map representing the contents of the JSON associative array.
	@exception SyntaxException if the given string is not a syntactically correct JSON associative array.
	*/
/*TODO fix
	public static Map<String, Object> parseAssociativeArray(final String string) throws SyntaxException
	{
		if(!startsWith(string, ASSOCIATIVE_ARRAY_BEGIN_CHAR) || !endsWith(string, ASSOCIATIVE_ARRAY_END_CHAR))	//if the string doesn't start or doesn't end with the correct characters
		{
			throw new SyntaxException(string, "String is not a syntactically correct JSON associative array.");
		}
		final String[] nameValuePairStrings=string.substring(1, string.length()-1).split(String.valueOf(ASSOCIATIVE_ARRAY_DELIMITER));	//split out the name/value pairs
		for(final String nameValuePairString:nameValuePairStrings)	//for each name/value pair
		{
			final String[] nameValue=nameValuePairString.trim().split(String.valueOf(ASSOCIATIVE_ARRAY_KEY_VALUE_DELIMITER));	//split out the name and value
			if(nameValue.length!=2)	//if we didn't get a name and a value
			{
				throw new SyntaxException(nameValuePairString, "Illegal JSON name-value syntax.");
			}
		}
	}
*/

	/**Parses a string encoded in a JSON string and delimited by [].
	The current implementation does not support nested arrays, strings with the array delimiter, or strings with escaped characters.
	@param string The string to be parsed as an array.
	@exception NullPointerException if the given string is <code>null</code>.
	@exception IllegalArgumentException if the given string does not represent a valid JSON array.
	*/
	public static Object[] parseArray(final String string)
	{
		if(!startsWith(string, ARRAY_BEGIN_CHAR) || !endsWith(string, ARRAY_END_CHAR))	//if the string doesn't start or doesn't end with the correct character
		{
			throw new IllegalArgumentException("String is not a syntactically correct JSON array: "+string);
		}
		final String[] strings=string.substring(1, string.length()-1).split(String.valueOf(ARRAY_DELIMITER));	//split out the inidividual entries
		final int length=strings.length;	//see how many objects there are
		final Object[] objects=new Object[length];	//create a new array of objects
		for(int i=length-1; i>=0; --i)	//for each index
		{
			objects[i]=parseValue(strings[i].trim());	//parse this object and store it in the array
		}
		return objects;	//return the parsed array
	}

	/**Parses a string encoded in a JSON string and delimited by "".
	The current implementation does not support escaped characters.
	@param string The string to be parsed as a string.
	@exception NullPointerException if the given string is <code>null</code>.
	@exception IllegalArgumentException if the given string does not represent a valid JSON string.
	*/
	public static String parseString(final String string)
	{
		if(!startsWith(string, QUOTATION_MARK_CHAR) || !endsWith(string, QUOTATION_MARK_CHAR))	//if the string doesn't start or doesn't end with the correct character
		{
			throw new IllegalArgumentException("String is not a syntactically correct JSON string: "+string);
		}
		return string.substring(1, string.length()-1);	//return the string without the quotation marks
	}

	/**Parses a number encoded in a JSON string.
	@param string The string to be parsed as a number.
	@return A new number representing the value represented by the string.
	@exception NullPointerException if the given string is <code>null</code>.
	@exception IllegalArgumentException if the given string does not represent a valid JSON number.
	*/
	public static Number parseNumber(final String string)
	{
		if(string.indexOf('.')>=0)	//if the string contains a decimal point
		{
			return Double.valueOf(string);	//return a double
		}
			//TODO add support for real numbers
		else	//if the string doesn't contain a decimal
		{
			return Long.valueOf(string);	//return a long
		}
	}
}
