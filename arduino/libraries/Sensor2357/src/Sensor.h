#ifndef SENSOR_H
#define SENSOR_H

#include <Arduino.h>
#include <JsonState.h>

typedef int (*intSensorFunc_T)();
typedef long (*longSensorFunc_T)();
typedef float (*floatSensorFunc_T)();
typedef double (*doubleSensorFunc_T)();
typedef bool (*boolSensorFunc_T)();
typedef const char *(*stringSensorFunc_T)();
typedef long (*longArraySensorFunc_T)(size_t index);
typedef double (*doubleArraySensorFunc_T)(size_t index);

union SensorFunc {
  intSensorFunc_T intSensorFunc;
  longSensorFunc_T longSensorFunc;
  floatSensorFunc_T floatSensorFunc;
  doubleSensorFunc_T doubleSensorFunc;
  boolSensorFunc_T boolSensorFunc;
  stringSensorFunc_T stringSensorFunc;
  longArraySensorFunc_T longArraySensorFunc;
  doubleArraySensorFunc_T doubleArraySensorFunc;
};

typedef void (*updateSensor_T)(JsonElement &jsonElement, SensorFunc sensorFunc);

void updateFloatSensor(JsonElement &jsonElement, SensorFunc sensorFunc);

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
