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
		{XML.RESOURCE_PREFIX+XML.ATTRIBUTE_RESOURCE_ID, "attribute"},
		{XML.RESOURCE_PREFIX+XML.DOCUMENT_TYPE_RESOURCE_ID, "document type"},
		{XML.RESOURCE_PREFIX+XML.ENTITY_DECLARATION_RESOURCE_ID, "entity declaration"},
		{XML.RESOURCE_PREFIX+XML.ELEMENT_RESOURCE_ID, "element"},
		{XML.RESOURCE_PREFIX+XML.CHARACTER_REFERENCE_RESOURCE_ID, "character reference"},
		{XML.RESOURCE_PREFIX+XML.UNKNOWN_RESOURCE_ID, "unknown"},
		{XML.RESOURCE_PREFIX+XML.ENTITY_REFERENCE_RESOURCE_ID, "entity reference"},
		{XML.RESOURCE_PREFIX+XML.PARAMETER_ENTITY_REFERENCE_RESOURCE_ID, "parameter entity reference"},
		{XML.RESOURCE_PREFIX+XML.EXTERNAL_ID_RESOURCE_ID, "external ID"},
		{XML.RESOURCE_PREFIX+XML.ENTITY_RESOURCE_ID, "entity"},
		{XML.RESOURCE_PREFIX+XML.MARKUP_RESOURCE_ID, "markup"},
		{XML.RESOURCE_PREFIX+XML.TAG_RESOURCE_ID, "tag"},
		{XML.RESOURCE_PREFIX+XML.XML_DECLARATION_RESOURCE_ID, "XML declaration"},
		{XML.RESOURCE_PREFIX+XML.CDATA_RESOURCE_ID, "CDATA"},
		{XML.RESOURCE_PREFIX+XML.COMMENT_RESOURCE_ID, "comment"},
		{XML.RESOURCE_PREFIX+XML.DOCUMENT_RESOURCE_ID, "document"},
		{XML.RESOURCE_PREFIX+XML.PROCESSING_INSTRUCTION_RESOURCE_ID, "processing instruction"}
	};
}

