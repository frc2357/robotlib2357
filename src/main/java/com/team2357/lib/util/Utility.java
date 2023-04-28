package com.team2357.lib.util;

public class Utility {

  public static double clamp(double input, double min, double max) {
    double value = input;
    value = Math.max(value, min);
    value = Math.min(value, max);
    return value;
  }

  public static int clamp(int input, int min, int max) {
    int value = input;
    value = Math.max(value, min);
    value = Math.min(value, max);
    return value;
  }

  public static int getAverage(int[] samples) {
    int sum = 0;
    for (int sample : samples) {
      sum += sample;
    }
    return sum / samples.length;
  }

  public static double deadband(double input, double deadband) {
    if (Math.abs(input) < deadband) {
      return 0.0;
    }
    return input;
  }

  public static boolean isWithinTolerance(double currentValue, double targetValue, double tolerance) {
    return Math.abs(currentValue - targetValue) <= tolerance;
  }
}
