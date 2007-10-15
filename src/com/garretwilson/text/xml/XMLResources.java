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
		{XMLConstants.RESOURCE_PREFIX+XMLConstants.ATTRIBUTE_RESOURCE_ID, "attribute"},
		{XMLConstants.RESOURCE_PREFIX+XMLConstants.DOCUMENT_TYPE_RESOURCE_ID, "document type"},
		{XMLConstants.RESOURCE_PREFIX+XMLConstants.ENTITY_DECLARATION_RESOURCE_ID, "entity declaration"},
		{XMLConstants.RESOURCE_PREFIX+XMLConstants.ELEMENT_RESOURCE_ID, "element"},
		{XMLConstants.RESOURCE_PREFIX+XMLConstants.CHARACTER_REFERENCE_RESOURCE_ID, "character reference"},
		{XMLConstants.RESOURCE_PREFIX+XMLConstants.UNKNOWN_RESOURCE_ID, "unknown"},
		{XMLConstants.RESOURCE_PREFIX+XMLConstants.ENTITY_REFERENCE_RESOURCE_ID, "entity reference"},
		{XMLConstants.RESOURCE_PREFIX+XMLConstants.PARAMETER_ENTITY_REFERENCE_RESOURCE_ID, "parameter entity reference"},
		{XMLConstants.RESOURCE_PREFIX+XMLConstants.EXTERNAL_ID_RESOURCE_ID, "external ID"},
		{XMLConstants.RESOURCE_PREFIX+XMLConstants.ENTITY_RESOURCE_ID, "entity"},
		{XMLConstants.RESOURCE_PREFIX+XMLConstants.MARKUP_RESOURCE_ID, "markup"},
		{XMLConstants.RESOURCE_PREFIX+XMLConstants.TAG_RESOURCE_ID, "tag"},
		{XMLConstants.RESOURCE_PREFIX+XMLConstants.XML_DECLARATION_RESOURCE_ID, "XML declaration"},
		{XMLConstants.RESOURCE_PREFIX+XMLConstants.CDATA_RESOURCE_ID, "CDATA"},
		{XMLConstants.RESOURCE_PREFIX+XMLConstants.COMMENT_RESOURCE_ID, "comment"},
		{XMLConstants.RESOURCE_PREFIX+XMLConstants.DOCUMENT_RESOURCE_ID, "document"},
		{XMLConstants.RESOURCE_PREFIX+XMLConstants.PROCESSING_INSTRUCTION_RESOURCE_ID, "processing instruction"}
	};
}

