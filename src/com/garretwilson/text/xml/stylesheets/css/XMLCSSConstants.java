package com.garretwilson.text.xml.stylesheets.css;

import java.awt.Color;
import java.util.regex.Pattern;

import javax.mail.internet.ContentType;

import com.globalmentor.io.ContentTypeConstants;
import com.globalmentor.io.ContentTypes;

import static com.globalmentor.java.Characters.*;

/**Several constants for CSS.*/
public class XMLCSSConstants
{
	/**The content type for CSS: <code>text/css</code>.*/ 
	public static final ContentType TEXT_CSS_CONTENT_TYPE=new ContentType(ContentTypes.TEXT_PRIMARY_TYPE, ContentTypeConstants.CSS_SUBTYPE, null);

	/**A space character.*/
	public final static char SPACE_CHAR=' ';
	/**A tab character.*/
	public final static char TAB_CHAR='\t';
	/**A carriage return character.*/
	public final static char CR_CHAR='\r';
	/**A linefeed character.*/
	public final static char LF_CHAR='\n';
	/**A formfeed character.*/
	public final static char FF_CHAR='\f';
	/**An equals sign.*/
	public final static char EQUAL_CHAR='=';
	/**A single quotation character.*/
	public final static char SINGLE_QUOTE_CHAR='\'';
	/**A double quotation character.*/
	public final static char DOUBLE_QUOTE_CHAR='"';
	/**The characters considered by CSS to be digits.*/
	public final static String DIGIT_CHARS="0123456789";	//digits are the digits 0-9
	/**The decimal character that separates a number from its fraction.*/
	public final static char DECIMAL_CHAR='.';
	/**The characters considered by CSS to be part of a number.*/
	public final static String NUMBER_CHARS=DIGIT_CHARS+DECIMAL_CHAR;	//numbers are composed of digits and an optional decimal
	/**The characters considered by CSS to be whitespace.*/
	public final static String WHITESPACE_CHARS=""+SPACE_CHAR+TAB_CHAR+CR_CHAR+LF_CHAR+FF_CHAR;	//whitespace characters in CSS are space, tab, CR, LF, and FF
	/**The characters considered by CSS to be whitespace.*/
	public final static Pattern WHITESPACE_PATTERN=Pattern.compile("[\\u0020\\0009\\u000A\\u000C\\u000D]");	//whitespace characters in CSS are space, tab, CR, LF, and FF
	/**The characters considered by CSS to be combinators.*/
	public final static String COMBINATOR_CHARS=WHITESPACE_CHARS+GREATER_THAN_CHAR+PLUS_SIGN_CHAR+TILDE_CHAR;	//combinators are white space, "greater-than sign" (U+003E, >), "plus sign" (U+002B, +) and "tilde" (U+007E, ~)

	/**The starting character of an at-rule: @{ident}.*/
	public final static String AT_RULE_START="@";
	/**The starting characters of an XML comment; CSS ignores this string, but not its contents.*/
	public final static String CDO="<!--";
	/**The ending characters of an XML comment; CSS ignores this string, but not its contents.*/
	public final static String CDC="-->";
	/**The starting characters of CSS comment.*/
	public final static String COMMENT_START="/*";
	/**The ending characters of a CSS comment.*/
	public final static String COMMENT_END="*/";
	/**The character used to separate declarations in a ruleset.*/
	public final static char DECLARATION_SEPARATOR_CHAR=';';
	/**The string representing a font face at-rule.*/
	public final static String FONT_FACE_RULE_SYMBOL="@font-face";
	/**The string representing an import at-rule.*/
	public final static String IMPORT_RULE_SYMBOL="@import";
	/**The string representing a media at-rule.*/
	public final static String MEDIA_RULE_SYMBOL="@media";
	/**The string representing a page at-rule.*/
	public final static String PAGE_RULE_SYMBOL="@page";
	/**The character used to divide a property from its value in a declaration.*/
	public final static char PROPERTY_DIVIDER_CHAR=':';
	/**The character used to begin a group of style rules.*/
	public final static char RULE_GROUP_START_CHAR='{';
	/**The character used to end a group of style rules.*/
	public final static char RULE_GROUP_END_CHAR='}';
	/**The character used to separate selectors.*/
	public final static char SELECTOR_SEPARATOR_CHAR=',';
	/**The character used to begin an RGB number in the form #RGB or #RRGGBB.*/
	public final static char RGB_NUMBER_CHAR='#';
	/**The delimiter used to introduce a class simple selector.*/
	public final static char CLASS_SELECTOR_DELIMITER='.';
	/**The delimiter used to introduce an ID simple selector.*/
	public final static char ID_SELECTOR_DELIMITER='#';
	/**The delimiter used to introduce a pseudo class simple selector.*/
	public final static char PSEUDO_CLASS_DELIMITER=':';
	/**The character used to separate items in a list.*/
	public final static char LIST_DELIMITER_CHAR=',';

