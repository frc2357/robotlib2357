package com.systemmeltdown.robotlib.arduino;

import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fazecast.jSerialComm.SerialPort;

public class ArduinoUSBController implements Runnable {
	private static String NAME_DISCONNECTED = "(disconnected)";
	private static String NAME_UNNAMED = "(unnamed)";
	private static int READ_TIMEOUT = 250;

	private Thread m_thread;
	private String m_name = NAME_DISCONNECTED;
	private JsonNode m_state;
	private SerialPort m_serialPort;
	private byte[] m_byteBuffer = new byte[1024];
	private StringBuffer m_stringBuffer = new StringBuffer();

	public ArduinoUSBController(String ttyDevice) {
		m_serialPort = SerialPort.getCommPort(ttyDevice);
		m_serialPort.setComPortParameters(115200, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
		m_serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, READ_TIMEOUT, 0);
	}

	public String getName() {
		return m_name;
	}

	public boolean isConnected() {
		return m_state != null;
	}

	public boolean hasDevice(String deviceName) {
		return isConnected() && getDevice(deviceName) != null;
	}

	public String getDeviceFieldString(String deviceName, String fieldName) {
		JsonNode field = getDeviceField(deviceName, fieldName);
		return field != null ? field.asText() : null;
	}

	public int getDeviceFieldInt(String deviceName, String fieldName) {
		JsonNode field = getDeviceField(deviceName, fieldName);
		return field != null ? field.asInt() : Integer.MIN_VALUE;
	}

	public double getDeviceFieldDouble(String deviceName, String fieldName) {
		JsonNode field = getDeviceField(deviceName, fieldName);
		return field != null ? field.asDouble() : Double.NaN;
	}

	public boolean getDeviceFieldBoolean(String deviceName, String fieldName) {
		JsonNode field = getDeviceField(deviceName, fieldName);
		return field != null ? field.asBoolean() : false;
	}

	public void start() {
		if (m_thread != null) {
			stop();
		}

		System.out.println("Opening serial port '" + m_serialPort.getSystemPortName() + "'");
		boolean success = m_serialPort.openPort();
		if (!success) {
			System.err.println("Error opening serial port '" + m_serialPort.getSystemPortName() + "'");
			return;
		}

		// It all checks out, start the thread for blocking reads.
		String threadName = "ArduinoUSB[" + m_serialPort.getSystemPortName() + "]";
		m_thread = new Thread(this, threadName);
		m_thread.start();
	}

	public void stop() {
		m_thread = null;
		m_name = NAME_DISCONNECTED;
		m_state = null;
	}

	@Override
	public void run() {
		while(m_thread != null) {
			try {
				read();
			} catch(IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}

	protected void read() throws IOException {
		int byteCount = m_serialPort.readBytes(m_byteBuffer, 1024);
		if (byteCount == 0) {
			return;
		}

		String newChars = new String(m_byteBuffer, 0, byteCount);
		if (newChars != null) {
			m_stringBuffer.append(newChars);
			int lineBreakIndex = m_stringBuffer.indexOf("\n");
			if (lineBreakIndex >= 0) {
				String line = m_stringBuffer.substring(0, lineBreakIndex);
				m_stringBuffer.delete(0, lineBreakIndex + 1);
				ObjectMapper objectMapper = new ObjectMapper();
				setState(objectMapper.readTree(line));
			}
		}
	}

	protected void setState(JsonNode state) {
		m_state = state;

		JsonNode name = state.get("name");
		m_name = name != null ? name.asText() : NAME_UNNAMED;
	}

	public void write(String message) {
		System.out.println("write: '" + message + "'");
		byte[] bytes = message.getBytes();
		m_serialPort.writeBytes(bytes, bytes.length);
	}

	protected JsonNode getDeviceField(String deviceName, String fieldName) {
		JsonNode device = getDevice(deviceName);
		if (device == null) {
			System.err.println("device '" + deviceName + "' not found for Arduino " + m_name + " on " + m_serialPort.getSystemPortName());
			return null;
		}
		JsonNode field = device.get(fieldName);
		if (field == null) {
			System.err.println("devices '" + deviceName + "' field '" + fieldName + "' not found for Arduino " + m_name + " on " + m_serialPort.getSystemPortName());
			return null;
		}
		return field;
	}

	protected JsonNode getDevice(String deviceName) {
		if (m_state == null) {
			System.err.println("state not yet available for Arduino on " + m_serialPort.getSystemPortName() );
			return null;
		}
		JsonNode devices = m_state.get("devices");
		if (devices == null) {
			System.err.println("devices not found for Arduino " + m_name + " on " + m_serialPort.getSystemPortName());
			return null;
		}
		return devices.get(deviceName);
	}
}
