package com.systemmeltdown.robotlib.subsystems.drive;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.sensors.PigeonIMU;

import edu.wpi.first.wpilibj.controller.PIDController;

public class PIDFalconDriveSubsystem extends SingleSpeedFalconDriveSubsystem {
    final int DRIVE_MAX_RPMS = 1100;
    final double VEL_FEED_FWD = 1023.0 / DRIVE_MAX_RPMS;
    final int ENCODER_TICKS_PER_ROTATION = 1024;

    final int VELOCITY_UNITS_PER_MIN = 60000 / 100;
    final int MAX_ENCODER_VELOCITY = DRIVE_MAX_RPMS * ENCODER_TICKS_PER_ROTATION / VELOCITY_UNITS_PER_MIN;

    private PIDController yawPID;

    protected PigeonIMU m_gyro;

    public PIDFalconDriveSubsystem(WPI_TalonFX leftFalconMaster, WPI_TalonFX[] leftFalconSlaves,
            WPI_TalonFX rightFalconMaster, WPI_TalonFX[] rightFalconSlaves, PigeonIMU gyro) {
        super(leftFalconMaster, leftFalconSlaves, rightFalconMaster, rightFalconSlaves);

        m_gyro = gyro;
    }

    public void configureYawPID() {
        resetYaw();
    
        yawPIDSource = new YawPIDSource();
    
        PIDValues yawPIDValues = RobotMap.PID_DRIVE_YAW;
        yawPID = new PIDController(
          yawPIDValues.kp,
          yawPIDValues.ki,
          yawPIDValues.kd,
          yawPIDSource,
          yawPIDSource,
          0.05
        );
        yawPID.setOutputRange(-yawPIDValues.peak, yawPIDValues.peak);
      }

    public void resetYaw() {
        gyro.setYaw(0);
        gyro.setAccumZAngle(0);
    }

    protected void PIDDrive() {
        if (this.isClosedLoopEnabled()) {
            return;
        }

        if (yawPID.isEnabled()) {
            yawPID.disable();
        }

        leftMaster.selectProfileSlot(RobotMap.TALON_SLOT_VELOCITY, RobotMap.TALON_PID_PRIMARY);
        rightMaster.selectProfileSlot(RobotMap.TALON_SLOT_VELOCITY, RobotMap.TALON_PID_PRIMARY);

        int leftVelocity = speed + turn;
        int rightVelocity = speed - turn;

        Utility.clamp(leftVelocity, -RobotMap.MAX_ENCODER_VELOCITY, RobotMap.MAX_ENCODER_VELOCITY);
        Utility.clamp(rightVelocity, -RobotMap.MAX_ENCODER_VELOCITY, RobotMap.MAX_ENCODER_VELOCITY);

        rightMaster.set(ControlMode.Velocity, rightVelocity);
        leftMaster.set(ControlMode.Velocity, leftVelocity);
    }
}