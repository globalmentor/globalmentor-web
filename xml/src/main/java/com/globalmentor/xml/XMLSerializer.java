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

package com.globalmentor.xml;

import java.io.*;
import java.util.*;

import javax.annotation.*;

import java.nio.charset.Charset;

import com.globalmentor.io.ByteOrderMark;

import static com.globalmentor.java.CharSequences.*;
import static com.globalmentor.java.Characters.SPACE_CHAR;
import static com.globalmentor.java.Conditions.*;
import static com.globalmentor.java.Objects.*;

import com.globalmentor.java.Characters;

import static com.globalmentor.xml.XmlDom.*;
import static com.globalmentor.xml.spec.XML.*;
import static java.nio.charset.StandardCharsets.*;
import static java.util.Objects.*;

import com.globalmentor.util.PropertiesUtilities;

import org.w3c.dom.*;

/**
 * Serializes an XML document to a byte-oriented output stream. Has the option of automatically formatting the output in a hierarchical structure with tabs or
 * other strings.
 * @author Garret Wilson
 */
public class XMLSerializer {

	/** Whether the output should be formatted. */
	public static final String OPTION_FORMAT_OUTPUT = "formatOutput";

	/** Default to unformatted output. */
	public static final boolean OPTION_FORMAT_OUTPUT_DEFAULT = false;

	/** Whether Unicode control characters should be XML-encoded. */
	public static final String OPTION_XML_ENCODE_CONTROL = "xmlEncodeControl";

	/** Default to not XML-encoding control characters. */
	public static final boolean OPTION_XML_ENCODE_CONTROL_DEFAULT = false;

	/** Whether extended characters (above 127) should be XML-encoded. */
	public static final String OPTION_XML_ENCODE_NON_ASCII = "xmlEncodeNonASCII";

	/** Default to not XML-encoding extended characters. */
	public static final boolean OPTION_XML_ENCODE_NON_ASCII_DEFAULT = false;

	/** Whether private use Unicode characters should be XML-encoded. */
	public static final String OPTION_XML_ENCODE_PRIVATE_USE = "xmlEncodePrivateUse";

	/** Default to not XML-encoding private use characters. */
	public static final boolean OPTION_XML_ENCODE_PRIVATE_USE_DEFAULT = false;

	/** Whether characters that match a one-character defined entity should be encoded using that entity. */
	public static final String OPTION_USE_DEFINED_ENTITIES = "useDefinedEntities";

	/** Default to using one-character predefined entities for encoding. */
	public static final boolean OPTION_USE_DEFINED_ENTITIES_DEFAULT = true;

	/** Whether characters that match a one-character predefined entity should be encoded using that entity. */
	public enum PredefinedEntitiesUse {
		/** Predefined entities will be used whenever possible. */
		ALWAYS,
		/** Predefined entities will be used when a character needs to be encoded. */
		AS_NEEDED,
		/** Predefined entities will never be used; a character reference will be used when a character needs to be encoded. */
		NEVER;
	}

	/** Whether characters that match a one-character predefined entity should be encoded using that entity. */
	public static final String OPTION_USE_PREDEFINED_ENTITIES = "usePredefinedEntities";

	/** Default to using one-character predefined entities for encoding as needed. */
	public static final PredefinedEntitiesUse OPTION_USE_PREDEFINED_ENTITIES_DEFAULT = PredefinedEntitiesUse.AS_NEEDED;

	private boolean bomWritten = false;

	/**
	 * Returns whether the serializer will write a byte order mark (BOM).
	 * @return Whether a BOM is written.
	 */
	public boolean isBomWritten() {
		return bomWritten;
	}

	/**
	 * Whether a byte order mark (BOM) is written.
	 * @implSpec This option is disabled by default.
	 * @param bomWritten Whether a BOM is written.
	 */
	public void setBomWritten(final boolean bomWritten) {
		this.bomWritten = bomWritten;
	}

	private boolean prologWritten = true;

	/**
	 * Returns whether the serializer will write an XML prolog.
	 * @return Whether an XML prolog is written.
	 */
	public boolean isPrologWritten() {
		return prologWritten;
	}

	/**
	 * Whether an XML prolog is written.
	 * @implSpec This option is enabled by default.
	 * @param prologWritten Whether an XML prolog is written.
	 */
	public void setPrologWritten(final boolean prologWritten) {
		this.prologWritten = prologWritten;
	}

	private boolean formatted = OPTION_FORMAT_OUTPUT_DEFAULT;

	/** @return Whether the output should be formatted. */
	public boolean isFormatted() {
		return formatted;
	}

	/**
	 * Sets whether the output should be formatted.
	 * @implSpec This option defaults to {@value #OPTION_FORMAT_OUTPUT_DEFAULT}.
	 * @param newFormatted <code>true</code> if the output should be formatted, else <code>false</code>.
	 */
	public void setFormatted(final boolean newFormatted) {
		formatted = newFormatted;
	}

	private boolean xmlEncodeControl = OPTION_XML_ENCODE_CONTROL_DEFAULT;

	/**
	 * @return Whether Unicode control characters should be XML-encoded.
	 * @see #isUseDefinedEntities()
	 */
	public boolean isXMLEncodeControl() {
		return xmlEncodeControl;
	}

	/**
	 * Sets whether Unicode control characters should be XML-encoded.
	 * @implSpec This option defaults to {@value #OPTION_XML_ENCODE_CONTROL_DEFAULT}.
	 * @param newXMLEncodeControl <code>true</code> if control characters should be XML-encoded, else <code>false</code>.
	 * @see #setUseDefinedEntities(boolean)
	 */
	public void setXMLEncodeControl(final boolean newXMLEncodeControl) {
		xmlEncodeControl = newXMLEncodeControl;
	}

	private boolean xmlEncodeNonASCII = OPTION_XML_ENCODE_NON_ASCII_DEFAULT;

	/**
	 * @return Whether extended characters (above 127) should be XML-encoded.
	 * @see #isUseDefinedEntities()
	 */
	public boolean isXmlEncodeNonAscii() {
		return xmlEncodeNonASCII;
	}

	/**
	 * Sets whether extended characters (above 127) should be XML-encoded.
	 * @implSpec This option defaults to {@value #OPTION_XML_ENCODE_NON_ASCII_DEFAULT}.
	 * @param newXmlEncodeNonAscii <code>true</code> if extended characters should be XML-encoded, else <code>false</code>.
	 * @see #setUseDefinedEntities(boolean)
	 */
	public void setXMLEncodeNonASCII(final boolean newXmlEncodeNonAscii) {
		xmlEncodeNonASCII = newXmlEncodeNonAscii;
	}

	private boolean xmlEncodePrivateUse = OPTION_XML_ENCODE_PRIVATE_USE_DEFAULT;

