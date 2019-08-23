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

import static com.globalmentor.html.HtmlDom.*;
import static com.globalmentor.html.spec.HTML.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.Set;

import org.junit.jupiter.api.*;
import org.w3c.dom.*;

/**
 * Tests of HTML DOM utilities.
 * 
 * @author Garret Wilson
 */
public class HtmlDomTest {

	/** @see HtmlDom#mergeAttributes(Element, Element) */
	@Test
	public void testMergeAttributesNS() {
		final String EXAMPLE_NS_URI_STRING = "http://example.com/";
		final String OTHER_CLASS_WITH_EXTRA_SPACES = " four  five  ";

		final Document document1 = createXHTMLDocument("Document1");
		final Element element1 = findHtmlBodyElement(document1).orElseThrow(AssertionError::new);
		element1.setAttributeNS(null, "old", "123"); //existing
		element1.setAttributeNS(null, "test", "foo"); //to be replaced
		element1.setAttributeNS(null, "class", "one two three"); //HTML class
		element1.setAttributeNS(EXAMPLE_NS_URI_STRING, "ex1:value", "same");
		element1.setAttributeNS(EXAMPLE_NS_URI_STRING, "ex1:change", "before");

		final Document document2 = createXHTMLDocument("Document2");
		final Element element2 = findHtmlBodyElement(document2).orElseThrow(AssertionError::new);
		element2.setAttributeNS(null, "new", "456");
		element2.setAttributeNS(null, "test", "bar"); //to replace
		element2.setAttributeNS(null, "class", OTHER_CLASS_WITH_EXTRA_SPACES); //HTML class
		element2.setAttributeNS(EXAMPLE_NS_URI_STRING, "ex2:value", "same");
		element2.setAttributeNS(EXAMPLE_NS_URI_STRING, "ex2:change", "after");
		element2.setAttributeNS(EXAMPLE_NS_URI_STRING, "new:one", "two");

		HtmlDom.mergeAttributes(element1, element2);

		assertThat(element1.getAttributes().getLength(), is(7));
		assertThat(element1.getAttributeNS(null, "old"), is("123"));
		assertThat(element1.getAttributeNS(null, "test"), is("bar"));
		assertThat(getClasses(element1), is(Set.of("one", "two", "three", "four", "five")));
		assertThat(element1.getAttributeNS(null, "test"), is("bar"));
		assertThat(element1.getAttributeNS(EXAMPLE_NS_URI_STRING, "value"), is("same"));
		assertThat(element1.getAttributeNodeNS(EXAMPLE_NS_URI_STRING, "value").getName(), is("ex2:value"));
		assertThat(element1.getAttributeNS(EXAMPLE_NS_URI_STRING, "change"), is("after"));
		assertThat(element1.getAttributeNodeNS(EXAMPLE_NS_URI_STRING, "change").getName(), is("ex2:change"));
		assertThat(element1.getAttributeNS(EXAMPLE_NS_URI_STRING, "one"), is("two"));
		assertThat(element1.getAttributeNodeNS(EXAMPLE_NS_URI_STRING, "one").getName(), is("new:one"));

		assertThat(element2.getAttributes().getLength(), is(6));
		assertThat(element2.getAttributeNS(null, "new"), is("456"));
		assertThat(element2.getAttributeNS(null, "test"), is("bar"));
		assertThat(element2.getAttributeNS(null, "class"), is(OTHER_CLASS_WITH_EXTRA_SPACES));
		assertThat(element2.getAttributeNS(EXAMPLE_NS_URI_STRING, "value"), is("same"));
		assertThat(element2.getAttributeNodeNS(EXAMPLE_NS_URI_STRING, "value").getName(), is("ex2:value"));
		assertThat(element2.getAttributeNS(EXAMPLE_NS_URI_STRING, "change"), is("after"));
		assertThat(element2.getAttributeNodeNS(EXAMPLE_NS_URI_STRING, "change").getName(), is("ex2:change"));
		assertThat(element2.getAttributeNS(EXAMPLE_NS_URI_STRING, "one"), is("two"));
		assertThat(element2.getAttributeNodeNS(EXAMPLE_NS_URI_STRING, "one").getName(), is("new:one"));

		//test merge when other element has no class
		element2.removeAttributeNS(null, ATTRIBUTE_CLASS);
		HtmlDom.mergeAttributes(element1, element2);
		assertThat(getClasses(element1), is(Set.of("one", "two", "three", "four", "five")));

		//test merge when only other element has class
		element1.removeAttributeNS(null, ATTRIBUTE_CLASS);
		element2.setAttributeNS(null, "class", "six seven eight"); //HTML class
		HtmlDom.mergeAttributes(element1, element2);
		assertThat(getClasses(element1), is(Set.of("six", "seven", "eight")));

		//test merge when neither element has class
		element1.removeAttributeNS(null, ATTRIBUTE_CLASS);
		element2.removeAttributeNS(null, ATTRIBUTE_CLASS);
		HtmlDom.mergeAttributes(element1, element2);
		assertThat(getClasses(element1), is(empty()));

		//test that remaining attributes are unaffected
		assertThat(element1.getAttributes().getLength(), is(6));
		assertThat(element1.getAttributeNS(null, "old"), is("123"));
		assertThat(element1.getAttributeNS(null, "test"), is("bar"));
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
