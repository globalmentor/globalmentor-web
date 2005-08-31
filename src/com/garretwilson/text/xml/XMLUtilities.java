package com.garretwilson.text.xml;

import java.io.*;
import java.lang.ref.*;
import java.util.*;
import javax.mail.internet.ContentType;

import com.garretwilson.lang.*;
import com.garretwilson.text.*;
import static com.garretwilson.text.xml.stylesheets.XMLStyleSheetConstants.*;
import com.garretwilson.util.Debug;
import org.w3c.dom.*;
import org.w3c.dom.traversal.*;

import static com.garretwilson.text.xml.mathml.MathMLConstants.*;
import static com.garretwilson.text.xml.svg.SVGConstants.*;
import static com.garretwilson.text.xml.xhtml.XHTMLConstants.*;

import static com.garretwilson.io.ContentTypeConstants.*;
import static com.garretwilson.io.ContentTypeUtilities.*;
import static com.garretwilson.lang.IntegerUtilities.*;
import static com.garretwilson.text.xml.XMLConstants.*;

/**Various XML manipuliating functions. The methods here are meant to be generic
in that they only access XML through the W3C DOM.
@author Garret Wilson
*/
public class XMLUtilities
{

	/**A lazily-created cache of system IDs keyed to public IDs.*/
	private static Reference<Map<String, String>> systemIDMapReference=null;

		/**A lazily-created cache of system IDs keyed to public IDs.*/
		protected static Map<String, String> getSystemIDMap()
		{
				//get the cache if we have one
			Map<String, String> systemIDMap=systemIDMapReference!=null ? systemIDMapReference.get() : null;
			if(systemIDMap==null)	//if the garbage collector has reclaimed the cache
			{
				systemIDMap=new HashMap<String, String>();	//create a new map of system IDs, and fill it with the default mappings
				systemIDMap.put(HTML_4_01_STRICT_PUBLIC_ID, HTML_4_01_STRICT_SYSTEM_ID);
				systemIDMap.put(HTML_4_01_TRANSITIONAL_PUBLIC_ID, HTML_4_01_TRANSITIONAL_SYSTEM_ID);
				systemIDMap.put(HTML_4_01_FRAMESET_PUBLIC_ID, HTML_4_01_FRAMESET_SYSTEM_ID);
				systemIDMap.put(XHTML_1_0_STRICT_PUBLIC_ID, XHTML_1_0_STRICT_SYSTEM_ID);
				systemIDMap.put(XHTML_1_0_TRANSITIONAL_PUBLIC_ID, XHTML_1_0_TRANSITIONAL_SYSTEM_ID);
				systemIDMap.put(XHTML_1_0_FRAMESET_PUBLIC_ID, XHTML_1_0_FRAMESET_SYSTEM_ID);
				systemIDMap.put(XHTML_1_1_PUBLIC_ID, XHTML_1_1_SYSTEM_ID);
				systemIDMap.put(XHTML_MATHML_SVG_PUBLIC_ID, XHTML_MATHML_SVG_SYSTEM_ID);
				systemIDMap.put(MATHML_2_0_PUBLIC_ID, MATHML_2_0_SYSTEM_ID);
				systemIDMap.put(SVG_1_0_PUBLIC_ID, SVG_1_0_SYSTEM_ID);
				systemIDMap.put(SVG_1_1_FULL_PUBLIC_ID, SVG_1_1_FULL_SYSTEM_ID);
				systemIDMap.put(SVG_1_1_BASIC_PUBLIC_ID, SVG_1_1_BASIC_SYSTEM_ID);
				systemIDMap.put(SVG_1_1_TINY_PUBLIC_ID, SVG_1_1_TINY_SYSTEM_ID);
				systemIDMapReference=new SoftReference<Map<String, String>>(systemIDMap);	//create a soft reference to the map
			}
			return systemIDMap;	//return the map
		}

		/**Determines the default system ID for the given public ID.
		@param publicID The public ID for which a doctype system ID should be retrieved.
		@return The default doctype system ID corresponding to the given public ID,
			or <code>null</code> if the given public ID is not recognized.
		*/
		public static String getDefaultSystemID(final String publicID)
		{
			return getSystemIDMap().get(publicID);	//return the system ID corresponding to the given public ID, if we have one
		}

	/**A lazily-created cache of content types keyed to public IDs.*/
	private static Reference<Map<String, ContentType>> contentTypeMapReference=null;

		/**A lazily-created cache of content types keyed to public IDs.*/
		protected static Map<String, ContentType> getContentTypeMap()
		{
				//get the cache if we have one
			Map<String, ContentType> contentTypeMap=contentTypeMapReference!=null ? contentTypeMapReference.get() : null;
			if(contentTypeMap==null)	//if the garbage collector has reclaimed the cache
			{
				contentTypeMap=new HashMap<String, ContentType>();	//create a new map of content types, and fill it with the default mappings
				contentTypeMap.put(HTML_4_01_STRICT_PUBLIC_ID, HTML_CONTENT_TYPE);
				contentTypeMap.put(HTML_4_01_TRANSITIONAL_PUBLIC_ID, HTML_CONTENT_TYPE);
				contentTypeMap.put(HTML_4_01_FRAMESET_PUBLIC_ID, HTML_CONTENT_TYPE);
				contentTypeMap.put(XHTML_1_0_STRICT_PUBLIC_ID, XHTML_CONTENT_TYPE);
				contentTypeMap.put(XHTML_1_0_TRANSITIONAL_PUBLIC_ID, XHTML_CONTENT_TYPE);
				contentTypeMap.put(XHTML_1_0_FRAMESET_PUBLIC_ID, XHTML_CONTENT_TYPE);
				contentTypeMap.put(XHTML_1_1_PUBLIC_ID, XHTML_CONTENT_TYPE);
				contentTypeMap.put(XHTML_MATHML_SVG_PUBLIC_ID, XHTML_CONTENT_TYPE);
				contentTypeMap.put(MATHML_2_0_PUBLIC_ID, MATHML_CONTENT_TYPE);
				contentTypeMap.put(SVG_1_0_PUBLIC_ID, SVG_CONTENT_TYPE);
				contentTypeMap.put(SVG_1_1_FULL_PUBLIC_ID, SVG_CONTENT_TYPE);
				contentTypeMap.put(SVG_1_1_BASIC_PUBLIC_ID, SVG_CONTENT_TYPE);
				contentTypeMap.put(SVG_1_1_TINY_PUBLIC_ID, SVG_CONTENT_TYPE);
				contentTypeMapReference=new SoftReference<Map<String, ContentType>>(contentTypeMap);	//create a soft reference to the map
			}
			return contentTypeMap;	//return the map
		}

		/**Determines the content type for the given public ID.
		@param publicID The public ID for which a content type should be retrieved.
		@return The content type corresponding to the given public ID,
			or <code>null</code> if the given public ID is not recognized.
		*/
		public static ContentType getContentType(final String publicID)
		{
			return getContentTypeMap().get(publicID);	//return the content type corresponding to the given public ID, if we have one
		}

	/**A lazily-created cache of root element local names keyed to content types base type names.*/
	private static Reference<Map<String, String>> rootElementLocalNameMapReference=null;

		/**A lazily-created cache of root element local names keyed to content types.*/
		protected static Map<String, String> getRootElementLocalNameMap()
		{
				//get the cache if we have one
			Map<String, String> rootElementLocalNameMap=rootElementLocalNameMapReference!=null ? rootElementLocalNameMapReference.get() : null;
			if(rootElementLocalNameMap==null)	//if the garbage collector has reclaimed the cache
			{
				rootElementLocalNameMap=new HashMap<String, String>();	//create a new map of root element local names, and fill it with the default mappings
				rootElementLocalNameMap.put(HTML_CONTENT_TYPE.getBaseType(), ELEMENT_HTML);
				rootElementLocalNameMap.put(XHTML_CONTENT_TYPE.getBaseType(), ELEMENT_HTML);
				rootElementLocalNameMap.put(MATHML_CONTENT_TYPE.getBaseType(), ELEMENT_MATHML);
				rootElementLocalNameMap.put(SVG_CONTENT_TYPE.getBaseType(), ELEMENT_SVG);
				rootElementLocalNameMapReference=new SoftReference<Map<String, String>>(rootElementLocalNameMap);	//create a soft reference to the map
			}
			return rootElementLocalNameMap;	//return the map
		}

