package com.garretwilson.text.xml.stylesheets.css;

import org.w3c.dom.DOMException;
import org.w3c.dom.css.*;

/**The class which forms a basis for all CSS rules, including rule sets and
at-rules.
@version DOM Level 2
@since DOM Level 2
@see org.w3c.dom.css.CSSRule
*/
public abstract class XMLCSSRule implements CSSRule//G***fix, Cloneable
{
	/*Constructor which requires a parent stylesheet to be specified.
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

	/**Sets the node type of this object. This is not a DOM function, but a
	protected function that allows derived objects to set the node type.
	@param nodeType The new node type.
	G***do we want to delete this, and make this have to be specified when the node is created?
	*/
//G***del		protected void setNodeType(final short nodeType) {NodeType=nodeType;}


	/**The parsable textual representation of the rule.*/
//G***fix	private String CssText="";

	/**Returns the parsable textual representation of the rule. This reflects the
	current state of the rule and not its initial value.
	@return The parsable textual representation of the rule.
	@version DOM Level 2
	@since DOM Level 2
	*/
//G***fix	public abstract String getCssText();

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
			//G***we need to just parse the text here, probably

		//G***check for syntax error
		//G***check for invalid modification
		//G***check for hierarchy request error
		//G***check for read-only status
//G***fix		CssText=cssText;	//set the text
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
//G***fix	void setOwnerXMLDocument(final XMLDocument ownerDocument) {OwnerDocument=ownerDocument;}

	/**Returns the document associated with this node, which was also in most
	circumstances the document which created this node. This will be <code>null</code>
	for documents or for document types which are not used with any document, yet.
	@return The document which owns this node, or <code>null</code> if there is no owner.
	@see XMLDocument
	@see XMLNode#getOwnerXMLDocument
	@version DOM Level 2
	*/
//G***fix	public Document getOwnerDocument() {return OwnerDocument;}

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


