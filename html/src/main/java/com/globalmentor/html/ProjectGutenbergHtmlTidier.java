/*
 * Copyright © 1996-2011 GlobalMentor, Inc. <http://www.globalmentor.com/>
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

package com.globalmentor.html;

import java.util.*;

import com.globalmentor.io.*;
import com.globalmentor.java.*;
import com.globalmentor.xml.XmlDom;

import static com.globalmentor.html.HtmlDom.*;
import static com.globalmentor.java.Characters.*;

import org.w3c.dom.*;

/**
 * Tidies an XHTML document representing a Project Gutenberg etext.
 * <p>
 * The header is considered to end on the line containing "*END*".
 * </p>
 * <p>
 * For extracting title and author information, the following header paragraphs inside the header are supported:
 * </p>
 * <dl>
 * <dt>bma10.txt</dt>
 * <dd>Project Gutenberg Etext of Bulfinch's Mythology, The Age of Fable<br>
 * #1 in our series by Thomas Bulfinch</dd>
 * <dt>0ws3510.txt</dt>
 * <dd>The Project Gutenberg's Etext of Shakespeare's First Folio***<br>
 * The Tragedie of Anthonie, and Cleopatra*************</dd>
 * <dt>vngln10i.txt</dt>
 * <dd>The Project Gutenberg Etext of Evangeline, by Henry W. Longfellow<br>
 * #6 in our series by Henry W. Longfellow</dd>
 * <dt>siddh02.txt</dt>
 * <dd>The Project Gutenberg Etext of "Siddhartha," by Hermann Hesse<br>
 * #2 in our series by Hesse</dd>
 * <dt>sawyr10.txt</dt>
 * <dd>The Project Gutenberg Etext of Tom Sawyer, by Twain/Clemens**<br>
 * This file should be named sawyr10.txt or sawyr10.zip*****</dd>
 * <dt>poe3v11.txt</dt>
 * <dd>The Project Gutenberg Etext of The Works of Edgar Allan Poe V. 3<br>
 * Volume 3 of the Raven Edition<br>
 * #8 in our series by Edgar Allan Poe</dd>
 * <dt>ozland10.txt</dt>
 * <dd>The Project Gutenberg Etext of The Marvelous Land of Oz*<br>
 * This file should be named ozland10.txt or ozland10.zip**</dd>
 * <dt>8cncd10.txt</dt>
 * <dd>The Project Gutenberg Etext of A Week on the Concord and Merrimack Rivers<br>
 * by Henry David Thoreau</dd>
 * <dt>ynkgp10.txt</dt>
 * <dd>The Project Gutenberg Etext of Yankee Gypsies, by Whittier***<br>
 * #1 in our series by John Greenleaf Whittier</dd>
 * <dt>wman11.txt</dt>
 * <dd>The Project Gutenberg Edition of "What Is Man?" by Mark Twain*<br>
 * #1 in our series by Mark Twain [Samuel Langhorne Clemens]</dd>
 * <dt>phant12.zip</dt>
 * <dd>The Project Gutenberg EBook of The Phantom of the Opera, by Gaston Leroux<br>
 * #1 in our series by Gaston Leroux</dd>
 * <dt>uscon95b.txt</dt>
 * <dd>Project Gutenberg's United States Congress Address Book****</dd>
 * <dt>ftroy10.txt</dt>
 * <dd>Project Gutenberg Etext; The Fall of Troy, by Quintus Smyrnaeus</dd>
 * 
 * </dl>
 * @author Garret Wilson
 */
public class ProjectGutenbergHtmlTidier { //TODO move to different package

	/**
	 * The first part of the words "Project Gutenberg". Some works such as optns10.txt misspell this as "Project Gutenburg".
	 */
	public static final String PROJECT_GUTENB = "Project Gutenb";

	/** The word "copyright". */
	public static final String COPYRIGHT = "copyright";

	/** The word "Edition". */
	public static final String EDITION = "Edition";

	/** The word "EText". */
	public static final String ETEXT = "Etext";

	/** The word "EBook". */
	public static final String EBOOK = "EBook";

	/** The word "the". */
	public static final String THE = "the";

	/** The word "book". */
	public static final String BOOK = "book";

	/** The word "is". */
	public static final String IS = "is";

	/** The word "of". */
	public static final String OF = "of";

	/** The word "by" to indicate an author. */
	public static final String BY = "by";

	/** A substring indicating Project Gutenberg small print. */
	public static final String SMALL_PRINT = "SMALL PRINT";

	/** A substring marking the start of the Project Gutenberg small print. */
	public static final String SMALL_PRINT_START = "START";

	/** A substring marking the end of the Project Gutenberg small print. */
	public static final String SMALL_PRINT_END = "END";

	/**
	 * Tidies an XHTML document representing a Project Gutenberg etext.
	 * @param document The XHTML document containing the Project Gutenberg text.
	 */
	/*TODO fix
		public static void tidy(final Document document)
		{
			final Element bodyElement=XHTMLUtilities.getBodyElement();  //get the
	//TODO fix XMLUtilities.extractChildren()
	
		}
	*/

	/**
	 * Removes the Project Gutenberg header. In most cases, the header and the small print will be combined before the start of the text. If the header and the
	 * small print are separated by the content, both will be extracted, concatenated, and returned.
	 * @param document The XHTML document containing the Project Gutenberg text.
	 * @return A document fragment that contains the text of the header that was removed from the document, or <code>null</code> if a header could not be found.
	 */
	public static DocumentFragment extractHeader(final Document document) {
		final Element bodyElement = findHtmlBodyElement(document).orElseThrow(() -> new IllegalArgumentException("Missing <body> element.")); //get the <body> element of the XHTML document
		int divIndex = -1; //we'll check for dividers, just in case we can't find the header
		int sendMoneyIndex = -1; //we'll find the "send money" index in case we need it to be a divider
		int smallPrintStartIndex = -1; //we'll check for the beginning of the small print, just in case we can't find the header
		final NodeList childNodes = bodyElement.getChildNodes(); //get the list of body child nodes
		for(int i = 0; i < childNodes.getLength(); ++i) { //look at each child node
			final Node childNode = childNodes.item(i); //get this child node
			if(childNode.getNodeType() == Node.ELEMENT_NODE) { //if this is an element
				final Element childElement = (Element)childNode; //get a reference to this element
				final String text = XmlDom.getText(childElement, true).trim(); //get the text of the element
				if(text.equals("***") && divIndex < 0) { //if this is a divider and it's the first division we've found TODO use a constant
					divIndex = i; //save the index of the divider in case we need it
				}
				//sometimes (e.g. lied210.txt) the end of the small print is missing, but this string gets us pretty close
				else if(Strings.indexOfIgnoreCase(text, "*WANT* TO SEND MONEY") >= 0) { //TODO use a constant
					sendMoneyIndex = i; //this is the best divider we can find without actually finding the end of the small print
				}
				//if we find that the text is starting, we'll count that as a divider if we haven't yet found one (e.g. "crowd11a.txt)
				else if(Strings.indexOfIgnoreCase(text, "Start of the Project Gutenberg") >= 0 && divIndex < 0) { //TODO use a constant
					divIndex = i; //save the index of the divider in case we need it
				}
				//if this is the start of the PG header, and it's the first start we've found
				else if(smallPrintStartIndex < 0 && Strings.indexOfIgnoreCase(text, SMALL_PRINT) >= 0 && Strings.indexOfIgnoreCase(text, SMALL_PRINT_START) >= 0) {
					smallPrintStartIndex = i; //make a note of the start of the small print
				}
				//if this is end of the the Project Gutenberg footer and we haven't found the start of the small print
				else if(isPGFooterEnd(text) && smallPrintStartIndex < 0) {
					smallPrintStartIndex = i + 1; //we'll pretend the small print starts just after the end of the footer
				} else if(isPGHeaderEnd(text) || i > childNodes.getLength() - 4) { //if this is the end of the PG header, or if we're out of elements (we'll just have to guess that the last element is close to the end, and that subsequent ending text nodes have been combined)
					if(divIndex < 0 || sendMoneyIndex < divIndex + 50) //if we didn't find a divider, or the divider is close to the send money index TODO use a constant
						divIndex = sendMoneyIndex; //use the send money index as the divider
					if(i < childNodes.getLength() - 5) //if the end of the header is not too close to the end of the document
						return XmlDom.extractChildren(bodyElement, 0, i + 1); //extract all the header elements, including this one
					else if(divIndex >= 0) { //if the header takes up the whole document, but we've found a divider before
						//if the small print starts before the divider, just after the
						//  divider, or if we didn't find the start of the small print
						if(smallPrintStartIndex < divIndex + 10) {
							return XmlDom.extractChildren(bodyElement, 0, divIndex + 1); //extract all the header elements up to the divider
						} else
						//if the start of the small print comes significantly after the divider,
						//  we'll assume there's content between the header and the small print
						//  ---but make sure that content includes the middle of the document
						//TODO fix for ffn110.txt						else if(divIndex<childNodes.getLength()/2 && smallPrintStartIndex>childNodes.getLength()/2)
						{
							//get the first part of the document, up to the divider
							final DocumentFragment headerFragment = XmlDom.extractChildren(bodyElement, 0, divIndex + 1);
							//extract the small print, which comes at the end (compensating for removing the header)
							final DocumentFragment smallPrintFragment = XmlDom.extractChildren(bodyElement, smallPrintStartIndex - (divIndex + 1), i - divIndex);
							headerFragment.appendChild(smallPrintFragment); //combine the two fragments
							return headerFragment; //return the combined header fragment
						}
					}
				}
				/*TODO del for now; this is too complicated
									//if we find the title on a line that's not the header line
								if(title!=null && StringUtilities.indexOfIgnoreCase(text, title)>=0
									  && !isHeaderElement(childElement))
								{
									titleIndexIndex=i; //save the index of the title in case we need it
								}
				*/
			}
		}
		return null; //show that we couldn't find a PG header
	}

