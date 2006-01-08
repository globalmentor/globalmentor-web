package com.garretwilson.text.xml.xhtml;

import java.net.URI;
import javax.mail.internet.ContentType;
import static com.garretwilson.io.ContentTypeConstants.*;

/**Constants for XHTML.
@author Garret Wilson
@see <a href="http://www.w3.org/QA/2002/04/valid-dtd-list.html">http://www.w3.org/QA/2002/04/valid-dtd-list.html</a>
*/
public interface XHTMLConstants
{

	/**The content type for HTML: <code>text/html</code>.*/ 
	public static final ContentType HTML_CONTENT_TYPE=new ContentType(TEXT, HTML_SUBTYPE, null);

	/**The content type for XHTML: <code>application/xhtml+xml</code>.*/ 
	public static final ContentType XHTML_CONTENT_TYPE=new ContentType(APPLICATION, XHTML_XML_SUBTYPE, null);

	/**The recommended prefix to the XHTML namespace.*/
	public static final String XHTML_NAMESPACE_PREFIX="xhtml";

	/**The URI to the XHTML namespace.*/
	public static final URI XHTML_NAMESPACE_URI=URI.create("http://www.w3.org/1999/xhtml");

//G***del	/**The package name of the OEB XML classes.*/
//G***del if not needed	public final static String PACKAGE_NAME="com.garretwilson.text.xml.oeb";

	/**The public ID for the HTML 2.0 DTD.*/
	public final static String HTML_2_0_PUBLIC_ID="-//IETF//DTD HTML 2.0//EN";
	
	/**The public ID for the HTML 3.2 DTD.*/
	public final static String HTML_3_2_PUBLIC_ID="-//W3C//DTD HTML 3.2 Final//EN";

	/**The public ID for the HTML 4.01 Strict DTD.*/
	public final static String HTML_4_01_STRICT_PUBLIC_ID="-//W3C//DTD HTML 4.01//EN";
	/**The system ID for the HTML 4.01 Strict DTD.*/
	public final static String HTML_4_01_STRICT_SYSTEM_ID="http://www.w3.org/TR/html4/strict.dtd";

	/**The public ID for the HTML 4.01 Traditional DTD.*/
	public final static String HTML_4_01_TRANSITIONAL_PUBLIC_ID="-//W3C//DTD HTML 4.01 Transitional//EN";
	/**The system ID for the HTML 4.01 Traditional DTD.*/
	public final static String HTML_4_01_TRANSITIONAL_SYSTEM_ID="http://www.w3.org/TR/html4/loose.dtd";

	/**The public ID for the HTML 4.01 Frameset DTD.*/
	public final static String HTML_4_01_FRAMESET_PUBLIC_ID="-//W3C//DTD HTML 4.01 Frameset//EN";
	/**The system ID for the HTML 4.01 Frameset DTD.*/
	public final static String HTML_4_01_FRAMESET_SYSTEM_ID="http://www.w3.org/TR/html4/frameset.dtd";

	/**The public ID for the XHTML 1.0 Strict DTD.*/
	public final static String XHTML_1_0_STRICT_PUBLIC_ID="-//W3C//DTD XHTML 1.0 Strict//EN";
	/**The system ID for the XHTML 1.0 Strict DTD.*/
	public final static String XHTML_1_0_STRICT_SYSTEM_ID="http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd";

	/**The public ID for the XHTML 1.0 Traditional DTD.*/
	public final static String XHTML_1_0_TRANSITIONAL_PUBLIC_ID="-//W3C//DTD XHTML 1.0 Transitional//EN";
	/**The system ID for the XHTML 1.0 Traditional DTD.*/
	public final static String XHTML_1_0_TRANSITIONAL_SYSTEM_ID="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd";

	/**The public ID for the XHTML 1.0 Frameset DTD.*/
	public final static String XHTML_1_0_FRAMESET_PUBLIC_ID="-//W3C//DTD XHTML 1.0 Frameset//EN";
	/**The system ID for the XHTML 1.0 Frameset DTD.*/
	public final static String XHTML_1_0_FRAMESET_SYSTEM_ID="http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd";

