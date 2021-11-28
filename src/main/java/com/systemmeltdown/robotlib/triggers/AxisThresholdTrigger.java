package com.systemmeltdown.robotlib.triggers;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.XboxController.Axis;
import edu.wpi.first.wpilibj2.command.button.Trigger;

public class AxisThresholdTrigger extends Trigger {
  private XboxController controller;
  private Axis axis;
  private double triggerThreshold;

  public AxisThresholdTrigger(XboxController controller, Axis axis, double triggerThreshold) {
    this.controller = controller;
    this.axis = axis;
    this.triggerThreshold = triggerThreshold;
  }

  @Override
  public boolean get() {
    double axisValue = 0;

    switch (axis) {
      case kLeftX:
        axisValue = controller.getLeftX();
        break;
      case kLeftY:
        axisValue = controller.getLeftY();
        break;
      case kLeftTrigger:
        axisValue = controller.getLeftTriggerAxis();
        break;
      case kRightTrigger:
        axisValue = controller.getRightTriggerAxis();
        break;
      case kRightX:
        axisValue = controller.getRightX();
        break;
      case kRightY:
        axisValue = controller.getRightY();
        break;
    }

    return (axisValue > triggerThreshold);
  }
}