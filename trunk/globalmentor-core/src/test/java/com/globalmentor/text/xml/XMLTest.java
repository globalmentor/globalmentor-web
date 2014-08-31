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

package com.globalmentor.text.xml;

import static java.nio.charset.StandardCharsets.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.*;

import java.io.*;
import java.nio.charset.Charset;

import org.junit.Test;

import com.globalmentor.io.ByteOrderMark;
import com.globalmentor.model.ObjectHolder;
import com.globalmentor.test.AbstractTest;

/**
 * Tests of XML processing.
 * 
 * @author Garret Wilson
 * @see XMLProcessor
 *
 */
public class XMLTest extends AbstractTest {

	/** @see XML#detectXMLCharset(InputStream, ObjectHolder, ObjectHolder) */
	@Test
	public void testDetectISO_8859_1() throws IOException {
		final InputStream inputStream = new BufferedInputStream(getClass().getResourceAsStream("hello-world-iso-8859-1.xml"));
		try {
			final ObjectHolder<ByteOrderMark> bom = new ObjectHolder<>();
			final ObjectHolder<String> declaredEncodingName = new ObjectHolder<>();
			final Charset charset = XML.detectXMLCharset(inputStream, bom, declaredEncodingName);
			assertThat(charset, is(ISO_8859_1));
			assertFalse(bom.isPresent());
			assertThat(declaredEncodingName.getObject(), is(ISO_8859_1.name()));
		} finally {
			inputStream.close();
		}
	}

	/** @see XML#detectXMLCharset(InputStream, ObjectHolder, ObjectHolder) */
	@Test
	public void testDetectUTF_8() throws IOException {
		final InputStream inputStream = new BufferedInputStream(getClass().getResourceAsStream("hello-world-utf-8.xml"));
		try {
			final ObjectHolder<ByteOrderMark> bom = new ObjectHolder<>();
			final ObjectHolder<String> declaredEncodingName = new ObjectHolder<>();
			final Charset charset = XML.detectXMLCharset(inputStream, bom, declaredEncodingName);
			assertThat(charset, is(UTF_8));
			assertFalse(bom.isPresent());
			assertThat(declaredEncodingName.getObject(), is(UTF_8.name()));
		} finally {
			inputStream.close();
		}
	}

	/** @see XML#detectXMLCharset(InputStream, ObjectHolder, ObjectHolder) */
	@Test
	public void testDetectUTF_8BOM() throws IOException {
		final InputStream inputStream = new BufferedInputStream(getClass().getResourceAsStream("hello-world-utf-8-bom.xml"));
		try {
			final ObjectHolder<ByteOrderMark> bom = new ObjectHolder<>();
			final ObjectHolder<String> declaredEncodingName = new ObjectHolder<>();
			final Charset charset = XML.detectXMLCharset(inputStream, bom, declaredEncodingName);
			assertThat(charset, is(UTF_8));
			assertThat(bom.getObject(), is(ByteOrderMark.UTF_8));
			assertThat(declaredEncodingName.getObject(), is(UTF_8.name()));
		} finally {
			inputStream.close();
		}
	}

	/** @see XML#detectXMLCharset(InputStream, ObjectHolder, ObjectHolder) */
	@Test
	public void testDetectUTF_8NoEcodingDeclaration() throws IOException {
		final InputStream inputStream = new BufferedInputStream(getClass().getResourceAsStream("hello-world-utf-8-no-encoding-declaration.xml"));
		try {
			final ObjectHolder<ByteOrderMark> bom = new ObjectHolder<>();
			final ObjectHolder<String> declaredEncodingName = new ObjectHolder<>();
			final Charset charset = XML.detectXMLCharset(inputStream, bom, declaredEncodingName);
			assertThat(charset, is(UTF_8));
			assertFalse(bom.isPresent());
			assertFalse(declaredEncodingName.isPresent());
		} finally {
			inputStream.close();
		}
	}

	/** @see XML#detectXMLCharset(InputStream, ObjectHolder, ObjectHolder) */
	@Test
	public void testDetectNothing() throws IOException {
		final InputStream inputStream = new BufferedInputStream(getClass().getResourceAsStream("hello-world-utf-8-no-xml-declaration.xml"));
		try {
			final ObjectHolder<ByteOrderMark> bom = new ObjectHolder<>();
			final ObjectHolder<String> declaredEncodingName = new ObjectHolder<>();
			final Charset charset = XML.detectXMLCharset(inputStream, bom, declaredEncodingName);
			assertThat(charset, is(nullValue()));
			assertFalse(bom.isPresent());
			assertFalse(declaredEncodingName.isPresent());
		} finally {
			inputStream.close();
		}
	}

	/** @see XML#detectXMLCharset(InputStream, ObjectHolder, ObjectHolder) */
	@Test
	public void testDetectUTF_16LE() throws IOException {
		final InputStream inputStream = new BufferedInputStream(getClass().getResourceAsStream("hello-world-utf-16le.xml"));
		try {
			final ObjectHolder<ByteOrderMark> bom = new ObjectHolder<>();
			final ObjectHolder<String> declaredEncodingName = new ObjectHolder<>();
			final Charset charset = XML.detectXMLCharset(inputStream, bom, declaredEncodingName);
			assertThat(charset, is(UTF_16LE));
			assertFalse(bom.isPresent());
			assertThat(declaredEncodingName.getObject(), is(UTF_16.name()));
		} finally {
			inputStream.close();
		}
	}

	/** @see XML#detectXMLCharset(InputStream, ObjectHolder, ObjectHolder) */
	@Test
	public void testDetectUTF_16LE_BOM() throws IOException {
		final InputStream inputStream = new BufferedInputStream(getClass().getResourceAsStream("hello-world-utf-16le-bom.xml"));
		try {
			final ObjectHolder<ByteOrderMark> bom = new ObjectHolder<>();
			final ObjectHolder<String> declaredEncodingName = new ObjectHolder<>();
			final Charset charset = XML.detectXMLCharset(inputStream, bom, declaredEncodingName);
			assertThat(charset, is(UTF_16LE));
			assertThat(bom.getObject(), is(ByteOrderMark.UTF_16LE));
			assertThat(declaredEncodingName.getObject(), is(UTF_16.name()));
		} finally {
			inputStream.close();
		}
	}
}
