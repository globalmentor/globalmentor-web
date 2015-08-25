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

package com.globalmentor.css;

import org.w3c.dom.*;
import org.w3c.dom.css.*;

/**
 * An object that can associate CSS styles with XML elements.
 * @author Garret Wilson
 */
public interface CSSStyleManager {

	/**
	 * Retrieves the style of the given element.
	 * @param element The element for which a style should be returned.
	 * @return A style for the given element, or <code>null</code> if the element has no style assigned to it.
	 */
	public CSSStyleDeclaration getStyle(final Element element);

	/**
	 * Sets the style of an element, replacing any existing style stored for the element.
	 * @param element The element for which a style is being specified.
	 * @param style The element style.
	 */
	public void setStyle(final Element element, final CSSStyleDeclaration style);

	/**
	 * Gets the CSS value object of a particular CSS property.
	 * @param styleManager The object that allows style lookups for elements.
	 * @param element The element with which style is associated.
	 * @param propertyName The name of the CSS property to search for.
	 * @param resolve Whether the element's parent hierarchy should be searched to find this CSS value if not found associated with this element.
	 * @return The CSS value object for the given property, or <code>null</code> if that property cannot be found.
	 */
//TODO convert to Java 8 default method
//	public static CSSValue getCSSPropertyCSSValue(final CSSStyleManager styleManager, final Element element, final String propertyName, final boolean resolve) {
//		final CSSStyleDeclaration cssStyle = styleManager.getStyle(element); //get the CSS style associated with the element
//		if(cssStyle != null) { //if there is a CSS style declaration object associated with the element
//			final CSSValue cssValue = cssStyle.getPropertyCSSValue(propertyName); //get the property as a CSSValue object
//			if(cssValue != null) { //if the style contains the given CSS property
//				return cssValue; //return the value
//			}
//		}
//		if(resolve) { //since the property isn't associated with this element, see if we should resolve up the chain; if so
//			final Node parentNode = element.getParentNode(); //get the parent to use to resolve
//			if(parentNode != null && parentNode.getNodeType() == Node.ELEMENT_NODE) //if we have a resolve parent element
//				return getCSSPropertyCSSValue(styleManager, (Element)parentNode, propertyName, resolve); //try to get the value from the resolving parent
//		}
//		return null; //show that we couldn't find the CSS property value anywhere
//	}

	/**
	 * Gets a particular value from the style that should be a length, returning the length in pixels. TODO fix; right now it returns points
	 * @param styleManager The object that allows style lookups for elements.
	 * @param element The element with which style is associated.
	 * @param cssProperty The name of the CSS property for which a color value should be retrieved.
	 * @param resolve Whether the attribute set's parent hierarchy should be searched to find this CSS value if not found in this attribute set.
	 * @param defaultValue The default value to use if the property does not exist.
	 * @param font The font to be used when calculating relative lengths, such as <code>ems</code>.
	 * @return The length in pixels. //TODO what happens if the value is not a length? probably just return the default
	 */
//TODO convert to Java 8 default method
//	protected static float getPixelLength(final CSSStyleManager styleManager, final Element element, final String cssProperty, final boolean resolve,
//			final float defaultValue/*TODO fix, final Font font*/) {
//		final CSSPrimitiveValue primitiveValue = (CSSPrimitiveValue)getCSSPropertyCSSValue(styleManager, element, cssProperty, resolve); //get CSS value for this property, resolving up the hierarchy if we should
//		if(primitiveValue != null) { //if we have a value
//			switch(primitiveValue.getPrimitiveType()) { //see which type of primitive type we have
//				case CSSPrimitiveValue.CSS_EMS: //if this is the ems property
//					return primitiveValue.getFloatValue(CSSPrimitiveValue.CSS_EMS); //TODO fix; hack for just returning any value
//					/*TODO fix
//										if(font!=null)  //if we have a font
//											return font.getSize()*primitiveValue.getFloatValue(XMLCSSPrimitiveValue.CSS_EMS);  //TODO fix; this probably isn't the same as the defined font size, which is what CSS calls for for EMS
//										break;
//					*/
//				case CSSPrimitiveValue.CSS_PT: //if they want the size in points
//					return primitiveValue.getFloatValue(CSSPrimitiveValue.CSS_PT); //get the value in pixels and round to the nearest integer pixel length TODO fix to use pixels instead of points, as it does now
//			}
//		}
//		return defaultValue; //if we couldn't determine the value, return the default value
//	}

