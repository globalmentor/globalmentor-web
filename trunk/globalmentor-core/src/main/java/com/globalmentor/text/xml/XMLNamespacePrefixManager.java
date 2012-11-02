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

import java.util.*;


import static com.globalmentor.java.Java.*;

import com.globalmentor.dcmi.DCMI;
import com.globalmentor.foaf.FOAF;
import com.globalmentor.net.http.webdav.*;
import static com.globalmentor.net.URIs.*;
import com.globalmentor.rdf.RDF;
import com.globalmentor.rdf.dicto.Dicto;
import com.globalmentor.rdf.directory.vcard.RDFVCard;
import com.globalmentor.rdf.rdfs.RDFS;
import com.globalmentor.rdf.version.RDFVersion;
import com.globalmentor.rdf.xeb.RDFXEB;
import com.globalmentor.rdf.xpackage.XMLOntology;
import com.globalmentor.rdf.xpackage.XPackage;
import com.globalmentor.text.xml.oeb.OEB;
import com.globalmentor.text.xml.schema.XMLSchema;
import com.globalmentor.text.xml.xhtml.XHTML;
import com.globalmentor.text.xml.xlink.XLink;

/**Manages XML namespaces and prefixes for serialization.
This class is initialized with a default set of known namespace prefix mappings. 
Mapping prefixes to the <code>null</code> namespace or to the <code>null</code> prefix is allowed.
<p>This class is not thread safe.</p>
@author Garret Wilson
@deprecated
*/
public class XMLNamespacePrefixManager	//TODO replace with XMLNamespaceLabelManager
{

	/**The map of XML serialization prefixes, keyed to namespacee.
	We must use strings rather than URIs, as XML serializations may have namespaces (notoriously the WebDAV namespace quasi-URI, "DAV:") that aren't technically URIs.
	*/
	private final Map<String, String> namespacePrefixMap=new HashMap<String, String>();

		/**Registers the given XML serialization prefix to be used with the given namespace.
		If a prefix is already registered with the given namespace, it is replaced with this prefix.
		@param namespace The namespace to register.
		@param prefix The XML serialization prefix to use with the given namespace.
		*/
		public void registerNamespacePrefix(final String namespace, final String prefix)
		{
			namespacePrefixMap.put(namespace, prefix);	//store the prefix in the map, keyed to the namesapce
		}

		/**Unregisters the XML serialization prefix for the given namespace.
		If no prefix is registered for the given namespace, no action occurs.
		@param namespace The namespace to unregister.
		*/
		public void unregisterNamespacePrefix(final String namespace)
		{
			namespacePrefixMap.remove(namespace);	//remove whatever prefix is registered with this namespace, if any
		}

		/**@return An iterable to the namespaces for which prefixes are registered.*/
		public Iterable<String> getRegisteredNamespaces()
		{
			return namespacePrefixMap.keySet();	//return the set of key namespaces
		}

