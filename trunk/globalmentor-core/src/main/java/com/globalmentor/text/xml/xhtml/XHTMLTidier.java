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

package com.globalmentor.text.xml.xhtml;

import java.io.*;
import java.util.*;
import java.util.Arrays;

import com.globalmentor.io.Files;
import com.globalmentor.java.*;
import com.globalmentor.log.Log;
import com.globalmentor.net.ContentType;
import com.globalmentor.oebps.spec.OEB;
import com.globalmentor.text.Prose;
import com.globalmentor.text.Unicode;
import com.globalmentor.text.css.CSSSerializer;
import com.globalmentor.text.css.CSSTidier;
import com.globalmentor.text.xml.XML;
import com.globalmentor.text.xml.xpath.XPath;
import com.globalmentor.util.*;
import com.globalmentor.w3c.spec.CSS;

import static com.globalmentor.java.Characters.*;
import static com.globalmentor.net.ContentTypeConstants.*;
import static com.globalmentor.text.xml.XML.*;
import static com.globalmentor.text.xml.xhtml.XHTML.*;
import static com.globalmentor.w3c.spec.CSS.*;

import org.w3c.dom.*;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.css.*;

/**
 * Contains several routines for tidying XHTML documents.
 * <p>
 * <code>tidy()</code> performs the following operations:
 * </p>
 * <ul>
 * <li>Converts <code>&lt;img&gt;</code> elements to <code>&lt;object&gt;</code> elements.</li>
 * <li>Converts <code>&lt;applet&gt;</code> elements to <code>&lt;object&gt;</code> elements.</li>
 * <li>Trims beginning whitespace (including non-breaking spaces) in block elements that are meant to contain text (e.g. <code>&lt;p&gt;</code>) or that contain
 * no block child elements.</li>
 * <li>Collapses all whitespace between elements to double linefeeds in the root <code>&lt;html&gt;</code> element, the <code>&lt;head&gt;</code> element, and
 * the <code>&lt;body&gt;</code> element.</li>
 * <li>Converts characters in an element requested the symbol font to Unicode equivalents.</li>
 * <li>Performs any transformation requested by the <code>text-transform</code> CSS property on immediate child nodes.</li>
 * <li>Converts text with list-like content to either ordered or unordered lists (code>&lt;ol&gt;</code> and code>&lt;ul&gt;</code>).</li>
 * <li>Groups text with equal left and right margins inside <code>&lt;div class="fullIndent"&gt;</code> elements.</li>
 * </ul>
 * <p>
 * Attributes are modified in the following manner:
 * </p>
 * <ul>
 * <li>Changes a "name" attribute to an "id" attribute, if the element has no "id" attribute.</li>
 * <li>Changes any "lang" attribute to "xml:lang".</li>
 * </ul>
 * <p>
 * The following characters are replaced:
 * </p>
 * <ul>
 * <li>0x91 replaced with a Unicode left single quote character, 0x2018.</li>
 * <li>0x92 replaced with a Unicode right single quote character, 0x2019.</li>
 * <li>0x93 replaced with a Unicode left double quote character, 0x201C.</li>
 * <li>0x94 replaced with a Unicode right double quote character, 0x201D.</li>
 * <li>0x96 replaced with a Unicode n dash character, 0x2013.</li>
 * <li>0x97 replaced with a Unicode m dash character, 0x2014.</li>
 * <li>0x99 replaced with a Unicode trademark character, 0x2122.</li>
 * <li>0x9C replaced with a Unicode Latin small ligature oe, 0x0153.</li>
 * <li>0x8C replaced with a Unicode Latin uppercase ligature OE, 0x0152.</li>
 * <li>0x85 replaced with a Unicode horizontal ellipsis, 0x2026.</li>
 * <li>0x9F replaced with a Unicode uppercase Y with diaeresis, 0x0178.</li>
 * //TODO del if not needed
 * <li>0x00A0 non-breaking space replaced with a space, 0x0020.</li>
 * <li>Runs of "--", "---", and "----" replaced with an m dash character, 0x2014.</li>
 * </ul>
 * <p>
 * The following elements and attributes are removed:
 * </p>
 * <ul>
 * <li>Elements from other namespaces.</li>
 * <li>Empty elements besides <code>&lt;applet&gt;</code>, <code>&lt;img&gt;</code>, <code>&lt;object&gt;</code>, and <code>&lt;param&gt;</code>.</li>
 * <li>Block elements that contain only whitespace.</li>
 * <li><code>&lt;style&gt;</code> elements.</li>
 * <li><code>&lt;style&gt;</code> attributes.</li>
 * <li><code>&lt;span&gt;</code> elements that have no attributes.</li>
 * <li><code>&lt;table&gt;</code> elements that contain only one row and only one cell.</li>
 * </ul>
 * <p>
 * The following elements and attributes are "excised" by removing the elements but promoting the child elements to be children of the removed element's parent:
 * </p>
 * <ul>
 * <li>Text containers (e.g. <code>&lt;p&gt;</code>, <code>&lt;span&gt;</code>, <code>&lt;em&gt;</code>) with only whitespace or non-breaking-space text
 * content.</li>
 * <li><code>&lt;span&gt;</code> elements which contain all the content of their parent element (that is, a span that is the only child of an element).</li>
 * <li>Lists (<code>&lt;ol&gt;</code> and <code>&lt;ul&gt;</code>) that contain a single list child element of the same name (duplicated nested lists).</li>
 * <li><code>&lt;div&gt;</code> elements that are the only element children of <code>&lt;body&gt;</code>.</li>
 * //TODO decide
 * <li><code>&lt;div&gt;</code> elements that contain only whitespace and empty elements (such as <code>&lt;hr&gt;</code>).</li>
 * </ul>
 * <p>
 * If Microsoft tidying is enabled, the the following operations are performed:
 * </p>
 * <ul>
 * <li>Removes comments that begin with <code>[if</code> and <code>[endif</code>.</li>
 * <li>Removes empty elements besides <code>&lt;applet&gt;</code>, <code>&lt;img&gt;</code>, and <code>&lt;object&gt;</code>.</li>
 * <li>Removes the "MsoNormal" class attribute value.</li>
 * <li>Changes the "MsoCaption" class attribute value to "caption".</li>
 * <li>Removes any class attribute value that is the same as the element name.</li>
 * <li>Converts an element with the "MsoBlockText" or "MsoBodyTextIndent" class attribute value to a <code>&lt;blockquote&gt;</code> element.</li>
 * <li>Changes the "MsoTitle" class attribute value to "title". The element name is changed to <code>&lt;h1&gt;</code> if it isn't already.</li>
 * </ul>
 * <p>
 * If Unicode tidying is enabled, the the following conversions are performed:
 * </p>
 * <ul>
 * <li>Changes every Unicode left single quote character, 0x2018, to "'"</li>
 * <li>Changes every Unicode right single quote character, 0x2019, to "'".</li>
 * <li>Changes every Unicode left double quote character, 0x201C, to "\"".</li>
 * <li>Changes every Unicode right double quote character, 0x201D, to "\"".</li>
 * <li>Changes every Unicode n dash character, 0x2013 to "-".</li>
 * <li>Changes every Unicode m dash character, 0x2014 to "-".</li>
 * <li>Changes every Unicode trademark character, 0x2122 to "(TM)".</li>
 * </ul>
 * <p>
 * Options:
 * </p>
 * <ul>
 * <li><code>CONVERT_UNDERLINE_ITALICS_OPTION</code>: Converts all underline tags (<code>&lt;u&gt;</code>) should be converted to italics tags (
 * <code>&lt;i&gt;</code>).</li>
 * <li><code>EXTRACT_INTERNAL_STYLESHEETS_OPTION</code>: Extracts internal stylesheets and stores them as external ones.
 * <li><code>NORMALIZE_STYLE_CLASSES_OPTION</code>:
 * <ul>
 * <li>If the class attribute contains "num" and at least "chap" or "title", change the class attribute to "chapterNumber". The element name is changed to
 * <code>&lt;h1&gt;</code> if it isn't already.</li>
 * </ul>
 * </li>
 * </ul>
 * @author Garret Wilson TODO added stuff not commented: removed <html> attributes added OEB DOCTYPe
 */
public class XHTMLTidier {

	/**
	 * Whether underline tags (<code>&lt;u&gt;</code>) should be converted to italics tags (<code>&lt;i&gt;</code>).
	 */
	public static final String CONVERT_UNDERLINE_ITALICS_OPTION = "convertUnderlineItalics";

	/** Default to not converting underline to italics. */
	public static final boolean CONVERT_UNDERLINE_ITALICS_OPTION_DEFAULT = false;

	/**
	 * Whether internal stylesheets should be extracted and stored as external stylesheets.
	 */
	public static final String EXTRACT_INTERNAL_STYLESHEETS_OPTION = "extractInternalStylesheets";

	/** Default to not extracting internal stylesheets. */
	public static final boolean EXTRACT_INTERNAL_STYLESHEETS_OPTION_DEFAULT = false;

	/** Whether style class names should be made consistent. */
	public static final String NORMALIZE_STYLE_CLASSES_OPTION = "normalizeStyleClasses";

	/** Default to normalizing style classes. */
	public static final boolean NORMALIZE_STYLE_CLASSES_OPTION_DEFAULT = true;

	/**
	 * Whether underline tags (<code>&lt;u&gt;</code>) should be converted to italics tags (<code>&lt;i&gt;</code>).
	 */
	private boolean convertUnderlineItalics = CONVERT_UNDERLINE_ITALICS_OPTION_DEFAULT;

	/**
	 * @return Whether underline tags (<code>&lt;u&gt;</code>) should be converted to italics tags (<code>&lt;i&gt;</code>).
	 */
	public boolean isConvertUnderlineItalics() {
		return convertUnderlineItalics;
	}

	/**
	 * Sets whether underlining should be converted to italics.
	 * @param newConvertUnderlineItalics <code>true</code> if underline tags (<code>&lt;u&gt;</code>) should be converted to italics tags (<code>&lt;i&gt;</code>
	 *          ), else <code>false</code>.
	 */
	public void setConvertUnderlineItalics(final boolean newConvertUnderlineItalics) {
		convertUnderlineItalics = newConvertUnderlineItalics;
	}

	/**
	 * Whether internal stylesheets should be extracted and stored as external stylesheets.
	 */
	private boolean extractInternalStylesheets = EXTRACT_INTERNAL_STYLESHEETS_OPTION_DEFAULT;

	/**
	 * @return Whether internal stylesheets should be extracted and stored as external stylesheets.
	 */
	public boolean isExtractInternalStylesheets() {
		return extractInternalStylesheets;
	}

	/**
	 * Sets whether internal stylesheets should be extracted and stored as external stylesheets.
	 * @param newExtractInternalStylesheets <code>true</code> if internal stylesheets should be extracted and stored as external stylesheets.
	 */
	public void setExtractInternalStylesheets(final boolean newExtractInternalStylesheets) {
		extractInternalStylesheets = newExtractInternalStylesheets;
	}

	/** Whether style class names should be made consistent. */
	private boolean normalizeStyleClasses = NORMALIZE_STYLE_CLASSES_OPTION_DEFAULT;

	/** @return Whether style class names should be made consistent. */
	public boolean isNormalizeStyleClasses() {
		return normalizeStyleClasses;
	}

	/**
	 * Sets whether style class names should be made consistent.
	 * @param newNormalizeStyleClasses <code>true</code> if the style classes should be normalized to consistent values, else <code>false</code>.
	 */
	public void setNormalizeStyleClasses(final boolean newNormalizeStyleClasses) {
		normalizeStyleClasses = newNormalizeStyleClasses;
	}

	//Microsoft Office style classes

	/** Microsoft Office caption class. */
	public static final String MSO_CAPTION_CLASS = "MsoCaption";

	/** Microsoft Office normal class. */
	public static final String MSO_NORMAL_CLASS = "MsoNormal";

	/** Microsoft Office block text class. */
	public static final String MSO_BLOCK_TEXT_CLASS = "MsoBlockText";

	/** Microsoft Office indented body text class. */
	public static final String MSO_BODY_TEXT_INDENT_CLASS = "MsoBodyTextIndent";

	/** Microsoft Office title class. */
	public static final String MSO_TITLE_CLASS = "MsoTitle";

	//Tidy style classes

	/** The normal caption class. */
	public static final String CAPTION_CLASS = "caption";

	/** The normal chapter number class. */
	public static final String CHAPTER_NUMBER_CLASS = "chapterNumber";

	/** The normal title class. */
	public static final String TITLE_CLASS = "title";

	/** A substring "chap" which might appear in a chapter title class. */
	public static final String CHAPTER_TITLE_CLASS_CHAP_SUBSTRING = "chap"; //TODO shouldn't this be chapterNumber?

	/** A substring "num" which might appear in a chapter title class. */
	public static final String CHAPTER_TITLE_CLASS_NUM_SUBSTRING = "num";

	/** A substring "title" which might appear in a chapter title class. */
	public static final String CHAPTER_TITLE_CLASS_TITLE_SUBSTRING = "title";

	//TODO probably pass a namespace and check all the elements against the namespace

	/** Whether Microsoft-specific items should be tidied. */
	private static final boolean tidyMicrosoft = true;

	/**
	 * Whether some uncommon Unicode characters should be converted to more common representations, such as mdash to a hyphen.
	 */
	//TODO fix for option only private static final boolean tidyUnicode=false; //TODO fix

	//characters to be converted TODO probably put these in some constant Unicode file

	/** An incorrect ellipsis. */
	protected static final char BAD_ELLIPSIS = 0x85;

	/** An incorrect OE. */
	protected static final char BAD_UPPERCASE_OE = 0x8C;

	/** An incorrect left single quote. */
	protected static final char BAD_LEFT_SINGLE_QUOTE = 0x91;

	/** An incorrect right single quote. */
	protected static final char BAD_RIGHT_SINGLE_QUOTE = 0x92;

	/** An incorrect left double quote. */
	protected static final char BAD_LEFT_DOUBLE_QUOTE = 0x93;

	/** An incorrect right double quote. */
	protected static final char BAD_RIGHT_DOUBLE_QUOTE = 0x94;

	/** An incorrect m dash. */
	protected static final char BAD_N_DASH = 0x96;

	/** An incorrect m dash. */
	protected static final char BAD_M_DASH = 0x97;

	/** An incorrect trademark. */
	protected static final char BAD_TRADEMARK = 0x99;

	/** An incorrect oe. */
	protected static final char BAD_LOWERCASE_OE = 0x9C;

