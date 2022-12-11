#ifndef SENSOR_DEVICE_H
#define SENSOR_DEVICE_H

#include <Arduino.h>
#include <JsonState.h>

#define SENSOR_DEVICE_DEFAULT_MAX_UPDATE_MS       1000
#define SENSOR_DEVICE_SERIAL_PREAMBLE             "|||||"

typedef void (*logErrorFunc_T)(const char *format, ...);

typedef long (*longSensorFunc_T)(logErrorFunc_T logError);
typedef double (*doubleSensorFunc_T)(logErrorFunc_T logError);
typedef bool (*boolSensorFunc_T)(logErrorFunc_T logError);
typedef const char *(*stringSensorFunc_T)(logErrorFunc_T logError);
typedef long (*longArraySensorFunc_T)(size_t index, logErrorFunc_T logError);
typedef double (*doubleArraySensorFunc_T)(size_t index, logErrorFunc_T logError);

template <size_t S>
class SensorDevice {
public:
  void initSensor(size_t index, const char *name, longSensorFunc_T sensorFunc, long minValue, long maxValue) {
  }

  void initSensor(size_t index, const char *name, doubleSensorFunc_T sensorFunc, double minValue, double maxValue) {
  }

  void initSensor(size_t index, const char *name, boolSensorFunc_T sensorFunc) {
  }

  void initSensor(size_t index, const char *name, stringSensorFunc_T sensorFunc) {
  }

  void initSensor(size_t index, const char *name, longArraySensorFunc_T sensorFunc, size_t length) {
  }

  void initSensor(size_t index, const char *name, doubleArraySensorFunc_T sensorFunc, size_t length) {
  }

  virtual void begin() {
  }

  virtual void update() {
    unsigned long now = millis();
    if (now > m_lastUpdateMs + getMaxUpdateMs()) {
      m_out.print(SENSOR_DEVICE_SERIAL_PREAMBLE);
      m_jsonState.printJson(m_out);
      // TODO: Only send what changed
      //m_jsonState.clearChanged();
      m_out.println();
      m_out.flush();
      m_lastUpdateMs = now;
    }
  }

  int getMaxUpdateMs() {
    return m_maxUpdateMsJson.asInt();
  }

  void setMaxUpdateMs(int ms) {
    m_maxUpdateMsJson = ms;
  }

protected:
  SensorDevice(const char *deviceName, Print &out)
    : m_out(out),
      m_maxUpdateMsJson(m_fieldsJson[1]),
      m_jsonState(m_sensorDeviceJson)
  {
    m_lastUpdateMs = 0;
    
    // Create sensor device JSON object
    m_fieldsJson[0] = Json::String("name", deviceName);
    m_fieldsJson[1] = Json::Int("maxUpdateMs", SENSOR_DEVICE_DEFAULT_MAX_UPDATE_MS);
    m_fieldsJson[2] = Json::Object("sensors", m_sensorsJson);
    m_sensorDeviceJson = Json::Object(m_fieldsJson);
  }

  ~SensorDevice() {
    // WARNING: SensorDevice should never be destructed. It should always be created in the root sketch
  }

  virtual void statusDisconnected() = 0;
  virtual void statusIdle() = 0;
  virtual void statusActive() = 0;
  virtual void statusError() = 0;
  virtual void logError(const char *format, ...) = 0;

private:
  Print &m_out;
  JsonElement &m_maxUpdateMsJson;
  JsonElement m_fieldsJson[3];
  JsonElement m_sensorsJson[S];
  void *m_sensorFuncs[S];
  JsonElement m_sensorDeviceJson;
  JsonState m_jsonState;
  unsigned long m_lastUpdateMs;
};

#endif /* SENSOR_DEVICE_H */
