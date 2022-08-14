/*
 * Test
 */
#include "JsonElement.h"

#define USB_BAUDRATE     115200

JsonElement sensorValues[] = {
  Json::Float(25.52),
  Json::Float(24.78),
  Json::Float(26.21),
};

JsonElement deviceFields[] = {
  Json::String("name", "device name"),
  Json::String("version", "1.0.0"),
  Json::Boolean("flag", false),
  Json::Int("tick", 0),
  Json::Array("sensorValues", sensorValues),
};
JsonElement device = Json::Object(deviceFields);

JsonState state(device);

void setup() {
  Serial.begin(USB_BAUDRATE);

  while (!Serial) {}

  state.printJson(Serial);
  Serial.println();
}

void loop() {
  if (Serial.available() > 0) {
    Serial.println("--- reading input...");
    String line = Serial.readString();

    if (line == "{}") {
      Serial.println("--- printing complete state...");
      state.printJson(Serial);
      Serial.println();
    } else {
      Serial.println("--- updating...");
      state.updateFromJson(line.c_str());
      Serial.println("--- updated");
    }
  }

  device["tick"] = device["tick"].asLong() + 1;
  sensorValues[1] = sensorValues[1].asDouble() * 1.001;
  sensorValues[2] = sensorValues[2].asDouble() * 0.999;

  state.printJson(Serial, true);
  Serial.println();

  state.clearChanged();

  delay(1000);
}
