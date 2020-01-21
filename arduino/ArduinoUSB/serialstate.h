#ifndef SERIALSTATE_H
#define SERIALSTATE_H

#define USB_BAUDRATE     115200
#define JSON_STRING_LEN  512
#define JSON_INT_LEN     64
#define JSON_DOUBLE_LEN  128
#define JSON_BOOL_LEN    64

#include "Arduino.h"
#include <ArduinoJson.h>

class SerialState {
  public:
    SerialState(DynamicJsonDocument *state, String initialState);

    void init();
    void handleSerial();
    void sendState();
    void updateField(JsonObject stateObject, String fieldName, String value);
    void updateField(JsonObject stateObject, String fieldName, int value);
    void updateField(JsonObject stateObject, String fieldName, double value);
    void updateField(JsonObject stateObject, String fieldName, bool value);
    void updateObject(JsonObject stateObject, JsonObject fields);
    void setError(String type, String message);
    void clearError();

  private:

    DynamicJsonDocument* state;

    void readMessage();
};

#endif /* serialstate_h */