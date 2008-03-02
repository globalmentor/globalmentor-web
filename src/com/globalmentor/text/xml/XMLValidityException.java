package com.globalmentor.text.xml;

import java.util.ResourceBundle;
import java.text.MessageFormat;

/**A validity constraint error.
@see XMLParseException
*/
public class XMLValidityException extends XMLParseException
{
	/**The prefix used as a prefix for all validity error string resources.*/
	protected static final String RESOURCE_PREFIX="VALID";

	/**A constant identifying the name of the exception in the resources.*/
	protected static final short ERROR_NAME_RESOURCE_ID=100;

	/**Validity Constraint: Root Element Type
	The Name in the document type declaration must match the element type of the root element.
	Arguments: Name of root element, name in the document type declaration.
	*/
	public static final short ROOT_ELEMENT_TYPE=0;	//G***update all these constants after we implement all of them

//G***put all the earlier constraints here

	/**Validity Constraint: Unique Element Type Declaration
	No element type may be declared more than once.
	Arguments: Name of duplicate element.
	*/
	public static final short UNIQUE_ELEMENT_TYPE_DECLARATION=1;	//G***update all these constants after we implement all of them

/*G***del
	protected static final String[] MESSAGE_PATTERNS=
	{
		"(Unique Element Type Declaration) Element type \"{0}\" has already been declared."
	};
*/

	/**Constructor for a validity error, along with error type and error location specified.
	@param errorType The type of error; one of the constants listed above.
	@param messageArguments The argument list for the error message.
	@param lineIndex The index of the line in which the error occurred.
	@param charIndex The index of the character at which the error occurred on the current line.
	@param sourceName The name of the source of the XML document (perhaps a filename).
	*/
	public XMLValidityException(final short errorType, final Object[] messageArguments, final long lineIndex, final long charIndex, final String sourceName)
	{
		//format a message based upon their error type
		super(ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME).getString(RESOURCE_PREFIX+ERROR_NAME_RESOURCE_ID)+": "+MessageFormat.format(ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME).getString(RESOURCE_PREFIX+errorType), messageArguments), lineIndex, charIndex, sourceName); //G***use constant here

	}

}

