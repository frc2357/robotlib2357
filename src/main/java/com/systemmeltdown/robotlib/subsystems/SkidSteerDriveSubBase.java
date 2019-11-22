package com.systemmeltdown.robotlib.subsystems;

import com.systemmeltdown.robotlib.commands.DriveProportional;
import com.systemmeltdown.robotlib.commands.DriveWithEncoders;

public abstract class SkidSteerDriveSubBase extends SubsystemBase {

    @Override
    public void initDefaultCommand() {
        if (this.isFailsafeActive()) {
            setDefaultCommand(new DriveProportional());
        } else {
            // TODO replace with driveWithEncoders
            setDefaultCommand(new DriveProportional());
        }
    }

    protected abstract void setLeftSpeed(double speed);

    protected abstract void setRightSpeed(double speed);

    public void tankDrive(double leftSpeed, double rightSpeed) {
        setLeftSpeed(leftSpeed);
        setRightSpeed(rightSpeed);
    }

    // Override me!
    public abstract void PIDDrive(int speed, int turn);

}
