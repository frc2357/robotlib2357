package frc.robot.commands.intake;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;

public class IntakeSetRPM extends Command {
  private double m_RPM;

  public IntakeSetRPM(double RPM) {
    m_RPM = RPM;
    addRequirements(Robot.intake);
  }

  @Override
  public void initialize() {
    Robot.intake.setRPM(m_RPM);
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
