package com.garretwilson.text.xml;

import java.io.*;
import java.util.*;
import java.net.URI;
import java.net.URISyntaxException;
//G***del import java.net.URL;
//G***del if we don't need import java.text.MessageFormat;
import org.w3c.dom.DOMException;
import org.w3c.dom.DocumentType;

import com.garretwilson.io.FileConstants;
import com.garretwilson.io.FileUtilities;
import com.garretwilson.io.ParseEOFException;	//G***go through and catch all these and throw XML exceptions
import com.garretwilson.io.ParseUnexpectedDataException;
import com.garretwilson.io.URIInputStreamable;
import com.garretwilson.net.URIUtilities;
import com.garretwilson.text.CharacterEncoding;
import com.garretwilson.text.xml.schema.*;
//G***del import com.garretwilson.util.StringManipulator;
import com.garretwilson.util.Debug;

//G***del all the XMLUndefinedEntityReferenceException throws when we don't need them anymore, in favor of XMLWellFormednessException

/**Class which parses an input stream containing XML data.
	This class also supports a "tidy" option which, if enabled, makes this a
	non-conforming XML processor, but means that not-well-formed input will be
	converted to well-formed XML. Specifically, the tidy option does the following:
	<ul>
	  <li>Defaults to the ISO-8859-1 character encoding if an XML declaration is not present.</li>
		<li>Allows a missing system ID for the <code>&lt;DOCTYPE</code>.</li>
		<li>Ignores document type declarations inside the root element.</li>
		<li>Ignores elements with invalid names (e.g. <code>&lt;![endif]&gt;</code>)</li>
		<li>Automatically converts tags which should be empty elements.</li>
	  <li>Accepts unquoted element attributes.</li>
		<li>Accepts attributes with no values (e.g. <code>&lt;hr noshade/&gt;</code>)</li>
		<li>Accepts document type declarations in lowercase (i.e. <code>&lt;!doctype...</code>).</li>
		<li>Accepts "PUBLIC" and "SYSTEM" identifiers in any case.</li>
		<li>Changes all tag names to lowercase. G***later change this to check the schema</li>
		<li>Changes all attribute names to lowercase. G***later change this to check the schema</li>
		<li>Ensures the name of any document type matches the name of the document element.</li>
	</ul>
G***make tidy fix things like <li>All rights reserved.</p></li>; right now it truncates by changing the </p> to an </li>
	<p>When loading an external DTD subset or an external entity which has a
		public ID, this class first searches for the file stored in the same location
		as the class itself. The filename should be the same as the public ID with
		illegal filename characters (e.g. '/' and '\') replaced with the underscore
		character ('_') and the extension removed. This allows for faster performance
		by making relieving network access and even making it unnecesary for the
		file to even exist at the location specified by its system ID.</p>
	<p>If possible, each instance of this class caches any DTDs loaded. This means
		performance increases can be achieved if the same XML processor is used for
		loading multiple documents which reference the same external DTD subset.</p>
@see XMLConstants
@author Garret Wilson
//G***del if we don't need @see XMLStatusReportable
*/
public class XMLProcessor extends XMLUtilities implements XMLConstants, URIInputStreamable
{

//G***del package com.garretwilson.text.xml;
//G***del	/**The relative*/
//G***del	protected final static String XML_RESOURCE_RELATIVE_DIRECTORY="../

	/**Whether we should automatically tidy non-well-formed XML.*/
	private boolean tidy=false;

		/**@return Whether we should automatically tidy non-well-formed XML.*/
		public boolean isTidy() {return tidy;}

		/**Sets whether non-well-formed XML should be automatically tidied.
		@param newTidy Whether we should automatically tidy non-well-formed XML.
		*/
		public void setTidy(final boolean newTidy) {tidy=newTidy;}

	/**In tidy mode, the document public and system IDs to use if none is available.*/
	private XMLExternalID tidyDocumentTypeExternalID=null;

		/**Sets an overriding public and system ID to use in tidy mode. If set,
		  any internal DTD subset will still be in effect but the tidy external
			public and system IDs will be used for an external DTD subset, whether or
			not one is defined in the document.
		@pararm publicID The overriding document type public ID.
		@pararm systemID The overriding document type system ID.
		@see #isTidy
		*/
		public void setTidyDocumentTypeExternalID(final String publicID, final String systemID)
		{
			tidyDocumentTypeExternalID=new XMLExternalID(publicID, systemID); //create a new external ID for tidying G***eventually make an option to remove this
		}

		/**@return The overriding document type external ID to be used when tidying,
		  or <code>null</code> if the document type should not be overridden when
			tidying.
		*/
		protected XMLExternalID getTidyDocumentTypeExternalID() {return tidyDocumentTypeExternalID;}

	/**Whether or not we should validate the document.*/
//G***fix	private boolean ShouldValidate=true;

		/**@return Whether or not we should validate the document.*/
//G***fix		protected boolean isShouldValidate() {return ShouldValidate;}

	/**Whether or not we should recognize and process any stylesheets we encounter.*/
//G***fix	private boolean ShouldProcessStyleSheets=true;

		/**@return Whether or not we should recognize and process any stylesheets we encounter.*/
//G***fix		protected boolean isShouldProcessStyleSheets() {return ShouldProcessStyleSheets;}

	/**Whether or not we should apply any stylesheets to the document.*/
//G***fix	private boolean ShouldApplyStyleSheets=true;

		/**@return Whether or not we should apply any stylesheets to the document.*/
//G***fix		protected boolean isShouldApplyStyleSheets() {return ShouldApplyStyleSheets;}

	/**The input stream from which the XML data is read.*/
//G***del	private InputStream InStream;	//G***fix

	/**The interface to use to locate external files. This can be this class or another
		class, depending on which constructor is used.
	@see XMLProcessor#XMLProcessor
	*/
//G***del	private InputStreamLocator inputStreamLocator=null;

		/**@return The interface to use to locate external files. This can be this class
			or another class, depending on which constructor has been used.
		@see XMLProcessor#XMLProcessor
		*/
//G***del		public InputStreamLocator getInputStreamLocator() {return inputStreamLocator;}

		/**Sets the interface to use to locate external files. This can be this class
			or another class, depending on which constructor has been used.
		@param newInputStreamLocator A class implementing the interface that can find files
			and return an <code>InputStream</code> to them.
		@see XMLProcessor#XMLProcessor
		*/
//G***del		protected void setInputStreamLocator(final InputStreamLocator newInputStreamLocator) {inputStreamLocator=newInputStreamLocator;}

	/**The interface to use to locate external files. This can be this class or another
		class, depending on which constructor is used.
	@see XMLProcessor#XMLProcessor
	*/
	private URIInputStreamable uriInputStreamable=null;

		/**@return The interface to use to locate external files. This can be this class
			or another class, depending on which constructor has been used.
		@see XMLProcessor#XMLProcessor
		*/
		public URIInputStreamable getURIInputStreamable() {return uriInputStreamable;}

		/**Sets the interface to use to locate external files. This can be this class
			or another class, depending on which constructor has been used.
		@param newURIInputStreamable A class implementing the interface that can find files
			and return an <code>InputStream</code> to them.
		@see XMLProcessor#XMLProcessor
		*/
		protected void setURIInputStreamable(final URIInputStreamable newURIInputStreamable) {uriInputStreamable=newURIInputStreamable;}

	/**The object used to process schemas.*/
	private XMLSchemaProcessor schemaProcessor=null;

		/**@return The object used to process schemas.*/
		public XMLSchemaProcessor getSchemaProcessor() {return schemaProcessor;}

		//G***fix, make accessor methods, and comment
	private Map nestedEntityReferenceMap=new HashMap();	//create a new map that will tell us which references we're nesting to prevent recursion

	/**This is the default document type which will be assigned to a document if that document
	has a document type. This will storing the default entity references used by this
	XML document, and is populated with amp, lt, gt, apos, quot, as XML recognizes these by default.
	*/
	private XMLDocumentType DocumentType=new XMLDocumentType(null);	//create a default document type G***use the XMLDOMImplementation function here

	/**@return The document type for the document being processed. This will be the same
	document type that is returned by the document's getXMLDocumentType() function if
	the document being processed has a document type.
	@see XMLDocument#getXMLDocumentTYpe
	*/
	protected XMLDocumentType getXMLDocumentType() {return DocumentType;}

	/**Whether or not we should allow end tags to be unnamed.
	Note that if this is set to true, we will not technically be adhering to the XML specification.
	*/
	private boolean AllowUnnamedEndTags=false;	//default to false, because the XML specification does not allow unnamed end tags

		/**@return Whether or not we should allow end tags to be unnamed.*/
		public boolean isAllowUnnamedEndTags() {return AllowUnnamedEndTags;}

		/**Set whether or not we should allow end tags to be unnamed.
		Note that if this is set to true, we will not technically be adhering to the XML specification.
		@param new AllowUnnamedEndTags Whether or not we should allow end tags to be unnamed.
		*/
		public void setAllowUnnamedEndTags(boolean newAllowUnnamedEndTags) {AllowUnnamedEndTags=newAllowUnnamedEndTags;}

	/**Default constructor. Sets the input stream locator to this class so that we can find
		our own files.*/
	public XMLProcessor()
	{
		setURIInputStreamable(this);	//show that we'll locate our own files
	}

	/**Constructor that sets the interface to use to locate external files.
	@param newURIInputSTreamable A class implementing the interface that can find files and
		return an <code>InputStream</code> to them.
	*/
	public XMLProcessor(final URIInputStreamable newURIInputStreamable)
	{
		setURIInputStreamable(newURIInputStreamable);	//show which input stream locator we'll use
		schemaProcessor=new XMLSchemaProcessor();  //create a new schema processor to use
	}

	/**Attempts to automatically detect the character encoding of a particular
		input stream that supposedly contains XML data.
	@param inputStream The stream which supposedly contains XML data.
	@param sourceObject The source of the data (e.g. a String, File, or URL).
	@param autodetectEncoding Receives a copy of the encoding that was autodetected,
		not necessarily the same as in the "encoding" attribute.
	@param autodetectPrereadCharacters Receives a copy of any characters preread during
		autodetection of character encoding.
	@return The name of the character encoding specified in the "encoding" attribute,
		or <code>null</code> if there was no encoding attribute specified.
	@exception IOException Thrown if an I/O error occurred.
	*/
	protected String prereadEncoding(final InputStream inputStream, final Object sourceObject, final StringBuffer autodetectEncoding, final StringBuffer autodetectPrereadCharacters) throws IOException
	{
		String prereadCharacters="";	//we'll store here any characters we preread while looking for the character encoding
		String characterEncodingName=null;		//we'll store here the name of the character encoding, when we determine it
		final int[] byteOrderMarkArray=new int[4];	//create an array to hold the byte order mark
		boolean eof=false;	//we'll set this to true if we reach the end of the file
		for(int i=0; i<4 && !eof; ++i)	//read each character unless we reach the end of the file (we're using a loop instead of read(int[]) because the latter could read less than the number of bytes requested, even if there are more available)
		{
			byteOrderMarkArray[i]=inputStream.read();	//read the next byte
			eof=byteOrderMarkArray[i]==-1;	//see if we reached the end of the file
		}
		if(!eof)	//if we didn't reach the end of the data
		{
/*G***del if not needed
			try
			{
*/
			CharacterEncoding characterEncoding=CharacterEncoding.create(byteOrderMarkArray, XML_DECL_START);	//see if we can recognize the encoding by the beginning characters
		  if(characterEncoding==null) //if no character encoding was found
			{
					//if we couldn't find an encoding, if we should tidy this document, and if the document did *not* start with "<?xml..."
				if(isTidy() && autodetectPrereadCharacters.toString().startsWith(XML_DECL_START.substring(0, Math.min(autodetectPrereadCharacters.length(), XML_DECL_START.length()))))
					characterEncoding=new CharacterEncoding(CharacterEncoding.ISO_8859_1, false);	//construct a default ISO-LATIN-1 character encoding for tidied documents
				else  //if we couldn't find the encoding, but we're either tidying or there was a "<?xml..." (which means we have to assume UTF-8)
					characterEncoding=new CharacterEncoding(CharacterEncoding.UTF_8, false);	//construct a default UTF-8 character encoding, since we don't recognize the Byte Order Mark (the big-endian/little-endian byte order flag is meaningless here)
//G***del Debug.trace("we're trying character encoding: "+characterEncoding); //G***del
			}
			autodetectEncoding.replace(0, autodetectEncoding.length(), characterEncoding.getEncoding());	//return the autodetected encoding method (the full encoding name, complete with byte order), replacing whatever contents the buffer already had
			if(characterEncoding.getFamily().equalsIgnoreCase(characterEncoding.UTF_16)
					&& byteOrderMarkArray[0]!=characterEncoding.BOM_UTF_16_BIG_ENDIAN[0]
					&& byteOrderMarkArray[0]!=characterEncoding.BOM_UTF_16_LITTLE_ENDIAN[0])	//if this was UTF-16, but there was no Byte Order Mark
				throw new XMLWellFormednessException(XMLWellFormednessException.INVALID_FORMAT, new Object[]{}, 0, 0, sourceObject!=null ? sourceObject.toString() : "");	//show that the UTF-16 had no Byte Order Mark
			final int bytesPerCharacter=characterEncoding.getBytesPerCharacter();	//find out how many bytes are used for each character
				//convert the Byte Order Mark bytes into a string
			if(bytesPerCharacter==1)	//if there is only one byte for each character
				for(int i=0; i<byteOrderMarkArray.length; prereadCharacters+=(char)byteOrderMarkArray[i++]);	//convert the bytes to characters normally
//G***del					prereadCharacters=new String(byteOrderMarkArray);	//convert the bytes to characters normally
			else if(bytesPerCharacter==2)	//if there are two bytes for each character, the first two make up the true Byte Order Mark, so ignore them
			{
				if(characterEncoding.isLittleEndian())	//if the least-sigificant byte (the one we're interested in) comes first
					prereadCharacters+=(char)byteOrderMarkArray[2];	//take the first byte (ignoring the Byte Order Mark)
				else	//if the least-sigificant byte (the one we're interested in) comes second
					prereadCharacters+=(char)byteOrderMarkArray[3];	//take the second byte (ignoring the Byte Order Mark)
			}
			else if(bytesPerCharacter==4)	//if there are four bytes for each character
			{
				if(characterEncoding.isLittleEndian())	//if the least-sigificant byte (the one we're interested in) comes first
					prereadCharacters+=(char)byteOrderMarkArray[0];	//take the first byte
				else	//if the least-sigificant byte (the one we're interested in) comes last
					prereadCharacters+=(char)byteOrderMarkArray[3];	//take the last byte
				//future: here, support unusual UCS-4 octet orders may be added
			}
//G***del			boolean confirmedXMLDeclStart=false;	//we don't know yet if this stream starts with an XML declaration "<?xml..."; if we find it doesn't, there's no need to look for an encoding attribute
/*G***del
			if(XML_DECL_START.startsWith(prereadCharacters))	//if there is an "<?xml" declaration in what we've preread so far, we'll see if an encoding was specified
			{
*/
			while(true)	//we now know the encoding family and have a string with the bytes read so far; now, try to find any specified character encoding
			{
				int nextCharInt;	//this will accept the next character read
				if(bytesPerCharacter==1)	//if there is only one byte for each character
					nextCharInt=inputStream.read();	//read the next character normally
				else if(bytesPerCharacter==2)	//if there are two bytes for each character
				{
					if(characterEncoding.isLittleEndian())	//if the least-sigificant byte (the one we're interested in) comes first
					{
						nextCharInt=inputStream.read();	//read the next character normally
						inputStream.skip(1);	//skip the next byte
					}
					else	//if the least-sigificant byte (the one we're interested in) comes second
					{
						inputStream.skip(1);	//skip the first byte
						nextCharInt=inputStream.read();	//read the next character normally
					}
				}
				else if(bytesPerCharacter==4)	//if there are four bytes for each character
				{
					if(characterEncoding.isLittleEndian())	//if the least-sigificant byte (the one we're interested in) comes first
					{
						nextCharInt=inputStream.read();	//read the next character normally
						inputStream.skip(3);	//skip the next three byte
					}
					else	//if the least-sigificant byte (the one we're interested in) comes last
					{
						inputStream.skip(3);	//skip the first three byte
						nextCharInt=inputStream.read();	//read the next character normally
					}
					//future: here, support unusual UCS-4 octet orders may be added
				}
				else	//if bytesPerCharacter has an unrecognized value
					nextCharInt=inputStream.read();	//assume one byte per character and read the next character normally
				if(nextCharInt==-1)	//if we reach the end of the stream
					break;	//stop trying to autodetect the encoding and process what we have
				prereadCharacters+=(char)nextCharInt;	//add the character read to the end of our string
				if(prereadCharacters.length()==XML_DECL_START.length() && !prereadCharacters.equals(XML_DECL_START))	//if we've read enough characters to see if this stream starts with the XML declaration "<?xml...", and it doesn't
					break;	//stop looking for an encoding attribute, since there isn't even an XML declaration
				if(prereadCharacters.endsWith(XML_DECL_END))	//if we've read all of the XML declaration
					break;	//stop trying to autodetect the encoding and process what we have
				final int encodingDeclarationStartIndex=prereadCharacters.indexOf(ENCODINGDECL_NAME);	//see where the "encoding" declaration starts (assuming we've read it yet)
				if(encodingDeclarationStartIndex!=-1)	//if we've at least found the "encoding" declaration (but perhaps not the actual value)
				{
					int quote1Index=prereadCharacters.indexOf(DOUBLE_QUOTE_CHAR, encodingDeclarationStartIndex+ENCODINGDECL_NAME.length());	//see if we can find a double quote character
					if(quote1Index==-1)	//if we couldn't find a double quote
						quote1Index=prereadCharacters.indexOf(SINGLE_QUOTE_CHAR, encodingDeclarationStartIndex+ENCODINGDECL_NAME.length());	//see if we can find a single quote character
					if(quote1Index!=-1)	//if we found either a single or double quote character
					{
						final char quoteChar=prereadCharacters.charAt(quote1Index);	//see which type of quote we found
						final int quote2Index=prereadCharacters.indexOf(quoteChar, quote1Index+1);	//see if we can find the matching quote
						if(quote2Index!=-1)	//if we found the second quote character
						{
							characterEncodingName=prereadCharacters.substring(quote1Index+1, quote2Index);	//get the character encoding name specified
							break;	//stop looking for the encoding
						}
					}
				}
			}
/*G***del if not needed
			}
			catch(UnsupportedEncodingException e)	//if the encoding could not be determined
			{
				throw new XMLWellFormednessException(XMLWellFormednessException.INVALID_FORMAT, new Object[]{}, 0, 0, sourceObject!=null ? sourceObject.toString() : "");	//show that we don't recognize the data format
			}
*/
		}
		autodetectPrereadCharacters.replace(0, autodetectPrereadCharacters.length(), prereadCharacters);	//send back the characters we preread
		return characterEncodingName;	//return the name of the encoding, or null if we didn't find an encoding
	}

//G***fix	public reportError(final XMLException xmlException);

	/**Skips all XML whitespace characters, and returns the number of characters skipped.
	Resets peeking.
	@param reader The reader from which to retrieve characters.
	@exception IOException Thrown when an i/o error occurs.
	@exception ParseEOFException Thrown when the end of the input stream is reached unexpectedly.
	@return The number of characters skipped.
	*/
	public long skipWhitespaceCharacters(final XMLReader reader) throws IOException, ParseEOFException
	{
		return reader.skipChars(WHITESPACE_CHARS);	//skip any whitespace characters and return the number of characters skipped
	}

	/**Skips all XML whitespace characters, and returns the number of characters skipped.
	No exception is thrown if the end of the file is reached.
	Resets peeking.
	@param reader The reader from which to retrieve characters.
	@exception IOException Thrown when an i/o error occurs.
	@return The number of characters skipped.
	*/
	public long skipWhitespaceCharactersEOF(final XMLReader reader) throws IOException
	{
		return reader.skipCharsEOF(WHITESPACE_CHARS);	//skip any whitespace characters and return the number of characters skipped without throwing an exception if we find the end of the file
	}

	/**Returns an input stream from given URL. This function is used when we're
		acting as our own <code>URIInputStreamabler</code>.
		This implementation only works with URLs.
	@param uri A complete URI to a file.
	@return An input stream to the contents of the file represented by the given URL.
	@exception IOException Thrown if an I/O error occurred.
	@see #getURIInputStreamable
	*/
	public InputStream getInputStream(final URI uri) throws IOException
	{
		return uri.toURL().openConnection().getInputStream();	//since we don't know any better (they didn't specify their own file locator), try to open a connection to the URL directly and return an input stream to that connection
	}

	/**Creates a reader to read the specified input stream. Encoding is preread and
		correctly interpreted.
	@param inputStream The input stream from which to get the XML data.
	@param sourceObject The source of the data (e.g. a String, File, or URL).
	@return A reader from which the file may be read.
	@exception IOException Thrown if an I/O error occurred.
	*/
	protected XMLReader createReader(final InputStream xmlInputStream, final Object sourceObject) throws IOException //G***comment exceptions
	{
		final StringBuffer autodetectEncoding=new StringBuffer();	//this will receive the autodetected encoding
		final StringBuffer autodetectPrereadCharacters=new StringBuffer(32);	//this will receive whatever characters were read while prereading the encoding
		String encoding=prereadEncoding(xmlInputStream, sourceObject, autodetectEncoding, autodetectPrereadCharacters);	//see if we can determine the encoding before we we parse the stream
		if(encoding==null)	//if there was no encoding specified
		{
			encoding=autodetectEncoding.toString();	//use the automatically detected encoding
//G***del Debug.trace("Could not find an encoding; using: "+encoding);  //G***del
//G***del Debug.trace("Autodetect preread characters: "+autodetectPrereadCharacters); //G***del

/*G***see how this should really be interpreted; probably don't do this in the parser itself
			if(!encoding.equalsIgnoreCase(CharacterEncoding.UTF8))	//if the encoding was something besides UTF-8, yet there was no "encoding" attribute, this is an error
				throw new XMLWellFormednessException(XMLWellFormednessException.INVALID_ENCODING, new Object[]{encoding}, 0, 0, sourceObject!=null ? sourceObject.toString() : "");	//show that something besides UTF-8 was used with no "encoding" attribute
*/
		}
		final InputStreamReader xmlInputStreamReader=new InputStreamReader(xmlInputStream, encoding);	//create a new input stream reader with the correct encoding
//G***catch any unsupported encoding exception here and throw our own
		final XMLReader xmlReader=new XMLReader(xmlInputStreamReader, autodetectPrereadCharacters, sourceObject);	//create a new XML reader with our correctly encoded reader and the characters we've preread so far in autodetecting the encoding
		xmlReader.tidy=isTidy();  //G***fix
		return xmlReader; //G***fix
	}

