package com.garretwilson.text.xml.xhtml;

import java.io.*;
import java.util.*;
import com.garretwilson.io.*;
import com.garretwilson.lang.CharSequenceUtilities;
import com.garretwilson.text.*;
import com.garretwilson.text.xml.XMLUtilities;
import com.garretwilson.util.Debug;
import org.w3c.dom.*;

import static com.garretwilson.text.CharacterConstants.*;

/**Allows creation of an XHTML document from a text document. Each group of
	lines is converted to an XHTML <code>&lt;p&gt;</code>.
@author Garret Wilson
*/
public class XHTMLCreator extends TextUtilities implements XHTMLConstants
{
	protected long totalLineCount=0;  //G***testing; comment; tidy
	protected long totalLineLengthSum=0;

	/**The list of buffered lines for analyzing.*/
	private final LinkedList lineBuffer=new LinkedList();

		/**@return The list of buffered lines for analyzing.*/
		public LinkedList getLineBuffer() {return lineBuffer;}

	/**The number of lines used per line of text.*/
	private int lineSpacing=1;

		/**@return The number of lines used per line of text.*/
		protected int getLineSpacing() {return lineSpacing;}

		/**Sets the number of lines used per line of text.
		@param newLineSpacing The new line spacing value.
		*/
		protected void setLineSpacing(final int newLineSpacing) {lineSpacing=newLineSpacing;}

	/**Whether paragraphs should be sensed even without blank lines.*/
	private boolean isParagraphSensing=false;

		/**@return Whether paragraphs should be sensed even without blank lines.*/
		protected boolean isParagraphSensing() {return isParagraphSensing;}

		/**Sets whether paragraphs should be sensed even without blank lines.
		@param newParagraphSensing Whether paragraphs should be sensed.
		*/
		protected void setParagraphSensing(final boolean newParagraphSensing) {isParagraphSensing=newParagraphSensing;}

	/**Whether the text should be interpreted as UTF-8.*/
	private boolean isUTF8=false;

		/**@return Whether the text should be interpreted as UTF-8.*/
		protected boolean isUTF8() {return isUTF8;}

		/**Sets whether the text should be interpreted as UTF-8.
		@param newUTF8 Whether text should be UTF-8 unencoded.
		*/
		protected void setUTF8(final boolean newUTF8) {isUTF8=newUTF8;}

	/**Default constructor.*/
	public XHTMLCreator()
	{
	}

	/**Creates an XHTML document from the text retrieved from the given input
		stream, using ISO 8859-1 as the default encoding.
	@param document The XHTML document already containing a <code>&lt;body&gt;</code>
		element in which the converted text content will be stored.
	@param inputStream The input stream that contains the text document.
	@exception IOException Thrown if there is an I/O error.
	*/
	public void createXHTMLFromText(final Document document, final InputStream inputStream) throws IOException
	{
		createXHTMLFromText(document, inputStream, CharacterEncodingConstants.ISO_8859_1);  //create the XHTML using the default encoding
	}

	/**Creates an XHTML document from the text retrieved from the given input
		stream using the specified encoding.
	@param document The XHTML document already containing a <code>&lt;body&gt;</code>
		element in which the converted text content will be stored.
	@param inputStream The input stream that contains the text document.
	@param encoding The encoding to use when interpreting the text contents.
	@exception IOException Thrown if there is an I/O error.
	*/
	public void createXHTMLFromText(final Document document, final InputStream inputStream, final String encoding) throws IOException
	{
			//see if we can detect an encoding from a byte order mark
		final CharacterEncoding detectedEncoding=InputStreamUtilities.getBOMEncoding(inputStream);
//G***del		final String detectedEncoding=null;
Debug.trace("detected encoding: ", detectedEncoding);  //G***del
Debug.trace("constructing buffered reader to text input stream"); //G***del
		getLineBuffer().clear();  //clear our list of lines
		  //create a reader to read the information in using the correct input encoding G***change the encoding to what Java understands
		final BufferedReader bufferedReader=new BufferedReader(
			  new InputStreamReader(inputStream, detectedEncoding!=null ? detectedEncoding.toString() : encoding));
		autodetectSettings(bufferedReader);  //calculate the line spacing used
		parseText(document, bufferedReader);  //parse the text
	}

