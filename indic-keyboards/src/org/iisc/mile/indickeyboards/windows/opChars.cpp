/**********************************************************************
 * File:           opChars.cpp
 * Description:    Native code responsible to put Unicode onto the
 * 					to the current active window.
 * Authors:        Akshay,Abhinava,Revati,Arun
 * Created:        Thu Oct 1 18:31:25 GMT 2009
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
#define _WIN32_WINNT 0x0500
#define WINVER 0x0400
#include <windows.h>
#include <winuser.h>
#include "jni.h"
#include "org_iisc_mile_indickeyboards_windows_OutputCharToActiveWindow.h"


JNIEXPORT void JNICALL Java_org_iisc_mile_indickeyboards_windows_OutputCharToActiveWindow_opChars(JNIEnv *env, jobject obj, jint ucode)
{

KEYBDINPUT kb={0};
INPUT Input={0};

// down
kb.wScan = ucode;/*enter unicode here*/;
kb.dwFlags = 4; // KEYEVENTF_UNICODE=4
Input.type = INPUT_KEYBOARD;
Input.ki = kb;

::SendInput(1,&Input,sizeof(Input));

// up
kb.wScan = ucode;/*enter unicode here*/;
kb.dwFlags = 4|KEYEVENTF_KEYUP; //KEYEVENTF_UNICODE=4
Input.type = INPUT_KEYBOARD;
Input.ki = kb;

::SendInput(1,&Input,sizeof(Input));

}
