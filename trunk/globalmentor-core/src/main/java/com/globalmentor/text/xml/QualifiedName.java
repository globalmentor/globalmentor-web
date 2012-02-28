/*
 * Copyright © 1996-2012 GlobalMentor, Inc. <http://www.globalmentor.com/>
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

import java.net.URI;

import static com.globalmentor.java.Objects.*;

import com.globalmentor.model.IDed;
import com.globalmentor.net.DefaultResource;

/**
 * Represents a qualified name (QName) of an XML element, including the namespace URI, the prefix, and the local name. The reference URI indicates the
 * concatenation of the namespace URI and the local name.
 * <p>
 * For qualified names created from existing XML nodes, if the node namespace is not a valid URI (e.g. "DAV:"), it will be converted to a valid URI (e.g.
 * "DAV:/") if possible; however, the entire reference URI will maintain the original namespace string (e.g. "DAV:creationdate).
 * </p>
 * <p>
 * This class does not currently use true URIs for namespaces, primarily because WebDAV uses a namespace ("DAV:") which is not a true URI.
 * </p>
 * @author Garret Wilson
 */
public class QualifiedName extends DefaultResource implements IDed<URI>
{

	/** The namespace URI, or <code>null</code> if there is no namespace URI. */
	private final URI namespaceURI;

	/** @return The namespace URI, or <code>null</code> if there is no namespace URI. */
	public URI getNamespaceURI()
	{
		return namespaceURI;
	}

	/** @return The unique identifier of the object. */
	public URI getID()
	{
		return getURI();
	}

	/** The prefix string, or <code>null</code> if there is no prefix. */
	private final String prefix;

	/** @return The prefix string, or <code>null</code> if there is no prefix. */
	public String getPrefix()
	{
		return prefix;
	}

	/** The local name. */
	private final String localName;

	/** @return The local name. */
	public String getLocalName()
	{
		return localName;
	}

	/**
	 * qname constructor.
	 * @param namespaceURI The namespace URI, or <code>null</code> if there is no namespace URI.
	 * @param qname The combined <var>prefix</var>:<var>localName</var> qualified name.
	 * @throws NullPointerException if the given qname is <code>null</code>.
	 */
	public QualifiedName(final URI namespaceURI, final String qname)
	{
		this(namespaceURI, XML.getPrefix(qname), XML.getLocalName(qname)); //split out the prefix and local name and call the full constructor
	}

	/**
	 * Full constructor.
	 * @param namespaceURI The namespace URI, or <code>null</code> if there is no namespace URI.
	 * @param prefix The prefix string, or <code>null</code> if there is no prefix.
	 * @param localName The local name.
	 * @throws NullPointerException if the given local name is <code>null</code>.
	 */
	public QualifiedName(final URI namespaceURI, final String prefix, final String localName)
	{
		super(createReferenceURI(namespaceURI, localName)); //create our reference URI by combining our namespace URI and our local name
		this.namespaceURI = namespaceURI;
		this.prefix = prefix;
		this.localName = checkInstance(localName, "Local name cannot be null.");
	}

	/**
	 * Namespace string, prefix, and local name constructor.
	 * <p>
	 * If the namespace is not a valid URI (e.g. "DAV:"), it will be converted to a valid URI (e.g. "DAV:/") if possible.
	 * </p>
	 * @param namespace The namespace, or <code>null</code> if there is no namespace.
	 * @param prefix The prefix string, or <code>null</code> if there is no prefix.
	 * @param localName The local name.
	 * @throws NullPointerException if the given local name is <code>null</code>.
	 * @throws IllegalArgumentException if the namespace is not <code>null</code> and cannot be converted to a valid URI.
	 * @see XML#toNamespaceURI(String)
	 */
	QualifiedName(final String namespace, final String prefix, final String localName)
	{
		super(createReferenceURI(namespace, localName)); //create our reference URI by combining our namespace URI and our local name
		this.namespaceURI = XML.toNamespaceURI(namespace);
		this.prefix = prefix;
		this.localName = checkInstance(localName, "Local name cannot be null.");
	}

	/**
	 * Returns the complete qualified name.
	 * @return The qualified name in <code><var>prefix</var>:<var>localName</var></code> form.
	 */
	public String getQName()
	{
		return XML.createQName(getPrefix(), getLocalName()); //create and return a qualified name
	}

	/**
	 * Creates a reference URI from an XML namespace URI and a local name.
	 * @param namespaceURI The XML namespace URI used in the serialization, or <code>null</code> if there is no namespace URI.
	 * @param localName The XML local name used in the serialization.
	 * @return A reference URI constructed from the given namespace and local name.
	 */
	private static URI createReferenceURI(final URI namespaceURI, final String localName) //TODO decide how to handle null
	{
		return createReferenceURI(namespaceURI != null ? namespaceURI.toString() : null, localName); //concatenate the string version of the namespace URI and the local name
	}

	/**
	 * Creates a reference URI from an XML namespace and a local name.
	 * <p>
	 * This method was created specifically for WebDAV, which uses a namespace ("DAV:") which is not a true URI.
	 * </p>
	 * @param namespace The XML namespace used in the serialization, or <code>null</code> if there is no namespace URI.
	 * @param localName The XML local name used in the serialization.
	 * @return A reference URI constructed from the given namespace and local name.
	 */
	private static URI createReferenceURI(final String namespace, final String localName)
	{
		final StringBuilder referenceURIBuilder = new StringBuilder(); //create a new string builder
		if(namespace != null) //if there is a namespace
		{
			referenceURIBuilder.append(namespace); //append the namespace
		}
		referenceURIBuilder.append(localName); //append the local name
		//TODO beware local names that may be valid XML names but not valid URI characters---we should probably encode the local name
		return URI.create(referenceURIBuilder.toString()); //concatenate the namespace and the local name to yield the reference URI
	}

}