	/** An incorrect Y umlaut. */
	protected static final char BAD_UPPERCASE_Y_UMLAUT = 0x9F;

	/** Characters to be replaced, along with their replacements. */
	//TODO move these up after moving the match character references (above) elsewhere
	protected static final char[][] CHARACTER_MATCH_REPLACE_SET_ARRAY = new char[][] { { BAD_LEFT_SINGLE_QUOTE, LEFT_SINGLE_QUOTATION_MARK_CHAR },
			{ BAD_RIGHT_SINGLE_QUOTE, RIGHT_SINGLE_QUOTATION_MARK_CHAR }, { BAD_LEFT_DOUBLE_QUOTE, LEFT_DOUBLE_QUOTATION_MARK_CHAR },
			{ BAD_RIGHT_DOUBLE_QUOTE, RIGHT_DOUBLE_QUOTATION_MARK_CHAR }, { BAD_N_DASH, EN_DASH_CHAR }, { BAD_M_DASH, EM_DASH_CHAR },
			{ BAD_TRADEMARK, TRADE_MARK_SIGN_CHAR }, { BAD_LOWERCASE_OE, LATIN_SMALL_LIGATURE_OE_CHAR }, { BAD_UPPERCASE_OE, LATIN_CAPITAL_LIGATURE_OE_CHAR },
			{ BAD_ELLIPSIS, HORIZONTAL_ELLIPSIS_CHAR }, { BAD_UPPERCASE_Y_UMLAUT, LATIN_CAPITAL_LETTER_Y_WITH_DIAERESIS_CHAR }
	//TODO del if not needed					{NO_BREAK_SPACE_CHAR, SPACE_CHAR}
	};

	/** Our object to tidy CSS, should we ever need to use it. */
	protected final CSSTidier cssTidier = new CSSTidier();

	/**
	 * Sets the options based on the contents of the option properties.
	 * @param options The properties which contain the options.
	 */
	public void setOptions(final Properties options) {
		setConvertUnderlineItalics(PropertiesUtilities.getBooleanProperty(options, CONVERT_UNDERLINE_ITALICS_OPTION, CONVERT_UNDERLINE_ITALICS_OPTION_DEFAULT));
		setExtractInternalStylesheets(PropertiesUtilities.getBooleanProperty(options, EXTRACT_INTERNAL_STYLESHEETS_OPTION,
				EXTRACT_INTERNAL_STYLESHEETS_OPTION_DEFAULT));
		setNormalizeStyleClasses(PropertiesUtilities.getBooleanProperty(options, NORMALIZE_STYLE_CLASSES_OPTION, NORMALIZE_STYLE_CLASSES_OPTION_DEFAULT));
		cssTidier.setOptions(options); //pass the options along to the CSS tidier
	}

	/** The title of the work. */
	private String title = null;

	/**
	 * @return The title of the work, or <code>null</code> if the title is not known.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the title of the work.
	 * @param newTitle The title of the work.
	 */
	public void setTitle(final String newTitle) {
		title = newTitle;
	}

	/** Default constructor initialized with the default options. */
	public XHTMLTidier() {
	}

	/**
	 * Constructs an XHTML tidier with a list of options.
	 * @param options The properties which specify the tidy options.
	 */
	public XHTMLTidier(final Properties options) {
		setOptions(options); //set the options from the properties
	}

	/**
	 * Tidies a document assumed to be XHTML. The entire document hierarchy is first tidied, using any locally declared styles if necessary. Then the hierarchy is
	 * tidied again for style, removing any unnecessary local style declarations.
	 * @param document The document to tidy.
	 */
	public void tidy(final Document document) {
		tidy(document, null); //tidy the document without a context file
	}

	/**
	 * Tidies a document assumed to be XHTML. The entire document hierarchy is first tidied, using any locally declared styles if necessary. Then the hierarchy is
	 * tidied again for style, removing any unnecessary local style declarations. A context file can be specified for several options which require a destination
	 * directory, such as extracting styles.
	 * @param document The document to tidy.
	 * @param contextFile The file that serves as a context for other generated files; usually is the filename of the original document being tidied.
	 */
	public void tidy(final Document document, final File contextFile) {
		/*TODO fix
							final XMLCSSProcessor cssProcessor=new XMLCSSProcessor();	//create a new CSS processor
							cssProcessor.parseStyles(xmlDocument, publicationURL);	//parse this document's styles
									//TODO check to make sure the styles are valid OEB styles somewhere here
							cssProcessor.applyxStyles(xmlDocument);	//apply the styles
		*/
		document.normalize(); //normalize the document so that multiple subsequent text nodes will be combined (important for finding enclosing characters, for instance)
		if(isExtractInternalStylesheets()) { //if we should extract internal stylesheets
			throw new UnsupportedOperationException("internal stylesheets not currently supported");
			/*TODO fix with general stylesheet processor implementation
			try {
				final XMLCSSProcessor cssProcessor = new XMLCSSProcessor(); //create a new CSS processor
				cssProcessor.parseInternalStyleSheets((com.globalmentor.text.xml.processor.XMLDocument)document); //parse the internal stylesheets TODO this needs to be fixed so that it doesn't store stylesheets in the document
				if(((com.globalmentor.text.xml.processor.XMLDocument)document).getStyleSheetList().size() > 0) { //if there are internal stylesheets TODO this is terrible; fix
					if(((com.globalmentor.text.xml.processor.XMLDocument)document).getStyleSheetList().get(0) instanceof CSSStyleSheet) { //if the first stylesheet is a CSS stylesheet
						final CSSStyleSheet cssStyleSheet = (CSSStyleSheet)((com.globalmentor.text.xml.processor.XMLDocument)document).getStyleSheetList().get(0); //get a reference to this CSS stylesheet
						cssTidier.tidy(cssStyleSheet); //tidy the stylesheet
						if(contextFile != null) { //if we have a context file
							final File stylesheetFile = new File(Files.changeExtension(contextFile, "css").getCanonicalPath()); //create a file with a .css extension TODO use a constant here
							//create an output file stream for writing the stylesheet
							final OutputStream stylesheetOutputStream = new BufferedOutputStream(new FileOutputStream(stylesheetFile));
							try {
								new CSSSerializer().serialize(cssStyleSheet, stylesheetOutputStream); //write the stylesheet out to the file TODO use a pre-created serializer, maybe
								stylesheetOutputStream.flush(); //flush the output stream
								addStyleSheetReference(document, stylesheetFile.getName(), OEB.OEB10_CSS_MEDIA_TYPE); //add the stylesheet to the document TODO eventually change to text/css

							} finally {
								stylesheetOutputStream.close(); //always close the file
							}
						}
					}
				}
			} catch(Exception exception) { //if any exception occurs
				Log.warn(exception); //warn and move on
			}
			*/
		}
		final Element documentElement = document.getDocumentElement(); //get the root element
		tidy(documentElement); //tidy the root element and all elements beneath it
		tidyStyle(documentElement); //now that we've tidied the document, tidy all styles, removing unneeded ones
		collapseChildWhitespaceLF(document.getDocumentElement()); //change whitespace to LFs in the root element
		//get a reference to the head element, if there is one
		final Element headElement = (Element)XPath.getNode(document, XPath.LOCATION_STEP_SEPARATOR_CHAR + ELEMENT_HEAD);
		if(headElement != null) //if there is a head element
			collapseChildWhitespaceLF(headElement); //collapse whitespace to LFs in the head element
		//get a reference to the body element
		final Element bodyElement = (Element)XPath.getNode(document, XPath.LOCATION_STEP_SEPARATOR_CHAR + ELEMENT_BODY);
		if(bodyElement != null) //if there is a body element (there always should be one)
			collapseChildWhitespaceLF(bodyElement); //collapse whitespace to LFs in the body element
	}

	//TODO testing; comment; rename
	public static void collapseChildWhitespaceLF(final Node node) {
		node.normalize(); //make sure the node is normalized
		final NodeList childNodes = node.getChildNodes(); //get the list of child nodes
		final int childNodeCount = childNodes.getLength(); //find out how many child nodes there are
		for(int i = 0; i < childNodeCount; ++i) { //look at each child node
			final Node childNode = childNodes.item(i); //get a reference to this node
			if(childNode.getNodeType() == Node.TEXT_NODE) { //if this is a text node
				final Text text = (Text)childNode; //cast the child to a text element
				final String data = text.getData(); //get the text data
				//TODO testing; comment; use StringBufferUtilities version, maybe
				final String collapsedData = Strings.collapse(data, Characters.TRIM_CHARACTERS, "\n\n");
				text.setData(collapsedData); //replace the text data with the collapsed data
			}
		}
	}

	/**
	 * Tidies a node of an XHTML document by first tidying all child nodes, then tidying the node based upon its node type. More specifically, these steps are:
	 * <ol>
	 * <li>Tidy any attributes.</li>
	 * <li>Tidy individual child nodes.</li>
	 * <li>Tidy current node.</li>
	 * <li>If this is a block element, tidy child node groups.</li>
	 * </ol>
	 * @param node The node to tidy. //TODO del if not needed @return The tidied node, which is the original node or a new node resulting //TODO del if not needed
	 *          from tidying the node.
	 */
	protected void tidy(final Node node) {
		final short nodeType = node.getNodeType(); //get the node's type
		if(nodeType == Node.ELEMENT_NODE) //if this is an element
			tidyAttributes((Element)node); //tidy the attributes of the element, if needed
		//TODO check to make sure the element is in the correct namespace here
		tidyChildNodes(node); //tidy the child nodes of this node
		switch(nodeType) { //see which type of node this is
			case Node.ELEMENT_NODE: //if this is an element node
			{
				Element element = (Element)node; //cast this node to an element
				element = tidyElement(element); //tidy the node and see if we get another element
				if(isBlockElement(element)) //if this is a block element
					tidyChildGroups(element); //tidy any groups of child nodes that have special meaning
			}
				break;
			case Node.TEXT_NODE: //if this is a text node
			{
				final Text textNode = (Text)node; //cast the node to a text node
				tidyText(textNode); //tidy the text node
			}
				break;
		}
	}

	/**
	 * Tidies the style of an element of an XHTML document by first tidying the styles of all child nodes, then tidying the style of the element itself.
	 * @param element The element of which local style declarations should be tidied.
	 */
	protected void tidyStyle(final Element element) {
		final NodeList childNodeList = element.getChildNodes(); //get a list of child nodes
		final int childNodeCount = childNodeList.getLength(); //find out how many child nodes there are
		//tidy the styles of all the children, first.
		for(int i = 0; i < childNodeList.getLength(); ++i) { //look at each child node
			final Node childNode = childNodeList.item(i); //get a reference to this child node
			if(childNode.getNodeType() == childNode.ELEMENT_NODE) { //if this is an element
				tidyStyle((Element)childNode); //tidy the style of this child element
			}
		}
		/*TODO fix with general style processor implementation
		//get the element's style TODO later remove the cast when XMLCSSStyleDeclaration implements CSS2Properties
		final XMLCSSStyleDeclaration oldStyle = (XMLCSSStyleDeclaration)XMLCSSProcessor.getLocalHTMLStyle(element);
		if(oldStyle != null) { //if there is an existing style
			final XMLCSSStyleDeclaration newStyle = new XMLCSSStyleDeclaration(); //create a new style declaration to received the tidied styles
			//TODO automate this style copying
			final CSSValue colorValue = oldStyle.getPropertyCSSValue(CSS_PROP_COLOR); //TODO testing -- do we want to copy the color, too?
			if(colorValue != null) //if we have a background set
				newStyle.setPropertyCSSValue(CSS_PROP_COLOR, colorValue);
			final CSSValue backgroundColorValue = oldStyle.getPropertyCSSValue(CSS_PROP_BACKGROUND_COLOR);
			if(backgroundColorValue != null) //if we have a background set
				newStyle.setPropertyCSSValue(CSS_PROP_BACKGROUND_COLOR, backgroundColorValue);
			final CSSValue listStyleTypeValue = oldStyle.getPropertyCSSValue(CSS_PROP_LIST_STYLE_TYPE);
			if(listStyleTypeValue != null) //if we have a list style type set
				newStyle.setPropertyCSSValue(CSS_PROP_LIST_STYLE_TYPE, listStyleTypeValue);
			//TODO bring over other style properties
			//TODO fix ((XMLElement)element).setLocalCSSStyle(style);  //set the element's style to whatever we constructed TODO eventually use a separate style tree instead of the element itself
			final String newStyleValue = newStyle.getCssText().trim(); //get the new style
			if(newStyleValue.length() > 0) //if we carried any styles over
				element.setAttributeNS(null, ATTRIBUTE_STYLE, newStyleValue); //change the style to match our new style
			else
				//if we didn't carry any styles over
				element.removeAttributeNS(null, ATTRIBUTE_STYLE); //remove the style attribute
		}
		*/
	}

	/**
	 * Tidies the children of a particular node, treating each one as an independent node. Called from <code>tidyNode()</code>.
	 * @param node The node the children of which should be tidied.
	 * @see #tidyNode
	 */
	protected void tidyChildNodes(final Node node) {
		final NodeList childNodeList = node.getChildNodes(); //get a list of child nodes
		//Tidy all the children, first. Dynamically check the length each time,
		//  because certain operations can add child nodes by breaking apart
		//  child nodes.
		for(int i = 0; i < childNodeList.getLength(); ++i) { //look at each child node TODO we probably want to use some sort of iterator instead, in case tidying a node can remove, replace, or otherwise modify the list
			final Node childNode = childNodeList.item(i); //get a reference to this child node
			tidy(childNode); //tidy this child node
		}
	}

	/**
	 * Tidies the first-level children of a particular node, recognizing meaning within groups of nodes. This method locates lists, for example. Called from
	 * <code>tidyNode()</code>.
	 * @param element The element the children of which should be tidied.
	 * @see #tidyNode
	 */
	protected void tidyChildGroups(final Element element) {
		convertChildHeadings(element); //convert any children that appear to be headings into headings
		convertChildListItems(element); //convert any children into lists that appear to be list items
		convertChildIndentedItems(element); //convert any children into indented blocks that appear to be blockquotes
	}

	/** Characters which could conceivably be appended to a list marker. */
	//TODO move this to the beginning, probably
	protected static final Characters LIST_ITEM_MARKER_DELIMITER_CHARS = new Characters('.', ')', '-', EM_DASH_CHAR, EN_DASH_CHAR);
	/** A string representation of the bullet character which will be converted to a list item marker. */
	//TODO move this to the beginning, probably
	protected static final String BULLET_STRING = String.valueOf(BULLET_CHAR);

