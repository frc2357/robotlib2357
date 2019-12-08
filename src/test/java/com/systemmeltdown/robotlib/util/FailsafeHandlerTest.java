package com.systemmeltdown.robotlib.util;

import edu.wpi.first.wpilibj.command.Subsystem;
import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.*;

public class FailsafeHandlerTest {
	@Test
	public void testSetFailsafeIsActiveWhenSetToTrue() {
		TestFailsafeHandler testSub = new TestFailsafeHandler();

		testSub.setFailsafeActive(true);
    
		assertEquals(testSub.isFailsafeActive(), true );

		testSub.close();
	}

	@Test
	public void testSetFailsafeIsNotActiveWhenSetToFalse() {
		TestFailsafeHandler testSub = new TestFailsafeHandler();

		testSub.setFailsafeActive(false);
    
		assertEquals(testSub.isFailsafeActive(), false );

		testSub.close();
	}

	@Test
	public void testIsFailsafeIsNotActiveByDeefault() {
		TestFailsafeHandler testSub = new TestFailsafeHandler();

		assertEquals( testSub.isFailsafeActive(), false );

		testSub.close();
	}

	@Ignore
	public static class TestFailsafeHandler extends Subsystem implements FailsafeHandler {
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