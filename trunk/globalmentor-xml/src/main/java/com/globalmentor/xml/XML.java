/*
 * Copyright © 1996-2014 GlobalMentor, Inc. <http://www.globalmentor.com/>
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

package com.globalmentor.xml;

import java.io.*;
import java.lang.ref.*;
import java.net.URI;
import java.nio.charset.*;
import java.util.*;

import javax.xml.parsers.*;

import static com.globalmentor.io.Charsets.*;
import static com.globalmentor.io.InputStreams.*;

import com.globalmentor.io.ByteOrderMark;
import com.globalmentor.java.*;
import com.globalmentor.java.Objects;
import com.globalmentor.model.ConfigurationException;
import com.globalmentor.model.NameValuePair;
import com.globalmentor.model.ObjectHolder;
import com.globalmentor.net.ContentType;
import com.globalmentor.net.URIs;
import com.globalmentor.text.ASCII;
import com.globalmentor.w3c.spec.HTML;
import com.globalmentor.xml.xhtml.XHTML;

import org.w3c.dom.*;
import org.w3c.dom.traversal.*;
import org.xml.sax.EntityResolver;
import org.xml.sax.SAXException;

import static com.globalmentor.java.Characters.*;
import static com.globalmentor.java.Objects.*;
import static com.globalmentor.net.ContentTypeConstants.*;
import static com.globalmentor.w3c.spec.HTML.*;
import static com.globalmentor.w3c.spec.MathML.*;
import static com.globalmentor.w3c.spec.SVG.*;
import static com.globalmentor.w3c.spec.XML.*;
import static com.globalmentor.xml.XMLStyleSheets.*;
import static java.nio.charset.StandardCharsets.*;

/**
 * Various XML manipulation functions, mostly using the DOM.
 * @author Garret Wilson
 */
public class XML {

	/**
	 * The number of bytes to use when auto-detecting character encoding.
	 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#sec-guessing-no-ext-info">XML 1.0 (Fifth Edition): F.1 Detection Without External Encoding
	 *      Information)</a>
	 */
	public static final int CHARACTER_ENCODING_AUTODETECT_BYTE_COUNT = 4;

	/** A lazily-created cache of system IDs keyed to public IDs. */
	private static Reference<Map<String, String>> systemIDMapReference = null;

	/** A lazily-created cache of system IDs keyed to public IDs. */
	protected static Map<String, String> getSystemIDMap() {
		//get the cache if we have one
		Map<String, String> systemIDMap = systemIDMapReference != null ? systemIDMapReference.get() : null;
		if(systemIDMap == null) { //if the garbage collector has reclaimed the cache
			systemIDMap = new HashMap<String, String>(); //create a new map of system IDs, and fill it with the default mappings
			systemIDMap.put(HTML_4_01_STRICT_PUBLIC_ID, HTML_4_01_STRICT_SYSTEM_ID);
			systemIDMap.put(HTML_4_01_TRANSITIONAL_PUBLIC_ID, HTML_4_01_TRANSITIONAL_SYSTEM_ID);
			systemIDMap.put(HTML_4_01_FRAMESET_PUBLIC_ID, HTML_4_01_FRAMESET_SYSTEM_ID);
			systemIDMap.put(XHTML_1_0_STRICT_PUBLIC_ID, XHTML_1_0_STRICT_SYSTEM_ID);
			systemIDMap.put(XHTML_1_0_TRANSITIONAL_PUBLIC_ID, XHTML_1_0_TRANSITIONAL_SYSTEM_ID);
			systemIDMap.put(XHTML_1_0_FRAMESET_PUBLIC_ID, XHTML_1_0_FRAMESET_SYSTEM_ID);
			systemIDMap.put(XHTML_1_1_PUBLIC_ID, XHTML_1_1_SYSTEM_ID);
			systemIDMap.put(XHTML_MATHML_SVG_PUBLIC_ID, XHTML_MATHML_SVG_SYSTEM_ID);
			systemIDMap.put(MATHML_2_0_PUBLIC_ID, MATHML_2_0_SYSTEM_ID);
			systemIDMap.put(SVG_1_0_PUBLIC_ID, SVG_1_0_SYSTEM_ID);
			systemIDMap.put(SVG_1_1_FULL_PUBLIC_ID, SVG_1_1_FULL_SYSTEM_ID);
			systemIDMap.put(SVG_1_1_BASIC_PUBLIC_ID, SVG_1_1_BASIC_SYSTEM_ID);
			systemIDMap.put(SVG_1_1_TINY_PUBLIC_ID, SVG_1_1_TINY_SYSTEM_ID);
			systemIDMapReference = new SoftReference<Map<String, String>>(systemIDMap); //create a soft reference to the map
		}
		return systemIDMap; //return the map
	}

	/**
	 * Attempts to automatically detect the character encoding of a particular input stream that supposedly contains XML data.
	 * <ul>
	 * <li>A byte order is attempted to be determined, either by an explicit byte order mark or by the order of the XML declaration start {@value #XML_DECL_START}
	 * . If no byte order can be determined, <code>null</code> is returned.</li>
	 * <li>Based upon the imputed byte order, an explicit encoding is searched for within the XML declaration. If no explicit encoding is found, the imputed byte
	 * order's assumed charset is returned. If a start {@value #XML_DECL_START} but not an end {@value #XML_DECL_END} of the XML declaration is found, an
	 * exception is thrown.</li>
	 * <li>If an explicit encoding declaration is found, it is returned, unless it is less specific than the imputed byte order. For example, if the imputed byte
	 * order is UTF-16BE but the declared encoding is UTF-16, then the charset UTF-16BE is returned.</li>
	 * <li>If there is no BOM and no XML declaration, <code>null</code> is returned; the caller should assume the default XML encoding of UTF-8.</li>
	 * </ul>
	 * @param inputStream The stream which supposedly contains XML data; this input stream must support mark/reset.
	 * @param bom Receives The actual byte order mark present, if any.
	 * @param declaredEncodingName Receives a copy of the explicitly declared name of the character encoding, if any.
	 * @return The character encoding specified in a byte order mark, the imputed byte order, or the "encoding" attribute; or <code>null</code> indicating that no
	 *         encoding was detecting, allowing the caller to assume UTF-8.
	 * @throws IllegalArgumentException if mark/reset is not supported by the given input stream.
	 * @throws IOException Thrown if an I/O error occurred, or the beginning but not the end of an XML declaration was found.
	 * @throws UnsupportedCharsetException If no support for a declared encoding is available in this instance of the Java virtual machine
	 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#sec-guessing-no-ext-info">XML 1.0 (Fifth Edition): F.1 Detection Without External Encoding
	 *      Information)</a>
	 */
	public static Charset detectXMLCharset(final InputStream inputStream, final ObjectHolder<ByteOrderMark> bom, final ObjectHolder<String> declaredEncodingName)
			throws IOException, UnsupportedCharsetException {
		checkMarkSupported(inputStream);
		//mark off enough room to read the largest BOM plus all the characters we need for imputation of a BOM
		final byte[] bytes = new byte[ByteOrderMark.MAX_BYTE_COUNT + CHARACTER_ENCODING_AUTODETECT_BYTE_COUNT]; //create an array to hold the byte order mark
		inputStream.mark(bytes.length);
		final ByteOrderMark imputedBOM;
		if(inputStream.read(bytes) == bytes.length) { //read the byte order mark; if we didn't reach the end of the data
			imputedBOM = ByteOrderMark.impute(bytes, XML_DECL_START, bom); //see if we can recognize the BOM by the beginning characters
		} else {
			imputedBOM = null;
		}
		inputStream.reset();
		if(imputedBOM == null) { //if we couldn't even impute a BOM, there aren't enough characters to detect anything
			return null;
		}
		//we now know enough about the byte order to try to find an explicit XML encoding declaration any specified character encoding
		//e.g. <?xml version="1.0" encoding="UTF-8"?>
		final int bytesPerCharacter = imputedBOM.getMinimumBytesPerCharacter(); //find out how many bytes are used for each character
		final int mostSignificantByteIndex = imputedBOM.getLeastSignificantByteIndex();
		//mark off 64 characters (in the appropriate encoding) plus the BOM, which should be enough to find an encoding declaration
		inputStream.mark(imputedBOM.getLength() + 64 * bytesPerCharacter);
		//skip the initial BOM, if actually present
		boolean eof = bom.isPresent() ? inputStream.skip(imputedBOM.getLength()) < imputedBOM.getLength() : false;
		final StringBuilder detectionBuffer = new StringBuilder();
		String encodingDeclarationValue = null;
		while(encodingDeclarationValue == null && !eof) { //stop searching when we find an encoding declaration or reach the end of the file
			int character = -1; //this will accept the next character read
			for(int i = 0; i < bytesPerCharacter && !eof; ++i) { //read each encoding group (there should be no UTF-8 encodings greater than one byte)
				if(i == mostSignificantByteIndex) { //read only the most significant byte normally
					character = inputStream.read();
					eof = character < 0;
				} else { //skip the other bytes within the encoding
					final long bytesSkipped = inputStream.skip(1);
					eof = bytesSkipped < 1;
				}
			}
			if(!eof) { //if we haven't yet reached the end of the stream
				assert character >= 0; //if we didn't reach the end of the stream, one of the bytes should have been the most significant one
				detectionBuffer.append((char)character); //add the character read to the end of our string
				//if we've read enough characters, see if this stream starts with the XML declaration "<?xml..."; if it doesn't
				if(detectionBuffer.length() == XML_DECL_START.length() && !XML_DECL_START.contentEquals(detectionBuffer)) {
					break; //stop looking for an encoding attribute, since there isn't even an XML declaration
				}
				if(CharSequences.endsWith(detectionBuffer, XML_DECL_END)) { //if we've read all of the XML declaration
					break; //stop trying to autodetect the encoding and process what we have
				}
				final int encodingDeclarationStartIndex = detectionBuffer.indexOf(ENCODINGDECL_NAME); //see where the "encoding" declaration starts (assuming we've read it yet)
				if(encodingDeclarationStartIndex >= 0) { //if we've at least found the "encoding" declaration (but perhaps not the actual value)
					int quote1Index = CharSequences.indexOf(detectionBuffer, DOUBLE_QUOTE_CHAR, encodingDeclarationStartIndex + ENCODINGDECL_NAME.length()); //see if we can find a double quote character
					if(quote1Index < 0) //if we couldn't find a double quote
						quote1Index = CharSequences.indexOf(detectionBuffer, SINGLE_QUOTE_CHAR, encodingDeclarationStartIndex + ENCODINGDECL_NAME.length()); //see if we can find a single quote character
					if(quote1Index >= 0) { //if we found either a single or double quote character
						final char quoteChar = detectionBuffer.charAt(quote1Index); //see which type of quote we found
						final int quote2Index = CharSequences.indexOf(detectionBuffer, quoteChar, quote1Index + 1); //see if we can find the matching quote
						if(quote2Index >= 0) { //if we found the second quote character
							encodingDeclarationValue = detectionBuffer.substring(quote1Index + 1, quote2Index); //get the character encoding name specified
						}
					}
				}
			}
		}
		inputStream.reset();
		if(eof) { //if we ran into the end of the stream
			throw new IOException("Unable to locate XML declaration end " + XML_DECL_END + ".");
		}
		if(encodingDeclarationValue != null) { //if a the character encoding value was explicitly given
			declaredEncodingName.setObject(encodingDeclarationValue); //indicate the exact name of the declared encoding
			final boolean isImputedBOMMoreSpecific; //see if the imputed BOM is more specific as to endianness than the declared character encoding
			switch(imputedBOM) { //see http://stackoverflow.com/q/25477854/421049
				case UTF_16LE:
				case UTF_16BE:
					isImputedBOMMoreSpecific = ASCII.equalsIgnoreCase(UTF_16_NAME, encodingDeclarationValue);
					break;
				case UTF_32LE:
				case UTF_32BE:
					isImputedBOMMoreSpecific = ASCII.equalsIgnoreCase(UTF_32_NAME, encodingDeclarationValue);
					break;
				default:
					isImputedBOMMoreSpecific = false;
			}
			if(!isImputedBOMMoreSpecific) { //if the declared character encoding is not less specific, go with that.
				return Charset.forName(encodingDeclarationValue); //return a charset for that name
			}
		}
		return imputedBOM.toCharset(); //if nothing was more specific, return the charset we imputed
	}

	/**
	 * Determines the default system ID for the given public ID.
	 * @param publicID The public ID for which a doctype system ID should be retrieved.
	 * @return The default doctype system ID corresponding to the given public ID, or <code>null</code> if the given public ID is not recognized.
	 */
	public static String getDefaultSystemID(final String publicID) {
		return getSystemIDMap().get(publicID); //return the system ID corresponding to the given public ID, if we have one
	}

	/** A lazily-created cache of content types keyed to public IDs. */
	private static Reference<Map<String, ContentType>> contentTypeMapReference = null;

