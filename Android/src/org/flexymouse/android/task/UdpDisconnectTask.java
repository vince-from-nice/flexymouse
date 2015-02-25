package org.flexymouse.android.task;

import org.flexymouse.android.MainActivity;

import android.os.AsyncTask;
import android.widget.Toast;

/********************************************************************************************/
/** Task classes */
/********************************************************************************************/

public class UdpDisconnectTask extends AsyncTask<String, Void, String> {

    private final MainActivity activity;

    public String result;

    public UdpDisconnectTask(MainActivity mainActivity) {
        activity = mainActivity;
    }

    @Override
    protected String doInBackground(String... stringData) {
        if (activity.getUdpSocket().isConnected()) {
            activity.getUdpSocket().disconnect();
            result = "Disconnected";
        } else {
            result = "Already disconnected";
        }
        // Update UI (need to do that in the main thread)
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                UdpDisconnectTask.this.activity.updateGuiForUdpConnection();
                Toast.makeText(activity, result, Toast.LENGTH_SHORT).show();
            }
        });
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        System.out.println(result);
    }
}