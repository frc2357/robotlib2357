package com.systemmeltdown.robotlib.subsystems.drive.controllerGroups;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

public class FalconGroup {
    TalonFX m_masterFalcon;
    TalonFX[] m_slaveFalcons;

    public FalconGroup(int masterFalconID, int[] slaveFalconIDs) {
        m_masterFalcon = new TalonFX(masterFalconID);
        m_slaveFalcons = new TalonFX[slaveFalconIDs.length];

        // for (int i = 0; i < slaveFalconIDs.length; i++) {
        //     m_slaveFalcons[i] = new TalonFX(slaveFalconIDs[i]);
        //     m_slaveFalcons[i].follow(m_masterFalcon);
        // }
    }

    public TalonFX getMasterFalcon() {
        return m_masterFalcon;
    }

    public TalonFX[] getSlaveFalcons() {
        return m_slaveFalcons;
    }

    public void configure(boolean isInverted) {
        m_masterFalcon.configFactoryDefault();
        m_masterFalcon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
        m_masterFalcon.setInverted(isInverted);

        // for (TalonFX slaveFalcon : m_slaveFalcons) {
        //     slaveFalcon.configFactoryDefault();
        //     slaveFalcon.setInverted(isInverted);
        // }
    }

    public void set(ControlMode mode, double value) {
        m_masterFalcon.set(mode, value);
    }

    public int getSelectedSensorPosition() {
        return m_masterFalcon.getSelectedSensorPosition();
    }
}