package com.garretwilson.text.xml;

import java.io.*;
import java.util.*;
import org.w3c.dom.*;
import com.garretwilson.text.CharacterEncodingConstants;
import com.garretwilson.lang.CharacterUtilities;
import com.garretwilson.lang.StringBufferUtilities;
import com.garretwilson.rdf.dublincore.DCConstants;
import com.garretwilson.text.xml.oeb.OEBConstants;
import com.garretwilson.text.xml.oeb.OEB2Constants;
import com.garretwilson.assess.qti.QTIConstants;
import com.garretwilson.rdf.RDFConstants;
import com.garretwilson.rdf.rdfs.RDFSConstants;
import com.garretwilson.text.xml.schema.XMLSchemaConstants;
import com.garretwilson.text.xml.xhtml.XHTMLConstants;
import com.garretwilson.text.xml.xlink.XLinkConstants;
//G***fix import com.garretwilson.text.xml.soap.SOAPConstants;
import com.garretwilson.rdf.xpackage.XPackageConstants;
import com.garretwilson.util.PropertyUtilities;

//G***del all the XMLUndefinedEntityReferenceException throws when we don't need them anymore, in favor of XMLWellFormednessException

/**Class which serializes an XML document to a byte-oriented output stream.
	Has the option of automatically formatting the output in a hierarchical
	structure with tabs or other strings.
@see XMLProcessor
@author Garret Wilson
*/
public class XMLSerializer implements XMLConstants
{

	/**Whether the output should be formatted.*/
	public final static String FORMAT_OUTPUT_OPTION="formatOutput";

		/**Default to unformatted output.*/
		public final static boolean FORMAT_OUTPUT_OPTION_DEFAULT=false;

	/**Whether Unicode control characters should be XML-encoded.*/
	public final static String XML_ENCODE_CONTROL_OPTION="xmlEncodeControl";

		/**Default to not XML-encoding control characters.*/
		public final static boolean XML_ENCODE_CONTROL_OPTION_DEFAULT=false;

	/**Whether extended characters (above 127) should be XML-encoded.*/
	public final static String XML_ENCODE_NON_ASCII_OPTION="xmlEncodeNonASCII";

		/**Default to not XML-encoding extended characters.*/
		public final static boolean XML_ENCODE_NON_ASCII_OPTION_DEFAULT=false;

	/**Whether private use Unicode characters should be XML-encoded.*/
	public final static String XML_ENCODE_PRIVATE_USE_OPTION="xmlEncodePrivateUse";

		/**Default to not XML-encoding private use characters.*/
		public final static boolean XML_ENCODE_PRIVATE_USE_OPTION_DEFAULT=false;

	/**Whether characters which match a one-character predefined entity should be
		encoded using that entity.*/
	public final static String USE_ENTITIES_OPTION="useEntities";

		/**Default to using one-character predefined entities for encoding.*/
		public final static boolean USE_ENTITIES_OPTION_DEFAULT=true;

	/**Whether the output should be formatted.*/
	private boolean formatted=FORMAT_OUTPUT_OPTION_DEFAULT;

		/**@return Whether the output should be formatted.*/
		public boolean isFormatted() {return formatted;}

		/**Sets whether the output should be formatted.
		@param newFormatted <code>true</code> if the output should be formatted, else <code>false</code>.
		*/
		public void setFormatted(final boolean newFormatted) {formatted=newFormatted;}

	/**Whether Unicode control characters should be XML-encoded.*/
	private boolean xmlEncodeControl=XML_ENCODE_CONTROL_OPTION_DEFAULT;

		/**@return Whether Unicode control characters should be XML-encoded.
		@see #isUseEntities
		*/
		public boolean isXMLEncodeControl() {return xmlEncodeControl;}

		/**Sets whether Unicode control characters should be XML-encoded.
		@param newXMLEncodeControl <code>true</code> if control characters should be XML-encoded, else <code>false</code>.
		@see setUseEntities
		*/
		public void setXMLEncodeControl(final boolean newXMLEncodeControl) {xmlEncodeControl=newXMLEncodeControl;}

	/**Whether extended characters (above 127) should be XML-encoded.*/
	private boolean xmlEncodeNonASCII=XML_ENCODE_NON_ASCII_OPTION_DEFAULT;

		/**@return Whether extended characters (above 127) should be XML-encoded.
		@see #isUseEntities
		*/
		public boolean isXMLEncodeNonASCII() {return xmlEncodeNonASCII;}

		/**Sets whether extended characters (above 127) should be XML-encoded.
		@param newXMLEncodeNonASCII <code>true</code> if extended characters should be XML-encoded, else <code>false</code>.
		@see setUseEntities
		*/
		public void setXMLEncodeNonASCII(final boolean newXMLEncodeNonASCII) {xmlEncodeNonASCII=newXMLEncodeNonASCII;}

