package com.myhealth.measurementemulator.measurementemulator.measurement;

/**
 * Created by Sander on 28-9-2015.
 * Class to hold a BPM measurement
 */
public class BPMMeasurement extends BaseMeasurement {
    // The BPM
    private int bpm;

    /**
     * Get the BPM
     *
     * @return The BPM
     */
    public int getBpm() {
        return this.bpm;
    }

    /**
     * Set the BPM
     *
     * @param bpm The new BPM
     */
    public void setBpm(int bpm) {
        this.bpm = bpm;
    }
}
