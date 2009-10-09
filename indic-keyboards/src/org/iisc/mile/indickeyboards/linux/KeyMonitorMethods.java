/**********************************************************************
 * File:           KeyMonitorMethods.java
 * Description:    Class which identifies and grabs the keyboard,
 * 					 and gets called from the native call throug a
 * 					 JNI callback. Also responsible to get the
 * 					 keycodes.
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

import java.io.File;
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
 * key presses obtained are <em>keycodes</em>. The keycodes are mapped to
 * appropriate character patterns (keysyms) and they are then sent to methods
 * which process them depending on whether the keyboard layout is phonetic or
 * non-phonetic.
 */
public class KeyMonitorMethods {

	/**
	 * Boolean variable which tells if keylogging is enabled or disabled.
	 * Default, i.e. when the software is started, the keylogging is disabled.
	 * To enable, use "Alt + F12" or select the option from the menu.
	 */
	public static boolean loggingEnabled = false;

	/**
	 * Contains the name of the keyboard connected to the system. May also
	 * contain a brief description.
	 */
	String keyboardName;

	/**
	 * Path to the event interface of the keyboard identified by the
	 * <code>identify()</code> method. Will be of the form
	 * <em>/dev/input/eventX</em>, X=0, 1, 2..
	 */
	String absolutePathToKeyBoard;

	/**
	 * Character patterns (keysyms) that are identified depending upon the
	 * keycode obtained.
	 */
	String pattern = "";

	/**
	 * Creates an object of the class <em>LinuxLibraries</em> to call the native
	 * methods.
	 * @see LinuxLibraries
	 */
	LinuxLibraries nativeMethodAccessObject = new LinuxLibraries();

	/*
	 * Constant Field Values.
	 */
	public final int KEY_A = 30;
	public final int KEY_B = 48;
	public final int KEY_C = 46;
	public final int KEY_D = 32;
	public final int KEY_E = 18;
	public final int KEY_F = 33;
	public final int KEY_G = 34;
	public final int KEY_H = 35;
	public final int KEY_I = 23;
	public final int KEY_J = 36;
	public final int KEY_K = 37;
	public final int KEY_L = 38;
	public final int KEY_M = 50;
	public final int KEY_N = 49;
	public final int KEY_O = 24;
	public final int KEY_P = 25;
	public final int KEY_Q = 16;
	public final int KEY_R = 19;
	public final int KEY_S = 31;
	public final int KEY_T = 20;
	public final int KEY_U = 22;
	public final int KEY_V = 47;
	public final int KEY_W = 17;
	public final int KEY_X = 45;
	public final int KEY_Y = 21;
	public final int KEY_Z = 44;
	
	public final int KEY_1 = 2;
	public final int KEY_2 = 3;
	public final int KEY_3 = 4;
	public final int KEY_4 = 5;
	public final int KEY_5 = 6;
	public final int KEY_6 = 7;
	public final int KEY_7 = 8;
	public final int KEY_8 = 9;
	public final int KEY_9 = 10;
	public final int KEY_0 = 11;
	
	public final int KEY_NUMPAD_0 = 82;
	public final int KEY_NUMPAD_1 = 79;
	public final int KEY_NUMPAD_2 = 80;
	public final int KEY_NUMPAD_3 = 81;
	public final int KEY_NUMPAD_4 = 75;
	public final int KEY_NUMPAD_5 = 76;
	public final int KEY_NUMPAD_6 = 77;
	public final int KEY_NUMPAD_7 = 71;
	public final int KEY_NUMPAD_8 = 72;
	public final int KEY_NUMPAD_9 = 73;
	public final int KEY_NUMPAD_ENTER = 96;
	public final int KEY_NUMPAD_PLUS = 78;
	public final int KEY_NUMPAD_MINUS = 74;
	public final int KEY_NUMPAD_ASTERISK = 55;
	public final int KEY_NUMPAD_SLASH = 98;
	public final int KEY_NUMPAD_PERIOD = 83;
	
