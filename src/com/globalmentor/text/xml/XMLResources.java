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

import java.util.ListResourceBundle;

/**Resource bundle for XML.
@author Garret Wilson
@deprecated
*/
public class XMLResources extends ListResourceBundle
{

	/**The base name used for getting resources.*/
	public static final String RESOURCE_BUNDLE_BASE_NAME=XMLResources.class.getPackage().getName()+".XMLResources";
	/**The prefix for XML resource names.*/
	public final static String RESOURCE_PREFIX="XML";

	public Object[][] getContents() {return contents;}	//returns the error strings

	static final Object[][] contents=
	{
		//Names of XML objects
		{XMLResources.RESOURCE_PREFIX+XMLUtilities.ATTRIBUTE_RESOURCE_ID, "attribute"},
		{XMLResources.RESOURCE_PREFIX+XMLUtilities.DOCUMENT_TYPE_RESOURCE_ID, "document type"},
		{XMLResources.RESOURCE_PREFIX+XMLUtilities.ENTITY_DECLARATION_RESOURCE_ID, "entity declaration"},
		{XMLResources.RESOURCE_PREFIX+XMLUtilities.ELEMENT_RESOURCE_ID, "element"},
		{XMLResources.RESOURCE_PREFIX+XMLUtilities.CHARACTER_REFERENCE_RESOURCE_ID, "character reference"},
		{XMLResources.RESOURCE_PREFIX+XMLUtilities.UNKNOWN_RESOURCE_ID, "unknown"},
		{XMLResources.RESOURCE_PREFIX+XMLUtilities.ENTITY_REFERENCE_RESOURCE_ID, "entity reference"},
		{XMLResources.RESOURCE_PREFIX+XMLUtilities.PARAMETER_ENTITY_REFERENCE_RESOURCE_ID, "parameter entity reference"},
		{XMLResources.RESOURCE_PREFIX+XMLUtilities.EXTERNAL_ID_RESOURCE_ID, "external ID"},
		{XMLResources.RESOURCE_PREFIX+XMLUtilities.ENTITY_RESOURCE_ID, "entity"},
		{XMLResources.RESOURCE_PREFIX+XMLUtilities.MARKUP_RESOURCE_ID, "markup"},
		{XMLResources.RESOURCE_PREFIX+XMLUtilities.TAG_RESOURCE_ID, "tag"},
		{XMLResources.RESOURCE_PREFIX+XMLUtilities.XML_DECLARATION_RESOURCE_ID, "XML declaration"},
		{XMLResources.RESOURCE_PREFIX+XMLUtilities.CDATA_RESOURCE_ID, "CDATA"},
		{XMLResources.RESOURCE_PREFIX+XMLUtilities.COMMENT_RESOURCE_ID, "comment"},
		{XMLResources.RESOURCE_PREFIX+XMLUtilities.DOCUMENT_RESOURCE_ID, "document"},
		{XMLResources.RESOURCE_PREFIX+XMLUtilities.PROCESSING_INSTRUCTION_RESOURCE_ID, "processing instruction"}
	};
}

