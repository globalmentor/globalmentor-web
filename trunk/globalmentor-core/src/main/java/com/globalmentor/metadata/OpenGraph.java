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

package com.globalmentor.metadata;

import static com.globalmentor.java.Objects.*;

import java.net.URI;

import com.globalmentor.model.*;

/**
 * Definitions of Facebook's Open Graph protocol.
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

	/**
	 * A predefined category of Open Graph type.
	 * @author Garret Wilson
	 * @see <a href="http://ogp.me/">The Open Graph Protocol</a>
	 */
	public enum PredefinedCategory implements Labeled {
		ACTIVITIES("Activities"), BUSINESSES("Businesses"), GROUPS("Groups"), ORGANIZATIONS("Organizations"), PEOPLE("People"), PLACES("Places"), PRODUCTS_ENTERTAINMENT(
				"Products and Entertainment"), WEBSITES("Websites");

		private final CharSequence label;

		/** {@inheritDoc} */
		public CharSequence getLabel() {
			return label;
		}

		PredefinedCategory(final CharSequence label) {
			this.label = checkInstance(label);
		}

	}

	/**
	 * One of the types predefined in the Open Graph specification.
	 * @author Garret Wilson
	 * @see <a href="http://ogp.me/">The Open Graph Protocol</a>
	 */
	public enum PredefinedType implements IDed<String> {
		//note: new predefined types must also be added to a category within getCategory() or an assertion exception will be thrown

		ACTIVITY, SPORT, BAR, COMPANY, CAFE, HOTEL, RESTAURANT, CAUSE, SPORTS_LEAGUE, SPORTS_TEAM, BAND, GOVERNMENT, NON_PROFIT, SCHOOL, UNIVERSITY, ACTOR, ATHLETE, AUTHOR, DIRECTOR, MUSICIAN, POLITICIAN, PROFILE, PUBLIC_FIGURE, CITY, COUNTRY, LANDMARK, STATE_PROVINCE, ALBUM, BOOK, DRINK, FOOD, GAME, MOVIE, PRODUCT, SONG, TV_SHOW, ARTICLE, BLOG, WEBSITE;

		/** @return The predefined category of this predefined type. */
		public PredefinedCategory getCategory() {
			switch(this) {
				case ACTIVITY:
				case SPORT:
					return PredefinedCategory.ACTIVITIES;
				case BAR:
				case COMPANY:
				case CAFE:
				case HOTEL:
				case RESTAURANT:
					return PredefinedCategory.BUSINESSES;
				case CAUSE:
				case SPORTS_LEAGUE:
				case SPORTS_TEAM:
					return PredefinedCategory.GROUPS;
				case BAND:
				case GOVERNMENT:
				case NON_PROFIT:
				case SCHOOL:
				case UNIVERSITY:
					return PredefinedCategory.ORGANIZATIONS;
				case ACTOR:
				case ATHLETE:
				case AUTHOR:
				case DIRECTOR:
				case MUSICIAN:
				case POLITICIAN:
				case PROFILE:
				case PUBLIC_FIGURE:
					return PredefinedCategory.PEOPLE;
				case CITY:
				case COUNTRY:
				case LANDMARK:
				case STATE_PROVINCE:
					return PredefinedCategory.PLACES;
				case ALBUM:
				case BOOK:
				case DRINK:
				case FOOD:
				case GAME:
				case MOVIE:
				case PRODUCT:
				case SONG:
				case TV_SHOW:
					return PredefinedCategory.PRODUCTS_ENTERTAINMENT;
				case ARTICLE:
				case BLOG:
				case WEBSITE:
					return PredefinedCategory.WEBSITES;
				default:
					throw new AssertionError("No category defined for type " + this);
			}
		}

		/**
		 * {@inheritDoc}
		 * <p>
		 * This ID is the official ID given by the Open Graph specification and suitable for serialization.
		 * </p>
		 */
		public String getID() {
			return toString().toLowerCase(); //all the type IDs are simply the lowercase version of the enum string
		}
	}

}
