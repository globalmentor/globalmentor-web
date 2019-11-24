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

import static com.globalmentor.xml.spec.XML.*;
import static java.util.Collections.*;

import java.util.List;

import org.w3c.dom.*;

import com.globalmentor.java.Characters;
import com.globalmentor.xml.spec.XML;

/**
 * A base format profile for XML formatting, recognizing XML space normalization characters and other indicators as per the XML specification.
 * @author Garret Wilson
 * @see XML#WHITESPACE_CHARACTERS
 * @see XML#ATTRIBUTE_SPACE
 */
public abstract class BaseXmlFormatProfile implements XmlFormatProfile {

	/**
	 * {@inheritDoc}
	 * @implSpec This implementation returns {@link XML#WHITESPACE_CHARACTERS}.
	 */
	@Override
	public Characters getSpaceNormalizationCharacters() {
		return XML.WHITESPACE_CHARACTERS;
	}

	/**
	 * {@inheritDoc}
	 * @implSpec This implementation considers an element to preserve formatting if its <code>xml:space</code> attribute is set to
	 *           {@value XML#ATTRIBUTE_SPACE_PRESERVE}.
	 * @see <a href="https://www.w3.org/TR/xml/#sec-white-space">Extensible Markup Language (XML) 1.0 (Fifth Edition), § 2.10 White Space Handling</a>
	 * @see XML#ATTRIBUTE_SPACE
	 * @see XML#ATTRIBUTE_SPACE_PRESERVE
	 */
	@Override
	public boolean isPreserved(final Element element) {
		return ATTRIBUTE_SPACE_PRESERVE.equals(element.getAttributeNS(XML_NAMESPACE_URI_STRING, ATTRIBUTE_SPACE));
	}

	/**
	 * {@inheritDoc}
	 * @implSpec This implementation returns an empty list.
	 */
	@Override
	public List<NsName> getAttributeOrder(final Element element) {
		return emptyList(); //TODO add support for xml:id
	}

}
