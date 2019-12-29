package com.systemmeltdown.robotlib.subsystems.pdp;

import com.systemmeltdown.robotlog.topics.DoubleTopic;
import com.systemmeltdown.robotlog.topics.StringTopic;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Manages logging and info for PDP
 * 
 * TODO: Add unit tests.
<<<<<<< HEAD
 * TODO: Add amperage threshold logging to info
=======
>>>>>>> Add PDPSubsystem
 */
public class PDPSubsystem extends Subsystem {
  private final StringTopic errorTopic = new StringTopic("PDP Error");
  private final StringTopic infoTopic = new StringTopic("PDP Info");
  private final StringTopic debugTopic = new StringTopic("PDP Debug");
  private final DoubleTopic batteryVoltsTopic = new DoubleTopic("Battery Volts", 0.25);
  private final DoubleTopic totalAmpsTopic = new DoubleTopic("Total Amps", 0.25);
<<<<<<< HEAD
  private final DoubleTopic temperatureTopic = new DoubleTopic("PDP Temp", 5.0);
=======
  private final DoubleTopic temperatureTopic = new DoubleTopic("PDP Temp", 1.0);
>>>>>>> Add PDPSubsystem
  private final PowerDistributionPanel m_pdp;

  public PDPSubsystem(int canId) {
    super("PDP Subsystem");
    infoTopic.log("PDPSubsystem: canId=" + canId);

    m_pdp = new PowerDistributionPanel(canId);
    debugTopic.log("pdp object created for canId of " + canId);
  }

  /**
   * Testing constructor
   * @param pdp PDP mock
   */
  PDPSubsystem(PowerDistributionPanel pdp) {
    super("PDP Subsystem");
    m_pdp = pdp;
  }

  /**
   * Gets the current for a given PDP channel.
   * @param channel The PDP power channel (0-15)
   * @return The current in amps.
   */
  public double getAmps(int channel) {
    if (channel < 0 || channel > 15) {
      errorTopic.log("getCurrent: invalid channel: " + channel);
      return Double.NaN;
    }
    return m_pdp.getCurrent(channel);
  }

  @Override
  public void periodic() {
    super.periodic();
    batteryVoltsTopic.log(m_pdp.getVoltage());
    totalAmpsTopic.log(m_pdp.getTotalCurrent());
    temperatureTopic.log(m_pdp.getTemperature());
  }

  @Override
  protected void initDefaultCommand() {
  }
}
