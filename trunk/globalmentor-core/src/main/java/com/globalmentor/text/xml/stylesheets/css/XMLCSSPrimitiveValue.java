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

package com.globalmentor.text.xml.stylesheets.css;

import static com.globalmentor.text.xml.stylesheets.css.XMLCSS.*;

import com.globalmentor.java.CharSequences;
import com.globalmentor.java.Strings;

import org.w3c.dom.DOMException;
import org.w3c.dom.css.*;

/**The class which represents a single CSS value and is used to determine or set
a specific style property in a block. This class is returned from the
getPropertyCSSValue() of the XMLCSSStyleDeclaration class.
@author Garret Wilson
@version DOM Level 2
@since DOM Level 2
@see XMLCSSStyleDeclaration#getPropertyCSSValue()
@see org.w3c.dom.css.CSSValue
@deprecated
*/
public class XMLCSSPrimitiveValue extends XMLCSSValue implements org.w3c.dom.css.CSSPrimitiveValue//TODO fix, Cloneable
{
	//Strings that specify the various accepted unit types. TODO perhaps later give these more appropriate names
		//TODO put these in a constants file
	public final static String UNIT_EMS_STRING="em";
	public final static String UNIT_EXS_STRING="ex";
	public final static String UNIT_PX_STRING="px";
	public final static String UNIT_CM_STRING="cm";
	public final static String UNIT_MM_STRING="mm";
	public final static String UNIT_IN_STRING="in";
	public final static String UNIT_PT_STRING="pt";
	public final static String UNIT_PC_STRING="pc";
	public final static String UNIT_DEG_STRING="deg";
	public final static String UNIT_RAD_STRING="rad";
	public final static String UNIT_GRAD_STRING="grad";
	public final static String UNIT_MS_STRING="ms";
	public final static String UNIT_S_STRING="s";
	public final static String UNIT_HZ_STRING="Hz";
	public final static String UNIT_KHZ_STRING="kHz";
	public final static String UNIT_PERCENTAGE_STRING="%";

	/*Constructor which requires a specific primitive type.
	@param primitiveType The primitive type of this primitive CSS value.
	@see CSSPrimitiveValue
	*/
	public XMLCSSPrimitiveValue(final short primitiveType)
	{
		super(CSS_PRIMITIVE_VALUE);	//construct the parent
		setPrimitiveType(primitiveType);	//set the primitive type of this primitive CSS value
	}

	/*Default constructor which creates a primitive type of CSS_UNKNOWN.
	@see CSSPrimitiveValue#CSS_UNKNOWN
	*/
	public XMLCSSPrimitiveValue()
	{
		this(CSS_UNKNOWN);	//do the default constructing, setting a value of CSS_UNKNOWN
	}

	/**Constructor which sets a string value using the specified unit.
	@param stringType  A string code, which can only be a string unit type
		(e.g. <code>CSS_URI</code>,
		<code>CSS_IDENT</code>, <code>CSS_INHERIT</code> and
		<code>CSS_ATTR</code>).
	@param stringValue  The new string value. If the <code>stringType</code>
		is equal to <code>CSS_INHERIT</code>, the <code>stringValue</code>
		should be <code>inherit</code>.
	@exception DOMException
	<ul>
		<li>INVALID_ACCESS_ERR: Raises if the CSS value doesn't contain a string
			value or if the string value can't be converted into the specified unit.</li>
		<li>NO_MODIFICATION_ALLOWED_ERR: Raised if this property is readonly.</li>
	</ul>
	*/
	public XMLCSSPrimitiveValue(final short stringType, final String stringValue) throws DOMException
	{
		this(stringType);	//do the default constructing (although the type will get set again when we actually set the string value)
		setStringValue(stringType, stringValue);	//set the string type and value
	}

