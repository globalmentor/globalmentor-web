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

package com.globalmentor.vocab;

import static java.util.Arrays.*;
import static java.util.Collections.*;

import java.net.URI;
import java.util.*;
import java.util.Map.Entry;

import javax.annotation.*;

/**
 * Map of vocabulary namespaces and their associated prefixes.
 * <p>
 * Each vocabulary namespace may be associated with more than one prefix, but each vocabulary namespace will have at most one prefix officially registered for
 * it.
 * </p>
 * <p>
 * A prefix will never have a <code>null</code> namespace associated with it, but depending on the implementation, a namespace may be mapped to a
 * <code>null</code> prefix indicating no prefix (e.g. <code>bar</code>) as opposed to a prefix of the empty string (e.g. <code>:bar</code>).
 * @apiNote A prefix is a token used as a substitute for the namespace URI in serializations such as XML and RDFa.
 * @see <a href="https://www.w3.org/TR/rdfa-core/#s_curies">RDFa Core 1.1, 6. CURIE Syntax Definition</a>
 * @see <a href="https://www.w3.org/TR/xml-names/">Namespaces in XML 1.0</a>
 * @author Garret Wilson
 */
public interface VocabularyRegistry {

	/**
	 * Creates a read-only vocabulary with the given registrations.
	 * @param vocabulariesByPrefix Vocabulary prefixes and vocabulary namespace URIs associated with them. If a prefix is mapped to more than one namespace, only
	 *          the first prefix will be reverse-mapped back to the namespace.
	 * @throws NullPointerException if a namespace is <code>null</code>.
	 */
	public static VocabularyRegistry of(@Nonnull final Iterable<Map.Entry<String, URI>> vocabulariesByPrefix) {
		return new DefaultVocabularyRegistry(vocabulariesByPrefix);
	}

	/**
	 * Creates a read-only vocabulary with the given registrations.
	 * @param vocabulariesByPrefix Vocabulary prefixes and vocabulary namespace URIs associated with them. If a prefix is mapped to more than one namespace, only
	 *          the first prefix will be reverse-mapped back to the namespace.
	 * @throws NullPointerException if a namespace is <code>null</code>.
	 */
	@SafeVarargs
	public static VocabularyRegistry of(final Map.Entry<String, URI>... vocabulariesByPrefix) {
		return of(asList(vocabulariesByPrefix));
	}

	/**
	 * Creates a read-only vocabulary with the given registrations.
	 * @param vocabulariesByPrefix Vocabulary prefixes and vocabulary namespace URIs associated with them. If a prefix is mapped to more than one namespace, only
	 *          the first prefix will be reverse-mapped back to the namespace.
	 * @throws NullPointerException if a namespace is <code>null</code>.
	 */
	public static VocabularyRegistry of(@Nonnull final Map<String, URI> vocabulariesByPrefix) {
		return of(vocabulariesByPrefix.entrySet());
	}

	/**
	 * Determines whether the given prefix is registered with a vocabulary.
	 * @param prefix A prefix that may be associated with a vocabulary namespace.
	 * @return <code>true</code> if the given prefix has been associated with a vocabulary.
	 */
	public boolean isPrefixRegistered(@Nullable final String prefix);

	/**
	 * Determines whether the given namespace URI is registered with a prefix.
	 * @apiNote A vocabulary may be registered with multiple prefixes.
	 * @param namespace A namespace URI identifying a vocabulary.
	 * @return <code>true</code> if a prefix has been associated with the given namespace.
	 * @throws NullPointerException if the given namespace is <code>null</code>.
	 */
	public boolean isVocabularyRegistered(@Nonnull final URI namespace);

	/**
	 * Finds a registered vocabulary by its prefix.
	 * @apiNote The same namespace may be associated with several prefixes.
	 * @param prefix The prefix of the vocabulary to return.
	 * @return The namespace URI identifying the vocabulary, if one is registered.
	 */
	public Optional<URI> findVocabularyByPrefix(@Nullable final String prefix);

	/**
	 * Retrieves the prefix to use for the given vocabulary namespace.
	 * @apiNote At most one prefix will be associated with a namespace.
	 * @apiNote This method returns a {@link Map.Entry} "registration" rather than only the prefix to distinguish between no registered prefix and a registered
	 *          prefix of <code>null</code>.
	 * @param namespace The URI of the namespace for which a prefix should be returned
	 * @return A prefix for use with the given namespace, if one is registered; the prefix value itself may be <code>null</code>.
	 * @throws NullPointerException if the given namespace is <code>null</code>.
	 */
	public Optional<Map.Entry<URI, String>> findPrefixRegistrationForVocabulary(@Nonnull final URI namespace);

	/**
	 * Returns the registered vocabularies and their associated prefixes. No two vocabularies will have the same associated prefix. The returned set will be
	 * read-only and iteration will not throw {@link ConcurrentModificationException}, but it may reflect changes made concurrently by the registry.
	 * @apiNote This method may not include all the prefixes returned by {@link #getRegisteredVocabulariesByPrefix()}, as that method allows multiple prefixes to
	 *          be mapped to the same namespace.
	 * @return A set of vocabulary namespace URI values and the prefixes with which they are associated.
	 */
	public Set<Map.Entry<URI, String>> getRegisteredPrefixesByVocabulary();

	/**
	 * Returns the registered vocabularies associated with their registered prefixes. Some vocabularies may be associated with multiple prefixes. The returned set
	 * will be read-only and iteration will not throw {@link ConcurrentModificationException}, but it may reflect changes made concurrently by the registry.
	 * @return A set of vocabulary namespace URI values and the prefixes with which they are associated.
	 */
	public Set<Map.Entry<String, URI>> getRegisteredVocabulariesByPrefix();

	/**
	 * Retrieves the namespace, if it can be determined, identifying the vocabulary the term is in.
	 * @param term The URI identifying the term in a vocabulary.
	 * @return The namespace representing the vocabulary of the term, or <code>null</code> if no vocabulary could be determined.
	 * @throws NullPointerException if the given namespace is <code>null</code>.
	 */
	//TODO public Optional<URI> findTermVocabulary(@Nonnull final URI term);

	/** An immutable empty vocabulary registry. */
	public static final VocabularyRegistry EMPTY = new VocabularyRegistry() {

		@Override
		public boolean isPrefixRegistered(final String prefix) {
			return false;
		}

		@Override
		public boolean isVocabularyRegistered(final URI namespace) {
			return false;
		}

		@Override
		public Optional<URI> findVocabularyByPrefix(final String prefix) {
			return Optional.empty();
		}

		@Override
		public Optional<Map.Entry<URI, String>> findPrefixRegistrationForVocabulary(final URI namespace) {
			return Optional.empty();
		}

		@Override
		public Set<Entry<URI, String>> getRegisteredPrefixesByVocabulary() {
			return emptySet();
		}

		@Override
		public Set<Entry<String, URI>> getRegisteredVocabulariesByPrefix() {
			return emptySet();
		}

	};

}
