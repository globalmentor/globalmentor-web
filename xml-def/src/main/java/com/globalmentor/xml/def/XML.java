/*
 * Copyright © 1996-2014 GlobalMentor, Inc. <https://www.globalmentor.com/>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.globalmentor.xml.def;

import java.net.URI;
import java.nio.charset.*;
import java.util.*;

import javax.annotation.*;

import com.globalmentor.java.*;
import com.globalmentor.net.MediaType;

import static com.globalmentor.java.Characters.*;
import static com.globalmentor.java.Integers.*;
import static java.nio.charset.StandardCharsets.*;

/**
 * Various XML constants defined by the Extensible Markup Language (XML) specification.
 * @author Garret Wilson
 * @see <a href="http://www.w3.org/XML/">Extensible Markup Language (XML)</a>
 */
public class XML {

	/** The latest XML version supported. */
	public static final String XML_VERSION = "1.0";

	/** The suffix for XML application types, as defined in <a href="http://www.ietf.org/rfc/rfc3023.txt">RFC 3023</a>, "XML Media Types". */
	public static final String XML_SUBTYPE_SUFFIX = "xml";
	/** The suffix for XML external parsed entity subtypes (not yet formally defined). */
	public static final String XML_EXTERNAL_PARSED_ENTITY_SUBTYPE_SUFFIX = "xml-external-parsed-entity";

	/** The media type for generic XML: <code>text/xml</code>. */
	public static final MediaType MEDIA_TYPE = MediaType.of(MediaType.TEXT_PRIMARY_TYPE, "xml");

	/**
	 * The media type for a generic XML fragment: <code>text/xml-external-parsed-entity</code>.
	 * @see <a href="https://tools.ietf.org/html/rfc3023">RFC 3023: XML Media Types</a>
	 */
	public static final MediaType EXTERNAL_PARSED_ENTITY_MEDIA_TYPE = MediaType.of(MediaType.TEXT_PRIMARY_TYPE, "xml-external-parsed-entity");

	/** The default charset to assume if none is indicated. */
	public static final Charset DEFAULT_CHARSET = UTF_8;

	/** The filename extension for eXtensible Markup Language. */
	public static final String FILENAME_EXTENSION = "xml";

	/** The prefix to the "xml" namespace, for use with <code>xml:lang</code>, for example. */
	public static final String XML_NAMESPACE_PREFIX = "xml";

	/** The string representing the "xml" namespace. */
	public static final String XML_NAMESPACE_URI_STRING = "http://www.w3.org/XML/1998/namespace";

	/** The URI to the "xml" namespace. */
	public static final URI XML_NAMESPACE_URI = URI.create(XML_NAMESPACE_URI_STRING);

	/** The prefix to the "xmlns" namespace, for use with namespace declarations. */
	public static final String XMLNS_NAMESPACE_PREFIX = "xmlns"; //TODO makes sure code appropriately uses the new ATTRIBUTE_XMLNS when appropriate for the attribute name

	/** The language attribute <code>xml:lang</code>. */
	public static final NsName ATTRIBUTE_LANG = NsName.of(XML_NAMESPACE_URI_STRING, "lang");

	/**
	 * The space attribute <code>xml:space</code>.
	 * @see <a href="https://www.w3.org/TR/xml/#sec-white-space">Extensible Markup Language (XML) 1.0 (Fifth Edition), § 2.10 White Space Handling</a>
	 */
	public static final NsName ATTRIBUTE_SPACE = NsName.of(XML_NAMESPACE_URI_STRING, "space");
	/** The <code>xml:space</code> value indicating default whitespace handling. */
	public static final String ATTRIBUTE_SPACE_DEFAULT = "default";
	/** The <code>xml:space</code> value indicating whitespace should be preserved. */
	public static final String ATTRIBUTE_SPACE_PRESERVE = "preserve";

	/** The string representing the "xmlns" namespace. */
	public static final String XMLNS_NAMESPACE_URI_STRING = "http://www.w3.org/2000/xmlns/";

	/** The URI to the "xmlns" namespace. */
	public static final URI XMLNS_NAMESPACE_URI = URI.create(XMLNS_NAMESPACE_URI_STRING);

	/**
	 * The XML namespace attribute <code>xmlns</code>.
	 * @apiNote Note that the XML DOM considers the <code>xmlns</code> attribute to be in the {@value XML#XMLNS_NAMESPACE_URI_STRING} namespace, even though it
	 *          has no prefix.
	 */
	public static final NsName ATTRIBUTE_XMLNS = NsName.of(XMLNS_NAMESPACE_URI_STRING, "xmlns");

	/** The name of a CDATA section node. */
	public static final String CDATASECTION_NODE_NAME = "#cdata-section";
	/** The name of a comment node. */
	public static final String COMMENT_NODE_NAME = "#comment";
	/** The name of a document fragment node. */
	public static final String DOCUMENT_FRAGMENT_NODE_NAME = "#document-fragment";
	/** The name of a textnode. */
	public static final String TEXT_NODE_NAME = "#text";

	/** An attribute identifier in the resources. */
	public static final short ATTRIBUTE_RESOURCE_ID = 0;

