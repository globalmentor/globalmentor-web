/*
 * Copyright © 1996-2019 GlobalMentor, Inc. <https://www.globalmentor.com/>
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

import com.globalmentor.vocab.BaseVocabularySpecification;
import com.globalmentor.xml.def.XML;

/**
 * Specification for XML namespace vocabularies.
 * @author Garret Wilson
 */
public class XmlVocabularySpecification extends BaseVocabularySpecification {

	/** The shared, singleton instance of this specification. */
	public static XmlVocabularySpecification INSTANCE = new XmlVocabularySpecification();

	/** This specification cannot be instantiated publicly. */
	private XmlVocabularySpecification() {
	}

	/**
	 * {@inheritDoc} This version determines whether the string is valid XML name.
	 * @see XML#isName(String)
	 */
	@Override
	public boolean isValidPrefix(final String prefix) {
		return XML.isName(prefix);
	}

}
