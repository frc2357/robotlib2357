#include "Sensor.h"

SensorSettings::SensorSettings(Sensor &sensor) : m_sensor(sensor) {}

JsonElement &SensorSettings::operator[](const char *key) const {
  return m_sensor.getSetting(key);
}

Sensor::Sensor() : m_settings(*this) {
  m_jsonElement = NULL;
  m_sensorFunc.boolSensorFunc = NULL;
  m_updateFunc = NULL;
}

void Sensor::init(JsonElement &jsonElement, const char *name, intSensorFunc_T sensorFunc) {
  m_sensorFieldsJson[0] = Json::Int(SENSOR_FIELD_VALUE, 0);
  m_sensorFieldsJson[1] = Json::Boolean(SENSOR_FIELD_ACTIVE, true);
  jsonElement = Json::Object(name, m_sensorFieldsJson);
  m_jsonElement = &jsonElement;
  m_sensorFunc = sensorFunc;
  m_updateFunc = &Sensor::updateInt;
}

void Sensor::init(JsonElement &jsonElement, const char *name, intMinMaxSensorFunc_T sensorFunc, int min, int max) {
  m_sensorFieldsJson[0] = Json::Int(SENSOR_FIELD_VALUE, 0);
  m_sensorFieldsJson[1] = Json::Boolean(SENSOR_FIELD_ACTIVE, true);
  m_sensorFieldsJson[2] = Json::Int(SENSOR_SETTING_MIN, min);
  m_sensorFieldsJson[3] = Json::Int(SENSOR_SETTING_MAX, max);
  jsonElement = Json::Object(name, m_sensorFieldsJson);
  m_jsonElement = &jsonElement;
  m_sensorFunc = sensorFunc;
  m_updateFunc = &Sensor::updateIntMinMax;
}

void Sensor::init(JsonElement &jsonElement, const char *name, longSensorFunc_T sensorFunc) {
  m_sensorFieldsJson[0] = Json::Int(SENSOR_FIELD_VALUE, 0);
  m_sensorFieldsJson[1] = Json::Boolean(SENSOR_FIELD_ACTIVE, true);
  jsonElement = Json::Object(name, m_sensorFieldsJson);
  m_jsonElement = &jsonElement;
  m_sensorFunc = sensorFunc;
  m_updateFunc = &Sensor::updateLong;
}

void Sensor::init(JsonElement &jsonElement, const char *name, longMinMaxSensorFunc_T sensorFunc, long min, long max) {
  m_sensorFieldsJson[0] = Json::Int(SENSOR_FIELD_VALUE, 0);
  m_sensorFieldsJson[1] = Json::Boolean(SENSOR_FIELD_ACTIVE, true);
  m_sensorFieldsJson[2] = Json::Int(SENSOR_SETTING_MIN, min);
  m_sensorFieldsJson[3] = Json::Int(SENSOR_SETTING_MAX, max);
  jsonElement = Json::Object(name, m_sensorFieldsJson);
  m_jsonElement = &jsonElement;
  m_sensorFunc = sensorFunc;
  m_updateFunc = &Sensor::updateLongMinMax;
}

void Sensor::init(JsonElement &jsonElement, const char *name, floatSensorFunc_T sensorFunc) {
  m_sensorFieldsJson[0] = Json::Float(SENSOR_FIELD_VALUE, 0);
  m_sensorFieldsJson[1] = Json::Boolean(SENSOR_FIELD_ACTIVE, true);
  jsonElement = Json::Object(name, m_sensorFieldsJson);
  m_jsonElement = &jsonElement;
  m_sensorFunc = sensorFunc;
  m_updateFunc = &Sensor::updateFloat;
}

void Sensor::init(JsonElement &jsonElement, const char *name, floatMinMaxSensorFunc_T sensorFunc, float min, float max) {
  m_sensorFieldsJson[0] = Json::Float(SENSOR_FIELD_VALUE, 0);
  m_sensorFieldsJson[1] = Json::Boolean(SENSOR_FIELD_ACTIVE, true);
  m_sensorFieldsJson[2] = Json::Float(SENSOR_SETTING_MIN, min);
  m_sensorFieldsJson[3] = Json::Float(SENSOR_SETTING_MAX, max);
  jsonElement = Json::Object(name, m_sensorFieldsJson);
  m_jsonElement = &jsonElement;
  m_sensorFunc = sensorFunc;
  m_updateFunc = &Sensor::updateFloatMinMax;
}

