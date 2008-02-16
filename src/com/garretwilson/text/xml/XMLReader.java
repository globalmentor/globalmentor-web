package com.garretwilson.text.xml;

import java.io.Reader;
import java.io.IOException;
import com.garretwilson.io.ParseReader;
//G***del import com.garretwilson.lang.StringManipulator;	//G***del when we don't need
import com.garretwilson.util.Debug;

import static com.garretwilson.text.xml.XML.*;

/**Class for parsing XML data from a stream.
This class lets <code>ParseReader</code> do most of the work, while overriding
<code>processBufferedData()</code> to convert all line-end markers to LF as
XML specifies.
@see com.garretwilson.io.ParseReader
*/
public class XMLReader extends ParseReader
{

	public boolean tidy=false;  //G***fix, probably by adding a listener to XMLProcessor or (maybe better) by an anonymous class in XMLProcessor

	/*Constructor that specifies an input stream.
	@param inStream The input stream that contains the XML data.
	*/
/*G***del
	public XMLReader(final InputStream inStream)
	{
		super(inStream);	//construct the base class
	}
*/

	/*Constructor that specifies another reader.
	@param reader The reader that contains the XML data.
	*/
	public XMLReader(final Reader reader)
	{
		super(reader);	//construct the base class
	}

	/*Constructor that specifies an input stream and a name.
	@param inStream The input stream that contains the XML data.
	@param name The name of the reader.
	*/
/*G***del
	public XMLReader(final InputStream inStream, final String name)
	{
		super(inStream, name);	//construct the base class
	}
*/

	/*Constructor that specifies another reader and a name.
	@param reader The reader that contains the XML data.
	@param name The name of the reader.
	*/
	public XMLReader(final Reader reader, final String name)
	{
		super(reader, name);	//construct the base class
	}

	/**Constructor to create a reader from the contents of a string.
	@param inString The string that should be used for input.
	@param name The name of the reader.
	@exception IOException Thrown when an I/O error occurs.
	*/
	public XMLReader(final String inString, final String name) throws IOException
	{
		super(inString, name);	//construct the base class
	}

	/*Constructor to create an <code>XMLReader</code> from another
		reader, along with several characters that have already been read.
		<code>prereadCharacters</code> must be less than or equal to the length of
		the buffer.
	@param inReader The reader that contains the data.
	@param prereadCharacters The characters that have already been read.
	@exception IOException Thrown if <code>prereadCharacters</code> is too long for the buffer.
	@see BufferedPushbackReader
	*/
	public XMLReader(final Reader inReader, final StringBuffer prereadCharacters) throws IOException
	{
		super(inReader, prereadCharacters);	//allow the super class to do the constructing
	}

	/*Constructor to create an <code>XMLReader</code> from another
		reader, along with a source object.
		<code>prereadCharacters</code> must be less than or equal to the length of
		the buffer.
	@param inReader The reader that contains the data.
	@param sourceObject The source of the data (e.g. a String, File, or URL).
	@exception IOException Thrown if <code>prereadCharacters</code> is too long for the buffer.
	@see BufferedPushbackReader
	*/
	public XMLReader(final Reader inReader, final Object sourceObject) throws IOException
	{
		super(inReader, sourceObject);	//allow the super class to do the constructing
	}

	/*Constructor to create an <code>XMLReader</code> from another
		reader, along with several characters that have already been read and a source
		object.
		<code>prereadCharacters</code> must be less than or equal to the length of
		the buffer.
	@param inReader The reader that contains the data.
	@param prereadCharacters The characters that have already been read.
	@param sourceObject The source of the data (e.g. a String, File, or URL).
	@exception IOException Thrown if <code>prereadCharacters</code> is too long for the buffer.
	@see BufferedPushbackReader
	*/
	public XMLReader(final Reader inReader, final StringBuffer prereadCharacters, final Object sourceObject) throws IOException
	{
		super(inReader, prereadCharacters, sourceObject);	//allow the super class to do the constructing
	}

	/**Constructor to create a reader from a URL.
	@param url The URL that should be used for input.
	@exception IOException Thrown when an I/O error occurs.
	*/
/*G***del if we can get away with it
	public XMLReader(final URL url) throws IOException
	{
		super(url);	//construct the parent class
//G***del System.out.println("XMLReader(URL)");	//G***del
	}
*/

	/*Constructor that specifies an input stream and a source object.
	@param inStream The input stream that contains the XML data.
	@param sourceObject The source of the data (e.g. a String, File, or URL).
	*/
/*G***del
	public XMLReader(final InputStream inStream, final Object sourceObject)
	{
		super(inStream, sourceObject);	//construct the parent class
	}
*/

