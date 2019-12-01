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
import static java.util.stream.Collectors.*;
import static java.util.stream.Stream.*;

import java.util.*;
import java.util.stream.Stream;

import com.globalmentor.html.spec.HTML;
import com.globalmentor.java.Characters;
import com.globalmentor.xml.AbstractXmlFormatProfile;
import com.globalmentor.xml.spec.*;

/**
 * A base format profile for HTML formatting.
 * @author Garret Wilson
 * @see HTML#SPACE_CHARACTERS
 */
public abstract class BaseHtmlFormatProfile extends AbstractXmlFormatProfile {

	private static final Set<NsName> BLOCK_ELEMENTS;

	static {
		Stream<NsName> blockElements = HTML.BLOCK_ELEMENTS.stream();
		blockElements = concat(blockElements, HTML.LIST_ITEM_ELEMENTS.stream());
		blockElements = concat(blockElements, HTML.METADATA_CONTENT.stream());
		blockElements = concat(blockElements, Stream.of(ELEMENT_HEAD).map(elementName -> NsName.of(XHTML_NAMESPACE_URI_STRING, elementName)));
		//consider adding ELEMENT_DATALIST
		//consider adding contained items: ELEMENT_AREA, ELEMENT_OPTION, ELEMENT_PARAM, ELEMENT_SOURCE, ELEMENT_TRACK
		BLOCK_ELEMENTS = blockElements.collect(toSet());
	}

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
	 * @implSpec This method considers an element a block element if its suggested user agent styling is <code>display: block</code> or
	 *           <code>display: list-item</code>.
	 * @implNote An alternate approach would be to use content categories, considering an element a block element if it is HTML5 <dfn>flow content</dfn> that is
	 *           not <dfn>phrasing content</dfn>; <dfn>metadata content</dfn>; or one of {@code <html>}, {@code <head>}, or {@code <body>}. However this would
	 *           still not include list-item elements such as {@code <li>} and other elements.
	 * @see <a href="https://www.w3.org/TR/html52/rendering.html#rendering">HTML 5.2 § 10. Rendering</a>
	 */
	@Override
	protected boolean isBlock(final NsName element) {
		return BLOCK_ELEMENTS.contains(element);
	}

	/**
	 * {@inheritDoc}
	 * @implSpec This method preserves space of HTML <code>&lt;pre&gt;</code> and <code>&lt;script&gt;</code> elements.
	 */
	@Override
	protected boolean isPreserved(final NsName element) {
		return PRESERVED_HTML_ELEMENTS.contains(element);
	}

	/**
	 * {@inheritDoc}
	 * @implSpec This implementation returns an order for HTML identification and style attributes, including <code>id</code>, <code>name</code>,
	 *           <code>class</code>, and <code>style</code>.
	 */
	@Override
	protected List<NsName> getAttributeOrder(final NsName element) {
		return ATTRIBUTE_ORDER;
	}

}
