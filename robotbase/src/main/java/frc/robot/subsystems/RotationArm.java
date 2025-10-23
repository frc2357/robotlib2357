package frc.robot.subsystems;

import static edu.wpi.first.units.Units.RadiansPerSecond;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.ClosedLoopSlot;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.ClosedLoopConfig.FeedbackSensor;
import com.revrobotics.spark.config.MAXMotionConfig;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkMaxConfig;
import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.units.Units;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.Voltage;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.ROTATION_ARM;
import frc.robot.util.Utility;

public class RotationArm extends SubsystemBase {

  private Angle m_targetAngle;

  private SparkMax m_motor;
  private SparkClosedLoopController m_PIDController;
  private RelativeEncoder m_alternateEncoder;
  private ArmFeedforward m_armFeedForward;

  public RotationArm() {
    m_motor = new SparkMax(
      Constants.CAN_ID.ROTATION_ARM_MOTOR_ID,
      MotorType.kBrushless
    );
    m_PIDController = m_motor.getClosedLoopController();
    m_alternateEncoder = m_motor.getAlternateEncoder();

    m_armFeedForward = new ArmFeedforward(
      ROTATION_ARM.ARM_FEED_FORWARD_KS,
      ROTATION_ARM.ARM_FEED_FORWARD_KG,
      ROTATION_ARM.ARM_FEED_FORWARD_KV,
      ROTATION_ARM.ARM_FEED_FORWARD_KA
    );

    configure();
  }

  private void configure() {
    MAXMotionConfig maxMotionConfig = new MAXMotionConfig()
      .maxVelocity(ROTATION_ARM.SMART_MOTION_MAX_VEL_RPM)
      .maxAcceleration(ROTATION_ARM.SMART_MOTION_MAX_ACC_RPM)
      .allowedClosedLoopError(ROTATION_ARM.SMART_MOTION_ALLOWED_ERROR);

    SparkBaseConfig motorConfig = new SparkMaxConfig()
      .inverted(ROTATION_ARM.MOTOR_INVERTED)
      .voltageCompensation(12)
      .idleMode(ROTATION_ARM.MOTOR_IDLE_MODE)
      .smartCurrentLimit(
        ROTATION_ARM.MOTOR_STALL_LIMIT_AMPS,
        ROTATION_ARM.MOTOR_FREE_LIMIT_AMPS
      );

    motorConfig.encoder.inverted(ROTATION_ARM.ENCODER_INVERTED);

    motorConfig.closedLoop
      .pidf(
        ROTATION_ARM.MOTOR_PID_P,
        ROTATION_ARM.MOTOR_PID_I,
        ROTATION_ARM.MOTOR_PID_D,
        ROTATION_ARM.MOTOR_PID_FF
      )
      .outputRange(-1, 1)
      .feedbackSensor(FeedbackSensor.kAlternateOrExternalEncoder)
      .apply(maxMotionConfig);

    m_motor.configure(
      motorConfig,
      ResetMode.kNoResetSafeParameters,
      PersistMode.kNoPersistParameters
    );
  }

  public void setAxisSpeed(double speed) {
    m_targetAngle = null;
    speed *= ROTATION_ARM.AXIS_MAX_SPEED;
    m_motor.set(speed);
  }

  public void stop() {
    m_targetAngle = null;
    m_motor.stopMotor();
  }

  public void setZero() {
    m_alternateEncoder.setPosition(0);
  }

  public Angle getAngle() {
    return Units.Radian.of(m_alternateEncoder.getPosition());
  }

  public boolean isAtTargetAngle() {
    return Utility.isWithinTolerance(
      getAngle().in(Units.Radians),
      m_targetAngle.in(Units.Radians),
      ROTATION_ARM.SMART_MOTION_ALLOWED_ERROR
    );
  }

  public void setTargetAngle(Angle angle) {
    m_targetAngle = angle;
    double armFeedForwardVolts = m_armFeedForward.calculate(
      getAngle().in(Units.Radians),
      Units.RPM.of(m_alternateEncoder.getVelocity()).in(RadiansPerSecond)
    );
    m_PIDController.setReference(
      angle.in(Units.Radians),
      ControlType.kMAXMotionPositionControl,
      ClosedLoopSlot.kSlot0,
      armFeedForwardVolts
    );
  }
}
