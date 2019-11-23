package com.systemmeltdown.robotlib.controllers;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import com.systemmeltdown.robotlib.util.Utility;

public class GunnerControls extends Controls {
  private double TURN_FACTOR;
  private double SPEED_FACTOR;
  private double deadband;
  private int gunnerEncoderSpeed;
  private int gunnerEncoderTurnRate;

  public GunnerControls(XboxController controller, double TURN_FACTOR, double SPEED_FACTOR, double deadband,
      int gunnerEncoderSpeed, int gunnerEncoderTurnRate) {
    super(controller, 0.0);
    this.TURN_FACTOR = TURN_FACTOR;
    this.SPEED_FACTOR = SPEED_FACTOR;
    this.deadband = deadband;
    this.gunnerEncoderSpeed = gunnerEncoderSpeed;
    this.gunnerEncoderTurnRate = gunnerEncoderTurnRate;
  }

  public double getProportionalTurn() {
    return controller.getX(Hand.kRight) * TURN_FACTOR;
  }

  public double getProportionalSpeed() {
    return -(controller.getY(Hand.kLeft) * SPEED_FACTOR);
  }

  public int getEncoderTurnDifferential() {
    double input = Utility.deadband(controller.getX(Hand.kRight), deadband);
    int turnRate = (int) (input * gunnerEncoderTurnRate);
    return turnRate;
  }

  public int getEncoderSpeed() {
    double input = Utility.deadband(controller.getY(Hand.kLeft), deadband);
    int speed = (int) (-input * gunnerEncoderSpeed);
    return speed;
  }

}