		//CSS units
			//relative units
	/**The "font-size" of the relevant font.*/
	public final static String EM_UNITS="em";
	/**The "x-height" of the relevant font.*/
	public final static String EX_UNITS="ex";
	/**Pixels, relative to the viewing device.*/
	public final static String PX_UNITS="px";
		//absolute units
	/**Inches---1 inch is equal to 2.54 centimeters.*/
	public final static String IN_UNITS="in";
	/**Centimeters.*/
	public final static String CM_UNITS="cm";
	/**Millimeters.*/
	public final static String MM_UNITS="mm";
	/**Points---the points used by CSS2 are equal to 1/72th of an inch.*/
	public final static String PT_UNITS="pt";
	/**Picas---1 pica is equal to 12 points.*/
	public final static String PC_UNITS="pc";

	//Property names for CSS2
	public final static String CSS_PROP_AZIMUTH="azimuth";
	public final static String CSS_PROP_BACKGROUND="background";
	public final static String CSS_PROP_BACKGROUND_ATTACHMENT="background-attachment";
	public final static String CSS_PROP_BACKGROUND_COLOR="background-color";
	public final static String CSS_PROP_BACKGROUND_IMAGE="background-image";
	public final static String CSS_PROP_BACKGROUND_POSITION="background-position";
	public final static String CSS_PROP_BACKGROUND_REPEAT="background-repeat";
	public final static String CSS_PROP_BORDER="border";
	public final static String CSS_PROP_BORDER_COLLAPSE="border-collapse";
	public final static String CSS_PROP_BORDER_COLOR="border-color";
	public final static String CSS_PROP_BORDER_SPACING="border-spacing";
	public final static String CSS_PROP_BORDER_STYLE="border-style";
	public final static String CSS_PROP_BORDER_TOP="border-top";
	public final static String CSS_PROP_BORDER_RIGHT="border-right";
	public final static String CSS_PROP_BORDER_BOTTOM="border-bottom";
	public final static String CSS_PROP_BORDER_LEFT="border-left";
	public final static String CSS_PROP_BORDER_TOP_COLOR="border-top-color";
	public final static String CSS_PROP_BORDER_RIGHT_COLOR="border-right-color";
	public final static String CSS_PROP_BORDER_BOTTOM_COLOR="border-bottom-color";
	public final static String CSS_PROP_BORDER_LEFT_COLOR="border-left-color";
	public final static String CSS_PROP_BORDER_TOP_STYLE="border-top-style";
	public final static String CSS_PROP_BORDER_RIGHT_STYLE="border-right-style";
	public final static String CSS_PROP_BORDER_BOTTOM_STYLE="border-bottom-style";
	public final static String CSS_PROP_BORDER_LEFT_STYLE="border-left-style";
	public final static String CSS_PROP_BORDER_TOP_WIDTH="border-top-width";
	public final static String CSS_PROP_BORDER_RIGHT_WIDTH="border-right-width";
	public final static String CSS_PROP_BORDER_BOTTOM_WIDTH="border-bottom-width";
	public final static String CSS_PROP_BORDER_LEFT_WIDTH="border-left-width";
	public final static String CSS_PROP_BORDER_WIDTH="border-width";
	public final static String CSS_PROP_BOTTOM="bottom";
	public final static String CSS_PROP_CAPTION="caption-side";
	public final static String CSS_PROP_CLEAR="clear";
	public final static String CSS_PROP_CLIP="clip";
	public final static String CSS_PROP_COLOR="color";
	public final static String CSS_PROP_CONTENT="content";
	public final static String CSS_PROP_COUNTER_INCREMENT="counter-increment";
	public final static String CSS_PROP_COUNTER_RESET="counter-reset";
	public final static String CSS_PROP_CUE="cue";
	public final static String CSS_PROP_CUE_AFTER="cue-after";
	public final static String CSS_PROP_CUE_BEFORE="cue-before";
	public final static String CSS_PROP_CURSOR="cursor";
	public final static String CSS_PROP_DIRECTION="direction";
	public final static String CSS_PROP_DISPLAY="display";
	public final static String CSS_PROP_ELEVATION="elevation";
	public final static String CSS_PROP_EMPTY_CELLS="empty-cells";
	public final static String CSS_PROP_FLOAT="float";
	public final static String CSS_PROP_FONT="font";
	public final static String CSS_PROP_FONT_FAMILY="font-family";
	public final static String CSS_PROP_FONT_SIZE="font-size";
	public final static String CSS_PROP_FONT_SIZE_ADJUST="font-size-adjust";
	public final static String CSS_PROP_FONT_STRETCH="font-stretch";
	public final static String CSS_PROP_FONT_STYLE="font-style";
	public final static String CSS_PROP_FONT_VARIANT="font-variant";
	public final static String CSS_PROP_FONT_WEIGHT="font-weight";
	public final static String CSS_PROP_HEIGHT="height";
	public final static String CSS_PROP_LEFT="left";
	public final static String CSS_PROP_LETTER_SPACING="letter-spacing";
	public final static String CSS_PROP_LINE_HEIGHT="line-height";
	public final static String CSS_PROP_LIST_STYLE="list-style";
	public final static String CSS_PROP_LIST_STYLE_IMAGE="list-style-image";
	public final static String CSS_PROP_LIST_STYLE_POSITION="list-style-position";
	public final static String CSS_PROP_LIST_STYLE_TYPE="list-style-type";
	public final static String CSS_PROP_MARGIN="margin";
	public final static String CSS_PROP_MARGIN_TOP="margin-top";
	public final static String CSS_PROP_MARGIN_RIGHT="margin-right";
	public final static String CSS_PROP_MARGIN_BOTTOM="margin-bottom";
	public final static String CSS_PROP_MARGIN_LEFT="margin-left";
	public final static String CSS_PROP_MARKET_OFFSET="marker-offset";
	public final static String CSS_PROP_MAX_HEIGHT="max-height";
	public final static String CSS_PROP_MAX_WIDTH="max-width";
	public final static String CSS_PROP_MIN_HEIGHT="min-height";
	public final static String CSS_PROP_MIN_WIDTH="min-width";
	public final static String CSS_PROP_OPACITY="opacity";
	public final static String CSS_PROP_ORPHANS="orphans";
	public final static String CSS_PROP_OUTLINE="outline";
	public final static String CSS_PROP_OUTLINE_COLOR="outline-color";
	public final static String CSS_PROP_OUTLINE_STYLE="outline-style";
	public final static String CSS_PROP_OUTLINE_WIDTH="outline-width";
	public final static String CSS_PROP_OVERFLOW="overflow";
	public final static String CSS_PROP_PADDING="padding";
	public final static String CSS_PROP_PADDING_TOP="padding-top";
	public final static String CSS_PROP_PADDING_RIGHT="padding-right";
	public final static String CSS_PROP_PADDING_BOTTOM="padding-bottom";
	public final static String CSS_PROP_PADDING_LEFT="padding-left";
	public final static String CSS_PROP_PAGE="page";
	public final static String CSS_PROP_PAGE_BREAK_AFTER="page-break-after";
	public final static String CSS_PROP_PAGE_BREAK_BEFORE="page-break-before";
	public final static String CSS_PROP_PAGE_BREAK_INSIDE="page-break-inside";
	public final static String CSS_PROP_PAUSE="pause";
	public final static String CSS_PROP_PAUSE_AFTER="pause-after";
	public final static String CSS_PROP_PAUSE_BEFORE="pause-before";
	public final static String CSS_PROP_PITCH="pitch";
	public final static String CSS_PROP_PITCH_NUMER="pitch-range";
	public final static String CSS_PROP_PLAY_DURING="play-during";
	public final static String CSS_PROP_POSITION="position";
	public final static String CSS_PROP_QUOTES="quotes";
	public final static String CSS_PROP_RICHNESS="richness";
	public final static String CSS_PROP_RIGHT="right";
	public final static String CSS_PROP_SPEAK="speak";
	public final static String CSS_PROP_SPEAK_HEADER="speak-header";
	public final static String CSS_PROP_SPEAK_NUMERAL="speak-numeral";
	public final static String CSS_PROP_SPEAK_PUNCTUATION="speak-punctuation";
	public final static String CSS_PROP_SPEECH_RATE="speech-rate";
	public final static String CSS_PROP_STRESS="stress";
	public final static String CSS_PROP_TABLE_LAYOUT="table-layout";
	public final static String CSS_PROP_TEXT_ALIGN="text-align ident";
	public final static String CSS_PROP_TEXT_DECORATION="text-decoration";
	public final static String CSS_PROP_TEXT_INDENT="text-indent";
	public final static String CSS_PROP_TEXT_SHADOW="text-shadow";
	public final static String CSS_PROP_TEXT_TRANSFORM="text-transform";
	public final static String CSS_PROP_TOP="top";
	public final static String CSS_PROP_UNICODE_BIDI="unicode-bidi";
	public final static String CSS_PROP_VERTICAL_ALIGN="vertical-align";
	public final static String CSS_PROP_VISIBILITY="visibility";
	public final static String CSS_PROP_VOICE_FAMILY="voice-family";
	public final static String CSS_PROP_VOLUME="volume";
	public final static String CSS_PROP_WHITE_SPACE="white-space";
	public final static String CSS_PROP_WIDOES="widows";
	public final static String CSS_PROP_WIDTH="width";
	public final static String CSS_PROP_WORD_SPACING="word-spacing";
	public final static String CSS_PROP_Z_INDEX="z-index";

