package common;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import common.OI;
import common.RobotTemplate;
import common.RobotMap;
import common.Utility;

public class DriverControls extends Controls implements ProportionalDrive, VelocityDrive {
  public final DriverSlowTrigger slowTrigger;

  public DriverControls(XboxController controller) {
    super(controller);

    slowTrigger = new DriverSlowTrigger(controller);
  }

  @Override
  public int getEncoderTurnDifferential() {
    double input = Utility.deadband(controller.getX(Hand.kRight), RobotMap.DRIVE_STICK_DEADBAND);
    int encoderTurn = RobotTemplate.OI.isDriverSlow() ? RobotMap.DRIVER_ENCODER_SLOW_TURN_RATE : RobotMap.DRIVER_ENCODER_TURN_RATE;
    int turnRate = (int)(input * encoderTurn);
    return turnRate;
  }

  @Override
  public int getEncoderSpeed() {

    double input = Utility.deadband(controller.getY(Hand.kLeft), RobotMap.DRIVE_STICK_DEADBAND);
    int encoderSpeed = RobotTemplate.OI.isDriverSlow() ? RobotMap.DRIVER_ENCODER_SLOW_SPEED : RobotMap.DRIVER_ENCODER_SPEED;
    int speed = (int)(-input * encoderSpeed);
    return speed;
  }

  @Override
  public double getProportionalTurn() {
    return controller.getX(Hand.kRight) * RobotMap.DRIVER_TURN_PROPORTION;
  }

  @Override
  public double getProportionalSpeed() {
    return - (controller.getY(Hand.kLeft) * RobotMap.DRIVER_SPEED_PROPORTION);
  }
}