	/**The public ID for the XHTML 1.1 DTD.*/
	public final static String XHTML_1_1_PUBLIC_ID="-//W3C//DTD XHTML 1.1//EN";
	/**The system ID for the XHTML 1.1 DTD.*/
	public final static String XHTML_1_1_SYSTEM_ID="http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd";

	/**The public ID for the XHTML+MathML+SVG DTD.*/
	public final static String XHTML_MATHML_SVG_PUBLIC_ID="-//W3C//DTD XHTML 1.1 plus MathML 2.0 plus SVG 1.1//EN";
	/**The system ID for the XHTML+MathML+SVG DTD.*/
	public final static String XHTML_MATHML_SVG_SYSTEM_ID="http://www.w3.org/2002/04/xhtml-math-svg/xhtml-math-svg.dtd";

	//The XHTML 1.0 document element names.
//G***fix; some are missing since these were copied from the OEB 1.0
	public final static String ELEMENT_A="a";
	public final static String ELEMENT_APPLET="applet";
	public final static String ELEMENT_AREA="area";
	public final static String ELEMENT_B="b";
	public final static String ELEMENT_BASE="base";
	public final static String ELEMENT_BIG="big";
	public final static String ELEMENT_BLOCKQUOTE="blockquote";
	public final static String ELEMENT_BODY="body";
	public final static String ELEMENT_BR="br";
	public final static String ELEMENT_BUTTON="button";
	public final static String ELEMENT_CAPTION="caption";
	public final static String ELEMENT_CENTER="center";
	public final static String ELEMENT_CITE="cite";
	public final static String ELEMENT_COL="col";
	public final static String ELEMENT_CODE="code";
	public final static String ELEMENT_DD="dd";
	public final static String ELEMENT_DFN="dfn";
	public final static String ELEMENT_DIV="div";
	public final static String ELEMENT_DL="dl";
	public final static String ELEMENT_DT="dt";
	public final static String ELEMENT_EM="em";
	public final static String ELEMENT_FIELDSET="fieldset";
	public final static String ELEMENT_FONT="font";
	public final static String ELEMENT_FORM="form";
	public final static String ELEMENT_H1="h1";
	public final static String ELEMENT_H2="h2";
	public final static String ELEMENT_H3="h3";
	public final static String ELEMENT_H4="h4";
	public final static String ELEMENT_H5="h5";
	public final static String ELEMENT_H6="h6";
	public final static String ELEMENT_HEAD="head";
	public final static String ELEMENT_HR="hr";
	public final static String ELEMENT_HTML="html";
	public final static String ELEMENT_I="i";
	public final static String ELEMENT_IMG="img";
	public final static String ELEMENT_INPUT="input";
	public final static String ELEMENT_KBD="kbd";
	public final static String ELEMENT_LI="li";
	public final static String ELEMENT_LABEL="label";
	public final static String ELEMENT_LEGEND="legend";
	public final static String ELEMENT_LINK="link";
	public final static String ELEMENT_MAP="map";
	public final static String ELEMENT_META="meta";
	public final static String ELEMENT_OBJECT="object";
	public final static String ELEMENT_OL="ol";
	public final static String ELEMENT_OPTION="option";
	public final static String ELEMENT_P="p";
	public final static String ELEMENT_PARAM="param";
	public final static String ELEMENT_PRE="pre";
	public final static String ELEMENT_Q="q";
	public final static String ELEMENT_S="s";
	public final static String ELEMENT_SAMP="samp";
	public final static String ELEMENT_SCRIPT="script";
	public final static String ELEMENT_SELECT="select";
	public final static String ELEMENT_SMALL="small";
	public final static String ELEMENT_SPAN="span";
	public final static String ELEMENT_STRIKE="strike";
	public final static String ELEMENT_STRONG="strong";
	public final static String ELEMENT_STYLE="style";
	public final static String ELEMENT_SUB="sub";
	public final static String ELEMENT_SUP="sup";
	public final static String ELEMENT_TABLE="table";
	public final static String ELEMENT_TD="td";
	public final static String ELEMENT_TEXTAREA="textarea";
	public final static String ELEMENT_TH="th";
	public final static String ELEMENT_TBODY="tbody";
	public final static String ELEMENT_THEAD="thead";
	public final static String ELEMENT_TFOOT="tfoot";
	public final static String ELEMENT_TITLE="title";
	public final static String ELEMENT_TR="tr";
	public final static String ELEMENT_TT="tt";
	public final static String ELEMENT_U="u";
	public final static String ELEMENT_UL="ul";
	public final static String ELEMENT_VAR="var";

