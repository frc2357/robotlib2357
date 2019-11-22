package com.systemmeltdown.robotlib.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;

public abstract class SubsystemBase extends Subsystem {
  private boolean failsafeActive;

  public SubsystemBase() {
    this(false);
  }

  public SubsystemBase(boolean failsafeActive) {
    this.failsafeActive = failsafeActive;
  }

  public boolean isFailsafeActive() {
    return failsafeActive;
  }

  public void setFailsafeActive(boolean failsafeActive) {
    this.failsafeActive = failsafeActive;
  }
}