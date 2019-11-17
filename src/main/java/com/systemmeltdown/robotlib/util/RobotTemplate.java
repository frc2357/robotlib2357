package com.systemmeltdown.robotlib.util;

import edu.wpi.first.wpilibj.TimedRobot;
import com.systemmeltdown.robotlib.controllers.DriverControls;
import edu.wpi.first.wpilibj.XboxController;

import com.systemmeltdown.robotlib.subsystems.DriveSub;

public class RobotTemplate extends TimedRobot {

    public static OI OI;

    XboxController controller;

    public static DriveSub DRIVE_SUB;

    public RobotTemplate(int DRIVE_MAX_RPMS, int ENCODER_TICKS_PER_ROTATION, double DRIVE_STICK_DEADBAND,
     double DRIVE_RAMP_SECONDS, double DRIVE_MOTOR_DEADBAND, double DRIVER_SPEED_PROPORTION,
     double DRIVER_TURN_PROPORTION, double DRIVER_SPEED_PROPORTION_SLOW, 
     double DRIVER_TURN_PROPORTION_SLOW, double GUNNER_SPEED_PROPORTION, double GUNNER_TURN_PROPORTION,
     double FAILSAFE_TRIM_FORWARD_DEFAULT, double FAILSAFE_TRIM_REVERSE_DEFAULT, 
     double  DRIVER_SLOW_TRIGGER_THRESHOLD, int CAN_ID_LEFT_DRIVE, int CAN_ID_LEFT_SLAVE,
     int CAN_ID_RIGHT_DRIVE, int CAN_ID_RIGHT_SLAVE, int CAN_ID_Pigeon_IMU, int GYRO_AXIS_TOTAL,
     int VEL_FEED_FWD, double WHEEL_CIRCUMFERENCE_INCHES, int TALON_TIMEOUT_MS,
     int TALON_PID_PRIMARY, int TALON_SLOT_VELOCITY,
     int TALON_SLOT_DISTANCE, int TALON_SLOT_TURNING, PIDValues PID_DRIVE_POS, PIDValues PID_DRIVE_SPEED,
     PIDValues PID_DRIVE_YAW, int GYRO_AXIS_YAW ) {

        controller = new XboxController(0);
        DriverControls driverControls = new DriverControls(controller, DRIVE_STICK_DEADBAND, DRIVER_TURN_PROPORTION, DRIVER_SPEED_PROPORTION, 0, 0, 0, 0);
        OI = new OI(driverControls);

        
        DRIVE_SUB = new DriveSub(CAN_ID_LEFT_DRIVE, CAN_ID_LEFT_SLAVE, CAN_ID_RIGHT_DRIVE,
        CAN_ID_RIGHT_SLAVE, CAN_ID_Pigeon_IMU, GYRO_AXIS_TOTAL, GYRO_AXIS_YAW);
    }
}