	/**Whether private use Unicode characters should be XML-encoded.*/
	private boolean xmlEncodePrivateUse=XML_ENCODE_PRIVATE_USE_OPTION_DEFAULT;

		/**@return Whether private use Unicode characters should be XML-encoded.
		@see #isUseEntities
		*/
		public boolean isXMLEncodePrivateUse() {return xmlEncodePrivateUse;}

		/**Sets whether private use Unicode characters should be XML-encoded.
		@param newXMLEncodePrivateUse <code>true</code> if private use characters should be XML-encoded, else <code>false</code>.
		@see setUseEntities
		*/
		public void setXMLEncodePrivateUse(final boolean newXMLEncodePrivateUse) {xmlEncodePrivateUse=newXMLEncodePrivateUse;}

	/**Whether characters which match a one-character predefined entity should be
		encoded using that entity.*/
	private boolean useEntities=USE_ENTITIES_OPTION_DEFAULT;

		/**@return Whether one-character entities will be used when possible.
		@see #isXMLEncode
		*/
		public boolean isUseEntities() {return useEntities;}

		/**Sets whether characters which match a one-character predefined entity
		  should be encoded using that entity. This will override the XML-encoding
			settings when applicable.
		@param newUseEntities <code>true</code> if available entities should be
			used to encode characters.
		@see #setXMLEncode
		*/
		public void setUseEntities(final boolean newUseEntities) {useEntities=newUseEntities;}

	/**Sets the options based on the contents of the option properties.
	@param options The properties which contain the options.
	*/
	public void setOptions(final Properties options)
	{
//G***del Debug.notify("XMLSerializer: "+options);  //G***del
		setFormatted(PropertyUtilities.getBooleanProperty(options, FORMAT_OUTPUT_OPTION, FORMAT_OUTPUT_OPTION_DEFAULT));
		setUseEntities(PropertyUtilities.getBooleanProperty(options, USE_ENTITIES_OPTION, USE_ENTITIES_OPTION_DEFAULT));
		setXMLEncodeControl(PropertyUtilities.getBooleanProperty(options, XML_ENCODE_CONTROL_OPTION, XML_ENCODE_CONTROL_OPTION_DEFAULT));
		setXMLEncodeNonASCII(PropertyUtilities.getBooleanProperty(options, XML_ENCODE_NON_ASCII_OPTION, XML_ENCODE_NON_ASCII_OPTION_DEFAULT));
		setXMLEncodePrivateUse(PropertyUtilities.getBooleanProperty(options, XML_ENCODE_PRIVATE_USE_OPTION, XML_ENCODE_PRIVATE_USE_OPTION_DEFAULT));
	}

	/**The string to use for horizontally aligning the elements if formatting is
		turned on. Defaults to a single tab character.
	*/
	private String horizontalAlignString="\t";

		/**@return The string to use for horizontally aligning the elements if formatting is turned on.
		@see #isFormatted
		*/
		public String getHorizontalAlignString() {return horizontalAlignString;}

		/**Sets the string to use for horizontally aligning the elements if formatting is turned on..
		@param newHorizontalAlignString The horizontal alignment string.
		@see #setFormatted
		*/
		public void setHorizontalAlignString(final String newHorizontalAlignString) {horizontalAlignString=newHorizontalAlignString;}

	/**The current level of nesting during document serialization if output is formatted.*/
	protected int nestLevel=-1;

	/**The string representing one-character entity values. The
		<code>entityNames</code> array will contain the names of the corresponding
		entities, each at the same index as the corresponding character value.
	@see #entityNames
	*/
	private String entityCharacterValues;

	/**An array of names corresponding to character values in the
		<code>entityCharacterValue</code> string at the same indexes.
	@see #entityCharacterValues
	*/
	private String[] entityNames;

	/**The options which specify how serialization should occur.*/
//G***del	private final Properties options;

		/**@return The options which specify how serialization should occur.*/
//G***del		public Properties getOptions() {return options;}

