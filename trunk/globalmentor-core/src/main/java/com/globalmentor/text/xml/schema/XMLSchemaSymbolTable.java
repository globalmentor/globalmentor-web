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

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.globalmentor.log.Log;

/**
 * A table which provides mappings from names to schema components, based upon the component type category (e.g. simple and complex type components fall into
 * the same type category). The table provides for symbol spaces grouped by namespace. That is, each namespace will have multiple symbol spaces (one for each
 * type), and within each will appear a separate mapping of names to schema components.
 * <p>
 * A schema's symbol table will contain components from all symbol spaces. A complex type, on the other hand, will only contain elements and attributes.
 * </p>
 * @see XMLSchema
 * @see XMLSchemaComplexTypeComponent
 * @author Garret Wilson
 * @deprecated
 */
public class XMLSchemaSymbolTable {

	/** The name of the attribute symbol space. */
	public static final String ATTRIBUTE_SYMBOL_SPACE_NAME = "attribute";
	/** The name of the attribute group symbol space. */
	public static final String ATTRIBUTE_GROUP_SYMBOL_SPACE_NAME = "attributeGroup";
	/** The name of the element symbol space. */
	public static final String ELEMENT_SYMBOL_SPACE_NAME = "element";
	/** The name of the symbol space for simple and complex types. */
	public static final String TYPE_SYMBOL_SPACE_NAME = "type";

	/**
	 * A map of namespaces keyed to namespace URIs. Each value in the map is a symbol space, represented by another map keyed to symbol space names. Each symbol
	 * space is in turn a map keyed to object name.
	 */
	private final Map namespaceSymbolSpaceMapMap = new HashMap();

	/**
	 * Returns a symbol space map for a particular namespace. If one does not exist, it will be created and added to the map.
	 * @param namespace The URI of the namespace for which a map of symbol spaces will be returned.
	 * @return A symbol space map for a particular namespace.
	 */
	private Map getSymbolSpaceMap(final String namespace) {
		//try to get the symbol space map for this namespace
		Map symbolSpaceMap = (Map)namespaceSymbolSpaceMapMap.get(namespace);
		if(symbolSpaceMap == null) { //if a symbol space map does not exist for this namespace
			symbolSpaceMap = new HashMap(); //create a new map for this namespace
			namespaceSymbolSpaceMapMap.put(namespace, symbolSpaceMap); //put the map into our map of maps
		}
		return symbolSpaceMap; //return the symbol space map we found or created
	}

	/**
	 * Returns a symbols space (a map of schema components keyed by name) for a particular symbol category inside a particular namespace. If a symbol space for
	 * this symbol category does not exist, it will be created and added to the map.
	 * @param namespace The URI of the namespace for which a symbol space will be returned.
	 * @param symbolSpaceName A name representing the category of symbols (such as attributes or types) the returned map will represent.
	 * @return A symbols space for a particular symbol category inside a particular namespace.
	 * @see #getSymbolSpaceMap
	 */
	private Map getSymbolSpace(final String namespace, final String symbolSpaceName) {
		final Map symbolSpaceMap = getSymbolSpaceMap(namespace); //get the map of symbol spaces for this namespace
		Map symbolSpace = (Map)symbolSpaceMap.get(symbolSpaceName); //try to get the symbol space
		if(symbolSpace == null) { //if a symbol space does not exist
			symbolSpace = new HashMap(); //create a new symbol space
			symbolSpaceMap.put(symbolSpaceName, symbolSpace); //put the symbol space into the map
		}
		return symbolSpace; //return the symbol space we found or created
	}

	/**
	 * Returns a read-only collection of all components in a symbol space. The returned collection, while read-only, is backed by the symbol space and will
	 * reflect any modifications to the symbol space. If a symbol space for this symbol category does not exist, it will be created and added to the map.
	 * @param namespace The URI of the namespace for which symbols will be returned.
	 * @param symbolSpaceName A name representing the category of symbols (such as attributes or types) the returned collection will represent.
	 * @return A collection of all components in the symbol space.
	 * @see #getSymbolSpace
	 */
	public Collection getComponents(final String namespace, final String symbolSpaceName) {
		//get the values of the symbol space and turn it into an unmodifiable collection
		return Collections.unmodifiableCollection(getSymbolSpace(namespace, symbolSpaceName).values());
	}

