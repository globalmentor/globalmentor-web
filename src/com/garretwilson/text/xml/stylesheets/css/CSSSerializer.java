package com.garretwilson.text.xml.stylesheets.css;

import java.io.*;
import java.util.*;
//G***del if not needed import org.w3c.dom.*;
import org.w3c.dom.DOMException;
import org.w3c.dom.css.*;
import com.garretwilson.text.CharacterEncoding;
//G***del import com.garretwilson.util.StringUtilities;
import com.garretwilson.lang.CharacterUtilities;
import com.garretwilson.lang.IntegerUtilities;
import com.garretwilson.lang.StringBufferUtilities;
import com.garretwilson.util.Debug;
import com.garretwilson.util.PropertyUtilities;

//G***del all the XMLUndefinedEntityReferenceException throws when we don't need them anymore, in favor of XMLWellFormednessException

/**Class which serializes a CSS stylesheet to a byte-oriented output stream.
	Has the option of automatically formatting the output in a hierarchical
	structure with tabs or other strings.
@see XMLCSSProcessor
@author Garret Wilson
*/
public class CSSSerializer implements XMLCSSConstants
{

	/**Whether the output should be formatted.*/
//G***del	public final static String FORMAT_OUTPUT_OPTION="formatOutput"; //G***we need to get this from a single source

		/**Default to unformatted output.*/
//G***del		public final static boolean FORMAT_OUTPUT_OPTION_DEFAULT=true;

	/**Whether the output should be formatted.*/
//G***del	private boolean formatted=FORMAT_OUTPUT_OPTION_DEFAULT;

		/**@return Whether the output should be formatted.*/
//G***del		public boolean isFormatted() {return formatted;}

		/**Sets whether the output should be formatted.
		@param newFormatted <code>true</code> if the output should be formatted, else <code>false</code>.
		*/
//G***del		public void setFormatted(final boolean newFormatted) {formatted=newFormatted;}

	/**Sets the options based on the contents of the option properties.
	@param options The properties which contain the options.
	*/
	public void setOptions(final Properties options)
	{
//G***del Debug.notify("XMLCSSSerializer: "+options);  //G***del
//G***del		setFormatted(PropertyUtilities.getBooleanProperty(options, FORMAT_OUTPUT_OPTION, FORMAT_OUTPUT_OPTION_DEFAULT));
	}

	/**The string to use for horizontally aligning the elements if formatting is
		turned on. Defaults to a single tab character.
	*/
//G***del	private String horizontalAlignString="\t";

		/**@return The string to use for horizontally aligning the elements if formatting is turned on.
		@see #isFormatted
		*/
//G***del		public String getHorizontalAlignString() {return horizontalAlignString;}

		/**Sets the string to use for horizontally aligning the elements if formatting is turned on..
		@param newHorizontalAlignString The horizontal alignment string.
		@see #setFormatted
		*/
//G***del		public void setHorizontalAlignString(final String newHorizontalAlignString) {horizontalAlignString=newHorizontalAlignString;}

	/**Default constructor for formatted output.*/
	public CSSSerializer()
	{
//G***del		this(new Properties()); //do the default construction with default properties
//G***del	  this(false);  //default to unformatted output
	}

	/**Constructor that specifies serialize options.
	@param options The options to use for serialization.
	*/
	public CSSSerializer(final Properties options)
	{
		setOptions(options);  //set the options from the properties
	}

	/**Constructor for an optionally formatted serializer.
	@param Whether the serializer should be formatted.
	*/
/*G***del
	public CSSSerializer(final boolean formatted)
	{
		setFormatted(formatted);	//set whether the output should be formatted
	}
*/

	/**Serializes the specified stylesheet to the given output stream using the
		UTF-8 encoding.
	@param document The CSS stylesheet to serialize.
	@param outputStream The stream into which the document should be serialized.
	@exception IOException Thrown if an I/O error occurred.
	@exception UnsupportedEncodingException Thrown if the UTF-8 encoding not recognized.
	*/
	public void serialize(final CSSStyleSheet stylesheet, final OutputStream outputStream) throws UnsupportedEncodingException, IOException
	{
		serialize(stylesheet, outputStream, CharacterEncoding.UTF_8);	//serialize the document, defaulting to UTF-8
	}

