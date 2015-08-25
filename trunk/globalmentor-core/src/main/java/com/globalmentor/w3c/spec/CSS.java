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

package com.globalmentor.w3c.spec;

import java.util.*;
import java.util.regex.Pattern;

import org.w3c.dom.css.*;

import com.globalmentor.java.Characters;
import com.globalmentor.net.ContentType;

import static com.globalmentor.java.Characters.*;
import static java.util.Collections.*;

/**
 * Constants defining the W3C Cascading Style Sheets (CSS) specification.
 * 
 * @author Garret Wilson
 * @see <a href="http://www.w3.org/Style/CSS/">Cascading Style Sheets</a>
 */
public class CSS {

	/** The name extension for CSS stylesheets. */
	public static final String CSS_NAME_EXTENSION = "css";

	/** A Cascading Style Sheet document MIME subtype. */
	public static final String CSS_SUBTYPE = "css";

	/** The content type for CSS: <code>text/css</code>. */
	public static final ContentType TEXT_CSS_CONTENT_TYPE = ContentType.create(ContentType.TEXT_PRIMARY_TYPE, CSS_SUBTYPE);

	/** A space character. */
	public static final char SPACE_CHAR = ' ';
	/** A tab character. */
	public static final char TAB_CHAR = '\t';
	/** A carriage return character. */
	public static final char CR_CHAR = '\r';
	/** A linefeed character. */
	public static final char LF_CHAR = '\n';
	/** A formfeed character. */
	public static final char FF_CHAR = '\f';
	/** An equals sign. */
	public static final char EQUAL_CHAR = '=';
	/** A single quotation character. */
	public static final char SINGLE_QUOTE_CHAR = '\'';
	/** A double quotation character. */
	public static final char DOUBLE_QUOTE_CHAR = '"';
	/** The characters considered by CSS to be digits. */
	public static final Characters DIGIT_CHARS = Characters.range('0', '9'); //digits are the digits 0-9
	/** The decimal character that separates a number from its fraction. */
	public static final char DECIMAL_CHAR = '.';
	/** The characters considered by CSS to be part of a number. */
	public static final Characters NUMBER_CHARS = DIGIT_CHARS.add(DECIMAL_CHAR); //numbers are composed of digits and an optional decimal
	/** The characters considered by CSS to be whitespace. */
	public static final String WHITESPACE_CHARS = "" + SPACE_CHAR + TAB_CHAR + CR_CHAR + LF_CHAR + FF_CHAR; //whitespace characters in CSS are space, tab, CR, LF, and FF
	/** The characters considered by CSS to be whitespace. */
	public static final Pattern WHITESPACE_PATTERN = Pattern.compile("[\\u0020\\0009\\u000A\\u000C\\u000D]"); //whitespace characters in CSS are space, tab, CR, LF, and FF
	/** The characters considered by CSS to be combinators. */
	public static final String COMBINATOR_CHARS = WHITESPACE_CHARS + GREATER_THAN_CHAR + PLUS_SIGN_CHAR + TILDE_CHAR; //combinators are white space, "greater-than sign" (U+003E, >), "plus sign" (U+002B, +) and "tilde" (U+007E, ~)

