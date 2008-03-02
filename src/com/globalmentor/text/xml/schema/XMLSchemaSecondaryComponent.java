package com.globalmentor.text.xml.schema;

/**Represents one of the secondary schema components:
<ul>
	<li>Attribute group definitions</li>
	<li>Identity-constraint definitions</li>
	<li>Model group definitions</li>
	<li>Notation declarations</li>
</ul>
@author Garret Wilson
*/
public abstract class XMLSchemaSecondaryComponent extends XMLSchemaNamedComponent
{

	/**Constructs a secondary schema component.
	@param newComponentType The type of XML schema component this is.
//G***del if not needed	@param ownerDocument The document which owns this node.
//G***del if not needed	@see XMLDocument
	*/
	public XMLSchemaSecondaryComponent(final short newComponentType/*G***del if not needed, final XMLDocument ownerDocument*/)
	{
		super(newComponentType);  //construct the parent class
	}

}