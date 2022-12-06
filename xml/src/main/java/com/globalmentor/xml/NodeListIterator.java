/*
 * Copyright © 2019 GlobalMentor, Inc. <https://www.globalmentor.com/>
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

package com.globalmentor.xml;

import java.util.*;

import javax.annotation.*;

import org.w3c.dom.*;

/**
 * Iterates through the nodes in a {@link NodeList}. The original node list must not be modified externally during iteration.
 * @implSpec This implementation fails fast, throwing a {@link ConcurrentModificationException} if it determines on a best-effort basis that the original node
 *           list has been modified.
 * @author Garret Wilson
 * @see NodeList
 */
public class NodeListIterator extends AbstractNodeIterator<Node> {

	private final NodeList nodeList;

	/**
	 * Constructor.
	 * @param nodeList The node list to iterate through.
	 */
	public NodeListIterator(@Nonnull final NodeList nodeList) {
		super(nodeList.getLength());
		this.nodeList = nodeList;
	}

	@Override
	protected int getLength() {
		return nodeList.getLength();
	}

	@Override
	protected Node getNode(final int index) {
		return nodeList.item(index);
	}

	/**
	 * {@inheritDoc}
	 * @implSpec This implementation removes the attribute node by calling {@link Node#removeChild(Node)} on the node's parent node.
	 * @see Node#removeChild(Node)
	 */
	@Override
	protected void removeImpl(final Node node) {
		final Node parentNode = node.getParentNode();
		if(parentNode == null) {
			throw new UnsupportedOperationException("This node list does not allow removal.");
		}
		try {
			parentNode.removeChild(node);
		} catch(final DOMException domException) { //likely `NO_MODIFICATION_ALLOWED_ERR` or `NOT_SUPPORTED_ERR` 
			throw new UnsupportedOperationException(domException); //"unsupported operation" is semantically correct for likely DOM exceptions
		}
	}

}