	/**
	 * @return Whether private use Unicode characters should be XML-encoded.
	 * @see #isUseDefinedEntities()
	 */
	public boolean isXMLEncodePrivateUse() {
		return xmlEncodePrivateUse;
	}

	/**
	 * Sets whether private use Unicode characters should be XML-encoded.
	 * @implSpec This option defaults to {@value #OPTION_XML_ENCODE_PRIVATE_USE_DEFAULT}.
	 * @param newXmlEncodePrivateUse <code>true</code> if private use characters should be XML-encoded, else <code>false</code>.
	 * @see #setUseDefinedEntities(boolean)
	 */
	public void setXMLEncodePrivateUse(final boolean newXmlEncodePrivateUse) {
		xmlEncodePrivateUse = newXmlEncodePrivateUse;
	}

	private boolean useDefinedEntities = OPTION_USE_DEFINED_ENTITIES_DEFAULT;

	/**
	 * @return Whether one-character entities will be used when possible.
	 * @see #isXMLEncodeControl()
	 */
	public boolean isUseDefinedEntities() {
		return useDefinedEntities;
	}

	/**
	 * Sets whether characters that match a one-character entity should be encoded using that entity. This will override the XML-encoding settings when
	 * applicable.
	 * @implSpec This option defaults to {@value #OPTION_USE_DEFINED_ENTITIES_DEFAULT}.
	 * @param newUseDefinedEntities <code>true</code> if available entities should be used to encode characters.
	 * @see #setXMLEncodeControl(boolean)
	 */
	public void setUseDefinedEntities(final boolean newUseDefinedEntities) {
		useDefinedEntities = newUseDefinedEntities;
	}

	private PredefinedEntitiesUse usePredefinedEntities = OPTION_USE_PREDEFINED_ENTITIES_DEFAULT;

	/**
	 * @return Whether one-character entities will be used.
	 * @see #isXMLEncodeControl()
	 */
	public PredefinedEntitiesUse getUsePredefinedEntities() {
		return usePredefinedEntities;
	}

	/**
	 * Sets whether characters that match a one-character predefined entity should be encoded using that entity. This will override the XML-encoding settings when
	 * applicable.
	 * @implSpec This option defaults to {@link #OPTION_USE_PREDEFINED_ENTITIES_DEFAULT}.
	 * @param newUsePredefinedEntities <code>true</code> if predefined entities should be used to encode characters.
	 * @see #setXMLEncodeControl(boolean)
	 */
	public void setUsePredefinedEntities(@Nonnull final PredefinedEntitiesUse newUsePredefinedEntities) {
		usePredefinedEntities = requireNonNull(newUsePredefinedEntities);
	}

	private boolean namespacesDeclarationsEnsured = true;

	/** @return Whether missing namespaces declarations should be added. */
	public boolean isNamespacesDeclarationsEnsured() {
		return namespacesDeclarationsEnsured;
	}

	/**
	 * Sets whether missing namespace declarations are added.
	 * @implSpec This option is enabled by default.
	 * @param namespacesDeclarationsEnsured Whether missing namespaces declarations should be added.
	 */
	public void setNamespacesDeclarationsEnsured(final boolean namespacesDeclarationsEnsured) {
		this.namespacesDeclarationsEnsured = namespacesDeclarationsEnsured;
	}

	/** Whether missing namespaces declarations should be added to the document element if possible, rather than the top-level element needing the declaration. */
	private boolean namespacesDocumentElementDeclarations = true;

	/**
	 * @return Whether missing namespaces declarations should be added to the document element if possible, rather than the top-level element needing the
	 *         declaration.
	 */
	public boolean isNamespacesDocumentElementDeclarations() {
		return namespacesDocumentElementDeclarations;
	}

	/**
	 * Sets whether missing namespace declarations are added to the document element if possible, or to the top-level element needing the declaration.
	 * @implSpec This option is enabled by default.
	 * @param namespacesDocumentElementDeclarations Whether missing namespaces declarations should be added to the document element if possible, rather than the
	 *          top-level element needing the declaration.
	 */
	public void setNamespacesDocumentElementDeclarations(final boolean namespacesDocumentElementDeclarations) {
		this.namespacesDocumentElementDeclarations = namespacesDocumentElementDeclarations;
	}

	/**
	 * Sets the options based on the contents of the option properties.
	 * @param options The properties which contain the options.
	 * @deprecated Replace with Confound.
	 */
	@Deprecated
	public void setOptions(@Nonnull final Properties options) {
		setFormatted(PropertiesUtilities.getBooleanProperty(options, OPTION_FORMAT_OUTPUT, OPTION_FORMAT_OUTPUT_DEFAULT));
		setUseDefinedEntities(PropertiesUtilities.getBooleanProperty(options, OPTION_USE_DEFINED_ENTITIES, OPTION_USE_DEFINED_ENTITIES_DEFAULT));
		setUsePredefinedEntities(
				asInstance(options.getOrDefault(OPTION_USE_PREDEFINED_ENTITIES, OPTION_USE_PREDEFINED_ENTITIES_DEFAULT), PredefinedEntitiesUse.class)
						.orElse(OPTION_USE_PREDEFINED_ENTITIES_DEFAULT));
		setXMLEncodeControl(PropertiesUtilities.getBooleanProperty(options, OPTION_XML_ENCODE_CONTROL, OPTION_XML_ENCODE_CONTROL_DEFAULT));
		setXMLEncodeNonASCII(PropertiesUtilities.getBooleanProperty(options, OPTION_XML_ENCODE_NON_ASCII, OPTION_XML_ENCODE_NON_ASCII_DEFAULT));
		setXMLEncodePrivateUse(PropertiesUtilities.getBooleanProperty(options, OPTION_XML_ENCODE_PRIVATE_USE, OPTION_XML_ENCODE_PRIVATE_USE_DEFAULT));
	}

	private String horizontalAlignString = "\t";

	/**
	 * @return The string to use for horizontally aligning the elements if formatting is turned on.
	 * @see #isFormatted()
	 */
	public String getHorizontalAlignString() {
		return horizontalAlignString;
	}

	/**
	 * Sets the string to use for horizontally aligning the elements if formatting is turned on..
	 * @implSpec The default is a single tab character.
	 * @param newHorizontalAlignString The horizontal alignment string.
	 * @see #setFormatted(boolean)
	 */
	public void setHorizontalAlignString(final String newHorizontalAlignString) {
		horizontalAlignString = newHorizontalAlignString;
	}

	private String lineSeparator = System.lineSeparator();

	/**
	 * Returns the character or characters to use to separate lines; that is, for newlines or line breaks.
	 * @return The newline delimiter to use.
	 * @see System#lineSeparator()
	 */
	public String getLineSeparator() {
		return lineSeparator;
	}

