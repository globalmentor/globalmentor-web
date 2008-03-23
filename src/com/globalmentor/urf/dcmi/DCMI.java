package com.globalmentor.urf.dcmi;

import java.net.URI;
import java.util.Locale;

import com.globalmentor.urf.*;

import static com.globalmentor.urf.URF.*;

/**Constants and methods used for Dublin Core as stored in URF.
<p>Copyright © 2007 GlobalMentor, Inc.
This source code can be freely used for any purpose, as long as the following conditions are met.
Any object code derived from this source code must include the following text to users using along with other "about" notifications:
"Uniform Resource Framework (URF) &lt;http://www.urf.name/&gt; specification and processing
written by Garret Wilson &lt;http://www.garretwilson.com/&gt; and Copyright © 2007 GlobalMentor, Inc. &lt;http://www.globalmentor.com/&gt;."
Any redistribution of this source code or derived source code must include these comments unmodified.</p>
@author Garret Wilson
@see <a href="http://dublincore.org/documents/dcmi-namespace/">DCMI Namespace Policy</a>
@see <a href="http://dublincore.org/documents/dces/">Dublin Core Metadata Element Set, Version 1.1</a>
*/
public class DCMI
{

	/**The recommended prefix of the Dublin Core Metadata Initiative elements namespace.*/
	public final static String DCMI_ELEMENTS_NAMESPACE_PREFIX="dc";
	/**The URI to the Dublin Core Metadata Initiative element set 1.0 namespace.*/
	public final static URI DCMI10_ELEMENTS_NAMESPACE_URI=URI.create("http://purl.org/dc/elements/1.0/");
	/**The URI to the Dublin Core Metadata Initiative element set 1.1 namespace.*/
	public final static URI DCMI11_ELEMENTS_NAMESPACE_URI=URI.create("http://purl.org/dc/elements/1.1/");

		//Dublin Core property names
	/**The title of a resource.*/
	public final static String TITLE_PROPERTY_NAME="title";
	/**The creator of a resource.*/
	public final static String CREATOR_PROPERTY_NAME="creator";
	/**The subject of a resource.*/
	public final static String SUBJECT_PROPERTY_NAME="subject";
	/**The description of a resource.*/
	public final static String DESCRIPTION_PROPERTY_NAME="description";
	/**The publisher of a resource.*/
	public final static String PUBLISHER_PROPERTY_NAME="publisher";
	/**The contributor of a resource.*/
	public final static String CONTRIBUTOR_PROPERTY_NAME="contributor";
	/**The date of a resource.*/
	public final static String DATE_PROPERTY_NAME="date";
	/**The Dublin Core type of a resource.*/
	public final static String TYPE_PROPERTY_NAME="type";
	/**The format of a resource.*/
	public final static String FORMAT_PROPERTY_NAME="format";
	/**The Dublin Core identifier of a resource.*/
	public final static String IDENTIFIER_PROPERTY_NAME="identifier";
	/**The source of a resource.*/
	public final static String SOURCE_PROPERTY_NAME="source";
	/**The language of a resource.*/
	public final static String LANGUAGE_PROPERTY_NAME="language";
	/**The relation of a resource.*/
	public final static String RELATION_PROPERTY_NAME="relation";
	/**The coverage of a resource.*/
	public final static String COVERAGE_PROPERTY_NAME="coverage";
	/**The rights of a resource.*/
	public final static String RIGHTS_PROPERTY_NAME="rights";

