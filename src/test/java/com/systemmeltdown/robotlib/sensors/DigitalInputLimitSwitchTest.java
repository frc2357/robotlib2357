package com.systemmeltdown.robotlib.sensors;

import org.junit.Test;

import edu.wpi.first.wpilibj.DigitalInput;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DigitalInputLimitSwitchTest {
	@Test
	public void testDigitalInputGet() {
		DigitalInput input = mock( DigitalInput.class );

		DigitalInputLimitSwitch limit = new DigitalInputLimitSwitch( input );

		when( input.get() ).thenReturn( true );
		assertTrue( limit.isAtLimit() );

		when( input.get() ).thenReturn( false );
		assertFalse( limit.isAtLimit() );

		limit.close();
	}

	@Test
	public void testDigitalInputInverted() {
		DigitalInput input = mock( DigitalInput.class );

		DigitalInputLimitSwitch limit = new DigitalInputLimitSwitch( input, true );

		when( input.get() ).thenReturn( true );
		assertFalse( limit.isAtLimit() );

		when( input.get() ).thenReturn( false );
		assertTrue( limit.isAtLimit() );

		limit.close();
	}
}