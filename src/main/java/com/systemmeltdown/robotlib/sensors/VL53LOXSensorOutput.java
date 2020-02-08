package com.systemmeltdown.robotlib.sensors;

import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.SerialPort.Port;

import java.io.FileReader;

import org.json.simple.JSONObject;

public class VL53LOXSensorOutput {
    private SerialPort m_serialPort;

    public VL53LOXSensorOutput(int baudRate, Port port) {
        m_serialPort = new SerialPort(115200, port);
    }

    public String getJSONString() {
        int count = m_serialPort.getBytesReceived();
        
        return m_serialPort.readString(count);
    }

    public int getInt(String value) {
        int count = m_serialPort.getBytesReceived();
        JSONObject sensorInfo = (JSONObject) new JSONParser()
            .parse(new FileReader(m_serialPort.readString(count));

        return sensorInfo.get(value);
    }
}