	/**Constructor which sets a float float value using the specified unit.
	@param unitType  A unit code as defined in CSSPrimitiveValue. The unit code
		can only be a float unit type
		(e.g. <code>NUMBER</code>, <code>PERCENTAGE</code>, <code>CSS_EMS</code>,
		<code>CSS_EXS</code>, <code>CSS_PX</code>, <code>CSS_PX</code>,
		<code>CSS_CM</code>, <code>CSS_MM</code>, <code>CSS_IN</code>,
		<code>CSS_PT</code>, <code>CSS_PC</code>, <code>CSS_DEG</code>,
		<code>CSS_RAD</code>, <code>CSS_GRAD</code>, <code>CSS_MS</code>,
		<code>CSS_S</code>, <code>CSS_HZ</code>, <code>CSS_KHZ</code>,
		<code>CSS_DIMENSION</code>).
	@param floatValue  The new float value.
	@exception DOMException
	<ul>
		<li>INVALID_ACCESS_ERR: Raises if the attached property doesn't support the
			float value or the unit type.</li>
		<li>NO_MODIFICATION_ALLOWED_ERR: Raised if this property is readonly.</li>
	</ul>
	*/
	public XMLCSSPrimitiveValue(final short unitType, final float floatValue) throws DOMException
	{
		this(unitType);	//do the default constructing (although the type will get set again when we actually set the float value)
		setFloatValue(unitType, floatValue);	//set the float type and value
	}


//TODO perhaps create other constructors which will automatically select a type

	/**The primitive type of this primitive CSS value..*/
	private short PrimitiveType;

	/**Returns the unit type of the value.
	@return A code representing the primitive type of the CSS value.
	@see org.w3c.dom.css.CSSPrimitiveValue
	@version DOM Level 2
	@since DOM Level 2
	*/
	public short getPrimitiveType() {return PrimitiveType;}

	/**Sets the unit type of the value. This is not a DOM function, but a
	protected function that allows derived objects to set the primitive type.
	@param primitiveType The new primitive type.
	*/
	protected void setPrimitiveType(final short primitiveType) {PrimitiveType=primitiveType;}

	/**Returns the parsable textual representation of the current value.
	@return The parsable textual representation of the value.
	@see XMLValue#getCssText
	@version DOM Level 2
	@since DOM Level 2
	*/
	public String getCssText()
	{
		if(isFloatType(getPrimitiveType()))	//if our value is a floating-point type
			return String.valueOf(FloatValue)+primitiveTypeToString(getPrimitiveType());	//return a string representation of our float value, along with the notation; we don't use getFloatValue() because it does redundant checking and can thrown an error TODO what about returning units as well?
/*TODO fix
			return String.valueOf(FloatValue)+primitiveTypeToString(getPrimitiveType());	//return a string representation of our float value, along with the notation; we don't use getFloatValue() because it does redundant checking and can thrown an error TODO what about returning units as well?
			return String.valueOf(FloatValue)+primitiveTypeToString(getPrimitiveType());	//return a string representation of our float value, along with the notation; we don't use getFloatValue() because it does redundant checking and can thrown an error TODO what about returning units as well?
*/
		else if(getPrimitiveType()==CSS_RGBCOLOR)	//if the type we contain is an RGB color
		{
			final RGBColor rgbColor=getRGBColorValue(); //get the RGB color value
			//TODO what if this is a color value which can only be stored in a rgb() format? is there even such a value, or can everything be stored in #XXXXXX format?
			return RGB_NUMBER_CHAR+ //return a string in the format #RRGGBB
				Strings.makeStringLength(Integer.toHexString((int)rgbColor.getRed().getFloatValue(CSSPrimitiveValue.CSS_NUMBER)).toUpperCase(), 2, '0', 0)+  //get the red value
				Strings.makeStringLength(Integer.toHexString((int)rgbColor.getGreen().getFloatValue(CSSPrimitiveValue.CSS_NUMBER)).toUpperCase(), 2, '0', 0)+  //get the red value
				Strings.makeStringLength(Integer.toHexString((int)rgbColor.getBlue().getFloatValue(CSSPrimitiveValue.CSS_NUMBER)).toUpperCase(), 2, '0', 0);  //get the red value
		}
		else if(getPrimitiveType()==CSS_STRING) //if this is a string
		  return '"'+StringValue+'"'; //return the value as a string inside quotes TODO make sure there aren't quotes inside the quotes
		else //if it's anything else, assume it's a string TODO fix for other types, and don't assume here; perhaps return "" for unknown types
			return StringValue;	//return our string value
	}

