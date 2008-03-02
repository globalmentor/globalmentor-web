package com.globalmentor.text.xml.events;

import java.util.ListResourceBundle;

/**Resource bundle for DOM event exceptions.*/
public class XMLEventExceptionResources extends ListResourceBundle
{
	public Object[][] getContents() {return contents;}	//returns the error strings

	static final Object[][] contents=
	{
		//DOM errors (DOMException)
				//error name
		{XMLEventException.RESOURCE_PREFIX+XMLEventException.ERROR_NAME_RESOURCE_ID, "XML Event Error"},
				//error codes
		{XMLEventException.RESOURCE_PREFIX+XMLEventException.UNSPECIFIED_EVENT_TYPE_ERR, "(Unspecified Event Type Error) The event's type was not specified before dispatching."},
	};
}