		//Dublin Core property URIs
	/**The title of a resource.*/
	public final static URI TITLE_PROPERTY_URI=DCMI11_ELEMENTS_NAMESPACE_URI.resolve(TITLE_PROPERTY_NAME);
	/**The creator of a resource.*/
	public final static URI CREATOR_PROPERTY_URI=DCMI11_ELEMENTS_NAMESPACE_URI.resolve(CREATOR_PROPERTY_NAME);
	/**The subject of a resource.*/
	public final static URI SUBJECT_PROPERTY_URI=DCMI11_ELEMENTS_NAMESPACE_URI.resolve(SUBJECT_PROPERTY_NAME);
	/**The description of a resource.*/
	public final static URI DESCRIPTION_PROPERTY_URI=DCMI11_ELEMENTS_NAMESPACE_URI.resolve(DESCRIPTION_PROPERTY_NAME);
	/**The publisher of a resource.*/
	public final static URI PUBLISHER_PROPERTY_URI=DCMI11_ELEMENTS_NAMESPACE_URI.resolve(PUBLISHER_PROPERTY_NAME);
	/**The contributor of a resource.*/
	public final static URI CONTRIBUTOR_PROPERTY_URI=DCMI11_ELEMENTS_NAMESPACE_URI.resolve(CONTRIBUTOR_PROPERTY_NAME);
	/**The date of a resource.*/
	public final static URI DATE_PROPERTY_URI=DCMI11_ELEMENTS_NAMESPACE_URI.resolve(DATE_PROPERTY_NAME);
	/**The Dublin Core type of a resource.*/
	public final static URI TYPE_PROPERTY_URI=DCMI11_ELEMENTS_NAMESPACE_URI.resolve(TYPE_PROPERTY_NAME);
	/**The format of a resource.*/
	public final static URI FORMAT_PROPERTY_URI=DCMI11_ELEMENTS_NAMESPACE_URI.resolve(FORMAT_PROPERTY_NAME);
	/**The Dublin Core identifier of a resource.*/
	public final static URI IDENTIFIER_PROPERTY_URI=DCMI11_ELEMENTS_NAMESPACE_URI.resolve(IDENTIFIER_PROPERTY_NAME);
	/**The source of a resource.*/
	public final static URI SOURCE_PROPERTY_URI=DCMI11_ELEMENTS_NAMESPACE_URI.resolve(SOURCE_PROPERTY_NAME);
	/**The language of a resource.*/
	public final static URI LANGUAGE_PROPERTY_URI=DCMI11_ELEMENTS_NAMESPACE_URI.resolve(LANGUAGE_PROPERTY_NAME);
	/**The relation of a resource.*/
	public final static URI RELATION_PROPERTY_URI=DCMI11_ELEMENTS_NAMESPACE_URI.resolve(RELATION_PROPERTY_NAME);
	/**The coverage of a resource.*/
	public final static URI COVERAGE_PROPERTY_URI=DCMI11_ELEMENTS_NAMESPACE_URI.resolve(COVERAGE_PROPERTY_NAME);
	/**The rights of a resource.*/
	public final static URI RIGHTS_PROPERTY_URI=DCMI11_ELEMENTS_NAMESPACE_URI.resolve(RIGHTS_PROPERTY_NAME);

	/**Returns the date of the resource.
	@param resource The resource for which the date should be returned.
	@return The date of the resource, or <code>null</code> if there is no date or the property does not contain an <code>urf.Date</code> or <code>urf.DateTime</code>.
	@see #DATE_PROPERTY_URI
	*/
	public static AbstractURFDateTime getDate(final URFResource resource)
	{
		return asAbstractDateTime(resource.getPropertyValue(DATE_PROPERTY_URI));	//return the dc.date as a date or date time
	}

	/**Returns the creator of the resource
	@param resource The resource the property of which should be located.
	@return The string value of the property, or <code>null</code> if there is no such property or the property value is not a string.
	@see #CREATOR_PROPERTY_URI
	*/
	public static String getCreator(final URFResource resource)
	{
		return asString(resource.getPropertyValue(CREATOR_PROPERTY_URI));
	}

	/**Sets the creator of the resource.
	@param resource The resource of which the property should be set.
	@param value The property value to set.
	@see #CREATOR_PROPERTY_URI
	*/
	public static void setCreator(final URFResource resource, final String value)
	{
		resource.setPropertyValue(CREATOR_PROPERTY_URI, value);
	}

	/**Sets the date of the resource
	@param resource The resource the date to set.
	@param date The new date.
	@see #DATE_PROPERTY_URI
	*/
	public static void setDate(final URFResource resource, final AbstractURFDateTime date)
	{
		if(date instanceof URFDate)	//if this is a date
		{
			resource.setPropertyValue(DATE_PROPERTY_URI, (URFDate)date);	//create a date resource and set the resource's dc.date
		}
		else if(date instanceof URFDateTime)	//if this is a date time
		{
			resource.setPropertyValue(DATE_PROPERTY_URI, (URFDateTime)date);	//create a date time resource and set the resource's dc.date
		}
		else	//if we don't recognize the type of abstract date time
		{
			throw new AssertionError("Unrecognized abstract date time type: "+date.getClass());
		}
	}

