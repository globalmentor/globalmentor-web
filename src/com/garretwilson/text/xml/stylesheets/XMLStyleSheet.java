package com.garretwilson.text.xml.stylesheets;

//G***del if we don't need import java.io.*;
//G***del import java.util.Vector;
import org.w3c.dom.Node;
import org.w3c.dom.stylesheets.*;
//G***del if we don't need import com.garretwilson.text.xml.XMLNode;

/**A single stylesheet within XML.
@see XMLNode
@see org.w3c.dom.stylesheets.StyleSheet
*/
public abstract class XMLStyleSheet implements org.w3c.dom.stylesheets.StyleSheet
{
	/**Constructor for a stylesheet specifying an owner node.
	@param ownerNode The node that associates this stylesheet with the document.
	*/
	public XMLStyleSheet(final Node ownerNode)
	{
		OwnerNode=ownerNode;	//set the owner node
	}

	/**Constructor for a stylesheet specifying a parent stylesheet.
	@param parentStyleSheet The stylesheet that included this stylesheet.
	*/
	public XMLStyleSheet(final StyleSheet parentStyleSheet)
	{
		ParentStyleSheet=parentStyleSheet;	//set the parent stylesheet
	}


	/**
	 *  This specifies the style sheet language for this style sheet. The style
	 * sheet language is specified as a content type (e.g. "text/css").  The
	 * content type is often specified in the <code>ownerNode</code>.  A list
	 * of registered content types can be found at
	 * ftp://ftp.isi.edu/in-notes/iana/assignments/media-types/. Also see the
	 * type attribute definition for the <code>LINK</code> element in HTML 4.0,
	 * and the type pseudo-attribute for the XML style sheet processing
	 * instruction.
	 */
	public String             getType() {return "text/css";}	//G***fix

	/**
	 *  <code>false</code> if the style sheet is applied to the document.
	 * <code>true</code> if it is not. Modifying this attribute may cause a new
	 * resolution of style for the document. For the , the medium has a higher
	 * priority than the <code>disabled</code> attribute. So, if the media
	 * doesn't apply to the current user agent, the <code>disabled</code>
	 * attribute is ignored.
	 */
	public boolean            getDisabled() {return false;} //G***fix
	public void               setDisabled(boolean disabled) {}	//G***fix

	/**
	 *  The node that associates this style sheet with the document. For HTML,
	 * this may be the corresponding <code>LINK</code> or <code>STYLE</code>
	 * element. For XML, it may be the linking processing instruction. For
	 * style sheets that are included by other style sheets, the value of this
	 * attribute is <code>null</code>.
	 */

	/**The node that associates this stylesheet with the document, such as an
	HTML LINK or STYLE element, or an XML linking processing instruction. If the
	stylesheet is included by another stylesheet, this will be <code>null</code>.
	*/
	private Node OwnerNode=null;

	/**The node that associates this stylesheet with the document, such as an
	HTML LINK or STYLE element, or an XML linking processing instruction.
	@return The owner node, or <code>null</code> if this stylesheet was included by
	another stylesheet.
	@version DOM Level 2
	@since DOM Level 2
	*/
	public Node getOwnerNode() {return OwnerNode;}


	/**The including stylesheet if this stylesheet was included from another
	stylesheet. If this is a top-level stylesheet, this value will be <code>null</code>.
	*/
	private StyleSheet ParentStyleSheet=null;

	/**The including stylesheet if this stylesheet was included from another stylesheet.
	@return The parent stylesheet, or <code>null</code> if this stylesheet has no parent.
	@version DOM Level 2
	@since DOM Level 2
	*/
	public StyleSheet getParentStyleSheet() {return ParentStyleSheet;}




	/**
	 *  If the style sheet is a linked style sheet, the value of its attribute
	 * is its location. For inline style sheets, the value of this attribute is
	 * <code>null</code>. See the href attribute definition for the
	 * <code>LINK</code> element in HTML 4.0, and the href pseudo-attribute for
	 * the XML style sheet processing instruction.
	 */
	public String             getHref() {return null;}	//G***fix

	/**
	 *  The advisory title.  The title is often specified in the
	 * <code>ownerNode</code>.  See the title attribute definition for the
	 * <code>LINK</code> element in HTML 4.0, and the title pseudo-attribute
	 * for the XML style sheet processing instruction.
	 */
	public String             getTitle() {return "";}	//G***fix


	/**
	 *  The intended destination media for style information.  The media is
	 * often specified in the <code>ownerNode</code>. If no media has been
	 * specified, the <code>MediaList</code> will be empty. See the media
	 * attribute definition for the <code>LINK</code> element in HTML 4.0, and
	 * the media pseudo-attribute for the XML style sheet processing
	 * instruction .
	 */
	public MediaList          getMedia() {return null;}	//G***fix


}