	/** The starting character of an at-rule: @{ident}. */
	public static final String AT_RULE_START = "@";
	/** The starting characters of an XML comment; CSS ignores this string, but not its contents. */
	public static final String CDO = "<!--";
	/** The ending characters of an XML comment; CSS ignores this string, but not its contents. */
	public static final String CDC = "-->";
	/** The starting characters of CSS comment. */
	public static final String COMMENT_START = "/*";
	/** The ending characters of a CSS comment. */
	public static final String COMMENT_END = "*/";
	/** The character used to separate declarations in a ruleset. */
	public static final char DECLARATION_SEPARATOR_CHAR = ';';
	/** The string representing a font face at-rule. */
	public static final String FONT_FACE_RULE_SYMBOL = "@font-face";
	/** The string representing an import at-rule. */
	public static final String IMPORT_RULE_SYMBOL = "@import";
	/** The string representing a media at-rule. */
	public static final String MEDIA_RULE_SYMBOL = "@media";
	/** The string representing a page at-rule. */
	public static final String PAGE_RULE_SYMBOL = "@page";
	/** The character used to divide a property from its value in a declaration. */
	public static final char PROPERTY_DIVIDER_CHAR = ':';
	/** The character used to begin a group of style rules. */
	public static final char RULE_GROUP_START_CHAR = '{';
	/** The character used to end a group of style rules. */
	public static final char RULE_GROUP_END_CHAR = '}';
	/** The character used to separate selectors. */
	public static final char SELECTOR_SEPARATOR_CHAR = ',';
	/** The character used to begin an RGB number in the form #RGB or #RRGGBB. */
	public static final char RGB_NUMBER_CHAR = '#';
	/** The delimiter used to introduce a class simple selector. */
	public static final char CLASS_SELECTOR_DELIMITER = '.';
	/** The delimiter used to introduce an ID simple selector. */
	public static final char ID_SELECTOR_DELIMITER = '#';
	/** The delimiter used to introduce a pseudo class simple selector. */
	public static final char PSEUDO_CLASS_DELIMITER = ':';
	/** The character used to separate items in a list. */
	public static final char LIST_DELIMITER_CHAR = ',';

	//CSS units
	//relative units
	/** The "font-size" of the relevant font. */
	public static final String EM_UNITS = "em";
	/** The "x-height" of the relevant font. */
	public static final String EX_UNITS = "ex";
	/** Pixels, relative to the viewing device. */
	public static final String PX_UNITS = "px";
	//absolute units
	/** Inches---1 inch is equal to 2.54 centimeters. */
	public static final String IN_UNITS = "in";
	/** Centimeters. */
	public static final String CM_UNITS = "cm";
	/** Millimeters. */
	public static final String MM_UNITS = "mm";
	/** Points---the points used by CSS2 are equal to 1/72th of an inch. */
	public static final String PT_UNITS = "pt";
	/** Picas---1 pica is equal to 12 points. */
	public static final String PC_UNITS = "pc";

	//general CSS properties
	public static final String CSS_VALUE_AUTO = "auto";
	public static final String CSS_VALUE_INHERIT = "inherit";

