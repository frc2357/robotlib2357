package com.systemmeltdown.meltdownlog.topics;

import java.util.HashMap;
import java.util.Map;

/**
 * Holds list of registered topics and allows sessions to subscribe to them.
 */
public class LogTopicRegistry {
	private static LogTopicRegistry instance;

	/**
	 * Gets the current instance of the registry.
	 */
	public static LogTopicRegistry getInstance() {
		if (instance == null) {
			instance = new LogTopicRegistry();
		}
		return instance;
	}

	private final Map<String, LogTopic> m_topics;

	/**
	 * Creates new topic registry.
	 * 
	 * This constructor is intentionally not public to restrict creation of registries
	 * to within this package.
	 */
	protected LogTopicRegistry() {
		m_topics = new HashMap<String, LogTopic>();
	}

	/**
	 * Gets a topic by its name.
	 * 
	 * @param topicName The name of the topic to get
	 * @return The topic or null if not found
	 */
	public LogTopic getTopic(String topicName) {
		return m_topics.get(topicName);
	}

	/**
	 * Adds a topic to this registry.
	 * 
	 * Note: This function intentionally not public to restrict usage to this package.
	 * 
	 * @param topic The topic to be added
	 * @throws RuntimeException If a topic by the same name already exists.
	 */
	protected void addTopic(final LogTopic topic) {
		if (m_topics.get(topic.getName()) != null) {
			// This is one of the few times we want to throw an exception in robot code.
			// These exceptions should always happen when subsystems are created so they should be "fail fast" 
			throw new RuntimeException("Topic '" + topic.getName() + "' already exists");
		}
		m_topics.put(topic.getName(), topic);
	}
}
