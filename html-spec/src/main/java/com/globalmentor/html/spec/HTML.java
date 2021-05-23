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

package com.globalmentor.html.spec;

import java.net.URI;
import java.util.*;
import java.util.stream.Stream;

import javax.annotation.Nonnegative;

import static java.util.Objects.*;
import static java.util.stream.Collectors.*;

import com.globalmentor.java.Characters;
import com.globalmentor.model.IDed;
import com.globalmentor.net.ContentType;
import com.globalmentor.text.ASCII;
import com.globalmentor.xml.spec.*;

import static com.globalmentor.java.Characters.*;
import static com.globalmentor.java.Conditions.*;

/**
 * Definitional constants of the HyperText Markup Language (HTML).
 * @author Garret Wilson
 * @see <a href="http://www.w3.org/html/">HTML, The Web’s Core Language</a>
 * @see <a href="http://www.w3.org/QA/2002/04/valid-dtd-list.html">W3C QA - Recommended list of DTDs</a>
 */
public class HTML {

	/** HTML MIME subtype. */
	public static final String HTML_SUBTYPE = "html";

	/** An XHTML application. */
	public static final String XHTML_XML_SUBTYPE = "xhtml" + ContentType.SUBTYPE_SUFFIX_DELIMITER_CHAR + XML.XML_SUBTYPE_SUFFIX;

	/** An XHTML fragment (not yet formally defined). */
	public static final String XHTML_XML_EXTERNAL_PARSED_ENTITY_SUBTYPE = "xhtml" + ContentType.SUBTYPE_SUFFIX_DELIMITER_CHAR
			+ XML.XML_EXTERNAL_PARSED_ENTITY_SUBTYPE_SUFFIX;

	/** The media type for HTML: <code>text/html</code>. */
	public static final ContentType HTML_MEDIA_TYPE = ContentType.of(ContentType.TEXT_PRIMARY_TYPE, HTML_SUBTYPE);

	/** The media type for XHTML: <code>application/xhtml+xml</code>. */
	public static final ContentType XHTML_MEDIA_TYPE = ContentType.of(ContentType.APPLICATION_PRIMARY_TYPE, XHTML_XML_SUBTYPE);

	/** The media type for an XHTML fragment: <code>application/xhtml+xml-external-parsed-entity</code>. */
	public static final ContentType XHTML_FRAGMENT_MEDIA_TYPE = ContentType.of(ContentType.APPLICATION_PRIMARY_TYPE, XHTML_XML_EXTERNAL_PARSED_ENTITY_SUBTYPE);

	/** The old extension for HTML resource names. */
	public static final String HTM_FILENAME_EXTENSION = "htm";

	/** The extension for HTML resource names. */
	public static final String HTML_FILENAME_EXTENSION = "html";

	/** The extension for XHTML resource names. */
	public static final String XHTML_FILENAME_EXTENSION = "xhtml";

	/** The recommended prefix to the XHTML namespace. */
	public static final String XHTML_NAMESPACE_PREFIX = "xhtml";

	/** The string representing the XHTML namespace. */
	public static final String XHTML_NAMESPACE_URI_STRING = "http://www.w3.org/1999/xhtml";

	/** The URI to the XHTML namespace. */
	public static final URI XHTML_NAMESPACE_URI = URI.create(XHTML_NAMESPACE_URI_STRING);

	/** The public ID for the HTML 2.0 DTD. */
	public static final String HTML_2_0_PUBLIC_ID = "-//IETF//DTD HTML 2.0//EN";

	/** The public ID for the HTML 3.2 DTD. */
	public static final String HTML_3_2_PUBLIC_ID = "-//W3C//DTD HTML 3.2 Final//EN";

	/** The public ID for the HTML 4.01 Strict DTD. */
	public static final String HTML_4_01_STRICT_PUBLIC_ID = "-//W3C//DTD HTML 4.01//EN";
	/** The system ID for the HTML 4.01 Strict DTD. */
	public static final String HTML_4_01_STRICT_SYSTEM_ID = "https://www.w3.org/TR/html4/strict.dtd";

	/** The public ID for the HTML 4.01 Traditional DTD. */
	public static final String HTML_4_01_TRANSITIONAL_PUBLIC_ID = "-//W3C//DTD HTML 4.01 Transitional//EN";
	/** The system ID for the HTML 4.01 Traditional DTD. */
	public static final String HTML_4_01_TRANSITIONAL_SYSTEM_ID = "https://www.w3.org/TR/html4/loose.dtd";

	/** The public ID for the HTML 4.01 Frameset DTD. */
	public static final String HTML_4_01_FRAMESET_PUBLIC_ID = "-//W3C//DTD HTML 4.01 Frameset//EN";
	/** The system ID for the HTML 4.01 Frameset DTD. */
	public static final String HTML_4_01_FRAMESET_SYSTEM_ID = "https://www.w3.org/TR/html4/frameset.dtd";

	/** The public ID for the XHTML 1.0 Strict DTD. */
	public static final String XHTML_1_0_STRICT_PUBLIC_ID = "-//W3C//DTD XHTML 1.0 Strict//EN";
	/** The system ID for the XHTML 1.0 Strict DTD. */
	public static final String XHTML_1_0_STRICT_SYSTEM_ID = "https://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd";

	/** The public ID for the XHTML 1.0 Traditional DTD. */
	public static final String XHTML_1_0_TRANSITIONAL_PUBLIC_ID = "-//W3C//DTD XHTML 1.0 Transitional//EN";
	/** The system ID for the XHTML 1.0 Traditional DTD. */
	public static final String XHTML_1_0_TRANSITIONAL_SYSTEM_ID = "https://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd";

	/** The public ID for the XHTML 1.0 Frameset DTD. */
	public static final String XHTML_1_0_FRAMESET_PUBLIC_ID = "-//W3C//DTD XHTML 1.0 Frameset//EN";
	/** The system ID for the XHTML 1.0 Frameset DTD. */
	public static final String XHTML_1_0_FRAMESET_SYSTEM_ID = "https://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd";

	/** The public ID for the XHTML 1.1 DTD. */
	public static final String XHTML_1_1_PUBLIC_ID = "-//W3C//DTD XHTML 1.1//EN";
	/** The system ID for the XHTML 1.1 DTD. */
	public static final String XHTML_1_1_SYSTEM_ID = "https://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd";

	/** The public ID for the XHTML+MathML+SVG DTD. */
	public static final String XHTML_1_1_MATHML_2_0_SVG_1_1_PUBLIC_ID = "-//W3C//DTD XHTML 1.1 plus MathML 2.0 plus SVG 1.1//EN";
	/** The system ID for the XHTML+MathML+SVG DTD. */
	public static final String XHTML_1_1_MATHML_2_0_SVG_1_1_SYSTEM_ID = "https://www.w3.org/2002/04/xhtml-math-svg/xhtml-math-svg.dtd";

	/**
	 * The public ID for the XHTML+MathML+SVG DTD.
	 * @deprecated to be removed in favor of {@link #XHTML_1_1_MATHML_2_0_SVG_1_1_PUBLIC_ID}.
	 */
	@Deprecated
	public static final String XHTML_MATHML_SVG_PUBLIC_ID = XHTML_1_1_MATHML_2_0_SVG_1_1_PUBLIC_ID;
	/**
	 * The system ID for the XHTML+MathML+SVG DTD.
	 * @deprecated to be removed in favor of {@link #XHTML_1_1_MATHML_2_0_SVG_1_1_SYSTEM_ID}.
	 */
	@Deprecated
	public static final String XHTML_MATHML_SVG_SYSTEM_ID = XHTML_1_1_MATHML_2_0_SVG_1_1_SYSTEM_ID;

