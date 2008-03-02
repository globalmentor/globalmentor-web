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

/**Resource bundle for XML parsing errors.
@author Garret Wilson
@see XMLWellFormednessException
@see XMLValidityException
@deprecated
*/
public class XMLParseExceptionResources extends ListResourceBundle
{
	public Object[][] getContents() {return contents;}	//returns the error strings

	static final Object[][] contents=
	{
		//Well-formedness errors (XMLWellFormednessException)
				//error name
		{XMLWellFormednessException.RESOURCE_PREFIX+XMLWellFormednessException.ERROR_NAME_RESOURCE_ID, "Well-Formedness Error"},
				//error types
		{XMLWellFormednessException.RESOURCE_PREFIX+XMLWellFormednessException.PE_IN_INTERNAL_SUBSET, "TODO fix for PE_IN_INTERNAL_SUBSET"},
		{XMLWellFormednessException.RESOURCE_PREFIX+XMLWellFormednessException.ELEMENT_TYPE_MATCH, "(Element Type Match) Element ending tag \"{1}\" does not match the beginning tag \"{0}\"."},
		{XMLWellFormednessException.RESOURCE_PREFIX+XMLWellFormednessException.UNIQUE_ATT_SPEC, "(Unique Att Spec) Attribute \"{1}\" already defined for tag \"{0}\"."},
		{XMLWellFormednessException.RESOURCE_PREFIX+XMLWellFormednessException.NO_EXTERNAL_ENTITY_REFERENCES, "(No External Entity References) Attribute \"{0}\" contains reference to external general entity \"{1}\"."},
		{XMLWellFormednessException.RESOURCE_PREFIX+XMLWellFormednessException.NO_LT_IN_ATTRIBUTE_VALUES, "(No < in Attribute Values) Attribute \"{0}\" contains the '<' character."},
		{XMLWellFormednessException.RESOURCE_PREFIX+XMLWellFormednessException.LEGAL_CHARACTER, "(Legal Character) Character reference \"{0}\" is not a legal XML character."},
		{XMLWellFormednessException.RESOURCE_PREFIX+XMLWellFormednessException.ENTITY_DECLARED, "(Entity Declared) The entity \"{0}\" has not been declared."},
		{XMLWellFormednessException.RESOURCE_PREFIX+XMLWellFormednessException.PARSED_ENTITY, "(Parsed Entity) The unparsed entity \"{0}\" cannot be referenced here."},
		{XMLWellFormednessException.RESOURCE_PREFIX+XMLWellFormednessException.NO_RECURSION, "(No Recursion) The entity \"{0}\" contains a recursive reference to itself."},
		{XMLWellFormednessException.RESOURCE_PREFIX+XMLWellFormednessException.IN_DTD, "(In DTD) The parameter entity \"{0}\" was referenced outside the document type declaration."},
		{XMLWellFormednessException.RESOURCE_PREFIX+XMLWellFormednessException.EOF, "(EOF) The end of the file was unexpectedly reached while processing {0} with name \"{1}\"."},
		{XMLWellFormednessException.RESOURCE_PREFIX+XMLWellFormednessException.UNEXPECTED_DATA, "(Unexpected Data) Expected {0}, found {1}."},
		{XMLWellFormednessException.RESOURCE_PREFIX+XMLWellFormednessException.INVALID_CHARACTER, "(Invalid Character) The character \"{0}\" is not a valid XML character."},
		{XMLWellFormednessException.RESOURCE_PREFIX+XMLWellFormednessException.INVALID_NAME, "(Invalid Name) The identifier \"{0}\" is not a valid XML name."},
		{XMLWellFormednessException.RESOURCE_PREFIX+XMLWellFormednessException.INVALID_PI_TARGET, "(Invalid PI Target) The identifier \"{0}\" is not a valid XML processing instruction target."},
		{XMLWellFormednessException.RESOURCE_PREFIX+XMLWellFormednessException.INVALID_FORMAT, "(Invalid Format) 	Invalid format or UTF-16 encoding was used without the Byte Order Mark #xFEFF."},
		{XMLWellFormednessException.RESOURCE_PREFIX+XMLWellFormednessException.INVALID_ENCODING, "(Invalid Encoding) 	The character encoding \"{0}\" was unrecognized or an encoding besides UTF-8 was used with no encoding attribute."},
		//Validity errors (XMLValidityException)
				//error name
		{XMLValidityException.RESOURCE_PREFIX+XMLValidityException.ERROR_NAME_RESOURCE_ID, "Validity Error"},
				//error types
		{XMLValidityException.RESOURCE_PREFIX+XMLValidityException.ROOT_ELEMENT_TYPE, "(Root Element Type) Element type of root element \"{0}\" does not match the name \"{1}\" in the document type declaration."},
		{XMLValidityException.RESOURCE_PREFIX+XMLValidityException.UNIQUE_ELEMENT_TYPE_DECLARATION, "(Unique Element Type Declaration) Element type \"{0}\" has already been declared."},
		//DOM errors (XMLDOMException)
				//error name
		{XMLDOMException.RESOURCE_PREFIX+XMLDOMException.ERROR_NAME_RESOURCE_ID, "XML DOM Error"},
				//error codes
		{XMLDOMException.RESOURCE_PREFIX+XMLDOMException.INDEX_SIZE_ERR, "(Index Size) Index or size [{0}] is negative or greater than the allowed value."},
		{XMLDOMException.RESOURCE_PREFIX+XMLDOMException.DOMSTRING_SIZE_ERR, "(DOM String Size) The length ({0}) of the specified range of text does not fit into a DOM String."},
		{XMLDOMException.RESOURCE_PREFIX+XMLDOMException.HIERARCHY_REQUEST_ERR, "(Hierarchy Request) The node \"{0}\" cannot be inserted at this location in the DOM tree."},
		{XMLDOMException.RESOURCE_PREFIX+XMLDOMException.WRONG_DOCUMENT_ERR, "(Wrong Document) The node \"{0}\" was not created by the document \"{1}\"."},
		{XMLDOMException.RESOURCE_PREFIX+XMLDOMException.INVALID_CHARACTER_ERR, "(Invalid Character) The character '{0}' is invalid in this context."},
		{XMLDOMException.RESOURCE_PREFIX+XMLDOMException.NO_DATA_ALLOWED_ERR, "(No Data Allowed) The node \"{0}\" does not support data."},
		{XMLDOMException.RESOURCE_PREFIX+XMLDOMException.NO_MODIFICATION_ALLOWED_ERR, "(No Modification Allowed) The object \"{0}\" cannot be modified."},
		{XMLDOMException.RESOURCE_PREFIX+XMLDOMException.NOT_FOUND_ERR, "(Not Found) The requested object \"{0}\" was not found."},
		{XMLDOMException.RESOURCE_PREFIX+XMLDOMException.NOT_SUPPORTED_ERR, "(Not Supported) This implementation does not support this request."},
		{XMLDOMException.RESOURCE_PREFIX+XMLDOMException.INUSE_ATTRIBUTE_ERR, "(In Use Attribute) The attribute \"{0}\" is already in use."}
	};
}

