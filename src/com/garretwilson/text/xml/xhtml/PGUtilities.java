package com.garretwilson.text.xml.xhtml;

import java.io.*;
import java.util.*;
import com.garretwilson.io.*;
import com.garretwilson.lang.*;
import static com.garretwilson.text.CharacterConstants.*;

import com.garretwilson.text.CharacterConstants;
import com.garretwilson.text.xml.XMLUtilities;
import com.garretwilson.util.Debug;
import org.w3c.dom.*;

/**Tidies an XHTML document representing a Project Gutenberg etext.
<p>The header is considered to end on the line containing "*END*".</p>
<p>For extracting title and author information, the following header paragraphs
	inside the header are supported:</p>
<dl>
	<dt>bma10.txt</dt>
	<dd>Project Gutenberg Etext of Bulfinch's Mythology, The Age of Fable<br />
	#1 in our series by Thomas Bulfinch</dd>
	<dt>0ws3510.txt</dt>
	<dd>***The Project Gutenberg's Etext of Shakespeare's First Folio***<br />
	************The Tragedie of Anthonie, and Cleopatra*************</dd>
	<dt>vngln10i.txt</dt>
	<dd>The Project Gutenberg Etext of Evangeline, by Henry W. Longfellow<br />
	#6 in our series by Henry W. Longfellow</dd>
	<dt>siddh02.txt</dt>
	<dd>The Project Gutenberg Etext of "Siddhartha," by Hermann Hesse<br />
	#2 in our series by Hesse</dd>
	<dt>sawyr10.txt</dt>
	<dd>**The Project Gutenberg Etext of Tom Sawyer, by Twain/Clemens**<br />
	******This file should be named sawyr10.txt or sawyr10.zip*****</dd>
	<dt>poe3v11.txt</dt>
	<dd>The Project Gutenberg Etext of The Works of Edgar Allan Poe V. 3<br />
	Volume 3 of the Raven Edition<br />
	#8 in our series by Edgar Allan Poe</dd>
	<dt>ozland10.txt</dt>
	<dd>*******The Project Gutenberg Etext of The Marvelous Land of Oz*<br />
	*******This file should be named ozland10.txt or ozland10.zip**</dd>
	<dt>8cncd10.txt</dt>
	<dd>The Project Gutenberg Etext of A Week on the Concord and Merrimack Rivers<br />
	by Henry David Thoreau</dd>
	<dt>ynkgp10.txt</dt>
	<dd>***The Project Gutenberg Etext of Yankee Gypsies, by Whittier***<br />
	#1 in our series by John Greenleaf Whittier</dd>
	<dt>wman11.txt</dt>
	<dd>*The Project Gutenberg Edition of "What Is Man?" by Mark Twain*<br />
	#1 in our series by Mark Twain [Samuel Langhorne Clemens]</dd>
	<dt>phant12.zip</dt>
	<dd>The Project Gutenberg EBook of The Phantom of the Opera, by Gaston Leroux<br />
	#1 in our series by Gaston Leroux</dd>
	<dt>uscon95b.txt</dt>
	<dd>****Project Gutenberg's United States Congress Address Book****</dd>
	<dt>ftroy10.txt</dt>
	<dd>Project Gutenberg Etext; The Fall of Troy, by Quintus Smyrnaeus</dd>

</dl>
@author Garret Wilson
*/
public class PGUtilities
{

	/**The title string that appears in all Project Gutenberg title lines.*/
//G***del	public final static String HEADER_LINE_SUBSTRING="The Project Gutenberg Etext";

	/**The first part of the words "Project Gutenberg".
	Some works such as optns10.txt misspell this as "Project Gutenburg".
	*/
	public final static String PROJECT_GUTENB="Project Gutenb";

	/**The word "copyright".*/
	public final static String COPYRIGHT="copyright";

	/**The word "Edition".*/
	public final static String EDITION="Edition";

	/**The word "EText".*/
	public final static String ETEXT="Etext";

	/**The word "EBook".*/
	public final static String EBOOK="EBook";

	/**The word "the".*/
	public final static String THE="the";

	/**The word "book".*/
	public final static String BOOK="book";

	/**The word "is".*/
	public final static String IS="is";

	/**The word "of".*/
	public final static String OF="of";

	/**The word "by" to indicate an author.*/
	public final static String BY="by";

	/**A substring indicating Project Gutenberg small print.*/
	public final static String SMALL_PRINT="SMALL PRINT";

	/**A substring marking the start of the Project Gutenberg small print.*/
	public final static String SMALL_PRINT_START="START";

	/**A substring marking the end of the Project Gutenberg small print.*/
	public final static String SMALL_PRINT_END="END";

	/**Tidies an XHTML document representing a Project Gutenberg etext.
	@param document The XHTML document containing the Project Gutenberg text.
	*/
/*G***fix
	public static void tidy(final Document document)
	{
		final Element bodyElement=XHTMLUtilities.getBodyElement();  //get the



//G***fix XMLUtilities.extractChildren()

	}
*/

	/**Removes the Project Gutenberg header.
		In most cases, the header and the small print will be combined before the
		start of the text. If the header and the small print are separated by the
		content, both will be extracted, concatenated, and returned.
	@param document The XHTML document containing the Project Gutenberg text.
	@return A document fragment that contains the text of the header that was
		removed from the document, or <code>null</code> if a header could not be
		found.
	*/
	public static DocumentFragment extractHeader(final Document document/*G***del, final String title*/)
	{
		final Element bodyElement=XHTMLUtilities.getBodyElement(document);  //get the <body> element of the XHTML document
		int divIndex=-1;  //we'll check for dividers, just in case we can't find the header
		int sendMoneyIndex=-1;  //we'll find the "send money" index in case we need it to be a divider
//G***del for now		int titleIndex=-1;  //we'll check for the title, just in case we can't find the header
		int smallPrintStartIndex=-1;  //we'll check for the beginning of the small print, just in case we can't find the header
		final NodeList childNodes=bodyElement.getChildNodes();  //get the list of body child nodes
		for(int i=0; i<childNodes.getLength(); ++i) //look at each child node
		{
			final Node childNode=childNodes.item(i);  //get this child node
			if(childNode.getNodeType()==Node.ELEMENT_NODE)  //if this is an element
			{
				final Element childElement=(Element)childNode;  //get a reference to this element
				final String text=XMLUtilities.getText(childElement, true).trim(); //get the text of the element
//G***del Debug.trace("looking at PG heading text: ", text); //G***del
				if(text.equals("***") && divIndex<0)  //if this is a divider and it's the first division we've found G***use a constant
				{
Debug.trace("this is a *** divider: "+i); //G***del
//G***del System.out.println("this is a divider"); //G***del
					divIndex=i; //save the index of the divider in case we need it
				}
					//sometimes (e.g. lied210.txt) the end of the small print is missing, but this string gets us pretty close
				else if(Strings.indexOfIgnoreCase(text, "*WANT* TO SEND MONEY")>=0) //G***use a constant
				{
Debug.trace("found *want* to send money at index: ", i);  //G***del
					sendMoneyIndex=i; //this is the best divider we can find without actually finding the end of the small print
				}
				  //if we find that the text is starting, we'll count that as a divider if we haven't yet found one (e.g. "crowd11a.txt)
				else if(Strings.indexOfIgnoreCase(text, "Start of the Project Gutenberg")>=0 && divIndex<0) //G***use a constant
				{
Debug.trace("this is 'Start of the PG' a divider: "+i); //G***del
//G***del System.out.println("this is a divider"); //G***del
					divIndex=i; //save the index of the divider in case we need it
				}
						//if this is the start of the PG header, and it's the first start we've found
				else if(smallPrintStartIndex<0 && Strings.indexOfIgnoreCase(text, SMALL_PRINT)>=0 && Strings.indexOfIgnoreCase(text, SMALL_PRINT_START)>=0)
				{
Debug.trace("small print start: ", i); //G***del
//G***del System.out.println("found first small print start: "+i); //G***del
					smallPrintStartIndex=i; //make a note of the start of the small print
				}
					//if this is end of the the Project Gutenberg footer and we haven't found the start of the small print
				else if(isPGFooterEnd(text) && smallPrintStartIndex<0)
				{
Debug.trace("is PG footer end and no small print in sight: ", i); //G***del
Debug.trace(text);  //G***del
//G***del System.out.println("found footer end without small print start: "+i); //G***del
					smallPrintStartIndex=i+1; //we'll pretend the small print starts just after the end of the footer
				}
				else if(isPGHeaderEnd(text) || i>childNodes.getLength()-4)  //if this is the end of the PG header, or if we're out of elements (we'll just have to guess that the last element is close to the end, and that subsequent ending text nodes have been combined)
				{
Debug.trace("is end of PG header"); //G***del
Debug.trace(text);  //G***del
//G***del System.out.println("found PG header end: "+i); //G***del
				  if(divIndex<0 || sendMoneyIndex<divIndex+50)  //if we didn't find a divider, or the divider is close to the send money index G***use a constant
						divIndex=sendMoneyIndex;  //use the send money index as the divider
					if(i<childNodes.getLength()-5)  //if the end of the header is not too close to the end of the document
				    return XMLUtilities.extractChildren(bodyElement, 0, i+1);  //extract all the header elements, including this one
					else if(divIndex>=0)  //if the header takes up the whole document, but we've found a divider before
					{
Debug.trace("header takes up whole document"); //G***del
Debug.trace("small print start: ", smallPrintStartIndex); //G***del
Debug.trace("div index: ", divIndex); //G***del
							//if the small print starts before the divider, just after the
							//  divider, or if we didn't find the start of the small print
						if(smallPrintStartIndex<divIndex+10)
						{
Debug.trace("extracting children up to divider: "+divIndex); //G***del
							return XMLUtilities.extractChildren(bodyElement, 0, divIndex+1);  //extract all the header elements up to the divider
						}
						else
							//if the start of the small print comes significantly after the divider,
							//  we'll assume there's content between the header and the small print
							//  ---but make sure that content includes the middle of the document
//G***fix for ffn110.txt						else if(divIndex<childNodes.getLength()/2 && smallPrintStartIndex>childNodes.getLength()/2)
						{
//G***del System.out.println("the start of the small print comes way after the divider: "+smallPrintStartIndex); //G***del
								//get the first part of the document, up to the divider
							final DocumentFragment headerFragment=XMLUtilities.extractChildren(bodyElement, 0, divIndex+1);
								//extract the small print, which comes at the end (compensating for removing the header)
							final DocumentFragment smallPrintFragment=XMLUtilities.extractChildren(bodyElement, smallPrintStartIndex-(divIndex+1), i-divIndex);
							headerFragment.appendChild(smallPrintFragment); //combine the two fragments
							return headerFragment;  //return the combined header fragment
						}
					}
				}
/*G***del for now; this is too complicated
					//if we find the title on a line that's not the header line
				if(title!=null && StringUtilities.indexOfIgnoreCase(text, title)>=0
					  && !isHeaderElement(childElement))
				{
//G***del Debug.trace("this is 'Start of the PG' a divider: "+i); //G***del
//G***del System.out.println("this is a divider"); //G***del
					titleIndexIndex=i; //save the index of the title in case we need it
				}
*/
			}
		}
		return null;  //show that we couldn't find a PG header
	}

