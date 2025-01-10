package frc.robot.commands.intake;

import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;

public class IntakeSetVelocity extends Command {
  private AngularVelocity m_velocity;

  public IntakeSetVelocity(AngularVelocity vel) {
    m_velocity = vel;
    addRequirements(Robot.intake);
  }

  @Override
  public void initialize() {
    Robot.intake.setVelocity(m_velocity);
  }

  @Override
  public boolean isFinished() {
    return false;
  }

  @Override
  public void end(boolean interrupted) {
    Robot.intake.stop();
  }
}
