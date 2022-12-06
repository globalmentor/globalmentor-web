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

import static java.util.Objects.*;
import static javax.xml.XMLConstants.*;

import java.net.URI;
import java.util.*;

import javax.annotation.*;
import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;

import com.globalmentor.vocab.VocabularyRegistry;

/**
 * A namespace context deriving its namespace prefix mapping from an existing {@link VocabularyRegistry}.
 * @apiNote This implementation is particularly useful for supplying prefix mappings for XPath using {@link XPath#setNamespaceContext(NamespaceContext)}.
 * @implSpec This implementation overrides any mappings of the vocabulary registry that conflict with those required by the {@link NamespaceContext} API,
 *           primarily mappings for the <code>xml</code> and <code>xmlns</code> prefixes.
 * @author Garret Wilson
 * @see XPath#setNamespaceContext(NamespaceContext)
 */
public class VocabularyNamespaceContext extends BaseNamespaceContext {

	private final VocabularyRegistry vocabularyRegistry;

	/**
	 * Vocabulary registry constructor.
	 * @param vocabularyRegistry The existing vocabulary registry to provide lookup for namespace prefixes.
	 */
	public VocabularyNamespaceContext(@Nonnull final VocabularyRegistry vocabularyRegistry) {
		this.vocabularyRegistry = requireNonNull(vocabularyRegistry);
	}

	@Override
	public String getNamespaceURI(final String prefix) {
		final String namespaceUri = super.getNamespaceURI(prefix);
		switch(namespaceUri) { //may be NULL_NS_URI, but never null
			case NULL_NS_URI:
				return vocabularyRegistry.findVocabularyByPrefix(prefix).map(URI::toString).orElse(NULL_NS_URI);
			default:
				return namespaceUri;
		}
	}

	@Override
	public String getPrefix(final String namespaceUriString) {
		final String prefix = super.getPrefix(namespaceUriString);
		return prefix != null ? prefix : vocabularyRegistry.findPrefixForVocabulary(URI.create(namespaceUriString)).orElse(null);
	}

	@Override
	public Iterator<String> getPrefixes(final String namespaceUriString) {
		Iterator<String> prefixIterator = super.getPrefixes(namespaceUriString);
		if(!prefixIterator.hasNext()) {
			final URI namespaceUri = URI.create(namespaceUriString); //required for correct URI comparison according to RFC
			prefixIterator = vocabularyRegistry.getRegisteredPrefixesByVocabulary().stream() //there may be multiple prefixes registered for the vocabulary
					.filter(entry -> entry.getKey().equals(namespaceUri)) //only look at registrations for this vocabulary
					.map(Map.Entry::getValue).iterator(); //return the prefixes mapped to the vocabulary
		}
		return prefixIterator;
	}

}
