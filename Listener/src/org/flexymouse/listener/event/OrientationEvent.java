package org.flexymouse.listener.event;

public class OrientationEvent extends AbstractEvent {

    private float azimuth;

    private float pitch;

    private float roll;

    static public final String[] KEYWORDS = new String[] { "ORIENTATION_V1", "ORIENTATION_V2", "ORIENTATION_V3" };

    public static boolean containsKeyword(String keyword) {
        for (int i = 0; i < KEYWORDS.length; i++) {
            if (KEYWORDS[i].equals(keyword)) {
                return true;
            }
        }
        return false;
    }

    public float getAzimuth() {
        return azimuth;
    }

    public void setAzimuth(float azimuth) {
        this.azimuth = azimuth;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float getRoll() {
        return roll;
    }

    public void setRoll(float roll) {
        this.roll = roll;
    }

}
