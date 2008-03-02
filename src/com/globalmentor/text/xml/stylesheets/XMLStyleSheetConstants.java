package com.globalmentor.text.xml.stylesheets;

/**Constants for linking an XML documents to a stylesheet according to
 <a href="http://www.w3.org/TR/xml-stylesheet">http://www.w3.org/TR/xml-stylesheet</a> .
@author Garret Wilson
*/
public class XMLStyleSheetConstants
{
		//stylesheet processing instruction
	/**The processing instruction for linking an XML document to a stylesheet according to
		<a href="http://www.w3.org/TR/xml-stylesheet">http://www.w3.org/TR/xml-stylesheet</a> .*/
	public final static String XML_STYLESHEET_PROCESSING_INSTRUCTION="xml-stylesheet";

		//pseudo attributes
	public final static String HREF_ATTRIBUTE="href";
	public final static String TYPE_ATTRIBUTE="type";
	public final static String TITLE_ATTRIBUTE="title";
	public final static String MEDIA_ATTRIBUTE="media";
	public final static String CHARSET_ATTRIBUTE="charset";
	public final static String ALTERNATE_ATTRIBUTE="alternate";
		//values for the "alternate" attribute
		public final static String ALTERNATE_YES="yes";
		public final static String ALTERNATE_NO="no";

}