package com.systemmeltdown.robotlog.outputs;

import com.systemmeltdown.robotlog.lib.LogEntryWriter;
import com.systemmeltdown.robotlog.lib.NanoTimeReference;

/**
 * Base class for any type of logging output
 */
public abstract class LogOutput implements LogEntryWriter {
	private NanoTimeReference m_timeReference;

	public LogOutput(NanoTimeReference timeReference) {
		m_timeReference = timeReference;
	}

	@Override
	public void notifySubscribe(String topicName, long nanos) {
		onTopicSubscribed(topicName, m_timeReference.convertNanosToRelative(nanos));
	}

	@Override
	public void notifyUnsubscribe(String topicName, long nanos) {
		onTopicUnsubscribed(topicName, m_timeReference.convertNanosToRelative(nanos));
	}

	@Override
	public void writeEntry(String topicName, Object value, long nanos) {
		onEntry(topicName, value, m_timeReference.convertNanosToRelative(nanos));
	}

	protected abstract void onTopicSubscribed(String topicName, long nanosFromStart);

	protected abstract void onTopicUnsubscribed(String topicName, long nanosFromStart);

	protected abstract void onEntry(String topicName, Object value, long nanosFromStart);
}
