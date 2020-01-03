package com.systemmeltdown.robotlib.subsystems.drive;

import com.ctre.phoenix.motorcontrol.ControlMode;

public class SingleSpeedTalonDriveSubsystem extends SkidSteerDriveSubsystem {
    private TalonGroup m_rightTalonGroup;
    private TalonGroup m_leftTalonGroup;

    public SingleSpeedTalonDriveSubsystem(TalonGroup rightTalonGroup, TalonGroup leftTalonGroup) {
        m_rightTalonGroup = rightTalonGroup;
        m_leftTalonGroup = leftTalonGroup;
    }

    @Override
    protected int getCurrentSpeedLeftClicksPerSecond() {
        return m_leftTalonGroup.getSelectedSensorPosition();
    }

    @Override
    protected int getCurrentSpeedRightClicksPerSecond() {
        return m_rightTalonGroup.getSelectedSensorPosition();
    }

    @Override
    protected void setProportional(double leftProportion, double rightProportion) {
        m_leftTalonGroup.set(ControlMode.PercentOutput, leftProportion);
        m_rightTalonGroup.set(ControlMode.PercentOutput, rightProportion);
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