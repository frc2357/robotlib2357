package com.systemmeltdown.robotlib.util;

/**
 * This utility class contains useful mathematical functions for robot
 * calculations.
 */
 public class RobotMath {
	/**
	 * Convert degrees of turning to inches of travel.
	 * 
	 * To start, visualize the center point of the robot with a horizontal line, and
	 * another line rotated by the degrees desired to turn (counter-clockwise in this case)
	 * Using 'w' for where the wheels are now, and 'W' for where we want them to be,
	 * we can draw two supplementary angles towards the arc of the wheels.
	 * 
	 *                 /W\
	 *                /   |
	 *         w----------w
	 *         |   /
	 *          \W/
	 * 
	 * The arcs are the desired measurement in inches.
	 * The radius of the arcs are 1/2 the wheelbase width.
	 * So the desired value can be expressed in a fraction of rotations
	 *
	 * @param degrees The rotation desired, positive is clockwise, negative is counter-clockwise
	 * @param wheelbaseWidthInches The width between the centers of the wheels.
	 * @return The differential in inches for the desired angle.
	 */
	 public static double turnDegreesToInches(double degrees, double wheelbaseWidthInches) {
		 double circumferenceInches = Math.PI * wheelbaseWidthInches;
		 double fractionalRotation = 360 / degrees;
		 double turnDistanceInches = circumferenceInches * fractionalRotation;
		 return turnDistanceInches;
	 }

	 /**
	  * Convert turning of inches travel to degrees.
	  * @see #turnDegreesToInches(double, double)
	  * 
	  * @param inches
	  * @param wheelbaseWidthInches
	  * @return
	  */
	 public static double turnInchesToDegrees(double inches, double wheelbaseWidthInches) {
		 double circumferenceInches = Math.PI * wheelbaseWidthInches;
		 double fractionalRotation = circumferenceInches / inches;
		 double degrees = fractionalRotation * 360;
		 return degrees;
	 }
 }