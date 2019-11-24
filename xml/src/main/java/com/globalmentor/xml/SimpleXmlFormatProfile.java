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

import static java.util.Collections.emptyList;
import static java.util.Objects.*;

import java.util.List;
import java.util.Set;

import javax.annotation.*;

import org.w3c.dom.*;

import com.globalmentor.java.Characters;

/**
 * A simple implementation of a format profile.
 * @author Garret Wilson
 */
public class SimpleXmlFormatProfile implements XmlFormatProfile {

	private final Characters spaceNormalizationCharacters;

	private final Set<NsName> blockElements;

	private final Set<NsName> preservedElements;

	/**
	 * Constructor.
	 * @param spaceNormalizationCharacters The characters this profile considers "spaces" for the purpose of normalization.
	 * @param blockElements The elements considered block elements for purposes of formatting.
	 * @param contentPreservedElements The elements which will be excluded from formatting content, although attributes will still be formatted.
	 */
	public SimpleXmlFormatProfile(@Nonnull final Characters spaceNormalizationCharacters, @Nonnull final Set<NsName> blockElements,
			@Nonnull final Set<NsName> contentPreservedElements) {
		this.spaceNormalizationCharacters = requireNonNull(spaceNormalizationCharacters);
		this.blockElements = requireNonNull(blockElements);
		this.preservedElements = requireNonNull(contentPreservedElements);
	}

	@Override
	public Characters getSpaceNormalizationCharacters() {
		return spaceNormalizationCharacters;
	}

	@Override
	public boolean isBlock(final Element element) {
		return blockElements.contains(NsName.ofNode(element));
	}

	@Override
	public boolean isPreserved(final Element element) {
		return preservedElements.contains(NsName.ofNode(element));
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
