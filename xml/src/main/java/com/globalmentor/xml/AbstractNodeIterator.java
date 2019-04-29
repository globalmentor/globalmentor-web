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

import java.util.*;

import javax.annotation.*;

import org.w3c.dom.*;

/**
 * A base class for iterating through the nodes in some data structure. The underlying data structure must not be modified externally during iteration.
 * @implSpec This implementation fails fast, throwing a {@link ConcurrentModificationException} if it determines on a best-effort basis that the original data
 *           structure has been modified.
 * @implSpec This implementation does not support {@link #remove()}.
 * @param <N> The type of node supported.
 * @author Garret Wilson
 */
public abstract class AbstractNodeIterator<N extends Node> implements Iterator<N> {

	private int length;

	private int index = 0;

	N node = null;

	/**
	 * Constructor.
	 * @param initialLength The initial length of the data structure being iterator.
	 */
	public AbstractNodeIterator(final int initialLength) {
		this.length = initialLength;
	}

	/** @return The current length of the underlying data structure. */
	protected abstract int getLength();

	/**
	 * {@inheritDoc}
	 * @throws ConcurrentModificationException if the size of the underlying data structure has changed.
	 */
	@Override
	public boolean hasNext() {
		if(getLength() != length) {
			throw new ConcurrentModificationException("Underlying node data structure was modified during iteration.");
		}
		return index < length;
	}

	/**
	 * {@inheritDoc}
	 * @implSpec This implementation delegates to {@link #getNode(int)} for actual node retrieval.
	 * @throws ConcurrentModificationException if the size of the underlying data structure has changed.
	 */
	@Override
	public N next() {
		if(!hasNext()) {
			throw new NoSuchElementException();
		}
		node = getNode(index);
		if(node == null) {
			throw new NoSuchElementException("Index " + index + " unexpectedly invalid.");
		}
		index++;
		return node;
	}

	/**
	 * Retrieves the node at the given index from the underlying data structure.
	 * @implNote The requirement to return <code>null</code> if the index is invalid, while unconventional, is used to ease interfacing with the DOM API.
	 * @param index The index of the node to retrieve.
	 * @return The node at the given index, or <code>null</code> if the index is not valid.
	 */
	protected abstract @Nullable N getNode(final int index);

	/**
	 * {@inheritDoc}
	 * @implSpec This method delegates to {@link #removeImpl(Node)} for actual node removal.
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
	 * Implementation that actually removes the node from the underlying data structure.
	 * <p>
	 * This method must not change any iterator state variables.
	 * </p>
	 * @implSpec The default implementation throws an instance of {@link UnsupportedOperationException} and performs no other action.
	 * @param node The node to remove.
	 * @throws UnsupportedOperationException if the {@link #remove()} operation is not supported by this iterator
	 */
	protected void removeImpl(@Nonnull final N node) {
		throw new UnsupportedOperationException("remove");
	}

}