	/** A document type identifier in the resources. */
	public static final short DOCUMENT_TYPE_RESOURCE_ID = 1;

	/** An entity declaration identifier in the resources. */
	public static final short ENTITY_DECLARATION_RESOURCE_ID = 2;

	/** An element declaration identifier in the resources. */
	public static final short ELEMENT_RESOURCE_ID = 3;

	/** A character reference identifier in the resources. */
	public static final short CHARACTER_REFERENCE_RESOURCE_ID = 4;

	/** An entity reference identifier in the resources. */
	public static final short ENTITY_REFERENCE_RESOURCE_ID = 5;

	/** A parameter entity reference identifier in the resources. */
	public static final short PARAMETER_ENTITY_REFERENCE_RESOURCE_ID = 6;

	/** A parameter entity reference identifier in the resources. */
	public static final short EXTERNAL_ID_RESOURCE_ID = 7;

	/** A entity identifier in the resources. */
	public static final short ENTITY_RESOURCE_ID = 8;

	/** A markup identifier in the resources. */
	public static final short MARKUP_RESOURCE_ID = 9;

	/** A markup identifier in the resources. */
	public static final short TAG_RESOURCE_ID = 10;

	/** A markup identifier in the resources. */
	public static final short XML_DECLARATION_RESOURCE_ID = 11;

	/** A markup identifier in the resources. */
	public static final short CDATA_RESOURCE_ID = 12;

	/** A markup identifier in the resources. */
	public static final short COMMENT_RESOURCE_ID = 13;

	/** A document identifier in the resources. */
	public static final short DOCUMENT_RESOURCE_ID = 14;

	/** A processing instruction identifier in the resources. */
	public static final short PROCESSING_INSTRUCTION_RESOURCE_ID = 15;

	/** An unknown identifier in the resources. */
	public static final short UNKNOWN_RESOURCE_ID = 99;

	/** A tab character. */
	public static final char TAB_CHAR = '\t';
	/** A carriage return character. */
	public static final char CR_CHAR = '\r';
	/** A linefeed character. */
	public static final char LF_CHAR = '\n';
	/** An equals sign. */
	public static final char EQUAL_CHAR = '=';
	/** A single quote character. */
	public static final char SINGLE_QUOTE_CHAR = '\'';
	/** A double quote character. */
	public static final char DOUBLE_QUOTE_CHAR = '"';

	/**
	 * The character to which any line break sequence is normalized during XML processing.
	 * @see <a href="https://www.w3.org/TR/xml/#sec-line-ends">Extensible Markup Language (XML) 1.0 (Fifth Edition), § 2.11 End-of-Line Handling</a>
	 */
	public static final char NORMALIZED_LINE_BREAK_CHAR = LF_CHAR;

	/**
	 * The characters considered by XML to be "whitespace": space, tab, <code>CR</code>, and <code>LF</code>.
	 * @see <a href="https://www.w3.org/TR/xml/#AVNormalize">Extensible Markup Language (XML) 1.0 (Fifth Edition), § 3.3.3 Attribute-Value Normalization</a>
	 */
	public static final Characters WHITESPACE_CHARACTERS = Characters.of(SPACE_CHAR, TAB_CHAR, CR_CHAR, LF_CHAR);

	/** Public ID characters. */
	public static final Characters PUBLIC_ID_CHARACTERS = Characters.of(SPACE_CHAR, CR_CHAR, LF_CHAR)
			.add("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-'()+,./:=?;!*#@$_%".toCharArray());

	/** The ranges considered by XML to be legal characters. */
	private static final char[][] CHAR_RANGES = new char[][] //each range is represented as two characters, the lower inclusive character of the range and the upper inclusive character of the range
	{{'\t', '\t'}, {'\n', '\n'}, {'\r', '\r'}, //tab, linefeed, and carriage return escaped in C fashion because including them as Unicode characters seems to make JBuilder try to parse them right away
			{'\u0020', '\uD7FF'}, {'\uE000', '\uFFFD'}, {'\uFFFE', '\uFFFF'}}; //TODO fix the last range to match XML's [#x10000-#x10FFFF]

