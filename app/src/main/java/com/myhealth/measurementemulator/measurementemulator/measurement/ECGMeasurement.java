package com.myhealth.measurementemulator.measurementemulator.measurement;

/**
 * Created by Sander on 28-9-2015.
 * Class to hold an ECG measurement
 */
public class ECGMeasurement extends BaseMeasurement {

    // The ECG
    private Double[] measurementValue;

    /**
     * Get the ECG
     *
     * @return The eCG
     */
    public Double[] getMeasurementValue() {
        return this.measurementValue;
    }

    /**
     * Set the ECT
     *
     * @param measurementValue The new ECG
     */
    public void setMeasurementValue(Double[] measurementValue) {
        this.measurementValue = measurementValue;
    }


}
