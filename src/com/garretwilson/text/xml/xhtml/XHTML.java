package com.garretwilson.text.xml.xhtml;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import javax.mail.internet.ContentType;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.css.CSSStyleDeclaration;

import com.garretwilson.io.ContentTypeConstants;
import com.garretwilson.io.ContentTypes;
import com.garretwilson.io.Files;
import com.garretwilson.io.ParseReader;
import static com.garretwilson.text.xml.XMLUtilities.*;
import com.garretwilson.text.xml.oeb.OEBConstants;
import com.garretwilson.text.xml.stylesheets.css.XMLCSSProcessor;
import com.garretwilson.text.xml.stylesheets.css.XMLCSSStyleDeclaration;
import com.garretwilson.text.xml.xpath.XPath;
import com.garretwilson.text.xml.xpath.XPathConstants;
import com.garretwilson.util.ArrayUtilities;
import com.globalmentor.java.Objects;

import static com.garretwilson.io.ContentTypeConstants.*;

/**Constants to work with an XHTML and DOM document representing XHTML.
@author Garret Wilson
@see <a href="http://www.w3.org/QA/2002/04/valid-dtd-list.html">W3C QA - Recommended list of DTDs</a>
@see <a href="http://www.pibil.org/technology/writing/xhtml-media-type.html">XHTML: An XML Application</a>
*/
public class XHTML
{

	/**The content type for HTML: <code>text/html</code>.*/
	public static final ContentType HTML_CONTENT_TYPE=new ContentType(TEXT, HTML_SUBTYPE, null);

	/**The content type for XHTML: <code>application/xhtml+xml</code>.*/
	public static final ContentType XHTML_CONTENT_TYPE=new ContentType(APPLICATION, XHTML_XML_SUBTYPE, null);

	/**The content type for an XHTML fragment: <code>application/xhtml+xml-external-parsed-entity</code>.*/
	public static final ContentType XHTML_FRAGMENT_CONTENT_TYPE=new ContentType(APPLICATION, XHTML_XML_EXTERNAL_PARSED_ENTITY_SUBTYPE, null);

	/**The old extension for HTML resource names.*/
	public final static String HTM_NAME_EXTENSION="htm";

	/**The extension for HTML resource names.*/
	public final static String HTML_NAME_EXTENSION="html";

	/**The extension for XHTML resource names.*/
	public final static String XHTML_NAME_EXTENSION="xhtml";

	/**The recommended prefix to the XHTML namespace.*/
	public static final String XHTML_NAMESPACE_PREFIX="xhtml";

	/**The URI to the XHTML namespace.*/
	public static final URI XHTML_NAMESPACE_URI=URI.create("http://www.w3.org/1999/xhtml");

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
	public final static String ELEMENT_IFRAME="iframe";
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
	public final static String ELEMENT_A_ATTRIBUTE_TARGET="target";

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

		//attributes for <iframe>
	public final static String ELEMENT_IFRAME_ATTRIBUTE_SRC="src";
	public final static String ELEMENT_IFRAME_ATTRIBUTE_FRAMEBORDER="frameborder";

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
	public final static String ELEMENT_INPUT_ATTRIBUTE_SIZE="size";
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
	public final static String ELEMENT_OBJECT_ATTRIBUTE_CODEBASE="codebase";
	public final static String ELEMENT_OBJECT_ATTRIBUTE_CODETYPE="codetype";
	public final static String ELEMENT_OBJECT_ATTRIBUTE_DATA="data";
	public final static String ELEMENT_OBJECT_ATTRIBUTE_HEIGHT="height";
	public final static String ELEMENT_OBJECT_ATTRIBUTE_WIDTH="width";
	public final static String ELEMENT_OBJECT_ATTRIBUTE_TYPE="type";

		//attributes for <option>
	public final static String ELEMENT_OPTION_ATTRIBUTE_DISABLED="disabled";
		public final static String OPTION_DISABLED_DISABLED="disabled";
	public final static String ELEMENT_OPTION_ATTRIBUTE_SELECTED="selected";
		public final static String OPTION_SELECTED_SELECTED="selected";

		//attributes for <param>
	public final static String ELEMENT_PARAM_ATTRIBUTE_NAME="name";
	public final static String ELEMENT_PARAM_ATTRIBUTE_VALUE="value";

		//attributes for <script>
	public final static String ELEMENT_SCRIPT_ATTRIBUTE_SRC="src";
	public final static String ELEMENT_SCRIPT_ATTRIBUTE_TYPE="type";
	public final static String ELEMENT_SCRIPT_ATTRIBUTE_LANGUAGE="language";

