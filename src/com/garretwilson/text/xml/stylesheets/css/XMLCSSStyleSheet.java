package com.garretwilson.text.xml.stylesheets.css;

//G***del if we don't need import java.io.*;
import com.garretwilson.text.xml.XMLDOMException;
import com.garretwilson.text.xml.XMLNode;
import com.garretwilson.text.xml.stylesheets.*;
import com.garretwilson.text.xml.stylesheets.css.*;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.css.*;
import org.w3c.dom.stylesheets.StyleSheet;

/**A CSS stylesheet (i.e. a stylesheet whose content type is "text/css").
@see CSSRule
@see org.w3c.dom.css.CSSStyleSheet
@see XMLCSSConstants
*/
public class XMLCSSStyleSheet extends XMLStyleSheet implements CSSStyleSheet, XMLCSSConstants
{

	/**Constructor for a CSS stylesheet specifying an owner node.
	@param ownerNode The node that associates this stylesheet with the document.
	*/
	public XMLCSSStyleSheet(final Node ownerNode)
	{
		super(ownerNode);	//construct the parent class
	}

	/**Constructor for a CSS stylesheet specifying a parent stylesheet.
	@param parentStyleSheet The stylesheet that included this stylesheet.
	*/
	public XMLCSSStyleSheet(final StyleSheet parentStyleSheet)
	{
		super(parentStyleSheet);	//construct the parent class
	}

	/**Creates and returns a duplicate copy of this node. The clone has no parent.
	This function creates a "shallow" clone which does not contain clones of all
	child nodes. For the DOM version, see cloneNode().
	@return A duplicate copy of this node.
	@see XMLNode#cloneXMLNode
	@see XMLNode#cloneNode
	@see XMLNode#getParentXMLNode
	*/
/*G***fix or del
	public Object clone()
	{
		return new XMLDocument();	//create a new document G***do we clone the document type and stuff as well?
	}

*/


	//G***perhaps have another constructer which specifies an owner rule


	/**@return A parsable string representation of the stylesheet.*/
	//G***probably put this in a CssText property and call it from here
	public String toString()
	{
		final StringBuffer stringBuffer=new StringBuffer(); //create a new string buffer to collect our data
		//G***return whatever header stuff is needed
		for(int ruleIndex=0; ruleIndex<((XMLCSSRuleList)getCssRules()).size(); ++ruleIndex)	//look at each of the rules
		{
//G***del			stringBuffer.append(RULE_GROUP_START_CHAR); //append the rule group start character
			stringBuffer.append((XMLCSSRule)((XMLCSSRuleList)getCssRules()).get(ruleIndex)).toString();	//return a string representation of this rule, with a linefeed at the end to make it look nice
//G***del			stringBuffer.append(RULE_GROUP_END_CHAR); //append the rule group end character
			stringBuffer.append('\n'); //append a linefeed at the end to make it look nice
		}
		return stringBuffer.toString();	//return the string we constructed
	}


	/**The rule which owns this stylesheet, if this stylesheet comes from an @import rule.*/

	private XMLCSSRule OwnerRule=null;

	/**Returns the CSS import rule which owns this stylesheet if this stylesheet
	comes from an @import rule. The owner node attribute will be <code>null</code>
	in this case. If the stylesheet comes from an element or a processing instruction,
	the owner rule will be <code>null</code> and the owner node attribute will contain
	the node.
	@see XMLStyleSheet#getOwnerNode
	@see XMLStyleSheet#getParentStyleSheet
	@version DOM Level 2
	@since DOM Level 2
	*/
	public CSSRule getOwnerRule() {return OwnerRule;}


	/**The list of all CSS rules, including both rule sets and at-rules.*/

	private XMLCSSRuleList CssRules=new XMLCSSRuleList();


	/**Returns the list of all CSS rules contained within the stylesheet. This
	includes both rule sets and at-rules.
	@return The list of all CSS rules, including both rule sets and at-rules.
	@version DOM Level 2
	@since DOM Level 2
	*/
	public CSSRuleList getCssRules() {return CssRules;}


	/**Inserts a new rule into the style sheet. The new rule now becomes part of
		the cascade.
	@param rule  The parsable text representing the rule. For rule sets this
		contains both the selector and the style declaration. For at-rules,
		this specifies both the at-identifier and the rule content.
	@param index   The index within the style sheet's rule list of the rule
		before which to insert the specified rule. If the  specified index is
		equal to the length of the style sheet's rule collection, the rule
		will be added to the end of the style sheet.
	@return  The index within the style sheet's rule collection of the newly
		inserted rule.
	@exception DOMException
	<ul>
		<li>HIERARCHY_REQUEST_ERR: Raised if the rule cannot be inserted at the
			specified index e.g. if an <code>@import</code> rule is inserted after
			a standard rule set or other at-rule.</li>
		<li>INDEX_SIZE_ERR: Raised if the specified index is not a valid
			insertion point.</li>
		<li>NO_MODIFICATION_ALLOWED_ERR: Raised if this style sheet is readonly.</li>
		<li>SYNTAX_ERR: Raised if the specified rule has a syntax error and is unparsable.</li>
	</ul>
	@version DOM Level 2
	@since DOM Level 2
	*/
	public int insertRule(String rule, int index) throws DOMException
	{
		return 0;	//G***fix
	}


	/**Delete a rule from the stylesheet.
	@param index The index within the style sheet's rule list of the rule to remove.
	@exception DOMException
	<ul>
		<li>INDEX_SIZE_ERR: Raised if the specified index does not correspond to a
			rule in the style sheet's rule list.</li>
		<li>NO_MODIFICATION_ALLOWED_ERR: Raised if this style sheet is readonly.<li>
	</ul>
	@version DOM Level 2
	@since DOM Level 2
	*/
	public void deleteRule(int index) throws DOMException
	{
		//G***check for read-only status
		try
		{
			((XMLCSSRuleList)getCssRules()).remove(index);	//remove the item at the specified index
		}
		catch(IndexOutOfBoundsException e)	//if they don't give a valid index
		{
			throw new XMLDOMException(DOMException.INDEX_SIZE_ERR, new Object[]{new Integer(index)});	//use our own type of exception to show that this index is out of bounds
		}
	}
}





