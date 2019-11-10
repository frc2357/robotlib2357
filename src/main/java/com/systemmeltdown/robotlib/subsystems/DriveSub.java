/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.systemmeltdown.robotlib.subsystems;

import com.systemmeltdown.robotlib.commands.DriveProportional;
import com.systemmeltdown.robotlib.commands.DriveWithEncoders;
import com.systemmeltdown.robotlib.util.PIDValues;
import com.systemmeltdown.robotlib.util.Utility;
import com.systemmeltdown.robotlib.util.RobotMap;


import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.FollowerType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatusFrame;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.sensors.PigeonIMU;
import com.ctre.phoenix.sensors.PigeonIMU_StatusFrame;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;


/**
 * Drive subsystem, controls 4 motors, 2 encoders, and 1 gyro.
 */
public class DriveSub extends SubsystemBase {
  public WPI_TalonSRX leftMaster = new WPI_TalonSRX(RobotMap.CAN_ID_LEFT_DRIVE);
  public WPI_TalonSRX leftSlave = new WPI_TalonSRX(RobotMap.CAN_ID_LEFT_DRIVE_SLAVE);
  public WPI_TalonSRX rightMaster = new WPI_TalonSRX(RobotMap.CAN_ID_RIGHT_DRIVE);
  public WPI_TalonSRX rightSlave = new WPI_TalonSRX(RobotMap.CAN_ID_RIGHT_DRIVE_SLAVE);

  private PigeonIMU pidgey = new PigeonIMU(RobotMap.CAN_ID_PIGEON_IMU);
  private double[] yawPitchRoll = new double[RobotMap.GYRO_AXIS_TOTAL];
  private YawPIDSource yawPIDSource;
  private PIDController yawPID;
  private int positionTarget;



  public DriveSub(int CAN_ID_LEFT_DRIVE, int CAN_ID_LEFT_SLAVE, int CAN_ID_RIGHT_DRIVE,
      int CAN_ID_RIGHT_SLAVE, int CAN_ID_Pigeon_IMU, int GYRO_AXIS_TOTAL, int GYRO_AXIS_YAW) {
      this.leftMaster = new WPI_TalonSRX(CAN_ID_LEFT_DRIVE);
      this.leftSlave = new WPI_TalonSRX(CAN_ID_LEFT_SLAVE);
      this.rightMaster = new WPI_TalonSRX(CAN_ID_RIGHT_DRIVE);
      this.rightSlave = new WPI_TalonSRX(CAN_ID_RIGHT_SLAVE);
      this.pidgey = new PigeonIMU(CAN_ID_Pigeon_IMU);
      this.yawPitchRoll = new double[GYRO_AXIS_TOTAL];
      this.yawPitchRoll = new double[GYRO_AXIS_YAW];
      
  }
  private class YawPIDSource implements PIDSource, PIDOutput {
    @Override
    public void setPIDSourceType(PIDSourceType pidSource) {
      System.err.println("YawPIDSource: Something tried to setPIDSourceType!");
    }

    @Override
    public PIDSourceType getPIDSourceType() {
      return PIDSourceType.kDisplacement;
    }

    @Override
    public double pidGet() {
      return getYaw(false);
    }

    @Override
    public void pidWrite(double output) {
      int encoderDifferential = (int) output;

      int leftVelocity = -encoderDifferential;
      int rightVelocity = +encoderDifferential;

      Utility.clamp(leftVelocity, -RobotMap.MAX_ENCODER_VELOCITY, RobotMap.MAX_ENCODER_VELOCITY);
      Utility.clamp(rightVelocity, -RobotMap.MAX_ENCODER_VELOCITY, RobotMap.MAX_ENCODER_VELOCITY);

      leftMaster.set(ControlMode.Velocity, leftVelocity);
      rightMaster.set(ControlMode.Velocity, rightVelocity);
    }
  }

