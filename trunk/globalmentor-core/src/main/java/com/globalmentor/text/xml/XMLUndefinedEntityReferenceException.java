/*
 * Copyright © 1996-2008 GlobalMentor, Inc. <http://www.globalmentor.com/>
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

package com.globalmentor.text.xml;

/**
 * Undefined entity reference error.
 * @author Garret Wilson
 * @see XMLParseException
 * @deprecated
 */
public class XMLUndefinedEntityReferenceException extends XMLParseException {

	//TODO comment these
	/**
	 * @param sourceName The name of the source of the XML document (perhaps a filename).
	 */
	public XMLUndefinedEntityReferenceException(final String entityReference, final long lineIndex, final long charIndex, final String sourceName) {
		super("XML undefined entity reference: \"" + entityReference + "\".", lineIndex, charIndex, sourceName);
	}
}
