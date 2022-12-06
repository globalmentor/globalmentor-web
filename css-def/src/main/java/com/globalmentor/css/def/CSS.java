/*
 * Copyright © 1996-2011 GlobalMentor, Inc. <https://www.globalmentor.com/>
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

package com.globalmentor.css.def;

import java.util.*;
import java.util.regex.Pattern;

import com.globalmentor.java.Characters;
import com.globalmentor.lex.Identifier;
import com.globalmentor.net.MediaType;

import static com.globalmentor.java.Characters.*;
import static java.util.Collections.*;

/**
 * Constants defining the W3C Cascading Style Sheets (CSS) specification.
 * 
 * @author Garret Wilson
 * @see <a href="http://www.w3.org/Style/CSS/">Cascading Style Sheets</a>
 */
public class CSS {

	/** The filename extension for CSS stylesheets. */
	public static final String FILENAME_EXTENSION = "css";

	/** A Cascading Style Sheet document MIME subtype. */
	public static final String CSS_SUBTYPE = "css";

	/** The media type for CSS: <code>text/css</code>. */
	public static final MediaType TEXT_CSS_MEDIA_TYPE = MediaType.of(MediaType.TEXT_PRIMARY_TYPE, CSS_SUBTYPE);

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
	public static final Characters DIGIT_CHARS = Characters.ofRange('0', '9'); //digits are the digits 0-9
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

	/** CSS property name definition. */
	public static final String CSS_VALUE_AUTO = "auto";
	/** CSS property name definition. */
	public static final String CSS_VALUE_INHERIT = "inherit";

	//Property names for CSS2

