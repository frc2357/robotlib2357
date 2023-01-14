const int ALERT_PIN = A3;
#include<Wire.h>
#include <Sensor2357.h>
#include <SparkFunTMP102.h>
TMP102 sensor0;

#define LOOP_DELAY_MS 10

float readFloatValue() {
  sensor0.wakeup();
  if (sensor0.begin() == false)
  {
    return 0.0F;
  }else{
    return sensor0.readTempF();
  }
}

SensorDevice_Seeed_XIAO_RP2040<1> device("TempatureSensor");

void setup() {
  Wire.begin(); //Join I2C Bus
  
  pinMode(ALERT_PIN,INPUT);
  if(!sensor0.begin())
  {
    Serial.println("Cannot connect to TMP102.");
    Serial.println("Is the board connected? Is the device ID correct?");
    while(1);
  }
  Serial.println("Connected to TMP102!");
  delay(100);
  sensor0.setFault(0);
  sensor0.setAlertPolarity(1);
  sensor0.setAlertMode(0);
  sensor0.setConversionRate(2);
  sensor0.setExtendedMode(0);
  sensor0.setHighTempF(85.0);
  sensor0.setLowTempF(84.0);
  device.initSensor("floatValue", readFloatValue);
  device.begin();
}

void loop() {
  device.update(); 
  delay(LOOP_DELAY_MS);
}
