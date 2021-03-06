/*
 * Copyright © 1996-2011 GlobalMentor, Inc. <http://www.globalmentor.com/>
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

package com.globalmentor.xml.dom.impl.stylesheets.css;

import static com.globalmentor.css.spec.CSS.*;

import java.util.HashMap;
import java.util.StringTokenizer;

import com.globalmentor.css.spec.CSS;
import com.globalmentor.xml.dom.impl.XMLDOMException;

import org.w3c.dom.DOMException;
import org.w3c.dom.css.*;

/**
 * A class representing a single CSS declaration block containing style properties.
 * @author Garret Wilson
 * @see org.w3c.dom.css.CSSStyleDeclaration
 * @see org.w3c.dom.css.CSS2Properties
 * @deprecated
 */
public class XMLCSSStyleDeclaration extends HashMap<String, CSSValue> implements CSSStyleDeclaration { //TODO fix, org.w3c.dom.css.CSS2Properties

	/**
	 * @return A parsable string representation of this style declaration block, excluding the surrounding curly braces. For the DOM version, see getCssText().
	 * @see XMLCSSStyleDeclaration#getCssText
	 */
	public String toString() {
		return getCssText();
	}

	/**
	 * Returns he parsable textual representation of the declaration block, excluding the surrounding curly braces.
	 * @return The textual representation of the declaration block.
	 * @version DOM Level 2
	 * @since DOM Level 2
	 */
	public String getCssText() {
		/*TODO del or fix all this
				String rulePostfix,
				if(size()>1) {	//if we have more than one rule in this style declaration
		
					rulePrefix=XMLCSSConstants.TAB_CHAR+XMLCSSConstants.TAB_CHAR;	//we'll tab twice for each rule prefix
					rulePostfix=
				String cssText=getSelectorText();	//get the text of the selectors
		*/
		final StringBuilder stringBuilder = new StringBuilder(); //create a new string buffer to collect our data
		for(int i = 0; i < size(); ++i) { //look at each rule
			final String propertyName = item(i); //get the name of this property
			if(i > 0) //if this isn't the first rule
				stringBuilder.append(SPACE_CHAR); //add a space
			stringBuilder.append(propertyName); //add the property name
			stringBuilder.append(PROPERTY_DIVIDER_CHAR); //add the property divider character
			stringBuilder.append(SPACE_CHAR); //add a space
			stringBuilder.append(getPropertyValue(propertyName)); //add the property value
			stringBuilder.append(DECLARATION_SEPARATOR_CHAR); //separate the declarations
			stringBuilder.append('\n'); //add a newline
			//TODO add the priority somewhere here
		}
		return stringBuilder.toString(); //return the string we constructed
	}

	/**
	 * Sets the parsable textual representation of the the declaration block (excluding the surrounding curly braces). Setting this attribute will result in the
	 * parsing of the new value and resetting of the properties in the declaration block.
	 * @param cssText The textual representation of the declaration block, excluding the surrounding curly braces.
	 * @throws DOMException
	 *           <ul>
	 *           <li>SYNTAX_ERR: Raised if the specified CSS string value has a syntax error and is unparsable.</li>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this media list is readonly.</li>
	 *           </ul>
	 */
	public void setCssText(String cssText) throws DOMException {
		//TODO check the string for a syntax error
		//TODO check for read-only status
		//TODO fix		return CssText;	//return the text
	}

	/**
	 * Returns the number of properties that have been explicitly set in this declaration block.
	 * @return The number of properties that have been explicitly set in this declaration block.
	 * @version DOM Level 2
	 * @since DOM Level 2
	 */
	public int getLength() {
		return size();
	}

	/**
	 * Retrieves properties that have been explicitly set in this declaration block. The order of the properties retrieved using this method does not have to be
	 * the order in which they were set. This method can be used to iterate over all properties in this declaration block.
	 * @param index Index of the property name to retrieve.
	 * @return The name of the property at this ordinal position, or the empty string if no property exists at this position.
	 * @version DOM Level 2
	 * @since DOM Level 2
	 */
	public String item(int index) {
		try {
			return (String)keySet().toArray()[index]; //return the object at the index TODO see if there's a more efficient way of doing this
		} catch(IndexOutOfBoundsException e) { //if they don't give a valid index
			return ""; //return an empty string instead of throwing an exception
		}
	}

	/**
	 * Makes copies of the properties in the provided style declaration and adds them to this style, replacing any properties with the same name that may already
	 * exist.
	 * @param styleDeclaration The style to import.
	 */
	public void importStyle(final XMLCSSStyleDeclaration styleDeclaration) {
		for(int i = 0; i < styleDeclaration.getLength(); ++i) { //look at each of the properties in the style to import
			final String propertyName = styleDeclaration.item(i); //get the name of this property
			setPropertyCSSValue(propertyName, styleDeclaration.getPropertyCSSValue(propertyName)); //get this property value from the style declaration, and set it in our style declaration, replacing any value(s) already set TODO do a clone() here before we set the value
		}
	}

	/**
	 * Retrieves the value of a CSS property if it has been explicitly set within this declaration block.
	 * @param propertyName The name of the CSS property. See the CSS property index.
	 * @return Returns the value of the property if it has been explicitly set for this declaration block, or the empty string if the property has not been set.
	 * @see XMLCSSStyleDeclaration#setProperty
	 * @version DOM Level 2
	 * @since DOM Level 2
	 */
	public String getPropertyValue(String propertyName) {
		if(containsKey(propertyName)) //if we have the specified property
			return getPropertyCSSValue(propertyName).getCssText(); //return the text of the value
		else
			//if we don't have the specified property
			return ""; //return the empty string
	}