	/** CSS property name definition. */
	public static final String CSS_PROP_AZIMUTH = "azimuth";
	/** CSS property name definition. */
	public static final String CSS_PROP_BACKGROUND = "background";
	/** CSS property name definition. */
	public static final String CSS_PROP_BACKGROUND_ATTACHMENT = "background-attachment";
	/** CSS property name definition. */
	public static final String CSS_PROP_BACKGROUND_COLOR = "background-color";
	/** CSS property name definition. */
	public static final String CSS_PROP_BACKGROUND_IMAGE = "background-image";
	/** CSS property name definition. */
	public static final String CSS_PROP_BACKGROUND_POSITION = "background-position";
	/** CSS property name definition. */
	public static final String CSS_PROP_BACKGROUND_REPEAT = "background-repeat";
	/** CSS property name definition. */
	public static final String CSS_PROP_BORDER = "border";
	/** CSS property name definition. */
	public static final String CSS_PROP_BORDER_COLLAPSE = "border-collapse";
	/** CSS property name definition. */
	public static final String CSS_PROP_BORDER_COLOR = "border-color";
	/** CSS property name definition. */
	public static final String CSS_PROP_BORDER_SPACING = "border-spacing";
	/** CSS property name definition. */
	public static final String CSS_PROP_BORDER_STYLE = "border-style";
	/** CSS property name definition. */
	public static final String CSS_PROP_BORDER_TOP = "border-top";
	/** CSS property name definition. */
	public static final String CSS_PROP_BORDER_RIGHT = "border-right";
	/** CSS property name definition. */
	public static final String CSS_PROP_BORDER_BOTTOM = "border-bottom";
	/** CSS property name definition. */
	public static final String CSS_PROP_BORDER_LEFT = "border-left";
	/** CSS property name definition. */
	public static final String CSS_PROP_BORDER_TOP_COLOR = "border-top-color";
	/** CSS property name definition. */
	public static final String CSS_PROP_BORDER_RIGHT_COLOR = "border-right-color";
	/** CSS property name definition. */
	public static final String CSS_PROP_BORDER_BOTTOM_COLOR = "border-bottom-color";
	/** CSS property name definition. */
	public static final String CSS_PROP_BORDER_LEFT_COLOR = "border-left-color";
	/** CSS property name definition. */
	public static final String CSS_PROP_BORDER_TOP_STYLE = "border-top-style";
	/** CSS property name definition. */
	public static final String CSS_PROP_BORDER_RIGHT_STYLE = "border-right-style";
	/** CSS property name definition. */
	public static final String CSS_PROP_BORDER_BOTTOM_STYLE = "border-bottom-style";
	/** CSS property name definition. */
	public static final String CSS_PROP_BORDER_LEFT_STYLE = "border-left-style";
	/** CSS property name definition. */
	public static final String CSS_PROP_BORDER_TOP_WIDTH = "border-top-width";
	/** CSS property name definition. */
	public static final String CSS_PROP_BORDER_RIGHT_WIDTH = "border-right-width";
	/** CSS property name definition. */
	public static final String CSS_PROP_BORDER_BOTTOM_WIDTH = "border-bottom-width";
	/** CSS property name definition. */
	public static final String CSS_PROP_BORDER_LEFT_WIDTH = "border-left-width";
	/** CSS property name definition. */
	public static final String CSS_PROP_BORDER_WIDTH = "border-width";
	/** CSS property name definition. */
	public static final String CSS_PROP_BOTTOM = "bottom";
	/** CSS property name definition. */
	public static final String CSS_PROP_CAPTION = "caption-side";
	/** CSS property name definition. */
	public static final String CSS_PROP_CLEAR = "clear";
	/** CSS property name definition. */
	public static final String CSS_PROP_CLIP = "clip";
	/** CSS property name definition. */
	public static final String CSS_PROP_COLOR = "color";
	/** CSS property name definition. */
	public static final String CSS_PROP_CONTENT = "content";
	/** CSS property name definition. */
	public static final String CSS_PROP_COUNTER_INCREMENT = "counter-increment";
	/** CSS property name definition. */
	public static final String CSS_PROP_COUNTER_RESET = "counter-reset";
	/** CSS property name definition. */
	public static final String CSS_PROP_CUE = "cue";
	/** CSS property name definition. */
	public static final String CSS_PROP_CUE_AFTER = "cue-after";
	/** CSS property name definition. */
	public static final String CSS_PROP_CUE_BEFORE = "cue-before";
	/** CSS property name definition. */
	public static final String CSS_PROP_CURSOR = "cursor";
	/** CSS property name definition. */
	public static final String CSS_PROP_DIRECTION = "direction";
	/** CSS property name definition. */
	public static final String CSS_PROP_DISPLAY = "display";
	/** CSS property name definition. */
	public static final String CSS_PROP_ELEVATION = "elevation";
	/** CSS property name definition. */
	public static final String CSS_PROP_EMPTY_CELLS = "empty-cells";
	/** CSS property name definition. */
	public static final String CSS_PROP_FLOAT = "float";
	/** CSS property name definition. */
	public static final String CSS_PROP_FONT = "font";
	/** CSS property name definition. */
	public static final String CSS_PROP_FONT_FAMILY = "font-family";
	/** CSS property name definition. */
	public static final String CSS_PROP_FONT_SIZE = "font-size";
	/** CSS property name definition. */
	public static final String CSS_PROP_FONT_SIZE_ADJUST = "font-size-adjust";
	/** CSS property name definition. */
	public static final String CSS_PROP_FONT_STRETCH = "font-stretch";
	/** CSS property name definition. */
	public static final String CSS_PROP_FONT_STYLE = "font-style";
	/** CSS property name definition. */
	public static final String CSS_PROP_FONT_VARIANT = "font-variant";
	/** CSS property name definition. */
	public static final String CSS_PROP_FONT_WEIGHT = "font-weight";
	/** CSS property name definition. */
	public static final String CSS_PROP_HEIGHT = "height";
	/** CSS property name definition. */
	public static final String CSS_PROP_LEFT = "left";
	/** CSS property name definition. */
	public static final String CSS_PROP_LETTER_SPACING = "letter-spacing";
	/** CSS property name definition. */
	public static final String CSS_PROP_LINE_HEIGHT = "line-height";
	/** CSS property name definition. */
	public static final String CSS_PROP_LIST_STYLE = "list-style";
	/** CSS property name definition. */
	public static final String CSS_PROP_LIST_STYLE_IMAGE = "list-style-image";
	/** CSS property name definition. */
	public static final String CSS_PROP_LIST_STYLE_POSITION = "list-style-position";
	/** CSS property name definition. */
	public static final String CSS_PROP_LIST_STYLE_TYPE = "list-style-type";
	/** CSS property name definition. */
	public static final String CSS_PROP_MARGIN = "margin";
	/** CSS property name definition. */
	public static final String CSS_PROP_MARGIN_TOP = "margin-top";
	/** CSS property name definition. */
	public static final String CSS_PROP_MARGIN_RIGHT = "margin-right";
	/** CSS property name definition. */
	public static final String CSS_PROP_MARGIN_BOTTOM = "margin-bottom";
	/** CSS property name definition. */
	public static final String CSS_PROP_MARGIN_LEFT = "margin-left";
	/** CSS property name definition. */
	public static final String CSS_PROP_MARKET_OFFSET = "marker-offset";
	/** CSS property name definition. */
	public static final String CSS_PROP_MAX_HEIGHT = "max-height";
	/** CSS property name definition. */
	public static final String CSS_PROP_MAX_WIDTH = "max-width";
	/** CSS property name definition. */
	public static final String CSS_PROP_MIN_HEIGHT = "min-height";
	/** CSS property name definition. */
	public static final String CSS_PROP_MIN_WIDTH = "min-width";
	/** CSS property name definition. */
	public static final String CSS_PROP_OPACITY = "opacity";
	/** CSS property name definition. */
	public static final String CSS_PROP_ORPHANS = "orphans";
	/** CSS property name definition. */
	public static final String CSS_PROP_OUTLINE = "outline";
	/** CSS property name definition. */
	public static final String CSS_PROP_OUTLINE_COLOR = "outline-color";
	/** CSS property name definition. */
	public static final String CSS_PROP_OUTLINE_STYLE = "outline-style";
	/** CSS property name definition. */
	public static final String CSS_PROP_OUTLINE_WIDTH = "outline-width";
	/** CSS property name definition. */
	public static final String CSS_PROP_OVERFLOW = "overflow";
	/** CSS property name definition. */
	public static final String CSS_PROP_PADDING = "padding";
	/** CSS property name definition. */
	public static final String CSS_PROP_PADDING_TOP = "padding-top";
	/** CSS property name definition. */
	public static final String CSS_PROP_PADDING_RIGHT = "padding-right";
	/** CSS property name definition. */
	public static final String CSS_PROP_PADDING_BOTTOM = "padding-bottom";
	/** CSS property name definition. */
	public static final String CSS_PROP_PADDING_LEFT = "padding-left";
	/** CSS property name definition. */
	public static final String CSS_PROP_PAGE = "page";
	/** CSS property name definition. */
	public static final String CSS_PROP_PAGE_BREAK_AFTER = "page-break-after";
	/** CSS property name definition. */
	public static final String CSS_PROP_PAGE_BREAK_BEFORE = "page-break-before";
	/** CSS property name definition. */
	public static final String CSS_PROP_PAGE_BREAK_INSIDE = "page-break-inside";
	/** CSS property name definition. */
	public static final String CSS_PROP_PAUSE = "pause";
	/** CSS property name definition. */
	public static final String CSS_PROP_PAUSE_AFTER = "pause-after";
	/** CSS property name definition. */
	public static final String CSS_PROP_PAUSE_BEFORE = "pause-before";
	/** CSS property name definition. */
	public static final String CSS_PROP_PITCH = "pitch";
	/** CSS property name definition. */
	public static final String CSS_PROP_PITCH_NUMER = "pitch-range";
	/** CSS property name definition. */
	public static final String CSS_PROP_PLAY_DURING = "play-during";
	/** CSS property name definition. */
	public static final String CSS_PROP_POSITION = "position";
	/** CSS property name definition. */
	public static final String CSS_PROP_QUOTES = "quotes";
	/** CSS property name definition. */
	public static final String CSS_PROP_RICHNESS = "richness";
	/** CSS property name definition. */
	public static final String CSS_PROP_RIGHT = "right";
	/** CSS property name definition. */
	public static final String CSS_PROP_SPEAK = "speak";
	/** CSS property name definition. */
	public static final String CSS_PROP_SPEAK_HEADER = "speak-header";
	/** CSS property name definition. */
	public static final String CSS_PROP_SPEAK_NUMERAL = "speak-numeral";
	/** CSS property name definition. */
	public static final String CSS_PROP_SPEAK_PUNCTUATION = "speak-punctuation";
	/** CSS property name definition. */
	public static final String CSS_PROP_SPEECH_RATE = "speech-rate";
	/** CSS property name definition. */
	public static final String CSS_PROP_STRESS = "stress";
	/** CSS property name definition. */
	public static final String CSS_PROP_TABLE_LAYOUT = "table-layout";
	/** CSS property name definition. */
	public static final String CSS_PROP_TEXT_ALIGN = "text-align ident";
	/** CSS property name definition. */
	public static final String CSS_PROP_TEXT_DECORATION = "text-decoration";
	/** CSS property name definition. */
	public static final String CSS_PROP_TEXT_INDENT = "text-indent";
	/** CSS property name definition. */
	public static final String CSS_PROP_TEXT_SHADOW = "text-shadow";
	/** CSS property name definition. */
	public static final String CSS_PROP_TEXT_TRANSFORM = "text-transform";
	/** CSS property name definition. */
	public static final String CSS_PROP_TOP = "top";
	/** CSS property name definition. */
	public static final String CSS_PROP_UNICODE_BIDI = "unicode-bidi";
	/** CSS property name definition. */
	public static final String CSS_PROP_VERTICAL_ALIGN = "vertical-align";
	/** CSS property name definition. */
	public static final String CSS_PROP_VISIBILITY = "visibility";
	/** CSS property name definition. */
	public static final String CSS_PROP_VOICE_FAMILY = "voice-family";
	/** CSS property name definition. */
	public static final String CSS_PROP_VOLUME = "volume";
	/** CSS property name definition. */
	public static final String CSS_PROP_WHITE_SPACE = "white-space";
	/** CSS property name definition. */
	public static final String CSS_PROP_WIDOES = "widows";
	/** CSS property name definition. */
	public static final String CSS_PROP_WIDTH = "width";
	/** CSS property name definition. */
	public static final String CSS_PROP_WORD_SPACING = "word-spacing";
	/** CSS property name definition. */
	public static final String CSS_PROP_Z_INDEX = "z-index";

