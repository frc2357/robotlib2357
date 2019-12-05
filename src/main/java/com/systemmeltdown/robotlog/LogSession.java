package com.systemmeltdown.robotlog;

import com.systemmeltdown.robotlog.lib.NanoTimeReference;
import com.systemmeltdown.robotlog.outputs.LogOutput;
import com.systemmeltdown.robotlog.topics.LogTopic;
import com.systemmeltdown.robotlog.topics.LogTopicRegistry;

/**
 * Represents a time-based session of logging. Each session is pre-configured
 * and logs to its outputs after it's started until it is stoppped.
 */
public abstract class LogSession implements NanoTimeReference {
	protected final LogTopicRegistry m_topicRegistry;
	protected long m_startNanos = Long.MIN_VALUE;
	protected long m_stopNanos = Long.MIN_VALUE;

	public LogSession() {
		this(LogTopicRegistry.getInstance());
	}

	public LogSession(final LogTopicRegistry topicRegistry) {
		m_topicRegistry = topicRegistry;
	}

	public boolean subscribeTopic(final LogOutput output, final String topicName) {
		return subscribeTopic(output, topicName, System.nanoTime());
	}

	protected boolean subscribeTopic(final LogOutput output, final String topicName, final long nanos) {
		final LogTopic topic = m_topicRegistry.getTopic(topicName);

		if (topic == null) {
			System.err.println("LogSession.addTopicOutput: topic by name '" + topicName + "' does not exist.");
			return false;
		}

		topic.addSubscriber(output, nanos);
		return true;
	}

	public boolean unsubscribeTopic(final LogOutput output, final String topicName) {
		return unsubscribeTopic(output, topicName, System.nanoTime());
	}

	protected boolean unsubscribeTopic(final LogOutput output, final String topicName, final long nanos) {
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
		onStart();
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
		onStop();
	}

	/**
	 * Called when this session is started.
	 */
	protected abstract void onStart();

	/**
	 * Called when this session is stopped.
	 */
	protected abstract void onStop();
}
