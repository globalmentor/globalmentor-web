package com.garretwilson.text.xml.xhtml;

import java.net.URI;
import com.garretwilson.io.MediaType;

/**Constants for XHTML.
@author Garret Wilson
*/
public interface XHTMLConstants
{

	/**The content type for XHTML: <code>application/xhtml+xml</code>.*/ 
	public static final MediaType XHTML_CONTENT_TYPE=new MediaType(MediaType.APPLICATION, MediaType.XHTML_XML);

	/**The recommended prefix to the XHTML namespace.*/
	public static final String XHTML_NAMESPACE_PREFIX="xhtml";

	/**The URI to the XHTML namespace.*/
	public static final URI XHTML_NAMESPACE_URI=URI.create("http://www.w3.org/1999/xhtml");

//G***del	/**The package name of the OEB XML classes.*/
//G***del if not needed	public final static String PACKAGE_NAME="com.garretwilson.text.xml.oeb";

	/**The public ID for XHTML 1.0.*/
	public final static String XHTML1_STRICT_PUBLIC_ID="-//W3C//DTD XHTML 1.0 Strict//EN";

	/**The base system ID for XHTML 1.0 strict.*/
	public final static String XHTML1_STRICT_SYSTEM_ID="xhtml1-strict.dtd";

	/**The public ID for XHTML 1.1.*/
	public final static String XHTML1_1_PUBLIC_ID="-//W3C//DTD XHTML 1.1//EN";

	/**The base system ID for XHTML 1.1.*/
	public final static String XHTML1_1_SYSTEM_ID="xhtml1.dtd";

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
	public final static String ELEMENT_LINK="link";
	public final static String ELEMENT_MAP="map";
	public final static String ELEMENT_META="meta";
	public final static String ELEMENT_OBJECT="object";
	public final static String ELEMENT_OL="ol";
	public final static String ELEMENT_P="p";
	public final static String ELEMENT_PARAM="param";
	public final static String ELEMENT_PRE="pre";
	public final static String ELEMENT_Q="q";
	public final static String ELEMENT_S="s";
	public final static String ELEMENT_SAMP="samp";
	public final static String ELEMENT_SCRIPT="script";
	public final static String ELEMENT_SMALL="small";
	public final static String ELEMENT_SPAN="span";
	public final static String ELEMENT_STRIKE="strike";
	public final static String ELEMENT_STRONG="strong";
	public final static String ELEMENT_STYLE="style";
	public final static String ELEMENT_SUB="sub";
	public final static String ELEMENT_SUP="sup";
	public final static String ELEMENT_TABLE="table";
	public final static String ELEMENT_TD="td";
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
	/**The attribute for type ID.*/
	public final static String ATTRIBUTE_ID="id";
	/**The attribute for language.*/
	public final static String ATTRIBUTE_LANG="lang";
	/**The attribute for name.*/
	public final static String ATTRIBUTE_NAME="name";
	/**The attribute for style.*/
	public final static String ATTRIBUTE_STYLE="style";

		//attributes for <a>
	public final static String ELEMENT_A_ATTRIBUTE_HREF="href";

		//attributes for <applet>
	public final static String ELEMENT_APPLET_ATTRIBUTE_CODE="code";
	public final static String ELEMENT_APPLET_ATTRIBUTE_HEIGHT="height";
	public final static String ELEMENT_APPLET_ATTRIBUTE_WIDTH="width";

		//attributes for <form>
	public final static String ELEMENT_FORM_ATTRIBUTE_ACTION="action";
	public final static String ELEMENT_FORM_ATTRIBUTE_ENCTYPE="enctype";
	public final static String ELEMENT_FORM_ATTRIBUTE_METHOD="method";
		public final static String FORM_METHOD_GET="get";
		public final static String FORM_METHOD_POST="post";

		//attributes for <img>
	public final static String ELEMENT_IMG_ATTRIBUTE_ALT="alt";
	public final static String ELEMENT_IMG_ATTRIBUTE_HEIGHT="height";
	public final static String ELEMENT_IMG_ATTRIBUTE_WIDTH="width";
	public final static String ELEMENT_IMG_ATTRIBUTE_SRC="src";

		//attributes for <object>
	public final static String ELEMENT_OBJECT_ATTRIBUTE_CLASSID="classid";
	public final static String ELEMENT_OBJECT_ATTRIBUTE_CODETYPE="codetype";
	public final static String ELEMENT_OBJECT_ATTRIBUTE_DATA="data";
	public final static String ELEMENT_OBJECT_ATTRIBUTE_HEIGHT="height";
	public final static String ELEMENT_OBJECT_ATTRIBUTE_WIDTH="width";
	public final static String ELEMENT_OBJECT_ATTRIBUTE_TYPE="type";

		//attributes for <param>
	public final static String ELEMENT_PARAM_ATTRIBUTE_NAME="name";
	public final static String ELEMENT_PARAM_ATTRIBUTE_VALUE="value";

}
