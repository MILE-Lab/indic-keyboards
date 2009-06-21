/** ********************************************************************
 * File:           IndicKeyboards.java
 * Description:    Entry point to the application.
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.BindException;
import java.net.ServerSocket;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class IndicKeyboards {
	public static ServerSocket socket;
	public static int portNumber;
	public static boolean AutoRepeat;

	public static void main(String[] args) throws IOException {

		/*
		 * Creating an instance of the SplashScreenMain class for the Splash
		 * screen.The constructor takes in the number of milliseconds splash
		 * screen will be active.
		 */
		/*
		 * final SplashScreenMain splash = new SplashScreenMain(3000);
		 * splash.SplashScreenShow();
		 * 
		 * The method has been modified to deal with the milliseconds in the
		 * file SplashScreen.java itself
		 */

		/*
		 * The file preferences.conf contains the port number to be used andto
		 * specify if the keyboard autorepeat should be turned on or off.By
		 * default, the port number would be 65530 and auto repeat will be ON.
		 */
		try {
			String preferencesPath = System.getProperty("user.dir");
			File preferences = new File(preferencesPath + "/preferences.conf");
			FileReader prefRead = new FileReader(preferences);
			BufferedReader prefer = new BufferedReader(prefRead);
			StringTokenizer token;

			String str = prefer.readLine();
			token = new StringTokenizer(str, ":");
			try {
				String key = token.nextToken();
				String port = token.nextToken().trim();
				portNumber = Integer.parseInt(port);
			} catch (NoSuchElementException e) {
				portNumber = 65530;
			}
			str = prefer.readLine();
			token = new StringTokenizer(str, ":");
			try {
				String key = token.nextToken();
				String autoRep = token.nextToken().trim();
				if (Integer.parseInt(autoRep) == 1) {
					AutoRepeat = true;
				} else if (Integer.parseInt(autoRep) == 0) {
					AutoRepeat = false;
				} else {
					AutoRepeat = true;
				}
			} catch (NoSuchElementException e) {
				AutoRepeat = true;
			}

		} catch (FileNotFoundException f) {
			portNumber = 65530;
			AutoRepeat = true;
		}

		try {
			socket = new ServerSocket(portNumber);
		} catch (BindException e) {
			Display display = Display.getCurrent();
			Shell shell = new Shell(display);
			MessageBox messageBox = new MessageBox(shell, SWT.OK
					| SWT.ICON_ERROR);
			messageBox.setText("Duplicate Instance");
			messageBox
					.setMessage("There is already an instance of indic-keyboards running. Close it first. If "
							+ "another instance isn't running, close any connections on port "
							+ portNumber
							+ " and try again."
							+ " Alternatively, you can view the preferences.conf file to check the"
							+ " port number.");
			messageBox.open();
			shell.dispose();
			// display.dispose();
			System.exit(0);

		}
		String pid = ManagementFactory.getRuntimeMXBean().getName();
		int index = pid.indexOf("@");
		System.out.println("This is the process ID : "
				+ pid.substring(0, index));
		System.out.println("indic-keyboards listening on port "
				+ socket.getLocalPort());
		if (AutoRepeat == false) {
			System.out.println("Auto Repeat is turned OFF");
		}
		if (AutoRepeat == true) {
			System.out.println("Auto Repeat is turned ON");
		}

		System.out.println(Thread.currentThread().getName());
		Runnable r = new KeyMonitor();
		Thread t = new Thread(r);
		t.setName("Key Monitor");
		t.start();
		SplashScreen s = new SplashScreen();
		s.screen();
		UI iFace = new UI();

	}// end of main
}// end of class CKI
