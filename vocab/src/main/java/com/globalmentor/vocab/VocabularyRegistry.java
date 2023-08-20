/*
 * Copyright © 1996-2019 GlobalMentor, Inc. <https://www.globalmentor.com/>
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

import static com.globalmentor.util.Optionals.*;
import static java.util.Arrays.*;
import static java.util.Collections.*;
import static java.util.Objects.*;

import java.net.URI;
import java.util.*;

import javax.annotation.*;

/**
 * Map of vocabulary namespaces and their associated prefixes.
 * <p>
 * Each vocabulary namespace may be associated with more than one prefix, but each vocabulary namespace will have at most one prefix officially registered for
 * it.
 * </p>
 * <p>
 * A prefix will never have a <code>null</code> namespace associated with it, and a namespace will never be mapped to a <code>null</code> prefix. However the
 * registry allows one namespace to be specified as a <dfn>default</dfn> namespace, indicating it is is the namespace intended if no prefix is indicated, and
 * that no prefix need be used for that namespace.
 * </p>
 * @apiNote A prefix is a token used as a substitute for the namespace URI in serializations such as XML and RDFa.
 * @see <a href="https://www.w3.org/TR/rdfa-core/#s_curies">RDFa Core 1.1, 6. CURIE Syntax Definition</a>
 * @see <a href="https://www.w3.org/TR/xml-names/">Namespaces in XML 1.0</a>
 * @author Garret Wilson
 */
public interface VocabularyRegistry {

	/**
	 * Creates a read-only vocabulary with the indicated default vocabulary and given registrations.
	 * @implSpec The {@link VocabularySpecification#DEFAULT} vocabulary specification is used.
	 * @param defaultVocabulary The namespace URI of the default vocabulary.
	 * @param vocabulariesByPrefix Vocabulary prefixes and vocabulary namespace URIs associated with them. If a prefix is mapped to more than one namespace, only
	 *          the first prefix will be reverse-mapped back to the namespace.
	 * @return An immutable vocabulary registry with the indicated registrations.
	 * @throws NullPointerException if a namespace is <code>null</code>.
	 */
	public static VocabularyRegistry of(@Nonnull URI defaultVocabulary, @Nonnull final Iterable<Map.Entry<String, URI>> vocabulariesByPrefix) {
		return new DefaultVocabularyRegistry(requireNonNull(defaultVocabulary), vocabulariesByPrefix);
	}

	/**
	 * Creates a read-only vocabulary with the given registrations.
	 * @implSpec The {@link VocabularySpecification#DEFAULT} vocabulary specification is used.
	 * @param vocabulariesByPrefix Vocabulary prefixes and vocabulary namespace URIs associated with them. If a prefix is mapped to more than one namespace, only
	 *          the first prefix will be reverse-mapped back to the namespace.
	 * @return An immutable vocabulary registry with the indicated registrations.
	 * @throws NullPointerException if a namespace is <code>null</code>.
	 */
	public static VocabularyRegistry of(@Nonnull final Iterable<Map.Entry<String, URI>> vocabulariesByPrefix) {
		return new DefaultVocabularyRegistry(null, vocabulariesByPrefix);
	}

	/**
	 * Creates a read-only vocabulary with the indicated default vocabulary and the given registrations.
	 * @implSpec The {@link VocabularySpecification#DEFAULT} vocabulary specification is used.
	 * @param defaultVocabulary The namespace URI of the default vocabulary.
	 * @param vocabulariesByPrefix Vocabulary prefixes and vocabulary namespace URIs associated with them. If a prefix is mapped to more than one namespace, only
	 *          the first prefix will be reverse-mapped back to the namespace.
	 * @return An immutable vocabulary registry with the indicated registrations.
	 * @throws NullPointerException if a namespace is <code>null</code>.
	 */
	@SafeVarargs
	@SuppressWarnings("varargs")
	public static VocabularyRegistry of(@Nonnull URI defaultVocabulary, final Map.Entry<String, URI>... vocabulariesByPrefix) {
		return of(defaultVocabulary, asList(vocabulariesByPrefix));
	}

