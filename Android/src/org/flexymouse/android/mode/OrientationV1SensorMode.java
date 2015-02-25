package org.flexymouse.android.mode;

import android.hardware.Sensor;

public class OrientationV1SensorMode extends AbstractSensorMode {

    public static final String NAME = "ORIENTATION_V1";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public int getPrimarySensorType() {
        return Sensor.TYPE_ORIENTATION;
    }

    @Override
    public String getRawUnit() {
        return "°";
    }

    @Override
    public String getLabelForRawX() {
        return "Azimuth";
    }

    @Override
    public String getLabelForRawY() {
        return "Pitch";
    }

    @Override
    public String getLabelForRawZ() {
        return "Roll";
    }

    @Override
    public int getMinRawX() {
        return 0;
    }

    @Override
    public int getMaxRawX() {
        return 360;
    }

    @Override
    public int getMinRawY() {
        return -180;
    }

    @Override
    public int getMaxRawY() {
        return 180;
    }

    @Override
    public int getMinRawZ() {
        return -90;
    }

    @Override
    public int getMaxRawZ() {
        return 90;
    }
    
    public int getMinFinalX() {
        return -180;
    }

    public int getMaxFinalX() {
        return 180;
    }

    public int getMinFinalY() {
        return -180;
    }

    public int getMaxFinalY() {
        return 180;
    }

    public int getMinFinalZ() {
        return -90;
    }

    public int getMaxFinalZ() {
        return 90;
    }

}
