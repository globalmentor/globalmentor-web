/*
 * Copyright © 2019-2020 GlobalMentor, Inc. <http://www.globalmentor.com/>
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

import static com.globalmentor.java.Conditions.*;
import static com.globalmentor.xml.spec.XML.*;
import static java.util.Objects.*;

import java.net.URI;
import java.util.Objects;

import javax.annotation.*;

import org.w3c.dom.Node;

/**
 * A namespaced name value object for encapsulating the namespace and a name of an XML element or attribute.
 * @apiNote The namespace is used in nullable string form to correspond to the DOM approach.
 * @author Garret Wilson
 */
public final class NsName extends AbstractNsObject {

	@Nonnull
	private final String localName;

	/** @return The local name; guaranteed not to be the empty string. */
	@Nonnull
	public String getLocalName() {
		return localName;
	}

	/**
	 * Static factory method from a local name with no namespace.
	 * @param localName The local name.
	 * @return A namespaced name with the given local name with no namespace.
	 * @throws NullPointerException if the given local name is <code>null</code>.
	 * @throws IllegalArgumentException if the given local name is the empty string.
	 */
	public static NsName of(@Nonnull final String localName) {
		return of((String)null, localName);
	}

	/**
	 * Static factory method from a namespace string and a local name.
	 * @param namespaceString The string form of the namespace URI, or <code>null</code> if there is no namespace URI.
	 * @param localName The local name.
	 * @return A namespaced name with the given namespace and local name.
	 * @throws NullPointerException if the given local name is <code>null</code>.
	 * @throws IllegalArgumentException if the given local name is the empty string.
	 */
	public static NsName of(@Nullable final String namespaceString, @Nonnull final String localName) {
		return new NsName(namespaceString, localName);
	}

	/**
	 * Static factory method from a namespace URI and a local name.
	 * @param namespaceUri The namespace URI, or <code>null</code> if there is no namespace URI.
	 * @param localName The local name.
	 * @return A namespaced name with the given namespace and local name.
	 * @throws NullPointerException if the given local name is <code>null</code>.
	 * @throws IllegalArgumentException if the given local name is the empty string.
	 */
	public static NsName of(@Nullable final URI namespaceUri, @Nonnull final String localName) {
		return new NsName(namespaceUri, localName);
	}

	/**
	 * Static factory method from DOM node.
	 * @param node The node of which the namespace string and local name will be determined.
	 * @return A namespaced name with the given namespace and local name.
	 * @throws NullPointerException if the given node's local name is <code>null</code>.
	 * @throws IllegalArgumentException if the given node's local name is the empty string.
	 * @throws IllegalArgumentException if the node was created with DOM Level 1, which does not support namespaces.
	 * @see Node#getNamespaceURI()
	 * @see Node#getLocalName()
	 */
	public static NsName ofNode(@Nullable final Node node) {
		final String localName = node.getLocalName();
		checkArgument(localName != null, "Namespaced name does not support node %s created with DOM Level 1.", node.getNodeName());
		return of(node.getNamespaceURI(), localName);
	}

	/**
	 * Namespace string constructor.
	 * @param namespaceString The string form of the namespace URI, or <code>null</code> if there is no namespace URI.
	 * @param localName The local name.
	 * @throws NullPointerException if the given local name is <code>null</code>.
	 * @throws IllegalArgumentException if the given local name is the empty string.
	 */
	private NsName(@Nullable final String namespaceString, @Nonnull final String localName) {
		super(namespaceString);
		this.localName = requireNonNull(localName);
		checkArgument(!localName.isEmpty(), "Namespaced local name cannot be empty.");
	}

	/**
	 * Namespace URI constructor.
	 * @param namespaceUri The namespace URI, or <code>null</code> if there is no namespace URI.
	 * @param localName The local name.
	 * @throws NullPointerException if the given local name is <code>null</code>.
	 * @throws IllegalArgumentException if the given local name is the empty string.
	 */
	private NsName(@Nullable final URI namespaceUri, @Nonnull final String localName) {
		super(namespaceUri);
		this.localName = requireNonNull(localName);
		checkArgument(!localName.isEmpty(), "Namespaced local name cannot be empty.");
	}

	/**
	 * Qualifies a namespaced name by adding a namespace prefix.
	 * @param prefix The qualifying namespace prefix to add, or <code>null</code> there is no prefix.
	 * @return A namespaced qualified name with the prefix appended to the local name.
	 */
	public NsQualifiedName withPrefix(@Nullable final String prefix) {
		return NsQualifiedName.of(getNamespaceString(), createQualifiedName(prefix, getLocalName()));
	}

	/**
	 * Qualifies a namespaced name by adding a namespace prefix.
	 * @implSpec This is a convenience method that delegates to {@link #withPrefix(String)} with a <code>null</code> prefix.
	 * @return A namespaced qualified name with no prefix.
	 */
	public NsQualifiedName withNoPrefix() {
		return withPrefix(null);
	}

	@Override
	public int hashCode() {
		return hash(getNamespaceString(), localName);
	}

	@Override
	public boolean equals(final Object object) {
		if(this == object) {
			return true;
		}
		if(!(object instanceof NsName)) {
			return false;
		}
		final NsName nsName = (NsName)object;
		return Objects.equals(getNamespaceString(), nsName.getNamespaceString()) && localName.equals(nsName.localName);
	}

	/**
	 * {@inheritDoc}
	 * @implSpec This implementation returns a concatenation of the namespace, if any, and the local name name in the form {@code <namespace>/localName}.
	 */
	@Override
	public String toString() {
		final StringBuilder stringBuilder = new StringBuilder();
		final String namespaceString = getNamespaceString();
		if(namespaceString != null) {
			stringBuilder.append('<').append(namespaceString).append('>').append('/');
		}
		stringBuilder.append(localName);
		return stringBuilder.toString();
	}

	/**
	 * Determines whether the given node has the same namespace and local name as this object. That is calling {@link #ofNode(Node)} on the node would result in
	 * an instance of this class that would be equal to this instance.
	 * @implSpec This implementation delegates to {@link #matches(String, String)}.
	 * @param node The node to check.
	 * @return <code>true</code> if the node namespace and local name match the namespace and name of this object.
	 * @see Node#getNamespaceURI()
	 * @see Node#getLocalName()
	 */
	public boolean matches(@Nonnull final Node node) {
		return matches(node.getNamespaceURI(), node.getLocalName());
	}

	/**
	 * Determines whether the given namespace and local name are the same as this object's. That is calling {@link #of(String, String)} on the values would result
	 * in an instance of this class that would be equal to this instance.
	 * @param namespaceString The string form of a namespace URI to check, or <code>null</code> if there is no namespace URI.
	 * @param localName The local name to check.
	 * @return <code>true</code> if the given namespace and local name match the namespace and name of this object.
	 * @see Node#getNamespaceURI()
	 * @see Node#getLocalName()
	 */
	public boolean matches(@Nullable final String namespaceString, @Nonnull final String localName) {
		return Objects.equals(getNamespaceString(), namespaceString) && getLocalName().equals(localName);
	}

}
