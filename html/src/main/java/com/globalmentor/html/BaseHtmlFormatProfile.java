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

package com.globalmentor.html;

import static com.globalmentor.html.spec.HTML.*;

import java.util.List;
import java.util.Set;

import org.w3c.dom.*;

import com.globalmentor.html.spec.HTML;
import com.globalmentor.java.Characters;
import com.globalmentor.xml.XmlFormatProfile;
import com.globalmentor.xml.spec.*;

/**
 * A base format profile for HTML formatting.
 * @author Garret Wilson
 * @see HTML#SPACE_CHARACTERS
 */
public abstract class BaseHtmlFormatProfile implements XmlFormatProfile {

	private static final Set<NsName> PRESERVED_HTML_ELEMENTS = Set.of(NsName.of(XHTML_NAMESPACE_URI_STRING, ELEMENT_PRE),
			NsName.of(XHTML_NAMESPACE_URI_STRING, ELEMENT_SCRIPT));

	private static final List<NsName> ATTRIBUTE_ORDER = List.of(NsName.of(ATTRIBUTE_ID), NsName.of(ATTRIBUTE_NAME), NsName.of(ATTRIBUTE_TITLE),
			NsName.of(ATTRIBUTE_LANG), NsName.of(ATTRIBUTE_DIR), NsName.of(ATTRIBUTE_CLASS), NsName.of(ATTRIBUTE_STYLE));

	/**
	 * {@inheritDoc}
	 * @implSpec This implementation returns {@link HTML#SPACE_CHARACTERS}.
	 */
	@Override
	public Characters getSpaceNormalizationCharacters() {
		return HTML.SPACE_CHARACTERS;
	}

	/**
	 * {@inheritDoc}
	 * @implSpec This method preserves space of HTML <code>&lt;pre&gt;</code> and <code>&lt;script&gt;</code> elements.
	 */
	@Override
	public boolean isPreserved(final Element element) {
		return PRESERVED_HTML_ELEMENTS.contains(NsName.ofNode(element));
	}

	/**
	 * {@inheritDoc}
	 * @implSpec This implementation returns an order for HTML identification and style attributes, including <code>id</code>, <code>name</code>,
	 *           <code>class</code>, and <code>style</code>.
	 */
	@Override
	public List<NsName> getAttributeOrder(final Element element) {
		return ATTRIBUTE_ORDER;
	}

}
