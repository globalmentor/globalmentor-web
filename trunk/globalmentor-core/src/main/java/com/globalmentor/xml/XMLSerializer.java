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
import java.nio.charset.Charset;

import com.globalmentor.io.ByteOrderMark;

import static com.globalmentor.java.CharSequences.*;
import static com.globalmentor.java.Characters.SPACE_CHAR;

import com.globalmentor.java.Characters;

import static com.globalmentor.w3c.spec.XML.*;
import static com.globalmentor.xml.XML.*;
import static java.nio.charset.StandardCharsets.*;

import com.globalmentor.util.PropertiesUtilities;

import org.w3c.dom.*;

/**
 * Class which serializes an XML document to a byte-oriented output stream. Has the option of automatically formatting the output in a hierarchical structure
 * with tabs or other strings.
 * @author Garret Wilson
 */
public class XMLSerializer {

	/** Whether the output should be formatted. */
	public static final String FORMAT_OUTPUT_OPTION = "formatOutput";

	/** Default to unformatted output. */
	public static final boolean FORMAT_OUTPUT_OPTION_DEFAULT = false;

	/** Whether Unicode control characters should be XML-encoded. */
	public static final String XML_ENCODE_CONTROL_OPTION = "xmlEncodeControl";

	/** Default to not XML-encoding control characters. */
	public static final boolean XML_ENCODE_CONTROL_OPTION_DEFAULT = false;

	/** Whether extended characters (above 127) should be XML-encoded. */
	public static final String XML_ENCODE_NON_ASCII_OPTION = "xmlEncodeNonASCII";

	/** Default to not XML-encoding extended characters. */
	public static final boolean XML_ENCODE_NON_ASCII_OPTION_DEFAULT = false;

	/** Whether private use Unicode characters should be XML-encoded. */
	public static final String XML_ENCODE_PRIVATE_USE_OPTION = "xmlEncodePrivateUse";

	/** Default to not XML-encoding private use characters. */
	public static final boolean XML_ENCODE_PRIVATE_USE_OPTION_DEFAULT = false;

	/**
	 * Whether characters which match a one-character predefined entity should be encoded using that entity.
	 */
	public static final String USE_ENTITIES_OPTION = "useEntities";

	/** Default to using one-character predefined entities for encoding. */
	public static final boolean USE_ENTITIES_OPTION_DEFAULT = true;

	/** Whether a byte order mark (BOM) is written. */
	private boolean bomWritten = false;

	/** @return Whether a byte order mark (BOM) is written. */
	public boolean isBOMWritten() {
		return bomWritten;
	}

	/**
	 * Whether a byte order mark (BOM) is written.
	 * @param bomWritten Whether a byte order mark (BOM) is written.
	 */
	public void setBOMWritten(final boolean bomWritten) {
		this.bomWritten = bomWritten;
	}

	/** Whether the output should be formatted. */
	private boolean formatted = FORMAT_OUTPUT_OPTION_DEFAULT;

	/** @return Whether the output should be formatted. */
	public boolean isFormatted() {
		return formatted;
	}

	/**
	 * Sets whether the output should be formatted.
	 * @param newFormatted <code>true</code> if the output should be formatted, else <code>false</code>.
	 */
	public void setFormatted(final boolean newFormatted) {
		formatted = newFormatted;
	}

	/** Whether Unicode control characters should be XML-encoded. */
	private boolean xmlEncodeControl = XML_ENCODE_CONTROL_OPTION_DEFAULT;

	/**
	 * @return Whether Unicode control characters should be XML-encoded.
	 * @see #isUseEntities
	 */
	public boolean isXMLEncodeControl() {
		return xmlEncodeControl;
	}

	/**
	 * Sets whether Unicode control characters should be XML-encoded.
	 * @param newXMLEncodeControl <code>true</code> if control characters should be XML-encoded, else <code>false</code>.
	 * @see setUseEntities
	 */
	public void setXMLEncodeControl(final boolean newXMLEncodeControl) {
		xmlEncodeControl = newXMLEncodeControl;
	}

	/** Whether extended characters (above 127) should be XML-encoded. */
	private boolean xmlEncodeNonASCII = XML_ENCODE_NON_ASCII_OPTION_DEFAULT;

	/**
	 * @return Whether extended characters (above 127) should be XML-encoded.
	 * @see #isUseEntities
	 */
	public boolean isXMLEncodeNonASCII() {
		return xmlEncodeNonASCII;
	}

