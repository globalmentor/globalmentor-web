package com.garretwilson.text.xml;

import java.util.ResourceBundle;
import java.text.MessageFormat;

import com.globalmentor.io.ParseEOFException;
import com.globalmentor.io.ParseUnexpectedDataException;
import com.globalmentor.util.Debug;

/**A well-formedness constraint error.
@see XMLParseException
*/
public class XMLWellFormednessException extends XMLParseException
{
	/**The prefix used as a prefix for all wellformedness error string resources.*/
	protected static final String RESOURCE_PREFIX="WF";

	/**A constant identifying the name of the exception in the resources.*/
	protected static final short ERROR_NAME_RESOURCE_ID=100;

	/**Well-Formedness Constraint: PEs in Internal Subset
	In the internal DTD subset, parameter-entity references can occur only where
	markup declarations can occur, not within markup declarations.
G***fix	Arguments: name of beginning tag, name of ending tag.
	*/
	public static final short PE_IN_INTERNAL_SUBSET=0;	//G***fix

	/**Well-Formedness Constraint: Element Type Match<br/>
	The Name in an element's end-tag must match the element type in the start-tag.
	Arguments: Name of beginning tag, name of ending tag.
	*/
	public static final short ELEMENT_TYPE_MATCH=1;

	/**Well-Formedness Constraint: Unique Att Spec<br/>
	No attribute name may appear more than once in the same start-tag or empty-element tag.
	Arguments: Tag name, attribute name.
	*/
	public static final short UNIQUE_ATT_SPEC=2;

	/**Well-Formedness Constraint: No External Entity References<br/>
	Attribute values cannot contain direct or indirect entity references to external entities.
	Arguments: Attribute name, external entity name.
	*/
	public static final short NO_EXTERNAL_ENTITY_REFERENCES=3;

	/**Well-Formedness Constraint: No < in Attribute Values
	The replacement text of any entity referred to directly or indirectly in an attribute value (other than "&lt;") must not contain a <.
	Arguments: Attribute name.
	*/
	public static final short NO_LT_IN_ATTRIBUTE_VALUES=4;

	/**Well-Formedness Constraint: Legal Character
	Characters referred to using character references must match the production for Char.
	Arguments: Character reference string.
	*/
	public static final short LEGAL_CHARACTER=5;

	/**Well-Formedness Constraint: Entity Declared
	In a document without any DTD, a document with only an internal DTD subset
	which contains no parameter entity references, or a document with
	"standalone='yes'", the Name given in the entity reference must match that
	in an entity declaration, except that well-formed documents need not declare
	any of the following entities: amp, lt, gt, apos, quot.
	Arguments: Character reference name.
	*/
	public static final short ENTITY_DECLARED=6;

	/**Well-Formedness Constraint: Parsed Entity
	An entity reference must not contain the name of an unparsed entity.
	Arguments: Name of parsed entity.
	*/
	public static final short PARSED_ENTITY=7;

	/**Well-Formedness Constraint: No Recursion
	A parsed entity must not contain a recursive reference to itself, either directly or indirectly.
	Arguments: Name of entity reference.
	*/
	public static final short NO_RECURSION=8;

	/**Well-Formedness Constraint: In DTD
	Parameter-entity references may only appear in the DTD.
	This should never be used, because everywhere else besides the DTD parameter
	entity references are considered normal text.
	Arguments: Name of parameter entity reference.
	*/
	public static final short IN_DTD=9;

	/**Well-Formedness Constraint: EOF
	The end of the file was unexpectedly encountered while parsing data.
	Arguments: Type of object being processed, name of object.
	*/
	public static final short EOF=50;

	/**Well-Formedness Constraint: Unexpected Data
	Incorrect data was encountered.
	Arguments: String with expected data, string with data found.
	*/
	public static final short UNEXPECTED_DATA=51;

	/**Well-Formedness Constraint: Invalid Character
	An invalid character was encountered.
	Arguments: The invalid character.
	*/
	public static final short INVALID_CHARACTER=52;

