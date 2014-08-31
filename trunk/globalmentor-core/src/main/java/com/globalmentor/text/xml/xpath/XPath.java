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

package com.globalmentor.text.xml.xpath;

import java.util.*;

import org.w3c.dom.*;

/**
 * Parses XPath expressions and performs XPath operations on an XML document. The current implementation only interprets location paths.
 * <p>
 * Example usages include:
 * </p>
 * <ul>
 * <li><code>/html/body/*</code> Selects all element children of <code>body</code>.</li>
 * <li><code>/html/body/img/@src</code> Selects the <code>src</code> attribute of the first <code>img</code> element in <code>body</code>.</li> </li>
 * @see <a href="http://www.w3.org/TR/xpath">XML Path Language (XPath)</a>
 * @author Garret Wilson
 */
@Deprecated	//TODO convert to Java's included XPath; see e.g. http://stackoverflow.com/questions/2811001/how-to-read-xml-using-xpath-in-java
public class XPath {

	/** The character used to separate a location path into location steps. */
	public static final char LOCATION_STEP_SEPARATOR_CHAR = '/';
	/** The character used as a wildcard matching character. */
	public static final char WILDCARD_CHAR = '*';
	/** The string form of the wildcard matching character. */
	public static final String WILDCARD_STRING = String.valueOf(WILDCARD_CHAR);
	/** The string used to separate an axis from a node test in a location step. */
	public static final String AXIS_SEPARATOR_STRING = "::";
	public static final char LEFT_PARENTHESIS = '(';
	public static final char RIGHT_PARENTHESIS = ')';
	//axes
	/**
	 * <code>ROOT</code> is not defined as an axis in XPath, but here represents the root of the document.
	 */
	public static final String ROOT = String.valueOf(LOCATION_STEP_SEPARATOR_CHAR);
	public static final String ANCESTOR = "ancestor";
	public static final String ANCESTOR_OR_SELF = "ancestor-or-self";
	public static final String ATTRIBUTE = "attribute";
	public static final String CHILD = "child";
	public static final String DESCENDANT = "descendant";
	public static final String DESCENDANT_OR_SELF = "descendant-or-self";
	public static final String FOLLOWING = "following";
	public static final String FOLLOWING_SIBLING = "following-sibling";
	public static final String NAMESPACE = "namespace";
	public static final String PARENT = "parent";
	public static final String PRECEDING = "preceding";
	public static final String PRECEDING_SIBLING = "preceding-sibling";
	public static final String SELF = "self";
	//node tests
	//node types
	public static final String COMMENT = "comment";
	public static final String TEXT = "text";
	public static final String PROCESSING_INSTRUCTION = "processing-instruction";
	public static final String NODE = "node";
	public static final String COMMENT_NODE_TEST = COMMENT + LEFT_PARENTHESIS + RIGHT_PARENTHESIS;
	public static final String TEXT_NODE_TEST = TEXT + LEFT_PARENTHESIS + RIGHT_PARENTHESIS;
	public static final String PROCESSING_INSTRUCTION_NODE_TEST = PROCESSING_INSTRUCTION + LEFT_PARENTHESIS + RIGHT_PARENTHESIS;
	public static final String NODE_NODE_TEST = NODE + LEFT_PARENTHESIS + RIGHT_PARENTHESIS;

	/** The abbreviation for self::node(). */
	public static final String SELF_NODE_ABBREVIATION = ".";
	/** The abbreviation for parent::node(). */
	public static final String PARENT_NODE_ABBREVIATION = "..";
	/** The abbreviation for attribute::. */
	public static final String ATTRIBUTE_ABBREVIATION = "@";

	/**
	 * Convenience function to retrieve a node from the root element of the specified document, based on the specified XPath expression. If multiple nodes match
	 * the expression, the first one will be returned. If none match, <code>null</code> will be returned.
	 * @param node The document on which the expression should be evaluated.
	 * @param expression The XPath expression to evaluate.
	 * @return The first node matching the XPath expression, or <code>null</code> if there is no match.
	 */
	public static Node getNode(final Document document, final String expression) {
		return getNode(document.getDocumentElement(), expression); //apply the expression to the document's root element
	}