	/**
	 * Sets the character or characters to use to separate lines; that is, for newlines or line breaks.
	 * @implSpec The default is the system line separator {@link System#lineSeparator()}.
	 * @param lineSeparator The newline delimiter to use.
	 * @see System#lineSeparator()
	 */
	public void setLineSeparator(@Nonnull final String lineSeparator) {
		this.lineSeparator = requireNonNull(lineSeparator);
	}

	/** The current level of nesting during document serialization if output is formatted. */
	protected int nestLevel = -1;

	/**
	 * The string representing one-character entity values defined in the document. The {@link #entityNames} array will contain the names of the corresponding
	 * entities, each at the same index as the corresponding character value.
	 * @see #entityNames
	 */
	private String entityCharacterValues;

	/**
	 * An array of names corresponding to character values in the <code>entityCharacterValue</code> string at the same indexes.
	 * @see #entityCharacterValues
	 */
	private String[] entityNames;

	/** Default constructor for unformatted output. */
	public XMLSerializer() {
		this(false);
	}

	/**
	 * Constructor that specifies serialize options.
	 * @param options The options to use for serialization.
	 */
	public XMLSerializer(final Properties options) {
		this(false); //do the default construction
		setOptions(options); //set the options from the properties
	}

	/**
	 * Constructor for an optionally formatted serializer using the {@link XmlFormatProfile#DEFAULT} format profile.
	 * @param formatted Whether the serializer should be formatted.
	 */
	public XMLSerializer(final boolean formatted) {
		this(formatted, XmlFormatProfile.DEFAULT);
	}

	private final XmlFormatProfile formatProfile;

	/** @return The characterization of the content for formatting purposes. */
	protected XmlFormatProfile getFormatProfile() {
		return formatProfile;
	}

	/**
	 * Constructor for an optionally formatted serializer.
	 * @param formatted Whether the serializer should be formatted.
	 * @param formatProfile The profile to use to guide formatting.
	 */
	public XMLSerializer(final boolean formatted, @Nonnull final XmlFormatProfile formatProfile) {
		initializeEntityLookup(null); //always initialize the entity lookup, so that at least the five XML entities will be included in the table in case they serialize only part of a document TODO fix better so that serializing part of the document somehow initializes these
		setFormatted(formatted); //set whether the output should be formatted
		this.formatProfile = requireNonNull(formatProfile);
	}

	/**
	 * Serializes the specified document to a string using the UTF-8 encoding with no byte order mark.
	 * @param document The XML document to serialize.
	 * @return A string containing the serialized XML data.
	 * @throws IOException Thrown if an I/O error occurred.
	 * @throws UnsupportedEncodingException Thrown if the UTF-8 encoding not recognized.
	 */
	public String serialize(@Nonnull final Document document) throws UnsupportedEncodingException, IOException {
		try {
			final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(); //create an output stream for receiving the XML data
			serialize(document, byteArrayOutputStream, UTF_8); //serialize the document to the output stream using UTF-8 with no byte order mark
			return byteArrayOutputStream.toString(UTF_8); //convert the byte array to a string, using the UTF-8 encoding, and return it
		} catch(final UnsupportedEncodingException unsupportedEncodingException) { //UTF-8 should always be supported
			throw new AssertionError(unsupportedEncodingException);
		} catch(final IOException ioException) { //there should never by an I/O exception writing to a byte array output stream
			throw new AssertionError(ioException);
		}
	}

	/**
	 * Serializes the specified document fragment to a string using the UTF-8 encoding with no byte order mark.
	 * @param documentFragment The XML document fragment to serialize.
	 * @return A string containing the serialized XML data.
	 */
	public String serialize(@Nonnull final DocumentFragment documentFragment) {
		try {
			final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(); //create an output stream for receiving the XML data
			serialize(documentFragment, byteArrayOutputStream, UTF_8); //serialize the document fragment to the output stream using UTF-8 with no byte order mark
			return byteArrayOutputStream.toString(UTF_8); //convert the byte array to a string, using the UTF-8 encoding, and return it
		} catch(final UnsupportedEncodingException unsupportedEncodingException) { //UTF-8 should always be supported
			throw new AssertionError(unsupportedEncodingException);
		} catch(final IOException ioException) { //there should never by an I/O exception writing to a byte array output stream
			throw new AssertionError(ioException);
		}
	}

	/**
	 * Serializes the specified element to a string using the UTF-8 encoding with no byte order mark.
	 * @param element The XML element to serialize.
	 * @return A string containing the serialized XML data.
	 */
	public String serialize(@Nonnull final Element element) {
		try {
			final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(); //create an output stream for receiving the XML data
			serialize(element, byteArrayOutputStream, UTF_8); //serialize the element to the output stream using UTF-8 with no byte order mark
			return byteArrayOutputStream.toString(UTF_8); //convert the byte array to a string, using the UTF-8 encoding, and return it
		} catch(final UnsupportedEncodingException unsupportedEncodingException) { //UTF-8 should always be supported
			throw new AssertionError(unsupportedEncodingException);
		} catch(final IOException ioException) { //there should never by an I/O exception writing to a byte array output stream
			throw new AssertionError(ioException);
		}
	}

	/**
	 * Serializes the content (all child nodes and their descendants) of a specified node to a string using the UTF-8 encoding with no byte order mark.
	 * @param node The XML node the content of which to serialize—usually an element or document fragment.
	 * @return A string containing the serialized XML data.
	 */
	public String serializeContent(@Nonnull final Node node) {
		try {
			final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(); //create an output stream for receiving the XML data
			serializeContent(node, byteArrayOutputStream, UTF_8); //serialize the node content to the output stream using UTF-8 with no byte order mark
			return byteArrayOutputStream.toString(UTF_8); //convert the byte array to a string, using the UTF-8 encoding, and return it
		} catch(final UnsupportedEncodingException unsupportedEncodingException) { //UTF-8 should always be supported
			throw new AssertionError(unsupportedEncodingException);
		} catch(final IOException ioException) { //there should never by an I/O exception writing to a byte array output stream
			throw new AssertionError(ioException);
		}
	}

	/**
	 * Serializes the specified document to the given output stream using the UTF-8 encoding with the UTF-8 byte order mark.
	 * @param document The XML document to serialize.
	 * @param outputStream The stream into which the document should be serialized.
	 * @throws IOException Thrown if an I/O error occurred.
	 */
	public void serialize(@Nonnull final Document document, @Nonnull final OutputStream outputStream) throws IOException {
		serialize(document, outputStream, UTF_8); //serialize the document, defaulting to UTF-8
	}