	//	/**
	//	 * The name used in a list to represent that there is an empty list. Used, for example, for text-decoration.
	//	 */
	//TODO del probably;	public static final String CSS_LIST_NONE="none";

	//TODO add other properties here

	/** The IE filter property. */
	public static final String CSS_PROP_FILTER = "filter";

	/** Transparent value for the <code>border-color</code> and <code>border-XXX-color</code> properties. */
	public static final String CSS_BORDER_COLOR_TRANSPARENT = "transparent";

	/** Collapse value for the <code>border-collapse</code> property. */
	public static final String CSS_BORDER_COLLAPSE_COLLAPSE = "collapse";
	/** Separate value for the <code>border-collapse</code> property. */
	public static final String CSS_BORDER_COLLAPSE_SEPARATE = "separate";

	/** Possible value for <code>border-style</code> and <code>border-XXX-style</code> properties. */
	public static final String CSS_BORDER_STYLE_NONE = "none";
	/** Possible value for <code>border-style</code> and <code>border-XXX-style</code> properties. */
	public static final String CSS_BORDER_STYLE_HIDDEN = "hidden";
	/** Possible value for <code>border-style</code> and <code>border-XXX-style</code> properties. */
	public static final String CSS_BORDER_STYLE_DOTTED = "dotted";
	/** Possible value for <code>border-style</code> and <code>border-XXX-style</code> properties. */
	public static final String CSS_BORDER_STYLE_DASHED = "dashed";
	/** Possible value for <code>border-style</code> and <code>border-XXX-style</code> properties. */
	public static final String CSS_BORDER_STYLE_SOLID = "solid";
	/** Possible value for <code>border-style</code> and <code>border-XXX-style</code> properties. */
	public static final String CSS_BORDER_STYLE_DOUBLE = "double";
	/** Possible value for <code>border-style</code> and <code>border-XXX-style</code> properties. */
	public static final String CSS_BORDER_STYLE_GROOVE = "groove";
	/** Possible value for <code>border-style</code> and <code>border-XXX-style</code> properties. */
	public static final String CSS_BORDER_STYLE_RIDGE = "ridge";
	/** Possible value for <code>border-style</code> and <code>border-XXX-style</code> properties. */
	public static final String CSS_BORDER_STYLE_INSET = "inset";
	/** Possible value for <code>border-style</code> and <code>border-XXX-style</code> properties. */
	public static final String CSS_BORDER_STYLE_OUTSET = "outset";

