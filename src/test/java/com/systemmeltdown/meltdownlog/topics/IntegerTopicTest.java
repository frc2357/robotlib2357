package com.systemmeltdown.meltdownlog.topics;

import com.systemmeltdown.meltdownlog.lib.LogEntryWriter;

import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

public class IntegerTopicTest {
	@Test
	public void testLog() {
		final LogEntryWriter writer = Mockito.mock(LogEntryWriter.class);
		final IntegerTopic topic = new IntegerTopic("integer-topic", 50);
		final long nanos = System.nanoTime();
		topic.addSubscriber(writer);

		topic.log(30, nanos + 0);
		topic.log(80, nanos + 100);
		topic.log(40, nanos + 200);
		topic.log(45, nanos + 300);
		topic.log(47, nanos + 400);
		topic.log(55, nanos + 500);
		topic.log(65, nanos + 600);
		topic.log(85, nanos + 700);

		InOrder inOrder = Mockito.inOrder(writer);
		inOrder.verify(writer).writeEntry("integer-topic", 50, nanos + 0);
		inOrder.verify(writer).writeEntry("integer-topic", 100, nanos + 100);
		inOrder.verify(writer).writeEntry("integer-topic", 50, nanos + 200);
		inOrder.verify(writer).writeEntry("integer-topic", 50, nanos + 600);
		inOrder.verify(writer).writeEntry("integer-topic", 100, nanos + 700);
	}
}