	/**
	 * Sets whether extended characters (above 127) should be XML-encoded.
	 * @param newXMLEncodeNonASCII <code>true</code> if extended characters should be XML-encoded, else <code>false</code>.
	 * @see setUseEntities
	 */
	public void setXMLEncodeNonASCII(final boolean newXMLEncodeNonASCII) {
		xmlEncodeNonASCII = newXMLEncodeNonASCII;
	}

	/** Whether private use Unicode characters should be XML-encoded. */
	private boolean xmlEncodePrivateUse = XML_ENCODE_PRIVATE_USE_OPTION_DEFAULT;

	/**
	 * @return Whether private use Unicode characters should be XML-encoded.
	 * @see #isUseEntities
	 */
	public boolean isXMLEncodePrivateUse() {
		return xmlEncodePrivateUse;
	}

	/**
	 * Sets whether private use Unicode characters should be XML-encoded.
	 * @param newXMLEncodePrivateUse <code>true</code> if private use characters should be XML-encoded, else <code>false</code>.
	 * @see setUseEntities
	 */
	public void setXMLEncodePrivateUse(final boolean newXMLEncodePrivateUse) {
		xmlEncodePrivateUse = newXMLEncodePrivateUse;
	}

	/**
	 * Whether characters which match a one-character predefined entity should be encoded using that entity.
	 */
	private boolean useEntities = USE_ENTITIES_OPTION_DEFAULT;

	/**
	 * @return Whether one-character entities will be used when possible.
	 * @see #isXMLEncode
	 */
	public boolean isUseEntities() {
		return useEntities;
	}

	/**
	 * Sets whether characters which match a one-character predefined entity should be encoded using that entity. This will override the XML-encoding settings
	 * when applicable.
	 * @param newUseEntities <code>true</code> if available entities should be used to encode characters.
	 * @see #setXMLEncode
	 */
	public void setUseEntities(final boolean newUseEntities) {
		useEntities = newUseEntities;
	}

	/** Whether missing namespaces declarations should be added. */
	private boolean namespacesDeclarationsEnsured = true;

	/** @return Whether missing namespaces declarations should be added. */
	public boolean isNamespacesDeclarationsEnsured() {
		return namespacesDeclarationsEnsured;
	}

	/**
	 * Sets whether missing namespace declarations are added.
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
	 * @param namespacesDocumentElementDeclarations Whether missing namespaces declarations should be added to the document element if possible, rather than the
	 *          top-level element needing the declaration.
	 */
	public void setNamespacesDocumentElementDeclarations(final boolean namespacesDocumentElementDeclarations) {
		this.namespacesDocumentElementDeclarations = namespacesDocumentElementDeclarations;
	}

	/**
	 * Sets the options based on the contents of the option properties.
	 * @param options The properties which contain the options.
	 */
	public void setOptions(final Properties options) {
		setFormatted(PropertiesUtilities.getBooleanProperty(options, FORMAT_OUTPUT_OPTION, FORMAT_OUTPUT_OPTION_DEFAULT));
		setUseEntities(PropertiesUtilities.getBooleanProperty(options, USE_ENTITIES_OPTION, USE_ENTITIES_OPTION_DEFAULT));
		setXMLEncodeControl(PropertiesUtilities.getBooleanProperty(options, XML_ENCODE_CONTROL_OPTION, XML_ENCODE_CONTROL_OPTION_DEFAULT));
		setXMLEncodeNonASCII(PropertiesUtilities.getBooleanProperty(options, XML_ENCODE_NON_ASCII_OPTION, XML_ENCODE_NON_ASCII_OPTION_DEFAULT));
		setXMLEncodePrivateUse(PropertiesUtilities.getBooleanProperty(options, XML_ENCODE_PRIVATE_USE_OPTION, XML_ENCODE_PRIVATE_USE_OPTION_DEFAULT));
	}

	/**
	 * The string to use for horizontally aligning the elements if formatting is turned on. Defaults to a single tab character.
	 */
	private String horizontalAlignString = "\t";

	/**
	 * @return The string to use for horizontally aligning the elements if formatting is turned on.
	 * @see #isFormatted
	 */
	public String getHorizontalAlignString() {
		return horizontalAlignString;
	}

	/**
	 * Sets the string to use for horizontally aligning the elements if formatting is turned on..
	 * @param newHorizontalAlignString The horizontal alignment string.
	 * @see #setFormatted
	 */
	public void setHorizontalAlignString(final String newHorizontalAlignString) {
		horizontalAlignString = newHorizontalAlignString;
	}

	/** The current level of nesting during document serialization if output is formatted. */
	protected int nestLevel = -1;

