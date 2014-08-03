/*
 * Copyright © 1996-2012 GlobalMentor, Inc. <http://www.globalmentor.com/>
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

import java.io.*;
import java.lang.ref.*;
import java.net.URI;
import java.util.*;

import javax.xml.parsers.*;

import com.globalmentor.config.ConfigurationException;
import static com.globalmentor.io.Charsets.*;
import com.globalmentor.java.*;
import com.globalmentor.java.Objects;
import com.globalmentor.model.NameValuePair;
import com.globalmentor.net.ContentType;
import com.globalmentor.net.URIs;
import com.globalmentor.text.xml.oeb.OEB;
import com.globalmentor.text.xml.xhtml.XHTML;

import org.w3c.dom.*;
import org.w3c.dom.traversal.*;
import org.xml.sax.EntityResolver;
import org.xml.sax.SAXException;

import static com.globalmentor.java.Characters.*;
import static com.globalmentor.java.Integers.*;
import static com.globalmentor.java.Objects.*;
import static com.globalmentor.net.ContentTypeConstants.*;
import static com.globalmentor.net.URIs.SCHEME_SEPARATOR;
import static com.globalmentor.text.xml.mathml.MathML.*;
import static com.globalmentor.text.xml.stylesheets.XMLStyleSheets.*;
import static com.globalmentor.text.xml.svg.SVG.*;
import static com.globalmentor.text.xml.xhtml.XHTML.*;

/**
 * Various XML constants and manipulation functions.
 * @author Garret Wilson
 */
public class XML {

	/** The latest XML version supported. */
	public static final String XML_VERSION = "1.0";

	/** The suffix for XML application types, as defined in <a href="http://www.ietf.org/rfc/rfc3023.txt">RFC 3023</a>, "XML Media Types". */
	public final static String XML_SUBTYPE_SUFFIX = "xml";
	/** The suffix for XML external parsed entity subtypes (not yet formally defined). */
	public final static String XML_EXTERNAL_PARSED_ENTITY_SUBTYPE_SUFFIX = "xml-external-parsed-entity";

	/** The content type for generic XML: <code>text/xml</code>. */
	public static final ContentType CONTENT_TYPE = ContentType.create(ContentType.TEXT_PRIMARY_TYPE, XML_SUBTYPE);

	/** The content type for a generic XML fragment: <code>text/xml-external-parsed-entity</code>. */
	public static final ContentType EXTERNAL_PARSED_ENTITY_CONTENT_TYPE = ContentType.create(ContentType.TEXT_PRIMARY_TYPE, XML_EXTERNAL_PARSED_ENTITY_SUBTYPE);

	/** The name extension for eXtensible Markup Language. */
	public final static String XML_NAME_EXTENSION = "xml";

	/** The prefix to the "xml" namespace, for use with "xml:lang", for example. */
	public static final String XML_NAMESPACE_PREFIX = "xml";

	/** The URI to the "xml" namespace. */
	public static final URI XML_NAMESPACE_URI = URI.create("http://www.w3.org/XML/1998/namespace");

	/** The prefix to the "xmlns" namespace, for use with namespace declarations. */
	public static final String XMLNS_NAMESPACE_PREFIX = "xmlns"; //TODO makes sure code appropriately uses the new ATTRIBUTE_XMLNS when appropriate for the attribute name

	/** The local name of the language attribute <code>xml:lang</code>. */
	public final static String ATTRIBUTE_LANG = "lang";

	/** The local name of the XML namespace attribute <code>xmlns</code>. */
	public final static String ATTRIBUTE_XMLNS = "xmlns";

	/** The URI to the "xmlns" namespace. */
	public static final URI XMLNS_NAMESPACE_URI = URI.create("http://www.w3.org/2000/xmlns/");

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
	public final static char TAB_CHAR = '\t';
	/** A carriage return character. */
	public final static char CR_CHAR = '\r';
	/** A linefeed character. */
	public final static char LF_CHAR = '\n';
	/** An equals sign. */
	public final static char EQUAL_CHAR = '=';
	/** A single quote character. */
	public final static char SINGLE_QUOTE_CHAR = '\'';
	/** A double quote character. */
	public final static char DOUBLE_QUOTE_CHAR = '"';
	/** The characters considered by XML to be whitespace. */
	public final static String WHITESPACE_CHARS = "" + SPACE_CHAR + TAB_CHAR + CR_CHAR + LF_CHAR; //whitespace characters in XML are space, tab, CR, and LF TODO upgrade to use Characters
	/** Public ID characters. */
	public final static String PUBLIC_ID_CHARS = SPACE_CHAR + CR_CHAR + LF_CHAR
			+ "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-'()+,./:=?;!*#@$_%";
	/*G***del when PUBLIC_ID_CHARS works
	#x20 | #xD | #xA | [a-zA-Z0-9] | [-'()+,./:=?;!*#@$_%]
	*/

	/** The ranges considered by XML to be legal characters. */
	public final static char[][] CHAR_RANGES = new char[][] //each range is represented as two characters, the lower inclusive character of the range and the upper inclusive character of the range
	{ { '\t', '\t' }, { '\n', '\n' }, { '\r', '\r' }, //tab, linefeed, and carriage return escaped in C fashion because including them as unicode characters seems to make JBuilder try to parse them right away
			{ '\u0020', '\uD7FF' }, { '\uE000', '\uFFFD' }, { '\uFFFE', '\uFFFF' } }; //TODO fix the last range to match XML's [#x10000-#x10FFFF]

	/** The characters considered by XML to be base characters. */
	public final static char[][] BASE_CHAR_RANGES = new char[][] //each range is represented as two characters, the lower inclusive character of the range and the upper inclusive character of the range
	{ { '\u0041', '\u005A' }, { '\u0061', '\u007A' }, { '\u00C0', '\u00D6' }, { '\u00D8', '\u00F6' }, { '\u00F8', '\u00FF' }, { '\u0100', '\u0131' },
			{ '\u0134', '\u013E' }, { '\u0141', '\u0148' }, { '\u014A', '\u017E' }, { '\u0180', '\u01C3' }, { '\u01CD', '\u01F0' }, { '\u01F4', '\u01F5' },
			{ '\u01FA', '\u0217' }, { '\u0250', '\u02A8' }, { '\u02BB', '\u02C1' }, { '\u0386', '\u0386' }, { '\u0388', '\u038A' }, { '\u038C', '\u03BC' },
			{ '\u038E', '\u03A1' }, { '\u03A3', '\u03CE' }, { '\u03D0', '\u03D6' }, { '\u03DA', '\u03DA' }, { '\u03DC', '\u03DC' }, { '\u03DE', '\u03DE' },
			{ '\u03E0', '\u03E0' }, { '\u03E2', '\u03F3' }, { '\u0401', '\u040C' }, { '\u040E', '\u044F' }, { '\u0451', '\u045C' }, { '\u045E', '\u0481' },
			{ '\u0490', '\u04C4' }, { '\u04C7', '\u04C8' }, { '\u04CB', '\u04CC' }, { '\u04D0', '\u04EB' }, { '\u04EE', '\u04F5' }, { '\u04F8', '\u04F9' },
			{ '\u0531', '\u0556' }, { '\u0559', '\u0559' }, { '\u0561', '\u0586' }, { '\u05D0', '\u05EA' }, { '\u05F0', '\u05F2' }, { '\u0621', '\u063A' },
			{ '\u0641', '\u064A' }, { '\u0671', '\u06B7' }, { '\u06BA', '\u06BE' }, { '\u06C0', '\u06CE' }, { '\u06D0', '\u06D3' }, { '\u06D5', '\u06D5' },
			{ '\u06E5', '\u06E6' }, { '\u0905', '\u0939' }, { '\u093D', '\u093D' }, { '\u0958', '\u0961' }, { '\u0985', '\u098C' }, { '\u098F', '\u0990' },
			{ '\u0993', '\u09A8' }, { '\u09AA', '\u09B0' }, { '\u09B2', '\u09B2' }, { '\u09B6', '\u09B9' }, { '\u09DC', '\u09DD' }, { '\u09DF', '\u09E1' },
			{ '\u09F0', '\u09F1' }, { '\u0A05', '\u0A0A' }, { '\u0A0F', '\u0A10' }, { '\u0A13', '\u0A28' }, { '\u0A2A', '\u0A30' }, { '\u0A32', '\u0A33' },
			{ '\u0A35', '\u0A36' }, { '\u0A38', '\u0A39' }, { '\u0A59', '\u0A5C' }, { '\u0A5E', '\u0A5E' }, { '\u0A72', '\u0A74' }, { '\u0A85', '\u0A8B' },
			{ '\u0A8D', '\u0A8D' }, { '\u0A8F', '\u0A91' }, { '\u0A93', '\u0AA8' }, { '\u0AAA', '\u0AB0' }, { '\u0AB2', '\u0AB3' }, { '\u0AB5', '\u0AB9' },
			{ '\u0ABD', '\u0ABD' }, { '\u0AE0', '\u0AE0' }, { '\u0B05', '\u0B0C' }, { '\u0B0F', '\u0B10' }, { '\u0B13', '\u0B28' }, { '\u0B2A', '\u0B30' },
			{ '\u0B32', '\u0B33' }, { '\u0B36', '\u0B39' }, { '\u0B3D', '\u0B3D' }, { '\u0B5C', '\u0B5D' }, { '\u0B5F', '\u0B61' }, { '\u0B85', '\u0B8A' },
			{ '\u0B8E', '\u0B90' }, { '\u0B92', '\u0B95' }, { '\u0B99', '\u0B9A' }, { '\u0B9C', '\u0B9C' }, { '\u0B9E', '\u0B9F' }, { '\u0BA3', '\u0BA4' },
			{ '\u0BA8', '\u0BAA' }, { '\u0BAE', '\u0BB5' }, { '\u0BB7', '\u0BB9' }, { '\u0C05', '\u0C0C' }, { '\u0C0E', '\u0C10' }, { '\u0C12', '\u0C28' },
			{ '\u0C2A', '\u0C33' }, { '\u0C35', '\u0C39' }, { '\u0C60', '\u0C61' }, { '\u0C85', '\u0C8C' }, { '\u0C8E', '\u0C90' }, { '\u0C92', '\u0CA8' },
			{ '\u0CAA', '\u0CB3' }, { '\u0CB5', '\u0CB9' }, { '\u0CDE', '\u0CDE' }, { '\u0CE0', '\u0CE1' }, { '\u0D05', '\u0D0C' }, { '\u0D0E', '\u0D10' },
			{ '\u0D12', '\u0D28' }, { '\u0D2A', '\u0D39' }, { '\u0D60', '\u0D61' }, { '\u0E01', '\u0E2E' }, { '\u0E30', '\u0E30' }, { '\u0E32', '\u0E33' },
			{ '\u0E40', '\u0E45' }, { '\u0E81', '\u0E82' }, { '\u0E84', '\u0E84' }, { '\u0E87', '\u0E88' }, { '\u0E8A', '\u0E8A' }, { '\u0E8D', '\u0E8D' },
			{ '\u0E94', '\u0E97' }, { '\u0E99', '\u0E9F' }, { '\u0EA1', '\u0EA3' }, { '\u0EA5', '\u0EA5' }, { '\u0EA7', '\u0EA7' }, { '\u0EAA', '\u0EAB' },
			{ '\u0EAD', '\u0EAE' }, { '\u0EB0', '\u0EB0' }, { '\u0EB2', '\u0EB3' }, { '\u0EBD', '\u0EBD' }, { '\u0EC0', '\u0EC4' }, { '\u0F40', '\u0F47' },
			{ '\u0F49', '\u0F69' }, { '\u10A0', '\u10C5' }, { '\u10D0', '\u10F6' }, { '\u1100', '\u1100' }, { '\u1102', '\u1103' }, { '\u1105', '\u1107' },
			{ '\u1109', '\u1109' }, { '\u110B', '\u110C' }, { '\u110E', '\u1112' }, { '\u113C', '\u113C' }, { '\u113E', '\u113E' }, { '\u1140', '\u1140' },
			{ '\u114C', '\u114C' }, { '\u114E', '\u114E' }, { '\u1150', '\u1150' }, { '\u1154', '\u1155' }, { '\u1159', '\u1159' }, { '\u115F', '\u1161' },
			{ '\u1163', '\u1163' }, { '\u1165', '\u1165' }, { '\u1167', '\u1167' }, { '\u1169', '\u1169' }, { '\u116D', '\u116E' }, { '\u1172', '\u1173' },
			{ '\u1175', '\u1175' }, { '\u119E', '\u119E' }, { '\u11A8', '\u11A8' }, { '\u11AB', '\u11AB' }, { '\u11AE', '\u11AF' }, { '\u11B7', '\u11B8' },
			{ '\u11BA', '\u11BA' }, { '\u11BC', '\u11C2' }, { '\u11EB', '\u11EB' }, { '\u11F0', '\u11F0' }, { '\u11F9', '\u11F9' }, { '\u1E00', '\u1E9B' },
			{ '\u1EA0', '\u1EF9' }, { '\u1F00', '\u1F15' }, { '\u1F18', '\u1F1D' }, { '\u1F20', '\u1F45' }, { '\u1F48', '\u1F4D' }, { '\u1F50', '\u1F57' },
			{ '\u1F59', '\u1F59' }, { '\u1F5B', '\u1F5B' }, { '\u1F5D', '\u1F5D' }, { '\u1F5F', '\u1F7D' }, { '\u1F80', '\u1FB4' }, { '\u1FB6', '\u1FBC' },
			{ '\u1FBE', '\u1FBE' }, { '\u1FC2', '\u1FC4' }, { '\u1FC6', '\u1FCC' }, { '\u1FD0', '\u1FD3' }, { '\u1FD6', '\u1FDB' }, { '\u1FE0', '\u1FEC' },
			{ '\u1FF2', '\u1FF4' }, { '\u1FF6', '\u1FFC' }, { '\u2126', '\u2126' }, { '\u212A', '\u212B' }, { '\u212E', '\u212E' }, { '\u2180', '\u2182' },
			{ '\u3041', '\u3094' }, { '\u30A1', '\u30FA' }, { '\u3105', '\u312C' }, { '\uAC00', '\uD7A3' } };

