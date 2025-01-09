package frc.robot.commands.extensionArm;

import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;

public class ExtensionArmSetRotations extends Command {
    private Angle m_rotations;

    public ExtensionArmSetRotations(Angle rotations) {
        m_rotations = rotations;
        addRequirements(Robot.extensionArm);
    }

    @Override
    public void initialize() {
        Robot.extensionArm.setExtensionRotations(m_rotations);
    }

    @Override
    public boolean isFinished() {
        return Robot.extensionArm.isAtTargetRotations();
    }

    @Override
    public void end(boolean interrupted) {
        Robot.extensionArm.stop();
    }
}
