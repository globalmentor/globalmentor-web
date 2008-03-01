package com.garretwilson.text.xml.svg;

import java.net.URI;
import javax.mail.internet.ContentType;

import com.globalmentor.io.ContentTypeConstants;
import com.globalmentor.io.ContentTypes;

/**Constants for SVG.
@author Garret Wilson
@see http://www.w3.org/QA/2002/04/valid-dtd-list.html
*/
public class SVGConstants
{

	/**The content type for SVG: <code>application/svg+xml</code>.*/ 
	public static final ContentType SVG_CONTENT_TYPE=new ContentType(ContentTypes.APPLICATION_PRIMARY_TYPE, ContentTypeConstants.SVG_XML_SUBTYPE, null);

	/**The recommended prefix to the SVG namespace.*/
	public static final String SVG_NAMESPACE_PREFIX="svg";

	/**The URI to the XHTML namespace.*/
//TODO fix	public static final URI XHTML_NAMESPACE_URI=URI.create("http://www.w3.org/1999/xhtml");

	/**The public ID for the SVG 1.0 DTD.*/
	public final static String SVG_1_0_PUBLIC_ID="-//W3C//DTD SVG 1.0//EN";
	/**The system ID for the SVG 1.0 DTD.*/
	public final static String SVG_1_0_SYSTEM_ID="http://www.w3.org/TR/2001/REC-SVG-20010904/DTD/svg10.dtd";

	/**The public ID for the SVG 1.1 Full DTD.*/
	public final static String SVG_1_1_FULL_PUBLIC_ID="-//W3C//DTD SVG 1.1//EN";
	/**The system ID for the SVG 1.1 Full DTD.*/
	public final static String SVG_1_1_FULL_SYSTEM_ID="http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd";
	
	/**The public ID for the SVG 1.1 Basic DTD.*/
	public final static String SVG_1_1_BASIC_PUBLIC_ID="-//W3C//DTD SVG 1.1 Basic//EN";
	/**The system ID for the SVG 1.1 Basic DTD.*/
	public final static String SVG_1_1_BASIC_SYSTEM_ID="http://www.w3.org/Graphics/SVG/1.1/DTD/svg11-basic.dtd";

	/**The public ID for the SVG 1.1 Tiny DTD.*/
	public final static String SVG_1_1_TINY_PUBLIC_ID="-//W3C//DTD SVG 1.1 Tiny//EN";
	/**The system ID for the SVG 1.1 Tiny DTD.*/
	public final static String SVG_1_1_TINY_SYSTEM_ID="http://www.w3.org/Graphics/SVG/1.1/DTD/svg11-tiny.dtd";

	//The SVG document element names.
	public final static String ELEMENT_SVG="svg";

}