	//Property names for CSS2
	public static final String CSS_PROP_AZIMUTH = "azimuth";
	public static final String CSS_PROP_BACKGROUND = "background";
	public static final String CSS_PROP_BACKGROUND_ATTACHMENT = "background-attachment";
	public static final String CSS_PROP_BACKGROUND_COLOR = "background-color";
	public static final String CSS_PROP_BACKGROUND_IMAGE = "background-image";
	public static final String CSS_PROP_BACKGROUND_POSITION = "background-position";
	public static final String CSS_PROP_BACKGROUND_REPEAT = "background-repeat";
	public static final String CSS_PROP_BORDER = "border";
	public static final String CSS_PROP_BORDER_COLLAPSE = "border-collapse";
	public static final String CSS_PROP_BORDER_COLOR = "border-color";
	public static final String CSS_PROP_BORDER_SPACING = "border-spacing";
	public static final String CSS_PROP_BORDER_STYLE = "border-style";
	public static final String CSS_PROP_BORDER_TOP = "border-top";
	public static final String CSS_PROP_BORDER_RIGHT = "border-right";
	public static final String CSS_PROP_BORDER_BOTTOM = "border-bottom";
	public static final String CSS_PROP_BORDER_LEFT = "border-left";
	public static final String CSS_PROP_BORDER_TOP_COLOR = "border-top-color";
	public static final String CSS_PROP_BORDER_RIGHT_COLOR = "border-right-color";
	public static final String CSS_PROP_BORDER_BOTTOM_COLOR = "border-bottom-color";
	public static final String CSS_PROP_BORDER_LEFT_COLOR = "border-left-color";
	public static final String CSS_PROP_BORDER_TOP_STYLE = "border-top-style";
	public static final String CSS_PROP_BORDER_RIGHT_STYLE = "border-right-style";
	public static final String CSS_PROP_BORDER_BOTTOM_STYLE = "border-bottom-style";
	public static final String CSS_PROP_BORDER_LEFT_STYLE = "border-left-style";
	public static final String CSS_PROP_BORDER_TOP_WIDTH = "border-top-width";
	public static final String CSS_PROP_BORDER_RIGHT_WIDTH = "border-right-width";
	public static final String CSS_PROP_BORDER_BOTTOM_WIDTH = "border-bottom-width";
	public static final String CSS_PROP_BORDER_LEFT_WIDTH = "border-left-width";
	public static final String CSS_PROP_BORDER_WIDTH = "border-width";
	public static final String CSS_PROP_BOTTOM = "bottom";
	public static final String CSS_PROP_CAPTION = "caption-side";
	public static final String CSS_PROP_CLEAR = "clear";
	public static final String CSS_PROP_CLIP = "clip";
	public static final String CSS_PROP_COLOR = "color";
	public static final String CSS_PROP_CONTENT = "content";
	public static final String CSS_PROP_COUNTER_INCREMENT = "counter-increment";
	public static final String CSS_PROP_COUNTER_RESET = "counter-reset";
	public static final String CSS_PROP_CUE = "cue";
	public static final String CSS_PROP_CUE_AFTER = "cue-after";
	public static final String CSS_PROP_CUE_BEFORE = "cue-before";
	public static final String CSS_PROP_CURSOR = "cursor";
	public static final String CSS_PROP_DIRECTION = "direction";
	public static final String CSS_PROP_DISPLAY = "display";
	public static final String CSS_PROP_ELEVATION = "elevation";
	public static final String CSS_PROP_EMPTY_CELLS = "empty-cells";
	public static final String CSS_PROP_FLOAT = "float";
	public static final String CSS_PROP_FONT = "font";
	public static final String CSS_PROP_FONT_FAMILY = "font-family";
	public static final String CSS_PROP_FONT_SIZE = "font-size";
	public static final String CSS_PROP_FONT_SIZE_ADJUST = "font-size-adjust";
	public static final String CSS_PROP_FONT_STRETCH = "font-stretch";
	public static final String CSS_PROP_FONT_STYLE = "font-style";
	public static final String CSS_PROP_FONT_VARIANT = "font-variant";
	public static final String CSS_PROP_FONT_WEIGHT = "font-weight";
	public static final String CSS_PROP_HEIGHT = "height";
	public static final String CSS_PROP_LEFT = "left";
	public static final String CSS_PROP_LETTER_SPACING = "letter-spacing";
	public static final String CSS_PROP_LINE_HEIGHT = "line-height";
	public static final String CSS_PROP_LIST_STYLE = "list-style";
	public static final String CSS_PROP_LIST_STYLE_IMAGE = "list-style-image";
	public static final String CSS_PROP_LIST_STYLE_POSITION = "list-style-position";
	public static final String CSS_PROP_LIST_STYLE_TYPE = "list-style-type";
	public static final String CSS_PROP_MARGIN = "margin";
	public static final String CSS_PROP_MARGIN_TOP = "margin-top";
	public static final String CSS_PROP_MARGIN_RIGHT = "margin-right";
	public static final String CSS_PROP_MARGIN_BOTTOM = "margin-bottom";
	public static final String CSS_PROP_MARGIN_LEFT = "margin-left";
	public static final String CSS_PROP_MARKET_OFFSET = "marker-offset";
	public static final String CSS_PROP_MAX_HEIGHT = "max-height";
	public static final String CSS_PROP_MAX_WIDTH = "max-width";
	public static final String CSS_PROP_MIN_HEIGHT = "min-height";
	public static final String CSS_PROP_MIN_WIDTH = "min-width";
	public static final String CSS_PROP_OPACITY = "opacity";
	public static final String CSS_PROP_ORPHANS = "orphans";
	public static final String CSS_PROP_OUTLINE = "outline";
	public static final String CSS_PROP_OUTLINE_COLOR = "outline-color";
	public static final String CSS_PROP_OUTLINE_STYLE = "outline-style";
	public static final String CSS_PROP_OUTLINE_WIDTH = "outline-width";
	public static final String CSS_PROP_OVERFLOW = "overflow";
	public static final String CSS_PROP_PADDING = "padding";
	public static final String CSS_PROP_PADDING_TOP = "padding-top";
	public static final String CSS_PROP_PADDING_RIGHT = "padding-right";
	public static final String CSS_PROP_PADDING_BOTTOM = "padding-bottom";
	public static final String CSS_PROP_PADDING_LEFT = "padding-left";
	public static final String CSS_PROP_PAGE = "page";
	public static final String CSS_PROP_PAGE_BREAK_AFTER = "page-break-after";
	public static final String CSS_PROP_PAGE_BREAK_BEFORE = "page-break-before";
	public static final String CSS_PROP_PAGE_BREAK_INSIDE = "page-break-inside";
	public static final String CSS_PROP_PAUSE = "pause";
	public static final String CSS_PROP_PAUSE_AFTER = "pause-after";
	public static final String CSS_PROP_PAUSE_BEFORE = "pause-before";
	public static final String CSS_PROP_PITCH = "pitch";
	public static final String CSS_PROP_PITCH_NUMER = "pitch-range";
	public static final String CSS_PROP_PLAY_DURING = "play-during";
	public static final String CSS_PROP_POSITION = "position";
	public static final String CSS_PROP_QUOTES = "quotes";
	public static final String CSS_PROP_RICHNESS = "richness";
	public static final String CSS_PROP_RIGHT = "right";
	public static final String CSS_PROP_SPEAK = "speak";
	public static final String CSS_PROP_SPEAK_HEADER = "speak-header";
	public static final String CSS_PROP_SPEAK_NUMERAL = "speak-numeral";
	public static final String CSS_PROP_SPEAK_PUNCTUATION = "speak-punctuation";
	public static final String CSS_PROP_SPEECH_RATE = "speech-rate";
	public static final String CSS_PROP_STRESS = "stress";
	public static final String CSS_PROP_TABLE_LAYOUT = "table-layout";
	public static final String CSS_PROP_TEXT_ALIGN = "text-align ident";
	public static final String CSS_PROP_TEXT_DECORATION = "text-decoration";
	public static final String CSS_PROP_TEXT_INDENT = "text-indent";
	public static final String CSS_PROP_TEXT_SHADOW = "text-shadow";
	public static final String CSS_PROP_TEXT_TRANSFORM = "text-transform";
	public static final String CSS_PROP_TOP = "top";
	public static final String CSS_PROP_UNICODE_BIDI = "unicode-bidi";
	public static final String CSS_PROP_VERTICAL_ALIGN = "vertical-align";
	public static final String CSS_PROP_VISIBILITY = "visibility";
	public static final String CSS_PROP_VOICE_FAMILY = "voice-family";
	public static final String CSS_PROP_VOLUME = "volume";
	public static final String CSS_PROP_WHITE_SPACE = "white-space";
	public static final String CSS_PROP_WIDOES = "widows";
	public static final String CSS_PROP_WIDTH = "width";
	public static final String CSS_PROP_WORD_SPACING = "word-spacing";
	public static final String CSS_PROP_Z_INDEX = "z-index";

