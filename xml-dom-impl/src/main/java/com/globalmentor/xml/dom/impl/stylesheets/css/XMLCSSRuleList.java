/*
 * Copyright © 1996-2008 GlobalMentor, Inc. <https://www.globalmentor.com/>
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

package com.globalmentor.xml.dom.impl.stylesheets.css;

import java.util.ArrayList;

import org.w3c.dom.css.CSSRule;

import com.globalmentor.xml.dom.impl.XMLDocument;

/**
 * A list of CSS rules.
 * @author Garret Wilson
 * @see XMLDocument
 * @see CSSRule
 * @deprecated
 */
@Deprecated
public class XMLCSSRuleList extends ArrayList implements org.w3c.dom.css.CSSRuleList {

	@Override
	public int getLength() {
		return size();
	}

	@Override
	public CSSRule item(int index) {
		try {
			return (CSSRule)get(index); //return the object at the index
		} catch(IndexOutOfBoundsException e) { //if they don't give a valid index
			return null; //return null instead of throwing an exception
		}
	}

}