	/**
	 * Retrieves the object representation of the value of a CSS property if it has been explicitly set within this declaration block. This method returns
	 * <code>null</code> if the property is a shorthand property. Shorthand property values can only be accessed and modified as strings, using the
	 * getPropertyValue() and setProperty() methods.
	 * @param propertyName The name of the CSS property. See the CSS property index.
	 * @return Returns the value of the property if it has been explicitly set for this declaration block, or <code>null</code> if the property has not been set.
	 * @see XMLCSSStyleDeclaration#getPropertyValue
	 * @see XMLCSSStyleDeclaration#setProperty
	 * @version DOM Level 2
	 * @since DOM Level 2
	 */
	public CSSValue getPropertyCSSValue(String propertyName) {
		return get(propertyName); //get the item with the specified property name
	}

	/**
	 * Removes a CSS property if it has been explicitly set within this declaration block.
	 * @param propertyName The name of the CSS property. See the CSS property index.
	 * @return Returns the value of the property if it has been explicitly set for this declaration block, or the empty string if the property has not been set or
	 *         the property name does not correspond to a valid CSS2 property.
	 * @throws DOMException
	 *           <ul>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this declaration is readonly.</li>
	 *           </ul>
	 * @see XMLCSSStyleDeclaration#getPropertyCSSValue
	 * @see XMLCSSStyleDeclaration#getPropertyValue
	 * @version DOM Level 2
	 * @since DOM Level 2
	 */
	public String removeProperty(String propertyName) throws DOMException {
		//TODO check read-only status here
		//TODO what about checking the propertyName to make sure it's a valid CSS property
		final CSSValue oldCSSValue = (CSSValue)remove(propertyName); //remove the specified value and store it
		if(oldCSSValue != null) //if there was already a value with this name already
			return oldCSSValue.getCssText(); //return the text of the value
		else
			//if we didn't have the specified property to begin with
			return ""; //return the empty string
	}

	/**
	 * Retrieves the priority of a CSS property (e.g. the "important" qualifier) if the property has been explicitly set in this declaration block.
	 * @param propertyName The name of the CSS property. See the CSS property index.
	 * @return A string representing the priority (e.g. "important") if one exists, or the empty string if none exists.
	 * @see XMLCSSStyleDeclaration#getPropertyCSSValue
	 * @see XMLCSSStyleDeclaration#getPropertyValue
	 * @version DOM Level 2
	 * @since DOM Level 2
	 */
	public String getPropertyPriority(String propertyName) {
		final XMLCSSValue cssValue = (XMLCSSValue)getPropertyCSSValue(propertyName); //see if we have a property with this name
		if(cssValue != null) //if there is a value with this name
			return cssValue.getPriority(); //return the priority of the value
		else
			//if we don't have the specified property
			return ""; //return the empty string
	}

	/**
	 * Sets a property value and priority within this declaration block.
	 * @param propertyName The name of the CSS property. See the CSS property index.
	 * @param value The new value of the property.
	 * @param priority The new priority of the property (e.g. "important").
	 * @throws DOMException
	 *           <ul>
	 *           <li>SYNTAX_ERR: Raised if the specified value has a syntax error and is unparsable.</li>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this declaration is readonly.</li>
	 *           </ul>
	 * @version DOM Level 2
	 * @since DOM Level 2
	 */
	//TODO right now, all string values passed will be interpreted as idents -- how can we determine if they are strings? should we examine whether they could be an ident or not, or should we allow quotes to be passed here?
	public void setProperty(String propertyName, String value, String priority) throws DOMException {
		//TODO check for read-only status here
		//TODO fix; make this parse the value string
		//TODO what if this isn't a primitive value?

		//check for shorthand properties
		/*TODO redo
				if(propertyName.equals(CSS_PROP_BACKGROUND)) {	//if this is the background shorthand property TODO fix for other types
				  setBackgroundProperty(value, priority);  //set the background property
					return;
				}
				else
		*/
		if(CSS_PROP_BACKGROUND.equals(propertyName) || CSS_PROP_BORDER.equals(propertyName) || CSS_PROP_BORDER_BOTTOM.equals(propertyName)
				|| CSS_PROP_BORDER_LEFT.equals(propertyName) || CSS_PROP_BORDER_RIGHT.equals(propertyName) || CSS_PROP_BORDER_RIGHT.equals(propertyName)
				|| CSS_PROP_FONT.equals(propertyName) || CSS_PROP_MARGIN.equals(propertyName) || CSS_PROP_PADDING.equals(propertyName)) { //if this a shorthand property TODO fix correctly
			setPropertyCSSValue(propertyName, new XMLCSSPrimitiveValue(CSSPrimitiveValue.CSS_UNKNOWN, value)); //create a general XML CSS primitive value
			return;
		}
		/*TODO fix
				if(propertyName.equals(CSS_PROP_BORDER)) {	//if this is the border shorthand property TODO fix for other types
					final XMLCSSValueList borderValueList=XMLCSSValueList.createValueList(value); //create a list of the shorthand values
						//TODO check for "inherit"
		
				}
		*/

		final XMLCSSValue cssValue = parseValue(propertyName, value); //parse the incoming value

		/*TODO fix
				XMLCSSValue cssValue=null;	//we'll assign a value to this variable based upon which type of CSS value should be created TODO is this the right way to do this? make sure the identical code in XMLCSSProcessor is in sync
				if(propertyName.equals(CSS_PROP_TEXT_DECORATION)) {	//if this is the text decoration property TODO fix for other types
					cssValue=XMLCSSValueList.createValueList(value);	//create a new list of values
				}
				else
					cssValue=XMLCSSPrimitiveValue.createPrimitiveValue(propertyName, value);	//create a new primitive value
		*/
		//TODO del if not needed		if(cssValue!=null)	//if we were able to construct a valid value object
		{
			cssValue.setPriority(priority); //set the priority of the property
			setPropertyCSSValue(propertyName, cssValue); //set the property, which places the value in the map
			//TODO del when works			put(propertyName, cssValue);	//put the property in our map, replacing any existing value
		}
		/*TODO del if not needed
				else	//if we couldn't parse the value
					throw new XMLDOMException(XMLDOMException.SYNTAX_ERR, new Object[]{value});	//show that this property value has a syntax error
		*/
	}

