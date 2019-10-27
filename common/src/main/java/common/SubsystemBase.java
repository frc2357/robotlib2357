package common;

import edu.wpi.first.wpilibj.command.Subsystem;

public abstract class SubsystemBase extends Subsystem {
  private boolean failsafeActive = false;

  public boolean isFailsafeActive() {
    return failsafeActive;
  }

  public void setFailsafeActive(boolean failsafeActive) {
    this.failsafeActive = failsafeActive;
  }
}