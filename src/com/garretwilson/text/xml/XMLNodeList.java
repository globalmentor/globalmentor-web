package com.garretwilson.text.xml;

import java.util.Iterator;
import java.util.LinkedList;
import org.w3c.dom.Node;

/**A list of nodes in an XML document.
@see XMLNode
@see XMLNodeIterator
@see org.w3c.dom.Node
@see org.w3c.dom.NodeList
*/
public class XMLNodeList extends LinkedList implements org.w3c.dom.NodeList
{

//G***del	private XMLNodeList() {}	//G***testing

	/**The node which owns this list and all nodes in the list.*/
//G***del	private XMLNode ParentNode;

	/**Constructs a node list with the parent node specified.
	@param parentNode The node which owns this list and all nodes in the list.
	*/
/*G***del
	public XMLNodeList(XMLNode parentNode)
	{
		ParentNode=parentNode;	//set the parent node
	}
*/

	/**Creates and returns a duplicate copy of this node list with no values.
	@return A duplicate "shallow clone" copy of this node list.
	@see XMLNode#cloneXMLNode
	@see XMLNode#cloneNode
	@see XMLNodeList#cloneDeep
	@see Object#clone
	*/
	public Object clone()
	{
		return new XMLNodeList();	//create a new node list and return it
	}

	/**Creates and returns a duplicate copy of this node list containing clones of all its children.
	@return A duplicate "deep clone" copy of this node list.
	@see XMLNode#cloneXMLNode
	@see XMLNode#cloneNode
	@see XMLNodeList#clone
	*/
	public XMLNodeList cloneDeep()
	{
		final XMLNodeList clone=(XMLNodeList)clone();	//create a new node list
		for(int i=0; i<size(); ++i)	//look at each node in our list
			clone.add(((XMLNode)get(i)).cloneXMLNode(true));	//deep clone this node and store it in our node list clone
		return clone;	//return our cloned node list
	}

	 /**Appends the specified node to the end of this list, and sets the node's
		getParentNode
		 *
		 * @param o element to be appended to this list.
		 * @return <tt>true</tt> (as per the general contract of Collection.add).
		 */
//G***fix		public boolean add(Object o) {


	/**Returns the <code>index</code>th item in the collection. If
	<code>index</code> is greater than or equal to the number of nodes in
	the list, this returns <code>null</code>.
	@param index Index into the collection.
	@return The node at the <code>index</code>th position in the
	<code>NodeList</code>, or <code>null</code> if that is not a valid
	index.
	@version DOM Level 1
	*/
	public Node item(int index)
	{
		try
		{
			return (Node)get(index);	//return the object at the index
		}
		catch(IndexOutOfBoundsException e)	//if they don't give a valid index
		{
			return null;	//return null instead of throwing an exception
		}
	}

	/**Returns the number of nodes in the list.
	The range of valid child node indices is 0 to <code>length-1</code> inclusive.
	@return The number of nodes in the list.
	@version DOM Level 1
	*/
	public int getLength() {return size();}

}
