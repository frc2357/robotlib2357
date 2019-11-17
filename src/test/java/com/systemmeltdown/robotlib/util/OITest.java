package com.systemmeltdown.robotlib.util;

import com.systemmeltdown.robotlib.controllers.DriverControls;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class OITest{
    @Test
    public void testIsDriverSlowIsFalseWhenRightTriggerIsZero(){
        XboxController controller = mock(XboxController.class);

        when(controller.getTriggerAxis(Hand.kRight)).thenReturn(0.0);

        DriverControls driveControls = new DriverControls(controller, 0.0, 0.0, 0.0, 0, 0, 0, 0);

        OI oi = new OI(driveControls);

        assertEquals(oi.isDriverSlow(),false);

    }

    @Test
    public void testIsDriverSlowIsTrueWhenRightTriggerIsOneHalf(){
        XboxController controller = mock(XboxController.class);

        when(controller.getTriggerAxis(Hand.kRight)).thenReturn(0.5);
        
        DriverControls driveControls = new DriverControls(controller, 0.0, 0.0, 0.0, 0, 0, 0, 0);

        OI oi = new OI(driveControls);

        assertEquals(oi.isDriverSlow(),true);

    }
}