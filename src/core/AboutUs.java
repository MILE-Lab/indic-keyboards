/** ********************************************************************
 * File:           AboutUs.java 
 * Description:    File which creates the "About indic-keyboards" shell.
 * Authors:        Abhinava,Akshay,Revati,Arun 
 * Created:        Sun May 25 02:01:25 IST 2009
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
package core;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;



/**
 * Creates a tabbed display with four tabs, and a few controls on each page
 */
public class AboutUs {

	public static Display display = Display.getCurrent();
	private TabFolder tabFolder;
	private TabItem about;
	private TabItem authors;
	private TabItem thanks;
	private TabItem license;
	private Link link1;
	private Link link2;
	private Link link3;
	private Link link4;

	/**
	 * Runs the application
	 */
	public void run() {

		Shell shell = new Shell(display);
		shell.setLayout(new FormLayout());
		shell.setSize(410, 500);
		int x = (display.getClientArea().width) / 2;
		int y = (display.getClientArea().height) / 2;
		shell.setLocation(x - 150, y - 220);
		shell.setText("About - indic-keyboads");
		Image image = new Image(display, IndicKeyboards.class
				.getResourceAsStream("trayicon.ico"));
		shell.setImage(image);
		createContents(shell);
		shell.open();

		link1.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				System.out.println("Selection: " + event.text);
				Program pBrowse = Program.findProgram(".html");
				pBrowse.execute(event.text);
			}
		});
		link2.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				System.out.println("Selection: " + event.text);
				if (event.text.contains("mailto")) {
					Program pBrowse = Program.findProgram(".html");
					pBrowse.execute(event.text);
				}

			}
		});
		link3.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				System.out.println("Selection: " + event.text);
				if (event.text.contains("http")) {
					Program pBrowse = Program.findProgram(".html");
					pBrowse.execute(event.text);
				}
				if (event.text.contains("mailto")) {
					Program pBrowse = Program.findProgram(".html");
					pBrowse.execute(event.text);
				}
			}
		});
		link4.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				System.out.println("Selection: " + event.text);
				Program pBrowse = Program.findProgram(".html");
				pBrowse.execute(event.text);
			}
		});
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		shell.dispose();
		image.dispose();
	}

	/**
	 * Creates the contents
	 * 
	 * @param shell
	 *            the parent shell
	 */
	private void createContents(Shell shell) {
		// Create the containing tab folder
		tabFolder = new TabFolder(shell, SWT.NONE);
		FormData tabFolderLData = new FormData();
		tabFolderLData.width = 377;
		tabFolderLData.height = 257;
		tabFolderLData.left = new FormAttachment(0, 1000, 12);
		tabFolderLData.top = new FormAttachment(0, 1000, 100);
		tabFolderLData.bottom = new FormAttachment(1000, 1000, -12);
		tabFolderLData.right = new FormAttachment(1000, 1000, -12);
		tabFolder.setLayoutData(tabFolderLData);

		Canvas canvas1;
		FormData canvas1LData = new FormData();
		canvas1LData.width = 381;
		canvas1LData.height = 64;
		canvas1LData.left = new FormAttachment(0, 1000, 12);
		canvas1LData.top = new FormAttachment(0, 1000, 12);
		canvas1 = new Canvas(shell, SWT.NONE);
		RowLayout canvas1Layout = new RowLayout(org.eclipse.swt.SWT.HORIZONTAL);
		canvas1.setLayout(canvas1Layout);
		canvas1.setLayoutData(canvas1LData);
		Image image1 = new Image(display, IndicKeyboards.class
				.getResourceAsStream("about.png"));
		canvas1.setBackgroundImage(image1);

		// Create each tab and set its text, tool tip text,
		// image, and control

		Image star = new Image(display, IndicKeyboards.class
				.getResourceAsStream("star.png"));

		about = new TabItem(tabFolder, SWT.NONE);
		about.setText("About");
		about.setToolTipText("About");
		about.setImage(star);
		about.setControl(getTabAboutControl(tabFolder));

		authors = new TabItem(tabFolder, SWT.NONE);
		authors.setText("Authors");
		authors.setToolTipText("Authors");
		authors.setImage(star);
		authors.setControl(getTabAuthorsControl(tabFolder));

		thanks = new TabItem(tabFolder, SWT.NONE);
		thanks.setText("Thanks To");
		thanks.setToolTipText("Thanks To");
		thanks.setImage(star);
		thanks.setControl(getTabThanksControl(tabFolder));

		license = new TabItem(tabFolder, SWT.NONE);
		license.setText("License");
		license.setToolTipText("License");
		license.setImage(star);
		license.setControl(getTabLicenseControl(tabFolder));

		// Select the third tab (index is zero-based)
		tabFolder.setSelection(0);

		/*
		 * // Add an event listener to write the selected tab to stdout
		 * tabFolder.addSelectionListener(new SelectionAdapter() { public void
		 * widgetSelected( org.eclipse.swt.events.SelectionEvent event) {
		 * System.out.println(tabFolder.getSelection()[0].getText() +
		 * " selected"); } });
		 */
	}

	private Control getTabAboutControl(TabFolder tabFolder) {
		// Create a multiline text field
		// Create a multiline text field
		/*
		 * Text textAbout = new Text(tabFolder, SWT.MULTI | SWT.READ_ONLY |
		 * SWT.WRAP | SWT.BORDER);
		 * textAbout.setText("\n\nAn Input Method Editor similar to " +
		 * "BarahaIME.\n\n" + "\u00A9MILE Lab IISc, Bangalore. 2009.\n\n" +
		 * "http://code.google.com/p/indic-keyboards\n\n" +
		 * "Users Mailing List :\nhttp://groups.google.com/group/cki-users/\n\n"
		 * +
		 * "Developers Mailing List :\nhttp://groups.google.com/group/cki-dev/"
		 * ); return textAbout;
		 */
		link1 = new Link(tabFolder, SWT.NONE);

		String text = "\n\nA Common Keyboard Interface for Indic Scripts - like BarahaIME\n\n"
				+ "\u00A9MILE Lab IISc, Bangalore. 2009.\n\n"
				+ "<a href=\"http://code.google.com/p/indic-keyboards\">http://code.google.com/p/indic-keyboards</a>\n\n"
				+ "User Mailing List :\n<a href=\"http://groups.google.com/group/cki-users/\">http://groups.google.com/group/cki-users/</a>\n\n"
				+ "Developer Mailing List :\n<a href=\"http://groups.google.com/group/cki-dev/\">http://groups.google.com/group/cki-dev/</a>\n\n"
				+ "Source Code Management :\n<a href=\"http://groups.google.com/group/indic-keyboards-scm/\">http://groups.google.com/group/indic-keyboards-scm/</a>";

		link1.setText(text);
		return link1;

	}

	private Control getTabAuthorsControl(TabFolder tabFolder) {
		// Create a multiline text field
		// Create a multiline text field
		/*
		 * Text textAuthor = new Text(tabFolder, SWT.MULTI | SWT.READ_ONLY |
		 * SWT.WRAP | SWT.BORDER);
		 * textAuthor.setText("\n\nAbhinava Shivakumar S.\n" +
		 * "abhinav.zoso@gmail.com\n\n" + "Akshay Rao\nu.akshay@gmail.com\n\n" +
		 * "Arun S.\nsarun87@gmail.com\n\n" +
		 * "Revati P. Junnarkar\nrevengr@gmail.com"); return textAuthor;
		 */
		link2 = new Link(tabFolder, SWT.NONE);
		String text = "\n\nAbhinava Shivakumar S.\n"
				+ "<a href=\"mailto:abhinav.zoso@gmail.com\">abhinav.zoso@gmail.com</a>\n\n"
				+ "Akshay Rao\n<a href=\"mailto:u.akshay@gmail.com\">u.akshay@gmail.com</a>\n\n"
				+ "Arun S.\n<a href=\"mailto:sarun87@gmail.com\">sarun87@gmail.com</a>\n\n"
				+ "Revati P. Junnarkar\n<a href=\"mailto:revengr@gmail.com\">revengr@gmail.com</a>";
		link2.setText(text);
		return link2;
	}

	private Control getTabThanksControl(TabFolder tabFolder) {
		// Create a multiline text field
		/*
		 * Text textThanks = new Text(tabFolder, SWT.MULTI | SWT.READ_ONLY |
		 * SWT.WRAP| SWT.BORDER); textThanks.setText("\n" +
		 * "Dr. A. G. Ramakrishnan\nAssociate Professor\n" +
		 * "Department of Electrical Engineering\n" +
		 * "Indian Institute of Science\n" + "Bangalore 560 012, INDIA\n" +
		 * "91 (0) 80 2293 2556\n" + "ramkiag@ee.iisc.ernet.in\n" +
		 * "http://ragashri.ee.iisc.ernet.in/MILE/index.htm\n\n" +
		 * "Shiva Kumar H. R.\n" + "shivahr@gmail.com\n" +
		 * "shivahr@ee.iisc.ernet.in\n" + "http://languagetechnology.org");
		 * return textThanks;
		 */
		link3 = new Link(tabFolder, SWT.NONE);
		String text = "\n"
				+ "Dr. A. G. Ramakrishnan\nAssociate Professor\n"
				+ "Department of Electrical Engineering\n"
				+ "Indian Institute of Science\n"
				+ "Bangalore 560 012, INDIA\n"
				+ "91 (0) 80 2293 2556\n"
				+ "<a href=\"mailto:ramakiag@ee.iisc.ernet.in\">ramkiag@ee.iisc.ernet.in</a>\n"
				+ "<a href=\"http://ragashri.ee.iisc.ernet.in/MILE/index.htm\">http://ragashri.ee.iisc.ernet.in/MILE/index.htm</a>\n\n"
				+ "Shiva Kumar H. R.\n"
				+ "<a href=\"mailto:shivahr@gmail.com\">shivahr@gmail.com</a>\n"
				+ "<a href=\"mailto:shivahr@ee.iisc.ernet.in\">shivahr@ee.iisc.ernet.in</a>\n"
				+ "<a href=\"http://languagetechnology.org\">http://languagetechnology.org</a>";
		link3.setText(text);
		return link3;
	}

  private Control getTabLicenseControl(TabFolder tabFolder) {
		// Create a multiline text field
		// Create a multiline text field
		/*
		 * Text textAuthor = new Text(tabFolder, SWT.MULTI | SWT.READ_ONLY |
		 * SWT.WRAP | SWT.BORDER);
		 * textAuthor.setText("\n\nAbhinava Shivakumar S.\n" +
		 * "abhinav.zoso@gmail.com\n\n" + "Akshay Rao\nu.akshay@gmail.com\n\n" +
		 * "Arun S.\nsarun87@gmail.com\n\n" +
		 * "Revati P. Junnarkar\nrevengr@gmail.com"); return textAuthor;
		 */
		link4 = new Link(tabFolder, SWT.NONE);
		String text = "\n\n\nTHIS SOFTWARE IS LICENSED UNDER THE APACHE 2.0 LICENSE. A COPY OF THE LICENSE IS GVEN IN THE PROGRAM FOLDER. IF YOU DOWNLOADED THIS SOFTWARE AND THE LICENSE WAS NOT INCLUDED IN THE FOLDER, PLEASE CONTACT THE AUTHORS. IN SUCH A CASE WHEREIN THE LICENSE IS NOT PRESENT IN THE PROGRAM FOLDER REFER THE FOLLOWING LINK TO GET A COPY OF THE LICENSE <a href=\"http://www.apache.org/licenses/LICENSE-2.0\">HERE</a>. THIS SOFTWARE IS GIVEN AS IS WITH NO WARRANTY WHATSOEVER. THE AUTHORS ARE NOT LIABLE IN ANY FORM FOR THE DAMAGES THAT MAY OCCUR DIRECTLY OR INDIRECTLY ON USE OF THIS SOFTWARE.";
		link4.setText(text);
		return link4;
	}

}