	/**
	 * Sets a property value and priority within this declaration block. For the DOM version see setPropertyValue().
	 * @param propertyName The name of the CSS property. See the CSS property index.
	 * @param cssValue The new value of the property. //TODO del or fix @param priority The new priority of the property (e.g. "important").
	 * @throws DOMException
	 *           <ul>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this declaration is readonly.</li>
	 *           </ul>
	 * @see XMLCSSStyleDeclaration setPropertyCSSValue
	 */
	public void setPropertyCSSValue(String propertyName, final CSSValue cssValue/*TODO del or fix, String priority*/) throws DOMException {
		//TODO check for read-only status here
		//TODO fix
		/*TODO del when works
				XMLCSSValue oldValue=(XMLCSSValue)getPropertyCSSValue(propertyName);	//get the current value, if any
				if(oldValue==null)	//if there isn't a value already
					oldValue=new XMLCSSPrimitiveValue(XMLCSSPrimitiveValue.CSS_IDENT);	//TODO testing
				primitiveValue.setStringValue(XMLCSSPrimitiveValue.CSS_IDENT, value);	//TODO testing
				primitiveValue.setPriority(priority);	//TODO testing
		*/
		put(propertyName, cssValue); //store the value
	}

	/**
	 * Sets a property value and priority within this declaration block with no priority. For the DOM version see setProperty(propertyName, value).
	 * @param propertyName The name of the CSS property. See the CSS property index.
	 * @param value The new value of the property.
	 * @throws DOMException
	 *           <ul>
	 *           <li>SYNTAX_ERR: Raised if the specified value has a syntax error and is unparsable.</li>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this declaration is readonly.</li>
	 *           </ul>
	 * @see XMLCSSStyleDeclaration#setProperty(String, String, String)
	 */
	public void setProperty(String propertyName, String value) throws DOMException {
		setProperty(propertyName, value, ""); //set the property with no priority
	}

	/**
	 * Parses a CSS value in the form of a string and converts it to a CSS value object.
	 * @param propertyName The name of the CSS property. See the CSS property index.
	 * @param valueString The new value of the property. //TODO fix @param priority The new priority of the property (e.g. "important"). //TODO fix, decide which
	 *          to use @throws DOMException
	 *          <ul>
	 *          <li>SYNTAX_ERR: Raised if the specified value has a syntax error and is unparsable.</li>
	 *          </ul>
	 * @return The parsed CSS value in the form of a CSS value object.
	 */
	protected static XMLCSSValue parseValue(final String propertyName, final String valueString) throws DOMException {
		XMLCSSValue value = null; //we'll assign a value to this variable based upon which type of CSS value should be created TODO is this the right way to do this? make sure the identical code in XMLCSSSTyleDeclaration is in sync
		//TODO fix; this is incorrect		if(propertyName.equals(CSS_PROP_TEXT_DECORATION)	//if this is the text decoration property TODO fix for other types
		if(CSS_PROP_FONT_FAMILY.equals(propertyName)) { //if this is a font-family property
			value = XMLCSSValueList.createValueList(valueString); //create a new list of values
		} else
			value = XMLCSSPrimitiveValue.createPrimitiveValue(/*TODO del if not needed propertyName, */valueString); //create a new primitive value
		if(value != null) //if we were able to construct a valid value object
			return value; //return the value
		else
			//if there was an error processing the value string
			throw new XMLDOMException(XMLDOMException.SYNTAX_ERR, new Object[] {value}); //show that this property value has a syntax error
	}

	/**
	 * Sets the background shorthand property value and priority within this declaration block by calling <code>setProperty()</code> for each of the shorthand
	 * property's elements.
	 * @param value The new value of the property.
	 * @param priority The new priority of the property (e.g. "important").
	 * @throws DOMException
	 *           <ul>
	 *           <li>SYNTAX_ERR: Raised if the specified value has a syntax error and is unparsable.</li>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this declaration is readonly.</li>
	 *           </ul>
	 */
	protected void setBackgroundProperty(String value, String priority) throws DOMException {
		final StringTokenizer stringTokenizer = new StringTokenizer(value, WHITESPACE_CHARS); //create a string tokenizer to look at the individual values, which can apparently be in any order
		while(stringTokenizer.hasMoreTokens()) { //while there are more tokens TODO this currently will not work with quoted strings with spaces
			final String token = stringTokenizer.nextToken(); //get the next token
			final XMLCSSPrimitiveValue primitiveValue = XMLCSSPrimitiveValue.createPrimitiveValue(token); //create a primitive value from this value
			if(primitiveValue != null) { //if we were able to parse this primitive value TODO fix exception handling here, and what to do on error
				primitiveValue.setPriority(priority); //set the priority of the property
				//if the value is an RGB color or a color identifier value, it's the background color
				if(primitiveValue.getPrimitiveType() == primitiveValue.CSS_RGBCOLOR || (primitiveValue.getPrimitiveType() == primitiveValue.CSS_IDENT
						&& CSS.CSS_COLOR_NAME_VALUES.containsKey(primitiveValue.getStringValue().toLowerCase()))) {
					setPropertyCSSValue(CSS_PROP_BACKGROUND_COLOR, primitiveValue); //set the background color property
				}
			}
			/*TODO del when works
						if(XMLCSSUtilities.getColor(token)!=null) //if this is a color
							setProperty(CSS_PROP_BACKGROUND_COLOR, token, priority);  //set the background color property
			*/
			//TODO check for "inherit"
		}
		//	public static Color getColor(final String colorString)
	}

