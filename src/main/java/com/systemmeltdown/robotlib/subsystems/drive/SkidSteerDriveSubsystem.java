package com.systemmeltdown.robotlib.subsystems.drive;

import com.systemmeltdown.robotlib.util.ClosedLoopSystem;
import com.systemmeltdown.robotlib.util.RobotMath;
import edu.wpi.first.wpilibj2.command.Subsystem;


/**
 * Base class for any kind of "Skid Steer" drive base. This makes assumptions
 * that we will use encoders and velocity drive. However, this makes zero
 * assumptions about hardware or implementation of such.
 */
public abstract class SkidSteerDriveSubsystem implements Subsystem, ClosedLoopSystem {

	private double m_wheelbaseWidthInches = 0;
	private int m_clicksPerInch = 0;
	private int m_maxSpeedClicksPerSecond = 0;
	private boolean m_ClosedLoopEnabled = true;

	public static class Configuration {
		/**
		 * The distance between the drive wheels. Measure from the center of the left
		 * wheels to the center of the right. Value: double (positive)
		 */
		public double m_wheelbaseWidthInches = 6;
		
		/**
		 * The number of encoder clicks per inch of drive base travel. Calculated with
		 * gear ratios and wheel diameter. Verify with measurement of working robot
		 * travel for best accuracy. Value: int (positive)
		 */
		public int m_clicksPerInch = 5;

		/**
		 * The number of encoder clicks per minute when running at max speed. Measure
		 * top running speed with no load (up on blocks) Value: int (positive)
		 */
		public int m_maxSpeedClicksPerSecond = 1024;
	}

	public void configure(Configuration config) {
		m_wheelbaseWidthInches = config.m_wheelbaseWidthInches;

		m_clicksPerInch = config.m_clicksPerInch;
		
		m_maxSpeedClicksPerSecond = config.m_maxSpeedClicksPerSecond;
	}

	public final double getMaxSpeedInchesPerSecond() {
		return m_maxSpeedClicksPerSecond / m_clicksPerInch;
	}

	public final double getCurrentSpeedInchesPerSecond() {
		int leftSpeedInches = getCurrentSpeedLeftClicksPerSecond() / m_clicksPerInch;
		int rightSpeedInches = getCurrentSpeedRightClicksPerSecond() / m_clicksPerInch;
		return (rightSpeedInches + leftSpeedInches) / 2;
	}

	public final double getCurrentTurnDegreesPerSecond() {
		int leftSpeedInches = getCurrentSpeedLeftClicksPerSecond() / m_clicksPerInch;
		int rightSpeedInches = getCurrentSpeedRightClicksPerSecond() / m_clicksPerInch;
		int rotationInches = (leftSpeedInches - rightSpeedInches) / 2;
		return RobotMath.turnInchesToDegrees(rotationInches, m_wheelbaseWidthInches);
	}

	public final void stop() {
		setProportional(0, 0);
	}

	public final void driveProportional(double speedProportion, double turnProportion) {
		double leftProportion = speedProportion - turnProportion;
		double rightProportion = speedProportion + turnProportion;
		setProportional(leftProportion, rightProportion);
	}

	public final void driveVelocity(double speedInchesPerSecond, double turnDegreesPerSecond) {
		if (this.isClosedLoopEnabled()) {
			System.err.println("Drive: Cannot driveVelocity while failsafe is active!");
			return;
		}

		double turnInchesPerSecond = RobotMath.turnDegreesToInches(turnDegreesPerSecond, m_wheelbaseWidthInches);
		int turnClicksPerSecond = (int) Math.round(turnInchesPerSecond * m_clicksPerInch);
		int speedClicksPerSecond = (int) Math.round(speedInchesPerSecond * m_clicksPerInch);

		int leftClicksPerSecond = speedClicksPerSecond + turnClicksPerSecond;
		int rightClicksPerSecond = speedClicksPerSecond - turnClicksPerSecond;

		setVelocity(leftClicksPerSecond, rightClicksPerSecond);
	}

	/**
	 * Gets the current speed of the left drive system
	 * 
	 * @return Speed in clicks per second (negative = backwards)
	 */
	protected abstract int getCurrentSpeedLeftClicksPerSecond();

	/**
	 * Gets the current speed of the right drive system
	 * 
	 * @return Speed in clicks per second (negative = backwards)
	 */
	protected abstract int getCurrentSpeedRightClicksPerSecond();

	/**
	 * Set the proportional speed of the drive base.
	 * 
	 * @param leftProportion  Speed of left drive (-1.0 to 1.0, negative =
	 *                        backwards)
	 * @param rightProportion Speed of right drive (-1.0 to 1.0, negative =
	 *                        backwards)
	 */
	protected abstract void setProportional(double leftProportion, double rightProportion);

	/**
	 * Set the valocity speed of the drive base.
	 * 
	 * @param leftClicksPerSecond  Speed of left drive in clicks per second
	 *                             (negative = backwards)
	 * @param rightClicksPerSecond Speed of right drive in clicks per second
	 *                             (negative = backwards)
	 */
	protected abstract void setVelocity(int leftClicksPerSecond, double rightClicksPerSecond);



	@Override
	public boolean isClosedLoopEnabled() {
		return this.m_ClosedLoopEnabled;
	}

	@Override
	public void setClosedLoopEnabled(boolean ClosedLoopEnabled) {
		this.m_ClosedLoopEnabled = ClosedLoopEnabled;
	}
}
