/*
 * Copyright © 2019 GlobalMentor, Inc. <http://www.globalmentor.com/>
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

package com.globalmentor.xml;

import static com.globalmentor.java.Conditions.*;
import static java.util.Objects.*;

import java.util.Objects;

import javax.annotation.*;

import org.w3c.dom.Node;

/**
 * A namespaced name value object for encapsulating the namespace and a name of an XML element or attribute.
 * @apiNote The namespace is used in string from and the methods are nullable to correspond to the DOM approach.
 * @author Garret Wilson
 */
public final class NsName {

	private final String namespaceString;

	/** @return The string form of the namespace URI, or <code>null</code> if there is no namespace URI. */
	public @Nullable String getNamespaceString() {
		return namespaceString;
	}

	/** The local name. */
	private final String localName;

	/** @return The local name; guaranteed not to be the empty string. */
	public @Nonnull String getLocalName() {
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
		return of(null, localName);
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
	 * Static factory method from a namespace string and a local name.
	 * @param node The node of which the namespace string and local name will be determined.
	 * @return A namespaced name with the given namespace and local name.
	 * @throws NullPointerException if the given local name is <code>null</code>.
	 * @throws IllegalArgumentException if the given local name is the empty string.
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
	 * Constructor.
	 * @param namespaceString The string form of the namespace URI, or <code>null</code> if there is no namespace URI.
	 * @param localName The local name.
	 * @throws NullPointerException if the given local name is <code>null</code>.
	 * @throws IllegalArgumentException if the given local name is the empty string.
	 */
	private NsName(@Nullable final String namespaceString, @Nonnull final String localName) {
		this.namespaceString = namespaceString;
		this.localName = requireNonNull(localName);
		checkArgument(!localName.isEmpty(), "Namespaced local name cannot be empty.");
	}

	@Override
	public int hashCode() {
		return hash(namespaceString, localName);
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
		return Objects.equals(namespaceString, nsName.namespaceString) && localName.equals(nsName.localName);
	}

	/**
	 * {@inheritDoc}
	 * @implSpec This implementation returns a concatenation of the namespace, if any, and the local name name.
	 */
	@Override
	public String toString() {
		final StringBuilder stringBuilder = new StringBuilder();
		if(namespaceString != null) {
			stringBuilder.append(namespaceString);
		}
		stringBuilder.append(localName);
		return stringBuilder.toString();
	}

}
