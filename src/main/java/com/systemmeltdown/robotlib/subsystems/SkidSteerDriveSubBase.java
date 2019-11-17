package com.systemmeltdown.robotlib.subsystems;

import com.systemmeltdown.robotlib.commands.DriveProportional;
import com.systemmeltdown.robotlib.commands.DriveWithEncoders;

public abstract class SkidSteerDriveSubBase extends SubsystemBase {

    @Override
    public void initDefaultCommand() {
        if (this.isFailsafeActive()) {
            setDefaultCommand(new DriveProportional());
        } else {
            //setDefualtCommand(new DriveWithEncoders());
        }
    }

    abstract void setLeftSpeed(double speed);

    abstract void setRightSpeed(double speed);

    public void tankDrive(double leftSpeed, double rightSpeed) {

        setLeftSpeed(leftSpeed);
        setRightSpeed(rightSpeed);
    }
    //Override me!
    public void PIDDrive(int speed, int turn) {

    }

    
}