	/** A lazily-created cache of content types keyed to public IDs. */
	protected static Map<String, ContentType> getContentTypeMap() {
		//get the cache if we have one
		Map<String, ContentType> contentTypeMap = contentTypeMapReference != null ? contentTypeMapReference.get() : null;
		if(contentTypeMap == null) { //if the garbage collector has reclaimed the cache
			contentTypeMap = new HashMap<String, ContentType>(); //create a new map of content types, and fill it with the default mappings
			contentTypeMap.put("-//Guise//DTD XHTML Guise 1.0//EN", XHTML_CONTENT_TYPE); //Guise XHTML DTD
			contentTypeMap.put(HTML_4_01_STRICT_PUBLIC_ID, HTML_CONTENT_TYPE);
			contentTypeMap.put(HTML_4_01_TRANSITIONAL_PUBLIC_ID, HTML_CONTENT_TYPE);
			contentTypeMap.put(HTML_4_01_FRAMESET_PUBLIC_ID, HTML_CONTENT_TYPE);
			contentTypeMap.put(XHTML_1_0_STRICT_PUBLIC_ID, XHTML_CONTENT_TYPE);
			contentTypeMap.put(XHTML_1_0_TRANSITIONAL_PUBLIC_ID, XHTML_CONTENT_TYPE);
			contentTypeMap.put(XHTML_1_0_FRAMESET_PUBLIC_ID, XHTML_CONTENT_TYPE);
			contentTypeMap.put(XHTML_1_1_PUBLIC_ID, XHTML_CONTENT_TYPE);
			contentTypeMap.put(XHTML_MATHML_SVG_PUBLIC_ID, XHTML_CONTENT_TYPE);
			contentTypeMap.put(MATHML_2_0_PUBLIC_ID, MATHML_CONTENT_TYPE);
			contentTypeMap.put(SVG_1_0_PUBLIC_ID, SVG_CONTENT_TYPE);
			contentTypeMap.put(SVG_1_1_FULL_PUBLIC_ID, SVG_CONTENT_TYPE);
			contentTypeMap.put(SVG_1_1_BASIC_PUBLIC_ID, SVG_CONTENT_TYPE);
			contentTypeMap.put(SVG_1_1_TINY_PUBLIC_ID, SVG_CONTENT_TYPE);
			contentTypeMapReference = new SoftReference<Map<String, ContentType>>(contentTypeMap); //create a soft reference to the map
		}
		return contentTypeMap; //return the map
	}

	/**
	 * Determines the content type for the given public ID.
	 * @param publicID The public ID for which a content type should be retrieved.
	 * @return The content type corresponding to the given public ID, or <code>null</code> if the given public ID is not recognized.
	 */
	public static ContentType getContentTypeForPublicID(final String publicID) {
		return getContentTypeMap().get(publicID); //return the content type corresponding to the given public ID, if we have one
	}

	/** A lazily-created cache of root element local names keyed to content types base type names. */
	private static Reference<Map<String, String>> rootElementLocalNameMapReference = null;

	/** A lazily-created cache of root element local names keyed to content types. */
	protected static Map<String, String> getRootElementLocalNameMap() {
		//get the cache if we have one
		Map<String, String> rootElementLocalNameMap = rootElementLocalNameMapReference != null ? rootElementLocalNameMapReference.get() : null;
		if(rootElementLocalNameMap == null) { //if the garbage collector has reclaimed the cache
			rootElementLocalNameMap = new HashMap<String, String>(); //create a new map of root element local names, and fill it with the default mappings
			rootElementLocalNameMap.put(HTML_CONTENT_TYPE.getBaseType(), ELEMENT_HTML);
			rootElementLocalNameMap.put(XHTML_CONTENT_TYPE.getBaseType(), ELEMENT_HTML);
			rootElementLocalNameMap.put(MATHML_CONTENT_TYPE.getBaseType(), ELEMENT_MATHML);
			rootElementLocalNameMap.put(SVG_CONTENT_TYPE.getBaseType(), ELEMENT_SVG);
			rootElementLocalNameMapReference = new SoftReference<Map<String, String>>(rootElementLocalNameMap); //create a soft reference to the map
		}
		return rootElementLocalNameMap; //return the map
	}

	/**
	 * Determines the default root element local name for the given content type
	 * @param contentType The content type for which a root element should be retrieved.
	 * @return The default root element local name corresponding to the given media type, or <code>null</code> if the given content type is not recognized.
	 */
	public static String getDefaultRootElementLocalName(final ContentType contentType) {
		return getRootElementLocalNameMap().get(contentType.getBaseType()); //return the root element corresponding to the given content type base type, if we have one
	}

	/**
	 * Creates a document builder and parses an input stream without namespace awareness with no validation. An entity resolver is installed to load requested
	 * resources from local resources if possible. This allows quick local lookup of the XHTML DTDs, for example. The Sun JDK 1.5 document builder handles the BOM
	 * correctly.
	 * <p>
	 * Any {@link SAXException} is converted to an {@link IOException}.
	 * </p>
	 * @param inputStream The input stream containing the content to be parsed.
	 * @return The parsed XML document.
	 * @throws ConfigurationException if a document builder cannot be created which satisfies the configuration requested.
	 * @throws IOException If there is an error reading or parsing the information.
	 */
	public static Document parse(final InputStream inputStream) throws IOException {
		return parse(inputStream, DefaultEntityResolver.getInstance());
	}

	/**
	 * Creates a document builder and parses an input stream without namespace awareness with no validation. The Sun JDK 1.5 document builder handles the BOM
	 * correctly.
	 * <p>
	 * Any {@link SAXException} is converted to an {@link IOException}.
	 * </p>
	 * @param inputStream The input stream containing the content to be parsed.
	 * @param entityResolver The strategy to use for resolving entities, or <code>null</code> if no entity resolver should be installed.
	 * @return The parsed XML document.
	 * @throws ConfigurationException if a document builder cannot be created which satisfies the configuration requested.
	 * @throws IOException If there is an error reading or parsing the information.
	 */
	public static Document parse(final InputStream inputStream, final EntityResolver entityResolver) throws IOException {
		return parse(inputStream, false, entityResolver);
	}

	/**
	 * Creates a document builder and parses an input stream without namespace awareness with no validation. An entity resolver is installed to load requested
	 * resources from local resources if possible. This allows quick local lookup of the XHTML DTDs, for example. The Sun JDK 1.5 document builder handles the BOM
	 * correctly.
	 * <p>
	 * Any {@link SAXException} is converted to an {@link IOException}.
	 * </p>
	 * @param inputStream The input stream containing the content to be parsed.
	 * @param systemID Provide a base for resolving relative URIs.
	 * @return The parsed XML document.
	 * @throws ConfigurationException if a document builder cannot be created which satisfies the configuration requested.
	 * @throws IOException If there is an error reading or parsing the information.
	 */
	public static Document parse(final InputStream inputStream, final URI systemID) throws IOException {
		return parse(inputStream, systemID, DefaultEntityResolver.getInstance());
	}

	/**
	 * Creates a document builder and parses an input stream without namespace awareness with no validation. The Sun JDK 1.5 document builder handles the BOM
	 * correctly.
	 * <p>
	 * Any {@link SAXException} is converted to an {@link IOException}.
	 * </p>
	 * @param inputStream The input stream containing the content to be parsed.
	 * @param systemID Provide a base for resolving relative URIs.
	 * @param entityResolver The strategy to use for resolving entities, or <code>null</code> if no entity resolver should be installed.
	 * @return The parsed XML document.
	 * @throws ConfigurationException if a document builder cannot be created which satisfies the configuration requested.
	 * @throws IOException If there is an error reading or parsing the information.
	 */
	public static Document parse(final InputStream inputStream, final URI systemID, final EntityResolver entityResolver) throws IOException {
		return parse(inputStream, systemID, false, entityResolver);
	}

	/**
	 * Creates a document builder and parses an input stream, specifying namespace awareness with no validation. An entity resolver is installed to load requested
	 * resources from local resources if possible. This allows quick local lookup of the XHTML DTDs, for example. The Sun JDK 1.5 document builder handles the BOM
	 * correctly.
	 * <p>
	 * Any {@link SAXException} is converted to an {@link IOException}.
	 * </p>
	 * @param inputStream The input stream containing the content to be parsed.
	 * @param namespaceAware <code>true</code> if the parser produced will provide support for XML namespaces, else <code>false</code>.
	 * @return The parsed XML document.
	 * @throws ConfigurationException if a document builder cannot be created which satisfies the configuration requested.
	 * @throws IOException If there is an error reading or parsing the information.
	 */
	public static Document parse(final InputStream inputStream, final boolean namespaceAware) throws IOException {
		return parse(inputStream, namespaceAware, DefaultEntityResolver.getInstance());
	}

	/**
	 * Creates a document builder and parses an input stream, specifying namespace awareness with no validation. The Sun JDK 1.5 document builder handles the BOM
	 * correctly.
	 * <p>
	 * Any {@link SAXException} is converted to an {@link IOException}.
	 * </p>
	 * @param inputStream The input stream containing the content to be parsed.
	 * @param namespaceAware <code>true</code> if the parser produced will provide support for XML namespaces, else <code>false</code>.
	 * @param entityResolver The strategy to use for resolving entities, or <code>null</code> if no entity resolver should be installed.
	 * @return The parsed XML document.
	 * @throws ConfigurationException if a document builder cannot be created which satisfies the configuration requested.
	 * @throws IOException If there is an error reading or parsing the information.
	 */
	public static Document parse(final InputStream inputStream, final boolean namespaceAware, final EntityResolver entityResolver) throws IOException {
		return parse(inputStream, namespaceAware, false, entityResolver);
	}

	/**
	 * Creates a document builder and parses an input stream, specifying namespace awareness with no validation. An entity resolver is installed to load requested
	 * resources from local resources if possible. This allows quick local lookup of the XHTML DTDs, for example. The Sun JDK 1.5 document builder handles the BOM
	 * correctly.
	 * <p>
	 * Any {@link SAXException} is converted to an {@link IOException}.
	 * </p>
	 * @param inputStream The input stream containing the content to be parsed.
	 * @param systemID Provide a base for resolving relative URIs.
	 * @param namespaceAware <code>true</code> if the parser produced will provide support for XML namespaces, else <code>false</code>.
	 * @return The parsed XML document.
	 * @throws ConfigurationException if a document builder cannot be created which satisfies the configuration requested.
	 * @throws IOException If there is an error reading or parsing the information.
	 */
	public static Document parse(final InputStream inputStream, final URI systemID, final boolean namespaceAware) throws IOException {
		return parse(inputStream, systemID, namespaceAware, DefaultEntityResolver.getInstance());
	}

	/**
	 * Creates a document builder and parses an input stream, specifying namespace awareness with no validation. The Sun JDK 1.5 document builder handles the BOM
	 * correctly.
	 * <p>
	 * Any {@link SAXException} is converted to an {@link IOException}.
	 * </p>
	 * @param inputStream The input stream containing the content to be parsed.
	 * @param systemID Provide a base for resolving relative URIs.
	 * @param namespaceAware <code>true</code> if the parser produced will provide support for XML namespaces, else <code>false</code>.
	 * @param entityResolver The strategy to use for resolving entities, or <code>null</code> if no entity resolver should be installed.
	 * @return The parsed XML document.
	 * @throws ConfigurationException if a document builder cannot be created which satisfies the configuration requested.
	 * @throws IOException If there is an error reading or parsing the information.
	 */
	public static Document parse(final InputStream inputStream, final URI systemID, final boolean namespaceAware, final EntityResolver entityResolver)
			throws IOException {
		return parse(inputStream, systemID, namespaceAware, false, entityResolver);
	}

	/**
	 * Creates a document builder and parses an input stream, specifying namespace awareness and validation. An entity resolver is installed to load requested
	 * resources from local resources if possible. This allows quick local lookup of the XHTML DTDs, for example. The Sun JDK 1.5 document builder handles the BOM
	 * correctly.
	 * <p>
	 * Any {@link SAXException} is converted to an {@link IOException}.
	 * </p>
	 * @param inputStream The input stream containing the content to be parsed.
	 * @param namespaceAware <code>true</code> if the parser produced will provide support for XML namespaces, else <code>false</code>.
	 * @param validating <code>true</code> if the parser produced will validate documents as they are parsed, else <code>false</code>.
	 * @return The parsed XML document.
	 * @throws ConfigurationException if a document builder cannot be created which satisfies the configuration requested.
	 * @throws IOException If there is an error reading or parsing the information.
	 */
	public static Document parse(final InputStream inputStream, final boolean namespaceAware, final boolean validating) throws IOException {
		return parse(inputStream, namespaceAware, validating, DefaultEntityResolver.getInstance());
	}

	/**
	 * Creates a document builder and parses an input stream, specifying namespace awareness and validation. The Sun JDK 1.5 document builder handles the BOM
	 * correctly.
	 * <p>
	 * Any {@link SAXException} is converted to an {@link IOException}.
	 * </p>
	 * @param inputStream The input stream containing the content to be parsed.
	 * @param namespaceAware <code>true</code> if the parser produced will provide support for XML namespaces, else <code>false</code>.
	 * @param validating <code>true</code> if the parser produced will validate documents as they are parsed, else <code>false</code>.
	 * @param entityResolver The strategy to use for resolving entities, or <code>null</code> if no entity resolver should be installed.
	 * @return The parsed XML document.
	 * @throws ConfigurationException if a document builder cannot be created which satisfies the configuration requested.
	 * @throws IOException If there is an error reading or parsing the information.
	 */
	public static Document parse(final InputStream inputStream, final boolean namespaceAware, final boolean validating, final EntityResolver entityResolver)
			throws IOException {
		try {
			return createDocumentBuilder(namespaceAware, validating, entityResolver).parse(inputStream);
		} catch(final SAXException saxException) {
			throw new IOException(saxException.getMessage(), saxException);
		}
	}

	/**
	 * Creates a document builder and parses an input stream, specifying namespace awareness and validation. An entity resolver is installed to load requested
	 * resources from local resources if possible. This allows quick local lookup of the XHTML DTDs, for example. The Sun JDK 1.5 document builder handles the BOM
	 * correctly.
	 * <p>
	 * Any {@link SAXException} is converted to an {@link IOException}.
	 * </p>
	 * @param inputStream The input stream containing the content to be parsed.
	 * @param systemID Provide a base for resolving relative URIs.
	 * @param namespaceAware <code>true</code> if the parser produced will provide support for XML namespaces, else <code>false</code>.
	 * @param validating <code>true</code> if the parser produced will validate documents as they are parsed, else <code>false</code>.
	 * @return The parsed XML document.
	 * @throws ConfigurationException if a document builder cannot be created which satisfies the configuration requested.
	 * @throws IOException If there is an error reading or parsing the information.
	 */
	public static Document parse(final InputStream inputStream, final URI systemID, final boolean namespaceAware, final boolean validating) throws IOException {
		return parse(inputStream, systemID, namespaceAware, validating, DefaultEntityResolver.getInstance());
	}