	/**
	 * Serializes the specified document to the given output stream using the specified encoding. Any byte order mark specified in the character encoding will be
	 * written to the stream.
	 * @param document The XML document to serialize.
	 * @param outputStream The stream into which the document should be serialized.
	 * @param charset The character set to use when serializing.
	 * @throws IOException Thrown if an I/O error occurred.
	 * @throws UnsupportedEncodingException Thrown if the specified encoding is not recognized.
	 */
	public void serialize(@Nonnull final Document document, @Nonnull final OutputStream outputStream, @Nonnull final Charset charset)
			throws IOException, UnsupportedEncodingException {
		nestLevel = 0; //show that we haven't started nesting yet
		if(isBomWritten()) { //if we should write a BOM
			final ByteOrderMark bom = ByteOrderMark.forCharset(charset); //get the byte order mark, if there is one
			if(bom != null) {
				outputStream.write(bom.getBytes()); //write the byte order mark
			}
		}
		final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, charset)); //create a new writer based on our encoding TODO see if the writer automatically writes the byte order mark already for non-UTF-8
		if(isPrologWritten()) {
			serializeProlog(writer, document, charset); //write the prolog
		}
		final DocumentType documentType = document.getDoctype(); //get the document type, if there is one
		if(documentType != null) { //if there is a document type
			initializeEntityLookup(documentType.getEntities()); //initialize the entity lookup based on the provided entities
			serialize(writer, documentType); //write the document type
		} else { //if there is no document type
			initializeEntityLookup(null); //always initialize the entity lookup, so that at least the five XML entities will be included in the table
		}
		serializeProcessingInstructions(writer, document); //write any processing instructions
		final Element documentElement = document.getDocumentElement(); //get the document element
		if(isNamespacesDeclarationsEnsured()) { //if we should ensure namespaces
			if(isNamespacesDocumentElementDeclarations()) { //if missing namespaces should be declared on the document element, process the entire document before writing
				ensureNamespaceDeclarations(documentElement, documentElement, true); //make sure all namespaces are declared that all elements need (i.e. deep), declaring any missing elements on the document element
			}
		}
		serialize(writer, documentElement); //write the document element and all elements below it
		if(isFormatted()) { //if we should write formatted output
			writer.append(getLineSeparator()); //newline
		}
		writer.flush(); //flush any data we've buffered
	}

	/**
	 * Serializes the specified document fragment to the given output stream using the UTF-8 encoding with the UTF-8 byte order mark.
	 * @param documentFragment The XML document fragment to serialize.
	 * @param outputStream The stream into which the document fragment should be serialized.
	 * @throws IOException Thrown if an I/O error occurred.
	 * @throws UnsupportedEncodingException Thrown if the UTF-8 encoding not recognized.
	 */
	public void serialize(@Nonnull final DocumentFragment documentFragment, @Nonnull final OutputStream outputStream)
			throws UnsupportedEncodingException, IOException {
		serialize(documentFragment, outputStream, UTF_8); //serialize the document, defaulting to UTF-8
	}

	/**
	 * Serializes the specified document fragment to the given output stream using the specified encoding. Any byte order mark specified in the character encoding
	 * will be written to the stream.
	 * @param documentFragment The XML document fragment to serialize.
	 * @param outputStream The stream into which the document fragment should be serialized.
	 * @param charset The charset to use when serializing.
	 * @throws IOException Thrown if an I/O error occurred.
	 * @throws UnsupportedEncodingException Thrown if the specified encoding is not recognized.
	 */
	public void serialize(@Nonnull final DocumentFragment documentFragment, @Nonnull final OutputStream outputStream, @Nonnull final Charset charset)
			throws IOException, UnsupportedEncodingException {
		serializeContent(documentFragment, outputStream, charset); //serialize the content of the document fragment		
	}

	/**
	 * Serializes the specified element and its children to the given output stream using the UTF-8 encoding with the UTF-8 byte order mark.
	 * @param element The XML element to serialize.
	 * @param outputStream The stream into which the element should be serialized.
	 * @throws IOException Thrown if an I/O error occurred.
	 * @throws UnsupportedEncodingException Thrown if the UTF-8 encoding is not recognized.
	 */
	public void serialize(@Nonnull final Element element, @Nonnull final OutputStream outputStream) throws IOException, UnsupportedEncodingException {
		serialize(element, outputStream, UTF_8); //serialize the element using UTF-8
	}

	/**
	 * Serializes the specified element and its children to the given output stream using the specified encoding. Any byte order mark specified in the character
	 * encoding will be written to the stream.
	 * @param element The XML element to serialize.
	 * @param outputStream The stream into which the element should be serialized.
	 * @param charset The charset to use when serializing.
	 * @throws IOException Thrown if an I/O error occurred.
	 * @throws UnsupportedEncodingException Thrown if the specified encoding is not recognized.
	 */
	public void serialize(@Nonnull final Element element, @Nonnull final OutputStream outputStream, @Nonnull final Charset charset)
			throws IOException, UnsupportedEncodingException {
		nestLevel = 0; //show that we haven't started nesting yet
		if(isBomWritten()) { //if we should write a BOM
			final ByteOrderMark bom = ByteOrderMark.forCharset(charset); //get the byte order mark, if there is one
			if(bom != null) {
				outputStream.write(bom.getBytes()); //write the byte order mark
			}
		}
		final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, charset)); //create a new writer based on our encoding TODO see if the writer automatically writes the byte order mark already for non-UTF-8
		serialize(writer, element); //write the element and all elements below it
		if(isFormatted()) { //if we should write formatted output
			writer.append(getLineSeparator()); //newline
		}
		writer.flush(); //flush any data we've buffered
	}

	/**
	 * Serializes the content (all child nodes and their descendants) of a specified node to the given output stream using the UTF-8 encoding with the UTF-8 byte
	 * order mark.
	 * @param node The XML node the content of which to serialize—usually an element or document fragment.
	 * @param outputStream The stream into which the element content should be serialized.
	 * @throws IOException Thrown if an I/O error occurred.
	 * @throws UnsupportedEncodingException Thrown if the UTF-8 encoding is not recognized.
	 */
	protected void serializeContent(@Nonnull final Node node, @Nonnull final OutputStream outputStream) throws IOException, UnsupportedEncodingException {
		serializeContent(node, outputStream, UTF_8); //serialize the content UTF-8
	}

	/**
	 * Serializes the content (all child nodes and their descendants) of a specified node to the given output stream using the specified encoding. Any byte order
	 * mark specified in the character encoding will be written to the stream.
	 * @param node The XML node the content of which to serialize—usually an element or document fragment.
	 * @param outputStream The stream into which the element content should be serialized.
	 * @param charset The charset to use when serializing.
	 * @throws IOException Thrown if an I/O error occurred.
	 * @throws UnsupportedEncodingException Thrown if the specified encoding is not recognized.
	 */
	protected void serializeContent(@Nonnull final Node node, @Nonnull final OutputStream outputStream, @Nonnull final Charset charset)
			throws IOException, UnsupportedEncodingException {
		nestLevel = 0; //show that we haven't started nesting yet
		if(isBomWritten()) { //if we should write a BOM
			final ByteOrderMark bom = ByteOrderMark.forCharset(charset); //get the byte order mark, if there is one
			if(bom != null) {
				outputStream.write(bom.getBytes()); //write the byte order mark
			}
		}
		final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, charset)); //create a new writer based on our encoding TODO see if the writer automatically writes the byte order mark already for non-UTF-8
		serializeContent(writer, node); //write all children of the node
		if(isFormatted()) { //if we should write formatted output
			writer.append(getLineSeparator()); //newline
		}
		writer.flush(); //flush any data we've buffered
	}

	/**
	 * Initializes the internal entity lookup with the entities defined in the specified map. The given entities will only be placed in the lookup table if the
	 * "useDefinedEntities" option is turned on, and then only entities which represent one-character entities will be used.
	 * @param entityMap The entity map which contains the entities to be placed in the internal lookup table, or <code>null</code> if entities are not available,
	 *          in which case only the default XML entities will be used.
	 * @throws IllegalArgumentException if a predefined entity was overridden using a different value.
	 * @see #isUseDefinedEntities()
	 * @see #setUseDefinedEntities(boolean)
	 */
	protected void initializeEntityLookup(@Nonnull final NamedNodeMap entityMap) {
		final List<String> entityNameList = new ArrayList<String>(); //create an array to hold the entity names we use
		final StringBuilder entityCharacterValueStringBuilder = new StringBuilder(); //create a buffer to hold all the character values
		if(isUseDefinedEntities()) { //if we were asked to use their entities
			if(entityMap != null) { //if we were provided a valid entity map
				final int entityCount = entityMap.getLength(); //find out how many entities there are
				for(int i = 0; i < entityCount; ++i) { //look at each of the entities
					final Entity entity = (Entity)entityMap.item(i); //get a reference to this entity
					final int entityChildCount = entity.getChildNodes().getLength(); //see how many children this entity has (entities store text content in child text nodes not in the entity value)
					if(entityChildCount == 1) { //if there is only one child node
						final Node entityChildNode = entity.getFirstChild(); //get a reference to the first child of the entity
						if(entityChildNode.getNodeType() == Node.TEXT_NODE) { //if this is a text node
							final String entityValue = ((Text)entityChildNode).getData(); //get the data of the node, which represents the replacement value of the entity
							if(entityValue.length() == 1) { //if this entity represents exactly one character
								final char c = entityValue.charAt(0);
								final String entityName = entity.getNodeName();
								//if they defined a predefined entity, make sure they didn't redefine it to something else
								checkArgument(!PREDEFINED_ENTITY_CHARACTERS.contains(c) || getPredefinedEntityName(c).equals(entityName));
								entityNameList.add(entityName); //add the entity's name to the list
								entityCharacterValueStringBuilder.append(c); //add the one-character value to the value buffer
							}
						}
					}
				}
			}
		}
		entityNames = entityNameList.toArray(new String[entityNameList.size()]); //convert the entities in the list to an array
		entityCharacterValues = entityCharacterValueStringBuilder.toString(); //convert the values into one searchable string
	}

	/**
	 * Serializes the specified documents to the given writer.
	 * @param appendable The destination into which the prolog should be written.
	 * @param document The XML document the prolog of which to serialize.
	 * @param charset The charset in use.
	 * @return The given appendable.
	 * @throws IOException Thrown if an I/O error occurred.
	 */
	protected Appendable serializeProlog(@Nonnull final Appendable appendable, @Nonnull final Document document, @Nonnull final Charset charset)
			throws IOException {
		appendable.append(XML_DECL_START).append(SPACE_CHAR).append(VERSIONINFO_NAME).append(EQUAL_CHAR).append(DOUBLE_QUOTE_CHAR).append(XML_VERSION)
				.append(DOUBLE_QUOTE_CHAR).append(SPACE_CHAR).append(ENCODINGDECL_NAME).append(EQUAL_CHAR).append(DOUBLE_QUOTE_CHAR).append(charset.name())
				.append(DOUBLE_QUOTE_CHAR).append(XML_DECL_END); //write the XML prolog
		if(isFormatted()) { //if we should write formatted output
			appendable.append(getLineSeparator()); //newline
		}
		return appendable;
	}

	/**
	 * Serializes the document's processing instructions to the given writer.
	 * @param appendable The destination into which the processing instructions should be written.
	 * @param document The XML document the processing instructions of which to serialize.
	 * @return The given appendable.
	 * @throws IOException Thrown if an I/O error occurred.
	 */
	protected Appendable serializeProcessingInstructions(@Nonnull final Appendable appendable, @Nonnull final Document document) throws IOException {
		final NodeList childNodeList = document.getChildNodes(); //get the list of document child nodes
		final int childNodeCount = childNodeList.getLength(); //find out how many document child nodes there are
		for(int childIndex = 0; childIndex < childNodeCount; childIndex++) { //look at each document child node
			final Node node = childNodeList.item(childIndex); //look at this document child node
			if(node.getNodeType() == Node.PROCESSING_INSTRUCTION_NODE) //if this is a processing instruction
				serialize(appendable, (ProcessingInstruction)node); //write the processing instruction node
		}
		return appendable;
	}

	/**
	 * Serializes the document type to the given writer.
	 * @param appendable The destination into which the document type should be written.
	 * @param documentType The XML document type to serialize.
	 * @return The given appendable.
	 * @throws IOException Thrown if an I/O error occurred.
	 */
	protected Appendable serialize(@Nonnull final Appendable appendable, @Nonnull final DocumentType documentType) throws IOException {
		appendable.append(DOCTYPE_DECL_START).append(SPACE_CHAR).append(documentType.getName()); //write the beginning of the document type declaration
		final String publicID = documentType.getPublicId(); //get the public ID, if there is one
		final String systemID = documentType.getSystemId(); //get the system ID, if there is one 
		if(publicID != null || systemID != null) { //if there is a public ID or a system ID
			appendable.append(SPACE_CHAR); //separate the identifiers from the doctype introduction
			if(publicID != null) { //if there is a public ID
				appendable.append(PUBLIC_ID_NAME).append(SPACE_CHAR).append(DOUBLE_QUOTE_CHAR).append(publicID).append(DOUBLE_QUOTE_CHAR).append(SPACE_CHAR); //write the public ID name and its public literal
				assert systemID != null : "A system ID is expected following a public ID.";
			} else { //if there is no public ID
				appendable.append(SYSTEM_ID_NAME).append(SPACE_CHAR); //write the system ID name
			}
			appendable.append(DOUBLE_QUOTE_CHAR).append(documentType.getSystemId()).append(DOUBLE_QUOTE_CHAR); //always write the system literal
		}
		appendable.append(DOCTYPE_DECL_END); //write the end of the document type declaration
		if(isFormatted()) { //if we should write formatted output
			appendable.append(getLineSeparator()); //newline
		}
		return appendable;
	}

	/**
	 * Serializes the given processing instruction to the given writer.
	 * @param appendable The destination into which the processing instruction should be written.
	 * @param processingInstruction The XML processing instruction to serialize.
	 * @return The given appendable.
	 * @throws IOException Thrown if an I/O error occurred.
	 */
	protected Appendable serialize(@Nonnull final Appendable appendable, @Nonnull final ProcessingInstruction processingInstruction) throws IOException {
		//write the beginning of the processing instruction: "<?target "
		appendable.append(PROCESSING_INSTRUCTION_START).append(processingInstruction.getTarget()).append(SPACE_CHAR);
		appendable.append(processingInstruction.getData()); //write the processing instruction data
		appendable.append(PROCESSING_INSTRUCTION_END); //write the end of the processing instruction
		if(isFormatted()) { //if we should write formatted output
			appendable.append(getLineSeparator()); //newline
		}
		return appendable;
	}

	/**
	 * Serializes the specified element to the given writer, using the default formatting options.
	 * @param appendable The destination into which the element should be written.
	 * @param element The XML element to serialize.
	 * @return The given appendable.
	 * @throws IOException Thrown if an I/O error occurred.
	 */
	protected Appendable serialize(@Nonnull final Appendable appendable, @Nonnull final Element element) throws IOException {
		return serialize(appendable, element, isFormatted()); //write the element using the default formatting options
	}

	/**
	 * Serializes the specified element to the given writer.
	 * @param appendable The destination into which the element should be written.
	 * @param element The XML element to serialize.
	 * @param formatted Whether this element and its contents, including any child elements, should be formatted.
	 * @return The given appendable.
	 * @throws IOException Thrown if an I/O error occurred.
	 */
	protected Appendable serialize(@Nonnull final Appendable appendable, @Nonnull final Element element, @Nonnull boolean formatted) throws IOException {
		if(formatted) { //if we should write formatted output
			serializeHorizontalAlignment(appendable, nestLevel); //horizontally align the element
		}
		appendable.append(TAG_START).append(element.getNodeName()); //write the beginning of the start tag
		if(isNamespacesDeclarationsEnsured()) { //if we should ensure namespaces
			ensureNamespaceDeclarations(element, null, false); //make sure all namespaces are declared that just this element needs; if any are missing, we can't declare up the tree, as those nodes have already been serialized
		}
		/*TODO fix; this correctly doesn't add namespaces, to the tree itself, but not doing so means that the namespaces will just get added again lower down in the hierarchy
					//get the undeclared namespaces for this element and write them before the normal attributes are written
				final NameValuePair[] prefixNamespacePairs=XMLNamespaceProcessor.getUndeclaredNamespaces(element);
				for(int i=0; i<prefixNamespacePairs.length; ++i) {	//look at each name/value pair
					final NameValuePair prefixNamespacePair=prefixNamespacePairs[i];	//get this name/value pair representing a prefix and a namespace
					final String prefix=(String)prefixNamespacePair.getName();	//get the prefix to use
							//get the namespace URI to use (using "" if no namespace is intended)
					final String namespaceURI=prefixNamespacePair.getValue()!=null ? (String)prefixNamespacePair.getValue() : "";
							//see if the attribute should be in the form xmlns:prefix="namespaceURI" or xmlns="namespaceURI" TODO fix for attributes that may use the same prefix for different namespace URIs
					final String attributeName=prefix!=null ? createQualifiedName(XMLNS_NAMESPACE_PREFIX, prefix) : XMLNS_NAMESPACE_URI.toString();
					writeAttribute(attributeName, namespaceURI, writer);	//write this namespace attribute attribute
				}
		*/
		for(int attributeIndex = 0; attributeIndex < element.getAttributes().getLength(); ++attributeIndex) { //look at each attribute
			final Attr attribute = (Attr)element.getAttributes().item(attributeIndex); //get a reference to this attribute
			serializeAttribute(appendable, attribute.getName(), attribute.getValue()); //write this attribute
		}
		if(isEmptyElementTag(element)) { //if we should serialize the element as an empty element tag, e.g. <foo />
			appendable.append(SPACE_CHAR).append('/').append(TAG_END); //write the end of the empty element tag, with an extra space for HTML browser compatibility TODO use a constant here
		} else {
			appendable.append(TAG_END); //write the end of the start tag
			/*TODO fix; update for formatting overhaul
				boolean contentFormatted = false; //we'll determine if we should format the content of the child nodes
				//we'll only format the contents if there are only element children
				if(formatted) { //if we've been told to format, we'll make sure there are element child nodes
					for(int childIndex = 0; childIndex < element.getChildNodes().getLength(); childIndex++) { //look at each child node
						final int childNodeType = element.getChildNodes().item(childIndex).getNodeType(); //get this child's type of node
						if(childNodeType == Node.ELEMENT_NODE) { //if this is an element child node
							contentFormatted = true; //show that we should format the element content
						} else if(childNodeType == Node.TEXT_NODE) { //if this is text
							contentFormatted = false; //text or mixed content is always unformatted
							break; //we know we shouldn't format; stop looking at the children
						}
					}
				}
			*/
			/*TODO move to formatting of children
			if(isFormatted()) { //if we should write formatted output for the content
				appendable.append(getLineSeparator()); //add a newline after the element's starting tag
			}
			*/
			++nestLevel; //show that we're nesting to the next level
			serializeContent(appendable, element, isFormatted()); //TODO do we still need to indicate formatted setting after formatting overhaul?
			--nestLevel; //show that we're finished with this level of the document hierarchy
			/*TODO move to formatting of children
			if(isFormatted()) { //if we should write formatted output for the content
				serializeHorizontalAlignment(appendable, nestLevel); //horizontally align the element's ending tag
			}
			*/
			appendable.append(TAG_START).append('/').append(element.getNodeName()).append(TAG_END); //write the ending tag TODO use a constant here
		}
		if(formatted) { //if we should write formatted output
			appendable.append(getLineSeparator()); //add a newline after the element
		}
		return appendable;
	}

	/**
	 * Indicates whether the given element should be serialized as an empty element tag.
	 * @implSpec The default implementation returns <code>false</code> if the given element has any children.
	 * @param element The element being serialized.
	 * @return <code>true</code> if the XML empty-element tag form should be used to serialize the tag.
	 * @see <a href="https://www.w3.org/TR/xml/#sec-starttags">Extensible Markup Language (XML) 1.0 (Fifth Edition) § 3.1 Start-Tags, End-Tags, and Empty-Element
	 *      Tags</a>
	 */
	protected boolean isEmptyElementTag(@Nonnull final Element element) {
		return element.getChildNodes().getLength() == 0;
	}

	/**
	 * Serializes the specified attribute name and value to the given writer.
	 * @param appendable The destination into which the attribute should be written.
	 * @param attributeName The name of the XML attribute to serialize.
	 * @param attributeValue The name of the XML attribute to serialize.
	 * @return The given appendable.
	 * @throws IOException Thrown if an I/O error occurred.
	 */
	protected Appendable serializeAttribute(@Nonnull final Appendable appendable, @Nonnull final String attributeName, @Nonnull final String attributeValue)
			throws IOException {
		//use a double quote character as a delimiter unless the value contains
		//  a double quote; in that case, use a single quote TODO fix escaping---what if both double and single quotes are used?
		final char valueDelimiter = attributeValue.indexOf(DOUBLE_QUOTE_CHAR) < 0 ? DOUBLE_QUOTE_CHAR : SINGLE_QUOTE_CHAR;
		//write the attribute and its value after encoding it for XML; pass the delimiter in case this value has both a single and a double quote
		appendable.append(SPACE_CHAR).append(attributeName).append(EQUAL_CHAR).append(valueDelimiter);
		encodeContent(appendable, attributeValue, valueDelimiter);
		appendable.append(valueDelimiter);
		return appendable;
	}

	/**
	 * Serializes the content of the specified node to the given writer using the default formatting options.
	 * @param appendable The destination into which the element content should be written.
	 * @param node The XML node the content of which to serialize—usually an element or document fragment.
	 * @return The given appendable.
	 * @throws IOException Thrown if an I/O error occurred.
	 */
	protected Appendable serializeContent(@Nonnull final Appendable appendable, @Nonnull final Node node) throws IOException {
		return serializeContent(appendable, node, isFormatted()); //write the content using the default formatting options
	}

	/**
	 * Serializes the content of the specified node to the given writer.
	 * @param appendable The destination into which the element content should be written.
	 * @param node The XML node the content of which to serialize—usually an element or document fragment.
	 * @param formatted Whether the contents of this element, including any child elements, should be formatted.
	 * @return The given appendable.
	 * @throws IOException Thrown if an I/O error occurred.
	 */
	protected Appendable serializeContent(@Nonnull final Appendable appendable, @Nonnull final Node node, final boolean formatted) throws IOException {
		for(int childIndex = 0; childIndex < node.getChildNodes().getLength(); childIndex++) { //look at each child node
			final Node childNode = node.getChildNodes().item(childIndex); //look at this node
			switch(childNode.getNodeType()) { //see which type of object this is
				case Node.ELEMENT_NODE: //if this is an element
					serialize(appendable, (Element)childNode, formatted); //write this element
					break;
				case Node.TEXT_NODE: //if this is a text node
				{
					final String textNodeValue = childNode.getNodeValue();
					final CharSequence text;
					if(formatted) {
						//TODO check formatting profile to see if this a context that allows formatting
						text = collapseRuns(textNodeValue, getFormatProfile().getSpaceNormalizationCharacters(), SPACE_CHAR, false, false); //TODO determine whether to trim based upon context
					} else {
						text = textNodeValue;
					}
					if(isChildTextEncoded(node)) {
						encodeContent(appendable, text); //write the text value of the node after encoding the string for XML
					} else {
						appendable.append(text); //write the text value of the node without encoding
					}
				}
					break;
				case Node.COMMENT_NODE: //if this is a comment node
					if(formatted) { //if we should write formatted output
						serializeHorizontalAlignment(appendable, nestLevel); //horizontally align the element
					}
					appendable.append(COMMENT_START); //write the start of the comment
					//TODO check content for disallowed sequence
					appendable.append(childNode.getNodeValue()); //write the text value of the node, but don't encode the string for XML since it's inside a comment
					appendable.append(COMMENT_END); //write the end of the comment
					if(formatted) { //if we should write formatted output
						appendable.append(getLineSeparator()); //add a newline after the comment
					}
					break;
				case Node.CDATA_SECTION_NODE: //if this is a CDATA section node
					appendable.append(CDATA_START); //write the start of the CDATA section
					//TODO check content for disallowed sequence
					appendable.append(childNode.getNodeValue()); //write the text value of the node, but don't encode the string for XML since it's inside a CDATA section
					appendable.append(CDATA_END); //write the end of the CDATA section
					break;
				//TODO see if there are any other types of nodes that need serialized
			}
		}
		return appendable;
	}

	/**
	 * Determines whether content of child text nodes of a given parent node should be encoded.
	 * @implSpec The default implementation returns <code>true</code> for all parent nodes.
	 * @param parentNode The parent node which has child text nodes.
	 * @return <code>true</code> if immediate text content of the given parent node should be encoded.
	 */
	protected boolean isChildTextEncoded(@Nonnull final Node parentNode) {
		return true;
	}

	/**
	 * Creates the specified number of horizontal alignment strings.
	 * @param appendable The destination into which the element should be written.
	 * @param nestLevel The level of nesting for horizontal alignment.
	 * @return The given appendable.
	 * @throws IOException Thrown if an I/O error occurred.
	 */
	protected Appendable serializeHorizontalAlignment(@Nonnull final Appendable appendable, int nestLevel) throws IOException {
		while(nestLevel > 0) { //while we haven't finished nesting
			appendable.append(getHorizontalAlignString()); //write another string for horizontal alignment
			--nestLevel; //show that we have one less level to next
		}
		return appendable;
	}

	/**
	 * Encodes content using the available entities and/or XML encoding for extended characters.
	 * @param appendable The destination into which the encoded content should be written.
	 * @param text The text to encode.
	 * @return The given appendable.
	 * @throws IOException Thrown if an I/O error occurred.
	 * @see #isUseDefinedEntities()
	 * @see #isXMLEncodeControl()
	 * @see #isXmlEncodeNonAscii()
	 * @see #isXMLEncodePrivateUse()
	 */
	protected Appendable encodeContent(@Nonnull final Appendable appendable, @Nonnull final CharSequence text) throws IOException {
		return encodeContent(appendable, text, (char)0); //show that there is no delimiter character
	}

	/**
	 * Encodes content using the available entities and/or XML encoding for extended characters. If the delimiter character is not 0, it is unconditionally
	 * encoded.
	 * @param appendable The destination into which the encoded content should be written.
	 * @param text The text to encode.
	 * @param delimiter The character which should always be encoded, such as the delimiter character for attributes ('"' or '\''), or 0 if there is no delimiter.
	 * @return The given appendable.
	 * @throws IOException Thrown if an I/O error occurred.
	 * @see #isUseDefinedEntities()
	 * @see #getUsePredefinedEntities()
	 * @see #isXMLEncodeControl()
	 * @see #isXmlEncodeNonAscii()
	 * @see #isXMLEncodePrivateUse()
	 */
	protected Appendable encodeContent(@Nonnull final Appendable appendable, @Nonnull final CharSequence text, final char delimiter) throws IOException {
		final boolean xmlEncodeControl = isXMLEncodeControl(); //see if we should XML-encode control characters
		final boolean xmlEncodeNonASCII = isXmlEncodeNonAscii(); //see if we should XML-encode characters over 127
		final boolean xmlEncodePrivateUse = isXMLEncodePrivateUse(); //see if we should XML-encode Unicode private use characters
		final int textLength = text.length(); //see how long the content is currently
		for(int i = 0; i < textLength; ++i) { //look at each character in the text
			final char c = text.charAt(i); //get a reference to this character
			final int entityIndex = entityCharacterValues.indexOf(c); //see if this character matches an entity value
			//always replace entities, regardless of the status of the settings
			//  because the lookup table has already been set up appropriately
			//  based on that option TODO always populate the table, but check the flag here
			if(entityIndex >= 0) { //if this character should be replaced by an entity
				appendable.append(ENTITY_REF_START); //append the start-of-entity
				appendable.append(entityNames[entityIndex]); //append the corresponding entity name
				appendable.append(ENTITY_REF_END); //append the end-of-entity
			} else {
				final boolean hasPredefinedEntity = PREDEFINED_ENTITY_CHARACTERS.contains(c);
				final boolean shouldEncode = REQUIRED_ENCODE_CHRACTERS.contains(c)
						|| (hasPredefinedEntity && getUsePredefinedEntities() == PredefinedEntitiesUse.ALWAYS) || c == delimiter
						|| (xmlEncodeNonASCII && !Characters.isASCII(c)) || (xmlEncodeControl && Character.isISOControl(c))
						|| (xmlEncodePrivateUse && Character.getType(c) == Character.PRIVATE_USE);
				if(shouldEncode) { //if we should encode, use a predefined entity if we can
					if(hasPredefinedEntity && getUsePredefinedEntities() != PredefinedEntitiesUse.NEVER) {
						appendable.append(ENTITY_REF_START); //append the start-of-entity
						appendable.append(getPredefinedEntityName(c)); //append the corresponding predefined entity name
						appendable.append(ENTITY_REF_END); //append the end-of-entity
					} else { //as a last resort append a character reference
						appendable.append(CHARACTER_REF_START); //append the start-of-character-reference
						appendable.append('x'); //show that this will be a hexadecimal number TODO use a constant here
						appendable.append(Integer.toHexString(c).toUpperCase()); //append the hex representation of the character
						appendable.append(CHARACTER_REF_END); //append the end-of-character-reference
					}
				} else { //if this character shouldn't be encoded
					appendable.append(c); //append the character normally
				}
			}
		}
		return appendable;
	}

	/**
	 * Collapses all runs of given characters (including single characters that are not already the normalized character itself) to a single normalized character.
	 * Optionally removes all run characters from the beginning and ending of the text.
	 * @implNote This method is useful to normalize runs of "space" characters such as whitespace to a single space <code>' '</code> character.
	 * @param text The text to normalize.
	 * @param trimStart Whether all run characters should be trimmed from the start of the string rather than collapsed.
	 * @param trimEnd Whether all run characters should be trimmed from the end of the string rather than collapsed.
	 * @param runCharacters The characters considers characters for purposes of collapsing runs, such as whitespace characters.
	 * @param normalizedChar The character to which to runs of characters; e.g. the space <code>' '</code> character itself.
	 * @return A character sequence with no beginning and/or trailing run characters, if start and/or end trimming was indicated, respectively; and all character
	 *         runs normalized to a single normalized character.
	 */
	public static CharSequence collapseRuns(@Nonnull final CharSequence text, @Nonnull final Characters runCharacters, final char normalizedChar,
			final boolean trimStart, final boolean trimEnd) {
		//There are two types of normalizing we may need to do:
		//* collapse runs - If we need to do this, we do it with a string builder and include trimming as well.
		//* trim - If we only need to trim and not collapse runs, we just return a subsequence at the end.
		final int length = text.length();
		final int start;
		if(trimStart) {
			start = indexNotOf(text, runCharacters);
			if(start < 0) { //string is only made up of run characters
				return "";
			}
		} else {
			start = 0;
		}

		final int end; //ending index is exclusive
		if(trimEnd) {
			end = lastIndexNotOf(text, runCharacters) + 1; //0 will be used if not found; end will never be -1
		} else {
			end = length;
		}
		if(end - start < 1) { //if there is no content to process
			return "";
		}
		StringBuilder runCollapseStringBuilder = null; //we'll only need to copy if we are collapsing runs
		int copyStart = start; //if we ever start copying, we'll copy starting at the start index
		int searchStart = start; //the search position will be updated for every search cycle
		while(searchStart < end) {
			final int runStart = indexOf(text, runCharacters, searchStart); //find the next run character
			if(runStart < 0 || runStart >= end) { //no more run characters within the range
				break;
			}
			final int runEnd;
			{
				final int rawRunEnd = indexNotOf(text, runCharacters, runStart + 1); //find the end of the character run
				runEnd = rawRunEnd >= 0 ? rawRunEnd : length; //compensate if we didn't trim the end and the run goes to the end
			}
			final int runLength = runEnd - runStart;
			//collapse runs of characters, as well as any character that is not already the normalized space character
			if(runLength > 1 || text.charAt(runStart) != normalizedChar) {
				if(runCollapseStringBuilder == null) {
					runCollapseStringBuilder = new StringBuilder(); //lazily create the string builder
				}
				runCollapseStringBuilder.append(text, copyStart, runStart); //append everything since our last copy up to the beginning of the run
				runCollapseStringBuilder.append(normalizedChar); //collapse to a single normalized character
				copyStart = runEnd; //when we need to copy again, we'll start copying after this run
			}
			searchStart = runEnd; //start searching after the run of characters, but leave our copy start where it was until we actually need to copy
		}
		if(runCollapseStringBuilder == null) { //if no runs were collapsed
			if(start > 0 || end < length) { //it's still possible we trimmed without collapsing
				return text.subSequence(start, end); //returning a subsequence is probably more efficient than copying
			}
			return text; //no trimming or run collapsing
		}
		if(copyStart < end) { //if we had to normalize and we have uncopied text at the end
			runCollapseStringBuilder.append(text, copyStart, end); //append all remaining text
		}
		return runCollapseStringBuilder; //return the string builder; the caller may not need to convert it to a string, thus increasing efficiency
	}

}