	/**Determines if the given text is the end of the Projet Gutenberg header.
	@param text The text to check for being the end of the header.
	@return <code>true</code> if the given text is the end of the Project
		Gutenberg header.
	*/
	protected static boolean isPGHeaderEnd(final String text)
	{
//G***del Debug.trace("checking end of PG header: ", text); //G***del
		  //we'll look at each line separately; some works (such as orrbr10.txt
			//  which uses single spacing for the header but double spacing for the
			//  rest), might result in multiple lines in one paragraph, with multiple
			//  occurences of "end"
		final StringTokenizer lineTokenizer=new StringTokenizer(text, EOL_CHARS); //tokenize the lines of the string
		while(lineTokenizer.hasMoreTokens())  //while there are more lines
		{
			final String line=lineTokenizer.nextToken();  //get the next line
			final int smallPrintIndex=Strings.indexOfIgnoreCase(line, SMALL_PRINT); //get the index of "small print"
//G***del 	Debug.trace("offset of small print: ", smallPrintIndex); //G***del
			final int smallPrintEndIndex=Strings.indexOfIgnoreCase(line, SMALL_PRINT_END);  //get the index of "end"
//G***del 	Debug.trace("offset of end: ", smallPrintEndIndex); //G***del
			if(smallPrintIndex>=0 && smallPrintEndIndex>=0) //if both "small print" and "end" are on the same line
			{
	/*G***del; improved
				final int startIndex=Math.min(smallPrintIndex, smallPrintEndIndex); //see which phrase comes first
				final int endIndex=Math.max(smallPrintIndex, smallPrintEndIndex); //see which phrase comes last
	Debug.trace("start index: ", startIndex); //G***del
	Debug.trace("end index: ", endIndex); //G***del
					//see if there's a line break after the first phrase
				final int eolIndex=StringUtilities.charIndexOf(line, EOL_CHARS, startIndex);
	Debug.trace("eol index: ", eolIndex); //G***del
				if(eolIndex<0 || eolIndex>endIndex) //if there are no line breaks between the phrases
	*/
					return true;  //this is probably a Project Gutenber header end
			}
		}
/*G***fix
			//some books (e.g. "crowd11a.txt") don't have a header "END", but simply say the book istarting
		if(StringUtilities.indexOfIgnoreCase(text, "START OF THE PROJECT GUTENBERG EBOOK")>=0) //if the book is starting
			return true;  //the start of the book is the end of the header
*/
		return false; //this text didn't pass our test
	}

	/**Removes the Project Gutenberg footer.
	@param document The XHTML document containing the Project Gutenberg text.
	@return A document fragment that contains the text of the footer that was
		removed from the document, or <code>null</code> if a footer could not be
		found.
	*/
	public static DocumentFragment extractFooter(final Document document)
	{
		final Element bodyElement=XHTMLUtilities.getBodyElement(document);  //get the <body> element of the XHTML document
		final NodeList childNodes=bodyElement.getChildNodes();  //get the list of body child nodes
		for(int i=childNodes.getLength()-1; i>=0; --i) //look at each child node, starting at the end
		{
			final Node childNode=childNodes.item(i);  //get this child node
			if(childNode.getNodeType()==Node.ELEMENT_NODE)  //if this is an element
			{
				final Element childElement=(Element)childNode;  //get a reference to this element
				final String text=XMLUtilities.getText(childElement, true); //get the text of the element
				if(isPGFooterEnd(text))  //if this is the end of the Project Gutenberg footer
				  return XMLUtilities.extractChildren(bodyElement, i, childNodes.getLength());  //extract all the footer elements from here on
			}
		}
		return null;  //show that we couldn't find a PG footer
	}

	/**Finds the Project Gutenberg footer element.
	@param document The XHTML document containing the Project Gutenberg text.
	@return The element containing the Project Gutenberg footer information, or
		<code>null</code> if a footer could not be found.
	*/
	public static Element getPGFooterElement(final Document document)
	{
		final Element bodyElement=XHTMLUtilities.getBodyElement(document);  //get the <body> element of the XHTML document
		final NodeList childNodes=bodyElement.getChildNodes();  //get the list of body child nodes
		for(int i=childNodes.getLength()-1; i>=0; --i) //look at each child node, starting at the end
		{
			final Node childNode=childNodes.item(i);  //get this child node
			if(childNode.getNodeType()==Node.ELEMENT_NODE)  //if this is an element
			{
				final Element childElement=(Element)childNode;  //get a reference to this element
				final String text=XMLUtilities.getText(childElement, true); //get the text of the element
				if(isPGFooterEnd(text))  //if this is the end of the Project Gutenberg footer
				{
				  return childElement;  //return this footer element
				}
			}
		}
		return null;  //show that we couldn't find a PG footer
	}



	/**Determines if the given text is the end of the Projet Gutenberg footer.
	@param text The text to check for being the end of the footer.
	@return <code>true</code> if the given text is the end of the Project
		Gutenberg footer.
	*/
	protected static boolean isPGFooterEnd(final String text)
	{
		boolean foundEnd=false; //this will keep track of whether we've found a line beginning with "end"
		boolean foundPG=false;  //this will keep track of whether we've found a line containing "Project Gutenberg"
		final StringTokenizer lineTokenizer=new StringTokenizer(text, EOL_CHARS); //tokenize the lines of the string
		while(lineTokenizer.hasMoreTokens())  //while there are more lines
		{
				//get the next line and trim it of whitespace and asterisks
			final String line=Strings.trim(lineTokenizer.nextToken(), TRIM_CHARS+'*'); //G***use a constant
			if(Strings.startsWithIgnoreCase(line, "end")) //if the line starts with "end" G***use a constant
				foundEnd=true;  //show that we found the "end" line
			if(Strings.indexOfIgnoreCase(text, PROJECT_GUTENB)>=0) //if this line contains "Project Gutenberg" in it
				foundPG=true; //show that we found the Project Gutenberg line
			if(foundEnd && foundPG) //if we've found both cases in this paragraph
				return true;  //we think this is a Project Gutenberg footer
		}
		return false;  //we don't think this is the Project Gutenberg footer
	}


