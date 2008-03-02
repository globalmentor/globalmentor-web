package com.globalmentor.text.xml.stylesheets.css;

import org.w3c.dom.Element;
import org.w3c.dom.css.CSSStyleDeclaration;

/**An object that can associate CSS styles with XML elements.
@author Garret Wilson
*/
public interface CSSStyleManager
{

	/**Retrieves the style of the given element.
	@param element The element for which a style should be returned.
	@return A style for the given element, or <code>null</code> if the element
		has no style assigned to it.
	*/
	public CSSStyleDeclaration getStyle(final Element element);

	/**Sets the style of an element, replacing any existing style stored for the
		element.
	@param element The element for which a style is being specified.
	@param style The element style.
	*/
	public void setStyle(final Element element, final CSSStyleDeclaration style);

}