package com.garretwilson.text.xml;

import java.io.IOException;
import com.garretwilson.io.ParseIOException;

/**General exception class for all XML parsing errors.
This inherits from IOException so that it may be thrown from a
<code>Reader.read()</code> function, for example.
@see IOException
*/
public class XMLParseException extends IOException	//G***it looks like we were going to make this descend from ParseIOException
{
	/**The base name used for getting resources.*/
	protected static final String RESOURCE_BUNDLE_BASE_NAME=XMLParseException.class.getPackage().getName()+".XMLParseExceptionResources";

	/**The index of the line on which the error occurred.
  @see CharIndex
  */
	private long LineIndex=-1;

		/**@return The index of the line on which the error occurred.
		@see getCharIndex
		*/
		public long getLineIndex() {return LineIndex;}

		/**Sets the index of the line on which the error occurred.
		@param lineIndex The new line index.
		@see #setCharIndex
		*/
		public void setLineIndex(final long lineIndex) {LineIndex=lineIndex;}

	/**The index of the character at which the error occurred on the current line.
	@see LineIndex
	*/
	private long CharIndex=-1;

		/**@return The index of the character at which the error occurred on the current line.
		@see getLineIndex
		*/
		public long getCharIndex()	{return CharIndex;}

		/**Sets the index of the character at which the error occurred on the current line.
		@param charIndex The new character index.
		@see #setLineIndex
		*/
		public void setCharIndex(final long charIndex) {CharIndex=charIndex;}

	/**The name of the source of this exception, such as a filename.*/
	private String SourceName="";

		/**@return The name of the source of this exception, such as a filename.*/
		public String getSourceName() {return SourceName;}

		/**Sets the name of the source of this exception, such as a filename.
		@param sourceName The new name of the sourceof the exception.
		*/
		public void setSourceName(final String sourceName) {SourceName=sourceName;}

	/**Default constructor for a generic parsing error.*/
	public XMLParseException()
	{
		super();
	}

	/**Constructor for a generic parsing error, along with a message.
	@param s The error message.
	*/
	public XMLParseException(String s)
	{
		super(s);
	}

	/**Constructor for a generic parsing error with error location specified.
	@param lineIndex The index of the line in which the error occurred.
	@param charIndex The index of the character at which the error occurred on the current line.
	@param sourceName The name of the source of the XML document (perhaps a filename).
	*/
	public XMLParseException(final long lineIndex, final long charIndex, final String sourceName)
	{
		super(sourceName+':'+lineIndex+':'+charIndex);
		setLineIndex(lineIndex);	//set the line index
		setCharIndex(charIndex);	//set the character index
		setSourceName(sourceName);	//set the source name
	}

	/**Constructor for a generic parsing error with error message and error location specified.
	@param s The error message.
	@param lineIndex The index of the line in which the error occurred.
	@param charIndex The index of the character at which the error occurred on the current line.
	@param sourceName The name of the source of the XML document (perhaps a filename).
	*/
	public XMLParseException(String s, final long lineIndex, final long charIndex, final String sourceName)
	{
		super(sourceName+':'+lineIndex+':'+charIndex+": "+s);
		setLineIndex(lineIndex);	//set the line index
		setCharIndex(charIndex);	//set the character index
		setSourceName(sourceName);	//set the source name
	}

	/**Constructor for an XML parse exception from a <code>ParseIOException</code>.
	@param parseIOException The <code>ParseIOException</code> from which to construct this exception.
	*/
	public XMLParseException(final ParseIOException parseIOException)
	{
		//do the default constructing with the data from the ParseIOException
		this(parseIOException.getLineIndex(), parseIOException.getCharIndex(), parseIOException.getSourceName());
	}

	//G***make this print Unicode codes for characters which may not be displayable.
	/**Converts a list of delimiter characters to a string with the characters in a list, each in a single quote.
	Whitespace characters besides space are displayed in their escaped form.
	@param delimiterChars A string with the delimiter characters to be converted to a string.
	*/
/*G***del if we don't need
	static public String convertDelimitersToMessage(final String delimiterChars)
	{
		String messageString="";	//this string will receive the message to return
		for(int i=0; i<delimiterChars.length(); ++i)	//look at each character in the string
		{
			messageString+='\'';	//add a single quote character
			switch(delimiterChars.charAt(i))	//see which character this is
			{
				case XMLProcessor.TAB_CHAR:
					messageString+="\\t";
					break;
				case XMLProcessor.CR_CHAR:
					messageString+="\\r";
					break;
      	case XMLProcessor.LF_CHAR:
        	messageString+="\\n";
          break;
        default:	//if we don't recognize the character
        	messageString+=delimiterChars.charAt(i);	//add it normally
          break;
      }
      if(i<delimiterChars.length()-1)	//if this isn't the last character in the string
      	messageString+="', ";	//show that there will be another character
      else	//if this is the last character in the string
      	messageString+="'";	//add just a single quote
    }
    return messageString;	//return the message string we constructed
	}
*/

	/**Converts an array of strings to a message with the strings separated by commas.
	@param stringArray An array of strings to be converted to a string.
	*/
	//G***convert the characters in these strings so that whitespace gets converted to characters
/*G***del if we don't need
	static public String convertStringsToMessage(final String[] stringArray)
	{
		String messageString="";	//this string will receive the message to return
		for(int i=0; i<stringArray.length; ++i)	//look at each string in the array
		{
			messageString+="\""+stringArray[i];	//add a double quote character followed by this string
			if(i<stringArray.length-1)	//if this isn't the last string in the array
				messageString+="\", ";	//show that there will be another string
			else	//if this is the last string in the array
				messageString+='"';	//add just a double quote
		}
		return messageString;	//return the message string we constructed
	}
*/

}

