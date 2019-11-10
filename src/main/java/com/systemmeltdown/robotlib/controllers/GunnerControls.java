package com.systemmeltdown.robotlib.controllers;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import com.systemmeltdown.robotlib.util.Utility;
import com.systemmeltdown.robotlib.util.RobotMap;
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
    super(controller,0.1);
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
    double input = Utility.deadband(controller.getX(Hand.kRight), RobotMap.DRIVE_STICK_DEADBAND);
    int turnRate = (int)(input * RobotMap.GUNNER_ENCODER_TURN_RATE);
    return turnRate;
  }

  @Override
  public int getEncoderSpeed() {
    double input = Utility.deadband(controller.getY(Hand.kLeft), RobotMap.DRIVE_STICK_DEADBAND);
    int speed = (int)(-input * RobotMap.GUNNER_ENCODER_SPEED);
    return speed;
  }

 
}