	/**
	 * The name used in a list to represent that there is an empty list. Used, for example, for text-decoration.
	 */
	//TODO del probably;	public static final String CSS_LIST_NONE="none";

	//TODO add other properties here

	/** The IE filter property. */
	public static final String CSS_PROP_FILTER = "filter";

	//properties <code>border-color</code> and <code>border-XXX-color</code>
	public static final String CSS_BORDER_COLOR_TRANSPARENT = "transparent";

	//properties <code>border-collapse</code>
	public static final String CSS_BORDER_COLLAPSE_COLLAPSE = "collapse";
	public static final String CSS_BORDER_COLLAPSE_SEPARATE = "separate";

	//properties <code>border-style</code> and <code>border-XXX-style</code>
	public static final String CSS_BORDER_STYLE_NONE = "none";
	public static final String CSS_BORDER_STYLE_HIDDEN = "hidden";
	public static final String CSS_BORDER_STYLE_DOTTED = "dotted";
	public static final String CSS_BORDER_STYLE_DASHED = "dashed";
	public static final String CSS_BORDER_STYLE_SOLID = "solid";
	public static final String CSS_BORDER_STYLE_DOUBLE = "double";
	public static final String CSS_BORDER_STYLE_GROOVE = "groove";
	public static final String CSS_BORDER_STYLE_RIDGE = "ridge";
	public static final String CSS_BORDER_STYLE_INSET = "inset";
	public static final String CSS_BORDER_STYLE_OUTSET = "outset";