	/**Parses a text file from a reader and outputs an OEB file.
	@param document The XHTML document already containing a <code>&lt;body&gt;</code>
		element in which the converted text content will be stored.
	@param bufferedReader The buffered reader which contains the text.
	@exception IOException Thrown if there is an I/O error.
	*/
	protected void parseText(final Document document, final BufferedReader bufferedReader) throws IOException
	{
Debug.trace("parsing text");  //G***del
		final Element bodyElement=XHTMLUtilities.getBodyElement(document);  //get a reference to the body element
		assert bodyElement!=null : "Missing body element";  //we should always have a body element starting out
Debug.trace("found body element");  //G***del
		Element paragraphElement=parseParagraph(document, bodyElement.getNamespaceURI(), bufferedReader);	//parse the first paragraph
		while(paragraphElement!=null)	//keep reading until we run out of paragraphs
		{
			XMLUtilities.appendText(bodyElement, "\n");	//append a newline to separate the paragraphs
			bodyElement.appendChild(paragraphElement);	//add the paragraph element to our document
			paragraphElement=parseParagraph(document, bodyElement.getNamespaceURI(), bufferedReader);	//parse the next paragraph
		}
		XMLUtilities.appendText(bodyElement, "\n");	//append a newline to end the content of the body element
		XMLUtilities.appendText(document.getDocumentElement(), "\n");	//append a newline to end the content of the html element
//G***del XMLUtilities.printTree(document, Debug.getOutput());  //G***del
	}

