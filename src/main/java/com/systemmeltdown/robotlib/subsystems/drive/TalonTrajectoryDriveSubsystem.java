package com.systemmeltdown.robotlib.subsystems.drive;

import com.systemmeltdown.robotlib.subsystems.drive.TalonGroup;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.sensors.PigeonIMU;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;

public class TalonTrajectoryDriveSubsystem extends SkidSteerDriveSubsystem {
    // The left-side drive encoder
    private final Encoder m_leftEncoder;
      
    // The right-side drive encoder
    private final Encoder m_rightEncoder;
  
    // The gyro sensor
    private PigeonIMU m_gyro;
    private boolean m_isGyroReversed;
   
    // Odometry class for tracking robot pose
    private final DifferentialDriveOdometry m_odometry;
  
    private TalonGroup m_rightTalonGroup;
    private TalonGroup m_leftTalonGroup;

    private boolean m_isLeftInverted;
    private boolean m_isRightInverted;

    public static class Configuration extends SkidSteerDriveSubsystem.Configuration {
        /**
         * Whether or not the left talon group needs to be inverted Value: boolean
         */
        public boolean m_isLeftInverted = false;
        
        /**
         * Whether or not the right talon group needs to be inverted Value: boolean
         */
        public boolean m_isRightInverted = true;
        
        /**
         * Whether or not the gyro is reversed Value: boolean
         */
        public boolean m_isGyroReversed = true;
    }

    /**
     * 
     * @param rightTalonGroup The talons used for the right side of the drivebase.
     * @param leftTalonGroup The talons used for the left side of the drivebase.
     * @param leftEncoder The encoder used on the left side of the drivebase.
     * @param rightEncoder The encoder used on the right side of the drivebase.
     * @param gyro The Pigeon IMU to use as the gyro.
     * @param encoderDistancePerPulse The encoder distance per pulse.
     */
    public TalonTrajectoryDriveSubsystem(
        TalonGroup rightTalonGroup, TalonGroup leftTalonGroup, Encoder leftEncoder, Encoder rightEncoder,
        PigeonIMU gyro, double encoderDistancePerPulse) {
            m_rightTalonGroup = rightTalonGroup;
            m_leftTalonGroup = leftTalonGroup;
            m_rightEncoder = rightEncoder;
            m_leftEncoder = leftEncoder;

            m_leftEncoder.setDistancePerPulse(encoderDistancePerPulse);
            m_rightEncoder.setDistancePerPulse(encoderDistancePerPulse);
        
            resetEncoders();
            m_gyro = gyro;
            m_gyro.configFactoryDefault();
            m_odometry = new DifferentialDriveOdometry(Rotation2d.fromDegrees(getHeading()));
    }

    @Override
    public void periodic() {
      // Update the odometry in the periodic block
      m_odometry.update(Rotation2d.fromDegrees(getHeading()), m_leftEncoder.getDistance(),
                        m_rightEncoder.getDistance());
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

    public void configure(Configuration config) {
        super.configure(config);
        m_leftTalonGroup.configure(config.m_isLeftInverted);
        m_rightTalonGroup.configure(config.m_isRightInverted);
        m_isGyroReversed = config.m_isGyroReversed;
    }

    @Override
    protected void setVelocity(int leftClicksPerSecond, double rightClicksPerSecond) {
        // TODO implement PID Loop
    }

    /**
     * Returns the currently-estimated pose of the robot.
     *
     * @return The pose.
     */
    public Pose2d getPose() {
        return m_odometry.getPoseMeters();
    }
  
    /**
     * Resets the odometry to the specified pose.
     *
     * @param pose The pose to which to set the odometry.
     */
    public void resetOdometry(Pose2d pose) {
        resetEncoders();
        m_odometry.resetPosition(pose, Rotation2d.fromDegrees(getHeading()));
    }

    /**
     * Returns the current wheel speeds of the robot.
     *
     * @return The current wheel speeds.
     */
    public DifferentialDriveWheelSpeeds getWheelSpeeds() {
        return new DifferentialDriveWheelSpeeds(m_leftEncoder.getRate(), m_rightEncoder.getRate());
      }
    
    /**
     * Controls the left and right sides of the drive directly with voltages.
     *
     * @param leftVolts  the commanded left output
     * @param rightVolts the commanded right output
     */
    public void setTankDriveVolts(double leftVolts, double rightVolts) {
        m_leftTalonGroup.setMasterTalonVolts(leftVolts);
        //rightVolts is negative because the right motors are inverted.
        m_rightTalonGroup.setMasterTalonVolts(-rightVolts);
    }
    
    /**
     * Resets the drive encoders to currently read a position of 0.
     */
    public void resetEncoders() {
        m_leftEncoder.reset();
        m_rightEncoder.reset();
    }

    /**
     * Gets the average distance of the two encoders.
     *
     * @return the average of the two encoder readings
     */
    public double getAverageEncoderDistance() {
        return (m_leftEncoder.getDistance() + m_rightEncoder.getDistance()) / 2.0;
    }

    /**
     * Gets the left drive encoder.
     *
     * @return the left drive encoder
     */
    public Encoder getLeftEncoder() {
        return m_leftEncoder;
    }

    /**
     * Gets the right drive encoder.
     *
     * @return the right drive encoder
     */
    public Encoder getRightEncoder() {
        return m_rightEncoder;
    }

    /**
     * Zeroes the heading of the robot.
     */
    public void zeroHeading() {
        m_gyro.setYaw(0);
        m_gyro.setAccumZAngle(0);
    }

    /**
     * Returns the heading of the robot.
     *
     * @return the robot's heading in degrees, from 180 to 180
     */
    public double getHeading() {
        double[] ypr = getYawPitchAndRoll();
        return Math.IEEEremainder(ypr[0], 360) * (m_isGyroReversed ? -1.0 : 1.0);
    }

    /**
     * Returns the turn rate of the robot.
     *
     * @return The turn rate of the robot, in degrees per second
     */
    public double getTurnRate() {
        double[] ypr = getYawPitchAndRoll();
        return ypr[1] * (m_isGyroReversed ? -1.0 : 1.0);
    }

    public double[] getYawPitchAndRoll() {
        double[] ypr = new double[3];

        m_gyro.getYawPitchRoll(ypr);

        return ypr;
    }
}