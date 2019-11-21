package com.systemmeltdown.robotlib.controllers;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import com.systemmeltdown.robotlib.util.Utility;
import com.systemmeltdown.robotlib.commands.ProportionalDrive;
import com.systemmeltdown.robotlib.commands.VelocityDrive;
import com.systemmeltdown.robotlib.triggers.AxisThresholdTrigger;

public class DriverControls extends Controls implements ProportionalDrive, VelocityDrive {
  public final AxisThresholdTrigger slowTrigger;
  double driveStickDeadband;
  double driverTurnProportion;
  double driverSpeedProportion;
  final double axisThreshold = 0.25;
  int driverEncoderSlowTurnRate;
  int driverEncoderTurnRate;
  int driverEncoderSlowSpeed;
  int driverEncoderSpeed;

  public DriverControls(XboxController controller, double driveStickDeadband, double driverTurnProportion,
      double driverSpeedProportion, int driverEncoderSlowTurnRate, int driverEncoderTurnRate,
      int driverEncoderSlowSpeed, int driverEncoderSpeed) {
    super(controller, 0.0);

    this.driveStickDeadband = driveStickDeadband;
    this.driverTurnProportion = driverTurnProportion;
    this.driverSpeedProportion = driverSpeedProportion;
    this.driverEncoderSlowTurnRate = driverEncoderSlowTurnRate;
    this.driverEncoderTurnRate = driverEncoderTurnRate;
    this.driverEncoderSlowSpeed = driverEncoderSlowSpeed;
    this.driverEncoderSpeed = driverEncoderSpeed;

    slowTrigger = new AxisThresholdTrigger(controller, Hand.kRight, 0.25);
  }

  @Override
  public int getEncoderTurnDifferential() {
    double input = Utility.deadband(controller.getX(Hand.kRight), driveStickDeadband);
    int encoderTurn = slowTrigger.get() ? driverEncoderSlowTurnRate : driverEncoderTurnRate;
    int turnRate = (int) (input * encoderTurn);
    return turnRate;
  }

  @Override
  public int getEncoderSpeed() {
    double input = Utility.deadband(controller.getY(Hand.kLeft), driveStickDeadband);
    int encoderSpeed = slowTrigger.get() ? driverEncoderSlowSpeed : driverEncoderSpeed;
    int speed = (int) (-input * encoderSpeed);
    return speed;
  }

  @Override
  public double getProportionalTurn() {
    return controller.getX(Hand.kRight) * driverTurnProportion;
  }

  @Override
  public double getProportionalSpeed() {
    return -(controller.getY(Hand.kLeft) * driverSpeedProportion);
  }
}