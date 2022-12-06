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

import static com.globalmentor.xml.XmlDom.*;

import java.io.*;
import java.net.*;

import javax.xml.parsers.*;
import javax.xml.validation.Schema;

import com.globalmentor.io.IO;
import com.globalmentor.model.ConfiguredStateException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Implementation of loading and saving XML information. This class is a {@link DocumentBuilder}, and can be configured to produced appropriate XML parsers.
 * Instances of {@link DocumentBuilder} are initialized with an {@link DefaultEntityResolver} entity resolver. This class is thread-safe.
 * @author Garret Wilson
 */
public class XMLIO extends DocumentBuilderFactory implements IO<Document> {

	/** Whether a byte order mark (BOM) is written. */
	private boolean bomWritten = true;

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

	/** Whether output is formatted. */
	private boolean formatted = true;

	/** @return Whether output is formatted. */
	public boolean isFormatted() {
		return formatted;
	}

	/**
	 * Sets whether output is formatted.
	 * @param formatted Whether output is formatted.
	 */
	public void setFormatted(final boolean formatted) {
		this.formatted = formatted;
	}

	/** The document builder factory for creating document builders; all access to the factory should be synchronized on the factory instance. */
	private final DocumentBuilderFactory documentBuilderFactory;

	/** Default constructor with no namespace awareness and no validation. */
	public XMLIO() {
		this(false); //construct the class with no namespace awareness
	}

	/**
	 * Namespace awareness constructor with no validation.
	 * @param namespaceAware <code>true</code> if the XML documents will provide support for XML namespaces, else <code>false</code>.
	 */
	public XMLIO(final boolean namespaceAware) {
		this(namespaceAware, false); //construct the class with no validation
	}

	/**
	 * Namespace awareness and validation constructor.
	 * @param namespaceAware <code>true</code> if the XML documents will provide support for XML namespaces, else <code>false</code>.
	 * @param validating <code>true</code> if the XML documents will be validated, else <code>false</code>.
	 */
	public XMLIO(final boolean namespaceAware, final boolean validating) {
		this.documentBuilderFactory = DocumentBuilderFactory.newInstance(); //create a document builder factory
		documentBuilderFactory.setNamespaceAware(namespaceAware); //set namespace awareness appropriately
		documentBuilderFactory.setValidating(validating); //set validating appropriately
	}

	/**
	 * Reads an XML document from an input stream.
	 * @param inputStream The input stream from which to read the data.
	 * @param baseURI The base URI of the data, or <code>null</code> if no base URI is available.
	 * @return The XML document read from the input stream.
	 * @throws NullPointerException if the given input stream is <code>null</code>.
	 * @throws IOException if there is an error reading the data.
	 * @throws IllegalStateException if there is no available parser to match the configuration.
	 */
	public final Document read(final InputStream inputStream, final URI baseURI) throws IOException {
		final DocumentBuilder documentBuilder;
		try {
			synchronized(this) { //prove thread-safety to the document builder factory
				documentBuilder = documentBuilderFactory.newDocumentBuilder(); //create a new document builder
			}
		} catch(final ParserConfigurationException parserConfigurationException) { //if there is an error finding an appropriate parser
			throw new IllegalStateException(parserConfigurationException);
		}
		return readXML(inputStream, baseURI, documentBuilder); //parse the XML and return the resulting document
	}

	/**
	 * Reads an XML document from an input stream using a default document builder without namespace awareness and with no validation. An entity resolver is
	 * installed to load requested resources from local resources if possible. This allows quick local lookup of the XHTML DTDs, for example. The Sun JDK 1.5
	 * document builder handles the BOM correctly.
	 * @param inputStream The input stream from which to read the data.
	 * @param baseURI The base URI of the data, or <code>null</code> if no base URI is available.
	 * @return The XML document representing the data read.
	 * @throws NullPointerException if the given input stream is <code>null</code>.
	 * @throws IOException if there is an error reading the data.
	 * @throws IllegalStateException if there is no available parser to match the configuration.
	 */
	public static Document readXML(final InputStream inputStream, final URI baseURI) throws IOException {
		return readXML(inputStream, baseURI, false); //read the XML with no namespace awareness
	}