	/**Determines the title of the Project Gutenberg etext from the header. The
		"Title: " property is first located, but ignored if its value is "title".
		If unsuccessful, the information is extracted from the header.
	@param headerFragment A document fragment containing a Project Gutenberg
		header.
	@return The title of the work, or <code>null</code> if the name could not be
		determined.
	*/
	public static String getTitle(final DocumentFragment headerFragment)
	{
Debug.trace("getting PG title");  //G***del
		Element nextLineTitleAfterElement=null; //in some special cases, we'll know when the next line will be the title
//G***del		boolean isNextLineTitle=false; //in some special cases, we'll know when the next line will be the title
		String title=getPropertyValue(headerFragment, "title", ':'); //try to get the title property in the header G***use a constant here
		if(title==null) //if we couldn't find the title property
		{
			boolean foundPGTitle=false; //show that we haven't found a title on the "Project Gutenberg" line
			final NodeList childNodes=headerFragment.getChildNodes();  //get the list of child nodes
			for(int childNodeIndex=0; childNodeIndex<childNodes.getLength(); ++childNodeIndex) //look at each child node
			{
				final Node childNode=childNodes.item(childNodeIndex);  //get this child node
				if(childNode.getNodeType()==Node.ELEMENT_NODE)  //if this is an element
				{
					final Element childElement=(Element)childNode;  //get a reference to this element
					final String text=XMLUtilities.getText(childElement, true); //get the text of the header
//G***del System.out.println("looking at child element: "+childElement);
					if(isPGHeaderElement(childElement))  //if this is a header element
					{
Debug.trace("found header element");  //G***del
//G***del System.out.println("found header element with text: "+text);  //G***del
		//G***del Debug.trace("header text: ", text);  //G***del
						final StringTokenizer lineTokenizer=new StringTokenizer(text, EOL_CHARS); //create a tokenizer to look at each line
						while(lineTokenizer.hasMoreTokens())  //while there are more lines
						{
							final String line=lineTokenizer.nextToken();  //get the next line
//G***del System.out.println("looking at line: "+line);  //G***del
		//G***del System.out.println("looking at line: "+line);  //G***del
		//G***del Debug.trace("line: ", line);  //G***del
								//check for both "Project Gutenberg" and "EText", because some works
								//  contain "Project Gutenberg EText" and some contain "Project
								//  Gutenberg's EText"; some works (such as etarn10.txt) have a line
								//  such as "This is a COPYRIGHTED Project Gutenberg Etext,
								//  Details Below", which is *not* a header element, although some
								//  (such as clstn10.txt) have two lines, one with "copyright", the
								//  other without
							final String projectGutenbergString=getProjectGutenbergString(line);
								//get the index of the string if it exists
							final int projectGutenbergIndex=projectGutenbergString!=null ? Strings.indexOfIgnoreCase(line, projectGutenbergString) : -1;
//G***del							final int projectGutenbergIndex=StringUtilities.indexOfIgnoreCase(line, PROJECT_GUTENB);
								//see if the word "copyright" is on the line, which means this is a false header
							int copyrightIndex=Strings.indexOfIgnoreCase(line, COPYRIGHT);
							if(Strings.indexOfIgnoreCase(line, "of:")>=0  //if the line contains "of:" G***last-minute hack for radio10.txt
									|| Strings.indexOfIgnoreCase(line, "of the")>=0)  //if the line contains "of the" G***last-minute hack for berne10.txt
								copyrightIndex=-1;  //G***testing
								//see if "Project Gutenberg EText" or "Project Gutenberg Edition" is on this line
							if(projectGutenbergIndex>=0 && copyrightIndex<0)  //if "Project Gutenberg" is on this line, *without* a copyright indication
							{
								final char originalFirstChar=line.charAt(0); //get the original first character in case we need it
								final char originalLastChar=line.charAt(line.length()-1); //get the original last character in case we need it
Debug.trace("found PG in line: "+line);  //G***del
		//G***del Debug.trace("found project gutenberg"); //G***del
								String eTextString=getETextString(line);  //get the etext string on the line
									  //get the index of the etext string, if there was one
										//  (if there wasn't one, the only way isPGHeaderElement()
										//  would have reported true is if this is the first element,
										//  so that's OK)
								int eTextStringIndex;
								boolean isSpecialFirstPGHeader=false; //we'll determine if this is the special beginning PG header; if so, we won't need to check about the etext string
								if(eTextString!=null) // we have an etext string
								{
									eTextStringIndex=Strings.indexOfIgnoreCase(line, eTextString);  //find out where the string starts
								}
								else  //if we didn't find "etext"
								{
										//skip "Project Gutenberg"
									eTextStringIndex=projectGutenbergIndex+projectGutenbergString.length()+1;
Debug.trace("no etext, pretending it's at index: ", eTextStringIndex);  //G***del
									if(eTextStringIndex<line.length()) //if there are characters after "Project Gutenberg"
									{
										eTextString=""; //assign a fake etext string, so that our calculations below will work
										isSpecialFirstPGHeader=true;  //show that we're assuming that this is the special starting Project Gutenberg header
									}
									else  //if we run out of characters
									{
										eTextStringIndex=-1;  //we can't use this line
									}
								}
									//get the first non-whitespace character
								final char firstChar=line.charAt(CharSequenceUtilities.notCharIndexOf(line, TRIM_CHARS));
//G***del			Debug.trace("first char: "+firstChar);  //G***del
									//if this is the correct line, and there's whitespace or dependent punctuation after the etext string
								if(eTextStringIndex>=0 && eTextStringIndex+eTextString.length()<line.length()
										&& (firstChar=='*'  //G***test with original problem etext, use constant
											|| isSpecialFirstPGHeader //we don't need to check for etext for the special header that has no etext
											|| CharacterUtilities.isWhitespace(line.charAt(eTextStringIndex+eTextString.length()))
										  || DEPENDENT_PUNCTUATION_CHARS.indexOf(line.charAt(eTextStringIndex+eTextString.length()))>=0))
								{
Debug.trace("found etext: "+eTextString); //G***del
//G***del Debug.trace("found etext: "+eTextString); //G***del
										//remove the header string and everything before it, and tidy the title
									String remainingText=tidyTitle(line.substring(eTextStringIndex+eTextString.length()));
									final boolean hasOf;  //we'll see if this heading has "of", which gives it more weight
										//if the string starts with "of" (but only in lowercase), remove the "of"
									if(remainingText.startsWith(OF) || remainingText.startsWith(IS)) //if the string starts with "of" or "is"
									{
										hasOf=true; //show that this looks even more like the right heading
										remainingText=tidyTitle(remainingText.substring(OF.length()));  //remove the beginning "of" and tidy the string (this only works for both "of" and "is" because they are both the same length)
Debug.trace("removed of: ", remainingText); //G***del
/*G***del
														//G***this should probably go into a tidyTitle() method
											//if the string starts with "the" or "the boook" (but only in lowercase), remove those words
										if(remainingText.startsWith(THE)) //if the string starts with "the" (but only in lowercase), remove "the"
										{
											remainingText=tidyProperty(remainingText.substring(THE.length()));  //remove the beginning "the" and tidy the string
											if(remainingText.startsWith(BOOK)) //if the string starts with "book" (but only in lowercase), remove "book"
											{
												remainingText=tidyProperty(remainingText.substring(BOOK.length()));  //remove the beginning "book" and tidy the string
											}
										}
*/

										if(remainingText.length()==0) //if there is no text left, the "of"... must have meant the title is on the next line
										{
											if(lineTokenizer.hasMoreTokens())  //if there are more lines
											{
												remainingText=tidyTitle(lineTokenizer.nextToken());  //get the next line and tidy it
											}
										}
									}
									else  //if neither "of" nor "is" is present
										hasOf=false;  //there is no "of"
										//remove "by" if present and tidy the property
//G***del; moved									remainingText=tidyProperty(StringUtilities.removeLengthIgnoreCase(remainingText, BY));
									if(remainingText.length()>0)  //if we have anything remaining
									{
											//only override an old title if this line has "of"
										if((title==null || hasOf==true)
											&& !foundPGTitle  //if we haven't already found a title in another PG line
											&& CharSequenceUtilities.containsLetterOrDigit(remainingText))
										{
										  title=tidyTitle(remainingText);  //whatever is left is the title
Debug.trace("found PG title: ", title); //G***del
											foundPGTitle=true;  //show that we found a title on the Project Gutenberg line, so don't use any titles from other Project Gutenberg lines
												//compensate for incomplete titles (e.g. "Volume 1:" from 1dfre10.txt, part of a title in 2ppdl10.txt))
/*G***del
											final String originalLine=StringUtilities.trim(line, "*");  //look at the original line (minus asterisks) G***use a constant
											final char originalLastChar=originalLine.charAt(originalLine.length()-1); //get the original last character
*/

										  final int byIndex=getByIndex(line);  //see where "by" appears on the line
										    //if the line originally ended with dependent punctuation
												//  (such as a ':') or the line started with an asterisk
												//  but didn't end with an asterisk, or what we have so
												//  far ends in "of" (e.g. 15frd10.txt) or "de"
												//  (e.g. marbo10.txt), and there are more lines, we'll
												//  add the next line to our title (but only if the
												//  author doesn't appear on this line)
											if(lineTokenizer.hasMoreTokens() && byIndex<0 &&
												  (DEPENDENT_PUNCTUATION_CHARS.indexOf(originalLastChar)>=0
												  || (originalFirstChar=='*' && originalLastChar!=originalFirstChar)
													|| title.endsWith(" "+OF)
													|| title.endsWith(" in")  //(e.g. frnrd10.txt) //G***use a constant
													|| title.endsWith(" and")  //(e.g. miltp10.txt, although that text will get these lines separated if paragraph sensing is turned on) //G***use a constant
													|| title.endsWith(" de")))  //G***use a constant
											{
												final String nextLine=lineTokenizer.nextToken();  //get the next line
												if(!isFileLine(nextLine)) //if this is not the file line
												{
													if(DEPENDENT_PUNCTUATION_CHARS.indexOf(originalLastChar)>=0)  //if the line ended with dependent punctuation
														title+=originalLastChar;  //add the puncutation back
														//add the tidied next line, and tidy everything again
													title=tidyTitle(title+" "+tidyTitle(nextLine));
												}
											}
										}
Debug.trace("we think the title is: "+title);  //G***del
										if(lineTokenizer.hasMoreTokens()) //if there are more lines
										{
											final String nextLine=lineTokenizer.nextToken();  //get the next line, just to make sure
												//some PG works (e.g. Shakespeare's folios) actually have the title on the following line
												//  some lines surrounded by asterisks simply tell the intended filename, however
												//  furthermore, some lines surrounded by asterisks (e.g. jarg400.txt) just tell about older versions of etexts
												//if the next line starts and ends with asterisks, but is not the file line
											if(nextLine.startsWith("*") //G***use a constant
												&& nextLine.endsWith("*") //G***use a constant
												&& getETextString(nextLine)==null //make sure there is no string like "etext"
												&& !isFileLine(nextLine)
												&& getByIndex(line)<0 //if the current line has "by" in it, the title on the first line was probably correct (e.g. acrdi10.txt)
													  //if the first line has "of" appearing before the almost-end
												&& (
													  (Strings.indexOfIgnoreCase(line, " of ")<0 || Strings.indexOfIgnoreCase(line, " of ")>(line.length()*3/4)) //it's risky going to the next line---make sure we want to go there (e.g. berne10.txt)
														|| CharSequenceUtilities.endsWithIgnoreCase(remainingText, " folio")  //if the title ends in "folio" (e.g. 0ws??10.txt), the actual title is probably on the next line
														)
												)
											{
												remainingText=tidyTitle(nextLine); //tidy the remaining line
												if(CharSequenceUtilities.containsLetterOrDigit(remainingText))  //if the trimmed string is a title
													title=remainingText;  //use this for a title
											}
										}
										break;  //stop looking for the title
									}
								}
							}
						}
						if(title!=null) //if we have found a title
						{
							break;  //stop looking at elements
						}
					}
					final StringTokenizer lineTokenizer=new StringTokenizer(text, EOL_CHARS); //create a tokenizer to look at each line
					while(lineTokenizer.hasMoreTokens())  //while there are more lines
					{
						final String line=lineTokenizer.nextToken();  //get the next line
						if(nextLineTitleAfterElement!=null)
						{
							title=line; //we'll use this text for the title
							nextLineTitleAfterElement=null;
							break;  //stop looking for the title
						}
						else if(CharSequenceUtilities.endsWithIgnoreCase(line.trim(), "Etext of:"))  //e.g. email025.txt G***use a constant
						{
							nextLineTitleAfterElement=childElement; //G***testing
						}
					}
					if(nextLineTitleAfterElement!=null && nextLineTitleAfterElement!=childElement)
					{
						title=text; //we'll use this text for the title
						break;  //stop looking for the title

					}
/*G***del
					if(StringUtilities.endsWithIgnoreCase(text.trim(), "Etext of:"))  //e.g. email025.txt G***use a constant
					{
Debug.trace("found special line: ", text);  //G***del
						isNextLineTitle=true;  //show that the next line will be the title
					}
					else if(isNextLineTitle) //if we have told ourselves that the next line is the title
					{
						title=text; //we'll use this text for the title
						break;  //stop looking for the title
					}
*/
				}
			}
		}
		return title!=null ? tidyTitle(title) : null; //return the title we found, if any---always tidy it once more if we have a valid string (it might have been a property which we haven't tidied yet, for example)
	}

	/**Removes the word "by" and everything following from a string.
		Convenience method used when locating a title.
	@param string The string that may contain "by".
	@return The string with "by" and everything following it removed.
	*/
/*G***del
	protected static String removeBy(final String string)
	{
		final int byIndex=StringUtilities.indexOfIgnoreCase(string, BY);  //see if there is a "by" in the text
		if(byIndex>=0) //if there is a "by"
		{
				//remove the "by" and everything following, and tidy the property
			return remainingText.substring(0, byIndex);
		}
		else  //if "by" isn't present
			return string;  //return the original string
	}
*/

