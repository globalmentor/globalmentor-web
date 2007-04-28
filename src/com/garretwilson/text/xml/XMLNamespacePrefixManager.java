package com.garretwilson.text.xml;

import static com.garretwilson.lang.JavaConstants.*;
import static com.garretwilson.net.URIConstants.*;

import java.net.URI;
import java.util.*;

import com.garretwilson.assess.qti.QTIConstants;
import com.garretwilson.net.http.webdav.ApacheWebDAVConstants;
import com.garretwilson.net.http.webdav.WebDAVConstants;
import com.garretwilson.rdf.RDFConstants;
import com.garretwilson.rdf.dicto.DictoConstants;
import com.garretwilson.rdf.directory.vcard.VCard;
import com.garretwilson.rdf.dublincore.DCConstants;
import com.garretwilson.rdf.foaf.FOAF;
import com.garretwilson.rdf.maqro.MAQROConstants;
import com.garretwilson.rdf.ploop.PLOOPConstants;
import com.garretwilson.rdf.rdfs.RDFSConstants;
import com.garretwilson.rdf.version.VersionConstants;
import com.garretwilson.rdf.xeb.XEBConstants;
import com.garretwilson.rdf.xpackage.FileOntologyConstants;
import com.garretwilson.rdf.xpackage.MIMEOntologyConstants;
import com.garretwilson.rdf.xpackage.XPackageConstants;
import com.garretwilson.text.xml.XMLConstants;
import com.garretwilson.text.xml.oeb.OEBConstants;
import com.garretwilson.text.xml.schema.XMLSchemaConstants;
import com.garretwilson.text.xml.xhtml.XHTMLConstants;
import com.garretwilson.text.xml.xlink.XLinkConstants;

/**Manages XML namespaces and prefixes for serialization.
This class is initialized with a default set of known namespace prefix mappings. 
<p>This class is not thread safe.</p>
@author Garret Wilson
*/
public class XMLNamespacePrefixManager
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
		registerNamespacePrefix(ApacheWebDAVConstants.APACHE_WEBDAV_PROPERTY_NAMESPACE_URI.toString(), ApacheWebDAVConstants.APACHE_WEBDAV_PROPERTY_NAMESPACE_PREFIX); //Apache WebDAV properties
		registerNamespacePrefix(DictoConstants.DICTO_NAMESPACE_URI.toString(), DictoConstants.DICTO_NAMESPACE_PREFIX); //Dicto
		registerNamespacePrefix(DCConstants.DCMI11_ELEMENTS_NAMESPACE_URI.toString(), DCConstants.DCMI_ELEMENTS_NAMESPACE_PREFIX); //Dublin Core
		registerNamespacePrefix(FOAF.FOAF_NAMESPACE_URI.toString(), FOAF.FOAF_NAMESPACE_PREFIX); //FOAF
		registerNamespacePrefix(MAQROConstants.MAQRO_NAMESPACE_URI.toString(), MAQROConstants.MAQRO_NAMESPACE_PREFIX); //MAQRO
		registerNamespacePrefix(OEBConstants.OEB1_DOCUMENT_NAMESPACE_URI.toString(), OEBConstants.OEB1_DOCUMENT_NAMESPACE_PREFIX); //OEB 1
		registerNamespacePrefix("http://globalmentor.com/namespaces/marmot#", "marmot"); //Marmot TODO link to Marmot constants when Marmot is included in normal libraries		
		registerNamespacePrefix(PLOOPConstants.PLOOP_PROPERTY_NAMESPACE_URI.toString(), PLOOPConstants.PLOOP_PROPERTY_NAMESPACE_PREFIX); //PLOOP property
		registerNamespacePrefix(QTIConstants.QTI_1_1_NAMESPACE_URI.toString(), QTIConstants.QTI_NAMESPACE_PREFIX); //QTI
		registerNamespacePrefix(RDFConstants.RDF_NAMESPACE_URI.toString(), RDFConstants.RDF_NAMESPACE_PREFIX); //RDF
		registerNamespacePrefix(RDFSConstants.RDFS_NAMESPACE_URI.toString(), RDFSConstants.RDFS_NAMESPACE_PREFIX); //RDFS
