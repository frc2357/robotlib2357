package frc.robot.subsystems;

import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.ClosedLoopConfig;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.math.filter.Debouncer;
import edu.wpi.first.math.filter.Debouncer.DebounceType;
import edu.wpi.first.units.Units;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.CAN_ID;
import frc.robot.Constants.DIGITAL_INPUT;
import frc.robot.Constants.INTAKE;
import frc.robot.util.Utility;

public class Intake extends SubsystemBase {
    private AngularVelocity m_targetRPM;

    private SparkMax m_topIntakeMotor;
    private SparkMax m_bottomIntakeMotor;

    private SparkClosedLoopController m_topPIDController;
    private SparkClosedLoopController m_bottomPIDController;

    private DigitalInput m_beamBreakSensor;

    private Debouncer m_debouncer;

    public Intake() {
        m_topIntakeMotor = new SparkMax(CAN_ID.TOP_INTAKE_MOTOR_ID, MotorType.kBrushless);
        m_bottomIntakeMotor = new SparkMax(CAN_ID.BOTTOM_INTAKE_MOTOR_ID, MotorType.kBrushless);

        m_topPIDController = m_topIntakeMotor.getClosedLoopController();
        m_bottomPIDController = m_bottomIntakeMotor.getClosedLoopController();

        m_beamBreakSensor = new DigitalInput(DIGITAL_INPUT.INTAKE_BEAM_BREAK_ID);

        m_debouncer = new Debouncer(INTAKE.DEBOUNCE_TIME_SECONDS, DebounceType.kBoth);

        configure();
    }

    private void configure() {
        ClosedLoopConfig topPIDConfig = new ClosedLoopConfig()
                .pidf(INTAKE.TOP_MOTOR_P, INTAKE.TOP_MOTOR_I, INTAKE.TOP_MOTOR_D, INTAKE.TOP_MOTOR_FF)
                .outputRange(-1, 1);

        ClosedLoopConfig bottomPIDConfig = new ClosedLoopConfig()
                .pidf(INTAKE.BOTTOM_MOTOR_P, INTAKE.BOTTOM_MOTOR_I, INTAKE.BOTTOM_MOTOR_D, INTAKE.BOTTOM_MOTOR_FF)
                .outputRange(-1, 1);

        SparkBaseConfig topMotorConfig = new SparkMaxConfig()
                .inverted(INTAKE.TOP_MOTOR_INVERTED).openLoopRampRate(INTAKE.RAMP_RATE).voltageCompensation(12)
                .idleMode(INTAKE.IDLE_MODE)
                .smartCurrentLimit(INTAKE.TOP_MOTOR_STALL_LIMIT_AMPS, INTAKE.TOP_MOTOR_FREE_LIMIT_AMPS)
                .apply(topPIDConfig);

        SparkBaseConfig bottomMotorConfig = new SparkMaxConfig()
                .inverted(INTAKE.BOTTOM_MOTOR_INVERTED).openLoopRampRate(INTAKE.RAMP_RATE).voltageCompensation(12)
                .idleMode(INTAKE.IDLE_MODE)
                .smartCurrentLimit(INTAKE.BOTTOM_MOTOR_STALL_LIMIT_AMPS, INTAKE.BOTTOM_MOTOR_FREE_LIMIT_AMPS)
                .apply(bottomPIDConfig);

        m_topIntakeMotor.configure(topMotorConfig, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
        m_bottomIntakeMotor.configure(bottomMotorConfig, ResetMode.kNoResetSafeParameters,
                PersistMode.kNoPersistParameters);
    }

    public void setAxisSpeed(double axisSpeed) {
        axisSpeed *= INTAKE.INTAKE_AXIS_MAX_SPEED;
        m_topIntakeMotor.set(axisSpeed);
        m_bottomIntakeMotor.set(axisSpeed);
    }

    public void stop() {
        m_targetRPM = null;
        m_topIntakeMotor.set(0.0);
        m_bottomIntakeMotor.set(0.0);
    }

    public void setRPM(AngularVelocity vel) {
        m_targetRPM = vel;
        m_topPIDController.setReference(vel.in(Units.RPM), ControlType.kVelocity);
        m_bottomPIDController.setReference(vel.in(Units.RPM), ControlType.kVelocity);
    }

    public double getTopVelocity() {
        return m_topIntakeMotor.getEncoder().getVelocity();
    }

    public double getBottomVelocity() {
        return m_bottomIntakeMotor.getEncoder().getVelocity();
    }

    public boolean isAtVelocity(AngularVelocity vel) {
        return Utility.isWithinTolerance(getTopVelocity(), vel.in(Units.RPM), INTAKE.RPM_TOLERANCE)
                && Utility.isWithinTolerance(getBottomVelocity(), vel.in(Units.RPM), INTAKE.RPM_TOLERANCE);
    }

    public boolean isAtTargetSpeed() {
        return isAtVelocity(m_targetRPM);
    }

    public boolean isBeamBroken() {
        return m_debouncer.calculate(!m_beamBreakSensor.get());
    }
}
