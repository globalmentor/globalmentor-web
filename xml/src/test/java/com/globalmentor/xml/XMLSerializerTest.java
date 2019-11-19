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

package com.globalmentor.xml;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.io.IOException;

import org.junit.jupiter.api.*;

import com.globalmentor.java.Characters;

public class XMLSerializerTest {

	/**
	 * @see XMLSerializer#encodeContent(Appendable, CharSequence)
	 * @see XMLSerializer#setUsePredefinedEntities(PredefinedEntitiesUse)
	 * @see XMLSerializer.PredefinedEntitiesUse#ALWAYS
	 */
	@Test
	public void testSerializePredefinedEntitiesAlways() throws IOException {
		final XMLSerializer xmlSerializer = new XMLSerializer();
		xmlSerializer.setUsePredefinedEntities(XMLSerializer.PredefinedEntitiesUse.ALWAYS);
		assertThat(xmlSerializer.encodeContent(new StringBuilder(), "<>&'\"").toString(), is("&lt;&gt;&amp;&apos;&quot;"));
	}

	/**
	 * @see XMLSerializer#encodeContent(Appendable, CharSequence)
	 * @see XMLSerializer#setUsePredefinedEntities(PredefinedEntitiesUse)
	 * @see XMLSerializer.PredefinedEntitiesUse#AS_NEEDED
	 */
	@Test
	public void testSerializePredefinedEntitiesAsNeeded() throws IOException {
		final XMLSerializer xmlSerializer = new XMLSerializer();
		xmlSerializer.setUsePredefinedEntities(XMLSerializer.PredefinedEntitiesUse.AS_NEEDED);
		assertThat(xmlSerializer.encodeContent(new StringBuilder(), "<>&'\"").toString(), is("&lt;&gt;&amp;'\""));
	}

	/**
	 * @see XMLSerializer#encodeContent(Appendable, CharSequence, char)
	 * @see XMLSerializer#setUsePredefinedEntities(PredefinedEntitiesUse)
	 * @see XMLSerializer.PredefinedEntitiesUse#AS_NEEDED
	 */
	@Test
	public void testSerializePredefinedEntitiesAsNeededWithAposDelimiter() throws IOException {
		final XMLSerializer xmlSerializer = new XMLSerializer();
		xmlSerializer.setUsePredefinedEntities(XMLSerializer.PredefinedEntitiesUse.AS_NEEDED);
		assertThat(xmlSerializer.encodeContent(new StringBuilder(), "<>&'\"", '\'').toString(), is("&lt;&gt;&amp;&apos;\""));
	}

	/**
	 * @see XMLSerializer#encodeContent(Appendable, CharSequence, char)
	 * @see XMLSerializer#setUsePredefinedEntities(PredefinedEntitiesUse)
	 * @see XMLSerializer.PredefinedEntitiesUse#AS_NEEDED
	 */
	@Test
	public void testSerializePredefinedEntitiesAsNeededWithQuotDelimiter() throws IOException {
		final XMLSerializer xmlSerializer = new XMLSerializer();
		xmlSerializer.setUsePredefinedEntities(XMLSerializer.PredefinedEntitiesUse.AS_NEEDED);
		assertThat(xmlSerializer.encodeContent(new StringBuilder(), "<>&'\"", '"').toString(), is("&lt;&gt;&amp;'&quot;"));
	}

	/**
	 * @see XMLSerializer#encodeContent(Appendable, CharSequence)
	 * @see XMLSerializer#setUsePredefinedEntities(PredefinedEntitiesUse)
	 * @see XMLSerializer.PredefinedEntitiesUse#NEVER
	 */
	@Test
	public void testSerializePredefinedEntitiesNever() throws IOException {
		final XMLSerializer xmlSerializer = new XMLSerializer();
		xmlSerializer.setUsePredefinedEntities(XMLSerializer.PredefinedEntitiesUse.NEVER);
		assertThat(xmlSerializer.encodeContent(new StringBuilder(), "<>&'\"").toString(), is("&#x3C;&#x3E;&#x26;'\""));
	}

	/**
	 * @see XMLSerializer#encodeContent(Appendable, CharSequence, char)
	 * @see XMLSerializer#setUsePredefinedEntities(PredefinedEntitiesUse)
	 * @see XMLSerializer.PredefinedEntitiesUse#NEVER
	 */
	@Test
	public void testSerializePredefinedEntitiesNeverWithAposDelimiter() throws IOException {
		final XMLSerializer xmlSerializer = new XMLSerializer();
		xmlSerializer.setUsePredefinedEntities(XMLSerializer.PredefinedEntitiesUse.NEVER);
		assertThat(xmlSerializer.encodeContent(new StringBuilder(), "<>&'\"", '\'').toString(), is("&#x3C;&#x3E;&#x26;&#x27;\""));
	}

	/**
	 * @see XMLSerializer#encodeContent(Appendable, CharSequence, char)
	 * @see XMLSerializer#setUsePredefinedEntities(PredefinedEntitiesUse)
	 * @see XMLSerializer.PredefinedEntitiesUse#NEVER
	 */
	@Test
	public void testSerializePredefinedEntitiesNeverWithQuotDelimiter() throws IOException {
		final XMLSerializer xmlSerializer = new XMLSerializer();
		xmlSerializer.setUsePredefinedEntities(XMLSerializer.PredefinedEntitiesUse.NEVER);
		assertThat(xmlSerializer.encodeContent(new StringBuilder(), "<>&'\"", '"').toString(), is("&#x3C;&#x3E;&#x26;'&#x22;"));
	}

