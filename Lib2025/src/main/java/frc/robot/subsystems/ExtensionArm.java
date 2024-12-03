package frc.robot.subsystems;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.ClosedLoopConfig;
import com.revrobotics.spark.config.EncoderConfig;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.EXTENSION_ARM;
import frc.robot.Constants.SHOOTER;

public class ExtensionArm extends SubsystemBase {
    private SparkMax m_motor;
    private SparkClosedLoopController m_PIDController;
    private RelativeEncoder m_encoder;
    private double m_targetRotations;

    public ExtensionArm() {
        m_motor = new SparkMax(Constants.CAN_ID.TRAP_AMP_ARM_MOTOR_ID, MotorType.kBrushless);
        m_PIDController = m_motor.getClosedLoopController();
        m_encoder = m_motor.getEncoder();

        configure();
    }

    private void configure() {
        ClosedLoopConfig PIDConfig = new ClosedLoopConfig()
                .pidf(EXTENSION_ARM.MOTOR_PID_P, EXTENSION_ARM.MOTOR_PID_I, EXTENSION_ARM.MOTOR_PID_D, EXTENSION_ARM.MOTOR_PID_FF)
                .outputRange(-1, 1);

        EncoderConfig enconderConfig = new EncoderConfig()
                .inverted(EXTENSION_ARM.ENCODER_INVERTED);

        SparkBaseConfig motorConfig = new SparkMaxConfig()
                .inverted(EXTENSION_ARM.MOTOR_IS_INVERTED)
                .openLoopRampRate(EXTENSION_ARM.RAMP_RATE)
                .voltageCompensation(12)
                .idleMode(EXTENSION_ARM.IDLE_MODE)
                .smartCurrentLimit(EXTENSION_ARM.TOP_MOTOR_STALL_LIMIT_AMPS, EXTENSION_ARM.TOP_MOTOR_FREE_LIMIT_AMPS)
                .apply(PIDConfig)
                .apply(enconderConfig);

        m_motor.configure(motorConfig,
                ResetMode.kNoResetSafeParameters,
                PersistMode.kNoPersistParameters);
    }
}