	/**
	 * Creates a read-only vocabulary with the given registrations.
	 * @implSpec The {@link VocabularySpecification#DEFAULT} vocabulary specification is used.
	 * @param vocabulariesByPrefix Vocabulary prefixes and vocabulary namespace URIs associated with them. If a prefix is mapped to more than one namespace, only
	 *          the first prefix will be reverse-mapped back to the namespace.
	 * @return An immutable vocabulary registry with the indicated registrations.
	 * @throws NullPointerException if a namespace is <code>null</code>.
	 */
	@SafeVarargs
	@SuppressWarnings("varargs")
	public static VocabularyRegistry of(final Map.Entry<String, URI>... vocabulariesByPrefix) {
		return of(asList(vocabulariesByPrefix));
	}

	/**
	 * Creates a read-only vocabulary with the indicated default vocabulary and the given registrations.
	 * @implSpec The {@link VocabularySpecification#DEFAULT} vocabulary specification is used.
	 * @param defaultVocabulary The namespace URI of the default vocabulary.
	 * @param vocabulariesByPrefix Vocabulary prefixes and vocabulary namespace URIs associated with them. If a prefix is mapped to more than one namespace, only
	 *          the first prefix will be reverse-mapped back to the namespace.
	 * @return An immutable vocabulary registry with the indicated registrations.
	 * @throws NullPointerException if a namespace is <code>null</code>.
	 */
	public static VocabularyRegistry of(@Nonnull URI defaultVocabulary, @Nonnull final Map<String, URI> vocabulariesByPrefix) {
		return of(defaultVocabulary, vocabulariesByPrefix.entrySet());
	}

	/**
	 * Creates a read-only vocabulary with the given registrations.
	 * @implSpec The {@link VocabularySpecification#DEFAULT} vocabulary specification is used.
	 * @param vocabulariesByPrefix Vocabulary prefixes and vocabulary namespace URIs associated with them. If a prefix is mapped to more than one namespace, only
	 *          the first prefix will be reverse-mapped back to the namespace.
	 * @return An immutable vocabulary registry with the indicated registrations.
	 * @throws NullPointerException if a namespace is <code>null</code>.
	 */
	public static VocabularyRegistry of(@Nonnull final Map<String, URI> vocabulariesByPrefix) {
		return of(vocabulariesByPrefix.entrySet());
	}

	/** @return The specification governing vocabularies for this registry. */
	public VocabularySpecification getVocabularySpecification();

	/**
	 * Returns the default namespace.
	 * @apiNote The default namespace is semantically equivalent to the namespace associated with the <code>null</code> prefix, if that were to be allowed.
	 * @return The default namespace, if one is assigned.
	 */
	public Optional<URI> getDefaultVocabulary();

	/**
	 * Returns the number of prefixes registered.
	 * @apiNote There may be more prefixes than vocabularies registered, as multiple prefixes may be mapped to the same vocabulary.
	 * @return The number of registered prefixes.
	 */
	public int getRegisteredPrefixCount();

	/**
	 * Returns the number of vocabularies registered.
	 * @return The number of registered vocabularies.
	 */
	public int getRegisteredVocabularyCount();

	/**
	 * Determines whether the given prefix is registered with a vocabulary.
	 * @param prefix A prefix that may be associated with a vocabulary namespace.
	 * @return <code>true</code> if the given prefix has been associated with a vocabulary.
	 */
	public boolean isPrefixRegistered(@Nullable final String prefix);

	/**
	 * Determines whether the given vocabulary namespace URI is registered with a prefix, or has been specified as the default.
	 * @apiNote A vocabulary may be registered with multiple prefixes.
	 * @param namespace A namespace URI identifying a vocabulary.
	 * @return <code>true</code> if a prefix has been associated with the given namespace.
	 * @throws NullPointerException if the given namespace is <code>null</code>.
	 * @see #getDefaultVocabulary()
	 */
	public boolean isVocabularyRegistered(@Nonnull final URI namespace);

	/**
	 * Finds a registered vocabulary by its prefix.
	 * @apiNote The same namespace may be associated with several prefixes.
	 * @param prefix The prefix of the vocabulary to return.
	 * @return The namespace URI identifying the vocabulary, if one is registered.
	 * @throws NullPointerException if the given prefix is <code>null</code>.
	 */
	public Optional<URI> findVocabularyByPrefix(@Nonnull final String prefix);

