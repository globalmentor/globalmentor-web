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

import java.net.URI;
import java.util.*;

import javax.annotation.*;

import static com.globalmentor.net.URIs.*;

/**
 * Mutable map of vocabulary namespaces and their associated prefixes, allowing new registrations.
 * <p>
 * A vocabulary registry distinguishes between namespace/prefix associations that have actually been registered, and <dfn>recognized</dfn> vocabularies which
 * the include those the registry knows about by default. Recognized vocabularies are used when a determining a prefix using
 * {@link #determinePrefixForVocabulary(URI)}. Recognized vocabularies aren't usually used when looking up vocabularies and prefixes unless auto-register has
 * been turned on.
 * </p>
 * @author Garret Wilson
 */
public interface VocabularyRegistrar extends VocabularyRegistry {

	/**
	 * Indicates whether the auto-register feature has been turned on.
	 * @return <code>true</code> if vocabularies and prefixes will be registered automatically when searching if they are recognized.
	 * @see #findPrefixForVocabulary(URI)
	 */
	public boolean isAutoRegister();

	/**
	 * Returns the registry of known but not necessarily registered vocabularies.
	 * @apiNote Recognized vocabularies are the union of known and registered vocabularies.
	 * @return The vocabularies that are already known, independent from registration.
	 */
	public VocabularyRegistry getKnownVocabularies();

	/**
	 * Sets the default vocabulary.
	 * @param namespace The namespace URI of the new default vocabulary.
	 * @return The old default vocabulary, if any.
	 * @throws NullPointerException if the given namespace is <code>null</code>.
	 */
	public Optional<URI> setDefaultVocabulary(@Nonnull final URI namespace);

	/**
	 * Determines whether the given namespace URI is that of a recognized vocabulary. A vocabulary is <dfn>recognized</dfn> if it has been registered, or if there
	 * is a default prefix that is <dfn>known</dfn> that would be assigned if one were needed, e.g. by calling {@link #determinePrefixForVocabulary(URI)}.
	 * @apiNote Not every recognized vocabulary will have been registered. Some are recognized by default, such as by some initial context, depending on the
	 *          implementation.
	 * @implSpec The default implementation returns <code>true</code> if the vocabulary has been registered as per {@link #isVocabularyRegistered(URI)} or
	 *           registered in {@link #getKnownVocabularies()}.
	 * @param namespace A namespace URI identifying a vocabulary.
	 * @return <code>true</code> if a prefix has been associated with the given vocabulary namespace or one could be determined if needed.
	 * @see #isVocabularyRegistered(URI)
	 * @see #getKnownVocabularies()
	 */
	public default boolean isVocabularyRecognized(@Nonnull final URI namespace) {
		return isVocabularyRegistered(namespace) || getKnownVocabularies().isVocabularyRegistered(namespace);
	}

	/**
	 * {@inheritDoc} If auto-register has been turned on, a vocabulary will be registered and returned if the prefix recognized, even if it was not registered
	 * before.
	 * @see #isAutoRegister()
	 * @see #isVocabularyRecognized(URI)
	 */
	@Override
	public Optional<URI> findVocabularyByPrefix(@Nonnull final String prefix);

	/**
	 * {@inheritDoc} If auto-register has been turned on, a prefix will be registered and returned if the vocabulary is recognized, even if it was not registered
	 * before.
	 * @see #isAutoRegister()
	 * @see #isVocabularyRecognized(URI)
	 */
	@Override
	public Optional<String> findPrefixForVocabulary(@Nonnull final URI namespace);

	/**
	 * {@inheritDoc}
	 * @apiNote If auto-register has been turned on, this method may not return all the vocabularies that would be supported by
	 *          {@link #findPrefixForVocabulary(URI)}, as that method included recognized vocabularies not registered.
	 */
	@Override
	public Set<Map.Entry<URI, String>> getRegisteredPrefixesByVocabulary();

