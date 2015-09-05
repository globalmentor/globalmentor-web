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

package com.globalmentor.w3c.spec;

import java.net.URI;

import com.globalmentor.net.ContentType;

import static com.globalmentor.java.Objects.*;

/**
 * Definition of the Resource Description Framework (RDF).
 * @author Garret Wilson
 * @see <a href="http://www.w3.org/RDF/">Resource Description Framework (RDF)</a>
 */
public class RDF {

	/** The recommended prefix to the RDF namespace. */
	public static final String NAMESPACE_PREFIX = "rdf";

	/** The URI to the RDF namespace. */
	public static final URI NAMESPACE_URI = URI.create("http://www.w3.org/1999/02/22-rdf-syntax-ns#");

	//RDF property names
	/** The pseudo-property name of a member container used only for serialization. The local name of rdf:li. */
	public static final String LI_PROPERTY_NAME = "li";
	/** The first element of a list. The local name of rdf:first. */
	public static final String FIRST_PROPERTY_NAME = "first";
	/** The rest of the elements of a list. The local name of rdf:rest. */
	public static final String REST_PROPERTY_NAME = "rest";
	/** The type of an RDF resource. The local name of rdf:type. */
	public static final String TYPE_PROPERTY_NAME = "type";
	/** The value of an RDF resource. The local name of rdf:value. (Defined in RDFS.) */
	public static final String VALUE_PROPERTY_NAME = "value";

	//RDF class names
	/** The local name of rdf:Alt. */
	public static final String ALT_CLASS_NAME = "Alt";
	/** The local name of rdf:Bag. */
	public static final String BAG_CLASS_NAME = "Bag";
	/** The local name of rdf:List. */
	public static final String LIST_CLASS_NAME = "List";
	/** The local name of rdf:Seq. */
	public static final String SEQ_CLASS_NAME = "Seq";

	//RDF datatypes
	/** The local name of the rdf:XMLLiteral. */
	public static final String XML_LITERAL_DATATYPE_NAME = "XMLLiteral";
	/** The URI of the <code>rdf:XMLLiteral</code> datatype. */
	public static final URI XML_LITERAL_DATATYPE_URI = createReferenceURI(NAMESPACE_URI, XML_LITERAL_DATATYPE_NAME);

	/**
	 * The prefix to be used when generating property names for each member of a container, originally represented by <code>&lt;li&gt;</code> in the
	 * serialization.
	 */
	public static final String CONTAINER_MEMBER_PREFIX = "_";

	//RDF predefined reference URIs
	/** The name of the <code>rdf:nil</code> resource. */
	public static final String NIL_RESOURCE_NAME = "nil";
	/** The URI of the <code>rdf:nil</code> resource. */
	public static final URI NIL_RESOURCE_URI = createReferenceURI(NAMESPACE_URI, NIL_RESOURCE_NAME);

	//RDF XML predefined class reference URIs
	/** The reference URI of the rdf:alt property. */
	public static final URI ALT_PROPERTY_REFERENCE_URI = createReferenceURI(NAMESPACE_URI, ALT_CLASS_NAME);
	/** The reference URI of the rdf:bag property. */
	public static final URI BAG_PROPERTY_REFERENCE_URI = createReferenceURI(NAMESPACE_URI, BAG_CLASS_NAME);
	/** The reference URI of the rdf:list property. */
	public static final URI LIST_PROPERTY_REFERENCE_URI = createReferenceURI(NAMESPACE_URI, LIST_CLASS_NAME);
	/** The reference URI of the rdf:seq property. */
	public static final URI SEQ_PROPERTY_REFERENCE_URI = createReferenceURI(NAMESPACE_URI, SEQ_CLASS_NAME);

	//RDF XML predefined property reference URIs
	/** The reference URI of the rdf:li property. */
	public static final URI LI_PROPERTY_REFERENCE_URI = createReferenceURI(NAMESPACE_URI, LI_PROPERTY_NAME);
	/** The reference URI of the rdf:type property. */
	public static final URI TYPE_PROPERTY_REFERENCE_URI = createReferenceURI(NAMESPACE_URI, TYPE_PROPERTY_NAME);

	/**
	 * Creates a resource reference URI from an RDF namespace URI and an RDF local name.
	 * @param namespaceURI The RDF namespace URI used in the serialization.
	 * @param localName The RDF local name used in the serialization.
	 * @return An RDF reference URI constructed from the given namespace and local name.
	 * @throws NullPointerException if the given namespace URI and/or local name is <code>null</code>.
	 */
	public static URI createReferenceURI(final URI namespaceURI, final String localName) { //TODO del if not needed throws URISyntaxException	//TODO del if not needed with QualifiedName.createReferenceURI
		//TODO check for local names that aren't valid URI characters---see QualifiedName.createReferenceURI
		final StringBuilder stringBuilder = new StringBuilder(); //create a string builder to hold the resource URI
		stringBuilder.append(checkInstance(namespaceURI, "Namespace URI cannot be null.")); //append the namespace URI
		stringBuilder.append(checkInstance(localName, "Local name cannot be null.")); //append the local name
		return URI.create(stringBuilder.toString()); //return a URI from the the string we constructed; if somehow concatenating the strings does not create a valid URI, a runtime exception will be thrown
	}

	/**
	 * Constants used in RDF+XML serializations.
	 * @author Garret Wilson
	 */
	public static class XML {

		/** The name extension for Resource Description Framework (RDF) files serialized in XML. */
		public static final String NAME_EXTENSION = "rdf";

		/**
		 * The content type for RDF serialized in XML: <code>application/rdf+xml</code>.
		 * @see <a href="https://www.ietf.org/rfc/rfc3870.txt">RFC 3870: application/rdf+xml Media Type Registration</a>
		 */
		public static final ContentType CONTENT_TYPE = ContentType.create(ContentType.APPLICATION_PRIMARY_TYPE, "rdf+xml");

		//RDF XML elements
		/** The name of the enclosing RDF element. */
		public static final String ELEMENT_RDF = "RDF";
		/** The name of the RDF description element. */
		public static final String ELEMENT_DESCRIPTION = "Description";

		//RDF XML attributes
		/** The name of the rdf:about attribute. */
		public static final String ATTRIBUTE_ABOUT = "about";
		/** The name of the rdf:ID attribute. */
		public static final String ATTRIBUTE_ID = "ID";
		/** The name of the rdf:nodeID attribute. */
		public static final String ATTRIBUTE_NODE_ID = "nodeID";
		/** The name of the rdf:parseType attribute. */
		public static final String ATTRIBUTE_PARSE_TYPE = "parseType";
		/** The name of the rdf:resource attribute. */
		public static final String ATTRIBUTE_RESOURCE = "resource";
		/** The name of the rdf:datatype attribute. */
		public static final String ATTRIBUTE_DATATYPE = "datatype";

		//RDF XML attribute values
		/** The parse type for a collection. */
		public static final String COLLECTION_PARSE_TYPE = "Collection";
		/** The parse type for an XML literal. */
		public static final String LITERAL_PARSE_TYPE = "Literal";
		/** The parse type for a resource. */
		public static final String RESOURCE_PARSE_TYPE = "Resource";

	}

}