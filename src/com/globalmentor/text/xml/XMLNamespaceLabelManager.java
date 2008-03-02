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
	@see XMLUtilities#isName(String)
	*/
	protected boolean isLabel(final String string)
	{
		return XMLUtilities.isName(string);	//return whether the given string is a valid XML name
	}

}