	/**Creates a reader to read the specified external file.
	@param context The context object, usually a URI.
	@param systemID The relative or absolute filename.
	@exception IOException Thrown when an i/o error occurs.
	@return A reader from which the file may be read.
	@exception IOException Thrown if an I/O error occurred.
	*/
	protected XMLReader createReader(final Object context, final String systemID) throws IOException //G***comment exceptions
	{
		return createReader(context, null, systemID);  //create a reader without checking a public ID
	}

	/**Creates a reader to read the specified external file, specifying both a
		public ID and a system ID. If the public ID is a valid public ID and
		represents a public ID which is stored in our resources in the same directory
		as this class, our local version of the file will be loaded instead of the
		physical file at the literal location indicated by the systemID.
		Note that any resource must be saved with the same name as its public ID,
		with all invalid filename characters replaced with underscores, and
		no extension for it to be recognized.
	@param context The context object, usually a URI.
	@param publicID The public identifier of the file to load, or <code>null</code>
		if there is no public ID.
	@param systemID The relative or absolute filename, which will only be used
		if there is no public ID or the public ID is not recognized.
	@exception IOException Thrown when an i/o error occurs.
	@return A reader from which the file may be read.
	@exception IOException Thrown if an I/O error occurred.
	@see FileConstants#ILLEGAL_FILENAME_CHARACTERS
	*/
	protected XMLReader createReader(final Object context, final String publicID, final String systemID) throws IOException //G***comment exceptions
	{
		try
		{
				//create a URI for the system ID (there should always be one) in relation to our reader's context, if any
			final URI uri=systemID!=null ? URIUtilities.createURI(context, systemID) : null;
				//convert the public ID (if there is one) to a valid filaname and see if we can load this
				//  resource locally rather than from the literal file location
			final String localFilename=FileUtilities.encodeCrossPlatformFilename(publicID);	//get the name of the file if it were to be stored locally
/*G***del
			if(publicID!=null)	//G***del
			{
				String name=localFilename;
						Class c = getClass();
						while (c.isArray()) {
								c = c.getComponentType();
						}
						String baseName = c.getName();
						int index = baseName.lastIndexOf('.');
						if (index != -1) {
								name = baseName.substring(0, index).replace('.', '/')
										+"/"+name;
						}
				Debug.trace(name);

				final java.net.URL url=getClass().getResource(localFilename);
		try {
			if(url!=null)
				url.openStream();
		} catch (IOException e) {
				Debug.error(e);
		}
			}
*/
			final InputStream localResourceInputStream=publicID!=null ? getClass().getResourceAsStream(localFilename) : null;
				//if we couldn't find the resource locally, create an input stream to the system ID URI we already created, using our input stream locator
			final InputStream inputStream=localResourceInputStream!=null ? localResourceInputStream : getURIInputStreamable().getInputStream(uri);
			return createReader(inputStream, uri);	//create a reader from the input stream, specifying the URI from whence it came (at least where it would have came had we not found it locally)
			//G***when will all these readers/connections/streams get closed?
				//G***check for FileNotFoundException and throw something appropriate for our XML processor
		}
		catch(URISyntaxException uriSyntaxException)
		{
			throw new IOException(uriSyntaxException.toString());	//G***fix with a better XML-related error
		}
	}

	/**Creates a reader to read from the specified entity, regardless of whether
		this entity is internal or external.
	@param context The context object, usually a URI.
	@exception IOException Thrown when an i/o error occurs.
	@return A reader from which the entity may be read.
	*/
	protected XMLReader createEntityReader(final Object context, final XMLEntity entity) throws IOException
	{
		XMLReader entityReader=null;	//the reader which we will construct and return; we don't know what it will be, yet
		if(entity.getSystemID()!=null)	//if there is a system identifier for this entity, it's an external entity
		{
//G***del Debug.trace("Before creating reader for: "+entity.getSystemID());
			return createReader(context, entity.getPublicID(), entity.getSystemID());	//G***testing; comment
/*G***del if we don't need
							entity.setSourceName(externalEntityReader.getName());	//store where we found the entity
			entity.setLineIndex(externalEntityReader.getLineIndex());	//store which line the entity starts on
			entity.setCharIndex(externalEntityReader.getCharIndex());	//store which character the entity starts on
			parseEntityContent(externalEntityReader, ownerDocument, entity, '~');	//parse the entity content G***fix so that parseEntityContent will ignore the delimiter character

System.out.println("Entity value: "+entity.getFirstChild().getNodeValue());	//G***del
*/
		}
		else	//if this is an internal entity reference
		{
			entityReader=new XMLReader(entity.getText(), "Entity \""+entity.getNodeName()+"\" from \""+entity.getSourceName()+"\"");	//create a string reader from the text of the entity, giving our entity name for the name of the reader G***Int G***show whether this is a parameter or a general entity reference

entityReader.tidy=isTidy();  //G***fix

//G***del when works newio			entityReader=new XMLReader(new StringReader(entity.getText()), "Entity \""+entity.getNodeName()+"\" from \""+entity.getSourceName()+"\"");	//create a string reader from the text of the entity, giving our entity name for the name of the reader G***Int G***show whether this is a parameter or a general entity reference
			entityReader.setLineIndex(entity.getLineIndex());	//pretend we're reading where the entity was located in that file, so any errors will show the correct information
			entityReader.setCharIndex(entity.getCharIndex());	//pretend we're reading where the entity was located in that file, so any errors will show the correct information
//G***del System.out.println("Found parameter entity reference, the text of which is: "+entity.getText());	//G***del
		}
		return entityReader;	//return the entity reader we created
	}

	/**Parses an input stream that supposedly contains a character reference.
	@param reader The reader from which to retrieve characters.
	@exception IOException Thrown when an i/o error occurs.
	@exception XMLSyntaxException Thrown when there is a syntax error in the XML file.
	@exception XMLWellFormednessException Thrown when there is a well-formedness error in the XML file.
	@exception ParseUnexpectedDataException Thrown when an unexpected character is found.
//G***del	@exception ParseEOFException Thrown when the end of the input stream is reached unexpectedly.
	@return The character encoded by the character reference.
	*/
	protected char parseCharacterReference(final XMLReader reader) throws IOException, XMLSyntaxException, XMLWellFormednessException, ParseUnexpectedDataException//G***del, ParseEOFException
	{
		try
		{
			String characterReferenceString=reader.readDelimitedString(CHARACTER_REF_START, CHARACTER_REF_END);	//read all the characters in the character reference between its start and end
			int numberBase;	//we'll determine which number base to use
			if(characterReferenceString.length()>0 && characterReferenceString.charAt(0)=='x')	//if the character reference begins with 'x' G***make this a constant
			{
				numberBase=16;	//show that this is a hex number
				characterReferenceString=characterReferenceString.substring(1, characterReferenceString.length());	//chop off the first character
			}
			else	//if this is not a hexadecimal number
				numberBase=10;	//it should be a decimal number
			final char c=(char)Integer.parseInt(characterReferenceString, numberBase);	//convert the number to a character G***what if it's an invalid number string? probably throw an error
			if(!isChar(c))	//if the character isn't a valid character
				throw new XMLWellFormednessException(XMLWellFormednessException.LEGAL_CHARACTER, new Object[]{CHARACTER_REF_START+characterReferenceString+CHARACTER_REF_END}, reader.getLineIndex(), reader.getCharIndex(), reader.getName());	//show that this character reference is invalid G***make sure we tell what base the reference string is, and perhaps give a better indication of its location
	//G***del System.out.println("Parsed character, lineIndex "+reader.getLineIndex()+" charIndex "+reader.getCharIndex());	//G***del
			return c;	//return the character referenced
		}
		catch(final ParseEOFException parseEOFException)	//if we run out of data
		{
			throw new XMLWellFormednessException(parseEOFException, ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME).getString(RESOURCE_PREFIX+CHARACTER_REFERENCE_RESOURCE), ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME).getString(RESOURCE_PREFIX+UNKNOWN_RESOURCE));	//show that we couldn't find the end of the character reference
		}
	}

	/**Parses an input stream that supposedly contains an entity reference and
		returns either the entity referenced by this entity reference, or
		a <code>String</code> containing the entity reference markup if no entity was found
		and this is not a well-formedness error. If a missing entity would cause a
		well-formedness error, an exception is thrown.
	@param reader The reader from which to retrieve characters.
	@param ownerDocument The document which will own the constructed entity.
	@exception IOException Thrown when an i/o error occurs.
	@exception XMLSyntaxException Thrown when there is a syntax error in the XML file.
	@exception XMLWellFormednessException Thrown when there is a well-formedness error in the XML file.
	@exception ParseUnexpectedDataException Thrown when an unexpected character is found.
//G***del	@exception ParseEOFException Thrown when the end of the input stream is reached unexpectedly.
	@exception XMLUndefinedEntityReferenceException Thrown if a named entity reference has not been defined.
	@return The entity referenced by this entity reference or a <code>String</code>
		containing the entity reference markup if none exists yet none is required
		for well-formedness.
	*/
	protected Object parseEntityReference(final XMLReader reader, final XMLDocument ownerDocument) throws IOException, XMLSyntaxException, XMLWellFormednessException, ParseUnexpectedDataException/*G***del, ParseEOFException*/, XMLUndefinedEntityReferenceException
	{
//G***del if we don't need		final long entityReferenceLineIndex=reader.getLineIndex(), entityReferenceCharIndex=reader.getCharIndex()+1;	//make a note of where the entity reference will begin
		String entityReferenceName="";	//this will be the name of our entity reference
		try
		{
			entityReferenceName=reader.readDelimitedString("&", ";");	//read all the characters in the entity reference between its start and end G***make these constant
			//if the document has a document type, get a reference to it, otherwise get a reference to our default document type
			final XMLDocumentType documentType=ownerDocument.getXMLDocumentType()!=null ? ownerDocument.getXMLDocumentType() : getXMLDocumentType();
			if(documentType.getEntityXMLNamedNodeMap().containsKey(entityReferenceName))	//if our map of entity references has this entity name
			{
				final XMLEntity entity=(XMLEntity)documentType.getEntityXMLNamedNodeMap().getNamedItem(entityReferenceName);	//find the entity referenced by this entity reference
				if(!entity.isParsedEntity())	//if this is an unparsed entity
					throw new XMLWellFormednessException(XMLWellFormednessException.PARSED_ENTITY, new Object[]{entity.getNodeName()}, reader.getLineIndex(), reader.getCharIndex(), reader.getName());	//show that we don't accept unparsed entity references here G***we need to give a better position in the error, here
				return entity;	//return the entity referenced by this name
			}
			else	//if we don't recognize this entity reference name
			{
				if(ownerDocument.getXMLDocumentType()==null || ownerDocument.getXMLDeclaration().isStandalone())	//if there is no DTD or this document is declared standalone G*** what about "internal DTD subset which contains no parameter entity references?"
					throw new XMLWellFormednessException(XMLWellFormednessException.ENTITY_DECLARED, new Object[]{entityReferenceName}, reader.getLineIndex(), reader.getCharIndex(), reader.getName());	//show that this entity reference was not found
				else	//if the document has a DTD, not having a declared entity is not a *well-formedness* error, although it probably makes the document not valid
					return new String("&"+entityReferenceName+";");	//G***check, comment, use constants
	//G***del				return null;	//show that we couldn't find a matching entity, but that didn't cause a well-formedness error
			}
		}
		catch(final ParseEOFException parseEOFException)	//if we run out of data
		{
			throw new XMLWellFormednessException(parseEOFException, ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME).getString(RESOURCE_PREFIX+ENTITY_REFERENCE_RESOURCE), entityReferenceName!=null ? entityReferenceName : ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME).getString(RESOURCE_PREFIX+UNKNOWN_RESOURCE));	//show that we couldn't find the end of the entity reference
		}
	}

	/**Parses an input stream that supposedly contains a parameter entity reference.
	@param reader The reader from which to retrieve characters.
	@param ownerDocument The document which will own the constructed entity.
	@param parameterEntityMap Any parameter entities we've collected along the
		way while parsing this DTD subset. TODO eventually use a DTD subset object
	@exception IOException Thrown when an i/o error occurs.
	@exception XMLSyntaxException Thrown when there is a syntax error in the XML file.
	@exception XMLWellFormednessException Thrown when there is a well-formedness error in the XML file.
	@exception ParseUnexpectedDataException Thrown when an unexpected character is found.
//G***del	@exception ParseEOFException Thrown when the end of the input stream is reached unexpectedly.
	@exception XMLUndefinedEntityReferenceException Thrown if a named entity reference has not been defined.
	@return The entity referenced by this entity reference.
	*/
	protected XMLEntity parseParameterEntityReference(final XMLReader reader, final XMLDocument ownerDocument, final XMLNamedNodeMap parameterEntityMap) throws IOException, XMLSyntaxException, XMLWellFormednessException, ParseUnexpectedDataException/*G***del, ParseEOFException*/, XMLUndefinedEntityReferenceException
	{
		final long entityReferenceLineIndex=reader.getLineIndex(), entityReferenceCharIndex=reader.getCharIndex()+1;	//make a note of where the entity reference will begin
		String entityReferenceName="";	//this will receive the name of our entity reference
		try
		{
			entityReferenceName=reader.readDelimitedString(PARAMETER_ENTITY_REF_START, PARAMETER_ENTITY_REF_END);	//read all the characters in the entity reference between its start and end
			//if the document has a document type, get a reference to it, otherwise get a reference to our default document type
			final XMLDocumentType documentType=ownerDocument.getXMLDocumentType()!=null ? ownerDocument.getXMLDocumentType() : getXMLDocumentType();
			if(documentType.getParameterEntityXMLNamedNodeMap().containsKey(entityReferenceName))	//if our map of entity references has this entity name
				return (XMLEntity)documentType.getParameterEntityXMLNamedNodeMap().getNamedItem(entityReferenceName);	//return the entity referenced by this name
			else	//if we don't recognize this entity reference name (it isn't stored in the document type
			{
				if(parameterEntityMap.containsKey(entityReferenceName))	//if our map of entity references we've collected recently has this entity name
					return (XMLEntity)parameterEntityMap.getNamedItem(entityReferenceName);	//return the entity referenced by this name
				else  //if we can't find the parameter entity in the document time entities, nor in the entities we've collected so far
					throw new XMLUndefinedEntityReferenceException(entityReferenceName, entityReferenceLineIndex, entityReferenceCharIndex, reader.getName());	//show that we don't recognize the entity reference G***we should probably make a distinction between general and parameter entity references in the error; perhaps both functions could even somehow be combined
			}
		}
		catch(final ParseEOFException parseEOFException)	//if we run out of data
		{
			throw new XMLWellFormednessException(parseEOFException, ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME).getString(RESOURCE_PREFIX+PARAMETER_ENTITY_REFERENCE_RESOURCE), entityReferenceName!=null ? entityReferenceName : ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME).getString(RESOURCE_PREFIX+UNKNOWN_RESOURCE));	//show that we couldn't find the end of the parameter entity reference
		}
	}

	/**Parses an attribute-value pair, constructs an XMLAttribute object, and returns it.
	@param reader The reader from which to retrieve characters.
	@param ownerDocument The document which will own the nodes encountered.
	@exception IOException Thrown when an i/o error occurs.
	@exception XMLSyntaxException Thrown when there is a syntax error in the XML file.
	@exception XMLWellFormednessException Thrown when there is a well-formedness error in the XML file.
	@exception XMLUndefinedEntityReferenceException Thrown if a named entity reference has not been defined.
	@exception ParseUnexpectedDataException Thrown when an unexpected character is found.
//G***del	@exception ParseEOFException Thrown when the end of the input stream is reached unexpectedly.
	@return An XMLAttribute object with the values of the read attribute-value pair.
	@see XMLAttribute
	*/
	protected XMLAttribute parseAttribute(final XMLReader reader, final XMLDocument ownerDocument) throws IOException, XMLSyntaxException, XMLWellFormednessException, XMLUndefinedEntityReferenceException, ParseUnexpectedDataException//G***del, ParseEOFException
	{
//G***del System.out.println("XMLParse Starting to parse attributes.");	//G***del
		final long attributeLineIndex=reader.getLineIndex(), attributeCharIndex=reader.getCharIndex()+1;	//make a note of where the attribute will begins
		String attributeName="";	//this will receive the name of our attribute
		try
		{
			attributeName=reader.readStringUntilChar(WHITESPACE_CHARS+EQUAL_CHAR+'>');	//find the attribute name (which will end before the equals sign or whitespace), allowing ourselves to find the end-of-tag character in case we're tidying G***use a constant here
/*G***fix
		  if(reader.peekChar()=='>' && !isTidy()) //if this is the end of the tag, meaning this is an attribute with no value at the end of the tag, and we're not tidying
				reader.readExpectedChar(WHITESPACE_CHARS+EQUAL_CHAR); //
*/

			if(!isName(attributeName))	//if this isn't a valid name
				throw new XMLWellFormednessException(XMLWellFormednessException.INVALID_NAME, new Object[]{attributeName}, reader.getLineIndex(), reader.getCharIndex(), reader.getName());	//show that this isn't a valid XML name
//G***del Debug.trace("XMLParse attribute name: "+attributeName);	//G***del
			final XMLAttribute attribute=new XMLAttribute(ownerDocument, attributeName, attributeLineIndex, attributeCharIndex);	//create a new attribute-value pair object

			skipWhitespaceCharacters(reader);	//skip all whitespace characters (we already know the next character is valid, except maybe the end-of-tag character if we weren't tidying, which will be caught below)
		  if(isTidy())  //if we're tidying, there could be attributes with no values, such as <hr noshade />
		  {
				final char equalChar=reader.peekChar();  //see what the next character is
				if(equalChar!=EQUAL_CHAR) //if this isn't the '=' we expect
				{
					reader.unread(SPACE_CHAR);  //make sure there's whitespace after the attribute, since we just skipped all whitespace to find out there was no equals sign
				  attribute.setValue(attributeName);  //set the attribute value to the same as its name (e.g. noshade="noshade")
					return attribute; //default to an attribute without a specified value
				}
		  }
			reader.readExpectedChar(EQUAL_CHAR);	//read the equals sign; if we read the end-of-tag character earlier and we're not tidying, this will catch it
			skipWhitespaceCharacters(reader);	//skip all whitespace characters (we don't yet know if the next non-whitespace character is valid)

		  String attributeValueDelimiters; //we'll store the ending delimiter for the attribute value in this variable
			boolean isTidyingValue;  //whether or not we're tidying this attribute value
//G***del if we don't need			boolean discardLastDelimiter; //whether we should discard the last attribute value delimiter; normally we will discard the last delimiter (a quote character) if we are not tidying
			final char attributeValueFirstChar=reader.peekChar();	//see which character comes next
//G***del Debug.trace("Found first attribute value character: "+attributeValueFirstChar); //G***del
			if(ATTRIBUTE_VALUE_DELIMITERS.indexOf(attributeValueFirstChar)==-1 && isTidy()) //if this is not a valid attribute value delimiter, but we're tidying the document
			{
				attributeValueDelimiters=WHITESPACE_CHARS+'>';  //we'll accept whitespace or an end of the tag after the attribute value as a valid delimiter G***use a constant here
				isTidyingValue=true;  //show that we're tidying this value
//G***del if we don't need				discardLastDelimiter=false;  //since we're tidying, we don't have an ending quote to discard
//G***del Debug.trace("ready to tidy with: "+attributeValueDelimiters); //G***del
			}
			else  //if this is a valid attribute value delimiter, or we're not tidying
			{
				attributeValueDelimiters=String.valueOf(reader.readExpectedChar(ATTRIBUTE_VALUE_DELIMITERS));	//read the delimiter of the attribute value, expecting a single or double quote
				isTidyingValue=false;  //show that we're not tidying this value
//G***del if not needed				discardLastDelimiter=true;  //discard the last character we parse, which will be a quote character
			}
//G***del			final char attributeValueDelimiter=reader.readExpectedChar("\"'");	//read the delimiter of the attribute value, expecting a single or double quote G***make this a constant
			if(!parseAttributeValue(reader, ownerDocument, attribute, attributeValueDelimiters))	//parse the attribute; if we couldn't find the end of the attribute value
				throw new XMLWellFormednessException(XMLWellFormednessException.EOF, new Object[]{ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME).getString(RESOURCE_PREFIX+ATTRIBUTE_RESOURCE), attribute.getNodeName()}, reader.getLineIndex(), reader.getCharIndex(), reader.getName());	//show that we couldn't find the end of the attribute
//G***del Debug.trace("Ready to read that last attribute value delimiter, one of: "+attributeValueDelimiters); //G***del
//G***del if we don't need		  if(discardLastDelimiter)  //if we should discard the last delimiter
//G***del Debug.trace("Is tidying: "+isTidyingValue+", found value: "+attribute.getNodeValue());
		  if(isTidyingValue) //if we're tidying this value, we need to know whether or not to discard the following character
			{
/*G***fix
				reader.resetPeek(); //reset peeking so that we'll really be peeking the next character to come
				final char characterAfterValue=reader.peekChar();	//see which character comes after the value
//G***del Debug.trace("Tidying value, character is: "+characterAfterValue); //G***del
				if(isWhitespace(characterAfterValue)==-1) //if this is *not* a whitespace character (e.g. it is a '>')
				{
Debug.trace("Discarding character: "+characterAfterValue);  //G***del
					reader.readExpectedChar(characterAfterValue);	//discard the character (we already know what it will be)
				}
*/
			}
			else  //if we're not tidying this value
			{
//G***del Debug.trace("not tidying, reading one of: "+attributeValueDelimiters);  //G***del
				reader.readExpectedChar(attributeValueDelimiters);	//read the ending delimiter of the attribute value (we already know what it will be)
			}
//G***del Debug.trace("Read."); //G***del
			return attribute;	//return the attribute we created
		}
		catch(final ParseEOFException parseEOFException)	//if we run out of data
		{
			throw new XMLWellFormednessException(parseEOFException, ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME).getString(RESOURCE_PREFIX+ATTRIBUTE_RESOURCE), attributeName!=null ? attributeName : ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME).getString(RESOURCE_PREFIX+UNKNOWN_RESOURCE));	//show that we couldn't find the end of the attribute
		}

