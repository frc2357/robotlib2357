#ifndef SENSORDEVICE_ADAFRUIT_RP2040_SCORPIO_H
#define SENSORDEVICE_ADAFRUIT_RP2040_SCORPIO_H

#include <Arduino.h>
#include <Adafruit_NeoPixel.h>
#include "SensorDevice.h"

#define SCORPIO_BAUDRATE               115200
#define SCORPIO_POWER_LED              13
#define SCORPIO_RGB_PIN                4
#define SCORPIO_LOG_ERROR_MAX_LEN      64
#define SCORPIO_SERIAL_WAIT_DELAY_MS   10
#define SCORPIO_DELAY_BEFORE_START_MS  500

template<size_t S>
class SensorDevice_Adafruit_RP2040_Scorpio : public SensorDevice<S> {
public:
  SensorDevice_Adafruit_RP2040_Scorpio(const char *deviceName)
    : SensorDevice<S>(deviceName, Serial, Serial),
      m_pixel(1, SCORPIO_RGB_PIN, NEO_GRB + NEO_KHZ800),
      m_pixelLastColor(m_pixel.Color(0, 0, 0))
  {}

  ~SensorDevice_Adafruit_RP2040_Scorpio() {}

  virtual void begin() {
    pinMode(SCORPIO_POWER_LED, OUTPUT);
    digitalWrite(SCORPIO_POWER_LED, HIGH);
    m_pixel.begin();

    Serial.begin(SCORPIO_BAUDRATE);

    // Wait for the usb connection before proceeding
    while (!Serial) {
      statusDisconnected();
      delay(SCORPIO_SERIAL_WAIT_DELAY_MS);
    }
    delay(SCORPIO_DELAY_BEFORE_START_MS);

    SensorDevice<S>::begin();
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

#endif /* SENSORDEVICE_ADAFRUIT_RP2040_SCORPIO_H */
