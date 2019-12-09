package com.systemmeltdown.robotlog;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.systemmeltdown.robotlog.outputs.LogOutput;
import com.systemmeltdown.robotlog.topics.LogTopic;
import com.systemmeltdown.robotlog.topics.LogTopicRegistry;
import com.systemmeltdown.robotlog.topics.LogTopicRegistryTest;

public class LogSessionTest {
	@Ignore
	public static class TestTopic extends LogTopic {
		public TestTopic(final String name, final LogTopicRegistry registry) {
			super(name, String.class, registry);
		}
	}

	@Test
	public void testStartStop() {
		final LogTopicRegistry topicRegistry = LogTopicRegistryTest.createTestRegistry();

		final LogOutput testOutput = Mockito.mock(LogOutput.class);
		when(testOutput.getName()).thenReturn("test-output");

		final TestSession session = new TestSession(new LogOutput[] { testOutput }, topicRegistry);

		long startNanos = 1000000000L;
		long stopNanos = 3000000000L;

		Assert.assertEquals(-1, session.timeSinceStartNanos(1003000000L));

		session.start(startNanos);

		Assert.assertEquals(1, session.onStartCalled);
		Assert.assertEquals(0, session.onStopCalled);

		Assert.assertEquals(1L, session.timeSinceStartNanos(1000000001L));
		Assert.assertEquals(3000000L, session.timeSinceStartNanos(1003000000L));
		Assert.assertEquals(8000000000L, session.timeSinceStartNanos(9000000000L));
		Assert.assertEquals(2000000000L, session.timeSinceStartNanos(stopNanos));

		session.stop(stopNanos);
		Assert.assertEquals(1, session.onStartCalled);
		Assert.assertEquals(1, session.onStopCalled);
	}

	@Test
	public void testSubscribeUnsubscribe() {
		final LogTopicRegistry topicRegistry = Mockito.mock(LogTopicRegistry.class);
		final LogTopic testTopic = new TestTopic("test-topic", topicRegistry);
		when(topicRegistry.getTopic("test-topic")).thenReturn(testTopic);

		final LogOutput testOutput = Mockito.mock(LogOutput.class);
		when(testOutput.getName()).thenReturn("test-output");

		final LogSession session = new TestSession(new LogOutput[] { testOutput }, topicRegistry);
		session.start(1000000000L);

		session.subscribeTopic("test-topic", "test-output", 1000000000L);
		verify(testOutput).notifySubscribe("test-topic", 1000000000L);

		session.unsubscribeTopic("test-topic", "test-output", 2000000000L);
		verify(testOutput).notifyUnsubscribe("test-topic", 2000000000L);

		session.stop(3000000000L);
	}

	@Ignore
	private class TestSession extends LogSession {
		int onStartCalled = 0;
		int onStopCalled = 0;

		TestSession(LogOutput[] outputs, LogTopicRegistry topicRegistry) {
			super(outputs, topicRegistry);
		}

		protected void onStart() {
			onStartCalled++;
		}

		protected void onStop() {
			onStopCalled++;
		}
	}
}