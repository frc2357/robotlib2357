package com.systemmeltdown.robotlib;

import com.systemmeltdown.robotlib.controllers.DriverControls;
import com.systemmeltdown.robotlib.subsystems.drive.SingleSpeedTalonDriveSubsystem;

import edu.wpi.first.wpilibj.XboxController;

// TODO:Finalize how we want to do this.

public class References {
    public References(XboxController xbox) {
        driverController = new DriverControls(xbox, 0.1);
    }
    public static DriverControls driverController;

    public static SingleSpeedTalonDriveSubsystem driveSub = new SingleSpeedTalonDriveSubsystem(1, 2, new int[]{3}, new int[]{4});
}