	/**
	 * Returns a named schema component from the table. The component resides inside a particular symbol space which in turn resides inside a particular
	 * namespace.
	 * @param namespace The URI of the namespace of the component.
	 * @param symbolSpaceName A name representing the category of symbols (such as attributes or types) the component is in.
	 * @param symbolName The name of the component to return.
	 * @return A schema component from the table, or <code>null</code> if no such schema component exists.
	 */
	public XMLSchemaNamedComponent getComponent(final String namespace, final String symbolSpaceName, final String symbolName) {
		XMLSchemaNamedComponent namedComponent = null; //assume we won't be able to find a component
		//look through the maps manually to keep from creating needless internal maps for non-existent components
		final Map symbolSpaceMap = (Map)namespaceSymbolSpaceMapMap.get(namespace); //try to get the correct map of symbol spaces
		if(symbolSpaceMap != null) { //if there is a symbol space map
			final Map symbolSpace = (Map)symbolSpaceMap.get(symbolSpaceName); //try to get the symbol space
			if(symbolSpace != null) { //if there is a symbol space
				namedComponent = (XMLSchemaNamedComponent)symbolSpace.get(symbolName); //get the named component from the map, if it exists
			}
		}
		return namedComponent; //return the component or null if we couldn't find it
	}

	/**
	 * Puts a named schema component into the table. The component will reside inside a particular symbol space which in turn resides inside a particular
	 * namespace.
	 * @param namespace The URI of the namespace of the component.
	 * @param namedComponent The component to store in the table.
	 * @see #getSymbolSpace
	 */
	public void putComponent(final String namespace, final XMLSchemaNamedComponent namedComponent) {
		final String symbolSpaceName = getSymbolSpaceName(namedComponent); //get the name of the symbol space to use for this component
		final Map symbolSpace = getSymbolSpace(namespace, symbolSpaceName); //get the symbol space for this component
		symbolSpace.put(namedComponent.getName(), namedComponent); //store the component in the map
	}

	/** Default constructor. */
	public XMLSchemaSymbolTable() {
	}

	/**
	 * Returns the name of the symbol space used for the named component.
	 * @param namedComponent The component to be stored in a symbol space.
	 * @return The symbol space in which the component should be stored.
	 */
	public static String getSymbolSpaceName(final XMLSchemaNamedComponent namedComponent) {
		return getSymbolSpaceName(namedComponent.getComponentType()); //return the symbol space name for the component's component type
	}

	/**
	 * Returns the name of the symbol space used for the given component type.
	 * @param componentType The type of component to be stored in a symbol space.
	 * @return The symbol space in which the component type should be stored.
	 */
	public static String getSymbolSpaceName(final short componentType) {
		switch(componentType) { //see which component type this is
			case XMLSchemaNamedComponent.ATTRIBUTE_COMPONENT: //attributes
				return ATTRIBUTE_SYMBOL_SPACE_NAME;
			case XMLSchemaNamedComponent.ATTRIBUTE_GROUP_COMPONENT: //attribute groups
				return ATTRIBUTE_GROUP_SYMBOL_SPACE_NAME;
			case XMLSchemaNamedComponent.ELEMENT_COMPONENT: //elements
				return ELEMENT_SYMBOL_SPACE_NAME;
			case XMLSchemaNamedComponent.SIMPLE_TYPE_COMPONENT: //simple types
			case XMLSchemaNamedComponent.COMPLEX_TYPE_COMPONENT: //complex types
				return TYPE_SYMBOL_SPACE_NAME;
			default:
				Log.error("Cannot find symbol space for component: " + componentType); //TODO fix for other types
				return null; //TODO fix
		}
	}

}