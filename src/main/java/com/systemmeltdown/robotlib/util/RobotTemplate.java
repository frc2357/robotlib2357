package com.systemmeltdown.robotlib.util;

import edu.wpi.first.wpilibj.TimedRobot;

import com.systemmeltdown.robotlib.subsystems.SingleSpeedTalonDriveSub;
import com.systemmeltdown.robotlib.subsystems.SkidSteerDriveSubBase;

public class RobotTemplate extends TimedRobot {
    public static final OI OI = new OI();

    public static SkidSteerDriveSubBase DRIVE_SUB;

    public RobotTemplate() {

        int[] leftSlaveIDS = { 2 };
        int[] rightSlaveIDS = { 3 };
        int ENCODER_TICKS_PER_ROTATION = 1024;
        int DRIVE_MAX_RPMS = 1100;
        int MILLISECONDS_PER_SECOND = 1000;
        int MILLIS_PER_MINUTE = 60 * MILLISECONDS_PER_SECOND;
        int VELOCITY_UNITS_PER_MIN = MILLIS_PER_MINUTE / 100;
        int MAX_ENCODER_VELOCITY = DRIVE_MAX_RPMS * ENCODER_TICKS_PER_ROTATION / VELOCITY_UNITS_PER_MIN;



        DRIVE_SUB = new SingleSpeedTalonDriveSub(0, 1, rightSlaveIDS, leftSlaveIDS, 10, 0, 0.04, 1.0, 2, 0, 1,
                new PIDValues(0.10, 0.0002, 10.0, 0.0, 0, 0.23), new PIDValues(0.50, 0.0, 40.0, 0.0, 0, 1.00), MAX_ENCODER_VELOCITY);
    }
}