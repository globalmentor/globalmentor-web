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

package com.globalmentor.text.xml.svg;

import java.net.URI;

import com.globalmentor.net.ContentType;
import com.globalmentor.text.xml.XML;

/**
 * Constants for SVG.
 * @author Garret Wilson
 * @see <a href="http://www.w3.org/Graphics/SVG/">Scalable Vector Graphics</a>
 * @see <a href="http://www.w3.org/QA/2002/04/valid-dtd-list.html">W3C QA - Recommended List of DTDs</a>
 */
public class SVG {

	/** The SVG image MIME subtype. */
	public static final String SVG_XML_SUBTYPE = "svg" + ContentType.SUBTYPE_SUFFIX_DELIMITER_CHAR + XML.XML_SUBTYPE_SUFFIX;

	/** The content type for SVG: <code>application/svg+xml</code>. */
	public static final ContentType SVG_CONTENT_TYPE = ContentType.create(ContentType.APPLICATION_PRIMARY_TYPE, SVG_XML_SUBTYPE);

	/** The recommended prefix to the SVG namespace. */
	public static final String SVG_NAMESPACE_PREFIX = "svg";

	/** The URI to the SVG namespace. */
	public static final URI SVG_NAMESPACE_URI = URI.create("http://www.w3.org/2000/svg");

	/** The public ID for the SVG 1.0 DTD. */
	public static final String SVG_1_0_PUBLIC_ID = "-//W3C//DTD SVG 1.0//EN";
	/** The system ID for the SVG 1.0 DTD. */
	public static final String SVG_1_0_SYSTEM_ID = "http://www.w3.org/TR/2001/REC-SVG-20010904/DTD/svg10.dtd";

	/** The public ID for the SVG 1.1 Full DTD. */
	public static final String SVG_1_1_FULL_PUBLIC_ID = "-//W3C//DTD SVG 1.1//EN";
	/** The system ID for the SVG 1.1 Full DTD. */
	public static final String SVG_1_1_FULL_SYSTEM_ID = "http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd";

	/** The public ID for the SVG 1.1 Basic DTD. */
	public static final String SVG_1_1_BASIC_PUBLIC_ID = "-//W3C//DTD SVG 1.1 Basic//EN";
	/** The system ID for the SVG 1.1 Basic DTD. */
	public static final String SVG_1_1_BASIC_SYSTEM_ID = "http://www.w3.org/Graphics/SVG/1.1/DTD/svg11-basic.dtd";

	/** The public ID for the SVG 1.1 Tiny DTD. */
	public static final String SVG_1_1_TINY_PUBLIC_ID = "-//W3C//DTD SVG 1.1 Tiny//EN";
	/** The system ID for the SVG 1.1 Tiny DTD. */
	public static final String SVG_1_1_TINY_SYSTEM_ID = "http://www.w3.org/Graphics/SVG/1.1/DTD/svg11-tiny.dtd";

	//The SVG document element names.
	public static final String ELEMENT_SVG = "svg";

}
