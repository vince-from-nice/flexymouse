package org.flexymouse.android.task;

import java.net.DatagramSocket;
import java.net.InetAddress;

import org.flexymouse.R;
import org.flexymouse.android.MainActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

public class UdpConnectTask extends AsyncTask<String, Void, String> {

    public MainActivity activity;

    public String result;

    static private final int DEFAULT_LISTENER_PORT = 1069;

    public UdpConnectTask(MainActivity actvity) {
        this.activity = actvity;
    }

    @Override
    protected String doInBackground(String... stringData) {
        // Connect UDP socket
        ConnectivityManager connMgr = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            try {
                if (activity.getUdpSocket() == null) {
                    activity.setUdpSocket(new DatagramSocket());
                }
                activity.setUdpHost(InetAddress.getByName(((TextView) activity
                        .findViewById(R.id.editTextListenerAddress)).getText().toString()));
                activity.setUdpPort(DEFAULT_LISTENER_PORT);
                activity.getUdpSocket().connect(activity.getUdpHost(), activity.getUdpPort());
                result = "Connected";
            } catch (Exception e) {
                result = "Unable to connect" + (e.getMessage() != null ? " : " + e.getMessage() : "");
            }
        } else {
            result = "Network is not available";
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, result, Toast.LENGTH_SHORT).show();
                }
            });
        }
        
        // Update UI (need to do that in the main thread)
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.updateGuiForUdpConnection();
            }
        });
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        System.out.println(result);
    }
}
