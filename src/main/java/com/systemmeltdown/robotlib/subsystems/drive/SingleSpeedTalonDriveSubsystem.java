package com.systemmeltdown.robotlib.subsystems.drive;

import com.ctre.phoenix.motorcontrol.ControlMode;

public class SingleSpeedTalonDriveSubsystem extends SkidSteerDriveSubsystem {
    private TalonGroup m_rightTalonGroup;
    private TalonGroup m_leftTalonGroup;

    public SingleSpeedTalonDriveSubsystem(
        TalonGroup rightTalonGroup,
        TalonGroup leftTalonGroup) {
            m_rightTalonGroup = rightTalonGroup;
            m_leftTalonGroup = leftTalonGroup;
    }

    @Override
    protected int getCurrentSpeedLeftClicksPerSecond() {
        // could add a getSelectedSensorPosition command to the TalonGroup, so that
        // we don't have to call getMasterTalon
        return m_leftTalonGroup.getMasterTalon().getSelectedSensorPosition();
    }

    @Override
    protected int getCurrentSpeedRightClicksPerSecond() {
        // could add a getSelectedSensorPosition command to the TalonGroup, so that
        // we don't have to call getMasterTalon
        return m_rightTalonGroup.getMasterTalon().getSelectedSensorPosition();
    }

    @Override
    protected void setProportional(double leftProportion, double rightProportion) {
        // could add a set command to the TalonGroup, so that
        // we don't have to call getMasterTalon
        m_leftTalonGroup.getMasterTalon().set(ControlMode.PercentOutput, leftProportion);
        // could add a set command to the TalonGroup, so that
        // we don't have to call getMasterTalon
        m_rightTalonGroup.getMasterTalon().set(ControlMode.PercentOutput, rightProportion);
    }

    @Override

    protected void setVelocity(int leftClicksPerSecond, double rightClicksPerSecond) {
        // TODO implement PID Loop
    }

    @Override
    protected void initDefaultCommand() {
        // when initailizing, call setDefaultCommand
    }

}