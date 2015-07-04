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

import java.util.List;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.css.*;

import static com.globalmentor.text.xml.stylesheets.css.XMLCSS.*;

import com.globalmentor.text.xml.processor.XMLDOMException;

/**
 * The class which represents a signle rule set in a CSS stylesheet.
 * @author Garret Wilson
 * @see org.w3c.dom.css.CSSRule
 * @deprecated
 */
public class XMLCSSStyleRule extends XMLCSSRule implements CSSStyleRule //TODO fix, Cloneable
{

	/**
	 * Constructor which requires a parent stylesheet to be specified.
	 * @param type The type of this CSS rule.
	 * @param parentStyleSheet The parent of this stylesheet.
	 * @see XMLDocument
	 */
	public XMLCSSStyleRule(final XMLCSSStyleSheet parentStyleSheet) {
		super(STYLE_RULE, parentStyleSheet); //construct the parent class
	}

	/**
	 * @return A parsable string representation of this style rule. For the DOM version, see getCssText().
	 * @see XMLCSSStyleRule#getCssText
	 */
	public String toString() {
		return getCssText();
	}

	/** The parsable textual representation of the rule. */

	/**
	 * Returns the parsable textual representation of the rule. This reflects the current state of the rule and not its initial value.
	 * @return The parsable textual representation of the rule.
	 * @version DOM Level 2
	 * @since DOM Level 2
	 */
	public String getCssText() {
		final StringBuilder stringBuilder = new StringBuilder(); //create a new string buffer to collect our data
		stringBuilder.append(getSelectorText()); //add the selector text
		stringBuilder.append('\n'); //add a newline
		stringBuilder.append(RULE_GROUP_START_CHAR); //append the rule group start character
		stringBuilder.append('\n'); //add a newline
		stringBuilder.append(getStyle().getCssText()); //add the text of the style itself
		stringBuilder.append(RULE_GROUP_END_CHAR); //append the rule group end character
		stringBuilder.append('\n'); //add a newline
		return stringBuilder.toString(); //return the string we constructed
	}

	/**
	 * A list of arrays, each containing contextual selectors (or, if the array has a length of one, a non-contextual selector).
	 */
	private ArrayList SelectorArrayList = new ArrayList();

	/**
	 * Returns the list of selector arrays, each containing contextual selectors (or, if the array has a length of one, a non-contextual selector).
	 * @return The list of selector arrays, each containing contextual selectors.
	 */
	//TODO with appliesTo, we may not need this function to be public
	public List getSelectorArrayList() {
		return SelectorArrayList;
	}

	/**
	 * Determines whether this contextual selector (represented by an array of selectors) applies to the specified element.
	 * @param element The element this context array might apply to.
	 * @see XMLCSSSelector#appliesTo
	 */
	protected boolean appliesTo(Element element, final XMLCSSSelector[] contextArray) {
		for(int contextIndex = contextArray.length - 1; contextIndex >= 0; --contextIndex) { //look at each context for this selector, working from the last one (applying to this element) to the first one (applying to an ancestor of this element)
			final XMLCSSSelector selectorContext = contextArray[contextIndex]; //get this context of the selector
			if(!selectorContext.appliesTo(element)) //if this selector context does not apply to this element
				return false; //this entire contextual selector does not apply
			if(contextIndex > 0) { //if we're still working our way up the chain
				if(element.getParentNode() != null && element.getParentNode().getNodeType() == Element.ELEMENT_NODE) //if this element has a parent element node
					element = (Element)element.getParentNode(); //we'll check the earlier context with this element's parent node
				else
					//if this element has no parent node
					return false; //since we're not at the top of the context chain, this element can't match since it has no parents to compare
			}
		}
		return true; //if we make it here, we've worked our way up the selector context chain and every element along the way has applied to the appropriate selector
	}

	/**
	 * Determines whether, based upon this style rule's selectors, this style applies to the specified element.
	 * @param element The element this context array might apply to.
	 * @see XMLCSSSelector#appliesTo
	 */
	public boolean appliesTo(Element element) {
		for(int selectorIndex = 0; selectorIndex < getSelectorArrayList().size(); ++selectorIndex) { //look at each selector array
			final XMLCSSSelector[] contextArray = (XMLCSSSelector[])getSelectorArrayList().get(selectorIndex); //get a reference to this array of selectors
			if(appliesTo(element, contextArray)) { //if this context array applies to the element
				return true; //we don't need to check the others; we've found a match
			}
		}
		return false; //if  none of our array of contextual selectors match, show that this style rule doesn't apply to this element
	}

