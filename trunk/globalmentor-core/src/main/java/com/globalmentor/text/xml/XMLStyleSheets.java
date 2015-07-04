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

package com.globalmentor.text.xml;

/**
 * Constants for linking an XML documents to a stylesheet according to <a href="http://www.w3.org/TR/xml-stylesheet">http://www.w3.org/TR/xml-stylesheet</a>.
 * @author Garret Wilson
 * @see <a href="http://www.w3.org/TR/xml-stylesheet">Associating Style Sheets with XML documents</a>
 */
public class XMLStyleSheets {

	/**
	 * The processing instruction for linking an XML document to a stylesheet according to <a href="http://www.w3.org/TR/xml-stylesheet">Associating Style Sheets
	 * with XML documents</a>.
	 */
	public static final String XML_STYLESHEET_PROCESSING_INSTRUCTION = "xml-stylesheet";

	//pseudo attributes
	public static final String HREF_ATTRIBUTE = "href";
	public static final String TYPE_ATTRIBUTE = "type";
	public static final String TITLE_ATTRIBUTE = "title";
	public static final String MEDIA_ATTRIBUTE = "media";
	public static final String CHARSET_ATTRIBUTE = "charset";
	public static final String ALTERNATE_ATTRIBUTE = "alternate";
	//values for the "alternate" attribute
	public static final String ALTERNATE_YES = "yes";
	public static final String ALTERNATE_NO = "no";

}