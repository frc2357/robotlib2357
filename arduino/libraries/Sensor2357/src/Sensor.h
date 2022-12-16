#ifndef SENSOR_H
#define SENSOR_H

#include <Arduino.h>
#include <JsonState.h>

typedef int (*intSensorFunc_T)(int min, int max);
typedef long (*longSensorFunc_T)(long min, long max);
typedef float (*floatSensorFunc_T)(float min, float max);
typedef double (*doubleSensorFunc_T)(double min, double max);
typedef bool (*boolSensorFunc_T)();
typedef const char *(*stringSensorFunc_T)();
typedef int (*intArraySensorFunc_T)(size_t index);
typedef long (*longArraySensorFunc_T)(size_t index);
typedef float (*floatArraySensorFunc_T)(size_t index);
typedef double (*doubleArraySensorFunc_T)(size_t index);

union SensorFunc {
  intSensorFunc_T intSensorFunc;
  longSensorFunc_T longSensorFunc;
  floatSensorFunc_T floatSensorFunc;
  doubleSensorFunc_T doubleSensorFunc;
  boolSensorFunc_T boolSensorFunc;
  stringSensorFunc_T stringSensorFunc;
  intArraySensorFunc_T intArraySensorFunc;
  longArraySensorFunc_T longArraySensorFunc;
  floatArraySensorFunc_T floatArraySensorFunc;
  doubleArraySensorFunc_T doubleArraySensorFunc;

  SensorFunc() { intSensorFunc = NULL; }
  SensorFunc(intSensorFunc_T func) { intSensorFunc = func; }
  SensorFunc(longSensorFunc_T func) { longSensorFunc = func; }
  SensorFunc(floatSensorFunc_T func) { floatSensorFunc = func; }
  SensorFunc(doubleSensorFunc_T func) { doubleSensorFunc = func; }
  SensorFunc(boolSensorFunc_T func) { boolSensorFunc = func; }
  SensorFunc(stringSensorFunc_T func) { stringSensorFunc = func; }
  SensorFunc(intArraySensorFunc_T func) { intArraySensorFunc = func; }
  SensorFunc(longArraySensorFunc_T func) { longArraySensorFunc = func; }
  SensorFunc(floatArraySensorFunc_T func) { floatArraySensorFunc = func; }
  SensorFunc(doubleArraySensorFunc_T func) { doubleArraySensorFunc = func; }
};

typedef void (*updateSensor_T)(JsonElement &jsonElement, SensorFunc sensorFunc);

void updateIntSensor(JsonElement &jsonElement, SensorFunc sensorFunc);
void updateLongSensor(JsonElement &jsonElement, SensorFunc sensorFunc);
void updateFloatSensor(JsonElement &jsonElement, SensorFunc sensorFunc);
void updateDoubleSensor(JsonElement &jsonElement, SensorFunc sensorFunc);
void updateBoolSensor(JsonElement &jsonElement, SensorFunc sensorFunc);
void updateStringSensor(JsonElement &jsonElement, SensorFunc sensorFunc);
void updateIntArraySensor(JsonElement &jsonElement, SensorFunc sensorFunc);
void updateLongArraySensor(JsonElement &jsonElement, SensorFunc sensorFunc);
void updateFloatArraySensor(JsonElement &jsonElement, SensorFunc sensorFunc);
void updateDoubleArraySensor(JsonElement &jsonElement, SensorFunc sensorFunc);

class Sensor {
public:
  void init(JsonElement *jsonElement, SensorFunc sensorFunc, updateSensor_T updateFunc);
  void update();

private:
  JsonElement *m_jsonElement;
  SensorFunc m_sensorFunc;
  updateSensor_T m_updateFunc;
};

#endif /* SENSOR_H */
