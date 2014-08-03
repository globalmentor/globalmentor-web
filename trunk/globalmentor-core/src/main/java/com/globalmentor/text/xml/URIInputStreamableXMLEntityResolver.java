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

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import static com.globalmentor.io.Files.*;
import com.globalmentor.io.URIInputStreamable;
import static com.globalmentor.java.Objects.*;

/**
 * An entity resolver that retrieves XML entities from a {@link URIInputStreamable}. This implementation looks up and returns special predefined entities
 * locally instead of actually retrieving them from the {@link URIInputStreamable}. This implementation searches for resource files within this package that
 * have the same name as the public ID, with illegal characters replaced with "^XX", where "XX" is a hex code. For example, the entity public ID
 * "-//W3C//DTD XHTML 1.1//EN" would be stored in this package under the filename "-^2F^2FW3C^2F^2FDTD XHTML 1.1^2F^2FEN". This is a singleton class that cannot
 * be publicly instantiated.
 * @author Garret Wilson
 */
public class URIInputStreamableXMLEntityResolver extends XMLEntityResolver {

	/** The source of the data. */
	private final URIInputStreamable source;

	/** @return The source of the data. */
	protected URIInputStreamable getSource() {
		return source;
	}

	/**
	 * Source constructor.
	 * @param source The source of the data.
	 * @throws NullPointerException if the given source is <code>null</code>.
	 */
	public URIInputStreamableXMLEntityResolver(final URIInputStreamable source) {
		this.source = checkInstance(source);
	}

	/**
	 * Resolves the given entity based upon its public and system IDs.
	 * <p>
	 * This method delegates to the source {@link URIInputStreamable} if the parent version cannot resolve the entity.
	 * @param publicID The entity putlic ID, or <code>null</code> if none was given.
	 * @param systemID The entity system ID.
	 * @return An input source describing the entity, or <code>null</code> to request that the parser open a regular URI connection to the system identifier.
	 */
	public InputSource resolveEntity(final String publicID, final String systemID) throws SAXException, IOException {
		InputSource inputSource = super.resolveEntity(publicID, systemID); //see if the parent version can resolve the entity
		if(inputSource == null && systemID != null) { //if the parent version doesn't know about the resource, and we have a system ID
			final InputStream inputStream = getSource().getInputStream(URI.create(systemID)); //get an input stream from the source
			inputSource = new InputSource(inputStream); //create an input source to the input stream
			inputSource.setPublicId(publicID); //note the public ID, if any
			inputSource.setSystemId(systemID); //note the system ID
		}
		return inputSource; //return the input source we found, if any
	}

}
