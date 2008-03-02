package com.globalmentor.text.xml.schema;

/**Represents one of the primary schema components:
<ul>
	<li>Simple type definitions</li>
	<li>Complex type definitions</li>
	<li>Attribute declarations</li>
	<li>Element declarations</li>
</ul>
@author Garret Wilson
*/
public abstract class XMLSchemaPrimaryComponent extends XMLSchemaNamedComponent
{

	/**Constructs a primary schema component.
	@param newComponentType The type of XML schema component this is.
//G***del if not needed	@param ownerDocument The document which owns this node.
//G***del if not needed	@see XMLDocument
	*/
	public XMLSchemaPrimaryComponent(final short newComponentType/*G***del if not needed, final XMLDocument ownerDocument*/)
	{
		super(newComponentType);  //construct the parent class
	}

}