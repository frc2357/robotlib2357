package frc.robot.commands.rotationArm;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;

public class RotationArmSetZero extends Command {
    public RotationArmSetZero() {
        addRequirements(Robot.rotationArm);
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public void end(boolean interrupted) {
        Robot.rotationArm.setZero();
    }
}
