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
import com.myhealth.measurementemulator.measurementemulator.measurement.ECG;
import com.myhealth.measurementemulator.measurementemulator.measurement.Measurement;

import java.io.IOException;
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
    private int duration = 40;
    // The default status message
    private String defaultStatus;
    // The activity to manage
    private MainActivity activity;
    // The connector
    private BlueToothConnector connector;

    /**
     * Create a bluetoothpresenter
     *
     * @param activity The activity to control
     */
    public BluetoothPresenter(MainActivity activity) {
        this.activity = activity;
        defaultStatus = activity.getString(R.string.press) + activity.getString(R.string.bluetooth_string) + activity.getString(R.string.incoming_connection);
        activity.setStatus(defaultStatus);
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
     * Generate data and send it
     */
    public void generateData() {
        Measurement measurement = createMeasurement();
        Gson gson = new Gson();
        String json = gson.toJson(measurement);
        try {
            connector.sendString(json);
            connector.cancel();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
        }
    }

    private Measurement createMeasurement() {
        Measurement measurement = new Measurement();
        //generate BMP
        measurement.setBpm(new BPM().getNewBPM());
        //generate BP
        measurement.setBp(new BP().getNewBP());
        //generate ECG
        List<Double> list = new ECG().getNewECG(duration);
        Double[] ecgArray = list.toArray(new Double[list.size()]);
        measurement.setEcg(ecgArray);
        return measurement;
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
                Log.d(TAG, e.toString());
            }
        }

        /**
         * Send a string of data over the bluetooth connection
         *
         * @param data The data to send
         */
        public void sendString(String data) throws IOException {
            if (!data.endsWith("\n")) {
                data += "\n";
            }
            socket.getOutputStream().write(data.getBytes());
            // Always send an empty line to let the other side know we're done
            socket.getOutputStream().write("\n".getBytes());
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