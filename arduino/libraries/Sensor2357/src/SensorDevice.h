#ifndef SENSOR_DEVICE_H
#define SENSOR_DEVICE_H

#include <Arduino.h>
#include <cstdarg>
#include <JsonState.h>
#include "Sensor.h"

#define SENSOR_DEVICE_DEFAULT_MAX_UPDATE_HZ       10
#define SENSOR_DEVICE_DEFAULT_TIMEOUT_MS          1000 // Use with RoboRIO
#define SENSOR_DEVICE_SERIAL_PREAMBLE             "||v1||"
#define SENSOR_DEVICE_ERROR_MAX_LENGTH            64
#define SENSOR_DEVICE_IN_BUFFER_LENGTH            256

#define SENSOR_DEVICE_FIELD_NAME                  0
#define SENSOR_DEVICE_FIELD_ERROR                 1
#define SENSOR_DEVICE_FIELD_MAX_UPDATE_HZ         2
#define SENSOR_DEVICE_FIELD_TIMEOUT_MS            3
#define SENSOR_DEVICE_FIELD_SENSORS               4
#define SENSOR_DEVICE_FIELD_COUNT                 5

template <size_t S>
class SensorDevice {
public:
  virtual void initSensor(const char *name, intSensorFunc_T sensorFunc) {
    size_t index = allocateIndex();
    if (index >= 0) {
      m_sensors[index].init(m_sensorsJson[index], name, sensorFunc);
    }
  }

  virtual void initSensor(const char *name, intSettingsSensorFunc_T sensorFunc) {
    size_t index = allocateIndex();
    if (index >= 0) {
      m_sensors[index].init(m_sensorsJson[index], name, sensorFunc);
    }
  }

  virtual void initSensor(const char *name, intMinMaxSensorFunc_T sensorFunc, int min, int max) {
    size_t index = allocateIndex();
    if (index >= 0) {
      m_sensors[index].init(m_sensorsJson[index], name, sensorFunc, min, max);
    }
  }

  virtual void initSensor(const char *name, intMinMaxSettingsSensorFunc_T sensorFunc, int min, int max) {
    size_t index = allocateIndex();
    if (index >= 0) {
      m_sensors[index].init(m_sensorsJson[index], name, sensorFunc, min, max);
    }
  }

  virtual void initSensor(const char *name, longSensorFunc_T sensorFunc) {
    size_t index = allocateIndex();
    if (index >= 0) {
      m_sensors[index].init(m_sensorsJson[index], name, sensorFunc);
    }
  }

  virtual void initSensor(const char *name, longSettingsSensorFunc_T sensorFunc) {
    size_t index = allocateIndex();
    if (index >= 0) {
      m_sensors[index].init(m_sensorsJson[index], name, sensorFunc);
    }
  }

  virtual void initSensor(const char *name, longMinMaxSensorFunc_T sensorFunc, long min, long max) {
    size_t index = allocateIndex();
    if (index >= 0) {
      m_sensors[index].init(m_sensorsJson[index], name, sensorFunc, min, max);
    }
  }

  virtual void initSensor(const char *name, longMinMaxSettingsSensorFunc_T sensorFunc, long min, long max) {
    size_t index = allocateIndex();
    if (index >= 0) {
      m_sensors[index].init(m_sensorsJson[index], name, sensorFunc, min, max);
    }
  }

  virtual void initSensor(const char *name, floatSensorFunc_T sensorFunc) {
    size_t index = allocateIndex();
    if (index >= 0) {
      m_sensors[index].init(m_sensorsJson[index], name, sensorFunc);
    }
  }

  virtual void initSensor(const char *name, floatSettingsSensorFunc_T sensorFunc) {
    size_t index = allocateIndex();
    if (index >= 0) {
      m_sensors[index].init(m_sensorsJson[index], name, sensorFunc);
    }
  }

  virtual void initSensor(const char *name, floatMinMaxSensorFunc_T sensorFunc, float min, float max) {
    size_t index = allocateIndex();
    if (index >= 0) {
      m_sensors[index].init(m_sensorsJson[index], name, sensorFunc, min, max);
    }
  }

  virtual void initSensor(const char *name, floatMinMaxSettingsSensorFunc_T sensorFunc, float min, float max) {
    size_t index = allocateIndex();
    if (index >= 0) {
      m_sensors[index].init(m_sensorsJson[index], name, sensorFunc, min, max);
    }
  }

  virtual void initSensor(const char *name, doubleSensorFunc_T sensorFunc) {
    size_t index = allocateIndex();
    if (index >= 0) {
      m_sensors[index].init(m_sensorsJson[index], name, sensorFunc);
    }
  }

  virtual void initSensor(const char *name, doubleSettingsSensorFunc_T sensorFunc) {
    size_t index = allocateIndex();
    if (index >= 0) {
      m_sensors[index].init(m_sensorsJson[index], name, sensorFunc);
    }
  }

  virtual void initSensor(const char *name, doubleMinMaxSensorFunc_T sensorFunc, double min, double max) {
    size_t index = allocateIndex();
    if (index >= 0) {
      m_sensors[index].init(m_sensorsJson[index], name, sensorFunc, min, max);
    }
  }