	/**
	 * Determines if the given text is the end of the Projet Gutenberg header.
	 * @param text The text to check for being the end of the header.
	 * @return <code>true</code> if the given text is the end of the Project Gutenberg header.
	 */
	protected static boolean isPGHeaderEnd(final String text) {
		//we'll look at each line separately; some works (such as orrbr10.txt
		//  which uses single spacing for the header but double spacing for the
		//  rest), might result in multiple lines in one paragraph, with multiple
		//  occurences of "end"
		final StringTokenizer lineTokenizer = new StringTokenizer(text, EOL_CHARACTERS.toString()); //tokenize the lines of the string
		while(lineTokenizer.hasMoreTokens()) { //while there are more lines
			final String line = lineTokenizer.nextToken(); //get the next line
			final int smallPrintIndex = Strings.indexOfIgnoreCase(line, SMALL_PRINT); //get the index of "small print"
			final int smallPrintEndIndex = Strings.indexOfIgnoreCase(line, SMALL_PRINT_END); //get the index of "end"
			if(smallPrintIndex >= 0 && smallPrintEndIndex >= 0) { //if both "small print" and "end" are on the same line
				/*TODO del; improved
							final int startIndex=Math.min(smallPrintIndex, smallPrintEndIndex); //see which phrase comes first
							final int endIndex=Math.max(smallPrintIndex, smallPrintEndIndex); //see which phrase comes last
								//see if there's a line break after the first phrase
							final int eolIndex=StringUtilities.charIndexOf(line, EOL_CHARS, startIndex);
							if(eolIndex<0 || eolIndex>endIndex) //if there are no line breaks between the phrases
				*/
				return true; //this is probably a Project Gutenber header end
			}
		}
		/*TODO fix
					//some books (e.g. "crowd11a.txt") don't have a header "END", but simply say the book istarting
				if(StringUtilities.indexOfIgnoreCase(text, "START OF THE PROJECT GUTENBERG EBOOK")>=0) //if the book is starting
					return true;  //the start of the book is the end of the header
		*/
		return false; //this text didn't pass our test
	}

	/**
	 * Removes the Project Gutenberg footer.
	 * @param document The XHTML document containing the Project Gutenberg text.
	 * @return A document fragment that contains the text of the footer that was removed from the document, or <code>null</code> if a footer could not be found.
	 */
	public static DocumentFragment extractFooter(final Document document) {
		final Element bodyElement = findHtmlBodyElement(document).orElseThrow(() -> new IllegalArgumentException("Missing <body> element.")); //get the <body> element of the XHTML document
		final NodeList childNodes = bodyElement.getChildNodes(); //get the list of body child nodes
		for(int i = childNodes.getLength() - 1; i >= 0; --i) { //look at each child node, starting at the end
			final Node childNode = childNodes.item(i); //get this child node
			if(childNode.getNodeType() == Node.ELEMENT_NODE) { //if this is an element
				final Element childElement = (Element)childNode; //get a reference to this element
				final String text = XmlDom.getText(childElement, true); //get the text of the element
				if(isPGFooterEnd(text)) //if this is the end of the Project Gutenberg footer
					return XmlDom.extractChildren(bodyElement, i, childNodes.getLength()); //extract all the footer elements from here on
			}
		}
		return null; //show that we couldn't find a PG footer
	}

	/**
	 * Finds the Project Gutenberg footer element.
	 * @param document The XHTML document containing the Project Gutenberg text.
	 * @return The element containing the Project Gutenberg footer information, or <code>null</code> if a footer could not be found.
	 */
	public static Element getPGFooterElement(final Document document) {
		final Element bodyElement = findHtmlBodyElement(document).orElseThrow(() -> new IllegalArgumentException("Missing <body> element.")); //get the <body> element of the XHTML document
		final NodeList childNodes = bodyElement.getChildNodes(); //get the list of body child nodes
		for(int i = childNodes.getLength() - 1; i >= 0; --i) { //look at each child node, starting at the end
			final Node childNode = childNodes.item(i); //get this child node
			if(childNode.getNodeType() == Node.ELEMENT_NODE) { //if this is an element
				final Element childElement = (Element)childNode; //get a reference to this element
				final String text = XmlDom.getText(childElement, true); //get the text of the element
				if(isPGFooterEnd(text)) { //if this is the end of the Project Gutenberg footer
					return childElement; //return this footer element
				}
			}
		}
		return null; //show that we couldn't find a PG footer
	}

	/**
	 * Determines if the given text is the end of the Projet Gutenberg footer.
	 * @param text The text to check for being the end of the footer.
	 * @return <code>true</code> if the given text is the end of the Project Gutenberg footer.
	 */
	protected static boolean isPGFooterEnd(final String text) {
		boolean foundEnd = false; //this will keep track of whether we've found a line beginning with "end"
		boolean foundPG = false; //this will keep track of whether we've found a line containing "Project Gutenberg"
		final StringTokenizer lineTokenizer = new StringTokenizer(text, EOL_CHARACTERS.toString()); //tokenize the lines of the string
		while(lineTokenizer.hasMoreTokens()) { //while there are more lines
			//get the next line and trim it of whitespace and asterisks
			final String line = Strings.trim(lineTokenizer.nextToken(), TRIM_CHARACTERS.add('*')); //TODO use a constant
			if(Strings.startsWithIgnoreCase(line, "end")) //if the line starts with "end" TODO use a constant
				foundEnd = true; //show that we found the "end" line
			if(Strings.indexOfIgnoreCase(text, PROJECT_GUTENB) >= 0) //if this line contains "Project Gutenberg" in it
				foundPG = true; //show that we found the Project Gutenberg line
			if(foundEnd && foundPG) //if we've found both cases in this paragraph
				return true; //we think this is a Project Gutenberg footer
		}
		return false; //we don't think this is the Project Gutenberg footer
	}

