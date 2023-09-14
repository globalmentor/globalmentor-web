/*
 * Copyright © 1996-2022 GlobalMentor, Inc. <https://www.globalmentor.com/>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.globalmentor.xml.xpath;

import java.util.*;
import java.util.stream.Stream;

import javax.annotation.*;
import javax.xml.xpath.*;

import org.w3c.dom.*;

import com.globalmentor.xml.XmlDom;

/**
 * Parses XPath expressions and performs XPath operations on an XML document. The current implementation only interprets location paths.
 * @apiNote To lower the number of checked exceptions and for consistency with DOM manipulation, these utilities convert the checked exception
 *          {@link XPathExpressionException} to {@link DOMException} with an appropriate error code depending on the operation being performed.
 * @see <a href="http://www.w3.org/TR/xpath">XML Path Language (XPath)</a>
 * @author Garret Wilson
 */
public class XPathDom {

	/**
	 * Compiles an XPath expression for later evaluation.
	 * @apiNote This method is equivalent to {@link XPath#compile(String)} except that it throws an unchecked {@link DOMException} rather than an
	 *          {@link XPathExpressionException}. Compare to {@link java.util.regex.Pattern#compile(String)} and {@link java.net.URI#create(String)}.
	 * @param xpath The XPath instance.
	 * @param expression The XPath expression to compile.
	 * @return The compiled XPath expression.
	 * @throws DOMException with code {@link DOMException#SYNTAX_ERR} if the expression could not be compiled; {@link DOMException#getCause()} will contain the
	 *           {@link XPathExpressionException} cause.
	 */
	public static XPathExpression compileExpression(@Nonnull final XPath xpath, @Nonnull String expression) {
		try {
			return xpath.compile(expression);
		} catch(final XPathExpressionException xpathExpressionException) {
			throw (DOMException)new DOMException(DOMException.SYNTAX_ERR, xpathExpressionException.getMessage()).initCause(xpathExpressionException);
		}
	}

	/**
	 * Evaluates the compiled XPath expression in the specified context and return the result as stream of nodes whether or not the expression path was found.
	 * @apiNote The type of the context is usually {@link org.w3c.dom.Node}.
	 * @apiNote This method will always return a stream, even if the expression path was not found. Use {@link #findAsNodeStream(XPathExpression, Object)} instead
	 *          if it is desired to detect when an expression path is not found.
	 * @param xpathExpression The XPath expression to evaluate.
	 * @param context The context the XPath expression will be evaluated in.
	 * @return The stream of nodes, which may be empty, that is the result of evaluating the expression.
	 * @throws DOMException with code {@link DOMException#INVALID_ACCESS_ERR} if the expression cannot not be evaluated; {@link DOMException#getCause()} will
	 *           contain the {@link XPathExpressionException} cause.
	 */
	public static Stream<Node> evaulateAsNodeStream(@Nonnull final XPathExpression xpathExpression, @Nonnull Object context) {
		return findAsNodeStream(xpathExpression, context).orElse(Stream.empty());
	}

	/**
	 * Evaluates the compiled XPath expression in the specified context and return the result as a node if found.
	 * @apiNote The type of the context is usually {@link org.w3c.dom.Node}.
	 * @param xpathExpression The XPath expression to evaluate.
	 * @param context The context the XPath expression will be evaluated in.
	 * @return The node, if any, that is the result of evaluating the expression.
	 * @throws DOMException with code {@link DOMException#INVALID_ACCESS_ERR} if the expression cannot not be evaluated; {@link DOMException#getCause()} will
	 *           contain the {@link XPathExpressionException} cause.
	 */
	public static Optional<Node> findAsNode(@Nonnull final XPathExpression xpathExpression, @Nonnull Object context) {
		try {
			return Optional.ofNullable((Node)xpathExpression.evaluate(context, XPathConstants.NODE));
		} catch(final XPathExpressionException xpathExpressionException) {
			throw (DOMException)new DOMException(DOMException.INVALID_ACCESS_ERR, xpathExpressionException.getMessage()).initCause(xpathExpressionException);
		}
	}

	/**
	 * Evaluates the compiled XPath expression in the specified context and return the result as a node list if found.
	 * @apiNote The type of the context is usually {@link org.w3c.dom.Node}.
	 * @param xpathExpression The XPath expression to evaluate.
	 * @param context The context the XPath expression will be evaluated in.
	 * @return The node list, if any, that is the result of evaluating the expression.
	 * @throws DOMException with code {@link DOMException#INVALID_ACCESS_ERR} if the expression cannot not be evaluated; {@link DOMException#getCause()} will
	 *           contain the {@link XPathExpressionException} cause.
	 */
	public static Optional<NodeList> findAsNodeList(@Nonnull final XPathExpression xpathExpression, @Nonnull Object context) {
		try {
			return Optional.ofNullable((NodeList)xpathExpression.evaluate(context, XPathConstants.NODESET));
		} catch(final XPathExpressionException xpathExpressionException) {
			throw (DOMException)new DOMException(DOMException.INVALID_ACCESS_ERR, xpathExpressionException.getMessage()).initCause(xpathExpressionException);
		}
	}

	/**
	 * Evaluates the compiled XPath expression in the specified context and return the result as stream of nodes if found.
	 * @apiNote The type of the context is usually {@link org.w3c.dom.Node}.
	 * @param xpathExpression The XPath expression to evaluate.
	 * @param context The context the XPath expression will be evaluated in.
	 * @return The stream of nodes, if any, that is the result of evaluating the expression.
	 * @throws DOMException with code {@link DOMException#INVALID_ACCESS_ERR} if the expression cannot not be evaluated; {@link DOMException#getCause()} will
	 *           contain the {@link XPathExpressionException} cause.
	 */
	public static Optional<Stream<Node>> findAsNodeStream(@Nonnull final XPathExpression xpathExpression, @Nonnull Object context) {
		return findAsNodeList(xpathExpression, context).map(XmlDom::streamOf);
	}

	/**
	 * Evaluates the compiled XPath expression in the specified context and return the result as a string if found.
	 * @apiNote The type of the context is usually {@link org.w3c.dom.Node}.
	 * @param xpathExpression The XPath expression to evaluate.
	 * @param context The context the XPath expression will be evaluated in.
	 * @return The string, if any, that is the result of evaluating the expression.
	 * @throws DOMException with code {@link DOMException#INVALID_ACCESS_ERR} if the expression cannot not be evaluated; {@link DOMException#getCause()} will
	 *           contain the {@link XPathExpressionException} cause.
	 */
	public static Optional<String> findAsString(@Nonnull final XPathExpression xpathExpression, @Nonnull Object context) {
		try {
			return Optional.ofNullable((String)xpathExpression.evaluate(context, XPathConstants.STRING));
		} catch(final XPathExpressionException xpathExpressionException) {
			throw (DOMException)new DOMException(DOMException.INVALID_ACCESS_ERR, xpathExpressionException.getMessage()).initCause(xpathExpressionException);
		}
	}

}