	/**The primitive value if this is a float-compatible value.*/	//TODO check about how best to store several types here
	private float FloatValue;

	/**The primitive value if this is a string-compatible value.*/	//TODO check about how best to store several types here
	private String StringValue=null;

	/**The primitive value if this is an RGB color value.*/	//TODO check about how best to store several types here
	private XMLCSSRGBColor rgbValue=null;

	/**Sets the float value with a specified unit. If the property attached with
		this value can not accept the specified unit or the float value, the value
		will be unchanged and a <code>DOMException</code> will be raised.
	@param unitType  A unit code as defined in CSSPrimitiveValue. The unit code
		can only be a float unit type
		(e.g. <code>NUMBER</code>, <code>PERCENTAGE</code>, <code>CSS_EMS</code>,
		<code>CSS_EXS</code>, <code>CSS_PX</code>, <code>CSS_PX</code>,
		<code>CSS_CM</code>, <code>CSS_MM</code>, <code>CSS_IN</code>,
		<code>CSS_PT</code>, <code>CSS_PC</code>, <code>CSS_DEG</code>,
		<code>CSS_RAD</code>, <code>CSS_GRAD</code>, <code>CSS_MS</code>,
		<code>CSS_S</code>, <code>CSS_HZ</code>, <code>CSS_KHZ</code>,
		<code>CSS_DIMENSION</code>).
	@param floatValue  The new float value.
	@exception DOMException
	<ul>
		<li>INVALID_ACCESS_ERR: Raises if the attached property doesn't support the
			float value or the unit type.</li>
		<li>NO_MODIFICATION_ALLOWED_ERR: Raised if this property is readonly.</li>
	</ul>
	@version DOM Level 2
	@since DOM Level 2
	*/
	public void setFloatValue(short unitType, float floatValue) throws DOMException
	{
		//TODO check for read-only status here
		if(isFloatType(unitType))	//if the type they specified is compatible with a float
		{
			setPrimitiveType(unitType);	//set the unit type
			FloatValue=floatValue;	//save the float value
		}
		else	//if this type isn't compatible with float
;//TODO throw an INVALID_ACCESS_ERR when we find out how to do so			throw new XMLDOMException(DOMException.INDEX_SIZE_ERR, new Object[]{new Integer(index)});	//use our own type of exception to show that this index is out of bounds
	}

	/**Returns a float value in a specified unit. If this CSS value doesn't
		contain a float value or can't be converted into the specified unit,
		a <code>DOMException</code> is raised.
	@param unitType A unit code to get the float value.  The unit code can
		only be a float unit type (e.g. <code>CSS_NUMBER</code>,
		<code>CSS_PERCENTAGE</code>, <code>CSS_EMS</code>, <code>CSS_EXS</code>,
		<code>CSS_PX</code>, <code>CSS_CM</code>,
		<code>CSS_MM</code>, <code>CSS_IN</code>,  <code>CSS_PT</code>,
		<code>CSS_PC</code>, <code>CSS_DEG</code>, <code>CSS_RAD</code>,
		<code>CSS_GRAD</code>, <code>CSS_MS</code>, <code>CSS_S</code>,
		<code>CSS_HZ</code>, <code>CSS_KHZ</code>, <code>CSS_DIMENSION</code>).
	@return  The float value in the specified unit.
	@exception DOMException
	<ul>
		<li>INVALID_ACCESS_ERR: Raises if the CSS value doesn't contain a float
			value or if the float value can't be converted into the specified unit.</li>
	</ul>
	@version DOM Level 2
	@since DOM Level 2
	*/
	public float getFloatValue(short unitType) throws DOMException
	{
		if(isFloatType(unitType))	//if the type they specified is compatible with a float
		{
			//TODO do the necessary conversions here, or throw an exception if we can't
			return FloatValue;	//return the float value
		}
		else	//if this type isn't compatible with float
return 0;//TODO throw an INVALID_ACCESS_ERR when we find out how to do so			throw new XMLDOMException(DOMException.INDEX_SIZE_ERR, new Object[]{new Integer(index)});	//use our own type of exception to show that this index is out of bounds
	}