	/** The characters considered by XML to be ideographic characters. */
	public final static char[][] IDEOGRAPHIC_RANGES = new char[][] { { '\u4E00', '\u9FA5' }, { '\u3007', '\u3007' }, { '\u3021', '\u3029' } }; //each range is represented as two characters, the lower inclusive character of the range and the upper inclusive character of the range
	/** The characters considered by XML to be combining characters. */
	public final static char[][] COMBINING_CHAR_RANGES = new char[][] //each range is represented as two characters, the lower inclusive character of the range and the upper inclusive character of the range
	{ { '\u0300', '\u0345' }, { '\u0360', '\u0361' }, { '\u0483', '\u0486' }, { '\u0591', '\u05A1' }, { '\u05A3', '\u05B9' }, { '\u05BB', '\u05BD' },
			{ '\u05BF', '\u05BF' }, { '\u05C1', '\u05C2' }, { '\u05C4', '\u05C4' }, { '\u064B', '\u0652' }, { '\u0670', '\u0670' }, { '\u06D6', '\u06DC' },
			{ '\u06DD', '\u06DF' }, { '\u06E0', '\u06E4' }, { '\u06E7', '\u06E8' }, { '\u06EA', '\u06ED' }, { '\u0901', '\u0903' }, { '\u093C', '\u093C' },
			{ '\u093E', '\u094C' }, { '\u094D', '\u094D' }, { '\u0951', '\u0954' }, { '\u0962', '\u0963' }, { '\u0981', '\u0983' }, { '\u09BC', '\u09BC' },
			{ '\u09BE', '\u09BE' }, { '\u09BF', '\u09BF' }, { '\u09C0', '\u09C4' }, { '\u09C7', '\u09C8' }, { '\u09CB', '\u09CD' }, { '\u09D7', '\u09D7' },
			{ '\u09E2', '\u09E3' }, { '\u0A02', '\u0A02' }, { '\u0A3C', '\u0A3C' }, { '\u0A3E', '\u0A3E' }, { '\u0A3F', '\u0A3F' }, { '\u0A40', '\u0A42' },
			{ '\u0A47', '\u0A48' }, { '\u0A4B', '\u0A4D' }, { '\u0A70', '\u0A71' }, { '\u0A81', '\u0A83' }, { '\u0ABC', '\u0ABC' }, { '\u0ABE', '\u0AC5' },
			{ '\u0AC7', '\u0AC9' }, { '\u0ACB', '\u0ACD' }, { '\u0B01', '\u0B03' }, { '\u0B3C', '\u0B3C' }, { '\u0B3E', '\u0B43' }, { '\u0B47', '\u0B48' },
			{ '\u0B4B', '\u0B4D' }, { '\u0B56', '\u0B57' }, { '\u0B82', '\u0B83' }, { '\u0BBE', '\u0BC2' }, { '\u0BC6', '\u0BC8' }, { '\u0BCA', '\u0BCD' },
			{ '\u0BD7', '\u0BD7' }, { '\u0C01', '\u0C03' }, { '\u0C3E', '\u0C44' }, { '\u0C46', '\u0C48' }, { '\u0C4A', '\u0C4D' }, { '\u0C55', '\u0C56' },
			{ '\u0C82', '\u0C83' }, { '\u0CBE', '\u0CC4' }, { '\u0CC6', '\u0CC8' }, { '\u0CCA', '\u0CCD' } };
	/*G***fix
	| [#x0CD5-#x0CD6] | [#x0D02-#x0D03] | [#x0D3E-#x0D43] | [#x0D46-#x0D48] | [#x0D4A-#x0D4D] | #x0D57 | #x0E31 | [#x0E34-#x0E3A] | [#x0E47-#x0E4E] | #x0EB1 | [#x0EB4-#x0EB9] | [#x0EBB-#x0EBC] | [#x0EC8-#x0ECD] | [#x0F18-#x0F19] | #x0F35 | #x0F37 | #x0F39 | #x0F3E | #x0F3F | [#x0F71-#x0F84] | [#x0F86-#x0F8B] | [#x0F90-#x0F95] | #x0F97 | [#x0F99-#x0FAD] | [#x0FB1-#x0FB7] | #x0FB9 | [#x20D0-#x20DC] | #x20E1 | [#x302A-#x302F] | #x3099 | #x309A
	*/

	/** The characters considered by XML to be digits. */
	public final static char[][] DIGIT_RANGES = new char[][] //each range is represented as two characters, the lower inclusive character of the range and the upper inclusive character of the range
	{ { '\u0030', '\u0039' }, { '\u0660', '\u0669' }, { '\u06F0', '\u06F9' }, { '\u0966', '\u096F' }, { '\u09E6', '\u09EF' }, { '\u0A66', '\u0A6F' },
			{ '\u0AE6', '\u0AEF' }, { '\u0B66', '\u0B6F' }, { '\u0BE7', '\u0BEF' }, { '\u0C66', '\u0C6F' }, { '\u0CE6', '\u0CEF' }, { '\u0D66', '\u0D6F' },
			{ '\u0E50', '\u0E59' }, { '\u0ED0', '\u0ED9' }, { '\u0F20', '\u0F29' } };
	/** The characters considered by XML to be extenders. */
	public final static char[][] EXTENDER_RANGES = new char[][] //each range is represented as two characters, the lower inclusive character of the range and the upper inclusive character of the range
	{ { '\u00B7', '\u00B7' }, { '\u02D0', '\u02D0' }, { '\u02D1', '\u02D1' }, { '\u0387', '\u0387' }, { '\u0640', '\u0640' }, { '\u0E46', '\u0E46' },
			{ '\u0EC6', '\u0EC6' }, { '\u3005', '\u3005' }, { '\u3031', '\u3035' }, { '\u309D', '\u309E' }, { '\u30FC', '\u30FE' } };

	/** The characters which can serve as delimiters for an attribute value. */
	public final static String ATTRIBUTE_VALUE_DELIMITERS = String.valueOf(SINGLE_QUOTE_CHAR) + String.valueOf(DOUBLE_QUOTE_CHAR);

	/** The characters which begin an attribute list declaration. */
	public final static String ATTLIST_DECL_START = "<!ATTLIST";
	/** The characters which end an attribute list declaration. */
	public final static String ATTLIST_DECL_END = ">";
	/** The "CDATA" attribute type. */
	public final static String ATTTYPE_CDATA = "CDATA";
	/** The "ID" attribute type. */
	public final static String ATTTYPE_ID = "ID";
	/** The "IDREF" attribute type. */
	public final static String ATTTYPE_IDREF = "IDREF";
	/** The "IDREFS" attribute type. */
	public final static String ATTTYPE_IDREFS = "IDREFS";
	/** The "ENTITY" attribute type. */
	public final static String ATTTYPE_ENTITY = "ENTITY";
	/** The "ENTITIES" attribute type. */
	public final static String ATTTYPE_ENTITIES = "ENTITIES";
	/** The "NMTOKEN" attribute type. */
	public final static String ATTTYPE_NMTOKEN = "NMTOKEN";
	/** The "NMTOKENS" attribute type. */
	public final static String ATTTYPE_NMTOKENS = "NMTOKENS";
	/** The notation type attribute type. */
	public final static String ATTTYPE_NOTATION = "NOTATION";
	/** The start of an enumerated attribute type. */
	public final static String ATTTYPE_ENUMERATION_START = "(";
	/** The end of an enumerated attribute type. */
	public final static String ATTTYPE_ENUMERATION_END = ")";
	/** The required attribute default declaration. */
	public final static String ATT_DEFAULT_REQUIRED = "#REQUIRED";
	/** The implied attribute default declaration. */
	public final static String ATT_DEFAULT_IMPLIED = "#IMPLIED";
	/** The fixed attribute default declaration. */
	public final static String ATT_DEFAULT_FIXED = "#FIXED";
	/** The characters which begin an entity reference. */
	public final static String ENTITY_REF_START = "&";
	/** The characters which end an entity reference. */
	public final static String ENTITY_REF_END = ";";
	/** The characters which begin a character reference. */
	public final static String CHARACTER_REF_START = "&#";
	/** The character which indicates a hexadecimal character reference. */
	public final static String CHARACTER_REF_HEX_FLAG = "x";
	/** The characters which end a character reference. */
	public final static String CHARACTER_REF_END = ";";
	/** The characters which begin a CDATA section. */
	public final static String CDATA_START = "<![CDATA[";
	/** The characters which end a CDATA section. */
	public final static String CDATA_END = "]]>";
	/** The characters which begin a comment section. */
	public final static String COMMENT_START = "<!--";
	/**
	 * The first set of characters which end a comment section (because the string "--" cannot appear inside a comment for campatibility reasons).
	 */
	public final static String COMMENT_END_PART1 = "--";
	/**
	 * The first set of characters which end a comment section (because the string "--" cannot appear inside a comment for campatibility reasons).
	 */
	public final static String COMMENT_END_PART2 = ">";
	/** The entire set of characters which end a comment section. */
	public final static String COMMENT_END = COMMENT_END_PART1 + COMMENT_END_PART2;
	/** The name which identifies a document type declaration section, it it were to be considered a tag. */
	public final static String DOCTYPE_DECL_NAME = "!DOCTYPE";
	/** The characters which begin a document type declaration section. */
	public final static String DOCTYPE_DECL_START = "<" + DOCTYPE_DECL_NAME;
	/** The characters which end a document type declaration section. */
	public final static String DOCTYPE_DECL_END = ">";
	/** The name of the document type system ID label. */
	public final static String SYSTEM_ID_NAME = "SYSTEM";
	/** The name of the document type public ID label. */
	public final static String PUBLIC_ID_NAME = "PUBLIC";
	/** DTD: A string indicating an element can have any content. */
	public final static String ELEMENT_CONTENT_ANY = "ANY";
	/** DTD: A string indicating an element must have no content. */
	public final static String ELEMENT_CONTENT_EMPTY = "EMPTY";
	/** The characters which begin an element declaration. */
	public final static String ELEMENT_TYPE_DECL_START = "<!ELEMENT";
	/** The characters which end an element declaration. */
	public final static String ELEMENT_TYPE_DECL_END = ">";
	/** The characters which begin an entity declaration. */
	public final static String ENTITY_DECL_START = "<!ENTITY";
	/** The characters which end an entity declaration. */
	public final static String ENTITY_DECL_END = ">";
	/** The characters which begin an internal document type declaration subset. */
	public final static String INTERNAL_DTD_SUBSET_START = "[";
	/** The characters which end a document type declaration internal subset. */
	public final static String INTERNAL_DTD_SUBSET_END = "]";
	/** The characters which begin a parameter entity reference. */
	public final static String PARAMETER_ENTITY_REF_START = "%";
	/** The characters which end a parameter entity reference. */
	public final static String PARAMETER_ENTITY_REF_END = ";";
	/** The characters which begin a processing instruction. */
	public final static String PROCESSING_INSTRUCTION_START = "<?";
	/** The characters which end a processing instruction. */
	public final static String PROCESSING_INSTRUCTION_END = "?>";
	/** The characters which begin a tag. */
	public final static String TAG_START = "<";
	/** The characters which end a tag. */
	public final static String TAG_END = ">";
	/** The characters which marks a tag as being an end tag or an empty element tag. */
	public final static char END_TAG_IDENTIFIER_CHAR = '/';
	/** The characters which begin an XML declaration section. */
	public final static String XML_DECL_START = "<?xml";
	/** The characters which end an XML declaration section. */
	public final static String XML_DECL_END = "?>";
	/** The name of the version info attribute of an XML declaration section. */
	public final static String VERSIONINFO_NAME = "version";
	/** The name of the encoding declaration attribute of an XML declaration section. */
	public final static String ENCODINGDECL_NAME = "encoding";
	/** The name of the standalone attribute of an XML declaration section. */
	public final static String SDDECL_NAME = "standalone";

	//default entities
	/** The lt entity name. */
	public final static String ENTITY_LT_NAME = "lt";
	/** The lt entity value. */
	public final static char ENTITY_LT_VALUE = '<';
	/** The gt entity name. */
	public final static String ENTITY_GT_NAME = "gt";
	/** The gt entity value. */
	public final static char ENTITY_GT_VALUE = '>';
	/** The amp entity name. */
	public final static String ENTITY_AMP_NAME = "amp";
	/** The amp entity value. */
	public final static char ENTITY_AMP_VALUE = '&';
	/** The apos entity name. */
	public final static String ENTITY_APOS_NAME = "apos";
	/** The apos entity value. */
	public final static char ENTITY_APOS_VALUE = '\'';
	/** The quot entity name. */
	public final static String ENTITY_QUOT_NAME = "quot";
	/** The quot entity value. */
	public final static char ENTITY_QUOT_VALUE = '"';

	/** The character used for indicating namespaces. */
	public final static char NAMESPACE_DIVIDER = ':';

	/** A lazily-created cache of system IDs keyed to public IDs. */
	private static Reference<Map<String, String>> systemIDMapReference = null;

	/** A lazily-created cache of system IDs keyed to public IDs. */
	protected static Map<String, String> getSystemIDMap() {
		//get the cache if we have one
		Map<String, String> systemIDMap = systemIDMapReference != null ? systemIDMapReference.get() : null;
		if(systemIDMap == null) { //if the garbage collector has reclaimed the cache
			systemIDMap = new HashMap<String, String>(); //create a new map of system IDs, and fill it with the default mappings
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
			systemIDMapReference = new SoftReference<Map<String, String>>(systemIDMap); //create a soft reference to the map
		}
		return systemIDMap; //return the map
	}

	/**
	 * Determines the default system ID for the given public ID.
	 * @param publicID The public ID for which a doctype system ID should be retrieved.
	 * @return The default doctype system ID corresponding to the given public ID, or <code>null</code> if the given public ID is not recognized.
	 */
	public static String getDefaultSystemID(final String publicID) {
		return getSystemIDMap().get(publicID); //return the system ID corresponding to the given public ID, if we have one
	}

	/** A lazily-created cache of content types keyed to public IDs. */
	private static Reference<Map<String, ContentType>> contentTypeMapReference = null;

	/** A lazily-created cache of content types keyed to public IDs. */
	protected static Map<String, ContentType> getContentTypeMap() {
		//get the cache if we have one
		Map<String, ContentType> contentTypeMap = contentTypeMapReference != null ? contentTypeMapReference.get() : null;
		if(contentTypeMap == null) { //if the garbage collector has reclaimed the cache
			contentTypeMap = new HashMap<String, ContentType>(); //create a new map of content types, and fill it with the default mappings
			contentTypeMap.put("-//Guise//DTD XHTML Guise 1.0//EN", XHTML_CONTENT_TYPE); //Guise XHTML DTD
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
			contentTypeMapReference = new SoftReference<Map<String, ContentType>>(contentTypeMap); //create a soft reference to the map
		}
		return contentTypeMap; //return the map
	}

	/**
	 * Determines the content type for the given public ID.
	 * @param publicID The public ID for which a content type should be retrieved.
	 * @return The content type corresponding to the given public ID, or <code>null</code> if the given public ID is not recognized.
	 */
	public static ContentType getContentType(final String publicID) {
		return getContentTypeMap().get(publicID); //return the content type corresponding to the given public ID, if we have one
	}

	/** A lazily-created cache of root element local names keyed to content types base type names. */
	private static Reference<Map<String, String>> rootElementLocalNameMapReference = null;

	/** A lazily-created cache of root element local names keyed to content types. */
	protected static Map<String, String> getRootElementLocalNameMap() {
		//get the cache if we have one
		Map<String, String> rootElementLocalNameMap = rootElementLocalNameMapReference != null ? rootElementLocalNameMapReference.get() : null;
		if(rootElementLocalNameMap == null) { //if the garbage collector has reclaimed the cache
			rootElementLocalNameMap = new HashMap<String, String>(); //create a new map of root element local names, and fill it with the default mappings
			rootElementLocalNameMap.put(HTML_CONTENT_TYPE.getBaseType(), ELEMENT_HTML);
			rootElementLocalNameMap.put(XHTML_CONTENT_TYPE.getBaseType(), ELEMENT_HTML);
			rootElementLocalNameMap.put(MATHML_CONTENT_TYPE.getBaseType(), ELEMENT_MATHML);
			rootElementLocalNameMap.put(SVG_CONTENT_TYPE.getBaseType(), ELEMENT_SVG);
			rootElementLocalNameMapReference = new SoftReference<Map<String, String>>(rootElementLocalNameMap); //create a soft reference to the map
		}
		return rootElementLocalNameMap; //return the map
	}

	/**
	 * Determines the default root element local name for the given content type
	 * @param contentType The content type for which a root element should be retrieved.
	 * @return The default root element local name corresponding to the given media type, or <code>null</code> if the given content type is not recognized.
	 */
	public static String getDefaultRootElementLocalName(final ContentType contentType) {
		return getRootElementLocalNameMap().get(contentType.getBaseType()); //return the root element corresponding to the given content type base type, if we have one
	}

	/**
	 * Creates a document builder and parses an input stream without namespace awareness with no validation. An entity resolver is installed to load requested
	 * resources from local resources if possible. This allows quick local lookup of the XHTML DTDs, for example. The Sun JDK 1.5 document builder handles the BOM
	 * correctly.
	 * <p>
	 * Any {@link SAXException} is converted to an {@link IOException}.
	 * </p>
	 * @param inputStream The input stream containing the content to be parsed.
	 * @return The parsed XML document.
	 * @throws ConfigurationException if a document builder cannot be created which satisfies the configuration requested.
	 * @throws IOException If there is an error reading or parsing the information.
	 */
	public static Document parse(final InputStream inputStream) throws IOException {
		return parse(inputStream, XMLEntityResolver.getInstance());
	}

	/**
	 * Creates a document builder and parses an input stream without namespace awareness with no validation. The Sun JDK 1.5 document builder handles the BOM
	 * correctly.
	 * <p>
	 * Any {@link SAXException} is converted to an {@link IOException}.
	 * </p>
	 * @param inputStream The input stream containing the content to be parsed.
	 * @param entityResolver The strategy to use for resolving entities, or <code>null</code> if no entity resolver should be installed.
	 * @return The parsed XML document.
	 * @throws ConfigurationException if a document builder cannot be created which satisfies the configuration requested.
	 * @throws IOException If there is an error reading or parsing the information.
	 */
	public static Document parse(final InputStream inputStream, final EntityResolver entityResolver) throws IOException {
		return parse(inputStream, false, entityResolver);
	}

