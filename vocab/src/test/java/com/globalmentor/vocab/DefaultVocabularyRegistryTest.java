/*
 * Copyright © 2019 GlobalMentor, Inc. <http://www.globalmentor.com/>
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

package com.globalmentor.vocab;

import static com.github.npathai.hamcrestopt.OptionalMatchers.*;
import static java.util.Arrays.*;
import static java.util.Collections.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.net.URI;
import java.util.*;

import org.junit.jupiter.api.*;

/**
 * Tests of {@link DefaultVocabularyRegistry}.
 * @author Garret Wilson
 */
public class DefaultVocabularyRegistryTest {

	private static final URI CC_NAMESPACE = URI.create("http://creativecommons.org/ns#");
	private static final URI DC_NAMESPACE = URI.create("http://purl.org/dc/terms/");
	private static final URI OG_NAMESPACE = URI.create("http://ogp.me/ns#");
	private static final URI EG_NAMESPACE = URI.create("https://example.com/ns/");

	@Test
	public void testEmptyRegistry() {
		final DefaultVocabularyRegistry registry = new DefaultVocabularyRegistry(null, emptySet());
		assertThat(registry.isPrefixRegistered("foo"), is(false));
		assertThat(registry.isVocabularyRegistered(DC_NAMESPACE), is(false));
		assertThat(registry.findVocabularyByPrefix("foo"), isEmpty());
		assertThat(registry.findPrefixForVocabulary(DC_NAMESPACE), isEmpty());
		assertThat(registry.findVocabularyByPrefix("foo"), isEmpty());
		assertThat(registry.getRegisteredPrefixesByVocabulary(), is(emptySet()));
		assertThat(registry.getRegisteredVocabulariesByPrefix(), is(emptySet()));
	}

	/** Tests basic registrations and retrieval. */
	@Test
	public void testBasicRegistrations() {
		final DefaultVocabularyRegistry registry = new DefaultVocabularyRegistry(EG_NAMESPACE,
				asList(new AbstractMap.SimpleImmutableEntry<>("dc", DC_NAMESPACE), new AbstractMap.SimpleImmutableEntry<>("og", OG_NAMESPACE)));

		assertThat(registry.getDefaultVocabulary(), isPresentAndIs(EG_NAMESPACE));

		assertThat(registry.isPrefixRegistered("dc"), is(true));
		assertThat(registry.isPrefixRegistered("foo"), is(false));
		assertThat(registry.isPrefixRegistered("og"), is(true));

		assertThat(registry.isVocabularyRegistered(DC_NAMESPACE), is(true));
		assertThat(registry.isVocabularyRegistered(URI.create("http://example.com/ns/")), is(false)); //difference in URI scheme
		assertThat(registry.isVocabularyRegistered(EG_NAMESPACE), is(true));
		assertThat(registry.isVocabularyRegistered(OG_NAMESPACE), is(true));

		assertThat(registry.findPrefixForVocabulary(DC_NAMESPACE), isPresentAndIs("dc"));
		assertThat(registry.findPrefixForVocabulary(URI.create("http://example.com/ns/")), isEmpty());
		assertThat(registry.findPrefixForVocabulary(EG_NAMESPACE), isEmpty());
		assertThat(registry.findPrefixForVocabulary(OG_NAMESPACE), isPresentAndIs("og"));

		assertThat(registry.findVocabularyByPrefix("dc"), isPresentAndIs(DC_NAMESPACE));
		assertThat(registry.findVocabularyByPrefix("foo"), isEmpty());
		assertThat(registry.findVocabularyByPrefix("og"), isPresentAndIs(OG_NAMESPACE));

		assertThat(registry.getRegisteredPrefixesByVocabulary(),
				is(new HashSet<>(asList(new AbstractMap.SimpleImmutableEntry<>(DC_NAMESPACE, "dc"), new AbstractMap.SimpleImmutableEntry<>(OG_NAMESPACE, "og")))));

		assertThat(registry.getRegisteredVocabulariesByPrefix(),
				is(new HashSet<>(asList(new AbstractMap.SimpleImmutableEntry<>("dc", DC_NAMESPACE), new AbstractMap.SimpleImmutableEntry<>("og", OG_NAMESPACE)))));
	}