	/**
	 * Determines if the given media type is one representing HTML in some form.
	 * <p>
	 * HTML media types include:
	 * </p>
	 * <ul>
	 * <li><code>text/html</code></li>
	 * <li><code>application/xhtml+xml</code></li>
	 * <li><code>application/xhtml+xml-external-parsed-entity</code> (not formally defined)</li>
	 * </ul>
	 * @param contentType The media type of a resource, or <code>null</code> for no media type.
	 * @return <code>true</code> if the given media type is one of several HTML media types.
	 */
	public static boolean isHTML(final ContentType contentType) { //TODO maybe move this to an HTMLUtilities
		if(contentType != null) { //if a media type is given
			final String primaryType = contentType.getPrimaryType(); //get the primary type
			final String subType = contentType.getSubType(); //get the sub type
			if(ASCII.equalsIgnoreCase(primaryType, ContentType.TEXT_PRIMARY_TYPE)) { //if this is "text/?"
				if(ASCII.equalsIgnoreCase(subType, HTML_SUBTYPE) //if this is "text/html"
				/*TODO fix for OEB || OEB.X_OEB1_DOCUMENT_SUBTYPE.equals(subType)*/) { //if this is "text/x-oeb1-document"
					return true; //show that this is HTML
				}
			}
			if(ContentType.APPLICATION_PRIMARY_TYPE.equals(primaryType)) { //if this is "application/?"
				//TODO probably add a parameter to specify whether we should allow fragments to qualify as XHTML---right now this behavior is not consistent with XMLUtilities.isXML(), which doesn't recognize fragments as XML
				if(ASCII.equalsIgnoreCase(subType, XHTML_XML_SUBTYPE) || ASCII.equalsIgnoreCase(subType, XHTML_XML_EXTERNAL_PARSED_ENTITY_SUBTYPE)) { //if this is "application/xhtml+xml" or "application/xhtml+xml-external-parsed-entity"
					return true; //show that this is HTML
				}
			}
		}
		return false; //this is not a media type we recognize as being HTML
	}

	/**
	 * Determines if the given URI represents one of the HTML XML namespaces.
	 * <p>
	 * HTML namespaces include:
	 * </p>
	 * <ul>
	 * <li><code>http://www.w3.org/1999/xhtml</code></li>
	 * </ul>
	 * @param namespaceURI A URI representing an XML namespace, or <code>null</code> for no namespace.
	 * @return <code>true</code> if the given URI represents one of the HTML XML namespaces.
	 * @deprecated It's better just to use the namespace URI directly; provide some pluggable means if OEB support is needed.
	 */
	@Deprecated
	public static boolean isHTMLNamespaceURI(final URI namespaceURI) {
		if(namespaceURI != null) { //if the namespace URI is valid
			//if it's part of the XHTML namespace
			if(XHTML_NAMESPACE_URI.equals(namespaceURI) /*TODO fix for OEB || OEB.OEB1_DOCUMENT_NAMESPACE_URI.equals(namespaceURI)*/)
				return true; //show that this is an HTML namespace
		}
		return false; //show that we didn't recognize the namespace as HTML
	}

	/**
	 * Characters considered <dfn>space characters</dfn> in HTML.
	 * @apiNote These are not to be confused with <dfn>whitespace</dfn> characters, which are those characters in the Unicode <code>White_Space</code> category.
	 * @see <a href="https://www.w3.org/TR/html52/infrastructure.html#space-characters">HTML 5.2, § 2.4.1. Common parser idioms: space characters</a>
	 */
	public static final Characters SPACE_CHARACTERS = Characters.of(SPACE_CHAR, CHARACTER_TABULATION_CHAR, LINE_FEED_CHAR, FORM_FEED_CHAR, CARRIAGE_RETURN_CHAR);

	//# elements

	public static final String ELEMENT_A = "a";
	public static final String ELEMENT_ABBR = "abbr";
	public static final String ELEMENT_ADDRESS = "address";
	public static final String ELEMENT_AREA = "area";
	public static final String ELEMENT_ARTICLE = "article";
	public static final String ELEMENT_ASIDE = "aside";
	public static final String ELEMENT_AUDIO = "audio";
	public static final String ELEMENT_B = "b";
	public static final String ELEMENT_BASE = "base";
	public static final String ELEMENT_BDI = "bdi";
	public static final String ELEMENT_BDO = "bdo";
	public static final String ELEMENT_BLOCKCODE = "blockcode";
	public static final String ELEMENT_BLOCKQUOTE = "blockquote";
	public static final String ELEMENT_BODY = "body";
	public static final String ELEMENT_BR = "br";
	public static final String ELEMENT_BUTTON = "button";
	public static final String ELEMENT_CANVAS = "canvas";
	public static final String ELEMENT_CAPTION = "caption";
	public static final String ELEMENT_CITE = "cite";
	public static final String ELEMENT_COL = "col";
	public static final String ELEMENT_CODE = "code";
	public static final String ELEMENT_DATA = "data";
	public static final String ELEMENT_DATE = "date";
	public static final String ELEMENT_DATALIST = "datalist";
	public static final String ELEMENT_DD = "dd";
	public static final String ELEMENT_DEL = "del";
	public static final String ELEMENT_DETAILS = "details";
	public static final String ELEMENT_DFN = "dfn";
	public static final String ELEMENT_DIALOG = "dialog";
	public static final String ELEMENT_DIV = "div";
	public static final String ELEMENT_DL = "dl";
	public static final String ELEMENT_DT = "dt";
	public static final String ELEMENT_EM = "em";
	public static final String ELEMENT_EMBED = "embed";
	public static final String ELEMENT_FIELDSET = "fieldset";
	public static final String ELEMENT_FIGCAPTION = "figcaption";
	public static final String ELEMENT_FIGURE = "figure";
	public static final String ELEMENT_FOOTER = "footer";
	public static final String ELEMENT_FORM = "form";
	public static final String ELEMENT_H1 = "h1";
	public static final String ELEMENT_H2 = "h2";
	public static final String ELEMENT_H3 = "h3";
	public static final String ELEMENT_H4 = "h4";
	public static final String ELEMENT_H5 = "h5";
	public static final String ELEMENT_H6 = "h6";
	public static final String ELEMENT_HEAD = "head";
	public static final String ELEMENT_HEADER = "header";
	public static final String ELEMENT_HR = "hr";
	public static final String ELEMENT_HTML = "html";
	public static final String ELEMENT_I = "i";
	public static final String ELEMENT_IFRAME = "iframe";
	public static final String ELEMENT_IMG = "img";
	public static final String ELEMENT_INPUT = "input";
	public static final String ELEMENT_INS = "ins";
	public static final String ELEMENT_KBD = "kbd";
	public static final String ELEMENT_LI = "li";
	public static final String ELEMENT_LABEL = "label";
	public static final String ELEMENT_LEGEND = "legend";
	public static final String ELEMENT_LINK = "link";
	public static final String ELEMENT_MAIN = "main";
	public static final String ELEMENT_MAP = "map";
	public static final String ELEMENT_MARK = "mark";
	public static final String ELEMENT_META = "meta";
	public static final String ELEMENT_METER = "meter";
	public static final String ELEMENT_NAV = "nav";
	public static final String ELEMENT_NOSCRIPT = "noscript";
	public static final String ELEMENT_OBJECT = "object";
	public static final String ELEMENT_OL = "ol";
	public static final String ELEMENT_OPTION = "option";
	public static final String ELEMENT_OUTPUT = "output";
	public static final String ELEMENT_P = "p";
	public static final String ELEMENT_PARAM = "param";
	public static final String ELEMENT_PICTURE = "picture";
	public static final String ELEMENT_PRE = "pre";
	public static final String ELEMENT_PROGRESS = "progress";
	public static final String ELEMENT_Q = "q";
	public static final String ELEMENT_RUBY = "ruby";
	public static final String ELEMENT_RP = "rp";
	public static final String ELEMENT_RT = "rt";
	public static final String ELEMENT_S = "s";
	public static final String ELEMENT_SAMP = "samp";
	public static final String ELEMENT_SCRIPT = "script";
	public static final String ELEMENT_SECTION = "section";
	public static final String ELEMENT_SELECT = "select";
	public static final String ELEMENT_SMALL = "small";
	public static final String ELEMENT_SOURCE = "source";
	public static final String ELEMENT_SPAN = "span";
	public static final String ELEMENT_STRONG = "strong";
	public static final String ELEMENT_STYLE = "style";
	public static final String ELEMENT_SUB = "sub";
	public static final String ELEMENT_SUMMARY = "summary";
	public static final String ELEMENT_SUP = "sup";
	public static final String ELEMENT_TABLE = "table";
	public static final String ELEMENT_TIME = "time";
	public static final String ELEMENT_TD = "td";
	public static final String ELEMENT_TEMPLATE = "template";
	public static final String ELEMENT_TEXTAREA = "textarea";
	public static final String ELEMENT_TH = "th";
	public static final String ELEMENT_TBODY = "tbody";
	public static final String ELEMENT_THEAD = "thead";
	public static final String ELEMENT_TFOOT = "tfoot";
	public static final String ELEMENT_TITLE = "title";
	public static final String ELEMENT_TR = "tr";
	public static final String ELEMENT_TRACK = "track";
	public static final String ELEMENT_U = "u";
	public static final String ELEMENT_UL = "ul";
	public static final String ELEMENT_VAR = "var";
	public static final String ELEMENT_VIDEO = "video";
	public static final String ELEMENT_WBR = "wbr";

