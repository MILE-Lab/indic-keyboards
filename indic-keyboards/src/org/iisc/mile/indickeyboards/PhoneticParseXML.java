/** ********************************************************************
 * File:           PhoneticParseXML.java 
 * Description:    XML parser for Phonetic input 
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

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Processing for Phonetic Language Input. Processes the input and generates the Unicode
 * output which is to be sent to the current active window.
 */
public class PhoneticParseXML {
	/**
	 * String variable which holds the name of the keyboard layout currently
	 * selected. This is changed when user selects a different keyboard layout.
	 * Selection is done in UI.java using event listeners.
	 */
	public static String Phkeyboardlayoutname;

	/**
	 * Keep track of whether phonetic input is selected or not. Initialized to 0
	 * meaning at startup by default phonetic is not selected.
	 */
	public static int PhoneticFlag = 0;
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
	 * This variable holds the Unicode value of halant in the selected language
	 * keyboard layout.
	 */
	public static String halant;
	/**
	 * The character keyed in previously. The character itself is stored here.
	 * This is only to facilitate the processing of Tamil99 keyboard layout.
	 */
	public static String previousChar = "";
	/**
	 * Holds the temporary unicode value. This is used to facilitate the two
	 * character input for Phonetic. That is, for example the combination "s" +
	 * "h" is used to hold a single Indic Character.
	 */
	public static StringBuilder tmp;
	/**
	 * Flag to keep track whether the pressed key that is the input is a
	 * character "a". When the character a is encountered, the half-consonant
	 * has to be replaced with a full consonant. Eg: "ka" is a full consonant.
	 */
	public static int aflag = 0;

