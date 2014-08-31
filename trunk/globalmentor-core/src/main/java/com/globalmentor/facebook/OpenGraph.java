/*
 * Copyright © 2011 GlobalMentor, Inc. <http://www.globalmentor.com/>
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

package com.globalmentor.facebook;

import java.net.URI;

/**
 * Definitions and utilities for working with Facebook's Open Graph protocol.
 * 
 * @author Garret Wilson
 * @see <a href="http://ogp.me/">The Open Graph Protocol</a>
 * @see <a href="https://developers.facebook.com/docs/opengraph/">Facebook Developers: Open Graph protocol</a>
 */
public class OpenGraph {

	/** The Open Graph namespace. */
	public static final URI NAMESPACE_URI = URI.create("http://ogp.me/ns#");
	/** The default prefix for the Open Graph namespace, e.g. in XML documents. */
	public static final String NAMESPACE_PREFIX = "og";

	/** The title of the object. (required) */
	public static final String TITLE_LOCAL_NAME = "title";
	/** The type of object, e.g. "movie". (required) */
	public static final String TYPE_LOCAL_NAME = "type";
	/** The URL of an image to represent the object. (required) */
	public static final String IMAGE_LOCAL_NAME = "image";
	/** The canonical URL of the object to be used as its permanent. (required) */
	public static final String URL_LOCAL_NAME = "url";
	/** A short description of the object. (optional) */
	public static final String DESCRIPTION_LOCAL_NAME = "description";
	/** The name of the overall site, if this object is part of a larger web site. (optional) */
	public static final String SITE_NAME_LOCAL_NAME = "site_name";

}
