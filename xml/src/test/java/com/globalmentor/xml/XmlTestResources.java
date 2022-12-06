/*
 * Copyright © 2020 GlobalMentor, Inc. <https://www.globalmentor.com/>
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

/**
 * Definitions of useful test resources for XML.
 * 
 * @author Garret Wilson
 */
public class XmlTestResources {

	/**
	 * A modular XHTML 1.1 skeleton document. Note that this document uses <code>http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd</code> instead of
	 * <code>http://www.w3.org/MarkUp/DTD/xhtml11.dtd</code> for its DTD system ID. Currently some dependencies of this DTD are missing from the W3C site, so
	 * {@link DefaultEntityResolver} should be used for successful parsing. (Tests as well as code in general should use {@link DefaultEntityResolver} anyway;
	 * otherwise it might take 10 minutes to download all the related dependencies.)
	 * @see <a href="https://stackoverflow.com/q/60655704/421049">W3C breaks XHTML 1.1 parsing by removing modules from web site</a>
	 */
	public static final String XHTML_1_1_SKELETON = "xhtml-1.1-skeleton.xhtml";

}
