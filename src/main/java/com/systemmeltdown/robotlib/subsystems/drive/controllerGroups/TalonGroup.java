package com.systemmeltdown.robotlib.subsystems.drive.controllerGroups;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class TalonGroup {
    WPI_TalonSRX m_masterTalon;
    WPI_TalonSRX[] m_slaveTalons;

    public TalonGroup(int masterTalonID, int[] slaveTalonIDs) {
        m_masterTalon = new WPI_TalonSRX(masterTalonID);
        m_slaveTalons = new WPI_TalonSRX[slaveTalonIDs.length];

        for (int i = 0; i < slaveTalonIDs.length; i++) {
            m_slaveTalons[i] = new WPI_TalonSRX(slaveTalonIDs[i]);
            m_slaveTalons[i].follow(m_masterTalon);
        }
    }

    public WPI_TalonSRX getMasterTalon() {
        return m_masterTalon;
    }

    public WPI_TalonSRX[] getSlaveTalons() {
        return m_slaveTalons;
    }

    public void configure(boolean isInverted) {
        m_masterTalon.configFactoryDefault();
        m_masterTalon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
        m_masterTalon.setInverted(isInverted);

        for (WPI_TalonSRX slaveTalon : m_slaveTalons) {
            slaveTalon.configFactoryDefault();
            slaveTalon.setInverted(isInverted);
        }
    }

    public void set(ControlMode mode, double value) {
        m_masterTalon.set(mode, value);
    }

    public int getSelectedSensorPosition() {
        return m_masterTalon.getSelectedSensorPosition();
    }
}