	//properties <code>border-width</code> and <code>border-XXX-width</code>
	public static final String CSS_BORDER_WIDTH_THIN = "thin";
	public static final String CSS_BORDER_WIDTH_MEDIUM = "medium";
	public static final String CSS_BORDER_WIDTH_THICK = "thick";

	//property <code>color<code>
	public static final String CSS_COLOR_TRANSPARENT = "transparent";
	//colors as defined by HTML; used with several properties, such as background-color
	public static final String CSS_COLOR_BLACK = "black";
	public static final int CSS_COLOR_BLACK_VALUE = 0x000000;
	public static final String CSS_COLOR_GREEN = "green";
	public static final int CSS_COLOR_GREEN_VALUE = 0x008000;
	public static final String CSS_COLOR_SILVER = "silver";
	public static final int CSS_COLOR_SILVER_VALUE = 0xC0C0C0;
	public static final String CSS_COLOR_LIME = "lime";
	public static final int CSS_COLOR_LIME_VALUE = 0x00FF00;
	public static final String CSS_COLOR_GRAY = "gray";
	public static final int CSS_COLOR_GRAY_VALUE = 0x808080;
	public static final String CSS_COLOR_OLIVE = "olive";
	public static final int CSS_COLOR_OLIVE_VALUE = 0x808000;
	public static final String CSS_COLOR_WHITE = "white";
	public static final int CSS_COLOR_WHITE_VALUE = 0xFFFFFF;
	public static final String CSS_COLOR_YELLOW = "yellow";
	public static final int CSS_COLOR_YELLOW_VALUE = 0xFFFF00;
	public static final String CSS_COLOR_MAROON = "maroon";
	public static final int CSS_COLOR_MAROON_VALUE = 0x800000;
	public static final String CSS_COLOR_NAVY = "navy";
	public static final int CSS_COLOR_NAVY_VALUE = 0x000080;
	public static final String CSS_COLOR_RED = "red";
	public static final int CSS_COLOR_RED_VALUE = 0xFF0000;
	public static final String CSS_COLOR_BLUE = "blue";
	public static final int CSS_COLOR_BLUE_VALUE = 0x0000FF;
	public static final String CSS_COLOR_PURPLE = "purple";
	public static final int CSS_COLOR_PURPLE_VALUE = 0x800080;
	public static final String CSS_COLOR_TEAL = "teal";
	public static final int CSS_COLOR_TEAL_VALUE = 0x008080;
	public static final String CSS_COLOR_FUCHSIA = "fuchsia";
	public static final int CSS_COLOR_FUCHSIA_VALUE = 0xFF00FF;
	public static final String CSS_COLOR_AQUA = "aqua";
	public static final int CSS_COLOR_AQUA_VALUE = 0x00FFFF;

	/** The standard CSS color names, in lowercase, and their associated values. */
	public static final Map<String, Integer> CSS_COLOR_NAME_VALUES;

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
	public static final String CSS_CLEAR_BOTH = "both";
	public static final String CSS_CLEAR_LEFT = "left";
	public static final String CSS_CLEAR_NONE = "none";
	public static final String CSS_CLEAR_RIGHT = "right";

	//Property <code>display</code>
	public static final String CSS_DISPLAY_BLOCK = "block";
	public static final String CSS_DISPLAY_INLINE = "inline";
	public static final String CSS_DISPLAY_INLINE_BLOCK = "inline-block";
	public static final String CSS_DISPLAY_LIST_ITEM = "list-item";
	public static final String CSS_DISPLAY_RUN_IN = "run-in";
	public static final String CSS_DISPLAY_COMPACT = "compact";
	public static final String CSS_DISPLAY_MARKER = "marker";
	public static final String CSS_DISPLAY_TABLE = "table";
	public static final String CSS_DISPLAY_INLINE_TABLE = "inline-table";
	public static final String CSS_DISPLAY_TABLE_ROW_GROUP = "table-row-group";
	public static final String CSS_DISPLAY_TABLE_HEADER_GROUP = "table-header-group";
	public static final String CSS_DISPLAY_TABLE_FOOTER_GROUP = "table-footer-group";
	public static final String CSS_DISPLAY_TABLE_ROW = "table-row";
	public static final String CSS_DISPLAY_TABLE_COLUMN_GROUP = "table-column-group";
	public static final String CSS_DISPLAY_TABLE_CELL = "table-cell";
	public static final String CSS_DISPLAY_TABLE_CAPTION = "table-caption";
	public static final String CSS_DISPLAY_NONE = "none";