	/**Determines the author of the Project Gutenberg etext from the header. The
		"Author: " property is first located, but ignored if its value is "author".
		If unsuccessful, a single line beginning with "by" is located.
		If unsuccessful, the information is extracted from the header.
	@param headerFragment A document fragment containing a Project Gutenberg
		header.
	@param title The title of the work, if it is known, or <code>null</code> if
		the title is not known.
	@return The author of the work, or <code>null</code> if the name could not be
		determined.
	*/
	public static String getAuthor(final DocumentFragment headerFragment, final String title)
	{
//G***del		boolean isNextLineAuthor=false; //in some special cases, we'll know when the next line will be the author
		String author=getPropertyValue(headerFragment, "author", ':'); //try to get the author property in the header G***use a constant here
Debug.trace("got author property: ", author); //G***del
		if(author==null) //if we couldn't find the author property
		{
			author=getPropertyValue(headerFragment, "authors", ':'); //try to get the authors property in the header G***later try to separate them out G***use a constant here
Debug.trace("got authors property: ", author); //G***del
		}
		if(author!=null && Strings.startsWithIgnoreCase(author.trim(), "several"))  //if this author is "several" (e.g. cm63b10.txt) G***use a constant
		{
			author=null;  //we really didn't find an author
		}
		if(author==null) //if we couldn't find the author property
		{
//G***del when works		  author=getPropertyValue(headerFragment, "by", ' '); //try to get the "by XXX" property in the header G***use a constant here
/*G**fix
		}
//G***del; testing		if(author==null) //if we still couldn't find the author property
		{
*/
			boolean foundByAuthor=false;  //this indicates if we've used "by XXX" to get the author
			boolean foundPGByAuthor=false;  //this indicates if we've used a Project Gutenberg "by XXX" to get the author
			String titlePossessionAuthor=null;  //if nothing else, we may be able to the author's name in the title
			String editedByAuthor=null; //even worse, we may want to use the editor
			final NodeList childNodes=headerFragment.getChildNodes();  //get the list of child nodes
			for(int childNodeIndex=0; childNodeIndex<childNodes.getLength(); ++childNodeIndex) //look at each child node
			{
				final Node childNode=childNodes.item(childNodeIndex);  //get this child node
				if(childNode.getNodeType()==Node.ELEMENT_NODE)  //if this is an element
				{
					final Element childElement=(Element)childNode;  //get a reference to this element
					final String text=XMLUtilities.getText(childElement, true); //get the text of the element
					boolean isNextLineByAuthor=false; //we don't know yet if the author is on the next line
					if(isPGHeaderElement(childElement))  //if this is a header element
					{
Debug.trace("found PG header for author");  //G***del
//G***del						final String headerText=XMLUtilities.getText(childElement, true); //get the text of the header
						final StringTokenizer lineTokenizer=new StringTokenizer(text, EOL_CHARS); //create a tokenizer to look at each line
						while(lineTokenizer.hasMoreTokens())  //while there are more lines
						{
							final String line=lineTokenizer.nextToken();  //get the next line
		//G***del Debug.trace("line: ", line);  //G***del
								//see if "Project Gutenberg EText" or "Project Gutenberg Edition" is on this line
							if(Strings.indexOfIgnoreCase(line, PROJECT_GUTENB)>=0)  //if "Project Gutenberg" is on this line
							{
Debug.trace("found PG in line for author: ", line);  //G***del
		//G***del Debug.trace("found project gutenberg"); //G***del
								final String eTextString=getETextString(line);  //get the etext string on the line
										//get the index of the etext string, if there was one
								final int eTextStringIndex=eTextString!=null ? Strings.indexOfIgnoreCase(line, eTextString) : -1;
									//if this is the correct line, and there's whitespace after the etext string
								if(eTextStringIndex>=0 && eTextStringIndex+eTextString.length()<line.length()
										&& (CharacterUtilities.isWhitespace(line.charAt(eTextStringIndex+eTextString.length()))
										  || DEPENDENT_PUNCTUATION_CHARS.indexOf(line.charAt(eTextStringIndex+eTextString.length()))>=0))
								{
		Debug.trace("found etext"); //G***del
										//remove the header string and everything before it
									String remainingText=line.substring(eTextStringIndex+eTextString.length());
		Debug.trace("first remaining text: ", remainingText); //G***del
									final int byIndex=getByIndex(remainingText);  //see if there is a "by" on the line
									if(byIndex>=0) //if there is a "by"
									{
											//remove the "by" and everything before it, and tidy the property
										remainingText=tidyAuthor(remainingText.substring(byIndex+BY.length()));
											//if we have anything remaining, and it's not "author", "himself", or "herself"
										if(!foundPGByAuthor //if we haven't already found a Project Gutenberg "by" author (e.g. ionly10.txt)
											  && CharSequenceUtilities.containsLetterOrDigit(remainingText)
											  && !"AUTHOR".equalsIgnoreCase(remainingText)  //G***use a constant
											  && !"himself".equalsIgnoreCase(remainingText)  //(e.g. advlr10.txt) G***use a constant
											  && !"herself".equalsIgnoreCase(remainingText))  //(e.g. advlr10.txt) G***use a constant
										{
		Debug.trace("remaining text: ", remainingText); //G***del
											author=tidyAuthor(remainingText);  //whatever is left is the author; tidy it
											foundPGByAuthor=true; //show that we used the Project Gutenberg header to get the by author
											continue; //try the other lines, just in case they have something better
										}
									}
										//try to find possession (e.g. "Shakespeare's") in the title---it may be all we have
									final int possessionIndex=Strings.indexOfIgnoreCase(remainingText, "'s");  //see if there is a "'s" in the title
									if(possessionIndex>0) //if there is possession in the title
									{
											//try to find the start of the word showing possession
										final int wordBeginIndex=CharSequenceUtilities.charLastIndexOf(remainingText, TRIM_CHARS, possessionIndex-1)+1;
										if(wordBeginIndex<possessionIndex)  //if this is a valid index
										{
											remainingText=remainingText.substring(wordBeginIndex, possessionIndex); //get the word showing possession
											if(remainingText.length()>0 //if we have anything remaining
											  && !Strings.startsWithIgnoreCase(remainingText, "everybody"))  //"everybody" isn't a valid author (as in "Everybody's Magazine", 10evm10.txt)
											{
Debug.trace("setting title possession author: ", titlePossessionAuthor);  //G***del
												titlePossessionAuthor=tidyAuthor(remainingText);  //whatever is left is the author
												continue; //try the other lines, just in case they have something better
											}
										}
									}
								}
							}
								//see if we can find "by XXX"
							if(!isFileLine(line)) //make sure this isn't the file line, which never has the author name
							{
Debug.trace("searching non-'PG' line: ", line); //G***del
								final int byIndex=getByIndex(line);  //see if there is a "by" on the line
								if(byIndex>=0 || isNextLineByAuthor) //if there is a "by", or we had decided the next line is the author
								{
Debug.trace("found by index: ", byIndex); //G***del
										//see if "used" appears (as in "used by")
								  final int usedIndex=Strings.indexOfIgnoreCase(line, "used "); //G***use a constant
										//remove the "by" and everything before it, and tidy the property
									String remainingText=byIndex>=0 //if we found by
										? tidyAuthor(line.substring(byIndex+BY.length())) //use everything after by
										: line; //if we didn't find by but we know the author should be on this line, use the whole line
//G***del System.out.println("remaining text: "+remainingText);
//G***del System.out.println("already have author: "+author);
									if(CharSequenceUtilities.containsLetterOrDigit(remainingText)  //if we have anything remaining
											&& (isNextLineByAuthor || usedIndex<0 || usedIndex!=byIndex-"used ".length()))  //make sure this isn't "used by" (e.g. brnte10.txt)
									{
Debug.trace("found non-PG 'by' author: ", remainingText); //G***del
Debug.trace("current author: ", author); //G***del
Debug.trace("already found 'by' author: "+foundByAuthor); //G***del
											//if we've already found an author, we'll only use the new author if it's a more complete version of the author already given (e.g. only the last name was given on the first line)
										if(author==null || CharSequenceUtilities.endsWithIgnoreCase(remainingText, author))
										{
											if(!foundByAuthor  //if this is the first author we've found using this method
											  && !Strings.startsWithIgnoreCase(remainingText, "AUTHOR")  //G***use a constant G***this code is duplicated
											  && !Strings.startsWithIgnoreCase(remainingText, "himself")  //(e.g. advlr10.txt) G***use a constant
											  && !Strings.startsWithIgnoreCase(remainingText, "herself"))  //(e.g. dbry11.txt) G***use a constant
											{
Debug.trace("setting author from non-PG by: ", remainingText);
												author=remainingText;  //whatever is left is the author
												foundByAuthor=true;  //show that we've already found an author this way, so don't replace this one if we find another
												continue; //try the other lines, just in case they have something better
											}
										}
									}
									else if(line.trim().endsWith(" "+BY)) //if this line ends with " by" in lowercase G***use a constant
									{
										isNextLineByAuthor=true;  //we'll expect the author to be on the next line
									}
								}
							}
						}
					}
/*G***del
					else if(StringUtilities.endsWithIgnoreCase(text.trim(), "Etext of:"))  //e.g. email025.txt G***use a constant
					{
Debug.trace("found special line: ", text);  //G***del
						isNextLineAuthor=true;  //show that the next line will be the author
					}
*/
						//if this element is not a header and we still don't have the author, it might talk about the title and author anyway, or even the editor
					else if(author==null)
					{
Debug.trace("we have no author");  //G***del
//G***del						final String text=XMLUtilities.getText(childElement, true); //get the text of the element
						if(Strings.indexOfIgnoreCase(text, PROJECT_GUTENB)<0) //the author paragraph should *not* have "Project Gutenberg" in it (e.g. 22gbl10.txt)
						{
							final StringTokenizer lineTokenizer=new StringTokenizer(text, EOL_CHARS); //create a tokenizer to look at each line
//G***del						final int lineCount=lineTokenizer.countTokens();  //get the line count G***check; last minute addition for early works
//G***del						if(lineCount<4) //if there aren't too many lines in this paragraph G***check; last minute addition for early works
							while(lineTokenizer.hasMoreTokens())  //while there are more lines
							{
								final String line=lineTokenizer.nextToken();  //get the next line
	Debug.trace("looking at line", line);  //G***del
								int startSearchIndex=-1; //we'll only start searching after the title or after "edited", as in "edited by"
								if(title!=null) //if we have a title
								{
Debug.trace("we have title but no author");  //G***del
									startSearchIndex=Strings.indexOfIgnoreCase(line, title);  //see if the title is on this line
									if(startSearchIndex>=0) //if we found something to search after
										startSearchIndex+=title.length(); //start searching after the title
								}
								if(startSearchIndex<0)  //if we didn't have a title or couldn't find it on this line
								{
									startSearchIndex=Strings.indexOfIgnoreCase(line, "edited");  //see if "edited", as in "edited by", is on this line G***use a constant
									if(startSearchIndex>=0) //if we found something to search after
										startSearchIndex+="edited".length(); //start searching after the string G***use a constant
								}
/*G***del
								if(startSearchIndex<0 && isNextLineAuthor)  //if we still don't know where to start searching, but we've told ourselves that the next line should be the author (e.g. email025.txt)
								{
									startSearchIndex=0; //start searching at the beginning
								}
*/
								if(startSearchIndex>=0) //if we have something to search after
								{
Debug.trace("we found a place to search for by");  //G***del
										 //see if there is a "by" on the line after the prefix
									final int byIndex=Strings.indexOfIgnoreCase(line, BY, startSearchIndex);
									if(byIndex>=0) //if there is a "by"
									{
											//remove the "by" and everything before it, and tidy the property
										final String remainingText=tidyAuthor(line.substring(byIndex+BY.length()));
										if(CharSequenceUtilities.containsLetterOrDigit(remainingText))  //if we have anything remaining
										{
Debug.trace("setting author to remaining text after by: ", remainingText);
											author=remainingText;  //whatever is left is the author
											break;  //this is about the best we can do
	//G***del										continue; //try the other lines, just in case they have something better
										}
									}
								}
							}
						}
					}
				}
			}
Debug.trace("author towards the end: ", author);  //G***del
				//try to get the "by XXX" property in the header G***use a constant here
		  final String byAuthor=getPropertyValue(headerFragment, "by", ' ');
Debug.trace("by author: ", author);  //G***del
				//if the by author is a more complete version of the PG header author
				//  (the PG header author should always be the most accurate, but it
				//  oftentimes be incomplete)
			if(byAuthor!=null && byAuthor.length()<128 //if we found a by author and it's not too long
				  && Strings.indexOfIgnoreCase(byAuthor, "PROJECT GUTENBERG-tm")<0)  //the line with "PROJECT GUTENBERG-tm" talks about using or reading the work, and is not the header (e.g. alad10.txt) G***use a constant
			{
				if(author==null //if we don't have an author yet, or the by author is a more complete version of the one we have
					  || (byAuthor.length()>author.length()
									&& Strings.indexOfIgnoreCase(byAuthor, author)>=0)
									&& !isFileLine(byAuthor)  //and this isn't the file line (e.g. sleep10.txt)
									)
				{
Debug.trace("setting author: ", byAuthor);  //G***del
				  author=byAuthor;  //use the by author
				}
			}
			if(author==null && titlePossessionAuthor!=null) //if we haven't found an author, but we found ownership indicated in the title
			{
Debug.trace("setting author to title possession: ", titlePossessionAuthor);  //G***del
				author=titlePossessionAuthor; //use that---it's the best we can find (this works for "Shakespeare's XXX", but not "Gulliver's Travels"
			}
		}
Debug.trace("author at end: ", author);  //G***del
		return author!=null ? tidyAuthor(author) : null; //return the title we found, if any---always tidy it once more if we have a valid string (it might have been a property which we haven't tidied yet, for example)
	}