	/**The name used in a list to represent that there is an empty list. Used,
		for example, for text-decoration.*/
//G***del probably;	public final static String CSS_LIST_NONE="none";

//G***add other properties here

	/**The IE filter property.*/
	public final static String CSS_PROP_FILTER="filter";

	
	//properties <code>border-color</code> and <code>border-XXX-color</code>
	public final static String CSS_BORDER_COLOR_TRANSPARENT="transparent";

	//properties <code>border-collapse</code>
	public final static String CSS_BORDER_COLLAPSE_COLLAPSE="collapse";
	public final static String CSS_BORDER_COLLAPSE_SEPARATE="separate";

	//properties <code>border-style</code> and <code>border-XXX-style</code>
	public final static String CSS_BORDER_STYLE_NONE="none";
	public final static String CSS_BORDER_STYLE_HIDDEN="hidden";
	public final static String CSS_BORDER_STYLE_DOTTED="dotted";
	public final static String CSS_BORDER_STYLE_DASHED="dashed";
	public final static String CSS_BORDER_STYLE_SOLID="solid";
	public final static String CSS_BORDER_STYLE_DOUBLE="double";
	public final static String CSS_BORDER_STYLE_GROOVE="groove";
	public final static String CSS_BORDER_STYLE_RIDGE="ridge";
	public final static String CSS_BORDER_STYLE_INSET="inset";
	public final static String CSS_BORDER_STYLE_OUTSET="outset";

