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

package com.globalmentor.mathml.def;

import java.net.URI;

import com.globalmentor.net.MediaType;
import com.globalmentor.xml.def.XML;

/**
 * Constants for MathML.
 * @author Garret Wilson
 * @see <a href="http://www.w3.org/TR/MathML2/">Mathematical Markup Language (MathML)</a>
 * @see <a href="http://www.w3.org/QA/2002/04/valid-dtd-list.html">W3C QA - Recommended List of DTDs</a>
 */
public class MathML {

	/** A MathML application. */
	public static final String MATHML_XML_SUBTYPE = "mathml" + MediaType.SUBTYPE_SUFFIX_DELIMITER_CHAR + XML.XML_SUBTYPE_SUFFIX;

	/** The media type for MathML: <code>application/mathml+xml</code>. */
	public static final MediaType MEDIA_TYPE = MediaType.of(MediaType.APPLICATION_PRIMARY_TYPE, MATHML_XML_SUBTYPE);

	/** The recommended prefix to the MathML namespace. */
	public static final String NAMESPACE_PREFIX = "mathml";

	/** The string representing the the MathML namespace. */
	public static final String NAMESPACE_URI_STRING = "http://www.w3.org/1998/Math/MathML";

	/** The URI to the MathML namespace. */
	public static final URI NAMESPACE_URI = URI.create(NAMESPACE_URI_STRING);

	/** The system ID for the MathML 1.01 DTD. */
	public static final String MATHML_1_01_SYSTEM_ID = "https://www.w3.org/Math/DTD/mathml1/mathml.dtd";

	/** The public ID for the MathML 2.0 DTD. */
	public static final String MATHML_2_0_PUBLIC_ID = "-//W3C//DTD MathML 2.0//EN";
	/** The system ID for the MathML 2.0 DTD. */
	public static final String MATHML_2_0_SYSTEM_ID = "https://www.w3.org/Math/DTD/mathml2/mathml2.dtd";

	/** The MathML document element names. */
	public static final String ELEMENT_MATHML = "mathml";

}
