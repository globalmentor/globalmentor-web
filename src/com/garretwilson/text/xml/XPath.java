package com.garretwilson.text.xml;

import java.util.StringTokenizer;
import java.util.ArrayList;
import org.w3c.dom.*;
import com.garretwilson.lang.StringUtilities;
import com.garretwilson.util.Debug;

/**Parses XPath expressions and performs XPath operations on an XML document.
The current implementation only interprets location paths.
<p>Example usages include:</p>
<ul>
	<li><code>/html/body/*</code> Selects all element children of <code>body</code>.</li>
	<li><code>/html/body/img/@src</code> Selects the <code>src</code> attribute
		of the first <code>img</code> element in <code>body</code>.</li>
</li>
@see http://www.w3.org/TR/xpath
@author Garret Wilson
*/
public class XPath
{
	/**The character used to separate a location path into location steps.*/
	public final static char LOCATION_STEP_SEPARATOR_CHAR='/';
	/**The character used as a wildcard matching character.*/
	public final static char WILDCARD_CHAR='*';
	/**The string form of the wildcard matching character.*/
	public final static String WILDCARD_STRING=String.valueOf(WILDCARD_CHAR);
	/**The string used to separate an axis from a node test in a location step.*/
	public final static String AXIS_SEPARATOR_STRING="::";
	public final static char LEFT_PARENTHESIS='(';
	public final static char RIGHT_PARENTHESIS=')';
		//axes
	/**<code>ROOT</code> is not defined as an axis in XPath, but here represents
		the root of the document.*/
	public final static String ROOT=String.valueOf(LOCATION_STEP_SEPARATOR_CHAR);
	public final static String ANCESTOR="ancestor";
	public final static String ANCESTOR_OR_SELF="ancestor-or-self";
	public final static String ATTRIBUTE="attribute";
	public final static String CHILD="child";
	public final static String DESCENDANT="descendant";
	public final static String DESCENDANT_OR_SELF="descendant-or-self";
	public final static String FOLLOWING="following";
	public final static String FOLLOWING_SIBLING="following-sibling";
	public final static String NAMESPACE="namespace";
	public final static String PARENT="parent";
	public final static String PRECEDING="preceding";
	public final static String PRECEDING_SIBLING="preceding-sibling";
	public final static String SELF="self";
		//node tests
			//node types
	public final static String COMMENT="comment";
	public final static String TEXT="text";
	public final static String PROCESSING_INSTRUCTION="processing-instruction";
	public final static String NODE="node";
	public final static String COMMENT_NODE_TEST=COMMENT+LEFT_PARENTHESIS+RIGHT_PARENTHESIS;
	public final static String TEXT_NODE_TEST=TEXT+LEFT_PARENTHESIS+RIGHT_PARENTHESIS;
	public final static String PROCESSING_INSTRUCTION_NODE_TEST=PROCESSING_INSTRUCTION+LEFT_PARENTHESIS+RIGHT_PARENTHESIS;
	public final static String NODE_NODE_TEST=NODE+LEFT_PARENTHESIS+RIGHT_PARENTHESIS;

	/**The abbreviation for self::node().*/
	public final static String SELF_NODE_ABBREVIATION=".";
	/**The abbreviation for parent::node().*/
	public final static String PARENT_NODE_ABBREVIATION="..";
	/**The abbreviation for attribute::.*/
	public final static String ATTRIBUTE_ABBREVIATION="@";

  public XPath()
  {
  }

	/**Convenience function to retrieve a node from the root element of the
		specified document, based on the specified XPath expression. If multiple
		nodes match the expression, the first one will be returned. If none match,
		<code>null</code> will be returned.
	@param node The document on which the expression should be evaluated.
	@param expression The XPath expression to evaluate.
	@return The first node matching the XPath expression, or <code>null</code> if
		there is no match.
	*/
	public static Node getNode(final Document document, final String expression)
	{
		return getNode(document.getDocumentElement(), expression);  //apply the expression to the document's root element
	}

