/*
 * Copyright © 1996-2011 GlobalMentor, Inc. <http://www.globalmentor.com/>
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
import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;

import org.w3c.dom.*;
import org.w3c.dom.css.*;

import com.globalmentor.io.*;
import com.globalmentor.java.Characters;
import com.globalmentor.log.Log;
import com.globalmentor.net.ContentType;

import com.globalmentor.text.xml.XMLNode;

import static com.globalmentor.java.Characters.*;
import static java.util.Collections.unmodifiableMap;

/** Constants and utilities for CSS. */
public class XMLCSS {

	/** The name extension for CSS stylesheets. */
	public final static String CSS_NAME_EXTENSION = "css";

	/** A Cascading Style Sheet document MIME subtype. */
	public final static String CSS_SUBTYPE = "css";

	/** The content type for CSS: <code>text/css</code>. */
	public static final ContentType TEXT_CSS_CONTENT_TYPE = ContentType.create(ContentType.TEXT_PRIMARY_TYPE, CSS_SUBTYPE);

	/** A space character. */
	public final static char SPACE_CHAR = ' ';
	/** A tab character. */
	public final static char TAB_CHAR = '\t';
	/** A carriage return character. */
	public final static char CR_CHAR = '\r';
	/** A linefeed character. */
	public final static char LF_CHAR = '\n';
	/** A formfeed character. */
	public final static char FF_CHAR = '\f';
	/** An equals sign. */
	public final static char EQUAL_CHAR = '=';
	/** A single quotation character. */
	public final static char SINGLE_QUOTE_CHAR = '\'';
	/** A double quotation character. */
	public final static char DOUBLE_QUOTE_CHAR = '"';
	/** The characters considered by CSS to be digits. */
	public final static Characters DIGIT_CHARS = Characters.range('0', '9'); //digits are the digits 0-9
	/** The decimal character that separates a number from its fraction. */
	public final static char DECIMAL_CHAR = '.';
	/** The characters considered by CSS to be part of a number. */
	public final static Characters NUMBER_CHARS = DIGIT_CHARS.add(DECIMAL_CHAR); //numbers are composed of digits and an optional decimal
	/** The characters considered by CSS to be whitespace. */
	public final static String WHITESPACE_CHARS = "" + SPACE_CHAR + TAB_CHAR + CR_CHAR + LF_CHAR + FF_CHAR; //whitespace characters in CSS are space, tab, CR, LF, and FF
	/** The characters considered by CSS to be whitespace. */
	public final static Pattern WHITESPACE_PATTERN = Pattern.compile("[\\u0020\\0009\\u000A\\u000C\\u000D]"); //whitespace characters in CSS are space, tab, CR, LF, and FF
	/** The characters considered by CSS to be combinators. */
	public final static String COMBINATOR_CHARS = WHITESPACE_CHARS + GREATER_THAN_CHAR + PLUS_SIGN_CHAR + TILDE_CHAR; //combinators are white space, "greater-than sign" (U+003E, >), "plus sign" (U+002B, +) and "tilde" (U+007E, ~)

	/** The starting character of an at-rule: @{ident}. */
	public final static String AT_RULE_START = "@";
	/** The starting characters of an XML comment; CSS ignores this string, but not its contents. */
	public final static String CDO = "<!--";
	/** The ending characters of an XML comment; CSS ignores this string, but not its contents. */
	public final static String CDC = "-->";
	/** The starting characters of CSS comment. */
	public final static String COMMENT_START = "/*";
	/** The ending characters of a CSS comment. */
	public final static String COMMENT_END = "*/";
	/** The character used to separate declarations in a ruleset. */
	public final static char DECLARATION_SEPARATOR_CHAR = ';';
	/** The string representing a font face at-rule. */
	public final static String FONT_FACE_RULE_SYMBOL = "@font-face";
	/** The string representing an import at-rule. */
	public final static String IMPORT_RULE_SYMBOL = "@import";
	/** The string representing a media at-rule. */
	public final static String MEDIA_RULE_SYMBOL = "@media";
	/** The string representing a page at-rule. */
	public final static String PAGE_RULE_SYMBOL = "@page";
	/** The character used to divide a property from its value in a declaration. */
	public final static char PROPERTY_DIVIDER_CHAR = ':';
	/** The character used to begin a group of style rules. */
	public final static char RULE_GROUP_START_CHAR = '{';
	/** The character used to end a group of style rules. */
	public final static char RULE_GROUP_END_CHAR = '}';
	/** The character used to separate selectors. */
	public final static char SELECTOR_SEPARATOR_CHAR = ',';
	/** The character used to begin an RGB number in the form #RGB or #RRGGBB. */
	public final static char RGB_NUMBER_CHAR = '#';
	/** The delimiter used to introduce a class simple selector. */
	public final static char CLASS_SELECTOR_DELIMITER = '.';
	/** The delimiter used to introduce an ID simple selector. */
	public final static char ID_SELECTOR_DELIMITER = '#';
	/** The delimiter used to introduce a pseudo class simple selector. */
	public final static char PSEUDO_CLASS_DELIMITER = ':';
	/** The character used to separate items in a list. */
	public final static char LIST_DELIMITER_CHAR = ',';

	//CSS units
	//relative units
	/** The "font-size" of the relevant font. */
	public final static String EM_UNITS = "em";
	/** The "x-height" of the relevant font. */
	public final static String EX_UNITS = "ex";
	/** Pixels, relative to the viewing device. */
	public final static String PX_UNITS = "px";
	//absolute units
	/** Inches---1 inch is equal to 2.54 centimeters. */
	public final static String IN_UNITS = "in";
	/** Centimeters. */
	public final static String CM_UNITS = "cm";
	/** Millimeters. */
	public final static String MM_UNITS = "mm";
	/** Points---the points used by CSS2 are equal to 1/72th of an inch. */
	public final static String PT_UNITS = "pt";
	/** Picas---1 pica is equal to 12 points. */
	public final static String PC_UNITS = "pc";

	//general CSS properties
	public final static String CSS_VALUE_AUTO = "auto";
	public final static String CSS_VALUE_INHERIT = "inherit";

