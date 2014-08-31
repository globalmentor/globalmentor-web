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

import java.io.*;
import java.util.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import static com.globalmentor.java.Characters.SPACE_CHAR;
import static com.globalmentor.text.xml.XML.*;

import com.globalmentor.io.ByteOrderMark;
import com.globalmentor.io.Files;
import com.globalmentor.io.ParseEOFException;
import com.globalmentor.io.ParseUnexpectedDataException;
import com.globalmentor.io.URIInputStreamable;
import com.globalmentor.log.Log;
import com.globalmentor.model.ObjectHolder;
import com.globalmentor.net.URIs;
import com.globalmentor.text.xml.schema.*;

//TODO del all the XMLUndefinedEntityReferenceException throws when we don't need them anymore, in favor of XMLWellFormednessException

/**
 * Class which parses an input stream containing XML data. This class also supports a "tidy" option which, if enabled, makes this a non-conforming XML
 * processor, but means that not-well-formed input will be converted to well-formed XML. Specifically, the tidy option does the following:
 * <ul>
 * <li>Defaults to the ISO-8859-1 character encoding if an XML declaration is not present.</li>
 * <li>Allows a missing system ID for the <code>&lt;DOCTYPE</code>.</li>
 * <li>Ignores document type declarations inside the root element.</li>
 * <li>Ignores elements with invalid names (e.g. <code>&lt;![endif]&gt;</code>)</li>
 * <li>Automatically converts tags which should be empty elements.</li>
 * <li>Accepts unquoted element attributes.</li>
 * <li>Accepts attributes with no values (e.g. <code>&lt;hr noshade/&gt;</code>)</li>
 * <li>Accepts document type declarations in lowercase (i.e. <code>&lt;!doctype...</code>).</li>
 * <li>Accepts "PUBLIC" and "SYSTEM" identifiers in any case.</li>
 * <li>Changes all tag names to lowercase. TODO later change this to check the schema</li>
 * <li>Changes all attribute names to lowercase. TODO later change this to check the schema</li>
 * <li>Ensures the name of any document type matches the name of the document element.</li>
 * </ul>
 * TODO make tidy fix things like <li>All rights reserved.</p></li>; right now it truncates by changing the </p> to an </li>
 * <p>
 * When loading an external DTD subset or an external entity which has a public ID, this class first searches for the file stored in the same location as the
 * class itself. The filename should be the same as the public ID with illegal filename characters (e.g. '/' and '\') replaced with the underscore character
 * ('_') and the extension removed. This allows for faster performance by making relieving network access and even making it unnecesary for the file to even
 * exist at the location specified by its system ID.
 * </p>
 * <p>
 * If possible, each instance of this class caches any DTDs loaded. This means performance increases can be achieved if the same XML processor is used for
 * loading multiple documents which reference the same external DTD subset.
 * </p>
 * @author Garret Wilson
 * @deprecated
 */
public class XMLProcessor implements URIInputStreamable {

	/** Whether we should automatically tidy non-well-formed XML. */
	private boolean tidy = false;

	/** @return Whether we should automatically tidy non-well-formed XML. */
	public boolean isTidy() {
		return tidy;
	}

	/**
	 * Sets whether non-well-formed XML should be automatically tidied.
	 * @param newTidy Whether we should automatically tidy non-well-formed XML.
	 */
	public void setTidy(final boolean newTidy) {
		tidy = newTidy;
	}

	/** In tidy mode, the document public and system IDs to use if none is available. */
	private XMLExternalID tidyDocumentTypeExternalID = null;

	/**
	 * Sets an overriding public and system ID to use in tidy mode. If set, any internal DTD subset will still be in effect but the tidy external public and
	 * system IDs will be used for an external DTD subset, whether or not one is defined in the document.
	 * @pararm publicID The overriding document type public ID.
	 * @pararm systemID The overriding document type system ID.
	 * @see #isTidy
	 */
	public void setTidyDocumentTypeExternalID(final String publicID, final String systemID) {
		tidyDocumentTypeExternalID = new XMLExternalID(publicID, systemID); //create a new external ID for tidying TODO eventually make an option to remove this
	}

	/**
	 * @return The overriding document type external ID to be used when tidying, or <code>null</code> if the document type should not be overridden when tidying.
	 */
	protected XMLExternalID getTidyDocumentTypeExternalID() {
		return tidyDocumentTypeExternalID;
	}

	/** Whether or not we should validate the document. */
	//TODO fix	private boolean ShouldValidate=true;

	/** @return Whether or not we should validate the document. */
	//TODO fix		protected boolean isShouldValidate() {return ShouldValidate;}

	/** Whether or not we should recognize and process any stylesheets we encounter. */
	//TODO fix	private boolean ShouldProcessStyleSheets=true;

	/** @return Whether or not we should recognize and process any stylesheets we encounter. */
	//TODO fix		protected boolean isShouldProcessStyleSheets() {return ShouldProcessStyleSheets;}

	/** Whether or not we should apply any stylesheets to the document. */
	//TODO fix	private boolean ShouldApplyStyleSheets=true;

	/** @return Whether or not we should apply any stylesheets to the document. */
	//TODO fix		protected boolean isShouldApplyStyleSheets() {return ShouldApplyStyleSheets;}

	/**
	 * The interface to use to locate external files. This can be this class or another class, depending on which constructor is used.
	 * @see XMLProcessor#XMLProcessor
	 */
	private URIInputStreamable uriInputStreamable = null;

	/**
	 * @return The interface to use to locate external files. This can be this class or another class, depending on which constructor has been used.
	 * @see XMLProcessor#XMLProcessor
	 */
	public URIInputStreamable getURIInputStreamable() {
		return uriInputStreamable;
	}

	/**
	 * Sets the interface to use to locate external files. This can be this class or another class, depending on which constructor has been used.
	 * @param newURIInputStreamable A class implementing the interface that can find files and return an <code>InputStream</code> to them.
	 * @see XMLProcessor#XMLProcessor
	 */
	protected void setURIInputStreamable(final URIInputStreamable newURIInputStreamable) {
		uriInputStreamable = newURIInputStreamable;
	}

	/** The object used to process schemas. */
	private XMLSchemaProcessor schemaProcessor = null;

	/** @return The object used to process schemas. */
	public XMLSchemaProcessor getSchemaProcessor() {
		return schemaProcessor;
	}

	//TODO fix, make accessor methods, and comment
	private Map nestedEntityReferenceMap = new HashMap(); //create a new map that will tell us which references we're nesting to prevent recursion

	/**
	 * This is the default document type which will be assigned to a document if that document has a document type. This will storing the default entity
	 * references used by this XML document, and is populated with amp, lt, gt, apos, quot, as XML recognizes these by default.
	 */
	private XMLDocumentType DocumentType = new XMLDocumentType(null); //create a default document type TODO use the XMLDOMImplementation function here

	/**
	 * @return The document type for the document being processed. This will be the same document type that is returned by the document's getXMLDocumentType()
	 *         function if the document being processed has a document type.
	 * @see XMLDocument#getXMLDocumentTYpe
	 */
	protected XMLDocumentType getXMLDocumentType() {
		return DocumentType;
	}

	/**
	 * Whether or not we should allow end tags to be unnamed. Note that if this is set to true, we will not technically be adhering to the XML specification.
	 */
	private boolean AllowUnnamedEndTags = false; //default to false, because the XML specification does not allow unnamed end tags

	/** @return Whether or not we should allow end tags to be unnamed. */
	public boolean isAllowUnnamedEndTags() {
		return AllowUnnamedEndTags;
	}

	/**
	 * Set whether or not we should allow end tags to be unnamed. Note that if this is set to true, we will not technically be adhering to the XML specification.
	 * @param new AllowUnnamedEndTags Whether or not we should allow end tags to be unnamed.
	 */
	public void setAllowUnnamedEndTags(boolean newAllowUnnamedEndTags) {
		AllowUnnamedEndTags = newAllowUnnamedEndTags;
	}

	/**
	 * Default constructor. Sets the input stream locator to this class so that we can find our own files.
	 */
	public XMLProcessor() {
		setURIInputStreamable(this); //show that we'll locate our own files
	}

	/**
	 * Constructor that sets the interface to use to locate external files.
	 * @param newURIInputSTreamable A class implementing the interface that can find files and return an <code>InputStream</code> to them.
	 */
	public XMLProcessor(final URIInputStreamable newURIInputStreamable) {
		setURIInputStreamable(newURIInputStreamable); //show which input stream locator we'll use
		schemaProcessor = new XMLSchemaProcessor(); //create a new schema processor to use
	}

	//TODO fix	public reportError(final XMLException xmlException);

	/**
	 * Skips all XML whitespace characters, and returns the number of characters skipped. Resets peeking.
	 * @param reader The reader from which to retrieve characters.
	 * @throws IOException Thrown when an i/o error occurs.
	 * @throws ParseEOFException Thrown when the end of the input stream is reached unexpectedly.
	 * @return The number of characters skipped.
	 */
	public long skipWhitespaceCharacters(final XMLReader reader) throws IOException, ParseEOFException {
		return reader.skipChars(WHITESPACE_CHARS); //skip any whitespace characters and return the number of characters skipped
	}

	/**
	 * Skips all XML whitespace characters, and returns the number of characters skipped. No exception is thrown if the end of the file is reached. Resets
	 * peeking.
	 * @param reader The reader from which to retrieve characters.
	 * @throws IOException Thrown when an i/o error occurs.
	 * @return The number of characters skipped.
	 */
	public long skipWhitespaceCharactersEOF(final XMLReader reader) throws IOException {
		return reader.skipCharsEOF(WHITESPACE_CHARS); //skip any whitespace characters and return the number of characters skipped without throwing an exception if we find the end of the file
	}

	/**
	 * Returns an input stream from given URL. This function is used when we're acting as our own <code>URIInputStreamabler</code>. This implementation only works
	 * with URLs.
	 * @param uri A complete URI to a file.
	 * @return An input stream to the contents of the file represented by the given URL.
	 * @throws IOException Thrown if an I/O error occurred.
	 * @see #getURIInputStreamable
	 */
	public InputStream getInputStream(final URI uri) throws IOException {
		return uri.toURL().openConnection().getInputStream(); //since we don't know any better (they didn't specify their own file locator), try to open a connection to the URL directly and return an input stream to that connection
	}

	/**
	 * Creates a reader to read the specified input stream. Encoding is preread and correctly interpreted.
	 * @param inputStream The input stream from which to get the XML data; mark/reset must be supported.
	 * @param sourceObject The source of the data (e.g. a String, File, or URL).
	 * @return A reader from which the file may be read.
	 * @throws IllegalArgumentException if mark/reset is not supported by the given input stream.
	 * @throws IOException Thrown if an I/O error occurred.
	 */
	protected XMLReader createReader(final InputStream xmlInputStream, final Object sourceObject) throws IOException { //TODO comment exceptions
		final ObjectHolder<ByteOrderMark> bom = new ObjectHolder<>();
		final ObjectHolder<String> declaredEncodingName = new ObjectHolder<>();
		Charset charset = XML.detectXMLCharset(xmlInputStream, bom, declaredEncodingName);
		if(charset != null) {
			/*TODO fix
			if(!isTidy() && UTF_16.equalsIgnoreCase(characterEncoding.getFamily()) && characterEncoding.getByteOrderMark().length == 0)
				throw new XMLWellFormednessException(XMLWellFormednessException.INVALID_FORMAT, new Object[] {}, 0, 0, sourceObject != null ? sourceObject.toString()
						: ""); //show that the UTF-16 had no Byte Order Mark
			*/
		} else { //if no charset is indicated, assume the default
			charset = DEFAULT_CHARSET;
		}
		if(bom.isPresent()) { //skip the BOM if present
			xmlInputStream.skip(bom.getObject().getLength());
		}
		final InputStreamReader xmlInputStreamReader = new InputStreamReader(xmlInputStream, charset); //create a new input stream reader with the correct encoding
		//TODO catch any unsupported encoding exception here and throw our own
		final XMLReader xmlReader = new XMLReader(xmlInputStreamReader, sourceObject); //create a new XML reader with our correctly encoded reader
		xmlReader.tidy = isTidy(); //TODO fix
		return xmlReader; //TODO fix
	}

	/**
	 * Creates a reader to read the specified external file.
	 * @param context The context object, usually a URI.
	 * @param systemID The relative or absolute filename.
	 * @throws IOException Thrown when an i/o error occurs.
	 * @return A reader from which the file may be read.
	 * @throws IOException Thrown if an I/O error occurred.
	 */
	protected XMLReader createReader(final Object context, final String systemID) throws IOException { //TODO comment exceptions
		return createReader(context, null, systemID); //create a reader without checking a public ID
	}

	/**
	 * Creates a reader to read the specified external file, specifying both a public ID and a system ID. If the public ID is a valid public ID and represents a
	 * public ID which is stored in our resources in the same directory as this class, our local version of the file will be loaded instead of the physical file
	 * at the literal location indicated by the systemID. Note that any resource must be saved with the same name as its public ID, with all invalid filename
	 * characters replaced with underscores, and no extension for it to be recognized.
	 * @param context The context object, usually a URI.
	 * @param publicID The public identifier of the file to load, or <code>null</code> if there is no public ID.
	 * @param systemID The relative or absolute filename, which will only be used if there is no public ID or the public ID is not recognized.
	 * @throws IOException Thrown when an i/o error occurs.
	 * @return A reader from which the file may be read.
	 * @throws IOException Thrown if an I/O error occurred.
	 * @see FileConstants#ILLEGAL_FILENAME_CHARACTERS
	 */
	protected XMLReader createReader(final Object context, final String publicID, final String systemID) throws IOException { //TODO comment exceptions
		try {
			//create a URI for the system ID (there should always be one) in relation to our reader's context, if any
			final URI uri = systemID != null ? URIs.createURI(context, systemID) : null;
			//convert the public ID (if there is one) to a valid filaname and see if we can load this
			//  resource locally rather than from the literal file location
			final String localFilename = publicID != null ? Files.encodeCrossPlatformFilename(publicID) : null; //get the name of the file if it were to be stored locally
			final InputStream localResourceInputStream = publicID != null ? getClass().getResourceAsStream(localFilename) : null;
			//if we couldn't find the resource locally, create an input stream to the system ID URI we already created, using our input stream locator
			final InputStream inputStream = localResourceInputStream != null ? localResourceInputStream : getURIInputStreamable().getInputStream(uri);
			return createReader(inputStream, uri); //create a reader from the input stream, specifying the URI from whence it came (at least where it would have came had we not found it locally)
			//TODO when will all these readers/connections/streams get closed?
			//TODO check for FileNotFoundException and throw something appropriate for our XML processor
		} catch(URISyntaxException uriSyntaxException) {
			throw new IOException(uriSyntaxException.toString()); //TODO fix with a better XML-related error
		}
	}

	/**
	 * Creates a reader to read from the specified entity, regardless of whether this entity is internal or external.
	 * @param context The context object, usually a URI.
	 * @throws IOException Thrown when an i/o error occurs.
	 * @return A reader from which the entity may be read.
	 */
	protected XMLReader createEntityReader(final Object context, final XMLEntity entity) throws IOException {
		XMLReader entityReader = null; //the reader which we will construct and return; we don't know what it will be, yet
		if(entity.getSystemID() != null) { //if there is a system identifier for this entity, it's an external entity
			Log.trace("Before creating reader for entity public ID", entity.getPublicId(), "system ID", entity.getSystemId());
			return createReader(context, entity.getPublicID(), entity.getSystemID()); //TODO testing; comment
		} else { //if this is an internal entity reference
			entityReader = new XMLReader(entity.getText(), "Entity \"" + entity.getNodeName() + "\" from \"" + entity.getSourceName() + "\""); //create a string reader from the text of the entity, giving our entity name for the name of the reader TODO i18n TODO show whether this is a parameter or a general entity reference

			entityReader.tidy = isTidy(); //TODO fix

			entityReader.setLineIndex(entity.getLineIndex()); //pretend we're reading where the entity was located in that file, so any errors will show the correct information
			entityReader.setCharIndex(entity.getCharIndex()); //pretend we're reading where the entity was located in that file, so any errors will show the correct information
		}
		return entityReader; //return the entity reader we created
	}

	/**
	 * Parses an input stream that supposedly contains a character reference.
	 * @param reader The reader from which to retrieve characters.
	 * @throws IOException Thrown when an i/o error occurs.
	 * @throws XMLSyntaxException Thrown when there is a syntax error in the XML file.
	 * @throws XMLWellFormednessException Thrown when there is a well-formedness error in the XML file.
	 * @throws ParseUnexpectedDataException Thrown when an unexpected character is found. //TODO del @throws ParseEOFException Thrown when the end of the input
	 *           stream is reached unexpectedly.
	 * @return The character encoded by the character reference.
	 */
	protected char parseCharacterReference(final XMLReader reader) throws IOException, XMLSyntaxException, XMLWellFormednessException,
			ParseUnexpectedDataException { //TODO del, ParseEOFException
		try {
			String characterReferenceString = reader.readDelimitedString(CHARACTER_REF_START, CHARACTER_REF_END); //read all the characters in the character reference between its start and end
			int numberBase; //we'll determine which number base to use
			if(characterReferenceString.length() > 0 && characterReferenceString.charAt(0) == 'x') { //if the character reference begins with 'x' TODO make this a constant
				numberBase = 16; //show that this is a hex number
				characterReferenceString = characterReferenceString.substring(1, characterReferenceString.length()); //chop off the first character
			} else
				//if this is not a hexadecimal number
				numberBase = 10; //it should be a decimal number
			final char c = (char)Integer.parseInt(characterReferenceString, numberBase); //convert the number to a character TODO what if it's an invalid number string? probably throw an error
			if(!isChar(c)) //if the character isn't a valid character
				throw new XMLWellFormednessException(XMLWellFormednessException.LEGAL_CHARACTER, new Object[] { CHARACTER_REF_START + characterReferenceString
						+ CHARACTER_REF_END }, reader.getLineIndex(), reader.getCharIndex(), reader.getName()); //show that this character reference is invalid TODO make sure we tell what base the reference string is, and perhaps give a better indication of its location
			return c; //return the character referenced
		} catch(final ParseEOFException parseEOFException) { //if we run out of data
			throw new XMLWellFormednessException(parseEOFException, ResourceBundle.getBundle(XMLResources.RESOURCE_BUNDLE_BASE_NAME).getString(
					XMLResources.RESOURCE_PREFIX + CHARACTER_REFERENCE_RESOURCE_ID), ResourceBundle.getBundle(XMLResources.RESOURCE_BUNDLE_BASE_NAME).getString(
					XMLResources.RESOURCE_PREFIX + UNKNOWN_RESOURCE_ID)); //show that we couldn't find the end of the character reference
		}
	}