		//attributes for <select>
	public final static String ELEMENT_SELECT_ATTRIBUTE_MULTIPLE="multiple";
		public final static String SELECT_MULTIPLE_MULTIPLE="multiple";
	public final static String ELEMENT_SELECT_ATTRIBUTE_SIZE="size";

		//attributes for <td>
	public final static String ELEMENT_TD_ATTRIBUTE_ALIGN="align";
		public final static String TD_ALIGN_LEFT="left";
		public final static String TD_ALIGN_CENTER="center";
		public final static String TD_ALIGN_RIGHT="right";
	public final static String ELEMENT_TD_ATTRIBUTE_VALIGN="valign";
		public final static String TD_VALIGN_TOP="top";
		public final static String TD_VALIGN_MIDDLE="middle";
		public final static String TD_VALIGN_BOTTOM="bottom";
	public final static String ELEMENT_TD_ATTRIBUTE_COLSPAN="colspan";
	public final static String ELEMENT_TD_ATTRIBUTE_ROWSPAN="rowspan";
	public final static String ELEMENT_TD_ATTRIBUTE_SCOPE="scope";
		public final static String TD_SCOPE_COLGROUP="colgroup";

	//attributes for <tr>
	public final static String ELEMENT_TR_ATTRIBUTE_ALIGN="align";
		public final static String TR_ALIGN_LEFT="left";
		public final static String TR_ALIGN_CENTER="center";
		public final static String TR_ALIGN_RIGHT="right";
	public final static String ELEMENT_TR_ATTRIBUTE_VALIGN="valign";
		public final static String TR_VALIGN_TOP="top";
		public final static String TR_VALIGN_MIDDLE="middle";
		public final static String TR_VALIGN_BOTTOM="bottom";

		//attributes for <textarea>
	public final static String ELEMENT_TEXTAREA_ATTRIBUTE_COLS="cols";
	public final static String ELEMENT_TEXTAREA_ATTRIBUTE_ROWS="rows";
	public final static String ELEMENT_TEXTAREA_ATTRIBUTE_WRAP="wrap";
		public final static String TEXTAREA_WRAP_OFF="off";
		public final static String TEXTAREA_WRAP_HARD="hard";
		public final static String TEXTAREA_WRAP_SOFT="soft";
/*"hard" and "soft" apparently have more acceptance than "physical" and "virtual"; see http://msdn2.microsoft.com/en-us/library/ms535152.aspx and http://lists.evolt.org/archive/Week-of-Mon-19991101/091388.html
		public final static String TEXTAREA_WRAP_PHYSICAL="physical";
		public final static String TEXTAREA_WRAP_VIRTUAL="virtual";
*/
	public final static String ELEMENT_TEXTAREA_ATTRIBUTE_DISABLED="disabled";
		public final static String TEXTAREA_DISABLED_DISABLED="disabled";
	public final static String ELEMENT_TEXTAREA_ATTRIBUTE_READONLY="readonly";
		public final static String TEXTAREA_READONLY_READONLY="readonly";

	/**Creates a new unformatted default XHTML document with the required minimal structure,
	{@code <html><head><title></title<head><body></html>}, with no document type.
	@param title The title of the document.
	@param includeDocumentType Whether a document type should be added to the document.
	@param publicID The external subset public identifier, or <code>null</code> if there is no such identifier.
	@param systemID The external subset system identifier, or <code>null</code> if there is no such identifier.
	@return A newly created default generic XHTML document with the required minimal structure.
	@throws NullPointerException if the given title is <code>null</code>.
	@throws IllegalStateException if a document builder cannot be created which satisfies the configuration requested.
	@throws DOMException if there is an error creating the XHTML document.
	*/
	public static Document createXHTMLDocument(final String title) throws DOMException
	{
		return createXHTMLDocument(title, false);	//create an unformatted XHTML document with no document type
	}

