package com.garretwilson.text.xml.stylesheets;

/**Class that encapsulates necessary information for referencing a stylesheet.
@author Garret Wilson
*/
public class XMLStyleSheetDescriptor  //G***fix media type, title, etc.
{

	/**The reference to the stylesheet's location.*/
	private final String href;

		/**@return The reference to the stylesheet's location.*/
		public final String getHRef() {return href;}

	/**Constructs a description of a stylesheet.
	@param newHRef The reference to the stylesheet's location.
	*/
	public XMLStyleSheetDescriptor(final String newHRef)
	{
		href=newHRef; //set the href
	}
}