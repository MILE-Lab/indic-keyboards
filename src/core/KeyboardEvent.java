/** ********************************************************************
 * File:           KeyboardEvent.java 
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

//KeyboardEvent.java
import java.util.*;

@SuppressWarnings("serial")
public class KeyboardEvent extends EventObject {
	boolean ts, ap, ek;
	int vk;

	public KeyboardEvent(Object source, boolean ts, int vk, boolean ap,
			boolean ek) {
		super(source);
		this.ts = ts;
		this.vk = vk;
		this.ap = ap;
		this.ek = ek;
	}

	public boolean getTransitionState() {
		return ts;
	}

	public int getVirtualKeyCode() {
		return vk;
	}

	public boolean isAltPressed() {
		return ap;
	}

	public boolean isExtendedKey() {
		return ek;
	}

	public boolean equals(KeyboardEvent event) {
		if (event.getVirtualKeyCode() == vk) {
			if (event.isExtendedKey() == ek) {
				if (event.isAltPressed() == ap) {
					return true;
				}
			}
		}
		return false;
	}
}
