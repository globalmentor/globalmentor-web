package com.garretwilson.text.xml.xpath;

import static com.garretwilson.text.xml.xpath.XPathConstants.*;

/**Represents an axis step in an XPath path expression.
@author Garret Wilson
*/
public class AxisStep implements Step
{
	/**The tree relationship between this step and the context node.*/
	private final String axis;

		/**@return The axis of the location step.*/
		public String getAxis() {return axis;}

	/**The node type selected by the location step.*/
	private final String nodeTest;

		/**@return The node type selected by the location step.*/
		public String getNodeTest() {return nodeTest;}

	/**Constructs a new location step with the specified axis and node test.
	@param newAxis The relationship between this step and the context node.
	@param newNodeTest The node type selected by the location step.
	*/
	public AxisStep(final String newAxis, final String newNodeTest)
	{
		this.axis=newAxis;	//set the axis
		this.nodeTest=newNodeTest;	//set the node test
	}

	/**@return A string representation of the location step in the form "axis::nodeTest",
		or "root" for the special location step containing "/" as an axis.*/
	public String toString()
	{
		if(getAxis().equals(ROOT))	//if this is the special "/" location step
			return "root";	//return "root"
		else	//if this is a normal location step
			return getAxis()+AXIS_SEPARATOR_STRING+getNodeTest();	//return axis::nodeTest
	}
}
