/*
 * Multi-Sensor Device
 */
#include "Sensor2357.h"

#define LOOP_DELAY_MS            10

float readDistance() {
  return 0.0F;
}

SensorDevice_Seeed_XIAO_RP2040<1> device("MultiSensorDevice");
//device.addSensor("distanceMM", readDistance, 5.0F, 30.0F);
//device.setMaxUpdateMs(100);

void setup() {
  device.begin();
}

void loop() {
  device.update();
  delay(LOOP_DELAY_MS);
}