  virtual void initSensor(const char *name, doubleMinMaxSettingsSensorFunc_T sensorFunc, double min, double max) {
    size_t index = allocateIndex();
    if (index >= 0) {
      m_sensors[index].init(m_sensorsJson[index], name, sensorFunc, min, max);
    }
  }

  virtual void initSensor(const char *name, boolSensorFunc_T sensorFunc) {
    size_t index = allocateIndex();
    if (index >= 0) {
      m_sensors[index].init(m_sensorsJson[index], name, sensorFunc);
    }
  }

  virtual void initSensor(const char *name, boolSettingsSensorFunc_T sensorFunc) {
    size_t index = allocateIndex();
    if (index >= 0) {
      m_sensors[index].init(m_sensorsJson[index], name, sensorFunc);
    }
  }

  virtual void initSensor(const char *name, stringSensorFunc_T sensorFunc, size_t length) {
    size_t index = allocateIndex();
    if (index >= 0) {
      m_sensors[index].init(m_sensorsJson[index], name, sensorFunc, length);
    }
  }

  virtual void initSensor(const char *name, stringSettingsSensorFunc_T sensorFunc, size_t length) {
    size_t index = allocateIndex();
    if (index >= 0) {
      m_sensors[index].init(m_sensorsJson[index], name, sensorFunc, length);
    }
  }

  virtual void initSensor(const char *name, intArraySensorFunc_T sensorFunc, size_t length) {
    size_t index = allocateIndex();
    if (index >= 0) {
      m_sensors[index].init(m_sensorsJson[index], name, sensorFunc, length);
    }
  }

  virtual void initSensor(const char *name, intArraySettingsSensorFunc_T sensorFunc, size_t length) {
    size_t index = allocateIndex();
    if (index >= 0) {
      m_sensors[index].init(m_sensorsJson[index], name, sensorFunc, length);
    }
  }

  virtual void initSensor(const char *name, longArraySensorFunc_T sensorFunc, size_t length) {
    size_t index = allocateIndex();
    if (index >= 0) {
      m_sensors[index].init(m_sensorsJson[index], name, sensorFunc, length);
    }
  }

  virtual void initSensor(const char *name, longArraySettingsSensorFunc_T sensorFunc, size_t length) {
    size_t index = allocateIndex();
    if (index >= 0) {
      m_sensors[index].init(m_sensorsJson[index], name, sensorFunc, length);
    }
  }

  virtual void initSensor(const char *name, floatArraySensorFunc_T sensorFunc, size_t length) {
    size_t index = allocateIndex();
    if (index >= 0) {
      m_sensors[index].init(m_sensorsJson[index], name, sensorFunc, length);
    }
  }

  virtual void initSensor(const char *name, floatArraySettingsSensorFunc_T sensorFunc, size_t length) {
    size_t index = allocateIndex();
    if (index >= 0) {
      m_sensors[index].init(m_sensorsJson[index], name, sensorFunc, length);
    }
  }

  virtual void initSensor(const char *name, doubleArraySensorFunc_T sensorFunc, size_t length) {
    size_t index = allocateIndex();
    if (index >= 0) {
      m_sensors[index].init(m_sensorsJson[index], name, sensorFunc, length);
    }
  }

  virtual void initSensor(const char *name, doubleArraySettingsSensorFunc_T sensorFunc, size_t length) {
    size_t index = allocateIndex();
    if (index >= 0) {
      m_sensors[index].init(m_sensorsJson[index], name, sensorFunc, length);
    }
  }

  virtual void begin() {
    if (m_sensorInitCount < S) {
      setError("WARN: %d sensors allocated, only %d initialized.", S, m_sensorInitCount);
    }
    sendState(true);
  }

  virtual void update() {
    bool updateReached = millis() > m_lastUpdateMs + (1000 / getMaxUpdateHz());
    bool sendFullState = false;

    // Update all the sensor values
    for (int i = 0; i < S; i++) {
      m_sensors[i].update();
    }

    if (m_in.available()) {
      sendFullState = receiveState();
    }

    if (isTimedOut()) {
      statusDisconnected();
      return;
    }

    bool hasChanged = m_jsonState.hasChanged();
    if (sendFullState || (updateReached && hasChanged)) {
      sendState(sendFullState);
      statusActive();
    } else {
      statusIdle();
    }
  }

  virtual int getMaxUpdateHz() {
    return m_fieldsJson[SENSOR_DEVICE_FIELD_MAX_UPDATE_HZ].asInt();
  }

  virtual void setMaxUpdateHz(int hz) {
    m_fieldsJson[SENSOR_DEVICE_FIELD_MAX_UPDATE_HZ] = hz;
  }

  bool isTimedOut() {
    return (millis() > m_lastRemoteMessageMs + getTimeoutMs());
  }

  virtual int getTimeoutMs() {
    return m_fieldsJson[SENSOR_DEVICE_FIELD_TIMEOUT_MS].asInt();
  }

