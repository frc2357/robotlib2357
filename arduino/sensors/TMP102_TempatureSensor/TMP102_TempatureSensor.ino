#include<Wire.h>
#include <Sensor2357.h>
#include <SparkFunTMP102.h>
#define ALERT_PIN A3
TMP102 tempSensor;

#define LOOP_DELAY_MS 10

float readFloatValue() {
  tempSensor.wakeup();
  if (tempSensor.begin() == false)
  { 
    return 0.0F;
  }else{
    return tempSensor.readTempF();
  }
}

SensorDevice_Seeed_XIAO_RP2040<1> device("TempatureSensor");

void setup() {
  Wire.begin(); //Join I2C Bus
  
  pinMode(ALERT_PIN,INPUT);
  if(!tempSensor.begin())
  {
    Serial.println("Cannot connect to TMP102.");
    Serial.println("Is the board connected? Is the device ID correct?");
    while(1);
  }
  Serial.println("Connected to TMP102!");
  delay(100);
  tempSensor.setFault(0);
  tempSensor.setAlertPolarity(1);
  tempSensor.setAlertMode(0);
  tempSensor.setConversionRate(2);
  tempSensor.setExtendedMode(0);
  tempSensor.setHighTempF(85.0);
  tempSensor.setLowTempF(84.0);
  device.initSensor("floatValue", readFloatValue);
  device.begin();
}

void loop() {
  device.update(); 
  delay(LOOP_DELAY_MS);
}
