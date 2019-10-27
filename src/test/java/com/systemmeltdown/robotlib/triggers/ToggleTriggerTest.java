package com.systemmeltdown.robotlib.triggers;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.command.Command;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ToggleTriggerTest {
	@Test
	public void testGetReturnsTrue() {
    ShuffleboardTab tab = Shuffleboard.getTab("Test");
    NetworkTableEntry entry = tab.add("Get Returns True", true)
      .withWidget(BuiltInWidgets.kToggleButton)
      .getEntry();

		ToggleTrigger trigger = new ToggleTrigger( entry );

		assertEquals( trigger.get(), true );

		trigger.close();
	}

	@Test
	public void testGetReturnsFalseAfterClick() {
    ShuffleboardTab tab = Shuffleboard.getTab("Test");
    NetworkTableEntry entry = tab.add("Get Returns False After Click", true)
      .withWidget(BuiltInWidgets.kToggleButton)
      .getEntry();

    ToggleTrigger trigger = new ToggleTrigger( entry );

    entry.setBoolean(false);

		assertEquals( trigger.get(), false );

		trigger.close();
	}

  // TODO: create test for if command if started on trigger activation
	// @Test
	// public void testToggleTriggersCommand() {
  //   ShuffleboardTab tab = Shuffleboard.getTab("Test");
  //   NetworkTableEntry entry = tab.add("Toggle Triggers Command", false)
  //     .withWidget(BuiltInWidgets.kToggleButton)
  //     .getEntry();

  //   Command testCommand = mock( Command.class );
  //   doAnswer(new Answer<Void>() {
  //     @Override
  //     public Void answer(InvocationOnMock invocation) throws Throwable {
  //       System.out.println("answer test");
  //       return null;
  //     }
  //   }).when( testCommand ).start();

    
  //   ToggleTrigger trigger = new ToggleTrigger( entry );
  //   trigger.whenActive(testCommand);

  //   // changing the entry value, but doesn't change triggers active
    
  //   System.out.println(trigger.get());
  //   System.out.println("IsCompleted " + testCommand.isCompleted());
  //   System.out.println("IsCanceled " + testCommand.isCanceled());
  //   System.out.println("IsRunning " + testCommand.isRunning());
  //   assertEquals( testCommand.isCompleted(), true );
    
  //   testCommand.cancel();
	// 	trigger.close();
  // }
}