	/**
	 * Retrieves the prefix to use for the given vocabulary namespace.
	 * @apiNote At most one prefix will be associated with a namespace.
	 * @param namespace The URI of the namespace for which a prefix should be returned
	 * @return A prefix for use with the given namespace, if one is registered.
	 * @throws NullPointerException if the given namespace is <code>null</code>.
	 */
	public Optional<String> findPrefixForVocabulary(@Nonnull final URI namespace);

	/**
	 * Retrieves the prefix to use for the given term.
	 * @implSpec The default implementation determines the namespace and name using {@link #asVocabularyTerm(URI)} and then determines the prefix by delegating to
	 *           {@link #findPrefixForVocabulary(URI)}.
	 * @param term The absolute URI identifying a term in a vocabulary.
	 * @return A prefix for use with the given term, if the given URI can be converted to a term and if a prefix is registered.
	 * @throws NullPointerException if the given term is <code>null</code>.
	 * @throws IllegalArgumentException if the given URI is not absolute.
	 */
	public default Optional<Map.Entry<VocabularyTerm, String>> findPrefixForTerm(@Nonnull final URI term) {
		return asVocabularyTerm(term).flatMap(
				vocabularyTerm -> findPrefixForVocabulary(vocabularyTerm.getNamespace()).map(prefix -> new AbstractMap.SimpleImmutableEntry<>(vocabularyTerm, prefix)));
	}

	/**
	 * Determines a vocabulary term from a given term URI based upon the vocabulary specification rules.
	 * @apiNote This method is for retrieving term components from a URI, possibly with help from recognized vocabularies, but there is no guarantee that the
	 *          returned term is from a registered or even recognized vocabulary.
	 * @implSpec The default implementation delegates to {@link VocabularySpecification#isNamespaceRegular(URI)}.
	 * @implSpec This method can only determine namespaces that are <dfn>regular</dfn> as per {@link VocabularySpecification#isNamespaceRegular(URI)}.
	 * @implNote If it is ever necessary to retrieve irregular namespaces (e.g. <code>http://www.w3.org/XML/1998/namespace</code>), this can be implemented in the
	 *           following manner in {@link AbstractVocabularyRegistry}:
	 *           <ol>
	 *           <li>As each namespace is added, determine if it is regular.</li>
	 *           <li>Add irregular namespaces to a separate set.</li>
	 *           <li>When this method is called, first check to see if the term has any of the irregular namespaces as a proper prefix (an expensive operation if
	 *           there are many irregular namespaces).</li>
	 *           </ol>
	 * @param term The absolute URI identifying a term in a vocabulary.
	 * @return The namespace and name of the term, which may be empty if the namespace could not be determined or the given string itself is a regular namespace.
	 * @throws NullPointerException if the given term is <code>null</code>.
	 * @throws IllegalArgumentException if the given URI is not absolute.
	 * @see #getVocabularySpecification()
	 */
	public default Optional<VocabularyTerm> asVocabularyTerm(@Nonnull final URI term) {
		return getVocabularySpecification().findTermNamespace(term).map(namespace -> {
			final String termString = term.toString();
			final String namespaceString = namespace.toString();
			assert termString.startsWith(namespaceString);
			final int namespaceLength = namespaceString.length();
			return VocabularyTerm.of(namespace, termString.substring(namespaceLength));
		});
	}

	/**
	 * Finds the vocabulary term the given CURIE represents by looking the namespace registered with its prefix or, if the CURIE does not indicate a prefix, the
	 * namespace registered as the default namespace, if any.
	 * @param curie The CURIE to use for looking up a vocabulary term.
	 * @return The vocabulary term the CURIE represents if the indicated or default prefix is registered.
	 * @throws IllegalArgumentException if the CURIE's reference combined with the registered vocabulary namespace does not form a valid URI.
	 * @see #findVocabularyByPrefix(String)
	 * @see #getDefaultVocabulary()
	 */
	public default Optional<VocabularyTerm> findVocabularyTerm(@Nonnull final Curie curie) {
		return curie.getPrefix().map(this::findVocabularyByPrefix).orElseGet(this::getDefaultVocabulary)
				.map(namespace -> VocabularyTerm.of(namespace, curie.getReference()));
	}

