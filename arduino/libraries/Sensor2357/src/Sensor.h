#ifndef SENSOR_H
#define SENSOR_H

#include <Arduino.h>
#include <JsonState.h>
#include <functional>

#define SENSOR_MAX_FIELD_COUNT      4
#define SENSOR_FIELD_PREFIX         "_"
#define SENSOR_FIELD_VALUE          "_value"
#define SENSOR_FIELD_ARRAY          "_array"
#define SENSOR_FIELD_LENGTH         "_length"
#define SENSOR_FIELD_ACTIVE         "_active"
#define SENSOR_SETTING_MIN          "min"
#define SENSOR_SETTING_MAX          "max"

class Sensor;

class SensorSettings {
public:
  SensorSettings(Sensor &sensor);
  JsonElement &operator[](const char *key) const;

private:
  Sensor &m_sensor;
};

typedef int (*intSensorFunc_T)(const SensorSettings &settings);
typedef int (*intMinMaxSensorFunc_T)(int min, int max, const SensorSettings &settings);
typedef long (*longSensorFunc_T)(const SensorSettings &settings);
typedef long (*longMinMaxSensorFunc_T)(long min, long max, const SensorSettings &settings);
typedef float (*floatSensorFunc_T)(const SensorSettings &settings);
typedef float (*floatMinMaxSensorFunc_T)(float min, float max, const SensorSettings &settings);
typedef double (*doubleSensorFunc_T)(const SensorSettings &settings);
typedef double (*doubleMinMaxSensorFunc_T)(double min, double max, const SensorSettings &settings);
typedef bool (*boolSensorFunc_T)(const SensorSettings &settings);
typedef const char *(*stringSensorFunc_T)(const SensorSettings &settings);
typedef int (*intArraySensorFunc_T)(size_t index, const SensorSettings &settings);
typedef long (*longArraySensorFunc_T)(size_t index, const SensorSettings &settings);
typedef float (*floatArraySensorFunc_T)(size_t index, const SensorSettings &settings);
typedef double (*doubleArraySensorFunc_T)(size_t index, const SensorSettings &settings);

union SensorFunc {
  intSensorFunc_T intSensorFunc;
  intMinMaxSensorFunc_T intMinMaxSensorFunc;
  longSensorFunc_T longSensorFunc;
  longMinMaxSensorFunc_T longMinMaxSensorFunc;
  floatSensorFunc_T floatSensorFunc;
  floatMinMaxSensorFunc_T floatMinMaxSensorFunc;
  doubleSensorFunc_T doubleSensorFunc;
  doubleMinMaxSensorFunc_T doubleMinMaxSensorFunc;
  boolSensorFunc_T boolSensorFunc;
  stringSensorFunc_T stringSensorFunc;
  intArraySensorFunc_T intArraySensorFunc;
  longArraySensorFunc_T longArraySensorFunc;
  floatArraySensorFunc_T floatArraySensorFunc;
  doubleArraySensorFunc_T doubleArraySensorFunc;

  SensorFunc() { intSensorFunc = NULL; }
  SensorFunc(intSensorFunc_T func) { intSensorFunc = func; }
  SensorFunc(intMinMaxSensorFunc_T func) { intMinMaxSensorFunc = func; }
  SensorFunc(longSensorFunc_T func) { longSensorFunc = func; }
  SensorFunc(longMinMaxSensorFunc_T func) { longMinMaxSensorFunc = func; }
  SensorFunc(floatSensorFunc_T func) { floatSensorFunc = func; }
  SensorFunc(floatMinMaxSensorFunc_T func) { floatMinMaxSensorFunc = func; }
  SensorFunc(doubleSensorFunc_T func) { doubleSensorFunc = func; }
  SensorFunc(doubleMinMaxSensorFunc_T func) { doubleMinMaxSensorFunc = func; }
  SensorFunc(boolSensorFunc_T func) { boolSensorFunc = func; }
  SensorFunc(stringSensorFunc_T func) { stringSensorFunc = func; }
  SensorFunc(intArraySensorFunc_T func) { intArraySensorFunc = func; }
  SensorFunc(longArraySensorFunc_T func) { longArraySensorFunc = func; }
  SensorFunc(floatArraySensorFunc_T func) { floatArraySensorFunc = func; }
  SensorFunc(doubleArraySensorFunc_T func) { doubleArraySensorFunc = func; }
};

class Sensor {
public:
  Sensor();
  ~Sensor();

  void init(JsonElement &jsonElement, const char *name, intSensorFunc_T sensorFunc);
  void init(JsonElement &jsonElement, const char *name, intMinMaxSensorFunc_T sensorFunc, int min, int max);
  void init(JsonElement &jsonElement, const char *name, longSensorFunc_T sensorFunc);
  void init(JsonElement &jsonElement, const char *name, longMinMaxSensorFunc_T sensorFunc, long min, long max);
  void init(JsonElement &jsonElement, const char *name, floatSensorFunc_T sensorFunc);
  void init(JsonElement &jsonElement, const char *name, floatMinMaxSensorFunc_T sensorFunc, float min, float max);
  void init(JsonElement &jsonElement, const char *name, doubleSensorFunc_T sensorFunc);
  void init(JsonElement &jsonElement, const char *name, doubleMinMaxSensorFunc_T sensorFunc, double min, double max);
  void init(JsonElement &jsonElement, const char *name, boolSensorFunc_T sensorFunc);
  void init(JsonElement &jsonElement, const char *name, stringSensorFunc_T sensorFunc, size_t length);
  void init(JsonElement &jsonElement, const char *name, intArraySensorFunc_T sensorFunc, size_t length);
  void init(JsonElement &jsonElement, const char *name, longArraySensorFunc_T sensorFunc, size_t length);
  void init(JsonElement &jsonElement, const char *name, floatArraySensorFunc_T sensorFunc, size_t length);
  void init(JsonElement &jsonElement, const char *name, doubleArraySensorFunc_T sensorFunc, size_t length);
  void update();

  JsonElement &getSetting(const char *name);

private:
  void updateInt();
  void updateIntMinMax();
  void updateLong();
  void updateLongMinMax();
  void updateFloat();
  void updateFloatMinMax();
  void updateDouble();
  void updateDoubleMinMax();
  void updateBool();
  void updateString();
  void updateIntArray();
  void updateLongArray();
  void updateFloatArray();
  void updateDoubleArray();

  JsonElement *m_jsonElement;
  JsonElement m_sensorFieldsJson[SENSOR_MAX_FIELD_COUNT];
  SensorFunc m_sensorFunc;
  std::function<void(Sensor&)> m_updateFunc;
  SensorSettings m_settings;
};

#endif /* SENSOR_H */
