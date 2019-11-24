package com.systemmeltdown.meltdownlog.outputs;

import com.systemmeltdown.meltdownlog.lib.LogEntryWriter;
import com.systemmeltdown.meltdownlog.lib.NanoTimeAnchor;

/**
 * Base class for any type of logging output
 */
public abstract class LogOutput implements LogEntryWriter {
	private NanoTimeAnchor m_timeAnchor;

	public LogOutput(NanoTimeAnchor timeAnchor) {
		m_timeAnchor = timeAnchor;
	}

	@Override
	public void notifySubscribe(String topicName, long nanos) {
		onTopicSubscribed(topicName, m_timeAnchor.convertNanosToRelative(nanos));
	}

	@Override
	public void notifyUnsubscribe(String topicName, long nanos) {
		onTopicUnsubscribed(topicName, m_timeAnchor.convertNanosToRelative(nanos));
	}

	@Override
	public void writeEntry(String topicName, Object value, long nanos) {
		onEntry(topicName, value, m_timeAnchor.convertNanosToRelative(nanos));
	}

	protected abstract void onTopicSubscribed(String topicName, long nanosFromStart);

	protected abstract void onTopicUnsubscribed(String topicName, long nanosFromStart);

	protected abstract void onEntry(String topicName, Object value, long nanosFromStart);
}
