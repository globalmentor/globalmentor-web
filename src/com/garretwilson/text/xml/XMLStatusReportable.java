package com.garretwilson.text.xml;

/**XML parsing error peporting interface.*/
public interface XMLStatusReportable
{

	/**Reports an error in the form of an XML exception.
	@param xmlException The exception which holds information about the error.
	*/
	public void reportError(final XMLParseException xmlParseException);

}