	/**
	 * The string representing one-character entity values. The <code>entityNames</code> array will contain the names of the corresponding entities, each at the
	 * same index as the corresponding character value.
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
		initializeEntityLookup(null); //always initialize the entity lookup, so that at least the five XML entities will be included in the table in case they serialize only part of a document TODO fix better so that serializing part of the document somehow initializes these
	}

	/**
	 * Constructor that specifies serialize options.
	 * @param options The options to use for serialization.
	 */
	public XMLSerializer(final Properties options) {
		this(); //do the default construction
		setOptions(options); //set the options from the properties
	}

	/**
	 * Constructor for an optionally formatted serializer.
	 * @param formatted Whether the serializer should be formatted.
	 */
	public XMLSerializer(final boolean formatted) {
		this(); //do the default construction
		setFormatted(formatted); //set whether the output should be formatted
	}

	/**
	 * Serializes the specified document to a string using the UTF-8 encoding with no byte order mark.
	 * @param document The XML document to serialize.
	 * @return A string containing the serialized XML data.
	 * @throws IOException Thrown if an I/O error occurred.
	 * @throws UnsupportedEncodingException Thrown if the UTF-8 encoding not recognized.
	 */
	public String serialize(final Document document) throws UnsupportedEncodingException, IOException {
		try {
			final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(); //create an output stream for receiving the XML data
			serialize(document, byteArrayOutputStream, UTF_8); //serialize the document to the output stream using UTF-8 with no byte order mark
			return byteArrayOutputStream.toString(UTF_8.name()); //convert the byte array to a string, using the UTF-8 encoding, and return it
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
	public String serialize(final DocumentFragment documentFragment) {
		try {
			final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(); //create an output stream for receiving the XML data
			serialize(documentFragment, byteArrayOutputStream, UTF_8); //serialize the document fragment to the output stream using UTF-8 with no byte order mark
			return byteArrayOutputStream.toString(UTF_8.name()); //convert the byte array to a string, using the UTF-8 encoding, and return it
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
	public String serialize(final Element element) {
		try {
			final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(); //create an output stream for receiving the XML data
			serialize(element, byteArrayOutputStream, UTF_8); //serialize the element to the output stream using UTF-8 with no byte order mark
			return byteArrayOutputStream.toString(UTF_8.name()); //convert the byte array to a string, using the UTF-8 encoding, and return it
		} catch(final UnsupportedEncodingException unsupportedEncodingException) { //UTF-8 should always be supported
			throw new AssertionError(unsupportedEncodingException);
		} catch(final IOException ioException) { //there should never by an I/O exception writing to a byte array output stream
			throw new AssertionError(ioException);
		}
	}

	/**
	 * Serializes the content (all child nodes and their descendants) of a specified node to a string using the UTF-8 encoding with no byte order mark.
	 * @param node The XML node the content of which to serialize&mdash;usually an element or document fragment.
	 * @return A string containing the serialized XML data.
	 */
	public String serializeContent(final Node node) {
		try {
			final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(); //create an output stream for receiving the XML data
			serializeContent(node, byteArrayOutputStream, UTF_8); //serialize the node content to the output stream using UTF-8 with no byte order mark
			return byteArrayOutputStream.toString(UTF_8.name()); //convert the byte array to a string, using the UTF-8 encoding, and return it
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
	public void serialize(final Document document, final OutputStream outputStream) throws IOException {
		try {
			serialize(document, outputStream, UTF_8); //serialize the document, defaulting to UTF-8
		} catch(final IOException ioException) { //there should never by an I/O exception writing to a byte array output stream
			throw new AssertionError(ioException);
		}
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
	public void serialize(final Document document, final OutputStream outputStream, final Charset charset) throws IOException, UnsupportedEncodingException {
		nestLevel = 0; //show that we haven't started nesting yet
		//TODO del if not needed		outputStream.write(charset.getByteOrderMark());	//write the byte order mark, which may be an empty array
		if(isBOMWritten()) { //if we should write a BOM
			final ByteOrderMark bom = ByteOrderMark.forCharset(charset); //get the byte order mark, if there is one
			outputStream.write(bom.getBytes()); //write the byte order mark
		}
		final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, charset)); //create a new writer based on our encoding TODO see if the writer automatically writes the byte order mark already for non-UTF-8
		writeProlog(document, writer, charset); //write the prolog
		final DocumentType documentType = document.getDoctype(); //get the document type, if there is one
		if(documentType != null) { //if there is a document type
			initializeEntityLookup(documentType.getEntities()); //initialize the entity lookup based on the provided entities
			write(documentType, writer); //write the document type
		} else
			//if there is no document type
			initializeEntityLookup(null); //always initialize the entity lookup, so that at least the five XML entities will be included in the table
		writeProcessingInstructions(document, writer); //write any processing instructions
		final Element documentElement = document.getDocumentElement(); //get the document element
		if(isNamespacesDeclarationsEnsured()) { //if we should ensure namespaces
			if(isNamespacesDocumentElementDeclarations()) { //if missing namespaces should be declared on the document element, process the entire document before writing
				ensureNamespaceDeclarations(documentElement, documentElement, true); //make sure all namespaces are declared that all elements need (i.e. deep), declaring any missing elements on the document element
			}
		}
		write(documentElement, writer); //write the document element and all elements below it
		if(isFormatted()) { //if we should write formatted output
			writer.newLine(); //add a newline in the default format
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
	public void serialize(final DocumentFragment documentFragment, final OutputStream outputStream) throws UnsupportedEncodingException, IOException {
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
	public void serialize(final DocumentFragment documentFragment, final OutputStream outputStream, final Charset charset) throws IOException,
			UnsupportedEncodingException {
		serializeContent(documentFragment, outputStream, charset); //serialize the content of the document fragment		
	}

	/**
	 * Serializes the specified element and its children to the given output stream using the UTF-8 encoding with the UTF-8 byte order mark.
	 * @param element The XML element to serialize.
	 * @param outputStream The stream into which the element should be serialized.
	 * @throws IOException Thrown if an I/O error occurred.
	 * @throws UnsupportedEncodingException Thrown if the UTF-8 encoding is not recognized.
	 */
	public void serialize(final Element element, final OutputStream outputStream) throws IOException, UnsupportedEncodingException {
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
	public void serialize(final Element element, final OutputStream outputStream, final Charset charset) throws IOException, UnsupportedEncodingException {
		nestLevel = 0; //show that we haven't started nesting yet
		//TODO del if not needed		outputStream.write(charset.getByteOrderMark());	//write the byte order mark, which may be an empty array
		if(isBOMWritten()) { //if we should write a BOM
			final ByteOrderMark bom = ByteOrderMark.forCharset(charset); //get the byte order mark, if there is one
			outputStream.write(bom.getBytes()); //write the byte order mark
		}
		final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, charset)); //create a new writer based on our encoding TODO see if the writer automatically writes the byte order mark already for non-UTF-8
		write(element, writer); //write the element and all elements below it
		if(isFormatted()) { //if we should write formatted output
			writer.newLine(); //add a newline in the default format
		}
		writer.flush(); //flush any data we've buffered
	}

	/**
	 * Serializes the content (all child nodes and their descendants) of a specified node to the given output stream using the UTF-8 encoding with the UTF-8 byte
	 * order mark.
	 * @param node The XML node the content of which to serialize&mdash;usually an element or document fragment.
	 * @param outputStream The stream into which the element content should be serialized.
	 * @throws IOException Thrown if an I/O error occurred.
	 * @throws UnsupportedEncodingException Thrown if the UTF-8 encoding is not recognized.
	 */
	protected void serializeContent(final Node node, final OutputStream outputStream) throws IOException, UnsupportedEncodingException {
		serializeContent(node, outputStream, UTF_8); //serialize the content UTF-8
	}

	/**
	 * Serializes the content (all child nodes and their descendants) of a specified node to the given output stream using the specified encoding. Any byte order
	 * mark specified in the character encoding will be written to the stream.
	 * @param node The XML node the content of which to serialize&mdash;usually an element or document fragment.
	 * @param outputStream The stream into which the element content should be serialized.
	 * @param charset The charset to use when serializing.
	 * @throws IOException Thrown if an I/O error occurred.
	 * @throws UnsupportedEncodingException Thrown if the specified encoding is not recognized.
	 */
	protected void serializeContent(final Node node, final OutputStream outputStream, final Charset charset) throws IOException, UnsupportedEncodingException {
		nestLevel = 0; //show that we haven't started nesting yet
		//TODO del if not needed		outputStream.write(charset.getByteOrderMark());	//write the byte order mark, which may be an empty array
		if(isBOMWritten()) { //if we should write a BOM
			final ByteOrderMark bom = ByteOrderMark.forCharset(charset); //get the byte order mark, if there is one
			outputStream.write(bom.getBytes()); //write the byte order mark
		}
		final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, charset)); //create a new writer based on our encoding TODO see if the writer automatically writes the byte order mark already for non-UTF-8
		writeContent(node, writer); //write all children of the node
		if(isFormatted()) { //if we should write formatted output
			writer.newLine(); //add a newline in the default format
		}
		writer.flush(); //flush any data we've buffered
	}

	/**
	 * Initializes the internal entity lookup with the entities defined in the specified map. The given entities will only be placed in the lookup table if the
	 * "useEntities" option is turned on, and then only entities which represent one-character entities will be used. If "useEntities" is turned on, the five
	 * entities guaranteed to be available in XML (<code>&lt;&amp;amp;&gt;</code>, <code>&lt;&amp;lt;&gt;</code>, <code>&lt;&amp;gt;&gt;</code>,
	 * <code>&lt;&amp;apos;&gt;</code>, <code>&lt;&amp;quot;&gt;</code>) will be included in the internal lookup table if these entities do not exist in the
	 * specified entity map, using their default XML values.
	 * @param entityMap The entity map which contains the entities to be placed in the internal lookup table, or <code>null</code> if entities are not available,
	 *          in which case only the default XML entities will be used.
	 * @see #isUseEntities
	 * @see #setUseEntities
	 */
	protected void initializeEntityLookup(final NamedNodeMap entityMap) {
		final List<String> entityNameList = new ArrayList<String>(); //create an array to hold the entity names we use
		final StringBuilder entityCharacterValueStringBuilder = new StringBuilder(); //create a buffer to hold all the character values
		if(isUseEntities()) { //if we were asked to use their entities
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
								entityNameList.add(entity.getNodeName()); //add the entity's name to the list
								entityCharacterValueStringBuilder.append(entityValue.charAt(0)); //add the one-character value to the value buffer
							}
						}
					}
				}
			}
			//add the "amp" entity if needed
			if(indexOf(entityCharacterValueStringBuilder, ENTITY_AMP_VALUE) < 0) { //if this entity value isn't defined
				entityNameList.add(ENTITY_AMP_NAME); //add the default entity name
				entityCharacterValueStringBuilder.append(ENTITY_AMP_VALUE); //add the default entity value
			}
			//add the "lt" entity if needed
			if(indexOf(entityCharacterValueStringBuilder, ENTITY_LT_VALUE) < 0) { //if this entity value isn't defined
				entityNameList.add(ENTITY_LT_NAME); //add the default entity name
				entityCharacterValueStringBuilder.append(ENTITY_LT_VALUE); //add the default entity value
			}
			//add the "gt" entity if needed
			if(indexOf(entityCharacterValueStringBuilder, ENTITY_GT_VALUE) < 0) { //if this entity value isn't defined
				entityNameList.add(ENTITY_GT_NAME); //add the default entity name
				entityCharacterValueStringBuilder.append(ENTITY_GT_VALUE); //add the default entity value
			}
			//add the "apos" entity if needed
			if(indexOf(entityCharacterValueStringBuilder, ENTITY_APOS_VALUE) < 0) { //if this entity value isn't defined
				entityNameList.add(ENTITY_APOS_NAME); //add the default entity name
				entityCharacterValueStringBuilder.append(ENTITY_APOS_VALUE); //add the default entity value
			}
			//add the "quot" entity if needed
			if(indexOf(entityCharacterValueStringBuilder, ENTITY_QUOT_VALUE) < 0) { //if this entity value isn't defined
				entityNameList.add(ENTITY_QUOT_NAME); //add the default entity name
				entityCharacterValueStringBuilder.append(ENTITY_QUOT_VALUE); //add the default entity value
			}
		}
		entityNames = entityNameList.toArray(new String[entityNameList.size()]); //convert the entities in the list to an array
		entityCharacterValues = entityCharacterValueStringBuilder.toString(); //convert the values into one searchable string
	}

