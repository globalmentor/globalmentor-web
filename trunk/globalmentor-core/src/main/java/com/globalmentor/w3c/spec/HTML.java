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

package com.globalmentor.w3c.spec;

import java.net.URI;
import java.util.*;

import com.globalmentor.java.Characters;
import com.globalmentor.model.IDed;
import com.globalmentor.net.ContentType;

import static com.globalmentor.collections.Sets.*;
import static com.globalmentor.java.Characters.*;
import static com.globalmentor.java.Objects.*;
import static com.globalmentor.net.ContentTypeConstants.*;

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

	/** The content type for HTML: <code>text/html</code>. */
	public static final ContentType HTML_CONTENT_TYPE = ContentType.create(ContentType.TEXT_PRIMARY_TYPE, HTML_SUBTYPE);

	/** The content type for XHTML: <code>application/xhtml+xml</code>. */
	public static final ContentType XHTML_CONTENT_TYPE = ContentType.create(ContentType.APPLICATION_PRIMARY_TYPE, XHTML_XML_SUBTYPE);

	/** The content type for an XHTML fragment: <code>application/xhtml+xml-external-parsed-entity</code>. */
	public static final ContentType XHTML_FRAGMENT_CONTENT_TYPE = ContentType.create(ContentType.APPLICATION_PRIMARY_TYPE,
			XHTML_XML_EXTERNAL_PARSED_ENTITY_SUBTYPE);

	/** The old extension for HTML resource names. */
	public static final String HTM_NAME_EXTENSION = "htm";

	/** The extension for HTML resource names. */
	public static final String HTML_NAME_EXTENSION = "html";

	/** The extension for XHTML resource names. */
	public static final String XHTML_NAME_EXTENSION = "xhtml";

	/** The recommended prefix to the XHTML namespace. */
	public static final String XHTML_NAMESPACE_PREFIX = "xhtml";

	/** The URI to the XHTML namespace. */
	public static final URI XHTML_NAMESPACE_URI = URI.create("http://www.w3.org/1999/xhtml");

	/** The public ID for the HTML 2.0 DTD. */
	public static final String HTML_2_0_PUBLIC_ID = "-//IETF//DTD HTML 2.0//EN";

	/** The public ID for the HTML 3.2 DTD. */
	public static final String HTML_3_2_PUBLIC_ID = "-//W3C//DTD HTML 3.2 Final//EN";

	/** The public ID for the HTML 4.01 Strict DTD. */
	public static final String HTML_4_01_STRICT_PUBLIC_ID = "-//W3C//DTD HTML 4.01//EN";
	/** The system ID for the HTML 4.01 Strict DTD. */
	public static final String HTML_4_01_STRICT_SYSTEM_ID = "http://www.w3.org/TR/html4/strict.dtd";

	/** The public ID for the HTML 4.01 Traditional DTD. */
	public static final String HTML_4_01_TRANSITIONAL_PUBLIC_ID = "-//W3C//DTD HTML 4.01 Transitional//EN";
	/** The system ID for the HTML 4.01 Traditional DTD. */
	public static final String HTML_4_01_TRANSITIONAL_SYSTEM_ID = "http://www.w3.org/TR/html4/loose.dtd";

	/** The public ID for the HTML 4.01 Frameset DTD. */
	public static final String HTML_4_01_FRAMESET_PUBLIC_ID = "-//W3C//DTD HTML 4.01 Frameset//EN";
	/** The system ID for the HTML 4.01 Frameset DTD. */
	public static final String HTML_4_01_FRAMESET_SYSTEM_ID = "http://www.w3.org/TR/html4/frameset.dtd";

	/** The public ID for the XHTML 1.0 Strict DTD. */
	public static final String XHTML_1_0_STRICT_PUBLIC_ID = "-//W3C//DTD XHTML 1.0 Strict//EN";
	/** The system ID for the XHTML 1.0 Strict DTD. */
	public static final String XHTML_1_0_STRICT_SYSTEM_ID = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd";

	/** The public ID for the XHTML 1.0 Traditional DTD. */
	public static final String XHTML_1_0_TRANSITIONAL_PUBLIC_ID = "-//W3C//DTD XHTML 1.0 Transitional//EN";
	/** The system ID for the XHTML 1.0 Traditional DTD. */
	public static final String XHTML_1_0_TRANSITIONAL_SYSTEM_ID = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd";

	/** The public ID for the XHTML 1.0 Frameset DTD. */
	public static final String XHTML_1_0_FRAMESET_PUBLIC_ID = "-//W3C//DTD XHTML 1.0 Frameset//EN";
	/** The system ID for the XHTML 1.0 Frameset DTD. */
	public static final String XHTML_1_0_FRAMESET_SYSTEM_ID = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd";

	/** The public ID for the XHTML 1.1 DTD. */
	public static final String XHTML_1_1_PUBLIC_ID = "-//W3C//DTD XHTML 1.1//EN";
	/** The system ID for the XHTML 1.1 DTD. */
	public static final String XHTML_1_1_SYSTEM_ID = "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd";

	/** The public ID for the XHTML+MathML+SVG DTD. */
	public static final String XHTML_MATHML_SVG_PUBLIC_ID = "-//W3C//DTD XHTML 1.1 plus MathML 2.0 plus SVG 1.1//EN";
	/** The system ID for the XHTML+MathML+SVG DTD. */
	public static final String XHTML_MATHML_SVG_SYSTEM_ID = "http://www.w3.org/2002/04/xhtml-math-svg/xhtml-math-svg.dtd";

	/**
	 * Characters considered <dfn>space characters</dfn> in HTML.
	 * @see <a href="http://www.w3.org/TR/html5/infrastructure.html#space-character">HTML5 space characters</a>
	 */
	public static final Characters SPACE_CHARACTERS = new Characters(SPACE_CHAR, CHARACTER_TABULATION_CHAR, LINE_FEED_CHAR, FORM_FEED_CHAR, CARRIAGE_RETURN_CHAR);

	//The XHTML 1.0 document element names.
	//TODO fix; some are missing since these were copied from the OEB 1.0
	public static final String ELEMENT_A = "a";
	public static final String ELEMENT_ADDRESS = "address";
	public static final String ELEMENT_APPLET = "applet";
	public static final String ELEMENT_AREA = "area";
	public static final String ELEMENT_ARTICLE = "article";
	public static final String ELEMENT_ASIDE = "aside";
	public static final String ELEMENT_B = "b";
	public static final String ELEMENT_BASE = "base";
	public static final String ELEMENT_BIG = "big";
	public static final String ELEMENT_BLOCKCODE = "blockcode";
	public static final String ELEMENT_BLOCKQUOTE = "blockquote";
	public static final String ELEMENT_BODY = "body";
	public static final String ELEMENT_BR = "br";
	public static final String ELEMENT_BUTTON = "button";
	public static final String ELEMENT_CAPTION = "caption";
	public static final String ELEMENT_CENTER = "center";
	public static final String ELEMENT_CITE = "cite";
	public static final String ELEMENT_COL = "col";
	public static final String ELEMENT_CODE = "code";
	public static final String ELEMENT_DD = "dd";
	public static final String ELEMENT_DFN = "dfn";
	public static final String ELEMENT_DIV = "div";
	public static final String ELEMENT_DL = "dl";
	public static final String ELEMENT_DT = "dt";
	public static final String ELEMENT_EM = "em";
	public static final String ELEMENT_EMBED = "embed";
	public static final String ELEMENT_FIELDSET = "fieldset";
	public static final String ELEMENT_FIGCAPTION = "figcaption";
	public static final String ELEMENT_FIGURE = "figure";
	public static final String ELEMENT_FONT = "font";
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
	public static final String ELEMENT_KBD = "kbd";
	public static final String ELEMENT_LI = "li";
	public static final String ELEMENT_LABEL = "label";
	public static final String ELEMENT_LEGEND = "legend";
	public static final String ELEMENT_LINK = "link";
	public static final String ELEMENT_MAP = "map";
	public static final String ELEMENT_META = "meta";
	public static final String ELEMENT_NAV = "nav";
	public static final String ELEMENT_OBJECT = "object";
	public static final String ELEMENT_OL = "ol";
	public static final String ELEMENT_OPTION = "option";
	public static final String ELEMENT_P = "p";
	public static final String ELEMENT_PARAM = "param";
	public static final String ELEMENT_PRE = "pre";
	public static final String ELEMENT_Q = "q";
	public static final String ELEMENT_S = "s";
	public static final String ELEMENT_SAMP = "samp";
	public static final String ELEMENT_SCRIPT = "script";
	public static final String ELEMENT_SECTION = "section";
	public static final String ELEMENT_SELECT = "select";
	public static final String ELEMENT_SMALL = "small";
	public static final String ELEMENT_SPAN = "span";
	public static final String ELEMENT_STRIKE = "strike";
	public static final String ELEMENT_STRONG = "strong";
	public static final String ELEMENT_STYLE = "style";
	public static final String ELEMENT_SUB = "sub";
	public static final String ELEMENT_SUP = "sup";
	public static final String ELEMENT_TABLE = "table";
	public static final String ELEMENT_TD = "td";
	public static final String ELEMENT_TEXTAREA = "textarea";
	public static final String ELEMENT_TH = "th";
	public static final String ELEMENT_TBODY = "tbody";
	public static final String ELEMENT_THEAD = "thead";
	public static final String ELEMENT_TFOOT = "tfoot";
	public static final String ELEMENT_TITLE = "title";
	public static final String ELEMENT_TR = "tr";
	public static final String ELEMENT_TT = "tt";
	public static final String ELEMENT_U = "u";
	public static final String ELEMENT_UL = "ul";
	public static final String ELEMENT_VAR = "var";

	//attributes

	/**
	 * The delimiter character for separating parts of an attribute string (e.g. for HTML5 data- attributes). This is distinct from the namespace prefix
	 * delimiter.
	 */
	public static final char ATTRIBUTE_DELIMITER_CHAR = '-';

	/**
	 * The identifier, with no delimiter, indicating that an attribute is an HTML5 data attribute
	 * @see <a href="http://www.w3.org/TR/html5/elements.html#embedding-custom-non-visible-data-with-the-data-attributes">HTML 5 Data Attributes</a>
	 */
	public static final String DATA_ATTRIBUTE_ID = "data";

	/** The attribute for class. */
	public static final String ATTRIBUTE_CLASS = "class";
	/** The attribute for direction. */
	public static final String ATTRIBUTE_DIR = "dir";
	/** The attribute for left-to-right direction. */
	public static final String DIR_LTR = "ltr";
	/** The attribute for right-to-left direction. */
	public static final String DIR_RTL = "rtl";
	/** The attribute for ID. */
	public static final String ATTRIBUTE_ID = "id";
	/** The attribute for language. */
	public static final String ATTRIBUTE_LANG = "lang";
	/** The attribute for name. */
	public static final String ATTRIBUTE_NAME = "name";
	/** The attribute for style. */
	public static final String ATTRIBUTE_STYLE = "style";
	/** The attribute for title. */
	public static final String ATTRIBUTE_TITLE = "title";
	/** The attribute for value. */
	public static final String ATTRIBUTE_VALUE = "value";
	//event attributes
	public static final String ATTRIBUTE_ONCLICK = "onclick";
	public static final String ATTRIBUTE_ONLOAD = "onload";

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
	public static final String ELEMENT_A_ATTRIBUTE_HREF = "href";
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
			this.id = checkInstance(id);
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

	//attributes for <button>
	public static final String ELEMENT_BUTTON_ATTRIBUTE_TYPE = "type";
	public static final String BUTTON_TYPE_BUTTON = "button";
	public static final String BUTTON_TYPE_RESET = "reset";
	public static final String BUTTON_TYPE_SUBMIT = "submit";

	//attributes for <form>
	public static final String ELEMENT_FORM_ATTRIBUTE_ACTION = "action";
	public static final String ELEMENT_FORM_ATTRIBUTE_ENCTYPE = "enctype";
	/** The "application/x-www-form-urlencoded" encoding type; see <a href="http://www.rfc-editor.org/rfc/rfc1867.txt">RFC 1867</a>. */
	public static final ContentType APPLICATION_X_WWW_FORM_URLENCODED_CONTENT_TYPE = ContentType.create(ContentType.APPLICATION_PRIMARY_TYPE,
			X_WWW_FORM_URLENCODED);
	/** The "multipart/form-data" encoding type; see <a href="http://www.rfc-editor.org/rfc/rfc1867.txt">RFC 1867</a>. */
	public static final ContentType MULTIPART_FORM_DATA_CONTENT_TYPE = ContentType.create(ContentType.MULTIPART_PRIMARY_TYPE, FORM_DATA_SUBTYPE);

	public static final String ELEMENT_FORM_ATTRIBUTE_METHOD = "method";
	public static final String FORM_METHOD_GET = "get";
	public static final String FORM_METHOD_POST = "post";

	//attributes for <embed>
	public static final String ELEMENT_EMBED_ATTRIBUTE_SRC = "src";
	public static final String ELEMENT_EMBED_ATTRIBUTE_HEIGHT = "height";
	public static final String ELEMENT_EMBED_ATTRIBUTE_WIDTH = "width";
	public static final String ELEMENT_EMBED_ATTRIBUTE_TYPE = "type";

	//attributes for <iframe>
	public static final String ELEMENT_IFRAME_ATTRIBUTE_ALLOW_TRANSPARENCY = "allowTransparency"; //IE-specific; see http://msdn.microsoft.com/en-us/library/ms533072.aspx
	public static final String ELEMENT_IFRAME_ATTRIBUTE_SRC = "src";
	public static final String ELEMENT_IFRAME_ATTRIBUTE_FRAMEBORDER = "frameborder";
	public static final String ELEMENT_IFRAME_ATTRIBUTE_SCROLLING = "scrolling";
	public static final String IFRAME_SCROLLING_NO = "no";
	public static final String IFRAME_SCROLLING_YES = "yes";
	public static final String IFRAME_SCROLLING_AUTO = "auto";

	//attributes for <img>
	public static final String ELEMENT_IMG_ATTRIBUTE_ALT = "alt";
	public static final String ELEMENT_IMG_ATTRIBUTE_HEIGHT = "height";
	public static final String ELEMENT_IMG_ATTRIBUTE_WIDTH = "width";
	public static final String ELEMENT_IMG_ATTRIBUTE_SRC = "src";

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
	public static final String ELEMENT_LINK_ATTRIBUTE_HREF = "href";
	public static final String ELEMENT_LINK_ATTRIBUTE_REL = LINK_ATTRIBUTE_REL;
	public static final String ELEMENT_LINK_ATTRIBUTE_TYPE = "type";
	public static final String ELEMENT_LINK_ATTRIBUTE_MEDIA = "media";
	public static final String LINK_MEDIA_SCREEN = "screen";

	//attributes for <meta>
	public static final String ELEMENT_META_ATTRIBUTE_CONTENT = "content";
	public static final String ELEMENT_META_ATTRIBUTE_NAME = "name";
	public static final String ELEMENT_META_ATTRIBUTE_PROPERTY = "property"; //TODO Facebook OpenGraph specific; document and move; see https://developers.facebook.com/docs/opengraph/ 

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
	public static final String ELEMENT_SCRIPT_ATTRIBUTE_SRC = "src";
	public static final String ELEMENT_SCRIPT_ATTRIBUTE_TYPE = "type";
	public static final String ELEMENT_SCRIPT_ATTRIBUTE_LANGUAGE = "language";

	//attributes for <select>
	public static final String ELEMENT_SELECT_ATTRIBUTE_MULTIPLE = "multiple";
	public static final String SELECT_MULTIPLE_MULTIPLE = "multiple";
	public static final String ELEMENT_SELECT_ATTRIBUTE_SIZE = "size";

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

	//attributes for <tr>
	public static final String ELEMENT_TR_ATTRIBUTE_ALIGN = "align";
	public static final String TR_ALIGN_LEFT = "left";
	public static final String TR_ALIGN_CENTER = "center";
	public static final String TR_ALIGN_RIGHT = "right";
	public static final String ELEMENT_TR_ATTRIBUTE_VALIGN = "valign";
	public static final String TR_VALIGN_TOP = "top";
	public static final String TR_VALIGN_MIDDLE = "middle";
	public static final String TR_VALIGN_BOTTOM = "bottom";

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

	/**
	 * The XHTML elements that by default have <code>display:block</code> CSS style.
	 * @see <a href="http://www.w3.org/TR/2003/WD-xhtml2-20030506/mod-block-text.html">XHTML Block Text Module</a>
	 */
	public static final Set<String> BLOCK_ELEMENTS = immutableSetOf(ELEMENT_ADDRESS, ELEMENT_BLOCKCODE, ELEMENT_BLOCKQUOTE, ELEMENT_DIV, ELEMENT_H1, ELEMENT_H2,
			ELEMENT_H3, ELEMENT_H4, ELEMENT_H5, ELEMENT_H6, ELEMENT_HR, ELEMENT_P, ELEMENT_PRE, ELEMENT_SECTION);

	/**
	 * Parses out the HTML CSS class IDs from the given classes character sequence.
	 * @param htmlClass The classes character sequence to split.
	 * @return The HTML CSS class identifiers, if any.
	 * @see #ATTRIBUTE_CLASS
	 * @see #splitSpaces(CharSequence)
	 * @see <a href="http://www.w3.org/TR/html5/dom.html#classes>The <code>class</code> attribute</a>
	 */
	public static Set<CharSequence> getClasses(final CharSequence htmlClass) {
		return immutableSetOf(splitSpaces(htmlClass)); //remove duplicates
	}

	/**
	 * Splits a string on spaces according to the HTML specification. Essentially this involves trimming the string of Unicode whitespace characters and then
	 * splitting the resulting string on HTML {@link #SPACE_CHARACTERS}, although Unicode whitespace is not allowed to begin the resulting tokens.
	 * <p>
	 * TODO This implementation does not yet support full splitting on Unicode whitespace.
	 * </p>
	 * @param charSequence The character sequence to split.
	 * @return The tokens resulting from the split-string-on-spaces operation.
	 * @see <a href="http://www.w3.org/TR/html5/infrastructure.html#split-a-string-on-spaces">split a string on spaces</a>
	 */
	public static List<CharSequence> splitSpaces(final CharSequence charSequence) {
		return SPACE_CHARACTERS.split(charSequence); //split on character
	}

}