		/**Determines the default root element local name for the given content type
		@param contentType The content type for which a root element should be retrieved.
		@return The default root element local name corresponding to the given
			media type, or <code>null</code> if the given content type is not recognized.
		*/
		public static String getDefaultRootElementLocalName(final ContentType contentType)
		{
			return getRootElementLocalNameMap().get(contentType.getBaseType());	//return the root element corresponding to the given content type base type, if we have one
		}

	/**Encodes text for use in XML by converting the five hard-coded XML entity
		characters into their corresponding entities. The encoded entities are the
		following:
		<ul>
		  <li>&amp;quot; (&quot;)</li>
		  <li>&amp;apos; (&apos;)</li>
		  <li>&amp;gt; (&gt;)</li>
		  <li>&amp;lt; (&lt;)</li>
		  <li>&amp;amp; (&amp;)</li>
		</ul>
	@param string A string which could contain XML character codes.
	@return A normal string.*/
/*G***del; this is probably not needed
	static public String fromXML(final String inString)
	{
		String outString=replace(inString, "&quot;", "\"");	//replace all occurences of &quot; with a quotes
		outString=replace(outString, "&apos;", "\'");	//replace all occurrences of &apos; with single quotes
		outString=replace(outString, "&gt;", ">");	//replace all occurrences of &gt; with a greater than sign
		outString=replace(outString, "&lt;", "<");	//replace all occurrences of &lt; with a less than sign
		outString=replace(outString, "#par;", "\n");	//replace all occurrences of #par; with a newline
		outString=replace(outString, "&amp;", "&");	//replace all occurrences of &amp; with an ampersand sign
		return outString;	//return our resulting string
	}
*/

	/**Creates a character reference to represent the given characters.
	@param character The character to encode.
	@return A character reference in the form <code>&#xXX;</code>.
	 */
	public static String createCharacterReference(final char character)
	{
		final StringBuilder stringBuilder=new StringBuilder(CHARACTER_REF_START);	//create a string builder with the start of a character reference
		stringBuilder.append(CHARACTER_REF_HEX_FLAG);	//indicate that the character reference is in hex
		stringBuilder.append(toHexString(character, 4));	//write the hex value of the character
		stringBuilder.append(CHARACTER_REF_END);	//end the character reference
		return stringBuilder.toString();	//return the constructed character reference
	}

	/**Returns the owner document of the given node or, if the node is a
		document, returns the node itself.
	@param node The node that may be a document or may have an owner document.
	@return The node owner document (which may be <code>null</code> if the node
		has no owner document) or, if the node is a docuent, the node itself.
	*/
	public static Document getDocument(final Node node)
	{
		return node.getNodeType()==Node.DOCUMENT_NODE ? (Document)node : node.getOwnerDocument(); //return the node if it is a document, otherwise return the node's owner document
	}

	/**Returns the first node in the hierarchy, beginning with the specified
		node and continuing with a depth-first search, that has a particular node
		type.
	@param node The node which will be checked for nodes, along with its
		children. The node's owner document should implement the
		<code>DocumentTraversal</code> interface.
	@param whatToShow Which node type(s) to return. See the
		description of <code>NodeFilter</code> for the set of possible
		<code>SHOW_</code> values. These flags can be combined using boolean OR.
	@return The first encountered node in a depth-first (document order) search
		that matches the filter criteria, or <code>null</code> if no matching node
		exists.
	@see NodeIterator
	@see NodeFilter
	*/
	public static Node getFirstNode(final Node node, final int whatToShow)
	{
			//create a node iterator that will only return the types of nodes we want
		final NodeIterator nodeIterator=((DocumentTraversal)node.getOwnerDocument()).createNodeIterator(node, whatToShow, null, false); //G***should we set expandEntityReferences to true or false? true?
		return nodeIterator.nextNode(); //get the next node (which will be the first node) and return it
	}


	/**Returns the text data of the first text node in the hierarchy, beginning
		with the specified node and continuing with a depth-first search.
	@param node The node which will be checked for text nodes, along with its
		children. The node's owner document should implement the
		<code>DocumentTraversal</code> interface.
	@return The text data of the first encountered text node in a depth-first
		(document order) search, or <code>null</code> if no text node exists.
	@see NodeIterator
	@see NodeFilter
G***should we return data from CDATA sections as well?
	*/
/*G***fix or del; this method is only useful for read-only functionality
	public static Node getFirstTextData(final Node node, final int whatToShow)
	{
		  NodeFilter.SHOW_TEXT|NodeFilter.SHOW_CDATA_SECTION
		  final Text textNode=(Text)XMLUtilities.getFirstNode(childNode, NodeFilter.SHOW_TEXT); //get the first text node in the element
			if(textNode!=null)  //if we found a text node somewhere inside this text node
			{
				//trim all beginning whitespace from the text node
				final String data=textNode.getData(); //get the text node data
*/

	/**Returns the prefix from the qualified name.
	@param qualifiedName The fully qualified name, with an optional namespace prefix.
	@return The namespace prefix, or <code>null</code> if no prefix is present.
	*/
	public static String getPrefix(final String qualifiedName)
	{
		if(qualifiedName!=null)	//if there is a qualified name given
		{
			final int prefixDividerIndex=qualifiedName.indexOf(NAMESPACE_DIVIDER);  //see if there is a prefix
			if(prefixDividerIndex>=0)  //if there is a prefix
				return qualifiedName.substring(0, prefixDividerIndex);  //return the prefix
			else  //if there is no prefix
				return null;  //show that there is no prefix
		}
		else	//if there is no qualified name
		{
			return null;	//there is no prefix
		}
	}

	/**Retrieves the local name from a qualified name, removing the prefix, if any.
	@param qualifiedName The XML qualified name.
	@return The local name without a prefix.
	*/
	public static String getLocalName(final String qualifiedName)
	{
		if(qualifiedName!=null)	//if there is a qualified name given
		{
			final int namespaceDividerIndex=qualifiedName.indexOf(NAMESPACE_DIVIDER); //find where the namespace divider is in the name
			return namespaceDividerIndex>=0 ? //if there is a namespace prefix
				  qualifiedName.substring(namespaceDividerIndex+1) :  //remove the namespace prefix
					qualifiedName;  //otherwise, just return the qualified name since it didn't have a prefix to begin with
		}
		else	//if there is no qualified name
		{
			return null;	//there is no local anme
		}
	}

	/**Returns the local name from the qualified name.
	@param qualifiedName The fully qualified name, with an optional namespace prefix.
	@return The local name, with the namespace prefix (if any) removed.
	*/
/*G***del
	public static String getLocalName(String qualifiedName)
	{
		final int prefixDividerIndex=qualifiedName.indexOf(XMLConstants.NAMESPACE_DIVIDER);  //see if there is a prefix
		if(prefixDividerIndex!=-1)  //if there is a prefix
		  qualifiedName=qualifiedName.substring(prefixDividerIndex+1);  //extract just the local name from the qualified name
		return qualifiedName; //return the qualified name with the prefix, if any, removed
	}
*/


	/**Returns the index of a given child node.
	@param nodeList The node list to search.
	@param node The node for which an index should be returned.
	@return The index of the node in the given node list, or -1 if the node does
		not appear in the node list.
	*/
//G***del if not needed	public static int childIndexOf(final NodeList nodeList, final Node node)

	/**Returns the index of a given node in a node list.
	@param nodeList The node list to search.
	@param node The node for which an index should be returned.
	@return The index of the node in the given node list, or -1 if the node does
		not appear in the node list.
	*/
	public static int indexOf(final NodeList nodeList, final Node node)
	{
		final int nodeCount=nodeList.getLength(); //see how many nodes there are
		for(int i=0; i<nodeCount; ++i)  //look at each node
		{
		  if(nodeList.item(i)==node)  //if the node at this index matches our node
				return i;  //show the index as which the node occurs
		}
		return -1;  //show that we were not able to find a matching node
	}

	/**Checks to see if the specified character is a legal XML character.
	@param c The character to check.
	@return true if the character is a legal XML character.
	*/
	static public boolean isChar(final char c)
	{
		return isCharInRange(c, CHAR_RANGES);	//see if the character is a legal XML character
	}

