package com.globalmentor.text.xml.schema;

/**Represents one of the "helper" schema components:
<ul>
	<li>Annotations</li>
	<li>Model groups</li>
	<li>Particles</li>
	<li>Wildcards</li>
</ul>
@author Garret Wilson
*/
public abstract class XMLSchemaHelperComponent extends XMLSchemaComponent
{
	/**Constructs a schema helper component.
	@param newComponentType The type of XML schema component this is.
//G***del if not needed	@param ownerDocument The document which owns this node.
//G***del if not needed	@see XMLDocument
	*/
	public XMLSchemaHelperComponent(final short newComponentType/*G***del if not needed, final XMLDocument ownerDocument*/)
	{
		super(newComponentType);  //construct the parent class
	}

}