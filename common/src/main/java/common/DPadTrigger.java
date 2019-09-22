package common;

import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.buttons.Trigger;

public class DPadTrigger extends Trigger {
  private JoystickButton[] buttons;

  public DPadTrigger(JoystickButton[] buttons) {
    this.buttons = buttons;
  }

  @Override
  public boolean get() {
    for (JoystickButton b : buttons) {
      if (! b.get()) {
        return false;
      }
    }
    // All buttons in chord are pressed!
    return true;
  }
}