	/** Possible value for the <code>border-width</code> and <code>border-XXX-width</code> properties. */
	public static final String CSS_BORDER_WIDTH_THIN = "thin";
	/** Possible value for the <code>border-width</code> and <code>border-XXX-width</code> properties. */
	public static final String CSS_BORDER_WIDTH_MEDIUM = "medium";
	/** Possible value for the <code>border-width</code> and <code>border-XXX-width</code> properties. */
	public static final String CSS_BORDER_WIDTH_THICK = "thick";

	/** Transparent value for the <code>color</code> property. */
	public static final String CSS_COLOR_TRANSPARENT = "transparent";

	/** A color defined by HTML; used with several properties, such as <code>background-color</code>. */
	public static final String CSS_COLOR_BLACK = "black";
	/** A color defined by HTML; used with several properties, such as <code>background-color</code>. */
	public static final int CSS_COLOR_BLACK_VALUE = 0x000000;
	/** A color defined by HTML; used with several properties, such as <code>background-color</code>. */
	public static final String CSS_COLOR_GREEN = "green";
	/** A color defined by HTML; used with several properties, such as <code>background-color</code>. */
	public static final int CSS_COLOR_GREEN_VALUE = 0x008000;
	/** A color defined by HTML; used with several properties, such as <code>background-color</code>. */
	public static final String CSS_COLOR_SILVER = "silver";
	/** A color defined by HTML; used with several properties, such as <code>background-color</code>. */
	public static final int CSS_COLOR_SILVER_VALUE = 0xC0C0C0;
	/** A color defined by HTML; used with several properties, such as <code>background-color</code>. */
	public static final String CSS_COLOR_LIME = "lime";
	/** A color defined by HTML; used with several properties, such as <code>background-color</code>. */
	public static final int CSS_COLOR_LIME_VALUE = 0x00FF00;
	/** A color defined by HTML; used with several properties, such as <code>background-color</code>. */
	public static final String CSS_COLOR_GRAY = "gray";
	/** A color defined by HTML; used with several properties, such as <code>background-color</code>. */
	public static final int CSS_COLOR_GRAY_VALUE = 0x808080;
	/** A color defined by HTML; used with several properties, such as <code>background-color</code>. */
	public static final String CSS_COLOR_OLIVE = "olive";
	/** A color defined by HTML; used with several properties, such as <code>background-color</code>. */
	public static final int CSS_COLOR_OLIVE_VALUE = 0x808000;
	/** A color defined by HTML; used with several properties, such as <code>background-color</code>. */
	public static final String CSS_COLOR_WHITE = "white";
	/** A color defined by HTML; used with several properties, such as <code>background-color</code>. */
	public static final int CSS_COLOR_WHITE_VALUE = 0xFFFFFF;
	/** A color defined by HTML; used with several properties, such as <code>background-color</code>. */
	public static final String CSS_COLOR_YELLOW = "yellow";
	/** A color defined by HTML; used with several properties, such as <code>background-color</code>. */
	public static final int CSS_COLOR_YELLOW_VALUE = 0xFFFF00;
	/** A color defined by HTML; used with several properties, such as <code>background-color</code>. */
	public static final String CSS_COLOR_MAROON = "maroon";
	/** A color defined by HTML; used with several properties, such as <code>background-color</code>. */
	public static final int CSS_COLOR_MAROON_VALUE = 0x800000;
	/** A color defined by HTML; used with several properties, such as <code>background-color</code>. */
	public static final String CSS_COLOR_NAVY = "navy";
	/** A color defined by HTML; used with several properties, such as <code>background-color</code>. */
	public static final int CSS_COLOR_NAVY_VALUE = 0x000080;
	/** A color defined by HTML; used with several properties, such as <code>background-color</code>. */
	public static final String CSS_COLOR_RED = "red";
	/** A color defined by HTML; used with several properties, such as <code>background-color</code>. */
	public static final int CSS_COLOR_RED_VALUE = 0xFF0000;
	/** A color defined by HTML; used with several properties, such as <code>background-color</code>. */
	public static final String CSS_COLOR_BLUE = "blue";
	/** A color defined by HTML; used with several properties, such as <code>background-color</code>. */
	public static final int CSS_COLOR_BLUE_VALUE = 0x0000FF;
	/** A color defined by HTML; used with several properties, such as <code>background-color</code>. */
	public static final String CSS_COLOR_PURPLE = "purple";
	/** A color defined by HTML; used with several properties, such as <code>background-color</code>. */
	public static final int CSS_COLOR_PURPLE_VALUE = 0x800080;
	/** A color defined by HTML; used with several properties, such as <code>background-color</code>. */
	public static final String CSS_COLOR_TEAL = "teal";
	/** A color defined by HTML; used with several properties, such as <code>background-color</code>. */
	public static final int CSS_COLOR_TEAL_VALUE = 0x008080;
	/** A color defined by HTML; used with several properties, such as <code>background-color</code>. */
	public static final String CSS_COLOR_FUCHSIA = "fuchsia";
	/** A color defined by HTML; used with several properties, such as <code>background-color</code>. */
	public static final int CSS_COLOR_FUCHSIA_VALUE = 0xFF00FF;
	/** A color defined by HTML; used with several properties, such as <code>background-color</code>. */
	public static final String CSS_COLOR_AQUA = "aqua";
	/** A color defined by HTML; used with several properties, such as <code>background-color</code>. */
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