	/** The characters considered by XML to be base characters. */
	private static final char[][] BASE_CHAR_RANGES = new char[][] //each range is represented as two characters, the lower inclusive character of the range and the upper inclusive character of the range
	{{'\u0041', '\u005A'}, {'\u0061', '\u007A'}, {'\u00C0', '\u00D6'}, {'\u00D8', '\u00F6'}, {'\u00F8', '\u00FF'}, {'\u0100', '\u0131'}, {'\u0134', '\u013E'},
			{'\u0141', '\u0148'}, {'\u014A', '\u017E'}, {'\u0180', '\u01C3'}, {'\u01CD', '\u01F0'}, {'\u01F4', '\u01F5'}, {'\u01FA', '\u0217'}, {'\u0250', '\u02A8'},
			{'\u02BB', '\u02C1'}, {'\u0386', '\u0386'}, {'\u0388', '\u038A'}, {'\u038C', '\u03BC'}, {'\u038E', '\u03A1'}, {'\u03A3', '\u03CE'}, {'\u03D0', '\u03D6'},
			{'\u03DA', '\u03DA'}, {'\u03DC', '\u03DC'}, {'\u03DE', '\u03DE'}, {'\u03E0', '\u03E0'}, {'\u03E2', '\u03F3'}, {'\u0401', '\u040C'}, {'\u040E', '\u044F'},
			{'\u0451', '\u045C'}, {'\u045E', '\u0481'}, {'\u0490', '\u04C4'}, {'\u04C7', '\u04C8'}, {'\u04CB', '\u04CC'}, {'\u04D0', '\u04EB'}, {'\u04EE', '\u04F5'},
			{'\u04F8', '\u04F9'}, {'\u0531', '\u0556'}, {'\u0559', '\u0559'}, {'\u0561', '\u0586'}, {'\u05D0', '\u05EA'}, {'\u05F0', '\u05F2'}, {'\u0621', '\u063A'},
			{'\u0641', '\u064A'}, {'\u0671', '\u06B7'}, {'\u06BA', '\u06BE'}, {'\u06C0', '\u06CE'}, {'\u06D0', '\u06D3'}, {'\u06D5', '\u06D5'}, {'\u06E5', '\u06E6'},
			{'\u0905', '\u0939'}, {'\u093D', '\u093D'}, {'\u0958', '\u0961'}, {'\u0985', '\u098C'}, {'\u098F', '\u0990'}, {'\u0993', '\u09A8'}, {'\u09AA', '\u09B0'},
			{'\u09B2', '\u09B2'}, {'\u09B6', '\u09B9'}, {'\u09DC', '\u09DD'}, {'\u09DF', '\u09E1'}, {'\u09F0', '\u09F1'}, {'\u0A05', '\u0A0A'}, {'\u0A0F', '\u0A10'},
			{'\u0A13', '\u0A28'}, {'\u0A2A', '\u0A30'}, {'\u0A32', '\u0A33'}, {'\u0A35', '\u0A36'}, {'\u0A38', '\u0A39'}, {'\u0A59', '\u0A5C'}, {'\u0A5E', '\u0A5E'},
			{'\u0A72', '\u0A74'}, {'\u0A85', '\u0A8B'}, {'\u0A8D', '\u0A8D'}, {'\u0A8F', '\u0A91'}, {'\u0A93', '\u0AA8'}, {'\u0AAA', '\u0AB0'}, {'\u0AB2', '\u0AB3'},
			{'\u0AB5', '\u0AB9'}, {'\u0ABD', '\u0ABD'}, {'\u0AE0', '\u0AE0'}, {'\u0B05', '\u0B0C'}, {'\u0B0F', '\u0B10'}, {'\u0B13', '\u0B28'}, {'\u0B2A', '\u0B30'},
			{'\u0B32', '\u0B33'}, {'\u0B36', '\u0B39'}, {'\u0B3D', '\u0B3D'}, {'\u0B5C', '\u0B5D'}, {'\u0B5F', '\u0B61'}, {'\u0B85', '\u0B8A'}, {'\u0B8E', '\u0B90'},
			{'\u0B92', '\u0B95'}, {'\u0B99', '\u0B9A'}, {'\u0B9C', '\u0B9C'}, {'\u0B9E', '\u0B9F'}, {'\u0BA3', '\u0BA4'}, {'\u0BA8', '\u0BAA'}, {'\u0BAE', '\u0BB5'},
			{'\u0BB7', '\u0BB9'}, {'\u0C05', '\u0C0C'}, {'\u0C0E', '\u0C10'}, {'\u0C12', '\u0C28'}, {'\u0C2A', '\u0C33'}, {'\u0C35', '\u0C39'}, {'\u0C60', '\u0C61'},
			{'\u0C85', '\u0C8C'}, {'\u0C8E', '\u0C90'}, {'\u0C92', '\u0CA8'}, {'\u0CAA', '\u0CB3'}, {'\u0CB5', '\u0CB9'}, {'\u0CDE', '\u0CDE'}, {'\u0CE0', '\u0CE1'},
			{'\u0D05', '\u0D0C'}, {'\u0D0E', '\u0D10'}, {'\u0D12', '\u0D28'}, {'\u0D2A', '\u0D39'}, {'\u0D60', '\u0D61'}, {'\u0E01', '\u0E2E'}, {'\u0E30', '\u0E30'},
			{'\u0E32', '\u0E33'}, {'\u0E40', '\u0E45'}, {'\u0E81', '\u0E82'}, {'\u0E84', '\u0E84'}, {'\u0E87', '\u0E88'}, {'\u0E8A', '\u0E8A'}, {'\u0E8D', '\u0E8D'},
			{'\u0E94', '\u0E97'}, {'\u0E99', '\u0E9F'}, {'\u0EA1', '\u0EA3'}, {'\u0EA5', '\u0EA5'}, {'\u0EA7', '\u0EA7'}, {'\u0EAA', '\u0EAB'}, {'\u0EAD', '\u0EAE'},
			{'\u0EB0', '\u0EB0'}, {'\u0EB2', '\u0EB3'}, {'\u0EBD', '\u0EBD'}, {'\u0EC0', '\u0EC4'}, {'\u0F40', '\u0F47'}, {'\u0F49', '\u0F69'}, {'\u10A0', '\u10C5'},
			{'\u10D0', '\u10F6'}, {'\u1100', '\u1100'}, {'\u1102', '\u1103'}, {'\u1105', '\u1107'}, {'\u1109', '\u1109'}, {'\u110B', '\u110C'}, {'\u110E', '\u1112'},
			{'\u113C', '\u113C'}, {'\u113E', '\u113E'}, {'\u1140', '\u1140'}, {'\u114C', '\u114C'}, {'\u114E', '\u114E'}, {'\u1150', '\u1150'}, {'\u1154', '\u1155'},
			{'\u1159', '\u1159'}, {'\u115F', '\u1161'}, {'\u1163', '\u1163'}, {'\u1165', '\u1165'}, {'\u1167', '\u1167'}, {'\u1169', '\u1169'}, {'\u116D', '\u116E'},
			{'\u1172', '\u1173'}, {'\u1175', '\u1175'}, {'\u119E', '\u119E'}, {'\u11A8', '\u11A8'}, {'\u11AB', '\u11AB'}, {'\u11AE', '\u11AF'}, {'\u11B7', '\u11B8'},
			{'\u11BA', '\u11BA'}, {'\u11BC', '\u11C2'}, {'\u11EB', '\u11EB'}, {'\u11F0', '\u11F0'}, {'\u11F9', '\u11F9'}, {'\u1E00', '\u1E9B'}, {'\u1EA0', '\u1EF9'},
			{'\u1F00', '\u1F15'}, {'\u1F18', '\u1F1D'}, {'\u1F20', '\u1F45'}, {'\u1F48', '\u1F4D'}, {'\u1F50', '\u1F57'}, {'\u1F59', '\u1F59'}, {'\u1F5B', '\u1F5B'},
			{'\u1F5D', '\u1F5D'}, {'\u1F5F', '\u1F7D'}, {'\u1F80', '\u1FB4'}, {'\u1FB6', '\u1FBC'}, {'\u1FBE', '\u1FBE'}, {'\u1FC2', '\u1FC4'}, {'\u1FC6', '\u1FCC'},
			{'\u1FD0', '\u1FD3'}, {'\u1FD6', '\u1FDB'}, {'\u1FE0', '\u1FEC'}, {'\u1FF2', '\u1FF4'}, {'\u1FF6', '\u1FFC'}, {'\u2126', '\u2126'}, {'\u212A', '\u212B'},
			{'\u212E', '\u212E'}, {'\u2180', '\u2182'}, {'\u3041', '\u3094'}, {'\u30A1', '\u30FA'}, {'\u3105', '\u312C'}, {'\uAC00', '\uD7A3'}};

