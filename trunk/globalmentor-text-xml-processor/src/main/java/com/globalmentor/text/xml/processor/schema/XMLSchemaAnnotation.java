/*
 * Copyright © 1996-2008 GlobalMentor, Inc. <http://www.globalmentor.com/>
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

package com.globalmentor.text.xml.processor.schema;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * An annotation to a primary or secondary schema component.
 * @author Garret Wilson
 * @deprecated
 */
public class XMLSchemaAnnotation extends XMLSchemaHelperComponent {

	/** The list of annotation elements. */
	private List elementList = new ArrayList();

	/**
	 * @return The list of annotation elements, each of which should either be application information or documentation.
	 * @see #ApplicationInformation
	 * @see #UserInformation
	 */
	public List getElementList() {
		return elementList;
	}

	/** Defualt constructor. */
	public XMLSchemaAnnotation() {
		super(ANNOTATION_COMPONENT); //construct the parent class
	}

	/**
	 * Adds user information to the annotation.
	 * @param userInfo The text of the user information.
	 */
	public void addUserInformation(final String userInfo) {
		addUserInformation(new UserInformation(userInfo)); //create an object with the correct information and add it
	}

	/**
	 * Adds user information to the annotation.
	 * @param userInfo The object containing the user information.
	 */
	public void addUserInformation(final UserInformation userInfo) {
		getElementList().add(userInfo); //add the user information to the list
	}

	/**
	 * @return A list of all user information elements (if any), in the correct order. The list cannot be modified.
	 * @see #getElementList
	 * @see #UserInformation
	 */
	public List getUserInformation() {
		final List userInfoList = new ArrayList(); //create a new list to hold the elements
		final Iterator iterator = getElementList().iterator(); //get an iterator of all elements
		while(iterator.hasNext()) { //while there are more elements
			final Object annotationElement = iterator.next(); //get the next annotation element
			if(annotationElement instanceof UserInformation) //if this is user information
				userInfoList.add(annotationElement); //add this element to our list
		}
		return Collections.unmodifiableList(userInfoList); //return the list we created
	}

	/**
	 * Adds application information to the annotation.
	 * @param appInfo The text of the application information.
	 */
	public void addApplicationInformation(final String applicationInfo) {
		addApplicationInformation(new ApplicationInformation(applicationInfo)); //create an object with the correct information and add it
	}

	/**
	 * Adds application information to the annotation.
	 * @param applicationInfo The object containing the application information.
	 */
	public void addApplicationInformation(final ApplicationInformation applicationInfo) {
		getElementList().add(applicationInfo); //add the application information to the list
	}

	/**
	 * @return A list of all application information elements (if any), in the correct order. The list cannot be modified.
	 * @see #getElementList
	 * @see #ApplicationInformation
	 */
	public List getApplicationInformation() {
		final List appInfoList = new ArrayList(); //create a new list to hold the elements
		final Iterator iterator = getElementList().iterator(); //get an iterator of all elements
		while(iterator.hasNext()) { //while there are more elements
			final Object annotationElement = iterator.next(); //get the next annotation element
			if(annotationElement instanceof ApplicationInformation) //if this is application information
				appInfoList.add(annotationElement); //add this element to our list
		}
		return Collections.unmodifiableList(appInfoList); //return the list we created
	}

	/** Internal class for holding application information. */
	public static class ApplicationInformation {

		/** The application information. */
		private String appInfo = "";

		/**
		 * Constructor which accepts a string of application information.
		 * @param newAppInfo The application information in string format.
		 */
		public ApplicationInformation(final String newAppInfo) {
			appInfo = newAppInfo;
		}

		/** @return The string representation of the information. */
		public String toString() {
			return appInfo;
		}

	}

	/** Internal class for holding user information. */
	public static class UserInformation {

		/** The user information. */
		private String userInfo = "";

		/**
		 * Constructor which accepts a string of user information.
		 * @param newUserInfo The user information in string format.
		 */
		public UserInformation(final String newUserInfo) {
			userInfo = newUserInfo;
		}

		/** @return The string representation of the information. */
		public String toString() {
			return userInfo;
		}

	}

}