	/**
	 * @implSpec This test uses non-space characters for less ambiguity and visibility.
	 * @see XMLSerializer#normalizeSpace(CharSequence, Characters, char)
	 */
	@Test
	public void testNormalizeSpace() {
		final Characters spaceCharacters = Characters.of('X', 'Y', 'Z');

		//empty string
		assertThat(XMLSerializer.normalizeSpace("", spaceCharacters, 'X').toString(), is(""));
		assertThat(XMLSerializer.normalizeSpace("X", spaceCharacters, 'X').toString(), is(""));
		assertThat(XMLSerializer.normalizeSpace("XX", spaceCharacters, 'X').toString(), is(""));
		assertThat(XMLSerializer.normalizeSpace("XXX", spaceCharacters, 'X').toString(), is(""));
		assertThat(XMLSerializer.normalizeSpace("XXXX", spaceCharacters, 'X').toString(), is(""));
		assertThat(XMLSerializer.normalizeSpace("Y", spaceCharacters, 'X').toString(), is(""));
		assertThat(XMLSerializer.normalizeSpace("YY", spaceCharacters, 'X').toString(), is(""));
		assertThat(XMLSerializer.normalizeSpace("ZXXY", spaceCharacters, 'X').toString(), is(""));

		//nothing to normalize
		assertThat(XMLSerializer.normalizeSpace("a", spaceCharacters, 'X').toString(), is("a"));
		assertThat(XMLSerializer.normalizeSpace("aa", spaceCharacters, 'X').toString(), is("aa"));
		assertThat(XMLSerializer.normalizeSpace("aaa", spaceCharacters, 'X').toString(), is("aaa"));
		assertThat(XMLSerializer.normalizeSpace("ab", spaceCharacters, 'X').toString(), is("ab"));
		assertThat(XMLSerializer.normalizeSpace("abc", spaceCharacters, 'X').toString(), is("abc"));

		//beginning space
		assertThat(XMLSerializer.normalizeSpace("Xabc", spaceCharacters, 'X').toString(), is("abc"));
		assertThat(XMLSerializer.normalizeSpace("Yabc", spaceCharacters, 'X').toString(), is("abc"));
		assertThat(XMLSerializer.normalizeSpace("XYYZabc", spaceCharacters, 'X').toString(), is("abc"));

		//ending space
		assertThat(XMLSerializer.normalizeSpace("abcX", spaceCharacters, 'X').toString(), is("abc"));
		assertThat(XMLSerializer.normalizeSpace("abcY", spaceCharacters, 'X').toString(), is("abc"));
		assertThat(XMLSerializer.normalizeSpace("abcXYYZ", spaceCharacters, 'X').toString(), is("abc"));

		//beginning and ending space
		assertThat(XMLSerializer.normalizeSpace("XabcX", spaceCharacters, 'X').toString(), is("abc"));
		assertThat(XMLSerializer.normalizeSpace("XabcY", spaceCharacters, 'X').toString(), is("abc"));
		assertThat(XMLSerializer.normalizeSpace("XYZZabcZXYY", spaceCharacters, 'X').toString(), is("abc"));

		//single spaces not needing normalizing
		assertThat(XMLSerializer.normalizeSpace("aXb", spaceCharacters, 'X').toString(), is("aXb"));
		assertThat(XMLSerializer.normalizeSpace("aXbXc", spaceCharacters, 'X').toString(), is("aXbXc"));
		assertThat(XMLSerializer.normalizeSpace("abcXdeXfg", spaceCharacters, 'X').toString(), is("abcXdeXfg"));

		//single spaces needing normalizing
		assertThat(XMLSerializer.normalizeSpace("aYb", spaceCharacters, 'X').toString(), is("aXb"));
		assertThat(XMLSerializer.normalizeSpace("aXbYc", spaceCharacters, 'X').toString(), is("aXbXc"));
		assertThat(XMLSerializer.normalizeSpace("abcYdeZfg", spaceCharacters, 'X').toString(), is("abcXdeXfg"));

		//normalizing runs of spaces
		assertThat(XMLSerializer.normalizeSpace("aXXb", spaceCharacters, 'X').toString(), is("aXb"));
		assertThat(XMLSerializer.normalizeSpace("aXYb", spaceCharacters, 'X').toString(), is("aXb"));
		assertThat(XMLSerializer.normalizeSpace("aYZb", spaceCharacters, 'X').toString(), is("aXb"));
		assertThat(XMLSerializer.normalizeSpace("aXXXb", spaceCharacters, 'X').toString(), is("aXb"));
		assertThat(XMLSerializer.normalizeSpace("aXYYb", spaceCharacters, 'X').toString(), is("aXb"));
		assertThat(XMLSerializer.normalizeSpace("aXYZb", spaceCharacters, 'X').toString(), is("aXb"));
		assertThat(XMLSerializer.normalizeSpace("aYYZYb", spaceCharacters, 'X').toString(), is("aXb"));
		assertThat(XMLSerializer.normalizeSpace("abXcdeXYfghijYYZZZZZYYlmnopXYZqrstuXXXXvYYYwxyz", spaceCharacters, 'X').toString(),
				is("abXcdeXfghijXlmnopXqrstuXvXwxyz"));

		//beginning space, ending space, and space runs
		assertThat(XMLSerializer.normalizeSpace("ZZZYYXabXcdeXYfghijYYZZZZZYYlmnopXYZqrstuXXXXvYYYwxyzXXXYYZ", spaceCharacters, 'X').toString(),
				is("abXcdeXfghijXlmnopXqrstuXvXwxyz"));
	}
}
