#include<Wire.h>
#include <Sensor2357.h>
#include "SparkFun_VCNL4040_Arduino_Library.h"
VCNL4040 proximitySensor;

#define LOOP_DELAY_MS 10
int sensorPin = A1;
int highPin = A2;
int sensorValue = 0;

bool readFloatValue() {
  int value = analogRead(sensorPin);
  if (value < 110) //The sensor will output a random number betweel 90 and 110 when its too close. Once its far enough away from an object thats how we know we are falling off
  {
    return false;
  }else{
    return true;
  }
}

SensorDevice_Seeed_XIAO_RP2040<1> device("ProximitySensor");

void setup() {
  pinMode(highPin, OUTPUT);
  digitalWrite(highPin, HIGH);
  device.initSensor("boolValue", readFloatValue);
  device.begin();
}

void loop() {
  device.update(); 
  delay(LOOP_DELAY_MS);
}
