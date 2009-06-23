/** ********************************************************************
 * File:           ParseXML.java 
 * Description:    XML parser for inscript and other layouts 
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

import java.io.File;
import java.io.FileNotFoundException;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class ParseXML {
	/*
	 * Variable declarations.
	 */
	public static String keyboardlayoutname;
	/*
	 * Flag variables used to keep track of type of keyboard Layout selected.
	 */
	public static final int INSCRIPT_KB = 1;
	public static final int OTHER_KB = 0;
	public static int inscriptothers = 0;

	/*
	 * Flag variables used to keep track of the type of indic character -
	 * consonants and vowels
	 */
	public static int previousConsonantFlaglog = 0;
	public static int previousConsonantFlag = 0;
	public static String currentconsonantflag;
	public static String previousChar = "";
	public static int tamil99count=0;

	public native void OutputActiveWindow(String ucode);
	
	public native void bkSpace();

	public void getPattern(String pattern) {
		/*
		 * This is to set the flag to zero whenever the space key is pressed so
		 * as to prevent the dependent vowel to be printed (For layouts other
		 * than inscript keyboard layouts)
		 */

		if (pattern.compareTo(" ") == 0) {
			previousConsonantFlag = 0;
		}
		if (pattern.compareTo("\b") == 0) {
			previousConsonantFlag = previousConsonantFlaglog;
		}

		try {
			/*
			 * Load the XML document into the memory. XML document name is specified by the keyboardlayoutname variable.
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

						/**
						 * Instead of printing the unicode here call the native
						 * code and print the unicode by passing the unicode to
						 * be printed as a parameter to the native code
						 */
						/*
						 * System.out.println("Unicode : " + ((Node)
						 * textLNList.item(0)).getNodeValue() .trim());
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
							StringBuilder temp = new StringBuilder(4);
							int i;
							if (ucodeValue.length() > 4) {
								for (int j = 0; j < ucodeValue.length(); j++) {
									temp.append(ucodeValue.charAt(j));
									if (temp.length() == 4) {
										System.out.println(temp.toString());
										OutputActiveWindow("U" + temp.toString());
										temp.delete(0, 4);
									}
								}
							} else {
								// Call native method with the unicode
								OutputActiveWindow("U" + ucodeValue);
							}
						} else {
							String ucodeValue;
							PhoneticParseXML.putbkspace();
							NodeList vovelList = firstPatternElement
									.getElementsByTagName("consonant");
							Element vovelElement = (Element) vovelList.item(0);
							NodeList vovelEtext = vovelElement.getChildNodes();
							currentconsonantflag = ((Node) vovelEtext.item(0))
									.getNodeValue().trim();
							System.out.println("Previous Flag:"
									+ previousConsonantFlag);
							System.out.println("Flag :"
									+ ((Node) vovelEtext.item(0))
											.getNodeValue().trim());

							// Using the same class instead.
							// ParseXML.processCode(pattern);
							if (previousConsonantFlag == 1
									&& Integer.valueOf(currentconsonantflag) == 0) {
								// Get the dependant vovel unicode
								NodeList uniList = firstPatternElement
										.getElementsByTagName("uni2");
								Element uniEle = (Element) uniList.item(0);
								NodeList uniEdepList = uniEle.getChildNodes();
								ucodeValue = ((Node) uniEdepList.item(0))
										.getNodeValue().trim();
								// For tamil99
								/*if (ParseXML.keyboardlayoutname
										.compareTo("tamil99.xml") == 0) {
									PhoneticParseXML.putbkspace();
									System.out.println("Knock off halant");
								}*/
							} else {
								// put as it is
								ucodeValue = ((Node) textLNList.item(0))
										.getNodeValue().trim();
								// For tamil99 only
								if (ParseXML.keyboardlayoutname
										.compareTo("tamil99.xml") == 0
										&& Integer
												.valueOf(currentconsonantflag) == 1 && previousConsonantFlag==1
										&& pattern.compareTo("f") != 0 && previousChar.compareTo(pattern) == 0 ) {
									if(tamil99count%2 ==0){
									 ucodeValue =  "0bcd" + ucodeValue;
									  System.out.println("Halant to be printed!");
									}
									tamil99count++;
									
								}
							}
							// @Debug
							System.out
									.println("String unicode is" + ucodeValue);

							// For key presses which have multiple unicodes
							StringBuilder temp = new StringBuilder(4);
							int i;
							if (ucodeValue.length() > 4) {
								for (int j = 0; j < ucodeValue.length(); j++) {
									temp.append(ucodeValue.charAt(j));
									if (temp.length() == 4) {
										System.out.println(temp.toString());
										OutputActiveWindow("U" + temp.toString());
										temp.delete(0, 4);
									}
								}

							} else {
								// Call native method with the unicode
								if (ParseXML.keyboardlayoutname
										.compareTo("tamil99.xml") == 0
										&& pattern.compareTo("a") == 0
										&& previousConsonantFlag != 0) {

								} else {

									OutputActiveWindow("U" + ucodeValue);
								}
							}
							previousConsonantFlaglog = previousConsonantFlag;
							previousConsonantFlag = Integer
									.valueOf(currentconsonantflag);
						
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

	public static void setlang(String name) {
		keyboardlayoutname = name;
		if (keyboardlayoutname.contains("inscript") == true)
			inscriptothers = INSCRIPT_KB;
		else
			inscriptothers = OTHER_KB;

	}

	static {
		System.out.println(System.getProperty("user.dir"));
		String path = System.getProperty("user.dir");
		try {
			System.load(path + "/libOutputActiveWindow-64.so.1.0");
			System.load(path + "/libbkSpace-64.so.1.0");
		} catch (UnsatisfiedLinkError e) {
			System.load("/usr/lib/libOutputActiveWindow-64.so.1.0");
			System.load("/usr/lib/libbkSpace-64.so.1.0");
		}

	}

}
