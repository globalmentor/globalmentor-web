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
import java.util.concurrent.atomic.AtomicReference;

import org.xml.sax.*;

import com.globalmentor.io.Files;

/**An entity resolver that looks up and returns special predefined entities, such as XHTML DTDs.
This implementation searches for resource files within this package that have the same name as the public ID, with illegal characters replaced with "^XX", where "XX" is a hex code.
For example, the entity public ID "-//W3C//DTD XHTML 1.1//EN" would be stored in this package under the filename "-^2F^2FW3C^2F^2FDTD XHTML 1.1^2F^2FEN".
<p>This is a singleton class that cannot be publicly instantiated.</p>
@author Garret Wilson
@see Files#encodeCrossPlatformFilename(String)
*/
public class XMLEntityResolver implements EntityResolver
{

	/**The singleton instance reference of this entity resolver.*/
	private static AtomicReference<XMLEntityResolver> instanceReference=new AtomicReference<XMLEntityResolver>();

		/**Returns the singleton instance of this entity resolver.
		This method is thread safe.
		@return The singleton instance of this entity resolver.
		*/
		public static XMLEntityResolver getInstance()
		{
			if(instanceReference.get()==null)	//if there is no instance
			{
				instanceReference.compareAndSet(null, new XMLEntityResolver());	//update the entity resolver reference atomically
			}
			return instanceReference.get();	//return the singleton instance
		}

	protected XMLEntityResolver()
	{
	}

	/**Resolves the given entity based upon its public and system IDs.
	@param publicID The entity putlic ID, or <code>null</code> if none was given.
	@param systemID The entity system ID.
	@return An input source describing the entity, or <code>null</code> to request that the parser open a regular URI connection to the system identifier.
	*/
	public InputSource resolveEntity(final String publicID, final String systemID) throws SAXException, IOException
	{
		if(publicID!=null)	//if there is a public ID
		{
			final String localFilename=Files.encodeCrossPlatformFilename(publicID);	//get the name of the file if it were to be stored locally
			final InputStream localResourceInputStream=getClass().getResourceAsStream(localFilename);	//see if we can get an input stream to the entity
			if(localResourceInputStream!=null)	//if we found the resource locally
			{
				final InputSource inputSource=new InputSource(localResourceInputStream);	//create an input source to the input stream
				inputSource.setPublicId(publicID);	//note the public ID (there will always be one at this point, or we couldn't have loaded the file from the resources)
				inputSource.setSystemId(systemID);	//note the system ID
				return inputSource;	//return the input source
			}
		}
		return null;	//indicate we couldn't look up a local copy of the entity by its public ID
	}

}