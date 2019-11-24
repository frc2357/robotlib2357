package com.systemmeltdown.meltdownlog.topics;

import com.systemmeltdown.meltdownlog.lib.Utils;

public class DoubleTopic extends LogTopic {
	private double m_roundingFactor;
	private double m_lastRoundedValue = Double.MIN_VALUE;
	private long m_lastNanos = Long.MIN_VALUE;

	public DoubleTopic(String name, double roundingFactor) {
		super(name);
		m_roundingFactor = roundingFactor;
	}

	public void log(double value) {
		log(value, System.nanoTime());
	}

	public void log(double value, long nanos) {
		final double roundedValue = Utils.roundByFactor(value, m_roundingFactor);
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
