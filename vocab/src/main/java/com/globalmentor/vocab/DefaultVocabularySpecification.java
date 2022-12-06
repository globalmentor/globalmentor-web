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

import static com.globalmentor.java.CharSequences.*;
import static com.globalmentor.java.Characters.*;

import com.globalmentor.java.Characters;

/**
 * A base vocabulary specification with useful functionality.
 * @apiNote This class can only be instantiated within its own package, as a static shared constant is available at {@link VocabularySpecification#DEFAULT}, but
 *          may be subclassed.
 * @author Garret Wilson
 */
public class DefaultVocabularySpecification extends BaseVocabularySpecification {

	/** Constructor. */
	protected DefaultVocabularySpecification() {
	}

	/**
	 * {@inheritDoc}
	 * @implSpec This implementation allows any non-empty string that does not contain Unicode whitespace.
	 * @see Characters#WHITESPACE_CHARACTERS
	 */
	@Override
	public boolean isValidPrefix(final String prefix) {
		return !prefix.isEmpty() && !contains(prefix, WHITESPACE_CHARACTERS);
	}

}
