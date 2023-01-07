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

Sensor::~Sensor() {}

void Sensor::init(JsonElement &jsonElement, const char *name, intSensorFunc_T sensorFunc) {
  initInt(jsonElement, name, sensorFunc, SENSOR_MIN_MAX_NONE, SENSOR_MIN_MAX_NONE, false);
}

void Sensor::init(JsonElement &jsonElement, const char *name, intSettingsSensorFunc_T sensorFunc) {
  initInt(jsonElement, name, sensorFunc, SENSOR_MIN_MAX_NONE, SENSOR_MIN_MAX_NONE, true);
}

void Sensor::init(JsonElement &jsonElement, const char *name, intMinMaxSensorFunc_T sensorFunc, int min, int max) {
  initInt(jsonElement, name, sensorFunc, min, max, false);
}

void Sensor::init(JsonElement &jsonElement, const char *name, intMinMaxSettingsSensorFunc_T sensorFunc, int min, int max) {
  initInt(jsonElement, name, sensorFunc, min, max, true);
}

void Sensor::init(JsonElement &jsonElement, const char *name, longSensorFunc_T sensorFunc) {
  initLong(jsonElement, name, sensorFunc, SENSOR_MIN_MAX_NONE, SENSOR_MIN_MAX_NONE, false);
}

void Sensor::init(JsonElement &jsonElement, const char *name, longSettingsSensorFunc_T sensorFunc) {
  initLong(jsonElement, name, sensorFunc, SENSOR_MIN_MAX_NONE, SENSOR_MIN_MAX_NONE, true);
}

void Sensor::init(JsonElement &jsonElement, const char *name, longMinMaxSensorFunc_T sensorFunc, long min, long max) {
  initLong(jsonElement, name, sensorFunc, min, max, false);
}

void Sensor::init(JsonElement &jsonElement, const char *name, longMinMaxSettingsSensorFunc_T sensorFunc, long min, long max) {
  initLong(jsonElement, name, sensorFunc, min, max, true);
}

void Sensor::init(JsonElement &jsonElement, const char *name, floatSensorFunc_T sensorFunc) {
  initFloat(jsonElement, name, sensorFunc, SENSOR_MIN_MAX_NONE, SENSOR_MIN_MAX_NONE, false);
}

void Sensor::init(JsonElement &jsonElement, const char *name, floatSettingsSensorFunc_T sensorFunc) {
  initFloat(jsonElement, name, sensorFunc, SENSOR_MIN_MAX_NONE, SENSOR_MIN_MAX_NONE, true);
}

void Sensor::init(JsonElement &jsonElement, const char *name, floatMinMaxSensorFunc_T sensorFunc, float min, float max) {
  initFloat(jsonElement, name, sensorFunc, min, max, false);
}

void Sensor::init(JsonElement &jsonElement, const char *name, floatMinMaxSettingsSensorFunc_T sensorFunc, float min, float max) {
  initFloat(jsonElement, name, sensorFunc, min, max, true);
}

void Sensor::init(JsonElement &jsonElement, const char *name, doubleSensorFunc_T sensorFunc) {
  initDouble(jsonElement, name, sensorFunc, SENSOR_MIN_MAX_NONE, SENSOR_MIN_MAX_NONE, false);
}

void Sensor::init(JsonElement &jsonElement, const char *name, doubleSettingsSensorFunc_T sensorFunc) {
  initDouble(jsonElement, name, sensorFunc, SENSOR_MIN_MAX_NONE, SENSOR_MIN_MAX_NONE, true);
}

void Sensor::init(JsonElement &jsonElement, const char *name, doubleMinMaxSensorFunc_T sensorFunc, double min, double max) {
  initDouble(jsonElement, name, sensorFunc, min, max, false);
}

void Sensor::init(JsonElement &jsonElement, const char *name, doubleMinMaxSettingsSensorFunc_T sensorFunc, double min, double max) {
  initDouble(jsonElement, name, sensorFunc, min, max, true);
}

void Sensor::init(JsonElement &jsonElement, const char *name, boolSensorFunc_T sensorFunc) {
  initBool(jsonElement, name, sensorFunc, false);
}

void Sensor::init(JsonElement &jsonElement, const char *name, boolSettingsSensorFunc_T sensorFunc) {
  initBool(jsonElement, name, sensorFunc, true);
}

void Sensor::init(JsonElement &jsonElement, const char *name, stringSensorFunc_T sensorFunc, size_t length) {
  initString(jsonElement, name, sensorFunc, length, false);
}

