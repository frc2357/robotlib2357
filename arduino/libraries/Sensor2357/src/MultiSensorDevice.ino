/*
 * Multi-Sensor Device
 */
#include "Sensor2357.h"

#define LOOP_DELAY_MS            10

float distance = 6.0F;
unsigned long lastIncrement = millis();
unsigned long nextIncrement = lastIncrement + 1000;

void increment() {
  distance += 2.25F;
  if (distance > 30.0F) {
    distance = 5.0F;
  }
}

float readDistance(float min, float max) {
  if (millis() > nextIncrement) {
    increment();
    lastIncrement = millis();
    nextIncrement = lastIncrement + random(250, 5000);
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
