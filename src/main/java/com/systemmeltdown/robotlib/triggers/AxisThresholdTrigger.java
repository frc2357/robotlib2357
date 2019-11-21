package com.systemmeltdown.robotlib.triggers;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.buttons.Trigger;

public class AxisThresholdTrigger extends Trigger {
  private XboxController controller;
  private Hand hand;
  private double triggerThreshold;

  public AxisThresholdTrigger(XboxController controller, Hand hand, double triggerThreshold) {
    this.controller = controller;
    this.hand = hand;
    this.triggerThreshold = triggerThreshold;
  }

  @Override
  public boolean get() {
    double axis = controller.getTriggerAxis(hand);
    
    return (axis > triggerThreshold);
  }
}