void Sensor::init(JsonElement &jsonElement, const char *name, stringSettingsSensorFunc_T sensorFunc, size_t length) {
  initString(jsonElement, name, sensorFunc, length, true);
}

void Sensor::init(JsonElement &jsonElement, const char *name, intArraySensorFunc_T sensorFunc, size_t length) {
  initIntArray(jsonElement, name, sensorFunc, length, false);
}

void Sensor::init(JsonElement &jsonElement, const char *name, intArraySettingsSensorFunc_T sensorFunc, size_t length) {
  initIntArray(jsonElement, name, sensorFunc, length, true);
}

void Sensor::init(JsonElement &jsonElement, const char *name, longArraySensorFunc_T sensorFunc, size_t length) {
  initLongArray(jsonElement, name, sensorFunc, length, false);
}

void Sensor::init(JsonElement &jsonElement, const char *name, longArraySettingsSensorFunc_T sensorFunc, size_t length) {
  initLongArray(jsonElement, name, sensorFunc, length, true);
}

void Sensor::init(JsonElement &jsonElement, const char *name, floatArraySensorFunc_T sensorFunc, size_t length) {
  initFloatArray(jsonElement, name, sensorFunc, length, false);
}

void Sensor::init(JsonElement &jsonElement, const char *name, floatArraySettingsSensorFunc_T sensorFunc, size_t length) {
  initFloatArray(jsonElement, name, sensorFunc, length, true);
}

void Sensor::init(JsonElement &jsonElement, const char *name, doubleArraySensorFunc_T sensorFunc, size_t length) {
  initDoubleArray(jsonElement, name, sensorFunc, length, false);
}

