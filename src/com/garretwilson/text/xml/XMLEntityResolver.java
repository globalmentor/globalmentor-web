package com.garretwilson.text.xml;

import java.io.IOException;
import java.io.InputStream;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import static com.garretwilson.io.FileUtilities.*;

/**An entity resolver that looks up and returns special predefined entities, such as XHTML DTDs.
This implementation searches for resource files within this package that have the same name as the public ID, with illegal characters replaced with "^XX", where "XX" is a hex code.
For example, the entity public ID "-//W3C//DTD XHTML 1.1//EN" would be stored in this package under the filename "-^2F^2FW3C^2F^2FDTD XHTML 1.1^2F^2FEN".
@author Garret Wilson
*/
public class XMLEntityResolver implements EntityResolver
{

	/**Resolves the given entity based upon its public and system IDs.
	@param publicID The entity putlic ID, or <code>null</code> if none was given.
	@param systemID The entity system ID.
	@return An input source describing the entity, or <code>null</code> to request that the parser open a regular URI connection to the system identifier.
	*/
	public InputSource resolveEntity(final String publicID, final String systemID) throws SAXException, IOException
	{
		if(publicID!=null)	//if there is a public ID
		{
			final String localFilename=encodeCrossPlatformFilename(publicID);	//get the name of the file if it were to be stored locally
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
