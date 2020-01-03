package com.systemmeltdown.robotlib.subsystems.drive;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class SingleSpeedTalonDriveSubsystem extends SkidSteerDriveSubsystem {
    private WPI_TalonSRX m_rightMaster;
    private WPI_TalonSRX m_leftMaster;
    private WPI_TalonSRX[] m_rightSlaves;
    private WPI_TalonSRX[] m_leftSlaves;

    public SingleSpeedTalonDriveSubsystem(
        TalonIDGroup rightTalonIDs,
        TalonIDGroup leftTalonIDs) {

        // init right master
        m_rightMaster = new WPI_TalonSRX(rightTalonIDs.getMasterTalonID());
        m_rightMaster.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
        m_rightMaster.setInverted(rightTalonIDs.getInvert());

        // init left master
        m_leftMaster = new WPI_TalonSRX(leftTalonIDs.getMasterTalonID());
        m_leftMaster.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
        m_leftMaster.setInverted(leftTalonIDs.getInvert());

        // init right slaves
        m_rightSlaves = new WPI_TalonSRX[rightTalonIDs.getSlaveTalonIDs().length];
        for (int i = 0; i < m_rightSlaves.length; i++) {
            m_rightSlaves[i] = new WPI_TalonSRX(rightTalonIDs.getSlaveTalonIDs()[i]);
            m_rightSlaves[i].setInverted(rightTalonIDs.getInvert());
            m_rightSlaves[i].follow(m_rightMaster);
        }

        // init left slaves
        m_leftSlaves = new WPI_TalonSRX[leftTalonIDs.getSlaveTalonIDs().length];
        for (int i = 0; i < m_leftSlaves.length; i++) {
            m_leftSlaves[i] = new WPI_TalonSRX(leftTalonIDs.getSlaveTalonIDs()[i]);
            m_leftSlaves[i].setInverted(leftTalonIDs.getInvert());
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
        // when initailizing, call setDefaultCommand
    }

}