	//Property <code>float</code>
	public static final String CSS_FLOAT_LEFT = "left";
	public static final String CSS_FLOAT_NONE = "none";
	public static final String CSS_FLOAT_RIGHT = "right";

	//Property "font-family"
	public static final String CSS_FONT_FAMILY_SERIF = "serif";
	public static final String CSS_FONT_FAMILY_SANS_SERIF = "sans-serif";
	public static final String CSS_FONT_FAMILY_CURSIVE = "cursive";
	public static final String CSS_FONT_FAMILY_FANTASY = "fantasy";
	public static final String CSS_FONT_FAMILY_MONOSPACE = "monospace";
	/** The CSS2 recommended scaling factor between absolute font size strings. */
	public static final float FONT_SIZE_SCALING_FACTOR = 1.2f;

	//Property "font-size"
	public static final String CSS_FONT_SIZE_XX_SMALL = "xx-small";
	public static final String CSS_FONT_SIZE_X_SMALL = "x-small";
	public static final String CSS_FONT_SIZE_SMALL = "small";
	public static final String CSS_FONT_SIZE_MEDIUM = "medium";
	public static final String CSS_FONT_SIZE_LARGE = "large";
	public static final String CSS_FONT_SIZE_X_LARGE = "x-large";
	public static final String CSS_FONT_SIZE_XX_LARGE = "xx-large";
	public static final String CSS_FONT_SIZE_SMALLER = "smaller";
	public static final String CSS_FONT_SIZE_LARGER = "larger";

	//Property "font-style"
	public static final String CSS_FONT_STYLE_NORMAL = "normal";
	public static final String CSS_FONT_STYLE_ITALIC = "italic";
	public static final String CSS_FONT_STYLE_OBLIQUE = "oblique";

	//Property "font-weight"
	public static final String CSS_FONT_WEIGHT_NORMAL = "normal";
	public static final String CSS_FONT_WEIGHT_BOLD = "bold";
	public static final String CSS_FONT_WEIGHT_BOLDER = "bolder";
	public static final String CSS_FONT_WEIGHT_LIGHTER = "lighter";

	//Property "line-height"
	public static final String CSS_LINE_HEIGHT_NORMAL = "normal";

	//Property <code>list-style-type</code>
	public static final String CSS_LIST_STYLE_TYPE_DISC = "disc";
	public static final String CSS_LIST_STYLE_TYPE_CIRCLE = "circle";
	public static final String CSS_LIST_STYLE_TYPE_SQUARE = "square";
	public static final String CSS_LIST_STYLE_TYPE_DECIMAL = "decimal";
	public static final String CSS_LIST_STYLE_TYPE_DECIMAL_LEADING_ZERO = "decimal-leading-zero";
	public static final String CSS_LIST_STYLE_TYPE_LOWER_ROMAN = "lower-roman";
	public static final String CSS_LIST_STYLE_TYPE_UPPER_ROMAN = "upper-roman";
	public static final String CSS_LIST_STYLE_TYPE_LOWER_GREEK = "lower-greek";
	public static final String CSS_LIST_STYLE_TYPE_LOWER_ALPHA = "lower-alpha";
	public static final String CSS_LIST_STYLE_TYPE_LOWER_LATIN = "lower-latin";
	public static final String CSS_LIST_STYLE_TYPE_UPPER_ALPHA = "upper-alpha";
	public static final String CSS_LIST_STYLE_TYPE_UPPER_LATIN = "upper-latin";
	public static final String CSS_LIST_STYLE_TYPE_HEBREW = "hebrew";
	public static final String CSS_LIST_STYLE_TYPE_ARMENIAN = "armenian";
	public static final String CSS_LIST_STYLE_TYPE_GEORGIAN = "georgian";
	public static final String CSS_LIST_STYLE_TYPE_CJK_IDEOGRAPHIC = "cjk-ideographic";
	public static final String CSS_LIST_STYLE_TYPE_HIRAGANA = "hiragana";
	public static final String CSS_LIST_STYLE_TYPE_KATAKANA = "katakana";
	public static final String CSS_LIST_STYLE_TYPE_HIRAGANA_IROHA = "hiragana-iroha";
	public static final String CSS_LIST_STYLE_TYPE_KATAKANA_IROHA = "katakana-iroha";
	public static final String CSS_LIST_STYLE_TYPE_NONE = "none";

