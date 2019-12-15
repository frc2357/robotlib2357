package com.systemmeltdown.robotlog.outputs;

import com.systemmeltdown.robotlog.lib.LogEntryWriter;
import com.systemmeltdown.robotlog.lib.RelativeTimeSource;

/**
 * Base class for any type of logging output
 */
public abstract class LogOutput implements LogEntryWriter {
	private RelativeTimeSource m_timeSource;

	public final boolean start(RelativeTimeSource timeSource, long nanos) {
		if (m_timeSource != null) {
			System.err.println("LogOutput.start: Already started.");
			return false;
		}

		m_timeSource = timeSource;
		onStart(m_timeSource.convertToRelativeNanos(nanos));
		return true;
	}

	public final boolean stop(long nanos) {
		if (m_timeSource == null) {
			System.err.println("LogOutput.stop: Cannot stop. Not yet started");
			return false;
		}

		onStop(m_timeSource.convertToRelativeNanos(nanos));
		m_timeSource = null;
		return true;
	}

	@Override
	public final void notifySubscribe(String topicName, long nanos) {
		onSubscribe(topicName, m_timeSource.convertToRelativeNanos(nanos));
	}

	@Override
	public final void notifyUnsubscribe(String topicName, long nanos) {
		onUnsubscribe(topicName, m_timeSource.convertToRelativeNanos(nanos));
	}

	@Override
	public final void writeEntry(String topicName, Object value, long nanos) {
		onEntry(topicName, value, m_timeSource.convertToRelativeNanos(nanos));
	}

	protected abstract void onStart(long relativeNanos);

	protected abstract void onStop(long relativeNanos);

	protected abstract void onSubscribe(String topicName, long relativeNanos);

	protected abstract void onUnsubscribe(String topicName, long relativeNanos);

	protected abstract void onEntry(String topicName, Object value, long relativeNanos);
}
