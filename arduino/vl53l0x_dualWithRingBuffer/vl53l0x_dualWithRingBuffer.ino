#include "Adafruit_VL53L0X.h"

// address we will assign if dual sensor is present
#define LOX1_ADDRESS 0x30
#define LOX2_ADDRESS 0x31

// set the pins to shutdown
#define SHT_LOX1 7
#define SHT_LOX2 6

#define VL53L0X_MAX_DISTANCE 2000
#define RINGBUFFER_LENGTH 6

// objects for the vl53l0x
Adafruit_VL53L0X lox1 = Adafruit_VL53L0X();
Adafruit_VL53L0X lox2 = Adafruit_VL53L0X();

// this holds the measurement
VL53L0X_RangingMeasurementData_t measure1;
VL53L0X_RangingMeasurementData_t measure2;

int ringBuffer1[RINGBUFFER_LENGTH];
int ringBufferIndex1 = 0;
int ringBuffer2[RINGBUFFER_LENGTH];
int ringBufferIndex2 = 0;
int lastDistance1 = -1;
int currentDistance1 = -1;
int lastDistance2 = -1;
int currentDistance2 = -1;
int actualDistance1 = -1;
int actualDistance2 = -1;
int TOF1Charactistics[6] = {408, 413, 253, 258, 45, 51};
int TOF2Characteristics[6] = {446, 453, 258, 263, 46, 52};
int midRange[2] = {130, 225};
//int lowMidRange1 = 100;
//int lowMidRange2 = 100;
/*
    Reset all sensors by setting all of their XSHUT pins low for delay(10), then set all XSHUT high to bring out of reset
    Keep sensor #1 awake by keeping XSHUT pin high
    Put all other sensors into shutdown by pulling XSHUT pins low
    Initialize sensor #1 with lox.begin(new_i2c_address) Pick any number but 0x29 and it must be under 0x7F. Going with 0x30 to 0x3F is probably OK.
    Keep sensor #1 awake, and now bring sensor #2 out of reset by setting its XSHUT pin high.
    Initialize sensor #2 with lox.begin(new_i2c_address) Pick any number but 0x29 and whatever you set the first sensor to
 */
void setID() {
  // all reset
  digitalWrite(SHT_LOX1, LOW);    
  digitalWrite(SHT_LOX2, LOW);
  delay(10);
  // all unreset
  digitalWrite(SHT_LOX1, HIGH);
  digitalWrite(SHT_LOX2, HIGH);
  delay(10);

  // activating LOX1 and reseting LOX2
  digitalWrite(SHT_LOX1, HIGH);
  digitalWrite(SHT_LOX2, LOW);

  // initing LOX1
  if(!lox1.begin(LOX1_ADDRESS)) {
    Serial.println(F("Failed to boot first VL53L0X"));
    while(1);
  }
  delay(10);

  // activating LOX2
  digitalWrite(SHT_LOX2, HIGH);
  delay(10);

  //initing LOX2
  if(!lox2.begin(LOX2_ADDRESS)) {
    Serial.println(F("Failed to boot second VL53L0X"));
    while(1);
  }
}

/*void read_dual_sensors() {
  
  lox1.rangingTest(&measure1, false); // pass in 'true' to get debug data printout!
  lox2.rangingTest(&measure2, false); // pass in 'true' to get debug data printout!

  // print sensor one reading
  Serial.print("1: ");
  if(measure1.RangeStatus != 4) {     // if not out of range
    Serial.print(measure1.RangeMilliMeter);
  } else {
    Serial.print("Out of range");
  }
  
  Serial.print(" ");

  // print sensor two reading
  Serial.print("2: ");
  if(measure2.RangeStatus != 4) {
    Serial.print(measure2.RangeMilliMeter);
  } else {
    Serial.print("Out of range");
  }
  
  Serial.println();
}*/

