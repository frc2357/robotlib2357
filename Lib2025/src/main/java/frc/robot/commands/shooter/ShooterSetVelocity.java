package frc.robot.commands.shooter;

import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;

public class ShooterSetVelocity extends Command {
  private AngularVelocity m_velocity;

  public ShooterSetVelocity(AngularVelocity vel) {
    m_velocity = vel;
    addRequirements(Robot.shooter);
  }

  @Override
  public void initialize() {
    Robot.shooter.setVelocity(m_velocity);
  }

  @Override
  public boolean isFinished() {
    return false;
  }

  @Override
  public void end(boolean interrupted) {
    Robot.shooter.stop();
  }
}