	/**Convenience function to retrieve a node from the specified node, based on
		the specified XPath expression. If multiple nodes match the expression, the
		first one will be returned. If none match, <code>null</code> will be
		returned.
	@param node The document node on which the expression should be evaluated.
	@param expression The XPath expression to evaluate.
	@return The first node matching the XPath expression, or <code>null</code> if
		there is no match.
	*/
	public static Node getNode(final Node node, final String expression)
	{
		final NodeList nodeList=(NodeList)evaluateLocationPath(node, expression); //get the list of matching nodes
		return nodeList.getLength()>0 ? nodeList.item(0) : null;  //return the first node in the list, or null if there are no matches
		//G***catch a class cast exception and throw another exception that's more descriptive
	}

	/**Evaluates an XPath location path and returns the appropriate result of the evaluation.
	@param node The document node on which the expression should be evaluated.
	@param expression The XPath expression to evaluate.
	@return <code>org.w3c.dom.NodeList</code>, <code>java.lang.Boolean</code>,
		<code>java.lang.Float</code>, or <code>java.lang.String</code> based upon the
		result of the evaluation.
	#see org.w3c.dom.Node
	#see org.w3c.dom.NodeList
	*/
	public static Object evaluateLocationPath(final Node node, final String expression)
	{
//G***del Debug.trace("evaluating expression: ", expression); //G***del
//G***fix, comment Debug.assert(node!=0);
		final LocationStep[] locationStepArray=parseLocationPath(expression);	//G***testing
/*G***del
		if(Debug.isDebug())	//G***del debug
		{
			for(int i=0; i<locationStepArray.length; ++i)	//G***del debug
				Debug.trace(locationStepArray[i].toString());	//G***del debug
		}
*/
		return evaluateLocationPath(node, locationStepArray, 0);	//evaluate the array of location steps and return the list of nodes we receive
	}

	/**Evaluates an XPath location path and returns the appropriate result of the evaluation.
	@param node The document node on which the expression should be evaluated.
	@param locationStepArray An array of location steps that represent the location path.
	@param locationStepIndex The current index of the location step being evaluated.
	@return <code>org.w3c.dom.NodeList</code>, <code>java.lang.Boolean</code>,
		<code>java.lang.Float</code>, or <code>java.lang.String</code> based upon the
		result of the evaluation.
	#see org.w3c.dom.Node
	#see org.w3c.dom.NodeList
	*/
	protected static XMLNodeList evaluateLocationPath(final Node node, final LocationStep[] locationStepArray, final int locationStepIndex)
	{
//G***del Debug.trace("XPath.evaluateLocationpath()");
		final XMLNodeList nodeList=new XMLNodeList();	//we'll keep everything that matches in this list
		final LocationStep locationStep=locationStepArray[locationStepIndex];	//get the location step we're currently evaluating
		final String nodeTest=locationStep.getNodeTest();	//get a reference to the node test
//G***del Debug.trace("nodeTest: "+nodeTest);
		if(locationStep.getAxis().equals(ROOT))	//if this node indicates we should start from the root
			nodeList.add(node.getOwnerDocument().getDocumentElement());	//add the document root to our list of matching objects
		else	//if this isn't the root location step
		{
//G***del			final ArrayList searchObjectList=new ArrayList();	//we'll keep everything that we'll search (but we don't know yet if matches) in this list
				//if this location step should include this node
			if(locationStep.getAxis().equals(SELF) || locationStep.getAxis().equals(ANCESTOR_OR_SELF) || locationStep.getAxis().equals(DESCENDANT_OR_SELF))
			{
				if(isMatch(node, nodeTest, node.getNodeType()==node.ATTRIBUTE_NODE ? Node.ATTRIBUTE_NODE : Node.ELEMENT_NODE))	//if this node matches (specifying that, if a wildcard is present, attributes should be picked only if this is an attribute; otherwise, elements should be picked for wildcards)
					nodeList.add(node);	//add this node to the list
			}
			if(locationStep.getAxis().equals(PARENT))	//if we should search the parent
				nodeList.addAll(getMatchingParent(node, nodeTest, false));	//add the matching parent to our list
			else if(locationStep.getAxis().equals(ANCESTOR) || locationStep.getAxis().equals(ANCESTOR_OR_SELF))	//if we should search all ancestors
				nodeList.addAll(getMatchingParent(node, nodeTest, true));	//add all matching ancestors to our list
			else if(locationStep.getAxis().equals(CHILD))	//if we should search children
				nodeList.addAll(getMatchingChildren(node, nodeTest, false));	//add all matching children to our list
			else if(locationStep.getAxis().equals(DESCENDANT) || locationStep.getAxis().equals(DESCENDANT_OR_SELF))	//if we should search all descendants
				nodeList.addAll(getMatchingChildren(node, nodeTest, true));	//add all matching descendants to our list
			else if(locationStep.getAxis().equals(ATTRIBUTE))	//if we should search attributes
				nodeList.addAll(getMatchingNodes(node.getAttributes(), nodeTest, false, Node.ATTRIBUTE_NODE));	//add all matching attributes to our list (indicating that we shouldn't search child nodes, since attributes have no child nodes)
		}
/*G***fix
	public final static String FOLLOWING="following";
	public final static String FOLLOWING_SIBLING="following-sibling";
	public final static String NAMESPACE="namespace";
	public final static String PRECEDING="preceding";
	public final static String PRECEDING_SIBLING="preceding-sibling";
*/
		if(locationStepIndex<locationStepArray.length-1)	//if we haven't reached the end of the location path
		{
			final XMLNodeList resultNodeList=new XMLNodeList();	//make a list of nodes to store our results
			for(int nodeIndex=0; nodeIndex<nodeList.getLength(); ++nodeIndex)	//look at each node that matched this step in the path
			{
				final Node locationStepNode=nodeList.item(nodeIndex);	//get a reference to this node
				resultNodeList.addAll(evaluateLocationPath(locationStepNode, locationStepArray, locationStepIndex+1));	//evaluate the next step in the path using this node, and add the results to the results we have so far
			}
			return resultNodeList;	//return the results we found
		}
		else	//if we've reached the end of our location path, the nodes we've gathered so far are our results
			return nodeList;	//return the list we filled with nodes
	}