	/** The characters considered by XML to be ideographic characters. */
	private static final char[][] IDEOGRAPHIC_RANGES = new char[][] {{'\u4E00', '\u9FA5'}, {'\u3007', '\u3007'}, {'\u3021', '\u3029'}}; //each range is represented as two characters, the lower inclusive character of the range and the upper inclusive character of the range

	/** The characters considered by XML to be combining characters. */
	private static final char[][] COMBINING_CHAR_RANGES = new char[][] //each range is represented as two characters, the lower inclusive character of the range and the upper inclusive character of the range
	{{'\u0300', '\u0345'}, {'\u0360', '\u0361'}, {'\u0483', '\u0486'}, {'\u0591', '\u05A1'}, {'\u05A3', '\u05B9'}, {'\u05BB', '\u05BD'}, {'\u05BF', '\u05BF'},
			{'\u05C1', '\u05C2'}, {'\u05C4', '\u05C4'}, {'\u064B', '\u0652'}, {'\u0670', '\u0670'}, {'\u06D6', '\u06DC'}, {'\u06DD', '\u06DF'}, {'\u06E0', '\u06E4'},
			{'\u06E7', '\u06E8'}, {'\u06EA', '\u06ED'}, {'\u0901', '\u0903'}, {'\u093C', '\u093C'}, {'\u093E', '\u094C'}, {'\u094D', '\u094D'}, {'\u0951', '\u0954'},
			{'\u0962', '\u0963'}, {'\u0981', '\u0983'}, {'\u09BC', '\u09BC'}, {'\u09BE', '\u09BE'}, {'\u09BF', '\u09BF'}, {'\u09C0', '\u09C4'}, {'\u09C7', '\u09C8'},
			{'\u09CB', '\u09CD'}, {'\u09D7', '\u09D7'}, {'\u09E2', '\u09E3'}, {'\u0A02', '\u0A02'}, {'\u0A3C', '\u0A3C'}, {'\u0A3E', '\u0A3E'}, {'\u0A3F', '\u0A3F'},
			{'\u0A40', '\u0A42'}, {'\u0A47', '\u0A48'}, {'\u0A4B', '\u0A4D'}, {'\u0A70', '\u0A71'}, {'\u0A81', '\u0A83'}, {'\u0ABC', '\u0ABC'}, {'\u0ABE', '\u0AC5'},
			{'\u0AC7', '\u0AC9'}, {'\u0ACB', '\u0ACD'}, {'\u0B01', '\u0B03'}, {'\u0B3C', '\u0B3C'}, {'\u0B3E', '\u0B43'}, {'\u0B47', '\u0B48'}, {'\u0B4B', '\u0B4D'},
			{'\u0B56', '\u0B57'}, {'\u0B82', '\u0B83'}, {'\u0BBE', '\u0BC2'}, {'\u0BC6', '\u0BC8'}, {'\u0BCA', '\u0BCD'}, {'\u0BD7', '\u0BD7'}, {'\u0C01', '\u0C03'},
			{'\u0C3E', '\u0C44'}, {'\u0C46', '\u0C48'}, {'\u0C4A', '\u0C4D'}, {'\u0C55', '\u0C56'}, {'\u0C82', '\u0C83'}, {'\u0CBE', '\u0CC4'}, {'\u0CC6', '\u0CC8'},
			{'\u0CCA', '\u0CCD'}};
	/*TODO fix
	| [#x0CD5-#x0CD6] | [#x0D02-#x0D03] | [#x0D3E-#x0D43] | [#x0D46-#x0D48] | [#x0D4A-#x0D4D] | #x0D57 | #x0E31 | [#x0E34-#x0E3A] | [#x0E47-#x0E4E] | #x0EB1 | [#x0EB4-#x0EB9] | [#x0EBB-#x0EBC] | [#x0EC8-#x0ECD] | [#x0F18-#x0F19] | #x0F35 | #x0F37 | #x0F39 | #x0F3E | #x0F3F | [#x0F71-#x0F84] | [#x0F86-#x0F8B] | [#x0F90-#x0F95] | #x0F97 | [#x0F99-#x0FAD] | [#x0FB1-#x0FB7] | #x0FB9 | [#x20D0-#x20DC] | #x20E1 | [#x302A-#x302F] | #x3099 | #x309A
	*/