	/**Sets the string value with a specified unit. If the property
		attached to this value can't accept the specified unit or the string
		value, the value will be unchanged and a <code>DOMException</code> will
		be raised.
	@param stringType  A string code as defined above. The string code can
		only be a string unit type (e.g. <code>CSS_URI</code>,
		<code>CSS_IDENT</code>, <code>CSS_INHERIT</code> and
		<code>CSS_ATTR</code>).
	@param stringValue  The new string value. If the <code>stringType</code>
		is equal to <code>CSS_INHERIT</code>, the <code>stringValue</code>
		should be <code>inherit</code>.
	@exception DOMException
	<ul>
		<li>INVALID_ACCESS_ERR: Raises if the CSS value doesn't contain a string
			value or if the string value can't be converted into the specified unit.</li>
		<li>NO_MODIFICATION_ALLOWED_ERR: Raised if this property is readonly.</li>
	</ul>
	@version DOM Level 2
	@since DOM Level 2
	*/
	public void setStringValue(short stringType, String stringValue) throws DOMException
	{
		//TODO check for read-only status here
		if(CSS_UNKNOWN==stringType || isStringType(stringType))	//if the type they specified is compatible with a string
		{
			//TODO check about this inherit business
			setPrimitiveType(stringType);	//set the unit type
			StringValue=stringValue;	//save the string value
		}
		else	//if this type isn't compatible with a string
;//TODO throw an INVALID_ACCESS_ERR when we find out how to do so			throw new XMLDOMException(DOMException.INDEX_SIZE_ERR, new Object[]{new Integer(index)});	//use our own type of exception to show that this index is out of bounds
	}

	/**Returns the string value in a specified unit. If the CSS value doesn't
		contain a string value, a <code>DOMException</code> is raised.
	@return  The string value in the current unit. The current <code>valueType</code>
		can only be a string unit type (e.g. <code>CSS_URI</code>,
		<code>CSS_IDENT</code> and <code>CSS_ATTR</code>).
	@exception DOMException
	<ul>
		<li>INVALID_ACCESS_ERR: Raises if the CSS value doesn't contain a string value.</li>
	</ul>
	@version DOM Level 2
	@since DOM Level 2
	*/
	public String getStringValue() throws DOMException
	{
		if(isStringType(getPrimitiveType()))	//if the type we contain is compatible with a string
			return StringValue;	//return the string value
		else	//if this type isn't compatible with string
return "";//TODO throw an INVALID_ACCESS_ERR when we find out how to do so			throw new XMLDOMException(DOMException.INDEX_SIZE_ERR, new Object[]{new Integer(index)});	//use our own type of exception to show that this index is out of bounds
	}

	/**
	 *  This method is used to get the Counter value. If this CSS value doesn't
	 * contain a counter value, a <code>DOMException</code> is raised.
	 * Modification to the corresponding style property can be achieved using
	 * the <code>Counter</code> interface.
	 * @return The Counter value.
	 * @exception DOMException
	 *    INVALID_ACCESS_ERR: Raises if the CSS value doesn't contain a Counter
	 *   value.
	 */
	public Counter            getCounterValue() throws DOMException {return null;}	//TODO fix

	/**
	 *  This method is used to get the Rect value. If this CSS value doesn't
	 * contain a rect value, a <code>DOMException</code> is raised.
	 * Modification to the corresponding style property can be achieved using
	 * the <code>Rect</code> interface.
	 * @return The Rect value.
	 * @exception DOMException
	 *    INVALID_ACCESS_ERR: Raises if the CSS value doesn't contain a Rect
	 *   value.
	 */
	public Rect               getRectValue() throws DOMException {return null;}	//TODO fix

