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

package com.globalmentor.xml;

import java.util.List;

import javax.annotation.*;

import org.w3c.dom.*;

import com.globalmentor.java.Characters;
import com.globalmentor.xml.def.NsName;

/**
 * Specifies a strategy for characterizing an XML document for XML serialization by indicating how different elements are classified for formatting.
 * @apiNote This interface does not provide settings for actual formatting, such as the amount of indentation, but instead helps the formatter decide which
 *          things are to be formatted as what.
 * @author Garret Wilson
 */
public interface XmlFormatProfile {

	/**
	 * Returns the characters considered "space characters" for normalizations.
	 * @apiNote This typically includes actual space characters and tabs, but usually should <em>not</em> include all Unicode characters specified as
	 *          "whitespace".
	 * @return The characters this profile considers "spaces" for the purpose of normalization.
	 */
	public Characters getSpaceNormalizationCharacters();

	/**
	 * Indicates whether the given element is considered block element for purposes of formatting.
	 * @param element A DOM element.
	 * @return <code>true</code> if the element should be formatted as a block element.
	 */
	public boolean isBlock(@Nonnull final Element element);

	/**
	 * Indicates whether the given element is considered a break element for purposes of formatting. A <dfn>break</dfn> element is one that results in a line
	 * break after the element, even though the element may not be a block element.
	 * @param element A DOM element.
	 * @return <code>true</code> if the element should be formatted as a break element.
	 */
	public boolean isBreak(@Nonnull final Element element);

	/**
	 * Indicates whether first-level children are never indented. The first-level children of flush elements, if they result in newlines (such as block children),
	 * are formatted flush with the given element—that is, at the same indention level rather than indented an additional level.
	 * @apiNote Typically a flush element will also be a block element and/or the root, document element.
	 * @param element A DOM element.
	 * @return return <code>true</code> if any non-inline children should be indented at the same level as the given element.
	 */
	public boolean isFlush(@Nonnull final Element element);

	/**
	 * Indicates whether the given element retains the format of its content. That is, the content should not be formatted independent of any formatting setting.
	 * If an element preserves formatting, this setting is applied to all its children.
	 * @apiNote These elements may still have their attributes reformatted.
	 * @apiNote XML elements with the attribute <code>xml:space</code> set to <code>preserve</code> and the HTML <code>&lt;pre&gt;</code> element are examples of
	 *          elements that typically retain their formatting.
	 * @param element A DOM element.
	 * @return return <code>true</code> if the element should retain its content formatting and should therefore not be formatted.
	 */
	public boolean isPreserved(@Nonnull final Element element);

	/**
	 * Determines the order of known attributes for an element.
	 * @apiNote For example, an HTML element may return the attributes <code>id</code> and <code>name</code> to ensure that these always are sorted first (after
	 *          any attributes the serializer decides should be first), in that order.
	 * @param element The element for which attribute order should be specified.
	 * @return An list of possible known attribute for the given element indicating serialization order.
	 */
	public List<NsName> getAttributeOrder(@Nonnull final Element element);

}
