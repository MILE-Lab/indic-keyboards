/** ********************************************************************
 * File:           UI.java 
 * Description:    File which creates the tray icon and all other menus.
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
import org.eclipse.swt.widgets.ToolTip;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;

public class UI {
	// Creating the main display.Since SWT 3.1, support for multiple
	// displays has been
	// discontinued. SO at a time, only one active display can exist.

	static Image image;

	public static Shell shell1 = new Shell(Display.getCurrent());
	public static ToolTip tip = new ToolTip(shell1, SWT.BALLOON
			| SWT.ICON_INFORMATION);

	public UI() {

		// the image path. The image File must be in the same directory/package
		// as that of the source file
		final Display display = Display.getCurrent();
		final Shell shell = new Shell(display);
		image = new Image(display, IndicKeyboards.class
				.getResourceAsStream("trayicon.ico"));

		// Creating the tray icon
		final Tray tray = display.getSystemTray();
		System.out.println("No. of tray Items : " + tray.getItemCount());
		tip.setMessage("Right click the tray icon to get going.");

		if (tray == null) {
			System.err.println("The system tray is not available");
			tip.setText("Notification from anywhere");
			tip.setLocation(400, 400);
		} else {
			final TrayItem item = new TrayItem(tray, SWT.NONE);
			item.setImage(image);
			item
					.setToolTipText("indic-keyboards\nDouble click to visit homepage");

			tip.setText("indic-keyboards");
			item.setToolTip(tip);
			tip.setVisible(true);

			/* DefaultSelection is Double-click */
			item.addListener(SWT.DefaultSelection, new Listener() {

				public void handleEvent(Event event) {

					Shell sh = new Shell(display, SWT.APPLICATION_MODAL);
					sh.setImage(image);
					MessageBox messageBox = new MessageBox(sh, SWT.YES | SWT.NO
							| SWT.ICON_QUESTION);
					messageBox.setText("Open Browser");

					messageBox
							.setMessage("Dou you want to open indic-keyboards' webpage??\n\n(Opens in the default internet browser)");
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
			// URL url1 = IndicKeyboards.class.getResource("kblayoutxmls");
			File dirname = new File(System.getProperty("user.dir"),
					"/kblayouts/userdefined");

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
					final String filename = userDefinedLayouts[i];
					final MenuItem mi = new MenuItem(submenu_user_def, SWT.PUSH);
					mi.setText(filename.substring(0, filename.length() - 4));
					// System.out.println(filename);

					// Listeners for the user defined keyboard layout menu items
					mi.addListener(SWT.Selection, new Listener() {

						public void handleEvent(Event event) {
							// String s = event.toString();
							// String q=s.substring(28, 35);

							mi.setSelection(false);
							{
								/*
								 * String qk = filename.substring(0, filename
								 * .length() - 4);
								 * 
								 * Shell sh = new Shell(display);
								 * sh.setImage(image); MessageBox messageBox =
								 * new MessageBox(sh, SWT.OK |
								 * SWT.ICON_INFORMATION);
								 * messageBox.setText("Language Selection");
								 * messageBox .setMessage("You have selected " +
								 * qk); messageBox.open(); sh.dispose();
								 */
								tip.setMessage(filename.substring(0, filename
										.length() - 4));
								tip.setVisible(true);

								Image image1 = new Image(display,
										IndicKeyboards.class
												.getResourceAsStream("UD.ico"));
								item.setImage(image1);
								item
										.setToolTipText("indic-keyboards - User Defined - "
												+ filename.substring(0,
														filename.length() - 4));
								ParseXML.setlang("userdefined/" + filename);
								PhoneticParseXML.PhoneticFlag = 0;

							}
						}
					});
					// End of listener

				}

			}

			final MenuItem kannada = new MenuItem(menu, SWT.CASCADE);
			kannada.setText("Kannada");
			final Menu submenu_kan = new Menu(shell, SWT.DROP_DOWN);
			kannada.setMenu(submenu_kan);
			final MenuItem kgp = new MenuItem(submenu_kan, SWT.PUSH);
			kgp.setText("KaGaPa");
			final MenuItem kanInscript = new MenuItem(submenu_kan, SWT.PUSH);
			kanInscript.setText("Inscript");
			new MenuItem(submenu_kan, SWT.SEPARATOR);
			final MenuItem kanPhonetic = new MenuItem(submenu_kan, SWT.PUSH);
			kanPhonetic.setText("Phonetic");

			final MenuItem gujarati = new MenuItem(menu, SWT.CASCADE);
			gujarati.setText("Gujarati");
			final Menu submenu_guj = new Menu(shell, SWT.DROP_DOWN);
			gujarati.setMenu(submenu_guj);
			final MenuItem gujLay = new MenuItem(submenu_guj, SWT.PUSH);
			gujLay.setText("Gujarati Layout");
			final MenuItem gujInscript = new MenuItem(submenu_guj, SWT.PUSH);
			gujInscript.setText("Inscript");

			final MenuItem marathi = new MenuItem(menu, SWT.CASCADE);
			marathi.setText("Marathi");
			final Menu submenu_mar = new Menu(shell, SWT.DROP_DOWN);
			marathi.setMenu(submenu_mar);
			final MenuItem marLay = new MenuItem(submenu_mar, SWT.PUSH);
			marLay.setText("Remington");
			final MenuItem marInscript = new MenuItem(submenu_mar, SWT.PUSH);
			marInscript.setText("Inscript");

			final MenuItem hindi = new MenuItem(menu, SWT.CASCADE);
			hindi.setText("Hindi");
			final Menu submenu_hin = new Menu(shell, SWT.DROP_DOWN);
			hindi.setMenu(submenu_hin);
			final MenuItem hinLay = new MenuItem(submenu_hin, SWT.PUSH);
			hinLay.setText("Remington");
			final MenuItem hinInscript = new MenuItem(submenu_hin, SWT.PUSH);
			hinInscript.setText("Inscript");

			final MenuItem tamil = new MenuItem(menu, SWT.CASCADE);
			tamil.setText("Tamil");
			final Menu submenu_tam = new Menu(shell, SWT.DROP_DOWN);
			tamil.setMenu(submenu_tam);
			final MenuItem tamil99 = new MenuItem(submenu_tam, SWT.PUSH);
			tamil99.setText("Tamilnet99");
			final MenuItem tamInscript = new MenuItem(submenu_tam, SWT.PUSH);
			tamInscript.setText("Inscript");
			final MenuItem tamRemington = new MenuItem(submenu_tam, SWT.PUSH);
			tamRemington.setText("Remington");

			final MenuItem telugu = new MenuItem(menu, SWT.CASCADE);
			telugu.setText("Telugu");
			final Menu submenu_tel = new Menu(shell, SWT.DROP_DOWN);
			telugu.setMenu(submenu_tel);
			final MenuItem telLay = new MenuItem(submenu_tel, SWT.PUSH);
			telLay.setText("Telugu Layout");
			final MenuItem telInscript = new MenuItem(submenu_tel, SWT.PUSH);
			telInscript.setText("Inscript");

			new MenuItem(menu, SWT.SEPARATOR);
			final MenuItem addLayouts = new MenuItem(menu, SWT.CASCADE);
			addLayouts.setText("Add New Layouts");
			final Menu submenu_newLay = new Menu(shell, SWT.DROP_DOWN);
			addLayouts.setMenu(submenu_newLay);

			final MenuItem XMLCreateIns = new MenuItem(submenu_newLay, SWT.PUSH);
			XMLCreateIns.setText("Add Inscript Layout");

			final MenuItem XMLCreateOth = new MenuItem(submenu_newLay, SWT.PUSH);
			XMLCreateOth.setText("Add Other Layout");

			new MenuItem(menu, SWT.SEPARATOR);
			final MenuItem enableDisable = new MenuItem(menu, SWT.PUSH);
			enableDisable.setText("Enable/Disable [F12]");
			enableDisable.addListener(SWT.Selection, new Listener() {

				public void handleEvent(Event event) {
					IndicKeyboards.enable = ! IndicKeyboards.enable;
					
					if(IndicKeyboards.enable){
						tip.setMessage("Enabled");
						tip.setVisible(true);
					}
					else{
						tip.setMessage("Disabled");
						tip.setVisible(true);
					}
				}
			});

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
			codegoogle.setText("indic-keyboards code.google.com");
			final MenuItem IKdev = new MenuItem(submenu2, SWT.PUSH);
			IKdev.setText("indic-keyboards Developer mailing list");
			final MenuItem IKscm = new MenuItem(submenu2, SWT.PUSH);
			IKscm.setText("indic-keyboards SCM group");
			final MenuItem IKuser = new MenuItem(submenu2, SWT.PUSH);
			IKuser.setText("indic-keyboards User mailing list");

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

						messageBox
								.setMessage("Are you sure to quit indic-keyboards??");
						int rCode = messageBox.open();
						if (rCode == SWT.YES) {
							image.dispose();
							shell.dispose();
							display.dispose();
							/*
							 * When the program stops running, if the Key
							 * Logging is enabled, auto repeat will stop
							 * working. So make sure that auto repeat is ON when
							 * the program quits.
							 */
							System.runFinalization();
							System.exit(0);
						} else {

							sh.dispose();
						}
					}
				}
			});

			// Listner for Enable/Disable
			enableDisable.addListener(SWT.Selection, new Listener() {

				public void handleEvent(Event event) {

					// This is to enable and disable the software using the
					// menu.
					/*
					 * if(IndicKeyboards.enable==true)
					 * IndicKeyboards.enable=false; else
					 * IndicKeyboards.enable=true;
					 */
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
						messageBox.setText("Opening indic-keyboards' help...");

						messageBox
								.setMessage("Dou you want to open indic-keyboards' help ??\n\n(Requires an Internet connection)");
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

			// XML Create Listener for Inscript like keyboards
			XMLCreateIns.addListener(SWT.Selection, new Listener() {

				public void handleEvent(Event event) {

					// String q=s.substring(28, 35);
					String d = XMLCreateIns.getText();
					exit.setSelection(false);
					if (d.compareTo("Add Inscript Layout") == 0) {

						XMLGenerator.GenerateXMLUI();

					} else {

					}
				}
			});

			// XML Create Listener for other keyboards
			XMLCreateOth.addListener(SWT.Selection, new Listener() {

				public void handleEvent(Event event) {

					// String q=s.substring(28, 35);
					String d = XMLCreateOth.getText();
					exit.setSelection(false);
					if (d.compareTo("Add Other Layout") == 0) {

						XMLGenerator.GenerateXMLUI();

					} else {

					}
				}
			});

			// All the kannada layouts go here
			{

				kgp.addListener(SWT.Selection, new Listener() {

					public void handleEvent(Event event) {
						// String s = event.toString();
						// String q=s.substring(28, 35);

						kgp.setSelection(false);
						{
							// String qk = s.substring(25, 31);

							/*
							 * Shell sh = new Shell(display);
							 * sh.setImage(image); MessageBox messageBox = new
							 * MessageBox(sh, SWT.OK | SWT.ICON_INFORMATION);
							 * messageBox.setText("Language Selection");
							 * messageBox
							 * .setMessage("You have selected Kannada - KaGaPa"
							 * ); messageBox.open(); sh.dispose();
							 */
							tip.setMessage("KaGaPa");
							tip.setVisible(true);
							ParseXML.setlang("kan_kagapa.xml");
							PhoneticParseXML.PhoneticFlag = 0;

							Image image1 = new Image(display,
									IndicKeyboards.class
											.getResourceAsStream("kan.ico"));
							item.setImage(image1);
							item
									.setToolTipText("indic-keyboards - KANNADA KaGaPa");
						}
					}
				});

				kanInscript.addListener(SWT.Selection, new Listener() {

					public void handleEvent(Event event) {
						// String s = event.toString();
						// String q=s.substring(28, 35);

						kanInscript.setSelection(false);
						{
							// String qk = s.substring(25, 43);

							/*
							 * Shell sh = new Shell(display);
							 * sh.setImage(image); MessageBox messageBox = new
							 * MessageBox(sh, SWT.OK | SWT.ICON_INFORMATION);
							 * messageBox.setText("Language Selection");
							 * 
							 * messageBox
							 * .setMessage("You have selected Kannada Inscript"
							 * ); messageBox.open(); sh.dispose();
							 */
							tip.setMessage("Kannada Inscript");
							tip.setVisible(true);
							ParseXML.setlang("kan_inscript.xml");
							PhoneticParseXML.PhoneticFlag = 0;

							Image image1 = new Image(display,
									IndicKeyboards.class
											.getResourceAsStream("kan.ico"));
							item.setImage(image1);
							item
									.setToolTipText("indic-keyboards - KANNADA Inscript");
						}
					}
				});

				kanPhonetic.addListener(SWT.Selection, new Listener() {

					public void handleEvent(Event event) {
						// String s = event.toString();
						// String q=s.substring(28, 35);

						kanPhonetic.setSelection(false);
						{
							// String qk = s.substring(25, 43);

							/*
							 * Shell sh = new Shell(display);
							 * sh.setImage(image); MessageBox messageBox = new
							 * MessageBox(sh, SWT.OK | SWT.ICON_INFORMATION);
							 * messageBox.setText("Language Selection");
							 * 
							 * messageBox
							 * .setMessage("You have selected Kannada Inscript"
							 * ); messageBox.open(); sh.dispose();
							 */
							tip.setMessage("Kannada Phonetic");
							tip.setVisible(true);
							PhoneticParseXML.setlang("kan_phonetic.xml");
							PhoneticParseXML.PhoneticFlag = 1;
							Image image1 = new Image(display,
									IndicKeyboards.class
											.getResourceAsStream("kan.ico"));
							item.setImage(image1);
							item
									.setToolTipText("indic-keyboards - KANNADA Phonetic");
						}
					}
				});
			}// End of kannada section

			// All the tamil layouts go here
			{
				tamil99.addListener(SWT.Selection, new Listener() {

					public void handleEvent(Event event) {
						// String s = event.toString();
						// String q=s.substring(28, 35);

						tamil99.setSelection(false);
						{
							// String qt = s.substring(25, 35);

							/*
							 * Shell sh1 = new Shell(display);
							 * sh1.setImage(image); MessageBox mBox1 = new
							 * MessageBox(sh1, SWT.OK | SWT.ICON_INFORMATION);
							 * mBox1.setText("Language Selection");
							 * 
							 * mBox1
							 * .setMessage("You have selected Tamil - Tamilnet99"
							 * ); mBox1.open();
							 * 
							 * sh1.dispose();
							 */
							tip.setMessage("Tamilnet99");
							tip.setVisible(true);
							ParseXML.setlang("tamilnet99.xml");
							PhoneticParseXML.PhoneticFlag = 0;

							Image image1 = new Image(display,
									IndicKeyboards.class
											.getResourceAsStream("tamil.ico"));
							item.setImage(image1);
							item
									.setToolTipText("indic-keyboards - TAMIL Tamilnet99");
						}

					}
				});

				tamRemington.addListener(SWT.Selection, new Listener() {

					public void handleEvent(Event event) {
						// String s = event.toString();
						// String q=s.substring(28, 35);

						tamRemington.setSelection(false);
						{
							// String qt = s.substring(25, 35);

							/*
							 * Shell sh1 = new Shell(display);
							 * sh1.setImage(image); MessageBox mBox1 = new
							 * MessageBox(sh1, SWT.OK | SWT.ICON_INFORMATION);
							 * mBox1.setText("Language Selection");
							 * 
							 * mBox1
							 * .setMessage("You have selected Tamil - Tamilnet99"
							 * ); mBox1.open();
							 * 
							 * sh1.dispose();
							 */
							tip.setMessage("Tamil Remington");
							tip.setVisible(true);
							ParseXML.setlang("tam_remington_inscript.xml");
							PhoneticParseXML.PhoneticFlag = 0;

							Image image1 = new Image(display,
									IndicKeyboards.class
											.getResourceAsStream("tamil.ico"));
							item.setImage(image1);
							item
									.setToolTipText("indic-keyboards - TAMIL Remington");
						}

					}
				});
				tamInscript.addListener(SWT.Selection, new Listener() {

					public void handleEvent(Event event) {
						// String s = event.toString();
						// String q=s.substring(28, 35);

						tamInscript.setSelection(false);
						{
							// String qt = s.substring(25, 35);

							/*
							 * Shell sh1 = new Shell(display);
							 * sh1.setImage(image); MessageBox mBox1 = new
							 * MessageBox(sh1, SWT.OK | SWT.ICON_INFORMATION);
							 * mBox1.setText("Language Selection");
							 * 
							 * mBox1
							 * .setMessage("You have selected Tamil Inscript");
							 * mBox1.open();
							 * 
							 * sh1.dispose();
							 */
							tip.setMessage("Tamil Inscript");
							tip.setVisible(true);
							ParseXML.setlang("tam_inscript.xml");
							PhoneticParseXML.PhoneticFlag = 0;

							Image image1 = new Image(display,
									IndicKeyboards.class
											.getResourceAsStream("tamil.ico"));
							item.setImage(image1);
							item
									.setToolTipText("indic-keyboards - TAMIL Inscript");
						}

					}
				});

			}// End of tamil section

			// All telugu layours go here
			{
				telLay.addListener(SWT.Selection, new Listener() {

					public void handleEvent(Event event) {
						// String s = event.toString();
						// String q=s.substring(28, 35);

						telLay.setSelection(false);
						{
							// String qt = s.substring(25, 35);

							/*
							 * Shell sh1 = new Shell(display);
							 * sh1.setImage(image); MessageBox mBox1 = new
							 * MessageBox(sh1, SWT.OK | SWT.ICON_INFORMATION);
							 * mBox1.setText("Language Selection");
							 * 
							 * 
							 * 
							 * mBox1.setMessage("You have selected Telugu Layout"
							 * ); mBox1.open();
							 * 
							 * sh1.dispose();
							 */
							tip.setMessage("Telugu Layout");
							tip.setVisible(true);
							ParseXML.setlang("tel_inscript.xml");
							PhoneticParseXML.PhoneticFlag = 0;

							Image image1 = new Image(display,
									IndicKeyboards.class
											.getResourceAsStream("telugu.ico"));
							item.setImage(image1);
							item.setToolTipText("indic-keyboards - TELUGU");
						}

					}
				});
				// All telugu layouts go here.
				telInscript.addListener(SWT.Selection, new Listener() {

					public void handleEvent(Event event) {
						// String s = event.toString();
						// String q=s.substring(28, 35);

						telInscript.setSelection(false);
						{
							// String qt = s.substring(25, 35);

							/*
							 * Shell sh1 = new Shell(display);
							 * sh1.setImage(image); MessageBox mBox1 = new
							 * MessageBox(sh1, SWT.OK | SWT.ICON_INFORMATION);
							 * mBox1.setText("Language Selection");
							 * 
							 * mBox1
							 * .setMessage("You have selected Telugu Inscript");
							 * mBox1.open();
							 * 
							 * sh1.dispose();
							 */
							tip.setMessage("Telugu Inscript");
							tip.setVisible(true);
							ParseXML.setlang("tel_inscript.xml");
							PhoneticParseXML.PhoneticFlag = 0;

							Image image1 = new Image(display,
									IndicKeyboards.class
											.getResourceAsStream("telugu.ico"));
							item.setImage(image1);
							item
									.setToolTipText("indic-keyboards - TELUGU Inscript");
						}

					}
				});

			}

			// All the gujarati layouts go here
			{
				gujLay.addListener(SWT.Selection, new Listener() {

					public void handleEvent(Event event) {
						// String s = event.toString();
						// String q=s.substring(28, 35);

						gujLay.setSelection(false);
						{
							// String qt = s.substring(25, 35);

							/*
							 * Shell sh1 = new Shell(display);
							 * sh1.setImage(image); MessageBox mBox1 = new
							 * MessageBox(sh1, SWT.OK | SWT.ICON_INFORMATION);
							 * mBox1.setText("Language Selection");
							 * 
							 * mBox1.setMessage("You have selected Gujrati");
							 * mBox1.open();
							 * 
							 * sh1.dispose();
							 */
							tip.setMessage("Gujrathi Layout");
							tip.setVisible(true);
							ParseXML.setlang("guj_inscript.xml");
							PhoneticParseXML.PhoneticFlag = 0;

							Image image1 = new Image(display,
									IndicKeyboards.class
											.getResourceAsStream("guj.ico"));
							item.setImage(image1);
							item.setToolTipText("indic-keyboards - GUJRATI");

						}

					}
				});

				gujInscript.addListener(SWT.Selection, new Listener() {

					public void handleEvent(Event event) {
						// String s = event.toString();
						// String q=s.substring(28, 35);

						gujInscript.setSelection(false);
						{
							// String qt = s.substring(25, 35);

							/*
							 * Shell sh1 = new Shell(display);
							 * sh1.setImage(image); MessageBox mBox1 = new
							 * MessageBox(sh1, SWT.OK | SWT.ICON_INFORMATION);
							 * mBox1.setText("Language Selection");
							 * 
							 * mBox1
							 * .setMessage("You have selected Gujrati Inscript"
							 * ); mBox1.open();
							 * 
							 * sh1.dispose();
							 */
							tip.setMessage("Gujrathi Inscript");
							tip.setVisible(true);
							ParseXML.setlang("guj_inscript.xml");
							PhoneticParseXML.PhoneticFlag = 0;

							Image image1 = new Image(display,
									IndicKeyboards.class
											.getResourceAsStream("guj.ico"));
							item.setImage(image1);
							item
									.setToolTipText("indic-keyboards - GUJRATI Inscript");

						}

					}
				});

			}// End of gujarati section

			// All the Hindi layouts go here
			{
				hinLay.addListener(SWT.Selection, new Listener() {

					public void handleEvent(Event event) {
						// String s = event.toString();
						// String q=s.substring(28, 35);

						hinLay.setSelection(false);
						{
							// String qt = s.substring(25, 35);

							/*
							 * Shell sh1 = new Shell(display);
							 * sh1.setImage(image); MessageBox mBox1 = new
							 * MessageBox(sh1, SWT.OK | SWT.ICON_INFORMATION);
							 * mBox1.setText("Language Selection");
							 * 
							 * mBox1.setMessage("You have selected Hindi");
							 * mBox1.open();
							 * 
							 * sh1.dispose();
							 */
							tip.setMessage("Hindi Remington");
							tip.setVisible(true);
							ParseXML.setlang("hin_remington_inscript.xml");
							PhoneticParseXML.PhoneticFlag = 0;

							Image image1 = new Image(display,
									IndicKeyboards.class
											.getResourceAsStream("hin.ico"));
							item.setImage(image1);
							item.setToolTipText("indic-keyboards - HINDI");

						}

					}
				});

				hinInscript.addListener(SWT.Selection, new Listener() {

					public void handleEvent(Event event) {
						// String s = event.toString();
						// String q=s.substring(28, 35);

						hinInscript.setSelection(false);
						{
							// String qt = s.substring(25, 35);

							/*
							 * Shell sh1 = new Shell(display);
							 * sh1.setImage(image); MessageBox mBox1 = new
							 * MessageBox(sh1, SWT.OK | SWT.ICON_INFORMATION);
							 * mBox1.setText("Language Selection");
							 * 
							 * mBox1
							 * .setMessage("You have selected Hindi Inscript");
							 * mBox1.open();
							 * 
							 * 
							 * sh1.dispose();
							 */
							tip.setMessage("Hindi Inscript");
							tip.setVisible(true);

							ParseXML.setlang("hin_inscript.xml");
							PhoneticParseXML.PhoneticFlag = 0;

							Image image1 = new Image(display,
									IndicKeyboards.class
											.getResourceAsStream("hin.ico"));
							item.setImage(image1);
							item
									.setToolTipText("indic-keyboards - HINDI Inscript");

						}

					}
				});

			}// End of hindi section

			// All the marathi layouts go here
			{
				marLay.addListener(SWT.Selection, new Listener() {

					public void handleEvent(Event event) {
						// String s = event.toString();
						// String q=s.substring(28, 35);

						marLay.setSelection(false);
						{
							// String qt = s.substring(25, 35);

							/*
							 * Shell sh1 = new Shell(display);
							 * sh1.setImage(image); MessageBox mBox1 = new
							 * MessageBox(sh1, SWT.OK | SWT.ICON_INFORMATION);
							 * mBox1.setText("Language Selection");
							 * 
							 * mBox1.setMessage("You have selected Marathi");
							 * mBox1.open();
							 * 
							 * sh1.dispose();
							 */
							tip.setMessage("Marathi Layout");
							tip.setVisible(true);
							ParseXML.setlang("mar_inscript.xml");
							PhoneticParseXML.PhoneticFlag = 0;

							Image image1 = new Image(display,
									IndicKeyboards.class
											.getResourceAsStream("hin.ico"));
							item.setImage(image1);
							item.setToolTipText("indic-keyboards - MARATHI");
						}

					}
				});

				marInscript.addListener(SWT.Selection, new Listener() {

					public void handleEvent(Event event) {
						// String s = event.toString();
						// String q=s.substring(28, 35);

						marInscript.setSelection(false);
						{
							// String qt = s.substring(25, 35);

							/*
							 * Shell sh1 = new Shell(display);
							 * sh1.setImage(image); MessageBox mBox1 = new
							 * MessageBox(sh1, SWT.OK | SWT.ICON_INFORMATION);
							 * mBox1.setText("Language Selection");
							 * 
							 * mBox1
							 * .setMessage("You have selected Marathi Inscript"
							 * ); mBox1.open();
							 * 
							 * sh1.dispose();
							 */
							tip.setMessage("Marathi Inscript");
							tip.setVisible(true);
							ParseXML.setlang("mar_inscript.xml");
							PhoneticParseXML.PhoneticFlag = 0;

							Image image1 = new Image(display,
									IndicKeyboards.class
											.getResourceAsStream("hin.ico"));
							item.setImage(image1);
							item
									.setToolTipText("indic-keyboards - MARATHI Inscript");
						}

					}
				});

			}// End of marathi section

			langtech.addListener(SWT.Selection, new Listener() {

				public void handleEvent(Event event) {

					Program pBrowse = Program.findProgram(".html");
					pBrowse.execute("http://languagetechnology.org");

				}
			});

			codegoogle.addListener(SWT.Selection, new Listener() {

				public void handleEvent(Event event) {

					Program pBrowse = Program.findProgram(".html");
					pBrowse.execute("http://code.google.com/p/indic-keyboards");

				}
			});

			IKdev.addListener(SWT.Selection, new Listener() {

				public void handleEvent(Event event) {

					Program pBrowse = Program.findProgram(".html");
					pBrowse.execute("http://groups.google.com/group/cki-dev/");

				}
			});

			IKscm.addListener(SWT.Selection, new Listener() {

				public void handleEvent(Event event) {

					Program pBrowse = Program.findProgram(".html");
					pBrowse
							.execute("http://groups.google.com/group/indic-keyboards-scm");

				}
			});

			IKuser.addListener(SWT.Selection, new Listener() {

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

			item.addListener(SWT.Selection, new Listener() {

				public void handleEvent(Event event) {
					tip.setVisible(true);
				}
			});

		}// end of else

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		image.dispose();
		shell.dispose();
		display.dispose();

	}
}
