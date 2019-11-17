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

        DRIVE_SUB = new SingleSpeedTalonDriveSub(0, 1, rightSlaveIDS, leftSlaveIDS, 10, 0, 0.04, 1.0, 2, 0, 1,
                new PIDValues(0.10, 0.0002, 10.0, 0.0, 0, 0.23), new PIDValues(0.50, 0.0, 40.0, 0.0, 0, 1.00));
    }
}