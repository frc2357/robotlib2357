package com.systemmeltdown.robotlib.subsystems.drive;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.SpeedControllerGroup;

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

    private WPI_TalonSRX m_leftTalonMaster;
    private WPI_TalonSRX m_rightTalonMaster;

    public SingleSpeedTalonDriveSubsystem(
        WPI_TalonSRX leftTalonMaster, WPI_TalonSRX[] leftTalonSlaves,
        WPI_TalonSRX rightTalonMaster, WPI_TalonSRX[] rightTalonSlaves
        ) {
            super(new SpeedControllerGroup(leftTalonMaster, leftTalonSlaves),
                  new SpeedControllerGroup(rightTalonMaster, rightTalonSlaves));
            m_leftTalonMaster = leftTalonMaster;
            m_rightTalonMaster = rightTalonMaster;
    }

    @Override
    protected double getCurrentSpeedLeftClicksPerSecond() {
        return m_leftTalonMaster.get();
    }

    @Override
    protected double getCurrentSpeedRightClicksPerSecond() {
        return m_rightTalonMaster.get();
    }

    @Override
    protected void setProportional(double leftProportion, double rightProportion) {
        m_leftTalonMaster.set(leftProportion);
        m_rightTalonMaster.set(rightProportion);
    }

    public void configure(Configuration config) {
        super.configure(config);
        m_leftTalonGroup.setInverted(config.m_isLeftInverted);
        m_rightTalonGroup.setInverted(config.m_isRightInverted);
    }

    @Override
    protected void setVelocity(int leftClicksPerSecond, double rightClicksPerSecond) {
        // TODO implement PID Loop
    }
}