	/**Sees if the specified character is in one of the specified ranges.
	@param c The character to check.
	@param ranges A string of character pairs, <em>in order</em>, the first of each pair specifying the bottom inclusive character of a range, the second of which specifying the top inclusive character of the range.
	@return true if the character is in one of the ranges, else false.
	*/
	static protected boolean isCharInRange(final char c, final String ranges)
	{
		for(int i=0; i<ranges.length(); ++i)	//look at each character in the ranges string
		{
			if(c<ranges.charAt(i))	//if we've found a higher character than ours
			{
				if(i==0)	//if this is the first character
					return false;	//our character must be below all of the characters
				else if (c==ranges.charAt(i-1))	//if our character is equal to the previous character, it's in the range
					return true;	//since our character is equal to one of the characters, it has to be in one of the ranges
				else if((i & 1)!=0)	//if we're at an odd index, meaning that this is the second character in one of the pairs
					return true;	//we've established that: 1) our character is below the upper range character and 2) it is above the lower range character
				else	//if we get here, our character is between ranges
					return false;	//we've established that: 1) our character is below the lower range character of a range and 2) it is above the upper range character of the range below it
			}
		}
		return false;	//if we get here, this means our character is higher than any of the characters, meaning the character is higher than all the ranges
	}

	/**Checks to see if the specified character is XML whitespace.
	@param c The character to check.
	@return true if the character is XML whitespace.
	*/
	static public boolean isWhitespace(final char c)
	{
		return WHITESPACE_CHARS.indexOf(c)!=-1;	//if the character matches any characters in our whitespace string, the character is whitespace
	}

	/**Checks to see if the specified character is a letter.
	@param c The character to check.
	@return true if the character is an XML letter.
	*/
	static public boolean isLetter(final char c)
	{
		return isCharInRange(c, BASE_CHAR_RANGES) || isCharInRange(c, IDEOGRAPHIC_RANGES);	//see if the character is a base character or an ideographic character
	}

	/**Checks to see if the specified character is a digit.
	@param c The character to check.
	@return true if the character is an XML digit.
	*/
	static public boolean isDigit(final char c)
	{
		return isCharInRange(c, DIGIT_RANGES);	//see if the character is a digit
	}

	/**Checks to see if the specified character is a combining character.
	@param c The character to check.
	@return true if the character is an XML combining character.
	*/
	static public boolean isCombiningChar(final char c)
	{
		return isCharInRange(c, COMBINING_CHAR_RANGES);	//see if the character is a combining character
	}

	/**Checks to see if the specified character is an extender.
	@param c The character to check.
	@return true if the character is an XML extender.
	*/
	static public boolean isExtender(final char c)
	{
		return isCharInRange(c, EXTENDER_RANGES);	//see if the character is an extender
	}

	/**Checks to see if the specified character is a name character.
	@param c The character to check.
	@return <code>true</code> if the character is an XML name character.
	*/
	static public boolean isNameChar(final char c)
	{
		return isLetter(c) || isDigit(c) || c=='.' || c=='-' || c=='_' || c==':' || isCombiningChar(c) || isExtender(c);	//see if the character is a name character
	}

	/**Checks to see if the specified character is a valid character for the first
		character of an XML name.
	@param c The character to check.
	@return <code>true</code> if the character is an XML name first character.
	*/
	static public boolean isNameFirstChar(final char c)
	{
		return isLetter(c) || c=='_' || c==':';	//the first character must be a letter, '_', or ':'
	}

	/**Checks to make sure the given name is valid.
	@param name The name to check for validity.
	@return <code>true</code> if the name is a valid XML name, else <code>false</code>.
	*/
	static public boolean isName(final String name)
	{
//G***del when works		if(name.length()==0 || (!isLetter(name.charAt(0)) && name.charAt(0)!='_' && name.charAt(0)!=':'))	//the first character must be a letter, '_', or ':'
		if(name.length()==0 || !isNameFirstChar(name.charAt(0)))	//the first character must be a letter, '_', or ':'
			return false;	//show that the name is invalid
		for(int i=1; i<name.length(); ++i)	//look at each character in the name, skipping the first one since we already checked it
		{
			if(!isNameChar(name.charAt(i)))	//if one of the other letters is not a name character
				return false;	//show that the name is invalid
		}
		return true;	//since nothing was invalid, the name is valid
	}

	/**Determines if the given content type is one representing XML in some form.
	<p>XML media types include:</p>
	<ul>
		<li><code>text/xml</code></li>
		<li><code>application/xml</code></li>
		<li><code>application/*+xml</code></li>
	</ul>
	@param contentType The content type of a resource, or <code>null</code> for no
		content type.
	@return <code>true</code> if the given content type is one of several XML
		media types.
	*/ 
	public static boolean isXML(final ContentType contentType)
	{
		if(contentType!=null)	//if a content type is given
		{
			if(TEXT.equals(contentType.getPrimaryType()) && XML_SUBTYPE.equals(contentType.getSubType()))	//if this is "text/xml"
			{
				return true;	//text/xml is an XML content type
			}
			if(APPLICATION.equals(contentType.getPrimaryType()))	//if this is "application/*"
			{
				return XML_SUBTYPE.equals(contentType.getSubType())	//see if the subtype is "xml"
						|| hasSubTypeSuffix(contentType, XML_SUBTYPE_SUFFIX);	//see if the subtype has an XML suffix
			}
		}
		return false;	//this is not a media type we recognize as being HTML
	}


	/**The character to replace the first character if needed.*/
	protected final static char REPLACEMENT_FIRST_CHAR='x';

	/**The character to use to replace any other character.*/
	protected final static char REPLACEMENT_CHAR='_';


	/**Characters to be replaced, along with their replacements.*/  //G***move these up after moving the match character references (above) elsewhere
/*G***del
	protected final static char[][] CHARACTER_MATCH_REPLACE_SET_ARRAY=new char[][]
			  {
					{BAD_LEFT_SINGLE_QUOTE, LEFT_SINGLE_QUOTATION_MARK_CHAR},
					{BAD_RIGHT_SINGLE_QUOTE, RIGHT_SINGLE_QUOTATION_MARK_CHAR},
					{BAD_LEFT_DOUBLE_QUOTE, LEFT_DOUBLE_QUOTATION_MARK_CHAR},
					{BAD_RIGHT_DOUBLE_QUOTE, RIGHT_DOUBLE_QUOTATION_MARK_CHAR},
					{BAD_N_DASH, EN_DASH_CHAR},
					{BAD_M_DASH, EM_DASH_CHAR},
					{BAD_TRADEMARK, TRADE_MARK_SIGN_CHAR},
					{BAD_LOWERCASE_OE, LATIN_SMALL_LIGATURE_OE_CHAR},
					{BAD_UPPERCASE_OE, LATIN_CAPITAL_LIGATURE_OE_CHAR},
					{BAD_ELLIPSIS, HORIZONTAL_ELLIPSIS_CHAR},
					{BAD_UPPERCASE_Y_UMLAUT, LATIN_CAPITAL_LETTER_Y_WITH_DIAERESIS_CHAR}
//G***del if not needed					{NO_BREAK_SPACE_CHAR, SPACE_CHAR}
			  };
*/

	/**The special XML symbols that should be replaced with entities.*/
	private final static char[] XML_ENTITY_CHARS={'&', '"', '\'', '>', '<'}; //G***use constants

	/**The strings to replace XML symbols.*/
	private final static String[] XML_ENTITY_REPLACMENTS={"&amp;", "&quot;", "&apos;", "&gt;", "&lt;"};  //G***use constants

	/**Replaces special XML symbols with their escaped versions, (e.g. replaces
		'&lt' with "&amp;lt;") so that the string is valid XML content.
	@param string The string to be manipulated.
	@return An XML-friendly string.
	*/
	public static String createValidContent(final String string)
	{
		return StringUtilities.replace(string, XML_ENTITY_CHARS, XML_ENTITY_REPLACMENTS); //do the replacments for the special XML symbols and return the results
	}