	/**
	 * Gets the CSS <code>margin-left</code> setting from the style in pixels. TODO actually, right now it returns the value in points; fix this
	 * @param styleManager The object that allows style lookups for elements.
	 * @param element The element with which style is associated.
	 * @param font The font to be used when calculating relative lengths, such as <code>ems</code>.
	 * @return The left margin size in pixels or, if the property is not specified, the default amount of 0.
	 */
//TODO convert to Java 8 default method
//	public static float getMarginLeft(final CSSStyleManager styleManager, final Element element/*TODO fix , final Font font*/) {
//		return getPixelLength(styleManager, element, CSS_PROP_MARGIN_LEFT, false, 0/*G**fix, font*/); //return the length in pixels without resolving
//	}

	/** The default text indent. */
//TODO fix	private static final int DEFAULT_TEXT_INDENT = 0;	//TODO should this go elsewhere?
	
	/**
	 * Gets the CSS <code>text-indent</code> setting from the style in pixels. TODO actually, right now it returns the value in points; fix this
	 * @param styleManager The object that allows style lookups for elements.
	 * @param element The element with which style is associated.
	 * @return The text indent amount in pixels or, if text indent is not specified, the default amount, which is 0. //TODO testing font; comment
	 */
//TODO convert to Java 8 default method
//	public static float getTextIndent(final CSSStyleManager styleManager, final Element element/*TODO fix, final Font font*/) //TODO we'll probably need to pass a length here or something
//	{
//		final CSSPrimitiveValue textIndentValue = (CSSPrimitiveValue)getCSSPropertyCSSValue(styleManager, element, CSS_PROP_TEXT_INDENT, true); //get CSS value for this property, resolving up the hierarchy if necessary
//		if(textIndentValue != null) { //if we have a value
//			//TODO del if not needed			if(textIndentValue.isAbsoluteLength())	//if this is an absolute length
//			{
//				switch(textIndentValue.getPrimitiveType()) { //see which type of primitive type we have
//				/*TODO fix
//								  case CSSPrimitiveValue.CSS_EMS: //TODO testing
//										return font.getSize()*textIndentValue.getFloatValue(XMLCSSPrimitiveValue.CSS_EMS);  //TODO fix; this probably isn't the same as the defined font size, which is what CSS calls for for EMS
//				*/
//				/*TODO fix
//										{
//					Log.trace("They want ems.");
//													// As a practical matter, this FRC will almost always
//													// be the right one.
//													AffineTransform xf
//															= GraphicsEnvironment.getLocalGraphicsEnvironment()
//															.getDefaultScreenDevice().getDefaultConfiguration()
//															.getDefaultTransform(); //TODO testing
//											final FontRenderContext fontRenderContext=new FontRenderContext(xf, false, false);  //TODO we should really get the font render context from somewhere else; for now, this should get close
//											final float emSize=(float)font.getStringBounds("m", fontRenderContext).getWidth(); //get the size of an em
//						Log.trace("each em is: ", new Float(emSize)); //TODO del
//											return emSize*textIndentValue.getFloatValue(XMLCSSPrimitiveValue.CSS_EMS);  //TODO testing
//										}
//				*/
//					case CSSPrimitiveValue.CSS_PT: //TODO testing
//						return textIndentValue.getFloatValue(CSSPrimitiveValue.CSS_PT); //get the value in pixels and round to the nearest integer pixel length TODO fix to use pixels instead of points, as it does now
//				}
//			}
//			//TODO del if not needed			else	//if this is not an absolute length, it could be a relative length or a percentage
//			{
//				/*TODO fix
//									float percentageValue=1;	//if we find a percentage value, we'll store it here (we won't really store a percentage, but the actual float number; that is, percentage/100) (we're defaulting to 1 because the compiler will complain without some value, even though this always gets changed by the logic of the code)
//									boolean isPercentage=;	//see if this is a percentage value
//									if(isPercentage)	//if this is an explicit percentage value
//										percentageValue=fontSizeValue.getFloatValue(XMLCSSPrimitiveValue.CSS_PERCENTAGE)/100;	//store the percentage value as a scaling value
//										//if this isn't an explicit percentage value, it might be a relative value, an absolute string value, or a relative string value that is the same as a percentage
//									if(textIndentValue.getPrimitiveType()==XMLCSSPrimitiveValue.CSS_PERCENTAGE) {	//if this is a percentage value
//										final AttributeSet resolveParent=attributeSet.getResolveParent();	//get the parent to use to resolve this attribute
//										if(resolveParent!=null)	//if we have a resolve parent
//											return Math.round(percentageValue*getFontSize(resolveParent));	//multiply the percentage with the font size from the parent
//										else	//if we don't have a resolve parent
//											return Math.round(percentageValue*DEFAULT_FONT_SIZE);	//multiply the percentage with the default font size
//									}
//									//TODO add support for relative sizes (ems, exs, etc.) here: else
//				*/
//			}
//		}
//		return DEFAULT_TEXT_INDENT; //return the default value, since we couldn't find an alternative
//	}

	
}