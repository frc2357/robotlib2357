package com.systemmeltdown.robotlog.topics;

import com.systemmeltdown.robotlog.lib.LogEntryWriter;

import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

public class StringTopicTest {
	@Test
	public void testLog() {
		final LogEntryWriter writer = Mockito.mock(LogEntryWriter.class);
		final StringTopic topic = new StringTopic("string-topic");
		final long nanos = System.nanoTime();
		topic.addSubscriber(writer);

		topic.log("zero", nanos + 0);
		topic.log("one", nanos + 100);
		topic.log("two", nanos + 200);
		topic.log("three", nanos + 300);
		topic.log("four", nanos + 400);
		topic.log("five", nanos + 500);
		topic.log("six", nanos + 600);
		topic.log("seven", nanos + 700);

		InOrder inOrder = Mockito.inOrder(writer);
		inOrder.verify(writer).writeEntry("string-topic", "zero", nanos + 0);
		inOrder.verify(writer).writeEntry("string-topic", "one", nanos + 100);
		inOrder.verify(writer).writeEntry("string-topic", "two", nanos + 200);
		inOrder.verify(writer).writeEntry("string-topic", "three", nanos + 300);
		inOrder.verify(writer).writeEntry("string-topic", "four", nanos + 400);
		inOrder.verify(writer).writeEntry("string-topic", "five", nanos + 500);
		inOrder.verify(writer).writeEntry("string-topic", "six", nanos + 600);
		inOrder.verify(writer).writeEntry("string-topic", "seven", nanos + 700);
	}
}