package com.systemmeltdown.robotlib.controllers;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import com.systemmeltdown.robotlib.util.Utility;
import com.systemmeltdown.robotlib.commands.ProportionalDrive;
import com.systemmeltdown.robotlib.commands.VelocityDrive;

public class GunnerControls extends Controls
    implements ProportionalDrive, VelocityDrive {
  private double TURN_FACTOR;
  private double SPEED_FACTOR;
  private double deadband;
  private int gunnerEncoderSpeed;
  private int gunnerEncoderTurnRate;
  
  
  

  public GunnerControls(XboxController controller, double TURN_FACTOR, double SPEED_FACTOR, double deadband,
  int gunnerEncoderSpeed, int gunnerEncoderTurnRate) {
    super(controller);
    this.TURN_FACTOR = TURN_FACTOR;
    this.SPEED_FACTOR = SPEED_FACTOR;
    this.deadband = deadband;
    this.gunnerEncoderSpeed = gunnerEncoderSpeed;
    this.gunnerEncoderTurnRate = gunnerEncoderTurnRate;
  }

  @Override
  public double getProportionalTurn() {
    return controller.getX(Hand.kRight) * TURN_FACTOR;
  }

  @Override
  public double getProportionalSpeed() {
    return -(controller.getY(Hand.kLeft) * SPEED_FACTOR);
  }

  @Override
  public int getEncoderTurnDifferential() {
    double input = Utility.deadband(controller.getX(Hand.kRight), deadband);
    int turnRate = (int)(input * gunnerEncoderTurnRate);
    return turnRate;
  }

  @Override
  public int getEncoderSpeed() {
    double input = Utility.deadband(controller.getY(Hand.kLeft), deadband);
    int speed = (int)(-input * gunnerEncoderSpeed);
    return speed;
  }

 
}