	//Property names for CSS2
	public final static String CSS_PROP_AZIMUTH = "azimuth";
	public final static String CSS_PROP_BACKGROUND = "background";
	public final static String CSS_PROP_BACKGROUND_ATTACHMENT = "background-attachment";
	public final static String CSS_PROP_BACKGROUND_COLOR = "background-color";
	public final static String CSS_PROP_BACKGROUND_IMAGE = "background-image";
	public final static String CSS_PROP_BACKGROUND_POSITION = "background-position";
	public final static String CSS_PROP_BACKGROUND_REPEAT = "background-repeat";
	public final static String CSS_PROP_BORDER = "border";
	public final static String CSS_PROP_BORDER_COLLAPSE = "border-collapse";
	public final static String CSS_PROP_BORDER_COLOR = "border-color";
	public final static String CSS_PROP_BORDER_SPACING = "border-spacing";
	public final static String CSS_PROP_BORDER_STYLE = "border-style";
	public final static String CSS_PROP_BORDER_TOP = "border-top";
	public final static String CSS_PROP_BORDER_RIGHT = "border-right";
	public final static String CSS_PROP_BORDER_BOTTOM = "border-bottom";
	public final static String CSS_PROP_BORDER_LEFT = "border-left";
	public final static String CSS_PROP_BORDER_TOP_COLOR = "border-top-color";
	public final static String CSS_PROP_BORDER_RIGHT_COLOR = "border-right-color";
	public final static String CSS_PROP_BORDER_BOTTOM_COLOR = "border-bottom-color";
	public final static String CSS_PROP_BORDER_LEFT_COLOR = "border-left-color";
	public final static String CSS_PROP_BORDER_TOP_STYLE = "border-top-style";
	public final static String CSS_PROP_BORDER_RIGHT_STYLE = "border-right-style";
	public final static String CSS_PROP_BORDER_BOTTOM_STYLE = "border-bottom-style";
	public final static String CSS_PROP_BORDER_LEFT_STYLE = "border-left-style";
	public final static String CSS_PROP_BORDER_TOP_WIDTH = "border-top-width";
	public final static String CSS_PROP_BORDER_RIGHT_WIDTH = "border-right-width";
	public final static String CSS_PROP_BORDER_BOTTOM_WIDTH = "border-bottom-width";
	public final static String CSS_PROP_BORDER_LEFT_WIDTH = "border-left-width";
	public final static String CSS_PROP_BORDER_WIDTH = "border-width";
	public final static String CSS_PROP_BOTTOM = "bottom";
	public final static String CSS_PROP_CAPTION = "caption-side";
	public final static String CSS_PROP_CLEAR = "clear";
	public final static String CSS_PROP_CLIP = "clip";
	public final static String CSS_PROP_COLOR = "color";
	public final static String CSS_PROP_CONTENT = "content";
	public final static String CSS_PROP_COUNTER_INCREMENT = "counter-increment";
	public final static String CSS_PROP_COUNTER_RESET = "counter-reset";
	public final static String CSS_PROP_CUE = "cue";
	public final static String CSS_PROP_CUE_AFTER = "cue-after";
	public final static String CSS_PROP_CUE_BEFORE = "cue-before";
	public final static String CSS_PROP_CURSOR = "cursor";
	public final static String CSS_PROP_DIRECTION = "direction";
	public final static String CSS_PROP_DISPLAY = "display";
	public final static String CSS_PROP_ELEVATION = "elevation";
	public final static String CSS_PROP_EMPTY_CELLS = "empty-cells";
	public final static String CSS_PROP_FLOAT = "float";
	public final static String CSS_PROP_FONT = "font";
	public final static String CSS_PROP_FONT_FAMILY = "font-family";
	public final static String CSS_PROP_FONT_SIZE = "font-size";
	public final static String CSS_PROP_FONT_SIZE_ADJUST = "font-size-adjust";
	public final static String CSS_PROP_FONT_STRETCH = "font-stretch";
	public final static String CSS_PROP_FONT_STYLE = "font-style";
	public final static String CSS_PROP_FONT_VARIANT = "font-variant";
	public final static String CSS_PROP_FONT_WEIGHT = "font-weight";
	public final static String CSS_PROP_HEIGHT = "height";
	public final static String CSS_PROP_LEFT = "left";
	public final static String CSS_PROP_LETTER_SPACING = "letter-spacing";
	public final static String CSS_PROP_LINE_HEIGHT = "line-height";
	public final static String CSS_PROP_LIST_STYLE = "list-style";
	public final static String CSS_PROP_LIST_STYLE_IMAGE = "list-style-image";
	public final static String CSS_PROP_LIST_STYLE_POSITION = "list-style-position";
	public final static String CSS_PROP_LIST_STYLE_TYPE = "list-style-type";
	public final static String CSS_PROP_MARGIN = "margin";
	public final static String CSS_PROP_MARGIN_TOP = "margin-top";
	public final static String CSS_PROP_MARGIN_RIGHT = "margin-right";
	public final static String CSS_PROP_MARGIN_BOTTOM = "margin-bottom";
	public final static String CSS_PROP_MARGIN_LEFT = "margin-left";
	public final static String CSS_PROP_MARKET_OFFSET = "marker-offset";
	public final static String CSS_PROP_MAX_HEIGHT = "max-height";
	public final static String CSS_PROP_MAX_WIDTH = "max-width";
	public final static String CSS_PROP_MIN_HEIGHT = "min-height";
	public final static String CSS_PROP_MIN_WIDTH = "min-width";
	public final static String CSS_PROP_OPACITY = "opacity";
	public final static String CSS_PROP_ORPHANS = "orphans";
	public final static String CSS_PROP_OUTLINE = "outline";
	public final static String CSS_PROP_OUTLINE_COLOR = "outline-color";
	public final static String CSS_PROP_OUTLINE_STYLE = "outline-style";
	public final static String CSS_PROP_OUTLINE_WIDTH = "outline-width";
	public final static String CSS_PROP_OVERFLOW = "overflow";
	public final static String CSS_PROP_PADDING = "padding";
	public final static String CSS_PROP_PADDING_TOP = "padding-top";
	public final static String CSS_PROP_PADDING_RIGHT = "padding-right";
	public final static String CSS_PROP_PADDING_BOTTOM = "padding-bottom";
	public final static String CSS_PROP_PADDING_LEFT = "padding-left";
	public final static String CSS_PROP_PAGE = "page";
	public final static String CSS_PROP_PAGE_BREAK_AFTER = "page-break-after";
	public final static String CSS_PROP_PAGE_BREAK_BEFORE = "page-break-before";
	public final static String CSS_PROP_PAGE_BREAK_INSIDE = "page-break-inside";
	public final static String CSS_PROP_PAUSE = "pause";
	public final static String CSS_PROP_PAUSE_AFTER = "pause-after";
	public final static String CSS_PROP_PAUSE_BEFORE = "pause-before";
	public final static String CSS_PROP_PITCH = "pitch";
	public final static String CSS_PROP_PITCH_NUMER = "pitch-range";
	public final static String CSS_PROP_PLAY_DURING = "play-during";
	public final static String CSS_PROP_POSITION = "position";
	public final static String CSS_PROP_QUOTES = "quotes";
	public final static String CSS_PROP_RICHNESS = "richness";
	public final static String CSS_PROP_RIGHT = "right";
	public final static String CSS_PROP_SPEAK = "speak";
	public final static String CSS_PROP_SPEAK_HEADER = "speak-header";
	public final static String CSS_PROP_SPEAK_NUMERAL = "speak-numeral";
	public final static String CSS_PROP_SPEAK_PUNCTUATION = "speak-punctuation";
	public final static String CSS_PROP_SPEECH_RATE = "speech-rate";
	public final static String CSS_PROP_STRESS = "stress";
	public final static String CSS_PROP_TABLE_LAYOUT = "table-layout";
	public final static String CSS_PROP_TEXT_ALIGN = "text-align ident";
	public final static String CSS_PROP_TEXT_DECORATION = "text-decoration";
	public final static String CSS_PROP_TEXT_INDENT = "text-indent";
	public final static String CSS_PROP_TEXT_SHADOW = "text-shadow";
	public final static String CSS_PROP_TEXT_TRANSFORM = "text-transform";
	public final static String CSS_PROP_TOP = "top";
	public final static String CSS_PROP_UNICODE_BIDI = "unicode-bidi";
	public final static String CSS_PROP_VERTICAL_ALIGN = "vertical-align";
	public final static String CSS_PROP_VISIBILITY = "visibility";
	public final static String CSS_PROP_VOICE_FAMILY = "voice-family";
	public final static String CSS_PROP_VOLUME = "volume";
	public final static String CSS_PROP_WHITE_SPACE = "white-space";
	public final static String CSS_PROP_WIDOES = "widows";
	public final static String CSS_PROP_WIDTH = "width";
	public final static String CSS_PROP_WORD_SPACING = "word-spacing";
	public final static String CSS_PROP_Z_INDEX = "z-index";

