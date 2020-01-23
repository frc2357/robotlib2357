package com.systemmeltdown.robotlog.testsession;

import java.util.HashMap;
import java.util.Map;

import com.systemmeltdown.robotlog.LogSession;
import com.systemmeltdown.robotlog.outputs.LogOutput;
import com.systemmeltdown.robotlog.outputs.PrintStreamOutput;

/**
 * RobotLog: Test Session
 * 
 * This is a command-line test session utility for RobotLog
 * 
 * To use: `./gradlew robotlogtestsession --args='<arguments>'`
 * 
 * Valid arguments:
 * 
 */
public class RobotLogTestSession {
  LogSession m_session;

  public RobotLogTestSession() {
    displayWelcome();
    installShutdownHook();

    new SineWave("Sine Wave", 0.01D, 1.0D, 5.0D, 0.25D);

    Map<String, LogOutput> outputs = new HashMap<String, LogOutput>();
    outputs.put("stdout", new PrintStreamOutput("RobotLog", System.out));

    m_session = new LogSession(outputs);
    m_session.subscribeTopic("Sine Wave", "stdout");
  }

  private void displayWelcome() {
    System.out.println(" _____     _       _   __            ");
    System.out.println("| __  |___| |_ ___| |_|  |   ___ ___ ");
    System.out.println("|    -| . | . | . |  _|  |__| . | . |");
    System.out.println("|__|__|___|___|___|_| |_____|___|_  |");
    System.out.println("   With <3 from Team FRC2357    |___|");
    System.out.println();
    System.out.println("-----------------------------");
    System.out.println("--- RobotLog Test Session ---");
    System.out.println("-----------------------------");
    System.out.flush();
  }

  private void installShutdownHook() {
    Runtime.getRuntime().addShutdownHook(new ShutdownHook());
  }

  private class ShutdownHook extends Thread {
    @Override
    public void run() {
      if (m_session != null) {
        m_session.stop();
      }
    }
  }

  public static void main(String[] args) {
    new RobotLogTestSession();
  }
}
