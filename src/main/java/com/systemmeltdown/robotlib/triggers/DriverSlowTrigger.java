package com.systemmeltdown.robotlib.triggers;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.buttons.Trigger;

public class DriverSlowTrigger extends Trigger {
  private XboxController controller;
  private double triggerThreshold;

  public DriverSlowTrigger(XboxController controller) {
    this(controller, 0.25);
  }

  public DriverSlowTrigger(XboxController controller, double triggerThreshold) {
    this.controller = controller;
    this.triggerThreshold = triggerThreshold;
  }

  @Override
  public boolean get() {
    double axis = controller.getTriggerAxis(Hand.kRight);

    return (axis > triggerThreshold);
  }
}