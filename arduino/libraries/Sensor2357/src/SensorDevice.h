#ifndef SENSOR_DEVICE_H
#define SENSOR_DEVICE_H

#include "Arduino.h"
#include <JsonState.h>

#define SENSOR_DEVICE_LOOP_DELAY_MS            10
#define SENSOR_DEVICE_DEFAULT_UPDATE_MS        1000
#define SENSOR_DEVICE_SERIAL_PREAMBLE          "|||||"

class SensorDevice {
public:
  virtual void begin();
  virtual void update();

protected:
  template<size_t S>
  SensorDevice(const char *deviceName, JsonElement (&sensors)[S], Print &out) : m_out(out) {
    m_deviceFields[0] = Json::String("name", deviceName);
    m_deviceFields[1] = Json::Object("sensors", sensors);
    m_deviceJson = Json::Object(m_deviceFields);
    m_lastUpdateMs = 0;
  }

  ~SensorDevice() {
    // SensorDevice should never be destructed because it should be created in the root sketch.
  }

  virtual void statusDisconnected() = 0;
  virtual void statusIdle() = 0;
  virtual void statusActive() = 0;
  virtual void statusError() = 0;
  virtual void writeRaw(const char *message) = 0;

private:
  Print &m_out;
  JsonElement m_deviceFields[2];
  JsonElement m_deviceJson;
  unsigned long m_lastUpdateMs;
};

#endif /* SENSOR_DEVICE_H */
