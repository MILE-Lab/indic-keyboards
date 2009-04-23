/** ********************************************************************
 * File:           IndicKeyboards.java 
 * Description:    UI for the Common Keyboard Interface
 * Authors:        Akshay,Abhinava,Revati,Arun 
 * Created:        Mon Oct 20 19:31:25 GMT 2008
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
package core;

import java.io.FileNotFoundException;

public class IndicKeyboards implements KeyboardEventListener {

	private Boolean flag = false;
	private long withShiftPressed = 48;
	public static Boolean enable = false;

	public static void main(String[] args) throws FileNotFoundException {

		// instantiation of the KeyboardHook class
		KeyboardHook kh = new KeyboardHook();
		kh.addEventListener(new IndicKeyboards());

		System.out.println("Press F12 to enable key logging");
		SplashScreen s = new SplashScreen();
		s.screen();
		@SuppressWarnings("unused")
		UI iFace = new UI();

	}// end of main

	// Methods to monitor global key presses
	public void GlobalKeyPressed(KeyboardEvent event) {

		String inputChar;

		if (event.getVirtualKeyCode() == 123) {
			enable = !enable;
			if (enable) {
				System.out.println("Software enabled");
				// Reset the consonant flags present in ParseXML.java
				ParseXML.previousConsonantFlag = 0;
			} else {
				System.out
						.println("Software disabled... Press F12 to re-enable");
			}
		}
		if (enable) {
			if ((event.getVirtualKeyCode() == 16)
					&& (event.getTransitionState())) {
				// Flag shift press
				flag = true;
			}
			if (flag) {
				withShiftPressed = event.getVirtualKeyCode();

				/**
				 * Managing the shift+ key presses.
				 */
				if ((event.getVirtualKeyCode() >= 65)
						&& (event.getVirtualKeyCode() <= 90))
					withShiftPressed = event.getVirtualKeyCode() + 32;

				else {
					switch (event.getVirtualKeyCode()) {

					case 186:
						withShiftPressed = 58;
						break;

					case 188:
						withShiftPressed = 60;
						break;

					case 190:
						withShiftPressed = 62;
						break;

					case 191:
						withShiftPressed = 63;
						break;

					case 192:
						withShiftPressed = 126;
						break;

					case 222:
						withShiftPressed = 34;
						break;

					case 221:
						withShiftPressed = 125;
						break;

					case 219:
						withShiftPressed = 123;
						break;

					case 189:
						withShiftPressed = 95;
						break;

					case 187:
						withShiftPressed = 43;
						break;

					case 220:
						withShiftPressed = 124;
						break;

					// shift plus numbers
					case 48:
						withShiftPressed = 41;
						break;
					case 49:
						withShiftPressed = 33;
						break;
					case 50:
						withShiftPressed = 64;
						break;
					case 51:
						withShiftPressed = 35;
						break;
					case 52:
						withShiftPressed = 36;
						break;
					case 53:
						withShiftPressed = 37;
						break;
					case 54:
						withShiftPressed = 94;
						break;
					case 55:
						withShiftPressed = 38;
						break;
					case 56:
						withShiftPressed = 42;
						break;
					case 57:
						withShiftPressed = 40;
						break;

					}
				}
				if (withShiftPressed == 48 || withShiftPressed == 16) {
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
				if (PhoneticParseXML.PhoneticFlag == 0) {
					ParseXML test = new ParseXML();
					inputChar = new Character((char) event.getVirtualKeyCode())
							.toString().toLowerCase();
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
			if ((event.getVirtualKeyCode() == 16)
					&& (!event.getTransitionState())) {
				flag = false;
				if (withShiftPressed == 48) {
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

}// end of class CKI