	/** Possible value for the <code>clear</code> property. */
	public static final String CSS_CLEAR_BOTH = "both";
	/** Possible value for the <code>clear</code> property. */
	public static final String CSS_CLEAR_LEFT = "left";
	/** Possible value for the <code>clear</code> property. */
	public static final String CSS_CLEAR_NONE = "none";
	/** Possible value for the <code>clear</code> property. */
	public static final String CSS_CLEAR_RIGHT = "right";

	/** Possible value for the <code>display</code> property. */
	public static final String CSS_DISPLAY_BLOCK = "block";
	/** Possible value for the <code>display</code> property. */
	public static final String CSS_DISPLAY_INLINE = "inline";
	/** Possible value for the <code>display</code> property. */
	public static final String CSS_DISPLAY_INLINE_BLOCK = "inline-block";
	/** Possible value for the <code>display</code> property. */
	public static final String CSS_DISPLAY_LIST_ITEM = "list-item";
	/** Possible value for the <code>display</code> property. */
	public static final String CSS_DISPLAY_RUN_IN = "run-in";
	/** Possible value for the <code>display</code> property. */
	public static final String CSS_DISPLAY_COMPACT = "compact";
	/** Possible value for the <code>display</code> property. */
	public static final String CSS_DISPLAY_MARKER = "marker";
	/** Possible value for the <code>display</code> property. */
	public static final String CSS_DISPLAY_TABLE = "table";
	/** Possible value for the <code>display</code> property. */
	public static final String CSS_DISPLAY_INLINE_TABLE = "inline-table";
	/** Possible value for the <code>display</code> property. */
	public static final String CSS_DISPLAY_TABLE_ROW_GROUP = "table-row-group";
	/** Possible value for the <code>display</code> property. */
	public static final String CSS_DISPLAY_TABLE_HEADER_GROUP = "table-header-group";
	/** Possible value for the <code>display</code> property. */
	public static final String CSS_DISPLAY_TABLE_FOOTER_GROUP = "table-footer-group";
	/** Possible value for the <code>display</code> property. */
	public static final String CSS_DISPLAY_TABLE_ROW = "table-row";
	/** Possible value for the <code>display</code> property. */
	public static final String CSS_DISPLAY_TABLE_COLUMN_GROUP = "table-column-group";
	/** Possible value for the <code>display</code> property. */
	public static final String CSS_DISPLAY_TABLE_CELL = "table-cell";
	/** Possible value for the <code>display</code> property. */
	public static final String CSS_DISPLAY_TABLE_CAPTION = "table-caption";
	/** Possible value for the <code>display</code> property. */
	public static final String CSS_DISPLAY_NONE = "none";