	/**Returns the RGB color. If this CSS value doesn't contain an RGB color
		value, a <code>DOMException</code> is raised.
		Modification to the corresponding style property can be achieved using
		the <code>RGBColor</code> interface.
	@return The RGB color value.
	@exception DOMException
	<ul>
		<li>INVALID_ACCESS_ERR: Raised if the attached property can't return an RGB
			color value.</li>
	</ul>
	*/
	public RGBColor getRGBColorValue() throws DOMException
	{
		if(getPrimitiveType()==CSS_RGBCOLOR)	//if the type we contain is an RGB color
			return rgbValue;	//return the rgb value
		else	//if this isn't an RGB value
return null;//TODO throw an INVALID_ACCESS_ERR when we find out how to do so			throw new XMLDOMException(DOMException.INDEX_SIZE_ERR, new Object[]{new Integer(index)});	//use our own type of exception to show that this index is out of bounds
	}

	/*Determines whether or not this value is a floating point number.
	@return <code>true</code> if the unit type is compatible with float, else <code>false</code>.
	@see CSSPrimitiveValue
	*/
	public boolean isFloatType()
	{
		return isFloatType(getPrimitiveType());	//return whether our type is a float type
	}

	/*Determines whether or not this value is a string.
	@return <code>true</code> if the unit type is compatible with String, else <code>false</code>.
	@see CSSPrimitiveValue
	*/
	public boolean isStringType()
	{
		return isStringType(getPrimitiveType());	//return whether our type is a string type
	}

	/**Determines whether or not this value is relative.
	@return <code>true</code> if the unit type is a relative unit, else <code>false</code>.
	@see CSSPrimitiveValue
	*/
	public boolean isRelativeLength()
	{
		return isRelativeLength(getPrimitiveType());	//return whether our primitive type is relative
	}

	/**Determines whether or not this value is absolute.
	@return <code>true</code> if the unit type is an absolute unit, else <code>false</code>.
	@see CSSPrimitiveValue
	*/
	public boolean isAbsoluteLength()
	{
		return isAbsoluteLength(getPrimitiveType());	//return whether our primitive type is absolute
	}

	/**Returns a string representation of the given type.
	@param primitiveType A unit type as defined in CSSPrimitiveType.
	@return The standard CSS string representation of the given type, or the empty
		string for a type that has no notation or is unrecognized.
	@see CSSPrimitiveType#getPrimitiveType
	@see CSSPrimitiveType#setPrimitiveType
	*/
	public static String primitiveTypeToString(final short primitiveType)
	{
		switch(primitiveType)	//see which primitive type this is, and return the appropriate string
		{
			case CSS_PERCENTAGE:
				return UNIT_PERCENTAGE_STRING;
			case CSS_EMS:
				return UNIT_EMS_STRING;
			case CSS_EXS:
				return UNIT_EXS_STRING;
			case CSS_PX:
				return UNIT_PX_STRING;
			case CSS_CM:
				return UNIT_CM_STRING;
			case CSS_MM:
				return UNIT_MM_STRING;
			case CSS_IN:
				return UNIT_IN_STRING;
			case CSS_PT:
				return UNIT_PT_STRING;
			case CSS_PC:
				return UNIT_PC_STRING;
			case CSS_DEG:
				return UNIT_DEG_STRING;
			case CSS_RAD:
				return UNIT_RAD_STRING;
			case CSS_GRAD:
				return UNIT_GRAD_STRING;
			case CSS_MS:
				return UNIT_MS_STRING;
			case CSS_S:
				return UNIT_S_STRING;
			case CSS_HZ:
				return UNIT_HZ_STRING;
			case CSS_KHZ:
				return UNIT_KHZ_STRING;
			default:	//if we don't recognize the type
				return "";	//return the empty string
		}
	}