	/**Create a description of the Project Gutenberg etext from the header. All
		lines relating to filenames are eleminated, the asterisks removed, and the
		lines concatenated.
	@param headerFragment A document fragment containing a Project Gutenberg
		header.
	@return A description of the work, or <code>null</code> if a description
		could not be determined.
	*/
	public static String getDescription(final DocumentFragment headerFragment)
	{
		final Element headerElement=getHeaderElement(headerFragment); //get the header element
		if(headerElement!=null)  //if we found the header element
		{
			final StringBuffer descriptionStringBuffer=new StringBuffer();  //create a new string buffer to hold the description we consruct
			final String headerText=XMLUtilities.getText(headerElement, true); //get the text of the header
			final StringTokenizer lineTokenizer=new StringTokenizer(headerText, EOL_CHARS); //create a tokenizer to look at each line
			while(lineTokenizer.hasMoreTokens())  //while there are more lines
			{
				final String line=lineTokenizer.nextToken();  //get the next line
					//if this isn't the filename line, and it's not the "copyright laws are changing all over the world..." line
				if(!isFileLine(line) && Strings.indexOfIgnoreCase(line, "copyright laws")<0)  //G***use a constant
				{
					if(descriptionStringBuffer.length()>0)  //if there is already part of a description
						descriptionStringBuffer.append(' ');    //separate the description components with spaces
							//trim both ends of "*" and whitespace and add the line to the description
					descriptionStringBuffer.append(Strings.trim(line, TRIM_CHARS+'*')); //G***use a constant here
				}
			}
			if(descriptionStringBuffer.length()>0)  //if we have a description
				return descriptionStringBuffer.toString();  //return the description string we constructed
		}
		return null;  //show that we couldn't find a description
	}

	/**Determines the language of the Project Gutenberg etext from the header. The
		"Language: " property is first located, but ignored if its value is "language".
	@param headerFragment A document fragment containing a Project Gutenberg
		header.
	@return The language of the work, or <code>null</code> if the language could
		not be determined.
	*/
	public static String getLanguage(final DocumentFragment headerFragment)
	{
		return getPropertyValue(headerFragment, "language", ':'); //try to get the language property in the header G***use a constant here
	}

	/**Finds the element containing the Project Gutenberg header lines.
	@param headerFragment A document fragment containing a Project Gutenberg
		header.
	@return The element containing the header ("The Project Gutenberg Etext"), or
		<code>null</code> if the header element could not be determined.
	*/
	public static Element getHeaderElement(final DocumentFragment headerFragment)
	{
//G***del Debug.trace("looking for header element");
		final NodeList childNodes=headerFragment.getChildNodes();  //get the list of child nodes
		for(int childNodeIndex=0; childNodeIndex<childNodes.getLength(); ++childNodeIndex) //look at each child node
		{
			final Node childNode=childNodes.item(childNodeIndex);  //get this child node
			if(childNode.getNodeType()==Node.ELEMENT_NODE)  //if this is an element
			{
				final Element childElement=(Element)childNode;  //get a reference to this element
				if(isPGHeaderElement(childElement)) //if this is a header element
					return childElement;  //return the element
			}
		}
		return null;  //show that we couldn't find the header element
	}

	/**Determines if this element containing the Project Gutenberg header lines.
	@param element An element from the header.
	@return <code>true</code> if this element contains the header information.
	*/
	public static boolean isPGHeaderElement(final Element element)
	{
		final String text=XMLUtilities.getText(element, true); //get the text of the element
Debug.trace("checking text for header element: ", text); //G***del
		final StringTokenizer lineTokenizer=new StringTokenizer(text, EOL_CHARS); //create a tokenizer to look at each line
		final int lineCount=lineTokenizer.countTokens();  //see how many lines we have
			//if this paragraph is four lines or less
		if(lineCount<=4
			  && Strings.indexOfIgnoreCase(text, "money")<0)  //G***use a constant G***test; last minute hack to get this to work on the early texts
//G***del			  && StringUtilities.indexOfIgnoreCase(text, "using or reading")<0)  //G***use a constant
		{
			while(lineTokenizer.hasMoreTokens())  //while there are more lines
			{
				final String line=lineTokenizer.nextToken();  //get the next line
	Debug.trace("checking line for header element: ", line); //G***del
					//check for both "Project Gutenberg" and "EText", because some works
					//  contain "Project Gutenberg EText" and some contain "Project
					//  Gutenberg's EText"; some works (such as etarn10.txt) have a line
					//  such as "This is a COPYRIGHTED Project Gutenberg Etext,
					//  Details Below", which is *not* a header element, although some
					//  (such as clstn10.txt) have two lines, one with "copyright", the
					//  other without
				final String projectGutenbergString=getProjectGutenbergString(line);
					//get the index of the string if it exists
				final int projectGutenbergIndex=projectGutenbergString!=null ? Strings.indexOfIgnoreCase(line, projectGutenbergString) : -1;
	//G***del			int projectGutenbergIndex=StringUtilities.indexOfIgnoreCase(line, PROJECT_GUTENB);
					//see if the word "copyright" is on the line, which means this is a false header
				final int copyrightIndex=Strings.indexOfIgnoreCase(line, COPYRIGHT);
	Debug.trace("project gutenbergIndex: "+projectGutenbergIndex); //G***del
				final String eTextString=getETextString(line);  //get the etext string on the line
	Debug.trace("eText String: "+eTextString); //G***del
						//get the index of the etext string, if there was one
				final int eTextStringIndex=eTextString!=null ? Strings.indexOfIgnoreCase(line, eTextString) : -1;
	Debug.trace("header etext string: "+eTextString);  //G***del
				if(projectGutenbergIndex>=0 && eTextStringIndex>=0 && copyrightIndex<0)  //if this element contains the header text
				{
							//make sure "etext" comes after "project gutenberg" (although it may come in the middle for "gutenberg's")
					if(eTextStringIndex>projectGutenbergIndex
								//make sure there's not too much between "project gutenberg" and "etext"
							&& eTextStringIndex<projectGutenbergIndex+projectGutenbergString.length()+4)  //G***use a constant
	//G***del						&& eTextStringIndex<projectGutenbergIndex+PROJECT_GUTENB.length()+3+4) //compensate again for the three letters we have missing G***use a constant
					{
	Debug.trace("found PG and etext");  //G***del
	//G***del; doesn't work					final boolean isFirstElement=element.getPreviousSibling()==null;  //find out if this element is the first node
	//G***del	Debug.trace("is first element: "+isFirstElement);  //G***del
						  //the line with "PROJECT GUTENBERG-tm" talks about using or reading the work, and is not the header (e.g. alad10.txt)
//G***del if not needed						if(StringUtilities.startsWithIgnoreCase(line.substring(projectGutenbergIndex), "PROJECT GUTENBERG-tm"))
						{
								//get the first non-whitespace character
							final char firstChar=line.charAt(CharSequenceUtilities.notCharIndexOf(line, TRIM_CHARS));
		//G***del Debug.trace("first char: "+firstChar);  //G***del
								//if the line doesn't start with '*', make sure there's whitespace or
								//  dependent punctuation after "etext" (e.g. it's not "...the first
								//  nine Project Gutenberg Etexts...")
							if(eTextStringIndex+eTextString.length()<line.length()
								&& (firstChar=='*'  //G***check with original problem etext; use constant
										|| CharacterUtilities.isWhitespace(line.charAt(eTextStringIndex+eTextString.length()))
										|| DEPENDENT_PUNCTUATION_CHARS.indexOf(text.charAt(eTextStringIndex+eTextString.length()))>=0))

							{
					//G***del System.out.println("found etext header element"); //G***del
		Debug.trace("found header element: ", text);  //G***del
								return true;  //this is a header element
							}
						}
					}
				}
					//some Project Gutenberg files (e.g. 22frd10.txt) start with just "Project Gutenberg..."
					//  if "Project Gutenberg" begins the element, and this is the first element
				if(projectGutenbergIndex==0)
				{
	Debug.trace("PG is at first of line");  //G***del
					boolean isFirstElement=true; //start out assuming this is the first element
					Node previousSiblingNode=element.getPreviousSibling();  //get the element's previous sibling
					while(previousSiblingNode!=null)  //while there are previous siblings
					{
							//if the previous sibling is an element, our element is not the first element
						if(previousSiblingNode.getNodeType()==previousSiblingNode.ELEMENT_NODE)
						{
							isFirstElement=false; //our element is not the first element
							break;  //stop trying to disprove ourselves
						}
						else  //if we haven't found a previous element so far
						{
							previousSiblingNode=previousSiblingNode.getPreviousSibling(); //get the previous sibling's previous sibling
						}
					}
	Debug.trace("is first element: "+isFirstElement);  //G***del
					if(isFirstElement)  //if this is the first element
						return true;  //show that this is the Project Gutenberg header element
				}
				if(isFileLine(line))  //if "Project Gutenberg" wasn't found, check for the file line (e.g. so that 2drvb10.txt can have a description)
				{
	Debug.trace("found header element: ", text);  //G***del
					return true;  //we'll assume the file line is in the same paragraph as the heading---in any case, this makes the pagraph *a* heading, if not *the* heading
				}
			}
		}
		return false;  //show that this doesn't appear to be the header element
	}

	/**Retrieves the value of a property stored in a header. Property values that
		are the same as the property name (e.g. "Title: TITLE") are ignored.
		Any line containing only "contents" is ignored, along with all following
		lines.
	@param headerFragment A document fragment containing a Project Gutenberg
		header.
	@param property The name of the property to retrieve; this name is case
		insensitive.
	@param delimiter The delimiter character (e.g. ':' or ' ') that separates the
		property from its value.
	@return The value of the property.
	*/
	public static String getPropertyValue(final DocumentFragment headerFragment, final String property, final char delimiter)
	{
		final NodeList childNodes=headerFragment.getChildNodes();  //get the list of child nodes
		for(int i=0; i<childNodes.getLength(); ++i) //look at each child node
		{
			final Node childNode=childNodes.item(i);  //get this child node
			if(childNode.getNodeType()==Node.ELEMENT_NODE)  //if this is an element
			{
				final Element childElement=(Element)childNode;  //get a reference to this element
				final String text=XMLUtilities.getText(childElement, true).trim(); //get the trimmed text of the element
//G***del Debug.trace("looking at property text: ", text);  //G***del
				  //if there are no ends-of-line in this text
//G***del; did we ever need this?				if(StringUtilities.charIndexOf(text, EOL_CHARS)<0)  //make sure CR/LF isn't present
//G***del				text.indexOf(CARRIAGE_RETURN_CHAR)<0 && text.indexOf(LINE_FEED_CHAR)<0)
				{
					if(Strings.startsWithIgnoreCase(text, property+delimiter)) //if the string starts with the property and the delimiter (e.g. "property:"), case insensitive
					{
						  //get everything after the colon and tidy it---that's the property value
						final String propertyValue=tidyProperty(text.substring(property.length()+1));
						if(!propertyValue.equalsIgnoreCase(property))  //if the property and its value are not the same (e.g. "Title: TITLE")
							return propertyValue; //return the value we found
					}
				}
			}
		}
		return null;  //show that we couldn't find the property
	}


