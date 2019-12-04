package com.systemmeltdown.robotlog.topics;

public class BooleanTopic extends LogTopic {
	private boolean m_lastValue = false;
	private long m_lastNanos = Long.MIN_VALUE;

	public BooleanTopic(String name) {
		super(name, Boolean.class);
	}

	public void log(boolean value) {
		log(value, System.nanoTime());
	}

	public void log(boolean value, long nanos) {
		if (value == m_lastValue) {
			// Don't write the same value over and over again.
			m_lastValue = value;
			m_lastNanos = nanos;
		} else {
			if (m_lastNanos != Long.MIN_VALUE) {
				// We skipped over entries, so write the last one so we can get an accurate transition.
				writeEntry(m_lastValue, m_lastNanos);
			}

			writeEntry(value, nanos);

			m_lastValue = value;
			// Clear out lastNanos to indicate that we didn't skip over writing any entries.
			m_lastNanos = Long.MIN_VALUE;
		}
	}
}
