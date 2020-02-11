package com.systemmeltdown.robotlib.sensors;

import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.SerialPort.Port;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class VL53LOXSensorOutput {
    private SerialPort m_serialPort;

    public VL53LOXSensorOutput(int baudRate, Port port) {
        m_serialPort = new SerialPort(115200, port);
    }

    public String getJSONString() {
        int count = m_serialPort.getBytesReceived();

        return m_serialPort.readString(count);
    }

    public void getInt(String value) throws FileNotFoundException, IOException, ParseException {
        int count = m_serialPort.getBytesReceived();
        String info = m_serialPort.readString(count);
        System.out.println(info);
        Object obj = new JSONParser()
            .parse(info);
        JSONObject sensorInfo = (JSONObject) obj;

        Map address = ((Map)sensorInfo.get(value));

        //System.out.println(sensorInfo.isEmpty());

        Iterator<Map.Entry> itr1 = address.entrySet().iterator(); 
        while (itr1.hasNext()) { 
            Map.Entry pair = itr1.next(); 
            System.out.println(pair.getKey() + " : " + pair.getValue()); 
        } 

        count = (int) sensorInfo.get(value);
        //return count;
    }
}
//Map address = ((Map)jo.get("address")); 