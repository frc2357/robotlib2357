#include "Adafruit_VL53L0X.h"

#define VL53L0X_MAX_DISTANCE 2000
#define RINGBUFFER_LENGTH 6

Adafruit_VL53L0X lox = Adafruit_VL53L0X();
int ringBuffer[RINGBUFFER_LENGTH];
int ringBufferIndex = 0;
int lastDistance = -1;
int currentDistance = -1;

void setup() {
  initRingBuffer();

  Serial.begin(115200);

  // wait until serial port opens for native USB devices
  while (! Serial) {
    delay(1);
  }
  
  Serial.println("Adafruit VL53L0X test");
  if (!lox.begin()) {
    Serial.println(F("Failed to boot VL53L0X"));
    while(1);
  }
  // power 
  Serial.println(F("VL53L0X API Simple Ranging example\n\n")); 
}

void loop() {
  readDistance();
  currentDistance = getAverage();

  if (currentDistance != lastDistance) {
    Serial.print("Distance (mm): ");
    Serial.println(currentDistance);
    lastDistance = currentDistance;
  }
}

int getAverage() {
  int validValueCount = 0;
  int sum = 0;

  for (int i = 0; i < RINGBUFFER_LENGTH; i++) {
    if (ringBuffer[i] > -1) {
      validValueCount++;
      sum += ringBuffer[i];
    }
  }

  if (validValueCount == 0) {
    return -1;
  }
  return sum / validValueCount;
}

void initRingBuffer() {
  for (int i = 0; i < RINGBUFFER_LENGTH; i++) {
    ringBuffer[i] = -1;
  }
}

int readDistance() {
  VL53L0X_RangingMeasurementData_t measure;
    
  lox.rangingTest(&measure, false); // pass in 'true' to get debug data printout!

  int distance = measure.RangeMilliMeter;
  if (measure.RangeStatus == 4 || // phase failures make incorrect data
      distance > VL53L0X_MAX_DISTANCE) {  // filter out 8191 incorrect distances
    distance = -1;
  }

  ringBuffer[ringBufferIndex] = distance;
  ringBufferIndex = ringBufferIndex < (RINGBUFFER_LENGTH - 1) ? ringBufferIndex + 1 : 0;

  return distance;
}