	/**
	 * Finds the CURIE that would represent the given vocabulary term using the registered vocabularies. If a prefix is registered for the term namespace, it is
	 * used. Otherwise if the term namespace is set as the default namespace, a CURIE with no prefix is returned.
	 * @implSpec The default implementation calls {@link #findPrefixForVocabulary(URI)}.
	 * @param vocabularyTerm The vocabulary term for which a CURIE should be found.
	 * @return The CURIE to represent the given vocabulary term, which may not be present if no prefix was registered with the vocabulary term namespace.
	 * @see #getDefaultVocabulary()
	 */
	public default Optional<Curie> findCurie(@Nonnull final VocabularyTerm vocabularyTerm) {
		final URI namespace = vocabularyTerm.getNamespace();
		final String prefix = findPrefixForVocabulary(vocabularyTerm.getNamespace()).orElse(null); //prefer mapped prefixes over the default
		if(prefix == null) { //if there was no registered prefix, we only want to create a CURIE if the namespace is the default
			if(!isPresentAndEquals(getDefaultVocabulary(), namespace)) {
				return Optional.empty();
			}
		}
		return Optional.of(Curie.of(prefix, vocabularyTerm.getName())); //here a null prefix indicates the namespace was the default
	}

	/**
	 * Finds the CURIE that would represent the given term using the registered vocabularies. If a prefix is registered for the term namespace, it is used.
	 * Otherwise if the term namespace is set as the default namespace, a CURIE with no prefix is returned.
	 * @implSpec The default implementation calls {@link #asVocabularyTerm(URI)} and then delegates to {@link #findCurie(VocabularyTerm)}.
	 * @param term The absolute URI identifying a term in a vocabulary.
	 * @return The CURIE to represent the given term, which may not be present if no prefix was registered with the vocabulary term namespace.
	 */
	public default Optional<Curie> findCurieForTerm(@Nonnull final URI term) {
		return asVocabularyTerm(term).flatMap(this::findCurie);
	}

	/**
	 * Returns the registered vocabularies and their associated prefixes, neither of which will be <code>null</code> for any registration. No two vocabularies
	 * will have the same associated prefix. The returned set may be live; if the consumer intends to update the registry during iteration, a defensive copy
	 * should be made.
	 * @apiNote This method may not include all the prefixes returned by {@link #getRegisteredVocabulariesByPrefix()}, as that method allows multiple prefixes to
	 *          be mapped to the same namespace.
	 * @return A set of vocabulary namespace URI values and the prefixes with which they are associated.
	 */
	public Set<Map.Entry<URI, String>> getRegisteredPrefixesByVocabulary();

	/**
	 * Returns the registered vocabularies associated with their registered prefixes, neither of which will be <code>null</code> for any registration. Some
	 * vocabularies may be associated with multiple prefixes. The returned set may be live; if the consumer intends to update the registry during iteration, a
	 * defensive copy should be made.
	 * @return A set of vocabulary namespace URI values and the prefixes with which they are associated.
	 */
	public Set<Map.Entry<String, URI>> getRegisteredVocabulariesByPrefix();

	/** @return <code>true</code> if there are no prefix or vocabulary registrations at all. */
	public boolean isEmpty();

	/**
	 * Creates a builder.
	 * @implSpec The {@link VocabularySpecification#DEFAULT} vocabulary specification is used.
	 * @return A new builder with the default vocabulary specification.
	 */
	public static Builder builder() {
		return builder(VocabularySpecification.DEFAULT);
	}

	/**
	 * Creates a builder with the given specification.
	 * @param vocabularySpecification The specification governing vocabularies in the registry being built.
	 * @return A new builder with the given vocabulary specification.
	 */
	public static Builder builder(@Nonnull VocabularySpecification vocabularySpecification) {
		return new VocabularyRegistryBuilderImpl(vocabularySpecification);
	}

	/**
	 * Creates a builder with values copied from an existing vocabulary registry.
	 * @param vocabularyRegistry The vocabulary registry with the values to copy.
	 * @return A new builder with the same vocabulary specification, default vocabulary, and mappings as the given registry.
	 */
	public static Builder builder(@Nonnull VocabularyRegistry vocabularyRegistry) {
		return new VocabularyRegistryBuilderImpl(vocabularyRegistry);
	}

