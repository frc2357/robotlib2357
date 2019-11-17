package com.systemmeltdown.robotlib.controllers;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DriverControlsTest{
  private XboxController controller;

  @Before
  public void setup() {
    controller = mock(XboxController.class);
  }

  @Test
  public void testGetEncoderTurnDifferentialIsOneWhenIsDriverSlowIsFalseAndTurnRateIsOneAndRightStickIsOne() {

    when(controller.getX(Hand.kRight)).thenReturn(1.0);

    DriverControls driverControlTester = new DriverControls(controller,0.0,0.0,0.0,0,1,0,0);

    assertEquals(driverControlTester.getEncoderTurnDifferential(), 1.0,0.0);
  }

  @Test
  public void testGetEncoderTurnDifferentialIsNegativeOneWhenIsDriverSlowIsFalseAndTurnRateIsOneAndRightStickIsNegativeOne() {
  
    when(controller.getX(Hand.kRight)).thenReturn(-1.0);

    DriverControls driverControlTester = new DriverControls(controller,0.0,0.0,0.0,0,1,0,0);

    assertEquals(driverControlTester.getEncoderTurnDifferential(), -1.0,0.0);
  }

  @Test
  public void testGetEncoderTurnDifferentialIsZeroWhenIsDriverSlowIsFalseAndTurnRateIsOneHalfAndRightStickIsZero() {

    when(controller.getX(Hand.kRight)).thenReturn(0.0);

    DriverControls driverControlTester = new DriverControls(controller,0.0,0.0,0.0,0,1,0,0);

    assertEquals(driverControlTester.getEncoderTurnDifferential(), 0.0,0.0);
  }

  @Test
  public void testGetEncoderTurnDifferentialIsOneWhenIsDriverSlowIsTrueAndDriverSlowTurnRateIsOneAndRightStickIsOne() {

    when(controller.getX(Hand.kRight)).thenReturn(1.0);
    when(controller.getTriggerAxis(Hand.kRight)).thenReturn(0.30);

    DriverControls driverControlTester = new DriverControls(controller,0.0,0.0,0.0,1,0,0,0);

    assertEquals(driverControlTester.getEncoderTurnDifferential(), 1.0,0.0);
  }

  @Test
  public void testGetEncoderSpeedIsOneWhenIsDriverSlowIsFalseAndSpeedRateIsOneAndLeftStickIsOne() {

    when(controller.getY(Hand.kLeft)).thenReturn(1.0);

    DriverControls driverControlTester = new DriverControls(controller,0.0,0.0,0.0,0,0,0,1);

    assertEquals(driverControlTester.getEncoderSpeed(), -1.0,0.0);
  }

  @Test
  public void testGetEncoderSpeedIsNegativeOneWhenIsDriverSlowIsFalseAndSpeedRateIsOneAndLeftStickIsNegativeOne() {

    when(controller.getY(Hand.kLeft)).thenReturn(-1.0);

    DriverControls driverControlTester = new DriverControls(controller,0.0,0.0,0.0,0,0,0,1);

    assertEquals(driverControlTester.getEncoderSpeed(), 1.0,0.0);
  }

  @Test
  public void testGetEncoderSpeedIsZeroWhenIsDriverSlowIsFalseAndSpeedRateIsTwoAndLeftStickIsZero() {

    when(controller.getY(Hand.kLeft)).thenReturn(0.0);

    DriverControls driverControlTester = new DriverControls(controller,0.0,0.0,0.0,0,0,0,2);

    assertEquals(driverControlTester.getEncoderSpeed(), 0.0,0.0);
  }

  @Test
  public void testGetEncoderSpeedIsOneWhenIsDriverSlowIsTrueAndDriverSlowSpeedRateIsOneAndLeftStickIsOne() {

    when(controller.getY(Hand.kLeft)).thenReturn(1.0);
    when(controller.getTriggerAxis(Hand.kRight)).thenReturn(0.30);

    DriverControls driverControlTester = new DriverControls(controller,0.0,0.0,0.0,0,0,1,0);

    assertEquals(driverControlTester.getEncoderSpeed(), -1.0,0.0);
  }
}