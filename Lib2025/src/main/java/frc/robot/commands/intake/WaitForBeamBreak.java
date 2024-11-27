package frc.robot.commands.intake;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;

public class WaitForBeamBreak extends Command {
  @Override
  public boolean isFinished() {
    return Robot.intake.isBeamBroken();
  }
}