  public void configure() {
    int timeout = RobotMap.TALON_TIMEOUT_MS;

    leftMaster.set(ControlMode.PercentOutput, 0.0);
    rightMaster.set(ControlMode.PercentOutput, 0.0);

    leftMaster.configFactoryDefault();
    leftSlave.configFactoryDefault();
    rightMaster.configFactoryDefault();
    rightSlave.configFactoryDefault();
    pidgey.configFactoryDefault();

    leftMaster.setNeutralMode(NeutralMode.Brake);
    rightMaster.setNeutralMode(NeutralMode.Brake);

    leftMaster.configSelectedFeedbackSensor(
      FeedbackDevice.QuadEncoder,
      RobotMap.TALON_PID_PRIMARY,
      timeout
    );
    rightMaster.configSelectedFeedbackSensor(
      FeedbackDevice.QuadEncoder,
      RobotMap.TALON_PID_PRIMARY,
      timeout
    );

    leftMaster.setInverted(false);
    leftMaster.setSensorPhase(false);
    leftSlave.setInverted(false);
    leftSlave.follow(leftMaster);

    rightMaster.setInverted(true);
    rightMaster.setSensorPhase(false);
    rightSlave.setInverted(true);
    rightSlave.follow(rightMaster);

    leftMaster.setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 20);
    rightMaster.setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 20);
    pidgey.setStatusFramePeriod(PigeonIMU_StatusFrame.CondStatus_9_SixDeg_YPR, 5);

    leftMaster.configNeutralDeadband(RobotMap.DRIVE_MOTOR_DEADBAND);
    rightMaster.configNeutralDeadband(RobotMap.DRIVE_MOTOR_DEADBAND);

    leftMaster.configOpenloopRamp(RobotMap.DRIVE_RAMP_SECONDS);
    rightMaster.configOpenloopRamp(RobotMap.DRIVE_RAMP_SECONDS);

    leftMaster.configNominalOutputForward(0, timeout);
    leftMaster.configNominalOutputReverse(0, timeout);
		leftMaster.configPeakOutputReverse(-1.0, timeout);
    leftMaster.configPeakOutputForward(+1.0, timeout);
    rightMaster.configNominalOutputForward(0, timeout);
    rightMaster.configNominalOutputReverse(0, timeout);
		rightMaster.configPeakOutputForward(+1.0, timeout);
    rightMaster.configPeakOutputReverse(-1.0, timeout);

    Utility.configTalonPID(leftMaster, RobotMap.TALON_SLOT_VELOCITY, RobotMap.PID_DRIVE_SPEED);
    Utility.configTalonPID(rightMaster, RobotMap.TALON_SLOT_VELOCITY, RobotMap.PID_DRIVE_SPEED);

    Utility.configTalonPID(rightMaster, RobotMap.TALON_SLOT_DISTANCE, RobotMap.PID_DRIVE_POS);
    Utility.configTalonPID(leftMaster, RobotMap.TALON_SLOT_DISTANCE, RobotMap.PID_DRIVE_POS);

    /**
		 * 1ms per loop.  PID loop can be slowed down if need be.
		 * For example,
		 * - if sensor updates are too slow
		 * - sensor deltas are very small per update, so derivative error never gets large enough to be useful.
		 * - sensor movement is very slow causing the derivative error to be near zero.
		 */
    int closedLoopTimeMs = 1;

    rightMaster.configClosedLoopPeriod(RobotMap.TALON_SLOT_DISTANCE, closedLoopTimeMs, timeout);
    rightMaster.configClosedLoopPeriod(RobotMap.TALON_SLOT_TURNING, closedLoopTimeMs, timeout);
    rightMaster.configClosedLoopPeriod(RobotMap.TALON_SLOT_VELOCITY, closedLoopTimeMs, timeout);

    resetEncoders();
    configureYawPID();
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

  public void resetEncoders() {
    rightMaster.getSensorCollection().setQuadraturePosition(0, RobotMap.TALON_TIMEOUT_MS);
    leftMaster.getSensorCollection().setQuadraturePosition(0, RobotMap.TALON_TIMEOUT_MS);
  }

  public void resetYaw() {
    pidgey.setYaw(0);
    pidgey.setAccumZAngle(0);
  }

  @Override
  public void initDefaultCommand() {
    setFailsafeActive(isFailsafeActive());
  }

  @Override
  public void setFailsafeActive(boolean failsafeActive) {
    super.setFailsafeActive(failsafeActive);

    if (failsafeActive) {
      setDefaultCommand(new DriveProportional());
    } else {
      setDefaultCommand(new DriveWithEncoders());
    }
  }

  public int getPositionError() {
    int currentPosition = rightMaster.getSelectedSensorPosition();
    return this.positionTarget - currentPosition;
  }

  public double getRotationError() {
    return yawPID.getError();
  }

  /**
   *
   * @param speed The desired speed in inches/sec
   * @param turn The desired turn rate in degrees/sec
   */
  public void PIDDrive(int speed, int turn) {
    if (super.isFailsafeActive()) {
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

  public void rotateDegrees(double degrees) {
    if (super.isFailsafeActive()) {
      return;
    }

    double currentYaw = getYaw(false);
    double targetYaw = currentYaw + degrees;

    leftMaster.selectProfileSlot(RobotMap.TALON_SLOT_VELOCITY, RobotMap.TALON_PID_PRIMARY);
    rightMaster.selectProfileSlot(RobotMap.TALON_SLOT_VELOCITY, RobotMap.TALON_PID_PRIMARY);

    yawPID.reset();
    yawPID.setSetpoint(targetYaw);
    yawPID.enable();
  }

  public void moveForwardDistance(double inches) {
    if (super.isFailsafeActive()) {
      return;
    }

    if (yawPID.isEnabled()) {
      yawPID.disable();
    }
    resetEncoders();

    leftMaster.selectProfileSlot(RobotMap.TALON_SLOT_DISTANCE, RobotMap.TALON_PID_PRIMARY);
    rightMaster.selectProfileSlot(RobotMap.TALON_SLOT_DISTANCE, RobotMap.TALON_PID_PRIMARY);

    int currentPosition = rightMaster.getSensorCollection().getQuadraturePosition();
    int distance = (int)(inches * RobotMap.ENCODER_TICKS_PER_ROTATION / RobotMap.WHEEL_CIRCUMFERENCE_INCHES);
    this.positionTarget = currentPosition + distance;

    rightMaster.set(ControlMode.Position, positionTarget);
    leftMaster.follow(rightMaster, FollowerType.AuxOutput1);
  }

  /**
   *
   * @return The gyro's current yaw value in degrees
   */
  public double getYaw(boolean moduloOutput) {
    pidgey.getYawPitchRoll(yawPitchRoll);
    double yaw = yawPitchRoll[RobotMap.GYRO_AXIS_YAW];
    if(moduloOutput) {
      if(yaw >= 180){
        yaw -= 360;
      }
      if(yaw <= -180){
        yaw += 360;
      }
    }
    return yaw;
  }

  public void tankDrive(double leftSpeed, double rightSpeed) {
    if (yawPID.isEnabled()) {
      yawPID.disable();
    }

    leftMaster.set(ControlMode.PercentOutput, leftSpeed);
    rightMaster.set(ControlMode.PercentOutput, rightSpeed);
  }
}