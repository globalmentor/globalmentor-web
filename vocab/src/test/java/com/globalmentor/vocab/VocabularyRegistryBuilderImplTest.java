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
import static org.hamcrest.MatcherAssert.*;

import java.net.URI;

import org.junit.jupiter.api.*;

/**
 * Tests of {@link VocabularyRegistryBuilderImpl}.
 * @author Garret Wilson
 */
public class VocabularyRegistryBuilderImplTest {

	private static final URI EG_NAMESPACE = URI.create("https://example.com/ns/");

	/**
	 * @see VocabularyRegistryBuilderImpl#re
	 * 
	 */
	@Test
	public void testRegisterAllMaintainsMultiplePrefixRegistrationsWithSameVocabulary() {
		final VocabularyManager manager = new VocabularyManager();
		manager.registerPrefix("foo", EG_NAMESPACE);
		manager.registerPrefix("bar", EG_NAMESPACE);
		final VocabularyRegistry registry = new VocabularyRegistryBuilderImpl(VocabularySpecification.DEFAULT).registerAll(manager).build();
		assertThat(registry.findVocabularyByPrefix("foo"), isPresentAndIs(EG_NAMESPACE));
		assertThat(registry.findVocabularyByPrefix("bar"), isPresentAndIs(EG_NAMESPACE));
		assertThat(registry.findPrefixForVocabulary(EG_NAMESPACE), isPresentAndIs("foo"));
	}

}