	/**@return A map of default namespace prefixes for known namespaces, keyed to
		namespace URIs, to be used for serializing namespace references.
	*/
	public static Map createNamespacePrefixMap()	//G***maybe later use real URIs
	{
		final Map map=new HashMap();  //create a new hash map
		map.put(XMLConstants.XML_NAMESPACE_URI.toString(), XMLConstants.XML_NAMESPACE_PREFIX); //XML
		map.put(XMLConstants.XML_NAMESPACE_URI.toString(), XMLConstants.XML_NAMESPACE_PREFIX); //XML namespaces
		map.put(DCConstants.DCMI11_ELEMENTS_NAMESPACE_URI.toString(), DCConstants.DCMI_ELEMENTS_NAMESPACE_PREFIX); //Dublin Core
		map.put(OEBConstants.OEB1_DOCUMENT_NAMESPACE_URI.toString(), OEBConstants.OEB1_DOCUMENT_NAMESPACE_PREFIX); //OEB 1
		map.put(OEB2Constants.OEB2_PACKAGE_NAMESPACE_URI.toString(), OEB2Constants.OEB2_PACKAGE_NAMESPACE_PREFIX); //OEB 2
		map.put(QTIConstants.QTI_1_1_NAMESPACE_URI.toString(), QTIConstants.QTI_NAMESPACE_PREFIX); //QTI
		map.put(RDFConstants.RDF_NAMESPACE_URI.toString(), RDFConstants.RDF_NAMESPACE_PREFIX); //RDF
		map.put(RDFSConstants.RDFS_NAMESPACE_URI.toString(), RDFSConstants.RDFS_NAMESPACE_PREFIX); //RDFS
//G***add SOAP
		map.put(XMLSchemaConstants.XML_SCHEMA_NAMESPACE_URI.toString(), XMLSchemaConstants.XML_SCHEMA_NAMESPACE_PREFIX); //XML Schema
		map.put(XHTMLConstants.XHTML_NAMESPACE_URI.toString(), XHTMLConstants.XHTML_NAMESPACE_PREFIX); //XHTML
		map.put(XLinkConstants.XLINK_NAMESPACE_URI.toString(), XLinkConstants.XLINK_NAMESPACE_PREFIX); //XLink
		map.put(XPackageConstants.XPACKAGE_NAMESPACE_URI.toString(), XPackageConstants.XPACKAGE_NAMESPACE_PREFIX); //XPackage
		map.put(XPackageConstants.XML_PROPERTIES_NAMESPACE_URI.toString(), XPackageConstants.XML_PROPERTIES_NAMESPACE_PREFIX); //XPackage XML properties
		return map; //return the map we constructed
	}

	/**Retrieves the prefix to use for the given namespace, using the provided
		namespace prefix map. If a namespace is unrecognized, a new one will be
		created and stored for future use.
	@param namespacePrefixMap The map of prefix strings, each keyed to a namespace
		URI.
	@param namespaceURI The namespace URI for which a prefix should be returned
	@return A prefix for use with the given namespace.
	@see #createNamespacePrefixMap
	*/
	public static String getNamespacePrefix(final Map namespacePrefixMap, final String namespaceURI)
	{
		String prefix=(String)namespacePrefixMap.get(namespaceURI);  //get the prefix keyed by the namespace
		if(prefix==null)  //if there is no prefix for this namespace
		{
			prefix="namespace"+namespacePrefixMap.size()+1; //create a unique namespace prefix
			namespacePrefixMap.put(namespaceURI, prefix); //store the prefix in the map
		}
	  return prefix;  //return the prefix we found or created
	}

	/**Default constructor for unformatted output.*/
	public XMLSerializer()
	{
		initializeEntityLookup(null); //always initialize the entity lookup, so that at least the five XML entities will be included in the table in case they serialize only part of a document G***fix better so that serializing part of the document somehow initializes these
//G***del		this(new Properties()); //do the default construction with default properties
//G***del	  this(false);  //default to unformatted output
	}

	/**Constructor that specifies serialize options.
	@param options The options to use for serialization.
	*/
	public XMLSerializer(final Properties options)
	{
		this(); //do the default construction
		setOptions(options);  //set the options from the properties
	}

	/**Constructor for an optionally formatted serializer.
	@param formatted Whether the serializer should be formatted.
	*/
	public XMLSerializer(final boolean formatted)
	{
		this(); //do the default construction
		setFormatted(formatted);	//set whether the output should be formatted
	}

	/**Serializes the specified document to the given output stream using the
		UTF-8 encoding.
	@param document The XML document to serialize.
	@param outputStream The stream into which the document should be serialized.
	@exception IOException Thrown if an I/O error occurred.
	@exception UnsupportedEncodingException Thrown if the UTF-8 encoding not recognized.
	*/
	public void serialize(final Document document, final OutputStream outputStream) throws UnsupportedEncodingException, IOException
	{
		serialize(document, outputStream, CharacterEncodingConstants.UTF_8);	//serialize the document, defaulting to UTF-8
	}

	/**Serializes the specified document a string using the UTF-8 encoding.
	@param document The XML document to serialize.
	@return A string containing the serialized XML data.
	@exception IOException Thrown if an I/O error occurred.
	@exception UnsupportedEncodingException Thrown if the UTF-8 encoding not recognized.
	*/
	public String serialize(final Document document) throws UnsupportedEncodingException, IOException
	{
		final ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();  //create an output stream for receiving the XML data
		serialize(document, byteArrayOutputStream, CharacterEncodingConstants.UTF_8);  //serialize the package description document to the output stream using UTF-8
		return byteArrayOutputStream.toString(CharacterEncodingConstants.UTF_8);  //convert the byte array to a string, using the UTF-8 encoding, and return it
	}

