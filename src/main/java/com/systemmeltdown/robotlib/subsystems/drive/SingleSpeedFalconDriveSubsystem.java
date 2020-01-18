package com.systemmeltdown.robotlib.subsystems.drive;

import java.util.Map;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.systemmeltdown.robotlib.subsystems.drive.controllerGroups.FalconGroup;

public class SingleSpeedFalconDriveSubsystem extends SkidSteerDriveSubsystem {
    /**
	 * Whether or not the left talon group needs to be inverted Value: boolean
	 */
    public static final String CONFIG_IS_LEFT_INVERTED = "is_left_inverted";
    
    /**
	 * Whether or not the right talon group needs to be inverted Value: boolean
	 */
	public static final String CONFIG_IS_RIGHT_INVERTED = "is_right_inverted";

    private FalconGroup m_rightFalconGroup;
    private FalconGroup m_leftFalconGroup;

    public SingleSpeedFalconDriveSubsystem(
        FalconGroup rightFalconGroup,
        FalconGroup leftFalconGroup) {
            m_rightFalconGroup = rightFalconGroup;
            m_leftFalconGroup = leftFalconGroup;
    }

    @Override
    protected int getCurrentSpeedLeftClicksPerSecond() {
        return m_leftFalconGroup.getSelectedSensorPosition();
    }

    @Override
    protected int getCurrentSpeedRightClicksPerSecond() {
        return m_rightFalconGroup.getSelectedSensorPosition();
    }

    @Override
    protected void setProportional(double leftProportion, double rightProportion) {
        m_leftFalconGroup.set(ControlMode.PercentOutput, leftProportion);
        m_rightFalconGroup.set(ControlMode.PercentOutput, rightProportion);
    }

    @Override
    public void configure(Map<String, Object> config) {
        super.configure(config);
        m_leftFalconGroup.configure(((Boolean) config.get(CONFIG_IS_LEFT_INVERTED)).booleanValue());
        m_rightFalconGroup.configure(((Boolean) config.get(CONFIG_IS_RIGHT_INVERTED)).booleanValue());
    }

    @Override
    protected void setVelocity(int leftClicksPerSecond, double rightClicksPerSecond) {
        // TODO implement PID Loop
    }
}