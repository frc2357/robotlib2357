#ifndef SENSORDEVICE_SEEED_XIAO_RP2040_H
#define SENSORDEVICE_SEEED_XIAO_RP2040_H

#include <Arduino.h>
#include <Adafruit_NeoPixel.h>
#include "SensorDevice.h"

#define XIAO_RP2040_BAUDRATE               115200
#define XIAO_RP2040_POWER_LED              11
#define XIAO_RP2040_RGB_PIN                12
#define XIAO_RP2040_LOG_ERROR_MAX_LEN      64
#define XIAO_RP2040_SERIAL_WAIT_DELAY_MS   10
#define XIAO_RP2040_DELAY_BEFORE_START_MS  500

template<size_t S>
class SensorDevice_Seeed_XIAO_RP2040 : public SensorDevice<S> {
public:
  SensorDevice_Seeed_XIAO_RP2040(const char *deviceName)
    : SensorDevice<S>(deviceName, Serial, Serial),
      m_pixel(1, XIAO_RP2040_RGB_PIN, NEO_GRB + NEO_KHZ800),
      m_pixelLastColor(m_pixel.Color(0, 0, 0))
  {}

  ~SensorDevice_Seeed_XIAO_RP2040() {}

  virtual void begin() {
    pinMode(XIAO_RP2040_POWER_LED, OUTPUT);
    digitalWrite(XIAO_RP2040_POWER_LED, HIGH);
    m_pixel.begin();

    Serial.begin(XIAO_RP2040_BAUDRATE);

    // Wait for the usb connection before proceeding
    while (!Serial) {
      statusDisconnected();
      delay(XIAO_RP2040_SERIAL_WAIT_DELAY_MS);
    }
    delay(XIAO_RP2040_DELAY_BEFORE_START_MS);

    SensorDevice<S>::begin();
  }

  virtual void update() {
    SensorDevice<S>::update();
    statusIdle();
  }

protected:
  virtual void statusDisconnected() {
    unsigned long cycleMillis = millis() % 2500;

    if (cycleMillis < 500) {
      float factor = ((float)cycleMillis) / 500.0;
      setPixel(m_pixel.Color(80 * factor, 15 * factor, 0));
    } else if (cycleMillis < 1000) {
      setPixel(m_pixel.Color(80, 15, 0));
    } else if (cycleMillis < 1500) {
      float factor = ((float)1500 - cycleMillis) / 500.0;
      setPixel(m_pixel.Color(80 * factor, 15 * factor, 0));
    } else {
      setPixel(m_pixel.Color(0, 0, 0));
    }
  }

  virtual void statusIdle() {
    unsigned long cycleMillis = millis() % 5000;

    if (cycleMillis < 250) {
      float factor = ((float)250 - cycleMillis) / 250.0;
      setPixel(m_pixel.Color(0, 50 * factor, 0));
    } else if (cycleMillis < 500) {
      float factor = ((float)cycleMillis - 250) / 250.0;
      setPixel(m_pixel.Color(0, 50 * factor, 0));
    } else {
      setPixel(m_pixel.Color(0, 50, 0));
    }
  }

  virtual void statusActive() {
    unsigned long cycleMillis = millis() % 500;

    if (cycleMillis < 250) {
      float factor = ((float)cycleMillis) / 250.0;
      setPixel(m_pixel.Color(0, 90 * factor, 0));
    } else {
      float factor = ((float)500 - cycleMillis) / 250.0;
      setPixel(m_pixel.Color(0, 90 * factor, 0));
    }
  }

  virtual void statusError() {
    unsigned long cycleMillis = millis() % 1000;

    if (cycleMillis < 500) {
      setPixel(m_pixel.Color(255, 0, 0));
    } else {
      setPixel(m_pixel.Color(0, 0, 0));
    }
  }

private:
  Adafruit_NeoPixel m_pixel;
  uint32_t m_pixelLastColor;

  void setPixel(uint32_t color) {
    if (color != m_pixelLastColor) {
      m_pixel.clear();
      m_pixel.setPixelColor(0, color);
      m_pixel.show();
      m_pixelLastColor = color;
    }
  }
};

#endif /* SENSORDEVICE_SEEED_XIAO_RP2040_H */