	//property <code>overflow</code>
	public static final String CSS_OVERFLOW_VISIBLE = "visible";
	public static final String CSS_OVERFLOW_HIDDEN = "hidden";
	public static final String CSS_OVERFLOW_SCROLL = "scroll";
	public static final String CSS_OVERFLOW_AUTO = "auto";

	//property <code>page-break-after</code>
	public static final String CSS_PAGE_BREAK_AFTER_AUTO = "auto";
	public static final String CSS_PAGE_BREAK_AFTER_ALWAYS = "always";
	public static final String CSS_PAGE_BREAK_AFTER_AVOID = "avoid";
	public static final String CSS_PAGE_BREAK_AFTER_LEFT = "left";
	public static final String CSS_PAGE_BREAK_AFTER_RIGHT = "right";
	public static final String CSS_PAGE_BREAK_AFTER_INHERIT = "inherit";

	//property <code>page-break-before</code>
	public static final String CSS_PAGE_BREAK_BEFORE_AUTO = "auto";
	public static final String CSS_PAGE_BREAK_BEFORE_ALWAYS = "always";
	public static final String CSS_PAGE_BREAK_BEFORE_AVOID = "avoid";
	public static final String CSS_PAGE_BREAK_BEFORE_LEFT = "left";
	public static final String CSS_PAGE_BREAK_BEFORE_RIGHT = "right";
	public static final String CSS_PAGE_BREAK_BEFORE_INHERIT = "inherit";

	//**property <code>position</code>
	public static final String CSS_POSITION_ABSOLUTE = "absolute";
	public static final String CSS_POSITION_FIXED = "fixed";
	public static final String CSS_POSITION_RELATIVE = "relative";

	//**property <code>table-layout</code>
	public static final String CSS_TABLE_LAYOUT_AUTO = "auto";
	public static final String CSS_TABLE_LAYOUT_FIXED = "fixed";

	//property <code>text-decoration</code>
	public static final String CSS_TEXT_DECORATION_NONE = "none";
	public static final String CSS_TEXT_DECORATION_UNDERLINE = "underline";
	public static final String CSS_TEXT_DECORATION_OVERLINE = "overline";
	public static final String CSS_TEXT_DECORATION_LINE_THROUGH = "line-through";
	public static final String CSS_TEXT_DECORATION_BLINK = "blink";
	public static final String CSS_TEXT_DECORATION_INHERIT = "inherit";

	//property <code>text-transform</code>
	public static final String CSS_TEXT_TRANSFORM_CAPITALIZE = "capitalize";
	public static final String CSS_TEXT_TRANSFORM_UPPERCASE = "uppercase";
	public static final String CSS_TEXT_TRANSFORM_LOWERCASE = "lowercase";
	public static final String CSS_TEXT_TRANSFORM_NONE = "none";
	public static final String CSS_TEXT_TRANSFORM_INHERIT = "inherit";