	/**
	 * Parses an input stream that supposedly contains an entity reference and returns either the entity referenced by this entity reference, or a
	 * <code>String</code> containing the entity reference markup if no entity was found and this is not a well-formedness error. If a missing entity would cause
	 * a well-formedness error, an exception is thrown.
	 * @param reader The reader from which to retrieve characters.
	 * @param ownerDocument The document which will own the constructed entity.
	 * @throws IOException Thrown when an i/o error occurs.
	 * @throws XMLSyntaxException Thrown when there is a syntax error in the XML file.
	 * @throws XMLWellFormednessException Thrown when there is a well-formedness error in the XML file.
	 * @throws ParseUnexpectedDataException Thrown when an unexpected character is found. //TODO del @throws ParseEOFException Thrown when the end of the input
	 *           stream is reached unexpectedly.
	 * @throws XMLUndefinedEntityReferenceException Thrown if a named entity reference has not been defined.
	 * @return The entity referenced by this entity reference or a <code>String</code> containing the entity reference markup if none exists yet none is required
	 *         for well-formedness.
	 */
	protected Object parseEntityReference(final XMLReader reader, final XMLDocument ownerDocument) throws IOException, XMLSyntaxException,
			XMLWellFormednessException, ParseUnexpectedDataException, XMLUndefinedEntityReferenceException {
		String entityReferenceName = ""; //this will be the name of our entity reference
		try {
			entityReferenceName = reader.readDelimitedString("&", ";"); //read all the characters in the entity reference between its start and end TODO make these constant
			//if the document has a document type, get a reference to it, otherwise get a reference to our default document type
			final XMLDocumentType documentType = ownerDocument.getXMLDocumentType() != null ? ownerDocument.getXMLDocumentType() : getXMLDocumentType();
			if(documentType.getEntityXMLNamedNodeMap().containsKey(entityReferenceName)) { //if our map of entity references has this entity name
				final XMLEntity entity = (XMLEntity)documentType.getEntityXMLNamedNodeMap().getNamedItem(entityReferenceName); //find the entity referenced by this entity reference
				if(!entity.isParsedEntity()) //if this is an unparsed entity
					throw new XMLWellFormednessException(XMLWellFormednessException.PARSED_ENTITY, new Object[] { entity.getNodeName() }, reader.getLineIndex(),
							reader.getCharIndex(), reader.getName()); //show that we don't accept unparsed entity references here TODO we need to give a better position in the error, here
				return entity; //return the entity referenced by this name
			} else { //if we don't recognize this entity reference name
				if(ownerDocument.getXMLDocumentType() == null || ownerDocument.getXMLDeclaration().isStandalone()) //if there is no DTD or this document is declared standalone TODO what about "internal DTD subset which contains no parameter entity references?"
					throw new XMLWellFormednessException(XMLWellFormednessException.ENTITY_DECLARED, new Object[] { entityReferenceName }, reader.getLineIndex(),
							reader.getCharIndex(), reader.getName()); //show that this entity reference was not found
				else
					//if the document has a DTD, not having a declared entity is not a *well-formedness* error, although it probably makes the document not valid
					return new String("&" + entityReferenceName + ";"); //TODO check, comment, use constants
			}
		} catch(final ParseEOFException parseEOFException) { //if we run out of data
			throw new XMLWellFormednessException(parseEOFException, ResourceBundle.getBundle(XMLResources.RESOURCE_BUNDLE_BASE_NAME).getString(
					XMLResources.RESOURCE_PREFIX + ENTITY_REFERENCE_RESOURCE_ID), entityReferenceName != null ? entityReferenceName : ResourceBundle.getBundle(
					XMLResources.RESOURCE_BUNDLE_BASE_NAME).getString(XMLResources.RESOURCE_PREFIX + UNKNOWN_RESOURCE_ID)); //show that we couldn't find the end of the entity reference
		}
	}

	/**
	 * Parses an input stream that supposedly contains a parameter entity reference.
	 * @param reader The reader from which to retrieve characters.
	 * @param ownerDocument The document which will own the constructed entity.
	 * @param parameterEntityMap Any parameter entities we've collected along the way while parsing this DTD subset. TODO eventually use a DTD subset object
	 * @throws IOException Thrown when an i/o error occurs.
	 * @throws XMLSyntaxException Thrown when there is a syntax error in the XML file.
	 * @throws XMLWellFormednessException Thrown when there is a well-formedness error in the XML file.
	 * @throws ParseUnexpectedDataException Thrown when an unexpected character is found. //TODO del @throws ParseEOFException Thrown when the end of the input
	 *           stream is reached unexpectedly.
	 * @throws XMLUndefinedEntityReferenceException Thrown if a named entity reference has not been defined.
	 * @return The entity referenced by this entity reference.
	 */
	protected XMLEntity parseParameterEntityReference(final XMLReader reader, final XMLDocument ownerDocument, final XMLNamedNodeMap parameterEntityMap)
			throws IOException, XMLSyntaxException, XMLWellFormednessException, ParseUnexpectedDataException, XMLUndefinedEntityReferenceException {
		final long entityReferenceLineIndex = reader.getLineIndex(), entityReferenceCharIndex = reader.getCharIndex() + 1; //make a note of where the entity reference will begin
		String entityReferenceName = ""; //this will receive the name of our entity reference
		try {
			entityReferenceName = reader.readDelimitedString(PARAMETER_ENTITY_REF_START, PARAMETER_ENTITY_REF_END); //read all the characters in the entity reference between its start and end
			//if the document has a document type, get a reference to it, otherwise get a reference to our default document type
			final XMLDocumentType documentType = ownerDocument.getXMLDocumentType() != null ? ownerDocument.getXMLDocumentType() : getXMLDocumentType();
			if(documentType.getParameterEntityXMLNamedNodeMap().containsKey(entityReferenceName)) //if our map of entity references has this entity name
				return (XMLEntity)documentType.getParameterEntityXMLNamedNodeMap().getNamedItem(entityReferenceName); //return the entity referenced by this name
			else { //if we don't recognize this entity reference name (it isn't stored in the document type
				if(parameterEntityMap.containsKey(entityReferenceName)) //if our map of entity references we've collected recently has this entity name
					return (XMLEntity)parameterEntityMap.getNamedItem(entityReferenceName); //return the entity referenced by this name
				else
					//if we can't find the parameter entity in the document time entities, nor in the entities we've collected so far
					throw new XMLUndefinedEntityReferenceException(entityReferenceName, entityReferenceLineIndex, entityReferenceCharIndex, reader.getName()); //show that we don't recognize the entity reference TODO we should probably make a distinction between general and parameter entity references in the error; perhaps both functions could even somehow be combined
			}
		} catch(final ParseEOFException parseEOFException) { //if we run out of data
			throw new XMLWellFormednessException(parseEOFException, ResourceBundle.getBundle(XMLResources.RESOURCE_BUNDLE_BASE_NAME).getString(
					XMLResources.RESOURCE_PREFIX + PARAMETER_ENTITY_REFERENCE_RESOURCE_ID), entityReferenceName != null ? entityReferenceName : ResourceBundle.getBundle(
					XMLResources.RESOURCE_BUNDLE_BASE_NAME).getString(XMLResources.RESOURCE_PREFIX + UNKNOWN_RESOURCE_ID)); //show that we couldn't find the end of the parameter entity reference
		}
	}

	/**
	 * Parses an attribute-value pair, constructs an XMLAttribute object, and returns it.
	 * @param reader The reader from which to retrieve characters.
	 * @param ownerDocument The document which will own the nodes encountered.
	 * @throws IOException Thrown when an i/o error occurs.
	 * @throws XMLSyntaxException Thrown when there is a syntax error in the XML file.
	 * @throws XMLWellFormednessException Thrown when there is a well-formedness error in the XML file.
	 * @throws XMLUndefinedEntityReferenceException Thrown if a named entity reference has not been defined.
	 * @throws ParseUnexpectedDataException Thrown when an unexpected character is found. //TODO del @throws ParseEOFException Thrown when the end of the input
	 *           stream is reached unexpectedly.
	 * @return An XMLAttribute object with the values of the read attribute-value pair.
	 * @see XMLAttribute
	 */
	protected XMLAttribute parseAttribute(final XMLReader reader, final XMLDocument ownerDocument) throws IOException, XMLSyntaxException,
			XMLWellFormednessException, XMLUndefinedEntityReferenceException, ParseUnexpectedDataException {
		final long attributeLineIndex = reader.getLineIndex(), attributeCharIndex = reader.getCharIndex() + 1; //make a note of where the attribute will begins
		String attributeName = ""; //this will receive the name of our attribute
		try {
			attributeName = reader.readStringUntilChar(WHITESPACE_CHARS + EQUAL_CHAR + '>'); //find the attribute name (which will end before the equals sign or whitespace), allowing ourselves to find the end-of-tag character in case we're tidying TODO use a constant here
			/*TODO fix
					  if(reader.peekChar()=='>' && !isTidy()) //if this is the end of the tag, meaning this is an attribute with no value at the end of the tag, and we're not tidying
							reader.readExpectedChar(WHITESPACE_CHARS+EQUAL_CHAR); //
			*/

			if(!isName(attributeName)) //if this isn't a valid name
				throw new XMLWellFormednessException(XMLWellFormednessException.INVALID_NAME, new Object[] { attributeName }, reader.getLineIndex(),
						reader.getCharIndex(), reader.getName()); //show that this isn't a valid XML name
			final XMLAttribute attribute = new XMLAttribute(ownerDocument, attributeName, attributeLineIndex, attributeCharIndex); //create a new attribute-value pair object

			skipWhitespaceCharacters(reader); //skip all whitespace characters (we already know the next character is valid, except maybe the end-of-tag character if we weren't tidying, which will be caught below)
			if(isTidy()) { //if we're tidying, there could be attributes with no values, such as <hr noshade />
				final char equalChar = reader.peekChar(); //see what the next character is
				if(equalChar != EQUAL_CHAR) { //if this isn't the '=' we expect
					reader.unread(SPACE_CHAR); //make sure there's whitespace after the attribute, since we just skipped all whitespace to find out there was no equals sign
					attribute.setValue(attributeName); //set the attribute value to the same as its name (e.g. noshade="noshade")
					return attribute; //default to an attribute without a specified value
				}
			}
			reader.readExpectedChar(EQUAL_CHAR); //read the equals sign; if we read the end-of-tag character earlier and we're not tidying, this will catch it
			skipWhitespaceCharacters(reader); //skip all whitespace characters (we don't yet know if the next non-whitespace character is valid)

			String attributeValueDelimiters; //we'll store the ending delimiter for the attribute value in this variable
			boolean isTidyingValue; //whether or not we're tidying this attribute value
			final char attributeValueFirstChar = reader.peekChar(); //see which character comes next
			if(ATTRIBUTE_VALUE_DELIMITERS.indexOf(attributeValueFirstChar) == -1 && isTidy()) { //if this is not a valid attribute value delimiter, but we're tidying the document
				attributeValueDelimiters = WHITESPACE_CHARS + '>'; //we'll accept whitespace or an end of the tag after the attribute value as a valid delimiter TODO use a constant here
				isTidyingValue = true; //show that we're tidying this value
			} else { //if this is a valid attribute value delimiter, or we're not tidying
				attributeValueDelimiters = String.valueOf(reader.readExpectedChar(ATTRIBUTE_VALUE_DELIMITERS)); //read the delimiter of the attribute value, expecting a single or double quote
				isTidyingValue = false; //show that we're not tidying this value
			}
			if(!parseAttributeValue(reader, ownerDocument, attribute, attributeValueDelimiters)) //parse the attribute; if we couldn't find the end of the attribute value
				throw new XMLWellFormednessException(XMLWellFormednessException.EOF, new Object[] {
						ResourceBundle.getBundle(XMLResources.RESOURCE_BUNDLE_BASE_NAME).getString(XMLResources.RESOURCE_PREFIX + ATTRIBUTE_RESOURCE_ID),
						attribute.getNodeName() }, reader.getLineIndex(), reader.getCharIndex(), reader.getName()); //show that we couldn't find the end of the attribute
			if(isTidyingValue) { //if we're tidying this value, we need to know whether or not to discard the following character
				/*TODO fix
								reader.resetPeek(); //reset peeking so that we'll really be peeking the next character to come
								final char characterAfterValue=reader.peekChar();	//see which character comes after the value
								if(isWhitespace(characterAfterValue)==-1) {	//if this is *not* a whitespace character (e.g. it is a '>')
									reader.readExpectedChar(characterAfterValue);	//discard the character (we already know what it will be)
								}
				*/
			} else { //if we're not tidying this value
				reader.readExpectedChar(attributeValueDelimiters); //read the ending delimiter of the attribute value (we already know what it will be)
			}
			return attribute; //return the attribute we created
		} catch(final ParseEOFException parseEOFException) { //if we run out of data
			throw new XMLWellFormednessException(parseEOFException, ResourceBundle.getBundle(XMLResources.RESOURCE_BUNDLE_BASE_NAME).getString(
					XMLResources.RESOURCE_PREFIX + ATTRIBUTE_RESOURCE_ID), attributeName != null ? attributeName : ResourceBundle.getBundle(
					XMLResources.RESOURCE_BUNDLE_BASE_NAME).getString(XMLResources.RESOURCE_PREFIX + UNKNOWN_RESOURCE_ID)); //show that we couldn't find the end of the attribute
		}
	}

	/**
	 * Parses an input stream that contains the value of an attribute. If this function reaches the end of the stream of the reader without finding an ending
	 * character for the attribute value, an exception is not thrown; rather, the return code will indicate if the end was found.
	 * @param reader The reader from which to retrieve characters.
	 * @param ownerDocument The document which will own the data.
	 * @param attribute The attribute that this value belongs to.
	 * @param attributeValueDelimiters A string of characters, any one of which can mark the end of this attribute value. This should be a single or double quote
	 *          character.
	 * @throws IOException Thrown when an i/o error occurs.
	 * @throws XMLSyntaxException Thrown when there is a syntax error in the XML file.
	 * @throws XMLWellFormednessException Thrown when there is a well-formedness error in the XML file.
	 * @throws XMLUndefinedEntityReferenceException Thrown if a named entity reference has not been defined.
	 * @throws ParseUnexpectedDataException Thrown when an unexpected character is found. //TODO del @throws ParseEOFException Thrown when the end of the input
	 *           stream is reached unexpectedly.
	 * @return <code>true</code> if the ending character for this entity was found, else <code>false</code>.
	 */
	protected boolean parseAttributeValue(final XMLReader reader, final XMLDocument ownerDocument, final XMLAttribute attribute,
			final String attributeValueDelimiters) throws IOException, XMLSyntaxException, XMLWellFormednessException, XMLUndefinedEntityReferenceException,
			ParseUnexpectedDataException {
		try {
			final StringBuffer attributeValueStringBuffer = new StringBuffer(); //create a string buffer for collecting attribute value data
			//TODO we can probably make this even more efficient by carrying this string buffer along with us when we recurse
			while(true) { //keep reading content; when we find the ending character we'll return from there
				//TODO fix for parameter entities here
				attributeValueStringBuffer.append(reader.readStringUntilCharEOF("&<" + attributeValueDelimiters)); //read character content until we find a reference, an illegal less-than character, the end of the attribute, or until we reach the end of the file TODO make these constants
				if(reader.isEnd()) { //if we run out of characters looking for the end of the entity, this may not be an error; we may have just been parsing an entity reader for another entity
					attribute.setNodeValue(attribute.getNodeValue() + attributeValueStringBuffer.toString()); //append whatever text we've collected so far to the attribute
					return false; //return, showing that we have not yet found the end of the attribute
				}
				final char c = reader.peekChar(); //see which character comes next
				if(attributeValueDelimiters.indexOf(c) != -1) { //if the next character is the end of the attribute value
					attribute.setNodeValue(attribute.getNodeValue() + attributeValueStringBuffer.toString()); //append whatever text we've collected so far to the attribute
					return true; //we're finished parsing all the attribute content we know about, so return
				}
				switch(c) { //see which character comes next
					case '&': //if this is a character or entity reference TODO use a constant here
						if(reader.peekChar() == '#') //if this is a character reference TODO make this a constant
							attributeValueStringBuffer.append(parseCharacterReference(reader)); //parse this character reference and add the character to our attribute value
						else { //if this is not a character reference, it must be an entity reference
							attribute.setNodeValue(attribute.getNodeValue() + attributeValueStringBuffer.toString()); //append whatever text we've collected so far to the attribute
							final Object entityResult = parseEntityReference(reader, ownerDocument); //parse this entity reference and see which entity it refers to TODO do we want to parse the entity and store everything as an entity reference node?
							if(entityResult instanceof XMLEntity) { //if we found a matching entity
								final XMLEntity entity = (XMLEntity)entityResult; //case the result to an entity
								if(nestedEntityReferenceMap.containsKey(entity)) //if, somewhere along our nested way, we've already processed this entity (i.e. this is a circular entity reference)
									throw new XMLWellFormednessException(XMLWellFormednessException.NO_RECURSION, new Object[] { entity.getNodeName() }, reader.getLineIndex(),
											reader.getCharIndex(), reader.getName()); //show that an entity can't reference itself, directly or indirectly TODO we need to give a better position in the error, here
								if(entity.isExternalEntity()) //if this is an external entity
									throw new XMLWellFormednessException(XMLWellFormednessException.NO_EXTERNAL_ENTITY_REFERENCES, new Object[] { attribute.getNodeName(),
											entity.getNodeName() }, reader.getLineIndex(), reader.getCharIndex(), reader.getName()); //show that this attribute contains an external entity reference
								final XMLReader entityReader = new XMLReader(entity.getText(), "General entity \"" + entity.getNodeName() + "\" from \""
										+ entity.getSourceName() + "\""); //create a string reader from the text of the entity, giving our entity name for the name of the reader TODO i18n

								entityReader.tidy = isTidy(); //TODO fix

								entityReader.setLineIndex(entity.getLineIndex()); //pretend we're reading where the entity was located in that file, so any errors will show the correct information
								entityReader.setCharIndex(entity.getCharIndex()); //pretend we're reading where the entity was located in that file, so any errors will show the correct information
								nestedEntityReferenceMap.put(entity, null); //show that we're getting ready to process this entity
								//parse the entity text just as if it were included in the file;
								//  if the ending delimiter for the attribute was found in the entity replacement text, that's an error
								if(parseAttributeValue(entityReader, ownerDocument, attribute, attributeValueDelimiters))
									return true; //show that we found the end of the element TODO throw an error here
								nestedEntityReferenceMap.remove(entity); //show that we're finished processing this entity
								attributeValueStringBuffer.setLength(0); //we'll now start collecting new attribute data
							} else { //if there was no matching entity, yet this doesn't cause a well-formedness error
								//TODO if we're validating, we'll probably want to throw a validity error, here
								attributeValueStringBuffer.append((String)entityResult); //store the entity reference name in our character data TODO maybe construct this manually; we don't want to depend on toString() behavior, and constructing this manually will probably be faster
							}
						}
						break;
					case '<': //if this is a less-than character in the attribute value TODO use a constant here
						reader.read(); //read the character to update our character positions
						throw new XMLWellFormednessException(XMLWellFormednessException.NO_LT_IN_ATTRIBUTE_VALUES, new Object[] { attribute.getNodeName() },
								reader.getLineIndex(), reader.getCharIndex(), reader.getName()); //show that this attribute contains a less-than character
					default: //TODO why do we check for end tag and also have a default?
						attributeValueStringBuffer.append(reader.readChar()); //add this character to what we've collected so far
						break;
				}
			}
		} catch(final ParseEOFException parseEOFException) { //if we run out of data
			throw new XMLWellFormednessException(parseEOFException, ResourceBundle.getBundle(XMLResources.RESOURCE_BUNDLE_BASE_NAME).getString(
					XMLResources.RESOURCE_PREFIX + ATTRIBUTE_RESOURCE_ID), attribute.getNodeName()); //show that we couldn't find the end of the attribute
		}
	}

