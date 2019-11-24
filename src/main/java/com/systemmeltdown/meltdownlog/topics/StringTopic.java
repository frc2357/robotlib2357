package com.systemmeltdown.meltdownlog.topics;

/**
 * Simply logs messages at the given timestamp.
 */
public class StringTopic extends LogTopic {
	public StringTopic(String name) {
		super(name);
	}

	public void log(String message) {
		log(message, System.nanoTime());
	}

	public void log(String message, long nanos) {
		writeEntry(message, nanos);
	}
}
