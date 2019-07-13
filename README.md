This is a java project for serving the TM1368 display board.
Feel free to contribute, license is open, project can be modified, distributed, sell etc, the only requirement is to leave a not from where it has been originally created.

example of usage:

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

import tm1638.TM1638;

public class Start {

	public static void main(String[] args) throws InterruptedException {
		Pin stb = RaspiPin.GPIO_15;
		Pin clk = RaspiPin.GPIO_16;
		Pin dio = RaspiPin.GPIO_04;

		TM1638 tm1638 = new TM1638(dio, clk, stb, 7);

		tm1638.displayText("HELLO.", true);

		ledsShow(tm1638);

		tm1638.displayText("PRESS A", true);
		sleep(1000);

		tm1638.displayText("BUTTON", true);

		while (true) {
			byte buttons = 0;
			while ((buttons = tm1638.readButtons()) == 0)
				;
			tm1638.displayText("DETECT " + (8 - Integer.numberOfTrailingZeros(buttons)), true);
		}

	}


	private static void ledsShow(TM1638 tm1638) {
		for (int index = 0; index < 8; index++) {
			tm1638.setLed(index, true);
			sleep(100);
		}
		for (int index = 0; index < 8; index++) {
			tm1638.setLed(index, false);
			sleep(100);
		}
	}

	private static void sleep(int delay) {
		try {
			Thread.currentThread().sleep(delay);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static void count(TM1638 tm1638) {
		for (int n = 0;; n++) {
			tm1638.displayText(Integer.toString(n++), true);
		}
	}

}
