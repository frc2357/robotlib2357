package frc.robot.commands.rotationArm;

import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;

public class RotationArmSetAngle extends Command {
  private Angle m_angle;

  public RotationArmSetAngle(Angle angle) {
    m_angle = angle;
    addRequirements(Robot.rotationArm);
  }

  @Override
  public void initialize() {
    Robot.rotationArm.setTargetAngle(m_angle);
  }

  @Override
  public boolean isFinished() {
    return Robot.rotationArm.isAtTargetAngle();
  }

  @Override
  public void end(boolean interrupted) {
    Robot.rotationArm.stop();
  }
}
