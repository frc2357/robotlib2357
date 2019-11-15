package com.systemmeltdown.robotlib.controllers;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DriverControlsTest{
  @Test
  public void testGetEncoderTurnDifferentialIsOneWhenIsDriverSlowIsFalseAndTurnRateIsOneAndRightStickIsOne(){
    XboxController controller = mock(XboxController.class);

    when(controller.getX(Hand.kRight)).thenReturn(1.0);

    DriverControls driverControlTester = new DriverControls(controller,0.0,0.0,0.0,0,1,0,0);

    assertEquals(driverControlTester.getEncoderTurnDifferential(), 0.9,1.0);
  }

  @Test
  public void testGetEncoderTurnDifferentialIsNegativeOneWhenIsDriverSlowIsFalseAndTurnRateIsOneAndRightStickIsNegativeOne(){
    XboxController controller = mock(XboxController.class);

    when(controller.getX(Hand.kRight)).thenReturn(-1.0);

    DriverControls driverControlTester = new DriverControls(controller,0.0,0.0,0.0,0,1,0,0);

    assertEquals(driverControlTester.getEncoderTurnDifferential(), -1.0,-0.9);
  }

  @Test
  public void testGetEncoderTurnDifferentialIsZeroWhenIsDriverSlowIsFalseAndTurnRateIsOneHalfAndRightStickIsZero(){
    XboxController controller = mock(XboxController.class);

    when(controller.getX(Hand.kRight)).thenReturn(0.0);

    DriverControls driverControlTester = new DriverControls(controller,0.0,0.0,0.0,0,1,0,0);

    assertEquals(driverControlTester.getEncoderTurnDifferential(), 0.0,0.0);
  }

  @Test
  public void testGetEncoderTurnDifferentialIsZeroWhenIsDriverSlowIsTrueAndDriverSlowTurnRateIs1AndRightStickIs1(){
    XboxController controller = mock(XboxController.class);

    when(controller.getX(Hand.kRight)).thenReturn(1.0);
    when(controller.getTriggerAxis(Hand.kRight)).thenReturn(0.30);

    DriverControls driverControlTester = new DriverControls(controller,0.0,0.0,0.0,1,0,0,0);

    assertEquals(driverControlTester.getEncoderTurnDifferential(), 0.9,1);
  }














  @Test
  public void testGetEncoderSpeedIsOneWhenIsDriverSlowIsFalseAndSpeedRateIsOneAndLeftStickIsOne(){
    XboxController controller = mock(XboxController.class);

    when(controller.getX(Hand.kLeft)).thenReturn(1.0);

    DriverControls driverControlTester = new DriverControls(controller,0.0,0.0,0.0,0,0,0,1);

    assertEquals(driverControlTester.getEncoderSpeed(), -1.0,-0.9);
  }

  @Test
  public void testGetEncoderSpeedIsNegativeOneWhenIsDriverSlowIsFalseAndSpeedRateIsOneAndLeftStickIsNegativeOne(){
    XboxController controller = mock(XboxController.class);

    when(controller.getX(Hand.kLeft)).thenReturn(-1.0);

    DriverControls driverControlTester = new DriverControls(controller,0.0,0.0,0.0,0,0,0,1);

    assertEquals(driverControlTester.getEncoderSpeed(), 0.9,1.0);
  }

  @Test
  public void testGetEncoderSpeedIsZeroWhenIsDriverSlowIsFalseAndSpeedRateIsTwoAndLeftStickIsZero(){
    XboxController controller = mock(XboxController.class);

    when(controller.getX(Hand.kRight)).thenReturn(0.0);

    DriverControls driverControlTester = new DriverControls(controller,0.0,0.0,0.0,0,0,0,2);

    assertEquals(driverControlTester.getEncoderSpeed(), 0.0,0.0);
  }

  @Test
  public void testGetEncoderSpeedIsZeroWhenIsDriverSlowIsTrueAndDriverSlowSpeedRateIsOneAndLeftStickIsOne(){
    XboxController controller = mock(XboxController.class);

    when(controller.getX(Hand.kRight)).thenReturn(1.0);
    when(controller.getTriggerAxis(Hand.kRight)).thenReturn(0.30);

    DriverControls driverControlTester = new DriverControls(controller,0.0,0.0,0.0,0,0,1,2);

    assertEquals(driverControlTester.getEncoderSpeed(), -1.0,-0.9);
  }
}