/*
 * Copyright © 2019 GlobalMentor, Inc. <https://www.globalmentor.com/>
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

package com.globalmentor.vocab;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.net.URI;

import org.junit.jupiter.api.*;

/**
 * Tests of {@link VocabularyTerm}.
 * @author Garret Wilson
 */
public class VocabularyTermTest {

	/** @see VocabularyTerm#toURI() */
	@Test
	public void testToURI() {
		assertThat(VocabularyTerm.of(URI.create("http://purl.org/dc/terms/"), "creator").toURI(), is(URI.create("http://purl.org/dc/terms/creator")));
		assertThat(VocabularyTerm.of(URI.create("http://ogp.me/ns#"), "title").toURI(), is(URI.create("http://ogp.me/ns#title")));
		assertThat(VocabularyTerm.of(URI.create("http://www.w3.org/XML/1998/namespace"), "lang").toURI(),
				is(URI.create("http://www.w3.org/XML/1998/namespacelang")));
	}

	/** @see VocabularyTerm#toURI(URI, String) */
	@Test
	public void testStaticToURI() {
		assertThat(VocabularyTerm.toURI(URI.create("http://purl.org/dc/terms/"), "creator"), is(URI.create("http://purl.org/dc/terms/creator")));
		assertThat(VocabularyTerm.toURI(URI.create("http://ogp.me/ns#"), "title"), is(URI.create("http://ogp.me/ns#title")));
		assertThat(VocabularyTerm.toURI(URI.create("http://www.w3.org/XML/1998/namespace"), "lang"), is(URI.create("http://www.w3.org/XML/1998/namespacelang")));
	}

}