	/**
	 * Reads an XML document from an input stream using a default document builder with no validation. An entity resolver is installed to load requested resources
	 * from local resources if possible. This allows quick local lookup of the XHTML DTDs, for example. The Sun JDK 1.5 document builder handles the BOM
	 * correctly.
	 * @param inputStream The input stream from which to read the data.
	 * @param baseURI The base URI of the data, or <code>null</code> if no base URI is available.
	 * @param namespaceAware <code>true</code> if the parser produced will provide support for XML namespaces, else <code>false</code>.
	 * @return The XML document representing the data read.
	 * @throws NullPointerException if the given input stream is <code>null</code>.
	 * @throws IOException if there is an error reading the data.
	 * @throws IllegalStateException if there is no available parser to match the configuration.
	 */
	public static Document readXML(final InputStream inputStream, final URI baseURI, final boolean namespaceAware) throws IOException {
		return readXML(inputStream, baseURI, namespaceAware, false); //read the XML with no validation
	}

	/**
	 * Reads an XML document from an input stream using a default document builder. An entity resolver is installed to load requested resources from local
	 * resources if possible. This allows quick local lookup of the XHTML DTDs, for example. The Sun JDK 1.5 document builder handles the BOM correctly.
	 * @param inputStream The input stream from which to read the data.
	 * @param baseURI The base URI of the data, or <code>null</code> if no base URI is available.
	 * @param namespaceAware <code>true</code> if the parser produced will provide support for XML namespaces, else <code>false</code>.
	 * @param validating <code>true</code> if the parser produced will validate documents as they are parsed, else <code>false</code>.
	 * @return The XML document representing the data read.
	 * @throws NullPointerException if the given input stream is <code>null</code>.
	 * @throws ConfiguredStateException if a document builder cannot be created which satisfies the configuration requested.
	 * @throws IOException if there is an error reading the data.
	 */
	public static Document readXML(final InputStream inputStream, final URI baseURI, final boolean namespaceAware, final boolean validating) throws IOException {
		return readXML(inputStream, baseURI, createDocumentBuilder(namespaceAware, validating)); //create a document builder and read the XML
	}

	/**
	 * Reads an XML document from an input stream using the provided document builder.
	 * @param inputStream The input stream from which to read the data.
	 * @param baseURI The base URI of the data, or <code>null</code> if no base URI is available.
	 * @param documentBuilder The document builder to use for parsing the XML.
	 * @return The XML document representing the data read.
	 * @throws NullPointerException if the given input stream and/or document builder is <code>null</code>.
	 * @throws IOException if there is an error reading the data.
	 */
	public static Document readXML(final InputStream inputStream, final URI baseURI, final DocumentBuilder documentBuilder) throws IOException {
		try {
			return documentBuilder.parse(inputStream); //parse an XML document from the input stream
		} catch(final SAXException saxException) { //if a parsing error occurs 
			throw new IOException(saxException);
		}
	}

	/**
	 * Writes an XML document to an output stream.
	 * @param outputStream The output stream to which to write the data.
	 * @param baseURI The base URI of the data, or <code>null</code> if no base URI is available.
	 * @param document The XML document to write to the given output stream.
	 * @throws NullPointerException if the given output stream and/or document is <code>null</code>.
	 * @throws IOException Thrown if there is an error writing the data.
	 */
	public void write(final OutputStream outputStream, final URI baseURI, final Document document) throws IOException {
		writeXML(outputStream, baseURI, document, isBOMWritten(), isFormatted()); //write the XML using the specified settings
	}

	/**
	 * Writes an XML document to an output stream, formatted with a byte order mark (BOM).
	 * @param outputStream The output stream to which to write the data.
	 * @param baseURI The base URI of the data, or <code>null</code> if no base URI is available.
	 * @param document The XML document to write to the given output stream.
	 * @throws NullPointerException if the given output stream and/or document is <code>null</code>.
	 * @throws IOException Thrown if there is an error writing the data.
	 */
	public static void writeXML(final OutputStream outputStream, final URI baseURI, final Document document) throws IOException {
		writeXML(outputStream, baseURI, document, true, true); //write the XML with formatted output and a BOM
	}

