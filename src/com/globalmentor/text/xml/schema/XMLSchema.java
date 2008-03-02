package com.globalmentor.text.xml.schema;

import java.util.*;

import com.globalmentor.util.Debug;

/**The class which represents an XML schema by holding a collection of schema
	components.
@author Garret Wilson
@see XMLSchemaComponent
*/
public class XMLSchema
{

	/**The global symbol table for this schema.*/
	private final XMLSchemaSymbolTable symbolTable=new XMLSchemaSymbolTable();

		/**Returns the global symbol table for this schema. This should only be
		  accessed by other classes in the XML schema package.
		@return The global symbol table for this schema.
		*/
		XMLSchemaSymbolTable getSymbolTable() {return symbolTable;}

	/**The list of top-level schema components.*/
	private final List componentList=new ArrayList();

		/**@return The list of top-level schema components.*/
		public List getComponentList() {return componentList;}  //G***this probably shouldn't be public

	/**The schema target namespace.*/
	private String targetNamespace=null;

		/**@return The schema target namespace.*/
		public String getTargetNamespace() {return targetNamespace;}

		/**Sets the schema target namespace.
		@param newTargetNamespace The target namespace for the schema.
		*/
		public void setTargetNamespace(final String newTargetNamepace) {targetNamespace=newTargetNamepace;}

	/**Default constructor.*/
	public XMLSchema()
	{
	}

	/**Adds a component to the schema. If the component is a named component, it
		is added to the correct symbol space for the target namespace. If the
		component is a typed component, its scope will be set to the schema,
		indicating global scope.
	@param schemaComponent The component to add to the schema.
//G***what about errors for duplicate symbol names?
	@see XMLSchemaNamedComponent
	@see XMLSchemaTypedComponent
	*/
	public void addComponent(final XMLSchemaComponent schemaComponent)
	{
		if(schemaComponent instanceof XMLSchemaNamedComponent)  //if this is a named component
		{
			final XMLSchemaNamedComponent namedComponent=(XMLSchemaNamedComponent)schemaComponent;  //cast the component to a named component
			final String name=namedComponent.getName(); //get the name of the component
			if(name!=null)  //if this component has a name
			{
				final String namespace=namedComponent.getTargetNamespace(); //find out which namespace the component is meant for (in XML schema documents, all top-level components will be for the same target namespace)
				getSymbolTable().putComponent(namespace, namedComponent); //put the component in our symbol table
			}
		}
		if(schemaComponent instanceof XMLSchemaTypedComponent)  //if this is a typed component
		{
			final XMLSchemaTypedComponent typedComponent=(XMLSchemaTypedComponent)schemaComponent;  //cast the component to a typed component
		  typedComponent.setScope(this);  //show that the typed component has global scope by assigning its scope to the schema
		}
		getComponentList().add(schemaComponent);  //add the component to our list
	}

	/**Returns a list of all global components in the schema of the specified type
		in the given namespace. The returned list cannot modified.
//G***del if we don't need	@param targetNamespace The URI of the requested namespace.
	@param componentType The type of component to return
	@return An unmodifiable list of schema components of the requested type.
	*/
	public List getComponentList(/*G***del if we don't need final String namespace, */final short componentType)
	{
//G***del Debug.trace("Looking for components with type: "+componentType+" out of list of components: "+getComponentList().size());

		final List componentList=new ArrayList(); //create a new list to hold the components
		final Iterator iterator=getComponentList().iterator();  //get an iterator of all components
		while(iterator.hasNext()) //while there are more components
		{
			final XMLSchemaComponent schemaComponent=(XMLSchemaComponent)iterator.next(); //get the next schema component
			if(schemaComponent.getComponentType()==componentType) //if this is the correct type of component
				componentList.add(schemaComponent); //add this component to our list
		}
		return Collections.unmodifiableList(componentList); //return the list we created
	}












	/**Returns a collection of all components in the schema from the specified
		symbol space. The collection is backed by the symbol space, so modifications
		to the symbol space will cause the collection to be modified. The returned
		collection is read-only.
	@param targetNamespace The URI of the requested namespace.
	@param symbolSpaceName The name of the symbol space from which to return
//G***del if not needed	@param componentType The type of component to return.
	@return An unmodifiable collection of schema components from the specified
		symbol space.
	*/
	public Collection getComponents(final String namespace, final String symbolSpaceName)
	{
//G***del if not needed Debug.trace("Looking for components from symbol space: "+symbolSpaceName+" out of list of components: "+getComponentList().size());
	  //get the name of the symbol space for this type of component
//G***del if not needed		final String symbolSpaceName=XMLSchemaSymbolTable.getSymbolSpaceName(componentType);
		return getSymbolTable().getComponents(namespace, symbolSpaceName);  //return all components from this symbol space inside this namespace
	}

}