	/**Creates a new default XHTML document with the required minimal structure,
	{@code <html><head><title></title<head><body></html>}, with no document type.
	@param title The title of the document.
	@param includeDocumentType Whether a document type should be added to the document.
	@param publicID The external subset public identifier, or <code>null</code> if there is no such identifier.
	@param systemID The external subset system identifier, or <code>null</code> if there is no such identifier.
	@param formatted <code>true</code> if the sections of the document should be formatted.
	@return A newly created default generic XHTML document with the required minimal structure.
	@throws NullPointerException if the given title is <code>null</code>.
	@throws IllegalStateException if a document builder cannot be created which satisfies the configuration requested.
	@throws DOMException if there is an error creating the XHTML document.
	*/
	public static Document createXHTMLDocument(final String title, final boolean formatted) throws DOMException
	{
		return createXHTMLDocument(title, false, null, null, formatted);	//create an XHTML document with no document type 
	}

	/**Creates a new unformatted default XHTML document with the required minimal structure,
	{@code <html><head><title></title<head><body></html>}, with an optional document type.
	@param title The title of the document.
	@param includeDocumentType Whether a document type should be added to the document.
	@param publicID The external subset public identifier, or <code>null</code> if there is no such identifier.
	@param systemID The external subset system identifier, or <code>null</code> if there is no such identifier.
	@return A newly created default generic XHTML document with the required minimal structure.
	@throws NullPointerException if the given title is <code>null</code>.
	@throws IllegalStateException if a document builder cannot be created which satisfies the configuration requested.
	@throws DOMException if there is an error creating the XHTML document.
	*/
	public static Document createXHTMLDocument(final String title, final boolean includeDocumentType, final String publicID, final String systemID) throws DOMException
	{
		return createXHTMLDocument(title, includeDocumentType, publicID, systemID, false);	//create an unformatted XHTML document
	}

