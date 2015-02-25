package org.flexymouse.android.mode;

public abstract class AbstractSensorMode {

    abstract public String getName();

    abstract public int getPrimarySensorType();

    abstract public String getRawUnit();

    abstract public String getLabelForRawX();

    abstract public String getLabelForRawY();

    abstract public String getLabelForRawZ();

    abstract public int getMinRawX();

    abstract public int getMaxRawX();

    abstract public int getMinRawY();

    abstract public int getMaxRawY();

    abstract public int getMinRawZ();

    abstract public int getMaxRawZ();

    public void transformValues(float[] primaryInput, float[] secondaryInput, float[] origin, boolean remapToOrigin,
            float[] result) {
        result[0] = primaryInput[0];
        result[1] = primaryInput[1];
        result[2] = primaryInput[2];
        // Eventually remap coordinates to a custom origin 
        if (remapToOrigin && origin != null) {
            result[0] -= origin[0];
            result[1] -= origin[1];
            result[2] -= origin[2];
        }
        // Need to adapt values within the final ranges
        if (result[0] < getMinFinalX()) {
            result[0] = result[0] + (getMaxFinalX() - getMinFinalX());
        } else if (result[0] > getMaxFinalX()) {
            result[0] = result[0] - (getMaxFinalX() - getMinFinalX());
        }
        if (result[1] < getMinFinalY()) {
            result[1] = result[1] + (getMaxFinalY() - getMinFinalY());
        } else if (result[1] > getMaxFinalY()) {
            result[1] = result[1] - (getMaxFinalY() - getMinFinalY());
        }
        if (result[2] < getMinFinalZ()) {
            result[2] = result[2] + (getMaxFinalZ() - getMinFinalZ());
        } else if (result[2] > getMaxFinalZ()) {
            result[2] = result[2] - (getMaxFinalZ() - getMinFinalZ());
        }
    }

    /*
     * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     * / Following methods are overrided by Orientation_V2 mode only
     * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     */
    
    public int getSecondarySensorType() {
        return 0;
    }

    public String getFinalUnit() {
        return getRawUnit();
    }
    
    public String getLabelForFinalX() {
        return getLabelForRawX();
    }
    
    public String getLabelForFinalY() {
        return getLabelForRawY();
    }
    
    public String getLabelForFinalZ() {
        return getLabelForRawZ();
    }

    public int getMinFinalX() {
        return getMinRawX();
    }

    public int getMaxFinalX() {
        return getMaxRawX();
    }

    public int getMinFinalY() {
        return getMinRawY();
    }

    public int getMaxFinalY() {
        return getMaxRawY();
    }

    public int getMinFinalZ() {
        return getMinRawZ();
    }

    public int getMaxFinalZ() {
        return getMaxRawZ();
    }

}
