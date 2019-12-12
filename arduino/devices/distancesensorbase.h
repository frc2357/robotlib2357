#ifndef DISTANCESENSORBASE_H
#define DISTANCESENSORBASE_H

#define USB_BAUDRATE     115200
#define JSON_STRING_LEN  512
#define JSON_INT_LEN     64
#define JSON_DOUBLE_LEN  128
#define JSON_BOOL_LEN    64

#include "Arduino.h"
#include <ArduinoJson.h>

class distancesensorbase {
  public:
    double getDistance();

  private:
    double distance;
}


#endif