	/**
	 * Parses a stream of characters which supposedly starts with an external ID, constructs an XMLExternalID object, and returns it.
	 * @param reader The reader from which to retrieve characters.
	 * @throws IOException Thrown when an i/o error occurs.
	 * @throws XMLWellFormednessException Thrown when there is a well-formedness error in the XML file.
	 * @throws ParseUnexpectedDataException Thrown when an unexpected character is found.
	 * @return An XMLExternalID object with the appropriate identifiers.
	 * @see XMLExternalID
	 */
	protected XMLExternalID parseExternalID(final XMLReader reader) throws IOException, XMLWellFormednessException, ParseUnexpectedDataException {
		try {
			final XMLExternalID externalID = new XMLExternalID(); //create a new external ID object to return
			//TODO maybe use readexpected... to make sure that these characters are accurate on the fly
			reader.resetPeek(); //reset peeking so that the next peek will correctly return the next character to be read
			char literalDelimiter; //we'll find out which character should be used for a delimiter in each case
			switch(reader.peekExpectedChar("SPsp")) { //see which character is next, and make sure it's one of the two we recognize; we don't care about case here, becuase if we're *not* tidying this will be checked later TODO use a constant here
				case 'S': //if this is the start of "SYSTEM" TODO use a constant here
				case 's': //if this is the start of "SYSTEM" (if we're not tidying, the case will be caught below) TODO use a constant here
					if(isTidy()) //if we're tidying
						reader.readExpectedStringIgnoreCase("SYSTEM"); //read the label for the system ID, ignoring case TODO use a constant here
					else
						//if we're not tidying
						reader.readExpectedString("SYSTEM"); //read the label for the system ID TODO use a constant here
					reader.readExpectedChar(WHITESPACE_CHARS); //we expect at least one whitespace character
					skipWhitespaceCharacters(reader); //skip any other whitespace characters
					literalDelimiter = reader.readExpectedChar("\"'"); //read the delimiter of the system literal TODO make this a constant
					externalID.setSystemID(reader.readStringUntilChar(literalDelimiter)); //read the system literal, which should end with the correct delimiter (a single or double quote, matching the first one)
					reader.readExpectedChar(literalDelimiter); //read the ending delimiter (we already know what it will be)
					break;
				case 'P': //if this is the start of "PUBLIC" TODO use a constant here
				case 'p': //if this is the start of "PUBLIC" (if we're not tidying, the case will be caught below) TODO use a constant here
					if(isTidy()) //if we're tidying
						reader.readExpectedStringIgnoreCase("PUBLIC"); //read the label for the public ID, ignoring case TODO use a constant here
					else
						//if we're not tidying
						reader.readExpectedString("PUBLIC"); //read the label for the public ID TODO use a constant here
					reader.readExpectedChar(WHITESPACE_CHARS); //we expect at least one whitespace character
					skipWhitespaceCharacters(reader); //skip any other whitespace characters
					literalDelimiter = reader.readExpectedChar("\"'"); //read the delimiter of the public ID literal TODO make this a constant
					externalID.setPublicID(reader.readStringUntilChar(literalDelimiter)); //read the public ID literal literal, which should end with the correct delimiter (a single or double quote, matching the first one)
					reader.readExpectedChar(literalDelimiter); //read the ending delimiter (we already know what it will be)
					if(isTidy()) { //if we're tidying, we can accept documents that do not have public system IDs
						if(!isWhitespace(reader.peekChar())) { //if the next character is not a whitespace character
							externalID.setSystemID(""); //set the system ID to the empty string, indicating no system ID was present
							break; //stop parsing the external ID, leaving the empty system ID
						}
					}
					reader.readExpectedChar(WHITESPACE_CHARS); //we expect at least one whitespace character
					skipWhitespaceCharacters(reader); //skip any other whitespace characters
					literalDelimiter = reader.readExpectedChar("\"'"); //read the delimiter of the system literal TODO make this a constant
					externalID.setSystemID(reader.readStringUntilChar(literalDelimiter)); //read the system literal, which should end with the correct delimiter (a single or double quote, matching the first one)
					reader.readExpectedChar(literalDelimiter); //read the ending delimiter (we already know what it will be)
					break;
			}
			return externalID; //return the external ID we constructed
		} catch(final ParseEOFException parseEOFException) { //if we run out of data
			throw new XMLWellFormednessException(parseEOFException, ResourceBundle.getBundle(XMLResources.RESOURCE_BUNDLE_BASE_NAME).getString(
					XMLResources.RESOURCE_PREFIX + EXTERNAL_ID_RESOURCE_ID), ResourceBundle.getBundle(XMLResources.RESOURCE_BUNDLE_BASE_NAME).getString(
					XMLResources.RESOURCE_PREFIX + UNKNOWN_RESOURCE_ID)); //show that we couldn't find the end of the external ID
		}
	}

	/**
	 * Parses an input stream that supposedly begins with a document type declaration ("<!DOCTYPE").
	 * @param reader The reader from which to retrieve characters.
	 * @param ownerDocument The document which will own the document type.
	 * @throws IOException Thrown when an i/o error occurs.
	 * @throws XMLSyntaxException Thrown when there is a syntax error in the XML file.
	 * @throws XMLWellFormednessException Thrown when there is a well-formedness error in the XML file.
	 * @throws XMLValidityException Thrown when there is a validity error in the XML file.
	 * @throws XMLUndefinedEntityReferenceException Thrown if a named entity reference has not been defined.
	 * @throws ParseUnexpectedDataException Thrown when an unexpected character is found.
	 * @return A new XML declaration constructed from the reader.
	 */
	protected XMLDocumentType parseDocumentType(final XMLReader reader, final XMLDocument ownerDocument) throws IOException, XMLSyntaxException,
			XMLWellFormednessException, XMLValidityException, XMLUndefinedEntityReferenceException, ParseUnexpectedDataException {
		final XMLDocumentType documentType = (XMLDocumentType)getXMLDocumentType().cloneXMLNode(true); //we'll use the default XML document type we've already built TODO do we want to do it this way?
		try {
			ownerDocument.setXMLDocumentType(documentType); //tell the owner document which document type it has TODO should this really go here? no, probably not; the receiving method should do this
			final XMLNodeList elementDeclarationList = new XMLNodeList(); //this list will hold references to the elements we create that represent element declarations
			final XMLNodeList attributeListDeclarationList = new XMLNodeList(); //this list will hold references to the elements we create that represent attribute list declarations
			if(isTidy()) //if we're tidying the document
				reader.readExpectedStringIgnoreCase(DOCTYPE_DECL_START); //ignore the case of the document type declaration
			else
				//if we're reading the document normally
				reader.readExpectedString(DOCTYPE_DECL_START); //we expect to read the start of an XML declaration
			reader.readExpectedChar(WHITESPACE_CHARS); //we expect at least one whitespace character
			skipWhitespaceCharacters(reader); //skip any whitespace characters
			final String documentTypeName = reader.readStringUntilChar(WHITESPACE_CHARS + "[>"); //read the name of the document type, which will end with whitespace or the beginning-of-internal-subset marker, or maybe even the end of the declaration TODO use constants here
			if(!isName(documentTypeName)) //if this isn't a valid name
				throw new XMLWellFormednessException(XMLWellFormednessException.INVALID_NAME, new Object[] { documentTypeName }, reader.getLineIndex(),
						reader.getCharIndex(), reader.getName()); //show that this isn't a valid XML name
			documentType.setNodeName(documentTypeName); //set the name of the document type
			skipWhitespaceCharacters(reader); //skip any whitespace characters
			final char c = reader.peekChar(); //see what the next character is
			if(c != '[' && c != '>') { //if we're not to the internal document type declaration subset, yet, and we're not to the end of the declaration, this must be an external ID TODO use constants here
				documentType.setExternalID(parseExternalID(reader)); //parse the external ID and use it to set the document type's external ID
				skipWhitespaceCharacters(reader); //skip any whitespace characters
			}
			reader.resetPeek(); //reset peeking so that we'll start peeking at the next character to be read
			if(reader.peekChar() == '[') { //if we're ready to read the internal document type declaration subset
				reader.readExpectedChar('['); //TODO fix, make constant
				//Parse the internal document type content; if we couldn't find the end
				//  of the element document type declaration, return an error. We're
				//  passing the entity and parameter declarations directly, so they will
				//  be updated immediately
				if(!parseDocumentTypeContent(reader, ownerDocument, documentType.getEntityXMLNamedNodeMap(), documentType.getParameterEntityXMLNamedNodeMap(),
						elementDeclarationList, attributeListDeclarationList)) {
					throw new XMLWellFormednessException(XMLWellFormednessException.EOF, new Object[] {
							ResourceBundle.getBundle(XMLResources.RESOURCE_BUNDLE_BASE_NAME).getString(XMLResources.RESOURCE_PREFIX + DOCUMENT_TYPE_RESOURCE_ID),
							documentType.getName() }, reader.getLineIndex(), reader.getCharIndex(), reader.getName()); //show that we couldn't find the end of the document type
				}
				reader.readExpectedChar(']'); //read the end of the internal DTD TODO fix, make constant
				skipWhitespaceCharacters(reader); //skip any whitespace characters
			}
			reader.readExpectedChar(DOCTYPE_DECL_END); //the next character should be the ending delimiter of the declaration

			if(isTidy()) { //if we're tidying the document, see if we have an overriding external ID
				final XMLExternalID tidyDocumentTypeExternalID = getTidyDocumentTypeExternalID(); //see if there is an overriding external ID to use
				if(tidyDocumentTypeExternalID != null) { //if there is an external ID we should use in place of the given one
					documentType.setExternalID(tidyDocumentTypeExternalID); //update the document's external ID to match the one required by tidying
				}
			}

			//if this document isn't standalone and has specified an external DTD
			if(!ownerDocument.getXMLDeclaration().isStandalone() && documentType.getSystemID() != null && documentType.getSystemID().length() > 0) { //TODO these last checks are for tidy; what do we really want to do if there is no system ID?
				//parse the external DTD subset
				parseExternalDocumentType(reader, ownerDocument, documentType, elementDeclarationList, attributeListDeclarationList);
			}
			//TODO we might want to have a validate flag somewhere, in case they don't want to validate the document
			/*TODO fix all this when we actually do the content model right
						//associate all the attribute lists with the elements of the same name
						final XMLNamedNodeMap elementDeclarationMap=new XMLNamedNodeMap();	//create a new map to hold our element declarations
							//add all the elements type declarations in the list to our map, checking for duplicates
						for(int i=0; i<elementDeclarationList.size(); ++i) {	//look at each of the element type declarations
							final XMLElement elementDeclaration=(XMLElement)elementDeclarationList.item(i);	//get a reference to this element
							if(!elementDeclarationMap.containsKey(elementDeclaration.getAttribute("name")))	//if the map doesn't already contain a declaration for this element type TODO use a constant here
								elementDeclarationMap.put(elementDeclaration.getAttribute("name"), elementDeclaration);	//add this element type declaration to our map TODO use a constant here, maybe put these in a common variable
							else	//if this element type has already been declared
								throw new XMLValidityException(XMLValidityException.UNIQUE_ELEMENT_TYPE_DECLARATION, new Object[]{elementDeclaration.getAttribute("name")}, reader.getLineIndex(), reader.getCharIndex(), reader.getName());	//show that this element type has already been declared TODO give a better indication of the element type declaration's location
						}
							//add all the attributes list declarations in the list to our map, ignoring duplicates TODO should we ignore or combine duplicates?
						for(int elementIndex=0; elementIndex<attributeListDeclarationList.size(); ++elementIndex) {	//look at each of the elements, each of which contain a list of attribute declarations
							final XMLElement attributeListElement=(XMLElement)attributeListDeclarationList.item(elementIndex);	//get a reference to this element, which contains a list of attribute declarations
							if(elementDeclarationMap.containsKey(attributeListElement.getAttribute("name"))) {	//if our map contains an element declaration for list of attribute declarations (if it doesn't this apparently is *not* an XML validity error, so we'll just ignore that attribute list declaration) TODO use a constant here
								final XMLElement elementDeclaration=(XMLElement)elementDeclarationMap.getNamedItem(attributeListElement.getAttribute("name"));	//find out which element declaration these attributes should belong to TODO use a constant
								for(int attributeIndex=0; attributeIndex<attributeListElement.getChildXMLNodeList().size(); ++attributeIndex) {	//look at each of the attribute declarations in the list
									final XMLElement attributeDeclaration=(XMLElement)attributeListElement.getChildXMLNodeList().item(attributeIndex);	//get a reference to this attribute declaration
									XMLElement existingAttributeDeclarationElement=null;	//we're going to determine if this element already has this attribute declared
									for(int i=0; i<elementDeclaration.getChildXMLNodeList().size(); ++i) {	//look at each of the child nodes for this element, each of which will be an attribute declaration
										final XMLElement thisAttributeDeclarationElement=(XMLElement)elementDeclaration.getChildXMLNodeList().get(i);	//get a reference to this node, which is an attribute declaration that the element already has
										if(thisAttributeDeclarationElement.getAttribute("name").equals(attributeDeclaration.getAttribute("name"))) {	//if this attribute declaration element has the same name as the one we're trying to add TODO use constants here
											existingAttributeDeclarationElement=thisAttributeDeclarationElement;	//show that we have an attribute declaration already
											break;	//stop looking for existing attribute declarations with this name
										}
									}
									if(existingAttributeDeclarationElement==null) {	//if an attribute with this name has not been declared for this element TODO if it does exist, is there some sort of XML combination rules?
										elementDeclaration.appendChild(attributeDeclaration);	//add this attribute declaration element as a child node to our element declaration element
									}
								}
							}
						}
			*/
			return documentType; //return the document type we constructed
		} catch(final ParseEOFException parseEOFException) { //if we run out of data
			throw new XMLWellFormednessException(parseEOFException, ResourceBundle.getBundle(XMLResources.RESOURCE_BUNDLE_BASE_NAME).getString(
					XMLResources.RESOURCE_PREFIX + DOCUMENT_TYPE_RESOURCE_ID), documentType.getName() != null ? documentType.getName() : ResourceBundle.getBundle(
					XMLResources.RESOURCE_BUNDLE_BASE_NAME).getString(XMLResources.RESOURCE_PREFIX + UNKNOWN_RESOURCE_ID)); //show that we couldn't find the end of the document type
		}
	}

	/**
	 * A map of DTD subsets being cached, keyed by public ID and/or private ID. Currently the associated object stored is an <code>XMLNamedNodeMap</code>
	 * representing the general entities in a DTD subset, with the parent of each set to <code>null</code>. TODO eventually allow a special DTD subset object to
	 * be stored
	 */
	private final Map documentTypeSubsetCacheMap = new HashMap();

	/**
	 * Places a document type subset in the cache for easy retrieval. The object will be deep-cloned, and the parent of the cloned object and all child objects
	 * will be recursively set to null. The cloned object will be stored keyed to both the public ID and private ID, providing they are not null.
	 * @param publicID The public ID of the docuement type subset, or <code>null</code> if there is no public ID.
	 * @param systemID The system ID of the docuement type subset.
	 */
	protected void putCachedDocumentTypeSubset(final String publicID, final String systemID, final XMLNamedNodeMap generalEntityMap) {
		final XMLNamedNodeMap clonedEntityMap = generalEntityMap.cloneDeep(); //make a deep clone of the map
		clonedEntityMap.setOwnerXMLDocument(null); //show that the clone has no owner document
		if(publicID != null) //if there is a public ID
			documentTypeSubsetCacheMap.put(publicID, clonedEntityMap); //put the DTD subset in the map keyed by its public ID
		if(systemID != null) //if there is a system ID
			documentTypeSubsetCacheMap.put(systemID, clonedEntityMap); //put the DTD subset in the map keyed by its system ID
	}

	/**
	 * Attempts to retrieve a cached document type subset, first by a public ID and then by a private ID. The retrieve object, if found, is first cloned before
	 * being returned.
	 * @param publicID The public ID of the document type which should be retrieved.
	 * @param systemID The system ID of the document type which should be retrieved.
	 * @return A clone of a cached document type subset, if one could be found.
	 */
	protected XMLNamedNodeMap getCachedDocumentTypeSubset(final String publicID, final String systemID) {
		XMLNamedNodeMap generalEntityMap = null; //we'll try to get this object from the map
		if(publicID != null) //if they passed a valid public ID
			generalEntityMap = (XMLNamedNodeMap)documentTypeSubsetCacheMap.get(publicID); //attempt to get the document type subset from the map using the public ID as a key
		if(generalEntityMap == null && systemID != null) //if we didn't find the document type subset, but they passed a system ID
			generalEntityMap = (XMLNamedNodeMap)documentTypeSubsetCacheMap.get(systemID); //attempt to get the document type subset from the map using the system ID as a key
		return generalEntityMap != null ? generalEntityMap.cloneDeep() : null; //return a clone of the document type subset we found, if we found one
	}

