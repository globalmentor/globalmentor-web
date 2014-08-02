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

package com.globalmentor.text.xml.traversal;

import java.util.*;
import org.w3c.dom.*;
import org.w3c.dom.traversal.*;

import com.globalmentor.text.xml.*;

/**An iterator that iterates of XML objects of a particular type contained in a
	collection of child objects.
@author Garret Wilson
@see Iterator
@see Collection
@see XMLNode
@deprecated
*/
public class XMLNodeIterator implements NodeIterator
{

	/**The root node of this iterator.*/
	private Node root;

		/**@return The root node of the iterator, as specified when it was created.*/
		public Node getRoot() {return root;}

		/**@return <code>true</code> if the iterator is invalid and is no longer
		  attached to a root.
		@see #getRoot
		*/
		public boolean isDetached() {return root==null;}

	/**The reference node used for iterating.*/
	private Node referenceNode=null;

		/**@return The reference node used for iterating.*/
		protected Node getReferenceNode() {return referenceNode;}

		/**Sets the reference node used for iterating.
		@param newReferenceNode The new node to be used as a position reference.
		*/
		protected void setReferenceNode(final Node newReferenceNode) {referenceNode=newReferenceNode;}

	/**Whether the iterator is before the reference node.*/
	private boolean beforeReferenceNode=true;

		/**@return Whether the iterator is before the reference node.
		@see #isAfterReferenceNode
		*/
		public boolean isBeforeReferenceNode() {return beforeReferenceNode;}

		/**@return Whether the iterator is after the reference node.
		@see #isBeforeReferenceNode
		*/
		public boolean isAfterReferenceNode() {return !beforeReferenceNode;}

		/**Sets whether the iterator is before the reference node.
		@param newBeforeReferenceNode Whether the iterator is now before the
			reference node.
		@see #setBeforeReferenceNode
		*/
		public void setBeforeReferenceNode(final boolean newBeforeReferenceNode) {beforeReferenceNode=newBeforeReferenceNode;}

		/**Sets whether the iterator is after the reference node.
		@param newAfterReferenceNode Whether the iterator is now after the
			reference node.
		@see #setAfterReferenceNode
		*/
		public void setAfterReferenceNode(final boolean newAfterReferenceNode) {beforeReferenceNode=!newAfterReferenceNode;}


	/**What should be shown. This defaults to everything <code>NodeFilter.SHOW_ALL</code>.*/
	private int whatToShow=NodeFilter.SHOW_ALL;

		/**Determines which node types are presented via the iterator. The available
		  set of constants is defined in the <code>NodeFilter</code> interface. Nodes
			not accepted by <code>whatToShow</code> will be skipped, but their children
			may still be considered. Note that this skip takes precedence over the filter,
			if any.
		@return The nodes which should be included in the iterator.
		@see NodeFilter
		*/
		public int getWhatToShow() {return whatToShow;}

	/**Creates a node iterator, specifying the root of the nodes over which to iterate.
	@param rootNode The root node over which to iterate.
	*/
	public XMLNodeIterator(final Node rootNode)
	{
		root=rootNode;  //set the root node
		setReferenceNode(rootNode); //set the root node as the reference node
		setBeforeReferenceNode(true); //set our iterator to be before our reference node
//TODO register as a listener of the root node
	}

	/**Creates a node iterator, specifying the root and the nodes to show and not
		expanding entity references.
	@param rootNode The root node over which to iterate.
	@param nodesToShow The nodes to be presented, as defined in <code>NodeFilter</code>.
	@see NodeFilter
	*/
	public XMLNodeIterator(final Node rootNode, final int nodesToShow)
	{
		this(rootNode, nodesToShow, null, false); //do the default construction
	}

	/**Creates a node iterator with an optional filter.
	@param rootNode The root node over which to iterate.
	@param nodesToShow The nodes to be presented, as defined in <code>NodeFilter</code>.
	@param filter The <code>NodeFilter</code> to be used with this
		<code>TreeWalker</code>, or <code>null</code> to indicate no filter.
	@param entityReferenceExpansion Whether entity reference nodes are expanded.
	@see NodeFilter
	*/
	public XMLNodeIterator(final Node rootNode, final int nodesToShow, final NodeFilter filter, final boolean entityReferenceExpansion)
	{
		this(rootNode); //do the default construction
		whatToShow=nodesToShow; //show which nodes to show
		nodeFilter=filter;  //set the node filter
		//TODO do something with entityReferenceExpansion here
	}

