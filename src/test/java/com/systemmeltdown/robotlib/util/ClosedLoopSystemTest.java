package com.systemmeltdown.robotlib.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.*;

public class ClosedLoopSystemTest {
	private m_testClosedLoopSystem m_testClosedLoop;
	
	@Before
  	public void setup() {
		m_testClosedLoop = new m_testClosedLoopSystem();	
  	}

	@Test
	public void testIsClosedLoopEnabledIsEnabledWhenSetToTrue() {
		m_testClosedLoop.setClosedLoopEnabled(true);
    
		assertEquals(m_testClosedLoop.isClosedLoopEnabled(), true );
	}

	@Test
	public void testIsClosedLoopEnabledIsNotEnabledWhenSetToFalse() {
		m_testClosedLoop.setClosedLoopEnabled(false);
    
		assertEquals(m_testClosedLoop.isClosedLoopEnabled(), false );
	}

	@Test
	public void testIsClosedLoopEnabledIsActiveByDefault() {
		assertEquals( m_testClosedLoop.isClosedLoopEnabled(), true );
	}

	@Ignore
	public static class m_testClosedLoopSystem implements ClosedLoopSystem {
		private boolean closedLoopEnabled = true;

		@Override
		public boolean isClosedLoopEnabled() {
			return this.closedLoopEnabled;
		}
	
		@Override
		public void setClosedLoopEnabled(boolean closedLoopEnabled) {
			this.closedLoopEnabled = closedLoopEnabled;
		}
	}
}