		//attribute names
	/**The attribute for class.*/
	public final static String ATTRIBUTE_CLASS="class";
	/**The attribute for direction.*/
	public final static String ATTRIBUTE_DIR="dir";
		/**The attribute for left-to-right direction.*/
		public final static String DIR_LTR="ltr";
		/**The attribute for right-to-left direction.*/
		public final static String DIR_RTL="rtl";
	/**The attribute for ID.*/
	public final static String ATTRIBUTE_ID="id";
	/**The attribute for language.*/
	public final static String ATTRIBUTE_LANG="lang";
	/**The attribute for name.*/
	public final static String ATTRIBUTE_NAME="name";
	/**The attribute for style.*/
	public final static String ATTRIBUTE_STYLE="style";
	/**The attribute for title.*/
	public final static String ATTRIBUTE_TITLE="title";
	/**The attribute for value.*/
	public final static String ATTRIBUTE_VALUE="value";
		//event attributes
	public final static String ATTRIBUTE_ONCLICK="onclick";
	public final static String ATTRIBUTE_ONLOAD="onload";
	
		//attributes for <a>
	public final static String ELEMENT_A_ATTRIBUTE_HREF="href";

		//attributes for <applet>
	public final static String ELEMENT_APPLET_ATTRIBUTE_CODE="code";
	public final static String ELEMENT_APPLET_ATTRIBUTE_HEIGHT="height";
	public final static String ELEMENT_APPLET_ATTRIBUTE_WIDTH="width";

		//attributes for <button>
	public final static String ELEMENT_BUTTON_ATTRIBUTE_TYPE="type";
		public final static String BUTTON_TYPE_BUTTON="button";
		public final static String BUTTON_TYPE_RESET="reset";
		public final static String BUTTON_TYPE_SUBMIT="submit";

		//attributes for <form>
	public final static String ELEMENT_FORM_ATTRIBUTE_ACTION="action";
	public final static String ELEMENT_FORM_ATTRIBUTE_ENCTYPE="enctype";
		/**The "application/x-www-form-urlencoded" encoding type; see <a href="http://www.rfc-editor.org/rfc/rfc1867.txt">RFC 1867</a>.*/
		public final static ContentType APPLICATION_X_WWW_FORM_URLENCODED_CONTENT_TYPE=new ContentType(APPLICATION, X_WWW_FORM_URLENCODED, null);
		/**The "multipart/form-data" encoding type; see <a href="http://www.rfc-editor.org/rfc/rfc1867.txt">RFC 1867</a>.*/
		public final static ContentType MULTIPART_FORM_DATA_CONTENT_TYPE=new ContentType(MULTIPART, FORM_DATA_SUBTYPE, null);
		
	public final static String ELEMENT_FORM_ATTRIBUTE_METHOD="method";
		public final static String FORM_METHOD_GET="get";
		public final static String FORM_METHOD_POST="post";

		//attributes for <img>
	public final static String ELEMENT_IMG_ATTRIBUTE_ALT="alt";
	public final static String ELEMENT_IMG_ATTRIBUTE_HEIGHT="height";
	public final static String ELEMENT_IMG_ATTRIBUTE_WIDTH="width";
	public final static String ELEMENT_IMG_ATTRIBUTE_SRC="src";

