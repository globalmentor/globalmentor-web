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

import static com.globalmentor.xml.XmlDom.*;
import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Stream;

import javax.annotation.*;

import org.junit.jupiter.api.*;
import org.w3c.dom.*;

import com.globalmentor.java.Characters;
import com.globalmentor.xml.spec.NsName;
import com.globalmentor.xml.spec.XML;

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

	//formatting

	/**
	 * @implSpec This test uses non-space characters for less ambiguity and visibility.
	 * @see XMLSerializer#collapseRuns(CharSequence, Characters, char, boolean, boolean)
	 */
	@Test
	public void testCollapseRuns() {
		final Characters spaceCharacters = Characters.of('X', 'Y', 'Z');

		//empty string
		assertThat(XMLSerializer.collapseRuns("", spaceCharacters, 'X', false, false).toString(), is(""));
		assertThat(XMLSerializer.collapseRuns("", spaceCharacters, 'X', true, false).toString(), is(""));
		assertThat(XMLSerializer.collapseRuns("", spaceCharacters, 'X', false, true).toString(), is(""));
		assertThat(XMLSerializer.collapseRuns("", spaceCharacters, 'X', true, true).toString(), is(""));

		assertThat(XMLSerializer.collapseRuns("X", spaceCharacters, 'X', false, false).toString(), is("X"));
		assertThat(XMLSerializer.collapseRuns("X", spaceCharacters, 'X', true, false).toString(), is(""));
		assertThat(XMLSerializer.collapseRuns("X", spaceCharacters, 'X', false, true).toString(), is(""));
		assertThat(XMLSerializer.collapseRuns("X", spaceCharacters, 'X', true, true).toString(), is(""));

		assertThat(XMLSerializer.collapseRuns("XX", spaceCharacters, 'X', false, false).toString(), is("X"));
		assertThat(XMLSerializer.collapseRuns("XX", spaceCharacters, 'X', true, false).toString(), is(""));
		assertThat(XMLSerializer.collapseRuns("XX", spaceCharacters, 'X', false, true).toString(), is(""));
		assertThat(XMLSerializer.collapseRuns("XX", spaceCharacters, 'X', true, true).toString(), is(""));

		assertThat(XMLSerializer.collapseRuns("XXX", spaceCharacters, 'X', false, false).toString(), is("X"));
		assertThat(XMLSerializer.collapseRuns("XXX", spaceCharacters, 'X', true, false).toString(), is(""));
		assertThat(XMLSerializer.collapseRuns("XXX", spaceCharacters, 'X', false, true).toString(), is(""));
		assertThat(XMLSerializer.collapseRuns("XXX", spaceCharacters, 'X', true, true).toString(), is(""));

		assertThat(XMLSerializer.collapseRuns("XXXX", spaceCharacters, 'X', true, true).toString(), is(""));
		assertThat(XMLSerializer.collapseRuns("Y", spaceCharacters, 'X', true, true).toString(), is(""));
		assertThat(XMLSerializer.collapseRuns("YY", spaceCharacters, 'X', true, true).toString(), is(""));

		assertThat(XMLSerializer.collapseRuns("ZXXY", spaceCharacters, 'X', false, false).toString(), is("X"));
		assertThat(XMLSerializer.collapseRuns("ZXXY", spaceCharacters, 'X', true, false).toString(), is(""));
		assertThat(XMLSerializer.collapseRuns("ZXXY", spaceCharacters, 'X', false, true).toString(), is(""));
		assertThat(XMLSerializer.collapseRuns("ZXXY", spaceCharacters, 'X', true, true).toString(), is(""));

		//nothing to collapse or trim
		for(final boolean trimStart : List.of(false, true)) {
			for(final boolean trimEnd : List.of(false, true)) {
				assertThat(XMLSerializer.collapseRuns("a", spaceCharacters, 'X', trimStart, trimEnd).toString(), is("a"));
				assertThat(XMLSerializer.collapseRuns("aa", spaceCharacters, 'X', trimStart, trimEnd).toString(), is("aa"));
				assertThat(XMLSerializer.collapseRuns("aaa", spaceCharacters, 'X', trimStart, trimEnd).toString(), is("aaa"));
				assertThat(XMLSerializer.collapseRuns("ab", spaceCharacters, 'X', trimStart, trimEnd).toString(), is("ab"));
				assertThat(XMLSerializer.collapseRuns("abc", spaceCharacters, 'X', trimStart, trimEnd).toString(), is("abc"));
			}
		}

		//beginning space
		assertThat(XMLSerializer.collapseRuns("Xabc", spaceCharacters, 'X', false, false).toString(), is("Xabc"));
		assertThat(XMLSerializer.collapseRuns("Xabc", spaceCharacters, 'X', true, false).toString(), is("abc"));
		assertThat(XMLSerializer.collapseRuns("Xabc", spaceCharacters, 'X', false, true).toString(), is("Xabc"));
		assertThat(XMLSerializer.collapseRuns("Xabc", spaceCharacters, 'X', true, true).toString(), is("abc"));

		assertThat(XMLSerializer.collapseRuns("Yabc", spaceCharacters, 'X', false, false).toString(), is("Xabc"));
		assertThat(XMLSerializer.collapseRuns("Yabc", spaceCharacters, 'X', true, false).toString(), is("abc"));
		assertThat(XMLSerializer.collapseRuns("Yabc", spaceCharacters, 'X', false, true).toString(), is("Xabc"));
		assertThat(XMLSerializer.collapseRuns("Yabc", spaceCharacters, 'X', true, true).toString(), is("abc"));

		assertThat(XMLSerializer.collapseRuns("XYYZabc", spaceCharacters, 'X', false, false).toString(), is("Xabc"));
		assertThat(XMLSerializer.collapseRuns("XYYZabc", spaceCharacters, 'X', true, false).toString(), is("abc"));
		assertThat(XMLSerializer.collapseRuns("XYYZabc", spaceCharacters, 'X', false, true).toString(), is("Xabc"));
		assertThat(XMLSerializer.collapseRuns("XYYZabc", spaceCharacters, 'X', true, true).toString(), is("abc"));

		//ending space
		assertThat(XMLSerializer.collapseRuns("abcX", spaceCharacters, 'X', false, false).toString(), is("abcX"));
		assertThat(XMLSerializer.collapseRuns("abcX", spaceCharacters, 'X', true, false).toString(), is("abcX"));
		assertThat(XMLSerializer.collapseRuns("abcX", spaceCharacters, 'X', false, true).toString(), is("abc"));
		assertThat(XMLSerializer.collapseRuns("abcX", spaceCharacters, 'X', true, true).toString(), is("abc"));

		assertThat(XMLSerializer.collapseRuns("abcY", spaceCharacters, 'X', false, false).toString(), is("abcX"));
		assertThat(XMLSerializer.collapseRuns("abcY", spaceCharacters, 'X', true, false).toString(), is("abcX"));
		assertThat(XMLSerializer.collapseRuns("abcY", spaceCharacters, 'X', false, true).toString(), is("abc"));
		assertThat(XMLSerializer.collapseRuns("abcY", spaceCharacters, 'X', true, true).toString(), is("abc"));

		assertThat(XMLSerializer.collapseRuns("abcXYYZ", spaceCharacters, 'X', false, false).toString(), is("abcX"));
		assertThat(XMLSerializer.collapseRuns("abcXYYZ", spaceCharacters, 'X', true, false).toString(), is("abcX"));
		assertThat(XMLSerializer.collapseRuns("abcXYYZ", spaceCharacters, 'X', false, true).toString(), is("abc"));
		assertThat(XMLSerializer.collapseRuns("abcXYYZ", spaceCharacters, 'X', true, true).toString(), is("abc"));

		//beginning and ending space
		assertThat(XMLSerializer.collapseRuns("XabcX", spaceCharacters, 'X', false, false).toString(), is("XabcX"));
		assertThat(XMLSerializer.collapseRuns("XabcX", spaceCharacters, 'X', true, false).toString(), is("abcX"));
		assertThat(XMLSerializer.collapseRuns("XabcX", spaceCharacters, 'X', false, true).toString(), is("Xabc"));
		assertThat(XMLSerializer.collapseRuns("XabcX", spaceCharacters, 'X', true, true).toString(), is("abc"));

		assertThat(XMLSerializer.collapseRuns("XabcY", spaceCharacters, 'X', false, false).toString(), is("XabcX"));
		assertThat(XMLSerializer.collapseRuns("XabcY", spaceCharacters, 'X', true, false).toString(), is("abcX"));
		assertThat(XMLSerializer.collapseRuns("XabcY", spaceCharacters, 'X', false, true).toString(), is("Xabc"));
		assertThat(XMLSerializer.collapseRuns("XabcY", spaceCharacters, 'X', true, true).toString(), is("abc"));

		assertThat(XMLSerializer.collapseRuns("XYZZabcZXYY", spaceCharacters, 'X', false, false).toString(), is("XabcX"));
		assertThat(XMLSerializer.collapseRuns("XYZZabcZXYY", spaceCharacters, 'X', true, false).toString(), is("abcX"));
		assertThat(XMLSerializer.collapseRuns("XYZZabcZXYY", spaceCharacters, 'X', false, true).toString(), is("Xabc"));
		assertThat(XMLSerializer.collapseRuns("XYZZabcZXYY", spaceCharacters, 'X', true, true).toString(), is("abc"));

		for(final boolean trimStart : List.of(false, true)) {
			for(final boolean trimEnd : List.of(false, true)) {
				//single spaces not needing normalizing
				assertThat(XMLSerializer.collapseRuns("aXb", spaceCharacters, 'X', trimStart, trimEnd).toString(), is("aXb"));
				assertThat(XMLSerializer.collapseRuns("aXbXc", spaceCharacters, 'X', trimStart, trimEnd).toString(), is("aXbXc"));
				assertThat(XMLSerializer.collapseRuns("abcXdeXfg", spaceCharacters, 'X', trimStart, trimEnd).toString(), is("abcXdeXfg"));

				//single spaces needing normalizing
				assertThat(XMLSerializer.collapseRuns("aYb", spaceCharacters, 'X', trimStart, trimEnd).toString(), is("aXb"));
				assertThat(XMLSerializer.collapseRuns("aXbYc", spaceCharacters, 'X', trimStart, trimEnd).toString(), is("aXbXc"));
				assertThat(XMLSerializer.collapseRuns("abcYdeZfg", spaceCharacters, 'X', trimStart, trimEnd).toString(), is("abcXdeXfg"));

				//normalizing runs of spaces
				assertThat(XMLSerializer.collapseRuns("aXXb", spaceCharacters, 'X', trimStart, trimEnd).toString(), is("aXb"));
				assertThat(XMLSerializer.collapseRuns("aXYb", spaceCharacters, 'X', trimStart, trimEnd).toString(), is("aXb"));
				assertThat(XMLSerializer.collapseRuns("aYZb", spaceCharacters, 'X', trimStart, trimEnd).toString(), is("aXb"));
				assertThat(XMLSerializer.collapseRuns("aXXXb", spaceCharacters, 'X', trimStart, trimEnd).toString(), is("aXb"));
				assertThat(XMLSerializer.collapseRuns("aXYYb", spaceCharacters, 'X', trimStart, trimEnd).toString(), is("aXb"));
				assertThat(XMLSerializer.collapseRuns("aXYZb", spaceCharacters, 'X', trimStart, trimEnd).toString(), is("aXb"));
				assertThat(XMLSerializer.collapseRuns("aYYZYb", spaceCharacters, 'X', trimStart, trimEnd).toString(), is("aXb"));
				assertThat(XMLSerializer.collapseRuns("abXcdeXYfghijYYZZZZZYYlmnopXYZqrstuXXXXvYYYwxyz", spaceCharacters, 'X', trimStart, trimEnd).toString(),
						is("abXcdeXfghijXlmnopXqrstuXvXwxyz"));
			}
		}

		//beginning space, ending space, and space runs
		assertThat(XMLSerializer.collapseRuns("ZZZYYXabXcdeXYfghijYYZZZZZYYlmnopXYZqrstuXXXXvYYYwxyzXXXYYZ", spaceCharacters, 'X', false, false).toString(),
				is("XabXcdeXfghijXlmnopXqrstuXvXwxyzX"));
		assertThat(XMLSerializer.collapseRuns("ZZZYYXabXcdeXYfghijYYZZZZZYYlmnopXYZqrstuXXXXvYYYwxyzXXXYYZ", spaceCharacters, 'X', true, false).toString(),
				is("abXcdeXfghijXlmnopXqrstuXvXwxyzX"));
		assertThat(XMLSerializer.collapseRuns("ZZZYYXabXcdeXYfghijYYZZZZZYYlmnopXYZqrstuXXXXvYYYwxyzXXXYYZ", spaceCharacters, 'X', false, true).toString(),
				is("XabXcdeXfghijXlmnopXqrstuXvXwxyz"));
		assertThat(XMLSerializer.collapseRuns("ZZZYYXabXcdeXYfghijYYZZZZZYYlmnopXYZqrstuXXXXvYYYwxyzXXXYYZ", spaceCharacters, 'X', true, true).toString(),
				is("abXcdeXfghijXlmnopXqrstuXvXwxyz"));
	}

	/**
	 * A test XML profile.
	 * <dl>
	 * <dt>block elements</dt>
	 * <dd>{@code <block>}, {@code <flush>}, and {@code <pre>}</dd>
	 * <dt>preserved elements</dt>
	 * <dd>{@code <pre>}</dd>
	 * <dt>flush elements</dt>
	 * <dd>{@code <flush>}</dd>
	 * <dt>attribute order</dt>
	 * <dd>{@code <ordered>}, either with no namespace or in the <code>http://example/ns/</code> namespace: <code>foo</code>, <code>bar</code></dd>
	 * </dl>
	 */
	public static final XmlFormatProfile BLOCK_FLUSH_PRE_FORMAT_PROFILE = new SimpleXmlFormatProfile(XML.WHITESPACE_CHARACTERS,
			Set.of(NsName.of("block"), NsName.of("flush"), NsName.of("pre")), Set.of(NsName.of("pre"))) {

		private final NsName flushElement = NsName.of("flush");

		private final List<NsName> attributeOrder = List.of(NsName.of("foo"), NsName.of("bar"));

		@Override
		public boolean isFlush(final Element element) {
			return flushElement.matches(element);
		};

		@Override
		public List<NsName> getAttributeOrder(final Element element) {
			if((element.getNamespaceURI() == null || element.getNamespaceURI().equals("http://example/ns/")) && element.getLocalName().equals("ordered")) {
				return attributeOrder;
			}
			return emptyList();
		}
	};

	/** @see XmlFormatProfile#getSpaceNormalizationCharacters() */
	@Test
	public void testSpacesNormalized() throws IOException {
		assertThat(reformat("<block>bar</block>", BLOCK_FLUSH_PRE_FORMAT_PROFILE, "", ""), is("<block>bar</block>"));
		assertThat(reformat("<block>abc def\thijk\r\n  lmnop\n\t\tqrstuv\n\n\n\nwxyz</block>", BLOCK_FLUSH_PRE_FORMAT_PROFILE, "", ""),
				is("<block>abc def hijk lmnop qrstuv wxyz</block>"));
	}

	/**
	 * @see XmlFormatProfile#getSpaceNormalizationCharacters()
	 * @see XmlFormatProfile#isBlock(Element)
	 */
	@Test
	public void testBlockStartChildTextTrimmed() throws IOException {
		assertThat(reformat("<block>\n\tfoo<inline>bar</inline>\t\t\tfoobar</block>", BLOCK_FLUSH_PRE_FORMAT_PROFILE, "", ""),
				is("<block>foo<inline>bar</inline> foobar</block>"));
		assertThat(reformat("<inline>\n\tfoo</inline>", BLOCK_FLUSH_PRE_FORMAT_PROFILE, "", ""), is("<inline> foo</inline>"));
	}

	/**
	 * @see XmlFormatProfile#getSpaceNormalizationCharacters()
	 * @see XmlFormatProfile#isBlock(Element)
	 */
	@Test
	public void testChildTextAfterBlockTrimmed() throws IOException {
		assertThat(reformat("<block>foo<inline>bar</inline>\t\t\tfoobar</block>", BLOCK_FLUSH_PRE_FORMAT_PROFILE, "", ""),
				is("<block>foo<inline>bar</inline> foobar</block>"));
		assertThat(reformat("<block>foo<block>bar</block>\t\t\tfoobar</block>", BLOCK_FLUSH_PRE_FORMAT_PROFILE, "", ""),
				is("<block>foo<block>bar</block>foobar</block>"));
	}

	/**
	 * @see XmlFormatProfile#getSpaceNormalizationCharacters()
	 * @see XmlFormatProfile#isBlock(Element)
	 */
	@Test
	public void testBlockEndChildTextTrimmed() throws IOException {
		assertThat(reformat("<block>foo\t\t\t<inline>bar</inline>foobar\n\t</block>", BLOCK_FLUSH_PRE_FORMAT_PROFILE, "", ""),
				is("<block>foo <inline>bar</inline>foobar</block>"));
		assertThat(reformat("<inline>foo\n\t</inline>", BLOCK_FLUSH_PRE_FORMAT_PROFILE, "", ""), is("<inline>foo </inline>"));
	}

	/**
	 * @see XmlFormatProfile#getSpaceNormalizationCharacters()
	 * @see XmlFormatProfile#isBlock(Element)
	 */
	@Test
	public void testChildTextBeforeBlockTrimmed() throws IOException {
		assertThat(reformat("<block>foo\t\t\t<inline>bar</inline>foobar</block>", BLOCK_FLUSH_PRE_FORMAT_PROFILE, "", ""),
				is("<block>foo <inline>bar</inline>foobar</block>"));
		assertThat(reformat("<block>foo\t\t\t<block>bar</block>foobar</block>", BLOCK_FLUSH_PRE_FORMAT_PROFILE, "", ""),
				is("<block>foo<block>bar</block>foobar</block>"));
	}

	/** @see XmlFormatProfile#isBlock(Element) */
	@Test
	public void testNewlinesAroundBlock() throws IOException {
		assertThat(reformat("<block>foo<inline>foobar</inline>bar</block>", BLOCK_FLUSH_PRE_FORMAT_PROFILE, "\n", ""),
				is("<block>foo<inline>foobar</inline>bar</block>\n"));
		assertThat(reformat("<block><block>foobar</block>bar</block>", BLOCK_FLUSH_PRE_FORMAT_PROFILE, "\n", ""),
				is("<block>\n<block>foobar</block>\nbar\n</block>\n"));
		assertThat(reformat("<block>foo<block>foobar</block></block>", BLOCK_FLUSH_PRE_FORMAT_PROFILE, "\n", ""),
				is("<block>foo\n<block>foobar</block>\n</block>\n"));
		assertThat(reformat("<block>foo<block>foobar</block>bar</block>", BLOCK_FLUSH_PRE_FORMAT_PROFILE, "\n", ""),
				is("<block>foo\n<block>foobar</block>\nbar\n</block>\n"));
	}

	/** @see XmlFormatProfile#isBlock(Element) */
	@Test
	public void testSubsequentInlinesDoNotBreakLine() throws IOException {
		assertThat(
				reformat("<block>foo<block>foobar</block><inline>inside</inline><inline>inside</inline>beside<block>another</block>bar</block>",
						BLOCK_FLUSH_PRE_FORMAT_PROFILE, "\n", ""),
				is("<block>foo\n<block>foobar</block>\n<inline>inside</inline><inline>inside</inline>beside\n<block>another</block>\nbar\n</block>\n"));
	}

	/** @see XmlFormatProfile#isBlock(Element) */
	@Test
	public void testTrailingInlineAfterBlockHasNewline() throws IOException {
		assertThat(reformat("<block><block>foobar</block>bar</block>", BLOCK_FLUSH_PRE_FORMAT_PROFILE, "\n", ""),
				is("<block>\n<block>foobar</block>\nbar\n</block>\n"));
	}

	/** @see XmlFormatProfile#isBlock(Element) */
	@Test
	public void testBlockIndented() throws IOException {
		assertThat(reformat("<block>foo<inline>foobar</inline>bar</block>", BLOCK_FLUSH_PRE_FORMAT_PROFILE, "", "\t"),
				is("<block>foo<inline>foobar</inline>bar</block>"));
		assertThat(reformat("<block><block>foobar</block>bar</block>", BLOCK_FLUSH_PRE_FORMAT_PROFILE, "", "\t"),
				is("<block>\t<block>foobar</block>\tbar</block>"));
		assertThat(reformat("<block>foo<block>foobar</block></block>", BLOCK_FLUSH_PRE_FORMAT_PROFILE, "", "\t"), is("<block>foo\t<block>foobar</block></block>"));
		assertThat(reformat("<block>foo<block>foobar</block>bar</block>", BLOCK_FLUSH_PRE_FORMAT_PROFILE, "", "\t"),
				is("<block>foo\t<block>foobar</block>\tbar</block>"));
	}

	/** @see XmlFormatProfile#isBlock(Element) */
	@Test
	public void testBlockNewlinesAndIndents() throws IOException {
		assertThat(reformat("<block>foo<inline>foobar</inline>bar</block>", BLOCK_FLUSH_PRE_FORMAT_PROFILE), is("<block>foo<inline>foobar</inline>bar</block>\n"));
		assertThat(reformat("<block><block>foobar</block>bar</block>", BLOCK_FLUSH_PRE_FORMAT_PROFILE), is("<block>\n\t<block>foobar</block>\n\tbar\n</block>\n"));
		assertThat(reformat("<block>foo<block>foobar</block></block>", BLOCK_FLUSH_PRE_FORMAT_PROFILE), is("<block>foo\n\t<block>foobar</block>\n</block>\n"));
		assertThat(reformat("<block>foo<block>foobar</block>bar</block>", BLOCK_FLUSH_PRE_FORMAT_PROFILE),
				is("<block>foo\n\t<block>foobar</block>\n\tbar\n</block>\n"));
		assertThat(reformat("<block>foo<block>foobar</block>bar<block>left<block>nest</block>right</block>end</block>", BLOCK_FLUSH_PRE_FORMAT_PROFILE),
				is("<block>foo\n\t<block>foobar</block>\n\tbar\n\t<block>left\n\t\t<block>nest</block>\n\t\tright\n\t</block>\n\tend\n</block>\n"));
		assertThat(
				reformat("<block>foo<block>foobar</block><inline>inside</inline><inline>inside</inline>beside<block>another</block>bar</block>",
						BLOCK_FLUSH_PRE_FORMAT_PROFILE),
				is("<block>foo\n\t<block>foobar</block>\n\t<inline>inside</inline><inline>inside</inline>beside\n\t<block>another</block>\n\tbar\n</block>\n"));
	}

	/** @see XmlFormatProfile#isFlush(Element) */
	@Test
	public void testFlush() throws IOException {
		assertThat(reformat("<block><block>foobar</block></block>", BLOCK_FLUSH_PRE_FORMAT_PROFILE), is("<block>\n\t<block>foobar</block>\n</block>\n"));
		assertThat(reformat("<flush><block>foobar</block></flush>", BLOCK_FLUSH_PRE_FORMAT_PROFILE), is("<flush>\n<block>foobar</block>\n</flush>\n"));
		assertThat(reformat("<flush>foo<block>foobar</block></flush>", BLOCK_FLUSH_PRE_FORMAT_PROFILE), is("<flush>foo\n<block>foobar</block>\n</flush>\n"));
		assertThat(reformat("<flush><block>foobar</block>bar</flush>", BLOCK_FLUSH_PRE_FORMAT_PROFILE), is("<flush>\n<block>foobar</block>\nbar\n</flush>\n"));
		assertThat(reformat("<flush>foo<block>foobar</block>bar</flush>", BLOCK_FLUSH_PRE_FORMAT_PROFILE),
				is("<flush>foo\n<block>foobar</block>\nbar\n</flush>\n"));
		assertThat(
				reformat(
						"<block><block><block>foo</block> indented <block>bar</block></block> foobar <flush><block>foo</block> flush <block>bar</block></flush></block>",
						BLOCK_FLUSH_PRE_FORMAT_PROFILE),
				is("<block>\n\t<block>\n\t\t<block>foo</block>\n\t\tindented\n\t\t<block>bar</block>\n\t</block>\n\tfoobar\n\t<flush>\n\t<block>foo</block>\n\tflush\n\t<block>bar</block>\n\t</flush>\n</block>\n"));
	}

	/** @see XmlFormatProfile#isPreserved(Element) */
	@Test
	public void testPreservedNotFormatted() throws IOException {
		assertThat(reformat("<pre>abc def\thijk\n  lmnop\n\t\tqrstuv\n\n\n\nwxyz</pre>", BLOCK_FLUSH_PRE_FORMAT_PROFILE),
				is("<pre>abc def\thijk\n  lmnop\n\t\tqrstuv\n\n\n\nwxyz</pre>\n"));
		assertThat(reformat(
				"<block> before\t\n\n<pre>\t x\n\n\tThere are <nested>inline \t\t things</nested> and \n\t\t<block>several    spaces</block>\n\t here.\t\t</pre>\t  after\n</block>",
				BLOCK_FLUSH_PRE_FORMAT_PROFILE),
				is("<block>before\n\t<pre>\t x\n\n\tThere are <nested>inline \t\t things</nested> and \n\t\t<block>several    spaces</block>\n\t here.\t\t</pre>\n\tafter\n</block>\n"));
	}

	/**
	 * @see XmlFormatProfile#isPreserved(Element)
	 * @see <a href="https://www.w3.org/TR/xml/#sec-line-ends">Extensible Markup Language (XML) 1.0 (Fifth Edition), § 2.11 End-of-Line Handling</a>
	 */
	@Test
	public void testPreservedNormalizesNewlines() throws IOException {
		assertThat(reformat("<pre>abc\r\r\r\rdef\n\n\n\nhij\r\n\r\nklmn\r\n\n\rop</pre>", BLOCK_FLUSH_PRE_FORMAT_PROFILE, "XY", "\t"),
				is("<pre>abcXYXYXYXYdefXYXYXYXYhijXYXYklmnXYXYXYop</pre>XY"));
	}

	/**
	 * @see XMLSerializer#serializeAttributes(Appendable, Element, Stream)
	 * @see XmlFormatProfile#getAttributeOrder(Element)
	 */
	@Test
	public void testAttributeOrder() throws IOException {
		//no order
		assertThat(
				reformat("<block banana=\"yellow\" bar=\"rab\" apple=\"red\" cherry=\"red\" foo=\"oof\">content</block>", BLOCK_FLUSH_PRE_FORMAT_PROFILE, "", ""),
				is("<block apple=\"red\" banana=\"yellow\" bar=\"rab\" cherry=\"red\" foo=\"oof\">content</block>"));
		//ordered
		assertThat(
				reformat("<ordered banana=\"yellow\" bar=\"rab\" apple=\"red\" cherry=\"red\" foo=\"oof\">content</ordered>", BLOCK_FLUSH_PRE_FORMAT_PROFILE, "", ""),
				is("<ordered foo=\"oof\" bar=\"rab\" apple=\"red\" banana=\"yellow\" cherry=\"red\">content</ordered>"));
		//default namespace declaration
		assertThat(
				reformat("<ordered banana=\"yellow\" bar=\"rab\" apple=\"red\" xmlns=\"http://example/ns/\" cherry=\"red\" foo=\"oof\">content</ordered>",
						BLOCK_FLUSH_PRE_FORMAT_PROFILE, "", ""),
				is("<ordered xmlns=\"http://example/ns/\" foo=\"oof\" bar=\"rab\" apple=\"red\" banana=\"yellow\" cherry=\"red\">content</ordered>"));
		//other defined namespaces (i.e. all possibilities)
		assertThat(reformat(
				"<ordered xmlns:veggie=\"http://example.com/ns/veggie/\" xml:lang=\"en-us\" fruit:banana=\"yellow\" bar=\"rab\" xmlns:fruit=\"http://example.com/ns/fruit/\""
						+ " fruit:apple=\"red\" veggie:broccoli=\"green\" xmlns=\"http://example/ns/\" fruit:cherry=\"red\" test=\"example\" foo=\"oof\">content</ordered>",
				BLOCK_FLUSH_PRE_FORMAT_PROFILE, "", ""),
				is("<ordered xmlns=\"http://example/ns/\"" //default namespace declaration
						+ " foo=\"oof\" bar=\"rab\"" //ordered attributes
						+ " test=\"example\"" //other non-prefixed attribute
						+ " xmlns:fruit=\"http://example.com/ns/fruit/\" xmlns:veggie=\"http://example.com/ns/veggie/\"" //namespace declarations
						+ " fruit:apple=\"red\" fruit:banana=\"yellow\" fruit:cherry=\"red\" veggie:broccoli=\"green\"" //prefixed attributes from defined namespaces
						+ " xml:lang=\"en-us\"" //a prefixed namespace that doesn't need to be defined
						+ ">content</ordered>"));
	}

	/**
	 * Parses and re-serializes an XML document from a string.
	 * @implSpec No prolog is written.
	 * @implSpec A single newline character <code>'\n'</code> is used as a line separator.
	 * @implSpec A single tab character <code>'\t'</code> is used as a horizontal aligner.
	 * @param text The text to parse and re-serialize.
	 * @param formatProfile The formatting characterization of the document.
	 * @return The re-serialized form of the document, with no XML prolog.
	 * @throws IOException if an error occurs parsing or serializing the document.
	 */
	public static String reformat(@Nonnull final String text, @Nonnull final XmlFormatProfile formatProfile) throws IOException {
		return reformat(text, formatProfile, "\n", "\t");
	}

	/**
	 * Parses and re-serializes an XML document from a string.
	 * @implSpec No prolog is written.
	 * @param text The text to parse and re-serialize.
	 * @param formatProfile The formatting characterization of the document.
	 * @param lineSeparator The newline delimiter to use.
	 * @param horizontalAligner The horizontal alignment character sequence.
	 * @return The re-serialized form of the document, with no XML prolog.
	 * @throws IOException if an error occurs parsing or serializing the document.
	 */
	public static String reformat(@Nonnull final String text, @Nonnull final XmlFormatProfile formatProfile, @Nonnull final String lineSeparator,
			@Nonnull final String horizontalAligner) throws IOException {
		final Document document = parse(new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8)), true);
		final XMLSerializer serializer = new XMLSerializer(true, formatProfile);
		serializer.setPrologWritten(false);
		serializer.setLineSeparator(lineSeparator);
		serializer.setHorizontalAligner(horizontalAligner);
		return serializer.serialize(document);
	}

}
