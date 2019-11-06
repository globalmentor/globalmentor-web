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

import static com.globalmentor.java.Conditions.*;

/**
 * A base vocabulary prefix specification with useful functionality.
 * @author Garret Wilson
 */
public abstract class BaseVocabularyPrefixSpecification implements VocabularyPrefixSpecification {

	private static final String PREFIX_PREFIX = "ns";

	/**
	 * {@inheritDoc}
	 * @implSpec This implementation returns {@value #PREFIX_PREFIX} with the uniqueness guarantee appended.
	 */
	@Override
	public String generatePrefix(final long uniquenessGuarantee) {
		checkArgumentNotNegative(uniquenessGuarantee);
		return PREFIX_PREFIX + uniquenessGuarantee;
	}

}