	/**Creates a new default XHTML document with the required minimal structure,
	{@code <html><head><title></title<head><body></html>}, with an optional document type.
	@param title The title of the document.
	@param includeDocumentType Whether a document type should be added to the document.
	@param publicID The external subset public identifier, or <code>null</code> if there is no such identifier.
	@param systemID The external subset system identifier, or <code>null</code> if there is no such identifier.
	@param formatted <code>true</code> if the sections of the document should be formatted.
	@return A newly created default generic XHTML document with the required minimal structure.
	@throws IllegalStateException if a document builder cannot be created which satisfies the configuration requested.
	@throws NullPointerException if the given title is <code>null</code>.
	@throws DOMException if there is an error creating the XHTML document.
	*/
	public static Document createXHTMLDocument(final String title, final boolean includeDocumentType, final String publicID, final String systemID, final boolean formatted) throws DOMException
	{
		final DocumentBuilder documentBuilder;
		try
		{
			documentBuilder=createDocumentBuilder(true);	//create a namespace-aware document builder
		}
		catch(final ParserConfigurationException parserConfigurationException)	//if there is no namespace-aware parser
		{
			throw new IllegalStateException(parserConfigurationException);
		}
		final DOMImplementation domImplementation=documentBuilder.getDOMImplementation();	//get the DOM implementation from the document builder
		final DocumentType documentType;	//the document type, if any
		if(includeDocumentType)	//if we should add a document type
		{
			documentType=domImplementation.createDocumentType(ELEMENT_HTML, publicID, systemID);	//create an XHTML document, using the given identifiers, if any 
		}
		else	//if we should not add a document type
		{
			documentType=null;	//don't use a document type
		}
		final Document document=domImplementation.createDocument(XHTML_NAMESPACE_URI.toString(), ELEMENT_HTML, documentType);	//create an XHTML document
			//G***check about whether we need to add a <head> and <title>
		final Element htmlElement=document.getDocumentElement();	//get the html element
		if(formatted)	//if we should format the document
		{
			appendText(htmlElement, "\n");	//append a newline to start the content of the html element
		}
		final Element headElement=document.createElementNS(XHTML_NAMESPACE_URI.toString(), ELEMENT_HEAD);	//create the head element
		htmlElement.appendChild(headElement);	//add the head element to the html element
		if(formatted)	//if we should format the document
		{
			appendText(headElement, "\n");	//append a newline to start the content in the head
			appendText(htmlElement, "\n");	//append a newline to seaprate the content of the html element
		}
		final Element titleElement=document.createElementNS(XHTML_NAMESPACE_URI.toString(), ELEMENT_TITLE);	//create the title element
		headElement.appendChild(titleElement);	//add the title element to the head element
		appendText(titleElement, title);	//append the title text to the title element 
		final Element bodyElement=document.createElementNS(XHTML_NAMESPACE_URI.toString(), ELEMENT_BODY);	//create the body element
		htmlElement.appendChild(bodyElement);	//add the body element to the html element
		if(formatted)	//if we should format the document
		{
			appendText(bodyElement, "\n");	//append a newline to separate the information in the body
		}
		return document;  //return the document we created
	}
	/**Finds the XHTML <code>&lt;body&gt;</code> element.
	@param document The XHTML document tree.
	@return A reference to the <code>&lt;body&gt;</code> element, or
		<code>null</code> if there is such element.
	*/
	public static Element getBodyElement(final Document document)
	{
		return (Element)XPath.getNode(document, XPathConstants.LOCATION_STEP_SEPARATOR_CHAR+ELEMENT_BODY);
	}
	/**Finds the XHTML <code>&lt;head&gt;</code> element.
	@param document The XHTML document tree.
	@return A reference to the <code>&lt;head&gt;</code> element, or
		<code>null</code> if there is no such element.
	*/
	public static Element getHeadElement(final Document document)
	{
		return (Element)XPath.getNode(document, XPathConstants.LOCATION_STEP_SEPARATOR_CHAR+ELEMENT_HEAD);
	}
	/**Determines the reference to the image file represented by the element,
		assuming the element (an "img" or "object" element) does in fact represent
		an image.
		For "img", this return the "src" attribute. For objects, the value of the
		"data" attribute is returned.
	@param xhtmlNamespaceURI The XHTML namespace.
	@param element The element which contains the image information.
	@return The reference to the image file, or <code>null</code> if no reference
		could be found.
	*/
	public static String getImageElementHRef(final String xhtmlNamespaceURI, final Element element)
	{
		if(element!=null) //if a valid element is passed
		{
			final String namespaceURI=element.getNamespaceURI();  //get the element's namespace URI
				//if this element is in the correct namespace
			if((xhtmlNamespaceURI==null && namespaceURI==null) || namespaceURI.equals(xhtmlNamespaceURI))
			{
				final String elementName=element.getLocalName();  //get the element's name
					//see if this is an <img> or <object> element
				if(elementName.equals(ELEMENT_IMG)) //if the corresponding element is an img element
				{
						//get the src attribute, representing the href of the image, or null if not present
					return getDefinedAttributeNS(element, null, ELEMENT_IMG_ATTRIBUTE_SRC);
				}
				else if(elementName.equals(ELEMENT_OBJECT)) //if the corresponding element is an object element
				{
						//get the data attribute, representing the href of the image, or null if not present
					return getDefinedAttributeNS(element, null, ELEMENT_OBJECT_ATTRIBUTE_DATA);
				}
			}
		}
		return null;  //show that we couldn't find an image reference
	}
	/**Determines if the specified element represents a link, and if so attempts
		to find the link element's reference.
	@param xhtmlNamespaceURI The XHTML namespace.
	@param element The element which might represent a link.
	@return The reference of the link, or <code>null</code> if the specified
		element does not represent a link or has no reference.
	*/
	public static String getLinkElementHRef(final String xhtmlNamespaceURI, final Element element)  //G***fix to call XLink and see if this is an XLink link
	{
		if(element!=null) //if a valid element was passed
		{
			final String namespaceURI=element.getNamespaceURI();  //get the element's namespace URI
				//if this element is in the correct namespace
			if(Objects.equals(xhtmlNamespaceURI, namespaceURI))
			{
				final String elementName=element.getLocalName();  //get the element's name
				if(elementName.equals(ELEMENT_A))	//if this is an <a> element
				{
					return getDefinedAttributeNS(element, null, ELEMENT_A_ATTRIBUTE_HREF);  //return the value of the href attribute, if there is one
				}
			}
		}
		return null; //show that we couldn't find an href or this wasn't even a link
	}
	/**Checks to see if the given element contains a <code>style</code> attribute,
		and if so returns a style declaration containing the given style properties.
	@param element The element to check for a style
	@return A style declaration representing the style in the style attribute, or
		<code>null</code> if there is no style in the style attribute.
	*/
	public static CSSStyleDeclaration getLocalStyle(final Element element)
	{
		final String styleValue=element.getAttributeNS(null, ATTRIBUTE_STYLE); //get the value of the style attribute
		if(styleValue.length()!=0)  //if there is a style value
		{
			final XMLCSSProcessor cssProcessor=new XMLCSSProcessor();	//create a new CSS processor G***make one for the entire tidier object, if we can -- don't create it locally
			final XMLCSSStyleDeclaration style=new XMLCSSStyleDeclaration(); //create a new style declaration
			try
			{
				final ParseReader styleReader=new ParseReader(styleValue, "Element "+element.getNodeName()+" Local Style");	//create a string reader from the value of this local style attribute G***i18n
				cssProcessor.parseRuleSet(styleReader, style); //read the style into our style declaration
				return style; //return the style
			}
			catch(IOException e)  //if we have any errors reading the style
			{
			  return null;  //show that we didn't understand the style
			}
		}
		return null;  //show that we couldn't find a style
	}
	/**Determines if the given content type is one representing HTML in some form.
	<p>HTML content types include:</p>
	<ul>
		<li><code>text/html</code></li>
		<li><code>application/xhtml+xml</code></li>
		<li><code>application/xhtml+xml-external-parsed-entity</code> (not formally defined)</li>
		<li><code>text/x-oeb1-document</code></li>
	</ul>
	@param contentType The content type of a resource, or <code>null</code> for no
		content type.
	@return <code>true</code> if the given media type is one of several HTML
		media types.
	*/
	public static boolean isHTML(final ContentType contentType)	//TODO maybe move this to an HTMLUtilities
	{
		if(contentType!=null)	//if a media type is given
		{
			final String primaryType=contentType.getPrimaryType();	//get the primary type
			final String subType=contentType.getSubType();	//get the sub type
			if(TEXT.equals(primaryType))	//if this is "text/?"
			{
				if(HTML_SUBTYPE.equals(subType)		//if this is "text/html"
						|| X_OEB1_DOCUMENT_SUBTYPE.equals(subType))	//if this is "text/x-oeb1-document"
				{
					return true;	//show that this is HTML
				}
			}
			if(APPLICATION.equals(primaryType))	//if this is "application/?"
			{
					//TODO probably add a parameter to specify whether we should allow fragments to qualify as XHTML---right now this behavior is not consistent with XMLUtilities.isXML(), which doesn't recognize fragments as XML
				if(XHTML_XML_SUBTYPE.equals(subType) || XHTML_XML_EXTERNAL_PARSED_ENTITY_SUBTYPE.equals(subType))		//if this is "application/xhtml+xml" or "application/xhtml+xml-external-parsed-entity"
				{
					return true;	//show that this is HTML
				}
			}
		}
		return false;	//this is not a media type we recognize as being HTML
	}
	/**Determines if the given URI represents one of the HTML XML namespaces.
	<p>HTML namespaces include:</p>
	<ul>
		<li><code>http://www.w3.org/1999/xhtml</code></li>
		<li><code>http://openebook.org/namespaces/oeb-document/1.0/</code></li>
	</ul>
	@param namespaceURI A URI representing an XML namespace, or <code>null</code>
		for no namespace.
	@return <code>true</code> if the given URI represents one of the HTML XML
		namespaces.
	*/
	public static boolean isHTMLNamespaceURI(final URI namespaceURI)
	{
		if(namespaceURI!=null)  //if the namespace URI is valid
		{
				//if it's part of the XHTML or OEB namespace
			if(XHTML_NAMESPACE_URI.equals(namespaceURI)
					|| OEBConstants.OEB1_DOCUMENT_NAMESPACE_URI.equals(namespaceURI))
				return true;  //show that this is an HTML namespace
		}
		return false;	//show that we didn't recognize the namespace as HTML
	}
	/**Determines if the specified element represents an image. Specifically, this
		returns <code>true</code> if the element's name is "img"; or if the
		element's name is "object" and the type attribute is an image or the data
		attribute references an image file.
	@param xhtmlNamespaceURI The XHTML namespace.
	@param element The element which might represent an image.
	@return <code>true</code> if the specified element represents an image.
	*/
	public static boolean isImageElement(final String xhtmlNamespaceURI, final Element element)
	{
		if(element!=null) //if a valid element was passed
		{
			final String namespaceURI=element.getNamespaceURI();  //get the element's namespace URI
				//if this element is in the correct namespace
			if(Objects.equals(xhtmlNamespaceURI, namespaceURI))
			{
	//G***del Debug.trace("XHTMLUtilities.isImageElement() looking at element: "+element.getNodeName()+" namespace: "+element.getNamespaceURI());  //G**del
	//G***del if(element.getNodeName().equals("param")) //G***del; testing
	//G***del 	XMLUtilities.printTree(element, Debug.getOutput());


	//G***fix				final String elementName=element.getLocalName();  //get the element's name
				final String elementName=element.getLocalName();  //get the element's name
				if(elementName.equals(ELEMENT_IMG))	//if this is an <img> element
					return true;  //show that this is an image object
				else if(elementName.equals(ELEMENT_OBJECT)) //if this is an <object> element
				{
						//see if there is a type attribute
					if(element.hasAttributeNS(null, ELEMENT_OBJECT_ATTRIBUTE_TYPE)) //if there is a type attribute
					{
						final String type=element.getAttributeNS(null, ELEMENT_OBJECT_ATTRIBUTE_TYPE);  //get the type
						final ContentType mediaType=ContentTypes.createContentType(type); //create a media type from the given type
						if(mediaType.getPrimaryType().equals(ContentTypeConstants.IMAGE)) //if this is an image
							return true;  //show that this is an image object
					}
						//see if there is a data attribute, since there is no type specified
					if(element.hasAttributeNS(null, ELEMENT_OBJECT_ATTRIBUTE_DATA)) //if there is a data attribute
					{
						final String data=element.getAttributeNS(null, ELEMENT_OBJECT_ATTRIBUTE_DATA);  //get the data
						final ContentType mediaType=Files.getContentType(new File(data)); //try to get a media type from the file
						if(mediaType!=null && mediaType.getPrimaryType().equals(ContentTypeConstants.IMAGE)) //if this is an image
							return true;  //show that this is an image object
					}
				}
			}
		}
		return false; //this does not appear to be an image element
	}
	/**Determines if the specified element represents a link. Specifically, this
		returns <code>true</code> if the element's name is "a".
	@param xhtmlNamespaceURI The XHTML namespace.
	@param element The element which might represent a link.
	@return <code>true</code> if the specified element represents a link.
	*/
	public static boolean isLinkElement(final String xhtmlNamespaceURI, final Element element)  //G***fix to call XLink and see if this is an XLink link
	{
		if(element!=null) //if a valid element was passed
		{
			final String namespaceURI=element.getNamespaceURI();  //get the element's namespace URI
				//if this element is in the correct namespace
			if(Objects.equals(xhtmlNamespaceURI, namespaceURI))
			{
				final String elementName=element.getLocalName();  //get the element's name
				if(elementName.equals(ELEMENT_A))	//if this is an <a> element
					return true;  //show that this is a link element
			}
		}
		return false; //this does not appear to be a link element
	}
	/**Determines if the specified element represents an empty element&mdash;an
		element that might be declared as <code>EMPTY</code> in a DTD.
	@param namespaceURI The element namespace.
	@param localName The local name of the element.
	@return <code>true</code> if the specified element should be empty.
	*/
	public static boolean isEmptyElement(final URI namespaceURI, final String localName)
	{
			//TODO make this static---placed here because it was causing a compile error due to an Eclipse bug
		final String[] EMPTY_ELEMENT_LOCAL_NAMES=new String[]{ELEMENT_BASE, ELEMENT_META, ELEMENT_LINK, ELEMENT_HR, ELEMENT_BR, ELEMENT_PARAM, ELEMENT_IMG, ELEMENT_AREA, ELEMENT_INPUT, ELEMENT_COL};	//TODO probably put these in a better place, either in a hash set for faster lookup or put this constant in XHTMLConstants
		if(localName!=null && (namespaceURI==null || isHTMLNamespaceURI(namespaceURI)))	//if this element has an HTML namespace or no namespace
		{
			return ArrayUtilities.indexOf(EMPTY_ELEMENT_LOCAL_NAMES, localName)>=0;	//return whether the local name is one of the empty element local names
		}
		return false; //this does not appear to be an empty element
	}
	/**Determines if the specified element represents a link, and if so sets the
		link element's reference.
	@param xhtmlNamespaceURI The XHTML namespace.
	@param element The element which represents a link.
	@param href The new reference to add to the link.
	*/
	public static void setLinkElementHRef(final String xhtmlNamespaceURI, final Element element, final String href)  //G***fix to call XLink and see if this is an XLink link
	{
		if(element!=null) //if a valid element was passed
		{
			final String namespaceURI=element.getNamespaceURI();  //get the element's namespace URI
				//if this element is in the correct namespace
			if(Objects.equals(xhtmlNamespaceURI, namespaceURI))
			{
				final String elementName=element.getLocalName();  //get the element's name
				if(elementName.equals(ELEMENT_A))	//if this is an <a> element
				{
					element.setAttributeNS(null, ELEMENT_A_ATTRIBUTE_HREF, href);  //set the href attribute G***what if they were using an attribute prefix?
				}
			}
		}
	}

}
