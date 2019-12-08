package com.systemmeltdown.robotlog.lib;

public interface LogEntryWriter {
	/**
	 * Notifies a writer that it has been subscribed to a topic.
	 * @param topicName The name of the topic which has been subscribed
	 * @param nanos The System.nanoTime() value for when the subscription occurred
	 */
	public void notifySubscribe(String topicName, long nanos);

	/**
	 * Notifies a writer that it has been unsubscribed from a topic.
	 * @param topicName The name of the topic which has been unsubscribed
	 * @param nanos The System.nanoTime() value for when the unsubscription occurred
	 */
	public void notifyUnsubscribe(String topicName, long nanos);

	/**
	 * Notifies when a log entry occurs.
	 * @param topicName The name of the topic to log
	 * @param value The value to be written
	 * @param nanos The System.nanoTime() value for this entry
	 */
	public void writeEntry(String topicName, Object value, long nanos);
}
