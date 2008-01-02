package com.garretwilson.text.xml;

import com.globalmentor.java.Objects;

/**The base class for XML objects that are named&mdash;identified by
	qname and/or namespace and local name.
<p>This class can be used indepedently of the rest of the W3C DOM
	implementations in this package.</p>
*/
public class XMLNamedObject	//TODO implement NamedObject or extend DefaultNamedObject when Java gets generics; also investigate leveraging the new QualifiedName
{

	/**The qualified name of the object.*/
	private String qname=null;

	/**The name of the object.*/
//G**del	private Stname=null;

		/**@return The name of the object.*/
//G***del	public String getName() {return qname;}
		
		/**Sets the name of the object.
		@param newName The object's new name.
		*/
//G**del		protected void setName(final Object newName) {name=newName;}

	/**The namespace URI, or <code>null</code> if there is no namespace.*/
	private String namespaceURI=null;

		/**@return The namespace URI, or <code>null</code> there is no namespace.*/
		public String getNamespaceURI() {return namespaceURI;}

		/**Sets the namespace URI.
		@param newNamespaceURI The new namespace URI, or <code>null</code> if there
			should be no namespace.
		*/
		void setNamespaceURI(final String newNamespaceURI) {namespaceURI=newNamespaceURI;}

	/**The namespace prefix, or <code>null</code> if there is no prefix.*/
	private String prefix=null;

		/**Returns the namespace prefix, or <code>null</code> if there is no prefix.*/
		public String getPrefix() {return prefix;}

		/**Sets the namespace prefix of this node. The prefix is actually set in
		  <code>setName()</code>.
		<p>Note that setting this attribute changes the qualified name.</p>
		@see #setName(java.lang.String, java.lang.String)
		*/
		public void setPrefix(final String prefix)
		{
			if(!Objects.equals(getPrefix(), prefix))	//if the prefix is really changing
			{
				setName(prefix, getLocalName()); //set the prefix and the local name, and update the node name itself
			}
		}

	/**The local name of the node.*/
	private String localName=null;

		/**@return The local part of the qualified name of this node.*/
		public String getLocalName() {return localName;}

		/**Sets the node's local name.
		@param localName The local part of the qualified name of this node
		*/
		public void setLocalName(final String localName)
		{
//G***del when works			localName=newLocalName;
			if(!Objects.equals(getLocalName(), localName))	//if the local name is really changing
			{
				setName(prefix, getLocalName()); //set the prefix and the local name, and update the node name itself
			}
		}

	/**@return The qualified name of the object.
	@see DefaultNamedObject#getName()
	*/	
	public String getQName() {return qname;}
	
	/**Sets the qualified name of the object, updating the prefix and local
		name as well.
	@param qname The new qualified name of the object.
	@see DefaultNamedObject#setName(Object)
	*/
	public void setQName(final String qname)
	{
//G***del		setName(qname);	//store the qname in the name property
		this.qname=qname;	//save the qualified name
/*G***del if not needed; this is slightly more efficient
			
		  final int namespaceDividerIndex=qualifiedName.indexOf(XMLConstants.NAMESPACE_DIVIDER); //find where the namespace divider is in the name
			if(namespaceDividerIndex!=-1) //if there was a namespace prefix indicated
				setNodeName(qualifiedName.substring(0, namespaceDividerIndex), qualifiedName.substring(namespaceDividerIndex+1)); //extract the prefix and local name from the qualified name and set them
			else  //if there was no namespace prefix indicated
				setNodeName(null, qualifiedName); //set just the qualified name, which will also update the local name
*/
		prefix=XMLUtilities.getPrefix(qname);	//determine and set the prefix
		localName=XMLUtilities.getLocalName(qname);	//determine and set the local name		
	}

	/**Sets the prefix and local name of this object, updating the qualified
		name as well.
	@param newPrefix The namespace prefix of the node, or <code>null</code> for no
		prefix.
	@param newLocalName The node's local name.
	@see #setName
	*/
	protected void setName(final String newPrefix, final String newLocalName)
	{
		prefix=newPrefix;  //set the prefix
		localName=newLocalName;	//set the local name
		qname=XMLUtilities.createQualifiedName(newPrefix, newLocalName);	//create a qualified name and store it (don't call setQName(), which will needlessly reset the prefix and local name)
//G***del when works		setName(XMLUtilities.createQualifiedName(newPrefix, newLocalName));	//create a qualified name and store it (don't call setQName(), which will needlessly reset the prefix and local name)
	}

	/**Constructor specifying the namespace and qname of the object.
	@param namespaceURI The URI of the namespace, or <code>null</code>
		if there is no namespace.
	@param qname The qualified name of the object.
	*/
	public XMLNamedObject(final String namespaceURI, final String qname)
	{
//G***fix		super(qname);	//construct the parent class
		setNamespaceURI(namespaceURI);	//set the namespace URI
		this.qname=qname;	//save the qualified name
		prefix=qname!=null ? XMLUtilities.getPrefix(qname) : null;	//determine and set the prefix
		localName=qname!=null ? XMLUtilities.getLocalName(qname) : null;	//determine and set the local name		
	}
	
}
