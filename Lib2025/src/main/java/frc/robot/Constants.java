// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.units.AngleUnit;
import edu.wpi.first.units.Units;
import edu.wpi.first.units.measure.Distance;

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
  public static final class CUSTOM_UNITS {
    // These units are ONLY for the output shaft on the neo. Any pulley will require
    // the addition of a gear ratio.
    public static final Distance NEO_SHAFT_CIRCUMFERENCE = Units.Millimeters.of(8 * Math.PI);
    public static final AngleUnit NEO_ENCODER_TICK = Units.derive(Units.Revolutions).splitInto(42)
        .named("Neo Encoder Tick").symbol("NET").make();
  }

  public static final class CAN_ID {
    public static final int TOP_SHOOTER_MOTOR_ID = 25;
    public static final int BOTTOM_SHOOTER_MOTOR_ID = 26;

    public static final int TOP_INTAKE_MOTOR_ID = 23;
    public static final int BOTTOM_INTAKE_MOTOR_ID = 24;

    public static final int EXTENSION_ARM_MOTOR_ID = 31;

    public static final int ROTATION_ARM_MOTOR_ID = 32;
  }

  public static final class DIGITAL_INPUT {
    public static final int INTAKE_BEAM_BREAK_ID = 5;
  }

  public static final class SHOOTER {
    // Example simple gear ratio from 2024 shooter

    // Pulley on motor has 28 teeth, Pulley on output shaft has 14 teeth
    public static final double GEAR_RATIO = 14.0 / 28.0;
    // Coulsons (and 2 in stealth wheels) are the last part of the chain so they are
    // the only thing we care about when determining distance
    public static final Distance COULSON_CIRCUMFERENCE = Units.Inches.of(2.5 * Math.PI);
    // Coulsons will spin twice every time the motor shaft spins once. The distance
    // traveled by the coulson per motor rotation is its circumference multiplied by
    // the gear ratio
    public static final Distance DISTANCE_TRAVELED_PER_MOTOR_ROTATION = COULSON_CIRCUMFERENCE
        .div(GEAR_RATIO);

    // In the scenario that the motor shaft rotates once, a point on the coulsons
    // would travel 15.71 inches

    public static final double SHOOTER_AXIS_MAX_SPEED = 0.0;

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
    public static final double INTAKE_AXIS_MAX_SPEED = 0.0;

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

  public static final class EXTENSION_ARM {
    // See shooter example for how this logic works
    public static final double GEAR_RATIO = 1.0 / 16.0;
    // When the final output from a motor is going to a belt that drives the
    // extension of a mechanism, we want to use the pitch diameter/circumference of
    // the pulley. We find the pitch circumference with the following formula:
    // (Pulley pitch) * (Number of Teeth)
    public static final Distance HTD5_PULLEY_PITCH = Units.Millimeters.of(5);
    public static final double OUTPUT_PULLEY_NUMBER_OF_TEETH = 18;
    public static final Distance MOTOR_PULLEY_PITCH_CIRCUMFERENCE = HTD5_PULLEY_PITCH
        .times(OUTPUT_PULLEY_NUMBER_OF_TEETH);

    public static final boolean MOTOR_INVERTED = false;
    public static final boolean ENCODER_INVERTED = true;

    public static final int ENCODER_COUNTS_PER_REV = 8196;

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

    public static final double AXIS_MAX_SPEED = 0.0;
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

    public static final double AXIS_MAX_SPEED = 0.0;

    public static final double ARM_FEED_FORWARD_KS = 0;
    public static final double ARM_FEED_FORWARD_KG = 0;
    public static final double ARM_FEED_FORWARD_KV = 0;
    public static final double ARM_FEED_FORWARD_KA = 0;
  }
}