	/** Possible value for the <code>float</code> property. */
	public static final String CSS_FLOAT_LEFT = "left";
	/** Possible value for the <code>float</code> property. */
	public static final String CSS_FLOAT_NONE = "none";
	/** Possible value for the <code>float</code> property. */
	public static final String CSS_FLOAT_RIGHT = "right";

	/** Possible value for the <code>font-family</code> property. */
	public static final String CSS_FONT_FAMILY_SERIF = "serif";
	/** Possible value for the <code>font-family</code> property. */
	public static final String CSS_FONT_FAMILY_SANS_SERIF = "sans-serif";
	/** Possible value for the <code>font-family</code> property. */
	public static final String CSS_FONT_FAMILY_CURSIVE = "cursive";
	/** Possible value for the <code>font-family</code> property. */
	public static final String CSS_FONT_FAMILY_FANTASY = "fantasy";
	/** Possible value for the <code>font-family</code> property. */
	public static final String CSS_FONT_FAMILY_MONOSPACE = "monospace";
	/** The CSS2 recommended scaling factor between absolute font size strings. */
	public static final float FONT_SIZE_SCALING_FACTOR = 1.2f;

	/** Possible value for the <code>font-size</code> property. */
	public static final String CSS_FONT_SIZE_XX_SMALL = "xx-small";
	/** Possible value for the <code>font-size</code> property. */
	public static final String CSS_FONT_SIZE_X_SMALL = "x-small";
	/** Possible value for the <code>font-size</code> property. */
	public static final String CSS_FONT_SIZE_SMALL = "small";
	/** Possible value for the <code>font-size</code> property. */
	public static final String CSS_FONT_SIZE_MEDIUM = "medium";
	/** Possible value for the <code>font-size</code> property. */
	public static final String CSS_FONT_SIZE_LARGE = "large";
	/** Possible value for the <code>font-size</code> property. */
	public static final String CSS_FONT_SIZE_X_LARGE = "x-large";
	/** Possible value for the <code>font-size</code> property. */
	public static final String CSS_FONT_SIZE_XX_LARGE = "xx-large";
	/** Possible value for the <code>font-size</code> property. */
	public static final String CSS_FONT_SIZE_SMALLER = "smaller";
	/** Possible value for the <code>font-size</code> property. */
	public static final String CSS_FONT_SIZE_LARGER = "larger";

	/** Possible value for the <code>font-style</code> property. */
	public static final String CSS_FONT_STYLE_NORMAL = "normal";
	/** Possible value for the <code>font-style</code> property. */
	public static final String CSS_FONT_STYLE_ITALIC = "italic";
	/** Possible value for the <code>font-style</code> property. */
	public static final String CSS_FONT_STYLE_OBLIQUE = "oblique";

	/** Possible value for the <code>font-weight</code> property. */
	public static final String CSS_FONT_WEIGHT_NORMAL = "normal";
	/** Possible value for the <code>font-weight</code> property. */
	public static final String CSS_FONT_WEIGHT_BOLD = "bold";
	/** Possible value for the <code>font-weight</code> property. */
	public static final String CSS_FONT_WEIGHT_BOLDER = "bolder";
	/** Possible value for the <code>font-weight</code> property. */
	public static final String CSS_FONT_WEIGHT_LIGHTER = "lighter";

	/** Normal value for the <code>line-height</code> property. */
	public static final String CSS_LINE_HEIGHT_NORMAL = "normal";

