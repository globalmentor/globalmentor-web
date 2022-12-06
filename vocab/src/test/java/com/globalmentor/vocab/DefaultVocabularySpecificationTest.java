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

import static com.github.npathai.hamcrestopt.OptionalMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;

import org.junit.jupiter.api.*;

/**
 * Tests of {@link DefaultVocabularySpecification}.
 * @author Garret Wilson
 */
public class DefaultVocabularySpecificationTest {

	/** @see DefaultVocabularySpecification#isValidPrefix(String) */
	@Test
	public void testIsValidPrefix() {
		final DefaultVocabularySpecification spec = new DefaultVocabularySpecification();
		assertThat(spec.isValidPrefix(""), is(false));
		assertThat(spec.isValidPrefix("foobar"), is(true));
		assertThat(spec.isValidPrefix("Foobar"), is(true));
		assertThat(spec.isValidPrefix("FooBar"), is(true));
		assertThat(spec.isValidPrefix("fooBar"), is(true));
		assertThat(spec.isValidPrefix("foo/bar"), is(true));
		assertThat(spec.isValidPrefix("foo_bar"), is(true));
		assertThat(spec.isValidPrefix("foo bar"), is(false));
		assertThat(spec.isValidPrefix(" foobar"), is(false));
		assertThat(spec.isValidPrefix("\tfoo"), is(false));
		assertThat(spec.isValidPrefix("foobar "), is(false));
		assertThat(spec.isValidPrefix("foobar\n"), is(false));
	}

	/** @see DefaultVocabularySpecification#isNamespaceRegular(URI) */
	@Test
	public void testIsNamespaceRegular() {
		final DefaultVocabularySpecification spec = new DefaultVocabularySpecification();
		assertThrows(IllegalArgumentException.class, () -> spec.isNamespaceRegular(URI.create("fooBar")));
		assertThat(spec.isNamespaceRegular(URI.create("http://example.com")), is(false));
		assertThat(spec.isNamespaceRegular(URI.create("http://example.com/")), is(true));
		assertThat(spec.isNamespaceRegular(URI.create("http://example.com#")), is(true));
		assertThat(spec.isNamespaceRegular(URI.create("http://purl.org/dc/terms/")), is(true));
		assertThat(spec.isNamespaceRegular(URI.create("http://creativecommons.org/ns#")), is(true));
		assertThat(spec.isNamespaceRegular(URI.create("http://www.w3.org/2001/XMLSchema#")), is(true));
		assertThat(spec.isNamespaceRegular(URI.create("http://www.w3.org/XML/1998/namespace")), is(false)); //an actual namespace
	}

	/** @see DefaultVocabularySpecification#findTermNamespace(URI) */
	@Test
	public void testFindNamespaceForTerm() {
		final DefaultVocabularySpecification spec = new DefaultVocabularySpecification();
		assertThrows(IllegalArgumentException.class, () -> spec.findTermNamespace(URI.create("fooBar")));
		assertThat(spec.findTermNamespace(URI.create("http://example.com")), isEmpty());
		assertThat(spec.findTermNamespace(URI.create("http://example.com/")), isEmpty());
		assertThat(spec.findTermNamespace(URI.create("http://example.com/foo")), isPresentAndIs(URI.create("http://example.com/")));
		assertThat(spec.findTermNamespace(URI.create("http://example.com#")), isEmpty());
		assertThat(spec.findTermNamespace(URI.create("http://example.com#foo")), isPresentAndIs(URI.create("http://example.com#")));
		assertThat(spec.findTermNamespace(URI.create("http://purl.org/dc/terms/")), isEmpty());
		assertThat(spec.findTermNamespace(URI.create("http://purl.org/dc/terms/creator")), isPresentAndIs(URI.create("http://purl.org/dc/terms/")));
		assertThat(spec.findTermNamespace(URI.create("http://creativecommons.org/ns#")), isEmpty());
		assertThat(spec.findTermNamespace(URI.create("http://creativecommons.org/ns#permits")), isPresentAndIs(URI.create("http://creativecommons.org/ns#")));
	}

}
