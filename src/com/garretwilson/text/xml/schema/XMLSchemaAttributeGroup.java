package com.garretwilson.text.xml.schema;

import java.util.ArrayList;
import java.util.List;

/**Represents an attribute group in an XML schema.
@author Garret Wilson
*/
public class XMLSchemaAttributeGroup extends XMLSchemaSecondaryComponent
{

	/**The list of child components.*/
	private List childComponentList=new ArrayList();

		/**@return The list of child components, which should either be attributes
		  or attribute groups.
		@see XMLSchemaAttribute
		@see XMLSchemaAttributeGroup
		*/
		public List getChildComponentList() {return childComponentList;}

	/**Default constructor.*/
	public XMLSchemaAttributeGroup()
	{
		super(ATTRIBUTE_GROUP_COMPONENT); //construct the parent class
	}
}