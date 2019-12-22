package com.systemmeltdown.robotlog.outputs;

public interface LogWriter {
	public abstract void onStart(long relativeNanos);

	public abstract void onStop(long relativeNanos);

	public abstract void onSubscribe(String topicName, long relativeNanos);

	public abstract void onUnsubscribe(String topicName, long relativeNanos);

	public abstract void onEntry(String topicName, Object value, long relativeNanos);
}