	/**
	 * Parses the contents of an external document type declaration.
	 * @param reader The reader from which to retrieve characters.
	 * @param ownerDocument The document which will own the document type.
	 * @param documentType The document type being processed.
	 * @param elementDeclarationList The list of elements that will be created to represent element declarations.
	 * @param attributeListDeclarationList The list of elements that will be created to represent attribute list declarations.
	 * @throws IOException Thrown when an i/o error occurs.
	 * @throws XMLSyntaxException Thrown when there is a syntax error in the XML file.
	 * @throws XMLWellFormednessException Thrown when there is a well-formedness error in the XML file.
	 * @throws XMLUndefinedEntityReferenceException Thrown if a named entity reference has not been defined.
	 * @throws ParseUnexpectedDataException Thrown when an unexpected character is found.
	 * @throws ParseEOFException Thrown when the end of the input stream is reached unexpectedly.
	 */
	protected void parseExternalDocumentType(final XMLReader reader, final XMLDocument ownerDocument, final XMLDocumentType documentType,
			final XMLNodeList elementDeclarationList, final XMLNodeList attributeListDeclarationList) throws IOException, XMLSyntaxException,
			XMLWellFormednessException, XMLUndefinedEntityReferenceException, ParseUnexpectedDataException, ParseEOFException {
		/*TODO fix
		final XMLDocument ownerDocument, final XMLDocumentType documentType

		*/
		final String publicID = documentType.getPublicID(); //get a reference to the document type's public ID, if there is one
		final String systemID = documentType.getSystemID(); //get a reference to the document type's system ID
		//we'll first try to get the DTD subset information from the cache
		XMLNamedNodeMap parameterEntityMap; //TODO eventually replace these with an object specifically for DTD subsets
		XMLNamedNodeMap generalEntityMap = getCachedDocumentTypeSubset(documentType.getPublicID(), documentType.getSystemID()); //try to get the document type subset from the cache
		if(generalEntityMap != null) { //if we found a document type subset with either the same public ID or system ID that we have already loaded
			generalEntityMap.setOwnerXMLDocument(ownerDocument); //set the owner document of the document type subset
			parameterEntityMap = null; //TODO fix by getting from the cache
		} else { //if we weren't able to find a cached document type subset
			generalEntityMap = new XMLNamedNodeMap(); //eventually replace these with an object specifically for DTD subsets
			parameterEntityMap = new XMLNamedNodeMap();
			final XMLReader externalDTDReader = createReader(reader.getSourceObject(), documentType.getPublicID(), documentType.getSystemID()); //TODO testing; comment
			try {
				//TODO change the code to know whether it should look for an ending ']'
				parseDocumentTypeContent(externalDTDReader, ownerDocument, generalEntityMap, parameterEntityMap, elementDeclarationList, attributeListDeclarationList); //parse the external document type content
				//cache the document type subset we just loaded
				putCachedDocumentTypeSubset(documentType.getPublicID(), documentType.getSystemID(), generalEntityMap);
			} finally {
				externalDTDReader.close(); //always close the reader we created
			}
		}
		//store the general entities in the document
		final Iterator entityIterator = generalEntityMap.values().iterator(); //get an iterator of all the enties we just found
		while(entityIterator.hasNext()) { //while there are entities
			final XMLEntity xmlEntity = (XMLEntity)entityIterator.next(); //get the next entity
			if(!documentType.getEntityXMLNamedNodeMap().containsNamedItem(xmlEntity)) //if we haven't already defined this general entity
				documentType.getEntityXMLNamedNodeMap().setNamedItem(xmlEntity); //store the entity in our named node map TODO this will eventually allow a DOMException to be thrown; reflect this
		}
		//TODO fix; right now, the parameter entities never get placed in the document type object
	}

	/**
	 * Parses an input stream that contains content of a document type. If this function reaches the end of the stream of the reader without finding an ending tag
	 * for the element, an exception is not thrown; rather, the return code will indicate if the end tag was found.
	 * @param reader The reader from which to retrieve characters.
	 * @param ownerDocument The document which will own this data. //TODO fix @param documentType The document type being processed.
	 * @param generalEntityMap The map of general entities which will be created and returned. TODO eventually pass all these in one encapsulating object
	 * @param parameterEntityMap The map of general entities which will be created and returned. TODO eventually pass all these in one encapsulating object
	 * @param elementDeclarationList The list of elements that will be created to represent element declarations, or <code>null</code> if these elements need not
	 *          be returned. TODO fix with new DOM storage of these elements
	 * @param attributeListDeclarationList The list of elements that will be created to represent attribute list declarations, or <code>null</code> if these
	 *          attributes need not be returned. TODO fix with new DOM storage of these attributes
	 * @throws IOException Thrown when an i/o error occurs.
	 * @throws XMLSyntaxException Thrown when there is a syntax error in the XML file.
	 * @throws XMLWellFormednessException Thrown when there is a well-formedness error in the XML file.
	 * @throws XMLUndefinedEntityReferenceException Thrown if a named entity reference has not been defined.
	 * @throws ParseUnexpectedDataException Thrown when an unexpected character is found.
	 * @throws ParseEOFException Thrown when the end of the input stream is reached unexpectedly. //TODO fix @return <code>true</code> if the ending tag for this
	 *           element was found, else <code>false</code>.
	 */
	//TODO have a variable here that specifies whether a conditional parameter entity existed; if so, we won't be able to cache the result
	protected boolean parseDocumentTypeContent(final XMLReader reader, final XMLDocument ownerDocument, final XMLNamedNodeMap generalEntityMap,
			final XMLNamedNodeMap parameterEntityMap, final XMLNodeList elementDeclarationList, final XMLNodeList attributeListDeclarationList) throws IOException,
			XMLSyntaxException, XMLWellFormednessException, XMLUndefinedEntityReferenceException, ParseUnexpectedDataException, ParseEOFException {
		//the markup strings we expect in this document type declaration
		final String[] EXPECTED_DTD_MARKUP_STRINGS = { INTERNAL_DTD_SUBSET_END, PARAMETER_ENTITY_REF_START, ENTITY_DECL_START, ELEMENT_TYPE_DECL_START,
				ATTLIST_DECL_START, COMMENT_START };
		//the indexes of the DTD markup strings in our array
		final int END_OF_INTERNAL_DTD_SUBSET = 0, PARAMETER_ENTITY_REF = 1, ENTITY_DECL = 2, ELEMENT_TYPE_DECL = 3, ATTLIST_DECL = 4, COMMENT = 5;
		while(true) { //keep reading content; when we find the end of the data we'll return from there
			skipWhitespaceCharactersEOF(reader); //skip any whitespace characters, but don't throw an exception if we reach the end of the stream
			if(reader.isEnd()) //if we run out of characters looking for the start of the next document type markup, this may not be an error; we may be processing an external DTD
				return false; //return, showing that we have not yet found the ending character marker of an internal DTD
			switch(reader.peekExpectedStrings(EXPECTED_DTD_MARKUP_STRINGS)) { //see what sort of DTD declaration markup this is TODO add other types of declarations
				case END_OF_INTERNAL_DTD_SUBSET: //if this is the end of the document type content
					return true; //TODO fix
				case PARAMETER_ENTITY_REF: //if this is a parameter entity reference
				{
					final XMLEntity entity = parseParameterEntityReference(reader, ownerDocument, parameterEntityMap); //parse this entity reference and see which entity it refers to
					final XMLReader entityReader = createEntityReader(reader.getSourceObject(), entity); //we'll use this to read either from the internal value or an external file
					try {
						if(parseDocumentTypeContent(entityReader, ownerDocument, /*TODO fix documentType, */generalEntityMap, parameterEntityMap, elementDeclarationList,
								attributeListDeclarationList)) //parse the entity text just as if it were included in the file (except for markup across entity boundaries); if the ending markup for the DTD for the element was found TODO fix; how can they find the ']' in a parameter entity?
							return true; //show that we found the end of the element
					} finally {
						entityReader.close(); //always close the entity reader
					}
				}
					break; //TODO perhaps have a more instructive error here if they try to split up general entities across entities
				//TODO del or fix					reader.readExpectedChar(']');	//read the end of the internal document type content TODO use constant
				case ENTITY_DECL: //if this is an entity declaration
				{
					final XMLEntity entity = parseEntityDeclaration(reader, ownerDocument); //parse the entity declaration
					if(entity.isParameterEntity()) { //if the entity is a parameter entity		//TODO do parameter entities have the same action of ignoring multiple definitions?
						if(!parameterEntityMap.containsNamedItem(entity)) //if we haven't already defined this parameter entity
							parameterEntityMap.setNamedItem(entity); //store the entity in our named node map TODO this will eventually allow a DOMException to be thrown; reflect this
						/*TODO del when caching works
														if(!documentType.getParameterEntityXMLNamedNodeMap().containsNamedItem(entity))	//if we haven't already defined this parameter entity
															documentType.getParameterEntityXMLNamedNodeMap().setNamedItem(entity);	//store the entity in our named node map TODO this will eventually allow a DOMException to be thrown; reflect this
						*/
					} else { //if this is a general entity
						if(!generalEntityMap.containsNamedItem(entity)) //if we haven't already defined this general entity
							generalEntityMap.setNamedItem(entity); //store the entity in our named node map TODO this will eventually allow a DOMException to be thrown; reflect this
						/*TODO del when caching works
														if(!documentType.getEntityXMLNamedNodeMap().containsNamedItem(entity))	//if we haven't already defined this general entity
															documentType.getEntityXMLNamedNodeMap().setNamedItem(entity);	//store the entity in our named node map TODO this will eventually allow a DOMException to be thrown; reflect this
						*/
						//TODO use an else here to display a warning if the node existed already
					}
				}
					//TODO do we want a default case here?
					break;
				case ELEMENT_TYPE_DECL: //if this is an element type declaration
				{
					final XMLElement elementTypeDeclaration = parseElementTypeDeclaration(reader, ownerDocument, parameterEntityMap); //parse this element type declaration TODO fix
					if(elementDeclarationList != null) //TODO this check will go away when we store these inside the document type itself
						elementDeclarationList.add(elementTypeDeclaration); //add the resulting element to our declaration list TODO fix
				}
					break;
				case ATTLIST_DECL: //if this is an attribute declaration
				{
					final XMLElement attributeListDeclaration = parseAttributeListDeclaration(reader, ownerDocument, parameterEntityMap); //parse this attribute list declaration TODO fix
					if(attributeListDeclarationList != null) //TODO this check will go away when we store these inside the document type itself
						attributeListDeclarationList.add(attributeListDeclaration); //add the resulting element to our declaration list TODO fix
				}
					break;
				case COMMENT: //if this is a comment
					parseComment(reader, ownerDocument); //TODO fix, comment
					break;
			}
		}
	}

	/**
	 * Parses an input stream that supposedly begins with an entity declaration.
	 * @param reader The reader from which to retrieve characters.
	 * @param ownerDocument The document which will own this entity.
	 * @throws IOException Thrown when an i/o error occurs.
	 * @throws XMLSyntaxException Thrown when there is a syntax error in the XML file.
	 * @throws XMLWellFormednessException Thrown when there is a well-formedness error in the XML file.
	 * @throws XMLUndefinedEntityReferenceException Thrown if a named entity reference has not been defined.
	 * @throws ParseUnexpectedDataException Thrown when an unexpected character is found.
	 * @return A new XML entity constructed from the reader.
	 */
	protected XMLEntity parseEntityDeclaration(final XMLReader reader, final XMLDocument ownerDocument) throws IOException, XMLSyntaxException,
			XMLWellFormednessException, XMLUndefinedEntityReferenceException, ParseUnexpectedDataException {
		final XMLEntity entity = new XMLEntity(ownerDocument); //create a new XML entity
		try {
			reader.readExpectedString(ENTITY_DECL_START); //we expect to read the start of an entity declaration
			reader.readExpectedChar(WHITESPACE_CHARS); //we expect at least one whitespace character
			skipWhitespaceCharacters(reader); //skip any other whitespace characters
			if(reader.peekChar() == '%') { //if this is a parameter entity reference TODO use a constant here
				entity.setParameterEntity(true); //we now know that this is a parameter entity
				reader.readExpectedChar('%'); //read the parameter entity reference identifier
				reader.readExpectedChar(WHITESPACE_CHARS); //we expect at least one whitespace character
				skipWhitespaceCharacters(reader); //skip any other whitespace characters
			}
			final String entityName = reader.readStringUntilChar(WHITESPACE_CHARS); //read the name of the entity, which will end with whitespace
			if(!isName(entityName)) //if this isn't a valid name
				throw new XMLWellFormednessException(XMLWellFormednessException.INVALID_NAME, new Object[] { entityName }, reader.getLineIndex(),
						reader.getCharIndex(), reader.getName()); //show that this isn't a valid XML name
			entity.setNodeName(entityName); //set the name of the entity
			reader.readExpectedChar(WHITESPACE_CHARS); //we expect at least one whitespace character
			skipWhitespaceCharacters(reader); //skip any other whitespace characters
			final char c = reader.peekChar(); //see what the next character will be
			if(c == '"' || c == '\'') { //if the next character is a single or double quote, this is an entity value
				final char entityValueDelimiter = reader.readExpectedChar("\"'"); //read the delimiter of the entity value TODO make this a constant
				//TODO should this be parsed right away?
				//TODO should we pass the text node instead?
				//now that we've found where this entity starts, show it's location in the XML document
				entity.setSourceName(reader.getName()); //store where we found the entity
				entity.setLineIndex(reader.getLineIndex()); //store which line the entity starts on
				entity.setCharIndex(reader.getCharIndex()); //store which character the entity starts on
				if(!parseEntityContent(reader, ownerDocument, entity, entityValueDelimiter)) { //parse the entity content; if we couldn't find the end of the entity
					throw new XMLWellFormednessException(XMLWellFormednessException.EOF, new Object[] {
							ResourceBundle.getBundle(XMLResources.RESOURCE_BUNDLE_BASE_NAME).getString(XMLResources.RESOURCE_PREFIX + ENTITY_DECLARATION_RESOURCE_ID),
							entity.getNodeName() }, reader.getLineIndex(), reader.getCharIndex(), reader.getName()); //show that we couldn't find the end of the entity declaration
				}
				reader.readExpectedChar(entityValueDelimiter); //read the ending delimiter (we already know what it will be)
				//TODO do something with the entity text, here
			} else { //if, instead of an entity value, there is an external ID
				entity.setExternalID(parseExternalID(reader)); //parse the external ID and use it to set the entity's external ID
				reader.peekExpectedChar(WHITESPACE_CHARS + '>'); //make sure the next character is one we expect (there has to be whitespace unless it's the end of the entity declaration) TODO make this a constant
				skipWhitespaceCharacters(reader); //skip any whitespace characters
				if(reader.peekChar() == 'N') { //if this is the start of an NDATA declaration
					//TODO fix NDATA parsing
				}

				/*TODO fix or del
									//TODO soon, change this to lazy evaluation so that we don't immediately load the content from the other file
							InputStream inputStream;	//we don't know where our input stream is coming from, yet
							try
							{
								inputStream=new URL(entity.getSystemID()).openConnection().getInputStream();	//assume the location is an Internet URL and try to open a connection to it
							}
							catch(MalformedURLException e) {	//if the location isn't a valid URL
								inputStream=new FileInputStream(entity.getSystemID());	//assume it's a file and try to open it
							}
							final ParseReader externalEntityReader=new ParseReader(inputStream, entity.getSystemID());	//create an XML reader that will read from the input stream

							entity.setSourceName(externalEntityReader.getName());	//store where we found the entity
							entity.setLineIndex(externalEntityReader.getLineIndex());	//store which line the entity starts on
							entity.setCharIndex(externalEntityReader.getCharIndex());	//store which character the entity starts on
							parseEntityContent(externalEntityReader, ownerDocument, entity, '~');	//parse the entity content TODO fix so that parseEntityContent will ignore the delimiter character
				*/

			}
			skipWhitespaceCharacters(reader); //skip any whitespace characters
			reader.readExpectedChar(ENTITY_DECL_END); //the next character should be the ending delimiter of the declaration
			return entity; //return the entity we constructed
		} catch(final ParseEOFException parseEOFException) { //if we run out of data
			throw new XMLWellFormednessException(parseEOFException, ResourceBundle.getBundle(XMLResources.RESOURCE_BUNDLE_BASE_NAME).getString(
					XMLResources.RESOURCE_PREFIX + ENTITY_DECLARATION_RESOURCE_ID), entity.getNodeName() != null ? entity.getNodeName() : ResourceBundle.getBundle(
					XMLResources.RESOURCE_BUNDLE_BASE_NAME).getString(XMLResources.RESOURCE_PREFIX + UNKNOWN_RESOURCE_ID)); //show that we couldn't find the end of the entity declaration
		}
	}

	/**
	 * Parses an input stream that contains content of an entity. If this function reaches the end of the stream of the reader without finding an ending character
	 * for the entity, an exception is not thrown; rather, the return code will indicate if the end was found.
	 * @param reader The reader from which to retrieve characters.
	 * @param ownerDocument The document which will own the data.
	 * @param entity The entity that this content belongs to.
	 * @param entityValueDelimiter The character which marks the end of this entity value. This should be a single or double quote character.
	 * @throws IOException Thrown when an i/o error occurs.
	 * @throws XMLSyntaxException Thrown when there is a syntax error in the XML file.
	 * @throws XMLWellFormednessException Thrown when there is a well-formedness error in the XML file.
	 * @throws XMLUndefinedEntityReferenceException Thrown if a named entity reference has not been defined.
	 * @throws ParseUnexpectedDataException Thrown when an unexpected character is found.
	 * @throws ParseEOFException Thrown when the end of the input stream is reached unexpectedly.
	 * @return <code>true</code> if the ending character for this entity was found, else <code>false</code>.
	 */
	protected boolean parseEntityContent(final XMLReader reader, final XMLDocument ownerDocument, final XMLEntity entity, final char entityValueDelimiter)
			throws IOException, XMLSyntaxException, XMLWellFormednessException, XMLUndefinedEntityReferenceException, ParseUnexpectedDataException, ParseEOFException {
		//TODO store the start position of the element so we can report the error location
		final StringBuffer entityValueStringBuffer = new StringBuffer(); //we haven't parsed any content, yet
		while(true) { //keep reading content; when we find the ending character we'll return from there
			//TODO fix for parameter entities here
			entityValueStringBuffer.append(reader.readStringUntilCharEOF("&" + entityValueDelimiter)); //read character content until we find a reference, the end of the entity declaration, or until we reach the end of the file TODO make these constants
			if(reader.isEnd()) { //if we run out of characters looking for the end of the entity, this may not be an error; we may have just been parsing an entity reader for another entity
				entity.appendText(entityValueStringBuffer.toString()); //append whatever text we've collected so far to the entity
				return false; //return, showing that we have not yet found the end of the entity
			}
			final char c = reader.peekChar(); //see which character comes next
			if(c == entityValueDelimiter) { //if the next character is the end of the entity declaration
				entity.appendText(entityValueStringBuffer.toString()); //append whatever text we've collected so far to the entity
				return true; //we're finished parsing all the entity content we know about, so return
			}
			//TODO somehow, we need to know if we should accept parameter entity references here
			switch(c) { //see which character comes next
				case '&': //if this is a character or entity reference
					if(reader.peekChar() == '#') //if this is a character reference TODO make this a constant
						entityValueStringBuffer.append(parseCharacterReference(reader)); //parse this character reference and add the character to our entity value
					else
						//if this is not a character reference, it must be an entity reference
						entityValueStringBuffer.append(reader.readChar()); //we'll not parse general entities inside entities, so read the beginning of the entity so that the rest of it won't be parsed as an entity
					//TODO fix for entity references; we should ignore them and keep the actual characters
					break;
				default: //TODO why do we check for end tag and also have a default?
					entity.appendText(entityValueStringBuffer.toString()); //append whatever text we've collected so far to the entity
					return true; //we're finished parsing all the entity content we know about, so return
			}
		}
	}

