package com.garretwilson.text.xml.xlink;

import com.garretwilson.text.xml.XMLConstants;

/**Definitions for XLink defined by the W3C at
	http://www.w3.org/TR/xlink/ .
@author Garret Wilson
*/
public interface XLinkConstants
{

	/**The default XLink namespace prefix.*/
	public static final String XLINK_NAMESPACE_PREFIX="xlink";

	/**The XLink namespace URI.*/
	public static final String XLINK_NAMESPACE_URI="http://www.w3.org/1999/xlink";

//G***fix the constant names of all these

	/**The type attribute (without the namespace).*/
	public static final String TYPE="type";
	/**The type attribute (with the namespace).*/
	public static final String XLINK_TYPE=XLINK_NAMESPACE_PREFIX+XMLConstants.NAMESPACE_DIVIDER+TYPE;

	/**The href attribute (without the namespace).*/
	public static final String HREF="href";
	/**The href attribute (with the namespace).*/
	public static final String XLINK_HREF=XLINK_NAMESPACE_PREFIX+XMLConstants.NAMESPACE_DIVIDER+HREF;

	/**The role attribute (without the namespace).*/
	public static final String ROLE="role";
	/**The role attribute (with the namespace).*/
	public static final String XLINK_ROLE=XLINK_NAMESPACE_PREFIX+XMLConstants.NAMESPACE_DIVIDER+ROLE;

	/**The title attribute (without the namespace).*/
	public static final String TITLE="title";
	/**The title attribute (with the namespace).*/
	public static final String XLINK_TITLE=XLINK_NAMESPACE_PREFIX+XMLConstants.NAMESPACE_DIVIDER+TITLE;

	/**The show attribute (without the namespace).*/
	public static final String SHOW="show";
	/**The show attribute (with the namespace).*/
	public static final String XLINK_SHOW=XLINK_NAMESPACE_PREFIX+XMLConstants.NAMESPACE_DIVIDER+SHOW;

	/**The actuate attribute (without the namespace).*/
	public static final String ACTUATE="actuate";
	/**The actuate attribute (with the namespace).*/
	public static final String XLINK_ACTUATE=XLINK_NAMESPACE_PREFIX+XMLConstants.NAMESPACE_DIVIDER+ACTUATE;

}
