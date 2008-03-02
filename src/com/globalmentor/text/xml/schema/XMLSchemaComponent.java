package com.globalmentor.text.xml.schema;

/**The abstract class which serves as the parent to all schema components.
@author Garret Wilson
*/
public abstract class XMLSchemaComponent
{

	//schema component types
//G***del	public final static short UNDEFINED_COMPONENT=0;
		//primary component types
	/**Simple type definition.*/
	public final static short SIMPLE_TYPE_COMPONENT=1;
	/**Complex type definition.*/
	public final static short COMPLEX_TYPE_COMPONENT=2;
	/**Attribute declaration.*/
	public final static short ATTRIBUTE_COMPONENT=3;
	/**Element declaration.*/
	public final static short ELEMENT_COMPONENT=4;
		//secondary component types
	/**Attribute group definition.*/
	public final static short ATTRIBUTE_GROUP_COMPONENT=5;
	/**Identity-constraint definition.*/
	public final static short IDENTITY_CONSTRAINT_COMPONENT=6;
	/**Model group definition.*/
	public final static short MODEL_GROUP_COMPONENT=7;  //G***fix; this is really a model group definition; a model group is a helper component
	/**Notation declaration.*/
	public final static short NOTATION_COMPONENT=8;
	  //helper component types
	/**Annotation.*/
	public final static short ANNOTATION_COMPONENT=9;
	/**Model group.*/
//G***fix	public final static short MODEL_GROUP_COMPONENT=10;
	/**Particle.*/
//G***fix	public final static short PARTICLE_COMPONENT=11;
	/**Wildcard.*/
//G***fix	public final static short WILDCARD_COMPONENT=12;

	/**The type of XML schema component this is.*/
	private short componentType;

		/**Returns the type of XML schema component.
		@return A code representing the type of the underlying schema component.
		*/
		public short getComponentType() {return componentType;}

	/**The schema to which this schema component belongs.*/
//G***fix if needed	private XMLSchema schema;

		/**@return The schema to which this schema component belongs.*/
//G***fix if needed		public XMLSchema getSchema() {return schema;}

//G***fix if needed	public XMLSchemaComponent

	/**Constructs an XML schema object.
	@param newComponentType The type of XML schema component this is.
//G***del if not needed	@param ownerDocument The document which owns this node.
//G***del if not needed	@see XMLDocument
	*/
	public XMLSchemaComponent(final short newComponentType/*G***del if not needed, final XMLDocument ownerDocument*/)
	{
		componentType=newComponentType;	//set the type of component this is
	}

}