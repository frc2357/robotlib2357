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
#define SENSOR_MIN_MAX_NONE         -32767

class Sensor;

class SensorSettings {
public:
  SensorSettings(Sensor &sensor);
  JsonElement &operator[](const char *key) const;

private:
  Sensor &m_sensor;
};

typedef int (*intSensorFunc_T)();
typedef int (*intSettingsSensorFunc_T)(const SensorSettings &settings);
typedef int (*intMinMaxSensorFunc_T)(int min, int max);
typedef int (*intMinMaxSettingsSensorFunc_T)(int min, int max, const SensorSettings &settings);
typedef long (*longSensorFunc_T)();
typedef long (*longSettingsSensorFunc_T)(const SensorSettings &settings);
typedef long (*longMinMaxSensorFunc_T)(long min, long max);
typedef long (*longMinMaxSettingsSensorFunc_T)(long min, long max, const SensorSettings &settings);
typedef float (*floatSensorFunc_T)();
typedef float (*floatSettingsSensorFunc_T)(const SensorSettings &settings);
typedef float (*floatMinMaxSensorFunc_T)(float min, float max);
typedef float (*floatMinMaxSettingsSensorFunc_T)(float min, float max, const SensorSettings &settings);
typedef double (*doubleSensorFunc_T)();
typedef double (*doubleSettingsSensorFunc_T)(const SensorSettings &settings);
typedef double (*doubleMinMaxSensorFunc_T)(double min, double max);
typedef double (*doubleMinMaxSettingsSensorFunc_T)(double min, double max, const SensorSettings &settings);
typedef bool (*boolSensorFunc_T)();
typedef bool (*boolSettingsSensorFunc_T)(const SensorSettings &settings);
typedef const char *(*stringSensorFunc_T)();
typedef const char *(*stringSettingsSensorFunc_T)(const SensorSettings &settings);
typedef int (*intArraySensorFunc_T)(size_t index);
typedef int (*intArraySettingsSensorFunc_T)(size_t index, const SensorSettings &settings);
typedef long (*longArraySensorFunc_T)(size_t index);
typedef long (*longArraySettingsSensorFunc_T)(size_t index, const SensorSettings &settings);
typedef float (*floatArraySensorFunc_T)(size_t index);
typedef float (*floatArraySettingsSensorFunc_T)(size_t index, const SensorSettings &settings);
typedef double (*doubleArraySensorFunc_T)(size_t index);
typedef double (*doubleArraySettingsSensorFunc_T)(size_t index, const SensorSettings &settings);

union SensorFunc {
  intSensorFunc_T intSensorFunc;
  intSettingsSensorFunc_T intSettingsSensorFunc;
  intMinMaxSensorFunc_T intMinMaxSensorFunc;
  intMinMaxSettingsSensorFunc_T intMinMaxSettingsSensorFunc;
  longSensorFunc_T longSensorFunc;
  longSettingsSensorFunc_T longSettingsSensorFunc;
  longMinMaxSensorFunc_T longMinMaxSensorFunc;
  longMinMaxSettingsSensorFunc_T longMinMaxSettingsSensorFunc;
  floatSensorFunc_T floatSensorFunc;
  floatSettingsSensorFunc_T floatSettingsSensorFunc;
  floatMinMaxSensorFunc_T floatMinMaxSensorFunc;
  floatMinMaxSettingsSensorFunc_T floatMinMaxSettingsSensorFunc;
  doubleSensorFunc_T doubleSensorFunc;
  doubleSettingsSensorFunc_T doubleSettingsSensorFunc;
  doubleMinMaxSensorFunc_T doubleMinMaxSensorFunc;
  doubleMinMaxSettingsSensorFunc_T doubleMinMaxSettingsSensorFunc;
  boolSensorFunc_T boolSensorFunc;
  boolSettingsSensorFunc_T boolSettingsSensorFunc;
  stringSensorFunc_T stringSensorFunc;
  stringSettingsSensorFunc_T stringSettingsSensorFunc;
  intArraySensorFunc_T intArraySensorFunc;
  intArraySettingsSensorFunc_T intArraySettingsSensorFunc;
  longArraySensorFunc_T longArraySensorFunc;
  longArraySettingsSensorFunc_T longArraySettingsSensorFunc;
  floatArraySensorFunc_T floatArraySensorFunc;
  floatArraySettingsSensorFunc_T floatArraySettingsSensorFunc;
  doubleArraySensorFunc_T doubleArraySensorFunc;
  doubleArraySettingsSensorFunc_T doubleArraySettingsSensorFunc;