	/**
	 * Serializes the specified document's to the given writer.
	 * @param document The XML document the prolog of which to serialize.
	 * @param writer The writer into which the prolog should be written.
	 * @param charset The charset in use.
	 * @throws IOException Thrown if an I/O error occurred.
	 */
	protected void writeProlog(final Document document, final BufferedWriter writer, final Charset charset) throws IOException {
		writer.write(XML_DECL_START + SPACE_CHAR + VERSIONINFO_NAME + EQUAL_CHAR + DOUBLE_QUOTE_CHAR + XML_VERSION + DOUBLE_QUOTE_CHAR + SPACE_CHAR
				+ ENCODINGDECL_NAME + EQUAL_CHAR + DOUBLE_QUOTE_CHAR + charset.name() + DOUBLE_QUOTE_CHAR + XML_DECL_END); //write the XML prolog
		if(isFormatted()) { //if we should write formatted output
			writer.newLine(); //add a newline in the default format
		}
	}

	/**
	 * Serializes the document's processing instructions to the given writer.
	 * @param document The XML document the processing instructions of which to serialize.
	 * @param writer The writer into which the processing instructions should be written.
	 * @throws IOException Thrown if an I/O error occurred.
	 */
	protected void writeProcessingInstructions(final Document document, final BufferedWriter writer) throws IOException {
		final NodeList childNodeList = document.getChildNodes(); //get the list of document child nodes
		final int childNodeCount = childNodeList.getLength(); //find out how many document child nodes there are
		for(int childIndex = 0; childIndex < childNodeCount; childIndex++) { //look at each document child node
			final Node node = childNodeList.item(childIndex); //look at this document child node
			if(node.getNodeType() == Node.PROCESSING_INSTRUCTION_NODE) //if this is a processing instruction
				write((ProcessingInstruction)node, writer); //write the processing instruction node
		}
	}

