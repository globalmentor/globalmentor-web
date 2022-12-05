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

package com.globalmentor.rdf.spec;

import java.net.URI;

/**
 * Constants and supporting methods for processing RDF schema resources.
 * @author Garret Wilson
 */
public class RDFS {

	/** The recommended prefix to the RDFS namespace. */
	public static final String RDFS_NAMESPACE_PREFIX = "rdfs";

	/** The URI to the RDFS namespace. */
	public static final URI RDFS_NAMESPACE_URI = URI.create("http://www.w3.org/2000/01/rdf-schema#");

	//RDFS core classes
	/** The class name of rdfs:Class. */
	public static final String CLASS_CLASS_NAME = "Class";
	/** The class name of rdfs:Resource. */
	public static final String RESOURCE_CLASS_NAME = "Resource";

	//RDFS core properties
	/** The property name of rdfs:isDefinedBy. */
	public static final String IS_DEFINED_BY_PROPERTY_NAME = "isDefinedBy";
	/** The property name of rdfs:seeAlso. */
	public static final String SEE_ALSO_PROPERTY_NAME = "seeAlso";
	/** The property name of rdfs:subClassOf. */
	public static final String SUB_CLASS_OF_PROPERTY_NAME = "subClassOf";
	/** The property name of rdfs:subPropertyOf. */
	public static final String SUB_PROPERTY_OF_PROPERTY_NAME = "subPropertyOf";

	//RDFS documentation properties
	/** The property name of rdfs:comment. */
	public static final String COMMENT_PROPERTY_NAME = "comment";
	/** The property name of rdfs:label. */
	public static final String LABEL_PROPERTY_NAME = "label";

}