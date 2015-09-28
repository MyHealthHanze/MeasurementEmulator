package com.myhealth.measurementemulator.measurementemulator.main;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.myhealth.measurementemulator.measurementemulator.R;
import com.myhealth.measurementemulator.measurementemulator.measurement.BP;
import com.myhealth.measurementemulator.measurementemulator.measurement.BPM;
import com.myhealth.measurementemulator.measurementemulator.measurement.BPMMeasurement;
import com.myhealth.measurementemulator.measurementemulator.measurement.BPMeasurement;
import com.myhealth.measurementemulator.measurementemulator.measurement.ECG;
import com.myhealth.measurementemulator.measurementemulator.measurement.ECGMeasurement;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by Sander on 26-9-2015.
 * A class to handle the logic of the bluetooth connection
 */
public class BluetoothPresenter {

    // Logging tag
    private static final String TAG = MainActivity.class.getSimpleName();
    // The amount of time to be discoverable
    private static final int DISCOVERABLE_TIME = 30;
    //duration of ECG in deciseconds
    private static final int ECG_DURATION = 40;
    // The default status message
    private String defaultStatus;
    // The activity to manage
    private MainActivity activity;
    // The connector
    private BlueToothConnector connector;
    // The GSon object
    private Gson gson;

    /**
     * Create a bluetoothpresenter
     *
     * @param activity The activity to control
     */
    public BluetoothPresenter(MainActivity activity) {
        this.activity = activity;
        defaultStatus = activity.getString(R.string.press) + activity.getString(R.string.bluetooth_string) + activity.getString(R.string.incoming_connection);
        activity.setStatus(defaultStatus);
        gson = new Gson();
    }

    /**
     * Turn discovery on
     */
    public void turnDiscoveryOn() {
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, DISCOVERABLE_TIME);
        activity.startActivityForResult(discoverableIntent, 1);
    }

    /**
     * Start listening for an incoming connection
     */
    public void startListening() {
        if (connector != null) {
            connector.cancel();
        } else {
            connector = new BlueToothConnector();
        }
        new Thread(connector).start();
    }

    /**
     * Generate ECG data and send it
     */
    public void generateAndSendECG() {
        ECGMeasurement ms = new ECGMeasurement();
        List<Double> list = new ECG().getNewECG(ECG_DURATION);
        ms.setMeasurementValue(list.toArray(new Double[list.size()]));
        ms.setMeasurementDate(getDateAsString());
        try {
            connector.sendString(gson.toJson(ms));
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
        }
    }

    /**
     * Generate BPM data and send it
     */
    public void generateAndSendBPM() {
        BPMMeasurement ms = new BPMMeasurement();
        ms.setMeasurementValue(new BPM().getNewBPM());
        ms.setMeasurementDate(getDateAsString());
        try {
            connector.sendString(gson.toJson(ms));
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
        }
    }

    /**
     * Generate BP data and send it
     */
    public void generateAndSendBP() {
        BPMeasurement ms = new BPMeasurement();
        ms.setMeasurementValue(new BP().getNewBP());
        ms.setMeasurementDate(getDateAsString());
        try {
            connector.sendString(gson.toJson(ms));
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
        }
    }

    /**
     * Disconnect from the device
     */
    public void disconnectFromDevice() {
        try {
            connector.sendString("\n");
            connector.cancel();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
        }
    }

    /**
     * Get the current date and time as a String
     *
     * @return The current date and time as a String
     */
    private String getDateAsString() {
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        dateFormatter.setLenient(false);
        return dateFormatter.format(new Date());
    }

    /**
     * Inner class to handle an incoming connection
     */
    private class BlueToothConnector implements Runnable {

        // Unique identifier necessary for a connection
        private static final String UUID_STRING = "34824060-611f-11e5-a837-0800200c9a66";
        // The socket to send data over
        private BluetoothSocket socket;

        /**
         * Wait for an incoming connection
         */
        @Override
        public void run() {
            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            BluetoothServerSocket tmp;
            try {
                activity.setStatus(activity.getString(R.string.waiting_connection));
                tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord("MeasurementEmulator", UUID.fromString(UUID_STRING));
                socket = tmp.accept(30000);
                tmp.close();
                mBluetoothAdapter.cancelDiscovery();
                activity.setSendDataVisibility(View.VISIBLE);
                activity.setStatus(activity.getString(R.string.connected_to) + socket.getRemoteDevice().getName());
            } catch (IOException e) {
                cancel();
                Log.d(TAG, e.toString());
            }
        }

        /**
         * Send a string of data over the bluetooth connection
         *
         * @param data The data to send
         */
        public void sendString(String data) throws IOException {
            data = data.replace("\n", "") + "\n";
//            if (!data.endsWith("\n")) {
//                data += "\n";
//            }
            socket.getOutputStream().write(data.getBytes());
        }

        /**
         * Cancel an ongoing connection
         */
        public void cancel() {
            try {
                socket.close();
                activity.setSendDataVisibility(View.INVISIBLE);
                activity.setStatus(defaultStatus);
            } catch (Exception e) {
                // Do nothing
            }
        }
    }
}
