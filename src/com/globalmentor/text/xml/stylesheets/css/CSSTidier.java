package com.globalmentor.text.xml.stylesheets.css;

import java.io.*;
import java.util.*;

import static com.globalmentor.text.xml.stylesheets.css.XMLCSSConstants.*;

import com.globalmentor.java.Characters;
import com.globalmentor.java.Maths;
import com.globalmentor.java.StringBuffers;
import com.globalmentor.java.Strings;
import com.globalmentor.text.xml.oeb.css.OEBCSS;
import com.globalmentor.util.Debug;
import com.globalmentor.util.PropertiesUtilities;

import org.w3c.dom.css.*;

/**Contains several routines for tidying CSS stylesheets.
<p>Currently this class removes non-OEB1 CSS properties.</p>
<p>Options:</p>
<ul>
	<li><code>MAKE_FONT_SIZES_RELATIVE</code>: Change all font sizes to be
		relative to a base font size. Currently this searches for a
		<code>p</code> normal selector to use as the base font size. The current
		implementation assumes there is no hierarchical cascading.</li>
	<li><code>REMOVE_MS_OFFICE_PROPERTIES</code>: Removes all properties that
		begin with <code>mso-</code>.</li>
</ul>
@author Garret Wilson
*/
//G***add a feature to remove empty style declarations
public class CSSTidier
{

	/**Whether font sizes should be made relative to a base font size.*/
	public final static String MAKE_FONT_SIZES_RELATIVE_OPTION="makeFontSizesRelative";

		/**Default to not making font sizes relative.*/
		public final static boolean MAKE_FONT_SIZES_RELATIVE_OPTION_DEFAULT=false;

	/**Whether Microsoft Office-specific  properties should be removed.*/
	public final static String REMOVE_MS_OFFICE_PROPERTIES_OPTION="removeMSOfficeProperties";

		/**Default to removing MS Office properties.*/
		public final static boolean REMOVE_MS_OFFICE_PROPERTIES_OPTION_DEFAULT=true;

	/**Whether font sizes should be made relative to a base font size.*/
	private boolean makeFontSizesRelative=MAKE_FONT_SIZES_RELATIVE_OPTION_DEFAULT;

		/**@return Whether font sizes should be made relative to a base font size.*/
		public boolean isMakeFontSizesRelative() {return makeFontSizesRelative;}

		/**Sets whether whether font sizes should be made relative to a base font size.
		@param newMakeFontSizesRelative <code>true</code> if font sizes should be
			made relative to a base font size.
		*/
		public void setMakeFontSizesRelative(final boolean newMakeFontSizesRelative) {makeFontSizesRelative=newMakeFontSizesRelative;}

	/**Whether Microsoft Office-specific  properties should be removed.*/
	private boolean removeMSOfficeProperties=REMOVE_MS_OFFICE_PROPERTIES_OPTION_DEFAULT;

		/**@return Whether Microsoft Office-specific  properties should be removed.*/
		public boolean isRemoveMSOfficeProperties() {return removeMSOfficeProperties;}

		/**Sets whether Microsoft Office-specific  properties should be removed.
		@param newRemoveMSOfficeProperties <code>true</code> if Microsoft Office-
			specific propertiees should be removed.
		*/
		public void setRemoveMSOfficeProperties(final boolean newRemoveMSOfficeProperties) {removeMSOfficeProperties=newRemoveMSOfficeProperties;}

	/**Sets the options based on the contents of the option properties.
	@param options The properties which contain the options.
	*/
	public void setOptions(final Properties options)
	{
		setMakeFontSizesRelative(PropertiesUtilities.getBooleanProperty(options, MAKE_FONT_SIZES_RELATIVE_OPTION, MAKE_FONT_SIZES_RELATIVE_OPTION_DEFAULT));
		setRemoveMSOfficeProperties(PropertiesUtilities.getBooleanProperty(options, REMOVE_MS_OFFICE_PROPERTIES_OPTION, REMOVE_MS_OFFICE_PROPERTIES_OPTION_DEFAULT));
	}

	/**The base font size, if we've found one, in its original CSS value.*/
//G***fix	protected CSSPrimitiveValue baseFontSizeCSSValue=null;

	  //G***comment
	protected short baseFontSizeCssValueType;
	protected float baseFontSizeValue;



