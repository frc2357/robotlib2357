package frc.robot.commands.rotationArm;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;
import frc.robot.controls.util.AxisInterface;

public class RotationArmAxis extends Command {
    private AxisInterface m_axis;

    public RotationArmAxis(AxisInterface axis) {
        m_axis = axis;
        addRequirements(Robot.rotationArm);
    }

    @Override
    public void execute() {
        double axisValue = m_axis.getValue();
        Robot.rotationArm.setAxisSpeed(axisValue);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        Robot.rotationArm.stop();
    }
}
