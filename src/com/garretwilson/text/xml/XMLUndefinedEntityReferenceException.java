package com.garretwilson.text.xml;

/**Undefined entity reference error.
@see XMLParseException
*/
public class XMLUndefinedEntityReferenceException extends XMLParseException
{
//G***comment these
	/**
	@param sourceName The name of the source of the XML document (perhaps a filename).

	*/
	public XMLUndefinedEntityReferenceException(final String entityReference, final long lineIndex, final long charIndex, final String sourceName)
	{
		super("XML undefined entity reference: \""+entityReference+"\".", lineIndex, charIndex, sourceName);
	}
}