	/**
	 * Determines the title of the Project Gutenberg etext from the header. The "Title: " property is first located, but ignored if its value is "title". If
	 * unsuccessful, the information is extracted from the header.
	 * @param headerFragment A document fragment containing a Project Gutenberg header.
	 * @return The title of the work, or <code>null</code> if the name could not be determined.
	 */
	public static String getTitle(final DocumentFragment headerFragment) {
		Element nextLineTitleAfterElement = null; //in some special cases, we'll know when the next line will be the title
		String title = getPropertyValue(headerFragment, "title", ':'); //try to get the title property in the header TODO use a constant here
		if(title == null) { //if we couldn't find the title property
			boolean foundPGTitle = false; //show that we haven't found a title on the "Project Gutenberg" line
			final NodeList childNodes = headerFragment.getChildNodes(); //get the list of child nodes
			for(int childNodeIndex = 0; childNodeIndex < childNodes.getLength(); ++childNodeIndex) { //look at each child node
				final Node childNode = childNodes.item(childNodeIndex); //get this child node
				if(childNode.getNodeType() == Node.ELEMENT_NODE) { //if this is an element
					final Element childElement = (Element)childNode; //get a reference to this element
					final String text = XmlDom.getText(childElement, true); //get the text of the header
					if(isPGHeaderElement(childElement)) { //if this is a header element
						final StringTokenizer lineTokenizer = new StringTokenizer(text, EOL_CHARACTERS.toString()); //create a tokenizer to look at each line
						while(lineTokenizer.hasMoreTokens()) { //while there are more lines
							final String line = lineTokenizer.nextToken(); //get the next line
							//check for both "Project Gutenberg" and "EText", because some works
							//  contain "Project Gutenberg EText" and some contain "Project
							//  Gutenberg's EText"; some works (such as etarn10.txt) have a line
							//  such as "This is a COPYRIGHTED Project Gutenberg Etext,
							//  Details Below", which is *not* a header element, although some
							//  (such as clstn10.txt) have two lines, one with "copyright", the
							//  other without
							final String projectGutenbergString = getProjectGutenbergString(line);
							//get the index of the string if it exists
							final int projectGutenbergIndex = projectGutenbergString != null ? Strings.indexOfIgnoreCase(line, projectGutenbergString) : -1;
							//see if the word "copyright" is on the line, which means this is a false header
							int copyrightIndex = Strings.indexOfIgnoreCase(line, COPYRIGHT);
							if(Strings.indexOfIgnoreCase(line, "of:") >= 0 //if the line contains "of:" TODO last-minute hack for radio10.txt
									|| Strings.indexOfIgnoreCase(line, "of the") >= 0) //if the line contains "of the" TODO last-minute hack for berne10.txt
								copyrightIndex = -1; //TODO testing
							//see if "Project Gutenberg EText" or "Project Gutenberg Edition" is on this line
							if(projectGutenbergIndex >= 0 && copyrightIndex < 0) { //if "Project Gutenberg" is on this line, *without* a copyright indication
								final char originalFirstChar = line.charAt(0); //get the original first character in case we need it
								final char originalLastChar = line.charAt(line.length() - 1); //get the original last character in case we need it
								String eTextString = getETextString(line); //get the etext string on the line
								//get the index of the etext string, if there was one
								//  (if there wasn't one, the only way isPGHeaderElement()
								//  would have reported true is if this is the first element,
								//  so that's OK)
								int eTextStringIndex;
								boolean isSpecialFirstPGHeader = false; //we'll determine if this is the special beginning PG header; if so, we won't need to check about the etext string
								if(eTextString != null) { // we have an etext string
									eTextStringIndex = Strings.indexOfIgnoreCase(line, eTextString); //find out where the string starts
								} else { //if we didn't find "etext"
									//skip "Project Gutenberg"
									eTextStringIndex = projectGutenbergIndex + projectGutenbergString.length() + 1;
									if(eTextStringIndex < line.length()) { //if there are characters after "Project Gutenberg"
										eTextString = ""; //assign a fake etext string, so that our calculations below will work
										isSpecialFirstPGHeader = true; //show that we're assuming that this is the special starting Project Gutenberg header
									} else { //if we run out of characters
										eTextStringIndex = -1; //we can't use this line
									}
								}
								//get the first non-whitespace character
								final char firstChar = line.charAt(CharSequences.notCharIndexOf(line, TRIM_CHARACTERS));
								//if this is the correct line, and there's whitespace or dependent punctuation after the etext string
								if(eTextStringIndex >= 0 && eTextStringIndex + eTextString.length() < line.length() && (firstChar == '*' //TODO test with original problem etext, use constant
										|| isSpecialFirstPGHeader //we don't need to check for etext for the special header that has no etext
										|| Characters.isWhitespace(line.charAt(eTextStringIndex + eTextString.length()))
										|| DEPENDENT_PUNCTUATION_CHARACTERS.contains(line.charAt(eTextStringIndex + eTextString.length())))) {
									//remove the header string and everything before it, and tidy the title
									String remainingText = tidyTitle(line.substring(eTextStringIndex + eTextString.length()));
									final boolean hasOf; //we'll see if this heading has "of", which gives it more weight
									//if the string starts with "of" (but only in lowercase), remove the "of"
									if(remainingText.startsWith(OF) || remainingText.startsWith(IS)) { //if the string starts with "of" or "is"
										hasOf = true; //show that this looks even more like the right heading
										remainingText = tidyTitle(remainingText.substring(OF.length())); //remove the beginning "of" and tidy the string (this only works for both "of" and "is" because they are both the same length)
										if(remainingText.length() == 0) { //if there is no text left, the "of"... must have meant the title is on the next line
											if(lineTokenizer.hasMoreTokens()) { //if there are more lines
												remainingText = tidyTitle(lineTokenizer.nextToken()); //get the next line and tidy it
											}
										}
									} else
										//if neither "of" nor "is" is present
										hasOf = false; //there is no "of"
									//remove "by" if present and tidy the property
									if(remainingText.length() > 0) { //if we have anything remaining
										//only override an old title if this line has "of"
										if((title == null || hasOf == true) && !foundPGTitle //if we haven't already found a title in another PG line
												&& CharSequences.containsLetterOrDigit(remainingText)) {
											title = tidyTitle(remainingText); //whatever is left is the title
											foundPGTitle = true; //show that we found a title on the Project Gutenberg line, so don't use any titles from other Project Gutenberg lines
											//compensate for incomplete titles (e.g. "Volume 1:" from 1dfre10.txt, part of a title in 2ppdl10.txt))
											final int byIndex = getByIndex(line); //see where "by" appears on the line
											//if the line originally ended with dependent punctuation
											//  (such as a ':') or the line started with an asterisk
											//  but didn't end with an asterisk, or what we have so
											//  far ends in "of" (e.g. 15frd10.txt) or "de"
											//  (e.g. marbo10.txt), and there are more lines, we'll
											//  add the next line to our title (but only if the
											//  author doesn't appear on this line)
											if(lineTokenizer.hasMoreTokens() && byIndex < 0
													&& (DEPENDENT_PUNCTUATION_CHARACTERS.contains(originalLastChar) || (originalFirstChar == '*' && originalLastChar != originalFirstChar)
															|| title.endsWith(" " + OF) || title.endsWith(" in") //(e.g. frnrd10.txt) //TODO use a constant
															|| title.endsWith(" and") //(e.g. miltp10.txt, although that text will get these lines separated if paragraph sensing is turned on) //TODO use a constant
															|| title.endsWith(" de"))) { //TODO use a constant
												final String nextLine = lineTokenizer.nextToken(); //get the next line
												if(!isFileLine(nextLine)) { //if this is not the file line
													if(DEPENDENT_PUNCTUATION_CHARACTERS.contains(originalLastChar)) //if the line ended with dependent punctuation
														title += originalLastChar; //add the puncutation back
													//add the tidied next line, and tidy everything again
													title = tidyTitle(title + " " + tidyTitle(nextLine));
												}
											}
										}
										if(lineTokenizer.hasMoreTokens()) { //if there are more lines
											final String nextLine = lineTokenizer.nextToken(); //get the next line, just to make sure
											//some PG works (e.g. Shakespeare's folios) actually have the title on the following line
											//  some lines surrounded by asterisks simply tell the intended filename, however
											//  furthermore, some lines surrounded by asterisks (e.g. jarg400.txt) just tell about older versions of etexts
											//if the next line starts and ends with asterisks, but is not the file line
											if(nextLine.startsWith("*") //TODO use a constant
													&& nextLine.endsWith("*") //TODO use a constant
													&& getETextString(nextLine) == null //make sure there is no string like "etext"
													&& !isFileLine(nextLine) && getByIndex(line) < 0 //if the current line has "by" in it, the title on the first line was probably correct (e.g. acrdi10.txt)
													//if the first line has "of" appearing before the almost-end
													&& ((Strings.indexOfIgnoreCase(line, " of ") < 0 || Strings.indexOfIgnoreCase(line, " of ") > (line.length() * 3 / 4)) //it's risky going to the next line---make sure we want to go there (e.g. berne10.txt)
															|| CharSequences.endsWithIgnoreCase(remainingText, " folio") //if the title ends in "folio" (e.g. 0ws??10.txt), the actual title is probably on the next line
													)) {
												remainingText = tidyTitle(nextLine); //tidy the remaining line
												if(CharSequences.containsLetterOrDigit(remainingText)) //if the trimmed string is a title
													title = remainingText; //use this for a title
											}
										}
										break; //stop looking for the title
									}
								}
							}
						}
						if(title != null) { //if we have found a title
							break; //stop looking at elements
						}
					}
					final StringTokenizer lineTokenizer = new StringTokenizer(text, EOL_CHARACTERS.toString()); //create a tokenizer to look at each line
					while(lineTokenizer.hasMoreTokens()) { //while there are more lines
						final String line = lineTokenizer.nextToken(); //get the next line
						if(nextLineTitleAfterElement != null) {
							title = line; //we'll use this text for the title
							nextLineTitleAfterElement = null;
							break; //stop looking for the title
						} else if(CharSequences.endsWithIgnoreCase(line.trim(), "Etext of:")) { //e.g. email025.txt TODO use a constant
							nextLineTitleAfterElement = childElement; //TODO testing
						}
					}
					if(nextLineTitleAfterElement != null && nextLineTitleAfterElement != childElement) {
						title = text; //we'll use this text for the title
						break; //stop looking for the title

					}
				}
			}
		}
		return title != null ? tidyTitle(title) : null; //return the title we found, if any---always tidy it once more if we have a valid string (it might have been a property which we haven't tidied yet, for example)
	}

