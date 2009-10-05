/**********************************************************************
 * File:           LinuxLibraries.java
 * Description:    All the native methods are declared here.
 * Authors:        Akshay,Abhinava,Revati,Arun
 * Created:        Thu Oct 1 18:31:25 GMT 2009
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

package org.iisc.mile.indickeyboards.linux;

public class LinuxLibraries {

	/**
     * This native method identifies the interface to the keyboard. Generally,
     * the path to the interface would be in the form <em>/dev/input/eventX</em>
     * where X=0, 1, 2 etc.
     * @param cmd The parameter will be <em>/dev/input/eventX</em>, X=0, 1, 2..
     * @return Returns a string which will have the description of the keyboard present.
     */
	public native String identify(String cmd);

    /**
     * Once the event interface to the keyboard is obtained, grab the keyboard and
     * start monitoring all the key presses. This is a native method.
     * @param KB The correct path to the keyboard's event interface obtained from
     * the native method <code>identify()</code>
     */
	public native void grab(String KB);

    /**
     * Native method which controls the AutoRepeat property of the keyboard.
     * This is checked using the AutoRepeat boolean variable in the
     * IndicKeyboards class. Xlib used to control this feature.
     * @see org.iisc.mile.indickeyboards.linux.InitLinux#AutoRepeat AutoRepeat
     * @param flag
     */
	public native void keyrepeat(int flag);

   /**
    * Calls the native method which outputs the character onto the active
    * window. Java Native Interface (JNI) is used to call the this method. X11
    * libraries are used to achieve this.
    * @param ucode Contains the Unicode code point value which will be put onto the active window.
    */
	public native static void OutputActiveWindow(String ucode);
}
