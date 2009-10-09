/** ********************************************************************
 * File:           InitWin.java 
 * Description:    Initialize indic-keyboards on Windows.
 * Authors:        Akshay,Abhinava,Revati,Arun 
 * Created:        Mon July 2 19:31:25 GMT 2009
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

/*
 * Common Keboard interface module
 *
 * SWT is used for the shell extension.
 * Main part is the interface.
 * 
 * @version 0.1
 */
/**
 * package core is the source package
 * for Common Keyboard Interface
 */
package org.iisc.mile.indickeyboards.windows;

import java.io.FileNotFoundException;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.iisc.mile.indickeyboards.IndicKeyboards;
import org.iisc.mile.indickeyboards.ParseXML;
import org.iisc.mile.indickeyboards.PhoneticParseXML;
import org.iisc.mile.indickeyboards.UI;

public class InitWin implements KeyboardEventListener {

	public static final int ENTERKEY = 13;
	public static final int RPARANKEY = 40;
	public static final int NUM_9 = 57;
	public static final int ASTERISKKEY = 42;
	public static final int NUM_8 = 56;
	public static final int AMPKEY = 38;
	public static final int NUM_7 = 55;
	public static final int CAPKEY = 94;
	public static final int NUM_6 = 54;
	public static final int PERCENTAGEKEY = 37;
	public static final int NUM_5 = 53;
	public static final int DOLLARKEY = 36;
	public static final int NUM_4 = 52;
	public static final int HASHKEY = 35;
	public static final int NUM_3 = 51;
	public static final int ATKEY = 64;
	public static final int NUM_2 = 50;
	public static final int EXCLAIMKEY = 33;
	public static final int NUM_1 = 49;
	public static final int LPARANKEY = 41;
	public static final int NUM_0 = 48;
	public static final int PIPEKEY = 124;
	public static final int BKSLASHKEY = 220;
	public static final int PLUSKEY = 43;
	public static final int EQUALSKEY = 187;
	public static final int UNDERSCOREKEY = 95;
	public static final int MINUSKEY = 189;
	public static final int LTBRACKETKEY = 219;
	public static final int RTBRACEKEY = 125;
	public static final int RTBRACKETKEY = 221;
	public static final int DOUBLEQUOTEKEY = 34;
	public static final int SINGLEQUOTEKEY = 222;
	public static final int TILDEKEY = 126;
	public static final int BACKTICKKEY = 192;
	public static final int QUESTIONKEY = 63;
	public static final int FWSLASH = 191;
	public static final int GTKEY = 62;
	public static final int DOTKEY = 190;
	public static final int LTKEY = 60;
	public static final int COMAKEY = 188;
	public static final int COLONKEY = 58;
	public static final int SEMICOLONKEY = 186;
	public static final int SHIFTKEY = 16;
	public static final int F12KEY = 123;
	public static final int ALTKEY = 18;
	public static final int CTRLKEY = 17;
	private Boolean shiftPressed = false;
	private long withShiftPressed = NUM_0, throwAwayKey = ALTKEY;
	public static Boolean enable = false, altPressed = false;

	private Boolean ctrlPressed = false;
	private long withCtrlPressed = NUM_0;

	public void InitWinStart() throws FileNotFoundException {
		// instantiation of the KeyboardHook class
		KeyboardHook kh = new KeyboardHook();
		kh.addEventListener(new InitWin());
	}