	/**Creates a string in which all illegal XML characters are replaced with
		the space character.
	@param string The string the characters of which should be checked for XML
		validity.
	@return A new string with illegal XML characters replaced with spaces, or the
		original string if no characters were replaced.
	 */
	public static String createValidString(final String string)
	{
		StringBuffer stringBuffer=null; //we'll only create a string buffer if there are invalid characters
		for(int i=string.length()-1; i>=0; --i) //look at all the characters in the string
		{
			if(!isChar(string.charAt(i))) //if this is not a valid character
			{
				if(stringBuffer==null)  //if we haven't create a string buffer, yet
					stringBuffer=new StringBuffer(string);  //create a string buffer to hold our replacements
				stringBuffer.setCharAt(i, CharacterConstants.SPACE_CHAR); //replace this character with a space
			}
		}
		return stringBuffer!=null ? stringBuffer.toString() : string; //return the original string unless we've actually modified something
	}

	/**Creates an XML name by replacing every non-name character with an underscore
		('_') character. If the first character of the string cannot begin an XML
		name, it will be replaced with an 'x'. An empty string will receive
		an 'x' as well.
	@param string The string to be changed to an XML name.
	@return The string modified to be an XML name.
	*/
	public static String createName(final String string)
	{
		if(isName(string))  //if the string is already a name (we'll check all the characters, assuming that most of the time the strings will already be valid names, making this more efficient)
		  return string;  //return the string, because it doesn't need to be converted
		else  //if the string isn't a name already (we'll check all the characters, assuming that most of the time the strings will already be valid names, making this more efficient)
		{
			final StringBuffer stringBuffer=new StringBuffer(string); //create a string buffer from the string, so that we can modify it as necessary
			if(stringBuffer.length()==0)  //if the string isn't long enough to be a name
				stringBuffer.append(REPLACEMENT_FIRST_CHAR);  //put an 'x' in the first position
			else if(!isNameFirstChar(stringBuffer.charAt(0))) //if the string does have at least one character, but it's not a valid first character for an XML name
				stringBuffer.setCharAt(0, REPLACEMENT_FIRST_CHAR);  //replace the first character with an 'x'
			for(int i=1; i<string.length(); ++i)  //look at each character in the string, except the first (which we've already checked)
			{
				if(!isNameChar(stringBuffer.charAt(i)))  //if this character isn't a name character
					stringBuffer.setCharAt(i, REPLACEMENT_CHAR);  //replace the character with an underscore
			}
			return stringBuffer.toString(); //return the string we constructed
		}
	}

	/**Creates a qualified name from a namespace prefix and a local name.
	@param prefix The namespace prefix, or <code>null</code> if there is no prefix.
	@param localName The XML local name.
	@return The XML qualified name.
	*/
	public static String createQualifiedName(final String prefix, final String localName)
	{
		if(prefix!=null)  //if there is a prefix defined
		  return prefix+NAMESPACE_DIVIDER+localName;  //construct a qualified name from prefix and local name
		else  //if there is no prefix
			return localName; //return the local name without the prefix
	}

	/**Creates a qualified name object from an XML node.
	@param node The XML node from which a qualified name is to be created.
	@return A qualified name object representing the given XML node
	*/
	public static QualifiedName createQualifiedName(final Node node)
	{
		return new QualifiedName(node.getNamespaceURI(), node.getPrefix(), node.getLocalName());	//create a qualified name for this node
	}

	/**Checks to make sure the given identifier is a valid target for a processing instruction.
	@param piTarget The identifier to check for validity.
	@return <code>true</code> if the name is a valid processing instructin target,
		else <code>false</code>.
	*/
	static public boolean isPITarget(final String piTarget)
	{
		return isName(piTarget) && !piTarget.equalsIgnoreCase("xml");	//a PI target is a valid name that does not equal "XML" in any case
	}

	/**Returns a string representing the Unicode character in the form "#xXXXX".
	@return A string representing the Unicode character.
	@param c The Unicode character to be converted to a string.
	*/
/*G***del if not needed
	static public String unicodeString(final char c)
	{
		return "#x"+Integer.toHexString((int)c);	//convert the character to a hex value and prepend a hex character correct symbol
	}
*/

	/**Replaces the five default XML entity characters with their entity representations (e.g. replaces '"' with &quot;).
	@param inString A string which could contain special XML character.
	@return A string with the default XML entity representation.*/
/*G***del if not needed
	public static String encodeXML(final String inString)
	{		//convert the ampersands first, so that the ampersands added by the other conversions won't cause more &amp;s than intended
		String outString=StringUtilities.replace(inString, ENTITY_AMP_VALUE, ENTITY_REF_START+ENTITY_AMP_NAME+ENTITY_REF_END);	//replace all occurrences of an ampersand sign with &amp;
		outString=StringUtilities.replace(outString, ENTITY_LT_VALUE, ENTITY_REF_START+ENTITY_LT_NAME+ENTITY_REF_END);	//replace all occurrences of a less than sign with &lt;
		outString=StringUtilities.replace(outString, ENTITY_GT_VALUE, ENTITY_REF_START+ENTITY_GT_NAME+ENTITY_REF_END);	//replace all occurrences of a greater than sign with &gt;
		outString=StringUtilities.replace(outString, ENTITY_APOS_VALUE, ENTITY_REF_START+ENTITY_APOS_NAME+ENTITY_REF_END);	//replace all occurrences of a single quote with &apos;
		outString=StringUtilities.replace(outString, ENTITY_QUOT_VALUE, ENTITY_REF_START+ENTITY_QUOT_NAME+ENTITY_REF_END);	//replace all occurences of a quote with &quot;
		return outString;	//return our resulting string
	}
*/

	/**Adds a stylesheet to the XML document using the standard
		<code>&lt;<?xml-stylesheet...</code> processing instruction notation.
	@param document The document to which the stylesheet reference should be added.
	@param href The reference to the stylesheet.
	@param mediaType The media type of the stylesheet.
	*/
	public static void addStyleSheetReference(final Document document, final String href, final ContentType mediaType)
	{
		final String target=XML_STYLESHEET_PROCESSING_INSTRUCTION;  //the PI target will be the name of the stylesheet processing instruction
		final StringBuffer dataStringBuffer=new StringBuffer(); //create a string buffer to construct the data parameter (with its pseudo attributes)
		//add: href="href"
		dataStringBuffer.append(HREF_ATTRIBUTE).append(EQUAL_CHAR).append(DOUBLE_QUOTE_CHAR).append(href).append(DOUBLE_QUOTE_CHAR);
		dataStringBuffer.append(SPACE_CHAR);  //add a space between the pseudo attributes
		//add: type="type"
		dataStringBuffer.append(TYPE_ATTRIBUTE).append(EQUAL_CHAR).append(DOUBLE_QUOTE_CHAR).append(mediaType).append(DOUBLE_QUOTE_CHAR);
		final String data=dataStringBuffer.toString();  //convert the data string buffer to a string
		final ProcessingInstruction processingInstruction=document.createProcessingInstruction(target, data); //create a processing instruction with the correct information
		document.appendChild(processingInstruction);  //append the processing instruction to the document
	}

	/**Performs a clone on the children of the source node and adds them to the
		destination node.
	@param destinationNode The node that will receive the cloned child nodes.
	@param sourceNode The node from whence the nodes will be cloned.
	@param deep Whether each child should be deeply cloned.
	*/
//G***list exceptions
	public static void appendClonedChildNodes(final Node destinationNode, final Node sourceNode, final boolean deep)
	{
		final NodeList sourceNodeList=sourceNode.getChildNodes(); //get the list of child nodes
		final int sourceNodeCount=sourceNodeList.getLength(); //find out how many nodes there are
		for(int i=0; i<sourceNodeCount; ++i)  //look at each of the source nodes
		{
			final Node sourceChildNode=sourceNodeList.item(i); //get a reference to this child node
//G***del Debug.trace("ready to clone child node: "+sourceChildNode.getNodeName()+" with parent: "+sourceChildNode.getParentNode().getNodeName());
			destinationNode.appendChild(sourceChildNode.cloneNode(deep));  //clone the node and add it to the destination node
		}
	}

	/**Performs an import on the children of the source node and adds them to the destination node.
	@param destinationNode The node that will receive the imported child nodes.
	@param sourceNode The node from whence the nodes will be imported.
	@param deep Whether each child should be deeply imported.
	*/
	//G***list exceptions
	public static void appendImportedChildNodes(final Node destinationNode, final Node sourceNode, final boolean deep)
	{
		final Document destinationDocument=destinationNode.getOwnerDocument();	//get the owner document of the destination node
		final NodeList sourceNodeList=sourceNode.getChildNodes(); //get the list of child nodes
		final int sourceNodeCount=sourceNodeList.getLength(); //find out how many nodes there are
		for(int i=0; i<sourceNodeCount; ++i)  //look at each of the source nodes
		{
			final Node sourceChildNode=sourceNodeList.item(i); //get a reference to this child node
	//G***del Debug.trace("ready to clone child node: "+sourceChildNode.getNodeName()+" with parent: "+sourceChildNode.getParentNode().getNodeName());
			destinationNode.appendChild(destinationDocument.importNode(sourceChildNode, deep));  //import the node and add it to the destination node
		}
	}