	/**Parses text and returns a paragraph element with the next subsequent non-empty
		lines of text.
	@param document The document to be used to create the paragraph element.
	@param namespaceURI The XHTML namespace to use.
	@param bufferedReader The buffered reader to be used to read the text.
	@return A paragraph, or <code>null</code> if the end of the data was reached
		without finding any text data.
	@exception IOException Thrown if there is an I/O error.
	*/
	protected Element parseParagraph(final Document document, final String namespaceURI, final BufferedReader bufferedReader) throws IOException
	{

//G***del Debug.trace("parsing paragraph"); //G***del
//G***del		boolean foundBreak=false; //we'll change this when we find a break
		final Element element=document.createElementNS(namespaceURI, ELEMENT_P);	//create a new paragraph element G***this assumes the namespace is the default name
		String line=getLine(bufferedReader);	//read the first line of text
//G***del Debug.trace("read line: ", textLine); //G***del
		while(line!=null)	//read all the blank lines until we find one with text (or until we read the end of the data)
		{
			if(CharSequenceUtilities.notCharIndexOf(line, WHITESPACE_CHARS)>=0)		//if there is text for this line
				break;	//we're ready to start storing the text
		  line=getLine(bufferedReader);	//read the next line of text
		}
//G***del		final int lineSpacing=getLineSpacing();  //get the number of lines per line of text we expect
		int lineCount=0;  //we haven't added any lines to the paragraph, yet
		int lineNumber=1; //this will keep track of the line number we're on, as far as linespacing goes
		int lineLengthSum=0;  //we'll keep track of the sum of line lengths that we've added
		boolean isLastLine=false; //we don't think this is the last line
		int headingType=NO_HEADING; //we don't think this paragraph is a heading
//G***del		String lineToAppend=null;	//this will hold the line to append each time
		while(line!=null && !isLastLine)	//keep reading until we read the end of the data or we we hit the last line of the paragraph
		{
				//make sure all the characters are valid XML characters
			line=XMLUtilities.createValidString(line);
			if(CharSequenceUtilities.notCharIndexOf(line, WHITESPACE_CHARS)>=0)		//if there is text for this line
			{
//G***del do we need to trim the line?				final String lineToAppend=line.trim();	//we'll append this line (trimmed) to the paragraph; we won't know until later whether we should add a newline
//G***del do we need to trim the line?				if(isBreak(lineToAppend)) //if the line we want to append is a break
				if(isBreak(line) && line.length()>10) //if the line we want to append is a break
				{
					if(lineCount>0) //if we already have lines accumulated
					{
						ungetLine(line);  //put the line back in the buffer
						break;  //return what we have so far, and come back later for this break
//G***del						return element; //return what we have so far, and come back later for this break
					}
					else  //if we haven't accumulated any lines
					{
						XMLUtilities.appendText(element, line); //append the break text to the element
						break;  //return the break element
//G***del						return element; //return the break element
					}
				}
					//see if this group of text starts with a heading, and if we need to detach that heading
				if(lineCount==0)  //if we haven't added any lines yet
				{
//G***del Debug.trace("getting heading type for line: ", line); //G***del
				  final int possibleHeadingType=getHeadingType(line); //find out if this line appears to be a heading
					  //title headings are too risky to break out---a couple of proper
						//  names on the last line of a paragraph would look like a title
						//  heading (see gabrm10.txt)
					if(possibleHeadingType!=TITLE_HEADING)  //if this isn't a title heading
					{
					  headingType=possibleHeadingType;  //we'll suppose this really is a heading
//G***del Debug.trace("changed heading type: ", headingType); //G***del
					}
				}
				else if(headingType!=NO_HEADING)  //if this isn't the first line and we have a heading
				{
//G***del Debug.trace("other line heading type: ", getHeadingType(line)); //G***del
				  final int possibleHeadingType=getHeadingType(line); //find out if this line appears to be a heading
					if(possibleHeadingType!=headingType) //if this line is of a different heading type (or no heading type)
					{
/*G***fix; this partially solves the problem, but then how does the XHTMLTidier know if this is a paragraph or a heading?
						final float averageLineLength=lineLengthSum/lineCount;  //find out the average line length
							//if we've had the heading for more than two lines, or if this line s signficantly longer than the first one
						if(lineCount>2 || line.length()>averageLineLength*1.10)
						{
*/

/*G***del; this was mistakenly added---this doesn't count if we already have a heading
							//title headings and subheadings are too risky to break out in
							//  the middle of a paragraph---a couple of proper names on the
							//  last line of a paragraph would look like a title heading
							//  (see gabrm10.txt), as would a capitalized phrase on the end
							//  of a line (see rslas10.txt)
						if(possibleHeadingType!=TITLE_HEADING && possibleHeadingType!=SUB_HEADING)  //if this isn't a title heading or a subheading
						{
*/
							ungetLine(line);  //put the line back in the buffer
						  break;  //return what we have so far, and come back later for this text (which could be the start of a paragraph after a heading, for instance)
//G***del						}
/*G***fix; this partially solves the problem, but then how does the XHTMLTidier know if this is a paragraph or a heading?
						}
						else  //if this is the second line and it isn't much longer than the first, this probably isn't a header
						{
							headingType=NO_HEADING; //we didn't really have a heading in this paragraph
						}
*/
					}
				}
				if(isParagraphSensing())  //if we have to sense paragraphs other than by blank lines
				{
					if(isPageNumber(line)) //if this line is a page number
					{
						line=null;  //discard the line
					}
					else if(lineCount>0) //if we have lines in the paragraph
					{
							//get the index of the first non-whitespace character
						final int firstCharIndex=CharSequenceUtilities.notCharIndexOf(line, WHITESPACE_CHARS);
						final char firstChar=line.charAt(firstCharIndex);  //get the first character
							//get the index of the last non-whitespace character
						final int lastCharIndex=CharSequenceUtilities.notCharLastIndexOf(line, WHITESPACE_CHARS);
						final char lastChar=line.charAt(lastCharIndex);  //get the last character
						if(headingType==NO_HEADING) //if this is not a heading, we still need to sense the start of a new heading within the paragraph (if the paragraph started with a heading, we've already checked to see if it has changed types)
						{
							final int lineHeadingType=getHeadingType(line); //see what type of heading this is
							if(lineHeadingType!=NO_HEADING) //if we've started a heading in the middle of the paragraph
							{
									//we have to be extra careful when breaking a paragraph apart for supposed headings
									//don't break for title headings that start or end with phrase punctuation
								if(lineHeadingType!=TITLE_HEADING ||
									  (PHRASE_PUNCTUATION_CHARS.indexOf(firstChar)<0
											  && PHRASE_PUNCTUATION_CHARS.indexOf(lastChar)<0))
								{
									ungetLine(line);  //put the line back in the buffer
									break;  //return what we have so far, and come back later for the heading text
								}
							}
						}
//G***del Debug.trace("sensing line: ", line);
//G***del Debug.trace("first character: "+firstChar);
						if(LEFT_QUOTE_CHARS.indexOf(firstChar)>=0) //if the first character is a quote, this is the start of a new line
						{
//G***del Debug.trace("is quoted string; stopping paragraph");
							ungetLine(line);  //put the line back in the buffer
							break;  //return what we have so far, and come back later for the quoted line
						}
							//next, check to see if this is the last line of the current paragraph
						final float averageLineLength=(float)lineLengthSum/lineCount; //get the average length so far
						final float expectedLength=averageLineLength*0.75f; //see how long we expect the line to be to be considered part of the paragraph body G***use a constant
						final float probablyLength=averageLineLength*0.85f; //see how long we expect the line to be to be considered part of the paragraph if the line ends in punctuation
						  //find out the length of the actual characters in the string (we don't have to worry about blank lines, because we've already checked for that)
						final int textLength=lastCharIndex-firstCharIndex+1;
						if(textLength<expectedLength //if there aren't enough characters in this line
											//or if the line is almost as long as the average, but ends in non-dependent phrase punctuation (such as '.')
							  || (textLength<probablyLength && PHRASE_PUNCTUATION_CHARS.indexOf(lastChar)>=0
											&& DEPENDENT_PUNCTUATION_CHARS.indexOf(lastChar)<0))
						{
								//get the next line to make sure it's not a page number
							String nextLine=getLine(bufferedReader);  //get the next line
							if(nextLine!=null && isPageNumber(nextLine))  //if the next line is a page number
							{
								;  //do nothing---just discard the next line and keep going (but we'll still add this line) G***this can probably be made more efficient and logical
							}
							else  //if the next line is not a page number
							{
								if(nextLine!=null)  //if we got a real line
								  ungetLine(nextLine);  //put the next line back---the current line is really the end of the paragraph
								isLastLine=true;  //show that this will be the last line to add
							}
						}
					}
				}
				if(line!=null)  //if we have a line to add (that is, we haven't thrown the line away)
				{
					if(lineCount>0) //if we've already added lines
						XMLUtilities.appendText(element, "\n"); //append a newline to the paragraph G***is there something more efficient and more spatially economic
					XMLUtilities.appendText(element, line); //append the line of text to the element
					++lineCount;  //show that we have more lines in the paragraph
					++totalLineCount;  //update our total line count
					lineNumber=2; //we're back to the first line in the linespacing (actually, this was the first line---the next will be the second)
					lineLengthSum+=line.length(); //add the line length to our sum of lengths
					totalLineLengthSum+=line.length();  //add this line length to our total sum of lengths
/*G***testing; might be useful later
					if(lineCount%15==0) //every 15 lines of a paragraph, see if we need to recalculate linespacing
					{
							//get the average length of all the lines
						final float totalAverageLineLength=(float)totalLineLengthSum/totalLineCount;
Debug.trace("total average line length: "+totalAverageLineLength); //G***del
							//get the average line length of the current paragraph
						final float averageLineLength=(float)lineLengthSum/lineCount;
Debug.trace("paragraph average line length: "+averageLineLength); //G***del
						  //if our paragraph length is less than have or greater than twice the total average
						if(averageLineLength<totalAverageLineLength/2 || averageLineLength>totalAverageLineLength*2)
						{
Debug.trace("autodetecting settings again");  //G***del
						  autodetectSettings(bufferedReader); //autodetect our settings again G***then maybe unread our paragraphs
						}
					}
*/
				}
			}
			else	//if there is no text on this line
			{
				if(lineNumber>getLineSpacing())  //if we've already used up all the blank lines we expect for this line spacing
				  break;	//we're finished with the paragraph
				else  //if we expected this blank line because of the line spacing
					++lineNumber; //go to the next line number
			}
			if(!isLastLine) //if we haven't reached the last line of text
			  line=getLine(bufferedReader);	//read another line of text
		}
		return element.getChildNodes().getLength()!=0 ? element : null;	//if we found any non-empty lines of text, return the paragraph element, else return null
			//G***remove the last '\n' from the paragraph
//G***del		return element.getChildNodes().getLength()!=0 ? element : null;	//if we found any non-empty lines of text, return the paragraph element, else return null
	}

