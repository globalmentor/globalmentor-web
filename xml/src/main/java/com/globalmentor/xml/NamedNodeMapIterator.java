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
 * Iterates through the nodes in a {@link NamedNodeMap}. The original named node map must not be modified externally during iteration.
 * @implSpec This implementation fails fast, throwing a {@link ConcurrentModificationException} if it determines on a best-effort basis that the original named
 *           node map has been modified.
 * @implSpec This implementation does not support {@link #remove()}.
 * @author Garret Wilson
 * @see NamedNodeMap
 */
public class NamedNodeMapIterator extends AbstractNodeIterator {

	private final NamedNodeMap namedNodeMap;

	/**
	 * Constructor.
	 * @param namedNodeMap The named node map to iterate through.
	 */
	public NamedNodeMapIterator(@Nonnull final NamedNodeMap namedNodeMap) {
		super(namedNodeMap.getLength());
		this.namedNodeMap = namedNodeMap;
	}

	@Override
	protected int getLength() {
		return namedNodeMap.getLength();
	}

	@Override
	protected Node getNode(final int index) {
		return namedNodeMap.item(index);
	}

}
