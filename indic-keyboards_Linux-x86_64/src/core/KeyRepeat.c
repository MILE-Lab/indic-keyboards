#include <X11/Xlib.h>
#include <X11/X.h>
#include <stdlib.h>
#include <unistd.h>
#include <stdio.h>
#include <stdbool.h>

#include "core_KeyMonitorMethods.h"

JNIEXPORT void JNICALL Java_core_KeyMonitorMethods_keyrepeat(JNIEnv *env, jobject obj, jint flag){
Display *disp = XOpenDisplay(NULL);
if(flag==1)
	XAutoRepeatOn(disp);
if(flag==0)
	XAutoRepeatOff(disp);

XCloseDisplay(disp);
}
