/*
 * Copyright © 2014 GlobalMentor, Inc. <http://www.globalmentor.com/>
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

package com.globalmentor.xml;

import static com.globalmentor.xml.XmlDom.*;
import static java.nio.charset.StandardCharsets.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.nio.charset.Charset;
import java.util.stream.Stream;

import javax.xml.parsers.DocumentBuilder;

import org.junit.jupiter.api.*;
import org.w3c.dom.*;

import com.globalmentor.io.ByteOrderMark;
import com.globalmentor.model.ObjectHolder;

/**
 * Tests of XML DOM utilities.
 * 
 * @author Garret Wilson
 */
public class XmlDomTest {

	/** @see XmlDom#detectXMLCharset(InputStream, ObjectHolder, ObjectHolder) */
	@Test
	public void testDetectISO_8859_1() throws IOException {
		try (final InputStream inputStream = new BufferedInputStream(getClass().getResourceAsStream("hello-world-iso-8859-1.xml"))) {
			final ObjectHolder<ByteOrderMark> bom = new ObjectHolder<>();
			final ObjectHolder<String> declaredEncodingName = new ObjectHolder<>();
			final Charset charset = XmlDom.detectXMLCharset(inputStream, bom, declaredEncodingName);
			assertThat(charset, is(ISO_8859_1));
			assertFalse(bom.isPresent());
			assertThat(declaredEncodingName.getObject(), is(ISO_8859_1.name()));
		}
	}

	/** @see XmlDom#detectXMLCharset(InputStream, ObjectHolder, ObjectHolder) */
	@Test
	public void testDetectUTF_8() throws IOException {
		try (final InputStream inputStream = new BufferedInputStream(getClass().getResourceAsStream("hello-world-utf-8.xml"))) {
			final ObjectHolder<ByteOrderMark> bom = new ObjectHolder<>();
			final ObjectHolder<String> declaredEncodingName = new ObjectHolder<>();
			final Charset charset = XmlDom.detectXMLCharset(inputStream, bom, declaredEncodingName);
			assertThat(charset, is(UTF_8));
			assertFalse(bom.isPresent());
			assertThat(declaredEncodingName.getObject(), is(UTF_8.name()));
		}
	}

	/** @see XmlDom#detectXMLCharset(InputStream, ObjectHolder, ObjectHolder) */
	@Test
	public void testDetectUTF_8BOM() throws IOException {
		try (final InputStream inputStream = new BufferedInputStream(getClass().getResourceAsStream("hello-world-utf-8-bom.xml"))) {
			final ObjectHolder<ByteOrderMark> bom = new ObjectHolder<>();
			final ObjectHolder<String> declaredEncodingName = new ObjectHolder<>();
			final Charset charset = XmlDom.detectXMLCharset(inputStream, bom, declaredEncodingName);
			assertThat(charset, is(UTF_8));
			assertThat(bom.getObject(), is(ByteOrderMark.UTF_8));
			assertThat(declaredEncodingName.getObject(), is(UTF_8.name()));
		}
	}

	/** @see XmlDom#detectXMLCharset(InputStream, ObjectHolder, ObjectHolder) */
	@Test
	public void testDetectUTF_8NoEcodingDeclaration() throws IOException {
		try (final InputStream inputStream = new BufferedInputStream(getClass().getResourceAsStream("hello-world-utf-8-no-encoding-declaration.xml"))) {
			final ObjectHolder<ByteOrderMark> bom = new ObjectHolder<>();
			final ObjectHolder<String> declaredEncodingName = new ObjectHolder<>();
			final Charset charset = XmlDom.detectXMLCharset(inputStream, bom, declaredEncodingName);
			assertThat(charset, is(UTF_8));
			assertFalse(bom.isPresent());
			assertFalse(declaredEncodingName.isPresent());
		}
	}

	/** @see XmlDom#detectXMLCharset(InputStream, ObjectHolder, ObjectHolder) */
	@Test
	public void testDetectNothing() throws IOException {
		try (final InputStream inputStream = new BufferedInputStream(getClass().getResourceAsStream("hello-world-utf-8-no-xml-declaration.xml"))) {
			final ObjectHolder<ByteOrderMark> bom = new ObjectHolder<>();
			final ObjectHolder<String> declaredEncodingName = new ObjectHolder<>();
			final Charset charset = XmlDom.detectXMLCharset(inputStream, bom, declaredEncodingName);
			assertThat(charset, is(nullValue()));
			assertFalse(bom.isPresent());
			assertFalse(declaredEncodingName.isPresent());
		}
	}