	/**
	 * Serializes the document type to the given writer.
	 * @param documentType The XML document type to serialize.
	 * @param writer The writer into which the document type should be written.
	 * @throws IOException Thrown if an I/O error occurred.
	 */
	protected void write(final DocumentType documentType, final BufferedWriter writer) throws IOException {
		writer.write(DOCTYPE_DECL_START + SPACE_CHAR + documentType.getName()); //write the beginning of the document type declaration
		final String publicID = documentType.getPublicId(); //get the public ID, if there is one
		final String systemID = documentType.getSystemId(); //get the system ID, if there is one 
		if(publicID != null || systemID != null) { //if there is a public ID or a system ID
			writer.write(SPACE_CHAR); //separate the identifiers from the doctype introduction
			if(publicID != null) { //if there is a public ID
				writer.write(PUBLIC_ID_NAME + SPACE_CHAR + DOUBLE_QUOTE_CHAR + publicID + DOUBLE_QUOTE_CHAR + SPACE_CHAR); //write the public ID name and its public literal
				assert systemID != null : "A system ID is expected following a public ID.";
			} else { //if there is no public ID
				writer.write(SYSTEM_ID_NAME + SPACE_CHAR); //write the system ID name
			}
			writer.write(DOUBLE_QUOTE_CHAR + documentType.getSystemId() + DOUBLE_QUOTE_CHAR); //always write the system literal
		}
		writer.write(DOCTYPE_DECL_END); //write the end of the document type declaration
		if(isFormatted()) { //if we should write formatted output
			writer.newLine(); //add a newline in the default format
		}
	}

