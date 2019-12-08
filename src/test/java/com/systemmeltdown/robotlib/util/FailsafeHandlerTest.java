package com.systemmeltdown.robotlib.util;

import edu.wpi.first.wpilibj.command.Subsystem;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.*;

public class FailsafeHandlerTest {
	private TestFailsafeHandler testFailsafe;
	
	@Before
  	public void setup() {
		testFailsafe = new TestFailsafeHandler();
  	}

	@Test
	public void testSetFailsafeIsActiveWhenSetToTrue() {
		testFailsafe.setFailsafeActive(true);
    
		assertEquals(testFailsafe.isFailsafeActive(), true );

		testFailsafe.close();
	}

	@Test
	public void testSetFailsafeIsNotActiveWhenSetToFalse() {
		testFailsafe.setFailsafeActive(false);
    
		assertEquals(testFailsafe.isFailsafeActive(), false );

		testFailsafe.close();
	}

	@Test
	public void testIsFailsafeIsNotActiveByDeefault() {
		assertEquals( testFailsafe.isFailsafeActive(), false );

		testFailsafe.close();
	}

	@After
	public void after() {
		testFailsafe.close();
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