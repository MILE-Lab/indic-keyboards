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


JNIEXPORT void JNICALL JNICALL Java_core_ParseXML_bkSpace(JNIEnv *env, jobject obj)
 {
		
	Display *disp = XOpenDisplay(NULL);
	//if(!disp) return 1;
	
	XTestFakeKeyEvent(disp, 22, True, CurrentTime);
  	XTestFakeKeyEvent(disp, 22, False, CurrentTime);
/*
jint revert_to=0;
	XEvent event;	
	Window focus_return;
	XGetInputFocus(disp, &focus_return, &revert_to);	
	XSelectInput(disp, focus_return, KeyPressMask);
				
				
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
				event.xkey.keycode = 22;
  				event.xkey.state = 0;
				XSendEvent( event.xkey.display, focus_return, True, KeyPressMask, &event);
				XAutoRepeatOn(disp);*/
				
		
	

XCloseDisplay(disp);
	 

	


	 
}



