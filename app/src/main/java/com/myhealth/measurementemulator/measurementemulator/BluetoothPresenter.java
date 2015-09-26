package com.myhealth.measurementemulator.measurementemulator;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import java.io.IOException;
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
        try {
            connector.sendString("Hello again from the other device\n");
            connector.cancel();
        } catch (IOException e) {
            // Do nothing
        }
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
                Log.d(TAG, e.getMessage());
            }
        }

        /**
         * Send a string of data over the bluetooth connection
         *
         * @param data The data to send
         */
        public void sendString(String data) throws IOException {
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
