package com.systemmeltdown.robotlib.controllers;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class GunnerControlsTest{

    @Test
    public void testGetProportionalTurnIsOneWhenTurnRateIs1AndRightStickIs1(){
        XboxController controller = mock(XboxController.class);

        when(controller.getX(Hand.kRight)).thenReturn(1.0);

        GunnerControls gunnerControlTester = new GunnerControls(controller,1.0,1.0,0.1,0,0);

        assertEquals(gunnerControlTester.getProportionalTurn(), -1.0,0.0);
    }
}