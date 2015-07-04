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

package com.globalmentor.text.xml.schema;

import java.net.URI;

import com.globalmentor.net.URIs;

/**
 * The class which represents an XML schema by holding a collection of schema components.
 * @author Garret Wilson
 */
public class XMLSchema {

	/** The Default XML schema namespace prefix. */
	public static final String XML_SCHEMA_NAMESPACE_PREFIX = "xsd";
	/** The URI to the XML schema namespace. */
	public static final URI XML_SCHEMA_NAMESPACE_URI = URI.create("http://www.w3.org/2001/XMLSchema");

	//XML schema elements
	public static final String ELEMENT_ANNOTATION = "annotation";
	public static final String ELEMENT_APPINFO = "appinfo";
	public static final String ELEMENT_ATTRIBUTE_GROUP = "attributeGroup";
	public static final String ELEMENT_DOCUMENTATION = "documentation";
	public static final String ELEMENT_ELEMENT = "element";

	//XML schema attributes
	public static final String ATTRIBUTE_TARGET_NAMESPACE = "targetNamespace";
	public static final String ATTRIBUTE_NAME = "name";

	//XML schema datatype names TODO maybe put these in a separate datatype class
	/** The name for the base64 binary datatype. The fragment identifier of <code>http://www.w3.org/2001/XMLSchema#base64Binary</code>. */
	public static final String BASE64_BINARY_DATATYPE_NAME = "base64Binary";
	/** The name for the boolean datatype. The fragment identifier of <code>http://www.w3.org/2001/XMLSchema#boolean</code>. */
	public static final String BOOLEAN_DATATYPE_NAME = "boolean";
	/** The name for the byte datatype. The fragment identifier of <code>http://www.w3.org/2001/XMLSchema#byte</code>. */
	public static final String BYTE_DATATYPE_NAME = "byte";
	/** The name for the date datatype. The fragment identifier of <code>http://www.w3.org/2001/XMLSchema#date</code>. */
	public static final String DATE_DATATYPE_NAME = "date";
	/** The name for the date time datatype. The fragment identifier of <code>http://www.w3.org/2001/XMLSchema#dateTime</code>. */
	public static final String DATE_TIME_DATATYPE_NAME = "dateTime";
	/** The name for the decimal datatype. The fragment identifier of <code>http://www.w3.org/2001/XMLSchema#decimal</code>. */
	public static final String DECIMAL_DATATYPE_NAME = "decimal";
	/** The name for the double datatype. The fragment identifier of <code>http://www.w3.org/2001/XMLSchema#double</code>. */
	public static final String DOUBLE_DATATYPE_NAME = "double";
	/** The name for the duration datatype. The fragment identifier of <code>http://www.w3.org/2001/XMLSchema#duration</code>. */
	public static final String DURATION_DATATYPE_NAME = "duration";
	/** The name for the float datatype. The fragment identifier of <code>http://www.w3.org/2001/XMLSchema#float</code>. */
	public static final String FLOAT_DATATYPE_NAME = "float";
	/** The name for the int datatype. The fragment identifier of <code>http://www.w3.org/2001/XMLSchema#int</code>. */
	public static final String INT_DATATYPE_NAME = "int";
	/** The name for the integer datatype. The fragment identifier of <code>http://www.w3.org/2001/XMLSchema#integer</code>. */
	public static final String INTEGER_DATATYPE_NAME = "integer";
	/** The name for the long datatype. The fragment identifier of <code>http://www.w3.org/2001/XMLSchema#long</code>. */
	public static final String LONG_DATATYPE_NAME = "long";
	/** The name for the short datatype. The fragment identifier of <code>http://www.w3.org/2001/XMLSchema#short</code>. */
	public static final String SHORT_DATATYPE_NAME = "short";
	/** The name for the string datatype. The fragment identifier of <code>http://www.w3.org/2001/XMLSchema#string</code>. */
	public static final String STRING_DATATYPE_NAME = "string";
	/** The name for the time datatype. The fragment identifier of <code>http://www.w3.org/2001/XMLSchema#time</code>. */
	public static final String TIME_DATATYPE_NAME = "time";
	/** The name for the URI datatype. The fragment identifier of <code>http://www.w3.org/2001/XMLSchema#anyURI</code>. */
	public static final String URI_DATATYPE_NAME = "anyURI";
	//XML schema datatypes
	/** The base 64 binary datatype. */
	public static final URI BASE64_BINARY_DATATYPE_URI = URIs.resolveRawFragment(XML_SCHEMA_NAMESPACE_URI, BASE64_BINARY_DATATYPE_NAME);
	/** The boolean datatype. */
	public static final URI BOOLEAN_DATATYPE_URI = URIs.resolveRawFragment(XML_SCHEMA_NAMESPACE_URI, BOOLEAN_DATATYPE_NAME);
	/** The byte datatype. */
	public static final URI BYTE_DATATYPE_URI = URIs.resolveRawFragment(XML_SCHEMA_NAMESPACE_URI, BYTE_DATATYPE_NAME);
	/** The date datatype. */
	public static final URI DATE_DATATYPE_URI = URIs.resolveRawFragment(XML_SCHEMA_NAMESPACE_URI, DATE_DATATYPE_NAME);
	/** The date time datatype. */
	public static final URI DATE_TIME_DATATYPE_URI = URIs.resolveRawFragment(XML_SCHEMA_NAMESPACE_URI, DATE_TIME_DATATYPE_NAME);
	/** The decimal datatype. */
	public static final URI DECIMAL_DATATYPE_URI = URIs.resolveRawFragment(XML_SCHEMA_NAMESPACE_URI, DECIMAL_DATATYPE_NAME);
	/** The 64-bit double datatype. */
	public static final URI DOUBLE_DATATYPE_URI = URIs.resolveRawFragment(XML_SCHEMA_NAMESPACE_URI, DOUBLE_DATATYPE_NAME);
	/** The duration datatype. */
	public static final URI DURATION_DATATYPE_URI = URIs.resolveRawFragment(XML_SCHEMA_NAMESPACE_URI, DURATION_DATATYPE_NAME);
	/** The 32-bit float datatype. */
	public static final URI FLOAT_DATATYPE_URI = URIs.resolveRawFragment(XML_SCHEMA_NAMESPACE_URI, FLOAT_DATATYPE_NAME);
	/** The int datatype. */
	public static final URI INT_DATATYPE_URI = URIs.resolveRawFragment(XML_SCHEMA_NAMESPACE_URI, INT_DATATYPE_NAME);
	/** The integer datatype. */
	public static final URI INTEGER_DATATYPE_URI = URIs.resolveRawFragment(XML_SCHEMA_NAMESPACE_URI, INTEGER_DATATYPE_NAME);
	/** The long datatype. */
	public static final URI LONG_DATATYPE_URI = URIs.resolveRawFragment(XML_SCHEMA_NAMESPACE_URI, LONG_DATATYPE_NAME);
	/** The short datatype. */
	public static final URI SHORT_DATATYPE_URI = URIs.resolveRawFragment(XML_SCHEMA_NAMESPACE_URI, SHORT_DATATYPE_NAME);
	/** The string datatype. */
	public static final URI STRING_DATATYPE_URI = URIs.resolveRawFragment(XML_SCHEMA_NAMESPACE_URI, STRING_DATATYPE_NAME);
	/** The time datatype. */
	public static final URI TIME_DATATYPE_URI = URIs.resolveRawFragment(XML_SCHEMA_NAMESPACE_URI, TIME_DATATYPE_NAME);
	/** The URI datatype. */
	public static final URI URI_DATATYPE_URI = URIs.resolveRawFragment(XML_SCHEMA_NAMESPACE_URI, URI_DATATYPE_NAME);

}