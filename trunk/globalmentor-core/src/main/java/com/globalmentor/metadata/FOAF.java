/*
 * Copyright © 2007 GlobalMentor, Inc. <http://www.globalmentor.com/>
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

package com.globalmentor.metadata;

import java.net.URI;

/**
 * Definition of the <a href="http://xmlns.com/foaf/0.1/">FOAF Vocabulary Specification</a>.
 * @author Garret Wilson
 * @see <a href="http://xmlns.com/foaf/0.1/">FOAF Vocabulary Specification</a>
 */
public class FOAF {

	/** The recommended prefix to the FOAF namespace. */
	public static final String FOAF_NAMESPACE_PREFIX = "foaf";
	/** The URI to the FOAF namespace. */
	public static final URI FOAF_NAMESPACE_URI = URI.create("http://xmlns.com/foaf/0.1/");

	//FOAF type names
	/** An agent (e.g. person, group, software or physical artifact). */
	public static final String AGENT_TYPE_NAME = "Agent";
	/** A church. */
	public static final String CHURCH_TYPE_NAME = "Church";
	/** A company. */
	public static final String COMPANY_TYPE_NAME = "Company";
	/** A non-governmental organization. */
	public static final String NGO_TYPE_NAME = "NGO";
	/** A non-profit organization. */
	public static final String NONPROFIT_TYPE_NAME = "Nonprofit";
	/** An organization. */
	public static final String ORGANIZATION_TYPE_NAME = "Organization";
	/** A person. */
	public static final String PERSON_TYPE_NAME = "Person";
	/** A religious organization. */
	public static final String RELIGIOUS_ORGANIZATION_TYPE_NAME = "ReligiousOrganization";
	/** A school. */
	public static final String SCHOOL_TYPE_NAME = "School";
	/** A synagogue. */
	public static final String SYNAGOGUE_TYPE_NAME = "Synagogue";
	/** A temple. */
	public static final String TEMPLE_TYPE_NAME = "Temple";
	/** A university. */
	public static final String UNIVERSITY_TYPE_NAME = "University";

	//FOAF property names
	/** The gender of this Agent (typically but not necessarily "male" or "female"). */
	public static final String GENDER_PROPERTY_NAME = "gender";
	/** A personal mailbox. */
	public static final String MBOX_PROPERTY_NAME = "mbox";
	/** A name for some thing. */
	public static final String NAME_PROPERTY_NAME = "name";

}