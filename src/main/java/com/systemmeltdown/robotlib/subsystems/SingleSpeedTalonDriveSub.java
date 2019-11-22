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
    int m_timeout;
    int m_talonPidPrimary;
    double m_driveMotorDeadband;
    double m_driveRampSeconds;

    int m_talonSlotVelocity;
    int m_talonSlotDistance;
    int m_talonSlotTurning;
    PIDValues m_pidDrivePos;

    PIDValues m_pidDriveSpeed;

    public SingleSpeedTalonDriveSub(int rightMasterID, int leftMasterID, int[] rightSlaveIDS, int[] leftSlaveIDS,
            int timeout, int talonPidPrimary, double driveMotorDeadband, double driveRampSeconds, int talonSlotVelocity,
            int talonSlotDistance, int talonSlotTurning, PIDValues pidDrivePos, PIDValues pidDriveSpeed) {

        m_rightSlaves = new WPI_TalonSRX[rightSlaveIDS.length];
        for (int i = 0; i < rightSlaveIDS.length; i++) {
            m_rightSlaves[i] = new WPI_TalonSRX(rightSlaveIDS[i]);
        }

        m_leftSlaves = new WPI_TalonSRX[leftSlaveIDS.length];
        for (int i = 0; i < leftSlaveIDS.length; i++) {
            m_leftSlaves[i] = new WPI_TalonSRX(leftSlaveIDS[i]);
        }

        m_rightMaster = new WPI_TalonSRX(rightMasterID);
        m_leftMaster = new WPI_TalonSRX(leftMasterID);

        m_timeout = timeout;
        m_talonPidPrimary = talonPidPrimary;
        m_driveMotorDeadband = driveMotorDeadband;
        m_driveRampSeconds = driveRampSeconds;

        m_talonSlotVelocity = talonSlotVelocity;
        m_talonSlotDistance = talonSlotDistance;
        m_talonSlotTurning = talonSlotTurning;
        m_pidDrivePos = pidDrivePos;

        m_pidDriveSpeed = pidDriveSpeed;

    }

    public void configure() {

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

        m_leftMaster.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, m_talonPidPrimary, m_timeout);
        m_rightMaster.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, m_talonPidPrimary, m_timeout);

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

        m_leftMaster.configNeutralDeadband(m_driveMotorDeadband);
        m_rightMaster.configNeutralDeadband(m_driveMotorDeadband);

        m_leftMaster.configOpenloopRamp(m_driveRampSeconds);
        m_rightMaster.configOpenloopRamp(m_driveRampSeconds);

        m_leftMaster.configNominalOutputForward(0, m_timeout);
        m_leftMaster.configNominalOutputReverse(0, m_timeout);
        m_leftMaster.configPeakOutputReverse(-1.0, m_timeout);
        m_leftMaster.configPeakOutputForward(+1.0, m_timeout);
        m_rightMaster.configNominalOutputForward(0, m_timeout);
        m_rightMaster.configNominalOutputReverse(0, m_timeout);
        m_rightMaster.configPeakOutputForward(+1.0, m_timeout);
        m_rightMaster.configPeakOutputReverse(-1.0, m_timeout);

        Utility.configTalonPID(m_leftMaster, m_talonSlotVelocity, m_pidDriveSpeed);
        Utility.configTalonPID(m_rightMaster, m_talonSlotVelocity, m_pidDriveSpeed);

        Utility.configTalonPID(m_rightMaster, m_talonSlotDistance, m_pidDrivePos);
        Utility.configTalonPID(m_leftMaster, m_talonSlotDistance, m_pidDrivePos);

        /**
         * 1ms per loop. PID loop can be slowed down if need be. For example, - if
         * sensor updates are too slow - sensor deltas are very small per update, so
         * derivative error never gets large enough to be useful. - sensor movement is
         * very slow causing the derivative error to be near zero.
         */
        int closedLoopTimeMs = 1;

        m_rightMaster.configClosedLoopPeriod(m_talonSlotDistance, closedLoopTimeMs, m_timeout);
        m_rightMaster.configClosedLoopPeriod(m_talonSlotTurning, closedLoopTimeMs, m_timeout);
        m_rightMaster.configClosedLoopPeriod(m_talonSlotVelocity, closedLoopTimeMs, m_timeout);

        resetEncoders(m_timeout);
    }

    public void resetEncoders(int m_timeout) {
        m_rightMaster.getSensorCollection().setQuadraturePosition(0, m_timeout);
        m_leftMaster.getSensorCollection().setQuadraturePosition(0, m_timeout);
    }

    @Override
    public void setLeftSpeed(double speed) {
        m_leftMaster.set(ControlMode.PercentOutput, speed);
    }

    @Override
    public void setRightSpeed(double speed) {
        m_rightMaster.set(ControlMode.PercentOutput, speed);
    }

    // Until next PR

    @Override
    public void PIDDrive(int speed, int turn) {
        // if (this.isFailsafeActive()) {
        // return;
        // }

        // if (yawPID.isEnabled()) {
        // yawPID.disable();
        // }

        // m_leftMaster.selectProfileSlot(RobotMap.TALON_SLOT_VELOCITY,
        // RobotMap.TALON_PID_PRIMARY);
        // m_rightMaster.selectProfileSlot(RobotMap.TALON_SLOT_VELOCITY,
        // RobotMap.TALON_PID_PRIMARY);

        // int leftVelocity = speed + turn;
        // int rightVelocity = speed - turn;

        // Utility.clamp(leftVelocity, -RobotMap.MAX_ENCODER_VELOCITY,
        // RobotMap.MAX_ENCODER_VELOCITY);
        // Utility.clamp(rightVelocity, -RobotMap.MAX_ENCODER_VELOCITY,
        // RobotMap.MAX_ENCODER_VELOCITY);

        // rightMaster.set(ControlMode.Velocity, rightVelocity);
        // leftMaster.set(ControlMode.Velocity, leftVelocity);
        // }

        // public void rotateDegrees(double degrees) {
        // if (Robot.getInstance().isFailsafeActive()) {
        // return;
        // }

        // double currentYaw = getYaw(false);
        // double targetYaw = currentYaw + degrees;

        // leftMaster.selectProfileSlot(RobotMap.TALON_SLOT_VELOCITY,
        // RobotMap.TALON_PID_PRIMARY);
        // rightMaster.selectProfileSlot(RobotMap.TALON_SLOT_VELOCITY,
        // RobotMap.TALON_PID_PRIMARY);

        // yawPID.reset();
        // yawPID.setSetpoint(targetYaw);
        // yawPID.enable();
    }

}