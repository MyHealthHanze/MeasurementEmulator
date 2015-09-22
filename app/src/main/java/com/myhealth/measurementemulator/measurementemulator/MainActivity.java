package com.myhealth.measurementemulator.measurementemulator;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    // Debugging tag
    public static final String TAG = MainActivity.class.getSimpleName();

    // The Connector class
    private BluetoothConnector connector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Turn Bluetooth on
     *
     * @param view
     */
    public void turnOnBluetooth(View view) {
        if (connector != null) {
            connector.stop();
        } else {
            connector = new BluetoothConnector();
        }
        new Thread(connector).start();
    }

    /**
     * Inner class to implement listening to incoming bluetooth connections
     */
    private class BluetoothConnector implements Runnable {

        // The UUID for connecting
        private static final String UUID_STRING = "34824060-611f-11e5-a837-0800200c9a66";
        // The server socket
        private BluetoothServerSocket tmp;
        // The bluetooth socket
        private BluetoothSocket connection;

        @Override
        public void run() {
            // Create a bluetooth adapter and turn it on
            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (!mBluetoothAdapter.isEnabled()) {
                mBluetoothAdapter.enable();
            }
            try {
                // Listen to incoming connections
                tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord("MeasurementEmulator", UUID.fromString(UUID_STRING));
                giveStatusUpdate(getString(R.string.listening_started));
                connection = tmp.accept(30000);
                tmp.close();
                giveStatusUpdate(getString(R.string.connection_success));
                // Test data
                connection.getOutputStream().write("Hello from the other device!".getBytes());
            } catch (final IOException e) {
                Log.d(MainActivity.TAG, e.getMessage());
            }
        }

        /**
         * Stop trying to connect
         */
        public void stop() {
            if (tmp != null) {
                try {
                    tmp.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * Give a status update using Toast.
         *
         * @param text The text to display
         */
        private void giveStatusUpdate(final String text) {
            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
