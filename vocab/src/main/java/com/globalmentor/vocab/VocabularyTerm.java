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
import static com.globalmentor.net.URIs.*;
import static java.util.Objects.*;

import java.net.URI;

import javax.annotation.*;

/**
 * Encapsulates the namespace and name of a web vocabulary term. This is a value object.
 * <p>
 * For example the term <code>http://purl.org/dc/terms/creator</code> is composed of the namespace <code>http://purl.org/dc/terms</code> and the name
 * <code>creator</code>.
 * </p>
 * @author Garret Wilson
 */
public final class VocabularyTerm {

	private final URI namespace;

	/** @return The term namespace, which is guaranteed to be absolute. */
	public @Nonnull URI getNamespace() {
		return namespace;
	}

	private final String name;

	/** @return The term name, which is guaranteed not to be empty. */
	public @Nonnull String getName() {
		return name;
	}

	/**
	 * Static factory method.
	 * @param namespace The term namespace, which must be absolute.
	 * @param name The term name, which must not be the empty string.
	 * @return A vocabulary term with the given namespace and name.
	 * @throws NullPointerException if the given namespace and/or term is <code>null</code>.
	 * @throws IllegalArgumentException if the given namespace is not absolute.
	 * @throws IllegalArgumentException if the given term is the empty string.
	 * @throws IllegalArgumentException if the concatenation of the namespace and name is not a valid {@link URI}.
	 */
	public static VocabularyTerm of(@Nonnull final URI namespace, @Nonnull final String name) {
		return new VocabularyTerm(namespace, name);
	}

	/**
	 * Constructor.
	 * @param namespace The term namespace, which must be absolute.
	 * @param name The term name, which must not be the empty string.
	 * @throws NullPointerException if the given namespace and/or term is <code>null</code>.
	 * @throws IllegalArgumentException if the given namespace is not absolute.
	 * @throws IllegalArgumentException if the given term is the empty string.
	 * @throws IllegalArgumentException if the concatenation of the namespace and name is not a valid {@link URI}.
	 */
	private VocabularyTerm(@Nonnull final URI namespace, @Nonnull final String name) {
		this.namespace = checkAbsolute(namespace);
		this.name = requireNonNull(name);
		checkArgument(!name.isEmpty(), "Vocabulary term name cannot be empty.");
		URI.create(namespace.toString() + name); //make sure the resulting URI is valid
	}

	@Override
	public int hashCode() {
		return hash(getNamespace(), getName());
	}

	@Override
	public boolean equals(final Object object) {
		if(this == object) {
			return true;
		}
		if(!(object instanceof VocabularyTerm)) {
			return false;
		}
		final VocabularyTerm vocabularyTerm = (VocabularyTerm)object;
		return getNamespace().equals(vocabularyTerm.getNamespace()) && getName().equals(vocabularyTerm.getName());
	}

	/**
	 * {@inheritDoc}
	 * @implSpec This implementation returns a concatenation of the string form of the term namespace and the term name.
	 * @implNote The implementation of {@link #toURI()} depends on this implementation.
	 * @see #getNamespace()
	 * @see #getName()
	 */
	@Override
	public String toString() {
		return getNamespace().toString() + getName();
	}

	/**
	 * Returns the URI form of the vocabulary term.
	 * @implSpec The URI is formed by simple concatenation of the namespace and name by delegation to {@link #toString()}.
	 * @return The URI representing the vocabulary term.
	 * @see #getNamespace()
	 * @see #getName()
	 * @see <a href="https://stackoverflow.com/q/17230712/421049">Correctly expanding xml namespaces without defined end character into valid URIs</a>
	 */
	public URI toURI() {
		return URI.create(toString());
	}

}