	/**Determines if the given document is a Project Gutenberg EText.
	  Determination is made if the words "Project Gutenberg" and "EText" appear
		in a single line, followed at some point by a line containing "*START*" and
		"SMALL PRINT", and another containing "*END*" and "SMALL PRINT", case
		insensitive.
	@param document The XHTML document possibly containing the Project Gutenberg
		text.
	@return <code>true</code> if the text is a Project Gutenberg EText.
	*/
	public static boolean isProjectGutenbergEText(final Document document)
	{
			//these three pairs of strings are the the ones to search for, in the order of lines given
		final String[][] indicatorStrings={{PROJECT_GUTENB, ETEXT}, {SMALL_PRINT, SMALL_PRINT_START}, {SMALL_PRINT, SMALL_PRINT_END}};
		int indicatorIndex=0;
		final Element bodyElement=XHTMLUtilities.getBodyElement(document);  //get the <body> element of the XHTML document
		if(bodyElement!=null) //if there is a body element
		{
			final NodeList childNodes=bodyElement.getChildNodes();  //get the list of body child nodes
			for(int i=0; i<childNodes.getLength(); ++i) //look at each child node
			{
				final Node childNode=childNodes.item(i);  //get this child node
				if(childNode.getNodeType()==Node.ELEMENT_NODE)  //if this is an element
				{
					final Element childElement=(Element)childNode;  //get a reference to this element
					final String text=XMLUtilities.getText(childElement, true); //get the text of the element
//G***del Debug.trace("is pg?: ", text);  //G***del
//G***del Debug.trace("indicator index:", indicatorIndex);  //G***del
					if(indicatorIndex==0) //if this is the first indicator, do special checking
					{
//G***del System.out.println("Checking first indicator: etext string: "+getETextString(text));
							//if the text contains both "Project Gutenberg" and "EText", "Edition", or "EBook"
						if(Strings.indexOfIgnoreCase(text, PROJECT_GUTENB)>=0
							  && getETextString(text)!=null)
						{
							++indicatorIndex; //go to the next indicators
Debug.trace("found indicator: "+indicatorIndex); //G***del
							if(indicatorIndex>=indicatorStrings.length) //if we've matchd all the indicators
								return true;  //show that this document matches all our checks, so we think it is a Project Gutenberg EText
						}
					}
						//for the other indicators, do normal checks if the text contains both of our current indicators
					else if(Strings.indexOfIgnoreCase(text, indicatorStrings[indicatorIndex][0])>=0
						&& Strings.indexOfIgnoreCase(text, indicatorStrings[indicatorIndex][1])>=0)
					{
						++indicatorIndex; //go to the next indicators
Debug.trace("found indicator: "+indicatorIndex); //G***del
Debug.trace("ready to return true? "+(indicatorIndex>=indicatorStrings.length)); //G***del
//G***del System.out.println("return true? "+(indicatorIndex>=indicatorStrings.length)); //G***del
						if(indicatorIndex>=indicatorStrings.length) //if we've matchd all the indicators
							return true;  //show that this document matches all our checks, so we think it is a Project Gutenberg EText
					}
/*G***del; not needed
						//the end of the small print has an alternate rendering
					else if(indicatorIndex==2 && StringUtilities.indexOfIgnoreCase(text, "*WANT* TO SEND MONEY")>=0) //G***tidy algorithm; use a constant
					{
						++indicatorIndex; //go to the next indicators
Debug.trace("found *want* to send money");  //G***del
//G***del System.out.println("found indicator: "+indicatorIndex); //G***del
//G***del System.out.println("return true? "+(indicatorIndex>=indicatorStrings.length)); //G***del
						if(indicatorIndex>=indicatorStrings.length) //if we've matchd all the indicators
							return true;  //show that this document matches all our checks, so we think it is a Project Gutenberg EText
					}
*/
				}
			}
		}
/*G***del; this isn't worth the effort for now
//G***del System.out.println("we have found indicators: "+indicatorIndex);  //G***del
//G***del System.out.println("footer element: "+getPGFooterElement(document));  //G***del
			//if we didn't find the correct strings, see if we at least found some
			//  strings and if we found the footer
		if(indicatorIndex>0 && getPGFooterElement(document)!=null)
			return true; //we still think this is a Project Gutenberg file, even if everything didn't match
*/
		return false;  //show that we don't think this is a Project Gutenberg EText
/*G***del this attempt for the complete works of Shakespeare, etext98
			//these three pairs of strings are the the ones to search for, in the order of lines given
//G***del		final String[][] indicatorStrings={{PROJECT_GUTENB, ETEXT}, {SMALL_PRINT, SMALL_PRINT_START}, {SMALL_PRINT, SMALL_PRINT_END}};
//G***del		int indicatorIndex=0;
		boolean hasProjectGutenberg=false;  //we'll try to find each of these indicators
		boolean hasSmallPrintStart=false;
		boolean hasSmallPrintEnd=false;
		final Element bodyElement=XHTMLUtilities.getBodyElement(document);  //get the <body> element of the XHTML document
		if(bodyElement!=null) //if there is a body element
		{
			final NodeList childNodes=bodyElement.getChildNodes();  //get the list of body child nodes
			for(int i=0; i<childNodes.getLength(); ++i) //look at each child node
			{
				final Node childNode=childNodes.item(i);  //get this child node
				if(childNode.getNodeType()==Node.ELEMENT_NODE)  //if this is an element
				{
					final Element childElement=(Element)childNode;  //get a reference to this element
					final String text=XMLUtilities.getText(childElement, true); //get the text of the element
//G***del Debug.trace("looking at text: ", text); //G***del
Debug.trace("Checking first indicator: etext string: ", getETextString(text));
						//if the text contains both "Project Gutenberg" and "EText", "Edition", or "EBook"
					if(StringUtilities.indexOfIgnoreCase(text, PROJECT_GUTENB)>=0
							&& getETextString(text)!=null)
					{
Debug.trace("found project gutenberg string");
						hasProjectGutenberg=true; //show that we found "Project Gutenberg"
//G***del						++indicatorIndex; //go to the next indicators
//G***del System.out.println("found indicator: "+indicatorIndex); //G***del
					}
						//if this text contains "small print"
					else if(StringUtilities.indexOfIgnoreCase(text, SMALL_PRINT)>=0)
					{
							//if we haven't found the start, and the text also contains "start" or "***"
						if(!hasSmallPrintStart &&
								(StringUtilities.indexOfIgnoreCase(text, SMALL_PRINT_START)>=0
								|| text.indexOf("***")>=0))  //G***use a constant
						{
Debug.trace("found small print start");
							hasSmallPrintStart=true;  //show that we found the start of the small print
						}
							//if the text also contains "end" or "V." or "Ver." or "Version"
						else if(StringUtilities.indexOfIgnoreCase(text, SMALL_PRINT_END)>=0
								|| StringUtilities.indexOfIgnoreCase(text, "V.")>=0 //G***use constants
								|| StringUtilities.indexOfIgnoreCase(text, "Ver.")>=0
								|| StringUtilities.indexOfIgnoreCase(text, "Version")>=0)
						{
Debug.trace("found small print end");
						  hasSmallPrintEnd=true;  //show that we found the end of the small print

						}
					}
				}
					//if all three elements are present
				if(hasProjectGutenberg && hasSmallPrintStart && hasSmallPrintEnd)
					return true;  //this looks like a Project Gutenberg file
			}
		}
		return false;  //show that we don't think this is a Project Gutenberg EText
*/
	}