//G***del when works		return new XMLAttribute(ownerDocument, attributeName, attributeValue, attributeLineIndex, attributeCharIndex);	//create a new attribute-value pair object and return it
	}

	/**Parses an input stream that contains the value of an attribute.
		If this function reaches the end of the stream of the reader without finding
		an ending character for the attribute value, an exception is not thrown; rather, the return
		code will indicate if the end was found.
	@param reader The reader from which to retrieve characters.
	@param ownerDocument The document which will own the data.
	@param attribute The attribute that this value belongs to.
	@param attributeValueDelimiters A string of characters, any one of which can mark the end of this attribute value.
	This should be a single or double quote character.
	@exception IOException Thrown when an i/o error occurs.
	@exception XMLSyntaxException Thrown when there is a syntax error in the XML file.
	@exception XMLWellFormednessException Thrown when there is a well-formedness error in the XML file.
	@exception XMLUndefinedEntityReferenceException Thrown if a named entity reference has not been defined.
	@exception ParseUnexpectedDataException Thrown when an unexpected character is found.
//G***del	@exception ParseEOFException Thrown when the end of the input stream is reached unexpectedly.
	@return <code>true</code> if the ending character for this entity was found, else <code>false</code>.
	*/
	protected boolean parseAttributeValue(final XMLReader reader, final XMLDocument ownerDocument, final XMLAttribute attribute, final String attributeValueDelimiters) throws IOException, XMLSyntaxException, XMLWellFormednessException, XMLUndefinedEntityReferenceException, ParseUnexpectedDataException//G***del, ParseEOFException
	{

//G***del System.out.println("Parsing attribute value of attribute: "+attribute.getNodeName());	//G***del
		try
		{
			final StringBuffer attributeValueStringBuffer=new StringBuffer(); //create a string buffer for collecting attribute value data
			//G***we can probably make this even more efficient by carrying this string buffer along with us when we recurse
//G***del when works			String attributeValue="";	//we haven't parsed any content, yet
			while(true)	//keep reading content; when we find the ending character we'll return from there
			{
						//G***fix for parameter entities here
				attributeValueStringBuffer.append(reader.readStringUntilCharEOF("&<"+attributeValueDelimiters));	//read character content until we find a reference, an illegal less-than character, the end of the attribute, or until we reach the end of the file G***make these constants
				if(reader.isEOF())	//if we run out of characters looking for the end of the entity, this may not be an error; we may have just been parsing an entity reader for another entity
				{
					attribute.setNodeValue(attribute.getNodeValue()+attributeValueStringBuffer.toString());	//append whatever text we've collected so far to the attribute
					return false;	//return, showing that we have not yet found the end of the attribute
				}
				final char c=reader.peekChar();	//see which character comes next
//G***del Debug.trace("Peeking character: '"+c+"'"); //G***del
				if(attributeValueDelimiters.indexOf(c)!=-1)	//if the next character is the end of the attribute value
				{
//G***del Debug.trace("That character works");  //G***del
					attribute.setNodeValue(attribute.getNodeValue()+attributeValueStringBuffer.toString());	//append whatever text we've collected so far to the attribute
					return true;	//we're finished parsing all the attribute content we know about, so return
				}
				switch(c)	//see which character comes next
				{
					case '&':	//if this is a character or entity reference G***use a constant here
						if(reader.peekChar()=='#')	//if this is a character reference G***make this a constant
							attributeValueStringBuffer.append(parseCharacterReference(reader));	//parse this character reference and add the character to our attribute value
						else	//if this is not a character reference, it must be an entity reference
						{
							attribute.setNodeValue(attribute.getNodeValue()+attributeValueStringBuffer.toString());	//append whatever text we've collected so far to the attribute
							final Object entityResult=parseEntityReference(reader, ownerDocument);	//parse this entity reference and see which entity it refers to G***do we want to parse the entity and store everything as an entity reference node?
							if(entityResult instanceof XMLEntity)	//if we found a matching entity
							{
								final XMLEntity entity=(XMLEntity)entityResult;	//case the result to an entity
								if(nestedEntityReferenceMap.containsKey(entity))	//if, somewhere along our nested way, we've already processed this entity (i.e. this is a circular entity reference)
									throw new XMLWellFormednessException(XMLWellFormednessException.NO_RECURSION, new Object[]{entity.getNodeName()}, reader.getLineIndex(), reader.getCharIndex(), reader.getName());	//show that an entity can't reference itself, directly or indirectly G***we need to give a better position in the error, here
								if(entity.isExternalEntity())	//if this is an external entity
									throw new XMLWellFormednessException(XMLWellFormednessException.NO_EXTERNAL_ENTITY_REFERENCES, new Object[]{attribute.getNodeName(), entity.getNodeName()}, reader.getLineIndex(), reader.getCharIndex(), reader.getName());	//show that this attribute contains an external entity reference
								final XMLReader entityReader=new XMLReader(entity.getText(), "General entity \""+entity.getNodeName()+"\" from \""+entity.getSourceName()+"\"");	//create a string reader from the text of the entity, giving our entity name for the name of the reader G***Int

entityReader.tidy=isTidy();  //G***fix

	//G***del when works newio							final ParseReader entityReader=new ParseReader(new StringReader(entity.getText()), "General entity \""+entity.getNodeName()+"\" from \""+entity.getSourceName()+"\"");	//create a string reader from the text of the entity, giving our entity name for the name of the reader G***Int
								entityReader.setLineIndex(entity.getLineIndex());	//pretend we're reading where the entity was located in that file, so any errors will show the correct information
								entityReader.setCharIndex(entity.getCharIndex());	//pretend we're reading where the entity was located in that file, so any errors will show the correct information
		//G***del System.out.println("Found entity reference, the text of which is: "+entity.getText());	//G***del
								nestedEntityReferenceMap.put(entity, null);	//show that we're getting ready to process this entity
									//parse the entity text just as if it were included in the file;
									//  if the ending delimiter for the attribute was found in the entity replacement text, that's an error
								if(parseAttributeValue(entityReader, ownerDocument, attribute, attributeValueDelimiters))
									return true;	//show that we found the end of the element G***throw an error here
								nestedEntityReferenceMap.remove(entity);	//show that we're finished processing this entity
								attributeValueStringBuffer.setLength(0);  //we'll now start collecting new attribute data
							}
							else	//if there was no matching entity, yet this doesn't cause a well-formedness error
							{
								//G***if we're validating, we'll probably want to throw a validity error, here
	//G***del							attributeValue+=reader.readDelimitedStringInclusive("&", ";");	//read the entire entity reference and include it in the character data
								attributeValueStringBuffer.append((String)entityResult);	//store the entity reference name in our character data G***maybe construct this manually; we don't want to depend on toString() behavior, and constructing this manually will probably be faster
							}
						}
						break;
					case '<':	//if this is a less-than character in the attribute value G***use a constant here
						reader.read();	//read the character to update our character positions
						throw new XMLWellFormednessException(XMLWellFormednessException.NO_LT_IN_ATTRIBUTE_VALUES, new Object[]{attribute.getNodeName()}, reader.getLineIndex(), reader.getCharIndex(), reader.getName());	//show that this attribute contains a less-than character
					default:	//G***why do we check for end tag and also have a default?
						attributeValueStringBuffer.append(reader.readChar());	//add this character to what we've collected so far
						break;
				}
			}
		}
		catch(final ParseEOFException parseEOFException)	//if we run out of data
		{
			throw new XMLWellFormednessException(parseEOFException, ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME).getString(RESOURCE_PREFIX+ATTRIBUTE_RESOURCE), attribute.getNodeName());	//show that we couldn't find the end of the attribute
		}
	}

	/**Parses a stream of characters which supposedly starts with an external ID, constructs an XMLExternalID object, and returns it.
	@param reader The reader from which to retrieve characters.
//G***del	@param ownerDocument The document which will own the external ID.
	@exception IOException Thrown when an i/o error occurs.
	@exception XMLWellFormednessException Thrown when there is a well-formedness error in the XML file.
	@exception ParseUnexpectedDataException Thrown when an unexpected character is found.
//G***del	@exception ParseEOFException Thrown when the end of the input stream is reached unexpectedly.
	@return An XMLExternalID object with the appropriate identifiers.
	@see XMLExternalID
	*/
	protected XMLExternalID parseExternalID(final XMLReader reader/*G***del, final XMLDocument ownerDocument*/) throws IOException, XMLWellFormednessException, ParseUnexpectedDataException//G***del, ParseEOFException
	{
		try
		{
			final XMLExternalID externalID=new XMLExternalID();	//create a new external ID object to return
				//G***maybe use readexpected... to make sure that these characters are accurate on the fly
	//G***del if we don't need		final long attributeLineIndex=reader.getLineIndex(), attributeCharIndex=reader.getCharIndex();	//make a note of where the attribute begins
			reader.resetPeek();	//reset peeking so that the next peek will correctly return the next character to be read
			char literalDelimiter;	//we'll find out which character should be used for a delimiter in each case
			switch(reader.peekExpectedChar("SPsp"))	//see which character is next, and make sure it's one of the two we recognize; we don't care about case here, becuase if we're *not* tidying this will be checked later G***use a constant here
			{
				case 'S':	//if this is the start of "SYSTEM" G***use a constant here
				case 's':	//if this is the start of "SYSTEM" (if we're not tidying, the case will be caught below) G***use a constant here
					if(isTidy())  //if we're tidying
						reader.readExpectedStringIgnoreCase("SYSTEM");	//read the label for the system ID, ignoring case G***use a constant here
					else  //if we're not tidying
						reader.readExpectedString("SYSTEM");	//read the label for the system ID G***use a constant here
					reader.readExpectedChar(WHITESPACE_CHARS);	//we expect at least one whitespace character
					skipWhitespaceCharacters(reader);	//skip any other whitespace characters
					literalDelimiter=reader.readExpectedChar("\"'");	//read the delimiter of the system literal G***make this a constant
					externalID.setSystemID(reader.readStringUntilChar(literalDelimiter));	//read the system literal, which should end with the correct delimiter (a single or double quote, matching the first one)
					reader.readExpectedChar(literalDelimiter);	//read the ending delimiter (we already know what it will be)
					break;
				case 'P':	//if this is the start of "PUBLIC" G***use a constant here
				case 'p':	//if this is the start of "PUBLIC" (if we're not tidying, the case will be caught below) G***use a constant here
					if(isTidy())  //if we're tidying
						reader.readExpectedStringIgnoreCase("PUBLIC");	//read the label for the public ID, ignoring case G***use a constant here
					else  //if we're not tidying
						reader.readExpectedString("PUBLIC");	//read the label for the public ID G***use a constant here
					reader.readExpectedChar(WHITESPACE_CHARS);	//we expect at least one whitespace character
					skipWhitespaceCharacters(reader);	//skip any other whitespace characters
					literalDelimiter=reader.readExpectedChar("\"'");	//read the delimiter of the public ID literal G***make this a constant
					externalID.setPublicID(reader.readStringUntilChar(literalDelimiter));	//read the public ID literal literal, which should end with the correct delimiter (a single or double quote, matching the first one)
					reader.readExpectedChar(literalDelimiter);	//read the ending delimiter (we already know what it will be)
				  if(isTidy())  //if we're tidying, we can accept documents that do not have public system IDs
					{
						if(!isWhitespace(reader.peekChar()))  //if the next character is not a whitespace character
						{
							externalID.setSystemID(""); //set the system ID to the empty string, indicating no system ID was present
						  break;  //stop parsing the external ID, leaving the empty system ID
						}
					}
//G***del					else  //if we're not tidying, there must always be whitespace after the public ID
					reader.readExpectedChar(WHITESPACE_CHARS);	//we expect at least one whitespace character
					skipWhitespaceCharacters(reader);	//skip any other whitespace characters
					literalDelimiter=reader.readExpectedChar("\"'");	//read the delimiter of the system literal G***make this a constant
					externalID.setSystemID(reader.readStringUntilChar(literalDelimiter));	//read the system literal, which should end with the correct delimiter (a single or double quote, matching the first one)
					reader.readExpectedChar(literalDelimiter);	//read the ending delimiter (we already know what it will be)
					break;
			}
			return externalID;	//return the external ID we constructed
		}
		catch(final ParseEOFException parseEOFException)	//if we run out of data
		{
			throw new XMLWellFormednessException(parseEOFException, ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME).getString(RESOURCE_PREFIX+EXTERNAL_ID_RESOURCE), ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME).getString(RESOURCE_PREFIX+UNKNOWN_RESOURCE));	//show that we couldn't find the end of the external ID
		}
	}

	/**Parses an input stream that supposedly begins with a document type declaration ("<!DOCTYPE").
	@param reader The reader from which to retrieve characters.
	@param ownerDocument The document which will own the document type.
	@exception IOException Thrown when an i/o error occurs.
	@exception XMLSyntaxException Thrown when there is a syntax error in the XML file.
	@exception XMLWellFormednessException Thrown when there is a well-formedness error in the XML file.
	@exception XMLValidityException Thrown when there is a validity error in the XML file.
	@exception XMLUndefinedEntityReferenceException Thrown if a named entity reference has not been defined.
	@exception ParseUnexpectedDataException Thrown when an unexpected character is found.
//G***del	@exception ParseEOFException Thrown when the end of the input stream is reached unexpectedly.
	@return A new XML declaration constructed from the reader.
	*/
	protected XMLDocumentType parseDocumentType(final XMLReader reader, final XMLDocument ownerDocument) throws IOException, XMLSyntaxException, XMLWellFormednessException, XMLValidityException, XMLUndefinedEntityReferenceException, ParseUnexpectedDataException//G***del, ParseEOFException
	{
		final XMLDocumentType documentType=(XMLDocumentType)getXMLDocumentType().cloneXMLNode(true);	//we'll use the default XML document type we've already built G***do we want to do it this way?
		try
		{
			ownerDocument.setXMLDocumentType(documentType);	//tell the owner document which document type it has G***should this really go here? no, probably not; the receiving method should do this
			final XMLNodeList elementDeclarationList=new XMLNodeList();	//this list will hold references to the elements we create that represent element declarations
			final XMLNodeList attributeListDeclarationList=new XMLNodeList();	//this list will hold references to the elements we create that represent attribute list declarations
		  if(isTidy())  //if we're tidying the document
				reader.readExpectedStringIgnoreCase(DOCTYPE_DECL_START);	//ignore the case of the document type declaration
			else  //if we're reading the document normally
				reader.readExpectedString(DOCTYPE_DECL_START);	//we expect to read the start of an XML declaration
			reader.readExpectedChar(WHITESPACE_CHARS);	//we expect at least one whitespace character
			skipWhitespaceCharacters(reader);	//skip any whitespace characters
			final String documentTypeName=reader.readStringUntilChar(WHITESPACE_CHARS+"[>");	//read the name of the document type, which will end with whitespace or the beginning-of-internal-subset marker, or maybe even the end of the declaration G*** use constants here
			if(!isName(documentTypeName))	//if this isn't a valid name
				throw new XMLWellFormednessException(XMLWellFormednessException.INVALID_NAME, new Object[]{documentTypeName}, reader.getLineIndex(), reader.getCharIndex(), reader.getName());	//show that this isn't a valid XML name
			documentType.setNodeName(documentTypeName);	//set the name of the document type
			skipWhitespaceCharacters(reader);	//skip any whitespace characters
			final char c=reader.peekChar();	//see what the next character is
			if(c!='[' && c!='>')	//if we're not to the internal document type declaration subset, yet, and we're not to the end of the declaration, this must be an external ID G***use constants here
			{
				documentType.setExternalID(parseExternalID(reader));	//parse the external ID and use it to set the document type's external ID
				skipWhitespaceCharacters(reader);	//skip any whitespace characters
			}
			reader.resetPeek();	//reset peeking so that we'll start peeking at the next character to be read
			if(reader.peekChar()=='[')	//if we're ready to read the internal document type declaration subset
			{
				reader.readExpectedChar('[');	//G***fix, make constant
//G***del Debug.trace("Found left bracket, getting ready to read document type content.");
				//Parse the internal document type content; if we couldn't find the end
				//  of the element document type declaration, return an error. We're
				//  passing the entity and parameter declarations directly, so they will
				//  be updated immediately
//G***del when works				if(!parseDocumentTypeContent(reader, ownerDocument, documentType, elementDeclarationList, attributeListDeclarationList))
				if(!parseDocumentTypeContent(reader, ownerDocument, documentType.getEntityXMLNamedNodeMap(), documentType.getParameterEntityXMLNamedNodeMap(), elementDeclarationList, attributeListDeclarationList))
				{
//G***del Debug.trace("Before throwing XMLWellFormednessException from parsing the document type content, document type resource is: "+ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME).getString(RESOURCE_PREFIX+DOCUMENT_TYPE_RESOURCE));
					throw new XMLWellFormednessException(XMLWellFormednessException.EOF, new Object[]{ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME).getString(RESOURCE_PREFIX+DOCUMENT_TYPE_RESOURCE), documentType.getName()}, reader.getLineIndex(), reader.getCharIndex(), reader.getName());	//show that we couldn't find the end of the document type
				}
	//G***del				throw new ParseEOFException("XML Unexpected end of stream reached while searching for ending tag of internal document type declaration \""+documentType.getNodeName()+"\", which began at G***fix.", reader.getLineIndex(), reader.getCharIndex(), reader.getName());	//show that we hit the end of the file while searching for the end of the internal DTD G***Int
				reader.readExpectedChar(']');	//read the end of the internal DTD G***fix, make constant
				skipWhitespaceCharacters(reader);	//skip any whitespace characters
			}
			reader.readExpectedChar(DOCTYPE_DECL_END);	//the next character should be the ending delimiter of the declaration

		  if(isTidy())  //if we're tidying the document, see if we have an overriding external ID
			{
				final XMLExternalID tidyDocumentTypeExternalID=getTidyDocumentTypeExternalID();  //see if there is an overriding external ID to use
				if(tidyDocumentTypeExternalID!=null)  //if there is an external ID we should use in place of the given one
				{
					documentType.setExternalID(tidyDocumentTypeExternalID); //update the document's external ID to match the one required by tidying
				}
			}

			//if this document isn't standalone and has specified an external DTD
			if(!ownerDocument.getXMLDeclaration().isStandalone() && documentType.getSystemID()!=null && documentType.getSystemID().length()>0)  //G***these last checks are for tidy; what do we really want to do if there is no system ID?
			{
					//parse the external DTD subset
				parseExternalDocumentType(reader, ownerDocument, documentType, elementDeclarationList, attributeListDeclarationList);
			}
			//G***we might want to have a validate flag somewhere, in case they don't want to validate the document
/*G***fix all this when we actually do the content model right
			//associate all the attribute lists with the elements of the same name
			final XMLNamedNodeMap elementDeclarationMap=new XMLNamedNodeMap();	//create a new map to hold our element declarations
				//add all the elements type declarations in the list to our map, checking for duplicates
			for(int i=0; i<elementDeclarationList.size(); ++i)	//look at each of the element type declarations
			{
				final XMLElement elementDeclaration=(XMLElement)elementDeclarationList.item(i);	//get a reference to this element
	//G***del System.out.println("processing element declaration "+elementDeclaration.getAttribute("name"));	//G***del
				if(!elementDeclarationMap.containsKey(elementDeclaration.getAttribute("name")))	//if the map doesn't already contain a declaration for this element type G***use a constant here
					elementDeclarationMap.put(elementDeclaration.getAttribute("name"), elementDeclaration);	//add this element type declaration to our map G***use a constant here, maybe put these in a common variable
				else	//if this element type has already been declared
					throw new XMLValidityException(XMLValidityException.UNIQUE_ELEMENT_TYPE_DECLARATION, new Object[]{elementDeclaration.getAttribute("name")}, reader.getLineIndex(), reader.getCharIndex(), reader.getName());	//show that this element type has already been declared G***give a better indication of the element type declaration's location
			}
				//add all the attributes list declarations in the list to our map, ignoring duplicates G***should we ignore or combine duplicates?
			for(int elementIndex=0; elementIndex<attributeListDeclarationList.size(); ++elementIndex)	//look at each of the elements, each of which contain a list of attribute declarations
			{
				final XMLElement attributeListElement=(XMLElement)attributeListDeclarationList.item(elementIndex);	//get a reference to this element, which contains a list of attribute declarations
	//G***del System.out.println("processing attribute list declaration for element "+attributeListElement.getAttribute("name"));	//G***del
				if(elementDeclarationMap.containsKey(attributeListElement.getAttribute("name")))	//if our map contains an element declaration for list of attribute declarations (if it doesn't this apparently is *not* an XML validity error, so we'll just ignore that attribute list declaration) G***use a constant here
				{
					final XMLElement elementDeclaration=(XMLElement)elementDeclarationMap.getNamedItem(attributeListElement.getAttribute("name"));	//find out which element declaration these attributes should belong to G***use a constant
	//G***del System.out.println("Element "+attributeListElement.getAttribute("name")+" has been defined.");	//G***del
					for(int attributeIndex=0; attributeIndex<attributeListElement.getChildXMLNodeList().size(); ++attributeIndex)	//look at each of the attribute declarations in the list
					{
						final XMLElement attributeDeclaration=(XMLElement)attributeListElement.getChildXMLNodeList().item(attributeIndex);	//get a reference to this attribute declaration
						XMLElement existingAttributeDeclarationElement=null;	//we're going to determine if this element already has this attribute declared
						for(int i=0; i<elementDeclaration.getChildXMLNodeList().size(); ++i)	//look at each of the child nodes for this element, each of which will be an attribute declaration
						{
							final XMLElement thisAttributeDeclarationElement=(XMLElement)elementDeclaration.getChildXMLNodeList().get(i);	//get a reference to this node, which is an attribute declaration that the element already has
							if(thisAttributeDeclarationElement.getAttribute("name").equals(attributeDeclaration.getAttribute("name")))	//if this attribute declaration element has the same name as the one we're trying to add G***use constants here
							{
								existingAttributeDeclarationElement=thisAttributeDeclarationElement;	//show that we have an attribute declaration already
								break;	//stop looking for existing attribute declarations with this name
							}
						}
						if(existingAttributeDeclarationElement==null)	//if an attribute with this name has not been declared for this element G***if it does exist, is there some sort of XML combination rules?
						{
							elementDeclaration.appendChild(attributeDeclaration);	//add this attribute declaration element as a child node to our element declaration element
	//G***del System.out.println("Adding attribute \""+attributeDeclaration.getAttribute("name")+"\" to element \""+elementDeclaration.getAttribute("name")+"\".");	//G***del
						}
					}
				}
			}
*/
			return documentType;	//return the document type we constructed
		}
		catch(final ParseEOFException parseEOFException)	//if we run out of data
		{
			throw new XMLWellFormednessException(parseEOFException, ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME).getString(RESOURCE_PREFIX+DOCUMENT_TYPE_RESOURCE), documentType.getName()!=null ? documentType.getName() : ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME).getString(RESOURCE_PREFIX+UNKNOWN_RESOURCE));	//show that we couldn't find the end of the document type
		}
	}

	/**A map of DTD subsets being cached, keyed by public ID and/or private ID.
		Currently the associated object stored is an <code>XMLNamedNodeMap</code>
		representing the general entities in a DTD subset, with the parent of each
		set to <code>null</code>. G***eventually allow a special DTD subset object to be stored
	*/
	private final Map documentTypeSubsetCacheMap=new HashMap();

	/**Places a document type subset in the cache for easy retrieval. The object
		will be deep-cloned, and the parent of the cloned object and all child
		objects will be recursively set to null. The cloned object will be stored
		keyed to both the public ID and private ID, providing they are not null.
	@param publicID The public ID of the docuement type subset, or
		<code>null</code> if there is no public ID.
	@param systemID The system ID of the docuement type subset.
	*/
	protected void putCachedDocumentTypeSubset(final String publicID, final String systemID, final XMLNamedNodeMap generalEntityMap)
	{
		final XMLNamedNodeMap clonedEntityMap=generalEntityMap.cloneDeep();  //make a deep clone of the map
		clonedEntityMap.setOwnerXMLDocument(null);  //show that the clone has no owner document
		if(publicID!=null) //if there is a public ID
			documentTypeSubsetCacheMap.put(publicID, clonedEntityMap);  //put the DTD subset in the map keyed by its public ID
		if(systemID!=null) //if there is a system ID
			documentTypeSubsetCacheMap.put(systemID, clonedEntityMap);  //put the DTD subset in the map keyed by its system ID
	}

	/**Attempts to retrieve a cached document type subset, first by a public ID
		and then by a private ID. The retrieve object, if found, is first cloned
		before being returned.
	@param publicID The public ID of the document type which should be retrieved.
	@param systemID The system ID of the document type which should be retrieved.
	@return A clone of a cached document type subset, if one could be found.
	*/
	protected XMLNamedNodeMap getCachedDocumentTypeSubset(final String publicID, final String systemID)
	{
		XMLNamedNodeMap generalEntityMap=null;  //we'll try to get this object from the map
		if(publicID!=null)  //if they passed a valid public ID
			generalEntityMap=(XMLNamedNodeMap)documentTypeSubsetCacheMap.get(publicID);  //attempt to get the document type subset from the map using the public ID as a key
		if(generalEntityMap==null && systemID!=null)  //if we didn't find the document type subset, but they passed a system ID
			generalEntityMap=(XMLNamedNodeMap)documentTypeSubsetCacheMap.get(systemID);  //attempt to get the document type subset from the map using the system ID as a key
		return generalEntityMap!=null ? generalEntityMap.cloneDeep() : null;  //return a clone of the document type subset we found, if we found one
	}

	/**Parses the contents of an external document type declaration.
	@param reader The reader from which to retrieve characters.
	@param ownerDocument The document which will own the document type.
	@param documentType The document type being processed.
	@param elementDeclarationList The list of elements that will be created to represent element declarations.
	@param attributeListDeclarationList The list of elements that will be created to represent attribute list declarations.
	@exception IOException Thrown when an i/o error occurs.
	@exception XMLSyntaxException Thrown when there is a syntax error in the XML file.
	@exception XMLWellFormednessException Thrown when there is a well-formedness error in the XML file.
	@exception XMLUndefinedEntityReferenceException Thrown if a named entity reference has not been defined.
	@exception ParseUnexpectedDataException Thrown when an unexpected character is found.
	@exception ParseEOFException Thrown when the end of the input stream is reached unexpectedly.
	*/
	protected void parseExternalDocumentType(final XMLReader reader, final XMLDocument ownerDocument, final XMLDocumentType documentType, final XMLNodeList elementDeclarationList, final XMLNodeList attributeListDeclarationList) throws IOException, XMLSyntaxException, XMLWellFormednessException, XMLUndefinedEntityReferenceException, ParseUnexpectedDataException, ParseEOFException
	{
/*G***fix
final XMLDocument ownerDocument, final XMLDocumentType documentType

*/
		final String publicID=documentType.getPublicID(); //get a reference to the document type's public ID, if there is one
		final String systemID=documentType.getSystemID(); //get a reference to the document type's system ID
			//we'll first try to get the DTD subset information from the cache
		XMLNamedNodeMap parameterEntityMap; //G*** eventually replace these with an object specifically for DTD subsets
		XMLNamedNodeMap generalEntityMap=getCachedDocumentTypeSubset(documentType.getPublicID(), documentType.getSystemID()); //try to get the document type subset from the cache
		if(generalEntityMap!=null)  //if we found a document type subset with either the same public ID or system ID that we have already loaded
		{
//G***del Debug.trace("Found a cached document type subset for public ID: ", publicID);
//G***del Debug.trace("Found a cached document type subset for system ID: ", systemID);
		  generalEntityMap.setOwnerXMLDocument(ownerDocument);  //set the owner document of the document type subset
		  parameterEntityMap=null;  //G***fix by getting from the cache
		}
		else  //if we weren't able to find a cached document type subset
		{
//G***del Debug.trace("Did not find a cached document type subset for public ID: ", publicID);
//G***del Debug.trace("Did not find a cached document type subset for system ID: ", systemID);
			generalEntityMap=new XMLNamedNodeMap(); //eventually replace these with an object specifically for DTD subsets
			parameterEntityMap=new XMLNamedNodeMap();
			final XMLReader externalDTDReader=createReader(reader.getSourceObject(), documentType.getPublicID(), documentType.getSystemID());	//G***testing; comment
			try
			{
					//G***change the code to know whether it should look for an ending ']'
				parseDocumentTypeContent(externalDTDReader, ownerDocument, /*G***fix or del documentType, */generalEntityMap, parameterEntityMap, elementDeclarationList, attributeListDeclarationList);	//parse the external document type content
					//cache the document type subset we just loaded
				putCachedDocumentTypeSubset(documentType.getPublicID(), documentType.getSystemID(), generalEntityMap);
			}
			finally
			{
				externalDTDReader.close();	//always close the reader we created
			}
		}
			//store the general entities in the document
		final Iterator entityIterator=generalEntityMap.values().iterator(); //get an iterator of all the enties we just found
		while(entityIterator.hasNext())  //while there are entities
		{
			final XMLEntity xmlEntity=(XMLEntity)entityIterator.next(); //get the next entity
			if(!documentType.getEntityXMLNamedNodeMap().containsNamedItem(xmlEntity))	//if we haven't already defined this general entity
				documentType.getEntityXMLNamedNodeMap().setNamedItem(xmlEntity);	//store the entity in our named node map G***this will eventually allow a DOMException to be thrown; reflect this
		}
		//G***fix; right now, the parameter entities never get placed in the document type object
	}

	/**Parses an input stream that contains content of a document type.
		If this function reaches the end of the stream of the reader without finding
		an ending tag for the element, an exception is not thrown; rather, the return
		code will indicate if the end tag was found.
	@param reader The reader from which to retrieve characters.
	@param ownerDocument The document which will own this data.
//G***fix	@param documentType The document type being processed.
	@param generalEntityMap The map of general entities which will be created and returned. G***eventually pass all these in one encapsulating object
	@param parameterEntityMap The map of general entities which will be created and returned. G***eventually pass all these in one encapsulating object
	@param elementDeclarationList The list of elements that will be created to
		represent element declarations, or <code>null</code> if these elements
		need not be returned. TODO fix with new DOM storage of these elements
	@param attributeListDeclarationList The list of elements that will be created
		to represent attribute list declarations, or <code>null</code> if these
		attributes need not be returned. TODO fix with new DOM storage of these attributes
	@exception IOException Thrown when an i/o error occurs.
	@exception XMLSyntaxException Thrown when there is a syntax error in the XML file.
	@exception XMLWellFormednessException Thrown when there is a well-formedness error in the XML file.
	@exception XMLUndefinedEntityReferenceException Thrown if a named entity reference has not been defined.
	@exception ParseUnexpectedDataException Thrown when an unexpected character is found.
	@exception ParseEOFException Thrown when the end of the input stream is reached unexpectedly.
//G***fix	@return <code>true</code> if the ending tag for this element was found, else <code>false</code>.
	*/
		//G***have a variable here that specifies whether a conditional parameter entity existed; if so, we won't be able to cache the result
	protected boolean parseDocumentTypeContent(final XMLReader reader, final XMLDocument ownerDocument, /*G***fix or del final XMLDocumentType documentType, */final XMLNamedNodeMap generalEntityMap, final XMLNamedNodeMap parameterEntityMap, final XMLNodeList elementDeclarationList, final XMLNodeList attributeListDeclarationList) throws IOException, XMLSyntaxException, XMLWellFormednessException, XMLUndefinedEntityReferenceException, ParseUnexpectedDataException, ParseEOFException
	{
	//G***del	try
		{
			//the markup strings we expect in this document type declaration
			final String[] EXPECTED_DTD_MARKUP_STRINGS={INTERNAL_DTD_SUBSET_END, PARAMETER_ENTITY_REF_START, ENTITY_DECL_START, ELEMENT_TYPE_DECL_START, ATTLIST_DECL_START, COMMENT_START};
			//the indexes of the DTD markup strings in our array
			final int END_OF_INTERNAL_DTD_SUBSET=0, PARAMETER_ENTITY_REF=1, ENTITY_DECL=2, ELEMENT_TYPE_DECL=3, ATTLIST_DECL=4, COMMENT=5;
			while(true)	//keep reading content; when we find the end of the data we'll return from there
			{
				skipWhitespaceCharactersEOF(reader);	//skip any whitespace characters, but don't throw an exception if we reach the end of the stream
				if(reader.isEOF())	//if we run out of characters looking for the start of the next document type markup, this may not be an error; we may be processing an external DTD
					return false;	//return, showing that we have not yet found the ending character marker of an internal DTD
				switch(reader.peekExpectedStrings(EXPECTED_DTD_MARKUP_STRINGS))	//see what sort of DTD declaration markup this is G***add other types of declarations
				{
					case END_OF_INTERNAL_DTD_SUBSET:	//if this is the end of the document type content
//G***del Debug.trace("Found end of internal DTD subset.");
						return true;	//G***fix
					case PARAMETER_ENTITY_REF:	//if this is a parameter entity reference
						{
//G***del Debug.trace("Found parameter entity ref.");
							final XMLEntity entity=parseParameterEntityReference(reader, ownerDocument, parameterEntityMap);	//parse this entity reference and see which entity it refers to
							final XMLReader entityReader=createEntityReader(reader.getSourceObject(), entity);	//we'll use this to read either from the internal value or an external file
							try
							{
								if(parseDocumentTypeContent(entityReader, ownerDocument, /*G***fix documentType, */generalEntityMap, parameterEntityMap, elementDeclarationList, attributeListDeclarationList))	//parse the entity text just as if it were included in the file (except for markup across entity boundaries); if the ending markup for the DTD for the element was found G***fix; how can they find the ']' in a parameter entity?
									return true;	//show that we found the end of the element
							}
							finally
							{
								entityReader.close();	//always close the entity reader
							}
						}
						break;	//G***perhaps have a more instructive error here if they try to split up general entities across entities
	//G***del or fix					reader.readExpectedChar(']');	//read the end of the internal document type content G***use constant
					case ENTITY_DECL:	//if this is an entity declaration
						{
//G***del Debug.trace("Found entity declaration.");
//G***del Debug.trace("Ready to parse entity declaration.");	//G***del
							final XMLEntity entity=parseEntityDeclaration(reader, ownerDocument);	//parse the entity declaration
//G***del Debug.trace("Parsed entity declaration: "+entity.getNodeName());	//G***del
							if(entity.isParameterEntity())	//if the entity is a parameter entity
							{		//G***do parameter entities have the same action of ignoring multiple definitions?
								if(!parameterEntityMap.containsNamedItem(entity))	//if we haven't already defined this parameter entity
									parameterEntityMap.setNamedItem(entity);	//store the entity in our named node map G***this will eventually allow a DOMException to be thrown; reflect this
/*G***del when caching works
								if(!documentType.getParameterEntityXMLNamedNodeMap().containsNamedItem(entity))	//if we haven't already defined this parameter entity
									documentType.getParameterEntityXMLNamedNodeMap().setNamedItem(entity);	//store the entity in our named node map G***this will eventually allow a DOMException to be thrown; reflect this
*/
							}
							else	//if this is a general entity
							{
								if(!generalEntityMap.containsNamedItem(entity))	//if we haven't already defined this general entity
									generalEntityMap.setNamedItem(entity);	//store the entity in our named node map G***this will eventually allow a DOMException to be thrown; reflect this
/*G***del when caching works
								if(!documentType.getEntityXMLNamedNodeMap().containsNamedItem(entity))	//if we haven't already defined this general entity
									documentType.getEntityXMLNamedNodeMap().setNamedItem(entity);	//store the entity in our named node map G***this will eventually allow a DOMException to be thrown; reflect this
*/
		//G***use an else here to display a warning if the node existed already
							}
						}
						//G***do we want a default case here?
						break;
					case ELEMENT_TYPE_DECL:	//if this is an element type declaration
//G***del Debug.trace("Found element type declaration.");
						{
							final XMLElement elementTypeDeclaration=parseElementTypeDeclaration(reader, ownerDocument, parameterEntityMap);	//parse this element type declaration G***fix
							if(elementDeclarationList!=null)	//TODO this check will go away when we store these inside the document type itself
								elementDeclarationList.add(elementTypeDeclaration);	//add the resulting element to our declaration list G***fix
						}
						break;
					case ATTLIST_DECL:	//if this is an attribute declaration
//G***del Debug.trace("Found attribute list declaration.");
						{
							final XMLElement attributeListDeclaration=parseAttributeListDeclaration(reader, ownerDocument, parameterEntityMap);	//parse this attribute list declaration G***fix
							if(attributeListDeclarationList!=null)	//TODO this check will go away when we store these inside the document type itself
								attributeListDeclarationList.add(attributeListDeclaration);	//add the resulting element to our declaration list G***fix
						}
						break;
					case COMMENT:	//if this is a comment
//G***del Debug.trace("Found comment.");
						parseComment(reader, ownerDocument);	//G***fix, comment
						break;
				}
			}
		}
/*G***del
		catch(final ParseEOFException parseEOFException)	//if we run out of data
		{
			throw new XMLWellFormednessException(parseEOFException, ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME).getString(RESOURCE_PREFIX+DOCUMENT_TYPE_RESOURCE), documentType.getName());	//show that we couldn't find the end of the document type content
		}
*/
	}

	/**Parses an input stream that supposedly begins with an entity declaration.
	@param reader The reader from which to retrieve characters.
	@param ownerDocument The document which will own this entity.
	@exception IOException Thrown when an i/o error occurs.
	@exception XMLSyntaxException Thrown when there is a syntax error in the XML file.
	@exception XMLWellFormednessException Thrown when there is a well-formedness error in the XML file.
	@exception XMLUndefinedEntityReferenceException Thrown if a named entity reference has not been defined.
	@exception ParseUnexpectedDataException Thrown when an unexpected character is found.
//G***del	@exception ParseEOFException Thrown when the end of the input stream is reached unexpectedly.
	@return A new XML entity constructed from the reader.
	*/
	protected XMLEntity parseEntityDeclaration(final XMLReader reader, final XMLDocument ownerDocument) throws IOException, XMLSyntaxException, XMLWellFormednessException, XMLUndefinedEntityReferenceException, ParseUnexpectedDataException//G***del, ParseEOFException
	{
		final XMLEntity entity=new XMLEntity(ownerDocument);	//create a new XML entity
		try
		{
	//G***del System.out.println("Parsing entity declaration.\n");	//G***del
			reader.readExpectedString(ENTITY_DECL_START);	//we expect to read the start of an entity declaration
			reader.readExpectedChar(WHITESPACE_CHARS);	//we expect at least one whitespace character
			skipWhitespaceCharacters(reader);	//skip any other whitespace characters
			if(reader.peekChar()=='%')	//if this is a parameter entity reference G***use a constant here
			{
				entity.setParameterEntity(true);	//we now know that this is a parameter entity
				reader.readExpectedChar('%');	//read the parameter entity reference identifier
				reader.readExpectedChar(WHITESPACE_CHARS);	//we expect at least one whitespace character
				skipWhitespaceCharacters(reader);	//skip any other whitespace characters
			}
			final String entityName=reader.readStringUntilChar(WHITESPACE_CHARS);	//read the name of the entity, which will end with whitespace
			if(!isName(entityName))	//if this isn't a valid name
				throw new XMLWellFormednessException(XMLWellFormednessException.INVALID_NAME, new Object[]{entityName}, reader.getLineIndex(), reader.getCharIndex(), reader.getName());	//show that this isn't a valid XML name
			entity.setNodeName(entityName);	//set the name of the entity
			reader.readExpectedChar(WHITESPACE_CHARS);	//we expect at least one whitespace character
			skipWhitespaceCharacters(reader);	//skip any other whitespace characters
			final char c=reader.peekChar();	//see what the next character will be
			if(c=='"' || c=='\'')	//if the next character is a single or double quote, this is an entity value
			{
				final char entityValueDelimiter=reader.readExpectedChar("\"'");	//read the delimiter of the entity value G***make this a constant
						//G***should this be parsed right away?
	//G***del if we don't need			final XMLText entityText=new XMLText(ownerDocument, reader.readStringUntilChar(entityValueDelimiter));	//read the entity value, which should end with the correct delimiter (a single or double quote, matching the first one)
	//G***should we pass the text node instead?
				//now that we've found where this entity starts, show it's location in the XML document
				entity.setSourceName(reader.getName());	//store where we found the entity
				entity.setLineIndex(reader.getLineIndex());	//store which line the entity starts on
				entity.setCharIndex(reader.getCharIndex());	//store which character the entity starts on
			if(!parseEntityContent(reader, ownerDocument, entity, entityValueDelimiter))	//parse the entity content; if we couldn't find the end of the entity
			{
//G***del Debug.trace("Before throwing XMLWellFormednessException from parsing the entity content, entity declartion resource is: "+ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME).getString(RESOURCE_PREFIX+ENTITY_DECLARATION_RESOURCE));
				throw new XMLWellFormednessException(XMLWellFormednessException.EOF, new Object[]{ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME).getString(RESOURCE_PREFIX+ENTITY_DECLARATION_RESOURCE), entity.getNodeName()}, reader.getLineIndex(), reader.getCharIndex(), reader.getName());	//show that we couldn't find the end of the entity declaration
			}
	//G***del			throw new ParseEOFException("XML unexpected end of stream reached while searching for end of entity \""+entity.getNodeName()+"\", which began at G***fix.", reader.getLineIndex(), reader.getCharIndex(), reader.getName());	//show that we hit the end of the file while searching for the ending deliminter of the entity G***Int
			reader.readExpectedChar(entityValueDelimiter);	//read the ending delimiter (we already know what it will be)

	//G***del System.out.println("Entity value: "+entity.getFirstChild().getNodeValue());	//G***del

					//G***do something with the entity text, here
			}
			else	//if, instead of an entity value, there is an external ID
			{
				entity.setExternalID(parseExternalID(reader));	//parse the external ID and use it to set the entity's external ID
				reader.peekExpectedChar(WHITESPACE_CHARS+'>');	//make sure the next character is one we expect (there has to be whitespace unless it's the end of the entity declaration) G***make this a constant
				skipWhitespaceCharacters(reader);	//skip any whitespace characters
				if(reader.peekChar()=='N')	//if this is the start of an NDATA declaration
				{
					//G***fix NDATA parsing
				}

	/*G***fix or del
	System.out.println("Getting ready to read data for entity: "+entity.getNodeName());	//G***del

						//G***soon, change this to lazy evaluation so that we don't immediately load the content from the other file

				InputStream inputStream;	//we don't know where our input stream is coming from, yet
				try
				{
					inputStream=new URL(entity.getSystemID()).openConnection().getInputStream();	//assume the location is an Internet URL and try to open a connection to it
				}
				catch(MalformedURLException e)	//if the location isn't a valid URL
				{
	System.out.println(entity.getSystemID()+" must be a file.");	//G***del

					inputStream=new FileInputStream(entity.getSystemID());	//assume it's a file and try to open it
				}
				final ParseReader externalEntityReader=new ParseReader(inputStream, entity.getSystemID());	//create an XML reader that will read from the input stream

				entity.setSourceName(externalEntityReader.getName());	//store where we found the entity
				entity.setLineIndex(externalEntityReader.getLineIndex());	//store which line the entity starts on
				entity.setCharIndex(externalEntityReader.getCharIndex());	//store which character the entity starts on
				parseEntityContent(externalEntityReader, ownerDocument, entity, '~');	//parse the entity content G***fix so that parseEntityContent will ignore the delimiter character

	System.out.println("Entity value: "+entity.getFirstChild().getNodeValue());	//G***del
	*/

			}
			skipWhitespaceCharacters(reader);	//skip any whitespace characters
			reader.readExpectedChar(ENTITY_DECL_END);	//the next character should be the ending delimiter of the declaration
			return entity;	//return the entity we constructed
		}
		catch(final ParseEOFException parseEOFException)	//if we run out of data
		{
			throw new XMLWellFormednessException(parseEOFException, ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME).getString(RESOURCE_PREFIX+ENTITY_DECLARATION_RESOURCE), entity.getNodeName()!=null ? entity.getNodeName() : ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME).getString(RESOURCE_PREFIX+UNKNOWN_RESOURCE));	//show that we couldn't find the end of the entity declaration
		}
	}

	/**Parses an input stream that contains content of an entity.
	If this function reaches the end of the stream of the reader without finding
	an ending character for the entity, an exception is not thrown; rather, the return
	code will indicate if the end was found.
	@param reader The reader from which to retrieve characters.
	@param ownerDocument The document which will own the data.
	@param entity The entity that this content belongs to.
	@param entityValueDelimiter The character which marks the end of this entity value.
	This should be a single or double quote character.
	@exception IOException Thrown when an i/o error occurs.
	@exception XMLSyntaxException Thrown when there is a syntax error in the XML file.
	@exception XMLWellFormednessException Thrown when there is a well-formedness error in the XML file.
	@exception XMLUndefinedEntityReferenceException Thrown if a named entity reference has not been defined.
	@exception ParseUnexpectedDataException Thrown when an unexpected character is found.
	@exception ParseEOFException Thrown when the end of the input stream is reached unexpectedly.
	@return <code>true</code> if the ending character for this entity was found, else <code>false</code>.
	*/
	protected boolean parseEntityContent(final XMLReader reader, final XMLDocument ownerDocument, final XMLEntity entity, final char entityValueDelimiter) throws IOException, XMLSyntaxException, XMLWellFormednessException, XMLUndefinedEntityReferenceException, ParseUnexpectedDataException, ParseEOFException
	{
//G***del		try
		{
	//G***del System.out.println("Parsing entity content.");	//G***del
	//G***del System.out.println("Read char: "+reader.readChar());	//G***del
			//G***store the start position of the element so we can report the error location
		  final StringBuffer entityValueStringBuffer=new StringBuffer();  //we haven't parsed any content, yet
			while(true)	//keep reading content; when we find the ending character we'll return from there
			{
						//G***fix for parameter entities here
				entityValueStringBuffer.append(reader.readStringUntilCharEOF("&"+entityValueDelimiter));	//read character content until we find a reference, the end of the entity declaration, or until we reach the end of the file G***make these constants

	//G***del System.out.println("Read so far: "+entityValue);	//G***del

	//G***del			characterContent+=expandReferences(charactersRead);	//parse the character data, which may have character and/or entity references, which themselves may contain character and/or entity references
				if(reader.isEOF())	//if we run out of characters looking for the end of the entity, this may not be an error; we may have just been parsing an entity reader for another entity
				{
					entity.appendText(entityValueStringBuffer.toString());	//append whatever text we've collected so far to the entity
					return false;	//return, showing that we have not yet found the end of the entity
				}
				final char c=reader.peekChar();	//see which character comes next
				if(c==entityValueDelimiter)	//if the next character is the end of the entity declaration
				{
					entity.appendText(entityValueStringBuffer.toString());	//append whatever text we've collected so far to the entity
					return true;	//we're finished parsing all the entity content we know about, so return
				}
				//G***somehow, we need to know if we should accept parameter entity references here
				switch(c)	//see which character comes next
				{
					case '&':	//if this is a character or entity reference
						if(reader.peekChar()=='#')	//if this is a character reference G***make this a constant
							entityValueStringBuffer.append(parseCharacterReference(reader));	//parse this character reference and add the character to our entity value
						else	//if this is not a character reference, it must be an entity reference
							entityValueStringBuffer.append(reader.readChar());	//we'll not parse general entities inside entities, so read the beginning of the entity so that the rest of it won't be parsed as an entity
	/*G***del
						{
							entityValue+=reader.readStringUntilChar(';');	//we'll not parse general entities inside entities, so just read the entity reference normally G***make these constants
							entityValue+=reader.readExpectedChar(';');	//read the end-of-entity
						}
	*/
	//G***del						reader.skipChars(1);	//for now, skip the character G***fix
						//G***fix for entity references; we should ignore them and keep the actual characters
						break;
					default:	//G***why do we check for end tag and also have a default?
						entity.appendText(entityValueStringBuffer.toString());	//append whatever text we've collected so far to the entity
						return true;	//we're finished parsing all the entity content we know about, so return
				}
			}
		}
/*G***del
		catch(final ParseEOFException parseEOFException)	//if we run out of data
		{
			throw new XMLWellFormednessException(parseEOFException, ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME).getString(RESOURCE_PREFIX+ENTITY_RESOURCE), entity.getNodeName());	//show that we couldn't find the end of the entity
		}
*/
	}

	/**Parses an input stream that supposedly begins with an element type declaration.
	@param reader The reader from which to retrieve characters.
	@param ownerDocument The document which will own this element type declaration.
	@param parameterEntityMap The parameter entities we've collected so far.
	@exception IOException Thrown when an i/o error occurs.
	@exception XMLSyntaxException Thrown when there is a syntax error in the XML file.
	@exception XMLWellFormednessException Thrown when there is a well-formedness error in the XML file.
	@exception XMLUndefinedEntityReferenceException Thrown if a named entity reference has not been defined.
	@exception ParseUnexpectedDataException Thrown when an unexpected character is found.
	@exception ParseEOFException Thrown when the end of the input stream is reached unexpectedly. G***fix
	@return A new XML element constructed from the reader containing the declaration information for this element.
	*/
	protected XMLElement parseElementTypeDeclaration(final XMLReader reader, final XMLDocument ownerDocument, final XMLNamedNodeMap parameterEntityMap) throws IOException, XMLSyntaxException, XMLWellFormednessException, XMLUndefinedEntityReferenceException, ParseUnexpectedDataException, ParseEOFException
	{
//G***del System.out.println("parsing element type declaration");	//G***del
		final XMLElement element=new XMLElement(ownerDocument, "element");	//create a new XML element which will hold the rules for this element declaration G***use constant G***use the document createElement() function
//G***del System.out.println("Parsing entity declaration.\n");	//G***del
		reader.readExpectedString(ELEMENT_TYPE_DECL_START);	//we expect to read the start of an element type declaration
		reader.readExpectedChar(WHITESPACE_CHARS);	//we expect at least one whitespace character
		skipWhitespaceCharacters(reader);	//skip any other whitespace characters
		element.setAttribute("name", reader.readStringUntilChar(WHITESPACE_CHARS));	//set the name attribute, specifying the name of the element, which will end with whitespace G***use a constant here
		reader.readExpectedChar(WHITESPACE_CHARS);	//we expect at least one whitespace character
		skipWhitespaceCharacters(reader);	//skip any other whitespace characters
		//the content specification strings we expect
		final String[] CONTENT_SPEC_STRINGS={ELEMENT_CONTENT_ANY, ELEMENT_CONTENT_EMPTY, ""};
		//the indexes of the content specification strings in our array
		final int ANY=0, EMPTY=1;
		reader.resetPeek();	//reset peeking so that the next character peeked will reflect the next character to be read
		switch(reader.peekExpectedStrings(CONTENT_SPEC_STRINGS))	//see what kind of markup this is
		{
			case ANY:	//if this specifies any content
				reader.readExpectedString(ELEMENT_CONTENT_ANY);	//read the "any" content spec
				element.setAttribute("content", "any");	//add an attribute specifying any content G***use constants
				break;
			case EMPTY:	//if this specifies no content
				reader.readExpectedString(ELEMENT_CONTENT_EMPTY);	//read the "empty" content spec
				element.setAttribute("content", "empty");	//add an attribute specifying no content G***use constants
				break;
			default:	//if neither "any" nor "empty" was found
				final String contentSpec=reader.readStringUntilChar('>');	//read the content spec string
				element.setAttribute("content", contentSpec);	//add an attribute specifying the content specification G***use constants
				break;
		}
		skipWhitespaceCharacters(reader);	//skip any other whitespace characters
		reader.readExpectedChar(ELEMENT_TYPE_DECL_END);	//the next character should be the ending delimiter of the declaration
		return element;	//return the element we constructed
	}

	/**Parses an input stream that supposedly begins with an attribute list declaration.
	@param reader The reader from which to retrieve characters.
	@param ownerDocument The document which will own this element type declaration.
	@param parameterEntityMap The parameter entities we've collected so far.
	@exception IOException Thrown when an i/o error occurs.
	@exception XMLSyntaxException Thrown when there is a syntax error in the XML file.
	@exception XMLWellFormednessException Thrown when there is a well-formedness error in the XML file.
	@exception XMLUndefinedEntityReferenceException Thrown if a named entity reference has not been defined.
	@exception ParseUnexpectedDataException Thrown when an unexpected character is found.
	@exception ParseEOFException Thrown when the end of the input stream is reached unexpectedly.
	@return A new XML element constructed from the reader containing the declaration information for this element.
	*/
	protected XMLElement parseAttributeListDeclaration(final XMLReader reader, final XMLDocument ownerDocument, final XMLNamedNodeMap parameterEntityMap) throws IOException, XMLSyntaxException, XMLWellFormednessException, XMLUndefinedEntityReferenceException, ParseUnexpectedDataException, ParseEOFException
	{
		//the attribute type strings we expect; notice that "IDREFS", "IDREF", and "ID" must be in that order because the first match is always chosen
		final String[] ATTTYPE_STRINGS={ATTTYPE_CDATA, ATTTYPE_IDREFS, ATTTYPE_IDREF, ATTTYPE_ID, ATTTYPE_ENTITY, ATTTYPE_ENTITIES, ATTTYPE_NMTOKEN, ATTTYPE_NMTOKENS, ATTTYPE_NOTATION, ATTTYPE_ENUMERATION_START};
		//the indexes of the attribute type strings in our array
		final int CDATA=0, IDREFS=1, IDREF=2, ID=3, ENTITY=4, ENTITIES=5, NMTOKEN=6, NMTOKENS=7, NOTATION=8, ENUMERATION=9;

//G***del System.out.println("parsing attribute list declaration");	//G***del

		final XMLElement element=new XMLElement(ownerDocument, "element");	//create a new XML element which will hold the attribute list for an element declaration G***use constant
		final String attributeListDeclarationString=reader.readDelimitedString(ATTLIST_DECL_START, ATTLIST_DECL_END);	//read all the characters in the attribute list declaration between its start and end

//G***del System.out.println("Before expanding references: "+attributeListDeclarationString);	//G***del

			//here, it would be nice to resolve the parameter entity references as we come to them, but they can be in so many places that we'll just resolve them all at once
				//G***specify the filename to the reader here somehow, so when an error occurs we can tell what file it is
		XMLReader attributeListDeclarationReader=new XMLReader(attributeListDeclarationString, reader.getName()+":Attribute List Declaration");	//create a string reader the attribute list declaration, but we don't know its name G***Int G***what if the object is null?

attributeListDeclarationReader.tidy=isTidy();  //G***fix

//G***del when works newio		ParseReader attributeListDeclarationReader=new ParseReader(new StringReader(attributeListDeclarationString), "Attribute List Declaration");	//create a string reader the attribute list declaration, but we don't know its name G***Int
		attributeListDeclarationReader.setLineIndex(reader.getLineIndex());	//pretend we're reading where we already were, so any errors will show at least something close (hopefully)
		attributeListDeclarationReader.setCharIndex(reader.getCharIndex());	//pretend we're reading where we already were, so any errors will show at least something close (hopefully)
		  //create a string buffer into which we can expand the attribute list declaration;
			//  we know it will be at least as long as the original string
		final StringBuffer expandedAttributeListDeclarationStringBuffer=new StringBuffer(attributeListDeclarationString.length());
//G***del when works		attributeListDeclarationString=expandParameterEntityReferences(attributeListDeclarationReader, ownerDocument, parameterEntityMap);	//expand all character references in this string
		expandParameterEntityReferences(attributeListDeclarationReader, ownerDocument, parameterEntityMap, expandedAttributeListDeclarationStringBuffer);	//expand all character references in this string into the string buffer

//G***del System.out.println("Expanded attribute declaration string: "+attributeListDeclarationString);	//G***del

			//now, make another reader to parse our new string with the parameter entities expanded
		attributeListDeclarationReader=new XMLReader(expandedAttributeListDeclarationStringBuffer.toString(), reader.getName()+":Attribute List Declaration");	//create a string reader the attribute list declaration, but we don't know its name G***Int

attributeListDeclarationReader.tidy=isTidy();  //G***fix

//G***del when works newio		attributeListDeclarationReader=new ParseReader(new StringReader(attributeListDeclarationString), "Attribute List Declaration");	//create a string reader the attribute list declaration, but we don't know its name G***Int
		attributeListDeclarationReader.setLineIndex(reader.getLineIndex());	//pretend we're reading where we already were, so any errors will show at least something close (hopefully)
		attributeListDeclarationReader.setCharIndex(reader.getCharIndex());	//pretend we're reading where we already were, so any errors will show at least something close (hopefully)
//G***del		reader.readExpectedString(ATTLIST_DECL_START);	//we expect to read the start of an element type declaration
		attributeListDeclarationReader.readExpectedChar(WHITESPACE_CHARS);	//we expect at least one whitespace character
		skipWhitespaceCharacters(attributeListDeclarationReader);	//skip any other whitespace characters
//G***del		element.setNodeName(attributeListDeclarationReader.readStringUntilChar(WHITESPACE_CHARS));	//set the name of the element, which will end with whitespace
		//G***check for a valid name here
		element.setAttribute("name", attributeListDeclarationReader.readStringUntilChar(WHITESPACE_CHARS));	//set the name attribute, specifying the name of the element, which will end with whitespace G***use a constant here
//G***del System.out.println("element name:"+element.getNodeName());	//G***del
//G***del		attributeListDeclarationReader.peekExpectedChar(WHITESPACE_CHARS+'%'+'>');	//make sure the next character is one we expect (there has to be whitespace unless it's the end of the declaration)
//G***del		reader=attributeListDeclarationReader;	//G***fix, comment

		while(true)	//read all the attribute definitions until we get to the end of the attribute list declaration
		{
			skipWhitespaceCharactersEOF(attributeListDeclarationReader);	//skip any whitespace characters, but don't throw an exception if we reach the end of the stream
			if(attributeListDeclarationReader.isEOF())	//if we run out of characters skipping whitespace, we've finished reading the attribute definitions
				break;	//we're finished reading the attribute list
			final XMLElement attributeElement=new XMLElement(ownerDocument, "attribute");	//create a new XML element which will hold the rules for this attribute G***use constant
			//G***check for a valid name here
			attributeElement.setAttribute("name", attributeListDeclarationReader.readStringUntilChar(WHITESPACE_CHARS));	//set the name attribute, specifying the name of the attribute, which will end with whitespace G***use a constant here
//G***del			attributeElement.setNodeName(attributeListDeclarationReader.readStringUntilChar(WHITESPACE_CHARS));	//set the name of the attribute, which will end with whitespace
			attributeListDeclarationReader.readExpectedChar(WHITESPACE_CHARS);	//we expect at least one whitespace character
			skipWhitespaceCharacters(attributeListDeclarationReader);	//skip any other whitespace characters
			String attributeType="";	//we'll read the type of attribute
			final int attributeTypeIndex=attributeListDeclarationReader.readExpectedStrings(ATTTYPE_STRINGS);	//see what type of attribute type this is
			switch(attributeTypeIndex)	//see what we should do with this attribute type
			{
				case CDATA:	//if this is the string data type
//G***del Debug.trace("Found attribute CDATA.");
					attributeType="string";	//use "string" as the indication of CDATA, for compatibility with schemas
					break;
				case NOTATION:	//if this is a notation
//G***del Debug.trace("Found attribute notation.");
					//G***fix attributeType=ATTTYPE_STRINGS[attributeTypeIndex];	//store this attribute type with no modification
					attributeListDeclarationReader.readStringUntilChar(')');	//G***fix
					attributeListDeclarationReader.readExpectedChar(WHITESPACE_CHARS);	//we expect at least one whitespace character
					skipWhitespaceCharacters(attributeListDeclarationReader);	//skip any other whitespace characters
							//G***this is wrong here! after a NOTATION follows not an enumeration, but something like a numeration with names instead of Nmtokens
				case ENUMERATION:	//if this is an enumeration (or a notation G***fix conflict between name and nmtoken of NOTATION and ENUMERATION types)
//G***del Debug.trace("Found attribute enumeration.");
					do
					{
						skipWhitespaceCharacters(attributeListDeclarationReader);	//skip any whitespace characters
						final String nmtokenString=attributeListDeclarationReader.readStringUntilChar(WHITESPACE_CHARS+'|'+')');	//read the nmtoken G***actually do something with this here G***use constants here
									//G***do something with the nmtoken here
//G***del Debug.trace("Found Nmtoken: "+nmtokenString);
						skipWhitespaceCharacters(attributeListDeclarationReader);	//skip any whitespace characters
					}
					while(attributeListDeclarationReader.readChar()=='|');	//keep reading more nmtokens while we find the or symbol; once we drop out of the loop, we won't need to read the end of the enumeration; we'll have already read it G***use a constant here
					break;
				default:	//if this is on the the standard tokenized types
//G***del Debug.trace("Found attribute default.");
					attributeType=ATTTYPE_STRINGS[attributeTypeIndex];	//store this attribute type with no modification
					break;
			}
//G***del Debug.trace("Finished with attribute declaration switch.");
			attributeListDeclarationReader.readExpectedChar(WHITESPACE_CHARS);	//we expect at least one whitespace character
//G***del Debug.trace("Finished reading witespace.");
			skipWhitespaceCharacters(attributeListDeclarationReader);	//skip any other whitespace characters
							//these are the default default declarations
			final int maxOccurs=1;	//currently attributes can appear at most once
			int minOccurs=0;	//default to not requiring the attribute
			String defaultString=null;	//if we find a default *or* a fixed value, we'll store it here
			boolean fixed=false;	//if we set this to true, this will indicate that the defaultString holds a fixed value
			//the attribute default declaration strings we expect
			final String[] DEFAULT_DECL_STRINGS={ATT_DEFAULT_REQUIRED, ATT_DEFAULT_IMPLIED, ATT_DEFAULT_FIXED, ""};
			//the indexes of the attribute default declaration strings in our array
			final int REQUIRED=0, IMPLIED=1, FIXED=2;
			switch(attributeListDeclarationReader.readExpectedStrings(DEFAULT_DECL_STRINGS))	//see what default declaration this is
			{
				case REQUIRED:	//if this value is required
					minOccurs=1;		//show that this attribute must appear at least once
					break;
				case IMPLIED:		//if this value is implied
					minOccurs=1;		//show that this attribute does not have to appear at all
					break;
				case FIXED:	//if this value is fixed
					fixed=true;	//show that this is a fixed value; we'll read the actual value below in the default: case
					attributeListDeclarationReader.readExpectedChar(WHITESPACE_CHARS);	//we expect at least one whitespace character
					skipWhitespaceCharacters(attributeListDeclarationReader);	//skip any other whitespace characters
				default:	//if a default value is given
					{
						final char defaultDelimiter=attributeListDeclarationReader.readExpectedChar("\"'");	//read the delimiter of the default value G***make this a constant
						defaultString=attributeListDeclarationReader.readStringUntilChar(defaultDelimiter);	//read the default value, which should end with the correct delimiter (a single or double quote, matching the first one)
						attributeListDeclarationReader.readExpectedChar(defaultDelimiter);	//read the ending delimiter (we already know what it will be) G***this can all probably go into one little routine, since we use it so much
					}
					break;
			}
			attributeElement.setAttribute("minOccurs", Integer.toString(minOccurs));	//add an attribute specifying how many times this attribute must appear G***use constants
			attributeElement.setAttribute("maxOccurs", Integer.toString(maxOccurs));	//add an attribute specifying how many times this attribute can appear G***use constants
			if(defaultString!=null)	//if we have a default string
			{
				if(fixed)	//if this is a fixed value G***should we fill both the default and fixed attributes?
					attributeElement.setAttribute("fixed", defaultString);	//add an attribute specifying the fixed string G***use constants
				else
					attributeElement.setAttribute("default", defaultString);	//add an attribute specifying the default string G***use constants
			}
			//G***check to see if this attribute has already been defined
			element.appendChild(attributeElement);	//add this attribute definition to the attribute declaration element we're constructing
		}
//G***del		if(!parseAttributeListDeclarationContent(attributeListDeclarationReader, ownerDocument, element))	//parse the attribute definitions; if we couldn't find the end of the attribute list declaration
//G***del			throw new ParseEOFException("XML Unexpected end of stream reached while searching for ending of attribute list declaration \""+element.getNodeName()+"\", which began at G***fix.", reader.getLineIndex(), reader.getCharIndex(), reader.getName());	//show that we hit the end of the file while searching for the end of the internal DTD G***Int
//G***del		reader.readExpectedString(ELEMENT_TYPE_DECL_END);	//read the end-of-declaration marker which we know should be here
		return element;	//return the element we constructed
	}


	/**Parses an input stream that contains attribute definitions of an attribute list declaration.
	If this function reaches the end of the stream of the reader without finding
	an ending character for the attribute list declaration, an exception is not
	thrown; rather, the return code will indicate if the end was found.
	@param reader The reader from which to retrieve characters.
	@param ownerDocument The document which will own the data.
	@param entity The element holding the contents of the attribute list declaration.
	This should be a single or double quote character.
	@exception IOException Thrown when an i/o error occurs.
	@exception XMLSyntaxException Thrown when there is a syntax error in the XML file.
	@exception XMLWellFormednessException Thrown when there is a well-formedness error in the XML file.
	@exception XMLUndefinedEntityReferenceException Thrown if a named entity reference has not been defined.
	@exception ParseUnexpectedDataException Thrown when an unexpected character is found.
	@exception ParseEOFException Thrown when the end of the input stream is reached unexpectedly.
	@return <code>true</code> if the ending character for this entity was found, else <code>false</code>.
	*/