	/**Default constructor that is initialized with a default set of namespace prefix mappings.*/
	public XMLNamespacePrefixManager()
	{
		registerNamespacePrefix(ApacheWebDAV.APACHE_WEBDAV_PROPERTY_NAMESPACE_URI.toString(), ApacheWebDAV.APACHE_WEBDAV_PROPERTY_NAMESPACE_PREFIX); //Apache WebDAV properties
		registerNamespacePrefix(Dicto.DICTO_NAMESPACE_URI.toString(), Dicto.DICTO_NAMESPACE_PREFIX); //Dicto
		registerNamespacePrefix(DCMI.DCMI11_ELEMENTS_NAMESPACE_URI.toString(), DCMI.DCMI_ELEMENTS_NAMESPACE_PREFIX); //Dublin Core
		registerNamespacePrefix(FOAF.FOAF_NAMESPACE_URI.toString(), FOAF.FOAF_NAMESPACE_PREFIX); //FOAF
//TODO convert to URF		registerNamespacePrefix(MAQRO.MAQRO_NAMESPACE_URI.toString(), MAQRO.MAQRO_NAMESPACE_PREFIX); //MAQRO
		registerNamespacePrefix(OEB.OEB1_DOCUMENT_NAMESPACE_URI.toString(), OEB.OEB1_DOCUMENT_NAMESPACE_PREFIX); //OEB 1
		registerNamespacePrefix("http://globalmentor.com/namespaces/marmot#", "marmot"); //Marmot TODO link to Marmot constants when Marmot is included in normal libraries
		registerNamespacePrefix("http://marmox.net/namespaces/content#", "content"); //Marmox content
//TODO del		registerNamespacePrefix(PLOOP.PLOOP_PROPERTY_NAMESPACE_URI.toString(), PLOOP.PLOOP_PROPERTY_NAMESPACE_PREFIX); //PLOOP property
//TODO del		registerNamespacePrefix(QTIConstants.QTI_1_1_NAMESPACE_URI.toString(), QTIConstants.QTI_NAMESPACE_PREFIX); //QTI
		registerNamespacePrefix(RDF.RDF_NAMESPACE_URI.toString(), RDF.RDF_NAMESPACE_PREFIX); //RDF
		registerNamespacePrefix(RDFS.RDFS_NAMESPACE_URI.toString(), RDFS.RDFS_NAMESPACE_PREFIX); //RDFS
//TODO add SOAP
		registerNamespacePrefix(RDFVCard.VCARD_NAMESPACE_URI.toString(), RDFVCard.VCARD_NAMESPACE_PREFIX); //vCard
		registerNamespacePrefix(RDFVersion.VERSION_NAMESPACE_URI.toString(), RDFVersion.VERSION_NAMESPACE_PREFIX); //version
		registerNamespacePrefix(XMLSchema.XML_SCHEMA_NAMESPACE_URI.toString(), XMLSchema.XML_SCHEMA_NAMESPACE_PREFIX); //XML Schema
		registerNamespacePrefix(XHTML.XHTML_NAMESPACE_URI.toString(), XHTML.XHTML_NAMESPACE_PREFIX); //XHTML
		registerNamespacePrefix(XLink.XLINK_NAMESPACE_URI.toString(), XLink.XLINK_NAMESPACE_PREFIX); //XLink
		registerNamespacePrefix(XML.XML_NAMESPACE_URI.toString(), XML.XML_NAMESPACE_PREFIX); //XML
		registerNamespacePrefix(XML.XMLNS_NAMESPACE_URI.toString(), XML.XMLNS_NAMESPACE_PREFIX); //XML namespaces
		registerNamespacePrefix(RDFXEB.XEB_NAMESPACE_URI.toString(), RDFXEB.XEB_NAMESPACE_PREFIX); //XEbook
		registerNamespacePrefix(XPackage.XPACKAGE_NAMESPACE_URI.toString(), XPackage.XPACKAGE_NAMESPACE_PREFIX); //XPackage
		registerNamespacePrefix(XMLOntology.XML_ONTOLOGY_NAMESPACE_URI.toString(), XMLOntology.XML_ONTOLOGY_NAMESPACE_PREFIX); //XPackage XML ontology
//TODO del		registerNamespacePrefix(FileOntologyConstants.FILE_ONTOLOGY_NAMESPACE_URI.toString(), FileOntologyConstants.FILE_ONTOLOGY_NAMESPACE_PREFIX); //XPackage file ontology
//TODO add XPackage Unicode ontology
//TODO del		registerNamespacePrefix(MIMEOntologyConstants.MIME_ONTOLOGY_NAMESPACE_URI.toString(), MIMEOntologyConstants.MIME_ONTOLOGY_NAMESPACE_PREFIX); //XPackage MIME ontology
		registerNamespacePrefix(WebDAV.WEBDAV_NAMESPACE, WebDAV.WEBDAV_NAMESPACE_PREFIX); //WebDAV namespace (quasi-URI)
	}