	/**Well-Formedness Constraint: Invalid Name
	An invalid XML name was found.
	Arguments: The invalid name.
	*/
	public static final short INVALID_NAME=53;

	/**Well-Formedness Constraint: Invalid PI Target
	An invalid XML processing instruction target was found.
	Arguments: The invalid processing instruction target.
	*/
	public static final short INVALID_PI_TARGET=54;

	/**Well-Formedness Constraint: Invalid Format
	Invalid format or UTF-16 encoding was used without the Byte Order Mark #xFEFF.
	*/
	public static final short INVALID_FORMAT=55;

	/**Well-Formedness Constraint: Invalid encoding.
	The character encoding was unrecognized or an encoding besides UTF-8 was used
	with no "encoding" attribute.
	Arguments: The invalid encoding name.
	*/
	public static final short INVALID_ENCODING=56;

	/**Constructor for a well-formedness error with error location specified.
	@param lineIndex The index of the line in which the error occurred.
	@param charIndex The index of the character at which the error occurred on the current line.
	@param sourceName The name of the source of the XML document (perhaps a filename).
	*/
/*G***del
	public XMLWellFormednessException(final long lineIndex, final long charIndex, final String sourceName)
	{
		super("XML well-formedness constraint error.", lineIndex, charIndex, sourceName);
	}
*/

//G***del	protected ResourceBundle Resources=ResourceBundle.getBundle("XMLErrorResources");	//get the resources we should use G***use a constant here

	/**Constructor for a well-formedness error, along with error type and error location specified.
	@param errorType The type of error; one of the constants listed above.
	@param messageArguments The argument list for the error message.
	@param lineIndex The index of the line in which the error occurred.
	@param charIndex The index of the character at which the error occurred on the current line.
	@param sourceName The name of the source of the XML document (perhaps a filename).
	*/
	public XMLWellFormednessException(final short errorType, final Object[] messageArguments, final long lineIndex, final long charIndex, final String sourceName)
	{
		//format a message based upon their error type
		super(ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME).getString(RESOURCE_PREFIX+ERROR_NAME_RESOURCE_ID)+": "+MessageFormat.format(ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME).getString(RESOURCE_PREFIX+errorType), messageArguments), lineIndex, charIndex, sourceName);
	}

	/**Constructor for a well-formedness error from a <code>ParseEOFException</code>.
	@param parseEOFException The <code>ParseEOFException</code> from which to construct this exception.
	@param objectType The type of object (e.g. attrbute, entity) being processed in string form.
	@param objectName The name of the object being processed.
	*/
	public XMLWellFormednessException(final ParseEOFException parseEOFException, final String objectType, final String objectName)
	{
		//format a message for an unexpected end of file
		super(ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME).getString(RESOURCE_PREFIX+ERROR_NAME_RESOURCE_ID)+": "+MessageFormat.format(ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME).getString(RESOURCE_PREFIX+EOF), new Object[]{objectType, objectName}), parseEOFException.getLineIndex(), parseEOFException.getCharIndex(), parseEOFException.getSourceName());
		initCause(parseEOFException);	//show what caused this exception
	}

	/**Constructor for a well-formedness error from a <code>ParseUnexpectedDataException</code>.
	@param parseUnexpectedDataException The <code>ParseUnexpectedDataException</code> from which to construct this exception.
	*/
	public XMLWellFormednessException(final ParseUnexpectedDataException parseUnexpectedDataException)
	{
		//format a message for unexpected data
		super(ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME).getString(RESOURCE_PREFIX+ERROR_NAME_RESOURCE_ID)+": "+MessageFormat.format(ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME).getString(RESOURCE_PREFIX+UNEXPECTED_DATA), new Object[]{parseUnexpectedDataException.getExpectedMessage(), parseUnexpectedDataException.getFoundMessage()}), parseUnexpectedDataException.getLineIndex(), parseUnexpectedDataException.getCharIndex(), parseUnexpectedDataException.getSourceName());
		initCause(parseUnexpectedDataException);	//show what caused this exception
	}

}

