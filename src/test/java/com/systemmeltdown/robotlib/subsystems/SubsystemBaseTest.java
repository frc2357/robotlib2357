package com.systemmeltdown.robotlib.subsystems;

import org.junit.Test;

import edu.wpi.first.wpilibj.command.Subsystem;

import org.junit.Ignore;
import static org.junit.Assert.*;

public class SubsystemBaseTest {
	@Test
	public void testSetFailsafeIsActiveWhenSetToTrue() {
		TestSubsystem testSub = new TestSubsystem();

		testSub.setFailsafeActive(true);
    
		assertEquals(testSub.isFailsafeActive(), true );

		testSub.close();
	}

	@Test
	public void testSetFailsafeIsNotActiveWhenSetToFalse() {
		TestSubsystem testSub = new TestSubsystem();

		testSub.setFailsafeActive(false);
    
		assertEquals(testSub.isFailsafeActive(), false );

		testSub.close();
	}

	@Test
	public void testIsFailsafeIsNotActiveByDeefault() {
		TestSubsystem testSub = new TestSubsystem();

		assertEquals( testSub.isFailsafeActive(), false );

		testSub.close();
	}

	@Ignore
	public static class TestSubsystem extends Subsystem implements Fallible {
		private boolean failsafeActive = false;
		@Override
		public void initDefaultCommand(){
		}

		@Override
		public boolean isFailsafeActive() {
			return this.failsafeActive;
		}
	
		@Override
		public void setFailsafeActive(boolean failsafeActive) {
			this.failsafeActive = failsafeActive;
		}
	}
}