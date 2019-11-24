package com.systemmeltdown.robotlib.controllers;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ControlsTest{
    @Test
    public void TestIsActiveIsFalseWhenBothSticksZero(){
        XboxController controller = mock(XboxController.class);

        when(controller.getY(Hand.kLeft)).thenReturn(0.0);
        when(controller.getX(Hand.kRight)).thenReturn(0.0);

        Controls controlTester = new Controls(controller, 0.1);

        assertEquals(controlTester.isActive(), false);
    }

    @Test
    public void TestIsActiveIsFalseWhenLeftStickIsUnderDeadband(){
        XboxController controller = mock(XboxController.class);

        when(controller.getY(Hand.kLeft)).thenReturn(0.09);
        when(controller.getX(Hand.kRight)).thenReturn(0.0);

        Controls controlTester = new Controls(controller, 0.1);

        assertEquals(controlTester.isActive(), false);
    }

    @Test
    public void TestIsActiveIsTrueWhenLeftStickIsAtDeadband(){
        XboxController controller = mock(XboxController.class);

        when(controller.getY(Hand.kLeft)).thenReturn(0.1);
        when(controller.getX(Hand.kRight)).thenReturn(0.0);

        Controls controlTester = new Controls(controller, 0.1);

        assertEquals(controlTester.isActive(), true);
    }

    @Test
    public void TestIsActiveIsTrueWhenLeftStickIsAboveDeadband(){
        XboxController controller = mock(XboxController.class);

        when(controller.getY(Hand.kLeft)).thenReturn(0.2);
        when(controller.getX(Hand.kRight)).thenReturn(0.0);

        Controls controlTester = new Controls(controller, 0.1);

        assertEquals(controlTester.isActive(), true);
    }

    @Test
    public void TestIsActiveIsFalseWhenRightStickIsUnderDeadband(){
        XboxController controller = mock(XboxController.class);

        when(controller.getY(Hand.kLeft)).thenReturn(0.0);
        when(controller.getX(Hand.kRight)).thenReturn(0.09);

        Controls controlTester = new Controls(controller, 0.1);

        assertEquals(controlTester.isActive(), false);
    }

    @Test
    public void TestIsActiveIsTrueWhenRightStickIsAtDeadband(){
        XboxController controller = mock(XboxController.class);

        when(controller.getY(Hand.kLeft)).thenReturn(0.0);
        when(controller.getX(Hand.kRight)).thenReturn(0.1);

        Controls controlTester = new Controls(controller, 0.1);

        assertEquals(controlTester.isActive(), true);
    }

    @Test
    public void TestIsActiveIsTrueWhenRightStickIsAboveDeadband(){
        XboxController controller = mock(XboxController.class);

        when(controller.getY(Hand.kLeft)).thenReturn(0.0);
        when(controller.getX(Hand.kRight)).thenReturn(0.2);

        Controls controlTester = new Controls(controller, 0.1);

        assertEquals(controlTester.isActive(), true);
    }

    @Test
    public void TestIsActiveIsTrueWhenBothSticksAreAboveDeadband(){
        XboxController controller = mock(XboxController.class);

        when(controller.getY(Hand.kLeft)).thenReturn(0.2);
        when(controller.getX(Hand.kRight)).thenReturn(0.2);

        Controls controlTester = new Controls(controller, 0.1);

        assertEquals(controlTester.isActive(), true);
    }

}