	/**
	 * Searches all first-level child block elements for any content which could be classified as list items and appropriately turns those items into lists.
	 * Called from <code>tidyChildGroups()</code>. This method takes the following steps to locate list items:
	 * <ol>
	 * <li>Look at each first-level block element child, ignoring list elements.</li>
	 * <li>If a child begins with an appropriate marker (such as '1' or 'a') and some delimiter (such as ')' or '.'), the item is marked as list item potential.</li>
	 * <li>If there are multiple potential list items, and all their markers are appropriately sequenced, the elements and all interspersed nodes are removed and
	 * placed inside an <code>&lt;ol&gt;</code> or <code>&lt;il&gt;</code> as appropriate in the same location as the first list item.</li>
	 * <li>The newly formed list is tidied.</li>
	 * <li>This procedure is repeated from the beginning, starting at the first child, until no new lists have been created.</li>
	 * </ol>
	 * @param element The element the children of which should be tidied.
	 * @see #tidyChildGroups
	 */
	/*TODO currently lists like this will turn into lists incorrectly:
	1.
	2.
		1.
		2.
	3.
	4.
	*/
	//TODO make the calling method not call this method for lists
	protected void convertChildListItems(final Element element) {
		final Document document = element.getOwnerDocument(); //get the owner document of the element
		final String elementNamespace = element.getNamespaceURI(); //get the element's namespace
		Node searchStartNode = element.getFirstChild(); //show that we should start the search at the first child of the element; we'll use this later if we need to start searching again at the first child node or anywhere else in the list
		do {
			final List listItemNodeList = new ArrayList(); //create a new list to hold sequential nodes we think might be part of a list
			String listStyleType = null; //when we start a list, we'll store what type of list we think it is here
			Node childNode = searchStartNode; //start searching at the requested location
			searchStartNode = null; //show that we've started our search; if we need to search again, we'll set this to some valid value
			while(childNode != null) { //while we have a child to look at
				Node nextNode = childNode.getNextSibling(); //make a note of what we *think* will be the next node
				final int childNodeType = childNode.getNodeType(); //get the type of this child node
				final String childNodeName = childNode.getNodeName(); //get the node's name TODO use namespaces
				//only look at block elements that are not list items
				if(childNodeType == childNode.ELEMENT_NODE && !ELEMENT_LI.equals(childNodeName) //skip list items TODO what if we skip some in the middle of a list?
						&& !childNodeName.startsWith("h") //skip headings
						&& isBlockElement((Element)childNode)) { //TODO eventually use isListItem() or something, or check the styles in case other elements are classified as list items
					//TODO check to see if this is already a list item; or maybe wait until we've found a marker and then, if it's a list item, remove the literal marker (but then we'd need to check to see if the style made it a plain list item, meaning there would be no automatic marker)
					String markerListStyleType = null; //we'll set this to a valid list style type if this item has a valid marker
					String markerString = null; //if we find what appears to be a marker (setting markerListStyleType to a valid value), we'll store the marker string here
					final Text textNode = (Text)getFirstNode(childNode, NodeFilter.SHOW_TEXT); //get the first text node in the element
					if(textNode != null) { //if we found a text node somewhere inside this text node
						final String data = textNode.getData(); //get the text node data
						//create a tokenizer to extract the list item marker
						final StringTokenizer stringTokenizer = new StringTokenizer(data, Characters.TRIM_CHARACTERS.toString());
						if(stringTokenizer.hasMoreTokens()) { //if there is a token, check to see if it's a list marker
							markerString = stringTokenizer.nextToken(); //get the text which may be a marker
							if(markerString.equals(BULLET_STRING)) { //if the marker is the bullet character
								markerListStyleType = CSS_LIST_STYLE_TYPE_DISC; //this is the disc style type
							} else { //if the marker is not the bullet character
								//see if this "marker" contains at least one list item delimiter
								final int delimiterIndex = CharSequences.charIndexOf(markerString, LIST_ITEM_MARKER_DELIMITER_CHARS);
								if(delimiterIndex >= 0) { //if this marker contained a delimiter, it's starting to look like a real marker; we'll trim the delimeters and see for sure
									//TODO same the delimiter somewhere
									//trim the "." or ")" or whatever from the string, giving us just the marker
									markerString = Strings.trim(markerString, LIST_ITEM_MARKER_DELIMITER_CHARS);
									if(CharSequences.isLatinDigits(markerString)) { //if this string contains only latin digits (the digits '0'-'9')
										markerListStyleType = CSS_LIST_STYLE_TYPE_DECIMAL; //assume this is a decimal list style type
									} else if(markerString.length() == 1) { //if the marker is only one character long
										final char c = markerString.charAt(0); //get the first character
										if(c >= 'a' && c <= 'z') { //if the character is 'a'-'z'
											markerListStyleType = CSS_LIST_STYLE_TYPE_LOWER_ALPHA; //show that this is lowercase letters
										} else if(c >= 'A' && c <= 'Z') { //if the character is 'A'-'Z'
											markerListStyleType = CSS_LIST_STYLE_TYPE_UPPER_ALPHA; //show that this is uppercase letters
										}
										//TODO fix for other types
									}
								}
							}
						}
					}
					//if we were able to determine a list style type for this item, and
					//  the list type matches the list we've already started (or we haven't
					//  started a list)
					if(markerListStyleType != null && (listStyleType == null || markerListStyleType == listStyleType)) {
						//get what we expect the marker string to be based upon the index this list item would be in the list
						final String expectedMarkerString = CSS.getMarkerString(markerListStyleType, listItemNodeList.size());
						//to detect new lists starting, see what would come at the first of this type of list
						final String expectedFirstMarkerString = CSS.getMarkerString(markerListStyleType, 0);
						final boolean isExpectedMarker; //we'll see if this is the marker we were expecting
						//if this is one of the list types that always have the same marker
						if(markerListStyleType == CSS_LIST_STYLE_TYPE_CIRCLE || markerListStyleType == CSS_LIST_STYLE_TYPE_DISC
								|| markerListStyleType == CSS_LIST_STYLE_TYPE_SQUARE)
							isExpectedMarker = true; //since the list item marker never changes, the marker will always match as long as it is of the same time
						else { //if this is a list type in which the marker can change for each list item
							isExpectedMarker = markerString.equals(expectedMarkerString); //see if the list item is what we expeted
						}
						if(isExpectedMarker) { //if we got what we expected, we seem to be guessing right
							listItemNodeList.add(childNode); //add this node to our list, since it matches what we expect
							listStyleType = markerListStyleType; //show that we know for sure what type of list this will be
						} else if(markerString.equals(expectedFirstMarkerString)) { //if we got what should be the first item in the list, we seem to be starting a new list
							if(listItemNodeList.size() == 1) { //if our old list only had one item, and we're starting a new list
								listItemNodeList.clear(); //clear the old list
								listItemNodeList.add(childNode); //we'll start over with this list
								listStyleType = markerListStyleType; //show that we know for sure what type of list this will be
							} else
								//if we come upon a new list (even though it has the same type), and we completed a list previously
								break; //stop and process the list we filled; we can come back and get this second list the next time around
						} else
							//if this doesn't match what we expect, either list is finished
							markerListStyleType = null; //show that this turned out to not be a marker list style type after all TODO we may not use this line anymore
					}
				}
				childNode = nextNode; //go to the next node
			}
			if(listItemNodeList.size() > 0) { //if we've collected a list of nodes to become list items
				if(listItemNodeList.size() > 1) { //if we have at least two list items, this constitutes a list
					final String listElementName = ELEMENT_OL; //TODO fix
					//create a new list element to contain the elements we found
					final Element listElement = document.createElementNS(elementNamespace, listElementName);
					//add the class="list-style: listStyleType" attribute with the correct list style TODO maybe later create a style declaration and write it to the class attribute, so this will be created automatically
					listElement.setAttributeNS(null, ATTRIBUTE_STYLE, CSS_PROP_LIST_STYLE_TYPE + CSS.PROPERTY_DIVIDER_CHAR + CSS.SPACE_CHAR + listStyleType);
					final Element firstListItemElement = (Element)listItemNodeList.get(0); //get a reference to our first element to become a list item
					final Element lastListItemElement = (Element)listItemNodeList.get(listItemNodeList.size() - 1); //get a reference to our last element to become a list item
					element.insertBefore(listElement, firstListItemElement); //insert our list right before the first list item
					Node moveNode; //this will keep track of the node we're currently moving
					Node nextMoveNode = firstListItemElement; //we'll start by moving the first list item element
					do {
						moveNode = nextMoveNode; //see which node we should move
						nextMoveNode = moveNode.getNextSibling(); //after we move this node, this is the node we'll move next
						//TODO del when works								Node replacementNode=element.removeChild(moveNode); //remove the node from its parent
						element.removeChild(moveNode); //remove the node from its parent
						listElement.appendChild(moveNode); //append the list item element to the list element
						if(listItemNodeList.indexOf(moveNode) >= 0) { //if we just moved an element that we've already determined should be a list item
							final Element listItemElement = replaceElementNS((Element)moveNode, elementNamespace, ELEMENT_LI); //rename the element to "li"
							//remove the marker from the list item
							final Text textNode = (Text)getFirstNode(listItemElement, NodeFilter.SHOW_TEXT); //get the first text node in the element
							if(textNode != null) { //if we found a text node somewhere inside this text node
								final String data = textNode.getData(); //get the text node data
								//see where the first whitespace is
								final int whitespaceIndex = CharSequences.charIndexOf(data, Characters.TRIM_CHARACTERS);
								if(whitespaceIndex >= 0) { //if there is whitespace (there always should be, or we could not have determined that this is a list item
									textNode.setData(data.substring(whitespaceIndex + 1)); //remove the marker and update the text node
								}
							}
						}
					} while(moveNode != lastListItemElement); //keep moving nodes until we've moved the last list item
					tidy(listElement); //tidy the list element we just created, which will potentially find nested lists and trim the front of the list items, if needed
					searchStartNode = element.getFirstChild(); //since we found a list, we should check the child elements again for lists, starting with the first child

				} else { //if we only found one item, that doesn't constitute a list; we'll need to search again, starting right after that list item
					searchStartNode = ((Element)listItemNodeList.get(0)).getNextSibling(); //we'll search the list again, this time starting with the node right after the one node we found last time
				}
			}
		} while(searchStartNode != null); //keep going through the list until we are not able to find a new list
	}

	/**
	 * Searches all first-level child block elements for any content which could be classified as indented and appropriately turns those items into
	 * <code>&lt;div class="fullIndent"&gt;</code> elements. Called from <code>tidyChildGroups()</code>. This method takes the following steps to locate block
	 * quotes:
	 * <ol>
	 * <li>Look at each first-level block element child, ignoring list elements.</li>
	 * <li>If a child element's left and right margins are greater than zero and are equal, the item is marked as included in a potential indented section.</li>
	 * <li>If an element is found with a greater indention, the preceding elements are discarded and the process starts over with the new indented element(s).</li>
	 * <li>If at the end of the child elements there are collected elements, they are grouped in a <code>&lt;div class="fullIndented"&gt;</code> element.</li>
	 * <li>This procedure is repeated from the beginning, starting at the first child, until no new indented elements have been created.</li>
	 * </ol>
	 * @param element The element the children of which should be tidied.
	 * @see #tidyChildGroups //TODO currently a nested blockquote that begins its group will not be recognized
	 */
	protected void convertChildIndentedItems(final Element element) {
		final Document document = element.getOwnerDocument(); //get the owner document of the element
		final String elementNamespace = element.getNamespaceURI(); //get the element's namespace
		Node searchStartNode = element.getFirstChild(); //show that we should start the search at the first child of the element; we'll use this later if we need to start searching again at the first child node or anywhere else
		do {
			final List blockquoteNodeList = new ArrayList(); //create a new list to hold sequential nodes we think might be part of a blockquote
			float margin = 0; //show that we haven't found any indentations, yet
			Node childNode = searchStartNode; //start searching at the requested location
			searchStartNode = null; //show that we've started our search; if we need to search again, we'll set this to some valid value
			while(childNode != null) { //while we have a child to look at
				Node nextNode = childNode.getNextSibling(); //make a note of what we *think* will be the next node
				final int childNodeType = childNode.getNodeType(); //get the type of this child node
				final String childNodeName = childNode.getNodeName(); //get the node's name TODO use namespaces
				//only look at block elements that are not blockquotes TODO what if we skip some in the middle of a list?
				if(childNodeType == childNode.ELEMENT_NODE /*TODO testing && !childNodeName.equals(ELEMENT_BLOCKQUOTE)*/&& isBlockElement((Element)childNode)) {
					float leftMargin = 0; //we'll store the left margin here, if we find one
					float rightMargin = 0; //we'll store the right margin here, if we find one
					final Element childElement = (Element)childNode; //cast the node to an element
					/*TODO fix with general stylesheet processor implementation
					final CSSStyleDeclaration style = XMLCSSProcessor.getLocalHTMLStyle(childElement); //get the child element's style from the style attribute
					if(style != null) { //if this element has a local style
						final CSSPrimitiveValue leftMarginValue = (CSSPrimitiveValue)style.getPropertyCSSValue(CSS_PROP_MARGIN_LEFT); //get the left margin value
						if(leftMarginValue != null) { //if there's a left margin value
							final CSSPrimitiveValue rightMarginValue = (CSSPrimitiveValue)style.getPropertyCSSValue(CSS_PROP_MARGIN_RIGHT); //get the right margin value
							if(rightMarginValue != null) { //if there's a right margin value
								leftMargin = leftMarginValue.getFloatValue(leftMarginValue.getPrimitiveType()); //get the left margin, ignoring the units
								rightMargin = leftMarginValue.getFloatValue(rightMarginValue.getPrimitiveType()); //get the left margin, ignoring the units
//TODO del when works
//								if(leftMargin==rightMargin) {	//if the left and right margins are identical, we assume this is a blockquote
//									if(leftMargin>margin) {	//if the margin increased, this is the start of another blockquote
//										blockquoteNodeList.clear(); //we'll start the list over, because this is a nested blockquote
//										blockquoteNodeList.add(childElement); //add this element which will become a blockquote
//										margin=leftMargin;  //show the margin our new blockquote uses
//									}
//									else if(leftMargin>0 && leftMargin==margin) {	//if this is valid blockquote at the same level of blockquote we had before
//										blockquoteNodeList.add(childElement); //add this element to our list of elements to become blockquotes
//									}
//									else if(blockquoteNodeList.size()>0) {	//if the margin decreased, the blockqoute is finished
//										break;  //we finished a blockquote; stop this current iteration of searching
//									}
//								}
							}
						}
					}
					*/
					//TODO it would probably be best to give the left and right margins some initial invalid number
					//if this is a blockquote and we've started a list, assume this is a nested blockquote
					if(ELEMENT_DIV.equals(childNodeName) && "fullIndent".equals(childElement.getAttributeNS(null, "class")) && blockquoteNodeList.size() > 0) { //G**use constants here
						blockquoteNodeList.add(childElement); //add this blockquote element as an element to be added as the child of the blockquote we're going to create
					} else if(leftMargin == rightMargin) { //if the left and right margins are identical, we assume this is a blockquote
						if(leftMargin > margin) { //if the margin increased, this is the start of another blockquote
							blockquoteNodeList.clear(); //we'll start the list over, because this is a nested blockquote
							blockquoteNodeList.add(childElement); //add this element which will become a blockquote
							margin = leftMargin; //show the margin our new blockquote uses
						} else if(leftMargin > 0 && leftMargin == margin) { //if this is valid blockquote at the same level of blockquote we had before
							blockquoteNodeList.add(childElement); //add this element to our list of elements to become blockquotes
						} else if(blockquoteNodeList.size() > 0) { //if the margin decreased, the blockqoute is finished
							break; //we finished a blockquote; stop this current iteration of searching
						}
					}
				}
				childNode = nextNode; //go to the next node
			}
			if(blockquoteNodeList.size() > 0) { //if we've collected a list of nodes to become blockquotes
				if(blockquoteNodeList.size() > 1) { //if we have at least two items, we'll wrap a blockquote around them
					//create a new blockquote element to contain the elements we found TODO eventually probably make this a separate utilities method
					final Element blockquoteElement = document.createElementNS(elementNamespace, ELEMENT_DIV);
					blockquoteElement.setAttributeNS(null, "class", "fullIndent"); //TODO comment; use constants
					final Element firstBlockquoteElement = (Element)blockquoteNodeList.get(0); //get a reference to our first element to become a blockquote
					final Element lastBlockquoteElement = (Element)blockquoteNodeList.get(blockquoteNodeList.size() - 1); //get a reference to our last element to become a blockquote
					element.insertBefore(blockquoteElement, firstBlockquoteElement); //insert our list right before the first list item
					Node moveNode; //this will keep track of the node we're currently moving
					Node nextMoveNode = firstBlockquoteElement; //we'll start by moving the first blockquote element
					do {
						moveNode = nextMoveNode; //see which node we should move
						nextMoveNode = moveNode.getNextSibling(); //after we move this node, this is the node we'll move next
						element.removeChild(moveNode); //remove the node from its parent
						blockquoteElement.appendChild(moveNode); //append the element to the blockquote element
					} while(moveNode != lastBlockquoteElement); //keep moving nodes until we've moved the last element
					searchStartNode = element.getFirstChild(); //since we found a blockquote, we should check the child elements again for blockquotes, starting with the first child
				} else { //if we only found one blockquote, simply rename the single element to a blockquote; we'll need to search again, starting right after that blockquote
					final Element blockquoteElement = replaceElementNS((Element)blockquoteNodeList.get(0), elementNamespace, ELEMENT_DIV); //rename the element to "div"
					blockquoteElement.setAttributeNS(null, "class", "fullIndent"); //TODO comment; use constants; what if there already is a class?
					searchStartNode = element.getFirstChild(); //since we found a blockquote, we should check the child elements again for blockquotes, starting with the first child
				}
			}
		} while(searchStartNode != null); //keep going through the list until we are not able to find new blockquotes
	}

