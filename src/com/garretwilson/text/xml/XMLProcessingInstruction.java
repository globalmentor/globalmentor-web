package com.garretwilson.text.xml;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import org.w3c.dom.DOMException;

import static com.garretwilson.text.xml.XMLConstants.*;

/**A processing instruction in an XML document.
@see org.w3c.dom.ProcessingInstruction
*/
public class XMLProcessingInstruction extends XMLNode implements org.w3c.dom.ProcessingInstruction
{

	/**A map of pseudo attributes and values. These are "pseudo" in the sense that
		XML does not define attributes for processing instructions as it does for
		elements, but many processing instructions may have attribute-like parts.*/
	private Map pseudoAttributeMap=new HashMap();

		/**Returns the value of any pseudo attribute in the form name="value" or
			name='value' which may be part of the processing instruction data.
		@return The value of the given pseudo attribute, or <code>null</code> if that
			attribute isn't present in the data of the processing instruction.
		@see #getData
		@see #setData
		@see #getNodeValue
		@see #setNodeValue
		*/
		public String getPseudoAttributeValue(final String pseudoAttributeName)
		{
			return (String)pseudoAttributeMap.get(pseudoAttributeName);	//return the attribute value, if its name is present in the map
		}

	/*Constructor which requires an owner document to be specified.
	@param ownerDocument The document which owns this node.
	*/
	public XMLProcessingInstruction(final XMLDocument ownerDocument)
	{
		super(XMLNode.PROCESSING_INSTRUCTION_NODE, ownerDocument);	//construct the parent class
	}

	/**Constructor that specifies the target of the processing instruction.
	@param ownerDocument The document which owns this node.
	@param target The target for the processing instruction.
	*/
	public XMLProcessingInstruction(final XMLDocument ownerDocument, final String target)
	{
		this(ownerDocument);	//do the default constructing
		setNodeName(target);	//set the name, which holds the target
	}

	/**Constructor that specifies the content of the comment.
	@param ownerDocument The document which owns this node.
	@param target The target for the processing instruction.
	@param data The data for the processing instruction.
	*/
	public XMLProcessingInstruction(final XMLDocument ownerDocument, final String target, final String data)
	{
		this(ownerDocument, target);	//do the default constructing
		setNodeValue(data);	//set the data
	}

	/**Creates and returns a duplicate copy of this node. The clone has no parent.
	This function creates a "shallow" clone which does not contain clones of all
	child nodes. For the DOM version, see cloneNode().
	@return A duplicate copy of this node.
	@see XMLNode#cloneXMLNode
	@see XMLNode#cloneNode
	@see XMLNode#getParentXMLNode
	*/
	public Object clone()
	{
		return new XMLProcessingInstruction(getOwnerXMLDocument(), getTarget(), getData());	//create a new node with the same owner document, target, and value
	}

	/**Returns the target of this processing instruction. XML defines this as
		being the first token following the markup that begins the processing
		instruction.
	@return The target of this procesing instruction.
	@see XMLNode#getNodeName
	@version DOM Level 1
	*/
	public String getTarget() {return getNodeName();}

	/**The character data of this node.*/
	private String Value="";

		/**Returns the character data of this node.
		@return The character data of this node.
		@version DOM Level 1
		*/
		public String getNodeValue() throws DOMException {return Value;}

		/**Sets the character data of the attribute.
		@param nodeValue The new character data for the attribute.
		@version DOM Level 1
		*/
		public void setNodeValue(String nodeValue) throws DOMException  //G***change to use the new XMLUtilities.getProcessingInstructionPseudoAttributeValues()
		{
			Value=nodeValue;	//set the value
				//G***this stuff may be eventually placed back into XMLProcessor
			pseudoAttributeMap.clear();	//clear all of the keys and values from the map
			final StringTokenizer tokenizer=new StringTokenizer(nodeValue, WHITESPACE_CHARS);	//create a tokenizer that separates tokens based on XML whitespace
			while(tokenizer.hasMoreTokens())	//while there are more tokens
			{
				final String token=tokenizer.nextToken();	//get the next token
				final int equalsIndex=token.indexOf(EQUAL_CHAR);	//get the index of the equal sign, if there is one
				if(equalsIndex!=-1 && equalsIndex>0 && token.length()>=equalsIndex+2)	//if there is an equal sign, it's not the first character, and there's enough room for at least the quotes
				{
					final char quoteChar=token.charAt(equalsIndex+1);	//get the character after the equals sign
					//if the character after the equals sign is one of the quote characters, and it matches the last character in the token
					if((quoteChar==SINGLE_QUOTE_CHAR || quoteChar==DOUBLE_QUOTE_CHAR) && token.charAt(token.length()-1)==quoteChar)
					{
						final String pseudoAttributeName=token.substring(0, equalsIndex);	//get the name of the attribute
							//G***this does not correctly interpret embedded entities; does XML require it?
						final String pseudoAttributeValue=token.substring(equalsIndex+2, token.length()-1);	//get the value of the attribute
						pseudoAttributeMap.put(pseudoAttributeName, pseudoAttributeValue);	//put the name and value in the map
					}
				}
			}
		}

	/**Returns the content of this processing instruction. This is from the first
		non-whitespace character after the target to the character immediately
		preceding the <code>?&gt;</code>.
	@return The character data of this node.
	@see XMLNode#getNodeValue
	@version DOM Level 1
	*/
	public String getData() throws DOMException
	{
		return getNodeValue();	//return the character data
	}

	/**Sets the content of this processing instruction.
	@param data The new character data for this node.
	<ul>
		<li>NO_MODIFICATION_ALLOWED_ERR: Raised when the node is readonly.<li>
	</ul>
	@see XMLNode#setNodeValue
	@version DOM Level 1
	*/
	public void setData(String data) throws DOMException
	{
		//G***check about this readonly stuff
		setNodeValue(data);
	}

}