	/**Returns the description of the resource
	@param resource The resource the property of which should be located.
	@return The string value of the property, or <code>null</code> if there is no such property or the property value is not a string.
	@see #DESCRIPTION_PROPERTY_URI
	*/
	public static String getDescription(final URFResource resource)
	{
		return asString(resource.getPropertyValue(DESCRIPTION_PROPERTY_URI));
	}

	/**Sets the description of the resource.
	@param resource The resource of which the property should be set.
	@param value The property value to set.
	@see #DESCRIPTION_PROPERTY_URI
	*/
	public static void setDescription(final URFResource resource, final String value)
	{
		resource.setPropertyValue(DESCRIPTION_PROPERTY_URI, value);
	}

	/**Returns the language of the resource.
	@param resource The resource the property of which should be located.
	@return The value of the first language property, or <code>null</code> if no such property exists or the property value does not contain a language resource.
	@exception IllegalArgumentException if the language value resource represents a locale that does not have the correct syntax, such as if the language tag has more than three components.
	@see #LANGUAGE_PROPERTY_URI
	*/
	public static Locale getLanguage(final URFResource resource)
	{
		return asLanguage(resource.getPropertyValue(LANGUAGE_PROPERTY_URI));	//return the language as a locale
	}

	/**Sets the language of the resource.
	@param resource The resource to which the property should be set.
	@param locale The property value to set.
	@see #LANGUAGE_PROPERTY_URI
	*/
	public static void setLanguage(final URFResource resource, final Locale locale)
	{
		resource.setPropertyValue(DCMI11_ELEMENTS_NAMESPACE_URI, DEFAULT_URF_RESOURCE_FACTORY.createLanguageResource(locale));	//create a resource for the given locale and add it as the property
	}

	/**Returns the rights of the resource
	@param resource The resource the property of which should be located.
	@return The string value of the property, or <code>null</code> if there is no such property or the property value is not a string.
	@see #RIGHTS_PROPERTY_URI
	*/
	public static String getRights(final URFResource resource)
	{
		return asString(resource.getPropertyValue(RIGHTS_PROPERTY_URI));
	}

	/**Sets the rights of the resource.
	@param resource The resource of which the property should be set.
	@param value The property value to set.
	@see #RIGHTS_PROPERTY_URI
	*/
	public static void setRights(final URFResource resource, final String value)
	{
		resource.setPropertyValue(RIGHTS_PROPERTY_URI, value);
	}
	
	/**Returns the subject of the resource
	@param resource The resource the property of which should be located.
	@return The string value of the property, or <code>null</code> if there is no such property or the property value is not a string.
	@see #SUBJECT_PROPERTY_URI
	*/
	public static String getSubject(final URFResource resource)
	{
		return asString(resource.getPropertyValue(SUBJECT_PROPERTY_URI));
	}

	/**Returns the subjects of the resource
	@param resource The resource the property of which should be located.
	@return The string values of the property.
	@see #SUBJECT_PROPERTY_URI
	*/
	public static String[] getSubjects(final URFResource resource)
	{
		return asStrings(resource.getPropertyValues(SUBJECT_PROPERTY_URI));
	}

	/**Sets the subject of the resource.
	@param resource The resource of which the property should be set.
	@param value The property value to set.
	@see #SUBJECT_PROPERTY_URI
	*/
	public static void setSubject(final URFResource resource, final String value)
	{
		resource.setPropertyValue(SUBJECT_PROPERTY_URI, value);
	}
	
	/**Sets the subjects of the resource.
	@param resource The resource of which the property should be set.
	@param values The property values to set.
	@see #SUBJECT_PROPERTY_URI
	*/
	public static void setSubjects(final URFResource resource, final String... values)
	{
		resource.setPropertyValues(SUBJECT_PROPERTY_URI, values);
	}
	
	/**Returns the title of the resource
	@param resource The resource the property of which should be located.
	@return The string value of the property, or <code>null</code> if there is no such property or the property value is not a string.
	@see #TITLE_PROPERTY_URI
	*/
	public static String getTitle(final URFResource resource)
	{
		return asString(resource.getPropertyValue(TITLE_PROPERTY_URI));
	}

	/**Sets the title of the resource.
	@param resource The resource of which the property should be set.
	@param value The property value to set.
	@see #TITLE_PROPERTY_URI
	*/
	public static void setTitle(final URFResource resource, final String value)
	{
		resource.setPropertyValue(TITLE_PROPERTY_URI, value);
	}

}