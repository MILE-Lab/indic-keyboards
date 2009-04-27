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

import java.io.FileNotFoundException;
import java.lang.management.ManagementFactory;

public class IndicKeyboards {

	public static void main(String[] args) throws FileNotFoundException {

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
		String pid = ManagementFactory.getRuntimeMXBean().getName();
    	int index=pid.indexOf("@");
    	System.out.println("This is the process ID : " +pid.substring(0, index));
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