	/**Tidies a title of the etext.
		Parenthetical postfixes are removed, and the string is trimmed if
		whitespace and other characters.
	@param string The string to tidy.
	@return The string with extra characters removed.
	@see #tidyProperty
	*/
	public static String tidyTitle(final String string)  //G***eventually put in some common class
	{
//G***del Debug.trace("tidying title: "+string);
		final StringBuffer stringBuffer=new StringBuffer(string); //create a new string buffer with the string
		tidyProperty(stringBuffer); //do default tidying
			//if the string starts with "release of:" (but only in lowercase), remove it (e.g. duglas11.txt)
		if(StringBufferUtilities.startsWith(stringBuffer, "release of:")) //if the string starts with "'s " (but only in lowercase) G***use a constant
		{
			tidyProperty(stringBuffer.delete(0, "release of:".length())); //remove the beginning Project Gutenberg string and tidy the string G***use a constant
//G***del Debug.trace("current title being tidied: ", stringBuffer);  //G***del
		}
			//if the string contains with "title:" in any case (e.g. wacia10.txt)
		final int titlePropertyIndex=Strings.indexOfIgnoreCase(stringBuffer.toString(), "title:"); //G***use a constant G***use StringBufferUtiliites
		if(titlePropertyIndex>=0) //G***this should probably go in getProperty(), as that's where the value comes from
		{
			tidyProperty(stringBuffer.delete(titlePropertyIndex, stringBuffer.length())); //remove that text and tidy the string G***use a constant
		}

		  //see if "Project Gutenberg" or a variation is in the string
		final String projectGutenbergString=getProjectGutenbergString(stringBuffer.toString()); //G***do something more efficient than creating a string
			//get the index of the string if it exists
		final int projectGutenbergIndex=projectGutenbergString!=null ? Strings.indexOfIgnoreCase(stringBuffer.toString(), projectGutenbergString) : -1;  //G***do something more efficient than creating a string
			//if the string starts with a variant of "Project Gutenberg" or "The Project Gutenberg"
		if(projectGutenbergString!=null
			&& (StringBufferUtilities.startsWith(stringBuffer, projectGutenbergString)
				  || StringBufferUtilities.startsWith(stringBuffer, "The "+projectGutenbergString) //G***use a constant
				  || StringBufferUtilities.startsWith(stringBuffer, "This is the "+projectGutenbergString) //G***use a constant
					))
		{
//G***del Debug.trace("PG string: ", projectGutenbergString);  //G***del
//G***del Debug.trace("PG index: ", projectGutenbergIndex);  //G***del
//G***del Debug.trace("current title being tidied: ", stringBuffer);  //G***del
			tidyProperty(stringBuffer.delete(0, projectGutenbergIndex+projectGutenbergString.length())); //remove the beginning "'s " and tidy the string G***use a constant
//G***del Debug.trace("current title being tidied: ", stringBuffer);  //G***del
			final String eTextString=getETextString(stringBuffer.toString());  //get the etext string in the string
				//if the string starts with "'s " (but only in lowercase), remove it (e.g. "PG's...")
			if(StringBufferUtilities.startsWith(stringBuffer, "'s ")) //if the string starts with "'s " (but only in lowercase) G***use a constant
			{
				tidyProperty(stringBuffer.delete(0, "'s ".length())); //remove the beginning Project Gutenberg string and tidy the string G***use a constant
//G***del Debug.trace("current title being tidied: ", stringBuffer);  //G***del
			}
				//if the string starts with a variant of "EText"
			if(eTextString!=null && StringBufferUtilities.startsWith(stringBuffer, eTextString))
			{
				tidyProperty(stringBuffer.delete(0, eTextString.length())); //remove the beginning etext string and tidy the string
//G***del Debug.trace("current title being tidied: ", stringBuffer);  //G***del
			}
		}
//G***del Debug.trace("current title being tidied: ", stringBuffer);  //G***del
			//if the string starts with "s" (but only in lowercase), remove it (e.g. "Project Gutenberg ETexts from...")
		if(StringBufferUtilities.startsWith(stringBuffer, "s ")) //if the string starts with "s " (but only in lowercase) G***use a constant
		{
			tidyProperty(stringBuffer.delete(0, "s ".length())); //remove the beginning "s " and tidy the string G***use a constant
		}
			//if the string starts with "in" (but only in lowercase), remove it (e.g. "in French of...", 8plno07.txt)
		if(StringBufferUtilities.startsWith(stringBuffer, "in"))  //G***use a constant
		{
			if(stringBuffer.indexOf("French")>=0 //G***use a constant; do stricter order checking
				  || stringBuffer.indexOf("Spanish")>=0
				  || stringBuffer.indexOf("German")>=0
				  || stringBuffer.indexOf("Italian")>=0
				  || stringBuffer.indexOf("Latin")>=0)
			{
				final int ofIndex=stringBuffer.indexOf(OF);  //find out where "of" appears
				if(ofIndex>=0)  //if "of" is present, we think the string begins with something line "in French of"
				{
						//remove everything up to and including "OF" and retidy
					tidyProperty(stringBuffer.delete(0, ofIndex+OF.length()));
				}
			}
		}
			//if the string starts with "of" (but only in lowercase), remove it (e.g. "The Project Gutenberg ETExt of...")
		if(StringBufferUtilities.startsWith(stringBuffer, OF)) //if the string starts with "of" (but only in lowercase) G***use a constant
		{
			tidyProperty(stringBuffer.delete(0, OF.length())); //remove the beginning "of" and tidy the string G***use a constant
		}
			//if the string starts with "from" (but only in lowercase), remove it (e.g. "Project Gutenberg ETexts from...")
		if(StringBufferUtilities.startsWith(stringBuffer, "from")) //if the string starts with "from" (but only in lowercase) G***use a constant
		{
			tidyProperty(stringBuffer.delete(0, "from".length())); //remove the beginning "from" and tidy the string G***use a constant
		}
			//if the string starts with "the" (but only in lowercase), remove it
		if(StringBufferUtilities.startsWith(stringBuffer, THE)) //if the string starts with "the" (but only in lowercase)
		{
			tidyProperty(stringBuffer.delete(0, THE.length())); //remove the beginning "the" and tidy the string
		}
			//if the string ends with ", or" (e.g. 03tcb10.txt)
		if(CharSequenceUtilities.endsWithIgnoreCase(stringBuffer, ", or")) //if the string ends with ", or" G***use a constnat
		{
			tidyProperty(stringBuffer.delete(stringBuffer.length()-", or".length(), stringBuffer.length())); //remove the beginning ", or" and tidy the string G***use a constant
		}
			//if the string starts with "book", "etext", or something similar
		final String eTextString=getETextString(stringBuffer.toString()); //see if "book" or "etext" or something appears in the string
		if(eTextString!=null) //if there is an etext string in the title
		{
	//G***del System.out.println("found etext string: "+eTextString);
				//see where the etext string appeared
	//G***fix; the current way is a hack and very inefficient; add a StringBuffer.indexOf(StringBuffer, String)		  final int eTextStringIndex=StringBufferUtilities.indexOf(stringBuffer, eTextString);
			final int eTextStringIndex=stringBuffer.toString().indexOf(eTextString);
			if(eTextStringIndex==0) //if the string appeared at the beginning G***this can cause problems if the title is "Ebook 101" or something
			{
	//G***del System.out.println("started at beginning"); //G***del
				tidyProperty(stringBuffer.delete(0, eTextString.length())); //remove the beginning etext word and tidy the string
	//G***del System.out.println("now title is: "+stringBuffer); //G***del
					//if the string starts with "of" (but only in lowercase)
				if(StringBufferUtilities.startsWith(stringBuffer, OF)) //if the string starts with "of" (but only in lowercase)
				{
					tidyProperty(stringBuffer.delete(0, OF.length())); //remove the beginning "of" and tidy the string
				}
			}
		}
			//if the string contains with "author:" in any case (e.g. ffnt110.txt)
		final int authorPropertyIndex=Strings.indexOfIgnoreCase(stringBuffer.toString(), "author:"); //G***use a constant G***use StringBufferUtiliites
		if(authorPropertyIndex>=0)  //G***this should probably go in getProperty(), as that's where the value comes from
		{
			tidyProperty(stringBuffer.delete(authorPropertyIndex, stringBuffer.length())); //remove that text and tidy the string G***use a constant
		}
		final int byIndex=getByIndex(stringBuffer.toString());  //get the index of "by" G***if we didn't have to convert to a string, this would be more efficient
		if(byIndex>=0 && Character.isLowerCase(stringBuffer.charAt(byIndex)))  //if we found "by" (only in lowercase)
		{
		  tidyProperty(stringBuffer.delete(byIndex, stringBuffer.length()));  //remove "by" and everything after it G***add a convenience routine like the one for strings
		}
			//see if "copyright" appears in the string
		final int copyrightIndex=Strings.indexOfIgnoreCase(stringBuffer.toString(), COPYRIGHT);  //G***use a StringBufferUtilities method
		if(copyrightIndex>=0) //if "copyright" appears in the title
		{
				//if "copyright" is followed by a copyright character
			if(CharSequenceUtilities.charIndexOf(stringBuffer, "@"+COPYRIGHT_SIGN, copyrightIndex+1)>=0
				  || stringBuffer.indexOf("(c)", copyrightIndex+1)>=0
					|| stringBuffer.indexOf("(C)", copyrightIndex+1)>=0)
			{
			  tidyProperty(stringBuffer.delete(copyrightIndex, stringBuffer.length()));  //remove "copyright" and everything after it G***add a convenience routine like the one for strings
			}
		}
			//see if "(c)" appears in the string (e.g. truth10.txt)
		final int copyrightCIndex=Strings.indexOfIgnoreCase(stringBuffer.toString(), "(c)");  //G***use a StringBufferUtilities method; G***use a constant
		if(copyrightCIndex>=0) //if "(c)" appears in the title
		{
			tidyProperty(stringBuffer.delete(copyrightCIndex, stringBuffer.length()));  //remove "(c)" and everything after it G***add a convenience routine like the one for strings
		}
		tidyProperty(stringBuffer); //tidy the title once more (it might have a comma before "by", for example)
		return stringBuffer.toString(); //return the string we tidied
	}

	/**Gets the index of "by", making sure it's surrounded by whitespace.
	@param string The string to search.
	@return The index of the word "by", or -1 if the word is not found or is part
		of another word.
	*/
	protected static int getByIndex(final String string)
	{
		int startSearchIndex=0; //there may be multiple possible "by"s
		while(startSearchIndex<string.length()) //if we have something to search after
		{
				 //see if there is a "by" on the line
			final int byIndex=Strings.indexOfIgnoreCase(string, BY, startSearchIndex);
			if(byIndex>=0)  //if by appears
			{
				 //if "by" is surrounded by whitespace or asterisks G***fix; right now, this removes parts of titles if those titles have "by" in them
				if((byIndex==0  //if "by" is at the first of the string
					  || Character.isWhitespace(string.charAt(byIndex-1)) //or is after whitespace
					  || string.charAt(byIndex-1)=='*')  //or is after an asterisk G***use a constant

						&& (byIndex+BY.length()==string.length()
							  || (byIndex+BY.length()<string.length()
								&& Character.isWhitespace(string.charAt(byIndex+BY.length()))))) //if there is whitespace after "by"
				{
					return byIndex; //return this index
				}
				startSearchIndex=byIndex+BY.length(); //start searching after the last occurrence of "by"
			}
			else  //if by doesn't appear
				break;  //stop looking
		}
		return -1;  //show that we couldn't find "by"
	}

	/**Tidies an author of the etext.
		All punctuation is removed.
	@param string The string to tidy.
	@return The string with extra characters removed.
	@see #tidyTitle
	*/
	public static String tidyAuthor(final String string)  //G***eventually put in some common class
	{
Debug.trace("tidying author: ", string); //G***del
		final StringBuffer stringBuffer=new StringBuffer(string); //create a new string buffer with the string
		tidyProperty(stringBuffer); //do default tidying
			//trim the string of all punctuation
//G***del		StringBufferUtilities.trim(stringBuffer, PUNCTUATION_CHARS+TRIM_CHARS);
			//trim the string of all punctuation (except quotes and group punctuation) and whitespace
		StringBufferUtilities.trim(stringBuffer, PHRASE_PUNCTUATION_CHARS+HYPHEN_MINUS_CHAR+EM_DASH_CHAR+EN_DASH_CHAR+TRIM_CHARS);
			//if the string starts with "the" (but only in lowercase), remove it
		if(StringBufferUtilities.startsWith(stringBuffer, THE)) //if the string starts with "the" (but only in lowercase)
		{
			tidyProperty(stringBuffer.delete(0, THE.length())); //remove the beginning "the" and tidy the string
		}
			//if the string contains with "author:" in any case (e.g. idiot10.txt)
		final int authorPropertyIndex=Strings.indexOfIgnoreCase(stringBuffer.toString(), "author:"); //G***use a constant G***use StringBufferUtiliites
		if(authorPropertyIndex>=0)  //G***this should probably go in getProperty(), as that's where the value comes from
		{
			tidyProperty(stringBuffer.delete(authorPropertyIndex, stringBuffer.length())); //remove that text and tidy the string G***use a constant
		}
			//if the string ends with "all rights reserved" in any case G***use a constant
		if(CharSequenceUtilities.endsWithIgnoreCase(stringBuffer, "all rights reserved"))
		{
			tidyProperty(stringBuffer.delete(stringBuffer.length()-"all rights reserved".length(), stringBuffer.length())); //remove that text and tidy the string G***use a constant
		}
Debug.trace("checking for copyright: ", stringBuffer);  //G***de
		  //see if "copyright 19" or "copyright 20" or "copyrihgt (", etc. appears in the string (e.g. efpap10.txt)
		int copyrightIndex=Strings.indexOfIgnoreCase(stringBuffer.toString(), COPYRIGHT); //see if "copyright" appears in the string G***change to StringBufferUtilities
		if(copyrightIndex>=0 &&
			(
		CharSequenceUtilities.charIndexOf(stringBuffer, "@"+COPYRIGHT_SIGN)>=0 //if "copyright" is followed by a copyright sign
			  || stringBuffer.indexOf("(c)")>=0 //if "copyright" is followed by (c) G***use a constant
			  || stringBuffer.indexOf("19")>=0 //if "copyright" is followed by 19 G***use a constant
			  || stringBuffer.indexOf("20")>=0 //if "copyright" is followed by 19 G***use a constant
			))
		{
			tidyProperty(stringBuffer.delete(copyrightIndex, stringBuffer.length())); //remove that text and tidy the string
		}
			//if the author contains ", this is..." (e.g. email025.txt) G***use a constant
		final int thisIsIndex=stringBuffer.indexOf(", this is");
		if(thisIsIndex>=0)
		{
			tidyProperty(stringBuffer.delete(thisIsIndex, stringBuffer.length())); //remove that text and tidy the string G***use a constant
		}
		final int byIndex=getByIndex(stringBuffer.toString());  //get the index of "by" G***if we didn't have to convert to a string, this would be more efficient
		if(byIndex>=0 && Character.isLowerCase(stringBuffer.charAt(byIndex)))  //if we found "by" (only in lowercase)
		{
				//see if we can find punctuation before "by"
			final int punctuationIndex=CharSequenceUtilities.charLastIndexOf(stringBuffer, PUNCTUATION_CHARS, byIndex-1);
				//if by is not at the first of the string and has punctuation before it
//G***del			if(byIndex>0 && StringBufferUtilities.notCharIndexOf(stringBuffer, WHITESPACE_CHARS)<byIndex
			if(punctuationIndex>=0) //if we can find punctuation before "by" (e.g. efpap10.txt)
			{
			  tidyProperty(stringBuffer.delete(punctuationIndex, stringBuffer.length()));  //remove the punctuation and everything else, because we assume it's talking about somebody else
			}
			else //if "by" is at the first of the string, assume it's talking about the author
			{
			  tidyProperty(stringBuffer.delete(0, byIndex+BY.length()));  //remove everything up to and including "by" G***add a convenience routine like the one for strings
			}
		}
Debug.trace("checking for punctuation: ", stringBuffer);  //G***de
			//if this string doesn't have group punctuation as its first character
		if(stringBuffer.length()>0 && LEFT_GROUP_PUNCTUATION_CHARS.indexOf(stringBuffer.charAt(0))<0)
		  StringBufferUtilities.trimEnd(stringBuffer, RIGHT_GROUP_PUNCTUATION_CHARS); //trim any right group punctuation that's been left on the author for some reason (e.g. huxbr10.txt)
		return stringBuffer.toString(); //return the string we tidied
	}

