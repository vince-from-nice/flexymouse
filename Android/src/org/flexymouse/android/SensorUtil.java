/*
 * Created on 20 sept. 2014
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.flexymouse.android;

import java.util.List;

import android.hardware.Sensor;
import android.hardware.SensorManager;

public class SensorUtil {
    
    static public String listSensor(SensorManager sensorManager) {
        List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
        StringBuffer sensorDesc = new StringBuffer();
        for (Sensor sensor : sensors) {
            sensorDesc.append("Name: " + sensor.getName() + "\r\n");
            sensorDesc.append("Type: " + getSensorType(sensor.getType()) + "\r\n");
            sensorDesc.append("Version: " + sensor.getVersion() + "\r\n");
            sensorDesc.append("Resolution (in the sensor unit): " + sensor.getResolution() + "\r\n");
            sensorDesc.append("Power in mA used by this sensor while in use" + sensor.getPower() + "\r\n");
            sensorDesc.append("Vendor: " + sensor.getVendor() + "\r\n");
            sensorDesc.append("Maximum range of the sensor in the sensor's unit." + sensor.getMaximumRange() + "\r\n");
            sensorDesc.append("Minimum delay allowed between two events in microsecond"
                    + " or zero if this sensor only returns a value when the data it's measuring changes"
                    + sensor.getMinDelay() + "\r\n");
            sensorDesc.append("\r\n");
        }
        return sensorDesc.toString();
    }

    static public String getSensorType(int type) {
        String strType;
        switch (type) {
        case Sensor.TYPE_ACCELEROMETER:
            strType = "TYPE_ACCELEROMETER";
            break;
        case Sensor.TYPE_GRAVITY:
            strType = "TYPE_GRAVITY";
            break;
        case Sensor.TYPE_GYROSCOPE:
            strType = "TYPE_GYROSCOPE";
            break;
        case Sensor.TYPE_LIGHT:
            strType = "TYPE_LIGHT";
            break;
        case Sensor.TYPE_LINEAR_ACCELERATION:
            strType = "TYPE_LINEAR_ACCELERATION";
            break;
        case Sensor.TYPE_MAGNETIC_FIELD:
            strType = "TYPE_MAGNETIC_FIELD";
            break;
        case Sensor.TYPE_ORIENTATION:
            strType = "TYPE_ORIENTATION";
            break;
        case Sensor.TYPE_PRESSURE:
            strType = "TYPE_PRESSURE";
            break;
        case Sensor.TYPE_PROXIMITY:
            strType = "TYPE_PROXIMITY";
            break;
        case Sensor.TYPE_ROTATION_VECTOR:
            strType = "TYPE_ROTATION_VECTOR";
            break;
        case Sensor.TYPE_TEMPERATURE:
            strType = "TYPE_TEMPERATURE";
            break;
        default:
            strType = "TYPE_UNKNOW";
            break;
        }
        return strType;
    }
}