	/**Finds all matching children of a particular node.
	@param node The node whose children should be checked.
	@param nodeTest The node test (usually from a location step) against which the nodes should be compared.
	@param includeDescendants Whether all descendants (children of children) should be included in the search
	@return A list of nodes which meet the criteria of <code>nodeTest</code>.
	*/
	protected static XMLNodeList getMatchingChildren(final Node node, final String nodeTest, final boolean includeDescendants)
	{
//G***del Debug.trace("XPath.getMatchingChildren() nodeTest: "+nodeTest+" includeDescendants: "+includeDescendants);
		final XMLNodeList nodeList=new XMLNodeList();	//create a new node list to return
//G***del Debug.trace("looking at children: ", node.getChildNodes().getLength()); //G***del
		for(int childIndex=0; childIndex<node.getChildNodes().getLength(); childIndex++)	//look at each child node
		{
//G***del Debug.trace("looking at child: ", childIndex); //G***del
			final Node childNode=node.getChildNodes().item(childIndex);	//get a reference to this node
//G***del Debug.trace("looking at child: ", childNode); //G***del
			if(isMatch(childNode, nodeTest, Node.ELEMENT_NODE))	//if this child node matches (specifying that, if a wildcard is present, only elements should be chosen)
				nodeList.add(childNode);	//add this child node to the list
			if(includeDescendants)	//if each of the children should check for matching tags as well
				nodeList.addAll(getMatchingChildren(childNode, nodeTest, includeDescendants));	//check all the descendants of this child as well
		}
		return nodeList;	//return the list we created and filled
	}

	/**Finds the matching parent(s) of a particular node.
	@param node The node whose parent(s) should be checked.
	@param nodeTest The node test (usually from a location step) against which the nodes should be compared.
	@param includeAncesrots Whether all ancestors (parents of parents, etc.) should be included in the search
	@return A list of nodes which meet the criteria of <code>nodeTest</code>.
	*/
	protected static XMLNodeList getMatchingParent(final Node node, final String nodeTest, final boolean includeAncestors)
	{
		final XMLNodeList nodeList=new XMLNodeList();	//create a new node list to return
		final Node parentNode=node.getParentNode();	//get a reference to this node's parent
		if(parentNode!=null)	//if this node has a parent
		{
			if(isMatch(parentNode, nodeTest, Node.ELEMENT_NODE))	//if the parent node matches (specifying that, if a wildcard is present, only elements should be chosen)
				nodeList.add(parentNode);	//add the parent node to the list
			if(includeAncestors)	//if the parent should check its parents for matching tags as well
				nodeList.addAll(getMatchingParent(parentNode, nodeTest, includeAncestors));	//check all the parents of this parent as well
		}
		return nodeList;	//return the list we created and filled
	}

