package org.flexymouse.android.mode;

import android.hardware.Sensor;

public class GravitySensorMode extends AbstractSensorMode {

    public static final String NAME = "GRAVITY";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public int getPrimarySensorType() {
        return Sensor.TYPE_GRAVITY;
    }

    @Override
    public String getRawUnit() {
        return "m²/s";
    }

    @Override
    public String getLabelForRawX() {
        return "Gravity around X";
    }

    @Override
    public String getLabelForRawY() {
        return "Gravity around Y";
    }

    @Override
    public String getLabelForRawZ() {
        return "Gravity around Z";
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
