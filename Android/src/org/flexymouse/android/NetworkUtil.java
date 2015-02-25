/*
 * Created on 1 oct. 2014
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.flexymouse.android;

public class NetworkUtil {
    
    static private byte[] convertArrayOfIntToArrayOfBytes(int i) {
        byte[] result = new byte[4];

        result[0] = (byte) (i >> 24);
        result[1] = (byte) (i >> 16);
        result[2] = (byte) (i >> 8);
        result[3] = (byte) (i /* >> 0 */);

        return result;
    }

    static private byte[] convertArrayOfIntToArrayOfBytes(int x, int y, int z) {
        byte[] result = new byte[12];

        result[0] = (byte) (x >> 24);
        result[1] = (byte) (x >> 16);
        result[2] = (byte) (x >> 8);
        result[3] = (byte) (x /* >> 0 */);

        result[4] = (byte) (y >> 24);
        result[5] = (byte) (y >> 16);
        result[6] = (byte) (y >> 8);
        result[7] = (byte) (y /* >> 0 */);

        result[8] = (byte) (z >> 24);
        result[9] = (byte) (z >> 16);
        result[10] = (byte) (z >> 8);
        result[11] = (byte) (z /* >> 0 */);

        return result;
    }
}
