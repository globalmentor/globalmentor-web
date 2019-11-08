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
 * Tests of {@link VocabularyManager}.
 * @author Garret Wilson
 */
public class VocabularyManagerTest {

	private static final URI DC_NAMESPACE = URI.create("http://purl.org/dc/terms/");
	private static final URI OG_NAMESPACE = URI.create("http://ogp.me/ns#");
	private static final URI EG_NAMESPACE = URI.create("https://example.com/ns/");

	@Test
	public void testEmptyManager() {
		final VocabularyManager manager = new VocabularyManager();
		assertThat(manager.isPrefixRegistered("foo"), is(false));
		assertThat(manager.isVocabularyRegistered(DC_NAMESPACE), is(false));
		assertThat(manager.findVocabularyByPrefix("foo"), isEmpty());
		assertThat(manager.findPrefixForVocabulary(DC_NAMESPACE), isEmpty());
		assertThat(manager.findVocabularyByPrefix("foo"), isEmpty());
		assertThat(manager.getRegisteredPrefixesByVocabulary(), is(emptySet()));
		assertThat(manager.getRegisteredVocabulariesByPrefix(), is(emptySet()));
	}

	/** Tests basic registrations and retrieval. */
	@Test
	public void testBasicRegistrations() {
		final VocabularyManager manager = new VocabularyManager();
		manager.setDefaultVocabulary(EG_NAMESPACE);
		manager.registerPrefix("dc", DC_NAMESPACE);
		manager.registerPrefix("og", OG_NAMESPACE);

		assertThat(manager.getDefaultVocabulary(), isPresentAndIs(EG_NAMESPACE));

		assertThat(manager.isPrefixRegistered("dc"), is(true));
		assertThat(manager.isPrefixRegistered("foo"), is(false));
		assertThat(manager.isPrefixRegistered("og"), is(true));

		assertThat(manager.isVocabularyRegistered(DC_NAMESPACE), is(true));
		assertThat(manager.isVocabularyRegistered(URI.create("http://example.com/ns/")), is(false)); //difference in URI scheme
		assertThat(manager.isVocabularyRegistered(EG_NAMESPACE), is(true));
		assertThat(manager.isVocabularyRegistered(OG_NAMESPACE), is(true));

		assertThat(manager.findPrefixForVocabulary(DC_NAMESPACE), isPresentAndIs("dc"));
		assertThat(manager.findPrefixForVocabulary(URI.create("http://example.com/ns/")), isEmpty());
		assertThat(manager.findPrefixForVocabulary(EG_NAMESPACE), isEmpty());
		assertThat(manager.findPrefixForVocabulary(OG_NAMESPACE), isPresentAndIs("og"));

		assertThat(manager.findVocabularyByPrefix("dc"), isPresentAndIs(DC_NAMESPACE));
		assertThat(manager.findVocabularyByPrefix("foo"), isEmpty());
		assertThat(manager.findVocabularyByPrefix("og"), isPresentAndIs(OG_NAMESPACE));

		assertThat(manager.getRegisteredPrefixesByVocabulary(), is(new HashSet<>(asList(Map.entry(DC_NAMESPACE, "dc"), Map.entry(OG_NAMESPACE, "og")))));

		assertThat(manager.getRegisteredVocabulariesByPrefix(), is(new HashSet<>(asList(Map.entry("dc", DC_NAMESPACE), Map.entry("og", OG_NAMESPACE)))));
	}

	//TODO test reverse registrations with multiple prefixes for same namespace

	/** @see VocabularyManager#determinePrefixForVocabulary(URI) */
	@Test
	public void testDetermineVocabularyPrefixUsesExistingRegistration() {
		final VocabularyManager manager = new VocabularyManager();
		manager.registerPrefix("dc", DC_NAMESPACE);
		assertThat(manager.determinePrefixForVocabulary(DC_NAMESPACE), is("dc"));
	}

	/** @see VocabularyManager#determinePrefixForVocabulary(URI) */
	@Test
	public void testDetermineVocabularyPrefixUsesPathName() {
		final VocabularyManager manager = new VocabularyManager();
		assertThat(manager.isPrefixRegistered("bar"), is(false));
		assertThat(manager.determinePrefixForVocabulary(URI.create("http://example.com/foo/bar")), is("bar"));
		assertThat(manager.isPrefixRegistered("bar"), is(true));
	}

	/** @see VocabularyManager#determinePrefixForVocabulary(URI) */
	@Test
	public void testDetermineVocabularyPrefixUsesCollectionPathComponent() {
		final VocabularyManager manager = new VocabularyManager();
		assertThat(manager.isPrefixRegistered("bar"), is(false));
		assertThat(manager.determinePrefixForVocabulary(URI.create("http://example.com/foo/bar/")), is("bar"));
		assertThat(manager.isPrefixRegistered("bar"), is(true));
	}