	/**Initializes the internal entity lookup with the entities defined in the
		specified map. The given entities will only be placed in the lookup table
		if the "useEntities" option is turned on, and then only entities which
		represent one-character entities will be used.
		If "useEntities" is turned on, the five entities guaranteed to be available
		in XML (<code>&lt;&amp;amp;&gt;</code>, <code>&lt;&amp;lt;&gt;</code>,
		<code>&lt;&amp;gt;&gt;</code>, <code>&lt;&amp;apos;&gt;</code>,
		<code>&lt;&amp;quot;&gt;</code>) will	be included in the internal
		lookup table if these entities do not exist in the specified entity map,
		using their default XML values.
	@param entityMap The entity map which contains the entities to be placed in
		the internal lookup table, or <code>null</code> if entities are not
		available, in which case only the default XML entities will be used.
	@see #isUseEntities
	@see #setUseEntities
	 */
	protected void initializeEntityLookup(final NamedNodeMap entityMap)
	{
//G***del Debug.trace("looking at entity map with entries: ", entityMap.getLength()); //G***del
		final List entityNameList=new ArrayList();  //create an array to hold the entity names we use
		final StringBuffer entityCharacterValueStringBuffer=new StringBuffer(); //create a buffer to hold all the character values
		if(isUseEntities()) //if we were asked to use their entities
		{
			if(entityMap!=null) //if we were provided a valid entity map
			{
				final int entityCount=entityMap.getLength();  //find out how many entities there are
				for(int i=0; i<entityCount; ++i)  //look at each of the entities
				{
	//G***del Debug.trace("looking at entity: ", i);
					final Entity entity=(Entity)entityMap.item(i);  //get a reference to this entity
	//G***del Debug.trace("entity: ", entity);
					final int entityChildCount=entity.getChildNodes().getLength();  //see how many children this entity has (entities store text content in child text nodes not in the entity value)
					if(entityChildCount==1) //if there is only one child node
					{
						final Node entityChildNode=entity.getFirstChild();  //get a reference to the first child of the entity
						if(entityChildNode.getNodeType()==entityChildNode.TEXT_NODE)  //if this is a text node
						{
							final String entityValue=((Text)entityChildNode).getData(); //get the data of the node, which represents the replacement value of the entity
							if(entityValue.length()==1) //if this entity represents exactly one character
							{
								entityNameList.add(entity.getNodeName()); //add the entity's name to the list
								entityCharacterValueStringBuffer.append(entityValue.charAt(0)); //add the one-character value to the value buffer
							}
						}
					}
				}
			}
				//add the "amp" entity if needed
			if(StringBufferUtilities.indexOf(entityCharacterValueStringBuffer, ENTITY_AMP_VALUE)<0) //if this entity value isn't defined
			{
				entityNameList.add(ENTITY_AMP_NAME);  //add the default entity name
				entityCharacterValueStringBuffer.append(ENTITY_AMP_VALUE);  //add the default entity value
			}
				//add the "lt" entity if needed
			if(StringBufferUtilities.indexOf(entityCharacterValueStringBuffer, ENTITY_LT_VALUE)<0) //if this entity value isn't defined
			{
				entityNameList.add(ENTITY_LT_NAME);  //add the default entity name
				entityCharacterValueStringBuffer.append(ENTITY_LT_VALUE);  //add the default entity value
			}
				//add the "gt" entity if needed
			if(StringBufferUtilities.indexOf(entityCharacterValueStringBuffer, ENTITY_GT_VALUE)<0) //if this entity value isn't defined
			{
				entityNameList.add(ENTITY_GT_NAME);  //add the default entity name
				entityCharacterValueStringBuffer.append(ENTITY_GT_VALUE);  //add the default entity value
			}
				//add the "apos" entity if needed
			if(StringBufferUtilities.indexOf(entityCharacterValueStringBuffer, ENTITY_APOS_VALUE)<0) //if this entity value isn't defined
			{
				entityNameList.add(ENTITY_APOS_NAME);  //add the default entity name
				entityCharacterValueStringBuffer.append(ENTITY_APOS_VALUE);  //add the default entity value
			}
				//add the "quot" entity if needed
			if(StringBufferUtilities.indexOf(entityCharacterValueStringBuffer, ENTITY_QUOT_VALUE)<0) //if this entity value isn't defined
			{
				entityNameList.add(ENTITY_QUOT_NAME);  //add the default entity name
				entityCharacterValueStringBuffer.append(ENTITY_QUOT_VALUE);  //add the default entity value
			}
		}
//G***del Debug.trace("Found entities to serialize: ", entityCharacterValueStringBuffer.length());  //G***del
		entityNames=(String[])entityNameList.toArray(new String[entityNameList.size()]);  //convert the entities in the list to an array
		entityCharacterValues=entityCharacterValueStringBuffer.toString();  //convert the values into one searchable string
	}

