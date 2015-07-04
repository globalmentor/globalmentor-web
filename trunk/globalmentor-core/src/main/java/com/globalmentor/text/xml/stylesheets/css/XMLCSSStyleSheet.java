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

import com.globalmentor.text.xml.processor.XMLDOMException;
import com.globalmentor.text.xml.stylesheets.*;

import org.w3c.dom.*;
import org.w3c.dom.css.*;
import org.w3c.dom.stylesheets.StyleSheet;

/**
 * A CSS stylesheet (i.e. a stylesheet whose content type is "text/css").
 * @author Garret Wilson
 * @see CSSRule
 * @see org.w3c.dom.css.CSSStyleSheet
 * @deprecated
 */
public class XMLCSSStyleSheet extends XMLStyleSheet implements CSSStyleSheet {

	/**
	 * Constructor for a CSS stylesheet specifying an owner node.
	 * @param ownerNode The node that associates this stylesheet with the document.
	 */
	public XMLCSSStyleSheet(final Node ownerNode) {
		super(ownerNode); //construct the parent class
	}

	/**
	 * Constructor for a CSS stylesheet specifying a parent stylesheet.
	 * @param parentStyleSheet The stylesheet that included this stylesheet.
	 */
	public XMLCSSStyleSheet(final StyleSheet parentStyleSheet) {
		super(parentStyleSheet); //construct the parent class
	}

	//TODO perhaps have another constructer which specifies an owner rule

	/** @return A parsable string representation of the stylesheet. */
	//TODO probably put this in a CssText property and call it from here
	public String toString() {
		final StringBuilder stringBuilder = new StringBuilder(); //create a new string builder to collect our data
		//TODO return whatever header stuff is needed
		for(int ruleIndex = 0; ruleIndex < ((XMLCSSRuleList)getCssRules()).size(); ++ruleIndex) { //look at each of the rules
			stringBuilder.append((XMLCSSRule)((XMLCSSRuleList)getCssRules()).get(ruleIndex)).toString(); //return a string representation of this rule, with a linefeed at the end to make it look nice
			stringBuilder.append('\n'); //append a linefeed at the end to make it look nice
		}
		return stringBuilder.toString(); //return the string we constructed
	}

	/** The rule which owns this stylesheet, if this stylesheet comes from an @import rule. */
	private XMLCSSRule OwnerRule = null;

	/**
	 * Returns the CSS import rule which owns this stylesheet if this stylesheet comes from an @import rule. The owner node attribute will be <code>null</code> in
	 * this case. If the stylesheet comes from an element or a processing instruction, the owner rule will be <code>null</code> and the owner node attribute will
	 * contain the node.
	 * @see XMLStyleSheet#getOwnerNode
	 * @see XMLStyleSheet#getParentStyleSheet
	 * @version DOM Level 2
	 * @since DOM Level 2
	 */
	public CSSRule getOwnerRule() {
		return OwnerRule;
	}

	/** The list of all CSS rules, including both rule sets and at-rules. */
	private XMLCSSRuleList CssRules = new XMLCSSRuleList();

	/**
	 * Returns the list of all CSS rules contained within the stylesheet. This includes both rule sets and at-rules.
	 * @return The list of all CSS rules, including both rule sets and at-rules.
	 * @version DOM Level 2
	 * @since DOM Level 2
	 */
	public CSSRuleList getCssRules() {
		return CssRules;
	}

	/**
	 * Inserts a new rule into the style sheet. The new rule now becomes part of the cascade.
	 * @param rule The parsable text representing the rule. For rule sets this contains both the selector and the style declaration. For at-rules, this specifies
	 *          both the at-identifier and the rule content.
	 * @param index The index within the style sheet's rule list of the rule before which to insert the specified rule. If the specified index is equal to the
	 *          length of the style sheet's rule collection, the rule will be added to the end of the style sheet.
	 * @return The index within the style sheet's rule collection of the newly inserted rule.
	 * @throws DOMException <ul>
	 *           <li>HIERARCHY_REQUEST_ERR: Raised if the rule cannot be inserted at the specified index e.g. if an <code>@import</code> rule is inserted after a
	 *           standard rule set or other at-rule.</li>
	 *           <li>INDEX_SIZE_ERR: Raised if the specified index is not a valid insertion point.</li>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this style sheet is readonly.</li>
	 *           <li>SYNTAX_ERR: Raised if the specified rule has a syntax error and is unparsable.</li>
	 *           </ul>
	 * @version DOM Level 2
	 * @since DOM Level 2
	 */
	public int insertRule(String rule, int index) throws DOMException {
		return 0; //TODO fix
	}

	/**
	 * Delete a rule from the stylesheet.
	 * @param index The index within the style sheet's rule list of the rule to remove.
	 * @throws DOMException <ul>
	 *           <li>INDEX_SIZE_ERR: Raised if the specified index does not correspond to a rule in the style sheet's rule list.</li>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this style sheet is readonly.
	 *           <li>
	 *           </ul>
	 * @version DOM Level 2
	 * @since DOM Level 2
	 */
	public void deleteRule(int index) throws DOMException {
		//TODO check for read-only status
		try {
			((XMLCSSRuleList)getCssRules()).remove(index); //remove the item at the specified index
		} catch(IndexOutOfBoundsException e) { //if they don't give a valid index
			throw new XMLDOMException(DOMException.INDEX_SIZE_ERR, new Object[] { new Integer(index) }); //use our own type of exception to show that this index is out of bounds
		}
	}
}
