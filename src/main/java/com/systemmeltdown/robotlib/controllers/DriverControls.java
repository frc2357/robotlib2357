package com.systemmeltdown.robotlib.controllers;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import com.systemmeltdown.robotlib.util.Utility;
import com.systemmeltdown.robotlib.commands.ProportionalDrive;
import com.systemmeltdown.robotlib.commands.VelocityDrive;

public class DriverControls extends Controls implements ProportionalDrive, VelocityDrive {
  double driveStickDeadband;
  double driverTurnProportion;
  double driverSpeedProportion;
  final double axisThreshold = 0.25;
  int driverEncoderSlowTurnRate;
  int driverEncoderTurnRate;
  int driverEncoderSlowSpeed;
  int driverEncoderSpeed;

  public DriverControls(XboxController controller,double driveStickDeadband, double driverTurnProportion,
  double driverSpeedProportion, int driverEncoderSlowTurnRate, int driverEncoderTurnRate,
  int driverEncoderSlowSpeed, int driverEncoderSpeed) {
    super(controller,0.0);

    this.driveStickDeadband = driveStickDeadband;
    this.driverTurnProportion = driverTurnProportion;
    this.driverSpeedProportion = driverSpeedProportion;
    this.driverEncoderSlowTurnRate = driverEncoderSlowTurnRate;
    this.driverEncoderTurnRate = driverEncoderTurnRate;
    this.driverEncoderSlowSpeed = driverEncoderSlowSpeed;
    this.driverEncoderSpeed = driverEncoderSpeed;

  }
  
  public boolean isDriverSlow() {
    double axis = controller.getTriggerAxis(Hand.kRight);

    return axis > axisThreshold;
  }
  
  @Override
  public int getEncoderTurnDifferential() {
    double input = Utility.deadband(controller.getX(Hand.kRight),driveStickDeadband);
    int encoderTurn = isDriverSlow() ? driverEncoderSlowTurnRate : driverEncoderTurnRate;
    int turnRate = (int)(input * encoderTurn);
    return turnRate;
  }

  @Override
  public int getEncoderSpeed() {

    double input = Utility.deadband(controller.getY(Hand.kLeft),driveStickDeadband);
    int encoderSpeed = isDriverSlow() ? driverEncoderSlowSpeed : driverEncoderSpeed;
    int speed = (int)(-input * encoderSpeed);
    return speed;
  }

  @Override
  public double getProportionalTurn() {
    return controller.getX(Hand.kRight) * driverTurnProportion;
  }

  @Override
  public double getProportionalSpeed() {
    return - (controller.getY(Hand.kLeft) * driverSpeedProportion);
  }
}