	/**
	 * Parses an input stream that supposedly begins with an element type declaration.
	 * @param reader The reader from which to retrieve characters.
	 * @param ownerDocument The document which will own this element type declaration.
	 * @param parameterEntityMap The parameter entities we've collected so far.
	 * @throws IOException Thrown when an i/o error occurs.
	 * @throws XMLSyntaxException Thrown when there is a syntax error in the XML file.
	 * @throws XMLWellFormednessException Thrown when there is a well-formedness error in the XML file.
	 * @throws XMLUndefinedEntityReferenceException Thrown if a named entity reference has not been defined.
	 * @throws ParseUnexpectedDataException Thrown when an unexpected character is found.
	 * @throws ParseEOFException Thrown when the end of the input stream is reached unexpectedly. TODO fix
	 * @return A new XML element constructed from the reader containing the declaration information for this element.
	 */
	protected XMLElement parseElementTypeDeclaration(final XMLReader reader, final XMLDocument ownerDocument, final XMLNamedNodeMap parameterEntityMap)
			throws IOException, XMLSyntaxException, XMLWellFormednessException, XMLUndefinedEntityReferenceException, ParseUnexpectedDataException, ParseEOFException {
		final XMLElement element = new XMLElement(ownerDocument, "element"); //create a new XML element which will hold the rules for this element declaration TODO use constant TODO use the document createElement() function
		reader.readExpectedString(ELEMENT_TYPE_DECL_START); //we expect to read the start of an element type declaration
		reader.readExpectedChar(WHITESPACE_CHARS); //we expect at least one whitespace character
		skipWhitespaceCharacters(reader); //skip any other whitespace characters
		element.setAttribute("name", reader.readStringUntilChar(WHITESPACE_CHARS)); //set the name attribute, specifying the name of the element, which will end with whitespace TODO use a constant here
		reader.readExpectedChar(WHITESPACE_CHARS); //we expect at least one whitespace character
		skipWhitespaceCharacters(reader); //skip any other whitespace characters
		//the content specification strings we expect
		final String[] CONTENT_SPEC_STRINGS = { ELEMENT_CONTENT_ANY, ELEMENT_CONTENT_EMPTY, "" };
		//the indexes of the content specification strings in our array
		final int ANY = 0, EMPTY = 1;
		reader.resetPeek(); //reset peeking so that the next character peeked will reflect the next character to be read
		switch(reader.peekExpectedStrings(CONTENT_SPEC_STRINGS)) { //see what kind of markup this is
			case ANY: //if this specifies any content
				reader.readExpectedString(ELEMENT_CONTENT_ANY); //read the "any" content spec
				element.setAttribute("content", "any"); //add an attribute specifying any content TODO use constants
				break;
			case EMPTY: //if this specifies no content
				reader.readExpectedString(ELEMENT_CONTENT_EMPTY); //read the "empty" content spec
				element.setAttribute("content", "empty"); //add an attribute specifying no content TODO use constants
				break;
			default: //if neither "any" nor "empty" was found
				final String contentSpec = reader.readStringUntilChar('>'); //read the content spec string
				element.setAttribute("content", contentSpec); //add an attribute specifying the content specification TODO use constants
				break;
		}
		skipWhitespaceCharacters(reader); //skip any other whitespace characters
		reader.readExpectedChar(ELEMENT_TYPE_DECL_END); //the next character should be the ending delimiter of the declaration
		return element; //return the element we constructed
	}

	/**
	 * Parses an input stream that supposedly begins with an attribute list declaration.
	 * @param reader The reader from which to retrieve characters.
	 * @param ownerDocument The document which will own this element type declaration.
	 * @param parameterEntityMap The parameter entities we've collected so far.
	 * @throws IOException Thrown when an i/o error occurs.
	 * @throws XMLSyntaxException Thrown when there is a syntax error in the XML file.
	 * @throws XMLWellFormednessException Thrown when there is a well-formedness error in the XML file.
	 * @throws XMLUndefinedEntityReferenceException Thrown if a named entity reference has not been defined.
	 * @throws ParseUnexpectedDataException Thrown when an unexpected character is found.
	 * @throws ParseEOFException Thrown when the end of the input stream is reached unexpectedly.
	 * @return A new XML element constructed from the reader containing the declaration information for this element.
	 */
	protected XMLElement parseAttributeListDeclaration(final XMLReader reader, final XMLDocument ownerDocument, final XMLNamedNodeMap parameterEntityMap)
			throws IOException, XMLSyntaxException, XMLWellFormednessException, XMLUndefinedEntityReferenceException, ParseUnexpectedDataException, ParseEOFException {
		//the attribute type strings we expect; notice that "IDREFS", "IDREF", and "ID" must be in that order because the first match is always chosen
		final String[] ATTTYPE_STRINGS = { ATTTYPE_CDATA, ATTTYPE_IDREFS, ATTTYPE_IDREF, ATTTYPE_ID, ATTTYPE_ENTITY, ATTTYPE_ENTITIES, ATTTYPE_NMTOKEN,
				ATTTYPE_NMTOKENS, ATTTYPE_NOTATION, ATTTYPE_ENUMERATION_START };
		//the indexes of the attribute type strings in our array
		final int CDATA = 0, IDREFS = 1, IDREF = 2, ID = 3, ENTITY = 4, ENTITIES = 5, NMTOKEN = 6, NMTOKENS = 7, NOTATION = 8, ENUMERATION = 9;
		final XMLElement element = new XMLElement(ownerDocument, "element"); //create a new XML element which will hold the attribute list for an element declaration TODO use constant
		final String attributeListDeclarationString = reader.readDelimitedString(ATTLIST_DECL_START, ATTLIST_DECL_END); //read all the characters in the attribute list declaration between its start and end
		//here, it would be nice to resolve the parameter entity references as we come to them, but they can be in so many places that we'll just resolve them all at once
		//TODO specify the filename to the reader here somehow, so when an error occurs we can tell what file it is
		XMLReader attributeListDeclarationReader = new XMLReader(attributeListDeclarationString, reader.getName() + ":Attribute List Declaration"); //create a string reader the attribute list declaration, but we don't know its name TODO i18n TODO what if the object is null?

		attributeListDeclarationReader.tidy = isTidy(); //TODO fix

		attributeListDeclarationReader.setLineIndex(reader.getLineIndex()); //pretend we're reading where we already were, so any errors will show at least something close (hopefully)
		attributeListDeclarationReader.setCharIndex(reader.getCharIndex()); //pretend we're reading where we already were, so any errors will show at least something close (hopefully)
		//create a string buffer into which we can expand the attribute list declaration;
		//  we know it will be at least as long as the original string
		final StringBuffer expandedAttributeListDeclarationStringBuffer = new StringBuffer(attributeListDeclarationString.length());
		expandParameterEntityReferences(attributeListDeclarationReader, ownerDocument, parameterEntityMap, expandedAttributeListDeclarationStringBuffer); //expand all character references in this string into the string buffer
		//now, make another reader to parse our new string with the parameter entities expanded
		attributeListDeclarationReader = new XMLReader(expandedAttributeListDeclarationStringBuffer.toString(), reader.getName() + ":Attribute List Declaration"); //create a string reader the attribute list declaration, but we don't know its name TODO i18n

		attributeListDeclarationReader.tidy = isTidy(); //TODO fix

		attributeListDeclarationReader.setLineIndex(reader.getLineIndex()); //pretend we're reading where we already were, so any errors will show at least something close (hopefully)
		attributeListDeclarationReader.setCharIndex(reader.getCharIndex()); //pretend we're reading where we already were, so any errors will show at least something close (hopefully)
		attributeListDeclarationReader.readExpectedChar(WHITESPACE_CHARS); //we expect at least one whitespace character
		skipWhitespaceCharacters(attributeListDeclarationReader); //skip any other whitespace characters
		//TODO check for a valid name here
		element.setAttribute("name", attributeListDeclarationReader.readStringUntilChar(WHITESPACE_CHARS)); //set the name attribute, specifying the name of the element, which will end with whitespace TODO use a constant here
		while(true) { //read all the attribute definitions until we get to the end of the attribute list declaration
			skipWhitespaceCharactersEOF(attributeListDeclarationReader); //skip any whitespace characters, but don't throw an exception if we reach the end of the stream
			if(attributeListDeclarationReader.isEnd()) //if we run out of characters skipping whitespace, we've finished reading the attribute definitions
				break; //we're finished reading the attribute list
			final XMLElement attributeElement = new XMLElement(ownerDocument, "attribute"); //create a new XML element which will hold the rules for this attribute TODO use constant
			//TODO check for a valid name here
			attributeElement.setAttribute("name", attributeListDeclarationReader.readStringUntilChar(WHITESPACE_CHARS)); //set the name attribute, specifying the name of the attribute, which will end with whitespace TODO use a constant here
			attributeListDeclarationReader.readExpectedChar(WHITESPACE_CHARS); //we expect at least one whitespace character
			skipWhitespaceCharacters(attributeListDeclarationReader); //skip any other whitespace characters
			String attributeType = ""; //we'll read the type of attribute
			final int attributeTypeIndex = attributeListDeclarationReader.readExpectedStrings(ATTTYPE_STRINGS); //see what type of attribute type this is
			switch(attributeTypeIndex) { //see what we should do with this attribute type
				case CDATA: //if this is the string data type
					attributeType = "string"; //use "string" as the indication of CDATA, for compatibility with schemas
					break;
				case NOTATION: //if this is a notation
					//TODO fix attributeType=ATTTYPE_STRINGS[attributeTypeIndex];	//store this attribute type with no modification
					attributeListDeclarationReader.readStringUntilChar(')'); //TODO fix
					attributeListDeclarationReader.readExpectedChar(WHITESPACE_CHARS); //we expect at least one whitespace character
					skipWhitespaceCharacters(attributeListDeclarationReader); //skip any other whitespace characters
					//TODO this is wrong here! after a NOTATION follows not an enumeration, but something like a numeration with names instead of Nmtokens
				case ENUMERATION: //if this is an enumeration (or a notation TODO fix conflict between name and nmtoken of NOTATION and ENUMERATION types)
					do {
						skipWhitespaceCharacters(attributeListDeclarationReader); //skip any whitespace characters
						final String nmtokenString = attributeListDeclarationReader.readStringUntilChar(WHITESPACE_CHARS + '|' + ')'); //read the nmtoken TODO actually do something with this here TODO use constants here
						//TODO do something with the nmtoken here
						skipWhitespaceCharacters(attributeListDeclarationReader); //skip any whitespace characters
					} while(attributeListDeclarationReader.readChar() == '|'); //keep reading more nmtokens while we find the or symbol; once we drop out of the loop, we won't need to read the end of the enumeration; we'll have already read it TODO use a constant here
					break;
				default: //if this is on the the standard tokenized types
					//TODO del Log.trace("Found attribute default.");
					attributeType = ATTTYPE_STRINGS[attributeTypeIndex]; //store this attribute type with no modification
					break;
			}
			attributeListDeclarationReader.readExpectedChar(WHITESPACE_CHARS); //we expect at least one whitespace character
			skipWhitespaceCharacters(attributeListDeclarationReader); //skip any other whitespace characters
			//these are the default default declarations
			final int maxOccurs = 1; //currently attributes can appear at most once
			int minOccurs = 0; //default to not requiring the attribute
			String defaultString = null; //if we find a default *or* a fixed value, we'll store it here
			boolean fixed = false; //if we set this to true, this will indicate that the defaultString holds a fixed value
			//the attribute default declaration strings we expect
			final String[] DEFAULT_DECL_STRINGS = { ATT_DEFAULT_REQUIRED, ATT_DEFAULT_IMPLIED, ATT_DEFAULT_FIXED, "" };
			//the indexes of the attribute default declaration strings in our array
			final int REQUIRED = 0, IMPLIED = 1, FIXED = 2;
			switch(attributeListDeclarationReader.readExpectedStrings(DEFAULT_DECL_STRINGS)) { //see what default declaration this is
				case REQUIRED: //if this value is required
					minOccurs = 1; //show that this attribute must appear at least once
					break;
				case IMPLIED: //if this value is implied
					minOccurs = 1; //show that this attribute does not have to appear at all
					break;
				case FIXED: //if this value is fixed
					fixed = true; //show that this is a fixed value; we'll read the actual value below in the default: case
					attributeListDeclarationReader.readExpectedChar(WHITESPACE_CHARS); //we expect at least one whitespace character
					skipWhitespaceCharacters(attributeListDeclarationReader); //skip any other whitespace characters
				default: //if a default value is given
				{
					final char defaultDelimiter = attributeListDeclarationReader.readExpectedChar("\"'"); //read the delimiter of the default value TODO make this a constant
					defaultString = attributeListDeclarationReader.readStringUntilChar(defaultDelimiter); //read the default value, which should end with the correct delimiter (a single or double quote, matching the first one)
					attributeListDeclarationReader.readExpectedChar(defaultDelimiter); //read the ending delimiter (we already know what it will be) TODO this can all probably go into one little routine, since we use it so much
				}
					break;
			}
			attributeElement.setAttribute("minOccurs", Integer.toString(minOccurs)); //add an attribute specifying how many times this attribute must appear TODO use constants
			attributeElement.setAttribute("maxOccurs", Integer.toString(maxOccurs)); //add an attribute specifying how many times this attribute can appear TODO use constants
			if(defaultString != null) { //if we have a default string
				if(fixed) //if this is a fixed value TODO should we fill both the default and fixed attributes?
					attributeElement.setAttribute("fixed", defaultString); //add an attribute specifying the fixed string TODO use constants
				else
					attributeElement.setAttribute("default", defaultString); //add an attribute specifying the default string TODO use constants
			}
			//TODO check to see if this attribute has already been defined
			element.appendChild(attributeElement); //add this attribute definition to the attribute declaration element we're constructing
		}
		return element; //return the element we constructed
	}

	/**
	 * Parses a stream and expands any parameter references found within. The resulting string will be appended to collectedCharacters and returned. This function
	 * will continue collecting characters until the end of the stream is reached. No exception will be thrown at the end of the stream.
	 * @param reader The reader from which to retrieve characters.
	 * @param ownerDocument The document which will own the data.
	 * @param parameterEntityMap The parameter entities we've collected so far
	 * @param expandedText The string buffer which will collect the expanded information read from the reader.
	 * @throws IOException Thrown when an i/o error occurs.
	 * @throws XMLSyntaxException Thrown when there is a syntax error in the XML file.
	 * @throws XMLWellFormednessException Thrown when there is a well-formedness error in the XML file.
	 * @throws XMLUndefinedEntityReferenceException Thrown if a named entity reference has not been defined.
	 * @throws ParseUnexpectedDataException Thrown when an unexpected character is found.
	 * @throws ParseEOFException Thrown when the end of the input stream is reached unexpectedly.
	 * @return The characters until the end of the string with any parameter entity references expanded and padded with one beginning and one end space, as the
	 *         XML specification requires.
	 */
	protected void expandParameterEntityReferences(final XMLReader reader, final XMLDocument ownerDocument, final XMLNamedNodeMap parameterEntityMap,
			final StringBuffer expandedText) throws IOException, XMLSyntaxException, XMLWellFormednessException, XMLUndefinedEntityReferenceException,
			ParseUnexpectedDataException, ParseEOFException {
		while(true) { //keep looking at charactes from the reader until we reach the end of file (which will kick us out from inside the loop)
			expandedText.append(reader.readStringUntilCharEOF('%')); //read character content until we find an entity reference or the end of the stream TODO use a constant here
			if(reader.isEnd()) { //if we're reached the end of the file
				return; //return, sending back all the characters we've collected
			} else { //if we've found an entity reference
				final XMLEntity entity = parseParameterEntityReference(reader, ownerDocument, parameterEntityMap); //parse this entity reference and see which entity it refers to
				final XMLReader entityReader = createEntityReader(reader.getSourceObject(), entity); //we'll use this to read either from the internal value or an external file
				//TODO why aren't we using a string buffer here?
				expandedText.append(' '); //append a space before the value
				//continue collecting data by expanding this parameter entity reference
				expandParameterEntityReferences(entityReader, ownerDocument, parameterEntityMap, expandedText);
				expandedText.append(' '); //append a space before the value
			}
		}
	}

