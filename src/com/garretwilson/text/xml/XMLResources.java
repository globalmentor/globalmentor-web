package com.garretwilson.text.xml;

import java.util.ListResourceBundle;

/**Resource bundle for XML.*/
public class XMLResources extends ListResourceBundle
{
	/**A constant identifying the name of the exception in the resources.*/
//G***del	protected static final short ERROR_NAME_RESOURCE_ID=100;


	public Object[][] getContents() {return contents;}	//returns the error strings

	static final Object[][] contents=
	{
		//Names of XML objects
		{XMLConstants.RESOURCE_PREFIX+XMLConstants.ATTRIBUTE_RESOURCE, "attribute"},
		{XMLConstants.RESOURCE_PREFIX+XMLConstants.DOCUMENT_TYPE_RESOURCE, "document type"},
		{XMLConstants.RESOURCE_PREFIX+XMLConstants.ENTITY_DECLARATION_RESOURCE, "entity declaration"},
		{XMLConstants.RESOURCE_PREFIX+XMLConstants.ELEMENT_RESOURCE, "element"},
		{XMLConstants.RESOURCE_PREFIX+XMLConstants.CHARACTER_REFERENCE_RESOURCE, "character reference"},
		{XMLConstants.RESOURCE_PREFIX+XMLConstants.UNKNOWN_RESOURCE, "unknown"},
		{XMLConstants.RESOURCE_PREFIX+XMLConstants.ENTITY_REFERENCE_RESOURCE, "entity reference"},
		{XMLConstants.RESOURCE_PREFIX+XMLConstants.PARAMETER_ENTITY_REFERENCE_RESOURCE, "parameter entity reference"},
		{XMLConstants.RESOURCE_PREFIX+XMLConstants.EXTERNAL_ID_RESOURCE, "external ID"},
		{XMLConstants.RESOURCE_PREFIX+XMLConstants.ENTITY_RESOURCE, "entity"},
		{XMLConstants.RESOURCE_PREFIX+XMLConstants.MARKUP_RESOURCE, "markup"},
		{XMLConstants.RESOURCE_PREFIX+XMLConstants.TAG_RESOURCE, "tag"},
		{XMLConstants.RESOURCE_PREFIX+XMLConstants.XML_DECLARATION_RESOURCE, "XML declaration"},
		{XMLConstants.RESOURCE_PREFIX+XMLConstants.CDATA_RESOURCE, "CDATA"},
		{XMLConstants.RESOURCE_PREFIX+XMLConstants.COMMENT_RESOURCE, "comment"},
		{XMLConstants.RESOURCE_PREFIX+XMLConstants.DOCUMENT_RESOURCE, "document"},
		{XMLConstants.RESOURCE_PREFIX+XMLConstants.PROCESSING_INSTRUCTION_RESOURCE, "processing instruction"}
	};
}

