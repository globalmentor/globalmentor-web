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

package com.globalmentor.text.xml.processor;

import java.io.IOException;

import com.globalmentor.io.ParseIOException;

/**
 * General exception class for all XML parsing errors. This inherits from IOException so that it may be thrown from a <code>Reader.read()</code> function, for
 * example.
 * @author Garret Wilson
 * @see IOException
 * @deprecated
 */
public class XMLParseException extends IOException { //TODO it looks like we were going to make this descend from ParseIOException

	/** The base name used for getting resources. */
	protected static final String RESOURCE_BUNDLE_BASE_NAME = XMLParseException.class.getPackage().getName() + ".XMLParseExceptionResources";

	/**
	 * The index of the line on which the error occurred.
	 * @see CharIndex
	 */
	private long LineIndex = -1;

	/**
	 * @return The index of the line on which the error occurred.
	 * @see getCharIndex
	 */
	public long getLineIndex() {
		return LineIndex;
	}

	/**
	 * Sets the index of the line on which the error occurred.
	 * @param lineIndex The new line index.
	 * @see #setCharIndex
	 */
	public void setLineIndex(final long lineIndex) {
		LineIndex = lineIndex;
	}

	/**
	 * The index of the character at which the error occurred on the current line.
	 * @see LineIndex
	 */
	private long CharIndex = -1;

	/**
	 * @return The index of the character at which the error occurred on the current line.
	 * @see getLineIndex
	 */
	public long getCharIndex() {
		return CharIndex;
	}

	/**
	 * Sets the index of the character at which the error occurred on the current line.
	 * @param charIndex The new character index.
	 * @see #setLineIndex
	 */
	public void setCharIndex(final long charIndex) {
		CharIndex = charIndex;
	}

	/** The name of the source of this exception, such as a filename. */
	private String SourceName = "";

	/** @return The name of the source of this exception, such as a filename. */
	public String getSourceName() {
		return SourceName;
	}

	/**
	 * Sets the name of the source of this exception, such as a filename.
	 * @param sourceName The new name of the sourceof the exception.
	 */
	public void setSourceName(final String sourceName) {
		SourceName = sourceName;
	}

	/** Default constructor for a generic parsing error. */
	public XMLParseException() {
		super();
	}

	/**
	 * Constructor for a generic parsing error, along with a message.
	 * @param s The error message.
	 */
	public XMLParseException(String s) {
		super(s);
	}

	/**
	 * Constructor for a generic parsing error with error location specified.
	 * @param lineIndex The index of the line in which the error occurred.
	 * @param charIndex The index of the character at which the error occurred on the current line.
	 * @param sourceName The name of the source of the XML document (perhaps a filename).
	 */
	public XMLParseException(final long lineIndex, final long charIndex, final String sourceName) {
		super(sourceName + ':' + lineIndex + ':' + charIndex);
		setLineIndex(lineIndex); //set the line index
		setCharIndex(charIndex); //set the character index
		setSourceName(sourceName); //set the source name
	}

	/**
	 * Constructor for a generic parsing error with error message and error location specified.
	 * @param s The error message.
	 * @param lineIndex The index of the line in which the error occurred.
	 * @param charIndex The index of the character at which the error occurred on the current line.
	 * @param sourceName The name of the source of the XML document (perhaps a filename).
	 */
	public XMLParseException(String s, final long lineIndex, final long charIndex, final String sourceName) {
		super(sourceName + ':' + lineIndex + ':' + charIndex + ": " + s);
		setLineIndex(lineIndex); //set the line index
		setCharIndex(charIndex); //set the character index
		setSourceName(sourceName); //set the source name
	}

	/**
	 * Constructor for an XML parse exception from a <code>ParseIOException</code>.
	 * @param parseIOException The <code>ParseIOException</code> from which to construct this exception.
	 */
	public XMLParseException(final ParseIOException parseIOException) {
		//do the default constructing with the data from the ParseIOException
		this(parseIOException.getLineIndex(), parseIOException.getCharIndex(), parseIOException.getSourceName());
	}

}
