/** ********************************************************************
 * File:           SWTResourceManager.java
 * Description:    Used by the XMLGenerator.java file to do SWT
 * 					memory clean up.
 * Authors:        Akshay,Abhinava,Revati,Arun
 * Created:        Thu Mar 26 20:01:25 IST 2009
 *
 * (C) Copyright 2008, MILE Lab, Indian Institute of Science
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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Widget;

/**
 * Class to manage SWT resources (Font, Color, Image and Cursor)
 * There are no restrictions on the use of this code.
 *

 * #SWTResourceManager:version4.0.0#
 */
public class SWTResourceManager {

	@SuppressWarnings("unchecked")
	private static HashMap resources = new HashMap();
	@SuppressWarnings("unchecked")
	private static Vector users = new Vector();
	private static SWTResourceManager instance = new SWTResourceManager();

	private static DisposeListener disposeListener = new DisposeListener() {
		public void widgetDisposed(DisposeEvent e) {
			users.remove(e.getSource());
			if (users.size() == 0)
				dispose();
		}
	};

	/**
	 * This method should be called by *all* Widgets which use resources
	 * provided by this SWTResourceManager. When widgets are disposed,
	 * they are removed from the "users" Vector, and when no more
	 * registered Widgets are left, all resources are disposed.
	 * <P>
	 * If this method is not called for all Widgets then it should not be called
	 * at all, and the "dispose" method should be explicitly called after all
	 * resources are no longer being used.
	 */
	@SuppressWarnings("unchecked")
	public static void registerResourceUser(Widget widget) {
		if (users.contains(widget))
			return;
		users.add(widget);
		widget.addDisposeListener(disposeListener);
	}

	@SuppressWarnings("unchecked")
	public static void dispose() {
		Iterator it = resources.keySet().iterator();
		while (it.hasNext()) {
			Object resource = resources.get(it.next());
			if (resource instanceof Font)
				 ((Font) resource).dispose();
			else if (resource instanceof Color)
				 ((Color) resource).dispose();
			else if (resource instanceof Image)
				 ((Image) resource).dispose();
			else if (resource instanceof Cursor)
				 ((Cursor) resource).dispose();
		}
		resources.clear();
	}

	public static Font getFont(String name, int size, int style) {
		return getFont(name, size, style, false, false);
	}

	@SuppressWarnings("unchecked")
	public static Font getFont(String name, int size, int style, boolean strikeout, boolean underline) {
		String fontName = name + "|" + size + "|" + style + "|" + strikeout + "|" + underline;
		if (resources.containsKey(fontName))
			return (Font) resources.get(fontName);
		FontData fd = new FontData(name, size, style);
		if (strikeout || underline) {
			try {
				Class lfCls = Class.forName("org.eclipse.swt.internal.win32.LOGFONT");
				Object lf = FontData.class.getField("data").get(fd);
				if (lf != null && lfCls != null) {
					if (strikeout)
						lfCls.getField("lfStrikeOut").set(lf, new Byte((byte) 1));
					if (underline)
						lfCls.getField("lfUnderline").set(lf, new Byte((byte) 1));
				}
			} catch (Throwable e) {
				System.err.println(
					"Unable to set underline or strikeout" + " (probably on a non-Windows platform). " + e);
			}
		}
		Font font = new Font(Display.getDefault(), fd);
		resources.put(fontName, font);
		return font;
	}

	public static Image getImage(String url, Control widget) {
		Image img = getImage(url);
		if(img != null && widget != null)
			img.setBackground(widget.getBackground());
		return img;
	}

	@SuppressWarnings("unchecked")
	public static Image getImage(String url) {
		try {
			url = url.replace('\\', '/');
			if (url.startsWith("/"))
				url = url.substring(1);
			if (resources.containsKey(url))
				return (Image) resources.get(url);
			Image img = new Image(Display.getDefault(), instance.getClass().getClassLoader().getResourceAsStream(url));
			if (img != null)
				resources.put(url, img);
			return img;
		} catch (Exception e) {
			System.err.println("SWTResourceManager.getImage: Error getting image "+url+", "+e);
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public static Color getColor(int red, int green, int blue) {
		String name = "COLOR:" + red + "," + green + "," + blue;
		if (resources.containsKey(name))
			return (Color) resources.get(name);
		Color color = new Color(Display.getDefault(), red, green, blue);
		resources.put(name, color);
		return color;
	}

	@SuppressWarnings("unchecked")
	public static Cursor getCursor(int type) {
		String name = "CURSOR:" + type;
		if (resources.containsKey(name))
			return (Cursor) resources.get(name);
		Cursor cursor = new Cursor(Display.getDefault(), type);
		resources.put(name, cursor);
		return cursor;
	}

}
