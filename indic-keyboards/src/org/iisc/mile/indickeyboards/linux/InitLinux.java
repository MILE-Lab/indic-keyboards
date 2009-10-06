/** ********************************************************************
 * File:           InitLinux.java 
 * Description:    Initialize indic-keyboards on Linux.
 * Authors:        Abhinava,Akshay. Revati,Arun 
 * Created:        Sun July 2 02:01:25 IST 2009
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

public class InitLinux {

	/**
	 * This variable holds the socket socket object on which indic-keybaords
	 * will listen.
	 */
	public static ServerSocket socket;
	/**
	 * Contains the port number on which indic-keyboards will listen. The port
	 * number will be taken from the file <strong>preferences.conf</strong>. The
	 * default port number is 65530. <strong>The socket is used to prevent
	 * multiple instances of indic-keyboards from running.</strong>
	 */
	public static int portNumber;
	/**
	 * This boolean variable gets it's value from the file
	 * <strong>preferences.conf</strong>. It regulates the keyboard
	 * <em>AutoRepeat</em> property. Default value is <em>ON</em>. Xlib used to
	 * control this feature.
	 * 
	 * @see core.KeyMonitorMethods#keyrepeat(int) Key repeat control
	 */
	public static boolean AutoRepeat;

	public void InitLinuxStart() throws IOException {

		/*
		 * The file preferences.conf contains the port number to be used andto
		 * specify if the keyboard autorepeat should be turned on or off.By
		 * default, the port number would be 65530 and auto repeat will be ON.
		 */
		try {
			/**
			 * Path to the <strong>preferences.conf</strong> file.
			 */
			String preferencesPath = System.getProperty("user.dir");

			/**
			 * Creates a <em>File</em> object to the
			 * <strong>preferences.conf</strong> file.
			 */
			File preferences = new File(preferencesPath + "/preferences.conf");

			/**
			 * <em>FileReader</em> object to read from the
			 * <strong>preferences.conf</strong> file.
			 */
			FileReader prefRead = new FileReader(preferences);

			/**
			 * <em>BufferedReader</em> object to read lines from the
			 * <strong>preferences.conf</strong> file.
			 */
			BufferedReader prefer = new BufferedReader(prefRead);

			/**
			 * <em>StringTokenizer</em> object which reads contents from the
			 * <strong>preferences.conf</strong> file as tokens.
			 */
			StringTokenizer token;

			/**
			 * Contains lines reads from the <strong>preferences.conf</strong>
			 * file.
			 */
			String str = prefer.readLine();
			token = new StringTokenizer(str, ":");
			try {
				token.nextToken();
				String port = token.nextToken().trim();
				/*
				 * portNumber is initialized here
				 */
				portNumber = Integer.parseInt(port);
			} catch (NoSuchElementException e) {
				portNumber = 65530;
			}
			str = prefer.readLine();
			token = new StringTokenizer(str, ":");
			try {
				token.nextToken();
				String autoRep = token.nextToken().trim();

				/*
				 * The boolean variable AutoRepeat is initialized in the
				 * following lines.
				 */
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

			/*
			 * If the file preferences.conf is not present, initialize it to
			 * default values.
			 */
		} catch (FileNotFoundException f) {
			portNumber = 65530;
			AutoRepeat = true;
		}

		/*
		 * Initialize the socket variable here.
		 */
		try {
			socket = new ServerSocket(portNumber);
		} catch (BindException e) {

			/*
			 * This displays a message box which notifies if another instance of
			 * indic-keyboards is already running on the specified port.
			 */
			Display display = Display.getCurrent();
			Shell shell = new Shell(display);
			MessageBox messageBox = new MessageBox(shell, SWT.OK
					| SWT.ICON_ERROR);
			messageBox.setText("Duplicate Instance");
			messageBox
					.setMessage("There is already an instance of indic-keyboards running. "
							+ "Close it first. If "
							+ "another instance isn't running, close any connections on port "
							+ portNumber
							+ " and try again."
							+ " Alternatively, you can view the preferences.conf file to check the"
							+ " port number.");
			messageBox.open();
			shell.dispose();
			System.exit(0);

		}
		/*
		 * Contains the pid of the JVM which runs indic-keyboards. This can be
		 * used to kill the JVM in case indic-keyboards stops responding.
		 */
		String pid = ManagementFactory.getRuntimeMXBean().getName();
		int index = pid.indexOf("@");

		/*
		 * Debug messages which will be shown on the console
		 */
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

		/*
		 * Get the name of the main thread.
		 */
		System.out.println(Thread.currentThread().getName());

		/*
		 * The following lines creates a new thread which will run the key
		 * logger. The key presses are monitored by this thread. The thread is
		 * named as "Key Monitor"
		 */
		Runnable r = new KeyMonitor();
		Thread t = new Thread(r);
		t.setName("Key Monitor");
		t.start();
	}// end of InitLinuxStart

	 /**
	 * Static block which loads the libMIKI.so.1.0/libMIKI-x64.so.1.0 library.
	 */
	static {
		System.out.println(System.getProperty("user.dir"));
		String path = System.getProperty("user.dir");
		String arch = System.getProperty("os.arch");
		String linuxLibraryName = "libMIKI-x86.so.1.0";
		if (arch.contains("86")) {
			linuxLibraryName = "libMIKI-x86.so.1.0";
		} else if (arch.contains("64")) {
			linuxLibraryName = "libMIKI-x86_64.so.1.0";
		}
		try {
			System.load(path + "/" + linuxLibraryName);
			System.out.println(path + "/" + linuxLibraryName);
		} catch (UnsatisfiedLinkError e) {
			Display display = Display.getCurrent();
			Shell shell = new Shell(display);
			MessageBox messageBox = new MessageBox(shell, SWT.OK
					| SWT.ICON_ERROR);
			messageBox.setText("Missing Library");
			messageBox
					.setMessage("The libraries necessary to run\nindic-keyboards are missing."
							+ "Please put the "
							+ linuxLibraryName
							+ " in the program folder.");
			messageBox.open();
			shell.dispose();
			System.exit(0);
		}
	}
}
