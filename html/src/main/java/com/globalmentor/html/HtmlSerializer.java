/*
 * Copyright © 2019 GlobalMentor, Inc. <http://www.globalmentor.com/>
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

package com.globalmentor.html;

import static com.globalmentor.java.Characters.SPACE_CHAR;

import java.io.IOException;

import javax.annotation.Nonnull;

import com.globalmentor.xml.XMLSerializer;

/**
 * Serializes a document as HTML. Has features to serialize features in an HTML-oriented way, such as empty attributes:
 * 
 * <pre>
 * {@code <button disabled/>}
 * </pre>
 * 
 * @author Garret Wilson
 */
public class HtmlSerializer extends XMLSerializer {

	/** Whether an attribute that has a value equal to its name should be serialized in empty attribute form. */
	public static final String OPTION_USE_EMPTY_ATTRIBUTES = "useEmptyAttributes"; //TODO use with property setting when integrated with Confound

	/** Default to serializing empty attributes when possible. */
	public static final boolean OPTION_USE_EMPTY_ATTRIBUTES_DEFAULT = true;

	private boolean useEmptyAttributes = OPTION_USE_EMPTY_ATTRIBUTES_DEFAULT;

	/** @return Whether to serialize an attribute in empty attribute form if possible. */
	public boolean isUseEmptyAttributes() {
		return useEmptyAttributes;
	}

	/**
	 * Sets whether an attribute that has a value equal to its name should be serialized in empty attribute form.
	 * @implSpec This option defaults to {@value #OPTION_USE_EMPTY_ATTRIBUTES_DEFAULT}.
	 * @param useEmptyAttributes <code>true</code> if HTML attributes should be used in serialization when possible.
	 */
	public void setUseEntities(final boolean useEmptyAttributes) {
		this.useEmptyAttributes = useEmptyAttributes;
	}

	/** Default constructor for unformatted output with no XML prolog. */
	public HtmlSerializer() {
		super();
		setPrologWritten(false);
	}

	/**
	 * Constructor for an optionally formatted serializer with no XML prolog.
	 * @param formatted Whether the serializer should be formatted.
	 */
	public HtmlSerializer(final boolean formatted) {
		this(); //do the default construction
		setFormatted(formatted); //set whether the output should be formatted
	}

	/**
	 * {@inheritDoc} @implSpec This version will write an empty attribute if that option is enabled and the attribute value is the same as its name. For example:
	 * 
	 * <pre>
	 * {@code <button disabled/>}
	 * </pre>
	 * 
	 * @see #isUseEmptyAttributes()
	 */
	@Override
	protected Appendable serializeAttribute(@Nonnull final Appendable appendable, @Nonnull final String attributeName, @Nonnull final String attributeValue)
			throws IOException {
		if(attributeValue.equals(attributeName)) { //if the attribute value is the same as its name TODO do we need to restrict this to some predefine list? 
			return appendable.append(SPACE_CHAR).append(attributeName); //append just the attribute name
		}
		return super.serializeAttribute(appendable, attributeName, attributeValue); //otherwise serialize the attribute normally as per XML
	}

}
