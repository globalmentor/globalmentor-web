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

package com.globalmentor.rdfa.spec;

import static java.util.Collections.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.util.*;

import org.junit.jupiter.api.*;

import com.globalmentor.vocab.VocabularyRegistry;

/**
 * Tests of {@link RDFa}.
 * @author Garret Wilson
 */
public class RDFaTest {

	private static final URI CC_NAMESPACE = URI.create("http://creativecommons.org/ns#");
	private static final URI DC_NAMESPACE = URI.create("http://purl.org/dc/terms/");
	private static final URI OG_NAMESPACE = URI.create("http://ogp.me/ns#");

	/** @see RDFa#toPrefixAttributeValue(Iterable)) */
	@Test
	public void testToPrefixAttributeValue() {
		assertThat(RDFa.toPrefixAttributeValue(emptyList()), is(""));
		assertThat(RDFa.toPrefixAttributeValue(List.of(Map.entry("dc", DC_NAMESPACE))), is("dc: http://purl.org/dc/terms/"));
		assertThat(RDFa.toPrefixAttributeValue(List.of(Map.entry("dc", DC_NAMESPACE), Map.entry("og", OG_NAMESPACE))),
				is("dc: http://purl.org/dc/terms/ og: http://ogp.me/ns#"));
		assertThat(RDFa.toPrefixAttributeValue(List.of(Map.entry("dc", DC_NAMESPACE), Map.entry("og", OG_NAMESPACE), Map.entry("cc", CC_NAMESPACE))),
				is("dc: http://purl.org/dc/terms/ og: http://ogp.me/ns# cc: http://creativecommons.org/ns#"));
	}

	/** @see RDFa#toPrefixAttributeValue(VocabularyRegistry) */
	@Test
	public void testToPrefixAttributeValueRegistryEmpty() {
		assertThat(RDFa.toPrefixAttributeValue(VocabularyRegistry.EMPTY), is(""));
	}

	/** @see RDFa#toPrefixAttributeValue(VocabularyRegistry) */
	@Test
	public void testToPrefixAttributeValueRegistryOneRegistration() {
		final VocabularyRegistry registry = VocabularyRegistry.of(Map.entry("dc", DC_NAMESPACE));
		assertThat(RDFa.toPrefixAttributeValue(registry), is("dc: http://purl.org/dc/terms/"));
	}

	/** @see RDFa#toPrefixAttributeValue(VocabularyRegistry) */
	@Test
	public void testToPrefixAttributeValueRegistryTwoRegistrations() {
		final VocabularyRegistry registry = VocabularyRegistry.of(Map.entry("dc", DC_NAMESPACE), Map.entry("og", OG_NAMESPACE));
		assertThat(RDFa.toPrefixAttributeValue(registry),
				oneOf("dc: http://purl.org/dc/terms/ og: http://ogp.me/ns#", "og: http://ogp.me/ns# dc: http://purl.org/dc/terms/"));
	}

	/** @see RDFa#fromPrefixAttributeValue(CharSequence) */
	@Test
	public void testFromPrefixAttributeValue() {
		assertThat(RDFa.fromPrefixAttributeValue(""), is(emptyList()));
		assertThat(RDFa.fromPrefixAttributeValue("dc: http://purl.org/dc/terms/"), is(List.of(Map.entry("dc", DC_NAMESPACE))));
		assertThat(RDFa.fromPrefixAttributeValue("dc: http://purl.org/dc/terms/ og: http://ogp.me/ns#"),
				is(List.of(Map.entry("dc", DC_NAMESPACE), Map.entry("og", OG_NAMESPACE))));
		assertThat(RDFa.fromPrefixAttributeValue("dc: http://purl.org/dc/terms/ og: http://ogp.me/ns# cc: http://creativecommons.org/ns#"),
				is(List.of(Map.entry("dc", DC_NAMESPACE), Map.entry("og", OG_NAMESPACE), Map.entry("cc", CC_NAMESPACE))));
		assertThrows(IllegalArgumentException.class, () -> RDFa.fromPrefixAttributeValue("dc: http://purl.org/dc/terms/ og: http://ogp.me/ns# cc:"));
		assertThrows(IllegalArgumentException.class,
				() -> RDFa.fromPrefixAttributeValue("dc: http://purl.org/dc/terms/ : http://ogp.me/ns# cc: http://creativecommons.org/ns#"));
	}

	/** @see RDFa#fromPrefixAttributeValue(CharSequence) */
	@Test
	public void testFromPrefixAttributeValueIgnoresWhiteSpace() {
		assertThat(RDFa.fromPrefixAttributeValue("\t\r\n    dc: http://purl.org/dc/terms/ og: http://ogp.me/ns#"),
				is(List.of(Map.entry("dc", DC_NAMESPACE), Map.entry("og", OG_NAMESPACE))));
		assertThat(RDFa.fromPrefixAttributeValue("dc: http://purl.org/dc/terms/ og:\t\r\nhttp://ogp.me/ns#"),
				is(List.of(Map.entry("dc", DC_NAMESPACE), Map.entry("og", OG_NAMESPACE))));
		assertThat(RDFa.fromPrefixAttributeValue("    dc: http://purl.org/dc/terms/ og: http://ogp.me/ns#    \t\r\n"),
				is(List.of(Map.entry("dc", DC_NAMESPACE), Map.entry("og", OG_NAMESPACE))));
		assertThat(RDFa.fromPrefixAttributeValue("    dc: http://purl.org/dc/terms/ og:\r\nhttp://ogp.me/ns#\t \t"),
				is(List.of(Map.entry("dc", DC_NAMESPACE), Map.entry("og", OG_NAMESPACE))));
	}

}
