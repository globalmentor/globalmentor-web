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

package com.globalmentor.css;

import org.w3c.dom.css.*;

import static com.globalmentor.w3c.spec.CSS.*;

/**
 * Constants and utilities for working with Cascading Style Sheets (CSS), especially through the CSS DOM.
 * 
 * @author Garret Wilson
 * @see <a href="http://www.w3.org/Style/CSS/">Cascading Style Sheets</a>
 */
public class CSS {

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
