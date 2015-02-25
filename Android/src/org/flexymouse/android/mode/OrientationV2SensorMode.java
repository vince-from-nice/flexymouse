package org.flexymouse.android.mode;

import android.hardware.Sensor;
import android.hardware.SensorManager;

public class OrientationV2SensorMode extends AbstractSensorMode {

    public static final String NAME = "ORIENTATION_V2";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public int getPrimarySensorType() {
        return Sensor.TYPE_ACCELEROMETER;
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

    @Override
    public void transformValues(float[] primaryInput, float[] secondaryInput, float[] origin, boolean remapToOrigin, float[] result) {
        float[] rotationMatrix = new float[9];
        float[] inclinationMatrix = new float[9];
        float[] newRotationMatrix = new float[9];
        float[] values = new float[3];
        SensorManager.getRotationMatrix(rotationMatrix, inclinationMatrix, primaryInput, secondaryInput);
        if (remapToOrigin && origin != null) {
            SensorManager.remapCoordinateSystem(rotationMatrix, SensorManager.AXIS_Y, SensorManager.AXIS_Z, newRotationMatrix);
        } else {
            newRotationMatrix = rotationMatrix;
        }
        SensorManager.getOrientation(newRotationMatrix, values);
        result[0] = (float) Math.toDegrees(values[0]);
        result[1] = (float) Math.toDegrees(values[1]);
        result[2] = (float) Math.toDegrees(values[2]);
    }

    public int getSecondarySensorType() {
        return Sensor.TYPE_MAGNETIC_FIELD;
    }
    
    public String getFinalUnit() {
        return "°";
    }
    
    @Override
    public String getLabelForFinalX() {
        return "Azimuth";
    }

    @Override
    public String getLabelForFinalY() {
        return "Pitch";
    }

    @Override
    public String getLabelForFinalZ() {
        return "Roll";
    }
    
    public int getMinFinalX() {
        return 0;
    }

    public int getMaxFinalX() {
        return 360;
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
