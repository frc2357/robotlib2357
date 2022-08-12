/**
 * @file ExampleSensor.ino
 * @author System Meltdown FRC 2357 (rprobotics.2357@systemmeltdown.com)
 * @brief Example Arduino Sensor Sketch
 * @version 0.1
 * @date 2022-08-11
 * 
 * @copyright Copyright (c) 2022
 * 
 */
#include <JsonElement.h>

#define USB_BAUDRATE     115200

// This is an example of how to set up sensor values
// This sends an array of values at the specified update rate in milliseconds

JsonElement values[] = {
  Json::Float(0.0),
  Json::Float(0.0),
  Json::Float(0.0),
};

JsonElement deviceFields[] = {
  Json::String("name", "ExampleSensor"),
  Json::String("version", "1.0.0"),
  Json::Int("maxUpdateMs", 100),     // Example: the maximum frequency to send updates
  Json::Array("values", values),     // Example set of values
};
JsonElement device = Json::Object(deviceFields);

JsonState state(device);

void setup() {
  pinMode(LED_BUILTIN, OUTPUT);
  digitalWrite(LED_BUILTIN, LOW);

  Serial.begin(USB_BAUDRATE);

  while (!Serial) {}
  digitalWrite(LED_BUILTIN, HIGH);

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

  values[0] = (float)millis();
  values[1] = (float)(millis() / 2);
  values[2] = (float)(millis() / 10);

  if (state.hasChanged()) {
    state.printJson(Serial, true);
    Serial.println();
  }

  state.clearChanged();
  delay(device["maxUpdateMs"].asInt());
}