	/**Creates a reader from the given filename relative to the given context object.
	@param contextObject The source context, such as a URL, or <code>null</code> if
		the filename should not be referenced from any object.
	@param filename The name of the file, either relative or absolute.
	@return A reader to read from the given file.
	@exception IOException Thrown if an I/O error occurred.
	@see File
	@see URL
	*/
/*G***del
	public XMLReader(Object contextObject, final String filename) throws IOException
	{
		this(URLUtilities.createURL(contextObject, filename));	//create an XML reader with a URL created from the context object and filename
System.out.println("new reader URL: "+URLUtilities.createURL(contextObject, filename));	//G***del

			//G***check for FileNotFoundException
	}
*/

	/**Creates a reader from the given filename relative to the given XML reader.
	@param contextXMLReader The reader with the source context.
	@param filename The name of the file, either relative or absolute.
	@return A reader to read from the given file.
	@exception IOException Thrown if an I/O error occurred.
	@see ParseReader#getSourceObject
	@see File
	@see URL
	*/
//G***del when not needed
/*G***del
	public XMLReader(final XMLReader contextXMLReader, final String filename) throws IOException
	{
		this(contextXMLReader.getSourceObject(), filename);	//create an XML reader with a URL created from the context object of the given reader and filename
	}
*/

	/**Creates a reader from the given filename relative to the given context object.
	@param contextObject The source context, such as a URL, or <code>null</code> if
		the filename should not be referenced from any object.
	@param filename The name of the file, either relative or absolute.
	@return A reader to read from the given file.
	G***comment throws
	@see File
	@see URL
	*/
/*G***del
	public static XMLReader createXMLReader(Object contextObject, final String filename) throws IOException
	{
		URL newURL=null;	//we'll use this variable to store the new URL we create
		try
		{
			if(contextObject!=null)	//if we know where we're getting its data from
			{
				if(contextObject instanceof URL)	//if the data is coming from a URL
				{
//G***del System.out.println("Source object is a URL: "+(URL)sourceObject);	//G***del
					newURL=new URL((URL)contextObject, filename);	//create a new URL from the old one
//G***del System.out.println("created new URL: "+newURL);	//G***del
				}
			}
//G***del		InputStream inputStream;	//we don't know where our input stream is coming from, yet
			if(newURL==null)	//if we haven't found a URL, yet
				newURL=new URL(filename);	//try to create one directly from the filename they give us
		}
		catch(MalformedURLException e)	//if the location isn't a valid URL
		{
//G***del System.out.println(filename+" must be a file.");	//G***del
			newURL=new File(filename).toURL();	//create a file object and convert that to a URL
			//G***check for MalformedURLException
		}
		//G***do something if we can't read from the URL
//G***del System.out.println("Creating reader for URL: "+newURL);	//G***del
		return new XMLReader(newURL);	//create an XML reader that will read from the new URL
			//G***check for FileNotFoundException
	}
*/

/*G***del
		if(contextObject!=null)	//if they passed a valid context object
		{
			if(contextObject instanceof URL)	//if the context object is a URL
			{
//G***del				final contextURL=(URL)contextObject;	//cast the context object to a URL
				final URL url=new URL((URL)contextObject, filename);	//create a new URL from the old one, using the supplied filename
				return new XMLReader(url);	//create an XML reader that will read from the new URL
			}
			else if(contextObject instanceof File)	//if the context object is a file
			{
//G***del				final contextURL=(URL)contextObject;	//cast the context object to a URL

				final File file=new File((File)contextObject, filename);	//create a new file from the old one, using the supplied filename
				return new XMLReader(file);	//create an XML reader that will read from the new file
			}
		}
		return null;
	}
*/

	/**Creates a reader from the given filename relative to this reader's context object.
	@param filename The name of the file, either relative or absolute.
	@return A reader to read from the given file.
	G***comment throws
	@see #getSourceObject
	@see File
	@see URL
	*/
/*G***del
	public XMLReader createXMLReader(final String filename) throws IOException
	{
		return createXMLReader(getSourceObject(), filename);	//create a new reader based upon our context object (if any) and the filename
	}
*/