	/** @see DefaultVocabularyRegistry#findPrefixForTerm(URI) */
	@Test
	public void testFindPrefixForTerm() {
		final DefaultVocabularyRegistry registry = new DefaultVocabularyRegistry(EG_NAMESPACE,
				asList(new AbstractMap.SimpleImmutableEntry<>("dc", DC_NAMESPACE), new AbstractMap.SimpleImmutableEntry<>("og", OG_NAMESPACE)));
		assertThat(registry.findPrefixForTerm(URI.create("http://example.com/")), isEmpty()); //not a term
		assertThat(registry.findPrefixForTerm(URI.create("http://example.com/foo")), isEmpty()); //not registered
		assertThat(registry.findPrefixForTerm(URI.create("http://example.com/foo/bar")), isEmpty()); //not registered
		assertThat(registry.findPrefixForTerm(URI.create("http://purl.org/dc/terms/creator")),
				isPresentAndIs(new AbstractMap.SimpleImmutableEntry<>(VocabularyTerm.of(DC_NAMESPACE, "creator"), "dc")));
		assertThat(registry.findPrefixForTerm(URI.create("http://ogp.me/ns#title")),
				isPresentAndIs(new AbstractMap.SimpleImmutableEntry<>(VocabularyTerm.of(OG_NAMESPACE, "title"), "og")));
		assertThat(registry.findPrefixForTerm(URI.create("http://creativecommons.org/ns#permits")), isEmpty());
	}

	/** @see DefaultVocabularyRegistry#asVocabularyTerm(URI) */
	@Test
	public void testAsVocabularyTerm() {
		final DefaultVocabularyRegistry registry = new DefaultVocabularyRegistry(EG_NAMESPACE,
				asList(new AbstractMap.SimpleImmutableEntry<>("dc", DC_NAMESPACE), new AbstractMap.SimpleImmutableEntry<>("og", OG_NAMESPACE)));
		assertThat(registry.asVocabularyTerm(URI.create("http://example.com/")), isEmpty());
		assertThat(registry.asVocabularyTerm(URI.create("http://purl.org/dc/terms/creator")), isPresentAndIs(VocabularyTerm.of(DC_NAMESPACE, "creator")));
		assertThat(registry.asVocabularyTerm(URI.create("http://ogp.me/ns#title")), isPresentAndIs(VocabularyTerm.of(OG_NAMESPACE, "title")));
		assertThat(registry.asVocabularyTerm(URI.create("http://creativecommons.org/ns#permits")), isPresentAndIs(VocabularyTerm.of(CC_NAMESPACE, "permits")));
	}

	/** @see DefaultVocabularyRegistry#findVocabularyTerm(Curie) */
	@Test
	public void testFindVocabularyTermWithoutDefaultNamespace() {
		final DefaultVocabularyRegistry registry = new DefaultVocabularyRegistry(null,
				asList(new AbstractMap.SimpleImmutableEntry<>("dc", DC_NAMESPACE), new AbstractMap.SimpleImmutableEntry<>("og", OG_NAMESPACE)));
		assertThat(registry.findVocabularyTerm(Curie.parse("foo:bar")), isEmpty());
		assertThat(registry.findVocabularyTerm(Curie.parse("bar")), isEmpty());
		assertThat(registry.findVocabularyTerm(Curie.parse("dc:creator")), isPresentAndIs(VocabularyTerm.of(DC_NAMESPACE, "creator")));
		assertThat(registry.findVocabularyTerm(Curie.parse("og:title")), isPresentAndIs(VocabularyTerm.of(OG_NAMESPACE, "title")));
	}

