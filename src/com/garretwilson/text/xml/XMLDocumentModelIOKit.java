package com.garretwilson.text.xml;

import java.io.*;
import java.net.*;
import com.garretwilson.io.*;
import com.garretwilson.model.*;
import com.garretwilson.text.CharacterEncodingConstants;
import org.w3c.dom.Document;

/**Class for loading and saving an XML document model.
@author Garret Wilson
@see XMLDocumentModel
*/
public class XMLDocumentModelIOKit extends AbstractModelIOKit
{

	/**Default constructor.*/
	public XMLDocumentModelIOKit()
	{
		this(null, null);
	}

	/**URI input stream locator constructor.
	@param uriInputStreamable The implementation to use for accessing a URI for
		input, or <code>null</code> if the default implementation should be used.
	*/
	public XMLDocumentModelIOKit(final URIInputStreamable uriInputStreamable)
	{
		this(uriInputStreamable, null);
	}

	/**URI output stream locator constructor.
	@param uriOutputStreamable The implementation to use for accessing a URI for
		output, or <code>null</code> if the default implementation should be used.
	*/
	public XMLDocumentModelIOKit(final URIOutputStreamable uriOutputStreamable)
	{
		this(null, uriOutputStreamable);
	}

	/**Full constructor.
	@param uriInputStreamable The implementation to use for accessing a URI for
		input, or <code>null</code> if the default implementation should be used.
	@param uriOutputStreamable The implementation to use for accessing a URI for
		output, or <code>null</code> if the default implementation should be used.
	*/
	public XMLDocumentModelIOKit(final URIInputStreamable uriInputStreamable, final URIOutputStreamable uriOutputStreamable)
	{
		super(uriInputStreamable, uriOutputStreamable);	//construct the parent class
	}

	/**Loads a model from an input stream.
	@param inputStream The input stream from which to read the data.
	@param baseURI The base URI of the content, or <code>null</code> if no base
		URI is available.
	@throws IOException Thrown if there is an error reading the data.
	*/ 
	public Model load(final InputStream inputStream, final URI baseURI) throws IOException
	{
		inputStream.mark(Integer.MAX_VALUE);	//G***testing
		final XMLProcessor xmlProcessor=new XMLProcessor(this);	//create a new XML processor that knows how to access streams from URIs
		final Document xml=xmlProcessor.parseDocument(inputStream, baseURI);	//parse the XML
		return new XMLDocumentModel(xml, baseURI, this);	//return a new XML document model
			//TODO check for XML DOM exceptions---not just here, but throughout the code; update the XMLProcessor, too
	}
	
	/**Saves a model to an output stream.
	@param model The model the data of which will be written to the given output stream.
	@param outputStream The output stream to which to write the model content.
	@throws IOException Thrown if there is an error writing the model.
	*/
	public void save(final Model model, final OutputStream outputStream) throws IOException
	{
		final Writer writer=new OutputStreamWriter(outputStream, CharacterEncodingConstants.UTF_8);	//create a UTF-8 writer
		new XMLSerializer(true).serialize(((XMLDocumentModel)model).getDocument(), outputStream);	//serialize the XML document
		writer.flush();	//flush the data to the output stream
	}
}
