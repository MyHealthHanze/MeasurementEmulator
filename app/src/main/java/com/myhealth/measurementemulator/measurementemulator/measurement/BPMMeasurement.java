package com.myhealth.measurementemulator.measurementemulator.measurement;

/**
 * Created by Sander on 28-9-2015.
 * Class to hold a BPM measurement
 */
public class BPMMeasurement extends BaseMeasurement {
    // The BPM
    private int measurementValue;

    /**
     * Get the BPM
     *
     * @return The BPM
     */
    public int getMeasurementValue() {
        return this.measurementValue;
    }

    /**
     * Set the BPM
     *
     * @param measurementValue The new BPM
     */
    public void setMeasurementValue(int measurementValue) {
        this.measurementValue = measurementValue;
    }
}
