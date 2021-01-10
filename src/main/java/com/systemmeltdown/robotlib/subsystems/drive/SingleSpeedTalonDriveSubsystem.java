package com.systemmeltdown.robotlib.subsystems.drive;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.SpeedControllerGroup;

public class SingleSpeedTalonDriveSubsystem extends SkidSteerDriveSubsystem {
    // Left out of the abstract to use Talon Specific methods
    protected WPI_TalonSRX m_leftTalonMaster;
    protected WPI_TalonSRX m_rightTalonMaster;

    public SingleSpeedTalonDriveSubsystem(WPI_TalonSRX leftTalonMaster, WPI_TalonSRX[] leftTalonSlaves,
            WPI_TalonSRX rightTalonMaster, WPI_TalonSRX[] rightTalonSlaves) {
        super(new SpeedControllerGroup(leftTalonMaster, leftTalonSlaves),
                new SpeedControllerGroup(rightTalonMaster, rightTalonSlaves));
        m_leftTalonMaster = leftTalonMaster;
        m_rightTalonMaster = rightTalonMaster;
    }

    @Override
    protected double getCurrentSpeedLeftClicksPerSecond() {
        double rawSensorUnitsPer100ms = m_leftTalonMaster.getSelectedSensorVelocity(); // returns selected sensor (in raw sensor units) per 100ms
        double rawSensorUnitsPerSec = rawSensorUnitsPer100ms / 10;
        // TODO: further changes?
        return rawSensorUnitsPerSec;
    }

    @Override
    protected double getCurrentSpeedRightClicksPerSecond() {
        double rawSensorUnitsPer100ms = m_rightTalonMaster.getSelectedSensorVelocity(); // returns selected sensor (in raw sensor units) per 100ms
        double rawSensorUnitsPerSec = rawSensorUnitsPer100ms / 10;
        // TODO: further changes?
        return rawSensorUnitsPerSec; 
    }

    @Override
    protected void setVelocity(int leftClicksPerSecond, double rightClicksPerSecond) {
        // TODO implement PID Loop
    }
}