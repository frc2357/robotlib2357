package com.systemmeltdown.robotlib.subsystems;

import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SubsystemBaseTest {
	@Test
	public void testSetFailsafeActive() {
		SubsystemBase subBase = new TestSubsystem();

		subBase.setFailsafeActive(true);
    
		assertEquals(subBase.isFailsafeActive(), true );

		subBase.close();
	}

	@Test
	public void testSetFailsafeNotActive() {
		SubsystemBase subBase = new TestSubsystem();

		subBase.setFailsafeActive(false);
    
		assertEquals(subBase.isFailsafeActive(), false );

		subBase.close();
	}

	@Test
	public void testIsFailsafeActive() {
        SubsystemBase subBase = mock(SubsystemBase.class);
        
		assertEquals( subBase.isFailsafeActive(), false );

		subBase.close();
	}

	@Ignore
	public static class TestSubsystem extends SubsystemBase {
		@Override
		public void initDefaultCommand(){
		}
	}
}

