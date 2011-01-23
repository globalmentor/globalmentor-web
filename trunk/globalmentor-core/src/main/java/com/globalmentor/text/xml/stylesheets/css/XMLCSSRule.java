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

import org.w3c.dom.DOMException;
import org.w3c.dom.css.*;

/**The class which forms a basis for all CSS rules, including rule sets and
at-rules.
@author Garret Wilson
@version DOM Level 2
@since DOM Level 2
@see org.w3c.dom.css.CSSRule
@deprecated
*/
public abstract class XMLCSSRule implements CSSRule//TODO fix, Cloneable
{
	/**Constructor which requires a parent stylesheet to be specified.
	@param type The type of this CSS rule.
	@param parentStyleSheet The parent of this stylesheet.
	@see XMLDocument
	*/
	public XMLCSSRule(final short type, final XMLCSSStyleSheet parentStyleSheet)
	{
		Type=type;	//set the type of this CSS rule
		ParentStyleSheet=parentStyleSheet;	//set the parent style sheet
	}

	/**The type of CSS rule this is.*/
	private short Type=UNKNOWN_RULE;

	/**Returns the type of CSS rule.
	@return A code representing the type of the CSS rule.
	@see org.w3c.dom.css.CSSRule
	@version DOM Level 2
	@since DOM Level 2
	*/
	public short getType() {return Type;}

	/**The parsable textual representation of the rule.*/
//TODO fix	private String CssText="";

	/**Returns the parsable textual representation of the rule. This reflects the
	current state of the rule and not its initial value.
	@return The parsable textual representation of the rule.
	@version DOM Level 2
	@since DOM Level 2
	*/
//TODO fix	public abstract String getCssText();

	/**Sets the parsable textual representation of the rule. This reflects the
	current state of the rule and not its initial value.
	@exception DOMException
	<ul>
		<li>HIERARCHY_REQUEST_ERR: Raised if the rule cannot be inserted at this
			point in the style sheet.</li>
		<li>NO_MODIFICATION_ALLOWED_ERR: Raised if the rule is readonly.</li>
		<li>SYNTAX_ERR: Raised if the specified CSS string value has a syntax error
			and is unparsable.</li>
		<li>INVALID_MODIFICATION_ERR: Raised if the specified CSS string value
			represents a different type of rule than the current one.</li>
	</ul>
	@version DOM Level 2
	@since DOM Level 2
	*/
	public void setCssText(String cssText) throws DOMException
	{
			//TODO we need to just parse the text here, probably

		//TODO check for syntax error
		//TODO check for invalid modification
		//TODO check for hierarchy request error
		//TODO check for read-only status
//TODO fix		CssText=cssText;	//set the text
	}

	/**The stylesheet which contains this rule.*/
	private XMLCSSStyleSheet ParentStyleSheet=null;

	/**Returns the stylesheet which contains this rule.
	@return The stylesheet which contains this rule.
	@see CSSStyleSheet
	@version DOM Level 2
	@since DOM Level 2
	*/
	public CSSStyleSheet getParentStyleSheet() {return ParentStyleSheet;}

	/**Specifies which XML document owns this node. Can only be accessed from
	within this package.
	@param ownerDocument The document which owns this node.
	@see XMLDocument
	@see XMLNode#getOwnerXMLDocument
	*/
//TODO fix	void setOwnerXMLDocument(final XMLDocument ownerDocument) {OwnerDocument=ownerDocument;}

	/**Returns the document associated with this node, which was also in most
	circumstances the document which created this node. This will be <code>null</code>
	for documents or for document types which are not used with any document, yet.
	@return The document which owns this node, or <code>null</code> if there is no owner.
	@see XMLDocument
	@see XMLNode#getOwnerXMLDocument
	@version DOM Level 2
	*/
//TODO fix	public Document getOwnerDocument() {return OwnerDocument;}

	/**The rule which contains this rule.*/
	private XMLCSSRule ParentRule=null;

	/**Returns this rule's parent rule, if this rule is contained inside another
		rule (e.g. a style rule inside a @media block). If this rule is not nested
		inside any other block, this returns <code>null</code>.
	@return The containing rule, or <code>null</code> if this rule has no parent rule.
	@version DOM Level 2
	@since DOM Level 2
	*/
	public CSSRule getParentRule() {return ParentRule;}

}


