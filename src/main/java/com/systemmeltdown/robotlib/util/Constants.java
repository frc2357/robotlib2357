package com.systemmeltdown.robotlib.util;

public final class Constants {
    /**
   * Controls if Ggyro is reversed or not.
   */

  public static final boolean GYRO_REVERSED = true;

  /**
   * Encoder Constants
   */
  
  public static final boolean LEFT_ENCODER_REVERSED = false;
  public static final boolean RIGHT_ENCODER_REVERSED = true;

  public static final double WHEEL_DIAMETER_IN_METERS = 0.1524;
  public static final int ENCODER_CPR = 1024;

  public static final double ENCODER_DISTANCE_PER_PULSE = 
    (WHEEL_DIAMETER_IN_METERS * Math.PI) / (double) ENCODER_CPR;
}