	/**
	 * Determines the author of the Project Gutenberg etext from the header. The "Author: " property is first located, but ignored if its value is "author". If
	 * unsuccessful, a single line beginning with "by" is located. If unsuccessful, the information is extracted from the header.
	 * @param headerFragment A document fragment containing a Project Gutenberg header.
	 * @param title The title of the work, if it is known, or <code>null</code> if the title is not known.
	 * @return The author of the work, or <code>null</code> if the name could not be determined.
	 */
	public static String getAuthor(final DocumentFragment headerFragment, final String title) {
		String author = getPropertyValue(headerFragment, "author", ':'); //try to get the author property in the header TODO use a constant here
		if(author == null) { //if we couldn't find the author property
			author = getPropertyValue(headerFragment, "authors", ':'); //try to get the authors property in the header TODO later try to separate them out TODO use a constant here
		}
		if(author != null && Strings.startsWithIgnoreCase(author.trim(), "several")) { //if this author is "several" (e.g. cm63b10.txt) TODO use a constant
			author = null; //we really didn't find an author
		}
		if(author == null) { //if we couldn't find the author property
			/*TODO fix
					}
			//TODO del; testing		if(author==null) //if we still couldn't find the author property
					{
			*/
			boolean foundByAuthor = false; //this indicates if we've used "by XXX" to get the author
			boolean foundPGByAuthor = false; //this indicates if we've used a Project Gutenberg "by XXX" to get the author
			String titlePossessionAuthor = null; //if nothing else, we may be able to the author's name in the title
			String editedByAuthor = null; //even worse, we may want to use the editor
			final NodeList childNodes = headerFragment.getChildNodes(); //get the list of child nodes
			for(int childNodeIndex = 0; childNodeIndex < childNodes.getLength(); ++childNodeIndex) { //look at each child node
				final Node childNode = childNodes.item(childNodeIndex); //get this child node
				if(childNode.getNodeType() == Node.ELEMENT_NODE) { //if this is an element
					final Element childElement = (Element)childNode; //get a reference to this element
					final String text = XmlDom.getText(childElement, true); //get the text of the element
					boolean isNextLineByAuthor = false; //we don't know yet if the author is on the next line
					if(isPGHeaderElement(childElement)) { //if this is a header element
						final StringTokenizer lineTokenizer = new StringTokenizer(text, EOL_CHARACTERS.toString()); //create a tokenizer to look at each line
						while(lineTokenizer.hasMoreTokens()) { //while there are more lines
							final String line = lineTokenizer.nextToken(); //get the next line
							//see if "Project Gutenberg EText" or "Project Gutenberg Edition" is on this line
							if(Strings.indexOfIgnoreCase(line, PROJECT_GUTENB) >= 0) { //if "Project Gutenberg" is on this line
								final String eTextString = getETextString(line); //get the etext string on the line
								//get the index of the etext string, if there was one
								final int eTextStringIndex = eTextString != null ? Strings.indexOfIgnoreCase(line, eTextString) : -1;
								//if this is the correct line, and there's whitespace after the etext string
								if(eTextStringIndex >= 0 && eTextStringIndex + eTextString.length() < line.length()
										&& (Characters.isWhitespace(line.charAt(eTextStringIndex + eTextString.length()))
												|| DEPENDENT_PUNCTUATION_CHARACTERS.contains(line.charAt(eTextStringIndex + eTextString.length())))) {
									//remove the header string and everything before it
									String remainingText = line.substring(eTextStringIndex + eTextString.length());
									final int byIndex = getByIndex(remainingText); //see if there is a "by" on the line
									if(byIndex >= 0) { //if there is a "by"
										//remove the "by" and everything before it, and tidy the property
										remainingText = tidyAuthor(remainingText.substring(byIndex + BY.length()));
										//if we have anything remaining, and it's not "author", "himself", or "herself"
										if(!foundPGByAuthor //if we haven't already found a Project Gutenberg "by" author (e.g. ionly10.txt)
												&& CharSequences.containsLetterOrDigit(remainingText) && !"AUTHOR".equalsIgnoreCase(remainingText) //TODO use a constant
												&& !"himself".equalsIgnoreCase(remainingText) //(e.g. advlr10.txt) TODO use a constant
												&& !"herself".equalsIgnoreCase(remainingText)) { //(e.g. advlr10.txt) TODO use a constant
											author = tidyAuthor(remainingText); //whatever is left is the author; tidy it
											foundPGByAuthor = true; //show that we used the Project Gutenberg header to get the by author
											continue; //try the other lines, just in case they have something better
										}
									}
									//try to find possession (e.g. "Shakespeare's") in the title---it may be all we have
									final int possessionIndex = Strings.indexOfIgnoreCase(remainingText, "'s"); //see if there is a "'s" in the title
									if(possessionIndex > 0) { //if there is possession in the title
										//try to find the start of the word showing possession
										final int wordBeginIndex = CharSequences.charLastIndexOf(remainingText, TRIM_CHARACTERS, possessionIndex - 1) + 1;
										if(wordBeginIndex < possessionIndex) { //if this is a valid index
											remainingText = remainingText.substring(wordBeginIndex, possessionIndex); //get the word showing possession
											if(remainingText.length() > 0 //if we have anything remaining
													&& !Strings.startsWithIgnoreCase(remainingText, "everybody")) { //"everybody" isn't a valid author (as in "Everybody's Magazine", 10evm10.txt)
												titlePossessionAuthor = tidyAuthor(remainingText); //whatever is left is the author
												continue; //try the other lines, just in case they have something better
											}
										}
									}
								}
							}
							//see if we can find "by XXX"
							if(!isFileLine(line)) { //make sure this isn't the file line, which never has the author name
								final int byIndex = getByIndex(line); //see if there is a "by" on the line
								if(byIndex >= 0 || isNextLineByAuthor) { //if there is a "by", or we had decided the next line is the author
									//see if "used" appears (as in "used by")
									final int usedIndex = Strings.indexOfIgnoreCase(line, "used "); //TODO use a constant
									//remove the "by" and everything before it, and tidy the property
									String remainingText = byIndex >= 0 //if we found by
											? tidyAuthor(line.substring(byIndex + BY.length())) //use everything after by
											: line; //if we didn't find by but we know the author should be on this line, use the whole line
									if(CharSequences.containsLetterOrDigit(remainingText) //if we have anything remaining
											&& (isNextLineByAuthor || usedIndex < 0 || usedIndex != byIndex - "used ".length())) { //make sure this isn't "used by" (e.g. brnte10.txt)
										//if we've already found an author, we'll only use the new author if it's a more complete version of the author already given (e.g. only the last name was given on the first line)
										if(author == null || CharSequences.endsWithIgnoreCase(remainingText, author)) {
											if(!foundByAuthor //if this is the first author we've found using this method
													&& !Strings.startsWithIgnoreCase(remainingText, "AUTHOR") //TODO use a constant TODO this code is duplicated
													&& !Strings.startsWithIgnoreCase(remainingText, "himself") //(e.g. advlr10.txt) TODO use a constant
													&& !Strings.startsWithIgnoreCase(remainingText, "herself")) { //(e.g. dbry11.txt) TODO use a constant
												author = remainingText; //whatever is left is the author
												foundByAuthor = true; //show that we've already found an author this way, so don't replace this one if we find another
												continue; //try the other lines, just in case they have something better
											}
										}
									} else if(line.trim().endsWith(" " + BY)) { //if this line ends with " by" in lowercase TODO use a constant
										isNextLineByAuthor = true; //we'll expect the author to be on the next line
									}
								}
							}
						}
					}
					//if this element is not a header and we still don't have the author, it might talk about the title and author anyway, or even the editor
					else if(author == null) {
						if(Strings.indexOfIgnoreCase(text, PROJECT_GUTENB) < 0) { //the author paragraph should *not* have "Project Gutenberg" in it (e.g. 22gbl10.txt)
							final StringTokenizer lineTokenizer = new StringTokenizer(text, EOL_CHARACTERS.toString()); //create a tokenizer to look at each line
							while(lineTokenizer.hasMoreTokens()) { //while there are more lines
								final String line = lineTokenizer.nextToken(); //get the next line
								int startSearchIndex = -1; //we'll only start searching after the title or after "edited", as in "edited by"
								if(title != null) { //if we have a title
									startSearchIndex = Strings.indexOfIgnoreCase(line, title); //see if the title is on this line
									if(startSearchIndex >= 0) //if we found something to search after
										startSearchIndex += title.length(); //start searching after the title
								}
								if(startSearchIndex < 0) { //if we didn't have a title or couldn't find it on this line
									startSearchIndex = Strings.indexOfIgnoreCase(line, "edited"); //see if "edited", as in "edited by", is on this line TODO use a constant
									if(startSearchIndex >= 0) //if we found something to search after
										startSearchIndex += "edited".length(); //start searching after the string TODO use a constant
								}
								if(startSearchIndex >= 0) { //if we have something to search after
									//see if there is a "by" on the line after the prefix
									final int byIndex = Strings.indexOfIgnoreCase(line, BY, startSearchIndex);
									if(byIndex >= 0) { //if there is a "by"
										//remove the "by" and everything before it, and tidy the property
										final String remainingText = tidyAuthor(line.substring(byIndex + BY.length()));
										if(CharSequences.containsLetterOrDigit(remainingText)) { //if we have anything remaining
											author = remainingText; //whatever is left is the author
											break; //this is about the best we can do
										}
									}
								}
							}
						}
					}
				}
			}
			//try to get the "by XXX" property in the header TODO use a constant here
			final String byAuthor = getPropertyValue(headerFragment, "by", ' ');
			//if the by author is a more complete version of the PG header author
			//  (the PG header author should always be the most accurate, but it
			//  oftentimes be incomplete)
			if(byAuthor != null && byAuthor.length() < 128 //if we found a by author and it's not too long
					&& Strings.indexOfIgnoreCase(byAuthor, "PROJECT GUTENBERG-tm") < 0) { //the line with "PROJECT GUTENBERG-tm" talks about using or reading the work, and is not the header (e.g. alad10.txt) TODO use a constant
				if(author == null //if we don't have an author yet, or the by author is a more complete version of the one we have
						|| (byAuthor.length() > author.length() && Strings.indexOfIgnoreCase(byAuthor, author) >= 0) && !isFileLine(byAuthor) //and this isn't the file line (e.g. sleep10.txt)
				) {
					author = byAuthor; //use the by author
				}
			}
			if(author == null && titlePossessionAuthor != null) { //if we haven't found an author, but we found ownership indicated in the title
				author = titlePossessionAuthor; //use that---it's the best we can find (this works for "Shakespeare's XXX", but not "Gulliver's Travels"
			}
		}
		return author != null ? tidyAuthor(author) : null; //return the title we found, if any---always tidy it once more if we have a valid string (it might have been a property which we haven't tidied yet, for example)
	}

