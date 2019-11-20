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

import static java.util.Objects.*;

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

	/**
	 * Constructor.
	 * @param spaceNormalizationCharacters The characters this profile considers "spaces" for the purpose of normalization.
	 * @param blockElements The elements considered block elements for purposes of formatting.
	 */
	public SimpleXmlFormatProfile(@Nonnull final Characters spaceNormalizationCharacters, @Nonnull final Set<NsName> blockElements) {
		this.spaceNormalizationCharacters = requireNonNull(spaceNormalizationCharacters);
		this.blockElements = requireNonNull(blockElements);
	}

	@Override
	public Characters getSpaceNormalizationCharacters() {
		return spaceNormalizationCharacters;
	}

	@Override
	public boolean isBlock(final Element element) {
		return blockElements.contains(NsName.ofNode(element));
	}

}
