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

package com.globalmentor.text.xml.xhtml;

import java.io.*;
import java.net.URI;
import java.util.*;

import javax.xml.parsers.*;

import org.w3c.dom.*;

import com.globalmentor.config.ConfigurationException;
import com.globalmentor.io.*;

import com.globalmentor.java.Arrays;
import com.globalmentor.java.Characters;
import com.globalmentor.java.Objects;
import com.globalmentor.log.Log;
import com.globalmentor.model.IDed;
import com.globalmentor.net.ContentType;
import com.globalmentor.text.xml.XML;

import static com.globalmentor.text.ASCII.*;
import static com.globalmentor.text.xml.xpath.XPath.*;

import static com.globalmentor.collections.Sets.*;
import static com.globalmentor.java.Characters.*;
import static com.globalmentor.java.Objects.*;
import static com.globalmentor.net.ContentTypeConstants.*;
import static com.globalmentor.text.xml.XML.*;

/**
 * Constants to work with an XHTML and DOM document representing XHTML.
 * @author Garret Wilson
 * @see <a href="http://www.w3.org/QA/2002/04/valid-dtd-list.html">W3C QA - Recommended list of DTDs</a>
 * @see <a href="http://www.pibil.org/technology/writing/xhtml-media-type.html">XHTML: An XML Application</a>
 * @see <a href="http://www.w3.org/TR/xhtml-media-types/">XHTML Media Types</a>
 */
public class XHTML {

	/** HTML MIME subtype. */
	public static final String HTML_SUBTYPE = "html";

	/** An XHTML application. */
	public static final String XHTML_XML_SUBTYPE = "xhtml" + ContentType.SUBTYPE_SUFFIX_DELIMITER_CHAR + XML_SUBTYPE_SUFFIX;

	/** An XHTML fragment (not yet formally defined). */
	public static final String XHTML_XML_EXTERNAL_PARSED_ENTITY_SUBTYPE = "xhtml" + ContentType.SUBTYPE_SUFFIX_DELIMITER_CHAR
			+ XML_EXTERNAL_PARSED_ENTITY_SUBTYPE_SUFFIX;

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
	 * Creates a new unformatted default XHTML document with the required minimal structure, {@code <html><head><title></title<head><body></html>}, with no
	 * document type.
	 * @param title The title of the document.
	 * @param includeDocumentType Whether a document type should be added to the document.
	 * @param publicID The external subset public identifier, or <code>null</code> if there is no such identifier.
	 * @param systemID The external subset system identifier, or <code>null</code> if there is no such identifier.
	 * @return A newly created default generic XHTML document with the required minimal structure.
	 * @throws NullPointerException if the given title is <code>null</code>.
	 * @throws ConfigurationException if a document builder cannot be created which satisfies the configuration requested.
	 * @throws DOMException if there is an error creating the XHTML document.
	 */
	public static Document createXHTMLDocument(final String title) throws DOMException {
		return createXHTMLDocument(title, false); //create an unformatted XHTML document with no document type
	}

	/**
	 * Creates a new default XHTML document with the required minimal structure, {@code <html><head><title></title<head><body></html>}, with no document type.
	 * @param title The title of the document.
	 * @param includeDocumentType Whether a document type should be added to the document.
	 * @param publicID The external subset public identifier, or <code>null</code> if there is no such identifier.
	 * @param systemID The external subset system identifier, or <code>null</code> if there is no such identifier.
	 * @param formatted <code>true</code> if the sections of the document should be formatted.
	 * @return A newly created default generic XHTML document with the required minimal structure.
	 * @throws NullPointerException if the given title is <code>null</code>.
	 * @throws ConfigurationException if a document builder cannot be created which satisfies the configuration requested.
	 * @throws DOMException if there is an error creating the XHTML document.
	 */
	public static Document createXHTMLDocument(final String title, final boolean formatted) throws DOMException {
		return createXHTMLDocument(title, false, null, null, formatted); //create an XHTML document with no document type 
	}

	/**
	 * Creates a new unformatted default XHTML document with the required minimal structure, {@code <html><head><title></title<head><body></html>}, with an
	 * optional document type but no public ID or system ID.
	 * @param title The title of the document.
	 * @param includeDocumentType Whether a document type should be added to the document.
	 * @param formatted <code>true</code> if the sections of the document should be formatted.
	 * @return A newly created default generic XHTML document with the required minimal structure.
	 * @throws NullPointerException if the given title is <code>null</code>.
	 * @throws ConfigurationException if a document builder cannot be created which satisfies the configuration requested.
	 * @throws DOMException if there is an error creating the XHTML document.
	 */
	public static Document createXHTMLDocument(final String title, final boolean includeDocumentType, final boolean formatted) throws DOMException {
		return createXHTMLDocument(title, includeDocumentType, null, null, formatted); //create an XHTML document with optional doctype		
	}

