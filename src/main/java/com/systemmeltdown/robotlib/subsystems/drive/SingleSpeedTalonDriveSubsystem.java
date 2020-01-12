package com.systemmeltdown.robotlib.subsystems.drive;

import java.util.Map;

import com.ctre.phoenix.motorcontrol.ControlMode;

public class SingleSpeedTalonDriveSubsystem extends SkidSteerDriveSubsystem {
    /**
	 * Whether or not the left talon group needs to be inverted Value: boolean
	 */
    public static final String CONFIG_IS_LEFT_INVERTED = "is_left_inverted";
    
    /**
	 * Whether or not the right talon group needs to be inverted Value: boolean
	 */
	public static final String CONFIG_IS_RIGHT_INVERTED = "is_right_inverted";

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

    @Override
    public void configure(Map<String, Object> config) {
        super.configure(config);
        m_leftTalonGroup.configure(((Boolean) config.get(CONFIG_IS_LEFT_INVERTED)).booleanValue());
        m_rightTalonGroup.configure(((Boolean) config.get(CONFIG_IS_RIGHT_INVERTED)).booleanValue());
    }

    @Override
    protected void setVelocity(int leftClicksPerSecond, double rightClicksPerSecond) {
        // TODO implement PID Loop
    }
}