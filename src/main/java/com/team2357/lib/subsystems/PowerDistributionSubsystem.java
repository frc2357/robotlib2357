package com.team2357.lib.subsystems;

import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.PowerDistribution.ModuleType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class PowerDistributionSubsystem extends SubsystemBase {

  public static PowerDistributionSubsystem instance = null;

  public PowerDistribution m_pd;

  public static PowerDistributionSubsystem getInstance() {
    return instance;
  }

  public PowerDistributionSubsystem(int canId, ModuleType type) {
    m_pd = new PowerDistribution(canId, type);
    instance = this;
  }

  public PowerDistribution getPowerDistribution() {
    return m_pd;
  }
}
