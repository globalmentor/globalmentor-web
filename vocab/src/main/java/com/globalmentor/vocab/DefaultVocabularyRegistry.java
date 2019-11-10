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

import static java.util.Objects.*;

import java.net.URI;
import java.util.Map;

import javax.annotation.*;

/**
 * Default read-only implementation of vocabulary registry.
 * @apiNote Unless a custom vocabulary specification is to be used, normally consumers will not create this class directly, but instead call one of the static
 *          factory methods in {@link VocabularyRegistry}.
 * @implSpec This implementation allows a <code>null</code> prefix, but <code>null</code> namespace prefixes are not allowed.
 * @implSpec This class is thread safe.
 * @author Garret Wilson
 */
public final class DefaultVocabularyRegistry extends AbstractVocabularyRegistry {

	/**
	 * Existing prefix-vocabulary registrations constructor.
	 * @implSpec The {@link VocabularySpecification#DEFAULT} vocabulary specification is used.
	 * @param defaultVocabulary The namespace URI of the default vocabulary, or <code>null</code> if there is no default.
	 * @param vocabulariesByPrefix Vocabulary prefixes and vocabulary namespace URIs associated with them. If a prefix key appears more than once, the last one
	 *          one will take affect. If a prefix is mapped to more than one namespace, only the first prefix will be reverse-mapped back to the namespace.
	 * @throws NullPointerException if a namespace is <code>null</code>.
	 * @throws IllegalArgumentException if one of the given prefixes is not valid as per the vocabulary specification.
	 * @see VocabularySpecification#isValidPrefix(String)
	 */
	public DefaultVocabularyRegistry(@Nullable URI defaultVocabulary, @Nonnull final Iterable<Map.Entry<String, URI>> vocabulariesByPrefix) {
		this(VocabularySpecification.DEFAULT, defaultVocabulary, vocabulariesByPrefix);
	}

	/**
	 * Vocabulary specification and existing prefix-vocabulary registrations constructor.
	 * @param vocabularySpecification The specification governing vocabularies in this registry.
	 * @param defaultVocabulary The namespace URI of the default vocabulary, or <code>null</code> if there is no default.
	 * @param vocabulariesByPrefix Vocabulary prefixes and vocabulary namespace URIs associated with them. If a prefix key appears more than once, the last one
	 *          one will take affect. If a prefix is mapped to more than one namespace, only the first prefix will be reverse-mapped back to the namespace.
	 * @throws NullPointerException if a namespace is <code>null</code>.
	 * @throws IllegalArgumentException if one of the given prefixes is not valid as per the vocabulary specification.
	 * @see VocabularySpecification#isValidPrefix(String)
	 */
	public DefaultVocabularyRegistry(@Nonnull VocabularySpecification vocabularySpecification, @Nullable URI defaultVocabulary,
			@Nonnull final Iterable<Map.Entry<String, URI>> vocabulariesByPrefix) {
		super(vocabularySpecification);
		if(defaultVocabulary != null) {
			setDefaultVocabulary(defaultVocabulary);
		}
		for(final Map.Entry<String, URI> vocabularyByPrefix : vocabulariesByPrefix) {
			final String prefix = vocabularySpecification.checkArgumentValidPrefix(vocabularyByPrefix.getKey());
			final URI namespace = requireNonNull(vocabularyByPrefix.getValue());
			getNamespacesByPrefix().put(prefix, namespace);
			getPrefixesByNamespace().putIfAbsent(namespace, prefix); //don't override a prefix already associated with the namespace
		}
	}

	/**
	 * Copy constructor to create an instance with the same vocabulary specification, default vocabulary, and mappings as the given registry.
	 * @param registry The vocabulary registry with the values to copy.
	 * @throws NullPointerException if the given vocabulary registry is <code>null</code>.
	 */
	public DefaultVocabularyRegistry(@Nonnull VocabularyRegistry registry) {
		super(registry.getVocabularySpecification());
		registry.getDefaultVocabulary().ifPresent(this::setDefaultVocabulary);
		final Map<URI, String> prefixesByNamespace = getPrefixesByNamespace();
		registry.getRegisteredPrefixesByVocabulary().forEach(entry -> prefixesByNamespace.put(entry.getKey(), entry.getValue()));
		final Map<String, URI> namespacesByPrefix = getNamespacesByPrefix();
		registry.getRegisteredVocabulariesByPrefix().forEach(entry -> namespacesByPrefix.put(entry.getKey(), entry.getValue()));
	}

}
