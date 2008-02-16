package com.garretwilson.text.xml;

import java.io.*;
import java.net.*;

import javax.xml.parsers.*;
import javax.xml.validation.Schema;

import com.garretwilson.io.IO;
import static com.garretwilson.text.xml.XMLUtilities.*;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**Implementation of loading and saving XML information.
This class is a {@link DocumentBuilder}, and can be configured to produced appropriate XML parsers.
Instances of {@link DocumentBuilder} are intialized with an {@link XMLEntityResolver} entity resolver.
This class is thread-safe.
@author Garret Wilson
*/
public class XMLIO extends DocumentBuilderFactory implements IO<Document>
{

	/**Whether a byte order mark (BOM) is written.*/
	private boolean bomWritten=true;

		/**@return Whether a byte order mark (BOM) is written.*/
		public boolean isBOMWritten() {return bomWritten;}

		/**Whether a byte order mark (BOM) is written.
		@param bomWritten Whether a byte order mark (BOM) is written.
		*/
		public void setBOMWritten(final boolean bomWritten) {this.bomWritten=bomWritten;}

	/**Whether output is formatted.*/
	private boolean formatted=true;

		/**@return Whether output is formatted.*/
		public boolean isFormatted() {return formatted;}

		/**Sets whether output is formatted.
		@param formatted Whether output is formatted.
		*/
		public void setFormatted(final boolean formatted) {this.formatted=formatted;}

	/**The document builder factory for creating document builders; all access to the factory should be synchronized on the factory instance.*/
	private final DocumentBuilderFactory documentBuilderFactory;
		
	/**Default constructor with no namespace awareness and no validation.*/
	public XMLIO()
	{
		this(false);	//construct the class with no namespace awareness
	}

	/**Namespace awareness constructor with no validation.
	@param namespaceAware <code>true</code> if the XML documents will provide support for XML namespaces, else <code>false</code>.
	*/
	public XMLIO(final boolean namespaceAware)
	{
		this(namespaceAware, false);	//construct the class with no validation
	}

	/**Namespace awareness and validation constructor.
	@param namespaceAware <code>true</code> if the XML documents will provide support for XML namespaces, else <code>false</code>.
	@param validating <code>true</code> if the XML documents will be validated, else <code>false</code>.
	*/
	public XMLIO(final boolean namespaceAware, final boolean validating)
	{
		this.documentBuilderFactory=DocumentBuilderFactory.newInstance();	//create a document builder factory
		documentBuilderFactory.setNamespaceAware(namespaceAware);	//set namespace awareness appropriately
		documentBuilderFactory.setValidating(validating);	//set validating appropriately
	}

	/**Reads an XML document from an input stream.
	@param inputStream The input stream from which to read the data.
	@param baseURI The base URI of the data, or <code>null</code> if no base URI is available.
	@return The XML document read from the input stream.
	@exception NullPointerException if the given input stream is <code>null</code>.
	@exception IOException if there is an error reading the data.
	@exception IllegalStateException if there is no available parser to match the configuration.
	*/ 
	public final Document read(final InputStream inputStream, final URI baseURI) throws IOException
	{
		final DocumentBuilder documentBuilder;
		try
		{
			synchronized(this)	//prove thread-safety to the document builder factory
			{
				documentBuilder=documentBuilderFactory.newDocumentBuilder();	//create a new document builder
			}
		}
		catch(final ParserConfigurationException parserConfigurationException)	//if there is an error finding an appropriate parser
		{
			throw new IllegalStateException(parserConfigurationException);
		}
		return readXML(inputStream, baseURI, documentBuilder);	//parse the XML and return the resulting document
	}

	/**Reads an XML document from an input stream using a default document builder without namespace awareness and with no validation.
	An entity resolver is installed to load requested resources from local resources if possible.
	This allows quick local lookup of the XHTML DTDs, for example.
	The Sun JDK 1.5 document builder handles the BOM correctly.
	@param inputStream The input stream from which to read the data.
	@param baseURI The base URI of the data, or <code>null</code> if no base URI is available.
	@return The XML document representing the data read.
	@exception NullPointerException if the given input stream is <code>null</code>.
	@exception IOException if there is an error reading the data.
	@exception IllegalStateException if there is no available parser to match the configuration.
	*/
	public static Document readXML(final InputStream inputStream, final URI baseURI) throws IOException
	{
		return readXML(inputStream, baseURI, false);	//read the XML with no namespace awareness
	}

