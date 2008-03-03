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

import java.net.URI;
import java.util.*;

import com.garretwilson.net.AbstractNamespaceLabelManager;

/**Map managing namespace URIs and labels for XML.
<p>This class is not thread safe.</p>
@author Garret Wilson
*/
public class XMLNamespaceLabelManager extends AbstractNamespaceLabelManager
{

	/**Default constructor using a hash map.*/
	public XMLNamespaceLabelManager()
	{
		this(new HashMap<URI, String>());	//construct the class with a hash map
	}

	/**Map constructor.
	@param map The map this map should decorate.
	@exception NullPointerException if the provided map is <code>null</code>.
	*/
	public XMLNamespaceLabelManager(final Map<URI, String> map)
	{
		super(map);	//construct the parent class
	}

	/**Determines whether the given string is a valid label
	This version determines whether the string is valid XML name.
	@param string The string to check for being a label.
	@return <code>true</code> if the given string represents a valid label.
	@exception NullPointerException if the given string is <code>null</code>.
	@see XML#isName(String)
	*/
	protected boolean isLabel(final String string)
	{
		return XML.isName(string);	//return whether the given string is a valid XML name
	}

}
