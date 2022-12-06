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

import java.net.URI;

import javax.annotation.*;

import com.globalmentor.vocab.VocabularyRegistry.Builder;

/**
 * Vocabulary registry builder implementation.
 * @author Garret Wilson
 */
class VocabularyRegistryBuilderImpl implements VocabularyRegistry.Builder {

	private final VocabularyManager manager;

	/**
	 * Vocabulary specification constructor.
	 * @param vocabularySpecification The specification governing vocabularies in the registry.
	 */
	public VocabularyRegistryBuilderImpl(@Nonnull VocabularySpecification vocabularySpecification) {
		manager = new VocabularyManager(vocabularySpecification);
	}

	/**
	 * Copy constructor to create a builder with the same vocabulary specification, default vocabulary, and mappings as the given registry.
	 * @param registry The vocabulary registry with the values to copy.
	 * @throws NullPointerException if the given vocabulary registry is <code>null</code>.
	 */
	public VocabularyRegistryBuilderImpl(@Nonnull VocabularyRegistry registry) {
		manager = new VocabularyManager(registry.getVocabularySpecification());
		manager.registerAll(registry);
	}

	@Override
	public Builder setDefaultVocabulary(final URI namespace) {
		manager.setDefaultVocabulary(namespace);
		return this;
	}

	@Override
	public Builder registerVocabulary(final URI namespace, final String prefix) {
		manager.registerVocabulary(namespace, prefix);
		return this;
	}

	@Override
	public Builder registerPrefix(final String prefix, final URI namespace) {
		manager.registerPrefix(prefix, namespace);
		return this;
	}

	@Override
	public Builder registerAll(final VocabularyRegistry registry) {
		manager.registerAll(registry);
		return this;
	}

	@Override
	public VocabularyRegistry build() {
		return new DefaultVocabularyRegistry(manager);
	}

}