	//obsolete, non-conforming elements; see https://www.w3.org/TR/html52/obsolete.html#non-conforming-features
	public static final String ELEMENT_APPLET = "applet";
	public static final String ELEMENT_ACRONYM = "acronym";
	public static final String ELEMENT_BASEFONT = "basefont";
	public static final String ELEMENT_BGSOUND = "bgsound";
	public static final String ELEMENT_BIG = "big";
	public static final String ELEMENT_BLINK = "blink";
	public static final String ELEMENT_CENTER = "center";
	public static final String ELEMENT_DIR = "dir";
	public static final String ELEMENT_FONT = "font";
	public static final String ELEMENT_FRAME = "frame";
	public static final String ELEMENT_FRAMESET = "frameset";
	public static final String ELEMENT_NOFRAMES = "noframes";
	public static final String ELEMENT_ISINDEX = "isindex";
	public static final String ELEMENT_LISTING = "listing";
	public static final String ELEMENT_MARQUEE = "marquee";
	public static final String ELEMENT_MENU = "menu";
	public static final String ELEMENT_MENUITEM = "menuitem";
	public static final String ELEMENT_MULTICOL = "multicol";
	public static final String ELEMENT_NEXTID = "nextid";
	public static final String ELEMENT_NOEMBED = "noembed";
	public static final String ELEMENT_NOBR = "nobr";
	public static final String ELEMENT_PLAINTEXT = "plaintext";
	public static final String ELEMENT_RB = "rb";
	public static final String ELEMENT_RTC = "rtc";
	public static final String ELEMENT_SPACER = "spacer";
	public static final String ELEMENT_STRIKE = "strike";
	public static final String ELEMENT_TT = "tt";
	public static final String ELEMENT_XMP = "xmp";

	//removed elements
	public static final String ELEMENT_HGROUP = "hgroup"; //in WHATWG but not W3C version; see https://html.spec.whatwg.org/#the-hgroup-element

	/**
	 * The one-based maximum level of the heading levels: {@value #MAX_H_LEVEL}.
	 * @see #ELEMENT_H6
	 */
	public static final int MAX_H_LEVEL = 6;

	/**
	 * Returns the heading element for the given level.
	 * @param level The one-based heading level, with one corresponding to {@link #ELEMENT_H1}.
	 * @return The XHTML name of the requested heading element.
	 * @throws IllegalArgumentException if the given level is less than one or greater than {@value #MAX_H_LEVEL}.
	 * @see #ELEMENT_H1
	 * @see #ELEMENT_H2
	 * @see #ELEMENT_H3
	 * @see #ELEMENT_H4
	 * @see #ELEMENT_H5
	 * @see #ELEMENT_H6
	 * @see #MAX_H_LEVEL
	 */
	public static NsName ELEMENT_H(@Nonnegative final int level) {
		final String localName = "h" + checkArgumentRange(level, 1, MAX_H_LEVEL);
		return NsName.of(XHTML_NAMESPACE_URI_STRING, localName);
	}

	/**
	 * The delimiter character for separating parts of an attribute string (e.g. for HTML5 data- attributes). This is distinct from the namespace prefix
	 * delimiter.
	 */
	public static final char ATTRIBUTE_DELIMITER_CHAR = '-';

	//# attributes

	//## global attributes; see [HTML 5.2 § 3.2.5. Global attributes](https://www.w3.org/TR/html52/dom.html#global-attributes)
	public static final String ATTRIBUTE_ACCESSKEY = "accesskey";
	public static final String ATTRIBUTE_CLASS = "class";
	public static final String ATTRIBUTE_CONTENTEDITABLE = "contenteditable";
	public static final String ATTRIBUTE_DIR = "dir";
	public static final String ATTRIBUTE_DRAGGABLE = "draggable";
	public static final String ATTRIBUTE_HIDDEN = "hidden";
	public static final String ATTRIBUTE_ID = "id";
	public static final String ATTRIBUTE_LANG = "lang";
	public static final String ATTRIBUTE_SPELLCHECK = "spellcheck";
	public static final String ATTRIBUTE_STYLE = "style";
	public static final String ATTRIBUTE_TABINDEX = "tabindex";
	public static final String ATTRIBUTE_TITLE = "title";
	public static final String ATTRIBUTE_TRANSLATE = "translate";

	//## common attributes

	/** The attribute for left-to-right direction. */
	public static final String DIR_LTR = "ltr";
	/** The attribute for right-to-left direction. */
	public static final String DIR_RTL = "rtl";

	/** The attribute for name. */
	public static final String ATTRIBUTE_NAME = "name";
	/** The attribute for value. */
	public static final String ATTRIBUTE_VALUE = "value";

	/** The common attribute for the source of content. */
	public static final String ATTRIBUTE_SRC = "src";
	/** The common attribute for a reference to a resource. */
	public static final String ATTRIBUTE_HREF = "href";

	//## event attributes

	public static final String ATTRIBUTE_ONCLICK = "onclick";
	public static final String ATTRIBUTE_ONLOAD = "onload";

	//## XHTML 1.1 attributes

	/**
	 * The doctype version attribute described in XHTML 1.1 and added as implied in the XHTML 1.1 DTDs.
	 * @see <a href="https://www.w3.org/TR/xhtml11/xhtml11.html#strict">XHTML 1.1 - Second Edition § 2.1.1. Strictly Conforming Documents</a>
	 */
	public static final String ATTRIBUTE_VERSION = "version";

	//## element-specific attributes

	/**
	 * The identifier, with no delimiter, indicating that an attribute is an HTML5 data attribute
	 * @see <a href="http://www.w3.org/TR/html5/elements.html#embedding-custom-non-visible-data-with-the-data-attributes">HTML 5 Data Attributes</a>
	 */
	public static final String DATA_ATTRIBUTE_ID = "data";