	/**
	 * The name used in a list to represent that there is an empty list. Used, for example, for text-decoration.
	 */
	//G***del probably;	public final static String CSS_LIST_NONE="none";

	//G***add other properties here

	/** The IE filter property. */
	public final static String CSS_PROP_FILTER = "filter";

	//properties <code>border-color</code> and <code>border-XXX-color</code>
	public final static String CSS_BORDER_COLOR_TRANSPARENT = "transparent";

	//properties <code>border-collapse</code>
	public final static String CSS_BORDER_COLLAPSE_COLLAPSE = "collapse";
	public final static String CSS_BORDER_COLLAPSE_SEPARATE = "separate";

	//properties <code>border-style</code> and <code>border-XXX-style</code>
	public final static String CSS_BORDER_STYLE_NONE = "none";
	public final static String CSS_BORDER_STYLE_HIDDEN = "hidden";
	public final static String CSS_BORDER_STYLE_DOTTED = "dotted";
	public final static String CSS_BORDER_STYLE_DASHED = "dashed";
	public final static String CSS_BORDER_STYLE_SOLID = "solid";
	public final static String CSS_BORDER_STYLE_DOUBLE = "double";
	public final static String CSS_BORDER_STYLE_GROOVE = "groove";
	public final static String CSS_BORDER_STYLE_RIDGE = "ridge";
	public final static String CSS_BORDER_STYLE_INSET = "inset";
	public final static String CSS_BORDER_STYLE_OUTSET = "outset";

	//properties <code>border-width</code> and <code>border-XXX-width</code>
	public final static String CSS_BORDER_WIDTH_THIN = "thin";
	public final static String CSS_BORDER_WIDTH_MEDIUM = "medium";
	public final static String CSS_BORDER_WIDTH_THICK = "thick";

	//property <code>color<code>
	public final static String CSS_COLOR_TRANSPARENT = "transparent";
	//colors as defined by HTML; used with several properties, such as background-color
	public final static String CSS_COLOR_BLACK = "black";
	public final static int CSS_COLOR_BLACK_VALUE = 0x000000;
	public final static String CSS_COLOR_GREEN = "green";
	public final static int CSS_COLOR_GREEN_VALUE = 0x008000;
	public final static String CSS_COLOR_SILVER = "silver";
	public final static int CSS_COLOR_SILVER_VALUE = 0xC0C0C0;
	public final static String CSS_COLOR_LIME = "lime";
	public final static int CSS_COLOR_LIME_VALUE = 0x00FF00;
	public final static String CSS_COLOR_GRAY = "gray";
	public final static int CSS_COLOR_GRAY_VALUE = 0x808080;
	public final static String CSS_COLOR_OLIVE = "olive";
	public final static int CSS_COLOR_OLIVE_VALUE = 0x808000;
	public final static String CSS_COLOR_WHITE = "white";
	public final static int CSS_COLOR_WHITE_VALUE = 0xFFFFFF;
	public final static String CSS_COLOR_YELLOW = "yellow";
	public final static int CSS_COLOR_YELLOW_VALUE = 0xFFFF00;
	public final static String CSS_COLOR_MAROON = "maroon";
	public final static int CSS_COLOR_MAROON_VALUE = 0x800000;
	public final static String CSS_COLOR_NAVY = "navy";
	public final static int CSS_COLOR_NAVY_VALUE = 0x000080;
	public final static String CSS_COLOR_RED = "red";
	public final static int CSS_COLOR_RED_VALUE = 0xFF0000;
	public final static String CSS_COLOR_BLUE = "blue";
	public final static int CSS_COLOR_BLUE_VALUE = 0x0000FF;
	public final static String CSS_COLOR_PURPLE = "purple";
	public final static int CSS_COLOR_PURPLE_VALUE = 0x800080;
	public final static String CSS_COLOR_TEAL = "teal";
	public final static int CSS_COLOR_TEAL_VALUE = 0x008080;
	public final static String CSS_COLOR_FUCHSIA = "fuchsia";
	public final static int CSS_COLOR_FUCHSIA_VALUE = 0xFF00FF;
	public final static String CSS_COLOR_AQUA = "aqua";
	public final static int CSS_COLOR_AQUA_VALUE = 0x00FFFF;

