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

import com.globalmentor.xml.dom.impl.*;

import org.w3c.dom.Node;
import org.w3c.dom.stylesheets.*;

/**
 * A single stylesheet within XML.
 * @see XMLNode
 * @see org.w3c.dom.stylesheets.StyleSheet
 * @deprecated
 */
public abstract class XMLStyleSheet implements org.w3c.dom.stylesheets.StyleSheet {

	/**
	 * Constructor for a stylesheet specifying an owner node.
	 * @param ownerNode The node that associates this stylesheet with the document.
	 */
	public XMLStyleSheet(final Node ownerNode) {
		OwnerNode = ownerNode; //set the owner node
	}

	/**
	 * Constructor for a stylesheet specifying a parent stylesheet.
	 * @param parentStyleSheet The stylesheet that included this stylesheet.
	 */
	public XMLStyleSheet(final StyleSheet parentStyleSheet) {
		ParentStyleSheet = parentStyleSheet; //set the parent stylesheet
	}

	@Override
	public String getType() {
		return "text/css";
	} //TODO fix

	@Override
	public boolean getDisabled() {
		return false;
	} //TODO fix

	@Override
	public void setDisabled(boolean disabled) {
	} //TODO fix

	/**
	 * The node that associates this stylesheet with the document, such as an HTML LINK or STYLE element, or an XML linking processing instruction. If the
	 * stylesheet is included by another stylesheet, this will be <code>null</code>.
	 */
	private Node OwnerNode = null;

	@Override
	public Node getOwnerNode() {
		return OwnerNode;
	}

	/**
	 * The including stylesheet if this stylesheet was included from another stylesheet. If this is a top-level stylesheet, this value will be <code>null</code>.
	 */
	private StyleSheet ParentStyleSheet = null;

	@Override
	public StyleSheet getParentStyleSheet() {
		return ParentStyleSheet;
	}

	@Override
	public String getHref() {
		return null;
	} //TODO fix

	@Override
	public String getTitle() {
		return "";
	} //TODO fix

	@Override
	public MediaList getMedia() {
		return null;
	} //TODO fix

}
