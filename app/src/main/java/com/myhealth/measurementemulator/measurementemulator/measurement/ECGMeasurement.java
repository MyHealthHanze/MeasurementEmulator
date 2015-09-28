package com.myhealth.measurementemulator.measurementemulator.measurement;

/**
 * Created by Sander on 28-9-2015.
 * Class to hold an ECG measurement
 */
public class ECGMeasurement extends BaseMeasurement {

    // The ECG
    private Double[] ecg;

    /**
     * Get the ECG
     *
     * @return The eCG
     */
    public Double[] getEcg() {
        return this.ecg;
    }

    /**
     * Set the ECT
     *
     * @param ecg The new ECG
     */
    public void setEcg(Double[] ecg) {
        this.ecg = ecg;
    }


}
