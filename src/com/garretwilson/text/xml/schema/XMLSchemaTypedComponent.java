package com.garretwilson.text.xml.schema;

/**Represents one of the schema components that can be assigned a type:
<ul>
	<li>Attribute declarations</li>
	<li>Element declarations</li>
</ul>
This should not be confused with <code>XMLSchemaTypeComponent</code>, which
actually represents a one of the types this component can be assigned.
@see XMLSchemaTypeComponent
@author Garret Wilson
*/
public abstract class XMLSchemaTypedComponent extends XMLSchemaPrimaryComponent
{

	/**The name of the type of this component.*/
	private String typeName=null;

		/**@return The name of the component type, which will be the same as the
		  name of the type component if one has been assigned. G***is that how we want to do this?
		@see @getType
		*/
		public String getTypeName() {return typeName;}

		/**Sets the name of this type. Used if this comoponent references another
		  type instead of defining it.
		@param newTypeName The name of the type to which this component is assigned.
		*/
		void setTypeName(final String newTypeName) {typeName=newTypeName;}

	/**The data type of this component, represented by another schema component.*/
	private XMLSchemaTypeComponent type;

		/**Returns the data type of this component, or <code>null</code> if the
		  type has not yet been assigned by the schema processor (e.g. this
			component references a type and the reference has not yet been resolved).
		*/
		public XMLSchemaTypeComponent getType() {return type;}

		/**Sets data type of this component. G***what about updating the type name
		@param newType The new data type for this component.
		*/
		public void setType(final XMLSchemaTypeComponent newType) {type=newType;}

	/**The object representing the scope of the component.*/
	private Object scope=null;

		/**Returns the scope of the component. If this element has not yet been
		  assigned a scope (e.g. an element is part of a model group), this returns
			<code>null</code>. Otherwise, the object returned will be a complex type
			definition or, representing global scope, the schema.
		@return The complex type definition representing the scope of the component,
			the schema if the component has global scope, or <code>null</code> if scope
			has not yet been determined.
		*/
		public Object getScope() {return scope;}

		/**Sets the scope of the component.
		@param newScope The object representing the scope of the component, either
			a complex type definition, the schema for global scope, or <code>null</code>.
		*/
		public void setScope(final Object newScope) {scope=newScope;}

	/**Constructs a schema typed component.
	@param newComponentType The type of XML schema component this is.
//G***del if not needed	@param ownerDocument The document which owns this node.
//G***del if not needed	@see XMLDocument
	*/
	public XMLSchemaTypedComponent(final short newComponentType/*G***del if not needed, final XMLDocument ownerDocument*/)
	{
		super(newComponentType);  //construct the parent class
	}

	/**Compares this typed schema component to another typed schema component.
		This method determines order based upon the combination of the target
		namespace, scope name (if present), and the component name. Global scopes
		and <code>null</code> namespaces are sorted appear earliest in the list.
//G***fix		 If
//G***fix		the object being compared is not an XMLSchemaTypedComponent, the parent
//G***fix		compare class is used
		If the scope of either object is <code>null</code>, scope will not be
		considered for comparing. The complex type scopes of locally scoped
		components should have names for sorting to occur correctly.
	@param object The object with which to compare the component. This must be
		another <code>XMLSchemaTypedComponent</code> object.
	@return A negative integer, zero, or a positive integer as this component is
		less than, equal to, or greater than the specified component, respectively.
	@exception ClassCastException Thrown if the specified object's type is not
		an <code>XMLSchemaTypedComponent</code>.
	@see #getScope
	@see #getCompareName
	*/
	public int compareTo(Object object) throws ClassCastException //G***do we want to allow comparing with a normal XMLSchemaNamedComponent?
	{
		final XMLSchemaTypedComponent typedComponent=(XMLSchemaTypedComponent)object; //cast the object to the correct type
		//if our target namespace is null and the other target namespace is not null
		if(getTargetNamespace()==null && typedComponent.getTargetNamespace()!=null)
		{
			return -1;  //a null namespace is always less than a specified namespace
		}
			//if we have a target namespace, but the other target namespace is null
		else if(typedComponent.getTargetNamespace()==null && getTargetNamespace()!=null)
		{
			return 1;  //a specified namespace is always greater than a null namespace
		}
		else  //if both target namespaces are null or if they both have values
		{
			if(getScope()!=null && typedComponent.getScope()!=null)  //make sure neither has a null scope
			{
				//if our scope is global and the other target namespace is not global
				if((getScope() instanceof XMLSchema) && !(typedComponent.getScope() instanceof XMLSchema))  //G***do we want to simply compare this to getOwnerSchema(), if we add that later?
				{
					return -1;  //a global scope is always less than a local scope
				}
					//if we have a local scope, but the other scope is global
				else if((typedComponent.getScope() instanceof XMLSchema) && !(getScope() instanceof XMLSchema))
				{
					return 1;  //a local scope is always greater than a global scope
				}
			}
				//if either scope is null, both scopes are global, or if both scopes are local, compare with the compare name
			return getCompareName().compareTo(typedComponent.getCompareName()); //compare the compare names normally
		}
	}

	/**@return The name used for comparing two typed schema components; in this
		implementation, this is the combination of the the target namespace, the
		scope name, and the component name. <code>null</code> scopes, unnamed scopes,
		and <code>null namespaces will not be included, so any comparing function
		must do special processing to account for this possibility.
	*/
	protected String getCompareName()
	{
		return (getTargetNamespace()!=null ? getTargetNamespace() : "")
			+ (getScope()!=null && !(getScope() instanceof XMLSchema) && ((XMLSchemaTypeComponent)getScope()).getName()!=null ? ((XMLSchemaTypeComponent)getScope()).getName() : "")
		  + (getName()!=null ? getName() : ""); //return TargetNamespace+Name, using "" instead of null for either
	}


}