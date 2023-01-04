#include<Wire.h>
#include <Sensor2357.h>
#include "SparkFun_VCNL4040_Arduino_Library.h"
VCNL4040 proximitySensor;

#define LOOP_DELAY_MS 10

int readFloatValue(int min, int max) {
  if (proximitySensor.begin() == false)
  {
    return 0;
  }else{
    return proximitySensor.getProximity();
  }
}

SensorDevice_Seeed_XIAO_RP2040<1> device("ProximitySensor");

void setup() {
  device.initSensor("intValue", readFloatValue, 0, 65535);
  device.begin();
}

void loop() {
  device.update();
  delay(LOOP_DELAY_MS);
}