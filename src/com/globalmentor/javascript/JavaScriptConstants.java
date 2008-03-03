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

package com.globalmentor.javascript;

import java.net.URI;

import javax.mail.internet.ContentType;

import static com.globalmentor.io.ContentTypes.*;
import static com.globalmentor.java.StringBuilders.*;

/**Constants for working with JavaScript.
@author Garret Wilson
@see <a href="http://www.ietf.org/rfc/rfc4329.txt">RFC 4329: Scripting Media Types</a>
*/
public class JavaScriptConstants
{

	/**The JavaScript MIME subtype.*/
	public final static String JAVASCRIPT_SUBTYPE="javascript";

	/**The content type for JavaScript: <code>application/javascript</code>.*/ 
	public static final ContentType JAVASCRIPT_CONTENT_TYPE=new ContentType(APPLICATION_PRIMARY_TYPE, JAVASCRIPT_SUBTYPE, null);

	/**The obsolete content type for JavaScript: <code>text/javascript</code>.*/ 
	public static final ContentType JAVASCRIPT_OBSOLETE_CONTENT_TYPE=new ContentType(TEXT_PRIMARY_TYPE, JAVASCRIPT_SUBTYPE, null);

	/**The delimiter '.' for object properties.*/
	public final static char PROPERTY_DELIMITER='.';

	/**The character '[' for starting an array index.*/
	public final static char ARRAY_INDEX_BEGIN_CHAR='[';

	/**The character ']' for ending an array index.*/
	public final static char ARRAY_INDEX_END_CHAR=']';

	/**The character '[' for starting an array.*/
	public final static char ARRAY_BEGIN_CHAR='[';

	/**The character ']' for ending an array.*/
	public final static char ARRAY_END_CHAR=']';

	/**The character ',' for separating array elements.*/
	public final static char ARRAY_DELIMITER=',';

	/**The character '{' for starting an associative array.*/
	public final static char ASSOCIATIVE_ARRAY_BEGIN_CHAR='{';

	/**The character '}' for ending an associative array.*/
	public final static char ASSOCIATIVE_ARRAY_END_CHAR='}';

	/**The character ',' for separating associative array elements.*/
	public final static char ASSOCIATIVE_ARRAY_DELIMITER=',';

	/**The character ';' for separating associative array key/value pairs.*/
	public final static char ASSOCIATIVE_ARRAY_KEY_VALUE_DELIMITER=':';

	/**The character '{' for starting a block.*/
	public final static char BLOCK_BEGIN_CHAR='{';

	/**The character '}' for ending a block.*/
	public final static char BLOCK_END_CHAR='}';

	/**The character used to escape characters in an encoded string.*/
	public final static char ESCAPE_CHAR='\\';

	/**The character '(' for starting a parameter list.*/
	public final static char PARAMETER_BEGIN_CHAR='(';

	/**The character ')' for ending a parameter list.*/
	public final static char PARAMETER_END_CHAR=')';

	/**The delimiter ',' for method parameter separation.*/
	public final static char PARAMETER_DELIMITER=',';

	/**The character '=' for variable assignment.*/
	public final static char ASSIGNMENT_CHAR='=';

	/**The character ';' indicating the ending of a statement.*/
	public final static char STATEMENT_END_CHAR=';';

	/**The characters that must be encoded in a string value.*/
	public final static char[] STRING_ENCODE_CHARS=new char[]{'"', '\\', '/', '\b', '\f', '\n', '\r', '\t'};
	/**The strings with which encoded characters will be replaced in a string value.*/
	public final static String[] STRING_ENCODE_REPLACEMENT_STRINGS=new String[]{"\\\"", "\\\\", "\\/", "\\b", "\\f", "\\n", "\\r", "\\t"};
	
	/**The if keyword.*/
	public final static String IF_KEYWORD="if";
	/**The else keyword.*/
	public final static String ELSE_KEYWORD="else";
	/**The return keyword.*/
	public final static String RETURN_KEYWORD="return";

	/**The negation operator character.*/
	public final static char NOT_OPERATOR='!';

	/**The document identifier.*/
	public final static String DOCUMENT="document";

	/**The forms identifier.*/
	public final static String FORMS="forms";

	/**The alert method.*/
	public final static String ALERT_METHOD="alert";
	/**The confirm method.*/
	public final static String CONFIRM_METHOD="confirm";
	/**The form submission method.*/
	public final static String SUBMIT_METHOD="submit";

	/**The null identifier.*/
	public final static String NULL="null";
	/**The boolean true identifier.*/
	public final static String TRUE="true";
	/**The boolean false identifier.*/
	public final static String FALSE="false";

