#ifndef SERIALSTATE_H
#define SERIALSTATE_H

#define USB_BAUDRATE 115200

#include "Arduino.h"
#include <ArduinoJson.h>

class SerialState {
  public:
    SerialState(DynamicJsonDocument *state, String initialState);

    void init();
    void handleSerial();
    void sendState();
    void updateObject(JsonObject stateObject, JsonObject fields);
    void setError(String type, String message);
    void clearError();

  private:

    DynamicJsonDocument* state;

    void readMessage();
};

#endif /* serialstate_h */
