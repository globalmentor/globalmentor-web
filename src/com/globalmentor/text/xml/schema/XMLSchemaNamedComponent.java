package com.globalmentor.text.xml.schema;

/**A schema component which can have a name.
@author Garret Wilson
@see XMLSchemaPrimaryComponent
@see XMLSchemaSecondaryComponent
//G***probably rename this class
*/
public abstract class XMLSchemaNamedComponent extends XMLSchemaComponent implements Comparable
{

	/**The name of the schem acomponent.*/
	private String name=null;

		/**@return The name of the schema component, or <code>null</code> if a name
		  has not yet been assigned.
		*/
		public String getName() {return name;}

		/**Sets the name of the schema component.
		@param newName The name for the schema component.
		*/
		public void setName(final String newName) {name=newName;}

	/**The target namespace of the component.*/
	private String targetNamespace=null;

		/**@return The target namespace of the component.*/
		public String getTargetNamespace() {return targetNamespace;}

		/**Sets the component target namespace.
		@param newTargetNamespace The target namespace for the component.
		*/
		public void setTargetNamespace(final String newTargetNamepace) {targetNamespace=newTargetNamepace;}

	/**This component's annotation, or <code>null</code> if none has been assigned.*/
	private XMLSchemaAnnotation annotation=null;

		/**@return The component's annotation, or <code>null</code> if none has been
		  assigned.*/
		public XMLSchemaAnnotation getAnnotation() {return annotation;};

		/**Sets the component's annotation.
		@param newAnnotation The annotation information.
		*/
		public void setAnnotation(final XMLSchemaAnnotation newAnnotation) {annotation=newAnnotation;};

	/**Constructs a named component.
	@param newComponentType The type of XML schema component this is.
//G***del if not needed	@param ownerDocument The document which owns this node.
//G***del if not needed	@see XMLDocument
	*/
	public XMLSchemaNamedComponent(final short newComponentType/*G***del if not needed, final XMLDocument ownerDocument*/)
	{
		super(newComponentType);  //construct the parent class
	}

	/**Compares this named schema component to another named schema component.
		This method determines order based upon the combination of the target
		namespace and the component name. <code>null</code> namespaces are sorted
		appear earliest in the list.
	@param object The object with which to compare the component. This must be
		another <code>XMLSchemaNamedComponent</code> object.
	@return A negative integer, zero, or a positive integer as this component is
		less than, equal to, or greater than the specified component, respectively.
	@exception ClassCastException Thrown if the specified object's type is not
		an <code>XMLSchemaNamedComponent</code>.
	@see #getCompareName
	@see #getTargetNamespace
	*/
	public int compareTo(Object object) throws ClassCastException
	{
		//if our target namespace is null and the other target namespace is not null
		if(getTargetNamespace()==null && ((XMLSchemaNamedComponent)object).getTargetNamespace()!=null)
		{
			return -1;  //a null namespace is always less than a specified namespace
		}
			//if we have a target namespace, but the other target namespace is null
		else if(((XMLSchemaNamedComponent)object).getTargetNamespace()==null && getTargetNamespace()!=null)
		{
			return 1;  //a specified namespace is always greater than a null namespace
		}
		else  //if both target namespaces are null or if they both have values
			return getCompareName().compareTo(((XMLSchemaNamedComponent)object).getCompareName()); //compare the compare names normally
	}

	/**@return The name used for comparing two named schema components; in this
		implementation, this is the combination of the target namespace and the
		component name. The <code>null</code> namespace will not be included, so any
		comparing function must do special processing to account for this possibility.
	*/
	protected String getCompareName()
	{
		return (getTargetNamespace()!=null ? getTargetNamespace() : "")
		  + (getName()!=null ? getName() : ""); //return TargetNamespace+Name, using "" instead of null for either
	}

}