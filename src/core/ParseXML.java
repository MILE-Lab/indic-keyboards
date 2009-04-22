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

public class ParseXML {
	public static String keyboardlayoutname;

	public static final int INSCRIPT_KB = 1;
	public static final int OTHER_KB = 0;
	public static int inscriptothers = 0;
	public static int previousConsonantFlag = 0;
	public static String currentconsonantflag;

	public native void opChars(int opchar);

	public void getPattern(String pattern) {
		/*
		 * This is to set the flag to zero whenever the space key is pressed or
		 * else the dependent vowel will be printed (For layouts other than
		 * inscript keyboard layouts)
		 */

		if (pattern.compareTo(" ") == 0) {
			previousConsonantFlag = 0;
		}

		try {

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

						// Delete the echoed characters
						try {
							Robot r = new Robot();
							r.keyPress(KeyEvent.VK_BACK_SPACE);
							r.keyRelease(KeyEvent.VK_BACK_SPACE);
						} catch (AWTException e) {
							e.printStackTrace();
						}
						// Load the dll for output
						System.loadLibrary("opChars");

						if (inscriptothers == 1) {
							String ucodeValue = ((Node) textLNList.item(0))
									.getNodeValue().trim();
							// @Debug
							// System.out.println("String unicode is"+
							// ucodeValue);

							// For key presses which have multiple unicodes
							StringBuilder temp = new StringBuilder(4);
							int i;
							if (ucodeValue.length() > 4) {
								for (int j = 0; j < ucodeValue.length(); j++) {
									temp.append(ucodeValue.charAt(j));
									if (temp.length() == 4) {
										System.out.println(temp.toString());
										i = Integer
												.valueOf(temp.toString(), 16)
												.intValue();
										opChars(i);
										temp.delete(0, 4);
									}
								}
							} else {
								// Call native method with the unicode
								i = Integer.valueOf(ucodeValue, 16).intValue();
								opChars(i);
							}
						} else {
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
							} else {
								// put as it is
								ucodeValue = ((Node) textLNList.item(0))
										.getNodeValue().trim();
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
										i = Integer
												.valueOf(temp.toString(), 16)
												.intValue();
										opChars(i);
										temp.delete(0, 4);
									}
								}

							} else {
								// Call native method with the unicode
								i = Integer.valueOf(ucodeValue, 16).intValue();
								opChars(i);
							}
							previousConsonantFlag = Integer
									.valueOf(currentconsonantflag);

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

}