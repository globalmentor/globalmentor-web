package com.globalmentor.text.xml.xlink;

import java.net.URI;

/**Definitions for XLink defined by the W3C at
	<a href="http://www.w3.org/TR/xlink/">http://www.w3.org/TR/xlink/</a>.
@author Garret Wilson
*/
public interface XLinkConstants
{

	/**The default XLink namespace prefix.*/
	public static final String XLINK_NAMESPACE_PREFIX="xlink";

	/**The XLink namespace URI.*/
	public static final URI XLINK_NAMESPACE_URI=URI.create("http://www.w3.org/1999/xlink");

	/**The type attribute (without the namespace).*/
	public static final String ATTRIBUTE_TYPE="type";
		/**The XLink simple link type.*/
		public final static String SIMPLE_TYPE="simple";
		/**The XLink extended link type.*/
		public final static String EXTENDED_TYPE="extended";

	/**The href attribute (without the namespace).*/
	public static final String ATTRIBUTE_HREF="href";

	/**The role attribute (without the namespace).*/
	public static final String ATTRIBUTE_ROLE="role";

	/**The title attribute (without the namespace).*/
	public static final String ATTRIBUTE_TITLE="title";

	/**The show attribute (without the namespace).*/
	public static final String ATTRIBUTE_SHOW="show";

	/**The actuate attribute (without the namespace).*/
	public static final String ATTRIBUTE_ACTUATE="actuate";

}