	//properties <code>border-width</code> and <code>border-XXX-width</code>
	public final static String CSS_BORDER_WIDTH_THIN="thin";
	public final static String CSS_BORDER_WIDTH_MEDIUM="medium";
	public final static String CSS_BORDER_WIDTH_THICK="thick";

	//**Property "color" G***fix
		//colors as defined by HTML; used with several properties, such as background-color
	public final static String CSS_COLOR_BLACK="black";
	public final static int CSS_COLOR_BLACK_VALUE=0x000000;
	public final static String CSS_COLOR_GREEN="green";
	public final static int CSS_COLOR_GREEN_VALUE=0x008000;
	public final static String CSS_COLOR_SILVER="silver";
	public final static int CSS_COLOR_SILVER_VALUE=0xC0C0C0;
	public final static String CSS_COLOR_LIME="lime";
	public final static int CSS_COLOR_LIME_VALUE=0x00FF00;
	public final static String CSS_COLOR_GRAY="gray";
	public final static int CSS_COLOR_GRAY_VALUE=0x808080;
	public final static String CSS_COLOR_OLIVE="olive";
	public final static int CSS_COLOR_OLIVE_VALUE=0x808000;
	public final static String CSS_COLOR_WHITE="white";
	public final static int CSS_COLOR_WHITE_VALUE=0xFFFFFF;
	public final static String CSS_COLOR_YELLOW="yellow";
	public final static int CSS_COLOR_YELLOW_VALUE=0xFFFF00;
	public final static String CSS_COLOR_MAROON="maroon";
	public final static int CSS_COLOR_MAROON_VALUE=0x800000;
	public final static String CSS_COLOR_NAVY="navy";
	public final static int CSS_COLOR_NAVY_VALUE=0x000080;
	public final static String CSS_COLOR_RED="red";
	public final static int CSS_COLOR_RED_VALUE=0xFF0000;
	public final static String CSS_COLOR_BLUE="blue";
	public final static int CSS_COLOR_BLUE_VALUE=0x0000FF;
	public final static String CSS_COLOR_PURPLE="purple";
	public final static int CSS_COLOR_PURPLE_VALUE=0x800080;
	public final static String CSS_COLOR_TEAL="teal";
	public final static int CSS_COLOR_TEAL_VALUE=0x008080;
	public final static String CSS_COLOR_FUCHSIA="fuchsia";
	public final static int CSS_COLOR_FUCHSIA_VALUE=0xFF00FF;
	public final static String CSS_COLOR_AQUA="aqua";
	public final static int CSS_COLOR_AQUA_VALUE=0x00FFFF;

