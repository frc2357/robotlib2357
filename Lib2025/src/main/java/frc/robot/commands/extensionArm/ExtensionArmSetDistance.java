package frc.robot.commands.extensionArm;

import edu.wpi.first.units.measure.Distance;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;

public class ExtensionArmSetDistance extends Command {
    private Distance m_distance;

    public ExtensionArmSetDistance(Distance distance) {
        m_distance = distance;
        addRequirements(Robot.extensionArm);
    }

    @Override
    public void initialize() {
        Robot.extensionArm.setExtensionDistance(m_distance);
    }

    @Override
    public boolean isFinished() {
        return Robot.extensionArm.isAtSetpoint();
    }

    @Override
    public void end(boolean interrupted) {
        Robot.extensionArm.stop();
    }
}