	/** @see DefaultVocabularyRegistry#findVocabularyTerm(Curie) */
	@Test
	public void testFindVocabularyTermWithDefaultNamespace() {
		final DefaultVocabularyRegistry registry = new DefaultVocabularyRegistry(EG_NAMESPACE,
				asList(new AbstractMap.SimpleImmutableEntry<>("dc", DC_NAMESPACE), new AbstractMap.SimpleImmutableEntry<>("og", OG_NAMESPACE)));
		assertThat(registry.findVocabularyTerm(Curie.parse("foo:bar")), isEmpty());
		assertThat(registry.findVocabularyTerm(Curie.parse("bar")), isPresentAndIs(VocabularyTerm.of(EG_NAMESPACE, "bar")));
		assertThat(registry.findVocabularyTerm(Curie.parse("dc:creator")), isPresentAndIs(VocabularyTerm.of(DC_NAMESPACE, "creator")));
		assertThat(registry.findVocabularyTerm(Curie.parse("og:title")), isPresentAndIs(VocabularyTerm.of(OG_NAMESPACE, "title")));
	}

	/** @see DefaultVocabularyRegistry#findCurie(VocabularyTerm) */
	@Test
	public void testFindCurieWithoutDefaultNamespace() {
		final DefaultVocabularyRegistry registry = new DefaultVocabularyRegistry(null,
				asList(new AbstractMap.SimpleImmutableEntry<>("dc", DC_NAMESPACE), new AbstractMap.SimpleImmutableEntry<>("og", OG_NAMESPACE)));
		assertThat(registry.findCurie(VocabularyTerm.of(CC_NAMESPACE, "permits")), isEmpty());
		assertThat(registry.findCurie(VocabularyTerm.of(EG_NAMESPACE, "bar")), isEmpty());
		assertThat(registry.findCurie(VocabularyTerm.of(DC_NAMESPACE, "creator")), isPresentAndIs(Curie.parse("dc:creator")));
		assertThat(registry.findCurie(VocabularyTerm.of(OG_NAMESPACE, "title")), isPresentAndIs(Curie.parse("og:title")));
	}

	/** @see DefaultVocabularyRegistry#findCurie(VocabularyTerm) */
	@Test
	public void testFindCurieWithDefaultNamespace() {
		final DefaultVocabularyRegistry registry = new DefaultVocabularyRegistry(EG_NAMESPACE,
				asList(new AbstractMap.SimpleImmutableEntry<>("dc", DC_NAMESPACE), new AbstractMap.SimpleImmutableEntry<>("og", OG_NAMESPACE)));
		assertThat(registry.findCurie(VocabularyTerm.of(CC_NAMESPACE, "permits")), isEmpty());
		assertThat(registry.findCurie(VocabularyTerm.of(EG_NAMESPACE, "bar")), isPresentAndIs(Curie.parse("bar")));
		assertThat(registry.findCurie(VocabularyTerm.of(DC_NAMESPACE, "creator")), isPresentAndIs(Curie.parse("dc:creator")));
		assertThat(registry.findCurie(VocabularyTerm.of(OG_NAMESPACE, "title")), isPresentAndIs(Curie.parse("og:title")));
	}

	/** @see DefaultVocabularyRegistry#findCurie(VocabularyTerm) */
	@Test
	public void testFindCuriePrefersRegisteredNamespaceOverDefaultNamespace() {
		final DefaultVocabularyRegistry registry = new DefaultVocabularyRegistry(EG_NAMESPACE, asList(new AbstractMap.SimpleImmutableEntry<>("dc", DC_NAMESPACE),
				new AbstractMap.SimpleImmutableEntry<>("eg", EG_NAMESPACE), new AbstractMap.SimpleImmutableEntry<>("og", OG_NAMESPACE)));
		assertThat(registry.findCurie(VocabularyTerm.of(CC_NAMESPACE, "permits")), isEmpty());
		assertThat(registry.findCurie(VocabularyTerm.of(EG_NAMESPACE, "bar")), isPresentAndIs(Curie.parse("eg:bar")));
		assertThat(registry.findCurie(VocabularyTerm.of(DC_NAMESPACE, "creator")), isPresentAndIs(Curie.parse("dc:creator")));
		assertThat(registry.findCurie(VocabularyTerm.of(OG_NAMESPACE, "title")), isPresentAndIs(Curie.parse("og:title")));
	}

}