	/** Possible value for the <code>list-style-type</code> property. */
	public static final String CSS_LIST_STYLE_TYPE_DISC = "disc";
	/** Possible value for the <code>list-style-type</code> property. */
	public static final String CSS_LIST_STYLE_TYPE_CIRCLE = "circle";
	/** Possible value for the <code>list-style-type</code> property. */
	public static final String CSS_LIST_STYLE_TYPE_SQUARE = "square";
	/** Possible value for the <code>list-style-type</code> property. */
	public static final String CSS_LIST_STYLE_TYPE_DECIMAL = "decimal";
	/** Possible value for the <code>list-style-type</code> property. */
	public static final String CSS_LIST_STYLE_TYPE_DECIMAL_LEADING_ZERO = "decimal-leading-zero";
	/** Possible value for the <code>list-style-type</code> property. */
	public static final String CSS_LIST_STYLE_TYPE_LOWER_ROMAN = "lower-roman";
	/** Possible value for the <code>list-style-type</code> property. */
	public static final String CSS_LIST_STYLE_TYPE_UPPER_ROMAN = "upper-roman";
	/** Possible value for the <code>list-style-type</code> property. */
	public static final String CSS_LIST_STYLE_TYPE_LOWER_GREEK = "lower-greek";
	/** Possible value for the <code>list-style-type</code> property. */
	public static final String CSS_LIST_STYLE_TYPE_LOWER_ALPHA = "lower-alpha";
	/** Possible value for the <code>list-style-type</code> property. */
	public static final String CSS_LIST_STYLE_TYPE_LOWER_LATIN = "lower-latin";
	/** Possible value for the <code>list-style-type</code> property. */
	public static final String CSS_LIST_STYLE_TYPE_UPPER_ALPHA = "upper-alpha";
	/** Possible value for the <code>list-style-type</code> property. */
	public static final String CSS_LIST_STYLE_TYPE_UPPER_LATIN = "upper-latin";
	/** Possible value for the <code>list-style-type</code> property. */
	public static final String CSS_LIST_STYLE_TYPE_HEBREW = "hebrew";
	/** Possible value for the <code>list-style-type</code> property. */
	public static final String CSS_LIST_STYLE_TYPE_ARMENIAN = "armenian";
	/** Possible value for the <code>list-style-type</code> property. */
	public static final String CSS_LIST_STYLE_TYPE_GEORGIAN = "georgian";
	/** Possible value for the <code>list-style-type</code> property. */
	public static final String CSS_LIST_STYLE_TYPE_CJK_IDEOGRAPHIC = "cjk-ideographic";
	/** Possible value for the <code>list-style-type</code> property. */
	public static final String CSS_LIST_STYLE_TYPE_HIRAGANA = "hiragana";
	/** Possible value for the <code>list-style-type</code> property. */
	public static final String CSS_LIST_STYLE_TYPE_KATAKANA = "katakana";
	/** Possible value for the <code>list-style-type</code> property. */
	public static final String CSS_LIST_STYLE_TYPE_HIRAGANA_IROHA = "hiragana-iroha";
	/** Possible value for the <code>list-style-type</code> property. */
	public static final String CSS_LIST_STYLE_TYPE_KATAKANA_IROHA = "katakana-iroha";
	/** Possible value for the <code>list-style-type</code> property. */
	public static final String CSS_LIST_STYLE_TYPE_NONE = "none";

	/** Possible value for the <code>overflow</code> property. */
	public static final String CSS_OVERFLOW_VISIBLE = "visible";
	/** Possible value for the <code>overflow</code> property. */
	public static final String CSS_OVERFLOW_HIDDEN = "hidden";
	/** Possible value for the <code>overflow</code> property. */
	public static final String CSS_OVERFLOW_SCROLL = "scroll";
	/** Possible value for the <code>overflow</code> property. */
	public static final String CSS_OVERFLOW_AUTO = "auto";

	/** Possible value for the <code>page-break-after</code> property. */
	public static final String CSS_PAGE_BREAK_AFTER_AUTO = "auto";
	/** Possible value for the <code>page-break-after</code> property. */
	public static final String CSS_PAGE_BREAK_AFTER_ALWAYS = "always";
	/** Possible value for the <code>page-break-after</code> property. */
	public static final String CSS_PAGE_BREAK_AFTER_AVOID = "avoid";
	/** Possible value for the <code>page-break-after</code> property. */
	public static final String CSS_PAGE_BREAK_AFTER_LEFT = "left";
	/** Possible value for the <code>page-break-after</code> property. */
	public static final String CSS_PAGE_BREAK_AFTER_RIGHT = "right";
	/** Possible value for the <code>page-break-after</code> property. */
	public static final String CSS_PAGE_BREAK_AFTER_INHERIT = "inherit";

	/** Possible value for the <code>page-break-before</code> property. */
	public static final String CSS_PAGE_BREAK_BEFORE_AUTO = "auto";
	/** Possible value for the <code>page-break-before</code> property. */
	public static final String CSS_PAGE_BREAK_BEFORE_ALWAYS = "always";
	/** Possible value for the <code>page-break-before</code> property. */
	public static final String CSS_PAGE_BREAK_BEFORE_AVOID = "avoid";
	/** Possible value for the <code>page-break-before</code> property. */
	public static final String CSS_PAGE_BREAK_BEFORE_LEFT = "left";
	/** Possible value for the <code>page-break-before</code> property. */
	public static final String CSS_PAGE_BREAK_BEFORE_RIGHT = "right";
	/** Possible value for the <code>page-break-before</code> property. */
	public static final String CSS_PAGE_BREAK_BEFORE_INHERIT = "inherit";

	/** Possible value for the <code>position</code> property. */
	public static final String CSS_POSITION_ABSOLUTE = "absolute";
	/** Possible value for the <code>position</code> property. */
	public static final String CSS_POSITION_FIXED = "fixed";
	/** Possible value for the <code>position</code> property. */
	public static final String CSS_POSITION_RELATIVE = "relative";

	/** Possible value for the <code>table-layout</code> property. */
	public static final String CSS_TABLE_LAYOUT_AUTO = "auto";
	/** Possible value for the <code>table-layout</code> property. */
	public static final String CSS_TABLE_LAYOUT_FIXED = "fixed";