  virtual void setTimeoutMs(int ms) {
    m_fieldsJson[SENSOR_DEVICE_FIELD_TIMEOUT_MS] = ms;
  }

  virtual void setError(const char *format, ...) {
    va_list args;
    char message[SENSOR_DEVICE_ERROR_MAX_LENGTH];
    va_start(args, format);
    vsprintf(message, format, args);
    va_end(args);
    m_fieldsJson[SENSOR_DEVICE_FIELD_ERROR] = message;
  }

  virtual void clearError() {
    m_fieldsJson[SENSOR_DEVICE_FIELD_ERROR] = "";
  }

protected:
  SensorDevice(const char *deviceName, Print &out, Stream &in)
    : m_out(out),
      m_in(in),
      m_jsonState(m_sensorDeviceJson)
  {
    m_lastUpdateMs = 0;
    m_sensorInitCount = 0;
    
    m_fieldsJson[SENSOR_DEVICE_FIELD_NAME] = Json::String("name", deviceName);
    m_fieldsJson[SENSOR_DEVICE_FIELD_ERROR] = Json::String("error", "", SENSOR_DEVICE_ERROR_MAX_LENGTH);
    m_fieldsJson[SENSOR_DEVICE_FIELD_MAX_UPDATE_HZ] = Json::Int("maxUpdateHz", SENSOR_DEVICE_DEFAULT_MAX_UPDATE_HZ);
    m_fieldsJson[SENSOR_DEVICE_FIELD_TIMEOUT_MS] = Json::Int("timeoutMs", SENSOR_DEVICE_DEFAULT_TIMEOUT_MS);
    m_fieldsJson[SENSOR_DEVICE_FIELD_SENSORS] = Json::Object("sensors", m_sensorsJson);
    m_sensorDeviceJson = Json::Object(m_fieldsJson);
  }

  ~SensorDevice() {
    // WARNING: SensorDevice should never be destructed. It should always be created in the root sketch
  }

  virtual void statusDisconnected() = 0;
  virtual void statusIdle() = 0;
  virtual void statusActive() = 0;
  virtual void statusError() = 0;

  void sendState(bool fullState) {
    m_out.print(SENSOR_DEVICE_SERIAL_PREAMBLE);
    m_jsonState.printJson(m_out, !fullState);
    m_jsonState.clearChanged();
    m_out.println();
    m_out.flush();
    m_lastUpdateMs = millis();
  }

  bool receiveState() {
    // Advance past whatever exists until the first preamble character.
    m_in.readBytesUntil(SENSOR_DEVICE_SERIAL_PREAMBLE[0], m_inBuffer, SENSOR_DEVICE_IN_BUFFER_LENGTH);

    // Read in the remainder of the preamble and terminate the string
    size_t length = m_in.readBytes(m_inBuffer, strlen(SENSOR_DEVICE_SERIAL_PREAMBLE) - 1);
    m_inBuffer[length] = '\0';

    // Verify the preamble
    if (strncmp(m_inBuffer, SENSOR_DEVICE_SERIAL_PREAMBLE + 1, strlen(SENSOR_DEVICE_SERIAL_PREAMBLE) - 1) != 0) {
      setError("Invalid preamble, expected '%s'", SENSOR_DEVICE_SERIAL_PREAMBLE);
      return false;
    }

    // Read JSON until newline
    length = m_in.readBytesUntil('\n', m_inBuffer, SENSOR_DEVICE_IN_BUFFER_LENGTH - 1);
    if (length == SENSOR_DEVICE_IN_BUFFER_LENGTH - 1) {
      setError("Received state must be < %d chars", SENSOR_DEVICE_IN_BUFFER_LENGTH);
      return false;
    }

    // If we've reached this far, we've successfully received a message.
    m_lastRemoteMessageMs = millis();

    // If empty line was sent, it's a keep-alive
    if (length == 0) {
      return false;
    }

    // If it's empty braces "{}" it's asking to send the full state
    if (length == 2 && m_inBuffer[0] == '{' && m_inBuffer[1] == '}') {
      return true;
    }

    // Set string terminator, so we can use it
    m_inBuffer[length] = '\0';
    m_jsonState.updateFromJson(m_inBuffer);
    return true;
  }

  size_t allocateIndex() {
    size_t index = m_sensorInitCount;
    m_sensorInitCount++;
    if (m_sensorInitCount > S) {
      setError("Can't init %d sensors, only %d allocated", m_sensorInitCount, S);
      return -1;
    }
    return index;
  }

private:
  Print &m_out;
  Stream &m_in;
  char m_inBuffer[SENSOR_DEVICE_IN_BUFFER_LENGTH];
  JsonElement m_fieldsJson[SENSOR_DEVICE_FIELD_COUNT];
  JsonElement m_sensorsJson[S];
  Sensor m_sensors[S];
  JsonElement m_sensorDeviceJson;
  JsonState m_jsonState;
  unsigned long m_lastUpdateMs;
  unsigned long m_lastRemoteMessageMs;
  size_t m_sensorInitCount;
};

#endif /* SENSOR_DEVICE_H */