	/** The standard CSS color names, in lowercase, and their associated values. */
	public final static Map<String, Integer> CSS_COLOR_NAME_VALUES;

	static {
		final Map<String, Integer> cssColorNameValues = new HashMap<String, Integer>();
		cssColorNameValues.put(CSS_COLOR_BLACK, Integer.valueOf(CSS_COLOR_BLACK_VALUE));
		cssColorNameValues.put(CSS_COLOR_GREEN, Integer.valueOf(CSS_COLOR_GREEN_VALUE));
		cssColorNameValues.put(CSS_COLOR_SILVER, Integer.valueOf(CSS_COLOR_SILVER_VALUE));
		cssColorNameValues.put(CSS_COLOR_LIME, Integer.valueOf(CSS_COLOR_LIME_VALUE));
		cssColorNameValues.put(CSS_COLOR_GRAY, Integer.valueOf(CSS_COLOR_GRAY_VALUE));
		cssColorNameValues.put(CSS_COLOR_OLIVE, Integer.valueOf(CSS_COLOR_OLIVE_VALUE));
		cssColorNameValues.put(CSS_COLOR_WHITE, Integer.valueOf(CSS_COLOR_WHITE_VALUE));
		cssColorNameValues.put(CSS_COLOR_YELLOW, Integer.valueOf(CSS_COLOR_YELLOW_VALUE));
		cssColorNameValues.put(CSS_COLOR_MAROON, Integer.valueOf(CSS_COLOR_MAROON_VALUE));
		cssColorNameValues.put(CSS_COLOR_NAVY, Integer.valueOf(CSS_COLOR_NAVY_VALUE));
		cssColorNameValues.put(CSS_COLOR_RED, Integer.valueOf(CSS_COLOR_RED_VALUE));
		cssColorNameValues.put(CSS_COLOR_BLUE, Integer.valueOf(CSS_COLOR_BLUE_VALUE));
		cssColorNameValues.put(CSS_COLOR_PURPLE, Integer.valueOf(CSS_COLOR_PURPLE_VALUE));
		cssColorNameValues.put(CSS_COLOR_TEAL, Integer.valueOf(CSS_COLOR_TEAL_VALUE));
		cssColorNameValues.put(CSS_COLOR_FUCHSIA, Integer.valueOf(CSS_COLOR_FUCHSIA_VALUE));
		cssColorNameValues.put(CSS_COLOR_AQUA, Integer.valueOf(CSS_COLOR_AQUA_VALUE));
		CSS_COLOR_NAME_VALUES = unmodifiableMap(cssColorNameValues);
	}

	//TODO add other properties here

	//Property <code>clear</code>
	public final static String CSS_CLEAR_BOTH = "both";
	public final static String CSS_CLEAR_LEFT = "left";
	public final static String CSS_CLEAR_NONE = "none";
	public final static String CSS_CLEAR_RIGHT = "right";

	//Property <code>display</code>
	public final static String CSS_DISPLAY_BLOCK = "block";
	public final static String CSS_DISPLAY_INLINE = "inline";
	public final static String CSS_DISPLAY_INLINE_BLOCK = "inline-block";
	public final static String CSS_DISPLAY_LIST_ITEM = "list-item";
	public final static String CSS_DISPLAY_RUN_IN = "run-in";
	public final static String CSS_DISPLAY_COMPACT = "compact";
	public final static String CSS_DISPLAY_MARKER = "marker";
	public final static String CSS_DISPLAY_TABLE = "table";
	public final static String CSS_DISPLAY_INLINE_TABLE = "inline-table";
	public final static String CSS_DISPLAY_TABLE_ROW_GROUP = "table-row-group";
	public final static String CSS_DISPLAY_TABLE_HEADER_GROUP = "table-header-group";
	public final static String CSS_DISPLAY_TABLE_FOOTER_GROUP = "table-footer-group";
	public final static String CSS_DISPLAY_TABLE_ROW = "table-row";
	public final static String CSS_DISPLAY_TABLE_COLUMN_GROUP = "table-column-group";
	public final static String CSS_DISPLAY_TABLE_CELL = "table-cell";
	public final static String CSS_DISPLAY_TABLE_CAPTION = "table-caption";
	public final static String CSS_DISPLAY_NONE = "none";

	//Property <code>float</code>
	public final static String CSS_FLOAT_LEFT = "left";
	public final static String CSS_FLOAT_NONE = "none";
	public final static String CSS_FLOAT_RIGHT = "right";

	//Property "font-family"
	public final static String CSS_FONT_FAMILY_SERIF = "serif";
	public final static String CSS_FONT_FAMILY_SANS_SERIF = "sans-serif";
	public final static String CSS_FONT_FAMILY_CURSIVE = "cursive";
	public final static String CSS_FONT_FAMILY_FANTASY = "fantasy";
	public final static String CSS_FONT_FAMILY_MONOSPACE = "monospace";
	/** The CSS2 recommended scaling factor between absolute font size strings. */
	public final static float FONT_SIZE_SCALING_FACTOR = 1.2f;

