package com.team2357.lib.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import com.team2357.lib.controllers.DriverControls;
import com.team2357.lib.subsystems.drive.SkidSteerDriveSubsystem;

public class DriveProportionalCommand extends CommandBase {
    private SkidSteerDriveSubsystem m_driveSub;
    private DriverControls m_driverController;

    public DriveProportionalCommand(SkidSteerDriveSubsystem driveSub, DriverControls driverController) {
        m_driveSub = driveSub;
        m_driverController = driverController;
        addRequirements(driveSub);
    }

    @Override
    public void execute() {
        m_driveSub.driveProportional(m_driverController.getSpeed(), m_driverController.getTurn());
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}