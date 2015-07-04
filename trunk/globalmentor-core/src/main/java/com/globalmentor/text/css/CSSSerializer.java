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

package com.globalmentor.text.css;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

import org.w3c.dom.css.*;

import static com.globalmentor.text.css.CSS.*;
import static java.nio.charset.StandardCharsets.*;

/**
 * Class which serializes a CSS stylesheet to a byte-oriented output stream. Has the option of automatically formatting the output in a hierarchical structure
 * with tabs or other strings.
 * @author Garret Wilson
 * @see XMLCSSProcessor
 */
public class CSSSerializer {	//TODO del all the XMLUndefinedEntityReferenceException throws when we don't need them anymore, in favor of XMLWellFormednessException

	/**
	 * Sets the options based on the contents of the option properties.
	 * @param options The properties which contain the options.
	 */
	public void setOptions(final Properties options) {
	}

	/** Default constructor for formatted output. */
	public CSSSerializer() {
	}

	/**
	 * Constructor that specifies serialize options.
	 * @param options The options to use for serialization.
	 */
	public CSSSerializer(final Properties options) {
		setOptions(options); //set the options from the properties
	}

	/**
	 * Serializes the specified stylesheet to the given output stream using the UTF-8 encoding.
	 * @param document The CSS stylesheet to serialize.
	 * @param outputStream The stream into which the document should be serialized.
	 * @throws IOException Thrown if an I/O error occurred.
	 * @throws UnsupportedEncodingException Thrown if the UTF-8 encoding not recognized.
	 */
	public void serialize(final CSSStyleSheet stylesheet, final OutputStream outputStream) throws UnsupportedEncodingException, IOException {
		serialize(stylesheet, outputStream, UTF_8); //serialize the document, defaulting to UTF-8
	}

	/**
	 * Serializes the specified stylesheet to the given output stream using the specified encoding.
	 * @param document The CSS stylesheet to serialize.
	 * @param outputStream The stream into which the document should be serialized.
	 * @param charset The encoding format to use when serializing.
	 * @throws IOException Thrown if an I/O error occurred.
	 * @throws UnsupportedEncodingException Thrown if the specified encoding is not recognized.
	 */
	public void serialize(final CSSStyleSheet stylesheet, final OutputStream outputStream, final Charset charset) throws IOException,
			UnsupportedEncodingException {
		final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, charset)); //create a new writer based on our encoding
		//TODO fix		writeProlog(document, writer, encoding);	//write the prolog
		//TODO return whatever header stuff is needed
		final CSSRuleList cssRuleList = stylesheet.getCssRules(); //get the list of CSS rules
		final int stylesheetRuleCount = cssRuleList.getLength(); //find out how many rules there are
		for(int ruleIndex = 0; ruleIndex < stylesheetRuleCount; ++ruleIndex) { //look at each of the rules
			final CSSRule cssRule = cssRuleList.item(ruleIndex); //get this CSS rule
			write(cssRule, writer); //write the CSS rule
			writer.newLine(); //add a newline in the default format
		}
		writer.newLine(); //add a newline in the default format
		writer.flush(); //flush any data we've buffered
	}

	/**
	 * Serializes the specified CSS rule to the given writer.
	 * @param cssRule The CSS rule to serialize.
	 * @param writer The writer into which the style rule should be written.
	 * @throws IOException Thrown if an I/O error occurred.
	 */
	protected void write(final CSSRule cssRule, final BufferedWriter writer) throws IOException {
		if(cssRule instanceof CSSStyleRule) { //TODO fix
			final CSSStyleRule cssStyleRule = (CSSStyleRule)cssRule; //TODO fix
			writer.write(cssStyleRule.getSelectorText()); //write the selector text
			writer.newLine(); //write a newline
			writer.write(RULE_GROUP_START_CHAR); //start the rule group
			writer.newLine(); //write a newline
			write(cssStyleRule.getStyle(), writer); //write the style declaration
			writer.write(RULE_GROUP_END_CHAR); //end the rule group
			writer.newLine(); //write a newline
		}
	}

	/**
	 * Serializes the specified CSS style declaration to the given writer.
	 * @param cssStyleDeclaration The CSS style declaration to serialize.
	 * @param writer The writer into which the style declaration should be written.
	 * @throws IOException Thrown if an I/O error occurred.
	 */
	protected void write(final CSSStyleDeclaration cssStyleDeclaration, final BufferedWriter writer) throws IOException {
		final int propertyCount = cssStyleDeclaration.getLength(); //get the number of properties
		for(int i = 0; i < propertyCount; ++i) { //look at each property
			final String propertyName = cssStyleDeclaration.item(i); //get the name of this property
			writer.write(TAB_CHAR); //write a tab
			writer.write(propertyName); //write the property name
			writer.write(PROPERTY_DIVIDER_CHAR); //write the property divider character
			writer.write(SPACE_CHAR); //write a space
			writer.write(cssStyleDeclaration.getPropertyValue(propertyName)); //write the property value
			writer.write(DECLARATION_SEPARATOR_CHAR); //separate the declarations
			writer.newLine(); //write a newline
		}
	}
}
