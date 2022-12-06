/*
 * Copyright © 1996-2020 GlobalMentor, Inc. <http://www.globalmentor.com/>
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

package com.globalmentor.xml.def;

import static com.globalmentor.java.CharSequences.*;
import static com.globalmentor.java.Conditions.*;
import static com.globalmentor.xml.def.XML.*;
import static java.util.Objects.*;

import java.net.URI;
import java.util.*;

import javax.annotation.*;

import org.w3c.dom.Node;

/**
 * Represents a qualified name of an XML element, including the namespace URI, the prefix, and the local name.
 * @apiNote The namespace is used in nullable string form to correspond to the DOM approach.
 * @author Garret Wilson
 * @see <a href="https://www.w3.org/TR/xml-names/#dt-qualname">Namespaces in XML 1.0 (Third Edition) § 2.1 Basic Concepts</a>
 */
public final class NsQualifiedName extends AbstractNsObject {

	@Nonnull
	private final String qualifiedName;

	/**
	 * Returns the complete qualified name; guaranteed not to be the empty string.
	 * @return The qualified name in <code><var>prefix</var>:<var>localName</var></code> form.
	 */
	@Nonnull
	public String getQualifiedName() {
		return qualifiedName;
	}

	/** @return The prefix string, if any; guaranteed not to be the empty string if present. */
	public Optional<String> findPrefix() {
		return XML.findPrefix(qualifiedName);
	}

	/** @return The local name; guaranteed not to be the empty string. */
	@Nonnull
	public String getLocalName() {
		return XML.getLocalName(qualifiedName);
	}

	/**
	 * Static factory method from a qualified name with no namespace.
	 * @param qualifiedName The qualified name in <code><var>prefix</var>:<var>localName</var></code> form.
	 * @return A namespaced qualified name with the given qualified name with no namespace.
	 * @throws NullPointerException if the given qualified name is <code>null</code>.
	 * @throws IllegalArgumentException if the given qualified name or its prefix and/or local name is the empty string.
	 */
	public static NsQualifiedName of(@Nonnull final String qualifiedName) {
		return of((String)null, qualifiedName);
	}

	/**
	 * Static factory method from a namespace string and a qualified name.
	 * @param namespaceString The string form of the namespace URI, or <code>null</code> if there is no namespace URI.
	 * @param qualifiedName The qualified name in <code><var>prefix</var>:<var>localName</var></code> form.
	 * @return A namespaced name with the given namespace and qualified name.
	 * @throws NullPointerException if the given qualified name is <code>null</code>.
	 * @throws IllegalArgumentException if the given qualified name or its prefix and/or local name is the empty string.
	 */
	public static NsQualifiedName of(@Nullable final String namespaceString, @Nonnull final String qualifiedName) {
		return new NsQualifiedName(namespaceString, qualifiedName);
	}

	/**
	 * Static factory method from a namespace URI and a qualified name.
	 * @param namespaceUri The namespace URI, or <code>null</code> if there is no namespace URI.
	 * @param qualifiedName The qualified name in <code><var>prefix</var>:<var>localName</var></code> form.
	 * @return A namespaced name with the given namespace and qualified name.
	 * @throws NullPointerException if the given qualified name is <code>null</code>.
	 * @throws IllegalArgumentException if the given qualified name or its prefix and/or local name is the empty string.
	 */
	public static NsQualifiedName of(@Nullable final URI namespaceUri, @Nonnull final String qualifiedName) {
		return new NsQualifiedName(namespaceUri, qualifiedName);
	}

	/**
	 * Static factory method from a DOM node.
	 * @param node The node of which the namespace string and qualified name will be determined.
	 * @return A namespaced qualified name with the given namespace and qualified name.
	 * @throws NullPointerException if the given node's qualified name is <code>null</code>.
	 * @throws IllegalArgumentException if the given node's qualified name or its prefix and/or local name is the empty string.
	 * @throws IllegalArgumentException if the node was created with DOM Level 1, which does not support namespaces.
	 * @see Node#getNamespaceURI()
	 * @see Node#getNodeName()
	 */
	public static NsQualifiedName ofNode(@Nullable final Node node) {
		checkArgument(node.getLocalName() != null, "Namespaced name does not support node %s created with DOM Level 1.", node.getNodeName());
		return of(node.getNamespaceURI(), node.getNodeName());
	}

	/**
	 * Namespace string, prefix, and local name constructor.
	 * @param namespaceString The namespace, or <code>null</code> if there is no namespace.
	 * @param qualifiedName The qualified name in <code><var>prefix</var>:<var>localName</var></code> form.
	 * @throws NullPointerException if the given qualified name is <code>null</code>.
	 * @throws IllegalArgumentException if the given qualified name or its prefix and/or local name is the empty string.
	 */
	private NsQualifiedName(final String namespaceString, final String qualifiedName) {
		super(namespaceString);
		this.qualifiedName = requireNonNull(qualifiedName);
		checkArgument(!qualifiedName.isEmpty(), "Namespaced qualified name cannot be empty.");
		checkArgument(!startsWith(qualifiedName, NAMESPACE_DIVIDER), "Namespaced qualified name cannot have an empty prefix.");
		checkArgument(!endsWith(qualifiedName, NAMESPACE_DIVIDER), "Namespaced qualified name cannot have an empty local name.");
	}

	/**
	 * Namespace string, prefix, and local name constructor.
	 * @param namespaceUri The namespace URI, or <code>null</code> if there is no namespace URI.
	 * @param qualifiedName The qualified name in <code><var>prefix</var>:<var>localName</var></code> form.
	 * @throws NullPointerException if the given qualified name is <code>null</code>.
	 * @throws IllegalArgumentException if the given qualified name or its prefix and/or local name is the empty string.
	 */
	public NsQualifiedName(final URI namespaceUri, final String qualifiedName) {
		super(namespaceUri);
		this.qualifiedName = requireNonNull(qualifiedName);
		checkArgument(!qualifiedName.isEmpty(), "Namespaced qualified name cannot be empty.");
		checkArgument(!startsWith(qualifiedName, NAMESPACE_DIVIDER), "Namespaced qualified name cannot have an empty prefix.");
		checkArgument(!endsWith(qualifiedName, NAMESPACE_DIVIDER), "Namespaced qualified name cannot have an empty local name.");
	}

	/**
	 * Converts this qualified name to an unqualified name.
	 * @return The unqualified form of the name, with no prefix information.
	 */
	public NsName toNsName() {
		return NsName.of(getNamespaceString(), getLocalName());
	}

	@Override
	public int hashCode() {
		return hash(getNamespaceString(), qualifiedName);
	}

	@Override
	public boolean equals(final Object object) {
		if(this == object) {
			return true;
		}
		if(!(object instanceof NsQualifiedName)) {
			return false;
		}
		final NsQualifiedName nsQualifiedName = (NsQualifiedName)object;
		return Objects.equals(getNamespaceString(), nsQualifiedName.getNamespaceString()) && qualifiedName.equals(nsQualifiedName.getQualifiedName());
	}

	/**
	 * {@inheritDoc}
	 * @implSpec This implementation returns a concatenation of the namespace, if any, and the local name name in the form {@code <namespace>/qualifiedName}.
	 */
	@Override
	public String toString() {
		final StringBuilder stringBuilder = new StringBuilder();
		final String namespaceString = getNamespaceString();
		if(namespaceString != null) {
			stringBuilder.append('<').append(namespaceString).append('>').append('/');
		}
		stringBuilder.append(qualifiedName);
		return stringBuilder.toString();
	}

}