	public static final String LINK_ATTRIBUTE_REL = "rel"; //the link type attribute for <a>, <area>, and <link>; see http://www.w3.org/TR/html5/links.html#linkTypes
	public static final String LINK_ATTRIBUTE_TYPE = "type"; //the link MIME type attribute for <a>, <area>, and <link>; see http://www.w3.org/TR/html5/links.html#attr-hyperlink-type
	//link types for <a>, <area>, and <link>; see http://www.w3.org/TR/html5/links.html#linkTypes 
	public static final String LINK_REL_ALTERNATE = "alternate"; //<link>, <a>/<area>
	public static final String LINK_REL_AUTHOR = "author"; //<link>, <a>/<area>
	public static final String LINK_REL_BOOKMARK = "bookmark"; //<a>/<area>
	public static final String LINK_REL_EXTERNAL = "external"; //<a>/<area>
	public static final String LINK_REL_HELP = "help"; //<link>, <a>/<area>
	public static final String LINK_REL_ICON = "icon"; //<link>
	public static final String LINK_REL_LICENSE = "license"; //<link>, <a>/<area>
	public static final String LINK_REL_NEXT = "next"; //<link>, <a>/<area>
	public static final String LINK_REL_NOFOLLOW = "nofollow"; //<a>/<area>
	public static final String LINK_REL_NOREFERRER = "noreferrer"; //<a>/<area>
	public static final String LINK_REL_PINGBACK = "pingback"; //<link>
	public static final String LINK_REL_PREFETCH = "prefetch"; //<link>, <a>/<area>
	public static final String LINK_REL_PREV = "prev"; //<link>, <a>/<area>
	public static final String LINK_REL_SEARCH = "search"; //<link>, <a>/<area>
	public static final String LINK_REL_SIDEBAR = "sidebar"; //<link>, <a>/<area>
	public static final String LINK_REL_STYLESHEET = "stylesheet"; //<link>
	public static final String LINK_REL_TAG = "tag"; //<link>, <a>/<area>

	//attributes for <a>
	public static final String ELEMENT_A_ATTRIBUTE_HREF = ATTRIBUTE_HREF;
	public static final String ELEMENT_A_ATTRIBUTE_HREFLANG = ATTRIBUTE_LANG;
	public static final String ELEMENT_A_ATTRIBUTE_TARGET = "target";
	public static final String ELEMENT_A_ATTRIBUTE_REL = LINK_ATTRIBUTE_REL;

	/**
	 * Link types for {@code<a>}, {@code<area>}, and {@code<link>}.
	 * @author Garret Wilson
	 * @see <a href="http://www.w3.org/TR/html5/links.html#linkTypes">HTML5 Link Types</a>
	 */
	public enum LinkType implements IDed<String> {
		ALTERNATE(LINK_REL_ALTERNATE), //<link>, <a>/<area>
		AUTHOR(LINK_REL_AUTHOR), //<link>, <a>/<area>
		BOOKMARK(LINK_REL_BOOKMARK), //<a>/<area>
		EXTERNAL(LINK_REL_EXTERNAL), //<a>/<area>
		HELP(LINK_REL_HELP), //<link>, <a>/<area>
		ICON(LINK_REL_ICON), //<link>
		LICENSE(LINK_REL_LICENSE), //<link>, <a>/<area>
		NEXT(LINK_REL_NEXT), //<link>, <a>/<area>
		NOFOLLOW(LINK_REL_NOFOLLOW), //<a>/<area>
		NOREFERRER(LINK_REL_NOREFERRER), //<a>/<area>
		PINGBACK(LINK_REL_PINGBACK), //<link>
		PREFETCH(LINK_REL_PREFETCH), //<link>, <a>/<area>
		PREV(LINK_REL_PREV), //<link>, <a>/<area>
		SEARCH(LINK_REL_SEARCH), //<link>, <a>/<area>
		SIDEBAR(LINK_REL_SIDEBAR), //<link>, <a>/<area>
		STYLESHEET(LINK_REL_STYLESHEET), //<link>
		TAG(LINK_REL_TAG); //<link>, <a>/<area>

		private final String id;

		private LinkType(final String id) {
			this.id = requireNonNull(id);
		}

		@Override
		public String getID() {
			return id;
		}
	}

	//attributes for <applet>
	public static final String ELEMENT_APPLET_ATTRIBUTE_CODE = "code";
	public static final String ELEMENT_APPLET_ATTRIBUTE_HEIGHT = "height";
	public static final String ELEMENT_APPLET_ATTRIBUTE_WIDTH = "width";

	//attributes for <area>
	public static final String ELEMENT_AREA_ATTRIBUTE_HREF = ATTRIBUTE_HREF;

	//attributes for <audio>
	public static final String ELEMENT_AUDIO_ATTRIBUTE_SRC = ATTRIBUTE_SRC;

	//attributes for <button>
	public static final String ELEMENT_BUTTON_ATTRIBUTE_TYPE = "type";
	public static final String BUTTON_TYPE_BUTTON = "button";
	public static final String BUTTON_TYPE_RESET = "reset";
	public static final String BUTTON_TYPE_SUBMIT = "submit";

	//attributes for <form>
	public static final String ELEMENT_FORM_ATTRIBUTE_ACTION = "action";
	public static final String ELEMENT_FORM_ATTRIBUTE_ENCTYPE = "enctype";
	/**
	 * The <code>application/x-www-form-urlencoded</code> encoding media type.
	 * @see <a href="https://tools.ietf.org/html/rfc1867">RFC 1867: Form-based File Upload in HTML</a>.
	 */
	public static final ContentType APPLICATION_X_WWW_FORM_URLENCODED_MEDIA_TYPE = ContentType.of(ContentType.APPLICATION_PRIMARY_TYPE, "www-form-urlencoded");
	/**
	 * The <code>multipart/form-data</code> encoding media type.
	 * @see <a href="https://tools.ietf.org/html/rfc1867">RFC 1867: Form-based File Upload in HTML</a>.
	 */
	public static final ContentType MULTIPART_FORM_DATA_MEDIA_TYPE = ContentType.of(ContentType.MULTIPART_PRIMARY_TYPE, "form-data");

	public static final String ELEMENT_FORM_ATTRIBUTE_METHOD = "method";
	public static final String FORM_METHOD_GET = "get";
	public static final String FORM_METHOD_POST = "post";

	//attributes for <embed>
	public static final String ELEMENT_EMBED_ATTRIBUTE_SRC = ATTRIBUTE_SRC;
	public static final String ELEMENT_EMBED_ATTRIBUTE_HEIGHT = "height";
	public static final String ELEMENT_EMBED_ATTRIBUTE_WIDTH = "width";
	public static final String ELEMENT_EMBED_ATTRIBUTE_TYPE = "type";

	//attributes for <frame>
	public static final String ELEMENT_FRAME_ATTRIBUTE_SRC = ATTRIBUTE_SRC;
	public static final String ELEMENT_FRAME_ATTRIBUTE_FRAMEBORDER = "frameborder";
	public static final String ELEMENT_FRAME_ATTRIBUTE_LONGDESC = "longdesc";
	public static final String ELEMENT_FRAME_ATTRIBUTE_MARGINHEIGHT = "marginheight";
	public static final String ELEMENT_FRAME_ATTRIBUTE_MARGINWIDTH = "marginwidth";
	public static final String ELEMENT_FRAME_ATTRIBUTE_NORESIZE = "noresize";
	public static final String ELEMENT_FRAME_ATTRIBUTE_SCROLLING = "scrolling";

	//attributes for <frameset>
	public static final String ELEMENT_FRAMESET_ATTRIBUTE_COLS = "cols";
	public static final String ELEMENT_FRAMESET_ATTRIBUTE_ROWS = "rows";

	//attributes for <iframe>
	public static final String ELEMENT_IFRAME_ATTRIBUTE_ALLOW_TRANSPARENCY = "allowTransparency"; //IE-specific; see http://msdn.microsoft.com/en-us/library/ms533072.aspx
	public static final String ELEMENT_IFRAME_ATTRIBUTE_SRC = ATTRIBUTE_SRC;
	public static final String ELEMENT_IFRAME_ATTRIBUTE_FRAMEBORDER = "frameborder";
	public static final String ELEMENT_IFRAME_ATTRIBUTE_SCROLLING = "scrolling";
	public static final String IFRAME_SCROLLING_NO = "no";
	public static final String IFRAME_SCROLLING_YES = "yes";
	public static final String IFRAME_SCROLLING_AUTO = "auto";

