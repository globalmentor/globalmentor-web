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
import static com.globalmentor.net.URIs.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import com.globalmentor.java.Characters;
import com.globalmentor.net.URIs;

/**
 * A base vocabulary specification with useful functionality.
 * @apiNote This class can only be instantiated within its own package, as a static shared constant is available at {@link VocabularySpecification#DEFAULT}, but
 *          may be subclassed.
 * @author Garret Wilson
 */
public class DefaultVocabularySpecification extends BaseVocabularySpecification {

	/**
	 * The delimiter characters for determining a regular namespace from a term.
	 */
	public static final Characters REGULAR_NAMESPACE_DELIMITER_CHARACTERS = Characters.of(PATH_SEPARATOR, FRAGMENT_SEPARATOR);

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

	/**
	 * {@inheritDoc}
	 * @implSpec This implementation considers a namespace "regular" if the string form of the URI ends with a forward slash {@value URIs#PATH_SEPARATOR}
	 *           character or a number sign {@value URIs#FRAGMENT_SEPARATOR} character.
	 * @see #REGULAR_NAMESPACE_DELIMITER_CHARACTERS
	 */
	@Override
	public boolean isNamespaceRegular(final URI namespace) {
		return endsWith(checkAbsolute(namespace).toString(), REGULAR_NAMESPACE_DELIMITER_CHARACTERS);
	}

	/**
	 * {@inheritDoc}
	 * @implSpec This implementation finds the longest string ending with a forward slash {@value URIs#PATH_SEPARATOR} character or a number sign
	 *           {@value URIs#FRAGMENT_SEPARATOR} character, that is a valid URI and is not the given term itself.
	 * @see #REGULAR_NAMESPACE_DELIMITER_CHARACTERS
	 */
	@Override
	public Optional<URI> findTermNamespace(final URI term) {
		final String termString = checkAbsolute(term).toString();
		final int delimiterIndex = lastIndexOf(termString, REGULAR_NAMESPACE_DELIMITER_CHARACTERS);
		if(delimiterIndex > 0 && delimiterIndex < termString.length() - 1) { //make sure the term itself (i.e. the entire string) is not a namespace
			final URI namespace;
			try {
				namespace = new URI(termString.substring(0, delimiterIndex + 1)); //include the delimiter
			} catch(final URISyntaxException uriSyntaxException) { //the thing that looked like a namespace (e.g. `http://`) wasn't really a URI
				return Optional.empty();
			}
			return Optional.of(namespace);
		} else {
			return Optional.empty();
		}
	}
}
