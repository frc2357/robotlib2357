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

import edu.wpi.first.units.Units;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.CAN_ID;
import frc.robot.Constants.SHOOTER;
import frc.robot.util.Utility;

public class Shooter extends SubsystemBase {
    private AngularVelocity m_targetVelocity;

    private SparkMax m_topShooterMotor;
    private SparkMax m_bottomShooterMotor;

    private SparkClosedLoopController m_topPIDController;
    private SparkClosedLoopController m_bottomPIDController;

    public Shooter() {
        m_topShooterMotor = new SparkMax(CAN_ID.TOP_SHOOTER_MOTOR_ID,
                MotorType.kBrushless);
        m_bottomShooterMotor = new SparkMax(CAN_ID.BOTTOM_SHOOTER_MOTOR_ID,
                MotorType.kBrushless);

        m_topPIDController = m_topShooterMotor.getClosedLoopController();
        m_bottomPIDController = m_bottomShooterMotor.getClosedLoopController();

        configure();
    }

    private void configure() {
        ClosedLoopConfig topPIDConfig = new ClosedLoopConfig()
                .pidf(SHOOTER.TOP_MOTOR_P, SHOOTER.TOP_MOTOR_I, SHOOTER.TOP_MOTOR_D, SHOOTER.TOP_MOTOR_FF)
                .outputRange(-1, 1);

        ClosedLoopConfig bottomPIDConfig = new ClosedLoopConfig()
                .pidf(SHOOTER.BOTTOM_MOTOR_P, SHOOTER.BOTTOM_MOTOR_I, SHOOTER.BOTTOM_MOTOR_D, SHOOTER.BOTTOM_MOTOR_FF)
                .outputRange(-1, 1);

        SparkBaseConfig topMotorConfig = new SparkMaxConfig()
                .inverted(SHOOTER.TOP_MOTOR_INVERTED).openLoopRampRate(SHOOTER.RAMP_RATE).voltageCompensation(12)
                .idleMode(SHOOTER.IDLE_MODE)
                .smartCurrentLimit(SHOOTER.TOP_MOTOR_STALL_LIMIT_AMPS, SHOOTER.TOP_MOTOR_FREE_LIMIT_AMPS)
                .apply(topPIDConfig);

        SparkBaseConfig bottomMotorConfig = new SparkMaxConfig()
                .inverted(SHOOTER.BOTTOM_MOTOR_INVERTED).openLoopRampRate(SHOOTER.RAMP_RATE).voltageCompensation(12)
                .idleMode(SHOOTER.IDLE_MODE)
                .smartCurrentLimit(SHOOTER.BOTTOM_MOTOR_STALL_LIMIT_AMPS, SHOOTER.BOTTOM_MOTOR_FREE_LIMIT_AMPS)
                .apply(bottomPIDConfig);

        m_topShooterMotor.configure(topMotorConfig, ResetMode.kNoResetSafeParameters, PersistMode.kNoPersistParameters);
        m_bottomShooterMotor.configure(bottomMotorConfig, ResetMode.kNoResetSafeParameters,
                PersistMode.kNoPersistParameters);
    }

    public void setMotorVelocity(AngularVelocity vel) {
        m_targetVelocity = vel;
        m_topPIDController.setReference(vel.in(Units.RPM), ControlType.kVelocity);
        m_bottomPIDController.setReference(vel.in(Units.RPM), ControlType.kVelocity);
    }

    public void setOutputVelocity(AngularVelocity vel) {
        setMotorVelocity(vel.times(SHOOTER.GEAR_RATIO));
    }

    public void setAxisSpeed(double speed) {
        m_targetVelocity = null;
        speed *= Constants.SHOOTER.SHOOTER_AXIS_MAX_SPEED;
        m_topShooterMotor.set(speed);
        m_bottomShooterMotor.set(speed);
    }

    public void stop() {
        m_targetVelocity = null;
        m_topShooterMotor.set(0.0);
        m_bottomShooterMotor.set(0.0);
    }

    public double getTopVelocity() {
        return m_topShooterMotor.getEncoder().getVelocity();
    }

    public double getBottomVelocity() {
        return m_bottomShooterMotor.getEncoder().getVelocity();
    }

    public boolean isAtVelocity(AngularVelocity vel) {
        return Utility.isWithinTolerance(getTopVelocity(), vel.in(Units.RPM), SHOOTER.RPM_TOLERANCE)
                && Utility.isWithinTolerance(getBottomVelocity(), vel.in(Units.RPM), SHOOTER.RPM_TOLERANCE);
    }

    public boolean isAtTargetSpeed() {
        return isAtVelocity(m_targetVelocity);
    }
}
