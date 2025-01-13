package frc.robot.commands.shooter;

import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;

public class ShooterSetWheelVelocity extends Command {
  private AngularVelocity m_velocity;

  public ShooterSetWheelVelocity(AngularVelocity vel) {
    m_velocity = vel;
    addRequirements(Robot.shooter);
  }

  @Override
  public void initialize() {
    Robot.shooter.setOutputVelocity(m_velocity);
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
