/*
 * Copyright © 2022 GlobalMentor, Inc. <http://www.globalmentor.com/>
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
import static java.util.Collections.*;
import static javax.xml.XMLConstants.*;

import java.util.Iterator;

import javax.annotation.*;
import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;

import com.globalmentor.collections.iterators.ObjectIterator;

/**
 * A base namespace context implementation that takes care of the default namespace mappings required by the API, primarily mappings for the <code>xml</code>
 * and <code>xmlns</code> prefixes.
 * @author Garret Wilson
 */
public abstract class BaseNamespaceContext implements NamespaceContext {

	/**
	 * {@inheritDoc}
	 * @apiNote Subclasses should <em>first</em> call this base implementation to ensure appropriate precondition checks and required associations are fulfilled,
	 *          and then perform further lookup if the return value is {@link XMLConstants#NULL_NS_URI}.
	 * @implSpec This implementation handles the mappings for the <code>xml</code> and <code>xmlns</code> prefixes as required by the API.
	 */
	@Override
	@Nonnull
	public String getNamespaceURI(final String prefix) {
		switch(checkArgumentNotNull(prefix)) { //unconventional precondition, but required by the API
			case XML_NS_PREFIX:
				return XML_NS_URI;
			case XMLNS_ATTRIBUTE:
				return XMLNS_ATTRIBUTE_NS_URI;
			default:
				return NULL_NS_URI;
		}
	}

	/**
	 * {@inheritDoc}
	 * @apiNote Subclasses should <em>first</em> call this base implementation to ensure appropriate precondition checks and required associations are fulfilled,
	 *          and then perform further lookup if the return value is <code>null</code>.
	 * @implSpec This implementation handles the mappings for the <code>xml</code> and <code>xmlns</code> prefixes as required by the API.
	 */
	@Override
	@Nullable
	public String getPrefix(final String namespaceURI) {
		switch(checkArgumentNotNull(namespaceURI)) { //unconventional precondition, but required by the API
			case XML_NS_URI:
				return XML_NS_PREFIX;
			case XMLNS_ATTRIBUTE_NS_URI:
				return XMLNS_ATTRIBUTE;
			default:
				return null;
		}
	}

	/**
	 * {@inheritDoc}
	 * @apiNote Subclasses should <em>first</em> call this base implementation to ensure appropriate precondition checks and required associations are fulfilled,
	 *          and then perform further lookup if the returned iterator is empty.
	 * @implSpec This implementation handles the mappings for the <code>xml</code> and <code>xmlns</code> prefixes as required by the API.
	 */
	@Override
	public Iterator<String> getPrefixes(final String namespaceURI) {
		switch(checkArgumentNotNull(namespaceURI)) { //unconventional precondition, but required by the API
			case XML_NS_URI:
				return new ObjectIterator<>(XML_NS_PREFIX);
			case XMLNS_ATTRIBUTE_NS_URI:
				return new ObjectIterator<>(XMLNS_ATTRIBUTE);
			default:
				return emptyIterator();
		}
	}

}
