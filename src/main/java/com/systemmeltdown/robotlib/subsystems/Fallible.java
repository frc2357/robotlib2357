package com.systemmeltdown.robotlib.subsystems;

public interface Fallible {

  public boolean isFailsafeActive();

  public void setFailsafeActive(boolean failsafeActive);
}