	/**
	 * Creates a document builder and parses an input stream, specifying namespace awareness and validation. The Sun JDK 1.5 document builder handles the BOM
	 * correctly.
	 * <p>
	 * Any {@link SAXException} is converted to an {@link IOException}.
	 * </p>
	 * @param inputStream The input stream containing the content to be parsed.
	 * @param systemID Provide a base for resolving relative URIs.
	 * @param namespaceAware <code>true</code> if the parser produced will provide support for XML namespaces, else <code>false</code>.
	 * @param validating <code>true</code> if the parser produced will validate documents as they are parsed, else <code>false</code>.
	 * @param entityResolver The strategy to use for resolving entities, or <code>null</code> if no entity resolver should be installed.
	 * @return The parsed XML document.
	 * @throws ConfigurationException if a document builder cannot be created which satisfies the configuration requested.
	 * @throws IOException If there is an error reading or parsing the information.
	 */
	public static Document parse(final InputStream inputStream, final URI systemID, final boolean namespaceAware, final boolean validating,
			final EntityResolver entityResolver) throws IOException {
		try {
			return createDocumentBuilder(namespaceAware, validating, entityResolver).parse(inputStream, systemID.toString());
		} catch(final SAXException saxException) {
			throw new IOException(saxException.getMessage(), saxException);
		}
	}

	/**
	 * Creates and returns a document builder without namespace awareness with no validation. An entity resolver is installed to load requested resources from
	 * local resources if possible. This allows quick local lookup of the XHTML DTDs, for example. The Sun JDK 1.5 document builder handles the BOM correctly.
	 * @return A new XML document builder.
	 * @throws ConfigurationException if a document builder cannot be created which satisfies the configuration requested.
	 */
	public static DocumentBuilder createDocumentBuilder() {
		return createDocumentBuilder(DefaultEntityResolver.getInstance());
	}

	/**
	 * Creates and returns a document builder without namespace awareness with no validation. The Sun JDK 1.5 document builder handles the BOM correctly.
	 * @param entityResolver The strategy to use for resolving entities, or <code>null</code> if no entity resolver should be installed.
	 * @return A new XML document builder.
	 * @throws ConfigurationException if a document builder cannot be created which satisfies the configuration requested.
	 */
	public static DocumentBuilder createDocumentBuilder(final EntityResolver entityResolver) {
		return createDocumentBuilder(false, entityResolver); //create a document builder with no namespace awareness
	}

	/**
	 * Creates and returns a document builder, specifying namespace awareness with no validation. An entity resolver is installed to load requested resources from
	 * local resources if possible. This allows quick local lookup of the XHTML DTDs, for example. The Sun JDK 1.5 document builder handles the BOM correctly.
	 * @param namespaceAware <code>true</code> if the parser produced will provide support for XML namespaces, else <code>false</code>.
	 * @return A new XML document builder.
	 * @throws ConfigurationException if a document builder cannot be created which satisfies the configuration requested.
	 */
	public static DocumentBuilder createDocumentBuilder(final boolean namespaceAware) {
		return createDocumentBuilder(namespaceAware, DefaultEntityResolver.getInstance());
	}

	/**
	 * Creates and returns a document builder, specifying namespace awareness with no validation. The Sun JDK 1.5 document builder handles the BOM correctly.
	 * @param namespaceAware <code>true</code> if the parser produced will provide support for XML namespaces, else <code>false</code>.
	 * @param entityResolver The strategy to use for resolving entities, or <code>null</code> if no entity resolver should be installed.
	 * @return A new XML document builder.
	 * @throws ConfigurationException if a document builder cannot be created which satisfies the configuration requested.
	 */
	public static DocumentBuilder createDocumentBuilder(final boolean namespaceAware, final EntityResolver entityResolver) {
		return createDocumentBuilder(namespaceAware, false, entityResolver); //create a document builder with no validation
	}

	/**
	 * Creates and returns a document builder, specifying namespace awareness and validation. An entity resolver is installed to load requested resources from
	 * local resources if possible. This allows quick local lookup of the XHTML DTDs, for example. The Sun JDK 1.5 document builder handles the BOM correctly.
	 * @param namespaceAware <code>true</code> if the parser produced will provide support for XML namespaces, else <code>false</code>.
	 * @param validating <code>true</code> if the parser produced will validate documents as they are parsed, else <code>false</code>.
	 * @return A new XML document builder.
	 * @throws ConfigurationException if a document builder cannot be created which satisfies the configuration requested.
	 */
	public static DocumentBuilder createDocumentBuilder(final boolean namespaceAware, final boolean validating) {
		return createDocumentBuilder(namespaceAware, validating, DefaultEntityResolver.getInstance());
	}

	/**
	 * Creates and returns a document builder, specifying namespace awareness and validation. The Sun JDK 1.5 document builder handles the BOM correctly.
	 * @param namespaceAware <code>true</code> if the parser produced will provide support for XML namespaces, else <code>false</code>.
	 * @param validating <code>true</code> if the parser produced will validate documents as they are parsed, else <code>false</code>.
	 * @param entityResolver The strategy to use for resolving entities, or <code>null</code> if no entity resolver should be installed.
	 * @return A new XML document builder.
	 * @throws ConfigurationException if a document builder cannot be created which satisfies the configuration requested.
	 */
	public static DocumentBuilder createDocumentBuilder(final boolean namespaceAware, final boolean validating, final EntityResolver entityResolver) {
		try {
			final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance(); //create a document builder factory			
			documentBuilderFactory.setNamespaceAware(namespaceAware); //set namespace awareness appropriately
			documentBuilderFactory.setValidating(validating); //set validating appropriately
			//prevent a NullPointerException in some cases when using the com.sun.org.apache.xerces.internal parser
			//see http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6181020
			//see http://issues.apache.org/jira/browse/XERCESJ-977
			//http://forums.sun.com/thread.jspa?threadID=5390848
			try {
				documentBuilderFactory.setFeature("http://apache.org/xml/features/dom/defer-node-expansion", false);
			} catch(final Throwable throwable) {
			} //if the parser doesn't support this feature, it's probably not the buggy Xerces parser, so we're in an even better situation; normally we'd expect a ParserConfigurationException, but sometimes a java.lang.AbstractMethodError is thrown by javax.xml.parsers.DocumentBuilderFactory.setFeature(Ljava/lang/String;Z)V
			final DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder(); //create a new document builder
			if(entityResolver != null) { //if an entity resolver was given
				documentBuilder.setEntityResolver(entityResolver); //install the given entity resolver
			}
			return documentBuilder; //return the configured document builder
		} catch(final ParserConfigurationException parserConfigurationException) { //if the requested parser is not supported
			throw new ConfigurationException(parserConfigurationException);
		}
	}

	/**
	 * Returns a list of all elements from the given list of nodes.
	 * @param nodes The list of nodes.
	 * @return A list of all elements in the given list.
	 * @throws NullPointerException if the given list is <code>null</code>.
	 */
	public static List<Element> getElements(final List<Node> nodes) {
		return getElementsAsType(nodes, Node.ELEMENT_NODE, Element.class);
	}

	/**
	 * Returns a list of all nodes of a given type from the given list of nodes.
	 * @param nodes The list of nodes.
	 * @param nodeType The type of node to retrieve, one of the <code>Node.?_NODE</code> constants.
	 * @return A list of all nodes of the given type from the given list.
	 * @throws NullPointerException if the given list is <code>null</code>.
	 * @throws ClassCastException if the given node type does not correspond to the given node class.
	 */
	protected static <N extends Node> List<N> getElementsAsType(final List<Node> nodes, final short nodeType, final Class<N> nodeClass) {
		final List<N> nodesAsType = new ArrayList<N>();
		for(final Node node : nodes) { //look at all the nodes
			if(node.getNodeType() == nodeType) { //if this node is of the correct type
				nodesAsType.add(nodeClass.cast(node)); //cast and add the node to the list
			}
		}
		return nodesAsType;
	}

	/**
	 * Returns the owner document of the given node or, if the node is a document, returns the node itself.
	 * @param node The node that may be a document or may have an owner document.
	 * @return The node owner document (which may be <code>null</code> if the node has no owner document) or, if the node is a document, the node itself.
	 */
	public static Document getDocument(final Node node) {
		return node.getNodeType() == Node.DOCUMENT_NODE ? (Document)node : node.getOwnerDocument(); //return the node if it is a document, otherwise return the node's owner document
	}

	/**
	 * Returns the first node in the hierarchy, beginning with the specified node and continuing with a depth-first search, that has a particular node type.
	 * @param node The node which will be checked for nodes, along with its children. The node's owner document should implement the
	 *          <code>DocumentTraversal</code> interface.
	 * @param whatToShow Which node type(s) to return. See the description of <code>NodeFilter</code> for the set of possible <code>SHOW_</code> values. These
	 *          flags can be combined using boolean OR.
	 * @return The first encountered node in a depth-first (document order) search that matches the filter criteria, or <code>null</code> if no matching node
	 *         exists.
	 * @see NodeIterator
	 * @see NodeFilter
	 */
	public static Node getFirstNode(final Node node, final int whatToShow) {
		//create a node iterator that will only return the types of nodes we want
		final NodeIterator nodeIterator = ((DocumentTraversal)node.getOwnerDocument()).createNodeIterator(node, whatToShow, null, false); //TODO should we set expandEntityReferences to true or false? true?
		return nodeIterator.nextNode(); //get the next node (which will be the first node) and return it
	}

	/**
	 * Returns the index of a given node in a node list.
	 * @param nodeList The node list to search.
	 * @param node The node for which an index should be returned.
	 * @return The index of the node in the given node list, or -1 if the node does not appear in the node list.
	 */
	public static int indexOf(final NodeList nodeList, final Node node) {
		final int nodeCount = nodeList.getLength(); //see how many nodes there are
		for(int i = 0; i < nodeCount; ++i) { //look at each node
			if(nodeList.item(i) == node) //if the node at this index matches our node
				return i; //show the index as which the node occurs
		}
		return -1; //show that we were not able to find a matching node
	}

	/**
	 * Determines if the given content type is one representing XML in some form.
	 * <p>
	 * XML media types include:
	 * </p>
	 * <ul>
	 * <li><code>text/xml</code></li>
	 * <li><code>application/xml</code></li>
	 * <li><code>application/*+xml</code></li>
	 * </ul>
	 * @param contentType The content type of a resource, or <code>null</code> for no content type.
	 * @return <code>true</code> if the given content type is one of several XML media types.
	 */
	public static boolean isXML(final ContentType contentType) {
		if(contentType != null) { //if a content type is given
			if(ContentType.TEXT_PRIMARY_TYPE.equals(contentType.getPrimaryType()) && XML_SUBTYPE.equals(contentType.getSubType())) { //if this is "text/xml"
				return true; //text/xml is an XML content type
			}
			if(ContentType.APPLICATION_PRIMARY_TYPE.equals(contentType.getPrimaryType())) { //if this is "application/*"
				return XML_SUBTYPE.equals(contentType.getSubType()) //see if the subtype is "xml"
						|| contentType.hasSubTypeSuffix(XML_SUBTYPE_SUFFIX); //see if the subtype has an XML suffix
			}
		}
		return false; //this is not a media type we recognize as being XML
	}

	/**
	 * Determines if the given content type is one representing an XML external parsed entity in some form.
	 * <p>
	 * XML external parsed entities include:
	 * </p>
	 * <ul>
	 * <li><code>text/xml-external-parsed-entity</code></li>
	 * <li><code>application/xml-external-parsed-entity</code></li>
	 * <li><code>text/*+xml-external-parsed-entity</code> (not formally defined)</li>
	 * <li><code>application/*+xml-external-parsed-entity</code> (not formally defined)</li>
	 * </ul>
	 * @param contentType The content type of a resource, or <code>null</code> for no content type.
	 * @return <code>true</code> if the given content type is one of several XML external parsed entity media types.
	 */
	public static boolean isXMLExternalParsedEntity(final ContentType contentType) {
		if(contentType != null) { //if a content type is given
			final String primaryType = contentType.getPrimaryType(); //get the primary type
			if(ContentType.TEXT_PRIMARY_TYPE.equals(primaryType) || ContentType.APPLICATION_PRIMARY_TYPE.equals(primaryType)) { //if this is "text/*" or "application/*"
				final String subType = contentType.getSubType(); //get the subtype
				return XML_EXTERNAL_PARSED_ENTITY_SUBTYPE.equals(subType) //if the subtype is /xml-external-parsed-entity
						|| contentType.hasSubTypeSuffix(XML_EXTERNAL_PARSED_ENTITY_SUBTYPE_SUFFIX); //or if the subtype has an XML external parsed entity suffix
			}
		}
		return false; //this is not a media type we recognize as being an XML external parsed entity
	}

	/** The character to replace the first character if needed. */
	protected static final char REPLACEMENT_FIRST_CHAR = 'x';

	/** The character to use to replace any other character. */
	protected static final char REPLACEMENT_CHAR = '_';

	/** The special XML symbols that should be replaced with entities. */
	private static final char[] XML_ENTITY_CHARS = { '&', '"', '\'', '>', '<' }; //TODO use constants

