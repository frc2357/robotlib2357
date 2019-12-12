package com.systemmeltdown.robotlib.commands;

import edu.wpi.first.wpilibj.command.Command;
import com.systemmeltdown.robotlib.References;

public class DriveProportionalCommand extends Command {

    public DriveProportionalCommand() {
        requires(References.driveSub);
    }

    @Override
    protected void execute() {
        References.driveSub.driveProportional(References.driverController.getSpeed(), References.driverController.getTurn());
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}