package com.systemmeltdown.robotlib.subsystems.drive;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.FollowerType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatusFrame;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.sensors.PigeonIMU;
import com.ctre.phoenix.sensors.PigeonIMU_StatusFrame;
import com.systemmeltdown.robotlib.util.PIDValues;
import com.systemmeltdown.robotlib.util.Utility;

import edu.wpi.first.wpilibj.controller.PIDController;

public class PIDFalconDriveSubsystem extends FalconTrajectoryDriveSubsystem {
    public static class Configuration extends FalconTrajectoryDriveSubsystem.Configuration {
        public int timeout = 30;
        public int talonPidPrimary = 0;
        public int talonSlotDistance = 0;
        public int talonSlotTurning = 1;
        public int talonSlotVelocity = 2;
        public double driveMotorDeadband = 0.04;
        public double driveRanpSeconds = 1.0;

        // Output units: motor percent (-1.0 to +1.0) P I D feed forward izone peak
        public PIDValues pidDriveSpeed = new PIDValues(0.50, 0.0, 40.0, 0.0, 0, 1.00);
        public PIDValues pidDrivePos = new PIDValues(0.10, 0.0002, 10.0, 0.0, 0, 0.23);

        // Output units: drive encoder clicks (-1700 to 1700) P I D feed forward izone
        // peak
        public PIDValues pidDriveYaw = new PIDValues(14.0, 0.620, 400.0, 0.0, 0, 950);
        public PIDValues pidVisionYaw = new PIDValues(3.0, 2.000, 80.0, 0.0, 0, 950);
        public PIDValues pidAdjustYaw = new PIDValues(45.0, 0.0, 0.0, 0.0, 0, 950);

        public int driveMaxRPMS;
        public double VEL_FEED_FWD = 1023.0 / driveMaxRPMS;
        public int ENCODER_TICKS_PER_ROTATION = 1024;

        public int VELOCITY_UNITS_PER_MIN = 600;
        public int MAX_ENCODER_VELOCITY = driveMaxRPMS * ENCODER_TICKS_PER_ROTATION / VELOCITY_UNITS_PER_MIN;
        public double WHEEL_CIRCUMFERENCE_INCHES;
    }

    private double[] yawPitchRoll;
    private PIDController yawPID;
    private int positionTarget;
    private Configuration config;

    public PIDFalconDriveSubsystem(WPI_TalonFX leftFalconMaster, WPI_TalonFX[] leftFalconSlaves,
            WPI_TalonFX rightFalconMaster, WPI_TalonFX[] rightFalconSlaves, PigeonIMU gyro,
            double encoderDistancePerPulse, int gyroAxisTotal) {
        super(leftFalconMaster, leftFalconSlaves, rightFalconMaster, rightFalconSlaves, gyro, encoderDistancePerPulse);
        // TODO Auto-generated constructor stub
        yawPitchRoll = new double[gyroAxisTotal];
    }

