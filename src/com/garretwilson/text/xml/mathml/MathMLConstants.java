package com.garretwilson.text.xml.mathml;

import java.net.URI;
import javax.mail.internet.ContentType;
import com.garretwilson.io.ContentTypeConstants;
import com.garretwilson.io.ContentTypes;

/**Constants for MathML.
@author Garret Wilson
@see http://www.w3.org/QA/2002/04/valid-dtd-list.html
*/
public class MathMLConstants
{

	/**The content type for MathML: <code>application/mathml+xml</code>.*/ 
	public static final ContentType MATHML_CONTENT_TYPE=new ContentType(ContentTypes.APPLICATION_PRIMARY_TYPE, ContentTypeConstants.MATHML_XML_SUBTYPE, null);

	/**The recommended prefix to the MathML namespace.*/
	public static final String MATHML_NAMESPACE_PREFIX="mathml";

	/**The URI to the XHTML namespace.*/
//TODO fix	public static final URI XHTML_NAMESPACE_URI=URI.create("http://www.w3.org/1999/xhtml");

	/**The system ID for the MathML 1.01 DTD.*/
	public final static String MATHML_1_01_SYSTEM_ID="http://www.w3.org/Math/DTD/mathml1/mathml.dtd";

	/**The public ID for the MathML 2.0 DTD.*/
	public final static String MATHML_2_0_PUBLIC_ID="-//W3C//DTD MathML 2.0//EN";
	/**The system ID for the MathML 2.0 DTD.*/
	public final static String MATHML_2_0_SYSTEM_ID="http://www.w3.org/TR/MathML2/dtd/mathml2.dtd";

	//The MathML document element names.
	public final static String ELEMENT_MATHML="mathml";

}