void Sensor::init(JsonElement &jsonElement, const char *name, doubleSensorFunc_T sensorFunc) {
  m_sensorFieldsJson[0] = Json::Float(SENSOR_FIELD_VALUE, 0);
  m_sensorFieldsJson[1] = Json::Boolean(SENSOR_FIELD_ACTIVE, true);
  jsonElement = Json::Object(name, m_sensorFieldsJson);
  m_jsonElement = &jsonElement;
  m_sensorFunc = sensorFunc;
  m_updateFunc = &Sensor::updateDouble;
}

void Sensor::init(JsonElement &jsonElement, const char *name, doubleMinMaxSensorFunc_T sensorFunc, double min, double max) {
  m_sensorFieldsJson[0] = Json::Float(SENSOR_FIELD_VALUE, 0);
  m_sensorFieldsJson[1] = Json::Boolean(SENSOR_FIELD_ACTIVE, true);
  m_sensorFieldsJson[2] = Json::Float(SENSOR_SETTING_MIN, min);
  m_sensorFieldsJson[3] = Json::Float(SENSOR_SETTING_MAX, max);
  jsonElement = Json::Object(name, m_sensorFieldsJson);
  m_jsonElement = &jsonElement;
  m_sensorFunc = sensorFunc;
  m_updateFunc = &Sensor::updateDoubleMinMax;
}

void Sensor::init(JsonElement &jsonElement, const char *name, boolSensorFunc_T sensorFunc) {
  m_sensorFieldsJson[0] = Json::Boolean(SENSOR_FIELD_VALUE, false);
  m_sensorFieldsJson[1] = Json::Boolean(SENSOR_FIELD_ACTIVE, true);
  jsonElement = Json::Object(name, m_sensorFieldsJson);
  m_jsonElement = &jsonElement;
  m_sensorFunc = sensorFunc;
  m_updateFunc = &Sensor::updateBool;
}

void Sensor::init(JsonElement &jsonElement, const char *name, stringSensorFunc_T sensorFunc, size_t length) {
  m_sensorFieldsJson[0] = Json::String(SENSOR_FIELD_VALUE, "", length);
  m_sensorFieldsJson[1] = Json::Boolean(SENSOR_FIELD_ACTIVE, true);
  jsonElement = Json::Object(name, m_sensorFieldsJson);
  m_jsonElement = &jsonElement;
  m_sensorFunc = sensorFunc;
  m_updateFunc = &Sensor::updateString;
}

void Sensor::init(JsonElement &jsonElement, const char *name, intArraySensorFunc_T sensorFunc, size_t length) {
  JsonElement *arrayElements = new JsonElement[length];
  for (int i = 0; i < length; i++) {
    arrayElements[i] = Json::Int(0);
  }

  m_sensorFieldsJson[0] = Json::Array(SENSOR_FIELD_ARRAY, arrayElements, length);
  m_sensorFieldsJson[1] = Json::Boolean(SENSOR_FIELD_ACTIVE, true);
  m_sensorFieldsJson[2] = Json::Int(SENSOR_FIELD_LENGTH, length);
  jsonElement = Json::Object(name, m_sensorFieldsJson);
  m_jsonElement = &jsonElement;
  m_sensorFunc = sensorFunc;
  m_updateFunc = &Sensor::updateIntArray;
}

void Sensor::init(JsonElement &jsonElement, const char *name, longArraySensorFunc_T sensorFunc, size_t length) {
  JsonElement *arrayElements = new JsonElement[length];
  for (int i = 0; i < length; i++) {
    arrayElements[i] = Json::Int(0);
  }

  m_sensorFieldsJson[0] = Json::Array(SENSOR_FIELD_ARRAY, arrayElements, length);
  m_sensorFieldsJson[1] = Json::Boolean(SENSOR_FIELD_ACTIVE, true);
  m_sensorFieldsJson[2] = Json::Int(SENSOR_FIELD_LENGTH, length);
  jsonElement = Json::Object(name, m_sensorFieldsJson);
  m_jsonElement = &jsonElement;
  m_sensorFunc = sensorFunc;
  m_updateFunc = &Sensor::updateLongArray;
}

