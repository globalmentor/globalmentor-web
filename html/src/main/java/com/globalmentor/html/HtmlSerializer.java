/*
 * Copyright © 2019 GlobalMentor, Inc. <https://www.globalmentor.com/>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.globalmentor.html;

import static com.globalmentor.html.def.HTML.*;
import static com.globalmentor.java.Characters.SPACE_CHAR;
import static com.globalmentor.xml.XmlDom.*;

import java.io.IOException;

import javax.annotation.Nonnull;

import org.w3c.dom.*;

import com.globalmentor.html.def.HTML;
import com.globalmentor.text.ASCII;
import com.globalmentor.xml.XMLSerializer;
import com.globalmentor.xml.XmlFormatProfile;
import com.globalmentor.xml.def.NsName;
import com.globalmentor.xml.def.XML;

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
	 * Constructor for an optionally formatted serializer with no XML prolog, using the {@link DefaultHtmlFormatProfile#INSTANCE} format profile.
	 * @param formatted Whether the serializer should be formatted.
	 */
	public HtmlSerializer(final boolean formatted) {
		this(formatted, DefaultHtmlFormatProfile.INSTANCE);
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
	 * @implSpec This version only serializes elements using an empty element tag if one of the HTML specifications forbids the use of an ending tag; this
	 *           includes HTML5 <dfn>void elements</dfn> but also obsolete HTML 4.0.1 elements forbidden from having end tags such as {@code <frame>}.
	 * @implNote This implementation results in an empty element tag for an HTML element forbidden to have children even if the element does in fact have
	 *           children.
	 */
	@Override
	protected boolean isEmptyElementTag(final Element element) {
		final boolean isEmptyElement = EMPTY_ELEMENTS.contains(NsName.ofNode(element));
		//TODO log a warning or throw an exception if a void element has children
		return isEmptyElement;
	}

	/**
	 * {@inheritDoc}
	 * @implSpec This version ignores the <code>xml:lang</code> attribute if a <code>lang</code> attribute is present with the same value; otherwise if no
	 *           <code>lang</code> attribute is present, <code>xml:lang</code> is serialized as <code>lang</code>.
	 * @implSpec This version ignores the <code>xml:space</code> attribute.
	 * @see <a href="https://www.w3.org/TR/html52/dom.html#the-lang-and-xmllang-attributes">HTML 5.2 § 3.2.5.2. The lang and xml:lang attributes</a>
	 * @see <a href="https://www.w3.org/TR/html52/dom.html#global-attributes">HTML 5.2 § 3.2.5. Global attributes</a>
	 */
	@Override
	protected Appendable serializeAttribute(final Appendable appendable, final Element element, final Attr attribute) throws IOException {
		if(XML.ATTRIBUTE_LANG.matches(attribute)) { //convert `xml:lang` to `lang` as appropriate
			final String attributeValue = attribute.getValue();
			final String htmlLang = findAttributeNS(element, null, HTML.ATTRIBUTE_LANG).orElse(null);
			if(htmlLang != null) {
				//if the HTML `lang` attribute is present with an equivalent value, don't serialize `xml:lang` at all 
				if(ASCII.equalsIgnoreCase(htmlLang, attributeValue)) {
					return appendable;
				}
				//TODO log a warning or throw an exception indicating that the xml:lang value is different than the HTML value
			} else { //if there is no HTML `lang` attribute, convert `xml:lang` to HTML `lang`
				return serializeAttribute(appendable, HTML.ATTRIBUTE_LANG, attributeValue);
			}
		} else if(XML.ATTRIBUTE_SPACE.matches(attribute)) { //ignore `xml:space` altogether
			return appendable;
		}
		return super.serializeAttribute(appendable, element, attribute);
	}

	/**
	 * {@inheritDoc}
	 * @implSpec This version will write an empty attribute if that option is enabled and the attribute value is the same as its name. For example:
	 *           <pre>{@code <button disabled/>}</pre>
	 * @see #isUseEmptyAttributes()
	 */
	@Override
	protected Appendable serializeAttribute(final Appendable appendable, final String attributeName, final String attributeValue) throws IOException {
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
