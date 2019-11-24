package com.systemmeltdown.meltdownlog.topics;

import com.systemmeltdown.meltdownlog.lib.Utils;

/**
 * Logs integers as a data topic.
 */
public class IntegerTopic extends LogTopic {
	private final int m_roundingFactor;
	private int m_lastRoundedValue = Integer.MIN_VALUE;
	private long m_lastNanos = Long.MIN_VALUE;

	public IntegerTopic(final String name, final int roundingFactor) {
		super(name);
		m_roundingFactor = roundingFactor;
	}

	public void log(final int value) {
		log(value, System.nanoTime());
	}

	public void log(final int value, final long nanos) {
		final int roundedValue = Utils.roundByFactor(value, m_roundingFactor);
		if (roundedValue == m_lastRoundedValue) {
			// Don't write the same value over and over again.
			m_lastRoundedValue = roundedValue;
			m_lastNanos = nanos;
		} else {
			if (m_lastNanos != Long.MIN_VALUE) {
				// We skipped over entries, so write the last one so we can get an accurate transition.
				writeEntry(m_lastRoundedValue, m_lastNanos);
			}

			writeEntry(roundedValue, nanos);

			m_lastRoundedValue = roundedValue;
			// Clear out lastNanos to indicate that we didn't skip over writing any entries.
			m_lastNanos = Long.MIN_VALUE;
		}
	}
}