	//Property "font-size"
	public final static String CSS_FONT_SIZE_XX_SMALL = "xx-small";
	public final static String CSS_FONT_SIZE_X_SMALL = "x-small";
	public final static String CSS_FONT_SIZE_SMALL = "small";
	public final static String CSS_FONT_SIZE_MEDIUM = "medium";
	public final static String CSS_FONT_SIZE_LARGE = "large";
	public final static String CSS_FONT_SIZE_X_LARGE = "x-large";
	public final static String CSS_FONT_SIZE_XX_LARGE = "xx-large";
	public final static String CSS_FONT_SIZE_SMALLER = "smaller";
	public final static String CSS_FONT_SIZE_LARGER = "larger";

	//Property "font-style"
	public final static String CSS_FONT_STYLE_NORMAL = "normal";
	public final static String CSS_FONT_STYLE_ITALIC = "italic";
	public final static String CSS_FONT_STYLE_OBLIQUE = "oblique";

	//Property "font-weight"
	public final static String CSS_FONT_WEIGHT_NORMAL = "normal";
	public final static String CSS_FONT_WEIGHT_BOLD = "bold";
	public final static String CSS_FONT_WEIGHT_BOLDER = "bolder";
	public final static String CSS_FONT_WEIGHT_LIGHTER = "lighter";

	//Property "line-height"
	public final static String CSS_LINE_HEIGHT_NORMAL = "normal";

	//Property <code>list-style-type</code>
	public final static String CSS_LIST_STYLE_TYPE_DISC = "disc";
	public final static String CSS_LIST_STYLE_TYPE_CIRCLE = "circle";
	public final static String CSS_LIST_STYLE_TYPE_SQUARE = "square";
	public final static String CSS_LIST_STYLE_TYPE_DECIMAL = "decimal";
	public final static String CSS_LIST_STYLE_TYPE_DECIMAL_LEADING_ZERO = "decimal-leading-zero";
	public final static String CSS_LIST_STYLE_TYPE_LOWER_ROMAN = "lower-roman";
	public final static String CSS_LIST_STYLE_TYPE_UPPER_ROMAN = "upper-roman";
	public final static String CSS_LIST_STYLE_TYPE_LOWER_GREEK = "lower-greek";
	public final static String CSS_LIST_STYLE_TYPE_LOWER_ALPHA = "lower-alpha";
	public final static String CSS_LIST_STYLE_TYPE_LOWER_LATIN = "lower-latin";
	public final static String CSS_LIST_STYLE_TYPE_UPPER_ALPHA = "upper-alpha";
	public final static String CSS_LIST_STYLE_TYPE_UPPER_LATIN = "upper-latin";
	public final static String CSS_LIST_STYLE_TYPE_HEBREW = "hebrew";
	public final static String CSS_LIST_STYLE_TYPE_ARMENIAN = "armenian";
	public final static String CSS_LIST_STYLE_TYPE_GEORGIAN = "georgian";
	public final static String CSS_LIST_STYLE_TYPE_CJK_IDEOGRAPHIC = "cjk-ideographic";
	public final static String CSS_LIST_STYLE_TYPE_HIRAGANA = "hiragana";
	public final static String CSS_LIST_STYLE_TYPE_KATAKANA = "katakana";
	public final static String CSS_LIST_STYLE_TYPE_HIRAGANA_IROHA = "hiragana-iroha";
	public final static String CSS_LIST_STYLE_TYPE_KATAKANA_IROHA = "katakana-iroha";
	public final static String CSS_LIST_STYLE_TYPE_NONE = "none";

	//property <code>overflow</code>
	public final static String CSS_OVERFLOW_VISIBLE = "visible";
	public final static String CSS_OVERFLOW_HIDDEN = "hidden";
	public final static String CSS_OVERFLOW_SCROLL = "scroll";
	public final static String CSS_OVERFLOW_AUTO = "auto";

	//property <code>page-break-after</code>
	public final static String CSS_PAGE_BREAK_AFTER_AUTO = "auto";
	public final static String CSS_PAGE_BREAK_AFTER_ALWAYS = "always";
	public final static String CSS_PAGE_BREAK_AFTER_AVOID = "avoid";
	public final static String CSS_PAGE_BREAK_AFTER_LEFT = "left";
	public final static String CSS_PAGE_BREAK_AFTER_RIGHT = "right";
	public final static String CSS_PAGE_BREAK_AFTER_INHERIT = "inherit";

	//property <code>page-break-before</code>
	public final static String CSS_PAGE_BREAK_BEFORE_AUTO = "auto";
	public final static String CSS_PAGE_BREAK_BEFORE_ALWAYS = "always";
	public final static String CSS_PAGE_BREAK_BEFORE_AVOID = "avoid";
	public final static String CSS_PAGE_BREAK_BEFORE_LEFT = "left";
	public final static String CSS_PAGE_BREAK_BEFORE_RIGHT = "right";
	public final static String CSS_PAGE_BREAK_BEFORE_INHERIT = "inherit";

	//**property <code>position</code>
	public final static String CSS_POSITION_ABSOLUTE = "absolute";
	public final static String CSS_POSITION_FIXED = "fixed";
	public final static String CSS_POSITION_RELATIVE = "relative";

	//**property <code>table-layout</code>
	public final static String CSS_TABLE_LAYOUT_AUTO = "auto";
	public final static String CSS_TABLE_LAYOUT_FIXED = "fixed";

	//property <code>text-decoration</code>
	public final static String CSS_TEXT_DECORATION_NONE = "none";
	public final static String CSS_TEXT_DECORATION_UNDERLINE = "underline";
	public final static String CSS_TEXT_DECORATION_OVERLINE = "overline";
	public final static String CSS_TEXT_DECORATION_LINE_THROUGH = "line-through";
	public final static String CSS_TEXT_DECORATION_BLINK = "blink";
	public final static String CSS_TEXT_DECORATION_INHERIT = "inherit";