	/**
	 * Sets the margin shorthand property value and priority within this declaration block by calling <code>setProperty()</code> for each of the shorthand
	 * property's elements.
	 * @param value The new value of the property.
	 * @param priority The new priority of the property (e.g. "important").
	 * @throws DOMException
	 *           <ul>
	 *           <li>SYNTAX_ERR: Raised if the specified value has a syntax error and is unparsable.</li>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this declaration is readonly.</li>
	 *           </ul>
	 */
	/*TODO fix
		protected void setMarginProperty(String value, String priority) throws DOMException
		{
			final String[] values=WHITESPACE_PATTERN.split(value);	//split the values into tokens
			if(values.length)
			
		  final StringTokenizer stringTokenizer=new StringTokenizer(value, WHITESPACE_CHARS); //create a string tokenizer to look at the individual values
		  while(stringTokenizer.hasMoreTokens()) {	//while there are more tokens TODO this currently will not work with quoted strings with spaces
				final String token=stringTokenizer.nextToken(); //get the next token
				final XMLCSSPrimitiveValue primitiveValue=XMLCSSPrimitiveValue.createPrimitiveValue(token); //create a primitive value from this value
			  if(primitiveValue!=null) {	//if we were able to parse this primitive value TODO fix exception handling here, and what to do on error
					primitiveValue.setPriority(priority);	//set the priority of the property
						//if the value is an RGB color or a color identifier value, it's the background color
					if(primitiveValue.getPrimitiveType()==primitiveValue.CSS_RGBCOLOR ||
						(primitiveValue.getPrimitiveType()==primitiveValue.CSS_IDENT && XMLCSSUtilities.getColor(primitiveValue.getStringValue())!=null))
					{
					  setPropertyCSSValue(CSS_PROP_BACKGROUND_COLOR, primitiveValue); //set the background color property
					}
			  }
				//TODO check for "inherit"
			}
		//public static Color getColor(final String colorString)
		}
	*/

	/**
	 * The CSS rule that contains this declaration block or <code>null</code> if this CSSStyleDeclaration is not attached to a <code>CSSRule</code>.
	 */
	private CSSRule ParentRule = null;

	/**
	 * Returns the CSS rule that contains this declaration block or <code>null</code> if this CSSStyleDeclaration is not attached to a <code>CSSRule</code>.
	 * @version DOM Level 2
	 * @since DOM Level 2
	 */

	//TODO fix it so that whenever a StyleRule adds a CSSSStyleDeclaration, this value gets set
	public CSSRule getParentRule() {
		return ParentRule;
	}

	//TODO add other properties here

	/**
	 * See the background property definition in CSS2.
	 * @throws DOMException
	 *           <ul>
	 *           <li>SYNTAX_ERR: Raised if the new value has a syntax error and is unparsable.</li>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this property is readonly.</li>
	 *           </ul>
	 */
	/*TODO fix
	    public String getBackground();
	    public void setBackground(String background)
	                                             throws DOMException;
	*/

	/** @return The value of the CSS border property. */
	public String getBorder() {
		return getPropertyValue(CSS_PROP_BORDER);
	}

	/**
	 * Sets the CSS border property.
	 * @param border The option width, border style, and color, or "inherit".
	 * @throws DOMException
	 *           <ul>
	 *           <li>SYNTAX_ERR: Raised if the new value has a syntax error and is unparsable.</li>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this property is readonly.</li>
	 *           </ul>
	 */
	public void setBorder(String border) throws DOMException {
		setProperty(CSS_PROP_BORDER, border); //set the property
	}

	/**
	 * See the border-collapse property definition in CSS2.
	 * @throws DOMException
	 *           <ul>
	 *           <li>SYNTAX_ERR: Raised if the new value has a syntax error and is unparsable.</li>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this property is readonly.</li>
	 *           </ul>
	 */
	/*TODO fix
	    public String getBorderCollapse();
	    public void setBorderCollapse(String borderCollapse) throws DOMException;
	*/

	/**
	 * See the border-color property definition in CSS2.
	 * @throws DOMException
	 *           <ul>
	 *           <li>SYNTAX_ERR: Raised if the new value has a syntax error and is unparsable.</li>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this property is readonly.</li>
	 *           </ul>
	 */
	/*TODO fix
	    public String getBorderColor();
	    public void setBorderColor(String borderColor) throws DOMException;
	*/

	/**
	 * See the border-spacing property definition in CSS2.
	 * @throws DOMException
	 *           <ul>
	 *           <li>SYNTAX_ERR: Raised if the new value has a syntax error and is unparsable.</li>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this property is readonly.</li>
	 *           </ul>
	 */
	/*TODO fix
	    public String getBorderSpacing();
	    public void setBorderSpacing(String borderSpacing) throws DOMException;
	*/

	/**
	 * See the border-style property definition in CSS2.
	 * @throws DOMException
	 *           <ul>
	 *           <li>SYNTAX_ERR: Raised if the new value has a syntax error and is unparsable.</li>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this property is readonly.</li>
	 *           </ul>
	 */
	/*TODO fix
	    public String getBorderStyle();
	    public void setBorderStyle(String borderStyle) throws DOMException;
	*/

	/**
	 * See the border-top property definition in CSS2.
	 * @throws DOMException
	 *           <ul>
	 *           <li>SYNTAX_ERR: Raised if the new value has a syntax error and is unparsable.</li>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this property is readonly.</li>
	 *           </ul>
	 */
	/*TODO fix
	    public String getBorderTop();
	    public void setBorderTop(String borderTop) throws DOMException;
	*/

	/**
	 * See the border-right property definition in CSS2.
	 * @throws DOMException
	 *           <ul>
	 *           <li>SYNTAX_ERR: Raised if the new value has a syntax error and is unparsable.</li>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this property is readonly.</li>
	 *           </ul>
	 */
	/*TODO fix
	    public String getBorderRight();
	    public void setBorderRight(String borderRight) throws DOMException;
	*/

	/**
	 * See the border-bottom property definition in CSS2.
	 * @throws DOMException
	 *           <ul>
	 *           <li>SYNTAX_ERR: Raised if the new value has a syntax error and is unparsable.</li>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this property is readonly.</li>
	 *           </ul>
	 */
	/*TODO fix
	    public String getBorderBottom();
	    public void setBorderBottom(String borderBottom) throws DOMException;
	*/

