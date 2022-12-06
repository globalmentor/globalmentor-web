/*
 * Copyright © 2020 GlobalMentor, Inc. <http://www.globalmentor.com/>
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

package com.globalmentor.xml.def;

import static com.github.npathai.hamcrestopt.OptionalMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.net.URI;

import org.junit.jupiter.api.Test;

/**
 * Tests of {@link NsName}.
 * @author Garret Wilson
 */
public class NsQualifiedNameTest {

	private static final URI FOO_NAMESPACE = URI.create("https://example.com/foo/");

	/** @see NsQualifiedName#of(String) */
	@Test
	void testOfLocalName() {
		final NsQualifiedName nsQualifiedName = NsQualifiedName.of("bar");
		assertThat(nsQualifiedName.getNamespaceString(), is(nullValue()));
		assertThat(nsQualifiedName.getNamespaceUri(), is(nullValue()));
		assertThat(nsQualifiedName.findPrefix(), isEmpty());
		assertThat(nsQualifiedName.getLocalName(), is("bar"));
	}

	/** @see NsQualifiedName#of(String, String) */
	@Test
	void testOfNamespaceString() {
		final NsQualifiedName nsQualifiedName = NsQualifiedName.of(FOO_NAMESPACE.toString(), "foo:bar");
		assertThat(nsQualifiedName.getNamespaceString(), is(FOO_NAMESPACE.toString()));
		assertThat(nsQualifiedName.getNamespaceUri(), is(FOO_NAMESPACE));
		assertThat(nsQualifiedName.findPrefix(), isPresentAndIs("foo"));
		assertThat(nsQualifiedName.getLocalName(), is("bar"));
	}

	/** @see NsQualifiedName#of(String, String) */
	@Test
	void testOfNamespaceStringWithNoPrefix() {
		final NsQualifiedName nsQualifiedName = NsQualifiedName.of(FOO_NAMESPACE.toString(), "bar");
		assertThat(nsQualifiedName.getNamespaceString(), is(FOO_NAMESPACE.toString()));
		assertThat(nsQualifiedName.getNamespaceUri(), is(FOO_NAMESPACE));
		assertThat(nsQualifiedName.findPrefix(), isEmpty());
		assertThat(nsQualifiedName.getLocalName(), is("bar"));
	}

	/** @see NsQualifiedName#of(String, String) */
	@Test
	void testOfNullNamespaceString() {
		final NsQualifiedName nsQualifiedName = NsQualifiedName.of((String)null, "foo:bar");
		assertThat(nsQualifiedName.getNamespaceString(), is(nullValue()));
		assertThat(nsQualifiedName.getNamespaceUri(), is(nullValue()));
		assertThat(nsQualifiedName.findPrefix(), isPresentAndIs("foo"));
		assertThat(nsQualifiedName.getLocalName(), is("bar"));
	}

	/** @see NsQualifiedName#of(String, String) */
	@Test
	void testOfNullNamespaceStringWithNoPrefix() {
		final NsQualifiedName nsQualifiedName = NsQualifiedName.of((String)null, "bar");
		assertThat(nsQualifiedName.getNamespaceString(), is(nullValue()));
		assertThat(nsQualifiedName.getNamespaceUri(), is(nullValue()));
		assertThat(nsQualifiedName.findPrefix(), isEmpty());
		assertThat(nsQualifiedName.getLocalName(), is("bar"));
	}

	/** @see NsQualifiedName#of(URI, String) */
	@Test
	void testOfNamespaceUri() {
		final NsQualifiedName nsQualifiedName = NsQualifiedName.of(FOO_NAMESPACE, "foo:bar");
		assertThat(nsQualifiedName.getNamespaceString(), is(FOO_NAMESPACE.toString()));
		assertThat(nsQualifiedName.getNamespaceUri(), is(FOO_NAMESPACE));
		assertThat(nsQualifiedName.findPrefix(), isPresentAndIs("foo"));
		assertThat(nsQualifiedName.getLocalName(), is("bar"));
	}

	/** @see NsQualifiedName#of(URI, String) */
	@Test
	void testOfNamespaceUriWithNoPrefix() {
		final NsQualifiedName nsQualifiedName = NsQualifiedName.of(FOO_NAMESPACE, "bar");
		assertThat(nsQualifiedName.getNamespaceString(), is(FOO_NAMESPACE.toString()));
		assertThat(nsQualifiedName.getNamespaceUri(), is(FOO_NAMESPACE));
		assertThat(nsQualifiedName.findPrefix(), isEmpty());
		assertThat(nsQualifiedName.getLocalName(), is("bar"));
	}

	/** @see NsQualifiedName#of(URI, String) */
	@Test
	void testOfNullNamespaceUri() {
		final NsQualifiedName nsQualifiedName = NsQualifiedName.of((URI)null, "foo:bar");
		assertThat(nsQualifiedName.getNamespaceString(), is(nullValue()));
		assertThat(nsQualifiedName.getNamespaceUri(), is(nullValue()));
		assertThat(nsQualifiedName.findPrefix(), isPresentAndIs("foo"));
		assertThat(nsQualifiedName.getLocalName(), is("bar"));
	}

	/** @see NsQualifiedName#of(URI, String) */
	@Test
	void testOfNullNamespaceUriWithNoPrefix() {
		final NsQualifiedName nsQualifiedName = NsQualifiedName.of((URI)null, "bar");
		assertThat(nsQualifiedName.getNamespaceString(), is(nullValue()));
		assertThat(nsQualifiedName.getNamespaceUri(), is(nullValue()));
		assertThat(nsQualifiedName.findPrefix(), isEmpty());
		assertThat(nsQualifiedName.getLocalName(), is("bar"));
	}

	/** @see NsQualifiedName#equals(Object) */
	@Test
	void testEquals() {
		//this doesn't test every variation, but shows generally that equals() is working, even from different construction
		assertThat(NsQualifiedName.of(FOO_NAMESPACE.toString(), "foo:bar"), equalTo(NsQualifiedName.of(FOO_NAMESPACE, "foo:bar")));
	}

	/** @see NsQualifiedName#toNsName() */
	void testToNsName() {
		final NsQualifiedName nsQualifiedName = NsQualifiedName.of(FOO_NAMESPACE.toString(), "foo:bar");
		final NsName nsName = nsQualifiedName.toNsName();
		assertThat(nsName.getNamespaceString(), is(FOO_NAMESPACE.toString()));
		assertThat(nsName.getNamespaceUri(), is(FOO_NAMESPACE));
		assertThat(nsName.getLocalName(), is("bar"));
	}

}