	/**The node filter used to screen nodes.*/
	private NodeFilter nodeFilter=null;	//TODO fix

		/**@return The node filter used to screen nodes.*/
		public NodeFilter getFilter() {return nodeFilter;}

		/**<p>The value of this flag determines whether the children of entity
		  reference nodes are visible to the iterator. If false, they and
		  their descendants will be rejected. Note that this rejection takes
		  precedence over <code>whatToShow</code> and the filter. Also note
		  that this is currently the only situation where
		  <code>NodeIterators</code> may reject a complete subtree rather than
		  skipping individual nodes.</p>
			<p>To produce a view of the document that has entity references
		  expanded and does not expose the entity reference node itself, use
		  the <code>whatToShow</code> flags to hide the entity reference node
		  and set <code>expandEntityReferences</code> to true when creating the
		  iterator. To produce a view of the document that has entity reference
		  nodes but no entity expansion, use the <code>whatToShow</code> flags
		  to show the entity reference node and set
		  <code>expandEntityReferences</code> to false.
		@return Whether entity reference should be expanded.
		*/
    public boolean getExpandEntityReferences() {return false;}  //TODO fix

	/**Returns the next node in the set and advances the position of the
		iterator in the set. After a <code>NodeIterator</code> is created,
		the first call to <code>nextNode()</code> returns the first node in
		the set. The reference node will be updated.
	@return The next <code>Node</code> in the set being iterated over, or
		<code>null</code> if there are no more members in that set.
	@throws DOMException
	<ul>
		<li>INVALID_STATE_ERR: Raised if this method is called after the
		  <code>detach</code> method was invoked.</li>
	</ul>
	*/
	public Node nextNode() throws DOMException
	{
		final Node oldReferenceNode=getReferenceNode();  //get the original reference node, in case we need to restore it
		final boolean wasBeforeReferenceNode=isBeforeReferenceNode(); //see if we're before the reference node
		Node node;  //we'll store the node here temporarily to make sure it should be included
		//get the next node, not checking to see if it should be included
		while((node=getNextNode())!=null) //while we haven't reached the last node
		{
			if(shouldShow(node, getWhatToShow()))  //if we should show this node TODO update for the filter, if present
				return node;  //return the node
		}
		setReferenceNode(oldReferenceNode); //restore the old reference node, since we can't find the next node
		setBeforeReferenceNode(wasBeforeReferenceNode); //restore which side of the node we were on
		return null;  //show that we can't find the previous node
	}

	/**Returns the previous node in the set and moves the position of the
		<code>NodeIterator</code> backwards in the set. The reference node will be
		updated.
	@return The previous <code>Node</code> in the set being iterated over,
		or <code>null</code> if there are no more members in that set.
	@throws DOMException
	<ul>
		<li>INVALID_STATE_ERR: Raised if this method is called after the
		  <code>detach</code> method was invoked.</li>
	</ul>
	*/
	public Node previousNode() throws DOMException
	{
		final Node oldReferenceNode=getReferenceNode();  //get the original reference node, in case we need to restore it
		final boolean wasAfterReferenceNode=isAfterReferenceNode(); //see if we're after the reference node
		Node node;  //we'll store the node here temporarily to make sure it should be included
		//get the previous node, not checking to see if it should be included
		while((node=getPreviousNode())!=null) //while we haven't reached the first node
		{
			if(shouldShow(node, getWhatToShow()))  //if we should show this node TODO update for the filter, if present
				return node;  //return the node
		}
		setReferenceNode(oldReferenceNode); //restore the old reference node, since we can't find the previous node
		setAfterReferenceNode(wasAfterReferenceNode); //restore which side of the node we were on
		return null;  //show that we can't find the previous node
	}