	/**
	 * Searches all first-level child block elements for any content which could be classified as headers and appropriately turns those items into
	 * <code>&lt;hX"&gt;</code> elements. Called from <code>tidyChildGroups()</code>.</p>
	 * <p>
	 * The following are always considered to be headers:
	 * </p>
	 * <ul>
	 * <li>Lines containing the words "Contents" (<code>CONTENTS_HEADING</code>).</li>
	 * <li>Lines containing the words "Preface" (<code>PREFACE_HEADING</code>).</li>
	 * <li>Lines containing the words "Foreword" (<code>FOREWORD_HEADING</code>).</li>
	 * <li>Lines containing the words "Introduction" (<code>INTRODUCTION_HEADING</code>).</li>
	 * <li>Lines containing the words "Afterword" (<code>AFTERWORD_HEADING</code>).</li>
	 * <li>Lines containing the words "Bibliography" (<code>BIBLIOGRAPHY_HEADING</code>).</li>
	 * <li>Lines containing the words "Glossary" (<code>GLOSSARY_HEADING</code>).</li>
	 * <li>Lines containing the words "Index" (<code>INDEX_HEADING</code>).</li>
	 * <li>Lines containing the words "Gospel According to" (<code>GOSPEL_HEADING</code>).</li>
	 * </ul>
	 * <p>
	 * The following numbered headings are located and placed in the following heading order:
	 * </p>
	 * <ol>
	 * <li>Lines containing "Volume X" or "Xth Volume" (<code>VOLUME_HEADING</code>).</li>
	 * <li>Lines containing "Book X" or "Xth Book" (<code>BOOK_HEADING</code>).</li>
	 * <li>Lines containing "Article X" or "Xth Article" (<code>ARTICLE_HEADING</code>).</li>
	 * <li>Lines containing "Part X" or "Xth Part" (<code>PART_HEADING</code>).</li>
	 * <li>Lines containing "Chapter X" or "Xth Chapter" (<code>CHAPTER_HEADING</code>).</li>
	 * <li>Lines containing "Scene X" or "Xth Scene" (<code>ACT_HEADING</code>).</li>
	 * <li>Lines containing "Act X" or "Xth Act" (<code>SCENE_HEADING</code>).</li>
	 * </ol>
	 * <p>
	 * Lines with all capital letters are counted as subheadings (<code>SUB_HEADING</code>, unless:
	 * </p>
	 * <ul>
	 * <li>They appear after an element ending with ':'.</li>
	 * <li>They begin with quotes and appear after an element that appears in quotes.</li>
	 * </ul>
	 * <p>
	 * Single lines that are correctly capitalized are counted as title headings (<code>TITLE_HEADING</code>).
	 * </p>
	 * @param element The element the children of which should be converted to child headings.
	 * @see #tidyChildGroups
	 */
	protected void convertChildHeadings(final Element element) {
		/** The heading levels for each heading types. */
		final int headingLevels[] = new int[Prose.MAX_HEADING + 1];
		Arrays.fill(headingLevels, -1); //start out with no heading level for any of the heading types
		int elementIndex = -1; //the index of the element we're processing
		Element contentsHeadingElement = null; //we're not processing a table of contents, yet
		Element lastElement = null; //we'll keep trak of the last element we find
		Element lastPageBreakElement = null; //we'll keep track of the last page break element we have found
		int lastPageBreakElementIndex = -1; //we'll keep track of the index of the last page break element we have found
		int lastPageBreakType = Prose.NO_HEADING; //the type of heading that last made a page break
		Element lastAttemptedPageBreakElement = null; //we'll keep track of the last page break element we attempted
		int lastAttemptedPageBreakElementIndex = -1; //we'll keep track of the index of the last page break element we attempted
		int lastAttemptedPageBreakType = Prose.NO_HEADING; //the type of heading that last attempted to make a page break
		final Document document = element.getOwnerDocument(); //get the owner document of the element
		final String elementNamespace = element.getNamespaceURI(); //get the element's namespace
		//TODO fix		final List headerElementList=new ArrayList(); //we'll put all headers in here
		for(Node childNode = element.getFirstChild(); //get the first child node
		childNode != null; //while we still have child nodes (we'll use getNextSibling() because we may dynamically remove elements
		//get the next sibling node to check (because we might remove the first node, this will get the first child if the node has been removed)
		childNode = childNode != null ? childNode.getNextSibling() : element.getFirstChild())
		/*TODO del when works
				final NodeList childNodes=element.getChildNodes();  //get the list of child nodes
				for(int childIndex=0; childIndex<childNodes.getLength(); ++childIndex) //look at each child node
		*/
		{
			//if this is an element that is not a list item
			if(childNode.getNodeType() == Node.ELEMENT_NODE && !ELEMENT_LI.equals(childNode.getLocalName())) {
				Element childElement = (Element)childNode; //get a reference to this element
				++elementIndex; //show that we're processing another element
				final String childElementNamespace = childElement.getNamespaceURI(); //get the child element's namespace
				if(isBlockElement(childElement)) { //if this is a block element
					final String text = getText(childElement, true).trim(); //get the trimmed text of the element
					if(Prose.isPageNumber(text)) { //if this is a page number
						element.removeChild(childElement); //remove the last page break
						childElement = lastElement; //go back to the last element we found
						childNode = lastElement; //syncrhonize the node and element variables
						continue; //stop processing the old node
					}
					boolean isBookTitleHeading = false; //we'll see if this heading is the same as the book title
					final int headingType = Prose.getHeadingType(text); //see what type of heading this is
					//determine the heading level
					int headingLevel = 2; //start out at the default heading level 2
					if(headingType == Prose.CONTENTS_HEADING) { //if this is a contents heading
						contentsHeadingElement = childElement; //show that we're processing the table of contents
					} else if(headingType == Prose.NO_HEADING) { //if this is not a heading
						contentsHeadingElement = null; //turn off processing of contents
					} else if(headingType > Prose.NO_HEADING) { //if we have a hierarchical heading
						//see if this heading is the book title
						if(headingType == Prose.SUB_HEADING || headingType == Prose.TITLE_HEADING) { //if this is a sub-heading or a title heading
							//if this heading text contains the title text (trimmed of punctuation)
							if(getTitle() != null && Strings.indexOfIgnoreCase(Strings.trim(text, PUNCTUATION_CHARS), Strings.trim(getTitle(), PUNCTUATION_CHARS)) >= 0) {
								headingLevel = 2; //use a high level for the title
								isBookTitleHeading = true; //show that this is a book title
							}
						}
						if(!isBookTitleHeading) { //if this is not a book title
							if(headingLevels[headingType] >= 0) { //if we know a heading level for this heading type
								headingLevel = headingLevels[headingType]; //use the heading level we've already established
							} else { //if we haven't yet established a heading level for this heading type
								for(int i = headingType - 1; i >= 0; --i) { //see if the lower heading types have heading levels already
									if(headingLevels[i] >= 0) //if this heading level has been established
										++headingLevel; //increase the heading level for our heading type
								}
								headingLevels[headingType] = headingLevel; //show that we've found a heading level for our heading type
								for(int i = headingType + 1; i < headingLevels.length; ++i) { //increase all the heading levels of the types above us
									if(headingLevels[i] >= 0) //if this heading level has already been established
										++headingLevels[i]; //increase that heading level, as it is above our heading type
								}
							}
						}
					}
					if(headingType == Prose.PAGE_BREAK_HEADING) {
						//create a page break element
						final Element breakElement = childElement.getOwnerDocument().createElementNS(childElementNamespace, "br"); //TODO use a constant
						breakElement.setAttributeNS(null, "class", "significantBreak"); //make this a hard page break TODO use constants
						element.replaceChild(breakElement, childElement); //replace the child element with our new break element
						childElement = breakElement; //show that we're now processing the new break element
						//if we're following another hard page break <br/>
						if(lastElement == lastAttemptedPageBreakElement && lastAttemptedPageBreakType == Prose.PAGE_BREAK_HEADING)
							element.removeChild(lastPageBreakElement); //remove the last page break
						lastPageBreakElement = childElement; //show that we made a page break
						lastPageBreakElementIndex = elementIndex; //show at what index we made a page break
						lastPageBreakType = headingType; //show what type of heading we used
						lastAttemptedPageBreakElement = childElement; //always show that we wanted to make a page break, even if we didn't
						lastAttemptedPageBreakElementIndex = elementIndex; //show at what index we wanted to make a page break
						lastAttemptedPageBreakType = headingType; //show what type of heading we wanted to use
					} else if(headingType != Prose.NO_HEADING) { //if this is any other type of heading
					/*TODO fix
											if(contentsHeadingElement!=null && contentsHeadingElement!=childElement) {	//if we're processing the table of contents
													//find out how many lines there are
												final int lineCount=(new StringTokenizer(text, EOL_CHARS, true).countTokens()+1)/2;

											}
											if(contentsHeadingElement==null || contentsHeadingElement==childElement)  //if we're not processing the table of contents, turn this element into a heading normally
					//TODO fix						else  //if we're not processing a table of contents, turn this element into a heading normally
					*/
						{
							boolean isIndependent = true; //a header must be indepedent of the other text; we'll confirm that this one is
							if(lastElement != null) { //if we've already processing an element
								final String lastElementText = getText(lastElement, true).trim(); //get the trimmed text of the last element
								if(lastElementText.length() > 0) { //if there was text before us
									final char lastChar = lastElementText.charAt(lastElementText.length() - 1); //get the last character in the last element's text
									//check for a heading appearing after a dependent character such as a colon
									if(DEPENDENT_PUNCTUATION_CHARACTERS.contains(lastChar)) //if this "heading" appears after element ending characters such as a colon, it isn't an independent heading
										isIndependent = false; //this heading doesn't stand alone, so discount it as a heading
									//check for a quoted heading appearing after another quote
									if(lastChar == QUOTATION_MARK_CHAR || lastChar == LEFT_DOUBLE_QUOTATION_MARK_CHAR) { //if this "heading" appears after an element ending with a quote
										if(text.length() > 0) { //if we have text (this is a redundent check, surely, added for verifiability)
											final char firstHeadingChar = text.charAt(0); //get the first character of the heading
											if(firstHeadingChar == QUOTATION_MARK_CHAR || firstHeadingChar == RIGHT_DOUBLE_QUOTATION_MARK_CHAR)
												isIndependent = false; //a "heading" that is a quote in the middle of quotes isn't a heading
										}
									}
								}
							}
							if(isIndependent) { //if this heading is independent
								final String localName = "h" + headingLevel; //TODO fix
								childElement = replaceElementNS(childElement, childElementNamespace, localName); //rename the element to to a heading TODO fix the namespace here
								//see if the last page break was a fixed heading
								final boolean isLastPageBreakHeadingFixed = lastPageBreakType < 0;
								//see if we should do a page break
								//second-level headings always get page breaks, as do chapter
								//  headings or a heading that falls directly under a larger
								//  division (i.e. if there have been no chapter heading)
								//TODO del when works								if(headingLevel==2 || headingType==CHAPTER_HEADING || headingLevels[CHAPTER_HEADING]<0)
								if(headingLevel == 2 || headingType <= Prose.MAX_SIGNIFICANT_HEADING || headingLevels[Prose.CHAPTER_HEADING] < 0) {
									//don't allow two page breaks to follow one another unless
									//  this one is a higher heading (i.e. a lower number) than
									//  the last or the last was a hard page break heading
									//  element (<br/>), in which case mark that last element
									//  for removal; book title headings always get page breaks
									if(lastElement != lastAttemptedPageBreakElement || headingType < lastAttemptedPageBreakType
											|| lastAttemptedPageBreakType == Prose.PAGE_BREAK_HEADING || isBookTitleHeading)
									//TODO del if not needed											|| (isLastPageBreakHeadingFixed && elementIndex>lastAttemptedPageBreakElementIndex+2))
									//TODO fix											|| lastPageBreakType<0) //TODO testing
									{
										//only allow page breaks if there is more than one element between the page breaks,
										//  or if there have been no page breaks, or if the last page
										//  break was a fixed heading (such as the table of contents)
										// (certain heading levels always are significant, though,
										//  as are book titles)
										if(headingType <= Prose.MAX_SIGNIFICANT_HEADING || elementIndex > lastAttemptedPageBreakElementIndex + 2
												|| lastAttemptedPageBreakElementIndex <= 0 || isLastPageBreakHeadingFixed || isBookTitleHeading) {
											//don't ever break pages on level-four headings
											//don't even break pages on the first element
											if(headingLevel < 4 && elementIndex > 0) {
												childElement.setAttributeNS(null, "class", "significantHeading"); //make this a chapter heading elemeng TODO use constants
												lastPageBreakElement = childElement; //show that we made a page break
												lastPageBreakElementIndex = elementIndex; //show at what index we made a page break
												lastPageBreakType = headingType; //show what type of heading we used
												if(lastPageBreakElement == lastElement && lastPageBreakType == Prose.PAGE_BREAK_HEADING) //if we're following a hard page break <br/>
													element.removeChild(lastPageBreakElement); //remove the last page break
											}
										}
									}
								}
								//TODO fix								if(lastElement!=lastPageBreakElement || lastPageBreakType>0)  //TODO testing
								{
									lastAttemptedPageBreakElement = childElement; //always show that we wanted to make a page break, even if we didn't
									lastAttemptedPageBreakElementIndex = elementIndex; //show at what index we wanted to make a page break
									lastAttemptedPageBreakType = headingType; //show what type of heading we wanted to use
								}
							}
						}
					}
					/*TODO del; this doesn't seem to skip blank paragraphs as it was supposed to
									  if(text.length()>0) //if we had text for this element, we'll count this as our last element
											lastElement=childElement; //show that this is the last element we found
					*/
				}
				lastElement = childElement; //show that this is the last element we found
				childNode = childElement; //make sure the child node is syncrhonized with the element we're on, in case we've replaced one
			}
		}
	}

