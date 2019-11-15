package com.systemmeltdown.robotlib.arduino;

import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fazecast.jSerialComm.SerialPort;

public class ArduinoUSBController implements Runnable {
	private static int READ_TIMEOUT = 250;

	private Thread m_thread;
	private String m_name;
	private SerialPort m_serialPort;
	private byte[] m_byteBuffer = new byte[1024];
	private StringBuffer m_stringBuffer = new StringBuffer();

	public ArduinoUSBController(String name, String ttyDevice) {
		m_name = name;
		m_serialPort = SerialPort.getCommPort(ttyDevice);
		m_serialPort.setComPortParameters(115200, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
		m_serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, READ_TIMEOUT, 0);
		System.out.println("Opening serial port '" + ttyDevice + "'");
		boolean success = m_serialPort.openPort();
		System.out.println("Success? " + success);
	}

	public void start() {
		if (m_thread != null) {
			stop();
		}
		m_thread = new Thread(this, m_name);
		m_thread.start();
	}

	public void stop() {
		m_thread = null;
	}

	@Override
	public void run() {
		while(m_thread != null) {
			String text = read();
			if (text != null) {
				m_stringBuffer.append(text);
				int lineBreakIndex = m_stringBuffer.indexOf("\n");
				if (lineBreakIndex >= 0) {
					String line = m_stringBuffer.substring(0, lineBreakIndex);
					System.out.println("+++line='" + line + "'");
					m_stringBuffer.delete(0, lineBreakIndex + 1);
					ObjectMapper objectMapper = new ObjectMapper();
					try {
						Map<String, Object> state = objectMapper.readValue(
							line,
							new TypeReference<Map<String, Object>>(){}
						);
						System.out.println("---- state ----");
						System.out.println(state);
						System.out.println("-------------");

						System.out.println("---- devices ----");
						System.out.println(state.get("devices"));
						System.out.println("-------------");
					} catch(Exception e) {
						e.printStackTrace();
					}

				}
			}
		}
	}

	public boolean hasMessages() {
		return m_serialPort.bytesAvailable() > 0;
	}

	public String read() {
		int byteCount = m_serialPort.readBytes(m_byteBuffer, 1024);
		if (byteCount == 0) {
			return null;
		}
		return new String(m_byteBuffer, 0, byteCount);
	}

	public void write(String message) {
		System.out.println("write: '" + message + "'");
		byte[] bytes = message.getBytes();
		m_serialPort.writeBytes(bytes, bytes.length);
	}
}