	/**
	 * Convenience function to retrieve a node from the specified node, based on the specified XPath expression. If multiple nodes match the expression, the first
	 * one will be returned. If none match, <code>null</code> will be returned.
	 * @param node The node on which the expression should be evaluated.
	 * @param expression The XPath expression to evaluate.
	 * @return The first node matching the XPath expression, or <code>null</code> if there is no match.
	 */
	public static Node getNode(final Node node, final String expression) {
		final List<Node> nodeList = (List<Node>)evaluatePathExpression(node, expression); //get the list of matching nodes
		return nodeList.size() > 0 ? nodeList.get(0) : null; //return the first node in the list, or null if there are no matches
		//TODO catch a class cast exception and throw another exception that's more descriptive
	}

	/**
	 * Convenience function to retrieve a node from the specified node, based on the specified XPath expression. If multiple nodes match the expression, the first
	 * one will be returned. If none match, <code>null</code> will be returned.
	 * @param node The node on which the expression should be evaluated.
	 * @param pathExpression The XPath path expression to evaluate.
	 * @return The first node matching the XPath expression, or <code>null</code> if there is no match.
	 */
	public static Node getNode(final Node node, final PathExpression pathExpression) {
		final List<Node> nodeList = (List<Node>)evaluatePathExpression(node, pathExpression); //get the list of matching nodes
		return nodeList.size() > 0 ? nodeList.get(0) : null; //return the first node in the list, or null if there are no matches
		//TODO catch a class cast exception and throw another exception that's more descriptive
	}

	/**
	 * Evaluates an XPath path expression on a node and returns the appropriate result of the evaluation.
	 * @param node The node on which the expression should be evaluated.
	 * @param pathExpression The XPath path expression to evaluate.
	 * @return <code>org.w3c.dom.NodeList</code>, <code>java.lang.Boolean</code>, <code>java.lang.Float</code>, or <code>java.lang.String</code> based upon the
	 *         result of the evaluation.
	 */
	public static Object evaluatePathExpression(final Node node, final PathExpression pathExpression) {
		return evaluatePathExpression(node, pathExpression, 0); //evaluate the array of location steps and return the list of nodes we receive
	}

	/**
	 * Evaluates an XPath location path and returns the appropriate result of the evaluation.
	 * @param node The document node on which the expression should be evaluated.
	 * @param expression The XPath expression to evaluate.
	 * @return <code>org.w3c.dom.NodeList</code>, <code>java.lang.Boolean</code>, <code>java.lang.Float</code>, or <code>java.lang.String</code> based upon the
	 *         result of the evaluation.
	 */
	public static Object evaluatePathExpression(final Node node, final String expression) {
		final PathExpression pathExpression = new PathExpression(expression); //TODO testing
		return evaluatePathExpression(node, pathExpression, 0); //evaluate the array of location steps and return the list of nodes we receive
	}

