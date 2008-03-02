package com.globalmentor.text.xml;

/**A syntax error in an XML input stream.
@see XMLParseException
*/
public class XMLSyntaxException extends XMLParseException
{

	//G***eventually, delete all XMLSyntaxException constructors that don't take error locations

  /**Default constructor for a syntax error.*/
	public XMLSyntaxException() {super("XML syntax error.");}

  /**Constructor for a syntax error, along with a message.
  @param s The error message.
  */
	public XMLSyntaxException(final String message) {super(message);}

  /**Constructor for a syntax error with error location specified.
  @param lineIndex The index of the line in which the error occurred.
  @param charIndex The index of the character at which the error occurred on the current line.
	@param sourceName The name of the source of the XML document (perhaps a filename).
	*/
	public XMLSyntaxException(final long lineIndex, final long charIndex, final String sourceName)
	{
		super("XML syntax error.", lineIndex, charIndex, sourceName);
	}

	/**Constructor for a syntax error, along with error message and error location specified.
	@param s The error message.
	@param lineIndex The index of the line in which the error occurred.
	@param charIndex The index of the character at which the error occurred on the current line.
	@param sourceName The name of the source of the XML document (perhaps a filename).
	*/
	public XMLSyntaxException(final String message, final long lineIndex, final long charIndex, final String sourceName)
	{
		super(message, lineIndex, charIndex, sourceName);
	}

}

