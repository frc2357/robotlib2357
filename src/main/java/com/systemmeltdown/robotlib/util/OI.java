package com.systemmeltdown.robotlib.util;

import com.systemmeltdown.robotlib.controllers.GunnerControls;
import com.systemmeltdown.robotlib.controllers.DriverControls;
import com.systemmeltdown.robotlib.commands.ProportionalDrive;

public class OI implements ProportionalDrive{

    private boolean driverSlow;

    private DriverControls driverControls;
    private GunnerControls gunnerControls;
    private int lastEncoderSpeed;
      
    public OI(){
        this.lastEncoderSpeed = 0;
        this.driverSlow = false;
    }

    public double getProportionalSpeed() {
        double speed = 0;
    
        speed += driverControls.getProportionalSpeed();
        speed += gunnerControls.getProportionalSpeed();
    
        speed = Utility.clamp(speed, -1.0, 1.0);
    
        return speed;
      }


      public double getProportionalTurn() {
        double turn = 0;
    
        turn += driverControls.getProportionalTurn();
        turn += gunnerControls.getProportionalTurn();
    
        turn = Utility.clamp(turn, -1.0, 1.0);
    
        return turn;
      }
      public boolean isDriverSlow() {
        return driverSlow;
      }

      public boolean isGunnerDriving() {
        return gunnerControls.getEncoderTurnDifferential() != 0 || gunnerControls.getEncoderSpeed() != 0;
      }

      public int getEncoderTurnDifferential() {
        int turn = 0;
    
        if (isGunnerDriving()) {
          turn = gunnerControls.getEncoderTurnDifferential();
        } else {
          turn = driverControls.getEncoderTurnDifferential();
        }
    
        turn = Utility.clamp(turn, -RobotMap.DRIVER_ENCODER_TURN_RATE, RobotMap.DRIVER_ENCODER_TURN_RATE);
    
        return turn;
      }
        
  public int getEncoderSpeed() {
    int speed = 0;

    if (isGunnerDriving()) {
      speed = gunnerControls.getEncoderSpeed();
    } else {
      speed = driverControls.getEncoderSpeed();
    }

    speed = Utility.clamp(speed, -RobotMap.DRIVER_ENCODER_SPEED, RobotMap.DRIVER_ENCODER_SPEED);

    // Limit the input speed on forward motion (to avoid tipping)
    double limitFactor = RobotMap.DRIVER_ENCODER_MAX_FORWARD_LIMIT_FACTOR;

    // Default is max from zero forward (reverse accel doesn't matter)
    int maxDiff = RobotMap.DRIVER_ENCODER_MAX_DIFF;

    if (speed - lastEncoderSpeed > maxDiff) {
      // Forward accel is too fast.
      int max = maxDiff;

      if (lastEncoderSpeed > 0) {
        // Limit forward acceleration.
        max = (int)(lastEncoderSpeed * limitFactor);
      } else if (lastEncoderSpeed < 0) {
        // Limit reverse deceleration.
        max = (int)(lastEncoderSpeed / limitFactor);
        max = max > -maxDiff ? 0 : max;
      }

      if (speed > max) {
        speed = max;
      }
    }

    lastEncoderSpeed = speed;
    return speed;
  }

}