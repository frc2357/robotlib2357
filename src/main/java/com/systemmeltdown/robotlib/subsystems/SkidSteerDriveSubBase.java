package com.systemmeltdown.robotlib.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

public abstract class SkidSteerDriveSubBase extends SubsystemBase {

    

    @Override
    public void initDefaultCommand() {
        if(this.isFailsafeActive()) {
            setDefaultCommand(splitArcadeDrive);
        }
    }

    abstract void tankDrive(double leftSpeed, double rightSpeed);

}
