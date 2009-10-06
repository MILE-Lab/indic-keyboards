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

package org.iisc.mile.indickeyboards;

import java.io.File;
import java.io.IOException;

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
import org.iisc.mile.indickeyboards.linux.InitLinux;
import org.iisc.mile.indickeyboards.linux.KeyMonitorMethods;
import org.iisc.mile.indickeyboards.windows.InitWin;
import sun.management.ManagementFactory;

/**
 * Creating the Main User Interface. Uses SWT.
 * <p>
 * Only one active Display can exist at a time. Create the Display. This class
 * takes care of creating the user interface with consists of the System Tray
 * object and it's functionality. The system tray consists of languages with the
 * keyboard layouts which is used to allow the user to select one for use.
 * <p>
 * Event LIsteners are used to listen to click events in the pop up menu.
 * 
 */
public class UI {

	/**
	 * Image variable used to load images. Used to load the Tray Icon image.
	 */
	static Image image;
	/**
	 * Create a shell object
	 */
	public static Shell shell1 = new Shell(Display.getCurrent());
	/**
	 * Tool tip object. This is used to display software notifications
	 */
	public static ToolTip tip = new ToolTip(shell1, SWT.BALLOON
			| SWT.ICON_INFORMATION);
	/**
	 * This variable is used to store the image of the current layout selected.
	 */
	public static String layoutImg;

	/**
	 * MenuItem for the Enable/Disable functionality.
	 */
	public static MenuItem enableDisable;

	/**
	 * <em>Image</em> Object used for Enable/Disable functionality.
	 */
	public static Image previousKeyboardIcon;
	
	/**
	 * <em>Tray</em> Object that is to be present in the system tray.
	 */
	public Tray tray;
	
	/**
	 * A <em>TrayItem</em> object which creates a new tray item in the
	 * System Tray/Notification Area.
	 */
	public static TrayItem item;

	/**
	 * This constructor is used to start up the user interface. It is then used
	 * to Listen for events and take the necessary action for the selection made
	 * by the user.
	 */
	public UI() {

		/**
		 * The display object is used to get the current active display. This
		 * variable is used to perform all Display canvas related task
		 */
		final Display display = Display.getCurrent();
		/**
		 * Shell object
		 */
		final Shell shell = new Shell(display);
		/*
		 * The image File must be in the same directory/package as that of the
		 * source file. (Image Path)
		 */
		image = new Image(display, IndicKeyboards.workingDirectory
				+ "/resources/trayicon.ico");

		previousKeyboardIcon = image;

		/**
		 * Initialise the tray object. Obtain the resources to the 
		 * System Tray/Notification area from the underlying platform.
		 */
		tray = display.getSystemTray();

		// @debug
		System.out.println("No. of tray Items : " + tray.getItemCount());
		// Set the tool tip message to the following.
		tip.setMessage("Right click the tray icon to get going.");

		/*
		 * Check if tray object can be created.
		 */
		if (tray == null) {
			System.err.println("The system tray is not available");
			tip.setText("Notification from anywhere");
			tip.setLocation(400, 400);
		} else {
			item = new TrayItem(tray, SWT.NONE);
			item.setImage(image);
			item.setToolTipText("indic-keyboards\nDouble click to visit homepage");

			tip.setText("indic-keyboards");
			item.setToolTip(tip);
			tip.setVisible(true);

			/* DefaultSelection is Double-click */
			item.addListener(SWT.DefaultSelection, new Listener() {
				/**
				 * Method handles the double click mouse event
				 */
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

						pBrowse
								.execute("http://code.google.com/p/indic-keyboards");

					} else {

						sh.dispose();
					}

				}
			});

			/**
			 * Menu object contains the Items - Languages, Help, Links, About,
			 * Userdefined Keyboard Layouts..
			 */
			final Menu menu = new Menu(shell, SWT.POP_UP); /*
															 * The main menu
															 * obtained when the
															 * tray is right
															 * clicked
															 */
			/* Used to load user defined keyboard layouts */
			final MenuItem currentLayout = new MenuItem(menu, SWT.PUSH);
			currentLayout.setText("Show Current Layout");
			currentLayout.setEnabled(false);
			new MenuItem(menu, SWT.SEPARATOR);

			final MenuItem bengali = new MenuItem(menu, SWT.CASCADE);
			bengali.setText("Bengali");
			final Menu submenu_ben = new Menu(shell, SWT.DROP_DOWN);
			bengali.setMenu(submenu_ben);
			final MenuItem benInscript = new MenuItem(submenu_ben, SWT.PUSH);
			benInscript.setText("Inscript");