	/** @see VocabularyManager#determinePrefixForVocabulary(URI) */
	@Test
	public void testDetermineVocabularyPrefixGeneratesPrefixWhenPathNameAlreadyRegistered() {
		final VocabularyManager manager = new VocabularyManager();
		manager.registerPrefix("bar", URI.create("http://example.com/some/other/namespace"));
		assertThat(manager.isPrefixRegistered("bar"), is(true));
		final String generatedPrefix = BaseVocabularyPrefixSpecification.PREFIX_PREFIX + "1";
		assertThat(manager.isPrefixRegistered(generatedPrefix), is(false));
		assertThat(manager.determinePrefixForVocabulary(URI.create("http://example.com/foo/bar")), is(generatedPrefix));
		assertThat(manager.isPrefixRegistered(generatedPrefix), is(true));
	}

	/** @see VocabularyManager#determinePrefixForVocabulary(URI) */
	@Test
	public void testDetermineVocabularyPrefixGeneratesPrefixWhenCannotBeAutoDeterminedFromURI() {
		final VocabularyManager manager = new VocabularyManager();
		final String generatedPrefix = BaseVocabularyPrefixSpecification.PREFIX_PREFIX + "1";
		assertThat(manager.isPrefixRegistered(generatedPrefix), is(false));
		assertThat(manager.determinePrefixForVocabulary(URI.create("http://example.com/foo%20bar")), is(generatedPrefix));
		assertThat(manager.isPrefixRegistered(generatedPrefix), is(true));
	}

	/** @see VocabularyManager#determinePrefixForVocabulary(URI) */
	@Test
	public void testDetermineVocabularyPrefixGeneratesUniquePrefixes() {
		final VocabularyManager manager = new VocabularyManager();
		assertThat(manager.determinePrefixForVocabulary(URI.create("http://example.com/foo%20bar")), is(BaseVocabularyPrefixSpecification.PREFIX_PREFIX + "1"));
		assertThat(manager.determinePrefixForVocabulary(URI.create("http://example.com/bar%20foo")), is(BaseVocabularyPrefixSpecification.PREFIX_PREFIX + "2"));
	}

	/** @see VocabularyManager#determinePrefixForVocabulary(URI) */
	@Test
	public void testDetermineVocabularyPrefixGeneratesAnotherPrefixGeneratedPrefixAlreadyRegistered() {
		final VocabularyManager manager = new VocabularyManager();
		final String generatedPrefix1 = BaseVocabularyPrefixSpecification.PREFIX_PREFIX + "1";
		manager.registerPrefix(generatedPrefix1, URI.create("http://example.com/some/other/namespace"));
		assertThat(manager.isPrefixRegistered(generatedPrefix1), is(true));
		final String generatedPrefix2 = BaseVocabularyPrefixSpecification.PREFIX_PREFIX + "2";
		assertThat(manager.isPrefixRegistered(generatedPrefix2), is(false));
		assertThat(manager.determinePrefixForVocabulary(URI.create("http://example.com/foo%20bar")), is(generatedPrefix2));
		assertThat(manager.isPrefixRegistered(generatedPrefix2), is(true));
	}

	/** @see VocabularyManager#determinePrefixForVocabulary(URI) */
	@Test
	public void testDetermineVocabularyPrefixUsesKnownVocabularyPrefix() {
		final URI fooNamespace = URI.create("http://example.com/foo");
		final VocabularyRegistry knownVocabularies = VocabularyRegistry.of(Map.entry("bar", fooNamespace));
		assertThat(knownVocabularies.isPrefixRegistered("bar"), is(true));
		final VocabularyManager manager = new VocabularyManager(knownVocabularies);
		assertThat(manager.isPrefixRegistered("bar"), is(false));
		assertThat(manager.determinePrefixForVocabulary(fooNamespace), is("bar"));
		assertThat(manager.isPrefixRegistered("bar"), is(true));
	}

	/**
	 * @see VocabularyManager#isAutoRegister()
	 * @see VocabularyManager#findVocabularyByPrefix(String)
	 */
	@Test
	public void testAutoRegisterUsesKnownVocabularyNamespace() {
		final URI fooNamespace = URI.create("http://example.com/foo");
		final URI otherNamespace = URI.create("http://example.com/other");
		final VocabularyRegistry knownVocabularies = VocabularyRegistry.of(Map.entry("bar", fooNamespace), Map.entry("other", otherNamespace));
		final VocabularyManager manager = new VocabularyManager(knownVocabularies, true); //turn on auto-register
		assertThat(manager.isVocabularyRegistered(fooNamespace), is(false));
		assertThat(manager.findVocabularyByPrefix("bar"), isPresentAndIs(fooNamespace));
		assertThat(manager.isVocabularyRegistered(fooNamespace), is(true));
	}

	/**
	 * @see VocabularyManager#isAutoRegister()
	 * @see VocabularyManager#findPrefixForVocabulary(URI)
	 */
	@Test
	public void testAutoRegisterUsesKnownVocabularyPrefix() {
		final URI fooNamespace = URI.create("http://example.com/foo");
		final URI otherNamespace = URI.create("http://example.com/other");
		final VocabularyRegistry knownVocabularies = VocabularyRegistry.of(Map.entry("bar", fooNamespace), Map.entry("other", otherNamespace));
		final VocabularyManager manager = new VocabularyManager(knownVocabularies, true); //turn on auto-register
		assertThat(manager.isPrefixRegistered("bar"), is(false));
		assertThat(manager.findPrefixForVocabulary(fooNamespace), isPresentAndIs("bar"));
		assertThat(manager.isPrefixRegistered("bar"), is(true));
	}

}
