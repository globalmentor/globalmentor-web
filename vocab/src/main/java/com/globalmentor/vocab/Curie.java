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
import static com.globalmentor.java.Conditions.*;
import static java.util.Objects.*;

import java.util.*;

import javax.annotation.*;

/**
 * Definition and utilities for a <dfn>Compact URI Expression</dfn> (CURIE), representing a URI of a vocabulary term using a more compact expression composed of
 * a prefix and a reference (usually a vocabulary term name) in the general form <code><var>prefix</var>:<var>reference</var></code>.
 * @apiNote This is a value class.
 * @implSpec This implementation makes no validations of the CURIE components, except to ensure that a prefix does not begin with {@value #SAFE_BEGIN}, which
 *           would be ambiguous with a safe CURIE.
 * @author Garret Wilson
 * @see <a href="https://www.w3.org/TR/curie/">CURIE Syntax 1.0</a>
 * @see <a href="https://www.w3.org/TR/rdfa-core/#s_curies">RDFa Core 1.1, § 6. CURIE Syntax Definition</a>
 */
public final class Curie {

	/** The character indicating the start of a safe CURIE. */
	public static final char SAFE_BEGIN = '[';

	/** The character indicating the end of a safe CURIE. */
	public static final char SAFE_END = ']';

	/** The delimiter separating the prefix from the reference. */
	public static final char PREFIX_DELIMITER = ':';

	private final String prefix; //nullable

	/** @return The prefix of the CURIE, if there is one. */
	public Optional<String> getPrefix() {
		return Optional.ofNullable(prefix);
	}

	private final String reference;

	/** @return The reference (name) of the CURIE. */
	public String getReference() {
		return reference;
	}

	/**
	 * Constructor.
	 * @apiNote The constructor is not accessible publicly.
	 * @param prefix The CURIE prefix, or <code>null</code> if there is no prefix.
	 * @param reference The CURIE reference.
	 * @throws NullPointerException if the reference is <code>null</code>.
	 * @throws IllegalArgumentException if the prefix and/or reference is the empty string.
	 * @throws IllegalArgumentException if the prefix begins with {@value #SAFE_BEGIN}.
	 */
	private Curie(@Nullable String prefix, @Nonnull String reference) {
		this.prefix = prefix;
		this.reference = requireNonNull(reference);
		if(prefix != null) {
			checkArgument(!prefix.isEmpty(), "An empty string is not allowed for the CURIE prefix.");
			checkArgument(!startsWith(prefix, SAFE_BEGIN), "CURIE prefix `%s` cannot begin with a safe CURIE delimiter `%s`.", prefix, SAFE_BEGIN);
		}
		checkArgument(!reference.isEmpty(), "CURIE with prefix `%s` cannot have empty reference.", prefix);
	}

	/**
	 * Static factory method.
	 * @param prefix The CURIE prefix, or <code>null</code> if there is no prefix.
	 * @param reference The CURIE reference.
	 * @throws NullPointerException if the reference is <code>null</code>.
	 * @throws IllegalArgumentException if the prefix and/or reference is the empty string.
	 * @throws IllegalArgumentException if the prefix begins with {@value #SAFE_BEGIN}.
	 */
	public static Curie of(@Nullable String prefix, @Nonnull String reference) {
		return new Curie(prefix, reference);
	}

	@Override
	public int hashCode() {
		return Objects.hash(prefix, reference);
	}

	@Override
	public boolean equals(final Object object) {
		if(this == object) {
			return true;
		}
		if(!(object instanceof Curie)) {
			return false;
		}
		final Curie curie = (Curie)object;
		return Objects.equals(prefix, curie.prefix) && reference.equals(curie.reference);
	}

	/**
	 * {@inheritDoc}
	 * @implSpec This implementation returns a string in the form <code>bar</code> or <code>foo:bar</code>.
	 */
	@Override
	public String toString() {
		return toString(prefix, reference);
	}

	/**
	 * Returns a safe CURIE string representation.
	 * @return A string in the form <code>[bar]</code> or <code>[foo:bar]</code>.
	 */
	public String toSafeString() {
		return toString(prefix, reference, true);
	}