	/**Default constructor initialized with the default options.*/
  public CSSTidier()
  {
//G***del		this(new Properties()); //do the default construction with default properties
  }

	/**Constructs a CSS tidier with a list of options.
	@param options The properties which specify the tidy options.
	*/
	public CSSTidier(final Properties options)
	{
		setOptions(options);  //set the options from the properties
//G***del		options=tidyOptions;  //store the options
	}

	/**Tidies a CSS stylesheet.
	@param cssStyleSheet The stylesheet to tidy.
	*/
	public void tidy(final CSSStyleSheet cssStyleSheet)
	{
		final CSSRuleList cssRuleList=cssStyleSheet.getCssRules(); //get the list of CSS rules
		if(isMakeFontSizesRelative()) //if we should make font sizes relative
		{
			final CSSPrimitiveValue baseFontSizeCSSValue=getBaseFontSize(cssRuleList);  //search the stylesheet for a base font size
			if(baseFontSizeCSSValue!=null)  //if there is a base font size
			{
				baseFontSizeCssValueType=baseFontSizeCSSValue.getCssValueType();  //record the value type of the base font size
				baseFontSizeValue=baseFontSizeCSSValue.getFloatValue(baseFontSizeCssValueType); //get the base font size value
			}
			else  //if we couldn't find a base font size
				baseFontSizeValue=-1; //show that we couldn't find a base font size
		}
		final int stylesheetRuleCount=cssRuleList.getLength(); //find out how many rules there are
		for(int ruleIndex=0; ruleIndex<stylesheetRuleCount; ++ruleIndex)	//look at each of the rules
		{
		  final CSSRule cssRule=cssRuleList.item(ruleIndex);  //get this CSS rule
			tidy(cssRule); //tidy the CSS rule
		}
//G***del if not needed		baseFontSizeCSSValue=null;  //release our base font size CSS value, if we've referenced one
	}

	/**Tidies the specified CSS rule.
	@param cssRule The CSS rule to tidy.
	*/
	protected void tidy(final CSSRule cssRule)
	{
		if(cssRule instanceof CSSStyleRule) //G***fix
		{
			final CSSStyleRule cssStyleRule=(CSSStyleRule)cssRule;  //G***fix
		  tidy(cssStyleRule.getStyle());  //tidy the style declaration
		}
	}

	/**Tidies the specified CSS style declaration.
	@param cssStyleDeclaration The CSS style declaration to tidy.
	*/
	protected void tidy(final CSSStyleDeclaration cssStyleDeclaration)
	{
//G***del		final int propertyCount=cssStyleDeclaration.getLength();  //get the number of properties
		for(int propertyIndex=0; propertyIndex<cssStyleDeclaration.getLength();)	//look at each property, checking the number of properties each time because we might delete one, thereby decreasing the number of properties
		{
			final String propertyName=cssStyleDeclaration.item(propertyIndex);	//get the name of this property
Debug.trace("Checking to see if we should remove property: ", propertyName);  //G***del
		  if(shouldRemoveProperty(cssStyleDeclaration, propertyName))  //if we should remove this property
		  {
Debug.trace("we should remove property");
/*G***del
				//if we should remove MS Office properties, and this is an MS Office property
		  if(isRemoveMSOfficeProperties() && propertyName.startsWith("mso-")) //G***use a constant here
		  {
*/
			  cssStyleDeclaration.removeProperty(propertyName); //remove this property
				continue; //continue without increasing the index, since the next property is now at this index
		  }
				//if we should make font sizes relative, and this is a font size
			else if(isMakeFontSizesRelative() && CSS_PROP_FONT_SIZE.equals(propertyName))
			{
//G***del Debug.trace("Found a font size"); //G***del

//G***del if not needed				if(baseFontSizeCSSValue!=null)  //if we know the base font size
				if(baseFontSizeValue!=-1)  //if we know the base font size G***use a constant or something more robust, maybe
				{
//G***del Debug.trace("we already know a base size"); //G***del
					final CSSPrimitiveValue propertyCSSValue=(CSSPrimitiveValue)cssStyleDeclaration.getPropertyCSSValue(propertyName);  //get the property with this name G***we shouldn't really assume this is a primitive value
//G***del Debug.trace("got property value: ", propertyCSSValue); //G***del
///G***del when works				  if(baseFontSizeCSSValue.getCssValueType()==propertyCSSValue.getCssValueType())  //if both value types are the same
				  if(baseFontSizeCssValueType==propertyCSSValue.getCssValueType())  //if both value types are the same
				  {
//G***del Debug.trace("both sizes have the same value type"); //G***del
//G***del when works						final float baseFontSize=baseFontSizeCSSValue.getFloatValue(baseFontSizeCSSValue.getCssValueType());  //get the base font size
						final float fontSizeValue=propertyCSSValue.getFloatValue(propertyCSSValue.getCssValueType());  //get the current font size
//G***del Debug.trace("font size: "+fontSizeValue);  //G***del
//G***del Debug.trace("base font size: "+baseFontSizeValue);  //G***del
//G***del						final float relativeSize=MathUtilities.round(fontSizeValue/baseFontSizeValue, -2)*100;  //get the relative size rounded to the second decimal position, and convert it to a percentage by multiplying by 100 G***use a constant here
						final float relativeSize=Math.round(fontSizeValue/baseFontSizeValue*100);  //get the relative size convert it to a percentage by multiplying by 100, rounding the value G***use a constant here
						propertyCSSValue.setFloatValue(propertyCSSValue.CSS_PERCENTAGE, relativeSize);  //change the value to a relative size
				  }
				}
			}
//G***del			else  //if we didn't remove anything
			{
				++propertyIndex;  //look at the next property
			}
		}
	}

