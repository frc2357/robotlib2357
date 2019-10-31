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


class SerialState {
  public:
    SerialState(DynamicJsonDocument* state, String initialState);

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


String name = "Test Devices";
String initialState = "{name: '" + name + "', devices: {}}";

DynamicJsonDocument state(1024);
SerialState serialState(&state, initialState);

void setup() {
  serialState.init();
}

void loop() {
  serialState.handleSerial();
}

SerialState::SerialState(DynamicJsonDocument* state, String initialState) {
  deserializeJson(*state, initialState);
  this->state = state;
}

void SerialState::init() {
  Serial.begin(USB_BAUDRATE);
  sendState();
}

void SerialState::handleSerial() {
  if (Serial.available() > 0) {
    readMessage();
  }
}

void SerialState::sendState() {
  serializeJson(*this->state, Serial);
  Serial.print("\n\r");
}

void SerialState::readMessage() {
  StaticJsonDocument<1024> message;
  DeserializationError readErr = deserializeJson(message, Serial);

  if (DeserializationError::Ok != readErr) {
    setError("JSON Parse", readErr.c_str());
    sendState();
    return;
  }

  JsonObject fields = message.as<JsonObject>();

  updateObject(this->state->as<JsonObject>(), fields);
  clearError();
  sendState();
}

void SerialState::updateObject(JsonObject stateObject, JsonObject fields) {
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

void SerialState::setError(String type, String message) {
  StaticJsonDocument<512> error;
  deserializeJson(error, "{type: '" + type + "', message: '" + message + "'}");
  (*this->state)["error"] = error.as<JsonObject>();
}

void SerialState::clearError() {
  this->state->as<JsonObject>().remove("error");
}