	/**
	 * Creates a new unformatted default XHTML document with the required minimal structure, {@code <html><head><title></title<head><body></html>}, with an
	 * optional document type.
	 * @param title The title of the document.
	 * @param includeDocumentType Whether a document type should be added to the document.
	 * @param publicID The external subset public identifier, or <code>null</code> if there is no such identifier.
	 * @param systemID The external subset system identifier, or <code>null</code> if there is no such identifier.
	 * @return A newly created default generic XHTML document with the required minimal structure.
	 * @throws NullPointerException if the given title is <code>null</code>.
	 * @throws ConfigurationException if a document builder cannot be created which satisfies the configuration requested.
	 * @throws DOMException if there is an error creating the XHTML document.
	 */
	public static Document createXHTMLDocument(final String title, final boolean includeDocumentType, final String publicID, final String systemID)
			throws DOMException {
		return createXHTMLDocument(title, includeDocumentType, publicID, systemID, false); //create an unformatted XHTML document
	}

	/**
	 * Creates a new default XHTML document with the required minimal structure, {@code <html><head><title></title<head><body></html>}, with an optional document
	 * type.
	 * @param title The title of the document.
	 * @param includeDocumentType Whether a document type should be added to the document.
	 * @param publicID The external subset public identifier, or <code>null</code> if there is no such identifier.
	 * @param systemID The external subset system identifier, or <code>null</code> if there is no such identifier.
	 * @param formatted <code>true</code> if the sections of the document should be formatted.
	 * @return A newly created default generic XHTML document with the required minimal structure.
	 * @throws NullPointerException if the given title is <code>null</code>.
	 * @throws ConfigurationException if a document builder cannot be created which satisfies the configuration requested.
	 * @throws DOMException if there is an error creating the XHTML document.
	 */
	public static Document createXHTMLDocument(final String title, final boolean includeDocumentType, final String publicID, final String systemID,
			final boolean formatted) throws DOMException {
		final DocumentBuilder documentBuilder = createDocumentBuilder(true); //create a namespace-aware document builder
		final DOMImplementation domImplementation = documentBuilder.getDOMImplementation(); //get the DOM implementation from the document builder
		final DocumentType documentType; //the document type, if any
		if(includeDocumentType) { //if we should add a document type
			documentType = domImplementation.createDocumentType(ELEMENT_HTML, publicID, systemID); //create an XHTML document, using the given identifiers, if any 
		} else { //if we should not add a document type
			documentType = null; //don't use a document type
		}
		final Document document = domImplementation.createDocument(XHTML_NAMESPACE_URI.toString(), ELEMENT_HTML, documentType); //create an XHTML document
		//TODO check about whether we need to add a <head> and <title>
		final Element htmlElement = document.getDocumentElement(); //get the html element
		if(formatted) { //if we should format the document
			appendText(htmlElement, "\n"); //append a newline to start the content of the html element
		}
		final Element headElement = document.createElementNS(XHTML_NAMESPACE_URI.toString(), ELEMENT_HEAD); //create the head element
		htmlElement.appendChild(headElement); //add the head element to the html element
		if(formatted) { //if we should format the document
			appendText(headElement, "\n"); //append a newline to start the content in the head
			appendText(htmlElement, "\n"); //append a newline to separate the content of the html element
		}
		final Element titleElement = document.createElementNS(XHTML_NAMESPACE_URI.toString(), ELEMENT_TITLE); //create the title element
		headElement.appendChild(titleElement); //add the title element to the head element
		appendText(titleElement, title); //append the title text to the title element 
		if(formatted) { //if we should format the document
			appendText(headElement, "\n"); //append a newline to separate the informtaion after the title
		}
		final Element bodyElement = document.createElementNS(XHTML_NAMESPACE_URI.toString(), ELEMENT_BODY); //create the body element
		htmlElement.appendChild(bodyElement); //add the body element to the html element
		if(formatted) { //if we should format the document
			appendText(bodyElement, "\n"); //append a newline to separate the information in the body
			appendText(htmlElement, "\n"); //append a newline to separate the information after the body
		}
		return document; //return the document we created
	}

