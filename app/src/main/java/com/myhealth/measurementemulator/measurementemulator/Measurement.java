package com.myhealth.measurementemulator.measurementemulator;

/**
 * @author Jitse
 * A class to store measurement values for easy Json conversion
 */
public class Measurement {
    private Double[] ecg;
    private int bpm;
    private int[] bp;

    public Double[] getEcg() {
        return this.ecg;
    }

    public void setEcg(Double[] ecg) {
        this.ecg = ecg;
    }

    public int getBpm() {
        return this.bpm;
    }

    public void setBpm(int bpm) {
        this.bpm = bpm;
    }

    public int[] getBp() {
        return this.bp;
    }

    public void setBp(int[] bp) {
        this.bp = bp;
    }
}