	/**Escapes a string for JavaScript.
	<ul>
		<li>'\\' is changed to "\\\\"</li>
		<li>'\'' is changed to "\\\'"</li>
		<li>'"' is changed to "\\\""</li>
		<li>'\n' is changed to "\\n"</li>
		<li>'\t' is changed to "\\t"</li>
	</ul>
	@param string The string to escape.
	@return An JavasScript-safe string.
	*/
	public static String escape(final String string)
	{
		final StringBuilder stringBuilder=new StringBuilder(string);	//create a new string builder based upon the string
		replace(stringBuilder, '\\', "\\\\");	//TODO use constants
		replace(stringBuilder, '\'', "\\\'");	//TODO use constants
		replace(stringBuilder, '"', "\\\"");	//TODO use constants
		replace(stringBuilder, '\n', "\\n");	//TODO use constants
		replace(stringBuilder, '\t', "\\t");	//TODO use constants
		return stringBuilder.toString();	//return the escaped string
	}

	/**Returns the name of a method:
	<code><var>method</var>()</code>
	@param method The name of the method.
	@param parameters The method parameters.
	*/
	public static String getMethod(final String method, final String... parameters)
	{
		final StringBuilder stringBuilder=new StringBuilder();
		stringBuilder.append(method);	//method
		stringBuilder.append(PARAMETER_BEGIN_CHAR);	//(
		for(final String parameter:parameters)	//look at each parameter
		{
			stringBuilder.append(parameter).append(PARAMETER_DELIMITER);	//parameter,
		}
		if(parameters.length>0)	//if there was at least one parameter
		{
			stringBuilder.deleteCharAt(stringBuilder.length()-1);	//remove the last parameter delimiter
		}
		stringBuilder.append(PARAMETER_END_CHAR);	//)
		return stringBuilder.toString();	//return the property method we constructed
	}

	/**Returns the name of an object property:
	<code><var>object</var>.<var>property</var></code>
	@param object The name of the object.
	@param property The name of the property.
	*/
	public static String getObjectProperty(final String object, final String property)
	{
		return object+PROPERTY_DELIMITER+property;
	}

	/**Returns the name of an object method:
	<code><var>object</var>.<var>method</var>()</code>
	@param object The name of the object.
	@param method The name of the method.
	@param parameters The method parameters.
	*/
	public static String getObjectMethod(final String object, final String method, final String... parameters)
	{
		final StringBuilder stringBuilder=new StringBuilder();
		stringBuilder.append(object).append(PROPERTY_DELIMITER);	//object.
		stringBuilder.append(getMethod(method, parameters));	//method(parameters)
		return stringBuilder.toString();	//return the object method we constructed
	}

	/**Returns the name of an object indexed property:
	<code><var>object</var>['<var>property</var>']</code>
	@param object The name of the object.
	@param property The name of the property.
	*/
	public static String getObjectIndexedProperty(final String object, final String property)
	{
		return object+ARRAY_INDEX_BEGIN_CHAR+'\''+property+'\''+ARRAY_INDEX_END_CHAR;
	}

	/**Returns the name of the form variable with the given ID:
	<code>document.forms[<var>formID</var>]</code>
	@param formID The ID of the form to return.
	*/
	public static String getFormVariable(final String formID)
	{
		return getObjectIndexedProperty(getObjectProperty(DOCUMENT, FORMS), formID);
	}

	/**Returns the name of a form component variable:
	<code>document.forms[<var>formID</var>][<var>componentID</var>]</code>
	@param formID The ID of the form containing the component.
	@param componentID The ID of the component variable to return.
	*/
	public static String getFormComponentVariable(final String formID, final String componentID)
	{
		return getObjectIndexedProperty(getFormVariable(formID), componentID);
	}

	/**Returns the name of a variable assignment:
	<code><var>variable</var>=<var>value</var></code>
	@param variable The variable to which the value should be assigned.
	@param value The value to assign to the variable, or <code>null</code>.
	*/
	public static String setValue(final String variable, final String value)
	{
		return variable+ASSIGNMENT_CHAR+(value!=null ? value : NULL);
	}

	/**Returns the name of a variable assignment with a literal variable:
	<code><var>variable</var>='<var>value</var>'</code>
	The value is escaped.
	@param variable The variable to which the value should be assigned.
	@param value The literal value to assign to the variable, or <code>null</code>.
	*/
	public static String setLiteralValue(final String variable, final String value)
	{
		return setValue(variable, value!=null ? '\''+escape(value)+'\'' : null);	//TODO use a constant
	}

	/**Returns a string representing a form component value assignment:
	<code>document.forms[<var>formID</var>][<var>componentID</var>].<var>property</var>=<var>value</var></code>
	@param formID The ID of the form containing the component.
	@param componentID The ID of the component variable.
	@param property The name of the component property.
	@param value The value to assign to the form component property.
	*/
	public static String setFormComponentPropertyValue(final String formID, final String componentID, final String property, final String value)
	{
		return setValue(getObjectProperty(getFormComponentVariable(formID, componentID), property), value);	//set the form component property value
	}

