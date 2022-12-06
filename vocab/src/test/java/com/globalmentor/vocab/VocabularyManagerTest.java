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
import static com.globalmentor.net.URIs.*;
import static java.util.AbstractMap.SimpleImmutableEntry;
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

	private static final URI CC_NAMESPACE = URI.create("http://creativecommons.org/ns#");
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

		assertThat(manager.getRegisteredPrefixesByVocabulary(),
				is(new HashSet<>(asList(new SimpleImmutableEntry<>(DC_NAMESPACE, "dc"), new SimpleImmutableEntry<>(OG_NAMESPACE, "og")))));

		assertThat(manager.getRegisteredVocabulariesByPrefix(),
				is(new HashSet<>(asList(new SimpleImmutableEntry<>("dc", DC_NAMESPACE), new SimpleImmutableEntry<>("og", OG_NAMESPACE)))));
	}

	/** @see VocabularyManager#registerPrefix(String, URI) */
	@Test
	public void testMultiplePrefixRegistrationsWithSameVocabulary() {
		final VocabularyManager manager = new VocabularyManager();
		assertThat(manager.findVocabularyByPrefix("foo"), isEmpty());
		assertThat(manager.findVocabularyByPrefix("bar"), isEmpty());
		assertThat(manager.findPrefixForVocabulary(EG_NAMESPACE), isEmpty());
		manager.registerPrefix("foo", EG_NAMESPACE);
		assertThat(manager.findVocabularyByPrefix("foo"), isPresentAndIs(EG_NAMESPACE));
		assertThat(manager.findVocabularyByPrefix("bar"), isEmpty());
		assertThat(manager.findPrefixForVocabulary(EG_NAMESPACE), isPresentAndIs("foo"));
		manager.registerPrefix("bar", EG_NAMESPACE);
		assertThat(manager.findVocabularyByPrefix("foo"), isPresentAndIs(EG_NAMESPACE));
		assertThat(manager.findVocabularyByPrefix("bar"), isPresentAndIs(EG_NAMESPACE));
		assertThat(manager.findPrefixForVocabulary(EG_NAMESPACE), isPresentAndIs("foo"));
	}

	/** @see VocabularyManager#registerPrefix(String, URI) */
	@Test
	public void testVocabularyRegistrationOverridesPrefix() {
		final VocabularyManager manager = new VocabularyManager();
		assertThat(manager.findVocabularyByPrefix("foo"), isEmpty());
		assertThat(manager.findVocabularyByPrefix("bar"), isEmpty());
		assertThat(manager.findPrefixForVocabulary(EG_NAMESPACE), isEmpty());
		manager.registerVocabulary(EG_NAMESPACE, "foo");
		assertThat(manager.findVocabularyByPrefix("foo"), isPresentAndIs(EG_NAMESPACE));
		assertThat(manager.findVocabularyByPrefix("bar"), isEmpty());
		assertThat(manager.findPrefixForVocabulary(EG_NAMESPACE), isPresentAndIs("foo"));
		manager.registerVocabulary(EG_NAMESPACE, "bar");
		assertThat(manager.findVocabularyByPrefix("foo"), isPresentAndIs(EG_NAMESPACE));
		assertThat(manager.findVocabularyByPrefix("bar"), isPresentAndIs(EG_NAMESPACE));
		assertThat(manager.findPrefixForVocabulary(EG_NAMESPACE), isPresentAndIs("bar"));
	}

	/** @see VocabularyManager#registerAll(VocabularyRegistry) */
	@Test
	public void testRegisterAllMaintainsMultiplePrefixRegistrationsWithSameVocabulary() {
		final VocabularyManager manager1 = new VocabularyManager();
		manager1.registerPrefix("foo", EG_NAMESPACE);
		manager1.registerPrefix("bar", EG_NAMESPACE);
		final VocabularyManager manager2 = new VocabularyManager();
		manager2.registerAll(manager1);
		assertThat(manager2.findVocabularyByPrefix("foo"), isPresentAndIs(EG_NAMESPACE));
		assertThat(manager2.findVocabularyByPrefix("bar"), isPresentAndIs(EG_NAMESPACE));
		assertThat(manager2.findPrefixForVocabulary(EG_NAMESPACE), isPresentAndIs("foo"));
	}

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
	public void testDetermineVocabularyPrefixDoesNotUseRootPath() {
		final VocabularyManager manager = new VocabularyManager();
		assertThat(manager.determinePrefixForVocabulary(URI.create("http://example.com/")), not(ROOT_PATH));
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
		final String generatedPrefix = BaseVocabularySpecification.PREFIX_PREFIX + "1";
		assertThat(manager.isPrefixRegistered(generatedPrefix), is(false));
		assertThat(manager.determinePrefixForVocabulary(URI.create("http://example.com/foo/bar")), is(generatedPrefix));
		assertThat(manager.isPrefixRegistered(generatedPrefix), is(true));
	}

	/** @see VocabularyManager#determinePrefixForVocabulary(URI) */
	@Test
	public void testDetermineVocabularyPrefixGeneratesPrefixWhenCannotBeAutoDeterminedFromURI() {
		final VocabularyManager manager = new VocabularyManager();
		final String generatedPrefix = BaseVocabularySpecification.PREFIX_PREFIX + "1";
		assertThat(manager.isPrefixRegistered(generatedPrefix), is(false));
		assertThat(manager.determinePrefixForVocabulary(URI.create("http://example.com/foo%20bar")), is(generatedPrefix));
		assertThat(manager.isPrefixRegistered(generatedPrefix), is(true));
	}

	/** @see VocabularyManager#determinePrefixForVocabulary(URI) */
	@Test
	public void testDetermineVocabularyPrefixGeneratesUniquePrefixes() {
		final VocabularyManager manager = new VocabularyManager();
		assertThat(manager.determinePrefixForVocabulary(URI.create("http://example.com/foo%20bar")), is(BaseVocabularySpecification.PREFIX_PREFIX + "1"));
		assertThat(manager.determinePrefixForVocabulary(URI.create("http://example.com/bar%20foo")), is(BaseVocabularySpecification.PREFIX_PREFIX + "2"));
	}

	/** @see VocabularyManager#determinePrefixForVocabulary(URI) */
	@Test
	public void testDetermineVocabularyPrefixGeneratesAnotherPrefixGeneratedPrefixAlreadyRegistered() {
		final VocabularyManager manager = new VocabularyManager();
		final String generatedPrefix1 = BaseVocabularySpecification.PREFIX_PREFIX + "1";
		manager.registerPrefix(generatedPrefix1, URI.create("http://example.com/some/other/namespace"));
		assertThat(manager.isPrefixRegistered(generatedPrefix1), is(true));
		final String generatedPrefix2 = BaseVocabularySpecification.PREFIX_PREFIX + "2";
		assertThat(manager.isPrefixRegistered(generatedPrefix2), is(false));
		assertThat(manager.determinePrefixForVocabulary(URI.create("http://example.com/foo%20bar")), is(generatedPrefix2));
		assertThat(manager.isPrefixRegistered(generatedPrefix2), is(true));
	}

	/** @see VocabularyManager#determinePrefixForVocabulary(URI) */
	@Test
	public void testDetermineVocabularyPrefixUsesKnownVocabularyPrefix() {
		final URI fooNamespace = URI.create("http://example.com/foo");
		final VocabularyRegistry knownVocabularies = VocabularyRegistry.of(new SimpleImmutableEntry<>("bar", fooNamespace));
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
		final VocabularyRegistry knownVocabularies = VocabularyRegistry.of(new SimpleImmutableEntry<>("bar", fooNamespace),
				new SimpleImmutableEntry<>("other", otherNamespace));
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
		final VocabularyRegistry knownVocabularies = VocabularyRegistry.of(new SimpleImmutableEntry<>("bar", fooNamespace),
				new SimpleImmutableEntry<>("other", otherNamespace));
		final VocabularyManager manager = new VocabularyManager(knownVocabularies, true); //turn on auto-register
		assertThat(manager.isPrefixRegistered("bar"), is(false));
		assertThat(manager.findPrefixForVocabulary(fooNamespace), isPresentAndIs("bar"));
		assertThat(manager.isPrefixRegistered("bar"), is(true));
	}

	/** @see VocabularyManager#determinePrefixForTerm(URI) */
	@Test
	public void testDeterminePrefixForTerm() {
		final VocabularyRegistry knownVocabularies = VocabularyRegistry.of(new SimpleImmutableEntry<>("cc", CC_NAMESPACE));
		final VocabularyManager manager = new VocabularyManager(knownVocabularies);
		manager.setDefaultVocabulary(URI.create("http://example.com/"));
		manager.registerPrefix("dc", DC_NAMESPACE);
		manager.registerPrefix("og", OG_NAMESPACE);

		assertThat(manager.determinePrefixForTerm(URI.create("http://example.com/")), isEmpty()); //not a term
		assertThat(manager.determinePrefixForTerm(URI.create("http://example.com/foo")), isPresentAndIs(
				new SimpleImmutableEntry<>(VocabularyTerm.of(URI.create("http://example.com/"), "foo"), BaseVocabularySpecification.PREFIX_PREFIX + "1"))); //set as default; prefix has to be generated
		assertThat(manager.determinePrefixForTerm(URI.create("https://example.com/foo")), isPresentAndIs(
				new SimpleImmutableEntry<>(VocabularyTerm.of(URI.create("https://example.com/"), "foo"), BaseVocabularySpecification.PREFIX_PREFIX + "2"))); //not registered; prefix has to be generated
		assertThat(manager.determinePrefixForTerm(URI.create("http://example.com/foo/bar")),
				isPresentAndIs(new SimpleImmutableEntry<>(VocabularyTerm.of(URI.create("http://example.com/foo/"), "bar"), "foo"))); //not registered; prefix from path
		assertThat(manager.determinePrefixForTerm(URI.create("http://purl.org/dc/terms/creator")),
				isPresentAndIs(new SimpleImmutableEntry<>(VocabularyTerm.of(DC_NAMESPACE, "creator"), "dc")));
		assertThat(manager.determinePrefixForTerm(URI.create("http://ogp.me/ns#title")),
				isPresentAndIs(new SimpleImmutableEntry<>(VocabularyTerm.of(OG_NAMESPACE, "title"), "og")));
		assertThat(manager.determinePrefixForTerm(URI.create("http://creativecommons.org/ns#permits")),
				isPresentAndIs(new SimpleImmutableEntry<>(VocabularyTerm.of(CC_NAMESPACE, "permits"), "cc"))); //not registered; known vocabulary
	}

	/** @see VocabularyManager#determineCurie(VocabularyTerm) */
	@Test
	public void testDetermineCurie() {
		final VocabularyRegistry knownVocabularies = VocabularyRegistry.of(new SimpleImmutableEntry<>("cc", CC_NAMESPACE));
		final VocabularyManager manager = new VocabularyManager(knownVocabularies);
		manager.setDefaultVocabulary(EG_NAMESPACE);
		manager.registerPrefix("dc", DC_NAMESPACE);
		manager.registerPrefix("og", OG_NAMESPACE);
		assertThat(manager.determineCurie(VocabularyTerm.of(EG_NAMESPACE, "bar")), is(Curie.parse("bar"))); //default namespace, no registered prefix
		assertThat(manager.determineCurie(VocabularyTerm.of(URI.create("http://example.com/foo/"), "bar")), is(Curie.parse("foo:bar"))); //prefix has to be registered
		assertThat(manager.determineCurie(VocabularyTerm.of(URI.create("http://example.com/not%20registered/"), "test")),
				is(Curie.parse(BaseVocabularySpecification.PREFIX_PREFIX + "1:test"))); //prefix has to be generated
		assertThat(manager.determineCurie(VocabularyTerm.of(DC_NAMESPACE, "creator")), is(Curie.parse("dc:creator")));
		assertThat(manager.determineCurie(VocabularyTerm.of(OG_NAMESPACE, "title")), is(Curie.parse("og:title")));
		assertThat(manager.determineCurie(VocabularyTerm.of(CC_NAMESPACE, "permits")), is(Curie.parse("cc:permits"))); //known namespace
	}

}
