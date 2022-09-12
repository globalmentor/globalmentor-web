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

package com.globalmentor.xml.dom.impl.stylesheets;

import java.util.ArrayList;

import org.w3c.dom.DOMException;

import com.globalmentor.xml.dom.impl.XMLDOMException;

/**
 * An ordered collection of media. An empty list is the same as a list that contains the medium <code>"all"</code>.
 * @see org.w3c.dom.stylesheets.MediaList
 * @deprecated
 */
@Deprecated
public class XMLMediaList extends ArrayList implements org.w3c.dom.stylesheets.MediaList {

	/**
	 * The parsable textual representation of the media list in comma-separated format.
	 */
	private String CssText = "";

	@Override
	//TODO change this to actually go through the list of media and return them as a string
	public String getMediaText() {
		return CssText;
	}

	@Override
	public void setMediaText(String cssText) throws DOMException {
		//TODO check the string for a syntax error
		//TODO check for read-only status
		//TODO fix		return CssText;	//return the text
	}

	@Override
	public int getLength() {
		return size();
	}

	@Override
	public String item(int index) {
		try {
			return (String)get(index); //return the object at the index
		} catch(IndexOutOfBoundsException e) { //if they don't give a valid index
			return null; //return null instead of throwing an exception
		}
	}

	@Override
	public void deleteMedium(String oldMedium) throws DOMException {
		//TODO check for read-only status here
		final int oldIndex = indexOf(oldMedium); //find the index of the medium
		if(oldIndex != -1) //if that medium exists in our list
			remove(oldIndex); //remove the medium
		else
			//if there is no such medium to remove
			throw new XMLDOMException(DOMException.NOT_FOUND_ERR, new Object[] {oldMedium}); //show that this media type wasn't found in our list, so we can't remove it
	}

	@Override
	public void appendMedium(String newMedium) throws DOMException {
		//TODO check for read-only status here
		final int oldIndex = indexOf(newMedium); //see if this medium already exists in our list
		if(oldIndex != -1) //if that medium exists in our list
			remove(oldIndex); //remove the medium from our list
		add(newMedium); //add the medium to our list, whether or not it existed before
	}

}
