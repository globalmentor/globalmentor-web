package com.garretwilson.text.xml.stylesheets.css;

import com.garretwilson.util.Debug;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.css.*;

//G***should we make this an inner class of XMLCSSSelector

/**A single selector with an optional name and an optional class, although one
of the two should be specified.
@see XMLCSSStyleRule
*/
public class XMLCSSSelector//G***fix, Cloneable
{
	/*Constructor which requires a tag name and a class.
	@param tagName The name of the tag this selector matches,
		or the empty string if no name is to be specified.
	@param tagClass The class of this selector, or the empty string if no class is to be specified.
	*/
	public XMLCSSSelector(final String tagName, final String tagClass)
	{
		TagName=tagName;	//set the tag name
		TagClass=tagClass;	//set the tag class
	}

	/*Constructor which requires a tag name.
	@param tagName The name of the tag this selector matches,
		or the empty string if no name is to be specified.
	*/
	public XMLCSSSelector(final String tagName)
	{
		this(tagName, "");	//construct this object with only a tag name
	}

	/**Determines whether, this selector applies to the specified element.
	@param The element whose name and class should match those contained in this selector.
	@see XMLCSSStyleRule#appliesTo
	*/
	public boolean appliesTo(Element element)
	{
//G***del Debug.trace("XMLCSSSelector checking to see if "+element.getNodeName()+" matches "+getCssText()); //G***del
			//G***later, add the CSS ID checking
		if(getTagName().length()==0 || element.getNodeName().equals(getTagName()))	//if the tag names match, or we don't have a tag name to match with (which means we'll be matching class only)
		{

//G***del Debug.trace("Element "+element.getNodeName()+" matched, now checking to see if class: "+element.getAttributeNS(null, "class")+" equals the tag we expect: "+getTagClass());	//G***del

			if(getTagClass().length()==0 || element.getAttributeNS(null, "class").equals(getTagClass()))	//if the class names match as well (or there isn't a specified class in this selector) G***use a constant here
				return true;
		}
		return false;	//if we get to this point, this selector doesn't apply to this element
	}

	/**The name of the tag this selector matches, or the empty string to match any name.*/
	private String TagName="";

	/**@return The name of the tag this selector matches, or the empty string to match any name.*/
	public String getTagName() {return TagName;}

	/**The class of the tag this selector matches, or the empty string to match any class.*/
	private String TagClass="";

	/**@return The class of the tag this selector matches, or the empty string to match any class.*/
	public String getTagClass() {return TagClass;}


	/**Returns the parsable textual representation of the selector in the
		form "tagName.className".
	@return The parsable textual representation of the selector.
	#see XMLCSSStyleRule#getSelectorText
	*/
	public String getCssText()
	{
		String cssText=getTagName();	//always include the tag name, even if this string is empty
		if(getTagClass().length()!=0)		//if a class is specified
			cssText+="."+getTagClass();			//add a separator period, along with the class
		return cssText;	//return the text we constructed
	}

}


