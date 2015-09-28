package com.myhealth.measurementemulator.measurementemulator.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.myhealth.measurementemulator.measurementemulator.R;

/**
 * The first and only activity of Measurement Emulator
 */
public class MainActivity extends AppCompatActivity {

    // The connector thread
    private BluetoothPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter = new BluetoothPresenter(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        presenter.startListening();
    }

    /**
     * Make the device discoverable
     *
     * @param view The view that triggered the method
     */
    public void discoverBluetooth(View view) {
        presenter.turnDiscoveryOn();
    }

    /**
     * Set the possibility of sending data
     */
    public void setSendDataVisibility(final int visibility) {
        if (visibility != View.VISIBLE && visibility != View.INVISIBLE) return;
        runOnUiThread(new Runnable() {
            public void run() {
                findViewById(R.id.send_ecg_button).setVisibility(visibility);
                findViewById(R.id.send_bp_button).setVisibility(visibility);
                findViewById(R.id.send_bpm_button).setVisibility(visibility);
                findViewById(R.id.done_button).setVisibility(visibility);
                if (visibility == View.VISIBLE) {
                    findViewById(R.id.bluetooth_button).setVisibility(View.INVISIBLE);
                } else {
                    findViewById(R.id.bluetooth_button).setVisibility(View.VISIBLE);
                }
            }
        });
    }

    /**
     * Set the status message
     *
     * @param status The status message
     */
    public void setStatus(final String status) {
        runOnUiThread(new Runnable() {
            public void run() {
                TextView text = (TextView) findViewById(R.id.status_text);
                text.setText(status);
            }
        });
    }

    /**
     * Start sending an ECG measurement over bluetooth
     *
     * @param view The view that triggered this method
     */
    public void sendECGData(View view) {
        presenter.generateAndSendECG();
    }

    /**
     * Start sending a BPM measurement over bluetooth
     *
     * @param view The view that triggered this method
     */
    public void sendBPMData(View view) {
        presenter.generateAndSendBPM();
    }

    /**
     * Start sending a BP measurement over bluetooth
     *
     * @param view The view that triggered this method
     */
    public void sendBPData(View view) {
        presenter.generateAndSendBP();
    }

    /**
     * Disconnect from the device
     *
     * @param view The view that triggered this method
     */
    public void doneSendingData(View view) {
        presenter.disconnectFromDevice();
    }
}
