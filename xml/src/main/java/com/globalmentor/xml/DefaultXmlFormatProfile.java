/*
 * Copyright © 2019 GlobalMentor, Inc. <https://www.globalmentor.com/>
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

import com.globalmentor.xml.def.*;

/**
 * The XML format profile for the XML to serializer to use by default.
 * @apiNote This class cannot be instantiated directly, as the singleton {@link #INSTANCE} is already provided. However the class may be subclassed to override
 *          and customize behavior.
 * @implSpec This profile which uses {@link XML#WHITESPACE_CHARACTERS} as space normalization characters, and considers all elements block elements with no
 *           flush elements.
 * @author Garret Wilson
 */
public class DefaultXmlFormatProfile extends BaseXmlFormatProfile {

	/** Shared singleton instance. */
	public static final DefaultXmlFormatProfile INSTANCE = new DefaultXmlFormatProfile();

	/** Constructor. */
	protected DefaultXmlFormatProfile() {
	}

	/**
	 * {@inheritDoc}
	 * @implSpec This version returns <code>true</code> for all elements.
	 */
	@Override
	protected boolean isBlock(final NsName element) {
		return true;
	}

	/**
	 * {@inheritDoc}
	 * @implSpec This version always returns <code>false</code>; no elements are recognized as break elements.
	 */
	@Override
	protected boolean isBreak(final NsName element) {
		return false;
	}

	/**
	 * {@inheritDoc}
	 * @implSpec This version always returns <code>false</code>; no elements are recognized as flush.
	 */
	@Override
	protected boolean isFlush(final NsName element) {
		return false;
	}

}