	/*Determines whether or not a particular unit type can accept a floating point number.

	@param unitType A unit code as defined in CSSPrimitiveValue.
	@return <code>true</code> if the unit type is compatible with float, else <code>false</code>.
	@see CSSPrimitiveValue
	*/
	public static boolean isFloatType(final short unitType)
	{
		switch(unitType)	//see which unit type they specified
		{
			case CSS_NUMBER:			//these are acceptable float types
			case CSS_PERCENTAGE:
			case CSS_EMS:
			case CSS_EXS:
			case CSS_PX:
			case CSS_CM:
			case CSS_MM:
			case CSS_IN:
			case CSS_PT:
			case CSS_PC:
			case CSS_DEG:
			case CSS_RAD:
			case CSS_GRAD:
			case CSS_MS:
			case CSS_S:
			case CSS_HZ:
			case CSS_KHZ:
			case CSS_DIMENSION:
				return true;		//show that this value can accept a float
			default:		//if it's not an acceptable float type
				return false;	//show that it can't accept a float
		}
	}

	/*Determines whether or not a particular unit type can accept a string.
	@param unitType A unit code as defined in CSSPrimitiveValue.
	@return <code>true</code> if the unit type is compatible with String, else <code>false</code>.
	@see CSSPrimitiveValue
	*/
	public static boolean isStringType(final short unitType)
	{
		switch(unitType)	//see which unit type they specified
		{
			case CSS_STRING:			//these are acceptable string types
			case CSS_URI:
			case CSS_IDENT:
			case CSS_INHERIT:	//TODO can we even have an inherit primitive type?
			case CSS_ATTR:
				return true;		//show that this value can accept a string
			default:		//if it's not an acceptable string type
				return false;	//show that it can't accept a string
		}
	}

	/**Determines whether or not a particular unit type is relative.
	@param unitType A unit code as defined in CSSPrimitiveValue.
	@return <code>true</code> if the unit type is a relative unit, else <code>false</code>.
	@see CSSPrimitiveValue
	*/
	public static boolean isRelativeLength(final short unitType)
	{
		switch(unitType)	//see which unit type they specified
		{
			case CSS_EMS:			//these are relative types
			case CSS_EXS:
			case CSS_PX:
				return true;		//show that this is a relative type
			default:		//if it isn't a relative type
				return false;	//show that it isn't a relative type
		}
	}

	/**Determines whether or not a particular unit type is absolute.
	@param unitType A unit code as defined in CSSPrimitiveValue.
	@return <code>true</code> if the unit type is an absolute unit, else <code>false</code>.
	@see CSSPrimitiveValue
	*/
	public static boolean isAbsoluteLength(final short unitType)
	{
		switch(unitType)	//see which unit type they specified
		{
			case CSS_IN:			//these are absolute types
			case CSS_CM:
			case CSS_MM:
			case CSS_PT:
			case CSS_PC:
				return true;		//show that this is an absolute type
			default:		//if it isn't an absolute type
				return false;	//show that it isn't an absolute type
		}
	}