	/** The strings to replace XML symbols. */
	private static final String[] XML_ENTITY_REPLACMENTS = { "&amp;", "&quot;", "&apos;", "&gt;", "&lt;" }; //TODO use constants

	/**
	 * Replaces special XML symbols with their escaped versions, (e.g. replaces '&lt' with "&amp;lt;") so that the string is valid XML content.
	 * @param string The string to be manipulated.
	 * @return An XML-friendly string.
	 */
	public static String createValidContent(final String string) {
		return Strings.replace(string, XML_ENTITY_CHARS, XML_ENTITY_REPLACMENTS); //do the replacements for the special XML symbols and return the results
	}

	/**
	 * Creates a string in which all illegal XML characters are replaced with the space character.
	 * @param string The string the characters of which should be checked for XML validity.
	 * @return A new string with illegal XML characters replaced with spaces, or the original string if no characters were replaced.
	 */
	public static String createValidString(final String string) {
		StringBuffer stringBuffer = null; //we'll only create a string buffer if there are invalid characters
		for(int i = string.length() - 1; i >= 0; --i) { //look at all the characters in the string
			if(!isChar(string.charAt(i))) { //if this is not a valid character
				if(stringBuffer == null) //if we haven't create a string buffer, yet
					stringBuffer = new StringBuffer(string); //create a string buffer to hold our replacements
				stringBuffer.setCharAt(i, SPACE_CHAR); //replace this character with a space
			}
		}
		return stringBuffer != null ? stringBuffer.toString() : string; //return the original string unless we've actually modified something
	}

	/**
	 * Creates a qualified name object from an XML node.
	 * <p>
	 * If the node namespace is not a valid URI (e.g. "DAV:"), it will be converted to a valid URI (e.g. "DAV:/") if possible.
	 * </p>
	 * @param node The XML node from which a qualified name is to be created.
	 * @return A qualified name object representing the given XML node
	 * @throws IllegalArgumentException if the namespace is not <code>null</code> and cannot be converted to a valid URI.
	 * @see #toNamespaceURI(String)
	 */
	public static QualifiedName createQualifiedName(final Node node) {
		return new QualifiedName(node.getNamespaceURI(), node.getPrefix(), node.getLocalName()); //create a qualified name for this node
	}

	/**
	 * Creates a namespace URI from the given namespace string.
	 * <p>
	 * This method attempts to compensate for XML documents that include a namespace string that is not a true URI, notably the <code>DAV:</code> namespace "URI"
	 * used by WebDAV. In such a case as <code>DAV:</code>, the URI <code>DAV:/</code> would be returned.
	 * </p>
	 * @param namespace The namespace string, or <code>null</code> if there is no namespace.
	 * @return A URI representing the namespace, or <code>null</code> if no namespace was given.
	 * @throws IllegalArgumentException if the namespace is not <code>null</code> and cannot be converted to a valid URI.
	 */
	public static URI toNamespaceURI(String namespace) {
		if(namespace == null) {
			return null;
		}
		final int schemeSeparatorIndex = namespace.indexOf(URIs.SCHEME_SEPARATOR); //find out where the scheme ends
		if(schemeSeparatorIndex == namespace.length() - 1) { //if the scheme separator is at the end of the string (i.e. there is no scheme-specific part, e.g. "DAV:")
			namespace += URIs.PATH_SEPARATOR; //append a path separator (e.g. "DAV:/")
		}
		return URI.create(namespace); //create a URI from the namespace
	}

	/**
	 * Adds a stylesheet to the XML document using the standard <code>&lt;<?xml-stylesheet...</code> processing instruction notation.
	 * @param document The document to which the stylesheet reference should be added.
	 * @param href The reference to the stylesheet.
	 * @param mediaType The media type of the stylesheet.
	 */
	public static void addStyleSheetReference(final Document document, final String href, final ContentType mediaType) {
		final String target = XML_STYLESHEET_PROCESSING_INSTRUCTION; //the PI target will be the name of the stylesheet processing instruction
		final StringBuffer dataStringBuffer = new StringBuffer(); //create a string buffer to construct the data parameter (with its pseudo attributes)
		//add: href="href"
		dataStringBuffer.append(HREF_ATTRIBUTE).append(EQUAL_CHAR).append(DOUBLE_QUOTE_CHAR).append(href).append(DOUBLE_QUOTE_CHAR);
		dataStringBuffer.append(SPACE_CHAR); //add a space between the pseudo attributes
		//add: type="type"
		dataStringBuffer.append(TYPE_ATTRIBUTE).append(EQUAL_CHAR).append(DOUBLE_QUOTE_CHAR).append(mediaType).append(DOUBLE_QUOTE_CHAR);
		final String data = dataStringBuffer.toString(); //convert the data string buffer to a string
		final ProcessingInstruction processingInstruction = document.createProcessingInstruction(target, data); //create a processing instruction with the correct information
		document.appendChild(processingInstruction); //append the processing instruction to the document
	}

	/**
	 * Performs a clone on the children of the source node and adds them to the destination node.
	 * @param destinationNode The node that will receive the cloned child nodes.
	 * @param sourceNode The node from whence the nodes will be cloned.
	 * @param deep Whether each child should be deeply cloned.
	 */
	//TODO list exceptions
	public static void appendClonedChildNodes(final Node destinationNode, final Node sourceNode, final boolean deep) {
		final NodeList sourceNodeList = sourceNode.getChildNodes(); //get the list of child nodes
		final int sourceNodeCount = sourceNodeList.getLength(); //find out how many nodes there are
		for(int i = 0; i < sourceNodeCount; ++i) { //look at each of the source nodes
			final Node sourceChildNode = sourceNodeList.item(i); //get a reference to this child node
			destinationNode.appendChild(sourceChildNode.cloneNode(deep)); //clone the node and add it to the destination node
		}
	}

	/**
	 * Performs a deep import on the children of the source node and adds them to the destination node.
	 * @param destinationNode The node that will receive the imported child nodes.
	 * @param sourceNode The node from whence the nodes will be imported.
	 */
	//TODO list exceptions
	public static void appendImportedChildNodes(final Node destinationNode, final Node sourceNode) {
		appendImportedChildNodes(destinationNode, sourceNode, true); //import and append all descendant nodes
	}

	/**
	 * Performs an import on the children of the source node and adds them to the destination node.
	 * @param destinationNode The node that will receive the imported child nodes.
	 * @param sourceNode The node from whence the nodes will be imported.
	 * @param deep Whether each child should be deeply imported.
	 */
	//TODO list exceptions
	public static void appendImportedChildNodes(final Node destinationNode, final Node sourceNode, final boolean deep) {
		final Document destinationDocument = destinationNode.getOwnerDocument(); //get the owner document of the destination node
		final NodeList sourceNodeList = sourceNode.getChildNodes(); //get the list of child nodes
		final int sourceNodeCount = sourceNodeList.getLength(); //find out how many nodes there are
		for(int i = 0; i < sourceNodeCount; ++i) { //look at each of the source nodes
			final Node sourceChildNode = sourceNodeList.item(i); //get a reference to this child node
			destinationNode.appendChild(destinationDocument.importNode(sourceChildNode, deep)); //import the node and add it to the destination node
		}
	}

	/**
	 * Performs a clone on the attributes of the source node and adds them to the destination node. It is assumed that all attributes have been added using
	 * namespace aware methods.
	 * @param destinationElement The element that will receive the cloned child nodes.
	 * @param sourceElement The element that contains the attributes to be cloned.
	 */
	//TODO list exceptions
	public static void appendClonedAttributeNodesNS(final Element destinationElement, final Element sourceElement) {
		final NamedNodeMap attributeNodeMap = sourceElement.getAttributes(); //get the source element's attributes
		final int sourceNodeCount = attributeNodeMap.getLength(); //find out how many nodes there are
		for(int i = 0; i < sourceNodeCount; ++i) { //look at each of the source nodes
			final Node sourceAttributeNode = attributeNodeMap.item(i); //get a reference to this attribute node
			destinationElement.setAttributeNodeNS((Attr)sourceAttributeNode.cloneNode(true)); //clone the attribute and add it to the destination element
		}
	}

	/**
	 * Creates a text node with the specified character and appends it to the specified element.
	 * @param element The element to which text should be added. This element must have a valid owner document.
	 * @param textCharacter The character to add to the element.
	 * @return The new text node that was created.
	 */
	//TODO list exceptions
	public static Text appendText(final Element element, final char textCharacter) {
		return appendText(element, String.valueOf(textCharacter)); //convert the character to a string and append it to the element
	}

	/**
	 * Creates a text node with the specified text and appends it to the specified element.
	 * @param element The element to which text should be added. This element must have a valid owner document.
	 * @param textString The text to add to the element.
	 * @return The new text node that was created.
	 * @throws NullPointerException if the given element and/or text string is <code>null</code>.
	 * @throws DOMException if there was an error appending the text.
	 */
	public static Text appendText(final Element element, final String textString) throws DOMException {
		final Text textNode = element.getOwnerDocument().createTextNode(textString); //create a new text node with the specified text
		element.appendChild(textNode); //append the text node to our paragraph
		return textNode; //return the text node we created
	}

	/**
	 * Convenience function to create an element and use it to replace the document element of the document.
	 * @param document The document which will serve as parent of the newly created element.
	 * @param elementNamespaceURI The namespace URI of the element to be created.
	 * @param elementName The name of the element to create.
	 * @return The newly created child element.
	 */
	//TODO list exceptions
	public static Element replaceDocumentElement(final Document document, final String elementNamespaceURI, final String elementName) {
		return replaceDocumentElementNS(document, elementNamespaceURI, elementName, null); //append an element with no text
	}

	/**
	 * Convenience function to create an element, replace the document element of the given document, and add optional text as a child of the given element. A
	 * heading, for instance, might be added using <code>replaceDocumentElement(document, XHTML_NAMESPACE_URI, ELEMENT_H2,
		"My Heading");</code>.
	 * @param document The document which will serve as parent of the newly created element.
	 * @param elementNamespaceURI The namespace URI of the element to be created.
	 * @param elementName The name of the element to create.
	 * @param textContent The text to add as a child of the created element, or <code>null</code> if no text should be added.
	 * @return The newly created child element.
	 * @throws DOMException if there was an error creating the element, appending the text, or replacing the child.
	 */
	public static Element replaceDocumentElementNS(final Document document, final String elementNamespaceURI, final String elementName,
			final String textContent) {
		final Element childElement = createElementNS(document, elementNamespaceURI, elementName, textContent); //create the new element
		document.replaceChild(childElement, document.getDocumentElement()); //replace the document element of the document
		return childElement; //return the element we created
	}

	/**
	 * Convenience function to create an element and add it as a child of the given parent element.
	 * @param parentElement The element which will serve as parent of the newly created element. This element must have a valid owner document.
	 * @param elementNamespaceURI The namespace URI of the element to be created.
	 * @param elementName The name of the element to create.
	 * @return The newly created child element.
	 * @throws DOMException if there was an error creating the element or appending the element to the parent element.
	 */
	public static Element appendElementNS(final Element parentElement, final String elementNamespaceURI, final String elementName) {
		return appendElementNS(parentElement, elementNamespaceURI, elementName, null); //append the element with no text
	}

	/**
	 * Convenience function to create an element, add it as a child of the given parent element, and add optional text as a child of the given element. A heading,
	 * for instance, might be added using <code>appendElement(bodyElement, XHTML_NAMESPACE_URI, ELEMENT_H2,
		"My Heading");</code>.
	 * @param parentElement The element which will serve as parent of the newly created element. This element must have a valid owner document.
	 * @param elementNamespaceURI The namespace URI of the element to be created.
	 * @param elementName The name of the element to create.
	 * @param textContent The text to add as a child of the created element, or <code>null</code> if no text should be added.
	 * @return The newly created child element.
	 * @throws DOMException if there was an error creating the element, appending the text, or appending the element to the parent element.
	 */
	public static Element appendElementNS(final Element parentElement, final String elementNamespaceURI, final String elementName, final String textContent) {
		final Element childElement = createElementNS(parentElement.getOwnerDocument(), elementNamespaceURI, elementName, textContent); //create the new element
		parentElement.appendChild(childElement); //add the child element to the parent element
		return childElement; //return the element we created
	}

	/**
	 * Creates a document wrapped around a copy of the given element.
	 * @param element The element to become the document element of the new document.
	 * @return A new document with a clone of the given element as the document element.
	 */
	public static Document createDocument(final Element element) {
		final DOMImplementation domImplementation = element.getOwnerDocument().getImplementation(); //get the DOM implementation used to create the document
		//create a new document corresponding to the element
		//TODO bring over the doctype, if needed
		final Document document = domImplementation.createDocument(element.getNamespaceURI(), element.getNodeName(), null);
		final Node importedNode = document.importNode(element, true); //import the element into our new document
		document.replaceChild(importedNode, document.getDocumentElement()); //set the element clone as the document element of the new document
		return document; //return the document we created
	}

	/**
	 * Convenience function to create an element and add optional text as a child of the given element. A heading, for instance, might be created using
	 * <code>appendElementNS(document, XHTML_NAMESPACE_URI, ELEMENT_H2,
		"My Heading");</code>.
	 * @param document The document to be used to create the new element.
	 * @param elementNamespaceURI The namespace URI of the element to be created.
	 * @param elementName The name of the element to create.
	 * @param textContent The text to add as a child of the created element, or <code>null</code> if no text should be added.
	 * @return The newly created child element.
	 * @throws DOMException if there was an error creating the element or appending the text.
	 * @see Document#createElementNS(String, String)
	 * @see #appendText(Element, String)
	 */
	public static Element createElementNS(final Document document, final String elementNamespaceURI, final String elementName, final String textContent)
			throws DOMException {
		final Element childElement = document.createElementNS(elementNamespaceURI, elementName); //create the new element
		if(textContent != null) { //if we have text content to add
			appendText(childElement, textContent); //append the text content to the newly created child element
		}
		return childElement; //return the element we created
	}

