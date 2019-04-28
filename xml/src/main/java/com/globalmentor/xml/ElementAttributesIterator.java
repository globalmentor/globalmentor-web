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

import java.util.*;

import javax.annotation.*;

import org.w3c.dom.*;

/**
 * Iterates through the attributes of an element. The original element attributes must not be modified externally during iteration.
 * @implSpec This implementation fails fast, throwing a {@link ConcurrentModificationException} if it determines on a best-effort basis that the original named
 *           node map has been modified.
 * @implSpec This implementation supports {@link #remove()}.
 * @implNote This implementation does not support attribute named node maps that create a new attribute when one is deleted; this will result in a
 *           {@link ConcurrentModificationException}.
 * @author Garret Wilson
 * @see Element
 * @see Element#getAttributes()
 */
public class ElementAttributesIterator extends AbstractNamedNodeMapIterator<Attr> {

	private final Element element;

	/**
	 * Constructor.
	 * @param element The element the attributes of which to iterate through.
	 */
	public ElementAttributesIterator(@Nonnull final Element element) {
		super(element.getAttributes());
		this.element = element;
	}

	/**
	 * {@inheritDoc}
	 * @implSpec This implementation removes the attribute node by calling {@link Element#removeAttributeNode(Attr)}.
	 * @implNote This implementation relies on the fact that an element's attribute named node map will only contain {@link Attr} instance.
	 * @implNote Note that removing an attribute in some instance may result in the creation of a default attribute from a DTD; this will eventually result in a
	 *           {@link ConcurrentModificationException} later when another iterator method is called.
	 * @see Element#removeAttributeNode(Attr)
	 */
	@Override
	protected void removeImpl(final Attr node) {
		try {
			element.removeAttributeNode(node);
		} catch(final DOMException domException) { //likely `NO_MODIFICATION_ALLOWED_ERR` 
			throw new UnsupportedOperationException(domException); //"unsupported operation" is semantically correct for likely DOM exceptions
		}
	}

}
