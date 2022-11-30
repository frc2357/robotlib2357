#include "SensorDevice.h"

void SensorDevice::begin() {
}

void SensorDevice::update() {
  unsigned long now = millis();
  // TODO: Add a device setting for the update ms
  if (now > m_lastUpdateMs + SENSOR_DEVICE_DEFAULT_UPDATE_MS) {
    JsonState state(m_deviceJson);
    m_out.print(SENSOR_DEVICE_SERIAL_PREAMBLE);
    state.printJson(m_out);
    m_out.println();
    m_lastUpdateMs = now;
  }

  delay(SENSOR_DEVICE_LOOP_DELAY_MS);
}