	/**Performs a clone on the attributes of the source node and adds them to the
		destination node. It is assumed that all attributes have been added using
		namespace aware methods.
	@param destinationElement The element that will receive the cloned child nodes.
	@param sourceElement The element that contains the attributes to be cloned.
	*/
//G***list exceptions
	public static void appendClonedAttributeNodesNS(final Element destinationElement, final Element sourceElement)
	{
		final NamedNodeMap attributeNodeMap=sourceElement.getAttributes();  //get the source element's attributes
		final int sourceNodeCount=attributeNodeMap.getLength(); //find out how many nodes there are
		for(int i=0; i<sourceNodeCount; ++i)  //look at each of the source nodes
		{
			final Node sourceAttributeNode=attributeNodeMap.item(i); //get a reference to this attribute node
//G***del Debug.trace("ready to clone child node: "+sourceChildNode.getNodeName()+" with parent: "+sourceChildNode.getParentNode().getNodeName());
			destinationElement.setAttributeNodeNS((Attr)sourceAttributeNode.cloneNode(true)); //clone the attribute and add it to the destination element
		}
	}

	/**Creates a text node with the specified character and appends it to the specified element.
	@param element The element to which text should be added. This element must
		have a valid owner document.
	@param textCharacter The character to add to the element.
	@return The new text node that was created.
	*/
//G***list exceptions
	public static Text appendText(final Element element, final char textCharacter)
	{
		return appendText(element, String.valueOf(textCharacter));	//convert the character to a string and append it to the element
	}

	/**Creates a text node with the specified text and appends it to the specified element.
	@param element The element to which text should be added. This element must
		have a valid owner document.
	@param textString The text to add to the element.
	@return The new text node that was created.
	*/
//G***list exceptions
	public static Text appendText(final Element element, final String textString)
	{
		final Text textNode=element.getOwnerDocument().createTextNode(textString);	//create a new text node with the specified text
		element.appendChild(textNode);	//append the text node to our paragraph
		return textNode;	//return the text node we created
	}

	/**Convenience function to create an element and use it to replace the
		document element of the document.
	@param document The document which will serve as parent of the newly
		created element.
	@param elementNamespaceURI The namespace URI of the element to be created.
	@param elementName The name of the element to create.
	@return The newly created child element.
	*/
//G***list exceptions
	public static Element replaceDocumentElement(final Document document, final String elementNamespaceURI, final String elementName)
	{
		return replaceDocumentElement(document, elementNamespaceURI, elementName, null);  //append an element with no text
	}

	/**Convenience function to create an element, replace the document element 
		of the given document, and add optional text as a child of the given
		element. A heading, for instance, might be added using
		<code>replaceDocumentElement(document, XHTML_NAMESPACE_URI, ELEMENT_H2,
		"My Heading");</code>.
	@param document The document which will serve as parent of the newly
		created element.
	@param elementNamespaceURI The namespace URI of the element to be created.
	@param elementName The name of the element to create.
	@param textContent The text to add as a child of the created element, or
		<code>null</code> if no text should be added.
	@return The newly created child element.
	*/
//G***list exceptions
	public static Element replaceDocumentElement(final Document document, final String elementNamespaceURI, final String elementName, final String textContent)
	{
		final Element childElement=createElement(document, elementNamespaceURI, elementName, textContent);  //create the new element
		document.replaceChild(childElement, document.getDocumentElement());	//replace the document element of the document
		return childElement;  //return the element we created
	}

	/**Convenience function to create an element and add it as a child of the given
		parent element.
	@param parentElement The element which will serve as parent of the newly
		created element. This element must have a valid owner document.
	@param elementNamespaceURI The namespace URI of the element to be created.
	@param elementName The name of the element to create.
	@return The newly created child element.
	*/
//G***list exceptions
//G***change name to XXXNS
	public static Element appendElement(final Element parentElement, final String elementNamespaceURI, final String elementName)
	{
		return appendElement(parentElement, elementNamespaceURI, elementName, null);  //append the element with no text
	}

	/**Convenience function to create an element, add it as a child of the given
		parent element, and add optional text as a child of the given element. A
		heading, for instance, might be added using
		<code>appendElement(bodyElement, XHTML_NAMESPACE_URI, ELEMENT_H2,
		"My Heading");</code>.
	@param parentElement The element which will serve as parent of the newly
		created element. This element must have a valid owner document.
	@param elementNamespaceURI The namespace URI of the element to be created.
	@param elementName The name of the element to create.
	@param textContent The text to add as a child of the created element, or
		<code>null</code> if no text should be added.
	@return The newly created child element.
	*/
//G***list exceptions
//G***change name to XXXNS
	public static Element appendElement(final Element parentElement,
	    final String elementNamespaceURI, final String elementName, final String textContent)
	{
		final Element childElement=createElement(parentElement.getOwnerDocument(), elementNamespaceURI, elementName, textContent);  //create the new element
//G***del when works		final Element childElement=parentElement.getOwnerDocument().createElementNS(elementNamespaceURI, elementName);	//create the new element
		parentElement.appendChild(childElement);	//add the child element to the parent element
/*G***del when works
		if(textContent!=null) //if we have text content to add
		  appendText(childElement, textContent);	//append the text content to the newly created child element
*/
		return childElement;  //return the element we created
	}

	/**Creates a document wrapped around a copy of the given element.
	@param element The element to become the document element of the new document.
	@return A new document with a clone of the given element as the document
		element.
	*/
	public static Document createDocument(final Element element)
	{
		final DOMImplementation domImplementation=element.getOwnerDocument().getImplementation(); //get the DOM implementation used to create the document
		  //create a new document corresponding to the element
//G***bring over the doctype, if needed
		final Document document=domImplementation.createDocument(element.getNamespaceURI(), element.getNodeName(), null);
		final Node importedNode=document.importNode(element, true); //import the element into our new document
		document.replaceChild(importedNode, document.getDocumentElement());  //set the element clone as the document element of the new document
		return document;  //return the document we created
	}

	/**Convenience function to create an element and add optional text as a child
		of the given element. A heading, for instance, might be created using
		<code>appendElement(document, XHTML_NAMESPACE_URI, ELEMENT_H2,
		"My Heading");</code>.
	@param document The document to be used to create the new element.
	@param elementNamespaceURI The namespace URI of the element to be created.
	@param elementName The name of the element to create.
	@param textContent The text to add as a child of the created element, or
		<code>null</code> if no text should be added.
	@return The newly created child element.
	*/
//G***list exceptions
//G***change name to XXXNS
	public static Element createElement(final Document document,
	    final String elementNamespaceURI, final String elementName, final String textContent)
	{
		final Element childElement=document.createElementNS(elementNamespaceURI, elementName);	//create the new element
		if(textContent!=null) //if we have text content to add
		  appendText(childElement, textContent);	//append the text content to the newly created child element
		return childElement;  //return the element we created
	}

	/**Extracts all the child nodes from the given node and places them in a
		document fragment.
	@param node The node from which child nodes should be extracted. This node
		must have a valid owner document.
	@return A new document fragment containing the extracted children.
	@exception DOMException
	<ul>
		<li>NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly.</li>
	</ul>
	@see #removeChildren
	*/
	public static DocumentFragment extractChildren(final Node node) throws DOMException
	{
		return extractChildren(node, 0, node.getChildNodes().getLength());	//extract all the children and return the new document fragment 
	}

