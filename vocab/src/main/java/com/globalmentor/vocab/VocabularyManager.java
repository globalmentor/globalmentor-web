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
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.*;

import static java.util.Objects.*;

/**
 * Map managing namespace URIs and labels for serialization. Mapping labels to the <code>null</code> namespace or to the <code>null</code> label is allowed.
 * @implSpec This implementation allows a <code>null</code> prefix, but <code>null</code> namespace prefixes are not allowed.
 * @implSpec This implementation supports namespaces that are recognized but not registered; that is, default namespace/prefix associations.
 * @implSpec This class is not thread safe.
 * @author Garret Wilson
 */
public class VocabularyManager extends AbstractVocabularyRegistry implements VocabularyRegistrar {

	private final VocabularyPrefixSpecification prefixSpecification;

	@Override
	public VocabularyPrefixSpecification getPrefixSpecification() {
		return prefixSpecification;
	}

	private final VocabularyRegistry knownVocabularies;

	private final boolean autoRegister;

	@Override
	public boolean isAutoRegister() {
		return autoRegister;
	}

	/**
	 * Default vocabulary manager constructor.
	 * @implSpec The {@link VocabularyPrefixSpecification#DEFAULT} prefix specification is used.
	 * @implSpec Auto-register will be disabled.
	 */
	public VocabularyManager() {
		this(VocabularyPrefixSpecification.DEFAULT);
	}

	/**
	 * Prefix specification constructor.
	 * @param prefixSpecification The specification governing allowed prefix for this registry.
	 * @implSpec Auto-register will be disabled.
	 */
	public VocabularyManager(@Nonnull VocabularyPrefixSpecification prefixSpecification) {
		this(prefixSpecification, VocabularyRegistry.EMPTY);
	}

	/**
	 * Known vocabularies constructor.
	 * @implSpec The {@link VocabularyPrefixSpecification#DEFAULT} prefix specification is used.
	 * @implSpec Auto-register will be disabled.
	 * @param knownVocabularies The vocabularies that are already recognized outside of any registrations.
	 */
	public VocabularyManager(@Nonnull final VocabularyRegistry knownVocabularies) {
		this(knownVocabularies, false);
	}

	/**
	 * Known vocabularies and auto-register constructor.
	 * @implSpec The {@link VocabularyPrefixSpecification#DEFAULT} prefix specification is used.
	 * @param knownVocabularies The vocabularies that are already recognized outside of any registrations.
	 * @param autoRegister <code>true</code> if known vocabularies should be registered automatically upon lookup.
	 */
	public VocabularyManager(@Nonnull final VocabularyRegistry knownVocabularies, final boolean autoRegister) {
		this(VocabularyPrefixSpecification.DEFAULT, knownVocabularies, autoRegister);
	}

	/**
	 * Prefix specification and known vocabularies constructor.
	 * @implSpec Auto-register will be disabled.
	 * @param prefixSpecification The specification governing allowed prefix for this registry.
	 * @param knownVocabularies The vocabularies that are already recognized outside of any registrations.
	 */
	public VocabularyManager(@Nonnull VocabularyPrefixSpecification prefixSpecification, @Nonnull final VocabularyRegistry knownVocabularies) {
		this(prefixSpecification, knownVocabularies, false);
	}

	/**
	 * Full constructor.
	 * @param prefixSpecification The specification governing allowed prefix for this registry.
	 * @param knownVocabularies The vocabularies that are already recognized outside of any registrations.
	 * @param autoRegister <code>true</code> if known vocabularies should be registered automatically upon lookup.
	 */
	public VocabularyManager(@Nonnull VocabularyPrefixSpecification prefixSpecification, @Nonnull final VocabularyRegistry knownVocabularies,
			final boolean autoRegister) {
		this.prefixSpecification = requireNonNull(prefixSpecification);
		this.knownVocabularies = requireNonNull(knownVocabularies);
		this.autoRegister = autoRegister;
	}

	/**
	 * {@inheritDoc}
	 * @implSpec This implementation returns <code>true</code> if the vocabulary has been registered or if it is one of the known namespaces for this manager.
	 */
	@Override
	public boolean isVocabularyRecognized(final URI namespace) {
		return isVocabularyRegistered(namespace) || knownVocabularies.isVocabularyRegistered(namespace);
	}

	@Override
	public Optional<URI> findVocabularyByPrefix(final String prefix) {
		Optional<URI> optionalNamespace = super.findVocabularyByPrefix(prefix);
		if(optionalNamespace.isEmpty() && isAutoRegister()) {
			optionalNamespace = knownVocabularies.findVocabularyByPrefix(prefix);
			optionalNamespace.ifPresent(namespace -> registerPrefix(prefix, namespace));
		}
		return optionalNamespace;
	}

	@Override
	public Optional<String> findPrefixForVocabulary(final URI namespace) {
		Optional<String> optionalPrefix = super.findPrefixForVocabulary(namespace);
		if(optionalPrefix.isEmpty() && isAutoRegister()) {
			optionalPrefix = knownVocabularies.findPrefixForVocabulary(namespace);
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
			prefix = getPrefixSpecification().generatePrefix(generatedPrefixCount.incrementAndGet());
		} while(isPrefixRegistered(prefix)); //keep generating prefixes until one isn't already registered
		assert !isPrefixRegistered(prefix);
		return prefix;
	}

	@Override
	public Optional<String> registerVocabulary(final URI namespace, final String prefix) {
		requireNonNull(namespace);
		getPrefixSpecification().checkArgumentValidPrefix(prefix);
		//TODO decide how to handle the "`null`" or "default" prefix
		final Optional<String> previousPrefix = Optional.ofNullable(getPrefixesByNamespace().put(namespace, prefix));
		getNamespacesByPrefix().put(prefix, namespace); //update the prefix-namespace mapping as well 
		return previousPrefix;
	}

	@Override
	public Optional<URI> registerPrefix(final String prefix, final URI namespace) {
		getPrefixSpecification().checkArgumentValidPrefix(prefix);
		requireNonNull(namespace);
		final Optional<URI> previousNamespace = Optional.ofNullable(getNamespacesByPrefix().put(prefix, namespace));
		getPrefixesByNamespace().putIfAbsent(namespace, prefix); //don't override a prefix already associated with the namespace 
		return previousNamespace;
	}

}
