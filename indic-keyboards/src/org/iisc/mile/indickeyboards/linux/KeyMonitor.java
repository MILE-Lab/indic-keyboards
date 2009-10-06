/**********************************************************************
 * File:           KeyMonitor.java
 * Description:    Creates a new thread for key logging.
 * Authors:        Akshay,Abhinava,Revati,Arun
 * Created:        Sat Mat 28 18:31:25 GMT 2009
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

/**
 * This class is under the thread <code>Key Monitor</code> created in the
 * <code>IndicKeyboards</code> class. This thread monitors all the key presses.
 * This class implements the <code>Runnable</code> iterface.
 * Creates an object of the <code>KeyNonitorMethods</code> class.
 * @see core.KeyMonitorMethods
 *
 */
public class KeyMonitor implements Runnable {

    /**
     * Overriding the <code>run()</code> method of the <code>Runnable</code>
     * interface.
     */
	public void run() {
		System.out.println(Thread.currentThread().getName());
        /*
         * Create an instance of the <code>KeyNonitorMethods</code> class.
         */
		KeyMonitorMethods k = new KeyMonitorMethods();
		k.identifyKB();
		k.grabKB();
	}
}