	/**
	 * Tidies an element of an XHTML document. This method may, if needed, replace the element with one or more elements.
	 * @param element The element to tidy.
	 * @return The first of the resulting elements. This may be the original element specified or the first of its replacements.
	 */
	protected Element tidyElement(Element element) {
		element = tidyClassAttribute(element); //tidy class-related attributes, reassigning the element in case it was replaced TODO maybe move to tidyAttiributes()
		element = checkRenameElement(element); //rename the element if we need to
		int removedChildCount; //this will hold the number of child nodes we remove on each iteration
		do { //sometimes removing a child will make a child we've already examined ripe for removal, so check several times
			element.normalize(); //normalize the element to combine all adjacent text nodes
			removedChildCount = removeUnknownElements(element); //remove unknown child elements from the element
		} while(removedChildCount > 0); //keep calling the function until we can remove no more child nodes
		final String elementName = element.getNodeName(); //get the element's name TODO fix for namespaces
		final String elementNamespace = element.getNamespaceURI(); //get the element's namespace
		final Node parentNode = element.getParentNode(); //get the element's parent node
		if(elementName.equals(ELEMENT_APPLET)) { //if this is the applet element, replace it with an object element TODO use namespaces here
			final Element objectElement = createAppletObjectElement(element); //convert the applet element to an object element
			parentNode.replaceChild(objectElement, element); //replace the applet element with the object element
		} else if(elementName.equals(ELEMENT_IMG)) { //if this is the img element, replace it with an object element TODO use namespaces here
			final Element objectElement = createImageObjectElement(element); //convert the img element to an object element
			parentNode.replaceChild(objectElement, element); //replace the img element with the object element
		}
		//if this is a block text container, trim beginning whitespace
		if(isBlockElement(element) && //if this is a block element that either is meant to be a text container or has no block child elements
				(isTextContainer(element) || getChildBlockElementCount(element) == 0)) {
			element = checkBreakElement(element); //break this element into several if we need to
			final Text textNode = (Text)getFirstNode(element, NodeFilter.SHOW_TEXT); //get the first text node in the element
			if(textNode != null) { //if we found a text node
				//trim all beginning whitespace from the text node
				textNode.setData(Strings.trimWhitespaceNoBreakBeginning(textNode.getData()));
			}
		}
		return element; //return whatever element we wound up with
	}

	/**
	 * Checks to see if this element should be renamed to another.
	 * @param element The element which may be renamed. The element must have a valid parent and a valid owner document.
	 * @return The original element if the element was not renamed, or an elements at the same position as the original element containing the same children but
	 *         with a different name.
	 */
	protected Element checkRenameElement(Element element) {
		final String elementName = element.getNodeName(); //get the element's name TODO fix for namespaces
		if(isConvertUnderlineItalics()) { //if we should replace underline tags with italics tags
			if(elementName.equals(ELEMENT_U)) //if this is the underline element
				element = replaceElementNS(element, element.getNamespaceURI(), ELEMENT_I); //rename the element to "i"
		}
		return element; //return whatever element we wound up with
	}

	/**
	 * Checks to see if this element contains one or more first-level break (<code>&lt;br&gt;</code>) elements. If so, the current element is cloned and its
	 * children are divided among the various clones, one for each break section. The actual break elements are removed.
	 * @param element The element to tidy. The element must have a valid parent and a valid owner document.
	 * @return The original element if no breaks were found, or an elements at the same position as the original element containing the first child representing
	 *         the content between the breaks.
	 */
	protected Element checkBreakElement(Element element) {
		Log.trace("Checking break element: ", element.getNodeName());
		final NodeList childNodeList = element.getChildNodes(); //get a reference to the child nodes
		final int childNodeCount = childNodeList.getLength(); //see how many child nodes there are
		if(childNodeCount > 0) { //if there are child nodes
			final int[] breakIndexes = new int[childNodeCount + 1]; //create an array representing the indexes of the breaks we find; there can be no more breaks than child elements, but we'll use one more index to represent the index past the content
			int breakCount = 0; //we haven't yet found any breaks
			for(int i = 0; i < childNodeCount; ++i) { //look at each of the child nodes
				final Node childNode = childNodeList.item(i); //get a reference to this node
				//if this node is a <br> element TODO fix for namespaces
				if(childNode.getNodeType() == childNode.ELEMENT_NODE && childNode.getNodeName().equals(ELEMENT_BR)) {
					breakIndexes[breakCount++] = i; //store this break index in our array, and show that we found one more break element
				}
			}
			if(breakCount > 0) { //if we found any breaks
				final Document document = element.getOwnerDocument(); //get the owner document
				final String namespaceURI = element.getNamespaceURI(); //get the element's namespace URI
				if(breakIndexes[breakCount - 1] < childNodeCount - 1) //if the last break element is not the last element, there is still content after that last break element
					breakIndexes[breakCount++] = childNodeCount; //create a fake break that comes just after the content, so we'll be sure and get the content after the last real break element
				final Element[] fragmentElementArray = new Element[breakCount]; //we'll create several clones and remove content from each of them
				int lastBreakIndex = -1; //show that we haven't had a break, yet
				int fragmentCount = 0; //there may actually be less fragments than breaks, because there may be no content between some breaks
				for(int i = 0; i < breakCount; ++i) { //create each of the broken elements
					//TODO fix				fragmentElementArray[i]=element.;
					final int nextBreakIndex = breakIndexes[i]; //find out the index of the break at the end of this content
					if(nextBreakIndex > 0 && nextBreakIndex > lastBreakIndex) { //if there is actually content between these two breaks, and the break isn't at the first of the content
						final Element fragmentElement = (Element)element.cloneNode(true); //create a deep clone of the element
						/*TODO del, maybe
												final Element fragmentElement=document.createElementNS(namespaceURI, ELEMENT_DIV);	//create the new div element
												//TODO important: have a function here for appending cloned attributes
												XMLUtilities.appendClonedChildNodes(fragmentElement, element, true);  //deep-clone the child nodes of the element and add them to the new fragment element
						*/
						if(childNodeCount > nextBreakIndex) //if there is content after the fragment
							removeChildren(fragmentElement, nextBreakIndex, childNodeCount); //remove all children after the fragment, starting at the next break and including all remaining children
						if(lastBreakIndex > 0) //if there is content before the fragment
							removeChildren(fragmentElement, 0, lastBreakIndex + 1); //remove all children before the fragment, starting at the first child and including the last break
						fragmentElementArray[fragmentCount++] = fragmentElement; //store this fragment and show that we've found another fragment
					}
					lastBreakIndex = nextBreakIndex; //update our record of our last break index
				}
				if(fragmentCount > 0) { //if we found any fragments that actually hold content
					final Node parentNode = element.getParentNode(); //get the element's parent
					Element lastFragmentElement = fragmentElementArray[fragmentCount - 1]; //we'll insert the last fragment first
					parentNode.replaceChild(lastFragmentElement, element); //replace our element with the last fragment
					for(int i = fragmentCount - 2; i >= 0; --i) { //look at any earlier fragments, if there are any
						final Element fragmentElement = fragmentElementArray[i]; //get a reference to this fragment
						parentNode.insertBefore(fragmentElement, lastFragmentElement); //insert this fragment in front of the last fragment
						lastFragmentElement = fragmentElement; //show that the current fragment is now our last fragment
					}
					return fragmentElementArray[0]; //return the first of our replacement fragments
				}
			}
		}
		return element; //return the original, unbroken element since we couldn't break this element
	}

	/**
	 * Tidies the attributes of the element. The element name or position is not changed.
	 * @param element The element the attributes of which should be tidied.
	 */
	protected static void tidyAttributes(final Element element) {
		tidyStyleAttribute(element); //tidy the style attribute, if it's present
		//change any "lang" attribute to "xml:lang"
		if(element.hasAttributeNS(null, XHTML.ATTRIBUTE_LANG)) { //if there is a lang attribute defined
			final String langValue = element.getAttributeNS(null, XHTML.ATTRIBUTE_LANG); //get the lang attribute value
			if(!element.hasAttributeNS(XML_NAMESPACE_URI.toString(), XML.ATTRIBUTE_LANG)) { //if there is no xml:lang attribute TODO use a constant, use namespaces
				//create an xml:lang attribute with the value of the lang attribute TODO use a constant here, use namespaces
				element.setAttributeNS(XML_NAMESPACE_URI.toString(), createQName(XML_NAMESPACE_PREFIX, XML.ATTRIBUTE_LANG), langValue);
				element.removeAttributeNS(null, XHTML.ATTRIBUTE_LANG); //remove the lang attribute
			}
		}
		final String elementName = element.getNodeName(); //get the element's name TODO make this namespace aware
		if(!elementName.equals(ELEMENT_PARAM)) { //if this isn't a param element
			//change any "name" attribute to "id"
			if(element.hasAttributeNS(null, ATTRIBUTE_NAME)) { //if there is a name attribute defined
				final String nameValue = element.getAttributeNS(null, ATTRIBUTE_NAME); //get the name attribute value
				if(!element.hasAttributeNS(null, ATTRIBUTE_ID)) { //if there is no ID attribute
					element.setAttributeNS(null, ATTRIBUTE_ID, nameValue); //create a name attribute with the value of the name attribute
					element.removeAttributeNS(null, ATTRIBUTE_NAME); //remove the name attribute
				}
			}
		}
		if(ELEMENT_HTML.equals(elementName)) { //if this is the HTML element
			//TODO replace this with an XMLUtilities method which removes the attributes
			final NamedNodeMap nodeMap = element.getAttributes(); //get a map of the attributes
			while(nodeMap.getLength() > 0) { //while there are more nodes to remove
				final Node lastNode = nodeMap.item(nodeMap.getLength() - 1); //get a reference to the last node
				nodeMap.removeNamedItemNS(lastNode.getNamespaceURI(), lastNode.getLocalName()); //remove the last node
			}
		}
	}