	public final int KEY_NUM_LOCK = 69;
	public final int KEY_CAPS_LOCK = 58;
	public final int KEY_SCROLL_LOCK = 70;
	public final int KEY_PRINTSCREEN = 99;
	public final int KEY_PAUSE = 119;
	
	public final int KEY_ESC = 1;
	public final int KEY_MINUS = 12;
	public final int KEY_EQUALS = 13;
	public final int KEY_BACK_QUOTE = 41;
	public final int KEY_BACK_SPACE = 14;
 	public final int KEY_BACK_SLASH = 43;
 	public final int KEY_TAB = 15;
 	public final int KEY_OPEN_BRACKET = 26;
 	public final int KEY_CLOSE_BRACKET = 27;
 	public final int KEY_ENTER = 28;
 	public final int KEY_SEMICOLON = 39;
 	public final int KEY_QUOTE = 40;
 	public final int KEY_COMMA = 51;
 	public final int KEY_PERIOD = 52;
 	public final int KEY_SLASH = 53;
 	public final int KEY_SPACE = 57;
 	public final int KEY_WINDOWS_LEFT = 125;
 	public final int KEY_WINDOWS_RIGHT = 126;
 	public final int KEY_RIGHT_CLICK = 127;
 	
 	public final int KEY_LEFT = 105;
 	public final int KEY_RIGHT = 106;
 	public final int KEY_UP = 103;
 	public final int KEY_DOWN = 108;
 	
 	public final int KEY_DELETE = 111;
 	public final int KEY_HOME = 102;
 	public final int KEY_END = 107;
 	public final int KEY_PAGE_UP = 104;
 	public final int KEY_PAGE_DOWN = 109;
 	