	//predefined colors
	public final static Color COLOR_BLACK=new Color(CSS_COLOR_BLACK_VALUE);
	public final static Color COLOR_GREEN=new Color(CSS_COLOR_GREEN_VALUE);
 	public final static Color COLOR_SILVER=new Color(CSS_COLOR_SILVER_VALUE);
 	public final static Color COLOR_LIME=new Color(CSS_COLOR_LIME_VALUE);
 	public final static Color COLOR_GRAY=new Color(CSS_COLOR_GRAY_VALUE);
 	public final static Color COLOR_OLIVE=new Color(CSS_COLOR_OLIVE_VALUE);
 	public final static Color COLOR_WHITE=new Color(CSS_COLOR_WHITE_VALUE);
 	public final static Color COLOR_YELLOW=new Color(CSS_COLOR_YELLOW_VALUE);
 	public final static Color COLOR_MAROON=new Color(CSS_COLOR_MAROON_VALUE);
 	public final static Color COLOR_NAVY=new Color(CSS_COLOR_NAVY_VALUE);
 	public final static Color COLOR_RED=new Color(CSS_COLOR_RED_VALUE);
 	public final static Color COLOR_BLUE=new Color(CSS_COLOR_BLUE_VALUE);
 	public final static Color COLOR_PURPLE=new Color(CSS_COLOR_PURPLE_VALUE);
 	public final static Color COLOR_TEAL=new Color(CSS_COLOR_TEAL_VALUE);
 	public final static Color COLOR_FUCHSIA=new Color(CSS_COLOR_FUCHSIA_VALUE);
 	public final static Color COLOR_AQUA=new Color(CSS_COLOR_AQUA_VALUE);


//G***add other properties here

	//**Property "display"
	public final static String CSS_DISPLAY_BLOCK="block";
	public final static String CSS_DISPLAY_INLINE="inline";
	public final static String CSS_DISPLAY_INLINE_BLOCK="inline-block";
	public final static String CSS_DISPLAY_LIST_ITEM="list-item";
	public final static String CSS_DISPLAY_RUN_IN="run-in";
	public final static String CSS_DISPLAY_COMPACT="compact";
	public final static String CSS_DISPLAY_MARKER="marker";
	public final static String CSS_DISPLAY_TABLE="table";
	public final static String CSS_DISPLAY_INLINE_TABLE="inline-table";
	public final static String CSS_DISPLAY_TABLE_ROW_GROUP="table-row-group";
	public final static String CSS_DISPLAY_TABLE_HEADER_GROUP="table-header-group";
	public final static String CSS_DISPLAY_TABLE_FOOTER_GROUP="table-footer-group";
	public final static String CSS_DISPLAY_TABLE_ROW="table-row";
	public final static String CSS_DISPLAY_TABLE_COLUMN_GROUP="table-column-group";
	public final static String CSS_DISPLAY_TABLE_CELL="table-cell";
	public final static String CSS_DISPLAY_TABLE_CAPTION="table-caption";
	public final static String CSS_DISPLAY_NONE="none";