	/**
	 * Extracts a single node from its parent and places it in a document fragment. The node is removed from its parent.
	 * @param node The node to be extracted. This node must have a valid parent and owner document.
	 * @return A new document fragment containing the extracted node.
	 * @throws DOMException
	 *           <ul>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly.</li>
	 *           </ul>
	 * @throws IllegalArgumentException if the given node has no owner document.
	 * @throws IllegalArgumentException if the given node has no parent node.
	 * @see #removeChildren(Node, int, int)
	 */
	public static DocumentFragment extractNode(final Node node) throws DOMException {
		return extractNode(node, true); //extract the node by removing it
	}

	/**
	 * Extracts a single node from its parent and places it in a document fragment.
	 * @param node The node to be extracted. This node must have a valid parent and owner document.
	 * @param remove Whether the node will be removed from its parent; if <code>false</code>, it will remain the child of its parent.
	 * @return A new document fragment containing the extracted node.
	 * @throws DOMException
	 *           <ul>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly.</li>
	 *           </ul>
	 * @throws IllegalArgumentException if the given node has no owner document.
	 * @throws IllegalArgumentException if the given node has no parent node.
	 * @see #removeChildren(Node, int, int)
	 */
	public static DocumentFragment extractNode(final Node node, final boolean remove) throws DOMException {
		final Document ownerDocument = node.getOwnerDocument(); //get the node owner document
		if(ownerDocument == null) { //if there is no owner document
			throw new IllegalArgumentException("Node " + node + " has no owner document.");
		}
		final Node parentNode = node.getParentNode(); //get the node's parent
		if(parentNode == null) { //if there is no parent node
			throw new IllegalArgumentException("Node " + node + " has no parent node.");
		}
		final DocumentFragment documentFragment = ownerDocument.createDocumentFragment(); //create a document fragment to hold the nodes
		if(remove) { //if we should remove the node
			parentNode.removeChild(node); //remove the node from its parent
		}
		documentFragment.appendChild(node); //append the removed child to the document fragment
		return documentFragment; //return the document fragment
	}

	/**
	 * Extracts all the child nodes from the given node and places them in a document fragment. The children are removed from their parents.
	 * @param node The node from which child nodes should be extracted. This node must have a valid owner document.
	 * @return A new document fragment containing the extracted children.
	 * @throws DOMException
	 *           <ul>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly.</li>
	 *           </ul>
	 * @see #removeChildren
	 */
	public static DocumentFragment extractChildren(final Node node) throws DOMException {
		return extractChildren(node, true); //extract the childen by removing them
	}

	/**
	 * Extracts all the child nodes from the given node and places them in a document fragment.
	 * @param node The node from which child nodes should be extracted. This node must have a valid owner document.
	 * @param remove Whether the nodes will be removed from the parentnode ; if <code>false</code>, they will remain the child of the parent node.
	 * @return A new document fragment containing the extracted children.
	 * @throws DOMException
	 *           <ul>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly.</li>
	 *           </ul>
	 * @see #removeChildren
	 */
	public static DocumentFragment extractChildren(final Node node, final boolean remove) throws DOMException {
		return extractChildren(node, 0, node.getChildNodes().getLength(), remove); //extract all the children and return the new document fragment 
	}

	/**
	 * Extracts the indexed nodes starting at <code>startChildIndex</code> up to but not including <code>endChildIndex</code>. The children are removed from their
	 * parents.
	 * @param node The node from which child nodes should be extracted. This node must have a valid owner document.
	 * @param startChildIndex The index of the first child to extract.
	 * @param endChildIndex The index directly after the last child to extract. Must be greater than <code>startChildIndex</code> or no action will occur.
	 * @return A new document fragment containing the extracted children.
	 * @throws ArrayIndexOutOfBoundsException Thrown if either index is negative, if the start index is greater than or equal to the number of children, or if the
	 *           ending index is greater than the number of children (unless the ending index is not greater than the starting index). TODO should we throw an
	 *           exception is startChildIndex>endChildIndex, like String.substring()?
	 * @throws IllegalArgumentException if the given node has no owner document.
	 * @throws ArrayIndexOutOfBoundsException if the given range is invalid for the given node's children.
	 * @throws DOMException
	 *           <ul>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly.</li>
	 *           </ul>
	 * @see #removeChildren(Node, int, int)
	 */
	public static DocumentFragment extractChildren(final Node node, final int startChildIndex, final int endChildIndex)
			throws ArrayIndexOutOfBoundsException, DOMException {
		return extractChildren(node, startChildIndex, endChildIndex, true); //extract the childen by removing them
	}

	/**
	 * Extracts the indexed nodes starting at <code>startChildIndex</code> up to but not including <code>endChildIndex</code>.
	 * @param node The node from which child nodes should be extracted. This node must have a valid owner document.
	 * @param startChildIndex The index of the first child to extract.
	 * @param endChildIndex The index directly after the last child to extract. Must be greater than <code>startChildIndex</code> or no action will occur.
	 * @param remove Whether the nodes will be removed from the parentnode ; if <code>false</code>, they will remain the child of the parent node.
	 * @return A new document fragment containing the extracted children.
	 * @throws ArrayIndexOutOfBoundsException Thrown if either index is negative, if the start index is greater than or equal to the number of children, or if the
	 *           ending index is greater than the number of children (unless the ending index is not greater than the starting index). TODO should we throw an
	 *           exception is startChildIndex>endChildIndex, like String.substring()?
	 * @throws IllegalArgumentException if the given node has no owner document.
	 * @throws ArrayIndexOutOfBoundsException if the given range is invalid for the given node's children.
	 * @throws DOMException
	 *           <ul>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly.</li>
	 *           </ul>
	 * @see #removeChildren(Node, int, int)
	 */
	public static DocumentFragment extractChildren(final Node node, final int startChildIndex, final int endChildIndex, final boolean remove)
			throws ArrayIndexOutOfBoundsException, DOMException {
		final Document ownerDocument = node.getOwnerDocument(); //get the node owner document
		if(ownerDocument == null) { //if there is no owner document
			throw new IllegalArgumentException("Node " + node + " has no owner document.");
		}
		final NodeList childNodeList = node.getChildNodes(); //get a reference to the child nodes
		final int childNodeCount = childNodeList.getLength(); //find out how many child nodes there are
		if(startChildIndex < 0 || (endChildIndex > startChildIndex && startChildIndex >= childNodeCount)) //if the start child index is out of range
			throw new ArrayIndexOutOfBoundsException(startChildIndex); //throw an exception indicating the illegal index
		if(endChildIndex < 0 || (endChildIndex > startChildIndex && endChildIndex > childNodeCount)) //if the ending child index is out of range
			throw new ArrayIndexOutOfBoundsException(endChildIndex); //throw an exception indicating the illegal index
		final DocumentFragment documentFragment = ownerDocument.createDocumentFragment(); //create a document fragment to hold the nodes
		Node lastAddedNode = null; //show that we haven't added any nodes, yet
		for(int i = endChildIndex - 1; i >= startChildIndex; --i) { //starting from the end, look at all the indexes before the ending index
			final Node childNode = childNodeList.item(i); //find the item at the given index
			if(remove) { //if we should remove the node
				node.removeChild(childNode); //remove the child node
			}
			if(lastAddedNode == null) //for the first node we add
				documentFragment.appendChild(childNode); //append the removed child to the document fragment
			else
				//for all other nodes
				documentFragment.insertBefore(childNode, lastAddedNode); //insert this child before the last one
			lastAddedNode = childNode; //show that we just added another node
		}
		return documentFragment; //return the document fragment we created
	}

	/**
	 * Gets the specified attribute value only if it is defined.
	 * @param element The element to check for the specified attribute.
	 * @param name The name of the attribute to retrieve.
	 * @return The attribute value, if the attribute is defined, else <code>null</code>.
	 * @see Element#hasAttribute(String)
	 * @see Element#getAttribute(String)
	 */
	public static String getDefinedAttribute(final Element element, final String name) {
		//retrieve and return the attribute if it exists, else return null
		return element.hasAttribute(name) ? element.getAttribute(name) : null;
	}

	/**
	 * Gets the specified attribute value only if it is defined.
	 * @param element The element to check for the specified attribute.
	 * @param namespaceURI The namespace of the attribute to retrieve.
	 * @param localName The local name of the attribute.
	 * @return The attribute value, if the attribute is defined, else <code>null</code>.
	 * @see Element#hasAttributeNS(String, String)
	 * @see Element#getAttributeNS(String, String)
	 */
	public static String getDefinedAttributeNS(final Element element, final String namespaceURI, final String localName) {
		//retrieve and return the attribute if it exists, else return null
		return element.hasAttributeNS(namespaceURI, localName) ? element.getAttributeNS(namespaceURI, localName) : null;
	}

	/**
	 * Retrieves the first child node of the specified type.
	 * @param node The node of which child elements will be examined.
	 * @param nodeType The type of node to return.
	 * @return The first node of the given type, or <code>null</code> if there is no such node of the given type.
	 */
	public static Node getChildNode(final Node node, final int nodeType) {
		final NodeList childNodeList = node.getChildNodes(); //get a reference to the child nodes
		final int childCount = childNodeList.getLength(); //find out how many children there are
		for(int i = 0; i < childCount; ++i) { //look at each of the children
			final Node childNode = childNodeList.item(i); //get a reference to this node
			if(childNode.getNodeType() == nodeType) { //if this node is of the correct type
				return childNode; //return this child node
			}
		}
		return null; //indicate that no matching nodes were found
	}

	/**
	 * Retrieves the first child node not of the specified type.
	 * @param node The node of which child elements will be examined.
	 * @param nodeType The type of node not to return.
	 * @return The first node not of the given type, or <code>null</code> if there is no node not of the given type.
	 */
	public static Node getChildNodeNot(final Node node, final int nodeType) {
		final NodeList childNodeList = node.getChildNodes(); //get a reference to the child nodes
		final int childCount = childNodeList.getLength(); //find out how many children there are
		for(int i = 0; i < childCount; ++i) { //look at each of the children
			final Node childNode = childNodeList.item(i); //get a reference to this node
			if(childNode.getNodeType() != nodeType) { //if this node is not of the specified type
				return childNode; //return this child node
			}
		}
		return null; //indicate that no non-matching nodes were found
	}

	/**
	 * Retrieves the nodes contained in child nodes of type {@link Node#ELEMENT_NODE}.
	 * @param node The node from which child elements will be returned.
	 * @see Node#ELEMENT_NODE
	 */
	public static List<Element> getChildElements(final Node node) {
		final List<Element> childElements = new ArrayList<Element>(); //create a list to hold the elements
		final NodeList childNodeList = node.getChildNodes(); //get a reference to the child nodes
		final int childCount = childNodeList.getLength(); //find out how many children there are
		for(int i = 0; i < childCount; ++i) { //look at each of the children
			final Node childNode = childNodeList.item(i); //get a reference to this node
			switch(childNode.getNodeType()) { //see which type of node this is
				case Node.ELEMENT_NODE: //if this is an element
					childElements.add((Element)childNode); //add this child element
					break;
			}
		}
		return childElements; //return the child element we collected
	}

	/**
	 * Returns a list of child nodes with a given type and node name. The special wildcard name "*" returns nodes of all names. If <code>deep</code> is set to
	 * <code>true</code>, returns a list of all descendant nodes with a given name, in the order in which they would be encountered in a preorder traversal of the
	 * node tree.
	 * @param node The node the child nodes of which will be searched.
	 * @param nodeType The type of nodes to include.
	 * @param nodeName The name of the node to match on. The special value "*" matches all nodes.
	 * @param deep Whether or not matching child nodes of each matching child node, etc. should be included.
	 * @return A new iterator object containing all the matching nodes.
	 */
	public static List<Node> getNodesByName(final Node node, final int nodeType, final String nodeName, final boolean deep) {
		final List<Node> nodeList = new ArrayList<Node>(); //crate a new node list to return
		final boolean matchAllNodes = "*".equals(nodeName); //see if they passed us the wildcard character TODO use a constant here
		final NodeList childNodeList = node.getChildNodes(); //get the list of child nodes
		final int childNodeCount = childNodeList.getLength(); //get the number of child nodes
		for(int childIndex = 0; childIndex < childNodeCount; childIndex++) { //look at each child node
			final Node childNode = childNodeList.item(childIndex); //get a reference to this node
			if(childNode.getNodeType() == nodeType) { //if this is a node of the correct type
				if((matchAllNodes || childNode.getNodeName().equals(nodeName))) //if node has the correct name (or they passed us the wildcard character)
					nodeList.add(childNode); //add this node to the list
				if(deep) //if each of the children should check for matching nodes as well
					nodeList.addAll(getNodesByName(childNode, nodeType, nodeName, deep)); //get this node's matching child nodes by name and add them to our list
			}
		}
		return nodeList; //return the list we created and filled
	}

	/**
	 * Returns a list of child nodes with a given type, namespace URI, and local name. The special wildcard name "*" returns nodes of all local names. If
	 * <code>deep</code> is set to <code>true</code>, returns a list of all descendant nodes with a given name, in the order in which they would be encountered in
	 * a preorder traversal of the node tree.
	 * @param node The node the child nodes of which will be searched.
	 * @param nodeType The type of nodes to include.
	 * @param namespaceURI The URI of the namespace of nodes to return. The special value "*" matches all namespaces.
	 * @param localName The local name of the node to match on. The special value "*" matches all local names.
	 * @param deep Whether or not matching child nodes of each matching child node, etc. should be included.
	 * @return A new list containing all the matching nodes.
	 */
	public static List<Node> getNodesByNameNS(final Node node, final int nodeType, final String namespaceURI, final String localName, final boolean deep) {
		return getNodesByNameNS(node, nodeType, namespaceURI, localName, deep, new ArrayList<Node>()); //gather the nodes into a list and return the list
	}

