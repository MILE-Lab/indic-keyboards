/**********************************************************************
 * File:           linuxLibraries.c
 * Description:    All the native methods declared in the file
					 linuxLibraries.java are defined here.
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

#include <X11/Xlib.h>
#include <stdlib.h>
#include <unistd.h>
#include <stdio.h>
#include <stdbool.h>
#include <string.h>
#include <linux/input.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <math.h>
#include "org_iisc_mile_indickeyboards_linux_LinuxLibraries.h"
#define test_bit(bit, array)    (array[bit/8] & (1<<(bit%8)))

JNIEXPORT jstring JNICALL Java_org_iisc_mile_indickeyboards_linux_LinuxLibraries_identify(JNIEnv *env,
		jobject obj, jstring id) {

	jint fd = -1;
	jint index;

	const jbyte *str;
	char buf[128];
	char name[256] = "Unknown";

	str = (*env)->GetStringUTFChars(env, id, NULL);

	if ((fd = open(str, O_RDONLY)) < 0) {
		perror("evdev open");
		exit(1);
	}

	if (ioctl(fd, EVIOCGNAME(sizeof(name)), name) < 0) {
		perror("evdev ioctl");
	}

	strcpy(buf, name);

	close(fd);
	return (*env)->NewStringUTF(env, buf);
}

JNIEXPORT void JNICALL Java_org_iisc_mile_indickeyboards_linux_LinuxLibraries_grab(JNIEnv *env, jobject obj, jstring kb) {

	const int LEFT_SHIFT = 42;
	const int RIGHT_SHIFT = 54;

	const int ALT = 56;
	const int ALT_GRAPH = 100;

	const int LEFT_CTRL = 29;
	const int RIGHT_CTRL = 97;

	const int F12 = 88;

	const int KEY_PRESS = 1;
	const int KEY_RELEASE = 0;
	const int KEY_AUTOREPEAT = 2;

	jint fd = -1;
	jint index;

	jclass class = (*env)->FindClass(env,"org/iisc/mile/indickeyboards/linux/KeyMonitorMethods");
	//jclass cls = (*env)->GetObjectClass(env, obj);
	jmethodID mid = (*env)->GetMethodID(env, class, "printKeys", "(I)V");

	jmethodID constructor = (*env)->GetMethodID(env, class, "<init>", "()V");
	jobject object = (*env)->NewObject(env,class,constructor);

	jint shiftFlag = False;
	jint altFlag = False;
	jint ctrlFlag = False;

	const jbyte *str;
	str = (*env)->GetStringUTFChars(env, kb, NULL);

	if ((fd = open(str, O_RDONLY)) < 0) {
		perror("evdev open");
		exit(1);
	}

	int rd;

	/*
	 * Linux input device event interface (Linux USB subsystem).
	 * View the file <linux/input.h> for more info.
	 */
	struct input_event event[1];

	while (1) {
		rd = read(fd, event, sizeof (struct input_event) *1);

		if (rd < (int) sizeof (struct input_event)) {
			perror("evtest: short read");
			exit(1);
		}
		for (index = 0; index < (int) (rd / sizeof(struct input_event)); index++)
		{

			if (EV_KEY == event[index].type) {
				if ((event[index].code == LEFT_SHIFT || event[index].code == RIGHT_SHIFT)) {
					if (event[index].value == KEY_PRESS || event[index].value == KEY_AUTOREPEAT)
					shiftFlag = True;
					else if (event[index].value == KEY_RELEASE)
					shiftFlag = False;

				}

				if ((event[index].code == ALT || event[index].code == ALT_GRAPH)) {
					if (event[index].value == KEY_PRESS || event[index].value == KEY_AUTOREPEAT)
					altFlag = True;
					else if (event[index].value == KEY_RELEASE)
					altFlag = False;

				}

				if ((event[index].code == LEFT_CTRL || event[index].code == RIGHT_CTRL)) {
					if (event[index].value == KEY_PRESS || event[index].value == KEY_AUTOREPEAT)
					ctrlFlag = True;
					else if (event[index].value == KEY_RELEASE)
					ctrlFlag = False;

				}

				else if (event[index].value == KEY_RELEASE || event[index].value == KEY_AUTOREPEAT) {
					if(ctrlFlag) {}
					else if(shiftFlag)
					(*env)->CallVoidMethod(env, object, mid, event[index].code + 200);
					else if(!shiftFlag)
					(*env)->CallVoidMethod(env, object, mid, event[index].code);
					if(altFlag)
					if (event[index].code == F12)
					(*env)->CallVoidMethod(env, object, mid, 666);
				}
			}
		}
	}
	close(fd);
}

JNIEXPORT void JNICALL Java_org_iisc_mile_indickeyboards_linux_LinuxLibraries_keyrepeat(JNIEnv *env, jobject obj, jint flag) {
	Display *display = XOpenDisplay(NULL);
	if(flag==1)
	XAutoRepeatOn(display);
	if(flag==0)
	XAutoRepeatOff(display);
	XCloseDisplay(display);
}

JNIEXPORT void JNICALL JNICALL Java_org_iisc_mile_indickeyboards_linux_LinuxLibraries_OutputActiveWindow(JNIEnv *env, jobject obj, jstring unicode)
{
	Display *display = XOpenDisplay(NULL);
	KeySym k;
	const jbyte *str;
	str = (*env)->GetStringUTFChars(env, unicode, NULL);

	jint revert_to=0;
	XEvent event;
	Window focus_return;
	XGetInputFocus(display, &focus_return, &revert_to);
	XSelectInput(display, focus_return, KeyPressMask);

	KeySym sym = XStringToKeysym(str);
	int min, max, numcodes;
	XDisplayKeycodes(display,&min,&max);
	KeySym *keysym;
	keysym = XGetKeyboardMapping(display,min,max-min+1,&numcodes);
	keysym[(max-min-1)*numcodes]=sym;
	XChangeKeyboardMapping(display,min,numcodes,keysym,(max-min));
	XFree(keysym);
	KeyCode code = XKeysymToKeycode(display,sym);

	event.xkey.display = display;
	event.xkey.window = focus_return;
	event.xkey.root = DefaultRootWindow(display);
	event.xkey.subwindow = None;
	event.xkey.time = CurrentTime;
	event.xkey.x = 1;
	event.xkey.y = 1;
	event.xkey.x_root = 1;
	event.xkey.y_root = 1;
	event.xkey.same_screen = True;
	event.xkey.type = KeyPress;

	event.xkey.keycode = code;
	event.xkey.state = 0;
	XSendEvent( event.xkey.display, focus_return, True, KeyPressMask, &event);

	XCloseDisplay(display);
}

