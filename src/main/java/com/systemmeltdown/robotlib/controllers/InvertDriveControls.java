package com.systemmeltdown.robotlib.controllers;

import com.systemmeltdown.robotlib.subsystems.TogglableLimelightSubsystem;
import com.systemmeltdown.robotlib.commands.InvertDriveCommand;

import com.systemmeltdown.robotlib.controllers.DriverControls;
import com.systemmeltdown.robotlib.subsystems.drive.FalconTrajectoryDriveSubsystem;
import com.systemmeltdown.robotlib.subsystems.drive.SingleSpeedFalconDriveSubsystem;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

import com.systemmeltdown.robotlib.util.Utility;
import com.systemmeltdown.robotlib.util.XboxRaw;
import com.systemmeltdown.robotlog.topics.BooleanTopic;
import com.systemmeltdown.robotlib.commands.VisionChangePipelineCommand;

/**
 * These extend {@link DriverControls} so these are the Driver's controls, adapted to support the
 * {@link InvertDriveCommand}.
 * 
 * @category Drive
 */
public class InvertDriveControls extends DriverControls {
    public final JoystickButton m_invertButton;
    public final JoystickButton m_changePipelineButton;
    private boolean m_isToggled = false;

    private final BooleanTopic isInvertedTopic = new BooleanTopic("Are Controls Inverted");

    public InvertDriveControls(InvertDriveControlsBuilder builder) {
        super(builder.m_controller, builder.m_deadband);
        m_invertButton = new JoystickButton(builder.m_controller, XboxRaw.A.value);
        m_changePipelineButton = new JoystickButton(builder.m_controller, XboxRaw.Back.value);
    }

    /**
     * Changes the value of m_isToggled from true to false or vice versa
     */
    public void invert() {
        isInvertedTopic.log(m_isToggled);
        m_isToggled = !m_isToggled;
        isInvertedTopic.log(m_isToggled);
    }
    
    @Override
    public double getSpeed() {
        double speed = super.getSpeed();
        speed = inputCurve(speed, 2);
        return m_isToggled ? speed : -speed;
    }

    @Override
    public double getTurn() {
        double turn = super.getTurn();
        turn = -inputCurve(super.getTurn(), 4);
        return Utility.clamp(turn, -.7, .7);
    }

    //I would put a javadoc here, but I really don't understand it. If you do, please delete this and put a javadoc here.
    public double inputCurve(double input, int curveFactor) {
        return Math.signum(input) * Math.abs(Math.pow(input, curveFactor));
    }

    /**
     * The builder for the InvertDriveControls
     */
    public static class InvertDriveControlsBuilder {
        private XboxController m_controller = null;
        private double m_deadband = 0.0;
        private SingleSpeedFalconDriveSubsystem m_driveSubsystem = null;
        private TogglableLimelightSubsystem m_visionSubsystem = null;

        /**
         * @param controller The driver's {@link XboxController}.
         * @param deadband The deadband for the driver's controller.
         */
        public InvertDriveControlsBuilder(XboxController controller, double deadband) {
            this.m_controller = controller;
            this.m_deadband = deadband;
        }

        public InvertDriveControlsBuilder withDriveSub(SingleSpeedFalconDriveSubsystem driveSubsystem){
            this.m_driveSubsystem = driveSubsystem;
            return this;
        }

        public InvertDriveControlsBuilder withVisionSub(TogglableLimelightSubsystem visionSubsystem) {
            this.m_visionSubsystem = visionSubsystem;
            return this;
        }

        public InvertDriveControls build() {
            InvertDriveControls m_driverControls = new InvertDriveControls(this);
            if (m_visionSubsystem != null) {
                m_driverControls.m_changePipelineButton.whileHeld(new VisionChangePipelineCommand(m_visionSubsystem));
            }
            if (m_driveSubsystem != null && m_visionSubsystem != null) {
                m_driverControls.m_invertButton.whenPressed(new InvertDriveCommand(m_visionSubsystem, m_driverControls));
            }
            return m_driverControls;
        }
    }
}