	/**
	 * Tidies the style attribute from an element, if it's present. If the style element specifies the "Symbol" font family, the immediate child text nodes are
	 * converted from the Symbol encoding to Unicode.
	 * @param element The element to be checked for a style attribute.
	 */
	protected static void tidyStyleAttribute(final Element element) { //TODO rename, perhaps, to tidyStyle()
		if(element.hasAttributeNS(null, ATTRIBUTE_STYLE)) { //if this element has a style attribute
			/*TODO bring back final */String styleValue = element.getAttributeNS(null, ATTRIBUTE_STYLE); //get the value of the style attribute
			if(Strings.indexOfIgnoreCase(styleValue, "symbol") >= 0) { //if the word "symbol" is found in the style, assume it is the symbol font TODO actually parse the style
				final NodeList childNodeList = element.getChildNodes(); //get a list of the child nodes
				final int childNodeCount = childNodeList.getLength(); //see how many child nodes there are
				for(int i = 0; i < childNodeCount; ++i) { //look at each child node
					final Node childNode = childNodeList.item(i); //get a reference to this child node
					if(childNode.getNodeType() == Node.TEXT_NODE) { //if this is a text node
						final Text textNode = (Text)childNode; //cast the node to a text node
						final StringBuffer dataStringBuffer = new StringBuffer(textNode.getData()); //create a string buffer from the text data
						convertSymbolToUnicode(dataStringBuffer); //convert the string from the Symbol font encoding to Unicode
						textNode.setData(dataStringBuffer.toString()); //update the text node's data
					}
				}
				element.removeAttributeNS(null, ATTRIBUTE_STYLE); //remove the style attribute TODO this is a hack, to accommodate for symbol elements that have non-symbol nested spans the text of which will be promosted; fix by only removing the font style on this line
				styleValue = ""; //TODO fix
			}
			//TODO del			try
			{
				if(styleValue.length() != 0) { //if there is a style value
/*TODO fix with general style processing implementation
					//TODO move all of this to tidy text or something
					final XMLCSSStyleDeclaration oldStyle = (XMLCSSStyleDeclaration)XMLCSSProcessor.getLocalHTMLStyle(element); //get the element's style TODO later remove the cast when XMLCSSStyleDeclaration implements CSS2Properties
//TODO del when works
//							//TODO change to XHTMLUtilities.getLocalStyle()
//					final XMLCSSProcessor cssProcessor=new XMLCSSProcessor();	//create a new CSS processor TODO make one for the entire tidier object -- don't create it locally
//					final XMLCSSStyleDeclaration oldStyle=new XMLCSSStyleDeclaration(); //create a new style declaration
//					final ParseReader styleReader=new ParseReader(styleValue, "Element "+element.getNodeName()+" Local Style");	//create a string reader from the value of this local style attribute TODO i18n
//					cssProcessor.parseRuleSet(styleReader, oldStyle); //read the style into our style declaration
					//TODO check for parseRuleSet() return value
					final String textTransform = oldStyle.getTextTransform(); //see if there is a text transform request
					if(textTransform.length() > 0) { //if a text transformation was requested
						if(CSS_TEXT_TRANSFORM_UPPERCASE.equals(textTransform))
							; //if we should transform the text to uppercase
						{
							final NodeList childNodeList = element.getChildNodes(); //get a list of the child nodes
							final int childNodeCount = childNodeList.getLength(); //see how many child nodes there are
							for(int i = 0; i < childNodeCount; ++i) { //look at each child node
								final Node childNode = childNodeList.item(i); //get a reference to this child node
								if(childNode.getNodeType() == Node.TEXT_NODE) { //if this is a text node
									final Text textNode = (Text)childNode; //cast the node to a text node
									textNode.setData(textNode.getData().toUpperCase()); //convert the text to uppercase TODO check locale; all this should be done better
								}
							}
						}
					}
					final XMLCSSStyleDeclaration newStyle = new XMLCSSStyleDeclaration(); //create a new style declaration to received the tidied styles
					//TODO automate this style copying
					final CSSValue backgroundColorValue = oldStyle.getPropertyCSSValue(CSS_PROP_BACKGROUND_COLOR);
					if(backgroundColorValue != null) //if we have a background set
						newStyle.setPropertyCSSValue(CSS_PROP_BACKGROUND_COLOR, backgroundColorValue);
					final CSSValue listStyleTypeValue = oldStyle.getPropertyCSSValue(CSS_PROP_LIST_STYLE_TYPE);
					if(listStyleTypeValue != null) //if we have a list style type set
						newStyle.setPropertyCSSValue(CSS_PROP_LIST_STYLE_TYPE, listStyleTypeValue);
*/
					//TODO bring over other style properties

					/*TODO fix
									((XMLElement)element).setLocalCSSStyle(style);  //set the element's style to whatever we constructed TODO eventually use a separate style tree instead of the element itself
					*/
					/*TODO fix; remove at a later stage in the process
									  final String newStyleValue=newStyle.getCssText().trim(); //get the new style
										if(newStyleValue.length()>0)  //if we carried any styles over
											element.setAttributeNS(null, ATTRIBUTE_STYLE, newStyleValue); //change the style to match our new style
										else  //if we didn't carry any styles over
											element.removeAttributeNS(null, ATTRIBUTE_STYLE); //remove the style attribute
					*/
				}
			}
			/*TODO del
						catch(IOException e)
						{
							Log.error(e); //TODO do something better here
						}
			*/
			//TODO del when works			element.removeAttributeNS(null, ATTRIBUTE_STYLE); //remove the style attribute
		}
	}

	/**
	 * Tidies class-related attributes of an element. If the element needs to be replaced, it is replaced, remaining at the same relative position as the original
	 * element. The new element is returned, either the original or its replacement.
	 * @param element The element to tidy class-related attributes.
	 * @return The resulting element, either the same element or its replacement.
	 */
	//TODO document the element name normalization in the file header comments
	protected Element tidyClassAttribute(Element element) {
		final String classValue = getDefinedAttributeNS(element, null, ATTRIBUTE_CLASS); //get the class attribute if it is defined
		if(classValue != null) { //if there is a class defined
			final String elementName = element.getNodeName(); //get the element's name TODO fix for namespaces
			if(classValue.trim().equalsIgnoreCase(elementName)) //if the class name and the element name are identical
				element.removeAttributeNS(null, ATTRIBUTE_CLASS); //remove the class attribute
			else if(isNormalizeStyleClasses()) { //if we should normalize style classes
				//if the class contains "num" and at least "chap" or "title"
				if(Strings.indexOfIgnoreCase(classValue, CHAPTER_TITLE_CLASS_NUM_SUBSTRING) != -1
						&& (Strings.indexOfIgnoreCase(classValue, CHAPTER_TITLE_CLASS_CHAP_SUBSTRING) != -1 || Strings.indexOfIgnoreCase(classValue,
								CHAPTER_TITLE_CLASS_TITLE_SUBSTRING) != -1)) {
					element.setAttributeNS(null, ATTRIBUTE_CLASS, CHAPTER_NUMBER_CLASS); //change the class value to "chapterNumber"
					if(!elementName.equals(ELEMENT_H1)) { //if this is not an H1 element
						//replace the element with an <h1> element
						element = replaceElementNS(element, element.getNamespaceURI(), ELEMENT_H1);
					}
				} else if(tidyMicrosoft) { //if we should tidy Microsoft-related classes
					if(classValue.equals(MSO_NORMAL_CLASS)) //or if this is the "MsoNormal" class
						element.removeAttributeNS(null, ATTRIBUTE_CLASS); //remove the class attribute
					else if(classValue.equals(MSO_CAPTION_CLASS)) //or if this is the "MsoCaption" class
						element.setAttributeNS(null, ATTRIBUTE_CLASS, CAPTION_CLASS); //change the class value to "caption"
					else if(classValue.equals(MSO_BODY_TEXT_INDENT_CLASS) //if this is the "MsoBodyTextIndent" class
							|| classValue.equals(MSO_BLOCK_TEXT_CLASS)) { //or if this is the "MsoBlockText" class TODO maybe do something more intelligent with this class
						element.removeAttributeNS(null, ATTRIBUTE_CLASS); //remove the class attribute
						//replace the element with a blockquote element and return it TODO only turn some of these into blockquotes; for others, use div class="fullIndent"
						return replaceElementNS(element, element.getNamespaceURI(), ELEMENT_BLOCKQUOTE);
					} else if(classValue.equals(MSO_TITLE_CLASS)) { //or if this is the "MsoTitle" class
						element.setAttributeNS(null, ATTRIBUTE_CLASS, TITLE_CLASS); //change the class value to "title"
						if(!elementName.equals(ELEMENT_H1)) { //if this is not an H1 element
							//replace the element with an <h1> element
							element = replaceElementNS(element, element.getNamespaceURI(), ELEMENT_H1);
						}
					}
				}
			}
		}
		return element; //return the element that we may have modified, and that might be a replacement
	}

	//TODO comment calls to enclosing characters tidying, and how splitting results in more nodes to the right that will eventually get processed

	/**
	 * Tidies a text node.
	 * @param textNode The text node to be tidied. The node should have a valid parent element.
	 */
	protected static void tidyText(Text textNode) {
		boolean modified = false; //show that we haven't yet modified the text
		String data = textNode.getData(); //get the data from the text node
		/*TODO fix or del
				final Node parentNode=textNode.getParentNode();  //get the parent node
				if(parentNode.getNodeType()==parentNode.ELEMENT_NODE) {	//if the parent node is an element (it always should be)
					final Element parentElement=(Element)parentNode;  //cast the parent node to an element TODO fix all of this with methods that query a style up the chain
					final String styleValue=parentElement.getAttributeNS(null, ATTRIBUTE_STYLE); //get the value of the style attribute
					if(styleValue!=null) {	//if the parent element has a style attribute
						try
						{
							final XMLCSSProcessor cssProcessor=new XMLCSSProcessor();	//create a new CSS processor TODO make one for the entire tidier object -- don't create it locally
							final XMLCSSStyleDeclaration style=new XMLCSSStyleDeclaration(); //create a new style declaration
							final ParseReader styleReader=new ParseReader(styleValue, "Element "+parentElement.getNodeName()+" Local Style");	//create a string reader from the value of this local style attribute TODO i18n
							cssProcessor.parseRuleSet(styleReader, style); //read the style into our style declaration
							final String textTransform=style.getTextTransform(); //see if there is a text transform request
							if(textTransform.length()>0) {	//if a text transformation was requested
								if(CSS_TEXT_TRANSFORM_UPPERCASE.equals(textTransform)); //if we should transform the text to uppercase
								{
									data=data.toUpperCase();  //convert the text to uppercase TODO fix for locale
									modified=true;  //show that we modified the text
								}
							}
						}
						catch(IOException e)
						{
							Log.error(e); //TODO do something better here
						}
					}
				}
		*/
		final StringBuffer dataStringBuffer = new StringBuffer(data); //create a string buffer from the text data
		if(tidyText(dataStringBuffer)) { //tidy the text data; if there were changes made
			modified = true; //show that we modified the text
		}
		if(modified) { //if we modified the text //TODO tidy; combine blocks
			data = dataStringBuffer.toString(); //convert the text back to a string
			textNode.setData(data); //update the text node's data
		}
		//turn "_..._" into an emphasized element
		textNode = tidyEnclosingDelimiters(textNode, '_', textNode.getParentNode().getNamespaceURI(), "em"); //TODO use a constant
		//turn "^...^" into an emphasized element
		textNode = tidyEnclosingDelimiters(textNode, '^', textNode.getParentNode().getNamespaceURI(), "em"); //TODO testing; eventually change to small caps; use a constant
		//turn "~...~" into an emphasized element
		textNode = tidyEnclosingDelimiters(textNode, '~', textNode.getParentNode().getNamespaceURI(), "em"); //TODO use a constant
	}

	//TODO comment
	protected static Text tidyEnclosingDelimiters(final Text textNode, final char delimiter, final String namespaceURI, final String qname) {
		final String data = textNode.getData(); //get the text data
		int startIndex = 0; //we'll start looking at the first of the text
		while(startIndex >= 0) { //while we haven't ran out of characters
			startIndex = data.indexOf(delimiter, startIndex); //find the next possible starting location
			if(startIndex >= 0) { //if we found the delimiter
				//if it's either at the start of the string or preceded by whitespace or punctuation
				if((startIndex == 0 || (Characters.isWordDelimiter(data.charAt(startIndex - 1)) && data.charAt(startIndex - 1) != delimiter)) //(but not preceded by another delimiter)
						&& startIndex < data.length() - 1 //and there's a character after the delimiter
						&& !Characters.isWhitespace(data.charAt(startIndex + 1)) //that is not whitespace
						&& data.charAt(startIndex + 1) != delimiter) { //or the delimiter itself
					int endIndex = data.indexOf(delimiter, startIndex + 1); //try to find the rightmost character
					if(endIndex > startIndex + 1 //if there is at least one character between the delimiters
							//and the rightmost delimiter does not come after whitespace
							&& !Characters.isWhitespace(data.charAt(endIndex - 1))) {
						textNode.deleteData(endIndex, 1); //remove the ending delimiter, leaving the ending index directly after the string
						textNode.deleteData(startIndex, 1); //remove the starting delimiter, leaving the starting index at the start of the string
						--endIndex; //decrement the ending index to compensate for removing the starting delimiter
						final Element parentElement = (Element)textNode.getParentNode(); //get the parent of the text node
						//split the selected text into a separate text node
						final Text splitTextNode = splitText(textNode, startIndex, endIndex);
						//create an element to wrap the characters
						final Element element = textNode.getOwnerDocument().createElementNS(namespaceURI, qname);
						parentElement.replaceChild(element, splitTextNode); //replace the split text node with the new element
						element.appendChild(splitTextNode); //set the child node as a child of the new element
						final Text firstTextNode; //we'll determine the first text node in our new split
						if(startIndex > 0) //if the split resulted in a text node being created before the split text
							firstTextNode = (Text)element.getPreviousSibling(); //there's a text node that appears before the split text node; return it
						else
							//if the split text was at the beginning of the node to begin with
							firstTextNode = splitTextNode; //return the text node we split out
						//recursively tidy enclosing delimiters for the first text node
						return tidyEnclosingDelimiters(firstTextNode, delimiter, namespaceURI, qname);
					}
				}
				++startIndex; //start looking after our current location for the next delimiter, since this one didn't work
			}
		}
		return textNode; //show that we were unable to find any enclosing delimiters
	}

