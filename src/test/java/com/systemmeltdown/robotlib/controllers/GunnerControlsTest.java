package com.systemmeltdown.robotlib.controllers;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class GunnerControlsTest{
    @Test
    public void testGetProportionalTurnIsOneWhenRightStickIsOneAndTurnRateIsOne(){
        XboxController controller = mock(XboxController.class);

        when(controller.getX(Hand.kRight)).thenReturn(1.0);

        GunnerControls gunnerControlTester = new GunnerControls(controller,1.0,1.0,0.1,0,0);

        assertEquals(gunnerControlTester.getProportionalTurn(), 0.9,1.0);
    }

    @Test
    public void testGetProportionalTurnIsNegativeOneWhenRightStickIsNegativeOneAndTurnRateIsOne(){
      XboxController controller = mock(XboxController.class);

      when(controller.getX(Hand.kRight)).thenReturn(-1.0);

      GunnerControls gunnerControlTester = new GunnerControls(controller,1.0,1.0,0.1,0,0);

      assertEquals(gunnerControlTester.getProportionalTurn(), -1.0,-0.9);
    }

    @Test
    public void testGetProportionalTurnIsZeroWhenRightStickIsZeroAndTurnRateIsOneHalf(){
      XboxController controller = mock(XboxController.class);

      when(controller.getX(Hand.kRight)).thenReturn(0.0);

      GunnerControls gunnerControlTester = new GunnerControls(controller,0.5,1.0,0.1,0,0);

      assertEquals(gunnerControlTester.getProportionalTurn(), 0.0,0.0);
    }

    @Test
    public void testGetProportionalSpeedIsNegativeOneWhenLeftStickIsOneAndSpeedFactorIsOne(){
        XboxController controller = mock(XboxController.class);

        when(controller.getY(Hand.kLeft)).thenReturn(1.0);

        GunnerControls gunnerControlTester = new GunnerControls(controller,0.0,1.0,0.1,0,0);

        assertEquals(gunnerControlTester.getProportionalSpeed(), -1.0,-0.9);
    }

    @Test
    public void testGetProportionalSpeedIsOneWhenLeftStickIsNegativeOneAndSpeedFactorIsOne(){
      XboxController controller = mock(XboxController.class);

      when(controller.getY(Hand.kLeft)).thenReturn(-1.0);

      GunnerControls gunnerControlTester = new GunnerControls(controller,0.0,1.0,0.1,0,0);

      assertEquals(gunnerControlTester.getProportionalSpeed(), 0.9,1.0);
    }

    @Test
    public void testGetProportionalSpeedIsZeroLeftStickIsZeroAndSpeedFactorIsOneHalf(){
      XboxController controller = mock(XboxController.class);

      when(controller.getY(Hand.kLeft)).thenReturn(0.0);

      GunnerControls gunnerControlTester = new GunnerControls(controller,0.0,0.5,0.1,0,0);

      assertEquals(gunnerControlTester.getProportionalSpeed(), 0.0,0.0);
    }

    @Test
    public void testGetEncoderTurnDifferentialIsOneWhenRightStickIsOneAndGunnerEncoderTurnRateIsOne(){
        XboxController controller = mock(XboxController.class);

        when(controller.getX(Hand.kRight)).thenReturn(1.0);

        GunnerControls gunnerControlTester = new GunnerControls(controller,0.0,0.0,0.1,0,1);

        assertEquals(gunnerControlTester.getEncoderTurnDifferential(), 0.9,1.0);
    }

    @Test
    public void testGetEncoderTurnDifferentialIsNegativeOneWhenRightStickIsNegativeOneAndGunnerEncoderTurnRateIsOne(){
      XboxController controller = mock(XboxController.class);

      when(controller.getX(Hand.kRight)).thenReturn(-1.0);

      GunnerControls gunnerControlTester = new GunnerControls(controller,0.0,0.0,0.1,0,1);

      assertEquals(gunnerControlTester.getEncoderTurnDifferential(), -1.0,-0.9);
    }

    @Test
    public void testGetEncoderTurnDifferentialIsZeroWhenRightStickIsZeroAndGunnerEncoderTurnRateIsTwo(){
      XboxController controller = mock(XboxController.class);

      when(controller.getX(Hand.kRight)).thenReturn(0.0);

      GunnerControls gunnerControlTester = new GunnerControls(controller,0.0,0.0,0.1,0,2);

      assertEquals(gunnerControlTester.getEncoderTurnDifferential(), 0.0,0.0);
    }

    @Test
    public void testGetEncoderSpeedIsNegativeOneWhenLeftStickIsOneAndGunnerEncoderSpeedIsOne(){
        XboxController controller = mock(XboxController.class);

        when(controller.getY(Hand.kLeft)).thenReturn(1.0);

        GunnerControls gunnerControlTester = new GunnerControls(controller,0.0,0.0,0.1,1,0);

        assertEquals(gunnerControlTester.getEncoderSpeed(), -1.0,-0.9);
    }

    @Test
    public void testGetEncoderSpeedIsOneWhenLeftStickIsNegativeOneAndGunnerEncoderSpeedIsOne(){
      XboxController controller = mock(XboxController.class);

      when(controller.getY(Hand.kLeft)).thenReturn(-1.0);

      GunnerControls gunnerControlTester = new GunnerControls(controller,0.0,0.0,0.1,1,0);

      assertEquals(gunnerControlTester.getEncoderSpeed(), 0.9,1.0);
    }

    @Test
    public void testGetEncoderSpeedIsZeroLeftStickIsZeroAndSpeedFactorIsTwo(){
      XboxController controller = mock(XboxController.class);

      when(controller.getY(Hand.kLeft)).thenReturn(0.0);

      GunnerControls gunnerControlTester = new GunnerControls(controller,0.0,0.5,0.1,2,0);

      assertEquals(gunnerControlTester.getEncoderSpeed(), 0.0,0.0);
    }
}