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

package com.globalmentor.text.xml;

import com.globalmentor.java.Objects;

/**
 * The base class for XML objects that are named&mdash;identified by qname and/or namespace and local name.
 * <p>
 * This class can be used independently of the rest of the W3C DOM implementations in this package.
 * </p>
 * @author Garret Wilson
 * @deprecated
 */
public class XMLNamedObject //TODO implement NamedObject or extend DefaultNamedObject when Java gets generics; also investigate leveraging the new QualifiedName
{

	/** The qualified name of the object. */
	private String qname = null;

	/** The namespace URI, or <code>null</code> if there is no namespace. */
	private String namespaceURI = null;

	/** @return The namespace URI, or <code>null</code> there is no namespace. */
	public String getNamespaceURI() {
		return namespaceURI;
	}

	/**
	 * Sets the namespace URI.
	 * @param newNamespaceURI The new namespace URI, or <code>null</code> if there should be no namespace.
	 */
	void setNamespaceURI(final String newNamespaceURI) {
		namespaceURI = newNamespaceURI;
	}

	/** The namespace prefix, or <code>null</code> if there is no prefix. */
	private String prefix = null;

	/** Returns the namespace prefix, or <code>null</code> if there is no prefix. */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * Sets the namespace prefix of this node. The prefix is actually set in <code>setName()</code>.
	 * <p>
	 * Note that setting this attribute changes the qualified name.
	 * </p>
	 * @see #setName(java.lang.String, java.lang.String)
	 */
	public void setPrefix(final String prefix) {
		if(!Objects.equals(getPrefix(), prefix)) { //if the prefix is really changing
			setName(prefix, getLocalName()); //set the prefix and the local name, and update the node name itself
		}
	}

	/** The local name of the node. */
	private String localName = null;

	/** @return The local part of the qualified name of this node. */
	public String getLocalName() {
		return localName;
	}

	/**
	 * Sets the node's local name.
	 * @param localName The local part of the qualified name of this node
	 */
	public void setLocalName(final String localName) {
		if(!Objects.equals(getLocalName(), localName)) { //if the local name is really changing
			setName(prefix, getLocalName()); //set the prefix and the local name, and update the node name itself
		}
	}

	/**
	 * @return The qualified name of the object.
	 * @see DefaultNamed#getName()
	 */
	public String getQName() {
		return qname;
	}

	/**
	 * Sets the qualified name of the object, updating the prefix and local name as well.
	 * @param qname The new qualified name of the object.
	 * @see DefaultNamed#setName(Object)
	 */
	public void setQName(final String qname) {
		this.qname = qname; //save the qualified name
		prefix = XML.getPrefix(qname); //determine and set the prefix
		localName = XML.getLocalName(qname); //determine and set the local name		
	}

	/**
	 * Sets the prefix and local name of this object, updating the qualified name as well.
	 * @param newPrefix The namespace prefix of the node, or <code>null</code> for no prefix.
	 * @param newLocalName The node's local name.
	 * @see #setName
	 */
	protected void setName(final String newPrefix, final String newLocalName) {
		prefix = newPrefix; //set the prefix
		localName = newLocalName; //set the local name
		qname = XML.createQName(newPrefix, newLocalName); //create a qualified name and store it (don't call setQName(), which will needlessly reset the prefix and local name)
	}

	/**
	 * Constructor specifying the namespace and qname of the object.
	 * @param namespaceURI The URI of the namespace, or <code>null</code> if there is no namespace.
	 * @param qname The qualified name of the object.
	 */
	public XMLNamedObject(final String namespaceURI, final String qname) {
		//TODO fix		super(qname);	//construct the parent class
		setNamespaceURI(namespaceURI); //set the namespace URI
		this.qname = qname; //save the qualified name
		prefix = qname != null ? XML.getPrefix(qname) : null; //determine and set the prefix
		localName = qname != null ? XML.getLocalName(qname) : null; //determine and set the local name		
	}

}
