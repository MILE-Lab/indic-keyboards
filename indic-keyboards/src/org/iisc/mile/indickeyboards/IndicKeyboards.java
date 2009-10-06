/** ********************************************************************
 * File:           IndicKeyboards.java 
 * Description:    Entry point to indic-keyboards. Branches appropriately to
 * 					initialize the Windows/Linux version.
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

package org.iisc.mile.indickeyboards;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.iisc.mile.indickeyboards.windows.InitWin;
import org.iisc.mile.indickeyboards.linux.InitLinux;

public class IndicKeyboards {
	public static String workingDirectory = System.getProperty("user.dir");
	public static String operatingSystem = System.getProperty("os.name");

	public static void main(String[] args) throws FileNotFoundException {

		System.out.println("Operating System : "
				+ System.getProperty("os.name"));
		System.out.println("Architecture : " + System.getProperty("os.arch"));
		if (operatingSystem.contains("Windows")) {
			/**
			 * Windows initiation code
			 */
			InitWin ob = new InitWin();
			ob.InitWinStart();
		} else {
			/**
			 * Linux Code goes here
			 */
			InitLinux ob = new InitLinux();
			try {
				ob.InitLinuxStart();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		/*
		 * Common code
		 */
		System.out.println("Press Alt + F12 to enable key logging");
		SplashScreen s = new SplashScreen();
		s.screen();
		@SuppressWarnings("unused")
		UI iFace = new UI();

	}// end of main

}
