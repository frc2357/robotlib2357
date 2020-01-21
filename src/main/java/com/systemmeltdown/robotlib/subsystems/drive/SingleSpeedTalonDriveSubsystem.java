package com.systemmeltdown.robotlib.subsystems.drive;

import com.ctre.phoenix.motorcontrol.ControlMode;

public class SingleSpeedTalonDriveSubsystem extends SkidSteerDriveSubsystem {

    public static class Configuration extends SkidSteerDriveSubsystem.Configuration {
        /**
         * Whether or not the left talon group needs to be inverted Value: boolean
         */
        public boolean m_isLeftInverted = false;
        
        /**
         * Whether or not the right talon group needs to be inverted Value: boolean
         */
        public boolean m_isRightInverted = false;
    }

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

    public void configure(Configuration config) {
        super.configure(config);
        m_leftTalonGroup.configure(config.m_isLeftInverted);
        m_rightTalonGroup.configure(config.m_isRightInverted);
    }

    @Override
    protected void setVelocity(int leftClicksPerSecond, double rightClicksPerSecond) {
        // TODO implement PID Loop
    }
}