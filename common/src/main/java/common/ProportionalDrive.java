package common;

/**
 * A proportional drive control.
 *
 * This represents the simplest of drive system controls.
 */
public interface ProportionalDrive {
  /**
   * Gets a proportional turn value.
   * @return A value between -1 (hard left), 0 (centered), and 1 (hard right)
   */
  public double getProportionalTurn();

  /**
   * Gets a proportional speed value.
   * @return A value between -1 (full reverse), 0 (centered), and 1 (full ahead)
   */
  public double getProportionalSpeed();
}