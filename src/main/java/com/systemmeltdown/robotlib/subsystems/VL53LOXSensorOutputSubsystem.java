package com.systemmeltdown.robotlib.subsystems;

import com.systemmeltdown.robotlib.arduino.ArduinoUSBController;

public class VL53LOXSensorOutputSubsystem extends ClosedLoopSubsystem {
	//private StringLogTopic errorLog = new StringLogTopic("arduino-error");

	private ArduinoUSBController m_arduinoUSB;

	public VL53LOXSensorOutputSubsystem(String ttyDevice) {
		m_arduinoUSB = new ArduinoUSBController(ttyDevice);

		m_arduinoUSB.start();
	}

	/**
	 * Get the count from the Arduino
	 * 
	 * This is an example of how a subsystem could expose sensor values
	 * from an arduino. No caching of state needed, state is already
	 * cached in the ArduinoUSBController.
	 */
	public int getCount() {
		// Check if the arduino is connected before getting values.
		if (!m_arduinoUSB.isConnected()) {
			return -1;
		}
		return m_arduinoUSB.getDeviceFieldInt("intakeCells", "cells");
	}
} 