void setup() {
  initRingBuffer1();
  initRingBuffer2();
   
  Serial.begin(115200);

  // wait until serial port opens for native USB devices
  while (! Serial) { delay(1); }

  pinMode(SHT_LOX1, OUTPUT);
  pinMode(SHT_LOX2, OUTPUT);

  Serial.println("Shutdown pins inited...");

  digitalWrite(SHT_LOX1, LOW);
  digitalWrite(SHT_LOX2, LOW);

  Serial.println("Both in reset mode...(pins are low)");
  
  
  Serial.println("Starting...");
  setID();
}

void loop() {
 // read_dual_sensors();

  readDistance(lox1, ringBuffer1, ringBufferIndex1);
  currentDistance1 = getAverage1();

  if (currentDistance1 != lastDistance1) {
    //Serial.print("Distance of 1st TOF(Raw mm): ");
    //Serial.println(currentDistance1);
    lastDistance1 = currentDistance1;
  }

   readDistance(lox2, ringBuffer2, ringBufferIndex2);
   currentDistance2 = getAverage2();
   
   if (currentDistance2 != lastDistance2) {
    //Serial.print("Distance of 2nd TOF(Raw mm): ");
    //Serial.println(currentDistance2);
    lastDistance2 = currentDistance2;
  }

  countCells();
}

int getAverage1() {
  int validValueCount = 0;
  int sum = 0;
  
  for (int i = 0; i < RINGBUFFER_LENGTH; i++) {
    if (ringBuffer1[i] > -1) {
      validValueCount++;
      sum += ringBuffer1[i];
    }
  }

  if (validValueCount == 0) {
    return -1;
  }
  return sum / validValueCount;
}

int getAverage2() {
  int validValueCount = 0;
  int sum = 0;

  for (int i = 0; i < RINGBUFFER_LENGTH; i++) {
    if (ringBuffer2[i] > -1) {
      validValueCount++;
      sum += ringBuffer2[i];
    }
  }
  if (validValueCount == 0) {
    return -1;
  }
  return sum / validValueCount;
}

void initRingBuffer1() {
  for (int i = 0; i < RINGBUFFER_LENGTH; i++) {
    ringBuffer1[i] = -1;
  }
}

void initRingBuffer2() {
  for (int i = 0; i < RINGBUFFER_LENGTH; i++) {
    ringBuffer2[i] = -1;
  }
}

int readDistance(Adafruit_VL53L0X lox, int ringBuffer[RINGBUFFER_LENGTH], int ringBufferIndex) {
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

String findSensor1Status() {
   if(currentDistance1 >= midRange[0] && currentDistance1 <= midRange[1]){
    return "mid";
  }
  if(currentDistance1 >= midRange[0]) {
      return "far";
   }  else {
      return "close"; 
   }
}

String findSensor2Status() {
  if(currentDistance2 >= midRange[0] && currentDistance2 <= midRange[1]){
    return "mid";
  }
  if(currentDistance2 >= midRange[0]) {
      return "far";
  } else {
      return "close"; 
  }
}

void countCells() {
  String sensor1Status = findSensor1Status();
  String sensor2Status = findSensor2Status();
  
  //Serial.println("Sensor1 Status: "+sensor1Status);
  //Serial.println("Sensor2 Status: "+sensor2Status);
  
  if(sensor1Status == "far" && sensor2Status == "far") {
    Serial.println("No cell detected");
  }
  
  if(sensor1Status == "close" && sensor2Status == "close" || sensor1Status == "close" && sensor2Status == "mid" ||  sensor1Status == "mid" && sensor2Status == "close") {
    Serial.println("Two cells detected");  
  } 
  if(sensor1Status == "mid" && sensor2Status == "mid" || sensor1Status == "close" && sensor2Status == "far" ||  sensor1Status == "far" && sensor2Status == "close" || sensor1Status == "far" && sensor2Status == "mid" ||  sensor1Status == "mid" && sensor2Status == "far") {
    Serial.println("One cell detected"); 
  }
}
