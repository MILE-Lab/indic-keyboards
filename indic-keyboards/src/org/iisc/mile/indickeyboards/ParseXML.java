/** ********************************************************************
 * File:           ParseXML.java 
 * Description:    XML parser for Inscript and other layouts 
 * Authors:        Akshay,Abhinava,Revati,Arun 
 * Created:        Thu Mar 26 20:01:25 IST 2009
 *
 * (C) Copyright 2009, MILE Lab, IISc
 ** Licensed under the Apache License, Version 2.0 (the "License");
 ** you may not use this file except in compliance with the License.
 ** You may obtain a copy of the License at
 ** http://www.apache.org/licenses/LICENSE-2.0
 ** Unless required by applicable law or agreed to in writing, software
 ** distributed under the License is distributed on an "AS IS" BASIS,
 ** WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 ** See the License for the specific language governing permissions and
 ** limitations under the License.
 *
 **********************************************************************/

package org.iisc.mile.indickeyboards;

import java.io.File;
import java.io.FileNotFoundException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Processing for Non-Phonetic keyboard layouts. Processes the input and generates the
 * Unicode output which is to be sent to the current active window.
 */
public class ParseXML {

	/**
	 * String variable which holds the name of the keyboard layout currently
	 * selected. This is changed when user selects a different keyboard layout.
	 * Selection is done in UI.java using event listeners.
	 */
	public static String keyboardlayoutname;
	/*
	 * Flag variables used to keep track of type of keyboard Layout selected.
	 */
	/**
	 * Value of constant INSCRIPT_KB
	 */
	public static final int INSCRIPT_KB = 1;
	/**
	 * Value of constant OTHER_KB
	 */
	public static final int OTHER_KB = 0;
	/**
	 * Initalize the type of keyboard layout selected to 0. By default it is set
	 * to OTHER_KB
	 */
	public static int inscriptothers = 0;

	/**
	 * Flag variable used to keep track of the type of character keyed in -
	 * consonant or vowel. This is for the 2rd previous character.
	 */
	public static int previousConsonantFlaglog = 0;
	/**
	 * Flag variable used to keep track of the type of character keyed in -
	 * consonant or vowel. This is for the previous character.
	 */
	public static int previousConsonantFlag = 0;
	/**
	 * Flag variable used to keep track of the type of character keyed in -
	 * consonant or vowel. Current character keyed in.
	 */
	public static String currentconsonantflag;
	/**
	 * The character keyed in previously. The character itself is stored here.
	 * This is only to facilitate the processing of Tamil99 keyboard layout.
	 */
	public static String previousChar = "";
	/**
	 * The 2nd previous keystroke is stored here. This is only to facilitate the
	 * processing of Tamil99 Keyboard Layout for backspace
	 */
	public static String previousCharlog="";
	/**
	 * Counr variable which keeps track of the number of same consonant that is
	 * typed continuously. This is done for Tamil99 to provide the rule of
	 * applying halant for the consonant when even repetitions occur.
	 */
	public static int tamil99count = 0;

	public static String pattern;
	
