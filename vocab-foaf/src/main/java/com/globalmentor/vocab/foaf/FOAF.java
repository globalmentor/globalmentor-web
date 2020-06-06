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

package com.globalmentor.vocab.foaf;

import java.net.URI;

import com.globalmentor.vocab.VocabularyTerm;

/**
 * Definition of the <a href="http://xmlns.com/foaf/spec/">FOAF Vocabulary Specification</a>.
 * @author Garret Wilson
 * @see <a href="http://xmlns.com/foaf/spec/">FOAF Vocabulary Specification</a>
 */
public class FOAF {

	/** The recommended prefix to the FOAF namespace. */
	public static final String NAMESPACE_PREFIX = "foaf";

	/** The URI to the FOAF namespace. */
	public static final URI NAMESPACE_URI = URI.create("http://xmlns.com/foaf/0.1/");

	//FOAF type names
	/** An agent (e.g. person, group, software or physical artifact). */
	public static final VocabularyTerm TYPE_AGENT = VocabularyTerm.of(NAMESPACE_URI, "Agent");
	/** A church. */
	public static final VocabularyTerm TYPE_CHURCH = VocabularyTerm.of(NAMESPACE_URI, "Church");
	/** A company. */
	public static final VocabularyTerm TYPE_COMPANY = VocabularyTerm.of(NAMESPACE_URI, "Company");
	/** A non-governmental organization. */
	public static final VocabularyTerm TYPE_NGO = VocabularyTerm.of(NAMESPACE_URI, "NGO");
	/** A non-profit organization. */
	public static final VocabularyTerm TYPE_NONPROFIT = VocabularyTerm.of(NAMESPACE_URI, "Nonprofit");
	/** An organization. */
	public static final VocabularyTerm TYPE_ORGANIZATION = VocabularyTerm.of(NAMESPACE_URI, "Organization");
	/** A person. */
	public static final VocabularyTerm TYPE_PERSON = VocabularyTerm.of(NAMESPACE_URI, "Person");
	/** A religious organization. */
	public static final VocabularyTerm TYPE_RELIGIOUS_ORGANIZATION = VocabularyTerm.of(NAMESPACE_URI, "ReligiousOrganization");
	/** A school. */
	public static final VocabularyTerm TYPE_SCHOOL = VocabularyTerm.of(NAMESPACE_URI, "School");
	/** A synagogue. */
	public static final VocabularyTerm TYPE_SYNAGOGUE = VocabularyTerm.of(NAMESPACE_URI, "Synagogue");
	/** A temple. */
	public static final VocabularyTerm TYPE_TEMPLE = VocabularyTerm.of(NAMESPACE_URI, "Temple");
	/** A university. */
	public static final VocabularyTerm TYPE_UNIVERSITY = VocabularyTerm.of(NAMESPACE_URI, "University");

	//FOAF property names
	/** The gender of this Agent (typically but not necessarily "male" or "female"). */
	public static final VocabularyTerm PROPERTY_GENDER = VocabularyTerm.of(NAMESPACE_URI, "gender");
	/** A personal mailbox. */
	public static final VocabularyTerm PROPERTY_MBOX = VocabularyTerm.of(NAMESPACE_URI, "mbox");
	/** A name for some thing. */
	public static final VocabularyTerm PROPERTY_NAME = VocabularyTerm.of(NAMESPACE_URI, "name");

}
