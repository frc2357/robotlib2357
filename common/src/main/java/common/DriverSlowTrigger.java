package common;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.buttons.Trigger;
import common.RobotMap;

public class DriverSlowTrigger extends Trigger {
  private XboxController controller;

  public DriverSlowTrigger(XboxController controller) {
    this.controller = controller;
  }

  @Override
  public boolean get() {
    double axis = controller.getTriggerAxis(Hand.kRight);

    return (axis > RobotMap.DRIVER_SLOW_TRIGGER_THRESHOLD);
  }
}