	/**Returns a string representing a form component literal value assignment:
	<code>document.forms[<var>formID</var>][<var>componentID</var>].<var>property</var>='<var>value</var>'</code>
	@param formID The ID of the form containing the component.
	@param componentID The ID of the component variable.
	@param property The name of the component property.
	@param value The literal value to assign to the form component property.
	*/
	public static String setFormComponentPropertyLiteralValue(final String formID, final String componentID, final String property, final String value)
	{
		return setLiteralValue(getObjectProperty(getFormComponentVariable(formID, componentID), property), value);	//set the form component property value
	}

	/**Returns a string representing an alert:
	<code>alert(<var>text</var>)</code>
	@param text The text to present to the user.
	*/
	public static String alert(final String text)
	{
		return getMethod(ALERT_METHOD, text);	//get the method for alerting
	}

	/**Returns a string representing an alert of literal text:
	<code>alert('<var>textID</var>')</code>
	The text is correctly escaped.
	@param text The text to present to the user.
	*/
	public static String alertLiteral(final String text)
	{
		return alert('\''+escape(text)+'\'');	//place the text in quotes
	}

	/**Returns a string representing a confirmation:
	<code>confirm(<var>text/var>)</code>
	@param text The text to present to the user.
	*/
	public static String confirm(final String text)
	{
		return getMethod(CONFIRM_METHOD, text);	//get the method for confirming by presenting a message
	}

	/**Returns a string representing a confirmation of literal text:
	<code>confirm('<var>text</var>')</code>
	The text is correctly escaped.
	@param text The text to present to the user.
	*/
	public static String confirmLiteral(final String text)
	{
		return confirm('\''+escape(text)+'\'');	//place the text in quotes
	}

	/**Returns a string representing a form submission:
	<code>document.forms[<var>formID</var>].submit()</code>
	@param formID The ID of the form containing the component.
	*/
	public static String submitForm(final String formID)
	{
		return getObjectMethod(getFormVariable(formID), SUBMIT_METHOD);	//get the method for submitting a form
	}

	/**Creates a statement:
	<code><var>statement</var>;
	@param statement The statement to create.
	*/
	public static String createStatement(final String statement)
	{
		return statement+STATEMENT_END_CHAR;
	}

	/**Returns a negation:
	<code>!<var>expression</var></code>
	@param expression The expression to negate.
	@return A string representing the negation of the expression.
	*/
	public static String createNot(final String expression)
	{
		return new StringBuilder().append(NOT_OPERATOR).append(expression).toString();
	}

	/**Returns a string representing an <code>if()</code> block:
	<code>if(<var>testExpression</var>){<var>thenBlock</var>}</code>
	@param testExpression The expression to test.
	@param thenBlock The block to execute if the test evaluates to <code>true</code>.
	@return A string representing an if statement.
	*/
	public static String createIf(final String testExpression, final String thenBlock)
	{
		return createIf(testExpression, thenBlock, null);	//create an if with no else block
	}

	/**Returns a string representing an <code>if()</code> block:
	<code>if(<var>testExpression</var>){<var>thenBlock</var>}else{<var>elseBlock</var>}</code>
	@param testExpression The expression to test.
	@param thenBlock The block to execute if the test evaluates to <code>true</code>.
	@param elseBlock The block to execute if the test evaluates to <code>false</code>,
		or <code>null</code> if there should be no else block.
	@return A string representing an if statement.
	*/
	public static String createIf(final String testExpression, final String thenBlock, final String elseBlock)
	{
		final StringBuilder stringBuilder=new StringBuilder(IF_KEYWORD);
		stringBuilder.append(PARAMETER_BEGIN_CHAR);	//(
		stringBuilder.append(testExpression);	//testExpression
		stringBuilder.append(PARAMETER_END_CHAR);	//)
		stringBuilder.append(BLOCK_BEGIN_CHAR);	//{
		stringBuilder.append(thenBlock);	//thenBlock		
		stringBuilder.append(BLOCK_END_CHAR);	//}
		if(elseBlock!=null)	//if we have an else block
		{
			stringBuilder.append(ELSE_KEYWORD);	//else
			stringBuilder.append(BLOCK_BEGIN_CHAR);	//{
			stringBuilder.append(elseBlock);	//thenBlock		
			stringBuilder.append(BLOCK_END_CHAR);	//}
		}
		return stringBuilder.toString();	//return the string we created
	}

	/**Creates JavaScript code for popping up a window.
	@param uri The URI to show in the new window.
	@param name The name of the window; must not have spaces to work on IE.
	*/
	public static String popupWindow(final URI uri, final String name)
	{
		return	//TODO testing
			"var win=window.open('"+uri+"','"+name+"','width=640,height=480,scrollbars=yes');"+
			"if(window.focus){win.focus()};";
	}

	/**Creates a return statement:
	<code>return <var>value</var>;</code>
	@param value The value to return.
	*/
	public static String returnValue(final Object value)
	{
		return createStatement(RETURN_KEYWORD+' '+value);
	}

}