	/**Determines if the given text is a break.
		A break is either comprised completely of '*' and/or '-', or is the
		word "page" on a single line surrounded by only punctuation and/or whitespace.
	@param text The text to check.
	@return <code>true</code> if the text is a break.
	*/
/*G***del; combined with TextUtilities.isBread()
	protected static boolean isBreak(final String text) //G***somehow combine this with TextUtilities.isPageBreakHeading()
	{
//G***del Debug.trace("checking text for page break: ", text);  //G***del
//G***del Debug.trace("stripped of punctuation and whitespace: ", StringUtilities.trim(text, PUNCTUATION_CHARS+CharacterConstants.WHITESPACE_CHARS));  //G***del
		if(StringUtilities.isAllChars(text, "*-"+EM_DASH_CHAR+EN_DASH_CHAR) && text.length()>10)  //if the string is only made up of asterisks and hyphens and is a certain length G***use a constant
			return true;  //this is a page break heading
				//if this line contains "page" surrounded by only punctuation
		else if("page".equalsIgnoreCase(StringUtilities.trim(text, PUNCTUATION_CHARS+WHITESPACE_CHARS)))
			return true;  //this is a page break indication
		else  //if this is not a page break heading
			return false; //show that we don't think this is a page break heading
	}
*/

	/**Gets the next available line, either from the buffer or from the given
		reader.
	@param bufferedReader The source of the lines if there are none in the buffer.
	@return The next line, or <code>null</code> if there are no lines left.
	@exception IOException Thrown if there is an I/O error.
	*/
	protected String getLine(final BufferedReader bufferedReader) throws IOException
	{
		if(getLineBuffer().size()>0)  //if there are lines in the buffer
		{
			return (String)getLineBuffer().removeFirst();  //remove and return the first available line from the buffer
		}
		else  //if there are no lines in the buffer
		{
			return bufferedReader.readLine(); //read a line from the reader
		}
	}

