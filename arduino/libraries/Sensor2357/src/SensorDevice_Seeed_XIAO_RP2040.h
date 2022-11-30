#ifndef SENSORDEVICE_SEEED_XIAO_RP2040_h
#define SENSORDEVICE_SEEED_XIAO_RP2040_h

#include "Arduino.h"
#include <Adafruit_NeoPixel.h>
#include "SensorDevice.h"

#define XIAO_RP2040_BAUDRATE   115200
#define XIAO_RP2040_POWER_LED  11
#define XIAO_RP2040_RGB_PIN    12

class SensorDevice_Seeed_XIAO_RP2040 : public SensorDevice {
public:
  template<size_t S>
  SensorDevice_Seeed_XIAO_RP2040(const char *deviceName, JsonElement (&sensors)[S]) :
    SensorDevice(deviceName, sensors, Serial),
    m_pixel(1, XIAO_RP2040_RGB_PIN, NEO_GRB + NEO_KHZ800),
    m_pixelLastColor(m_pixel.Color(0, 0, 0))
  {}

  ~SensorDevice_Seeed_XIAO_RP2040() {
    Serial.println("WARNING: SensorDevice must be created in root level sketch file (before setup() and loop() functions)");
  }

  virtual void begin();
  virtual void update();

protected:
  virtual void statusDisconnected();
  virtual void statusIdle();
  virtual void statusActive();
  virtual void statusError();
  virtual void writeRaw(const char *message);

private:
  Adafruit_NeoPixel m_pixel;
  uint32_t m_pixelLastColor;

  void setPixel(uint32_t color);
};

#endif /* SENSORDEVICE_SEEED_XIAO_RP2040_h */
