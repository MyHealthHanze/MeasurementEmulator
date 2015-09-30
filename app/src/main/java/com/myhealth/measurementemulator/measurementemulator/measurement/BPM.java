package com.myhealth.measurementemulator.measurementemulator.measurement;

import java.util.Random;

/**
 * @author Jitse
 * A class to generate random Beats per minute values.
 */
public class BPM {

    private static final int MINIMUM_BPM = 35;
    private static final int MAXIMUM_BPM = 130;

    /**
     * Generates random BPM values.
     *
     * @return a random  BPM value.
     */
    public int getNewBPM(){
        Random random = new Random();
        return MINIMUM_BPM + random.nextInt(MAXIMUM_BPM-MINIMUM_BPM);
    }
}
