package com.myhealth.measurementemulator.measurementemulator.measurement;

/**
 * Created by Sander on 28-9-2015.
 * A Base for other measurements, to hold the date and time
 */
public class BaseMeasurement {

    // The date and time of the measurement
    private String measurementDate;
    // The type of the measurement
    private String type;

    public BaseMeasurement(String type) {
        setType(type);
    }

    /**
     * Get the date and time of the measurement
     *
     * @return The date and time
     */
    public String getMeasurementDate() {
        return measurementDate;
    }

    /**
     * Set the date and time of the measurement
     *
     * @param measurementDate The date and time
     */
    public void setMeasurementDate(String measurementDate) {
        this.measurementDate = measurementDate;
    }

    /**
     * Get the type
     *
     * @return The type
     */
    public String getType() {
        return type;
    }

    /**
     * Set the type
     *
     * @param type The type
     */
    public void setType(String type) {
        this.type = type;
    }

}
