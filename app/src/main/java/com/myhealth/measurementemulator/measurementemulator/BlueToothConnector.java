package com.myhealth.measurementemulator.measurementemulator;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by Sander on 9/22/2015.
 */
public class BlueToothConnector implements Runnable {

    private static final String UUID_STRING = "34824060-611f-11e5-a837-0800200c9a66";

    @Override
    public void run() {
        Log.d(MainActivity.TAG, "Result");
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothServerSocket tmp = null;
        try {
            tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord("MeasurementEmulator", UUID.fromString(UUID_STRING));
            BluetoothSocket socket = tmp.accept();
            Log.d(MainActivity.TAG, "Connected!");
            socket.close();
            TextView text = (TextView) new Activity().findViewById(R.id.mainText);
            text.setText("Connected!");
        } catch (IOException e) {
        }
    }
}