	/**
	 * Writes an XML document instance to an output stream.
	 * @param outputStream The output stream to which to write the data.
	 * @param baseURI The base URI of the data, or <code>null</code> if no base URI is available.
	 * @param document The XML document to write to the given output stream.
	 * @param bomWritten Whether a byte order mark (BOM) is written.
	 * @param formatted Whether output is formatted.
	 * @throws NullPointerException if the given output stream and/or document is <code>null</code>.
	 * @throws IOException Thrown if there is an error writing the data.
	 */
	public static void writeXML(final OutputStream outputStream, final URI baseURI, final Document document, final boolean bomWritten, final boolean formatted)
			throws IOException {
		final XMLSerializer xmlSerializer = new XMLSerializer(formatted); //create a new XML serializer
		xmlSerializer.setBomWritten(bomWritten); //indicate whether the BOM should be written
		xmlSerializer.serialize(document, outputStream); //serialize the document to the output stream 
	}

	@Override
	public DocumentBuilder newDocumentBuilder() throws ParserConfigurationException {
		synchronized(documentBuilderFactory) {
			return documentBuilderFactory.newDocumentBuilder();
		}
	}

	@Override
	public void setNamespaceAware(boolean awareness) {
		synchronized(documentBuilderFactory) {
			documentBuilderFactory.setNamespaceAware(awareness);
		}
	}

	@Override
	public void setValidating(boolean validating) {
		synchronized(documentBuilderFactory) {
			documentBuilderFactory.setValidating(validating);
		}
	}

	@Override
	public void setIgnoringElementContentWhitespace(boolean whitespace) {
		synchronized(documentBuilderFactory) {
			documentBuilderFactory.setIgnoringElementContentWhitespace(whitespace);
		}
	}

	@Override
	public void setExpandEntityReferences(boolean expandEntityRef) {
		synchronized(documentBuilderFactory) {
			documentBuilderFactory.setExpandEntityReferences(expandEntityRef);
		}
	}

	@Override
	public void setIgnoringComments(boolean ignoreComments) {
		synchronized(documentBuilderFactory) {
			documentBuilderFactory.setIgnoringComments(ignoreComments);
		}
	}

	@Override
	public void setCoalescing(boolean coalescing) {
		synchronized(documentBuilderFactory) {
			documentBuilderFactory.setCoalescing(coalescing);
		}
	}

	@Override
	public boolean isNamespaceAware() {
		synchronized(documentBuilderFactory) {
			return documentBuilderFactory.isNamespaceAware();
		}
	}

	@Override
	public boolean isValidating() {
		synchronized(documentBuilderFactory) {
			return documentBuilderFactory.isValidating();
		}
	}

	@Override
	public boolean isIgnoringElementContentWhitespace() {
		synchronized(documentBuilderFactory) {
			return documentBuilderFactory.isIgnoringElementContentWhitespace();
		}
	}

	@Override
	public boolean isExpandEntityReferences() {
		synchronized(documentBuilderFactory) {
			return documentBuilderFactory.isExpandEntityReferences();
		}
	}

	@Override
	public boolean isIgnoringComments() {
		synchronized(documentBuilderFactory) {
			return documentBuilderFactory.isIgnoringComments();
		}
	}

	@Override
	public boolean isCoalescing() {
		synchronized(documentBuilderFactory) {
			return documentBuilderFactory.isCoalescing();
		}
	}

	@Override
	public void setAttribute(String name, Object value) {
		synchronized(documentBuilderFactory) {
			documentBuilderFactory.setAttribute(name, value);
		}
	}

	@Override
	public Object getAttribute(String name) {
		synchronized(documentBuilderFactory) {
			return documentBuilderFactory.getAttribute(name);
		}
	}

	@Override
	public void setFeature(String name, boolean value) throws ParserConfigurationException {
		synchronized(documentBuilderFactory) {
			documentBuilderFactory.setFeature(name, value);
		}
	}

	@Override
	public boolean getFeature(String name) throws ParserConfigurationException {
		synchronized(documentBuilderFactory) {
			return documentBuilderFactory.getFeature(name);
		}
	}

	@Override
	public Schema getSchema() {
		synchronized(documentBuilderFactory) {
			return documentBuilderFactory.getSchema();
		}
	}

	@Override
	public void setSchema(Schema schema) {
		synchronized(documentBuilderFactory) {
			documentBuilderFactory.setSchema(schema);
		}
	}

	@Override
	public void setXIncludeAware(final boolean state) {
		synchronized(documentBuilderFactory) {
			documentBuilderFactory.setXIncludeAware(state);
		}
	}

	@Override
	public boolean isXIncludeAware() {
		synchronized(documentBuilderFactory) {
			return documentBuilderFactory.isXIncludeAware();
		}
	}

}
