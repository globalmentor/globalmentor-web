/*
 * Copyright © 1996-2008 GlobalMentor, Inc. <https://www.globalmentor.com/>
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

package com.globalmentor.xml;

import java.io.*;

import org.xml.sax.*;

import com.globalmentor.io.Filenames;

/**
 * An entity resolver that looks up and returns special predefined entities, such as XHTML DTDs.
 * <p>
 * This entity resolver uses cached copies of the following predefined entities:
 * </p>
 * <ul>
 * <li><a href="https://www.w3.org/TR/html4/strict.dtd"><code>-//W3C//DTD HTML 4.01//EN</code></a></li>
 * <li><a href="https://www.w3.org/TR/html4/frameset.dtd"><code>-//W3C//DTD HTML 4.01 Frameset//EN</code></a></li>
 * <li><a href="https://www.w3.org/TR/html4/loose.dtd"><code>-//W3C//DTD HTML 4.01 Transitional//EN</code></a></li>
 * <li><a href="https://www.w3.org/Math/DTD/mathml2/mathml2.dtd"><code>-//W3C//DTD MathML 2.0//EN</code></a></li>
 * <li><a href="https://www.w3.org/TR/2001/REC-SVG-20010904/DTD/svg10.dtd"><code>-//W3C//DTD SVG 1.0//EN</code></a></li>
 * <li><a href="https://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd"><code>-//W3C//DTD SVG 1.1//EN</code></a></li>
 * <li><a href="https://www.w3.org/Graphics/SVG/1.1/DTD/svg11-basic.dtd"><code>-//W3C//DTD SVG 1.1 Basic//EN</code></a></li>
 * <li><a href="https://www.w3.org/Graphics/SVG/1.1/DTD/svg11-tiny.dtd"><code>-//W3C//DTD SVG 1.1 Tiny//EN</code></a></li>
 * <li><a href="https://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd"><code>-//W3C//DTD XHTML 1.0 Frameset//EN</code></a></li>
 * <li><a href="https://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"><code>-//W3C//DTD XHTML 1.0 Strict//EN</code></a></li>
 * <li><a href="https://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><code>-//W3C//DTD XHTML 1.0 Transitional//EN</code></a></li>
 * <li><a href="https://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd"><code>-//W3C//DTD XHTML 1.1//EN</code></a></li>
 * <li><a href="https://www.w3.org/2002/04/xhtml-math-svg/xhtml-math-svg.dtd"><code>-//W3C//DTD XHTML 1.1 plus MathML 2.0 plus SVG 1.1//EN</code></a></li>
 * <li><a href="https://www.w3.org/TR/xhtml-basic/xhtml-basic10.dtd"><code>-//W3C//DTD XHTML Basic 1.0//EN</code></a></li>
 * <li><a href="https://www.w3.org/MarkUp/DTD/xhtml-arch-1.mod"><code>-//W3C//ELEMENTS XHTML Base Architecture 1.0//EN</code></a></li>
 * <li><a href="https://www.w3.org/MarkUp/DTD/xhtml-base-1.mod"><code>-//W3C//ELEMENTS XHTML Base Element 1.0//EN</code></a></li>
 * <li><a href="https://www.w3.org/MarkUp/DTD/xhtml-basic-form-1.mod"><code>-//W3C//ELEMENTS XHTML Basic Forms 1.0//EN</code></a></li>
 * <li><a href="https://www.w3.org/MarkUp/DTD/xhtml-basic-table-1.mod"><code>-//W3C//ELEMENTS XHTML Basic Tables 1.0//EN</code></a></li>
 * <li><a href="https://www.w3.org/MarkUp/DTD/xhtml-bdo-1.mod"><code>-//W3C//ELEMENTS XHTML BDO Element 1.0//EN</code></a></li>
 * <li><a href="https://www.w3.org/MarkUp/DTD/xhtml-bdo-1.mod"><code>-//W3C//ELEMENTS XHTML BIDI Override Element 1.0//EN</code></a></li>
 * <li><a href="https://www.w3.org/MarkUp/DTD/xhtml-blkphras-1.mod"><code>-//W3C//ELEMENTS XHTML Block Phrasal 1.0//EN</code></a></li>
 * <li><a href="https://www.w3.org/MarkUp/DTD/xhtml-blkpres-1.mod"><code>-//W3C//ELEMENTS XHTML Block Presentation 1.0//EN</code></a></li>
 * <li><a href="https://www.w3.org/MarkUp/DTD/xhtml-blkstruct-1.mod"><code>-//W3C//ELEMENTS XHTML Block Structural 1.0//EN</code></a></li>
 * <li><a href="https://www.w3.org/MarkUp/DTD/xhtml-csismap-1.mod"><code>-//W3C//ELEMENTS XHTML Client-side Image Maps 1.0//EN</code></a></li>
 * <li><a href="https://www.w3.org/MarkUp/DTD/xhtml-struct-1.mod"><code>-//W3C//ELEMENTS XHTML Document Structure 1.0//EN</code></a></li>
 * <li><a href="https://www.w3.org/MarkUp/DTD/xhtml-edit-1.mod"><code>-//W3C//ELEMENTS XHTML Editing Elements 1.0//EN</code></a></li>
 * <li><a href="https://www.w3.org/MarkUp/DTD/xhtml-edit-1.mod"><code>-//W3C//ELEMENTS XHTML Editing Markup 1.0//EN</code></a></li>
 * <li><a href="https://www.w3.org/MarkUp/DTD/xhtml-object-1.mod"><code>-//W3C//ELEMENTS XHTML Embedded Object 1.0//EN</code></a></li>
 * <li><a href="https://www.w3.org/MarkUp/DTD/xhtml-form-1.mod"><code>-//W3C//ELEMENTS XHTML Forms 1.0//EN</code></a></li>
 * <li><a href="https://www.w3.org/MarkUp/DTD/xhtml-hypertext-1.mod"><code>-//W3C//ELEMENTS XHTML Hypertext 1.0//EN</code></a></li>
 * <li><a href="https://www.w3.org/MarkUp/DTD/xhtml-image-1.mod"><code>-//W3C//ELEMENTS XHTML Images 1.0//EN</code></a></li>
 * <li><a href="https://www.w3.org/MarkUp/DTD/xhtml-iframe-1.mod"><code>-//W3C//ELEMENTS XHTML Inline Frame Element 1.0//EN</code></a></li>
 * <li><a href="https://www.w3.org/MarkUp/DTD/xhtml-inlphras-1.mod"><code>-//W3C//ELEMENTS XHTML Inline Phrasal 1.0//EN</code></a></li>
 * <li><a href="https://www.w3.org/MarkUp/DTD/xhtml-inlpres-1.mod"><code>-//W3C//ELEMENTS XHTML Inline Presentation 1.0//EN</code></a></li>
 * <li><a href="https://www.w3.org/MarkUp/DTD/xhtml-inlstruct-1.mod"><code>-//W3C//ELEMENTS XHTML Inline Structural 1.0//EN</code></a></li>
 * <li><a href="https://www.w3.org/MarkUp/DTD/xhtml-inlstyle-1.mod"><code>-//W3C//ELEMENTS XHTML Inline Style 1.0//EN</code></a></li>
 * <li><a href="https://www.w3.org/MarkUp/DTD/xhtml-link-1.mod"><code>-//W3C//ELEMENTS XHTML Link Element 1.0//EN</code></a></li>
 * <li><a href="https://www.w3.org/MarkUp/DTD/xhtml-list-1.mod"><code>-//W3C//ELEMENTS XHTML Lists 1.0//EN</code></a></li>
 * <li><a href="https://www.w3.org/MarkUp/DTD/xhtml-legacy-1.mod"><code>-//W3C//ELEMENTS XHTML Legacy Markup 1.0//EN</code></a></li>
 * <li><a href="https://www.w3.org/MarkUp/DTD/xhtml-meta-1.mod"><code>-//W3C//ELEMENTS XHTML Metainformation 1.0//EN</code></a></li>
 * <li><a href="https://www.w3.org/MarkUp/DTD/xhtml-param-1.mod"><code>-//W3C//ELEMENTS XHTML Param Element 1.0//EN</code></a></li>
 * <li><a href="https://www.w3.org/MarkUp/DTD/xhtml-pres-1.mod"><code>-//W3C//ELEMENTS XHTML Presentation 1.0//EN</code></a></li>
 * <li><a href="https://www.w3.org/MarkUp/DTD/xhtml-ruby-1.mod"><code>-//W3C//ELEMENTS XHTML Ruby 1.0//EN</code></a></li>
 * <li><a href="https://www.w3.org/MarkUp/DTD/xhtml-script-1.mod"><code>-//W3C//ELEMENTS XHTML Scripting 1.0//EN</code></a></li>
 * <li><a href="https://www.w3.org/MarkUp/DTD/xhtml-ssismap-1.mod"><code>-//W3C//ELEMENTS XHTML Server-side Image Maps 1.0//EN</code></a></li>
 * <li><a href="https://www.w3.org/MarkUp/DTD/xhtml-style-1.mod"><code>-//W3C//ELEMENTS XHTML Style Sheets 1.0//EN</code></a></li>
 * <li><a href="https://www.w3.org/MarkUp/DTD/xhtml-table-1.mod"><code>-//W3C//ELEMENTS XHTML Tables 1.0//EN</code></a></li>
 * <li><a href="https://www.w3.org/MarkUp/DTD/xhtml-target-1.mod"><code>-//W3C//ELEMENTS XHTML Target 1.0//EN</code></a></li>
 * <li><a href="https://www.w3.org/MarkUp/DTD/xhtml-text-1.mod"><code>-//W3C//ELEMENTS XHTML Text 1.0//EN</code></a></li>
 * <li><a href="https://www.w3.org/TR/html4/HTMLlat1.ent"><code>-//W3C//ENTITIES Latin1//EN//HTML</code></a></li>
 * <li><a href="https://www.w3.org/TR/xhtml1/DTD/xhtml-lat1.ent"><code>-//W3C//ENTITIES Latin 1 for XHTML//EN</code></a></li>
 * <li><a href="https://www.w3.org/Math/DTD/mathml2/mathml2-qname-1.mod"><code>-//W3C//ENTITIES MathML 2.0 Qualified Names 1.0//EN</code></a></li>
 * <li><a href="https://www.w3.org/TR/html4/HTMLspecial.ent"><code>-//W3C//ENTITIES Special//EN//HTML</code></a></li>
 * <li><a href="https://www.w3.org/TR/xhtml1/DTD/xhtml-special.ent"><code>-//W3C//ENTITIES Special for XHTML//EN</code></a></li>
 * <li><a href="https://www.w3.org/TR/html4/HTMLsymbol.ent"><code>-//W3C//ENTITIES Symbols//EN//HTML</code></a></li>
 * <li><a href="https://www.w3.org/TR/xhtml1/DTD/xhtml-symbol.ent"><code>-//W3C//ENTITIES Symbols for XHTML//EN</code></a></li>
 * <li><a href="https://www.w3.org/TR/xhtml11/DTD/xhtml11-model-1.mod"><code>-//W3C//ENTITIES XHTML 1.1 Document Model 1.0//EN</code></a></li>
 * <li><a href="https://www.w3.org/MarkUp/DTD/xhtml-charent-1.mod"><code>-//W3C//ENTITIES XHTML Character Entities 1.0//EN</code></a></li>
 * <li><a href="https://www.w3.org/MarkUp/DTD/xhtml-attribs-1.mod"><code>-//W3C//ENTITIES XHTML Common Attributes 1.0//EN</code></a></li>
 * <li><a href="https://www.w3.org/MarkUp/DTD/xhtml-datatypes-1.mod"><code>-//W3C//ENTITIES XHTML Datatypes 1.0//EN</code></a></li>
 * <li><a href="https://www.w3.org/MarkUp/DTD/xhtml-events-1.mod"><code>-//W3C//ENTITIES XHTML Intrinsic Events 1.0//EN</code></a></li>
 * <li><a href="https://www.w3.org/MarkUp/DTD/xhtml-framework-1.mod"><code>-//W3C//ENTITIES XHTML Modular Framework 1.0//EN</code></a></li>
 * <li><a href="https://www.w3.org/MarkUp/DTD/xhtml-qname-1.mod"><code>-//W3C//ENTITIES XHTML Qualified Names 1.0//EN</code></a></li>
 * <li><a href="https://www.w3.org/MarkUp/DTD/xhtml-notations-1.mod"><code>-//W3C//NOTATIONS XHTML Notations 1.0//EN</code></a></li>
 * <li><code>+//ISBN 0-9673008-1-9//DTD OEB 1.0 Document//EN</code></li>
 * <li><code>+//ISBN 0-9673008-1-9//DTD OEB 1.0 Entities//EN</code></li>
 * <li><code>+//ISBN 0-9673008-1-9//DTD OEB 1.0 Package//EN</code></li>
 * <li><code>+//ISBN 0-9673008-1-9//DTD OEB 1.0.1 Document//EN</code></li>
 * <li><code>+//ISBN 0-9673008-1-9//DTD OEB 1.0.1 Package//EN</code></li>
 * </ul>
 * @implSpec This implementation uses cached versions of the following DTDs: The <code>.ent</code> and <code>.mod</code> entities are not yet included for
 *           MathML 2.0.
 * @implSpec This implementation searches for resource files within this package that have the same name as the public ID, with illegal characters replaced with
 *           "^XX", where "XX" is a hex code. For example, the entity public ID <code>-//W3C//DTD XHTML 1.1//EN</code> would be stored in this package under the
 *           filename <code>-^2F^2FW3C^2F^2FDTD XHTML 1.1^2F^2FEN</code>.
 * @implSpec This is a singleton class that cannot be publicly instantiated.
 * @author Garret Wilson
 * @see Filenames#encodeCrossPlatformFilename(String)
 */
