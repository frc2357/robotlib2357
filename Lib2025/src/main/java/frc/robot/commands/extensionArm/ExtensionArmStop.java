package frc.robot.commands.extensionArm;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;

public class ExtensionArmStop extends Command {
    public ExtensionArmStop() {
        addRequirements(Robot.extensionArm);
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public void end(boolean interrupted) {
        Robot.extensionArm.stop();
    }
}