    public void configure(Configuration config) {
        super.configure(config);
        this.config = config;

        super.m_rightFalconMaster.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, config.talonPidPrimary, config.timeout);
        super.m_rightFalconMaster.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, config.talonPidPrimary, config.timeout);
          
        Utility.configFalconPID(super.m_leftFalconMaster, config.talonSlotVelocity, config.pidDriveSpeed);
        Utility.configFalconPID(super.m_rightFalconMaster, config.talonSlotVelocity, config.pidDriveSpeed);

        Utility.configFalconPID(super.m_rightFalconMaster, config.talonSlotDistance, config.pidDrivePos);
        Utility.configFalconPID(super.m_leftFalconMaster, config.talonSlotDistance, config.pidDrivePos);

        super.m_leftFalconMaster.set(ControlMode.PercentOutput, 0.0);
        super.m_rightFalconMaster.set(ControlMode.PercentOutput, 0.0);

        super.m_leftFalconMaster.configFactoryDefault();
        super.m_rightFalconMaster.configFactoryDefault();

        super.m_leftFalconMaster.setNeutralMode(NeutralMode.Brake);
        super.m_rightFalconMaster.setNeutralMode(NeutralMode.Brake);

        super.m_leftFalconMaster.setSensorPhase(false);

        super.m_rightFalconMaster.setSensorPhase(false);

        super.m_leftFalconMaster.setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 20);
        super.m_rightFalconMaster.setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 20);
        super.m_gyro.setStatusFramePeriod(PigeonIMU_StatusFrame.CondStatus_9_SixDeg_YPR, 5);

        super.m_leftFalconMaster.configNeutralDeadband(config.driveMotorDeadband);
        super.m_rightFalconMaster.configNeutralDeadband(config.driveMotorDeadband);

        super.m_leftFalconMaster.configOpenloopRamp(config.driveRanpSeconds);
        super.m_rightFalconMaster.configOpenloopRamp(config.driveRanpSeconds);

        super.m_leftFalconMaster.configNominalOutputForward(0, config.timeout);
        super.m_leftFalconMaster.configNominalOutputReverse(0, config.timeout);
        super.m_leftFalconMaster.configPeakOutputReverse(-1.0, config.timeout);
        super.m_leftFalconMaster.configPeakOutputForward(+1.0, config.timeout);
        super.m_rightFalconMaster.configNominalOutputForward(0, config.timeout);
        super.m_rightFalconMaster.configNominalOutputReverse(0, config.timeout);
        super.m_rightFalconMaster.configPeakOutputForward(+1.0, config.timeout);
        super.m_rightFalconMaster.configPeakOutputReverse(-1.0, config.timeout);

        /**
         * 1ms per loop. PID loop can be slowed down if need be. For example, - if
         * sensor updates are too slow - sensor deltas are very small per update, so
         * derivative error never gets large enough to be useful. - sensor movement is
         * very slow causing the derivative error to be near zero.
         */
        int closedLoopTimeMs = 1;

        super.m_rightFalconMaster.configClosedLoopPeriod(config.talonSlotDistance, closedLoopTimeMs, config.timeout);
        super.m_rightFalconMaster.configClosedLoopPeriod(config.talonSlotTurning, closedLoopTimeMs, config.timeout);
        super.m_rightFalconMaster.configClosedLoopPeriod(config.talonSlotVelocity, closedLoopTimeMs, config.timeout);

        resetEncoders();
        configureYawPID();
    }

    public void configureYawPID() {
        resetYaw();

        yawPID = new PIDController(config.pidDriveYaw.kp, config.pidDriveYaw.ki, config.pidDriveYaw.kd, 0.05);
        yawPID.setIntegratorRange(-config.pidDriveYaw.peak, config.pidDriveYaw.peak);
    }

    public void resetEncoders() {
        super.m_rightFalconMaster.getSensorCollection().setIntegratedSensorPosition(0, config.timeout);
        super.m_leftFalconMaster.getSensorCollection().setIntegratedSensorPosition(0, config.timeout);
    }

    public void resetYaw() {
        super.m_gyro.setYaw(0);
        super.m_gyro.setAccumZAngle(0);
    }

    public int getPositionError() {
        int currentPosition = super.m_rightFalconMaster.getSelectedSensorPosition();
        return this.positionTarget - currentPosition;
    }

    public double getRotationError() {
        return yawPID.getPositionError();
    }

    /**
     *
     * @param speed The desired speed in inches/sec
     * @param turn  The desired turn rate in degrees/sec
     */
    public void PIDDrive(int speed, int turn) {
        super.m_leftFalconMaster.selectProfileSlot(config.talonSlotVelocity, config.talonPidPrimary);
        super.m_rightFalconMaster.selectProfileSlot(config.talonSlotVelocity, config.talonPidPrimary);

        int leftVelocity = speed + turn;
        int rightVelocity = speed - turn;

        Utility.clamp(leftVelocity, -config.MAX_ENCODER_VELOCITY, config.MAX_ENCODER_VELOCITY);
        Utility.clamp(rightVelocity, -config.MAX_ENCODER_VELOCITY, config.MAX_ENCODER_VELOCITY);

        super.m_rightFalconMaster.set(ControlMode.Velocity, rightVelocity);
        super.m_leftFalconMaster.set(ControlMode.Velocity, leftVelocity);
    }

    public void rotateDegrees(double degrees) {
        double currentYaw = getYaw(false);
        double targetYaw = currentYaw + degrees;

        super.m_leftFalconMaster.selectProfileSlot(config.talonSlotVelocity, config.talonPidPrimary);
        super.m_rightFalconMaster.selectProfileSlot(config.talonSlotVelocity, config.talonPidPrimary);

        yawPID.reset();
        yawPID.setSetpoint(targetYaw);
    }

    public void moveForwardDistance(double inches) {
        resetEncoders();

        super.m_leftFalconMaster.selectProfileSlot(config.talonSlotDistance, config.talonPidPrimary);
        super.m_rightFalconMaster.selectProfileSlot(config.talonSlotDistance, config.talonPidPrimary);

        int currentPosition = (int) super.m_rightFalconMaster.getSensorCollection().getIntegratedSensorPosition();
        int distance = (int) (inches * config.ENCODER_TICKS_PER_ROTATION / config.WHEEL_CIRCUMFERENCE_INCHES);
        this.positionTarget = currentPosition + distance;

        super.m_rightFalconMaster.set(ControlMode.Position, positionTarget);
        super.m_leftFalconMaster.follow(super.m_rightFalconMaster, FollowerType.AuxOutput1);
    }

    /**
     *
     * @return The gyro's current yaw value in degrees
     */
    public double getYaw(boolean moduloOutput) {
        super.m_gyro.getYawPitchRoll(yawPitchRoll);
        double yaw = yawPitchRoll[0];
        if (moduloOutput) {
            if (yaw >= 180) {
                yaw -= 360;
            }
            if (yaw <= -180) {
                yaw += 360;
            }
        }
        return yaw;
    }
}