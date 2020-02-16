#include <Wire.h>
#include <ArduinoJson.h>
#include "Adafruit_TCS34725.h"
#include "serialstate.h"

// Pick analog outputs, for the UNO these three work well
// use ~560  ohm resistor between Red & Blue, ~1K for green (its brighter)
#define redpin 3
#define greenpin 5
#define bluepin 6
// for a common anode LED, connect the common pin to +5V
// for common cathode, connect the common to ground

// set to false if using a common cathode LED
#define commonAnode true

String name = "Control Panel Color";
String initialState = "{name: '" + name + "', devices: {colorFinder: {}}}";
DynamicJsonDocument state(1024);
SerialState serialState(&state, initialState);
JsonObject devices = state["devices"];
JsonObject colorFinder = devices["colorFinder"];
JsonObject redRanges = colorFinder["redRanges"];
JsonObject greenRanges = colorFinder["greenRanges"];
JsonObject blueRanges = colorFinder["blueRanges"];
JsonObject yellowRanges = colorFinder["yellowRanges"];

// our RGB -> eye-recognized gamma color
byte gammatable[256];

int lastRed = 0, lastGreen = 0, lastBlue = 0;
String lastResultColor = "";
String resultColor = " ";

Adafruit_TCS34725 tcs = Adafruit_TCS34725(TCS34725_INTEGRATIONTIME_50MS, TCS34725_GAIN_4X);

void setup() {
  serialState.init();
  //Serial.println("Color View Test!");

  //Range defaults
  redRanges["redHigh"] = 160;
  redRanges["redlow"] = 150;
  redRanges["greenHigh"] = 55;
  redRanges["greenLow"] = 45;
  redRanges["blueHigh"] = 55;
  redRanges["blueLow"] = 45;

  greenRanges["redHigh"] = 85;
  greenRanges["redlow"] = 75;
  greenRanges["greenHigh"] = 100;
  greenRanges["greenLow"] = 90;
  greenRanges["blueHigh"] = 75;
  greenRanges["blueLow"] = 65;

  blueRanges["redHigh"] = 60;
  blueRanges["redlow"] = 50;
  blueRanges["greenHigh"] = 90;
  blueRanges["greenLow"] = 80;
  blueRanges["blueHigh"] = 110;
  blueRanges["blueLow"] = 100;

  yellowRanges["redHigh"] = 120;
  yellowRanges["redlow"] = 110;
  yellowRanges["greenHigh"] = 90;
  yellowRanges["greenLow"] = 80;
  yellowRanges["blueHigh"] = 50;
  yellowRanges["blueLow"] = 40;
  
  if (tcs.begin()) {
    //Serial.println("Found sensor");
  } else {
    Serial.println("No TCS34725 found ... check your connections");
    while (1); // halt!
  }

  // use these three pins to drive an LED
#if defined(ARDUINO_ARCH_ESP32)
  ledcAttachPin(redpin, 1);
  ledcSetup(1, 12000, 8);
  ledcAttachPin(greenpin, 2);
  ledcSetup(2, 12000, 8);
  ledcAttachPin(bluepin, 3);
  ledcSetup(3, 12000, 8);
#else
  pinMode(redpin, OUTPUT);
  pinMode(greenpin, OUTPUT);
  pinMode(bluepin, OUTPUT);
#endif

  // thanks PhilB for this gamma table!
  // it helps convert RGB colors to what humans see
  for (int i = 0; i < 256; i++) {
    float x = i;
    x /= 255;
    x = pow(x, 2.5);
    x *= 255;

    if (commonAnode) {
      gammatable[i] = 255 - x;
    } else {
      gammatable[i] = x;
    }
  }
}

void loop() {
  serialState.handleSerial();

  float currentRed, currentGreen, currentBlue;

  tcs.setInterrupt(false);  // turn on LED

  delay(60);  // takes 50ms to read

  tcs.getRGB(&currentRed, &currentGreen, &currentBlue);

  tcs.setInterrupt(true);  // turn off LED

  //Serial.print("R:\t"); Serial.print(int(currentRed));
  //Serial.print("\tG:\t"); Serial.print(int(currentGreen));
  //Serial.print("\tB:\t"); Serial.print(int(currentBlue));

  //Serial.print("\n");

  if ((float)lastRed != currentRed || (float)lastGreen != currentGreen || (float)lastBlue != currentBlue) {
    lastRed = (int)currentRed;
    lastGreen = (int)currentGreen;
    lastBlue = (int)currentBlue;

    findColor(currentRed, currentGreen, currentBlue);
  }
}

void findColor(float red, float green, float blue) {
  int  R = (int)red;
  int  G = (int)green;
  int  B = (int)blue;
  resultColor = "No Color";

  if (R <= greenRanges["redHigh"] && R >= greenRanges["redLow"] && G <= greenRanges["greenHigh"] && G >= greenRanges["greenLow"] && B <= greenRanges["blueHigh"] && B >= greenRanges["blueLow"]) {
    resultColor = "GREEN";
  } else if (R <= yellowRanges["redHigh"] && R >= yellowRanges["redLow"] && G <= yellowRanges["greenHigh"] && G >= yellowRanges["greenLow"] && B <= yellowRanges["blueHigh"] && B >= yellowRanges["blueLow"]) {
    resultColor = "YELLOW";
  } else if (R <= redRanges["redHigh"] && R >= redRanges["redLow"] && G <= redRanges["greenHigh"] && G >= redRanges["greenLow"] && B <= redRanges["blueHigh"] && B >= redRanges["blueLow"]) {
    resultColor = "RED";
  } else if (R <= blueRanges["redHigh"] && R >= blueRanges["redLow"] && G <= blueRanges["greenHigh"] && G >= blueRanges["greenLow"] && B <= blueRanges["blueHigh"] && B >= blueRanges["blueLow"]) {
    resultColor = "BLUE";
  }

  if (resultColor != lastResultColor) {
    lastResultColor = resultColor;
    Serial.println(resultColor);
    serialState.updateField(colorFinder, "color", resultColor);
    serialState.sendState();
    //Serial.println(resultColor);
  }
}
