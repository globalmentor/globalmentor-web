/*
 * Copyright © 1996-2022 GlobalMentor, Inc. <http://www.globalmentor.com/>
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

package com.globalmentor.xml.spec;

/**
 * Definitions from <a href="https://www.w3.org/TR/1999/REC-xpath-19991116/">XML Path Language (XPath) Version 1.0</a>.
 * @author Garret Wilson
 * @see <a href="https://www.w3.org/TR/xpath/">XML Path Language (XPath)</a>
 */
public class XPath {

	/** The character used to separate a location path into location steps. */
	public static final char LOCATION_STEP_SEPARATOR_CHAR = '/';
	/** The character used as a wildcard matching character. */
	public static final char WILDCARD_CHAR = '*';
	/** The string form of the wildcard matching character. */
	public static final String WILDCARD_STRING = String.valueOf(WILDCARD_CHAR);
	/** The string used to separate an axis from a node test in a location step. */
	public static final String AXIS_SEPARATOR_STRING = "::";
	/** XPath left parenthesis character. */
	public static final char LEFT_PARENTHESIS = '(';
	/** XPath right parenthesis character. */
	public static final char RIGHT_PARENTHESIS = ')';
	//axes
	/** <code>ROOT</code> is not defined as an axis in XPath, but here represents the root of the document. */
	public static final String ROOT = String.valueOf(LOCATION_STEP_SEPARATOR_CHAR);
	/** Ancestor location step. */
	public static final String ANCESTOR = "ancestor";
	/** Ancestor or self location step. */
	public static final String ANCESTOR_OR_SELF = "ancestor-or-self";
	/** Attribute location step. */
	public static final String ATTRIBUTE = "attribute";
	/** Child location step. */
	public static final String CHILD = "child";
	/** Descendant location step. */
	public static final String DESCENDANT = "descendant";
	/** Descendant or self location step. */
	public static final String DESCENDANT_OR_SELF = "descendant-or-self";
	/** Following location step. */
	public static final String FOLLOWING = "following";
	/** Following sibling location step. */
	public static final String FOLLOWING_SIBLING = "following-sibling";
	/** Namespace location step. */
	public static final String NAMESPACE = "namespace";
	/** Parent location step. */
	public static final String PARENT = "parent";
	/** Preceding location step. */
	public static final String PRECEDING = "preceding";
	/** Preceding sibling location step. */
	public static final String PRECEDING_SIBLING = "preceding-sibling";
	/** Self location step. */
	public static final String SELF = "self";
	//node tests
	//node types
	/** Comment node type. */
	public static final String COMMENT = "comment";
	/** Text node type. */
	public static final String TEXT = "text";
	/** Processing instruction node type. */
	public static final String PROCESSING_INSTRUCTION = "processing-instruction";
	/** Node node type. */
	public static final String NODE = "node";
	/** Comment node test. */
	public static final String COMMENT_NODE_TEST = COMMENT + LEFT_PARENTHESIS + RIGHT_PARENTHESIS;
	/** Text node test. */
	public static final String TEXT_NODE_TEST = TEXT + LEFT_PARENTHESIS + RIGHT_PARENTHESIS;
	/** Processing instruction node test. */
	public static final String PROCESSING_INSTRUCTION_NODE_TEST = PROCESSING_INSTRUCTION + LEFT_PARENTHESIS + RIGHT_PARENTHESIS;
	/** Node node test. */
	public static final String NODE_NODE_TEST = NODE + LEFT_PARENTHESIS + RIGHT_PARENTHESIS;

	/** The abbreviation for self::node(). */
	public static final String SELF_NODE_ABBREVIATION = ".";
	/** The abbreviation for parent::node(). */
	public static final String PARENT_NODE_ABBREVIATION = "..";
	/** The abbreviation for attribute::. */
	public static final String ATTRIBUTE_ABBREVIATION = "@";

}
