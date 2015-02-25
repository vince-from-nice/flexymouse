package org.flexymouse.android.mode;

import android.hardware.Sensor;

public class GameRotationVectorSensorMode extends AbstractSensorMode {

    public static final String NAME = "GAME_ROTATION_VECTOR";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public int getPrimarySensorType() {
        return Sensor.TYPE_GAME_ROTATION_VECTOR;
    }

    @Override
    public String getRawUnit() {
        return "";
    }

    @Override
    public String getLabelForRawX() {
        return "Rotation along X";
    }

    @Override
    public String getLabelForRawY() {
        return "Rotation along Y";
    }

    @Override
    public String getLabelForRawZ() {
        return "Rotation along Z";
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