	/**
	 * Finds the XHTML <code>&lt;body&gt;</code> element.
	 * @param document The XHTML document tree.
	 * @return A reference to the <code>&lt;body&gt;</code> element, or <code>null</code> if there is such element.
	 */
	public static Element getBodyElement(final Document document) {
		return (Element)getNode(document, LOCATION_STEP_SEPARATOR_CHAR + ELEMENT_BODY);
	}

	/**
	 * Finds the XHTML {@code <head>} element.
	 * @param document The XHTML document tree.
	 * @return A reference to the {@code <head>} element, or <code>null</code> if there is no such element.
	 */
	public static Element getHeadElement(final Document document) {
		return (Element)getNode(document, LOCATION_STEP_SEPARATOR_CHAR + ELEMENT_HEAD);
	}

	/**
	 * Finds the XHTML {@code <head><title>} element.
	 * @param document The XHTML document tree.
	 * @return A reference to the {@code <head><title>} element, or <code>null</code> if there is no such element.
	 */
	public static Element getHeadTitleElement(final Document document) {
		return (Element)getNode(document, LOCATION_STEP_SEPARATOR_CHAR + ELEMENT_HEAD + LOCATION_STEP_SEPARATOR_CHAR + ELEMENT_TITLE);
	}

	/**
	 * Finds all XHTML {@code <head><meta>} elements.
	 * @param document The XHTML document tree.
	 * @return A list of all {@code <head><meta>} elements, if any.
	 */
	@SuppressWarnings("unchecked")
	public static List<Element> getHeadMetaElements(final Document document) {
		//		final Element headElement=getHeadElement(document);	//TODO improve XPath processor to extract elements

		return getElements((List<Node>)evaluatePathExpression(document.getDocumentElement(), ELEMENT_HEAD + LOCATION_STEP_SEPARATOR_CHAR + ELEMENT_META));
	}

	/**
	 * Finds the first XHTML {@code <head><meta>} element with the given name. The meta name is matched in a case insensitive manner.
	 * @param document The XHTML document tree.
	 * @param metaName The name of the meta element to return.
	 * @return The first {@code <head><meta>} element with the given name, case insensitive, or <code>null</code> if no such meta element can be found.
	 * @throws NullPointerException if the given document and/or name is <code>null</code>.
	 * @see #ELEMENT_META_ATTRIBUTE_NAME
	 */
	public static Element getHeadMetaElement(final Document document, final String metaName) {
		for(final Element element : getHeadMetaElements(document)) { //get all the meta elements
			if(metaName.equalsIgnoreCase(element.getAttribute(ELEMENT_META_ATTRIBUTE_NAME))) { //if this meta element has the correct name
				return element;
			}
		}
		return null; //we coudn't find such a named meta element
	}

	/**
	 * Finds the first XHTML {@code <head><meta>} element with the given name and returns its <code>content</code> attribute. The meta name is matched in a case
	 * insensitive manner.
	 * @param document The XHTML document tree.
	 * @param metaName The name of the meta element to return.
	 * @return The <code>content</code> attribute of the first {@code <head><meta>} element with the given name, case insensitive, or <code>null</code> if no such
	 *         meta element can be found.
	 * @throws NullPointerException if the given document and/or name is <code>null</code>.
	 * @see #ELEMENT_META_ATTRIBUTE_NAME
	 * @see #ELEMENT_META_ATTRIBUTE_CONTENT
	 */
	public static String getHeadMetaElementContent(final Document document, final String metaName) {
		final Element metaElement = getHeadMetaElement(document, metaName);
		return metaElement != null ? metaElement.getAttribute(ELEMENT_META_ATTRIBUTE_CONTENT) : null;
	}

