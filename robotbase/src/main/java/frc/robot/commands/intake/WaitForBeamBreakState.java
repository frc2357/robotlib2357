package frc.robot.commands.intake;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;

public class WaitForBeamBreakState extends Command {
  private boolean m_state;

  public WaitForBeamBreakState() {
    this(true);
  }

  public WaitForBeamBreakState(boolean state) {
    m_state = state;
  }

  @Override
  public boolean isFinished() {
    return Robot.intake.isBeamBroken() == m_state;
  }
}