	//property <code>text-transform</code>
	public final static String CSS_TEXT_TRANSFORM_CAPITALIZE = "capitalize";
	public final static String CSS_TEXT_TRANSFORM_UPPERCASE = "uppercase";
	public final static String CSS_TEXT_TRANSFORM_LOWERCASE = "lowercase";
	public final static String CSS_TEXT_TRANSFORM_NONE = "none";
	public final static String CSS_TEXT_TRANSFORM_INHERIT = "inherit";

	//property <code>vertical-align</code>
	public final static String CSS_VERTICAL_ALIGN_BASELINE = "baseline";
	public final static String CSS_VERTICAL_ALIGN_SUB = "sub";
	public final static String CSS_VERTICAL_ALIGN_SUPER = "super";
	public final static String CSS_VERTICAL_ALIGN_TOP = "top";
	public final static String CSS_VERTICAL_ALIGN_TEXT_TOP = "text-top";
	public final static String CSS_VERTICAL_ALIGN_MIDDLE = "middle";
	public final static String CSS_VERTICAL_ALIGN_BOTTOM = "bottom";
	public final static String CSS_VERTICAL_ALIGN_TEXT_BOTTOM = "text-bottom";
	public final static String CSS_VERTICAL_ALIGN_INHERIT = "inherit";

	//property <code>visibility</code>
	public final static String CSS_VISIBILITY_COLLAPSE = "collapse";
	public final static String CSS_VISIBILITY_HIDDEN = "hidden";
	public final static String CSS_VISIBILITY_INHERIT = "inherit";
	public final static String CSS_VISIBILITY_VISIBLE = "visible";

	/** The default text indent. */
	//TODO should this go elsewhere?
	private final static int DEFAULT_TEXT_INDENT = 0;

	/**
	 * Gets the CSS value object of a particular CSS property.
	 * @param styleManager The object that allows style lookups for elements.
	 * @param element The element with which style is associated.
	 * @param propertyName The name of the CSS property to search for.
	 * @param resolve Whether the element's parent hierarchy should be searched to find this CSS value if not found associated with this element.
	 * @return The CSS value object for the given property, or <code>null</code> if that property cannot be found.
	 */
	public static CSSValue getCSSPropertyCSSValue(final CSSStyleManager styleManager, final Element element, final String propertyName, final boolean resolve) {
		final CSSStyleDeclaration cssStyle = styleManager.getStyle(element); //get the CSS style associated with the element
		if(cssStyle != null) { //if there is a CSS style declaration object associated with the element
			final CSSValue cssValue = cssStyle.getPropertyCSSValue(propertyName); //get the property as a CSSValue object
			if(cssValue != null) { //if the style contains the given CSS property
				return cssValue; //return the value
			}
		}
		if(resolve) { //since the property isn't associated with this element, see if we should resolve up the chain; if so
			final Node parentNode = element.getParentNode(); //get the parent to use to resolve
			if(parentNode != null && parentNode.getNodeType() == Node.ELEMENT_NODE) //if we have a resolve parent element
				return getCSSPropertyCSSValue(styleManager, (Element)parentNode, propertyName, resolve); //try to get the value from the resolving parent
		}
		return null; //show that we couldn't find the CSS property value anywhere
	}

	/**
	 * Returns a string to display on a marker for the given list item.
	 * @param listStyleType The type of list marker to use, one of the <code>XMLCSS.CSS_LIST_STYLE_TYPE_</code> constants.
	 * @param listItemIndex The index of the list item for which text should be generated
	 * @return The string to be rendered for the given list item, or <code>null</code> if a string rendering is not appropriate for the given list style type.
	 */
	public static String getMarkerString(final String listStyleType, final int listItemIndex) { //TODO maybe make sure none of these wrap around
		if(CSS_LIST_STYLE_TYPE_DECIMAL.equals(listStyleType)) //decimal
			return String.valueOf(1 + listItemIndex); //return the ordinal position as a decimal number
		//TODO fix	public final static String CSS_LIST_STYLE_TYPE_DECIMAL_LEADING_ZERO="decimal-leading-zero";
		else if(CSS_LIST_STYLE_TYPE_LOWER_ROMAN.equals(listStyleType)) { //lower-roman
			switch(listItemIndex) { //see which index this is TODO fix with a better algorithm
				case 0:
					return "i"; //TODO fix
				case 1:
					return "ii"; //TODO fix
				case 2:
					return "iii"; //TODO fix
				case 3:
					return "iv"; //TODO fix
				case 4:
					return "v"; //TODO fix
				case 5:
					return "vi"; //TODO fix
				case 6:
					return "vii"; //TODO fix
				case 7:
					return "viii"; //TODO fix
				case 8:
					return "ix"; //TODO fix
				case 9:
					return "x"; //TODO fix
				default:
					return ""; //TODO fix
			}
		}
		//TODO fix	public final static String CSS_LIST_STYLE_TYPE_UPPER_ROMAN="upper-roman";
		//TODO fix	public final static String CSS_LIST_STYLE_TYPE_LOWER_GREEK="lower-greek";
		else if(CSS_LIST_STYLE_TYPE_LOWER_GREEK.equals(listStyleType)) //lower-greek
			return String.valueOf((char)('\u03B1' + listItemIndex)); //return the correct lowercase greek character as a string
		else if(CSS_LIST_STYLE_TYPE_LOWER_ALPHA.equals(listStyleType) //lower-alpha
				|| CSS_LIST_STYLE_TYPE_LOWER_LATIN.equals(listStyleType)) //lower-latin
			return String.valueOf((char)('a' + listItemIndex)); //return the correct lowercase character as a string
		else if(CSS_LIST_STYLE_TYPE_UPPER_ALPHA.equals(listStyleType) //upper-alpha
				|| CSS_LIST_STYLE_TYPE_UPPER_LATIN.equals(listStyleType)) //upper-latin
			return String.valueOf((char)('A' + listItemIndex)); //return the correct lowercase character as a string
		//TODO fix	public final static String CSS_LIST_STYLE_TYPE_HEBREW="hebrew";
		//TODO fix	public final static String CSS_LIST_STYLE_TYPE_ARMENIAN="armenian";
		//TODO fix	public final static String CSS_LIST_STYLE_TYPE_GEORGIAN="georgian";
		//TODO fix	public final static String CSS_LIST_STYLE_TYPE_CJK_IDEOGRAPHIC="cjk-ideographic";
		//TODO fix	public final static String CSS_LIST_STYLE_TYPE_HIRAGANA="hiragana";
		//TODO fix	public final static String CSS_LIST_STYLE_TYPE_KATAKANA="katakana";
		//TODO fix	public final static String CSS_LIST_STYLE_TYPE_HIRAGANA_IROHA="hiragana-iroha";
		//TODO fix	public final static String CSS_LIST_STYLE_TYPE_KATAKANA_IROHA="katakana-iroha";
		//TODO fix	public final static String CSS_LIST_STYLE_TYPE_NONE="none";
		return null; //show that we couldn't find an appropriate market string
	}

