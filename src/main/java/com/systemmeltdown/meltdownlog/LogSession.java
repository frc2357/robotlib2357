package com.systemmeltdown.meltdownlog;

import com.systemmeltdown.meltdownlog.lib.NanoTimeReference;
import com.systemmeltdown.meltdownlog.outputs.LogOutput;
import com.systemmeltdown.meltdownlog.topics.LogTopic;
import com.systemmeltdown.meltdownlog.topics.LogTopicRegistry;

/**
 * Represents a time-based session of logging. Each session is pre-configured
 * and logs to its outputs after it's started until it is stoppped.
 */
public abstract class LogSession implements NanoTimeReference {
	protected final LogTopicRegistry m_topicRegistry;
	protected long m_startNanos = Long.MIN_VALUE;
	protected long m_stopNanos = Long.MIN_VALUE;

	public LogSession(final LogTopicRegistry topicRegistry) {
		m_topicRegistry = topicRegistry;
	}

	public boolean addTopicOutput(final LogOutput output, final String topicName) {
		return addTopicOutput(output, topicName, System.nanoTime());
	}

	protected boolean addTopicOutput(final LogOutput output, final String topicName, final long nanos) {
		final LogTopic topic = m_topicRegistry.getTopic(topicName);

		if (topic == null) {
			System.err.println("LogSession.addTopicOutput: topic by name '" + topicName + "' does not exist.");
			return false;
		}

		topic.addSubscriber(output, nanos);
		return true;
	}

	public boolean removeTopicOutput(final LogOutput output, final String topicName) {
		return removeTopicOutput(output, topicName, System.nanoTime());
	}

	protected boolean removeTopicOutput(final LogOutput output, final String topicName, final long nanos) {
		final LogTopic topic = m_topicRegistry.getTopic(topicName);

		if (topic == null) {
			System.err.println("LogSession.removeTopicOutput: topic by name '" + topicName + "' does not exist.");
			return false;
		}

		final boolean wasRemoved = topic.removeSubscriber(output, nanos);

		if (!wasRemoved) {
			System.err.println("LogSession.removeTopicOutput: output wasn't subscribed to topic '" + topicName + "'");
		}

		return true;
	}

	public long convertNanosToRelative(final long nanos) {
		if (m_startNanos == Long.MIN_VALUE) {
			return -1;
		}
		return nanos - m_startNanos;
	}

	public void start() {
		start(System.nanoTime());
	}

	protected void start(final long nanos) {
		if (m_startNanos != Long.MIN_VALUE) {
			System.err.println("LogSession: Cannot start again, sessions are NOT reusable. Create a new one.");
			return;
		}

		m_startNanos = nanos;
	}

	public void stop() {
		stop(System.nanoTime());
	}

	protected void stop(final long nanos) {
		if (m_startNanos == Long.MIN_VALUE) {
			System.err.println("LogSession: Cannot stop. Session not yet started");
			return;
		}

		m_stopNanos = nanos;
	}
}