	/** The characters considered by XML to be digits. */
	private static final char[][] DIGIT_RANGES = new char[][] //each range is represented as two characters, the lower inclusive character of the range and the upper inclusive character of the range
	{{'\u0030', '\u0039'}, {'\u0660', '\u0669'}, {'\u06F0', '\u06F9'}, {'\u0966', '\u096F'}, {'\u09E6', '\u09EF'}, {'\u0A66', '\u0A6F'}, {'\u0AE6', '\u0AEF'},
			{'\u0B66', '\u0B6F'}, {'\u0BE7', '\u0BEF'}, {'\u0C66', '\u0C6F'}, {'\u0CE6', '\u0CEF'}, {'\u0D66', '\u0D6F'}, {'\u0E50', '\u0E59'}, {'\u0ED0', '\u0ED9'},
			{'\u0F20', '\u0F29'}};
	/** The characters considered by XML to be extenders. */
	private static final char[][] EXTENDER_RANGES = new char[][] //each range is represented as two characters, the lower inclusive character of the range and the upper inclusive character of the range
	{{'\u00B7', '\u00B7'}, {'\u02D0', '\u02D0'}, {'\u02D1', '\u02D1'}, {'\u0387', '\u0387'}, {'\u0640', '\u0640'}, {'\u0E46', '\u0E46'}, {'\u0EC6', '\u0EC6'},
			{'\u3005', '\u3005'}, {'\u3031', '\u3035'}, {'\u309D', '\u309E'}, {'\u30FC', '\u30FE'}};

	/** The characters which can serve as delimiters for an attribute value. */
	public static final String ATTRIBUTE_VALUE_DELIMITERS = String.valueOf(SINGLE_QUOTE_CHAR) + String.valueOf(DOUBLE_QUOTE_CHAR);

