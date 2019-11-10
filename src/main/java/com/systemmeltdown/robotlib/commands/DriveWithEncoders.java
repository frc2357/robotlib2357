/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package com.systemmeltdown.robotlib.commands;

import edu.wpi.first.wpilibj.command.Command;
import com.systemmeltdown.robotlib.util.OI;
import com.systemmeltdown.robotlib.util.RobotTemplate;

public class DriveWithEncoders extends Command {

  public DriveWithEncoders() {
    requires(RobotTemplate.DRIVE_SUB);
  }

  @Override
  protected void execute() {
    int turn = RobotTemplate.OI.getEncoderTurnDifferential();
    int speed = RobotTemplate.OI.getEncoderSpeed();
    RobotTemplate.DRIVE_SUB.PIDDrive(speed, turn);

    /*
    System.out.println(
      "left: " + Robot.DRIVE_SUB.leftMaster.getClosedLoopError() + "|" + ((int)(Robot.DRIVE_SUB.leftMaster.getMotorOutputPercent()*100.0)) +
      " right: " + Robot.DRIVE_SUB.rightMaster.getClosedLoopError() + "|" + ((int)(Robot.DRIVE_SUB.rightMaster.getMotorOutputPercent()*100.0))
    );*/
    
  }

  @Override
  protected boolean isFinished() {
    return false;
  }
} 