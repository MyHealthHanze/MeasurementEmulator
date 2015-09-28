package com.myhealth.measurementemulator.measurementemulator.measurement;

/**
 * Created by Sander on 28-9-2015.
 * A Base for other measurements, to hold the date and time
 */
public class BaseMeasurement {

    // The date and time of the measurement
    private String dateTime;

    /**
     * Get the date and time of the measurement
     *
     * @return The date and time
     */
    public String getDateTime() {
        return dateTime;
    }

    /**
     * Set the date and time of the measurement
     *
     * @param dateTime The date and time
     */
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

}
