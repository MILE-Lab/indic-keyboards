#include <linux/input.h>
#include <sys/stat.h>
#include <stdlib.h>
#include <fcntl.h>
#include <stdio.h>
#include "core_KeyMonitorMethods.h"
#include <string.h>
#include <stdbool.h>
#include <math.h>

JNIEXPORT jstring JNICALL
Java_core_KeyMonitorMethods_identify(JNIEnv *env, jobject obj, jstring id) {

    jint fd = -1;
    jint yalv;
    const jbyte *str;
    char buf[128];
    char name[256] = "Unknown";

    str = (*env)->GetStringUTFChars(env, id, NULL);
    system("clear");


    if ((fd = open(str, O_RDONLY)) < 0) {
        perror("evdev open");
        exit(1);
    }

    if (ioctl(fd, EVIOCGNAME(sizeof (name)), name) < 0) {
        perror("evdev ioctl");
    }


    strcpy(buf, name);
    /* printf("The device on %s says its name is %s\n",
           argv[1], name); */

    close(fd);
    return (*env)->NewStringUTF(env, buf);
}

JNIEXPORT void JNICALL Java_core_KeyMonitorMethods_grab
(JNIEnv *env, jobject obj, jstring kb) {

    jclass cls = (*env)->GetObjectClass(env, obj);
    jmethodID mid = (*env)->GetMethodID(env, cls, "printKeys", "(I)V");
    jmethodID midP = (*env)->GetMethodID(env, cls, "keyLogControl", "()V");
    jint fd = -1;
    jint yalv;
    clock_t press, release;
    long press1, press2;
    long delay;
    const jbyte *str;
    str = (*env)->GetStringUTFChars(env, kb, NULL);

    if ((fd = open(str, O_RDONLY)) < 0) {
        perror("evdev open");
        exit(1);
    }
    /* how many bytes were read */
    int rd;

    /* the events (up to 64 at once) */
    struct input_event ev[1];

    //system("stty -echo");

    while (1) {
        rd = read(fd, ev, sizeof (struct input_event));

        if (rd < (int) sizeof (struct input_event)) {
            perror("evtest: short read");
            exit(1);
        }
        if (ev[0].value == 1) {
            press1 = ev[0].time.tv_usec;
            if ((ev[0].code == 42 && ev[0].value != 0) || (ev[0].code == 42 && ev[0].value == 1) || (ev[0].code == 42 && ev[0].value != 0) || (ev[0].code == 54 && ev[0].value != 0) || (ev[0].code == 54 && ev[0].value == 1) || (ev[0].code == 54 && ev[0].value != 0)) {
                int rd1;
                struct input_event ev1[1];
                while (1) {

                    rd1 = read(fd, ev1, sizeof (struct input_event));

                    if (rd1 < (int) sizeof (struct input_event)) {
                        perror("evtest: short read");
                        exit(1);
                    }
                    press2 = ev1[0].time.tv_usec;
                    delay = press2 - press1;
                    if (abs(delay) > 290000) break;

                    if (ev1[0].value == 1) {



                        (*env)->CallVoidMethod(env, obj, mid, ev1[0].code + 200);
                        break;

                    }

                }

            } else if (ev[0].code != 42 || ev[0].code != 54)
                //printf("code %d \n", ev[0].code);
                (*env)->CallVoidMethod(env, obj, mid, ev[0].code);

        }

    }


    close(fd);
    system("stty echo");

}