	/**Extracts the indexed nodes starting at <code>startChildIndex</code> up to
		but not including <code>endChildIndex</code>.
	@param node The node from which child nodes should be extracted. This node
		must have a valid owner document.
	@param startChildIndex The index of the first child to extract.
	@param endChildIndex The index directly after the last child to extract. Must
		be greater than <code>startChildIndex</code> or no action will occur.
	@return A new document fragment containing the extracted children.
	@exception ArrayIndexOutOfBoundsException Thrown if either index is negative,
		if the start index is greater than or equal to the number of children,
		or if the ending index is greater than the number of children (unless
		the ending index is not greater than the starting index).
	G***should we throw an exception is startChildIndex>endChildIndex, like String.substring()?
	@exception DOMException
	<ul>
		<li>NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly.</li>
	</ul>
	@see #removeChildren
	*/
	public static DocumentFragment extractChildren(final Node node, final int startChildIndex, final int endChildIndex) throws ArrayIndexOutOfBoundsException, DOMException
	{
		final NodeList childNodeList=node.getChildNodes();  //get a reference to the child nodes
		final int childNodeCount=childNodeList.getLength(); //find out how many child nodes there are
		if(startChildIndex<0 || (endChildIndex>startChildIndex && startChildIndex>=childNodeCount))  //if the start child index is out of range
			throw new ArrayIndexOutOfBoundsException(startChildIndex); //throw an exception indicating the illegal index
		if(endChildIndex<0 || (endChildIndex>startChildIndex && endChildIndex>childNodeCount))  //if the ending child index is out of range
			throw new ArrayIndexOutOfBoundsException(endChildIndex); //throw an exception indicating the illegal index
		final DocumentFragment documentFragment=node.getOwnerDocument().createDocumentFragment(); //create a document fragment to hold the nodes
		Node lastAddedNode=null; //show that we haven't added any nodes, yet
		for(int i=endChildIndex-1; i>=startChildIndex; --i)  //starting from the end, look at all the indexes before the ending index
		{
		  final Node childNode=node.removeChild(childNodeList.item(i)); //find the item at the given index and remove it
			if(lastAddedNode==null)  //for the first node we add
			  documentFragment.appendChild(childNode);  //append the removed child to the document fragment
			else  //for all other nodes
			  documentFragment.insertBefore(childNode, lastAddedNode);  //insert this child before the last one
			lastAddedNode=childNode;  //show that we just added another node
		}
		return documentFragment;  //return the document fragment we created
	}

	/**Gets the specified attribute value only if it is defined.
	@param element The element to check for the specified attribute.
	@param name The name of the attribute to retrieve.
	@return The attribute value, if the attribute is defined, else <code>null</code>.
	@see Element@hasAttribute
	@see Element@getAttribute
	*/
//G***list exceptions
	public static String getDefinedAttribute(final Element element, final String name)
	{
			//retrieve and return the attribute if it exists, else return null
		return element.hasAttribute(name) ? element.getAttribute(name) : null;
	}

	/**Gets the specified attribute value only if it is defined.
	@param element The element to check for the specified attribute.
	@param namespaceURI The namespace of the attribute to retrieve.
	@param localName The local name of the attribute.
	@return The attribute value, if the attribute is defined, else <code>null</code>.
	@see Element@hasAttributeNS
	@see Element@getAttributeNS
	*/
//G***list exceptions
	public static String getDefinedAttributeNS(final Element element, final String namespaceURI, final String localName)
	{
			//retrieve and return the attribute if it exists, else return null
		return element.hasAttributeNS(namespaceURI, localName) ? element.getAttributeNS(namespaceURI, localName) : null;
	}

	/**Returns a list of child nodes with a given type and
		node name. The special wildcard name "*" returns nodes of all names.
		If <code>deep</code> is set to <code>true</code>, returns a list of all
		descendant nodes with a given name, in the order in which they would be
		encountered in a preorder traversal of the node tree.
	@param node The node the child nodes of which will be searched.
	@param nodeType The type of nodes to include.
	@param nodeName The name of the node to match on. The special value "*" matches all nodes.
	@param deep Whether or not matching child nodes of each matching child node, etc. should be included.
	@return A new iterator object containing all the matching nodes.
	*/
	public static List getNodesByName(final Node node, final int nodeType, final String nodeName, final boolean deep)
	{
		final List nodeList=new ArrayList();  //crate a new node list to return
		final boolean matchAllNodes="*".equals(nodeName);	//see if they passed us the wildcard character G***use a constant here
		final NodeList childNodeList=node.getChildNodes();  //get the list of child nodes
		final int childNodeCount=childNodeList.getLength(); //get the number of child nodes
		for(int childIndex=0; childIndex<childNodeCount; childIndex++)	//look at each child node
		{
			final Node childNode=childNodeList.item(childIndex);	//get a reference to this node
			if(childNode.getNodeType()==nodeType)	//if this is a node of the correct type
			{
				if((matchAllNodes || childNode.getNodeName().equals(nodeName)))	//if node has the correct name (or they passed us the wildcard character)
					nodeList.add(childNode);	//add this node to the list
				if(deep)	//if each of the children should check for matching nodes as well
					nodeList.addAll(getNodesByName(childNode, nodeType, nodeName, deep));	//get this node's matching child nodes by name and add them to our list
			}
		}
		return nodeList;	//return the list we created and filled
	}

	/**Returns a list of child nodes with a given type, namespace URI, and local
		name. The special wildcard name "*" returns nodes of all local names.
		If <code>deep</code> is set to <code>true</code>, returns a list of all
		descendant nodes with a given name, in the order	in which they would be
		encountered in a preorder traversal of the node tree.
	@param node The node the child nodes of which will be searched.
	@param nodeType The type of nodes to include.
	@param namespaceURI The URI of the namespace of nodes to return. The special value "*" matches all namespaces.
	@param localName The local name of the node to match on. The special value "*" matches all local names.
	@param deep Whether or not matching child nodes of each matching child node, etc. should be included.
	@return A new list containing all the matching nodes.
	*/
	public static List getNodesByNameNS(final Node node, final int nodeType, final String namespaceURI, final String localName, final boolean deep)
	{
		final List nodeList=new ArrayList();	//create a new node list to return
		final boolean matchAllNamespaces="*".equals(namespaceURI);	//see if they passed us the wildcard character for the namespace URI G***use a constant here
		final boolean matchAllLocalNames="*".equals(localName);	//see if they passed us the wildcard character for the local name G***use a constant here
		final NodeList childNodeList=node.getChildNodes();  //get the list of child nodes
		final int childNodeCount=childNodeList.getLength(); //get the number of child nodes
		for(int childIndex=0; childIndex<childNodeCount; childIndex++)	//look at each child node
		{
			final Node childNode=childNodeList.item(childIndex);	//get a reference to this node
			if(childNode.getNodeType()==nodeType)	//if this is a node of the correct type
			{
				final String nodeNamespaceURI=childNode.getNamespaceURI(); //get the node's namespace URI
				final String nodeLocalName=childNode.getLocalName(); //get the node's local name
				if(matchAllNamespaces ||  //if we should match all namespaces
						((namespaceURI==null && nodeNamespaceURI==null) ||  //if both namespaces are null
					  (namespaceURI!=null && namespaceURI.equals(nodeNamespaceURI))) //if the namespace URI's match
				  ) //if we should match all namespaces, or the namespaces match
				{
					if(matchAllLocalNames || localName.equals(nodeLocalName)) //if we should match all local names, or the local names match
					{
						nodeList.add(childNode);	//add this node to the list
					}
				}
				if(deep)	//if each of the children should check for matching nodes as well
					nodeList.addAll(getNodesByNameNS(childNode, nodeType, namespaceURI, localName, deep));	//get this node's matching child nodes by name and add them to our list
			}
		}
		return nodeList;	//return the list we created and filled
	}


	/**Retrieves the value of a processing instruction's pseudo attribute.
	@param processingInstructionData The data of a processing instruction.
	@param pseudoAttributeName The name of the pseudo attribute the value of which
		to retrieve.
	@return The string value of the given pseudo attribute, or <code>null</code>
		if that attribute is not defined in the processing instruction's data.
	*/
	public static String getProcessingInstructionPseudoAttributeValue(final String processingInstructionData, final String pseudoAttributeName)
	{
		final StringTokenizer tokenizer=new StringTokenizer(processingInstructionData, WHITESPACE_CHARS);	//create a tokenizer that separates tokens based on XML whitespace
		while(tokenizer.hasMoreTokens())	//while there are more tokens
		{
			final String token=tokenizer.nextToken();	//get the next token
			final int equalsIndex=token.indexOf(EQUAL_CHAR);	//get the index of the equal sign, if there is one
			if(equalsIndex!=-1 && equalsIndex>0 && token.length()>=equalsIndex+2)	//if there is an equal sign, it's not the first character, and there's enough room for at least the quotes
			{
				final char quoteChar=token.charAt(equalsIndex+1);	//get the character after the equals sign
				//if the character after the equals sign is one of the quote characters, and it matches the last character in the token
				if((quoteChar==SINGLE_QUOTE_CHAR || quoteChar==DOUBLE_QUOTE_CHAR) && token.charAt(token.length()-1)==quoteChar)
				{
					final String currentPseudoAttributeName=token.substring(0, equalsIndex);	//get the name of the attribute
					if(currentPseudoAttributeName.equals(pseudoAttributeName))  //if this is the requested name
					{
							//G***this does not correctly interpret embedded entities; does XML require it?
						return token.substring(equalsIndex+2, token.length()-1);	//return the value of the attribute
					}
				}
			}
		}
		return null;  //show that we didn't find the requested pseudo attribute value
	}

