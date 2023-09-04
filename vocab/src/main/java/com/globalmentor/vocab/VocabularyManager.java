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
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.*;

import static java.util.Objects.*;

/**
 * Map managing namespace URIs and prefixes for serialization. As a manager is also a {@link VocabularyRegistry}, <code>null</code> namespaces and
 * <code>null</code> prefixes are not allowed, but a default vocabulary may be specified using {@link #setDefaultVocabulary(URI)}.
 * @implSpec This implementation supports namespaces that are recognized but not registered; that is, default namespace/prefix associations.
 * @implSpec This class is not thread safe.
 * @author Garret Wilson
 */
public class VocabularyManager extends AbstractVocabularyRegistry implements VocabularyRegistrar {

	private final boolean autoRegister;

	@Override
	public boolean isAutoRegister() {
		return autoRegister;
	}

	private final VocabularyRegistry knownVocabularies;

	@Override
	public VocabularyRegistry getKnownVocabularies() {
		return knownVocabularies;
	}

	/**
	 * Default vocabulary manager constructor.
	 * @implSpec The {@link VocabularySpecification#DEFAULT} vocabulary specification is used.
	 * @implSpec Auto-register will be disabled.
	 */
	public VocabularyManager() {
		this(VocabularySpecification.DEFAULT);
	}

	/**
	 * Vocabulary specification constructor.
	 * @param vocabularySpecification The specification governing vocabularies in this registry.
	 * @implSpec Auto-register will be disabled.
	 */
	public VocabularyManager(@Nonnull VocabularySpecification vocabularySpecification) {
		this(vocabularySpecification, VocabularyRegistry.EMPTY);
	}

	/**
	 * Known vocabularies constructor.
	 * @implSpec The {@link VocabularySpecification#DEFAULT} vocabulary specification is used.
	 * @implSpec Auto-register will be disabled.
	 * @param knownVocabularies The vocabularies that are already recognized outside of any registrations.
	 */
	public VocabularyManager(@Nonnull final VocabularyRegistry knownVocabularies) {
		this(knownVocabularies, false);
	}

	/**
	 * Known vocabularies and auto-register constructor.
	 * @implSpec The {@link VocabularySpecification#DEFAULT} vocabulary specification is used.
	 * @param knownVocabularies The vocabularies that are already recognized outside of any registrations.
	 * @param autoRegister <code>true</code> if known vocabularies should be registered automatically upon lookup.
	 */
	public VocabularyManager(@Nonnull final VocabularyRegistry knownVocabularies, final boolean autoRegister) {
		this(VocabularySpecification.DEFAULT, knownVocabularies, autoRegister);
	}

	/**
	 * Vocabulary specification and known vocabularies constructor.
	 * @implSpec Auto-register will be disabled.
	 * @param vocabularySpecification The specification governing vocabularies in this registry.
	 * @param knownVocabularies The vocabularies that are already recognized outside of any registrations.
	 */
	public VocabularyManager(@Nonnull VocabularySpecification vocabularySpecification, @Nonnull final VocabularyRegistry knownVocabularies) {
		this(vocabularySpecification, knownVocabularies, false);
	}

	/**
	 * Full constructor.
	 * @param vocabularySpecification The specification governing vocabularies in this registry.
	 * @param knownVocabularies The vocabularies that are already recognized outside of any registrations.
	 * @param autoRegister <code>true</code> if known vocabularies should be registered automatically upon lookup.
	 */
	public VocabularyManager(@Nonnull VocabularySpecification vocabularySpecification, @Nonnull final VocabularyRegistry knownVocabularies,
			final boolean autoRegister) {
		super(vocabularySpecification);
		this.knownVocabularies = requireNonNull(knownVocabularies);
		this.autoRegister = autoRegister;
	}

	@Override
	public Optional<URI> setDefaultVocabulary(URI namespace) {
		return super.setDefaultVocabulary(namespace);
	}

	@Override
	public Optional<URI> findVocabularyByPrefix(final String prefix) {
		Optional<URI> optionalNamespace = super.findVocabularyByPrefix(prefix);
		if(!optionalNamespace.isPresent() && isAutoRegister()) {
			optionalNamespace = getKnownVocabularies().findVocabularyByPrefix(prefix);
			optionalNamespace.ifPresent(namespace -> registerPrefix(prefix, namespace));
		}
		return optionalNamespace;
	}

	@Override
	public Optional<String> findPrefixForVocabulary(final URI namespace) {
		Optional<String> optionalPrefix = super.findPrefixForVocabulary(namespace);
		if(!optionalPrefix.isPresent() && isAutoRegister()) {
			optionalPrefix = getKnownVocabularies().findPrefixForVocabulary(namespace);
			optionalPrefix.ifPresent(prefix -> registerVocabulary(namespace, prefix));
		}
		return optionalPrefix;
	}

	/** The atomic variable used to generate prefixes. */
	private final AtomicLong generatedPrefixCount = new AtomicLong(0);

	@Override
	public String generatePrefix() {
		String prefix;
		do {
			prefix = getVocabularySpecification().generatePrefix(generatedPrefixCount.incrementAndGet());
		} while(isPrefixRegistered(prefix)); //keep generating prefixes until one isn't already registered
		assert !isPrefixRegistered(prefix);
		return prefix;
	}

	@Override
	public Optional<Map.Entry<URI, String>> registerVocabulary(final URI namespace, final String prefix) {
		requireNonNull(namespace);
		getVocabularySpecification().checkArgumentValidPrefix(prefix);
		final Map<URI, String> prefixesByNamespace = getPrefixesByNamespace();
		final boolean hadPrefixRegistration = prefixesByNamespace.containsKey(namespace); //check first to distinguish between no mapping and null value
		final String previousPrefix = getPrefixesByNamespace().put(namespace, prefix);
		getNamespacesByPrefix().put(prefix, namespace); //update the prefix-namespace mapping as well 
		return hadPrefixRegistration ? Optional.of(Map.entry(namespace, previousPrefix)) : Optional.empty();
	}

	@Override
	public Optional<URI> registerPrefix(final String prefix, final URI namespace) {
		getVocabularySpecification().checkArgumentValidPrefix(prefix);
		requireNonNull(namespace);
		final Optional<URI> previousNamespace = Optional.ofNullable(getNamespacesByPrefix().put(prefix, namespace));
		getPrefixesByNamespace().putIfAbsent(namespace, prefix); //don't override a prefix already associated with the namespace 
		return previousNamespace;
	}

	@Override
	public VocabularyRegistrar registerAll(final VocabularyRegistry registry) {
		final Map<URI, String> prefixesByNamespace = getPrefixesByNamespace();
		registry.getRegisteredPrefixesByVocabulary().forEach(entry -> prefixesByNamespace.put(entry.getKey(), entry.getValue()));
		final Map<String, URI> namespacesByPrefix = getNamespacesByPrefix();
		registry.getRegisteredVocabulariesByPrefix().forEach(entry -> namespacesByPrefix.put(entry.getKey(), entry.getValue()));
		return this;
	}
}
