package frc.robot.subsystems;

import com.revrobotics.RelativeEncoder;
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

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.EXTENSION_ARM;
import frc.robot.util.Utility;

public class ExtensionArm extends SubsystemBase {
    private double m_targetRotations;

    private SparkMax m_motor;
    private SparkClosedLoopController m_PIDController;
    private RelativeEncoder m_alternateEncoder;

    public ExtensionArm() {
        m_motor = new SparkMax(Constants.CAN_ID.EXTENSION_ARM_MOTOR_ID, MotorType.kBrushless);
        m_PIDController = m_motor.getClosedLoopController();
        m_alternateEncoder = m_motor.getAlternateEncoder();

        configure();
    }

    private void configure() {
        MAXMotionConfig maxMotionConfig = new MAXMotionConfig()
                .maxVelocity(EXTENSION_ARM.SMART_MOTION_MAX_VEL_RPM)
                .maxAcceleration(EXTENSION_ARM.SMART_MOTION_MAX_ACC_RPM)
                .allowedClosedLoopError(EXTENSION_ARM.SMART_MOTION_ALLOWED_ERROR);

        SparkBaseConfig motorConfig = new SparkMaxConfig()
                .inverted(EXTENSION_ARM.MOTOR_INVERTED)
                .voltageCompensation(12)
                .idleMode(EXTENSION_ARM.MOTOR_IDLE_MODE)
                .smartCurrentLimit(EXTENSION_ARM.MOTOR_STALL_LIMIT_AMPS, EXTENSION_ARM.MOTOR_FREE_LIMIT_AMPS);

        motorConfig.encoder
                .inverted(EXTENSION_ARM.ENCODER_INVERTED);

        motorConfig.closedLoop
                .pidf(EXTENSION_ARM.MOTOR_PID_P, EXTENSION_ARM.MOTOR_PID_I, EXTENSION_ARM.MOTOR_PID_D,
                        EXTENSION_ARM.MOTOR_PID_FF)
                .outputRange(-1, 1)
                .feedbackSensor(FeedbackSensor.kAlternateOrExternalEncoder)
                .apply(maxMotionConfig);

        m_motor.configure(motorConfig,
                ResetMode.kNoResetSafeParameters,
                PersistMode.kNoPersistParameters);
    }

    public void setAxisSpeed(double speed) {
        m_targetRotations = Double.NaN;
        speed *= EXTENSION_ARM.AXIS_MAX_SPEED;
        m_motor.set(speed);
    }

    public void stop() {
        m_targetRotations = Double.NaN;
        m_motor.stopMotor();
    }

    public void setTargetRotations(double targetRotations) {
        m_targetRotations = targetRotations;
        m_PIDController.setReference(m_targetRotations, ControlType.kMAXMotionPositionControl);
    }

    public double getRotations() {
        return m_alternateEncoder.getPosition();
    }

    public boolean isAtTargetRotations() {
        return Utility.isWithinTolerance(getRotations(), m_targetRotations, EXTENSION_ARM.SMART_MOTION_ALLOWED_ERROR);
    }

    public void setZero() {
        m_alternateEncoder.setPosition(0);
    }
}
