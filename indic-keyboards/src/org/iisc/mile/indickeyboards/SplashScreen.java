/** ********************************************************************
 * File:           SplashScreen.java 
 * Description:    Creates the Splash Screen for the Common Keyboard Interface
 * Authors:        Akshay,Abhinava,Revati,Arun 
 * Created:        Wed Oct 29 23:31:25 GMT 2008
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

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Display; //import org.eclipse.swt.widgets.Layout not reqd;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

public class SplashScreen {

	void screen() {
		Display d = new Display();
		Shell sh = new Shell(d, SWT.NO_TRIM);

		sh.setLayout(new GridLayout(2, true));
		sh.setText("Opening indic-keyboards...");
		sh.setLayout(new RowLayout(SWT.HORIZONTAL));
		sh.setBackground(d.getSystemColor(SWT.COLOR_DARK_GRAY));

		/*
		 * @debug String temp = InitWin.workingDirectory +
		 * "\\resources\\splash1.jpg"; System.out.println(temp);
		 */

		Image im = new Image(d, IndicKeyboards.workingDirectory
				+ "/resources/splashscreen.jpg");

		int width = 455;
		int height = 115;
		int x = (d.getClientArea().width - width) / 2;
		int y = (d.getClientArea().height - height) / 2;

		sh.setBounds(x, y - 25, width, height + 135);
		sh.setBackgroundImage(im);

		sh.open();

		ProgressBar pg = new ProgressBar(sh, SWT.SMOOTH);
		pg.setMinimum(0);
		pg.setMaximum(45);
		pg.setBounds(0, sh.getClientArea().height - 20,
				sh.getClientArea().width, 20);

		for (int i = 0; i <= 45; i++) {
			try {

				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			pg.setSelection(i);
		}
		// Cleanup
		pg.dispose();
		sh.dispose();
		d.dispose();
	}
}