	/**Serializes the specified document to the given output stream using the
		specified encoding.
	@param document The XML document to serialize.
	@param outputStream The stream into which the document should be serialized.
	@param encoding The encoding format to use when serializing.
	@exception IOException Thrown if an I/O error occurred.
	@exception UnsupportedEncodingException Thrown if the specified encoding is not recognized.
	*/
	public void serialize(final Document document, final OutputStream outputStream, final String encoding) throws IOException, UnsupportedEncodingException
	{
		nestLevel=0;	//show that we haven't started nesting yet
		final BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(outputStream, encoding));	//create a new writer based on our encoding
		writeProlog(document, writer, encoding);	//write the prolog
		final DocumentType documentType=document.getDoctype();	//get the document type, if there is one
		if(documentType!=null)	//if there is a document type
		{
		  initializeEntityLookup(documentType.getEntities()); //initialize the entity lookup based on the provided entities
			write(documentType, writer);	//write the document type
		}
		else  //if there is no document type
		  initializeEntityLookup(null); //always initialize the entity lookup, so that at least the five XML entities will be included in the table
	  writeProcessingInstructions(document, writer);  //write any processing instructions
		write(document.getDocumentElement(), writer);	//write the root element and all elements below it
		writer.newLine();	//add a newline in the default format
		writer.flush();	//flush any data we've buffered
	}

	/**Serializes the specified element and its children to the given output
		stream using the UTF-8 encoding.
	@param element The XML element to serialize.
	@param outputStream The stream into which the element should be serialized.
	@exception IOException Thrown if an I/O error occurred.
	@exception UnsupportedEncodingException Thrown if the UTF-8 encoding is not recognized.
	*/
	public void serialize(final Element element, final OutputStream outputStream) throws IOException, UnsupportedEncodingException
	{
		serialize(element, outputStream, CharacterEncodingConstants.UTF_8);  //serialize the element using the UTF-8 encoding
	}

	/**Serializes the specified element and its children to the given output
		stream using the specified encoding.
	@param element The XML element to serialize.
	@param outputStream The stream into which the element should be serialized.
	@param encoding The encoding format to use when serializing.
	@exception IOException Thrown if an I/O error occurred.
	@exception UnsupportedEncodingException Thrown if the specified encoding is not recognized.
	*/
	public void serialize(final Element element, final OutputStream outputStream, final String encoding) throws IOException, UnsupportedEncodingException
	{
		nestLevel=0;	//show that we haven't started nesting yet
		final BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(outputStream, encoding));	//create a new writer based on our encoding
		write(element, writer);	//write the element and all elements below it
		writer.newLine();	//add a newline in the default format
		writer.flush();	//flush any data we've buffered
	}

	/**Serializes the content (all child nodes and their descendants) of a
		specified element and its children to the given output stream using the
		UTF-8 encoding.
	@param element The XML element the content of which to serialize.
	@param outputStream The stream into which the element content should be serialized.
	@exception IOException Thrown if an I/O error occurred.
	@exception UnsupportedEncodingException Thrown if the UTF-8 encoding is not recognized.
	*/
	public void serializeContent(final Element element, final OutputStream outputStream) throws IOException, UnsupportedEncodingException
	{
		serializeContent(element, outputStream, CharacterEncodingConstants.UTF_8); //serialize the content using UTF-8 as the encoding
	}

	/**Serializes the content (all child nodes and their descendants) of a
		specified element and its children to the given output stream using the
		specified encoding.
	@param element The XML element the content of which to serialize.
	@param outputStream The stream into which the element content should be serialized.
	@param encoding The encoding format to use when serializing.
	@exception IOException Thrown if an I/O error occurred.
	@exception UnsupportedEncodingException Thrown if the specified encoding is not recognized.
	*/
	public void serializeContent(final Element element, final OutputStream outputStream, final String encoding) throws IOException, UnsupportedEncodingException
	{
		nestLevel=0;	//show that we haven't started nesting yet
		final BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(outputStream, encoding));	//create a new writer based on our encoding
		writeContent(element, writer);	//write all children of the element
		writer.newLine();	//add a newline in the default format
		writer.flush();	//flush any data we've buffered
	}

	/**Serializes the specified document to the given writer.
	@param document The XML document to write.
	@param writer The writer into which the document should be written.
	@exception IOException Thrown if an I/O error occurred.
	*/
