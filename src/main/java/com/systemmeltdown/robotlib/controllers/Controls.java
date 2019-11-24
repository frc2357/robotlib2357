package com.systemmeltdown.robotlib.controllers;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import com.systemmeltdown.robotlib.util.Utility;

public class Controls {
  protected XboxController controller;
  private double deadband;

  public Controls(XboxController controller, double deadband) {
    this.controller = controller;
    this.deadband = deadband;
  }

  public boolean isActive() {
    double speed = Utility.deadband(controller.getY(Hand.kLeft), deadband);
    double turn = Utility.deadband(controller.getX(Hand.kRight), deadband);
    return (speed != 0 || turn != 0);
  }
}