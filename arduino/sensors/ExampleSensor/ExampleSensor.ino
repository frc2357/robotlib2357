/*
 * Example Sensor Device
 *
 * This example shows how a floating point value works with minimum and maximum values.
 * When the value returned from the callback function is outside the range of min/max values, 
 * the result is not sent up to the RoboRIO. It can be queried, but it is not automatically sent.
 * 
 * To try it out:
 * 1. Install this code on a supported device (Seeed XIAO RP2040 from Seeed Studios)
 * 2. Open the USB serial port in a serial terminal program (115200 bps)
 * 3. Send {}
 */
#include<Wire.h>
#include <Sensor2357.h>
#include "SparkFun_VCNL4040_Arduino_Library.h"
VCNL4040 proximitySensor;

#define LOOP_DELAY_MS            10

int readFloatValue(int min, int max) {
  if (proximitySensor.begin() == false)
  {
    return 0;
  }else{
    return proximitySensor.getProximity();
  }
}

SensorDevice_Seeed_XIAO_RP2040<1> device("ExampleSensor");

void setup() {
  device.initSensor("intValue", readFloatValue, 0, 65535);
  device.begin();
}

void loop() {
  device.update();
  delay(LOOP_DELAY_MS);
}