	/**
	 * Create a description of the Project Gutenberg etext from the header. All lines relating to filenames are eleminated, the asterisks removed, and the lines
	 * concatenated.
	 * @param headerFragment A document fragment containing a Project Gutenberg header.
	 * @return A description of the work, or <code>null</code> if a description could not be determined.
	 */
	public static String getDescription(final DocumentFragment headerFragment) {
		final Element headerElement = getHeaderElement(headerFragment); //get the header element
		if(headerElement != null) { //if we found the header element
			final StringBuilder descriptionStringBuilder = new StringBuilder(); //create a new string buffer to hold the description we consruct
			final String headerText = XmlDom.getText(headerElement, true); //get the text of the header
			final StringTokenizer lineTokenizer = new StringTokenizer(headerText, EOL_CHARACTERS.toString()); //create a tokenizer to look at each line
			while(lineTokenizer.hasMoreTokens()) { //while there are more lines
				final String line = lineTokenizer.nextToken(); //get the next line
				//if this isn't the filename line, and it's not the "copyright laws are changing all over the world..." line
				if(!isFileLine(line) && Strings.indexOfIgnoreCase(line, "copyright laws") < 0) { //TODO use a constant
					if(descriptionStringBuilder.length() > 0) //if there is already part of a description
						descriptionStringBuilder.append(' '); //separate the description components with spaces
					//trim both ends of "*" and whitespace and add the line to the description
					descriptionStringBuilder.append(Strings.trim(line, TRIM_CHARACTERS.add('*'))); //TODO use a constant here
				}
			}
			if(descriptionStringBuilder.length() > 0) //if we have a description
				return descriptionStringBuilder.toString(); //return the description string we constructed
		}
		return null; //show that we couldn't find a description
	}

	/**
	 * Determines the language of the Project Gutenberg etext from the header. The "Language: " property is first located, but ignored if its value is "language".
	 * @param headerFragment A document fragment containing a Project Gutenberg header.
	 * @return The language of the work, or <code>null</code> if the language could not be determined.
	 */
	public static String getLanguage(final DocumentFragment headerFragment) {
		return getPropertyValue(headerFragment, "language", ':'); //try to get the language property in the header TODO use a constant here
	}

	/**
	 * Finds the element containing the Project Gutenberg header lines.
	 * @param headerFragment A document fragment containing a Project Gutenberg header.
	 * @return The element containing the header ("The Project Gutenberg Etext"), or <code>null</code> if the header element could not be determined.
	 */
	public static Element getHeaderElement(final DocumentFragment headerFragment) {
		final NodeList childNodes = headerFragment.getChildNodes(); //get the list of child nodes
		for(int childNodeIndex = 0; childNodeIndex < childNodes.getLength(); ++childNodeIndex) { //look at each child node
			final Node childNode = childNodes.item(childNodeIndex); //get this child node
			if(childNode.getNodeType() == Node.ELEMENT_NODE) { //if this is an element
				final Element childElement = (Element)childNode; //get a reference to this element
				if(isPGHeaderElement(childElement)) //if this is a header element
					return childElement; //return the element
			}
		}
		return null; //show that we couldn't find the header element
	}

	/**
	 * Determines if this element containing the Project Gutenberg header lines.
	 * @param element An element from the header.
	 * @return <code>true</code> if this element contains the header information.
	 */
	public static boolean isPGHeaderElement(final Element element) {
		final String text = XmlDom.getText(element, true); //get the text of the element
		final StringTokenizer lineTokenizer = new StringTokenizer(text, EOL_CHARACTERS.toString()); //create a tokenizer to look at each line
		final int lineCount = lineTokenizer.countTokens(); //see how many lines we have
		//if this paragraph is four lines or less
		if(lineCount <= 4 && Strings.indexOfIgnoreCase(text, "money") < 0) { //TODO use a constant TODO test; last minute hack to get this to work on the early texts
			while(lineTokenizer.hasMoreTokens()) { //while there are more lines
				final String line = lineTokenizer.nextToken(); //get the next line
				//check for both "Project Gutenberg" and "EText", because some works
				//  contain "Project Gutenberg EText" and some contain "Project
				//  Gutenberg's EText"; some works (such as etarn10.txt) have a line
				//  such as "This is a COPYRIGHTED Project Gutenberg Etext,
				//  Details Below", which is *not* a header element, although some
				//  (such as clstn10.txt) have two lines, one with "copyright", the
				//  other without
				final String projectGutenbergString = getProjectGutenbergString(line);
				//get the index of the string if it exists
				final int projectGutenbergIndex = projectGutenbergString != null ? Strings.indexOfIgnoreCase(line, projectGutenbergString) : -1;
				//see if the word "copyright" is on the line, which means this is a false header
				final int copyrightIndex = Strings.indexOfIgnoreCase(line, COPYRIGHT);
				final String eTextString = getETextString(line); //get the etext string on the line
				//get the index of the etext string, if there was one
				final int eTextStringIndex = eTextString != null ? Strings.indexOfIgnoreCase(line, eTextString) : -1;
				if(projectGutenbergIndex >= 0 && eTextStringIndex >= 0 && copyrightIndex < 0) { //if this element contains the header text
					//make sure "etext" comes after "project gutenberg" (although it may come in the middle for "gutenberg's")
					if(eTextStringIndex > projectGutenbergIndex
							//make sure there's not too much between "project gutenberg" and "etext"
							&& eTextStringIndex < projectGutenbergIndex + projectGutenbergString.length() + 4) { //TODO use a constant
						//the line with "PROJECT GUTENBERG-tm" talks about using or reading the work, and is not the header (e.g. alad10.txt)
						//TODO del if not needed						if(StringUtilities.startsWithIgnoreCase(line.substring(projectGutenbergIndex), "PROJECT GUTENBERG-tm"))
						{
							//get the first non-whitespace character
							final char firstChar = line.charAt(CharSequences.notCharIndexOf(line, TRIM_CHARACTERS));
							//if the line doesn't start with '*', make sure there's whitespace or
							//  dependent punctuation after "etext" (e.g. it's not "...the first
							//  nine Project Gutenberg Etexts...")
							if(eTextStringIndex + eTextString.length() < line.length() && (firstChar == '*' //TODO check with original problem etext; use constant
									|| Characters.isWhitespace(line.charAt(eTextStringIndex + eTextString.length()))
									|| DEPENDENT_PUNCTUATION_CHARACTERS.contains(text.charAt(eTextStringIndex + eTextString.length()))))

							{
								return true; //this is a header element
							}
						}
					}
				}
				//some Project Gutenberg files (e.g. 22frd10.txt) start with just "Project Gutenberg..."
				//  if "Project Gutenberg" begins the element, and this is the first element
				if(projectGutenbergIndex == 0) {
					boolean isFirstElement = true; //start out assuming this is the first element
					Node previousSiblingNode = element.getPreviousSibling(); //get the element's previous sibling
					while(previousSiblingNode != null) { //while there are previous siblings
						//if the previous sibling is an element, our element is not the first element
						if(previousSiblingNode.getNodeType() == previousSiblingNode.ELEMENT_NODE) {
							isFirstElement = false; //our element is not the first element
							break; //stop trying to disprove ourselves
						} else { //if we haven't found a previous element so far
							previousSiblingNode = previousSiblingNode.getPreviousSibling(); //get the previous sibling's previous sibling
						}
					}
					if(isFirstElement) //if this is the first element
						return true; //show that this is the Project Gutenberg header element
				}
				if(isFileLine(line)) { //if "Project Gutenberg" wasn't found, check for the file line (e.g. so that 2drvb10.txt can have a description)
					return true; //we'll assume the file line is in the same paragraph as the heading---in any case, this makes the pagraph *a* heading, if not *the* heading
				}
			}
		}
		return false; //show that this doesn't appear to be the header element
	}

	/**
	 * Retrieves the value of a property stored in a header. Property values that are the same as the property name (e.g. "Title: TITLE") are ignored. Any line
	 * containing only "contents" is ignored, along with all following lines.
	 * @param headerFragment A document fragment containing a Project Gutenberg header.
	 * @param property The name of the property to retrieve; this name is case insensitive.
	 * @param delimiter The delimiter character (e.g. ':' or ' ') that separates the property from its value.
	 * @return The value of the property.
	 */
	public static String getPropertyValue(final DocumentFragment headerFragment, final String property, final char delimiter) {
		final NodeList childNodes = headerFragment.getChildNodes(); //get the list of child nodes
		for(int i = 0; i < childNodes.getLength(); ++i) { //look at each child node
			final Node childNode = childNodes.item(i); //get this child node
			if(childNode.getNodeType() == Node.ELEMENT_NODE) { //if this is an element
				final Element childElement = (Element)childNode; //get a reference to this element
				final String text = XmlDom.getText(childElement, true).trim(); //get the trimmed text of the element
				//if there are no ends-of-line in this text
				//TODO del; did we ever need this?				if(StringUtilities.charIndexOf(text, EOL_CHARS)<0)  //make sure CR/LF isn't present
				{
					if(Strings.startsWithIgnoreCase(text, property + delimiter)) { //if the string starts with the property and the delimiter (e.g. "property:"), case insensitive
						//get everything after the colon and tidy it---that's the property value
						final String propertyValue = tidyProperty(text.substring(property.length() + 1));
						if(!propertyValue.equalsIgnoreCase(property)) //if the property and its value are not the same (e.g. "Title: TITLE")
							return propertyValue; //return the value we found
					}
				}
			}
		}
		return null; //show that we couldn't find the property
	}