	/**
	 * Loads a default stylesheet resource if one is available for the given XML namespace. A stylesheet will be found for the given namespace if it is stored as
	 * a file in the directory corresponding to this class package, with the same name as the namespace, with all invalid filename characters replaced with
	 * underscores, with an extension of "css".
	 * @param namespaceURI The namespace for which to locate a default stylsheet.
	 * @return A W3C CSS DOM tree representing the stylesheet, or <code>null</code> if no stylesheet was found or <code>namespaceURI</code> is <code>null</code>.
	 * @deprecated
	 */
	public static CSSStyleSheet getDefaultStyleSheet(final String namespaceURI) { //TODO add a URIInputStreamable argument which can get local input streams as well as external ones
		//TODO do we want to cache stylesheets? probably not
		if(namespaceURI != null) { //if a valid namespace was passed
			//convert the namespace URI to a valid filaname and add a "css" extension
			final String cssFilename = Files.encodeCrossPlatformFilename(namespaceURI) + Files.FILENAME_EXTENSION_SEPARATOR + CSS_NAME_EXTENSION;
			final URL styleSheetURL = XMLCSS.class.getResource(cssFilename); //see if we can load this resource locally
			if(styleSheetURL != null) { //if we were able to find a stylesheet stored as a resource
				final XMLCSSStyleSheet styleSheet = new XMLCSSStyleSheet((com.globalmentor.text.xml.XMLNode)null); //create a new CSS stylesheet that has no owner TODO make this cast use a generic Node, or make a default constructor
				try {
					//TODO XMLCSSProcessor has been updated -- see if we need to modify this code
					final InputStream inputStream = styleSheetURL.openConnection().getInputStream(); //open a connection to the URL and get an input stream from that
					final InputStreamReader inputStreamReader = new InputStreamReader(inputStream); //get an input stream reader to the stylesheet TODO what about encoding?
					final ParseReader styleSheetReader = new ParseReader(inputStreamReader, styleSheetURL); //create a parse reader reader to use to read the stylesheet
					try {
						//TODO fix			entityReader.setCurrentLineIndex(entity.getLineIndex());	//pretend we're reading where the entity was located in that file, so any errors will show the correct information
						//TODO fix			entityReader.setCurrentCharIndex(entity.getCharIndex());	//pretend we're reading where the entity was located in that file, so any errors will show the correct information
						final XMLCSSProcessor cssProcessor = new XMLCSSProcessor(); //create a new CSS processor
						cssProcessor.parseStyleSheetContent(styleSheetReader, styleSheet); //parse the stylesheet content
						return styleSheet; //return the stylesheet we parsed
					} finally {
						styleSheetReader.close(); //always close the stylesheet reader
					}
				} catch(IOException ioException) { //if anything goes wrong reading the stylesheet, that's bad but shouldn't keep the program from continuing
					Log.warn(ioException); //warn that there's was an IO problem
				}
			}
		}
		return null; //show that for some reason we couldn't load a default stylesheet for the given namespace
	}

	/**
	 * Gets a particular value from the style that should be a length, returning the length in pixels. TODO fix; right now it returns points
	 * @param styleManager The object that allows style lookups for elements.
	 * @param element The element with which style is associated.
	 * @param cssProperty The name of the CSS property for which a color value should be retrieved.
	 * @param resolve Whether the attribute set's parent hierarchy should be searched to find this CSS value if not found in this attribute set.
	 * @param defaultValue The default value to use if the property does not exist.
	 * @param font The font to be used when calculating relative lengths, such as <code>ems</code>.
	 * @return The length in pixels. //TODO what happens if the value is not a length? probably just return the default
	 */
	protected static float getPixelLength(final CSSStyleManager styleManager, final Element element, final String cssProperty, final boolean resolve,
			final float defaultValue/*TODO fix, final Font font*/) {
		final CSSPrimitiveValue primitiveValue = (CSSPrimitiveValue)getCSSPropertyCSSValue(styleManager, element, cssProperty, resolve); //get CSS value for this property, resolving up the hierarchy if we should
		if(primitiveValue != null) { //if we have a value
			switch(primitiveValue.getPrimitiveType()) { //see which type of primitive type we have
				case CSSPrimitiveValue.CSS_EMS: //if this is the ems property
					return primitiveValue.getFloatValue(CSSPrimitiveValue.CSS_EMS); //TODO fix; hack for just returning any value
					/*TODO fix
										if(font!=null)  //if we have a font
											return font.getSize()*primitiveValue.getFloatValue(XMLCSSPrimitiveValue.CSS_EMS);  //TODO fix; this probably isn't the same as the defined font size, which is what CSS calls for for EMS
										break;
					*/
				case CSSPrimitiveValue.CSS_PT: //if they want the size in points
					return primitiveValue.getFloatValue(primitiveValue.CSS_PT); //get the value in pixels and round to the nearest integer pixel length TODO fix to use pixels instead of points, as it does now
			}
		}
		return defaultValue; //if we couldn't determine the value, return the default value
	}

