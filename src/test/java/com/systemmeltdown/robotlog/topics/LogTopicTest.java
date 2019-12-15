package com.systemmeltdown.robotlog.topics;

import org.junit.Test;
import org.mockito.Mockito;
import org.junit.Assert;
import org.junit.Ignore;
import static org.mockito.Mockito.*;

import com.systemmeltdown.robotlog.lib.LogEntryWriter;

public class LogTopicTest {
	@Ignore
	public static class TestTopic extends LogTopic {
		public TestTopic(final String name, final LogTopicRegistry registry) {
			super(name, String.class, registry);
		}
	}

	@Test
	public void testConstructor() {
		final LogTopicRegistry registry = Mockito.mock(LogTopicRegistry.class);
		final TestTopic topic = new TestTopic("test-topic", registry);

		Assert.assertEquals("test-topic", topic.getName());
		verify(registry).addTopic(topic);
	}

	@Test
	public void testSubscribers() {
		final LogTopicRegistry registry = Mockito.mock(LogTopicRegistry.class);
		final TestTopic topic = new TestTopic("test-topic", registry);
		final LogEntryWriter subscriber1 = Mockito.mock(LogEntryWriter.class);
		final LogEntryWriter subscriber2 = Mockito.mock(LogEntryWriter.class);

		Assert.assertFalse(topic.hasSubscribers());

		topic.addSubscriber(subscriber1);
		Assert.assertTrue(topic.hasSubscribers());

		topic.writeEntry("test-value", 1000000000L);
		verify(subscriber1).writeEntry("test-topic", "test-value", 1000000000L);

		topic.addSubscriber(subscriber2);
		Assert.assertTrue(topic.hasSubscribers());

		topic.writeEntry("test-value2", 2000000000L);
		verify(subscriber1).writeEntry("test-topic", "test-value2", 2000000000L);
		verify(subscriber2).writeEntry("test-topic", "test-value2", 2000000000L);

		topic.removeSubscriber(subscriber1);
		Assert.assertTrue(topic.hasSubscribers());

		topic.writeEntry("test-value3", 3000000000L);
		verify(subscriber2).writeEntry("test-topic", "test-value3", 3000000000L);

		topic.removeSubscriber(subscriber2);
		Assert.assertFalse(topic.hasSubscribers());

	}
}
