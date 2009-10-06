/**********************************************************************
 * File:           KeyMonitorMethods.java
 * Description:    Class which interfaces the Java and Native code.
 * Authors:        Akshay,Abhinava,Revati,Arun
 * Created:        Sat Mat 28 18:31:25 GMT 2009
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
package org.iisc.mile.indickeyboards.linux;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.iisc.mile.indickeyboards.IndicKeyboards;
import org.iisc.mile.indickeyboards.ParseXML;
import org.iisc.mile.indickeyboards.PhoneticParseXML;
import org.iisc.mile.indickeyboards.UI;

/**
 * 
 * This class identifies the keyboard event interface present and gets all the
 * key presses from the org.iisc.mile.indickeyboards.linux library. The
 * keypresses obtained are <em>keycodes</em>. The keycodes are mapped to
 * appropriate character patterns (keysyms) and they are then sent to methods
 * which process them depending on whether the keyboard layout is phonetic or
 * non-phonetic.
 */
public class KeyMonitorMethods {

	static int cLock = 0;

	/**
	 * Variable to indicate the state of the Num Lock
	 */
	static int nLock = 1;

	/**
	 * Boolean variable which tells if keylogging is enabled or disabled.
	 * Default, i.e. when the software is started, the keylogging is disabled.
	 * To enable, use "Alt+F12" or select the option from the menu.
	 */
	public static boolean loggingEnabled = false;

	/**
	 * Contains the name of the keyboard connected to the system. May also
	 * contain a brief description.
	 */
	String actualKB;

	/**
	 * Path to the event interface of the keyboard identified by the
	 * <code>identify()</code> method. Will be of the form
	 * <em>/dev/input/eventX</em>, X=0, 1, 2..
	 */
	String pathKB;

	/**
	 * Character patterns (keysyms) that are identified depending upon the
	 * keycode obtained.
	 */
	String pattern = "";

	/**
	 * Creates an object of the class <em>LinuxLibraries</em> to call the native
	 * methods.
	 */
	LinuxLibraries nativeMethodAccessObject = new LinuxLibraries();;

	/**
	 * Method which is called by the native method <code>grab()</code>. This
	 * method identifies the key which has been pressed depending upon the
	 * keycode.
	 * 
	 * @param code
	 *            This is sent by the native method <code>grab()</code>. For
	 *            Shift+ keypresses, a value of 200 is added to every keycode.
	 */

