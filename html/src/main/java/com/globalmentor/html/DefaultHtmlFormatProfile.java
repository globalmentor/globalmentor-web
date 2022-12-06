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

import static com.globalmentor.html.def.HTML.*;

import java.util.*;

import com.globalmentor.xml.def.*;

/**
 * The HTNML format profile for the XML to serializer to use by default.
 * @apiNote This class cannot be instantiated directly, as the singleton {@link #INSTANCE} is already provided. However the class may be subclassed to override
 *          and customize behavior.
 * @implSpec This profile considers an element a block element if it is HTML5 <dfn>flow content</dfn> that is not <dfn>phrasing content</dfn>, or <dfn>metadata
 *           content</dfn>.
 * @implSpec This profile makes the direct child content of <code>&lt;html&gt;</code> flush with no indention.
 * @implSpec This implementation returns an order for HTML identification, style, and metadata attributes, including <code>id</code>, <code>name</code>,
 *           <code>class</code>, <code>style</code>, and <code>title</code>.
 * @author Garret Wilson
 */
public class DefaultHtmlFormatProfile extends BaseHtmlFormatProfile {

	/** Shared singleton instance. */
	public static final DefaultHtmlFormatProfile INSTANCE = new DefaultHtmlFormatProfile();

	/** Constructor. */
	protected DefaultHtmlFormatProfile() {
	}

	/** The elements this base implementation considers flush elements for formatting. */
	public static final Set<NsName> FLUSH_ELEMENTS = Set.of(NsName.of(XHTML_NAMESPACE_URI_STRING, ELEMENT_HTML));

	/** The predefined attribute order this implementation uses for all elements. */
	public static final List<NsName> ATTRIBUTE_ORDER = List.of(
			//element identification
			NsName.of(ATTRIBUTE_ID), NsName.of(ATTRIBUTE_NAME),
			//element style
			NsName.of(ATTRIBUTE_CLASS), NsName.of(ATTRIBUTE_STYLE),
			//element description
			NsName.of(ATTRIBUTE_LANG), NsName.of(ATTRIBUTE_DIR),
			//content source
			NsName.of(ATTRIBUTE_SRC), NsName.of(ELEMENT_IMG_ATTRIBUTE_ALT),
			//content reference
			NsName.of(LINK_ATTRIBUTE_REL), NsName.of(ATTRIBUTE_HREF), NsName.of(ELEMENT_A_ATTRIBUTE_HREFLANG), NsName.of(LINK_ATTRIBUTE_TYPE),
			NsName.of(ELEMENT_A_ATTRIBUTE_TARGET),
			//metadata
			NsName.of(ATTRIBUTE_TITLE));

	/**
	 * {@inheritDoc}
	 * @implSpec This implementation returns {@link #FLUSH_ELEMENTS}.
	 */
	@Override
	protected boolean isFlush(final NsName element) {
		return FLUSH_ELEMENTS.contains(element);
	}

	/**
	 * {@inheritDoc}
	 * @implSpec This implementation returns {@link #ATTRIBUTE_ORDER}.
	 */
	@Override
	protected List<NsName> getAttributeOrder(final NsName element) {
		return ATTRIBUTE_ORDER;
	}

}
