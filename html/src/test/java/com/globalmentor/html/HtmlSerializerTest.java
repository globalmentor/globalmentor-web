/*
 * Copyright © 1996-2019 GlobalMentor, Inc. <http://www.globalmentor.com/>
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

package com.globalmentor.html;

import static com.globalmentor.io.Readers.*;
import static com.globalmentor.xml.XmlDom.*;
import static java.nio.charset.StandardCharsets.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.io.*;
import java.nio.charset.StandardCharsets;

import javax.annotation.*;

import org.junit.jupiter.api.*;
import org.w3c.dom.Document;

import com.globalmentor.xml.XmlFormatProfile;

/**
 * Tests of {@link HtmlSerializer} with {@link DefaultHtmlFormatProfile#INSTANCE}.
 * @author Garret Wilson
 */
public class HtmlSerializerTest {

	private static final String HTML5_FORMATTED_RESOURCE_NAME = "html5-formatted.html";
	private static final String HTML5_UNFORMATTED_RESOURCE_NAME = "html5-unformatted.html";

	@Test
	public void testHtml5Example() throws IOException {
		final String unformatted = readString(
				new InputStreamReader(new BufferedInputStream(getClass().getResourceAsStream(HTML5_UNFORMATTED_RESOURCE_NAME)), UTF_8));
		final String formatted = reformat(unformatted, DefaultHtmlFormatProfile.INSTANCE, "\n", "\t", true);
		final String expected = readString(new InputStreamReader(new BufferedInputStream(getClass().getResourceAsStream(HTML5_FORMATTED_RESOURCE_NAME)), UTF_8))
				//normalize line endings in because the formatted resource might be stored in a Git repository on Windows 
				.replace("\r\n", "\n"); //TODO create a utility, probably in a system utility class, to normalize line endings; use in serializers as well
		assertThat(formatted, is(expected));
	}

	/**
	 * Parses and re-serializes an HTML document from a string.
	 * @implSpec This implementation delegates to {@link #reformat(String, XmlFormatProfile, String, String)}.
	 * @implSpec No prolog or ending newline is written.
	 * @implSpec A single newline character <code>'\n'</code> is used as a line separator.
	 * @implSpec A single tab character <code>'\t'</code> is used as a horizontal aligner.
	 * @param text The text to parse and re-serialize.
	 * @param formatProfile The formatting characterization of the document.
	 * @return The re-serialized form of the HTML document.
	 * @throws IOException if an error occurs parsing or serializing the document.
	 */
	public static String reformat(@Nonnull final String text, @Nonnull final XmlFormatProfile formatProfile) throws IOException {
		return reformat(text, formatProfile, "\n", "\t");
	}

	/**
	 * Parses and re-serializes an HTML document from a string.
	 * @implSpec This implementation delegates to {@link #reformat(String, XmlFormatProfile, String, String, boolean)}.
	 * @implSpec No ending newline is written.
	 * @param text The text to parse and re-serialize.
	 * @param formatProfile The formatting characterization of the document.
	 * @param lineSeparator The newline delimiter to use.
	 * @param horizontalAligner The horizontal alignment character sequence.
	 * @return The re-serialized form of the HTML document.
	 * @throws IOException if an error occurs parsing or serializing the document.
	 */
	public static String reformat(@Nonnull final String text, @Nonnull final XmlFormatProfile formatProfile, @Nonnull final String lineSeparator,
			@Nonnull final String horizontalAligner) throws IOException {
		return reformat(text, formatProfile, lineSeparator, horizontalAligner, false);
	}

	/**
	 * Parses and re-serializes an HTML document from a string.
	 * @param text The text to parse and re-serialize.
	 * @param formatProfile The formatting characterization of the document.
	 * @param lineSeparator The newline delimiter to use.
	 * @param horizontalAligner The horizontal alignment character sequence.
	 * @param endNewline Whether an ending newline should be added.
	 * @return The re-serialized form of the HTML document.
	 * @throws IOException if an error occurs parsing or serializing the document.
	 */
	public static String reformat(@Nonnull final String text, @Nonnull final XmlFormatProfile formatProfile, @Nonnull final String lineSeparator,
			@Nonnull final String horizontalAligner, final boolean endNewline) throws IOException {
		final Document document = parse(new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8)), true);
		final HtmlSerializer serializer = new HtmlSerializer(true, formatProfile);
		serializer.setLineSeparator(lineSeparator);
		serializer.setHorizontalAligner(horizontalAligner);
		serializer.setFormatEndNewline(endNewline);
		return serializer.serialize(document);
	}

}