	/**
	 * Gets the CSS <code>margin-left</code> setting from the style in pixels. TODO actually, right now it returns the value in points; fix this
	 * @param styleManager The object that allows style lookups for elements.
	 * @param element The element with which style is associated.
	 * @param font The font to be used when calculating relative lengths, such as <code>ems</code>.
	 * @return The left margin size in pixels or, if the property is not specified, the default amount of 0.
	 */
	public static float getMarginLeft(final CSSStyleManager styleManager, final Element element/*TODO fix , final Font font*/) {
		return getPixelLength(styleManager, element, CSS_PROP_MARGIN_LEFT, false, 0/*G**fix, font*/); //return the length in pixels without resolving
	}

	/**
	 * Gets the CSS <code>text-indent</code> setting from the style in pixels. TODO actually, right now it returns the value in points; fix this
	 * @param styleManager The object that allows style lookups for elements.
	 * @param element The element with which style is associated.
	 * @return The text indent amount in pixels or, if text indent is not specified, the default amount, which is 0. //TODO testing font; comment
	 */
	public static float getTextIndent(final CSSStyleManager styleManager, final Element element/*TODO fix, final Font font*/) //TODO we'll probably need to pass a length here or something
	{
		final CSSPrimitiveValue textIndentValue = (CSSPrimitiveValue)getCSSPropertyCSSValue(styleManager, element, CSS_PROP_TEXT_INDENT, true); //get CSS value for this property, resolving up the hierarchy if necessary
		if(textIndentValue != null) { //if we have a value
			//TODO del if not needed			if(textIndentValue.isAbsoluteLength())	//if this is an absolute length
			{
				switch(textIndentValue.getPrimitiveType()) { //see which type of primitive type we have
				/*TODO fix
								  case CSSPrimitiveValue.CSS_EMS: //TODO testing
										return font.getSize()*textIndentValue.getFloatValue(XMLCSSPrimitiveValue.CSS_EMS);  //TODO fix; this probably isn't the same as the defined font size, which is what CSS calls for for EMS
				*/
				/*TODO fix
										{
					Log.trace("They want ems.");
													// As a practical matter, this FRC will almost always
													// be the right one.
													AffineTransform xf
															= GraphicsEnvironment.getLocalGraphicsEnvironment()
															.getDefaultScreenDevice().getDefaultConfiguration()
															.getDefaultTransform(); //TODO testing
											final FontRenderContext fontRenderContext=new FontRenderContext(xf, false, false);  //TODO we should really get the font render context from somewhere else; for now, this should get close
											final float emSize=(float)font.getStringBounds("m", fontRenderContext).getWidth(); //get the size of an em
						Log.trace("each em is: ", new Float(emSize)); //TODO del
											return emSize*textIndentValue.getFloatValue(XMLCSSPrimitiveValue.CSS_EMS);  //TODO testing
										}
				*/
					case XMLCSSPrimitiveValue.CSS_PT: //TODO testing
						return textIndentValue.getFloatValue(CSSPrimitiveValue.CSS_PT); //get the value in pixels and round to the nearest integer pixel length TODO fix to use pixels instead of points, as it does now
				}
			}
			//TODO del if not needed			else	//if this is not an absolute length, it could be a relative length or a percentage
			{
				/*TODO fix
									float percentageValue=1;	//if we find a percentage value, we'll store it here (we won't really store a percentage, but the actual float number; that is, percentage/100) (we're defaulting to 1 because the compiler will complain without some value, even though this always gets changed by the logic of the code)
									boolean isPercentage=;	//see if this is a percentage value
									if(isPercentage)	//if this is an explicit percentage value
										percentageValue=fontSizeValue.getFloatValue(XMLCSSPrimitiveValue.CSS_PERCENTAGE)/100;	//store the percentage value as a scaling value
										//if this isn't an explicit percentage value, it might be a relative value, an absolute string value, or a relative string value that is the same as a percentage
									if(textIndentValue.getPrimitiveType()==XMLCSSPrimitiveValue.CSS_PERCENTAGE) {	//if this is a percentage value
										final AttributeSet resolveParent=attributeSet.getResolveParent();	//get the parent to use to resolve this attribute
										if(resolveParent!=null)	//if we have a resolve parent
											return Math.round(percentageValue*getFontSize(resolveParent));	//multiply the percentage with the font size from the parent
										else	//if we don't have a resolve parent
											return Math.round(percentageValue*DEFAULT_FONT_SIZE);	//multiply the percentage with the default font size
									}
									//TODO add support for relative sizes (ems, exs, etc.) here: else
				*/
			}
		}
		return DEFAULT_TEXT_INDENT; //return the default value, since we couldn't find an alternative
	}

	/**
	 * Returns whether or not this style declaration has an inline display style. This is implemented to only return <code>true</code> if the display is
	 * specifically set to <code>CSS_DISPLAY_INLINE</code> or if there is no display property.
	 * @param cssStyle The CSS DOM syle declaration, which is expected to implement <code>CSS2Properties</code>, although this parameter may be <code>null</code>.
	 * @return <code>true</code> if this style declaration has an inline display style, has no display style, or the style declaration is <code>null</code>.
	 * @see CSS2Properties#getDisplay()
	 * @deprecated
	 */
	public static boolean isDisplayInline(final CSSStyleDeclaration cssStyle) {
		if(cssStyle != null) { //if a valid style is passed
		/*TODO fix when our XMLCSSStyleDeclaration implements CSS2Properties
					Debug.assert(cssStyle instanceof CSS2Properties, "DOM implementation does not support CSS2Properties interface for CSSStyleDeclaration"); //TODO do we want to take action if the style does not implement CSS2Properties?
					final CSS2Properties cssProperties=(CSS2Properties)cssStyle;  //get the CSS2Properties interface, which is expected to be implemented by the DOM CSSStyleDeclaration
		*/
			final XMLCSSStyleDeclaration cssProperties = (XMLCSSStyleDeclaration)cssStyle; //get the CSS2Properties interface, which is expected to be implemented by the DOM CSSStyleDeclaration TODO fix
			final String displayString = cssProperties.getDisplay(); //get the display property
			return displayString.length() == 0 || displayString.equals(CSS_DISPLAY_INLINE); //return true if there is no display string or it is equal to "inline"
		}
		return true; //if there is no style, we assume inline
	}

}
