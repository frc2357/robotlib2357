package com.systemmeltdown.robotlib.triggers;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.systemmeltdown.robotlib.util.DPadValue;

public class DPadTriggerTest {
	@Test
	public void testDPadValueNotEqualToLastValue() {

		XboxController controller = mock(XboxController.class);
		DPadValue triggerValue = DPadValue.fromPOV(1);

		when(controller.getPOV()).thenReturn(1);
        
		DPadTrigger Dpad = new DPadTrigger(controller,triggerValue);

		assertEquals(Dpad.get(), true);

		Dpad.close();
		
	}

	@Test
	public void testDpadValueEqualToLastValue() {

		XboxController controller = mock(XboxController.class);
		DPadValue triggerValue = DPadValue.fromPOV(-1);

		when(controller.getPOV()).thenReturn(-1);
        
		DPadTrigger Dpad = new DPadTrigger(controller,triggerValue);

		assertEquals(Dpad.get(), false);

		Dpad.close();

		
	}
}