	//attributes for <img>
	public static final String ELEMENT_IMG_ATTRIBUTE_ALT = "alt";
	public static final String ELEMENT_IMG_ATTRIBUTE_HEIGHT = "height";
	public static final String ELEMENT_IMG_ATTRIBUTE_WIDTH = "width";
	public static final String ELEMENT_IMG_ATTRIBUTE_SRC = ATTRIBUTE_SRC;

	//attributes for <input>
	public static final String ELEMENT_INPUT_ATTRIBUTE_ACCEPT = "accept";
	public static final String ELEMENT_INPUT_ATTRIBUTE_TYPE = "type";
	public static final String INPUT_TYPE_BUTTON = "button";
	public static final String INPUT_TYPE_CHECKBOX = "checkbox";
	public static final String INPUT_TYPE_FILE = "file";
	public static final String INPUT_TYPE_HIDDEN = "hidden";
	public static final String INPUT_TYPE_IMAGE = "image";
	public static final String INPUT_TYPE_PASSWORD = "password";
	public static final String INPUT_TYPE_RADIO = "radio";
	public static final String INPUT_TYPE_RESET = "reset";
	public static final String INPUT_TYPE_SUBMIT = "submit";
	public static final String INPUT_TYPE_TEXT = "text";
	public static final String ELEMENT_INPUT_ATTRIBUTE_CHECKED = "checked";
	public static final String INPUT_CHECKED_CHECKED = "checked";
	public static final String ELEMENT_INPUT_ATTRIBUTE_DISABLED = "disabled";
	public static final String INPUT_DISABLED_DISABLED = "disabled";
	public static final String ELEMENT_INPUT_ATTRIBUTE_MAXLENGTH = "maxlength";
	public static final String ELEMENT_INPUT_ATTRIBUTE_READONLY = "readonly";
	public static final String ELEMENT_INPUT_ATTRIBUTE_SIZE = "size";
	public static final String INPUT_READONLY_READONLY = "readonly";

	//attributes for <label>
	public static final String ELEMENT_LABEL_ATTRIBUTE_FOR = "for";

	//attributes for <link>
	public static final String ELEMENT_LINK_ATTRIBUTE_HREF = ATTRIBUTE_HREF;
	public static final String ELEMENT_LINK_ATTRIBUTE_REL = LINK_ATTRIBUTE_REL;
	public static final String ELEMENT_LINK_ATTRIBUTE_TYPE = "type";
	public static final String ELEMENT_LINK_ATTRIBUTE_MEDIA = "media";
	public static final String LINK_MEDIA_SCREEN = "screen";

	//attributes for <meta>
	public static final String ELEMENT_META_ATTRIBUTE_CONTENT = "content";
	public static final String ELEMENT_META_ATTRIBUTE_NAME = "name";
	public static final String ELEMENT_META_ATTRIBUTE_PROPERTY = "property"; //TODO Facebook OpenGraph specific; document and move; see https://developers.facebook.com/docs/opengraph/

	//standard metadata names as per https://www.w3.org/TR/html52/document-metadata.html#standard-metadata-names
	public static final String META_NAME_APPLICATION_NAME = "application-name";
	public static final String META_NAME_AUTHOR = "author";
	public static final String META_NAME_DESCRIPTION = "description";
	public static final String META_NAME_GENERATOR = "generator";
	public static final String META_NAME_KEYWORDS = "keywords";
	public static final String META_NAME_REFERRER = "referrer";

	//attributes for <object>
	public static final String ELEMENT_OBJECT_ATTRIBUTE_CLASSID = "classid";
	public static final String ELEMENT_OBJECT_ATTRIBUTE_CODEBASE = "codebase";
	public static final String ELEMENT_OBJECT_ATTRIBUTE_CODETYPE = "codetype";
	public static final String ELEMENT_OBJECT_ATTRIBUTE_DATA = "data";
	public static final String ELEMENT_OBJECT_ATTRIBUTE_HEIGHT = "height";
	public static final String ELEMENT_OBJECT_ATTRIBUTE_WIDTH = "width";
	public static final String ELEMENT_OBJECT_ATTRIBUTE_TYPE = "type";

	//attributes for <option>
	public static final String ELEMENT_OPTION_ATTRIBUTE_DISABLED = "disabled";
	public static final String OPTION_DISABLED_DISABLED = "disabled";
	public static final String ELEMENT_OPTION_ATTRIBUTE_SELECTED = "selected";
	public static final String OPTION_SELECTED_SELECTED = "selected";

	//attributes for <param>
	public static final String ELEMENT_PARAM_ATTRIBUTE_NAME = "name";
	public static final String ELEMENT_PARAM_ATTRIBUTE_VALUE = "value";

	//attributes for <script>
	public static final String ELEMENT_SCRIPT_ATTRIBUTE_SRC = ATTRIBUTE_SRC;
	public static final String ELEMENT_SCRIPT_ATTRIBUTE_TYPE = "type";
	public static final String ELEMENT_SCRIPT_ATTRIBUTE_LANGUAGE = "language";

	//attributes for <select>
	public static final String ELEMENT_SELECT_ATTRIBUTE_MULTIPLE = "multiple";
	public static final String SELECT_MULTIPLE_MULTIPLE = "multiple";
	public static final String ELEMENT_SELECT_ATTRIBUTE_SIZE = "size";

	//attributes for <source>
	public static final String ELEMENT_SOURCE_ATTRIBUTE_SRC = ATTRIBUTE_SRC;

	//attributes for <td>
	public static final String ELEMENT_TD_ATTRIBUTE_ALIGN = "align";
	public static final String TD_ALIGN_LEFT = "left";
	public static final String TD_ALIGN_CENTER = "center";
	public static final String TD_ALIGN_RIGHT = "right";
	public static final String ELEMENT_TD_ATTRIBUTE_VALIGN = "valign";
	public static final String TD_VALIGN_TOP = "top";
	public static final String TD_VALIGN_MIDDLE = "middle";
	public static final String TD_VALIGN_BOTTOM = "bottom";
	public static final String ELEMENT_TD_ATTRIBUTE_COLSPAN = "colspan";
	public static final String ELEMENT_TD_ATTRIBUTE_ROWSPAN = "rowspan";
	public static final String ELEMENT_TD_ATTRIBUTE_SCOPE = "scope";
	public static final String TD_SCOPE_COLGROUP = "colgroup";

	//attributes for <textarea>
	public static final String ELEMENT_TEXTAREA_ATTRIBUTE_COLS = "cols";
	public static final String ELEMENT_TEXTAREA_ATTRIBUTE_ROWS = "rows";
	public static final String ELEMENT_TEXTAREA_ATTRIBUTE_WRAP = "wrap";
	public static final String TEXTAREA_WRAP_OFF = "off";
	public static final String TEXTAREA_WRAP_HARD = "hard";
	public static final String TEXTAREA_WRAP_SOFT = "soft";
	/*"hard" and "soft" apparently have more acceptance than "physical" and "virtual"; see http://msdn2.microsoft.com/en-us/library/ms535152.aspx and http://lists.evolt.org/archive/Week-of-Mon-19991101/091388.html
			public static final String TEXTAREA_WRAP_PHYSICAL="physical";
			public static final String TEXTAREA_WRAP_VIRTUAL="virtual";
	*/
	public static final String ELEMENT_TEXTAREA_ATTRIBUTE_DISABLED = "disabled";
	public static final String TEXTAREA_DISABLED_DISABLED = "disabled";
	public static final String ELEMENT_TEXTAREA_ATTRIBUTE_READONLY = "readonly";
	public static final String TEXTAREA_READONLY_READONLY = "readonly";