	/** An immutable empty vocabulary registry using the {@link VocabularySpecification#DEFAULT} default vocabulary specification. */
	public static final VocabularyRegistry EMPTY = new VocabularyRegistry() {

		@Override
		public VocabularySpecification getVocabularySpecification() {
			return VocabularySpecification.DEFAULT;
		}

		@Override
		public Optional<URI> getDefaultVocabulary() {
			return Optional.empty();
		}

		@Override
		public int getRegisteredPrefixCount() {
			return 0;
		}

		@Override
		public int getRegisteredVocabularyCount() {
			return 0;
		}

		@Override
		public boolean isPrefixRegistered(final String prefix) {
			requireNonNull(prefix);
			return false;
		}

		@Override
		public boolean isVocabularyRegistered(final URI namespace) {
			requireNonNull(namespace);
			return false;
		}

		@Override
		public Optional<URI> findVocabularyByPrefix(final String prefix) {
			requireNonNull(prefix);
			return Optional.empty();
		}

		@Override
		public Optional<String> findPrefixForVocabulary(final URI namespace) {
			requireNonNull(namespace);
			return Optional.empty();
		}

		@Override
		public Set<Map.Entry<URI, String>> getRegisteredPrefixesByVocabulary() {
			return emptySet();
		}

		@Override
		public Set<Map.Entry<String, URI>> getRegisteredVocabulariesByPrefix() {
			return emptySet();
		}

		@Override
		public boolean isEmpty() {
			return true;
		}

	};

	/**
	 * Builder for a vocabulary registry.
	 * @author Garret Wilson
	 */
	public interface Builder {

		/**
		 * Sets the default vocabulary.
		 * @param namespace The namespace URI of the new default vocabulary.
		 * @return This builder.
		 * @throws NullPointerException if the given namespace is <code>null</code>.
		 */
		public Builder setDefaultVocabulary(@Nonnull final URI namespace);

		/**
		 * Adds a vocabulary and associates the given prefix with it, so that any vocabulary lookup by namespace will retrieve the given prefix. The vocabulary will
		 * also be associated with the prefix, so that any later vocabulary lookup by prefix will return the given namespace.
		 * @apiNote This method overrides any previous namespaces associated with the given prefix, as well as any previous prefixes associated with the given
		 *          namespace, unlike {@link #registerPrefix(String, URI)}, which does not override any existing prefixes associated with the given namespace.
		 * @param namespace The URI of the namespace of the vocabulary to register.
		 * @param prefix The prefix to associate with the vocabulary.
		 * @return This builder.
		 * @throws NullPointerException if the given namespace is <code>null</code>.
		 * @throws IllegalArgumentException if the given prefix is not valid.
		 */
		public Builder registerVocabulary(@Nonnull final URI namespace, @Nullable final String prefix);

		/**
		 * Adds a prefix associates the indicated vocabulary with it, so that any vocabulary lookup by prefix will retrieve the given namespace. If the prefix has
		 * not already been associated with another vocabulary, it will also be associated with the vocabulary, so that any later vocabulary lookup by prefix will
		 * return the given namespace.
		 * @apiNote This method differs from {@link #registerVocabulary(URI, String)} in that this method will not change any namespace to prefix mapping if it
		 *          already exists, allowing multiple prefixes to map to the same prefix. If it is desired to set the canonical prefix for a vocabulary, even if one
		 *          has already been registered, call {@link #registerVocabulary(URI, String)}.
		 * @param prefix The prefix to register.
		 * @param namespace The URI of the namespace of the vocabulary to associate with the prefix.
		 * @return This builder.
		 * @throws NullPointerException if the given namespace is <code>null</code>.
		 * @throws IllegalArgumentException if the given prefix is not valid.
		 */
		public Builder registerPrefix(@Nullable final String prefix, @Nonnull final URI namespace);

		/**
		 * Registers all registered vocabularies in the given registry by adding the same namespace to prefix and prefix to namespace mappings as the given
		 * registry.
		 * @param registry The registry containing the registrations to register.
		 * @return This builder.
		 * @throws NullPointerException if the given registry is <code>null</code>.
		 * @throws IllegalArgumentException if one of the given prefixes in the given registry is not valid.
		 */
		public Builder registerAll(@Nonnull final VocabularyRegistry registry);

		/**
		 * Builds a vocabulary registry the state of this builder.
		 * @return A new read-only vocabulary registry with the current values.
		 */
		public VocabularyRegistry build();

	}

}