	/**Creates a primitive value from a value string. TODO decide if we want this in XMLCSSPrimitiveValue or XMLCSSValue.createValue()
//G**del if not needed	@propertyName The name of the property to which the value will be assigned, or
//TODO del if not needed		<code>null</code> if the property name is not known.
	@valueString The string which contains the parsable CSS primitive value.
	@return The new primitive value, or <code>null</code> if the value string was not parsable. TODO do we want to instead throw an exception?
	*/
//TODO after figuring out the difference between ident and string, do we really need the property name passed here?
	public static XMLCSSPrimitiveValue createPrimitiveValue(/*TODO del if not needed final String propertyName, */String valueString)
	{
		final XMLCSSPrimitiveValue value=new XMLCSSPrimitiveValue();	//create a new primitive value
		short primitiveType=XMLCSSPrimitiveValue.CSS_UNKNOWN;	//show that we don't know what type of primitive value this is, yet (indeed, we don't even know if it is a primitive value)
		valueString=valueString.trim();	//trim the value string of whitespace
		if(valueString.length()>0)	//if we have at least one character in the value string
		{
			final char firstChar=valueString.charAt(0);	//get the first character in the string
			if(NUMBER_CHARS.contains(firstChar))	//if the first character is a digit or a decimal, we'll assume this is a number; now we'll need to determine what kind of units this represents
			{
				final int typeCharIndex=CharSequences.notCharIndexOf(valueString, NUMBER_CHARS, 0);	//find the first character that isn't a digit or a decimal point TODO use a constant here
				if(typeCharIndex!=-1)	//if we found a non-digit character int the string
				{
					final String typeString=valueString.substring(typeCharIndex, valueString.length());	//get a string representing the type of unit
					valueString=valueString.substring(0, typeCharIndex);	//chop the unit type part off of the value string
					if(typeString.equals(XMLCSSPrimitiveValue.UNIT_EMS_STRING))	//if this specifies the font size of the font
						primitiveType=XMLCSSPrimitiveValue.CSS_EMS;	//record the type
					else if(typeString.equals(XMLCSSPrimitiveValue.UNIT_EXS_STRING))	//if this specifies the x-height of the font
						primitiveType=XMLCSSPrimitiveValue.CSS_EXS;	//record the type
					else if(typeString.equals(XMLCSSPrimitiveValue.UNIT_PX_STRING))	//if this specifies pixels
						primitiveType=XMLCSSPrimitiveValue.CSS_PX;	//record the type
					else if(typeString.equals(XMLCSSPrimitiveValue.UNIT_CM_STRING))	//if this specifies centimeters
						primitiveType=XMLCSSPrimitiveValue.CSS_CM;	//record the type
					else if(typeString.equals(XMLCSSPrimitiveValue.UNIT_MM_STRING))	//if this specifies millimeters
						primitiveType=XMLCSSPrimitiveValue.CSS_MM;	//record the type
					else if(typeString.equals(XMLCSSPrimitiveValue.UNIT_IN_STRING))	//if this specifies inches
						primitiveType=XMLCSSPrimitiveValue.CSS_IN;	//record the type
					else if(typeString.equals(XMLCSSPrimitiveValue.UNIT_PT_STRING))	//if this specifies points
						primitiveType=XMLCSSPrimitiveValue.CSS_PT;	//record the type
					else if(typeString.equals(XMLCSSPrimitiveValue.UNIT_PC_STRING))	//if this specifies picas
						primitiveType=XMLCSSPrimitiveValue.CSS_PC;	//record the type
					else if(typeString.equals(XMLCSSPrimitiveValue.UNIT_DEG_STRING))	//if this specifies degrees
						primitiveType=XMLCSSPrimitiveValue.CSS_DEG;	//record the type
					else if(typeString.equals(XMLCSSPrimitiveValue.UNIT_RAD_STRING))	//if this specifies radians
						primitiveType=XMLCSSPrimitiveValue.CSS_RAD;	//record the type
					else if(typeString.equals(XMLCSSPrimitiveValue.UNIT_GRAD_STRING))	//if this specifies gradient
						primitiveType=XMLCSSPrimitiveValue.CSS_GRAD;	//record the type
					else if(typeString.equals(XMLCSSPrimitiveValue.UNIT_MS_STRING))	//if this specifies milliseconds
						primitiveType=XMLCSSPrimitiveValue.CSS_MS;	//record the type
					else if(typeString.equals(XMLCSSPrimitiveValue.UNIT_S_STRING))	//if this specifies seconds
						primitiveType=XMLCSSPrimitiveValue.CSS_S;	//record the type
					else if(typeString.equals(XMLCSSPrimitiveValue.UNIT_HZ_STRING))	//if this specifies picas
						primitiveType=XMLCSSPrimitiveValue.CSS_HZ;	//record the type
					else if(typeString.equals(XMLCSSPrimitiveValue.UNIT_KHZ_STRING))	//if this specifies picas
						primitiveType=XMLCSSPrimitiveValue.CSS_KHZ;	//record the type
					else if(typeString.equals(XMLCSSPrimitiveValue.UNIT_PERCENTAGE_STRING))	//if this specifies percentage
						primitiveType=XMLCSSPrimitiveValue.CSS_PERCENTAGE;	//record the type
					else	//if we don't recognize the unit
						primitiveType=XMLCSSPrimitiveValue.CSS_DIMENSION;	//record the type as a number with an unknown dimension
				}
				else	//if there are no non-digit characters in the string
					primitiveType=XMLCSSPrimitiveValue.CSS_NUMBER;	//the value is a simple number
			}
			else if(firstChar==SINGLE_QUOTE_CHAR || firstChar==DOUBLE_QUOTE_CHAR)	//if the first character is a quote
			{
				primitiveType=XMLCSSPrimitiveValue.CSS_STRING;	//the value is a string
				if(valueString.charAt(valueString.length()-1)==firstChar)	//if the last character is a quote, as we expect
					valueString=valueString.substring(1, valueString.length()-1);	//remove the quotes from the value
				else	//if there is no ending quote
					valueString=valueString.substring(1, valueString.length());	//TODO throw an exception here regarding the missing ending quote
			}
			else if(firstChar==RGB_NUMBER_CHAR)	//if the first character is a quote
			{
				primitiveType=XMLCSSPrimitiveValue.CSS_RGBCOLOR;  //show that the value is a color
				valueString=valueString.substring(1); //remove the beginning character for the value string
			}
			else	//if the first character isn't a digit or a quote
			{
				primitiveType=XMLCSSPrimitiveValue.CSS_IDENT;	//the value is an identifier
				//TODO actually, in a font-family list, this would still be a filename -- how do we rectify this?
					//TODO validate that this is a valid identifierfs
				//TODO fix for other types, if there are any
				//TODO fix for RGB()
				//TODO fix for rectangles
			}
			switch(primitiveType)	//see what type we determined it was TODO what if we didn't determine a type?
			{
				case XMLCSSPrimitiveValue.CSS_STRING:	//if this is one of the string types TODO why not use isStringType()?
				case XMLCSSPrimitiveValue.CSS_IDENT:	//if this is one of the string types TODO why not use isStringType()?
				//TODO add other string types here
					value.setStringValue(primitiveType, valueString);	//set the string value of this type
					break;
				case XMLCSSPrimitiveValue.CSS_RGBCOLOR: //if this is an RGB color
					final int valueStringLength=valueString.length(); //get the length of the value string
					if(valueStringLength!=3 && valueStringLength!=6)  //if the string is not in the form RGB or RRGBB
						return null;  //show that we could not parse the RGB value
					final int componentSubstringLength=valueStringLength/3; //find out how many characters each component takes up
						//get the strings representing the various components
				  final String redString=valueString.substring(0*componentSubstringLength, 0*componentSubstringLength+componentSubstringLength);
				  final String greenString=valueString.substring(1*componentSubstringLength, 1*componentSubstringLength+componentSubstringLength);
				  final String blueString=valueString.substring(2*componentSubstringLength, 2*componentSubstringLength+componentSubstringLength);
				  value.setPrimitiveType(primitiveType);  //update the primitive type's type record
						//create an RGB color value with the integer values of the three hex strings
					try
					{
					  value.rgbValue=new XMLCSSRGBColor(Integer.parseInt(redString, 16), Integer.parseInt(greenString, 16), Integer.parseInt(blueString, 16));
					}
					catch(NumberFormatException e)  //if we can't parse the components of the RGB color value
					{
						return null;  //show that there was a parsing error
					}
				  break;
				default:	//if this is one of the float types
					value.setFloatValue(primitiveType, Float.valueOf(valueString).floatValue());	//set the string value of this type
					break;
			}
			return value;	//return the value we constructed
		}
		return null;	//if the value string was invalid in some way, we'll wind up here; we'll report our problems by returning null
	}

}