	/**
	 * See the border-left property definition in CSS2.
	 * @throws DOMException
	 *           <ul>
	 *           <li>SYNTAX_ERR: Raised if the new value has a syntax error and is unparsable.</li>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this property is readonly.</li>
	 *           </ul>
	 */
	/*TODO fix
	    public String getBorderLeft();
	    public void setBorderLeft(String borderLeft) throws DOMException;
	*/

	/**
	 * See the border-top-color property definition in CSS2.
	 * @throws DOMException
	 *           <ul>
	 *           <li>SYNTAX_ERR: Raised if the new value has a syntax error and is unparsable.</li>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this property is readonly.</li>
	 *           </ul>
	 */
	/*TODO fix
	    public String getBorderTopColor();
	    public void setBorderTopColor(String borderTopColor) throws DOMException;
	*/

	/**
	 * See the border-right-color property definition in CSS2.
	 * @throws DOMException
	 *           <ul>
	 *           <li>SYNTAX_ERR: Raised if the new value has a syntax error and is unparsable.</li>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this property is readonly.</li>
	 *           </ul>
	 */
	/*TODO fix
	    public String getBorderRightColor();
	    public void setBorderRightColor(String borderRightColor) throws DOMException;
	*/

	/**
	 * See the border-bottom-color property definition in CSS2.
	 * @throws DOMException
	 *           <ul>
	 *           <li>SYNTAX_ERR: Raised if the new value has a syntax error and is unparsable.</li>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this property is readonly.</li>
	 *           </ul>
	 */
	/*TODO fix
	    public String getBorderBottomColor();
	    public void setBorderBottomColor(String borderBottomColor) throws DOMException;
	*/

	/**
	 * See the border-left-color property definition in CSS2.
	 * @throws DOMException
	 *           <ul>
	 *           <li>SYNTAX_ERR: Raised if the new value has a syntax error and is unparsable.</li>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this property is readonly.</li>
	 *           </ul>
	 */
	/*TODO fix
	    public String getBorderLeftColor();
	    public void setBorderLeftColor(String borderLeftColor) throws DOMException;
	*/

	/**
	 * See the border-top-style property definition in CSS2.
	 * @throws DOMException
	 *           <ul>
	 *           <li>SYNTAX_ERR: Raised if the new value has a syntax error and is unparsable.</li>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this property is readonly.</li>
	 *           </ul>
	 */
	/*TODO fix
	    public String getBorderTopStyle();
	    public void setBorderTopStyle(String borderTopStyle) throws DOMException;
	*/

	/**
	 * See the border-right-style property definition in CSS2.
	 * @throws DOMException
	 *           <ul>
	 *           <li>SYNTAX_ERR: Raised if the new value has a syntax error and is unparsable.</li>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this property is readonly.</li>
	 *           </ul>
	 */
	/*TODO fix
	    public String getBorderRightStyle();
	    public void setBorderRightStyle(String borderRightStyle) throws DOMException;
	*/

	/**
	 * See the border-bottom-style property definition in CSS2.
	 * @throws DOMException
	 *           <ul>
	 *           <li>SYNTAX_ERR: Raised if the new value has a syntax error and is unparsable.</li>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this property is readonly.</li>
	 *           </ul>
	 */
	/*TODO fix
	    public String getBorderBottomStyle();
	    public void setBorderBottomStyle(String borderBottomStyle) throws DOMException;
	*/

	/**
	 * See the border-left-style property definition in CSS2.
	 * @throws DOMException
	 *           <ul>
	 *           <li>SYNTAX_ERR: Raised if the new value has a syntax error and is unparsable.</li>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this property is readonly.</li>
	 *           </ul>
	 */
	/*TODO fix
	    public String getBorderLeftStyle();
	    public void setBorderLeftStyle(String borderLeftStyle) throws DOMException;
	*/

	/**
	 * See the border-top-width property definition in CSS2.
	 * @throws DOMException
	 *           <ul>
	 *           <li>SYNTAX_ERR: Raised if the new value has a syntax error and is unparsable.</li>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this property is readonly.</li>
	 *           </ul>
	 */
	/*TODO fix
	    public String getBorderTopWidth();
	    public void setBorderTopWidth(String borderTopWidth) throws DOMException;
	*/

	/**
	 * See the border-right-width property definition in CSS2.
	 * @throws DOMException
	 *           <ul>
	 *           <li>SYNTAX_ERR: Raised if the new value has a syntax error and is unparsable.</li>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this property is readonly.</li>
	 *           </ul>
	 */
	/*TODO fix
	    public String getBorderRightWidth();
	    public void setBorderRightWidth(String borderRightWidth) throws DOMException;
	*/

	/**
	 * See the border-bottom-width property definition in CSS2.
	 * @throws DOMException
	 *           <ul>
	 *           <li>SYNTAX_ERR: Raised if the new value has a syntax error and is unparsable.</li>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this property is readonly.</li>
	 *           </ul>
	 */
	/*TODO fix
	    public String getBorderBottomWidth();
	    public void setBorderBottomWidth(String borderBottomWidth) throws DOMException;
	*/

	/**
	 * See the border-left-width property definition in CSS2.
	 * @throws DOMException
	 *           <ul>
	 *           <li>SYNTAX_ERR: Raised if the new value has a syntax error and is unparsable.</li>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this property is readonly.</li>
	 *           </ul>
	 */
	/*TODO fix
	    public String getBorderLeftWidth();
	    public void setBorderLeftWidth(String borderLeftWidth) throws DOMException;
	*/

	/**
	 * See the border-width property definition in CSS2.
	 * @throws DOMException
	 *           <ul>
	 *           <li>SYNTAX_ERR: Raised if the new value has a syntax error and is unparsable.</li>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this property is readonly.</li>
	 *           </ul>
	 */
	/*TODO fix
	    public String getBorderWidth();
	    public void setBorderWidth(String borderWidth) throws DOMException;
	*/