	/**
	 * Creates a document builder and parses an input stream without namespace awareness with no validation. An entity resolver is installed to load requested
	 * resources from local resources if possible. This allows quick local lookup of the XHTML DTDs, for example. The Sun JDK 1.5 document builder handles the BOM
	 * correctly.
	 * <p>
	 * Any {@link SAXException} is converted to an {@link IOException}.
	 * </p>
	 * @param inputStream The input stream containing the content to be parsed.
	 * @param systemID Provide a base for resolving relative URIs.
	 * @return The parsed XML document.
	 * @throws ConfigurationException if a document builder cannot be created which satisfies the configuration requested.
	 * @throws IOException If there is an error reading or parsing the information.
	 */
	public static Document parse(final InputStream inputStream, final URI systemID) throws IOException {
		return parse(inputStream, systemID, XMLEntityResolver.getInstance());
	}

	/**
	 * Creates a document builder and parses an input stream without namespace awareness with no validation. The Sun JDK 1.5 document builder handles the BOM
	 * correctly.
	 * <p>
	 * Any {@link SAXException} is converted to an {@link IOException}.
	 * </p>
	 * @param inputStream The input stream containing the content to be parsed.
	 * @param systemID Provide a base for resolving relative URIs.
	 * @param entityResolver The strategy to use for resolving entities, or <code>null</code> if no entity resolver should be installed.
	 * @return The parsed XML document.
	 * @throws ConfigurationException if a document builder cannot be created which satisfies the configuration requested.
	 * @throws IOException If there is an error reading or parsing the information.
	 */
	public static Document parse(final InputStream inputStream, final URI systemID, final EntityResolver entityResolver) throws IOException {
		return parse(inputStream, systemID, false, entityResolver);
	}

	/**
	 * Creates a document builder and parses an input stream, specifying namespace awareness with no validation. An entity resolver is installed to load requested
	 * resources from local resources if possible. This allows quick local lookup of the XHTML DTDs, for example. The Sun JDK 1.5 document builder handles the BOM
	 * correctly.
	 * <p>
	 * Any {@link SAXException} is converted to an {@link IOException}.
	 * </p>
	 * @param inputStream The input stream containing the content to be parsed.
	 * @param namespaceAware <code>true</code> if the parser produced will provide support for XML namespaces, else <code>false</code>.
	 * @return The parsed XML document.
	 * @throws ConfigurationException if a document builder cannot be created which satisfies the configuration requested.
	 * @throws IOException If there is an error reading or parsing the information.
	 */
	public static Document parse(final InputStream inputStream, final boolean namespaceAware) throws IOException {
		return parse(inputStream, namespaceAware, XMLEntityResolver.getInstance());
	}

	/**
	 * Creates a document builder and parses an input stream, specifying namespace awareness with no validation. The Sun JDK 1.5 document builder handles the BOM
	 * correctly.
	 * <p>
	 * Any {@link SAXException} is converted to an {@link IOException}.
	 * </p>
	 * @param inputStream The input stream containing the content to be parsed.
	 * @param namespaceAware <code>true</code> if the parser produced will provide support for XML namespaces, else <code>false</code>.
	 * @param entityResolver The strategy to use for resolving entities, or <code>null</code> if no entity resolver should be installed.
	 * @return The parsed XML document.
	 * @throws ConfigurationException if a document builder cannot be created which satisfies the configuration requested.
	 * @throws IOException If there is an error reading or parsing the information.
	 */
	public static Document parse(final InputStream inputStream, final boolean namespaceAware, final EntityResolver entityResolver) throws IOException {
		return parse(inputStream, namespaceAware, false, entityResolver);
	}

	/**
	 * Creates a document builder and parses an input stream, specifying namespace awareness with no validation. An entity resolver is installed to load requested
	 * resources from local resources if possible. This allows quick local lookup of the XHTML DTDs, for example. The Sun JDK 1.5 document builder handles the BOM
	 * correctly.
	 * <p>
	 * Any {@link SAXException} is converted to an {@link IOException}.
	 * </p>
	 * @param inputStream The input stream containing the content to be parsed.
	 * @param systemID Provide a base for resolving relative URIs.
	 * @param namespaceAware <code>true</code> if the parser produced will provide support for XML namespaces, else <code>false</code>.
	 * @return The parsed XML document.
	 * @throws ConfigurationException if a document builder cannot be created which satisfies the configuration requested.
	 * @throws IOException If there is an error reading or parsing the information.
	 */
	public static Document parse(final InputStream inputStream, final URI systemID, final boolean namespaceAware) throws IOException {
		return parse(inputStream, systemID, namespaceAware, XMLEntityResolver.getInstance());
	}

	/**
	 * Creates a document builder and parses an input stream, specifying namespace awareness with no validation. The Sun JDK 1.5 document builder handles the BOM
	 * correctly.
	 * <p>
	 * Any {@link SAXException} is converted to an {@link IOException}.
	 * </p>
	 * @param inputStream The input stream containing the content to be parsed.
	 * @param systemID Provide a base for resolving relative URIs.
	 * @param namespaceAware <code>true</code> if the parser produced will provide support for XML namespaces, else <code>false</code>.
	 * @param entityResolver The strategy to use for resolving entities, or <code>null</code> if no entity resolver should be installed.
	 * @return The parsed XML document.
	 * @throws ConfigurationException if a document builder cannot be created which satisfies the configuration requested.
	 * @throws IOException If there is an error reading or parsing the information.
	 */
	public static Document parse(final InputStream inputStream, final URI systemID, final boolean namespaceAware, final EntityResolver entityResolver)
			throws IOException {
		return parse(inputStream, systemID, namespaceAware, false, entityResolver);
	}

	/**
	 * Creates a document builder and parses an input stream, specifying namespace awareness and validation. An entity resolver is installed to load requested
	 * resources from local resources if possible. This allows quick local lookup of the XHTML DTDs, for example. The Sun JDK 1.5 document builder handles the BOM
	 * correctly.
	 * <p>
	 * Any {@link SAXException} is converted to an {@link IOException}.
	 * </p>
	 * @param inputStream The input stream containing the content to be parsed.
	 * @param namespaceAware <code>true</code> if the parser produced will provide support for XML namespaces, else <code>false</code>.
	 * @param validating <code>true</code> if the parser produced will validate documents as they are parsed, else <code>false</code>.
	 * @return The parsed XML document.
	 * @throws ConfigurationException if a document builder cannot be created which satisfies the configuration requested.
	 * @throws IOException If there is an error reading or parsing the information.
	 */
	public static Document parse(final InputStream inputStream, final boolean namespaceAware, final boolean validating) throws IOException {
		return parse(inputStream, namespaceAware, validating, XMLEntityResolver.getInstance());
	}

	/**
	 * Creates a document builder and parses an input stream, specifying namespace awareness and validation. The Sun JDK 1.5 document builder handles the BOM
	 * correctly.
	 * <p>
	 * Any {@link SAXException} is converted to an {@link IOException}.
	 * </p>
	 * @param inputStream The input stream containing the content to be parsed.
	 * @param namespaceAware <code>true</code> if the parser produced will provide support for XML namespaces, else <code>false</code>.
	 * @param validating <code>true</code> if the parser produced will validate documents as they are parsed, else <code>false</code>.
	 * @param entityResolver The strategy to use for resolving entities, or <code>null</code> if no entity resolver should be installed.
	 * @return The parsed XML document.
	 * @throws ConfigurationException if a document builder cannot be created which satisfies the configuration requested.
	 * @throws IOException If there is an error reading or parsing the information.
	 */
	public static Document parse(final InputStream inputStream, final boolean namespaceAware, final boolean validating, final EntityResolver entityResolver)
			throws IOException {
		try {
			return createDocumentBuilder(namespaceAware, validating, entityResolver).parse(inputStream);
		} catch(final SAXException saxException) {
			throw new IOException(saxException.getMessage(), saxException);
		}
	}

	/**
	 * Creates a document builder and parses an input stream, specifying namespace awareness and validation. An entity resolver is installed to load requested
	 * resources from local resources if possible. This allows quick local lookup of the XHTML DTDs, for example. The Sun JDK 1.5 document builder handles the BOM
	 * correctly.
	 * <p>
	 * Any {@link SAXException} is converted to an {@link IOException}.
	 * </p>
	 * @param inputStream The input stream containing the content to be parsed.
	 * @param systemID Provide a base for resolving relative URIs.
	 * @param namespaceAware <code>true</code> if the parser produced will provide support for XML namespaces, else <code>false</code>.
	 * @param validating <code>true</code> if the parser produced will validate documents as they are parsed, else <code>false</code>.
	 * @return The parsed XML document.
	 * @throws ConfigurationException if a document builder cannot be created which satisfies the configuration requested.
	 * @throws IOException If there is an error reading or parsing the information.
	 */
	public static Document parse(final InputStream inputStream, final URI systemID, final boolean namespaceAware, final boolean validating) throws IOException {
		return parse(inputStream, systemID, namespaceAware, validating, XMLEntityResolver.getInstance());
	}

	/**
	 * Creates a document builder and parses an input stream, specifying namespace awareness and validation. The Sun JDK 1.5 document builder handles the BOM
	 * correctly.
	 * <p>
	 * Any {@link SAXException} is converted to an {@link IOException}.
	 * </p>
	 * @param inputStream The input stream containing the content to be parsed.
	 * @param systemID Provide a base for resolving relative URIs.
	 * @param namespaceAware <code>true</code> if the parser produced will provide support for XML namespaces, else <code>false</code>.
	 * @param validating <code>true</code> if the parser produced will validate documents as they are parsed, else <code>false</code>.
	 * @param entityResolver The strategy to use for resolving entities, or <code>null</code> if no entity resolver should be installed.
	 * @return The parsed XML document.
	 * @throws ConfigurationException if a document builder cannot be created which satisfies the configuration requested.
	 * @throws IOException If there is an error reading or parsing the information.
	 */
	public static Document parse(final InputStream inputStream, final URI systemID, final boolean namespaceAware, final boolean validating,
			final EntityResolver entityResolver) throws IOException {
		try {
			return createDocumentBuilder(namespaceAware, validating, entityResolver).parse(inputStream, systemID.toString());
		} catch(final SAXException saxException) {
			throw new IOException(saxException.getMessage(), saxException);
		}
	}

	/**
	 * Creates and returns a document builder without namespace awareness with no validation. An entity resolver is installed to load requested resources from
	 * local resources if possible. This allows quick local lookup of the XHTML DTDs, for example. The Sun JDK 1.5 document builder handles the BOM correctly.
	 * @return A new XML document builder.
	 * @throws ConfigurationException if a document builder cannot be created which satisfies the configuration requested.
	 */
	public static DocumentBuilder createDocumentBuilder() {
		return createDocumentBuilder(XMLEntityResolver.getInstance());
	}

	/**
	 * Creates and returns a document builder without namespace awareness with no validation. The Sun JDK 1.5 document builder handles the BOM correctly.
	 * @param entityResolver The strategy to use for resolving entities, or <code>null</code> if no entity resolver should be installed.
	 * @return A new XML document builder.
	 * @throws ConfigurationException if a document builder cannot be created which satisfies the configuration requested.
	 */
	public static DocumentBuilder createDocumentBuilder(final EntityResolver entityResolver) {
		return createDocumentBuilder(false, entityResolver); //create a document builder with no namespace awareness
	}

	/**
	 * Creates and returns a document builder, specifying namespace awareness with no validation. An entity resolver is installed to load requested resources from
	 * local resources if possible. This allows quick local lookup of the XHTML DTDs, for example. The Sun JDK 1.5 document builder handles the BOM correctly.
	 * @param namespaceAware <code>true</code> if the parser produced will provide support for XML namespaces, else <code>false</code>.
	 * @return A new XML document builder.
	 * @throws ConfigurationException if a document builder cannot be created which satisfies the configuration requested.
	 */
	public static DocumentBuilder createDocumentBuilder(final boolean namespaceAware) {
		return createDocumentBuilder(namespaceAware, XMLEntityResolver.getInstance());
	}

	/**
	 * Creates and returns a document builder, specifying namespace awareness with no validation. The Sun JDK 1.5 document builder handles the BOM correctly.
	 * @param namespaceAware <code>true</code> if the parser produced will provide support for XML namespaces, else <code>false</code>.
	 * @param entityResolver The strategy to use for resolving entities, or <code>null</code> if no entity resolver should be installed.
	 * @return A new XML document builder.
	 * @throws ConfigurationException if a document builder cannot be created which satisfies the configuration requested.
	 */
	public static DocumentBuilder createDocumentBuilder(final boolean namespaceAware, final EntityResolver entityResolver) {
		return createDocumentBuilder(namespaceAware, false, entityResolver); //create a document builder with no validation
	}

	/**
	 * Creates and returns a document builder, specifying namespace awareness and validation. An entity resolver is installed to load requested resources from
	 * local resources if possible. This allows quick local lookup of the XHTML DTDs, for example. The Sun JDK 1.5 document builder handles the BOM correctly.
	 * @param namespaceAware <code>true</code> if the parser produced will provide support for XML namespaces, else <code>false</code>.
	 * @param validating <code>true</code> if the parser produced will validate documents as they are parsed, else <code>false</code>.
	 * @return A new XML document builder.
	 * @throws ConfigurationException if a document builder cannot be created which satisfies the configuration requested.
	 */
	public static DocumentBuilder createDocumentBuilder(final boolean namespaceAware, final boolean validating) {
		return createDocumentBuilder(namespaceAware, validating, XMLEntityResolver.getInstance());
	}

	/**
	 * Creates and returns a document builder, specifying namespace awareness and validation. The Sun JDK 1.5 document builder handles the BOM correctly.
	 * @param namespaceAware <code>true</code> if the parser produced will provide support for XML namespaces, else <code>false</code>.
	 * @param validating <code>true</code> if the parser produced will validate documents as they are parsed, else <code>false</code>.
	 * @param entityResolver The strategy to use for resolving entities, or <code>null</code> if no entity resolver should be installed.
	 * @return A new XML document builder.
	 * @throws ConfigurationException if a document builder cannot be created which satisfies the configuration requested.
	 */
	public static DocumentBuilder createDocumentBuilder(final boolean namespaceAware, final boolean validating, final EntityResolver entityResolver) {
		try {
			final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance(); //create a document builder factory			
			documentBuilderFactory.setNamespaceAware(namespaceAware); //set namespace awareness appropriately
			documentBuilderFactory.setValidating(validating); //set validating appropriately
			//prevent a NullPointerException in some cases when using the com.sun.org.apache.xerces.internal parser
			//see http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6181020
			//see http://issues.apache.org/jira/browse/XERCESJ-977
			//http://forums.sun.com/thread.jspa?threadID=5390848
			try {
				documentBuilderFactory.setFeature("http://apache.org/xml/features/dom/defer-node-expansion", false);
			} catch(final Throwable throwable) {
			} //if the parser doesn't support this feature, it's probably not the buggy Xerces parser, so we're in an even better situation; normally we'd expect a ParserConfigurationException, but sometimes a java.lang.AbstractMethodError is thrown by javax.xml.parsers.DocumentBuilderFactory.setFeature(Ljava/lang/String;Z)V
			final DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder(); //create a new document builder
			if(entityResolver != null) { //if an entity resolver was given
				documentBuilder.setEntityResolver(entityResolver); //install the given entity resolver
			}
			return documentBuilder; //return the configured document builder
		} catch(final ParserConfigurationException parserConfigurationException) { //if the requested parser is not supported
			throw new ConfigurationException(parserConfigurationException);
		}
	}

	/**
	 * Creates a character reference to represent the given characters.
	 * @param character The character to encode.
	 * @return A character reference in the form <code>&#xXX;</code>.
	 */
	public static String createCharacterReference(final char character) {
		final StringBuilder stringBuilder = new StringBuilder(XML.CHARACTER_REF_START); //create a string builder with the start of a character reference
		stringBuilder.append(XML.CHARACTER_REF_HEX_FLAG); //indicate that the character reference is in hex
		stringBuilder.append(toHexString(character, 4)); //write the hex value of the character
		stringBuilder.append(XML.CHARACTER_REF_END); //end the character reference
		return stringBuilder.toString(); //return the constructed character reference
	}