	/**
	 * Tidies text data and indicates whether the text data was changed.
	 * <ul>
	 * <li>Proprietary characters are replaced with Unicode code points.</li>
	 * <li>The runs "--", "---", and "----" are each replaced with an em-dash.</li>
	 * <li>The run "''" (subsequent apostrophes) is replaced with a quote character.</li>
	 * <li>The run "``" (subsequent grave accents) is replaced with a quote character.</li>
	 * //TODO fix
	 * <li>Grave accents following spaces are converted to apostrophes.</li>
	 * </ul>
	 * @param stringBuffer The buffer containing the text data to tidy.
	 * @return <code>true</code> if the data was changed, <code>false</code> if the data was already tidy.
	 */
	protected static boolean tidyText(final StringBuffer stringBuffer) {
		//make the necessary replacements and make a note of how many replacements were made
		final int replacementCount = StringBuffers.replace(stringBuffer, CHARACTER_MATCH_REPLACE_SET_ARRAY);
		/*TODO fix
							final String textTransform=oldStyle.getTextTransform(); //see if there is a text transform request
							if(textTransform.length()>0) {	//if a text transformation was requested
							  CSS_TEXT_TRANSFORM_UPPERCASE.equals(textTransform)

							}
		*/
		boolean modified = replacementCount > 0; //see if we modified the characters
		final int originalLength = stringBuffer.length(); //get the length before we replace runs
		//replace "--", "---", and "----" with an em-dash (this can only make the string shorter)
		StringBuffers.replaceRuns(stringBuffer, HYPHEN_MINUS_CHAR, 2, 4, EM_DASH_CHAR);
		//replace "''" (subsequent apostrophes) with a quote character (this can only make the string shorter)
		StringBuffers.replaceRuns(stringBuffer, APOSTROPHE_CHAR, 2, 2, QUOTATION_MARK_CHAR);
		//replace "``" (subsequent grave accents) with a quote character (this can only make the string shorter)
		StringBuffers.replaceRuns(stringBuffer, GRAVE_ACCENT_CHAR, 2, 2, QUOTATION_MARK_CHAR);
		if(stringBuffer.length() != originalLength) //if replacing runs changed the length of the string buffer
			modified = true; //something was modified
			/*TODO this needs to be thought through more---what about at the end of a word? should we replace all of them?
							//convert grave accents following whitespace to apostrophes TODO use a common routine to do this
					for(int i=stringBuffer.lengt()-1; i>=0; --i)  //look at each character in the string buffer
			*/
		/*TODO fix for option only
				if(tidyUnicode) {	//if we should convert some Unicode characters to their plain equivalents
					//TODO optimize; make static somewhere else; make more readable representation
					final String unicodeCharacters=""+LEFT_SINGLE_QUOTE+RIGHT_SINGLE_QUOTE+LEFT_DOUBLE_QUOTE+RIGHT_DOUBLE_QUOTE+N_DASH+M_DASH+TRADEMARK;  //characters which will be replaced
					final String[] replacementStrings=new String[]{"'", "'", "\"", "\"", "-", "--", "(TM)"};  //replacement strings for the Unicode characters TODO use constants here
					final StringBuffer tidyStringBuffer=new StringBuffer(stringBuffer.toString());  //create a new string buffer with the contents of the first
					stringBuffer.delete(0, length);  //remove all the characters in the original string buffer
					for(int i=0; i<length; ++i) {	//look at each character in the string buffer
						final char currentCharacter=tidyStringBuffer.charAt(i); //get this character
						final int replacementIndex=unicodeCharacters.indexOf(currentCharacter);  //see if this character should be replaced
						if(replacementIndex!=-1) {	//if this character should be replaced
							stringBuffer.append(replacementStrings[replacementIndex]);  //replace the original character with its replacement string
							modified=true;  //show that we just replaced a character
						}
						else  //if this character shouldn't be replaced
							stringBuffer.append(currentCharacter);  //append the character normally
					}
				}
		*/
		return modified; //return whether or not we modified characters in the string buffer
		//TODO fix		return replacementCount>0;  //return whether or not we modified characters in the string buffer
	}

	/**
	 * Converts text data from the Adobe Symbol font encoding to Unicode.
	 * @param stringBuffer The buffer containing the text data to convert.
	 */
	protected static void convertSymbolToUnicode(final StringBuffer stringBuffer) {
		//TODO shouldn't we have a function that transforms this automatically?
		StringBuffers.replace(stringBuffer, Unicode.SYMBOL_FONT_TO_UNICODE_TABLE); //convert all symbol characters to Unicode
		/*TODO del when works
				final int converstionTableLength=SYMBOL_FONT_TO_UNICODE_TABLE.length; //find out how many characters we recognize
				final int stringBufferLength=stringBuffer.length(); //find out how many characters there are to convert
				for(int i=0; i<stringBufferLength; ++i) {	//look at each character to convert
					//look up the Unicode character at the index of the character we're looking at, and replace the character with the new one
					stringBuffer.setCharAt(i, SYMBOL_FONT_TO_UNICODE_TABLE[stringBuffer.charAt(i)]);
				}
		*/
	}

	/**
	 * Creates an <code>&lt;object&gt;</code> element from an <code>&lt;img&gt;</code> element.
	 * @param element The img element.
	 * @return A new object element converted from the image.
	 */
	protected static Element createImageObjectElement(final Element element) {
		final String elementNamespace = element.getNamespaceURI(); //get the element's namespace

		//get the href attribute, or null if not present
		final String src = element.hasAttributeNS(null, ELEMENT_IMG_ATTRIBUTE_SRC) ? element.getAttributeNS(null, ELEMENT_IMG_ATTRIBUTE_SRC) : null;
		//get the alt attribute, or "Image" if not present
		final String alt = element.hasAttributeNS(null, ELEMENT_IMG_ATTRIBUTE_ALT) ? element.getAttributeNS(null, ELEMENT_IMG_ATTRIBUTE_ALT) : "Image"; //TODO i18n
		//get the height attribute, or null if not present
		final String height = element.hasAttributeNS(null, ELEMENT_IMG_ATTRIBUTE_HEIGHT) ? element.getAttributeNS(null, ELEMENT_IMG_ATTRIBUTE_HEIGHT) : null;
		//get the width attribute, or null if not present
		final String width = element.hasAttributeNS(null, ELEMENT_IMG_ATTRIBUTE_WIDTH) ? element.getAttributeNS(null, ELEMENT_IMG_ATTRIBUTE_WIDTH) : null;
		final Element objectElement = element.getOwnerDocument().createElementNS(elementNamespace, ELEMENT_OBJECT); //create the element object in the same namespace as the image object
		if(src != null) //if there is a src attribute
			objectElement.setAttributeNS(null, ELEMENT_OBJECT_ATTRIBUTE_DATA, src); //set the object data attribute
		if(height != null) //if there is a height attribute
			objectElement.setAttributeNS(null, ELEMENT_OBJECT_ATTRIBUTE_HEIGHT, height); //set the object height attribute
		if(width != null) //if there is a width attribute
			objectElement.setAttributeNS(null, ELEMENT_OBJECT_ATTRIBUTE_WIDTH, width); //set the object width attribute
		appendText(objectElement, alt); //add the alternate text as child text to the object element
		return objectElement; //return the element we created
	}

	/**
	 * Creates an <code>&lt;object&gt;</code> element from an <code>&lt;applet&gt;</code> element.
	 * @param element The applet element.
	 * @return A new object element converted from the applet.
	 */
	protected static Element createAppletObjectElement(final Element element) {
		final String elementNamespace = element.getNamespaceURI(); //get the element's namespace
		//get the code attribute, or null if not present
		final String code = element.hasAttributeNS(null, ELEMENT_APPLET_ATTRIBUTE_CODE) ? element.getAttributeNS(null, ELEMENT_APPLET_ATTRIBUTE_CODE) : null;
		//get the height attribute, or null if not present
		final String height = element.hasAttributeNS(null, ELEMENT_APPLET_ATTRIBUTE_HEIGHT) ? element.getAttributeNS(null, ELEMENT_APPLET_ATTRIBUTE_HEIGHT) : null;
		//get the width attribute, or null if not present
		final String width = element.hasAttributeNS(null, ELEMENT_APPLET_ATTRIBUTE_WIDTH) ? element.getAttributeNS(null, ELEMENT_APPLET_ATTRIBUTE_WIDTH) : null;
		final Element objectElement = element.getOwnerDocument().createElementNS(elementNamespace, ELEMENT_OBJECT); //create the element object in the same namespace as the applet object
		if(code != null) //if there is a code attribute
			objectElement.setAttributeNS(null, ELEMENT_OBJECT_ATTRIBUTE_CLASSID, code); //set the object classid attribute TODO correctly add needed "java:" and ".class"
		objectElement.setAttributeNS(null, ELEMENT_OBJECT_ATTRIBUTE_CODETYPE, ContentType.toString(ContentType.APPLICATION_PRIMARY_TYPE, JAVA_SUBTYPE)); //set the object codetype attribute to "application/java"
		if(height != null) //if there is a height attribute
			objectElement.setAttributeNS(null, ELEMENT_OBJECT_ATTRIBUTE_HEIGHT, height); //set the object height attribute
		if(width != null) //if there is a width attribute
			objectElement.setAttributeNS(null, ELEMENT_OBJECT_ATTRIBUTE_WIDTH, width); //set the object width attribute
		appendClonedChildNodes(objectElement, element, true); //clone the applet element's child nodes and add them to the new object node
		appendText(objectElement, "This OEB reading system does not support Java."); //add alternate text as child text to the object element TODO i18n; fix
		return objectElement; //return the element we created
	}

	//TODO comment, rename removeUntidyChildNodes
	/**
	 * Removes any child nodes that are determined to be untidy.
	 * @param element The element the children of which should be examined for removal.
	 * @return The number of child nodes removed.
	 */
	protected static int removeUnknownElements(final Element element) { //TODO probably rename this
		int removedChildCount = 0; //show that we've not removed any child notes, so far
		Node childNode = element.getFirstChild(); //get the first child
		while(childNode != null) { //while the element has child nodes
			Node nextNode = childNode.getNextSibling(); //get a reference to the next sibling so we'll have it when we need it
			if(shouldRemove(childNode)) { //if we should remove this node
				prepareNodeForRemoval(childNode); //do whatever we need to do before removing the node, such as promoting attributes if this is an element
				element.removeChild(childNode); //remove the node
				removedChildCount++; //show that we removed another child node
			} else if(shouldPrune(childNode)) { //if this child should be pruned TODO put this in a separate method
				final Node previousNode = childNode.getPreviousSibling(); //pruning a node may promote its children; we'll have to recalculate the next node in that case
				prepareNodeForRemoval(childNode); //do whatever we need to do before removing the node, such as promoting attributes if this is an element
				pruneChild(element, childNode); //prune the child, promoting the child's children
				removedChildCount++; //show that we removed another child node
				//the next node will be the first node after the old previous sibling, or if there was no previous sibling, the first child node of the element
				nextNode = previousNode != null ? previousNode.getNextSibling() : element.getFirstChild();
			}
			childNode = nextNode; //look at the next node
		}
		return removedChildCount; //show how many child nodes we removed
	}

	/**
	 * Prepares a node for removal by promoting "id", and/or "name" attributes (if this is an element) to either the next sibling, the previous sibling, or if
	 * there are no siblings, the parent. "xml:lang", "class" and/or "style" elements are only promoted to the parent element, and only if that parent has no
	 * other child elements. If the element to which the attribute values will be promoted already has values for attributes with those names, the values for the
	 * element being removed will be lost.
	 */
	protected static void prepareNodeForRemoval(final Node node) {
		if(node.getNodeType() == Node.ELEMENT_NODE) { //if this node is an element
			final Element element = (Element)node; //cast the node to an element
			//TODO maybe put this in some utilities function -- get closest node or something
			Node promotionNode = element.getNextSibling(); //get the element's next sibling
			//keep looking forwards until we find another element or we run out of nodes
			while(promotionNode != null && promotionNode.getNodeType() != Node.ELEMENT_NODE)
				promotionNode = promotionNode.getNextSibling(); //get the next sibling's next sibling
			if(promotionNode == null) { //if we found no previous sibling
				promotionNode = element.getPreviousSibling(); //get the element's previous sibling
				//keep looking backwards until we find another element or we run out of nodes
				while(promotionNode != null && promotionNode.getNodeType() != Node.ELEMENT_NODE)
					promotionNode = promotionNode.getPreviousSibling(); //get the previous sibling's previous sibling
			}
			if(promotionNode == null) //if we found no next sibling, either
				promotionNode = element.getParentNode(); //get the element's parent
			final Node stylePromotionNode = element.getParentNode(); //get the element to which we will promote styles and classes
			//if there is an element to which to promote attributes from this element
			if(promotionNode != null && promotionNode.getNodeType() == Node.ELEMENT_NODE) {
				final Element promotionElement = (Element)promotionNode; //cast the node to an element
				//promote "id" and "name" attributes
				//TODO del when works				copyAttributeValue(promotionElement, element, null, ATTRIBUTE_CLASS);
				copyAttributeValue(promotionElement, element, null, ATTRIBUTE_ID);
				copyAttributeValue(promotionElement, element, null, ATTRIBUTE_NAME);
				//TODO del when works				copyAttributeValue(promotionElement, element, null, ATTRIBUTE_STYLE);
			}
			//if there is an element to which to promote styles from this element
			if(stylePromotionNode != null && stylePromotionNode.getNodeType() == Node.ELEMENT_NODE) {
				final Element stylePromotionElement = (Element)promotionNode; //cast the node to an element
				if(stylePromotionElement.getChildNodes().getLength() == 1) { //if we're removing the only child TODO is this even useful?
					//promote "xml:lang", "class", and "style" attributes
					copyAttributeValue(stylePromotionElement, element, XML_NAMESPACE_URI.toString(), "lang"); //TODO use a constant here
					copyAttributeValue(stylePromotionElement, element, null, ATTRIBUTE_CLASS);
					//TODO del; this is not useful, and can cause harm (by moving up the symbol font property, for example)					copyAttributeValue(stylePromotionElement, element, null, ATTRIBUTE_STYLE);
				}
			}
		}
	}

	/**
	 * Copies the value of an attribute to another element, if the attribute exists in the source element but does not exist in the destination element. The
	 * attribute is not removed from the source element.
	 * @param destElement The element to which the attribute value should be copied.
	 * @param sourceElement The element from which the attribute value should be copied.
	 * @param namespaceURI The URI of the attribute's namespace.
	 * @param localname The local name of the attribute.
	 */
	protected static void copyAttributeValue(final Element destElement, final Element sourceElement, final String namespaceURI, final String localName) {
		if(sourceElement.hasAttributeNS(namespaceURI, localName)) { //if the attribute exists in the source element
			final String value = sourceElement.getAttributeNS(namespaceURI, localName); //get the attribute value from the source element
			if(!destElement.hasAttributeNS(namespaceURI, localName)) { //if the attribute does not exist in the destination element
				destElement.setAttributeNS(namespaceURI, localName, value); //create the attribute with the value in the destination element
			}
		}
	}

	/**
	 * Prunes any child nodes that are determined to be untidy, promoting their children to the same level as the removed node.
	 * @param element The element the children of which should be examined for pruning.
	 * @return The number of child nodes pruned.
	 */
	/*TODO fix, maybe
		protected static int pruneUntidyChildNodes(final Element element)
		{
			int prunedChildCount=0;  //show that we've not pruned any child notes, so far
			Node childNode=element.getFirstChild();	//get the first child
			while(childNode!=null) {	//while the element has child nodes
			  Node nextNode=childNode.getNextSibling();	//get a reference to the next sibling so we'll have it when we need it
				if(shouldPrune(childNode)) {	//if this child should be pruned TODO put this in a separate method
					pruneChild(element, childNode);  //prune the child, promoting the child's children
					removedChildCount++;  //show that we removed another child node
				}
				childNode=nextNode;	//look at the next node
			}
			return removedChildCount; //show how many child nodes we removed
		}
	*/