  SensorFunc() { intSensorFunc = NULL; }
  SensorFunc(intSensorFunc_T func) { intSensorFunc = func; }
  SensorFunc(intSettingsSensorFunc_T func) { intSettingsSensorFunc = func; }
  SensorFunc(intMinMaxSensorFunc_T func) { intMinMaxSensorFunc = func; }
  SensorFunc(intMinMaxSettingsSensorFunc_T func) { intMinMaxSettingsSensorFunc = func; }
  SensorFunc(longSensorFunc_T func) { longSensorFunc = func; }
  SensorFunc(longSettingsSensorFunc_T func) { longSettingsSensorFunc = func; }
  SensorFunc(longMinMaxSensorFunc_T func) { longMinMaxSensorFunc = func; }
  SensorFunc(longMinMaxSettingsSensorFunc_T func) { longMinMaxSettingsSensorFunc = func; }
  SensorFunc(floatSensorFunc_T func) { floatSensorFunc = func; }
  SensorFunc(floatSettingsSensorFunc_T func) { floatSettingsSensorFunc = func; }
  SensorFunc(floatMinMaxSensorFunc_T func) { floatMinMaxSensorFunc = func; }
  SensorFunc(floatMinMaxSettingsSensorFunc_T func) { floatMinMaxSettingsSensorFunc = func; }
  SensorFunc(doubleSensorFunc_T func) { doubleSensorFunc = func; }
  SensorFunc(doubleSettingsSensorFunc_T func) { doubleSettingsSensorFunc = func; }
  SensorFunc(doubleMinMaxSensorFunc_T func) { doubleMinMaxSensorFunc = func; }
  SensorFunc(doubleMinMaxSettingsSensorFunc_T func) { doubleMinMaxSettingsSensorFunc = func; }
  SensorFunc(boolSensorFunc_T func) { boolSensorFunc = func; }
  SensorFunc(boolSettingsSensorFunc_T func) { boolSettingsSensorFunc = func; }
  SensorFunc(stringSensorFunc_T func) { stringSensorFunc = func; }
  SensorFunc(stringSettingsSensorFunc_T func) { stringSettingsSensorFunc = func; }
  SensorFunc(intArraySensorFunc_T func) { intArraySensorFunc = func; }
  SensorFunc(intArraySettingsSensorFunc_T func) { intArraySettingsSensorFunc = func; }
  SensorFunc(longArraySensorFunc_T func) { longArraySensorFunc = func; }
  SensorFunc(longArraySettingsSensorFunc_T func) { longArraySettingsSensorFunc = func; }
  SensorFunc(floatArraySensorFunc_T func) { floatArraySensorFunc = func; }
  SensorFunc(floatArraySettingsSensorFunc_T func) { floatArraySettingsSensorFunc = func; }
  SensorFunc(doubleArraySensorFunc_T func) { doubleArraySensorFunc = func; }
  SensorFunc(doubleArraySettingsSensorFunc_T func) { doubleArraySettingsSensorFunc = func; }
};

class Sensor {
public:
  Sensor();
  ~Sensor();

