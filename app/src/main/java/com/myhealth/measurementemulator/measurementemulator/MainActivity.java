package com.myhealth.measurementemulator.measurementemulator;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.UUID;

/**
 * The first an only activity of Measurement Emulator
 */
public class MainActivity extends AppCompatActivity {

    // Logging tag
    private static final String TAG = MainActivity.class.getSimpleName();
    // The amount of time to be discoverable
    private static final int DISCOVERABLE_TIME = 30;
    // The default status message
    private String defaultStatus;

    // The connector thread
    private BlueToothConnector connector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        defaultStatus = getString(R.string.press) + getString(R.string.bluetooth_string) + getString(R.string.incoming_connection);
        setStatus(defaultStatus);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (connector != null) {
            connector.cancel();
        }
        Log.d(TAG, "Start a new thread");
        connector = new BlueToothConnector();
        new Thread(connector).start();
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
     * Make the device discoverable
     *
     * @param view The view that triggered the method
     */
    public void discoverBlueTooth(View view) {
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, DISCOVERABLE_TIME);
        startActivityForResult(discoverableIntent, 1);
    }

    /**
     * Send data over bluetooth
     *
     * @param view The view that triggered this method
     */
    public void sendGeneratedData(View view) {
        try {
            connector.sendString("Hello from the other device!\n");
            connector.cancel();
        } catch (IOException e) {
            Log.d(TAG, e.getMessage());
        }
    }

    /**
     * Set the possibility of sending data
     */
    private void setSendDataVisibility(final int visibility) {
        if (visibility != View.VISIBLE && visibility != View.INVISIBLE) return;
        runOnUiThread(new Runnable() {
            public void run() {
                Button button = (Button) findViewById(R.id.send_button);
                button.setVisibility(visibility);
            }
        });
    }

    /**
     * Set the status message
     *
     * @param status The status message
     */
    private void setStatus(final String status) {
        runOnUiThread(new Runnable() {
            public void run() {
                TextView text = (TextView) findViewById(R.id.status_text);
                text.setText(status);
            }
        });
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
                setStatus(getString(R.string.waiting_connection));
                tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord("MeasurementEmulator", UUID.fromString(UUID_STRING));
                socket = tmp.accept(30000);
                tmp.close();
                mBluetoothAdapter.cancelDiscovery();
                setSendDataVisibility(View.VISIBLE);
                setStatus(getString(R.string.connected_to) + socket.getRemoteDevice().getName());
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
                setSendDataVisibility(View.INVISIBLE);
                setStatus(defaultStatus);
            } catch (Exception e) {
                // Do nothing
            }
        }
    }
}
