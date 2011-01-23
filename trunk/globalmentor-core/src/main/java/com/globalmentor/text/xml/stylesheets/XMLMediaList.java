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

import java.util.ArrayList;

import com.globalmentor.text.xml.XMLDOMException;

import org.w3c.dom.DOMException;

/**An ordered collection of media. An empty list is the same as a list that
contains the medium <code>"all"</code>.
@see org.w3c.dom.stylesheets.MediaList
@deprecated
*/
public class XMLMediaList extends ArrayList implements org.w3c.dom.stylesheets.MediaList
{

	/**The parsable textual representation of the media list in comma-separated
	format.
	*/
	private String CssText="";

	/**Returns the parsable textual representation of the media list in comma-separated
	format.
	@return The parsable comma-separated textual representation of the media list.
	@version DOM Level 2
	@since DOM Level 2
	*/
	//TODO change this to actually go through the list of media and return them as a string
	public String getMediaText() {return CssText;}

	/**Sets the parsable textual representation of the media list.
	@param cssText The textual representation of the media list in comma-separated
		format.
	@exception DOMException
	<ul>
		<li>NO_MODIFICATION_ALLOWED_ERR: Raised if this media list is readonly.</li>
		<li>SYNTAX_ERR: Raised if the specified CSS string value has a syntax
	</ul>
	*/
	public void setMediaText(String cssText) throws DOMException
	{
		//TODO check the string for a syntax error
		//TODO check for read-only status
//TODO fix		return CssText;	//return the text
	}

	/**Returns the number of media in the list.
	The range of valid media list indices is 0 to <code>length-1</code> inclusive.
	@return The number of media in the list.
	@version DOM Level 2
	@since DOM Level 2
	*/
	public int getLength() {return size();}


	/**Returns the <code>index</code>th item in the list. If
	<code>index</code> is greater than or equal to the number of media in
	the list, this returns <code>null</code>.
	@param index Index into the list.
	@return The medium at the <code>index</code>th position in the
	list, or <code>null</code> if that is not a valid index.
	@version DOM Level 2
	@since DOM Level 2
	*/
	public String item(int index)
	{
		try
		{
			return (String)get(index);	//return the object at the index
		}
		catch(IndexOutOfBoundsException e)	//if they don't give a valid index
		{
			return null;	//return null instead of throwing an exception
		}
	}


	/**Deletes the medium indicated by <code>oldMedium</code> from the list.
	@param oldMedium The medium to delete in the media list.
	@exception DOMException
	<ul>
		<li>NO_MODIFICATION_ALLOWED_ERR: Raised if this list is readonly.</li>
		<li>NOT_FOUND_ERR: Raised if <code>oldMedium</code> is not in the list.</li>
	</ul>
	*/
	public void deleteMedium(String oldMedium) throws DOMException
	{
		//TODO check for read-only status here
		final int oldIndex=indexOf(oldMedium);	//find the index of the medium
		if(oldIndex!=-1)	//if that medium exists in our list
			remove(oldIndex);	//remove the medium
		else	//if there is no such medium to remove
			throw new XMLDOMException(DOMException.NOT_FOUND_ERR, new Object[]{oldMedium});	//show that this media type wasn't found in our list, so we can't remove it
	}

	/**Adds the medium <code>newMedium</code> to the end of the list. It the
	<code>newMedium</code> is already used, it is first removed.
	@param newMedium The new medium to add.
	@exception DOMException
	<ul>
		<li>NO_MODIFICATION_ALLOWED_ERR: Raised if this list is readonly.</li>
	</ul>
	*/
	public void appendMedium(String newMedium) throws DOMException
	{
		//TODO check for read-only status here
		final int oldIndex=indexOf(newMedium);	//see if this medium already exists in our list
		if(oldIndex!=-1)	//if that medium exists in our list
			remove(oldIndex);	//remove the medium from our list
		add(newMedium);	//add the medium to our list, whether or not it existed before
	}

}

