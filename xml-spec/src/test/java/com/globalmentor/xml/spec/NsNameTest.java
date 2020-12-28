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

package com.globalmentor.xml.spec;

import static com.github.npathai.hamcrestopt.OptionalMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.net.URI;

import org.junit.jupiter.api.Test;

/**
 * Tests of {@link NsName}.
 * @author Garret Wilson
 */
public class NsNameTest {

	private static final URI FOO_NAMESPACE = URI.create("https://example.com/foo/");

	/** @see NsName#of(String) */
	@Test
	void testOfLocalName() {
		final NsName nsName = NsName.of("bar");
		assertThat(nsName.getNamespaceString(), is(nullValue()));
		assertThat(nsName.getNamespaceUri(), is(nullValue()));
		assertThat(nsName.getLocalName(), is("bar"));
	}

	/** @see NsName#of(String, String) */
	@Test
	void testOfNamespaceString() {
		final NsName nsName = NsName.of(FOO_NAMESPACE.toString(), "bar");
		assertThat(nsName.getNamespaceString(), is(FOO_NAMESPACE.toString()));
		assertThat(nsName.getNamespaceUri(), is(FOO_NAMESPACE));
		assertThat(nsName.getLocalName(), is("bar"));
	}

	/** @see NsName#of(String, String) */
	@Test
	void testOfNullNamespaceString() {
		final NsName nsName = NsName.of((String)null, "bar");
		assertThat(nsName.getNamespaceString(), is(nullValue()));
		assertThat(nsName.getNamespaceUri(), is(nullValue()));
		assertThat(nsName.getLocalName(), is("bar"));
	}

	/** @see NsName#of(URI, String) */
	@Test
	void testOfNamespaceUri() {
		final NsName nsName = NsName.of(FOO_NAMESPACE, "bar");
		assertThat(nsName.getNamespaceString(), is(FOO_NAMESPACE.toString()));
		assertThat(nsName.getNamespaceUri(), is(FOO_NAMESPACE));
		assertThat(nsName.getLocalName(), is("bar"));
	}

	/** @see NsName#of(URI, String) */
	@Test
	void testOfNullNamespaceUri() {
		final NsName nsName = NsName.of((URI)null, "bar");
		assertThat(nsName.getNamespaceString(), is(nullValue()));
		assertThat(nsName.getNamespaceUri(), is(nullValue()));
		assertThat(nsName.getLocalName(), is("bar"));
	}

	/** @see NsName#equals(Object) */
	@Test
	void testEquals() {
		//this doesn't test every variation, but shows generally that equals() is working, even from different construction
		assertThat(NsName.of(FOO_NAMESPACE.toString(), "bar"), equalTo(NsName.of(FOO_NAMESPACE, "bar")));
	}

	/** @see NsName#withPrefix(String) */
	void testWithPrefix() {
		final NsName nsName = NsName.of(FOO_NAMESPACE, "bar");
		final NsQualifiedName nsQualifiedName = nsName.withPrefix("foo");
		assertThat(nsQualifiedName.getNamespaceString(), is(FOO_NAMESPACE.toString()));
		assertThat(nsQualifiedName.getNamespaceUri(), is(FOO_NAMESPACE));
		assertThat(nsQualifiedName.findPrefix(), isPresentAndIs("foo"));
		assertThat(nsQualifiedName.getLocalName(), is("bar"));
	}

	/** @see NsName#withNoPrefix() */
	void testWithNoPrefix() {
		final NsName nsName = NsName.of(FOO_NAMESPACE, "bar");
		final NsQualifiedName nsQualifiedName = nsName.withNoPrefix();
		assertThat(nsQualifiedName.getNamespaceString(), is(FOO_NAMESPACE.toString()));
		assertThat(nsQualifiedName.getNamespaceUri(), is(FOO_NAMESPACE));
		assertThat(nsQualifiedName.findPrefix(), isEmpty());
		assertThat(nsQualifiedName.getLocalName(), is("bar"));
	}

}
