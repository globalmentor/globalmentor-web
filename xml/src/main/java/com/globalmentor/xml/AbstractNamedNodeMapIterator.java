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
 * Abstract base class for an iterator to iterate through the nodes in a {@link NamedNodeMap}, supporting different types of nodes. The original named node map
 * must not be modified externally during iteration.
 * @implSpec This implementation fails fast, throwing a {@link ConcurrentModificationException} if it determines on a best-effort basis that the original named
 *           node map has been modified.
 * @implSpec This implementation does not support {@link #remove()}.
 * @implNote This implementation has no way to check that the given named node map will in fact return nodes of the generic type indicated.
 * @param <N> The type of node supported.
 * @author Garret Wilson
 * @see NamedNodeMap
 */
public class AbstractNamedNodeMapIterator<N extends Node> extends AbstractNodeIterator<N> {

	private final NamedNodeMap namedNodeMap;

	/**
	 * Constructor.
	 * @param namedNodeMap The named node map to iterate through.
	 */
	public AbstractNamedNodeMapIterator(@Nonnull final NamedNodeMap namedNodeMap) {
		super(namedNodeMap.getLength());
		this.namedNodeMap = namedNodeMap;
	}

	@Override
	protected int getLength() {
		return namedNodeMap.getLength();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected N getNode(final int index) {
		return (N)namedNodeMap.item(index);
	}

}
