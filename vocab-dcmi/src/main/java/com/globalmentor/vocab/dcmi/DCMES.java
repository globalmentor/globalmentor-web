/*
 * Copyright © 2007-2012 GlobalMentor, Inc. <https://www.globalmentor.com/>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.globalmentor.vocab.dcmi;

import java.net.URI;

import com.globalmentor.vocab.VocabularyTerm;

/**
 * Constants and methods used for Dublin Core Metadata Element Set (DCMES).
 * @apiNote The core Dublin Core Metadata Element Set has been incorporated into the more recent DCMI Metadata Terms.
 * @author Garret Wilson
 * @see <a href="https://www.dublincore.org/specifications/dublin-core/dces/">Dublin Core Metadata Element Set, Version 1.1</a>
 * @see <a href="https://www.dublincore.org/resources/userguide/publishing_metadata/">DCMI Publishing Metadata</a>
 * @see <a href="https://www.dublincore.org/specifications/dublin-core/dcmi-terms/">DCMI Metadata Terms</a>
 */
public class DCMES {

	/**
	 * The recommended prefix of the Dublin Core Metadata Element Set properties namespace.
	 * @see <a href="https://www.dublincore.org/specifications/dublin-core/dcmi-terms/">DCMI Metadata Terms</a>
	 */
	public static final String NAMESPACE_PREFIX = "dc";

	/**
	 * The URI to the obsolete Dublin Core Metadata Element Set 1.0 namespace.
	 * <p>
	 * <em>This namespace is now obsolete and should not be used.</em>
	 * </p>
	 * @apiNote This namespace was used in the Open eBook Publication Structure (OEBPS) 1.1 specification, for example.
	 */
	public static final URI DCMES_1_0_NAMESPACE_URI = URI.create("http://purl.org/dc/elements/1.0/");

	/**
	 * The URI to the Dublin Core Metadata Element Set 1.1 namespace.
	 * <p>
	 * <em>This is the current DCMES namespace.</em> It has not changed since 2000. Judging from the other more recent Dublin Core, which have no version number,
	 * the DCMI no longer intends to create new namespace version numbers and this will remain the DCMES namespace.
	 * </p>
	 * @apiNote The {@link #NAMESPACE_URI} constant is to be preferred over this one.
	 * @see <a href="https://www.dublincore.org/specifications/dublin-core/dcmi-terms/">DCMI Metadata Terms</a>
	 */
	public static final URI DCMES_1_1_ELEMENTS_NAMESPACE_URI = URI.create("http://purl.org/dc/elements/1.1/");

	/** The URI to the Dublin Core Metadata Element Set namespace. */
	public static final URI NAMESPACE_URI = DCMES_1_1_ELEMENTS_NAMESPACE_URI;

	/** The title of a resource. */
	public static final VocabularyTerm TERM_TITLE = VocabularyTerm.of(NAMESPACE_URI, "title");
	/** The creator of a resource. */
	public static final VocabularyTerm TERM_CREATOR = VocabularyTerm.of(NAMESPACE_URI, "creator");
	/** The subject of a resource. */
	public static final VocabularyTerm TERM_SUBJECT = VocabularyTerm.of(NAMESPACE_URI, "subject");
	/** The description of a resource. */
	public static final VocabularyTerm TERM_DESCRIPTION = VocabularyTerm.of(NAMESPACE_URI, "description");
	/** The publisher of a resource. */
	public static final VocabularyTerm TERM_PUBLISHER = VocabularyTerm.of(NAMESPACE_URI, "publisher");
	/** The contributor of a resource. */
	public static final VocabularyTerm TERM_CONTRIBUTOR = VocabularyTerm.of(NAMESPACE_URI, "contributor");
	/** The date of a resource. */
	public static final VocabularyTerm TERM_DATE = VocabularyTerm.of(NAMESPACE_URI, "date");
	/** The Dublin Core type of a resource. */
	public static final VocabularyTerm TERM_TYPE = VocabularyTerm.of(NAMESPACE_URI, "type");
	/** The format of a resource. */
	public static final VocabularyTerm TERM_FORMAT = VocabularyTerm.of(NAMESPACE_URI, "format");
	/** The Dublin Core identifier of a resource. */
	public static final VocabularyTerm TERM_IDENTIFIER = VocabularyTerm.of(NAMESPACE_URI, "identifier");
	/** The source of a resource. */
	public static final VocabularyTerm TERM_SOURCE = VocabularyTerm.of(NAMESPACE_URI, "source");
	/** The language of a resource. */
	public static final VocabularyTerm TERM_LANGUAGE = VocabularyTerm.of(NAMESPACE_URI, "language");
	/** The relation of a resource. */
	public static final VocabularyTerm TERM_RELATION = VocabularyTerm.of(NAMESPACE_URI, "relation");
	/** The coverage of a resource. */
	public static final VocabularyTerm TERM_COVERAGE = VocabularyTerm.of(NAMESPACE_URI, "coverage");
	/** The rights of a resource. */
	public static final VocabularyTerm TERM_RIGHTS = VocabularyTerm.of(NAMESPACE_URI, "rights");

}
