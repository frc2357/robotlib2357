package com.systemmeltdown.robotlib.sensors;

import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.SerialPort.Port;

public class VL53LOXSensorOutput {
    private SerialPort m_serialPort;

    public VL53LOXSensorOutput(int baudRate, Port port) {
        m_serialPort = new SerialPort(115200, port);
    }

    public String read() {
        int count = m_serialPort.getBytesReceived();

        return m_serialPort.readString(count);
    }
}