/*G***del if not needed
	protected static void write(final Document document, final BufferedWriter writer)
	{

	}
*/

	/**Serializes the specified document's to the given writer.
	@param document The XML document the prolog of which to serialize.
	@param writer The writer into which the prolog should be written.
	@param encoding The encoding format in use.
	@exception IOException Thrown if an I/O error occurred.
	*/
	protected void writeProlog(final Document document, final BufferedWriter writer, final String encoding) throws IOException
	{
		writer.write(XML_DECL_START+SPACE_CHAR+
			VERSIONINFO_NAME+EQUAL_CHAR+DOUBLE_QUOTE_CHAR+XML_VERSION+DOUBLE_QUOTE_CHAR+SPACE_CHAR+
			ENCODINGDECL_NAME+EQUAL_CHAR+DOUBLE_QUOTE_CHAR+encoding+DOUBLE_QUOTE_CHAR+
			//G***fix standalone writing here
			XML_DECL_END);	//write the XML prolog
		writer.newLine();	//add a newline in the default format
	}

	/**Serializes the document's processing instrucitons to the given writer.
	@param document The XML document the processing instructions of which to serialize.
	@param writer The writer into which the processing instructions should be written.
	@exception IOException Thrown if an I/O error occurred.
	*/
	protected void writeProcessingInstructions(final Document document, final BufferedWriter writer) throws IOException
	{
//G***del		final Element rootElement=document.getDocumentElement();  //get the document element
		final NodeList childNodeList=document.getChildNodes(); //get the list of document child nodes
		final int childNodeCount=childNodeList.getLength(); //find out how many document child nodes there are
		for(int childIndex=0; childIndex<childNodeCount; childIndex++)	//look at each document child node
		{
			final Node node=childNodeList.item(childIndex);	//look at this document child node
			if(node.getNodeType()==Node.PROCESSING_INSTRUCTION_NODE)  //if this is a processing instruction
				write((ProcessingInstruction)node, writer); //write the processing instruction node
		}
	}

	/**Serializes the document type to the given writer.
	@param documentType The XML document type to serialize.
	@param writer The writer into which the document type should be written.
	@exception IOException Thrown if an I/O error occurred.
	*/
	protected void write(final DocumentType documentType, final BufferedWriter writer) throws IOException
	{
		writer.write(DOCTYPE_DECL_START+SPACE_CHAR+documentType.getName()+SPACE_CHAR);	//write the beginning of the document type declaration
		final String publicID=documentType.getPublicId();	//get the public ID, if there is one
		if(publicID!=null)	//if there is a public ID
			writer.write(PUBLIC_ID_NAME+SPACE_CHAR+DOUBLE_QUOTE_CHAR+publicID+DOUBLE_QUOTE_CHAR+SPACE_CHAR);	//write the public ID name and its public literal
		else	//if there is no public ID
			writer.write(SYSTEM_ID_NAME+SPACE_CHAR);	//write the system ID name
		writer.write(DOUBLE_QUOTE_CHAR+documentType.getSystemId()+DOUBLE_QUOTE_CHAR);	//always write the system literal
		writer.write(DOCTYPE_DECL_END);	//write the end of the document type declaration
		writer.newLine();	//add a newline in the default format
	}

	/**Serializes the given processing instruction to the given writer.
	@param processingInstruction The XML processing instruction to serialize.
	@param writer The writer into which the processing instruction should be written.
	@exception IOException Thrown if an I/O error occurred.
	*/
	protected void write(final ProcessingInstruction processingInstruction, final BufferedWriter writer) throws IOException
	{
		//write the beginning of the processing instruction: "<?target "
		writer.write(PROCESSING_INSTRUCTION_START+processingInstruction.getTarget()+SPACE_CHAR);
		writer.write(processingInstruction.getData());  //write the processing instruction data
		writer.write(PROCESSING_INSTRUCTION_END);	//write the end of the processing instruction
		writer.newLine();	//add a newline in the default format
	}

	/**Serializes the specified element to the given writer, using the default
		formatting options.
	@param element The XML element to serialize.
	@param writer The writer into which the element should be written.
	@exception IOException Thrown if an I/O error occurred.
	*/
	protected void write(final Element element, final BufferedWriter writer) throws IOException
	{
	  write(element, writer, isFormatted());  //write the element using the default formatting options
	}

	/**Serializes the specified element to the given writer.
	@param element The XML element to serialize.
	@param writer The writer into which the element should be written.
	@param formatted Whether this element and its contents, including any child
		elements, should be formatted.
	@exception IOException Thrown if an I/O error occurred.
	*/
	protected void write(final Element element, final BufferedWriter writer, boolean formatted) throws IOException
	{
//G***del Debug.trace("starting element, nestLevel: "+nestLevel);
//G***del		if(isFormatted())	//if we should write formatted output
		XMLNamespaceProcessor.ensureNamespaceDeclarations(element); //make sure this element's namespaces are all declared
		if(formatted)	//if we should write formatted output
			writeHorizontalAlignment(writer, nestLevel);		//horizontally align the element
		writer.write(TAG_START+element.getNodeName());	//write the beginning of the start tag
		for(int attributeIndex=0; attributeIndex<element.getAttributes().getLength(); ++attributeIndex)	//look at each attribute
		{
			final Attr attribute=(Attr)element.getAttributes().item(attributeIndex);	//get a reference to this attribute
		  final String attributeValue=attribute.getValue(); //get the attribute's value
				//use a double quote character as a delimiter unless the value contains
				//  a double quote; in that case, use a single quote
			final char valueDelimiter=attributeValue.indexOf(DOUBLE_QUOTE_CHAR)<0 ? DOUBLE_QUOTE_CHAR : SINGLE_QUOTE_CHAR;
				//write the attribute and its value after encoding it for XML; pass the delimiter in case this value has both a single and a double quote
			writer.write(SPACE_CHAR+attribute.getName()+EQUAL_CHAR+valueDelimiter+encodeContent(attribute.getValue(), valueDelimiter)+valueDelimiter);
		}
		if(element.getChildNodes().getLength()>0)	//if there are child elements
		{
			writer.write(TAG_END);	//write the end of the start tag
		  boolean contentFormatted=false;  //we'll determine if we should format the content of the child nodes
				//we'll only format the contents if there are only element children
			if(formatted) //if we've been told to format, we'll make sure there are element child nodes
			{
				for(int childIndex=0; childIndex<element.getChildNodes().getLength(); childIndex++)	//look at each child node
				{
					final int childNodeType=element.getChildNodes().item(childIndex).getNodeType(); //get thic child's type of node
					if(childNodeType==Node.ELEMENT_NODE)	//if this is an element child node
					{
						contentFormatted=true; //show that we should format the element content
//G***del						break;  //we know we should format; stop looking for element children
					}
					else if(childNodeType==Node.TEXT_NODE)  //if this is text
					{
						contentFormatted=false; //text or mixed content is always unformatted
						break;  //we know we shouldn't format; stop looking at the children
					}
				}
			}
			if(contentFormatted)	//if we should write formatted output for the content
				writer.newLine();	//add a newline after the element's starting tag
			++nestLevel;	//show that we're nesting to the next level
			writeContent(element, writer, contentFormatted);
			--nestLevel;	//show that we're finished with this level of the document hierarchy
			if(contentFormatted)	//if we should write formatted output for the content
				writeHorizontalAlignment(writer, nestLevel);		//horizontally align the element's ending tag
			writer.write(TAG_START+'/'+element.getNodeName()+TAG_END);	//write the ending tag G***use a constant here
		}
		else	//if there are no child elements, this is an empty element
			writer.write(String.valueOf(XMLConstants.SPACE_CHAR)+'/'+TAG_END);	//write the end of the empty element tag, with an extra space for HTML browser compatibility G***use a constant here
		if(formatted)	//if we should write formatted output
			writer.newLine();	//add a newline after the element
//G***del if not needed		writer.newLine();	//add a newline in the default format
	}

	/**Serializes the content of the specified element to the given writer using
		the default formatting options.
	@param element The XML element the content of which to serialize.
	@param writer The writer into which the element content should be written.
	@exception IOException Thrown if an I/O error occurred.
	*/
	protected void writeContent(final Element element, final BufferedWriter writer) throws IOException
	{
		writeContent(element, writer, isFormatted());  //write the content using the default formatting options
	}

	/**Serializes the content of the specified element to the given writer.
	@param element The XML element the content of which to serialize.
	@param writer The writer into which the element content should be written.
	@param formatted Whether the contents of this element, including any child
		elements, should be formatted.
	@exception IOException Thrown if an I/O error occurred.
	*/
	protected void writeContent(final Element element, final BufferedWriter writer, final boolean formatted) throws IOException
	{
		for(int childIndex=0; childIndex<element.getChildNodes().getLength(); childIndex++)	//look at each child node
		{
//G***del Debug.trace("writing child: "+childIndex+", nestLevel: "+nestLevel);
			final Node node=element.getChildNodes().item(childIndex);	//look at this node
			switch(node.getNodeType())	//see which type of object this is
			{
				case Node.ELEMENT_NODE:	//if this is an element
//G***del Debug.trace("Found element node, nestLevel: "+nestLevel);
					write((Element)node, writer, formatted);	//write this element
					break;
				case Node.TEXT_NODE:	//if this is a text node
//G***del Debug.trace("Found text node, nestLevel: "+nestLevel);
					writer.write(encodeContent(node.getNodeValue()));	//write the text value of the node after encoding the string for XML
					break;
				case Node.COMMENT_NODE:	//if this is a comment node
//G***del Debug.trace("Found comment node, nestLevel: "+nestLevel);
					if(formatted)	//if we should write formatted output
						writeHorizontalAlignment(writer, nestLevel);		//horizontally align the element
					writer.write(XMLConstants.COMMENT_START);	//write the start of the comment
					writer.write(node.getNodeValue());	//write the text value of the node, but don't encode the string for XML since it's inside a comment
					writer.write(XMLConstants.COMMENT_END);	//write the end of the comment
					if(formatted)	//if we should write formatted output
						writer.newLine();	//add a newline after the element
					break;
				case Node.CDATA_SECTION_NODE:	//if this is a CDATA section node
					writer.write(XMLConstants .CDATA_START);	//write the start of the CDATA section
					writer.write(encodeContent(node.getNodeValue()));	//write the text value of the node after encoding the string for XML
					writer.write(XMLConstants.CDATA_END);	//write the end of the CDATA section
					break;
				//G***see if there are any other types of nodes that need serialized
			}
		}
	}

	/**Creates the specified number of horizontal alignment strings.
	@param writer The writer into which the element should be written.
	@param nestLevel The level of nesting for horizontal alignment.
	@exception IOException Thrown if an I/O error occurred.
	*/
	protected void writeHorizontalAlignment(final BufferedWriter writer, int nestLevel) throws IOException
	{
//G***del Debug.trace("Inside writing horizontal alignment, nestLevel: "+this.nestLevel);
		while(nestLevel>0)	//while we haven't finished nesting
		{
//G***del Debug.trace("Writing horizontal alignment.");
			writer.write(getHorizontalAlignString());	//write another string for horizontal alignment
			--nestLevel;	//show that we have one less level to next
		}
	}

	/**Encodes content using the available entities and/or XML encoding for
		extended characters.
	@param text The text to encode.
	@return The encoded version of the content.
	@see #isUseEntities
	@see #isXMLEncodeControl
	@see #isXMLEncodeNonASCII
	@see #isXMLEncodePrivateUse
	*/
	protected String encodeContent(final String text)
	{
		return encodeContent(text, (char)0); //show that there is no delimiter character
	}

	/**Encodes content using the available entities and/or XML encoding for
		extended characters. If the delimiter character is not 0, it is
		unconditionally encoded.
	@param text The text to encode.
	@param delimiter The character which should always be encoded, such as the
		delimiter character for attributes ('"' or '\''), or 0 if there is no
		delimiter.
	@return The encoded version of the content.
	@see #isUseEntities
	@see #isXMLEncodeControl
	@see #isXMLEncodeNonASCII
	@see #isXMLEncodePrivateUse
	*/
	protected String encodeContent(final String text, final char delimiter)
	{
//G***del		final boolean useEntities=isUseEntities();  //see if we should use entities if available
		final boolean xmlEncodeControl=isXMLEncodeControl();  //see if we should XML-encode control characters
		final boolean xmlEncodeNonASCII=isXMLEncodeNonASCII();  //see if we should XML-encode characters over 127
		final boolean xmlEncodePrivateUse=isXMLEncodePrivateUse();  //see if we should XML-encode Unicode private use characters
		final StringBuffer stringBuffer=new StringBuffer(text.length());  //we know that the output string will be at least as long as the input string
		final int textLength=text.length(); //see how long the content is currently
		for(int i=0; i<textLength; ++i) //look at each character in the text
		{
			final char c=text.charAt(i);  //get a reference to this character
			final int entityIndex=entityCharacterValues.indexOf(c); //see if this character matches an entity value
				//always replace entities, regardless of the status of isXMLEncode(),
				//  because the lookup table has already been set up appropriately
				//  based on that option, and we *always* want to encode such things as '&'
		  if(entityIndex>=0) //if this character should be replaced by an entity
			{
				stringBuffer.append(ENTITY_REF_START);  //append the start-of-entity
				stringBuffer.append(entityNames[entityIndex]);  //append the corresponding entity name
				stringBuffer.append(ENTITY_REF_END);  //append the end-of-entity
			}
			//if we have no entity replacement for the character,
			//  but it is an extended character and we should XML-encode such
			//  characters (note that the ampersand and less-than/greater-than should *always* be encoded)
			else if(
					((xmlEncodeNonASCII && !CharacterUtilities.isASCII(c)) || c==ENTITY_AMP_VALUE || c==ENTITY_LT_VALUE || c==ENTITY_GT_VALUE)
					|| (xmlEncodeControl && Character.isISOControl(c))  //if we should encode control characters, and this is a control character
					|| (xmlEncodePrivateUse && Character.getType(c)==Character.PRIVATE_USE) //if we should encode control characters, and this is a control character
					|| (c==delimiter) //the delimiter character, if present, will *always* be encoded
				)
			{
				stringBuffer.append(CHARACTER_REF_START); //append the start-of-character-reference
				stringBuffer.append('x'); //show that this will be a hexadecimal number G***use a constant here
				stringBuffer.append(Integer.toHexString(c).toUpperCase()); //append the hex representation of the character
//G***del if not needed				stringBuffer.append(IntegerUtilities.toHexString(c, 4));  //append the character in four-digit hex format (i.g. &#XXXX;)
				stringBuffer.append(CHARACTER_REF_END); //append the end-of-character-reference
			}
		  else  //if this character shouldn't be encoded
			{
				stringBuffer.append(c); //append the character normally
			}
		}
		return stringBuffer.toString(); //return the encoded version of the content
	}

}
