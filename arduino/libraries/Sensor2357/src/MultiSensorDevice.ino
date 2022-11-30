/*
 * Multi-Sensor Device
 */
#include <JsonState.h>
#include "Sensor2357.h"

//SensorValue value;

//Sensor distanceSensor("distanceMM", 0.0);
//distanceSensor.addSetting("minRangeMM", 20.0);
//distanceSensor.addSetting("maxRangeMM", 200.0);

//Sensor temperatureSensor("temperatureC", 0.0);

/*
#define TEMPERATURE_CURVE_DATAPOINTS   100
Value temperatureCurveArray[TEMPERATURE_CURVE_DATAPOINTS];
for (int i = 0; i < TEMPERATURE_CURVE_DATAPOINTS; i++) {
  temperatureCurve[i] = 0;
}
Sensor temperatureCurveSensor("temperatureCurve", temperatureCurveArray);
*/

/*
Sensor sensors[] = { distanceSensor, temperatureSensor, temperatureCurveSensor };
SensorDevice device(sensors);
device.setMaxUpdateMs(100);
*/

JsonElement distanceSettingsFields[] = {
  Json::Float("min", 0.0),
  Json::Float("max", 200.0),
};

JsonElement distanceSensorFields[] = {
  Json::Object("settings", distanceSettingsFields),
  Json::Float("value", 0.0),
};

JsonElement temperatureSettingsFields[] = {
  Json::Float("min", 10.0),
  Json::Float("max", 30.0),
};

JsonElement temperatureSensorFields[] = {
  Json::Object("settings", temperatureSettingsFields),
  Json::Float("value", 0.0),
};

JsonElement sensors[] = {
  Json::Object("dist", distanceSensorFields),
  Json::Object("temp", temperatureSensorFields),
};

SensorDevice_Seeed_XIAO_RP2040 device("MultiSensorDevice", sensors);

void setup() {
  device.begin();
}

void loop() {
  device.update();

  /*
  incrementDistance();
  updateTemperature();

  // Send value updates and receive any settings changes
  device.update();
  */
  //delay(1000);

}

// Increment distance value by 0.1 until we hit max and then start over at min.
/*
void incrementDistance() {
  Value max = distanceSensor.getSetting("maxRangeMM");
  Value distance = distanceSensor.getValue();

  if (distance < max) {
    distance = distance + 0.1;
  } else {
    Value min = distanceSensor.getSetting("minRangeMM");
    distanceSensor.setValue(min);
  }
}
*/

// Update the temperature curve and current temperature with a random value
/*
void updateTemperature() {
  double newValue = random(10, 30);

  temperatureSensor.getValue() = newValue;

  // Shift all the existing values back one
  Value curve = temperatureCurveSensor.getValue();
  for (int i = TEMPERATURE_CURVE_DATAPOINTS - 1; i > 0; i--) {
    curve[i] = curve(i - 1);
  }
  // Update index 0 with the new value
  curve[0] = newValue;
}
*/
