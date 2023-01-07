# Sensor2357 Arduino Library

This library is designed to enable a quick and easy interface between Arduino sensors
and the RoboRIO used in FIRST Robotics Competition. The primary goal of this library is to
make arduino-based sensor code easy to use in the FRC environment, but also accomplishes:

- Manages sensors that send a lot of data (noisy) and condenses their signal to something the robot cares about
- A single value can be sent to summarize data from multiple sensors
- With multiple Arduinos, multiple identical i2c devices (with identical device IDs) can be supported
- Settings for an Arduino sensor can be sent from the RoboRIO (e.g. from the "constants" file)
- Sends full updates only periodically, and partial updates on demand (with configurable limits)
- Supports integer, floating point, boolean, and string values
- Supports integer and floating point arrays
- Has built-in support for a min/max range (e.g. Only send updates to RoboRIO when an object is sensed between 200mm and 2 meters)
- Has support for multiple sensors on the same Arduino device
- Sensors can be easily tested with a serial terminal instead of requiring use of the RoboRIO
- RGB LED support to display current status, just like most FRC devices

For each Arduino device, multiple sensors can be connected, but it's completely fine
to use a single arduino for each sensor as well.

## Supported Devices

### Seeeduino XIAO RP2040

The first completely supported device, the XIAO RP2040 features ample processing
power, plenty of RAM and program space, a full set of Digital/Analog IO and I2C and more.
Additionally, it has a built-in RGB LED which this library makes use of:

Yellow slow-flash (once per second): Arduino is on, serial to RoboRIO is unopened or timed-out
Green occasional flash (every 5 seconds): Arduino is actively connected to RoboRIO and sending state
Red flash: Error has occurred, check "error" in returned state for more details

### Your Arduino-compatible device of choice

All it takes to support a new device is to make your own class a subclass of SensorDevice and fill
out all pure-virtual functions and fill in the input and output for the constructor. See `SensorDevice_Seeed_XIAO_RP2040.h` for an example.

### Serial interface with Sensor2357

Sensor2357 is designed to be communicated with via the USB serial port. The full JSON state is sent
periodically, and partial updates are sent when values change. The RoboRIO then can send requests to
the Arduino over the same serial port in order to change settings, request a full state, or send periodic
heartbeat keep-alive messages to let the Arduino know that the RoboRIO is still listening to its output.

#### Sensor2357 Output

Example full state output:

`||v1||{"name":"SensorDeviceName","error":"","maxUpdateHz":10,"timeoutMs":1000,"sensors":{"distanceMM":{"_value":0.00,"_active":true,"min":5.00,"max":30.00}}}`

Example partial state output:

`||v1||{"sensors":{"distanceMM":{"_value":0.00}}}`
or
`||v1||{"sensors":{"distanceMM":{"_active":false}}}`

#### Valid Input Values

The Sensor2357 device will also receive JSON from the RoboRIO in the following forms:

##### Keep Alive Heartbeat

Example:
`||v1||`

A Keep-Alive heartbeat is where the versioned preamble is sent alone. This feeds the timeout timer on the
Arduino Sensor2357 code. The default timeout is 1 second, but can be configured as a setting from the RoboRIO

##### Full State Request

Example:
`||v1||{}`

An empty object can be sent which tells the Sensor2357 code that no updates are to be made, but a full state
should be sent back to the RoboRIO

##### Partial Update

Partial updates will primarily be sent from the RoboRIO for the purposes of changing settings on a specific
sensor, or on the sensor device itself. After each partial update, the device will return a full state
update in response.

Note: The RoboRIO may not send a setting that is not already present in the JSON.

Example:
`||v1||{"maxUpdateHz": 20}`
or
`||v1||{"sensors": {"sensor1": "min": 200, "max": 2000}}`

#### SensorDevice settings

The Sensor2357 SensorDevice has a few built-in settings:

##### timeoutMs

Default value: `1000` (1 second)

This is the number of milliseconds that can pass after the last message from the RoboRIO before the sensor
device considers itself to be offline. When in a timed-out offline state, the device will stop sending
partial and full updates to the RoboRIO, and will display the offline LED state. As soon as another message
is received from the RoboRIO, the device will return to an active state and resume sending updates.

##### maxUpdateHz

Default value: `10` (10 times a second, or every 100 milliseconds)

The is the maximum frequency (in hertz, or cycles per second) at which any updates will be sent. This is designed for throttling communications between this device and the RoboRIO. Setting this to a higher number means quickly changing
sensor values will be sent more frequently to the RoboRIO for processing.

##### error

Default value: `""`

The error field is an empty string during normal operation. When this field is not empty, it is a short descript of a
current error that has occurred on the Arduino. Error values are not cleared automatically, it's up to the RoboRIO to
send a partial update with an empty error string to clear it after it has been read, logged, or otherwise dealt with.

#### Serial terminal testing

The Sensor2357 library is easy to test with manually, no RoboRIO needed. This is useful when developing sensor code
for a new sensor, trying it out, or debugging.

1. Find a suitable Serial terminal program (The Arduino dev environment has one built-in, or you can use PuTTY for Windows)
2. Using the terminal program, set to 115200 bps and connect to the USB serial port on the Arduino
3. You should see an update sent from Sensor2357, but it will likely timeout quickly and stop
4. Send `||v1||{"timeoutMs": 60000}` to adjust the timeout to 60 seconds, to avoid timeouts while testing
5. Watch the terminal output for sensor values, etc. and send partial updates to adjust settings.