	/**
	 * Determines if the given document is a Project Gutenberg EText. Determination is made if the words "Project Gutenberg" and "EText" appear in a single line,
	 * followed at some point by a line containing "*START*" and "SMALL PRINT", and another containing "*END*" and "SMALL PRINT", case insensitive.
	 * @param document The XHTML document possibly containing the Project Gutenberg text.
	 * @return <code>true</code> if the text is a Project Gutenberg EText.
	 */
	public static boolean isProjectGutenbergEText(final Document document) {
		//these three pairs of strings are the the ones to search for, in the order of lines given
		final String[][] indicatorStrings = {{PROJECT_GUTENB, ETEXT}, {SMALL_PRINT, SMALL_PRINT_START}, {SMALL_PRINT, SMALL_PRINT_END}};
		int indicatorIndex = 0;
		final Element bodyElement = findHtmlBodyElement(document).orElse(null); //get the <body> element of the XHTML document
		if(bodyElement != null) { //if there is a body element
			final NodeList childNodes = bodyElement.getChildNodes(); //get the list of body child nodes
			for(int i = 0; i < childNodes.getLength(); ++i) { //look at each child node
				final Node childNode = childNodes.item(i); //get this child node
				if(childNode.getNodeType() == Node.ELEMENT_NODE) { //if this is an element
					final Element childElement = (Element)childNode; //get a reference to this element
					final String text = XmlDom.getText(childElement, true); //get the text of the element
					if(indicatorIndex == 0) { //if this is the first indicator, do special checking
						//if the text contains both "Project Gutenberg" and "EText", "Edition", or "EBook"
						if(Strings.indexOfIgnoreCase(text, PROJECT_GUTENB) >= 0 && getETextString(text) != null) {
							++indicatorIndex; //go to the next indicators
							if(indicatorIndex >= indicatorStrings.length) //if we've matchd all the indicators
								return true; //show that this document matches all our checks, so we think it is a Project Gutenberg EText
						}
					}
					//for the other indicators, do normal checks if the text contains both of our current indicators
					else if(Strings.indexOfIgnoreCase(text, indicatorStrings[indicatorIndex][0]) >= 0
							&& Strings.indexOfIgnoreCase(text, indicatorStrings[indicatorIndex][1]) >= 0) {
						++indicatorIndex; //go to the next indicators
						if(indicatorIndex >= indicatorStrings.length) //if we've matchd all the indicators
							return true; //show that this document matches all our checks, so we think it is a Project Gutenberg EText
					}
				}
			}
		}
		/*TODO del; this isn't worth the effort for now
					//if we didn't find the correct strings, see if we at least found some
					//  strings and if we found the footer
				if(indicatorIndex>0 && getPGFooterElement(document)!=null)
					return true; //we still think this is a Project Gutenberg file, even if everything didn't match
		*/
		return false; //show that we don't think this is a Project Gutenberg EText
		/*TODO del this attempt for the complete works of Shakespeare, etext98
					//these three pairs of strings are the the ones to search for, in the order of lines given
		//TODO del		final String[][] indicatorStrings={{PROJECT_GUTENB, ETEXT}, {SMALL_PRINT, SMALL_PRINT_START}, {SMALL_PRINT, SMALL_PRINT_END}};
		//TODO del		int indicatorIndex=0;
				boolean hasProjectGutenberg=false;  //we'll try to find each of these indicators
				boolean hasSmallPrintStart=false;
				boolean hasSmallPrintEnd=false;
				final Element bodyElement=XHTMLUtilities.getBodyElement(document);  //get the <body> element of the XHTML document
				if(bodyElement!=null) {	//if there is a body element
					final NodeList childNodes=bodyElement.getChildNodes();  //get the list of body child nodes
					for(int i=0; i<childNodes.getLength(); ++i) {	//look at each child node
						final Node childNode=childNodes.item(i);  //get this child node
						if(childNode.getNodeType()==Node.ELEMENT_NODE) {	//if this is an element
							final Element childElement=(Element)childNode;  //get a reference to this element
							final String text=XMLUtilities.getText(childElement, true); //get the text of the element
		Log.trace("Checking first indicator: etext string: ", getETextString(text));
								//if the text contains both "Project Gutenberg" and "EText", "Edition", or "EBook"
							if(StringUtilities.indexOfIgnoreCase(text, PROJECT_GUTENB)>=0
									&& getETextString(text)!=null)
							{
		Log.trace("found project gutenberg string");
								hasProjectGutenberg=true; //show that we found "Project Gutenberg"
							}
								//if this text contains "small print"
							else if(StringUtilities.indexOfIgnoreCase(text, SMALL_PRINT)>=0)
							{
									//if we haven't found the start, and the text also contains "start" or "***"
								if(!hasSmallPrintStart &&
										(StringUtilities.indexOfIgnoreCase(text, SMALL_PRINT_START)>=0
										|| text.indexOf("***")>=0)) {	//TODO use a constant
		Log.trace("found small print start");
									hasSmallPrintStart=true;  //show that we found the start of the small print
								}
									//if the text also contains "end" or "V." or "Ver." or "Version"
								else if(StringUtilities.indexOfIgnoreCase(text, SMALL_PRINT_END)>=0
										|| StringUtilities.indexOfIgnoreCase(text, "V.")>=0 //TODO use constants
										|| StringUtilities.indexOfIgnoreCase(text, "Ver.")>=0
										|| StringUtilities.indexOfIgnoreCase(text, "Version")>=0)
								{
		Log.trace("found small print end");
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

	/**
	 * Tidies a title of the etext. Parenthetical postfixes are removed, and the string is trimmed if whitespace and other characters.
	 * @param string The string to tidy.
	 * @return The string with extra characters removed.
	 * @see #tidyProperty
	 */
	public static String tidyTitle(final String string) { //TODO eventually put in some common class
		final StringBuilder stringBuilder = new StringBuilder(string); //create a new string buffer with the string
		tidyProperty(stringBuilder); //do default tidying
		//if the string starts with "release of:" (but only in lowercase), remove it (e.g. duglas11.txt)
		if(StringBuilders.startsWith(stringBuilder, "release of:")) { //if the string starts with "'s " (but only in lowercase) TODO use a constant
			tidyProperty(stringBuilder.delete(0, "release of:".length())); //remove the beginning Project Gutenberg string and tidy the string TODO use a constant
		}
		//if the string contains with "title:" in any case (e.g. wacia10.txt)
		final int titlePropertyIndex = Strings.indexOfIgnoreCase(stringBuilder.toString(), "title:"); //TODO use a constant TODO use StringBuilders
		if(titlePropertyIndex >= 0) { //TODO this should probably go in getProperty(), as that's where the value comes from
			tidyProperty(stringBuilder.delete(titlePropertyIndex, stringBuilder.length())); //remove that text and tidy the string TODO use a constant
		}

		//see if "Project Gutenberg" or a variation is in the string
		final String projectGutenbergString = getProjectGutenbergString(stringBuilder.toString()); //TODO do something more efficient than creating a string
		//get the index of the string if it exists
		final int projectGutenbergIndex = projectGutenbergString != null ? Strings.indexOfIgnoreCase(stringBuilder.toString(), projectGutenbergString) : -1; //TODO do something more efficient than creating a string
		//if the string starts with a variant of "Project Gutenberg" or "The Project Gutenberg"
		if(projectGutenbergString != null
				&& (StringBuilders.startsWith(stringBuilder, projectGutenbergString) || StringBuilders.startsWith(stringBuilder, "The " + projectGutenbergString) //TODO use a constant
						|| StringBuilders.startsWith(stringBuilder, "This is the " + projectGutenbergString) //TODO use a constant
				)) {
			tidyProperty(stringBuilder.delete(0, projectGutenbergIndex + projectGutenbergString.length())); //remove the beginning "'s " and tidy the string TODO use a constant
			final String eTextString = getETextString(stringBuilder.toString()); //get the etext string in the string
			//if the string starts with "'s " (but only in lowercase), remove it (e.g. "PG's...")
			if(StringBuilders.startsWith(stringBuilder, "'s ")) { //if the string starts with "'s " (but only in lowercase) TODO use a constant
				tidyProperty(stringBuilder.delete(0, "'s ".length())); //remove the beginning Project Gutenberg string and tidy the string TODO use a constant
			}
			//if the string starts with a variant of "EText"
			if(eTextString != null && StringBuilders.startsWith(stringBuilder, eTextString)) {
				tidyProperty(stringBuilder.delete(0, eTextString.length())); //remove the beginning etext string and tidy the string
			}
		}
		//if the string starts with "s" (but only in lowercase), remove it (e.g. "Project Gutenberg ETexts from...")
		if(StringBuilders.startsWith(stringBuilder, "s ")) { //if the string starts with "s " (but only in lowercase) TODO use a constant
			tidyProperty(stringBuilder.delete(0, "s ".length())); //remove the beginning "s " and tidy the string TODO use a constant
		}
		//if the string starts with "in" (but only in lowercase), remove it (e.g. "in French of...", 8plno07.txt)
		if(StringBuilders.startsWith(stringBuilder, "in")) { //TODO use a constant
			if(stringBuilder.indexOf("French") >= 0 //TODO use a constant; do stricter order checking
					|| stringBuilder.indexOf("Spanish") >= 0 || stringBuilder.indexOf("German") >= 0 || stringBuilder.indexOf("Italian") >= 0
					|| stringBuilder.indexOf("Latin") >= 0) {
				final int ofIndex = stringBuilder.indexOf(OF); //find out where "of" appears
				if(ofIndex >= 0) { //if "of" is present, we think the string begins with something line "in French of"
					//remove everything up to and including "OF" and retidy
					tidyProperty(stringBuilder.delete(0, ofIndex + OF.length()));
				}
			}
		}
		//if the string starts with "of" (but only in lowercase), remove it (e.g. "The Project Gutenberg ETExt of...")
		if(StringBuilders.startsWith(stringBuilder, OF)) { //if the string starts with "of" (but only in lowercase) TODO use a constant
			tidyProperty(stringBuilder.delete(0, OF.length())); //remove the beginning "of" and tidy the string TODO use a constant
		}
		//if the string starts with "from" (but only in lowercase), remove it (e.g. "Project Gutenberg ETexts from...")
		if(StringBuilders.startsWith(stringBuilder, "from")) { //if the string starts with "from" (but only in lowercase) TODO use a constant
			tidyProperty(stringBuilder.delete(0, "from".length())); //remove the beginning "from" and tidy the string TODO use a constant
		}
		//if the string starts with "the" (but only in lowercase), remove it
		if(StringBuilders.startsWith(stringBuilder, THE)) { //if the string starts with "the" (but only in lowercase)
			tidyProperty(stringBuilder.delete(0, THE.length())); //remove the beginning "the" and tidy the string
		}
		//if the string ends with ", or" (e.g. 03tcb10.txt)
		if(CharSequences.endsWithIgnoreCase(stringBuilder, ", or")) { //if the string ends with ", or" TODO use a constnat
			tidyProperty(stringBuilder.delete(stringBuilder.length() - ", or".length(), stringBuilder.length())); //remove the beginning ", or" and tidy the string TODO use a constant
		}
		//if the string starts with "book", "etext", or something similar
		final String eTextString = getETextString(stringBuilder.toString()); //see if "book" or "etext" or something appears in the string
		if(eTextString != null) { //if there is an etext string in the title
			//see where the etext string appeared
			//TODO fix; the current way is a hack and very inefficient; add a StringBuilder.indexOf(StringBUilder, String)		  final int eTextStringIndex=StringBuilder.indexOf(stringBuilder, eTextString);
			final int eTextStringIndex = stringBuilder.toString().indexOf(eTextString);
			if(eTextStringIndex == 0) { //if the string appeared at the beginning TODO this can cause problems if the title is "Ebook 101" or something
				tidyProperty(stringBuilder.delete(0, eTextString.length())); //remove the beginning etext word and tidy the string
				//if the string starts with "of" (but only in lowercase)
				if(StringBuilders.startsWith(stringBuilder, OF)) { //if the string starts with "of" (but only in lowercase)
					tidyProperty(stringBuilder.delete(0, OF.length())); //remove the beginning "of" and tidy the string
				}
			}
		}
		//if the string contains with "author:" in any case (e.g. ffnt110.txt)
		final int authorPropertyIndex = Strings.indexOfIgnoreCase(stringBuilder.toString(), "author:"); //TODO use a constant TODO use StringBuilders
		if(authorPropertyIndex >= 0) { //TODO this should probably go in getProperty(), as that's where the value comes from
			tidyProperty(stringBuilder.delete(authorPropertyIndex, stringBuilder.length())); //remove that text and tidy the string TODO use a constant
		}
		final int byIndex = getByIndex(stringBuilder.toString()); //get the index of "by" TODO if we didn't have to convert to a string, this would be more efficient
		if(byIndex >= 0 && Character.isLowerCase(stringBuilder.charAt(byIndex))) { //if we found "by" (only in lowercase)
			tidyProperty(stringBuilder.delete(byIndex, stringBuilder.length())); //remove "by" and everything after it TODO add a convenience routine like the one for strings
		}
		//see if "copyright" appears in the string
		final int copyrightIndex = Strings.indexOfIgnoreCase(stringBuilder.toString(), COPYRIGHT); //TODO use a StringBuilders method
		if(copyrightIndex >= 0) { //if "copyright" appears in the title
			//if "copyright" is followed by a copyright character
			if(CharSequences.charIndexOf(stringBuilder, Characters.of('@', COPYRIGHT_SIGN), copyrightIndex + 1) >= 0
					|| stringBuilder.indexOf("(c)", copyrightIndex + 1) >= 0 || stringBuilder.indexOf("(C)", copyrightIndex + 1) >= 0) {
				tidyProperty(stringBuilder.delete(copyrightIndex, stringBuilder.length())); //remove "copyright" and everything after it TODO add a convenience routine like the one for strings
			}
		}
		//see if "(c)" appears in the string (e.g. truth10.txt)
		final int copyrightCIndex = Strings.indexOfIgnoreCase(stringBuilder.toString(), "(c)"); //TODO use a StringBuilders method; TODO use a constant
		if(copyrightCIndex >= 0) { //if "(c)" appears in the title
			tidyProperty(stringBuilder.delete(copyrightCIndex, stringBuilder.length())); //remove "(c)" and everything after it TODO add a convenience routine like the one for strings
		}
		tidyProperty(stringBuilder); //tidy the title once more (it might have a comma before "by", for example)
		return stringBuilder.toString(); //return the string we tidied
	}

	/**
	 * Gets the index of "by", making sure it's surrounded by whitespace.
	 * @param string The string to search.
	 * @return The index of the word "by", or -1 if the word is not found or is part of another word.
	 */
	protected static int getByIndex(final String string) {
		int startSearchIndex = 0; //there may be multiple possible "by"s
		while(startSearchIndex < string.length()) { //if we have something to search after
			//see if there is a "by" on the line
			final int byIndex = Strings.indexOfIgnoreCase(string, BY, startSearchIndex);
			if(byIndex >= 0) { //if by appears
				//if "by" is surrounded by whitespace or asterisks TODO fix; right now, this removes parts of titles if those titles have "by" in them
				if((byIndex == 0 //if "by" is at the first of the string
						|| Character.isWhitespace(string.charAt(byIndex - 1)) //or is after whitespace
						|| string.charAt(byIndex - 1) == '*') //or is after an asterisk TODO use a constant

						&& (byIndex + BY.length() == string.length()
								|| (byIndex + BY.length() < string.length() && Character.isWhitespace(string.charAt(byIndex + BY.length()))))) { //if there is whitespace after "by"
					return byIndex; //return this index
				}
				startSearchIndex = byIndex + BY.length(); //start searching after the last occurrence of "by"
			} else
				//if by doesn't appear
				break; //stop looking
		}
		return -1; //show that we couldn't find "by"
	}

	/**
	 * Tidies an author of the etext. All punctuation is removed.
	 * @param string The string to tidy.
	 * @return The string with extra characters removed.
	 * @see #tidyTitle
	 */
	public static String tidyAuthor(final String string) { //TODO eventually put in some common class
		final StringBuilder stringBuilder = new StringBuilder(string); //create a new string buffer with the string
		tidyProperty(stringBuilder); //do default tidying
		//trim the string of all punctuation
		//trim the string of all punctuation (except quotes and group punctuation) and whitespace
		StringBuilders.trim(stringBuilder, PHRASE_PUNCTUATION_CHARACTERS.add(HYPHEN_MINUS_CHAR).add(EM_DASH_CHAR).add(EN_DASH_CHAR).add(TRIM_CHARACTERS));
		//if the string starts with "the" (but only in lowercase), remove it
		if(StringBuilders.startsWith(stringBuilder, THE)) { //if the string starts with "the" (but only in lowercase)
			tidyProperty(stringBuilder.delete(0, THE.length())); //remove the beginning "the" and tidy the string
		}
		//if the string contains with "author:" in any case (e.g. idiot10.txt)
		final int authorPropertyIndex = Strings.indexOfIgnoreCase(stringBuilder.toString(), "author:"); //TODO use a constant TODO use StringBuilders
		if(authorPropertyIndex >= 0) { //TODO this should probably go in getProperty(), as that's where the value comes from
			tidyProperty(stringBuilder.delete(authorPropertyIndex, stringBuilder.length())); //remove that text and tidy the string TODO use a constant
		}
		//if the string ends with "all rights reserved" in any case TODO use a constant
		if(CharSequences.endsWithIgnoreCase(stringBuilder, "all rights reserved")) {
			tidyProperty(stringBuilder.delete(stringBuilder.length() - "all rights reserved".length(), stringBuilder.length())); //remove that text and tidy the string TODO use a constant
		}
		//see if "copyright 19" or "copyright 20" or "copyrihgt (", etc. appears in the string (e.g. efpap10.txt)
		int copyrightIndex = Strings.indexOfIgnoreCase(stringBuilder.toString(), COPYRIGHT); //see if "copyright" appears in the string TODO change to StringBuilders
		if(copyrightIndex >= 0 && (CharSequences.charIndexOf(stringBuilder, Characters.of('@', COPYRIGHT_SIGN)) >= 0 //if "copyright" is followed by a copyright sign TODO use a constant
				|| stringBuilder.indexOf("(c)") >= 0 //if "copyright" is followed by (c) TODO use a constant
				|| stringBuilder.indexOf("19") >= 0 //if "copyright" is followed by 19 TODO use a constant
				|| stringBuilder.indexOf("20") >= 0 //if "copyright" is followed by 19 TODO use a constant
		)) {
			tidyProperty(stringBuilder.delete(copyrightIndex, stringBuilder.length())); //remove that text and tidy the string
		}
		//if the author contains ", this is..." (e.g. email025.txt) TODO use a constant
		final int thisIsIndex = stringBuilder.indexOf(", this is");
		if(thisIsIndex >= 0) {
			tidyProperty(stringBuilder.delete(thisIsIndex, stringBuilder.length())); //remove that text and tidy the string TODO use a constant
		}
		final int byIndex = getByIndex(stringBuilder.toString()); //get the index of "by" TODO if we didn't have to convert to a string, this would be more efficient
		if(byIndex >= 0 && Character.isLowerCase(stringBuilder.charAt(byIndex))) { //if we found "by" (only in lowercase)
			//see if we can find punctuation before "by"
			final int punctuationIndex = CharSequences.charLastIndexOf(stringBuilder, PUNCTUATION_CHARS, byIndex - 1);
			//if by is not at the first of the string and has punctuation before it
			if(punctuationIndex >= 0) { //if we can find punctuation before "by" (e.g. efpap10.txt)
				tidyProperty(stringBuilder.delete(punctuationIndex, stringBuilder.length())); //remove the punctuation and everything else, because we assume it's talking about somebody else
			} else { //if "by" is at the first of the string, assume it's talking about the author
				tidyProperty(stringBuilder.delete(0, byIndex + BY.length())); //remove everything up to and including "by" TODO add a convenience routine like the one for strings
			}
		}
		//if this string doesn't have group punctuation as its first character
		if(stringBuilder.length() > 0 && !LEFT_GROUP_PUNCTUATION_CHARACTERS.contains(stringBuilder.charAt(0)))
			StringBuilders.trimEnd(stringBuilder, RIGHT_GROUP_PUNCTUATION_CHARACTERS); //trim any right group punctuation that's been left on the author for some reason (e.g. huxbr10.txt)
		return stringBuilder.toString(); //return the string we tidied
	}

	/**
	 * Tidies a property, such as the title or the author, of the etext. Parenthetical postfixes are removed, and the string is trimmed if whitespace and other
	 * characters.
	 * @param string The string to tidy.
	 * @return The string with extra characters removed.
	 */
	public static String tidyProperty(final String string) { //TODO eventually put in some common class
		return tidyProperty(new StringBuilder(string)).toString(); //tidy the string from a string buffer
	}

	/**
	 * Tidies a property, such as the title or the author, of the etext. Parenthetical postfixes are removed, and the string is trimmed if whitespace and other
	 * characters.
	 * @param stringBuilder The characters to tidy.
	 * @return The string buffer with extra characters removed.
	 */
	protected static StringBuilder tidyProperty(final StringBuilder stringBuilder) { //TODO eventually put in some common class
		//see if "contents" is part of the property (e.g. tbroa10.txt)
		final int contentsIndex = Strings.indexOfIgnoreCase(stringBuilder.toString(), "contents"); //TODO use a constant; use StringBuilders
		if(contentsIndex > 0 && EOL_CHARACTERS.contains(stringBuilder.charAt(contentsIndex - 1))) //if contents appears after a linebreak
			stringBuilder.delete(contentsIndex, stringBuilder.length()); //remove that line break and everything after
		//some works (e.g. valen10.txt) for some reason end in "^M"
		if(stringBuilder.length() > 1 && stringBuilder.charAt(stringBuilder.length() - 2) == '^') {
			stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length()); //remove the ending control character
		}
		/** The characters we'll trim from the front and back of the string. */
		final Characters TRIM_CHARS = Characters.TRIM_CHARACTERS.add(WHITESPACE_CHARACTERS).add(DEPENDENT_PUNCTUATION_CHARACTERS)
				.add(/*TODO bring back if needed QUOTE_CHARS+*/'*', '.'); //TODO use constants
		//trim the string of whitespace, dashes, and asterisks
		StringBuilders.trim(stringBuilder, TRIM_CHARS);
		//see if there is any group punctuation at the end (e.g. "XXX (XXX)")
		final int rightGroupIndex = CharSequences.charLastIndexOf(stringBuilder, RIGHT_GROUP_PUNCTUATION_CHARACTERS);
		if(rightGroupIndex >= 0) { //if the string ends with a right group characters
			//if there's nothing but whitespace after the right group, we'll remove the entire group
			if(StringBuilders.notCharIndexOf(stringBuilder, Characters.TRIM_CHARACTERS, rightGroupIndex + 1) < 0) {
				//remove evertying from the start of the group onward
				//see if there is any left punctuation at the end (e.g. "XXX (XXX)") (don't just grab the first left group punctuation, because there could be several sets of them)
				final int leftGroupIndex = CharSequences.charLastIndexOf(stringBuilder, LEFT_GROUP_PUNCTUATION_CHARACTERS, rightGroupIndex - 1);
				if(leftGroupIndex >= 0) { //if we found a matching left group punctuation character
					stringBuilder.delete(leftGroupIndex, stringBuilder.length()); //remove the left punctuation and everything after it
				}
			}
		}
		//trim the string of whitespace, dashes, and asterisks again
		StringBuilders.trim(stringBuilder, TRIM_CHARS);
		//collapse all whitespace into spaces
		StringBuilders.collapse(stringBuilder, WHITESPACE_CHARACTERS, " ");
		return stringBuilder; //return the string we tidied
	}

