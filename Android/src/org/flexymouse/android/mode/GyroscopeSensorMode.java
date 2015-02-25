package org.flexymouse.android.mode;

import android.hardware.Sensor;

public class GyroscopeSensorMode extends AbstractSensorMode {

    public static final String NAME = "GYROSCOPE";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public int getPrimarySensorType() {
        return Sensor.TYPE_GYROSCOPE;
    }

    @Override
    public String getRawUnit() {
        return "rad/s";
    }

    @Override
    public String getLabelForRawX() {
        return "X rotation rate";
    }

    @Override
    public String getLabelForRawY() {
        return "Y rotation rate";
    }

    @Override
    public String getLabelForRawZ() {
        return "Z rotation rate";
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