 	public final int KEY_F1 = 59;
 	public final int KEY_F2 = 60;
 	public final int KEY_F3 = 61;
 	public final int KEY_F4 = 62;
 	public final int KEY_F5 = 63;
 	public final int KEY_F6 = 64;
 	public final int KEY_F7 = 65;
 	public final int KEY_F8 = 66;
 	public final int KEY_F9 = 67;
 	public final int KEY_F10 = 68;
 	public final int KEY_F11 = 87;
 	public final int KEY_F12 = 88;
 	
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
			case KEY_ESC:
				pattern = " Esc ";
				break;
			case KEY_1:
				System.out.println(code);
				pattern = "1";
				break;
			case KEY_1 + 200:
				pattern = "!";
				break;
			case KEY_2:
				pattern = "2";
				break;
			case KEY_2 + 200:
				pattern = "@";
				break;
			case KEY_3:
				pattern = "3";
				break;
			case KEY_3 + 200:
				pattern = "#";
				break;
			case KEY_4:
				pattern = "4";
				break;
			case KEY_4 + 200:
				pattern = "$";
				break;
			case KEY_5:
				pattern = "5";
				break;
			case KEY_5 + 200:
				pattern = "%";
				break;
			case KEY_6:
				pattern = "6";
				break;
			case KEY_6 + 200:
				pattern = "^";
				break;
			case KEY_7:
				pattern = "7";
				break;
			case KEY_7 + 200:
				pattern = "&";
				break;
			case KEY_8:
				pattern = "8";
				break;
			case KEY_8 + 200:
				pattern = "*";
				break;
			case KEY_9:
				pattern = "9";
				break;
			case KEY_9 + 200:
				pattern = "(";
				break;
			case KEY_0:
				pattern = "0";
				break;
			case KEY_0 + 200:
				pattern = ")";
				break;
			case KEY_MINUS:
				pattern = "-";
				break;
			case KEY_MINUS + 200:
				pattern = "_";
				break;
			case KEY_EQUALS:
				pattern = "=";
				break;
			case KEY_EQUALS + 200:
				pattern = "+";
				break;
			case KEY_BACK_SPACE:
				pattern = "\b";
				break;
			case KEY_BACK_SPACE + 200:
				pattern = "\b";
				break;
			case KEY_TAB:
				pattern = "\t";
				break;
			case KEY_TAB + 200 : 
				pattern = "\t";
				break;
			case KEY_Q:
				pattern = "q";
				break;
			case KEY_Q + 200:
				pattern = "Q";
				break;
			case KEY_W:
				pattern = "w";
				break;
			case KEY_W + 200:
				pattern = "W";
				break;
			case KEY_E:
				pattern = "e";
				break;
			case KEY_E + 200:
				pattern = "E";
				break;
			case KEY_R:
				pattern = "r";
				break;
			case KEY_R + 200:
				pattern = "R";
				break;
			case KEY_T:
				pattern = "t";
				break;
			case KEY_T + 200:
				pattern = "T";
				break;
			case KEY_Y:
				pattern = "y";
				break;
			case KEY_Y + 200:
				pattern = "Y";
				break;
			case KEY_U:
				pattern = "u";
				break;
			case KEY_U + 200:
				pattern = "U";
				break;
			case KEY_I:
				pattern = "i";
				break;
			case KEY_I + 200:
				pattern = "I";
				break;
			case KEY_O:
				pattern = "o";
				break;
			case KEY_O + 200:
				pattern = "O";
				break;
			case KEY_P:
				pattern = "p";
				break;
			case KEY_P + 200:
				pattern = "P";
				break;
			case KEY_OPEN_BRACKET:
				pattern = "[";
				break;
			case KEY_OPEN_BRACKET + 200:
				pattern = "{";
				break;
			case KEY_CLOSE_BRACKET:
				pattern = "]";
				break;
			case KEY_CLOSE_BRACKET + 200:
				pattern = "}";
				break;
			case KEY_ENTER:
				pattern = "\n";
				break;
			case 29:
				pattern = " Left Ctrl ";
				break;
			case KEY_A:
				pattern = "a";
				break;
			case KEY_A + 200:
				pattern = "A";
				break;
			case KEY_S:
				pattern = "s";
				break;
			case KEY_S + 200:
				pattern = "S";
				break;
			case KEY_D:
				pattern = "d";
				break;
			case KEY_D + 200:
				pattern = "D";
				break;
			case KEY_F:
				pattern = "f";
				break;
			case KEY_F + 200:
				pattern = "F";
				break;
			case KEY_G:
				pattern = "g";
				break;
			case KEY_G + 200:
				pattern = "G";
				break;
			case KEY_H:
				pattern = "h";
				break;
			case KEY_H + 200:
				pattern = "H";
				break;
			case KEY_J:
				pattern = "j";
				break;
			case KEY_J + 200:
				pattern = "J";
				break;
			case KEY_K:
				pattern = "k";
				break;
			case KEY_K + 200:
				pattern = "K";
				break;
			case KEY_L:
				pattern = "l";
				break;
			case KEY_L + 200:
				pattern = "L";
				break;
			case KEY_SEMICOLON:
				pattern = ";";
				break;
			case KEY_SEMICOLON + 200:
				pattern = ":";
				break;
			case KEY_QUOTE:
				pattern = "'";
				break;
			case KEY_QUOTE + 200:
				//Double Quote
				pattern = "\"";
				break;
			case KEY_BACK_QUOTE:
				pattern = "`";
				break;
			case KEY_BACK_QUOTE + 200:
				//Tilde
				pattern = "~";
				break;
			case 42:
				pattern = " Left Shift ";
				break;
			case KEY_BACK_SLASH:
				pattern = "\\";
				break;
			case KEY_BACK_SLASH + 200:
				//Pipe
				pattern = "|";
				break;
			case KEY_Z:
				pattern = "z";
				break;
			case KEY_Z + 200:
				pattern = "Z";
				break;
			case KEY_X:
				pattern = "x";
				break;
			case KEY_X + 200:
				pattern = "X";
				break;
			case KEY_C:
				pattern = "c";
				break;
			case KEY_C + 200:
				pattern = "C";
				break;
			case KEY_V:
				pattern = "v";
				break;
			case KEY_V + 200:
				pattern = "V";
				break;
			case KEY_B:
				pattern = "b";
				break;
			case KEY_B + 200:
				pattern = "B";
				break;
			case KEY_N:
				pattern = "n";
				break;
			case KEY_N + 200:
				pattern = "N";
				break;
			case KEY_M:
				pattern = "m";
				break;
			case KEY_M + 200:
				pattern = "M";
				break;
			case KEY_COMMA:
				pattern = ",";
				break;
			case KEY_COMMA + 200:
				//Left Angular Bracket
				pattern = "<";
				break;
			case KEY_PERIOD:
				pattern = ".";
				break;
			case KEY_PERIOD + 200:
				//RIgjt Angular Bracket
				pattern = ">";
				break;
			case KEY_SLASH:
				pattern = "/";
				break;
			case KEY_SLASH + 200:
				pattern = "?";
				break;
			case 54:
				pattern = " Right Shift ";
				break;
			case KEY_NUMPAD_ASTERISK:
				pattern = "*";
				break;
			case 56:
				pattern = " Left Alt ";
				break;
			case KEY_SPACE:
				pattern = " ";
				break;
			case KEY_CAPS_LOCK:
				//Caps Lock
				break;
			case KEY_DELETE:
				//Delete
				break;
			case KEY_F1:
				pattern = " F1 ";
				break;
			case KEY_F2:
				pattern = " F2 ";
				break;
			case KEY_F3:
				pattern = " F3 ";
				break;
			case KEY_F4:
				pattern = " F4 ";
				break;
			case KEY_F5:
				pattern = " F5 ";
				break;
			case KEY_F6:
				pattern = " F6 ";
				break;
			case KEY_F7:
				pattern = " F7 ";
				break;
			case KEY_F8:
				pattern = " F8 ";
				break;
			case KEY_F9:
				pattern = " F9 ";
				break;
			case KEY_F10:
				pattern = " F10 ";
				break;
			case KEY_NUM_LOCK:
				pattern = " Num Lock ";
				break;
			case KEY_SCROLL_LOCK:
				pattern = " Scroll Lock ";
				break;
			case KEY_NUMPAD_7:
				pattern = "7";
				break;
			case KEY_NUMPAD_8:
				pattern = "8";
				break;
			case KEY_NUMPAD_9:
				pattern = "9";
				break;
			case KEY_NUMPAD_MINUS:
				pattern = "-";
				break;
			case KEY_NUMPAD_4:
				pattern = "4";
				break;
			case KEY_NUMPAD_5:
				pattern = "5";
				break;
			case KEY_NUMPAD_6:
				pattern = "6";
				break;
			case KEY_NUMPAD_PLUS:
				pattern = "+";
				break;
			case KEY_NUMPAD_1:
				pattern = "1";
				break;
			case KEY_NUMPAD_2:
				pattern = "2";
				break;
			case KEY_NUMPAD_3:
				pattern = "3";
				break;
			case KEY_NUMPAD_0:
				pattern = "0";
				break;
			case KEY_NUMPAD_PERIOD:
				pattern = ".";
				break;
			case KEY_F11:
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
	void identifyKeyBoard() {
		String deadPathToKeyBoard = "/dev/input/event";
		boolean found = false;
		File keyBoardDirectory = new File ("/dev/input/");
		String []eventHandlers = keyBoardDirectory.list();
		for (int i = 0; i < eventHandlers.length; i++) {
			String temp = "" + i;
			keyboardName = nativeMethodAccessObject.identify(deadPathToKeyBoard + temp);
			keyboardName = keyboardName.toLowerCase();
			if (keyboardName.contains("keyboard")) {
				System.out.println("The keyboard present is : " + keyboardName);
				absolutePathToKeyBoard = deadPathToKeyBoard + temp;
				found = true;
				break;
			}

		}
		if (!found) {
			System.out.println("No proper keyboard found");
			System.exit(0);
		}
	}

	/**
	 * Grabs the keyboard identified by the <code>identifyKeyBoard()</code> method by
	 * calling the <code>grab()</code> native method.
	 */
	void grabKeyBoard() {
		System.out.println("The path to keyboard's input interface : " + absolutePathToKeyBoard);
		nativeMethodAccessObject.grab(absolutePathToKeyBoard);
	}
}
