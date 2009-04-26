/** ********************************************************************
 * File:           ParseXML.java 
 * Description:    XML parser for Phonetic input 
 * Authors:        Akshay,Abhinava,Revati,Arun 
 * Created:        Thu Mar 26 20:01:25 IST 2009
 *
 * (C) Copyright 2008, MILE Lab, IISc
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

package core;

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

public class PhoneticParseXML {
	public static String Phkeyboardlayoutname;

	public static int PhoneticFlag = 0;
	public static int previousConsonantFlaglog = 0;
	public static int previousConsonantFlag = 0;
	public static String currentconsonantflag;
	public static String halant;
	public static String previousChar = "";
	public static StringBuilder tmp;
	public static int aflag = 0;

	// public native void opChars(int opchar);

	public void getPhoneticPattern(String pattern) {

		ParseXML ob = new ParseXML();
		System.out.println("Phonetic Mode");
		if (pattern.compareTo(" ") == 0) {
			previousConsonantFlag = 0;
		}
		if (pattern.compareTo("\b") == 0) {
			previousConsonantFlag = previousConsonantFlaglog;
		}
		// To set the unicode of the halant to static string variable halant
		halant = getunicode("halant");
		System.out.println("Halant: " + halant);
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

					// System.out.println("The inputChar param =" + inputChar);
					if (pattern
							.equals(textFNList.item(0).getNodeValue().trim())) {
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
						try {
							Robot r = new Robot();
							r.keyPress(KeyEvent.VK_BACK_SPACE);
							r.keyRelease(KeyEvent.VK_BACK_SPACE);
						} catch (AWTException e) {
							e.printStackTrace();
						}
						// Load the dll for output
						ob.loadopcharsDLL();
						if (previousConsonantFlag == 1
								&& Integer.valueOf(currentconsonantflag) == 0) {
							// Knock off the halant and o/p dependant vovel
							// unicode.
							try {
								Robot r = new Robot();
								r.keyPress(KeyEvent.VK_BACK_SPACE);
								r.keyRelease(KeyEvent.VK_BACK_SPACE);
							} catch (AWTException e) {
								e.printStackTrace();
							} // Halant is knocked off.
							// Get the dependant vovel unicode
							NodeList uniList = firstPatternElement
									.getElementsByTagName("uni2");
							Element uniEle = (Element) uniList.item(0);
							NodeList uniEdepList = uniEle.getChildNodes();
							ucodeValue = ((Node) uniEdepList.item(0))
									.getNodeValue().trim();
							if (pattern.compareTo("a") == 0) {
								aflag = 1;
							}
						} else {
							// put as it is
							ucodeValue = ((Node) textLNList.item(0))
									.getNodeValue().trim();
							tmp = new StringBuilder(ucodeValue);
							if (Integer.valueOf(currentconsonantflag) == 0) {

							} else {

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
						StringBuilder temp = new StringBuilder(4);
						int i;
						if (ucodeValue.length() > 4) {
							for (int j = 0; j < ucodeValue.length(); j++) {
								temp.append(ucodeValue.charAt(j));
								if (temp.length() == 4) {
									System.out.println(temp.toString());
									i = Integer.valueOf(temp.toString(), 16)
											.intValue();
									ob.opChars(i);
									temp.delete(0, 4);
								}
							}

						} else {
							// Call native method with the unicode
							if (aflag == 0) {
								i = Integer.valueOf(ucodeValue, 16).intValue();
								ob.opChars(i);
							} else
								aflag = 0;
						}
						previousConsonantFlaglog = previousConsonantFlag;
						previousConsonantFlag = Integer
								.valueOf(currentconsonantflag);
						previousChar = pattern;

						/**
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
	public static void putbkspace() {
		// Delete the echoed characters
		try {
			Robot r = new Robot();
			r.keyPress(KeyEvent.VK_BACK_SPACE);
			r.keyRelease(KeyEvent.VK_BACK_SPACE);
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	public static void setlang(String name) {
		Phkeyboardlayoutname = name;

	}

}