	//attributes for <tr>
	public static final String ELEMENT_TR_ATTRIBUTE_ALIGN = "align";
	public static final String TR_ALIGN_LEFT = "left";
	public static final String TR_ALIGN_CENTER = "center";
	public static final String TR_ALIGN_RIGHT = "right";
	public static final String ELEMENT_TR_ATTRIBUTE_VALIGN = "valign";
	public static final String TR_VALIGN_TOP = "top";
	public static final String TR_VALIGN_MIDDLE = "middle";
	public static final String TR_VALIGN_BOTTOM = "bottom";

	//attributes for <track>
	public static final String ELEMENT_TRACK_ATTRIBUTE_SRC = ATTRIBUTE_SRC;

	//attributes for <video>
	public static final String ELEMENT_VIDEO_ATTRIBUTE_SRC = ATTRIBUTE_SRC;

	/**
	 * HTML5 obsolete, non-conforming elements.
	 * @see <a href="https://www.w3.org/TR/html52/obsolete.html#non-conforming-features">HTML 5.2 § 11.2. Non-conforming features</a>
	 */
	public static final Set<NsName> OBSOLETE_ELEMENTS = Stream.of(ELEMENT_APPLET, ELEMENT_ACRONYM, ELEMENT_BASEFONT, ELEMENT_BGSOUND, ELEMENT_BIG, ELEMENT_BLINK,
			ELEMENT_CENTER, ELEMENT_DIR, ELEMENT_FONT, ELEMENT_FRAME, ELEMENT_FRAMESET, ELEMENT_NOFRAMES, ELEMENT_ISINDEX, ELEMENT_LISTING, ELEMENT_MARQUEE,
			ELEMENT_MENU, ELEMENT_MENUITEM, ELEMENT_MULTICOL, ELEMENT_NEXTID, ELEMENT_NOEMBED, ELEMENT_NOBR, ELEMENT_PLAINTEXT, ELEMENT_RB, ELEMENT_RTC,
			ELEMENT_SPACER, ELEMENT_STRIKE, ELEMENT_TT, ELEMENT_XMP).map(elementName -> NsName.of(XHTML_NAMESPACE_URI_STRING, elementName))
			.collect(toUnmodifiableSet());

	//kinds of elements; see https://www.w3.org/TR/html52/syntax.html#writing-html-documents-elements

	/**
	 * The HTML5 <dfn>void elements</dfn>.
	 * @apiNote This definition only includes HTML5 elements designated as "void elements". It does not include obsolete elements from previous specifications
	 *          that also must be empty. For a complete set of empty elements, including void elements and obsolete empty elements, use {@link #EMPTY_ELEMENTS}.
	 * @see <a href="https://www.w3.org/TR/html52/syntax.html#void-elements">HTML 5.2 § 8.1.2. Elements: Void elements</a>
	 */
	public static final Set<NsName> VOID_ELEMENTS = Stream.of(ELEMENT_AREA, ELEMENT_BASE, ELEMENT_BR, ELEMENT_COL, ELEMENT_EMBED, ELEMENT_HR, ELEMENT_IMG,
			ELEMENT_INPUT, ELEMENT_LINK, ELEMENT_META, ELEMENT_PARAM, ELEMENT_SOURCE, ELEMENT_TRACK, ELEMENT_WBR)
			.map(elementName -> NsName.of(XHTML_NAMESPACE_URI_STRING, elementName)).collect(toUnmodifiableSet());

	/**
	 * HTML elements which must be empty. These include the HTML5 <dfn>void elements</dfn> defined in {@link #VOID_ELEMENTS}, as well as other obsolete elements
	 * that were specified as empty.
	 * @see <a href="https://www.w3.org/TR/html52/syntax.html#void-elements">HTML 5.2 § 8.1.2. Elements: Void elements</a>
	 * @see <a href="https://www.w3.org/TR/html4/index/elements.html">HTML 4.01 Index of Elements</a>
	 */
	public static final Set<NsName> EMPTY_ELEMENTS = Stream
			.concat(VOID_ELEMENTS.stream(),
					Stream.of(ELEMENT_BASEFONT, ELEMENT_FRAME, ELEMENT_ISINDEX).map(elementName -> NsName.of(XHTML_NAMESPACE_URI_STRING, elementName)))
			.collect(toUnmodifiableSet());

	//kinds of content; see https://www.w3.org/TR/html52/dom.html#content-models

	/**
	 * Elements HTML5 considers <dfn>metadata content</dfn>.
	 * @see <a href="https://www.w3.org/TR/html52/dom.html#metadata-content">HTML 5.2 § 3.2.4.2.1. Metadata content</a>
	 */
	public static final Set<NsName> METADATA_CONTENT = Stream
			.of(ELEMENT_BASE, ELEMENT_LINK, ELEMENT_META, ELEMENT_NOSCRIPT, ELEMENT_SCRIPT, ELEMENT_STYLE, ELEMENT_TEMPLATE, ELEMENT_TITLE)
			.map(elementName -> NsName.of(XHTML_NAMESPACE_URI_STRING, elementName)).collect(toUnmodifiableSet());

	/**
	 * Elements HTML5 considers <dfn>flow content</dfn>.
	 * @apiNote This includes the <code>&lt;math&gt;</code> and <code>&lt;svg&gt;</code> elements, even though they they are technically in separate namespaces,
	 *          because they are classified as flow content in HTML5.
	 * @see <a href="https://www.w3.org/TR/html52/dom.html#flow-content">HTML 5.2 § 3.2.4.2.2. Flow content</a>
	 * @see <a href="https://www.w3.org/TR/MathML/chapter2.html#interf.namespace">Mathematical Markup Language (MathML) Version 3.0 2nd Edition § 2.1.2 MathML and
	 *      Namespaces</a>
	 * @see <a href="https://www.w3.org/TR/SVG2/struct.html#Namespace">Scalable Vector Graphics (SVG) 2 § 5.1.2. Namespace</a>
	 */
	public static final Set<NsName> FLOW_CONTENT = Stream.concat(
			//HTML elements
			Stream.of(ELEMENT_A, ELEMENT_ABBR, ELEMENT_ADDRESS, ELEMENT_AREA, //if it is a descendant of a `<map>` element
					ELEMENT_ARTICLE, ELEMENT_ASIDE, ELEMENT_AUDIO, ELEMENT_B, ELEMENT_BDI, ELEMENT_BDO, ELEMENT_BLOCKQUOTE, ELEMENT_BR, ELEMENT_BUTTON, ELEMENT_CANVAS,
					ELEMENT_CITE, ELEMENT_CODE, ELEMENT_DATA, ELEMENT_DATALIST, ELEMENT_DEL, ELEMENT_DETAILS, ELEMENT_DFN, ELEMENT_DIALOG, ELEMENT_DIV, ELEMENT_DL,
					ELEMENT_EM, ELEMENT_EMBED, ELEMENT_FIELDSET, ELEMENT_FIGURE, ELEMENT_FOOTER, ELEMENT_FORM, ELEMENT_H1, ELEMENT_H2, ELEMENT_H3, ELEMENT_H4, ELEMENT_H5,
					ELEMENT_H6, ELEMENT_HEADER, ELEMENT_HR, ELEMENT_I, ELEMENT_IFRAME, ELEMENT_IMG, ELEMENT_INPUT, ELEMENT_INS, ELEMENT_KBD, ELEMENT_LABEL, ELEMENT_LINK, //if it is allowed in the body
					ELEMENT_MAIN, ELEMENT_MAP, ELEMENT_MARK, //MathML: `<math>`
					ELEMENT_METER, ELEMENT_NAV, ELEMENT_NOSCRIPT, ELEMENT_OBJECT, ELEMENT_OL, ELEMENT_OUTPUT, ELEMENT_P, ELEMENT_PICTURE, ELEMENT_PRE, ELEMENT_PROGRESS,
					ELEMENT_Q, ELEMENT_RUBY, ELEMENT_S, ELEMENT_SAMP, ELEMENT_SCRIPT, ELEMENT_SECTION, ELEMENT_SELECT, ELEMENT_SMALL, ELEMENT_SPAN, ELEMENT_STRONG,
					ELEMENT_STYLE, ELEMENT_SUB, ELEMENT_SUP, //SVG: `<svg>`
					ELEMENT_TABLE, ELEMENT_TEMPLATE, ELEMENT_TEXTAREA, ELEMENT_TIME, ELEMENT_U, ELEMENT_UL, ELEMENT_VAR, ELEMENT_VIDEO, ELEMENT_WBR)
					.map(elementName -> NsName.of(XHTML_NAMESPACE_URI_STRING, elementName)),
			//non-HTML elements
			Stream.of(
					//MathML
					NsName.of("http://www.w3.org/1998/Math/MathML", "math"),
					//SVG
					NsName.of("http://www.w3.org/2000/svg", "svg")))
			.collect(toUnmodifiableSet());

