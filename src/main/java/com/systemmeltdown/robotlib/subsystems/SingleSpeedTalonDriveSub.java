package com.systemmeltdown.robotlib.subsystems;

import java.util.ArrayList;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatusFrame;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.systemmeltdown.robotlib.util.PIDValues;
import com.systemmeltdown.robotlib.util.Utility;

public class SingleSpeedTalonDriveSub extends SkidSteerDriveSubBase {
    WPI_TalonSRX m_rightMaster;
    WPI_TalonSRX m_leftMaster;
    WPI_TalonSRX[] m_rightSlaves;
    WPI_TalonSRX[] m_leftSlaves;

    public SingleSpeedTalonDriveSub(WPI_TalonSRX rightMaster, WPI_TalonSRX leftMaster, WPI_TalonSRX[] rightSlaves,
            WPI_TalonSRX[] leftSlaves) {

        m_rightMaster = rightMaster;
        m_leftMaster = leftMaster;
        m_rightSlaves = rightSlaves;
        m_leftSlaves = leftSlaves;

    }

    public SingleSpeedTalonDriveSub(int rightMasterID, int leftMasterID, int[] rightSlaveIDS, int[] leftSlaveIDS) {

        m_rightMaster = new WPI_TalonSRX(rightMasterID);
        m_leftMaster = new WPI_TalonSRX(leftMasterID);
        ArrayList<WPI_TalonSRX> rightSlaves = new ArrayList<>();
        ArrayList<WPI_TalonSRX> leftSlaves = new ArrayList<>();

        for (int id : rightSlaveIDS) {
            rightSlaves.add(new WPI_TalonSRX(id));
        }

        m_rightSlaves = rightSlaves.toArray(m_rightSlaves);

        for (int id : leftSlaveIDS) {
            leftSlaves.add(new WPI_TalonSRX(id));
        }

        m_leftSlaves = leftSlaves.toArray(m_leftSlaves);

    }

    public void configure(int timeout, int talonPidPrimary, double driveMotorDeadband, double driveRampSeconds,
            int talonSlotVelocity, int talonSlotDistance, int talonSlotTurning, PIDValues pidDrivePos,
            PIDValues pidDriveSpeed) {

        m_leftMaster.set(ControlMode.PercentOutput, 0.0);
        m_rightMaster.set(ControlMode.PercentOutput, 0.0);

        m_leftMaster.configFactoryDefault();

        for (WPI_TalonSRX leftSlave : m_leftSlaves)
            leftSlave.configFactoryDefault();
        m_rightMaster.configFactoryDefault();
        for (WPI_TalonSRX rightSlave : m_rightSlaves)
            rightSlave.configFactoryDefault();

        m_leftMaster.setNeutralMode(NeutralMode.Brake);
        m_rightMaster.setNeutralMode(NeutralMode.Brake);

        m_leftMaster.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, talonPidPrimary, timeout);
        m_rightMaster.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, talonPidPrimary, timeout);

        m_leftMaster.setInverted(false);
        m_leftMaster.setSensorPhase(false);

        for (WPI_TalonSRX leftSlave : m_leftSlaves) {
            leftSlave.setInverted(false);
            leftSlave.follow(m_leftMaster);
        }

        m_rightMaster.setInverted(true);
        m_rightMaster.setSensorPhase(false);

        for (WPI_TalonSRX talon : m_rightSlaves) {
            talon.setInverted(true);
            talon.follow(m_rightMaster);
        }

        m_leftMaster.setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 20);
        m_rightMaster.setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 20);

        m_leftMaster.configNeutralDeadband(driveMotorDeadband);
        m_rightMaster.configNeutralDeadband(driveMotorDeadband);

        m_leftMaster.configOpenloopRamp(driveRampSeconds);
        m_rightMaster.configOpenloopRamp(driveRampSeconds);

        m_leftMaster.configNominalOutputForward(0, timeout);
        m_leftMaster.configNominalOutputReverse(0, timeout);
        m_leftMaster.configPeakOutputReverse(-1.0, timeout);
        m_leftMaster.configPeakOutputForward(+1.0, timeout);
        m_rightMaster.configNominalOutputForward(0, timeout);
        m_rightMaster.configNominalOutputReverse(0, timeout);
        m_rightMaster.configPeakOutputForward(+1.0, timeout);
        m_rightMaster.configPeakOutputReverse(-1.0, timeout);

        Utility.configTalonPID(m_leftMaster, talonSlotVelocity, pidDriveSpeed);
        Utility.configTalonPID(m_rightMaster, talonSlotVelocity, pidDriveSpeed);

        Utility.configTalonPID(m_rightMaster, talonSlotDistance, pidDrivePos);
        Utility.configTalonPID(m_leftMaster, talonSlotDistance, pidDrivePos);

        /**
         * 1ms per loop. PID loop can be slowed down if need be. For example, - if
         * sensor updates are too slow - sensor deltas are very small per update, so
         * derivative error never gets large enough to be useful. - sensor movement is
         * very slow causing the derivative error to be near zero.
         */
        int closedLoopTimeMs = 1;

        m_rightMaster.configClosedLoopPeriod(talonSlotDistance, closedLoopTimeMs, timeout);
        m_rightMaster.configClosedLoopPeriod(talonSlotTurning, closedLoopTimeMs, timeout);
        m_rightMaster.configClosedLoopPeriod(talonSlotVelocity, closedLoopTimeMs, timeout);

        resetEncoders(timeout);
    }

    public void resetEncoders(int timeout) {
        m_rightMaster.getSensorCollection().setQuadraturePosition(0, timeout);
        m_leftMaster.getSensorCollection().setQuadraturePosition(0, timeout);
    }

    @Override
    public void setLeftSpeed(double speed) {
        m_leftMaster.set(ControlMode.PercentOutput, speed);
    }

    @Override
    public void setRightSpeed(double speed) {
        m_rightMaster.set(ControlMode.PercentOutput, speed);
    }

    
}