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

package com.globalmentor.text.xml.processor.events;

import java.util.ResourceBundle;
import java.text.MessageFormat;
import org.w3c.dom.events.EventException;

/**
 * Exception class for all XML DOM event errors.
 * @author Garret Wilson
 * @deprecated
 */
public class XMLEventException extends EventException {

	/** The base name used for getting resources. */
	//TODO move this to XMLException, make XMLException an interface, and implement that interface here
	protected static final String RESOURCE_BUNDLE_BASE_NAME = XMLEventException.class.getPackage().getName() + ".XMLEventExceptionResources"; //TODO implement this resource file

	/** The prefix used as a prefix for all event error string resources. */
	protected static final String RESOURCE_PREFIX = "Event";

	/** A constant identifying the name of the exception in the resources. */
	protected static final short ERROR_NAME_RESOURCE_ID = 100;

	//Resource string pattern arguments:

	/**
	 * Event Error: UNSPECIFIED_EVENT_TYPE_ERR<br/>
	 * Arguments: None.
	 */

	/**
	 * Constructor for a DOM event error, with error code specified.
	 * @param code The type of error; one of the constants from org.w3c.dom.events.EventException.
	 * @param messageArguments The argument list for the error message.
	 */
	public XMLEventException(final short code, final Object[] messageArguments) {
		//format a message based upon their error type
		super(code, ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME).getString(RESOURCE_PREFIX + ERROR_NAME_RESOURCE_ID) + ": "
				+ MessageFormat.format(ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE_NAME).getString(RESOURCE_PREFIX + code), messageArguments));
	}
}
