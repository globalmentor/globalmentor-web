package com.garretwilson.text.xml;

import org.w3c.dom.*;

import static com.garretwilson.text.xml.XMLConstants.*;

/**A lightweight portion of an XML document.
@see XMLNode
@author Garret Wilson
*/
public class XMLDocumentFragment extends XMLNode implements DocumentFragment
{

	/**Default constructor for a document fragment. Since a document fragment has
		no owner document, its owner document will be set to <code>null</code>.
	*/
	public XMLDocumentFragment()
	{
		super(DOCUMENT_FRAGMENT_NODE, null);	//show that we have no owner document
		setNodeName(DOCUMENT_FRAGMENT_NODE_NAME);	//set the appropriate name for a document fragment
	}

	/**Function override to specify the types of nodes that can be child nodes.
	@return Whether or not the specified node can be added to the list of children.
	@see XMLNode#isNodeTypeAllowed
	*/
	protected boolean isNodeTypeAllowed(final Node node)  //G***make sure these are the right nodes for DocumentFragment to accept
	{
		switch(node.getNodeType())	//see which type of node this is
		{
			case ELEMENT_NODE:		//if we accept one of these types of child nodes
			case TEXT_NODE:
			case COMMENT_NODE:
			case PROCESSING_INSTRUCTION_NODE:
			case CDATA_SECTION_NODE:
			case ENTITY_REFERENCE_NODE:
				return true;	//show that we accept it
			default:	//if this is another type of node
				return false;	//show that we don't accept it
		}
	}

	/**Creates and returns a duplicate copy of this node. The clone has no parent.
	This function creates a "shallow" clone which does not contain clones of all
	child nodes. For the DOM version, see cloneNode().
	@return A duplicate copy of this node.
	@see XMLNode#cloneXMLNode
	@see XMLNode#cloneNode
	@see XMLNode#getParentXMLNode
	*/
	public Object clone()
	{
		return new XMLDocumentFragment();	//create a new document fragment G***this isn't right; we need to copy the child nodes, even if we don't clone them
	}

}