	/**Returns the next node in the set and advances the position of the
		iterator in the set. After a <code>NodeIterator</code> is created,
		the first call to <code>nextNode()</code> returns the first node in
		the set. This function returns the next node without checking it against
		the filter to see	if it should be included. The reference node will be updated.
	@return The next <code>Node</code> in the set being iterated over, or
		<code>null</code> if there are no more members in that set.
	@throws DOMException
	<ul>
		<li>INVALID_STATE_ERR: Raised if this method is called after the
		  <code>detach</code> method was invoked.</li>
	</ul>
	*/
	protected Node getNextNode() throws DOMException
	{
		if(isDetached())  //if this iterator has been detached
			throw new XMLDOMException(DOMException.INVALID_STATE_ERR, new Object[]{getClass().getName()});	//show that iterator is in an invalid state
		final Node oldReferenceNode=getReferenceNode();  //get the reference node
		if(isBeforeReferenceNode()) //if we're before the reference node
		{
			setAfterReferenceNode(true);  //show that our iterator is now after the reference node
		  return oldReferenceNode;  //return the reference node
		}
		else  //if we're after the reference node
		{
			if(oldReferenceNode.hasChildNodes())  //if the reference node has children
			{
				final Node newReferenceNode=oldReferenceNode.getFirstChild(); //the first child will be the new reference node
				setReferenceNode(newReferenceNode); //set the new reference node
				return newReferenceNode;  //return the new reference node
			}
			else if(oldReferenceNode!=getRoot())  //if the reference node has no children (and it's not a childless root), try to get the next sibling
			{
			  Node currentReferenceNode=oldReferenceNode; //we'll be changing the reference node, and we'll use this as a temporary reference node
			  Node newReferenceNode=currentReferenceNode.getNextSibling(); //look for the next sibling
				while(newReferenceNode==null) //while we haven't found a new reference node
				{
					currentReferenceNode=currentReferenceNode.getParentNode(); //try to find this node's parent
						//if this node has a parent, and it's not the root of our iterator
				  if(currentReferenceNode!=null && currentReferenceNode!=getRoot())
					{
					  newReferenceNode=currentReferenceNode.getNextSibling(); //look for the next sibling
					}
					else  //if we couldn't find a parent, or we found the root, we're at the end of the iterator
						break;
				}
				if(newReferenceNode!=null)  //if we found a new reference node
					setReferenceNode(newReferenceNode); //update the reference node
				return newReferenceNode;  //return the new reference node, which may be a valid value, or null if no more nodes are left
			}
			else  //if we tried to go past a childless root
				return null;  //show that there are no more nodes left
		}
	}

	/**Returns the previous node in the set and moves the position of the
		<code>NodeIterator</code> backwards in the set. This function returns the
		previous node without checking it against 	the filter to see	if it should
		be included. The reference node will be updated.
	@return The previous <code>Node</code> in the set being iterated over,
		or <code>null</code> if there are no more members in that set.
	@throws DOMException
	<ul>
		<li>INVALID_STATE_ERR: Raised if this method is called after the
		  <code>detach</code> method was invoked.</li>
	</ul>
	*/
	protected Node getPreviousNode() throws DOMException
	{
		if(isDetached())  //if this iterator has been detached
			throw new XMLDOMException(DOMException.INVALID_STATE_ERR, new Object[]{getClass().getName()});	//show that iterator is in an invalid state
		final Node oldReferenceNode=getReferenceNode();  //get the reference node
		if(isAfterReferenceNode()) //if we're after the reference node
		{
			setBeforeReferenceNode(true);  //show that our iterator is now before the reference node
		  return oldReferenceNode;  //return the reference node
		}
		else  //if we're before the reference node
		{
			Node newReferenceNode=null;  //assume there is no new reference node
			if(oldReferenceNode!=getRoot())  //if we aren't already at the root
			{
			  newReferenceNode=oldReferenceNode.getPreviousSibling(); //look for the previous sibling
			  if(newReferenceNode!=null)  //if there is a previous sibling
				{
					if(newReferenceNode.hasChildNodes())  //if the reference node has children
					{
						newReferenceNode=newReferenceNode.getLastChild(); //the last child will be the new reference node
					}
					//otherwise, if the previous sibling has no children, we'll accept that node as the previous node
				}
				else  //if there is no previous sibling
				{
					newReferenceNode=oldReferenceNode.getParentNode();  //get the parent of this node
				}
			}
			if(newReferenceNode!=null)  //if we found a new reference node
				setReferenceNode(newReferenceNode); //update the reference node
			return newReferenceNode;  //return the previous node, represented by our local new reference node, which may be null if there are no previous nodes
		}
	}

	/**The list (derived from the collection) which contains the objects to iterate through.*/
//TODO fix or del	private List ChildList;

	/**The type of node to iterate through in the iteration (all others will be ignored).*/
//TODO fix or del	private short NodeType;

	/**The current child we're looking at.*/
//TODO fix or del	private int ChildIndex;

