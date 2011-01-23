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

package com.globalmentor.text.xml.xpath;

import static com.globalmentor.text.xml.xpath.XPath.*;

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