	/** Possible value for the <code>text-decoration</code> property */
	public static final String CSS_TEXT_DECORATION_NONE = "none";
	/** Possible value for the <code>text-decoration</code> property */
	public static final String CSS_TEXT_DECORATION_UNDERLINE = "underline";
	/** Possible value for the <code>text-decoration</code> property */
	public static final String CSS_TEXT_DECORATION_OVERLINE = "overline";
	/** Possible value for the <code>text-decoration</code> property */
	public static final String CSS_TEXT_DECORATION_LINE_THROUGH = "line-through";
	/** Possible value for the <code>text-decoration</code> property */
	public static final String CSS_TEXT_DECORATION_BLINK = "blink";
	/** Possible value for the <code>text-decoration</code> property */
	public static final String CSS_TEXT_DECORATION_INHERIT = "inherit";

	/** Possible value for the <code>text-transform</code> property. */
	public static final String CSS_TEXT_TRANSFORM_CAPITALIZE = "capitalize";
	/** Possible value for the <code>text-transform</code> property. */
	public static final String CSS_TEXT_TRANSFORM_UPPERCASE = "uppercase";
	/** Possible value for the <code>text-transform</code> property. */
	public static final String CSS_TEXT_TRANSFORM_LOWERCASE = "lowercase";
	/** Possible value for the <code>text-transform</code> property. */
	public static final String CSS_TEXT_TRANSFORM_NONE = "none";
	/** Possible value for the <code>text-transform</code> property. */
	public static final String CSS_TEXT_TRANSFORM_INHERIT = "inherit";

	/** Possible value for the <code>vertical-align</code> property. */
	public static final String CSS_VERTICAL_ALIGN_BASELINE = "baseline";
	/** Possible value for the <code>vertical-align</code> property. */
	public static final String CSS_VERTICAL_ALIGN_SUB = "sub";
	/** Possible value for the <code>vertical-align</code> property. */
	public static final String CSS_VERTICAL_ALIGN_SUPER = "super";
	/** Possible value for the <code>vertical-align</code> property. */
	public static final String CSS_VERTICAL_ALIGN_TOP = "top";
	/** Possible value for the <code>vertical-align</code> property. */
	public static final String CSS_VERTICAL_ALIGN_TEXT_TOP = "text-top";
	/** Possible value for the <code>vertical-align</code> property. */
	public static final String CSS_VERTICAL_ALIGN_MIDDLE = "middle";
	/** Possible value for the <code>vertical-align</code> property. */
	public static final String CSS_VERTICAL_ALIGN_BOTTOM = "bottom";
	/** Possible value for the <code>vertical-align</code> property. */
	public static final String CSS_VERTICAL_ALIGN_TEXT_BOTTOM = "text-bottom";
	/** Possible value for the <code>vertical-align</code> property. */
	public static final String CSS_VERTICAL_ALIGN_INHERIT = "inherit";

	/** Possible value for the <code>visibility</code> property. */
	public static final String CSS_VISIBILITY_COLLAPSE = "collapse";
	/** Possible value for the <code>visibility</code> property. */
	public static final String CSS_VISIBILITY_HIDDEN = "hidden";
	/** Possible value for the <code>visibility</code> property. */
	public static final String CSS_VISIBILITY_INHERIT = "inherit";
	/** Possible value for the <code>visibility</code> property. */
	public static final String CSS_VISIBILITY_VISIBLE = "visible";

	/**
	 * Standard cursors supported by CSS.
	 * @author Garret Wilson
	 * @see <a href="http://www.w3.org/TR/CSS21/ui.html">CSS 2.1 User Interface: Cursors</a>
	 */
	public static enum Cursor implements Identifier {

		/** The UA determines the cursor to display based on the current context. */
		AUTO,
		/** A simple crosshair (e.g., short line segments resembling a "+" sign). */
		CROSSHAIR,
		/** The platform-dependent default cursor. Often rendered as an arrow. */
		DEFAULT,
		/** The cursor is a pointer that indicates a link. */
		POINTER,
		/** Indicates something is to be moved. */
		MOVE,
		/** Indicate that some edge is to be moved from the east of the box. */
		E_RESIZE,
		/** Indicate that some edge is to be moved from the north-east corner of the box. */
		NE_RESIZE,
		/** Indicate that some edge is to be moved from the north-west corner of the box. */
		NW_RESIZE,
		/** Indicate that some edge is to be moved from the north of the box. */
		N_RESIZE,
		/** Indicate that some edge is to be moved from the south-east corner of the box. */
		SE_RESIZE,
		/** Indicate that some edge is to be moved from the south-west corner of the box. */
		SW_RESIZE,
		/** Indicate that some edge is to be moved from the south corner of the box. */
		S_RESIZE,
		/** Indicate that some edge is to be moved from the west of the box. */
		W_RESIZE,
		/** Indicates text that may be selected. Often rendered as an I-beam. */
		TEXT,
		/** Indicates that the program is busy and the user should wait. Often rendered as a watch or hourglass. */
		WAIT,
		/**
		 * A progress indicator. The program is performing some processing, but is different from {@link #WAIT} in that the user may still interact with the
		 * program. Often rendered as a spinning beach ball, or an arrow with a watch or hourglass.
		 */
		PROGRESS,
		/** Help is available for the object under the cursor. Often rendered as a question mark or a balloon. */
		HELP;
	}

}
