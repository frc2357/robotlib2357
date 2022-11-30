#ifndef SENSOR_VALUE_H
#define SENSOR_VALUE_H

#include "Arduino.h"
#include <JsonState.h>

class SensorValue {
public:
  SensorValue();
  ~SensorValue();

private:
  JsonElement m_jsonElement;
};

#endif /* SENSOR_VALUE_H */
