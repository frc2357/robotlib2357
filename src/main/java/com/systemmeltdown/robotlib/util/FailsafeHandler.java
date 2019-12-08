package com.systemmeltdown.robotlib.util;

public interface FailsafeHandler {

  public boolean isFailsafeActive();

  public void setFailsafeActive(boolean failsafeActive);
}