	//property <code>vertical-align</code>
	public static final String CSS_VERTICAL_ALIGN_BASELINE = "baseline";
	public static final String CSS_VERTICAL_ALIGN_SUB = "sub";
	public static final String CSS_VERTICAL_ALIGN_SUPER = "super";
	public static final String CSS_VERTICAL_ALIGN_TOP = "top";
	public static final String CSS_VERTICAL_ALIGN_TEXT_TOP = "text-top";
	public static final String CSS_VERTICAL_ALIGN_MIDDLE = "middle";
	public static final String CSS_VERTICAL_ALIGN_BOTTOM = "bottom";
	public static final String CSS_VERTICAL_ALIGN_TEXT_BOTTOM = "text-bottom";
	public static final String CSS_VERTICAL_ALIGN_INHERIT = "inherit";

	//property <code>visibility</code>
	public static final String CSS_VISIBILITY_COLLAPSE = "collapse";
	public static final String CSS_VISIBILITY_HIDDEN = "hidden";
	public static final String CSS_VISIBILITY_INHERIT = "inherit";
	public static final String CSS_VISIBILITY_VISIBLE = "visible";

	/**
	 * Returns a string to display on a marker for the given list item.
	 * @param listStyleType The type of list marker to use, one of the <code>CSS_LIST_STYLE_TYPE_</code> constants.
	 * @param listItemIndex The index of the list item for which text should be generated
	 * @return The string to be rendered for the given list item, or <code>null</code> if a string rendering is not appropriate for the given list style type.
	 */
	public static String getMarkerString(final String listStyleType, final int listItemIndex) { //TODO maybe make sure none of these wrap around
		if(CSS_LIST_STYLE_TYPE_DECIMAL.equals(listStyleType)) //decimal
			return String.valueOf(1 + listItemIndex); //return the ordinal position as a decimal number
		//TODO fix	public static final String CSS_LIST_STYLE_TYPE_DECIMAL_LEADING_ZERO="decimal-leading-zero";
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
		//TODO fix	public static final String CSS_LIST_STYLE_TYPE_UPPER_ROMAN="upper-roman";
		//TODO fix	public static final String CSS_LIST_STYLE_TYPE_LOWER_GREEK="lower-greek";
		else if(CSS_LIST_STYLE_TYPE_LOWER_GREEK.equals(listStyleType)) //lower-greek
			return String.valueOf((char)('\u03B1' + listItemIndex)); //return the correct lowercase greek character as a string
		else if(CSS_LIST_STYLE_TYPE_LOWER_ALPHA.equals(listStyleType) //lower-alpha
				|| CSS_LIST_STYLE_TYPE_LOWER_LATIN.equals(listStyleType)) //lower-latin
			return String.valueOf((char)('a' + listItemIndex)); //return the correct lowercase character as a string
		else if(CSS_LIST_STYLE_TYPE_UPPER_ALPHA.equals(listStyleType) //upper-alpha
				|| CSS_LIST_STYLE_TYPE_UPPER_LATIN.equals(listStyleType)) //upper-latin
			return String.valueOf((char)('A' + listItemIndex)); //return the correct lowercase character as a string
		//TODO fix	public static final String CSS_LIST_STYLE_TYPE_HEBREW="hebrew";
		//TODO fix	public static final String CSS_LIST_STYLE_TYPE_ARMENIAN="armenian";
		//TODO fix	public static final String CSS_LIST_STYLE_TYPE_GEORGIAN="georgian";
		//TODO fix	public static final String CSS_LIST_STYLE_TYPE_CJK_IDEOGRAPHIC="cjk-ideographic";
		//TODO fix	public static final String CSS_LIST_STYLE_TYPE_HIRAGANA="hiragana";
		//TODO fix	public static final String CSS_LIST_STYLE_TYPE_KATAKANA="katakana";
		//TODO fix	public static final String CSS_LIST_STYLE_TYPE_HIRAGANA_IROHA="hiragana-iroha";
		//TODO fix	public static final String CSS_LIST_STYLE_TYPE_KATAKANA_IROHA="katakana-iroha";
		//TODO fix	public static final String CSS_LIST_STYLE_TYPE_NONE="none";
		return null; //show that we couldn't find an appropriate market string
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
			final String displayString = cssStyle.getPropertyValue(CSS_PROP_DISPLAY); //get the display property
			return displayString.length() == 0 || displayString.equals(CSS_DISPLAY_INLINE); //return true if there is no display string or it is equal to "inline"
		}
		return true; //if there is no style, we assume inline
	}

}
