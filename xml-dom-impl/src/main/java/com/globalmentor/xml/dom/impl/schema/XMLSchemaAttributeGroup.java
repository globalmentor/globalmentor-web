/*
 * Copyright © 1996-2008 GlobalMentor, Inc. <http://www.globalmentor.com/>
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

package com.globalmentor.xml.dom.impl.schema;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an attribute group in an XML schema.
 * @author Garret Wilson
 * @deprecated
 */
public class XMLSchemaAttributeGroup extends XMLSchemaSecondaryComponent {

	/** The list of child components. */
	private List childComponentList = new ArrayList();

	/**
	 * @return The list of child components, which should either be attributes or attribute groups.
	 * @see XMLSchemaAttribute
	 * @see XMLSchemaAttributeGroup
	 */
	public List getChildComponentList() {
		return childComponentList;
	}

	/** Default constructor. */
	public XMLSchemaAttributeGroup() {
		super(ATTRIBUTE_GROUP_COMPONENT); //construct the parent class
	}
}