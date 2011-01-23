package com.globalmentor.urf.foaf;

import java.net.URI;

/**Definition of the <a href="http://xmlns.com/foaf/0.1/">FOAF Vocabulary Specification</a>. 
<p>Copyright © 2007 GlobalMentor, Inc.
This source code can be freely used for any purpose, as long as the following conditions are met.
Any object code derived from this source code must include the following text to users using along with other "about" notifications:
"Uniform Resource Framework (URF) &lt;http://www.urf.name/&gt; specification and processing
written by Garret Wilson &lt;http://www.garretwilson.com/&gt; and Copyright © 2007 GlobalMentor, Inc. &lt;http://www.globalmentor.com/&gt;."
Any redistribution of this source code or derived source code must include these comments unmodified.</p>
@author Garret Wilson
@see <a href="http://xmlns.com/foaf/0.1/">FOAF Vocabulary Specification</a>
*/
public class FOAF
{

	/**The recommended prefix to the FOAF namespace.*/
	public final static String FOAF_NAMESPACE_PREFIX="foaf";
	/**The URI to the FOAF namespace.*/
	public final static URI FOAF_NAMESPACE_URI=URI.create("http://xmlns.com/foaf/0.1/");

		//FOAF type names
	/**An agent (e.g. person, group, software or physical artifact).*/
	public final static String AGENT_TYPE_NAME="Agent";
	/**A church.*/
	public final static String CHURCH_TYPE_NAME="Church";
	/**A company.*/
	public final static String COMPANY_TYPE_NAME="Company";
	/**A non-governmental organization.*/
	public final static String NGO_TYPE_NAME="NGO";
	/**A non-profit organization.*/
	public final static String NONPROFIT_TYPE_NAME="Nonprofit";
	/**An organization.*/
	public final static String ORGANIZATION_TYPE_NAME="Organization";
	/**A person.*/
	public final static String PERSON_TYPE_NAME="Person";
	/**A religious organization.*/
	public final static String RELIGIOUS_ORGANIZATION_TYPE_NAME="ReligiousOrganization";
	/**A school.*/
	public final static String SCHOOL_TYPE_NAME="School";
	/**A synagogue.*/
	public final static String SYNAGOGUE_TYPE_NAME="Synagogue";
	/**A temple.*/
	public final static String TEMPLE_TYPE_NAME="Temple";
	/**A university.*/
	public final static String UNIVERSITY_TYPE_NAME="University";

		//FOAF property names
	/**The gender of this Agent (typically but not necessarily "male" or "female").*/
	public final static String GENDER_PROPERTY_NAME="gender";
	/**A personal mailbox.*/
	public final static String MBOX_PROPERTY_NAME="mbox";
	/**A name for some thing.*/
	public final static String NAME_PROPERTY_NAME="name";

}