package com.systemmeltdown.robotlib.controllers;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import com.systemmeltdown.robotlib.util.Utility;

/**
 * Set of control inputs for the Driver position.
 */
public class DriverControls implements ArcadeAxisInput {
  private XboxController m_controller;
  private double m_deadband;

  /**
   * Create driver controls
   * @param controller The Xbox Controller used for the driver
   * @param deadband The deadband used for all drive axis output (typically 0.1 or less)
   */
  public DriverControls(XboxController controller, double deadband) {
    m_controller = controller;
    m_deadband = deadband;
  }

  @Override
  public double getSpeed() {
    double value = m_controller.getY(Hand.kLeft);
    System.out.println("Left value: " + value);
    return Utility.deadband(value, m_deadband);
  }

  @Override
  public double getTurn() {
    double value = m_controller.getX(Hand.kRight);
    System.out.println("right value: " + value);
    return Utility.deadband(value, m_deadband);
  }
}
