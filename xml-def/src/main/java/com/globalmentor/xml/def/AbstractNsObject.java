/*
 * Copyright © 2019-2020 GlobalMentor, Inc. <https://www.globalmentor.com/>
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

package com.globalmentor.xml.def;

import static java.lang.String.format;

import java.net.URI;
import java.net.URISyntaxException;

import javax.annotation.*;

/**
 * Abstract base class of an object with an optional namespace.
 * @apiNote The namespace is used in nullable string form to correspond to the DOM approach.
 * @author Garret Wilson
 */
abstract class AbstractNsObject {

	@Nullable
	private final String namespaceString;

	/** @return The string form of the namespace URI, or <code>null</code> if there is no namespace URI. */
	@Nullable
	public String getNamespaceString() {
		return namespaceString;
	}

	@Nullable
	private volatile URI namespaceUri; //lazily initialized if not provided at construction

	/**
	 * Returns The URI form of the namespace, which may be <code>null</code>.
	 * @apiNote Although the overwhelming majority of names use namespace strings that are valid URIs, some obsolete specifications may use a namespace that is
	 *          not a valid URI. This includes the WebDAV protocol, which uses <code>DAV:</code> for an XML namespace.
	 * @implSpec This implementation lazily creates the namespace URI if it hasn't been determined already.
	 * @return The namespace URI, which may be <code>null</code>.
	 * @throws UnsupportedOperationException if this name does not support a URI form of the namespace string.
	 */
	@Nullable
	public URI getNamespaceUri() {
		if(namespaceString != null && namespaceUri == null) { //ensure a URI only if we have namespace; race condition is benign
			try {
				namespaceUri = new URI(namespaceString);
			} catch(final URISyntaxException uriSyntaxException) {
				throw new UnsupportedOperationException(
						format("Namespace name %s does not support URI form of namespace: %s", namespaceString, uriSyntaxException.getMessage()), uriSyntaxException);
			}
		}
		return namespaceUri;
	}

	/**
	 * String namespace constructor.
	 * @param namespaceString The string form of the namespace URI, or <code>null</code> if there is no namespace URI.
	 */
	protected AbstractNsObject(@Nullable final String namespaceString) {
		this.namespaceString = namespaceString;
		this.namespaceUri = null; //lazily initialize later
	}

	/**
	 * URI namespace constructor.
	 * @param namespaceUri The string form of the namespace URI, or <code>null</code> if there is no namespace URI.
	 */
	protected AbstractNsObject(@Nullable final URI namespaceUri) {
		this.namespaceUri = namespaceUri;
		this.namespaceString = namespaceUri != null ? namespaceUri.toString() : null;
	}

}
