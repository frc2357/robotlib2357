package com.systemmeltdown.robotlib.subsystems.drive;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.sensors.PigeonIMU;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;

public class TalonTrajectoryDriveSubsystem extends SingleSpeedTalonDriveSubsystem {
    private double m_leftLastValue;
    private double m_rightLastValue;
    private double m_distancePerPulse;

    // The gyro sensor
    private PigeonIMU m_gyro;
    private boolean m_isGyroReversed;

    // Odometry class for tracking robot pose
    private final DifferentialDriveOdometry m_odometry;

    public static class Configuration extends SkidSteerDriveSubsystem.Configuration {
        /**
         * Whether or not the gyro is reversed Value: boolean
         */
        public boolean m_isGyroReversed = true;
    }

    /**
     * 
     * @param leftTalonMaster
     * @param leftTalonSlaves
     * @param rightTalonMaster
     * @param rightTalonSlaves
     * @param gyro
     * @param encoderDistancePerPulse
     */
    public TalonTrajectoryDriveSubsystem(WPI_TalonSRX leftTalonMaster, WPI_TalonSRX[] leftTalonSlaves,
            WPI_TalonSRX rightTalonMaster, WPI_TalonSRX[] rightTalonSlaves, PigeonIMU gyro,
            double encoderDistancePerPulse) {
        super(leftTalonMaster, leftTalonSlaves, rightTalonMaster, rightTalonSlaves);
        m_distancePerPulse = encoderDistancePerPulse;

        resetEncoders();
        m_gyro = gyro;
        m_gyro.configFactoryDefault();
        m_odometry = new DifferentialDriveOdometry(Rotation2d.fromDegrees(getHeading()));
    }

    @Override
    public void periodic() {
        // Update the odometry in the periodic block
        m_odometry.update(Rotation2d.fromDegrees(getHeading()), getLeftDistance(), getRightDistance());
    }

    public void configure(Configuration config) {
        super.configure(config);
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
        return new DifferentialDriveWheelSpeeds(super.m_leftTalonMaster.getSelectedSensorVelocity(),
                super.m_rightTalonMaster.getSelectedSensorVelocity());
    }

    /**
     * Controls the left and right sides of the drive directly with voltages.
     *
     * @param leftVolts  the commanded left output
     * @param rightVolts the commanded right output
     */
    public void setTankDriveVolts(double leftVolts, double rightVolts) {
        // negative if motors are inverted.
        super.m_leftControllers.setVoltage(super.m_isLeftInverted ? -leftVolts : leftVolts);
        super.m_rightControllers.setVoltage(super.m_isRightInverted ? -rightVolts : rightVolts);
    }

    /**
     * Resets the drive encoders to currently read a position of 0.
     */
    public void resetEncoders() {
        getLeftDistance();
        getRightDistance();
    }

    /**
     * Gets the average distance of the two encoders.
     *
     * @return the average of the two encoder readings
     */
    public double getAverageEncoderDistance() {
        return (getLeftDistance() + getRightDistance()) / 2.0;
    }

    public double getLeftDistance() {
        double encoderPositon = super.m_leftTalonMaster.getSelectedSensorPosition();
        double difOfPostion = encoderPositon - m_leftLastValue;
        m_leftLastValue = encoderPositon;
        return difOfPostion * m_distancePerPulse;
    }

    public double getRightDistance() {
        double encoderPositon = super.m_rightTalonMaster.getSelectedSensorPosition();
        double difOfPostion = encoderPositon - m_rightLastValue;
        m_rightLastValue = encoderPositon;
        return difOfPostion * m_distancePerPulse;
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