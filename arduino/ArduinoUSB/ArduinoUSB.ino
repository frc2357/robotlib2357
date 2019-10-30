/*
 * ArduinoUSB
 * 
 * This is a firmware for Arduino microcontrollers communicating
 * with the RoboRIO over USB
 * 
 * The primary purpose of these Arduinos is to:
 *  - Connect to sensors using established Arduino code
 *  - Filter and debounce data from those sensors
 *  - Send messages to the RoboRIO when sensor data is relevant
 *
 * USB Protocol
 *  - Upon startup, the arduino will send its initial state in JSON format
 *  - The USB host may send a partial JSON state which will be updated and a full state returned.
 */

#include <ArduinoJson.h>

#define USB_BAUDRATE 115200

String name = "Test Devices";
String initialState = "{name: '" + name + "', devices: {}}";
DynamicJsonDocument state(1024);

void setup() {
  deserializeJson(state, initialState);
  Serial.begin(USB_BAUDRATE);

  sendState();
}

void loop() {
  if (Serial.available() > 0) {
    readMessage();
  }
}

void sendState() {
  serializeJson(state, Serial);
  Serial.print("\n\r");
}

void readMessage() {
  StaticJsonDocument<1024> message;
  DeserializationError readErr = deserializeJson(message, Serial);

  if (DeserializationError::Ok != readErr) {
    setError("JSON Parse", readErr.c_str());
    sendState();
    return;
  }

  JsonObject fields = message.as<JsonObject>();

  updateObject(state.as<JsonObject>(), fields);
  clearError();
  sendState();
}

void updateObject(JsonObject stateObject, JsonObject fields) {
  for (JsonPair kv : fields) {
    const String key = kv.key().c_str();
    const JsonVariant value = kv.value();
    JsonVariant existingValue = stateObject[key];

    if (value.is<JsonObject>() &&
        existingValue.is<JsonObject>() &&
        !existingValue.isNull()) {

      // Only replace the fields given by recursing a level deeper.
      updateObject(existingValue, value);
    } else {
      // Overwrite the entire object
      stateObject[key] = value;
    }
  }
}

void setError(String type, String message) {
  StaticJsonDocument<512> error;
  deserializeJson(error, "{type: '" + type + "', message: '" + message + "'}");
  state["error"] = error.as<JsonObject>();
}

void clearError() {
  state.as<JsonObject>().remove("error");
}

