package com.garretwilson.text.xml.stylesheets.css;

//G***del import java.util.Iterator;
import java.util.ArrayList;
import org.w3c.dom.css.CSSRule;

/**A list of CSS rules.
@see com.garretwilson.xml.XMLDocument
@see org.w3c.dom.css.CSSRule
*/
public class XMLCSSRuleList extends ArrayList implements org.w3c.dom.css.CSSRuleList
{

	/**Creates and returns a duplicate copy of this node list with no values.
	@return A duplicate "shallow clone" copy of this node list.
	@see XMLNode#cloneXMLNode
	@see XMLNode#cloneNode
	@see XMLNodeList#cloneDeep
	@see Object#clone
	*/
/*G***del if we don't need
	public Object clone()
	{
		return new XMLNodeList();	//create a new node list and return it
	}
*/

	/**Creates and returns a duplicate copy of this node list containing clones of all its children.
	@return A duplicate "deep clone" copy of this node list.
	@see XMLNode#cloneXMLNode
	@see XMLNode#cloneNode
	@see XMLNodeList#clone
	*/
/*G***del if we don't ned
	public XMLNodeList cloneDeep()
	{
		final XMLNodeList clone=(XMLNodeList)clone();	//create a new node list
		for(int i=0; i<size(); ++i)	//look at each node in our list
			clone.add(((XMLNode)get(i)).cloneXMLNode(true));	//deep clone this node and store it in our node list clone
		return clone;	//return our cloned node list
	}
*/

	/**Returns the number of reules in the list.
	The range of valid rule indices is 0 to <code>length-1</code> inclusive.
	@return The number of rules in the list.
	@version DOM Level 2
	@since DOM Level 2
	*/
	public int getLength() {return size();}

	/**Returns the <code>index</code>th item in the list. If
	<code>index</code> is greater than or equal to the number of CSS rules in
	the list, this returns <code>null</code>.
	@param index Index into the list.
	@return The CSS rule at the <code>index</code>th position in the
	list, or <code>null</code> if that is not a valid index.
	@version DOM Level 2
	@since DOM Level 2
	*/
	public CSSRule item(int index)
	{
		try
		{
			return (CSSRule)get(index);	//return the object at the index
		}
		catch(IndexOutOfBoundsException e)	//if they don't give a valid index
		{
			return null;	//return null instead of throwing an exception
		}
	}

}

