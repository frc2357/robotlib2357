package com.systemmeltdown.robotlib.triggers;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * A Shuffleboard toggle button trigger.
 *
 * This keeps track of when the button state changes for
 * events that should trigger on an edge.
 */
public class ToggleTrigger extends Trigger {
  private NetworkTableEntry entry;

  public ToggleTrigger(NetworkTableEntry entry) {
    this.entry = entry;
  }

  @Override
  public boolean get() {
    return this.entry.getBoolean(false);
  }
}