	/**Finds all matching nodes in a named node map.
	@param namedNodeMap The map of nodes whose children should be checked.
	@param nodeTest The node test (usually from a location step) against which
		the nodes should be compared.
	@param includeDescendants Whether this node's descendants should be included
		in the search
	@param wildcardType The type of node (a <code>Node</code> constant) which
		should be returned if <code>nodeTest</code> is a wildcard.
	@return A list of nodes which meet the criteria of <code>nodeTest</code>.
	*/
	protected static XMLNodeList getMatchingNodes(final NamedNodeMap namedNodeMap, final String nodeTest, final boolean includeDescendants, final short wildcardType)
	{
		final XMLNodeList resultNodeList=new XMLNodeList();	//create a new node list to return
		for(int nodeIndex=0; nodeIndex<namedNodeMap.getLength(); nodeIndex++)	//look at each node in the list
		{
			final Node node=namedNodeMap.item(nodeIndex);	//get a reference to this node
			if(isMatch(node, nodeTest, wildcardType))	//if this node matches
				resultNodeList.add(node);	//add this node to the list
		}
		return resultNodeList;	//return the list we created and filled
	}

	/**Checks to see if a particular node matches the criteria of a location step.
	@param The node to check.
	@param nodeTest The node test (usually from a location step) against which the
		node should be compared.
	@param wildcardType The type of node (a <code>Node</code> constant) which
		should be returned if <code>nodeTest</code> is a wildcard.
	@return <code>true</code> if the node meets the correct criteria of the
		locationStep, else <code>false</code>.
	*/
	protected static boolean isMatch(final Node node, final String nodeTest, final short wildcardType)
	{
Debug.trace("XPath.isMatch() node: "+node.getNodeName()+" nodeTest: "+nodeTest);
		if(nodeTest.equals(WILDCARD_STRING))	//if this is a test for a wildcard
		{
			return node.getNodeType()==wildcardType;  //return whether or not the node is of the correct type for a wildcard match
/*G***del
		  if(attributeWildcard) //if this wildcard is for attributes
				return node.getNodeType()==ATTRIBUTE_NODE;  //return whether or not this is an attribute
			else  //if this wildcard is not for an attribute, it is for an element
				return node.getNodeType()==ELEMENT_NODE;  //return whether this is an element
*/
		}
		if(nodeTest.equals(NODE_NODE_TEST))	//if this is a test for "node()"
			return true;	//every node matches this test
		if(nodeTest.equals(TEXT_NODE_TEST))	//if the test is for a text node
			return node.getNodeType()==Node.TEXT_NODE;	//show whether this is a text node
		if(nodeTest.equals(COMMENT_NODE_TEST))	//if the test is for a comment node
			return node.getNodeType()==Node.COMMENT_NODE;	//return whether this is a comment node
		if(nodeTest.equals(PROCESSING_INSTRUCTION_NODE_TEST))
			return node.getNodeType()==Node.PROCESSING_INSTRUCTION_NODE;	//return whether this is a processing instruction node
		//G***check for arguments to the processing instruction
		//if this isn't one of the specific tests, this is just a simple name test
		return node.getNodeName().equals(nodeTest);	//return whether or not the name matches the node test
	}

