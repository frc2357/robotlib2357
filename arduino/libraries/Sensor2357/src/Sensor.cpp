#include "Sensor.h"

void updateIntSensor(JsonElement &jsonElement, SensorFunc sensorFunc) {
  jsonElement["value"] = sensorFunc.intSensorFunc(jsonElement["min"].asInt(), jsonElement["max"].asInt());
}

void updateLongSensor(JsonElement &jsonElement, SensorFunc sensorFunc) {
  jsonElement["value"] = sensorFunc.longSensorFunc(jsonElement["min"].asLong(), jsonElement["max"].asLong());
}

void updateFloatSensor(JsonElement &jsonElement, SensorFunc sensorFunc) {
  jsonElement["value"] = sensorFunc.floatSensorFunc(jsonElement["min"].asFloat(), jsonElement["max"].asFloat());
}

void updateDoubleSensor(JsonElement &jsonElement, SensorFunc sensorFunc) {
  jsonElement["value"] = sensorFunc.doubleSensorFunc(jsonElement["min"].asDouble(), jsonElement["max"].asDouble());
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
