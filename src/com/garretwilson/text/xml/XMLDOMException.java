package com.garretwilson.text.xml;

import java.util.ResourceBundle;
import java.text.MessageFormat;
import org.w3c.dom.DOMException;

/**Exception class for all XML DOM errors.
@see org.w3c.dom.DOMException
*/
public class XMLDOMException extends DOMException
{
	/**The base name used for getting resources.*/	//G***move this to XMLException, make XMLException an interface, and implement that interface here
	protected static final String RESOURCE_BUNDLE_BASE_NAME=XMLConstants.PACKAGE_NAME+".XMLDOMExceptionResources";	//G***implement this resource file

	/**The prefix used as a prefix for all DOM error string resources.*/
	protected static final String RESOURCE_PREFIX="DOM";

	/**A constant identifying the name of the exception in the resources.*/
	protected static final short ERROR_NAME_RESOURCE_ID=100;

	//Resource string pattern arguments:

	/**DOM Error: INDEX_SIZE_ERR<br/>
	Arguments: Invalid size or index.
	*/

	/**DOM Error: DOMSTRING_SIZE_ERR<br/>
	Arguments: Size of text.
	*/

	/**DOM Error: HIERARCHY_REQUEST_ERR<br/>
	Arguments: Name of node being inserted.
	*/

	/**DOM Error: WRONG_DOCUMENT_ERR<br/>
	Arguments: Name of node, name of document.
	*/

	/**DOM Error: INVALID_CHARACTER_ERR<br/>
	Arguments: The invalid character.
	*/

	/**DOM Error: NO_DATA_ALLOWED_ERR<br/>
	Arguments: Name of node.
	*/

	/**DOM Error: NO_MODIFICATION_ALLOWED_ERR<br/>
	Arguments: Name of read-only node.
	*/

	/**DOM Error: NOT_FOUND_ERR<br/>
	Arguments: Name of node or object.
	*/

	/**DOM Error: NOT_SUPPORTED_ERR<br/>
	Arguments: Name of the object or operation not supported.
	*/

	/**DOM Error: INUSE_ATTRIBUTE_ERR<br/>
	Arguments: Name of in-use attribute.
	*/

	/**DOM Error: INVALID_STATE_ERR<br/>
	Arguments: Name of unusable object.
	*/

	/**DOM Error: SYNTAX_ERR<br/>
	Arguments: String that cannot be parsed.
	*/

	/**DOM Error: INVALID_MODIFICATION_ERR<br/>
	Arguments: None.
	*/

	/**DOM Error: NAMESPACE_ERR<br/>
	Arguments: Name of namespace.
	*/

	/**DOM Error: INVALID_ACCESS_ERR<br/>
	Arguments: Name of unsupported parameter or operation.
	*/

	/**Constructor for a DOM error, with error code specified.
	@param code The type of error; one of the constants from org.w3c.dom.DOMException.
	@param messageArguments The argument list for the error message.
	*/
	public XMLDOMException(final short code, final Object[] messageArguments)
	{
		//format a message based upon their error type
		super(code, ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME).getString(RESOURCE_PREFIX+ERROR_NAME_RESOURCE_ID)+": "+MessageFormat.format(ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME).getString(RESOURCE_PREFIX+code), messageArguments));
	}
}