	/**
	 * Parses an input stream that supposedly begins with XML markup.
	 * @param reader The reader from which to retrieve characters.
	 * @param ownerDocument The document which will own the nodes encountered.
	 * @throws IOException Thrown when an i/o error occurs.
	 * @throws XMLSyntaxException Thrown when there is a syntax error in the XML file.
	 * @throws XMLWellFormednessException Thrown when there is a well-formedness error in the XML file.
	 * @throws XMLValidityException Thrown when there is a validity error in the XML file.
	 * @throws XMLUndefinedEntityReferenceException Thrown if a named entity reference has not been defined.
	 * @throws ParseUnexpectedDataException Thrown when an unexpected character is found.
	 * @return A new XML object constructed from the markup.
	 */
	protected XMLNode parseMarkup(final XMLReader reader, final XMLDocument ownerDocument) throws IOException, XMLSyntaxException, XMLWellFormednessException,
			XMLValidityException, XMLUndefinedEntityReferenceException, ParseUnexpectedDataException {
		try {
			reader.resetPeek(); //reset peeking so that the next character peeked will reflect the next character to be read
			//An XML declarations get special processing because 1) its name conflicts
			//with the beginning of a processing instruction, making it difficult for
			//the loop below without whitespace checking, and 2) XML declarations are
			//not normal markup in the first place, and may get processed differently
			//in the code in the future, anyway.
			//if there is an XML declaration ("<?xml") followed by whitespace (don't throw an error on the end of file, in case we're parsing a short ending tag)
			if(reader.peekStringEOF(XML_DECL_START.length()).equals(XML_DECL_START) && isWhitespace(reader.peekChar())) //TODO an XML declaration without whitespace following could throw an exception because of the EOF, but that would be illegal anyway
				return parseXMLDeclaration(reader, ownerDocument); //parse the XML declaration and return it
			else
				//if this is any other markup
				reader.resetPeek(); //reset peeking so that the next character peeked will reflect the next character to be read
			//the markup strings we expect; make sure we put the TAG_START last, because all the markup matches TAG_START
			final String[] EXPECTED_MARKUP_STRINGS = { COMMENT_START, CDATA_START, DOCTYPE_DECL_START, PROCESSING_INSTRUCTION_START, TAG_START };
			//the indexes of the markup strings in our array
			final int COMMENT = 0, CDATA_SECTION = 1, DOCTYPE_DECL = 2, PROCESSING_INSTRUCTION = 3, TAG = 4;
			reader.resetPeek(); //reset peeking so that the next character peeked will reflect the next character to be read
			switch(reader.peekExpectedStrings(EXPECTED_MARKUP_STRINGS)) { //see what kind of markup this is
				case COMMENT: //if this is a comment
					return parseComment(reader, ownerDocument); //parse the comment and return it
				case CDATA_SECTION: //if this is a CDATA section
					return parseCDATASection(reader, ownerDocument); //parse the CDATA and return it
				case DOCTYPE_DECL: //if this is a documement type declaration
					return parseDocumentType(reader, ownerDocument); //parse the document type declaration and return it
				case PROCESSING_INSTRUCTION: //if this is a processing instruction
					return parseProcessingInstruction(reader, ownerDocument); //parse the processing instruction and return it
				case TAG: //if this is a tag
					if(isTidy()) { //if we should tidy, make sure this isn't a misspelled document type or something else besides a tag
						reader.resetPeek(); //reset peeking so that the next character peeked will reflect the next character to be read
						//see if they put the document type declaration in lowercase; don't throw an exception for the end of the file, since parsing the tag will do that if needed TODO what about a case insensitive compare?
						if(reader.peekStringEOF(DOCTYPE_DECL_START.length()).equals(DOCTYPE_DECL_START.toLowerCase()) && isWhitespace(reader.peekChar()))
							return parseDocumentType(reader, ownerDocument); //parse the document type declaration and return it
						else
							//if this is any other markup
							reader.resetPeek(); //reset peeking so that the next character peeked will reflect the next character to be read
					}
					return parseTag(reader, ownerDocument); //parse the tag and return it
				default: //if the markup isn't any of the above (this should never happen unless the code is incorrect)
					throw new XMLSyntaxException("Unrecognized markup.", reader.getLineIndex(), reader.getCharIndex(), reader.getName()); //show that we were expecting markup data TODO this shouldn't be an XMLSyntaxException
			}
		} catch(final ParseEOFException parseEOFException) { //if we run out of data
			throw new XMLWellFormednessException(parseEOFException, ResourceBundle.getBundle(XMLResources.RESOURCE_BUNDLE_BASE_NAME).getString(
					XMLResources.RESOURCE_PREFIX + MARKUP_RESOURCE_ID), ResourceBundle.getBundle(XMLResources.RESOURCE_BUNDLE_BASE_NAME).getString(
					XMLResources.RESOURCE_PREFIX + UNKNOWN_RESOURCE_ID)); //show that we couldn't find markup
		}
	}

	/**
	 * Parses an input stream that supposedly begins with a tag.
	 * @param reader The reader from which to retrieve characters.
	 * @param ownerDocument The document which will own the tag.
	 * @throws IOException Thrown when an i/o error occurs.
	 * @throws XMLSyntaxException Thrown when there is a syntax error in the XML file.
	 * @throws XMLWellFormednessException Thrown when there is a well-formedness error in the XML file.
	 * @throws ParseUnexpectedDataException Thrown when an unexpected character is found.
	 * @throws XMLUndefinedEntityReferenceException Thrown if a named entity reference has not been defined.
	 * @return A new XML tag constructed from the reader.
	 */
	protected XMLTag parseTag(final XMLReader reader, final XMLDocument ownerDocument) throws IOException, XMLSyntaxException, XMLWellFormednessException,
			ParseUnexpectedDataException, XMLUndefinedEntityReferenceException {
		final XMLTag tag = new XMLTag(ownerDocument); //create a new XML tag
		try {
			reader.readExpectedChar('<'); //since we're here, we expect the start of a tag TODO use a constant here
			if(reader.peekChar() == '/') //if the next character to be read is a '/', this is an ending tag TODO use a constant here
			{
				reader.readExpectedChar('/'); //read the next character TODO use a constant here
				tag.setTagType(XMLTag.END_TAG); //show that it's an end tag
				//TODO have the tag check to make sure this is not an empty name
				final String tagName = reader.readStringUntilChar(WHITESPACE_CHARS + ">"); //read the name of the tag, which will end with whitespace or the end-of-tag marker TODO use a constant here
				if(!isName(tagName) && !isTidy()) //if this isn't a valid name, and we're not tidying
					throw new XMLWellFormednessException(XMLWellFormednessException.INVALID_NAME, new Object[] { tagName }, reader.getLineIndex(), reader.getCharIndex(),
							reader.getName()); //show that this isn't a valid XML name
				tag.setNodeName(tagName); //set the name of the tag
				skipWhitespaceCharacters(reader); //skip all whitespace characters (we already know the next character is valid)
				reader.readExpectedChar('>'); //the character after this needs to be the end of the tag TODO use a constant here
				return tag; //return the tag we constructed (an ending tag has no attributes
			}
			//TODO have the tag check to make sure this is not an empty name
			final String tagName = reader.readStringUntilChar(WHITESPACE_CHARS + "/>"); //read the name of the tag, which will end with whitespace or the end-of-tag marker TODO use a constant here
			if(!isName(tagName)) { //if this isn't a valid name
				if(isTidy()) //if we're tidying, we'll just ignore everything in this tag with the invalid name
					reader.readStringUntilChar("/>"); //read and discard everything up to the end of the end-of-tag marker TODO use a constant here
				else
					//if we're not tidying
					throw new XMLWellFormednessException(XMLWellFormednessException.INVALID_NAME, new Object[] { tagName }, reader.getLineIndex(), reader.getCharIndex(),
							reader.getName()); //show that this isn't a valid XML name
			}
			tag.setNodeName(tagName); //set the name of the tag
			while(true) { //read all the attribute/value pairs until we get to the end of the tag
				reader.resetPeek(); //make sure the peek pointer is up-to-date before trying to look at the next character
				reader.peekExpectedChar(WHITESPACE_CHARS + '/' + '>'); //make sure the next character is one we expect (there has to be whitespace unless it's the end of the tag) TODO use constants here
				skipWhitespaceCharacters(reader); //skip any whitespace characters
				switch(reader.peekChar()) { //see what the next character is
					case '/': //if we find the ending tag marker, this is an empty element tag since we know it's not an end tag TODO use a constant here
						reader.readExpectedChar('/'); //read the ending tag marker which we know should be here TODO use a constant here
						tag.setTagType(XMLTag.EMPTY_ELEMENT_TAG); //show that it's an empty element tag
						reader.readExpectedChar('>'); //the next character should be the ending delimiter of the tag TODO combine this with the readExpectedCharacter() above with a constant string
						return tag; //return the tag we constructed
					case '>': //if we find the ending delimiter of the tag TODO use a constant here
						reader.readExpectedChar('>'); //read the end-of-tag marker which we know should be here TODO use a constant here
						tag.setTagType(XMLTag.START_TAG); //show that it's a start tag (we know it's not an end tag, and we didn't find a '/' character)
						return tag; //return the tag we constructed
					default: //any other character means we have more attribute/value pairs
						final XMLAttribute attribute = parseAttribute(reader, ownerDocument); //read an attribute-value pair
						//TODO store the tag's location within the file, and return that location in the error message
						if(tag.getAttributeXMLNamedNodeMap().containsNamedItem(attribute)) //if this attribute has already been defined
							throw new XMLWellFormednessException(XMLWellFormednessException.UNIQUE_ATT_SPEC, new Object[] { tag.getNodeName(), attribute.getNodeName() },
									reader.getLineIndex(), reader.getCharIndex(), reader.getName()); //show that this attribute has already been defined
						//TODO make sure the attribute name is valid, here; this is different than checking the name inside the XMLAttribute object
						tag.getAttributeXMLNamedNodeMap().setNamedItem(attribute); //add this attribute to the tag's map of attributes
						break;
				}
			}
		} catch(final ParseEOFException parseEOFException) { //if we run out of data
			//show that we couldn't find the end of the tag
			throw new XMLWellFormednessException(parseEOFException, ResourceBundle.getBundle(XMLResources.RESOURCE_BUNDLE_BASE_NAME).getString(
					XMLResources.RESOURCE_PREFIX + TAG_RESOURCE_ID), tag.getNodeName() != null ? tag.getNodeName() : ResourceBundle.getBundle(
					XMLResources.RESOURCE_BUNDLE_BASE_NAME).getString(XMLResources.RESOURCE_PREFIX + UNKNOWN_RESOURCE_ID));
		}
	}

	/**
	 * Parses an input stream that supposedly begins with an XML declaration ("<?xml").
	 * @param reader The reader from which to retrieve characters.
	 * @param ownerDocument The document which will own the XML declaration.
	 * @throws IOException Thrown when an i/o error occurs.
	 * @throws XMLSyntaxException Thrown when there is a syntax error in the XML file.
	 * @throws XMLWellFormednessException Thrown when there is a well-formedness error in the XML file.
	 * @throws ParseUnexpectedDataException Thrown when an unexpected character is found.
	 * @throws XMLUndefinedEntityReferenceException Thrown if a named entity reference has not been defined.
	 * @return A new XML declaration constructed from the reader.
	 */
	protected XMLDeclaration parseXMLDeclaration(final XMLReader reader, final XMLDocument ownerDocument) throws IOException, XMLSyntaxException,
			XMLWellFormednessException, ParseUnexpectedDataException, XMLUndefinedEntityReferenceException {
		XMLDeclaration xmlDecl = new XMLDeclaration(ownerDocument); //create a new XML declaration object
		try {
			reader.readExpectedString(XML_DECL_START); //we expect to read the start of an XML declaration
			boolean foundVersionInfo = false, foundEncodingDecl = false, foundSDDecl = false; //whether or not we've found each of the attributes
			while(true) { //read all the attribute/value pairs until we get to the end of the tag
				reader.peekExpectedChar(WHITESPACE_CHARS + '?'); //make sure the next character is one we expect (there has to be whitespace unless it's the end of the XML declaration)
				skipWhitespaceCharacters(reader); //skip any whitespace characters
				switch(reader.peekChar()) { //see what the next character is
					case '?': //if we find the start of the end of the XML declaration TODO use a constant here
						reader.readExpectedString(XML_DECL_END); //we expect to read the end of an XML declaration
						return xmlDecl; //return the XML declaration we constructed
					default: //any other character means we have more attribute/value pairs
						final XMLAttribute attribute = parseAttribute(reader, ownerDocument); //read an attribute-value pair
						if(attribute.getNodeName().equals(VERSIONINFO_NAME)) { //if this is the version info ("version")
							if(foundVersionInfo) //if we've already found the version information
								throw new XMLSyntaxException("\"" + attribute.getNodeName() + "\" already defined.", attribute.getLineIndex(), attribute.getCharIndex(),
										reader.getName()); //show that this attribute has already been defined TODO this shouln't be a syntax exception
							//TODO check the format of the version number here
							foundVersionInfo = true; //show that we've found the version info
							xmlDecl.setVersion(attribute.getNodeValue()); //set the version
						} else if(attribute.getNodeName().equals(ENCODINGDECL_NAME)) { //if this is the encoding declaration ("encoding")
							if(!foundVersionInfo) //if we haven't yet found the version information
								throw new XMLSyntaxException("Missing \"" + VERSIONINFO_NAME + "\" declaration.", attribute.getLineIndex(), attribute.getCharIndex(),
										reader.getName()); //show that the version info is missing TODO this shouldn't be a syntax exception
							if(foundEncodingDecl) //if we've already found the encoding declaration
								throw new XMLSyntaxException("\"" + attribute.getNodeName() + "\" already defined.", attribute.getLineIndex(), attribute.getCharIndex(),
										reader.getName()); //show that this attribute has already been defined TODO this shouln't be a syntax exception
							if(foundSDDecl) //if we've already found the standalone declaration
								throw new XMLSyntaxException("\"" + attribute.getNodeName() + "\" out of order.", attribute.getLineIndex(), attribute.getCharIndex(),
										reader.getName()); //show that this attribute is out of order TODO this shouln't be a syntax exception
							//TODO check the format of the version number here
							foundEncodingDecl = true; //show that we've found the encoding declaration
							xmlDecl.setEncoding(attribute.getNodeValue()); //set the encoding
						} else if(attribute.getNodeName().equals(SDDECL_NAME)) { //if this is the encoding declaration ("encoding")
							if(!foundVersionInfo) //if we haven't yet found the version information
								throw new XMLSyntaxException("Missing \"" + VERSIONINFO_NAME + "\" declaration.", attribute.getLineIndex(), attribute.getCharIndex(),
										reader.getName()); //show that this attribute has already been defined TODO this shouln't be a syntax exception
							if(foundSDDecl) //if we've already found the standalone declaration
								throw new XMLSyntaxException("\"" + attribute.getNodeName() + "\" already defined.", attribute.getLineIndex(), attribute.getCharIndex(),
										reader.getName()); //show that this attribute has already been defined TODO this shouln't be a syntax exception
							//TODO check the format of the value here
							foundSDDecl = true; //show that we've found the standalone declaration
							//TODO fix						xmlDecl.setEncoding(attribute.getNodeValue());	//set the encoding
						} else
							throw new XMLSyntaxException("Invalid XML declaration attribute: \"" + VERSIONINFO_NAME + "\".", attribute.getLineIndex(),
									attribute.getCharIndex(), reader.getName()); //show that we don't recognize this attribute TODO this shouldn't be a syntax exception
						break;
				}
			}
		} catch(final ParseEOFException parseEOFException) { //if we run out of data
			throw new XMLWellFormednessException(parseEOFException, ResourceBundle.getBundle(XMLResources.RESOURCE_BUNDLE_BASE_NAME).getString(
					XMLResources.RESOURCE_PREFIX + XML_DECLARATION_RESOURCE_ID), xmlDecl.getNodeName() != null ? xmlDecl.getNodeName() : ResourceBundle.getBundle(
					XMLResources.RESOURCE_BUNDLE_BASE_NAME).getString(XMLResources.RESOURCE_PREFIX + UNKNOWN_RESOURCE_ID)); //show that we couldn't find the end of the XML declaration
		}
	}

	/**
	 * Parses an input stream that supposedly begins with an XML processing instruction.
	 * @param reader The reader from which to retrieve characters.
	 * @param ownerDocument The document which will own the XML declaration.
	 * @throws IOException Thrown when an i/o error occurs.
	 * @throws XMLWellFormednessException Thrown when there is a well-formedness error in the XML file.
	 * @throws ParseUnexpectedDataException Thrown when an unexpected character is found.
	 * @return A new XML processing instruction constructed from the reader.
	 */
	protected XMLProcessingInstruction parseProcessingInstruction(final XMLReader reader, final XMLDocument ownerDocument) throws IOException,
			XMLSyntaxException, XMLWellFormednessException, ParseUnexpectedDataException {
		XMLProcessingInstruction processingInstruction = new XMLProcessingInstruction(ownerDocument); //create a new processing instruction
		try {
			reader.readExpectedString(PROCESSING_INSTRUCTION_START); //we expect to read the start of an XML declaration
			final String piTarget = reader.readStringUntilChar(WHITESPACE_CHARS); //read the name of the processing instruction, which will end with whitespace
			if(!isPITarget(piTarget)) //if this isn't a valid processing instruction target
				throw new XMLWellFormednessException(XMLWellFormednessException.INVALID_PI_TARGET, new Object[] { piTarget }, reader.getLineIndex(),
						reader.getCharIndex(), reader.getName()); //show that this isn't a valid XML name
			processingInstruction.setNodeName(piTarget); //set the name (target) of the processing instruction
			skipWhitespaceCharacters(reader); //skip any whitespace characters (we require at least one, but we know there's one here because we found one when reading the name, above)
			processingInstruction.setData(reader.readDelimitedString(PROCESSING_INSTRUCTION_END)); //read all the characters in the processing instruction up to the PROCESSING_INSTRUCTION_END delimiter
			return processingInstruction; //return the processing instruction we constructed
		} catch(final ParseEOFException parseEOFException) { //if we run out of data
			throw new XMLWellFormednessException(parseEOFException, ResourceBundle.getBundle(XMLResources.RESOURCE_BUNDLE_BASE_NAME).getString(
					XMLResources.RESOURCE_PREFIX + PROCESSING_INSTRUCTION_RESOURCE_ID), processingInstruction.getTarget() != null ? processingInstruction.getNodeName()
					: ResourceBundle.getBundle(XMLResources.RESOURCE_BUNDLE_BASE_NAME).getString(XMLResources.RESOURCE_PREFIX + UNKNOWN_RESOURCE_ID)); //show that we couldn't find the end of the XML processing instruction
		}
	}

	/**
	 * Parses an input stream that supposedly begins with a CDATA section.
	 * @param reader The reader from which to retrieve characters.
	 * @param ownerDocument The document which will own this CDATA node.
	 * @throws IOException Thrown when an i/o error occurs.
	 * @throws XMLSyntaxException Thrown when there is a syntax error in the XML file.
	 * @throws XMLWellFormednessException Thrown when there is a well-formedness error in the XML file.
	 * @throws ParseUnexpectedDataException Thrown when an unexpected character is found.
	 * @return A new CDATA constructed from the reader.
	 */
	protected XMLCDATASection parseCDATASection(final XMLReader reader, final XMLDocument ownerDocument) throws IOException, XMLSyntaxException,
			XMLWellFormednessException, ParseUnexpectedDataException {
		try {
			String characterData = reader.readDelimitedString(CDATA_START, CDATA_END); //read all the characters in the CDATA section between the CDATA_START and CDATA_END delimiters
			XMLCDATASection CDATA = new XMLCDATASection(ownerDocument, characterData); //create a new CDATA object
			return CDATA; //return the CDATA we constructed
		} catch(final ParseEOFException parseEOFException) { //if we run out of data
			throw new XMLWellFormednessException(parseEOFException, ResourceBundle.getBundle(XMLResources.RESOURCE_BUNDLE_BASE_NAME).getString(
					XMLResources.RESOURCE_PREFIX + CDATA_RESOURCE_ID), ResourceBundle.getBundle(XMLResources.RESOURCE_BUNDLE_BASE_NAME).getString(
					XMLResources.RESOURCE_PREFIX + UNKNOWN_RESOURCE_ID)); //show that we couldn't find the end of the CDATA
		}
	}