	/**
	 * Evaluates an XPath path expression and returns the appropriate result of the evaluation.
	 * @param node The document node on which the expression should be evaluated.
	 * @param pathExpression The representation of th path expression.
	 * @param stepIndex The current index of the path epxression being evaluated.
	 * @return <code>org.w3c.dom.NodeList</code>, <code>java.lang.Boolean</code>, <code>java.lang.Float</code>, or <code>java.lang.String</code> based upon the
	 *         result of the evaluation.
	 */
	protected static List<Node> evaluatePathExpression(final Node node, final PathExpression pathExpression, final int stepIndex) {
		final List<Node> nodeList = new ArrayList<Node>(); //we'll keep everything that matches in this list
		final Step step = pathExpression.getStep(stepIndex); //get the step we're currently evaluating
		assert step instanceof AxisStep : "Non-axis steps not yet supported";
		final AxisStep axisStep = (AxisStep)step; //get the step as an axis step
		final String nodeTest = axisStep.getNodeTest(); //get a reference to the node test
		if(axisStep.getAxis().equals(ROOT)) { //if this node indicates we should start from the root
			nodeList.add(node.getOwnerDocument().getDocumentElement()); //add the document root to our list of matching objects
		} else { //if this isn't the root location step
			//if this location step should include this node
			if(axisStep.getAxis().equals(SELF) || axisStep.getAxis().equals(ANCESTOR_OR_SELF) || axisStep.getAxis().equals(DESCENDANT_OR_SELF)) {
				if(isMatch(node, nodeTest, node.getNodeType() == Node.ATTRIBUTE_NODE ? Node.ATTRIBUTE_NODE : Node.ELEMENT_NODE)) //if this node matches (specifying that, if a wildcard is present, attributes should be picked only if this is an attribute; otherwise, elements should be picked for wildcards)
					nodeList.add(node); //add this node to the list
			}
			if(axisStep.getAxis().equals(PARENT)) //if we should search the parent
				nodeList.addAll(getMatchingParent(node, nodeTest, false)); //add the matching parent to our list
			else if(axisStep.getAxis().equals(ANCESTOR) || axisStep.getAxis().equals(ANCESTOR_OR_SELF)) //if we should search all ancestors
				nodeList.addAll(getMatchingParent(node, nodeTest, true)); //add all matching ancestors to our list
			else if(axisStep.getAxis().equals(CHILD)) //if we should search children
				nodeList.addAll(getMatchingChildren(node, nodeTest, false)); //add all matching children to our list
			else if(axisStep.getAxis().equals(DESCENDANT) || axisStep.getAxis().equals(DESCENDANT_OR_SELF)) //if we should search all descendants
				nodeList.addAll(getMatchingChildren(node, nodeTest, true)); //add all matching descendants to our list
			else if(axisStep.getAxis().equals(ATTRIBUTE)) //if we should search attributes
				nodeList.addAll(getMatchingNodes(node.getAttributes(), nodeTest, false, Node.ATTRIBUTE_NODE)); //add all matching attributes to our list (indicating that we shouldn't search child nodes, since attributes have no child nodes)
		}
		/*TODO fix
			public static final String FOLLOWING="following";
			public static final String FOLLOWING_SIBLING="following-sibling";
			public static final String NAMESPACE="namespace";
			public static final String PRECEDING="preceding";
			public static final String PRECEDING_SIBLING="preceding-sibling";
		*/
		if(stepIndex < pathExpression.getStepCount() - 1) { //if we haven't reached the end of the path expression
			final List<Node> resultNodeList = new ArrayList<Node>(); //make a list of nodes to store our results
			for(int nodeIndex = 0; nodeIndex < nodeList.size(); ++nodeIndex) { //look at each node that matched this step in the path
				final Node locationStepNode = nodeList.get(nodeIndex); //get a reference to this node
				resultNodeList.addAll(evaluatePathExpression(locationStepNode, pathExpression, stepIndex + 1)); //evaluate the next step in the path using this node, and add the results to the results we have so far
			}
			return resultNodeList; //return the results we found
		} else
			//if we've reached the end of our location path, the nodes we've gathered so far are our results
			return nodeList; //return the list we filled with nodes
	}

	/**
	 * Finds all matching children of a particular node.
	 * @param node The node whose children should be checked.
	 * @param nodeTest The node test (usually from a location step) against which the nodes should be compared.
	 * @param includeDescendants Whether all descendants (children of children) should be included in the search
	 * @return A list of nodes which meet the criteria of <code>nodeTest</code>.
	 */
	protected static List<Node> getMatchingChildren(final Node node, final String nodeTest, final boolean includeDescendants) {
		final List<Node> nodeList = new ArrayList<Node>(); //create a new node list to return
		for(int childIndex = 0; childIndex < node.getChildNodes().getLength(); childIndex++) { //look at each child node
			final Node childNode = node.getChildNodes().item(childIndex); //get a reference to this node
			if(isMatch(childNode, nodeTest, Node.ELEMENT_NODE)) //if this child node matches (specifying that, if a wildcard is present, only elements should be chosen)
				nodeList.add(childNode); //add this child node to the list
			if(includeDescendants) //if each of the children should check for matching tags as well
				nodeList.addAll(getMatchingChildren(childNode, nodeTest, includeDescendants)); //check all the descendants of this child as well
		}
		return nodeList; //return the list we created and filled
	}

