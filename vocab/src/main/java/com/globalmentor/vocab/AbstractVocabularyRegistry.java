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
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Collections.*;
import static java.util.Objects.*;

/**
 * Abstract base class for managing vocabulary prefix registrations.
 * @implSpec This implementation allows a <code>null</code> prefix, but <code>null</code> namespace prefixes are not allowed.
 * @implSpec This class is not thread safe.
 * @author Garret Wilson
 */
public abstract class AbstractVocabularyRegistry implements VocabularyRegistry {

	/** @implSpec Use a concurrent hash map to allow updating registry during iteration without {@link ConcurrentModificationException}. */
	private final Map<String, URI> namespacesByPrefix = new ConcurrentHashMap<>();

	/** @return The internal map of registered prefixes and their associated namespaces. */
	protected Map<String, URI> getNamespacesByPrefix() {
		return namespacesByPrefix;
	}

	/** @implSpec Use a concurrent hash map to allow updating registry during iteration without {@link ConcurrentModificationException}. */
	private final Map<URI, String> prefixesByNamespace = new ConcurrentHashMap<>();

	/** @return The internal map of registered namespaces and their associated prefixes. */
	protected Map<URI, String> getPrefixesByNamespace() {
		return prefixesByNamespace;
	}

	@Override
	public boolean isPrefixRegistered(final String prefix) {
		return getNamespacesByPrefix().containsKey(prefix);
	}

	@Override
	public boolean isVocabularyRegistered(final URI namespace) {
		return getPrefixesByNamespace().containsKey(requireNonNull(namespace));
	}

	@Override
	public Optional<URI> findVocabularyByPrefix(final String prefix) {
		return Optional.ofNullable(getNamespacesByPrefix().get(prefix));
	}

	@Override
	public Optional<String> findPrefixForVocabulary(final URI namespace) {
		final String prefix = getPrefixesByNamespace().get(requireNonNull(namespace));
		if(prefix == null && !getPrefixesByNamespace().containsKey(namespace)) { //see if null means null or missing
			return Optional.empty();
		}
		return Optional.ofNullable(prefix); //TODO decide how to handle the "`null`" or "default" prefix; this doesn't work
	}

	@Override
	public Set<Map.Entry<URI, String>> getRegisteredPrefixesByVocabulary() {
		return unmodifiableSet(getPrefixesByNamespace().entrySet());
	}

	@Override
	public Set<Map.Entry<String, URI>> getRegisteredVocabulariesByPrefix() {
		return unmodifiableSet(getNamespacesByPrefix().entrySet());
	}

}
