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

import static java.util.Objects.*;

import java.util.*;

import javax.annotation.*;

import org.w3c.dom.*;

/**
 * Iterates through the nodes in a {@link NodeList}. The original node list must not be modified during iteration.
 * @implSpec This implementation fails fast, throwing a {@link ConcurrentModificationException} if it determines on a best-effort basis that the original node
 *           list has been modified.
 * @author Garret Wilson
 * @see NodeList
 */
public class NodeListIterator implements Iterator<Node> {

	private final NodeList nodeList;

	private final int length;

	private int index = 0;

	/**
	 * Constructor.
	 * @param nodeList The node list to iterate through.
	 */
	public NodeListIterator(@Nonnull final NodeList nodeList) {
		this.nodeList = requireNonNull(nodeList);
		this.length = nodeList.getLength();
	}

	/**
	 * {@inheritDoc}
	 * @throws ConcurrentModificationException if the size of the original node list has changed.
	 */
	@Override
	public boolean hasNext() {
		if(nodeList.getLength() != length) {
			throw new ConcurrentModificationException("Underlying node list was modified during iteration.");
		}
		return index < length;
	}

	/**
	 * {@inheritDoc}
	 * @throws ConcurrentModificationException if the size of the original node list has changed.
	 */
	@Override
	public Node next() {
		if(!hasNext()) {
			throw new NoSuchElementException();
		}
		return nodeList.item(index++);
	}

}