	/**
	 * Determines if a particular node should be removed, along with all its children.
	 * @param node The node to examine.
	 * @return <code>true</code> if the node being examined should be removed, along with its entire descendant hierarchy.
	 */
	protected static boolean shouldRemove(final Node node) {
		final Node parentNode = node.getParentNode(); //get the node's parent
		final short nodeType = node.getNodeType(); //get the type of node this is
		final String nodeName = node.getNodeName(); //get the name of the node
		if(nodeType == Node.ELEMENT_NODE) { //if node is an element
			final Element element = (Element)node; //cast the node to an element
			if(nodeName.indexOf(':') != -1) //if this is from another namespace TODO use a constant TODO make this really namespace aware or something
				return true; //show that we should remove this node
			if(nodeName.equals(ELEMENT_STYLE)) //if this is a style element
				return true; //remove all style elements
			if(node.getChildNodes().getLength() == 0) { //if there are no child nodes
				if(!nodeName.equals(ELEMENT_APPLET) //if this is not one of the elements we expect to be empty TODO get the list of these from somewhere else
						&& !nodeName.equals(ELEMENT_BR) && !nodeName.equals(ELEMENT_HR) && !nodeName.equals(ELEMENT_IMG)
						&& !nodeName.equals(ELEMENT_PARAM)
						&& !nodeName.equals(ELEMENT_OBJECT)) {
					return true; //show that we should remove the empty node (such as a paragraph or span)
				}
			}
			//see if this is an empty (except for whitespace) block element
			if(isBlockElement(element)) { //if this is a block element such as <h1> or <p>
				if(element.getChildNodes().getLength() == 1) { //if the element has just one child node TODO should we look at all text children, or should we assume that the previous normalization has already taken care of this?
					final Node childNode = element.getFirstChild(); //get the first child
					if(childNode.getNodeType() == childNode.TEXT_NODE) { //if the child node is a text node
						final Text textNode = (Text)childNode; //cast the node to a text node
						//trim all beginning whitespace from the text node;
						//  if the text was only whitespace or non-breaking spaces
						if(Strings.trimWhitespaceNoBreak(textNode.getData()).length() == 0)
							return true; //remove the entire element
					}
				}
			}
		} else if(nodeType == Node.TEXT_NODE) { //if this is a text node
			final Text text = (Text)node; //cast the node to a text node
			final String data = text.getData(); //get the text data
			if(parentNode != null && parentNode.getChildNodes().getLength() == 1) { //if this is the only child of the parent (i.e. don't remove empty text nodes when there are other sibling nodes)
				if(isTextContainer(parentNode)) { //if this text is inside a text container (i.e. <p>, <span>, etc. and not not <ul> or something similar)
				/*TODO fix; probably change to pruning; change class comments
									final String trimmedData=data.replace(StringUtilities.NON_BREAKING_SPACE, ' ').trim();  //trim the string, after replacing all non-breaking spaces with normal spaces
									if(trimmedData.length()==0) {	//if this text node has only whitespace
										return true;  //we'll remove empty text children TODO only do this for <p>, <span>, etc. -- not <ul> and such
									}
				*/
				}
			}
		} else if(nodeType == Node.COMMENT_NODE) { //if node is a comment
			final Comment comment = (Comment)node; //cast the node to a comment
			final String data = comment.getData(); //get the comment data
			if(tidyMicrosoft) { //if we should tidy Microsoft-specific items
				if(data.startsWith("[if") || data.startsWith("[endif")) //if this is the Microsoft "if" or "endif" comment
					return true; //remove these comments
			}
		}
		return false; //if we can't find a reason to remove the node, show that we shouldn't remove it
	}

	/**
	 * Determines if a particular node should be prumed, promoting its children to be children of the node's parent.
	 * @param node The node to examine.
	 * @return <code>true</code> if the node being examined should be pruned and its children promoted to be children of the node's parent.
	 */
	protected static boolean shouldPrune(final Node node) {
		final short nodeType = node.getNodeType(); //get the type of node this is
		final String nodeName = node.getNodeName(); //get the name of the node
		final Node parentNode = node.getParentNode(); //get the node's parent node, which may be null
		final NodeList childNodeList = node.getChildNodes(); //get a reference to the child node list
		if(parentNode != null) { //only nodes with non-null parents can be pruned
			final String parentNodeName = parentNode.getNodeName(); //get the name of the parent node TODO should we check for null here?
			if(nodeType == Node.ELEMENT_NODE) { //if node is an element
				final Element element = (Element)node; //cast the node to an element
				if(nodeName.equals(ELEMENT_DIV)) { //if this is the div element
					if(parentNodeName.equals(ELEMENT_BODY) && parentNode.getNodeType() == parentNode.ELEMENT_NODE
							&& getChildCount((Element)parentNode, Node.ELEMENT_NODE) == 1) //if the div is the only child of the body
						return true; //<body><div> should be pruned
					//if the div element has only one attribute, "class", and its value contains "section" in any case
					if(element.getAttributes().getLength() == 1 && Strings.indexOfIgnoreCase(element.getAttributeNS(null, ATTRIBUTE_CLASS), "section") >= 0)
						return true; //<div class="*section*> should be pruned
						/*TODO decide if we want this or not -- after all, <object> an <img> should be inside a block
											boolean onlyWhitespace=true;  //we'll see if there is any non-text whitespace
											boolean onlyEmptyElements=true; //we'll see if there are any non-empty child elements
										  for(int childIndex=childNodeList.getLength()-1; childIndex>=0; --childIndex) {	//look at each of the child nodes
												final Node childNode=childNodeList.item(childIndex); //get a reference to this child
												if(childNode.getNodeType()==childNode.TEXT_NODE) {	//if the child is a text node
													final Text text=(Text)childNode;  //cast the child node to a text node
													final String data=text.getData();    //get the text data
													final int dataLength=data.length(); //find out how much text data there is
												  for(int i=0; i<dataLength; ++i) {	//look at each character in the data
														final char c=data.charAt(i);  //get a reference to this character
														if(!Character.isWhitespace(c) && c!=NO_BREAK_SPACE_CHAR) {	//if this is not whitespace, and it's not a non-breaking space
															onlyWhitespace=false; //show that this we found non-whitespace
															childIndex=0; //stop looking at all thie children
															break;  //stop searching
														}
													}
												}
												else if(childNode.getChildNodes().getLength()>0) {	//if this is not an empty element
													onlyEmptyElements=false;  //show that we found a non-empty element
													break;  //stop searching
												}
											}
											if(onlyWhitespace && onlyEmptyElements) {	//if there are only empty elements and whitespace inside the <div>
												return true;  //show that we should prune the <div>
											}
						*/
				} else if(isListElement(element)) { //if this is a list (<ol> or <ul>)
					//see how many child lists there are
					final int childListCount = getChildElementCount(element, element.getNamespaceURI(), element.getLocalName());
					//if there is only one child list, and it's the only child element we have
					if(childListCount == 1 && getChildCount(element, Node.ELEMENT_NODE) == childListCount)
						return true; //we should prune redundant nested lists
				} else if(nodeName.equals(ELEMENT_SPAN)) { //if this is a span element
					if(node.getAttributes().getLength() == 0) //if there are no attributes
						return true; //show that we should prune empty spans
					else if(parentNode.getChildNodes().getLength() == 1) //if the span encloses all the content of the parent element
						return true; //show that we should prune the single span child
				} else if(nodeName.equals(ELEMENT_TABLE)) { //if this is a table element
					final NodeList trNodeList = element.getElementsByTagName(ELEMENT_TR); //get a list of all tr row elements in the table
					if(trNodeList.getLength() == 1) { //if there's only one tr element
						final NodeList tdNodeList = element.getElementsByTagName(ELEMENT_TD); //get a list of all td cell elements in the row
						if(tdNodeList.getLength() == 1) //if there's only one td element
							return true; //prune the table from around the rows and columns (we'll remove the rows and columns on the next round
					}
				} else if(nodeName.equals(ELEMENT_TR)) { //if this is a table row element
					if(!parentNodeName.equals(ELEMENT_TABLE)) //if this is a row without a table (this compensates for pruning the table, above)
						return true; //prune the table row, continuing the work of pruning the table
				} else if(nodeName.equals(ELEMENT_TD)) { //if this is a table cell element
					if(!parentNodeName.equals(ELEMENT_TR)) //if this is a cell without a row (this compensates for pruning the table, above)
						return true; //prune the table cell, continuing the work of pruning the table and row
				} else if(nodeName.equals(ELEMENT_FONT)) { //if this is a font element
					return true; //always prune font elements
				}
				//TODO testing; fix, comment class
				if(isTextContainer(node)) { //if this is a text container (i.e. <p>, <span>, etc. and not not <ul> or something similar)
					if(node.getChildNodes().getLength() == 1) { //if there is only one child node
						final Node childNode = node.getFirstChild(); //get the child node
						if(childNode.getNodeType() == childNode.TEXT_NODE) { //if the child is a text node
							final Text text = (Text)childNode; //cast the child node to a text node
							final String data = text.getData(); //get the text data
							final int dataLength = data.length(); //find out how much text data there is
							boolean onlyWhitespace = true; //without looking at the data, we don't know if this is just whitespace or not; assume it is to begin with
							for(int i = 0; i < dataLength; ++i) { //look at each character in the data
								final char c = data.charAt(i); //get a reference to this character
								if(!Character.isWhitespace(c) && c != NO_BREAK_SPACE_CHAR) { //if this is not whitespace, and it's not a non-breaking space
									onlyWhitespace = false; //show that this we found non-whitespace
									break; //stop searching
								}
							}
							if(onlyWhitespace) { //if the text is only whitespace inside the text container
								return true; //show that we should prune the text container
							}
						}
					}
				}
			}
		}
		return false; //if we can't find a reason to prune the node, show that we shouldn't prune it
	}

	/**
	 * Counts the number of child nodes with the specified node type.
	 * @param element The element to have its child nodes examined.
	 * @param nodeType The type of node to count.
	 * @return The number of child nodes of the specified type.
	 */
	protected static int getChildCount(final Element element, final short nodeType) {
		final NodeList childNodeList = element.getChildNodes(); //get a list of child nodes
		int childCount = 0; //we'll use this to keep track of the number of children of the specified type
		for(int i = childNodeList.getLength() - 1; i >= 0; --i) { //look at each of the child elements
			if(childNodeList.item(i).getNodeType() == nodeType) //if this node is of the specified type
				++childCount; //show that we found another match
		}
		return childCount; //show how many nodes we found of the specified type
	}

	/**
	 * Counts the number of child elements that are block elements.
	 * @param element The element to have its child elements examined.
	 * @return The number of child elements that are block elements.
	 */
	protected static int getChildBlockElementCount(final Element element) {
		final NodeList childNodeList = element.getChildNodes(); //get a list of child nodes
		int childBlockElementCount = 0; //we'll use this to keep track of the number of child elements that are block elements
		for(int i = childNodeList.getLength() - 1; i >= 0; --i) { //look at each of the child elements
			final Node node = childNodeList.item(i); //get a reference to this node
			if(node.getNodeType() == Node.ELEMENT_NODE) { //if this is an element node
				if(isBlockElement((Element)node)) //if this element is a block element
					++childBlockElementCount; //show that we found another match
			}
		}
		return childBlockElementCount; //show how many block elements we found of the specified type
	}

	/**
	 * Counts the number of child elements with the specified name.
	 * @param element The element to have its child elements examined.
	 * @param elementNamespaceURI The namespace of the elements to count.
	 * @param elementLocalName The local name name of th eelements to count.
	 * @return The number of child elements with the specified name.
	 */
	protected static int getChildElementCount(final Element element, final String elementNamespaceURI, final String elementLocalName) {
		final NodeList childNodeList = element.getChildNodes(); //get a list of child nodes
		int childElementCount = 0; //we'll use this to keep track of the number of child elements with the specified name
		for(int i = childNodeList.getLength() - 1; i >= 0; --i) { //look at each of the child elements
			final Node node = childNodeList.item(i); //get a reference to this node
			if(node.getNodeType() == Node.ELEMENT_NODE) { //if this is an element node
				//if this element has the specified name in the specified namespace
				if(node.getLocalName().equals(elementLocalName) && node.getNamespaceURI().equals(elementNamespaceURI))
					++childElementCount; //show that we found another match
			}
		}
		return childElementCount; //show how many elements we found of the specified type
	}

	/**
	 * Determines if the given node is a block element such as <code>&lt;p&gt;</code>, <code>&lt;blockquote&gt;</code>, and <code>&lt;div&gt;</code>.
	 * @param node The node to examine.
	 * @return <code>true</code> if the node is a block element.
	 */
	protected static boolean isBlockElement(final Element element) {
		final String elementName = element.getNodeName(); //get the element name TODO use namespaces
		//TODO add others here
		return elementName.equals(ELEMENT_P) || elementName.equals(ELEMENT_BODY) || elementName.equals(ELEMENT_BLOCKQUOTE) || elementName.equals(ELEMENT_DIV)
				|| elementName.equals(ELEMENT_H1) || elementName.equals(ELEMENT_H2) || elementName.equals(ELEMENT_H3) || elementName.equals(ELEMENT_H4)
				|| elementName.equals(ELEMENT_H5) || elementName.equals(ELEMENT_H6) || elementName.equals(ELEMENT_LI);
	}

	/**
	 * Determines if the given node is a list element such as <code>&lt;ol&gt;</code> and <code>&lt;ul&gt;</code>.
	 * @param node The node to examine.
	 * @return <code>true</code> if the node is a list element.
	 */
	protected static boolean isListElement(final Element element) {
		final String elementName = element.getNodeName(); //get the element name TODO use namespaces
		return elementName.equals(ELEMENT_OL) || elementName.equals(ELEMENT_UL);
	}

	/**
	 * Determines if the given node is a text container, an element that is meant to contain text, such as <code>&lt;p&gt;</code>, <code>&lt;span&gt;</code>,
	 * <code>&lt;em&gt;</code>. Used to determine if a whitespace-only element should be removed.
	 * @param node The node to examine.
	 * @return <code>true</code> if the node is an element which normally holds text.
	 */
	protected static boolean isTextContainer(final Node node) {
		final String nodeName = node.getNodeName(); //get the name of the node
		if(node.getNodeType() == Node.ELEMENT_NODE && //if this is an element and the name matches one of our text container elements
				(
				//TODO fix					nodeName.equals(ELEMENT_DIV)  //TODO add others, like <em>, <code>, etc.
				nodeName.equals(ELEMENT_P) || nodeName.equals(ELEMENT_SPAN) || nodeName.equals(ELEMENT_B) || nodeName.equals(ELEMENT_EM) || nodeName.equals(ELEMENT_I)
						|| nodeName.equals(ELEMENT_STRONG) || nodeName.equals(ELEMENT_SUB) || nodeName.equals(ELEMENT_SUP))) {
			return true; //show that this is a text container
		}
		return false; //show that this isn't a text container
	}

}