			final MenuItem gujarati = new MenuItem(menu, SWT.CASCADE);
			gujarati.setText("Gujarati");
			final Menu submenu_guj = new Menu(shell, SWT.DROP_DOWN);
			gujarati.setMenu(submenu_guj);
			final MenuItem gujInscript = new MenuItem(submenu_guj, SWT.PUSH);
			gujInscript.setText("Inscript");
			new MenuItem(submenu_guj, SWT.SEPARATOR);
			final MenuItem gujPhonetic = new MenuItem(submenu_guj, SWT.PUSH);
			gujPhonetic.setText("Phonetic");

			final MenuItem gurmukhi = new MenuItem(menu, SWT.CASCADE);
			gurmukhi.setText("Gurmukhi");
			final Menu submenu_gur = new Menu(shell, SWT.DROP_DOWN);
			gurmukhi.setMenu(submenu_gur);
			final MenuItem gurInscript = new MenuItem(submenu_gur, SWT.PUSH);
			gurInscript.setText("Inscript");

			final MenuItem hindi = new MenuItem(menu, SWT.CASCADE);
			hindi.setText("Hindi");
			final Menu submenu_hin = new Menu(shell, SWT.DROP_DOWN);
			hindi.setMenu(submenu_hin);
			final MenuItem hinLay = new MenuItem(submenu_hin, SWT.PUSH);
			hinLay.setText("Remington");
			final MenuItem hinInscript = new MenuItem(submenu_hin, SWT.PUSH);
			hinInscript.setText("Inscript");
			new MenuItem(submenu_hin, SWT.SEPARATOR);
			final MenuItem hinPhonetic = new MenuItem(submenu_hin, SWT.PUSH);
			hinPhonetic.setText("Phonetic");

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

			final MenuItem malayalam = new MenuItem(menu, SWT.CASCADE);
			malayalam.setText("Malayalam");
			final Menu submenu_mal = new Menu(shell, SWT.DROP_DOWN);
			malayalam.setMenu(submenu_mal);
			final MenuItem malInscript = new MenuItem(submenu_mal, SWT.PUSH);
			malInscript.setText("Inscript");

			final MenuItem marathi = new MenuItem(menu, SWT.CASCADE);
			marathi.setText("Marathi");
			final Menu submenu_mar = new Menu(shell, SWT.DROP_DOWN);
			marathi.setMenu(submenu_mar);
			final MenuItem marLay = new MenuItem(submenu_mar, SWT.PUSH);
			marLay.setText("Remington");
			final MenuItem marInscript = new MenuItem(submenu_mar, SWT.PUSH);
			marInscript.setText("Inscript");
			new MenuItem(submenu_mar, SWT.SEPARATOR);
			final MenuItem marPhonetic = new MenuItem(submenu_mar, SWT.PUSH);
			marPhonetic.setText("Phonetic");

			final MenuItem oriya = new MenuItem(menu, SWT.CASCADE);
			oriya.setText("Oriya");
			final Menu submenu_ori = new Menu(shell, SWT.DROP_DOWN);
			oriya.setMenu(submenu_ori);
			final MenuItem oriInscript = new MenuItem(submenu_ori, SWT.PUSH);
			oriInscript.setText("Inscript");

			final MenuItem tamil = new MenuItem(menu, SWT.CASCADE);
			tamil.setText("Tamil");
			final Menu submenu_tam = new Menu(shell, SWT.DROP_DOWN);
			tamil.setMenu(submenu_tam);
			final MenuItem tamil99 = new MenuItem(submenu_tam, SWT.PUSH);
			tamil99.setText("Tamil99");
			final MenuItem tamInscript = new MenuItem(submenu_tam, SWT.PUSH);
			tamInscript.setText("Inscript");
			final MenuItem tamRemington = new MenuItem(submenu_tam, SWT.PUSH);
			tamRemington.setText("Remington");
			new MenuItem(submenu_tam, SWT.SEPARATOR);
			final MenuItem tamPhonetic = new MenuItem(submenu_tam, SWT.PUSH);
			tamPhonetic.setText("Phonetic");

