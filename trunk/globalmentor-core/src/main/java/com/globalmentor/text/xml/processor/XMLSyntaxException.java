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

/**
 * A syntax error in an XML input stream.
 * @author Garret Wilson
 * @see XMLParseException
 * @deprecated
 */
public class XMLSyntaxException extends XMLParseException {

	//TODO eventually, delete all XMLSyntaxException constructors that don't take error locations

	/** Default constructor for a syntax error. */
	public XMLSyntaxException() {
		super("XML syntax error.");
	}

	/**
	 * Constructor for a syntax error, along with a message.
	 * @param s The error message.
	 */
	public XMLSyntaxException(final String message) {
		super(message);
	}

	/**
	 * Constructor for a syntax error with error location specified.
	 * @param lineIndex The index of the line in which the error occurred.
	 * @param charIndex The index of the character at which the error occurred on the current line.
	 * @param sourceName The name of the source of the XML document (perhaps a filename).
	 */
	public XMLSyntaxException(final long lineIndex, final long charIndex, final String sourceName) {
		super("XML syntax error.", lineIndex, charIndex, sourceName);
	}

	/**
	 * Constructor for a syntax error, along with error message and error location specified.
	 * @param s The error message.
	 * @param lineIndex The index of the line in which the error occurred.
	 * @param charIndex The index of the character at which the error occurred on the current line.
	 * @param sourceName The name of the source of the XML document (perhaps a filename).
	 */
	public XMLSyntaxException(final String message, final long lineIndex, final long charIndex, final String sourceName) {
		super(message, lineIndex, charIndex, sourceName);
	}

}