	/**Puts a line back into the buffer as the next available line.
	@param line The line to unget.
	*/
	protected void ungetLine(final String line)
	{
		getLineBuffer().addFirst(line); //add the line to the first of the buffer
	}

	/**Preloads a number of lines into the buffer.
	@param bufferedReader The source of the lines.
	@exception IOException Thrown if there is an I/O error.
	*/
	protected void primeBuffer(final BufferedReader bufferedReader) throws IOException
	{
		String line=bufferedReader.readLine();  //read a line from the buffer
		while(line!=null && getLineBuffer().size()<2000)  //while we have lines to add, and we haven't yet have enough in the buffer G***use a constant here
		{
			getLineBuffer().addLast(line);  //add the line to the end of our buffer
		  line=bufferedReader.readLine();  //read another line from the buffer
		}
Debug.trace("primed buffer, number of lines: ", getLineBuffer().size());
	}

	/**Calculates the line spacing used by the file. Paragraph sensing is turned
		on if there are too low a number of blank lines in relation to the total
		number of lines.
	@param bufferedReader The source of the lines.
	@see #setLineSpacing
	@see #isParagraphSensing
	@exception IOException Thrown if there is an I/O error.
	*/
	protected void autodetectSettings(final BufferedReader bufferedReader) throws IOException //G***fix the cases in which each paragraph appears on one line--i.e. check the average line length
	{
		primeBuffer(bufferedReader);  //preload the buffer with lines
		tidyBuffer(); //tidy the buffer of long line runs (important for Project Gutenberg truth10.txt, for example)
		final int totalLineCount=getLineBuffer().size()*2/3;  //we'll use part of the buffer contents
		final int startIndex=getLineBuffer().size()/4;  //we'll start part of the way through the buffer
		final int endIndex=startIndex+totalLineCount;  //we'll look at a
		int lineCount=0;  //we haven't found any lines, yet
		int nonBlankLineCount=0;  //we haven't found any non-blank lines, yet
		int endingPunctuationSum=0;  //the sum of lines that end in punctuation
		for(int i=startIndex; i<endIndex; ++i)  //look at each line following each non-blank line
		{
		  final String line=(String)getLineBuffer().get(i); //get another line
//G***del System.out.print("Line "+i+": "+line);  //G***del
			++lineCount;  //show that we've found another line
			  //get the index of the last character in the string that isn't whitespace
			final int lastNonWhitespaceCharIndex=CharSequenceUtilities.notCharLastIndexOf(line, WHITESPACE_CHARS);
			if(lastNonWhitespaceCharIndex>=0) //if this is not a blank line
			{
				++nonBlankLineCount;  //show that we've found another non-blank line
				final char lastChar=line.charAt(lastNonWhitespaceCharIndex); //get the last character on the line
					//if there is independent punctuation at the end of this line
				if(PUNCTUATION_CHARS.indexOf(lastChar)>=0 && DEPENDENT_PUNCTUATION_CHARS.indexOf(lastChar)<0)
				  ++endingPunctuationSum; //show that we found another line with ending punctuation
			}
		}
Debug.trace("line count: ", lineCount); //G***del
Debug.trace("nonblank line count: ", nonBlankLineCount); //G***del
			//get the number of lines used for each non-blank line
		final float linesPerLine=(float)lineCount/(float)nonBlankLineCount;
Debug.trace("lines per line: "+linesPerLine); //G***del
//G***del		  //only use this line spacing if it's within our recogni
//G***del		final int lineSpacing=Math.round(linesPerLine); //determine the line spacing

		  //see the fraction of lines that end in punctuation
		final float endingPunctuationFraction=(float)endingPunctuationSum/nonBlankLineCount;
Debug.trace("ending punctuation sum: ", endingPunctuationSum);  //G***del
Debug.trace("ending punctuation fraction: "+endingPunctuationFraction);  //G***del
		final int lineSpacing; //determine the line spacing differently depending on whether we think paragraphs are each on one long line or not
		if(endingPunctuationFraction>0.85f) //if most lines end in punctuation, each line is probably a paragraph
		{
			lineSpacing=1;  //one-line paragraphs always have single line spacing
		}
		else  //if most lines don't end in punctuation, each paragraph is probably made up of multiple lines
		{
		  lineSpacing=(int)linesPerLine; //determine the line spacing by dropping the decimal portion---do *not* round, because partial lines per line do not count
		}
		setLineSpacing(lineSpacing);  //set the line spacing
Debug.trace("line spacing: ", getLineSpacing()); //G***del
		  //normalize the lines-per-line measurement by subtracting out the lines
			//  expected for line spacing
		final float normalizedLinesPerLine=((float)lineCount-nonBlankLineCount*(lineSpacing-1))/(float)nonBlankLineCount;
Debug.trace("normalized lines per line: "+normalizedLinesPerLine); //G***del
		//we expect a certain number of blank lines to be used just for paragraphs;
		//  if not, that means there's not enough blank lines, so we'll have to
		//  sense paragraphs (note that 1.2 is too high a threshold for works like
		//  Project Gutenberg rlas10.txt (1.16))
//G***del; this is too high		setParagraphSensing(normalizedLinesPerLine<1.2);  //set the paragraph sensing
//G***del; this is too high		setParagraphSensing(normalizedLinesPerLine<1.1);  //set the paragraph sensing
//G***del; this is still too high		setParagraphSensing(normalizedLinesPerLine<1.05);  //set the paragraph sensing
		setParagraphSensing(normalizedLinesPerLine<1.04);  //set the paragraph sensing
Debug.trace("paragraph sensing: "+isParagraphSensing()); //G***del
	}

