/** ********************************************************************
 * File:           UI.java 
 * Description:    
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

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;

public class UI {
	// Creating the main display.Since SWT 3.1, support for multiple
	// displays has been
	// discontinued. SO at a time, only one active display can exist.
	UI() {
		final Display display = new Display();
		final Shell shell = new Shell(display);

		// the image path. The image File must be in the same directory/package
		// as that of the source file

		final Image image = new Image(display, IndicKeyboards.class
				.getResourceAsStream("pifmgr1.ico"));

		// Creating the tray icon
		final Tray tray = display.getSystemTray();

		if (tray == null) {
			System.err.println("The system tray is not available");
		} else {
			final TrayItem item = new TrayItem(tray, SWT.NONE);
			item
					.setToolTipText("Common Keyboard Interface\nDouble click to visit CKI homepage");
			/* DefaultSelection is Double-click */
			item.addListener(SWT.DefaultSelection, new Listener() {

				public void handleEvent(Event event) {

					Shell sh = new Shell(display, SWT.APPLICATION_MODAL);
					sh.setImage(image);
					MessageBox messageBox = new MessageBox(sh, SWT.YES | SWT.NO
							| SWT.ICON_QUESTION);
					messageBox.setText("Open Browser");

					messageBox
							.setMessage("Dou you want to open CKI's webpage ??\n\n(Opens in the default internet browser)");
					int rCode = messageBox.open();
					if (rCode == SWT.YES) {
						/*
						 * Opens the default browser. Or any program which opens
						 * .html files by default
						 */
						Program pBrowse = Program.findProgram(".html");

						pBrowse.execute("http://code.google.com/p/cki");

					} else {

						sh.dispose();
					}

				}
			});

			final Menu menu = new Menu(shell, SWT.POP_UP); /*
															 * The main menu
															 * obtained when the
															 * tray is right
															 * clicked
															 */
			/* Used to load user defined keyboard layouts */

			File dirname = new File("./kblayoutxmls");
			String[] userDefinedLayouts = dirname.list();
			if (userDefinedLayouts == null) {
				// Either dir does not exist or is not a directory
			} else {
				final MenuItem user_def = new MenuItem(menu, SWT.CASCADE);
				user_def.setText("User Defined");
				final Menu submenu_user_def = new Menu(shell, SWT.DROP_DOWN);
				user_def.setMenu(submenu_user_def);
				new MenuItem(menu, SWT.SEPARATOR);
				for (int i = 0; i < userDefinedLayouts.length; i++) {
					// Get filename of file or directory
					String filename = userDefinedLayouts[i];
					final MenuItem mi = new MenuItem(submenu_user_def, SWT.PUSH);
					mi.setText(filename.substring(0, filename.length() - 4));
					System.out.println(filename);

				}

			}

			final MenuItem kannada = new MenuItem(menu, SWT.CASCADE);
			kannada.setText("Kannada");
			final Menu submenu_kan = new Menu(shell, SWT.DROP_DOWN);
			kannada.setMenu(submenu_kan);
			final MenuItem kgp = new MenuItem(submenu_kan, SWT.PUSH);
			kgp.setText("KaGaPa");
			final MenuItem inscript = new MenuItem(submenu_kan, SWT.PUSH);
			inscript.setText("Microsoft Inscript");

			final MenuItem gujarati = new MenuItem(menu, SWT.CASCADE);
			gujarati.setText("Gujarati");
			final Menu submenu_guj = new Menu(shell, SWT.DROP_DOWN);
			gujarati.setMenu(submenu_guj);
			final MenuItem gujLay = new MenuItem(submenu_guj, SWT.PUSH);
			gujLay.setText("Gujarati Layout");

			final MenuItem marathi = new MenuItem(menu, SWT.CASCADE);
			marathi.setText("Marathi");
			final Menu submenu_mar = new Menu(shell, SWT.DROP_DOWN);
			marathi.setMenu(submenu_mar);
			final MenuItem marLay = new MenuItem(submenu_mar, SWT.PUSH);
			marLay.setText("Marathi Layout");

			final MenuItem hindi = new MenuItem(menu, SWT.CASCADE);
			hindi.setText("Hindi");
			final Menu submenu_hin = new Menu(shell, SWT.DROP_DOWN);
			hindi.setMenu(submenu_hin);
			final MenuItem hinLay = new MenuItem(submenu_hin, SWT.PUSH);
			hinLay.setText("Hindi Layout");

			final MenuItem tamil = new MenuItem(menu, SWT.CASCADE);
			tamil.setText("Tamil");
			final Menu submenu_tam = new Menu(shell, SWT.DROP_DOWN);
			tamil.setMenu(submenu_tam);
			final MenuItem tamil99 = new MenuItem(submenu_tam, SWT.PUSH);
			tamil99.setText("Tamilnet99");

			new MenuItem(menu, SWT.SEPARATOR);

			final MenuItem help = new MenuItem(menu, SWT.PUSH);
			help.setText("Help");

			new MenuItem(menu, SWT.SEPARATOR);

			final MenuItem links = new MenuItem(menu, SWT.CASCADE);
			links.setText("Useful Links");
			final Menu submenu2 = new Menu(shell, SWT.DROP_DOWN);
			links.setMenu(submenu2);
			final MenuItem langtech = new MenuItem(submenu2, SWT.PUSH);
			langtech.setText("Language Technology");
			final MenuItem codegoogle = new MenuItem(submenu2, SWT.PUSH);
			codegoogle.setText("CKI code.google.com");
			final MenuItem ckidev = new MenuItem(submenu2, SWT.PUSH);
			ckidev.setText("CKI Developers Google group");
			final MenuItem ckiuser = new MenuItem(submenu2, SWT.PUSH);
			ckiuser.setText("CKI Users Google group");

			new MenuItem(menu, SWT.SEPARATOR);

			final MenuItem exit = new MenuItem(menu, SWT.PUSH);
			exit.setText("Exit");

			exit.addListener(SWT.Selection, new Listener() {

				public void handleEvent(Event event) {

					// String q=s.substring(28, 35);
					String d = exit.getText();
					exit.setSelection(false);
					if (d.compareTo("Exit") == 0) {

						Shell sh = new Shell(display, SWT.APPLICATION_MODAL);
						sh.setImage(image);
						MessageBox messageBox = new MessageBox(sh, SWT.YES
								| SWT.NO | SWT.ICON_QUESTION);
						messageBox.setText("Exit...");

						messageBox.setMessage("Are you sure to quit CKI??");
						int rCode = messageBox.open();
						if (rCode == SWT.YES) {
							image.dispose();
							shell.dispose();
							display.dispose();
							System.exit(0);

						} else {

							sh.dispose();
						}
					}
				}
			});

			help.addListener(SWT.Selection, new Listener() {

				public void handleEvent(Event event) {

					// String q=s.substring(28, 35);
					String d = help.getText();
					exit.setSelection(false);
					if (d.compareTo("Help") == 0) {

						Shell sh = new Shell(display, SWT.APPLICATION_MODAL);
						sh.setImage(image);
						MessageBox messageBox = new MessageBox(sh, SWT.YES
								| SWT.NO | SWT.ICON_QUESTION);
						messageBox.setText("Opening CKI's help...");

						messageBox
								.setMessage("Dou you want to open CKI's help ??\n\n(Requires an Internet connection)");
						int rCode = messageBox.open();
						if (rCode == SWT.YES) {
							/*
							 * Opens the default browser. Or any program which
							 * opens .html files by default
							 */
							Program pBrowse = Program.findProgram(".html");

							pBrowse
									.execute("http://groups.google.com/group/cki-users/web/help-for-users");

						} else {

							sh.dispose();
						}
					}
				}
			});

			// All the kannada layouts go here
			{

				kgp.addListener(SWT.Selection, new Listener() {

					public void handleEvent(Event event) {
						String s = event.toString();
						// String q=s.substring(28, 35);

						kgp.setSelection(false);
						{
							String qk = s.substring(25, 31);

							Shell sh = new Shell(display);
							sh.setImage(image);
							MessageBox messageBox = new MessageBox(sh, SWT.OK
									| SWT.ICON_INFORMATION);
							messageBox.setText("Language Selection");
							messageBox.setMessage("You have selected " + qk);
							messageBox.open();
							sh.dispose();
						}
					}
				});

				inscript.addListener(SWT.Selection, new Listener() {

					public void handleEvent(Event event) {
						String s = event.toString();
						// String q=s.substring(28, 35);

						inscript.setSelection(false);
						{
							String qk = s.substring(25, 43);

							Shell sh = new Shell(display);
							sh.setImage(image);
							MessageBox messageBox = new MessageBox(sh, SWT.OK
									| SWT.ICON_INFORMATION);
							messageBox.setText("Language Selection");

							messageBox.setMessage("You have selected " + qk);
							messageBox.open();
							sh.dispose();

						}
					}
				});

			}// End of kannada section

			// All the tamil layouts go here
			{
				tamil99.addListener(SWT.Selection, new Listener() {

					public void handleEvent(Event event) {
						String s = event.toString();
						// String q=s.substring(28, 35);

						tamil99.setSelection(false);
						{
							String qt = s.substring(25, 35);

							Shell sh1 = new Shell(display);
							sh1.setImage(image);
							MessageBox mBox1 = new MessageBox(sh1, SWT.OK
									| SWT.ICON_INFORMATION);
							mBox1.setText("Language Selection");

							mBox1.setMessage("You have selected " + qt);
							mBox1.open();

							sh1.dispose();

						}

					}
				});

			}// End of tamil section

			langtech.addListener(SWT.Selection, new Listener() {

				public void handleEvent(Event event) {

					Program pBrowse = Program.findProgram(".html");
					pBrowse.execute("http://languagetechnology.org");

				}
			});

			codegoogle.addListener(SWT.Selection, new Listener() {

				public void handleEvent(Event event) {

					Program pBrowse = Program.findProgram(".html");
					pBrowse.execute("http://code.google.com/p/cki");

				}
			});

			ckidev.addListener(SWT.Selection, new Listener() {

				public void handleEvent(Event event) {

					Program pBrowse = Program.findProgram(".html");
					pBrowse.execute("http://groups.google.com/group/cki-dev/");

				}
			});

			ckiuser.addListener(SWT.Selection, new Listener() {

				public void handleEvent(Event event) {

					Program pBrowse = Program.findProgram(".html");
					pBrowse.execute("http://groups.google.com/group/cki-users");

				}
			});

			item.addListener(SWT.MenuDetect, new Listener() {

				public void handleEvent(Event event) {
					menu.setVisible(true);
				}
			});
			item.setImage(image);
		}// end of else

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		image.dispose();
		shell.dispose();
		display.dispose();
		// label.dispose(); part of the previous segment of code.

	}
}