//G***add SOAP
		registerNamespacePrefix(VCard.VCARD_NAMESPACE_URI.toString(), VCard.VCARD_NAMESPACE_PREFIX); //vCard
		registerNamespacePrefix(VersionConstants.VERSION_NAMESPACE_URI.toString(), VersionConstants.VERSION_NAMESPACE_PREFIX); //version
		registerNamespacePrefix(XMLSchemaConstants.XML_SCHEMA_NAMESPACE_URI.toString(), XMLSchemaConstants.XML_SCHEMA_NAMESPACE_PREFIX); //XML Schema
		registerNamespacePrefix(XHTMLConstants.XHTML_NAMESPACE_URI.toString(), XHTMLConstants.XHTML_NAMESPACE_PREFIX); //XHTML
		registerNamespacePrefix(XLinkConstants.XLINK_NAMESPACE_URI.toString(), XLinkConstants.XLINK_NAMESPACE_PREFIX); //XLink
		registerNamespacePrefix(XMLConstants.XML_NAMESPACE_URI.toString(), XMLConstants.XML_NAMESPACE_PREFIX); //XML
		registerNamespacePrefix(XMLConstants.XML_NAMESPACE_URI.toString(), XMLConstants.XML_NAMESPACE_PREFIX); //XML namespaces
		registerNamespacePrefix(XEBConstants.XEB_NAMESPACE_URI.toString(), XEBConstants.XEB_NAMESPACE_PREFIX); //XEbook
		registerNamespacePrefix(XPackageConstants.XPACKAGE_NAMESPACE_URI.toString(), XPackageConstants.XPACKAGE_NAMESPACE_PREFIX); //XPackage
		registerNamespacePrefix(XPackageConstants.XML_ONTOLOGY_NAMESPACE_URI.toString(), XPackageConstants.XML_ONTOLOGY_NAMESPACE_PREFIX); //XPackage XML ontology
		registerNamespacePrefix(FileOntologyConstants.FILE_ONTOLOGY_NAMESPACE_URI.toString(), FileOntologyConstants.FILE_ONTOLOGY_NAMESPACE_PREFIX); //XPackage file ontology
//TODO add XPackage Unicode ontology
		registerNamespacePrefix(MIMEOntologyConstants.MIME_ONTOLOGY_NAMESPACE_URI.toString(), MIMEOntologyConstants.MIME_ONTOLOGY_NAMESPACE_PREFIX); //XPackage MIME ontology
		registerNamespacePrefix(WebDAVConstants.WEBDAV_NAMESPACE, WebDAVConstants.WEBDAV_NAMESPACE_PREFIX); //WebDAV namespace (quasi-URI)
	}

	/**The prefix, "java:", of a Java package namespace URI.*/
	private final static String JAVA_PACKAGE_NAMESPACE_URI_PREFIX=JAVA_SCHEME+SCHEME_SEPARATOR;

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
	<p>If a namespace is unrecognized, a new one will optionally be created and
		stored in the map for future use.</p>
	<p>The Java package name of any Java URIs ending in '.' will be used as the namespace prefix if possible, if none exists already.</p>
	@param namespace The namespace for which a prefix should be returned
	@param generatePrefix <code>true</code> if a prefix should be generated if no prefix is assigned to the given namespace URI.
	@return A prefix for use with the given namespace, or <code>null</code> if no prefix is assigned to the given namespace URI and <var>generatePrefix</var> is <code>false</code>.
	*/
	public String getNamespacePrefix(final String namespaceURI, boolean generatePrefix)
	{
		String prefix=namespacePrefixMap.get(namespaceURI);  //get the prefix keyed by the namespace
		if(prefix==null)	//if we didn't find a prefix, try the namespaceURI without its ending # (RDF has different URI generation rules than, for example, XML Schema, resulting in different namespace representations)
		{
				//if this URI ends with '#' and has data before that character
			if(namespaceURI!=null && namespaceURI.length()>1 && namespaceURI.charAt(namespaceURI.length()-1)==FRAGMENT_SEPARATOR)
			{
					//see if we recognize the URI without the ending '#'
				prefix=namespacePrefixMap.get(namespaceURI.substring(0, namespaceURI.length()-1));
			}			
		}
		if(prefix==null && generatePrefix)  //if there is no prefix for this namespace, and we should generate a prefix
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
	  return prefix;  //return the prefix we found or created
	}
}