	/**
	 * {@inheritDoc}
	 * @apiNote If auto-register has been turned on, this method may not return all the vocabularies that would be supported by
	 *          {@link #findPrefixForVocabulary(URI)}, as that method included recognized vocabularies not registered.
	 */
	@Override
	public Set<Map.Entry<String, URI>> getRegisteredVocabulariesByPrefix();

	/**
	 * Adds a vocabulary and associates it with a default prefix.
	 * @param namespace The URI of the namespace of the vocabulary to register.
	 * @return <code>true</code> if the namespace was not previously known.
	 * @throws NullPointerException if the given namespace is <code>null</code>.
	 */
	public default boolean registerVocabulary(@Nonnull final URI namespace) {
		if(!isVocabularyRegistered(namespace)) { //if we don't know about this vocabulary
			determinePrefixForVocabulary(namespace); //determine a prefix for this namespace, which will register it
			assert isVocabularyRegistered(namespace);
			return true; //indicate that this is a new namespace
		} else { //if we already know about this vocabulary
			return false; //indicate that we already know about this vocabulary
		}
	}

	/**
	 * Adds a vocabulary and associates the given prefix with it, so that any vocabulary lookup by namespace will retrieve the given prefix. The vocabulary will
	 * also be associated with the prefix, so that any later vocabulary lookup by prefix will return the given namespace.
	 * @apiNote This method overrides any previous namespaces associated with the given prefix, as well as any previous prefixes associated with the given
	 *          namespace, unlike {@link #registerPrefix(Map.Entry)}, which does not override any existing prefixes associated with the given namespace.
	 * @implSpec The default implementation delegates to {@link #registerVocabulary(URI, String)}.
	 * @param prefixForNamespace The The URI of the namespace of the vocabulary to register, along with the prefix to associate with the vocabulary.
	 * @return The prefix registration, if any, that was previously made with the given namespace.
	 * @throws NullPointerException if the given namespace is <code>null</code>.
	 * @throws IllegalArgumentException if the given prefix is not valid.
	 * @see #getVocabularySpecification()
	 */
	public default Optional<Map.Entry<URI, String>> registerVocabulary(@Nonnull Map.Entry<URI, String> prefixForNamespace) {
		return registerVocabulary(prefixForNamespace.getKey(), prefixForNamespace.getValue());
	}

	/**
	 * Adds a vocabulary and associates the given prefix with it, so that any vocabulary lookup by namespace will retrieve the given prefix. The vocabulary will
	 * also be associated with the prefix, so that any later vocabulary lookup by prefix will return the given namespace.
	 * @apiNote This method overrides any previous namespaces associated with the given prefix, as well as any previous prefixes associated with the given
	 *          namespace, unlike {@link #registerPrefix(String, URI)}, which does not override any existing prefixes associated with the given namespace.
	 * @param namespace The URI of the namespace of the vocabulary to register.
	 * @param prefix The prefix to associate with the vocabulary.
	 * @return The prefix registration, if any, that was previously made with the given namespace.
	 * @throws NullPointerException if the given namespace and/or prefix is <code>null</code>.
	 * @throws IllegalArgumentException if the given prefix is not valid.
	 * @see #getVocabularySpecification()
	 */
	public Optional<Map.Entry<URI, String>> registerVocabulary(@Nonnull final URI namespace, @Nonnull final String prefix);

	/**
	 * Adds a prefix associates the indicated vocabulary with it, so that any vocabulary lookup by prefix will retrieve the given namespace. If the prefix has not
	 * already been associated with another vocabulary, it will also be associated with the vocabulary, so that any later vocabulary lookup by prefix will return
	 * the given namespace.
	 * @implSpec The default implementation delegates to {@link #registerPrefix(String, URI)}.
	 * @param namespaceByPrefix The prefix key to register, along with the the URI value of the namespace of the vocabulary to associate with the prefix.
	 * @return The namespace of the vocabulary, if any, that was previously associated with the given prefix.
	 * @throws NullPointerException if the given namespace value is <code>null</code>.
	 * @throws IllegalArgumentException if the given prefix is not valid.
	 * @see #getVocabularySpecification()
	 */
	public default Optional<URI> registerPrefix(@Nonnull Map.Entry<String, URI> namespaceByPrefix) {
		return registerPrefix(namespaceByPrefix.getKey(), namespaceByPrefix.getValue());
	}

