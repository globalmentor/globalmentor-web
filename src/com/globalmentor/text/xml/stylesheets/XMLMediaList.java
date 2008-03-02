package com.globalmentor.text.xml.stylesheets;

//G***del import java.util.Iterator;
import java.util.ArrayList;
import org.w3c.dom.DOMException;
import org.w3c.dom.stylesheets.StyleSheet;

import com.globalmentor.text.xml.XMLDOMException;

/**An ordered collection of media. An empty list is the same as a list that
contains the medium <code>"all"</code>.
@see org.w3c.dom.stylesheets.MediaList
*/
public class XMLMediaList extends ArrayList implements org.w3c.dom.stylesheets.MediaList
{

	/**Creates and returns a duplicate copy of this node list with no values.
	@return A duplicate "shallow clone" copy of this node list.
	@see XMLNode#cloneXMLNode
	@see XMLNode#cloneNode
	@see XMLNodeList#cloneDeep
	@see Object#clone
	*/
/*G***del if we don't need
	public Object clone()
	{
		return new XMLNodeList();	//create a new node list and return it
	}
*/

	/**Creates and returns a duplicate copy of this node list containing clones of all its children.
	@return A duplicate "deep clone" copy of this node list.
	@see XMLNode#cloneXMLNode
	@see XMLNode#cloneNode
	@see XMLNodeList#clone
	*/
/*G***del if we don't ned
	public XMLNodeList cloneDeep()
	{
		final XMLNodeList clone=(XMLNodeList)clone();	//create a new node list
		for(int i=0; i<size(); ++i)	//look at each node in our list
			clone.add(((XMLNode)get(i)).cloneXMLNode(true));	//deep clone this node and store it in our node list clone
		return clone;	//return our cloned node list
	}

*/


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
	//G***change this to actually go through the list of media and return them as a string
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
		//G***check the string for a syntax error
		//G***check for read-only status
//G***fix		return CssText;	//return the text
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
		//G***check for read-only status here
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
		//G***check for read-only status here
		final int oldIndex=indexOf(newMedium);	//see if this medium already exists in our list
		if(oldIndex!=-1)	//if that medium exists in our list
			remove(oldIndex);	//remove the medium from our list
		add(newMedium);	//add the medium to our list, whether or not it existed before
	}

}

