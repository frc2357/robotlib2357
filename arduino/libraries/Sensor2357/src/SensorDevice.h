#ifndef SENSOR_DEVICE_H
#define SENSOR_DEVICE_H

#include <Arduino.h>
#include <JsonState.h>

#define SENSOR_DEVICE_DEFAULT_MAX_UPDATE_MS       1000
#define SENSOR_DEVICE_SERIAL_PREAMBLE             "|||||"
#define SENSOR_DEVICE_SENSOR_MAX_FIELD_COUNT      3
#define SENSOR_DEVICE_ERROR_MAX_LENGTH            64

#define SENSOR_DEVICE_FIELD_NAME                  0
#define SENSOR_DEVICE_FIELD_ERROR                 1
#define SENSOR_DEVICE_FIELD_MAX_UPDATE_MS         2
#define SENSOR_DEVICE_FIELD_SENSORS               3
#define SENSOR_DEVICE_FIELD_COUNT                 4

typedef int (*intSensorFunc_T)();
typedef long (*longSensorFunc_T)();
typedef float (*floatSensorFunc_T)();
typedef double (*doubleSensorFunc_T)();
typedef bool (*boolSensorFunc_T)();
typedef const char *(*stringSensorFunc_T)();
typedef long (*longArraySensorFunc_T)(size_t index);
typedef double (*doubleArraySensorFunc_T)(size_t index);

template <size_t S>
class SensorDevice {
public:
  virtual void initSensor(const char *name, intSensorFunc_T sensorFunc, long minValue, long maxValue) {
  }

  virtual void initSensor(const char *name, longSensorFunc_T sensorFunc, long minValue, long maxValue) {
  }

  virtual void initSensor(const char *name, floatSensorFunc_T sensorFunc, double minValue, double maxValue) {
    size_t index = m_sensorInitCount;
    m_sensorInitCount++;
    if (m_sensorInitCount > S) {
      setError("Can't init %d sensors, only %d allocated", m_sensorInitCount, S);
      return;
    }

    long initialValue = sensorFunc();
    m_sensorFieldsJson[index][0] = Json::Int("value", initialValue);
    m_sensorFieldsJson[index][1] = Json::Int("min", minValue);
    m_sensorFieldsJson[index][2] = Json::Int("max", maxValue);
    m_sensorsJson[index] = Json::Object(name, m_sensorFieldsJson[index]);
  }

  virtual void initSensor(const char *name, doubleSensorFunc_T sensorFunc, double minValue, double maxValue) {
  }

  virtual void initSensor(const char *name, boolSensorFunc_T sensorFunc) {
  }

  virtual void initSensor(const char *name, stringSensorFunc_T sensorFunc) {
  }

  virtual void initSensor(const char *name, longArraySensorFunc_T sensorFunc, size_t length) { }

  virtual void initSensor(const char *name, doubleArraySensorFunc_T sensorFunc, size_t length) {
  }

  virtual void begin() {
    if (m_sensorInitCount < S) {
      setError("WARN: %d sensors allocated, only %d initialized.", S, m_sensorInitCount);
    }
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

  virtual int getMaxUpdateMs() {
    return m_fieldsJson[SENSOR_DEVICE_FIELD_MAX_UPDATE_MS].asInt();
  }

  virtual void setMaxUpdateMs(int ms) {
    m_fieldsJson[SENSOR_DEVICE_FIELD_MAX_UPDATE_MS] = ms;
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
  SensorDevice(const char *deviceName, Print &out)
    : m_out(out),
      m_jsonState(m_sensorDeviceJson)
  {
    m_lastUpdateMs = 0;
    m_sensorInitCount = 0;
    
    m_fieldsJson[SENSOR_DEVICE_FIELD_NAME] = Json::String("name", deviceName);
    m_fieldsJson[SENSOR_DEVICE_FIELD_ERROR] = Json::String("error", "", SENSOR_DEVICE_ERROR_MAX_LENGTH);
    m_fieldsJson[SENSOR_DEVICE_FIELD_MAX_UPDATE_MS] = Json::Int("maxUpdateMs", SENSOR_DEVICE_DEFAULT_MAX_UPDATE_MS);
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

private:
  Print &m_out;
  JsonElement m_fieldsJson[SENSOR_DEVICE_FIELD_COUNT];
  JsonElement m_sensorFieldsJson[S][SENSOR_DEVICE_SENSOR_MAX_FIELD_COUNT];
  JsonElement m_sensorsJson[S];
  void *m_sensorFuncs[S];
  JsonElement m_sensorDeviceJson;
  JsonState m_jsonState;
  unsigned long m_lastUpdateMs;
  size_t m_sensorInitCount;
};

#endif /* SENSOR_DEVICE_H */
