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

package com.globalmentor.net;

import java.net.URI;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import static java.util.Objects.*;

import static java.util.Collections.*;

import static com.globalmentor.net.URIs.*;

import com.globalmentor.collections.MapDecorator;

/**
 * Map managing namespace URIs and labels for serialization. Mapping labels to the <code>null</code> namespace or to the <code>null</code> label is allowed.
 * <p>
 * This class is not thread safe.
 * </p>
 * @author Garret Wilson
 */
public abstract class AbstractNamespaceLabelManager extends MapDecorator<URI, String> {

	/** The set of known namespace URIs. */
	public static final Set<URI> KNOWN_NAMESPACE_URIS;

	static { //add the default namespaces
		final Set<URI> knownNamespaceURIs = new HashSet<URI>(); //create a temporary set to fill
		knownNamespaceURIs.add(URI.create("http://apache.org/dav/props/")); //Apache WebDAV properties
		knownNamespaceURIs.add(URI.create("http://example.com/example/")); //example
		knownNamespaceURIs.add(URI.create("http://xmlns.com/foaf/0.1/")); //FOAF
		//TODO move many of these out into application-specific initializations
		knownNamespaceURIs.add(URI.create("http://urf.name/urf/")); //URF
		knownNamespaceURIs.add(URI.create("http://urf.name/default/")); //URF default
		knownNamespaceURIs.add(URI.create("http://urf.name/content/")); //URF Content
		knownNamespaceURIs.add(URI.create("http://urf.name/select/")); //URF Select
		knownNamespaceURIs.add(URI.create("http://urf.name/vcard/")); //URF VCard
		KNOWN_NAMESPACE_URIS = unmodifiableSet(knownNamespaceURIs); //store a static read-only set
	}

	/** The default map of namespace-label mappings. */
	public static final Map<URI, String> DEFAULT_NAMESPACE_URI_LABEL_MAP;

	static { //add the default labels TODO move definitions to resource file
		final Map<URI, String> tempNamespaceURILabelMap = new HashMap<URI, String>(); //create a temporary map to fill
		//add default labels for special namespace URIs
		tempNamespaceURILabelMap.put(URI.create("http://globalmentor.com/namespaces/2003/dicto#"), "dicto"); //Dicto
		tempNamespaceURILabelMap.put(URI.create("http://purl.org/dc/elements/1.1/"), "dc"); //Dublin Core
		tempNamespaceURILabelMap.put(URI.create("http://xmlns.com/foaf/0.1/"), "foaf"); //FOAF
		//TODO convert to URF		tempNamespaceURILabelMap.put(MAQRO.MAQRO_NAMESPACE_URI, MAQRO.MAQRO_NAMESPACE_PREFIX); //MAQRO
		tempNamespaceURILabelMap.put(URI.create("http://openebook.org/namespaces/oeb-document/1.0/"), "oeb1"); //OEB 1
		tempNamespaceURILabelMap.put(URI.create("http://globalmentor.com/namespaces/marmot#"), "marmot"); //Marmot TODO link to Marmot constants when Marmot is included in normal libraries
		tempNamespaceURILabelMap.put(URI.create("http://marmox.net/namespaces/content#"), "content"); //Marmox content
		//TODO del		tempNamespaceURIPrefixMap.put(PLOOP.PLOOP_PROPERTY_NAMESPACE_URI, PLOOP.PLOOP_PROPERTY_NAMESPACE_PREFIX); //PLOOP property
		//TODO fix		tempNamespaceURILabelMap.put(URI.create(QTIConstants.QTI_1_1_NAMESPACE_URI), QTIConstants.QTI_NAMESPACE_PREFIX); //QTI
		tempNamespaceURILabelMap.put(URI.create("http://www.w3.org/1999/02/22-rdf-syntax-ns#"), "rdf"); //RDF
		tempNamespaceURILabelMap.put(URI.create("http://www.w3.org/2000/01/rdf-schema#"), "rdfs"); //RDFS
		//TODO add SOAP
		//TODO del		tempNamespaceURILabelMap.put(VCard.VCARD_NAMESPACE_URI, VCard.VCARD_NAMESPACE_PREFIX); //vCard
		tempNamespaceURILabelMap.put(URI.create("http://globalmentor.com/namespaces/2003/version#"), "ver"); //version
		tempNamespaceURILabelMap.put(URI.create("http://www.w3.org/2001/XMLSchema"), "xsd"); //XML Schema
		tempNamespaceURILabelMap.put(URI.create("http://www.w3.org/1999/xhtml"), "xhtml"); //XHTML
		tempNamespaceURILabelMap.put(URI.create("http://www.w3.org/1999/xlink"), "xlink"); //XLink
		tempNamespaceURILabelMap.put(URI.create("http://www.w3.org/XML/1998/namespace"), "xml"); //XML
		tempNamespaceURILabelMap.put(URI.create("http://www.w3.org/2000/xmlns/"), "xmlns"); //XML namespaces
		//TODO del		tempNamespaceURIPrefixMap.put(FileOntologyConstants.FILE_ONTOLOGY_NAMESPACE_URI, FileOntologyConstants.FILE_ONTOLOGY_NAMESPACE_PREFIX); //XPackage file ontology
		//TODO add XPackage Unicode ontology
		//TODO del		tempNamespaceURIPrefixMap.put(MIMEOntologyConstants.MIME_ONTOLOGY_NAMESPACE_URI, MIMEOntologyConstants.MIME_ONTOLOGY_NAMESPACE_PREFIX); //XPackage MIME ontology
		for(final URI knownNamespaceURI : KNOWN_NAMESPACE_URIS) { //for each known namespace
			if(!tempNamespaceURILabelMap.containsKey(knownNamespaceURI)) { //if we haven't added a special namespace label for this namespace
				tempNamespaceURILabelMap.put(knownNamespaceURI, getName(knownNamespaceURI)); //use the name of this known namespace as its label
			}
		}
		DEFAULT_NAMESPACE_URI_LABEL_MAP = unmodifiableMap(tempNamespaceURILabelMap); //store a static read-only map
	}

