package com.systemmeltdown.robotlib.subsystems.drive;

import com.systemmeltdown.robotlib.CanIdMap;
import com.systemmeltdown.robotlib.subsystems.drive.TalonGroup;
import com.systemmeltdown.robotlib.util.Constants;

import java.util.Map;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.sensors.PigeonIMU;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;
//import edu.wpi.first.wpilibj.drive.RobotDriveBase;

public class SingleSpeedTalonDriveSubsystemWithTrajectorySubsystem extends SkidSteerDriveSubsystem {
    /**
	 * Whether or not the left talon group needs to be inverted Value: boolean
	 */
    public static final String CONFIG_IS_LEFT_INVERTED = "is_left_inverted";
    
    /**
	 * Whether or not the right talon group needs to be inverted Value: boolean
	 */
    public static final String CONFIG_IS_RIGHT_INVERTED = "is_right_inverted";
    
    // The left-side drive encoder
    private final Encoder m_leftEncoder =
        new Encoder(CanIdMap.LEFT_ENCODER_PORTS[0], CanIdMap.LEFT_ENCODER_PORTS[1], 
                    Constants.LEFT_ENCODER_REVERSED);
    // The right-side drive encoder
    private final Encoder m_rightEncoder =
        new Encoder(CanIdMap.RIGHT_ENCODER_PORTS[0], CanIdMap.RIGHT_ENCODER_PORTS[1],
                    Constants.RIGHT_ENCODER_REVERSED);
  
    // The gyro sensor
    private PigeonIMU m_gyro;
   
    // Odometry class for tracking robot pose
    private final DifferentialDriveOdometry m_odometry;
  

    private TalonGroup m_rightTalonGroup;
    private TalonGroup m_leftTalonGroup;

    public SingleSpeedTalonDriveSubsystemWithTrajectorySubsystem(
        TalonGroup rightTalonGroup,
        TalonGroup leftTalonGroup, int gyroID) {
            m_rightTalonGroup = rightTalonGroup;
            m_leftTalonGroup = leftTalonGroup;
            m_leftEncoder.setDistancePerPulse(Constants.ENCODER_DISTANCE_PER_PULSE);
            m_rightEncoder.setDistancePerPulse(Constants.ENCODER_DISTANCE_PER_PULSE);
        
            resetEncoders();
            m_gyro = new PigeonIMU(gyroID);
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
        m_rightTalonGroup.setMasterTalonVolts(leftVolts);
        m_leftTalonGroup.setMasterTalonVolts(-rightVolts);
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
     * Sets the max output of the drive.  Useful for scaling the drive to drive more slowly.
     *Currently commented out as it is currently uneeded, and creates an error.
     * @param maxOutput the maximum output to which the drive will be constrained
     */
  /*  public void setMaxOutput(double maxOutput) {
        m_drive.setMaxOutput(maxOutput);
    } */

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
        return Math.IEEEremainder(ypr[0], 360) * (Constants.GYRO_REVERSED ? -1.0 : 1.0);
    }

    /**
     * Returns the turn rate of the robot.
     *
     * @return The turn rate of the robot, in degrees per second
     */
    public double getTurnRate() {
        double[] ypr = getYawPitchAndRoll();
        return ypr[1] * (Constants.GYRO_REVERSED ? -1.0 : 1.0);
    }

    public double[] getYawPitchAndRoll() {
        double[] ypr = new double[3];

        m_gyro.getYawPitchRoll(ypr);

        return ypr;
    }
}