	/** The characters which begin an attribute list declaration. */
	public static final String ATTLIST_DECL_START = "<!ATTLIST";
	/** The characters which end an attribute list declaration. */
	public static final String ATTLIST_DECL_END = ">";
	/** The "CDATA" attribute type. */
	public static final String ATTTYPE_CDATA = "CDATA";
	/** The "ID" attribute type. */
	public static final String ATTTYPE_ID = "ID";
	/** The "IDREF" attribute type. */
	public static final String ATTTYPE_IDREF = "IDREF";
	/** The "IDREFS" attribute type. */
	public static final String ATTTYPE_IDREFS = "IDREFS";
	/** The "ENTITY" attribute type. */
	public static final String ATTTYPE_ENTITY = "ENTITY";
	/** The "ENTITIES" attribute type. */
	public static final String ATTTYPE_ENTITIES = "ENTITIES";
	/** The "NMTOKEN" attribute type. */
	public static final String ATTTYPE_NMTOKEN = "NMTOKEN";
	/** The "NMTOKENS" attribute type. */
	public static final String ATTTYPE_NMTOKENS = "NMTOKENS";
	/** The notation type attribute type. */
	public static final String ATTTYPE_NOTATION = "NOTATION";
	/** The start of an enumerated attribute type. */
	public static final String ATTTYPE_ENUMERATION_START = "(";
	/** The end of an enumerated attribute type. */
	public static final String ATTTYPE_ENUMERATION_END = ")";
	/** The required attribute default declaration. */
	public static final String ATT_DEFAULT_REQUIRED = "#REQUIRED";
	/** The implied attribute default declaration. */
	public static final String ATT_DEFAULT_IMPLIED = "#IMPLIED";
	/** The fixed attribute default declaration. */
	public static final String ATT_DEFAULT_FIXED = "#FIXED";
	/** The characters which begin an entity reference. */
	public static final String ENTITY_REF_START = "&";
	/** The characters which end an entity reference. */
	public static final String ENTITY_REF_END = ";";
	/** The characters which begin a character reference. */
	public static final String CHARACTER_REF_START = "&#";
	/** The character which indicates a hexadecimal character reference. */
	public static final String CHARACTER_REF_HEX_FLAG = "x";
	/** The characters which end a character reference. */
	public static final String CHARACTER_REF_END = ";";
	/** The characters which begin a CDATA section. */
	public static final String CDATA_START = "<![CDATA[";
	/** The characters which end a CDATA section. */
	public static final String CDATA_END = "]]>";
	/** The characters which begin a comment section. */
	public static final String COMMENT_START = "<!--";
	/**
	 * The first set of characters which end a comment section (because the string "--" cannot appear inside a comment for campatibility reasons).
	 */
	public static final String COMMENT_END_PART1 = "--";
	/**
	 * The first set of characters which end a comment section (because the string "--" cannot appear inside a comment for campatibility reasons).
	 */
	public static final String COMMENT_END_PART2 = ">";
	/** The entire set of characters which end a comment section. */
	public static final String COMMENT_END = COMMENT_END_PART1 + COMMENT_END_PART2;
	/** The name which identifies a document type declaration section, it it were to be considered a tag. */
	public static final String DOCTYPE_DECL_NAME = "!DOCTYPE";
	/** The characters which begin a document type declaration section. */
	public static final String DOCTYPE_DECL_START = "<" + DOCTYPE_DECL_NAME;
	/** The characters which end a document type declaration section. */
	public static final String DOCTYPE_DECL_END = ">";
	/** The name of the document type system ID label. */
	public static final String SYSTEM_ID_NAME = "SYSTEM";
	/** The name of the document type public ID label. */
	public static final String PUBLIC_ID_NAME = "PUBLIC";
	/** DTD: A string indicating an element can have any content. */
	public static final String ELEMENT_CONTENT_ANY = "ANY";
	/** DTD: A string indicating an element must have no content. */
	public static final String ELEMENT_CONTENT_EMPTY = "EMPTY";
	/** The characters which begin an element declaration. */
	public static final String ELEMENT_TYPE_DECL_START = "<!ELEMENT";
	/** The characters which end an element declaration. */
	public static final String ELEMENT_TYPE_DECL_END = ">";
	/** The characters which begin an entity declaration. */
	public static final String ENTITY_DECL_START = "<!ENTITY";
	/** The characters which end an entity declaration. */
	public static final String ENTITY_DECL_END = ">";
	/** The characters which begin an internal document type declaration subset. */
	public static final String INTERNAL_DTD_SUBSET_START = "[";
	/** The characters which end a document type declaration internal subset. */
	public static final String INTERNAL_DTD_SUBSET_END = "]";
	/** The characters which begin a parameter entity reference. */
	public static final String PARAMETER_ENTITY_REF_START = "%";
	/** The characters which end a parameter entity reference. */
	public static final String PARAMETER_ENTITY_REF_END = ";";
	/** The characters which begin a processing instruction. */
	public static final String PROCESSING_INSTRUCTION_START = "<?";
	/** The characters which end a processing instruction. */
	public static final String PROCESSING_INSTRUCTION_END = "?>";
	/** The characters which begin a tag. */
	public static final String TAG_START = "<";
	/** The characters which end a tag. */
	public static final String TAG_END = ">";
	/** The characters which marks a tag as being an end tag or an empty element tag. */
	public static final char END_TAG_IDENTIFIER_CHAR = '/';
	/** The characters which begin an XML declaration section. */
	public static final String XML_DECL_START = "<?xml";
	/** The characters which end an XML declaration section. */
	public static final String XML_DECL_END = "?>";
	/** The name of the version info attribute of an XML declaration section. */
	public static final String VERSIONINFO_NAME = "version";
	/** The name of the encoding declaration attribute of an XML declaration section. */
	public static final String ENCODINGDECL_NAME = "encoding";
	/** The name of the standalone attribute of an XML declaration section. */
	public static final String SDDECL_NAME = "standalone";

	//default entities
	/** The lt entity name. */
	public static final String ENTITY_LT_NAME = "lt";
	/** The lt entity value. */
	public static final char ENTITY_LT_VALUE = '<';
	/** The gt entity name. */
	public static final String ENTITY_GT_NAME = "gt";
	/** The gt entity value. */
	public static final char ENTITY_GT_VALUE = '>';
	/** The amp entity name. */
	public static final String ENTITY_AMP_NAME = "amp";
	/** The amp entity value. */
	public static final char ENTITY_AMP_VALUE = '&';
	/** The apos entity name. */
	public static final String ENTITY_APOS_NAME = "apos";
	/** The apos entity value. */
	public static final char ENTITY_APOS_VALUE = '\'';
	/** The quot entity name. */
	public static final String ENTITY_QUOT_NAME = "quot";
	/** The quot entity value. */
	public static final char ENTITY_QUOT_VALUE = '"';