	/**Reads an XML document from an input stream using a default document builder with no validation.
	An entity resolver is installed to load requested resources from local resources if possible.
	This allows quick local lookup of the XHTML DTDs, for example.
	The Sun JDK 1.5 document builder handles the BOM correctly.
	@param inputStream The input stream from which to read the data.
	@param baseURI The base URI of the data, or <code>null</code> if no base URI is available.
	@param namespaceAware <code>true</code> if the parser produced will provide support for XML namespaces, else <code>false</code>.
	@return The XML document representing the data read.
	@exception NullPointerException if the given input stream is <code>null</code>.
	@exception IOException if there is an error reading the data.
	@exception IllegalStateException if there is no available parser to match the configuration.
	*/
	public static Document readXML(final InputStream inputStream, final URI baseURI, final boolean namespaceAware) throws IOException
	{
		return readXML(inputStream, baseURI, namespaceAware, false);	//read the XML with no validation
	}
	
	/**Reads an XML document from an input stream using a default document builder.
	An entity resolver is installed to load requested resources from local resources if possible.
	This allows quick local lookup of the XHTML DTDs, for example.
	The Sun JDK 1.5 document builder handles the BOM correctly.
	@param inputStream The input stream from which to read the data.
	@param baseURI The base URI of the data, or <code>null</code> if no base URI is available.
	@param namespaceAware <code>true</code> if the parser produced will provide support for XML namespaces, else <code>false</code>.
	@param validating <code>true</code> if the parser produced will validate documents as they are parsed, else <code>false</code>.
	@return The XML document representing the data read.
	@exception NullPointerException if the given input stream is <code>null</code>.
	@exception IOException if there is an error reading the data.
	@exception IllegalStateException if there is no available parser to match the configuration.
	*/
	public static Document readXML(final InputStream inputStream, final URI baseURI, final boolean namespaceAware, final boolean validating) throws IOException
	{
		try
		{
			return readXML(inputStream, baseURI, createDocumentBuilder(namespaceAware, validating));	//create a document builder and read the XML
		}
		catch(final ParserConfigurationException parserConfigurationException)	//if there is an error finding an appropriate parser
		{
			throw new IllegalStateException(parserConfigurationException);
		}
	}
	
	/**Reads an XML document from an input stream using the provided document builder.
	@param inputStream The input stream from which to read the data.
	@param baseURI The base URI of the data, or <code>null</code> if no base URI is available.
	@param documentBuilder The document builder to use for parsing the XML.
	@return The XML document representing the data read.
	@exception NullPointerException if the given input stream and/or document builder is <code>null</code>.
	@exception IOException if there is an error reading the data.
	*/
	public static Document readXML(final InputStream inputStream, final URI baseURI, final DocumentBuilder documentBuilder) throws IOException
	{
		try
		{
			return documentBuilder.parse(inputStream);	//parse an XML document from the input stream
		}
		catch(final SAXException saxException)	//if a parsing error occurs 
		{
			throw new IOException(saxException);
		}
	}
	
	/**Writes an XML document to an output stream.
	@param outputStream The output stream to which to write the data.
	@param baseURI The base URI of the data, or <code>null</code> if no base URI is available.
	@param document The XML Mdocument to write to the given output stream.
	@exception NullPointerException if the given output stream and/or document is <code>null</code>.
	@throws IOException Thrown if there is an error writing the data.
	*/
	public void write(final OutputStream outputStream, final URI baseURI, final Document document) throws IOException
	{
		writeXML(outputStream, baseURI, document, isBOMWritten(), isFormatted());	//write the XML using the specified settings
	}
	