	/**
	 * Adds a single selector (which may be contextual) to this style rule.
	 * @param selectorText A selector (which may consist of several context strings) in the "name.class" format; either the name portion or the class portion must
	 *          be specified.
	 * @throws DOMException <ul>
	 *           <li>SYNTAX_ERR: Raised if the specified CSS string value has a syntax error and is unparsable.</li>
	 *           </ul>
	 */
	public void addSelector(final String selectorText) { //TODO do we need this?: throws DOMException
		final List selectorContextList = new ArrayList(); //create a list to hold our contextual selectors
		final StringTokenizer contextTokenizer = new StringTokenizer(selectorText, WHITESPACE_CHARS); //create a tokenizer to look through the contextual parts of the selector
		while(contextTokenizer.hasMoreTokens()) { //while there are more context selectors
			final String contextText = contextTokenizer.nextToken(); //get the next context String
			String tagName = "", tagClass = ""; //it's possible one of these will remain an empty string
			int separatorPos = contextText.indexOf('.'); //find the separator between the name and the class TODO use a constant here
			if(separatorPos == -1) //if there is no separator
				tagName = contextText.trim(); //the entire string (after we trim it) will be the tag name TODO we probably don't need to trim it, here, using the string tokenizer
			else { //if there is a separator
				tagName = contextText.substring(0, separatorPos).trim(); //the trimmed first part will be the name TODO what about trailing space here?
				tagClass = contextText.substring(separatorPos + 1).trim(); //the trimmed second part will be the name TODO what about beginning space here?
			}
			if(tagName.length() == 0 && tagClass.length() == 0) //if this selector has neither name nor class
				throw new XMLDOMException(XMLDOMException.SYNTAX_ERR, new Object[] { selectorText }); //show that this selector text has a syntax error
			selectorContextList.add(new XMLCSSSelector(tagName, tagClass)); //add a new selector for this context
		}
		addSelector((XMLCSSSelector[])selectorContextList.toArray(new XMLCSSSelector[selectorContextList.size()])); //convert the list to an array and add it as one of our selectors
	}

	/**
	 * Adds a single selector to this style rule.
	 * @param selector A single, not-contextual selector.
	 * @see XMLCSSSelector
	 */
	public void addSelector(final XMLCSSSelector selector) {
		addSelector(new XMLCSSSelector[] { selector }); //create a new array and add it to our list
	}

	/**
	 * Adds a contextual selector to this style rule.
	 * @param selectorArray An array of selectors for a contextual selector.
	 * @see XMLCSSSelector
	 */
	public void addSelector(final XMLCSSSelector[] selectorArray) {
		getSelectorArrayList().add(selectorArray); //add this array to our list
	}

	/**
	 * Returns the textual representation of the selector for the rule set.
	 * @return The textual representation of the selector for the rule set.
	 * @see XMLCSSSelector
	 * @version DOM Level 2
	 * @since DOM Level 2
	 */
	public String getSelectorText() {
		final StringBuilder stringBuilder = new StringBuilder(); //create a new string builder for constructing the text
		for(int selectorIndex = 0; selectorIndex < getSelectorArrayList().size(); ++selectorIndex) { //look at each selector array
			final XMLCSSSelector[] contextArray = (XMLCSSSelector[])getSelectorArrayList().get(selectorIndex); //get a reference to this array of selectors
			for(int contextIndex = 0; contextIndex < contextArray.length; ++contextIndex) { //look at each context for this selector
				stringBuilder.append(contextArray[contextIndex].getCssText()); //add this context to our selector string
				if(contextIndex < contextArray.length - 1) { //if this isn't the last context
					stringBuilder.append(SPACE_CHAR); //place a space between contexts
				}
			}
			if(selectorIndex < getSelectorArrayList().size() - 1) { //if this isn't the last selector
				stringBuilder.append(SELECTOR_SEPARATOR_CHAR).append(SPACE_CHAR); //place a comma and a space between selectors
			}
		}
		return stringBuilder.toString(); //return the text we constructed
	}

	/**
	 * Sets the textual representation of the selector for the rule set.
	 * @throws DOMException <ul>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this rule is readonly.</li>
	 *           <li>SYNTAX_ERR: Raised if the specified CSS string value has a syntax error and is unparsable.</li>
	 *           </ul>
	 * @see XMLCSSSelector
	 * @version DOM Level 2
	 * @since DOM Level 2
	 */
	public void setSelectorText(String selectorText) throws DOMException {
		//TODO check read-only status here

		//TODO fix
	}

	/**
	 * The declaration block of this rule set, which has a default style declaration block when an instance of this class is created.
	 */
	private XMLCSSStyleDeclaration Style = new XMLCSSStyleDeclaration(); //create a default style

	/**
	 * Returns the declaration block of this rule set.
	 * @return The declaration block of this rule set.
	 * @version DOM Level 2
	 * @since DOM Level 2
	 */
	public CSSStyleDeclaration getStyle() {
		return Style;
	}

}
