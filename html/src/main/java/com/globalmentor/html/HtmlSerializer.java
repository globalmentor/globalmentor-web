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

import static com.globalmentor.html.spec.HTML.*;
import static com.globalmentor.java.Characters.SPACE_CHAR;

import java.io.IOException;

import javax.annotation.Nonnull;

import org.w3c.dom.*;

import com.globalmentor.xml.XMLSerializer;
import com.globalmentor.xml.XmlFormatProfile;
import com.globalmentor.xml.spec.NsName;

/**
 * Serializes a document as HTML. Has features to serialize features in an HTML-oriented way, such as void elements and empty attributes:
 * 
 * <pre>
 * {@code <button disabled/>}
 * </pre>
 * 
 * @author Garret Wilson
 */
public class HtmlSerializer extends XMLSerializer {

	/**
	 * The default HTML formatting profile, which uses categories specified by the HTML specification as well as some useful defaults.
	 * @implSpec This profile considers an element a block element if it is HTML5 <dfn>flow content</dfn> that is not <dfn>phrasing content</dfn>, or
	 *           <dfn>metadata content</dfn>.
	 * @implSpec This profile makes the direct child content of <code>&lt;html</code> flush with no indention.
	 */
	public static XmlFormatProfile DEFAULT_HTML_FORMAT_PROFILE = new BaseHtmlFormatProfile() {

		private final NsName flushElement = NsName.of(XHTML_NAMESPACE_URI_STRING, ELEMENT_HTML);

		@Override
		public boolean isFlush(final Element element) {
			return flushElement.matches(element);
		};

	};

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
	public void setUseEmptyAttributes(final boolean useEmptyAttributes) {
		this.useEmptyAttributes = useEmptyAttributes;
	}

	/** Default constructor for unformatted output with no XML prolog. */
	public HtmlSerializer() {
		this(false);
	}

	/**
	 * Constructor for an optionally formatted serializer with no XML prolog, using the {@link #DEFAULT_HTML_FORMAT_PROFILE} format profile.
	 * @param formatted Whether the serializer should be formatted.
	 */
	public HtmlSerializer(final boolean formatted) {
		this(formatted, DEFAULT_HTML_FORMAT_PROFILE);
	}

	/**
	 * Constructor for an optionally formatted serializer with no XML prolog.
	 * @param formatted Whether the serializer should be formatted.
	 * @param formatProfile The profile to use to guide formatting.
	 */
	public HtmlSerializer(final boolean formatted, @Nonnull final XmlFormatProfile formatProfile) {
		super(formatted, formatProfile);
		setPrologWritten(false);
	}

	/**
	 * {@inheritDoc}
	 * @implSpec This version only serializes elements using an empty element tag if the element is an HTML <dfn>void element</dfn>.
	 * @implNote This implementation results in an empty element tag for void elements even if the element has children, as HTML void elements are not allowed to
	 *           have children.
	 */
	@Override
	protected boolean isEmptyElementTag(final Element element) {
		final boolean isVoidElement = VOID_ELEMENTS.contains(NsName.ofNode(element));
		//TODO log a warning or throw an exception if a void element has children
		return isVoidElement;
	}

	/**
	 * {@inheritDoc}
	 * @implSpec This version will write an empty attribute if that option is enabled and the attribute value is the same as its name. For example:
	 *           <pre>{@code <button disabled/>}</pre>
	 * @see #isUseEmptyAttributes()
	 */
	@Override
	protected Appendable serializeAttribute(@Nonnull final Appendable appendable, @Nonnull final String attributeName, @Nonnull final String attributeValue)
			throws IOException {
		if(attributeValue.equals(attributeName)) { //if the attribute value is the same as its name TODO do we need to restrict this to some predefined list? 
			return appendable.append(SPACE_CHAR).append(attributeName); //append just the attribute name
		}
		return super.serializeAttribute(appendable, attributeName, attributeValue); //otherwise serialize the attribute normally as per XML
	}

	/**
	 * {@inheritDoc}
	 * @implSpec This implementation disables encoding for HTML {@code <script>} elements.
	 * @see <a href="https://www.w3.org/TR/html52/semantics-scripting.html#script-content-restrictions">HTML 5.2 § 4.12.1.3. Restrictions for contents of script
	 *      elements</a>
	 */
	@Override
	protected boolean isChildTextEncoded(final Node parentNode) {
		if(parentNode instanceof Element) {
			final Element parentElement = (Element)parentNode;
			if(XHTML_NAMESPACE_URI_STRING.equals(parentElement.getNamespaceURI()) && ELEMENT_SCRIPT.equals(parentElement.getLocalName())) {
				return false; //turn off encoding for <script>
			}
		}
		return super.isChildTextEncoded(parentNode);
	}

}
