package com.globalmentor.text.xml;

import java.util.ListResourceBundle;

/**Resource bundle for DOM exceptions.*/
public class XMLDOMExceptionResources extends ListResourceBundle
{
	public Object[][] getContents() {return contents;}	//returns the error strings

	static final Object[][] contents=
	{
		//DOM errors (DOMException)
				//error name
		{XMLDOMException.RESOURCE_PREFIX+XMLDOMException.ERROR_NAME_RESOURCE_ID, "XML DOM Error"},
				//error codes
		{XMLDOMException.RESOURCE_PREFIX+XMLDOMException.INDEX_SIZE_ERR, "(Index Size Error) The index or size {0} is negative or greater than the allowed value."},
		{XMLDOMException.RESOURCE_PREFIX+XMLDOMException.DOMSTRING_SIZE_ERR, "(DOMString Size Error) The specified range of text with length {0} cannot fit into a DOMString."},
		{XMLDOMException.RESOURCE_PREFIX+XMLDOMException.HIERARCHY_REQUEST_ERR, "(Hierarchy Request Error) The node \"{0}\" cannot be inserted at this location in the hierarchy."},
		{XMLDOMException.RESOURCE_PREFIX+XMLDOMException.WRONG_DOCUMENT_ERR, "(Wrong Document) The node \"{0}\" is not supported by document \"{1}\"."},
		{XMLDOMException.RESOURCE_PREFIX+XMLDOMException.INVALID_CHARACTER_ERR, "(Invalid Character) The character '{0}' is invalid or illegal."},
		{XMLDOMException.RESOURCE_PREFIX+XMLDOMException.NO_DATA_ALLOWED_ERR, "(No Data Allowed) The node \"{0}\" does not support data."},
		{XMLDOMException.RESOURCE_PREFIX+XMLDOMException.NO_MODIFICATION_ALLOWED_ERR, "(No Modification Allowed) The node \"{0}\" is read-only and cannot be modified."},
		{XMLDOMException.RESOURCE_PREFIX+XMLDOMException.NOT_FOUND_ERR, "(Not Found) The node \"{0}\" does not exist in this context."},
		{XMLDOMException.RESOURCE_PREFIX+XMLDOMException.NOT_SUPPORTED_ERR, "(Not Supported) This object or operation \"{0}\" is not supported in this implementation."},
		{XMLDOMException.RESOURCE_PREFIX+XMLDOMException.INUSE_ATTRIBUTE_ERR, "(In-Use Attribute) The attribute \"{0}\" is already in use."},
		{XMLDOMException.RESOURCE_PREFIX+XMLDOMException.INVALID_STATE_ERR, "(Invalid State) The object \"{0}\" is not usable in this state."},
		{XMLDOMException.RESOURCE_PREFIX+XMLDOMException.SYNTAX_ERR, "(Syntax Error) The string \"{0}\" cannot be parsed."},
		{XMLDOMException.RESOURCE_PREFIX+XMLDOMException.INVALID_MODIFICATION_ERR, "(Invalid Modification) An invalid modification attempt was made."},
		{XMLDOMException.RESOURCE_PREFIX+XMLDOMException.NAMESPACE_ERR, "(Namespace Error) The operation is not compatible with namespace \"{0}\"."},
		{XMLDOMException.RESOURCE_PREFIX+XMLDOMException.INVALID_ACCESS_ERR, "(Invalid Access) The parameter or operation \"{0}\" is not supported by the underlying object."}
	};
}