	/**
	 * Serializes the given processing instruction to the given writer.
	 * @param processingInstruction The XML processing instruction to serialize.
	 * @param writer The writer into which the processing instruction should be written.
	 * @throws IOException Thrown if an I/O error occurred.
	 */
	protected void write(final ProcessingInstruction processingInstruction, final BufferedWriter writer) throws IOException {
		//write the beginning of the processing instruction: "<?target "
		writer.write(PROCESSING_INSTRUCTION_START + processingInstruction.getTarget() + SPACE_CHAR);
		writer.write(processingInstruction.getData()); //write the processing instruction data
		writer.write(PROCESSING_INSTRUCTION_END); //write the end of the processing instruction
		if(isFormatted()) { //if we should write formatted output
			writer.newLine(); //add a newline in the default format
		}
	}

	/**
	 * Serializes the specified element to the given writer, using the default formatting options.
	 * @param element The XML element to serialize.
	 * @param writer The writer into which the element should be written.
	 * @throws IOException Thrown if an I/O error occurred.
	 */
	protected void write(final Element element, final BufferedWriter writer) throws IOException {
		write(element, writer, isFormatted()); //write the element using the default formatting options
	}

	/**
	 * Serializes the specified element to the given writer.
	 * @param element The XML element to serialize.
	 * @param writer The writer into which the element should be written.
	 * @param formatted Whether this element and its contents, including any child elements, should be formatted.
	 * @throws IOException Thrown if an I/O error occurred.
	 */
	protected void write(final Element element, final BufferedWriter writer, boolean formatted) throws IOException {
		if(formatted) //if we should write formatted output
			writeHorizontalAlignment(writer, nestLevel); //horizontally align the element
		writer.write(TAG_START + element.getNodeName()); //write the beginning of the start tag
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
			writeAttribute(attribute.getName(), attribute.getValue(), writer); //write this attribute
		}
		if(element.getChildNodes().getLength() > 0) { //if there are child elements
			writer.write(TAG_END); //write the end of the start tag
			boolean contentFormatted = false; //we'll determine if we should format the content of the child nodes
			//we'll only format the contents if there are only element children
			if(formatted) { //if we've been told to format, we'll make sure there are element child nodes
				for(int childIndex = 0; childIndex < element.getChildNodes().getLength(); childIndex++) { //look at each child node
					final int childNodeType = element.getChildNodes().item(childIndex).getNodeType(); //get thic child's type of node
					if(childNodeType == Node.ELEMENT_NODE) { //if this is an element child node
						contentFormatted = true; //show that we should format the element content
					} else if(childNodeType == Node.TEXT_NODE) { //if this is text
						contentFormatted = false; //text or mixed content is always unformatted
						break; //we know we shouldn't format; stop looking at the children
					}
				}
			}
			if(contentFormatted) //if we should write formatted output for the content
				writer.newLine(); //add a newline after the element's starting tag
			++nestLevel; //show that we're nesting to the next level
			writeContent(element, writer, contentFormatted);
			--nestLevel; //show that we're finished with this level of the document hierarchy
			if(contentFormatted) //if we should write formatted output for the content
				writeHorizontalAlignment(writer, nestLevel); //horizontally align the element's ending tag
			writer.write(TAG_START + '/' + element.getNodeName() + TAG_END); //write the ending tag TODO use a constant here
		} else
			//if there are no child elements, this is an empty element
			writer.write(String.valueOf(SPACE_CHAR) + '/' + TAG_END); //write the end of the empty element tag, with an extra space for HTML browser compatibility TODO use a constant here
		if(formatted) //if we should write formatted output
			writer.newLine(); //add a newline after the element
	}

	/**
	 * Serializes the specified attribute name and value to the given writer.
	 * @param attributeName The name of the XML attribute to serialize.
	 * @param attributeValue The name of the XML attribute to serialize.
	 * @param writer The writer into which the attribute should be written.
	 * @throws IOException Thrown if an I/O error occurred.
	 */
	protected void writeAttribute(final String attributeName, final String attributeValue, final BufferedWriter writer) throws IOException {
		//use a double quote character as a delimiter unless the value contains
		//  a double quote; in that case, use a single quote TODO fix escaping---what if both double and single quotes are used?
		final char valueDelimiter = attributeValue.indexOf(DOUBLE_QUOTE_CHAR) < 0 ? DOUBLE_QUOTE_CHAR : SINGLE_QUOTE_CHAR;
		//write the attribute and its value after encoding it for XML; pass the delimiter in case this value has both a single and a double quote
		writer.write(SPACE_CHAR + attributeName + EQUAL_CHAR + valueDelimiter + encodeContent(attributeValue, valueDelimiter) + valueDelimiter);
	}

	/**
	 * Serializes the content of the specified node to the given writer using the default formatting options.
	 * @param node The XML node the content of which to serialize&mdash;usually an element or document fragment.
	 * @param writer The writer into which the element content should be written.
	 * @throws IOException Thrown if an I/O error occurred.
	 */
	protected void writeContent(final Node node, final BufferedWriter writer) throws IOException {
		writeContent(node, writer, isFormatted()); //write the content using the default formatting options
	}

	/**
	 * Serializes the content of the specified element to the given writer.
	 * @param node The XML node the content of which to serialize&mdash;usually an element or document fragment.
	 * @param writer The writer into which the element content should be written.
	 * @param formatted Whether the contents of this element, including any child elements, should be formatted.
	 * @throws IOException Thrown if an I/O error occurred.
	 */
	protected void writeContent(final Node node, final BufferedWriter writer, final boolean formatted) throws IOException {
		for(int childIndex = 0; childIndex < node.getChildNodes().getLength(); childIndex++) { //look at each child node
			final Node childNode = node.getChildNodes().item(childIndex); //look at this node
			switch(childNode.getNodeType()) { //see which type of object this is
				case Node.ELEMENT_NODE: //if this is an element
					write((Element)childNode, writer, formatted); //write this element
					break;
				case Node.TEXT_NODE: //if this is a text node
					writer.write(encodeContent(childNode.getNodeValue())); //write the text value of the node after encoding the string for XML
					break;
				case Node.COMMENT_NODE: //if this is a comment node
					if(formatted) //if we should write formatted output
						writeHorizontalAlignment(writer, nestLevel); //horizontally align the element
					writer.write(COMMENT_START); //write the start of the comment
					writer.write(childNode.getNodeValue()); //write the text value of the node, but don't encode the string for XML since it's inside a comment
					writer.write(COMMENT_END); //write the end of the comment
					if(formatted) //if we should write formatted output
						writer.newLine(); //add a newline after the element
					break;
				case Node.CDATA_SECTION_NODE: //if this is a CDATA section node
					writer.write(CDATA_START); //write the start of the CDATA section
					writer.write(encodeContent(childNode.getNodeValue())); //write the text value of the node after encoding the string for XML
					writer.write(CDATA_END); //write the end of the CDATA section
					break;
			//TODO see if there are any other types of nodes that need serialized
			}
		}
	}

	/**
	 * Creates the specified number of horizontal alignment strings.
	 * @param writer The writer into which the element should be written.
	 * @param nestLevel The level of nesting for horizontal alignment.
	 * @throws IOException Thrown if an I/O error occurred.
	 */
	protected void writeHorizontalAlignment(final BufferedWriter writer, int nestLevel) throws IOException {
		while(nestLevel > 0) { //while we haven't finished nesting
			writer.write(getHorizontalAlignString()); //write another string for horizontal alignment
			--nestLevel; //show that we have one less level to next
		}
	}

	/**
	 * Encodes content using the available entities and/or XML encoding for extended characters.
	 * @param text The text to encode.
	 * @return The encoded version of the content.
	 * @see #isUseEntities
	 * @see #isXMLEncodeControl
	 * @see #isXMLEncodeNonASCII
	 * @see #isXMLEncodePrivateUse
	 */
	protected String encodeContent(final String text) {
		return encodeContent(text, (char)0); //show that there is no delimiter character
	}

	/**
	 * Encodes content using the available entities and/or XML encoding for extended characters. If the delimiter character is not 0, it is unconditionally
	 * encoded.
	 * @param text The text to encode.
	 * @param delimiter The character which should always be encoded, such as the delimiter character for attributes ('"' or '\''), or 0 if there is no delimiter.
	 * @return The encoded version of the content.
	 * @see #isUseEntities
	 * @see #isXMLEncodeControl
	 * @see #isXMLEncodeNonASCII
	 * @see #isXMLEncodePrivateUse
	 */
	protected String encodeContent(final String text, final char delimiter) {
		final boolean xmlEncodeControl = isXMLEncodeControl(); //see if we should XML-encode control characters
		final boolean xmlEncodeNonASCII = isXMLEncodeNonASCII(); //see if we should XML-encode characters over 127
		final boolean xmlEncodePrivateUse = isXMLEncodePrivateUse(); //see if we should XML-encode Unicode private use characters
		final StringBuffer stringBuffer = new StringBuffer(text.length()); //we know that the output string will be at least as long as the input string
		final int textLength = text.length(); //see how long the content is currently
		for(int i = 0; i < textLength; ++i) { //look at each character in the text
			final char c = text.charAt(i); //get a reference to this character
			final int entityIndex = entityCharacterValues.indexOf(c); //see if this character matches an entity value
			//always replace entities, regardless of the status of isXMLEncode(),
			//  because the lookup table has already been set up appropriately
			//  based on that option, and we *always* want to encode such things as '&'
			if(entityIndex >= 0) { //if this character should be replaced by an entity
				stringBuffer.append(ENTITY_REF_START); //append the start-of-entity
				stringBuffer.append(entityNames[entityIndex]); //append the corresponding entity name
				stringBuffer.append(ENTITY_REF_END); //append the end-of-entity
			}
			//if we have no entity replacement for the character,
			//  but it is an extended character and we should XML-encode such
			//  characters (note that the ampersand and less-than/greater-than should *always* be encoded)
			else if(((xmlEncodeNonASCII && !Characters.isASCII(c)) || c == ENTITY_AMP_VALUE || c == ENTITY_LT_VALUE || c == ENTITY_GT_VALUE)
					|| (xmlEncodeControl && Character.isISOControl(c)) //if we should encode control characters, and this is a control character
					|| (xmlEncodePrivateUse && Character.getType(c) == Character.PRIVATE_USE) //if we should encode control characters, and this is a control character
					|| (c == delimiter) //the delimiter character, if present, will *always* be encoded
			) {
				stringBuffer.append(CHARACTER_REF_START); //append the start-of-character-reference
				stringBuffer.append('x'); //show that this will be a hexadecimal number TODO use a constant here
				stringBuffer.append(Integer.toHexString(c).toUpperCase()); //append the hex representation of the character
				//TODO del if not needed				stringBuffer.append(IntegerUtilities.toHexString(c, 4));  //append the character in four-digit hex format (i.g. &#XXXX;)
				stringBuffer.append(CHARACTER_REF_END); //append the end-of-character-reference
			} else { //if this character shouldn't be encoded
				stringBuffer.append(c); //append the character normally
			}
		}
		return stringBuffer.toString(); //return the encoded version of the content
	}

}
