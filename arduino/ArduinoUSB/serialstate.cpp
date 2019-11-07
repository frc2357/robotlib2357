#include "serialstate.h"

SerialState::SerialState(DynamicJsonDocument *state, String initialState) {
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
