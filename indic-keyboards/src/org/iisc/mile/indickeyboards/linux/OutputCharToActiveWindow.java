/**********************************************************************
 * File:           OutputCharToActiveWindow.java
 * Description:    Java code that takes Unicode and puts them onto
 * 					to the current active window.
 * Authors:        Akshay,Abhinava,Revati,Arun
 * Created:        Thu Oct 1 18:31:25 GMT 2009
 *
 * (C) Copyright 2009, MILE Lab, Indian Institute of Science
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
