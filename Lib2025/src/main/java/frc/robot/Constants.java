// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide
 * numerical or boolean
 * constants. This class should not be used for any other purpose. All constants
 * should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>
 * It is advised to statically import this class (or one of its inner classes)
 * wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
  public static final class CAN_ID {
    public static final int TOP_SHOOTER_MOTOR_ID = 25;
    public static final int BOTTOM_SHOOTER_MOTOR_ID = 26;

    public static final int TOP_INTAKE_MOTOR_ID = 23;
    public static final int BOTTOM_INTAKE_MOTOR_ID = 24;
  }

  public static final class SHOOTER {
    public static final double SHOOTER_AXIS_MAX_SPEED = 0.8;

    public static final IdleMode IDLE_MODE = IdleMode.kCoast;

    public static final boolean TOP_MOTOR_INVERTED = false;
    public static final boolean BOTTOM_MOTOR_INVERTED = true;

    public static final int TOP_MOTOR_STALL_LIMIT_AMPS = 50;
    public static final int TOP_MOTOR_FREE_LIMIT_AMPS = 50;

    public static final int BOTTOM_MOTOR_STALL_LIMIT_AMPS = 50;
    public static final int BOTTOM_MOTOR_FREE_LIMIT_AMPS = 50;

    public static final double RAMP_RATE = .25;

    public static final double TOP_MOTOR_P = 0.0;
    public static final double TOP_MOTOR_I = 0.0;
    public static final double TOP_MOTOR_D = 0.0;
    public static final double TOP_MOTOR_FF = 0.00018;

    public static final double BOTTOM_MOTOR_P = 0.0;
    public static final double BOTTOM_MOTOR_I = 0.0;
    public static final double BOTTOM_MOTOR_D = 0.0;
    public static final double BOTTOM_MOTOR_FF = 0.00018;

    public static final double RPM_TOLERANCE = 100;
  }

  public static final class INTAKE {
    public static final double INTAKE_AXIS_MAX_SPEED = 0.8;

    public static final double DEBOUNCE_TIME_SECONDS = 0.02;

    public static final double FEED_TO_SHOOTER_TIMEOUT = 0;
    public static final double FLOOR_INTAKE_REVERSE_TIMEOUT = 0.1;

    public static final double PICKUP_SPEED_PERCENT_OUTPUT = .75;
    public static final double SLOW_PICKUP_SPEED_PERCENT_OUTPUT = .1;
    public static final double REVERSE_FEED_SPEED_PERCENT_OUTPUT = -0.2;
    public static final double FEED_SPEED_PERCENT_OUTPUT = 0.75;

    public static final IdleMode IDLE_MODE = IdleMode.kBrake;

    public static final boolean TOP_MOTOR_INVERTED = false;
    public static final boolean BOTTOM_MOTOR_INVERTED = false;

    public static final int TOP_MOTOR_STALL_LIMIT_AMPS = 60;
    public static final int TOP_MOTOR_FREE_LIMIT_AMPS = 60;

    public static final int BOTTOM_MOTOR_STALL_LIMIT_AMPS = 60;
    public static final int BOTTOM_MOTOR_FREE_LIMIT_AMPS = 60;

    public static final double RAMP_RATE = .25;

    public static final double TOP_MOTOR_P = 0.0;
    public static final double TOP_MOTOR_I = 0.0;
    public static final double TOP_MOTOR_D = 0.0;
    public static final double TOP_MOTOR_FF = 0.00018;

    public static final double BOTTOM_MOTOR_P = 0.0;
    public static final double BOTTOM_MOTOR_I = 0.0;
    public static final double BOTTOM_MOTOR_D = 0.0;
    public static final double BOTTOM_MOTOR_FF = 0.00018;

    public static final double RPM_TOLERANCE = 100;
  }

  public static final class DIGITAL_INPUT {
    public static final int INTAKE_BEAM_BREAK_ID = 5;
    public static final int END_AFFECTOR_PROXIMITY_SENSOR_ID = 6;
    public static final int END_AFFECTOR_PROXIMITY_SENSOR_POWER_ID = 8;
  }
}
