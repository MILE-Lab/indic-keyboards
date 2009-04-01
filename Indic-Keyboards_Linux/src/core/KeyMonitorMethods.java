/** ********************************************************************
 * File:           KeyMonitorMethods.java
 * Description:    1. Code to access native methods through JNI.
2. Loads the linux library.
 * Authors:        Akshay,Abhinava,Revati,Arun
 * Created:        Sat Mat 28 18:31:25 GMT 2009
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


/**
 *
 * @author Abhinav
 */
public class KeyMonitorMethods {

    private native String identify(String cmd);

    private native void grab(String KB);
    //private native void xwin(int kcode);
    static int cLock = 0;
    static int nLock = 1;
    static boolean loggingEnabled = false;
    String actualKB;
    String pathKB;

    private void printKeys(int code) {
        //xwin(code);
        ParseXML xmlobj=new ParseXML();
        if (code == 88) {
            if (loggingEnabled == true) {
                loggingEnabled = false;
                System.out.println("Key Logging Disabled");
                            

            } else if (loggingEnabled == false) {
                loggingEnabled = true;
                System.out.println("Key Logging Enabled");
            }
        }

        if (loggingEnabled == true) {
            switch (code) {
                case 1:
                    xmlobj.getPattern(" Esc ");
                    break;
                case 2:
                    xmlobj.getPattern("1");
                    break;
                case 3:
                    xmlobj.getPattern("2");
                    break;
                case 4:
                    xmlobj.getPattern("3");
                    break;
                case 5:
                    xmlobj.getPattern("4");
                    break;
                case 6:
                    xmlobj.getPattern("5");
                    break;
                case 7:
                    xmlobj.getPattern("6");
                    break;
                case 8:
                    xmlobj.getPattern("7");
                    break;
                case 9:
                    xmlobj.getPattern("8");
                    break;
                case 10:
                    xmlobj.getPattern("9");
                    break;
                case 11:
                    xmlobj.getPattern("0");
                    break;
                case 12:
                    xmlobj.getPattern("-");
                    break;
                case 13:
                    xmlobj.getPattern("=");
                    break;
                case 14:
                    xmlobj.getPattern("\b");
                    break;
                case 15:
                    xmlobj.getPattern("\t");
                    break;
                case 16:
                    xmlobj.getPattern("q");
                    break;
                case 17:
                    xmlobj.getPattern("w");
                    break;
                case 18:
                    xmlobj.getPattern("e");
                    break;
                case 19:
                    xmlobj.getPattern("r");
                    break;
                case 20:
                    xmlobj.getPattern("t");
                    break;
                case 21:
                    xmlobj.getPattern("y");
                    break;
                case 22:
                    xmlobj.getPattern("u");
                    break;
                case 23:
                    xmlobj.getPattern("i");
                    break;
                case 24:
                    xmlobj.getPattern("o");
                    break;
                case 25:
                    xmlobj.getPattern("p");
                    break;
                case 26:
                    xmlobj.getPattern("[");
                    break;
                case 27:
                    xmlobj.getPattern("]");
                    break;
                case 28:
                //    xmlobj.getPattern();
                    break;
                case 29:
                    xmlobj.getPattern(" Left Ctrl ");
                    break;
                case 30:
                    xmlobj.getPattern("a");
                    break;//xmlobj.getPattern("\u0C85");break;
                case 31:
                    xmlobj.getPattern("s");
                    break;
                case 32:
                    xmlobj.getPattern("d");
                    break;
                case 33:
                    xmlobj.getPattern("f");
                    break;
                case 34:
                    xmlobj.getPattern("g");
                    break;
                case 35:
                    xmlobj.getPattern("h");
                    break;
                case 36:
                    xmlobj.getPattern("j");
                    break;
                case 37:
                    xmlobj.getPattern("k");
                    break;
                case 38:
                    xmlobj.getPattern("l");
                    break;
                case 39:
                    xmlobj.getPattern(";");
                    break;
                case 40:
                    xmlobj.getPattern("'");
                    break;
                case 41:
                    xmlobj.getPattern("`");
                    break;
                case 42:
                    xmlobj.getPattern(" Left Shift ");
                    break;
                case 43:
                    xmlobj.getPattern("\\");
                    break;
                case 44:
                    xmlobj.getPattern("z");
                    break;
                case 45:
                    xmlobj.getPattern("x");
                    break;
                case 46:
                    xmlobj.getPattern("c");
                    break;
                case 47:
                    xmlobj.getPattern("v");
                    break;
                case 48:
                    xmlobj.getPattern("b");
                    break;
                case 49:
                    xmlobj.getPattern("n");
                    break;
                case 50:
                    xmlobj.getPattern("m");
                    break;
                case 51:
                    xmlobj.getPattern(",");
                    break;
                case 52:
                    xmlobj.getPattern(".");
                    break;
                case 53:
                    xmlobj.getPattern("/");
                    break;
                case 54:
                    xmlobj.getPattern(" Right Shift ");
                    break;
                case 55:
                    xmlobj.getPattern("*");
                    break;
                case 56:
                    xmlobj.getPattern(" Left Alt ");
                    break;
                case 57:
                    xmlobj.getPattern(" ");
                    break;
                case 58:
                    if (cLock == 0) {
                        xmlobj.getPattern(" Caps Lock On ");
                        cLock = 1;
                    } else {
                        xmlobj.getPattern(" Caps Lock Off ");
                        cLock = 0;
                    }
                    break;
                case 59:
                    xmlobj.getPattern(" F1 ");
                    break;
                case 60:
                    xmlobj.getPattern(" F2 ");
                    break;
                case 61:
                    xmlobj.getPattern(" F3 ");
                    break;
                case 62:
                    xmlobj.getPattern(" F4 ");
                    break;
                case 63:
                    xmlobj.getPattern(" F5 ");
                    break;
                case 64:
                    xmlobj.getPattern(" F6 ");
                    break;
                case 65:
                    xmlobj.getPattern(" F7 ");
                    break;
                case 66:
                    xmlobj.getPattern(" F8 ");
                    break;
                case 67:
                    xmlobj.getPattern(" F9 ");
                    break;
                case 68:
                    xmlobj.getPattern(" F10 ");
                    break;
                case 69:
                    if (nLock == 1) {
                        System.out.print(" Num Lock Off");
                        nLock = 0;
                    } else {
                        System.out.print(" Num Lock On ");
                        nLock = 1;
                    }
                    break;
                case 70:
                    xmlobj.getPattern(" Scroll Lock ");
                    break;
                case 71:
                    xmlobj.getPattern("7");
                    break;
                case 72:
                    xmlobj.getPattern("8");
                    break;
                case 73:
                    xmlobj.getPattern("9");
                    break;
                case 74:
                    xmlobj.getPattern("-");
                    break;
                case 75:
                    xmlobj.getPattern("4");
                    break;
                case 76:
                    xmlobj.getPattern("5");
                    break;
                case 77:
                    xmlobj.getPattern("6");
                    break;
                case 78:
                    xmlobj.getPattern("+");
                    break;
                case 79:
                    xmlobj.getPattern("1");
                    break;
                case 80:
                    xmlobj.getPattern("2");
                    break;
                case 81:
                    xmlobj.getPattern("3");
                    break;
                case 82:
                    xmlobj.getPattern("0");
                    break;
                case 83:
                    xmlobj.getPattern(".");
                    break;
                case 87:
                    xmlobj.getPattern(" F11 ");
                    break;
                default: break;
            }
        }
        if (loggingEnabled == false) {
            //do nothing
        }
    }

    void identifyKB() {
        String id = "/dev/input/event";
        String kb = null;
        int found = 0;
        for (int i = 0; i < 7; i++) {
            String temp = "" + i;
            actualKB = identify(id + temp);
            if (actualKB.contains("keyboard") || actualKB.contains("Keyboard") || actualKB.contains("KEYBAORD")) {
                System.out.println("The keyboard present is : " + actualKB);
                pathKB = id + temp;
                found = 1;
                break;
            }

        }
        if (found == 0) {
            System.out.println("No proper keyboard found");
            System.exit(0);
        }
    }

    void grabKB() {
        System.out.println("The path to keyboard's input interface : " + pathKB);
        grab(pathKB);
    }


    static {
    	System.out.println(System.getProperty("user.dir"));
    	String path = System.getProperty("user.dir");
        try{
    	System.load(path+"/libKeyMonitor.so.1.0");
        }
        catch (UnsatisfiedLinkError e)
        {
        	System.load("/usr/lib/libKeyMonitor.so.1.0");
        }
    }
}