	/**Processes newly buffered data to convert all end-of-line indicators to LF
		as specified by XML. If the last character in the buffer's new data (before
		the fetch index) is  CR, we don't know if we should throw it away or replace
		it with an LF, so we'll move the fetch buffer back one so that it can
		be processed next time.
	@param newDataBeginIndex The starting index of the newly fetched data.
	@except IOException Thrown when an I/O error occurs.
	@see BufferedPushbackReader#getFetchBufferIndex
	*/
	protected void processBufferedData(final int newDataBeginIndex) throws IOException
	{
		super.processBufferedData(newDataBeginIndex);	//do the default processing of the data (this currently does nothing)
	//G***it might be a good idea to see if there's even any new data in the buffer at all
/*G***del
System.out.println("Getting ready to process buffered data.");
		String tempString=new String(getBuffer(), newDataBeginIndex, getBufferEndIndex()-newDataBeginIndex);	//G***del
		tempString=StringManipulator.replaceEvery(tempString, '\r', "\\r");	//G***del
		tempString=StringManipulator.replaceEvery(tempString, '\n', "\\n");	//G***del
		tempString=StringManipulator.replaceEvery(tempString, '\t', "\\t");	//G***del
System.out.println("Before processBufferedData: "+tempString);	//G***del
*/
		final char[] buffer=getBuffer();	//get a reference to our buffer
		{	//collapse CR+LF into LF
			final int bufferEndIndex=getFetchBufferIndex();	//find out the effective end of our new data
			int sourceIndex, destIndex;	//we'll start looking at the beginning of the new data
				//start at the beginning of the data and go to the end, copying data backwards to erase characters if needed
				//ignore the last character for the moment, giving us enough guaranteed room to look for CRLF at the end of the line
			for(sourceIndex=newDataBeginIndex, destIndex=newDataBeginIndex; sourceIndex<bufferEndIndex-1; ++sourceIndex, ++destIndex)	//G***check, comment
			{
				final char c=buffer[sourceIndex];	//see what character this is
				if(c==CR_CHAR)	//if this is a CR
				{
					if(buffer[sourceIndex+1]==LF_CHAR)	//if the next character is the second part of a CR/LF pair
						++sourceIndex;	//don't copy the CR part of the CR/LF (skip it)
					else	//if this is just a lone CR
					{
						buffer[destIndex]=LF_CHAR;	//convert the lone CR to an LF in the destination
						continue;	//skip ahead since we're already done our copy
					}
				}
				if(sourceIndex!=destIndex)	//if we've collapsed at least one CR/LF to an LF, we'll be copying information
					buffer[destIndex]=buffer[sourceIndex];	//copy this byte
			}
			if(sourceIndex<bufferEndIndex)	//if we haven't reached the end of the buffer (if the last two characters are "\r\n", we will have already finished all the characters)
			{
				final char c=buffer[sourceIndex];	//see what character the last character is
				if(c==CR_CHAR)	//if this is a CR
				{
					if(isLastBuffer())	//if we're out of data altogether
					{
						buffer[sourceIndex]=LF_CHAR;	//change the CR to an LF, because it can't be part of a CR/LF pair since there's no more data
					}
				}
				if(sourceIndex!=destIndex)	//if we've collapsed at least one CR/LF to an LF, we'll be copying information
					buffer[destIndex]=buffer[sourceIndex];	//copy this byte
				++sourceIndex;	//advance the source index
				++destIndex;	//advance the destination index
			}
			final int moveDistance=bufferEndIndex-destIndex;	//find out how far to move the buffer pointers back
			if(moveDistance!=0)	//if we have something to move
			{
				setBufferEndIndex(bufferEndIndex-moveDistance);	//show where the new end of the buffer is
				setFetchBufferIndex(getFetchBufferIndex()-moveDistance);	//move the fetch buffer index back as well (this may get readjusted even more, below)
			}
			final int newBufferEndIndex=getFetchBufferIndex();	//find out the effective end of our new data after collapsing CR+LF
			if(newBufferEndIndex>newDataBeginIndex && buffer[newBufferEndIndex-1]==CR_CHAR)	//if the buffer ends with CR, but there may be more data in another buffer
			{
				setBufferEndIndex(newBufferEndIndex-1);	//leave processing of the CR until the next buffer
			}			
		}
		final int bufferEndIndex=getFetchBufferIndex();	//find out the effective end of our new data after dealing with CR+LF
			//make sure each character is a valid XML character
		for(int charIndex=newDataBeginIndex; charIndex<bufferEndIndex; ++charIndex)	//look at each character in the buffer
		{
//G***del Debug.trace(String.valueOf(buffer[charIndex]));  //G***del
			if(!XMLUtilities.isChar(buffer[charIndex]))	//if the character isn't a valid character
			{
				if(tidy)  //if tidying is enabled G***fix
					buffer[charIndex]=' ';  //replace the invalid character with a space G***use a constant
				else  //if tidying is not enabled, report an error for the invalid character
					throw new XMLWellFormednessException(XMLWellFormednessException.INVALID_CHARACTER, new Object[]{"#x"+Integer.toHexString((int)buffer[charIndex])}, getLineIndex(charIndex), getCharIndex(charIndex), getName());	//show that this character reference is invalid G***tell exactly where the character appears in the file
			}
		}
	}

}


