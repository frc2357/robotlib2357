package  com.systemmeltdown.robotlib.controllers;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import com.systemmeltdown.robotlib.util.Utility;
import  com.systemmeltdown.robotlib.util.RobotMap;

public class Controls {
  protected XboxController controller;

  public Controls(XboxController controller) {
    this.controller = controller;
  }

  public boolean isActive() {
    double speed = Utility.deadband(controller.getY(Hand.kLeft), RobotMap.DRIVE_STICK_DEADBAND);
    double turn = Utility.deadband(controller.getX(Hand.kRight), RobotMap.DRIVE_STICK_DEADBAND);
    return (speed != 0 || turn != 0);
  }
}