	/**
	 * Parses an input stream that supposedly begins with a comment.
	 * @param reader The reader from which to retrieve characters.
	 * @param ownerDocument The document which will own this comment.
	 * @throws IOException Thrown when an i/o error occurs.
	 * @throws XMLSyntaxException Thrown when there is a syntax error in the XML file.
	 * @throws XMLWellFormednessException Thrown when there is a well-formedness error in the XML file.
	 * @throws ParseUnexpectedDataException Thrown when an unexpected character is found.
	 * @return A new comment object constructed from the reader.
	 */
	protected XMLComment parseComment(final XMLReader reader, final XMLDocument ownerDocument) throws IOException, XMLSyntaxException,
			XMLWellFormednessException, ParseUnexpectedDataException {
		try {
			String characterData = reader.readDelimitedString(COMMENT_START, COMMENT_END_PART1); //read all the characters in the comment section between the COMMENT_START and COMMENT_END_PART1 delimiters
			XMLComment comment = new XMLComment(ownerDocument, characterData); //create a new comment object
			reader.readExpectedString(COMMENT_END_PART2); //read the second part of the end of the comment (this two-step process is because XML doesn't allow "--" inside of comment for compatibility reasons)
			return comment; //return the comment we constructed
		} catch(final ParseEOFException parseEOFException) { //if we run out of data
			throw new XMLWellFormednessException(parseEOFException, ResourceBundle.getBundle(XMLResources.RESOURCE_BUNDLE_BASE_NAME).getString(
					XMLResources.RESOURCE_PREFIX + COMMENT_RESOURCE_ID), ResourceBundle.getBundle(XMLResources.RESOURCE_BUNDLE_BASE_NAME).getString(
					XMLResources.RESOURCE_PREFIX + UNKNOWN_RESOURCE_ID)); //show that we couldn't find the end of the comment
		}
	}

	/**
	 * Parses an input stream that supposedly contains an XML element.
	 * @param reader The reader from which to retrieve characters.
	 * @param ownerDocument The document which will own this element.
	 * @param tag The tag that marks the start of this element, which can be a start tag or an empty element tag.
	 * @throws IOException Thrown when an i/o error occurs.
	 * @throws XMLSyntaxException Thrown when there is a syntax error in the XML file.
	 * @throws XMLWellFormednessException Thrown when there is a well-formedness error in the XML file.
	 * @throws XMLValidityException Thrown when there is a validity error in the XML file.
	 * @throws XMLUndefinedEntityReferenceException Thrown if a named entity reference has not been defined.
	 * @throws ParseUnexpectedDataException Thrown when an unexpected character is found.
	 * @return A new XML element with the appropriate content.
	 */
	protected XMLElement parseElement(final XMLReader reader, final XMLDocument ownerDocument, final XMLTag tag) throws IOException, XMLSyntaxException,
			XMLWellFormednessException, XMLValidityException, XMLUndefinedEntityReferenceException, ParseUnexpectedDataException {
		//TODO store the start position of the element so we can report the error location
		//TODO check to make sure that they passed us a start tag or an empty element tag, not an ending tag
		final XMLElement element = new XMLElement(ownerDocument, tag); //create a new XML element with the same name and attributes as the tag (which should either be a start tag or an empty element tag)
		try {
			if(tag.getTagType() == XMLTag.EMPTY_ELEMENT_TAG) //if this is an empty element tag
				return element; //there is no content in this element
			if(!parseElementContent(reader, ownerDocument, element, "")) { //parse the element content, showing that we have not yet found any character content; if we couldn't find the end of the element
				throw new XMLWellFormednessException(XMLWellFormednessException.EOF, new Object[] {
						ResourceBundle.getBundle(XMLResources.RESOURCE_BUNDLE_BASE_NAME).getString(XMLResources.RESOURCE_PREFIX + ELEMENT_RESOURCE_ID),
						element.getNodeName() }, reader.getLineIndex(), reader.getCharIndex(), reader.getName()); //show that we couldn't find the end of the element
			}
			return element; //return the element we constructed
		} catch(final ParseEOFException parseEOFException) { //if we run out of data
			throw new XMLWellFormednessException(parseEOFException, ResourceBundle.getBundle(XMLResources.RESOURCE_BUNDLE_BASE_NAME).getString(
					XMLResources.RESOURCE_PREFIX + ELEMENT_RESOURCE_ID), element.getNodeName()); //show that we couldn't find the end of the element
		}

	}

	/**
	 * Parses an input stream that contains content of an element. If this function reaches the end of the stream of the reader without finding an ending tag for
	 * the element, an exception is not thrown; rather, the return code will indicate if the end tag was found.
	 * @param reader The reader from which to retrieve characters.
	 * @param ownerDocument The document which will own the data.
	 * @param element The element that this content belongs to.
	 * @param characterContent Any character content which has already been accumulated for this element.
	 * @throws IOException Thrown when an i/o error occurs.
	 * @throws XMLSyntaxException Thrown when there is a syntax error in the XML file.
	 * @throws XMLWellFormednessException Thrown when there is a well-formedness error in the XML file.
	 * @throws XMLValidityException Thrown when there is a validity error in the XML file.
	 * @throws XMLUndefinedEntityReferenceException Thrown if a named entity reference has not been defined.
	 * @throws ParseUnexpectedDataException Thrown when an unexpected character is found.
	 * @throws ParseEOFException Thrown when the end of the input stream is reached unexpectedly.
	 * @return <code>true</code> if the ending tag for this element was found, else <code>false</code>.
	 */
	protected boolean parseElementContent(final XMLReader reader, final XMLDocument ownerDocument, final XMLElement element, String characterContent)
			throws IOException, XMLSyntaxException, XMLWellFormednessException, XMLValidityException, XMLUndefinedEntityReferenceException,
			ParseUnexpectedDataException, ParseEOFException {
		while(true) { //keep reading content; when we find the ending tag we'll return from there
			//TODO we *might* be able to improve this characterContent somewhat by using a string buffer
			characterContent += reader.readStringUntilCharEOF("<&"); //read character content until we find non-character data markup, such as a tag or comment or a reference, or until we reach the end of the file TODO make these constants
			if(reader.isEnd()) { //if we run out of characters looking for the end of the tag, this may not be an error; we may have just been parsing a general entity reader
				element.appendText(characterContent); //append whatever text we've collected so far to the element
				return false; //return, showing that we have not yet found the ending tag
			}
			switch(reader.peekChar()) { //see which character comes next
				case '&': //if this is a character or entity reference
					if(reader.peekChar() == '#') //if this is a character reference TODO make this a constant
						characterContent += parseCharacterReference(reader); //parse this character reference and add the character to our character content
					else { //if this is not a character reference, it must be an entity reference
						final Object entityResult = parseEntityReference(reader, ownerDocument); //parse this entity reference and see which entity it refers to TODO do we want to parse the entity and store everything as an entity reference node?
						if(entityResult instanceof XMLEntity) { //if we found a matching entity
							final XMLEntity entity = (XMLEntity)entityResult; //case the result to an entity
							if(nestedEntityReferenceMap.containsKey(entity)) //if, somewhere along our nested way, we've already processed this entity (i.e. this is a circular entity reference)
								throw new XMLWellFormednessException(XMLWellFormednessException.NO_RECURSION, new Object[] { entity.getNodeName() }, reader.getLineIndex(),
										reader.getCharIndex(), reader.getName()); //show that an entity can't reference itself, directly or indirectly TODO we need to give a better position in the error, here
							final XMLReader entityReader = new XMLReader(entity.getText(), "General entity \"" + entity.getNodeName() + "\" from \"" + entity.getSourceName()
									+ "\""); //create a string reader from the text of the entity, giving our entity name for the name of the reader TODO i18n

							entityReader.tidy = isTidy(); //TODO fix

							entityReader.setLineIndex(entity.getLineIndex()); //pretend we're reading where the entity was located in that file, so any errors will show the correct information
							entityReader.setCharIndex(entity.getCharIndex()); //pretend we're reading where the entity was located in that file, so any errors will show the correct information
							nestedEntityReferenceMap.put(entity, entity); //show that we're getting ready to process this entity
							if(parseElementContent(entityReader, ownerDocument, element, characterContent)) //parse the entity text just as if it were included in the file (except for markup across entity boundaries); if they found ending tag for the element was found
								return true; //show that we found the end of the element
							nestedEntityReferenceMap.remove(entity); //show that we're finished processing this entity
							characterContent = ""; //since we passed the character content to parseElementContent(), it will have already been appended to the text of this element
						} else { //if there was no matching entity, yet this doesn't cause a well-formedness error
							//TODO if we're validating, we'll probably want to throw a validity error, here
							characterContent += (String)entityResult; //store the entity reference name in our character data
						}
					} //TODO perhaps have a more instructive error here if they try to split up general entities across entities
					break;
				case '<': //if this is some sort of markup TODO make these constant somewhere or something
					if(characterContent.length() > 0) { //if we have collected character content
						//TODO should we check for a DOMException and throw something based on XMLException here?
						element.appendChild(new XMLText(ownerDocument, characterContent)); //create a text node from the character element we've collected so far and add it to this element's children
						characterContent = ""; //show that other character content (if any) will appear after the element we're beginning to parse (if that's what it is), and will therefore be a separate XMLText node
					}
					XMLNode node = parseMarkup(reader, ownerDocument); //parse this bit of markup, and get a node which represents it
					switch(node.getNodeType()) { //see what type of node we just parsed
						case XMLNode.TAG_NODE: //if we just read a tag
							final XMLTag childTag = (XMLTag)node; //cast the node to a tag
							if(isTidy()) //if we're tidying the document
								tidyTag(element, childTag); //tidy the tag; this may change the tag to an empty element tag, if it's supposed to be
							switch(childTag.getTagType()) { //see what type of tag this is
								case XMLTag.START_TAG: //if this is a starting tag
								case XMLTag.EMPTY_ELEMENT_TAG: //if this is an empty element tag
									if(isTidy()) { //if we're tidying the document
										if(!isName(childTag.getNodeName())) { //if the tag doesn't have a valid name
											break; //ignore this tag
										}
										if(!isTidyContentModel(element, childTag)) { //if this tag shouldn't be a child of the enclosing element
											reader.unread(childTag.toXMLString().toCharArray()); //unread the child tag so that it will be read the next time instead of processing it now TODO use constants here
											element.appendText(characterContent); //append whatever text we've collected so far to the element TODO testing
											return true; //we're finished parsing all the element content we know about, so return TODO how will we know when we're at the end of the tag if we're just looking at a string?
										}
									}
									final XMLElement childElement = parseElement(reader, ownerDocument, childTag); //parse the child element
									//TODO should we check for a DOMException and throw something based on XMLException here?
									element.appendChild(childElement); //add this child element to our list of children
									break;
								case XMLTag.END_TAG: //if this is the ending tag
								default: //TODO why do we check for end tag and also have a default?
									//TODO do we really want empty ending tags to be allowed?									if(childTag.getNodeName().length()==0 && isAllowUnnamedEndTags())	//if we should allow unnamed end tags (for compatibility with MSXML-generated XML files, for example)
									//TODO do we really want empty ending tags to be allowed?										foundEndTag=true;	//show that we've found the end tag for this element
									//TODO do we really want empty ending tags to be allowed?									else if(!childTag.getNodeName().equals(element.getNodeName()))	//if the ending tag doesn't have the same name as the beginning tag
									if(!childTag.getNodeName().equals(element.getNodeName())) { //if the ending tag doesn't have the same name as the beginning tag
										if(isTidy()) { //if we're tidying the document
											//assume there was just a missing end-tag for this element, and hopefully *this* tag is the ending tag to one of our ancestors
											reader.unread(childTag.toXMLString().toCharArray()); //unread the ending tag so that it will be read the next time as an end tag for our parent instead of processing it now TODO use constants here
										} else
											//if we're not tidying the document
											throw new XMLWellFormednessException(XMLWellFormednessException.ELEMENT_TYPE_MATCH, new Object[] { element.getNodeName(),
													childTag.getNodeName() }, reader.getLineIndex(), reader.getCharIndex(), reader.getName()); //show that the beginning and ending tags do not match
									}
									element.appendText(characterContent); //append whatever text we've collected so far to the element
									return true; //we're finished parsing all the element content we know about, so return TODO how will we know when we're at the end of the tag if we're just looking at a string?
							}
							break;
						case XMLNode.DOCUMENT_TYPE_NODE: //if this is a document type node, this will be an error unless we're tidying, in which case we'll use the new document type
							if(isTidy()) //if we're tidying
								break; //ignore the error TODO we need to first set the document type, after we remove that functionality from the parseDocumentType() method
							//if we're not tidying, we'll fall through to the default functionality which will generate an error TODO maybe explicitly do the error stuff here
						default: //all other node types get added normally
							element.appendChild(node); //add this child node to our list of children
							break;
					}
					break;
			}
		}
	}

	/**
	 * Does whatever processing is needed to tidy the specified tag before its being returned. This will, for example, check to see if a tag represents an empty
	 * element, and if so, automatically convert that tag to an empty element tag. This function is called from the <code>parseTag()</code> method. This function
	 * is only called in tidy mode.
	 * @param parentNode The node which will become the tag's parent.
	 * @param childTag The tag to become a child of the element.
	 * @see #isTidy
	 * @see #parseTag
	 */
	protected void tidyTag(final XMLNode parentNode, final XMLTag childTag) {
		final String tagName = childTag.getNodeName().toLowerCase(); //get the name of the tag, and change the name to lowercase TODO later, check the schema for case
		childTag.setNodeName(tagName); //change the child tag name, in case our changing its case changed the name
		//tidy the attribute names
		final XMLNamedNodeMap attributeXMLNamedNodeMap = (XMLNamedNodeMap)childTag.getAttributes(); //get the attributes
		if(attributeXMLNamedNodeMap.size() != 0) { //if there are attributes
			final XMLNamedNodeMap newAttributeNamedNodeMap = new XMLNamedNodeMap(); //create a new named node map that will contain the new tidied values
			final Iterator attributeIterator = attributeXMLNamedNodeMap.values().iterator(); //create an iterator to iterate through the original attributes
			while(attributeIterator.hasNext()) { //while there are more attributes
				final XMLAttribute attribute = (XMLAttribute)attributeIterator.next(); //get the next attribute
				final String attributeName = attribute.getNodeName().toLowerCase(); //get the attribute's name and convert the name to lowercase TODO later make this namespace aware when we update namespaces on the fly from within the XML processor
				attribute.setNodeName(attributeName); //change the attribute name to its lowercase version
				newAttributeNamedNodeMap.setNamedItem(attribute); //add the attribute to our new named node map with its new name TODO make this namespace aware, too, when needed
			}
			childTag.setAttributeXMLNamedNodeMap(newAttributeNamedNodeMap); //now that we've processed the attributes, replace the old non-namespace-aware attrites with our new ones that recognize namespaces
		}
		if(tagName.equals("br") //TODO get all this from the DTD
				|| tagName.equals("area") || tagName.equals("hr") || tagName.equals("img") || tagName.equals("link")
				|| tagName.equals("param")
				|| tagName.equals("frame") || tagName.equals("meta"))
			childTag.setTagType(XMLTag.EMPTY_ELEMENT_TAG); //convert the tag to an empty element tag
	}

	/**
	 * Determines whether a child tag fits in in the content model of a given element. If this function returns <code>false</code>, the enclosing element will be
	 * tidied by implicitly being finished before the child tag is processed. This function is only called in tidy mode.
	 * @param parentElement The element which will become the tag's parent.
	 * @param childTag The tag to become a child of the element.
	 * @return <code>true</code> If the tag should become a child of the element.
	 * @see #isTidy
	 */
	protected boolean isTidyContentModel(final XMLElement parentElement, final XMLTag childTag) {
		//TODO testing; these are elements which cannot have content
		if(parentElement.getNodeName().equals("meta") || parentElement.getNodeName().equals("link"))
			return false; //TODO testing
		if(parentElement.getNodeName().equals("p") && (childTag.getNodeName().equals("p") || childTag.getNodeName().equals("body"))) //TODO testing; comment
			return false; //TODO testing
		return true; //TODO fix
	}

