package com.systemmeltdown.meltdownlog.topics;

import org.junit.Test;
import org.mockito.Mockito;
import org.junit.Assert;
import org.junit.Ignore;

public class LogTopicRegistryTest {
	/**
	 * This is for other tests in other packages to use.
	 */
	@Ignore
	public static LogTopicRegistry createTestRegistry() {
		return new LogTopicRegistry();
	}

	@Test
	public void testAddTopic() {
		final LogTopicRegistry registry = new LogTopicRegistry();

		final LogTopic topic1 = new TestTopic("test-topic-1");
		final LogTopic topic2 = new TestTopic("test-topic-2");

		registry.addTopic(topic1);
		registry.addTopic(topic2);

		Assert.assertEquals(topic1, registry.getTopic("test-topic-1"));
		Assert.assertEquals(topic2, registry.getTopic("test-topic-2"));
		Assert.assertNull(registry.getTopic("test-topic-3"));
	}

	@Ignore
	private class TestTopic extends LogTopic {
		public TestTopic(final String name) {
			super(name, String.class, Mockito.mock(LogTopicRegistry.class));
		}
	}
}