	/**
	 * Determines the reference to the image file represented by the element, assuming the element (an "img" or "object" element) does in fact represent an image.
	 * For "img", this return the "src" attribute. For objects, the value of the "data" attribute is returned.
	 * @param xhtmlNamespaceURI The XHTML namespace.
	 * @param element The element which contains the image information.
	 * @return The reference to the image file, or <code>null</code> if no reference could be found.
	 */
	public static String getImageElementHRef(final String xhtmlNamespaceURI, final Element element) {
		if(element != null) { //if a valid element is passed
			final String namespaceURI = element.getNamespaceURI(); //get the element's namespace URI
			//if this element is in the correct namespace
			if((xhtmlNamespaceURI == null && namespaceURI == null) || namespaceURI.equals(xhtmlNamespaceURI)) {
				final String elementName = element.getLocalName(); //get the element's name
				//see if this is an <img> or <object> element
				if(elementName.equals(ELEMENT_IMG)) { //if the corresponding element is an img element
					//get the src attribute, representing the href of the image, or null if not present
					return getDefinedAttributeNS(element, null, ELEMENT_IMG_ATTRIBUTE_SRC);
				} else if(elementName.equals(ELEMENT_OBJECT)) { //if the corresponding element is an object element
					//get the data attribute, representing the href of the image, or null if not present
					return getDefinedAttributeNS(element, null, ELEMENT_OBJECT_ATTRIBUTE_DATA);
				}
			}
		}
		return null; //show that we couldn't find an image reference
	}

	/**
	 * Determines if the specified element represents a link, and if so attempts to find the link element's reference.
	 * @param xhtmlNamespaceURI The XHTML namespace.
	 * @param element The element which might represent a link.
	 * @return The reference of the link, or <code>null</code> if the specified element does not represent a link or has no reference.
	 */
	public static String getLinkElementHRef(final String xhtmlNamespaceURI, final Element element) { //TODO fix to call XLink and see if this is an XLink link
		if(element != null) { //if a valid element was passed
			final String namespaceURI = element.getNamespaceURI(); //get the element's namespace URI
			//if this element is in the correct namespace
			if(Objects.equals(xhtmlNamespaceURI, namespaceURI)) {
				final String elementName = element.getLocalName(); //get the element's name
				if(elementName.equals(ELEMENT_A)) { //if this is an <a> element
					return getDefinedAttributeNS(element, null, ELEMENT_A_ATTRIBUTE_HREF); //return the value of the href attribute, if there is one
				}
			}
		}
		return null; //show that we couldn't find an href or this wasn't even a link
	}

	/**
	 * Determines the content type indicated by the given link element.
	 * @param element The element, presumably a link ({@code<a>}, {@code<area>}, or {@code<link>}).
	 * @return The content type specified by the link element, or <code>null</code> if no content type was specified or the content type was not syntactically
	 *         correct.
	 * @see #LINK_ATTRIBUTE_TYPE
	 * @see <a href="http://www.w3.org/TR/html5/links.html#attr-hyperlink-type">HTML5 Link MIME type</a>
	 */
	public static ContentType getLinkContentType(final Element element) {
		final String typeString = element.getAttributeNS(null, LINK_ATTRIBUTE_TYPE); //get the value of the type attribute
		if(!typeString.isEmpty()) { //if there is a type specified
			try {
				return ContentType.create(typeString); //parse the content type and return it
			} catch(final IllegalArgumentException illegalArgumentException) { //if the content type isn't valid
				Log.debug(illegalArgumentException);
			}
		}
		return null;
	}

	/**
	 * Checks to see if the given link's {@value #LINK_ATTRIBUTE_REL} attribute contains the given link type with ASCII case insensitivity.
	 * @param element The element, presumably a link ({@code<a>}, {@code<area>}, or {@code<link>}).
	 * @param linkType The type of link to look for.
	 * @return <code>true</code> if the given element has a {@value #LINK_ATTRIBUTE_REL} attribute with one of the given link types.
	 * @see #LINK_ATTRIBUTE_REL
	 * @see <a href="http://www.w3.org/TR/html5/links.html#linkTypes">HTML5 Link Types</a>
	 */
	public static boolean hasLinkType(final Element element, final LinkType linkType) {
		return hasLinkType(element, linkType.getID());
	}

	/**
	 * Checks to see if the given link's {@value #LINK_ATTRIBUTE_REL} attribute contains the given link type with ASCII case insensitivity.
	 * @param element The element, presumably a link ({@code<a>}, {@code<area>}, or {@code<link>}).
	 * @param linkType The type of link to look for.
	 * @return <code>true</code> if the given element has a {@value #LINK_ATTRIBUTE_REL} attribute with one of the given link types.
	 * @see #LINK_ATTRIBUTE_REL
	 */
	public static boolean hasLinkType(final Element element, final String linkType) {
		return containsTokenIgnoreCase(element.getAttributeNS(null, LINK_ATTRIBUTE_REL), SPACE_CHARACTERS, linkType); //see if the given link type ID is one of the tokens in the "rel" attribute value
	}