	// Methods to monitor global key presses
	public void GlobalKeyPressed(KeyboardEvent event) {

		String inputChar;

		if ((event.getVirtualKeyCode() == ALTKEY)
				&& (event.getTransitionState())) {
			altPressed = true;
		}
		if (altPressed) {
			throwAwayKey = event.getVirtualKeyCode();
			System.out.println("ThrowAwayKEy = " + throwAwayKey);
			if (event.getVirtualKeyCode() == F12KEY) {
				throwAwayKey = F12KEY;
				enable = !enable;
				altPressed = !altPressed;
				System.out.println("keycode" + throwAwayKey);

				if (enable) {
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
					System.out.println("Software enabled");
					// Reset the consonant flags present in ParseXML.java
					ParseXML.previousConsonantFlag = 0;
				} else if (!enable) {
					Display.getDefault().syncExec(new Runnable() {
						public void run() {
							org.iisc.mile.indickeyboards.UI.previousKeyboardIcon = org.iisc.mile.indickeyboards.UI.item
									.getImage();
							Image image1 = new Image(
									Display.getCurrent(),
									IndicKeyboards.workingDirectory
											+ "/resources/trayicon_disabled.ico");
							org.iisc.mile.indickeyboards.UI.item
									.setImage(image1);
							UI.tip.setMessage("Disabled");
							UI.tip.setVisible(true);
							UI.enableDisable.setText("Enable [Alt+F12]");
							System.gc();
						}
					});
					System.out
							.println("Software disabled... Press Alt + F12 to re-enable");
				}
			}
		}

		if (enable) {
			// Allow the "Ctrl +" combinations to work 
			if ((event.getVirtualKeyCode() == CTRLKEY) && event.getTransitionState()) {
				ctrlPressed = true;

			}
			if (ctrlPressed) {
				withCtrlPressed = event.getVirtualKeyCode();
				if (withCtrlPressed == CTRLKEY) {

				} else {

				}
				return;
			}
			// End of block for "Ctrl +" combination code
			if ((event.getVirtualKeyCode() == SHIFTKEY)
					&& (event.getTransitionState())) {
				// Flag shift press
				shiftPressed = true;
			}
			if (shiftPressed) {
				withShiftPressed = event.getVirtualKeyCode();

				/**
				 * Managing the shift+ key presses.
				 */
				if ((event.getVirtualKeyCode() >= 65)
						&& (event.getVirtualKeyCode() <= 90))
					withShiftPressed = event.getVirtualKeyCode() + 32;

				else {
					switch (event.getVirtualKeyCode()) {

					case SEMICOLONKEY:
						withShiftPressed = COLONKEY;
						break;

					case COMAKEY:
						withShiftPressed = LTKEY;
						break;

					case DOTKEY:
						withShiftPressed = GTKEY;
						break;

					case FWSLASH:
						withShiftPressed = QUESTIONKEY;
						break;

					case BACKTICKKEY:
						withShiftPressed = TILDEKEY;
						break;

					case SINGLEQUOTEKEY:
						withShiftPressed = DOUBLEQUOTEKEY;
						break;

					case RTBRACKETKEY:
						withShiftPressed = RTBRACEKEY;
						break;

					case LTBRACKETKEY:
						withShiftPressed = 123; // F12 key and the openBrace
						// have the same key code "{"
						break;

					case MINUSKEY:
						withShiftPressed = UNDERSCOREKEY;
						break;

					case EQUALSKEY:
						withShiftPressed = PLUSKEY;
						break;

					case BKSLASHKEY:
						withShiftPressed = PIPEKEY;
						break;

					// shift plus numbers
					case NUM_0:
						withShiftPressed = LPARANKEY;
						break;
					case NUM_1:
						withShiftPressed = EXCLAIMKEY;
						break;
					case NUM_2:
						withShiftPressed = ATKEY;
						break;
					case NUM_3:
						withShiftPressed = HASHKEY;
						break;
					case NUM_4:
						withShiftPressed = DOLLARKEY;
						break;
					case NUM_5:
						withShiftPressed = PERCENTAGEKEY;
						break;
					case NUM_6:
						withShiftPressed = CAPKEY;
						break;
					case NUM_7:
						withShiftPressed = AMPKEY;
						break;
					case NUM_8:
						withShiftPressed = ASTERISKKEY;
						break;
					case NUM_9:
						withShiftPressed = RPARANKEY;
						break;

					}
				}
				if (withShiftPressed == SHIFTKEY) {
					// do not print shift while it is pressed
				} else {
					if (PhoneticParseXML.PhoneticFlag == 0) {
						ParseXML test = new ParseXML();

						/*
						 * If the key pressed is a character, send uppercase
						 * chars else the keycode which is stored in
						 * withShiftPressed variable is sent to the parser
						 */
						if (withShiftPressed >= 97 && withShiftPressed <= 122) {
							inputChar = new Character((char) withShiftPressed)
									.toString().toUpperCase();
						} else {
							inputChar = new Character((char) withShiftPressed)
									.toString();
						}
						test.getPattern(inputChar);
						System.out.println("Key Pressed with shift: "
								+ withShiftPressed);

						/*
						 * The following code is in case the option chosen is
						 * phonetic No manipulations required.
						 */
					} else {
						PhoneticParseXML test1 = new PhoneticParseXML();
						if (withShiftPressed >= 97 && withShiftPressed <= 122) {
							inputChar = new Character((char) withShiftPressed)
									.toString().toUpperCase();
						} else {
							inputChar = new Character((char) withShiftPressed)
									.toString();
						}
						test1.getPhoneticPattern(inputChar);
						System.out.println("Key Pressed with shift: "
								+ withShiftPressed);
					}
				}
			} else {
				// Added flag reset in PhoneticParseXML
				if (event.getVirtualKeyCode() == ENTERKEY) {
					PhoneticParseXML.previousConsonantFlag = 0;
					ParseXML.previousConsonantFlag = 0;
					ParseXML.tamil99count = 0;
				}
				int tempKeyCode = 0;
				if (PhoneticParseXML.PhoneticFlag == 0) {
					ParseXML test = new ParseXML();

					if (event.getVirtualKeyCode() <= 185) {
						inputChar = new Character((char) event
								.getVirtualKeyCode()).toString().toLowerCase();
					} else {

						switch (event.getVirtualKeyCode()) {

						case 186:
							tempKeyCode = 59;
							break;

						case 222:
							tempKeyCode = 39;
							break;

						case 188:
							tempKeyCode = 44;
							break;

						case 190:
							tempKeyCode = 46;
							break;

						case 191:
							tempKeyCode = 47;
							break;

						case 219:
							tempKeyCode = 91;
							break;

						case 221:
							tempKeyCode = 93;
							break;

						case 192:
							tempKeyCode = 96;
							break;

						case 189:
							tempKeyCode = 45;
							break;

						case 187:
							tempKeyCode = 61;
							break;

						case 220:
							tempKeyCode = 92;
							break;

						}
						inputChar = new Character((char) tempKeyCode)
								.toString();
					}

					test.getPattern(inputChar);
					System.out.println("Key Pressed: "
							+ event.getVirtualKeyCode());
				} else {
					PhoneticParseXML test1 = new PhoneticParseXML();
					inputChar = new Character((char) event.getVirtualKeyCode())
							.toString().toLowerCase();
					test1.getPhoneticPattern(inputChar);
					System.out.println("Key Pressed: "
							+ event.getVirtualKeyCode());
				}
			}
		}
	}

	public void GlobalKeyReleased(KeyboardEvent event) {
		if (enable) {
			// For "Control +" actions
			if ((event.getVirtualKeyCode() == CTRLKEY)
					&& (!event.getTransitionState())) {
				ctrlPressed = false;
				if (withCtrlPressed == NUM_0) {

				} else {

				}
			}
			// End of "Control +" actions code
			if ((event.getVirtualKeyCode() == SHIFTKEY)
					&& (!event.getTransitionState())) {
				shiftPressed = false;
				if (withShiftPressed == NUM_0) {
					/*
					 * Either do the following or simply leave out shift key
					 * logging System.out.println("Key Released: " +
					 * event.getVirtualKeyCode());
					 */
				} else {
					/*
					 * optional statement. logs key releases of only those where
					 * shift key is pressed. System.out.println("Key Released: "
					 * + withShiftPressed);
					 */
				}
			}
		}
	}

}// end of class InitWin
