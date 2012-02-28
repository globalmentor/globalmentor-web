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

package com.globalmentor.text.xml.xlink;

import java.net.URI;

import static com.globalmentor.text.xml.XML.*;

import org.w3c.dom.Element;

/**Definitions for XLink defined by the W3C at
	<a href="http://www.w3.org/TR/xlink/">XML Linking Language (XLink)</a>.
@author Garret Wilson
@see <a href="http://www.w3.org/TR/xlink/">XML Linking Language (XLink)</a>
*/
public class XLink
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

	/**Sets the <code>xlink:type</code> attribute of the given element.
	@param element The element for which the XLink attribute should be set.
	@param type The xlink:type value to use, either <code>XLink.SIMPLE_TYPE</code>
		or <code>XLink.EXTENDED_TYPE</code>.
	*/
	public static void setXLinkType(final Element element, final String type)
	{
		element.setAttributeNS(XLINK_NAMESPACE_URI.toString(), createQName(XLINK_NAMESPACE_PREFIX, ATTRIBUTE_TYPE), type);	//set xlink:type=type		
	}

	/**Sets the <code>xlink:href</code> attribute of the given element.
	@param element The element for which the XLink attribute should be set.
	@param href The xlink:href value to use.
	*/
	public static void setXLinkHRef(final Element element, final String href)
	{
		element.setAttributeNS(XLINK_NAMESPACE_URI.toString(), createQName(XLINK_NAMESPACE_PREFIX, ATTRIBUTE_HREF), href);	//set xlink:href=href		
	}

	/**Sets the <code>xlink:type</code> and <code>xlink:href</code> attributes
		of the given element.
	@param element The element for which the XLink attributes should be set.
	@param type The xlink:type value to use, either <code>XLink.SIMPLE_TYPE</code>
		or <code>XLink.EXTENDED_TYPE</code>.
	@param href The xlink:href value to use.
	*/
	public static void setXLink(final Element element, final String type, final String href)
	{
		setXLinkType(element, type);	//set the XLink type
		setXLinkHRef(element, href);	//set the XLink href
	}

}