	/**
	 * @return The CSS2 "color" property.
	 * @version DOM Level 2
	 * @since DOM Level 2
	 */
	public String getColor() {
		return getPropertyValue(CSS_PROP_COLOR);
	}

	/**
	 * Sets the CSS2 "color" property
	 * @param color The new value, either a predefined color name or an RGB value.
	 * @throws DOMException
	 *           <ul>
	 *           <li>SYNTAX_ERR: Raised if the new value has a syntax error and is unparsable.</li>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this property is readonly.</li>
	 *           </ul>
	 */
	public void setColor(String color) throws DOMException {
		setProperty(CSS_PROP_COLOR, color); //set the property
	}

	//TODO add other properties here

	/**
	 * @return The CSS2 "display" property.
	 * @version DOM Level 2
	 * @since DOM Level 2
	 */
	public String getDisplay() {
		return getPropertyValue(CSS_PROP_DISPLAY);
	}

	/**
	 * Returns whether or not this style declaration has an inline display style. This is implemented to only return <code>true</code> if the display is
	 * specifically set to CSS_DISPLAY_INLINE or if there is no display property.
	 * @return Whether or not this style declaration has an inline display style.
	 * @see #getDisplay()
	 */
	public boolean isDisplayInline() { //TODO del and use XMLUtilities.isDisplayInline instead
		final String displayString = getDisplay(); //get the display property
		return displayString.length() == 0 || displayString.equals(CSS_DISPLAY_INLINE); //return true if there is no display string or it is equal to "inline"
	}

	/**
	 * Sets the CSS2 "display" property
	 * @param display The new value, one of:
	 *          <ul>
	 *          <li>CSS_DISPLAY_BLOCK</li>
	 *          <li>CSS_DISPLAY_INLINE</li>
	 *          </ul>
	 * @throws DOMException
	 *           <ul>
	 *           <li>SYNTAX_ERR: Raised if the new value has a syntax error and is unparsable.</li>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this property is readonly.</li>
	 *           </ul>
	 */
	public void setDisplay(String display) throws DOMException {
		setProperty(CSS_PROP_DISPLAY, display); //set the property
	}

	//TODO add other properties here

	/**
	 * @return The CSS2 "font-family" property.
	 * @version DOM Level 2
	 * @since DOM Level 2
	 */
	public String getFontFamily() {
		return getPropertyValue(CSS_PROP_FONT_FAMILY);
	}

	/**
	 * Sets the CSS2 "font-family" property
	 * @param fontFamily The new value, either a family name or one of the following:
	 *          <ul>
	 *          <li>serif</li>
	 *          <li>sans-serif</li>
	 *          <li>cursive</li>
	 *          <li>fantasy</li>
	 *          <li>monospace</li>
	 *          </ul>
	 * @throws DOMException
	 *           <ul>
	 *           <li>SYNTAX_ERR: Raised if the new value has a syntax error and is unparsable.</li>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this property is readonly.</li>
	 *           </ul>
	 */
	public void setFontFamily(String fontFamily) throws DOMException {
		setProperty(CSS_PROP_FONT_FAMILY, fontFamily); //set the property
	}

	/**
	 * @return The CSS2 "font-size" property.
	 * @version DOM Level 2
	 * @since DOM Level 2
	 */
	public String getFontSize() {
		return getPropertyValue(CSS_PROP_FONT_SIZE);
	}

	/**
	 * Sets the CSS2 "font-size" property
	 * @param fontSize The new value, either a length, a percentage, or an absolute size:
	 *          <ul>
	 *          <li>xx-small</li>
	 *          <li>x-small</li>
	 *          <li>small</li>
	 *          <li>medium</li>
	 *          <li>large</li>
	 *          <li>x-large</li>
	 *          <li>xx-large</li>
	 *          </ul>
	 *          or a relative size:
	 *          <ul>
	 *          <li>smaller</li>
	 *          <li>larger</li>
	 *          </ul>
	 * @throws DOMException
	 *           <ul>
	 *           <li>SYNTAX_ERR: Raised if the new value has a syntax error and is unparsable.</li>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this property is readonly.</li>
	 *           </ul>
	 */
	public void setFontSize(String fontSize) throws DOMException {
		setProperty(CSS_PROP_FONT_SIZE, fontSize); //set the property
	}

	//TODO add other properties here

	/**
	 * @return The CSS2 "font-style" property.
	 * @version DOM Level 2
	 * @since DOM Level 2
	 */
	public String getFontStyle() {
		return getPropertyValue(CSS_PROP_FONT_STYLE);
	}

	/**
	 * Sets the CSS2 "font-style" property
	 * @param fontStyle The new value, one of:
	 *          <ul>
	 *          <li>CSS_FONT_STYLE_NORMAL</li>
	 *          <li>CSS_FONT_STYLE_ITALIC</li>
	 *          <li>CSS_FONT_STYLE_OBLIQUE</li>
	 *          </ul>
	 * @throws DOMException
	 *           <ul>
	 *           <li>SYNTAX_ERR: Raised if the new value has a syntax error and is unparsable.</li>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this property is readonly.</li>
	 *           </ul>
	 */
	public void setFontStyle(String fontStyle) throws DOMException {
		setProperty(CSS_PROP_FONT_STYLE, fontStyle); //set the property
	}

	//TODO add other properties here

	/**
	 * @return The CSS2 "font-weight" property.
	 * @version DOM Level 2
	 * @since DOM Level 2
	 */
	public String getFontWeight() {
		return getPropertyValue(CSS_PROP_FONT_WEIGHT);
	}

	/**
	 * Sets the CSS2 "font-weight" property
	 * @param fontWeight The new value, one of:
	 *          <ul>
	 *          <li>CSS_FONT_WEIGHT_NORMAL</li>
	 *          <li>CSS_FONT_WEIGHT_BOLD</li>
	 *          <li>CSS_FONT_WEIGHT_BOLDER</li>
	 *          <li>CSS_FONT_WEIGHT_LIGHTER</li>
	 *          </ul>
	 * @throws DOMException
	 *           <ul>
	 *           <li>SYNTAX_ERR: Raised if the new value has a syntax error and is unparsable.</li>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this property is readonly.</li>
	 *           </ul>
	 */
	public void setFontWeight(String fontWeight) throws DOMException {
		setProperty(CSS_PROP_FONT_WEIGHT, fontWeight); //set the property
	}

