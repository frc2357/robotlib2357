package com.systemmeltdown.robotlib.subsystems.drive;

public class TalonIDGroup {
    int m_masterTalonID;
    int[] m_slaveTalonIDs;
    boolean m_invert;

    public TalonIDGroup(int masterTalonID, int[] slaveTalonIDs, boolean invert) {
        m_masterTalonID = masterTalonID;
        m_slaveTalonIDs = slaveTalonIDs;
        m_invert = invert;
    }

    public int getMasterTalonID() {
        return m_masterTalonID;
    }

    public int[] getSlaveTalonIDs() {
        return m_slaveTalonIDs;
    }

    public boolean getInvert() {
        return m_invert;
    }
}