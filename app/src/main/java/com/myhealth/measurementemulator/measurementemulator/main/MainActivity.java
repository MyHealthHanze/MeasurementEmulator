package com.myhealth.measurementemulator.measurementemulator.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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
     * Send data over bluetooth
     *
     * @param view The view that triggered this method
     */
    public void sendGeneratedData(View view) {
        presenter.generateAndSendData();
    }

    /**
     * Set the possibility of sending data
     */
    public void setSendDataVisibility(final int visibility) {
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
    public void setStatus(final String status) {
        runOnUiThread(new Runnable() {
            public void run() {
                TextView text = (TextView) findViewById(R.id.status_text);
                text.setText(status);
            }
        });
    }
}
