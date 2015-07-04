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

package com.globalmentor.text.xml.processor.schema;

/**
 * The abstract class which serves as the parent to all schema components.
 * @author Garret Wilson
 * @deprecated
 */
public abstract class XMLSchemaComponent {

	//schema component types
	//primary component types
	/** Simple type definition. */
	public static final short SIMPLE_TYPE_COMPONENT = 1;
	/** Complex type definition. */
	public static final short COMPLEX_TYPE_COMPONENT = 2;
	/** Attribute declaration. */
	public static final short ATTRIBUTE_COMPONENT = 3;
	/** Element declaration. */
	public static final short ELEMENT_COMPONENT = 4;
	//secondary component types
	/** Attribute group definition. */
	public static final short ATTRIBUTE_GROUP_COMPONENT = 5;
	/** Identity-constraint definition. */
	public static final short IDENTITY_CONSTRAINT_COMPONENT = 6;
	/** Model group definition. */
	public static final short MODEL_GROUP_COMPONENT = 7; //TODO fix; this is really a model group definition; a model group is a helper component
	/** Notation declaration. */
	public static final short NOTATION_COMPONENT = 8;
	//helper component types
	/** Annotation. */
	public static final short ANNOTATION_COMPONENT = 9;
	/** Model group. */
	//TODO fix	public static final short MODEL_GROUP_COMPONENT=10;
	/** Particle. */
	//TODO fix	public static final short PARTICLE_COMPONENT=11;
	/** Wildcard. */
	//TODO fix	public static final short WILDCARD_COMPONENT=12;

	/** The type of XML schema component this is. */
	private short componentType;

	/**
	 * Returns the type of XML schema component.
	 * @return A code representing the type of the underlying schema component.
	 */
	public short getComponentType() {
		return componentType;
	}

	/** The schema to which this schema component belongs. */
	//TODO fix if needed	private XMLSchema schema;

	/** @return The schema to which this schema component belongs. */
	//TODO fix if needed		public XMLSchema getSchema() {return schema;}

	//TODO fix if needed	public XMLSchemaComponent

	/**
	 * Constructs an XML schema object.
	 * @param newComponentType The type of XML schema component this is.
	 */
	public XMLSchemaComponent(final short newComponentType) {
		componentType = newComponentType; //set the type of component this is
	}

}