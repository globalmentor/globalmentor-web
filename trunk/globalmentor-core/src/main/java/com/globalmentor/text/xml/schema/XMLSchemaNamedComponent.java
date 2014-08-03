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

package com.globalmentor.text.xml.schema;

/**
 * A schema component which can have a name.
 * @author Garret Wilson
 * @see XMLSchemaPrimaryComponent
 * @see XMLSchemaSecondaryComponent
 * @deprecated
 */
public abstract class XMLSchemaNamedComponent extends XMLSchemaComponent implements Comparable //TODO probably rename this class
{

	/** The name of the schem acomponent. */
	private String name = null;

	/**
	 * @return The name of the schema component, or <code>null</code> if a name has not yet been assigned.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the schema component.
	 * @param newName The name for the schema component.
	 */
	public void setName(final String newName) {
		name = newName;
	}

	/** The target namespace of the component. */
	private String targetNamespace = null;

	/** @return The target namespace of the component. */
	public String getTargetNamespace() {
		return targetNamespace;
	}

	/**
	 * Sets the component target namespace.
	 * @param newTargetNamespace The target namespace for the component.
	 */
	public void setTargetNamespace(final String newTargetNamepace) {
		targetNamespace = newTargetNamepace;
	}

	/** This component's annotation, or <code>null</code> if none has been assigned. */
	private XMLSchemaAnnotation annotation = null;

	/**
	 * @return The component's annotation, or <code>null</code> if none has been assigned.
	 */
	public XMLSchemaAnnotation getAnnotation() {
		return annotation;
	};

	/**
	 * Sets the component's annotation.
	 * @param newAnnotation The annotation information.
	 */
	public void setAnnotation(final XMLSchemaAnnotation newAnnotation) {
		annotation = newAnnotation;
	};

	/**
	 * Constructs a named component.
	 * @param newComponentType The type of XML schema component this is.
	 */
	public XMLSchemaNamedComponent(final short newComponentType) {
		super(newComponentType); //construct the parent class
	}

	/**
	 * Compares this named schema component to another named schema component. This method determines order based upon the combination of the target namespace and
	 * the component name. <code>null</code> namespaces are sorted appear earliest in the list.
	 * @param object The object with which to compare the component. This must be another <code>XMLSchemaNamedComponent</code> object.
	 * @return A negative integer, zero, or a positive integer as this component is less than, equal to, or greater than the specified component, respectively.
	 * @throws ClassCastException Thrown if the specified object's type is not an <code>XMLSchemaNamedComponent</code>.
	 * @see #getCompareName
	 * @see #getTargetNamespace
	 */
	public int compareTo(Object object) throws ClassCastException {
		//if our target namespace is null and the other target namespace is not null
		if(getTargetNamespace() == null && ((XMLSchemaNamedComponent)object).getTargetNamespace() != null) {
			return -1; //a null namespace is always less than a specified namespace
		}
		//if we have a target namespace, but the other target namespace is null
		else if(((XMLSchemaNamedComponent)object).getTargetNamespace() == null && getTargetNamespace() != null) {
			return 1; //a specified namespace is always greater than a null namespace
		} else
			//if both target namespaces are null or if they both have values
			return getCompareName().compareTo(((XMLSchemaNamedComponent)object).getCompareName()); //compare the compare names normally
	}

	/**
	 * @return The name used for comparing two named schema components; in this implementation, this is the combination of the target namespace and the component
	 *         name. The <code>null</code> namespace will not be included, so any comparing function must do special processing to account for this possibility.
	 */
	protected String getCompareName() {
		return (getTargetNamespace() != null ? getTargetNamespace() : "") + (getName() != null ? getName() : ""); //return TargetNamespace+Name, using "" instead of null for either
	}

}