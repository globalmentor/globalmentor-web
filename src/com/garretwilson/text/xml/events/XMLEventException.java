package com.garretwilson.text.xml.events;

import java.util.ResourceBundle;
import java.text.MessageFormat;
import org.w3c.dom.events.EventException;

/**Exception class for all XML DOM event errors.
@author Garret Wilson
*/
public class XMLEventException extends EventException
{
	/**The base name used for getting resources.*/	//G***move this to XMLException, make XMLException an interface, and implement that interface here
	protected static final String RESOURCE_BUNDLE_BASE_NAME=XMLEventConstants.PACKAGE_NAME+".XMLEventExceptionResources";	//G***implement this resource file

	/**The prefix used as a prefix for all event error string resources.*/
	protected static final String RESOURCE_PREFIX="Event";

	/**A constant identifying the name of the exception in the resources.*/
	protected static final short ERROR_NAME_RESOURCE_ID=100;

	//Resource string pattern arguments:

	/**Event Error: UNSPECIFIED_EVENT_TYPE_ERR<br/>
	Arguments: None.
	*/

	/**Constructor for a DOM event error, with error code specified.
	@param code The type of error; one of the constants from org.w3c.dom.events.EventException.
	@param messageArguments The argument list for the error message.
	*/
	public XMLEventException(final short code, final Object[] messageArguments)
	{
		//format a message based upon their error type
		super(code, ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME).getString(RESOURCE_PREFIX+ERROR_NAME_RESOURCE_ID)+": "+MessageFormat.format(ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME).getString(RESOURCE_PREFIX+code), messageArguments));
	}
}
