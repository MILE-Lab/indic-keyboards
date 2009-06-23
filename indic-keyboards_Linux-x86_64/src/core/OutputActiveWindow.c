#include <X11/Xlib.h>
#include <X11/X.h>
#include <stdlib.h>
#include <unistd.h>
#include <stdio.h>
#include <X11/Xlib.h>
#include <X11/Xatom.h>
#include <stdbool.h>
#include <X11/keysym.h>
#include <X11/keysymdef.h>
#include "core_ParseXML.h"
#include <X11/Xmu/Lookup.h>
#include <string.h>


JNIEXPORT void JNICALL JNICALL Java_core_ParseXML_OutputActiveWindow(JNIEnv *env, jobject obj, jstring unicode)
 {
		
	Display *disp = XOpenDisplay(NULL);
	//if(!disp) return 1;
	KeySym k;
	const jbyte *str;
	str = (*env)->GetStringUTFChars(env, unicode, NULL);
	
	jint revert_to=0;
	XEvent event;	
	Window focus_return;
	XGetInputFocus(disp, &focus_return, &revert_to);	
	XSelectInput(disp, focus_return, KeyPressMask);
				
				KeySym sym = XStringToKeysym(str);
				int min, max, numcodes;
  				XDisplayKeycodes(disp,&min,&max);
  				KeySym *keysym;
				keysym = XGetKeyboardMapping(disp,min,max-min+1,&numcodes);
 				keysym[(max-min-1)*numcodes]=sym;
				XChangeKeyboardMapping(disp,min,numcodes,keysym,(max-min));
				XFree(keysym);
  				//XFlush( disp );
				KeyCode code = XKeysymToKeycode(disp,sym);

				event.xkey.display = disp;
				event.xkey.window = focus_return;
				event.xkey.root = DefaultRootWindow(disp);
  				event.xkey.subwindow = None;
  				event.xkey.time = CurrentTime;
  				event.xkey.x = 1;
  				event.xkey.y = 1;
  				event.xkey.x_root = 1;
  				event.xkey.y_root = 1;
  				event.xkey.same_screen = True;
  				event.xkey.type = KeyPress;
	
				//XTestFakeKeyEvent(disp, 22, True, 1);
  				//XTestFakeKeyEvent(disp, 22, False, 1);

				/*event.xkey.keycode=22;
				event.xkey.state = 0;	
                                XSendEvent( event.xkey.display, focus_return, True, KeyPressMask, &event);*/
				//XSync(disp,False);
				
				
				
				event.xkey.keycode = code;
  				event.xkey.state = 0;
				XSendEvent( event.xkey.display, focus_return, True, KeyPressMask, &event);
				
				//XSync(disp,False);
				
		
	

XCloseDisplay(disp);
	 
}

//xwininfo -root -tree | grep "my-window-title"


