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

    public static final int EXTENSION_ARM_MOTOR_ID = 31;

    public static final int ROTATION_ARM_MOTOR_ID = 32;
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
  }

  public static final class EXTENSION_ARM {
    public static final boolean MOTOR_INVERTED = false;
    public static final boolean ENCODER_INVERTED = true;

    public static final IdleMode MOTOR_IDLE_MODE = IdleMode.kBrake;

    public static final int MOTOR_STALL_LIMIT_AMPS = 40;
    public static final int MOTOR_FREE_LIMIT_AMPS = 40;

    public static final double MOTOR_PID_P = 0.003;
    public static final double MOTOR_PID_I = 0;
    public static final double MOTOR_PID_D = 0;
    public static final double MOTOR_PID_FF = 0.000005;

    public static final int SMART_MOTION_MAX_VEL_RPM = 5600;
    public static final int SMART_MOTION_MAX_ACC_RPM = 50000;
    public static final double SMART_MOTION_ALLOWED_ERROR = 0.1;

    public static final double AXIS_MAX_SPEED = 0.75;
  }

  public static final class ROTATION_ARM {
    public static final boolean MOTOR_INVERTED = false;
    public static final boolean ENCODER_INVERTED = true;

    public static final IdleMode MOTOR_IDLE_MODE = IdleMode.kBrake;

    public static final int MOTOR_STALL_LIMIT_AMPS = 40;
    public static final int MOTOR_FREE_LIMIT_AMPS = 40;

    public static final double MOTOR_PID_P = 0.003;
    public static final double MOTOR_PID_I = 0;
    public static final double MOTOR_PID_D = 0;
    public static final double MOTOR_PID_FF = 0.000005;

    public static final int SMART_MOTION_MAX_VEL_RPM = 5600;
    public static final int SMART_MOTION_MAX_ACC_RPM = 50000;
    public static final double SMART_MOTION_ALLOWED_ERROR = 0.1;

    public static final double AXIS_MAX_SPEED = 0.75;

    public static final double ARM_FEED_FORWARD_KS = 0;
    public static final double ARM_FEED_FORWARD_KG = 0;
    public static final double ARM_FEED_FORWARD_KV = 0;
    public static final double ARM_FEED_FORWARD_KA = 0;
  }
}
