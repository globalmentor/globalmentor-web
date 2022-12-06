/*
 * Copyright © 1996-2008 GlobalMentor, Inc. <https://www.globalmentor.com/>
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

package com.globalmentor.xml.dom.impl.schema;

/**
 * Represents one of the primary schema components:
 * <ul>
 * <li>Simple type definitions</li>
 * <li>Complex type definitions</li>
 * <li>Attribute declarations</li>
 * <li>Element declarations</li>
 * </ul>
 * @author Garret Wilson
 * @deprecated
 */
@Deprecated
public abstract class XMLSchemaPrimaryComponent extends XMLSchemaNamedComponent {

	/**
	 * Constructs a primary schema component.
	 * @param newComponentType The type of XML schema component this is.
	 */
	public XMLSchemaPrimaryComponent(final short newComponentType) {
		super(newComponentType); //construct the parent class
	}

}
