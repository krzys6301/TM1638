This is a pure java library for serving the TM1368 display board on raspberry pi platform.
It allows to 
* display numbers/text/dots 
* set leds ON/OFF
* read the buttons pressed.

example of usage:

	import com.pi4j.io.gpio.Pin;
	import com.pi4j.io.gpio.RaspiPin;
	
	import kcichocki.tm1638.TM1638;
	
	public class Start {
	
		public static void main(String[] args) throws InterruptedException {
			Pin stb = RaspiPin.GPIO_15;
			Pin clk = RaspiPin.GPIO_16;
			Pin dio = RaspiPin.GPIO_04;
	
			TM1638 tm1638 = new TM1638(dio, clk, stb, 3);
	
			tm1638.displayText("HELLO.   ");
			sleep(1000);
			tm1638.clearDisplay();
	
			for (int n = 0; n < 2; n++) {
				ledsShow(tm1638);
			}
	
			tm1638.setBrightness(3);
			tm1638.displayText("PRESS A  ");
			sleep(2000);
	
			tm1638.displayText("BUTTON   ");
	
			while (true) {
				byte buttons = 0;
				while ((buttons = tm1638.readButtons()) == 0)
					;
				tm1638.displayText("DETECT " + (8 - Integer.numberOfTrailingZeros(buttons)));
			}
	
		}
	
		private static void ledsShow(TM1638 tm1638) {
			tm1638.setBrightness(1);
			for (int index = 0; index < 8; index++) {
				tm1638.ledOn(index);
				tm1638.ledOff(index - 2);
				sleep(100);
			}
			tm1638.setBrightness(7);
			tm1638.ledOn(7);
			tm1638.ledOff(6);
			sleep(100);
			tm1638.setBrightness(1);
			for (int index = 7; index >= 0; index--) {
				tm1638.ledOn(index);
				tm1638.ledOff(index + 2);
				sleep(100);
			}
			tm1638.setBrightness(7);
			tm1638.ledOn(0);
			tm1638.ledOff(1);
			sleep(100);
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
				tm1638.displayText(Integer.toString(n++));
			}
		}
	
	}