	/**Retrieves the text of the element contained in child nodes of type
		<code>Node.Text</code>.
		If <code>deep</code> is set to <code>true</code> the text of all descendant
		nodes in document (depth-first) order; otherwise, only text of direct
		children will be returned.
	@param element The element from which text will be retrieved.
	@param deep Whether text of all descendents in documet order will be returned.
	@return The data of all <code>Text</code> children nodes.
	@see Node#TEXT_NODE
	@see Text#getData
	*/
	public static String getText(final Element element, final boolean deep)
	{
		final StringBuilder stringBuilder=new StringBuilder(); //create a string buffer to collect the text data
		getText(element, deep, stringBuilder); //collect the text in the string buffer
		return stringBuilder.toString(); //convert the string buffer to a string and return it
	}

	/**Retrieves the text of the element contained in child nodes of type
		<code>Node.Text</code>.
		If <code>deep</code> is set to <code>true</code> the text of all descendant
		nodes in document (depth-first) order; otherwise, only text of direct
		children will be returned.
	@param element The element from which text will be retrieved.
	@param deep Whether text of all descendents in documet order will be returned.
	@param stringBuilder The buffer to which text will be added.
	@see Node#TEXT_NODE
	@see Text#getData
	*/
	public static void getText(final Element element, final boolean deep, final StringBuilder stringBuilder)
	{
		final NodeList childNodeList=element.getChildNodes();  //get a reference to the child nodes
		final int childCount=childNodeList.getLength(); //find out how many children there are
		for(int i=0; i<childCount; ++i) //look at each of the children
		{
			final Node childNode=childNodeList.item(i); //get a reference to this node
			switch(childNode.getNodeType()) //see which type of node this is
			{
				case Node.TEXT_NODE:  //if this is a text node
					stringBuilder.append(((Text)childNode).getData()); //append this text node data to the string buffer
					break;
				case Node.ELEMENT_NODE: //if this is an element G***what about CDATA?
					if(deep)  //if we should get deep text
						getText((Element)childNode, deep, stringBuilder);  //append the text of this element
					break;
			}
		}
	}

	/**Creates and inserts a new element encompassing the text of a given text
		node.
	@param textNode The text node to split into a new element.
	@param element The element to insert.
	@param startIndex The index of the first character to include in the element.
	@param endIndex The index immediately after the last character to include
		in the element.
	@return The inserted element.
	@exception DOMException
	<ul>
		<li>INDEX_SIZE_ERR: Raised if the specified offset is negative or greater
			than the number of 16-bit units in <code>data</code>.</li>
		<li>NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly.</li>
	</ul>
	*/
	public static Element insertElement(final Text textNode, final Element element, final int startIndex, final int endIndex) throws DOMException
	{
		final Text splitTextNode=splitText(textNode, startIndex, endIndex); //split the text node into pieces
		final Element parentElement=(Element)splitTextNode.getParentNode(); //get the text node's parent
		parentElement.replaceChild(element, splitTextNode); //replace the split text node with the element that will enclose it
		element.appendChild(splitTextNode); //add the split text node to the given element
		return element; //return the inserted enclosing element
	}

	/**Splits a text node into one, two, or three text nodes and replaces the
		original text node with the new ones.
	@param textNode The text node to split.
	@param startIndex The index of the first character to be split.
	@param endIndex The index immediately after the last character to split.
	@return The new text node that contains the text selected by the start and
		ending indexes.
	@exception DOMException
	<ul>
		<li>INDEX_SIZE_ERR: Raised if the specified offset is negative or greater
			than the number of 16-bit units in <code>data</code>.</li>
		<li>NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly.</li>
	</ul>
	*/
	public static Text splitText(Text textNode, int startIndex, int endIndex) throws DOMException
	{
//G***del		final String data=textNode.getData(); //get the text data
//G***del		final Document document=textNode.getOwnerDocument();  //get the owner document
		if(startIndex>0)  //if the split text doesn't begin at the start of the text
		{
			textNode=textNode.splitText(startIndex);  //split off the first part of the text
			endIndex-=startIndex; //the ending index will now slide back because the start index is sliding back
		  startIndex=0; //we'll do the next split at the first of this string
		}
		if(endIndex<textNode.getLength()) //if there will be text left after the split
		{
			/*G***del final Text lastTextNode=*/textNode.splitText(endIndex); //split off the text after the node
//G***del Debug.trace("last split text node parent: ", lastTextNode.getParentNode());  //G***del
		}
		return textNode;  //return the node in the middle
	}

	/**Removes the specified child node from the parent node, and promoting all the
		children of the child node to be children of the parent node.
	@param parentNode The parent of the node to remove.
	@param childNode The node to remove, promoting its children in the process.
//G***list exceptions
	*/
	public static void pruneChild(final Node parentNode, final Node childNode)  //G***maybe rename to excise child
	{
			//promote all the child node's children to be children of the parent node
		while(childNode.hasChildNodes())  //while the child node has children
		{
			final Node node=childNode.getFirstChild();  //get the first child of the node
			childNode.removeChild(node);  //remove the child's child
			parentNode.insertBefore(node, childNode); //insert the child's child before its parent (the parent node's child)
		}
		parentNode.removeChild(childNode);  //remove the child, now that its children have been promoted
	}

	/**Removes the indexed nodes starting at <code>startChildIndex</code> up to
		but not including <code>endChildIndex</code>.
	@param node The node from which child nodes should be removed.
	@param startChildIndex The index of the first child to remove.
	@param endChildIndex The index directly after the last child to remove. Must
		be greater than <code>startChildIndex</code> or no action will occur.
	@exception ArrayIndexOutOfBoundsException Thrown if either index is negative,
		if the start index is greater than or equal to the number of children,
		or if the ending index is greater than the number of children.
	G***should we throw an exception is startChildIndex>endChildIndex, like String.substring()?
	@exception DOMException
	<ul>
		<li>NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly.</li>
	</ul>
	*/
	public static void removeChildren(final Node node, final int startChildIndex, final int endChildIndex) throws ArrayIndexOutOfBoundsException, DOMException
	{
		final NodeList childNodeList=node.getChildNodes();  //get a reference to the child nodes
		final int childNodeCount=childNodeList.getLength(); //find out how many child nodes there are
		if(startChildIndex<0 || startChildIndex>=childNodeCount)  //if the start child index is out of range
			throw new ArrayIndexOutOfBoundsException(startChildIndex); //throw an exception indicating the illegal index
		if(endChildIndex<0 || endChildIndex>childNodeCount)  //if the ending child index is out of range
			throw new ArrayIndexOutOfBoundsException(endChildIndex); //throw an exception indicating the illegal index
		for(int i=endChildIndex-1; i>=startChildIndex; --i)  //starting from the end, look at all the indexes before the ending index
		  node.removeChild(childNodeList.item(i)); //find the item at the given index and remove it
	}

