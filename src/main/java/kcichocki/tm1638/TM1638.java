package kcichocki.tm1638;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalMultipurpose;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;

public class TM1638 {

	// commands for TM1638
	private final byte READ_MODE = 0x02;
	private final byte WRITE_MODE = 0x00;
	private final byte INCR_ADDR = 0x00;
	private final byte FIXED_ADDR = 0x04;

	// communication pins
	private final GpioPinDigitalMultipurpose dio;
	private final GpioPinDigitalOutput clk;
	private final GpioPinDigitalOutput stb;

	// definition of letters for the display
	private final Font font = new Font();
	private final byte dot = font.translateChar('.');
	private byte lastButtons = 0;

	/**
	 * @param dio        Data I/O GPIO
	 * @param clk        clock GPIO
	 * @param stb        Chip Select GPIO
	 * @param brightness brightness of the display (between 0 and 7)
	 */
	public TM1638(Pin dio, Pin clk, Pin stb, int brightness) {
		final GpioController gpio = GpioFactory.getInstance();
		this.dio = gpio.provisionDigitalMultipurposePin(dio, "dio", PinMode.DIGITAL_INPUT);
		this.clk = gpio.provisionDigitalOutputPin(clk, "clk", PinState.HIGH);
		this.stb = gpio.provisionDigitalOutputPin(stb, "stb", PinState.HIGH);
		setBrightness((byte) brightness);
		clearDisplay();
	}

	/**
	 * displays a text on 8 char display, signs are written from left to right,
	 * there could be a dot after each sign, it will be added to this sign. if the
	 * text is shorter than the display, the remainding registers will be leaved as
	 * they are, meaning no update. If needed to pad text to left, then string
	 * should be padded to be 8 chars long.
	 * 
	 * @param text - text to be displayed.
	 */
	public void displayText(String text) {
		int stringPos = 0;
		for (int index = 0; index < Math.min(8, text.length()); index++) {
			byte displayRegister = (byte) ((index % 8) * 2);
			byte characterToDisplay = font.translateChar(text.charAt(stringPos));
			if (characterToDisplay != dot && stringPos + 1 < text.length()) {
				if (text.charAt(stringPos + 1) == '.') {
					characterToDisplay = (byte) (characterToDisplay | dot);
					stringPos++;
				}

			}
			sendData(displayRegister, characterToDisplay);
			stringPos++;
		}
	}

	/**
	 * Reads the buttons state as byte. When one of the first 4 buttons is pressed,
	 * we can't read the state of the corresponding button from the last 4 buttons,
	 * because of this, last known state of this button is returned.
	 * 
	 * @return byte, where 8 bits represents buttons state
	 */
	public byte readButtons() {
		byte[] buffer = new byte[7];
		readData(buffer);
		byte buttons = (byte) ((buffer[0] & 17) | (buffer[1] & 34) | (buffer[2] & 68) | (buffer[3] & 136));
		lastButtons = (byte) (buttons & 15 | lastButtons & 240);
		for (int bit = 1; bit <= 8; bit <<= 1) {
			if ((buttons & bit) == 0) {
				lastButtons = (byte) ((lastButtons & ~(bit << 4)) | (buttons & (bit << 4)));
			}
		}
		return reverseByte(lastButtons);
	}

	private byte reverseByte(byte x) {
		return (byte) (Integer.reverse(x) >> 24);
	}

	/**
	 * turns on a led
	 * @param index - the index of led to be turned on, valid range is 0 to 7
	 */
	public void ledOn(int index) {
		setLed(index, 1);
	}
	/**
	 * turns off a led
	 * @param index - the index of led to be turned on, valid range is 0 to 7
	 */
	public void ledOff(int index) {
		setLed(index, 0);
	}

	private void setLed(int index, int enabled) {
		byte ledRegister = (byte) ((index % 8) * 2 + 1);
		sendData(ledRegister, (byte) enabled);
	}

	public void clearDisplay() {
		for (byte idx = 0; idx < 16; idx++) {
			sendData(idx, (byte) 0x00);
		}
	}

	public void turnOff() {
		sendCommand((byte) 0x80);
	}

	/**
	 * Turn on the display and set the brightness The pulse width used is set to:
	 * <li>0 => 1/16
	 * <li>1 => 2/16
	 * <li>2 => 4/16
	 * <li>3 => 10/16
	 * <li>4 => 11/16
	 * <li>5 => 12/16
	 * <li>6 => 13/16
	 * <li>7 => 14/16
	 * 
	 * @param brightness between 0 and 7
	 */
	public void setBrightness(int brightness) {
		sendCommand((byte) (0x88 | (brightness & 7)));
	}

	private void sendCommand(byte cmd) {
		stb.low();
		sendByte(cmd);
		stb.high();
	}

	/**
	 * @param addr adress of the data
	 * @param data value of the data
	 */
	private void sendData(byte addr, byte data) {
		stb.low();
		setDataMode(WRITE_MODE, FIXED_ADDR);
		stb.high();
		stb.low();
		sendByte((byte) (0xC0 | addr));
		sendByte(data);
		stb.high();
	}

	/**
	 * reads data into buffer, reads as many bytes as the buffer size;
	 * 
	 * @param buffer
	 */
	private void readData(byte[] buffer) {
		stb.low();
		setDataMode(READ_MODE, INCR_ADDR);
		sleep10us();
		for (int idx = 0; idx < buffer.length; idx++) {
			buffer[idx] = getByte();
		}
	}

	private void sleep10us() {
		try {
			Thread.currentThread().sleep(0, 10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param wr_mode   READ_MODE (read the key scan) or WRITE_MODE (write data)
	 * @param addr_mode INCR_ADDR (automatic address increased) or FIXED_ADDR
	 */
	private void setDataMode(byte wr_mode, byte addr_mode) {
		sendByte((byte) (0x40 | wr_mode | addr_mode));
	}

	private void sendByte(byte data) {
		dio.setMode(PinMode.DIGITAL_OUTPUT);
		dio.setPullResistance(PinPullResistance.PULL_UP);
		for (int idx = 0; idx < 8; idx++) {
			clk.low();
			dio.setState((data & 1) == 1);
			data >>= 1;
			clk.high();
		}

	}

	private byte getByte() {
		dio.setMode(PinMode.DIGITAL_INPUT);
		dio.setPullResistance(PinPullResistance.PULL_UP); // ?? is this needed
		byte readedByte = 0;
		for (int idx = 0; idx < 8; idx++) {
			readedByte >>= 1;
			clk.low();
			if (dio.isHigh()) {
				readedByte |= 0x80;
			}
			clk.high();
		}
		return readedByte;
	}
}