	//TODO add other properties here

	/**
	 * @return The CSS2 "line-height" property.
	 * @version DOM Level 2
	 * @since DOM Level 2
	 */
	public String getLineHeight() {
		return getPropertyValue(CSS_PROP_LINE_HEIGHT);
	}

	/**
	 * Sets the CSS2 "line-height" property.
	 * @param lineHeight The new value, either a length, a number, a percentage, or <code>XMLCSSConstants.CSS_LINE_HEIGHT_NORMAL</code>.
	 * @throws DOMException
	 *           <ul>
	 *           <li>SYNTAX_ERR: Raised if the new value has a syntax error and is unparsable.</li>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this property is readonly.</li>
	 *           </ul>
	 */
	public void setLineHeight(String lineHeight) throws DOMException {
		setProperty(CSS_PROP_LINE_HEIGHT, lineHeight); //set the property
	}

	//TODO add other properties here

	/**
	 * @return The CSS2 "list-style" property.
	 * @version DOM Level 2
	 * @since DOM Level 2
	 */
	//TODO fix	public String getListStyle() {return getPropertyValue(CSS_PROP_LIST_STYLE);

	/**
	 * Sets the CSS2 "list-style" property.
	 * @param listStyle The new style value See the list-style property definition in CSS2.
	 * @throws DOMException SYNTAX_ERR: Raised if the new value has a syntax error and is unparsable.
	 *           <ul>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this property is readonly.</li>
	 *           </ul>
	 */
	//TODO fix    public void setListStyle(String listStyle) throws DOMException;

	/**
	 * See the list-style-image property definition in CSS2.
	 * @throws DOMException SYNTAX_ERR: Raised if the new value has a syntax error and is unparsable.
	 *           <ul>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this property is readonly.</li>
	 *           </ul>
	 */
	/*TODO fix
	    public String getListStyleImage();
	    public void setListStyleImage(String listStyleImage)
	                                             throws DOMException;
	*/

	/**
	 * See the list-style-position property definition in CSS2.
	 * @throws DOMException SYNTAX_ERR: Raised if the new value has a syntax error and is unparsable.
	 *           <ul>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this property is readonly.</li>
	 *           </ul>
	 */
	/*TODO fix
	    public String getListStylePosition();
	    public void setListStylePosition(String listStylePosition)
	                                             throws DOMException;
	*/

	/** @return The <code>list-style-type</code> property definition in CSS2. */
	public String getListStyleType() {
		return getPropertyValue(CSS_PROP_LIST_STYLE_TYPE);
	}

	/**
	 * Sets the <code>list-style-type</code> property definition in CSS2.
	 * @param listStyleType The type of list style, one of the <code>XMLCSSConstants.CSS_LIST_STYLE_TYPE_</code> constants.
	 * @throws DOMException
	 *           <ul>
	 *           <li>SYNTAX_ERR: Raised if the new value has a syntax error and is unparsable.</li>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this property is read-only.</li>
	 *           </ul>
	 */
	public void setListStyleType(String listStyleType) throws DOMException {
		setProperty(CSS_PROP_LIST_STYLE_TYPE, listStyleType); //set the property
	}

	/**
	 * See the margin property definition in CSS2.
	 * @throws DOMException SYNTAX_ERR: Raised if the new value has a syntax error and is unparsable.
	 *           <ul>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this property is readonly.</li>
	 *           </ul>
	 */
	/*TODO fix
	    public String getMargin();
	    public void setMargin(String margin)
	                                             throws DOMException;
	*/

	/** @return The value of the CSS <code>margin-top</code> property. */
	public String getMarginTop() {
		return getPropertyValue(CSS_PROP_MARGIN_TOP);
	}

	/**
	 * Sets the <code>margin-top</code> property in CSS.
	 * @param marginTop The new top margin value.
	 * @throws DOMException
	 *           <ul>
	 *           <li>SYNTAX_ERR: Raised if the new value has a syntax error and is unparsable.</li>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this property is readonly.</li>
	 *           </ul>
	 */
	public void setMarginTop(String marginTop) throws DOMException {
		setProperty(CSS_PROP_MARGIN_TOP, marginTop); //set the property
	}

	/** @return The value of the CSS <code>margin-right</code> property. */
	public String getMarginRight() {
		return getPropertyValue(CSS_PROP_MARGIN_RIGHT);
	}

	/**
	 * Sets the <code>margin-right</code> property in CSS.
	 * @param marginRight The new right margin value.
	 * @throws DOMException
	 *           <ul>
	 *           <li>SYNTAX_ERR: Raised if the new value has a syntax error and is unparsable.</li>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this property is readonly.</li>
	 *           </ul>
	 */
	public void setMarginRight(String marginRight) throws DOMException {
		setProperty(CSS_PROP_MARGIN_RIGHT, marginRight); //set the property
	}

	/** @return The value of the CSS <code>margin-bottom</code> property. */
	public String getMarginBottom() {
		return getPropertyValue(CSS_PROP_MARGIN_BOTTOM);
	}

	/**
	 * Sets the <code>margin-bottom</code> property in CSS.
	 * @param marginBottom The new bottom margin value.
	 * @throws DOMException
	 *           <ul>
	 *           <li>SYNTAX_ERR: Raised if the new value has a syntax error and is unparsable.</li>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this property is readonly.</li>
	 *           </ul>
	 */
	public void setMarginBottom(String marginBottom) throws DOMException {
		setProperty(CSS_PROP_MARGIN_BOTTOM, marginBottom); //set the property
	}