	/**
	 * Determines if the given content type is one representing HTML in some form.
	 * <p>
	 * HTML content types include:
	 * </p>
	 * <ul>
	 * <li><code>text/html</code></li>
	 * <li><code>application/xhtml+xml</code></li>
	 * <li><code>application/xhtml+xml-external-parsed-entity</code> (not formally defined)</li>
	 * </ul>
	 * @param contentType The content type of a resource, or <code>null</code> for no content type.
	 * @return <code>true</code> if the given media type is one of several HTML media types.
	 */
	public static boolean isHTML(final ContentType contentType) { //TODO maybe move this to an HTMLUtilities
		if(contentType != null) { //if a media type is given
			final String primaryType = contentType.getPrimaryType(); //get the primary type
			final String subType = contentType.getSubType(); //get the sub type
			if(ContentType.TEXT_PRIMARY_TYPE.equals(primaryType)) { //if this is "text/?"
				if(HTML_SUBTYPE.equals(subType) //if this is "text/html"
						/*TODO fix for OEB || OEB.X_OEB1_DOCUMENT_SUBTYPE.equals(subType)*/) { //if this is "text/x-oeb1-document"
					return true; //show that this is HTML
				}
			}
			if(ContentType.APPLICATION_PRIMARY_TYPE.equals(primaryType)) { //if this is "application/?"
				//TODO probably add a parameter to specify whether we should allow fragments to qualify as XHTML---right now this behavior is not consistent with XMLUtilities.isXML(), which doesn't recognize fragments as XML
				if(XHTML_XML_SUBTYPE.equals(subType) || XHTML_XML_EXTERNAL_PARSED_ENTITY_SUBTYPE.equals(subType)) { //if this is "application/xhtml+xml" or "application/xhtml+xml-external-parsed-entity"
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
	 */
	public static boolean isHTMLNamespaceURI(final URI namespaceURI) {
		if(namespaceURI != null) { //if the namespace URI is valid
			//if it's part of the XHTML namespace
			if(XHTML_NAMESPACE_URI.equals(namespaceURI) /*TODO fix for OEB || OEB.OEB1_DOCUMENT_NAMESPACE_URI.equals(namespaceURI)*/)
				return true; //show that this is an HTML namespace
		}
		return false; //show that we didn't recognize the namespace as HTML
	}

	/**
	 * Determines if the specified element represents an image. Specifically, this returns <code>true</code> if the element's name is "img"; or if the element's
	 * name is "object" and the type attribute is an image or the data attribute references an image file.
	 * @param xhtmlNamespaceURI The XHTML namespace.
	 * @param element The element which might represent an image.
	 * @return <code>true</code> if the specified element represents an image.
	 */
	public static boolean isImageElement(final String xhtmlNamespaceURI, final Element element) {
		if(element != null) { //if a valid element was passed
			final String namespaceURI = element.getNamespaceURI(); //get the element's namespace URI
			//if this element is in the correct namespace
			if(Objects.equals(xhtmlNamespaceURI, namespaceURI)) {
				//TODO fix				final String elementName=element.getLocalName();  //get the element's name
				final String elementName = element.getLocalName(); //get the element's name
				if(elementName.equals(ELEMENT_IMG)) //if this is an <img> element
					return true; //show that this is an image object
				else if(elementName.equals(ELEMENT_OBJECT)) { //if this is an <object> element
					//see if there is a type attribute
					if(element.hasAttributeNS(null, ELEMENT_OBJECT_ATTRIBUTE_TYPE)) { //if there is a type attribute
						final String type = element.getAttributeNS(null, ELEMENT_OBJECT_ATTRIBUTE_TYPE); //get the type
						final ContentType mediaType = ContentType.create(type); //create a media type from the given type
						if(mediaType.getPrimaryType().equals(ContentType.IMAGE_PRIMARY_TYPE)) //if this is an image
							return true; //show that this is an image object
					}
					//see if there is a data attribute, since there is no type specified
					if(element.hasAttributeNS(null, ELEMENT_OBJECT_ATTRIBUTE_DATA)) { //if there is a data attribute
						final String data = element.getAttributeNS(null, ELEMENT_OBJECT_ATTRIBUTE_DATA); //get the data
						final ContentType mediaType = Files.getContentType(new File(data)); //try to get a media type from the file
						if(mediaType != null && mediaType.getPrimaryType().equals(ContentType.IMAGE_PRIMARY_TYPE)) //if this is an image
							return true; //show that this is an image object
					}
				}
			}
		}
		return false; //this does not appear to be an image element
	}

	/**
	 * Determines if the specified element represents a link. Specifically, this returns <code>true</code> if the element's name is "a".
	 * @param xhtmlNamespaceURI The XHTML namespace.
	 * @param element The element which might represent a link.
	 * @return <code>true</code> if the specified element represents a link.
	 */
	public static boolean isLinkElement(final String xhtmlNamespaceURI, final Element element) { //TODO fix to call XLink and see if this is an XLink link
		if(element != null) { //if a valid element was passed
			final String namespaceURI = element.getNamespaceURI(); //get the element's namespace URI
			//if this element is in the correct namespace
			if(Objects.equals(xhtmlNamespaceURI, namespaceURI)) {
				final String elementName = element.getLocalName(); //get the element's name
				if(elementName.equals(ELEMENT_A)) //if this is an <a> element
					return true; //show that this is a link element
			}
		}
		return false; //this does not appear to be a link element
	}

	/**
	 * Determines if the specified element represents an empty element&mdash;an element that might be declared as <code>EMPTY</code> in a DTD.
	 * @param namespaceURI The element namespace.
	 * @param localName The local name of the element.
	 * @return <code>true</code> if the specified element should be empty.
	 */
	public static boolean isEmptyElement(final URI namespaceURI, final String localName) {
		//TODO make this static---placed here because it was causing a compile error due to an Eclipse bug
		final String[] EMPTY_ELEMENT_LOCAL_NAMES = new String[] { ELEMENT_BASE, ELEMENT_META, ELEMENT_LINK, ELEMENT_HR, ELEMENT_BR, ELEMENT_PARAM, ELEMENT_IMG,
				ELEMENT_AREA, ELEMENT_INPUT, ELEMENT_COL }; //TODO probably put these in a better place, either in a hash set for faster lookup or put this constant in XHTMLConstants
		if(localName != null && (namespaceURI == null || isHTMLNamespaceURI(namespaceURI))) { //if this element has an HTML namespace or no namespace
			return Arrays.indexOf(EMPTY_ELEMENT_LOCAL_NAMES, localName) >= 0; //return whether the local name is one of the empty element local names
		}
		return false; //this does not appear to be an empty element
	}

	/**
	 * Determines if the specified element represents a link, and if so sets the link element's reference.
	 * @param xhtmlNamespaceURI The XHTML namespace.
	 * @param element The element which represents a link.
	 * @param href The new reference to add to the link.
	 */
	public static void setLinkElementHRef(final String xhtmlNamespaceURI, final Element element, final String href) { //TODO fix to call XLink and see if this is an XLink link
		if(element != null) { //if a valid element was passed
			final String namespaceURI = element.getNamespaceURI(); //get the element's namespace URI
			//if this element is in the correct namespace
			if(Objects.equals(xhtmlNamespaceURI, namespaceURI)) {
				final String elementName = element.getLocalName(); //get the element's name
				if(elementName.equals(ELEMENT_A)) { //if this is an <a> element
					element.setAttributeNS(null, ELEMENT_A_ATTRIBUTE_HREF, href); //set the href attribute TODO what if they were using an attribute prefix?
				}
			}
		}
	}

	/**
	 * Retrieves the text of the node contained in child nodes of type {@link Node#TEXT_NODE}, extracting text deeply.
	 * <p>
	 * This HTML-specific version adds whitespace to separate block elements.
	 * </p>
	 * @param node The node from which text will be retrieved.
	 * @return The data of all <code>Text</code> descendant nodes, which may be the empty string.
	 * @see XML#getText(Node, Set)
	 * @see #BLOCK_ELEMENTS
	 */
	public static String getText(final Node node) {
		return XML.getText(node, BLOCK_ELEMENTS);
	}

	/**
	 * Retrieves the text of the node contained in child nodes of type <code>Node.Text</code>. If <code>deep</code> is set to <code>true</code> the text of all
	 * descendant nodes in document (depth-first) order; otherwise, only text of direct children will be returned.
	 * <p>
	 * This HTML-specific version adds whitespace to separate block elements.
	 * </p>
	 * @param node The node from which text will be retrieved.
	 * @param deep Whether text of all descendants in document order will be returned.
	 * @param stringBuilder The buffer to which text will be added.
	 * @see XML#getText(Node, Set, boolean, StringBuilder)
	 * @see #BLOCK_ELEMENTS
	 */
	public static void getText(final Node node, final StringBuilder stringBuilder) {
		XML.getText(node, BLOCK_ELEMENTS, true, stringBuilder);
	}

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
