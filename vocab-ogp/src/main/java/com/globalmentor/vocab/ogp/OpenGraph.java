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

package com.globalmentor.vocab.ogp;

import static java.util.Objects.*;

import java.net.URI;

import com.globalmentor.model.*;
import com.globalmentor.vocab.VocabularyTerm;

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
	public static final VocabularyTerm PROPERTY_TITLE = VocabularyTerm.of(NAMESPACE_URI, "title");
	/** The type of object, e.g. "movie". (required) */
	public static final VocabularyTerm PROPERTY_TYPE = VocabularyTerm.of(NAMESPACE_URI, "type");
	/** The URL of an image to represent the object. (required) */
	public static final VocabularyTerm PROPERTY_IMAGE = VocabularyTerm.of(NAMESPACE_URI, "image");
	/** The canonical URL of the object to be used as its permanent. (required) */
	public static final VocabularyTerm PROPERTY_URL = VocabularyTerm.of(NAMESPACE_URI, "url");
	/** A short description of the object. (optional) */
	public static final VocabularyTerm PROPERTY_DESCRIPTION = VocabularyTerm.of(NAMESPACE_URI, "description");
	/** The name of the overall site, if this object is part of a larger web site. (optional) */
	public static final VocabularyTerm PROPERTY_SITE_NAME = VocabularyTerm.of(NAMESPACE_URI, "site_name");

	/**
	 * A predefined category of Open Graph type.
	 * @author Garret Wilson
	 * @see <a href="http://ogp.me/">The Open Graph Protocol</a>
	 */
	public enum PredefinedCategory implements Labeled {
		/** The predefined OpenGraph category for activities. */
		ACTIVITIES("Activities"),
		/** The predefined OpenGraph category for businesses. */
		BUSINESSES("Businesses"),
		/** The predefined OpenGraph category for groups. */
		GROUPS("Groups"),
		/** The predefined OpenGraph category for organizations. */
		ORGANIZATIONS("Organizations"),
		/** The predefined OpenGraph category for people. */
		PEOPLE("People"),
		/** The predefined OpenGraph category for places. */
		PLACES("Places"),
		/** The predefined OpenGraph category for products and entertainment. */
		PRODUCTS_ENTERTAINMENT("Products and Entertainment"),
		/** The predefined OpenGraph category for websites. */
		WEBSITES("Websites");

		private final CharSequence label;

		/** {@inheritDoc} */
		public CharSequence getLabel() {
			return label;
		}

		PredefinedCategory(final CharSequence label) {
			this.label = requireNonNull(label);
		}

	}

	/**
	 * One of the types predefined in the Open Graph specification.
	 * @author Garret Wilson
	 * @see <a href="http://ogp.me/">The Open Graph Protocol</a>
	 */
	public enum PredefinedType implements IDed<String> {
		//note: new predefined types must also be added to a category within getCategory() or an assertion exception will be thrown

		/** The predefined OpenGraph type for activity. */
		ACTIVITY,
		/** The predefined OpenGraph type for sport. */
		SPORT,
		/** The predefined OpenGraph type for bar. */
		BAR,
		/** The predefined OpenGraph type for company. */
		COMPANY,
		/** The predefined OpenGraph type for cafe. */
		CAFE,
		/** The predefined OpenGraph type for hotel. */
		HOTEL,
		/** The predefined OpenGraph type for restaurant. */
		RESTAURANT,
		/** The predefined OpenGraph type for cause. */
		CAUSE,
		/** The predefined OpenGraph type for sports league. */
		SPORTS_LEAGUE,
		/** The predefined OpenGraph type for sports team. */
		SPORTS_TEAM,
		/** The predefined OpenGraph type for band. */
		BAND,
		/** The predefined OpenGraph type for government. */
		GOVERNMENT,
		/** The predefined OpenGraph type for non-profit. */
		NON_PROFIT,
		/** The predefined OpenGraph type for school. */
		SCHOOL,
		/** The predefined OpenGraph type for university. */
		UNIVERSITY,
		/** The predefined OpenGraph type for actor. */
		ACTOR,
		/** The predefined OpenGraph type for athlete. */
		ATHLETE,
		/** The predefined OpenGraph type for author. */
		AUTHOR,
		/** The predefined OpenGraph type for director. */
		DIRECTOR,
		/** The predefined OpenGraph type for musician. */
		MUSICIAN,
		/** The predefined OpenGraph type for politician. */
		POLITICIAN,
		/** The predefined OpenGraph type for profile. */
		PROFILE,
		/** The predefined OpenGraph type for public figure. */
		PUBLIC_FIGURE,
		/** The predefined OpenGraph type for city. */
		CITY,
		/** The predefined OpenGraph type for country. */
		COUNTRY,
		/** The predefined OpenGraph type for landmark. */
		LANDMARK,
		/** The predefined OpenGraph type for state province. */
		STATE_PROVINCE,
		/** The predefined OpenGraph type for album. */
		ALBUM,
		/** The predefined OpenGraph type for book. */
		BOOK,
		/** The predefined OpenGraph type for drink. */
		DRINK,
		/** The predefined OpenGraph type for food. */
		FOOD,
		/** The predefined OpenGraph type for game. */
		GAME,
		/** The predefined OpenGraph type for movie. */
		MOVIE,
		/** The predefined OpenGraph type for product. */
		PRODUCT,
		/** The predefined OpenGraph type for song. */
		SONG,
		/** The predefined OpenGraph type for TV show. */
		TV_SHOW,
		/** The predefined OpenGraph type for article. */
		ARTICLE,
		/** The predefined OpenGraph type for blog. */
		BLOG,
		/** The predefined OpenGraph type for web site. */
		WEBSITE;

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
