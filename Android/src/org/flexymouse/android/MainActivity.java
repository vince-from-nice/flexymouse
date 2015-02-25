package org.flexymouse.android;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.flexymouse.R;
import org.flexymouse.android.mode.AbstractSensorMode;
import org.flexymouse.android.mode.AccelerometerSensorMode;
import org.flexymouse.android.mode.GameRotationVectorSensorMode;
import org.flexymouse.android.mode.GeomagneticRotationVectorSensorMode;
import org.flexymouse.android.mode.GravitySensorMode;
import org.flexymouse.android.mode.GyroscopeSensorMode;
import org.flexymouse.android.mode.LinearAccelerationSensorMode;
import org.flexymouse.android.mode.MagneticFieldSensorMode;
import org.flexymouse.android.mode.OrientationV1SensorMode;
import org.flexymouse.android.mode.OrientationV2SensorMode;
import org.flexymouse.android.mode.RotationVectorSensorMode;
import org.flexymouse.android.task.UdpConnectTask;
import org.flexymouse.android.task.UdpDisconnectTask;
import org.flexymouse.android.task.UdpSendTask;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements SensorEventListener, OnItemSelectedListener {

    /********************************************************************************************/
    /** Sensors fields */
    /********************************************************************************************/

    private SensorManager sensorManager;

    private AbstractSensorMode selectedSensorMode;

    static private Map<String, AbstractSensorMode> sensorModes;

    private Sensor currentPrimarySensor;

    private Sensor currentSecondaySensor;

    // private int sensorRate = SensorManager.SENSOR_DELAY_NORMAL;
    // private int sensorRate = SensorManager.SENSOR_DELAY_GAME;
    // private int sensorRate = SensorManager.SENSOR_DELAY_FASTEST;
    private int sensorRate = SensorManager.SENSOR_DELAY_UI;

    /********************************************************************************************/
    /** Sensor data fields */
    /********************************************************************************************/

    // private Float azimuth, pitch, roll;

    private float[] primaryRawVector = new float[3];

    private float[] secondaryRawVector = new float[3];

    private float[] originVector = null;

    private float[] finalVector = new float[3];

    /********************************************************************************************/
    /** Widgets fields */
    /********************************************************************************************/

    private ProgressBar progressBarRawX, progressBarRawY, progressBarRawZ;

    private ProgressBar progressBarComputedX, progressBarComputedY, progressBarComputedZ;

    /********************************************************************************************/
    /** Networking fields */
    /********************************************************************************************/

    DatagramSocket udpSocket;

    private InetAddress udpHost;

    private int udpPort;

    BluetoothAdapter bluetoothAdapter;

    /********************************************************************************************/
    /** Miscellaneous fields */
    /********************************************************************************************/

    private boolean needToResetOrigin = false;

    /********************************************************************************************/
    /** Static fields */
    /********************************************************************************************/

    static private final String DEFAULT_SENSOR_MODE_NAME = OrientationV1SensorMode.NAME;

    static private final int ENABLE_BLUETOOTH_REQUEST_CODE = 69;

    /********************************************************************************************/
    /** Life cycle methods */
    /********************************************************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize sensor modes
        sensorModes = new HashMap<String, AbstractSensorMode>();
        sensorModes.put(AccelerometerSensorMode.NAME, new AccelerometerSensorMode());
        sensorModes.put(LinearAccelerationSensorMode.NAME, new LinearAccelerationSensorMode());
        sensorModes.put(GameRotationVectorSensorMode.NAME, new GameRotationVectorSensorMode());
        sensorModes.put(GeomagneticRotationVectorSensorMode.NAME, new GeomagneticRotationVectorSensorMode());
        sensorModes.put(GyroscopeSensorMode.NAME, new GyroscopeSensorMode());
        sensorModes.put(GravitySensorMode.NAME, new GravitySensorMode());
        sensorModes.put(MagneticFieldSensorMode.NAME, new MagneticFieldSensorMode());
        sensorModes.put(OrientationV1SensorMode.NAME, new OrientationV1SensorMode());
        sensorModes.put(OrientationV2SensorMode.NAME, new OrientationV2SensorMode());
        sensorModes.put(RotationVectorSensorMode.NAME, new RotationVectorSensorMode());

        // Initialize default sensor mode
        this.sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        this.selectedSensorMode = sensorModes.get(DEFAULT_SENSOR_MODE_NAME);
        if (selectedSensorMode.getPrimarySensorType() > 0) {
            this.currentPrimarySensor = sensorManager.getDefaultSensor(selectedSensorMode.getPrimarySensorType());
            this.sensorManager.registerListener(this, this.currentPrimarySensor, sensorRate);
        }
        if (selectedSensorMode.getSecondarySensorType() > 0) {
            this.currentSecondaySensor = sensorManager.getDefaultSensor(selectedSensorMode.getSecondarySensorType());
            this.sensorManager.registerListener(this, this.currentSecondaySensor, sensorRate);
        }

        // Initialize sensors spinner
        Spinner sensorSpinner = (Spinner) findViewById(R.id.spinnerSensorMode);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sensors_array,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sensorSpinner.setAdapter(adapter);
        sensorSpinner.setOnItemSelectedListener(this);
        sensorSpinner.setSelection(adapter.getPosition(DEFAULT_SENSOR_MODE_NAME));

        // Initialize progress bars
        this.progressBarRawX = (ProgressBar) findViewById(R.id.progressBarRawX);
        this.progressBarRawY = (ProgressBar) findViewById(R.id.progressBarRawY);
        this.progressBarRawZ = (ProgressBar) findViewById(R.id.progressBarRawZ);
        this.progressBarRawX.setMax(this.selectedSensorMode.getMaxRawX() - this.selectedSensorMode.getMinRawX());
        this.progressBarRawY.setMax(this.selectedSensorMode.getMaxRawY() - this.selectedSensorMode.getMinRawY());
        this.progressBarRawZ.setMax(this.selectedSensorMode.getMaxRawZ() - this.selectedSensorMode.getMinRawZ());
        this.progressBarComputedX = (ProgressBar) findViewById(R.id.progressBarComputedX);
        this.progressBarComputedY = (ProgressBar) findViewById(R.id.progressBarComputedY);
        this.progressBarComputedZ = (ProgressBar) findViewById(R.id.progressBarComputedZ);
        this.progressBarComputedX.setMax(360);
        this.progressBarComputedY.setMax(360);
        this.progressBarComputedZ.setMax(360);

        // Initialize networking (UDP)
        try {
            this.udpSocket = new DatagramSocket();
        } catch (SocketException e) {
            Toast.makeText(MainActivity.this, "Unable to create socket : " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        updateGuiForUdpConnection();

        // Initialize Bluetooth
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(MainActivity.this, "Unable to get Bluetooth adapter", Toast.LENGTH_LONG).show();
        }
        if (this.bluetoothAdapter.isEnabled()) {
            ((CheckBox) findViewById(R.id.checkboxActivateBluetooth)).setChecked(true);
        } else {
            //((ViewGroup) findViewById(R.id.viewsForBluetoothEnabled)).setVisibility(View.GONE);
            ((Button) findViewById(R.id.buttonBluetoothListPairedDevices)).setClickable(false);
            ((Button) findViewById(R.id.buttonBluetoothListPairedDevices)).setEnabled(false);
            ((Button) findViewById(R.id.buttonBluetoothBeDiscoverable)).setClickable(false);
            ((Button) findViewById(R.id.buttonBluetoothBeDiscoverable)).setEnabled(false);
        }
    }

    @Override
    protected void onPause() {
        sensorManager.unregisterListener(this, this.currentPrimarySensor);
        sensorManager.unregisterListener(this, this.currentSecondaySensor);
        super.onPause();
    }

    @Override
    protected void onResume() {
        sensorManager.registerListener(this, this.currentPrimarySensor, sensorRate);
        sensorManager.registerListener(this, this.currentSecondaySensor, sensorRate);
        super.onResume();
    }

    /********************************************************************************************/
    /** UI events methods */
    /********************************************************************************************/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.buttongluar) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onWidgetButtonClick(View view) {
        int button = 0;
        if (view.getId() == R.id.button1) {
            button = 1;
        } else if (view.getId() == R.id.button2) {
            button = 2;
        }
        System.out.println("Button " + button + " has been clicked !!");
        // Send data to the listener
        if (udpSocket.isConnected()) {
            new UdpSendTask(this).execute("BUTTON " + button);
        }
    }

    public void onRecenterButtonClick(View view) {
        this.needToResetOrigin = true;
    }
    
    public void onUdpButtonClick(View view) {
        if (view.getId() == R.id.buttonUdpConnect) {
            UdpConnectTask task = new UdpConnectTask(this);
            task.execute("");
        } else if (view.getId() == R.id.buttonUdpDisconnect) {
            UdpDisconnectTask task = new UdpDisconnectTask(this);
            task.execute("");
        }
    }

    public void onBluetoothButtonClick(View view) {
        if (view.getId() == R.id.buttonBluetoothBeDiscoverable) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        } else if (view.getId() == R.id.buttonBluetoothListPairedDevices) {
            Set<BluetoothDevice> pairedDevices = this.bluetoothAdapter.getBondedDevices();
            // If there are paired devices
            if (pairedDevices.size() > 0) {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
                // Display device list into a dialog
                ListView listView = new ListView(this);
                for (BluetoothDevice device : pairedDevices) {
                    adapter.add(device.getAddress() + " " + device.getName());
                }
                listView.setAdapter(adapter);
                Dialog dialog = new Dialog(this);
                dialog.setTitle("Paired devices");
                dialog.setContentView(listView);
                dialog.show();
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String s = (String) parent.getItemAtPosition(position);
        // Sensor modes spinner
        for (AbstractSensorMode m : MainActivity.sensorModes.values()) {
            if (m.getName().equals(s)) {
                this.selectedSensorMode = m;
                // Unregister old sensor(s)
                if (this.currentPrimarySensor != null) {
                    sensorManager.unregisterListener(this, this.currentPrimarySensor);
                }
                if (this.currentSecondaySensor != null) {
                    sensorManager.unregisterListener(this, this.currentSecondaySensor);
                }
                // Register new sensor(s)
                if (m.getPrimarySensorType() > 0) {
                    this.currentPrimarySensor = sensorManager.getDefaultSensor(m.getPrimarySensorType());
                    sensorManager.registerListener(this, this.currentPrimarySensor, sensorRate);
                }
                if (m.getSecondarySensorType() > 0) {
                    this.currentSecondaySensor = sensorManager.getDefaultSensor(m.getSecondarySensorType());
                    sensorManager.registerListener(this, this.currentSecondaySensor, sensorRate);
                }
                // Update UI (progress bar's maximums and labels)
                this.progressBarRawX.setMax(m.getMaxRawX() - m.getMinRawX());
                this.progressBarRawY.setMax(m.getMaxRawY() - m.getMinRawY());
                this.progressBarRawZ.setMax(m.getMaxRawZ() - m.getMinRawZ());
                this.progressBarComputedX.setMax(m.getMaxFinalX() - m.getMinFinalX());
                this.progressBarComputedY.setMax(m.getMaxFinalY() - m.getMinFinalY());
                this.progressBarComputedZ.setMax(m.getMaxFinalZ() - m.getMinFinalZ());
                ((TextView) findViewById(R.id.textViewLabelForX)).setText(m.getLabelForRawX());
                ((TextView) findViewById(R.id.textViewLabelForY)).setText(m.getLabelForRawY());
                ((TextView) findViewById(R.id.textViewLabelForZ)).setText(m.getLabelForRawZ());
                ((TextView) findViewById(R.id.textViewLabelForFinalX)).setText(m.getLabelForFinalX());
                ((TextView) findViewById(R.id.textViewLabelForFinalY)).setText(m.getLabelForFinalY());
                ((TextView) findViewById(R.id.textViewLabelForFinalZ)).setText(m.getLabelForFinalZ());
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // nothing..
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();
        switch (view.getId()) {
        // "Use origin" checkbox
        case R.id.checkboxUseOrigin:
            if (checked) {
                System.out.println("Checkbox 'Use Origin' is checked");
            } else {
                System.out.println("Checkbox 'Use Origin' is not checked");
            }
            break;
        // Bluetooth checkbox
        case R.id.checkboxActivateBluetooth:
            if (checked) {
                System.out.println("Checkbox 'Activate Bluetooth' is checked");
                if (!this.bluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, ENABLE_BLUETOOTH_REQUEST_CODE);
                    System.out.println("Bluetooth is enabled");
                }
                ((Button) findViewById(R.id.buttonBluetoothListPairedDevices)).setClickable(true);
                ((Button) findViewById(R.id.buttonBluetoothListPairedDevices)).setEnabled(true);
                ((Button) findViewById(R.id.buttonBluetoothBeDiscoverable)).setClickable(true);
                ((Button) findViewById(R.id.buttonBluetoothBeDiscoverable)).setEnabled(true);
                //((ViewGroup) findViewById(R.id.viewsForBluetoothEnabled)).setVisibility(View.VISIBLE);
            } else {
                System.out.println("Checkbox 'Activate Bluetooth' is unchecked");
                if (this.bluetoothAdapter.isEnabled()) {
                    this.bluetoothAdapter.disable();
                    System.out.println("Bluetooth is disabled");
                }
                //((ViewGroup) findViewById(R.id.viewsForBluetoothEnabled)).setVisibility(View.GONE);
                ((Button) findViewById(R.id.buttonBluetoothListPairedDevices)).setClickable(false);
                ((Button) findViewById(R.id.buttonBluetoothListPairedDevices)).setEnabled(false);
                ((Button) findViewById(R.id.buttonBluetoothBeDiscoverable)).setClickable(false);
                ((Button) findViewById(R.id.buttonBluetoothBeDiscoverable)).setEnabled(false);

            }
            break;
        }
    }

    public void updateGuiForUdpConnection() {
        TextView tv = (TextView) findViewById(R.id.textViewUdpConnectionState);
        ViewGroup viewsForUdpConnect = (ViewGroup) findViewById(R.id.viewsForUdpNotConnected);
        ViewGroup viewsForUdpDisconnect = (ViewGroup) findViewById(R.id.viewsForUdpConnected);
        if (this.udpSocket.isConnected()) {
            viewsForUdpConnect.setVisibility(View.GONE);
            viewsForUdpDisconnect.setVisibility(View.VISIBLE);
            String address = udpHost.getHostAddress() + ":" + udpPort;
            tv.setText("You're connected to " + address);
            tv.setTextColor(Color.GREEN);
        } else {
            viewsForUdpDisconnect.setVisibility(View.GONE);
            viewsForUdpConnect.setVisibility(View.VISIBLE);
            tv.setText("You are currently not connected");
            tv.setTextColor(Color.RED);
        }

    }

    /********************************************************************************************/
    /** Sensor events methods */
    /********************************************************************************************/

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // Looks if the event is relevant
        if (event.sensor.getType() != this.selectedSensorMode.getPrimarySensorType()
                && event.sensor.getType() != this.selectedSensorMode.getSecondarySensorType()) {
            return;
        }

        // Primary raw values
        if (event.sensor.getType() == this.selectedSensorMode.getPrimarySensorType()) {
            // Store raw values into the concerned raw
            this.primaryRawVector = event.values.clone();
            // Update UI for raw values labels
            ((TextView) findViewById(R.id.textViewRawX)).setText("" + (int) this.primaryRawVector[0] + " "
                    + this.selectedSensorMode.getRawUnit());
            ((TextView) findViewById(R.id.textViewRawY)).setText("" + (int) this.primaryRawVector[1] + " "
                    + this.selectedSensorMode.getRawUnit());
            ((TextView) findViewById(R.id.textViewRawZ)).setText("" + (int) this.primaryRawVector[2] + " "
                    + this.selectedSensorMode.getRawUnit());
            System.out.println("gluar : " + this.primaryRawVector[0] + " " + this.primaryRawVector[1] + " " + this.primaryRawVector[2]);
            // Update progress bars with adjusted values
            int deltaX = 0, deltaY = 0, deltaZ = 0;
            if (this.selectedSensorMode.getMinRawX() < 0) {
                deltaX = (this.selectedSensorMode.getMaxRawX() - this.selectedSensorMode.getMinRawX()) / 2;
            }
            if (this.selectedSensorMode.getMinRawY() < 0) {
                deltaY = (this.selectedSensorMode.getMaxRawY() - this.selectedSensorMode.getMinRawY()) / 2;
            }
            if (this.selectedSensorMode.getMinRawZ() < 0) {
                deltaZ = (this.selectedSensorMode.getMaxRawZ() - this.selectedSensorMode.getMinRawZ()) / 2;
            }
            this.progressBarRawX.setProgress((int) this.primaryRawVector[0] + deltaX);
            this.progressBarRawY.setProgress((int) this.primaryRawVector[1] + deltaY);
            this.progressBarRawZ.setProgress((int) this.primaryRawVector[2] + deltaZ);
        }
        // Secondary raw values
        else {
            // Store raw values into the concerned raw
            this.secondaryRawVector = event.values.clone();
        }

        // Initialize the original orientation vector if it's not initialized yet or if "recenter" flag is on
        if (this.originVector == null || this.needToResetOrigin) {
            // Apparently the first event has only 0 value, ignore it..
            if (event.values[0] != 0 && event.values[1] != 0 && event.values[2] != 0) {
                this.originVector = event.values.clone();
                this.needToResetOrigin = false;
                ((TextView) findViewById(R.id.textViewCurrentOrigin)).setText("" + (int) this.originVector[0] + "  "
                        + (int) this.originVector[1] + "  " + (int) this.originVector[2]);
            }
        }

        // Compute final values
        this.selectedSensorMode.transformValues(this.primaryRawVector, this.secondaryRawVector, this.originVector,
                ((CheckBox) findViewById(R.id.checkboxUseOrigin)).isChecked(), this.finalVector);
        // Update UI for computed values labels
        ((TextView) findViewById(R.id.textViewComputedX)).setText("" + (int) this.finalVector[0] + ""
                + this.selectedSensorMode.getFinalUnit());
        ((TextView) findViewById(R.id.textViewComputedY)).setText("" + (int) this.finalVector[1] + ""
                + this.selectedSensorMode.getFinalUnit());
        ((TextView) findViewById(R.id.textViewComputedZ)).setText("" + (int) this.finalVector[2] + ""
                + this.selectedSensorMode.getFinalUnit());
        // Update progress bars with adjusted values
        int deltaX = 0, deltaY = 0, deltaZ = 0;
        if (this.selectedSensorMode.getMinFinalX() < 0) {
            deltaX = (this.selectedSensorMode.getMaxFinalX() - this.selectedSensorMode.getMinFinalX()) / 2;
        }
        if (this.selectedSensorMode.getMinFinalY() < 0) {
            deltaY = (this.selectedSensorMode.getMaxFinalY() - this.selectedSensorMode.getMinFinalY()) / 2;
        }
        if (this.selectedSensorMode.getMinFinalZ() < 0) {
            deltaZ = (this.selectedSensorMode.getMaxFinalZ() - this.selectedSensorMode.getMinFinalZ()) / 2;
        }
        this.progressBarComputedX.setProgress((int) this.finalVector[0] + deltaX);
        this.progressBarComputedY.setProgress((int) this.finalVector[1] + deltaY);
        this.progressBarComputedZ.setProgress((int) this.finalVector[2] + deltaZ);

        // Send data to the listener
        if (this.udpSocket.isConnected()) {
            UdpSendTask task = new UdpSendTask(this);
            task.execute(this.selectedSensorMode.getName() + " " + ((int) this.finalVector[0])
                    + this.selectedSensorMode.getFinalUnit() + " " + ((int) this.finalVector[1])
                    + this.selectedSensorMode.getFinalUnit() + " " + ((int) this.finalVector[2])
                    + this.selectedSensorMode.getFinalUnit() + " ");
        }
    }

    /********************************************************************************************/
    /** Gettes & Setters */
    /********************************************************************************************/

    public DatagramSocket getUdpSocket() {
        return udpSocket;
    }

    public void setUdpSocket(DatagramSocket udpSocket) {
        this.udpSocket = udpSocket;
    }

    public InetAddress getUdpHost() {
        return udpHost;
    }

    public void setUdpHost(InetAddress udpHost) {
        this.udpHost = udpHost;
    }

    public int getUdpPort() {
        return udpPort;
    }

    public void setUdpPort(int udpPort) {
        this.udpPort = udpPort;
    }

}
