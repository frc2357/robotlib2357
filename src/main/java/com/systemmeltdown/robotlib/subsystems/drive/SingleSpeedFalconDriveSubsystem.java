package com.systemmeltdown.robotlib.subsystems.drive;

import edu.wpi.first.wpilibj.SpeedControllerGroup;

public class SingleSpeedFalconDriveSubsystem extends SkidSteerDriveSubsystem {
    
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

    private SpeedControllerGroup m_rightFalconGroup;
    private SpeedControllerGroup m_leftFalconGroup;

    public SingleSpeedFalconDriveSubsystem(
        SpeedControllerGroup rightFalconGroup,
        SpeedControllerGroup leftFalconGroup) {
            m_rightFalconGroup = rightFalconGroup;
            m_leftFalconGroup = leftFalconGroup;
    }

    @Override
    protected double getCurrentSpeedLeftClicksPerSecond() {
        return m_leftFalconGroup.get();
    }

    @Override
    protected double getCurrentSpeedRightClicksPerSecond() {
        return m_rightFalconGroup.get();
    }

    @Override
    protected void setProportional(double leftProportion, double rightProportion) {
        m_leftFalconGroup.set(leftProportion);
        m_rightFalconGroup.set(rightProportion);
    }

    public void configure(Configuration config) {
        super.configure(config);
        m_leftFalconGroup.setInverted(config.m_isLeftInverted);
        m_rightFalconGroup.setInverted(config.m_isRightInverted);
    }

    @Override
    protected void setVelocity(int leftClicksPerSecond, double rightClicksPerSecond) {
        // TODO implement PID Loop
    }
}