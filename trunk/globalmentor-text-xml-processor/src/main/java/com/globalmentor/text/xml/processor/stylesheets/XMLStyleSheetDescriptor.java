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

package com.globalmentor.text.xml.processor.stylesheets;

/**
 * Class that encapsulates necessary information for referencing a stylesheet.
 * @author Garret Wilson
 * @deprecated
 */
public class XMLStyleSheetDescriptor {	//TODO fix media type, title, etc.

	/** The reference to the stylesheet's location. */
	private final String href;

	/** @return The reference to the stylesheet's location. */
	public final String getHRef() {
		return href;
	}

	/**
	 * Constructs a description of a stylesheet.
	 * @param newHRef The reference to the stylesheet's location.
	 */
	public XMLStyleSheetDescriptor(final String newHRef) {
		href = newHRef; //set the href
	}
}