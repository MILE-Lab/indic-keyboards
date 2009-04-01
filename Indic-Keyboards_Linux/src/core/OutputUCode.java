package core;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

public class OutputUCode {
	public static void outputCode(String uni) {
		try {
			Robot r;
			r = new Robot();
			r.keyPress(KeyEvent.VK_BACK_SPACE);
			r.keyRelease(KeyEvent.VK_BACK_SPACE);
			r.keyPress(KeyEvent.VK_U);
			r.keyPress(KeyEvent.VK_SHIFT);
			r.keyPress(KeyEvent.VK_CONTROL);
		
			//System.out.println(uni);
		
		for (int i = 0; i < uni.length(); i++) {
			//System.out.println(c);
			//OutputUCode.printChar(uni.charAt(i));;
			switch (uni.charAt(i)) {
			case '0':
				r.keyPress(KeyEvent.VK_0);
				r.keyRelease(KeyEvent.VK_0);
				break;
			case '1':
				r.keyPress(KeyEvent.VK_1);
				r.keyRelease(KeyEvent.VK_1);

				break;
			case '2':
				r.keyPress(KeyEvent.VK_2);
				r.keyRelease(KeyEvent.VK_2);
				break;
			case '3':
				r.keyPress(KeyEvent.VK_3);
				r.keyRelease(KeyEvent.VK_3);
				break;
			case '4':
				r.keyPress(KeyEvent.VK_4);
				r.keyRelease(KeyEvent.VK_4);
				break;
			case '5':
				r.keyPress(KeyEvent.VK_5);
				r.keyRelease(KeyEvent.VK_5);
				break;
			case '6':
				r.keyPress(KeyEvent.VK_6);
				r.keyRelease(KeyEvent.VK_6);
				break;
			case '7':
				r.keyPress(KeyEvent.VK_7);
				r.keyRelease(KeyEvent.VK_7);
				break;
			case '8':
				r.keyPress(KeyEvent.VK_8);
				r.keyRelease(KeyEvent.VK_8);
				break;
			case '9':
				r.keyPress(KeyEvent.VK_9);
				r.keyRelease(KeyEvent.VK_9);
				break;
			case 'A':
				r.keyPress(KeyEvent.VK_A);
				r.keyRelease(KeyEvent.VK_A);
				break;
			case 'B':
				r.keyPress(KeyEvent.VK_B);
				r.keyRelease(KeyEvent.VK_B);
				break;
			case 'C':
				r.keyPress(KeyEvent.VK_C);
				r.keyRelease(KeyEvent.VK_C);
				break;
			case 'D':
				r.keyPress(KeyEvent.VK_D);
				r.keyRelease(KeyEvent.VK_D);
				break;
			case 'E':
				r.keyPress(KeyEvent.VK_E);
				r.keyRelease(KeyEvent.VK_E);
				break;
			case 'F':
				r.keyPress(KeyEvent.VK_F);
				r.keyRelease(KeyEvent.VK_F);
				break;
				default:break;
			}
		}

			//r.delay(1000);
			r.keyRelease(KeyEvent.VK_U);
			r.keyRelease(KeyEvent.VK_SHIFT);
			r.keyRelease(KeyEvent.VK_CONTROL);

		} catch (AWTException e) {
			e.printStackTrace();
		}

	}

	
}