	/**
	 * Returns a list of all elements from the given list of nodes.
	 * @param nodes The list of nodes.
	 * @return A list of all elements in the given list.
	 * @throws NullPointerException if the given list is <code>null</code>.
	 */
	public static List<Element> getElements(final List<Node> nodes) {
		return getElementsAsType(nodes, Node.ELEMENT_NODE, Element.class);
	}

	/**
	 * Returns a list of all nodes of a given type from the given list of nodes.
	 * @param nodes The list of nodes.
	 * @param nodeType The type of node to retrieve, one of the <code>Node.?_NODE</code> constants.
	 * @return A list of all nodes of the given type from the given list.
	 * @throws NullPointerException if the given list is <code>null</code>.
	 * @throws ClassCastException if the given node type does not correspond to the given node class.
	 */
	protected static <N extends Node> List<N> getElementsAsType(final List<Node> nodes, final short nodeType, final Class<N> nodeClass) {
		final List<N> nodesAsType = new ArrayList<N>();
		for(final Node node : nodes) { //look at all the nodes
			if(node.getNodeType() == nodeType) { //if this node is of the correct type
				nodesAsType.add(nodeClass.cast(node)); //cast and add the node to the list
			}
		}
		return nodesAsType;
	}

	/**
	 * Returns the owner document of the given node or, if the node is a document, returns the node itself.
	 * @param node The node that may be a document or may have an owner document.
	 * @return The node owner document (which may be <code>null</code> if the node has no owner document) or, if the node is a document, the node itself.
	 */
	public static Document getDocument(final Node node) {
		return node.getNodeType() == Node.DOCUMENT_NODE ? (Document)node : node.getOwnerDocument(); //return the node if it is a document, otherwise return the node's owner document
	}

	/**
	 * Returns the first node in the hierarchy, beginning with the specified node and continuing with a depth-first search, that has a particular node type.
	 * @param node The node which will be checked for nodes, along with its children. The node's owner document should implement the
	 *          <code>DocumentTraversal</code> interface.
	 * @param whatToShow Which node type(s) to return. See the description of <code>NodeFilter</code> for the set of possible <code>SHOW_</code> values. These
	 *          flags can be combined using boolean OR.
	 * @return The first encountered node in a depth-first (document order) search that matches the filter criteria, or <code>null</code> if no matching node
	 *         exists.
	 * @see NodeIterator
	 * @see NodeFilter
	 */
	public static Node getFirstNode(final Node node, final int whatToShow) {
		//create a node iterator that will only return the types of nodes we want
		final NodeIterator nodeIterator = ((DocumentTraversal)node.getOwnerDocument()).createNodeIterator(node, whatToShow, null, false); //TODO should we set expandEntityReferences to true or false? true?
		return nodeIterator.nextNode(); //get the next node (which will be the first node) and return it
	}

	/**
	 * Returns the prefix from the qualified name.
	 * @param qualifiedName The fully qualified name, with an optional namespace prefix.
	 * @return The namespace prefix, or <code>null</code> if no prefix is present.
	 * @throws NullPointerException if the given qualified name is <code>null</code>.
	 */
	public static String getPrefix(final String qualifiedName) {
		final int prefixDividerIndex = qualifiedName.indexOf(XML.NAMESPACE_DIVIDER); //see if there is a prefix
		if(prefixDividerIndex >= 0) //if there is a prefix
			return qualifiedName.substring(0, prefixDividerIndex); //return the prefix
		else
			//if there is no prefix
			return null; //show that there is no prefix
	}

	/**
	 * Retrieves the local name from a qualified name, removing the prefix, if any.
	 * @param qualifiedName The XML qualified name.
	 * @return The local name without a prefix.
	 * @throws NullPointerException if the given qualified name is <code>null</code>.
	 */
	public static String getLocalName(final String qualifiedName) {
		final int namespaceDividerIndex = qualifiedName.indexOf(XML.NAMESPACE_DIVIDER); //find where the namespace divider is in the name
		return namespaceDividerIndex >= 0 ? //if there is a namespace prefix
		qualifiedName.substring(namespaceDividerIndex + 1)
				: //remove the namespace prefix
				qualifiedName; //otherwise, just return the qualified name since it didn't have a prefix to begin with
	}

	/**
	 * Returns the index of a given node in a node list.
	 * @param nodeList The node list to search.
	 * @param node The node for which an index should be returned.
	 * @return The index of the node in the given node list, or -1 if the node does not appear in the node list.
	 */
	public static int indexOf(final NodeList nodeList, final Node node) {
		final int nodeCount = nodeList.getLength(); //see how many nodes there are
		for(int i = 0; i < nodeCount; ++i) { //look at each node
			if(nodeList.item(i) == node) //if the node at this index matches our node
				return i; //show the index as which the node occurs
		}
		return -1; //show that we were not able to find a matching node
	}

	/**
	 * Checks to see if the specified character is a legal XML character.
	 * @param c The character to check.
	 * @return true if the character is a legal XML character.
	 */
	public static boolean isChar(final char c) {
		return Characters.isCharInRange(c, XML.CHAR_RANGES); //see if the character is a legal XML character
	}

	/**
	 * Checks to see if the specified character is XML whitespace.
	 * @param c The character to check.
	 * @return true if the character is XML whitespace.
	 */
	public static boolean isWhitespace(final char c) {
		return XML.WHITESPACE_CHARS.indexOf(c) != -1; //if the character matches any characters in our whitespace string, the character is whitespace
	}

	/**
	 * Checks to see if the specified character is a letter.
	 * @param c The character to check.
	 * @return true if the character is an XML letter.
	 */
	public static boolean isLetter(final char c) {
		return Characters.isCharInRange(c, XML.BASE_CHAR_RANGES) || Characters.isCharInRange(c, XML.IDEOGRAPHIC_RANGES); //see if the character is a base character or an ideographic character
	}

