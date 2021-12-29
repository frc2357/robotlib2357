package com.systemmeltdown.robotlib.commands;

import com.systemmeltdown.robotlog.topics.LogTopicRegistry;
import com.systemmeltdown.robotlog.topics.StringTopic;

import edu.wpi.first.wpilibj2.command.CommandBase;

/**
 * A base for commands that has the ability to log commands.
 */
abstract class CommandLoggerBase extends CommandBase {
    private StringTopic m_commandTopic;
    private static final String COMMAND_TOPIC_NAME = "Command Topic";

    public CommandLoggerBase() {
        m_commandTopic = (StringTopic) LogTopicRegistry.getInstance().getTopic(COMMAND_TOPIC_NAME);

        if (m_commandTopic == null) {
            m_commandTopic = new StringTopic(COMMAND_TOPIC_NAME);
        }
    }

    private void logCmd(String status) {
        m_commandTopic.log(getClass().getSimpleName() + " " + status);
    }

    @Override
    public void initialize() {
        logCmd("initialized");
    }

    @Override
    public void end(boolean interrupted) {
        if (interrupted) {
            logCmd("interrupted");
        } else {
            logCmd("ended");
        }
    }
}