	public void printKeys(int code) {
		/*
		 * Controls the enabling and disabling of the keylogging. Can be done
		 * using "Alt+F12" or by selecting the option from the menu
		 */
		if (code == 666) {
			if (loggingEnabled == true) {
				loggingEnabled = false;
				System.out.println("Key Logging Disabled");
				if (InitLinux.AutoRepeat == false) {
					nativeMethodAccessObject.keyrepeat(1);
				}
				Display.getDefault().syncExec(new Runnable() {
					public void run() {
						org.iisc.mile.indickeyboards.UI.previousKeyboardIcon = org.iisc.mile.indickeyboards.UI.item
								.getImage();
						Image image1 = new Image(Display.getCurrent(),
								IndicKeyboards.workingDirectory
										+ "/resources/trayicon_disabled.ico");
						org.iisc.mile.indickeyboards.UI.item.setImage(image1);
						UI.tip.setMessage("Disabled");
						UI.tip.setVisible(true);
						UI.enableDisable.setText("Enable [Alt+F12]");
						System.gc();
					}
				});
			} else if (loggingEnabled == false) {
				loggingEnabled = true;
				System.out.println("Key Logging Enabled");
				ParseXML.previousConsonantFlag = 0;
				if (InitLinux.AutoRepeat == false) {
					nativeMethodAccessObject.keyrepeat(0);
				}
				Display.getDefault().syncExec(new Runnable() {
					public void run() {
						org.iisc.mile.indickeyboards.UI.item
								.setImage(org.iisc.mile.indickeyboards.UI.previousKeyboardIcon);
						UI.tip.setMessage("Enabled");
						UI.tip.setVisible(true);
						UI.enableDisable.setText("Disable [Alt+F12]");
						System.gc();
					}
				});
			}
		}

		if (loggingEnabled == false) {
			// do nothing
		}
		/*
		 * Keycode to pattern mapping.
		 */
		else if (loggingEnabled == true) {
			switch (code) {
			case 1:
				pattern = " Esc ";
				break;
			case 2:
				pattern = "1";
				break;
			case 202:
				pattern = "!";
				break;
			case 3:
				pattern = "2";
				break;
			case 203:
				pattern = "@";
				break;
			case 4:
				pattern = "3";
				break;
			case 204:
				pattern = "#";
				break;
			case 5:
				pattern = "4";
				break;
			case 205:
				pattern = "$";
				break;
			case 6:
				pattern = "5";
				break;
			case 206:
				pattern = "%";
				break;
			case 7:
				pattern = "6";
				break;
			case 207:
				pattern = "^";
				break;
			case 8:
				pattern = "7";
				break;
			case 208:
				pattern = "&";
				break;
			case 9:
				pattern = "8";
				break;
			case 209:
				pattern = "*";
				break;
			case 10:
				pattern = "9";
				break;
			case 210:
				pattern = "(";
				break;
			case 11:
				pattern = "0";
				break;
			case 211:
				pattern = ")";
				break;
			case 12:
				pattern = "-";
				break;
			case 212:
				pattern = "_";
				break;
			case 13:
				pattern = "=";
				break;
			case 213:
				pattern = "+";
				break;
			case 14:
				pattern = "\b";
				break;
			case 214:
				pattern = "\b";
				break;
			case 15:
				pattern = "\t";
				break;
			case 16:
				pattern = "q";
				break;
			case 216:
				pattern = "Q";
				break;
			case 17:
				pattern = "w";
				break;
			case 217:
				pattern = "W";
				break;
			case 18:
				pattern = "e";
				break;
			case 218:
				pattern = "E";
				break;
			case 19:
				pattern = "r";
				break;
			case 219:
				pattern = "R";
				break;
			case 20:
				pattern = "t";
				break;
			case 220:
				pattern = "T";
				break;
			case 21:
				pattern = "y";
				break;
			case 221:
				pattern = "Y";
				break;
			case 22:
				pattern = "u";
				break;
			case 222:
				pattern = "U";
				break;
			case 23:
				pattern = "i";
				break;
			case 223:
				pattern = "I";
				break;
			case 24:
				pattern = "o";
				break;
			case 224:
				pattern = "O";
				break;
			case 25:
				pattern = "p";
				break;
			case 225:
				pattern = "P";
				break;
			case 26:
				pattern = "[";
				break;
			case 226:
				pattern = "{";
				break;
			case 27:
				pattern = "]";
				break;
			case 227:
				pattern = "}";
				break;
			case 28:
				pattern = "\n";
				break;
			case 29:
				pattern = " Left Ctrl ";
				break;
			case 30:
				pattern = "a";
				break;
			case 230:
				pattern = "A";
				break;
			case 31:
				pattern = "s";
				break;
			case 231:
				pattern = "S";
				break;
			case 32:
				pattern = "d";
				break;
			case 232:
				pattern = "D";
				break;
			case 33:
				pattern = "f";
				break;
			case 233:
				pattern = "F";
				break;
			case 34:
				pattern = "g";
				break;
			case 234:
				pattern = "G";
				break;
			case 35:
				pattern = "h";
				break;
			case 235:
				pattern = "H";
				break;
			case 36:
				pattern = "j";
				break;
			case 236:
				pattern = "J";
				break;
			case 37:
				pattern = "k";
				break;
			case 237:
				pattern = "K";
				break;
			case 38:
				pattern = "l";
				break;
			case 238:
				pattern = "L";
				break;
			case 39:
				pattern = ";";
				break;
			case 239:
				pattern = ":";
				break;
			case 40:
				pattern = "'";
				break;
			case 240:
				pattern = "\"";
				break;
			case 41:
				pattern = "`";
				break;
			case 241:
				pattern = "~";
				break;
			case 42:
				pattern = " Left Shift ";
				break;
			case 43:
				pattern = "\\";
				break;
			case 243:
				pattern = "|";
				break;
			case 44:
				pattern = "z";
				break;
			case 244:
				pattern = "Z";
				break;
			case 45:
				pattern = "x";
				break;
			case 245:
				pattern = "X";
				break;
			case 46:
				pattern = "c";
				break;
			case 246:
				pattern = "C";
				break;
			case 47:
				pattern = "v";
				break;
			case 247:
				pattern = "V";
				break;
			case 48:
				pattern = "b";
				break;
			case 248:
				pattern = "B";
				break;
			case 49:
				pattern = "n";
				break;
			case 249:
				pattern = "N";
				break;
			case 50:
				pattern = "m";
				break;
			case 250:
				pattern = "M";
				break;
			case 51:
				pattern = ",";
				break;
			case 251:
				pattern = "<";
				break;
			case 52:
				pattern = ".";
				break;
			case 252:
				pattern = ">";
				break;
			case 53:
				pattern = "/";
				break;
			case 253:
				pattern = "?";
				break;
			case 54:
				pattern = " Right Shift ";
				break;
			case 55:
				pattern = "*";
				break;
			case 56:
				pattern = " Left Alt ";
				break;
			case 57:
				pattern = " ";
				break;
			case 58:
				break;
			case 111:
				break;
			case 59:
				pattern = " F1 ";
				break;
			case 60:
				pattern = " F2 ";
				break;
			case 61:
				pattern = " F3 ";
				break;
			case 62:
				pattern = " F4 ";
				break;
			case 63:
				pattern = " F5 ";
				break;
			case 64:
				pattern = " F6 ";
				break;
			case 65:
				pattern = " F7 ";
				break;
			case 66:
				pattern = " F8 ";
				break;
			case 67:
				pattern = " F9 ";
				break;
			case 68:
				pattern = " F10 ";
				break;
			case 69:
				if (nLock == 1) {
					System.out.print(" Num Lock Off");
					nLock = 0;
				} else {
					System.out.print(" Num Lock On ");
					nLock = 1;
				}
				break;
			case 70:
				pattern = " Scroll Lock ";
				break;
			case 71:
				pattern = "7";
				break;
			case 72:
				pattern = "8";
				break;
			case 73:

				pattern = "9";

				break;
			case 74:
				pattern = "-";
				break;
			case 75:

				pattern = "4";

				break;
			case 76:

				pattern = "5";

				break;
			case 77:

				pattern = "6";

				break;
			case 78:
				pattern = "+";
				break;
			case 79:

				pattern = "1";

				break;
			case 80:

				pattern = "2";

				break;
			case 81:

				pattern = "3";

				break;
			case 82:

				pattern = "0";

				break;
			case 83:
				pattern = ".";
				break;
			case 87:
				pattern = " F11 ";
				break;
			default:
				pattern = "";
				break;
			}
			/**
			 * See if the layout selected is Phonetic or Non-Phonetic. If it is
			 * phonetic, send the pattern to PhoneticParseXML. Else, send it to
			 * ParseXML.
			 */
			if (PhoneticParseXML.PhoneticFlag == 0) {
				System.out.println(pattern);
				ParseXML xmlobj = new ParseXML();
				xmlobj.getPattern(pattern);

			} else if (PhoneticParseXML.PhoneticFlag == 1) {
				PhoneticParseXML pxmlobj = new PhoneticParseXML();
				System.out.println(pattern);
				pxmlobj.getPhoneticPattern(pattern);
			}
		}
	}

	/**
	 * Identifies the path to the keyboard event interface and it's details by
	 * calling the native methdod <code>identify()</code>
	 */
	void identifyKB() {
		String id = "/dev/input/event";
		// String kb = null;
		int found = 0;
		for (int i = 0; i < 10; i++) {
			String temp = "" + i;
			actualKB = nativeMethodAccessObject.identify(id + temp);
			if (actualKB.contains("keyboard") || actualKB.contains("Keyboard")
					|| actualKB.contains("KEYBAORD")) {
				System.out.println("The keyboard present is : " + actualKB);
				pathKB = id + temp;
				found = 1;
				break;
			}

		}
		if (found == 0) {
			System.out.println("No proper keyboard found");
			System.exit(0);
		}
	}

	/**
	 * Grabs the keyboard identified by the <code>identifyKB()</code> method by
	 * calling the <code>grab()</code> native method.
	 */
	void grabKB() {
		System.out
				.println("The path to keyboard's input interface : " + pathKB);
		nativeMethodAccessObject.grab(pathKB);
	}
}