	/**Constructor that specifies the children which contain the objects, along with the type of object to iterate through.
	@param childCollection The collection of children which contains the objects to iterate through.
	@param nodeType The type of node to iterate through in the iteration (all others will be ignored).
	@see XMLNode
	*/
/*TODO fix or del
	public XMLNodeIterator(final Collection childCollection, final short nodeType)
	{
		ChildList=new ArrayList(childCollection);	//create an array list with the children in the collection
		NodeType=nodeType;	//store the node type
		ChildIndex=0;	//start searching at the beginning of the vector
	}
*/

	/**@return true if there are more child objects of the correct type.
  @see Iterator.hasNext
  */
/*TODO fix
	public boolean hasNext()
	{
		for(int i=ChildIndex; i<ChildList.size(); ++i)	//look at the child objects
		{
			if(((XMLNode)ChildList.get(i)).getNodeType()==NodeType)	//if this node has the correct type
				return true;	//show that there are more elments
		}
		return false;	//show that we couldn't find another object of the correct type
	}
*/

	/**@return The next child object of the correct type available.
  @throw NoSuchElementException Thrown if no more child objects are left.
  @see Iterator.next
  */
/*TODO fix or del
	public Object next()
	{
		for(; ChildIndex<ChildList.size(); ++ChildIndex)	//look at the child object
		{
			XMLNode childNode=(XMLNode)ChildList.get(ChildIndex);	//look at this object
			if(childNode.getNodeType()==NodeType)	//if this node has the correct type
			{
				ChildIndex++;	//show that we'll start looking at the next child next time
				return childNode;	//return this child node
			}
		}
		throw new NoSuchElementException();	//it's an error if we couldn't find another object of the correct type
	}
*/

	/**Removing children is not currently allowed by XMLNodeIterator, and will always throw an exception.
  @throw UnsupportedOperationException Always thrown, because this operation is currently not supported.
  @see Iterator.remove
  */
/*TODO fix or del
  public void remove()
  {
		throw new UnsupportedOperationException();	//show that we currently don't support this operation
  }
*/


	/**Detaches the <code>NodeIterator</code> from the set which it iterated
		over, releasing any computational resources and placing the iterator
		in the <code>INVALID</code> state. After <code>detach</code> has been invoked,
		calls to <code>nextNode</code> or <code>previousNode</code> will
		raise the exception <code>INVALID_STATE_ERR</code>.
	@version DOM Level 2
	*/
	public void detach()
	{
		//TODO remove ourselves as a listener from the root node
		referenceNode=null; //set the reference node to null
		root=null;  //show that we don't have a root anymore
		nodeFilter=null;  //get rid of the node filter
	}

	/**Returns whether a particular node meets the given criterion of what to show.
	@param node The node to test.
	@param whatToShow The nodes to be presented, as defined in <code>NodeFilter</code>.
	@see NodeFilter
	*/
	protected static boolean shouldShow(final Node node, final int whatToShow)
	{
		final short nodeType=node.getNodeType();  //see which type of node this is
		switch(whatToShow)  //see which nodes should be shown
		{
			case NodeFilter.SHOW_ALL: //if all nodes should be shown
				return true;
			case NodeFilter.SHOW_ATTRIBUTE:
				return nodeType==Node.ATTRIBUTE_NODE;
			case NodeFilter.SHOW_CDATA_SECTION:
				return nodeType==Node.CDATA_SECTION_NODE;
			case NodeFilter.SHOW_COMMENT:
				return nodeType==Node.COMMENT_NODE;
			case NodeFilter.SHOW_DOCUMENT:
				return nodeType==Node.DOCUMENT_NODE;
			case NodeFilter.SHOW_DOCUMENT_FRAGMENT:
				return nodeType==Node.DOCUMENT_FRAGMENT_NODE;
			case NodeFilter.SHOW_DOCUMENT_TYPE:
				return nodeType==Node.DOCUMENT_TYPE_NODE;
			case NodeFilter.SHOW_ELEMENT:
				return nodeType==Node.ELEMENT_NODE;
			case NodeFilter.SHOW_ENTITY:
				return nodeType==Node.ENTITY_NODE;
			case NodeFilter.SHOW_ENTITY_REFERENCE:
				return nodeType==Node.ENTITY_REFERENCE_NODE;
			case NodeFilter.SHOW_NOTATION:
				return nodeType==Node.NOTATION_NODE;
			case NodeFilter.SHOW_PROCESSING_INSTRUCTION:
				return nodeType==Node.PROCESSING_INSTRUCTION_NODE;
			case NodeFilter.SHOW_TEXT:
				return nodeType==Node.TEXT_NODE;
			default:  //if we don't recognize the type of node to show
				return false; //show that we shouldn't include the node
		}
	}

}
