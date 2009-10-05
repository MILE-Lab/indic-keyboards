package org.iisc.mile.indickeyboards;

import java.io.FileNotFoundException;
import java.io.IOException; 


import org.iisc.mile.indickeyboards.windows.InitWin;
import org.iisc.mile.indickeyboards.linux.InitLinux;

public class IndicKeyboards {
	public static String workingDirectory = System.getProperty("user.dir");
	public static String operatingSystem=System.getProperty("os.name");

	public static void main(String[] args) throws FileNotFoundException {

		System.out.println("Operating System : " + System.getProperty("os.name"));
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