	/**
	 * Finds the matching parent(s) of a particular node.
	 * @param node The node whose parent(s) should be checked.
	 * @param nodeTest The node test (usually from a location step) against which the nodes should be compared.
	 * @param includeAncestors Whether all ancestors (parents of parents, etc.) should be included in the search
	 * @return A list of nodes which meet the criteria of <code>nodeTest</code>.
	 */
	protected static List<Node> getMatchingParent(final Node node, final String nodeTest, final boolean includeAncestors) {
		final List<Node> nodeList = new ArrayList<Node>(); //create a new node list to return
		final Node parentNode = node.getParentNode(); //get a reference to this node's parent
		if(parentNode != null) { //if this node has a parent
			if(isMatch(parentNode, nodeTest, Node.ELEMENT_NODE)) //if the parent node matches (specifying that, if a wildcard is present, only elements should be chosen)
				nodeList.add(parentNode); //add the parent node to the list
			if(includeAncestors) //if the parent should check its parents for matching tags as well
				nodeList.addAll(getMatchingParent(parentNode, nodeTest, includeAncestors)); //check all the parents of this parent as well
		}
		return nodeList; //return the list we created and filled
	}

	/**
	 * Finds all matching nodes in a named node map.
	 * @param namedNodeMap The map of nodes whose children should be checked.
	 * @param nodeTest The node test (usually from a location step) against which the nodes should be compared.
	 * @param includeDescendants Whether this node's descendants should be included in the search
	 * @param wildcardType The type of node (a <code>Node</code> constant) which should be returned if <code>nodeTest</code> is a wildcard.
	 * @return A list of nodes which meet the criteria of <code>nodeTest</code>.
	 */
	protected static List<Node> getMatchingNodes(final NamedNodeMap namedNodeMap, final String nodeTest, final boolean includeDescendants,
			final short wildcardType) {
		final List<Node> resultNodeList = new ArrayList<Node>(); //create a new node list to return
		for(int nodeIndex = 0; nodeIndex < namedNodeMap.getLength(); nodeIndex++) { //look at each node in the list
			final Node node = namedNodeMap.item(nodeIndex); //get a reference to this node
			if(isMatch(node, nodeTest, wildcardType)) //if this node matches
				resultNodeList.add(node); //add this node to the list
		}
		return resultNodeList; //return the list we created and filled
	}

	/**
	 * Checks to see if a particular node matches the criteria of a location step.
	 * @param node The node to check.
	 * @param nodeTest The node test (usually from a location step) against which the node should be compared.
	 * @param wildcardType The type of node (a <code>Node</code> constant) which should be returned if <code>nodeTest</code> is a wildcard.
	 * @return <code>true</code> if the node meets the correct criteria of the locationStep, else <code>false</code>.
	 */
	protected static boolean isMatch(final Node node, final String nodeTest, final short wildcardType) {
		if(nodeTest.equals(WILDCARD_STRING)) { //if this is a test for a wildcard
			return node.getNodeType() == wildcardType; //return whether or not the node is of the correct type for a wildcard match
		}
		if(nodeTest.equals(NODE_NODE_TEST)) //if this is a test for "node()"
			return true; //every node matches this test
		if(nodeTest.equals(TEXT_NODE_TEST)) //if the test is for a text node
			return node.getNodeType() == Node.TEXT_NODE; //show whether this is a text node
		if(nodeTest.equals(COMMENT_NODE_TEST)) //if the test is for a comment node
			return node.getNodeType() == Node.COMMENT_NODE; //return whether this is a comment node
		if(nodeTest.equals(PROCESSING_INSTRUCTION_NODE_TEST))
			return node.getNodeType() == Node.PROCESSING_INSTRUCTION_NODE; //return whether this is a processing instruction node
		//TODO check for arguments to the processing instruction
		//if this isn't one of the specific tests, this is just a simple name test
		return node.getNodeName().equals(nodeTest); //return whether or not the name matches the node test
	}

}