	/**
	 * Gathers child nodes with a given type, namespace URI, and local name. The special wildcard name "*" returns nodes of all local names. If <code>deep</code>
	 * is set to <code>true</code>, returns a list of all descendant nodes with a given name, in the order in which they would be encountered in a preorder
	 * traversal of the node tree.
	 * @param node The node the child nodes of which will be searched.
	 * @param nodeType The type of nodes to include.
	 * @param namespaceURI The URI of the namespace of nodes to return. The special value "*" matches all namespaces.
	 * @param localName The local name of the node to match on. The special value "*" matches all local names.
	 * @param deep Whether or not matching child nodes of each matching child node, etc. should be included.
	 * @param nodes The collection into which the nodes will be gathered.
	 * @return A new list containing all the matching nodes.
	 */
	public static <C extends Collection<Node>> C getNodesByNameNS(final Node node, final int nodeType, final String namespaceURI, final String localName,
			final boolean deep, final C nodes) {
		final boolean matchAllNamespaces = "*".equals(namespaceURI); //see if they passed us the wildcard character for the namespace URI TODO use a constant here
		final boolean matchAllLocalNames = "*".equals(localName); //see if they passed us the wildcard character for the local name TODO use a constant here
		final NodeList childNodeList = node.getChildNodes(); //get the list of child nodes
		final int childNodeCount = childNodeList.getLength(); //get the number of child nodes
		for(int childIndex = 0; childIndex < childNodeCount; childIndex++) { //look at each child node
			final Node childNode = childNodeList.item(childIndex); //get a reference to this node
			if(childNode.getNodeType() == nodeType) { //if this is a node of the correct type
				final String nodeNamespaceURI = childNode.getNamespaceURI(); //get the node's namespace URI
				final String nodeLocalName = childNode.getLocalName(); //get the node's local name
				if(matchAllNamespaces || Objects.equals(namespaceURI, nodeNamespaceURI)) { //if we should match all namespaces, or the namespaces match
					if(matchAllLocalNames || localName.equals(nodeLocalName)) { //if we should match all local names, or the local names match
						nodes.add(childNode); //add this node to the list
					}
				}
				if(deep) { //if each of the children should check for matching nodes as well
					getNodesByNameNS(childNode, nodeType, namespaceURI, localName, deep, nodes); //get this node's matching child nodes by name and add them to our collection
				}
			}
		}
		return nodes; //return the collection we filled
	}

	/**
	 * Retrieves the text of the node contained in child nodes of type {@link Node#TEXT_NODE}, extracting text deeply.
	 * @param node The node from which text will be retrieved.
	 * @return The data of all <code>Text</code> descendant nodes, which may be the empty string.
	 * @see Node#TEXT_NODE
	 * @see Text#getData()
	 */
	public static String getText(final Node node) {
		return getText(node, true); //get text deeply
	}

	/**
	 * Retrieves the text of the node contained in child nodes of type {@link Node#TEXT_NODE}, extracting text deeply.
	 * @param node The node from which text will be retrieved.
	 * @param blockElementNames The names of elements considered "block" elements, which will be separated from other elements using whitespace.
	 * @return The data of all <code>Text</code> descendant nodes, which may be the empty string.
	 * @see Node#TEXT_NODE
	 * @see Text#getData()
	 */
	public static String getText(final Node node, final Set<String> blockElementNames) {
		final StringBuilder stringBuilder = new StringBuilder(); //create a string buffer to collect the text data
		getText(node, blockElementNames, true, stringBuilder); //collect the text in the string buffer
		return stringBuilder.toString(); //convert the string buffer to a string and return it
	}

	/**
	 * Retrieves the text of the node contained in child nodes of type {@link Node#TEXT_NODE}. If <code>deep</code> is set to <code>true</code> the text of all
	 * descendant nodes in document (depth-first) order; otherwise, only text of direct children will be returned.
	 * @param node The node from which text will be retrieved.
	 * @param deep Whether text of all descendants in document order will be returned.
	 * @return The data of all <code>Text</code> children nodes, which may be the empty string.
	 * @see Node#TEXT_NODE
	 * @see Text#getData()
	 */
	public static String getText(final Node node, final boolean deep) {
		final StringBuilder stringBuilder = new StringBuilder(); //create a string buffer to collect the text data
		getText(node, Collections.<String> emptySet(), deep, stringBuilder); //collect the text in the string buffer
		return stringBuilder.toString(); //convert the string buffer to a string and return it
	}

	/**
	 * Retrieves the text of the node contained in child nodes of type <code>Node.Text</code>. If <code>deep</code> is set to <code>true</code> the text of all
	 * descendant nodes in document (depth-first) order; otherwise, only text of direct children will be returned.
	 * @param node The node from which text will be retrieved.
	 * @param blockElementNames The names of elements considered "block" elements, which will be separated from other elements using whitespace.
	 * @param deep Whether text of all descendants in document order will be returned.
	 * @param stringBuilder The buffer to which text will be added.
	 * @return The given string builder.
	 * @see Node#TEXT_NODE
	 * @see Text#getData()
	 */
	public static StringBuilder getText(final Node node, final Set<String> blockElementNames, final boolean deep, final StringBuilder stringBuilder) {
		final NodeList childNodeList = node.getChildNodes(); //get a reference to the child nodes
		final int childCount = childNodeList.getLength(); //find out how many children there are
		for(int i = 0; i < childCount; ++i) { //look at each of the children
			final Node childNode = childNodeList.item(i); //get a reference to this node
			switch(childNode.getNodeType()) { //see which type of node this is
				case Node.TEXT_NODE: //if this is a text node
					stringBuilder.append(((Text)childNode).getData()); //append this text node data to the string buffer
					break;
				case Node.CDATA_SECTION_NODE: //if this is a CDATA node
					stringBuilder.append(((Text)childNode).getData()); //append this text node data to the string buffer
					break;
				case Node.ELEMENT_NODE: //if this is an element
					if(deep) { //if we should get deep text
						final boolean isBlockElement = !blockElementNames.isEmpty() && blockElementNames.contains(node.getNodeName()); //separate block elements
						if(isBlockElement) {
							stringBuilder.append(' ');
						}
						getText((Element)childNode, blockElementNames, deep, stringBuilder); //append the text of this element
						if(isBlockElement) {
							stringBuilder.append(' ');
						}
					}
					break;
			}
		}
		return stringBuilder;
	}

