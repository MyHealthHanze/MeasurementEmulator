package com.myhealth.measurementemulator.measurementemulator.measurement;

/**
 * Created by Sander on 28-9-2015.
 * A class for storing a BPM measurement
 */
public class BPMeasurement extends BaseMeasurement {
    // The BP. The first position is systolic, the second diastolic
    private int[] bp;

    /**
     * Get the BP
     *
     * @return the BP
     */
    public int[] getBp() {
        return this.bp;
    }

    /**
     * Set the BP
     *
     * @param bp the new BP
     */
    public void setBp(int[] bp) {
        this.bp = bp;
    }

}
