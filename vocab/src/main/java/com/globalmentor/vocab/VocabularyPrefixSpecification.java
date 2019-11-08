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

import static com.globalmentor.java.CharSequences.*;
import static com.globalmentor.java.Characters.*;
import static com.globalmentor.java.Conditions.*;

import javax.annotation.*;

import com.globalmentor.java.Characters;

/**
 * A specification for valid vocabulary prefixes.
 * @author Garret Wilson
 */
public interface VocabularyPrefixSpecification {

	/**
	 * The default specification for prefixes.
	 * @implSpec This implementation allows any string that does not contain Unicode whitespace.
	 * @see Characters#WHITESPACE_CHARACTERS
	 */
	public static final VocabularyPrefixSpecification DEFAULT = new BaseVocabularyPrefixSpecification() {
		@Override
		public boolean isValid(final String prefix) {
			return !contains(prefix, WHITESPACE_CHARACTERS);
		}
	};

	/**
	 * Determines whether the given string is a valid prefix.
	 * @param prefix The string to check for being a valid prefix.
	 * @return <code>true</code> if the given string represents a valid label.
	 * @throws NullPointerException if the given string is <code>null</code>.
	 */
	public boolean isValid(@Nonnull final String prefix);

	/**
	 * Checks to ensure the given prefix is valid.
	 * @implSpec The default implementation delegates to {@link #isValid(String)}.
	 * @param prefix The string to check for being a valid prefix.
	 * @return The given prefix.
	 * @throws NullPointerException if the given string is <code>null</code>.
	 * @throws IllegalArgumentException if the given prefix is not valid.
	 */
	public default String checkArgumentValidPrefix(@Nonnull final String prefix) {
		checkArgument(isValid(prefix), "The string `%s` is not a valid vocabulary prefix.", prefix);
		return prefix;
	}

	/**
	 * Generates a new vocabulary prefix. The returned prefix will be at least as unique as the uniqueness guarantee in its context.
	 * @apiNote Normally a consumer would never call this method more than once with the same uniqueness guarantee.
	 * @param uniquenessGuarantee An integer value to guarantee uniqueness.
	 * @return A new vocabulary prefix, valid as per {@link #isValid(String)}.
	 */
	public String generatePrefix(@Nonnegative final long uniquenessGuarantee);

}