	/**
	 * Elements HTML5 considers <dfn>sectioning content</dfn>.
	 * @see <a href="https://www.w3.org/TR/html52/dom.html#sectioning-content">HTML 5.2 § 3.2.4.2.3. Sectioning content</a>
	 */
	public static final Set<NsName> SECTIONING_CONTENT = Stream.of(ELEMENT_ARTICLE, ELEMENT_ASIDE, ELEMENT_NAV, ELEMENT_SECTION)
			.map(elementName -> NsName.of(XHTML_NAMESPACE_URI_STRING, elementName)).collect(toUnmodifiableSet());

	/**
	 * Elements HTML5 considers <dfn>heading content</dfn>.
	 * @see <a href="https://www.w3.org/TR/html52/dom.html#heading-content">HTML 5.2 § 3.2.4.2.4. Heading content</a>
	 */
	public static final Set<NsName> HEADING_CONTENT = Stream.of(ELEMENT_H1, ELEMENT_H2, ELEMENT_H3, ELEMENT_H4, ELEMENT_H5, ELEMENT_H6)
			.map(elementName -> NsName.of(XHTML_NAMESPACE_URI_STRING, elementName)).collect(toUnmodifiableSet());

	/**
	 * Elements HTML5 considers <dfn>phrasing content</dfn>.
	 * @apiNote This includes the <code>&lt;math&gt;</code> and <code>&lt;svg&gt;</code> elements, even though they they are technically in separate namespaces,
	 *          because they are classified as phrasing content in HTML5.
	 * @see <a href="https://www.w3.org/TR/html52/dom.html#phrasing-content">HTML 5.2 § 3.2.4.2.5. Phrasing content</a>
	 * @see <a href="https://www.w3.org/TR/MathML/chapter2.html#interf.namespace">Mathematical Markup Language (MathML) Version 3.0 2nd Edition § 2.1.2 MathML and
	 *      Namespaces</a>
	 * @see <a href="https://www.w3.org/TR/SVG2/struct.html#Namespace">Scalable Vector Graphics (SVG) 2 § 5.1.2. Namespace</a>
	 */
	public static final Set<NsName> PHRASING_CONTENT = Stream.concat(
			//HTML elements
			Stream.of(ELEMENT_A, ELEMENT_ABBR, ELEMENT_AREA, //if it is a descendant of a `<map>` element
					ELEMENT_AUDIO, ELEMENT_B, ELEMENT_BDI, ELEMENT_BDO, ELEMENT_BR, ELEMENT_BUTTON, ELEMENT_CANVAS, ELEMENT_CITE, ELEMENT_CODE, ELEMENT_DATA,
					ELEMENT_DATALIST, ELEMENT_DEL, ELEMENT_DFN, ELEMENT_EM, ELEMENT_EMBED, ELEMENT_I, ELEMENT_IFRAME, ELEMENT_IMG, ELEMENT_INPUT, ELEMENT_INS,
					ELEMENT_KBD, ELEMENT_LABEL, ELEMENT_LINK, //if it is allowed in the body
					ELEMENT_MAP, ELEMENT_MARK, //MathML: `<math>`
					ELEMENT_METER, ELEMENT_NOSCRIPT, ELEMENT_OBJECT, ELEMENT_OUTPUT, ELEMENT_PICTURE, ELEMENT_PROGRESS, ELEMENT_Q, ELEMENT_RUBY, ELEMENT_S, ELEMENT_SAMP,
					ELEMENT_SCRIPT, ELEMENT_SELECT, ELEMENT_SMALL, ELEMENT_SPAN, ELEMENT_STRONG, ELEMENT_SUB, ELEMENT_SUP, //SVG: `<svg>`
					ELEMENT_TEMPLATE, ELEMENT_TEXTAREA, ELEMENT_TIME, ELEMENT_U, ELEMENT_VAR, ELEMENT_VIDEO, ELEMENT_WBR)
					.map(elementName -> NsName.of(XHTML_NAMESPACE_URI_STRING, elementName)),
			//non-HTML elements
			Stream.of(
					//MathML
					NsName.of("http://www.w3.org/1998/Math/MathML", "math"),
					//SVG
					NsName.of("http://www.w3.org/2000/svg", "svg")))
			.collect(toUnmodifiableSet());

	/**
	 * Elements HTML5 considers <dfn>embedded content</dfn>.
	 * @apiNote This includes the <code>&lt;math&gt;</code> and <code>&lt;svg&gt;</code> elements, even though they they are technically in separate namespaces,
	 *          because they are classified as embedded content in HTML5.
	 * @see <a href="https://www.w3.org/TR/html52/dom.html#embedded-content">HTML 5.2 § 3.2.4.2.6. Embedded content</a>
	 * @see <a href="https://www.w3.org/TR/MathML/chapter2.html#interf.namespace">Mathematical Markup Language (MathML) Version 3.0 2nd Edition § 2.1.2 MathML and
	 *      Namespaces</a>
	 * @see <a href="https://www.w3.org/TR/SVG2/struct.html#Namespace">Scalable Vector Graphics (SVG) 2 § 5.1.2. Namespace</a>
	 */
	public static final Set<NsName> EMBEDDED_CONTENT = Stream.concat(
			//HTML elements
			Stream.of(ELEMENT_AUDIO, ELEMENT_CANVAS, ELEMENT_EMBED, ELEMENT_IFRAME, ELEMENT_IMG, //MathML: `<math>`
					ELEMENT_OBJECT, ELEMENT_PICTURE, //SVG: `<svg>`
					ELEMENT_VIDEO).map(elementName -> NsName.of(XHTML_NAMESPACE_URI_STRING, elementName)),
			//non-HTML elements
			Stream.of(
					//MathML
					NsName.of("http://www.w3.org/1998/Math/MathML", "math"),
					//SVG
					NsName.of("http://www.w3.org/2000/svg", "svg")))
			.collect(toUnmodifiableSet());

	/**
	 * Elements HTML5 considers <dfn>interactive content</dfn>.
	 * @see <a href="https://www.w3.org/TR/html52/dom.html#interactive-content">HTML 5.2 § 3.2.4.2.7. Interactive content</a>
	 */
	public static final Set<NsName> INTERACTIVE_CONTENT = Stream.of(ELEMENT_A, //if the `href` attribute is present
			ELEMENT_AUDIO, //if the `controls` attribute is present
			ELEMENT_BUTTON, ELEMENT_DETAILS, ELEMENT_EMBED, ELEMENT_IFRAME, ELEMENT_IMG, //if the `usemap` attribute is present
			ELEMENT_INPUT, //if the `type` attribute is not in the `Hidden` state
			ELEMENT_LABEL, ELEMENT_SELECT, ELEMENT_TEXTAREA, ELEMENT_VIDEO) //if the controls attribute is present 
			.map(elementName -> NsName.of(XHTML_NAMESPACE_URI_STRING, elementName)).collect(toUnmodifiableSet());