	//**Property <code>float</code>
	public final static String CSS_FLOAT_LEFT="left";
	public final static String CSS_FLOAT_NONE="none";
	public final static String CSS_FLOAT_RIGHT="right";

	//**Property "font-family"
	public final static String CSS_FONT_FAMILY_SERIF="serif";
	public final static String CSS_FONT_FAMILY_SANS_SERIF="sans-serif";
	public final static String CSS_FONT_FAMILY_CURSIVE="cursive";
	public final static String CSS_FONT_FAMILY_FANTASY="fantasy";
	public final static String CSS_FONT_FAMILY_MONOSPACE="monospace";
	/**The CSS2 recommended scaling factor between absolute font size strings.*/
	public final static float FONT_SIZE_SCALING_FACTOR=1.2f;

	//**Property "font-size"
	public final static String CSS_FONT_SIZE_XX_SMALL="xx-small";
	public final static String CSS_FONT_SIZE_X_SMALL="x-small";
	public final static String CSS_FONT_SIZE_SMALL="small";
	public final static String CSS_FONT_SIZE_MEDIUM="medium";
	public final static String CSS_FONT_SIZE_LARGE="large";
	public final static String CSS_FONT_SIZE_X_LARGE="x-large";
	public final static String CSS_FONT_SIZE_XX_LARGE="xx-large";
	public final static String CSS_FONT_SIZE_SMALLER="smaller";
	public final static String CSS_FONT_SIZE_LARGER="larger";

	//**Property "font-style"
	public final static String CSS_FONT_STYLE_NORMAL="normal";
	public final static String CSS_FONT_STYLE_ITALIC="italic";
	public final static String CSS_FONT_STYLE_OBLIQUE="oblique";

	//**Property "font-weight"
	public final static String CSS_FONT_WEIGHT_NORMAL="normal";
	public final static String CSS_FONT_WEIGHT_BOLD="bold";
	public final static String CSS_FONT_WEIGHT_BOLDER="bolder";
	public final static String CSS_FONT_WEIGHT_LIGHTER="lighter";

	//**Property "line-height"
	public final static String CSS_LINE_HEIGHT_NORMAL="normal";

	//**Property <code>list-style-type</code>
	public final static String CSS_LIST_STYLE_TYPE_DISC="disc";
	public final static String CSS_LIST_STYLE_TYPE_CIRCLE="circle";
	public final static String CSS_LIST_STYLE_TYPE_SQUARE="square";
	public final static String CSS_LIST_STYLE_TYPE_DECIMAL="decimal";
	public final static String CSS_LIST_STYLE_TYPE_DECIMAL_LEADING_ZERO="decimal-leading-zero";
	public final static String CSS_LIST_STYLE_TYPE_LOWER_ROMAN="lower-roman";
	public final static String CSS_LIST_STYLE_TYPE_UPPER_ROMAN="upper-roman";
	public final static String CSS_LIST_STYLE_TYPE_LOWER_GREEK="lower-greek";
	public final static String CSS_LIST_STYLE_TYPE_LOWER_ALPHA="lower-alpha";
	public final static String CSS_LIST_STYLE_TYPE_LOWER_LATIN="lower-latin";
	public final static String CSS_LIST_STYLE_TYPE_UPPER_ALPHA="upper-alpha";
	public final static String CSS_LIST_STYLE_TYPE_UPPER_LATIN="upper-latin";
	public final static String CSS_LIST_STYLE_TYPE_HEBREW="hebrew";
	public final static String CSS_LIST_STYLE_TYPE_ARMENIAN="armenian";
	public final static String CSS_LIST_STYLE_TYPE_GEORGIAN="georgian";
	public final static String CSS_LIST_STYLE_TYPE_CJK_IDEOGRAPHIC="cjk-ideographic";
	public final static String CSS_LIST_STYLE_TYPE_HIRAGANA="hiragana";
	public final static String CSS_LIST_STYLE_TYPE_KATAKANA="katakana";
	public final static String CSS_LIST_STYLE_TYPE_HIRAGANA_IROHA="hiragana-iroha";
	public final static String CSS_LIST_STYLE_TYPE_KATAKANA_IROHA="katakana-iroha";
	public final static String CSS_LIST_STYLE_TYPE_NONE="none";

