package com.systemmeltdown.robotlog.topics;

import com.systemmeltdown.robotlog.lib.LogEntryWriter;

import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

public class DoubleTopicTest {
	@Test
	public void testLog() {
		final LogEntryWriter writer = Mockito.mock(LogEntryWriter.class);
		final DoubleTopic topic = new DoubleTopic("double-topic", 0.5);
		final long nanos = System.nanoTime();
		topic.addSubscriber(writer);

		topic.log(0.30, nanos + 0);
		topic.log(0.80, nanos + 100);
		topic.log(0.40, nanos + 200);
		topic.log(0.45, nanos + 300);
		topic.log(0.47, nanos + 400);
		topic.log(0.55, nanos + 500);
		topic.log(0.65, nanos + 600);
		topic.log(0.85, nanos + 700);

		InOrder inOrder = Mockito.inOrder(writer);
		inOrder.verify(writer).writeEntry("double-topic", 0.50, nanos + 0);
		inOrder.verify(writer).writeEntry("double-topic", 1.00, nanos + 100);
		inOrder.verify(writer).writeEntry("double-topic", 0.50, nanos + 200);
		inOrder.verify(writer).writeEntry("double-topic", 0.50, nanos + 600);
		inOrder.verify(writer).writeEntry("double-topic", 1.00, nanos + 700);
	}
}