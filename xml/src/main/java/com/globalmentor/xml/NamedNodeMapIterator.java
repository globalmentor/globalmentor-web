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

import static com.globalmentor.java.Conditions.checkState;
import static java.util.Objects.*;

import java.util.*;

import javax.annotation.*;

import org.w3c.dom.*;

/**
 * Iterates through the nodes in a {@link NamedNodeMap}. The original named node map must not be modified during iteration.
 * @implSpec This implementation fails fast, throwing a {@link ConcurrentModificationException} if it determines on a best-effort basis that the original named
 *           node map has been modified.
 * @implSpec This implementation does not support {@link #remove()}.
 * @implNote This implementation does not support attribute named node maps that create a new attribute when one is deleted; this will result in a
 *           {@link ConcurrentModificationException}.
 * @author Garret Wilson
 * @see NamedNodeMap
 */
public class NamedNodeMapIterator implements Iterator<Node> {

	private final NamedNodeMap namedNodeMap;

	private int length;

	private int index = 0;

	Node node = null;

	/**
	 * Constructor.
	 * @param namedNodeMap The named node map to iterate through.
	 */
	public NamedNodeMapIterator(@Nonnull final NamedNodeMap namedNodeMap) {
		this.namedNodeMap = requireNonNull(namedNodeMap);
		this.length = namedNodeMap.getLength();
	}

	/**
	 * {@inheritDoc}
	 * @throws ConcurrentModificationException if the size of the original named node map has changed.
	 */
	@Override
	public boolean hasNext() {
		if(namedNodeMap.getLength() != length) {
			throw new ConcurrentModificationException("Underlying named node map was modified during iteration.");
		}
		return index < length;
	}

	/**
	 * {@inheritDoc}
	 * @throws ConcurrentModificationException if the size of the original named node map has changed.
	 */
	@Override
	public Node next() {
		if(!hasNext()) {
			throw new NoSuchElementException();
		}
		node = namedNodeMap.item(index++);
		return node;
	}

	/**
	 * {@inheritDoc}
	 * @implSpec This method delegates to {@link #removeImpl()} for actual node removal.
	 */
	@Override
	public void remove() {
		checkState(node != null, "Attempt to remove from iterator without iterating to next element.");
		removeImpl(node);
		index--; //the next node is now back where we looked before
		length--; //the list has grown smaller
		//if removing the node results in a new one being created, the length discrepancy will be detected
		node = null; //there is no longer a node to remove
	}

	/**
	 * Implementation that actually removes the node from the named node map.
	 * <p>
	 * This method must not change any iterator state variables.
	 * </p>
	 * @implSpec The default implementation throws an instance of {@link UnsupportedOperationException} and performs no other action.
	 * @param node The node to remove, or <code>null</code> if there is no node available for removal.
	 * @throws UnsupportedOperationException if the {@link #remove()} operation is not supported by this iterator
	 */
	protected void removeImpl(@Nonnull final Node node) {
		throw new UnsupportedOperationException("remove");
	}

}