	/**Writes an XML document to an output stream, formatted with a byte order mark (BOM).
	@param outputStream The output stream to which to write the data.
	@param baseURI The base URI of the data, or <code>null</code> if no base URI is available.
	@param document The XML document to write to the given output stream.
	@excepion NullPointerException if the given output stream and/or document is <code>null</code>.
	@throws IOException Thrown if there is an error writing the data.
	*/
	public static void writeXML(final OutputStream outputStream, final URI baseURI, final Document document) throws IOException
	{
		writeXML(outputStream, baseURI, document, true, true);	//write the XML with formatted output and a BOM
	}
	
	
	/**Writes an XML document instance to an output stream.
	@param outputStream The output stream to which to write the data.
	@param baseURI The base URI of the data, or <code>null</code> if no base URI is available.
	@param document The XML document to write to the given output stream.
	@param bomWritten Whether a byte order mark (BOM) is written.
	@param formatted Whether output is formatted.
	@excepion NullPointerException if the given output stream and/or document is <code>null</code>.
	@throws IOException Thrown if there is an error writing the data.
	*/
	public static void writeXML(final OutputStream outputStream, final URI baseURI, final Document document, final boolean bomWritten, final boolean formatted) throws IOException
	{
		final XMLSerializer xmlSerializer=new XMLSerializer(formatted);	//create a new XML serializer
		xmlSerializer.setBOMWritten(bomWritten);	//indicate whether the BOM shoudl be written
		xmlSerializer.serialize(document);	//serialize the document
	}

  /**
   * Creates a new instance of a {@link javax.xml.parsers.DocumentBuilder}
   * using the currently configured parameters.
   *
   * @return A new instance of a DocumentBuilder.
   *
   * @throws ParserConfigurationException if a DocumentBuilder
   *   cannot be created which satisfies the configuration requested.
   */
  
  public DocumentBuilder newDocumentBuilder() throws ParserConfigurationException
  {
  	synchronized(documentBuilderFactory)
  	{
  		return documentBuilderFactory.newDocumentBuilder();
  	}
  }
  
  /**
   * Specifies that the parser produced by this code will
   * provide support for XML namespaces. By default the value of this is set
   * to <code>false</code>
   *
   * @param awareness true if the parser produced will provide support
   *                  for XML namespaces; false otherwise.
   */
  
  public void setNamespaceAware(boolean awareness)
  {
  	synchronized(documentBuilderFactory)
  	{
  		documentBuilderFactory.setNamespaceAware(awareness);
  	}
  }

  /**
   * Specifies that the parser produced by this code will
   * validate documents as they are parsed. By default the value of this
   * is set to <code>false</code>.
   * 
   * <p>
   * Note that "the validation" here means
   * <a href="http://www.w3.org/TR/REC-xml#proc-types">a validating
   * parser</a> as defined in the XML recommendation.
   * In other words, it essentially just controls the DTD validation.
   * (except the legacy two properties defined in JAXP 1.2.)
   * </p>
   * 
   * <p>
   * To use modern schema languages such as W3C XML Schema or
   * RELAX NG instead of DTD, you can configure your parser to be
   * a non-validating parser by leaving the {@link #setValidating(boolean)}
   * method <code>false</code>, then use the {@link #setSchema(Schema)}
   * method to associate a schema to a parser.
   * </p>
   * 
   * @param validating true if the parser produced will validate documents
   *                   as they are parsed; false otherwise.
   */
  
  public void setValidating(boolean validating)
  {
  	synchronized(documentBuilderFactory)
  	{
  		documentBuilderFactory.setValidating(validating);
  	}
  }

  /**
   * Specifies that the parsers created by this  factory must eliminate
   * whitespace in element content (sometimes known loosely as
   * 'ignorable whitespace') when parsing XML documents (see XML Rec
   * 2.10). Note that only whitespace which is directly contained within
   * element content that has an element only content model (see XML
   * Rec 3.2.1) will be eliminated. Due to reliance on the content model
   * this setting requires the parser to be in validating mode. By default
   * the value of this is set to <code>false</code>.
   *
   * @param whitespace true if the parser created must eliminate whitespace
   *                   in the element content when parsing XML documents;
   *                   false otherwise.
   */

