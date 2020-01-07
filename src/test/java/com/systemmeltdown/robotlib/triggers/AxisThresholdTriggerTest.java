package com.systemmeltdown.robotlib.triggers;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AxisThresholdTriggerTest {
  @Test
	public void testGetReturnsTrueWhenHeld() {
    XboxController controller = mock( XboxController.class );

    when( controller.getTriggerAxis(Hand.kRight) ).thenReturn(1.0);

    AxisThresholdTrigger trigger = new AxisThresholdTrigger(controller, Hand.kRight, .25);

    assertEquals( trigger.get(), true );
  }

  @Test
  public void testGetReturnsFalseWhenOpen() {
    XboxController controller = mock( XboxController.class );

    when( controller.getTriggerAxis(Hand.kRight) ).thenReturn(0.0);

    AxisThresholdTrigger trigger = new AxisThresholdTrigger(controller, Hand.kRight, .25);

	  assertEquals( trigger.get(), false );
  }

  @Test
  public void testGetReturnsFalseWhenJustUnderThreshold() {
    XboxController controller = mock( XboxController.class );

    when( controller.getTriggerAxis(Hand.kRight) ).thenReturn(0.249);

    AxisThresholdTrigger trigger = new AxisThresholdTrigger(controller, Hand.kRight, .25);

	  assertEquals( trigger.get(), false );
  }

  @Test
  public void testGetReturnsTrueWhenJustAboveThreshold() {
    XboxController controller = mock( XboxController.class );

    when( controller.getTriggerAxis(Hand.kRight) ).thenReturn(0.251);

    AxisThresholdTrigger trigger = new AxisThresholdTrigger(controller, Hand.kRight, .25);

	  assertEquals( trigger.get(), true );
  }

  @Test
  public void testGetReturnsFalseWhenAtThreshold() {
    XboxController controller = mock( XboxController.class );

    when( controller.getTriggerAxis(Hand.kRight) ).thenReturn(0.25);

    AxisThresholdTrigger trigger = new AxisThresholdTrigger(controller, Hand.kRight, .25);

	  assertEquals( trigger.get(), false );
  }
}