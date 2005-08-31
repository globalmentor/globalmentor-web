package com.garretwilson.text.xml.xpath;

import java.util.*;

import com.garretwilson.lang.StringUtilities;

import static com.garretwilson.text.xml.xpath.XPathConstants.*;

/**A compiled form of an XPath path expression.
A special {@link AxisStep} with the axis of "/" as the first location step indicates an absolute path from the document root.
@author Garret Wilson
*/
public class PathExpression implements Iterable<Step>
{

	/**The list of steps in the path expression.*/
	private List<Step> stepList=new ArrayList<Step>();

	/**@return An iterator to contained steps.*/
	public Iterator<Step> iterator() {return stepList.iterator();}

	/**@return The number of steps in the path expression.*/
	public int getStepCount() {return stepList.size();}

	/**Returns the step at the given index in the path expression.
	@param stepIndex The index of the path to return.
	@return The step at the given index.
	*/
	public Step getStep(final int stepIndex) {return stepList.get(stepIndex);}

	/**Converts a string representing a path expression into a compiled path expression.
	@param pathExpression The string representation of a location path or, if step strings are given, the first step string.
	@param stepStrings If step strings are being provided, the step strings to be compiled into a path expression, starting with the second step string (the first is represented by the path expression). 
	*/
	public PathExpression(final String pathExpression, final String... stepStrings)
	{
		if(stepStrings.length>0)	//if step strings are being provided
		{
			stepList.add(getStep(pathExpression));	//the path expression is really the first step string
			for(final String stepString:stepStrings)	//for each other step string
			{
				stepList.add(getStep(stepString));	//add a step for this step string
			}
		}
		else	//if only a path expression is provided (a single step string is a path expresion as well)
		{
			final StringTokenizer locationPathStringTokenizer=new StringTokenizer(pathExpression, String.valueOf(LOCATION_STEP_SEPARATOR_CHAR), true);	//create a string tokenizer to parse the location path
			boolean expectingToken=true;	//show that we're expecting a token first of all
			while(locationPathStringTokenizer.hasMoreTokens())	//while there are more location steps on this line
			{
				String stepString=locationPathStringTokenizer.nextToken();	//get the next field value
				if(stepString.equals(String.valueOf(LOCATION_STEP_SEPARATOR_CHAR)))	//if this is a location step separator
				{
					if(expectingToken)	//if we were expecting a token
						stepString="";	//show that there was a blank token; since we just received a delimiter, we're still expecting another token
					else	//if we were expecting this delimiter (that is, we weren't expecting a token)
					{
						expectingToken=true;	//show that we're now expecting a token, since we just received a delimiter
						continue;	//don't process the delimiter that we expected
					}
				}
				else	//if this is a token
					expectingToken=false;	//show that we're no longer expecting a token, since we just received one
				final Step step;	//we'll determine a step for this step
				if(stepString.length()==0 && stepList.size()==0)	//if this is a blank location step and this is the first step, a blank location step means the root element
				{
					step=new AxisStep(ROOT, "");	//use a special type of location step representing the root TODO make this final static
				}
				else	//for all other steps
				{
					step=getStep(stepString);	//create a step from the step string
				}
				stepList.add(step);	//add this step to our list
			}
		}
	}

	/**Creates a step from a string version of a step
	@param stepString The provided literal step value.
	@return A step representing the given step string.
	*/
	protected static Step getStep(String stepString)
	{
		stepString=getCanonicalStepString(stepString);	//get the canonical form of the step string
		final int axisSeparatorIndex=stepString.indexOf(AXIS_SEPARATOR_STRING);	//get the index of "::"
		final String axis=stepString.substring(0, axisSeparatorIndex).trim();	//extract the axis from the string and trim it
		final String nodeTest=stepString.substring(axisSeparatorIndex+AXIS_SEPARATOR_STRING.length()).trim();	//extract the node test from the string and trim it
		return new AxisStep(axis, nodeTest);	//create a location step
	}

	/**Retrieves the canonical form of a step string.
	@param stepString The provided literal step value.
	@return The canonical form of the given step string
	*/
	protected static String getCanonicalStepString(String stepString)
	{
		if(stepString.length()==0)	//if this is a blank location step
		{
			stepString=DESCENDANT_OR_SELF+AXIS_SEPARATOR_STRING+NODE_NODE_TEST;	//if this isn't the first location step, blank location steps are abbreviated forms of "descendant-or-self::node()"
		}
		else if(stepString.equals(SELF_NODE_ABBREVIATION))	//if this is an abbreviation for self::node()
			stepString=SELF+AXIS_SEPARATOR_STRING+NODE_NODE_TEST;
		else if(stepString.equals(PARENT_NODE_ABBREVIATION))	//if this is an abbreviation for parent::node()
			stepString=PARENT+AXIS_SEPARATOR_STRING+NODE_NODE_TEST;
				//replace every occurrence of "@" with "attribute::" as per the XPath spec
		stepString=StringUtilities.replace(stepString, ATTRIBUTE_ABBREVIATION, ATTRIBUTE+AXIS_SEPARATOR_STRING);
		if(stepString.indexOf(AXIS_SEPARATOR_STRING)<0)	//get the index of "::"; if there is no axis separator
		{
			stepString=CHILD+AXIS_SEPARATOR_STRING+stepString;	//prepend "child::"
		}
		return stepString;	//return the canonical form of the step string
	}
}