	/**
	 * Determines if the given lines is the Project Gutenberg line indicating the preferred filename. This is determined by whether the line starts and ends in
	 * asterisks and contains the word "file", or contains the words ".txt", or ".zip".
	 * @param line The line of text that may be a file line.
	 * @return <code>true</code> if the string is the filename indicator line.
	 */
	protected static boolean isFileLine(final String line) {
		final String trimmedLine = line.trim(); //trim the line
		return (trimmedLine.startsWith("*") //see if the line begins and ends with asterisks and contains the string "file"
				&& trimmedLine.endsWith("*") && Strings.indexOfIgnoreCase(trimmedLine, "file") >= 0) //TODO use a constant
				|| Strings.indexOfIgnoreCase(trimmedLine, ".txt") >= 0 //TODO use a constant
				|| Strings.indexOfIgnoreCase(trimmedLine, ".zip") >= 0; //TODO use a constant
	}

	/**
	 * Searches for "Project Gutenberg" or "PG" in the current string, and returns the match. (For "PG's", see 4saht10.txt.)
	 * @param text The text to search.
	 * @return The matched string, or <code>null</code> if there was no match.
	 */
	protected static String getProjectGutenbergString(final String text) {
		final String[] pgStrings = {"PG", "Project Gutenburg", "Project Gutenberg"}; //any of these strings will work TODO use constants
		for(int i = pgStrings.length - 1; i >= 0; --i) { //look at each of the etext strings
			final String pgString = pgStrings[i]; //get this string
			//see if the string is in the text
			final int pgStringIndex = Strings.indexOfIgnoreCase(text, pgString);
			if(pgStringIndex >= 0) { //if the string is in the text
				//return the real string we found
				return text.substring(pgStringIndex, pgStringIndex + pgString.length());
			}
		}
		return null; //show that we didn't find a Project Gutenberg string
	}