	/**Collapses large runs of blank lines in the buffer, which would skew our
		line spacing calculations
	*/
	protected void tidyBuffer()
	{
		final int MAX_BLANK_LINE_RUN_LENGTH=5;  //the maximum number of blank lines we'll accept in a row
		int lineCount=0;  //we haven't found any lines, yet
		int nonBlankLineCount=0;  //we haven't found any non-blank lines, yet
		int blankLineRunStartIndex=-1;  //show that we haven't started a run of blank lines, yet
		for(int i=0; i<getLineBuffer().size(); ++i)  //look at each line
		{
		  final String line=(String)getLineBuffer().get(i); //get another line
			++lineCount;  //show that we've found another line
			final boolean isBlankLine=CharSequenceUtilities.notCharIndexOf(line, WHITESPACE_CHARS)<0; //see if this is a blank line
			if(!isBlankLine)  //if this isn't a blank line
				++nonBlankLineCount;  //show that we've found another non-blank line
			if(blankLineRunStartIndex<0)  //if we haven't started a run, yet
			{
				if(isBlankLine) //if this is a blank line
				{
//G***del Debug.trace("starting blank line run at index: ", i); //G***del
					blankLineRunStartIndex=i; //show that this is where the run starts
				}
			}
			else  //if we're in the middle of a run
			{
				if(!isBlankLine || i==getLineBuffer().size()-1) //if this is not another blank line, or we're out of lines altogether (which will skew our count by one line, but it's the end of the buffer so we don't know the real end of the run, anyway)
				{
//G***del Debug.trace("end of run at index: ", i); //G***del
					final int blankLineRunLength=i-blankLineRunStartIndex;  //see how long this run was
//G***del Debug.trace("run length: ", blankLineRunLength); //G***del
					if(blankLineRunLength>MAX_BLANK_LINE_RUN_LENGTH)  //if this run was too long
					{
							//get the number of lines used for each non-blank line on average
						final float linesPerLine=(float)lineCount/(float)nonBlankLineCount;
		  		  final int lineSpacing=(int)linesPerLine; //determine the line spacing so far by dropping the decimal portion---do *not* round, because partial lines per line do not count
//G***del Debug.trace("before collapsing, current line spacing is: ", lineSpacing); //G***del
//G***del Debug.trace("collapsing a blank line run of: ", blankLineRunLength);  //G***del
						  //we'll leave as many blank lines as the current average line spacing would suggest
						final int newBlankLineRunStartIndex=blankLineRunStartIndex+lineSpacing-1;
						  //look at each blank line, from the last to the first, skipping the first
						for(int blankLineIndex=i-1; blankLineIndex>newBlankLineRunStartIndex; --blankLineIndex)
						{
//G***del Debug.trace("removing index: ", blankLineIndex);  //G***del
							getLineBuffer().remove(blankLineIndex); //remove this blank line
						}
						i=newBlankLineRunStartIndex; //we've collapsed everything down to one line, so we're on the first line of the run again (compensating for line spacing)
					}
					blankLineRunStartIndex=-1; //show that we're not in a run anymore
				}
			}
		}
	}

	/**Determines if the UTF-8 encoding should be used based upon a sample of
		lines in the buffer. If so, the lines in the buffer are converted to
		UTF-8.
	@param bufferedReader The source of the lines.
	@see #setLineSpacing
	@see #isParagraphSensing
	@exception IOException Thrown if there is an I/O error.
	*/
	protected void autodetectEncoding(final BufferedReader bufferedReader) throws IOException //G***fix the cases in which each paragraph appears on one line--i.e. check the average line length
	{
		primeBuffer(bufferedReader);  //preload the buffer with lines
		tidyBuffer(); //tidy the buffer of long line runs (important for Project Gutenberg truth10.txt, for example)
	}

}