	/** Default constructor using a hash map. */
	public AbstractNamespaceLabelManager() {
		this(new HashMap<URI, String>()); //construct the class with a hash map
	}

	/**
	 * Map constructor.
	 * @param map The map this map should decorate.
	 * @throws NullPointerException if the provided map is <code>null</code>.
	 */
	public AbstractNamespaceLabelManager(final Map<URI, String> map) {
		super(map); //construct the parent class
	}

	/** The atomic variable used to generate labels. */
	private final AtomicLong generatedLabelCount = new AtomicLong(0);

	/**
	 * Generates a new label unique to this manager.
	 * @return A new label unique to this manager.
	 */
	private String generateLabel() {
		return "namespace" + generatedLabelCount.incrementAndGet(); //atomically get the next counter value and use it in generating a new labek
	}

	/**
	 * Determines whether the given namespace URI is that of a recognized namespace. A namespace is recognized if a label has been associated with the given
	 * namespace URI or the default namespace URI label map has a record of this namespace.
	 * @param namespaceURI A namespace URId
	 * @return <code>true</code> if a label has been associated with the given namespace URI or if a label is known that could be associated with this namespace
	 *         URI.
	 * @see #DEFAULT_NAMESPACE_URI_LABEL_MAP
	 */
	public boolean isRecognized(final URI namespaceURI) {
		return containsKey(namespaceURI) || DEFAULT_NAMESPACE_URI_LABEL_MAP.containsKey(namespaceURI); //return whether this map or the default namespace map recognizes the given namespace URI
	}

	/**
	 * Adds a namespace URI and associates it with a default label.
	 * @param namespaceURI The namespace URI to add.
	 * @return <code>true</code> if the namespace was not previously known.
	 * @throws NullPointerException if the given namespace URI is <code>null</code>.
	 */
	public boolean addNamespaceURI(final URI namespaceURI) {
		if(!containsKey(requireNonNull(namespaceURI, "Namespace URI cannot be null."))) { //if we don't know about this namespace
			determineNamespaceLabel(namespaceURI); //determine a label for this namespace
			return true; //indicate that this is a new namespace
		} else { //if we already know about this namespace
			return false; //indicate that we already know about this namespace
		}
	}

	/**
	 * Retrieves the label to use for the given namespace. If a namespace is unrecognized (i.e. no label, including the <code>null</code> label, has been
	 * registered with the given namespace), a new one will be created and stored in the map for future use. The last package segment of any hierarchical URIs
	 * will be used as the namespace label if possible.
	 * @param namespaceURI The namespace URI for which a label should be returned
	 * @return A label for use with the given namespace, or <code>null</code> if the <code>null</code> label is assigned to the given namespace.
	 * @see #isLabel(String)
	 */
	public String determineNamespaceLabel(final URI namespaceURI) {
		String label = get(namespaceURI); //get the label keyed by the namespace, if any
		if(label == null && !containsKey(namespaceURI)) { //if we didn't find a label because the namespace wasn't registered, generate a label
			label = DEFAULT_NAMESPACE_URI_LABEL_MAP.get(namespaceURI); //look up the namespace in the map of default label mappings
			if(label == null) { //if there is still no label for this namespace, get the name of the URI
				final String name = getName(namespaceURI); //get the name identified by the URI (the last URI path sequence)
				if(name != null && isLabel(name) && !containsValue(name)) { //if the name is a valid label that we haven't yet used
					label = name; //use the name as the label
				}
				if(label == null) { //if we didn't find a label from the URI name
					label = generateLabel(); //generate a unique namespace label
				}
			}
			put(namespaceURI, label); //store the label in the map
		}
		return label; //return the label we found or created
	}

	/**
	 * Determines whether the given string is a valid label
	 * @param string The string to check for being a label.
	 * @return <code>true</code> if the given string represents a valid label.
	 * @throws NullPointerException if the given string is <code>null</code>.
	 */
	protected abstract boolean isLabel(final String string);

}