	/**
	 * Searches for "EText", "EBook", or "Edition" in the current string, and returns the match. This even accepts "Gutenberg's", as in, "Project Gutenberg's
	 * United States Congress Address Book". The string is only accepted if it is followed by whitespace or dependent punctuation, or appears at the end of the
	 * string.
	 * @param text The text to search.
	 * @return The matched string, or <code>null</code> if there was no match.
	 */
	protected static String getETextString(final String text) {
		final String[] eTextStrings = {"Gutenberg's", BOOK, EDITION, EBOOK, ETEXT}; //any of these eText strings will work TODO test "Gutenberg's", use constant
		for(int i = eTextStrings.length - 1; i >= 0; --i) { //look at each of the etext strings
			final String eTextString = eTextStrings[i]; //get this etext string
			//see if the etext string is in the text
			final int eTextStringIndex = Strings.indexOfIgnoreCase(text, eTextString);
			if(eTextStringIndex >= 0) { //if the etext string is in the text
				if(eTextStringIndex + eTextString.length() == text.length() //make sure the string either comes at the end of the text
						//or the string comes before whitespace
						|| Character.isWhitespace(text.charAt(eTextStringIndex + eTextString.length()))
						//or if the string comes before dependent punctuation
						|| DEPENDENT_PUNCTUATION_CHARACTERS.contains(text.charAt(eTextStringIndex + eTextString.length()))) {
					//return the real string we found
					return text.substring(eTextStringIndex, eTextStringIndex + eTextString.length());
				}
			}
		}
		return null; //show that we didn't find an etext string
	}

	/**
	 * Extracts a Project Gutenberg identifier from the given filename.
	 * <p>
	 * Project Gutenberg files usually end in a one or two digit version number, optionally followed by a source letter (e.g. test12a). This method removes the
	 * file extension and removes the version number, returning the surrounding characters. If characters appeared after the version number, a hyphen ('-') will
	 * be inserted (e.g. test-a).
	 * </p>
	 * @param filename The string identifying the original file.
	 * @return The identifier of this Project Gutenberg work.
	 */
	public static String getID(final String filename) {
		//get the name of the file and remove its extension
		final StringBuilder stringBuilder = new StringBuilder(Filenames.removeExtension(filename));
		int versionEndIndex = stringBuilder.length(); //start assuming the version is at the end of the name
		//find the first digit on the right
		while(versionEndIndex > 0) { //while we haven't ran out of characters
			if(Character.isDigit(stringBuilder.charAt(versionEndIndex - 1))) { //if the previous character is a digit
				break; //we've found the end of the version
			} else { //if the character before this position isn't a digit
				--versionEndIndex; //look at the character before this
			}
		}
		//remove two digits in a row
		if(versionEndIndex > 0) { //if we found a digit
			int versionBeginIndex = versionEndIndex - 1; //we'll get at least one digit---maybe two
			//if the previous character is a digit as well
			if(versionBeginIndex > 0 && Character.isDigit(stringBuilder.charAt(versionBeginIndex - 1))) {
				--versionBeginIndex; //use two digits
			}
			if(versionEndIndex < stringBuilder.length()) { //if there are characters after the digits we're going to delete
				stringBuilder.insert(versionEndIndex, '-'); //insert a hyphen to separate the name from the other characters
			}
			stringBuilder.delete(versionBeginIndex, versionEndIndex); //remove the version from the string
		}
		return stringBuilder.toString(); //return the ID we constructed
	}

}
