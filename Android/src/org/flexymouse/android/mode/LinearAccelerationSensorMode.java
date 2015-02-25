package org.flexymouse.android.mode;

import android.hardware.Sensor;

public class LinearAccelerationSensorMode extends AbstractSensorMode {

    public static final String NAME = "LINEAR_ACCELEROMETER";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public int getPrimarySensorType() {
        return Sensor.TYPE_LINEAR_ACCELERATION;
    }

    @Override
    public String getRawUnit() {
        return "m²/s";
    }

    @Override
    public String getLabelForRawX() {
        return "X axis force";
    }

    @Override
    public String getLabelForRawY() {
        return "Y axis force";
    }

    @Override
    public String getLabelForRawZ() {
        return "Z axis force";
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
