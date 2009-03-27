/** ********************************************************************
 * File:           KeyboardHook.java 
 * Description:    
 * Authors:        Akshay,Abhinava,Revati,Arun 
 * Created:        Thu Mar 26 20:01:25 IST 2009
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

package core;

// KeyboardHook.java
//import java.io.*;

public class KeyboardHook {
	public KeyboardHook() {
		(new PollThread(this)).start();
	}

	protected javax.swing.event.EventListenerList listenerList = new javax.swing.event.EventListenerList();

	public void addEventListener(KeyboardEventListener listener) {
		listenerList.add(KeyboardEventListener.class, listener);
	}

	public void removeEventListener(KeyboardEventListener listener) {
		listenerList.remove(KeyboardEventListener.class, listener);
	}

	void keyPressed(KeyboardEvent event) {
		Object[] listeners = listenerList.getListenerList();
		for (int i = 0; i < listeners.length; i += 2) {
			if (listeners[i] == KeyboardEventListener.class) {
				((KeyboardEventListener) listeners[i + 1])
						.GlobalKeyPressed(event);
			}
		}
	}
	
	void keyReleased(KeyboardEvent event) {
		Object[] listeners = listenerList.getListenerList();
		for (int i = 0; i < listeners.length; i += 2) {
			if (listeners[i] == KeyboardEventListener.class) {
				((KeyboardEventListener) listeners[i + 1])
						.GlobalKeyReleased(event);
			}
		}
	}
}

class PollThread extends Thread {
	public native void checkKeyboardChanges();

	private KeyboardHook kbh;

	public PollThread(KeyboardHook kh) {
		kbh = kh;
		System.loadLibrary("Syshook");
	}

	@Override
	public void run() {
		for (;;) {
			checkKeyboardChanges();
			// yield();
			try {
				Thread.sleep(1);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	void Callback(boolean ts, int vk, boolean ap, boolean ek) {
		KeyboardEvent event = new KeyboardEvent(this, ts, vk, ap, ek);
		if (ts) {
			kbh.keyPressed(event);
		}else {
			kbh.keyReleased(event);
		}
	}
}