  public void setIgnoringElementContentWhitespace(boolean whitespace)
  {
  	synchronized(documentBuilderFactory)
  	{
  		documentBuilderFactory.setIgnoringElementContentWhitespace(whitespace);
  	}
  }

  /**
   * Specifies that the parser produced by this code will
   * expand entity reference nodes. By default the value of this is set to
   * <code>true</code>
   *
   * @param expandEntityRef true if the parser produced will expand entity
   *                        reference nodes; false otherwise.
   */
  
  public void setExpandEntityReferences(boolean expandEntityRef)
  {
  	synchronized(documentBuilderFactory)
  	{
  		documentBuilderFactory.setExpandEntityReferences(expandEntityRef);
  	}
  }

  /**
   * <p>Specifies that the parser produced by this code will
   * ignore comments. By default the value of this is set to <code>false
   * </code>.</p>
   * 
   * @param ignoreComments <code>boolean</code> value to ignore comments during processing
   */
  
  public void setIgnoringComments(boolean ignoreComments)
  {
  	synchronized(documentBuilderFactory)
  	{
  		documentBuilderFactory.setIgnoringComments(ignoreComments);
  	}
  }

  /**
   * Specifies that the parser produced by this code will
   * convert CDATA nodes to Text nodes and append it to the
   * adjacent (if any) text node. By default the value of this is set to
   * <code>false</code>
   *
   * @param coalescing  true if the parser produced will convert CDATA nodes
   *                    to Text nodes and append it to the adjacent (if any)
   *                    text node; false otherwise.
   */
  
  public void setCoalescing(boolean coalescing)
  {
  	synchronized(documentBuilderFactory)
  	{
  		documentBuilderFactory.setCoalescing(coalescing);
  	}
  }

  /**
   * Indicates whether or not the factory is configured to produce
   * parsers which are namespace aware.
   *
   * @return  true if the factory is configured to produce parsers which
   *          are namespace aware; false otherwise.
   */
  
  public boolean isNamespaceAware()
  {
  	synchronized(documentBuilderFactory)
  	{
  		return documentBuilderFactory.isNamespaceAware();
  	}
  }

  /**
   * Indicates whether or not the factory is configured to produce
   * parsers which validate the XML content during parse.
   *
   * @return  true if the factory is configured to produce parsers
   *          which validate the XML content during parse; false otherwise.
   */
  
  public boolean isValidating()
  {
  	synchronized(documentBuilderFactory)
  	{
  		return documentBuilderFactory.isValidating();
  	}
  }

  /**
   * Indicates whether or not the factory is configured to produce
   * parsers which ignore ignorable whitespace in element content.
   *
   * @return  true if the factory is configured to produce parsers
   *          which ignore ignorable whitespace in element content;
   *          false otherwise.
   */
  
  public boolean isIgnoringElementContentWhitespace()
  {
  	synchronized(documentBuilderFactory)
  	{
  		return documentBuilderFactory.isIgnoringElementContentWhitespace();
  	}
  }

  /**
   * Indicates whether or not the factory is configured to produce
   * parsers which expand entity reference nodes.
   *
   * @return  true if the factory is configured to produce parsers
   *          which expand entity reference nodes; false otherwise.
   */
  
  public boolean isExpandEntityReferences()
  {
  	synchronized(documentBuilderFactory)
  	{
  		return documentBuilderFactory.isExpandEntityReferences();
  	}
  }

  /**
   * Indicates whether or not the factory is configured to produce
   * parsers which ignores comments.
   *
   * @return  true if the factory is configured to produce parsers
   *          which ignores comments; false otherwise.
   */
  
  public boolean isIgnoringComments()
  {
  	synchronized(documentBuilderFactory)
  	{
  		return documentBuilderFactory.isIgnoringComments();
  	}
  }

  /**
   * Indicates whether or not the factory is configured to produce
   * parsers which converts CDATA nodes to Text nodes and appends it to
   * the adjacent (if any) Text node.
   *
   * @return  true if the factory is configured to produce parsers
   *          which converts CDATA nodes to Text nodes and appends it to
   *          the adjacent (if any) Text node; false otherwise.
   */
  
