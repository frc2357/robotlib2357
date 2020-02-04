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

#include "serialstate.h"

String name = "Test Devices";
String initialState = "{name: '" + name + "', devices: {example: {}}}";

DynamicJsonDocument state(1024);
SerialState serialState(&state, initialState);

JsonObject laserToF = state["devices"]["example"];
double count = 0;

void setup() {
  serialState.init();
}

void loop() {
  serialState.handleSerial();
  delay(500);

  count += 0.5;

  serialState.updateField(laserToF, "count", count);
  serialState.sendState();
}

