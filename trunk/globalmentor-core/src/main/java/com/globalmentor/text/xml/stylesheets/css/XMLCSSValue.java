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

import org.w3c.dom.DOMException;
import org.w3c.dom.css.*;

/**The class which represents a simple or complex CSS value
@author Garret Wilson
@version DOM Level 2
@since DOM Level 2
@see org.w3c.dom.css.CSSValue
@deprecated
*/
public abstract class XMLCSSValue implements org.w3c.dom.css.CSSValue//TODO fix, Cloneable
{
	/**Constructor which requires a value type.
	@param valueType The type of this CSS value.
	@see CSSValue
	*/
	public XMLCSSValue(final short valueType)
	{
		ValueType=valueType;	//set the value type of this CSS value
	}

	/**The type of CSS value this is.*/
	private short ValueType=CSS_CUSTOM;

	/**Returns a the type of value.
	@return A code representing the type of the CSS value.
	@see org.w3c.dom.css.CSSValue
	@version DOM Level 2
	@since DOM Level 2
	*/
	public short getCssValueType() {return ValueType;}

	/**Sets the node type of this object. This is not a DOM function, but a
	protected function that allows derived objects to set the node type.
	@param nodeType The new node type.
	TODO do we want to delete this, and make this have to be specified when the node is created?
	*/

	/**The string representation of the current value.*/
	private String CssText="";

	/**Returns the parsable textual representation of the current value.
	@return The parsable textual representation of the value.
	@version DOM Level 2
	@since DOM Level 2
	*/
//TODO fix in derived classes	public String getCssText() {return CssText;}

	/**@return A parsable string representation of the CSS value.
	@see CSSValue#getCssText
	*/
	public String toString()
	{
		return getCssText();	//return the text of this value
	}

	/**Sets the parsable textual representation of the value.
	@exception DOMException
	<ul>
		<li>SYNTAX_ERR: Raised if the specified CSS string value has a syntax error
			and is unparsable.</li>
		<li>NO_MODIFICATION_ALLOWED_ERR: Raised if the rule is readonly.</li>
	</ul>
	@version DOM Level 2
	@since DOM Level 2
	*/
	public void setCssText(String cssText) throws DOMException
	{
			//TODO we need to just parse the text here, probably

		//TODO check for syntax error
		//TODO check for read-only status
		CssText=cssText;	//set the text
	}

	/**The priority (e.g. "important") of this value.*/	//TODO it would probably be best to have a separate XMLProperty class name/value/priority similar to XMLAttribute.
	private String Priority="";

	/**@return The priority (e.g. "important") of the value.
	In this implementation this property can only has package access because it may be relocated.
	*/
	String getPriority() {return Priority;}


	/**Sets the priority (e.g. "important") of the value.
	In this implementation this property can only has package access because it may relocated.
	@param priority The new priority of the value.
	*/
	void setPriority(final String priority) {Priority=priority;}

}

