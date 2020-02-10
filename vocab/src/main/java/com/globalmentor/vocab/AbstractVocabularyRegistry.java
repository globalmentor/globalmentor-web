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

import java.net.URI;
import java.util.*;

import javax.annotation.Nonnull;

import static com.globalmentor.util.Optionals.*;
import static java.util.Collections.*;
import static java.util.Objects.*;

/**
 * Abstract base class for managing vocabulary prefix registrations.
 * @implSpec This implementation allows a <code>null</code> prefix, but <code>null</code> namespace prefixes are not allowed.
 * @implSpec This class is not thread safe.
 * @author Garret Wilson
 */
public abstract class AbstractVocabularyRegistry implements VocabularyRegistry {

	private final VocabularySpecification vocabularySpecification;

	@Override
	public VocabularySpecification getVocabularySpecification() {
		return vocabularySpecification;
	}

	private URI defaultNamespace = null;

	/** @implSpec <code>null</code> is not allowed as a key or as a value. */
	private final Map<String, URI> namespacesByPrefix = new HashMap<>();

	/** @return The internal map of registered prefixes and their associated namespaces. */
	protected Map<String, URI> getNamespacesByPrefix() {
		return namespacesByPrefix;
	}

	@Override
	public int getRegisteredPrefixCount() {
		return getNamespacesByPrefix().size();
	}

	/** @implSpec <code>null</code> is not allowed as a key or as a value. */
	private final Map<URI, String> prefixesByNamespace = new HashMap<>();

	/** @return The internal map of registered namespaces and their associated prefixes. */
	protected Map<URI, String> getPrefixesByNamespace() {
		return prefixesByNamespace;
	}

	@Override
	public int getRegisteredVocabularyCount() {
		return getPrefixesByNamespace().size();
	}

	/**
	 * Vocabulary specification constructor.
	 * @param vocabularySpecification The specification governing vocabularies in this registry.
	 */
	public AbstractVocabularyRegistry(@Nonnull VocabularySpecification vocabularySpecification) {
		this.vocabularySpecification = requireNonNull(vocabularySpecification);
	}

	/**
	 * Sets the default vocabulary.
	 * @param namespace The namespace URI of the new default vocabulary.
	 * @return The old default vocabulary, if any.
	 * @throws NullPointerException if the given namespace is <code>null</code>.
	 */
	protected Optional<URI> setDefaultVocabulary(@Nonnull final URI namespace) {
		final URI oldDefaultNamespace = defaultNamespace;
		defaultNamespace = requireNonNull(namespace);
		return Optional.ofNullable(oldDefaultNamespace);
	}

	@Override
	public Optional<URI> getDefaultVocabulary() {
		return Optional.ofNullable(defaultNamespace);
	}

	@Override
	public boolean isPrefixRegistered(final String prefix) {
		return getNamespacesByPrefix().containsKey(prefix);
	}

	@Override
	public boolean isVocabularyRegistered(final URI namespace) {
		return getPrefixesByNamespace().containsKey(requireNonNull(namespace)) || isPresentAndEquals(getDefaultVocabulary(), namespace);
	}

	@Override
	public Optional<URI> findVocabularyByPrefix(final String prefix) {
		return Optional.ofNullable(getNamespacesByPrefix().get(requireNonNull(prefix)));
	}

	@Override
	public Optional<String> findPrefixForVocabulary(final URI namespace) {
		return Optional.ofNullable(getPrefixesByNamespace().get(requireNonNull(namespace)));
	}

	@Override
	public Set<Map.Entry<URI, String>> getRegisteredPrefixesByVocabulary() {
		return unmodifiableSet(getPrefixesByNamespace().entrySet());
	}

	@Override
	public Set<Map.Entry<String, URI>> getRegisteredVocabulariesByPrefix() {
		return unmodifiableSet(getNamespacesByPrefix().entrySet());
	}

	@Override
	public boolean isEmpty() {
		assert getNamespacesByPrefix().isEmpty() == getPrefixesByNamespace().isEmpty();
		return getNamespacesByPrefix().isEmpty();
	}

}