	/**Serializes the specified stylesheet to the given output stream using the
		specified encoding.
	@param document The CSS stylesheet to serialize.
	@param outputStream The stream into which the document should be serialized.
	@param encoding The encoding format to use when serializing.
	@exception IOException Thrown if an I/O error occurred.
	@exception UnsupportedEncodingException Thrown if the specified encoding is not recognized.
	*/
	public void serialize(final CSSStyleSheet stylesheet, final OutputStream outputStream, final String encoding) throws IOException, UnsupportedEncodingException
	{
		final BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(outputStream, encoding));	//create a new writer based on our encoding
//G***fix		writeProlog(document, writer, encoding);	//write the prolog
		//G***return whatever header stuff is needed
		final CSSRuleList cssRuleList=stylesheet.getCssRules(); //get the list of CSS rules
		final int stylesheetRuleCount=cssRuleList.getLength(); //find out how many rules there are
Debug.trace("Number of stylesheet rules: ", stylesheetRuleCount); //G***del
		for(int ruleIndex=0; ruleIndex<stylesheetRuleCount; ++ruleIndex)	//look at each of the rules
		{
		  final CSSRule cssRule=cssRuleList.item(ruleIndex);  //get this CSS rule
Debug.trace("Writing cssRule: ", cssRule);
			write(cssRule, writer); //write the CSS rule
//G***del			if(isFormatted()) //if we're formatting the output
		  writer.newLine();	//add a newline in the default format
		}
//G***del		return stringBuffer.toString();	//return the string we constructed
		writer.newLine();	//add a newline in the default format
		writer.flush();	//flush any data we've buffered
	}

	/**Serializes the specified CSS rule to the given writer.
	@param cssRule The CSS rule to serialize.
	@param writer The writer into which the style rule should be written.
	@exception IOException Thrown if an I/O error occurred.
	*/
	protected void write(final CSSRule cssRule, final BufferedWriter writer) throws IOException
	{
Debug.trace("CSS rule: ", cssRule.getClass().getName()); //G***del
		if(cssRule instanceof CSSStyleRule) //G***fix
		{
Debug.trace("It's a CSS style rule"); //G***del
			final CSSStyleRule cssStyleRule=(CSSStyleRule)cssRule;  //G***fix
			writer.write(cssStyleRule.getSelectorText());  //write the selector text
			writer.newLine(); //write a newline
			writer.write(RULE_GROUP_START_CHAR);  //start the rule group
			writer.newLine(); //write a newline
		  write(cssStyleRule.getStyle(), writer);  //write the style declaration
			writer.write(RULE_GROUP_END_CHAR);  //end the rule group
			writer.newLine(); //write a newline
		}
	}

	/**Serializes the specified CSS style declaration to the given writer.
	@param cssStyleDeclaration The CSS style declaration to serialize.
	@param writer The writer into which the style declaration should be written.
	@exception IOException Thrown if an I/O error occurred.
	*/
	protected void write(final CSSStyleDeclaration cssStyleDeclaration, final BufferedWriter writer) throws IOException
	{
		final int propertyCount=cssStyleDeclaration.getLength();  //get the number of properties
		for(int i=0; i<propertyCount; ++i)	//look at each property
		{
			final String propertyName=cssStyleDeclaration.item(i);	//get the name of this property
			writer.write(TAB_CHAR); //write a tab
			writer.write(propertyName);  //write the property name
			writer.write(PROPERTY_DIVIDER_CHAR); //write the property divider character
			writer.write(SPACE_CHAR); //write a space
			writer.write(cssStyleDeclaration.getPropertyValue(propertyName)); //write the property value
			writer.write(DECLARATION_SEPARATOR_CHAR); //separate the declarations
			writer.newLine(); //write a newline
		}
	}
}
