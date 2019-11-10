package com.systemmeltdown.robotlib.commands;

/**
 * A proportional drive control.
 *
 * This represents the simplest of drive system controls.
 */
public interface VelocityDrive {
  /**
   * Gets a turn value in encocer click differential.
   * @return Positive for clockwise, negative for counter-clockwise.
   */
  public int getEncoderTurnDifferential();

  /**
   * Gets a desired speed in encoder clicks.
   * @return Positive for forward, negative for backward.
   */
  public int getEncoderSpeed();
}