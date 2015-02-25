package org.flexymouse.android.mode;

import android.hardware.Sensor;

public class MagneticFieldSensorMode extends AbstractSensorMode {

    public static final String NAME = "MAGNETIC_FIELD";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public int getPrimarySensorType() {
        return Sensor.TYPE_MAGNETIC_FIELD;
    }

    @Override
    public String getRawUnit() {
        return "μT";
    }

    @Override
    public String getLabelForRawX() {
        return "Magnetic field along X";
    }

    @Override
    public String getLabelForRawY() {
        return "Magnetic field along Y";
    }

    @Override
    public String getLabelForRawZ() {
        return "Magnetic field along Z";
    }

    @Override
    public int getMinRawX() {
        return -10;
    }

    @Override
    public int getMaxRawX() {
        return 10;
    }

    @Override
    public int getMinRawY() {
        return -10;
    }

    @Override
    public int getMaxRawY() {
        return 10;
    }

    @Override
    public int getMinRawZ() {
        return -10;
    }

    @Override
    public int getMaxRawZ() {
        return 10;
    }
}