	/**
	 * Parses an XML document.
	 * @param reader The reader from which to get the XML data. //TODO fix @param validate Whether the document should be validated against stylesheets, if any.
	 *          //TODO fix @param processStyleSheets Whether any stylesheets encountered should be recognized and processed as such. //TODO fix @param
	 *          applyStyleSheets Whether any stylesheets encountered should be applied.
	 * @throws IOException Thrown when an i/o error occurs.
	 * @throws XMLSyntaxException Thrown when there is a syntax error in the XML file.
	 * @throws XMLWellFormednessException Thrown when there is a well-formedness error in the XML file.
	 * @throws XMLValidityException Thrown when there is a validity error in the XML file.
	 * @throws XMLUndefinedEntityReferenceException Thrown if a named entity reference has not been defined.
	 * @return A new XML document.
	 */
	protected XMLDocument parseDocument(final XMLReader reader) throws IOException, XMLSyntaxException, XMLWellFormednessException, XMLValidityException,
			XMLUndefinedEntityReferenceException {
		long nodeLineIndex = 0, nodeCharIndex = 0; //make a note of where each node begins

		XMLDocument document = new XMLDocument(); //create a new document	TODO use the XMLDOMImplementation function here
		//TODO do we want to store the default values somewhere else? perhaps in the document itself?
		//TODO use constants below
		//create the default XML entity references; note that if these would have been declared in the actual MXL document, the &#38; would already have been expanded to '&', as shown here
		getXMLDocumentType().getEntityXMLNamedNodeMap().setNamedItem(new XMLEntity(document, "lt", "&#60;")); //lt
		getXMLDocumentType().getEntityXMLNamedNodeMap().setNamedItem(new XMLEntity(document, "gt", "&#62;")); //gt
		getXMLDocumentType().getEntityXMLNamedNodeMap().setNamedItem(new XMLEntity(document, "amp", "&#38;")); //amp
		getXMLDocumentType().getEntityXMLNamedNodeMap().setNamedItem(new XMLEntity(document, "apos", "&#39;")); //apos
		getXMLDocumentType().getEntityXMLNamedNodeMap().setNamedItem(new XMLEntity(document, "quot", "&#34;")); //quot
		try {
			XMLNode node = parseMarkup(reader, document); //parse the markup
			if(node.getNodeType() != XMLNode.ELEMENT_NODE) { //if this is not the root element of the document TODO would this really ever return an element node? TODO no, it wouldn't; remove this line
				if(node.getNodeType() == XMLNode.XMLDECL_NODE) { //if this is an XML declaration
					//TODO should we check for a DOMException and throw something based on XMLException here?
					document.setXMLDeclaration((XMLDeclaration)node); //tell the document which XML declaration it has
					//TODO decide if we want to do this or not			document.appendChild(node);	//store the XML declaration in the document
					//TODO store the XML declaration here somewhere
					skipWhitespaceCharacters(reader); //skip any whitespace characters
					nodeLineIndex = reader.getLineIndex(); //store the current line index
					nodeCharIndex = reader.getCharIndex(); //store the current character index TODO do we need to add one to this?
					node = parseMarkup(reader, document); //parse the following markup
				}
				boolean foundDTD = false; //show that we haven't found a document type declaration, yet
				while(node.getNodeType() != XMLNode.TAG_NODE) { //we won't be finished with the prolog until we've found the root element of the document
					switch(node.getNodeType()) { //see which type of node this is
						case XMLNode.XMLDECL_NODE: //if this is an XML declaration
							throw new XMLSyntaxException("XML declaration not allowed here.", nodeLineIndex, nodeCharIndex, reader.getName()); //show that an XML declaration isn't allowed here TODO this probably isn't a syntax error TODO i18n
						case XMLNode.DOCUMENT_TYPE_NODE: //if this is a document
							if(foundDTD) //if we've already found the document type declaration
								throw new XMLSyntaxException("Cannot have more than one document type declaration.", nodeLineIndex, nodeCharIndex, reader.getName()); //show that there can't be more than one DTD TODO this probably isn't a syntax error TODO i18n
							foundDTD = true; //show that we found the document type declaration
							break;
					}
					//TODO see if we should check to make sure this is a comment or processing instruction
					document.appendChild(node); //whatever type of node it is, append it to our document
					skipWhitespaceCharacters(reader); //skip any whitespace characters
					nodeLineIndex = reader.getLineIndex(); //store the current line index
					nodeCharIndex = reader.getCharIndex(); //store the current character index TODO do we need to add one to this? is this even used?
					node = parseMarkup(reader, document); //parse the following markup
				}
			}
			final XMLTag rootTag = (XMLTag)node; //cast the root node to a tag
			if(isTidy()) //if we're tidying the document
				tidyTag(document, rootTag); //tidy the tag; this may change the tag to an empty element tag, if it's supposed to be
			//if tidying, load a default DTD if needed
			if(isTidy() && document.getXMLDocumentType() == null) { //if we're tidying the document and have not yet created a document type (because there has been no DTD, neither internal nor external)
				final XMLExternalID tidyDocumentTypeExternalID = getTidyDocumentTypeExternalID(); //see if there is an overriding external ID to use
				if(tidyDocumentTypeExternalID != null) { //if there is an external ID we should use for tidying
					final XMLDocumentType documentType = (XMLDocumentType)getXMLDocumentType().cloneXMLNode(true); //we'll use the default XML document type we've already built TODO do we want to do it this way?
					document.setXMLDocumentType(documentType); //tell the owner document which document type it has
					documentType.setExternalID(tidyDocumentTypeExternalID); //update the document's external ID to match the one required by tidying
					//parse the external document type requested for tidying TODO fix the XMLNodeList parameters; remove when schemas/real DTD parsing works
					parseExternalDocumentType(reader, document, documentType, new XMLNodeList(), new XMLNodeList());
				}
			}
			//TODO remove this when default tidy DTDs completely work
			if(isTidy() && rootTag.getNodeName().equals("html")) { //TODO testing; maybe put this in another function to be overridden; comment; there must be a better way to do this
				getXMLDocumentType().getEntityXMLNamedNodeMap().setNamedItem(new XMLEntity(document, "nbsp", "&#160;")); //TODO fix; comment; get this from some default DTD
			}
			final XMLElement root = parseElement(reader, document, rootTag); //parse the child element
			final XMLDocumentType docType = document.getXMLDocumentType(); //get a document type if we have one
			if(docType != null && !root.getNodeName().equals(docType.getName())) { //if we have a document type, and the root's name doesn't match what it calls for TODO only do this if we should validate, but perhaps *always* tidy if needed, whether we're validating or not
				if(isTidy()) //if we're tidying
					docType.setNodeName(root.getNodeName()); //make the document type name match the root node name TODO should we do this after namespace processing? or should we put namespace processing in the processor itself?
				else
					//if we're not tidying, this consitutes a validity error
					throw new XMLValidityException(XMLValidityException.ROOT_ELEMENT_TYPE, new Object[] { root.getNodeName(), docType.getName() }, reader.getLineIndex(),
							reader.getCharIndex(), reader.getName()); //show that this element type has already been declared TODO give a better indication of the element type declaration's location
			}
			//TODO does this adequately check for multiple root nodes?
			document.getChildXMLNodeList().add(root); //add the root to the document (don't append it using a DOM method, because that will throw an exception)
			//TODO set the document, set the parent, etc.
			root.setParentXMLNode(document); //set the parent of the added child
			//TODO fix to allow items after the document element
			//TODO somewhere here, catch a sun.io.MalformedInputException when the input data is bad
		} catch(final ParseUnexpectedDataException parseUnexpectedDataException) { //if we run into data we don't recognize
			throw new XMLWellFormednessException(parseUnexpectedDataException); //create a well-formedness exception and throw it
		} catch(final ParseEOFException parseEOFException) { //if we run out of data
			throw new XMLWellFormednessException(parseEOFException, ResourceBundle.getBundle(XMLResources.RESOURCE_BUNDLE_BASE_NAME).getString(
					XMLResources.RESOURCE_PREFIX + DOCUMENT_RESOURCE_ID), document.getNodeName() != null ? document.getNodeName() : ResourceBundle.getBundle(
					XMLResources.RESOURCE_BUNDLE_BASE_NAME).getString(XMLResources.RESOURCE_PREFIX + UNKNOWN_RESOURCE_ID)); //show that we couldn't find the end of the document
		}
		//TODO decide if we should close the reader ourselves here
		//TODO process namespaces here
		XMLProcessor.processNamespaces(document); //TODO testing
		//TODO catch whatever DOMExceptions are thrown here
		return document; //return the parsed document
	}

	/**
	 * Parses an XML document from an input stream.
	 * @param inputStream The input stream from which to get the XML data.
	 * @param sourceObject The source of the data (e.g. a String, File, or URL).
	 * @throws IOException Thrown when an i/o error occurs.
	 * @throws XMLSyntaxException Thrown when there is a syntax error in the XML file.
	 * @throws XMLWellFormednessException Thrown when there is a well-formedness error in the XML file.
	 * @throws XMLValidityException Thrown when there is a validity error in the XML file.
	 * @throws XMLUndefinedEntityReferenceException Thrown if a named entity reference has not been defined.
	 * @return A new XML document.
	 */
	public XMLDocument parseDocument(final InputStream xmlInputStream, final Object sourceObject) throws IOException, XMLSyntaxException,
			XMLWellFormednessException, XMLValidityException, XMLUndefinedEntityReferenceException {
		final XMLReader xmlReader = createReader(xmlInputStream, sourceObject); //create a reader from the input stream
		return parseDocument(xmlReader); //parse the document using the reader we constructed, and return the new document
	}

	/**
	 * Parses an XML document from a reader.
	 * @param reader The input stream from which to get the XML data.
	 * @param sourceObject The source of the data (e.g. a String, File, or URL).
	 * @throws IOException Thrown when an i/o error occurs.
	 * @throws XMLSyntaxException Thrown when there is a syntax error in the XML file.
	 * @throws XMLWellFormednessException Thrown when there is a well-formedness error in the XML file.
	 * @throws XMLValidityException Thrown when there is a validity error in the XML file.
	 * @throws XMLUndefinedEntityReferenceException Thrown if a named entity reference has not been defined.
	 * @return A new XML document.
	 */
	public XMLDocument parseDocument(final Reader reader, final Object sourceObject) throws IOException, XMLSyntaxException, XMLWellFormednessException,
			XMLValidityException, XMLUndefinedEntityReferenceException {
		final XMLReader xmlReader = new XMLReader(reader, sourceObject); //create an XML reader from the reader
		return parseDocument(xmlReader); //parse the document using the reader we constructed, and return the new document
	}

	/**
	 * Parses a string which contains content for the given element. The text and element nodes are appended to the given element. The given element must have a
	 * valid document as its owner. If an ending tag for the specified element is present, an <code>XMLWellFormednessException</code> will be thrown.
	 * @param element The element to which the parsed XML sub-tree should be appended.
	 * @param elementContent The string which contains information to parse.
	 * @throws IOException Thrown when an i/o error occurs.
	 * @throws XMLSyntaxException Thrown when there is a syntax error in the XML file.
	 * @throws XMLWellFormednessException Thrown when there is a well-formedness error in the XML file.
	 * @throws XMLValidityException Thrown when there is a validity error in the XML file.
	 * @throws XMLUndefinedEntityReferenceException Thrown if a named entity reference has not been defined.
	 */
	public void parseElementContent(final XMLElement element, final String elementContent) throws IOException, XMLSyntaxException, XMLWellFormednessException,
			XMLValidityException, XMLUndefinedEntityReferenceException {
		final XMLDocument document = (XMLDocument)element.getOwnerDocument(); //get the owner document of the element
		final XMLReader reader = new XMLReader(elementContent, "Element Content"); //create a string reader from the given element content TODO i18n

		reader.tidy = isTidy(); //TODO fix

		try {
			if(parseElementContent(reader, (XMLDocument)element.getOwnerDocument(), element, "")) { //parse the element content, showing that we have not yet found any character content; if we found the end of the element
				throw new XMLWellFormednessException(XMLWellFormednessException.UNEXPECTED_DATA, new Object[] { "", "</" + element.getNodeName() + ">" },
						reader.getLineIndex(), reader.getCharIndex(), reader.getName()); //show that we shouldn't have found the end of the element TODO use a constant here
			}
		} catch(final ParseUnexpectedDataException parseUnexpectedDataException) { //if we run into data we don't recognize
			throw new XMLWellFormednessException(parseUnexpectedDataException); //create a well-formedness exception and throw it
		} catch(final ParseEOFException parseEOFException) { //if we run out of data
			throw new XMLWellFormednessException(parseEOFException, ResourceBundle.getBundle(XMLResources.RESOURCE_BUNDLE_BASE_NAME).getString(
					XMLResources.RESOURCE_PREFIX + DOCUMENT_RESOURCE_ID), document.getNodeName() != null ? document.getNodeName() : ResourceBundle.getBundle(
					XMLResources.RESOURCE_BUNDLE_BASE_NAME).getString(XMLResources.RESOURCE_PREFIX + UNKNOWN_RESOURCE_ID)); //show that we couldn't find the end of the document
		}
	}

	/**
	 * Parses an XML document, validates it if possible, and processes and applies any stylesheets.
	 * @throws IOException Thrown when an i/o error occurs.
	 * @throws XMLSyntaxException Thrown when there is a syntax error in the XML file.
	 * @throws XMLWellFormednessException Thrown when there is a well-formedness error in the XML file.
	 * @throws XMLValidityException Thrown when there is a validity error in the XML file.
	 * @throws XMLUndefinedEntityReferenceException Thrown if a named entity reference has not been defined.
	 * @return A new XML document.
	 */
	/*TODO fix
		public XMLDocument parseDocument() throws IOException, XMLSyntaxException, XMLWellFormednessException, XMLValidityException, XMLUndefinedEntityReferenceException
		{
			return parseDocument(true, true, true);	//parse the document, validate it, and process and apply any stylesheets
		}
	*/

	/**
	 * Resolves the namespace references in a document by iterating over each of its nodes and setting the namespace URIs and prefixes of the appropriate elements
	 * and attributes.
	 * @param xmlDocument The document for which namespaces should be processed.
	 */
	static void processNamespaces(final XMLDocument xmlDocument) {
		final Element documentElement = xmlDocument.getDocumentElement(); //get the root of the document
		processNamespaces((XMLElement)documentElement); //process the namespaces of the root element
	}

	/**
	 * Resolves the namespace references in this element and its children by setting the namespace URIs and prefixes of the appropriate elements and attributes.
	 * @param xmlElement The element for which namespaces should be processed and resolved.
	 * @throws DOMException <ul>
	 *           <li>NAMESPACE_ERR: Raised if the a <code>prefix</code> is malformed, if a <code>namespaceURI</code> is <code>null</code>, if a prefix is "xml"
	 *           and the <code>namespaceURI</code> of the node is different from "http://www.w3.org/XML/1998/namespace", if a node is an attribute and the
	 *           specified prefix is "xmlns" and the <code>namespaceURI</code> of this node is different from "http://www.w3.org/2000/xmlns/", or if a node is an
	 *           attribute and the <code>qualifiedName</code> of the node is "xmlns".</li>
	 *           </ul>
	 */
	protected static void processNamespaces(final XMLElement xmlElement) throws DOMException {
		//TODO del Log.trace("Processing namespaces for element: "+xmlElement.getNodeName());
		final XMLNamedNodeMap attributeXMLNamedNodeMap = (XMLNamedNodeMap)xmlElement.getAttributes(); //get the attributes
		final XMLNamedNodeMap newAttributeNamedNodeMap = new XMLNamedNodeMap(); //create a new named node map that will contain the new namespace-aware values
		//TODO del when works		xmlElement.setAttributeXMLNamedNodeMap(newAttributeNamedNodeMap); //now that we've processed the attributes, replace the old non-namespace-aware attrites with our new ones that recognize namespaces
		//Replace the old non-namespace-aware attrites with our new ones that
		//	recognize namespaces. Although we haven't yet populated the new list,
		//	it is important after we update the namespaces of the xmlns attributes
		//	that they be already present so that we can find namespaces for
		//	attributes that have their namespaces defined on the same element.
		xmlElement.setAttributeXMLNamedNodeMap(newAttributeNamedNodeMap); //start using our new attribute map right away; first fill it with updated xmlns attributes, then with all other attributes
		//first, resolve all the "xmlns:" and "xmlns" attributes to the predefined namespace "http://www.w3.org/2000/xmlns/"
		final Iterator xmlnsAttributeIterator = attributeXMLNamedNodeMap.values().iterator(); //create an iterator to iterate through the original attributes -- specifically, the "xmlns" and "xmlns:" attributes
		while(xmlnsAttributeIterator.hasNext()) { //while there are more attributes
			final XMLAttribute xmlAttribute = (XMLAttribute)xmlnsAttributeIterator.next(); //get the next attribute
			final String attributeName = xmlAttribute.getNodeName(); //get the attribute's name
			final String attributePrefix = getPrefix(attributeName); //get the prefix of the attribute, or null if there is no prefix
			//if this is the "xmlns" attribute, or the attribute has the the "xmlns:" prefix
			if((attributePrefix == null && attributeName.equals(XMLNS_NAMESPACE_PREFIX))
					|| (attributePrefix != null && attributePrefix.equals(XMLNS_NAMESPACE_PREFIX))) {
				final String attributeLocalName = getLocalName(attributeName); //get the local name encoded in the attribute name
				//TODO del				final String namespaceURI=XMLNode.getLocalName(attributeName);  //get the local name of this attribute, which is the namespace prefix being defined
				//TODO maybe combine all this into something that automatically updates attributes
				xmlAttribute.setNamespaceURI(XMLNS_NAMESPACE_URI.toString()); //attributes with an "xmlns:" prefix always have the "http://www.w3.org/2000/xmlns/" namespace URI
				xmlAttribute.setNodeName(attributePrefix, attributeLocalName); //set the prefix and the local name
				xmlnsAttributeIterator.remove(); //remove the attribute from the original map
				newAttributeNamedNodeMap.setNamedItemNS(xmlAttribute); //add the attribute to our new named node map, this time taking into account its namespace values
			}
		}
		//next, for all the other attributes besides "xmlns:", resolve the attribute to the correct namespace
		final Iterator attributeIterator = attributeXMLNamedNodeMap.values().iterator(); //get another iterator to look through each of the original attributes, minus the "xmlns" and "xmlns:" attributes we already examined and removed
		while(attributeIterator.hasNext()) { //while there are more attributes
			final XMLAttribute xmlAttribute = (XMLAttribute)attributeIterator.next(); //get the next attribute
			final String attributeName = xmlAttribute.getNodeName(); //get the attribute's name
			final String attributePrefix = getPrefix(attributeName); //get the prefix of the attribute, or null if there is no prefix
			final String attributeLocalName = getLocalName(attributeName); //get the local name encoded in the attribute name
			//if there is an attribute prefix, try to get the corresponding namespace URL TODO fix for blank URIs
			final String attributeNamespaceURI = attributePrefix != null ? getNamespaceURI(xmlElement, attributePrefix, true) : null;
			xmlAttribute.setNamespaceURI(attributeNamespaceURI); //set whatever namespace we found
			xmlAttribute.setNodeName(attributePrefix, attributeLocalName); //set the prefix and the local name
			newAttributeNamedNodeMap.setNamedItemNS(xmlAttribute); //add the attribute to our new named node map, this time taking into account its namespace values
		}
		//now process this element
		final String nodeName = xmlElement.getNodeName(); //get the node name
		final String nodePrefix = getPrefix(nodeName); //get the prefix, or null if there is no prefix
		final String nodeLocalName = getLocalName(nodeName); //get the local name encoded in the name

		//TODO del Log.trace("(ready to find namespace for node: "+nodeName+" with prefix: "+nodePrefix+" and local name: "+nodeLocalName+")");  //TODO del

		//get the namespace URI; if there is no prefix, this attempts to find a default namespace URI
		final String nodeNamespaceURI = getNamespaceURI(xmlElement, nodePrefix, true);
		//TODO del Log.trace("Found namespace URI: "+nodeNamespaceURI);  //TODO del
		//TODO del; we want to do this for all nodes, so they will give correct namespace values		if(nodeNamespaceURI!=null) //if there is a namespace URI found TODO is this what we want to do for null namespace URIs?
		xmlElement.setNamespaceURI(nodeNamespaceURI); //set whatever namespace we found
		xmlElement.setNodeName(nodePrefix, nodeLocalName); //set the prefix and the local name
		//TODO del if not needed		final NodeList childElementList=xmlElement.getElementsByTagName("*"); //get all child elements TODO is there a better way to do this?
		final NodeList childElementList = xmlElement.getChildNodes(); //get a list of the child nodes TODO see if we should use a tree walker or something
		for(int i = 0; i < childElementList.getLength(); ++i) { //look at each node
			final Node node = childElementList.item(i); //get a reference to this node
			if(node.getNodeType() == Node.ELEMENT_NODE) //if this is an element
				processNamespaces((XMLElement)node); //process the namespaces for this element
		}
	}

}
