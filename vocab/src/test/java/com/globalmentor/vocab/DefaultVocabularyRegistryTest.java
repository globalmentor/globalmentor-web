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
import java.util.HashSet;
import java.util.Map;

import org.junit.jupiter.api.*;

import com.globalmentor.collections.NameValuePairMapEntry;

/**
 * Tests of {@link DefaultVocabularyRegistry}.
 * @author Garret Wilson
 */
public class DefaultVocabularyRegistryTest {

	private static final URI DC_NAMESPACE = URI.create("http://purl.org/dc/terms/");
	private static final URI OG_NAMESPACE = URI.create("http://ogp.me/ns#");
	private static final URI EG_NAMESPACE = URI.create("https://example.com/ns/");

	@Test
	public void testEmptyRegistry() {
		final DefaultVocabularyRegistry registry = new DefaultVocabularyRegistry(emptySet());
		assertThat(registry.isPrefixRegistered("foo"), is(false));
		assertThat(registry.isVocabularyRegistered(DC_NAMESPACE), is(false));
		assertThat(registry.findVocabularyByPrefix("foo"), isEmpty());
		assertThat(registry.findPrefixRegistrationForVocabulary(DC_NAMESPACE), isEmpty());
		assertThat(registry.findVocabularyByPrefix("foo"), isEmpty());
		assertThat(registry.getRegisteredPrefixesByVocabulary(), is(emptySet()));
		assertThat(registry.getRegisteredVocabulariesByPrefix(), is(emptySet()));
	}

	/** Tests simple registrations and retrieval. <code>null</code> prefixes are tested as well. */
	@Test
	public void testRegistrations() {
		final DefaultVocabularyRegistry registry = new DefaultVocabularyRegistry(
				asList(Map.entry("dc", DC_NAMESPACE), new NameValuePairMapEntry<>(null, EG_NAMESPACE), Map.entry("og", OG_NAMESPACE)));

		assertThat(registry.isPrefixRegistered("dc"), is(true));
		assertThat(registry.isPrefixRegistered("foo"), is(false));
		assertThat(registry.isPrefixRegistered(null), is(true));
		assertThat(registry.isPrefixRegistered("og"), is(true));

		assertThat(registry.isVocabularyRegistered(DC_NAMESPACE), is(true));
		assertThat(registry.isVocabularyRegistered(URI.create("http://example.com/ns/")), is(false)); //difference in URI scheme
		assertThat(registry.isVocabularyRegistered(EG_NAMESPACE), is(true));
		assertThat(registry.isVocabularyRegistered(OG_NAMESPACE), is(true));

		assertThat(registry.findPrefixRegistrationForVocabulary(DC_NAMESPACE), isPresentAndIs(Map.entry(DC_NAMESPACE, "dc")));
		assertThat(registry.findPrefixRegistrationForVocabulary(URI.create("http://example.com/ns/")), isEmpty());
		assertThat(registry.findPrefixRegistrationForVocabulary(EG_NAMESPACE), isPresentAndIs(new NameValuePairMapEntry<>(EG_NAMESPACE, null)));
		assertThat(registry.findPrefixRegistrationForVocabulary(OG_NAMESPACE), isPresentAndIs(Map.entry(OG_NAMESPACE, "og")));

		assertThat(registry.findVocabularyByPrefix("dc"), isPresentAndIs(DC_NAMESPACE));
		assertThat(registry.findVocabularyByPrefix("foo"), isEmpty());
		assertThat(registry.findVocabularyByPrefix(null), isPresentAndIs(EG_NAMESPACE));
		assertThat(registry.findVocabularyByPrefix("og"), isPresentAndIs(OG_NAMESPACE));

		assertThat(registry.getRegisteredPrefixesByVocabulary(),
				is(new HashSet<>(asList(Map.entry(DC_NAMESPACE, "dc"), new NameValuePairMapEntry<>(EG_NAMESPACE, null), Map.entry(OG_NAMESPACE, "og")))));

		assertThat(registry.getRegisteredVocabulariesByPrefix(),
				is(new HashSet<>(asList(Map.entry("dc", DC_NAMESPACE), new NameValuePairMapEntry<>(null, EG_NAMESPACE), Map.entry("og", OG_NAMESPACE)))));
	}

}