  void init(JsonElement &jsonElement, const char *name, intSensorFunc_T sensorFunc);
  void init(JsonElement &jsonElement, const char *name, intSettingsSensorFunc_T sensorFunc);
  void init(JsonElement &jsonElement, const char *name, intMinMaxSensorFunc_T sensorFunc, int min, int max);
  void init(JsonElement &jsonElement, const char *name, intMinMaxSettingsSensorFunc_T sensorFunc, int min, int max);
  void init(JsonElement &jsonElement, const char *name, longSensorFunc_T sensorFunc);
  void init(JsonElement &jsonElement, const char *name, longSettingsSensorFunc_T sensorFunc);
  void init(JsonElement &jsonElement, const char *name, longMinMaxSensorFunc_T sensorFunc, long min, long max);
  void init(JsonElement &jsonElement, const char *name, longMinMaxSettingsSensorFunc_T sensorFunc, long min, long max);
  void init(JsonElement &jsonElement, const char *name, floatSensorFunc_T sensorFunc);
  void init(JsonElement &jsonElement, const char *name, floatSettingsSensorFunc_T sensorFunc);
  void init(JsonElement &jsonElement, const char *name, floatMinMaxSensorFunc_T sensorFunc, float min, float max);
  void init(JsonElement &jsonElement, const char *name, floatMinMaxSettingsSensorFunc_T sensorFunc, float min, float max);
  void init(JsonElement &jsonElement, const char *name, doubleSensorFunc_T sensorFunc);
  void init(JsonElement &jsonElement, const char *name, doubleSettingsSensorFunc_T sensorFunc);
  void init(JsonElement &jsonElement, const char *name, doubleMinMaxSensorFunc_T sensorFunc, double min, double max);
  void init(JsonElement &jsonElement, const char *name, doubleMinMaxSettingsSensorFunc_T sensorFunc, double min, double max);
  void init(JsonElement &jsonElement, const char *name, boolSensorFunc_T sensorFunc);
  void init(JsonElement &jsonElement, const char *name, boolSettingsSensorFunc_T sensorFunc);
  void init(JsonElement &jsonElement, const char *name, stringSensorFunc_T sensorFunc, size_t length);
  void init(JsonElement &jsonElement, const char *name, stringSettingsSensorFunc_T sensorFunc, size_t length);
  void init(JsonElement &jsonElement, const char *name, intArraySensorFunc_T sensorFunc, size_t length);
  void init(JsonElement &jsonElement, const char *name, intArraySettingsSensorFunc_T sensorFunc, size_t length);
  void init(JsonElement &jsonElement, const char *name, longArraySensorFunc_T sensorFunc, size_t length);
  void init(JsonElement &jsonElement, const char *name, longArraySettingsSensorFunc_T sensorFunc, size_t length);
  void init(JsonElement &jsonElement, const char *name, floatArraySensorFunc_T sensorFunc, size_t length);
  void init(JsonElement &jsonElement, const char *name, floatArraySettingsSensorFunc_T sensorFunc, size_t length);
  void init(JsonElement &jsonElement, const char *name, doubleArraySensorFunc_T sensorFunc, size_t length);
  void init(JsonElement &jsonElement, const char *name, doubleArraySettingsSensorFunc_T sensorFunc, size_t length);
  void update();

  JsonElement &getSetting(const char *name);
  long getValueInt();
  double getValueFloat();
  long getMinInt();
  long getMaxInt();
  double getMinFloat();
  double getMaxFloat();


private:
  void initInt(JsonElement &jsonElement, const char *name, SensorFunc sensorFunc, int min, int max, bool settings);
  void initLong(JsonElement &jsonElement, const char *name, SensorFunc sensorFunc, long min, long max, bool settings);
  void initFloat(JsonElement &jsonElement, const char *name, SensorFunc sensorFunc, float min, float max, bool settings);
  void initDouble(JsonElement &jsonElement, const char *name, SensorFunc sensorFunc, double min, double max, bool settings);
  void initBool(JsonElement &jsonElement, const char *name, SensorFunc sensorFunc, bool settings);
  void initString(JsonElement &jsonElement, const char *name, SensorFunc sensorFunc, int length, bool settings);
  void initIntArray(JsonElement &jsonElement, const char *name, SensorFunc sensorFunc, int length, bool settings);
  void initLongArray(JsonElement &jsonElement, const char *name, SensorFunc sensorFunc, int length, bool settings);
  void initFloatArray(JsonElement &jsonElement, const char *name, SensorFunc sensorFunc, int length, bool settings);
  void initDoubleArray(JsonElement &jsonElement, const char *name, SensorFunc sensorFunc, int length, bool settings);

  void clearChangeIfInactive();
  void updateMinMaxActiveInt();
  void updateMinMaxActiveFloat();

  void updateInt();
  void updateIntSettings();
  void updateIntMinMax();
  void updateIntMinMaxSettings();
  void updateLong();
  void updateLongSettings();
  void updateLongMinMax();
  void updateLongMinMaxSettings();
  void updateFloat();
  void updateFloatSettings();
  void updateFloatMinMax();
  void updateFloatMinMaxSettings();
  void updateDouble();
  void updateDoubleSettings();
  void updateDoubleMinMax();
  void updateDoubleMinMaxSettings();
  void updateBool();
  void updateBoolSettings();
  void updateString();
  void updateStringSettings();
  void updateIntArray();
  void updateIntArraySettings();
  void updateLongArray();
  void updateLongArraySettings();
  void updateFloatArray();
  void updateFloatArraySettings();
  void updateDoubleArray();
  void updateDoubleArraySettings();

  JsonElement *m_jsonElement;
  JsonElement m_sensorFieldsJson[SENSOR_MAX_FIELD_COUNT];
  SensorFunc m_sensorFunc;
  std::function<void(Sensor&)> m_updateFunc;
  SensorSettings m_settings;
};

#endif /* SENSOR_H */
