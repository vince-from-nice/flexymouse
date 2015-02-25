package org.flexymouse.android.task;

import java.net.DatagramPacket;

import org.flexymouse.android.MainActivity;

import android.os.AsyncTask;
import android.widget.Toast;

public class UdpSendTask extends AsyncTask<String, Void, String> {

    private final MainActivity activity;

    public String result;
    
    static final int DEFAULT_PACKET_SIZE = 50;

    static final String BREAKING_STRING = "%%%";

    public UdpSendTask(MainActivity mainActivity) {
        this.activity = mainActivity;
    }
    
    @Override
    protected String doInBackground(String... stringData) {
        try {
            byte[] data = new byte[DEFAULT_PACKET_SIZE];
            if (stringData[0].length() > DEFAULT_PACKET_SIZE - 1) {
                return "Data cannot be sent because it's too big (" + stringData[0].length() + ")";
            }
            byte[] bytesData = (stringData[0] + BREAKING_STRING).getBytes();
            for (int i = 0; i < bytesData.length; i++) {
                data[i] = bytesData[i];
            }
            for (int i = bytesData.length; i < DEFAULT_PACKET_SIZE; i++) {
                data[i] = 0;
            }
            DatagramPacket sendPacket = new DatagramPacket(data, data.length/* , listenerHost, listenerPort */);
            this.activity.getUdpSocket().send(sendPacket);
            result = "Data sent : " + stringData[0];
        } catch (Exception e) {
            // Force disconnection on each exception
            this.activity.getUdpSocket().disconnect();
            System.err.println("Disconnecting UDP socket");
            result = "Disconnected : " + e.getMessage();
            // Update UI (need to do that in the main thread)
            this.activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    UdpSendTask.this.activity.updateGuiForUdpConnection();
                    Toast.makeText(activity, result, Toast.LENGTH_SHORT).show();
                }
            });
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        System.out.println(result);
    }
}