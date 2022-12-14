#ifndef SENSOR_DEVICE_H
#define SENSOR_DEVICE_H

#include <Arduino.h>
#include <JsonState.h>
#include "Sensor.h"

#define SENSOR_DEVICE_DEFAULT_MAX_UPDATE_MS       1000
#define SENSOR_DEVICE_SERIAL_PREAMBLE             "||v1||"
#define SENSOR_DEVICE_SENSOR_MAX_FIELD_COUNT      3
#define SENSOR_DEVICE_ERROR_MAX_LENGTH            64

#define SENSOR_DEVICE_FIELD_NAME                  0
#define SENSOR_DEVICE_FIELD_ERROR                 1
#define SENSOR_DEVICE_FIELD_MAX_UPDATE_MS         2
#define SENSOR_DEVICE_FIELD_SENSORS               3
#define SENSOR_DEVICE_FIELD_COUNT                 4

template <size_t S>
class SensorDevice {
public:
  virtual void initSensor(const char *name, intSensorFunc_T sensorFunc, int min, int max) {
    initIntMinMax(name, sensorFunc, updateIntSensor, min, max);
  }

  virtual void initSensor(const char *name, longSensorFunc_T sensorFunc, long min, long max) {
    initIntMinMax(name, sensorFunc, updateLongSensor, min, max);
  }

  virtual void initSensor(const char *name, floatSensorFunc_T sensorFunc, float min, float max) {
    initFloatMinMax(name, sensorFunc, updateFloatSensor, min, max);
  }

  virtual void initSensor(const char *name, doubleSensorFunc_T sensorFunc, double min, double max) {
    initFloatMinMax(name, sensorFunc, updateDoubleSensor, min, max);
  }

  virtual void initSensor(const char *name, boolSensorFunc_T sensorFunc) {
    initBool(name, sensorFunc);
  }

  virtual void initSensor(const char *name, stringSensorFunc_T sensorFunc, size_t length) {
    initString(name, sensorFunc, length);
  }

  virtual void initSensor(const char *name, intArraySensorFunc_T sensorFunc, size_t length) {
    initIntArray(name, sensorFunc, updateIntArraySensor, length);
  }

  virtual void initSensor(const char *name, longArraySensorFunc_T sensorFunc, size_t length) {
    initIntArray(name, sensorFunc, updateLongArraySensor, length);
  }

  virtual void initSensor(const char *name, floatArraySensorFunc_T sensorFunc, size_t length) {
    initFloatArray(name, sensorFunc, updateFloatArraySensor, length);
  }

  virtual void initSensor(const char *name, doubleArraySensorFunc_T sensorFunc, size_t length) {
    initFloatArray(name, sensorFunc, updateDoubleArraySensor, length);
  }

  virtual void begin() {
    if (m_sensorInitCount < S) {
      setError("WARN: %d sensors allocated, only %d initialized.", S, m_sensorInitCount);
    }
    sendState(true);
  }

  void sendState(bool fullState) {
    m_out.print(SENSOR_DEVICE_SERIAL_PREAMBLE);
    m_jsonState.printJson(m_out, !fullState);
    m_jsonState.clearChanged();
    m_out.println();
    m_out.flush();
    m_lastUpdateMs = millis();
  }

