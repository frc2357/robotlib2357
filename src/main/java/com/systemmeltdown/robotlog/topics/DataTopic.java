package com.systemmeltdown.robotlog.topics;

/**
 * Sends data updates, omitting redundant values.
 */
public abstract class DataTopic extends LogTopic {
	private Object m_lastValue = null;
	private long m_lastDuplicateNanos = Long.MIN_VALUE;

	protected DataTopic(String name, Class<?> valueType) {
		super(name, valueType);
	}

	protected void log(Object value, long nanos) {
		if (m_lastValue != null && m_lastValue.equals(value)) {
			// Don't log duplicates, but keep track of the last timestamp for them
			m_lastDuplicateNanos = nanos;
			return;
		}

		if (m_lastDuplicateNanos != Long.MIN_VALUE) {
			// Finish reporting the duplicate values before reporting this one.
			writeEntry(m_lastValue, m_lastDuplicateNanos);
		}

		writeEntry(value, nanos);

		// Now we don't have any unreported values.
		m_lastDuplicateNanos = Long.MIN_VALUE;
	}
}
