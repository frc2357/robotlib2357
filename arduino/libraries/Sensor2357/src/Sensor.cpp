#include "Sensor.h"

void updateFloatSensor(JsonElement &jsonElement, SensorFunc sensorFunc) {
  float value = sensorFunc.floatSensorFunc();
  jsonElement["value"] = value;
}

void Sensor::init(JsonElement *jsonElement, SensorFunc sensorFunc, updateSensor_T updateFunc) {
  m_jsonElement = jsonElement;
  m_sensorFunc = sensorFunc;
  m_updateFunc = updateFunc;
}

void Sensor::update() {
  m_updateFunc(*m_jsonElement, m_sensorFunc);
}
