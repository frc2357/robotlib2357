#include "Sensor.h"

void updateIntSensor(JsonElement &jsonElement, SensorFunc sensorFunc) {
  int min = jsonElement["min"].asInt();
  int max = jsonElement["max"].asInt();
  int value = sensorFunc.intSensorFunc(min, max);
  bool active = (value >= min && value <= max);

  jsonElement["active"] = active;
  jsonElement["value"] = value;
  if (!active) {
    jsonElement["value"].clearChanged();
  }
}

void updateLongSensor(JsonElement &jsonElement, SensorFunc sensorFunc) {
  long min = jsonElement["min"].asLong();
  long max = jsonElement["max"].asLong();
  long value = sensorFunc.longSensorFunc(min, max);
  bool active = (value >= min && value <= max);

  jsonElement["active"] = active;
  jsonElement["value"] = value;
  if (!active) {
    jsonElement["value"].clearChanged();
  }
}

void updateFloatSensor(JsonElement &jsonElement, SensorFunc sensorFunc) {
  float min = jsonElement["min"].asFloat();
  float max = jsonElement["max"].asFloat();
  float value = sensorFunc.floatSensorFunc(min, max);
  bool active = (value >= min && value <= max);

  jsonElement["active"] = active;
  jsonElement["value"] = value;
  if (!active) {
    jsonElement["value"].clearChanged();
  }
}

void updateDoubleSensor(JsonElement &jsonElement, SensorFunc sensorFunc) {
  double min = jsonElement["min"].asDouble();
  double max = jsonElement["max"].asDouble();
  double value = sensorFunc.doubleSensorFunc(min, max);
  bool active = (value >= min && value <= max);

  jsonElement["active"] = active;
  jsonElement["value"] = value;
  if (!active) {
    jsonElement["value"].clearChanged();
  }
}

void updateBoolSensor(JsonElement &jsonElement, SensorFunc sensorFunc) {
  jsonElement["value"] = sensorFunc.boolSensorFunc();
}

void updateStringSensor(JsonElement &jsonElement, SensorFunc sensorFunc) {
  jsonElement["value"] = sensorFunc.stringSensorFunc();
}

void updateIntArraySensor(JsonElement &jsonElement, SensorFunc sensorFunc) {
  size_t length = jsonElement["length"].asInt();
  for (int index = 0; index < length; index++) {
    jsonElement["array"][index] = sensorFunc.intArraySensorFunc(index);
  }
}

void updateLongArraySensor(JsonElement &jsonElement, SensorFunc sensorFunc) {
  size_t length = jsonElement["length"].asInt();
  for (int index = 0; index < length; index++) {
    jsonElement["array"][index] = sensorFunc.longArraySensorFunc(index);
  }
}

void updateFloatArraySensor(JsonElement &jsonElement, SensorFunc sensorFunc) {
  size_t length = jsonElement["length"].asInt();
  for (int index = 0; index < length; index++) {
    jsonElement["array"][index] = sensorFunc.floatArraySensorFunc(index);
  }
}

void updateDoubleArraySensor(JsonElement &jsonElement, SensorFunc sensorFunc) {
  size_t length = jsonElement["length"].asInt();
  for (int index = 0; index < length; index++) {
    jsonElement["array"][index] = sensorFunc.doubleArraySensorFunc(index);
  }
}

void Sensor::init(JsonElement *jsonElement, SensorFunc sensorFunc, updateSensor_T updateFunc) {
  m_jsonElement = jsonElement;
  m_sensorFunc = sensorFunc;
  m_updateFunc = updateFunc;
  update();
}

void Sensor::update() {
  m_updateFunc(*m_jsonElement, m_sensorFunc);
}