		//attributes for <input>
	public final static String ELEMENT_INPUT_ATTRIBUTE_ACCEPT="accept";
	public final static String ELEMENT_INPUT_ATTRIBUTE_TYPE="type";
		public final static String INPUT_TYPE_BUTTON="button";
		public final static String INPUT_TYPE_CHECKBOX="checkbox";
		public final static String INPUT_TYPE_FILE="file";
		public final static String INPUT_TYPE_HIDDEN="hidden";
		public final static String INPUT_TYPE_IMAGE="image";
		public final static String INPUT_TYPE_PASSWORD="password";
		public final static String INPUT_TYPE_RADIO="radio";
		public final static String INPUT_TYPE_RESET="reset";
		public final static String INPUT_TYPE_SUBMIT="submit";
		public final static String INPUT_TYPE_TEXT="text";
	public final static String ELEMENT_INPUT_ATTRIBUTE_CHECKED="checked";
		public final static String INPUT_CHECKED_CHECKED="checked";
	public final static String ELEMENT_INPUT_ATTRIBUTE_DISABLED="disabled";
		public final static String INPUT_DISABLED_DISABLED="disabled";
	public final static String ELEMENT_INPUT_ATTRIBUTE_MAXLENGTH="maxlength";
	public final static String ELEMENT_INPUT_ATTRIBUTE_READONLY="readonly";
		public final static String INPUT_READONLY_READONLY="readonly";

		//attributes for <label>
	public final static String ELEMENT_LABEL_ATTRIBUTE_FOR="for";

		//attributes for <link>
	public final static String ELEMENT_LINK_ATTRIBUTE_HREF="href";
	public final static String ELEMENT_LINK_ATTRIBUTE_REL="rel";
		public final static String LINK_REL_STYLESHEET="stylesheet";
	public final static String ELEMENT_LINK_ATTRIBUTE_TYPE="type";

		//attributes for <object>
	public final static String ELEMENT_OBJECT_ATTRIBUTE_CLASSID="classid";
	public final static String ELEMENT_OBJECT_ATTRIBUTE_CODETYPE="codetype";
	public final static String ELEMENT_OBJECT_ATTRIBUTE_DATA="data";
	public final static String ELEMENT_OBJECT_ATTRIBUTE_HEIGHT="height";
	public final static String ELEMENT_OBJECT_ATTRIBUTE_WIDTH="width";
	public final static String ELEMENT_OBJECT_ATTRIBUTE_TYPE="type";

		//attributes for <option>
	public final static String ELEMENT_OPTION_ATTRIBUTE_SELECTED="selected";
		public final static String OPTION_SELECTED_SELECTED="selected";

		//attributes for <param>
	public final static String ELEMENT_PARAM_ATTRIBUTE_NAME="name";
	public final static String ELEMENT_PARAM_ATTRIBUTE_VALUE="value";

		//attributes for <script>
	public final static String ELEMENT_SCRIPT_ATTRIBUTE_SRC="src";
	public final static String ELEMENT_SCRIPT_ATTRIBUTE_TYPE="type";

		//attributes for <select>
	public final static String ELEMENT_SELECT_ATTRIBUTE_MULTIPLE="multiple";
		public final static String SELECT_MULTIPLE_MULTIPLE="multiple";
	public final static String ELEMENT_SELECT_ATTRIBUTE_SIZE="size";

		//attributes for <td>
	public final static String ELEMENT_TD_ATTRIBUTE_COLSPAN="colspan";
	public final static String ELEMENT_TD_ATTRIBUTE_SCOPE="scope";
		public final static String TD_SCOPE_COLGROUP="colgroup";

		//attributes for <textarea>
	public final static String ELEMENT_TEXTAREA_ATTRIBUTE_COLS="cols";
	public final static String ELEMENT_TEXTAREA_ATTRIBUTE_ROWS="rows";
	public final static String ELEMENT_TEXTAREA_ATTRIBUTE_WRAP="wrap";
		public final static String TEXTAREA_WRAP_OFF="off";
		public final static String TEXTAREA_WRAP_PHYSICAL="physical";
		public final static String TEXTAREA_WRAP_VIRTUAL="virtual";
	public final static String ELEMENT_TEXTAREA_ATTRIBUTE_DISABLED="disabled";
		public final static String TEXTAREA_DISABLED_DISABLED="disabled";
	public final static String ELEMENT_TEXTAREA_ATTRIBUTE_READONLY="readonly";
		public final static String TEXTAREA_READONLY_READONLY="readonly";

}
