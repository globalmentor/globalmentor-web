/*
 * Copyright © 2022 GlobalMentor, Inc. <https://www.globalmentor.com/>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
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

import java.net.URI;

import javax.xml.namespace.NamespaceContext;

import org.junit.jupiter.api.*;

import com.globalmentor.vocab.VocabularyRegistry;

/**
 * Tests of {@link VocabularyNamespaceContext}.
 * @author Garret Wilson
 */
public class VocabularyNamespaceContextTest extends BaseNamespaceContextTest {

	/**
	 * {@inheritDoc}
	 * @implSpec This implementation returns a namespace context with the prefix <code>dc11</code> registered to the namespace
	 *           <code>http://purl.org/dc/elements/1.1/</code>, and both prefixes <code>dc</code> and <code>dcterms</code> registered with
	 *           <code>"http://purl.org/dc/terms/"</code>.
	 * @see <a href="https://www.dublincore.org/specifications/dublin-core/dces/">Dublin Core Metadata Element Set, Version 1.1</a>
	 * @see <a href="https://www.dublincore.org/specifications/dublin-core/dcmi-terms/">DCMI Metadata Terms</a>
	 */
	@Override
	protected NamespaceContext createTestNamespaceContext() {
		final VocabularyRegistry vocabularyRegistry = VocabularyRegistry.builder().registerPrefix("dc", URI.create("http://purl.org/dc/terms/"))
				.registerPrefix("dc11", URI.create("http://purl.org/dc/elements/1.1/")).registerPrefix("dcterms", URI.create("http://purl.org/dc/terms/")).build();
		return new VocabularyNamespaceContext(vocabularyRegistry);
	}

	/** @see VocabularyNamespaceContext#getNamespaceURI(String) */
	@Test
	@Override
	void testGetNamespaceURI() {
		super.testGetNamespaceURI();
		assertThat("dc", getTestNamespaceContext().getNamespaceURI("dc"), is("http://purl.org/dc/terms/"));
		assertThat("dcterms", getTestNamespaceContext().getNamespaceURI("dcterms"), is("http://purl.org/dc/terms/"));
		assertThat("dc11", getTestNamespaceContext().getNamespaceURI("dc11"), is("http://purl.org/dc/elements/1.1/"));
	}

	/** @see VocabularyNamespaceContext#getPrefix(String) */
	@Test
	@Override
	void testGetPrefix() {
		super.testGetPrefix();
		assertThat("http://purl.org/dc/terms/", getTestNamespaceContext().getPrefix("http://purl.org/dc/terms/"), is("dc")); //the first registered prefix wins as per the API
		assertThat("http://purl.org/dc/elements/1.1/", getTestNamespaceContext().getPrefix("http://purl.org/dc/elements/1.1/"), is("dc11"));
	}

	@Test
	@Override
	void testGetPrefixes() {
		super.testGetPrefixes();
		assertThat("http://purl.org/dc/terms/", () -> getTestNamespaceContext().getPrefixes("http://purl.org/dc/terms/"), contains("dc")); //the first registered prefix wins as per the API
		assertThat("http://purl.org/dc/elements/1.1/", () -> getTestNamespaceContext().getPrefixes("http://purl.org/dc/elements/1.1/"), contains("dc11"));
	}

}
