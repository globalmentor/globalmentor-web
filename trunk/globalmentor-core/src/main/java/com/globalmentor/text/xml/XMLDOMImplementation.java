/*
 * Copyright © 1996-2008 GlobalMentor, Inc. <http://www.globalmentor.com/>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.globalmentor.text.xml;

import org.w3c.dom.*;

/**Methods for performing operations that are independent of any particular
instance of the document object model.
@author Garret Wilson
@see org.w3c.dom.DOMImplementation
@deprecated
*/
class XMLDOMImplementation implements DOMImplementation
{

	/**Tests to see if the DOM implementation implements a specific feature.
	@param feature The name of the feature to test (case-insensitive). The name
		must be an XML name.
	@param version The version number of the feature to test. In Level 2, this is
		the string "2.0". If the version is not specified, supporting any version
		of the feature causes the method to return <code>true</code>.
	@return <code>true</code> if the feature is implemented in the specified
		version, <code>false</code> otherwise.
	*/
	public boolean hasFeature(String feature, String version) {return true;}	//G***fix

    /**
     *  This method returns a specialized object which implements the 
     * specialized APIs of the specified feature and version, as specified 
     * in . The specialized object may also be obtained by using 
     * binding-specific casting methods but is not necessarily expected to, 
     * as discussed in . This method also allow the implementation to 
     * provide specialized objects which do not support the 
     * <code>DOMImplementation</code> interface. 
     * @param feature  The name of the feature requested. Note that any plus 
     *   sign "+" prepended to the name of the feature will be ignored since 
     *   it is not significant in the context of this method. 
     * @param version  This is the version number of the feature to test. 
     * @return  Returns an object which implements the specialized APIs of 
     *   the specified feature and version, if any, or <code>null</code> if 
     *   there is no object which implements interfaces associated with that 
     *   feature. If the <code>DOMObject</code> returned by this method 
     *   implements the <code>DOMImplementation</code> interface, it must 
     *   delegate to the primary core <code>DOMImplementation</code> and not 
     *   return results inconsistent with the primary core 
     *   <code>DOMImplementation</code> such as <code>hasFeature</code>, 
     *   <code>getFeature</code>, etc. 
     * @since DOM Level 3
     */
    public Object getFeature(String feature, 
                             String version) {throw new UnsupportedOperationException();}	//TODO fix for DOM 3

	/**Creates an empty <code>DocumentType</code> node. Entity declarations
		and notations are not made available. Entity reference expansions and
		default attribute additions do not occur.
	@param qualifiedName The qualified name of the document type to be created.
	@param publicId The external subset public identifier.
	@param systemId The external subset system identifier.
	@return  A new <code>DocumentType</code> node with
		<code>Node.ownerDocument</code> set to <code>null</code> .
	@exception DOMException
	<ul>
		<li>INVALID_CHARACTER_ERR: Raised if the specified qualified name
			contains an illegal character.</li>
		<li>NAMESPACE_ERR: Raised if the <code>qualifiedName</code> is malformed.</li>
	</li>
	@since DOM Level 2
	*/
	public DocumentType createDocumentType(String qualifiedName, String publicId, String systemId) throws DOMException
	{
		final XMLDocumentType documentType=new XMLDocumentType(null, qualifiedName);	//create a document type with the correctd name but no owner document
		documentType.setPublicID(publicId);	//set the public ID
		documentType.setSystemID(systemId);	//set the system ID
		return documentType;	//return the document type we created
	}

	/**Creates an XML <code>Document</code> object of the specified type with
		its document element.
	@param namespaceURI The namespace URI of the document element to create.
	@param qualifiedName The qualified name of the document element to be created.
	@param doctype The type of document to be created or <code>null</code>.
		When <code>doctype</code> is not <code>null</code> , its
		<code>Node.ownerDocument</code> attribute is set to the document being created.
	@return A new <code>Document</code> object.
	@exception DOMException
	<ul>
		<li>INVALID_CHARACTER_ERR: Raised if the specified qualified name contains
			an illegal character.</li>
		<li>NAMESPACE_ERR: Raised if the <code>qualifiedName</code> is malformed,
			if the <code>qualifiedName</code> has a prefix and the
			<code>namespaceURI</code> is <code>null</code> or an empty string,
			or if the <code>qualifiedName</code> has a prefix that is "xml" and
			the <code>namespaceURI</code> is different from "http://www.w3.org/XML/1998/namespace".</li>
		<li>WRONG_DOCUMENT_ERR: Raised if <code>doctype</code> has already been
			used with a different document or was created from a different implementation.</li>
	</ul>
	@since DOM Level 2
	*/
	public Document createDocument(String namespaceURI, String qualifiedName, DocumentType doctype) throws DOMException
	{
		//G***fix for namespaces
		//G***check to see if the doctype.getNodeName() is not null, and if so throw a WRONG_DOCUMENT_ERR
		//G***check all the other conditions that might cause errors
		final XMLDocument document=new XMLDocument();	//create a new document
		document.setNodeName(qualifiedName);	//set the name of the document
		//G***make sure that doctype is really an XMLDocumentType, or throw a WRONG_DOCUMENT_ERR if not
				//TODO probably add the document type as a child, just like the document element
		document.setXMLDocumentType((XMLDocumentType)doctype);	//set the document type
		//G***check the namespace somewhere here; maybe use document.checkNamespace()
		//G***do whatever we need to do with the namespace
			//create the document element
		final XMLElement documentElement=(XMLElement)document.createElementNS(namespaceURI, qualifiedName);
		document.getChildXMLNodeList().add(documentElement);	//add this child the document
		//G***set the document, set the parent, etc.
		documentElement.setParentXMLNode(document);	//set the parent of the added child
/*G***check to see if we need to fire events when the document element is added
		if(isEventsEnabled()) //if events are enabled
		{
			//create a new event for the inserted node
			final Event nodeInsertedEvent=XMLMutationEvent.createDOMNodeInsertedEvent(xmlNewChild, this);
			xmlNewChild.dispatchEvent(nodeInsertedEvent); //dispatch the event
			//G***dispatch the document events
		}
*/
		return document;	//return the document we created
	}

}

