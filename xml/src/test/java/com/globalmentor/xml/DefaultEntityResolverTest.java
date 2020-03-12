/*
 * Copyright © 2020 GlobalMentor, Inc. <http://www.globalmentor.com/>
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

import static com.globalmentor.xml.XmlTestResources.*;

import java.io.*;

import javax.xml.parsers.*;

import org.junit.jupiter.api.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

/**
 * Tests of {@link DefaultEntityResolver}.
 * 
 * @author Garret Wilson
 */
public class DefaultEntityResolverTest {

	/**
	 * Confirms the ability to parse an XHTML 1.1 modularized document using {@link DefaultEntityResolver} without exceptions, even though some of the entities
	 * may longer be present at their original URLs.
	 * @implSpec This test uses the {@value XmlTestResources#XHTML_1_1_SKELETON} test resource.
	 * @implNote Strange behavior will occur of {@link Document#normalizeDocument()} is called after loading document in some situations: the entity resolver will
	 *           never be accessed, and an error message will be printed. Although this behavior does not occur in this test, even if
	 *           <code>document.normalizeDocument()</code> is invoked, if this parsing and documentation normalization is placed in a loop it may crash the JVM.
	 *           See additional <a href="https://stackoverflow.com/q/60592749/421049">description and discussion on Stack Overflow</a>.
	 */
	@Test
	public void testParseXhtml11() throws IOException, ParserConfigurationException, SAXException {
		final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		documentBuilderFactory.setNamespaceAware(true);
		final DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		documentBuilder.setEntityResolver(DefaultEntityResolver.getInstance()); //load XHTML entities as local resources
		final Document document;
		try (final InputStream inputStream = new BufferedInputStream(getClass().getResourceAsStream(XHTML_1_1_SKELETON))) {
			document = documentBuilder.parse(inputStream);
		}
		document.normalize(); //see note in documentation about calling `document.normalizeDocument()` instead
	}

}
