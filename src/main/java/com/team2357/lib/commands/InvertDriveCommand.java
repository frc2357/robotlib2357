package com.team2357.lib.commands;

import com.team2357.lib.controllers.InvertDriveControls;
import com.team2357.lib.subsystems.TogglableLimelightSubsystem;

/**
 * This command inverts the controls. For example: when pressed, forward becomes back,
 * and left becomes right, and vice versa. This is done so that the driver does not have to turn the robot
 * all the way around: they can just press a button instead.
 * 
 * @category Drive
 */
public class InvertDriveCommand extends CommandLoggerBase {
    private TogglableLimelightSubsystem m_visionSub;
    private InvertDriveControls m_controls;

    /**
     * @param visionSub The {@link TogglableLimelightSubsystem}. When this command is called, the stream
     * will switch to a camera on the other side of the robot.
     * 
     * @param controls The controls. The {@link InvertDriveControls} class extends off of 
     * {@link DriverControls} so these should be used instead of DriverControls.
     */
    public InvertDriveCommand(TogglableLimelightSubsystem visionSub, InvertDriveControls controls) {
        m_controls = controls;
        m_visionSub = visionSub;
    }

    @Override
    public void initialize() {
        super.initialize();
        m_controls.invert();
        m_visionSub.toggleStream();
    }
    
    @Override
    public boolean isFinished() {
        return true;
    }
}