	/**
	 * FOR PHONETIC INPUT This method receives the current key pressed as a
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
	public void getPhoneticPattern(String pattern) {
		
		// @debug
		System.out.println("Phonetic Mode");
		/*
		 * This sets the flag to zero whenever the space key is pressed so as to
		 * prevent the dependent vowel to be printed (For layouts other than
		 * inscript keyboard layouts)
		 */
		if (pattern.compareTo(" ") == 0) {
			previousConsonantFlag = 0;
		}
		/*
		 * This sets the flag to previous flag value whenever the backspace key
		 * is pressed so as to change the current state back to the previous
		 * state. Only keeps track of one backspace.
		 */
		if (pattern.compareTo("\b") == 0) {
			previousConsonantFlag = previousConsonantFlaglog;
		}
		// To set the unicode of the halant to static string variable halant
		halant = getunicode("halant");
		// @debug
		System.out.println("Halant: " + halant);
		try {
			/*
			 * Load the XML document into the memory. XML document name is
			 * specified by the "keyboardlayoutname" variable.
			 */
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(new File(System
					.getProperty("user.dir"), "/kblayouts/"
					+ Phkeyboardlayoutname));

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

						String ucodeValue;
						NodeList vovelList = firstPatternElement
								.getElementsByTagName("consonant");
						Element vovelElement = (Element) vovelList.item(0);
						NodeList vovelEtext = vovelElement.getChildNodes();
						currentconsonantflag = ((Node) vovelEtext.item(0))
								.getNodeValue().trim();
						System.out.println("Previous Flag:"
								+ previousConsonantFlag);
						System.out.println("Flag :"
								+ ((Node) vovelEtext.item(0)).getNodeValue()
										.trim());
						// Using the same class instead.
						// ParseXML.processCode(pattern);
						// Delete the echoed characters
						PhoneticParseXML.putbkspace();
						
						
						/*
						 * If a vowel follows a consonant, then the dependent
						 * vowel is to be printed instead of the independent
						 * vowel. Check whether previous input is consonant and
						 * current input is a vowel.
						 */
						if (previousConsonantFlag == 1
								&& Integer.valueOf(currentconsonantflag) == 0) {
							/*
							 * Delete the halant that is put previously with the
							 * consonant and o/p dependent vowel Unicode.
							 */
							PhoneticParseXML.putbkspace();
							// Halant is removed.

							// Get the dependent vowel Unicode
							NodeList uniList = firstPatternElement
									.getElementsByTagName("uni2");
							Element uniEle = (Element) uniList.item(0);
							NodeList uniEdepList = uniEle.getChildNodes();
							ucodeValue = ((Node) uniEdepList.item(0))
									.getNodeValue().trim();
							/*
							 * If the input is a, the flag "aflag" is set.
							 */
							if (pattern.compareTo("a") == 0) {
								aflag = 1;
							}
						} else {
							/*
							 * If vowel doesn't follow a consonant then put the
							 * independent vowel unicode directly. Here there is
							 * no need to again read the XML for the 2nd
							 * unicode.
							 */
							ucodeValue = ((Node) textLNList.item(0))
									.getNodeValue().trim();
							tmp = new StringBuilder(ucodeValue);
							/*
							 * If the current input is a vowel then there is no
							 * need to check for the 2 character patterns.
							 * Ifcurrentconsonantflag=0 it means that the
							 * current input is a vowel.
							 */
							if (Integer.valueOf(currentconsonantflag) == 0) {
								// Do Nothing.
							} else {
								/*
								 * Declerations of the combintions of characters
								 * to form patterns. Here, first compare the
								 * previous character and the current character.
								 * Since this case occurs only when a consonant
								 * is put previously, that consonant has to be
								 * deleted and the consonant corresponding to
								 * the pattern is to be displayed. Two
								 * backspaces are applied, one for the Halant
								 * and the other for the full consonant. Half
								 * Consonant = Full consonant + halant.
								 * 
								 * Add "if" condition if other character
								 * patterns are to be added. Note that the
								 * pattern should be of maximum 2 characters if
								 * the coding logic is not to be changed. Also
								 * the pattern should be defined in the XML
								 * file.
								 */
								if (previousChar.compareTo("t") == 0
										&& pattern.compareTo("h") == 0
										&& previousConsonantFlag == 1) {
									putbkspace();
									putbkspace();
									ucodeValue = getunicode("th");
									// get unicode of th
								}// end of if(previousChar.compareTo("t")==0
								if (previousChar.compareTo("T") == 0
										&& pattern.compareTo("h") == 0
										&& previousConsonantFlag == 1) {
									putbkspace();
									putbkspace();
									ucodeValue = getunicode("Th");
									// get unicode of Th
								}// end of if(previousChar.compareTo("T")==0
								if (previousChar.compareTo("s") == 0
										&& pattern.compareTo("h") == 0
										&& previousConsonantFlag == 1) {
									putbkspace();
									putbkspace();
									ucodeValue = getunicode("sh");
									// get unicode of sh
								}// end of if(previousChar.compareTo("s")==0
								if (previousChar.compareTo("S") == 0
										&& pattern.compareTo("h") == 0
										&& previousConsonantFlag == 1) {
									putbkspace();
									putbkspace();
									ucodeValue = getunicode("Sh");
									// get unicode of Sh
								}// end of if(previousChar.compareTo("t")==0
								if (previousChar.compareTo("d") == 0
										&& pattern.compareTo("h") == 0
										&& previousConsonantFlag == 1) {
									putbkspace();
									putbkspace();
									ucodeValue = getunicode("dh");
									// get unicode of dh
								}// end of if(previousChar.compareTo("t")==0
								if (previousChar.compareTo("D") == 0
										&& pattern.compareTo("h") == 0
										&& previousConsonantFlag == 1) {
									putbkspace();
									putbkspace();
									ucodeValue = getunicode("Dh");
									// get unicode of Dh
								}// end of if(previousChar.compareTo("t")==0

								// COncatinate the halant with the consonant.
								tmp = new StringBuilder(ucodeValue);
								if (pattern.compareTo("M") != 0)
									tmp.append(halant);
							}

							ucodeValue = tmp.toString();
						}
						// @Debug
						System.out.println("String unicode is" + ucodeValue);

						// For key presses which have multiple unicodes
						// Output depending on OS type
						if(IndicKeyboards.operatingSystem.contains("Windows"))
						{
							org.iisc.mile.indickeyboards.windows.OutputCharToActiveWindow.getcharforop_phonetic(ucodeValue);
						}
						else
						{
							org.iisc.mile.indickeyboards.linux.OutputCharToActiveWindow.outputToActiveWindowPhonetic(ucodeValue);
						
						}
						
						/*
						 * Set the flags. Make the value of currentconsonantflag
						 * to previous, the previous to 2nd previous.
						 */
						previousConsonantFlaglog = previousConsonantFlag;
						previousConsonantFlag = Integer
								.valueOf(currentconsonantflag);
						previousChar = pattern;

						/*
						 * Instead of printing the unicode here call the native
						 * code and print the unicode by passing the unicode to
						 * be printed as a parameter to the native code
						 */
						/*
						 * System.out.println("Unicode : " + ((Node)
						 * textLNList.item(0)).getNodeValue() .trim());
						 */

					} // End of if
				}// end of if
			}// end of for
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

	}// End of getPhoneticPattern(String pattern)

	/**
	 * This method accepts a character and returns it's unicode value. This is
	 * designed to work only to search the Unicode values of consonants, that is
	 * only patterns having a single Unicode value.
	 * <p>
	 * This has been coded mainly to get the unicode value of Halant.
	 * 
	 * @param temp
	 *            Has the character whose unicode is to be found.
	 * @return Returns the Unicode as a String
	 */
	private String getunicode(String temp) {
		String returnvalue = "";
		try {

			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(new File(System
					.getProperty("user.dir"), "/kblayouts/"
					+ Phkeyboardlayoutname));

			// normalize text representation
			doc.getDocumentElement().normalize();
			NodeList listOfPatterns = doc.getElementsByTagName("pattern");
			// int totalPatterns = listOfPatterns.getLength();

			for (int s = 0; s < listOfPatterns.getLength(); s++) {

				Node firstPatternNode = listOfPatterns.item(s);
				if (firstPatternNode.getNodeType() == Node.ELEMENT_NODE) {

					Element firstPatternElement = (Element) firstPatternNode;

					// -------
					NodeList charNameList = firstPatternElement
							.getElementsByTagName("char");
					Element charElement = (Element) charNameList.item(0);

					NodeList textFNList = charElement.getChildNodes();

					if (temp.equals(textFNList.item(0).getNodeValue().trim())) {
						/*
						 * System.out.println("Char : " + ((Node)
						 * textFNList.item(0)).getNodeValue() .trim());
						 */

						// -------
						NodeList lastNameList = firstPatternElement
								.getElementsByTagName("unicode");
						Element lastNameElement = (Element) lastNameList
								.item(0);
						NodeList textLNList = lastNameElement.getChildNodes();

						returnvalue = ((Node) textLNList.item(0))
								.getNodeValue().trim();
						return returnvalue;
					} // End of if
				}// end of if
			}// end of for

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
		return returnvalue;

	}

	// End of getPattern

	/**
	 * Method used to send a backspace character to the current output active
	 * window. It is used the delete one character. This method uses the Robot
	 * Class defined in AWT
	 */
	public static void putbkspace() {
		
		// Delete the echoed characters
		try {
			// Create object of Robot class.
			Robot r = new Robot();
			// Press and release the backspace key emulating a back space.
			r.keyPress(KeyEvent.VK_BACK_SPACE);
			r.keyRelease(KeyEvent.VK_BACK_SPACE);
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sets the keyboard layout that has been selected by the user. This
	 * information is used to open the selected keyboard layout XML file during
	 * operation.
	 * <p>
	 * 
	 * @param name
	 *            Contains the name of the keyboard layout selected. It is the
	 *            same as that of the filename
	 */
	public static void setlang(String name) {
		Phkeyboardlayoutname = name;

	}

}