	/**
	 * Checks to see if the specified character is a digit.
	 * @param c The character to check.
	 * @return true if the character is an XML digit.
	 */
	public static boolean isDigit(final char c) {
		return Characters.isCharInRange(c, XML.DIGIT_RANGES); //see if the character is a digit
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
	 * Determines if the given content type is one representing XML in some form.
	 * <p>
	 * XML media types include:
	 * </p>
	 * <ul>
	 * <li><code>text/xml</code></li>
	 * <li><code>application/xml</code></li>
	 * <li><code>application/*+xml</code></li>
	 * </ul>
	 * @param contentType The content type of a resource, or <code>null</code> for no content type.
	 * @return <code>true</code> if the given content type is one of several XML media types.
	 */
	public static boolean isXML(final ContentType contentType) {
		if(contentType != null) { //if a content type is given
			if(ContentType.TEXT_PRIMARY_TYPE.equals(contentType.getPrimaryType()) && XML_SUBTYPE.equals(contentType.getSubType())) { //if this is "text/xml"
				return true; //text/xml is an XML content type
			}
			if(ContentType.APPLICATION_PRIMARY_TYPE.equals(contentType.getPrimaryType())) { //if this is "application/*"
				return XML_SUBTYPE.equals(contentType.getSubType()) //see if the subtype is "xml"
						|| contentType.hasSubTypeSuffix(XML.XML_SUBTYPE_SUFFIX); //see if the subtype has an XML suffix
			}
		}
		return false; //this is not a media type we recognize as being XML
	}

	/**
	 * Determines if the given content type is one representing an XML external parsed entity in some form.
	 * <p>
	 * XML external parsed entities include:
	 * </p>
	 * <ul>
	 * <li><code>text/xml-external-parsed-entity</code></li>
	 * <li><code>application/xml-external-parsed-entity</code></li>
	 * <li><code>text/*+xml-external-parsed-entity</code> (not formally defined)</li>
	 * <li><code>application/*+xml-external-parsed-entity</code> (not formally defined)</li>
	 * </ul>
	 * @param contentType The content type of a resource, or <code>null</code> for no content type.
	 * @return <code>true</code> if the given content type is one of several XML external parsed entity media types.
	 */
	public static boolean isXMLExternalParsedEntity(final ContentType contentType) {
		if(contentType != null) { //if a content type is given
			final String primaryType = contentType.getPrimaryType(); //get the primary type
			if(ContentType.TEXT_PRIMARY_TYPE.equals(primaryType) || ContentType.APPLICATION_PRIMARY_TYPE.equals(primaryType)) { //if this is "text/*" or "application/*"
				final String subType = contentType.getSubType(); //get the subtype
				return XML_EXTERNAL_PARSED_ENTITY_SUBTYPE.equals(subType) //if the subtype is /xml-external-parsed-entity
						|| contentType.hasSubTypeSuffix(XML.XML_EXTERNAL_PARSED_ENTITY_SUBTYPE_SUFFIX); //or if the subtype has an XML external parsed entity suffix
			}
		}
		return false; //this is not a media type we recognize as being an XML external parsed entity
	}

	/** The character to replace the first character if needed. */
	protected final static char REPLACEMENT_FIRST_CHAR = 'x';

	/** The character to use to replace any other character. */
	protected final static char REPLACEMENT_CHAR = '_';

	/** The special XML symbols that should be replaced with entities. */
	private final static char[] XML_ENTITY_CHARS = { '&', '"', '\'', '>', '<' }; //TODO use constants

	/** The strings to replace XML symbols. */
	private final static String[] XML_ENTITY_REPLACMENTS = { "&amp;", "&quot;", "&apos;", "&gt;", "&lt;" }; //TODO use constants

	/**
	 * Replaces special XML symbols with their escaped versions, (e.g. replaces '&lt' with "&amp;lt;") so that the string is valid XML content.
	 * @param string The string to be manipulated.
	 * @return An XML-friendly string.
	 */
	public static String createValidContent(final String string) {
		return Strings.replace(string, XML_ENTITY_CHARS, XML_ENTITY_REPLACMENTS); //do the replacments for the special XML symbols and return the results
	}

	/**
	 * Creates a string in which all illegal XML characters are replaced with the space character.
	 * @param string The string the characters of which should be checked for XML validity.
	 * @return A new string with illegal XML characters replaced with spaces, or the original string if no characters were replaced.
	 */
	public static String createValidString(final String string) {
		StringBuffer stringBuffer = null; //we'll only create a string buffer if there are invalid characters
		for(int i = string.length() - 1; i >= 0; --i) { //look at all the characters in the string
			if(!isChar(string.charAt(i))) { //if this is not a valid character
				if(stringBuffer == null) //if we haven't create a string buffer, yet
					stringBuffer = new StringBuffer(string); //create a string buffer to hold our replacements
				stringBuffer.setCharAt(i, SPACE_CHAR); //replace this character with a space
			}
		}
		return stringBuffer != null ? stringBuffer.toString() : string; //return the original string unless we've actually modified something
	}

	/**
	 * Creates an XML name by replacing every non-name character with an underscore ('_') character. If the first character of the string cannot begin an XML
	 * name, it will be replaced with an 'x'. An empty string will receive an 'x' as well.
	 * @param string The string to be changed to an XML name.
	 * @return The string modified to be an XML name.
	 */
	public static String createName(final String string) {
		if(isName(string)) //if the string is already a name (we'll check all the characters, assuming that most of the time the strings will already be valid names, making this more efficient)
			return string; //return the string, because it doesn't need to be converted
		else { //if the string isn't a name already (we'll check all the characters, assuming that most of the time the strings will already be valid names, making this more efficient)
			final StringBuffer stringBuffer = new StringBuffer(string); //create a string buffer from the string, so that we can modify it as necessary
			if(stringBuffer.length() == 0) //if the string isn't long enough to be a name
				stringBuffer.append(REPLACEMENT_FIRST_CHAR); //put an 'x' in the first position
			else if(!isNameFirstChar(stringBuffer.charAt(0))) //if the string does have at least one character, but it's not a valid first character for an XML name
				stringBuffer.setCharAt(0, REPLACEMENT_FIRST_CHAR); //replace the first character with an 'x'
			for(int i = 1; i < string.length(); ++i) { //look at each character in the string, except the first (which we've already checked)
				if(!isNameChar(stringBuffer.charAt(i))) //if this character isn't a name character
					stringBuffer.setCharAt(i, REPLACEMENT_CHAR); //replace the character with an underscore
			}
			return stringBuffer.toString(); //return the string we constructed
		}
	}

	/**
	 * Creates a qualified name from a namespace prefix and a local name.
	 * @param prefix The namespace prefix, or <code>null</code> if there is no prefix.
	 * @param localName The XML local name.
	 * @return The XML qualified name.
	 */
	public static String createQName(final String prefix, final String localName) {
		final StringBuilder stringBuilder = new StringBuilder();
		if(prefix != null) { //if there is a prefix defined
			stringBuilder.append(prefix).append(NAMESPACE_DIVIDER); //prepend the prefix and the namespace delimiter
		}
		return stringBuilder.append(localName).toString(); //always append the local name
	}

	/**
	 * Creates a qualified name object from an XML node.
	 * <p>
	 * If the node namespace is not a valid URI (e.g. "DAV:"), it will be converted to a valid URI (e.g. "DAV:/") if possible.
	 * </p>
	 * @param node The XML node from which a qualified name is to be created.
	 * @return A qualified name object representing the given XML node
	 * @throws IllegalArgumentException if the namespace is not <code>null</code> and cannot be converted to a valid URI.
	 * @see #toNamespaceURI(String)
	 */
	public static QualifiedName createQualifiedName(final Node node) {
		return new QualifiedName(node.getNamespaceURI(), node.getPrefix(), node.getLocalName()); //create a qualified name for this node
	}

	/**
	 * Creates a namespace URI from the given namespace string.
	 * <p>
	 * This method attempts to compensate for XML documents that include a namespace string that is not a true URI, notably the <code>DAV:</code> namespace "URI"
	 * used by WebDAV. In such a case as <code>DAV:</code>, the URI <code>DAV:/</code> would be returned.
	 * </p>
	 * @param namespace The namespace string, or <code>null</code> if there is no namespace.
	 * @return A URI representing the namespace, or <code>null</code> if no namespace was given.
	 * @throws IllegalArgumentException if the namespace is not <code>null</code> and cannot be converted to a valid URI.
	 */
	public static URI toNamespaceURI(String namespace) {
		if(namespace == null) {
			return null;
		}
		final int schemeSeparatorIndex = namespace.indexOf(SCHEME_SEPARATOR); //find out where the scheme ends
		if(schemeSeparatorIndex == namespace.length() - 1) { //if the scheme separator is at the end of the string (i.e. there is no scheme-specific part, e.g. "DAV:")
			namespace += URIs.PATH_SEPARATOR; //append a path separator (e.g. "DAV:/")
		}
		return URI.create(namespace); //create a URI from the namespace
	}

	/**
	 * Checks to make sure the given identifier is a valid target for a processing instruction.
	 * @param piTarget The identifier to check for validity.
	 * @return <code>true</code> if the name is a valid processing instruction target, else <code>false</code>.
	 */
	public static boolean isPITarget(final String piTarget) {
		return isName(piTarget) && !piTarget.equalsIgnoreCase("xml"); //a PI target is a valid name that does not equal "XML" in any case
	}

	/**
	 * Adds a stylesheet to the XML document using the standard <code>&lt;<?xml-stylesheet...</code> processing instruction notation.
	 * @param document The document to which the stylesheet reference should be added.
	 * @param href The reference to the stylesheet.
	 * @param mediaType The media type of the stylesheet.
	 */
	public static void addStyleSheetReference(final Document document, final String href, final ContentType mediaType) {
		final String target = XML_STYLESHEET_PROCESSING_INSTRUCTION; //the PI target will be the name of the stylesheet processing instruction
		final StringBuffer dataStringBuffer = new StringBuffer(); //create a string buffer to construct the data parameter (with its pseudo attributes)
		//add: href="href"
		dataStringBuffer.append(HREF_ATTRIBUTE).append(XML.EQUAL_CHAR).append(XML.DOUBLE_QUOTE_CHAR).append(href).append(XML.DOUBLE_QUOTE_CHAR);
		dataStringBuffer.append(SPACE_CHAR); //add a space between the pseudo attributes
		//add: type="type"
		dataStringBuffer.append(TYPE_ATTRIBUTE).append(XML.EQUAL_CHAR).append(XML.DOUBLE_QUOTE_CHAR).append(mediaType).append(XML.DOUBLE_QUOTE_CHAR);
		final String data = dataStringBuffer.toString(); //convert the data string buffer to a string
		final ProcessingInstruction processingInstruction = document.createProcessingInstruction(target, data); //create a processing instruction with the correct information
		document.appendChild(processingInstruction); //append the processing instruction to the document
	}

	/**
	 * Performs a clone on the children of the source node and adds them to the destination node.
	 * @param destinationNode The node that will receive the cloned child nodes.
	 * @param sourceNode The node from whence the nodes will be cloned.
	 * @param deep Whether each child should be deeply cloned.
	 */
	//TODO list exceptions
	public static void appendClonedChildNodes(final Node destinationNode, final Node sourceNode, final boolean deep) {
		final NodeList sourceNodeList = sourceNode.getChildNodes(); //get the list of child nodes
		final int sourceNodeCount = sourceNodeList.getLength(); //find out how many nodes there are
		for(int i = 0; i < sourceNodeCount; ++i) { //look at each of the source nodes
			final Node sourceChildNode = sourceNodeList.item(i); //get a reference to this child node
			destinationNode.appendChild(sourceChildNode.cloneNode(deep)); //clone the node and add it to the destination node
		}
	}

	/**
	 * Performs a deep import on the children of the source node and adds them to the destination node.
	 * @param destinationNode The node that will receive the imported child nodes.
	 * @param sourceNode The node from whence the nodes will be imported.
	 */
	//TODO list exceptions
	public static void appendImportedChildNodes(final Node destinationNode, final Node sourceNode) {
		appendImportedChildNodes(destinationNode, sourceNode, true); //import and append all descendant nodes
	}

	/**
	 * Performs an import on the children of the source node and adds them to the destination node.
	 * @param destinationNode The node that will receive the imported child nodes.
	 * @param sourceNode The node from whence the nodes will be imported.
	 * @param deep Whether each child should be deeply imported.
	 */
	//TODO list exceptions
	public static void appendImportedChildNodes(final Node destinationNode, final Node sourceNode, final boolean deep) {
		final Document destinationDocument = destinationNode.getOwnerDocument(); //get the owner document of the destination node
		final NodeList sourceNodeList = sourceNode.getChildNodes(); //get the list of child nodes
		final int sourceNodeCount = sourceNodeList.getLength(); //find out how many nodes there are
		for(int i = 0; i < sourceNodeCount; ++i) { //look at each of the source nodes
			final Node sourceChildNode = sourceNodeList.item(i); //get a reference to this child node
			destinationNode.appendChild(destinationDocument.importNode(sourceChildNode, deep)); //import the node and add it to the destination node
		}
	}

	/**
	 * Performs a clone on the attributes of the source node and adds them to the destination node. It is assumed that all attributes have been added using
	 * namespace aware methods.
	 * @param destinationElement The element that will receive the cloned child nodes.
	 * @param sourceElement The element that contains the attributes to be cloned.
	 */
	//TODO list exceptions
	public static void appendClonedAttributeNodesNS(final Element destinationElement, final Element sourceElement) {
		final NamedNodeMap attributeNodeMap = sourceElement.getAttributes(); //get the source element's attributes
		final int sourceNodeCount = attributeNodeMap.getLength(); //find out how many nodes there are
		for(int i = 0; i < sourceNodeCount; ++i) { //look at each of the source nodes
			final Node sourceAttributeNode = attributeNodeMap.item(i); //get a reference to this attribute node
			destinationElement.setAttributeNodeNS((Attr)sourceAttributeNode.cloneNode(true)); //clone the attribute and add it to the destination element
		}
	}

	/**
	 * Creates a text node with the specified character and appends it to the specified element.
	 * @param element The element to which text should be added. This element must have a valid owner document.
	 * @param textCharacter The character to add to the element.
	 * @return The new text node that was created.
	 */
	//TODO list exceptions
	public static Text appendText(final Element element, final char textCharacter) {
		return appendText(element, String.valueOf(textCharacter)); //convert the character to a string and append it to the element
	}

	/**
	 * Creates a text node with the specified text and appends it to the specified element.
	 * @param element The element to which text should be added. This element must have a valid owner document.
	 * @param textString The text to add to the element.
	 * @return The new text node that was created.
	 * @throws NullPointerException if the given element and/or text string is <code>null</code>.
	 * @throws DOMException if there was an error appending the text.
	 */
	public static Text appendText(final Element element, final String textString) throws DOMException {
		final Text textNode = element.getOwnerDocument().createTextNode(textString); //create a new text node with the specified text
		element.appendChild(textNode); //append the text node to our paragraph
		return textNode; //return the text node we created
	}

	/**
	 * Convenience function to create an element and use it to replace the document element of the document.
	 * @param document The document which will serve as parent of the newly created element.
	 * @param elementNamespaceURI The namespace URI of the element to be created.
	 * @param elementName The name of the element to create.
	 * @return The newly created child element.
	 */
	//TODO list exceptions
	public static Element replaceDocumentElement(final Document document, final String elementNamespaceURI, final String elementName) {
		return replaceDocumentElementNS(document, elementNamespaceURI, elementName, null); //append an element with no text
	}

	/**
	 * Convenience function to create an element, replace the document element of the given document, and add optional text as a child of the given element. A
	 * heading, for instance, might be added using <code>replaceDocumentElement(document, XHTML_NAMESPACE_URI, ELEMENT_H2,
		"My Heading");</code>.
	 * @param document The document which will serve as parent of the newly created element.
	 * @param elementNamespaceURI The namespace URI of the element to be created.
	 * @param elementName The name of the element to create.
	 * @param textContent The text to add as a child of the created element, or <code>null</code> if no text should be added.
	 * @return The newly created child element.
	 * @throws DOMException if there was an error creating the element, appending the text, or replacing the child.
	 */
	public static Element replaceDocumentElementNS(final Document document, final String elementNamespaceURI, final String elementName, final String textContent) {
		final Element childElement = createElementNS(document, elementNamespaceURI, elementName, textContent); //create the new element
		document.replaceChild(childElement, document.getDocumentElement()); //replace the document element of the document
		return childElement; //return the element we created
	}

	/**
	 * Convenience function to create an element and add it as a child of the given parent element.
	 * @param parentElement The element which will serve as parent of the newly created element. This element must have a valid owner document.
	 * @param elementNamespaceURI The namespace URI of the element to be created.
	 * @param elementName The name of the element to create.
	 * @return The newly created child element.
	 * @throws DOMException if there was an error creating the element or appending the element to the parent element.
	 */
	public static Element appendElementNS(final Element parentElement, final String elementNamespaceURI, final String elementName) {
		return appendElementNS(parentElement, elementNamespaceURI, elementName, null); //append the element with no text
	}

	/**
	 * Convenience function to create an element, add it as a child of the given parent element, and add optional text as a child of the given element. A heading,
	 * for instance, might be added using <code>appendElement(bodyElement, XHTML_NAMESPACE_URI, ELEMENT_H2,
		"My Heading");</code>.
	 * @param parentElement The element which will serve as parent of the newly created element. This element must have a valid owner document.
	 * @param elementNamespaceURI The namespace URI of the element to be created.
	 * @param elementName The name of the element to create.
	 * @param textContent The text to add as a child of the created element, or <code>null</code> if no text should be added.
	 * @return The newly created child element.
	 * @throws DOMException if there was an error creating the element, appending the text, or appending the element to the parent element.
	 */
	public static Element appendElementNS(final Element parentElement, final String elementNamespaceURI, final String elementName, final String textContent) {
		final Element childElement = createElementNS(parentElement.getOwnerDocument(), elementNamespaceURI, elementName, textContent); //create the new element
		parentElement.appendChild(childElement); //add the child element to the parent element
		return childElement; //return the element we created
	}

	/**
	 * Creates a document wrapped around a copy of the given element.
	 * @param element The element to become the document element of the new document.
	 * @return A new document with a clone of the given element as the document element.
	 */
	public static Document createDocument(final Element element) {
		final DOMImplementation domImplementation = element.getOwnerDocument().getImplementation(); //get the DOM implementation used to create the document
		//create a new document corresponding to the element
		//TODO bring over the doctype, if needed
		final Document document = domImplementation.createDocument(element.getNamespaceURI(), element.getNodeName(), null);
		final Node importedNode = document.importNode(element, true); //import the element into our new document
		document.replaceChild(importedNode, document.getDocumentElement()); //set the element clone as the document element of the new document
		return document; //return the document we created
	}

	/**
	 * Convenience function to create an element and add optional text as a child of the given element. A heading, for instance, might be created using
	 * <code>appendElementNS(document, XHTML_NAMESPACE_URI, ELEMENT_H2,
		"My Heading");</code>.
	 * @param document The document to be used to create the new element.
	 * @param elementNamespaceURI The namespace URI of the element to be created.
	 * @param elementName The name of the element to create.
	 * @param textContent The text to add as a child of the created element, or <code>null</code> if no text should be added.
	 * @return The newly created child element.
	 * @throws DOMException if there was an error creating the element or appending the text.
	 * @see Document#createElementNS(String, String)
	 * @see #appendText(Element, String)
	 */
	public static Element createElementNS(final Document document, final String elementNamespaceURI, final String elementName, final String textContent)
			throws DOMException {
		final Element childElement = document.createElementNS(elementNamespaceURI, elementName); //create the new element
		if(textContent != null) { //if we have text content to add
			appendText(childElement, textContent); //append the text content to the newly created child element
		}
		return childElement; //return the element we created
	}

	/**
	 * Extracts a single node from its parent and places it in a document fragment. The node is removed from its parent.
	 * @param node The node to be extracted. This node must have a valid parent and owner document.
	 * @return A new document fragment containing the extracted node.
	 * @throws DOMException <ul>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly.</li>
	 *           </ul>
	 * @throws IllegalArgumentException if the given node has no owner document.
	 * @throws IllegalArgumentException if the given node has no parent node.
	 * @see #removeChildren(Node, int, int)
	 */
	public static DocumentFragment extractNode(final Node node) throws DOMException {
		return extractNode(node, true); //extract the node by removing it
	}

	/**
	 * Extracts a single node from its parent and places it in a document fragment.
	 * @param node The node to be extracted. This node must have a valid parent and owner document.
	 * @param remove Whether the node will be removed from its parent; if <code>false</code>, it will remain the child of its parent.
	 * @return A new document fragment containing the extracted node.
	 * @throws DOMException <ul>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly.</li>
	 *           </ul>
	 * @throws IllegalArgumentException if the given node has no owner document.
	 * @throws IllegalArgumentException if the given node has no parent node.
	 * @see #removeChildren(Node, int, int)
	 */
	public static DocumentFragment extractNode(final Node node, final boolean remove) throws DOMException {
		final Document ownerDocument = node.getOwnerDocument(); //get the node owner document
		if(ownerDocument == null) { //if there is no owner document
			throw new IllegalArgumentException("Node " + node + " has no owner document.");
		}
		final Node parentNode = node.getParentNode(); //get the node's parent
		if(parentNode == null) { //if there is no parent node
			throw new IllegalArgumentException("Node " + node + " has no parent node.");
		}
		final DocumentFragment documentFragment = ownerDocument.createDocumentFragment(); //create a document fragment to hold the nodes
		if(remove) { //if we should remove the node
			parentNode.removeChild(node); //remove the node from its parent
		}
		documentFragment.appendChild(node); //append the removed child to the document fragment
		return documentFragment; //return the document fragment
	}

	/**
	 * Extracts all the child nodes from the given node and places them in a document fragment. The children are removed from their parents.
	 * @param node The node from which child nodes should be extracted. This node must have a valid owner document.
	 * @return A new document fragment containing the extracted children.
	 * @throws DOMException <ul>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly.</li>
	 *           </ul>
	 * @see #removeChildren
	 */
	public static DocumentFragment extractChildren(final Node node) throws DOMException {
		return extractChildren(node, true); //extract the childen by removing them
	}

	/**
	 * Extracts all the child nodes from the given node and places them in a document fragment.
	 * @param node The node from which child nodes should be extracted. This node must have a valid owner document.
	 * @param remove Whether the nodes will be removed from the parentnode ; if <code>false</code>, they will remain the child of the parent node.
	 * @return A new document fragment containing the extracted children.
	 * @throws DOMException <ul>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly.</li>
	 *           </ul>
	 * @see #removeChildren
	 */
	public static DocumentFragment extractChildren(final Node node, final boolean remove) throws DOMException {
		return extractChildren(node, 0, node.getChildNodes().getLength(), remove); //extract all the children and return the new document fragment 
	}

	/**
	 * Extracts the indexed nodes starting at <code>startChildIndex</code> up to but not including <code>endChildIndex</code>. The children are removed from their
	 * parents.
	 * @param node The node from which child nodes should be extracted. This node must have a valid owner document.
	 * @param startChildIndex The index of the first child to extract.
	 * @param endChildIndex The index directly after the last child to extract. Must be greater than <code>startChildIndex</code> or no action will occur.
	 * @return A new document fragment containing the extracted children.
	 * @throws ArrayIndexOutOfBoundsException Thrown if either index is negative, if the start index is greater than or equal to the number of children, or if the
	 *           ending index is greater than the number of children (unless the ending index is not greater than the starting index). TODO should we throw an
	 *           exception is startChildIndex>endChildIndex, like String.substring()?
	 * @throws IllegalArgumentException if the given node has no owner document.
	 * @throws ArrayIndexOutOfBoundsException if the given range is invalid for the given node's children.
	 * @throws DOMException <ul>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly.</li>
	 *           </ul>
	 * @see #removeChildren(Node, int, int)
	 */
	public static DocumentFragment extractChildren(final Node node, final int startChildIndex, final int endChildIndex) throws ArrayIndexOutOfBoundsException,
			DOMException {
		return extractChildren(node, startChildIndex, endChildIndex, true); //extract the childen by removing them
	}

	/**
	 * Extracts the indexed nodes starting at <code>startChildIndex</code> up to but not including <code>endChildIndex</code>.
	 * @param node The node from which child nodes should be extracted. This node must have a valid owner document.
	 * @param startChildIndex The index of the first child to extract.
	 * @param endChildIndex The index directly after the last child to extract. Must be greater than <code>startChildIndex</code> or no action will occur.
	 * @param remove Whether the nodes will be removed from the parentnode ; if <code>false</code>, they will remain the child of the parent node.
	 * @return A new document fragment containing the extracted children.
	 * @throws ArrayIndexOutOfBoundsException Thrown if either index is negative, if the start index is greater than or equal to the number of children, or if the
	 *           ending index is greater than the number of children (unless the ending index is not greater than the starting index). TODO should we throw an
	 *           exception is startChildIndex>endChildIndex, like String.substring()?
	 * @throws IllegalArgumentException if the given node has no owner document.
	 * @throws ArrayIndexOutOfBoundsException if the given range is invalid for the given node's children.
	 * @throws DOMException <ul>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly.</li>
	 *           </ul>
	 * @see #removeChildren(Node, int, int)
	 */
	public static DocumentFragment extractChildren(final Node node, final int startChildIndex, final int endChildIndex, final boolean remove)
			throws ArrayIndexOutOfBoundsException, DOMException {
		final Document ownerDocument = node.getOwnerDocument(); //get the node owner document
		if(ownerDocument == null) { //if there is no owner document
			throw new IllegalArgumentException("Node " + node + " has no owner document.");
		}
		final NodeList childNodeList = node.getChildNodes(); //get a reference to the child nodes
		final int childNodeCount = childNodeList.getLength(); //find out how many child nodes there are
		if(startChildIndex < 0 || (endChildIndex > startChildIndex && startChildIndex >= childNodeCount)) //if the start child index is out of range
			throw new ArrayIndexOutOfBoundsException(startChildIndex); //throw an exception indicating the illegal index
		if(endChildIndex < 0 || (endChildIndex > startChildIndex && endChildIndex > childNodeCount)) //if the ending child index is out of range
			throw new ArrayIndexOutOfBoundsException(endChildIndex); //throw an exception indicating the illegal index
		final DocumentFragment documentFragment = ownerDocument.createDocumentFragment(); //create a document fragment to hold the nodes
		Node lastAddedNode = null; //show that we haven't added any nodes, yet
		for(int i = endChildIndex - 1; i >= startChildIndex; --i) { //starting from the end, look at all the indexes before the ending index
			final Node childNode = childNodeList.item(i); //find the item at the given index
			if(remove) { //if we should remove the node
				node.removeChild(childNode); //remove the child node
			}
			if(lastAddedNode == null) //for the first node we add
				documentFragment.appendChild(childNode); //append the removed child to the document fragment
			else
				//for all other nodes
				documentFragment.insertBefore(childNode, lastAddedNode); //insert this child before the last one
			lastAddedNode = childNode; //show that we just added another node
		}
		return documentFragment; //return the document fragment we created
	}

	/**
	 * Gets the specified attribute value only if it is defined.
	 * @param element The element to check for the specified attribute.
	 * @param name The name of the attribute to retrieve.
	 * @return The attribute value, if the attribute is defined, else <code>null</code>.
	 * @see Element#hasAttribute(String)
	 * @see Element#getAttribute(String)
	 */
	public static String getDefinedAttribute(final Element element, final String name) {
		//retrieve and return the attribute if it exists, else return null
		return element.hasAttribute(name) ? element.getAttribute(name) : null;
	}

	/**
	 * Gets the specified attribute value only if it is defined.
	 * @param element The element to check for the specified attribute.
	 * @param namespaceURI The namespace of the attribute to retrieve.
	 * @param localName The local name of the attribute.
	 * @return The attribute value, if the attribute is defined, else <code>null</code>.
	 * @see Element#hasAttributeNS(String, String)
	 * @see Element#getAttributeNS(String, String)
	 */
	public static String getDefinedAttributeNS(final Element element, final String namespaceURI, final String localName) {
		//retrieve and return the attribute if it exists, else return null
		return element.hasAttributeNS(namespaceURI, localName) ? element.getAttributeNS(namespaceURI, localName) : null;
	}

	/**
	 * Retrieves the first child node of the specified type.
	 * @param node The node of which child elements will be examined.
	 * @param nodeType The type of node to return.
	 * @return The first node of the given type, or <code>null</code> if there is no such node of the given type.
	 */
	public static Node getChildNode(final Node node, final int nodeType) {
		final NodeList childNodeList = node.getChildNodes(); //get a reference to the child nodes
		final int childCount = childNodeList.getLength(); //find out how many children there are
		for(int i = 0; i < childCount; ++i) { //look at each of the children
			final Node childNode = childNodeList.item(i); //get a reference to this node
			if(childNode.getNodeType() == nodeType) { //if this node is of the correct type
				return childNode; //return this child node
			}
		}
		return null; //indicate that no matching nodes were found
	}

	/**
	 * Retrieves the first child node not of the specified type.
	 * @param node The node of which child elements will be examined.
	 * @param nodeType The type of node not to return.
	 * @return The first node not of the given type, or <code>null</code> if there is no node not of the given type.
	 */
	public static Node getChildNodeNot(final Node node, final int nodeType) {
		final NodeList childNodeList = node.getChildNodes(); //get a reference to the child nodes
		final int childCount = childNodeList.getLength(); //find out how many children there are
		for(int i = 0; i < childCount; ++i) { //look at each of the children
			final Node childNode = childNodeList.item(i); //get a reference to this node
			if(childNode.getNodeType() != nodeType) { //if this node is not of the specified type
				return childNode; //return this child node
			}
		}
		return null; //indicate that no non-matching nodes were found
	}

	/**
	 * Retrieves the nodes contained in child nodes of type {@link Node#ELEMENT_NODE}.
	 * @param node The node from which child elements will be returned.
	 * @see Node#ELEMENT_NODE
	 */
	public static List<Element> getChildElements(final Node node) {
		final List<Element> childElements = new ArrayList<Element>(); //create a list to hold the elements
		final NodeList childNodeList = node.getChildNodes(); //get a reference to the child nodes
		final int childCount = childNodeList.getLength(); //find out how many children there are
		for(int i = 0; i < childCount; ++i) { //look at each of the children
			final Node childNode = childNodeList.item(i); //get a reference to this node
			switch(childNode.getNodeType()) { //see which type of node this is
				case Node.ELEMENT_NODE: //if this is an element
					childElements.add((Element)childNode); //add this child element
					break;
			}
		}
		return childElements; //return the child element we collected
	}

	/**
	 * Returns a list of child nodes with a given type and node name. The special wildcard name "*" returns nodes of all names. If <code>deep</code> is set to
	 * <code>true</code>, returns a list of all descendant nodes with a given name, in the order in which they would be encountered in a preorder traversal of the
	 * node tree.
	 * @param node The node the child nodes of which will be searched.
	 * @param nodeType The type of nodes to include.
	 * @param nodeName The name of the node to match on. The special value "*" matches all nodes.
	 * @param deep Whether or not matching child nodes of each matching child node, etc. should be included.
	 * @return A new iterator object containing all the matching nodes.
	 */
	public static List<Node> getNodesByName(final Node node, final int nodeType, final String nodeName, final boolean deep) {
		final List<Node> nodeList = new ArrayList<Node>(); //crate a new node list to return
		final boolean matchAllNodes = "*".equals(nodeName); //see if they passed us the wildcard character TODO use a constant here
		final NodeList childNodeList = node.getChildNodes(); //get the list of child nodes
		final int childNodeCount = childNodeList.getLength(); //get the number of child nodes
		for(int childIndex = 0; childIndex < childNodeCount; childIndex++) { //look at each child node
			final Node childNode = childNodeList.item(childIndex); //get a reference to this node
			if(childNode.getNodeType() == nodeType) { //if this is a node of the correct type
				if((matchAllNodes || childNode.getNodeName().equals(nodeName))) //if node has the correct name (or they passed us the wildcard character)
					nodeList.add(childNode); //add this node to the list
				if(deep) //if each of the children should check for matching nodes as well
					nodeList.addAll(getNodesByName(childNode, nodeType, nodeName, deep)); //get this node's matching child nodes by name and add them to our list
			}
		}
		return nodeList; //return the list we created and filled
	}

	/**
	 * Returns a list of child nodes with a given type, namespace URI, and local name. The special wildcard name "*" returns nodes of all local names. If
	 * <code>deep</code> is set to <code>true</code>, returns a list of all descendant nodes with a given name, in the order in which they would be encountered in
	 * a preorder traversal of the node tree.
	 * @param node The node the child nodes of which will be searched.
	 * @param nodeType The type of nodes to include.
	 * @param namespaceURI The URI of the namespace of nodes to return. The special value "*" matches all namespaces.
	 * @param localName The local name of the node to match on. The special value "*" matches all local names.
	 * @param deep Whether or not matching child nodes of each matching child node, etc. should be included.
	 * @return A new list containing all the matching nodes.
	 */
	public static List<Node> getNodesByNameNS(final Node node, final int nodeType, final String namespaceURI, final String localName, final boolean deep) {
		return getNodesByNameNS(node, nodeType, namespaceURI, localName, deep, new ArrayList<Node>()); //gather the nodes into a list and return the list
	}

	/**
	 * Gathers child nodes with a given type, namespace URI, and local name. The special wildcard name "*" returns nodes of all local names. If <code>deep</code>
	 * is set to <code>true</code>, returns a list of all descendant nodes with a given name, in the order in which they would be encountered in a preorder
	 * traversal of the node tree.
	 * @param node The node the child nodes of which will be searched.
	 * @param nodeType The type of nodes to include.
	 * @param namespaceURI The URI of the namespace of nodes to return. The special value "*" matches all namespaces.
	 * @param localName The local name of the node to match on. The special value "*" matches all local names.
	 * @param deep Whether or not matching child nodes of each matching child node, etc. should be included.
	 * @param nodes The collection into which the nodes will be gathered.
	 * @return A new list containing all the matching nodes.
	 */
	public static <C extends Collection<Node>> C getNodesByNameNS(final Node node, final int nodeType, final String namespaceURI, final String localName,
			final boolean deep, final C nodes) {
		final boolean matchAllNamespaces = "*".equals(namespaceURI); //see if they passed us the wildcard character for the namespace URI TODO use a constant here
		final boolean matchAllLocalNames = "*".equals(localName); //see if they passed us the wildcard character for the local name TODO use a constant here
		final NodeList childNodeList = node.getChildNodes(); //get the list of child nodes
		final int childNodeCount = childNodeList.getLength(); //get the number of child nodes
		for(int childIndex = 0; childIndex < childNodeCount; childIndex++) { //look at each child node
			final Node childNode = childNodeList.item(childIndex); //get a reference to this node
			if(childNode.getNodeType() == nodeType) { //if this is a node of the correct type
				final String nodeNamespaceURI = childNode.getNamespaceURI(); //get the node's namespace URI
				final String nodeLocalName = childNode.getLocalName(); //get the node's local name
				if(matchAllNamespaces || Objects.equals(namespaceURI, nodeNamespaceURI)) { //if we should match all namespaces, or the namespaces match
					if(matchAllLocalNames || localName.equals(nodeLocalName)) { //if we should match all local names, or the local names match
						nodes.add(childNode); //add this node to the list
					}
				}
				if(deep) { //if each of the children should check for matching nodes as well
					getNodesByNameNS(childNode, nodeType, namespaceURI, localName, deep, nodes); //get this node's matching child nodes by name and add them to our collection
				}
			}
		}
		return nodes; //return the collection we filled
	}

	/**
	 * Retrieves the value of a processing instruction's pseudo attribute.
	 * @param processingInstructionData The data of a processing instruction.
	 * @param pseudoAttributeName The name of the pseudo attribute the value of which to retrieve.
	 * @return The string value of the given pseudo attribute, or <code>null</code> if that attribute is not defined in the processing instruction's data.
	 */
	public static String getProcessingInstructionPseudoAttributeValue(final String processingInstructionData, final String pseudoAttributeName) {
		final StringTokenizer tokenizer = new StringTokenizer(processingInstructionData, XML.WHITESPACE_CHARS); //create a tokenizer that separates tokens based on XML whitespace
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

	/**
	 * Retrieves the text of the node contained in child nodes of type {@link Node#TEXT_NODE}, extracting text deeply.
	 * @param node The node from which text will be retrieved.
	 * @return The data of all <code>Text</code> descendant nodes, which may be the empty string.
	 * @see Node#TEXT_NODE
	 * @see Text#getData()
	 */
	public static String getText(final Node node) {
		return getText(node, true); //get text deeply
	}

	/**
	 * Retrieves the text of the node contained in child nodes of type {@link Node#TEXT_NODE}, extracting text deeply.
	 * @param node The node from which text will be retrieved.
	 * @param blockElementNames The names of elements considered "block" elements, which will be separated from other elements using whitespace.
	 * @return The data of all <code>Text</code> descendant nodes, which may be the empty string.
	 * @see Node#TEXT_NODE
	 * @see Text#getData()
	 */
	public static String getText(final Node node, final Set<String> blockElementNames) {
		final StringBuilder stringBuilder = new StringBuilder(); //create a string buffer to collect the text data
		getText(node, blockElementNames, true, stringBuilder); //collect the text in the string buffer
		return stringBuilder.toString(); //convert the string buffer to a string and return it
	}

	/**
	 * Retrieves the text of the node contained in child nodes of type {@link Node#TEXT_NODE}. If <code>deep</code> is set to <code>true</code> the text of all
	 * descendant nodes in document (depth-first) order; otherwise, only text of direct children will be returned.
	 * @param node The node from which text will be retrieved.
	 * @param deep Whether text of all descendants in document order will be returned.
	 * @return The data of all <code>Text</code> children nodes, which may be the empty string.
	 * @see Node#TEXT_NODE
	 * @see Text#getData()
	 */
	public static String getText(final Node node, final boolean deep) {
		final StringBuilder stringBuilder = new StringBuilder(); //create a string buffer to collect the text data
		getText(node, Collections.<String> emptySet(), deep, stringBuilder); //collect the text in the string buffer
		return stringBuilder.toString(); //convert the string buffer to a string and return it
	}

	/**
	 * Retrieves the text of the node contained in child nodes of type <code>Node.Text</code>. If <code>deep</code> is set to <code>true</code> the text of all
	 * descendant nodes in document (depth-first) order; otherwise, only text of direct children will be returned.
	 * @param node The node from which text will be retrieved.
	 * @param blockElementNames The names of elements considered "block" elements, which will be separated from other elements using whitespace.
	 * @param deep Whether text of all descendants in document order will be returned.
	 * @param stringBuilder The buffer to which text will be added.
	 * @return The given string builder.
	 * @see Node#TEXT_NODE
	 * @see Text#getData()
	 */
	public static StringBuilder getText(final Node node, final Set<String> blockElementNames, final boolean deep, final StringBuilder stringBuilder) {
		final NodeList childNodeList = node.getChildNodes(); //get a reference to the child nodes
		final int childCount = childNodeList.getLength(); //find out how many children there are
		for(int i = 0; i < childCount; ++i) { //look at each of the children
			final Node childNode = childNodeList.item(i); //get a reference to this node
			switch(childNode.getNodeType()) { //see which type of node this is
				case Node.TEXT_NODE: //if this is a text node
					stringBuilder.append(((Text)childNode).getData()); //append this text node data to the string buffer
					break;
				case Node.CDATA_SECTION_NODE: //if this is a CDATA node
					stringBuilder.append(((Text)childNode).getData()); //append this text node data to the string buffer
					break;
				case Node.ELEMENT_NODE: //if this is an element
					if(deep) { //if we should get deep text
						final boolean isBlockElement = !blockElementNames.isEmpty() && blockElementNames.contains(node.getNodeName()); //separate block elements
						if(isBlockElement) {
							stringBuilder.append(' ');
						}
						getText((Element)childNode, blockElementNames, deep, stringBuilder); //append the text of this element
						if(isBlockElement) {
							stringBuilder.append(' ');
						}
					}
					break;
			}
		}
		return stringBuilder;
	}

	/**
	 * Determines whether the given element has an ancestor with the given namespace and name.
	 * @param element The element the ancestors of which to check.
	 * @param ancestorElementNamespaceURI The namespace URI of the ancestor element to check for.
	 * @param ancestorElementName The name of the ancestor element to check for.
	 * @return <code>true</code> if an ancestor element with the given namespace URI and name was found.
	 */
	public static boolean hasAncestorElementNS(Element element, final String ancestorElementNamespaceURI, final String ancestorElementName) {
		while((element = asInstance(element.getParentNode(), Element.class)) != null) { //keep looking at parents until we run out of elements and hit the document
			if(Objects.equals(element.getNamespaceURI(), ancestorElementNamespaceURI) && element.getNodeName().equals(ancestorElementName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Creates and inserts a new element encompassing the text of a given text node.
	 * @param textNode The text node to split into a new element.
	 * @param element The element to insert.
	 * @param startIndex The index of the first character to include in the element.
	 * @param endIndex The index immediately after the last character to include in the element.
	 * @return The inserted element.
	 * @throws DOMException <ul>
	 *           <li>INDEX_SIZE_ERR: Raised if the specified offset is negative or greater than the number of 16-bit units in <code>data</code>.</li>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly.</li>
	 *           </ul>
	 */
	public static Element insertElement(final Text textNode, final Element element, final int startIndex, final int endIndex) throws DOMException {
		final Text splitTextNode = splitText(textNode, startIndex, endIndex); //split the text node into pieces
		final Element parentElement = (Element)splitTextNode.getParentNode(); //get the text node's parent
		parentElement.replaceChild(element, splitTextNode); //replace the split text node with the element that will enclose it
		element.appendChild(splitTextNode); //add the split text node to the given element
		return element; //return the inserted enclosing element
	}

	/**
	 * Splits a text node into one, two, or three text nodes and replaces the original text node with the new ones.
	 * @param textNode The text node to split.
	 * @param startIndex The index of the first character to be split.
	 * @param endIndex The index immediately after the last character to split.
	 * @return The new text node that contains the text selected by the start and ending indexes.
	 * @throws DOMException <ul>
	 *           <li>INDEX_SIZE_ERR: Raised if the specified offset is negative or greater than the number of 16-bit units in <code>data</code>.</li>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly.</li>
	 *           </ul>
	 */
	public static Text splitText(Text textNode, int startIndex, int endIndex) throws DOMException {
		if(startIndex > 0) { //if the split text doesn't begin at the start of the text
			textNode = textNode.splitText(startIndex); //split off the first part of the text
			endIndex -= startIndex; //the ending index will now slide back because the start index is sliding back
			startIndex = 0; //we'll do the next split at the first of this string
		}
		if(endIndex < textNode.getLength()) { //if there will be text left after the split
			textNode.splitText(endIndex); //split off the text after the node
		}
		return textNode; //return the node in the middle
	}

	/**
	 * Removes the specified child node from the parent node, and promoting all the children of the child node to be children of the parent node.
	 * @param parentNode The parent of the node to remove.
	 * @param childNode The node to remove, promoting its children in the process. //TODO list exceptions
	 */
	public static void pruneChild(final Node parentNode, final Node childNode) { //TODO maybe rename to excise child
		//promote all the child node's children to be children of the parent node
		while(childNode.hasChildNodes()) { //while the child node has children
			final Node node = childNode.getFirstChild(); //get the first child of the node
			childNode.removeChild(node); //remove the child's child
			parentNode.insertBefore(node, childNode); //insert the child's child before its parent (the parent node's child)
		}
		parentNode.removeChild(childNode); //remove the child, now that its children have been promoted
	}

	/**
	 * Removes the indexed nodes starting at <code>startChildIndex</code> up to but not including <code>endChildIndex</code>.
	 * @param node The node from which child nodes should be removed.
	 * @param startChildIndex The index of the first child to remove.
	 * @param endChildIndex The index directly after the last child to remove. Must be greater than <code>startChildIndex</code> or no action will occur.
	 * @throws ArrayIndexOutOfBoundsException Thrown if either index is negative, if the start index is greater than or equal to the number of children, or if the
	 *           ending index is greater than the number of children. TODO should we throw an exception is startChildIndex>endChildIndex, like String.substring()?
	 * @throws DOMException <ul>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly.</li>
	 *           </ul>
	 */
	public static void removeChildren(final Node node, final int startChildIndex, final int endChildIndex) throws ArrayIndexOutOfBoundsException, DOMException {
		final NodeList childNodeList = node.getChildNodes(); //get a reference to the child nodes
		final int childNodeCount = childNodeList.getLength(); //find out how many child nodes there are
		if(startChildIndex < 0 || startChildIndex >= childNodeCount) //if the start child index is out of range
			throw new ArrayIndexOutOfBoundsException(startChildIndex); //throw an exception indicating the illegal index
		if(endChildIndex < 0 || endChildIndex > childNodeCount) //if the ending child index is out of range
			throw new ArrayIndexOutOfBoundsException(endChildIndex); //throw an exception indicating the illegal index
		for(int i = endChildIndex - 1; i >= startChildIndex; --i)
			//starting from the end, look at all the indexes before the ending index
			node.removeChild(childNodeList.item(i)); //find the item at the given index and remove it
	}

	/**
	 * Removes all named child nodes deeply.
	 * @param node The node the named children of which should be removed.
	 * @param nodeNames The names of the nodes to remove.
	 * @throws NullPointerException if the given node and/or node names is <code>null</code>.
	 */
	public static void removeChildrenByName(final Node node, final Set<String> nodeNames) {
		removeChildrenByName(node, nodeNames, true);
	}

	/**
	 * Removes all named child nodes.
	 * @param node The node the named children of which should be removed.
	 * @param nodeNames The names of the nodes to remove.
	 * @param deep If all descendants should be examined.
	 * @throws NullPointerException if the given node and/or node names is <code>null</code>.
	 */
	public static void removeChildrenByName(final Node node, final Set<String> nodeNames, final boolean deep) {
		final NodeList childNodeList = node.getChildNodes(); //get the list of child nodes
		for(int childIndex = childNodeList.getLength() - 1; childIndex >= 0; childIndex--) { //look at each child node in reverse to prevent problems from removal
			final Node childNode = childNodeList.item(childIndex); //get a reference to this node
			if(nodeNames.contains(childNode.getNodeName())) { //if this node is to be removed
				node.removeChild(childNode); //remove it
			} else if(deep) { //if we should remove deeply
				removeChildrenByName(childNode, nodeNames, deep);
			}
		}
	}

	/**
	 * Removes all child elements with the given name and attribute value.
	 * @param node The node the named child elements of which should be removed.
	 * @param elementName The names of the elements to remove.
	 * @param attributeName The name of the attribute to check.
	 * @param attributeValue The value of the attribute indicating removal.
	 */
	public static void removeChildElementsByNameAttribute(final Node node, final String elementName, final String attributeName, final String attributeValue) {
		removeChildElementsByNameAttribute(node, elementName, attributeName, attributeValue, true);
	}

	/**
	 * Removes all child elements with the given name and attribute value.
	 * @param node The node the named child elements of which should be removed.
	 * @param elementName The names of the elements to remove.
	 * @param attributeName The name of the attribute to check.
	 * @param attributeValue The value of the attribute indicating removal.
	 * @param deep If all descendants should be examined.
	 */
	public static void removeChildElementsByNameAttribute(final Node node, final String elementName, final String attributeName, final String attributeValue,
			final boolean deep) {
		final NodeList childNodeList = node.getChildNodes(); //get the list of child nodes
		for(int childIndex = childNodeList.getLength() - 1; childIndex >= 0; childIndex--) { //look at each child node in reverse to prevent problems from removal
			final Node childNode = childNodeList.item(childIndex); //get a reference to this node
			if(childNode.getNodeType() == Node.ELEMENT_NODE && childNode.getNodeName().equals(elementName)
					&& ((Element)childNode).getAttribute(attributeName).equals(attributeValue)) { //if this node is to be removed
				node.removeChild(childNode); //remove it
			} else if(deep) { //if we should remove deeply
				removeChildElementsByNameAttribute(childNode, elementName, attributeName, attributeValue, deep);
			}
		}
	}

	/**
	 * Renames an element by creating a new element with the specified name, cloning the original element's children, and replacing the original element with the
	 * new, renamed clone. While this method's purpose is renaming, because of DOM restrictions it must remove the element and replace it with a new one, which is
	 * reflected by the method's name.
	 * @param element The element to rename.
	 * @param namespaceURI The new element namespace.
	 * @param localName The new element local name.
	 * @return The new element with the specified name which replaced the old element. //TODO list exceptions
	 */
	public static Element replaceElementNS(final Element element, final String namespaceURI, final String localName) {
		final Document document = element.getOwnerDocument(); //get the owner document
		final Element newElement = document.createElementNS(namespaceURI, localName); //create the new element
		appendClonedAttributeNodesNS(newElement, element); //clone the attributes TODO testing
		appendClonedChildNodes(newElement, element, true); //deep-clone the child nodes of the element and add them to the new element
		final Node parentNode = element.getParentNode(); //get the parent node, which we'll need for the replacement
		parentNode.replaceChild(newElement, element); //replace the old element with the new one
		return newElement; //return the element we created
	}

	//TODO fix, comment private static final int tabDelta=2;	//
	private static final String tabString = "|\t"; //TODO fix to adjust automatically to tabDelta, comment

	/**
	 * Prints a tree representation of the document to the standard output.
	 * @param document The document to print.
	 * @param printStream The stream (e.g. <code>System.out</code>) to use for printing the tree.
	 */
	public static void printTree(final Document document, final PrintStream printStream) {
		if(document != null) //if we have a root element
			printTree(document.getDocumentElement(), 0, printStream); //dump the contents of the root element
		else
			//if we don't have a root element
			printStream.println("Empty document."); //TODO fix, comment, i18n
	}

	/**
	 * Prints a tree representation of the element to the standard output.
	 * @param element The element to print.
	 * @param printStream The stream (e.g. <code>System.out</code>) to use for printing the tree.
	 */
	public static void printTree(final Element element, final PrintStream printStream) {
		printTree(element, 0, printStream); //if we're called normally, we'll dump starting at the first tab position
	}

	/**
	 * Prints a tree representation of the element to the standard output starting at the specified tab position.
	 * @param element The element to print.
	 * @param tabPos The zero-based tab position to which to align.
	 * @param printStream The stream (e.g. <code>System.out</code>) to use for printing the tree.
	 */
	protected static void printTree(final Element element, int tabPos, final PrintStream printStream) {
		for(int i = 0; i < tabPos; ++i)
			printStream.print(tabString); //TODO fix to adjust automatically to tabDelta, comment
		printStream.print("[Element] "); //TODO fix to adjust automatically to tabDelta, comment
		printStream.print("<" + element.getNodeName()); //print the element name

		final NamedNodeMap attributeMap = element.getAttributes(); //get the attributes
		for(int i = attributeMap.getLength() - 1; i >= 0; --i) { //look at each attribute
			final Attr attribute = (Attr)attributeMap.item(i); //get a reference to this attribute
			printStream.print(" " + attribute.getName() + "=\"" + attribute.getValue() + "\""); //print the attribute and its value
			printStream.print(" (" + attribute.getNamespaceURI() + ")"); //print the attribute namespace
		}
		if(element.getChildNodes().getLength() == 0) //if there are no child nodes
			printStream.print('/'); //show that this is an empty element
		printStream.println("> (namespace URI=\"" + element.getNamespaceURI() + "\" local name=\"" + element.getLocalName() + "\")");
		if(element.getChildNodes().getLength() > 0) { //if there are child nodes
			for(int childIndex = 0; childIndex < element.getChildNodes().getLength(); childIndex++) { //look at each child node
				Node node = element.getChildNodes().item(childIndex); //look at this node
				printTree(node, tabPos, printStream); //print the node to the stream
				//TODO process the child elements
			}

			for(int i = 0; i < tabPos; ++i)
				printStream.print(tabString); //TODO fix to adjust automatically to tabDelta, comment
			printStream.print("[/Element] "); //TODO fix to adjust automatically to tabDelta, comment
			printStream.println("</" + element.getNodeName() + '>');
		}
	}

	/**
	 * Prints a tree representation of the node to the given pring stream starting at the specified tab position.
	 * @param node The node to print.
	 * @param printStream The stream (e.g. <code>System.out</code>) to use for printing the tree.
	 */
	public static void printTree(final Node node, final PrintStream printStream) {
		printTree(node, 0, printStream); //if we're called normally, we'll dump starting at the first tab position
	}

	/**
	 * Prints a tree representation of the node to the given pring stream starting at the specified tab position.
	 * @param node The node to print.
	 * @param tabPos The zero-based tab position to which to align.
	 * @param printStream The stream (e.g. <code>System.out</code>) to use for printing the tree.
	 */
	protected static void printTree(final Node node, int tabPos, final PrintStream printStream) {
		switch(node.getNodeType()) { //see which type of object this is
			case Node.ELEMENT_NODE: //if this is an element

				//TODO fix for empty elements

				//TODO del tabPos+=tabDelta;	//TODO check this; maybe static classes don't have recursive-aware functions
				printTree((Element)node, tabPos + 1, printStream); //comment, check to see if we need the typecast
				//TODO del tabPos-=tabDelta;	//TODO check this; maybe static classes don't have recursive-aware functions
				break;
			case Node.TEXT_NODE: //if this is a text node
				for(int i = 0; i < tabPos + 1; ++i)
					printStream.print(tabString); //TODO fix to adjust automatically to tabDelta, comment
				printStream.print("[Text] "); //TODO fix to adjust automatically to tabDelta, comment
				printStream.println(Strings.replace(node.getNodeValue(), '\n', "\\n")); //print the text of this node
				break;
			case Node.COMMENT_NODE: //if this is a comment node
				for(int i = 0; i < tabPos + 1; i += ++i)
					printStream.print(tabString); //TODO fix to adjust automatically to tabDelta, comment
				printStream.print("[Comment] "); //TODO fix to adjust automatically to tabDelta, comment
				printStream.println(Strings.replace(node.getNodeValue(), '\n', "\\n")); //print the text of this node
				break;
		}
	}

	/**
	 * Converts an XML document to a string. If an error occurs converting the document to a string, the normal object string will be returned.
	 * @param document The XML document to convert.
	 * @return A string representation of the XML document.
	 */
	public static String toString(final Document document) {
		try {
			return new XMLSerializer(true).serialize(document); //serialize the document to a string, formatting the XML output
		} catch(final IOException ioException) { //if an IO exception occurs
			return ioException.getMessage() + ' ' + document.toString(); //ask the document to convert itself to a string
		}
	}

	/**
	 * Converts an XML element to a string. If an error occurs converting the element to a string, the normal object string will be returned.
	 * @param element The XML elment to convert.
	 * @return A string representation of the XML element.
	 */
	public static String toString(final Element element) {
		return new XMLSerializer(true).serialize(element); //serialize the element to a string, formatting the XML output
	}

	/**
	 * Determines the default XML namespace for the given MIME content type.
	 * @param mediaType The media type for which a default namespace should be found, or <code>null</code> if the media type is not known.
	 * @return The default XML namespace URI used by resources of the given content type, or <code>null</code> if there is no default namespace URI or the default
	 *         namespace URI is not known.
	 */
	public static URI getDefaultNamespaceURI(final ContentType mediaType) {
		if(mediaType != null) { //if we were given a valid media type
			if(XHTML.isHTML(mediaType)) //if this is one of the HTML media types
				return XHTML.XHTML_NAMESPACE_URI; //return the XHTML media type
			else if(mediaType.match(ContentType.TEXT_PRIMARY_TYPE, OEB.X_OEB1_DOCUMENT_SUBTYPE)) //if this is an OEB 1.x document
				return OEB.OEB1_DOCUMENT_NAMESPACE_URI; //return the OEB 1.x document namespace
		}
		return null; //show that we can't find a default namespace URI
	}

	/**
	 * Searches the attributes of the given node for a definition of a namespace URI for the given prefix. If the prefix is not defined for the given element, its
	 * ancestors are searched if requested. If the prefix is not defined anywhere up the hieararchy, <code>null</code> is returned. If the prefix is defined, it
	 * is returned logically: a blank declared namespace will return <code>null</code>.
	 * @param element The element which should be searched for a namespace definition, along with its ancestors.
	 * @param prefix The namespace prefix for which a definition should be found, or <code>null</code> for a default attribute.
	 * @param resolve Whether the entire tree hierarchy should be searched.
	 * @return The defined namespace URI for the given prefix, or <code>null</code> if none is defined.
	 */
	public static String getNamespaceURI(final Element element, final String prefix, final boolean resolve) {
		//get the namespace URI declared for this prefix
		final String namespaceURI = getDeclaredNamespaceURI(element, prefix, resolve);
		if(namespaceURI != null && namespaceURI.length() > 0) { //if a namespace is defined that isn't the empty string
			return namespaceURI; //return that namespace URI
		} else { //if the namespace is null or is the empty string
			return null; //the empty string is the same as a null namespace
		}
	}

	/**
	 * Searches the attributes of the given node for a definition of a namespace URI for the given prefix. If the prefix is not defined for the given element, its
	 * ancestors are searched if requested. If the prefix is not defined anywhere up the hieararchy, <code>null</code> is returned. If the prefix is defined, it
	 * is returned literally: a blank declared namespace will return the empty string. This allows differentiation between a declared empty namespace and no
	 * declared namespace.
	 * @param element The element which should be searched for a namespace definition, along with its ancestors.
	 * @param prefix The namespace prefix for which a definition should be found, or <code>null</code> for a default attribute.
	 * @param resolve Whether the entire tree hierarchy should be searched.
	 * @return The defined namespace URI for the given prefix, or <code>null</code> if none is defined.
	 */
	public static String getDeclaredNamespaceURI(final Element element, final String prefix, final boolean resolve) {
		String namespaceURI = null; //assume we won't find a matching namespace
		if(prefix != null) { //if they specified a prefix
			if(prefix.equals(XML.XMLNS_NAMESPACE_PREFIX)) //if this is the "xmlns" prefix
				return XML.XMLNS_NAMESPACE_URI.toString(); //return the namespace URI for "xmlns:"
			else if(prefix.equals(XML.XML_NAMESPACE_PREFIX)) //if this is the "xml" prefix
				return XML.XML_NAMESPACE_URI.toString(); //return the namespace URI for "xml:"
			//see if this element has "xmlns:prefix" defined, and if so, retrieve it
			if(element.hasAttributeNS(XML.XMLNS_NAMESPACE_URI.toString(), prefix)) //G***fix for empty namespace strings
				namespaceURI = element.getAttributeNS(XML.XMLNS_NAMESPACE_URI.toString(), prefix);
		} else { //if no prefix was specified
			//see if there is an "xmlns" attribute defined in the "http://www.w3.org/2000/xmlns/" namespace
			if(element.hasAttributeNS(XML.XMLNS_NAMESPACE_URI.toString(), XML.XMLNS_NAMESPACE_PREFIX))
				namespaceURI = element.getAttributeNS(XML.XMLNS_NAMESPACE_URI.toString(), XML.XMLNS_NAMESPACE_PREFIX);
		}
		//if we didn't find a matching namespace definition for this node, search up the chain
		//(unless no prefix was specified, and we can't use the default namespace)
		if(namespaceURI == null) {
			final Node parentNode = element.getParentNode(); //get the parent node
			//if we should resolve, there is a parent, and it's an element (not the document)
			if(resolve && parentNode != null && parentNode.getNodeType() == Node.ELEMENT_NODE) {
				namespaceURI = getDeclaredNamespaceURI((Element)parentNode, prefix, resolve); //continue the search up the chain
			}
		}
		return namespaceURI; //return the namespace URI we found
	}

	/**
	 * Checks to ensure that all namespaces for the element and its attributes are properly declared using the appropriate <code>xmlns=</code> or
	 * <code>xmlns:prefix=</code> attribute declaration.
	 * @param element The element the namespace of which to declare.
	 */
	public static void ensureNamespaceDeclarations(final Element element) {
		ensureNamespaceDeclarations(element, null, false); //ensure namespace declarations only for this element and its attributes, adding any declarations to the element itself
	}

	/**
	 * Checks to ensure that all namespaces for the element and its attributes are properly declared using the appropriate <code>xmlns=</code> or
	 * <code>xmlns:prefix=</code> attribute declaration. The children of this element are optionally checked.
	 * @param element The element the namespace of which to declare.
	 * @param declarationElement The element on which to declare missing namespaces, or <code>null</code> if namespaces should always be declared on the element
	 *          on which they are found missing.
	 * @param deep Whether all children and their descendants are also recursively checked for namespace declarations.
	 */
	public static void ensureNamespaceDeclarations(final Element element, final Element declarationElement, final boolean deep) {
		final NameValuePair<String, String>[] prefixNamespacePairs = getUndeclaredNamespaces(element); //get the undeclared namespaces for this element
		declareNamespaces(declarationElement != null ? declarationElement : element, prefixNamespacePairs); //declare the undeclared namespaces, using the declaration element if provided
		if(deep) { //if we should recursively check the children of this element
			final NodeList childElementList = element.getChildNodes(); //get a list of the child nodes
			for(int i = 0; i < childElementList.getLength(); ++i) { //look at each node
				final Node node = childElementList.item(i); //get a reference to this node
				if(node.getNodeType() == Node.ELEMENT_NODE) { //if this is an element
					ensureNamespaceDeclarations((Element)node, declarationElement, deep); //process the namespaces for this element and its descendants
				}
			}
		}
	}

	/**
	 * Checks to ensure that all namespaces for the <em>child elements</em> and their attributes are properly declared using the appropriate <code>xmlns=</code>
	 * or <code>xmlns:prefix=</code> attribute declaration. If a child element does not have a necessary namespace declaration, the declaration is added to the
	 * same parent element all the way down the hierarchy if there are no conflicts. If there is a conflict, the namespace is added to the child element itself.
	 * <p>
	 * This method is useful for adding namespace attributes to the top level of a fragment that contains unknown content that preferably should be lef
	 * undisturbed.
	 * </p>
	 * @param parentElement The element to which all child namespaces will be added if there are no conflicts.
	 */
	public static void ensureChildNamespaceDeclarations(final Element parentElement) {
		ensureChildNamespaceDeclarations(parentElement, parentElement); //ensure the child namespace declarations for all children of this element, showing that this element is the parent
	}

	/**
	 * Checks to ensure that all namespaces for the <em>child elements</em> and their attributes are properly declared using the appropriate <code>xmlns=</code>
	 * or <code>xmlns:prefix=</code> attribute declaration. If a child element does not have a necessary namespace declaration, the declaration is added to the
	 * same parent element all the way down the hierarchy if there are no conflicts. If there is a conflict, the namespace is added to the child element itself.
	 * <p>
	 * This method is useful for adding namespace attributes to the top level of a fragment that contains unknown content that preferably should be lef
	 * undisturbed.
	 * </p>
	 * @param rootElement The element to which all child namespaces will be added if there are no conflicts.
	 * @param parentElement The element the children of which are currently being checked.
	 */
	protected static void ensureChildNamespaceDeclarations(final Element rootElement, final Element parentElement) {
		final NodeList childElementList = parentElement.getChildNodes(); //get a list of the child nodes
		for(int childIndex = 0; childIndex < childElementList.getLength(); ++childIndex) { //look at each child node
			final Node childNode = childElementList.item(childIndex); //get a reference to this node
			if(childNode.getNodeType() == Node.ELEMENT_NODE) { //if this is an element
				final Element childElement = (Element)childNode; //cast the node to an element
				final NameValuePair<String, String>[] prefixNamespacePairs = getUndeclaredNamespaces(childElement); //get the undeclared namespaces for the child element
				for(int i = 0; i < prefixNamespacePairs.length; ++i) { //look at each name/value pair
					final NameValuePair<String, String> prefixNamespacePair = prefixNamespacePairs[i]; //get this name/value pair representing a prefix and a namespace
					final String prefix = prefixNamespacePair.getName(); //get the prefix
					final String namespaceURI = prefixNamespacePair.getValue(); //get the namespace
					if(getDeclaredNamespaceURI(rootElement, prefix, true) == null) { //if the rooot element does not have this prefix defined, it's OK to add it to the parent element
						declareNamespace(rootElement, prefix, namespaceURI); //declare this namespace on the root element
					} else { //if the parent element has already defined this namespace
						declareNamespace(childElement, prefix, namespaceURI); //declare the namespace on the child element
					}
				}
				ensureChildNamespaceDeclarations(rootElement, childElement); //check the children of the child element
			}
		}
	}

	/**
	 * Gets the namespace declarations this element needs so that all namespaces for the element and its attributes are properly declared using the appropriate
	 * <code>xmlns=</code> or <code>xmlns:prefix=</code> attribute declaration. The children of this element are optionally checked.
	 * @param element The element for which namespace declarations should be checked.
	 * @param deep Whether all children and their descendants are also recursively checked for namespace declarations.
	 * @return An array of name/value pairs. The name of each is the the prefix to declare, or <code>null</code> if no prefix is used. The value of each is the
	 *         URI string of the namespace being defined, or <code>null</code> if no namespace is used.
	 */
	@SuppressWarnings("unchecked")
	//we create an array from a generic list, creating this warning to suppress
	public static NameValuePair<String, String>[] getUndeclaredNamespaces(final Element element) {
		final List<NameValuePair<String, String>> prefixNamespacePairList = new ArrayList<NameValuePair<String, String>>(); //create a new list in which to store name/value pairs of prefixes and namespaces
		if(!isNamespaceDeclared(element, element.getPrefix(), element.getNamespaceURI())) //if the element doesn't have the needed declarations
			prefixNamespacePairList.add(new NameValuePair<String, String>(element.getPrefix(), element.getNamespaceURI())); //add this prefix and namespace to the list of namespaces needing to be declared
		final NamedNodeMap attributeNamedNodeMap = element.getAttributes(); //get the map of attributes
		final int attributeCount = attributeNamedNodeMap.getLength(); //find out how many attributes there are
		for(int i = 0; i < attributeCount; ++i) { //look at each attribute
			final Attr attribute = (Attr)attributeNamedNodeMap.item(i); //get this attribute
			//as attribute namespaces are not inherited, don't check namespace
			//  declarations for attributes if they have neither prefix nor
			//  namespace declared
			if(attribute.getPrefix() != null || attribute.getNamespaceURI() != null) {
				if(!isNamespaceDeclared(element, attribute.getPrefix(), attribute.getNamespaceURI())) //if the attribute doesn't have the needed declarations
					prefixNamespacePairList.add(new NameValuePair<String, String>(attribute.getPrefix(), attribute.getNamespaceURI())); //add this prefix and namespace to the list of namespaces needing to be declared
			}
		}
		return prefixNamespacePairList.toArray(new NameValuePair[prefixNamespacePairList.size()]); //return an array of the prefixes and namespaces we gathered
	}

	/**
	 * Declares a prefix for the given namespace using the appropriate <code>xmlns=</code> or <code>xmlns:prefix=</code> attribute declaration.
	 * <p>
	 * It is assumed that the namespace is used at the level of the given element. If the namespace prefix is already declared somewhere up the tree, and that
	 * prefix is assigned to the same namespace, no action occurs.
	 * </p>
	 * @param element The element for which the namespace should be declared.
	 * @param prefix The prefix to declare, or <code>null</code> if no prefix is used.
	 * @param namespaceURI The namespace being defined, or <code>null</code> if no namespace is used.
	 */
	public static void ensureNamespaceDeclaration(final Element element, final String prefix, final String namespaceURI) {
		if(!isNamespaceDeclared(element, prefix, namespaceURI)) { //if this namespace isn't declared for this element
			declareNamespace(element, prefix, namespaceURI); //declare the namespace
		}
	}

	/**
	 * Determines if the given namespace is declared using the appropriate <code>xmlns=</code> or <code>xmlns:prefix=</code> attribute declaration either on the
	 * given element or on any element up the tree.
	 * <p>
	 * The "xmlns" and "xml" prefixes and namespaces always result in <code>true</code> being returned, because they never need to be declared.
	 * </p>
	 * @param element The element for which the namespace should be declared.
	 * @param prefix The prefix to declare, or <code>null</code> if no prefix is used.
	 * @param namespaceURI The namespace being defined, or <code>null</code> if no namespace is used.
	 * @return <code>true</code> if the namespace is sufficiently declared, either on the given element or somewhere up the element hierarchy.
	 */
	public static boolean isNamespaceDeclared(final Element element, final String prefix, final String namespaceURI) {
		if(XML.XMLNS_NAMESPACE_PREFIX.equals(prefix) && XML.XMLNS_NAMESPACE_URI.toString().equals(namespaceURI)) //we don't need to define the "xmlns:" prefix
			return true;
		if(prefix == null && XML.XMLNS_NAMESPACE_URI.toString().equals(namespaceURI)) //we don't need to define the "xmlns" name
			return true;
		if(XML.XML_NAMESPACE_PREFIX.equals(prefix) && XML.XML_NAMESPACE_URI.toString().equals(namespaceURI)) //we don't need to define the "xml" prefix
			return true;
		//find out what namespace is defined for the prefix anywhere up the hierarchy
		final String declaredNamespaceURI = getDeclaredNamespaceURI(element, prefix, true);
		if(declaredNamespaceURI != null) { //if some element declared a namespace for this prefix
			if(declaredNamespaceURI.length() == 0) { //if an empty namespace was declared
				if(namespaceURI == null) { //if we expected a null namespace
					return true; //show that the namespace is declared as expected
				}
			} else if(declaredNamespaceURI.equals(namespaceURI)) { //if a normal namespace was declared and it's the same one we expect
				return true; //show that the namespace is declared as expected
			}
		}
		return false; //show that we couldn't find this namespace declared using the given prefix
	}

	/**
	 * Declares prefixes for the given namespaces using the appropriate <code>xmlns=</code> or <code>xmlns:prefix=</code> attribute declaration for the given
	 * element.
	 * @param declarationElement The element on which the namespaces should be declared.
	 * @param previxNamespacePairs An array of name/value pairs. The name of each is the the prefix to declare, or <code>null</code> if no prefix is used. The
	 *          value of each is the URI string of the namespace being defined, or <code>null</code> if no namespace is used.
	 */
	public static void declareNamespaces(final Element declarationElement, final NameValuePair<String, String>[] prefixNamespacePairs) {
		for(int i = 0; i < prefixNamespacePairs.length; ++i) { //look at each name/value pair
			final NameValuePair<String, String> nameValuePair = prefixNamespacePairs[i]; //get this name/value pair representing a prefix and a namespace
			declareNamespace(declarationElement, nameValuePair.getName(), nameValuePair.getValue()); //declare this namespace
		}
	}

	/**
	 * Declares a prefix for the given namespace using the appropriate <code>xmlns=</code> or <code>xmlns:prefix=</code> attribute declaration for the given
	 * element.
	 * @param declarationElement The element on which the namespace should be declared.
	 * @param prefix The prefix to declare, or <code>null</code> if no prefix is used.
	 * @param namespaceURI The namespace being defined, or <code>null</code> if no namespace is used.
	 */
	public static void declareNamespace(final Element declarationElement, final String prefix, String namespaceURI) {
		if(XML.XMLNS_NAMESPACE_PREFIX.equals(prefix) && XML.XMLNS_NAMESPACE_URI.toString().equals(namespaceURI)) //we don't need to define the "xmlns" prefix
			return;
		if(prefix == null && XML.XMLNS_NAMESPACE_URI.toString().equals(namespaceURI)) //we don't need to define the "xmlns" name
			return;
		if(XML.XML_NAMESPACE_PREFIX.equals(prefix) && XML.XML_NAMESPACE_URI.toString().equals(namespaceURI)) //we don't need to define the "xml" prefix
			return;
		if(namespaceURI == null) //if no namespace URI was given
			namespaceURI = ""; //we'll declare an empty namespace URI
		if(prefix != null) { //if we were given a prefix
			//create an attribute in the form, xmlns:prefix="namespaceURI" TODO fix for attributes that may use the same prefix for different namespace URIs
			declarationElement.setAttributeNS(XML.XMLNS_NAMESPACE_URI.toString(), createQName(XML.XMLNS_NAMESPACE_PREFIX, prefix), namespaceURI);
		} else { //if we weren't given a prefix
			//create an attribute in the form, xmlns="namespaceURI" TODO fix for attributes that may use the same prefix for different namesapce URIs
			declarationElement.setAttributeNS(XML.XMLNS_NAMESPACE_URI.toString(), XML.XMLNS_NAMESPACE_PREFIX, namespaceURI);
		}
	}

	/**
	 * Parses the given text as an XML fragment using the given document builder as a parser.
	 * @param fragmentText The text of the XML fragment.
	 * @param documentBuilder The document builder to use to parse the fragment.
	 * @param defaultNamespaceURI The default namespace URI of the fragment, or <code>null</code> if there is no default namespace
	 * @return A document fragment containing the parsed contents of the given fragment text.
	 * @throws SAXException if there was an error parsing the fragment.
	 */
	public static DocumentFragment parseFragment(final String fragmentText, final DocumentBuilder documentBuilder, final String defaultNamespaceURI)
			throws SAXException {
		final StringBuilder stringBuilder = new StringBuilder("<?xml version='1.0' encoding='UTF-8'?>"); //TODO use constants if we can
		stringBuilder.append("<fragment");
		if(defaultNamespaceURI != null) { //if a default namespace was given
			stringBuilder.append(" xmlns='").append(defaultNamespaceURI).append("'"); //xmlns="defaultNamespaceURI"
		}
		stringBuilder.append(">").append(fragmentText).append("</fragment>");
		try {
			final Document document = documentBuilder.parse(new ByteArrayInputStream(stringBuilder.toString().getBytes(UTF_8_CHARSET))); //parse the bytes of the string
			return extractChildren(document.getDocumentElement()); //extract the children of the fragment document element and return them as a document fragment
		} catch(final IOException ioException) { //we should never get an I/O exception reading from a string
			throw new AssertionError(ioException);
		}
	}

}
