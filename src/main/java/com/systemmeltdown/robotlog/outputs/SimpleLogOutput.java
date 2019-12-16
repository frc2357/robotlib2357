package com.systemmeltdown.robotlog.outputs;

import com.systemmeltdown.robotlog.lib.RelativeTimeSource;

/**
 * Simple synchronous logging output
 */
public class SimpleLogOutput implements LogOutput {
	private RelativeTimeSource m_timeSource;
	private LogWriter m_logWriter;

	protected SimpleLogOutput(LogWriter logWriter) {
		m_logWriter = logWriter;
	}

	@Override
	public final boolean start(RelativeTimeSource timeSource, long nanos) {
		if (m_timeSource != null) {
			System.err.println("LogOutput.start: Already started.");
			return false;
		}

		m_timeSource = timeSource;
		m_logWriter.onStart(m_timeSource.convertToRelativeNanos(nanos));
		return true;
	}

	@Override
	public final boolean stop(long nanos) {
		if (m_timeSource == null) {
			System.err.println("LogOutput.stop: Cannot stop. Not yet started");
			return false;
		}

		m_logWriter.onStop(m_timeSource.convertToRelativeNanos(nanos));
		m_timeSource = null;
		return true;
	}

	@Override
	public final void notifySubscribe(String topicName, long nanos) {
		m_logWriter.onSubscribe(topicName, m_timeSource.convertToRelativeNanos(nanos));
	}

	@Override
	public final void notifyUnsubscribe(String topicName, long nanos) {
		m_logWriter.onUnsubscribe(topicName, m_timeSource.convertToRelativeNanos(nanos));
	}

	@Override
	public final void writeEntry(String topicName, Object value, long nanos) {
		m_logWriter.onEntry(topicName, value, m_timeSource.convertToRelativeNanos(nanos));
	}
}