/*G***del
	protected boolean parseAttributeListDeclarationContent(final ParseReader reader, final XMLDocument ownerDocument, final XMLElement element) throws IOException, XMLSyntaxException, XMLWellFormednessException, XMLUndefinedEntityReferenceException, ParseUnexpectedDataException, ParseEOFException
	{
		while(true)	//read all the attribute definitions until we get to the end of the attribute list declaration
		{
			skipWhitespaceCharactersEOF(reader);	//skip any whitespace characters, but don't throw an exception if we reach the end of the stream
			if(reader.isEOF())	//if we run out of characters looking for the start of the next document type markup, this may not be an error; we may be processing an external DTD
				return false;	//return, showing that we have not yet found the ending character marker of an internal DTD
//G***bring back			switch(reader.peekChar())	//see what the next character is
			char c=reader.peekChar();
//G***del System.out.println("Peeking char: "+c);	//G***del
			switch(c)	//see what the next character is
			{
				case '>':	//if we find the ending delimiter of the declaration G***use a constant here

//G***del System.out.println("Found end of attribute list.");	//G***del

					return true;	//show that we found the end of the declaration
				case '%':	//if we find the start of a parameter entity G***use a constant here
					final XMLEntity entity=parseParameterEntityReference(reader, ownerDocument);	//parse this entity reference and see which entity it refers to
					final ParseReader entityReader=createEntityReader(entity);	//we'll use this to read either from the internal value or an external file G***check for recursive nesting here
					if(parseAttributeListDeclarationContent(entityReader, ownerDocument, element))	//parse the attribute definition just as if it were included in the file (except for ending across entity boundaries); if the ending markup for the attribute list declaration was found G***fix; how can they find the ']' in a parameter entity?
						return true;	//show that we found the end of the declaration G***this is incorrect; if they return true, we need to throw a cross-boundaries parameter entity error
					break;
				default:	//any other character means we expect more attribute definitions
					{
//G***del System.out.println("Ready to read attribute definition.");	//G***del
						final XMLElement attributeElement=new XMLElement(ownerDocument, "attribute");	//create a new XML element which will hold the rules for this attribute G***use constant
						attributeElement.setNodeName(reader.readStringUntilChar(WHITESPACE_CHARS));	//set the name of the attribute, which will end with whitespace
//G***del System.out.println("Attribute name: "+attributeElement.getNodeName());	//G***del
						reader.readExpectedChar(WHITESPACE_CHARS);	//we expect at least one whitespace character
						skipWhitespaceCharacters(reader);	//skip any other whitespace characters
						String attributeType="";	//we'll read the type of attribute
						//the attribute type strings we expect
						final String[] ATTTYPE_STRINGS={ATTTYPE_CDATA, ATTTYPE_ID, ATTTYPE_IDREF, ATTTYPE_IDREFS, ATTTYPE_ENTITY, ATTTYPE_ENTITIES, ATTTYPE_NMTOKEN, ATTTYPE_NMTOKENS, ATTTYPE_NOTATION, ATTTYPE_ENUMERATION_START};
						//the indexes of the attribute type strings in our array
						final int CDATA=0, ID=1, IDREF=2, IDREFS=3, ENTITY=4, ENTITIES=5, NMTOKEN=6, NMTOKENS=7, NOTATION=8, ENUMERATION=9;
						final int attributeTypeIndex=reader.readExpectedStrings(ATTTYPE_STRINGS);	//see what type of attribute type this is
						switch(attributeTypeIndex)	//see what we should do with this attribute type
						{
							case CDATA:	//if this is the string data type
								attributeType="string";	//use "string" as the indication of CDATA, for compatibility with schemas
								break;
							case NOTATION:	//if this is a notation
								//G***fix attributeType=ATTTYPE_STRINGS[attributeTypeIndex];	//store this attribute type with no modification
								reader.readStringUntilChar(')');	//G***fix
								reader.readExpectedChar(WHITESPACE_CHARS);	//we expect at least one whitespace character
								skipWhitespaceCharacters(reader);	//skip any other whitespace characters
							case ENUMERATION:	//if this is an enumeration (or a notation G***fix conflict between name and nmtoken of NOTATION and ENUMERATION types)
//G***del System.out.println("Enumerated type.");	//G***del
//G***del								reader.readExpectedString(ATTTYPE_ENUMERATION_START);	//read the start of the enumeration
								do
								{
									skipWhitespaceCharacters(reader);	//skip any whitespace characters
									final String nmtokenString=reader.readStringUntilChar(WHITESPACE_CHARS+'|'+')');	//read the nmtoken G***actually do something with this here G***use constants here

//G***del System.out.println("Just read nmtoken: "+nmtokenString);	//G***del

									//G***do something with the nmtoken here
									skipWhitespaceCharacters(reader);	//skip any whitespace characters
								}
								while(reader.readChar()=='|');	//keep reading more nmtokens while we find the or symbol; once we drop out of the loop, we won't need to read the end of the enumeration; we'll have already read it G***use a constant here
//G***del								reader.readExpectedString(ATTTYPE_ENUMERATION_END);	//read the end of the enumeration
								break;
							default:	//if this is on the the standard tokenized types
								attributeType=ATTTYPE_STRINGS[attributeTypeIndex];	//store this attribute type with no modification
								break;
						}
						reader.readExpectedChar(WHITESPACE_CHARS);	//we expect at least one whitespace character
//G***del System.out.println("Read whitespace char.");	//G***del

						skipWhitespaceCharacters(reader);	//skip any other whitespace characters
							//these are the default default declarations
						final int maxOccurs=1;	//currently attributes can appear at most once
						int minOccurs=0;	//default to not requiring the attribute
						String defaultString=null;	//if we find a default *or* a fixed value, we'll store it here
						boolean fixed=false;	//if we set this to true, this will indicate that the defaultString holds a fixed value
						//the attribute default declaration strings we expect
						final String[] DEFAULT_DECL_STRINGS={ATT_DEFAULT_REQUIRED, ATT_DEFAULT_IMPLIED, ATT_DEFAULT_FIXED, ""};
						//the indexes of the attribute default declaration strings in our array
						final int REQUIRED=0, IMPLIED=1, FIXED=2;
						switch(reader.readExpectedStrings(DEFAULT_DECL_STRINGS))	//see what default declaration this is
						{
							case REQUIRED:	//if this value is required
								minOccurs=1;		//show that this attribute must appear at least once
								break;
							case IMPLIED:		//if this value is implied
								minOccurs=1;		//show that this attribute does not have to appear at all
								break;
							case FIXED:	//if this value is fixed
								fixed=true;	//show that this is a fixed value; we'll read the actual value below in the default: case
								reader.readExpectedChar(WHITESPACE_CHARS);	//we expect at least one whitespace character
								skipWhitespaceCharacters(reader);	//skip any other whitespace characters
							default:	//if a default value is given
								{
//G***del System.out.println("Ready to read default value.");	//G***del

									final char defaultDelimiter=reader.readExpectedChar("\"'");	//read the delimiter of the default value G***make this a constant
									defaultString=reader.readStringUntilChar(defaultDelimiter);	//read the default value, which should end with the correct delimiter (a single or double quote, matching the first one)
									reader.readExpectedChar(defaultDelimiter);	//read the ending delimiter (we already know what it will be) G***this can all probably go into one little routine, since we use it so much

//G***del System.out.println("Default string: "+defaultString);	//G***del
								}
								break;
						}
						attributeElement.setAttribute("minOccurs", Integer.toString(minOccurs));	//add an attribute specifying how many times this attribute must appear G***use constants
						attributeElement.setAttribute("maxOccurs", Integer.toString(maxOccurs));	//add an attribute specifying how many times this attribute can appear G***use constants
						if(defaultString!=null)	//if we have a default string
						{
							if(fixed)	//if this is a fixed value G***should we fill both the default and fixed attributes?
								attributeElement.setAttribute("fixed", defaultString);	//add an attribute specifying the fixed string G***use constants
							else
								attributeElement.setAttribute("default", defaultString);	//add an attribute specifying the default string G***use constants
						}
						//G***check to see if this attribute has already been defined
						element.appendChild(attributeElement);	//add this attribute definition to the attribute declaration element we're constructing
						reader.peekExpectedChar(WHITESPACE_CHARS+'>');	//make sure the next character is one we expect (there has to be whitespace unless it's the end of the declaration)
					}
					break;
			}
		}
	}
*/

	/**Parses a stream and expands any parameter references found within. The resulting
	string will be appended to collectedCharacters and returned. This function will
	continue collecting characters until the end of the stream is reached. No exception
	will be thrown at the end of the stream.
	@param reader The reader from which to retrieve characters.
	@param ownerDocument The document which will own the data.
	@param parameterEntityMap The parameter entities we've collected so far
	@param expandedText The string buffer which will collect the expanded information read from the reader.
	@exception IOException Thrown when an i/o error occurs.
	@exception XMLSyntaxException Thrown when there is a syntax error in the XML file.
	@exception XMLWellFormednessException Thrown when there is a well-formedness error in the XML file.
	@exception XMLUndefinedEntityReferenceException Thrown if a named entity reference has not been defined.
	@exception ParseUnexpectedDataException Thrown when an unexpected character is found.
	@exception ParseEOFException Thrown when the end of the input stream is reached unexpectedly.
	@return The characters until the end of the string with any parameter entity
	references expanded and padded with one beginning and one end space, as the XML
	specification requires.
	*/
	protected void expandParameterEntityReferences(final XMLReader reader, final XMLDocument ownerDocument, final XMLNamedNodeMap parameterEntityMap, final StringBuffer expandedText) throws IOException, XMLSyntaxException, XMLWellFormednessException, XMLUndefinedEntityReferenceException, ParseUnexpectedDataException, ParseEOFException
	{
//G***del when works		String collectedCharacters="";	//this will hold all the characters we find
		while(true)	//keep looking at charactes from the reader until we reach the end of file (which will kick us out from inside the loop)
		{
//G***del System.out.println("ready to read string until EOF");	//G***del

			expandedText.append(reader.readStringUntilCharEOF('%'));	//read character content until we find an entity reference or the end of the stream G***use a constant here
//G***del System.out.println("After reading string until EOF: "+collectedCharacters);	//G***del
//G***del System.out.println("OEF: "+reader.isEOF());	//G***del
			if(reader.isEOF())	//if we're reached the end of the file
			{
//G***del System.out.println("Ready to return collected characters: "+collectedCharacters);	//G***del
				return; //return, sending back all the characters we've collected
//G***del when works				return collectedCharacters;	//return the characters we've collected
			}
			else	//if we've found an entity reference

//G***del			if(reader.peekChar()=='%')	//if we've found an entity reference
			{
				final XMLEntity entity=parseParameterEntityReference(reader, ownerDocument, parameterEntityMap);	//parse this entity reference and see which entity it refers to
				final XMLReader entityReader=createEntityReader(reader.getSourceObject(), entity);	//we'll use this to read either from the internal value or an external file
				//G***why aren't we using a string buffer here?
				expandedText.append(' '); //append a space before the value
					//continue collecting data by expanding this parameter entity reference
				expandParameterEntityReferences(entityReader, ownerDocument, parameterEntityMap, expandedText);
				expandedText.append(' '); //append a space before the value
//G***del when works				collectedCharacters+=' '+expandParameterEntityReferences(entityReader, ownerDocument, parameterEntityMap)+' ';	//expand the entity references in the replacement text and add those characters to the ones we've collected; put a space at the beginning and end, as the XML specification requires
			}
//G***del			else	//if this isn't an entity reference, we must be at the end of the file
		}
	}