  public boolean isCoalescing()
  {
  	synchronized(documentBuilderFactory)
  	{
  		return documentBuilderFactory.isCoalescing();
  	}
  }

  /**
   * Allows the user to set specific attributes on the underlying
   * implementation.
   *
   * @param name The name of the attribute.
   * @param value The value of the attribute.
   *
   * @throws IllegalArgumentException thrown if the underlying
   *   implementation doesn't recognize the attribute.
   */
  public void setAttribute(String name, Object value)
  {
  	synchronized(documentBuilderFactory)
  	{
  		documentBuilderFactory.setAttribute(name, value);
  	}
  }

  /**
   * Allows the user to retrieve specific attributes on the underlying
   * implementation.
   *
   * @param name The name of the attribute.
   *
   * @return value The value of the attribute.
   *
   * @throws IllegalArgumentException thrown if the underlying
   *   implementation doesn't recognize the attribute.
   */
  public Object getAttribute(String name)
  {
  	synchronized(documentBuilderFactory)
  	{
  		return documentBuilderFactory.getAttribute(name);
  	}
  }
              
	/**
	 * <p>Set a feature for this <code>DocumentBuilderFactory</code> and <code>DocumentBuilder</code>s created by this factory.</p>
	 * 
	 * <p>
	 * Feature names are fully qualified {@link java.net.URI}s.
	 * Implementations may define their own features.
	 * A {@link ParserConfigurationException} is thrown if this <code>DocumentBuilderFactory</code> or the
	 * <code>DocumentBuilder</code>s it creates cannot support the feature.
	 * It is possible for a <code>DocumentBuilderFactory</code> to expose a feature value but be unable to change its state.
	 * </p>
	 * 
	 * <p>
	 * All implementations are required to support the {@link javax.xml.XMLConstants#FEATURE_SECURE_PROCESSING} feature.
	 * When the feature is:</p>
	 * <ul>
	 *   <li>
	 *     <code>true</code>: the implementation will limit XML processing to conform to implementation limits.
	 *     Examples include enity expansion limits and XML Schema constructs that would consume large amounts of resources.
	 *     If XML processing is limited for security reasons, it will be reported via a call to the registered
	 *    {@link org.xml.sax.ErrorHandler#fatalError(SAXParseException exception)}.
	 *     See {@link  DocumentBuilder#setErrorHandler(org.xml.sax.ErrorHandler errorHandler)}.
	 *   </li>
	 *   <li>
	 *     <code>false</code>: the implementation will processing XML according to the XML specifications without
	 *     regard to possible implementation limits.
	 *   </li>
	 * </ul>
	 * 
	 * @param name Feature name.
	 * @param value Is feature state <code>true</code> or <code>false</code>.
	 *  
	 * @throws ParserConfigurationException if this <code>DocumentBuilderFactory</code> or the <code>DocumentBuilder</code>s
	 *   it creates cannot support this feature.
	   * @throws NullPointerException If the <code>name</code> parameter is null.
	 */
	public void setFeature(String name, boolean value) throws ParserConfigurationException
	{
  	synchronized(documentBuilderFactory)
  	{
  		documentBuilderFactory.setFeature(name, value);
  	}
	}

	/**
	 * <p>Get the state of the named feature.</p>
	 * 
	 * <p>
	 * Feature names are fully qualified {@link java.net.URI}s.
	 * Implementations may define their own features.
	 * An {@link ParserConfigurationException} is thrown if this <code>DocumentBuilderFactory</code> or the
	 * <code>DocumentBuilder</code>s it creates cannot support the feature.
	 * It is possible for an <code>DocumentBuilderFactory</code> to expose a feature value but be unable to change its state.
	 * </p>
	 * 
	 * @param name Feature name.
	 * 
	 * @return State of the named feature.
	 * 
	 * @throws ParserConfigurationException if this <code>DocumentBuilderFactory</code>
	 *   or the <code>DocumentBuilder</code>s it creates cannot support this feature.
	 */
	public boolean getFeature(String name) throws ParserConfigurationException
	{
  	synchronized(documentBuilderFactory)
  	{
  		return documentBuilderFactory.getFeature(name);
  	}
	}

