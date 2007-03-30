package com.garretwilson.rdf.foaf;

import java.net.URI;

/**Definition of the <a href="http://xmlns.com/foaf/0.1/">FOAF Vocabulary Specification</a>. 
@author Garret Wilson
*/
public class FOAF
{

	/**The recommended prefix to the FOAF namespace.*/
	public final static String FOAF_NAMESPACE_PREFIX="foaf";
	/**The URI to the FOAF namespace.*/
	public final static URI FOAF_NAMESPACE_URI=URI.create("http://xmlns.com/foaf/0.1/");

		//FOAF type names
	/**A church. The local name of <code>foaf:Church</code>.*/
	public final static String CHURCH_TYPE_NAME="Church";
	/**An agent (e.g. person, group, software or physical artifact). The local name of <code>foaf:Agent</code>.*/
	public final static String AGENT_TYPE_NAME="Agent";
	/**An organization. The local name of <code>foaf:Organization</code>.*/
	public final static String ORGANIZATION_TYPE_NAME="Organization";
	/**A person. The local name of <code>foaf:Person</code>.*/
	public final static String PERSON_TYPE_NAME="Person";
	/**A school. The local name of <code>foaf:School</code>.*/
	public final static String SCHOOL_TYPE_NAME="School";
	/**A synagogue. The local name of <code>foaf:Synagogue</code>.*/
	public final static String SYNAGOGUE_TYPE_NAME="Synagogue";
	/**A temple. The local name of <code>foaf:Temple</code>.*/
	public final static String TEMPLE_TYPE_NAME="Temple";
	/**A university. The local name of <code>foaf:University</code>.*/
	public final static String UNIVERSITY_TYPE_NAME="University";

		//FOAF property names
	/**The gender of this Agent (typically but not necessarily "male" or "female"). The local name of <code>foaf:gender</code>.*/
	public final static String GENDER_PROPERTY_NAME="gender";
	/**A personal mailbox. The local name of <code>foaf:mbox</code>.*/
	public final static String MBOX_PROPERTY_NAME="mbox";
	/**A name for some thing. The local name of <code>foaf:name</code>.*/
	public final static String NAME_PROPERTY_NAME="name";

}