	/** @return The value of the CSS <code>margin-left</code> property. */
	public String getMarginLeft() {
		return getPropertyValue(CSS_PROP_MARGIN_LEFT);
	}

	/**
	 * Sets the <code>margin-left</code> property in CSS.
	 * @param marginLeft The new left margin value.
	 * @throws DOMException
	 *           <ul>
	 *           <li>SYNTAX_ERR: Raised if the new value has a syntax error and is unparsable.</li>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this property is readonly.</li>
	 *           </ul>
	 */
	public void setMarginLeft(String marginLeft) throws DOMException {
		setProperty(CSS_PROP_MARGIN_LEFT, marginLeft); //set the property
	}

	/** @return the value of the <code>page-break-after</code> property. */
	public String getPageBreakAfter() {
		return getPropertyValue(CSS_PROP_PAGE_BREAK_AFTER);
	}

	/**
	 * Sets the <code>page-break-after</code> property definition in CSS2.
	 * @param pageBreakAfter The page break after value, one of the <code>XMLCSSConstants.CSS_PAGE_BREAK_AFTER_</code> constants.
	 * @throws DOMException
	 *           <ul>
	 *           <li>SYNTAX_ERR: Raised if the new value has a syntax error and is unparsable.</li>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this property is readonly.</li>
	 *           </ul>
	 */
	public void setPageBreakAfter(String pageBreakAfter) throws DOMException {
		setProperty(CSS_PROP_PAGE_BREAK_AFTER, pageBreakAfter); //set the property
	}

	/** @return the value of the <code>page-break-before</code> property. */
	public String getPageBreakBefore() {
		return getPropertyValue(CSS_PROP_PAGE_BREAK_BEFORE);
	}

	/**
	 * Sets the <code>page-break-before</code> property definition in CSS2.
	 * @param pageBreakBefore The page break before value, one of the <code>XMLCSSConstants.CSS_PAGE_BREAK_BEFORE_</code> constants.
	 * @throws DOMException
	 *           <ul>
	 *           <li>SYNTAX_ERR: Raised if the new value has a syntax error and is unparsable.</li>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this property is readonly.</li>
	 *           </ul>
	 */
	public void setPageBreakBefore(String pageBreakBefore) throws DOMException {
		setProperty(CSS_PROP_PAGE_BREAK_BEFORE, pageBreakBefore); //set the property
	}

	/**
	 * See the page-break-inside property definition in CSS2.
	 * @throws DOMException SYNTAX_ERR: Raised if the new value has a syntax error and is unparsable.
	 *           <ul>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this property is readonly.</li>
	 *           </ul>
	 */
	//TODO fix    public String getPageBreakInside();
	//TODO fix    public void setPageBreakInside(String pageBreakInside) throws DOMException;

	/**
	 * See the text-decoration property definition in CSS2.
	 * @throws DOMException SYNTAX_ERR: Raised if the new value has a syntax error and is unparsable.
	 *           <ul>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this property is readonly.</li>
	 *           </ul>
	 */
	/*TODO fix
		public String getTextDecoration()
		{
	
		}
	*/

	/**
	 * Gets the CSS "text-decoration" property.
	 * @param textDecoration The new list of values in string format.
	 * @throws DOMException
	 *           <ul>
	 *           <li>SYNTAX_ERR: Raised if the new value has a syntax error and is unparsable.</li>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this property is readonly.</li>
	 *           </ul>
	 */
	public void setTextDecoration(String textDecoration) throws DOMException {
		setProperty(CSS_PROP_TEXT_DECORATION, textDecoration); //set the property
	}

	//TODO add other properties here

	/**
	 * @return The CSS2 "text-indent" property.
	 * @version DOM Level 2
	 * @since DOM Level 2
	 */
	public String getTextIndent() {
		return getPropertyValue(CSS_PROP_TEXT_INDENT);
	}

	/**
	 * Gets the CSS2 "text-indent" property
	 * @param textIndent The new value, either a length or a percentage.
	 * @throws DOMException
	 *           <ul>
	 *           <li>SYNTAX_ERR: Raised if the new value has a syntax error and is unparsable.</li>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this property is readonly.</li>
	 *           </ul>
	 */
	public void setTextIndent(String textIndent) throws DOMException {
		setProperty(CSS_PROP_TEXT_INDENT, textIndent); //set the property
	}

	//TODO add other properties here

	/** @return The <code>text-transform</code> property value of CSS2. */
	public String getTextTransform() {
		return getPropertyValue(CSS_PROP_TEXT_TRANSFORM);
	}

	/**
	 * Sets the <code>text-transform</code> property of CSS2.
	 * @param textTransform The text transformation value, one of the <code>XMLCSSConstants.CSS_TEXT_TRANSFORM_</code> constants.
	 * @throws DOMException
	 *           <ul>
	 *           <li>SYNTAX_ERR: Raised if the new value has a syntax error and is unparsable.</li>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this property is readonly.</li>
	 *           </ul>
	 */
	public void setTextTransform(String textTransform) throws DOMException {
		setProperty(CSS_PROP_TEXT_TRANSFORM, textTransform); //set the property
	}

	/** @return The <code>vertical-align</code> CSS property value. */
	public String getVerticalAlign() {
		return getPropertyValue(CSS_PROP_VERTICAL_ALIGN);
	}

	/**
	 * Sets the <code>vertical-align</code> property value in CSS.
	 * @param verticalAlign The vertical alignment value, one of the <code>XMLCSSConstants.CSS_VERTICAL_ALIGN_</code> constants.
	 * @throws DOMException
	 *           <ul>
	 *           <li>SYNTAX_ERR: Raised if the new value has a syntax error and is unparsable.</li>
	 *           <li>NO_MODIFICATION_ALLOWED_ERR: Raised if this property is readonly.</li>
	 *           </ul>
	 */
	public void setVerticalAlign(String verticalAlign) throws DOMException {
		setProperty(CSS_PROP_VERTICAL_ALIGN, verticalAlign); //set the property
	}

}
