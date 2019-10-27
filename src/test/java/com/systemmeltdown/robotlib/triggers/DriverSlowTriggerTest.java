package com.systemmeltdown.robotlib.triggers;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DriverSlowTriggerTest {
	@Test
	public void testGetReturnsTrueWhenHeld() {
    XboxController controller = mock( XboxController.class );

    when( controller.getTriggerAxis(Hand.kRight)).thenReturn(1.0);

    DriverSlowTrigger trigger = new DriverSlowTrigger(controller);

		assertEquals( trigger.get(), true );

		trigger.close();
	}

	@Test
	public void testGetReturnsFalseWhenOpen() {
    XboxController controller = mock( XboxController.class );

    when( controller.getTriggerAxis(Hand.kRight)).thenReturn(0.0);

    DriverSlowTrigger trigger = new DriverSlowTrigger(controller);

		assertEquals( trigger.get(), false );

		trigger.close();
	}
}