public class DefaultEntityResolver implements EntityResolver {

	//You can download all the entities listed in the documentation of this class and then move them to the correct resources directory
	//by running the following commands in Bash:
	//```
	//cd xml/src/main/java/com/globalmentor/xml/
	//source <(sed -nE 's/^\s*\*\s*<li><a href="([^"]+)"><code>([-+]\/\/.+)<\/code><\/a><\/li>.*$/echo Downloading \1 to \2 ... \&\& curl -s \1 -o ".\/\2"/p' DefaultEntityResolver.java | sed -E 's/([^:])\/\//\1^2F^2F/g')
	//mv ./-* ../../../../resources/com/globalmentor/xml/ # move files starting with -
	//mv ./+* ../../../../resources/com/globalmentor/xml/ # move files starting with + (if any)
	//```
	//If Windows line endings (CRLF) are desired, add an exra ` \&\& unix2dos ".\/\2"` to the first sed replacement section:
	//```
	//source <(sed -nE 's/^\s*\*\s*<li><a href="([^"]+)"><code>([-+]\/\/.+)<\/code><\/a><\/li>.*$/echo Downloading \1 to \2 ... \&\& curl -s \1 -o ".\/\2" \&\& unix2dos ".\/\2"/p' DefaultEntityResolver.java | sed -E 's/([^:])\/\//\1^2F^2F/g')
	//```
	//This script will look at all lines in the following form (without the beginning `//`, and without additional whitespace). 
	//```
	//* <li><a href="https://www.w3.org/TR/html4/strict.dtd"><code>-//W3C//DTD HTML 4.01//EN</code></a></li>
	//```
	//The files will be downloaded the current directory using curl; they should then be moved to the resources directory tree.
	//This command has been verified to work in GNU bash, version 4.4.23(1)-release (x86_64-pc-msys) on Windows 10 using Git Bash, git version 2.25.1.windows.1