	/**
	 * Adds a prefix associates the indicated vocabulary with it, so that any vocabulary lookup by prefix will retrieve the given namespace. If the prefix has not
	 * already been associated with another vocabulary, it will also be associated with the vocabulary, so that any later vocabulary lookup by prefix will return
	 * the given namespace.
	 * @apiNote This method differs from {@link #registerVocabulary(URI, String)} in that this method will not change any namespace to prefix mapping if it
	 *          already exists, allowing multiple prefixes to map to the same prefix. If it is desired to set the canonical prefix for a vocabulary, even if one
	 *          has already been registered, call {@link #registerVocabulary(URI, String)}.
	 * @param prefix The prefix to register.
	 * @param namespace The URI of the namespace of the vocabulary to associate with the prefix.
	 * @return The namespace of the vocabulary, if any, that was previously associated with the given prefix.
	 * @throws NullPointerException if the given namespace is <code>null</code>.
	 * @throws IllegalArgumentException if the given prefix is not valid.
	 * @see #getVocabularySpecification()
	 */
	public Optional<URI> registerPrefix(@Nullable final String prefix, @Nonnull final URI namespace);

	/**
	 * Registers all registered vocabularies in the given registry by adding the same namespace to prefix and prefix to namespace mappings as the given registry.
	 * @param registry The registry containing the registrations to register.
	 * @return This registrar.
	 * @throws NullPointerException if the given registry is <code>null</code>.
	 * @throws IllegalArgumentException if one of the given prefixes in the given registry is not valid.
	 * @see #getVocabularySpecification()
	 */
	public VocabularyRegistrar registerAll(@Nonnull final VocabularyRegistry registry);

	/**
	 * Generates a new vocabulary prefix unique to this manager. The generated prefix will be valid as per {@link VocabularySpecification#isValidPrefix(String)}
	 * and will not be registered.
	 * @return A new prefix unique to this manager.
	 * @see #getVocabularySpecification()
	 * @see #isPrefixRegistered(String)
	 */
	public String generatePrefix();

	/**
	 * Retrieves the prefix to use for the indicated vocabulary. If a vocabulary has no prefix registered, a new prefix will be determined and registered with the
	 * vocabulary. Thus when this method returns, the vocabulary is guaranteed to have been registered.
	 * @apiNote If a default vocabulary is supported, a caller may first want to check if the given namespace indicates default vocabulary using
	 *          {@link #getDefaultVocabulary()}.
	 * @apiNote This method is similar to {@link #findPrefixForVocabulary(URI)} except that this method will always return a prefix, generating a new one if
	 *          needed.
	 * @implSpec The default implementation attempts to use the last URI segment as a prefix, if it is valid and has not yet been registered with another
	 *           namespace. Otherwise it delegates to {@link #generatePrefix()}.
	 * @param namespace A namespace URI identifying a vocabulary.
	 * @return A prefix for use with the identified vocabulary.
	 * @throws NullPointerException if the given namespace is <code>null</code>.
	 * @see #findPrefixForVocabulary(URI)
	 * @see #generatePrefix()
	 * @see VocabularySpecification#isValidPrefix(String)
	 * @see #registerVocabulary(URI, String)
	 */
	public default String determinePrefixForVocabulary(@Nonnull final URI namespace) {
		final Optional<String> foundRegisteredPrefix = findPrefixForVocabulary(namespace);
		final String prefix = foundRegisteredPrefix.orElseGet(() -> getKnownVocabularies().findPrefixForVocabulary(namespace)
				.orElseGet(() -> findName(namespace).filter(name -> !name.equals(ROOT_PATH))
						//make sure the name is a valid prefix that we haven't yet used
						.filter(name -> getVocabularySpecification().isValidPrefix(name) && !isPrefixRegistered(name))
						//if we didn't find a label from the URI name, generate a unique vocabulary prefix
						.orElseGet(this::generatePrefix)));
		if(!foundRegisteredPrefix.isPresent()) { //if the prefix wasn't already registered
			registerVocabulary(namespace, prefix); //associate the prefix and namespace
		}
		return prefix; //return the prefix we found or created
	}

