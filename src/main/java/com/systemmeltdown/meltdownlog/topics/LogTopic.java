package com.systemmeltdown.meltdownlog.topics;

import java.util.ArrayList;
import java.util.List;

import com.systemmeltdown.meltdownlog.lib.LogEntryWriter;

/**
 * Represents a single logging topic.
 * 
 * Each topic should represent a single data source or message stream.
 * (e.g. 'battery-volts', 'elevator-motor-amps', 'drive-error')
 */
public abstract class LogTopic {
	private final String m_name;
	private List<LogEntryWriter> m_subscribers;

	/**
	 * Topic constructor.
	 * @param name The name for the topic, in the format of 'system-units'
	 */
	protected LogTopic(final String name) {
		this(name, LogTopicRegistry.getInstance());
	}

	/**
	 * Full constructor.
	 * 
	 * This constructor is only called directly for testing purposes.
	 * @param name The name for the topic, in the format of 'system-units'
	 * @param registry The registry for this topic to be added
	 */
	protected LogTopic(final String name, final LogTopicRegistry registry) {
		m_name = name;
		m_subscribers = new ArrayList<LogEntryWriter>();
		registry.addTopic(this);

	}

	/**
	 * Gets this topic name
	 * 
	 * @return The name for this topic.
	 */
	public final String getName() {
		return m_name;
	}

	/**
	 * Checks if this topic has any subscribers.
	 * 
	 * @return true if any subscribers exist for this topic, false otherwise.
	 */
	public boolean hasSubscribers() {
		return m_subscribers.size() > 0;
	}

	/**
	 * Add a subscriber to this topic.
	 * 
	 * @param subscriber The subscriber to be added
	 * @return True if successful, false if already subscribed
	 */
	public boolean addSubscriber(LogEntryWriter subscriber) {
		return addSubscriber(subscriber, System.nanoTime());
	}

	public boolean addSubscriber(LogEntryWriter subscriber, long nanos) {
		if (m_subscribers.contains(subscriber)) {
			System.err.println("LogTopic: Cannot add subscriber, topic already has subscriber");
			return false;
		}
		m_subscribers.add(subscriber);
		subscriber.notifySubscribe(m_name, nanos);
		return true;
	}

	/**
	 * Remove a subscriber from this topic.
	 * 
	 * @param subscriber The previously added subscriber to be removed
	 * @return True if successful, false if not found
	 */
	public boolean removeSubscriber(LogEntryWriter subscriber) {
		return removeSubscriber(subscriber, System.nanoTime());
	}

	public boolean removeSubscriber(LogEntryWriter subscriber, long nanos) {
		if (!m_subscribers.contains(subscriber)) {
			System.err.println("LogTopic: Cannot remove subscriber, subscriber not found");
			return false;
		}
		m_subscribers.remove(subscriber);
		subscriber.notifyUnsubscribe(m_name, nanos);
		return true;
	}

	/**
	 * Writes a log entry out with a given time.
	 * 
	 * @param value The log entry value
	 * @param nanos The relative time for timestamping (should have been collected
	 *              via System.nanoTime())
	 */
	protected final void writeEntry(final Object value, final long nanos) {
		if (hasSubscribers()) {
			for (LogEntryWriter subscriber : m_subscribers) {
				subscriber.writeEntry(m_name, value, nanos);
			}
		}
	}
}
