package com.systemmeltdown.robotlib.subsystems;

import com.systemmeltdown.robotlib.subsystems.SubsystemBase;
import org.junit.Test;
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
		
	}

	@Test
	public void testIsFailsafeActive() {
        SubsystemBase subBase = mock(SubsystemBase.class);
        

		assertEquals( subBase.isFailsafeActive(), false );

		subBase.close();
	}

	private class TestSubsystem extends SubsystemBase{
		@Override
		public void initDefaultCommand(){
		}
	}
}

