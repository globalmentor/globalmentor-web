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

package com.globalmentor.text.xml.stylesheets;

import java.util.LinkedList;

import com.globalmentor.text.xml.*;

import org.w3c.dom.stylesheets.*;

/**A list of stylesheets in an XML document.
@see XMLDocument
@see XMLStyleSheet
@see StyleSheet
@see StyleSheetList
@deprecated
*/
public class XMLStyleSheetList extends LinkedList implements org.w3c.dom.stylesheets.StyleSheetList
{
	/**Returns the number of stylesheets in the list.
	The range of valid stylesheet indices is 0 to <code>length-1</code> inclusive.
	@return The number of stylesheets in the list.
	@version DOM Level 2
	@since DOM Level 2
	*/
	public int getLength() {return size();}

	/**Returns the <code>index</code>th item in the list. If
	<code>index</code> is greater than or equal to the number of stylesheets in
	the list, this returns <code>null</code>.
	@param index Index into the list.
	@return The stylesheet at the <code>index</code>th position in the
	list, or <code>null</code> if that is not a valid index.
	@version DOM Level 2
	@since DOM Level 2
	*/
	public StyleSheet item(int index)
	{
		try
		{
			return (StyleSheet)get(index);	//return the object at the index
		}
		catch(IndexOutOfBoundsException e)	//if they don't give a valid index
		{
			return null;	//return null instead of throwing an exception
		}
	}
}