	/**The prefix, "java:", of a Java package namespace URI.*/
	private final static String JAVA_PACKAGE_NAMESPACE_URI_PREFIX=JAVA_URI_SCHEME+SCHEME_SEPARATOR;

	/**The suffix, ".", of a Java package namespace URI.*/
	private final static String JAVA_PACKAGE_NAMESPACE_URI_SUFFIX=String.valueOf(PACKAGE_SEPARATOR);


	/**Retrieves the prefix to use for the given namespace.
	If a namespace is unrecognized, a new one will be created and stored for future use.
	@param namespace The namespace for which a prefix should be returned
	@return A prefix for use with the given namespace.
	*/
	public String getNamespacePrefix(final String namespace)
	{
		return getNamespacePrefix(namespace, true);	//get a namespace prefix, generating a new one if needed 
	}

	/**Retrieves the prefix to use for the given namespace.
	<p>To accommodate vocabularies with different namespace-prefix mapping rules
		(such as RDF and and XML Schema) which result in different namespace
		representations, if the namespace URI ends in a fragment separator ('#')
		and no prefix is found, an attempt is made to look up a prefix using the
		namespace URI without that trailing fragment separator character.</p>
	<p>If a namespace is unrecognized (i.e. no prefix, inluding the <code>null</code> prefix, has been registered with the given namespace), a new one will optionally be created and
		stored in the map for future use.</p>
	<p>The Java package name of any Java URIs ending in '.' will be used as the namespace prefix if possible, if none exists already.</p>
	@param namespace The namespace for which a prefix should be returned
	@param generatePrefix <code>true</code> if a prefix should be generated if no prefix is assigned to the given namespace URI.
	@return A prefix for use with the given namespace, or <code>null</code> if no prefix is assigned to the given namespace URI and <var>generatePrefix</var> is <code>false</code>.
	*/
	public String getNamespacePrefix(final String namespaceURI, boolean generatePrefix)
	{
		String prefix=namespacePrefixMap.get(namespaceURI);  //get the prefix keyed by the namespace
		if(prefix==null && !namespacePrefixMap.containsKey(namespaceURI))	//if we didn't find a prefix because the namespace wasn't registered, try the namespaceURI without its ending # (RDF has different URI generation rules than, for example, XML Schema, resulting in different namespace representations)
		{
				//if this URI ends with '#' and has data before that character
			if(namespaceURI!=null && namespaceURI.length()>1 && namespaceURI.charAt(namespaceURI.length()-1)==FRAGMENT_SEPARATOR)
			{
					//see if we recognize the URI without the ending '#'
				prefix=namespacePrefixMap.get(namespaceURI.substring(0, namespaceURI.length()-1));
			}			
			if(prefix==null && generatePrefix)  //if there is still no prefix for this namespace, and we should generate a prefix
			{
				if(namespaceURI.startsWith(JAVA_PACKAGE_NAMESPACE_URI_PREFIX) && namespaceURI.endsWith(JAVA_PACKAGE_NAMESPACE_URI_SUFFIX))	//if this is a Java package namespace
				{
					final String tentativePrefix=namespaceURI.substring(JAVA_PACKAGE_NAMESPACE_URI_PREFIX.length(), namespaceURI.length()-1);	//remove the prefix and suffix
					if(tentativePrefix.length()>0 && !namespacePrefixMap.containsValue(tentativePrefix))	//check for the unlikely case that the original URI was "java:."; then if this prefix isn't already being used (this is an expensive operation, but necessary)
					{
						prefix=tentativePrefix;	//use the new prefix
					}
				}
				if(prefix==null)	//if we didn't find a Java package namespace prefix
				{
					prefix="namespace"+namespacePrefixMap.size()+1; //create a unique namespace prefix TODO use a constant
				}
				namespacePrefixMap.put(namespaceURI, prefix); //store the prefix in the map
			}
		}
	  return prefix;  //return the prefix we found or created
	}
}
