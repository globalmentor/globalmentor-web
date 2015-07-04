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

import java.util.*;

/**
 * The class which represents an XML schema by holding a collection of schema components.
 * @author Garret Wilson
 */
public class XMLSchema {

	/** The global symbol table for this schema. */
	private final XMLSchemaSymbolTable symbolTable = new XMLSchemaSymbolTable();

	/**
	 * Returns the global symbol table for this schema. This should only be accessed by other classes in the XML schema package.
	 * @return The global symbol table for this schema.
	 */
	XMLSchemaSymbolTable getSymbolTable() {
		return symbolTable;
	}

	/** The list of top-level schema components. */
	private final List componentList = new ArrayList();

	/** @return The list of top-level schema components. */
	public List getComponentList() {
		return componentList;
	} //TODO this probably shouldn't be public

	/** The schema target namespace. */
	private String targetNamespace = null;

	/** @return The schema target namespace. */
	public String getTargetNamespace() {
		return targetNamespace;
	}

	/**
	 * Sets the schema target namespace.
	 * @param newTargetNamespace The target namespace for the schema.
	 */
	public void setTargetNamespace(final String newTargetNamepace) {
		targetNamespace = newTargetNamepace;
	}

	/** Default constructor. */
	public XMLSchema() {
	}

	/**
	 * Adds a component to the schema. If the component is a named component, it is added to the correct symbol space for the target namespace. If the component
	 * is a typed component, its scope will be set to the schema, indicating global scope.
	 * @param schemaComponent The component to add to the schema. //TODO what about errors for duplicate symbol names?
	 * @see XMLSchemaNamedComponent
	 * @see XMLSchemaTypedComponent
	 */
	public void addComponent(final XMLSchemaComponent schemaComponent) {
		if(schemaComponent instanceof XMLSchemaNamedComponent) { //if this is a named component
			final XMLSchemaNamedComponent namedComponent = (XMLSchemaNamedComponent)schemaComponent; //cast the component to a named component
			final String name = namedComponent.getName(); //get the name of the component
			if(name != null) { //if this component has a name
				final String namespace = namedComponent.getTargetNamespace(); //find out which namespace the component is meant for (in XML schema documents, all top-level components will be for the same target namespace)
				getSymbolTable().putComponent(namespace, namedComponent); //put the component in our symbol table
			}
		}
		if(schemaComponent instanceof XMLSchemaTypedComponent) { //if this is a typed component
			final XMLSchemaTypedComponent typedComponent = (XMLSchemaTypedComponent)schemaComponent; //cast the component to a typed component
			typedComponent.setScope(this); //show that the typed component has global scope by assigning its scope to the schema
		}
		getComponentList().add(schemaComponent); //add the component to our list
	}

	/**
	 * Returns a list of all global components in the schema of the specified type in the given namespace. The returned list cannot modified.
	 * @param componentType The type of component to return
	 * @return An unmodifiable list of schema components of the requested type.
	 */
	public List getComponentList(final short componentType) {
		final List componentList = new ArrayList(); //create a new list to hold the components
		final Iterator iterator = getComponentList().iterator(); //get an iterator of all components
		while(iterator.hasNext()) { //while there are more components
			final XMLSchemaComponent schemaComponent = (XMLSchemaComponent)iterator.next(); //get the next schema component
			if(schemaComponent.getComponentType() == componentType) //if this is the correct type of component
				componentList.add(schemaComponent); //add this component to our list
		}
		return Collections.unmodifiableList(componentList); //return the list we created
	}

	/**
	 * Returns a collection of all components in the schema from the specified symbol space. The collection is backed by the symbol space, so modifications to the
	 * symbol space will cause the collection to be modified. The returned collection is read-only.
	 * @param targetNamespace The URI of the requested namespace.
	 * @param symbolSpaceName The name of the symbol space from which to return
	 * @return An unmodifiable collection of schema components from the specified symbol space.
	 */
	public Collection getComponents(final String namespace, final String symbolSpaceName) {
		//get the name of the symbol space for this type of component
		return getSymbolTable().getComponents(namespace, symbolSpaceName); //return all components from this symbol space inside this namespace
	}

}