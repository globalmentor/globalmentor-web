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

import static com.github.npathai.hamcrestopt.OptionalMatchers.*;
import static com.globalmentor.lex.CompoundTokenization.*;
import static java.util.function.Function.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.function.Function;

import org.junit.jupiter.api.*;

/**
 * Tests of {@link Curie}.
 * @author Garret Wilson
 */
public class CurieTest {

	/** @see Curie#of(String, String) */
	@Test
	public void testOf() {
		assertThat(Curie.of(null, "bar").getPrefix(), isEmpty());
		assertThat(Curie.of(null, "bar").getReference(), is("bar"));
		assertThat(Curie.of("foo", "bar").getPrefix(), isPresentAndIs("foo"));
		assertThat(Curie.of("foo", "bar").getReference(), is("bar"));
		assertThrows(IllegalArgumentException.class, () -> Curie.of("", "bar"));
		assertThrows(IllegalArgumentException.class, () -> Curie.of("foo", ""));
		assertThrows(IllegalArgumentException.class, () -> Curie.of("[foo", "bar"));
	}

	/** @see Curie#parse(String, boolean) */
	@Test
	public void testParse() {
		assertThat(Curie.parse("bar", false), is(Curie.of(null, "bar")));
		assertThat(Curie.parse(":bar", false), is(Curie.of(null, "bar")));
		assertThat(Curie.parse("foo:bar", false), is(Curie.of("foo", "bar")));
		assertThat(Curie.parse("bar", true), is(Curie.of(null, "bar")));
		assertThat(Curie.parse(":bar", true), is(Curie.of(null, "bar")));
		assertThat(Curie.parse("foo:bar", true), is(Curie.of("foo", "bar")));

		assertThrows(IllegalArgumentException.class, () -> Curie.parse("[bar", false));
		assertThrows(IllegalArgumentException.class, () -> Curie.parse("[:bar", false));
		assertThrows(IllegalArgumentException.class, () -> Curie.parse("[foo:bar", false));
		assertThrows(IllegalArgumentException.class, () -> Curie.parse("[bar", true));
		assertThrows(IllegalArgumentException.class, () -> Curie.parse("[:bar", true));
		assertThrows(IllegalArgumentException.class, () -> Curie.parse("[foo:bar", true));

		assertThrows(IllegalArgumentException.class, () -> Curie.parse("[bar]", false));
		assertThrows(IllegalArgumentException.class, () -> Curie.parse("[:bar]", false));
		assertThrows(IllegalArgumentException.class, () -> Curie.parse("[foo:bar]", false));
		assertThat(Curie.parse("[bar]", true), is(Curie.of(null, "bar")));
		assertThat(Curie.parse("[:bar]", true), is(Curie.of(null, "bar")));
		assertThat(Curie.parse("[foo:bar]", true), is(Curie.of("foo", "bar")));

		assertThrows(IllegalArgumentException.class, () -> Curie.parse("[[bar]]", false));
		assertThrows(IllegalArgumentException.class, () -> Curie.parse("[[:bar]]", false));
		assertThrows(IllegalArgumentException.class, () -> Curie.parse("[[foo:bar]]", false));
		assertThrows(IllegalArgumentException.class, () -> Curie.parse("[[bar]]", true));
		assertThrows(IllegalArgumentException.class, () -> Curie.parse("[[:bar]]", true));
		assertThrows(IllegalArgumentException.class, () -> Curie.parse("[[foo:bar]]", true));
	}

	/** @see Curie#mapReference(Function) */
	@Test
	public void testMapReference() {
		assertThat(Curie.parse("bar").mapReference(identity()), is(Curie.parse("bar")));
		assertThat(Curie.parse("bar").mapReference(String::toUpperCase), is(Curie.parse("BAR")));
		assertThat(Curie.parse("foo:bar").mapReference(identity()), is(Curie.parse("foo:bar")));
		assertThat(Curie.parse("foo:bar").mapReference(String::toUpperCase), is(Curie.parse("foo:BAR")));
		assertThat(Curie.parse("test:fooBar").mapReference(CAMEL_CASE::toKebabCase), is(Curie.parse("test:foo-bar")));
	}

	/**
	 * @see Curie#toString(String, String, boolean)
	 * @see Curie#toString()
	 * @see Curie#toSafeString()
	 */
	@Test
	public void testToString() {
		assertThat(Curie.toString(null, "bar", false), is("bar"));
		assertThat(Curie.toString("foo", "bar", false), is("foo:bar"));

		assertThat(Curie.toString(null, "bar", true), is("[bar]"));
		assertThat(Curie.toString("foo", "bar", true), is("[foo:bar]"));

		assertThat(Curie.of(null, "bar").toString(), is("bar"));
		assertThat(Curie.of("foo", "bar").toString(), is("foo:bar"));

		assertThat(Curie.of(null, "bar").toSafeString(), is("[bar]"));
		assertThat(Curie.of("foo", "bar").toSafeString(), is("[foo:bar]"));
	}

}
