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

package com.globalmentor.xml.dom.impl.stylesheets.css;

import io.clogr.Clogged;

import org.w3c.dom.DOMException;
import org.w3c.dom.css.*;

/**
 * Represents a color as an RGB value using multiple primitive values. This interface is used to represent any RGB color value. This interface reflects the
 * values in the underlying style property. Hence, modifications made to the <code>CSSPrimitiveValue</code> objects modify the style property.
 * <p>
 * A specified RGB color is not clipped (even if the number is outside the range 0-255 or 0%-100%). A computed RGB color is clipped depending on the device.
 * </p>
 * <p>
 * Even if a style sheet can only contain an integer for a color value, the internal storage of this integer is a float, and this can be used as a float in the
 * specified or the computed style.
 * </p>
 * <p>
 * A color percentage value can always be converted to a number and vice versa.
 * </p>
 * <p>
 * See also the <a href='http://www.w3.org/TR/2000/REC-DOM-Level-2-Style-20001113'>Document Object Model (DOM) Level 2 Style Specification</a>.
 * @author Garret Wilson
 * @see org.w3c.dom.css.RGBColor
 * @deprecated
 */
@Deprecated
public class XMLCSSRGBColor implements RGBColor, Clogged {

	/** The red color component as a primitive value. */
	final protected XMLCSSPrimitiveValue redPrimitiveValue;

	/** The green color component as a primitive value. */
	final protected XMLCSSPrimitiveValue greenPrimitiveValue;

	/** The blue color component as a primitive value. */
	final protected XMLCSSPrimitiveValue bluePrimitiveValue;

	/** Default constructor which defaults to black. */
	public XMLCSSRGBColor() {
		this(0, 0, 0); //default to black
	}

	/**
	 * Creates an RGB color value based upon separate red, green, and blue values.
	 * @param red The red value.
	 * @param green The green value.
	 * @param blue The blue value.
	 */
	public XMLCSSRGBColor(final float red, final float green, final float blue) {
		try {
			redPrimitiveValue = new XMLCSSPrimitiveValue(XMLCSSPrimitiveValue.CSS_NUMBER, red); //set the red value
			greenPrimitiveValue = new XMLCSSPrimitiveValue(XMLCSSPrimitiveValue.CSS_NUMBER, green); //set the green value
			bluePrimitiveValue = new XMLCSSPrimitiveValue(XMLCSSPrimitiveValue.CSS_NUMBER, blue); //set the blue value
		} catch(DOMException e) { //there should never be a DOM exception here, because we know what values to set
			getLogger().error(e.getMessage(), e); //this should never occur
			throw e; //rethrow the exception
		}
	}

	/** @return This red value of the RGB color as a primitive value. */
	public CSSPrimitiveValue getRed() {
		return redPrimitiveValue;
	}

	/** @return The green value of the RGB color as a primitive value. */
	public CSSPrimitiveValue getGreen() {
		return greenPrimitiveValue;
	}

	/** @return The blue value of the RGB color as a primitive value. */
	public CSSPrimitiveValue getBlue() {
		return bluePrimitiveValue;
	}

}