	/**Tidies a property, such as the title or the author, of the etext.
		Parenthetical postfixes are removed, and the string is trimmed if
		whitespace and other characters.
	@param string The string to tidy.
	@return The string with extra characters removed.
	 */
	public static String tidyProperty(final String string)  //G***eventually put in some common class
	{
		return tidyProperty(new StringBuffer(string)).toString(); //tidy the string from a string buffer
	}

	/**Tidies a property, such as the title or the author, of the etext.
		Parenthetical postfixes are removed, and the string is trimmed if
		whitespace and other characters.
	@param stringBuffer The characters to tidy.
	@return The string buffer with extra characters removed.
	 */
	protected static StringBuffer tidyProperty(final StringBuffer stringBuffer)  //G***eventually put in some common class
	{
			//see if "contents" is part of the property (e.g. tbroa10.txt)
		final int contentsIndex=Strings.indexOfIgnoreCase(stringBuffer.toString(), "contents");  //G***use a constant; use StringBufferUtilities
//G***del Debug.trace("tidying: ", stringBuffer); //G***del
//G***del Debug.trace("contents index: ", contentsIndex); //G***del
		if(contentsIndex>0 && EOL_CHARS.indexOf(stringBuffer.charAt(contentsIndex-1))>=0) //if contents appears after a linebreak
			stringBuffer.delete(contentsIndex, stringBuffer.length());  //remove that line break and everything after
		  //some works (e.g. valen10.txt) for some reason end in "^M"
		if(stringBuffer.length()>1 && stringBuffer.charAt(stringBuffer.length()-2)=='^')
		{
		  stringBuffer.delete(stringBuffer.length()-2, stringBuffer.length());  //remove the ending control character
		}
		/**The characters we'll trim from the front and back of the string.*/
		final String TRIM_CHARS=CharacterConstants.TRIM_CHARS+WHITESPACE_CHARS+DEPENDENT_PUNCTUATION_CHARS+/*G***bring back if needed QUOTE_CHARS+*/'*'+'.';  //G**use constants
//G***del System.out.println("tidying: "+string); //G***del
		  //trim the string of whitespace, dashes, and asterisks
		StringBufferUtilities.trim(stringBuffer, TRIM_CHARS);
			//see if there is any group punctuation at the end (e.g. "XXX (XXX)")
		final int rightGroupIndex=CharSequenceUtilities.charLastIndexOf(stringBuffer, RIGHT_GROUP_PUNCTUATION_CHARS);
		if(rightGroupIndex>=0)  //if the string ends with a right group characters
		{
//G***del System.out.println("found right group: "+rightGroupIndex); //G***del
		    //if there's nothing but whitespace after the right group, we'll remove the entire group
			if(StringBufferUtilities.notCharIndexOf(stringBuffer, CharacterConstants.TRIM_CHARS, rightGroupIndex+1)<0)
			{
//G***del System.out.println("nothing but whitespace after"); //G***del
					//remove evertying from the start of the group onward
					//see if there is any left punctuation at the end (e.g. "XXX (XXX)") (don't just grab the first left group punctuation, because there could be several sets of them)
				final int leftGroupIndex=CharSequenceUtilities.charLastIndexOf(stringBuffer, LEFT_GROUP_PUNCTUATION_CHARS, rightGroupIndex-1);
				if(leftGroupIndex>=0) //if we found a matching left group punctuation character
				{
					stringBuffer.delete(leftGroupIndex, stringBuffer.length()); //remove the left punctuation and everything after it
				}
//G**del; old, incorrect way				StringBufferUtilities.removeFirstCharLength(stringBuffer, LEFT_GROUP_PUNCTUATION_CHARS);
			}
		}
		  //remove any parenthesis or brackets after the string
//G***del		StringBufferUtilities.removeFirstCharLength(stringBuffer, GROUP_PUNCTUATION_CHARS);
		  //trim the string of whitespace, dashes, and asterisks again
		StringBufferUtilities.trim(stringBuffer, TRIM_CHARS);
//G***del		StringBufferUtilities.trim(stringBuffer, WHITESPACE_CHARS+HYPHEN_MINUS_CHAR+EM_DASH_CHAR+EN_DASH_CHAR+'*'+'.'+','); //G***use a constant
		  //collapse all whitespace into spaces
		StringBufferUtilities.collapse(stringBuffer, WHITESPACE_CHARS, " ");
		return stringBuffer; //return the string we tidied
	}

	/**Determines if the given lines is the Project Gutenberg line indicating the
		preferred filename. This is determined by whether the line starts and ends
		in asterisks and contains the word "file", or contains the words ".txt", or
		".zip".
	@param line The line of text that may be a file line.
	@return <code>true</code> if the string is the filename indicator line.
	 */
	protected static boolean isFileLine(final String line)
	{
		final String trimmedLine=line.trim(); //trim the line
		return (trimmedLine.startsWith("*")  //see if the line begins and ends with asterisks and contains the string "file"
			  && trimmedLine.endsWith("*")
				&& Strings.indexOfIgnoreCase(trimmedLine, "file")>=0) //G***use a constant
					|| Strings.indexOfIgnoreCase(trimmedLine, ".txt")>=0 //G***use a constant
					|| Strings.indexOfIgnoreCase(trimmedLine, ".zip")>=0; //G***use a constant
	}

	/**Searches for "Project Gutenberg" or "PG" in the current string, and
		returns the match. (For "PG's", see 4saht10.txt.)
	@param text The text to search.
	@return The matched string, or <code>null</code> if there was no match.
	 */
	protected static String getProjectGutenbergString(final String text)
	{
		final String[] pgStrings={"PG", "Project Gutenburg", "Project Gutenberg"}; //any of these strings will work G***use constants
		for(int i=pgStrings.length-1; i>=0; --i) //look at each of the etext strings
		{
			final String pgString=pgStrings[i];  //get this string
//G***del System.out.println("Checking etext string: "+eTextString);  //G***del
				//see if the string is in the text
			final int pgStringIndex=Strings.indexOfIgnoreCase(text, pgString);
			if(pgStringIndex>=0)  //if the string is in the text
			{
					//return the real string we found
				return text.substring(pgStringIndex, pgStringIndex+pgString.length());
			}
		}
		return null;  //show that we didn't find a Project Gutenberg string
	}

	/**Searches for "EText", "EBook", or "Edition" in the current string, and
		returns the match. This even accepts "Gutenberg's", as in, "Project
		Gutenberg's United States Congress Address Book". The string is only
		accepted if it is followed by whitespace or dependent punctuation,
		or appears at the end of the string.
	@param text The text to search.
	@return The matched string, or <code>null</code> if there was no match.
	 */
	protected static String getETextString(final String text)
	{
		final String[] eTextStrings={"Gutenberg's", BOOK, EDITION, EBOOK, ETEXT}; //any of these eText strings will work G***test "Gutenberg's", use constant
		for(int i=eTextStrings.length-1; i>=0; --i) //look at each of the etext strings
		{
			final String eTextString=eTextStrings[i];  //get this etext string
//G***del System.out.println("Checking etext string: "+eTextString);  //G***del
				//see if the etext string is in the text
			final int eTextStringIndex=Strings.indexOfIgnoreCase(text, eTextString);
			if(eTextStringIndex>=0)  //if the etext string is in the text
			{
				if(eTextStringIndex+eTextString.length()==text.length() //make sure the string either comes at the end of the text
							//or the string comes before whitespace
					|| Character.isWhitespace(text.charAt(eTextStringIndex+eTextString.length()))
						  //or if the string comes before dependent punctuation
					|| DEPENDENT_PUNCTUATION_CHARS.indexOf(text.charAt(eTextStringIndex+eTextString.length()))>=0)
				{
						//return the real string we found
					return text.substring(eTextStringIndex, eTextStringIndex+eTextString.length());
				}
			}
		}
		return null;  //show that we didn't find an etext string
	}

	/**Extracts a Project Gutenberg identifier from the given filename.
		<p>Project Gutenberg files usually end in a one or two digit version number,
		optionally followed by a source letter (e.g. test12a). This method removes
		the file extension and removes the version number, returning the surrounding
		characters. If characters appeared after the version number, a hyphen ('-')
		will be inserted (e.g. test-a).</p>
	@param filename The string identifying the original file.
	@return The identifier of this Project Gutenberg work.
	*/
	public static String getID(final String filename)
	{
			//get the name of the file and remove its extension
		final StringBuffer stringBuffer=new StringBuffer(Files.removeExtension(filename));
		int versionEndIndex=stringBuffer.length();  //start assuming the version is at the end of the name
		  //find the first digit on the right
		while(versionEndIndex>0)  //while we haven't ran out of characters
		{
		  if(Character.isDigit(stringBuffer.charAt(versionEndIndex-1)))  //if the previous character is a digit
			{
				break;  //we've found the end of the version
			}
			else  //if the character before this position isn't a digit
			{
				--versionEndIndex;  //look at the character before this
			}
		}
			//remove two digits in a row
		if(versionEndIndex>0) //if we found a digit
		{
		  int versionBeginIndex=versionEndIndex-1;  //we'll get at least one digit---maybe two
				//if the previous character is a digit as well
			if(versionBeginIndex>0 && Character.isDigit(stringBuffer.charAt(versionBeginIndex-1)))
		  {
				--versionBeginIndex;  //use two digits
		  }
			if(versionEndIndex<stringBuffer.length()) //if there are characters after the digits we're going to delete
			{
				stringBuffer.insert(versionEndIndex, '-');  //insert a hyphen to separate the name from the other characters
			}
			stringBuffer.delete(versionBeginIndex, versionEndIndex);  //remove the version from the string
		}
		return stringBuffer.toString(); //return the ID we constructed
	}

}