	//property <code>overflow</code>
	public final static String CSS_OVERFLOW_VISIBLE="visible";
	public final static String CSS_OVERFLOW_HIDDEN="hidden";
	public final static String CSS_OVERFLOW_SCROLL="scroll";
	public final static String CSS_OVERFLOW_AUTO="auto";

	//property <code>page-break-after</code>
	public final static String CSS_PAGE_BREAK_AFTER_AUTO="auto";
	public final static String CSS_PAGE_BREAK_AFTER_ALWAYS="always";
	public final static String CSS_PAGE_BREAK_AFTER_AVOID="avoid";
	public final static String CSS_PAGE_BREAK_AFTER_LEFT="left";
	public final static String CSS_PAGE_BREAK_AFTER_RIGHT="right";
	public final static String CSS_PAGE_BREAK_AFTER_INHERIT="inherit";

	//property <code>page-break-before</code>
	public final static String CSS_PAGE_BREAK_BEFORE_AUTO="auto";
	public final static String CSS_PAGE_BREAK_BEFORE_ALWAYS="always";
	public final static String CSS_PAGE_BREAK_BEFORE_AVOID="avoid";
	public final static String CSS_PAGE_BREAK_BEFORE_LEFT="left";
	public final static String CSS_PAGE_BREAK_BEFORE_RIGHT="right";
	public final static String CSS_PAGE_BREAK_BEFORE_INHERIT="inherit";

	//**property <code>position</code>
	public final static String CSS_POSITION_ABSOLUTE="absolute";
	public final static String CSS_POSITION_FIXED="fixed";
	public final static String CSS_POSITION_RELATIVE="relative";

	//**property <code>table-layout</code>
	public final static String CSS_TABLE_LAYOUT_AUTO="auto";
	public final static String CSS_TABLE_LAYOUT_FIXED="fixed";

	//property <code>text-decoration</code>
	public final static String CSS_TEXT_DECORATION_NONE="none";
	public final static String CSS_TEXT_DECORATION_UNDERLINE="underline";
	public final static String CSS_TEXT_DECORATION_OVERLINE="overline";
	public final static String CSS_TEXT_DECORATION_LINE_THROUGH="line-through";
	public final static String CSS_TEXT_DECORATION_BLINK="blink";
	public final static String CSS_TEXT_DECORATION_INHERIT="inherit";

	//property <code>text-transform</code>
	public final static String CSS_TEXT_TRANSFORM_CAPITALIZE="capitalize";
	public final static String CSS_TEXT_TRANSFORM_UPPERCASE="uppercase";
	public final static String CSS_TEXT_TRANSFORM_LOWERCASE="lowercase";
	public final static String CSS_TEXT_TRANSFORM_NONE="none";
	public final static String CSS_TEXT_TRANSFORM_INHERIT="inherit";

	//property <code>vertical-align</code>
	public final static String CSS_VERTICAL_ALIGN_BASELINE="baseline";
	public final static String CSS_VERTICAL_ALIGN_SUB="sub";
	public final static String CSS_VERTICAL_ALIGN_SUPER="super";
	public final static String CSS_VERTICAL_ALIGN_TOP="top";
	public final static String CSS_VERTICAL_ALIGN_TEXT_TOP="text-top";
	public final static String CSS_VERTICAL_ALIGN_MIDDLE="middle";
	public final static String CSS_VERTICAL_ALIGN_BOTTOM="bottom";
	public final static String CSS_VERTICAL_ALIGN_TEXT_BOTTOM="text-bottom";
	public final static String CSS_VERTICAL_ALIGN_INHERIT="inherit";

	//property <code>visibility</code>
	public final static String CSS_VISIBILITY_COLLAPSE="collapse";
	public final static String CSS_VISIBILITY_HIDDEN="hidden";
	public final static String CSS_VISIBILITY_INHERIT="inherit";
	public final static String CSS_VISIBILITY_VISIBLE="visible";

}
