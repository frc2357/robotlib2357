#include "SensorDevice_Seeed_XIAO_RP2040.h"

#define SENSOR_DEVICE_XIAO_RP2040_SERIAL_PREAMBLE        "|||||"
#define SENSOR_DEVICE_XIAO_RP2040_SERIAL_WAIT_DELAY_MS   10
#define SENSOR_DEVICE_XIAO_RP2040_DELAY_BEFORE_START_MS  500

void SensorDevice_Seeed_XIAO_RP2040::begin() {
  pinMode(XIAO_RP2040_POWER_LED, OUTPUT);
  digitalWrite(XIAO_RP2040_POWER_LED, HIGH);
  m_pixel.begin();

  Serial.begin(XIAO_RP2040_BAUDRATE);

  // Wait for the usb connection before proceeding
  while (!Serial) {
    statusDisconnected();
    delay(SENSOR_DEVICE_XIAO_RP2040_SERIAL_WAIT_DELAY_MS);
  }
  delay(SENSOR_DEVICE_XIAO_RP2040_DELAY_BEFORE_START_MS);

  SensorDevice::begin();
}

void SensorDevice_Seeed_XIAO_RP2040::update() {
  SensorDevice::update();
  statusIdle();
}

void SensorDevice_Seeed_XIAO_RP2040::statusDisconnected() {
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

void SensorDevice_Seeed_XIAO_RP2040::statusIdle() {
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

void SensorDevice_Seeed_XIAO_RP2040::statusActive() {
  unsigned long cycleMillis = millis() % 500;

  if (cycleMillis < 250) {
    float factor = ((float)cycleMillis) / 250.0;
    setPixel(m_pixel.Color(0, 90 * factor, 0));
  } else {
    float factor = ((float)500 - cycleMillis) / 250.0;
    setPixel(m_pixel.Color(0, 90 * factor, 0));
  }
}

void SensorDevice_Seeed_XIAO_RP2040::statusError() {
  unsigned long cycleMillis = millis() % 1000;

  if (cycleMillis < 500) {
    setPixel(m_pixel.Color(255, 0, 0));
  } else {
    setPixel(m_pixel.Color(0, 0, 0));
  }
}

void SensorDevice_Seeed_XIAO_RP2040::setPixel(uint32_t color) {
  if (color != m_pixelLastColor) {
    m_pixel.clear();
    m_pixel.setPixelColor(0, color);
    m_pixel.show();
    m_pixelLastColor = color;
  }
}

void SensorDevice_Seeed_XIAO_RP2040::writeRaw(const char *message) {
  Serial.println(message);
}