//G***del	protected XMLEntity parseParameterEntityReference(final ParseReader reader, final XMLDocument ownerDocument) throws IOException, XMLSyntaxException, ParseUnexpectedDataException, ParseEOFException, XMLUndefinedEntityReferenceException



	/**Parses a string and expands any character and/or entity references.
	@param characterContent The string of characters to parse.
	@exception XMLUndefinedEntityReferenceException Thrown if a named entity reference has not been defined.
	@return The string with expanded character and entity references.
	*/
/*G***del if we don't need
	protected String expandReferences(final String characterContent) throws XMLUndefinedEntityReferenceException
	{
		String parsedContent="";	//create a string to return after we've parsed the content
//G***del    boolean foundReference;	//this will indicate whether or not we've found a character or entity reference
		int searchBeginIndex=0;	//we'll start searching at the beginning of the string
		int referenceBeginIndex, referenceEndIndex;	//this will receive the beginning and ending of any reference we find
		do
		{
			referenceBeginIndex=characterContent.indexOf('&', searchBeginIndex);	//find the beginning of a reference in the string, starting from where we left off last time G***make this a constant
			if(referenceBeginIndex!=-1)	//if we found the beginning of a reference
			{
				referenceEndIndex=characterContent.indexOf(';', referenceBeginIndex+1);	//find the end of the reference (which will, of course, be after its beginning) G***make this a constant

//G***del System.out.println("Reference begin: "+referenceBeginIndex+" Reference end: "+referenceEndIndex+"\n");	//G***del

				//G***check to make sure we find the end of the reference
				final String referenceName=characterContent.substring(referenceBeginIndex+1, referenceEndIndex);	//get the name of the reference
					//G***make sure this reference name has at least one character
				if(referenceName.charAt(0)=='#')	//if this is a character entity reference G***make this a constant
				{
					//G***make sure this is a valid number
					parsedContent+=characterContent.substring(searchBeginIndex, referenceBeginIndex)+(char)Integer.parseInt(referenceName.substring(1), 10);	//ignoring the beginning character, change the decimal string to a number and add it to the normal character content up to this point
				}
				else	//if this isn't a character reference, we'll assume it's an entity reference
				{
					if(getEntityReferenceMap().containsKey(referenceName))	//if our map of entity references has this entity name
					{
						final String replacementText=expandReferences((String)getEntityReferenceMap().get(referenceName));	//recursively look up the replacement text G***really make this recursive
						parsedContent+=characterContent.substring(searchBeginIndex, referenceBeginIndex)+replacementText;	//add everything from where we began searching up to the entity reference, and then add the replacement text for the
						searchBeginIndex=referenceEndIndex+1;	//next time, start looking for another reference *after* this reference
					}
					else	//if we don't recognize this entity reference name
						throw new XMLUndefinedEntityReferenceException(referenceName);	//show that we don't recognize the entity reference
				}
				searchBeginIndex=referenceEndIndex+1;	//next time, start looking for another reference *after* this reference

//G***del System.out.println("Reference: "+referenceName+"\n");	//G***del
			}
		}
		while(referenceBeginIndex!=-1);	//keep looking until there are no more references left
		//G***add the rest of the content we didn't get to
//G***del    parsedContent=characterContent;	//G***fix
		return parsedContent;	//return our parsed content
	}
*/

	/**Parses an input stream that supposedly begins with XML markup.
	@param reader The reader from which to retrieve characters.
	@param ownerDocument The document which will own the nodes encountered.
	@exception IOException Thrown when an i/o error occurs.
	@exception XMLSyntaxException Thrown when there is a syntax error in the XML file.
	@exception XMLWellFormednessException Thrown when there is a well-formedness error in the XML file.
	@exception XMLValidityException Thrown when there is a validity error in the XML file.
	@exception XMLUndefinedEntityReferenceException Thrown if a named entity reference has not been defined.
	@exception ParseUnexpectedDataException Thrown when an unexpected character is found.
//G***del	@exception ParseEOFException Thrown when the end of the input stream is reached unexpectedly.
	@return A new XML object constructed from the markup.
	*/
	protected XMLNode parseMarkup(final XMLReader reader, final XMLDocument ownerDocument) throws IOException, XMLSyntaxException, XMLWellFormednessException, XMLValidityException, XMLUndefinedEntityReferenceException, ParseUnexpectedDataException//G***del, ParseEOFException
	{
//G***del Debug.trace("XMLParse Starting to parse markup.");	//G***del
		try
		{
			reader.resetPeek();	//reset peeking so that the next character peeked will reflect the next character to be read
			//An XML declarations get special processing because 1) its name conflicts
			//with the beginning of a processing instruction, making it difficult for
			//the loop below without whitespace checking, and 2) XML declarations are
			//not normal markup in the first place, and may get processed differently
			//in the code in the future, anyway.
				//if there is an XML declaration ("<?xml") followed by whitespace (don't throw an error on the end of file, in case we're parsing a short ending tag)
			if(reader.peekStringEOF(XML_DECL_START.length()).equals(XML_DECL_START) && isWhitespace(reader.peekChar())) //G***an XML declaration without whitespace following could throw an exception because of the EOF, but that would be illegal anyway
				return parseXMLDeclaration(reader, ownerDocument);	//parse the XML declaration and return it
			else	//if this is any other markup
				reader.resetPeek();	//reset peeking so that the next character peeked will reflect the next character to be read
			//the markup strings we expect; make sure we put the TAG_START last, because all the markup matches TAG_START
			final String[] EXPECTED_MARKUP_STRINGS={COMMENT_START, CDATA_START, DOCTYPE_DECL_START, /*G***del when works XML_DECL_START, */PROCESSING_INSTRUCTION_START, TAG_START};
			//the indexes of the markup strings in our array
			final int COMMENT=0, CDATA_SECTION=1, DOCTYPE_DECL=2, /*G***del when works XML_DECL=3, */PROCESSING_INSTRUCTION=3, TAG=4;
			reader.resetPeek();	//reset peeking so that the next character peeked will reflect the next character to be read
//G***del Debug.trace("XMLParse parseMarkup() ready to peek expected strings.");	//G***del
			switch(reader.peekExpectedStrings(EXPECTED_MARKUP_STRINGS))	//see what kind of markup this is
			{
				case COMMENT:	//if this is a comment
//G***del Debug.trace("XMLParse parseMarkup() COMMENT.");	//G***del
					return parseComment(reader, ownerDocument);	//parse the comment and return it
				case CDATA_SECTION:	//if this is a CDATA section
//G***del Debug.trace("XMLParse parseMarkup() CDATA.");	//G***del
					return parseCDATASection(reader, ownerDocument);	//parse the CDATA and return it
				case DOCTYPE_DECL:	//if this is a documement type declaration
//G***del Debug.trace("XMLParse parseMarkup() DOCTYPE_DECL.");	//G***del
					return parseDocumentType(reader, ownerDocument);	//parse the document type declaration and return it
/*G***del when works; moved
				case XML_DECL:	//if this is an XML declaration
Debug.trace("XMLParse parseMarkup() XML_DECL.");	//G***del
					return parseXMLDeclaration(reader, ownerDocument);	//parse the XML declaration and return it
*/
				case PROCESSING_INSTRUCTION:	//if this is a processing instruction
//G***del Debug.trace("XMLParse parseMarkup() XML_DECL.");	//G***del
					return parseProcessingInstruction(reader, ownerDocument);	//parse the processing instruction and return it
				case TAG:	//if this is a tag
//G***del Debug.trace("XMLParse parseMarkup() TAG.");	//G***del
				  if(isTidy())  //if we should tidy, make sure this isn't a misspelled document type or something else besides a tag
				  {
//G***del Debug.trace("Checking for tidy tag markup.");
		  			reader.resetPeek();	//reset peeking so that the next character peeked will reflect the next character to be read
							//see if they put the document type declaration in lowercase; don't throw an exception for the end of the file, since parsing the tag will do that if needed G***what about a case insensitive compare?
		  			if(reader.peekStringEOF(DOCTYPE_DECL_START.length()).equals(DOCTYPE_DECL_START.toLowerCase()) && isWhitespace(reader.peekChar()))
							return parseDocumentType(reader, ownerDocument);	//parse the document type declaration and return it
		  			else	//if this is any other markup
		  				reader.resetPeek();	//reset peeking so that the next character peeked will reflect the next character to be read
				  }
					return parseTag(reader, ownerDocument);	//parse the tag and return it
				default:	//if the markup isn't any of the above (this should never happen unless the code is incorrect)
//G***del Debug.trace("XMLParse parseMarkup() default.");	//G***del
					throw new XMLSyntaxException("Unrecognized markup.", reader.getLineIndex(), reader.getCharIndex(), reader.getName());	//show that we were expecting markup data G***this shouldn't be an XMLSyntaxException
			}
		}
		catch(final ParseEOFException parseEOFException)	//if we run out of data
		{
			throw new XMLWellFormednessException(parseEOFException, ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME).getString(RESOURCE_PREFIX+MARKUP_RESOURCE), ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME).getString(RESOURCE_PREFIX+UNKNOWN_RESOURCE));	//show that we couldn't find markup
		}
	}

	/**Parses an input stream that supposedly begins with a tag.
	@param reader The reader from which to retrieve characters.
	@param ownerDocument The document which will own the tag.
	@exception IOException Thrown when an i/o error occurs.
	@exception XMLSyntaxException Thrown when there is a syntax error in the XML file.
	@exception XMLWellFormednessException Thrown when there is a well-formedness error in the XML file.
	@exception ParseUnexpectedDataException Thrown when an unexpected character is found.
//G***del	@exception ParseEOFException Thrown when the end of the input stream is reached unexpectedly.
	@exception XMLUndefinedEntityReferenceException Thrown if a named entity reference has not been defined.
	@return A new XML tag constructed from the reader.
	*/
	protected XMLTag parseTag(final XMLReader reader, final XMLDocument ownerDocument) throws IOException, XMLSyntaxException, XMLWellFormednessException, ParseUnexpectedDataException, /*G***del ParseEOFException, */XMLUndefinedEntityReferenceException
	{
//G***del Debug.trace("Starting to parse a tag.");	//G***del
//G***del		char c;	//we'll use this to read characters
		final XMLTag tag=new XMLTag(ownerDocument);	//create a new XML tag
		try
		{
//G***del			boolean isTidyingTag=false; //we'll set this to true if we're going to ignore a tag with an invalid name
			reader.readExpectedChar('<');	//since we're here, we expect the start of a tag G***use a constant here
			if(reader.peekChar()=='/')	//if the next character to be read is a '/', this is an ending tag G***use a constant here
			{
//G***del Debug.trace("Must be an ending tag.");	//G***del
				reader.readExpectedChar('/');	//read the next character G***use a constant here
				tag.setTagType(XMLTag.END_TAG);	//show that it's an end tag
					//G***have the tag check to make sure this is not an empty name
	//G***del System.out.println("Getting ready to read string until expected character.");	//G***del
				final String tagName=reader.readStringUntilChar(WHITESPACE_CHARS+">");	//read the name of the tag, which will end with whitespace or the end-of-tag marker G*** use a constant here

//G***del Debug.trace("ready to check name: ", tagName);  //G***del

				if(!isName(tagName) && !isTidy())	//if this isn't a valid name, and we're not tidying
					throw new XMLWellFormednessException(XMLWellFormednessException.INVALID_NAME, new Object[]{tagName}, reader.getLineIndex(), reader.getCharIndex(), reader.getName());	//show that this isn't a valid XML name
				tag.setNodeName(tagName);	//set the name of the tag
//G***del Debug.trace("Read tag name: "+tagName); //G***del
				skipWhitespaceCharacters(reader);	//skip all whitespace characters (we already know the next character is valid)
				reader.readExpectedChar('>');	//the character after this needs to be the end of the tag G***use a constant here
				return tag;	//return the tag we constructed (an ending tag has no attributes
			}
				//G***have the tag check to make sure this is not an empty name
			final String tagName=reader.readStringUntilChar(WHITESPACE_CHARS+"/>");	//read the name of the tag, which will end with whitespace or the end-of-tag marker G*** use a constant here
//G***del Debug.trace("ready to check name: ", tagName);  //G***del
			if(!isName(tagName))  //if this isn't a valid name
			{
				if(isTidy())  //if we're tidying, we'll just ignore everything in this tag with the invalid name
					reader.readStringUntilChar("/>");	//read and discard everything up to the end of the end-of-tag marker G*** use a constant here
				else  //if we're not tidying
					throw new XMLWellFormednessException(XMLWellFormednessException.INVALID_NAME, new Object[]{tagName}, reader.getLineIndex(), reader.getCharIndex(), reader.getName());	//show that this isn't a valid XML name
			}
			tag.setNodeName(tagName);	//set the name of the tag
//G***del Debug.trace("Created tag with name: ", tagName);  //G***del
			while(true)	//read all the attribute/value pairs until we get to the end of the tag
			{
//G***del Debug.trace("XMLParse looping inside parseTag()");	//G***del
				reader.resetPeek(); //make sure the peek pointer is up-to-date before trying to look at the next character
				reader.peekExpectedChar(WHITESPACE_CHARS+'/'+'>');	//make sure the next character is one we expect (there has to be whitespace unless it's the end of the tag) G***use constants here
				skipWhitespaceCharacters(reader);	//skip any whitespace characters
				switch(reader.peekChar())	//see what the next character is
				{
					case '/':	//if we find the ending tag marker, this is an empty element tag since we know it's not an end tag G***use a constant here
						reader.readExpectedChar('/');	//read the ending tag marker which we know should be here G***use a constant here
						tag.setTagType(XMLTag.EMPTY_ELEMENT_TAG);	//show that it's an empty element tag
						reader.readExpectedChar('>');	//the next character should be the ending delimiter of the tag G***combine this with the readExpectedCharacter() above with a constant string
//G***del Debug.trace("XMLParse parseTag() finished with empty element tag: "+tag.getNodeName());	//G***del
						return tag;	//return the tag we constructed
					case '>':	//if we find the ending delimiter of the tag G***use a constant here
//G***del Debug.trace("Found the end of a tag.");	//G***del
						reader.readExpectedChar('>');	//read the end-of-tag marker which we know should be here G***use a constant here
						tag.setTagType(XMLTag.START_TAG);	//show that it's a start tag (we know it's not an end tag, and we didn't find a '/' character)
	//G**del System.out.println("XMLParse parseTag() finished with start tag: "+tag.getNodeName());	//G***del
						return tag;	//return the tag we constructed
					default:	//any other character means we have more attribute/value pairs
//G***del Debug.trace("XMLParse parseTag() ready to parse another attribute/value pair for: "+tag.getNodeName());	//G***del
						final XMLAttribute attribute=parseAttribute(reader, ownerDocument);	//read an attribute-value pair
//G***del Debug.trace("Parsed attribute: "+attribute.getName()+"="+attribute.getValue()); //G***del
							//G***store the tag's location within the file, and return that location in the error message
						if(tag.getAttributeXMLNamedNodeMap().containsNamedItem(attribute))	//if this attribute has already been defined
							throw new XMLWellFormednessException(XMLWellFormednessException.UNIQUE_ATT_SPEC, new Object[]{tag.getNodeName(), attribute.getNodeName()}, reader.getLineIndex(), reader.getCharIndex(), reader.getName());	//show that this attribute has already been defined
							//G***make sure the attribute name is valid, here; this is different than checking the name inside the XMLAttribute object
						tag.getAttributeXMLNamedNodeMap().setNamedItem(attribute);	//add this attribute to the tag's map of attributes
						break;
				}
			}
		}
		catch(final ParseEOFException parseEOFException)	//if we run out of data
		{
				//show that we couldn't find the end of the tag
			throw (XMLWellFormednessException)new XMLWellFormednessException(parseEOFException, ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME).getString(RESOURCE_PREFIX+TAG_RESOURCE), tag.getNodeName()!=null ? tag.getNodeName() : ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME).getString(RESOURCE_PREFIX+UNKNOWN_RESOURCE)).initCause(parseEOFException);	
		}
	}

	/**Parses an input stream that supposedly begins with an XML declaration ("<?xml").
	@param reader The reader from which to retrieve characters.
	@param ownerDocument The document which will own the XML declaration.
	@exception IOException Thrown when an i/o error occurs.
	@exception XMLSyntaxException Thrown when there is a syntax error in the XML file.
	@exception XMLWellFormednessException Thrown when there is a well-formedness error in the XML file.
	@exception ParseUnexpectedDataException Thrown when an unexpected character is found.
//G***del	@exception ParseEOFException Thrown when the end of the input stream is reached unexpectedly.
	@exception XMLUndefinedEntityReferenceException Thrown if a named entity reference has not been defined.
	@return A new XML declaration constructed from the reader.
	*/
	protected XMLDeclaration parseXMLDeclaration(final XMLReader reader, final XMLDocument ownerDocument) throws IOException, XMLSyntaxException, XMLWellFormednessException, ParseUnexpectedDataException, /*G***del ParseEOFException, */XMLUndefinedEntityReferenceException
	{
//G***del System.out.println("XMLParse Starting to parse XML declaration.");	//G***del
		XMLDeclaration xmlDecl=new XMLDeclaration(ownerDocument);	//create a new XML declaration object
		try
		{
//G***del System.out.println("Parsing XML declaration.\n");	//G***del
			reader.readExpectedString(XML_DECL_START);	//we expect to read the start of an XML declaration
			boolean foundVersionInfo=false, foundEncodingDecl=false, foundSDDecl=false;	//whether or not we've found each of the attributes
			while(true)	//read all the attribute/value pairs until we get to the end of the tag
			{
				reader.peekExpectedChar(WHITESPACE_CHARS+'?');	//make sure the next character is one we expect (there has to be whitespace unless it's the end of the XML declaration)
				skipWhitespaceCharacters(reader);	//skip any whitespace characters
				switch(reader.peekChar())	//see what the next character is
				{
					case '?':	//if we find the start of the end of the XML declaration G***use a constant here
	//G***del System.out.println("XMLParse found question mark.");	//G***del
						reader.readExpectedString(XML_DECL_END);	//we expect to read the end of an XML declaration
						return xmlDecl;	//return the XML declaration we constructed
					default:	//any other character means we have more attribute/value pairs
	//G**del System.out.println("XMLParse Ready for another XML declaration att/val pair.");	//G***del

						final XMLAttribute attribute=parseAttribute(reader, ownerDocument);	//read an attribute-value pair
						if(attribute.getNodeName().equals(VERSIONINFO_NAME))	//if this is the version info ("version")
						{
							if(foundVersionInfo)	//if we've already found the version information
								throw new XMLSyntaxException("\""+attribute.getNodeName()+"\" already defined.", attribute.getLineIndex(), attribute.getCharIndex(), reader.getName());	//show that this attribute has already been defined G***this shouln't be a syntax exception
							//G***check the format of the version number here
							foundVersionInfo=true;	//show that we've found the version info
							xmlDecl.setVersion(attribute.getNodeValue());	//set the version
						}
						else if(attribute.getNodeName().equals(ENCODINGDECL_NAME))	//if this is the encoding declaration ("encoding")
						{
							if(!foundVersionInfo)	//if we haven't yet found the version information
								throw new XMLSyntaxException("Missing \""+VERSIONINFO_NAME+"\" declaration.", attribute.getLineIndex(), attribute.getCharIndex(), reader.getName());	//show that the version info is missing G***this shouldn't be a syntax exception
							if(foundEncodingDecl)	//if we've already found the encoding declaration
								throw new XMLSyntaxException("\""+attribute.getNodeName()+"\" already defined.", attribute.getLineIndex(), attribute.getCharIndex(), reader.getName());	//show that this attribute has already been defined G***this shouln't be a syntax exception
							if(foundSDDecl)	//if we've already found the standalone declaration
								throw new XMLSyntaxException("\""+attribute.getNodeName()+"\" out of order.", attribute.getLineIndex(), attribute.getCharIndex(), reader.getName());	//show that this attribute is out of order G***this shouln't be a syntax exception
							//G***check the format of the version number here
							foundEncodingDecl=true;	//show that we've found the encoding declaration
							xmlDecl.setEncoding(attribute.getNodeValue());	//set the encoding
						}
						else if(attribute.getNodeName().equals(SDDECL_NAME))	//if this is the encoding declaration ("encoding")
						{
							if(!foundVersionInfo)	//if we haven't yet found the version information
								throw new XMLSyntaxException("Missing \""+VERSIONINFO_NAME+"\" declaration.", attribute.getLineIndex(), attribute.getCharIndex(), reader.getName());	//show that this attribute has already been defined G***this shouln't be a syntax exception
							if(foundSDDecl)	//if we've already found the standalone declaration
								throw new XMLSyntaxException("\""+attribute.getNodeName()+"\" already defined.", attribute.getLineIndex(), attribute.getCharIndex(), reader.getName());	//show that this attribute has already been defined G***this shouln't be a syntax exception
							//G***check the format of the value here
							foundSDDecl=true;	//show that we've found the standalone declaration
	//G***fix						xmlDecl.setEncoding(attribute.getNodeValue());	//set the encoding
						}
						else
							throw new XMLSyntaxException("Invalid XML declaration attribute: \""+VERSIONINFO_NAME+"\".", attribute.getLineIndex(), attribute.getCharIndex(), reader.getName());	//show that we don't recognize this attribute G***this shouldn't be a syntax exception
						break;
				}
			}
		}
		catch(final ParseEOFException parseEOFException)	//if we run out of data
		{
			throw new XMLWellFormednessException(parseEOFException, ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME).getString(RESOURCE_PREFIX+XML_DECLARATION_RESOURCE), xmlDecl.getNodeName()!=null ? xmlDecl.getNodeName() : ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME).getString(RESOURCE_PREFIX+UNKNOWN_RESOURCE));	//show that we couldn't find the end of the XML declaration
		}
	}

	/**Parses an input stream that supposedly begins with an XML processing instruction.
	@param reader The reader from which to retrieve characters.
	@param ownerDocument The document which will own the XML declaration.
	@exception IOException Thrown when an i/o error occurs.
//G***del when we can	@exception XMLSyntaxException Thrown when there is a syntax error in the XML file.
	@exception XMLWellFormednessException Thrown when there is a well-formedness error in the XML file.
	@exception ParseUnexpectedDataException Thrown when an unexpected character is found.
	@return A new XML processing instruction constructed from the reader.
	*/
	protected XMLProcessingInstruction parseProcessingInstruction(final XMLReader reader, final XMLDocument ownerDocument) throws IOException, XMLSyntaxException, XMLWellFormednessException, ParseUnexpectedDataException /*G***del ParseEOFException, */
	{
//G***del Debug.trace("XMLParse Starting to parse processing instruction.");	//G***del
		XMLProcessingInstruction processingInstruction=new XMLProcessingInstruction(ownerDocument);	//create a new processing instruction
		try
		{
			reader.readExpectedString(PROCESSING_INSTRUCTION_START);	//we expect to read the start of an XML declaration
			final String piTarget=reader.readStringUntilChar(WHITESPACE_CHARS);	//read the name of the processing instruction, which will end with whitespace
//G***del Debug.trace("Read processing instruction, piTarget: "+piTarget);
			if(!isPITarget(piTarget))	//if this isn't a valid processing instruction target
				throw new XMLWellFormednessException(XMLWellFormednessException.INVALID_PI_TARGET, new Object[]{piTarget}, reader.getLineIndex(), reader.getCharIndex(), reader.getName());	//show that this isn't a valid XML name
			processingInstruction.setNodeName(piTarget);	//set the name (target) of the processing instruction
			skipWhitespaceCharacters(reader);	//skip any whitespace characters (we require at least one, but we know there's one here because we found one when reading the name, above)
			processingInstruction.setData(reader.readDelimitedString(PROCESSING_INSTRUCTION_END));	//read all the characters in the processing instruction up to the PROCESSING_INSTRUCTION_END delimiter
			return processingInstruction;	//return the processing instruction we constructed
		}
		catch(final ParseEOFException parseEOFException)	//if we run out of data
		{
			throw new XMLWellFormednessException(parseEOFException, ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME).getString(RESOURCE_PREFIX+PROCESSING_INSTRUCTION_RESOURCE), processingInstruction.getTarget()!=null ? processingInstruction.getNodeName() : ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME).getString(RESOURCE_PREFIX+UNKNOWN_RESOURCE));	//show that we couldn't find the end of the XML processing instruction
		}
	}

	/**Parses an input stream that supposedly begins with a CDATA section.
	@param reader The reader from which to retrieve characters.
	@param ownerDocument The document which will own this CDATA node.
	@exception IOException Thrown when an i/o error occurs.
	@exception XMLSyntaxException Thrown when there is a syntax error in the XML file.
	@exception XMLWellFormednessException Thrown when there is a well-formedness error in the XML file.
	@exception ParseUnexpectedDataException Thrown when an unexpected character is found.
//G***del	@exception ParseEOFException Thrown when the end of the input stream is reached unexpectedly.
	@return A new CDATA constructed from the reader.
	*/
	protected XMLCDATASection parseCDATASection(final XMLReader reader, final XMLDocument ownerDocument) throws IOException, XMLSyntaxException, XMLWellFormednessException, ParseUnexpectedDataException//G***del, ParseEOFException
	{
		try
		{
			String characterData=reader.readDelimitedString(CDATA_START, CDATA_END);	//read all the characters in the CDATA section between the CDATA_START and CDATA_END delimiters
			XMLCDATASection CDATA=new XMLCDATASection(ownerDocument, characterData);	//create a new CDATA object
			return CDATA;	//return the CDATA we constructed
		}
		catch(final ParseEOFException parseEOFException)	//if we run out of data
		{
			throw new XMLWellFormednessException(parseEOFException, ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME).getString(RESOURCE_PREFIX+CDATA_RESOURCE), ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME).getString(RESOURCE_PREFIX+UNKNOWN_RESOURCE));	//show that we couldn't find the end of the CDATA
		}
	}

	/**Parses an input stream that supposedly begins with a comment.
	@param reader The reader from which to retrieve characters.
	@param ownerDocument The document which will own this comment.
	@exception IOException Thrown when an i/o error occurs.
	@exception XMLSyntaxException Thrown when there is a syntax error in the XML file.
	@exception XMLWellFormednessException Thrown when there is a well-formedness error in the XML file.
	@exception ParseUnexpectedDataException Thrown when an unexpected character is found.
//G***del	@exception ParseEOFException Thrown when the end of the input stream is reached unexpectedly.
	@return A new comment object constructed from the reader.
	*/
	protected XMLComment parseComment(final XMLReader reader, final XMLDocument ownerDocument) throws IOException, XMLSyntaxException, XMLWellFormednessException, ParseUnexpectedDataException//G***del, ParseEOFException
	{
		try
		{
			String characterData=reader.readDelimitedString(COMMENT_START, COMMENT_END_PART1);	//read all the characters in the comment section between the COMMENT_START and COMMENT_END_PART1 delimiters
			XMLComment comment=new XMLComment(ownerDocument, characterData);	//create a new comment object
			reader.readExpectedString(COMMENT_END_PART2);	//read the second part of the end of the comment (this two-step process is because XML doesn't allow "--" inside of comment for compatibility reasons)
			return comment;	//return the comment we constructed
		}
		catch(final ParseEOFException parseEOFException)	//if we run out of data
		{
			throw new XMLWellFormednessException(parseEOFException, ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME).getString(RESOURCE_PREFIX+COMMENT_RESOURCE), ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME).getString(RESOURCE_PREFIX+UNKNOWN_RESOURCE));	//show that we couldn't find the end of the comment
		}
	}

	/**Parses an input stream that supposedly contains an XML element.
	@param reader The reader from which to retrieve characters.
	@param ownerDocument The document which will own this element.
	@param tag The tag that marks the start of this element, which can be a start tag or an empty element tag.
	@exception IOException Thrown when an i/o error occurs.
	@exception XMLSyntaxException Thrown when there is a syntax error in the XML file.
	@exception XMLWellFormednessException Thrown when there is a well-formedness error in the XML file.
	@exception XMLValidityException Thrown when there is a validity error in the XML file.
	@exception XMLUndefinedEntityReferenceException Thrown if a named entity reference has not been defined.
	@exception ParseUnexpectedDataException Thrown when an unexpected character is found.
//G***del	@exception ParseEOFException Thrown when the end of the input stream is reached unexpectedly.
	@return A new XML element with the appropriate content.
	*/
	protected XMLElement parseElement(final XMLReader reader, final XMLDocument ownerDocument, final XMLTag tag) throws IOException, XMLSyntaxException, XMLWellFormednessException, XMLValidityException, XMLUndefinedEntityReferenceException, ParseUnexpectedDataException//G***del, ParseEOFException
	{
		//G***store the start position of the element so we can report the error location
			//G***check to make sure that they passed us a start tag or an empty element tag, not an ending tag
//G***del		final XMLElement element=(XMLElement)ownerDocument.createElement(tag.getNodeName());  //create a new XML element with the same name as the tag (which should either be a start tag or an empty element tag)
//G***del when works		XMLElement element=new XMLElement(ownerDocument, tag.getNodeName());  //create a new XML element with the same name as the tag (which should either be a start tag or an empty element tag)
		final XMLElement element=new XMLElement(ownerDocument, tag);  //create a new XML element with the same name and attributes as the tag (which should either be a start tag or an empty element tag)
		try
		{
//G***del when works			element.setAttributeXMLNamedNodeMap(tag.getAttributeXMLNamedNodeMap());	//give the element the same list of attributes that the tag formed from the file

	/*G***del when works
			for(int i=0; i<tag.getAttributeList().size(); ++i)	//look at all of the attributes-value pairs of the tag
				element.addAttribute((XMLAttribute)tag.getAttributeList().get(i));	//add this attribute to the element G***check for duplicates and such
	*/

			if(tag.getTagType()==XMLTag.EMPTY_ELEMENT_TAG)	//if this is an empty element tag
				return element;	//there is no content in this element
	//G***del		final Map nestedEntityReferenceMap=new HashMap();	//create a new map that will tell us which references we're nesting to prevent recursion
			if(!parseElementContent(reader, ownerDocument, element, ""))	//parse the element content, showing that we have not yet found any character content; if we couldn't find the end of the element
			{
//G***del Debug.trace("Before throwing XMLWellFormednessException from parsing the element content, element declartion resource is: "+ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME).getString(RESOURCE_PREFIX+ELEMENT_RESOURCE));
				throw new XMLWellFormednessException(XMLWellFormednessException.EOF, new Object[]{ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME).getString(RESOURCE_PREFIX+ELEMENT_RESOURCE), element.getNodeName()}, reader.getLineIndex(), reader.getCharIndex(), reader.getName());	//show that we couldn't find the end of the element
			}
	//G***del			throw new ParseEOFException("XML Unexpected end of stream reached while searching for ending tag of element \""+element.getNodeName()+"\", which began at G***fix.", reader.getLineIndex(), reader.getCharIndex(), reader.getName());	//show that we hit the end of the file while searching for the ending tag of the element G***Int
//G***del	Debug.trace("XMLParse Finished parsing element "+element.getNodeName()+" with text: "+element.getText());	//G***del
			return element;	//return the element we constructed
		}
		catch(final ParseEOFException parseEOFException)	//if we run out of data
		{
			throw new XMLWellFormednessException(parseEOFException, ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME).getString(RESOURCE_PREFIX+ELEMENT_RESOURCE), element.getNodeName());	//show that we couldn't find the end of the element
		}

	}

	/**Parses an input stream that contains content of an element.
	If this function reaches the end of the stream of the reader without finding
	an ending tag for the element, an exception is not thrown; rather, the return
	code will indicate if the end tag was found.
	@param reader The reader from which to retrieve characters.
	@param ownerDocument The document which will own the data.
	@param element The element that this content belongs to.
	@param characterContent Any character content which has already been accumulated for this element.
	@exception IOException Thrown when an i/o error occurs.
	@exception XMLSyntaxException Thrown when there is a syntax error in the XML file.
	@exception XMLWellFormednessException Thrown when there is a well-formedness error in the XML file.
	@exception XMLValidityException Thrown when there is a validity error in the XML file.
	@exception XMLUndefinedEntityReferenceException Thrown if a named entity reference has not been defined.
	@exception ParseUnexpectedDataException Thrown when an unexpected character is found.
	@exception ParseEOFException Thrown when the end of the input stream is reached unexpectedly.
	@return <code>true</code> if the ending tag for this element was found, else <code>false</code>.
	*/
	protected boolean parseElementContent(final XMLReader reader, final XMLDocument ownerDocument, final XMLElement element, String characterContent) throws IOException, XMLSyntaxException, XMLWellFormednessException, XMLValidityException, XMLUndefinedEntityReferenceException, ParseUnexpectedDataException, ParseEOFException
	{
		while(true)	//keep reading content; when we find the ending tag we'll return from there
		{
				//G***we *might* be able to improve this characterContent somewhat by using a string buffer
			characterContent+=reader.readStringUntilCharEOF("<&");	//read character content until we find non-character data markup, such as a tag or comment or a reference, or until we reach the end of the file G***make these constants
//G***del Debug.trace("XMLParse parseElementContent() still reading more data.");	//G***del
//G***del Debug.trace("characterContent: "+characterContent);	//G***del
			if(reader.isEOF())	//if we run out of characters looking for the end of the tag, this may not be an error; we may have just been parsing a general entity reader
			{
				element.appendText(characterContent);	//append whatever text we've collected so far to the element
				return false;	//return, showing that we have not yet found the ending tag
			}
			switch(reader.peekChar())	//see which character comes next
			{
				case '&':	//if this is a character or entity reference
					if(reader.peekChar()=='#')	//if this is a character reference G***make this a constant
						characterContent+=parseCharacterReference(reader);	//parse this character reference and add the character to our character content
					else	//if this is not a character reference, it must be an entity reference
					{

//G***del System.out.println("Found entity reference.");	//G***del

						final Object entityResult=parseEntityReference(reader, ownerDocument);	//parse this entity reference and see which entity it refers to G***do we want to parse the entity and store everything as an entity reference node?
						if(entityResult instanceof XMLEntity)	//if we found a matching entity
						{
							final XMLEntity entity=(XMLEntity)entityResult;	//case the result to an entity
							if(nestedEntityReferenceMap.containsKey(entity))	//if, somewhere along our nested way, we've already processed this entity (i.e. this is a circular entity reference)
								throw new XMLWellFormednessException(XMLWellFormednessException.NO_RECURSION, new Object[]{entity.getNodeName()}, reader.getLineIndex(), reader.getCharIndex(), reader.getName());	//show that an entity can't reference itself, directly or indirectly G***we need to give a better position in the error, here
							final XMLReader entityReader=new XMLReader(entity.getText(), "General entity \""+entity.getNodeName()+"\" from \""+entity.getSourceName()+"\"");	//create a string reader from the text of the entity, giving our entity name for the name of the reader G***Int

entityReader.tidy=isTidy();  //G***fix

//G***del when works newio							final ParseReader entityReader=new ParseReader(new StringReader(entity.getText()), "General entity \""+entity.getNodeName()+"\" from \""+entity.getSourceName()+"\"");	//create a string reader from the text of the entity, giving our entity name for the name of the reader G***Int
							entityReader.setLineIndex(entity.getLineIndex());	//pretend we're reading where the entity was located in that file, so any errors will show the correct information
							entityReader.setCharIndex(entity.getCharIndex());	//pretend we're reading where the entity was located in that file, so any errors will show the correct information
							nestedEntityReferenceMap.put(entity, entity);	//show that we're getting ready to process this entity
							if(parseElementContent(entityReader, ownerDocument, element, characterContent))	//parse the entity text just as if it were included in the file (except for markup across entity boundaries); if they found ending tag for the element was found
								return true;	//show that we found the end of the element
							nestedEntityReferenceMap.remove(entity);	//show that we're finished processing this entity
							characterContent="";	//since we passed the character content to parseElementContent(), it will have already been appended to the text of this element
						}
						else	//if there was no matching entity, yet this doesn't cause a well-formedness error
						{
							//G***if we're validating, we'll probably want to throw a validity error, here
//G***del							characterContent+=reader.readDelimitedStringInclusive("&", ";");	//read the entire entity reference and include it in the character data
							characterContent+=(String)entityResult;	//store the entity reference name in our character data
						}
					}	//G***perhaps have a more instructive error here if they try to split up general entities across entities
					break;
				case '<':	//if this is some sort of markup G***make these constant somewhere or something
//G***del Debug.trace("XMLParse parseElementContent() another '<'.");	//G***del
					if(characterContent.length()>0)	//if we have collected character content
					{
													//G***should we check for a DOMException and throw something based on XMLException here?
						element.appendChild(new XMLText(ownerDocument, characterContent));	//create a text node from the character element we've collected so far and add it to this element's children
						characterContent="";	//show that other character content (if any) will appear after the element we're beginning to parse (if that's what it is), and will therefore be a separate XMLText node
					}
//G***del				case '&':
//G***del					unreadCharacter(c);	//push back the character

//G***del System.out.println("Found some sort of markup.\n");	//G***del

//G***del Debug.trace("XMLParse parseElementContent() ready to parse markup.");	//G***del
					XMLNode node=parseMarkup(reader, ownerDocument);	//parse this bit of markup, and get a node which represents it
//G***del Debug.trace("parsed element content markup: ", node);
					switch(node.getNodeType())	//see what type of node we just parsed
					{
						case XMLNode.TAG_NODE:	//if we just read a tag
							final XMLTag childTag=(XMLTag)node;	//cast the node to a tag
							if(isTidy())  //if we're tidying the document
							  tidyTag(element, childTag); //tidy the tag; this may change the tag to an empty element tag, if it's supposed to be
							switch(childTag.getTagType())	//see what type of tag this is
							{
								case XMLTag.START_TAG:		//if this is a starting tag
								case XMLTag.EMPTY_ELEMENT_TAG:		//if this is an empty element tag
									if(isTidy()) //if we're tidying the document
								  {
										if(!isName(childTag.getNodeName()))  //if the tag doesn't have a valid name
										{
											break;  //ignore this tag
										}
									  if(!isTidyContentModel(element, childTag)) //if this tag shouldn't be a child of the enclosing element
										{
											reader.unread(childTag.toXMLString().toCharArray()); //unread the child tag so that it will be read the next time instead of processing it now G***use constants here
											element.appendText(characterContent);	//append whatever text we've collected so far to the element G***testing
											return true;	//we're finished parsing all the element content we know about, so return G***how will we know when we're at the end of the tag if we're just looking at a string?
										}
								  }
									final XMLElement childElement=parseElement(reader, ownerDocument, childTag);	//parse the child element
//G***del Debug.trace("XMLParse parseElement() finished parsing child element and ready to append it: ", element.getNodeName());	//G***del
													//G***should we check for a DOMException and throw something based on XMLException here?
									element.appendChild(childElement);	//add this child element to our list of children
									break;
								case XMLTag.END_TAG:	//if this is the ending tag
								default:	//G***why do we check for end tag and also have a default?
//G***do we really want empty ending tags to be allowed?									if(childTag.getNodeName().length()==0 && isAllowUnnamedEndTags())	//if we should allow unnamed end tags (for compatibility with MSXML-generated XML files, for example)
//G***do we really want empty ending tags to be allowed?										foundEndTag=true;	//show that we've found the end tag for this element
//G***do we really want empty ending tags to be allowed?									else if(!childTag.getNodeName().equals(element.getNodeName()))	//if the ending tag doesn't have the same name as the beginning tag
									if(!childTag.getNodeName().equals(element.getNodeName()))	//if the ending tag doesn't have the same name as the beginning tag
									{
										if(isTidy())  //if we're tidying the document
										{
//G***del Debug.trace("child tag: "+childTag+" doesn't equal element beginning tag: "+element); //G***del
											//assume there was just a missing end-tag for this element, and hopefully *this* tag is the ending tag to one of our ancestors
											reader.unread(childTag.toXMLString().toCharArray()); //unread the ending tag so that it will be read the next time as an end tag for our parent instead of processing it now G***use constants here
										}
										else  //if we're not tidying the document
											throw new XMLWellFormednessException(XMLWellFormednessException.ELEMENT_TYPE_MATCH, new Object[]{element.getNodeName(), childTag.getNodeName()}, reader.getLineIndex(), reader.getCharIndex(), reader.getName());	//show that the beginning and ending tags do not match
									}
//G***del when works										throw new XMLWellFormednessException("Ending tag for element \""+element.getNodeName()+"\" is does not match: \""+childTag.getNodeName()+"\".", reader.getLineIndex(), reader.getCharIndex(), reader.getName());	//show that the beginning and ending tags do not match
									element.appendText(characterContent);	//append whatever text we've collected so far to the element
									return true;	//we're finished parsing all the element content we know about, so return G***how will we know when we're at the end of the tag if we're just looking at a string?
							}
							break;
						case XMLNode.DOCUMENT_TYPE_NODE:  //if this is a document type node, this will be an error unless we're tidying, in which case we'll use the new document type
							if(isTidy())  //if we're tidying
								break;  //ignore the error G***we need to first set the document type, after we remove that functionality from the parseDocumentType() method
							//if we're not tidying, we'll fall through to the default functionality which will generate an error G***maybe explicitly do the error stuff here
						default:	//all other node types get added normally
//G***del System.out.println("Adding child element "+node.getNodeName()+" to elemement "+element.getNodeName());	//G***del

//G***del Debug.trace("Adding child element "+node.getNodeName()+" to elemement "+element.getNodeName());	//G***del
							element.appendChild(node);	//add this child node to our list of children
//G***del wxhen fixed							element.getChildren().add(object);	//add this child node to our list of children
							break;
					}
					break;
			}
		}
	}

	/**Does	whatever processing is needed to tidy the specified tag before its
		being returned. This will, for example, check to see if a tag represents
		an empty element, and if so, automatically convert that tag to an empty
		element tag.
		This function is called from the <code>parseTag()</code> method.
		This function is only called in tidy mode.
	@param parentNode The node which will become the tag's parent.
	@param childTag The tag to become a child of the element.
	@see #isTidy
	@see #parseTag
	*/
	protected void tidyTag(final XMLNode parentNode, final XMLTag childTag)
	{
		final String tagName=childTag.getNodeName().toLowerCase(); //get the name of the tag, and change the name to lowercase G***later, check the schema for case
//G***del Debug.trace("tidying tag: "+tagName);
		childTag.setNodeName(tagName);  //change the child tag name, in case our changing its case changed the name
		  //tidy the attribute names
//G***del Debug.trace("tag: "+childTag);
		final XMLNamedNodeMap attributeXMLNamedNodeMap=(XMLNamedNodeMap)childTag.getAttributes(); //get the attributes
//G***del Debug.trace("attributes: "+attributeXMLNamedNodeMap);
		if(attributeXMLNamedNodeMap.size()!=0) //if there are attributes
		{
			final XMLNamedNodeMap newAttributeNamedNodeMap=new XMLNamedNodeMap(); //create a new named node map that will contain the new tidied values
			final Iterator attributeIterator=attributeXMLNamedNodeMap.values().iterator(); //create an iterator to iterate through the original attributes
			while(attributeIterator.hasNext())  //while there are more attributes
			{
				final XMLAttribute attribute=(XMLAttribute)attributeIterator.next();  //get the next attribute
				final String attributeName=attribute.getNodeName().toLowerCase();  //get the attribute's name and convert the name to lowercase G***later make this namespace aware when we update namespaces on the fly from within the XML processor
//G***del Debug.trace("tidying attribute: "+attributeName);

				attribute.setNodeName(attributeName); //change the attribute name to its lowercase version
				newAttributeNamedNodeMap.setNamedItem(attribute); //add the attribute to our new named node map with its new name G***make this namespace aware, too, when needed
			}
			childTag.setAttributeXMLNamedNodeMap(newAttributeNamedNodeMap); //now that we've processed the attributes, replace the old non-namespace-aware attrites with our new ones that recognize namespaces
		}
		if(tagName.equals("br") //G***get all this from the DTD
			  || tagName.equals("area")
			  || tagName.equals("hr")
			  || tagName.equals("img")
			  || tagName.equals("link")
			  || tagName.equals("param")
			  || tagName.equals("frame")
			  || tagName.equals("meta"))
			childTag.setTagType(XMLTag.EMPTY_ELEMENT_TAG);  //convert the tag to an empty element tag
	}

	/**Determines whether a child tag fits in in the content model of a given
		element. If this function returns <code>false</code>, the enclosing element
		will be tidied by implicitly being finished before the child tag is processed.
		This function is only called in tidy mode.
	@param parentElement The element which will become the tag's parent.
	@param childTag The tag to become a child of the element.
	@return <code>true</code> If the tag should become a child of the element.
	@see #isTidy
	*/
	protected boolean isTidyContentModel(final XMLElement parentElement, final XMLTag childTag)
	{
//G***del Debug.trace("Checking tidy content model: can "+parentElement.getNodeName()+" have child "+childTag.getNodeName());
				//G***testing; these are elements which cannot have content
		if(parentElement.getNodeName().equals("meta")
			  || parentElement.getNodeName().equals("link"))
			return false; //G***testing
//G***del Debug.trace("Checking tidy content model with: "+parentElement.getNodeName()+" and "+childTag.getNodeName());
		if(parentElement.getNodeName().equals("p") &&
			(childTag.getNodeName().equals("p") || childTag.getNodeName().equals("body")))  //G***testing; comment
			return false; //G***testing
		return true;  //G***fix
	}

	/**Parses an XML document.
	@param reader The reader from which to get the XML data.
//G***fix	@param validate Whether the document should be validated against stylesheets, if any.
//G***fix	@param processStyleSheets Whether any stylesheets encountered should be recognized and processed as such.
//G***fix	@param applyStyleSheets Whether any stylesheets encountered should be applied.
	@exception IOException Thrown when an i/o error occurs.
	@exception XMLSyntaxException Thrown when there is a syntax error in the XML file.
	@exception XMLWellFormednessException Thrown when there is a well-formedness error in the XML file.
	@exception XMLValidityException Thrown when there is a validity error in the XML file.
	@exception XMLUndefinedEntityReferenceException Thrown if a named entity reference has not been defined.
	@return A new XML document.
	*/
	protected XMLDocument parseDocument(final XMLReader reader) throws IOException, XMLSyntaxException, XMLWellFormednessException, XMLValidityException, XMLUndefinedEntityReferenceException
	{
		long nodeLineIndex=0, nodeCharIndex=0;	//make a note of where each node begins

		XMLDocument document=new XMLDocument();	//create a new document	G***use the XMLDOMImplementation function here
//G***del		final XMLReader reader=InReader;	//G***comment, maybe replace with getInReader()
		//G***do we want to store the default values somewhere else? perhaps in the document itself?
					//G***use constants below
		//create the default XML entity references; note that if these would have been declared in the actual MXL document, the &#38; would already have been expanded to '&', as shown here
		getXMLDocumentType().getEntityXMLNamedNodeMap().setNamedItem(new XMLEntity(document, "lt", "&#60;"));	//lt
		getXMLDocumentType().getEntityXMLNamedNodeMap().setNamedItem(new XMLEntity(document, "gt", "&#62;"));	//gt
		getXMLDocumentType().getEntityXMLNamedNodeMap().setNamedItem(new XMLEntity(document, "amp", "&#38;"));	//amp
		getXMLDocumentType().getEntityXMLNamedNodeMap().setNamedItem(new XMLEntity(document, "apos", "&#39;"));	//apos
		getXMLDocumentType().getEntityXMLNamedNodeMap().setNamedItem(new XMLEntity(document, "quot", "&#34;"));	//quot
		try
		{
			XMLNode node=parseMarkup(reader, document);	//parse the markup
//G***del Debug.trace("Parsed root node: ", node);
			if(node.getNodeType()!=XMLNode.ELEMENT_NODE)	//if this is not the root element of the document G***would this really ever return an element node? G***no, it wouldn't; remove this line
			{
				if(node.getNodeType()==XMLNode.XMLDECL_NODE)	//if this is an XML declaration
				{
									//G***should we check for a DOMException and throw something based on XMLException here?
					document.setXMLDeclaration((XMLDeclaration)node);	//tell the document which XML declaration it has
	//G***decide if we want to do this or not			document.appendChild(node);	//store the XML declaration in the document
					//G***store the XML declaration here somewhere
					skipWhitespaceCharacters(reader);	//skip any whitespace characters
					nodeLineIndex=reader.getLineIndex();	//store the current line index
					nodeCharIndex=reader.getCharIndex();	//store the current character index G***do we need to add one to this?
					node=parseMarkup(reader, document);	//parse the following markup
				}
				boolean foundDTD=false;	//show that we haven't found a document type declaration, yet
				while(node.getNodeType()!=XMLNode.TAG_NODE)	//we won't be finished with the prolog until we've found the root element of the document
				{
					switch(node.getNodeType())	//see which type of node this is
					{
						case XMLNode.XMLDECL_NODE:	//if this is an XML declaration
							throw new XMLSyntaxException("XML declaration not allowed here.", nodeLineIndex, nodeCharIndex, reader.getName());	//show that an XML declaration isn't allowed here G***this probably isn't a syntax error G***Int
						case XMLNode.DOCUMENT_TYPE_NODE:	//if this is a document
							if(foundDTD)	//if we've already found the document type declaration
								throw new XMLSyntaxException("Cannot have more than one document type declaration.", nodeLineIndex, nodeCharIndex, reader.getName());	//show that there can't be more than one DTD G***this probably isn't a syntax error G***Int
							foundDTD=true;	//show that we found the document type declaration
							break;
					}
					//G***see if we should check to make sure this is a comment or processing instruction
					document.appendChild(node);	//whatever type of node it is, append it to our document
					skipWhitespaceCharacters(reader);	//skip any whitespace characters
					nodeLineIndex=reader.getLineIndex();	//store the current line index
					nodeCharIndex=reader.getCharIndex();	//store the current character index G***do we need to add one to this? is this even used?
					node=parseMarkup(reader, document);	//parse the following markup
				}
			}
			final XMLTag rootTag=(XMLTag)node;  //cast the root node to a tag
//G***del Debug.trace("Found the root tag: ", rootTag);
			if(isTidy())  //if we're tidying the document
				tidyTag(document, rootTag); //tidy the tag; this may change the tag to an empty element tag, if it's supposed to be
				//if tidying, load a default DTD if needed
			if(isTidy() && document.getXMLDocumentType()==null)  //if we're tidying the document and have not yet created a document type (because there has been no DTD, neither internal nor external)
			{
//G***del Debug.trace("Is tidy, but document type is null");
				final XMLExternalID tidyDocumentTypeExternalID=getTidyDocumentTypeExternalID();  //see if there is an overriding external ID to use
				if(tidyDocumentTypeExternalID!=null)  //if there is an external ID we should use for tidying
				{
//G***del Debug.trace("A tidy external ID has been requested");
					final XMLDocumentType documentType=(XMLDocumentType)getXMLDocumentType().cloneXMLNode(true);	//we'll use the default XML document type we've already built G***do we want to do it this way?
					document.setXMLDocumentType(documentType);	//tell the owner document which document type it has
					documentType.setExternalID(tidyDocumentTypeExternalID); //update the document's external ID to match the one required by tidying
						//parse the external document type requested for tidying G***fix the XMLNodeList parameters; remove when schemas/real DTD parsing works
				  parseExternalDocumentType(reader, document, documentType, new XMLNodeList(), new XMLNodeList());
//G***del Debug.trace("Parsed external document type, now have general entities: ", documentType.getEntities().getLength());  //G***del
				}
			}
				//G***remove this when default tidy DTDs completely work
			if(isTidy() && rootTag.getNodeName().equals("html"))  //G***testing; maybe put this in another function to be overridden; comment; there must be a better way to do this
			{
				getXMLDocumentType().getEntityXMLNamedNodeMap().setNamedItem(new XMLEntity(document, "nbsp", "&#160;"));	//G***fix; comment; get this from some default DTD
			}
//G***del Debug.trace("Ready to parse the element for the tag: ", rootTag); //G***del
			final XMLElement root=parseElement(reader, document, rootTag);	//parse the child element
			final XMLDocumentType docType=document.getXMLDocumentType();	//get a document type if we have one
//G***del System.out.println("root: "+root.getNodeName()+" docType: "+docType.getName());	//G***del
			if(docType!=null && !root.getNodeName().equals(docType.getName()))	//if we have a document type, and the root's name doesn't match what it calls for G***only do this if we should validate, but perhaps *always* tidy if needed, whether we're validating or not
			{
				if(isTidy())  //if we're tidying
					docType.setNodeName(root.getNodeName());  //make the document type name match the root node name G***should we do this after namespace processing? or should we put namespace processing in the processor itself?
				else  //if we're not tidying, this consitutes a validity error
					throw new XMLValidityException(XMLValidityException.ROOT_ELEMENT_TYPE, new Object[]{root.getNodeName(), docType.getName()}, reader.getLineIndex(), reader.getCharIndex(), reader.getName());	//show that this element type has already been declared G***give a better indication of the element type declaration's location
			}
			document.appendChild(root);	//add the root element to the document
			//G***fix to allow items after the document element
	//G***del		XMLElement root=parseElement(reader, document, (XMLTag)node);	//parse the child element
	//G***del		document.setRoot(root);	//tell the document which root it has




//G***somewhere here, catch a sun.io.MalformedInputException when the input data is bad


		}
		catch(final ParseUnexpectedDataException parseUnexpectedDataException)	//if we run into data we don't recognize
		{
//G***del; not needed			throw (XMLWellFormednessException)new XMLWellFormednessException(parseUnexpectedDataException).fillInStackTrace();	//create a well-formedness exception and throw it G***testing fillInStackTrace()
			throw new XMLWellFormednessException(parseUnexpectedDataException);	//create a well-formedness exception and throw it
		}
		catch(final ParseEOFException parseEOFException)	//if we run out of data
		{
			throw new XMLWellFormednessException(parseEOFException, ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME).getString(RESOURCE_PREFIX+DOCUMENT_RESOURCE), document.getNodeName()!=null ? document.getNodeName() : ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME).getString(RESOURCE_PREFIX+UNKNOWN_RESOURCE));	//show that we couldn't find the end of the document
		}
		//G***decide if we should close the reader ourselves here



		//G***process namespaces here