	/**
	 * Determines whether the given element has an ancestor with the given namespace and name.
	 * @param element The element the ancestors of which to check.
	 * @param ancestorElementNamespaceURI The namespace URI of the ancestor element to check for.
	 * @param ancestorElementName The name of the ancestor element to check for.
	 * @return <code>true</code> if an ancestor element with the given namespace URI and name was found.
	 */
	public static boolean hasAncestorElementNS(Element element, final String ancestorElementNamespaceURI, final String ancestorElementName) {
		while((element = asInstance(element.getParentNode(), Element.class)) != null) { //keep looking at parents until we run out of elements and hit the document
			if(Objects.equals(element.getNamespaceURI(), ancestorElementNamespaceURI) && element.getNodeName().equals(ancestorElementName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Creates and inserts a new element encompassing the text of a given text node.
	 * @param textNode The text node to split into a new element.
	 * @param element The element to insert.
	 * @param startIndex The index of the first character to include in the element.
	 * @param endIndex The index immediately after the last character to include in the element.
	 * @return The inserted element.
	 * @throws DOMException
	 *           <ul>
	 *           <li>INDEX_SIZE_ERR: Raised if the specified offset is negative or greater than the number of 16-bit units in <code>data</code>.</li>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly.</li>
	 *           </ul>
	 */
	public static Element insertElement(final Text textNode, final Element element, final int startIndex, final int endIndex) throws DOMException {
		final Text splitTextNode = splitText(textNode, startIndex, endIndex); //split the text node into pieces
		final Element parentElement = (Element)splitTextNode.getParentNode(); //get the text node's parent
		parentElement.replaceChild(element, splitTextNode); //replace the split text node with the element that will enclose it
		element.appendChild(splitTextNode); //add the split text node to the given element
		return element; //return the inserted enclosing element
	}

	/**
	 * Splits a text node into one, two, or three text nodes and replaces the original text node with the new ones.
	 * @param textNode The text node to split.
	 * @param startIndex The index of the first character to be split.
	 * @param endIndex The index immediately after the last character to split.
	 * @return The new text node that contains the text selected by the start and ending indexes.
	 * @throws DOMException
	 *           <ul>
	 *           <li>INDEX_SIZE_ERR: Raised if the specified offset is negative or greater than the number of 16-bit units in <code>data</code>.</li>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly.</li>
	 *           </ul>
	 */
	public static Text splitText(Text textNode, int startIndex, int endIndex) throws DOMException {
		if(startIndex > 0) { //if the split text doesn't begin at the start of the text
			textNode = textNode.splitText(startIndex); //split off the first part of the text
			endIndex -= startIndex; //the ending index will now slide back because the start index is sliding back
			startIndex = 0; //we'll do the next split at the first of this string
		}
		if(endIndex < textNode.getLength()) { //if there will be text left after the split
			textNode.splitText(endIndex); //split off the text after the node
		}
		return textNode; //return the node in the middle
	}

	/**
	 * Removes the specified child node from the parent node, and promoting all the children of the child node to be children of the parent node.
	 * @param parentNode The parent of the node to remove.
	 * @param childNode The node to remove, promoting its children in the process. //TODO list exceptions
	 */
	public static void pruneChild(final Node parentNode, final Node childNode) { //TODO maybe rename to excise child
		//promote all the child node's children to be children of the parent node
		while(childNode.hasChildNodes()) { //while the child node has children
			final Node node = childNode.getFirstChild(); //get the first child of the node
			childNode.removeChild(node); //remove the child's child
			parentNode.insertBefore(node, childNode); //insert the child's child before its parent (the parent node's child)
		}
		parentNode.removeChild(childNode); //remove the child, now that its children have been promoted
	}

	/**
	 * Removes the indexed nodes starting at <code>startChildIndex</code> up to but not including <code>endChildIndex</code>.
	 * @param node The node from which child nodes should be removed.
	 * @param startChildIndex The index of the first child to remove.
	 * @param endChildIndex The index directly after the last child to remove. Must be greater than <code>startChildIndex</code> or no action will occur.
	 * @throws ArrayIndexOutOfBoundsException Thrown if either index is negative, if the start index is greater than or equal to the number of children, or if the
	 *           ending index is greater than the number of children. TODO should we throw an exception is startChildIndex>endChildIndex, like String.substring()?
	 * @throws DOMException
	 *           <ul>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly.</li>
	 *           </ul>
	 */
	public static void removeChildren(final Node node, final int startChildIndex, final int endChildIndex) throws ArrayIndexOutOfBoundsException, DOMException {
		final NodeList childNodeList = node.getChildNodes(); //get a reference to the child nodes
		final int childNodeCount = childNodeList.getLength(); //find out how many child nodes there are
		if(startChildIndex < 0 || startChildIndex >= childNodeCount) //if the start child index is out of range
			throw new ArrayIndexOutOfBoundsException(startChildIndex); //throw an exception indicating the illegal index
		if(endChildIndex < 0 || endChildIndex > childNodeCount) //if the ending child index is out of range
			throw new ArrayIndexOutOfBoundsException(endChildIndex); //throw an exception indicating the illegal index
		for(int i = endChildIndex - 1; i >= startChildIndex; --i)
			//starting from the end, look at all the indexes before the ending index
			node.removeChild(childNodeList.item(i)); //find the item at the given index and remove it
	}

	/**
	 * Removes all named child nodes deeply.
	 * @param node The node the named children of which should be removed.
	 * @param nodeNames The names of the nodes to remove.
	 * @throws NullPointerException if the given node and/or node names is <code>null</code>.
	 */
	public static void removeChildrenByName(final Node node, final Set<String> nodeNames) {
		removeChildrenByName(node, nodeNames, true);
	}

	/**
	 * Removes all named child nodes.
	 * @param node The node the named children of which should be removed.
	 * @param nodeNames The names of the nodes to remove.
	 * @param deep If all descendants should be examined.
	 * @throws NullPointerException if the given node and/or node names is <code>null</code>.
	 */
	public static void removeChildrenByName(final Node node, final Set<String> nodeNames, final boolean deep) {
		final NodeList childNodeList = node.getChildNodes(); //get the list of child nodes
		for(int childIndex = childNodeList.getLength() - 1; childIndex >= 0; childIndex--) { //look at each child node in reverse to prevent problems from removal
			final Node childNode = childNodeList.item(childIndex); //get a reference to this node
			if(nodeNames.contains(childNode.getNodeName())) { //if this node is to be removed
				node.removeChild(childNode); //remove it
			} else if(deep) { //if we should remove deeply
				removeChildrenByName(childNode, nodeNames, deep);
			}
		}
	}

	/**
	 * Removes all child elements with the given name and attribute value.
	 * @param node The node the named child elements of which should be removed.
	 * @param elementName The names of the elements to remove.
	 * @param attributeName The name of the attribute to check.
	 * @param attributeValue The value of the attribute indicating removal.
	 */
	public static void removeChildElementsByNameAttribute(final Node node, final String elementName, final String attributeName, final String attributeValue) {
		removeChildElementsByNameAttribute(node, elementName, attributeName, attributeValue, true);
	}

	/**
	 * Removes all child elements with the given name and attribute value.
	 * @param node The node the named child elements of which should be removed.
	 * @param elementName The names of the elements to remove.
	 * @param attributeName The name of the attribute to check.
	 * @param attributeValue The value of the attribute indicating removal.
	 * @param deep If all descendants should be examined.
	 */
	public static void removeChildElementsByNameAttribute(final Node node, final String elementName, final String attributeName, final String attributeValue,
			final boolean deep) {
		final NodeList childNodeList = node.getChildNodes(); //get the list of child nodes
		for(int childIndex = childNodeList.getLength() - 1; childIndex >= 0; childIndex--) { //look at each child node in reverse to prevent problems from removal
			final Node childNode = childNodeList.item(childIndex); //get a reference to this node
			if(childNode.getNodeType() == Node.ELEMENT_NODE && childNode.getNodeName().equals(elementName)
					&& ((Element)childNode).getAttribute(attributeName).equals(attributeValue)) { //if this node is to be removed
				node.removeChild(childNode); //remove it
			} else if(deep) { //if we should remove deeply
				removeChildElementsByNameAttribute(childNode, elementName, attributeName, attributeValue, deep);
			}
		}
	}

	/**
	 * Renames an element by creating a new element with the specified name, cloning the original element's children, and replacing the original element with the
	 * new, renamed clone. While this method's purpose is renaming, because of DOM restrictions it must remove the element and replace it with a new one, which is
	 * reflected by the method's name.
	 * @param element The element to rename.
	 * @param namespaceURI The new element namespace.
	 * @param localName The new element local name.
	 * @return The new element with the specified name which replaced the old element. //TODO list exceptions
	 */
	public static Element replaceElementNS(final Element element, final String namespaceURI, final String localName) {
		final Document document = element.getOwnerDocument(); //get the owner document
		final Element newElement = document.createElementNS(namespaceURI, localName); //create the new element
		appendClonedAttributeNodesNS(newElement, element); //clone the attributes TODO testing
		appendClonedChildNodes(newElement, element, true); //deep-clone the child nodes of the element and add them to the new element
		final Node parentNode = element.getParentNode(); //get the parent node, which we'll need for the replacement
		parentNode.replaceChild(newElement, element); //replace the old element with the new one
		return newElement; //return the element we created
	}

	//TODO fix, comment private static final int tabDelta=2;	//
	private static final String tabString = "|\t"; //TODO fix to adjust automatically to tabDelta, comment

	/**
	 * Prints a tree representation of the document to the standard output.
	 * @param document The document to print.
	 * @param printStream The stream (e.g. <code>System.out</code>) to use for printing the tree.
	 */
	public static void printTree(final Document document, final PrintStream printStream) {
		if(document != null) //if we have a root element
			printTree(document.getDocumentElement(), 0, printStream); //dump the contents of the root element
		else
			//if we don't have a root element
			printStream.println("Empty document."); //TODO fix, comment, i18n
	}

	/**
	 * Prints a tree representation of the element to the standard output.
	 * @param element The element to print.
	 * @param printStream The stream (e.g. <code>System.out</code>) to use for printing the tree.
	 */
	public static void printTree(final Element element, final PrintStream printStream) {
		printTree(element, 0, printStream); //if we're called normally, we'll dump starting at the first tab position
	}

	/**
	 * Prints a tree representation of the element to the standard output starting at the specified tab position.
	 * @param element The element to print.
	 * @param tabPos The zero-based tab position to which to align.
	 * @param printStream The stream (e.g. <code>System.out</code>) to use for printing the tree.
	 */
	protected static void printTree(final Element element, int tabPos, final PrintStream printStream) {
		for(int i = 0; i < tabPos; ++i)
			printStream.print(tabString); //TODO fix to adjust automatically to tabDelta, comment
		printStream.print("[Element] "); //TODO fix to adjust automatically to tabDelta, comment
		printStream.print("<" + element.getNodeName()); //print the element name

		final NamedNodeMap attributeMap = element.getAttributes(); //get the attributes
		for(int i = attributeMap.getLength() - 1; i >= 0; --i) { //look at each attribute
			final Attr attribute = (Attr)attributeMap.item(i); //get a reference to this attribute
			printStream.print(" " + attribute.getName() + "=\"" + attribute.getValue() + "\""); //print the attribute and its value
			printStream.print(" (" + attribute.getNamespaceURI() + ")"); //print the attribute namespace
		}
		if(element.getChildNodes().getLength() == 0) //if there are no child nodes
			printStream.print('/'); //show that this is an empty element
		printStream.println("> (namespace URI=\"" + element.getNamespaceURI() + "\" local name=\"" + element.getLocalName() + "\")");
		if(element.getChildNodes().getLength() > 0) { //if there are child nodes
			for(int childIndex = 0; childIndex < element.getChildNodes().getLength(); childIndex++) { //look at each child node
				Node node = element.getChildNodes().item(childIndex); //look at this node
				printTree(node, tabPos, printStream); //print the node to the stream
				//TODO process the child elements
			}

			for(int i = 0; i < tabPos; ++i)
				printStream.print(tabString); //TODO fix to adjust automatically to tabDelta, comment
			printStream.print("[/Element] "); //TODO fix to adjust automatically to tabDelta, comment
			printStream.println("</" + element.getNodeName() + '>');
		}
	}

	/**
	 * Prints a tree representation of the node to the given pring stream starting at the specified tab position.
	 * @param node The node to print.
	 * @param printStream The stream (e.g. <code>System.out</code>) to use for printing the tree.
	 */
	public static void printTree(final Node node, final PrintStream printStream) {
		printTree(node, 0, printStream); //if we're called normally, we'll dump starting at the first tab position
	}

	/**
	 * Prints a tree representation of the node to the given pring stream starting at the specified tab position.
	 * @param node The node to print.
	 * @param tabPos The zero-based tab position to which to align.
	 * @param printStream The stream (e.g. <code>System.out</code>) to use for printing the tree.
	 */
	protected static void printTree(final Node node, int tabPos, final PrintStream printStream) {
		switch(node.getNodeType()) { //see which type of object this is
			case Node.ELEMENT_NODE: //if this is an element

				//TODO fix for empty elements

				//TODO del tabPos+=tabDelta;	//TODO check this; maybe static classes don't have recursive-aware functions
				printTree((Element)node, tabPos + 1, printStream); //comment, check to see if we need the typecast
				//TODO del tabPos-=tabDelta;	//TODO check this; maybe static classes don't have recursive-aware functions
				break;
			case Node.TEXT_NODE: //if this is a text node
				for(int i = 0; i < tabPos + 1; ++i)
					printStream.print(tabString); //TODO fix to adjust automatically to tabDelta, comment
				printStream.print("[Text] "); //TODO fix to adjust automatically to tabDelta, comment
				printStream.println(Strings.replace(node.getNodeValue(), '\n', "\\n")); //print the text of this node
				break;
			case Node.COMMENT_NODE: //if this is a comment node
				for(int i = 0; i < tabPos + 1; i += ++i)
					printStream.print(tabString); //TODO fix to adjust automatically to tabDelta, comment
				printStream.print("[Comment] "); //TODO fix to adjust automatically to tabDelta, comment
				printStream.println(Strings.replace(node.getNodeValue(), '\n', "\\n")); //print the text of this node
				break;
		}
	}

	/**
	 * Converts an XML document to a string. If an error occurs converting the document to a string, the normal object string will be returned.
	 * @param document The XML document to convert.
	 * @return A string representation of the XML document.
	 */
	public static String toString(final Document document) {
		try {
			return new XMLSerializer(true).serialize(document); //serialize the document to a string, formatting the XML output
		} catch(final IOException ioException) { //if an IO exception occurs
			return ioException.getMessage() + ' ' + document.toString(); //ask the document to convert itself to a string
		}
	}

	/**
	 * Converts an XML element to a string. If an error occurs converting the element to a string, the normal object string will be returned.
	 * @param element The XML element to convert.
	 * @return A string representation of the XML element.
	 */
	public static String toString(final Element element) {
		return new XMLSerializer(true).serialize(element); //serialize the element to a string, formatting the XML output
	}

	/**
	 * Determines the default XML namespace for the given MIME content type.
	 * @param mediaType The media type for which a default namespace should be found, or <code>null</code> if the media type is not known.
	 * @return The default XML namespace URI used by resources of the given content type, or <code>null</code> if there is no default namespace URI or the default
	 *         namespace URI is not known.
	 */
	public static URI getDefaultNamespaceURI(final ContentType mediaType) {
		if(mediaType != null) { //if we were given a valid media type
			if(XHTML.isHTML(mediaType)) //if this is one of the HTML media types
				return HTML.XHTML_NAMESPACE_URI; //return the XHTML media type
			/*TODO fix for OEB; create some sort of registration facility
			else if(mediaType.match(ContentType.TEXT_PRIMARY_TYPE, OEB.X_OEB1_DOCUMENT_SUBTYPE)) //if this is an OEB 1.x document
				return OEB.OEB1_DOCUMENT_NAMESPACE_URI; //return the OEB 1.x document namespace
			*/
		}
		return null; //show that we can't find a default namespace URI
	}

	/**
	 * Searches the attributes of the given node for a definition of a namespace URI for the given prefix. If the prefix is not defined for the given element, its
	 * ancestors are searched if requested. If the prefix is not defined anywhere up the hieararchy, <code>null</code> is returned. If the prefix is defined, it
	 * is returned logically: a blank declared namespace will return <code>null</code>.
	 * @param element The element which should be searched for a namespace definition, along with its ancestors.
	 * @param prefix The namespace prefix for which a definition should be found, or <code>null</code> for a default attribute.
	 * @param resolve Whether the entire tree hierarchy should be searched.
	 * @return The defined namespace URI for the given prefix, or <code>null</code> if none is defined.
	 */
	public static String getNamespaceURI(final Element element, final String prefix, final boolean resolve) {
		//get the namespace URI declared for this prefix
		final String namespaceURI = getDeclaredNamespaceURI(element, prefix, resolve);
		if(namespaceURI != null && namespaceURI.length() > 0) { //if a namespace is defined that isn't the empty string
			return namespaceURI; //return that namespace URI
		} else { //if the namespace is null or is the empty string
			return null; //the empty string is the same as a null namespace
		}
	}

	/**
	 * Searches the attributes of the given node for a definition of a namespace URI for the given prefix. If the prefix is not defined for the given element, its
	 * ancestors are searched if requested. If the prefix is not defined anywhere up the hieararchy, <code>null</code> is returned. If the prefix is defined, it
	 * is returned literally: a blank declared namespace will return the empty string. This allows differentiation between a declared empty namespace and no
	 * declared namespace.
	 * @param element The element which should be searched for a namespace definition, along with its ancestors.
	 * @param prefix The namespace prefix for which a definition should be found, or <code>null</code> for a default attribute.
	 * @param resolve Whether the entire tree hierarchy should be searched.
	 * @return The defined namespace URI for the given prefix, or <code>null</code> if none is defined.
	 */
	public static String getDeclaredNamespaceURI(final Element element, final String prefix, final boolean resolve) {
		String namespaceURI = null; //assume we won't find a matching namespace
		if(prefix != null) { //if they specified a prefix
			if(prefix.equals(XMLNS_NAMESPACE_PREFIX)) //if this is the "xmlns" prefix
				return XMLNS_NAMESPACE_URI.toString(); //return the namespace URI for "xmlns:"
			else if(prefix.equals(XML_NAMESPACE_PREFIX)) //if this is the "xml" prefix
				return XML_NAMESPACE_URI.toString(); //return the namespace URI for "xml:"
			//see if this element has "xmlns:prefix" defined, and if so, retrieve it
			if(element.hasAttributeNS(XMLNS_NAMESPACE_URI.toString(), prefix)) //TODO fix for empty namespace strings
				namespaceURI = element.getAttributeNS(XMLNS_NAMESPACE_URI.toString(), prefix);
		} else { //if no prefix was specified
			//see if there is an "xmlns" attribute defined in the "http://www.w3.org/2000/xmlns/" namespace
			if(element.hasAttributeNS(XMLNS_NAMESPACE_URI.toString(), XMLNS_NAMESPACE_PREFIX))
				namespaceURI = element.getAttributeNS(XMLNS_NAMESPACE_URI.toString(), XMLNS_NAMESPACE_PREFIX);
		}
		//if we didn't find a matching namespace definition for this node, search up the chain
		//(unless no prefix was specified, and we can't use the default namespace)
		if(namespaceURI == null) {
			final Node parentNode = element.getParentNode(); //get the parent node
			//if we should resolve, there is a parent, and it's an element (not the document)
			if(resolve && parentNode != null && parentNode.getNodeType() == Node.ELEMENT_NODE) {
				namespaceURI = getDeclaredNamespaceURI((Element)parentNode, prefix, resolve); //continue the search up the chain
			}
		}
		return namespaceURI; //return the namespace URI we found
	}

	/**
	 * Checks to ensure that all namespaces for the element and its attributes are properly declared using the appropriate <code>xmlns=</code> or
	 * <code>xmlns:prefix=</code> attribute declaration.
	 * @param element The element the namespace of which to declare.
	 */
	public static void ensureNamespaceDeclarations(final Element element) {
		ensureNamespaceDeclarations(element, null, false); //ensure namespace declarations only for this element and its attributes, adding any declarations to the element itself
	}

	/**
	 * Checks to ensure that all namespaces for the element and its attributes are properly declared using the appropriate <code>xmlns=</code> or
	 * <code>xmlns:prefix=</code> attribute declaration. The children of this element are optionally checked.
	 * @param element The element the namespace of which to declare.
	 * @param declarationElement The element on which to declare missing namespaces, or <code>null</code> if namespaces should always be declared on the element
	 *          on which they are found missing.
	 * @param deep Whether all children and their descendants are also recursively checked for namespace declarations.
	 */
	public static void ensureNamespaceDeclarations(final Element element, final Element declarationElement, final boolean deep) {
		final NameValuePair<String, String>[] prefixNamespacePairs = getUndeclaredNamespaces(element); //get the undeclared namespaces for this element
		declareNamespaces(declarationElement != null ? declarationElement : element, prefixNamespacePairs); //declare the undeclared namespaces, using the declaration element if provided
		if(deep) { //if we should recursively check the children of this element
			final NodeList childElementList = element.getChildNodes(); //get a list of the child nodes
			for(int i = 0; i < childElementList.getLength(); ++i) { //look at each node
				final Node node = childElementList.item(i); //get a reference to this node
				if(node.getNodeType() == Node.ELEMENT_NODE) { //if this is an element
					ensureNamespaceDeclarations((Element)node, declarationElement, deep); //process the namespaces for this element and its descendants
				}
			}
		}
	}

	/**
	 * Checks to ensure that all namespaces for the <em>child elements</em> and their attributes are properly declared using the appropriate <code>xmlns=</code>
	 * or <code>xmlns:prefix=</code> attribute declaration. If a child element does not have a necessary namespace declaration, the declaration is added to the
	 * same parent element all the way down the hierarchy if there are no conflicts. If there is a conflict, the namespace is added to the child element itself.
	 * <p>
	 * This method is useful for adding namespace attributes to the top level of a fragment that contains unknown content that preferably should be lef
	 * undisturbed.
	 * </p>
	 * @param parentElement The element to which all child namespaces will be added if there are no conflicts.
	 */
	public static void ensureChildNamespaceDeclarations(final Element parentElement) {
		ensureChildNamespaceDeclarations(parentElement, parentElement); //ensure the child namespace declarations for all children of this element, showing that this element is the parent
	}

	/**
	 * Checks to ensure that all namespaces for the <em>child elements</em> and their attributes are properly declared using the appropriate <code>xmlns=</code>
	 * or <code>xmlns:prefix=</code> attribute declaration. If a child element does not have a necessary namespace declaration, the declaration is added to the
	 * same parent element all the way down the hierarchy if there are no conflicts. If there is a conflict, the namespace is added to the child element itself.
	 * <p>
	 * This method is useful for adding namespace attributes to the top level of a fragment that contains unknown content that preferably should be lef
	 * undisturbed.
	 * </p>
	 * @param rootElement The element to which all child namespaces will be added if there are no conflicts.
	 * @param parentElement The element the children of which are currently being checked.
	 */
	protected static void ensureChildNamespaceDeclarations(final Element rootElement, final Element parentElement) {
		final NodeList childElementList = parentElement.getChildNodes(); //get a list of the child nodes
		for(int childIndex = 0; childIndex < childElementList.getLength(); ++childIndex) { //look at each child node
			final Node childNode = childElementList.item(childIndex); //get a reference to this node
			if(childNode.getNodeType() == Node.ELEMENT_NODE) { //if this is an element
				final Element childElement = (Element)childNode; //cast the node to an element
				final NameValuePair<String, String>[] prefixNamespacePairs = getUndeclaredNamespaces(childElement); //get the undeclared namespaces for the child element
				for(int i = 0; i < prefixNamespacePairs.length; ++i) { //look at each name/value pair
					final NameValuePair<String, String> prefixNamespacePair = prefixNamespacePairs[i]; //get this name/value pair representing a prefix and a namespace
					final String prefix = prefixNamespacePair.getName(); //get the prefix
					final String namespaceURI = prefixNamespacePair.getValue(); //get the namespace
					if(getDeclaredNamespaceURI(rootElement, prefix, true) == null) { //if the rooot element does not have this prefix defined, it's OK to add it to the parent element
						declareNamespace(rootElement, prefix, namespaceURI); //declare this namespace on the root element
					} else { //if the parent element has already defined this namespace
						declareNamespace(childElement, prefix, namespaceURI); //declare the namespace on the child element
					}
				}
				ensureChildNamespaceDeclarations(rootElement, childElement); //check the children of the child element
			}
		}
	}

	/**
	 * Gets the namespace declarations this element needs so that all namespaces for the element and its attributes are properly declared using the appropriate
	 * <code>xmlns=</code> or <code>xmlns:prefix=</code> attribute declaration. The children of this element are optionally checked.
	 * @param element The element for which namespace declarations should be checked.
	 * @param deep Whether all children and their descendants are also recursively checked for namespace declarations.
	 * @return An array of name/value pairs. The name of each is the the prefix to declare, or <code>null</code> if no prefix is used. The value of each is the
	 *         URI string of the namespace being defined, or <code>null</code> if no namespace is used.
	 */
	@SuppressWarnings("unchecked")
	//we create an array from a generic list, creating this warning to suppress
	public static NameValuePair<String, String>[] getUndeclaredNamespaces(final Element element) {
		final List<NameValuePair<String, String>> prefixNamespacePairList = new ArrayList<NameValuePair<String, String>>(); //create a new list in which to store name/value pairs of prefixes and namespaces
		if(!isNamespaceDeclared(element, element.getPrefix(), element.getNamespaceURI())) //if the element doesn't have the needed declarations
			prefixNamespacePairList.add(new NameValuePair<String, String>(element.getPrefix(), element.getNamespaceURI())); //add this prefix and namespace to the list of namespaces needing to be declared
		final NamedNodeMap attributeNamedNodeMap = element.getAttributes(); //get the map of attributes
		final int attributeCount = attributeNamedNodeMap.getLength(); //find out how many attributes there are
		for(int i = 0; i < attributeCount; ++i) { //look at each attribute
			final Attr attribute = (Attr)attributeNamedNodeMap.item(i); //get this attribute
			//as attribute namespaces are not inherited, don't check namespace
			//  declarations for attributes if they have neither prefix nor
			//  namespace declared
			if(attribute.getPrefix() != null || attribute.getNamespaceURI() != null) {
				if(!isNamespaceDeclared(element, attribute.getPrefix(), attribute.getNamespaceURI())) //if the attribute doesn't have the needed declarations
					prefixNamespacePairList.add(new NameValuePair<String, String>(attribute.getPrefix(), attribute.getNamespaceURI())); //add this prefix and namespace to the list of namespaces needing to be declared
			}
		}
		return prefixNamespacePairList.toArray(new NameValuePair[prefixNamespacePairList.size()]); //return an array of the prefixes and namespaces we gathered
	}

	/**
	 * Declares a prefix for the given namespace using the appropriate <code>xmlns=</code> or <code>xmlns:prefix=</code> attribute declaration.
	 * <p>
	 * It is assumed that the namespace is used at the level of the given element. If the namespace prefix is already declared somewhere up the tree, and that
	 * prefix is assigned to the same namespace, no action occurs.
	 * </p>
	 * @param element The element for which the namespace should be declared.
	 * @param prefix The prefix to declare, or <code>null</code> if no prefix is used.
	 * @param namespaceURI The namespace being defined, or <code>null</code> if no namespace is used.
	 */
	public static void ensureNamespaceDeclaration(final Element element, final String prefix, final String namespaceURI) {
		if(!isNamespaceDeclared(element, prefix, namespaceURI)) { //if this namespace isn't declared for this element
			declareNamespace(element, prefix, namespaceURI); //declare the namespace
		}
	}

	/**
	 * Determines if the given namespace is declared using the appropriate <code>xmlns=</code> or <code>xmlns:prefix=</code> attribute declaration either on the
	 * given element or on any element up the tree.
	 * <p>
	 * The "xmlns" and "xml" prefixes and namespaces always result in <code>true</code> being returned, because they never need to be declared.
	 * </p>
	 * @param element The element for which the namespace should be declared.
	 * @param prefix The prefix to declare, or <code>null</code> if no prefix is used.
	 * @param namespaceURI The namespace being defined, or <code>null</code> if no namespace is used.
	 * @return <code>true</code> if the namespace is sufficiently declared, either on the given element or somewhere up the element hierarchy.
	 */
	public static boolean isNamespaceDeclared(final Element element, final String prefix, final String namespaceURI) {
		if(XMLNS_NAMESPACE_PREFIX.equals(prefix) && XMLNS_NAMESPACE_URI.toString().equals(namespaceURI)) //we don't need to define the "xmlns:" prefix
			return true;
		if(prefix == null && XMLNS_NAMESPACE_URI.toString().equals(namespaceURI)) //we don't need to define the "xmlns" name
			return true;
		if(XML_NAMESPACE_PREFIX.equals(prefix) && XML_NAMESPACE_URI.toString().equals(namespaceURI)) //we don't need to define the "xml" prefix
			return true;
		//find out what namespace is defined for the prefix anywhere up the hierarchy
		final String declaredNamespaceURI = getDeclaredNamespaceURI(element, prefix, true);
		if(declaredNamespaceURI != null) { //if some element declared a namespace for this prefix
			if(declaredNamespaceURI.length() == 0) { //if an empty namespace was declared
				if(namespaceURI == null) { //if we expected a null namespace
					return true; //show that the namespace is declared as expected
				}
			} else if(declaredNamespaceURI.equals(namespaceURI)) { //if a normal namespace was declared and it's the same one we expect
				return true; //show that the namespace is declared as expected
			}
		}
		return false; //show that we couldn't find this namespace declared using the given prefix
	}

	/**
	 * Declares prefixes for the given namespaces using the appropriate <code>xmlns=</code> or <code>xmlns:prefix=</code> attribute declaration for the given
	 * element.
	 * @param declarationElement The element on which the namespaces should be declared.
	 * @param previxNamespacePairs An array of name/value pairs. The name of each is the the prefix to declare, or <code>null</code> if no prefix is used. The
	 *          value of each is the URI string of the namespace being defined, or <code>null</code> if no namespace is used.
	 */
	public static void declareNamespaces(final Element declarationElement, final NameValuePair<String, String>[] prefixNamespacePairs) {
		for(int i = 0; i < prefixNamespacePairs.length; ++i) { //look at each name/value pair
			final NameValuePair<String, String> nameValuePair = prefixNamespacePairs[i]; //get this name/value pair representing a prefix and a namespace
			declareNamespace(declarationElement, nameValuePair.getName(), nameValuePair.getValue()); //declare this namespace
		}
	}

	/**
	 * Declares a prefix for the given namespace using the appropriate <code>xmlns=</code> or <code>xmlns:prefix=</code> attribute declaration for the given
	 * element.
	 * @param declarationElement The element on which the namespace should be declared.
	 * @param prefix The prefix to declare, or <code>null</code> if no prefix is used.
	 * @param namespaceURI The namespace being defined, or <code>null</code> if no namespace is used.
	 */
	public static void declareNamespace(final Element declarationElement, final String prefix, String namespaceURI) {
		if(XMLNS_NAMESPACE_PREFIX.equals(prefix) && XMLNS_NAMESPACE_URI.toString().equals(namespaceURI)) //we don't need to define the "xmlns" prefix
			return;
		if(prefix == null && XMLNS_NAMESPACE_URI.toString().equals(namespaceURI)) //we don't need to define the "xmlns" name
			return;
		if(XML_NAMESPACE_PREFIX.equals(prefix) && XML_NAMESPACE_URI.toString().equals(namespaceURI)) //we don't need to define the "xml" prefix
			return;
		if(namespaceURI == null) //if no namespace URI was given
			namespaceURI = ""; //we'll declare an empty namespace URI
		if(prefix != null) { //if we were given a prefix
			//create an attribute in the form, xmlns:prefix="namespaceURI" TODO fix for attributes that may use the same prefix for different namespace URIs
			declarationElement.setAttributeNS(XMLNS_NAMESPACE_URI.toString(), createQName(XMLNS_NAMESPACE_PREFIX, prefix), namespaceURI);
		} else { //if we weren't given a prefix
			//create an attribute in the form, xmlns="namespaceURI" TODO fix for attributes that may use the same prefix for different namesapce URIs
			declarationElement.setAttributeNS(XMLNS_NAMESPACE_URI.toString(), XMLNS_NAMESPACE_PREFIX, namespaceURI);
		}
	}

	/**
	 * Parses the given text as an XML fragment using the given document builder as a parser.
	 * @param fragmentText The text of the XML fragment.
	 * @param documentBuilder The document builder to use to parse the fragment.
	 * @param defaultNamespaceURI The default namespace URI of the fragment, or <code>null</code> if there is no default namespace
	 * @return A document fragment containing the parsed contents of the given fragment text.
	 * @throws SAXException if there was an error parsing the fragment.
	 */
	public static DocumentFragment parseFragment(final String fragmentText, final DocumentBuilder documentBuilder, final String defaultNamespaceURI)
			throws SAXException {
		final StringBuilder stringBuilder = new StringBuilder("<?xml version='1.0' encoding='UTF-8'?>"); //TODO use constants if we can
		stringBuilder.append("<fragment");
		if(defaultNamespaceURI != null) { //if a default namespace was given
			stringBuilder.append(" xmlns='").append(defaultNamespaceURI).append("'"); //xmlns="defaultNamespaceURI"
		}
		stringBuilder.append(">").append(fragmentText).append("</fragment>");
		try {
			final Document document = documentBuilder.parse(new ByteArrayInputStream(stringBuilder.toString().getBytes(UTF_8))); //parse the bytes of the string
			return extractChildren(document.getDocumentElement()); //extract the children of the fragment document element and return them as a document fragment
		} catch(final IOException ioException) { //we should never get an I/O exception reading from a string
			throw new AssertionError(ioException);
		}
	}

}