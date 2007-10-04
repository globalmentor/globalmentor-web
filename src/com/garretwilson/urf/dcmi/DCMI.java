package com.garretwilson.urf.dcmi;

import java.net.URI;
import java.util.Locale;

import com.garretwilson.urf.*;
import static com.garretwilson.util.LocaleUtilities.*;

/**Constants used for Dublin Core as stored in URF.
@author Garret Wilson
@see <a href="http://dublincore.org/documents/dcmi-namespace/">DCMI Namespace Policy</a>
*/
public class DCMI
{

	/**The recommended prefix to the Dublin Core Metadata Initiative elements namespace.*/
	public final static String DCMI_ELEMENTS_NAMESPACE_PREFIX="dc";
	/**The URI to the Dublin Core Metadata Initiative element set 1.0 namespace.*/
	public final static URI DCMI10_ELEMENTS_NAMESPACE_URI=URI.create("http://purl.org/dc/elements/1.0/");
	/**The URI to the Dublin Core Metadata Initiative element set 1.1 namespace.*/
	public final static URI DCMI11_ELEMENTS_NAMESPACE_URI=URI.create("http://purl.org/dc/elements/1.1/");

		//Dublin Core Dublin Core Metadata Initiative element property names
	/**The title of a resource.*/
	public final static URI TITLE_PROPERTY_URI=DCMI11_ELEMENTS_NAMESPACE_URI.resolve("title");
	/**The creator of a resource.*/
	public final static URI CREATOR_PROPERTY_URI=DCMI11_ELEMENTS_NAMESPACE_URI.resolve("creator");
	/**The subject of a resource.*/
	public final static URI SUBJECT_PROPERTY_URI=DCMI11_ELEMENTS_NAMESPACE_URI.resolve("subject");
	/**The description of a resource.*/
	public final static URI DESCRIPTION_PROPERTY_URI=DCMI11_ELEMENTS_NAMESPACE_URI.resolve("description");
	/**The publisher of a resource.*/
	public final static URI PUBLISHER_PROPERTY_URI=DCMI11_ELEMENTS_NAMESPACE_URI.resolve("publisher");
	/**The contributor of a resource.*/
	public final static URI CONTRIBUTOR_PROPERTY_URI=DCMI11_ELEMENTS_NAMESPACE_URI.resolve("contributor");
	/**The date of a resource.*/
	public final static URI DATE_PROPERTY_URI=DCMI11_ELEMENTS_NAMESPACE_URI.resolve("date");
	/**The Dublin Core type of a resource.*/
	public final static URI TYPE_PROPERTY_URI=DCMI11_ELEMENTS_NAMESPACE_URI.resolve("type");
	/**The format of a resource.*/
	public final static URI FORMAT_PROPERTY_URI=DCMI11_ELEMENTS_NAMESPACE_URI.resolve("format");
	/**The Dublin Core identifier of a resource.*/
	public final static URI IDENTIFIER_PROPERTY_URI=DCMI11_ELEMENTS_NAMESPACE_URI.resolve("identifier");
	/**The source of a resource.*/
	public final static URI SOURCE_PROPERTY_URI=DCMI11_ELEMENTS_NAMESPACE_URI.resolve("source");
	/**The language of a resource.*/
	public final static URI LANGUAGE_PROPERTY_URI=DCMI11_ELEMENTS_NAMESPACE_URI.resolve("language");
	/**The relation of a resource.*/
	public final static URI RELATION_PROPERTY_URI=DCMI11_ELEMENTS_NAMESPACE_URI.resolve("relation");
	/**The coverage of a resource.*/
	public final static URI COVERAGE_PROPERTY_URI=DCMI11_ELEMENTS_NAMESPACE_URI.resolve("coverage");
	/**The rights of a resource.*/
	public final static URI RIGHTS_PROPERTY_URI=DCMI11_ELEMENTS_NAMESPACE_URI.resolve("rights");

	/**Sets the «{@value #LANGUAGE_PROPERTY_URI}» property with the given value to the resource.
	@param resource The resource to which the property should be set.
	@param locale The property value to set.
	*/
	public static void setLanguage(final URFResource resource, final Locale locale)
	{
		resource.setPropertyValue(DCMI11_ELEMENTS_NAMESPACE_URI, new DefaultURFResource(createInfoLangURI(locale)));	//create a resource for the given locale and add it as the property
	}

}