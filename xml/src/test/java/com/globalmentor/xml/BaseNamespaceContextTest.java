/*
 * Copyright © 2022 GlobalMentor, Inc. <https://www.globalmentor.com/>
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

package com.globalmentor.xml;

import static javax.xml.XMLConstants.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;

import javax.xml.namespace.NamespaceContext;

import org.junit.jupiter.api.*;

/**
 * Tests of {@link BaseNamespaceContext}.
 * @author Garret Wilson
 */
public class BaseNamespaceContextTest {

	/**
	 * Unknown namespace URI used for testing.
	 * @apiNote This constant is of type {@link URI} for type safety; use {@link URI#toString()} in tests.
	 */
	static final URI UNKNOWN_NAMESPACE_URI = URI.create("https://example.com/ns/unknown/");

	/** Unknown namespace prefix used for testing. */
	static final String UNKNOWN_NAMESPACE_PREFIX = "unknown";

	private NamespaceContext testNamespaceContext;

	@BeforeEach
	void setup() {
		testNamespaceContext = createTestNamespaceContext();
	}

	/**
	 * Factory to create an appropriately initialized namespace context for testing.
	 * @apiNote Subclasses should override this method to create the correct type of namespace context.
	 * @implSpec The default implementation returns a direct concrete implementation of {@link BaseNamespaceContext}.
	 * @return The namespace context under test.
	 */
	protected NamespaceContext createTestNamespaceContext() {
		return new BaseNamespaceContext() {};
	}

	/** @return The namespace context under test. */
	protected NamespaceContext getTestNamespaceContext() {
		return testNamespaceContext;
	}

	/**
	 * @implSpec This implementation depends on the prefix {@value #UNKNOWN_NAMESPACE_PREFIX} not being registered.
	 * @see BaseNamespaceContext#getNamespaceURI(String)
	 */
	@Test
	void testGetNamespaceURI() {
		assertThrows(IllegalArgumentException.class, () -> testNamespaceContext.getNamespaceURI(null));
		assertThat(XML_NS_PREFIX, getTestNamespaceContext().getNamespaceURI(XML_NS_PREFIX), is(XML_NS_URI));
		assertThat(XMLNS_ATTRIBUTE, getTestNamespaceContext().getNamespaceURI(XMLNS_ATTRIBUTE), is(XMLNS_ATTRIBUTE_NS_URI));
		assertThat("Empty (default) prefix.", getTestNamespaceContext().getNamespaceURI(DEFAULT_NS_PREFIX), is(NULL_NS_URI));
		assertThat("Unknown prefix.", getTestNamespaceContext().getNamespaceURI(UNKNOWN_NAMESPACE_PREFIX), is(NULL_NS_URI));
	}

	/**
	 * @implSpec This implementation depends on the namespace {@value #UNKNOWN_NAMESPACE_URI} as well as an empty string not being registered.
	 * @see BaseNamespaceContext#getPrefix(String)
	 */
	@Test
	void testGetPrefix() {
		assertThrows(IllegalArgumentException.class, () -> getTestNamespaceContext().getPrefix(null));
		assertThat(XML_NS_URI, getTestNamespaceContext().getPrefix(XML_NS_URI), is(XML_NS_PREFIX));
		assertThat(XMLNS_ATTRIBUTE_NS_URI, getTestNamespaceContext().getPrefix(XMLNS_ATTRIBUTE_NS_URI), is(XMLNS_ATTRIBUTE));
		assertThat("Empty (\"null\") namespace.", getTestNamespaceContext().getPrefix(NULL_NS_URI), is(nullValue()));
		assertThat("Unknown namespace.", getTestNamespaceContext().getPrefix(UNKNOWN_NAMESPACE_URI.toString()), is(nullValue()));
	}

	/**
	 * @implSpec This implementation depends on the namespace {@value #UNKNOWN_NAMESPACE_URI} as well as an empty string not being registered.
	 * @see BaseNamespaceContext#getPrefixes(String)
	 */
	@Test
	void testGetPrefixes() {
		assertThrows(IllegalArgumentException.class, () -> getTestNamespaceContext().getPrefixes(null));
		assertThat(XML_NS_URI, () -> getTestNamespaceContext().getPrefixes(XML_NS_URI), contains(XML_NS_PREFIX));
		assertThat(XMLNS_ATTRIBUTE_NS_URI, () -> getTestNamespaceContext().getPrefixes(XMLNS_ATTRIBUTE_NS_URI), contains(XMLNS_ATTRIBUTE));
		assertThat("Empty (\"null\") namespace.", (Iterable<String>)() -> getTestNamespaceContext().getPrefixes(NULL_NS_URI), is(emptyIterable()));
		assertThat("Unknown namespace.", (Iterable<String>)() -> getTestNamespaceContext().getPrefixes(UNKNOWN_NAMESPACE_URI.toString()), is(emptyIterable()));
	}

}