	/**
	 * Retrieves the prefix to use for the vocabulary term. If the term vocabulary has no prefix registered, a new prefix will be determined and registered with
	 * the vocabulary. Thus when this method returns, the vocabulary is guaranteed to have been registered.
	 * @apiNote This method is similar to {@link #findPrefixForTerm(URI)} except that this method will always return a prefix, generating a new one if needed.
	 * @implSpec The default implementation delegates to {@link #determinePrefixForVocabulary(URI)}.
	 * @param term The absolute URI identifying a term in a vocabulary.
	 * @return A prefix for use with the given term, if the given URI can be converted to a term and if a prefix is registered.
	 * @throws NullPointerException if the given term is <code>null</code>.
	 * @throws IllegalArgumentException if the given URI is not absolute.
	 * @see #asVocabularyTerm(URI)
	 * @see #determinePrefixForVocabulary(URI)
	 * @see #findPrefixForTerm(URI)
	 * @see #generatePrefix()
	 * @see VocabularySpecification#isValidPrefix(String)
	 * @see #registerVocabulary(URI, String)
	 */
	public default Optional<Map.Entry<VocabularyTerm, String>> determinePrefixForTerm(@Nonnull final URI term) {
		return asVocabularyTerm(term).map(vocabularyTerm -> Map.entry(vocabularyTerm, determinePrefixForVocabulary(vocabularyTerm.getNamespace())));
	}

	/**
	 * Determines the CURIE that would represent the given vocabulary term using the registered vocabularies, registering a new prefix if needed. If the term
	 * namespace is set as the default namespace, a CURIE with no prefix is returned. When this method returns, the vocabulary is guaranteed to have been
	 * registered.
	 * @implSpec The default implementation delegates to {@link #findCurie(VocabularyTerm)} and, if a CURIE could not be determined, determines a prefix by
	 *           calling {@link #determinePrefixForVocabulary(URI)}.
	 * @param vocabularyTerm The vocabulary term for which a CURIE should be determined.
	 * @return The CURIE to represent the given vocabulary term.
	 * @see #determinePrefixForVocabulary(URI)
	 * @see #findCurie(VocabularyTerm)
	 */
	public default Curie determineCurie(@Nonnull final VocabularyTerm vocabularyTerm) {
		return findCurie(vocabularyTerm).orElseGet(() -> Curie.of(determinePrefixForVocabulary(vocabularyTerm.getNamespace()), vocabularyTerm.getName()));
	}

	/**
	 * Determines the CURIE that would represent the given term using the registered vocabularies, registering a new prefix if needed. If the term namespace is
	 * set as the default namespace, a CURIE with no prefix is returned. When this method returns, the vocabulary is guaranteed to have been registered.
	 * @implSpec The default implementation calls {@link #asVocabularyTerm(URI)} and then delegates to {@link #determineCurie(VocabularyTerm)}.
	 * @param term The absolute URI identifying a term in a vocabulary.
	 * @return The CURIE to represent the given term, which may be empty if the namespace could not be determined or the given string itself is a regular
	 *         namespace.
	 * @throws NullPointerException if the given term is <code>null</code>.
	 * @throws IllegalArgumentException if the given URI is not absolute.
	 * @see #determinePrefixForVocabulary(URI)
	 */
	public default Optional<Curie> determineCurieForTerm(@Nonnull final URI term) {
		return asVocabularyTerm(term).map(this::determineCurie);
	}

}