	/**
	 * Elements HTML5 considers <dfn>palpable content</dfn>.
	 * @apiNote This includes the <code>&lt;math&gt;</code> and <code>&lt;svg&gt;</code> elements, even though they they are technically in separate namespaces,
	 *          because they are classified as palpable content in HTML5.
	 * @see <a href="https://www.w3.org/TR/html52/dom.html#palpable-content">HTML 5.2 § 3.2.4.2.8. Palpable content</a>
	 * @see <a href="https://www.w3.org/TR/MathML/chapter2.html#interf.namespace">Mathematical Markup Language (MathML) Version 3.0 2nd Edition § 2.1.2 MathML and
	 *      Namespaces</a>
	 * @see <a href="https://www.w3.org/TR/SVG2/struct.html#Namespace">Scalable Vector Graphics (SVG) 2 § 5.1.2. Namespace</a>
	 */
	public static final Set<NsName> PALPABLE_CONTENT = Stream.concat(
			//HTML elements
			Stream.of(ELEMENT_A, ELEMENT_ABBR, ELEMENT_ADDRESS, ELEMENT_ARTICLE, ELEMENT_ASIDE, ELEMENT_AUDIO, //if the `controls` attribute is present
					ELEMENT_B, ELEMENT_BDI, ELEMENT_BDO, ELEMENT_BLOCKQUOTE, ELEMENT_BUTTON, ELEMENT_CANVAS, ELEMENT_CITE, ELEMENT_CODE, ELEMENT_DATA, ELEMENT_DETAILS,
					ELEMENT_DFN, ELEMENT_DIV, ELEMENT_DL, //if the element’s children include at least one name-value group
					ELEMENT_EM, ELEMENT_EMBED, ELEMENT_FIELDSET, ELEMENT_FIGURE, ELEMENT_FOOTER, ELEMENT_FORM, ELEMENT_H1, ELEMENT_H2, ELEMENT_H3, ELEMENT_H4, ELEMENT_H5,
					ELEMENT_H6, ELEMENT_HEADER, ELEMENT_I, ELEMENT_IFRAME, ELEMENT_IMG, ELEMENT_INPUT, //if the `type` attribute is not in the `Hidden` state
					ELEMENT_INS, ELEMENT_KBD, ELEMENT_LABEL, ELEMENT_MAIN, ELEMENT_MAP, ELEMENT_MARK, //MathML: `<math>`
					ELEMENT_METER, ELEMENT_NAV, ELEMENT_OBJECT, ELEMENT_OL, //if the element’s children include at least one `<li>` element
					ELEMENT_OUTPUT, ELEMENT_P, ELEMENT_PRE, ELEMENT_PROGRESS, ELEMENT_Q, ELEMENT_RUBY, ELEMENT_S, ELEMENT_SAMP, ELEMENT_SECTION, ELEMENT_SELECT,
					ELEMENT_SMALL, ELEMENT_SPAN, ELEMENT_STRONG, ELEMENT_SUB, ELEMENT_SUP, //SVG: `<svg>`
					ELEMENT_TABLE, ELEMENT_TEXTAREA, ELEMENT_TIME, ELEMENT_U, ELEMENT_UL, //if the element’s children include at least one `<li>` element
					ELEMENT_VAR, ELEMENT_VIDEO).map(elementName -> NsName.of(XHTML_NAMESPACE_URI_STRING, elementName)),
			//non-HTML elements
			Stream.of(
					//MathML
					NsName.of("http://www.w3.org/1998/Math/MathML", "math"),
					//SVG
					NsName.of("http://www.w3.org/2000/svg", "svg")))
			.collect(toUnmodifiableSet());

	/**
	 * Elements HTML5 considers <dfn>script-supporting elements</dfn>.
	 * @see <a href="https://www.w3.org/TR/html52/dom.html#script-supporting-elements">HTML 5.2 § 3.2.4.2.9. Script-supporting elements</a>
	 */
	public static final Set<NsName> SCRIPT_SUPPORTING_ELEMENTS = Stream.of(ELEMENT_SCRIPT, ELEMENT_TEMPLATE)
			.map(elementName -> NsName.of(XHTML_NAMESPACE_URI_STRING, elementName)).collect(toUnmodifiableSet());

	//roles based upon styles; see [HTML 5.2 § 10. Rendering](https://www.w3.org/TR/html52/rendering.html#rendering); see also [Browsers' default CSS for HTML elements](https://stackoverflow.com/q/6867254/421049)

	/**
	 * Elements HTML5 and CSS 2.1 suggests should be rendered as CSS <code>display: block</code>.
	 * @apiNote The majority of these elements follow the HTML 5 specification, but they also include obsolete elements not mentioned as block elements but
	 *          indicated to be block elements by CSS 2.1 for HTML 4.
	 * @see <a href="https://www.w3.org/TR/html52/rendering.html#the-page">HTML 5.2 § 10.3.2. The page</a>
	 * @see <a href="https://www.w3.org/TR/html52/rendering.html#non-replaced-elements-flow-content">HTML 5.2 § 10.3.3. Flow content</a>
	 * @see <a href="https://www.w3.org/TR/html52/rendering.html#sections-and-headings">HTML 5.2 § 10.3.7. Sections and headings</a>
	 * @see <a href="https://www.w3.org/TR/html52/rendering.html#section-lists">HTML 5.2 § 10.3.8. Lists</a>
	 * @see <a href="https://www.w3.org/TR/html52/rendering.html#the-fieldset-and-legend-elements">HTML 5.2 § 10.3.13. The fieldset and legend elements</a>
	 * @see <a href="https://www.w3.org/TR/CSS2/sample.html">CSS 2.1 Appendix D. Default style sheet for HTML 4</a>
	 */
	public static final Set<NsName> BLOCK_ELEMENTS = Stream.of(
			//the page
			ELEMENT_HTML, ELEMENT_BODY,
			//flow content
			ELEMENT_ADDRESS, ELEMENT_BLOCKQUOTE, ELEMENT_CENTER, ELEMENT_DIV, ELEMENT_FIGURE, ELEMENT_FIGCAPTION, ELEMENT_FOOTER, ELEMENT_FORM, ELEMENT_HEADER,
			ELEMENT_HR, ELEMENT_LEGEND, ELEMENT_LISTING, ELEMENT_MAIN, ELEMENT_P, ELEMENT_PLAINTEXT, ELEMENT_PRE, ELEMENT_XMP,
			//sections and headings
			ELEMENT_ARTICLE, ELEMENT_ASIDE, ELEMENT_H1, ELEMENT_H2, ELEMENT_H3, ELEMENT_H4, ELEMENT_H5, ELEMENT_H6, ELEMENT_HGROUP, ELEMENT_NAV, ELEMENT_SECTION,
			//lists
			ELEMENT_DIR, ELEMENT_DD, ELEMENT_DL, ELEMENT_DT, ELEMENT_OL, ELEMENT_UL,
			//fieldset and legend
			ELEMENT_FIELDSET,
			//obsolete elements
			ELEMENT_FRAME, ELEMENT_FRAMESET, ELEMENT_NOFRAMES, ELEMENT_CENTER, ELEMENT_MENU).map(elementName -> NsName.of(XHTML_NAMESPACE_URI_STRING, elementName))
			.collect(toUnmodifiableSet());

	/**
	 * Elements HTML5 suggests should be rendered as CSS <code>display: list-item</code>.
	 * @see <a href="https://www.w3.org/TR/html52/rendering.html#section-lists">HTML 5.2 § 10.3.8. Lists</a>
	 * @see <a href="https://www.w3.org/TR/html52/rendering.html#the-details-element-rendering">HTML 5.2 § 10.5.3. The details and summary elements</a>
	 */
	public static final Set<NsName> LIST_ITEM_ELEMENTS = Stream.of(
			//lists
			ELEMENT_LI,
			//details and summary
			ELEMENT_SUMMARY).map(elementName -> NsName.of(XHTML_NAMESPACE_URI_STRING, elementName)).collect(toUnmodifiableSet());

}