	/**Searches the stylesheet for a base font size.
	@param cssRuleList The stylesheet's rule list.
	@return The CSS value representing the base font size used in the document, or
		<code>null</code> if a base font size can't be found.
	*/
	protected CSSPrimitiveValue getBaseFontSize(final CSSRuleList cssRuleList)
	{
		final int ruleCount=cssRuleList.getLength(); //find out how many rules there are
		for(int ruleIndex=0; ruleIndex<ruleCount; ++ruleIndex)	//look at each of the rules
		{
		  final CSSRule cssRule=cssRuleList.item(ruleIndex);  //get this CSS rule
			if(cssRule instanceof CSSStyleRule) //G***fix
			{
				final CSSStyleRule cssStyleRule=(CSSStyleRule)cssRule;  //G***fix
				final String selectorText=cssStyleRule.getSelectorText(); //get the selector text
					//if this selects a normal paragraph G***fix this; this is highly dependent on HTML and MSWord
				if(selectorText.indexOf("p.")>=0 && Strings.indexOfIgnoreCase(selectorText, "normal")>=0)
				{
					final CSSStyleDeclaration cssStyleDeclaration=cssStyleRule.getStyle();  //get the style declaration
					final int propertyCount=cssStyleDeclaration.getLength();  //get the number of properties
					for(int propertyIndex=0; propertyIndex<propertyCount; ++propertyIndex)	//look at each property
					{
						final String propertyName=cssStyleDeclaration.item(propertyIndex);	//get the name of this property
						if(CSS_PROP_FONT_SIZE.equals(propertyName)) //if this is the font-size property
						{
							final CSSValue propertyCSSValue=cssStyleDeclaration.getPropertyCSSValue(propertyName);  //get the property with this name
							if(propertyCSSValue.getCssValueType()==propertyCSSValue.CSS_PRIMITIVE_VALUE)  //if this is a primitive value
							{
//G***del Debug.trace("Found base font size CSS value: ", propertyCSSValue);  //G***del
								return (CSSPrimitiveValue)propertyCSSValue;  //return this primitive value property as the base font size
							}
						}
					}
				}
			}
		}
		return null;  //show that we couldn't find a base font size
	}

	/**Determines whether a particular CSS property should be removed.
	@param cssStyleDeclaration The CSS style declaration containing the property
	@param propertyName The name of the property being examined.
	*/
	protected boolean shouldRemoveProperty(final CSSStyleDeclaration cssStyleDeclaration, final String propertyName)
	{
				//if we should remove MS Office properties, and this is an MS Office property
		if(isRemoveMSOfficeProperties() && propertyName.startsWith("mso-")) //G***use a constant here
			return true;  //show that we should remove this property
		if(!OEBCSS.isOEB1CSSProperty(propertyName))  //if this is not an OEB 1.0 CSS property G***move this to an OEBCSSTidier
			return true;  //remove non-OEB CSS properties
		return false; //since we didn't find a reason to, don't remove this property
	}

}