package org.iisc.mile.indickeyboards.linux;

import org.iisc.mile.indickeyboards.ParseXML;
import org.iisc.mile.indickeyboards.PhoneticParseXML;

public class OutputCharToActiveWindow {

	/**
	 * Creates an object of the class <em>LinuxLibraries</em> to call the native
	 * methods.
	 * @see org.iisc.mile.indickeyboards
	 */
	LinuxLibraries nativeMethodAccess = new LinuxLibraries();

	public static void outputToActiveWindowNonPhonetic(String ucodeValue) {
		StringBuilder temp = new StringBuilder(4);
		if (ucodeValue.length() > 4) {
			for (int j = 0; j < ucodeValue.length(); j++) {
				temp.append(ucodeValue.charAt(j));
				if (temp.length() == 4) {
					System.out.println(temp.toString());
					LinuxLibraries.OutputActiveWindow("U" + temp.toString());
					temp.delete(0, 4);
				}
			}

		} else {

			/*
			 * Tamil99 works as a part Phonetic layout. If vowel "a" is pressed
			 * after a consonant then nothing is to be printed.
			 */
			if (ParseXML.keyboardlayoutname.compareTo("tamil99.xml") == 0
					&& ParseXML.pattern.compareTo("a") == 0
					&& ParseXML.previousConsonantFlag != 0) {
				// Print nothing
			} else {
				// Call native method with the unicode
				LinuxLibraries.OutputActiveWindow("U" + ucodeValue);
			}
		}
	}

	public static void outputToActiveWindowPhonetic(String ucodeValue) {
		StringBuilder temp = new StringBuilder(4);
		if (ucodeValue.length() > 4) {
			for (int j = 0; j < ucodeValue.length(); j++) {
				temp.append(ucodeValue.charAt(j));
				if (temp.length() == 4) {
					System.out.println(temp.toString());
					LinuxLibraries.OutputActiveWindow("U" + temp.toString());
					temp.delete(0, 4);
				}
			}

		} else {
			if (PhoneticParseXML.aflag == 0) {
				LinuxLibraries.OutputActiveWindow("U" + ucodeValue);
			} else
				PhoneticParseXML.aflag = 0;
		}
	}
}