			final MenuItem telugu = new MenuItem(menu, SWT.CASCADE);
			telugu.setText("Telugu");
			final Menu submenu_tel = new Menu(shell, SWT.DROP_DOWN);
			telugu.setMenu(submenu_tel);
			final MenuItem telInscript = new MenuItem(submenu_tel, SWT.PUSH);
			telInscript.setText("Inscript");
			new MenuItem(submenu_tel, SWT.SEPARATOR);
			final MenuItem telPhonetic = new MenuItem(submenu_tel, SWT.PUSH);
			telPhonetic.setText("Phonetic");

			/*
			 * Adding user defined layouts to the menu. First check whether the
			 * userdefined directory exists then read the files in it one by
			 * one. Add them to the menu.
			 */
			new MenuItem(menu, SWT.SEPARATOR);
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

					// Listeners for the user defined keyboard layout menu items
					mi.addListener(SWT.Selection, new Listener() {

						public void handleEvent(Event event) {
							{
								layoutImg = "na";
								currentLayout.setEnabled(false);
								Image image1 = new Image(display,
										IndicKeyboards.workingDirectory
												+ "/resources/UD.ico");
								item.setImage(image1);
								tip.setMessage("Custom Created  - "
										+ filename.substring(0, filename
												.length() - 4));
								tip.setVisible(true);
								ParseXML.setlang("userdefined/" + filename);
								PhoneticParseXML.PhoneticFlag = 0;
								item.setToolTipText("indic-keyboards - User Defined - "
										+ filename.substring(0, filename
												.length() - 4));
							}
						}
					});
					// End of listener

				}

			}

			final MenuItem addLayouts = new MenuItem(menu, SWT.CASCADE);
			addLayouts.setText("Add New Layouts");
			final Menu submenu_newLay = new Menu(shell, SWT.DROP_DOWN);
			addLayouts.setMenu(submenu_newLay);

			final MenuItem XMLCreateIns = new MenuItem(submenu_newLay, SWT.PUSH);
			XMLCreateIns.setText("Add Inscript Layout");

			final MenuItem XMLCreateOth = new MenuItem(submenu_newLay, SWT.PUSH);
			XMLCreateOth.setText("Add Other Layout");

			new MenuItem(menu, SWT.SEPARATOR);

			enableDisable = new MenuItem(menu, SWT.PUSH);
			enableDisable.setText("Enable [Alt+F12]");

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

			final MenuItem help = new MenuItem(menu, SWT.PUSH);
			help.setText("Help");

			new MenuItem(menu, SWT.SEPARATOR);

			final MenuItem about = new MenuItem(menu, SWT.PUSH);
			about.setText("About indic-keyboards");

			new MenuItem(menu, SWT.SEPARATOR);

			final MenuItem exit = new MenuItem(menu, SWT.PUSH);
			exit.setText("Exit");

			exit.addListener(SWT.Selection, new Listener() {

				public void handleEvent(Event event) {
					String d = exit.getText();
					exit.setSelection(false);
					if (d.compareTo("Exit") == 0) {

						Shell sh = new Shell(display, SWT.APPLICATION_MODAL);
						sh.setImage(image);
						MessageBox messageBox = new MessageBox(sh, SWT.YES
								| SWT.NO | SWT.ICON_QUESTION);
						messageBox.setText("Exit...");
						if (System.getProperty("os.name").contains("Windows")) {
							messageBox
									.setMessage("Are you sure to quit indic-keyboards??");
						} else {
							String pid = ManagementFactory.getRuntimeMXBean()
									.getName();
							int index = pid.indexOf("@");
							messageBox
									.setMessage("Are you sure to quit indic-keyboards??"
											+ "\n\nIf the application doesn't close\nwhen Exit is selected, "
											+ "kill the process.\n\n(The process ID is "
											+ pid.substring(0, index) + ")");
						}
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
							if (System.getProperty("os.name").contains(
									"Windows")) {
								System.runFinalization();
								System.exit(0);
							} else {
								KeyMonitorMethods quit = new KeyMonitorMethods();
								KeyMonitorMethods.loggingEnabled = true;
								quit.printKeys(88); // Enable Auto Repeat.
								try {
									InitLinux.socket.close();
								} catch (IOException e) {
									e.printStackTrace();
								}
								System.runFinalization();
								System.exit(0);
							}
						} else {
							sh.dispose();
						}
					}
				}
			});

			help.addListener(SWT.Selection, new Listener() {

				public void handleEvent(Event event) {
					String d = help.getText();
					exit.setSelection(false);
					if (d.compareTo("Help") == 0) {

						Shell sh = new Shell(display, SWT.APPLICATION_MODAL);
						sh.setImage(image);
						MessageBox messageBox = new MessageBox(sh, SWT.YES
								| SWT.NO | SWT.ICON_QUESTION);
						messageBox.setText("Opening indic-keyboards' help...");

						messageBox
								.setMessage("Dou you want to open indic-keyboards help ??\n\n(Requires an Internet connection)");
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
						kgp.setSelection(false);
						{
							layoutImg = "kagapa";
							currentLayout.setEnabled(true);
							tip.setMessage("KaGaPa");
							tip.setVisible(true);
							// Set the selected language to be kan_kagapa.xml.
							ParseXML.setlang("kan_kagapa.xml");
							PhoneticParseXML.PhoneticFlag = 0;
							Image image1 = new Image(display,
									IndicKeyboards.workingDirectory
											+ "/resources/kannada.ico");
							item.setImage(image1);
							item.setToolTipText("indic-keyboards - KANNADA KaGaPa");
							previousKeyboardIcon=item.getImage();
						}
					}
				});

				kanInscript.addListener(SWT.Selection, new Listener() {
					public void handleEvent(Event event) {
						{
							currentLayout.setEnabled(true);
							tip.setMessage("Kannada Inscript");
							tip.setVisible(true);
							ParseXML.setlang("kan_inscript.xml");
							PhoneticParseXML.PhoneticFlag = 0;
							Image image1 = new Image(display,
									IndicKeyboards.workingDirectory
											+ "/resources/kannada.ico");
							item.setImage(image1);
							item.setToolTipText("indic-keyboards - KANNADA Inscript");
							previousKeyboardIcon=item.getImage();
						}
					}
				});

				kanPhonetic.addListener(SWT.Selection, new Listener() {
					public void handleEvent(Event event) {
						{
							currentLayout.setEnabled(false);
							tip.setMessage("Kannada Phonetic");
							tip.setVisible(true);
							PhoneticParseXML.setlang("kan_phonetic.xml");
							PhoneticParseXML.PhoneticFlag = 1;
							Image image1 = new Image(display,
									IndicKeyboards.workingDirectory
											+ "/resources/kannada.ico");
							item.setImage(image1);
							item.setToolTipText("indic-keyboards - KANNADA Phonetic");
							previousKeyboardIcon=item.getImage();
						}
					}
				});

			}// End of kannada section

			// All the tamil layouts go here
			{
				tamil99.addListener(SWT.Selection, new Listener() {
					public void handleEvent(Event event) {
						{
							layoutImg = "tamil99";
							currentLayout.setEnabled(true);
							tip.setMessage("Tamil99");
							tip.setVisible(true);
							ParseXML.setlang("tamil99.xml");
							PhoneticParseXML.PhoneticFlag = 0;
							Image image1 = new Image(display,
									IndicKeyboards.workingDirectory
											+ "/resources/tamil.ico");
							item.setImage(image1);
							item.setToolTipText("indic-keyboards - TAMIL Tamil99");
							previousKeyboardIcon=item.getImage();
						}

					}
				});

				tamInscript.addListener(SWT.Selection, new Listener() {

					public void handleEvent(Event event) {
						{
							currentLayout.setEnabled(true);
							tip.setMessage("Tamil Inscript");
							tip.setVisible(true);
							ParseXML.setlang("tam_inscript.xml");
							PhoneticParseXML.PhoneticFlag = 0;
							Image image1 = new Image(display,
									IndicKeyboards.workingDirectory
											+ "/resources/tamil.ico");
							item.setImage(image1);
							item.setToolTipText("indic-keyboards - TAMIL Inscript");
							previousKeyboardIcon=item.getImage();
						}

					}
				});

				tamRemington.addListener(SWT.Selection, new Listener() {

					public void handleEvent(Event event) {
						{
							currentLayout.setEnabled(true);
							tip.setMessage("Tamil Remington");
							tip.setVisible(true);
							ParseXML.setlang("tam_remington_inscript.xml");
							PhoneticParseXML.PhoneticFlag = 0;
							Image image1 = new Image(display,
									IndicKeyboards.workingDirectory
											+ "/resources/tamil.ico");
							item.setImage(image1);
							item.setToolTipText("indic-keyboards - TAMIL Remington");
							previousKeyboardIcon=item.getImage();
						}

					}
				});

				// Tamil Phonetic keyboard layout
				tamPhonetic.addListener(SWT.Selection, new Listener() {

					public void handleEvent(Event event) {
						{
							tip.setMessage("Tamil Phonetic");
							tip.setVisible(true);
							PhoneticParseXML.setlang("tam_phonetic.xml");
							PhoneticParseXML.PhoneticFlag = 1;
							Image image1 = new Image(display,
									IndicKeyboards.workingDirectory
											+ "/resources/tamil.ico");
							item.setImage(image1);
							item.setToolTipText("indic-keyboards - TAMIL Phonetic");
							previousKeyboardIcon=item.getImage();
						}
					}
				});

			}// End of tamil section

			// All telugu layouts go here
			{
				telInscript.addListener(SWT.Selection, new Listener() {

					public void handleEvent(Event event) {
						{
							layoutImg = "telugu_inscript";
							currentLayout.setEnabled(true);
							tip.setMessage("Telugu Inscript");
							tip.setVisible(true);
							ParseXML.setlang("tel_inscript.xml");
							PhoneticParseXML.PhoneticFlag = 0;
							Image image1 = new Image(display,
									IndicKeyboards.workingDirectory
											+ "/resources/telugu.ico");
							item.setImage(image1);
							item.setToolTipText("indic-keyboards - TELUGU Inscript");
							previousKeyboardIcon=item.getImage();
						}

					}
				});
				// Telugu phonetic keyboard listener
				telPhonetic.addListener(SWT.Selection, new Listener() {

					public void handleEvent(Event event) {
						{
							tip.setMessage("Telugu Phonetic");
							currentLayout.setEnabled(false);
							tip.setVisible(true);
							PhoneticParseXML.setlang("tel_phonetic.xml");
							PhoneticParseXML.PhoneticFlag = 1;
							Image image1 = new Image(display,
									IndicKeyboards.workingDirectory
											+ "/resources/telugu.ico");
							item.setImage(image1);
							item.setToolTipText("indic-keyboards - TELUGU Phonetic");
							previousKeyboardIcon=item.getImage();
						}
					}
				});

			}

			// All the gujarati layouts go here
			{

				gujInscript.addListener(SWT.Selection, new Listener() {
					public void handleEvent(Event event) {
						gujInscript.setSelection(false);
						{
							layoutImg = "gujarati_inscript";
							currentLayout.setEnabled(true);
							tip.setMessage("Gujrathi Inscript");
							tip.setVisible(true);
							ParseXML.setlang("guj_inscript.xml");
							PhoneticParseXML.PhoneticFlag = 0;
							Image image1 = new Image(display,
									IndicKeyboards.workingDirectory
											+ "/resources/gujarati.ico");
							item.setImage(image1);
							item.setToolTipText("indic-keyboards - GUJRATI Inscript");
							previousKeyboardIcon=item.getImage();

						}

					}
				});
				// Gujarati Phonetic keyboard listener
				gujPhonetic.addListener(SWT.Selection, new Listener() {

					public void handleEvent(Event event) {
						gujPhonetic.setSelection(false);
						{
							tip.setMessage("Gujarati Phonetic");
							currentLayout.setEnabled(false);
							tip.setVisible(true);
							PhoneticParseXML.setlang("guj_phonetic.xml");
							PhoneticParseXML.PhoneticFlag = 1;
							Image image1 = new Image(display,
									IndicKeyboards.workingDirectory
											+ "/resources/gujarati.ico");
							item.setImage(image1);
							item.setToolTipText("indic-keyboards - GUJARATI Phonetic");
							previousKeyboardIcon=item.getImage();
						}
					}
				});

			}// End of gujarati section

			// All the Hindi layouts go here
			{
				hinLay.addListener(SWT.Selection, new Listener() {

					public void handleEvent(Event event) {
						hinLay.setSelection(false);
						{
							layoutImg = "hindi_remington";
							currentLayout.setEnabled(true);
							tip.setMessage("Hindi Remington");
							tip.setVisible(true);
							ParseXML.setlang("hin_remington_inscript.xml");
							PhoneticParseXML.PhoneticFlag = 0;
							Image image1 = new Image(display,
									IndicKeyboards.workingDirectory
											+ "/resources/hindi.ico");
							item.setImage(image1);
							item.setToolTipText("indic-keyboards - HINDI Remington");
							previousKeyboardIcon=item.getImage();

						}

					}
				});

				hinInscript.addListener(SWT.Selection, new Listener() {

					public void handleEvent(Event event) {
						hinInscript.setSelection(false);
						{
							layoutImg = "hindi_inscript";
							currentLayout.setEnabled(true);
							tip.setMessage("Hindi Inscript");
							tip.setVisible(true);

							ParseXML.setlang("hin_inscript.xml");
							PhoneticParseXML.PhoneticFlag = 0;
							Image image1 = new Image(display,
									IndicKeyboards.workingDirectory
											+ "/resources/hindi.ico");
							item.setImage(image1);
							item.setToolTipText("indic-keyboards - HINDI Inscript");
							previousKeyboardIcon=item.getImage();
						}

					}
				});
				// Hindi Phonetic keyboard listener
				hinPhonetic.addListener(SWT.Selection, new Listener() {

					public void handleEvent(Event event) {
						hinPhonetic.setSelection(false);
						{
							tip.setMessage("Hindi Phonetic");
							currentLayout.setEnabled(false);
							tip.setVisible(true);
							PhoneticParseXML.setlang("hin_phonetic.xml");
							PhoneticParseXML.PhoneticFlag = 1;
							Image image1 = new Image(display,
									IndicKeyboards.workingDirectory
											+ "/resources/hindi.ico");
							item.setImage(image1);
							item.setToolTipText("indic-keyboards - HINDI Phonetic");
							previousKeyboardIcon=item.getImage();
						}
					}
				});

			}// End of hindi section

			// All the marathi layouts go here
			{
				marLay.addListener(SWT.Selection, new Listener() {

					public void handleEvent(Event event) {
						marLay.setSelection(false);
						{
							layoutImg = "hindi_remington";
							currentLayout.setEnabled(true);
							tip.setMessage("Marathi Remington");
							tip.setVisible(true);
							ParseXML.setlang("hin_remington_inscript.xml");
							PhoneticParseXML.PhoneticFlag = 0;
							Image image1 = new Image(display,
									IndicKeyboards.workingDirectory
											+ "/resources/hindi.ico");
							item.setImage(image1);
							item.setToolTipText("indic-keyboards - MARATHI");
							previousKeyboardIcon=item.getImage();
						}

					}
				});

				marInscript.addListener(SWT.Selection, new Listener() {

					public void handleEvent(Event event) {
						marInscript.setSelection(false);
						{
							layoutImg = "hindi_inscript";
							currentLayout.setEnabled(true);
							tip.setMessage("Marathi Inscript");
							tip.setVisible(true);
							ParseXML.setlang("mar_inscript.xml");
							PhoneticParseXML.PhoneticFlag = 0;
							Image image1 = new Image(display,
									IndicKeyboards.workingDirectory
											+ "/resources/hindi.ico");
							item.setImage(image1);
							item.setToolTipText("indic-keyboards - MARATHI Inscript");
							previousKeyboardIcon=item.getImage();
						}

					}
				});
				// Marathi phonetic keyboard listener
				marPhonetic.addListener(SWT.Selection, new Listener() {

					public void handleEvent(Event event) {
						marPhonetic.setSelection(false);
						{
							tip.setMessage("Marathi Phonetic");
							currentLayout.setEnabled(false);
							tip.setVisible(true);
							PhoneticParseXML.setlang("hin_phonetic.xml");
							PhoneticParseXML.PhoneticFlag = 1;
							Image image1 = new Image(display,
									IndicKeyboards.workingDirectory
											+ "/resources/hindi.ico");
							item.setImage(image1);
							item.setToolTipText("indic-keyboards - MARATHI Phonetic");
							previousKeyboardIcon=item.getImage();
						}
					}
				});

			}// End of marathi section

			// Bengali layout listener goes here
			benInscript.addListener(SWT.Selection, new Listener() {

				public void handleEvent(Event event) {
					benInscript.setSelection(false);
					{
						layoutImg = "bengali_inscript";
						currentLayout.setEnabled(true);
						tip.setMessage("Bengali Inscript");
						tip.setVisible(true);
						ParseXML.setlang("ben_inscript.xml");
						PhoneticParseXML.PhoneticFlag = 0;
						Image image1 = new Image(display,
								IndicKeyboards.workingDirectory
										+ "/resources/bengali.ico");
						item.setImage(image1);
						item.setToolTipText("indic-keyboards - BENGALI Inscript");
						previousKeyboardIcon=item.getImage();
					}

				}
			});

			// Punjabi layout listeners goes here
			gurInscript.addListener(SWT.Selection, new Listener() {

				public void handleEvent(Event event) {
					gurInscript.setSelection(false);
					{
						layoutImg = "gurmukhi_inscript";
						currentLayout.setEnabled(true);
						tip.setMessage("Gurmukhi Inscript");
						tip.setVisible(true);
						ParseXML.setlang("gur_inscript.xml");
						PhoneticParseXML.PhoneticFlag = 0;
						Image image1 = new Image(display,
								IndicKeyboards.workingDirectory
										+ "/resources/gurmukhi.ico");
						item.setImage(image1);
						item.setToolTipText("indic-keyboards - GURMUKHI Inscript");
						previousKeyboardIcon=item.getImage();
					}

				}
			});

			// Malayalam layout listener goes here
			malInscript.addListener(SWT.Selection, new Listener() {

				public void handleEvent(Event event) {
					malInscript.setSelection(false);
					{
						layoutImg = "malayalam_inscript";
						currentLayout.setEnabled(true);
						tip.setMessage("Malayalam Inscript");
						tip.setVisible(true);
						ParseXML.setlang("mal_inscript.xml");
						PhoneticParseXML.PhoneticFlag = 0;
						Image image1 = new Image(display,
								IndicKeyboards.workingDirectory
										+ "/resources/malayalam.ico");
						item.setImage(image1);
						item.setToolTipText("indic-keyboards - MALAYALAM Inscript");
						previousKeyboardIcon=item.getImage();
					}

				}
			});

			// Oriya layout listeners goes here
			oriInscript.addListener(SWT.Selection, new Listener() {

				public void handleEvent(Event event) {
					oriInscript.setSelection(false);
					{
						layoutImg = "oriya_inscript";
						currentLayout.setEnabled(true);
						tip.setMessage("Oriya Inscript");
						tip.setVisible(true);
						ParseXML.setlang("ori_inscript.xml");
						PhoneticParseXML.PhoneticFlag = 0;
						Image image1 = new Image(display,
								IndicKeyboards.workingDirectory
										+ "/resources/oriya.ico");
						item.setImage(image1);
						item.setToolTipText("indic-keyboards - ORIYA Inscript");
						previousKeyboardIcon=item.getImage();
					}

				}
			});

			// Listener for displaying the current layout image.
			currentLayout.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event event) {
					try {
						final Shell currentLayout = new Shell(display,
								SWT.DIALOG_TRIM);

						Image layImage = new Image(display,
								IndicKeyboards.workingDirectory
										+ "/resources/trayicon.ico");
						currentLayout.setImage(layImage);

						if (layoutImg.compareTo("kagapa") == 0) {
							showCurrentLayout(display, currentLayout, "KaGaPa",
									IndicKeyboards.workingDirectory
											+ "/resources/kagapa.png", 1006,
									290);
						}
						if (layoutImg.compareTo("kannada_inscript") == 0) {
							showCurrentLayout(
									display,
									currentLayout,
									"Kannada Inscript",
									IndicKeyboards.workingDirectory
											+ "/resources/kannada_inscript.png",
									1006, 300);
						}
						if (layoutImg.compareTo("tamil99") == 0) {
							showCurrentLayout(display, currentLayout,
									"Tamil 99", IndicKeyboards.workingDirectory
											+ "/resources/tamil99.png", 1006,
									275);
						}
						if (layoutImg.compareTo("tamil_inscript") == 0) {
							showCurrentLayout(display, currentLayout,
									"Tamil Inscript",
									IndicKeyboards.workingDirectory
											+ "/resources/tamil_inscript.png",
									1006, 275);

						}
						if (layoutImg.compareTo("tamil_remington") == 0) {
							showCurrentLayout(display, currentLayout,
									"Tamil Remington",
									IndicKeyboards.workingDirectory
											+ "/resources/tamil_remington.gif",
									800, 250);
						}
						if (layoutImg.compareTo("hindi_inscript") == 0) {
							showCurrentLayout(display, currentLayout,
									"Hindi Inscript",
									IndicKeyboards.workingDirectory
											+ "/resources/hindi_inscript.png",
									1006, 275);
						}
						if (layoutImg.compareTo("hindi_remington") == 0) {
							showCurrentLayout(display, currentLayout,
									"Hindi Remington",
									IndicKeyboards.workingDirectory
											+ "/resources/hindi_remington.png",
									1006, 300);
						}
						if (layoutImg.compareTo("gujarati_inscript") == 0) {
							showCurrentLayout(
									display,
									currentLayout,
									"Gujarati Inscript",
									IndicKeyboards.workingDirectory
											+ "/resources/gujarati_inscript.png",
									1006, 280);
						}
						if (layoutImg.compareTo("telugu_inscript") == 0) {
							showCurrentLayout(display, currentLayout,
									"Telugu Inscript",
									IndicKeyboards.workingDirectory
											+ "/resources/telugu_inscript.png",
									1006, 255);
						}
						if (layoutImg.compareTo("bengali_inscript") == 0) {
							showCurrentLayout(
									display,
									currentLayout,
									"Bengali Inscript",
									IndicKeyboards.workingDirectory
											+ "/resources/bengali_inscript.png",
									1006, 280);
						}
						if (layoutImg.compareTo("gurmukhi_inscript") == 0) {
							showCurrentLayout(
									display,
									currentLayout,
									"Gurmukhi Inscript",
									IndicKeyboards.workingDirectory
											+ "/resources/gurmukhi_inscript.png",
									1006, 280);
						}
						if (layoutImg.compareTo("malayalam_inscript") == 0) {
							showCurrentLayout(
									display,
									currentLayout,
									"Malayalam Inscript",
									IndicKeyboards.workingDirectory
											+ "/resources/malayalam_inscript.png",
									1006, 280);
						}
						if (layoutImg.compareTo("oriya_inscript") == 0) {
							showCurrentLayout(display, currentLayout,
									"Oriya Inscript",
									IndicKeyboards.workingDirectory
											+ "/resources/oriya_inscript.png",
									1006, 300);
						}
						if (layoutImg.compareTo("na") == 0) {
							currentLayout.setEnabled(false);
						}
					} catch (NullPointerException e) {

					} catch (org.eclipse.swt.SWTException e) {

					}

				}
			});

			// Enable and disable listener - should be differnet for Linux and
			// windows
			if (System.getProperty("os.name").contains("Windows")) {
				enableDisable.addListener(SWT.Selection, new Listener() {
					public void handleEvent(Event event) {
						InitWin.enable = !InitWin.enable;
						if (InitWin.enable) {

							item.setImage(previousKeyboardIcon);
							tip.setMessage("Enabled");
							tip.setVisible(true);
							enableDisable.setText("Disable [Alt+F12]");
							System.gc();
						} else {
							previousKeyboardIcon = item.getImage();
							Image image1 = new Image(
									display,
									IndicKeyboards.workingDirectory
											+ "/resources/trayicon_disabled.ico");
							item.setImage(image1);
							tip.setMessage("Disabled");
							tip.setVisible(true);
							enableDisable.setText("Enable [Alt+F12]");
							System.gc();
						}
					}
				});
			} else {
				enableDisable.addListener(SWT.Selection, new Listener() {
					public void handleEvent(Event event) {
						if (KeyMonitorMethods.loggingEnabled == false) {
							KeyMonitorMethods enable = new KeyMonitorMethods();
							enable.printKeys(666);
							item.setImage(previousKeyboardIcon);
							tip.setMessage("Enabled");
							tip.setVisible(true);
							enableDisable.setText("Disable [Alt+F12]");
							System.gc();
						} else if (KeyMonitorMethods.loggingEnabled == true) {
							KeyMonitorMethods disable = new KeyMonitorMethods();
							disable.printKeys(666);
							previousKeyboardIcon = item.getImage();
							Image image1 = new Image(
									display,
									IndicKeyboards.workingDirectory
											+ "/resources/trayicon_disabled.ico");
							item.setImage(image1);
							tip.setMessage("Disabled");
							tip.setVisible(true);
							enableDisable.setText("Enable [Alt+F12]");
							System.gc();
						}
					}
				});
			}

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

			about.addListener(SWT.Selection, new Listener() {

				public void handleEvent(Event event) {

					AboutUs a = new AboutUs();
					a.showAboutUs();
				}
			});

			item.addListener(SWT.MenuDetect, new Listener() {

				public void handleEvent(Event event) {
					menu.setVisible(true);
				}
			});

			item.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event event) {
					tip.setText("indic-keyboards");
					tip.setMessage("Please right click");
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

	public static void showCurrentLayout(Display display, Shell currentLayout,
			String text, String imagePath, int height, int width) {
		final Image image = new Image(display, imagePath);
		currentLayout.setText(text);
		currentLayout.setSize(height, width);
		currentLayout.setBackgroundImage(image);
		currentLayout.open();
		currentLayout.setActive();
		currentLayout.setFocus();
	}
}