void Sensor::init(JsonElement &jsonElement, const char *name, doubleArraySettingsSensorFunc_T sensorFunc, size_t length) {
  initDoubleArray(jsonElement, name, sensorFunc, length, true);
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

long Sensor::getValueInt() {
  return (*m_jsonElement)[SENSOR_FIELD_VALUE].asLong();
}

double Sensor::getValueFloat() {
  return (*m_jsonElement)[SENSOR_FIELD_VALUE].asDouble();
}

long Sensor::getMinInt() {
  return (*m_jsonElement)[SENSOR_SETTING_MIN].asLong();
}

long Sensor::getMaxInt() {
  return (*m_jsonElement)[SENSOR_SETTING_MAX].asLong();
}

double Sensor::getMinFloat() {
  return (*m_jsonElement)[SENSOR_SETTING_MIN].asDouble();
}

double Sensor::getMaxFloat() {
  return (*m_jsonElement)[SENSOR_SETTING_MAX].asDouble();
}

void Sensor::initInt(JsonElement &jsonElement, const char *name, SensorFunc sensorFunc, int min, int max, bool settings) {
  m_sensorFieldsJson[0] = Json::Int(SENSOR_FIELD_VALUE, 0);
  m_sensorFieldsJson[1] = Json::Boolean(SENSOR_FIELD_ACTIVE, true);
  if (max == SENSOR_MIN_MAX_NONE) {
    m_updateFunc = (settings ? &Sensor::updateIntSettings : &Sensor::updateInt);
  } else {
    m_sensorFieldsJson[2] = Json::Int(SENSOR_SETTING_MIN, min);
    m_sensorFieldsJson[3] = Json::Int(SENSOR_SETTING_MAX, max);
    m_updateFunc = (settings ? &Sensor::updateIntMinMaxSettings : &Sensor::updateIntMinMax);
  }
  jsonElement = Json::Object(name, m_sensorFieldsJson);
  m_jsonElement = &jsonElement;
  m_sensorFunc = sensorFunc;
}

void Sensor::initLong(JsonElement &jsonElement, const char *name, SensorFunc sensorFunc, long min, long max, bool settings) {
  m_sensorFieldsJson[0] = Json::Int(SENSOR_FIELD_VALUE, 0);
  m_sensorFieldsJson[1] = Json::Boolean(SENSOR_FIELD_ACTIVE, true);
  if (max == SENSOR_MIN_MAX_NONE) {
    m_updateFunc = (settings ? &Sensor::updateLongSettings : &Sensor::updateLong);
  } else {
    m_sensorFieldsJson[2] = Json::Int(SENSOR_SETTING_MIN, min);
    m_sensorFieldsJson[3] = Json::Int(SENSOR_SETTING_MAX, max);
    m_updateFunc = (settings ? &Sensor::updateLongMinMaxSettings : &Sensor::updateLongMinMax);
  }
  jsonElement = Json::Object(name, m_sensorFieldsJson);
  m_jsonElement = &jsonElement;
  m_sensorFunc = sensorFunc;
}

void Sensor::initFloat(JsonElement &jsonElement, const char *name, SensorFunc sensorFunc, float min, float max, bool settings) {
  m_sensorFieldsJson[0] = Json::Float(SENSOR_FIELD_VALUE, 0);
  m_sensorFieldsJson[1] = Json::Boolean(SENSOR_FIELD_ACTIVE, true);
  if (max == SENSOR_MIN_MAX_NONE) {
    m_updateFunc = (settings ? &Sensor::updateFloatSettings : &Sensor::updateFloat);
  } else {
    m_sensorFieldsJson[2] = Json::Float(SENSOR_SETTING_MIN, min);
    m_sensorFieldsJson[3] = Json::Float(SENSOR_SETTING_MAX, max);
    m_updateFunc = (settings ? &Sensor::updateFloatMinMaxSettings : &Sensor::updateFloatMinMax);
  }
  jsonElement = Json::Object(name, m_sensorFieldsJson);
  m_jsonElement = &jsonElement;
  m_sensorFunc = sensorFunc;
}

void Sensor::initDouble(JsonElement &jsonElement, const char *name, SensorFunc sensorFunc, double min, double max, bool settings) {
  m_sensorFieldsJson[0] = Json::Float(SENSOR_FIELD_VALUE, 0);
  m_sensorFieldsJson[1] = Json::Boolean(SENSOR_FIELD_ACTIVE, true);
  if (max == SENSOR_MIN_MAX_NONE) {
    m_updateFunc = (settings ? &Sensor::updateDoubleSettings : &Sensor::updateDouble);
  } else {
    m_sensorFieldsJson[2] = Json::Float(SENSOR_SETTING_MIN, min);
    m_sensorFieldsJson[3] = Json::Float(SENSOR_SETTING_MAX, max);
    m_updateFunc = (settings ? &Sensor::updateDoubleMinMaxSettings : &Sensor::updateDoubleMinMax);
  }
  jsonElement = Json::Object(name, m_sensorFieldsJson);
  m_jsonElement = &jsonElement;
  m_sensorFunc = sensorFunc;
}

void Sensor::initBool(JsonElement &jsonElement, const char *name, SensorFunc sensorFunc, bool settings) {
  m_sensorFieldsJson[0] = Json::Boolean(SENSOR_FIELD_VALUE, false);
  m_sensorFieldsJson[1] = Json::Boolean(SENSOR_FIELD_ACTIVE, true);
  jsonElement = Json::Object(name, m_sensorFieldsJson);
  m_jsonElement = &jsonElement;
  m_sensorFunc = sensorFunc;
  m_updateFunc = (settings ? &Sensor::updateBoolSettings : &Sensor::updateBool);
}

void Sensor::initString(JsonElement &jsonElement, const char *name, SensorFunc sensorFunc, int length, bool settings) {
  m_sensorFieldsJson[0] = Json::String(SENSOR_FIELD_VALUE, "", length);
  m_sensorFieldsJson[1] = Json::Boolean(SENSOR_FIELD_ACTIVE, true);
  jsonElement = Json::Object(name, m_sensorFieldsJson);
  m_jsonElement = &jsonElement;
  m_sensorFunc = sensorFunc;
  m_updateFunc = (settings ? &Sensor::updateStringSettings : &Sensor::updateString);
}

void Sensor::initIntArray(JsonElement &jsonElement, const char *name, SensorFunc sensorFunc, int length, bool settings) {
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
  m_updateFunc = (settings ? &Sensor::updateIntArraySettings : &Sensor::updateIntArray);
}

void Sensor::initLongArray(JsonElement &jsonElement, const char *name, SensorFunc sensorFunc, int length, bool settings) {
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
  m_updateFunc = (settings ? &Sensor::updateLongArraySettings : &Sensor::updateLongArray);
}

void Sensor::initFloatArray(JsonElement &jsonElement, const char *name, SensorFunc sensorFunc, int length, bool settings) {
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
  m_updateFunc = (settings ? &Sensor::updateFloatArraySettings : &Sensor::updateFloatArray);
}

void Sensor::initDoubleArray(JsonElement &jsonElement, const char *name, SensorFunc sensorFunc, int length, bool settings) {
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
  m_updateFunc = (settings ? &Sensor::updateDoubleArraySettings : &Sensor::updateDoubleArray);
}

void Sensor::clearChangeIfInactive() {
  if (!(*m_jsonElement)[SENSOR_FIELD_ACTIVE].asBoolean()) {
    (*m_jsonElement)[SENSOR_FIELD_VALUE].clearChanged();
  }
}

void Sensor::updateMinMaxActiveInt() {
  long value = getValueInt();
  bool active = (value >= getMinInt() && value <= getMaxInt());
  (*m_jsonElement)[SENSOR_FIELD_ACTIVE] = active;
  if (!active) {
    (*m_jsonElement)[SENSOR_FIELD_VALUE].clearChanged();
  }
}

void Sensor::updateMinMaxActiveFloat() {
  double value = getValueFloat();
  bool active = (value >= getMinFloat() && value <= getMaxFloat());
  (*m_jsonElement)[SENSOR_FIELD_ACTIVE] = active;
  if (!active) {
    (*m_jsonElement)[SENSOR_FIELD_VALUE].clearChanged();
  }
}

void Sensor::updateInt() {
  (*m_jsonElement)[SENSOR_FIELD_VALUE] = m_sensorFunc.intSensorFunc();
  clearChangeIfInactive();
}

void Sensor::updateIntSettings() {
  (*m_jsonElement)[SENSOR_FIELD_VALUE] = m_sensorFunc.intSettingsSensorFunc(m_settings);
  clearChangeIfInactive();
}

void Sensor::updateIntMinMax() {
  (*m_jsonElement)[SENSOR_FIELD_VALUE] = m_sensorFunc.intMinMaxSensorFunc(getMinInt(), getMaxInt());
  updateMinMaxActiveInt();
}

void Sensor::updateIntMinMaxSettings() {
  (*m_jsonElement)[SENSOR_FIELD_VALUE] = m_sensorFunc.intMinMaxSettingsSensorFunc(getMinInt(), getMaxInt(), m_settings);
  updateMinMaxActiveInt();
}

void Sensor::updateLong() {
  (*m_jsonElement)[SENSOR_FIELD_VALUE] = m_sensorFunc.longSensorFunc();
  clearChangeIfInactive();
}

void Sensor::updateLongSettings() {
  (*m_jsonElement)[SENSOR_FIELD_VALUE] = m_sensorFunc.longSettingsSensorFunc(m_settings);
  clearChangeIfInactive();
}

void Sensor::updateLongMinMax() {
  (*m_jsonElement)[SENSOR_FIELD_VALUE] = m_sensorFunc.longMinMaxSensorFunc(getMinInt(), getMaxInt());
  updateMinMaxActiveInt();
}

void Sensor::updateLongMinMaxSettings() {
  (*m_jsonElement)[SENSOR_FIELD_VALUE] = m_sensorFunc.longMinMaxSettingsSensorFunc(getMinInt(), getMaxInt(), m_settings);
  updateMinMaxActiveInt();
}

void Sensor::updateFloat() {
  (*m_jsonElement)[SENSOR_FIELD_VALUE] = m_sensorFunc.floatSensorFunc();
  clearChangeIfInactive();
}

void Sensor::updateFloatSettings() {
  (*m_jsonElement)[SENSOR_FIELD_VALUE] = m_sensorFunc.floatSettingsSensorFunc(m_settings);
  clearChangeIfInactive();
}

void Sensor::updateFloatMinMax() {
  (*m_jsonElement)[SENSOR_FIELD_VALUE] = m_sensorFunc.floatMinMaxSensorFunc(getMinFloat(), getMaxFloat());
  updateMinMaxActiveFloat();
}

void Sensor::updateFloatMinMaxSettings() {
  (*m_jsonElement)[SENSOR_FIELD_VALUE] = m_sensorFunc.floatMinMaxSettingsSensorFunc(getMinFloat(), getMaxFloat(), m_settings);
  updateMinMaxActiveFloat();
}

void Sensor::updateDouble() {
  (*m_jsonElement)[SENSOR_FIELD_VALUE] = m_sensorFunc.doubleSensorFunc();
  clearChangeIfInactive();
}

void Sensor::updateDoubleSettings() {
  (*m_jsonElement)[SENSOR_FIELD_VALUE] = m_sensorFunc.doubleSettingsSensorFunc(m_settings);
  clearChangeIfInactive();
}

void Sensor::updateDoubleMinMax() {
  (*m_jsonElement)[SENSOR_FIELD_VALUE] = m_sensorFunc.doubleMinMaxSensorFunc(getMinFloat(), getMaxFloat());
  updateMinMaxActiveFloat();
}

void Sensor::updateDoubleMinMaxSettings() {
  (*m_jsonElement)[SENSOR_FIELD_VALUE] = m_sensorFunc.doubleMinMaxSettingsSensorFunc(getMinFloat(), getMaxFloat(), m_settings);
  updateMinMaxActiveFloat();
}

void Sensor::updateBool() {
  (*m_jsonElement)[SENSOR_FIELD_VALUE] = m_sensorFunc.boolSensorFunc();
  clearChangeIfInactive();
}

void Sensor::updateBoolSettings() {
  (*m_jsonElement)[SENSOR_FIELD_VALUE] = m_sensorFunc.boolSettingsSensorFunc(m_settings);
  clearChangeIfInactive();
}

void Sensor::updateString() {
  (*m_jsonElement)[SENSOR_FIELD_VALUE] = m_sensorFunc.stringSensorFunc();
  clearChangeIfInactive();
}

void Sensor::updateStringSettings() {
  (*m_jsonElement)[SENSOR_FIELD_VALUE] = m_sensorFunc.stringSettingsSensorFunc(m_settings);
  clearChangeIfInactive();
}

void Sensor::updateIntArray() {
  size_t length = (*m_jsonElement)[SENSOR_FIELD_LENGTH].asInt();
  for (int index = 0; index < length; index++) {
    (*m_jsonElement)[SENSOR_FIELD_ARRAY][index] = m_sensorFunc.intArraySensorFunc(index);
  }
  clearChangeIfInactive();
}

void Sensor::updateIntArraySettings() {
  size_t length = (*m_jsonElement)[SENSOR_FIELD_LENGTH].asInt();
  for (int index = 0; index < length; index++) {
    (*m_jsonElement)[SENSOR_FIELD_ARRAY][index] = m_sensorFunc.intArraySettingsSensorFunc(index, m_settings);
  }
  clearChangeIfInactive();
}

void Sensor::updateLongArray() {
  size_t length = (*m_jsonElement)[SENSOR_FIELD_LENGTH].asInt();
  for (int index = 0; index < length; index++) {
    (*m_jsonElement)[SENSOR_FIELD_ARRAY][index] = m_sensorFunc.longArraySensorFunc(index);
  }
  clearChangeIfInactive();
}

void Sensor::updateLongArraySettings() {
  size_t length = (*m_jsonElement)[SENSOR_FIELD_LENGTH].asInt();
  for (int index = 0; index < length; index++) {
    (*m_jsonElement)[SENSOR_FIELD_ARRAY][index] = m_sensorFunc.longArraySettingsSensorFunc(index, m_settings);
  }
  clearChangeIfInactive();
}

void Sensor::updateFloatArray() {
  size_t length = (*m_jsonElement)[SENSOR_FIELD_LENGTH].asInt();
  for (int index = 0; index < length; index++) {
    (*m_jsonElement)[SENSOR_FIELD_ARRAY][index] = m_sensorFunc.floatArraySensorFunc(index);
  }
  clearChangeIfInactive();
}

void Sensor::updateFloatArraySettings() {
  size_t length = (*m_jsonElement)[SENSOR_FIELD_LENGTH].asInt();
  for (int index = 0; index < length; index++) {
    (*m_jsonElement)[SENSOR_FIELD_ARRAY][index] = m_sensorFunc.floatArraySettingsSensorFunc(index, m_settings);
  }
  clearChangeIfInactive();
}

void Sensor::updateDoubleArray() {
  size_t length = (*m_jsonElement)[SENSOR_FIELD_LENGTH].asInt();
  for (int index = 0; index < length; index++) {
    (*m_jsonElement)[SENSOR_FIELD_ARRAY][index] = m_sensorFunc.doubleArraySensorFunc(index);
  }
  clearChangeIfInactive();
}

void Sensor::updateDoubleArraySettings() {
  size_t length = (*m_jsonElement)[SENSOR_FIELD_LENGTH].asInt();
  for (int index = 0; index < length; index++) {
    (*m_jsonElement)[SENSOR_FIELD_ARRAY][index] = m_sensorFunc.doubleArraySettingsSensorFunc(index, m_settings);
  }
  clearChangeIfInactive();
}
