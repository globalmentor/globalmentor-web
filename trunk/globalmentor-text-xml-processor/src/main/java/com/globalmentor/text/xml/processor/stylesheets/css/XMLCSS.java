/*
 * Copyright © 1996-2011 GlobalMentor, Inc. <http://www.globalmentor.com/>
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

package com.globalmentor.text.xml.processor.stylesheets.css;

import static com.globalmentor.text.css.CSS.*;

import java.io.*;
import java.net.URL;

import org.w3c.dom.css.*;

import com.globalmentor.io.*;
import com.globalmentor.log.Log;

/** Constants and utilities for CSS used with the XML processor implementation. */
public class XMLCSS {

	/**
	 * Loads a default stylesheet resource if one is available for the given XML namespace. A stylesheet will be found for the given namespace if it is stored as
	 * a file in the directory corresponding to this class package, with the same name as the namespace, with all invalid filename characters replaced with
	 * underscores, with an extension of "css".
	 * @param namespaceURI The namespace for which to locate a default stylesheet.
	 * @return A W3C CSS DOM tree representing the stylesheet, or <code>null</code> if no stylesheet was found or <code>namespaceURI</code> is <code>null</code>.
	 * @deprecated
	 */
	public static CSSStyleSheet getDefaultStyleSheet(final String namespaceURI) { //TODO add a URIInputStreamable argument which can get local input streams as well as external ones
		//TODO do we want to cache stylesheets? probably not
		if(namespaceURI != null) { //if a valid namespace was passed
			//convert the namespace URI to a valid filename and add a "css" extension
			final String cssFilename = Files.encodeCrossPlatformFilename(namespaceURI) + Files.FILENAME_EXTENSION_SEPARATOR + CSS_NAME_EXTENSION;
			final URL styleSheetURL = XMLCSS.class.getResource(cssFilename); //see if we can load this resource locally
			if(styleSheetURL != null) { //if we were able to find a stylesheet stored as a resource
				final XMLCSSStyleSheet styleSheet = new XMLCSSStyleSheet((com.globalmentor.text.xml.processor.XMLNode)null); //create a new CSS stylesheet that has no owner TODO make this cast use a generic Node, or make a default constructor
				try {
					//TODO XMLCSSProcessor has been updated -- see if we need to modify this code
					final InputStream inputStream = styleSheetURL.openConnection().getInputStream(); //open a connection to the URL and get an input stream from that
					final InputStreamReader inputStreamReader = new InputStreamReader(inputStream); //get an input stream reader to the stylesheet TODO what about encoding?
					final ParseReader styleSheetReader = new ParseReader(inputStreamReader, styleSheetURL); //create a parse reader reader to use to read the stylesheet
					try {
						//TODO fix			entityReader.setCurrentLineIndex(entity.getLineIndex());	//pretend we're reading where the entity was located in that file, so any errors will show the correct information
						//TODO fix			entityReader.setCurrentCharIndex(entity.getCharIndex());	//pretend we're reading where the entity was located in that file, so any errors will show the correct information
						final XMLCSSProcessor cssProcessor = new XMLCSSProcessor(); //create a new CSS processor
						cssProcessor.parseStyleSheetContent(styleSheetReader, styleSheet); //parse the stylesheet content
						return styleSheet; //return the stylesheet we parsed
					} finally {
						styleSheetReader.close(); //always close the stylesheet reader
					}
				} catch(IOException ioException) { //if anything goes wrong reading the stylesheet, that's bad but shouldn't keep the program from continuing
					Log.warn(ioException); //warn that there's was an IO problem
				}
			}
		}
		return null; //show that for some reason we couldn't load a default stylesheet for the given namespace
	}

}
