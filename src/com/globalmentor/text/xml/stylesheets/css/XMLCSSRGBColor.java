package com.globalmentor.text.xml.stylesheets.css;

import java.awt.Color;

import com.globalmentor.util.Debug;

import org.w3c.dom.DOMException;
import org.w3c.dom.css.*;

/**Represents a color as an RGB value using multiple primitive values.
	This interface is used to represent any RGB color value. This interface
	reflects the values in the underlying style property. Hence, modifications
	made to the <code>CSSPrimitiveValue</code> objects modify the style property.
	<p>A specified RGB color is not clipped (even if the number is outside the
	range 0-255 or 0%-100%). A computed RGB color is clipped depending on the
	device.</p>
	<p>Even if a style sheet can only contain an integer for a color value,
	the internal storage of this integer is a float, and this can be used as
	a float in the specified or the computed style.</p>
	<p>A color percentage value can always be converted to a number and vice
	versa.</p>
	<p>See also the <a href='http://www.w3.org/TR/2000/REC-DOM-Level-2-Style-20001113'>Document Object Model (DOM) Level 2 Style Specification</a>.
@author Garret Wilson
@version DOM Level 2
@since DOM Level 2
@see org.w3c.dom.css.RGBColor
*/
public class XMLCSSRGBColor implements RGBColor
{

	/**The red color component as a primitive value.*/
	final protected XMLCSSPrimitiveValue redPrimitiveValue;

	/**The green color component as a primitive value.*/
	final protected XMLCSSPrimitiveValue greenPrimitiveValue;

	/**The blue color component as a primitive value.*/
	final protected XMLCSSPrimitiveValue bluePrimitiveValue;

	/**Default constructor which defaults to black.*/
	public XMLCSSRGBColor()
	{
		this(0, 0, 0);  //default to black
	}

	/**Creates an RGB color value based upon separate red, green, and blue
		values.
	@param red The red value.
	@param green The green value.
	@param blue The blue value.
	*/
	public XMLCSSRGBColor(final float red, final float green, final float blue)
	{
		try
		{
			redPrimitiveValue=new XMLCSSPrimitiveValue(XMLCSSPrimitiveValue.CSS_NUMBER, red);  //set the red value
			greenPrimitiveValue=new XMLCSSPrimitiveValue(XMLCSSPrimitiveValue.CSS_NUMBER, green); //set the green value
			bluePrimitiveValue=new XMLCSSPrimitiveValue(XMLCSSPrimitiveValue.CSS_NUMBER, blue); //set the blue value
		}
		catch(DOMException e) //there should never be a DOM exception here, because we know what values to set
		{
		  Debug.error(e); //this should never occur
			throw e;  //rethrow the exception
		}
	}

	/**Creates an RGB color value based upon a color object.
	@param color The color object which contains the color information.
	*/
	public XMLCSSRGBColor(final Color color)
	{
		this(color.getRed(), color.getGreen(), color.getBlue());
	}

	/**@return This red value of the RGB color as a primitive value.*/
	public CSSPrimitiveValue getRed() {return redPrimitiveValue;}

	/**@return The green value of the RGB color as a primitive value.*/
	public CSSPrimitiveValue getGreen() {return greenPrimitiveValue;}

	/**@return The blue value of the RGB color as a primitive value.*/
	public CSSPrimitiveValue getBlue() {return bluePrimitiveValue;}

}