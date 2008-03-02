package com.globalmentor.text.xml;

/**Invalid name error.
@see XMLSyntaxException
*/
public class XMLInvalidNameException extends XMLSyntaxException
{
//G***comment these
	public XMLInvalidNameException(final String name) {super("Invalid XML name: \""+name+"\".");}
}

