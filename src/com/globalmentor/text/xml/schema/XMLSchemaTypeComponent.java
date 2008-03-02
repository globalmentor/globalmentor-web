package com.globalmentor.text.xml.schema;

/**Represents one of the schema components representing type:
<ul>
	<li>Simple type definitions</li>
	<li>Complex type definitions</li>
</ul>
This should not be confused with <code>XMLSchemaTypedComponent</code>, which
represents a component which can be assigned a type.
@see XMLSchemaTypedComponent
@author Garret Wilson
*/
public abstract class XMLSchemaTypeComponent extends XMLSchemaPrimaryComponent
{

	/**Constructs a schema type component.
	@param newComponentType The type of XML schema component this is.
//G***del if not needed	@param ownerDocument The document which owns this node.
//G***del if not needed	@see XMLDocument
	*/
	public XMLSchemaTypeComponent(final short newComponentType/*G***del if not needed, final XMLDocument ownerDocument*/)
	{
		super(newComponentType);  //construct the parent class
	}

}