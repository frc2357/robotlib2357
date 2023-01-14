#include <Sensor2357.h>
#include "Adafruit_VL53L0X.h"
Adafruit_VL53L0X lox = Adafruit_VL53L0X();

#define LOOP_DELAY_MS 10

int readFloatValue(int min, int max) {
  if (!lox.begin())
  {
    return -1;
  }else{
    VL53L0X_RangingMeasurementData_t measure;
    lox.rangingTest(&measure, false);
     if (measure.RangeStatus != 4){return measure.RangeMilliMeter;}
     else {return 0;}  
  }
}

SensorDevice_Seeed_XIAO_RP2040<1> device("LaserLightSensor");

void setup() {
  device.initSensor("intValue", readFloatValue, 0, 65535);
  device.begin();
}

void loop() {
  device.update(); 
  delay(LOOP_DELAY_MS);
}