void Sensor::init(JsonElement &jsonElement, const char *name, floatArraySensorFunc_T sensorFunc, size_t length) {
  JsonElement *arrayElements = new JsonElement[length];
  for (int i = 0; i < length; i++) {
    arrayElements[i] = Json::Float(0.0);
  }

  m_sensorFieldsJson[0] = Json::Array(SENSOR_FIELD_ARRAY, arrayElements, length);
  m_sensorFieldsJson[1] = Json::Boolean(SENSOR_FIELD_ACTIVE, true);
  m_sensorFieldsJson[2] = Json::Int(SENSOR_FIELD_LENGTH, length);
  jsonElement = Json::Object(name, m_sensorFieldsJson);
  m_jsonElement = &jsonElement;
  m_sensorFunc = sensorFunc;
  m_updateFunc = &Sensor::updateFloatArray;
}

void Sensor::init(JsonElement &jsonElement, const char *name, doubleArraySensorFunc_T sensorFunc, size_t length) {
  JsonElement *arrayElements = new JsonElement[length];
  for (int i = 0; i < length; i++) {
    arrayElements[i] = Json::Float(0.0);
  }

  m_sensorFieldsJson[0] = Json::Array(SENSOR_FIELD_ARRAY, arrayElements, length);
  m_sensorFieldsJson[1] = Json::Boolean(SENSOR_FIELD_ACTIVE, true);
  m_sensorFieldsJson[2] = Json::Int(SENSOR_FIELD_LENGTH, length);
  jsonElement = Json::Object(name, m_sensorFieldsJson);
  m_jsonElement = &jsonElement;
  m_sensorFunc = sensorFunc;
  m_updateFunc = &Sensor::updateDoubleArray;
}

void Sensor::update() {
  m_updateFunc(*this);
}

JsonElement &Sensor::getSetting(const char *name) {
  if (strlen(name) == 0 || name[0] == '_') {
    // Either an empty name was requested or one that is a builtin field
    return JsonElement::NotFound;
  }
  return (*m_jsonElement)[name];
}

void Sensor::updateInt() {
  int value = m_sensorFunc.intSensorFunc(m_settings);
  (*m_jsonElement)[SENSOR_FIELD_VALUE] = value;

  if (!(*m_jsonElement)[SENSOR_FIELD_ACTIVE].asBoolean()) {
    (*m_jsonElement)[SENSOR_FIELD_VALUE].clearChanged();
  }
}

void Sensor::updateIntMinMax() {
  int min = (*m_jsonElement)[SENSOR_SETTING_MIN].asInt();
  int max = (*m_jsonElement)[SENSOR_SETTING_MAX].asInt();
  int value = m_sensorFunc.intMinMaxSensorFunc(min, max, m_settings);
  bool active = (value >= min && value <= max);

  (*m_jsonElement)[SENSOR_FIELD_ACTIVE] = active;
  (*m_jsonElement)[SENSOR_FIELD_VALUE] = value;
  if (!active) {
    (*m_jsonElement)[SENSOR_FIELD_VALUE].clearChanged();
  }
}

void Sensor::updateLong() {
  long value = m_sensorFunc.longSensorFunc(m_settings);
  (*m_jsonElement)[SENSOR_FIELD_VALUE] = value;

  if (!(*m_jsonElement)[SENSOR_FIELD_ACTIVE].asBoolean()) {
    (*m_jsonElement)[SENSOR_FIELD_VALUE].clearChanged();
  }
}

void Sensor::updateLongMinMax() {
  long min = (*m_jsonElement)[SENSOR_SETTING_MIN].asLong();
  long max = (*m_jsonElement)[SENSOR_SETTING_MAX].asLong();
  long value = m_sensorFunc.longMinMaxSensorFunc(min, max, m_settings);
  bool active = (value >= min && value <= max);

  (*m_jsonElement)[SENSOR_FIELD_ACTIVE] = active;
  (*m_jsonElement)[SENSOR_FIELD_VALUE] = value;
  if (!active) {
    (*m_jsonElement)[SENSOR_FIELD_VALUE].clearChanged();
  }
}

