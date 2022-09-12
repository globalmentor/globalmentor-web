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

package com.globalmentor.xml.dom.impl.stylesheets.css;

import org.w3c.dom.*;
import org.w3c.dom.css.*;
import org.w3c.dom.stylesheets.StyleSheet;

import com.globalmentor.xml.dom.impl.XMLDOMException;
import com.globalmentor.xml.dom.impl.stylesheets.XMLStyleSheet;

/**
 * A CSS stylesheet (i.e. a stylesheet whose content type is "text/css").
 * @author Garret Wilson
 * @see CSSRule
 * @see org.w3c.dom.css.CSSStyleSheet
 * @deprecated
 */
@Deprecated
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

	@Override
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

	@Override
	public CSSRule getOwnerRule() {
		return OwnerRule;
	}

	/** The list of all CSS rules, including both rule sets and at-rules. */
	private XMLCSSRuleList CssRules = new XMLCSSRuleList();

	@Override
	public CSSRuleList getCssRules() {
		return CssRules;
	}

	@Override
	public int insertRule(String rule, int index) throws DOMException {
		return 0; //TODO fix
	}

	@Override
	public void deleteRule(int index) throws DOMException {
		//TODO check for read-only status
		try {
			((XMLCSSRuleList)getCssRules()).remove(index); //remove the item at the specified index
		} catch(IndexOutOfBoundsException e) { //if they don't give a valid index
			throw new XMLDOMException(DOMException.INDEX_SIZE_ERR, new Object[] { Integer.valueOf(index) }); //use our own type of exception to show that this index is out of bounds
		}
	}
}
