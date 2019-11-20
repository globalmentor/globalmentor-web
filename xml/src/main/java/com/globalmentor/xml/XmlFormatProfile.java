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

package com.globalmentor.xml;

import javax.annotation.*;

import org.w3c.dom.*;

import com.globalmentor.java.Characters;
import com.globalmentor.xml.spec.XML;

/**
 * Specifies a strategy for characterizing an XML document for XML serialization by indicating how different elements are classified for formatting.
 * @apiNote This interface does not provide settings for actual formatting, such as the amount of indentation, but instead helps the formatter decide which
 *          things are to be formatted as what.
 * @author Garret Wilson
 */
public interface XmlFormatProfile {

	/**
	 * Default XML formatting profile which uses {@link XML#WHITESPACE_CHARACTERS} as space normalization characters, and considers all elements block elements.
	 */
	public static final XmlFormatProfile DEFAULT = new XmlFormatProfile() {
		@Override
		public Characters getSpaceNormalizationCharacters() {
			return XML.WHITESPACE_CHARACTERS;
		}

		@Override
		public boolean isBlock(final Element element) {
			return true;
		}
	};

	/**
	 * Returns the characters considered "space characters" for normalizations.
	 * @apiNote This typically includes actual space characters and tabs, but usually should <em>not</em> include all Unicode characters specified as
	 *          "whitespace".
	 * @return The characters this profile considers "spaces" for the purpose of normalization.
	 */
	public Characters getSpaceNormalizationCharacters();

	/**
	 * Indicates whether the given element is considered block element for purposes of formatting.
	 * @param element A DOM element.
	 * @return <code>true</code> if the element should be formatted as a block element.
	 */
	public boolean isBlock(@Nonnull final Element element);

}