  /**
   * Gets the {@link Schema} object specified through
   * the {@link #setSchema(Schema schema)} method.
   *
   * @return
   *      the {@link Schema} object that was last set through
   *      the {@link #setSchema(Schema)} method, or null
   *      if the method was not invoked since a {@link DocumentBuilderFactory}
   *      is created.
   *
   * @throws UnsupportedOperationException When implementation does not
   *   override this method.
   *
   * @since 1.5
   */
  public Schema getSchema()
  {
  	synchronized(documentBuilderFactory)
  	{
  		return documentBuilderFactory.getSchema();
  	}
 	}
  
  /**
   * <p>Set the {@link Schema} to be used by parsers created
   * from this factory.
   * 
   * <p>
   * When a {@link Schema} is non-null, a parser will use a validator
   * created from it to validate documents before it passes information
   * down to the application.
   * 
   * <p>When errors are found by the validator, the parser is responsible
   * to report them to the user-specified {@link org.xml.sax.ErrorHandler}
   * (or if the error handler is not set, ignore them or throw them), just
   * like any other errors found by the parser itself.
   * In other words, if the user-specified {@link org.xml.sax.ErrorHandler}
   * is set, it must receive those errors, and if not, they must be
   * treated according to the implementation specific
   * default error handling rules.
   * 
   * <p>
   * A validator may modify the outcome of a parse (for example by
   * adding default values that were missing in documents), and a parser
   * is responsible to make sure that the application will receive
   * modified DOM trees.  
   * 
   * <p>
   * Initialy, null is set as the {@link Schema}. 
   * 
   * <p>
   * This processing will take effect even if
   * the {@link #isValidating()} method returns <code>false</code>.
   * 
   * <p>It is an error to use
   * the <code>http://java.sun.com/xml/jaxp/properties/schemaSource</code>
   * property and/or the <code>http://java.sun.com/xml/jaxp/properties/schemaLanguage</code>
   * property in conjunction with a {@link Schema} object.
   * Such configuration will cause a {@link ParserConfigurationException}
   * exception when the {@link #newDocumentBuilder()} is invoked.</p>
   *
   *  
   * <h4>Note for implmentors</h4>
   *
   * <p>
   * A parser must be able to work with any {@link Schema}
   * implementation. However, parsers and schemas are allowed
   * to use implementation-specific custom mechanisms
   * as long as they yield the result described in the specification.
   * </p>
   *
   * @param schema <code>Schema</code> to use or <code>null</code>
   *   to remove a schema.
   *
   * @throws UnsupportedOperationException When implementation does not
   *   override this method.
   *
   * @since 1.5
   */
  public void setSchema(Schema schema)
  {
  	synchronized(documentBuilderFactory)
  	{
  		documentBuilderFactory.setSchema(schema);
  	}
 	}  
  
  /**
   * <p>Set state of XInclude processing.</p>
   * 
   * <p>If XInclude markup is found in the document instance, should it be
   * processed as specified in <a href="http://www.w3.org/TR/xinclude/">
   * XML Inclusions (XInclude) Version 1.0</a>.</p>
   * 
   * <p>XInclude processing defaults to <code>false</code>.</p>
   * 
   * @param state Set XInclude processing to <code>true</code> or
   *   <code>false</code>
   *
   * @throws UnsupportedOperationException When implementation does not
   *   override this method.
   *
   * @since 1.5
   */
  public void setXIncludeAware(final boolean state)
  {
  	synchronized(documentBuilderFactory)
  	{
  		documentBuilderFactory.setXIncludeAware(state);
  	}
  }

  /**
   * <p>Get state of XInclude processing.</p>
   * 
   * @return current state of XInclude processing
   *
   * @throws UnsupportedOperationException When implementation does not
   *   override this method.
   *
   * @since 1.5
   */
  public boolean isXIncludeAware()
  {
  	synchronized(documentBuilderFactory)
  	{
  		return documentBuilderFactory.isXIncludeAware();
  	}
  }
	
}
