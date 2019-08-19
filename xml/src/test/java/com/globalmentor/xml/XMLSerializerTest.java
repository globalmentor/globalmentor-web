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

}