	/** The character values of the predefined entities. */
	public static final Characters PREDEFINED_ENTITY_CHARACTERS = Characters.of(ENTITY_LT_VALUE, ENTITY_GT_VALUE, ENTITY_AMP_VALUE, ENTITY_APOS_VALUE,
			ENTITY_QUOT_VALUE);

	/**
	 * Determines the name of the predefined entity associated with the given character.
	 * @param c The character to be replaced by a predefined entity.
	 * @return The name of the predefined entity for the given character.
	 * @throws IllegalArgumentException if the given character does not have a predefined entity as per the XML specification.
	 * @see #PREDEFINED_ENTITY_CHARACTERS
	 */
	public static final String getPredefinedEntityName(final char c) {
		switch(c) {
			case ENTITY_LT_VALUE:
				return ENTITY_LT_NAME;
			case ENTITY_GT_VALUE:
				return ENTITY_GT_NAME;
			case ENTITY_AMP_VALUE:
				return ENTITY_AMP_NAME;
			case ENTITY_APOS_VALUE:
				return ENTITY_APOS_NAME;
			case ENTITY_QUOT_VALUE:
				return ENTITY_QUOT_NAME;
			default:
				throw new IllegalArgumentException(
						String.format("Character %s is not one of the characters %s with a predefined entity.", c, PREDEFINED_ENTITY_CHARACTERS));
		}
	}

	/** Characters that must be encoded under normal circumstances (e.g. not in a CDATA section). */
	public static final Characters REQUIRED_ENCODE_CHRACTERS = Characters.of(ENTITY_LT_VALUE, ENTITY_GT_VALUE, ENTITY_AMP_VALUE);

	/** The character used for indicating namespaces. */
	public static final char NAMESPACE_DIVIDER = ':';

	/**
	 * Creates a character reference to represent the given characters.
	 * @param character The character to encode.
	 * @return A character reference in the form {@code &#xXX;}.
	 */
	public static String createCharacterReference(final char character) {
		final StringBuilder stringBuilder = new StringBuilder(CHARACTER_REF_START); //create a string builder with the start of a character reference
		stringBuilder.append(CHARACTER_REF_HEX_FLAG); //indicate that the character reference is in hex
		stringBuilder.append(toHexString(character, 4)); //write the hex value of the character
		stringBuilder.append(CHARACTER_REF_END); //end the character reference
		return stringBuilder.toString(); //return the constructed character reference
	}

	/**
	 * Returns the prefix from the qualified name.
	 * @param qualifiedName The fully qualified name, with an optional namespace prefix.
	 * @return The namespace prefix if present.
	 * @throws NullPointerException if the given qualified name is <code>null</code>.
	 */
	public static Optional<String> findPrefix(@Nonnull final String qualifiedName) {
		final int prefixDividerIndex = qualifiedName.indexOf(NAMESPACE_DIVIDER); //see if there is a prefix
		if(prefixDividerIndex >= 0) //if there is a prefix
			return Optional.of(qualifiedName.substring(0, prefixDividerIndex)); //return the prefix
		else { //if there is no prefix
			return Optional.empty(); //show that there is no prefix
		}
	}

	/**
	 * Retrieves the local name from a qualified name, removing the prefix, if any.
	 * @param qualifiedName The XML qualified name.
	 * @return The local name without a prefix.
	 * @throws NullPointerException if the given qualified name is <code>null</code>.
	 */
	@Nonnull
	public static String getLocalName(@Nonnull final String qualifiedName) {
		final int namespaceDividerIndex = qualifiedName.indexOf(NAMESPACE_DIVIDER); //find where the namespace divider is in the name
		return namespaceDividerIndex >= 0 ? //if there is a namespace prefix
				qualifiedName.substring(namespaceDividerIndex + 1) : //remove the namespace prefix
				qualifiedName; //otherwise, just return the qualified name since it didn't have a prefix to begin with
	}

	/**
	 * Checks to see if the specified character is a legal XML character.
	 * @param c The character to check.
	 * @return true if the character is a legal XML character.
	 */
	public static boolean isChar(final char c) {
		return Characters.isCharInRange(c, CHAR_RANGES); //see if the character is a legal XML character
	}

	/**
	 * Checks to see if the specified character is a letter.
	 * @param c The character to check.
	 * @return true if the character is an XML letter.
	 */
	public static boolean isLetter(final char c) {
		return Characters.isCharInRange(c, BASE_CHAR_RANGES) || Characters.isCharInRange(c, IDEOGRAPHIC_RANGES); //see if the character is a base character or an ideographic character
	}

	/**
	 * Checks to see if the specified character is a digit.
	 * @param c The character to check.
	 * @return true if the character is an XML digit.
	 */
	public static boolean isDigit(final char c) {
		return Characters.isCharInRange(c, DIGIT_RANGES); //see if the character is a digit
	}

