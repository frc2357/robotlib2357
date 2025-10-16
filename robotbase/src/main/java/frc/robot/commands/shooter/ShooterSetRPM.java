package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;

public class ShooterSetRPM extends Command {
  private double m_RPM;

  public ShooterSetRPM(double RPM) {
    m_RPM = RPM;
    addRequirements(Robot.shooter);
  }

  @Override
  public void initialize() {
    Robot.shooter.setRPM(m_RPM);
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