	/** @see XmlDom#detectXMLCharset(InputStream, ObjectHolder, ObjectHolder) */
	@Test
	public void testDetectUTF_16LE() throws IOException {
		try (final InputStream inputStream = new BufferedInputStream(getClass().getResourceAsStream("hello-world-utf-16le.xml"))) {
			final ObjectHolder<ByteOrderMark> bom = new ObjectHolder<>();
			final ObjectHolder<String> declaredEncodingName = new ObjectHolder<>();
			final Charset charset = XmlDom.detectXMLCharset(inputStream, bom, declaredEncodingName);
			assertThat(charset, is(UTF_16LE));
			assertFalse(bom.isPresent());
			assertThat(declaredEncodingName.getObject(), is(UTF_16.name()));
		}
	}

	/** @see XmlDom#detectXMLCharset(InputStream, ObjectHolder, ObjectHolder) */
	@Test
	public void testDetectUTF_16LE_BOM() throws IOException {
		try (final InputStream inputStream = new BufferedInputStream(getClass().getResourceAsStream("hello-world-utf-16le-bom.xml"))) {
			final ObjectHolder<ByteOrderMark> bom = new ObjectHolder<>();
			final ObjectHolder<String> declaredEncodingName = new ObjectHolder<>();
			final Charset charset = XmlDom.detectXMLCharset(inputStream, bom, declaredEncodingName);
			assertThat(charset, is(UTF_16LE));
			assertThat(bom.getObject(), is(ByteOrderMark.UTF_16LE));
			assertThat(declaredEncodingName.getObject(), is(UTF_16.name()));
		}
	}

	/**
	 * @see XmlDom#mergeAttributesNS(Element, Element)
	 * @see XmlDom#mergeAttributesNS(Element, Stream)
	 */
	@Test
	public void testMergeAttributesNS() {
		final String FOOBAR_NS_URI_STRING = "http://foo.bar/";
		final String EXAMPLE_NS_URI_STRING = "http://example.com/";
		final DocumentBuilder documentBuilder = createDocumentBuilder(true);
		final DOMImplementation domImplementation = documentBuilder.getDOMImplementation();

		final Document document1 = domImplementation.createDocument(FOOBAR_NS_URI_STRING, "foobar", null);
		final Element element1 = document1.getDocumentElement();
		element1.setAttributeNS(null, "old", "123"); //existing
		element1.setAttributeNS(null, "test", "foo"); //to be replaced
		element1.setAttributeNS(EXAMPLE_NS_URI_STRING, "ex1:value", "same");
		element1.setAttributeNS(EXAMPLE_NS_URI_STRING, "ex1:change", "before");

		final Document document2 = domImplementation.createDocument(FOOBAR_NS_URI_STRING, "foobar", null);
		final Element element2 = document2.getDocumentElement();
		element2.setAttributeNS(null, "new", "456");
		element2.setAttributeNS(null, "test", "bar"); //to replace
		element2.setAttributeNS(EXAMPLE_NS_URI_STRING, "ex2:value", "same");
		element2.setAttributeNS(EXAMPLE_NS_URI_STRING, "ex2:change", "after");
		element2.setAttributeNS(EXAMPLE_NS_URI_STRING, "new:one", "two");

		XmlDom.mergeAttributesNS(element1, element2);

		assertThat(element1.getAttributes().getLength(), is(6));
		assertThat(element1.getAttributeNS(null, "old"), is("123"));
		assertThat(element1.getAttributeNS(null, "test"), is("bar"));
		assertThat(element1.getAttributeNS(EXAMPLE_NS_URI_STRING, "value"), is("same"));
		assertThat(element1.getAttributeNodeNS(EXAMPLE_NS_URI_STRING, "value").getName(), is("ex2:value"));
		assertThat(element1.getAttributeNS(EXAMPLE_NS_URI_STRING, "change"), is("after"));
		assertThat(element1.getAttributeNodeNS(EXAMPLE_NS_URI_STRING, "change").getName(), is("ex2:change"));
		assertThat(element1.getAttributeNS(EXAMPLE_NS_URI_STRING, "one"), is("two"));
		assertThat(element1.getAttributeNodeNS(EXAMPLE_NS_URI_STRING, "one").getName(), is("new:one"));

		assertThat(element2.getAttributes().getLength(), is(5));
		assertThat(element2.getAttributeNS(null, "new"), is("456"));
		assertThat(element2.getAttributeNS(null, "test"), is("bar"));
		assertThat(element2.getAttributeNS(EXAMPLE_NS_URI_STRING, "value"), is("same"));
		assertThat(element2.getAttributeNodeNS(EXAMPLE_NS_URI_STRING, "value").getName(), is("ex2:value"));
		assertThat(element2.getAttributeNS(EXAMPLE_NS_URI_STRING, "change"), is("after"));
		assertThat(element2.getAttributeNodeNS(EXAMPLE_NS_URI_STRING, "change").getName(), is("ex2:change"));
		assertThat(element2.getAttributeNS(EXAMPLE_NS_URI_STRING, "one"), is("two"));
		assertThat(element2.getAttributeNodeNS(EXAMPLE_NS_URI_STRING, "one").getName(), is("new:one"));
	}

}
