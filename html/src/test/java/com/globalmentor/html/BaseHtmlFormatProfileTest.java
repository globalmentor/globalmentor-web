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
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.*;
import org.w3c.dom.Element;

import com.globalmentor.xml.def.NsName;

/**
 * Tests {@link BaseHtmlFormatProfile} to ensure it appropriately characterizes HTML.
 * @author Garret Wilson
 *
 */
public class BaseHtmlFormatProfileTest {

	/** A default implementation of the base profile, with no flush elements. */
	private static final BaseHtmlFormatProfile PROFILE = new BaseHtmlFormatProfile() {
		@Override
		protected boolean isBreak(final NsName element) {
			return false;
		}

		@Override
		protected boolean isFlush(final NsName element) {
			return false;
		}
	};

	/** @see BaseHtmlFormatProfile#isBlock(Element) */
	@Test
	public void testBlockElements() {
		Stream.of(ELEMENT_HTML,
				//<head>
				ELEMENT_HEAD, ELEMENT_TITLE, ELEMENT_META, ELEMENT_LINK, ELEMENT_SCRIPT,
				//<body>
				ELEMENT_BODY, ELEMENT_DIV, ELEMENT_PRE,
				//lists
				ELEMENT_DL, ELEMENT_DT, ELEMENT_DD, ELEMENT_OL, ELEMENT_UL, ELEMENT_LI,
				//metadata
				ELEMENT_BASE, ELEMENT_STYLE, ELEMENT_TEMPLATE).map(elementName -> NsName.of(XHTML_NAMESPACE_URI_STRING, elementName))
				.forEach(element -> assertThat(element.toString(), PROFILE.isBlock(element), is(true)));
		Stream.of(ELEMENT_SPAN, ELEMENT_IMG, ELEMENT_EM,
				//hidden elements we don't want to be block
				ELEMENT_BASEFONT, ELEMENT_RP).map(elementName -> NsName.of(XHTML_NAMESPACE_URI_STRING, elementName))
				.forEach(element -> assertThat(element.toString(), PROFILE.isBlock(element), is(false)));
	}

}
