package com.systemmeltdown.robotlib.subsystems.drive;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.systemmeltdown.robotlib.commands.DriveProportionalCommand;

public class SingleSpeedTalonDriveSubsystem extends SkidSteerDriveSubsystem {
    private WPI_TalonSRX m_rightMaster;
    private WPI_TalonSRX m_leftMaster;
    private WPI_TalonSRX[] m_rightSlaves;
    private WPI_TalonSRX[] m_leftSlaves;

    public SingleSpeedTalonDriveSubsystem(int rightMasterId, int leftMasterId, int[] rightSlaveIds,
            int[] leftSlaveIds) {

        // init right master
        m_rightMaster = new WPI_TalonSRX(rightMasterId);
        m_rightMaster.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);

        // init left master
        m_leftMaster = new WPI_TalonSRX(leftMasterId);
        m_leftMaster.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);

        // init right slaves
        m_rightSlaves = new WPI_TalonSRX[rightSlaveIds.length];
        for (int i = 0; i < m_rightSlaves.length; i++) {
            m_rightSlaves[i] = new WPI_TalonSRX(rightSlaveIds[i]);
            m_rightSlaves[i].follow(m_rightMaster);
        }

        // init left slaves
        m_leftSlaves = new WPI_TalonSRX[leftSlaveIds.length];
        for (int i = 0; i < m_leftSlaves.length; i++) {
            m_leftSlaves[i] = new WPI_TalonSRX(leftSlaveIds[i]);
            m_leftSlaves[i].follow(m_leftMaster);
        }

        // TODO: add more config to talons?
    }

    @Override
    protected int getCurrentSpeedLeftClicksPerSecond() {
        return m_leftMaster.getSelectedSensorPosition();

    }

    @Override
    protected int getCurrentSpeedRightClicksPerSecond() {
        return m_rightMaster.getSelectedSensorPosition();

    }

    @Override
    protected void setProportional(double leftProportion, double rightProportion) {
        m_leftMaster.set(ControlMode.PercentOutput, leftProportion);
        m_rightMaster.set(ControlMode.PercentOutput, rightProportion);
    }

    @Override

    protected void setVelocity(int leftClicksPerSecond, double rightClicksPerSecond) {
        // TODO implement PID Loop
    }

    @Override
    protected void initDefaultCommand() {
        if (isFailsafeActive()) {
             setDefaultCommand(new DriveProportionalCommand());
        } else {
            // TODO: make DriveWithEncoders command
            // setDefaultCommand(new DriveWithEncoders());
        }
    }

}