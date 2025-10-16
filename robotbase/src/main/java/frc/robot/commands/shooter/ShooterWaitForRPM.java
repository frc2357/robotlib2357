package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;

public class ShooterWaitForRPM extends Command {
  @Override
  public boolean isFinished() {
    return Robot.shooter.isAtTargetSpeed();
  }
}
