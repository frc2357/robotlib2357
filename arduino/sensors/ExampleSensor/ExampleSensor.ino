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
#include <Sensor2357.h>

#define LOOP_DELAY_MS            10

float floatValue = 6.0F;
unsigned long lastIncrement = millis();
unsigned long nextIncrement = lastIncrement + 1000;

void increment() {
  floatValue += 2.25F;
  if (floatValue > 40.0F) {
    floatValue = 0.0F;
  }
}

float readFloatValue(float min, float max) {
  if (millis() > nextIncrement) {
    increment();
    lastIncrement = millis();
    nextIncrement = lastIncrement + random(250, 5000);
  }
  return floatValue;
}

SensorDevice_Seeed_XIAO_RP2040<1> device("ExampleSensor");

void setup() {
  device.initSensor("floatValue", readFloatValue, 5.0F, 30.0F);
  device.begin();
}

void loop() {
  device.update();
  delay(LOOP_DELAY_MS);
}
