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

import java.net.URI;
import java.util.Optional;

import javax.annotation.*;

/**
 * A specification for vocabulary namespaces and prefixes.
 * @author Garret Wilson
 */
public interface VocabularySpecification {

	/** The default specification for prefixes. */
	public static final DefaultVocabularySpecification DEFAULT = new DefaultVocabularySpecification();

	/**
	 * Determines whether the given string is a valid prefix.
	 * @param prefix The string to check for being a valid prefix.
	 * @return <code>true</code> if the given string represents a valid label.
	 * @throws NullPointerException if the given string is <code>null</code>.
	 */
	public boolean isValidPrefix(@Nonnull final String prefix);

	/**
	 * Checks to ensure the given prefix is valid.
	 * @implSpec The default implementation delegates to {@link #isValidPrefix(String)}.
	 * @param prefix The string to check for being a valid prefix.
	 * @return The given prefix.
	 * @throws NullPointerException if the given string is <code>null</code>.
	 * @throws IllegalArgumentException if the given prefix is not valid.
	 */
	public default String checkArgumentValidPrefix(@Nonnull final String prefix) {
		checkArgument(isValidPrefix(prefix), "The string `%s` is not a valid vocabulary prefix.", prefix);
		return prefix;
	}

	/**
	 * Generates a new vocabulary prefix. The returned prefix will be at least as unique as the uniqueness guarantee in its context.
	 * @apiNote Normally a consumer would never call this method more than once with the same uniqueness guarantee.
	 * @param uniquenessGuarantee An integer value to guarantee uniqueness.
	 * @return A new vocabulary prefix, valid as per {@link #isValidPrefix(String)}.
	 */
	public String generatePrefix(@Nonnegative final long uniquenessGuarantee);

	/**
	 * Determines if the given namespace is <dfn>regular</dfn>; that is, whether it is one that can be determined automatically from a typical term in the
	 * vocabulary.
	 * @param namespace The vocabulary namespace URI.
	 * @return <code>true</code> if the namespace is a regular namespace according to this vocabulary specification.
	 * @throws NullPointerException if the given namespace is <code>null</code>.
	 * @throws IllegalArgumentException if the given URI is not absolute.
	 * @see #findTermNamespace(URI)
	 */
	public boolean isNamespaceRegular(@Nonnull final URI namespace);

	/**
	 * Retrieves the namespace of the given term based upon the vocabulary specification rules.
	 * @apiNote This method can only determine namespaces that are <dfn>regular</dfn> as per {@link #isNamespaceRegular(URI)}.
	 * @param term The absolute URI identifying a term in the vocabulary.
	 * @return The namespace of the term, which may be empty if the namespace could not be determined or the given string itself is a regular namespace.
	 * @throws NullPointerException if the given term is <code>null</code>.
	 * @throws IllegalArgumentException if the given URI is not absolute.
	 * @see #isNamespaceRegular(URI)
	 */
	public Optional<URI> findTermNamespace(@Nonnull final URI term);

}