	/**Converts a string representing a location path into an array of location step objects.
	A special <code>LocationStep</code> with the axis of "/" as the first
		location step indicates an absolute path from the document root.
	@param locationPath The string representation of a location path.
	@return An array of objects of type <code>LocationStep</code>.
	*/
	protected static LocationStep[] parseLocationPath(final String locationPath)
	{
		final ArrayList locationStepList=new ArrayList();	//create an array to hold the location steps we find
		final StringTokenizer locationPathStringTokenizer=new StringTokenizer(locationPath, String.valueOf(LOCATION_STEP_SEPARATOR_CHAR), true);	//create a string tokenizer to parse the location path
		boolean expectingToken=true;	//show that we're expecting a token first of all
		while(locationPathStringTokenizer.hasMoreTokens())	//while there are more location steps on this line
		{
			String locationStepString=locationPathStringTokenizer.nextToken();	//get the next field value
			if(locationStepString.equals(String.valueOf(LOCATION_STEP_SEPARATOR_CHAR)))	//if this is a location step separator
			{
				if(expectingToken)	//if we were expecting a token
					locationStepString="";	//show that there was a blank token; since we just received a delimiter, we're still expecting another token
				else	//if we were expecting this delimiter (that is, we weren't expecting a token)
				{
					expectingToken=true;	//show that we're now expecting a token, since we just received a delimiter
					continue;	//don't process the delimiter that we expected
				}
			}
			else	//if this is a token
				expectingToken=false;	//show that we're no longer expecting a token, since we just received one
			if(locationStepString.length()==0)	//if this is a blank location step
			{
				if(locationStepList.size()==0)	//if this is the first step, a blank location step means the root element
				{
					locationStepList.add(new LocationStep(ROOT, ""));	//add a special type of location step representing the root
					continue;	//continue processing other location steps
//G***del						locationStepString=ROOT;	//show that this is a special location step indicating the root node
				}
				else	//if this isn't the first location step, blank location steps are abbreviated forms of "descendant-or-self::node()"
					locationStepString=DESCENDANT_OR_SELF+AXIS_SEPARATOR_STRING+NODE_NODE_TEST;
			}
			else if(locationStepString.equals(SELF_NODE_ABBREVIATION))	//if this is an abbreviation for self::node()
				locationStepString=SELF+AXIS_SEPARATOR_STRING+NODE_NODE_TEST;
			else if(locationStepString.equals(PARENT_NODE_ABBREVIATION))	//if this is an abbreviation for parent::node()
				locationStepString=PARENT+AXIS_SEPARATOR_STRING+NODE_NODE_TEST;
					//replace every occurrence of "@" with "attribute::" as per the XPath spec
			locationStepString=StringUtilities.replace(locationStepString, ATTRIBUTE_ABBREVIATION, ATTRIBUTE+AXIS_SEPARATOR_STRING);
			int axisSeparatorIndex=locationStepString.indexOf(AXIS_SEPARATOR_STRING);	//get the index of "::"
			if(axisSeparatorIndex==-1)	//if there is no axis separator, prepend "child::"
			{
				locationStepString=CHILD+AXIS_SEPARATOR_STRING+locationStepString;	//prepend "child::"
				axisSeparatorIndex=CHILD.length();	//we know exactly where the axis separator is now, since we put it there
			}
			final String axis=locationStepString.substring(0, axisSeparatorIndex).trim();	//extract the axis from the string and trim it
			final String nodeTest=locationStepString.substring(axisSeparatorIndex+AXIS_SEPARATOR_STRING.length()).trim();	//extract the node test from the string and trim it
			final LocationStep locationStep=new LocationStep(axis, nodeTest);	//create a location step
			locationStepList.add(locationStep);	//add this location step to our array
		}
		return (LocationStep[])locationStepList.toArray(new LocationStep[]{});	//convert the list to an array and return it
	}

	/**Represents a step in an XPath location path.*/
	protected static class LocationStep	//G***should this really be declared static?
	{
		/**The tree relationship between this step and the context node.*/
		private String axis;

			/**@return The axis of the location step.*/
			public String getAxis() {return axis;}

			/**Sets the axis of the location step.
			@param newAxis The relationship between this step and the context node.
			*/
			public void setAxis(final String newAxis) {axis=newAxis;}

		/**The node type selected by the location step.*/
		private String nodeTest;

			/**@return The node type selected by the location step.*/
			public String getNodeTest() {return nodeTest;}

			/**Sets the node test of this location step.
			@param newNodeTest The node type selected by the location step.
			*/
			public void setNodeTest(final String newNodeTest) {nodeTest=newNodeTest;}

		/**Constructs a new location step with the specified axis and node test.
		@param newAxis The relationship between this step and the context node.
		@param newNodeTest The node type selected by the location step.
		*/
		public LocationStep(final String newAxis, final String newNodeTest)
		{
			setAxis(newAxis);	//set the axis
			setNodeTest(newNodeTest);	//set the node test
		}

		/**@return A string representation of the location step in the form "axis::nodeTest",
			or "root" for the special location step containing "/" as an axis.*/
		public String toString()
		{
			if(getAxis().equals(ROOT))	//if this is the special "/" location step
				return "root";	//return "root"
			else	//if this is a normal location step
				return getAxis()+AXIS_SEPARATOR_STRING+getNodeTest();	//return axis::nodeTest
		}
	}

}