#ifndef SENSORDEVICE_NANO
#define SENSORDEVICE_NANO

#include <Arduino.h>
#include "SensorDevice.h"


#define NANO_BAUDRATE               115200
#define NANO_LOG_ERROR_MAX_LEN      64
#define NANO_SERIAL_WAIT_DELAY_MS   10
#define NANO_DELAY_BEFORE_START_MS  500

template<size_t S>
class SensorDevice_Nano : public SensorDevice<S> {
public:

  ~SensorDevice_Nano() {}
