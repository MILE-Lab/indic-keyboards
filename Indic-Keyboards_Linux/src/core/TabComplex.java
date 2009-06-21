package core;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

/**
 * Creates a tabbed display with four tabs, and a few controls on each page
 */
public class TabComplex {
	
	public static Display display = Display.getCurrent();
	

	/**
	 * Runs the application
	 */
	public static void run() {
		
		Shell shell = new Shell(display);
		shell.setLayout(new FormLayout());
		shell.setSize(410, 500);
		shell.setLocation(500, 200);
		shell.setText("About - indic-keyboads");
		Image image = new Image(display, IndicKeyboards.class
				.getResourceAsStream("trayicon.ico"));
		shell.setImage(image);
		createContents(shell);
		shell.open();
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
	private static void createContents(Shell shell) {
		// Create the containing tab folder
		final TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
		FormData tabFolderLData = new FormData();
		tabFolderLData.width = 377;
		tabFolderLData.height = 257;
		tabFolderLData.left = new FormAttachment(0, 1000, 12);
		tabFolderLData.top = new FormAttachment(0, 1000, 100);
		tabFolderLData.bottom = new FormAttachment(1000, 1000, -12);
		tabFolderLData.right = new FormAttachment(1000, 1000, -12);
		tabFolder.setLayoutData(tabFolderLData);

		// Create each tab and set its text, tool tip text,
		// image, and control
		Image star = new Image(display, IndicKeyboards.class
				.getResourceAsStream("star.png"));
		
		TabItem about = new TabItem(tabFolder, SWT.NONE);
		about.setText("About");
		about.setToolTipText("About");
		about.setImage(star);
		// one.setControl(getTabOneControl(tabFolder));
		TabItem authors = new TabItem(tabFolder, SWT.NONE);
		authors.setText("Authors");
		authors.setToolTipText("Authors");
		authors.setImage(star);
		//authors.setControl(getTabTwoControl(tabFolder));

		TabItem thanks = new TabItem(tabFolder, SWT.NONE);
		thanks.setText("Thanks To");
		thanks.setToolTipText("Thanks To");
		thanks.setImage(star);
		// thanks.setControl(getTabThreeControl(tabFolder));

		TabItem license = new TabItem(tabFolder, SWT.NONE);
		license.setText("License");
		license.setToolTipText("License");
		license.setImage(star);

		// Select the third tab (index is zero-based)
		tabFolder.setSelection(2);

		// Add an event listener to write the selected tab to stdout
		tabFolder.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(
					org.eclipse.swt.events.SelectionEvent event) {
				System.out.println(tabFolder.getSelection()[0].getText()
						+ " selected");
			}
		});
	}

	


	private Control getTabTwoControl(TabFolder tabFolder) {
		// Create a multiline text field
		final Text text = new Text(tabFolder, SWT.BORDER | SWT.MULTI
				| SWT.V_SCROLL | SWT.WRAP | SWT.LEFT);
		FormData data = new FormData();
		data.width = 30;
		data.height = 20;
		data.left = new FormAttachment(0, 1000, 12);
		data.top = new FormAttachment(0, 1000, 100);
		data.bottom = new FormAttachment(1000, 1000, -12);
		data.right = new FormAttachment(1000, 1000, -12);
		text.setLayoutData(data);
		return text;
		// return new Text(tabFolder, SWT.BORDER | SWT.MULTI | SWT.WRAP);
	}

	
	
}