	/**
	 * Parses the given CURIE string, allowing the normal or safe form.
	 * @param text The text form of a CURIE, which may be a safe CURIE.
	 * @return A CURIE encapsulation value object.
	 * @throws IllegalArgumentException if the given string is not a valid CURIE, including the empty string, an empty reference, or a prefix begins with
	 *           {@value #SAFE_BEGIN}.
	 */
	public static Curie parse(@Nonnull final CharSequence text) {
		return parse(text, true);
	}

	/**
	 * Parses the given CURIE string.
	 * @param text The text form of a CURIE, which may be a safe CURIE.
	 * @param allowSafe Whether the safe CURIE form should be allowed.
	 * @return A CURIE encapsulation value object.
	 * @throws IllegalArgumentException if the given string is not a valid CURIE, including the empty string, an empty reference, or a prefix begins with
	 *           {@value #SAFE_BEGIN}.
	 */
	public static Curie parse(@Nonnull final CharSequence text, final boolean allowSafe) {
		final int textLength = text.length();
		checkArgument(textLength != 0, "The empty string is not a valid CURIE.");
		if(startsWith(text, SAFE_BEGIN)) {
			checkArgument(allowSafe, "Safe CURIE `%s` not allowed in this context; remove `%s` and `%s` delimiters.", text, SAFE_BEGIN, SAFE_END);
			checkArgument(endsWith(text, SAFE_END), "CURIE `%s` missing matching ending safe CURIE delimiter `%s`.", text, SAFE_END);
			return parse(text.subSequence(1, textLength - 1), false); //recursively parse the CURIE inside the safe delimiters, not allowing the safe form this time
		}
		final int delimiterIndex = indexOf(text, PREFIX_DELIMITER);
		final String prefix = delimiterIndex > 0 ? text.subSequence(0, delimiterIndex).toString() : null; //the spec allows a CURIE beginning with `:`
		assert prefix == null || !prefix.isEmpty() : "An empty prefix should have been converted to `null`.";
		final String reference = text.subSequence(delimiterIndex + 1, textLength).toString(); //this works even with a missing delimiter index of -1
		checkArgument(!reference.isEmpty(), "CURIE `%s` cannot have empty reference.", text);
		return Curie.of(prefix, reference);
	}

	/**
	 * Produces the string form of a CURIE in the general form <code>foo:bar</code>. No validation is performed on the component strings.
	 * @apiNote The prefix and reference should not begin or end with the safe CURIE delimiters {@value #SAFE_BEGIN} and {@value #SAFE_END}, respectively.
	 * @param prefix The CURIE prefix, or <code>null</code> if there is no prefix.
	 * @param reference The CURIE reference.
	 * @throws NullPointerException if the reference is <code>null</code>.
	 */
	public static String toString(@Nullable String prefix, @Nonnull String reference) {
		return toString(prefix, reference, false);
	}

	/**
	 * Produces the string form of a CURIE in the general form <code>foo:bar</code> or <code>[foo:bar]</code>. No validation is performed on the component
	 * strings.
	 * @apiNote The prefix and reference should not begin or end with the safe CURIE delimiters {@value #SAFE_BEGIN} and {@value #SAFE_END}, respectively.
	 * @param prefix The CURIE prefix, or <code>null</code> if there is no prefix.
	 * @param reference The CURIE reference.
	 * @param safe <code>true</code> if a safe CURIE should be generated in the general form <code>[foo:bar]</code>.
	 * @throws NullPointerException if the reference is <code>null</code>.
	 */
	public static String toString(@Nullable String prefix, @Nonnull String reference, final boolean safe) {
		final StringBuilder stringBuilder = new StringBuilder();
		if(safe) {
			stringBuilder.append(SAFE_BEGIN);
		}
		if(prefix != null) {
			stringBuilder.append(prefix);
			stringBuilder.append(PREFIX_DELIMITER);
		}
		stringBuilder.append(reference);
		if(safe) {
			stringBuilder.append(SAFE_END);
		}
		return stringBuilder.toString();
	}

}
