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
