package com.systemmeltdown.robotlib.subsystems.drive;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.SpeedControllerGroup;

/**
 * A drive subsystem for a drive subsystem that uses falcons. Extends off of SkidSteerDriveSubsystem.
 */
public class SingleSpeedFalconDriveSubsystem extends SkidSteerDriveSubsystem {
    // Left out of the abstract to use Falcon Specific
    protected WPI_TalonFX m_leftFalconMaster;
    protected WPI_TalonFX m_rightFalconMaster;

    /**
     * @param leftFalconMaster
     * @param leftFalconSlaves
     * @param rightFalconMaster
     * @param rightFalconSlaves
     */
    public SingleSpeedFalconDriveSubsystem(WPI_TalonFX leftFalconMaster, WPI_TalonFX[] leftFalconSlaves,
            WPI_TalonFX rightFalconMaster, WPI_TalonFX[] rightFalconSlaves) {
        super(new SpeedControllerGroup(leftFalconMaster, leftFalconSlaves),
                new SpeedControllerGroup(rightFalconMaster, rightFalconSlaves));
        m_leftFalconMaster = leftFalconMaster;
        m_rightFalconMaster = rightFalconMaster;
    }

    @Override
    protected double getCurrentSpeedLeftClicksPerSecond() {
        return m_leftFalconMaster.getSelectedSensorVelocity();
    }

    @Override
    protected double getCurrentSpeedRightClicksPerSecond() {
        return m_rightFalconMaster.getSelectedSensorVelocity();
    }

    @Override
    protected void setVelocity(int leftClicksPerSecond, double rightClicksPerSecond) {
        // TODO implement PID Loop
    }
}