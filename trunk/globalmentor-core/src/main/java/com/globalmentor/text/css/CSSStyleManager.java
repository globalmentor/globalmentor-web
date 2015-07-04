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

package com.globalmentor.text.css;

import org.w3c.dom.Element;
import org.w3c.dom.css.CSSStyleDeclaration;

/**
 * An object that can associate CSS styles with XML elements.
 * @author Garret Wilson
 */
public interface CSSStyleManager {

	/**
	 * Retrieves the style of the given element.
	 * @param element The element for which a style should be returned.
	 * @return A style for the given element, or <code>null</code> if the element has no style assigned to it.
	 */
	public CSSStyleDeclaration getStyle(final Element element);

	/**
	 * Sets the style of an element, replacing any existing style stored for the element.
	 * @param element The element for which a style is being specified.
	 * @param style The element style.
	 */
	public void setStyle(final Element element, final CSSStyleDeclaration style);

}