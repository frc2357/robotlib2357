package com.systemmeltdown.meltdownlog;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.systemmeltdown.meltdownlog.outputs.LogOutput;
import com.systemmeltdown.meltdownlog.topics.LogTopic;
import com.systemmeltdown.meltdownlog.topics.LogTopicRegistry;
import com.systemmeltdown.meltdownlog.topics.LogTopicRegistryTest;

public class LogSessionTest {
	@Ignore
	public static class TestTopic extends LogTopic {
		public TestTopic(final String name, final LogTopicRegistry registry) {
			super(name, registry);
		}
	}

	@Test
	public void testAddRemoveOutput() {
		final LogTopicRegistry topicRegistry = Mockito.mock(LogTopicRegistry.class);
		final LogTopic testTopic = new TestTopic("test-topic", topicRegistry);
		when(topicRegistry.getTopic("test-topic")).thenReturn(testTopic);

		final LogOutput output = Mockito.mock(LogOutput.class);
		final LogSession session = new TestSession(topicRegistry);

		session.addTopicOutput(output, "test-topic", 1000000000L);
		verify(output).notifySubscribe("test-topic", 1000000000L);

		session.removeTopicOutput(output, "test-topic", 2000000000L);
		verify(output).notifyUnsubscribe("test-topic", 2000000000L);
	}

	@Test
	public void testStartStop() {
		final LogTopicRegistry topicRegistry = LogTopicRegistryTest.createTestRegistry();
		final LogSession session = new TestSession(topicRegistry);

		long startNanos = 1000000000L;
		long stopNanos = 3000000000L;

		Assert.assertEquals(-1, session.convertNanosToRelative(1003000000L));

		session.start(startNanos);

		Assert.assertEquals(startNanos, session.m_startNanos);

		Assert.assertEquals(1L, session.convertNanosToRelative(1000000001L));
		Assert.assertEquals(3000000L, session.convertNanosToRelative(1003000000L));
		Assert.assertEquals(8000000000L, session.convertNanosToRelative(9000000000L));
		Assert.assertEquals(2000000000L, session.convertNanosToRelative(stopNanos));

		session.stop(stopNanos);

		Assert.assertEquals(stopNanos, session.m_stopNanos);
	}

	@Ignore
	private class TestSession extends LogSession {
		TestSession(LogTopicRegistry topicRegistry) {
			super(topicRegistry);
		}
	}
}