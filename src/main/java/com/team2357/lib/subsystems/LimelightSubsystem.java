/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.team2357.lib.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

/**
 * Controls the limelight camera options.
 */
public class LimelightSubsystem extends ClosedLoopSubsystem {
  /**
   * This class represents a snapshot of an acquired target.
   */
  public static class VisionTarget {
    private final double m_Ts;
    private final double m_Thor;
    private final double m_Tvert;
    private final double m_Tx;
    private final double m_Ty;
    private final Configuration m_Config;
    private final double m_TargetHeightFromFloor;

    protected VisionTarget(Configuration config, double targetHeightFromFloor, double ts, double thor, double tvert, double tx, double ty) {
      m_Config = config;
      m_TargetHeightFromFloor = targetHeightFromFloor;
      m_Ts = ts;
      m_Thor = thor;
      m_Tvert = tvert;
      m_Tx = tx;
      m_Ty = ty;
    }

    public double getX() {
      return m_Tx;
    }

    public double getY() {
      return m_Ty;
    }

    /** Skew of target in degrees. Positive values are to the left, negative to the right */
    public double getSkew() {
      if(m_Ts < -45) {
        return m_Ts + 90;
      }
      else
      {
        return m_Ts;
      }
    }

    public boolean isHeadOn() {
      double skew = getSkew();
      return -m_Config.m_HeadOnTolerance <= skew && skew <= m_Config.m_HeadOnTolerance;
    }

    public boolean isToLeft() {
      return getSkew() > m_Config.m_HeadOnTolerance;
    }

    public boolean isToRight() {
      return getSkew() < m_Config.m_HeadOnTolerance;
    }

    public double getTargetRotationDegrees() {
      if (isHeadOn()) {
        return 0.0;
      }
      else if (isToLeft()) {
        return -getRotationAngle();
      }
      else {
        return getRotationAngle();
      }
    }

    private double getRotationAngle() {
      double proportion = m_Thor / m_Tvert;
      double factor = proportion * m_Config.m_TargetHeight / m_Config.m_TargetWidth;
      return 90.0 * (1 - factor);
    }

    public double getInchesFromTarget() {
      double angleDegrees = Math.abs(m_Ty) + m_Config.m_LimelightMountingAngle;

      double heightDifference = m_Config.m_LimelightMountingHeightInches - m_TargetHeightFromFloor;
      double distance = heightDifference / Math.tan(Math.toRadians(angleDegrees));

      return distance;
    }
  }

  public static class Configuration {
    /** Angle of the Limelight axis from horizontal (degrees) */
    public double m_LimelightMountingAngle = 0;

    /** Height of the Limelight lens center from the floor (inches) */
    public double m_LimelightMountingHeightInches = 0;

    /** Default value to return if the camera can't be polled */
    public double m_DefaultReturnValue = 0;

    /** Tolerance in degrees for skew to be considered head on */
    public double m_HeadOnTolerance = 1e-4;

    /** Target width in inches */
    public double m_TargetWidth = 1;

    /** Target height in inches */
    public double m_TargetHeight = 1;
  }

  protected NetworkTable m_Table = NetworkTableInstance.getDefault().getTable("limelight");
  private NetworkTableEntry m_Pipeline = m_Table.getEntry("pipeline");
  private NetworkTableEntry m_Tv = m_Table.getEntry("tv");
  private NetworkTableEntry m_Tx = m_Table.getEntry("tx");
  private NetworkTableEntry m_Ty = m_Table.getEntry("ty");
  private NetworkTableEntry m_Ta = m_Table.getEntry("ta");
  private NetworkTableEntry m_Ts = m_Table.getEntry("ts");
  private NetworkTableEntry m_Thor = m_Table.getEntry("thor");
  private NetworkTableEntry m_Tvert = m_Table.getEntry("tvert");

  private VisionTarget m_CurrentTarget = null;
  private VisionTarget m_LastVisibleTarget = null;

  private Configuration m_Configuration = new Configuration();

  public void setConfiguration(Configuration configuration) {
    m_Configuration = configuration;
  }

  public VisionTarget getCurrentTarget() {
    return m_CurrentTarget;
  }

  public VisionTarget getLastVisibleTarget() {
    return m_LastVisibleTarget;
  }

  /**
   * Acquire a target. Returns null if no target is in view.
   * @param targetHeightFromFloor Height of the target from the floor. Used in distance calculation.
   */
  public VisionTarget acquireTarget(double targetHeightFromFloor) {
    if (0 < getTV()) {
      m_CurrentTarget = new VisionTarget(m_Configuration, targetHeightFromFloor, getTS(), getTHOR(), getTVERT(), getTX(), getTY());
      m_LastVisibleTarget = m_CurrentTarget;
    } else {
      m_CurrentTarget = null;
    }
    return m_CurrentTarget;
  }

  public int getPipeline() {
    double value = m_Pipeline.getDouble(Double.NaN);
    return (int)Math.round(value);
  }

  public void setPipeline(int index) {
    m_Pipeline.setDouble((double)index);
  }

  /**
   * Whether the camera has a valid target
   * @return 1 for true, 0 for false
   */
  public double getTV() {
    return m_Tv.getDouble(m_Configuration.m_DefaultReturnValue);
  }

  /** Horizontal offset from crosshair to target (degrees) */
  public double getTX() {
    return m_Tx.getDouble(m_Configuration.m_DefaultReturnValue);
  }

  /** Vertical offset from crosshair to target (degrees) */
  public double getTY() {
    return m_Ty.getDouble(m_Configuration.m_DefaultReturnValue);
  }

  /** Percent of image covered by target [0, 100] */
  public double getTA() {
    return m_Ta.getDouble(m_Configuration.m_DefaultReturnValue);
  }

  /** Skew or rotation (degrees, [-90, 0]) */
  public double getTS() {
    return m_Ts.getDouble(m_Configuration.m_DefaultReturnValue);
  }

  /** Horizontal sidelength of rough bounding box (0 - 320 pixels) */
  public double getTHOR() {
    return m_Thor.getDouble(m_Configuration.m_DefaultReturnValue);
  }

  /** Vertical sidelength of rough bounding box (0 - 320 pixels) */
  public double getTVERT() {
    return m_Tvert.getDouble(m_Configuration.m_DefaultReturnValue);
  }
}
