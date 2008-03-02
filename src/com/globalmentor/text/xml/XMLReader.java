/*
 * Copyright © 1996-2008 GlobalMentor, Inc. <http://www.globalmentor.com/>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.globalmentor.text.xml;

import java.io.Reader;
import java.io.IOException;

import com.globalmentor.io.ParseReader;
import static com.globalmentor.text.xml.XMLUtilities.*;

/**Class for parsing XML data from a stream.
This class lets <code>ParseReader</code> do most of the work, while overriding
<code>processBufferedData()</code> to convert all line-end markers to LF as
XML specifies.
@author Garret Wilson
@see com.globalmentor.io.ParseReader
@deprecated
*/
public class XMLReader extends ParseReader
{

	public boolean tidy=false;  //TODO fix, probably by adding a listener to XMLProcessor or (maybe better) by an anonymous class in XMLProcessor

	/**Constructor that specifies another reader.
	@param reader The reader that contains the XML data.
	*/
	public XMLReader(final Reader reader)
	{
		super(reader);	//construct the base class
	}

	/**Constructor that specifies another reader and a name.
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
	//TODO it might be a good idea to see if there's even any new data in the buffer at all
		final char[] buffer=getBuffer();	//get a reference to our buffer
		{	//collapse CR+LF into LF
			final int bufferEndIndex=getFetchBufferIndex();	//find out the effective end of our new data
			int sourceIndex, destIndex;	//we'll start looking at the beginning of the new data
				//start at the beginning of the data and go to the end, copying data backwards to erase characters if needed
				//ignore the last character for the moment, giving us enough guaranteed room to look for CRLF at the end of the line
			for(sourceIndex=newDataBeginIndex, destIndex=newDataBeginIndex; sourceIndex<bufferEndIndex-1; ++sourceIndex, ++destIndex)	//TODO check, comment
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
			if(!isChar(buffer[charIndex]))	//if the character isn't a valid character
			{
				if(tidy)  //if tidying is enabled G***fix
					buffer[charIndex]=' ';  //replace the invalid character with a space G***use a constant
				else  //if tidying is not enabled, report an error for the invalid character
					throw new XMLWellFormednessException(XMLWellFormednessException.INVALID_CHARACTER, new Object[]{"#x"+Integer.toHexString((int)buffer[charIndex])}, getLineIndex(charIndex), getCharIndex(charIndex), getName());	//show that this character reference is invalid G***tell exactly where the character appears in the file
			}
		}
	}

}


