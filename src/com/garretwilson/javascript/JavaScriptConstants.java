package com.garretwilson.javascript;

import javax.mail.internet.ContentType;
import static com.garretwilson.io.ContentTypeConstants.*;

/**Constants for working with JavaScript.
@author Garret Wilson
*/
public class JavaScriptConstants
{

	/**The content type for JavaScript: <code>text/javascript</code>.*/ 
	public static final ContentType JAVASCRIPT_CONTENT_TYPE=new ContentType(TEXT, JAVASCRIPT_SUBTYPE, null);

	/**The delimiter '.' for object properties.*/
	public final static char PROPERTY_DELIMITER='.';

	/**The character '[' for starting an array index.*/
	public final static char ARRAY_INDEX_BEGIN_CHAR='[';

	/**The character ']' for ending an array index.*/
	public final static char ARRAY_INDEX_END_CHAR=']';

	/**The character '{' for starting a block.*/
	public final static char BLOCK_BEGIN_CHAR='{';

	/**The character '}' for ending a block.*/
	public final static char BLOCK_END_CHAR='}';

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
}
