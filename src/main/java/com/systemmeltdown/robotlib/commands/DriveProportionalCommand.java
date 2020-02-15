package com.systemmeltdown.robotlib.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import com.systemmeltdown.robotlib.controllers.DriverControls;
import com.systemmeltdown.robotlib.subsystems.drive.SkidSteerDriveSubsystem;

/**
 * Drives porportionally (without encoders or anything like that. Based purely off of the joystick).
 * 
 * @category Commands
 */
public class DriveProportionalCommand extends CommandBase {
    private SkidSteerDriveSubsystem m_driveSub;
    private DriverControls m_driverController;

    /**
     * Creates a new {@link DriveProportionalCommand}.
     * 
     * @param driveSub The drivesub you wish to be affected by the command.
     * @param driverController Whatever is controlling the drivesub.
     */
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