  virtual void update() {
    bool maxUpdateReached = millis() > m_lastUpdateMs + getMaxUpdateMs();
    bool sendFullState = false;

    // Update all the sensor values
    for (int i = 0; i < S; i++) {
      m_sensors[i].update();
    }

    if (m_in.available()) {
      String line = m_in.readString();

      if (line[0] == '{' && line[1] == '}') {
        sendFullState = true;
      } else {
        m_jsonState.updateFromJson(line.c_str());
        sendFullState = true;
      }
    }

    if (sendFullState || (maxUpdateReached && m_jsonState.hasChanged())) {
      sendState(sendFullState);
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
  SensorDevice(const char *deviceName, Print &out, Stream &in)
    : m_out(out),
      m_in(in),
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
  Stream &m_in;
  JsonElement m_fieldsJson[SENSOR_DEVICE_FIELD_COUNT];
  JsonElement m_sensorFieldsJson[S][SENSOR_DEVICE_SENSOR_MAX_FIELD_COUNT];
  JsonElement m_sensorsJson[S];
  Sensor m_sensors[S];
  JsonElement m_sensorDeviceJson;
  JsonState m_jsonState;
  unsigned long m_lastUpdateMs;
  size_t m_sensorInitCount;

  size_t allocateIndex() {
    size_t index = m_sensorInitCount;
    m_sensorInitCount++;
    if (m_sensorInitCount > S) {
      setError("Can't init %d sensors, only %d allocated", m_sensorInitCount, S);
      return -1;
    }
    return index;
  }

  void initIntMinMax(const char *name, SensorFunc sensorFunc, updateSensor_T updateFunc, long min, long max) {
    size_t index = allocateIndex();
    if (index == -1) {
      return;
    }

    m_sensorFieldsJson[index][0] = Json::Int("value", 0);
    m_sensorFieldsJson[index][1] = Json::Int("min", min);
    m_sensorFieldsJson[index][2] = Json::Int("max", max);
    m_sensorsJson[index] = Json::Object(name, m_sensorFieldsJson[index]);
    m_sensors[index].init(&m_sensorsJson[index], sensorFunc, updateFunc);
  }

  void initFloatMinMax(const char *name, SensorFunc sensorFunc, updateSensor_T updateFunc, double min, double max) {
    size_t index = allocateIndex();
    if (index == -1) {
      return;
    }

    m_sensorFieldsJson[index][0] = Json::Float("value", 0.0);
    m_sensorFieldsJson[index][1] = Json::Float("min", min);
    m_sensorFieldsJson[index][2] = Json::Float("max", max);
    m_sensorsJson[index] = Json::Object(name, m_sensorFieldsJson[index]);
    m_sensors[index].init(&m_sensorsJson[index], sensorFunc, updateFunc);
  }

  void initBool(const char *name, SensorFunc sensorFunc) {
    size_t index = allocateIndex();
    if (index == -1) {
      return;
    }

    m_sensorFieldsJson[index][0] = Json::Boolean("value", false);
    m_sensorsJson[index] = Json::Object(name, m_sensorFieldsJson[index]);
    m_sensors[index].init(&m_sensorsJson[index], sensorFunc, updateBoolSensor);
  }

  void initString(const char *name, SensorFunc sensorFunc, int length) {
    size_t index = allocateIndex();
    if (index == -1) {
      return;
    }

    m_sensorFieldsJson[index][0] = Json::String("value", "", length);
    m_sensorsJson[index] = Json::Object(name, m_sensorFieldsJson[index]);
    m_sensors[index].init(&m_sensorsJson[index], sensorFunc, updateStringSensor);
  }

  void initIntArray(const char *name, SensorFunc sensorFunc, updateSensor_T updateFunc, size_t length) {
    size_t index = allocateIndex();
    if (index == -1) {
      return;
    }

    JsonElement *arrayElements = new JsonElement[length];
    for (int i = 0; i < length; i++) {
      arrayElements[i] = Json::Int(0);
    }

    m_sensorFieldsJson[index][0] = Json::Array("array", arrayElements, length);
    m_sensorFieldsJson[index][1] = Json::Float("length", length);
    m_sensorsJson[index] = Json::Object(name, m_sensorFieldsJson[index]);
    m_sensors[index].init(&m_sensorsJson[index], sensorFunc, updateFunc);
  }

  void initFloatArray(const char *name, SensorFunc sensorFunc, updateSensor_T updateFunc, size_t length) {
    size_t index = allocateIndex();
    if (index == -1) {
      return;
    }

    JsonElement *arrayElements = new JsonElement[length];
    for (int i = 0; i < length; i++) {
      arrayElements[i] = Json::Float(0.0);
    }

    m_sensorFieldsJson[index][0] = Json::Array("array", arrayElements, length);
    m_sensorFieldsJson[index][1] = Json::Float("length", length);
    m_sensorsJson[index] = Json::Object(name, m_sensorFieldsJson[index]);
    m_sensors[index].init(&m_sensorsJson[index], sensorFunc, updateFunc);
  }
};

#endif /* SENSOR_DEVICE_H */