	/** The singleton instance reference of this entity resolver. */
	private static final DefaultEntityResolver INSTANCE = new DefaultEntityResolver();

	/**
	 * Returns the singleton instance of this entity resolver. This method is thread safe.
	 * @return The singleton instance of this entity resolver.
	 */
	public static DefaultEntityResolver getInstance() {
		return INSTANCE;
	}

	/** No-args constructor. */
	protected DefaultEntityResolver() {
	}

	/**
	 * Resolves the given entity based upon its public and system IDs.
	 * @param publicID The entity public ID, or <code>null</code> if none was given.
	 * @param systemID The entity system ID.
	 * @return An input source describing the entity, or <code>null</code> to request that the parser open a regular URI connection to the system identifier.
	 */
	public InputSource resolveEntity(final String publicID, final String systemID) throws SAXException, IOException {
		if(publicID != null) { //if there is a public ID
			final String localFilename = Filenames.encodeCrossPlatformFilename(publicID); //get the name of the file if it were to be stored locally
			final InputStream localResourceInputStream = getClass().getResourceAsStream(localFilename); //see if we can get an input stream to the entity
			if(localResourceInputStream != null) { //if we found the resource locally
				final InputSource inputSource = new InputSource(localResourceInputStream); //create an input source to the input stream
				inputSource.setPublicId(publicID); //note the public ID (there will always be one at this point, or we couldn't have loaded the file from the resources)
				inputSource.setSystemId(systemID); //note the system ID
				return inputSource; //return the input source
			}
		}
		return null; //indicate we couldn't look up a local copy of the entity by its public ID
	}

}
