package frc.robot.commands.intake;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;
import frc.robot.controls.util.AxisInterface;

public class IntakeAxis extends Command {
  private AxisInterface m_axis;

  public IntakeAxis(AxisInterface axis) {
    m_axis = axis;
    addRequirements(Robot.intake);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    double axisValue = m_axis.getValue();
    Robot.intake.setAxisSpeed(axisValue);
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