	/**Renames an element by creating a new element with the specified name,
		cloning the original element's children, and replacing the original element
		with the new, renamed clone. While this method's purpose is renaming,
		because of DOM restrictions it must remove the element and replace it with
		a new one, which is reflected by the method's name.
	@param element The element to rename.
	@param namespaceURI The new element namespace.
	@param localName The new element local name.
	@return The new element with the specified name which replaced the old element.
//G***list exceptions
	*/
	public static Element replaceElementNS(final Element element, final String namespaceURI, final String localName)
	{
//G***del Debug.trace("replaceElementNS(), old element's parent: "+(element.getParentNode()!=null? element.getParentNode().getNodeName() : "null"));
		final Document document=element.getOwnerDocument(); //get the owner document
		final Element newElement=document.createElementNS(namespaceURI, localName);	//create the new element
		appendClonedAttributeNodesNS(newElement, element);  //clone the attributes G***testing
		appendClonedChildNodes(newElement, element, true);  //deep-clone the child nodes of the element and add them to the new element
		final Node parentNode=element.getParentNode();  //get the parent node, which we'll need for the replacement
//G***del Debug.assert(parentNode!=null, "Cannot replace element that has no parent."); //G***fix
		parentNode.replaceChild(newElement, element); //replace the old element with the new one
//G***del Debug.trace("replaceElementNS(), new element's parent: "+(newElement.getParentNode()!=null? newElement.getParentNode().getNodeName() : "null"));
		return newElement;  //return the element we created
	}

	//G***del	private static final int tabDelta=2;	//fix, G***comment
	private static final String tabString="|\t";	//G***fix to adjust automatically to tabDelta, comment

	/**Prints a tree representation of the document to the standard output.
	@param document The document to print.
	@param printStream The stream (e.g. <code>System.out</code>) to use for
		printing the tree.
	*/
	public static void printTree(final Document document, final PrintStream printStream)
	{
		if(document!=null)	//if we have a root element
			printTree(document.getDocumentElement(), 0, printStream);	//dump the contents of the root element
		else	//if we don't have a root element
			printStream.println("Empty document.");	//G***fix, comment, i18n
	}

	/**Prints a tree representation of the element to the standard output.
	@param element The element to print.
	@param printStream The stream (e.g. <code>System.out</code>) to use for
		printing the tree.
	*/
	public static void printTree(final Element element, final PrintStream printStream)
	{
		printTree(element, 0, printStream); //if we're called normally, we'll dump starting at the first tab position
	}

	/**Prints a tree representation of the element to the standard output starting
		at the specified tab position.
	@param element The element to print.
	@param tabPos The zero-based tab position to which to align.
	@param printStream The stream (e.g. <code>System.out</code>) to use for
		printing the tree.
	*/
	protected static void printTree(final Element element, int tabPos, final PrintStream printStream)
	{
		for(int i=0; i<tabPos; ++i)
			printStream.print(tabString);	//G***fix to adjust automatically to tabDelta, comment
		printStream.print("[Element] ");	//G**fix to adjust automatically to tabDelta, comment
		printStream.print("<"+element.getNodeName());  //print the element name

		final NamedNodeMap attributeMap=element.getAttributes();  //get the attributes
		for(int i=attributeMap.getLength()-1; i>=0; --i)  //look at each attribute
		{
			final Attr attribute=(Attr)attributeMap.item(i);  //get a reference to this attribute
			printStream.print(" "+attribute.getName()+"=\""+attribute.getValue()+"\"");  //print the attribute and its value
			printStream.print(" ("+attribute.getNamespaceURI()+")");  //print the attribute namespace
		}
		if(element.getChildNodes().getLength()==0)  //if there are no child nodes
			printStream.print('/');  //show that this is an empty element
		printStream.println("> (namespace URI=\""+element.getNamespaceURI()+"\" local name=\""+element.getLocalName()+"\")");
		if(element.getChildNodes().getLength()>0)  //if there are child nodes
		{
			for(int childIndex=0; childIndex<element.getChildNodes().getLength(); childIndex++)	//look at each child node
			{
				Node node=element.getChildNodes().item(childIndex);	//look at this node
				printTree(node, tabPos, printStream); //print the node to the stream
/*G***del when works
	//G***del			String charactersToAdd="";	//this will be the characters to add
				switch(node.getNodeType())	//see which type of object this is
				{
					case Node.ELEMENT_NODE:	//if this is an element

					//G***fix for empty elements

	//G***del tabPos+=tabDelta;	//G***check this; maybe static classes don't have recursive-aware functions
						printTree((Element)node, tabPos+1, printStream);	//comment, check to see if we need the typecast
	//G***del tabPos-=tabDelta;	//G***check this; maybe static classes don't have recursive-aware functions
						break;
					case Node.TEXT_NODE:	//if this is a text node
						for(int i=0; i<tabPos+1; ++i)
							printStream.print(tabString);	//G***fix to adjust automatically to tabDelta, comment
						printStream.print("[Text] ");	//G**fix to adjust automatically to tabDelta, comment
						printStream.println(StringUtilities.replace(node.getNodeValue(), '\n', "\\n"));	//print the text of this node
						break;
					case Node.COMMENT_NODE:	//if this is a comment node
						for(int i=0; i<tabPos+1; i+=++i)
							printStream.print(tabString);	//G***fix to adjust automatically to tabDelta, comment
						printStream.print("[Comment] ");	//G**fix to adjust automatically to tabDelta, comment
						printStream.println(StringUtilities.replace(node.getNodeValue(), '\n', "\\n"));	//print the text of this node
						break;
				}
*/
			//G***process the child elements
			}

			for(int i=0; i<tabPos; ++i)
				printStream.print(tabString);	//G***fix to adjust automatically to tabDelta, comment
			printStream.print("[/Element] ");	//G**fix to adjust automatically to tabDelta, comment
			printStream.println("</"+element.getNodeName()+'>');
		}
	}

	/**Prints a tree representation of the node to the given pring stream starting
		at the specified tab position.
	@param node The node to print.
	@param printStream The stream (e.g. <code>System.out</code>) to use for
		printing the tree.
	*/
	public static void printTree(final Node node, final PrintStream printStream)
	{
		printTree(node, 0, printStream); //if we're called normally, we'll dump starting at the first tab position
	}

	/**Prints a tree representation of the node to the given pring stream starting
		at the specified tab position.
	@param node The node to print.
	@param tabPos The zero-based tab position to which to align.
	@param printStream The stream (e.g. <code>System.out</code>) to use for
		printing the tree.
	*/
	protected static void printTree(final Node node, int tabPos, final PrintStream printStream)
	{
		switch(node.getNodeType())	//see which type of object this is
		{
			case Node.ELEMENT_NODE:	//if this is an element

			//G***fix for empty elements

//G***del tabPos+=tabDelta;	//G***check this; maybe static classes don't have recursive-aware functions
				printTree((Element)node, tabPos+1, printStream);	//comment, check to see if we need the typecast
//G***del tabPos-=tabDelta;	//G***check this; maybe static classes don't have recursive-aware functions
				break;
			case Node.TEXT_NODE:	//if this is a text node
				for(int i=0; i<tabPos+1; ++i)
					printStream.print(tabString);	//G***fix to adjust automatically to tabDelta, comment
				printStream.print("[Text] ");	//G**fix to adjust automatically to tabDelta, comment
				printStream.println(StringUtilities.replace(node.getNodeValue(), '\n', "\\n"));	//print the text of this node
				break;
			case Node.COMMENT_NODE:	//if this is a comment node
				for(int i=0; i<tabPos+1; i+=++i)
					printStream.print(tabString);	//G***fix to adjust automatically to tabDelta, comment
				printStream.print("[Comment] ");	//G**fix to adjust automatically to tabDelta, comment
				printStream.println(StringUtilities.replace(node.getNodeValue(), '\n', "\\n"));	//print the text of this node
				break;
		}
	}

	/**Converts an XML document to a string. If an error occurs converting the
		document to a string, the normal object string will be returned.
	@param document The XML document to convert.
	@return A string representation of the XML document.
	*/
	public static String toString(final Document document)
	{
		try
		{
		  return new XMLSerializer(true).serialize(document); //serialize the document to a string, formatting the XML output
		}
		catch(final IOException ioException)  //if an IO exception occurs
		{
			return ioException.getMessage()+' '+document.toString();  //ask the document to convert itself to a string
		}
	}

	/**Converts an XML element to a string. If an error occurs converting the
		element to a string, the normal object string will be returned.
	@param element The XML elment to convert.
	@return A string representation of the XML element.
	*/
	public static String toString(final Element element)
	{
		try
		{
		  return new XMLSerializer(true).serialize(element); //serialize the element to a string, formatting the XML output
		}
		catch(final IOException ioException)  //if an IO exception occurs
		{
			return ioException.getMessage()+' '+element.toString();  //ask the document to convert itself to a string
		}
	}

}