//G***del Debug.trace("Ready to process namespaces.");
		XMLNamespaceProcessor.processNamespaces(document);  //G***testing
		//G***catch whatever DOMExceptions are thrown here



		return document;	//return the parsed document
	}

	/**Parses an XML document from an input stream.
	@param inputStream The input stream from which to get the XML data.
	@param sourceObject The source of the data (e.g. a String, File, or URL).
	@exception IOException Thrown when an i/o error occurs.
	@exception XMLSyntaxException Thrown when there is a syntax error in the XML file.
	@exception XMLWellFormednessException Thrown when there is a well-formedness error in the XML file.
	@exception XMLValidityException Thrown when there is a validity error in the XML file.
	@exception XMLUndefinedEntityReferenceException Thrown if a named entity reference has not been defined.
	@return A new XML document.
	*/
	public XMLDocument parseDocument(final InputStream xmlInputStream, final Object sourceObject) throws IOException, XMLSyntaxException, XMLWellFormednessException, XMLValidityException, XMLUndefinedEntityReferenceException
	{
		final XMLReader xmlReader=createReader(xmlInputStream, sourceObject); //create a reader from the input stream
		return parseDocument(xmlReader);	//parse the document using the reader we constructed, and return the new document
	}

	/**Parses an XML document from a reader.
	@param reader The input stream from which to get the XML data.
	@param sourceObject The source of the data (e.g. a String, File, or URL).
	@exception IOException Thrown when an i/o error occurs.
	@exception XMLSyntaxException Thrown when there is a syntax error in the XML file.
	@exception XMLWellFormednessException Thrown when there is a well-formedness error in the XML file.
	@exception XMLValidityException Thrown when there is a validity error in the XML file.
	@exception XMLUndefinedEntityReferenceException Thrown if a named entity reference has not been defined.
	@return A new XML document.
	*/
	public XMLDocument parseDocument(final Reader reader, final Object sourceObject) throws IOException, XMLSyntaxException, XMLWellFormednessException, XMLValidityException, XMLUndefinedEntityReferenceException
	{
		final XMLReader xmlReader=new XMLReader(reader, sourceObject);	//create an XML reader from the reader
		return parseDocument(xmlReader);	//parse the document using the reader we constructed, and return the new document
	}

	/**Parses a string which contains content for the given element. The text and
		element nodes are appended to the given element. The given element must have
		a valid document as its owner. If an ending tag for the specified element is
		present, an <code>XMLWellFormednessException</code> will be thrown.
	@param element The element to which the parsed XML sub-tree should be appended.
	@param elementContent The string which contains information to parse.
	@exception IOException Thrown when an i/o error occurs.
	@exception XMLSyntaxException Thrown when there is a syntax error in the XML file.
	@exception XMLWellFormednessException Thrown when there is a well-formedness error in the XML file.
	@exception XMLValidityException Thrown when there is a validity error in the XML file.
	@exception XMLUndefinedEntityReferenceException Thrown if a named entity reference has not been defined.
	*/
	public void parseElementContent(final XMLElement element, final String elementContent) throws IOException, XMLSyntaxException, XMLWellFormednessException, XMLValidityException, XMLUndefinedEntityReferenceException
	{
		final XMLDocument document=(XMLDocument)element.getOwnerDocument();  //get the owner document of the element
		final XMLReader reader=new XMLReader(elementContent, "Element Content");	//create a string reader from the given element content G***i18n

reader.tidy=isTidy();  //G***fix


		try
		{
			if(parseElementContent(reader, (XMLDocument)element.getOwnerDocument(),
				element, ""))	//parse the element content, showing that we have not yet found any character content; if we found the end of the element
			{
				throw new XMLWellFormednessException(XMLWellFormednessException.UNEXPECTED_DATA, new Object[]{"", "</"+element.getNodeName()+">"}, reader.getLineIndex(), reader.getCharIndex(), reader.getName());	//show that we shouldn't have found the end of the element G***use a constant here
			}
		}
		catch(final ParseUnexpectedDataException parseUnexpectedDataException)	//if we run into data we don't recognize
		{
			throw new XMLWellFormednessException(parseUnexpectedDataException);	//create a well-formedness exception and throw it
		}
		catch(final ParseEOFException parseEOFException)	//if we run out of data
		{
			throw new XMLWellFormednessException(parseEOFException, ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME).getString(RESOURCE_PREFIX+DOCUMENT_RESOURCE), document.getNodeName()!=null ? document.getNodeName() : ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME).getString(RESOURCE_PREFIX+UNKNOWN_RESOURCE));	//show that we couldn't find the end of the document
		}
	}

	/**Parses an XML document, validates it if possible, and processes and applies any stylesheets.
	@exception IOException Thrown when an i/o error occurs.
	@exception XMLSyntaxException Thrown when there is a syntax error in the XML file.
	@exception XMLWellFormednessException Thrown when there is a well-formedness error in the XML file.
	@exception XMLValidityException Thrown when there is a validity error in the XML file.
	@exception XMLUndefinedEntityReferenceException Thrown if a named entity reference has not been defined.
//G***del	@exception ParseUnexpectedDataException Thrown when an unexpected character is found.
//G***del	@exception ParseEOFException Thrown when the end of the input stream is reached unexpectedly.
	@return A new XML document.
	*/
/*G***fix
	public XMLDocument parseDocument() throws IOException, XMLSyntaxException, XMLWellFormednessException, XMLValidityException, XMLUndefinedEntityReferenceException
	{
		return parseDocument(true, true, true);	//parse the document, validate it, and process and apply any stylesheets
	}
*/

}
