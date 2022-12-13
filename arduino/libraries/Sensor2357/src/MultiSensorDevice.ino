/*
 * Multi-Sensor Device
 */
#include "Sensor2357.h"

#define LOOP_DELAY_MS            10

float distance = 6.0F;

float readDistance() {
  distance += 2.25F;
  if (distance > 30.0F) {
    distance = 5.0F;
  }
  return distance;
}

SensorDevice_Seeed_XIAO_RP2040<1> device("MultiSensorDevice");

void setup() {
  device.initSensor("distanceMM", readDistance, 5.0F, 30.0F);
  device.begin();
}

void loop() {
  device.update();
  delay(LOOP_DELAY_MS);
}
