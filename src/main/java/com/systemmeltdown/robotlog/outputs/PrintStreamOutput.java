package com.systemmeltdown.robotlog.outputs;

import java.io.PrintStream;

public class PrintStreamOutput extends LogOutput {
	private final PrintStream m_stream;

	public PrintStreamOutput(final PrintStream stream) {
		m_stream = stream;
	}

	protected String nanosToTime(final long relativeNanos) {
		if (relativeNanos < 0) {
			return "<invalid>";
		}

		long relativeMillis = relativeNanos / 1000000L;
		long minutes = (relativeMillis / 1000) / 60;
		long seconds = (relativeMillis / 1000) % 60;
		long millis = (relativeMillis % 1000);

		return String.format("%02d:%02d.%03d", minutes, seconds, millis);
	}

	@Override
	protected void onStart(long relativeNanos) {
		m_stream.println(nanosToTime(relativeNanos) + " ( Session Start )");
	}

	@Override
	protected void onStop(long relativeNanos) {
		m_stream.println(nanosToTime(relativeNanos) + " ( Session Stop )");
	}

	@Override
	protected void onSubscribe(String topicName, long relativeNanos) {
		m_stream.println(nanosToTime(relativeNanos) + " [" + topicName + "]( Subscribed )");
	}

	@Override
	protected void onUnsubscribe(String topicName, long relativeNanos) {
		m_stream.println(nanosToTime(relativeNanos) + " [" + topicName + "]( Unsubscribed )");
	}

	@Override
	protected void onEntry(String topicName, Object value, long relativeNanos) {
		m_stream.println(nanosToTime(relativeNanos) + " [" + topicName + "]: " + value);
	}
}
