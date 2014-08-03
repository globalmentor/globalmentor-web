/*
 * Copyright © 1996-2008 GlobalMentor, Inc. <http://www.globalmentor.com/>
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

import org.w3c.dom.Element;

/**
 * A single selector with an optional name and an optional class, although one of the two should be specified.
 * @author Garret Wilson
 * @see XMLCSSStyleRule
 * @deprecated
 */
public class XMLCSSSelector//TODO fix, Cloneable
{

	/*Constructor which requires a tag name and a class.
	@param tagName The name of the tag this selector matches,
		or the empty string if no name is to be specified.
	@param tagClass The class of this selector, or the empty string if no class is to be specified.
	*/
	public XMLCSSSelector(final String tagName, final String tagClass) {
		TagName = tagName; //set the tag name
		TagClass = tagClass; //set the tag class
	}

	/*Constructor which requires a tag name.
	@param tagName The name of the tag this selector matches,
		or the empty string if no name is to be specified.
	*/
	public XMLCSSSelector(final String tagName) {
		this(tagName, ""); //construct this object with only a tag name
	}

	/**
	 * Determines whether, this selector applies to the specified element.
	 * @param The element whose name and class should match those contained in this selector.
	 * @see XMLCSSStyleRule#appliesTo
	 */
	public boolean appliesTo(Element element) {
		//TODO later, add the CSS ID checking
		if(getTagName().length() == 0 || element.getNodeName().equals(getTagName())) { //if the tag names match, or we don't have a tag name to match with (which means we'll be matching class only)
			if(getTagClass().length() == 0 || element.getAttributeNS(null, "class").equals(getTagClass())) //if the class names match as well (or there isn't a specified class in this selector) TODO use a constant here
				return true;
		}
		return false; //if we get to this point, this selector doesn't apply to this element
	}

	/** The name of the tag this selector matches, or the empty string to match any name. */
	private String TagName = "";

	/** @return The name of the tag this selector matches, or the empty string to match any name. */
	public String getTagName() {
		return TagName;
	}

	/** The class of the tag this selector matches, or the empty string to match any class. */
	private String TagClass = "";

	/** @return The class of the tag this selector matches, or the empty string to match any class. */
	public String getTagClass() {
		return TagClass;
	}

	/**
	 * Returns the parsable textual representation of the selector in the form "tagName.className".
	 * @return The parsable textual representation of the selector. #see XMLCSSStyleRule#getSelectorText
	 */
	public String getCssText() {
		String cssText = getTagName(); //always include the tag name, even if this string is empty
		if(getTagClass().length() != 0) //if a class is specified
			cssText += "." + getTagClass(); //add a separator period, along with the class
		return cssText; //return the text we constructed
	}

}