void Sensor::updateFloat() {
  float value = m_sensorFunc.floatSensorFunc(m_settings);
  (*m_jsonElement)[SENSOR_FIELD_VALUE] = value;

  if (!(*m_jsonElement)[SENSOR_FIELD_ACTIVE].asBoolean()) {
    (*m_jsonElement)[SENSOR_FIELD_VALUE].clearChanged();
  }
}

void Sensor::updateFloatMinMax() {
  float min = (*m_jsonElement)[SENSOR_SETTING_MIN].asFloat();
  float max = (*m_jsonElement)[SENSOR_SETTING_MAX].asFloat();
  float value = m_sensorFunc.floatMinMaxSensorFunc(min, max, m_settings);
  bool active = (value >= min && value <= max);

  (*m_jsonElement)[SENSOR_FIELD_ACTIVE] = active;
  (*m_jsonElement)[SENSOR_FIELD_VALUE] = value;
  if (!active) {
    (*m_jsonElement)[SENSOR_FIELD_VALUE].clearChanged();
  }
}

void Sensor::updateDouble() {
  double value = m_sensorFunc.doubleSensorFunc(m_settings);
  (*m_jsonElement)[SENSOR_FIELD_VALUE] = value;

  if (!(*m_jsonElement)[SENSOR_FIELD_ACTIVE].asBoolean()) {
    (*m_jsonElement)[SENSOR_FIELD_VALUE].clearChanged();
  }
}

void Sensor::updateDoubleMinMax() {
  double min = (*m_jsonElement)[SENSOR_SETTING_MIN].asDouble();
  double max = (*m_jsonElement)[SENSOR_SETTING_MAX].asDouble();
  double value = m_sensorFunc.doubleMinMaxSensorFunc(min, max, m_settings);
  bool active = (value >= min && value <= max);

  (*m_jsonElement)[SENSOR_FIELD_ACTIVE] = active;
  (*m_jsonElement)[SENSOR_FIELD_VALUE] = value;
  if (!active) {
    (*m_jsonElement)[SENSOR_FIELD_VALUE].clearChanged();
  }
}

void Sensor::updateBool() {
  (*m_jsonElement)[SENSOR_FIELD_VALUE] = m_sensorFunc.boolSensorFunc(m_settings);
}

void Sensor::updateString() {
  (*m_jsonElement)[SENSOR_FIELD_VALUE] = m_sensorFunc.stringSensorFunc(m_settings);
}

void Sensor::updateIntArray() {
  size_t length = (*m_jsonElement)[SENSOR_FIELD_LENGTH].asInt();
  for (int index = 0; index < length; index++) {
    (*m_jsonElement)[SENSOR_FIELD_ARRAY][index] = m_sensorFunc.intArraySensorFunc(index, m_settings);
  }
}

void Sensor::updateLongArray() {
  size_t length = (*m_jsonElement)[SENSOR_FIELD_LENGTH].asInt();
  for (int index = 0; index < length; index++) {
    (*m_jsonElement)[SENSOR_FIELD_ARRAY][index] = m_sensorFunc.longArraySensorFunc(index, m_settings);
  }
}

void Sensor::updateFloatArray() {
  size_t length = (*m_jsonElement)[SENSOR_FIELD_LENGTH].asInt();
  for (int index = 0; index < length; index++) {
    (*m_jsonElement)[SENSOR_FIELD_ARRAY][index] = m_sensorFunc.floatArraySensorFunc(index, m_settings);
  }
}

void Sensor::updateDoubleArray() {
  size_t length = (*m_jsonElement)[SENSOR_FIELD_LENGTH].asInt();
  for (int index = 0; index < length; index++) {
    (*m_jsonElement)[SENSOR_FIELD_ARRAY][index] = m_sensorFunc.doubleArraySensorFunc(index, m_settings);
  }
}
