package com.myhealth.measurementemulator.measurementemulator.measurement;

/**
 * Created by Sander on 28-9-2015.
 * A class for storing a BPM measurement
 */
public class BPMeasurement extends BaseMeasurement {
    // The BP. The first position is systolic, the second diastolic
    private int[] measurementValue;

    /**
     * Get the BP
     *
     * @return the BP
     */
    public int[] getMeasurementValue() {
        return this.measurementValue;
    }

    /**
     * Set the BP
     *
     * @param measurementValue the new BP
     */
    public void setMeasurementValue(int[] measurementValue) {
        this.measurementValue = measurementValue;
    }

}