	/**
	 * Checks to see if the specified character is a combining character.
	 * @param c The character to check.
	 * @return true if the character is an XML combining character.
	 */
	public static boolean isCombiningChar(final char c) {
		return Characters.isCharInRange(c, XML.COMBINING_CHAR_RANGES); //see if the character is a combining character
	}

	/**
	 * Checks to see if the specified character is an extender.
	 * @param c The character to check.
	 * @return true if the character is an XML extender.
	 */
	public static boolean isExtender(final char c) {
		return Characters.isCharInRange(c, XML.EXTENDER_RANGES); //see if the character is an extender
	}

	/**
	 * Checks to see if the specified character is a name character.
	 * @param c The character to check.
	 * @return <code>true</code> if the character is an XML name character.
	 */
	public static boolean isNameChar(final char c) {
		//first do a quick check for the most common name characters, because the rigorous search is very inefficient; most name characters will be in the restricted quick-test ranges
		if((c >= 'a' && c <= 'z') //'a'-'z'
				|| (c >= 'A' && c <= 'Z') //'A'-'Z'
				|| (c >= '0' && c <= '9') //'0'-'9'
				|| c == '.' || c == '-' || c == '_' || c == ':') { //'.', '-', '_', ':'
			return true; //this is a name character
		}
		return isLetter(c) || isDigit(c) || isCombiningChar(c) || isExtender(c); //only do a more rigorous check if the character isn't a common name character
	}

	/**
	 * Checks to see if the specified character is a valid character for the first character of an XML name.
	 * @param c The character to check.
	 * @return <code>true</code> if the character is an XML name first character.
	 */
	public static boolean isNameFirstChar(final char c) {
		return isLetter(c) || c == '_' || c == ':'; //the first character must be a letter, '_', or ':'
	}

	/**
	 * Checks to make sure the given name is valid.
	 * @param name The name to check for validity.
	 * @return <code>true</code> if the name is a valid XML name, else <code>false</code>.
	 */
	public static boolean isName(final String name) {
		if(name.length() == 0 || !isNameFirstChar(name.charAt(0))) //the first character must be a letter, '_', or ':'
			return false; //show that the name is invalid
		for(int i = 1; i < name.length(); ++i) { //look at each character in the name, skipping the first one since we already checked it
			if(!isNameChar(name.charAt(i))) //if one of the other letters is not a name character
				return false; //show that the name is invalid
		}
		return true; //since nothing was invalid, the name is valid
	}

	/**
	 * Creates a qualified name from a namespace prefix and a local name.
	 * @param prefix The namespace prefix, or <code>null</code> if there is no prefix.
	 * @param localName The XML local name.
	 * @return The XML qualified name.
	 */
	public static String createQualifiedName(@Nullable String prefix, @Nonnull final String localName) {
		if(prefix == null) {
			return localName;
		}
		return prefix + NAMESPACE_DIVIDER + localName;
	}

	/**
	 * Checks to make sure the given identifier is a valid target for a processing instruction.
	 * @param piTarget The identifier to check for validity.
	 * @return <code>true</code> if the name is a valid processing instruction target, else <code>false</code>.
	 */
	public static boolean isPITarget(final String piTarget) {
		return isName(piTarget) && !piTarget.equalsIgnoreCase("xml"); //a PI target is a valid name that does not equal to "XML" in any case
	}

	/**
	 * Retrieves the value of a processing instruction's pseudo attribute.
	 * @param processingInstructionData The data of a processing instruction.
	 * @param pseudoAttributeName The name of the pseudo attribute the value of which to retrieve.
	 * @return The string value of the given pseudo attribute, or <code>null</code> if that attribute is not defined in the processing instruction's data.
	 */
	public static String getProcessingInstructionPseudoAttributeValue(final String processingInstructionData, final String pseudoAttributeName) {
		final StringTokenizer tokenizer = new StringTokenizer(processingInstructionData, WHITESPACE_CHARACTERS.toString()); //create a tokenizer that separates tokens based on XML whitespace
		while(tokenizer.hasMoreTokens()) { //while there are more tokens
			final String token = tokenizer.nextToken(); //get the next token
			final int equalsIndex = token.indexOf(XML.EQUAL_CHAR); //get the index of the equal sign, if there is one
			if(equalsIndex != -1 && equalsIndex > 0 && token.length() >= equalsIndex + 2) { //if there is an equal sign, it's not the first character, and there's enough room for at least the quotes
				final char quoteChar = token.charAt(equalsIndex + 1); //get the character after the equals sign
				//if the character after the equals sign is one of the quote characters, and it matches the last character in the token
				if((quoteChar == XML.SINGLE_QUOTE_CHAR || quoteChar == XML.DOUBLE_QUOTE_CHAR) && token.charAt(token.length() - 1) == quoteChar) {
					final String currentPseudoAttributeName = token.substring(0, equalsIndex); //get the name of the attribute
					if(currentPseudoAttributeName.equals(pseudoAttributeName)) { //if this is the requested name
						//TODO this does not correctly interpret embedded entities; does XML require it?
						return token.substring(equalsIndex + 2, token.length() - 1); //return the value of the attribute
					}
				}
			}
		}
		return null; //show that we didn't find the requested pseudo attribute value
	}

}