	/**
	 * FOR NON-PHONETIC INPUT This method receives the current key pressed as a
	 * character and using the appropriate XML file, gets the corresponding
	 * Unicode which is to be printed instead of the english character.
	 * <p>
	 * This method reads the current selected layout which is stored in the
	 * variable "keyboardlayoutname" and loads the XML file for that layout.
	 * Next this checks whether the keyboard layout selected in inscript or not.
	 * For inscript layouts the processing involves direct concatination of
	 * unicodes. The processing of consonant-vowel, vowel-vowel,
	 * vowel-consonant, consonant-consonant is done here.
	 * 
	 * @param pattern
	 *            Has the ASCII character which has been pressed
	 */
	public void getPattern(String pattern) {
		ParseXML.pattern=pattern;
		/*
		 * This sets the flag to zero whenever the space key is pressed so as to
		 * prevent the dependent vowel to be printed (For layouts other than
		 * inscript keyboard layouts)
		 */
		if (pattern.compareTo(" ") == 0) {
			previousConsonantFlag = 0;
			tamil99count = 0;
		}

		/*
		 * This sets the flag to previous flag value whenever the backspace key
		 * is pressed so as to change the current state back to the previous
		 * state. Only keeps track of one backspace.
		 */
		if (pattern.compareTo("\b") == 0) {
			previousConsonantFlag = previousConsonantFlaglog;
			previousChar=previousCharlog;
		}

		try {
			/*
			 * Load the XML document into the memory. XML document name is
			 * specified by the "keyboardlayoutname" variable. If the selected
			 * layout is a user defined layout then the /userdefined/ is
			 * appended to the keyboard layout name.
			 */
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(new File(System
					.getProperty("user.dir"), "/kblayouts/"
					+ keyboardlayoutname));

			// normalize text representation
			doc.getDocumentElement().normalize();
			NodeList listOfPatterns = doc.getElementsByTagName("pattern");

			// @debug int totalPatterns = listOfPatterns.getLength();

			/*
			 * Loop to read the patterns one by one and to extract the requried
			 * information from the XML.
			 */
			for (int s = 0; s < listOfPatterns.getLength(); s++) {

				Node firstPatternNode = listOfPatterns.item(s);
				if (firstPatternNode.getNodeType() == Node.ELEMENT_NODE) {

					Element firstPatternElement = (Element) firstPatternNode;

					// -------
					NodeList charNameList = firstPatternElement
							.getElementsByTagName("char");
					Element charElement = (Element) charNameList.item(0);

					NodeList textFNList = charElement.getChildNodes();

					// @debug
					// System.out.println("The inputChar param =" + inputChar);
					if (pattern
							.equals(textFNList.item(0).getNodeValue().trim())) {
						/*
						 * @debug System.out.println("Char : " + ((Node)
						 * textFNList.item(0)).getNodeValue() .trim());
						 */

						// -------
						NodeList lastNameList = firstPatternElement
								.getElementsByTagName("unicode");
						Element lastNameElement = (Element) lastNameList
								.item(0);
						NodeList textLNList = lastNameElement.getChildNodes();

						/*
						 * Instead of printing the unicode here call the native
						 * code and print the unicode by passing it 
						 * as a parameter to the native code
						 */
						/*
						 * @debug System.out.println("Unicode : " + ((Node)
						 * textLNList.item(0)).getNodeValue() .trim());
						 */

						
						/*
						 * Check if the selected keyboard layout is inscript or
						 * other. If inscript then get the unicode from the XML
						 * and just directly print it onto the active window.
						 */
						if (inscriptothers == 1) {
							String ucodeValue = ((Node) textLNList.item(0))
									.getNodeValue().trim();
							// @Debug
							// System.out.println("String unicode is"+
							// ucodeValue);

							// delete echoed chars
							PhoneticParseXML.putbkspace();

							// For key presses which have multiple unicodes
							// Output depending on OS type
							if(IndicKeyboards.operatingSystem.contains("Windows"))
							{
								
								org.iisc.mile.indickeyboards.windows.OutputCharToActiveWindow.getcharforop_nonPhonetic(ucodeValue);
							}
							else
							{
								org.iisc.mile.indickeyboards.linux.OutputCharToActiveWindow.outputToActiveWindowNonPhonetic(ucodeValue);
							
							}
							
							
						} else {
							/*
							 * If the keyboardlayout selected in not inscript
							 * type, simple concatination will not work.
							 */

							String ucodeValue;
							/*
							 * Use the putbkspace method to put a backspace onto
							 * the current active window. This is used to remove
							 * the English character that is put in the active
							 * window.
							 */
							PhoneticParseXML.putbkspace();
							/*
							 * Read the value of the tag <consonant> from the
							 * XML. This states whether the keyed character in a
							 * consonant or a vowel. It is stored in the
							 * variable "currenconsonantflag".
							 */
							NodeList vovelList = firstPatternElement
									.getElementsByTagName("consonant");
							Element vovelElement = (Element) vovelList.item(0);
							NodeList vovelEtext = vovelElement.getChildNodes();
							currentconsonantflag = ((Node) vovelEtext.item(0))
									.getNodeValue().trim();
							// @debug

							System.out.println("Previous Flag:"
									+ previousConsonantFlag);
							System.out.println("Flag :"
									+ ((Node) vovelEtext.item(0))
											.getNodeValue().trim());

							/*
							 * If a vowel follows a consonant, then the
							 * dependent vowel is to be printed instead of the
							 * independent vowel. Check whether previous input
							 * is consonant and current input is a vowel.
							 */
							if (previousConsonantFlag == 1
									&& Integer.valueOf(currentconsonantflag) == 0) {
								// Get the dependent vowel Unicode. Read 2nd
								// unicode from the XML.
								NodeList uniList = firstPatternElement
										.getElementsByTagName("uni2");
								Element uniEle = (Element) uniList.item(0);
								NodeList uniEdepList = uniEle.getChildNodes();
								ucodeValue = ((Node) uniEdepList.item(0))
										.getNodeValue().trim();
								
								// For tamil99
								/*
								 * if (ParseXML.keyboardlayoutname
								 * .compareTo("tamil99.xml") == 0) {
								 * PhoneticParseXML.putbkspace();
								 * System.out.println("Knock off halant"); }
								 */
								tamil99count=0;
							} else {
								/*
								 * If vowel doesn't follow a consonant then put
								 * the independent vowel unicode directly. Here
								 * there is no need to again read the XML for
								 * the 2nd unicode.
								 */
								ucodeValue = ((Node) textLNList.item(0))
										.getNodeValue().trim();
								/*
								 * For tamil99 only. This code is to first check
								 * whether the input character is a consonant
								 * and the selected layout is Tamil99. If yes
								 * then check whether the consonant the same as
								 * that of the previous input and also check the
								 * number of times the same input is repeated.
								 * If it is even number of times then output the
								 * halant first and then the consonant.
								 * The last && condition in the if statement is used to check the nasal + consonant = nasal + halant + consonant rule
								 */
								if (ParseXML.keyboardlayoutname
										.compareTo("tamil99.xml") == 0
										&& Integer
												.valueOf(currentconsonantflag) == 1
										&& previousConsonantFlag == 1
										&& pattern.compareTo("f") != 0
										&& (previousChar.compareTo(pattern) == 0 || (pattern.compareTo("h")==0 && previousChar.compareTo("b")==0)
																				 || ((pattern.compareTo("[")==0 || pattern.compareTo("E")==0) && previousChar.compareTo("]")==0)
																				 || (pattern.compareTo("j")==0 && previousChar.compareTo("k")==0)
																				 || (pattern.compareTo("l")==0 && previousChar.compareTo(";")==0)
																				 || (pattern.compareTo("o")==0 && previousChar.compareTo("p")==0)
										    )){
									
									if (tamil99count % 2 == 0) {
										ucodeValue = "0bcd" + ucodeValue;
										System.out
												.println("Halant to be printed!");
									}
									tamil99count++;
								}else
								if (ParseXML.keyboardlayoutname
										.compareTo("tamil99.xml") == 0
										&& Integer
												.valueOf(currentconsonantflag) == 1
										&& previousConsonantFlag == 1
										&& pattern.compareTo("f") != 0){
									//ucodeValue="0bcd"+ucodeValue;
									tamil99count=0;
									if(tamil99count%2!=0)
										ucodeValue="0bcd"+ucodeValue;
								}
								
							}
							// @Debug
							System.out.println("String unicode is " + ucodeValue);

							// Output depending on Operating System
							if(IndicKeyboards.operatingSystem.contains("Windows"))
							{
								org.iisc.mile.indickeyboards.windows.OutputCharToActiveWindow.getcharforop_nonPhonetic(ucodeValue);
							}
							else
							{
								org.iisc.mile.indickeyboards.linux.OutputCharToActiveWindow.outputToActiveWindowNonPhonetic(ucodeValue);
							
							}
							
							/*
							 * Set the flags. Make the value of
							 * currentconsonantflag to previous, the previous to
							 * 2nd previous.
							 */
							previousConsonantFlaglog = previousConsonantFlag;
							previousConsonantFlag = Integer
									.valueOf(currentconsonantflag);
							previousCharlog=previousChar;
							previousChar = pattern;

						}// end of else of if(inscriptothers==1)

						// @Debug
						// System.out.println("float unicode i"+ i);

						//core.OutputUCode.outputCode(((Node)textLNList.item(0))
						// .getNodeValue().trim());
					}
				}// end of if clause

			}// end of for loop with s var

		} catch (SAXParseException err) {
			System.out.println("** Parsing error" + ", line "
					+ err.getLineNumber() + ", uri " + err.getSystemId());
			System.out.println(" " + err.getMessage());

		} catch (SAXException e) {
			Exception x = e.getException();
			((x == null) ? e : x).printStackTrace();

		} catch (FileNotFoundException fnf) {
			System.out.println("Select a keyboard layout first!!");

		} catch (Throwable t) {
			t.printStackTrace();
		}

		// System.exit (0);

	}// End of getPattern

	/**
	 * Sets the keyboard layout that has been selected by the user. This
	 * information is used to open the selected keyboard layout XML file during
	 * operation.
	 * <p>
	 * If the keyboard is inscript type then the "inscriptothers" variable is
	 * set to INSCRIPT_KB else it is set to OTHER_KB. THis is required to
	 * facilitate the processing of the keystrokes.
	 * 
	 * @param name
	 *            Contains the name of the keyboard layout selected. It is the
	 *            same as that of the filename
	 */
	public static void setlang(String name) {
		keyboardlayoutname = name;
		if (keyboardlayoutname.contains("inscript") == true)
			inscriptothers = INSCRIPT_KB;
		else
			inscriptothers = OTHER_KB;

	}

	/**
	 * This method is used to load the Library which contains the native code
	 * responsible for printing the unicodes onto the active window.
	 */
	

}