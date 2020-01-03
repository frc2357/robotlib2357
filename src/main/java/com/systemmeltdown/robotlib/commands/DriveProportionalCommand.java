package com.systemmeltdown.robotlib.commands;

import edu.wpi.first.wpilibj.command.Command;
import com.systemmeltdown.robotlib.controllers.DriverControls;
import com.systemmeltdown.robotlib.subsystems.drive.SkidSteerDriveSubsystem;

public class DriveProportionalCommand extends Command {
    private SkidSteerDriveSubsystem m_driveSub;
    private DriverControls m_driverController;

    public DriveProportionalCommand(SkidSteerDriveSubsystem driveSub, DriverControls driverController) {
        m_driveSub = driveSub;
        m_driverController = driverController;
        requires(driveSub);
    }

    @Override
    protected void execute() {
        m_driveSub.driveProportional(m_driverController.getSpeed(), m_driverController.getTurn());
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}